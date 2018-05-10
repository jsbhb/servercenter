package com.zm.order.bussiness.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.zm.order.bussiness.component.CapitalPoolThreadPool;
import com.zm.order.bussiness.component.ShareProfitComponent;
import com.zm.order.bussiness.dao.OrderMapper;
import com.zm.order.bussiness.service.CacheAbstractService;
import com.zm.order.bussiness.service.OrderService;
import com.zm.order.constants.Constants;
import com.zm.order.feignclient.ActivityFeignClient;
import com.zm.order.feignclient.GoodsFeignClient;
import com.zm.order.feignclient.PayFeignClient;
import com.zm.order.feignclient.SupplierFeignClient;
import com.zm.order.feignclient.UserFeignClient;
import com.zm.order.feignclient.model.GoodsConvert;
import com.zm.order.feignclient.model.GoodsFile;
import com.zm.order.feignclient.model.GoodsSpecs;
import com.zm.order.feignclient.model.OrderBussinessModel;
import com.zm.order.feignclient.model.PayModel;
import com.zm.order.feignclient.model.RefundPayModel;
import com.zm.order.feignclient.model.SendOrderResult;
import com.zm.order.pojo.CustomModel;
import com.zm.order.pojo.Express;
import com.zm.order.pojo.ExpressFee;
import com.zm.order.pojo.Order4Confirm;
import com.zm.order.pojo.OrderCount;
import com.zm.order.pojo.OrderDetail;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderIdAndSupplierId;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.Pagination;
import com.zm.order.pojo.PostFeeDTO;
import com.zm.order.pojo.ResultModel;
import com.zm.order.pojo.ShoppingCart;
import com.zm.order.pojo.Tax;
import com.zm.order.pojo.ThirdOrderInfo;
import com.zm.order.utils.CalculationUtils;
import com.zm.order.utils.CommonUtils;
import com.zm.order.utils.DateUtils;
import com.zm.order.utils.JSONUtil;

/**
 * ClassName: OrderServiceImpl <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: Aug 11, 2017 3:54:27 PM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */

@Service("orderService")
@Transactional(isolation = Isolation.READ_COMMITTED)
public class OrderServiceImpl implements OrderService {

	@Resource
	OrderMapper orderMapper;

	@Resource
	GoodsFeignClient goodsFeignClient;

	@Resource
	UserFeignClient userFeignClient;

	@Resource
	PayFeignClient payFeignClient;

	@Resource
	RedisTemplate<String, Object> template;

	@Resource
	SupplierFeignClient supplierFeignClient;

	@Resource
	ShareProfitComponent shareProfitComponent;

	@Resource
	ActivityFeignClient activityFeignClient;

	@Resource
	CapitalPoolThreadPool capitalPoolThreadPool;

	@Resource
	CacheAbstractService cacheAbstractService;

	@SuppressWarnings("unchecked")
	@Override
	public ResultModel saveOrder(OrderInfo info, String payType, String type, HttpServletRequest req)
			throws DataIntegrityViolationException, Exception {
		ResultModel result = new ResultModel();
		if (info == null) {
			result.setErrorMsg("订单不能为空");
			result.setSuccess(false);
			return result;
		}
		if (!info.check()) {
			result.setErrorMsg("订单参数不全");
			result.setSuccess(false);
			return result;
		}

		String openId = null;
		if (Constants.WX_PAY.equals(payType)) {
			openId = req.getParameter("openId");
			if (Constants.JSAPI.equals(type)) {
				if (openId == null || "".equals(openId)) {
					result.setSuccess(false);
					result.setErrorMsg("请使用微信授权登录");
					return result;
				}
			}
		}

		String orderId = CommonUtils.getOrderId(info.getOrderFlag() + "");

		List<OrderBussinessModel> list = new ArrayList<OrderBussinessModel>();
		OrderBussinessModel model = null;
		StringBuilder detail = new StringBuilder();
		int localAmount = (int) (info.getOrderDetail().getPayment() * 100);
		for (OrderGoods goods : info.getOrderGoodsList()) {
			model = new OrderBussinessModel();
			model.setOrderId(orderId);
			model.setItemCode(goods.getItemCode());
			model.setDeliveryPlace(info.getOrderDetail().getDeliveryPlace());
			model.setItemId(goods.getItemId());
			model.setQuantity(goods.getItemQuantity());
			model.setSku(goods.getSku());
			list.add(model);
			detail.append(goods.getItemName() + "*" + goods.getItemQuantity() + ";");
		}

		Map<String, Object> priceAndWeightMap = null;
		Double amount = 0.0;
		boolean vip = false;
		Integer centerId = null;
		if (Constants.PREDETERMINE_ORDER == info.getOrderSource()) {// 如果是订货平台订单，默认centerId
			centerId = -1;
		} else {
			centerId = info.getCenterId();
		}

		// 获取该用户是否是VIP
		vip = userFeignClient.getVipUser(Constants.FIRST_VERSION, info.getUserId(), info.getCenterId());
		// 根据itemID和数量获得金额并扣减库存（除了第三方代发不需要扣库存，其他需要）
		result = goodsFeignClient.getPriceAndDelStock(Constants.FIRST_VERSION, list, info.getSupplierId(), vip,
				centerId, info.getOrderFlag(), info.getCouponIds(), info.getUserId());

		if (!result.isSuccess()) {
			return result;
		}
		priceAndWeightMap = (Map<String, Object>) result.getObj();
		amount = (Double) priceAndWeightMap.get("totalAmount");

		// 邮费和税费初始值
		Double postFee = 0.0;
		Double taxFee = 0.0;
		Double totalExciseTax = 0.0;
		Double totalIncremTax = 0.0;
		Double unDiscountAmount = 0.0;
		Integer weight = (Integer) priceAndWeightMap.get("weight");

		//获取包邮包税
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		Map<String, String> tempMap = hashOperations.entries(Constants.POST_TAX + info.getSupplierId());
		boolean freePost = false;
		boolean freeTax = false;
		if (tempMap != null) {
			freePost = Constants.FREE_POST.equals(tempMap.get("post")) ? true : false;
			freeTax = Constants.FREE_TAX.equals(tempMap.get("tax")) ? true : false;
		}
		if (!freePost) {
			// 计算邮费(自提不算邮费)
			if (Constants.EXPRESS.equals(info.getExpressType())) {
				String province = info.getOrderDetail().getReceiveProvince();
				PostFeeDTO post = new PostFeeDTO(amount, province, weight, info.getCenterId());
				postFee = getPostFee(post);
			}
		}
		if (!freeTax) {
			// 计算税费
			if (Constants.O2O_ORDER_TYPE.equals(info.getOrderFlag())) {
				Map<String, Double> map = (Map<String, Double>) priceAndWeightMap.get("tax");

				for (Map.Entry<String, Double> entry : map.entrySet()) {
					unDiscountAmount += entry.getValue();
				}
				for (Map.Entry<String, Double> entry : map.entrySet()) {
					Tax tax = JSONUtil.parse(entry.getKey(), Tax.class);
					Double fee = entry.getValue();
					Double subPostFee = CalculationUtils.mul(CalculationUtils.div(fee, unDiscountAmount, 2), postFee);
					if (tax.getExciseTax() != null) {
						if (tax.getExciseTax() >= 1 || tax.getIncrementTax() >= 1) {
							result.setErrorMsg("消费税或增值税设置有误");
							result.setSuccess(false);
							return result;
						}
						Double temp = CalculationUtils.div(CalculationUtils.add(fee, subPostFee),
								CalculationUtils.sub(1.0, tax.getExciseTax()), 2);
						Double exciseTax = CalculationUtils.mul(temp, tax.getExciseTax());
						totalExciseTax += CalculationUtils.mul(exciseTax, 0.7);
						Double incremTax = CalculationUtils.mul(CalculationUtils.add(fee, subPostFee, exciseTax),
								tax.getIncrementTax());
						totalIncremTax += CalculationUtils.mul(incremTax, 0.7);
					} else {
						totalIncremTax += CalculationUtils.mul(
								CalculationUtils.mul(CalculationUtils.add(fee, subPostFee), tax.getIncrementTax()),
								0.7);
					}
				}
				taxFee = CalculationUtils.add(totalExciseTax, totalIncremTax);
			}
		}

		Double disAmount = 0.0;
		if (unDiscountAmount > 0) {
			disAmount = CalculationUtils.sub(unDiscountAmount, amount);
		}

		amount = CalculationUtils.add(amount, taxFee, postFee);
		amount = CalculationUtils.round(2, amount);
		int totalAmount = (int) CalculationUtils.mul(amount, 100);

		if (totalAmount - localAmount > 5 || totalAmount - localAmount < -5) {// 价格区间定义在正负5分
			result.setErrorMsg("价格前后台不一致,商品原总价：" + unDiscountAmount + ",现订单总价：" + amount + ",税费：" + taxFee + ",运费："
					+ postFee + ",优惠金额：" + disAmount);
			result.setSuccess(false);
			return result;
		}

		PayModel payModel = new PayModel();
		payModel.setBody("购物订单");
		payModel.setOrderId(orderId);
		payModel.setTotalAmount(totalAmount + "");
		String detailStr = detail.toString().substring(0, detail.toString().length() - 1);
		if (detailStr.length() > 100) {// 支付宝描述过长会报错
			detailStr = detailStr.substring(0, 100) + "...";
		}
		payModel.setDetail(detailStr);

		if (Constants.WX_PAY.equals(payType)) {
			payModel.setOpenId(openId);
			payModel.setIP(req.getRemoteAddr());
			Map<String, String> paymap = payFeignClient.wxPay(centerId, type, payModel);
			result.setObj(paymap);
		} else if (Constants.ALI_PAY.equals(payType)) {
			result.setObj(payFeignClient.aliPay(centerId, type, payModel));
		} else if (Constants.UNION_PAY.equals(payType)) {
			result.setObj(payFeignClient.unionpay(centerId, type, payModel));
		} else {
			result.setSuccess(false);
			result.setErrorMsg("请指定正确的支付方式");
			return result;
		}

		if (info.getCouponIds() != null) {
			activityFeignClient.updateUserCoupon(Constants.FIRST_VERSION, info.getCenterId(), info.getUserId(),
					info.getCouponIds());
		}

		if (info.getPushUserId() != null) {// 如果是推手订单，判断该推手是否有效
			boolean flag = userFeignClient.verifyEffective(Constants.FIRST_VERSION, info.getShopId(),
					info.getPushUserId());
			if (!flag) {// 失效推手ID设为null
				info.setPushUserId(null);
			}
		}

		info.setOrderId(orderId);
		info.setWeight(weight);
		info.getOrderDetail().setOrderId(orderId);
		info.setStatus(0);

		orderMapper.saveOrder(info);

		info.getOrderDetail().setPostFee(postFee);
		info.getOrderDetail().setPayment(amount);
		info.getOrderDetail().setTaxFee(taxFee);
		info.getOrderDetail().setIncrementTax(totalIncremTax);
		info.getOrderDetail().setExciseTax(totalExciseTax);
		info.getOrderDetail().setTariffTax(0.0);
		info.getOrderDetail().setDisAmount(disAmount);
		orderMapper.saveOrderDetail(info.getOrderDetail());

		for (OrderGoods goods : info.getOrderGoodsList()) {
			goods.setOrderId(orderId);
		}
		orderMapper.saveOrderGoods(info.getOrderGoodsList());

		// 增加缓存订单数量
		cacheAbstractService.addOrderCountCache(info.getShopId(), Constants.ORDER_STATISTICS_DAY, "produce");
		// 增加月订单数
		String time = DateUtils.getTimeString("yyyyMM");
		cacheAbstractService.addOrderCountCache(info.getShopId(), Constants.ORDER_STATISTICS_MONTH, time);

		result.setSuccess(true);
		result.setErrorMsg(orderId);
		return result;

	}

	@Override
	public ResultModel listUserOrder(OrderInfo info, Pagination pagination) {
		ResultModel result = new ResultModel();
		Map<String, Object> param = new HashMap<String, Object>();
		if (pagination != null) {
			pagination.init();
			param.put("pagination", pagination);
		}
		param.put("info", info);
		// 查询待收货订单时用
		if (info.getStatusArr() != null) {
			String[] tempArr = info.getStatusArr().split(",");
			List<Integer> statusList = new ArrayList<Integer>();
			try {
				for (String status : tempArr) {
					statusList.add(Integer.valueOf(status));
				}
				param.put("statusList", statusList);
			} catch (NumberFormatException e) {
				result.setSuccess(false);
				result.setErrorMsg("状态参数出错");
				return result;
			}
		}
		// end
		Integer count = orderMapper.queryCountOrderInfo(param);
		pagination.setTotalRows(count.longValue());
		pagination.webListConverter();
		List<OrderInfo> list = orderMapper.listOrderByParam(param);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("pagination", pagination);
		resultMap.put("orderList", list);
		result.setObj(resultMap);
		result.setSuccess(true);

		return result;
	}

	@Override
	public ResultModel removeUserOrder(Map<String, Object> param) {
		ResultModel result = new ResultModel();

		orderMapper.removeUserOrder(param);

		result.setSuccess(true);
		return result;
	}

	@Override
	public ResultModel confirmUserOrder(Map<String, Object> param) {
		ResultModel result = new ResultModel();

		int count = orderMapper.confirmUserOrder(param);
		if (count > 0) {// 有更新结果后插入状态记录表
			param.put("status", Constants.ORDER_COMPLETE);
			orderMapper.addOrderStatusRecord(param);
			shareProfitComponent.calShareProfit(param.get("orderId").toString());
		}

		result.setSuccess(true);
		return result;
	}

	@Override
	public ResultModel updateOrderPayStatusByOrderId(Map<String, Object> param) {

		ResultModel result = new ResultModel();

		String orderId = param.get("orderId") + "";

		int count = orderMapper.updateOrderPayStatusByOrderId(orderId);

		orderMapper.updateOrderDetailPayTime(param);
		if (count > 0) {// 有更新结果后插入状态记录表
			param.put("status", Constants.ORDER_PAY);
			orderMapper.addOrderStatusRecord(param);

			shareProfitComponent.calShareProfitStayToAccount(orderId);
		}

		result.setSuccess(true);
		return result;
	}

	@Override
	public Integer getClientIdByOrderId(String orderId) {

		return orderMapper.getClientIdByOrderId(orderId);
	}

	@Override
	public void saveShoppingCart(ShoppingCart cart) {

		orderMapper.saveShoppingCart(cart);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ShoppingCart> listShoppingCart(Map<String, Object> param) throws Exception {

		List<ShoppingCart> list = orderMapper.listShoppingCart(param);

		if (list.size() == 0 || list == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		for (ShoppingCart model : list) {
			sb.append(model.getItemId() + ",");
		}

		String ids = sb.substring(0, sb.length() - 1);

		ResultModel result = goodsFeignClient.listGoodsSpecs(Constants.FIRST_VERSION, ids,
				(Integer) param.get("centerId"), "feign");
		if (result.isSuccess()) {
			Map<String, Object> resultMap = (Map<String, Object>) result.getObj();
			List<Map<String, Object>> specsList = (List<Map<String, Object>>) resultMap.get("specsList");
			List<Map<String, Object>> fileList = (List<Map<String, Object>>) resultMap.get("pic");
			GoodsSpecs specs = null;
			for (ShoppingCart model : list) {
				for (Map<String, Object> map : specsList) {
					specs = JSONUtil.parse(JSONUtil.toJson(map), GoodsSpecs.class);
					if (specs.getItemId().equals(model.getItemId())) {
						model.setGoodsSpecs(specs);
						break;
					}
				}
			}
			GoodsFile file = null;
			for (ShoppingCart model : list) {
				for (Map<String, Object> map : fileList) {
					file = JSONUtil.parse(JSONUtil.toJson(map), GoodsFile.class);
					if (file.getGoodsId().equals(model.getGoodsSpecs().getGoodsId())) {
						model.setPicPath(file.getPath());
						break;
					}
				}
			}

		} else {
			throw new RuntimeException("内部调用商品服务出错");
		}

		return list;
	}

	@Override
	public List<OrderCount> getCountByStatus(Map<String, Object> param, String type) {

		if ("0".equals(type)) {
			return orderMapper.getCountByStatus(param);
		}
		if ("1".equals(type)) {
			return orderMapper.getPushCountByStatus(param);
		}
		return null;
	}

	@Override
	public void removeShoppingCart(Map<String, Object> param) {
		orderMapper.removeShoppingCart(param);

	}

	@Override
	public Integer countShoppingCart(Map<String, Object> param) {
		return orderMapper.countShoppingCart(param);
	}

	@Override
	public ResultModel orderCancel(Integer userId, String orderId) throws Exception {

		OrderInfo info = orderMapper.getOrderByOrderId(orderId);
		if (info == null || !userId.equals(info.getUserId())) {
			return new ResultModel(false, "该订单号不是您的订单号");
		}

		if (info.getStatus() >= Constants.ORDER_DELIVER && !Constants.ORDER_EXCEPTION.equals(info.getStatus())) {
			return new ResultModel(false, "该订单已发货或已完成，请联系客服");
		}

		if (Constants.O2O_ORDER_TYPE.equals(info.getOrderFlag())) {
			if (Constants.ORDER_TO_WAREHOUSE > info.getStatus()
					&& !Constants.ORDER_EXCEPTION.equals(info.getStatus())) {
				if (template.opsForValue().get(orderId) != null) {// 是否正在发送给第三方
					return new ResultModel(false, "该订单正在发送给保税仓，请稍后重试");
				}
				RefundPayModel model = new RefundPayModel(info.getOrderId(), info.getOrderDetail().getPayNo(),
						info.getOrderDetail().getPayment() + "", "正常退款");
				Map<String, Object> result = new HashMap<String, Object>();
				if (Constants.ALI_PAY.equals(info.getOrderDetail().getPayType())) {
					result = payFeignClient.aliRefundPay(info.getCenterId(), model);
				}
				if (Constants.WX_PAY.equals(info.getOrderDetail().getPayType())) {
					result = payFeignClient.wxRefundPay(info.getCenterId(), model);
				}
				if ((Boolean) result.get("success")) {
					OrderDetail detail = new OrderDetail();
					detail.setOrderId(orderId);
					detail.setReturnPayNo((String) result.get("returnPayNo"));
					int count = orderMapper.updateOrderCancel(orderId);
					if (count > 0) {
						Map<String, Object> param = new HashMap<String, Object>();
						param.put("status", Constants.ORDER_CANCEL);
						param.put("orderId", orderId);
						orderMapper.addOrderStatusRecord(param);
					}
					stockBack(info);
				} else {
					return new ResultModel(false, result.get("errorMsg"));
				}
			} else {
				// TODO 发送仓库确认是否可以退单
				return new ResultModel(false, "已发仓库，退款请联系客服");
			}
		} else {
			// TODO 大贸和一般贸易
		}

		return new ResultModel(true, null);
	}

	@Override
	public OrderInfo getOrderByOrderIdForPay(String orderId) {

		return orderMapper.getOrderByOrderId(orderId);
	}

	@Override
	public boolean updateOrderPayType(OrderDetail detail) {

		orderMapper.updateOrderPayType(detail);
		return true;
	}

	private static final Integer DEFAULT_USER_ID = -1;

	@Override
	public boolean closeOrder(Integer userId, String orderId) {

		OrderInfo info = orderMapper.getOrderByOrderId(orderId);
		if (!DEFAULT_USER_ID.equals(userId) && (info == null || !userId.equals(info.getUserId()))) {
			return false;
		}
		int count = orderMapper.updateOrderClose(orderId);
		if (count > 0) {
			// 增加取消数量缓存
			cacheAbstractService.addOrderCountCache(info.getShopId(), Constants.ORDER_STATISTICS_DAY, "cancel");

			Map<String, Object> param = new HashMap<String, Object>();
			param.put("status", Constants.ORDER_CLOSE);
			param.put("orderId", orderId);
			orderMapper.addOrderStatusRecord(param);
		}
		stockBack(info);
		return true;
	}

	private void stockBack(OrderInfo info) {
		List<OrderGoods> goodsList = info.getOrderGoodsList();
		List<OrderBussinessModel> list = new ArrayList<OrderBussinessModel>();
		OrderBussinessModel model = null;
		for (OrderGoods goods : goodsList) {
			model = new OrderBussinessModel();
			model.setItemId(goods.getItemId());
			model.setQuantity(goods.getItemQuantity());
			list.add(model);
		}
		goodsFeignClient.stockBack(Constants.FIRST_VERSION, list, info.getOrderFlag());
	}

	@Override
	public void timeTaskcloseOrder() {
		String time = DateUtils.getTime(Calendar.MINUTE, -90, "yyyy-MM-dd HH:mm:ss");
		List<String> orderIdList = orderMapper.listTimeOutOrderIds(time);
		for (String orderId : orderIdList) {
			closeOrder(DEFAULT_USER_ID, orderId);
		}
	}

	@Override
	public List<CustomModel> listPayCustomOrder() {

		return orderMapper.listPayCustomOrder();
	}

	@Override
	public void updatePayCustom(String orderId) {
		int count = orderMapper.updatePayCustom(orderId);
		if (count > 0) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("status", Constants.ORDER_PAY_CUSTOMS);
			param.put("orderId", orderId);
			orderMapper.addOrderStatusRecord(param);
		}
	}

	@Override
	public Double getPostFee(PostFeeDTO postFee) {
		String id = judgeCenterId(postFee.getCenterId());
		Double conditionFee = orderMapper.getFreePostFee(id);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		if (conditionFee != null && postFee.getPrice() >= conditionFee) {
			return 0.0;
		} else {
			param.put("province", postFee.getProvince());
			ExpressFee expressFee = orderMapper.getExpressFee(param);
			if (expressFee != null) {
				if (postFee.getWeight() > expressFee.getWeight()) {
					Double weight = Math.ceil(CalculationUtils
							.div(CalculationUtils.sub(postFee.getWeight(), expressFee.getWeight()), 1000.0));
					return CalculationUtils.add(expressFee.getFee(),
							CalculationUtils.mul(expressFee.getHeavyFee(), weight));
				} else {
					return expressFee.getFee();
				}
			} else {
				return orderMapper.getDefaultFee(postFee.getExpressKey());
			}
		}
	}

	@Override
	public void createTable(Integer centerId) {
		orderMapper.createExpressFee(centerId);
		orderMapper.createFreeExpressFee(centerId);
		orderMapper.createProfitProportion(centerId);
	}

	private String judgeCenterId(Integer id) {
		if (Constants.PREDETERMINE_PLAT_TYPE == id) {
			return "_2B";
		}
		return "_" + id;
	}

	@Override
	public List<Express> listExpress() {
		return orderMapper.listExpress();
	}

	@Override
	public void updateRefundPayNo(OrderDetail detail) {
		orderMapper.updateRefundPayNo(detail);
	}

	@Override
	public List<OrderInfo> listOrderForSendToWarehouse() {
		List<OrderInfo> list = new ArrayList<OrderInfo>();
		Set<String> set = new HashSet<String>();
		List<OrderGoods> goodsList = null;
		Map<String, OrderGoods> tempMap = new HashMap<String, OrderGoods>();
		OrderGoods temp = null;
//		list.addAll(orderMapper.listOrderForSendToTTWarehouse());
//		list.addAll(orderMapper.listOrderForSendToOtherWarehouse());
		list.addAll(orderMapper.listOrderForSendToWarehouse());
		if (list.size() > 0) {
			for (OrderInfo info : list) {// 找出所有的itemId
				for (OrderGoods goods : info.getOrderGoodsList()) {
					set.add(goods.getItemId());
				}
			}
			Map<String, GoodsConvert> result = goodsFeignClient.listSkuAndConversionByItemId(Constants.FIRST_VERSION,
					set);
			if (result != null) {// 对每个商品进行换算和补全sku并合并
				for (OrderInfo info : list) {
					tempMap.clear();
					goodsList = info.getOrderGoodsList();
					Iterator<OrderGoods> it = goodsList.iterator();
					while (it.hasNext()) {
						temp = it.next();
						convert(temp, result);// 补全sku和比例换算
						if (tempMap.containsKey(temp.getSku().trim())) {
							OrderGoods model = tempMap.get(temp.getSku().trim());
							double actualprice = CalculationUtils.mul(model.getActualPrice(), model.getItemQuantity());
							double itemprice = CalculationUtils.mul(model.getItemPrice(), model.getItemQuantity());
							double temactualprice = CalculationUtils.mul(temp.getActualPrice(), temp.getItemQuantity());
							double temitemprice = CalculationUtils.mul(temp.getItemPrice(), temp.getItemQuantity());
							model.setItemQuantity(model.getItemQuantity() + temp.getItemQuantity());
							try {
								model.setActualPrice(CalculationUtils.div(
										CalculationUtils.add(temactualprice, actualprice), model.getItemQuantity(), 2));
								model.setItemPrice(CalculationUtils.div(CalculationUtils.add(itemprice, temitemprice),
										model.getItemQuantity(), 2));
								it.remove();// 合并后删除该商品
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}
						} else {
							tempMap.put(temp.getSku().trim(), temp);
						}

					}
				}
			}
		}
		return list;
	}

	private void convert(OrderGoods temp, Map<String, GoodsConvert> result) {
		GoodsConvert convert;
		convert = result.get(temp.getItemId());
		if (temp.getSku() == null || "".equals(temp.getSku().trim())) {
			temp.setSku(convert.getSku().trim());
		}
		// 如果换算比例大于1，单价和售价需要除以换算比例，并且数量要乘以换算比例
		if (convert.getConversion() != null && convert.getConversion() > 1) {
			try {
				temp.setActualPrice(CalculationUtils.div(temp.getActualPrice(), convert.getConversion(), 2));
				temp.setItemPrice(CalculationUtils.div(temp.getItemPrice(), convert.getConversion(), 2));
				temp.setItemQuantity((int) CalculationUtils.mul(temp.getItemQuantity(), convert.getConversion()));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void saveThirdOrder(List<SendOrderResult> list) {
		orderMapper.saveThirdOrder(list);
		int count = orderMapper.updateOrderSendToWarehouse(list.get(0).getOrderId());
		if (count > 0) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("status", Constants.ORDER_TO_WAREHOUSE);
			param.put("orderId", list.get(0).getOrderId());
			orderMapper.addOrderStatusRecord(param);
		}
	}

	@Override
	public ResultModel checkOrderStatus(List<OrderIdAndSupplierId> list) {
		return supplierFeignClient.checkOrderStatus(Constants.FIRST_VERSION, list);
	}

	@Override
	public void changeOrderStatusByThirdWarehouse(List<ThirdOrderInfo> list) {
		orderMapper.updateThirdOrderInfo(list);
		List<Integer> statusList = orderMapper.listOrderStatus(list.get(0).getOrderId());
		ThirdOrderInfo orderInfo = list.get(0);
		if (statusList != null && statusList.size() > 1) {
			Collections.sort(statusList);
			// 有一个没发货订单状态就是单证放行
			if (Constants.ORDER_DELIVER.equals(statusList.get(statusList.size() - 1))
					&& !statusList.get(0).equals(Constants.ORDER_DELIVER)) {
				orderInfo.setOrderStatus(Constants.ORDER_DZFX);
			}
		}
		int count = orderMapper.updateOrderStatusByThirdStatus(orderInfo);
		if (count > 0) {
			// 发货新增发货数量缓存
			if (Constants.ORDER_DELIVER.equals(orderInfo.getOrderStatus())) {
				// 增加发货数量缓存
				Integer gradeId = orderMapper.getGradeId(orderInfo.getOrderId());
				cacheAbstractService.addOrderCountCache(gradeId, Constants.ORDER_STATISTICS_DAY, "deliver");
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("status", list.get(0).getOrderStatus());
			param.put("orderId", list.get(0).getOrderId());
			orderMapper.addOrderStatusRecord(param);
		}
	}

	@Override
	public Integer countShoppingCartQuantity(Map<String, Object> param) {

		return orderMapper.countShoppingCartQuantity(param);
	}

	@Override
	public List<Object> getProfit(Integer shopId) {
		return template.opsForList().range(Constants.PROFIT + shopId, 0, -1);
	}

	@Override
	public List<OrderIdAndSupplierId> listUnDeliverOrder() {
		return orderMapper.listUnDeliverOrder();
	}

	@Override
	public void confirmByTimeTask() {
		String time = DateUtils.getTime(Calendar.DATE, -7, "yyyy-MM-dd HH:mm:ss");
		List<Order4Confirm> list = orderMapper.listUnConfirmOrder(time);
		Map<String, Object> param = null;
		for (Order4Confirm model : list) {
			param = new HashMap<String, Object>();
			param.put("userId", model.getUserId());
			param.put("orderId", model.getOrderId());
			confirmUserOrder(param);
		}
	}

	@Override
	public ResultModel repayingPushJudge(Integer pushUserId, Integer shopId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("pushUserId", pushUserId);
		param.put("shopId", shopId);
		int count = 0;
		count = orderMapper.repayingPushJudge(param);
		if (count > 0) {
			return new ResultModel(false, "还有未完成的订单");
		}
		return new ResultModel(true, "");
	}

	@Override
	public ResultModel pushUserOrderCount(Integer shopId, List<Integer> pushUserIdList) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("pushUserIdList", pushUserIdList);
		param.put("shopId", shopId);
		return new ResultModel(true, orderMapper.pushUserOrderCount(param));
	}

	@Override
	public ResultModel orderBackCancel(String orderId, String payNo) {
		OrderInfo info = orderMapper.getOrderByOrderId(orderId);
		int count = orderMapper.updateOrderCancel(orderId);
		OrderDetail detail = new OrderDetail();
		detail.setOrderId(orderId);
		detail.setReturnPayNo(payNo);
		orderMapper.updateRefundPayNo(detail);
		if (count > 0) {

			// 增加取消数量缓存
			cacheAbstractService.addOrderCountCache(info.getShopId(), Constants.ORDER_STATISTICS_DAY, "cancel");

			Map<String, Object> param = new HashMap<String, Object>();
			param.put("status", Constants.ORDER_CANCEL);
			param.put("orderId", orderId);
			orderMapper.addOrderStatusRecord(param);
		}
		stockBack(info);
		Integer status = info.getStatus();
		if (!Constants.ORDER_COMPLETE.equals(status) && !Constants.ORDER_CANCEL.equals(status)
				&& !Constants.ORDER_CLOSE.equals(status) && !Constants.ORDER_INIT.equals(status)) {
			shareProfitComponent.calRefundShareProfit(orderId);
		}
		return new ResultModel(true, "");
	}

	@Override
	public ResultModel refunds(String orderId) {

		orderMapper.updateOrderRefunds(orderId);
		return new ResultModel(true, "");
	}

	/**
	 * @fun 按照区域中心ID区分订单，防止并发时计算错误 由线程池处理，防止feign调用超时
	 * @return
	 */
	@Override
	public boolean capitalPoolRecount() {
		List<OrderInfo> infoList = orderMapper.listCapitalPoolNotEnough();
		if (infoList != null && infoList.size() > 0) {
			Map<Integer, List<OrderInfo>> tempMap = new HashMap<Integer, List<OrderInfo>>();
			List<OrderInfo> orderTmpList = null;
			for (OrderInfo info : infoList) {
				if (tempMap.get(info.getCenterId()) == null) {
					orderTmpList = new ArrayList<OrderInfo>();
					orderTmpList.add(info);
					tempMap.put(info.getCenterId(), orderTmpList);
				} else {
					tempMap.get(info.getCenterId()).add(info);
				}
			}
			for (Map.Entry<Integer, List<OrderInfo>> entry : tempMap.entrySet()) {
				capitalPoolThreadPool.capitalPoolRecount(entry.getValue());
			}
		}
		return false;
	}
}

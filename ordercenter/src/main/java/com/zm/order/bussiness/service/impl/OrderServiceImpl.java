package com.zm.order.bussiness.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zm.order.bussiness.dao.OrderMapper;
import com.zm.order.bussiness.service.OrderService;
import com.zm.order.constants.Constants;
import com.zm.order.constants.LogConstants;
import com.zm.order.feignclient.GoodsFeignClient;
import com.zm.order.feignclient.LogFeignClient;
import com.zm.order.feignclient.PayFeignClient;
import com.zm.order.feignclient.UserFeignClient;
import com.zm.order.feignclient.model.Activity;
import com.zm.order.feignclient.model.GoodsFile;
import com.zm.order.feignclient.model.GoodsSpecs;
import com.zm.order.feignclient.model.OrderBussinessModel;
import com.zm.order.feignclient.model.PayModel;
import com.zm.order.feignclient.model.RefundPayModel;
import com.zm.order.pojo.CustomModel;
import com.zm.order.pojo.Express;
import com.zm.order.pojo.ExpressFee;
import com.zm.order.pojo.OrderCount;
import com.zm.order.pojo.OrderDetail;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.Pagination;
import com.zm.order.pojo.PostFeeDTO;
import com.zm.order.pojo.ResultModel;
import com.zm.order.pojo.ShoppingCart;
import com.zm.order.pojo.Tax;
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
@Transactional
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
	LogFeignClient logFeignClient;

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
		if (info.getOrderDetail() == null) {
			result.setErrorMsg("订单详情不能为空");
			result.setSuccess(false);
			return result;
		}
		if (info.getOrderGoodsList() == null || info.getOrderGoodsList().size() == 0) {
			result.setErrorMsg("订单商品不能为空");
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
		String localAmount = (int) (info.getOrderDetail().getPayment() * 100) + "";
		for (OrderGoods goods : info.getOrderGoodsList()) {
			model = new OrderBussinessModel();
			model.setOrderId(orderId);
			model.setDeliveryPlace(info.getOrderDetail().getDeliveryPlace());
			model.setItemId(goods.getItemId());
			model.setQuantity(goods.getItemQuantity());
			list.add(model);
			detail.append(goods.getItemName() + "*" + goods.getItemQuantity() + ";");
		}

		Map<String, Object> priceAndWeightMap = null;
		Double amount = 0.0;
		boolean vip = false;
		Activity activity = null;

		// 获取该用户是否是VIP
		vip = userFeignClient.getVipUser(Constants.FIRST_VERSION, info.getUserId(), info.getCenterId());
		// 获取全场活动
		result = goodsFeignClient.getActivity(Constants.FIRST_VERSION, null, Constants.ACTIVE_AREA, info.getCenterId());
		if (!result.isSuccess()) {
			result.setErrorMsg("获取活动信息失败");
			return result;
		}

		if (result.getObj() != null) {
			activity = (Activity) result.getObj();
		}
		// 根据itemID和数量获得金额并扣减库存（除了第三方代发不需要扣库存，其他需要）
		if (Constants.O2O_ORDER_TYPE.equals(info.getOrderFlag())
				&& !Constants.OWN_SUPPLIER.equals(info.getSupplierId())) {
			result = goodsFeignClient.getPriceAndDelStock(Constants.FIRST_VERSION, list, false, vip, info.getCenterId(),
					info.getOrderFlag());
		} else {
			result = goodsFeignClient.getPriceAndDelStock(Constants.FIRST_VERSION, list, true, vip, info.getCenterId(),
					info.getOrderFlag());
		}
		if (!result.isSuccess()) {
			return result;
		}
		priceAndWeightMap = (Map<String, Object>) result.getObj();
		amount = (Double) priceAndWeightMap.get("totalAmount");
		// 是否有活动
		if (activity != null) {
			if (Constants.FULL_CUT.equals(activity.getType())) {
				if (amount > activity.getConditionPrice()) {
					amount = CalculationUtils.sub(amount, activity.getDiscount());
				}
			}
			if (Constants.FULL_DISCOUNT.equals(activity.getType())) {
				if (amount > activity.getConditionPrice()) {
					Double discount = CalculationUtils.div(activity.getDiscount(), 10.0);
					amount = CalculationUtils.mul(amount, discount);
				}
			}
		}

		// 计算邮费(自提不算邮费)
		Double postFee = 0.0;
		Integer weight = (Integer) priceAndWeightMap.get("weight");
		if (Constants.EXPRESS.equals(info.getExpressType())) {
			String province = info.getOrderDetail().getReceiveProvince();
			PostFeeDTO post = new PostFeeDTO(amount, province, weight, info.getCenterId());
			postFee = getPostFee(post);
		}

		// 计算税费
		Double taxFee = 0.0;
		Double totalExciseTax = 0.0;
		Double totalIncremTax = 0.0;
		Double unDiscountAmount = 0.0;
		if (Constants.O2O_ORDER_TYPE.equals(info.getOrderFlag())) {
			Map<Tax, Double> map = (Map<Tax, Double>) priceAndWeightMap.get("tax");

			for (Map.Entry<Tax, Double> entry : map.entrySet()) {
				unDiscountAmount += entry.getValue();
			}
			for (Map.Entry<Tax, Double> entry : map.entrySet()) {
				Tax tax = entry.getKey();
				Double fee = entry.getValue();
				Double subPostFee = CalculationUtils.mul(CalculationUtils.div(fee, unDiscountAmount), postFee);
				if (tax.getExciseTax() != null) {
					Double exciseTax = CalculationUtils.mul(CalculationUtils.div(CalculationUtils.add(fee, subPostFee),
							CalculationUtils.sub(1.0, tax.getExciseTax())), tax.getExciseTax());
					totalExciseTax += CalculationUtils.mul(exciseTax, 0.7);
					Double incremTax = CalculationUtils.mul(CalculationUtils.add(fee, subPostFee, exciseTax),
							tax.getIncrementTax());
					totalIncremTax += CalculationUtils.mul(incremTax, 0.7);
				} else {
					totalIncremTax += CalculationUtils.mul(CalculationUtils.add(fee, subPostFee),
							tax.getIncrementTax());
				}
			}
			taxFee = CalculationUtils.add(totalExciseTax, totalIncremTax);
		}

		Double disAmount = 0.0;
		if (unDiscountAmount > 0) {
			disAmount = CalculationUtils.sub(unDiscountAmount, amount);
		}

		amount = CalculationUtils.add(amount, taxFee, postFee);
		String totalAmount = (int) (amount * 100) + "";

		if (!totalAmount.equals(localAmount + "")) {
			result.setErrorMsg("价格前后台不一致");
			result.setSuccess(false);
			return result;
		}

		PayModel payModel = new PayModel();
		payModel.setBody("购物订单");
		payModel.setOrderId(orderId);
		payModel.setTotalAmount(totalAmount);
		payModel.setDetail(detail.toString().substring(0, detail.toString().length() - 1));

		if (Constants.WX_PAY.equals(payType)) {
			payModel.setOpenId(openId);
			payModel.setIP(req.getRemoteAddr());
			Map<String, String> paymap = payFeignClient.wxPay(info.getCenterId(), type, payModel);
			result.setObj(paymap);
		} else if (Constants.ALI_PAY.equals(payType)) {
			result.setObj(payFeignClient.aliPay(info.getCenterId(), type, payModel));
		} else {
			result.setSuccess(false);
			result.setErrorMsg("请指定正确的支付方式");
			return result;
		}

		info.setOrderId(orderId);
		info.setWeight(weight);
		info.getOrderDetail().setOrderId(orderId);

		orderMapper.saveOrder(info);

		info.getOrderDetail().setPostFee(postFee);
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

		result.setSuccess(true);
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
		result.setObj(orderMapper.listOrderByParam(param));
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

		orderMapper.confirmUserOrder(param);

		result.setSuccess(true);
		return result;
	}

	@Override
	public ResultModel updateOrderPayStatusByOrderId(Map<String, Object> param) {

		ResultModel result = new ResultModel();

		orderMapper.updateOrderPayStatusByOrderId(param.get("orderId") + "");

		orderMapper.updateOrderDetailPayTime(param);

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
				(Integer) param.get("centerId"));
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
	public List<OrderCount> getCountByStatus(Map<String, Object> param) {

		return orderMapper.getCountByStatus(param);
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

		if (Constants.O2O_ORDER_TYPE.equals(info.getOrderFlag())) {
			if (Constants.ORDER_TO_WAREHOUSE > info.getStatus()) {// TODO
																	// 如果再发送第三方过程中退款？？？？
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
					orderMapper.updateOrderCancel(orderId);
					stockBack(info);
				} else {
					return new ResultModel(false, result.get("errorMsg"));
				}
			} else {
				// TODO 发送仓库确认是否可以退单
			}
		} else {
			// TODO 大贸和一般贸易
		}

		String content = "订单号\"" + info.getOrderId() + "\"退单";

		logFeignClient.saveLog(Constants.FIRST_VERSION, CommonUtils.packageLog(LogConstants.ORDER_CANCEL, "订单退单",
				info.getCenterId(), content, info.getUserId() + ""));

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
		orderMapper.updateOrderClose(orderId);
		if (Constants.O2O_ORDER_TYPE.equals(info.getOrderFlag())
				&& !Constants.OWN_SUPPLIER.equals(info.getSupplierId())) {

		} else {
			stockBack(info);
		}
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
		goodsFeignClient.stockBack(Constants.FIRST_VERSION, list, info.getCenterId(), info.getOrderFlag());
	}

	@Override
	public void timeTaskcloseOrder() {
		String time = DateUtils.getTime(Calendar.DATE, -1);
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
		orderMapper.updatePayCustom(orderId);
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
	}

	private String judgeCenterId(Integer id) {
		String centerId;
		if (Constants.BIG_TRADE_CENTERID.equals(id) || Constants.O2O_CENTERID.equals(id)) {
			centerId = "";
		} else {
			centerId = "_" + id;
		}
		return centerId;
	}

	@Override
	public List<Express> listExpress() {
		return orderMapper.listExpress();
	}

	@Override
	public void updateRefundPayNo(OrderDetail detail) {
		orderMapper.updateRefundPayNo(detail);
	}
}

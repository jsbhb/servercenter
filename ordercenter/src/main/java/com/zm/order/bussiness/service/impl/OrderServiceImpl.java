package com.zm.order.bussiness.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import com.zm.order.feignclient.model.GoodsFile;
import com.zm.order.feignclient.model.GoodsSpecs;
import com.zm.order.feignclient.model.OrderBussinessModel;
import com.zm.order.feignclient.model.PayModel;
import com.zm.order.pojo.AbstractPayConfig;
import com.zm.order.pojo.OrderCount;
import com.zm.order.pojo.OrderDetail;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.Pagination;
import com.zm.order.pojo.ResultModel;
import com.zm.order.pojo.ShoppingCart;
import com.zm.order.pojo.WeiXinPayConfig;
import com.zm.order.utils.CommonUtils;
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

	@Override
	public ResultModel saveOrder(OrderInfo info, String payType, String type, AbstractPayConfig payConfig)
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
		boolean vip = userFeignClient.getVipUser(Constants.FIRST_VERSION, info.getUserId(), info.getCenterId());

		// 根据itemID和数量获得金额并扣减库存（除了自营仓需要扣库存，其他不需要）
		if (Constants.OWN_SUPPLIER.equals(info.getSupplierId())) {
			result = goodsFeignClient.getPriceAndDelStock(Constants.FIRST_VERSION, list, true, vip);
		} else {
			result = goodsFeignClient.getPriceAndDelStock(Constants.FIRST_VERSION, list, false, vip);
		}

		if (!result.isSuccess()) {
			return result;
		}

		String totalAmount = (int) ((Double) result.getObj() * 100) + "";

		if (!totalAmount.equals(localAmount + "")) {
			result.setErrorMsg("价格前后台不一致");
			result.setSuccess(false);
			return result;
		}

		PayModel payModel = new PayModel();
		payModel.setBody("中国供销-购物订单");
		payModel.setOrderId(orderId);
		payModel.setTotalAmount(totalAmount);
		payModel.setDetail(detail.toString().substring(0, detail.toString().length() - 1));

		if (Constants.WX_PAY.equals(payType)) {
			WeiXinPayConfig weiXinPayConfig = (WeiXinPayConfig) payConfig;
			payModel.setOpenId(weiXinPayConfig.getOpenId());
			payModel.setIP(weiXinPayConfig.getIp());
			Map<String, String> paymap = payFeignClient.wxPay(Integer.valueOf(info.getCenterId()), type, payModel);
			result.setObj(paymap);
		} else {
			result.setSuccess(false);
			result.setErrorMsg("请指定正确的支付方式");
			return result;
		}

		info.setOrderId(orderId);
		info.getOrderDetail().setOrderId(orderId);

		orderMapper.saveOrder(info);

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

		ResultModel result = goodsFeignClient.listGoodsSpecs(Constants.FIRST_VERSION, ids);
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
	public ResultModel orderCancel(OrderInfo info) {

		if (Constants.TRADE_ORDER_TYPE.equals(info.getOrderFlag())) {
			orderMapper.updateOrderCancel(info.getOrderId());

		} else if (Constants.O2O_ORDER_TYPE.equals(info.getOrderFlag())) {

			Integer status = orderMapper.getOrderStatusByOrderId(info.getOrderId());
			if(Constants.ORDER_DELIVER.equals(status)){
				//已发货，不能退单
			}
			// TODO 退单流程，根据status状态，库存处理，资金处理
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

	@Override
	public boolean closeOrder(String orderId) {
		
		orderMapper.updateOrderClose(orderId);
		OrderInfo info = orderMapper.getOrderByOrderId(orderId);
		if(Constants.OWN_SUPPLIER.equals(info.getSupplierId())){
			//TODO 库存回滚
		}
		return false;
	}
}

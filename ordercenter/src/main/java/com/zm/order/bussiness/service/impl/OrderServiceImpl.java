package com.zm.order.bussiness.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zm.order.bussiness.dao.OrderMapper;
import com.zm.order.bussiness.service.OrderService;
import com.zm.order.constants.Constants;
import com.zm.order.feignclient.GoodsFeignClient;
import com.zm.order.feignclient.PayFeignClient;
import com.zm.order.feignclient.UserFeignClient;
import com.zm.order.feignclient.model.GoodsFile;
import com.zm.order.feignclient.model.GoodsSpecs;
import com.zm.order.feignclient.model.OrderBussinessModel;
import com.zm.order.feignclient.model.PayModel;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.Pagination;
import com.zm.order.pojo.ResultModel;
import com.zm.order.pojo.ShoppingCart;
import com.zm.order.utils.CommonUtils;

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

	@Override
	public ResultModel saveOrder(OrderInfo info, Double version, String openId, String payType, String type) throws DataIntegrityViolationException, Exception{
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
		
		String orderId = CommonUtils.getOrderId(info.getOrderDetail().getOrderFlag() + "");
		
		List<OrderBussinessModel> list = new ArrayList<OrderBussinessModel>();
		OrderBussinessModel model = null;
		StringBuffer detail = new StringBuffer();
		for(OrderGoods goods : info.getOrderGoodsList()){
			model = new OrderBussinessModel();
			model.setOrderId(orderId);
			model.setDeliveryPlace(info.getOrderDetail().getDeliveryPlace());
			model.setItemId(goods.getItemId());
			model.setQuantity(goods.getItemQuantity());
			list.add(model);
			detail.append(goods.getItemName()+"*"+goods.getItemQuantity()+";");
		}
		boolean vip = userFeignClient.getVipUser(version, info.getUserId(), info.getCenterId());
		
		//根据itemID和数量获得金额并扣减库存（除了自营仓需要扣库存，其他不需要）
		if(Constants.OWN_SUPPLIER.equals(info.getSupplierId())){
			result = goodsFeignClient.getPriceAndDelStock(version, list, true, vip);
		} else {
			result = goodsFeignClient.getPriceAndDelStock(version, list, false, vip);
		}
		
		if(!result.isSuccess()){
			return result;
		}
		
		String totalAmount = ((Double)result.getObj() * 100) + "";
		
		PayModel payModel = new PayModel();
		payModel.setBody("中国供销-购物订单");
		payModel.setOrderId(orderId);
		payModel.setTotalAmount(totalAmount);
		payModel.setDetail(detail.toString().substring(0, detail.toString().length()-1));
		
		if(Constants.WX_PAY.equals(payType)){
			Map<String,String> paymap = payFeignClient.wxPay(openId, Integer.valueOf(info.getCenterId()), type, payModel);
			result.setObj(paymap);
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
	public ResultModel listUserOrder(Map<String, Object> param, Pagination pagination) {
		ResultModel result = new ResultModel();
		if(pagination != null){
			pagination.init();
			param.put("pagination", pagination);
		}
		
		result.setObj(orderMapper.listOrderByUser(param));
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
	public ResultModel updateOrderPayStatusByOrderId(String orderId) {
		
		ResultModel result = new ResultModel();
		
		orderMapper.updateOrderPayStatusByOrderId(orderId);
		
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

	@Override
	public List<ShoppingCart> listShoppingCart(Map<String, Object> param) {

		List<ShoppingCart> list = orderMapper.listShoppingCart(param);
		
		List<String> itemIdList = new ArrayList<String>();
		for(ShoppingCart model : list){
			itemIdList.add(model.getItemId());
		}
		
		ResultModel result = goodsFeignClient.listGoodsSpecs(1.0, itemIdList);
		if(result.isSuccess()){
			Map<String,Object> resultMap = (Map<String, Object>) result.getObj();
			List<GoodsSpecs> specsList = (List<GoodsSpecs>) resultMap.get("specsList");
			List<GoodsFile> fileList = (List<GoodsFile>) resultMap.get("pic");
			
			for(ShoppingCart model : list){
				for(GoodsSpecs specs : specsList){
					if(specs.getItemId().equals(model.getItemId())){
						model.setGoodsSpecs(specs);
						break;
					}
				}
			}
			
			for(ShoppingCart model : list){
				for(GoodsFile file : fileList){
					if(file.getGoodsId().equals(model.getGoodsSpecs().getGoodsId())){
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
}

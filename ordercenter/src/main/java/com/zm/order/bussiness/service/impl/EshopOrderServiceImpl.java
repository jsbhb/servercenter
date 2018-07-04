package com.zm.order.bussiness.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.zm.order.bussiness.dao.EshopOrderMapper;
import com.zm.order.bussiness.service.EshopOrderService;
import com.zm.order.constants.Constants;
import com.zm.order.feignclient.GoodsFeignClient;
import com.zm.order.pojo.OrderGoodsDTO;
import com.zm.order.pojo.OrderInfoDTO;
import com.zm.order.pojo.Pagination;
import com.zm.order.pojo.ResultModel;
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

@Service("eshopOrderService")
@Transactional(isolation = Isolation.READ_COMMITTED)
public class EshopOrderServiceImpl implements EshopOrderService {

	@Resource
	EshopOrderMapper eshopOrderMapper;

	@Resource
	GoodsFeignClient goodsFeignClient;

	@Override
	public ResultModel queryUserOrderList(OrderInfoDTO info, Pagination pagination) {
		ResultModel result = new ResultModel();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("info", info);
		List<OrderInfoDTO> list = eshopOrderMapper.userOrderListByParam(param);
		pagination.setTotalRows(new Long((long)list.size()));
		pagination.webListConverter();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("pagination", pagination);
		resultMap.put("orderList", list);
		result.setObj(resultMap);
		result.setSuccess(true);
		return result;
	}

	@Override
	public ResultModel queryUserOrderDetail(OrderInfoDTO info) {
		ResultModel result = new ResultModel();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("info", info);
		List<OrderInfoDTO> list = eshopOrderMapper.userOrderDetailByParam(param);
		result.setObj(list);
		result.setSuccess(true);
		return result;
	}

	@Override
	public void setUserOrderFlg(OrderInfoDTO info) {
		eshopOrderMapper.updateOrderBaseEshopIn(info);
	}

	@Override
	public ResultModel userOrderInstock(OrderInfoDTO info) {
		ResultModel result = null;
		
		//查询当前订单是否已经是进货状态
		info.setIsEshopIn(1);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("info", info);
		List<OrderInfoDTO> list = eshopOrderMapper.userOrderListByParam(param);
		if (list.size() <= 0) {
			result = new ResultModel(false, "当前订单不是进货状态，入库操作失败！");
			return result;
		}
		
		//更新当前订单为入库状态
		info.setIsEshopIn(2);
		eshopOrderMapper.updateOrderBaseEshopIn(info);
		
		//同步生成商品采购单信息
		OrderInfoDTO orderInfo = list.get(0);
		orderInfo.setOpt(info.getOpt());
		result = goodsFeignClient.createPurchaseInfo(Constants.FIRST_VERSION, orderInfo);

		return result;
	}

	@Override
	public ResultModel createSellOrderInfo(OrderInfoDTO info) {
		ResultModel result = null;
		
		//校验商品库存信息
		ResultModel checkResult = goodsFeignClient.checkSellOrderGoodsStockForEshop(Constants.FIRST_VERSION, info);
		if (!checkResult.isSuccess()) {
			return checkResult;
		}
		
		//创建销售单信息
		info.setOrderId(CommonUtils.getEshopSellOrderId(info.getMallId()+""+info.getGradeId()));
		eshopOrderMapper.insertSellOrder(info);
		eshopOrderMapper.insertSellOrderDetail(info.getOrderGoodsList());

		result = new ResultModel(true, "销售单创建成功！");
		return result;
	}

	@Override
	public ResultModel confirmSellOrderInfo(OrderInfoDTO info) {
		ResultModel result = null;
		
		//校验商品库存信息
		ResultModel checkResult = goodsFeignClient.checkSellOrderGoodsStockForEshop(Constants.FIRST_VERSION, info);
		if (!checkResult.isSuccess()) {
			return checkResult;
		}
		
		//确认销售单信息
		eshopOrderMapper.updateSellOrder(info);
		
		//扣减商品库存
		ResultModel createResult = goodsFeignClient.createSellOrderGoodsInfoForEshop(Constants.FIRST_VERSION, info);
		if (!createResult.isSuccess()) {
			return createResult;
		}

		result = new ResultModel(true, "销售单确认成功！");
		return result;
	}

	@Override
	public ResultModel querySellOrderInfo(OrderInfoDTO info) {
		ResultModel result = new ResultModel();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("info", info);
		List<OrderInfoDTO> list = eshopOrderMapper.sellOrderListByParam(param);
		result.setObj(list);
		result.setSuccess(true);
		return result;
	}

	@Override
	public ResultModel querySellOrderDetail(OrderInfoDTO info) {
		ResultModel result = new ResultModel();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("info", info);
		List<OrderInfoDTO> list = eshopOrderMapper.sellOrderDetailByParam(param);
		result.setObj(list);
		result.setSuccess(true);
		return result;
	}

	@Override
	public ResultModel querySellOrderCountInfo(OrderInfoDTO info) {
		ResultModel result = new ResultModel();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("info", info);
		OrderInfoDTO countInfo = eshopOrderMapper.selectSellOrderCountInfo(param);
		result.setObj(countInfo);
		result.setSuccess(true);
		return result;
	}

	@Override
	public ResultModel querySellOrderGoodsCountInfo(OrderInfoDTO info) {
		ResultModel result = new ResultModel();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("info", info);
		List<OrderGoodsDTO> countInfoList = eshopOrderMapper.selectSellOrderGoodsCountInfo(param);
		result.setObj(countInfoList);
		result.setSuccess(true);
		return result;
	}
}

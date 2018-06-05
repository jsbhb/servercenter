/**  
 * Project Name:ordercenter  
 * File Name:OrderBackServiceImpl.java  
 * Package Name:com.zm.order.bussiness.service.impl  
 * Date:Jan 1, 20182:49:06 PM  
 *  
 */
package com.zm.order.bussiness.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.order.bussiness.component.ShareProfitComponent;
import com.zm.order.bussiness.dao.OrderMapper;
import com.zm.order.bussiness.dao.OrderStockOutMapper;
import com.zm.order.bussiness.service.CacheAbstractService;
import com.zm.order.bussiness.service.OrderStockOutService;
import com.zm.order.common.ResultModel;
import com.zm.order.constants.Constants;
import com.zm.order.feignclient.GoodsFeignClient;
import com.zm.order.feignclient.UserFeignClient;
import com.zm.order.pojo.OrderDetail;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.OrderInfoListForDownload;
import com.zm.order.pojo.ThirdOrderInfo;
import com.zm.order.pojo.bo.ExpressMaintenanceBO;
import com.zm.order.pojo.bo.GoodsItemBO;
import com.zm.order.pojo.bo.OrderMaintenanceBO;
import com.zm.order.utils.CalculationUtils;
import com.zm.order.utils.DateUtils;
import com.zm.order.utils.JSONUtil;

/**
 * ClassName: OrderBackServiceImpl <br/>
 * Function: 后台订单操作服务类. <br/>
 * date: Jan 1, 2018 2:49:06 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class OrderStockOutServiceImpl implements OrderStockOutService {

	@Resource
	OrderStockOutMapper orderBackMapper;

	@Resource
	OrderMapper orderMapper;

	@Resource
	UserFeignClient userFeignClient;

	@Resource
	GoodsFeignClient goodsFeignClient;

	@Resource
	CacheAbstractService cacheAbstractService;

	@Resource
	ShareProfitComponent shareProfitComponent;

	@Override
	public Page<OrderInfo> queryByPage(OrderInfo entity) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("entity", entity);
		entity.init();
		if (entity.getShopId() != null) {
			List<Integer> childrenIds = userFeignClient.listChildrenGrade(Constants.FIRST_VERSION, entity.getShopId());
			param.put("list", childrenIds);
		}
		Integer count = orderBackMapper.queryCountOrderInfo(param);
		List<OrderInfo> orderList = orderBackMapper.selectForPage(param);
		entity.setTotalRows(count);
		entity.webListConverter();// 计算总页数
		Page<OrderInfo> page = new Page<OrderInfo>(entity.getCurrentPage(), entity.getNumPerPage(), count);
		page.addAll(orderList);
		page.setPages(entity.getTotalPages());
		return page;
	}

	@Override
	public OrderInfo queryByOrderId(String orderId) {
		return orderMapper.getOrderByOrderId(orderId);
	}

	@Override
	public Page<OrderGoods> queryByPageForGoods(OrderGoods entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return orderBackMapper.selectOrderGoodsForPage(entity);
	}

	@Override
	public List<ThirdOrderInfo> queryThirdInfo(String orderId) {
		return orderMapper.getThirdInfo(orderId);
	}

	@Override
	public List<OrderInfoListForDownload> queryOrdreListForDownload(String startTime, String endTime, String gradeId,
			String supplierId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		List<Integer> childrenIds = userFeignClient.listChildrenGrade(Constants.FIRST_VERSION,
				Integer.parseInt(gradeId));
		param.put("list", childrenIds);
		param.put("supplierId", supplierId);
		return orderBackMapper.selectOrdreListForDownload(param);
	}

	@Override
	public void maintenanceExpress(List<OrderMaintenanceBO> list) {
		if (list != null && list.size() > 0) {
			ThirdOrderInfo thirdOrderInfo = null;
			List<ExpressMaintenanceBO> tempList = null;
			for (OrderMaintenanceBO model : list) {
				thirdOrderInfo = new ThirdOrderInfo();
				thirdOrderInfo.setOrderStatus(model.getStatus() == null ? Constants.ORDER_DELIVER : model.getStatus());
				thirdOrderInfo.setOrderId(model.getOrderId());
				orderMapper.updateOrderStatusByThirdStatus(thirdOrderInfo);
				tempList = model.getExpressList();
				if (tempList != null && tempList.size() > 0) {
					for (ExpressMaintenanceBO express : tempList) {
						express.setOrderId(model.getOrderId());
						express.setSupplierId(model.getSupplierId());
						if (express.getId() == null) {
							orderMapper.saveThirdOrderInfo(express);
						} else {
							orderMapper.updateThirdOrderInfoById(express);
						}
					}
				}
			}
		}
	}

	@Override
	public ResultModel importOrder(List<OrderInfo> list) {
		if (list != null && list.size() > 0) {
			List<GoodsItemBO> itemList = new ArrayList<GoodsItemBO>();
			GoodsItemBO item = null;
			List<OrderGoods> goodsList = new ArrayList<OrderGoods>();
			List<OrderDetail> detailList = new ArrayList<OrderDetail>();
			for (OrderInfo info : list) {
				for (OrderGoods goods : info.getOrderGoodsList()) {
					item = new GoodsItemBO();
					item.setItemCode(goods.getItemCode());
					item.setItemId(goods.getItemId());
					item.setRetailPrice(goods.getItemPrice());
					item.setSku(goods.getSku());
					item.setConversion(goods.getConversion());
					itemList.add(item);
				}
				goodsList.addAll(info.getOrderGoodsList());
				detailList.add(info.getOrderDetail());
			}
			ResultModel result = goodsFeignClient.manualOrderGoodsCheck(Constants.FIRST_VERSION, itemList);
			if (!result.isSuccess()) {
				return result;
			}
			result = consummateOrderGoods(list, result);
			if (!result.isSuccess()) {
				return result;
			}

			orderBackMapper.insertOrderBaseBatch(list);
			orderBackMapper.insertOrderGoodsBatch(goodsList);
			orderBackMapper.insertOrderDetailBatch(detailList);
			// 统计
			for (OrderInfo info : list) {
				// 增加缓存订单数量
				cacheAbstractService.addOrderCountCache(info.getShopId(), Constants.ORDER_STATISTICS_DAY, "produce");
				// 增加月订单数
				String time = DateUtils.getTimeString("yyyyMM");
				cacheAbstractService.addOrderCountCache(info.getShopId(), Constants.ORDER_STATISTICS_MONTH, time);

				// 如果是有赞或展厅的不进行返佣计算
				if (Constants.ORDER_SOURCE_EXHIBITION.equals(info.getOrderSource())
						|| Constants.ORDER_SOURCE_YOUZAN.equals(info.getOrderSource())) {

					// 增加当天销售额
					cacheAbstractService.addSalesCache(info.getShopId(), Constants.SALES_STATISTICS_DAY, "sales",
							info.getOrderDetail().getPayment());
					// 增加月销售额
					cacheAbstractService.addSalesCache(info.getShopId(), Constants.SALES_STATISTICS_MONTH, time,
							info.getOrderDetail().getPayment());
				} else {// 其他来源的计算返佣，统计在计算时一起加上
					shareProfitComponent.calShareProfitStayToAccount(info.getOrderId());
				}
			}
			// end
			return new ResultModel(true, "操作成功");
		}
		return new ResultModel(false, "没有订单信息");
	}

	@SuppressWarnings("unchecked")
	private ResultModel consummateOrderGoods(List<OrderInfo> list, ResultModel result) {
		List<Map<String, Object>> tempList = (List<Map<String, Object>>) result.getObj();
		List<GoodsItemBO> itemList = new ArrayList<GoodsItemBO>();
		GoodsItemBO itemBO = null;
		for (Map<String, Object> map : tempList) {
			itemBO = JSONUtil.parse(JSONUtil.toJson(map), GoodsItemBO.class);
			itemList.add(itemBO);
		}
		Map<String, GoodsItemBO> tempMap = new HashMap<String, GoodsItemBO>();
		for (GoodsItemBO model : itemList) {
			tempMap.put(model.getItemId(), model);
			tempMap.put(model.getSku() + "," + model.getConversion(), model);
		}
		int orderFlag = 0;
		int supplierId = 0;
		double amount = 0;
		for (OrderInfo info : list) {
			for (OrderGoods goods : info.getOrderGoodsList()) {
				if (goods.getItemId() != null && !"".equals(goods.getItemId())) {
					itemBO = tempMap.get(goods.getItemId());
					if (itemBO == null) {
						return new ResultModel(false, "商品编号：" + goods.getItemId() + ",不存在");
					}
					goods.setSku(itemBO.getSku());
					goods.setItemCode(itemBO.getItemCode());
				} else {
					itemBO = tempMap.get(goods.getSku() + "," + goods.getConversion());
					if (itemBO == null) {
						return new ResultModel(false,
								"自有编号：" + goods.getSku() + ",换算比例：" + goods.getConversion() + ",不存在");
					}
					goods.setItemId(itemBO.getItemId());
					goods.setItemCode(itemBO.getItemCode());
				}
				goods.setItemName(itemBO.getGoodsName());
				if (goods.getItemPrice() == null || "".equals(goods.getItemPrice())) {
					goods.setItemPrice(itemBO.getRetailPrice());
					goods.setActualPrice(itemBO.getRetailPrice());
				}
				amount = CalculationUtils.add(amount,
						CalculationUtils.mul(goods.getItemPrice(), goods.getItemQuantity()));
				orderFlag = itemBO.getType();
				supplierId = itemBO.getSupplierId();
			}
			info.setOrderFlag(orderFlag);
			info.setSupplierId(supplierId);
			info.getOrderDetail().setPayment(CalculationUtils.round(2, amount));
		}
		return new ResultModel(true, null);
	}

}

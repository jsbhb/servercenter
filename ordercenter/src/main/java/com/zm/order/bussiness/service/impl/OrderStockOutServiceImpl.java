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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.order.bussiness.component.OrderComponentUtil;
import com.zm.order.bussiness.component.ThreadPoolComponent;
import com.zm.order.bussiness.dao.OrderMapper;
import com.zm.order.bussiness.dao.OrderStockOutMapper;
import com.zm.order.bussiness.service.CacheAbstractService;
import com.zm.order.bussiness.service.OrderStockOutService;
import com.zm.order.common.ResultModel;
import com.zm.order.constants.Constants;
import com.zm.order.feignclient.FinanceFeignClient;
import com.zm.order.feignclient.GoodsFeignClient;
import com.zm.order.feignclient.ThirdPartFeignClient;
import com.zm.order.feignclient.UserFeignClient;
import com.zm.order.log.LogUtil;
import com.zm.order.pojo.GoodsItemEntity;
import com.zm.order.pojo.OrderDetail;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.OrderInfoEntityForMJY;
import com.zm.order.pojo.OrderInfoListForDownload;
import com.zm.order.pojo.StockInVoucherSku;
import com.zm.order.pojo.StockOutVoucherSku;
import com.zm.order.pojo.ThirdOrderInfo;
import com.zm.order.pojo.bo.ExpressMaintenanceBO;
import com.zm.order.pojo.bo.GoodsItemBO;
import com.zm.order.pojo.bo.OrderMaintenanceBO;
import com.zm.order.pojo.bo.RebateDownload;
import com.zm.order.utils.CalculationUtils;
import com.zm.order.utils.CommonUtils;
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
	ThreadPoolComponent threadPoolComponent;

	@Resource
	ThirdPartFeignClient thirdPartFeignClient;

	@Resource
	OrderComponentUtil orderComponentUtil;

	@Resource
	FinanceFeignClient financeFeignClient;

	@Override
	public Page<OrderInfo> queryByPage(OrderInfo entity) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("entity", entity);
		entity.init();
		if (entity.getShopId() != null) {
			List<Integer> childrenIds = userFeignClient.listChildrenGrade(Constants.FIRST_VERSION, entity.getShopId());
			param.put("list", childrenIds);
		}
		// Integer count = orderBackMapper.queryCountOrderInfo(param);
		// List<OrderInfo> orderList = orderBackMapper.selectForPage(param);
		param.put("page", "true");
		List<OrderInfo> pageOrderList = orderBackMapper.selectForPage(param);
		param.put("pageNeed", "true");
		List<OrderInfo> pageNeedOrderList = orderBackMapper.selectForPage(param);
		Integer count = pageOrderList.size();
		List<String> orderIds = new ArrayList<String>();
		for (OrderInfo oi : pageNeedOrderList) {
			orderIds.add(oi.getOrderId());
		}
		param.remove("page");
		param.remove("pageNeed");
		param.put("orderIds", orderIds);
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
		return orderBackMapper.getOrderByOrderId(orderId);
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
	public com.zm.order.pojo.ResultModel importOrder(List<OrderInfo> list) {
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
			com.zm.order.pojo.ResultModel result = goodsFeignClient.manualOrderGoodsCheck(Constants.FIRST_VERSION,
					itemList);
			if (!result.isSuccess()) {
				return result;
			}
			consummateOrderGoods(list, result);
			if (!result.isSuccess()) {
				return result;
			}

			for (OrderInfo info : list) {
				orderComponentUtil.paramValidate(info, null, null, result, null);
				if (!result.isSuccess()) {
					result.setErrorMsg("订单编号：" + info.getOrderId() + "," + result.getErrorMsg());
					return result;
				}
				orderComponentUtil.renderOrderInfo(info, null, null, null, null, null, false);
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
						|| Constants.ORDER_SOURCE_YOUZAN.equals(info.getOrderSource())
						|| Constants.ORDER_SOURCE_BIG_CUSTOMER.equals(info.getOrderSource())) {

					// 增加当天销售额
					cacheAbstractService.addSalesCache(info.getShopId(), Constants.SALES_STATISTICS_DAY, "sales",
							info.getOrderDetail().getPayment());
					// 增加月销售额
					cacheAbstractService.addSalesCache(info.getShopId(), Constants.SALES_STATISTICS_MONTH, time,
							info.getOrderDetail().getPayment());
				} else {// 其他来源的计算返佣，统计在计算时一起加上
					threadPoolComponent.calShareProfitStayToAccount(info.getOrderId());
				}
			}
			// end
			return new com.zm.order.pojo.ResultModel(true, "操作成功");
		}
		return new com.zm.order.pojo.ResultModel(false, "没有订单信息");
	}

	@SuppressWarnings("unchecked")
	private void consummateOrderGoods(List<OrderInfo> list, com.zm.order.pojo.ResultModel result) {
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
		Set<Integer> supplierSet = null;// 用来判断一个订单里面的商品是否都属于同一个供应商
		Set<Integer> typeSet = null;// 用来判断一个订单里面的商品是否都属于跨境或一般贸易
		for (OrderInfo info : list) {
			double amount = 0;
			supplierSet = new HashSet<Integer>();
			typeSet = new HashSet<Integer>();
			for (OrderGoods goods : info.getOrderGoodsList()) {
				if (goods.getItemId() != null && !"".equals(goods.getItemId())) {
					itemBO = tempMap.get(goods.getItemId());
					if (itemBO == null) {
						result.setSuccess(false);
						result.setErrorMsg("商品编号：" + goods.getItemId() + ",不存在");
						return;
					}
					goods.setSku(itemBO.getSku());
					goods.setItemCode(itemBO.getItemCode());
					goods.setItemInfo(itemBO.getInfo());
				} else {
					itemBO = tempMap.get(goods.getSku() + "," + goods.getConversion());
					if (itemBO == null) {
						result.setSuccess(false);
						result.setErrorMsg("自有编号：" + goods.getSku() + ",换算比例：" + goods.getConversion() + ",不存在");
						return;
					}
					goods.setItemId(itemBO.getItemId());
					goods.setItemCode(itemBO.getItemCode());
					goods.setItemInfo(itemBO.getInfo());
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
				supplierSet.add(itemBO.getSupplierId());
				typeSet.add(itemBO.getType());
			}
			if (supplierSet.size() > 1 || typeSet.size() > 1) {
				result.setSuccess(false);
				result.setErrorMsg("订单号：" + info.getOrderId() + "商品不属于同一个仓库或者包含跨境和一般贸易");
				return;
			}
			info.setOrderFlag(orderFlag);
			info.setSupplierId(supplierId);
			info.getOrderDetail().setPayment(CalculationUtils.round(2, amount));
		}
	}

	@Override
	public ResultModel getStockInGoodsInfoByOrderId(String orderId) {
		OrderInfoEntityForMJY goodsInfo = orderBackMapper.selectStockInByOrderIdForMJY(orderId);
		if (goodsInfo.getExpectedSkuQuantity() == goodsInfo.getStockInVoucherSkus().size()) {
			// 获取业务流水号：MJYSTOCKIN+时间+4位随机数
			goodsInfo.setCode(CommonUtils.getMJYStockInOrderId());

			// 将订单中商品的goodsId都拿出来，查询每个goodsId对应最小单位的itemId
			List<String> goodsIds = new ArrayList<String>();
			for (StockInVoucherSku sivs : goodsInfo.getStockInVoucherSkus()) {
				if (!"".equals(sivs.getGoodsId().trim())) {
					goodsIds.add(sivs.getGoodsId().trim());
				} else {
					return new ResultModel(false, "订单内商品对应的goodsId为空，请修改后重试");
				}
			}
			List<GoodsItemEntity> items = goodsFeignClient.queryGoodsItemInfoByGoodsIdForEshop(Constants.FIRST_VERSION,
					goodsIds);
			if (items == null || items.size() <= 0) {
				return new ResultModel(false, "订单内商品对应的转换信息为空，请修改后重试");
			}
			// 将订单内的商品信息转换为单包装的商品信息
			GoodsItemEntity tmpConverItem;
			GoodsItemEntity tmpItem;
			for (StockInVoucherSku sivs : goodsInfo.getStockInVoucherSkus()) {
				tmpConverItem = null;
				tmpItem = null;
				for (GoodsItemEntity gie : items) {
					if (sivs.getGoodsId().equals(gie.getGoodsId())) {
						if (tmpConverItem == null && gie.getConversion() == 1) {
							tmpConverItem = gie;
						}
						if (sivs.getSkuCode().equals(gie.getItemId())) {
							tmpItem = gie;
							break;
						}
					}
				}
				if (tmpConverItem == null) {
					return new ResultModel(false, "商品编号" + sivs.getSkuCode() + "对应的单包装信息为空，请修改后重试");
				}
				if (tmpItem == null) {
					return new ResultModel(false, "商品编号" + sivs.getSkuCode() + "对应的转换信息为空，请联系技术");
				}
				Integer tmpConversionQty = sivs.getExpectedQuantity() * tmpItem.getConversion();
				sivs.setExpectedQuantity(tmpConversionQty);
				sivs.setSkuCode(tmpConverItem.getItemId());
				sivs.setPrice(tmpConverItem.getRetailPrice());
			}

			Integer tmpQty = 0;
			Map<String, Object> stockInMap = new HashMap<String, Object>();
			List<StockInVoucherSku> newStockInList = new ArrayList<StockInVoucherSku>();
			for (StockInVoucherSku sivs : goodsInfo.getStockInVoucherSkus()) {
				tmpQty = tmpQty + sivs.getExpectedQuantity();
				if (!stockInMap.containsKey(sivs.getSkuCode())) {
					stockInMap.put(sivs.getSkuCode(), sivs);
				} else {
					StockInVoucherSku tmpSku = (StockInVoucherSku) stockInMap.get(sivs.getSkuCode());
					tmpSku.setExpectedQuantity(sivs.getExpectedQuantity() + tmpSku.getExpectedQuantity());
					stockInMap.put(sivs.getSkuCode(), tmpSku);
				}
			}
			for (Map.Entry<String, Object> entry : stockInMap.entrySet()) {
				StockInVoucherSku tmpSku = (StockInVoucherSku) entry.getValue();
				newStockInList.add(tmpSku);
			}
			goodsInfo.setStockInVoucherSkus(newStockInList);
			goodsInfo.setExpectedSkuQuantity(tmpQty);

			ResultModel createResult = thirdPartFeignClient.addStoreSio(Constants.FIRST_VERSION, goodsInfo);

			if (!createResult.isSuccess()) {
				return new ResultModel(false, createResult.getErrorMsg());
			}
			// 返回成功后将orderBase的is_eshop_in状态修改
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("orderId", orderId);
			param.put("isEshopIn", 1);
			orderBackMapper.updateOrderBaseEshopIn(param);
		} else {
			return new ResultModel(false, "订单内商品种类数量不正确");
		}

		return new ResultModel(true, null);
	}

	@Override
	public ResultModel getStockOutGoodsInfoByOrderId(String orderId) {
		OrderInfoEntityForMJY goodsInfo = orderBackMapper.selectStockOutByOrderIdForMJY(orderId);
		if (goodsInfo.getExpectedSkuQuantity() == goodsInfo.getStockOutVoucherSkus().size()) {
			// 获取业务流水号：MJYSTOCKOUT+时间+4位随机数
			goodsInfo.setCode(CommonUtils.getMJYStockOutOrderId());

			// 将订单中商品的goodsId都拿出来，查询每个goodsId对应最小单位的itemId
			List<String> goodsIds = new ArrayList<String>();
			for (StockOutVoucherSku sovs : goodsInfo.getStockOutVoucherSkus()) {
				if (!"".equals(sovs.getGoodsId().trim())) {
					goodsIds.add(sovs.getGoodsId().trim());
				} else {
					return new ResultModel(false, "订单内商品对应的goodsId为空，请修改后重试");
				}
			}
			List<GoodsItemEntity> items = goodsFeignClient.queryGoodsItemInfoByGoodsIdForEshop(Constants.FIRST_VERSION,
					goodsIds);
			if (items == null || items.size() <= 0) {
				return new ResultModel(false, "订单内商品对应的转换信息为空，请修改后重试");
			}
			// 将订单内的商品信息转换为单包装的商品信息
			GoodsItemEntity tmpConverItem;
			GoodsItemEntity tmpItem;
			for (StockOutVoucherSku sovs : goodsInfo.getStockOutVoucherSkus()) {
				tmpConverItem = null;
				tmpItem = null;
				for (GoodsItemEntity gie : items) {
					if (sovs.getGoodsId().equals(gie.getGoodsId())) {
						if (tmpConverItem == null && gie.getConversion() == 1) {
							tmpConverItem = gie;
						}
						if (sovs.getSkuCode().equals(gie.getItemId())) {
							tmpItem = gie;
							break;
						}
					}
				}
				if (tmpConverItem == null) {
					return new ResultModel(false, "商品编号" + sovs.getSkuCode() + "对应的单包装信息为空，请修改后重试");
				}
				if (tmpItem == null) {
					return new ResultModel(false, "商品编号" + sovs.getSkuCode() + "对应的转换信息为空，请联系技术");
				}
				Integer tmpConversionQty = sovs.getExpectedQuantity() * tmpItem.getConversion();
				sovs.setExpectedQuantity(tmpConversionQty);
				sovs.setSkuCode(tmpConverItem.getItemId());
			}

			Integer tmpQty = 0;
			Map<String, Object> stockOutMap = new HashMap<String, Object>();
			List<StockOutVoucherSku> newStockOutList = new ArrayList<StockOutVoucherSku>();
			for (StockOutVoucherSku sovs : goodsInfo.getStockOutVoucherSkus()) {
				tmpQty = tmpQty + sovs.getExpectedQuantity();
				if (!stockOutMap.containsKey(sovs.getSkuCode())) {
					stockOutMap.put(sovs.getSkuCode(), sovs);
				} else {
					StockOutVoucherSku tmpSku = (StockOutVoucherSku) stockOutMap.get(sovs.getSkuCode());
					tmpSku.setExpectedQuantity(sovs.getExpectedQuantity() + tmpSku.getExpectedQuantity());
					stockOutMap.put(sovs.getSkuCode(), tmpSku);
				}
			}
			for (Map.Entry<String, Object> entry : stockOutMap.entrySet()) {
				StockOutVoucherSku tmpSku = (StockOutVoucherSku) entry.getValue();
				newStockOutList.add(tmpSku);
			}
			goodsInfo.setStockOutVoucherSkus(newStockOutList);
			goodsInfo.setExpectedSkuQuantity(tmpQty);

			ResultModel createResult = thirdPartFeignClient.addStoreSoo(Constants.FIRST_VERSION, goodsInfo);

			if (!createResult.isSuccess()) {
				return new ResultModel(false, createResult.getErrorMsg());
			}
			// 返回成功后将orderBase的is_eshop_in状态修改
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("orderId", orderId);
			param.put("isEshopIn", 0);
			orderBackMapper.updateOrderBaseEshopIn(param);
		} else {
			return new ResultModel(false, "订单内商品种类数量不正确");
		}

		return new ResultModel(true, null);
	}

	@Override
	public List<RebateDownload> queryForRebate(String startTime, String endTime, String gradeId) {
		// 获取该分级的下级
		List<Integer> childrenIds = userFeignClient.listChildrenGrade(Constants.FIRST_VERSION,
				Integer.parseInt(gradeId));
		// 获取订单信息
		List<RebateDownload> orderResult = listOrderForRebateDownload(startTime, endTime, gradeId, childrenIds);
		// 获取返佣信息
		List<RebateDownload> rebateResult = listRebateForDownload(orderResult, childrenIds);
		// 合并信息
		List<RebateDownload> result = mergeResult(orderResult, rebateResult);
		return result;

	}

	private List<RebateDownload> mergeResult(List<RebateDownload> orderResult, List<RebateDownload> rebateResult) {
		List<RebateDownload> result = new ArrayList<RebateDownload>();
		RebateDownload rebateDownload = null;
		for (RebateDownload rebate : rebateResult) {
			for (RebateDownload order : orderResult) {
				if (rebate.getOrderId().equals(order.getOrderId())) {
					rebateDownload = new RebateDownload();
					rebateDownload.setOrderFlag(order.getOrderFlag());
					rebateDownload.setOrderId(order.getOrderId());
					rebateDownload.setTotalRebate(rebate.getTotalRebate());
					rebateDownload.setConfirmRebateTime(rebate.getConfirmRebateTime());
					rebateDownload.setGradeId(rebate.getGradeId());
					rebateDownload.setInfo(order.getInfo());
					rebateDownload.setItemCode(order.getItemCode());
					rebateDownload.setItemId(order.getItemId());
					rebateDownload.setItemPrice(order.getItemPrice());
					rebateDownload.setQuantity(order.getQuantity());
					rebateDownload.setRebateTime(rebate.getRebateTime());
					rebateDownload.setStatus(rebate.getStatus());
					rebateDownload.setRebate(calSingleRebate(order, rebate.getGradeId()));
					rebateDownload.setGoodsName(order.getGoodsName());
					result.add(rebateDownload);
				}
			}
		}
		return result;
	}

	// 计算单个商品返佣
	@SuppressWarnings("unchecked")
	private String calSingleRebate(RebateDownload order, String gradeId) {
		String json = order.getRebate();
		if (json == null || "".equals(json)) {
			return "0";
		}
		Map<String, String> rebateMap = JSONUtil.parse(json, Map.class);
		String rebate = rebateMap.get(gradeId);
		double totalMoney = CalculationUtils.mul(order.getItemPrice(), order.getQuantity());
		try {
			return String.valueOf(CalculationUtils.round(2, CalculationUtils.mul(totalMoney, Double.valueOf(rebate))));
		} catch (Exception e) {
			LogUtil.writeErrorLog("计算单个商品返佣出错", e);
			return "0";
		}
	}

	private List<RebateDownload> listRebateForDownload(List<RebateDownload> orderResult, List<Integer> childrenIds) {
		Set<String> orderIds = new HashSet<String>();
		for (RebateDownload temp : orderResult) {
			orderIds.add(temp.getOrderId());
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("orderIds", orderIds);
		param.put("list", childrenIds);
		List<RebateDownload> rebateResult = financeFeignClient.listRebateDetailForDownload(Constants.FIRST_VERSION,
				param);
		if (rebateResult == null || rebateResult.size() == 0) {
			throw new RuntimeException("没有获取到返佣信息");
		}
		return rebateResult;
	}

	private List<RebateDownload> listOrderForRebateDownload(String startTime, String endTime, String gradeId,
			List<Integer> childrenIds) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		param.put("list", childrenIds);
		List<RebateDownload> orderResult = orderBackMapper.queryForRebate(param);
		if (orderResult == null || orderResult.size() == 0) {
			throw new RuntimeException("没有获取到返佣信息");
		}
		return orderResult;
	}

}

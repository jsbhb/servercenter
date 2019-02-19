package com.zm.goods.bussiness.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zm.goods.pojo.GoodsConvert;
import com.zm.goods.pojo.OrderBussinessModel;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.ThirdWarehouseGoods;
import com.zm.goods.pojo.WarehouseStock;
import com.zm.goods.pojo.bo.GoodsItemBO;

public interface GoodsFeignService {

	ResultModel manualOrderGoodsCheck(List<GoodsItemBO> list);
	
	/**
	 * getPriceAndDelStock:处理订单信息. <br/>
	 * 
	 * @author wqy
	 * @param list，delStock
	 * @return
	 * @since JDK 1.7
	 */
	ResultModel getPriceAndDelStock(List<OrderBussinessModel> list, Integer supplierId, boolean vip, Integer centerId,
			Integer orderFlag, String couponIds, Integer userId, boolean isFx, int platformSource, int gradeId);
	
	/**
	 * stockBack:库存回滚. <br/>
	 * 
	 * @author wqy
	 * @param
	 * @return
	 * @since JDK 1.7
	 */
	void stockBack(List<OrderBussinessModel> list, Integer orderFlag);
	
	/**
	 * stockJudge:库存判断（支付时第三方仓库）. <br/>
	 * 
	 * @author wqy
	 * @param
	 * @return
	 * @since JDK 1.7
	 */
	ResultModel stockJudge(List<OrderBussinessModel> list, Integer orderFlag, Integer supplierId);
	
	/**
	 * @fun 根据同步到的库存更新库存信息
	 */
	boolean updateThirdWarehouseStock(List<WarehouseStock> list);

	/**
	 * @fun 保存第三方商品
	 */
	boolean saveThirdGoods(List<ThirdWarehouseGoods> list);
	
	List<OrderBussinessModel> checkStock();
	
	Map<String, GoodsConvert> listSkuAndConversionByItemId(Set<String> set);

	ResultModel calStock(List<OrderBussinessModel> list, Integer supplierId, Integer orderFlag);
	
	public List<String> listPreSellItemIds();

}

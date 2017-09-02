package com.zm.goods.bussiness.service;

import java.util.List;
import java.util.Map;

import com.zm.goods.pojo.GoodsFile;
import com.zm.goods.pojo.GoodsItem;
import com.zm.goods.pojo.OrderBussinessModel;
import com.zm.goods.pojo.PriceContrast;
import com.zm.goods.pojo.ResultModel;

public interface GoodsService {

	/**  
	 * listBigTradeGoods:获取商品. <br/>  
	 *  
	 * @author wqy  
	 * @param param
	 * @return  
	 * @since JDK 1.7  
	 */
	List<GoodsItem> listGoods(Map<String,Object> param);
	
	/**  
	 * listBigTradeGoods:获取大贸海蒸鲜商品比价信息. <br/>  
	 *  
	 * @author wqy  
	 * @param param
	 * @return  
	 * @since JDK 1.7  
	 */
	List<PriceContrast> listPriceContrast(Map<String,Object> param);
	
	
	/**  
	 * listBigTradeGoods:获取大贸海蒸鲜商品菜谱. <br/>  
	 *  
	 * @author wqy  
	 * @param goodsId
	 * @return  
	 * @since JDK 1.7  
	 */
	List<GoodsFile> listGoodsFile(Integer goodsId);
	
	/**  
	 * listBigTradeGoods:获取大贸海蒸鲜商品购买也信息. <br/>  
	 *  
	 * @author wqy  
	 * @param itemId
	 * @return  
	 * @since JDK 1.7  
	 */
	Map<String,Object> tradeGoodsDetail(String itemId);
	
	/**  
	 * getPriceAndDelStock:处理订单信息. <br/>  
	 *  
	 * @author wqy  
	 * @param list，delStock
	 * @return  
	 * @since JDK 1.7  
	 */
	ResultModel getPriceAndDelStock(List<OrderBussinessModel> list, boolean delStock, boolean vip);
	
	Map<String, Object> listGoodsSpecs(List<Integer> list);
}

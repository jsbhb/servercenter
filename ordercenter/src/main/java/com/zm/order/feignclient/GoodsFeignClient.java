package com.zm.order.feignclient;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zm.order.feignclient.model.GoodsConvert;
import com.zm.order.feignclient.model.OrderBussinessModel;
import com.zm.order.pojo.GoodsItemEntity;
import com.zm.order.pojo.OrderInfoDTO;
import com.zm.order.pojo.ResultModel;
import com.zm.order.pojo.bo.DealOrderDataBO;
import com.zm.order.pojo.bo.GoodsItemBO;

@FeignClient("goodscenter")
public interface GoodsFeignClient {

	/**
	 * @fun 获取订单商品的价格和税率等信息
	 * @param version
	 *            版本默认1.0
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/for-order", method = RequestMethod.POST)
	public ResultModel getPriceAndDelStock(@PathVariable("version") Double version, @RequestBody DealOrderDataBO bo);

	@RequestMapping(value = "auth/{version}/goods/goodsSpecs", method = RequestMethod.GET)
	public ResultModel listGoodsSpecs(@PathVariable("version") Double version, @RequestParam("specsTpIds") String ids,
			@RequestParam("platformSource") int platformSource, @RequestParam("gradeId") int gradeId);

	@RequestMapping(value = "{version}/goods/stockback", method = RequestMethod.POST)
	public ResultModel stockBack(@PathVariable("version") Double version, @RequestBody List<OrderBussinessModel> list,
			@RequestParam("orderFlag") Integer orderFlag);

	@RequestMapping(value = "{version}/goods/list-itemId", method = RequestMethod.POST)
	public Map<String, GoodsConvert> listSkuAndConversionByItemId(@PathVariable("version") Double version,
			@RequestBody Set<String> set);

	@RequestMapping(value = "{version}/goods/tag/presell", method = RequestMethod.POST)
	public List<String> listPreSellItemIds(@PathVariable("version") Double version);

	@RequestMapping(value = "{version}/goods/feign/manualordergoods/check", method = RequestMethod.POST)
	public ResultModel manualOrderGoodsCheck(@PathVariable("version") Double version,
			@RequestBody List<GoodsItemBO> set);

	@RequestMapping(value = "{version}/goods/createPurchaseInfoForEshop", method = RequestMethod.POST)
	public ResultModel createPurchaseInfo(@PathVariable("version") Double version, @RequestBody OrderInfoDTO info);

	@RequestMapping(value = "{version}/goods/checkSellOrderGoodsStockForEshop", method = RequestMethod.POST)
	public ResultModel checkSellOrderGoodsStockForEshop(@PathVariable("version") Double version,
			@RequestBody OrderInfoDTO info);

	@RequestMapping(value = "{version}/goods/createSellOrderGoodsInfoForEshop", method = RequestMethod.POST)
	public ResultModel createSellOrderGoodsInfoForEshop(@PathVariable("version") Double version,
			@RequestBody OrderInfoDTO info);

	@RequestMapping(value = "{version}/goods/queryGoodsItemInfoByGoodsIdForEshop", method = RequestMethod.POST)
	public List<GoodsItemEntity> queryGoodsItemInfoByGoodsIdForEshop(@PathVariable("version") Double version,
			@RequestBody List<String> goodsIds);

	/**
	 * @fun 获取砍价订单的商品信息
	 * @param version
	 * @param list
	 * @param couponIds
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "{version}/active/bargain/goods/info", method = RequestMethod.POST)
	public ResultModel getBargainGoodsInfo(@PathVariable("version") Double version, @RequestBody DealOrderDataBO bo,
			@RequestParam(value = "id") Integer id);

	/**
	 * @fun 下单后更新对应的记录为已购买
	 * @param version
	 * @param id
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "{version}/active/bargain/goods/buy", method = RequestMethod.POST)
	public boolean updateBargainGoodsBuy(@PathVariable("version") Double version,
			@RequestParam(value = "id") Integer id, @RequestParam(value = "userId") Integer userId);
}

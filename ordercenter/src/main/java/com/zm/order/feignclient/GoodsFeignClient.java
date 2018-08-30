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
import com.zm.order.pojo.bo.GoodsItemBO;

@FeignClient("goodscenter")
public interface GoodsFeignClient {

	/**
	 * @fun 获取订单商品的价格和税率等信息
	 * @param version
	 *            版本默认1.0
	 * @param list
	 *            订单商品信息
	 * @param supplierId
	 *            供应商ID
	 * @param vip
	 *            用户是否vip
	 * @param centerId
	 *            商城ID
	 * @param orderFlag
	 *            订单类型 0跨境，2一般贸易
	 * @param couponIds
	 *            优惠券id
	 * @param userId
	 *            用户ID
	 * @param isFx
	 *            是否获取分销商品
	 * @param platformSource
	 *            订单来源，福利网站单独计算
	 * @param gradeId
	 *            分级ID 如果是福利网站需要用到
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/for-order", method = RequestMethod.POST)
	public ResultModel getPriceAndDelStock(@PathVariable("version") Double version,
			@RequestBody List<OrderBussinessModel> list, @RequestParam("supplierId") Integer supplierId,
			@RequestParam("vip") boolean vip, @RequestParam("centerId") Integer centerId,
			@RequestParam("orderFlag") Integer orderFlag,
			@RequestParam(value = "couponIds", required = false) String couponIds,
			@RequestParam(value = "userId", required = false) Integer userId, @RequestParam("isFx") boolean isFx,
			@RequestParam("platformSource") int platformSource, @RequestParam("gradeId") int gradeId);

	@RequestMapping(value = "auth/{version}/goods/goodsSpecs", method = RequestMethod.GET)
	public ResultModel listGoodsSpecs(@PathVariable("version") Double version, @RequestParam("itemIds") String ids,
			@RequestParam("source") String source, @RequestParam("platformSource") int platformSource,
			@RequestParam("gradeId") int gradeId);

	@RequestMapping(value = "auth/{version}/goods/active", method = RequestMethod.GET)
	public ResultModel getActivity(@PathVariable("version") Double version, @RequestParam("type") Integer type,
			@RequestParam("typeStatus") Integer typeStatus, @RequestParam("centerId") Integer centerId);

	@RequestMapping(value = "{version}/goods/stockback", method = RequestMethod.POST)
	public ResultModel stockBack(@PathVariable("version") Double version, @RequestBody List<OrderBussinessModel> list,
			@RequestParam("orderFlag") Integer orderFlag);

	@RequestMapping(value = "{version}/goods/costPrice", method = RequestMethod.POST)
	public Double getCostPrice(@PathVariable("version") Double version, @RequestBody List<OrderBussinessModel> list);

	@RequestMapping(value = "{version}/goods/list-itemId", method = RequestMethod.POST)
	public Map<String, GoodsConvert> listSkuAndConversionByItemId(@PathVariable("version") Double version,
			@RequestBody Set<String> set);

	@RequestMapping(value = "{version}/goods/tag/presell", method = RequestMethod.POST)
	public List<String> listPreSellItemIds(@PathVariable("version") Double version);

	@RequestMapping(value = "{version}/goods/feign/manualordergoods/check", method = RequestMethod.POST)
	public ResultModel manualOrderGoodsCheck(@PathVariable("version") Double version,
			@RequestBody List<GoodsItemBO> set);

	@RequestMapping(value = "{version}/goods/cal-stock", method = RequestMethod.POST)
	public ResultModel calStock(@PathVariable("version") Double version, @RequestBody List<OrderBussinessModel> list,
			@RequestParam("supplierId") Integer supplierId, @RequestParam("orderFlag") Integer orderFlag);

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
}

package com.zm.goods.bussiness.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.zm.goods.constants.Constants;
import com.zm.goods.exception.OriginalPriceUnEqual;
import com.zm.goods.exception.WrongPlatformSource;
import com.zm.goods.log.LogUtil;
import com.zm.goods.pojo.GoodsFile;
import com.zm.goods.pojo.GoodsItem;
import com.zm.goods.pojo.GoodsPrice;
import com.zm.goods.pojo.GoodsSpecs;
import com.zm.goods.pojo.OrderBussinessModel;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.bo.GradeBO;
import com.zm.goods.utils.CalculationUtils;
import com.zm.goods.utils.CommonUtils;
import com.zm.goods.utils.HttpClientUtil;
import com.zm.goods.utils.JSONUtil;

@Component
public class GoodsServiceComponent {

	@Resource
	RedisTemplate<String, String> template;

	/**
	 * @fun 计算订单价格，根据是否福利网站，分级ID计算不同价格
	 * @param vip
	 *            用户是否vip
	 * @param specs
	 *            商品规格详情
	 * @param model
	 *            订单商品
	 * @param promotion
	 *            促销折扣
	 * @param platformSource
	 *            平台类型
	 * @param gradeId
	 *            分级ID
	 * @return
	 * @throws WrongPlatformSource
	 * @throws OriginalPriceUnEqual
	 */
	// 计算价格
	public Double getAmount(boolean vip, GoodsSpecs specs, OrderBussinessModel model, Double promotion,
			int platformSource, int gradeId) throws WrongPlatformSource, OriginalPriceUnEqual {
		Double totalAmount = 0.0;
		getPriceInterval(specs, promotion);
		boolean calculation = false;
		Double discount = 10.0;
		if (promotion != null && promotion != 0.0) {
			discount = promotion;
		}
		discount = CalculationUtils.div(discount, 10.0);
		for (GoodsPrice price : specs.getPriceList()) {
			LogUtil.writeLog("购买数量====：" + model.getQuantity() + ",最小数量！！！！====：" + price.getMin() + ",最大数量******："
					+ price.getMax());
			// 判断购买数量是否在规定的范围内
			boolean flag = model.getQuantity() >= price.getMin()
					&& (price.getMax() == null || price.getMax() == 0 || model.getQuantity() <= price.getMax());
			if (flag) {
				totalAmount = calPrice(platformSource, gradeId, vip, model, price);
				calculation = true;
				break;
			}

		}
		if (!calculation) {
			throw new OriginalPriceUnEqual("购买数量不在指定范围内");
		}

		return CalculationUtils.mul(totalAmount, discount);
	}

	/**
	 * @fun 封装商品图片和规格信息
	 * @param goodsList
	 *            商品列表
	 * @param fileList
	 *            图片列表
	 * @param specsList
	 *            规格列表
	 * @param flag
	 *            false--搜索界面把规格按字符串拼接，true--获取商品详情，封装规格详情
	 */
	@SuppressWarnings("unchecked")
	public void packageGoodsItem(List<GoodsItem> goodsList, List<GoodsFile> fileList, List<GoodsSpecs> specsList,
			boolean flag) {

		Map<String, GoodsItem> goodsMap = new HashMap<String, GoodsItem>();
		if (goodsList == null || goodsList.size() == 0) {
			return;
		}

		for (GoodsItem item : goodsList) {
			goodsMap.put(item.getGoodsId(), item);
		}

		GoodsItem item = null;
		List<GoodsFile> tempFileList = null;
		List<GoodsSpecs> tempSpecsList = null;
		if (fileList != null && fileList.size() > 0) {
			for (GoodsFile file : fileList) {
				item = goodsMap.get(file.getGoodsId());
				if (item == null) {
					continue;
				}
				if (item.getGoodsFileList() == null) {
					tempFileList = new ArrayList<GoodsFile>();
					tempFileList.add(file);
					item.setGoodsFileList(tempFileList);
				} else {
					item.getGoodsFileList().add(file);
				}
			}
		}

		if (specsList != null && specsList.size() > 0) {
			Map<String, String> temp = null;
			Set<String> tempSet = null;
			Double discount = null;
			for (GoodsSpecs specs : specsList) {
				item = goodsMap.get(specs.getGoodsId());
				if (item == null) {
					continue;
				}
				discount = specs.getDiscount();
				getPriceInterval(specs, discount);
				if (flag) {
					if (item.getGoodsSpecsList() == null) {
						tempSpecsList = new ArrayList<GoodsSpecs>();
						tempSpecsList.add(specs);
						item.setGoodsSpecsList(tempSpecsList);
					} else {
						item.getGoodsSpecsList().add(specs);
					}

				} else {
					if (specs.getInfo() != null && !"".equals(specs.getInfo().trim())) {
						temp = JSONUtil.parse(specs.getInfo(), Map.class);
						for (Map.Entry<String, String> entry : temp.entrySet()) {
							if (item.getSpecsInfo() == null) {
								tempSet = new HashSet<String>();
								tempSet.add(entry.getValue());
								item.setSpecsInfo(tempSet);
							} else {
								item.getSpecsInfo().add(entry.getKey());
							}
						}
					}
				}
			}
		}
	}

	/**
	 * @fun 封装价格区间，获取规格的最大价格和最小价格
	 * @param specs
	 * @param discountParam
	 *            折扣
	 */
	public void getPriceInterval(GoodsSpecs specs, Double discountParam) {
		List<GoodsPrice> priceList = specs.getPriceList();
		specs.infoFilter();
		if (priceList == null) {
			return;
		}
		Double discount = 10.0;
		if (discountParam != null && discountParam != 0.0) {
			discount = discountParam;
		}
		discount = CalculationUtils.div(discount, 10.0);
		for (int i = 0; i < priceList.size(); i++) {
			if (i == 0) {
				specs.setMinPrice(priceList.get(i).getPrice());
				specs.setMaxPrice(priceList.get(i).getPrice());
				specs.setRealMinPrice(CalculationUtils.mul(priceList.get(i).getPrice(), discount));
				specs.setRealMaxPrice(CalculationUtils.mul(priceList.get(i).getPrice(), discount));
				if (priceList.get(i).getVipPrice() != null) {
					specs.setVipMinPrice(priceList.get(i).getVipPrice());
					specs.setVipMaxPrice(priceList.get(i).getVipPrice());
					specs.setRealVipMinPrice(CalculationUtils.mul(priceList.get(i).getVipPrice(), discount));
					specs.setRealVipMaxPrice(CalculationUtils.mul(priceList.get(i).getVipPrice(), discount));
				}

			} else {
				specs.setRealMinPrice(CalculationUtils
						.mul(CommonUtils.getMinDouble(specs.getMinPrice(), priceList.get(i).getPrice()), discount));
				specs.setMinPrice(CommonUtils.getMinDouble(specs.getMinPrice(), priceList.get(i).getPrice()));
				specs.setRealMaxPrice(CalculationUtils
						.mul(CommonUtils.getMaxDouble(specs.getMaxPrice(), priceList.get(i).getPrice()), discount));
				specs.setMaxPrice(CommonUtils.getMaxDouble(specs.getMaxPrice(), priceList.get(i).getPrice()));
				if (priceList.get(i).getVipPrice() != null) {
					specs.setRealVipMinPrice(CalculationUtils.mul(
							CommonUtils.getMinDouble(specs.getVipMinPrice(), priceList.get(i).getVipPrice()),
							discount));
					specs.setVipMinPrice(
							CommonUtils.getMinDouble(specs.getVipMinPrice(), priceList.get(i).getVipPrice()));
					specs.setRealVipMaxPrice(CalculationUtils.mul(
							CommonUtils.getMaxDouble(specs.getVipMaxPrice(), priceList.get(i).getVipPrice()),
							discount));
					specs.setVipMaxPrice(
							CommonUtils.getMaxDouble(specs.getVipMaxPrice(), priceList.get(i).getVipPrice()));
				}
			}
		}
	}

	public String judgeCenterId(Integer id) {
		if (Constants.PREDETERMINE_PLAT_TYPE == id) {
			return "_2B";
		}
		return "_" + id;
	}

	/**
	 * @fun 判断订单商品是否在指定购买数量内 并返回单个商品的总价
	 * @param vip
	 *            用户是否vip用户
	 * @param result
	 *            用于判断是否成功
	 * @param specs
	 *            规格
	 * @param model
	 *            订单商品
	 * @param platformSource
	 *            平台类型，0普通；1福利网站
	 * @param gradeId
	 *            分级ID
	 * @return
	 * @throws WrongPlatformSource
	 * @throws OriginalPriceUnEqual
	 */
	public Double judgeQuantityRange(boolean vip, ResultModel result, GoodsSpecs specs, OrderBussinessModel model,
			int platformSource, int gradeId) throws WrongPlatformSource, OriginalPriceUnEqual {
		Double amount = getAmount(vip, specs, model, 10.0, platformSource, gradeId);
		if (amount == null) {
			result.setSuccess(false);
			result.setErrorMsg("购买数量不在指定范围内");
		}
		return amount;
	}

	/**
	 * @fun 获取规格列表中的最低价格
	 * @param specsList
	 * @param needToPriceInterval
	 *            判断需不需要进行价格区间的封装
	 * @return
	 */
	public Map<String, Double> getMinPrice(List<GoodsSpecs> specsList, boolean needToPriceInterval) {
		Map<String, Double> result = new HashMap<String, Double>();
		if (specsList == null || specsList.size() == 0) {
			result.put("price", 0.0);
			result.put("realPrice", 0.0);
			return result;
		}
		if (!needToPriceInterval) {
			for (GoodsSpecs specs : specsList) {
				getPriceInterval(specs, specs.getDiscount());
			}
		}
		int len = specsList.size();

		for (int i = 0; i < len; i++) {
			if (i == 0) {
				result.put("price", specsList.get(i).getMinPrice());
				if (specsList.get(i).getWelfarePrice() > 0) {
					result.put("realPrice", specsList.get(i).getWelfarePrice());
				} else {
					result.put("realPrice", specsList.get(i).getRealMinPrice());
				}
			} else {
				if (specsList.get(i).getMinPrice() < result.get("price")) {
					result.put("price", specsList.get(i).getMinPrice());
				}
				if (specsList.get(i).getWelfarePrice() > 0) {
					if (specsList.get(i).getWelfarePrice() < result.get("realPrice")) {
						result.put("realPrice", specsList.get(i).getWelfarePrice());
					}
				} else {
					if (specsList.get(i).getRealMinPrice() < result.get("realPrice")) {
						result.put("realPrice", specsList.get(i).getRealMinPrice());
					}
				}
			}
		}
		return result;
	}

	private Double calPrice(int platformSource, int gradeId, boolean vip, OrderBussinessModel model, GoodsPrice price)
			throws WrongPlatformSource, OriginalPriceUnEqual {
		// 判断原价是否相等
		if (!model.getItemPrice().equals(price.getPrice())) {
			throw new OriginalPriceUnEqual("itemId = " + price.getItemId() + "原价匹配不正确");
		}
		switch (platformSource) {
		case Constants.WELFARE_WEBSITE:
			return getWelfareWebsitePrice(gradeId, vip, model, price);
		case Constants.BACK_MANAGER_WEBSITE:
			return getBackManagerWebsitePrice(gradeId, vip, model, price);
		default:
			return getDefaultPrice(vip, model, price);
		}
	}

	// 计算后台去掉本级返佣后的价格
	private Double getBackManagerWebsitePrice(int gradeId, boolean vip, OrderBussinessModel model, GoodsPrice price) {
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		Map<String, String> goodsRebate = hashOperations.entries(Constants.GOODS_REBATE + model.getItemId());
		try {
			GradeBO gradeBO = JSONUtil.parse(hashOperations.get(Constants.GRADEBO_INFO, gradeId + ""), GradeBO.class);
			int gradeType = gradeBO.getGradeType();
			// 获取该类型的返佣的比例
			double proportion = Double
					.valueOf(goodsRebate.get(gradeType + "") == null ? "0" : goodsRebate.get(gradeType + ""));
			double amount = CalculationUtils.mul(
					(vip ? (price.getVipPrice() == null ? 0 : price.getVipPrice()) : price.getPrice()),
					CalculationUtils.sub(1, proportion));// 扣除返佣后的单价
			amount = CalculationUtils.round(2, amount); // 保留2为小数
			amount = CalculationUtils.mul(model.getQuantity(), amount);// 单价乘以数量
			return amount;
		} catch (Exception e) {
			double amount = CalculationUtils.mul(model.getQuantity(),
					(vip ? (price.getVipPrice() == null ? 0 : price.getVipPrice()) : price.getPrice()));
			return amount;
		}
	}

	private Double getDefaultPrice(boolean vip, OrderBussinessModel model, GoodsPrice price) {
		return CalculationUtils.mul(model.getQuantity(),
				(vip ? (price.getVipPrice() == null ? 0 : price.getVipPrice()) : price.getPrice()));
	}

	private final int GRADESTYLE_WELFARE_WEBSITE = 1;// 福利商城标志位

	private Double getWelfareWebsitePrice(int gradeId, boolean vip, OrderBussinessModel model, GoodsPrice price)
			throws WrongPlatformSource {
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		Map<String, String> goodsRebate = hashOperations.entries(Constants.GOODS_REBATE + model.getItemId());
		GradeBO gradeBO = JSONUtil.parse(hashOperations.get(Constants.GRADEBO_INFO, gradeId + ""), GradeBO.class);
		if (gradeBO.getWelfareType() != GRADESTYLE_WELFARE_WEBSITE) {
			throw new WrongPlatformSource("该分级已经不是福利网站类型");
		}
		double welfareRebate = gradeBO.getWelfareRebate();// 福利网站拿的返佣比例
		int gradeType = gradeBO.getGradeType();
		// 获取该类型的返佣的比例
		double proportion = Double
				.valueOf(goodsRebate.get(gradeType + "") == null ? "0" : goodsRebate.get(gradeType + ""));
		double temp = CalculationUtils.mul(proportion, CalculationUtils.sub(1, welfareRebate));// 扣除福利网站自己的返佣后
		double amount = CalculationUtils.mul(
				(vip ? (price.getVipPrice() == null ? 0 : price.getVipPrice()) : price.getPrice()),
				CalculationUtils.sub(1, temp));// 商品单价
		amount = CalculationUtils.round(2, amount);//保留2位小数
		amount = CalculationUtils.mul(amount, model.getQuantity());//单价乘以数量
		return amount;
	}

	/**
	 * @fun 获取福利网站的福利价格
	 * @param specs
	 * @param discount
	 * @param gradeId
	 * @throws WrongPlatformSource
	 */
	public void getWelfareWebsitePriceInterval(GoodsSpecs specs, Double discount, int gradeId)
			throws WrongPlatformSource {
		getPriceInterval(specs, discount);
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		Map<String, String> goodsRebate = hashOperations.entries(Constants.GOODS_REBATE + specs.getItemId());
		String json = hashOperations.get(Constants.GRADEBO_INFO, gradeId + "");
		if (json == null || "".equals(json)) {
			throw new WrongPlatformSource("该分级已经不是福利网站类型");
		}
		GradeBO gradeBO = JSONUtil.parse(json, GradeBO.class);
		if (gradeBO.getWelfareType() != GRADESTYLE_WELFARE_WEBSITE) {
			throw new WrongPlatformSource("该分级已经不是福利网站类型");
		}
		double welfareRebate = gradeBO.getWelfareRebate();// 福利网站拿的返佣比例
		int gradeType = gradeBO.getGradeType();
		// 获取该类型的返佣的比例
		double proportion = Double
				.valueOf(goodsRebate.get(gradeType + "") == null ? "0" : goodsRebate.get(gradeType + ""));
		double temp = CalculationUtils.mul(proportion, CalculationUtils.sub(1, welfareRebate));// 扣除福利网站自己的返佣后
		double welfarePrice = CalculationUtils.mul(specs.getPriceList().get(0).getPrice(),
				CalculationUtils.sub(1, temp));
		specs.setWelfarePrice(welfarePrice);
	}

	/**
	 * @fun 获取福利网站的福利价格
	 * @param specs
	 * @param discount
	 * @param gradeId
	 * @throws WrongPlatformSource
	 */
	public void getBackWebsitePriceInterval(GoodsSpecs specs, Double discount, int gradeId) {
		getPriceInterval(specs, discount);
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		Map<String, String> goodsRebate = hashOperations.entries(Constants.GOODS_REBATE + specs.getItemId());
		String json = hashOperations.get(Constants.GRADEBO_INFO, gradeId + "");
		GradeBO gradeBO;
		try {
			gradeBO = JSONUtil.parse(json, GradeBO.class);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		int gradeType = gradeBO.getGradeType();
		// 获取该类型的返佣的比例
		double proportion = Double
				.valueOf(goodsRebate.get(gradeType + "") == null ? "0" : goodsRebate.get(gradeType + ""));
		double backPrice = CalculationUtils.mul(specs.getPriceList().get(0).getPrice(),
				CalculationUtils.sub(1, proportion));
		specs.setWelfarePrice(backPrice);
	}

	public void packDetailPath(List<GoodsItem> itemList) {
		for (GoodsItem item : itemList) {
			if (item.getDetailPath() != null) {
				item.setDetailList(getPicPath(HttpClientUtil.get(item.getDetailPath())));
			}
		}
	}

	private List<String> getPicPath(String html) {
		List<String> srcList = new ArrayList<String>(); // 用来存储获取到的图片地址
		Pattern p = Pattern.compile("<(img|IMG)(.*?)(>|></img>|/>)");// 匹配字符串中的img标签
		Matcher matcher = p.matcher(html);
		boolean hasPic = matcher.find();
		if (hasPic == true) {// 判断是否含有图片
			while (hasPic) {
				String group = matcher.group(2);// 获取第二个分组的内容，也就是 (.*?)匹配到的
				Pattern srcText = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");// 匹配图片的地址
				Matcher matcher2 = srcText.matcher(group);
				if (matcher2.find()) {
					srcList.add(matcher2.group(3));// 把获取到的图片地址添加到列表中
				}
				hasPic = matcher.find();// 判断是否还有img标签
			}
		}
		return srcList;
	}
}

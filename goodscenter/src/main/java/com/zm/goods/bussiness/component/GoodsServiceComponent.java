package com.zm.goods.bussiness.component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.zm.goods.bussiness.dao.GoodsMapper;
import com.zm.goods.constants.Constants;
import com.zm.goods.enummodel.AutoSelectionModeEnum;
import com.zm.goods.exception.WrongPlatformSource;
import com.zm.goods.pojo.bo.AutoSelectionBO;
import com.zm.goods.pojo.bo.DealOrderDataBO;
import com.zm.goods.pojo.bo.GradeBO;
import com.zm.goods.pojo.po.Goods;
import com.zm.goods.pojo.po.GoodsSpecsTradePattern;
import com.zm.goods.pojo.po.Items;
import com.zm.goods.utils.CalculationUtils;
import com.zm.goods.utils.HttpClientUtil;
import com.zm.goods.utils.JSONUtil;

@Component
public class GoodsServiceComponent {

	@Resource
	RedisTemplate<String, String> template;

	@Resource
	GoodsMapper goodsMapper;

	private final int GRADESTYLE_WELFARE_WEBSITE = 1;// 福利商城标志位

	/**
	 * @fun 获取福利网站的福利价格
	 * @param specs
	 * @param discount
	 * @param gradeId
	 * @throws WrongPlatformSource
	 */
	public void getWelfareWebsitePriceInterval(GoodsSpecsTradePattern specs, Double discount, int gradeId)
			throws WrongPlatformSource {
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		Map<String, String> goodsRebate = hashOperations.entries(Constants.GOODS_REBATE + specs.getSpecsTpId());
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
		double welfarePrice = CalculationUtils.mul(specs.getRetailPrice(), CalculationUtils.sub(1, temp));
		specs.setRetailPrice(welfarePrice);
	}

	/**
	 * @fun 获取福利网站的福利价格
	 * @param specs
	 * @param discount
	 * @param gradeId
	 * @throws WrongPlatformSource
	 */
	public void getBackWebsitePriceInterval(GoodsSpecsTradePattern specs, Double discount, int gradeId) {
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
		double backPrice = CalculationUtils.mul(specs.getRetailPrice(), CalculationUtils.sub(1, proportion));
		specs.setRetailPrice(backPrice);
	}

	public void packDetailPath(Goods item) {
		if (item.getDetailPath() != null) {
			item.setDetailList(getPicPath(HttpClientUtil.get(item.getDetailPath())));
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

	/**
	 * @fun 跨境批量上架自动选择itemId
	 * @param specsTpIds
	 * @param mode
	 * @return
	 */
	public List<AutoSelectionBO> AutoSelectItemId(List<String> specsTpIds, AutoSelectionModeEnum mode) {
		switch (mode) {
		case INTERNAL_PRICE_LOWEST:
			return selectInternalPriceLowest(specsTpIds);
		default:
			return null;
		}
	}

	/**
	 * @fun 按照内供价最低的要求进行itemId分配
	 * @param specsTpIds
	 * @return
	 */
	private List<AutoSelectionBO> selectInternalPriceLowest(List<String> specsTpIds) {
		List<Items> itemsList = goodsMapper.listItemsBySpecsTpIds(specsTpIds);
		// 根据specsTpId分组
		Map<String, List<Items>> map = itemsList.stream().collect(Collectors.groupingBy(Items::getSpecsTpId));
		List<AutoSelectionBO> boList = new ArrayList<>();
		AutoSelectionBO bo = null;
		for (Map.Entry<String, List<Items>> entry : map.entrySet()) {
			Items item = entry.getValue().stream().sorted(Comparator.comparing(Items::getInternalPrice)).findFirst()
					.get(); 
			bo = new AutoSelectionBO();
			bo.setItemId(item.getItemId());
			bo.setSpecsTpId(item.getSpecsTpId());
			boList.add(bo);
		}
		return boList;
	}

	/**
	 * @fun 一般贸易订单自动选择itemId
	 * @param bo
	 * @param mode
	 * @return
	 */
	public List<AutoSelectionBO> AutoSelectForOrder(DealOrderDataBO bo, AutoSelectionModeEnum mode) {
		switch (mode) {
		case INTERNAL_PRICE_LOWEST:
			return selectInternalPriceLowest(bo);
		case DEFAULT:
			return selectDefault(bo);
		}
		return null;
	}

	/**
	 * @FUN 默认该商品有多个供应商时人工介入
	 * @param bo
	 * @return
	 */
	private List<AutoSelectionBO> selectDefault(DealOrderDataBO bo) {
		List<String> specsTpIds = bo.getModelList().stream().map(m -> m.getSpecsTpId()).collect(Collectors.toList());
		List<Items> itemsList = goodsMapper.listItemsBySpecsTpIds(specsTpIds);
		List<AutoSelectionBO> boList = new ArrayList<>();
		if (specsTpIds.size() == itemsList.size()) {// 相等说明一一对应，不相等说明一个商品对应多个供应商
			AutoSelectionBO tbo = null;
			for (Items items : itemsList) {
				tbo = new AutoSelectionBO();
				tbo.setItemId(items.getItemId());
				tbo.setSpecsTpId(items.getSpecsTpId());
				boList.add(tbo);
			}
		}
		return boList;
	}

	/**
	 * @fun 按照内供价最低
	 * @param bo
	 * @return
	 */
	private List<AutoSelectionBO> selectInternalPriceLowest(DealOrderDataBO bo) {
		return null;
	}
}

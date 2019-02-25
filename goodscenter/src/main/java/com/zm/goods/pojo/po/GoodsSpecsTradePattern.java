package com.zm.goods.pojo.po;

/**
 * @fun 对应数据库表kj_goods_specs_tradepattern
 * @author user
 *
 */
public class GoodsSpecsTradePattern {

	private Integer id;
	
	private String specsTpId;
	
	private String goodsId;
	
	private String specsId;//规格ID
	
	private int status;//0:初始,1：下架，2：上架，3：售罄
	
	private int freePost;//是否包邮;0:否,1:是
	
	private int freeTax;//是否包税,0:否,1是
	
	private int tagRatio;//权重
	
	private double incrementTax;//增值税
	
	private double tariff;//关税
	
	private double exciseTax;//消费税
	
	private int promotion;//是否促销0:否;1是
	
	private double discount;//促销折扣
	
	private boolean welfare;//是否在福利商城显示0:否;1:是
	
	private boolean distribution;//是否分销0:否;1:是
	
	private boolean combinedgoods;//是否组合商品0:否;1:是
	
	private String combinedSpecsTpId;//组合商品包含的specs_tp_id
	
	private int display;//显示(针对上架商品):0:不显示;1:前端显示;2:后台显示;3:前后台都显示
	
	private double distributionPrice;//分销价格
	
	private double vipPrice;//会员价
	
	private double retailPrice;//零售价
	
	private double linePrice;//划线价
	
	private double instantRatio;//顺加比例
	
	private int saleNum;//销售数量
	
	private String itemId;//跨境商品上架需绑定对应ItemId
	
	private boolean fresh;//是否是新品:0是，1：否
	
	private String upshelfTime;//上架时间
	
	private int stock;
	
	private String createTime;
	
	private String updateTime;
	
	private String opt;

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSpecsTpId() {
		return specsTpId;
	}

	public void setSpecsTpId(String specsTpId) {
		this.specsTpId = specsTpId;
	}

	public String getSpecsId() {
		return specsId;
	}

	public void setSpecsId(String specsId) {
		this.specsId = specsId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getFreePost() {
		return freePost;
	}

	public void setFreePost(int freePost) {
		this.freePost = freePost;
	}

	public int getFreeTax() {
		return freeTax;
	}

	public void setFreeTax(int freeTax) {
		this.freeTax = freeTax;
	}

	public int getTagRatio() {
		return tagRatio;
	}

	public void setTagRatio(int tagRatio) {
		this.tagRatio = tagRatio;
	}

	public double getIncrementTax() {
		return incrementTax;
	}

	public void setIncrementTax(double incrementTax) {
		this.incrementTax = incrementTax;
	}

	public double getTariff() {
		return tariff;
	}

	public void setTariff(double tariff) {
		this.tariff = tariff;
	}

	public double getExciseTax() {
		return exciseTax;
	}

	public void setExciseTax(double exciseTax) {
		this.exciseTax = exciseTax;
	}

	public int getPromotion() {
		return promotion;
	}

	public void setPromotion(int promotion) {
		this.promotion = promotion;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public boolean isWelfare() {
		return welfare;
	}

	public void setWelfare(boolean welfare) {
		this.welfare = welfare;
	}

	public boolean isDistribution() {
		return distribution;
	}

	public void setDistribution(boolean distribution) {
		this.distribution = distribution;
	}

	public boolean isCombinedgoods() {
		return combinedgoods;
	}

	public void setCombinedgoods(boolean combinedgoods) {
		this.combinedgoods = combinedgoods;
	}

	public String getCombinedSpecsTpId() {
		return combinedSpecsTpId;
	}

	public void setCombinedSpecsTpId(String combinedSpecsTpId) {
		this.combinedSpecsTpId = combinedSpecsTpId;
	}

	public int getDisplay() {
		return display;
	}

	public void setDisplay(int display) {
		this.display = display;
	}

	public double getDistributionPrice() {
		return distributionPrice;
	}

	public void setDistributionPrice(double distributionPrice) {
		this.distributionPrice = distributionPrice;
	}

	public double getVipPrice() {
		return vipPrice;
	}

	public void setVipPrice(double vipPrice) {
		this.vipPrice = vipPrice;
	}

	public double getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(double retailPrice) {
		this.retailPrice = retailPrice;
	}

	public double getLinePrice() {
		return linePrice;
	}

	public void setLinePrice(double linePrice) {
		this.linePrice = linePrice;
	}

	public double getInstantRatio() {
		return instantRatio;
	}

	public void setInstantRatio(double instantRatio) {
		this.instantRatio = instantRatio;
	}

	public int getSaleNum() {
		return saleNum;
	}

	public void setSaleNum(int saleNum) {
		this.saleNum = saleNum;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public boolean isFresh() {
		return fresh;
	}

	public void setFresh(boolean fresh) {
		this.fresh = fresh;
	}

	public String getUpshelfTime() {
		return upshelfTime;
	}

	public void setUpshelfTime(String upshelfTime) {
		this.upshelfTime = upshelfTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}
	
	
}

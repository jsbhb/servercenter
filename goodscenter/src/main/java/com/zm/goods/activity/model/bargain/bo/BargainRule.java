package com.zm.goods.activity.model.bargain.bo;

import java.text.ParseException;
import java.util.Random;

import com.zm.goods.activity.inf.ActiveRule;
import com.zm.goods.activity.model.bargain.base.IUserBargain;
import com.zm.goods.exception.ActiviteyException;
import com.zm.goods.utils.CalculationUtils;
import com.zm.goods.utils.DateUtil;

public class BargainRule implements ActiveRule<IUserBargain> {

	private String itemId;// 商品itemId
	private double initPrice;// 初始价格
	private double floorPrice;// 底价
	private int minRatio;// 每砍一刀最小比例
	private int maxRatio;// 每砍一刀最大比例
	private int maxCount;// 该商品最多被砍几刀，0：无限
	private int firstMinRatio;// 第一刀最小比例
	private int firstMaxRatio;// 第一刀最大比例
	private int type;// 0:普通，1：接龙
	private int duration;// 持续时间（小时）
	private double lessMinPrice;// 砍价时，现价和底价小于该值时直接砍到底价
	private Random ratio;// 砍价时的随机比例

	@Override
	public final void ruleInit() {
		// 生成随机数random初始化
		ratio = new Random();
	}

	@Override
	public final void processRuleJudge(IUserBargain ientity) throws ActiviteyException {
		UserBargainEntity entity = (UserBargainEntity) ientity;
		if (isRepeatBargain(entity)) {// 如果该userId已经砍过
			throw new ActiviteyException("你已经帮好友砍过价", 4);
		}
		if (isReachFloorPrice(entity)) {// 是否达到底价
			throw new ActiviteyException("该砍价商品已达底价，请尽快购买", 1);
		}
		if (isOverTime(entity)) {// 时间是否超时
			throw new ActiviteyException("该砍价商品已经结束，请尽快购买", 2);
		}
		if (!entity.isStart()) {
			throw new ActiviteyException("该砍价商品已经结束，请尽快购买", 3);
		}
		if (maxCount > 0) {// 是否规定了刀数
			int currentCount = entity.getRecordList() == null ? 0 : entity.getRecordList().size();
			if (currentCount >= maxCount) {
				throw new ActiviteyException("砍价次数已经达到最大", 0);
			}
		}
	}

	private final boolean isRepeatBargain(UserBargainEntity entity) {
		if (entity.getRecordList() == null || entity.getRecordList().size() == 0)
			return false;
		return entity.getRecordList().stream().anyMatch(record -> record.getUserId() == entity.getUserId());
	}

	private final boolean isOverTime(UserBargainEntity entity) throws ActiviteyException {
		if (entity.getRecordList() == null || entity.getRecordList().size() == 0)
			return false;
		try {
			return DateUtil.isAfter(entity.getCreateTime(), duration);
		} catch (ParseException e) {
			throw new ActiviteyException("你的开团时间程序员小哥哥忘记记录啦", 5);
		}
	}

	private final boolean isReachFloorPrice(UserBargainEntity entity) {
		if (entity.getRecordList() == null || entity.getRecordList().size() == 0)
			return false;
		double bargainPrice = entity.getRecordList().stream().mapToDouble(BargainRecord::getBargainPrice).sum();
		if (CalculationUtils.sub(initPrice, bargainPrice) <= floorPrice)
			return true;
		return false;
	}

	@Override
	public final void finalRuleJudge(IUserBargain ientity) throws ActiviteyException {
		// TODO Auto-generated method stub

	}

	public double getRatio() {
		return CalculationUtils.div(ratio.nextInt(maxRatio - minRatio) + minRatio, 100);
	}

	public double getFirstRatio() {
		return CalculationUtils.div(ratio.nextInt(firstMaxRatio - firstMinRatio) + firstMinRatio, 100);
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public double getInitPrice() {
		return initPrice;
	}

	public void setInitPrice(double initPrice) {
		this.initPrice = initPrice;
	}

	public double getFloorPrice() {
		return floorPrice;
	}

	public void setFloorPrice(double floorPrice) {
		this.floorPrice = floorPrice;
	}

	public double getMinRatio() {
		return minRatio;
	}

	public void setMinRatio(int minRatio) {
		this.minRatio = minRatio;
	}

	public double getMaxRatio() {
		return maxRatio;
	}

	public void setMaxRatio(int maxRatio) {
		this.maxRatio = maxRatio;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public double getFirstMinRatio() {
		return firstMinRatio;
	}

	public void setFirstMinRatio(int firstMinRatio) {
		this.firstMinRatio = firstMinRatio;
	}

	public double getFirstMaxRatio() {
		return firstMaxRatio;
	}

	public void setFirstMaxRatio(int firstMaxRatio) {
		this.firstMaxRatio = firstMaxRatio;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public double getLessMinPrice() {
		return lessMinPrice;
	}

	public void setLessMinPrice(double lessMinPrice) {
		this.lessMinPrice = lessMinPrice;
	}

}

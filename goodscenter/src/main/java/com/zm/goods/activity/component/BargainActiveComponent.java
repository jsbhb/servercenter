package com.zm.goods.activity.component;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

import com.zm.goods.activity.inf.AbstractActive;
import com.zm.goods.activity.inf.ActiveRule;
import com.zm.goods.activity.model.bargain.BargainRecord;
import com.zm.goods.activity.model.bargain.BargainRule;
import com.zm.goods.activity.model.bargain.UserBargainEntity;
import com.zm.goods.activity.model.bargain.base.IUserBargain;
import com.zm.goods.exception.ActiviteyException;
import com.zm.goods.utils.JSONUtil;
import com.zm.goods.utils.Number;
import com.zm.goods.utils.SpringContextUtil;

public class BargainActiveComponent extends AbstractActive<BargainRecord, IUserBargain> {

	private String id;// 每一个团的唯一ID

	private final String BARGAIN_ACTIVE_PER = "bargain_active:";// 存入redis的前缀
	
	private RedisTemplate<String, String> template;

	@SuppressWarnings("unchecked")
	public BargainActiveComponent(ActiveRule<IUserBargain> rule, String lock, boolean start) {
		super(start, rule);
		this.id = lock;
		template = (RedisTemplate<String, String>) SpringContextUtil.getBean("template");
	}

	@Override
	public BargainRecord processHandle(IUserBargain ientity) throws ActiviteyException {
		UserBargainEntity entity = (UserBargainEntity) ientity;
		synchronized (id) {// 第一次判断根据初始传进来的值进行判断，通过后进入同步状态，根据每一个开团的ID进行同步
			String json = template.opsForValue().get(BARGAIN_ACTIVE_PER + id);// 获取最新的砍价记录
			if (json != null) {// 如果有，不是第一次
				entity = JSONUtil.parse(json, UserBargainEntity.class);// 获取最新数据
				activeRule.processRuleJudge(entity);// 拿最新的数据进行规则判断，双重检验
			}
			BargainRule rule = (BargainRule) activeRule;
			BargainRecord record = new BargainRecord();
			record.setBuy(false);
			record.setUserId(entity.getUserId());
			if (entity.getRecordList() == null || entity.getRecordList().size() == 0) {// 第一刀
				double bargainPrice = new Number(entity.getInitPrice() + "").sub(entity.getFloorPrice() + "")
						.mul(rule.getFirstRatio() + "").round(2).parseToDouble();
				record.setBargainPrice(bargainPrice);
				entity.setBargainRecord(record);
				template.opsForValue().set(BARGAIN_ACTIVE_PER + id, JSONUtil.toJson(entity), rule.getDuration() + 1,
						TimeUnit.HOURS);//设置超时时间，砍价结束后清除redis，由于更新的时候超时时间也会更新，所以会在结束后再存在redis内一段时间
				return record;
			} else {
				List<BargainRecord> recordList = entity.getRecordList();
				double bargainTotalPrice = recordList.stream().mapToDouble(BargainRecord::getBargainPrice).sum();
				if (rule.getMaxCount() > 0) {
					if (rule.getMaxCount() - recordList.size() == 1) {// 最后一刀
						record.setBargainPrice(new Number(entity.getInitPrice() + "").sub(entity.getFloorPrice() + "")
								.sub(bargainTotalPrice + "").round(2).parseToDouble());
						entity.setBargainRecord(record);
						template.opsForValue().set(BARGAIN_ACTIVE_PER + id, JSONUtil.toJson(entity), rule.getDuration() + 1,
								TimeUnit.HOURS);
						return record;
					}
				}
				//剩余金额
				double residue = new Number(entity.getInitPrice() + "").sub(entity.getFloorPrice() + "")
						.sub(bargainTotalPrice + "").round(2).parseToDouble();
				if(residue <= rule.getLessMinPrice()){
					record.setBargainPrice(residue);
					entity.setBargainRecord(record);
				} else {
					double bargainPrice = new Number(residue+ "")
							.mul(rule.getRatio() + "").round(2).parseToDouble();
					record.setBargainPrice(bargainPrice);
					entity.setBargainRecord(record);
				}
				template.opsForValue().set(BARGAIN_ACTIVE_PER + id, JSONUtil.toJson(entity), rule.getDuration() + 1,
						TimeUnit.HOURS);
				return record;
			}
		}
	}

	@Override
	public BargainRecord finalHandle(IUserBargain entity) throws ActiviteyException {

		return null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}

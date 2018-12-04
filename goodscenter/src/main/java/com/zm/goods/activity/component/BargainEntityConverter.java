package com.zm.goods.activity.component;

import java.util.ArrayList;
import java.util.List;

import com.zm.goods.activity.model.bargain.BargainRecord;
import com.zm.goods.activity.model.bargain.BargainRule;
import com.zm.goods.activity.model.bargain.po.BargainRecordPO;
import com.zm.goods.activity.model.bargain.po.BargainRulePO;
import com.zm.goods.activity.model.bargain.po.UserBargainPO;
import com.zm.goods.activity.model.bargain.vo.BargainGoods;
import com.zm.goods.activity.model.bargain.vo.MyBargain;
import com.zm.goods.activity.model.bargain.vo.MyBargainRecord;
import com.zm.goods.utils.CalculationUtils;

/**
 * @fun 砍价活动实体类互相转换工具类
 * @author user
 *
 */
public class BargainEntityConverter {

	public final List<MyBargain> userBargainPO2MyBargain(List<UserBargainPO> list){
		if(list == null || list.size() == 0){
			return null;
		}
		List<MyBargain> result = new ArrayList<MyBargain>();
		for(UserBargainPO po : list){
			result.add(setMyBargain(po));
		}
		return result;
	}
	
	public final MyBargain userBargainPO2MyBargain(UserBargainPO po){
		
		return setMyBargain(po);
	}

	private MyBargain setMyBargain(UserBargainPO po) {
		MyBargain my = new MyBargain();
		List<MyBargainRecord> temp;
		my.setId(po.getId());
		my.setGoodsPrice(po.getInitPrice());//商品原价
		my.setLowPrice(po.getFloorPrice());//商品底价
		my.setDuration(po.getDuration());//砍价持续时间
		my.setItemId(po.getItemId());//商品itemId，用于中间件获取商品属性
		my.setUserId(po.getUserId());//userId，用于中间件获取用户头像
		my.setStart(po.isStart());
		if(po.getBargainList() != null){
			temp = new ArrayList<MyBargainRecord>();
			MyBargainRecord record = null;
			double bargainPrice = 0.0;
			for(BargainRecordPO recordPO : po.getBargainList()){
				record = new MyBargainRecord();
				record.setBargainPrice(recordPO.getBargainPrice());//每次砍价金额记录
				record.setId(recordPO.getId());
				record.setUserId(recordPO.getUserId());//用户ID，用于中间件获取用户信息
				record.setBuy(recordPO.isBuy());
				temp.add(record);
				bargainPrice = CalculationUtils.add(bargainPrice, recordPO.getBargainPrice());//计算已经砍价的金额
			}
			my.setBargainList(temp);//设置砍价记录
			my.setBargainPrice(bargainPrice);//设置已经砍价金额
		}
		return my;
	}
	
	public final List<BargainGoods> BargainRulePO2BargainGoods(List<BargainRulePO> list){
		if(list == null){
			return null;
		}
		List<BargainGoods> result = new ArrayList<BargainGoods>();
		list.forEach(po ->{
			BargainGoods goods = new BargainGoods();
			goods.setItemId(po.getItemId());
			goods.setGoodsPrice(po.getInitPrice());
			goods.setBargainCount(po.getCount());
			goods.setGoodsRoleId(po.getId());
			result.add(goods);
		});
		return result;
	}
	
	public final BargainRule BargainRulePO2BargainRule(BargainRulePO po){
		BargainRule rule = new BargainRule();
		rule.setDuration(po.getDuration());
		rule.setFirstMaxRatio(po.getFirstMaxRatio());
		rule.setFirstMinRatio(po.getFirstMinRatio());
		rule.setFloorPrice(po.getFloorPrice());
		rule.setInitPrice(po.getInitPrice());
		rule.setItemId(po.getItemId());
		rule.setLessMinPrice(po.getLessMinPrice());
		rule.setMaxCount(po.getMaxCount());
		rule.setMaxRatio(po.getMaxRatio());
		rule.setMinRatio(po.getMinRatio());
		rule.setType(po.getType());
		return rule;
	}
	
	public final UserBargainPO BargainRulePO2UserBargainPO(Integer userId, BargainRulePO po){
		UserBargainPO userBargainPO = new UserBargainPO();
		userBargainPO.setFloorPrice(po.getFloorPrice());
		userBargainPO.setGoodsRoleId(po.getId());
		userBargainPO.setInitPrice(po.getInitPrice());
		userBargainPO.setUserId(userId);
		userBargainPO.setStart(true);
		return userBargainPO;
	}
	
	public final BargainRecordPO BargainRecord2BargainRecordPO(int id, BargainRecord record){
		BargainRecordPO po = new BargainRecordPO();
		po.setGoodsRecordId(id);
		po.setBargainPrice(record.getBargainPrice());
		po.setBuy(record.isBuy());
		po.setUserId(record.getUserId());
		return po;
	}
}

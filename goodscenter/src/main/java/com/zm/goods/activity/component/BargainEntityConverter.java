package com.zm.goods.activity.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zm.goods.activity.model.ActiveGoods;
import com.zm.goods.activity.model.bargain.bo.BargainRecord;
import com.zm.goods.activity.model.bargain.bo.BargainRule;
import com.zm.goods.activity.model.bargain.bo.UserBargainEntity;
import com.zm.goods.activity.model.bargain.dto.BargainInfoDTO;
import com.zm.goods.activity.model.bargain.po.BargainRecordPO;
import com.zm.goods.activity.model.bargain.po.BargainRulePO;
import com.zm.goods.activity.model.bargain.po.UserBargainPO;
import com.zm.goods.activity.model.bargain.vo.BargainGoods;
import com.zm.goods.activity.model.bargain.vo.MyBargain;
import com.zm.goods.activity.model.bargain.vo.MyBargainRecord;
import com.zm.goods.pojo.vo.GoodsVO;
import com.zm.goods.utils.CalculationUtils;
import com.zm.goods.utils.JSONUtil;

/**
 * @fun 砍价活动实体类互相转换工具类
 * @author user
 *
 */
public class BargainEntityConverter {

	public final List<MyBargain> userBargainPO2MyBargain(List<UserBargainPO> list, int userId) {
		List<MyBargain> result = new ArrayList<MyBargain>();
		if (list == null || list.size() == 0) {
			return result;
		}
		for (UserBargainPO po : list) {
			result.add(setMyBargain(po, userId));
		}
		return result;
	}

	public final MyBargain userBargainPO2MyBargain(UserBargainPO po, int userId) {

		return setMyBargain(po, userId);
	}

	private MyBargain setMyBargain(UserBargainPO po, int userId) {
		MyBargain my = new MyBargain();
		List<MyBargainRecord> temp;
		my.setId(po.getId());
		my.setGoodsPrice(po.getInitPrice());// 商品原价
		my.setLowPrice(po.getFloorPrice());// 商品底价
		my.setDuration(po.getDuration());// 砍价持续时间
		my.setSpecsTpId(po.getSpecsTpId());// 商品specsTpId，用于中间件获取商品属性
		my.setUserId(po.getUserId());// userId，用于中间件获取用户头像
		my.setStart(po.isStart());
		my.setCreateTime(po.getCreateTime());
		if (po.getBargainList() != null) {
			temp = new ArrayList<MyBargainRecord>();
			MyBargainRecord record = null;
			double bargainPrice = 0.0;
			for (BargainRecordPO recordPO : po.getBargainList()) {
				record = new MyBargainRecord();
				record.setBargainPrice(recordPO.getBargainPrice());// 每次砍价金额记录
				record.setId(recordPO.getId());
				record.setUserId(recordPO.getUserId());// 用户ID，用于中间件获取用户信息
				record.setBuy(recordPO.isBuy());
				record.setUserImg(recordPO.getUserImg());
				record.setUserName(recordPO.getUserName());
				if (recordPO.getUserId() == userId) {
					my.setBuy(recordPO.isBuy());
				}
				temp.add(record);
				bargainPrice = CalculationUtils.add(bargainPrice, recordPO.getBargainPrice());// 计算已经砍价的金额
			}
			my.setBargainList(temp);// 设置砍价记录
			my.setBargainPrice(bargainPrice);// 设置已经砍价金额
		}
		return my;
	}

	public final List<BargainGoods> BargainRulePO2BargainGoods(List<BargainRulePO> list) {
		List<BargainGoods> result = new ArrayList<BargainGoods>();
		if (list == null) {
			return result;
		}
		list.forEach(po -> {
			BargainGoods goods = new BargainGoods();
			goods.setSpecsTpId(po.getSpecsTpId());
			goods.setGoodsPrice(po.getInitPrice());
			goods.setBargainCount(po.getCount());
			goods.setGoodsRoleId(po.getId());
			goods.setLowPrice(po.getFloorPrice());
			result.add(goods);
		});
		return result;
	}

	public final BargainRule BargainRulePO2BargainRule(BargainRulePO po) {
		BargainRule rule = new BargainRule();
		rule.setDuration(po.getDuration());
		rule.setFirstMaxRatio(po.getFirstMaxRatio());
		rule.setFirstMinRatio(po.getFirstMinRatio());
		rule.setFloorPrice(po.getFloorPrice());
		rule.setInitPrice(po.getInitPrice());
		rule.setSpecsTpId(po.getSpecsTpId());
		rule.setLessMinPrice(po.getLessMinPrice());
		rule.setMaxCount(po.getMaxCount());
		rule.setMaxRatio(po.getMaxRatio());
		rule.setMinRatio(po.getMinRatio());
		rule.setType(po.getType());
		return rule;
	}

	public final UserBargainPO BargainRulePO2UserBargainPO(Integer userId, BargainRulePO po) {
		UserBargainPO userBargainPO = new UserBargainPO();
		userBargainPO.setFloorPrice(po.getFloorPrice());
		userBargainPO.setGoodsRoleId(po.getId());
		userBargainPO.setInitPrice(po.getInitPrice());
		userBargainPO.setUserId(userId);
		userBargainPO.setStart(true);
		return userBargainPO;
	}

	public final BargainRecordPO BargainRecord2BargainRecordPO(int id, BargainRecord record) {
		BargainRecordPO po = new BargainRecordPO();
		po.setGoodsRecordId(id);
		po.setBargainPrice(record.getBargainPrice());
		po.setBuy(record.isBuy());
		po.setUserId(record.getUserId());
		po.setUserName(record.getUserName());
		po.setUserImg(record.getUserImg());
		return po;
	}

	public final UserBargainEntity UserBargainPO2UserBargainEntity(BargainInfoDTO dto, UserBargainPO po) {
		UserBargainEntity entity = new UserBargainEntity();
		entity.setCreateTime(po.getCreateTime());
		entity.setFloorPrice(po.getFloorPrice());
		entity.setId(po.getId());
		entity.setInitPrice(po.getInitPrice());
		entity.setSpecsTpId(po.getSpecsTpId());
		entity.setStart(po.isStart());
		entity.setUserId(po.getUserId());
		entity.setUserName(dto.getUserName());
		entity.setUserImg(dto.getUserImg());
		if (po.getBargainList() != null) {
			List<BargainRecord> recordList = new ArrayList<BargainRecord>();
			po.getBargainList().forEach(recordPO -> {
				BargainRecord record = new BargainRecord();
				record.setBargainPrice(recordPO.getBargainPrice());
				record.setBuy(recordPO.isBuy());
				record.setCreateTime(recordPO.getCreateTime());
				record.setUserId(recordPO.getUserId());
				recordList.add(record);
			});
			entity.setRecordList(recordList);
		}
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	public final ActiveGoods GoodsVO2ActiveGoods(GoodsVO vo){
		ActiveGoods goods = new ActiveGoods();
		goods.setDescription(vo.getDescription());
		Map<String,String> map = JSONUtil.parse(vo.getSpecsList().get(0).getInfo(), Map.class);
		StringBuilder goodsNameSuffix = new StringBuilder();
		for(Map.Entry<String, String> entry : map.entrySet()){
			goodsNameSuffix.append(entry.getValue() + " ");
		}
		goods.setGoodsName(vo.getGoodsName() + goodsNameSuffix.toString());
		goods.setOrigin(vo.getOrigin());
		goods.setPath(vo.getGoodsFileList().get(0).getPath());
		goods.setSpecsTpId(vo.getSpecsList().get(0).getSpecsTpId());
		goods.setStock(vo.getSpecsList().get(0).getStock());
		return goods;
	}
}

package com.zm.goods.activity.component;

import java.util.ArrayList;
import java.util.List;

import com.zm.goods.activity.model.bargain.po.BargainRecordPO;
import com.zm.goods.activity.model.bargain.po.UserBargainPO;
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
		MyBargain my = null;
		List<MyBargain> result = new ArrayList<MyBargain>();
		List<MyBargainRecord> temp = null;
		for(UserBargainPO po : list){
			my = new MyBargain();
			my.setId(po.getId());
			my.setGoodsPrice(po.getInitPrice());//商品原价
			my.setLowPrice(po.getFloorPrice());//商品底价
			my.setDuration(po.getDuration());//砍价持续时间
			my.setItemId(po.getItemId());//商品itemId，用于中间件获取商品属性
			if(po.getBargainList() != null){
				temp = new ArrayList<MyBargainRecord>();
				MyBargainRecord record = null;
				double bargainPrice = 0.0;
				for(BargainRecordPO recordPO : po.getBargainList()){
					record = new MyBargainRecord();
					record.setBargainPrice(recordPO.getBargainPrice());//每次砍价金额记录
					record.setId(recordPO.getId());
					record.setUserId(recordPO.getUserId());//用户ID，用于中间件获取用户信息
					temp.add(record);
					bargainPrice = CalculationUtils.add(bargainPrice, recordPO.getBargainPrice());//计算已经砍价的金额
				}
				my.setBargainList(temp);//设置砍价记录
				my.setBargainPrice(bargainPrice);//设置已经砍价金额
			}
			result.add(my);
		}
		return result;
	}
	
	public final MyBargain userBargainPO2MyBargain(UserBargainPO po){
		return null;
	}
}

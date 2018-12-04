package com.zm.goods.activity.front.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.goods.activity.component.BargainActiveComponent;
import com.zm.goods.activity.component.BargainEntityConverter;
import com.zm.goods.activity.front.dao.BargainMapper;
import com.zm.goods.activity.front.service.BargainActivityService;
import com.zm.goods.activity.model.ActiveGoods;
import com.zm.goods.activity.model.bargain.BargainRecord;
import com.zm.goods.activity.model.bargain.BargainRule;
import com.zm.goods.activity.model.bargain.bo.BargainCountBO;
import com.zm.goods.activity.model.bargain.po.BargainRecordPO;
import com.zm.goods.activity.model.bargain.po.BargainRulePO;
import com.zm.goods.activity.model.bargain.po.UserBargainPO;
import com.zm.goods.activity.model.bargain.vo.BargainGoods;
import com.zm.goods.activity.model.bargain.vo.MyBargain;
import com.zm.goods.common.Pagination;
import com.zm.goods.constants.Constants;
import com.zm.goods.exception.ActiviteyException;
import com.zm.goods.feignclient.UserFeignClient;
import com.zm.goods.feignclient.model.UserBO;
import com.zm.goods.log.LogUtil;
import com.zm.goods.utils.DateUtil;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class BargainActivityServiceImpl implements BargainActivityService {

	@Resource
	BargainMapper bargainMapper;

	@Resource
	UserFeignClient userFeignClient;

	@Resource
	RedisTemplate<String, String> template;

	@Override
	public List<MyBargain> listMyBargain(Integer userId) {
		// 获取普通的砍价活动
		List<UserBargainPO> normalList = bargainMapper.listBargainNormalByUserId(userId);
		// 获取接龙形式的砍价活动
		List<UserBargainPO> chainList = bargainMapper.listBargainChainsByUserId(userId);
		// 合并List
		List<UserBargainPO> bargainList = Stream.of(normalList, chainList).flatMap(Collection::stream)
				.collect(Collectors.toList());
		// 创建砍价活动转换器
		BargainEntityConverter convert = new BargainEntityConverter();
		// 获取前端展示需要的对象
		List<MyBargain> myBargainList = convert.userBargainPO2MyBargain(bargainList);
		// 完善砍价数据
		renderBargain(myBargainList, false);
		return myBargainList;
	}

	@Override
	public MyBargain getMyBargainDetail(Integer userId, int id) throws ActiviteyException {
		int type = bargainMapper.getRuleTypeByUserBargainId(id);// 获取该砍价记录的模式
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userId);
		param.put("id", id);
		param.put("type", type);
		UserBargainPO userBargainPO = bargainMapper.getBargainDetailByParam(param);
		if (userBargainPO == null) {
			throw new ActiviteyException("没有获取对应的详情，是不是数据迷路了", 5);
		}
		// 创建砍价活动转换器
		BargainEntityConverter convert = new BargainEntityConverter();
		// 获取前端展示需要的对象
		MyBargain myBargain = convert.userBargainPO2MyBargain(userBargainPO);
		List<MyBargain> myBargainList = new ArrayList<MyBargain>();
		myBargainList.add(myBargain);
		// 完善砍价数据
		renderBargain(myBargainList, true);
		return myBargain;
	}

	/**
	 * @fun 根据是否详情页，来完善砍价信息 true ---- 完善商品信息和用户信息 false ---- 完善商品信息
	 * @param myBargainList
	 * @param isDetail
	 */
	private void renderBargain(List<MyBargain> myBargainList, boolean isDetail) {
		// 获取商品信息
		List<String> itemIdList = myBargainList.stream().map(my -> my.getItemId()).collect(Collectors.toList());
		List<ActiveGoods> goodsList = bargainMapper.listActiceGoods(itemIdList);
		// 完善数据
		Map<String, ActiveGoods> map = goodsList.stream()
				.collect(Collectors.toMap(ActiveGoods::getItemId, ActiveGoods -> ActiveGoods));
		myBargainList.forEach(myBargain -> {
			ActiveGoods goods = map.get(myBargain.getItemId());
			myBargain.setGoodsImg(goods.getPath());
			myBargain.setGoodsName(goods.getGoodsName());
			myBargain.setStock(goods.getStock());
			myBargain.setOriginCountry(goods.getOrigin());
			myBargain.setDescription(goods.getDescription());
			if (!isDetail) {
				myBargain.setBargainList(null);// 设为null不传给前端
				myBargain.setUserImg(null);// 设为null不传给前端
			}
		});
		if (isDetail) {// 详情页需要每个砍价用户的头像和名称
			List<Integer> userIdList = myBargainList.get(0).getBargainList().stream().map(record -> record.getUserId())
					.collect(Collectors.toList());
			List<UserBO> userList = userFeignClient.listUserByUserId(Constants.FIRST_VERSION, userIdList);
			if (userList != null) {
				Map<Integer, UserBO> userMap = userList.stream()
						.collect(Collectors.toMap(UserBO::getUserId, UserBO -> UserBO));
				myBargainList.forEach(myBargain -> {
					UserBO bo = userMap.get(myBargain.getUserId());
					myBargain.setUserImg(bo.getHeadImg());
					myBargain.getBargainList().forEach(record -> {
						UserBO userBo = userMap.get(myBargain.getUserId());
						record.setUserImg(userBo.getHeadImg());
						record.setUserName(userBo.getUserName());
					});
				});
			}
		}
	}

	@Override
	public Page<BargainGoods> listBargainGoods(Pagination pagination) {
		PageHelper.startPage(pagination.getCurrentPage(), pagination.getNumPerPage(), true);
		Page<BargainRulePO> page = bargainMapper.listBargainGoodsForPage();
		// 获取开团数量
		List<Integer> idList = page.stream().map(rule -> rule.getId()).collect(Collectors.toList());
		List<BargainCountBO> bargainCountList = bargainMapper.listBargainCount(idList);
		// 补全page的开团数量
		Map<Integer, Integer> countMap = bargainCountList.stream()
				.collect(Collectors.toMap(BargainCountBO::getId, BargainCountBO::getCount));
		page.forEach(po -> {
			Integer count = countMap.get(po.getId()) == null ? 0 : countMap.get(po.getId());
			po.setCount(count);
		});
		// 创建砍价活动转换器
		BargainEntityConverter convert = new BargainEntityConverter();
		// 获取前端展示需要的对象
		List<BargainGoods> bargainGoodsList = convert.BargainRulePO2BargainGoods(page);
		// 获取商品信息
		List<String> itemIdList = bargainGoodsList.stream().map(goods -> goods.getItemId())
				.collect(Collectors.toList());
		List<ActiveGoods> goodsList = bargainMapper.listActiceGoods(itemIdList);
		// 完善商品数据
		Map<String, ActiveGoods> map = goodsList.stream()
				.collect(Collectors.toMap(ActiveGoods::getItemId, ActiveGoods -> ActiveGoods));
		bargainGoodsList.forEach(goods -> {
			ActiveGoods activeGoods = map.get(goods.getItemId());
			goods.setGoodsImg(activeGoods.getPath());
			goods.setGoodsName(activeGoods.getGoodsName());
			goods.setStock(activeGoods.getStock());
			goods.setOriginCountry(activeGoods.getOrigin());
			goods.setDescription(activeGoods.getDescription());
		});
		Page<BargainGoods> resultPage = new Page<>(page.getPageNum(), page.getPageSize(), (int) page.getTotal());
		resultPage.setPages(page.getPages());
		resultPage.addAll(bargainGoodsList);
		return resultPage;
	}

	@Override
	public boolean userBargainOver(Integer userId, Integer id) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userId);
		param.put("id", id);
		bargainMapper.updateUserBargainOver(param);
		return true;
	}

	@Override
	public Integer startBargain(Integer userId, Integer goodsRoleId) throws ActiviteyException {
		BargainRulePO rulePO = bargainMapper.getBargainRuleById(goodsRoleId);
		if(rulePO == null){
			throw new ActiviteyException("没有该类的砍价活动",6);
		}
		// 创建砍价活动转换器
		BargainEntityConverter convert = new BargainEntityConverter();
		// 获取规则类
		BargainRule rule = convert.BargainRulePO2BargainRule(rulePO);
		// 生成用户砍价记录类
		UserBargainPO userBargainPO = convert.BargainRulePO2UserBargainPO(userId, rulePO);
		bargainMapper.saveUserBargain(userBargainPO);
		userBargainPO.setCreateTime(DateUtil.getNowTimeStr("yyyy-MM-dd HH:mm:ss"));
		LogUtil.writeLog("用户开团记录ID为=====" + userBargainPO.getId());
		// 创建砍价核心逻辑组件
		BargainActiveComponent bargainComponent = new BargainActiveComponent(rule, userBargainPO.getId() + "",
				template);
		// 实现砍价
		doBargain(userId, convert, userBargainPO, bargainComponent);
		return userBargainPO.getId();
	}

	private BargainRecord doBargain(Integer userId, BargainEntityConverter convert, UserBargainPO userBargainPO,
			BargainActiveComponent bargainComponent) throws ActiviteyException {
		
		//获取砍价发起人的userId，将帮砍人的userId赋值进去，进行判断
		int bargainOwnUserId = userBargainPO.getUserId();
		userBargainPO.setUserId(userId);
		try {
			// 砍价
			BargainRecord record = bargainComponent.doProcess(userBargainPO);
			// 保存砍价记录
			BargainRecordPO po = convert.BargainRecord2BargainRecordPO(userBargainPO.getId(), record);
			bargainMapper.saveBargainRecord(po);
			return record;
		} catch (ActiviteyException e) {
			if (e.getErrorCode() == 2) {// 时间超时前端没有掉接口，需要主动将该记录状态改变
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("userId", bargainOwnUserId);
				param.put("id", userBargainPO.getId());
				bargainMapper.updateUserBargainOver(param);
			}
			throw e;
		}
	}

	@Override
	public double bargain(Integer userId, Integer id) throws ActiviteyException {
		boolean effective = userFeignClient.verifyUserId(Constants.FIRST_VERSION, userId);
		if(!effective){
			throw new ActiviteyException("你这个沙雕，想作弊",7);
		}
		UserBargainPO userBargainPO = bargainMapper.getUserBargainById(id);
		if(userBargainPO == null){
			throw new ActiviteyException("没有该类的砍价活动",6);
		}
		BargainRulePO rulePO = bargainMapper.getBargainRuleById(userBargainPO.getGoodsRoleId());
		// 创建砍价活动转换器
		BargainEntityConverter convert = new BargainEntityConverter();
		// 获取规则类
		BargainRule rule = convert.BargainRulePO2BargainRule(rulePO);
		// 创建砍价核心逻辑组件
		BargainActiveComponent bargainComponent = new BargainActiveComponent(rule, userBargainPO.getId() + "",
				template);

		// 实现砍价
		BargainRecord record = doBargain(userId, convert, userBargainPO, bargainComponent);
		return record.getBargainPrice();
	}

}

package com.zm.goods.activity.front.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
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
import com.zm.goods.activity.model.bargain.bo.BargainCountBO;
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
import com.zm.goods.bussiness.dao.GoodsMapper;
import com.zm.goods.common.Pagination;
import com.zm.goods.constants.Constants;
import com.zm.goods.enummodel.ErrorCodeEnum;
import com.zm.goods.exception.ActiviteyException;
import com.zm.goods.feignclient.UserFeignClient;
import com.zm.goods.log.LogUtil;
import com.zm.goods.pojo.GoodsSpecs;
import com.zm.goods.pojo.OrderBussinessModel;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.Tax;
import com.zm.goods.utils.CalculationUtils;
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

	@Resource
	GoodsMapper goodsMapper;

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
		if (myBargainList != null && myBargainList.size() > 0) {
			// 完善砍价数据
			renderBargain(myBargainList, false);
		}
		return myBargainList.stream().sorted(Comparator.comparing(MyBargain::isStart)).collect(Collectors.toList());
	}

	@Override
	public MyBargain getMyBargainDetail(Integer userId, int id) throws ActiviteyException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		param.put("accurate", "no");//如果是订单作用的，需要根据userId等信息精确查询,分享砍价的不需要
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
				myBargain.setUserName(null);// 设为null不传给前端
			}
		});
		if (isDetail) {
			MyBargainRecord record = myBargainList.get(0).getBargainList().stream()
					.filter(temp -> temp.getUserId() == myBargainList.get(0).getUserId()).findAny().get();
			myBargainList.get(0).setUserImg(record.getUserImg());
			myBargainList.get(0).setUserName(record.getUserName());

		}
	}

	@Override
	public Page<BargainGoods> listBargainGoods(Pagination pagination, Integer userId) {
		List<Integer> goodsRoleIdList = null;
		if (userId != null) {
			goodsRoleIdList = bargainMapper.listGoodsRoleIdsByUserId(userId);
		}
		PageHelper.startPage(pagination.getCurrentPage(), pagination.getNumPerPage(), true);
		Page<BargainRulePO> page = bargainMapper.listBargainGoodsForPage(goodsRoleIdList);
		List<BargainGoods> bargainGoodsList = new ArrayList<BargainGoods>();
		if (page.size() > 0) {
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
			bargainGoodsList = convert.BargainRulePO2BargainGoods(page);
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
		}
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
	public Integer startBargain(BargainInfoDTO dto) throws ActiviteyException {
		Integer goodsRoleId = dto.getId();
		BargainRulePO rulePO = bargainMapper.getBargainRuleById(goodsRoleId);
		if (rulePO == null) {
			throw new ActiviteyException("没有该类的砍价活动", 6);
		}
		// 创建砍价活动转换器
		BargainEntityConverter convert = new BargainEntityConverter();
		// 获取规则类
		BargainRule rule = convert.BargainRulePO2BargainRule(rulePO);
		// 生成用户砍价记录类
		UserBargainPO userBargainPO = convert.BargainRulePO2UserBargainPO(dto.getUserId(), rulePO);
		try {
			bargainMapper.saveUserBargain(userBargainPO);
		} catch (Exception e) {
			if (e.getMessage() != null && e.getMessage().contains("Duplicate entry")) {
				throw new ActiviteyException("你已经参加过该商品的砍价", 8);
			} else {
				throw new ActiviteyException("创建砍价信息失败，去砍程序员小哥哥吧", 9);
			}
		}
		userBargainPO.setCreateTime(DateUtil.getNowTimeStr("yyyy-MM-dd HH:mm:ss"));
		LogUtil.writeLog("用户开团记录ID为=====" + userBargainPO.getId());
		UserBargainEntity entity = convert.UserBargainPO2UserBargainEntity(dto, userBargainPO);
		// 创建砍价核心逻辑组件
		BargainActiveComponent bargainComponent = new BargainActiveComponent(rule, userBargainPO.getId() + "",
				template);
		// 实现砍价
		doBargain(dto.getUserId(), convert, entity, bargainComponent);
		return userBargainPO.getId();
	}

	private BargainRecord doBargain(Integer userId, BargainEntityConverter convert, UserBargainEntity entity,
			BargainActiveComponent bargainComponent) throws ActiviteyException {

		// 获取砍价发起人的userId，将帮砍人的userId赋值进去，进行判断
		int bargainOwnUserId = entity.getUserId();
		entity.setUserId(userId);
		try {
			// 砍价
			BargainRecord record = bargainComponent.doProcess(entity);
			// 保存砍价记录
			BargainRecordPO po = convert.BargainRecord2BargainRecordPO(entity.getId(), record);
			bargainMapper.saveBargainRecord(po);
			return record;
		} catch (ActiviteyException e) {
			if (e.getErrorCode() == 2) {// 时间超时前端没有掉接口，需要主动将该记录状态改变
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("userId", bargainOwnUserId);
				param.put("id", entity.getId());
				bargainMapper.updateUserBargainOver(param);
			}
			throw e;
		}
	}

	@Override
	public double bargain(BargainInfoDTO dto) throws ActiviteyException {
		boolean effective = userFeignClient.verifyUserId(Constants.FIRST_VERSION, dto.getUserId());
		if (!effective) {
			throw new ActiviteyException("你这个沙雕，想作弊", 7);
		}
		UserBargainPO userBargainPO = bargainMapper.getUserBargainById(dto.getId());
		if (userBargainPO == null) {
			throw new ActiviteyException("没有该类的砍价活动", 6);
		}
		BargainRulePO rulePO = bargainMapper.getBargainRuleById(userBargainPO.getGoodsRoleId());
		// 创建砍价活动转换器
		BargainEntityConverter convert = new BargainEntityConverter();
		// 获取规则类
		BargainRule rule = convert.BargainRulePO2BargainRule(rulePO);
		// 生成用户砍价类
		UserBargainEntity entity = convert.UserBargainPO2UserBargainEntity(dto, userBargainPO);
		// 创建砍价核心逻辑组件
		BargainActiveComponent bargainComponent = new BargainActiveComponent(rule, userBargainPO.getId() + "",
				template);

		// 实现砍价
		BargainRecord record = doBargain(dto.getUserId(), convert, entity, bargainComponent);
		return record.getBargainPrice();
	}

	@Override
	public ResultModel getBargainGoodsInfo(List<OrderBussinessModel> list, Integer userId, Integer id) {
		Map<String, Tax> tempTaxMap = new HashMap<String, Tax>();// 税费temp
		Map<Tax, Double> taxMap = new HashMap<Tax, Double>();// 税费map
		ResultModel result = new ResultModel(true, "");
		Map<String, Object> map = new HashMap<String, Object>();// 返回结果的map
		List<String> itemIds = list.stream().map(OrderBussinessModel::getItemId).collect(Collectors.toList());
		// 获取所有税率信息
		List<Tax> taxList = goodsMapper.getTax(itemIds);
		for (Tax tax : taxList) {
			tempTaxMap.put(tax.getItemId(), tax);
		}
		Map<String, Object> param = new HashMap<String, Object>();// 参数map
		param.put("list", itemIds);
		List<GoodsSpecs> specsList = goodsMapper.getGoodsSpecsForOrder(param);
		if (specsList == null || specsList.size() == 0) {
			return new ResultModel(false, ErrorCodeEnum.GOODS_DOWNSHELVES.getErrorCode(),
					"所有商品" + ErrorCodeEnum.GOODS_DOWNSHELVES.getErrorMsg());
		}
		int weight = 0;
		for (GoodsSpecs specs : specsList) {
			weight += specs.getWeight() * list.get(0).getQuantity();
		}
		int type = bargainMapper.getRuleTypeByUserBargainId(id);// 获取该砍价记录的模式
		param.put("userId", userId);
		param.put("id", id);
		param.put("type", type);
		param.put("accurate", "yes");//如果是订单作用的，需要根据userId等信息精确查询
		UserBargainPO userBargainPO = bargainMapper.getBargainDetailByParam(param);
		if (userBargainPO == null) {
			return new ResultModel(false, "没有该活动订单");
		}
		boolean buy = userBargainPO.getBargainList().stream().filter(record -> record.getUserId() == userId).findAny()
				.get().isBuy();
		if (buy) {
			return new ResultModel(false, "你已经买过该商品");
		}
		taxMap.put(tempTaxMap.get(list.get(0).getItemId()), userBargainPO.getInitPrice());// 税率对应的总金额
		double alreadyBargainPrice = userBargainPO.getBargainList().stream()
				.mapToDouble(BargainRecordPO::getBargainPrice).sum();
		double totalAmount = CalculationUtils.sub(userBargainPO.getInitPrice(), alreadyBargainPrice);
		totalAmount = CalculationUtils.round(2, totalAmount);
		map.put("tax", taxMap);
		map.put("originalPrice", userBargainPO.getInitPrice());
		map.put("weight", weight);
		map.put("totalAmount", totalAmount);
		result.setSuccess(true);
		result.setObj(map);
		return result;
	}

	@Override
	public boolean updateBargainGoodsBuy(Integer userId, Integer id) {
		Map<String, Object> param = new HashMap<String, Object>();
		int type = bargainMapper.getRuleTypeByUserBargainId(id);// 获取该砍价记录的模式
		param.put("userId", userId);
		param.put("id", id);
		param.put("type", type);
		param.put("accurate", "yes");//如果是订单作用的，需要根据userId等信息精确查询
		UserBargainPO userBargainPO = bargainMapper.getBargainDetailByParam(param);
		if (userBargainPO == null) {
			return false;
		}
		int recordId = userBargainPO.getBargainList().stream().filter(record -> record.getUserId() == userId).findAny()
				.get().getId();
		bargainMapper.updateBargainGoodsBuy(recordId);
		return true;
	}

}

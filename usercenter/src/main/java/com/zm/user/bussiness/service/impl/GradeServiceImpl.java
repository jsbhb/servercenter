/**  
 * Project Name:usercenter  
 * File Name:GradeServiceImpl.java  
 * Package Name:com.zm.user.bussiness.service.impl  
 * Date:Oct 29, 20177:58:30 PM  
 *  
 */
package com.zm.user.bussiness.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.user.bussiness.component.UserComponent;
import com.zm.user.bussiness.dao.GradeMapper;
import com.zm.user.bussiness.service.GradeService;
import com.zm.user.common.Pagination;
import com.zm.user.common.ResultModel;
import com.zm.user.constants.Constants;
import com.zm.user.enummodel.BackManagerErrorEnum;
import com.zm.user.enummodel.PublishType;
import com.zm.user.feignclient.OrderFeignClient;
import com.zm.user.log.LogUtil;
import com.zm.user.pojo.FuzzySearchGrade;
import com.zm.user.pojo.Grade;
import com.zm.user.pojo.ShopEntity;
import com.zm.user.pojo.bo.ButtjointUserBO;
import com.zm.user.pojo.bo.CreateAreaCenterSEO;
import com.zm.user.pojo.bo.GradeBO;
import com.zm.user.pojo.bo.RebateFormulaBO;
import com.zm.user.pojo.dto.GradeTypeDTO;
import com.zm.user.pojo.po.GradeTypePO;
import com.zm.user.pojo.po.RebateFormula;
import com.zm.user.seo.publish.PublishComponent;
import com.zm.user.utils.ConvertUtil;
import com.zm.user.utils.JSONUtil;
import com.zm.user.utils.TreePackUtil;

/**
 * ClassName: GradeServiceImpl <br/>
 * Function: 分级管理实现类. <br/>
 * date: Oct 29, 2017 7:58:30 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class GradeServiceImpl implements GradeService {

	@Resource
	GradeMapper<Grade> gradeMapper;

	@Resource
	UserComponent userComponent;

	@Resource
	RedisTemplate<String, String> template;

	@Resource
	OrderFeignClient orderFeignClient;

	@Override
	public Page<Grade> queryForPagination(Grade grade) {
		PageHelper.startPage(grade.getCurrentPage(), grade.getNumPerPage(), true);
		return gradeMapper.selectForPage(grade);
	}

	@Override
	public Grade queryById(Integer id) {
		return gradeMapper.selectById(id);
	}

	@Override
	public void update(Grade entity) {
		Grade grade = gradeMapper.selectById(entity.getId());
		gradeMapper.update(entity);
		gradeMapper.updateGradeData(entity);
		if (Constants.BUTT_JOINT_USER.equals(entity.getType())) {
			Set<String> set = template.opsForSet().members(Constants.BUTT_JOINT_USER_PREFIX);
			ButtjointUserBO bo = null;
			for (String str : set) {
				bo = JSONUtil.parse(str, ButtjointUserBO.class);
				if (bo.getAppKey().equals(entity.getAppKey()) && bo.getAppSecret().equals(entity.getAppSecret())) {
					template.opsForSet().remove(Constants.BUTT_JOINT_USER_PREFIX, JSONUtil.toJson(bo));// 删除原来的
					template.opsForSet().add(Constants.BUTT_JOINT_USER_PREFIX,
							JSONUtil.toJson(ConvertUtil.converToButtjoinUserBO(entity)));
				}
			}
		}
		// 更新redis内gradeBO信息
		entity.setParentId(grade.getParentId());
		GradeBO gradeBO = ConvertUtil.converToGradeBO(entity);
		template.opsForHash().put(Constants.GRADEBO_INFO, grade.getId() + "", JSONUtil.toJson(gradeBO));
		orderFeignClient.noticeToAddGrade(Constants.FIRST_VERSION, gradeBO);// ordercenter不能用redis需要实时通知更新统计信息
	}

	@Override
	public ShopEntity queryByGradeId(Integer gradeId) {
		return gradeMapper.selectByGradeId(gradeId);
	}

	@Override
	public void updateShop(ShopEntity entity) {
		ShopEntity shopEntity = gradeMapper.selectByGradeId(entity.getGradeId());
		if (shopEntity != null) {
			gradeMapper.updateGradeConfig(entity);
		} else {
			gradeMapper.insertGradeConfig(entity);
		}
	}

	@Override
	public ResultModel fuzzySearch(FuzzySearchGrade entity) {
		List<FuzzySearchGrade> list = gradeMapper.fuzzyListGrade(entity);
		// if(list != null && list.size() > 0){
		// Grade grade = null;
		// for(FuzzySearchGrade temp : list){
		// grade = userComponent.getUrl(temp.getId());
		// temp.setUrl(grade.getRedirectUrl());
		// temp.setMobileUrl(grade.getMobileUrl());
		// }
		// }
		return new ResultModel(true, list);
	}

	@Override
	public ResultModel saveGradeType(GradeTypePO entity) {

		gradeMapper.saveGradeType(entity);
		return new ResultModel(true, entity.getId());
	}

	@Override
	public ResultModel listGradeType(Integer id) {
		List<GradeTypePO> list = null;
		if (id != null) {
			list = gradeMapper.listParentGradeTypeById(id);
		} else {
			list = gradeMapper.listGradeType();
		}

		return new ResultModel(true, TreePackUtil.packGradeTypeChildren(list, null));
	}

	@Override
	public ResultModel listGradeTypeChildren(Integer id) {
		List<GradeTypePO> poList = new ArrayList<>();
		String parentIdStr = gradeMapper.listGradeTypeChildren(id);

		if (parentIdStr == null) {
			return new ResultModel(true, poList);
		}
		List<Integer> list = packAllGradeTypeChildId(id, parentIdStr);
		if (list.size() == 0) {
			return new ResultModel(true, poList);
		}
		poList = gradeMapper.listGradeTypeByIds(list);

		return new ResultModel(true, TreePackUtil.packGradeTypeChildren(poList, id));
	}

	@Override
	public ResultModel removeGradeType(Integer id) {
		List<GradeTypePO> list = gradeMapper.listGradeTypeChildrenById(id);
		int count = gradeMapper.countGradeByGradeType(id);
		if (count > 0 || (list != null && list.size() > 0)) {
			return new ResultModel(false, BackManagerErrorEnum.DELETE_ERROR.getErrorCode(),
					BackManagerErrorEnum.DELETE_ERROR.getErrorMsg());
		}
		gradeMapper.removeGradeType(id);
		return new ResultModel(true, null);
	}

	@Override
	public ResultModel updateGradeType(GradeTypePO entity) {
		gradeMapper.updateGradeType(entity);
		return new ResultModel(true, null);
	}

	@Override
	public ResultModel getGradeType(Integer id) {
		GradeTypePO gradeTypePO = gradeMapper.getGradeType(id);
		GradeTypeDTO dto = new GradeTypeDTO();
		dto.setId(gradeTypePO.getId());
		dto.setName(gradeTypePO.getName());
		dto.setParentId(gradeTypePO.getParentId());
		return new ResultModel(true, dto);
	}

	@Override
	public ResultModel initAreaCenter(Integer id) {
		Grade grade = gradeMapper.getGradeForInitAreaCenterById(id);
		if (!Constants.AREA_CENTER.equals(grade.getGradeType())) {
			return new ResultModel(false, "", "区域中心才能初始化域名");
		}
		if (grade.getRedirectUrl() == null || grade.getMobileUrl() == null) {
			return new ResultModel(false, "", "请填写域名");
		}
		CreateAreaCenterSEO createAreaCenterSEO = new CreateAreaCenterSEO(grade.getId(), grade.getRedirectUrl(),
				grade.getMobileUrl());
		ResultModel temp = PublishComponent.publish(JSONUtil.toJson(createAreaCenterSEO), PublishType.REGION_CREATE);
		if (!temp.isSuccess()) {
			return new ResultModel(false, "", temp.getErrorMsg());
		}
		gradeMapper.updateGradeInit(id);
		return new ResultModel(true, null);
	}

	@Override
	public void updateWelfareType(Grade entity) {
		Grade grade = gradeMapper.selectById(entity.getId());
		grade.setWelfareType(entity.getWelfareType());
		grade.setWelfareRebate(entity.getWelfareRebate());
		gradeMapper.update(entity);
		// 更新redis内gradeBO信息
		GradeBO gradeBO = ConvertUtil.converToGradeBO(grade);
		template.opsForHash().put(Constants.GRADEBO_INFO, grade.getId() + "", JSONUtil.toJson(gradeBO));
		// orderFeignClient.noticeToAddGrade(Constants.FIRST_VERSION,
		// gradeBO);// ordercenter不能用redis需要实时通知更新统计信息
	}

	@Override
	public ResultModel saveGradeTypeRebateFormula(RebateFormula rebateFormula) {
		Integer id = gradeMapper.getIdByGradeTypeId(rebateFormula.getGradeTypeId());
		if (id != null) {
			return new ResultModel(false, "", "该分级类型公式已经存在");
		}
		gradeMapper.saveGradeTypeRebateFormula(rebateFormula);
		return new ResultModel(true, "");
	}

	@Override
	public ResultModel updateGradeTypeRebateFormula(RebateFormula rebateFormula) {
		gradeMapper.updateGradeTypeRebateFormula(rebateFormula);
		return new ResultModel(true, "");
	}

	@Override
	public ResultModel listGradeTypeRebateFormula(RebateFormula rebateFormula, boolean needPaging) {
		if (needPaging) {
			PageHelper.startPage(rebateFormula.getCurrentPage(), rebateFormula.getNumPerPage(), true);
			Page<RebateFormula> page = gradeMapper.listGradeTypeRebateFormula(rebateFormula);
			return new ResultModel(true, page, new Pagination(page));
		} else {
			List<RebateFormula> page = gradeMapper.listAllGradeTypeRebateFormula();
			List<RebateFormulaBO> list = new ArrayList<RebateFormulaBO>();
			for (RebateFormula temp : page) {
				list.add(ConvertUtil.converToRebateFormulaBO(temp));
			}
			return new ResultModel(true, list);
		}
	}

	@Override
	public ResultModel getGradeTypeRebateFormulaById(Integer id) {
		return new ResultModel(true, gradeMapper.getGradeTypeRebateFormulaById(id));
	}

	@Override
	public ResultModel auditShop(Grade grade) {
		Grade g = gradeMapper.selectById(grade.getId());
		// 由于存在事务管理，读出来的数据不是最新的，所以重新设置
		g.setParentId(grade.getParentId());
		g.setGradeType(grade.getGradeType());
		g.setRemark(grade.getRemark());
		g.setStatus(grade.getStatus());
		if (grade.getStatus() == 2) {// 审核通过生效后
			// 通知订单中心新增grade并做缓存
			grade.setType(1);// 普通客户
			grade.setWelfareType(0);// 非福利客户
			GradeBO gradeBO = ConvertUtil.converToGradeBO(g);
			template.opsForHash().put(Constants.GRADEBO_INFO, grade.getId() + "", JSONUtil.toJson(gradeBO));
			orderFeignClient.noticeToAddGrade(Constants.FIRST_VERSION, gradeBO);
		}
		gradeMapper.auditShop(grade);
		return new ResultModel(true, g);
	}

	@Override
	public ResultModel listGradeTypeChildrenByParentGradeId(Integer gradeId) {
		List<GradeTypePO> poList = new ArrayList<>();
		Integer tmp = gradeMapper.getGradeTypeByGradeId(gradeId);
		tmp = tmp == null ? 1 : tmp;// 中国供销海外购没有上级
		String parentIdStr = gradeMapper.listGradeTypeChildren(tmp);
		if (parentIdStr == null) {
			return new ResultModel(true, poList);
		}
		List<Integer> list = packAllGradeTypeChildId(tmp, parentIdStr);
		if (list.size() == 0) {
			return new ResultModel(true, poList);
		}
		poList = gradeMapper.listGradeTypeByIds(list);
		return new ResultModel(true, poList);
	}

	private List<Integer> packAllGradeTypeChildId(Integer tmp, String parentIdStr) {
		String[] parentIdArr = parentIdStr.split(",");
		List<Integer> list = new ArrayList<Integer>();
		for (String str : parentIdArr) {
			if (!"$".equals(str) && !str.equals(tmp.toString())) {
				try {
					list.add(Integer.valueOf(str));
				} catch (NumberFormatException e) {
					LogUtil.writeErrorLog("【封装下级ID出错】===ID：" + str, e);
				}
			}
		}
		return list;
	}

}

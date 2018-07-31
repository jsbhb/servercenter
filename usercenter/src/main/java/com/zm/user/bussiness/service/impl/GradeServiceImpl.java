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

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.user.bussiness.component.UserComponent;
import com.zm.user.bussiness.dao.GradeMapper;
import com.zm.user.bussiness.service.GradeService;
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
import com.zm.user.pojo.dto.GradeTypeDTO;
import com.zm.user.pojo.po.GradeTypePO;
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
		if (Constants.BUTT_JOINT_USER.equals(entity.getType())
				&& !grade.getRedirectUrl().equals(entity.getRedirectUrl())) {
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
		if(!grade.getGradeType().equals(entity.getGradeType())){
			// 通知订单中心新增grade
			GradeBO gradeBO = new GradeBO();
			gradeBO.setId(grade.getId());
			gradeBO.setParentId(grade.getParentId());
			gradeBO.setGradeType(entity.getGradeType());

			orderFeignClient.noticeToAddGrade(Constants.FIRST_VERSION, gradeBO);
		}
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

		String parentIdStr = gradeMapper.listGradeTypeChildren(id);
		List<Integer> list = new ArrayList<Integer>();
		if (parentIdStr == null) {
			return new ResultModel(true, null);
		}
		String[] parentIdArr = parentIdStr.split(",");
		for (String str : parentIdArr) {
			if (!"$".equals(str) && !id.equals(str)) {
				try {
					list.add(Integer.valueOf(str));
				} catch (NumberFormatException e) {
					LogUtil.writeErrorLog("【封装下级ID出错】===ID：" + str, e);
				}
			}
		}
		List<GradeTypePO> poList = gradeMapper.listGradeTypeByIds(list);

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

}

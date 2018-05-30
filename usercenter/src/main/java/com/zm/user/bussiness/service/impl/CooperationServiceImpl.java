package com.zm.user.bussiness.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.user.bussiness.dao.CooperationMapper;
import com.zm.user.bussiness.service.CooperationService;
import com.zm.user.common.Pagination;
import com.zm.user.common.ResultModel;
import com.zm.user.pojo.po.PartnerPO;
import com.zm.user.pojo.po.ShopKeeperPO;
import com.zm.user.pojo.vo.PartnerVO;
import com.zm.user.pojo.vo.ShopKeeperVO;

@Service
public class CooperationServiceImpl implements CooperationService {

	@Resource
	CooperationMapper cooperationMapper;

	@Override
	public ResultModel listShopkeeper(Pagination pagination) {
		PageHelper.startPage(pagination.getCurrentPage(), pagination.getNumPerPage(), true);
		Page<ShopKeeperPO> page = cooperationMapper.listShopKeeper();
		ShopKeeperVO vo = null;
		List<ShopKeeperVO> list = new ArrayList<ShopKeeperVO>();
		for (ShopKeeperPO po : page) {
			vo = new ShopKeeperVO(po);
			list.add(vo);
		}
		return new ResultModel(true, list, new Pagination(page));
	}

	@Override
	public ResultModel listPartner(Pagination pagination) {
		PageHelper.startPage(pagination.getCurrentPage(), pagination.getNumPerPage(), true);
		Page<PartnerPO> page = cooperationMapper.listPartner();
		PartnerVO vo = null;
		List<PartnerVO> list = new ArrayList<PartnerVO>();
		for (PartnerPO po : page) {
			vo = new PartnerVO(po);
			list.add(vo);
		}
		return new ResultModel(true, list, new Pagination(page));
	}

	@Override
	public ResultModel savePartner(PartnerPO po) {
		try {
			cooperationMapper.savePartner(po);
			return new ResultModel(true,null);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultModel(false,"该合伙人名称已经存在");
		}
		
	}

	@Override
	public ResultModel saveShopkeeper(ShopKeeperPO po) {
		cooperationMapper.saveShopKeeper(po);
		return new ResultModel(true,null);
	}

	@Override
	public ResultModel updateShopkeeper(ShopKeeperPO po) {
		cooperationMapper.updateShopKeeper(po);
		return new ResultModel(true,null);
	}

	@Override
	public ResultModel updatePartner(PartnerPO po) {
		try {
			cooperationMapper.updatePartner(po);
			return new ResultModel(true,null);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultModel(false,"该合伙人名称已经存在");
		}
	}

	@Override
	public ResultModel deleteShopkeeper(Integer id) {
		cooperationMapper.deleteShopkeeperById(id);
		return new ResultModel(true,null);
	}

	@Override
	public ResultModel deletePartner(Integer id) {
		cooperationMapper.deletePartnerById(id);
		return new ResultModel(true,null);
	}

	@Override
	public ResultModel getShopkeeperBack(Integer id) {
	
		return new ResultModel(true,cooperationMapper.getShopkeeperById(id));
	}

	@Override
	public ResultModel getPartnerBack(Integer id) {
		
		return new ResultModel(true,cooperationMapper.getPartnerById(id));
	}

	@Override
	public ResultModel listShopkeeperBack(ShopKeeperPO po) {
		PageHelper.startPage(po.getCurrentPage(), po.getNumPerPage(), true);
		Page<ShopKeeperPO> page = cooperationMapper.selectShopKeeperForPage(po);
		return new ResultModel(true, page, new Pagination(page));
	}

	@Override
	public ResultModel listPartnerBack(PartnerPO po) {
		PageHelper.startPage(po.getCurrentPage(), po.getNumPerPage(), true);
		Page<PartnerPO> page = cooperationMapper.selectPartnerForPage(po);
		return new ResultModel(true, page, new Pagination(page));
	}

}

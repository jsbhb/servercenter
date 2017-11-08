package com.zm.supplier.bussiness.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.supplier.bussiness.dao.SupplierMapper;
import com.zm.supplier.bussiness.service.SupplierService;
import com.zm.supplier.constants.Constants;
import com.zm.supplier.feignclient.UserFeignClient;
import com.zm.supplier.pojo.Express;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.SupplierEntity;
import com.zm.supplier.pojo.SupplierInterface;
import com.zm.supplier.pojo.UserInfo;
import com.zm.supplier.supplierinf.AbstractSupplierButtJoint;
import com.zm.supplier.util.SpringContextUtil;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

	@Resource
	SupplierMapper supplierMapper;

	@Resource
	UserFeignClient userFeignClient;

	@Resource
	RedisTemplate<String, SupplierInterface> redisTemplate;

	@Override
	public List<Express> listExpressBySupplierId(Integer supplierId) {
		return supplierMapper.listExpressBySupplierId(supplierId);
	}

	@Override
	public Map<String, Object> sendOrder(OrderInfo info) {
		AbstractSupplierButtJoint buttJoint = getTargetInterface(info.getSupplierId());
		UserInfo user = userFeignClient.getUser(Constants.FIRST_VERSION, info.getUserId());
		return null;
	}

	private AbstractSupplierButtJoint getTargetInterface(Integer supplierId) {
		SupplierInterface inf = redisTemplate.opsForValue().get(Constants.SUPPLIER_INTERFACE + supplierId);
		AbstractSupplierButtJoint buttJoint = (AbstractSupplierButtJoint) SpringContextUtil
				.getBean(inf.getTargetObject());
		buttJoint.setAppKey(inf.getAppKey());
		buttJoint.setAppSecret(inf.getAppSecret());
		return buttJoint;
	}

	@Override
	public Page<SupplierEntity> queryByPage(SupplierEntity supplier) {
		PageHelper.startPage(supplier.getCurrentPage(), supplier.getNumPerPage(), true);
		return supplierMapper.selectForPage(supplier);
	}

	@Override
	public void saveSupplier(SupplierEntity entity) {
		supplierMapper.insert(entity);
	}

	@Override
	public SupplierEntity queryById(int id) {
		return supplierMapper.selectById(id);
	}

}

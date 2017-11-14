package com.zm.supplier.bussiness.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.supplier.bussiness.component.WarehouseThreadPool;
import com.zm.supplier.bussiness.dao.SupplierMapper;
import com.zm.supplier.bussiness.service.SupplierService;
import com.zm.supplier.pojo.Express;
import com.zm.supplier.pojo.OrderIdAndSupplierId;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.OrderStatus;
import com.zm.supplier.pojo.SupplierEntity;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

	@Resource
	SupplierMapper supplierMapper;

	@Resource
	WarehouseThreadPool warehouseThreadPool;

	@Override
	public List<Express> listExpressBySupplierId(Integer supplierId) {
		return supplierMapper.listExpressBySupplierId(supplierId);
	}

	@Override
	public void sendOrder(List<OrderInfo> infoList) {
//		for (OrderInfo info : infoList) {
			warehouseThreadPool.sendOrder(null);
//		}
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

	@Override
	public List<OrderStatus> checkOrderStatus(List<OrderIdAndSupplierId> list) {
		Map<Integer, List<OrderIdAndSupplierId>> param = new HashMap<Integer, List<OrderIdAndSupplierId>>();
		List<OrderIdAndSupplierId> tempList = null;
		List<OrderStatus> resultList = new ArrayList<OrderStatus>();
		for(OrderIdAndSupplierId model : list){
			if(param.get(model.getSupplierId()) == null){
				tempList = new ArrayList<OrderIdAndSupplierId>();
				tempList.add(model);
				param.put(model.getSupplierId(), tempList);
			} else {
				param.get(model.getSupplierId()).add(model);
			}
		}
		CountDownLatch  countDownLatch = new CountDownLatch(param.size());
		for(Map.Entry<Integer, List<OrderIdAndSupplierId>> entry : param.entrySet()){
			resultList.addAll(warehouseThreadPool.checkOrderStatus(entry.getValue(), entry.getKey(), countDownLatch));
		}
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return resultList;
	}

}

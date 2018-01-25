package com.zm.supplier.bussiness.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.supplier.bussiness.component.WarehouseThreadPool;
import com.zm.supplier.bussiness.dao.SupplierMapper;
import com.zm.supplier.bussiness.service.SupplierService;
import com.zm.supplier.common.ResultModel;
import com.zm.supplier.pojo.Express;
import com.zm.supplier.pojo.OrderBussinessModel;
import com.zm.supplier.pojo.OrderIdAndSupplierId;
import com.zm.supplier.pojo.OrderInfo;
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
		for (OrderInfo info : infoList) {
			warehouseThreadPool.sendOrder(info);
		}
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

	private static final Integer XINYUN_WAREHOUSE = 3;
	private static final Integer FUBANG_WAREHOUSE = 4;

	@Override
	public ResultModel checkOrderStatus(List<OrderIdAndSupplierId> list) {
		Map<Integer, List<OrderIdAndSupplierId>> param = new HashMap<Integer, List<OrderIdAndSupplierId>>();
		List<OrderIdAndSupplierId> tempList = null;
		for (OrderIdAndSupplierId model : list) {
			if (param.get(model.getSupplierId()) == null) {
				tempList = new ArrayList<OrderIdAndSupplierId>();
				tempList.add(model);
				param.put(model.getSupplierId(), tempList);
			} else {
				param.get(model.getSupplierId()).add(model);
			}
		}
		for (Map.Entry<Integer, List<OrderIdAndSupplierId>> entry : param.entrySet()) {
			if (XINYUN_WAREHOUSE.equals(entry.getKey()) || FUBANG_WAREHOUSE.equals(entry.getKey())) {
				List<OrderIdAndSupplierId> temp = null;
				for (OrderIdAndSupplierId model : entry.getValue()) {
					temp = new ArrayList<OrderIdAndSupplierId>();
					temp.add(model);
					warehouseThreadPool.checkOrderStatus(temp, entry.getKey());
				}
			} else {
				warehouseThreadPool.checkOrderStatus(entry.getValue(), entry.getKey());
			}
		}
		return new ResultModel(true, "正在同步状态");
	}

	@Override
	public ResultModel checkStock(List<OrderBussinessModel> list, Integer supplierId, boolean flag) {
		if (FUBANG_WAREHOUSE.equals(supplierId)) {
			List<OrderBussinessModel> temp = null;
			for (OrderBussinessModel model : list) {
				temp = new ArrayList<OrderBussinessModel>();
				temp.add(model);
				if (flag) {
					warehouseThreadPool.checkStockByAsync(temp, supplierId);//定时器调用的库存方法用线程池，防止feign调用超时
				} else {
					warehouseThreadPool.checkStock(temp, supplierId);
				}
			}
		} else {
			if(flag){
				warehouseThreadPool.checkStockByAsync(list, supplierId);//定时器调用的库存方法用线程池，防止feign调用超时
			}else {
				warehouseThreadPool.checkStock(list, supplierId);
			}
		}
		return new ResultModel(true, "同步库存完成");
	}

	@Override
	public ResultModel getGoods(List<String> list, Integer supplierId, String supplierName) {
		if (list != null) {
			for (String itemCode : list) {
				warehouseThreadPool.getThirdGoods(itemCode, supplierId, supplierName);
			}
		}
		return new ResultModel(true, "正在同步商品");
	}

	@Override
	public List<SupplierEntity> queryAll() {
		return supplierMapper.selectAll();
	}

}

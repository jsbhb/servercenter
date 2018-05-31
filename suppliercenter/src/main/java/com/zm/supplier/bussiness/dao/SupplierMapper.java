package com.zm.supplier.bussiness.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.zm.supplier.pojo.Express;
import com.zm.supplier.pojo.SupplierEntity;
import com.zm.supplier.pojo.SupplierInterface;

public interface SupplierMapper {

	List<Express> listExpressBySupplierId(Integer supplierId);

	List<SupplierInterface> listSupplierInterface();

	/**
	 * selectForPage:分页查询数据. <br/>
	 * 
	 * @author hebin
	 * @param supplier
	 * @return
	 * @since JDK 1.7
	 */
	Page<SupplierEntity> selectForPage(SupplierEntity supplier);

	/**
	 * insert:插入供应商信息. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @since JDK 1.7
	 */
	void insert(SupplierEntity entity);

	/**  
	 * selectById:根据供应商编号检索实体. <br/>  
	 *  
	 * @author hebin  
	 * @param id
	 * @return  
	 * @since JDK 1.7  
	 */
	SupplierEntity selectById(int id);
	
	SupplierInterface getSupplierInterface(@Param("supplierId")Integer supplierId);

	/**  
	 * selectAll:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @return  
	 * @since JDK 1.7  
	 */
	List<SupplierEntity> selectAll();

	/**
	 * update:更新供应商信息. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @since JDK 1.7
	 */
	void update(SupplierEntity entity);
}

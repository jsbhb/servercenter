package com.zm.supplier.bussiness.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.zm.supplier.pojo.Express;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.SupplierEntity;

public interface SupplierService {

	List<Express> listExpressBySupplierId(Integer supplierId);

	Map<String, Object> sendOrder(OrderInfo info);

	/**  
	 * queryByPage:查询分页效果. <br/>  
	 *  
	 * @author hebin  
	 * @param supplier
	 * @return  
	 * @since JDK 1.7  
	 */
	Page<SupplierEntity> queryByPage(SupplierEntity supplier);

	/**  
	 * saveSupplier:插入供应商表. <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void saveSupplier(SupplierEntity entity);

	/**  
	 * queryById:根据编号查询. <br/>  
	 *  
	 * @author hebin  
	 * @param id
	 * @return  
	 * @since JDK 1.7  
	 */
	SupplierEntity queryById(int id);
}

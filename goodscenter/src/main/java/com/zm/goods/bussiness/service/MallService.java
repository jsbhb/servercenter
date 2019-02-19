/**  
 * Project Name:goodscenter  
 * File Name:BrandService.java  
 * Package Name:com.zm.goods.bussiness.service  
 * Date:Nov 9, 20178:37:03 PM  
 *  
 */
package com.zm.goods.bussiness.service;

import java.util.List;

import com.github.pagehelper.Page;
import com.zm.goods.pojo.ComponentData;
import com.zm.goods.pojo.ComponentPage;
import com.zm.goods.pojo.po.BigSalesGoodsRecord;

/**
 * ClassName: MallService <br/>
 * Function: 商城服务类. <br/>
 * date: Nov 9, 2017 8:37:03 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public interface MallService {

	Page<ComponentPage> queryComponentByPage(ComponentPage entity);
	
	List<ComponentData> queryComponentDataByPageId(String pageId);
	
	void updateComponentData(ComponentData data);
	
	void mergeBigSaleData(List<BigSalesGoodsRecord> list);
	
	List<BigSalesGoodsRecord> queryBigSaleData();

}

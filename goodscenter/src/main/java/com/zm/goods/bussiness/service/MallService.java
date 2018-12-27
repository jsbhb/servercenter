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
import com.zm.goods.pojo.DictData;
import com.zm.goods.pojo.Layout;
import com.zm.goods.pojo.PopularizeDict;
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

	/**
	 * queryByPage:分页查询商城信息. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @return
	 * @since JDK 1.7
	 */
	Page<PopularizeDict> queryDictByPage(PopularizeDict entity);
	
	/**
	 * queryById:根据编号查询规格. <br/>
	 * 
	 * @author hebin
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	PopularizeDict queryDictById(PopularizeDict dict);

	/**  
	 * save:新增模块. <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void saveDict(PopularizeDict entity);
	
	/**
	 * queryByPage:分页查询商城信息. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @return
	 * @since JDK 1.7
	 */
	Page<DictData> queryDataByPage(DictData entity);
	
	/**  
	 * save:新增数据. <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void saveData(DictData entity);

	/**  
	 * queryDataById:根据编号查询. <br/>  
	 *  
	 * @author hebin  
	 * @param id
	 * @return  
	 * @since JDK 1.7  
	 */
	DictData queryDataById(DictData entity);

	/**  
	 * initData:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void initData(PopularizeDict entity);
	
	/**
	 * 
	 * queryDataAll:查询所有. <br/>  
	 *  
	 * @author hebin  
	 * @param code
	 * @return  
	 * @since JDK 1.7
	 */
	public List<DictData> queryDataAll(Layout layout);

	/**  
	 * updateData:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void updateData(DictData entity);

	/**  
	 * updateDict:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void updateDict(PopularizeDict entity);
	
	Page<ComponentPage> queryComponentByPage(ComponentPage entity);
	
	List<ComponentData> queryComponentDataByPageId(String pageId);
	
	void updateComponentData(ComponentData data);
	
	void mergeBigSaleData(List<BigSalesGoodsRecord> list);
	
	List<BigSalesGoodsRecord> queryBigSaleData();

}

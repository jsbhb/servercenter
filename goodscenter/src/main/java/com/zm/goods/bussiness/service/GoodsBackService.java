/**  
 * Project Name:goodscenter  
 * File Name:BrandService.java  
 * Package Name:com.zm.goods.bussiness.service  
 * Date:Nov 9, 20178:37:03 PM  
 *  
 */
package com.zm.goods.bussiness.service;

import com.github.pagehelper.Page;
import com.zm.goods.pojo.GoodsEntity;
import com.zm.goods.pojo.ThirdWarehouseGoods;

/**
 * ClassName: GoodsBackService <br/>
 * Function: 后台商品服务类. <br/>
 * date: Nov 9, 2017 8:37:03 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public interface GoodsBackService {

	/**
	 * queryByPage:分页查询商品信息. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @return
	 * @since JDK 1.7
	 */
	Page<GoodsEntity> queryByPage(GoodsEntity entity);

	/**
	 * queryByPage:分页查询商品信息. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @return
	 * @since JDK 1.7
	 */
	Page<ThirdWarehouseGoods> queryByPage(ThirdWarehouseGoods entity);

	/**
	 * queryById:根据编号查询商品. <br/>
	 * 
	 * @author hebin
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	GoodsEntity queryById(int id);

	/**
	 * saveBrand:保存商品. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @param type
	 *            sync: 同步商品新增 normal：自营商品新增
	 * @since JDK 1.7
	 */
	void save(GoodsEntity entity, String type);

	/**
	 * queryThird:查询供应商商品. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @return
	 * @since JDK 1.7
	 */
	ThirdWarehouseGoods queryThird(ThirdWarehouseGoods entity);

	/**
	 * saveBrand:保存商品. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @param type
	 *            sync: 同步商品新增 normal：自营商品新增
	 * @since JDK 1.7
	 */
	void edit(GoodsEntity entity);

	/**
	 * saveBrand:保存商品. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @param type
	 *            sync: 同步商品新增 normal：自营商品新增
	 * @since JDK 1.7
	 */
	void remove(GoodsEntity entity);

	/**
	 * queryById:根据编号查询商品. <br/>
	 * 
	 * @author hebin
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	GoodsEntity checkRecordForDel(GoodsEntity entity);

	/**  
	 * saveDetailPath:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void saveDetailPath(GoodsEntity entity);

	/**
	 * queryById:根据编号查询商品. <br/>
	 * 
	 * @author hebin
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	GoodsEntity checkRecordForUpd(GoodsEntity entity);

}

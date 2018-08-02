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
import com.zm.goods.pojo.ERPGoodsTagBindEntity;
import com.zm.goods.pojo.ERPGoodsTagEntity;
import com.zm.goods.pojo.GoodsEntity;
import com.zm.goods.pojo.GoodsFielsMaintainBO;
import com.zm.goods.pojo.GoodsInfoEntity;
import com.zm.goods.pojo.GoodsInfoListForDownload;
import com.zm.goods.pojo.GoodsListDownloadParam;
import com.zm.goods.pojo.GoodsRebateEntity;
import com.zm.goods.pojo.GoodsStockEntity;
import com.zm.goods.pojo.GoodsTagBindEntity;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.TagFuncEntity;
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

	/**
	 * queryByPage:分页查询商品信息. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @return
	 * @since JDK 1.7
	 */
	Page<GoodsRebateEntity> queryAllGoods(GoodsEntity entity);

	List<GoodsRebateEntity> queryById(String itemId);
	
	GoodsRebateEntity checkRecordForRebate(GoodsRebateEntity entity);
	
	void insertGoodsRebate(List<GoodsRebateEntity> entityList);
	
	void updateGoodsRebate(GoodsRebateEntity entity);

	/**
	 * queryByPage:分页查询商品标签信息. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @return
	 * @since JDK 1.7
	 */
	Page<ERPGoodsTagEntity> queryTagForPage(ERPGoodsTagEntity entity);
	
	void insertGoodsTag(ERPGoodsTagEntity entity);
	
	void updateGoodsTag(ERPGoodsTagEntity entity);
	
	void deleteGoodsTag(ERPGoodsTagEntity entity);

	/**
	 * queryById:根据编号查询商品. <br/>
	 * 
	 * @author hebin
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	ERPGoodsTagEntity queryTagInfo(ERPGoodsTagEntity entity);

	/**
	 * queryById:根据编号查询商品. <br/>
	 * 
	 * @author hebin
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	List<ERPGoodsTagEntity> queryTagListInfo();

	/**
	 * queryById:根据编号查询商品. <br/>
	 * 
	 * @author hebin
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	List<TagFuncEntity> queryTagFuncList();

	/**
	 * queryById:根据编号查询商品. <br/>
	 * 
	 * @author hebin
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	List<ERPGoodsTagBindEntity> queryGoodsTagBindListInfo(ERPGoodsTagBindEntity entity);

	/**
	 * saveBrand:保存商品. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @param type
	 *            sync: 同步商品新增 normal：自营商品新增
	 * @since JDK 1.7
	 */
	ResultModel saveGoodsInfo(GoodsInfoEntity entity);

	/**
	 * queryById:根据编号查询商品. <br/>
	 * 
	 * @author hebin
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	GoodsInfoEntity queryGoodsInfoEntity(String itemId);

	/**
	 * saveBrand:保存商品. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @param type
	 *            sync: 同步商品新增 normal：自营商品新增
	 * @since JDK 1.7
	 */
	ResultModel updateGoodsInfo(GoodsInfoEntity entity);

	ResultModel getGoodsRebate(String itemId);

	List<GoodsInfoListForDownload> queryGoodsListForDownload(GoodsListDownloadParam param);
	
	void maintainStockByItemId(List<GoodsStockEntity> stocks);

	/**
	 * @fun 批量维护商品商详和商品主图
	 * @param list
	 */
	String maintainFiles(List<GoodsFielsMaintainBO> list);

	/**
	 * @fun 导入商品
	 * @param list
	 * @return
	 */
	ResultModel importGoods(List<GoodsInfoEntity> list);

	/**
	 * @fun 批量绑定标签
	 * @param list
	 * @return
	 */
	ResultModel tagBatchBind(List<GoodsTagBindEntity> list);
	
	ResultModel saveItemInfo(GoodsInfoEntity entity);

	Page<GoodsEntity> listPublishExceptionGoods(Integer type, GoodsEntity entity, Integer centerId);
	
	GoodsEntity queryGoodsInfoByGoodsId(GoodsEntity entity);

}

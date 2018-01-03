package com.zm.goods.bussiness.dao;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.zm.goods.pojo.DictData;
import com.zm.goods.pojo.Layout;
import com.zm.goods.pojo.PopularizeDict;

public interface MallMapper {

	/**  
	 * selectDictForPage:按照分页信息检索字典数据. <br/>  
	 *  
	 * @author hebin  
	 * @param entity
	 * @return  
	 * @since JDK 1.7  
	 */
	Page<PopularizeDict> selectDictForPage(PopularizeDict entity);

	/**  
	 * selectDictById:根据编号检索字典数据. <br/>  
	 *  
	 * @author hebin  
	 * @param id
	 * @return  
	 * @since JDK 1.7  
	 */
	PopularizeDict selectDictById(int id);

	/**  
	 * insertDict:插入字段表. <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void insertDict(PopularizeDict entity);

	/**  
	 * selectDataForPage:按照分页信息检索数据. <br/>  
	 *  
	 * @author hebin  
	 * @param entity
	 * @return  
	 * @since JDK 1.7  
	 */
	Page<DictData> selectDataForPage(DictData entity);

	/**  
	 * insertData:插入字典数据. <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void insertData(DictData entity);

	/**  
	 * queryDataById:根据编号检索字典数据. <br/>  
	 *  
	 * @author hebin  
	 * @param id
	 * @return  
	 * @since JDK 1.7  
	 */
	PopularizeDict queryDataById(Integer id);
	
	/**  
	 * insertLayout:根据编号检索字典数据. <br/>  
	 *  
	 * @author hebin  
	 * @param id
	 * @return  
	 * @since JDK 1.7  
	 */
	void insertLayout(Layout layout);

	/**  
	 * selectDataById:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param id
	 * @return  
	 * @since JDK 1.7  
	 */
	PopularizeDict selectDataById(Integer id);

	/**  
	 * insertDataBatch:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param map  
	 * @since JDK 1.7  
	 */
	void insertDataBatch(Map<String, Object> map);

	/**  
	 * selectDataAll:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param layout
	 * @return  
	 * @since JDK 1.7  
	 */
	List<DictData> selectDataAll(Layout layout);
}

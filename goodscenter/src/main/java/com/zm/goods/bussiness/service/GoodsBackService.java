/**  
 * Project Name:goodscenter  
 * File Name:BrandService.java  
 * Package Name:com.zm.goods.bussiness.service  
 * Date:Nov 9, 20178:37:03 PM  
 *  
 */
package com.zm.goods.bussiness.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.zm.goods.pojo.GoodsInfoListForDownload;
import com.zm.goods.pojo.GoodsListDownloadParam;
import com.zm.goods.pojo.GoodsRebateEntity;
import com.zm.goods.pojo.GoodsTagBindEntity;
import com.zm.goods.pojo.GoodsTagEntity;
import com.zm.goods.pojo.GuidePropertyEntity;
import com.zm.goods.pojo.GuidePropertyValueEntity;
import com.zm.goods.pojo.PropertyEntity;
import com.zm.goods.pojo.PropertyValueEntity;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.TagFuncEntity;
import com.zm.goods.pojo.bo.GoodsRender4New;
import com.zm.goods.pojo.po.BackGoodsPO;
import com.zm.goods.pojo.po.Goods;
import com.zm.goods.pojo.po.GuidePropertyBindGoods;
import com.zm.goods.pojo.po.Items;
import com.zm.goods.pojo.vo.BackGoodsItemVO;
import com.zm.goods.pojo.vo.BackGoodsVO;
import com.zm.goods.pojo.vo.BackItemsVO;
import com.zm.goods.pojo.vo.GoodsSpecsVO;

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
	 * @fun 后台供应链新增商品（只填写供应链必填字段）
	 * @param entity
	 */
	void save(BackGoodsPO entity);

	/**
	 * saveDetailPath:(保存商品详情). <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @since JDK 1.7
	 */
	void saveDetailPath(Goods entity);

	/**
	 * queryByItemdId:根据ItemId查询商品. <br/>
	 * 
	 * @author hebin
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	BackGoodsPO queryByItemdId(String itemId);

	/**
	 * queryBySpecsTpId:根据specsTpId查询商品. <br/>
	 * 
	 * @author hebin
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	BackGoodsPO queryBySpecsTpId(String specsTpId);

	/**
	 * @fun 更新商品数据
	 * @param entity
	 */
	void updateGoods(BackGoodsPO entity);

	/**
	 * queryByPage:分页查询商品标签信息. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @return
	 * @since JDK 1.7
	 */
	Page<GoodsTagEntity> queryTagForPage(GoodsTagEntity entity);

	/**
	 * @fun 新增标签
	 * @param entity
	 */
	void insertGoodsTag(GoodsTagEntity entity);

	/**
	 * @fun 更新标签
	 * @param entity
	 */
	void updateGoodsTag(GoodsTagEntity entity);

	/**
	 * @fun 删除标签
	 * @param entity
	 */
	ResultModel deleteGoodsTag(GoodsTagEntity entity);

	/**
	 * @fun 查询所有标签
	 * @return
	 */
	List<GoodsTagEntity> queryTagListInfo();

	/**
	 * @fun 根据specsTpId 获取返佣信息
	 * @param specsTpId
	 * @return
	 */
	ResultModel getGoodsRebate(String specsTpId);

	/**
	 * queryById:根据编号查询商品. <br/>
	 * 
	 * @author hebin
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	List<TagFuncEntity> queryTagFuncList();

	List<GoodsInfoListForDownload> queryGoodsListForDownload(GoodsListDownloadParam param);

	/**
	 * @fun 批量绑定标签
	 * @param list
	 * @return
	 */
	ResultModel tagBatchBind(List<GoodsTagBindEntity> list);

	List<String> getGoodsPicPath(String goodsId);

	/**
	 * @fun 更新零售价
	 * @param param
	 */
	void updateRetailPrice(Map<String, Object> param);

	/**
	 * @fun 更新返佣
	 * @param entityList
	 */
	void insertGoodsRebate(List<GoodsRebateEntity> entityList);

	/**
	 * @fun 根据条码和跨境、一般贸易属性获取商品
	 * @param encode
	 * @return
	 */
	List<GoodsRender4New> queryByEnCode(String encode, int type);

	/**
	 * @fun 根据三级分类和品牌来提示对应的Goods
	 * @param thirdCategory
	 * @param brandId
	 * @return
	 */
	List<Goods> queryGoods4Tips(String thirdCategory, String brandId);

	/**
	 * @fun 供应商商品审核
	 * @param status
	 * @param reason
	 */
	void itemAudit(Items item);

	/**
	 * @fun 更新福利商城为显示
	 * @param specsTpId
	 */
	void welfareDisplay(String specsTpId, int status);

	/**
	 * @fun 根据三级分类ID获取绑定的系列属性名
	 * @param thirdCategory
	 * @return
	 */
	List<PropertyEntity> listSpecsPropertyName(String thirdCategory);

	/**
	 * @fun 根据属性nameId获取属性value
	 * @param nameId
	 * @return
	 */
	List<PropertyValueEntity> listSpecsPropertyValue(String nameId);

	/**
	 * @fun 查询所有的系列属性名
	 * @return
	 */
	List<PropertyEntity> listAllSpecsPropertyName();

	/**
	 * @fun 根据三级分类ID获取绑定的导购属性名
	 * @param thirdCategory
	 * @return
	 */
	List<GuidePropertyEntity> listGuidePropertyName(String thirdCategory);

	/**
	 * @fun 根据属性nameId获取属性value
	 * @param nameId
	 * @return
	 */
	List<GuidePropertyValueEntity> listGuidePropertyValue(String nameId);

	/**
	 * @fun 查询所有的导购属性名
	 * @return
	 */
	List<GuidePropertyEntity> listAllGuidePropertyName();

	/**
	 * @fun 导购属性绑定商品
	 * @param bindList
	 */
	void guidePropertyBind(List<GuidePropertyBindGoods> bindList);

	/**
	 * @fun 获取供应商商品列表
	 * @param item
	 * @return
	 */
	Page<BackGoodsItemVO> listItems4Page(BackGoodsItemVO item);

	/**
	 * @fun 获取商品列表
	 * @param item
	 * @return
	 */
	Page<BackGoodsVO> listGoods4Page(BackGoodsVO item, Integer status);
	/**
	 * @fun 根据goodsId和状态获取该goods下的规格
	 * @param goodsId
	 * @param status
	 * @return
	 */
	List<GoodsSpecsVO> listGoodsSpecsTp(String goodsId, Integer status);
	/**
	 * @fun 根据specsTpId 获取items
	 * @param specsTpId
	 * @return
	 */
	List<BackItemsVO> listItemBySpecsTpId(String specsTpId);
}

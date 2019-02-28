package com.zm.goods.bussiness.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.zm.goods.bussiness.service.GoodsBackService;
import com.zm.goods.common.Pagination;
import com.zm.goods.constants.Constants;
import com.zm.goods.enummodel.ErrorCodeEnum;
import com.zm.goods.log.LogUtil;
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
import com.zm.goods.pojo.po.Items;

/**
 * ClassName: GoodsBackController <br/>
 * Function: 商品PAI控制器. <br/>
 * date: Aug 22, 2017 9:51:44 AM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */

@RestController
public class GoodsBackController {

	@Resource
	GoodsBackService goodsBackService;

	/**
	 * @fun 新增商品
	 * @param request
	 * @param version
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/goods/save", method = RequestMethod.POST)
	public ResultModel save(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody BackGoodsPO entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				goodsBackService.save(entity);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 更新商详
	 * @param request
	 * @param version
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/goods/saveDetailPath", method = RequestMethod.POST)
	public ResultModel saveDetailPath(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody Goods entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				goodsBackService.saveDetailPath(entity);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 根据itemId查看商品信息
	 * @param request
	 * @param version
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/goods/query/byItemId/{itemId}", method = RequestMethod.GET)
	public ResultModel queryByItemdId(HttpServletRequest request, @PathVariable("version") Double version,
			@PathVariable("itemId") String itemId) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				BackGoodsPO result = goodsBackService.queryByItemdId(itemId);
				return new ResultModel(true, result);
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}

	/**
	 * @fun 根据specsTpId查看商品信息
	 * @param request
	 * @param version
	 * @param specsTpId
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/goods/query/bySpecsTpId/{specsTpId}", method = RequestMethod.GET)
	public ResultModel queryBySpecsTpId(HttpServletRequest request, @PathVariable("version") Double version,
			@PathVariable("specsTpId") String specsTpId) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				BackGoodsPO result = goodsBackService.queryBySpecsTpId(specsTpId);
				return new ResultModel(true, result);
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}

	/**
	 * @fun 根据条码获取商品
	 * @param request
	 * @param version
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/goods/query/byEnCode/{type}", method = RequestMethod.GET)
	public ResultModel queryByEnCode(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestParam("encode") String encode, @PathVariable("type") int type) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				List<GoodsRender4New> result = goodsBackService.queryByEnCode(encode, type);
				return new ResultModel(true, result);
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}

	/**
	 * @fun 新增Goods时，根据三级分类和品牌来提示需要新增的商品
	 * @param request
	 * @param version
	 * @param specsTpId
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/tips", method = RequestMethod.GET)
	public ResultModel queryGoods4Tips(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestParam("thirdCategory") String thirdCategory, @RequestParam("brandId") String brandId) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				List<Goods> goods = goodsBackService.queryGoods4Tips(thirdCategory, brandId);
				return new ResultModel(true, goods);
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}

	/**
	 * @fun 更新商品信息
	 * @param request
	 * @param version
	 * @param specsTpId
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/goods/update", method = RequestMethod.POST)
	public ResultModel updateGoods(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody BackGoodsPO entity) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				goodsBackService.updateGoods(entity);
				return new ResultModel(true, null);
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}

	@RequestMapping(value = "{version}/goods/goodsTag/queryForPage", method = RequestMethod.POST)
	public ResultModel goodsTagQueryForPage(@PathVariable("version") Double version,
			@RequestBody GoodsTagEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			Page<GoodsTagEntity> page = goodsBackService.queryTagForPage(entity);
			return new ResultModel(true, page, new Pagination(page));
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/goodsTag/queryTagListInfo", method = RequestMethod.POST)
	public ResultModel queryTagListInfo(@PathVariable("version") Double version) {

		if (Constants.FIRST_VERSION.equals(version)) {

			List<GoodsTagEntity> result = goodsBackService.queryTagListInfo();
			return new ResultModel(true, result);
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/goodsTag/saveTag", method = RequestMethod.POST)
	public ResultModel saveTag(@PathVariable("version") Double version, @RequestBody GoodsTagEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				goodsBackService.insertGoodsTag(entity);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/goodsTag/modifyTag", method = RequestMethod.POST)
	public ResultModel modifyTag(@PathVariable("version") Double version, @RequestBody GoodsTagEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				goodsBackService.updateGoodsTag(entity);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/goodsTag/removeTag", method = RequestMethod.POST)
	public ResultModel removeTag(@PathVariable("version") Double version, @RequestBody GoodsTagEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				return goodsBackService.deleteGoodsTag(entity);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/goodsTag/tagFunc", method = RequestMethod.POST)
	public ResultModel tagFunc(@PathVariable("version") Double version) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				List<TagFuncEntity> result = goodsBackService.queryTagFuncList();
				return new ResultModel(true, result);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/item/rebate", method = RequestMethod.GET)
	public ResultModel getGoodsRebate(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestParam("specsTpId") String specsTpId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return goodsBackService.getGoodsRebate(specsTpId);
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/item/queryGoodsInfoListForDownload", method = RequestMethod.POST)
	public ResultModel queryGoodsInfoListForDownload(HttpServletRequest request,
			@PathVariable("version") Double version, @RequestBody GoodsListDownloadParam param) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				LogUtil.writeLog("【导出数据开始】" + System.currentTimeMillis() / 1000);
				List<GoodsInfoListForDownload> result = goodsBackService.queryGoodsListForDownload(param);
				LogUtil.writeLog("【导出数据结束】" + System.currentTimeMillis() / 1000);
				return new ResultModel(true, result);
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}

	@RequestMapping(value = "{version}/goods/tag/batch-bind", method = RequestMethod.POST)
	public ResultModel tagBatchBind(@PathVariable("version") Double version,
			@RequestBody List<GoodsTagBindEntity> list) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return goodsBackService.tagBatchBind(list);
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	@RequestMapping(value = "{version}/goods/pic/{goodsId}", method = RequestMethod.GET)
	public ResultModel getGoodsPicPath(HttpServletRequest request, @PathVariable("version") Double version,
			@PathVariable("goodsId") String goodsId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				List<String> picList = goodsBackService.getGoodsPicPath(goodsId);
				return new ResultModel(true, picList);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 更新返佣
	 * @param request
	 * @param version
	 * @param list
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/rebate", method = RequestMethod.POST)
	public ResultModel updateGoodsRebate(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody List<GoodsRebateEntity> list) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				goodsBackService.insertGoodsRebate(list);
				return new ResultModel(true, null);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 商品审核
	 * @param request
	 * @param version
	 * @param item
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/item/audit", method = RequestMethod.POST)
	public ResultModel itemAudit(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody Items item) {
		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				goodsBackService.itemAudit(item);
				return new ResultModel(true, null);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 福利商城显示/不显示
	 * @param request
	 * @param version
	 * @param item
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/welfare/display/{specsTpId}", method = RequestMethod.POST)
	public ResultModel welfareDisplay(HttpServletRequest request, @PathVariable("version") Double version,
			@PathVariable("specsTpId") String specsTpId, @RequestParam("welfare") int welfare) {
		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				goodsBackService.welfareDisplay(specsTpId,welfare);
				return new ResultModel(true, null);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}
	/**
	 * @fun 根据三级分类ID获取绑定的系列属性名
	 * @param request
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/specs/property/name", method = RequestMethod.GET)
	public ResultModel listSpecsPropertyName(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestParam("thirdCategory") String thirdCategory) {
		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				List<PropertyEntity> propertyList = goodsBackService.listSpecsPropertyName(thirdCategory);
				return new ResultModel(true, propertyList);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}
	/**
	 * @fun 根据属性nameId 获取属性value
	 * @param request
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/specs/property/value/{nameId}", method = RequestMethod.GET)
	public ResultModel listSpecsPropertyValue(HttpServletRequest request, @PathVariable("version") Double version,
			@PathVariable("nameId") String nameId) {
		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				List<PropertyValueEntity> valueList = goodsBackService.listSpecsPropertyValue(nameId);
				return new ResultModel(true, valueList);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}
		return new ResultModel(false, "版本错误");
	}
	/**
	 * @fun 查询所有系列属性名
	 * @param request
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/specs/property/name/all", method = RequestMethod.GET)
	public ResultModel listAllSpecsPropertyName(HttpServletRequest request, @PathVariable("version") Double version) {
		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				List<PropertyEntity> propertyList = goodsBackService.listAllSpecsPropertyName();
				return new ResultModel(true, propertyList);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}
		return new ResultModel(false, "版本错误");
	}
	
	/**
	 * @fun 根据三级分类ID获取绑定的导购属性名
	 * @param request
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/guide/property/name", method = RequestMethod.GET)
	public ResultModel listGuidePropertyName(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestParam("thirdCategory") String thirdCategory) {
		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				List<GuidePropertyEntity> propertyList = goodsBackService.listGuidePropertyName(thirdCategory);
				return new ResultModel(true, propertyList);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}
	/**
	 * @fun 根据属性nameId 获取属性value
	 * @param request
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/guide/property/value/{nameId}", method = RequestMethod.GET)
	public ResultModel listGuidePropertyValue(HttpServletRequest request, @PathVariable("version") Double version,
			@PathVariable("nameId") String nameId) {
		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				List<GuidePropertyValueEntity> valueList = goodsBackService.listGuidePropertyValue(nameId);
				return new ResultModel(true, valueList);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}
		return new ResultModel(false, "版本错误");
	}
	/**
	 * @fun 查询所有导购属性名
	 * @param request
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/guide/property/name/all", method = RequestMethod.GET)
	public ResultModel listAllGuidePropertyName(HttpServletRequest request, @PathVariable("version") Double version) {
		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				List<GuidePropertyEntity> propertyList = goodsBackService.listAllGuidePropertyName();
				return new ResultModel(true, propertyList);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}
		return new ResultModel(false, "版本错误");
	}
}

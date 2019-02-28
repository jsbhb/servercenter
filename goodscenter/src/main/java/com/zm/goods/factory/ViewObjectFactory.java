package com.zm.goods.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.zm.goods.pojo.GoodsTagEntity;
import com.zm.goods.pojo.po.Goods;
import com.zm.goods.pojo.po.GoodsSpecs;
import com.zm.goods.pojo.po.GoodsSpecsTradePattern;
import com.zm.goods.pojo.vo.GoodsSpecsVO;
import com.zm.goods.pojo.vo.GoodsTagVO;
import com.zm.goods.pojo.vo.GoodsVO;
import com.zm.goods.pojo.vo.SpecsTpStockVO;
import com.zm.goods.processWarehouse.model.WarehouseModel;

/**
 * @fun 统一生成前端展示用的对象
 * @author user
 *
 */
public class ViewObjectFactory {

	/**
	 * @fun 生成商品前端展示的实体类
	 * @param goods
	 * @param goodsSpecsTpList
	 * @param specsList
	 * @param stockList
	 * @return
	 */
	public static GoodsVO createGoodsVO(Goods goods, List<GoodsSpecsTradePattern> goodsSpecsTpList,
			List<GoodsSpecs> specsList, Optional<List<WarehouseModel>> stockList) {
		GoodsVO vo = new GoodsVO();
		vo.setGoodsName(goods.getGoodsName());
		vo.setBrand(goods.getBrandName());
		vo.setGoodsId(goods.getGoodsId());
		vo.setDescription(goods.getDescription());
		vo.setType(goods.getType());
		vo.setOrigin(goods.getOrigin());
		vo.setDetailPath(goods.getDetailPath());
		vo.setThirdCategory(goods.getThirdCategory());
		vo.setSecondCategory(goods.getSecondCategory());
		vo.setFirstCategory(goods.getFirstCategory());
		vo.setGoodsFileList(goods.getGoodsFileList());
		vo.setAccessPath(goods.getAccessPath());
		List<GoodsSpecsVO> specsListVO = new ArrayList<>();
		Map<String, GoodsSpecs> specsMap = specsList.stream()
				.collect(Collectors.toMap(GoodsSpecs::getSpecsId, GoodsSpecs -> GoodsSpecs));
		Map<String, WarehouseModel> stockMap = stockList.orElse(new ArrayList<>()).stream().collect(
				Collectors.toMap(WarehouseModel::getSpecsTpId, warehouseModel -> warehouseModel));
		goodsSpecsTpList.stream().forEach(specsTp -> {
			specsListVO.add(createGoodsSpecsVO(specsTp, specsMap.get(specsTp.getSpecsId()),
					Optional.ofNullable(stockMap.get(specsTp.getSpecsTpId()))));
		});
		vo.setSpecsList(specsListVO);
		return vo;
	}

	/**
	 * @fun 生成前端展示用商品规格实体类
	 * @param specsTp
	 * @param goodsSpecs
	 * @param warehouseModel
	 * @return
	 */
	public static GoodsSpecsVO createGoodsSpecsVO(GoodsSpecsTradePattern specsTp, GoodsSpecs goodsSpecs,
			Optional<WarehouseModel> warehouseModel) {
		GoodsSpecsVO vo = new GoodsSpecsVO();
		vo.setSpecsTpId(specsTp.getSpecsTpId());
		vo.setTariff(specsTp.getTariff());
		vo.setIncrementTax(specsTp.getIncrementTax());
		vo.setExciseTax(specsTp.getExciseTax());
		vo.setUnit(goodsSpecs.getUnit());
		vo.setItemId(specsTp.getItemId());
		vo.setInfo(goodsSpecs.getInfo());
		vo.setWeight(goodsSpecs.getWeight());
		vo.setStock(warehouseModel.orElse(new WarehouseModel()).getFxqty());
		vo.setRetailPrice(specsTp.getRetailPrice());
		vo.setLinePrice(specsTp.getLinePrice());
		vo.setSaleNum(specsTp.getSaleNum());
		vo.setCarton(goodsSpecs.getCarton());
		vo.setConversion(goodsSpecs.getConversion());
		return vo;
	}
	
	public static SpecsTpStockVO createSpecsTpStockVO(WarehouseModel model){
		SpecsTpStockVO vo = new SpecsTpStockVO();
		vo.setSpecsTpId(model.getSpecsTpId());
		vo.setStock(model.getFxqty());
		return vo;
	}

	public static GoodsTagVO createGoodsTagVO(GoodsTagEntity tag) {
		GoodsTagVO vo = new GoodsTagVO();
		vo.setTagName(tag.getTagName());
		vo.setSpecsTpId(tag.getSpecsTpId());
		vo.setTagFun(tag.getTagFunId());
		return vo;
	}
}

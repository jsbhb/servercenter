package com.zm.goods.bussiness.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.goods.bussiness.dao.GoodsOpenInterfaceMapper;
import com.zm.goods.bussiness.service.GoodsOpenInterfaceService;
import com.zm.goods.enummodel.ErrorCodeEnum;
import com.zm.goods.pojo.GoodsDetail;
import com.zm.goods.pojo.GoodsStock;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.utils.JSONUtil;

@Service
public class GoodsOpenInterfaceServiceImpl implements GoodsOpenInterfaceService {

	@Resource
	GoodsOpenInterfaceMapper goodsOpenInterfaceMapper;
	
	private final int MAX_SIZE = 100;

	@SuppressWarnings("unchecked")
	@Override
	public ResultModel getGoodsStock(String data) {
		
		Map<String,String> param = null;
		try {
			param = JSONUtil.parse(data, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultModel(false, ErrorCodeEnum.FORMAT_ERROR.getErrorCode(),
					ErrorCodeEnum.FORMAT_ERROR.getErrorMsg());
		}
		String itemId = param.get("itemId");
		if (itemId == null) {
			return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
					ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
		}
		String[] itemIdArr = itemId.split(",");
		if(itemIdArr.length > MAX_SIZE){
			return new ResultModel(false, ErrorCodeEnum.EXCEED_MAX_SIZE.getErrorCode(),
					ErrorCodeEnum.EXCEED_MAX_SIZE.getErrorMsg());
		}

		List<GoodsStock> list = goodsOpenInterfaceMapper.listGoodsStock(itemIdArr);
		if (list == null || list.size() == 0) {
			return new ResultModel(false, ErrorCodeEnum.NO_DATA_ERROR.getErrorCode(),
					ErrorCodeEnum.NO_DATA_ERROR.getErrorMsg());
		}
		
		if (list.size() != itemIdArr.length) {
			Map<String, GoodsStock> temp = new HashMap<String, GoodsStock>();
			for (GoodsStock detail : list) {
				temp.put(detail.getItemId(), detail);
			}
			StringBuilder sb = new StringBuilder();
			for (String id : itemIdArr) {
				if (temp.get(id) == null) {
					sb.append(id + ",");
				}
			}
			String s = sb.substring(0, sb.length() - 1);
			return new ResultModel(false, ErrorCodeEnum.GOODS_DOWNSHELVES.getErrorCode(),
					"itemId:" + s + ErrorCodeEnum.GOODS_DOWNSHELVES.getErrorMsg());
		}
		
		return new ResultModel(true, list);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ResultModel getGoodsDetail(String data) {
		
		Map<String,Object> param = null;
		try {
			param = JSONUtil.parse(data, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultModel(false, ErrorCodeEnum.FORMAT_ERROR.getErrorCode(),
					ErrorCodeEnum.FORMAT_ERROR.getErrorMsg());
		}
		
		String itemId = param.get("itemId") == null ? null : param.get("itemId").toString();
		if (itemId != null) {
			String[] itemIdArr = itemId.split(",");
			if(itemIdArr.length > MAX_SIZE){
				return new ResultModel(false, ErrorCodeEnum.EXCEED_MAX_SIZE.getErrorCode(),
						ErrorCodeEnum.EXCEED_MAX_SIZE.getErrorMsg());
			}
			List<GoodsDetail> list = goodsOpenInterfaceMapper.listGoodsDetail(itemIdArr);
			if (list == null || list.size() == 0) {
				return new ResultModel(false, ErrorCodeEnum.NO_DATA_ERROR.getErrorCode(),
						ErrorCodeEnum.NO_DATA_ERROR.getErrorMsg());
			}
			// 判断是否有商品已经下架
			if (list.size() != itemIdArr.length) {
				Map<String, GoodsDetail> temp = new HashMap<String, GoodsDetail>();
				for (GoodsDetail detail : list) {
					temp.put(detail.getItemId(), detail);
				}
				StringBuilder sb = new StringBuilder();
				for (String id : itemIdArr) {
					if (temp.get(id) == null) {
						sb.append(id + ",");
					}
				}
				String s = sb.substring(0, sb.length() - 1);
				return new ResultModel(false, ErrorCodeEnum.GOODS_DOWNSHELVES.getErrorCode(),
						"itemId:" + s + ErrorCodeEnum.GOODS_DOWNSHELVES.getErrorMsg());
			}
			//规格信息格式化
			infoFormat(list);
			return new ResultModel(true, list);
		}
		String pageStr = param.get("page") == null ? null : param.get("page").toString();
		String pageSizeStr = param.get("pageSize") == null ? null : param.get("pageSize").toString();
		if (pageStr == null || pageSizeStr == null) {
			return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
					ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
		}
		Integer page = null;
		Integer pageSize = null;
		try {
			page = Integer.valueOf(pageStr);
			pageSize = Integer.valueOf(pageSizeStr);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultModel(false, ErrorCodeEnum.NUMBER_FORMAT_ERROR.getErrorCode(),
					ErrorCodeEnum.NUMBER_FORMAT_ERROR.getErrorMsg());
		}
		if(pageSize > MAX_SIZE){
			return new ResultModel(false, ErrorCodeEnum.EXCEED_MAX_SIZE.getErrorCode(),
					ErrorCodeEnum.EXCEED_MAX_SIZE.getErrorMsg());
		}
		int startRow = page > 0 ? (page-1) * pageSize : 0;
		Map<String,Object> queryParam = new HashMap<String,Object>();
		queryParam.put("startRow", startRow);
		queryParam.put("pageSize", pageSize);
		
		List<GoodsDetail> list = goodsOpenInterfaceMapper.listGoodsDetailByPage(queryParam);
		if (list == null || list.size() == 0) {
			return new ResultModel(false, ErrorCodeEnum.NO_DATA_ERROR.getErrorCode(),
					ErrorCodeEnum.NO_DATA_ERROR.getErrorMsg());
		}
		//规格信息格式化
		infoFormat(list);
		
		return new ResultModel(true, list);
	}
	
	private void infoFormat(List<GoodsDetail> list){
		for(GoodsDetail detail : list){
			detail.infoFilter();
		}
	}

}

package com.zm.goods.bussiness.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.goods.bussiness.dao.GoodsOpenInterfaceMapper;
import com.zm.goods.bussiness.service.GoodsOpenInterfaceService;
import com.zm.goods.pojo.ErrorCodeEnum;
import com.zm.goods.pojo.GoodsDetail;
import com.zm.goods.pojo.GoodsStock;
import com.zm.goods.pojo.ResultModel;

@Service
public class GoodsOpenInterfaceServiceImpl implements GoodsOpenInterfaceService {

	@Resource
	GoodsOpenInterfaceMapper goodsOpenInterfaceMapper;

	@Override
	public ResultModel getGoodsStock(String itemId) {
		if (itemId == null) {
			return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
					ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
		}
		String[] itemIdArr = itemId.split(",");

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

	@Override
	public ResultModel getGoodsDetail(Map<String, String> param) {
		String itemId = param.get("itemId");
		if (itemId != null) {
			String[] itemIdArr = itemId.split(",");
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
			return new ResultModel(true, list);
		}
		String pageStr = param.get("page");
		String pageSizeStr = param.get("pageSize");
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
		
		int startRow = page > 0 ? (page-1) * pageSize : 0;
		Map<String,Object> queryParam = new HashMap<String,Object>();
		queryParam.put("startRow", startRow);
		queryParam.put("pageSize", pageSize);
		
		List<GoodsDetail> list = goodsOpenInterfaceMapper.listGoodsDetailByPage(queryParam);
		if (list == null || list.size() == 0) {
			return new ResultModel(false, ErrorCodeEnum.NO_DATA_ERROR.getErrorCode(),
					ErrorCodeEnum.NO_DATA_ERROR.getErrorMsg());
		}
		
		return new ResultModel(true, list);
	}

}

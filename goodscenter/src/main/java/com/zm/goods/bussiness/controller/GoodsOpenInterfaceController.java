package com.zm.goods.bussiness.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.goods.bussiness.service.GoodsOpenInterfaceService;
import com.zm.goods.constants.Constants;
import com.zm.goods.pojo.ErrorCodeEnum;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.utils.JSONUtil;

@RestController
public class GoodsOpenInterfaceController {

	@Resource
	GoodsOpenInterfaceService goodsOpenInterfaceService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "{version}/goods_stock", method = RequestMethod.POST)
	public ResultModel getGoodsStock(@PathVariable("version") Double version, HttpServletRequest req) {

		if (Constants.FIRST_VERSION.equals(version)) {
			String data = req.getParameter("data");
			Map<String,String> param = null;
			try {
				param = JSONUtil.parse(data, Map.class);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResultModel(false, ErrorCodeEnum.FORMAT_ERROR.getErrorCode(),
						ErrorCodeEnum.FORMAT_ERROR.getErrorMsg());
			}
			try {
				String itemId = param.get("itemId");
				return goodsOpenInterfaceService.getGoodsStock(itemId);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResultModel(false, ErrorCodeEnum.SERVER_ERROR.getErrorCode(),
						ErrorCodeEnum.SERVER_ERROR.getErrorMsg());
			}
			
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "{version}/goods_detail", method = RequestMethod.POST)
	public ResultModel getGoodsDetail(@PathVariable("version") Double version, HttpServletRequest req) {

		if (Constants.FIRST_VERSION.equals(version)) {
			String data = req.getParameter("data");
			Map<String,String> param = null;
			try {
				param = JSONUtil.parse(data, Map.class);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResultModel(false, ErrorCodeEnum.FORMAT_ERROR.getErrorCode(),
						ErrorCodeEnum.FORMAT_ERROR.getErrorMsg());
			}
			try {
				return goodsOpenInterfaceService.getGoodsDetail(param);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResultModel(false, ErrorCodeEnum.SERVER_ERROR.getErrorCode(),
						ErrorCodeEnum.SERVER_ERROR.getErrorMsg());
			}
			
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}
}

package com.zm.goods.bussiness.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.goods.bussiness.service.GoodsOpenInterfaceService;
import com.zm.goods.constants.Constants;
import com.zm.goods.enummodel.ErrorCodeEnum;
import com.zm.goods.pojo.ResultModel;

@RestController
public class GoodsOpenInterfaceController {

	@Resource
	GoodsOpenInterfaceService goodsOpenInterfaceService;
	
	@RequestMapping(value = "{version}/goods_stock", method = RequestMethod.POST)
	public ResultModel getGoodsStock(@PathVariable("version") Double version, HttpServletRequest req) {

		if (Constants.FIRST_VERSION.equals(version)) {
			String data = req.getParameter("data");
			String appKey = req.getParameter("appKey");
			if (data == null || appKey == null || "".equals(data) || "".equals(appKey)) {
				return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
						ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
			}
			try {
				return goodsOpenInterfaceService.getGoodsStock(data);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResultModel(false, ErrorCodeEnum.SERVER_ERROR.getErrorCode(),
						ErrorCodeEnum.SERVER_ERROR.getErrorMsg());
			}
			
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}
	
	@RequestMapping(value = "{version}/goods_detail", method = RequestMethod.POST)
	public ResultModel getGoodsDetail(@PathVariable("version") Double version, HttpServletRequest req) {

		if (Constants.FIRST_VERSION.equals(version)) {
			String data = req.getParameter("data");
			String appKey = req.getParameter("appKey");
			if (data == null || appKey == null || "".equals(data) || "".equals(appKey)) {
				return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
						ErrorCodeEnum.MISSING_PARAM.getErrorCode());
			}
			try {
				return goodsOpenInterfaceService.getGoodsDetail(data);
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

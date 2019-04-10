package com.zm.user.bussiness.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.user.bussiness.service.GradeFrontService;
import com.zm.user.common.ResultModel;
import com.zm.user.constants.Constants;
import com.zm.user.enummodel.ErrorCodeEnum;

@RestController
public class GradeFrontController {

	@Resource
	GradeFrontService gradeFrontService;

	/**
	 * @fun 获取微店头部信息
	 * @param version
	 * @param gradeId
	 * @return
	 */
	@RequestMapping(value = "auth/{version}/grade/config/{centerId}", method = RequestMethod.GET)
	public ResultModel getGradeConfig(@PathVariable("version") Double version,
			@PathVariable("centerId") Integer centerId,
			@RequestParam(value = "shopId", required = false) Integer shopId,
			@RequestParam(value = "userId", required = false) Integer userId) {
		if (Constants.FIRST_VERSION.equals(version)) {
			if (shopId == null && userId == null) {
				return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
						ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
			}
			return new ResultModel(true, gradeFrontService.getGradeConfig(centerId, shopId, userId));
		}
		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 获取区域中心主站url
	 * @param version
	 * @param centerId
	 * @return
	 */
	@RequestMapping(value = "{version}/grade-url/{centerId}", method = RequestMethod.GET)
	public String getClientUrl(@PathVariable("centerId") Integer centerId, @PathVariable("version") Double version) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return gradeFrontService.getClientUrl(centerId);
		}
		return null;
	}

	/**
	 * @fun 获取店铺的手机端url
	 * @param version
	 * @param shopId
	 * @return
	 */
	@RequestMapping(value = "{version}/getMobileUrl/{shopId}", method = RequestMethod.GET)
	public ResultModel getMobileUrl(@PathVariable("version") Double version, @PathVariable("shopId") Integer shopId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return new ResultModel(true, gradeFrontService.getMobileUrl(shopId));
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "auth/{version}/grade/shopBillboard", method = RequestMethod.POST)
	public void getShopBillboard(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res, @RequestParam(value = "shopId", required = true) Integer shopId) {
		if (Constants.FIRST_VERSION.equals(version)) {
			byte[] data = gradeFrontService.getShopBillboard(shopId);
			res.setContentType("image/png");
			OutputStream stream;
			try {
				stream = res.getOutputStream();
				Base64 encoder = new Base64 ();
				stream.write(encoder.encode(data));
		        stream.flush();
		        stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

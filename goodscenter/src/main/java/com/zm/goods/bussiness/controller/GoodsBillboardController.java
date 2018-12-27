package com.zm.goods.bussiness.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.goods.bussiness.service.GoodsBillboardService;
import com.zm.goods.constants.Constants;
import com.zm.goods.pojo.dto.GoodsBillboardDTO;

@RestController
public class GoodsBillboardController {

	@Resource
	GoodsBillboardService goodsBillboardService;

	/**
	 * @fun 获取商品牌图片
	 * @param version
	 * @param req
	 * @param res
	 */
	@RequestMapping(value = "auth/{version}/goodsBillboard", method = RequestMethod.POST)
	public void getGoodsBillboard(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res, @RequestBody GoodsBillboardDTO dto) {
		if (Constants.FIRST_VERSION.equals(version)) {
			byte[] data = goodsBillboardService.getGoodsBillboard(dto);
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

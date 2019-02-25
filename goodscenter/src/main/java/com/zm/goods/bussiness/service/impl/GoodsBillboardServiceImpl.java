package com.zm.goods.bussiness.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.zm.goods.bussiness.dao.GoodsMapper;
import com.zm.goods.bussiness.service.GoodsBillboardService;
import com.zm.goods.bussiness.service.GoodsService;
import com.zm.goods.constants.Constants;
import com.zm.goods.feignclient.ThirdFeignClient;
import com.zm.goods.feignclient.model.AppletCodeParameter;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.bo.GoodsBillboard;
import com.zm.goods.pojo.dto.GoodsBillboardDTO;
import com.zm.goods.pojo.vo.GoodsVO;
import com.zm.goods.utils.HttpClientUtil;
import com.zm.goods.utils.ImageUtils;

@Service
public class GoodsBillboardServiceImpl implements GoodsBillboardService {

	@Value("${staticUrl}")
	private String staticUrl;

	@Resource
	ThirdFeignClient thirdFeignClient;
	
	@Resource
	GoodsService goodsService;

	@Resource
	GoodsMapper goodsMapper;

	@Override
	public byte[] getGoodsBillboard(GoodsBillboardDTO dto) {
		GoodsBillboard board = null;
		if (dto.getId() == null) {
			board = new GoodsBillboard();
			board.setDefault(staticUrl);// 使用默认模板
		}
		// 获取商品图片、名称、类型、价格
		GoodsVO goodsVO = goodsService.listGoods(dto.getGoodsId(), null, false);
		// 拼装url
		String url = assemblingUrl(dto);
		// 获取小程序二维码流
		InputStream in = null;
		// 如果二维码不存在
		byte[] temp = codeExist(url);
		if (temp == null) {
			// 生成二维码
			AppletCodeParameter param = new AppletCodeParameter();
			param.setScene("shopId=" + dto.getShopId() + "&goodsId=" + dto.getGoodsId());
			param.setPage("web/goodsDetail/goodsDetail");
			param.setWidth("400");
			ResultModel model = thirdFeignClient.getAppletCode(Constants.FIRST_VERSION, param);
			if (!model.isSuccess()) {
				throw new RuntimeException("获取二维码失败");
			}
			// 图片字符串需base64解码
			Base64 base = new Base64();
			in = new ByteArrayInputStream(base.decode(model.getObj().toString()));
		} else {
			in = new ByteArrayInputStream(temp);
		}
		// end
		ImageUtils imageUtil = new ImageUtils();
		byte[] result = imageUtil.drawGoodsBillboardDTO(goodsVO, board, in, staticUrl);
		return result;
	}

	private String assemblingUrl(GoodsBillboardDTO param) {
		Integer shopId = param.getShopId();
		String goodsId = param.getGoodsId();// 获取goodsId
		return staticUrl + "/wechat/appletcode/" + shopId + "/goods/" + goodsId + ".png";
	}

	private byte[] codeExist(String url) {
		return HttpClientUtil.getByteArr(url);
	}

}

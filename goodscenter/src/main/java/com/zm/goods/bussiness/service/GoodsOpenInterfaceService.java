package com.zm.goods.bussiness.service;

import com.zm.goods.pojo.ResultModel;

public interface GoodsOpenInterfaceService {

	ResultModel getGoodsStock(String data);

	ResultModel getGoodsDetail(String data);

}

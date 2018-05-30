package com.zm.goods.bussiness.service;

import java.util.Set;

import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.bo.GoodsItemBO;

public interface GoodsFeignService {

	ResultModel manualOrderGoodsCheck(Set<GoodsItemBO> set);

}

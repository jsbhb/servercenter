package com.zm.goods.aop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.zm.goods.annotation.GoodsLifeCycle;
import com.zm.goods.bussiness.dao.GoodsMapper;
import com.zm.goods.log.LogUtil;
import com.zm.goods.pojo.GoodsItemEntity;
import com.zm.goods.pojo.bo.GoodsLifeCycleModel;
import com.zm.goods.utils.IPUtil;

@Component
@Aspect
public class GoodsLifeCycleAOP {

	@Resource
	GoodsMapper goodsMapper;

	@SuppressWarnings("unchecked")
	@After(value = "@annotation(goodsLifeCycle)", argNames = "goodsLifeCycle")
	public void afterCustomPointCut(JoinPoint jp, GoodsLifeCycle goodsLifeCycle) {
		Object[] arguments = jp.getArgs();
		GoodsLifeCycleModel model = null;
		List<GoodsLifeCycleModel> modelList = new ArrayList<GoodsLifeCycleModel>();
		List<String> list = null;
		try {
			if (arguments != null && arguments.length > 0) {
				for (Object obj : arguments) {
					if (obj instanceof java.util.List) {
						list = (List<String>) obj;
						break;
					} else if(obj instanceof java.lang.String){
						list = Arrays.asList(obj.toString().split(","));
						break;
					} else if(obj instanceof com.zm.goods.pojo.GoodsItemEntity){
						list = Arrays.asList(((GoodsItemEntity) obj).getItemId().split(","));
						break;
					}
				}
			}
		} catch (Exception e) {
			LogUtil.writeErrorLog("【记录商品生命周期出错】", e);
		}
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String ip = IPUtil.getOriginIp(request);
		int status = goodsLifeCycle.status();
		int isFx = goodsLifeCycle.isFx();
		String remark = goodsLifeCycle.remark();
		//生成商品生命周期实体类并保存
		if(list != null){
			for (String str : list) {
				model = new GoodsLifeCycleModel();
				model.setItemId(str);
				model.setIsFx(isFx);
				model.setRemark(remark);
				model.setStatus(status);
				model.setIp(ip);
				modelList.add(model);
			}
			goodsMapper.insertGoodsLifeCycleBatch(modelList);
		}
	}
}

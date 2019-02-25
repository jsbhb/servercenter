/**  
 * Project Name:goodscenter  
 * File Name:MallServiceImpl.java  
 * Package Name:com.zm.goods.bussiness.service.impl  
 * Date:Jan 2, 20182:57:31 PM  
 *  
 */
package com.zm.goods.bussiness.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.goods.bussiness.dao.GoodsItemMapper;
import com.zm.goods.bussiness.dao.MallMapper;
import com.zm.goods.bussiness.service.MallService;
import com.zm.goods.pojo.ComponentData;
import com.zm.goods.pojo.ComponentPage;
import com.zm.goods.pojo.po.BigSalesGoodsRecord;
import com.zm.goods.pojo.po.GoodsPrice;

/**
 * ClassName: MallServiceImpl <br/>
 * date: Jan 2, 2018 2:57:31 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
@Service
public class MallServiceImpl implements MallService {

	@Resource
	MallMapper mallMapper;
	
	@Resource
	GoodsItemMapper goodsItemMapper;

	@Override
	public Page<ComponentPage> queryComponentByPage(ComponentPage entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return mallMapper.selectComponentForPage(entity);
	}

	@Override
	public List<ComponentData> queryComponentDataByPageId(String pageId) {
		return mallMapper.selectComponentDataByPageId(pageId);
	}

	@Override
	public void updateComponentData(ComponentData data) {
		mallMapper.updateComponentData(data);
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void mergeBigSaleData(List<BigSalesGoodsRecord> list) {
		List<BigSalesGoodsRecord> insList = new ArrayList<BigSalesGoodsRecord>();
		List<BigSalesGoodsRecord> updList = new ArrayList<BigSalesGoodsRecord>();
		List<GoodsPrice> priceList = new ArrayList<GoodsPrice>();
		GoodsPrice price = null;
		for(BigSalesGoodsRecord bsgr:list) {
			price = new GoodsPrice();
			price.setItemId(bsgr.getItemId());
			price.setLinePrice(bsgr.getLinePrice());
			priceList.add(price);
			
			if (bsgr.getId() == 0) {
				insList.add(bsgr);
			} else {
				updList.add(bsgr);
			}
		}
		if (priceList.size()>0) {
			goodsItemMapper.updateGoodsItemPrice(priceList);
		}
		if (insList.size() > 0) {
			mallMapper.insertBigSaleDataa(insList);
		}
		if (updList.size() > 0) {
			mallMapper.updateBigSaleData(updList);
		}
	}

	@Override
	public List<BigSalesGoodsRecord> queryBigSaleData() {
		return mallMapper.selectBigSaleData();
	}
}

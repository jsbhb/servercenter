package com.zm.thirdcenter.bussiness.info.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.thirdcenter.bussiness.dao.InfoMapper;
import com.zm.thirdcenter.pojo.NewsModel;
import com.zm.thirdcenter.pojo.Pagination;

@Service
public class InfoServiceImpl implements InfoService {

	@Resource
	InfoMapper infoMapper;

	@Override
	public void saveNews(NewsModel model) {
		infoMapper.saveNews(model);
	}

	@Override
	public List<NewsModel> listNewsRand(int type) {
		List<NewsModel> newsList = infoMapper.listNewsRand(type);
		return newsList;
	}

	@Override
	public List<NewsModel> listNews(int type, Pagination pagination) {
		Map<String,Object> param = new HashMap<>();
		int currentPage = pagination.getCurrentPage();
		int numPerPage = pagination.getNumPerPage();
		int startRow = currentPage <= 1 ? 0 : (currentPage-1)*numPerPage;
		param.put("startRow", startRow);
		param.put("numPerPage", numPerPage);
		param.put("type", type);
		List<NewsModel> newsList = infoMapper.listNews(param);
		int count = infoMapper.countNews(type);
		pagination.setTotalRows((long)count);
		pagination.webListConverter();
		return newsList;
	}

}

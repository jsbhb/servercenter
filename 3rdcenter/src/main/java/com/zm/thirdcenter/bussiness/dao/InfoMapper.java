package com.zm.thirdcenter.bussiness.dao;

import java.util.List;
import java.util.Map;

import com.zm.thirdcenter.pojo.NewsModel;

public interface InfoMapper {

	void saveNews(NewsModel model);

	List<NewsModel> listNewsRand(int type);

	List<NewsModel> listNews(Map<String,Object> param);

	int countNews(int type);
}

package com.zm.thirdcenter.bussiness.info.service;

import java.util.List;

import com.zm.thirdcenter.pojo.NewsModel;
import com.zm.thirdcenter.pojo.Pagination;

public interface InfoService {

	void saveNews(NewsModel model);

	List<NewsModel> listNewsRand(int type);

	List<NewsModel> listNews(int type, Pagination pagination);
}

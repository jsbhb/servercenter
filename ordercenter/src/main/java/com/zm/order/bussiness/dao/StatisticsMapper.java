package com.zm.order.bussiness.dao;

import java.util.List;

import com.zm.order.pojo.OrderInfo;

public interface StatisticsMapper {

	List<OrderInfo> queryAll();

	List<OrderInfo> queryLastWeek(String time);
}

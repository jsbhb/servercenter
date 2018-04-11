package com.zm.finance.bussiness.dao;

import java.util.List;

import com.github.pagehelper.Page;
import com.zm.finance.pojo.card.Card;

public interface CardMapper {

	void insertCard(Card card);

	void updateCard(Card card);

	List<Card> getCard(Integer gradeId);

	void removeCard(Integer id);
	
	Page<Card> selectForPage(Card card);
	
	Card queryByCardId(Card card);
}

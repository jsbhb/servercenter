package com.zm.finance.bussiness.service;

import com.zm.finance.pojo.ResultModel;
import com.zm.finance.pojo.card.Card;

public interface CardService {

	ResultModel addCard(Card card);

	ResultModel modifyCard(Card card);

	ResultModel getCard(Integer id, Integer type);

	ResultModel removeCard(Integer id);

	ResultModel checkCardNo(String cardNo);
}

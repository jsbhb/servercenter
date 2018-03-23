package com.zm.finance.bussiness.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.finance.bussiness.dao.CardMapper;
import com.zm.finance.bussiness.service.CardService;
import com.zm.finance.pojo.ResultModel;
import com.zm.finance.pojo.card.Card;
import com.zm.finance.util.BankDictionary;
import com.zm.finance.util.HttpClientUtil;
import com.zm.finance.util.JSONUtil;

@Service
@Transactional
public class CardServiceImpl implements CardService{

	@Resource
	CardMapper cardMapper;
	
	@Override
	public ResultModel addCard(Card card) {
		cardMapper.insertCard(card);
		return new ResultModel(true);
	}

	@Override
	public ResultModel modifyCard(Card card) {
		cardMapper.updateCard(card);
		return new ResultModel(true);
	}

	@Override
	public ResultModel getCard(Integer id, Integer type) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("typeId", id);
		param.put("type", type);
		return new ResultModel(true,cardMapper.getCard(param));
	}

	@Override
	public ResultModel removeCard(Integer id) {
		cardMapper.removeCard(id);
		return new ResultModel(true);
	}

	private static final String url = "https://ccdcapi.alipay.com/validateAndCacheCardInfo.json";
	@SuppressWarnings("rawtypes")
	@Override
	public ResultModel checkCardNo(String cardNo) {
		Map<String,String> param = new HashMap<String, String>();
		param.put("_input_charset", "utf-8");
		param.put("cardNo", cardNo);
		param.put("cardBinCheck", "true");
		String result = HttpClientUtil.get(url, param);
		Map resultMap = JSONUtil.parse(result, Map.class);
		if((Boolean) resultMap.get("validated")){
			String bank = resultMap.get("bank").toString();
			return new ResultModel(true, BankDictionary.bank.get(bank));
		}
		return new ResultModel(false,"卡号有误");
	}

	@Override
	public Page<Card> getCardForPage(Card card) {
		PageHelper.startPage(card.getCurrentPage(), card.getNumPerPage(), true);
		return cardMapper.selectForPage(card);
	}
	
	@Override
	public ResultModel queryByCardId(Card card) {
		return new ResultModel(true,cardMapper.queryByCardId(card));
	}
}

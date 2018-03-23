package com.zm.finance.bussiness.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.zm.finance.bussiness.service.CardService;
import com.zm.finance.constants.Constants;
import com.zm.finance.pojo.Pagination;
import com.zm.finance.pojo.ResultModel;
import com.zm.finance.pojo.card.Card;

@RestController
public class CardController {

	@Resource
	CardService cardService;

	@RequestMapping(value = "{version}/finance/card", method = RequestMethod.POST)
	public ResultModel addCard(@PathVariable("version") Double version, @RequestBody Card card) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return cardService.addCard(card);
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/finance/modify/card", method = RequestMethod.POST)
	public ResultModel modifyCard(@PathVariable("version") Double version, @RequestBody Card card) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return cardService.modifyCard(card);
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/finance/card/{id}", method = RequestMethod.GET)
	public ResultModel getCard(@PathVariable("version") Double version, @PathVariable("id") Integer id,
			@RequestParam("type") Integer type) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return cardService.getCard(id, type);
		}
		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/finance/card/{id}", method = RequestMethod.DELETE)
	public ResultModel removeCard(@PathVariable("version") Double version, @PathVariable("id") Integer id) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return cardService.removeCard(id);
		}
		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/finance/card/check", method = RequestMethod.GET)
	public ResultModel checkCardNo(@PathVariable("version") Double version, @RequestParam("cardNo") String cardNo) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return cardService.checkCardNo(cardNo);
		}
		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/finance/card/queryForPage", method = RequestMethod.POST)
	public ResultModel queryForPage(@PathVariable("version") Double version, @RequestBody Card entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			Page<Card> page = cardService.getCardForPage(entity);
			return new ResultModel(true, page, new Pagination(page));
		}

		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/finance/card/queryByCardId", method = RequestMethod.POST)
	public ResultModel queryByCardId(@PathVariable("version") Double version, @RequestBody Card entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return cardService.queryByCardId(entity);
		}

		return new ResultModel(false, "版本错误");
	}
	
}

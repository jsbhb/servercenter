package com.zm.supplier.supplierinf.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "items")
public class EdbGoodsInfoList {

	
	private List<EdbGoodsInfo> list;

	@XmlElement(name = "Rows")
	public List<EdbGoodsInfo> getList() {
		return list;
	}

	public void setList(List<EdbGoodsInfo> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "EdbGoodsInfoList [list=" + list + "]";
	}
}

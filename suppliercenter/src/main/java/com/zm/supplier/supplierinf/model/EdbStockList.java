package com.zm.supplier.supplierinf.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "items")
public class EdbStockList {

	private List<EdbStockModel> edbStockList;

	@XmlElement(name = "Rows")
	public List<EdbStockModel> getEdbStockList() {
		return edbStockList;
	}

	public void setEdbStockList(List<EdbStockModel> edbStockList) {
		this.edbStockList = edbStockList;
	}

	@Override
	public String toString() {
		return "EdbStockList [edbStockList=" + edbStockList + "]";
	}
	
}

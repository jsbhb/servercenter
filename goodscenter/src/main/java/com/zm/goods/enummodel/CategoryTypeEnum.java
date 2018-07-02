/**  
 * Project Name:cardmanager  
 * File Name:GoodsStatusEnum.java  
 * Package Name:com.card.manager.factory.goods.pojo  
 * Date:Dec 21, 201710:13:19 AM  
 *  
 */
package com.zm.goods.enummodel;

/**
 * ClassName: CategoryTypeEnum <br/>
 * Function: 分类类型枚举类. <br/>
 * date: Dec 21, 2017 10:13:19 AM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public enum CategoryTypeEnum {

	FIRST("一级分类", "1"), SECOND("二级分类", "2"), THIRD("三级分类", "3");

	private String name;
	private String type;

	private CategoryTypeEnum(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public static String getName(String type) {
		for (CategoryTypeEnum c : CategoryTypeEnum.values()) {
			if (c.getType().equals(type)) {
				return c.name;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}

/**  
 * Project Name:cardmanager  
 * File Name:DiagramPojo.java  
 * Package Name:com.card.manager.factory.auth.model  
 * Date:Apr 25, 201810:14:02 AM  
 *  
 */
package com.zm.order.pojo.bo;

/**
 * ClassName: DiagramPojo <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: Apr 25, 2018 10:14:02 AM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public class DiagramBO {
	private String name;
	private Object value;

	public DiagramBO(String name,Object value){
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}

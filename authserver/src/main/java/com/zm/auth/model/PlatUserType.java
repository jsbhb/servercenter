/**  
 * Project Name:authserver  
 * File Name:PlatUserType.java  
 * Package Name:com.zm.gateway.model  
 * Date:Aug 30, 20174:41:04 PM  
 *  
 */
package com.zm.auth.model;

/**
 * ClassName: PlatUserType <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: Aug 30, 2017 4:41:04 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public enum PlatUserType {
	CROSS_BORDER("跨境业务", 1), EARA("区域中心", 2), BIG_TRADE("大贸", 3), DISTRIBUTION("分销", 4), CONSUMER("消费者", 5), B2B_BUSSINESS("订货平台",6),PUSH_USER("推手平台",7);

	private String name;
	private int index;

	private PlatUserType(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}

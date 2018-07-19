/**  
 * Project Name:cardmanager  
 * File Name:GoodsStatusEnum.java  
 * Package Name:com.card.manager.factory.goods.pojo  
 * Date:Dec 21, 201710:13:19 AM  
 *  
 */
package com.zm.goods.enummodel;

/**  
 * ClassName: GoodsStatusEnum <br/>  
 * Function: 商品状态枚举类. <br/>   
 * date: Dec 21, 2017 10:13:19 AM <br/>  
 *  
 * @author hebin  
 * @version   
 * @since JDK 1.7  
 */
public enum  GoodsStatusEnum {
	
	NOTFX("不可分销",0),FX("可分销",1);
	
	private String name;
	private int index;
	
	private GoodsStatusEnum(String name,int index){
		this.name = name;
		this.index = index;
	}
	
    public static String getName(int index) {
        for (GoodsStatusEnum c : GoodsStatusEnum.values()) {
            if (c.getIndex() == index) {
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}

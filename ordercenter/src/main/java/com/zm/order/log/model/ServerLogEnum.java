package com.zm.order.log.model;

public enum ServerLogEnum {

	USER_CENTER(1, "用户中心"), GOODS_CENTER(2, "商品中心"), ORDER_CENTER(3, "订单中心"), SUPPLIER_CENTER(4,
			"供应商中心"), PAYMENT_CENTER(5, "支付中心"), THIRD_CENTER(6, "第三方服务中心"), AUTH_CENTER(7, "权限中心"), TIME_TASK_CENTER(8,
					"定时器中心"),ACTIVITY_CENTER(9, "活动中心");

	private Integer serverId;
	private String serverName;

	private ServerLogEnum(Integer serverId, String serverName) {
		this.serverId = serverId;
		this.serverName = serverName;
	}

	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
}

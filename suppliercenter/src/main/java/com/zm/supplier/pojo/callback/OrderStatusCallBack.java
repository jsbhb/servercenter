package com.zm.supplier.pojo.callback;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.zm.supplier.constants.Constants;
import com.zm.supplier.pojo.callback.base.CallBackBase;

/**
 * @fun 订单状态回传类
 * @author user
 *
 */
public class OrderStatusCallBack extends CallBackBase {

	private String orderId;
	private Integer type;
	private Integer status;
	private String expressName;
	private String expressKey;
	private String expressId;
	private String errorMsg;

	public boolean checkParam() {
		if (appKey == null || nonceStr == null || sign == null || status == null || orderId == null || type == null)
			return false;
		if (Constants.ORDER_DELIVER.equals(status)) {
			if (expressKey == null || expressName == null || expressId == null) {
				return false;
			}
		}
		return true;
	}

	public boolean checkOrderStatus() {
		if (Constants.ORDER_CUSTOMS.equals(status) || Constants.ORDER_DZFX.equals(status)
				|| Constants.ORDER_DELIVER.equals(status) || Constants.ORDER_EXCEPTION.equals(status)) {
			return true;
		}
		return false;
	}
	
	public void decodeExpressName() {
		if (expressName != null && !"".equals(expressName)) {
			try {
				expressName = URLDecoder.decode(expressName, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public String getExpressKey() {
		return expressKey;
	}

	public void setExpressKey(String expressKey) {
		this.expressKey = expressKey;
	}

	public String getExpressId() {
		return expressId;
	}

	public void setExpressId(String expressId) {
		this.expressId = expressId;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	@Override
	public String toString() {
		return "OrderStatusCallBack [orderId=" + orderId + ", type=" + type + ", status=" + status + ", expressName="
				+ expressName + ", expressKey=" + expressKey + ", expressId=" + expressId + ", errorMsg=" + errorMsg
				+ ", appKey=" + appKey + ", nonceStr=" + nonceStr + ", sign=" + sign + "]";
	}
}

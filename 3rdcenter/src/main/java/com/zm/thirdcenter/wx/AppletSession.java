package com.zm.thirdcenter.wx;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.zm.thirdcenter.utils.JSONUtil;

public class AppletSession implements Serializable {

	/**
	 * serialVersionUID:
	 * 
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 1L;
	private String json;
	private String openid;
	private String session_key;
	private String unionid;
	private String errcode;
	@SuppressWarnings("unused")
	private String errMsg;
	private Map<String, String> errMsgMap = new HashMap<String, String>() {
		/**
		 * @since JDK 1.7
		 */
		private static final long serialVersionUID = 1L;

		{
			put("-1", "系统繁忙，此时请开发者稍候再试");
			put("40029", "code 无效");
			put("45011", "频率限制，每个用户每分钟100次");
		}
	};

	public AppletSession(String json) {
		this.json = json;
		@SuppressWarnings("unchecked")
		Map<String, Object> temp = JSONUtil.parse(json, Map.class);
		this.openid = temp.get("openid") + "";
		this.unionid = temp.get("unionid") + "";
		this.session_key = temp.get("session_key") + "";
		this.errcode = temp.get("errcode") + "";
		this.errMsg = temp.get("errMsg") + "";
	}

	public boolean isAvailable() {
		if (openid == null && unionid == null)
			return false;
		return true;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getSession_key() {
		return session_key;
	}

	public void setSession_key(String session_key) {
		this.session_key = session_key;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public String getErrMsg() {
		return errMsgMap.get(errcode);
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getJson() {
		return json;
	}

}

package com.zm.thirdcenter.wx;

import java.io.Serializable;
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
	private String errmsg;

	public AppletSession(String json) {
		this.json = json;
		@SuppressWarnings("unchecked")
		Map<String, Object> temp = JSONUtil.parse(json, Map.class);
		this.openid = temp.get("openid") == null ? null : temp.get("openid") + "";
		this.unionid = temp.get("unionid") == null ? null : temp.get("unionid") + "";
		this.session_key = temp.get("session_key") == null ? null : temp.get("session_key") + "";
		this.errcode = temp.get("errcode") == null ? null : temp.get("errcode") + "";
		this.errmsg = temp.get("errmsg") == null ? null : temp.get("errmsg") + "";
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
		return errmsg;
	}

	public void setErrMsg(String errMsg) {
		this.errmsg = errMsg;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getJson() {
		return json;
	}

}

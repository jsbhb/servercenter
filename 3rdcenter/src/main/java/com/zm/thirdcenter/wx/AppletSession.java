package com.zm.thirdcenter.wx;

import java.io.Serializable;
import java.util.Map;

import com.zm.thirdcenter.utils.JSONUtil;
import com.zm.thirdcenter.utils.RetryUtils.ResultCheck;

public class AppletSession implements ResultCheck, Serializable{

	/**  
	 * serialVersionUID:
	 * @since JDK 1.7  
	 */
	private static final long serialVersionUID = 1L;
	private String json;
	private String openid;
	private String session_key;
	private String unionid;
	private String errcode;
	private String errMsg;
	
	public AppletSession(String json){
		this.json = json;
		@SuppressWarnings("unchecked")
		Map<String, String> temp = JSONUtil.parse(json, Map.class);
		this.openid = temp.get("openid");
		this.unionid = temp.get("unionid");
		this.session_key = temp.get("session_key");
		this.errcode = temp.get("errcode");
		this.errMsg = temp.get("errMsg");
	}
	
	public boolean isAvailable() {
		if (!"0".equals(errcode))
			return false;
		return openid != null;
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
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public void setJson(String json) {
		this.json = json;
	}

	@Override
	public boolean matching() {
		return isAvailable();
	}

	@Override
	public String getJson() {
		return json;
	}

}

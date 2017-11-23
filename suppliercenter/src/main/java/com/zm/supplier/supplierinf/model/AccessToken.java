package com.zm.supplier.supplierinf.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessToken {

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("expires_in")
	private Long expiresIn;

	private Long expiredTime;

	public boolean isAvailable()
    {
        if (expiredTime == null)
            return false;
        if (expiredTime < System.currentTimeMillis())
            return false;
        return accessToken != null;
    }
	
	public void setExpiresTime() {
		if (expiresIn != null)
			expiredTime = System.currentTimeMillis() + ((expiresIn - 5) * 1000);
	}
	
	

	public Long getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Long expiredTime) {
		this.expiredTime = expiredTime;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}

	@Override
	public String toString() {
		return "AccessToken [accessToken=" + accessToken + ", expiresIn=" + expiresIn + ", expiredTime=" + expiredTime
				+ "]";
	}

}

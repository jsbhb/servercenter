package com.zm.pay.pojo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.github.wxpay.sdk.WXPayConfig;

public class WeixinPayConfig implements WXPayConfig {

	private byte[] certData;

	private String certPath;

	private String appID;

	private String mchID;

	private String key;

	private Integer httpConnectTimeoutMs;

	private Integer httpReadTimeoutMs;


	public InputStream getCertStream() {
		try {
			if (certPath != null) {
				File file = new File(certPath);
				InputStream certStream = new FileInputStream(file);
				this.certData = new byte[(int) file.length()];
				certStream.read(this.certData);
				certStream.close();
				ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
				return certBis;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public byte[] getCertData() {
		return certData;
	}


	public void setCertData(byte[] certData) {
		this.certData = certData;
	}


	public String getCertPath() {
		return certPath;
	}


	public void setCertPath(String certPath) {
		this.certPath = certPath;
	}


	@Override
	public String getAppID() {
		return appID;
	}


	public void setAppID(String appID) {
		this.appID = appID;
	}
	
	@Override
	public String getMchID() {
		return mchID;
	}

	public void setMchID(String mchID) {
		this.mchID = mchID;
	}

	@Override
	public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public int getHttpConnectTimeoutMs() {
		return httpConnectTimeoutMs;
	}


	public void setHttpConnectTimeoutMs(Integer httpConnectTimeoutMs) {
		this.httpConnectTimeoutMs = httpConnectTimeoutMs;
	}

	@Override
	public int getHttpReadTimeoutMs() {
		return httpReadTimeoutMs;
	}


	public void setHttpReadTimeoutMs(Integer httpReadTimeoutMs) {
		this.httpReadTimeoutMs = httpReadTimeoutMs;
	}

}

package com.zm.supplier.custominf.model;
/**
 * @fun 海关对接配置信息
 * @author user
 *
 */
public class CustomConfig {
	
	private String targetObject;
	
	private int customId;

	private String url;
	// 备案编号
	private String companyCode;
	// 备案名称
	private String companyName;
	// 电商企业编码
	private String eCommerceCode;
	// 电商企业名称
	private String eCommerceName;
	//海关RSA 公钥（其他海关加密方式可能不一样，也用这个字段）用来解密签名
	private String customPublicKey;
	//RSA 私钥（其他海关加密方式可能不一样，也用这个字段）
	private String privateKey;
	//RSA 公钥（企业公钥） 只是暂存，防止丢失，主要给对方解密用
	private String publicKey;
	//aes秘钥
	private String aesKey;
	//海关AES秘钥
	private String customAesKey;
	//加签传输ID
	private String dxPid;
	//总署推送加签报文url
	private String zsurl;
	public String getZsurl() {
		return zsurl;
	}
	public void setZsurl(String zsurl) {
		this.zsurl = zsurl;
	}
	public String getDxPid() {
		return dxPid;
	}
	public void setDxPid(String dxPid) {
		this.dxPid = dxPid;
	}
	public int getCustomId() {
		return customId;
	}
	public void setCustomId(int customId) {
		this.customId = customId;
	}
	public String getTargetObject() {
		return targetObject;
	}
	public void setTargetObject(String targetObject) {
		this.targetObject = targetObject;
	}
	public String getAesKey() {
		return aesKey;
	}
	public void setAesKey(String aesKey) {
		this.aesKey = aesKey;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String geteCommerceCode() {
		return eCommerceCode;
	}
	public void seteCommerceCode(String eCommerceCode) {
		this.eCommerceCode = eCommerceCode;
	}
	public String geteCommerceName() {
		return eCommerceName;
	}
	public void seteCommerceName(String eCommerceName) {
		this.eCommerceName = eCommerceName;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	public String getCustomPublicKey() {
		return customPublicKey;
	}
	public void setCustomPublicKey(String customPublicKey) {
		this.customPublicKey = customPublicKey;
	}
	public String getCustomAesKey() {
		return customAesKey;
	}
	public void setCustomAesKey(String customAesKey) {
		this.customAesKey = customAesKey;
	}
}

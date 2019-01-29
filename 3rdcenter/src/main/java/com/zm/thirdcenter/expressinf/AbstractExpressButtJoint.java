package com.zm.thirdcenter.expressinf;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zm.thirdcenter.pojo.ExpressInfoResult;
import com.zm.thirdcenter.pojo.ExpressInterface;
import com.zm.thirdcenter.pojo.OrderInfo;
import com.zm.thirdcenter.utils.JSONUtil;
import com.zm.thirdcenter.utils.XmlUtil;

public abstract class AbstractExpressButtJoint {
	
	protected String appKey;
	protected String appSecret;
	protected String url;
	protected String tradeId;
	protected double version;
	protected String buzType;
	protected String format;
	protected int retryLimit;
	
	private static final String XML = "XML";
	private static final String JSON = "JSON";
	
	protected Logger logger = LoggerFactory.getLogger(AbstractExpressButtJoint.class);
	
	/**
	 * @fun 返回key为obj，value为calss实体类的list集合
	 * @param result
	 * @param format
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public <T> Set<T> renderResult(String result, String format, Class<T> clazz) throws Exception{
		Set<T> set = null;
		if(result != null){
			if(XML.equalsIgnoreCase(format)){
				set = XmlUtil.parseXml(result, clazz);
			}else if(JSON.equalsIgnoreCase(format)){
				set = JSONUtil.toObject(result, clazz);
			}
		}
		if(set == null){
			//失败后处理
		}
		return set;
	}
	
	public void init(ExpressInterface inf){
		this.appKey = inf.getAppKey();
		this.appSecret = inf.getAppSecret();
		this.url = inf.getUrl();
		this.tradeId = inf.getTradeId();
		this.version = inf.getVersion();
		this.buzType = inf.getBuzType();
		this.format = inf.getFormat();
		this.retryLimit = inf.getRetryLimit();
	}
	
	
	/**
	 * @fun 创建运单信息
	 * @param info
	 * @return
	 */
	public abstract Set<ExpressInfoResult> createExpressInfo(List<OrderInfo> infoList);
	
	/**
	 * @fun 修改运单信息
	 * @param info
	 * @return
	 */
	public abstract Set<ExpressInfoResult> updateExpressInfo(List<OrderInfo> infoList);
	
	/**
	 * @fun 取消运单信息
	 * @param info
	 * @return
	 */
	public abstract Set<ExpressInfoResult> deleteExpressInfo(List<OrderInfo> infoList);
	
	/**
	 * @fun 获取运单信息
	 * @param orderId
	 * @return
	 */
	public abstract Set<ExpressInfoResult> retrieveExpressInfo(List<OrderInfo> infoList);

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public double getVersion() {
		return version;
	}

	public void setVersion(double version) {
		this.version = version;
	}

	public String getBuzType() {
		return buzType;
	}

	public void setBuzType(String buzType) {
		this.buzType = buzType;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public int getRetryLimit() {
		return retryLimit;
	}

	public void setRetryLimit(int retryLimit) {
		this.retryLimit = retryLimit;
	}
}

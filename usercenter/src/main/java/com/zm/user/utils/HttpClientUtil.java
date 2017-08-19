package com.zm.user.utils;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**  
 * ClassName: HttpClientUtil <br/>  
 * Function: TODO ADD FUNCTION. <br/>   
 * date: Aug 18, 2017 4:00:11 PM <br/>  
 *  
 * @author wqy  
 * @version   
 * @since JDK 1.7  
 */
@SuppressWarnings("deprecation")
public class HttpClientUtil {
	private final static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	private static PoolingHttpClientConnectionManager connManager = null;
	private static CloseableHttpClient httpclient = null;
	public final static int connectTimeout = 50000;


	static {
		try {
			connManager = new PoolingHttpClientConnectionManager();

			connManager.setMaxTotal(1000);// 设置整个连接池最大连接数 根据自己的场景决定
			connManager.setDefaultMaxPerRoute(connManager.getMaxTotal());
			httpclient = wrapClient(new DefaultHttpClient());
		} catch (Exception e) {
			logger.error("NoSuchAlgorithmException", e);
		}
	}

	public static void closeConnections() {
		if (logger.isInfoEnabled()) {
			logger.info("release start connect count:=" + connManager.getTotalStats().getAvailable());
		}
		// Close expired connections
		connManager.closeExpiredConnections();
		// Optionally, close connections
		// that have been idle longer than readTimeout*2 MILLISECONDS
		connManager.closeIdleConnections(connectTimeout * 2, TimeUnit.MILLISECONDS);

		if (logger.isInfoEnabled()) {
			logger.info("release end connect count:=" + connManager.getTotalStats().getAvailable());
		}

	}

	private static CloseableHttpClient wrapClient(HttpClient base) {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					// TODO Auto-generated method stub

				}

				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					// TODO Auto-generated method stub

				}

			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("https", 443, ssf));
			ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager(registry);
			return new DefaultHttpClient(mgr, base.getParams());
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 默认超时为5S 发送 get请求
	 * 
	 * @param params
	 * @return
	 */
	public static String get(String url, Map<String, String> params) {
		String resultStr = "";
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(connectTimeout)
				.setConnectTimeout(connectTimeout).setConnectionRequestTimeout(connectTimeout).build();

		HttpGet httpGet = null;
		HttpEntity entity = null;
		CloseableHttpResponse response = null;

		try {

			if (params != null && !params.isEmpty()) {
				UrlEncodedFormEntity uefEntity;

				// 创建参数队列
				List<NameValuePair> formParams = new ArrayList<NameValuePair>();
				// 绑定到请求 Entry
				for (Map.Entry<String, String> entry : params.entrySet()) {
					formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				uefEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
				String parms = EntityUtils.toString(uefEntity);

				// 创建get请求
				httpGet = new HttpGet(url + "&" + parms);
			} else {
				// 创建get请求
				httpGet = new HttpGet(url);
			}

			httpGet.setConfig(requestConfig);

			logger.info("executing request uri：" + httpGet.getURI());

			response = httpclient.execute(httpGet);

			// 如果连接状态异常，则直接关闭
			if (response.getStatusLine().getStatusCode() != 200) {
				logger.info("httpclient 访问异常 ");
				httpGet.abort();
				return null;
			}
			entity = response.getEntity();
			if (entity != null) {
				resultStr = EntityUtils.toString(entity, "UTF-8");
				logger.info(" httpClient response string " + resultStr);
			}

		} catch (Exception e) {
			httpGet.abort();
			logger.error("http get error " + e.getMessage());
			return null;
			// 关闭连接,释放资源
		} finally {
			try {
				if (entity != null) {
					EntityUtils.consume(entity);// 关闭
				}
				if (response != null) {
					response.close();
				}
				if (httpGet != null) {
					// 关闭连接,释放资源
					httpGet.releaseConnection();
				}

			} catch (Exception e) {
				logger.error("http get error " + e.getMessage());
			}

		}
		return resultStr;
	}

	public static String post(String url, String jsonStr) {
		String resultStr = "";
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(connectTimeout)
				.setConnectTimeout(connectTimeout).setConnectionRequestTimeout(connectTimeout).build();

		// 创建httppost
		HttpPost httpPost = new HttpPost(url);

		StringEntity stringEntity = new StringEntity(jsonStr, Charsets.UTF_8);
		stringEntity.setContentType("text/plain; charset=UTF-8");
		HttpEntity entity = null;
		CloseableHttpResponse response = null;
		try {
			httpPost.setEntity(stringEntity);
			httpPost.setConfig(requestConfig);
			logger.info("executing request uri：" + httpPost.getURI());
			response = httpclient.execute(httpPost);

			// 如果连接状态异常，则直接关闭
			if (response.getStatusLine().getStatusCode() != 200) {
				logger.info("httpclient 访问异常 ");
				httpPost.abort();
				return null;
			}
			entity = response.getEntity();
			if (entity != null) {
				resultStr = EntityUtils.toString(entity, "UTF-8");
				logger.info(" httpClient response string " + resultStr);
			}

		} catch (Exception e) {
			httpPost.abort();
			logger.error("http post error " + e.getMessage());
			return null;
			// 关闭连接,释放资源
		} finally {
			try {
				if (entity != null) {
					EntityUtils.consume(entity);// 关闭
				}
				if (response != null) {
					response.close();
				}
				if (httpPost != null) {
					// 关闭连接,释放资源
					httpPost.releaseConnection();
				}

			} catch (Exception e) {
				logger.error("http post error " + e.getMessage());
			}

		}
		return resultStr;
	}
}

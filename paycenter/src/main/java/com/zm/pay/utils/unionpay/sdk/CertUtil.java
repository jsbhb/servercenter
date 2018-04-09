/**
 *
 * Licensed Property to China UnionPay Co., Ltd.
 * 
 * (C) Copyright of China UnionPay Co., Ltd. 2010
 *     All Rights Reserved.
 *
 * 
 * Modification History:
 * =============================================================================
 *   Author         Date          Description
 *   ------------ ---------- ---------------------------------------------------
 *   xshu       2014-05-28       证书工具类.
 * =============================================================================
 */
package com.zm.pay.utils.unionpay.sdk;

import static com.zm.pay.utils.unionpay.sdk.SDKConstants.UNIONPAY_CNNAME;
import static com.zm.pay.utils.unionpay.sdk.SDKUtil.isEmpty;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.zm.pay.constants.Constants;
import com.zm.pay.pojo.UnionPayConfig;

/**
 * @ClassName: CertUtil
 * @Description: acpsdk证书工具类，主要用于对证书的加载和使用
 * @date 2016-7-22 下午2:46:20
 *       声明：以下代码只是为了方便接入方测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码，性能，
 *       规范性等方面的保障
 */
public final class CertUtil {
	/** 敏感信息加密公钥证书 */
	private final Map<Integer, X509Certificate> encryptCertMap = new ConcurrentHashMap<Integer, X509Certificate>();
	/** 验签中级证书 */
	private final Map<Integer, X509Certificate> middleCertMap = new ConcurrentHashMap<Integer, X509Certificate>();
	/** 验签根证书 */
	private final Map<Integer, X509Certificate> rootCertMap = new ConcurrentHashMap<Integer, X509Certificate>();
	/** 商户私钥存储Map */
	private final Map<String, KeyStore> keyStoreMap = new ConcurrentHashMap<String, KeyStore>();

	private static CertUtil certUtil = new CertUtil();

	public static CertUtil getInstants() {
		return certUtil;
	}

	/**
	 * 初始化所有证书.
	 */
	private CertUtil() {
		try {
			addProvider();// 向系统添加BC provider
			// initSignCert();//初始化签名私钥证书
			// initMiddleCert();//初始化验签证书的中级证书
			// initRootCert();//初始化验签证书的根证书
			// initEncryptCert();//初始化加密公钥
			// initTrackKey();//构建磁道加密公钥
			// initValidateCertFromDir();//初始化所有的验签证书
		} catch (Exception e) {
			LogUtil.writeErrorLog("init失败。（如果是用对称密钥签名的可无视此异常。）", e);
		}
	}

	/**
	 * 添加签名，验签，加密算法提供者
	 */
	private void addProvider() {
		if (Security.getProvider("BC") == null) {
			LogUtil.writeLog("add BC provider");
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		} else {
			Security.removeProvider("BC"); // 解决eclipse调试时tomcat自动重新加载时，BC存在不明原因异常的问题。
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			LogUtil.writeLog("re-add BC provider");
		}
		printSysInfo();
	}

	/**
	 * 用配置文件acp_sdk.properties中配置的私钥路径和密码 加载签名证书
	 */
	public KeyStore initSignCert(UnionPayConfig config) {
		if (!"01".equals(Constants.SIGN_METHOD)) {
			LogUtil.writeLog("非rsa签名方式，不加载签名证书。");
			return null;
		}
		if (config.getSignCertPath() == null || config.getSignCertPwd() == null || config.getSignCertType() == null) {
			LogUtil.writeErrorLog("WARN: SIGNCERT_PATH 或  SIGNCERT_PWD " + "或 SIGNCERT_TYPE 为空。 停止加载签名证书。");
			return null;
		}
		try {
			KeyStore keyStore = getKeyInfo(config.getSignCertPath(), config.getSignCertPwd(), config.getSignCertType());
			keyStoreMap.put(config.getSignCertPath(), keyStore);
			LogUtil.writeLog("InitSignCert Successful. CertId=[" + getSignCertId(config) + "]");
			return keyStore;
		} catch (IOException e) {
			LogUtil.writeErrorLog("InitSignCert Error", e);
			return null;
		}
	}

	/**
	 * 用配置文件acp_sdk.properties配置路径 加载敏感信息加密证书
	 */
	public void initMiddleCert(UnionPayConfig config) {
		LogUtil.writeLog("加载中级证书==>" + config.getMiddleCertPath());
		if (!isEmpty(config.getMiddleCertPath())) {
			X509Certificate middleCert = initCert(config.getMiddleCertPath());
			middleCertMap.put(config.getCenterId(), middleCert);
			LogUtil.writeLog("Load MiddleCert Successful");
		} else {
			LogUtil.writeLog("WARN: acpsdk.middle.path is empty");
		}
	}

	/**
	 * 用配置文件acp_sdk.properties配置路径 加载敏感信息加密证书
	 */
	public void initRootCert(UnionPayConfig config) {
		LogUtil.writeLog("加载根证书==>" + config.getRootCertPath());
		if (!isEmpty(config.getRootCertPath())) {
			X509Certificate rootCert = initCert(config.getRootCertPath());
			rootCertMap.put(config.getCenterId(), rootCert);
			LogUtil.writeLog("Load RootCert Successful");
		} else {
			LogUtil.writeLog("WARN: acpsdk.rootCert.path is empty");
		}
	}

	/**
	 * 用配置文件acp_sdk.properties配置路径 加载银联公钥上级证书（中级证书）
	 */
	public void initEncryptCert(UnionPayConfig config) {
		LogUtil.writeLog("加载敏感信息加密证书==>" + config.getEncryptCertPath());
		if (!isEmpty(config.getEncryptCertPath())) {
			X509Certificate encryptCert = initCert(config.getEncryptCertPath());
			encryptCertMap.put(config.getCenterId(), encryptCert);
			LogUtil.writeLog("Load EncryptCert Successful");
		} else {
			LogUtil.writeLog("WARN: acpsdk.encryptCert.path is empty");
		}
	}

	/**
	 * 用给定的路径和密码 加载签名证书，并保存到certKeyStoreMap
	 * 
	 * @param certFilePath
	 * @param certPwd
	 */
	public void loadSignCert(String certFilePath, String certPwd) {
		KeyStore keyStore = null;
		try {
			keyStore = getKeyInfo(certFilePath, certPwd, "PKCS12");
			keyStoreMap.put(certFilePath, keyStore);
			LogUtil.writeLog("LoadRsaCert Successful");
		} catch (IOException e) {
			LogUtil.writeErrorLog("LoadRsaCert Error", e);
		}
	}

	/**
	 * 通过证书路径初始化为公钥证书
	 * 
	 * @param path
	 * @return
	 */
	private X509Certificate initCert(String path) {
		X509Certificate encryptCertTemp = null;
		CertificateFactory cf = null;
		InputStream in = null;
		try {
			cf = CertificateFactory.getInstance("X.509", "BC");
			URL url = new URL(path); // 创建URL
			URLConnection urlconn = url.openConnection(); // 试图连接并取得返回状态码
			urlconn.connect();
			HttpURLConnection httpconn = (HttpURLConnection) urlconn;
			int httpResult = httpconn.getResponseCode();
			if (httpResult != HttpURLConnection.HTTP_OK) // 不等于HTTP_OK说明连接不成功
				System.out.print("无法连接到");
			else {
				in = urlconn.getInputStream();
			}
//			in = new FileInputStream(path);
			encryptCertTemp = (X509Certificate) cf.generateCertificate(in);
			// 打印证书加载信息,供测试阶段调试
			LogUtil.writeLog("[" + path + "][CertId=" + encryptCertTemp.getSerialNumber().toString() + "]");
		} catch (CertificateException e) {
			LogUtil.writeErrorLog("InitCert Error", e);
		} catch (FileNotFoundException e) {
			LogUtil.writeErrorLog("InitCert Error File Not Found", e);
		} catch (NoSuchProviderException e) {
			LogUtil.writeErrorLog("LoadVerifyCert Error No BC Provider", e);
		} catch(Exception e){
			LogUtil.writeErrorLog("LoadVerifyCert Error", e);
		}finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					LogUtil.writeErrorLog(e.toString());
				}
			}
		}
		return encryptCertTemp;
	}

	/**
	 * 通过keyStore 获取私钥签名证书PrivateKey对象
	 * 
	 * @return
	 */
	public PrivateKey getSignCertPrivateKey(UnionPayConfig config) {
		try {
			KeyStore keyStore = keyStoreMap.get(config.getSignCertPath());
			if (keyStore == null) {
				keyStore = initSignCert(config);
			}
			Enumeration<String> aliasenum = keyStore.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyAlias, config.getSignCertPwd().toCharArray());
			return privateKey;
		} catch (KeyStoreException e) {
			LogUtil.writeErrorLog("getSignCertPrivateKey Error", e);
			return null;
		} catch (UnrecoverableKeyException e) {
			LogUtil.writeErrorLog("getSignCertPrivateKey Error", e);
			return null;
		} catch (NoSuchAlgorithmException e) {
			LogUtil.writeErrorLog("getSignCertPrivateKey Error", e);
			return null;
		}
	}

	/**
	 * 通过指定路径的私钥证书 获取PrivateKey对象
	 * 
	 * @return
	 */
	public PrivateKey getSignCertPrivateKeyByStoreMap(String certPath, String certPwd) {
		if (!keyStoreMap.containsKey(certPath)) {
			loadSignCert(certPath, certPwd);
		}
		try {
			Enumeration<String> aliasenum = keyStoreMap.get(certPath).aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			PrivateKey privateKey = (PrivateKey) keyStoreMap.get(certPath).getKey(keyAlias, certPwd.toCharArray());
			return privateKey;
		} catch (KeyStoreException e) {
			LogUtil.writeErrorLog("getSignCertPrivateKeyByStoreMap Error", e);
			return null;
		} catch (UnrecoverableKeyException e) {
			LogUtil.writeErrorLog("getSignCertPrivateKeyByStoreMap Error", e);
			return null;
		} catch (NoSuchAlgorithmException e) {
			LogUtil.writeErrorLog("getSignCertPrivateKeyByStoreMap Error", e);
			return null;
		}
	}

	/**
	 * 获取敏感信息加密证书PublicKey
	 * 
	 * @return
	 */
	public PublicKey getEncryptCertPublicKey(UnionPayConfig config) {
		if (encryptCertMap.get(config.getCenterId()) == null) {
			String path = config.getEncryptCertPath();
			if (!isEmpty(path)) {
				X509Certificate encryptCert = initCert(path);
				return encryptCert.getPublicKey();
			} else {
				LogUtil.writeErrorLog("acpsdk.encryptCert.path is empty");
				return null;
			}
		} else {
			return encryptCertMap.get(config.getCenterId()).getPublicKey();
		}
	}

	/**
	 * 获取配置文件acp_sdk.properties中配置的签名私钥证书certId
	 * 
	 * @return 证书的物理编号
	 */
	public String getSignCertId(UnionPayConfig config) {
		try {
			KeyStore keyStore = keyStoreMap.get(config.getSignCertPath());
			if (keyStore == null) {
				keyStore = initSignCert(config);
			}
			Enumeration<String> aliasenum = keyStore.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			X509Certificate cert = (X509Certificate) keyStore.getCertificate(keyAlias);
			return cert.getSerialNumber().toString();
		} catch (Exception e) {
			LogUtil.writeErrorLog("getSignCertId Error", e);
			return null;
		}
	}

	/**
	 * 获取敏感信息加密证书的certId
	 * 
	 * @return
	 */
	public String getEncryptCertId(UnionPayConfig config) {
		if (encryptCertMap.get(config.getCenterId()) == null) {
			String path = config.getEncryptCertPath();
			if (!isEmpty(path)) {
				X509Certificate encryptCert = initCert(path);
				return encryptCert.getSerialNumber().toString();
			} else {
				LogUtil.writeErrorLog("acpsdk.encryptCert.path is empty");
				return null;
			}
		} else {
			return encryptCertMap.get(config.getCenterId()).getSerialNumber().toString();
		}
	}

	/**
	 * 将签名私钥证书文件读取为证书存储对象
	 * 
	 * @param pfxkeyfile
	 *            证书文件名
	 * @param keypwd
	 *            证书密码
	 * @param type
	 *            证书类型
	 * @return 证书对象
	 * @throws IOException
	 */
	private static KeyStore getKeyInfo(String pfxkeyfile, String keypwd, String type) throws IOException {
		LogUtil.writeLog("加载签名证书==>" + pfxkeyfile);
		InputStream fis = null;
		try {
			KeyStore ks = KeyStore.getInstance(type, "BC");
			LogUtil.writeLog("Load RSA CertPath=[" + pfxkeyfile + "],Pwd=[" + keypwd + "],type=[" + type + "]");
			URL url = new URL(pfxkeyfile); // 创建URL
			URLConnection urlconn = url.openConnection(); // 试图连接并取得返回状态码
			urlconn.connect();
			HttpURLConnection httpconn = (HttpURLConnection) urlconn;
			int httpResult = httpconn.getResponseCode();
			if (httpResult != HttpURLConnection.HTTP_OK) // 不等于HTTP_OK说明连接不成功
				System.out.print("无法连接到");
			else {
				fis = urlconn.getInputStream();
			}
			// fis = new FileInputStream(pfxkeyfile);
			char[] nPassword = null;
			nPassword = null == keypwd || "".equals(keypwd.trim()) ? null : keypwd.toCharArray();
			if (null != ks) {
				ks.load(fis, nPassword);
			}
			return ks;
		} catch (Exception e) {
			LogUtil.writeErrorLog("getKeyInfo Error", e);
			return null;
		} finally {
			if (null != fis)
				fis.close();
		}
	}

	/**
	 * 通过签名私钥证书路径，密码获取私钥证书certId
	 * 
	 * @param certPath
	 * @param certPwd
	 * @return
	 */
	public String getCertIdByKeyStoreMap(String certPath, String certPwd) {
		if (!keyStoreMap.containsKey(certPath)) {
			// 缓存中未查询到,则加载RSA证书
			loadSignCert(certPath, certPwd);
		}
		return getCertIdIdByStore(keyStoreMap.get(certPath));
	}

	/**
	 * 通过keystore获取私钥证书的certId值
	 * 
	 * @param keyStore
	 * @return
	 */
	private static String getCertIdIdByStore(KeyStore keyStore) {
		Enumeration<String> aliasenum = null;
		try {
			aliasenum = keyStore.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			X509Certificate cert = (X509Certificate) keyStore.getCertificate(keyAlias);
			return cert.getSerialNumber().toString();
		} catch (KeyStoreException e) {
			LogUtil.writeErrorLog("getCertIdIdByStore Error", e);
			return null;
		}
	}

	/**
	 * 将字符串转换为X509Certificate对象.
	 * 
	 * @param x509CertString
	 * @return
	 */
	public X509Certificate genCertificateByStr(String x509CertString) {
		X509Certificate x509Cert = null;
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
			InputStream tIn = new ByteArrayInputStream(x509CertString.getBytes("ISO-8859-1"));
			x509Cert = (X509Certificate) cf.generateCertificate(tIn);
		} catch (Exception e) {
			LogUtil.writeErrorLog("gen certificate error", e);
		}
		return x509Cert;
	}

	/**
	 * 从配置文件acp_sdk.properties中获取验签公钥使用的中级证书
	 * 
	 * @return
	 */
	public X509Certificate getMiddleCert(UnionPayConfig config) {
		if (middleCertMap.get(config.getCenterId()) == null) {
			String path = config.getMiddleCertPath();
			if (!isEmpty(path)) {
				initMiddleCert(config);
			} else {
				LogUtil.writeErrorLog("MIDDLECERT_PATH not set in config");
				return null;
			}
		}
		return middleCertMap.get(config.getCenterId());
	}

	/**
	 * 从配置文件acp_sdk.properties中获取验签公钥使用的根证书
	 * 
	 * @return
	 */
	public X509Certificate getRootCert(UnionPayConfig config) {
		if (rootCertMap.get(config.getCenterId()) == null) {
			String path = config.getRootCertPath();
			if (!isEmpty(path)) {
				initRootCert(config);
			} else {
				LogUtil.writeErrorLog("SDK_ROOTCERT_PATH not set in config");
				return null;
			}
		}
		return rootCertMap.get(config.getCenterId());
	}

	/**
	 * 获取证书的CN
	 * 
	 * @param aCert
	 * @return
	 */
	private static String getIdentitiesFromCertficate(X509Certificate aCert) {
		String tDN = aCert.getSubjectDN().toString();
		String tPart = "";
		if ((tDN != null)) {
			String tSplitStr[] = tDN.substring(tDN.indexOf("CN=")).split("@");
			if (tSplitStr != null && tSplitStr.length > 2 && tSplitStr[2] != null)
				tPart = tSplitStr[2];
		}
		return tPart;
	}

	/**
	 * 验证书链。
	 * 
	 * @param cert
	 * @return
	 */
	private boolean verifyCertificateChain(X509Certificate cert, UnionPayConfig config) {

		if (null == cert) {
			LogUtil.writeErrorLog("cert must Not null");
			return false;
		}

		X509Certificate middleCert = getMiddleCert(config);
		if (null == middleCert) {
			LogUtil.writeErrorLog("middleCert must Not null");
			return false;
		}

		X509Certificate rootCert = getRootCert(config);
		if (null == rootCert) {
			LogUtil.writeErrorLog("rootCert or cert must Not null");
			return false;
		}

		try {

			X509CertSelector selector = new X509CertSelector();
			selector.setCertificate(cert);

			Set<TrustAnchor> trustAnchors = new HashSet<TrustAnchor>();
			trustAnchors.add(new TrustAnchor(rootCert, null));
			PKIXBuilderParameters pkixParams = new PKIXBuilderParameters(trustAnchors, selector);

			Set<X509Certificate> intermediateCerts = new HashSet<X509Certificate>();
			intermediateCerts.add(rootCert);
			intermediateCerts.add(middleCert);
			intermediateCerts.add(cert);

			pkixParams.setRevocationEnabled(false);

			CertStore intermediateCertStore = CertStore.getInstance("Collection",
					new CollectionCertStoreParameters(intermediateCerts), "BC");
			pkixParams.addCertStore(intermediateCertStore);

			CertPathBuilder builder = CertPathBuilder.getInstance("PKIX", "BC");

			@SuppressWarnings("unused")
			PKIXCertPathBuilderResult result = (PKIXCertPathBuilderResult) builder.build(pkixParams);
			LogUtil.writeLog("verify certificate chain succeed.");
			return true;
		} catch (java.security.cert.CertPathBuilderException e) {
			LogUtil.writeErrorLog("verify certificate chain fail.", e);
		} catch (Exception e) {
			LogUtil.writeErrorLog("verify certificate chain exception: ", e);
		}
		return false;
	}

	/**
	 * 检查证书链
	 * 
	 * @param rootCerts
	 *            根证书
	 * @param cert
	 *            待验证的证书
	 * @return
	 */
	public boolean verifyCertificate(X509Certificate cert, UnionPayConfig config) {

		if (null == cert) {
			LogUtil.writeErrorLog("cert must Not null");
			return false;
		}
		try {
			cert.checkValidity();// 验证有效期
			// cert.verify(middleCert.getPublicKey());
			if (!verifyCertificateChain(cert, config)) {
				return false;
			}
		} catch (Exception e) {
			LogUtil.writeErrorLog("verifyCertificate fail", e);
			return false;
		}

		if (Constants.IF_VALIDATE_CNNAME) {
			// 验证公钥是否属于银联
			if (!UNIONPAY_CNNAME.equals(CertUtil.getIdentitiesFromCertficate(cert))) {
				LogUtil.writeErrorLog("cer owner is not CUP:" + CertUtil.getIdentitiesFromCertficate(cert));
				return false;
			}
		} else {
			// 验证公钥是否属于银联
			if (!UNIONPAY_CNNAME.equals(CertUtil.getIdentitiesFromCertficate(cert))
					&& !"00040000:SIGN".equals(CertUtil.getIdentitiesFromCertficate(cert))) {
				LogUtil.writeErrorLog("cer owner is not CUP:" + CertUtil.getIdentitiesFromCertficate(cert));
				return false;
			}
		}
		return true;
	}

	/**
	 * 打印系统环境信息
	 */
	private static void printSysInfo() {
		LogUtil.writeLog("================= SYS INFO begin====================");
		LogUtil.writeLog("os_name:" + System.getProperty("os.name"));
		LogUtil.writeLog("os_arch:" + System.getProperty("os.arch"));
		LogUtil.writeLog("os_version:" + System.getProperty("os.version"));
		LogUtil.writeLog("java_vm_specification_version:" + System.getProperty("java.vm.specification.version"));
		LogUtil.writeLog("java_vm_specification_vendor:" + System.getProperty("java.vm.specification.vendor"));
		LogUtil.writeLog("java_vm_specification_name:" + System.getProperty("java.vm.specification.name"));
		LogUtil.writeLog("java_vm_version:" + System.getProperty("java.vm.version"));
		LogUtil.writeLog("java_vm_name:" + System.getProperty("java.vm.name"));
		LogUtil.writeLog("java.version:" + System.getProperty("java.version"));
		LogUtil.writeLog("java.vm.vendor=[" + System.getProperty("java.vm.vendor") + "]");
		LogUtil.writeLog("java.version=[" + System.getProperty("java.version") + "]");
		printProviders();
		LogUtil.writeLog("================= SYS INFO end=====================");
	}

	/**
	 * 打jre中印算法提供者列表
	 */
	private static void printProviders() {
		LogUtil.writeLog("Providers List:");
		Provider[] providers = Security.getProviders();
		for (int i = 0; i < providers.length; i++) {
			LogUtil.writeLog(i + 1 + "." + providers[i].getName());
		}
	}

	/**
	 * 证书文件过滤器
	 * 
	 */
	static class CerFilter implements FilenameFilter {
		public boolean isCer(String name) {
			if (name.toLowerCase().endsWith(".cer")) {
				return true;
			} else {
				return false;
			}
		}

		public boolean accept(File dir, String name) {
			return isCer(name);
		}
	}

}

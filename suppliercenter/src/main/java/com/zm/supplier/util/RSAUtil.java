/******************************************************************************
  *Copyright (c) 2006-2013 ZheJiang Electronic Port, Inc.
  * All rights reserved.
  * 
  * 项目名称：EPLINK
  * 版权说明：本软件属浙江电子口岸有限公司所有，在未获得浙江电子口岸有限公司正式授权
  *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
  *           识产权保护的内容。
  *****************************************************************************/
package com.zm.supplier.util;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

/**
 * RSA加密工具类
 * @author <a href="mailto:zengdan@zjport.gov.cn">zengdan</a>
 * @version $Id$   
 * @since 1.0
 */
public class RSAUtil {
	/**
	 * 数字签名
	 * 密钥算法
	 */
	public static final String KEY_ALGORITHM = "RSA";

	/**
	 * 数字签名
	 * 签名/验证算法
	 */
	public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";

	/**
	 * 公钥
	 */
	private static final String PUBLIC_KEY = "RSAPublicKey";

	/**
	 * 私钥
	 */
	private static final String PRIVATE_KEY = "RSAPrivateKey";

	/**
	 * RSA密钥长度 默认1024位，
	 *  密钥长度必须是64的倍数， 
	 *  范围在512至65536位之间。
	 */
	private static final int KEY_SIZE = 512;

	/**
	 * 签名
	 * 
	 * @param data
	 *            待签名数据
	 * @param privateKey
	 *            私钥
	 * @return byte[] 数字签名
	 * @throws Exception
	 */
	public static byte[] sign(byte[] data, byte[] privateKey) throws Exception {

		// 转换私钥材料
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);

		// 实例化密钥工厂
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

		// 取私钥匙对象
		PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

		// 实例化Signature
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);

		// 初始化Signature
		signature.initSign(priKey);

		// 更新
		signature.update(data);

		// 签名
		return signature.sign();
	}

	/**
	 * 校验
	 * 
	 * @param data
	 *            待校验数据
	 * @param publicKey
	 *            公钥
	 * @param sign
	 *            数字签名
	 * 
	 * @return boolean 校验成功返回true 失败返回false
	 * @throws Exception
	 * 
	 */
	public static boolean verify(byte[] data, byte[] publicKey, byte[] sign)
			throws Exception {

		// 转换公钥材料
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);

		// 实例化密钥工厂
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

		// 生成公钥
		PublicKey pubKey = keyFactory.generatePublic(keySpec);

		// 实例化Signature
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);

		// 初始化Signature
		signature.initVerify(pubKey);

		// 更新
		signature.update(data);

		// 验证
		return signature.verify(sign);
	}

	/**
	 * 取得私钥
	 * 
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
	public static byte[] getPrivateKey(Map<String, Object> keyMap)
			throws Exception {

		Key key = (Key) keyMap.get(PRIVATE_KEY);

		return key.getEncoded();
	}

	/**
	 * 取得公钥
	 * 
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
	public static byte[] getPublicKey(Map<String, Object> keyMap)
			throws Exception {

		Key key = (Key) keyMap.get(PUBLIC_KEY);

		return key.getEncoded();
	}

	/**
	 * 初始化密钥
	 * 
	 * @return Map 密钥对儿 Map
	 * @throws Exception
	 */
	public static Map<String, Object> initKey() throws Exception {

		// 实例化密钥对儿生成器
		KeyPairGenerator keyPairGen = KeyPairGenerator
				.getInstance(KEY_ALGORITHM);

		// 初始化密钥对儿生成器
		keyPairGen.initialize(KEY_SIZE);

		// 生成密钥对儿
		KeyPair keyPair = keyPairGen.generateKeyPair();

		// 公钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

		// 私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

		// 封装密钥
		Map<String, Object> keyMap = new HashMap<String, Object>(2);

		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);

		return keyMap;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException, Exception {
		String content = "axKAKwRMq3ByTzDS32nsHTkSdM1dxwOyhOP%2FWUpGO3vwozAS5kNhD8W%2BjiBpVdODuzz1WeoyPUwJPcYVGUsbSzVU4codModBF%2F5uQb6z6jpvae1Z67M6MppJyg88J%2BeE%2BDsrSLPUDn5Y8r5aWT3fE2QPVhtQwDezDN8Zoe942PZvIzTI89Fg2e5Az5mmu50fTOpk%2BUwM1rBouk0oNNhJlwZCNeQbAipqdrFw%2Fj1MEOssP46AqDan19fwhhz7LYI0PFGZuYlTa%2F5Z7D%2Bu%2FW1ap%2Bc0Clzgm3mgLeGBhgVjrrOJFIOfBwr7dzUsmgH5i6LFP06%2FH%2BUsEpIeeunmSCkxK8Ht83YhC51e%2BK3vcQengtl%2BmUzA%2Fa%2FX3U4h0qE7FTv%2F5FNV1vP8vcZMS%2FHpersUGVYK6SUDz8x5PKbMTx60mxKjLApUa3YsNRB%2FCxE%2Bvp6ukII%2FYBQqqGgn0ywJ%2FJGU2j9lKYHYWKGiw2uaKyVs2aOTDYNPy5HDWCzkMfBFhJZDYhnA42zre5DJCWsUlIy8qd10GEf13O2akDtS1aBHIze%2Bi5rk5dFYl2LbL%2FgR3sAv7VZOwTAtMFpkgjZU9gTz7xYuhYdA%2FzkcEX8m5qJQZ4WQ00O573CI8bdeByjaEdaEq2nv%2FxBb8cZgqdM6T1OtejEXk5TqbFaFb%2FhiTPwnsu%2BNan1A0sy7LLhuNv%2BgU6rTikj%2Fo70bvyMenvUaVH7ZQJwDGXloIkkJRuXRySrYOV0bWkgKIW5yTAylToBLBG7E%2FyL%2Bfq3Q4ZXmLFuTSbT4dEUDEMRL0%2Fwm86aNL5i3AEMQyE27nP8EbFaC%2BxtH4PZpJL%2BV8%2F%2BuovsPTdkchUSB2%2F1roQ96EJqoxUrQ0PlhetXp5jftnHCMWCE0MK9SPk2zds0K3AS5xPf%2BYfFSa46iM37nSqLo4gAc907hEnKkLUyeh%2BsMDL78y%2BGhyhgAhv0%2BcPVCbfb2SZbFN1XWC4%2FLIw50z9oBPu6hkb%2BAlCA3YGkHIvA3dG6U%2FHdlKJIJ%2FZ3m7SRpE%2B9ifTUGa%2F8Av0ULojDBfGwDjSSC80U3jAuSQuIqh1LU1Tco%2F04b5MoaANb%2FEH%2BfoMAAre%2B8wDQYbBkk5dtXDc%2FRbTw8VzGmFYe9N1yDtaZrN6ZRpxST3MH8eNn%2BwOOhN7%2Fl4LsrBuucscvFFC7s%2BPKBQUsYivOMric8SOREyJyq0LoqwJmAsV7XRbhZo3b%2B6Dnd0IN7pafie%2BhL%2Bg0RiqcZy17EGc54H9EmCNxGjoSR1%2FjytvT0EdTZz0x8BWVH8f2W6q%2BsjmPhl050Ar9FahUXwHdMRP%2BToZPT%2Fohd8mOvSg5n7AaPOAyHLPUbSY7OwruPEofmJdNlHNwK%2BbyWaQlldf%2BVxGRjazyFjbPF3bWru%2Bp0Kg1XcrigehU%2B%2BNurhXu%2BZ2U5Ee4%2BJH1B3%2B1xWMFg7%2BVZEzG84nBK7GwfXWrPm1WcPmq2TYIw%2FcGamft4NpgjHurQCaOavqkyMrJ5BWHBzmZkzOUCtxpGrdZYVDZA%2FPnF5iw2zPeDOlJLrRQU6JnnEr4sF0wX5Px%2FWhMabX5RAtkrarYUE0Uccr8TwgXCnanPSBMcTYEjn3s%2FWn6gVAzZPubrdDJBkq7v%2BJexVJ2kBV1JitzYY78iMpMbblgAqNb%2FQH4pKxiP8%2FImOjtatiXo61oiANQMElJuI70dreNLkKA61XlORygfPN8HN65CYsfUbWLg5LsD0oLHhe00Bo5nbKcCPkl024K2yY07Dp%2BBgF9dwU0y3tHSrr3ZKoz%2FPLPw%2BUg0rQZ9R1zshLph7Lh%2FHPmyQ9gsUlpszAyVaWaUPOtvmitWGy4XYo9nsrxuijNMmM8bpmXAZNFkJwPTYsD0JViwqxbAWZ5mPQUEk0Zfg0fsjR7YNNpW3WJpHyF4Nx9yohZWIajk625RVaBc67lhAU2o3268X73fvZ1xyaJuMTwyWNfyIflGtDjujDnfGdjgxgwEabFU4vBj%2Blr8VOs798VWRI9ks%2B3eGef1XbT4tAX%2FCzR8%2Fi3iGcLnj3M7lfF%2FWryw3mqooGtstNq6ovYmIw0bG%2FTFlFFoG9VmafjJjFCk5btDaiOABe%2F8okx0Z243clMPOh2pKXcInBWfFwHC90WzCzgLpBUft1DHi5HSnJsdi8JDkJVHbBAyHULd8qVnbij5INN1JM2ELSUIt%2BAmOr16tfF7AARgMVv5FtWWfu6M6rWSccJKCTcWcAHoUtMNHX4%2B0YqXzaLEZSb1%2FHZli3RekZVwCZ7pUKvawNgLpu1CgLKrCuPmOBLHNCXOAP%2F3YPHrOYakBmr3KIe%2BDd7RvxgDbtPjDFtINRTMa71bbmg%2Fi8an3pUJs2zWFAr0nWLfLV5WD7%2F2tR3PAxqkwpBcSJHw4lp%2BDzxCdNucQW3dM8wKS7UDVOpxx8EMXUtqHf522wOtM%2BDKl0Ras7L23EKHziR%2B5wRbNv6sq4H5uxaulPw8wf%2BuE%2BUpQ7MVHnGfdvWK%2FBU%2FrkW3xP5YCZB6UDFWTKqHKMGvHKFEulXfYEBrAp8%2B13JPc8kKWYboM5tv5IxuS9Zrn4kBezRc2VUvLgJGERpfpoXK2oew8eQvEoEhu1jHobvY3cbCop767xTdKNDehYK0NhobdFDEe4kBU7bwYKdaTBRZg7rJMoKIsQHKweFFFZp7DX9O8N7EtfPR%2FMfTaQTuV5DuxdTnFMXioBbvxnHGv6RZTtMsxIrHyxOUpZPVq8XHCl64F2%2BJweJdxJNa5s3nlZR7pZbEXUZrWA1FQbKdGVd%2BRKCMSbVJ%2FlFDU9rL%2FnTCH5Bui4nuKgaZAolaTRU9Ofts4IavbGvy3Ky8C2sCfjfZIdrsAyhgk1yCcqJs7twmCV1%2BkMYnhs6kOF3kcDiZTa1%2F%2FapmJqJ4%2F431c9mCkT7KlZU1hF2xDZYX2S9%2BO9OJcylgUtGyH1JRJXYQQLOd4o6AWuZMUy6fbM7GJBhKuUGnji7sslbOCgSTQUnjJoodqRystKNPZA%2Bk6WRELeyME5%2Bqomn%2BLJTalkdUUiLf5raCFMEJyKrZ1vgfcdUDZg5K5qm3Ollxajyr72SeIKwLFxdkmadteFvevjmZR1JHCJHdyD5AtCwWNd7668AtX4%2By9pruPTebWCQ3ymMLsYKkzR1uqK0mFpqUojbarS705xSSdc88v7xxfSbWoTlRU21gQjDVh37dp7kk2yeLEvL1CCxqrjUdCMlTPNoG9yxO94U8D5nljwyl1sttJLZm0CLm9i5gVrUvSbPBUizSJdPkHXiqtMZf";
		String key = "BT1u9bdyWtJmLlpBva8NLQ==";
		String inputContent = "<mo version=\"1.0.0\"><head><businessType>IMPORTORDER</businessType></head><body><orderInfoList><orderInfo><jkfSign><companyCode>3302462946</companyCode><businessNo>GX1001001212110411</businessNo><businessType>IMPORTORDER</businessType><declareType>1</declareType><cebFlag>03</cebFlag></jkfSign><jkfOrderImportHead><eCommerceCode>3302462946</eCommerceCode><eCommerceName>宁波鑫海通达跨境电子商务有限公司</eCommerceName><ieFlag>I</ieFlag><payType>03</payType><payCompanyCode>440316T004</payCompanyCode><payNumber>530121197008214197</payNumber><orderTotalAmount>0.02</orderTotalAmount><orderNo>GX1001001212110411</orderNo><orderTaxAmount>0.01</orderTaxAmount><orderGoodsAmount>0.02</orderGoodsAmount><feeAmount>0</feeAmount><insureAmount>0</insureAmount><companyName>宁波鑫海通达跨境电子商务有限公司</companyName><companyCode>3302462946</companyCode><tradeTime>null</tradeTime><currCode>142</currCode><totalAmount>0.02</totalAmount><consigneeTel>18949518599</consigneeTel><consignee>李政</consignee><consigneeAddress>山东济宁市南山区南山科技园海天一路4栋</consigneeAddress><totalCount>null</totalCount><senderCountry>142</senderCountry><senderName>中国供销海外购</senderName><purchaserId>18949518599</purchaserId><logisCompanyName>中通速递</logisCompanyName><logisCompanyCode>123456</logisCompanyCode><zipCode>273100</zipCode><rate>1</rate><discount>null</discount><userProcotol>本人承诺所购买商品系个人合理自用，现委托商家代理申报、代缴税款等通关事宜，本人保证遵守《海关法》和国家相关法律法规，保证所提供的身份信息和收货信息真实完整，无侵犯他人权益的行为，以上委托关系系如实填写，本人愿意接受海关、检验检疫机构及其他监管部门的监管，并承担相应法律责任。</userProcotol></jkfOrderImportHead><jkfOrderDetailList><jkfOrderDetail><goodsOrder>01</goodsOrder><goodsName>ceshi</goodsName><goodsModel>ceshi,ceshi,null</goodsModel><codeTs>94233803</codeTs><unitPrice>0.01</unitPrice><goodsUnit>件</goodsUnit><goodsCount>2</goodsCount><originCountry>328</originCountry><currency>142</currency></jkfOrderDetail></jkfOrderDetailList><jkfGoodsPurchaser><id>18949518599</id><name>李政</name><telNumber>18949518599</telNumber><paperType>01</paperType><paperNumber>530121197008214197</paperNumber></jkfGoodsPurchaser></orderInfo></orderInfoList></body></mo>";
		byte[] aesKeyCode = Base64.decodeBase64(key.getBytes("utf-8"));
		// 报文加密加密
		String encData = new String(Base64.encodeBase64(AESUtil.encrypt(inputContent.getBytes("utf-8"), aesKeyCode)), "utf-8");
		System.out.println(encData);
		byte[] input_content = Base64.decodeBase64(encData.getBytes("utf-8"));
		byte[] aes_key = Base64.decodeBase64(key.getBytes("utf-8"));
		String original_content =new String(AESUtil.decrypt(input_content, aes_key),"utf-8"); 
		System.out.println(original_content);

	}
}

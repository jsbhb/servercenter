package com.zm.supplier.custominf.impl;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Component;

import com.zm.supplier.bussiness.component.CustomsAddSignatureComponent;
import com.zm.supplier.custominf.AbstractCustom;
import com.zm.supplier.log.LogUtil;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.OrderStatus;
import com.zm.supplier.pojo.bo.SupplierResponseEnum;
import com.zm.supplier.pojo.callback.ReceiveLogisticsCompany;
import com.zm.supplier.util.AESUtil;
import com.zm.supplier.util.ButtJointMessageUtils;
import com.zm.supplier.util.DateUtil;
import com.zm.supplier.util.HttpClientUtil;
import com.zm.supplier.util.RSAUtil;
import com.zm.supplier.util.XmlUtil;

/**
 * @fun 杭州海关
 * @author user
 *
 */
@Component
public class HangZhouCustomImpl extends AbstractCustom {

	@Resource
	CustomsAddSignatureComponent customsAddSignatureComponent;

	private static final String WEBSERVICE_XML_NAMESPACE = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.newyork.zjport.gov.cn/\">";
	private static final String WEBSERVICE_XML_HEAD = "<soapenv:Header/><soapenv:Body><ws:receive>";
	private static final String WEBSERVICE_XML_END = "</ws:receive></soapenv:Body></soapenv:Envelope>";
	private static final String SUCCESS_FLAG = "chkMark&gt;1&lt;/chkMark";
	private static final String SUCCESS_FLAG_1 = "chkMark>1</chkMark";
	private static final String TERMINAL_XML_HEAD = "<DxpMsg xmlns=\"http://www.chinaport.gov.cn/dxp\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ver=\"1.0\">";
	private static final String TERMINAL_XML_END = "</DxpMsg>";

	@Override
	public Set<OrderStatus> orderCustom(OrderInfo info, ReceiveLogisticsCompany receiveLogisticsCompany) {
		String result = null;
		Map<String, String> resultMap = null;
		String customData = ButtJointMessageUtils.getHangZhouCustomOrderMsg(info, config, receiveLogisticsCompany);
		byte[] inputContent;
		try {
			inputContent = customData.getBytes("utf-8");
			byte[] aesKeyCode = Base64.decodeBase64(config.getAesKey().getBytes("utf-8"));
			// 报文加密加密
			String encData = new String(Base64.encodeBase64(AESUtil.encrypt(inputContent, aesKeyCode)), "utf-8");
			// 生成数字签名
			byte[] privateKeyCode = Base64.decodeBase64(config.getPrivateKey().getBytes("utf-8"));
			String sign = new String(Base64.encodeBase64(RSAUtil.sign(inputContent, privateKeyCode)), "utf-8");
			// 封装成webservice 定义的xml报文
			StringBuilder sb = new StringBuilder();
			sb.append(WEBSERVICE_XML_NAMESPACE);
			sb.append(WEBSERVICE_XML_HEAD);
			sb.append("<content>" + encData + "</content>");
			sb.append("<msgType>IMPORTORDER</msgType>");
			sb.append("<dataDigest>" + sign + "</dataDigest>");
			sb.append("<sendCode>" + config.getCompanyCode() + "</sendCode>");
			sb.append(WEBSERVICE_XML_END);
			// 封装webservice 报文完成
			result = HttpClientUtil.post(config.getUrl(), sb.toString(), null);
			// 保存回执
			saveResponse(info.getOrderId(), result, SupplierResponseEnum.CUSTOMS);
			// 处理结果并转成map
			resultMap = handleResult(result);
		} catch (DocumentException e1) {
			LogUtil.writeErrorLog("XML转换出错", e1);
			// 包含chkMark = 1说明是成功的，只是xml转换出错，对整个流程不影响，进行加签申报
			if (result.contains(SUCCESS_FLAG) || result.contains(SUCCESS_FLAG_1)) {
				return addSignatureAndZsCustoms(info, receiveLogisticsCompany);
			}
		} catch (Exception e) {// 第一步推送海关出错不进行状态更新
			LogUtil.writeErrorLog("海关申报encodeing/加密签名", e);
			return null;
		}
		String chkMark = resultMap.get("chkMark");
		if (!"1".equals(chkMark)) {
			return builderResultSet(info, resultMap.get("resultInfo"));
		} else if ("1".equals(chkMark)) {
			// 发送成功，调用国际物流加签接口
			return addSignatureAndZsCustoms(info, receiveLogisticsCompany);
		}
		return null;
	}

	/**
	 * @fun 调用国际物流加签并推送总署
	 * @param info
	 * @param receiveLogisticsCompany
	 * @return
	 */
	@Override
	public Set<OrderStatus> addSignatureAndZsCustoms(OrderInfo info, ReceiveLogisticsCompany receiveLogisticsCompany) {
		String result;
		Map<String, String> resultMap;
		String chkMark;
		try {
			String signStr = customsAddSignatureComponent.addSignature(info, receiveLogisticsCompany, config);
			if (signStr == null) {
				return builderResultSet(info, "国际物流加签失败");
			}
			//封装总署终端报文
			StringBuilder sb = new StringBuilder();
			sb.append(TERMINAL_XML_HEAD);
			sb.append("<TransInfo>");
			sb.append("<CopMsgId>"+UUID.randomUUID().toString().toUpperCase()+"</CopMsgId>");
			sb.append("<SenderId>"+config.getDxPid()+"</SenderId>");
			sb.append("<ReceiverIds><ReceiverId>DXPEDCCEB0000002</ReceiverId></ReceiverIds>");
			sb.append("<CreatTime>"+DateUtil.getDateString(new Date(), "YYYY-MM-dd'T'HH:mm:ss")+"</CreatTime>");
			sb.append("<MsgType>CEB311Message</MsgType>");
			sb.append("</TransInfo>");
			sb.append("<Data>"+Base64.encodeBase64String(signStr.getBytes())+"</Data>");
			sb.append(TERMINAL_XML_END);
			//封装总署终端报文END
			// 封装webservice xml报文
			StringBuilder webService = new StringBuilder();
			webService.append(WEBSERVICE_XML_NAMESPACE);
			webService.append(WEBSERVICE_XML_HEAD);
			webService.append("<content><![CDATA[" + sb.toString() + "]]></content>");
			webService.append(WEBSERVICE_XML_END);
			// 封装webservice xml报文结束
			result = HttpClientUtil.post(config.getZsurl(), webService.toString(), null);
			saveResponse(info.getOrderId(), result, SupplierResponseEnum.ZONGSHU_CUSTOMS);
			// 处理结果并转成map
			resultMap = handleResult(result);
			chkMark = resultMap.get("chkMark");
			if (!"1".equals(chkMark)) {// 推送加签报文失败，更新异常状态
				return builderResultSet(info, resultMap.get("resultInfo"));
			} else {
				return null;
			}
		} catch (UnsupportedEncodingException | DocumentException e) {// 国际物流加签失败，更新异常状态
			LogUtil.writeErrorLog("国际物流加签失败", e);
			return builderResultSet(info, "国际物流加签失败");
		}
	}

	/**
	 * @fun 处理返回结果并转成map
	 * @param result
	 * @return
	 * @throws DocumentException
	 */
	private Map<String, String> handleResult(String result) throws DocumentException {
		Map<String, String> resultMap;
		result = StringEscapeUtils.unescapeXml(result);// 把&lt;，&gt;转义成<>
		// 截取xml报文
		result = result.substring(result.indexOf("<?xml"), result.indexOf("</mo>") + 5);
		resultMap = XmlUtil.xmlToMap(result);
		return resultMap;
	}

	/**
	 * @fun 构建失败返回结果
	 * @param info
	 * @param resultInfo
	 * @return
	 */
	private Set<OrderStatus> builderResultSet(OrderInfo info, String resultInfo) {
		OrderStatus status = new OrderStatus();
		status.setOrderId(info.getOrderId());
		status.setThirdOrderId(info.getOrderId());
		status.setSupplierId(info.getSupplierId());
		status.setStatus(resultInfo);
		Set<OrderStatus> set = new HashSet<>();
		set.add(status);
		return set;
	}

//	public static void main(String[] args) throws Exception {
//		HangZhouCustomImpl impl = new HangZhouCustomImpl();
//		CustomConfig config = new CustomConfig();
//		config.setAesKey("BT1u9bdyWtJmLlpBva8NLQ==");
//		config.setCompanyCode("3302462946");
//		config.setCompanyName("宁波鑫海通达跨境电子商务有限公司");
//		config.setDxPid("DXPENT0000021610");
//		config.seteCommerceCode("3302462946");
//		config.seteCommerceName("宁波鑫海通达跨境电子商务有限公司");
//		config.setPrivateKey(
//				"MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAiEVzSCgNRo4VqoIqFDK8rpV/SspNHP6FsOujypqabVkcFWXStAizy1MAdPBw84u+o/vOt5BbRLW9k01mdMIjDQIDAQABAkAoXIGnpo1AD4dlSEZUUy7FeuwH5+FtLAnG/BQ4RxBkQGgf4ivbG4veuBqGnhs1rdb6ZZFvoBuHxE4LVGiRmlC1AiEA0D3RxnEJZP+xJ9M5sv5MHwIpLQViLXG5cCrQ1vUf/bcCIQCnhiY0ha+LbqQjRL2QDVETirWh7kyTgiSRrRYuKwSlWwIgIcbFLdDL20v16iXCqBvDMQxirWDAKerWTzFCqnsb80UCIHJMokaVrAdbTxxNK3Vc0KOfsXuxpofCdQb77LVFXp8fAiBXXrs/8UoVit/pWutFZadDdIbljRBV2htVjbFghxhl4w==");
//		config.setPublicKey(
//				"MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIhFc0goDUaOFaqCKhQyvK6Vf0rKTRz+hbDro8qamm1ZHBVl0rQIs8tTAHTwcPOLvqP7zreQW0S1vZNNZnTCIw0CAwEAAQ==");
//		config.setUrl("http://122.224.230.4:18003/newyorkWS/ws/ReceiveEncryptDeclare?wsdl");
//		config.setZsurl("http://122.224.230.4:18003/newyorkWS/ws/ReceiveCebDeclare?wsdl");
//		String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ceb:CEB311Message xmlns:ceb=\"http://www.chinaport.gov.cn/ceb\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" guid=\"4137E7CB-2CD9-4E7F-8907-36AF1347AED9\" version=\"1.0\"><ceb:Order><ceb:OrderHead><ceb:guid>4137E7CB-2CD9-4E7F-8907-36AF1347AED9</ceb:guid><ceb:appType>1</ceb:appType><ceb:appTime>20190387041959</ceb:appTime><ceb:appStatus>2</ceb:appStatus><ceb:orderType>I</ceb:orderType><ceb:orderNo>GX0190328141214225000</ceb:orderNo><ceb:ebpCode>3302462946</ceb:ebpCode><ceb:ebpName>宁波鑫海通达跨境电子商务有限公司</ceb:ebpName><ceb:ebcCode>3302462946</ceb:ebcCode><ceb:ebcName>宁波鑫海通达跨境电子商务有限公司</ceb:ebcName><ceb:goodsValue>0.01</ceb:goodsValue><ceb:freight>0</ceb:freight><ceb:discount>0</ceb:discount><ceb:taxTotal>0.0</ceb:taxTotal><ceb:acturalPaid>0.01</ceb:acturalPaid><ceb:currency>142</ceb:currency><ceb:buyerRegNo>15957456456</ceb:buyerRegNo><ceb:buyerName>我们</ceb:buyerName><ceb:buyerTelephone>15957456456</ceb:buyerTelephone><ceb:buyerIdType>1</ceb:buyerIdType><ceb:buyerIdNumber>330206199911111112</ceb:buyerIdNumber><ceb:payCode>ZF14021901</ceb:payCode><ceb:payName>支付宝(中国)网络技术有限公司</ceb:payName><ceb:payTransactionId>4200000268201903283170563007</ceb:payTransactionId><ceb:consignee>我们</ceb:consignee><ceb:consigneeTelephone>15957456456</ceb:consigneeTelephone><ceb:consigneeAddress>北京北京东城区我</ceb:consigneeAddress></ceb:OrderHead><ceb:OrderList><ceb:gnum>01</ceb:gnum><ceb:itemNo>100001843</ceb:itemNo><ceb:itemName>普超水果味什锦软糖</ceb:itemName><ceb:gModel>普超水果味什锦软糖,普超,null</ceb:gModel><ceb:unit>件</ceb:unit><ceb:qty>1</ceb:qty><ceb:price>0.01</ceb:price><ceb:totalPrice>0.01</ceb:totalPrice><ceb:currency>142</ceb:currency><ceb:country>142</ceb:country></ceb:OrderList></ceb:Order><ceb:BaseTransfer><ceb:copCode>3302462946</ceb:copCode><ceb:copName>宁波鑫海通达跨境电子商务有限公司</ceb:copName><ceb:dxpMode>DXP</ceb:dxpMode><ceb:dxpId>DXPENT0000021610</ceb:dxpId></ceb:BaseTransfer><Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/><SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/><Reference URI=\"\"><Transforms><Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/><DigestValue>/h0Z6M8Tw81IrrWPJxZqFRvpY+I=</DigestValue></Reference></SignedInfo><SignatureValue>VtsfyeDzLKT7pR2F/BNqFLRN/xcT0cFNibN/OZjqAnWt5fxtcDUuOCmtmeEoczVvkhUqwINDkU2TBtIE5i0SN2/czYxd4PCl6R5dv+7UHyB+dPBQUOfJJrOjsy8uTCLFz97Z/dpiBjUU9w58Usfloh6f9aGnfjiN2cYn6jFAGZe+sdfkwhBb9h55u0YGudKevUVg57SVkgexYvstI1V3ByQiS1FOTHeqWgF0rNeRDdUy1zV1ZdmZAAHzAeD13mHGY8zudQK1GfGwkZwW2TKBL5aia4cqfkz8zpgEp9uJgdSeUvMmstDq17ZaBDE1UgOK1D41vAWJwzDfkZjqFfOpSA==</SignatureValue><KeyInfo><KeyName>0001</KeyName><X509Data><X509Certificate>MIIFVzCCBD+gAwIBAgIIAgEAAAAAIZkwDQYJKoZIhvcNAQEFBQAwgZgxCzAJBgNVBAYTAkNOMQ8wDQYDVQQIDAbljJfkuqwxDzANBgNVBAcMBuWMl+S6rDEbMBkGA1UECgwS5Lit5Zu955S15a2Q5Y+j5bK4MRswGQYDVQQLDBLor4HkuabnrqHnkIbkuK3lv4MxLTArBgNVBAMMJOS4reWbveeUteWtkOS4muWKoeivgeS5pueuoeeQhuS4reW/gzAeFw0xOTAzMjYwMDAwMDBaFw0zOTAzMjYwMDAwMDBaMGwxCzAJBgNVBAYTAkNOMREwDwYDVQQIDAhaaGVKaWFuZzEPMA0GA1UEBwwGTmluZ0JvMTkwNwYDVQQDDDDlroHms6LpkavmtbfpgJrovr7ot6jlooPnlLXlrZDllYbliqHmnInpmZDlhazlj7gwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCGUIBft/Ja+cZ/ptEtgsWOmP5fXX+aIjI7FmZrwdDtc3Wz4RZh6HVjzDdo4vrkkAczNImFja/QOaf9OP5C5JhOcBFxdNGm06a0O9wqzp2c+wqIeqXgyA6O91QkTy3yhMDqxdnGfiVD45mnUgp1t3HQHCa1GxsTjCVvDwLv+VcVvhcgqeg18obTPC/ZT7qf78/gT6whgsCYiS2qTo2Mwl8Aqev0KS0Ob+hVRLcjq9eWaJ98snIQJdJLdMhBLvlexX477XRpr4LiM3JgHsDNVGB8WPNaXp5I6k17paRWu1WT85FciOI2FvHN9TaCuOZkptxAq5IbciB7t6HImd/fLiA9AgMBAAGjggHOMIIByjAOBgNVHQ8BAf8EBAMCA/gwDAYDVR0TAQH/BAIwADAfBgNVHSMEGDAWgBR9d2tWOmBmEHUWSAFM/nMSjRRWGDAdBgNVHQ4EFgQUoAvoWNuPFQZ1/TMtKswvpvzsk7MwRAYJKoZIhvcNAQkPBDcwNTAOBggqhkiG9w0DAgICAIAwDgYIKoZIhvcNAwQCAgCAMAcGBSsOAwIHMAoGCCqGSIb3DQMHMIGBBgNVHR8EejB4MHagdKByhnBsZGFwOi8vbGRhcC5jaGluYXBvcnQuZ292LmNuOjM4OS9jbj1jcmwwMjAxMDAsb3U9Y3JsMDEsb3U9Y3JsLGM9Y24/Y2VydGlmaWNhdGVSZXZvY2F0aW9uTGlzdCwqP2Jhc2U/Y249Y3JsMDIwMTAwMBoGCisGAQQBqUNkBQYEDAwKUzAwMDAwMTI5MTAaBgorBgEEAalDZAUJBAwMClMwMDAwMDEyOTEwEgYKKwYBBAGpQ2QCAQQEDAIxOTASBgorBgEEAalDZAIEBAQMAkNBMB4GCGCGSAGG+EMJBBIMEDAwMDAwMDAwMDAwMDEzNzYwIAYIKoEc0BQEAQQEFBMSOTEzMzAyMDFNQTJBSjc4ODlNMA0GCSqGSIb3DQEBBQUAA4IBAQBv3b6QlEKWq5hbReF006DA/Z4VLO3FsphVMVrKef2hCHD0hG3dwBfjjnZD7Ets/og48VFxpbD6wnCacnH2zmiijKlczmNOXmrKUH9hb8SQq5v6hxd+EbXcAFIXthbMp1pBG3p3adWFxjmqj1vS+ZFEI3vIX+Kq8fkDsaegoyQJLm6Zmtkkm8Xur2b06qVj3GSZnCOm1tOKy+H4zRXwQkEMx5sMVYUZvczaMdQT8NOBxyigHKQDWCKpQiR0Ui4wZLUrliiINeSSJWbwNaCkEsRkLCN+RCdp5wCUsNZIsJcP9eLgo/o480R2xDM+9JaTMSk8EJuEE2vFm0e9xKgT1eu9</X509Certificate></X509Data></KeyInfo></Signature></ceb:CEB311Message>";
//		//封装总署终端报文
//		StringBuilder sb = new StringBuilder();
//		sb.append(TERMINAL_XML_HEAD);
//		sb.append("<TransInfo>");
//		sb.append("<CopMsgId>"+UUID.randomUUID().toString().toUpperCase()+"</CopMsgId>");
//		sb.append("<SenderId>"+config.getDxPid()+"</SenderId>");
//		sb.append("<ReceiverIds><ReceiverId>DXPEDCCEB0000002</ReceiverId></ReceiverIds>");
//		sb.append("<CreatTime>"+DateUtil.getDateString(new Date(), "YYYY-MM-dd'T'HH:mm:ss")+"</CreatTime>");
//		sb.append("<MsgType>CEB311Message</MsgType>");
//		sb.append("</TransInfo>");
//		sb.append("<Data>"+Base64.encodeBase64String(s.getBytes())+"</Data>");
//		sb.append(TERMINAL_XML_END);
//		//封装总署终端报文END
//		// 封装webservice xml报文
//		StringBuilder webService = new StringBuilder();
//		webService.append(WEBSERVICE_XML_NAMESPACE);
//		webService.append(WEBSERVICE_XML_HEAD);
//		webService.append("<content><![CDATA[" + sb.toString() + "]]></content>");
//		webService.append(WEBSERVICE_XML_END);
//		// 封装webservice xml报文结束
//		String result = HttpClientUtil.post(config.getZsurl(), webService.toString(), null);
//		System.out.println(result);
//	}

}

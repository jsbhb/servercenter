package com.zm.supplier.util;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zm.supplier.pojo.SendOrderResult;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

/**
 * Json转换
 *
 * @author L.cm email: 596392912@qq.com site:http://www.dreamlu.net date
 *         2015年5月13日下午4:58:33
 */
public class JSONUtil {

	private static final ObjectMapper objectMapper;

	static {
		objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		objectMapper.configure(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
	}

	public static String toJson(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
		}
	}

	public static <T> T parse(String jsonString, Class<T> type) {
		try {
			return objectMapper.readValue(jsonString, type);
		} catch (Exception e) {
			throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
		}
	}

	/**
	 * @fun json转xml后解析
	 * @param jsonString
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static <T> Set<T> toObject(String jsonString, Class<T> type) throws Exception {

		return XmlUtil.parseXml(json2XML(jsonString), type);
	}
	
	public static String json2XML(String json) {
		JSONObject jobj = JSONObject.fromObject(json);
		String xml = new XMLSerializer().write(jobj);
		System.out.println("转换后XML======" + xml);
		return xml;
	}

	public static void main(String[] args) throws Exception {
		String str = "{\"status\":1,\"error_msg\":\"\u5904\u7406\u6210\u529f!\"}";
		Set<SendOrderResult> map = toObject(str, SendOrderResult.class);
		System.out.println(map);
	}

}

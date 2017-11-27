package com.zm.supplier.util;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zm.supplier.pojo.CheckStockModel;
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

	public static <T> Set<T> toObject(String jsonString, Class<T> type) throws Exception {

		return XmlUtil.parseXml(json2XML(jsonString), type);
	}

	public static String json2XML(String json) {
		json = json.replace("\"[", "[").replace("]\"", "]").replace("\\", "");
		System.out.println("替换后JSON======" + json);
		JSONObject jobj = JSONObject.fromObject(json);
		String xml = new XMLSerializer().write(jobj);
		System.out.println("转换后XML======" + xml);
		return xml;
	}

	public static void main(String[] args) throws Exception {
		String s = "{\"order_no\":\"PA35115100639737719\",\"real_pay_amount\":\"130000\",\"total_freight\":\"1000\",\"sign\":\"6ef40df5c18358a83bdb2dfd962adebe\",\"ret_msg\":\"交易成功！\",\"fbatchIds\":[\"TXQS101003778-MBS07866-B-4\",\"TXQS101003778-MBS04487-B-2\"],\"merchant_order_no\":\"GX10010012121103\",\"order_status\":\"2\",\"total_all_amount\":\"130000\",\"ret_code\":\"000000\",\"items\":[{\"order_status\":\"2\",\"buy_num\":2,\"sku_id\":\"MBS07866-B\",\"freight_amount\":0,\"goods_order\":\"OA35115100639830459\",\"price\":9500},{\"order_status\":\"2\",\"buy_num\":5,\"sku_id\":\"MBS04487-B\",\"freight_amount\":1000,\"goods_order\":\"OA35115100640075349\",\"price\":22000}],\"sign_type\":\"MD5\",\"status\":2}";
		String str = "{\"sign\":\"c3b818c3319fdf0ad2d6fa35b1cf21ee\",\"stock_info\":\"[{\\\"sku_id\\\":\\\"MBS04487-B\\\",\\\"sku_stock_num_show\\\":272},{\\\"sku_id\\\":\\\"MBS07866-B\\\",\\\"sku_stock_num_show\\\":488}]\",\"ret_msg\":\"get_goods_stock查询成功！\",\"ret_code\":\"100002\",\"sign_type\":\"MD5\"}";
		System.out.println(toObject(str, CheckStockModel.class));
	}

}

package com.zm.supplier.util;

import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zm.supplier.pojo.CheckStockModel;
import com.zm.supplier.pojo.OrderStatus;
import com.zm.supplier.pojo.SendOrderResult;
import com.zm.supplier.supplierinf.model.LiangYouOrderStatusTemp;

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
		String s = "{\"ret_msg\":\"\",\"ret_num\":\"0\",\"ret_data\":{\"check_flg\":\"1\",\"check_msg\":\"test\",\"logistics_name\":\"顺丰\",\"logistics_no\":\"12345\",\"status\":\"22\"}}";
		String str = "{\"sign\":\"c3b818c3319fdf0ad2d6fa35b1cf21ee\",\"stock_info\":\"[{\\\"sku_id\\\":\\\"MBS04487-B\\\",\\\"sku_stock_num_show\\\":272},{\\\"sku_id\\\":\\\"MBS07866-B\\\",\\\"sku_stock_num_show\\\":488}]\",\"ret_msg\":\"get_goods_stock查询成功！\",\"ret_code\":\"100002\",\"sign_type\":\"MD5\"}";
		String str1="{\"LogisticsNo\":\"812223195521\",\"LogisticsName\":\"\u5317\u4ed1\u519b\u901a\",\"CheckMsg\":\"\u9884\u6821\u9a8c\u901a\u8fc7\u3002\",\"message\":[{\"Status\":\"24\",\"Result\":\"\u653e\u884c\u6210\u529f\",\"CreateTime\":\"2018-01-24 13:29:59\"},{\"Status\":\"21\",\"Result\":\"\u6d77\u5173\u5df2\u7533\u62a5\",\"CreateTime\":\"2018-01-25 17:25:51\"},{\"Status\":\"11\",\"Result\":\"\u56fd\u68c0\u5df2\u7533\u62a5\",\"CreateTime\":\"2018-01-23 17:24:12\"},{\"Status\":\"03\",\"Result\":\"\u4ed3\u5e93\u51fa\u5e93\u62a5\u68c0\",\"CreateTime\":\"2018-01-23 17:24:09\"},{\"Status\":\"02\",\"Result\":\"\u5df2\u53d1\u4ed3\u5e93\",\"CreateTime\":\"2018-01-23 17:23:36\"}]}";
		LiangYouOrderStatusTemp map = parse(str1, LiangYouOrderStatusTemp.class);
		System.out.println(map);
		map.sortList();
		System.out.println(map);
	}

}

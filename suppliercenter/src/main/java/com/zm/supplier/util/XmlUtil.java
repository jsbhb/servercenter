package com.zm.supplier.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.zm.supplier.pojo.CheckStockModel;

public class XmlUtil {

	public static Map<String, String> xmlToMap(String xml) throws DocumentException {

		Map<String, String> result = new HashMap<String, String>();

		Document doc = DocumentHelper.parseText(xml);

		Element root = doc.getRootElement();
		parseNode(result, root);

		return result;
	}

	@SuppressWarnings("unchecked")
	private static void parseNode(Map<String, String> result, Element node) {
		if (!(node.getTextTrim().equals(""))) {
			Attribute attr = node.attribute("name");
			if (attr == null) {
				result.put(node.getName(), node.getTextTrim());
			} else {
				result.put(attr.getValue() == null ? node.getName() : attr.getValue(), node.getTextTrim());
			}
		}
		// 同时迭代当前节点下面的所有子节点
		// 使用递归
		Iterator<Element> iterator = node.elementIterator();
		while (iterator.hasNext()) {
			Element e = iterator.next();
			parseNode(result, e);
		}
	}

	@SuppressWarnings("unchecked")
	private static boolean parseNode(Element node, Map<String, String> confMap) {
		if (!(node.getTextTrim().equals(""))) {
			String nodeName = node.getName();
			String value = node.getTextTrim();
			String localValue = confMap.get(nodeName) == null ? "" : confMap.get(nodeName);
			String[] strs = localValue.split(",");
			for(String s : strs){
				if (value.equals(s)) {
					return true;
				}
			}
		}
		Iterator<Element> iterator = node.elementIterator();
		while (iterator.hasNext()) {
			Element e = iterator.next();
			boolean flag = parseNode(e, confMap);
			if (flag) {
				return flag;
			}
		}
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> Set<T> parseXml(String xml, Class<T> clazz) throws Exception {
		Document doc = DocumentHelper.parseText(xml);
		Map<String, String> confMap = ConfUtils.getConfMap();
		Element root = doc.getRootElement();
		boolean flag = parseNode(root, confMap);
		if (flag) {
			Object obj = clazz.newInstance();
			Method method = clazz.getMethod("setUniquId", String.class);
			method.invoke(obj, UUID.randomUUID().toString().replaceAll("-", ""));
			Map<String, Object> map = new HashMap<String, Object>();
			parseNode(root, clazz, confMap, map, obj);
			Set set = new HashSet();
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				set.add(entry.getValue());
			}
			return set;
		}
		return null;
	}

	@SuppressWarnings({ "unchecked" })
	private static <T> Object parseNode(Element node, Class<T> clazz, Map<String, String> confMap,
			Map<String, Object> map, Object obj) throws Exception {

		if (!(node.getTextTrim().equals(""))) {
			String nodeName = node.getName();
			String fieldName = confMap.get(nodeName);
			if (fieldName != null) {
				Method getMethod;
				try {
					getMethod = clazz.getMethod("get" + fieldName);
					Object o = getMethod.invoke(obj);

					if (o != null) {
						obj = clazz.newInstance();
						String uniquId = UUID.randomUUID().toString().replaceAll("-", "");;
						Method method = clazz.getMethod("setUniquId", String.class);
						method.invoke(obj, uniquId);
					}
					String value = node.getTextTrim();
					Method method = clazz.getMethod("set" + fieldName, String.class);
					method.invoke(obj, value);
					getMethod = clazz.getMethod("getUniquId");
					String tem = (String) getMethod.invoke(obj);
					if (!map.containsKey(tem)) {
						map.put(tem, obj);
					}
				} catch (NoSuchMethodException e) {

				} catch (SecurityException e) {
					e.printStackTrace();
				}

			}
		}
		Iterator<Element> iterator = node.elementIterator();
		while (iterator.hasNext()) {
			Element e = iterator.next();
			obj = parseNode(e, clazz, confMap, map, obj);
		}
		return obj;
	}

	public static void main(String[] args) throws Exception {
		String xml1 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><o><ret_code type=\"string\">100002</ret_code><ret_msg type=\"string\">get_goods_stock查询成功！</ret_msg><sign type=\"string\">c3b818c3319fdf0ad2d6fa35b1cf21ee</sign><sign_type type=\"string\">MD5</sign_type><stock_info class=\"array\"><e class=\"object\"><sku_id type=\"string\">MBS04487-B</sku_id><sku_stock_num_show type=\"number\">272</sku_stock_num_show></e><e class=\"object\"><sku_id type=\"string\">MBS07866-B</sku_id><sku_stock_num_show type=\"number\">488</sku_stock_num_show></e></stock_info></o>";
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><o><fbatchIds class=\"array\"><e type=\"string\">TXQS101003778-MBS07866-B-4</e><e type=\"string\">TXQS101003778-MBS04487-B-2</e></fbatchIds><items class=\"array\"><e class=\"object\"><buy_num type=\"number\">2</buy_num><freight_amount type=\"number\">0</freight_amount><goods_order type=\"string\">OA35114226249246197</goods_order><order_status type=\"string\">2</order_status><price type=\"number\">9500</price><sku_id type=\"string\">MBS07866-B</sku_id></e><e class=\"object\"><buy_num type=\"number\">5</buy_num><freight_amount type=\"number\">1000</freight_amount><goods_order type=\"string\">OA35114226249338342</goods_order><order_status type=\"string\">2</order_status><price type=\"number\">22000</price><sku_id type=\"string\">MBS04487-B</sku_id></e></items><merchant_order_no type=\"string\">GX10010012121102</merchant_order_no><order_no type=\"string\">PA35114226249117065</order_no><order_status type=\"string\">2</order_status><real_pay_amount type=\"string\">130000</real_pay_amount><ret_code type=\"string\">000000</ret_code><ret_msg type=\"string\">交易成功！</ret_msg><sign type=\"string\">c4b8e9c8f4745ea9aead700566963cc8</sign><sign_type type=\"string\">MD5</sign_type><status type=\"number\">2</status><total_all_amount type=\"string\">130000</total_all_amount><total_freight type=\"string\">1000</total_freight></o>";
		System.out.println(parseXml(xml1, CheckStockModel.class));
	}
}

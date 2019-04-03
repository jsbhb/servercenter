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

import com.zm.supplier.pojo.OrderStatus;

public class XmlUtil {

	public static Map<String, String> xmlToMap(String xml) throws DocumentException {

		Map<String, String> result = new HashMap<String, String>();

		Document doc = DocumentHelper.parseText(xml);

		Element root = doc.getRootElement();
		parseNode(result, root);

		return result;
	}

	@SuppressWarnings("unchecked")
	public static void parseNode(Map<String, String> result, Element node) {
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
			for (String s : strs) {
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
			String uniquId = UUID.randomUUID().toString().replaceAll("-", "");
			method.invoke(obj, uniquId);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(uniquId, obj);
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
						String uniquId = UUID.randomUUID().toString().replaceAll("-", "");
						;
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
		String xml1 = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?><response><result>success</result><code>S200</code><message>订单查询成功</message><orderProcess><process><orderNo>EO1526638090849359</orderNo><orderStatus>订单审核</orderStatus><orderFailDesc></orderFailDesc><waybillNo>632725249350</waybillNo><logisticsCode>ZTO</logisticsCode><recTime>2018-05-18 18:08:10</recTime><hgPros><hgPro><hgStatus>O_OL_002</hgStatus><desc>订单审核</desc><proTime>2018-05-18 18:08:24</proTime></hgPro></hgPros></process></orderProcess><totalCount>1</totalCount></response>";
		System.out.println(parseXml(xml1, OrderStatus.class));
	}
}

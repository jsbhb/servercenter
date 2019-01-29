package com.zm.thirdcenter.utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> Set<T> parseXml(String xml, Class<T> clazz) throws Exception {
		Document doc = DocumentHelper.parseText(xml);
		Element root = doc.getRootElement();
		Object obj = clazz.newInstance();
		Map<String, Object> map = new HashMap<String, Object>();
		parseNode(root, clazz, map, obj);
		Set set = new HashSet();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			set.add(entry.getValue());
		}
		return set;
	}

	@SuppressWarnings({ "unchecked" })
	private static <T> Object parseNode(Element node, Class<T> clazz, Map<String, Object> map, Object obj)
			throws Exception {

		if (!(node.getTextTrim().equals(""))) {
			String fieldName = node.getName();
			if (fieldName != null) {
				fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				Method getMethod;
				try {
					getMethod = clazz.getMethod("get" + fieldName);
					String value = node.getTextTrim();
					Method method = clazz.getMethod("set" + fieldName, String.class);
					method.invoke(obj, value);
					String tem = (String) getMethod.invoke(obj);
					if (!map.containsKey(tem)) {
						map.put(tem, obj);
					}
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
		}
		Iterator<Element> iterator = node.elementIterator();
		while (iterator.hasNext()) {
			Element e = iterator.next();
			obj = parseNode(e, clazz, map, obj);
		}
		return obj;
	}
}

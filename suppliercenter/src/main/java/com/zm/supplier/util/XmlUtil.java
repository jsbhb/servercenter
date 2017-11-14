package com.zm.supplier.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.zm.supplier.pojo.SendOrderResult;

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
			if (value.equals(confMap.get(nodeName))) {
				return true;
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

	@SuppressWarnings("rawtypes")
	public static <T> List<T> parseXml(String xml, Class<T> clazz) throws Exception {
		Document doc = DocumentHelper.parseText(xml);
		Map<String, String> confMap = ConfUtils.getConfMap();
		Element root = doc.getRootElement();
		boolean flag = parseNode(root, confMap);
		if (flag) {
			Object obj = clazz.newInstance();
			List list = new ArrayList();
			parseNode(root, clazz, confMap, list, obj);
			return list;
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static <T> void parseNode(Element node, Class<T> clazz, Map<String, String> confMap,
			List list, Object obj) throws Exception {

		if (!(node.getTextTrim().equals(""))) {
			String nodeName = node.getName();
			String fieldName = confMap.get(nodeName);
			if (fieldName != null) {
				Method getMethod;
				try {
					getMethod = clazz.getMethod("get" + fieldName);
					Object o = getMethod.invoke(obj);
					if (o != null) {
						list.add(obj);
						obj = clazz.newInstance();
					}
					String value = node.getTextTrim();
					Method method = clazz.getMethod("set" + fieldName,String.class);
					method.invoke(obj, value);
				} catch (NoSuchMethodException e) {

				} catch (SecurityException e) {
					e.printStackTrace();
				}

			}
		}
		Iterator<Element> iterator = node.elementIterator();
		while (iterator.hasNext()) {
			Element e = iterator.next();
			parseNode(e, clazz, confMap, list, obj);
		}
	}

	public static void main(String[] args) throws Exception {
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><response><code></code><result>success</result><message></message><serialno></serialno><orderNos><no><busOrderNo>hwj4737262</busOrderNo><orderNo>PD146639611435211</orderNo></no><no><busOrderNo>hwj2828282</busOrderNo><orderNo>PD146639611448612</orderNo></no></orderNos></response>";
		System.out.println(parseXml(xml, SendOrderResult.class));
	}
}

package com.zm.supplier.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.zm.supplier.pojo.OrderStatus;
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> Set<T> parseXml(String xml, Class<T> clazz) throws Exception {
		Document doc = DocumentHelper.parseText(xml);
		Map<String, String> confMap = ConfUtils.getConfMap();
		Element root = doc.getRootElement();
		boolean flag = parseNode(root, confMap);
		if (flag) {
			Object obj = clazz.newInstance();
			List list = new ArrayList();
			parseNode(root, clazz, confMap, list, obj);
			Set set = new HashSet();
			for(Object o : list){
				set.add(o);
			}
			list.clear();
			return set;
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static <T> Object parseNode(Element node, Class<T> clazz, Map<String, String> confMap,
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
						obj = clazz.newInstance();
					}
					String value = node.getTextTrim();
					Method method = clazz.getMethod("set" + fieldName,String.class);
					method.invoke(obj, value);
					list.add(obj);
				} catch (NoSuchMethodException e) {

				} catch (SecurityException e) {
					e.printStackTrace();
				}

			}
		}
		Iterator<Element> iterator = node.elementIterator();
		while (iterator.hasNext()) {
			Element e = iterator.next();
			obj = parseNode(e, clazz, confMap, list, obj);
		}
		return obj;
	}

	public static void main(String[] args) throws Exception {
		String xml = "<response><result>success</result><code>S200</code><message>订单查询成功</message><orderProcess><process><orderNo>EO151101736421215</orderNo><orderStatus>审单未通过</orderStatus><orderFailDesc>验证参数异常/售价不能低于1%.商品有:[名称=测试奶粉,productID=3105166008N0000001]/</orderFailDesc><logisticsCode>TTKDEX</logisticsCode><recTime>2017-11-18 23:02:44</recTime><hgPros/></process></orderProcess><totalCount>1</totalCount></response>";
		System.out.println(parseXml(xml, OrderStatus.class));
	}
}

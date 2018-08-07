package com.zm.supplier.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class JaxbUtil {

	/**
	 * @fun 对象转XML
	 * @param c
	 * @return
	 * @throws JAXBException
	 */
	public static <T> String getMarshaller(Object obj, Class<T> c) throws JAXBException {

		JAXBContext context = JAXBContext.newInstance(c);
		Marshaller marshaller = context.createMarshaller();
		// 编码格式
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		// 是否格式化生成的xml串
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		// 是否省略xml头信息（<?xml version="1.0" encoding="utf-8" standalone="yes"?>）
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
		StringWriter writer = new StringWriter();
		
		marshaller.marshal(obj, writer);
		
		return writer.toString();
	}
	
	/**
	 * @fun xml转对象
	 * @param clazz
	 * @param context
	 * @param file
	 * @return
	 * @throws JAXBException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T readString(Class<T> clazz, String context, boolean file) throws JAXBException, IOException {
        try {
        	InputStream inputStream = null;
        	if(file){
        		inputStream = ClassLoader.getSystemResource(context).openStream();
        	} else {
        		inputStream = new ByteArrayInputStream(context.getBytes());
        	}
            JAXBContext jc = JAXBContext.newInstance(clazz);
            Unmarshaller u = jc.createUnmarshaller();
            return (T) u.unmarshal(inputStream);
        } catch (JAXBException e) {
            throw e;
        }
    }


}

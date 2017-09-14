package com.zm.order.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.zm.order.constants.LogConstants;
import com.zm.order.feignclient.model.LogInfo;

public class CommonUtils {

	private static final String DATE_FORMATE = "yyMMddHHmmssSSS";
	
	private static final String DECIMAL_FORMAT="000";
	
	public static String getOrderId(String orderFlag){
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMATE);
		DecimalFormat df2=(DecimalFormat) DecimalFormat.getInstance();
		df2.applyPattern(DECIMAL_FORMAT);
		StringBuffer sb = new StringBuffer();
		
		sb.append("GX");
		sb.append(orderFlag);
		sb.append(sdf.format(now));
		sb.append(df2.format(poll()));
		
		return sb.toString();
	}
	
	
	
	private static final ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<Integer>();
    private static final CountDownLatch latch = new CountDownLatch(1);
  
    private static final int MAX_PER_MSEC_SIZE = 100;

    private static void init() {
        for (int i = 0; i < MAX_PER_MSEC_SIZE; i++) {
            queue.offer(i);
        }
        latch.countDown();
    }

    private static Integer poll() {
        try {
            if (latch.getCount() > 0) {
                init();
                latch.await(1, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Integer i = queue.poll();
        queue.offer(i);
        return i;
    }
    
    
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {    
        if (map == null)   
            return null;    
  
        Object obj = beanClass.newInstance();  
  
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());    
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();    
        for (PropertyDescriptor property : propertyDescriptors) {  
            Method setter = property.getWriteMethod();    
            if (setter != null) {  
                setter.invoke(obj, map.get(property.getName()));   
            }  
        }  
  
        return obj;  
    }    
    
    
    public static LogInfo packageLog(Integer apiId, String apiName, Integer clientId, String content, String opt) {
		LogInfo info = new LogInfo();

		info.setApiId(apiId);
		info.setApiName(apiName);
		info.setCenterId(LogConstants.ORDER_CENTER_ID);
		info.setCenterName("订单服务中心");
		info.setClientId(clientId);
		info.setContent(content);
		info.setOpt(opt);

		return info;
	}
    
}
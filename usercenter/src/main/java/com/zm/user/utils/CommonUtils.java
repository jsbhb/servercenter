package com.zm.user.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CommonUtils {

	private static final String DATE_FORMATE = "yyMMddHHmmssSSS";
	
	private static final String DECIMAL_FORMAT="000";
	
	public static String getOrderId(){
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMATE);
		DecimalFormat df2=(DecimalFormat) DecimalFormat.getInstance();
		df2.applyPattern(DECIMAL_FORMAT);
		StringBuffer sb = new StringBuffer();
		
		sb.append("VIP");
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
}
package com.zm.order.utils;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Json转换
 *
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * date 2015年5月13日下午4:58:33
 */
public class JSONUtil {


    private static final ObjectMapper objectMapper;

    static {
    	 objectMapper = new ObjectMapper();
         objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
         objectMapper.configure(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
         objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
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
        
        public static <T> T parse(String jsonString, TypeReference<T> type) {
            try {
                return objectMapper.readValue(jsonString, type);
            } catch (Exception e) {
                throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
            }
        }

}

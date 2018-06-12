package com.zm.goods.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinYin4JUtil {

	/**
	 * @fun 获取字符串第一个字符的第一个英文字母(中文和英文)
	 * @param
	 * @return 首字母
	 */
	public static String cn2PYInitial(String chinese) {
		if (chinese == null || chinese.equals("")) {
			return "";
		} else {
			StringBuilder pybf = new StringBuilder();

			// 英文字母不需要转换
			char[] chars = chinese.trim().toCharArray();

			HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
			// 格式化为小写字母
			format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
			// 不需要音调
			format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			// 设置对拼音字符 ü 的处理
			format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);

			String[] str = null;
			char ch = chars[0];
			if((ch>='a' && ch<='z') || (ch>='A' && ch<='Z')){
				pybf.append(ch);
				return pybf.toString();
			}
			try {
				str = PinyinHelper.toHanyuPinyinStringArray(ch, format);
				// 不是汉字，估计是特殊字符,统一用#号代替
				if (str == null || str.length == 0) {
					pybf.append("#");
				} else {
					// 多音字只要第一个读音的首字母
					pybf.append(str[0].charAt(0));
				}
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
				// 出现格式化异常,统一用#号代替
				pybf.append("#");
			}
			return pybf.toString();
		}
	}
	
	/**
	 * @fun 按首字母分装成map，如果不是String的list，needConvert为对象中需要首字母的字段
	 * @param list
	 * @param clazz
	 * @param needConvert
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String,List<Object>> packDataByFirstCode(List list,Class<?> clazz, String needConvert){
		Map<String,List<Object>> result = new HashMap<String, List<Object>>();
		if(list == null || list.size() == 0){
			throw new RuntimeException("list 不能为空");
		}
		boolean isString = true;
		if(!clazz.getName().equals("java.lang.String")){
			isString = false;
		}
		List<Object> temp = null;
		for(Object obj : list){
			if(isString){
				if(result.get(cn2PYInitial(obj.toString())) == null){
					temp = new ArrayList<Object>();
					temp.add(obj);
					result.put(cn2PYInitial(obj.toString()), temp);
				} else {
					result.get(cn2PYInitial(obj.toString())).add(obj);
				}
			} else {
				try {
					Method method = clazz.getMethod("get"+needConvert.substring(0,1).toUpperCase()+needConvert.substring(1));
					try {
						String str = method.invoke(obj).toString();
						if(result.get(cn2PYInitial(str)) == null){
							temp = new ArrayList<Object>();
							temp.add(obj);
						} else {
							result.get(cn2PYInitial(str)).add(obj);
						}
					} catch (IllegalAccessException e) {
						e.printStackTrace();
						return null;
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						return null;
					} catch (InvocationTargetException e) {
						e.printStackTrace();
						return null;
					}
				} catch (NoSuchMethodException e) {
					throw new RuntimeException("请确定对象中是否有该字段的get方法",e);
				} catch (SecurityException e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return result;
	}
}

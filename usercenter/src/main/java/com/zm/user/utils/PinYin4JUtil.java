package com.zm.user.utils;

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
}

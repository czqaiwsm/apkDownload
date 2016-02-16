package com.download.base.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * EditText相关的辅助类,主要用来判断是用户输入的信息是否符合需求
 */
public class EditTextUtils {

	/**
	 * 判断用户输入的信息是否为数字、字母或下划线
	 * 
	 * @Title: isNumberAndLetter
	 * @param
	 * @return boolean true为数字、字母或下划线
	 */
	public static boolean isNumberAndLetter(String string) {
		Pattern p = Pattern.compile("[0-9a-zA-Z_]*");
		Matcher m = p.matcher(string);
		if (m.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * 判断用户输入的信息是否为汉字
	 * 
	 * @Title: isChinese
	 * @param
	 * @return boolean true为汉字
	 */
	public static boolean isChinese(String string) {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(string);
		if (m.matches()) {
			return true;
		}
		return false;
	}

}

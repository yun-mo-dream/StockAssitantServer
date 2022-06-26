package com.storage;

public class TextUtil {

	public static boolean isNullOrEmpty(String text) {
		return null == text || text.equals("");
	}
	
	public static boolean isNullOrEmpty(String[] array) {
		return null == array || array.length <= 0;
	}
}

package com.meitao.common.util;




public class RenlingUtil {
	public static String getSearchColumnByType(String type) {
		if ("0".equals(type)) {
			return null;
		}
		if ("1".equals(type)) {
			return "company";
		}
		if("2".equals(type)) {
			return "baoguo_id";
		}
		if ("3".equals(type)) {
			return "title";
		}
		if ("4".equals(type)) {
			return "content";
		}
		if ("5".equals(type)) {
			return "user_id";
		}
		if ("6".equals(type)) {
			return "rev_name";
		}
		if ("7".equals(type)) {
			return "baoguo_phone";
		}
		
		return null;
	}


	

}

package com.meitao.common.util.excel;

import java.lang.reflect.Method;

public class GetProperty {

	/**
	 * 根据属性名称调用对应class的对象get方法获取该属性的值
	 * 
	 * @param propertyName
	 * @param cls
	 * @param obj
	 * @return
	 */
	public static Object getProperty(String propertyName, Class<?> cls, Object obj) {
		try {
			if (propertyName != null && !"".equals(propertyName)) {
				Method method = cls.getMethod("get"
				        + propertyName.replaceFirst(String.valueOf(propertyName.charAt(0)), String.valueOf(
				                propertyName.charAt(0)).toUpperCase()));
				return method.invoke(obj);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

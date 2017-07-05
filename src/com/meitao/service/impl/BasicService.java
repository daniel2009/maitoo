package com.meitao.service.impl;

import java.util.Date;

import com.meitao.common.util.StringUtil;
import com.meitao.model.Employee;
import com.meitao.model.User;


public class BasicService {
	/**
	 * 处理职员对象中的安全属性，比如密码等。
	 * 
	 * @param employee
	 * @return
	 */
	public Employee getSecurityValue(Employee employee) {
		return employee;
	}

	/**
	 * 处理user对象中的安全属性，比如密码等
	 * 
	 * @param user
	 * @return
	 */
	public User getSecurityValue(User user) {
		user.setPassword(null);
		return user;
	}

	/**
	 * 根据用户id生成一个订单id
	 * 
	 * @param userId
	 * @return
	 */
	public String generateOrderId(String userId) {
		StringBuffer sb = new StringBuffer(20);
		sb.append(new Date().getTime()); // 13位
		sb.append(this.makeStr(userId, 4));// 四位
		sb.append(StringUtil.generateRandomInteger(4));
		return sb.substring(0, Math.max(sb.length(), 20)); // 最多二十位
	}

	private String makeStr(String value, int len) {
		StringBuffer sb = new StringBuffer(len);
		int n = len;
		if (value != null) {
			n = len - value.trim().length();
		}
		while (n-- > 0) {
			sb.append("0");
		}
		sb.append(value);
		return sb.substring(0, Math.min(sb.length(), len));
	}
}

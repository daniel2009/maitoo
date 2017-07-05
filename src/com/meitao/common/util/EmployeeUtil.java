package com.meitao.common.util;

import com.meitao.common.constants.ValidationConstants;

public class EmployeeUtil {

	public static boolean validateId(String id) {
		return !StringUtil.isEmpty(id) && StringUtil.isPattern(ValidationConstants.COMMON_ID_REGEX, id);
	}

	/**
	 * 校验账户，如果账户为空或者是不符合规定，则返回false。否则返回true。
	 * 
	 * @param accountName
	 * @return
	 */
	public static boolean validateAccountName(String accountName) {
		return !StringUtil.isEmpty(accountName)
		        && StringUtil.isPattern(ValidationConstants.EMPLOYEE_ACCOUNT_REGEX, accountName);
	}

	public static boolean validatePhone(String phone) {
		return StringUtil.isEmpty(phone) || StringUtil.isPattern(ValidationConstants.EMPLOYEE_PHONE, phone);
	}

	/**
	 * 检查密码，如果密码为空或者不符合规定，则返回false。否则返回true。
	 * 
	 * @param password
	 * @return
	 */
	public static boolean validatePassword(String password) {
		return !StringUtil.isEmpty(password)
		        && StringUtil.isPattern(ValidationConstants.EMPLOYEE_PASSWORD_REGEX, password);
	}

	public static boolean validateStoreId(String storeId) {
		return StringUtil.isEmpty(storeId)
		        || StringUtil.isPattern(ValidationConstants.EMPLOYEE_STORE_ID_REGEX, storeId);
	}

	public static boolean validateStoreName(String storeName) {
		return StringUtil.isEmpty(storeName)
		        || StringUtil.isPattern(ValidationConstants.EMPLOYEE_STORE_NAME_REGEX, storeName);
	}
}

package com.meitao.common.util;

import com.meitao.common.constants.ValidationConstants;

/**
 * 专门处理上门取件信息的工具类
 * 
 */
public class CallOrderUtil {

	/**
	 * 验证上门取件id是否符合定义，如果不符合，则返回false。否则返回true。
	 * 
	 * @param id
	 * @return
	 */
	public static boolean validateId(String id) {
		return !StringUtil.isEmpty(id) && StringUtil.isPattern(ValidationConstants.COMMON_ID_REGEX, id);
	}

	/**
	 * 验证寄件人id是否符合定义，如果符合就返回true。否则返回false。
	 * 
	 * @param userId
	 * @return
	 */
	public static boolean validateUserId(String userId) {
		return !StringUtil.isEmpty(userId) && StringUtil.isPattern(ValidationConstants.COMMON_ID_REGEX, userId);
	}

	/**
	 * 验证寄件人姓名是否符合规定，如果符合就返回true，否则返回false。
	 * 
	 * @param name
	 * @return
	 */
	public static boolean validateCallOrderName(String name) {
		return !StringUtil.isEmpty(name) && StringUtil.isPattern(ValidationConstants.CALL_ORDER_NAME_REGEX, name);
	}

	/**
	 * 验证寄件人省份是否正确，如果符合，则返回true。否则返回false。
	 * 
	 * @param province
	 * @return
	 */
	public static boolean validateProvince(String province) {
		return !StringUtil.isEmpty(province)
		        && StringUtil.isPattern(ValidationConstants.CALL_ORDER_PROVINCE_REGEX, province);
	}

	/**
	 * 验证寄件人城市是否符合定义，如果符合就返回true。否则返回false。
	 * 
	 * @param city
	 * @return
	 */
	public static boolean validateCity(String city) {
		return !StringUtil.isEmpty(city) && StringUtil.isPattern(ValidationConstants.CALL_ORDER_CITY_REGEX, city);
	}

	/**
	 * 验证寄件人区县是否符合定义，如果符合就返回true。否则返回false。
	 * 
	 * @param district
	 * @return
	 */
	public static boolean validateDistrict(String district) {
		return !StringUtil.isEmpty(district)
		        && StringUtil.isPattern(ValidationConstants.CALL_ORDER_DISTRICT_REGEX, district);
	}

	/**
	 * 验证寄件人街道名称是否符合定义，如果符合就返回true。否则返回false。
	 * 
	 * @param sadd
	 * @return
	 */
	public static boolean validateStreetAddress(String sadd) {
		return !StringUtil.isEmpty(sadd)
		        && StringUtil.isPattern(ValidationConstants.CALL_ORDER_STREET_ADDRESS_REGEX, sadd);
	}

	/**
	 * 验证寄件人地址邮编是否符合定义，如果符合就返回true。否则返回false。
	 * 
	 * @param zcode
	 * @return
	 */
	public static boolean validateZipCode(String zcode) {
		return !StringUtil.isEmpty(zcode) && StringUtil.isPattern(ValidationConstants.CALL_ORDER_ZIP_CODE_REGEX, zcode);
	}

	/**
	 * 验证寄件人电话号码是否符合定义，如果符合就返回true。否则返回false。
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean validatePhone(String phone) {
		return !StringUtil.isEmpty(phone) && StringUtil.isPattern(ValidationConstants.CALL_ORDER_PHONE_REGEX, phone);
	}
}

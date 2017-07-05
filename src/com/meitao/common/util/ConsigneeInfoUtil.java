package com.meitao.common.util;

import com.meitao.common.constants.ValidationConstants;

/**
 * 专门处理收货地址信息的工具类
 * 
 */
public class ConsigneeInfoUtil {

	/**
	 * 验证收货地址id是否符合定义，如果不符合，则返回false。否则返回true。
	 * 
	 * @param id
	 * @return
	 */
	public static boolean validateId(String id) {
		return !StringUtil.isEmpty(id) && StringUtil.isPattern(ValidationConstants.COMMON_ID_REGEX, id);
	}

	/**
	 * 验证用户id是否符合定义，如果符合就返回true。否则返回false。
	 * 
	 * @param userId
	 * @return
	 */
	public static boolean validateUserId(String userId) {
		return !StringUtil.isEmpty(userId) && StringUtil.isPattern(ValidationConstants.COMMON_ID_REGEX, userId);
	}

	/**
	 * 验证收货人姓名是否符合规定，如果符合就返回true，否则返回false。
	 * 
	 * @param name
	 * @return
	 */
	public static boolean validateConsigneeName(String name) {
		return !StringUtil.isEmpty(name) && StringUtil.isPattern(ValidationConstants.CONSIGNEE_NAME_REGEX, name);
	}

	/**
	 * 验证收货省份是否正确，如果符合，则返回true。否则返回false。
	 * 
	 * @param province
	 * @return
	 */
	public static boolean validateProvince(String province) {
		return !StringUtil.isEmpty(province)
		        && StringUtil.isPattern(ValidationConstants.CONSIGNEE_PROVINCE_REGEX, province);
	}

	/**
	 * 验证收货城市是否符合定义，如果符合就返回true。否则返回false。
	 * 
	 * @param city
	 * @return
	 */
	public static boolean validateCity(String city) {
		return !StringUtil.isEmpty(city) && StringUtil.isPattern(ValidationConstants.CONSIGNEE_CITY_REGEX, city);
	}

	/**
	 * 验证收货区县是否符合定义，如果符合就返回true。否则返回false。
	 * 
	 * @param district
	 * @return
	 */
	public static boolean validateDistrict(String district) {
		return !StringUtil.isEmpty(district)
		        && StringUtil.isPattern(ValidationConstants.CONSIGNEE_DISTRICT_REGEX, district);
	}

	/**
	 * 验证收货街道名称是否符合定义，如果符合就返回true。否则返回false。
	 * 
	 * @param sadd
	 * @return
	 */
	public static boolean validateStreetAddress(String sadd) {
		return !StringUtil.isEmpty(sadd)
		        && StringUtil.isPattern(ValidationConstants.CONSIGNEE_STREET_ADDRESS_REGEX, sadd);
	}

	/**
	 * 验证收货地址邮编是否符合定义，如果符合就返回true。否则返回false。
	 * 
	 * @param zcode
	 * @return
	 */
	public static boolean validateZipCode(String zcode) {
		return !StringUtil.isEmpty(zcode) && StringUtil.isPattern(ValidationConstants.CONSIGNEE_ZIP_CODE_REGEX, zcode);
	}

	/**
	 * 验证电话号码是否符合定义，如果符合就返回true。否则返回false。
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean validatePhone(String phone) {
		return !StringUtil.isEmpty(phone) && StringUtil.isPattern(ValidationConstants.CONSIGNEE_PHONE_REGEX, phone);
	}

	/**
	 * 验证固定电话是否符合定义，如果符合就返回true。否则返回false。
	 * 
	 * @param telephone
	 * @return
	 */
	public static boolean validateTelephone(String telephone) {
		return StringUtil.isEmpty(telephone)
		        || StringUtil.isPattern(ValidationConstants.CONSIGNEE_TELEPHONE_REGEX, telephone);
	}

	/**
	 * 验证身份证号码是否符合规定，如果为空或者是不超过20位的数字，那么久返回true。
	 * 
	 * @param cid
	 * @return
	 */
	public static boolean validateCardId(String cid) {
		return StringUtil.isEmpty(cid) || StringUtil.isPattern(ValidationConstants.CONSIGNEE_CARD_ID, cid);
	}
}

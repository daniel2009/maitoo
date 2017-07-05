package com.meitao.common.util;

import java.io.File;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;

import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ValidationConstants;
import com.meitao.common.util.excel.ExcelUtil;
import com.meitao.common.util.excel.ExcelWrite;
import com.meitao.model.User;

import jxl.write.Label;
import jxl.write.WritableSheet;

public class UserUtil {
	private static final String SEARCH_NAME = "name";
	private static final String SEARCH_EMAIL = "email";
	private static final String SEARCH_REALNAME = "real_name";
	private static final String SEARCH_PHONE = "phone";
	private static final String SEARCH_QQ = "qq";

	/**
	 * 验证用户编码是否正确。
	 * 
	 * @param id
	 * @return
	 */
	public static boolean validateId(String id) {
		return !StringUtil.isEmpty(id) && StringUtil.isPattern(ValidationConstants.COMMON_ID_REGEX, id);
	}

	/**
	 * 检查email是否符合规定。如果符合则返回true。否则返回false。
	 * 
	 * @param email
	 * @return
	 */
	public static boolean validateEmail(String email) {
		return !StringUtil.isEmpty(email) && StringUtil.isPattern(ValidationConstants.USER_EMAIL_REGEX, email);
	}

	/**
	 * 校验昵称，如果账户为空或者是不符合规定，则返回false。否则返回true。
	 * 
	 * @param nickName
	 * @return
	 */
	public static boolean validateNickName(String nickName) {
		return !StringUtil.isEmpty(nickName)
		        && StringUtil.isPattern(ValidationConstants.USER_NICK_NAME_REGEX, nickName);
	}

	/**
	 * 检查密码，如果密码为空或者不符合规定，则返回false。否则返回true。
	 * 
	 * @param password
	 * @return
	 */
	public static boolean validatePassword(String password) {
		return !StringUtil.isEmpty(password) && StringUtil.isPattern(ValidationConstants.USER_PASSWORD_REGEX, password);
	}

	/**
	 * 检查真实姓名是否符合规定。如果符合则返回true。否则返回false。
	 * 
	 * @param realName
	 * @return
	 */
	public static boolean validateRealName(String realName) {
		return StringUtil.isEmpty(realName) || StringUtil.isPattern(ValidationConstants.USER_REAL_NAME_REGEX, realName);
	}

	/**
	 * 检查电话号码是否符合规定。如果符合则返回true。否则返回false。
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean validatePhone(String phone) {
		return StringUtil.isEmpty(phone) || StringUtil.isPattern(ValidationConstants.USER_PHONE_REGEX, phone);
	}

	/**
	 * 检查qq是否符合规定。如果符合则返回true。否则返回false。
	 * 
	 * @param qq
	 * @return
	 */
	public static boolean validateQQ(String qq) {
		return StringUtil.isEmpty(qq) || StringUtil.isPattern(ValidationConstants.USER_QQ_REGEX, qq);
	}

	/**
	 * 检查推荐人账户是否符合规定。如果符合则返回true。否则返回false。
	 * 
	 * @param realName
	 * @return
	 */
	public static boolean validateRecommender(String recommender) {
		return StringUtil.isEmpty(recommender) || "-1".equals(recommender)
		        || StringUtil.isPattern(ValidationConstants.COMMON_ID_REGEX, recommender);
	}

	/**
	 * 验证用户所属国家是否字符串是否符合规定，如果符合则返回true。否则返回false。
	 * 
	 * @param country
	 * @return
	 */
	public static boolean validateCountry(String country) {
		return StringUtil.isEmpty(country) || StringUtil.isPattern(ValidationConstants.USER_COUNTRY_REGEX, country);
	}

	/**
	 * 验证用户所属地址信息是否字符串是否符合规定，如果符合则返回true。否则返回false。
	 * 
	 * @param country
	 * @return
	 */
	public static boolean validateAddress(String address) {
		return StringUtil.isEmpty(address) || StringUtil.isPattern(ValidationConstants.USER_ADDRESS_REGEX, address);
	}

	/**
	 * 根据传入的type值，获取查询时候的column值
	 * 
	 * @param type
	 * @return
	 */
	public static String getSearchColumnByType(String type) {
		if (SEARCH_EMAIL.equals(type)) {
			return "email";
		} else if (SEARCH_NAME.equals(type)) {
			return "nick_name";
		} else if (SEARCH_REALNAME.equals(type)) {
			return "real_name";
		} else if (SEARCH_PHONE.equals(type)) {
			return "phone";
		} else if (SEARCH_QQ.equals(type)) {
			return "qq";
		} else {
			return null;
		}
	}

	/**
	 * 根据用户的type int值获取对应的type字符串
	 * 
	 * @param type
	 * @return
	 */
	public static String transformerUserType(String type) {
		if (Constant.USER_TYPE_NORMAL.equals(type)) {
			return "普通会员";
		} else if (Constant.USER_TYPE_VIP0.equals(type)) {
			return "白银VIP会员";
		}else if (Constant.USER_TYPE_VIP1.equals(type)) {
			return "黄金VIP会员";
		}else if (Constant.USER_TYPE_VIP2.equals(type)) {
			return "白金VIP会员";
		}else if (Constant.USER_TYPE_VIP3.equals(type)) {
			return "钻石VIP会员";
		}else if (Constant.USER_TYPE_VIP4.equals(type)) {
			return "至尊VIP1会员";
		}else if (Constant.USER_TYPE_VIP5.equals(type)) {
			return "至尊VIP2会员";
		}else if (Constant.USER_TYPE_VIP6.equals(type)) {
			return "至尊VIP3会员";
		}else if (Constant.USER_TYPE_VIP7.equals(type)) {
			return "至尊VIP4会员";
		} else {
			return "会员未定";
		}
		
	
	}

	/**
	 * 检测date是否符合yyyy-mm-dd或者yyyy-m-d或者其他的时间格式
	 * 
	 * @param date
	 * @return
	 */
	public static boolean validateExportDate(String date) {
		return StringUtil.isPattern("\\d{4}-(\\d|\\d{2})-(\\d|\\d{2})", date);
	}

	/**
	 * 转换时间字符串为yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String transformerDateString(String date, String endStr) {
		StringBuffer sb = new StringBuffer(10);
		int len = date.length();
		int k = 0;
		for (int i = 0; i < len; i++) {
			char ch = date.charAt(i);
			k++;
			sb.append(ch);
			if (ch == '-') {
				if (k == 2) {
					// 不是在那个位置的
					sb.insert(sb.length() - 2, '0');
				}
				k = 0;
			}
		}
		if (k == 1) {
			sb.insert(sb.length() - 1, '0');
		}
		return sb.toString() + endStr;
	}

	/**
	 * 导出users的excel文件到输出了os中
	 * 
	 * @param datas
	 * @param templetFile
	 * @param os
	 * @throws Exception
	 */
	public static void exportUsersToExcel(Collection<User> datas, File templetFile, OutputStream os) throws Exception {
		if (templetFile != null && templetFile.exists()) {
			// 模板复制
			ExcelUtil.exportExcel(new ExcelWrite<User>() {
				@Override
				public void write(Map<String, String> headers, Collection<User> datas, WritableSheet sheet)
				        throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有用户数据.....");
						sheet.addCell(cell);
						return;
					}

					int row = 1;
					for (User user : datas) {
						int columnIndex = 0;
						sheet.addCell(new Label(columnIndex++, row, user.getId()));
						sheet.addCell(new Label(columnIndex++, row, transformerUserType(user.getType())));
						sheet.addCell(new Label(columnIndex++, row, user.getNickName()));
						sheet.addCell(new Label(columnIndex++, row, user.getRealName()));
						sheet.addCell(new Label(columnIndex++, row, user.getPhone()));
						sheet.addCell(new Label(columnIndex++, row, user.getQq()));
						sheet.addCell(new Label(columnIndex++, row, user.getEmail()));
						sheet.addCell(new Label(columnIndex++, row, user.getSignDate()));
						sheet.addCell(new Label(columnIndex++, row, user.getRmbBalance()));
						sheet.addCell(new Label(columnIndex++, row, user.getUsdBalance()));
						sheet.addCell(new Label(columnIndex++, row, user.getCountry()));
						row++;
					}
				}
			}, templetFile, datas, os);
		} else {
			// 重新写
			ExcelUtil.exportExcel(new ExcelWrite<User>() {
				@Override
				public void write(Map<String, String> headers, Collection<User> datas, WritableSheet sheet)
				        throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有用户数据.....");
						sheet.addCell(cell);
						return;
					}

					int row = 0;
					int columnIndex = 0;

					sheet.addCell(new Label(columnIndex++, row, "用户编号"));
					sheet.addCell(new Label(columnIndex++, row, "用户类型"));
					sheet.addCell(new Label(columnIndex++, row, "登录名"));
					sheet.addCell(new Label(columnIndex++, row, "真实姓名"));
					sheet.addCell(new Label(columnIndex++, row, "电话号码"));
					sheet.addCell(new Label(columnIndex++, row, "QQ"));
					sheet.addCell(new Label(columnIndex++, row, "Email"));
					sheet.addCell(new Label(columnIndex++, row, "注册时间"));
					sheet.addCell(new Label(columnIndex++, row, "RMB余额"));
					sheet.addCell(new Label(columnIndex++, row, "USD余额"));
					sheet.addCell(new Label(columnIndex++, row, "国别"));

					row++;
					for (User user : datas) {
						columnIndex = 0;
						sheet.addCell(new Label(columnIndex++, row, user.getId()));
						sheet.addCell(new Label(columnIndex++, row, transformerUserType(user.getType())));
						sheet.addCell(new Label(columnIndex++, row, user.getNickName()));
						sheet.addCell(new Label(columnIndex++, row, user.getRealName()));
						sheet.addCell(new Label(columnIndex++, row, user.getPhone()));
						sheet.addCell(new Label(columnIndex++, row, user.getQq()));
						sheet.addCell(new Label(columnIndex++, row, user.getEmail()));
						sheet.addCell(new Label(columnIndex++, row, user.getSignDate()));
						sheet.addCell(new Label(columnIndex++, row, user.getRmbBalance()));
						sheet.addCell(new Label(columnIndex++, row, user.getUsdBalance()));
						sheet.addCell(new Label(columnIndex++, row, user.getCountry()));
						row++;
					}
				}
			}, "user-datas", null, datas, os);
		}
	}
}

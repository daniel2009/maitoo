package com.meitao.common.util;

import com.meitao.common.constants.ValidationConstants;

public class ErrorTranshipmentInfoUtil {
	/**
	 * 验证异常件内容是否符合规定，如果不符合，则返回false。
	 * 
	 * @param content
	 * @return
	 */
	public static boolean validateContent(String content) {
		return !StringUtil.isEmpty(content)
		        && StringUtil.isPattern(ValidationConstants.ERROR_TORDER_CONTENT_REGEX, content);
	}

	/**
	 * 验证留言的父节点，是否符合规定。如果不符合，则返回false。
	 * 
	 * @param fatherId
	 * @return
	 */
	public static boolean validateParentId(String fatherId) {
		return StringUtil.isEmpty(fatherId) || StringUtil.isPattern(ValidationConstants.COMMON_ID_REGEX, fatherId);
	}
}

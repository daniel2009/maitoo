package com.meitao.common.util;

import com.meitao.common.constants.ValidationConstants;

/**
 * 操作新闻的工具类
 * 
 */
public class NewsUtil {
	/**
	 * 验证新闻id是否正常
	 * 
	 * @param id
	 * @return
	 */
	public static boolean validateId(String id) {
		return !StringUtil.isEmpty(id) && StringUtil.isPattern(ValidationConstants.COMMON_ID_REGEX, id);
	}
	
	/**
	 * 操作新闻查询时，对应列的关系
	 * 
	 */
	
	public static String validatecolumn(String nomber)
	{
		if(nomber.equalsIgnoreCase("0"))//新闻标题
		{
			return "title";//数据库中的列
		}
		else if(nomber.equalsIgnoreCase("1"))//新闻内容
		{
			return "viewcontent";//数据库中的列
		}
		else  if(nomber.equalsIgnoreCase("2"))//新闻添加人
		{
			return "author";//数据库中的列
		}
		return "";
	}
}



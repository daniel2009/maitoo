package com.meitao.service;

import javax.servlet.http.HttpServletRequest;



import com.meitao.exception.ServiceException;

import com.meitao.model.EndiciaLabel;

import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;


/**
 * Endicail 接口操作
 */
public interface EndiciaService {

	/**
	 * 查询价格
	 * 
	 */
	public ResponseObject<Object> checkediciaprices(EndiciaLabel label) throws ServiceException;
	public ResponseObject<Object> checkediciapricesbyadminnew(EndiciaLabel label) throws ServiceException;//新的后台检查价格操作
	
	
	
	
	public ResponseObject<Object> checkediciapricesbyadmin(EndiciaLabel label) throws ServiceException;//管理员检查

	/**
	 * 打印标签
	 * 
	 */
	public ResponseObject<Object> printlabel(EndiciaLabel label,HttpServletRequest request) throws ServiceException;
	public ResponseObject<Object> printlabelbyadmin(EndiciaLabel label,HttpServletRequest request,String adminname) throws ServiceException;	
	

	public ResponseObject<PageSplit<EndiciaLabel>> searchEndiciaLabelByKeys( String userId,String userInfo,  String labelInfo, String sdate,  String edate,int pageSize, int pageNow) throws ServiceException;
}

package com.meitao.service;

import java.util.List;


import javax.servlet.http.HttpServletRequest;

import com.meitao.exception.ServiceException;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.cardid.manage.CardId_lib;
import com.meitao.cardid.manage.import_t_orders;
import com.meitao.cardid.manage.importcardargs;


public interface CardidManageService {
	/**
	 * 添加航班信息
	 * 
	 * @param warehouse
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Object> addcardids(List<importcardargs> importflyInfo) throws ServiceException;

	public ResponseObject<PageSplit<CardId_lib>> searchbykey(String content,HttpServletRequest request, int pageSize, int pageNow) throws ServiceException;
	
	public ResponseObject<Object> deleteonecard(String id,HttpServletRequest request) throws ServiceException;
	
	public ResponseObject<Object> verifycardid(List<import_t_orders> imorders,String rule,HttpServletRequest request) throws ServiceException;
}

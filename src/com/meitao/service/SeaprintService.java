package com.meitao.service;

import java.util.List;


import com.meitao.exception.ServiceException;
import com.meitao.model.CardId;

import com.meitao.model.PageSplit;
import com.meitao.model.Porder;
import com.meitao.model.ResponseObject;
import com.meitao.model.Seaprint;



public interface SeaprintService {
	/**
	 * 添加航班信息
	 * 
	 * @param warehouse
	 * @return
	 * @throws ServiceException
	 */
	
	public ResponseObject<Object> addSeaprint(Seaprint seaprint) throws ServiceException;
	
	public ResponseObject<PageSplit<Seaprint>> searchSeaprint(String seaprintno, int pageSize, int pageNow,String storeId) throws ServiceException;
	
	public ResponseObject<Object> getbyid(String id,String storeId) throws ServiceException;
	
	public ResponseObject<Object> deleteSeaprintById(String id) throws ServiceException;//删除seaprint通过id

	
	public ResponseObject<Object> importcardids(List<CardId> cardids) throws ServiceException;//导入身份证数据
	public ResponseObject<Object> importporders(List<Porder> porder,String seaprintid) throws ServiceException;//海关打印单
	
	
	public ResponseObject<PageSplit<Porder>> searchPorders(String seaprintno,String porder,String content,String state, int pageSize, int pageNow,String storeId) throws ServiceException; 
	
	
	public ResponseObject<Object> getporderbyid(String id) throws ServiceException;
	public ResponseObject<Object> modifyporderstate(String id,String state) throws ServiceException;
	
	public ResponseObject<Object> deleteporderbyid(String id) throws ServiceException;
}

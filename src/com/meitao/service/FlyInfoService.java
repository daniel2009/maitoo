package com.meitao.service;

import java.util.List;


import com.meitao.exception.ServiceException;
import com.meitao.model.FlyInfo;

import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;

import com.meitao.model.temp.ImportFlyInfo;


public interface FlyInfoService {
	/**
	 * 添加航班信息
	 * 
	 * @param warehouse
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Object> addFlyInfo(FlyInfo flyinfo) throws ServiceException;
	public ResponseObject<PageSplit<FlyInfo>> searchFlyInfo(String flightno, String key, String sdate,
	        String edate, String state, int pageSize, int pageNow,String storeId) throws ServiceException;

	public ResponseObject<Object> deleteFlyInfoById(String id) throws ServiceException;
	

	public ResponseObject<FlyInfo> getbyid(String id) throws ServiceException;
	
	
	public ResponseObject<Object> addFlyForMorder(FlyInfo flyinfo) throws ServiceException;
	
	//修改m_order的航班信息
	public ResponseObject<Object> modifyFlyInfoWithExcelForMorder(FlyInfo flyinfo,String changeorderflag,String empName,List<ImportFlyInfo> importflyInfo,String sendmessage,String storeId) throws ServiceException;
	//修改单独上传文件内容
	public ResponseObject<Object> modifyFlyInfoByfile(FlyInfo flyinfo,String empName,List<ImportFlyInfo> importflyInfo,String storeId) throws ServiceException;
	//修改航班信息
	public ResponseObject<Object> modifyFlyInfoMorder(FlyInfo flyinfo,String changeorderflag,String sendmessage,String sendmessage_cardId,String empName,String storeId) throws ServiceException;
}

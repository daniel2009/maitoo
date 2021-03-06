package com.meitao.service;

import java.util.List;
import java.util.Map;

import com.meitao.exception.ServiceException;

import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;

import com.meitao.model.RenlingBaoguo;

public interface RenlingService {
	/**
	 * 添加仓库地址
	 * 
	 * @param warehouse
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Map<String, String>> addRenling(RenlingBaoguo renling) throws ServiceException;
	
	/**
	 * 根据key模糊查询，开始时间为state，结束时间为edate。如果这个时间为空，则表示时间不限制。
	 * 
	 * @param rid
	 *            TODO
	 * @param key
	 * @param column
	 * @param state
	 * @param sdate
	 * @param edate
	 * @param pageSize
	 * @param pageNow
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<PageSplit<RenlingBaoguo>> searchByKey(String rid, String key, String column,String state, String sdate,
	        String edate, int pageSize, int pageNow) throws ServiceException;
	
	
	/**
	 * 根据id删除认领信息<br/>
	 * 管理员操作
	 * 
	 * @param ids
	 *            运单id，主键
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Object> deleteRenlingbill(List<String> ids) throws ServiceException;
	
	
	public ResponseObject<RenlingBaoguo> getRenlingbyid(String id) throws ServiceException;
	
	
	/**
	 * 修改信领状态
	 * 
	 * @param warehouse
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Map<String, String>> modifyRenling(RenlingBaoguo renling) throws ServiceException;

	public ResponseObject<Integer> countNeedAudit() throws ServiceException;

	public ResponseObject<Object> updateByAdmin(RenlingBaoguo renling) throws ServiceException;
}

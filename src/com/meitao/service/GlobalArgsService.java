package com.meitao.service;

import java.util.List;

import com.meitao.exception.ServiceException;

import com.meitao.model.ResponseObject;

import com.meitao.model.globalargs;
import com.meitao.model.globalargsExport;
import com.meitao.model.globalargsIndex;


/**
 * 提供新闻服务的接口
 */
public interface GlobalArgsService {

	/**
	 * 获取全部的全局参数列表
	 * 创建时间：20151012
	 * 创建者：kai
	 * 修改时间：
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<List<globalargs>> getAll() throws ServiceException;
	
	/**
	 * 保存一条全局参数
	 * 
	 * @param args
	 *            要添加的全局变量
	 * @return 如果添加成功，则返回true;否则返回false.
	 * @throws ServiceException
	 *             如果操作过程中出现异常，则抛出此封装异常。
	 */
	public ResponseObject<Object> saveglobalargs(globalargs args) throws ServiceException;
	/**
	 * 获取一个全局变量根据id
	 * 创建时间：20151013
	 * 创建者：kai
	 * 修改时间：
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<globalargs> getonebyid(String id) throws ServiceException;
	
	/**
	 * 保存一条全局参数
	 * 
	 * @param args
	 *            要添加的全局变量
	 * @return 如果添加成功，则返回true;否则返回false.
	 * @throws ServiceException
	 *             如果操作过程中出现异常，则抛出此封装异常。
	 */
	public ResponseObject<Object> modifyglobalargs(globalargs args) throws ServiceException;
	
	public ResponseObject<Object> modifyglobalargs1(globalargs args) throws ServiceException;
	
	/**
	 * 删除一条记录,根据id
	 * 
	 * @param
	 *          
	 * @return 如果添加成功，则返回true;否则返回false.
	 * @throws ServiceException
	 *             如果操作过程中出现异常，则抛出此封装异常。
	 */
	public ResponseObject<Object> deleteoneglobalargs(String id) throws ServiceException;
	
	/**
	 * 获取一条记录的内容,根据flag
	 * 
	 * @param
	 *          
	 * @return 返回内容
	 * @throws ServiceException
	 *             如果操作过程中出现异常，则抛出此封装异常。
	 */
	public ResponseObject<String> getcontentbyuser(String flag) throws ServiceException;
	/**
	 * 获取多条记录的内容,根据flags是一数组
	 * 
	 * @param
	 *          
	 * @return 返回内容
	 * @throws ServiceException
	 *             如果操作过程中出现异常，则抛出此封装异常。
	 */
	public ResponseObject<List<String>> getcontenstbyuser(String[] flags) throws ServiceException;

	public ResponseObject<String> getByFlag(String flag) throws ServiceException;
	
	public ResponseObject<List<globalargsExport>> getargsbyindexs() throws ServiceException;
	
	public ResponseObject<List<globalargsIndex>> getindex() throws ServiceException;
	
	public ResponseObject<List<globalargs>> getargsbyindex(String index) throws ServiceException;
	
}

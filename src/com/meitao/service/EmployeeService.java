package com.meitao.service;


import java.util.List;

import com.meitao.exception.ServiceException;
import com.meitao.model.Authority_url;
import com.meitao.model.Employee;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;



public interface EmployeeService {
	/**
	 * 根据用户名和密码进行登录，如果登录成功，则返回500，否则返回其他值。<br/>
	 * 如果在此过程中出现异常，则抛出ServiceException
	 * 
	 * @param accountName
	 * @param password
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Employee> checkLogin(String accountName, String password) throws ServiceException;

	public ResponseObject<Object> checkEmployee(String id, String password) throws ServiceException;

	/**
	 * 添加员工到数据库中，如果添加成功返回500.否则返回其他code
	 * 
	 * @param employee
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Object> addEmployee(Employee employee,List<String> authorityids) throws ServiceException;

	public ResponseObject<Object> modifyEmployee(Employee employee,List<String> authorityids) throws ServiceException;

	public ResponseObject<Object> modifyPassword(String id, String password, String oldpwd) throws ServiceException;

	public ResponseObject<Object> deleteEmployee(String id) throws ServiceException;

	public ResponseObject<Employee> getById(String id) throws ServiceException;

	public ResponseObject<Employee> getByAccoutName(String accountName) throws ServiceException;

	public ResponseObject<PageSplit<Employee>> searchAll(int pageSize, int pageNow,String groupid,String empid) throws ServiceException;
	
	
	public ResponseObject<PageSplit<Employee>> searchAllbyadmin(int pageSize, int pageNow,String keyword,String storeId,String empid) throws ServiceException;
	
	/**
	 * 添加一个职工权限到数据库中
	 * 
	 * @param authority
	 *            要添加的职工权限
	 * @return 如果添加成功，则返回true；否则返回false。
	 * @throws ServiceException
	 *             如果操作过程中出现异常，则抛出此封装异常。
	 */
	public ResponseObject<Object> addAuthorityEmplyees( List<String> authority)throws ServiceException;
			
	
	/**
	 * 根据员工ID修改权限信息
	 */
	public ResponseObject<Object> modifyAuthority(String eid, List<String> authoritys) throws ServiceException ;
	/**
	 * 根据员工ID删除权限
	 */
	public ResponseObject<Object> deleteAuthority(String id) throws ServiceException;
	
	//根据员工ID获取员工权限对应的可操作url列表
	public ResponseObject<List<Authority_url>> getMenuInfoByEmployeeId(String employeeId)
	throws ServiceException;

	public ResponseObject<List<Authority_url>> getAllMenuInfo() throws ServiceException;
	
	public ResponseObject<String[]> getRealTimeCount4AdminNav(String warehouseId) throws ServiceException;

}

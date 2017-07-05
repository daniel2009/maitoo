package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.meitao.model.Employee;


public interface EmployeeDao {
	/**
	 * 根据账户名获取员工信息
	 * 
	 * @param account
	 * @return
	 * @throws Exception
	 */
	public Employee getEmployeeByAccount(@Param("account") String account) throws Exception;

	public Employee getEmployeeById(@Param("id") String id) throws Exception;

	/**
	 * 添加职员到数据库中，返回受影响的行，同时将产生的主键id赋值给参数employee。
	 * 
	 * @param employee
	 * @return
	 * @throws Exception
	 */
	public int insertEmployee(Employee employee) throws Exception;

	public int updateEmployee(Employee employee) throws Exception;

	public int updatePassword(@Param("id") String id, @Param("password") String password, @Param("oldpwd") String oldpwd)
	        throws Exception;

	public int deleteEmployee(@Param("id") String id) throws Exception;
	
	public int deleteEmployeebystoreId(@Param("storeId") String storeId) throws Exception;	
	

	public int count() throws Exception;

	public List<Employee> searchAllEmployee(@Param("index") int index, @Param("size") int size, @Param("groupid") String groupId) throws Exception;
	
	/**
	 * 张敬琦
	 * 2015-01-23
	 * 添加职工与权限的关系到数据库，如果成功返回受影响的行
	 * 如果失败返回异常
	 */
	public int insert(@Param("id") String id ) throws Exception;
	
	public int countbyadmin(@Param("id") String id,@Param("keyword") String keyword,@Param("storeId") String storeId) throws Exception;

	public List<Employee> searchAllbyadmin(@Param("id") String id,@Param("keyword") String keyword,@Param("storeId") String storeId,@Param("index") int index, @Param("size") int size) throws Exception;
}

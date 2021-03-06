//判断其他网站登陆信息
//张敬琦
//2015-012-1


package com.meitao.service;

import java.util.List;

import com.meitao.exception.ServiceException;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.User;


public interface UserService {
	//public ResponseObject<User> getUserByAccount(String username, String email) throws ServiceException;

	/**
	 * 根据手机号码，检测数据库中是否存在一样用户名的记录，如果有，则返回true。否则返回false。
	 * 
	 * @param phone
	 * @return
	 * @throws ServiceException
	 */
	public boolean checkExistsByPhone(String phone) throws ServiceException;
	
	/**
	 * 根据账号，检测数据库中是否存在一样用户名的记录，如果有，则返回true。否则返回false。
	 * 
	 * @param email
	 * @return
	 * @throws ServiceException
	 */
	public boolean checkExistsByEmail(String email) throws ServiceException;

	/**
	 * 根据用户名检测数据库中有没有对应的数据，如果有，则返回true。否则返回false。
	 * 
	 * @param name
	 * @return
	 * @throws ServiceException
	 */
	//public boolean checkExistsByName(String name) throws ServiceException;

	/**
	 * 根据账号和密码进行登录校验。校验成功返回200.否则返回其他code
	 * 
	 * @param account
	 *            账号，即用户名或者email
	 * @param password
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<User> checkLogin(String account, String password) throws ServiceException;

	/**
	 * 根据用户id获取用户信息，如果获取成功，并且数据库中有数据，则返回200.否则返回其他code值<br/>
	 * 如果在此过程中出现异常，则抛出ServiceException
	 * 
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<User> getUserById(String id) throws ServiceException;
	
	
	public ResponseObject<User> getUserByPhone(String phone) throws ServiceException;


	/**
	 * 添加用户user到数据库中去。
	 * 
	 * @param user
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Object> addUser(User user) throws ServiceException;

	/**
	 * 根据id删除用户
	 * 
	 * @param ids
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Object> deleteUserByIds(String[] ids) throws ServiceException;

	/**
	 * 修改user用户数据
	 * 
	 * @param user
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Object> modifyUser(User user) throws ServiceException;

	/**
	 * 修改用户的密码
	 * 
	 * @param id
	 *            TODO
	 * @param phone
	 * @param password
	 * @param oldPassword
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Object> modifyPassword(String id, String phone, String password, String oldPassword)
	        throws ServiceException;
	
	
	/**
	 * 用户登陆后修改用户的密码
	 * 
	 * @param id
	 *            TODO
	 * @param phone
	 * @param password
	 * @param oldPassword
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Object> modifyPasswordbyuser(String id, String password, String oldPassword)
	        throws ServiceException;

	/**
	 * 在column字段上根据key查询进行模糊查询
	 * 
	 * @param userId
	 *            TODO
	 * @param key
	 * @param searchColumn
	 * @param pageSize
	 * @param pageNow
	 * 
	 * @param <T>
	 * @return
	 * @throws ServiceException
	 */
	public <T> ResponseObject<PageSplit<T>> searchByKey(String userId, String key, String searchColumn, int pageSize,
	        int pageNow,String groupId) throws ServiceException;

	/**
	 * 获取sdate到edate时间端中，用户类型为type的用户。
	 * 
	 * @param sdate
	 * @param edate
	 * @param type
	 * @return
	 * @throws ServiceException
	 */
	public List<User> getExportUserDatas(String sdate, String edate, String type, String groupid) throws ServiceException;

	public List<User> getExportUserAllDatas(String sdate, String edate, String type)
		    throws ServiceException;

	public ResponseObject<Object> modifyPasswordByEmail(String id,
			String email, String password, String oldPassword)throws ServiceException;
	
	/**
	 * 用户修改电话
	 */
	public ResponseObject<Object> modifyphonebyuser(User user)
	        throws ServiceException;
	/**
	 * 用户修改邮箱
	 */
	public ResponseObject<Object> modifyemailbyuser(User user)
	        throws ServiceException;

	public ResponseObject<String[]> getNeedFocusCount(String userId) throws ServiceException;

	public ResponseObject<String[]> getRealTimeCount4UserNav(String userId) throws ServiceException;
	
	
	
	//查找会员信息
	public <T> ResponseObject<PageSplit<T>> searchByinfo(String userinfo, int pageSize,
	        int pageNow) throws ServiceException;
	
	public <T> ResponseObject<PageSplit<T>> searchByadminwithkeyword(String userId, String key, String type, int pageSize,
	        int pageNow) throws ServiceException;
	//转运通过用户标识和用户代码来查找信息
	public <T> ResponseObject<PageSplit<T>> searchByinfouseforzy(String userinfo, int pageSize,
	        int pageNow) throws ServiceException;
}

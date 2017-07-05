//判断其他网站登陆信息
//张敬琦
//2015-012-1


package com.meitao.service;



import com.meitao.exception.ServiceException;



public interface CodeService {
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


}

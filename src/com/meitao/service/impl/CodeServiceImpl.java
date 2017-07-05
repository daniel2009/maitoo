package com.meitao.service.impl;









import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitao.dao.AccountDao;
import com.meitao.dao.AccountDetailDao;
import com.meitao.dao.ConsigneeInfoDao;
import com.meitao.dao.LoginInfoDao;
import com.meitao.dao.UserDao;
import com.meitao.service.CodeService;

import com.meitao.common.util.ExceptionUtil;

import com.meitao.common.util.StringUtil;

import com.meitao.exception.ServiceException;

import com.meitao.model.User;

@Service("codeService")
public class CodeServiceImpl extends BasicService implements CodeService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private AccountDao accountDao;
	@Autowired
	private AccountDetailDao accountDetailDao;
	@Autowired
	private LoginInfoDao loginInfoDao;
	@Autowired
	private ConsigneeInfoDao consigneeInfoDao;



	public boolean checkExistsByPhone(String phone) throws ServiceException {
		if (StringUtil.isEmpty(phone)) {
			return false;
		} else {
			try {
				//User user = this.userDao.getUserByAccount(phone);	
				User user = this.userDao.getUserByPhone(phone);
				if (user != null && phone.equals(user.getPhone())) {
					// this user is exists
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
	}
	
	public boolean checkExistsByEmail(String email) throws ServiceException {
		if (StringUtil.isEmpty(email)) {
			return false;
		} else {
			try {
				User user = this.userDao.getUserByEmailAccount(email);
				if (user != null && email.equalsIgnoreCase(user.getEmail())) {
					// this user is exists
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
	}
	
}

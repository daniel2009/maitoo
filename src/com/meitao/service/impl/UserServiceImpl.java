package com.meitao.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitao.dao.AccountDao;
import com.meitao.dao.AccountDetailDao;
import com.meitao.dao.CallOrderDao;
import com.meitao.dao.ConsigneeInfoDao;
import com.meitao.dao.LoginInfoDao;

import com.meitao.dao.RenlingDao;
import com.meitao.dao.RenlingPersonDao;
import com.meitao.dao.ReturnPackageDao;
import com.meitao.dao.TranshipmentBillDao;
import com.meitao.dao.UserDao;
import com.meitao.service.AutoSendService;
import com.meitao.service.UserService;

import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.common.util.MD5Util;
import com.meitao.common.util.StringUtil;

import com.meitao.exception.ServiceException;
import com.meitao.model.Account;
import com.meitao.model.Order;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.User;

@Service("userService")
public class UserServiceImpl extends BasicService implements UserService {
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
	@Autowired
	private TranshipmentBillDao transhipmentBillDao;
	@Autowired
	private ReturnPackageDao returnPackageDao;
	@Autowired
	private RenlingPersonDao renlingPersonDao;

	@Autowired
	private CallOrderDao callOrderDao;
	@Autowired
	private RenlingDao renlingDao;
	@Resource(name="autoSendService")
	private AutoSendService autoSendService;

	public ResponseObject<User> getUserById(String id) throws ServiceException {
		ResponseObject<User> result = new ResponseObject<User>();
		if (StringUtil.isEmpty(id)) {
			result.setCode(ResponseCode.PARAMETER_ERROR);
			result.setMessage("参数无效");
		} else {
			try {
				User user = this.userDao.getUserById(id);
				if (user != null) {
					result.setCode(ResponseCode.SUCCESS_CODE);
					result.setData(getSecurityValue(user));
				} else {
					result.setCode(ResponseCode.USER_ID_EXISTS);
					result.setMessage("数据库中没有对应id的用户，ID:" + id);
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return result;
	}

	public ResponseObject<Object> addUser(User user) throws ServiceException {
		ResponseObject<Object> responseObj = new ResponseObject<Object>();
		if (null == user) {
			responseObj.setCode(ResponseCode.PARAMETER_ERROR);
			responseObj.setMessage("参数无效");
		} else {
			String date = DateUtil.date2String(new Date());
			//String password = user.getPassword();
			user.setPassword(MD5Util.encode(user.getPassword()));
			user.setSignDate(date);
			
			String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			while (true) {
				String usercode = "";
				String useralias = "";

				for (int i = 0; i < 6; i++) {
					useralias += chars.charAt((int) (Math.random() * 26));
					usercode += (int) (Math.random() * 10);
				}
				User tmp=null;
				try {
					tmp = this.userDao.getUserByUserCodeOrUserAlias(usercode,useralias);
				} catch (Exception e) {
					
					throw ExceptionUtil.handle2ServiceException("生成收件标识与收件代码异常，请稍后重试", e);
				}
				if (tmp == null) {
					// this usercode or useralias is no exists
					user.setUsercode(usercode);
					user.setUseralias(useralias);
					break;
				}
			}
			
			try {
				int iresult = this.userDao.insertUser(user);
				if (iresult > 0) {
					// 创建一个账户
					Account account = new Account();
					account.setUserId(user.getId());
					account.setRmb("0");
					account.setUsd("0");
					account.setModifyDate(date);
					account.setCreateDate(date);
					this.accountDao.insertOrUpdateAccount(account);
					Map<String, String> map = new HashMap<String, String>();
					map.put("id", user.getId());
					responseObj.setCode(ResponseCode.SUCCESS_CODE);
					responseObj.setData(map);
					//if(user.getPhone()!=null&&!user.getPhone().equals(""))
						//this.autoSendService.send(user, Constant.AUTO_SEND_NEW_USER_PASSWORD, password);
//						SmsSendUtil.sendAdminAddUserMsg(password, user.getPhone());
				} else {
					responseObj.setCode(ResponseCode.USER_INSERT_ERROR);
					responseObj.setMessage("添加新用户保存到数据库中失败");
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return responseObj;
	}

	public ResponseObject<User> checkLogin(String account, String password) throws ServiceException {
		ResponseObject<User> result = new ResponseObject<User>();
		try {
			String pwd = MD5Util.encode(password);
			User user = this.userDao.getUserByAccount(account);
			if (user != null && pwd.equals(user.getPassword())
			        && (account.equals(user.getPhone()) ||account.equals(user.getEmail()))) {
				result.setCode(ResponseCode.SUCCESS_CODE);
				result.setData(getSecurityValue(user));
				try {
					this.loginInfoDao.insertUpdate(user.getId(), "0", DateUtil.date2String(new Date()));
				} catch (Exception e) {
					// ignore
				}
			} else {
				result.setCode(ResponseCode.USER_LOGIN_ACCOUNT_ERROR);
				result.setMessage("账户或者密码错误");
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException("检验用户登录失败", e);
		}
		return result;
	}

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

	/*public boolean checkExistsByName(String name) throws ServiceException {
		if (StringUtil.isEmpty(name)) {
			return false;
		} else {
			try {
				User user = this.userDao.getUserByAccount(name);
				if (user != null && name.equalsIgnoreCase(user.getNickName())) {
					// this user is exists
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
	}*/
	
	public ResponseObject<Object> modifyUser(User user) throws ServiceException {
		ResponseObject<Object> result = new ResponseObject<Object>();
		if (user == null) {
			result.setCode(ResponseCode.PARAMETER_ERROR);
			result.setMessage("修改用户参数错误");
		} else {
			try {
				int flag=0;
				if((user.getEmail()!=null)&&( ! user.getEmail().equalsIgnoreCase("")))
				{
					List<User> list=this.userDao.checkexistsforemail(user.getEmail());
					if((list!=null)&&(list.size()==1))
					{
						if(list.get(0).getEmail().equalsIgnoreCase(user.getEmail()))
						{
							
						}
						else
						{
							flag=1;
							result.setMessage("修改用户信息失败，邮箱已经存在！");
						}
						
					}
					else if((list!=null)&&(list.size()>1))
					{
						flag=1;
						result.setMessage("修改用户信息失败，邮箱已经存在！");
					}
					else
					{
						
					}
				}
				
				if(flag==0)
				{
					if((user.getPhone()!=null)&&( ! user.getPhone().equalsIgnoreCase("")))
					{
						List<User> list=this.userDao.checkexistsforphone(user.getPhone());
						if((list!=null)&&(list.size()==1))
						{
							if(list.get(0).getPhone().equalsIgnoreCase(user.getPhone()))
							{
								
							}
							else
							{
								flag=1;
								result.setMessage("修改用户信息失败，联系电话已经存在！");
							}
							
						}
						else if((list!=null)&&(list.size()>1))
						{
							flag=1;
							result.setMessage("修改用户信息失败，联系电话已经存在！");
						}
						else
						{
							
						}
					}
				}
				if(flag!=0)
				{
					result.setCode(ResponseCode.USER_MODIFY_FAILURE);
					
				}
				else
				{
					int iresult = this.userDao.updateUserById(user);
					if (iresult > 0) {
						result.setCode(ResponseCode.SUCCESS_CODE);
					} else {
						result.setCode(ResponseCode.USER_MODIFY_FAILURE);
						result.setMessage("修改用户信息失败，请重新尝试！");
					}
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return result;
	}

	public ResponseObject<Object> modifyPassword(String id, String phone, String password, String oldPassword)
	        throws ServiceException {
		ResponseObject<Object> result = new ResponseObject<Object>();
		if (StringUtil.isEmpty(password)) {
			result.setCode(ResponseCode.PARAMETER_ERROR);
			result.setMessage("无效的参数");
		} else {
			try {
				int iresult = this.userDao.updatePassword(id, phone, MD5Util.encode(password), MD5Util
				        .encode(oldPassword));
				if (iresult > 0) {
					result.setCode(ResponseCode.SUCCESS_CODE);
				} else {
					result.setCode(ResponseCode.USER_MODIFY_FAILURE);
					result.setMessage("数据库中没有对应账号的用户");
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return result;
	}
	
	public ResponseObject<Object> modifyPasswordByEmail(String id, String email, String password, String oldPassword)
	        throws ServiceException {
		ResponseObject<Object> result = new ResponseObject<Object>();
		if (StringUtil.isEmpty(password)) {
			result.setCode(ResponseCode.PARAMETER_ERROR);
			result.setMessage("无效的参数");
		} else {
			try {
				int iresult = this.userDao.updatePasswordByEmail(id, email, MD5Util.encode(password), MD5Util
				        .encode(oldPassword));
				if (iresult > 0) {
					result.setCode(ResponseCode.SUCCESS_CODE);
				} else {
					result.setCode(ResponseCode.USER_MODIFY_FAILURE);
					result.setMessage("数据库中没有对应账号的用户");
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return result;
	}

	public ResponseObject<Object> deleteUserByIds(String[] ids) throws ServiceException {
		ResponseObject<Object> result = new ResponseObject<Object>();
		if (ids == null || ids.length == 0) {
			result.setCode(ResponseCode.PARAMETER_ERROR);
			result.setMessage("参数无效");
		} else {
			try {
				List<String> userIds = Arrays.asList(ids);
				int iresult = this.userDao.deleteUserByIds(userIds);
				if (iresult > 0 && iresult == userIds.size()) {
					this.accountDao.deleteAccountByUserIds(userIds);
					this.accountDao.deleteCreditByUserIds(userIds);
					this.accountDetailDao.deleteAccountDetailByUserIds(userIds);
					this.consigneeInfoDao.deleteConsigneeInfoByUserIds(userIds);
					result.setCode(ResponseCode.SUCCESS_CODE);
				} else {
					// 进行事务回滚
					throw new Exception();
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public <T> ResponseObject<PageSplit<T>> searchByKey(String userId, String key, String searchColumn, int pageSize, int pageNow, String groupid)
	        throws ServiceException {
		try {
			key = StringUtil.escapeStringOfSearchKey(key);
			int rowCount = 0;
			try {
				rowCount = this.userDao.countByKey(userId, key, searchColumn);
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("获取用户个数失败", e);
			}

			ResponseObject<PageSplit<T>> responseObj = new ResponseObject<PageSplit<T>>(ResponseCode.SUCCESS_CODE);
			if (rowCount > 0) {
				pageSize = Math.max(pageSize, 1);
				int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
				pageNow = Math.min(pageNow, pageCount);
				PageSplit<T> pageSplit = new PageSplit<T>();
				pageSplit.setPageCount(pageCount);
				pageSplit.setPageNow(pageNow);
				pageSplit.setRowCount(rowCount);
				pageSplit.setPageSize(pageSize);

				int startIndex = (pageNow - 1) * pageSize;
				try {
					List<User> users = this.userDao.searchUserByKey(userId, key, searchColumn, startIndex, pageSize, groupid);
					if (users != null && !users.isEmpty()) {
						for (User u : users) {
							pageSplit.addData((T) getSecurityValue(u));
						}
					}
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("获取用户列表失败", e);
				}
				responseObj.setData(pageSplit);
			} else {
				responseObj.setMessage("没有用户");
			}
			return responseObj;
		} catch (ServiceException e) {
			throw e;
		}
	}

	public List<User> getExportUserDatas(String sdate, String edate, String type, String groupid) throws ServiceException {
		try {
			return this.userDao.getExportUsersBySignDate(sdate, edate, type, groupid);
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	
	@Override
	public List<User> getExportUserAllDatas(String sdate, String edate,
			String type) throws ServiceException {
		try {
			return this.userDao.getExportAllUsersBySignDate(sdate, edate, type);
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	/*
	@Override
	public ResponseObject<User> getUserByAccount(String username, String email)
			throws ServiceException {
		try{
			User user=null;
			if(!StringUtil.isEmpty(email)) {
				  user = this.userDao.getUserByAccount(email);
				}

				if (user==null) {
				  user = this.userDao.getUserByAccount(username);
				}

				if (user==null) {
					return new ResponseObject<User>(ResponseCode.PARAMETER_ERROR,"用户不存在");
					}else{
						return new ResponseObject<User>(ResponseCode.SUCCESS_CODE, "success", user);	
					}
			}catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}

	  */

	@Override
	public ResponseObject<User> getUserByPhone(String phone)
			throws ServiceException {
		ResponseObject<User> result = new ResponseObject<User>();
		if (StringUtil.isEmpty(phone)) {
			result.setCode(ResponseCode.PARAMETER_ERROR);
			result.setMessage("参数无效");
		} else {
			try {
				User user = this.userDao.getUserByAccount(phone);
				if (user != null) {
					result.setCode(ResponseCode.SUCCESS_CODE);
					result.setData(getSecurityValue(user));
				} else {
					result.setCode(ResponseCode.USER_ID_EXISTS);
					result.setMessage("数据库中没有对应用户名的用户，用户名:" + phone);
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return result;
	}

	
	public ResponseObject<Object> modifyPasswordbyuser(String id, String password, String oldPassword)
	        throws ServiceException {
		ResponseObject<Object> result = new ResponseObject<Object>();
		if (StringUtil.isEmpty(password)) {
			result.setCode(ResponseCode.PARAMETER_ERROR);
			result.setMessage("无效的参数");
		} else {
			try {
				int iresult = this.userDao.updatePasswordbyuser(id, MD5Util.encode(password), MD5Util.encode(oldPassword));
						
				if (iresult > 0) {
					result.setCode(ResponseCode.SUCCESS_CODE);
				} else {
					result.setCode(ResponseCode.USER_MODIFY_FAILURE);
					result.setMessage("数据库中没有对应账号的用户或用户密码输入不对!");
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return result;
	}
	
	public ResponseObject<Object> modifyphonebyuser(User user)
	        throws ServiceException{
		ResponseObject<Object> result = new ResponseObject<Object>();
		if (StringUtil.isEmpty(user.getPhone())) {
			result.setCode(ResponseCode.PARAMETER_ERROR);
			result.setMessage("无效的参数");
		} else {
			try {
				List<User> list=this.userDao.checkexistsforphone(user.getPhone());
						if((list!=null)&&(list.size()==1))
						{
							User user1=list.get(0);
							if(!user.getEmail().equalsIgnoreCase(user1.getEmail()))
							{
								result.setCode(ResponseCode.USER_MODIFY_FAILURE);
								result.setMessage("数据库中已经存在此电话!");
							}
							else
							{
								int iresult = this.userDao.updatephoneById(user);
								
								if (iresult > 0) {
									result.setCode(ResponseCode.SUCCESS_CODE);
								} else {
									result.setCode(ResponseCode.USER_MODIFY_FAILURE);
									result.setMessage("数据库中没有对应账号的用户或用户密码输入不对!");
								}
							}
						}
						else if((list!=null)&&(list.size()>1))
						{
							result.setCode(ResponseCode.USER_MODIFY_FAILURE);
							result.setMessage("数据库中已经存在此电话!");
						}
						else
						{
							int iresult = this.userDao.updatephoneById(user);
									
							if (iresult > 0) {
								result.setCode(ResponseCode.SUCCESS_CODE);
							} else {
								result.setCode(ResponseCode.USER_MODIFY_FAILURE);
								result.setMessage("数据库中没有对应账号的用户或用户密码输入不对!");
							}
						}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return result;
		
	}
	public ResponseObject<Object> modifyemailbyuser(User user)
	        throws ServiceException{
		ResponseObject<Object> result = new ResponseObject<Object>();
		if (StringUtil.isEmpty(user.getEmail())) {
			result.setCode(ResponseCode.PARAMETER_ERROR);
			result.setMessage("无效的参数");
		} else {
			try {
				List<User> list=this.userDao.checkexistsforemail(user.getEmail());
				if((list!=null)&&(list.size()==1))
				{
					User user1=list.get(0);
					if(!user.getEmail().equalsIgnoreCase(user1.getEmail()))
					{
						result.setCode(ResponseCode.USER_MODIFY_FAILURE);
						result.setMessage("数据库中已经存在此邮箱!");
					}
					else
					{
						int iresult = this.userDao.updateEmailById(user);
						
						if (iresult > 0) {
							result.setCode(ResponseCode.SUCCESS_CODE);
						} else {
							result.setCode(ResponseCode.USER_MODIFY_FAILURE);
							result.setMessage("数据库中没有对应账号的用户或用户密码输入不对!");
						}
					}
				}
				else if((list!=null)&&(list.size()>1))
				{
					result.setCode(ResponseCode.USER_MODIFY_FAILURE);
					result.setMessage("数据库中已经存在此邮箱!");
				}
				else
				{
					int iresult = this.userDao.updateEmailById(user);
							
					if (iresult > 0) {
						result.setCode(ResponseCode.SUCCESS_CODE);
					} else {
						result.setCode(ResponseCode.USER_MODIFY_FAILURE);
						result.setMessage("数据库中没有对应账号的用户或用户密码输入不对!");
					}
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return result;
		
	}

	@Override
	public ResponseObject<String[]> getNeedFocusCount(String userId) throws ServiceException {
		ResponseObject<String[]> responseObject = new ResponseObject<String[]>();
		if (StringUtil.isEmpty(userId)) {
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("无效的参数");
		} else {
			try {
				String[] array = new String[14];
				int i = 0;
				//1
				String countTranshipmentState1 = String.valueOf(this.transhipmentBillDao.countByKey(null, null, null, null, null, userId, "1", null));
				array[i++] = "countTranshipmentState1";
				array[i++] = countTranshipmentState1;
				array[i++] = "countTranshipmentBillState1InNav";
				array[i++] = countTranshipmentState1;
				array[i++] = "countTranshipmentState3";
				array[i++] = String.valueOf(this.transhipmentBillDao.countByKey(null, null, null, null, null, userId, "3", null));
				array[i++] = "countReturnPackageState2";
				array[i++] = String.valueOf(this.returnPackageDao.countByKey(null, null, null, userId, "2", null, null));
				array[i++] = "countClaimPackageState4";
				array[i++] = String.valueOf(this.renlingPersonDao.countByKey(userId, "4"));
				//String countOrderState1 = String.valueOf(this.orderDao.countOfSearchKeys(null, null, null, null, null, userId, "1", null,null));
				//11
				//array[i++] = "countOrderState1";
				//array[i++] = countOrderState1;
				//array[i++] = "countOrderState1InNav";
				//array[i++] = countOrderState1;
				responseObject.setCode(ResponseCode.SUCCESS_CODE);
				responseObject.setData(array);
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return responseObject;
	}

	@Override
	public ResponseObject<String[]> getRealTimeCount4UserNav(String userId) throws ServiceException {
		ResponseObject<String[]> responseObject = new ResponseObject<String[]>();
		if (StringUtil.isEmpty(userId)) {
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("无效的参数");
		} else {
			try {
				Order order = new Order();
				order.setUserId(userId);
				order.setState("-10");
				String[] array = new String[16];
				int i = 0;
				//1
				array[i++] = "countCallOrderInNav";
				array[i++] = String.valueOf(this.callOrderDao.countAllByUserId(userId));
				array[i++] = "countTranshipmentBillInNav";
				array[i++] = String.valueOf(this.transhipmentBillDao.countByKey(null, null, null, null, null, userId, null, null));
				array[i++] = "countReturnPackageInNav";
				array[i++] = String.valueOf(this.returnPackageDao.countByKey(null, null, null, userId, null, null, null));
				array[i++] = "countTranshipmentBillStateN10InNav";
				array[i++] = String.valueOf(this.transhipmentBillDao.countByKey(null, null, null, null, null, userId, "-10", null));
				array[i++] = "countOrderAfterState2InNav";
				//order.setState(Constant.ORDER_STATE3);
				//array[i++] = String.valueOf(this.orderDao.countByAfterState(order));
				//11
				//array[i++] = "countOrderInNav";
				//array[i++] = String.valueOf(this.orderDao.countOfUserId(userId));
				array[i++] = "countClaimPackageInNav";
				array[i++] = String.valueOf(this.renlingDao.countByNotClaimedAndUser(userId, "%"));
				array[i++] = "countTranshipmentBillState3InNav";
				array[i++] = String.valueOf(this.transhipmentBillDao.countByKey(null, null, null, null, null, userId, "3", null));
				responseObject.setCode(ResponseCode.SUCCESS_CODE);
				responseObject.setData(array);
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return responseObject;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public <T> ResponseObject<PageSplit<T>> searchByinfo(String userinfo, int pageSize,
	        int pageNow) throws ServiceException {
		try {
			String id=null;
			if(StringUtil.isEmpty(userinfo))
			{
				userinfo=null;
			}
			else
			{
				id=userinfo;
				userinfo=StringUtil.escapeStringOfSearchKey(userinfo);
			}
			
			int rowCount = 0;
			try {
				rowCount = this.userDao.countByInfo(id,userinfo);
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("获取用户个数失败", e);
			}

			ResponseObject<PageSplit<T>> responseObj = new ResponseObject<PageSplit<T>>(ResponseCode.SUCCESS_CODE);
			if (rowCount > 0) {
				pageSize = Math.max(pageSize, 1);
				int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
				pageNow = Math.min(pageNow, pageCount);
				PageSplit<T> pageSplit = new PageSplit<T>();
				pageSplit.setPageCount(pageCount);
				pageSplit.setPageNow(pageNow);
				pageSplit.setRowCount(rowCount);
				pageSplit.setPageSize(pageSize);

				int startIndex = (pageNow - 1) * pageSize;
				try {
					List<User> users = this.userDao.searchUserByInfo(id,userinfo, startIndex, pageSize);
							
					if (users != null && !users.isEmpty()) {
						for (User u : users) {
							pageSplit.addData((T) getSecurityValue(u));
						}
					}
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("获取用户列表失败", e);
				}
				responseObj.setData(pageSplit);
			} else {
				responseObj.setMessage("没有用户");
			}
			return responseObj;
		} catch (ServiceException e) {
			throw e;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> ResponseObject<PageSplit<T>> searchByinfouseforzy(String userinfo, int pageSize,
	        int pageNow) throws ServiceException {
		try {
			String id=null;
			if(StringUtil.isEmpty(userinfo))
			{
				userinfo=null;
			}
			else
			{
				id=userinfo;
				userinfo=StringUtil.escapeStringOfSearchKey(userinfo);
			}
			
			int rowCount = 0;
			try {
				rowCount = this.userDao.countByInfoforzy(id, userinfo);
						//.countByInfo(id,userinfo);
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("获取用户个数失败", e);
			}

			ResponseObject<PageSplit<T>> responseObj = new ResponseObject<PageSplit<T>>(ResponseCode.SUCCESS_CODE);
			if (rowCount > 0) {
				pageSize = Math.max(pageSize, 1);
				int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
				pageNow = Math.min(pageNow, pageCount);
				PageSplit<T> pageSplit = new PageSplit<T>();
				pageSplit.setPageCount(pageCount);
				pageSplit.setPageNow(pageNow);
				pageSplit.setRowCount(rowCount);
				pageSplit.setPageSize(pageSize);

				int startIndex = (pageNow - 1) * pageSize;
				try {
					List<User> users = this.userDao.searchUserByInfoforzy(id, userinfo, startIndex, pageSize);
							//.searchUserByInfo(id,userinfo, startIndex, pageSize);
							
					if (users != null && !users.isEmpty()) {
						for (User u : users) {
							pageSplit.addData((T) getSecurityValue(u));
						}
					}
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("获取用户列表失败", e);
				}
				responseObj.setData(pageSplit);
			} else {
				responseObj.setMessage("没有用户");
			}
			return responseObj;
		} catch (ServiceException e) {
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public <T> ResponseObject<PageSplit<T>> searchByadminwithkeyword(String userId, String key, String type, int pageSize,
	        int pageNow) throws ServiceException {
		try {
			if(StringUtil.isEmpty(key))
			{
				key = null;
			}
			else
			{
				key = StringUtil.escapeStringOfSearchKey(key);
			}
			int rowCount = 0;
			try {
				rowCount = this.userDao.countByKeyAdmin(userId, key, type);
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("获取用户个数失败", e);
			}

			ResponseObject<PageSplit<T>> responseObj = new ResponseObject<PageSplit<T>>(ResponseCode.SUCCESS_CODE);
			if (rowCount > 0) {
				pageSize = Math.max(pageSize, 1);
				int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
				pageNow = Math.min(pageNow, pageCount);
				PageSplit<T> pageSplit = new PageSplit<T>();
				pageSplit.setPageCount(pageCount);
				pageSplit.setPageNow(pageNow);
				pageSplit.setRowCount(rowCount);
				pageSplit.setPageSize(pageSize);

				int startIndex = (pageNow - 1) * pageSize;
				try {
					List<User> users=this.userDao.searchByKeyAdmin(userId, key, type, startIndex, pageSize);
					
					if (users != null && !users.isEmpty()) {
						for (User u : users) {
							pageSplit.addData((T) getSecurityValue(u));
						}
					}
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("获取用户列表失败", e);
				}
				responseObj.setData(pageSplit);
			} else {
				responseObj.setMessage("没有用户");
			}
			return responseObj;
		} catch (ServiceException e) {
			throw e;
		}
	}
}

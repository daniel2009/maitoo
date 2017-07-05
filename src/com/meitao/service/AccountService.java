package com.meitao.service;

import java.util.List;

import com.meitao.exception.ServiceException;
import com.meitao.model.Account;
import com.meitao.model.AccountDetail;
import com.meitao.model.Credit;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.User;


public interface AccountService {
	public ResponseObject<Object> addCredit(Credit credit) throws ServiceException;

	public ResponseObject<Object> addAccountDetail(AccountDetail detail) throws ServiceException;

	public ResponseObject<List<Credit>> getCreditsByUserId(String userId) throws ServiceException;

	public ResponseObject<Credit> getCreditById(String id, String userId) throws ServiceException;

	public ResponseObject<Object> modifyAccountOfAdmin(AccountDetail detail, Account newAccount)
	        throws ServiceException;

	public ResponseObject<AccountDetail> getAccountDetailById(String id) throws ServiceException;

	public ResponseObject<Object> modifyAccountDetail(AccountDetail detail) throws ServiceException;
	
	public ResponseObject<Object> insertAccountDetail(AccountDetail detail) throws ServiceException;
	
	public ResponseObject<Object> procedure(AccountDetail detail) throws ServiceException;
	
	
	public ResponseObject<PageSplit<AccountDetail>> search(String userId, String key, int pageSize, int pageNow,
	        String state, String type,String groupid) throws ServiceException;

	public ResponseObject<Account> getAccountByUserId(String userId) throws ServiceException;

	/**
	 * 
	 * @param userId
	 * @param amount
	 * @param modifyDate will use as flag for checkIfScanPay method
	 * @return
	 * @throws ServiceException
	 */
	ResponseObject<Object> rechargeRmb(String userId, double amount,
			String modifyDate) throws ServiceException;

	public ResponseObject<Object> addPayment(User user, String price, String currency, String detail) throws ServiceException;

	public ResponseObject<Object> checkBalanceEnough(User user, String paymentString, String currency) throws ServiceException;
	
	
	public ResponseObject<PageSplit<AccountDetail>> searchbyinfo(String userId, String info, int pageSize, int pageNow,
	        String state, String type) throws ServiceException;
	
	public ResponseObject<PageSplit<AccountDetail>> searchbyinfo_superadmin(String c_state,String s_storeId,String userId, String info, int pageSize, int pageNow,
	        String state, String type) throws ServiceException;
	//修改确认状态
	public ResponseObject<Object> changeconfirmstate(String id, String type, String confirm_state,String admin_remark) throws ServiceException;
	public ResponseObject<Object> getmanydetail(String[] ids) throws ServiceException;
	
	public ResponseObject<Object> getdetailNo() throws ServiceException;//获取统计信息
}

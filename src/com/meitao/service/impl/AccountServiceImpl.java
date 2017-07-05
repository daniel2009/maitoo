package com.meitao.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
























import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitao.dao.AccountDao;
import com.meitao.dao.AccountDetailDao;
import com.meitao.dao.UserDao;
import com.meitao.dao.globalargsDao;
import com.meitao.service.AccountService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.common.util.PaymentUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.exception.ServiceException;
import com.meitao.model.Account;
import com.meitao.model.AccountDetail;
import com.meitao.model.Credit;
import com.meitao.model.Employee;
import com.meitao.model.M_order;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.User;
import com.meitao.model.Warehouse;
import com.meitao.model.temp.MoneyDetailNo;

@Service("accountService")
public class AccountServiceImpl implements AccountService {
	@Autowired
	private AccountDao accountDao;
	@Autowired
	private AccountDetailDao accountDetailDao;
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private globalargsDao globalargsDao;

	public ResponseObject<Object> addAccountDetail(AccountDetail detail) throws ServiceException {
		try {
			String date = DateUtil.date2String(new Date());
			detail.setCreateDate(date);
			detail.setModifyDate(date);
			int i = this.accountDetailDao.insertAccountDetail(detail);
			if (i > 0) {
				ResponseObject<Object> result = new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", detail.getId());
				result.setData(map);
				return result;
			} else {
				return new ResponseObject<Object>(ResponseCode.ACCOUNT_INSERT_FAILURE, "插入数据库失败");
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}

	public ResponseObject<Object> addCredit(Credit credit) throws ServiceException {
		try {
			String date = DateUtil.date2String(new Date());
			credit.setModifyDate(date);
			int i = -1;
			if (StringUtil.isEmpty(credit.getId())) {
				i = this.accountDao.insertCredit(credit);
			} else {
				i = this.accountDao.updateCredit(credit);
			}
			if (i > 0) {
				ResponseObject<Object> result = new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", credit.getId());
				map.put("name", credit.getName());
				result.setData(map);
				return result;
			} else {
				return new ResponseObject<Object>(ResponseCode.ACCOUNT_INSERT_FAILURE, "插入数据库失败");
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}

	public ResponseObject<PageSplit<AccountDetail>> search(String userId, String key, int pageSize, int pageNow,
	        String state, String type, String groupid) throws ServiceException {
		try {
			key = StringUtil.isEmpty(key) ? null : StringUtil.escapeStringOfSearchKey(key);
			state = StringUtil.isEmpty(state) ? null : state.trim();
			type = StringUtil.isEmpty(type) ? null : type.trim();
			userId = StringUtil.isEmpty(userId) ? null : userId.trim();
			int rowCount = 0;
			try {
				rowCount = this.accountDetailDao.countByKey(userId, key, state, type, groupid);
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("获取消费详情记录数量失败", e);
			}

			ResponseObject<PageSplit<AccountDetail>> responseObj = new ResponseObject<PageSplit<AccountDetail>>(
			        ResponseCode.SUCCESS_CODE);
			if (rowCount > 0) {
				pageSize = Math.max(pageSize, 1);
				int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
				pageNow = Math.min(pageNow, pageCount);
				PageSplit<AccountDetail> pageSplit = new PageSplit<AccountDetail>();
				pageSplit.setPageCount(pageCount);
				pageSplit.setPageNow(pageNow);
				pageSplit.setRowCount(rowCount);
				pageSplit.setPageSize(pageSize);

				int startIndex = (pageNow - 1) * pageSize;
				try {
					List<AccountDetail> details = this.accountDetailDao.searchByKey(userId, key, state, type,
					        startIndex, pageSize);
					
					if (details != null && !details.isEmpty()) {
						for (AccountDetail detail : details) {
							
							if((detail.getUser()==null)&&(!StringUtil.isEmpty(detail.getUserId())))
							{
								detail.setUser(this.userDao.getUserById(detail.getUserId()));
							}
								
			
							
							pageSplit.addData(detail);
						}
					}
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("获取消费详情列表失败", e);
				}
				responseObj.setData(pageSplit);
			} else {
				responseObj.setMessage("没有消费记录");
			}
			return responseObj;
		} catch (ServiceException e) {
			throw e;
		}
	}

	public ResponseObject<AccountDetail> getAccountDetailById(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return new ResponseObject<AccountDetail>(ResponseCode.PARAMETER_ERROR, "参数无效");
		}

		try {
			AccountDetail detail = this.accountDetailDao.getById(id);
			if (detail != null) {
				detail.getUser().setPassword(null);
				ResponseObject<AccountDetail> result = new ResponseObject<AccountDetail>(ResponseCode.SUCCESS_CODE);
				result.setData(detail);
				return result;
			} else {
				return new ResponseObject<AccountDetail>(ResponseCode.PARAMETER_ERROR, "数据库中没有对应id的详情记录");
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}

	public ResponseObject<Object> modifyAccountDetail(AccountDetail detail) throws ServiceException {
		if (Constant.ACCOUNT_DETAIL_STATE1.equals(detail.getState())) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR, "参数无效");
		}

		try {
			String date = DateUtil.date2String(new Date());
			detail.setModifyDate(date);
			// modify the account detail state
			int i = this.accountDetailDao.modifyAccountDetail(detail);
			if (StringUtil.isEmpty(detail.getRealAmount())) {
				detail.setRealAmount(detail.getAmount());
			}
			if (i > 0) {
				if (Constant.ACCOUNT_DETAIL_STATE2.equals(detail.getState())) {
					String userId = detail.getUserId();
					Account account = new Account();
					account.setUserId(userId);
					if (Constant.ACCOUNT_DETAIL_TYPE11.equals(detail.getType())) {
						// 信用卡充值
						account.setUsd(detail.getRealAmount());
						account.setRmb("0");
					} else if (Constant.ACCOUNT_DETAIL_TYPE12.equals(detail.getType())) {
						// 人民币充值
						account.setUsd("0");
						account.setRmb(detail.getRealAmount());
					} else {
						throw new Exception();
					}
					account.setModifyDate(date);
					int k = this.accountDao.insertOrUpdateAccount(account);
					if (k > 0) {
						// pass
					} else {
						throw new Exception();
					}
				}
				return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			} else {
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR, "没有对应的消费详情或者是该操作以及执行过了");
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	
	public ResponseObject<Object> procedure(AccountDetail detail) throws ServiceException {
		try {
			String date = DateUtil.date2String(new Date());
			Account a = accountDao.getAccountByUserId(detail.getUserId());
			double usd= StringUtil.string2Double(a.getUsd());
			double newusd = usd - StringUtil.string2Double(detail.getAmount());
			// 账户支付，修改账户余额
			Account account = new Account();
			account.setUsd(String.valueOf(newusd));
			account.setRmb(a.getRmb());
			account.setUserId(a.getUserId());
			account.setModifyDate(date);
			if (this.accountDao.modifyAccount(account) > 0) {
				// pass
			} else {
				throw new Exception();
			}
			this.accountDetailDao.insertAccountDetail(detail);
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
		return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
	}
	
	
	public ResponseObject<Object> insertAccountDetail(AccountDetail detail) throws ServiceException {
		try {
			String date = DateUtil.date2String(new Date());
			detail.setModifyDate(date);
			// modify the account detail state
			int i = this.accountDetailDao.insertAccountDetail(detail);
			if (StringUtil.isEmpty(detail.getRealAmount())) {
				detail.setRealAmount(detail.getAmount());
			}
			if (i > 0) {
				if (Constant.ACCOUNT_DETAIL_STATE2.equals(detail.getState())) {
					String userId = detail.getUserId();
					Account account = new Account();
					account.setUserId(userId);
					if (Constant.ACCOUNT_DETAIL_TYPE11.equals(detail.getType())) {
						// 信用卡充值
						account.setUsd(detail.getRealAmount());
						account.setRmb("0");
					} else if (Constant.ACCOUNT_DETAIL_TYPE12.equals(detail.getType())) {
						// 人民币充值
						account.setUsd("0");
						account.setRmb(detail.getRealAmount());
					} else {
						throw new Exception();
					}
					account.setModifyDate(date);
					int k = this.accountDao.insertOrUpdateAccount(account);
					if (k > 0) {
						// pass
					} else {
						throw new Exception();
					}
				}
				return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			} else {
				return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION, "已经充值成功，但记录写入数据库失败，请联系客服，修改充值记录!");
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}

	public ResponseObject<Object> modifyAccountOfAdmin(AccountDetail detail, Account newAccount)
	        throws ServiceException {
		try {
			String date = DateUtil.date2String(new Date());
			detail.setCreateDate(date);
			detail.setModifyDate(date);
			newAccount.setModifyDate(date);
			if (Constant.ACCOUNT_DETAIL_TYPE11.equals(detail.getCurrency())) {
				// 美元
				detail.setCurrency("美元");
			} else if (Constant.ACCOUNT_DETAIL_TYPE12.equals(detail.getCurrency())) {
				// 人民币
				detail.setCurrency("人民币");
			} else {
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR, "充值的货币类型必须是美元或者人民币");
			}

			int i = this.accountDetailDao.insertAccountDetail(detail);
			if (i > 0) {
				int k = this.accountDao.insertOrUpdateAccount(newAccount);
				if (k > 0) {
					return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
				} else {
					throw new Exception();
				}
			} else {
				return new ResponseObject<Object>(ResponseCode.ACCOUNT_INSERT_FAILURE, "插入账户消费详情记录失败");
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}

	public ResponseObject<Account> getAccountByUserId(String userId) throws ServiceException {
		ResponseObject<Account> result = new ResponseObject<Account>();
		if (StringUtil.isEmpty(userId)) {
			result.setCode(ResponseCode.PARAMETER_ERROR);
			result.setMessage("会员编号不能为空");
		} else {
			try {
				Account account = this.accountDao.getAccountByUserId(userId);
				if (account != null) {
					result.setCode(ResponseCode.SUCCESS_CODE);
					result.setData(account);
				} else {
					result.setCode(ResponseCode.PARAMETER_ERROR);
					result.setMessage("数据库中没有对应用户id的账户记录");
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return result;
	}

	public ResponseObject<List<Credit>> getCreditsByUserId(String userId) throws ServiceException {
		if (StringUtil.isEmpty(userId)) {
			return new ResponseObject<List<Credit>>(ResponseCode.PARAMETER_ERROR, "参数无效");
		}
		try {
	        List<Credit> credits = this.accountDao.getCreditsByUserId(userId);
	        ResponseObject<List<Credit>> result = new ResponseObject<List<Credit>>(ResponseCode.SUCCESS_CODE);
	        result.setData(credits);
	        return result;
        } catch (Exception e) {
        	throw ExceptionUtil.handle2ServiceException(e);
        }
    }

	public ResponseObject<Credit> getCreditById(String id, String userId) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return new ResponseObject<Credit>(ResponseCode.PARAMETER_ERROR, "参数无效");
		}
		try {
	        Credit credit = this.accountDao.getCreditById(id);
	        if (credit != null && userId.equals(credit.getUserId())) {
	        	ResponseObject<Credit> result = new ResponseObject<Credit>(ResponseCode.SUCCESS_CODE);
	        	result.setData(credit);
	        	return result;
	        } else {
	        	// 没有数据
	        	return new ResponseObject<Credit>(ResponseCode.PARAMETER_ERROR, "数据库中没有对应的信用卡记录");
	        }
        } catch (Exception e) {
        	throw ExceptionUtil.handle2ServiceException(e);
        }
    }

	@Override
	public ResponseObject<Object> rechargeRmb(String userId, double amount, String modifyDate) throws ServiceException {
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		if(StringUtil.isEmpty(userId) || amount <= 0){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setCode("参数错误");
		}else{
			try {
				int result = this.accountDao.rechargeRmb(userId, String.valueOf(amount), modifyDate);
				if(result > 0){
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}else{
					responseObject.setCode(ResponseCode.ACCOUNT_RECHARGE_RMB_FAILURE);
					responseObject.setMessage("账户人民币充值失败，请联系客服");
				}
	        } catch (Exception e) {
	        	throw ExceptionUtil.handle2ServiceException(e);
	        }
		}
		
		return responseObject;
	}

	@Override
	public ResponseObject<Object> addPayment(User user, String paymentString, String currency, String detail) throws ServiceException {
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		String rateString = null;
		try {
			user = userDao.getUserById(user.getId());
			rateString = this.globalargsDao.getcontentbyflag("cur_usa_cn");
			if("RMB".equals(currency)){
				double rate = StringUtil.string2Double(rateString);
				rate = new BigDecimal(1 / rate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			}
			if(PaymentUtil.calculatePayment(user.getUsdBalance(), user.getRmbBalance(), paymentString, rateString)==null){
				throw new ServiceException("账户余额不足");
			}else{
				AccountDetail accountDetail = new AccountDetail();
				Account account = new Account();
				PaymentUtil.setAccountAndAccountDetailByPayment(account, accountDetail, user, paymentString, rateString, false, detail);
				accountDetailDao.insertAccountDetail(accountDetail);
				this.accountDao.modifyAccount(account);
			}
			responseObject.setCode(ResponseCode.SUCCESS_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
		return responseObject;
	}

	@Override
	public ResponseObject<Object> checkBalanceEnough(User user, String paymentString, String currency) throws ServiceException {
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		String rateString = null;
		try {
			user = userDao.getUserById(user.getId());
			rateString = this.globalargsDao.getcontentbyflag("cur_usa_cn");
			if("RMB".equals(currency)){
				double rate = StringUtil.string2Double(rateString);
				rate = new BigDecimal(1 / rate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			}
			if(PaymentUtil.calculatePayment(user.getUsdBalance(), user.getRmbBalance(), paymentString, rateString)==null){
				responseObject.setCode(ResponseCode.ACCOUNT_BALANCE_NOT_ENOUGH);
				responseObject.setMessage("账户余额不足");
			}else{
				responseObject.setCode(ResponseCode.SUCCESS_CODE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
		return responseObject;
	}
	
	
	
	
	public ResponseObject<PageSplit<AccountDetail>> searchbyinfo(String userId, String info, int pageSize, int pageNow,
	        String state, String type) throws ServiceException {
		try {
			info = StringUtil.isEmpty(info) ? null : StringUtil.escapeStringOfSearchKey(info);
			state = StringUtil.isEmpty(state) ? null : state.trim();
			type = StringUtil.isEmpty(type) ? null : type.trim();
			userId = StringUtil.isEmpty(userId) ? null : userId.trim();
			int rowCount = 0;
			try {
				rowCount=this.accountDetailDao.countByInfo(userId, info, state, type);
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("获取消费详情记录数量失败", e);
			}

			ResponseObject<PageSplit<AccountDetail>> responseObj = new ResponseObject<PageSplit<AccountDetail>>(
			        ResponseCode.SUCCESS_CODE);
			if (rowCount > 0) {
				pageSize = Math.max(pageSize, 1);
				int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
				pageNow = Math.min(pageNow, pageCount);
				PageSplit<AccountDetail> pageSplit = new PageSplit<AccountDetail>();
				pageSplit.setPageCount(pageCount);
				pageSplit.setPageNow(pageNow);
				pageSplit.setRowCount(rowCount);
				pageSplit.setPageSize(pageSize);

				int startIndex = (pageNow - 1) * pageSize;
				try {
					List<AccountDetail> details=this.accountDetailDao.searchByInfo(userId, info, state, type, startIndex, pageSize);
					
					
					if (details != null && !details.isEmpty()) {
						for (AccountDetail detail : details) {
							
							if((detail.getUser()==null)&&(!StringUtil.isEmpty(detail.getUserId())))
							{
								detail.setUser(this.userDao.getUserById(detail.getUserId()));
							}
								
			
							
							pageSplit.addData(detail);
						}
					}
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("获取消费详情列表失败", e);
				}
				responseObj.setData(pageSplit);
			} else {
				responseObj.setMessage("没有消费记录");
			}
			return responseObj;
		} catch (ServiceException e) {
			throw e;
		}
	}
	
	
	public ResponseObject<PageSplit<AccountDetail>> searchbyinfo_superadmin(String c_state,String s_storeId,String userId, String info, int pageSize, int pageNow,
	        String state, String type) throws ServiceException {
		try {
			info = StringUtil.isEmpty(info) ? null : StringUtil.escapeStringOfSearchKey(info);
			state = StringUtil.isEmpty(state) ? null : state.trim();
			type = StringUtil.isEmpty(type) ? null : type.trim();
			userId = StringUtil.isEmpty(userId) ? null : userId.trim();
			int rowCount = 0;
			try {
				//rowCount=this.accountDetailDao.countByInfo(userId, info, state, type);
				rowCount=this.accountDetailDao.countByInfo_superadmin(s_storeId, c_state,  userId, info, state, type);
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("获取消费详情记录数量失败", e);
			}

			ResponseObject<PageSplit<AccountDetail>> responseObj = new ResponseObject<PageSplit<AccountDetail>>(
			        ResponseCode.SUCCESS_CODE);
			if (rowCount > 0) {
				pageSize = Math.max(pageSize, 1);
				int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
				pageNow = Math.min(pageNow, pageCount);
				PageSplit<AccountDetail> pageSplit = new PageSplit<AccountDetail>();
				pageSplit.setPageCount(pageCount);
				pageSplit.setPageNow(pageNow);
				pageSplit.setRowCount(rowCount);
				pageSplit.setPageSize(pageSize);

				int startIndex = (pageNow - 1) * pageSize;
				try {
					List<AccountDetail> details=this.accountDetailDao.searchByInfo_superadmin(s_storeId, c_state,userId, info, state, type, startIndex, pageSize);
					//List<AccountDetail> details=this.accountDetailDao.searchByInfo(userId, info, state, type, startIndex, pageSize);
					
					
					if (details != null && !details.isEmpty()) {
						for (AccountDetail detail : details) {
							
							if((detail.getUser()==null)&&(!StringUtil.isEmpty(detail.getUserId())))
							{
								detail.setUser(this.userDao.getUserById(detail.getUserId()));
							}
								
			
							
							pageSplit.addData(detail);
						}
					}
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("获取消费详情列表失败", e);
				}
				responseObj.setData(pageSplit);
			} else {
				responseObj.setMessage("没有消费记录");
			}
			return responseObj;
		} catch (ServiceException e) {
			throw e;
		}
	}
	
	//修改确认状态
	public ResponseObject<Object> changeconfirmstate(String id, String type, String confirm_state,String admin_remark) throws ServiceException {
		try {
			String date = DateUtil.date2String(new Date());
			
			int k=this.accountDetailDao.updateconfirmstate(id, type, admin_remark, confirm_state, date);
			if(k<1)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"修改失败!");
			}
			return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"修改成功!");
			
		} catch (Exception e) {
			return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"修改发生异常!");
			//throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	
	
	public ResponseObject<Object> getmanydetail(String[] ids) throws ServiceException
	{
		try{
			List<AccountDetail> orders=new ArrayList<AccountDetail>();
			for(int i=0;i<ids.length;i++)
			{
				AccountDetail detail=this.accountDetailDao.getById(ids[i]);
				
				if(detail!=null)
				{
					
					orders.add(detail);
				}
			}
			if(orders.size()<1)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取信息失败!");
			}
			ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			obj.setData(orders);
			return obj;
		}catch(Exception e)
		{
			return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"获取信息异常");
		}
	}
	public ResponseObject<Object> getdetailNo() throws ServiceException//获取统计信息
	{
	
		try{
			MoneyDetailNo detail=new MoneyDetailNo();
			
			//消费类型
			int k=this.accountDetailDao.counttype(Constant.ACCOUNT_DETAIL_TYPE2);//消费类型总数量
			detail.setAllcustomerMoney_no(k);
			List<String> list=this.accountDetailDao.countamount(Constant.ACCOUNT_DETAIL_TYPE2,null,null);
			double amount=0;
			int amount_q=0;
			for(String a:list)
			{
				try{
					if(amount_q>=0)
					{
						amount+=Double.parseDouble(a);
					}
					else
					{
						amount_q++;
					}
					
				}catch(Exception e){
					amount_q++;
				}
			}
			
			
			BigDecimal   bd   =   new   BigDecimal(amount);   
			  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
			
			detail.setAllcustomerMoney(bd.doubleValue());
			detail.setAllcustomerMoney_no_q(amount_q);
			
			//状态变更
			list=this.accountDetailDao.countamount(Constant.ACCOUNT_DETAIL_TYPE3,null,null);
			detail.setAllchangestateMoney_no(list.size());
			amount=0;
			amount_q=0;
			for(String a:list)
			{
				try{
					if(amount_q>=0)
					{
						amount+=Double.parseDouble(a);
					}
					else
					{
						amount_q++;
					}
					
				}catch(Exception e){
					amount_q++;
				}
			}
			bd   =   new   BigDecimal(amount);   
			  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
			detail.setAllchangestateMoney(bd.doubleValue());
			detail.setAllchangestateMoney_no_q(amount_q);
			
			
		
			//信用卡充值
			list=this.accountDetailDao.countamount(Constant.ACCOUNT_DETAIL_TYPE11,null,"美元");
			detail.setAllCredit_usa_no(list.size());
			amount=0;
			amount_q=0;
			for(String a:list)
			{
				try{
					if(amount_q>=0)
					{
						amount+=Double.parseDouble(a);
					}
					else
					{
						amount_q++;
					}
					
				}catch(Exception e){
					amount_q++;
				}
			}
			bd   =   new   BigDecimal(amount);   
			  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
			detail.setAllCredit_usa(bd.doubleValue());
			detail.setAllCredit_usa_no(amount_q);;
			
			list=this.accountDetailDao.countamount(Constant.ACCOUNT_DETAIL_TYPE11,null,"人民币");
			detail.setAllCredit_rm_no(list.size());
			
			amount=0;
			amount_q=0;
			for(String a:list)
			{
				try{
					if(amount_q>=0)
					{
						amount+=Double.parseDouble(a);
					}
					else
					{
						amount_q++;
					}
					
				}catch(Exception e){
					amount_q++;
				}
			}
			bd   =   new   BigDecimal(amount);   
			  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
			detail.setAllCredit_rm(bd.doubleValue());
			detail.setAllCredit_rm_no(amount_q);;
			
			//支付宝,没有支付的
			list=this.accountDetailDao.countamount(Constant.ACCOUNT_DETAIL_TYPE12,Constant.ACCOUNT_DETAIL_STATE1,null);
			detail.setAlpayNopay_no(list.size());
			
			amount=0;
			amount_q=0;
			for(String a:list)
			{
				try{
					if(amount_q>=0)
					{
						amount+=Double.parseDouble(a);
					}
					else
					{
						amount_q++;
					}
					
				}catch(Exception e){
					amount_q++;
				}
			}
			bd   =   new   BigDecimal(amount);   
			  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
			detail.setAlpayNopay(bd.doubleValue());
			detail.setAlpayNopay_no_q(amount_q);
			
			//支付宝,支付失败的
			list=this.accountDetailDao.countamount(Constant.ACCOUNT_DETAIL_TYPE12,Constant.ACCOUNT_DETAIL_STATE3,null);
			detail.setAlpayfailedpay_no(list.size());
			
			amount=0;
			amount_q=0;
			for(String a:list)
			{
				try{
					if(amount_q>=0)
					{
						amount+=Double.parseDouble(a);
					}
					else
					{
						amount_q++;
					}
					
				}catch(Exception e){
					amount_q++;
				}
			}
			bd   =   new   BigDecimal(amount);   
			  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
			detail.setAlpayfailedpay(bd.doubleValue());
			detail.setAlpayfailedpay_no_q(amount_q);
			
			
			
			//支付宝,支付成功
			list=this.accountDetailDao.countamount(Constant.ACCOUNT_DETAIL_TYPE12,Constant.ACCOUNT_DETAIL_STATE2,null);
			detail.setAlpaysuccesspay_no(list.size());
			
			amount=0;
			amount_q=0;
			for(String a:list)
			{
				try{
					if(amount_q>=0)
					{
						amount+=Double.parseDouble(a);
					}
					else
					{
						amount_q++;
					}
					
				}catch(Exception e){
					amount_q++;
				}
			}
			bd   =   new   BigDecimal(amount);   
			  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
			detail.setAlpaysuccesspay(bd.doubleValue());
			detail.setAlpaysuccesspay_no_q(amount_q);
			
			
			//管理员充值,没确认数量
			int number=0;
			list=this.accountDetailDao.countadminamount(Constant.ACCOUNT_DETAIL_TYPE13, "0", "美元");
			number=list.size();
			
			
			amount=0;
			amount_q=0;
			for(String a:list)
			{
				try{
					if(amount_q>=0)
					{
						amount+=Double.parseDouble(a);
					}
					else
					{
						amount_q++;
					}
					
				}catch(Exception e){
					amount_q++;
				}
			}
			bd   =   new   BigDecimal(amount);   
			  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
			
			detail.setAdmin_nosure_usa(bd.doubleValue());
			
			list=this.accountDetailDao.countadminamount(Constant.ACCOUNT_DETAIL_TYPE13, "0", "人民币");
			number+=list.size();
			detail.setAdmin_nosure_no(number);
			
			amount=0;
			
			for(String a:list)
			{
				try{
					if(amount_q>=0)
					{
						amount+=Double.parseDouble(a);
					}
					else
					{
						amount_q++;
					}
					
				}catch(Exception e){
					amount_q++;
				}
			}
			bd   =   new   BigDecimal(amount);   
			  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
			
			detail.setAdmin_nosure_rm(bd.doubleValue());
			detail.setAdmin_nosure_no_q(amount_q);
			
			
			//已确认的
			//管理员充值,没确认数量
			number=0;
			list=this.accountDetailDao.countadminamount(Constant.ACCOUNT_DETAIL_TYPE13, "1", "美元");
			number=list.size();
			
			
			amount=0;
			amount_q=0;
			for(String a:list)
			{
				try{
					if(amount_q>=0)
					{
						amount+=Double.parseDouble(a);
					}
					else
					{
						amount_q++;
					}
					
				}catch(Exception e){
					amount_q++;
				}
			}
			
			bd   =   new   BigDecimal(amount);   
			  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
			
			detail.setAdmin_sure_usa(bd.doubleValue());
			
			list=this.accountDetailDao.countadminamount(Constant.ACCOUNT_DETAIL_TYPE13, "1", "人民币");
			number+=list.size();
			detail.setAdmin_sure_no(number);
			
			amount=0;
			
			for(String a:list)
			{
				try{
					if(amount_q>=0)
					{
						amount+=Double.parseDouble(a);
					}
					else
					{
						amount_q++;
					}
					
				}catch(Exception e){
					amount_q++;
				}
			}
			bd   =   new   BigDecimal(amount);   
			  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
			detail.setAdmin_sure_rm(bd.doubleValue());
			detail.setAdmin_sure_no_q(amount_q);
			
			
			ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			obj.setData(detail);
			return obj;
		}catch(Exception e)
		{
			return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"获取统计信息发生异常");
		}
	}
}

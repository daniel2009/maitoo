package com.meitao.service.impl;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitao.dao.AccountDao;
import com.meitao.dao.AccountDetailDao;
import com.meitao.dao.globalargsDao;
import com.meitao.service.AccountService;
import com.meitao.service.AlipayService;

import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.common.util.alipay.config.AlipayConfig;

import com.meitao.exception.ServiceException;
import com.meitao.model.AccountDetail;
import com.meitao.model.ResponseObject;

@Service("alipayService")
public class AlipayServiceImpl implements AlipayService{
	@Resource(name = "accountService")
	private AccountService accountService;
	@Autowired
	private AccountDao accountDao;
	@Autowired
	private AccountDetailDao accountDetailDao;
	@Autowired
	private globalargsDao globalargsDao;

	@Override
	public ResponseObject<String> recharge(String userId, String amount, String outTradeNo) throws ServiceException {
		ResponseObject<String> responseObject = new ResponseObject<String>();
		try{
			AccountDetail accountDetail = new AccountDetail();
			accountDetail.setAmount(String.valueOf(amount));
			accountDetail.setRealAmount(accountDetail.getAmount());
			accountDetail.setUserId(userId);
			accountDetail.setCurrency("人民币");
			accountDetail.setName("支付宝支付");
			accountDetail.setType(Constant.ACCOUNT_DETAIL_TYPE12);
			accountDetail.setState(Constant.ACCOUNT_DETAIL_STATE2);//完成
			accountDetail.setCreateDate(DateUtil.date2String(new Date()));
			accountDetail.setModifyDate(DateUtil.date2String(new Date()));
			accountDetail.setRemark("支付成功;交易单号:" + outTradeNo);
			accountDetail.setCreditId("-1");
			accountDetail.setStoreId("-1");
			accountDetail.setConfirm_state("0");
			int result = this.accountDetailDao.insertAccountDetail(accountDetail);
			if(result > 0){
				result = this.accountDao.rechargeRmb(userId, amount, DateUtil.date2String(new Date()));
				if(result > 0){
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}else{
					responseObject.setCode(ResponseCode.ACCOUNT_RECHARGE_RMB_FAILURE);
					responseObject.setMessage("账户人民币充值失败，请联系客服");
				}
			}else{
				responseObject.setCode(ResponseCode.ACCOUNT_INSERT_FAILURE);
				responseObject.setMessage("账户充值记录加载失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw ExceptionUtil.handle2ServiceException("进行账户充值加载出现异常", e);
		}
		return responseObject;
	}

	
	//添加没有确认交易成功的处理方式
	//@Override
	public ResponseObject<String> beforerecharge(String userId, String amount, String outTradeNo) throws ServiceException {
		ResponseObject<String> responseObject = new ResponseObject<String>();
		try{
			AccountDetail accountDetail = new AccountDetail();
			accountDetail.setAmount(String.valueOf(amount));
			accountDetail.setRealAmount(accountDetail.getAmount());
			accountDetail.setUserId(userId);
			accountDetail.setCurrency("人民币");
			accountDetail.setName("支付宝支付");
			accountDetail.setType(Constant.ACCOUNT_DETAIL_TYPE12);
			accountDetail.setState(Constant.ACCOUNT_DETAIL_STATE1);//待处理
			accountDetail.setCreateDate(DateUtil.date2String(new Date()));
			accountDetail.setModifyDate(DateUtil.date2String(new Date()));
			accountDetail.setRemark("待处理;交易单号:" + outTradeNo);
			accountDetail.setCreditId("-1");
			accountDetail.setStoreId("-1");
			accountDetail.setConfirm_state("0");
			int result = this.accountDetailDao.insertAccountDetail(accountDetail);
			if(result > 0){
				responseObject.setCode(ResponseCode.SUCCESS_CODE);
			}else{
				responseObject.setCode(ResponseCode.ACCOUNT_INSERT_FAILURE);
				responseObject.setMessage("账户充值记录加载失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw ExceptionUtil.handle2ServiceException("进行账户充值加载出现异常", e);
		}
		return responseObject;
	}
	@Override
	public boolean setMapFromDataBase(Map<String, String> map) throws ServiceException {
		boolean result = false;
		String key = "";
		String partner = "";
		String sellerEmail = "";
		try {
			key = this.globalargsDao.getcontentbyflag("alipay_pay_key");
			partner = this.globalargsDao.getcontentbyflag("alipay_pay_partner");
			sellerEmail = this.globalargsDao.getcontentbyflag("alipay_pay_seller_email");
			AlipayConfig.setKey(key);
			AlipayConfig.setSeller_email(sellerEmail);
			AlipayConfig.setPartner(partner);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(StringUtil.isEmpty(key) || StringUtil.isEmpty(partner) || StringUtil.isEmpty(sellerEmail)){
		}else{
			AlipayConfig.key = key;//key的计算方式不一样，要计算sign后才能添加
			map.put("partner", partner);
			map.put("seller_email", sellerEmail);
			result = true;
		}
		
		return result;
	}

	@Override
	public String checkRechargeState(String userId, String amount, String outTradeNo)
			throws ServiceException {
		String remark = "支付成功;交易单号:" + outTradeNo;
		String state = null;
		try{
			state = this.accountDetailDao.checkStateIfPayDetailByRemark(userId, amount, remark);
		} catch (Exception e) {
			e.printStackTrace();
			throw ExceptionUtil.handle2ServiceException("进行账户充值加载出现异常", e);
		}
		return state;
	}
	
	//根据交易号检查交易号是否存在，并反回id号
	//@Override
	public AccountDetail checkbeforeRechargeState(String outTradeNo)
			throws ServiceException {
		String remark = "待处理;交易单号:" + outTradeNo;
		//String state = null;
		AccountDetail detail=null;
		try{
			 detail = this.accountDetailDao.checkStateIfPayDetailByRemarkno(remark);
		} catch (Exception e) {
			e.printStackTrace();
			throw ExceptionUtil.handle2ServiceException("进行账户充值加载出现异常", e);
		}
		return detail;
	}
	
	//修改已有的数据
	@Override
	public ResponseObject<String> modifyrecharge(String amount, String outTradeNo,AccountDetail accountDetail) throws ServiceException {
		ResponseObject<String> responseObject = new ResponseObject<String>();
		try{
			//AccountDetail accountDetail = new AccountDetail();
			accountDetail.setAmount(String.valueOf(amount));
			accountDetail.setRealAmount(accountDetail.getAmount());
			
			accountDetail.setCurrency("人民币");
			accountDetail.setName("支付宝支付");
			accountDetail.setType(Constant.ACCOUNT_DETAIL_TYPE12);
			accountDetail.setState(Constant.ACCOUNT_DETAIL_STATE2);//完成

			accountDetail.setModifyDate(DateUtil.date2String(new Date()));
			accountDetail.setRemark("支付成功;交易单号:" + outTradeNo);
			accountDetail.setCreditId("-1");
			int result = this.accountDetailDao.modifyAccountDetail(accountDetail);
			if(result > 0){
				result = this.accountDao.rechargeRmb(accountDetail.getUserId(), amount, DateUtil.date2String(new Date()));
				if(result > 0){
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}else{
					responseObject.setCode(ResponseCode.ACCOUNT_RECHARGE_RMB_FAILURE);
					responseObject.setMessage("账户人民币充值失败，请联系客服");
				}
			}else{
				responseObject.setCode(ResponseCode.ACCOUNT_INSERT_FAILURE);
				responseObject.setMessage("账户充值记录加载失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw ExceptionUtil.handle2ServiceException("进行账户充值加载出现异常", e);
		}
		return responseObject;
	}
}

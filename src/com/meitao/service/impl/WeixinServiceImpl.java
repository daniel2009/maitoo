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
import com.meitao.service.WeixinService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.common.util.weixin.WeixinConfig;
import com.meitao.exception.ServiceException;
import com.meitao.model.AccountDetail;
import com.meitao.model.ResponseObject;

@Service("weixinService")
public class WeixinServiceImpl implements WeixinService{
	@Resource(name = "accountService")
	private AccountService accountService;
	@Autowired
	private AccountDao accountDao;
	@Autowired
	private AccountDetailDao accountDetailDao;
	@Autowired
	private globalargsDao globalargsDao;

	@Override
	public ResponseObject<String> scanRecharge(String userId, String amount, String flag) throws ServiceException {
		ResponseObject<String> responseObject = new ResponseObject<String>();
		try{
			if(ResponseCode.ACCOUNT_SCAN_PAY_NOT_COMPLETE.equals(this.checkIfScanPay(userId, amount, null, flag).getCode())){//未添加付款数据
				int result = this.accountDetailDao.updateStateByPaySuccess(userId, flag, DateUtil.date2String(new Date()));
				if(result > 0){
					result = this.accountDao.rechargeRmb(userId, amount, DateUtil.date2String(new Date()));
					if(result > 0){
						responseObject.setCode(ResponseCode.SUCCESS_CODE);
					}else{
						responseObject.setCode(ResponseCode.ACCOUNT_RECHARGE_RMB_FAILURE);
						responseObject.setMessage("账户人民币充值失败，请联系客服");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw ExceptionUtil.handle2ServiceException("进行账户支付出现异常", e);
		}
		return responseObject;
	}

	@Override
	public ResponseObject<Object> checkIfScanPay(String userId, String amount,
			String accountDetailId, String remark) throws ServiceException {
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		if(StringUtil.isEmpty(userId) || StringUtil.isEmpty(amount) || (StringUtil.isEmpty(accountDetailId) && StringUtil.isEmpty(remark))){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数错误");
		}else{
			try{
				String state = null;
				if(StringUtil.isEmpty(accountDetailId)){
					state = this.accountDetailDao.checkStateIfPayDetailByRemark(userId, amount, remark);
				}else{
					state = this.accountDetailDao.checkIfScanPayDetail(userId, amount, accountDetailId);
				}
				if(StringUtil.isEmpty(state)){//找不到记录
					responseObject.setCode(ResponseCode.ACCOUNT_SCAN_PAY_NOT_EXISTS);
					responseObject.setMessage("没有待充值的信息");
				}else if(state.equals(Constant.ACCOUNT_DETAIL_STATE2)){//已付款
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}else{//未付款
					responseObject.setCode(ResponseCode.ACCOUNT_SCAN_PAY_NOT_COMPLETE);
					responseObject.setMessage("未付款");
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw ExceptionUtil.handle2ServiceException("进行账户支付查询出现异常", e);
			}
		}
		return responseObject;
	}
	@Override
	public ResponseObject<String> addPreRecharge(String userId, double amount, String flag)
			throws ServiceException {
		ResponseObject<String> responseObject = new ResponseObject<String>();
		AccountDetail accountDetail = new AccountDetail();
		accountDetail.setAmount(String.valueOf(amount));
		accountDetail.setRealAmount(accountDetail.getAmount());
		accountDetail.setUserId(userId);
		accountDetail.setCurrency("人民币");
		accountDetail.setName("微信扫码支付");
		accountDetail.setType(Constant.ACCOUNT_DETAIL_TYPE12);
		accountDetail.setState(Constant.ACCOUNT_DETAIL_STATE1);//未扫码付款
		accountDetail.setCreateDate(DateUtil.date2String(new Date()));
		accountDetail.setModifyDate(DateUtil.date2String(new Date()));
		accountDetail.setRemark(flag);
		accountDetail.setCreditId("-1");
		accountDetail.setStoreId("-1");
		accountDetail.setConfirm_state("0");
		try{
			int result = this.accountDetailDao.insertAccountDetail(accountDetail);
			if(result > 0){
				responseObject.setCode(ResponseCode.SUCCESS_CODE);
				responseObject.setData(accountDetail.getId());
			}else{
				responseObject.setCode(ResponseCode.ACCOUNT_INSERT_FAILURE);
				responseObject.setMessage("账户充值记录加载");
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
		String appid = "";
		String mchId = "";
		try {
			key = this.globalargsDao.getcontentbyflag("weixin_scan_pay_key");
			appid = this.globalargsDao.getcontentbyflag("weixin_scan_pay_appid");
			mchId = this.globalargsDao.getcontentbyflag("weixin_scan_pay_mch_id");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(StringUtil.isEmpty(key) || StringUtil.isEmpty(appid) || StringUtil.isEmpty(mchId)){
		}else{
			WeixinConfig.key = key;//key的计算方式不一样，要计算sign后才能添加
			map.put("appid", appid);
			map.put("mch_id", mchId);
			result = true;
		}
		
		return result;
	}
}

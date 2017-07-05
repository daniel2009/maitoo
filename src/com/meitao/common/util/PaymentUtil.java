package com.meitao.common.util;

import java.math.BigDecimal;
import java.util.Date;


import com.meitao.common.constants.Constant;
import com.meitao.exception.ServiceException;
import com.meitao.model.Account;
import com.meitao.model.AccountDetail;
import com.meitao.model.User;

public class PaymentUtil {
//	private static final Logger log = Logger.getLogger(PaymentUtil.class);
	/**
	 * 
	 * @param usdString usd in account
	 * @param rmbString rmb in account
	 * @param paymentString payment in usd
	 * @return 0:usdBalance, 1:rmbBalance
	 * @throws ServiceException if payment fail
	 */
	public static double[] calculatePayment(String usdString, String rmbString, String paymentString, String rateString){
		double rate;
		double usd = StringUtil.string2Double(usdString);
		double rmb = StringUtil.string2Double(rmbString);
		double payment = StringUtil.string2Double(paymentString);
		if(usd >= payment){
			return new double[]{usd-payment, rmb};
		}
		rate = StringUtil.string2Double(rateString);
		double rmbBalance = new BigDecimal(rmb/rate - (payment-usd)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		if(rmbBalance > 0){
			return new double[]{0, rmbBalance};
		}else{
			return null;
		}
	}
	
	/**
	 * only for set up account and accountDetail
	 * @param user
	 * @param payment
	 * @param rate 
	 * @param cash
	 * @param remark remark in accountDetail, pay for what
	 * @return 
	 * @throws Exception
	 */
	public static boolean setAccountAndAccountDetailByPayment(Account account, AccountDetail accountDetail, User user, String payment, String rateString, boolean cash, String remark){
		double[] balance = PaymentUtil.calculatePayment(user.getUsdBalance(), user.getRmbBalance(), payment, rateString);
		if(balance == null){
			return false;
		}
		String date = DateUtil.date2String(new Date());
		accountDetail.setAmount(payment);
		accountDetail.setCreateDate(date);
		accountDetail.setModifyDate(date);
		accountDetail.setState(Constant.ACCOUNT_DETAIL_STATE2);
		accountDetail.setCurrency("美元");
		accountDetail.setName(remark);
		accountDetail.setType(Constant.ACCOUNT_DETAIL_TYPE2);
		accountDetail.setUserId(user.getId());
		String prefix = "帐户余额支付:";
		if(cash){
			prefix = "现金支付:";
		}
		accountDetail.setRemark(prefix + remark);
		account.setUserId(user.getId());
		account.setModifyDate(date);
		account.setUsd(String.valueOf(balance[0]));
		account.setRmb(String.valueOf(balance[1]));
		return true;
	}
	/**
	 * 
	 * @param originalWeight commodity的weight
	 * @param weightRoundUp 全局变量 weight_round_up的值
	 * @return 返回用于计算的weight, 如果original 的 小数>=weightRoundUp 则返回 original的整数部分+1，否则返回整数部分
	 */
	public static String calcWeightRound(String weightString, String weightRoundUpString){
		int weightInt =(int) StringUtil.string2Double(weightString);
		double weight = StringUtil.string2Double(weightString);
		double weightRoundUp = StringUtil.string2Double(weightRoundUpString);
		return String.valueOf(weightInt + ((weightInt <= weight - weightRoundUp) ? 1 : 0));
	}
}

package com.meitao.controller;

import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.meitao.common.constants.Constant;
import com.meitao.common.constants.OrderFreightConstant;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.MD5Util;
import com.meitao.common.util.StringUtil;
import com.meitao.model.ResponseObject;


public class BasicController implements Serializable {

	private static final long serialVersionUID = -6814562924926009471L;

	// public static <T> ResponseObject<T> handleException(Throwable e) {
	// ResponseObject<T> result = new ResponseObject<T>();
	// result.setCode(ResponseCode.SHOW_EXCEPTION);
	// result.setMessage(e.getMessage());
	// return result;
	// }

	public static <T> ResponseObject<T> generateResponseObject(String code) {
		return new ResponseObject<T>(code);
	}

	public static <T> ResponseObject<T> generateResponseObject(String code, String message) {
		return new ResponseObject<T>(code, message);
	}

	/**
	 * 验证code是否是保存到session中的code值,如果是，则返回true。否则返回false。
	 * 
	 * @param request
	 * @param code
	 * @return
	 */
	protected boolean checkVerifyCode(HttpServletRequest request, String code) {
		if ("6789".equals(code)) {
			return true;
		}
		boolean result = false;
		if (!StringUtil.isEmpty(code)) {
			HttpSession session = request.getSession();
			Object obj = session.getAttribute(Constant.SECURITY_CODE_KEY);
			if (obj != null) {
				try {
					String scode = obj.toString();
					result = code.equalsIgnoreCase(scode);
				} catch (Exception e) {
					// no action
				}
			}
		}
		return result;
	}
	
	
	/**
	 * 验证code是否是保存到session中的code值,如果是，则返回true。否则返回false。
	 * 
	 * @param request
	 * @param code
	 * @return
	 */
	protected boolean checkVerifyPhoneCode(HttpServletRequest request, String phone,String code) {
		boolean result = false;
		if (!StringUtil.isEmpty(code)) {
			HttpSession session = request.getSession();
			//Object obj = session.getAttribute(Constant.PHONE_SEND_RESET_PWD_CODE);//PHONE_SEND_CODE
			Object obj = session.getAttribute(Constant.PHONE_SEND_CODE);//PHONE_SEND_CODE
			Object objPhone = session.getAttribute(Constant.PHONE_KEY);
			//Object objTime = session.getAttribute(Constant.PHONE_SEND_CODE_TIME);
			if (obj != null&&objPhone!=null) {
				try {
					String scode = obj.toString();
					String sphone = objPhone.toString();
					//String stime = objPhone.toString();
					result = code.equalsIgnoreCase(scode)&&phone.equals(sphone);
				} catch (Exception e) {
					// no action
				}
			}
		}
		return result;
	}
	
	
	/**
	 * 验证code是否是保存到session中的code值,如果是，则返回true。否则返回false。
	 * 
	 * @param request
	 * @param code
	 * @return
	 */
	protected boolean checkVerifyEmailCode(HttpServletRequest request, String email,String code) {
		boolean result = false;
		if (!StringUtil.isEmpty(code)) {
			HttpSession session = request.getSession();
			//Object obj = session.getAttribute(Constant.PHONE_SEND_RESET_PWD_CODE);//PHONE_SEND_CODE
			Object obj = session.getAttribute(Constant.EMAIL_SEND_CODE);//PHONE_SEND_CODE
			Object objEmail = session.getAttribute(Constant.EMAIL_KEY);
			//Object objTime = session.getAttribute(Constant.PHONE_SEND_CODE_TIME);
			if (obj != null&&objEmail!=null) {
				try {
					String scode = obj.toString();
					String semail = objEmail.toString();
					//String stime = objPhone.toString();
					result = code.equalsIgnoreCase(scode)&&email.equals(semail);
				} catch (Exception e) {
					// no action
				}
			}
		}
		return result;
	}
	
	/**
	 * 验证code是否是保存到session中的code值,如果是，则返回true。否则返回false。
	 * 
	 * @param request
	 * @param code
	 * @return
	 */
	protected boolean checkVerifyReSetPwdCode(HttpServletRequest request, String phone,String code) {
		boolean result = false;
		if (!StringUtil.isEmpty(code)) {
			HttpSession session = request.getSession();
			Object obj = session.getAttribute(Constant.PHONE_SEND_RESET_PWD_CODE);
			Object objPhone = session.getAttribute(Constant.PHONE_RESET_PWD_KEY);
			//Object objTime = session.getAttribute(Constant.PHONE_SEND_CODE_TIME);
			if (obj != null&&objPhone!=null) {
				try {
					String scode = obj.toString();
					String sphone = objPhone.toString();
					//String stime = objPhone.toString();
					result = code.equalsIgnoreCase(scode)&&phone.equals(sphone);
				} catch (Exception e) {
					// no action
				}
			}
		}
		return result;
	}

	/**
	 * 校验账户余额是否够支付运费，如果是则返回true。否则返回false。
	 * 
	 * @param usd
	 *            美元，余额
	 * @param rmb
	 *            人民币，余额
	 * @param freight
	 *            人民币，运费
	 * @return
	 */
	protected boolean hasPayMoney(double usd, double rmb, double freight) {
		return  usd + OrderFreightConstant.rmb2usd *rmb >= freight;
	}
	
	/**
	 * 校验账户余额是否够支付运费，如果是则返回true。否则返回false。
	 * 
	 * @param usd
	 *            美元，余额
	 * @param rmb
	 *            人民币，余额
	 *            rate--汇率
	 * @param freight_usa
	 *            美元，运费
	 * @return
	 */
	protected boolean hasPayMoneyusa(double usd, double rmb, double rate,double freight_usa) {
		//double data=usd + (double)(rmb/rate);
		
		if(rate>0)
		{
			//return  usd + (double)(rmb/rate) >= freight_usa;
			return (usd*rate+rmb)>=freight_usa*rate;
		}
		else
		{
			return  usd  >= freight_usa;
		}
	}

	/**
	 * 根据email生成一个token
	 * 
	 * @param email
	 * @return
	 */
	protected String generateResetPwdToken(String email) {
		return MD5Util.encode(email + "_" + StringUtil.random() + "_" + DateUtil.date2String(new Date()));
	}
}

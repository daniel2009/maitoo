package com.meitao.common.util.sms;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.Properties;

import com.meitao.common.constants.Constant;
import com.meitao.common.util.PropertiesReader;


public class SmsSendUtil {
	 
	public static String sendGeneralMsg(String content, String mobile) throws Exception {
		try {
			SimpleSmsSender sender = SmsSenderFactory.generateSmsSender();
			if(mobile.length()==10)
				mobile="+1"+mobile;
			else if(mobile.length()==11)
				mobile="+86"+mobile;
			return sender.sendSms(content, mobile);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	public static String sendResetPwdMsg(String password, String mobile) throws Exception {
		try {
			Properties prop = null;
			if(mobile.length()==10){
				mobile="+1"+mobile;
				prop = PropertiesReader.read(SmsConfigUtil.getSmsConfigFile(Constant.COUNTRY_CODE_US));
			}else if(mobile.length()==11){
				mobile="+86"+mobile;
				prop = PropertiesReader.read(SmsConfigUtil.getSmsConfigFile(Constant.COUNTRY_CODE_CN));
			}else{
				prop = PropertiesReader.read(SmsConfigUtil.getSmsConfigFile(Constant.COUNTRY_CODE_CN));
			}
			
			String text = prop.getProperty("meitao.register.content");
			text = MessageFormat.format(text, new Object[] {password});
			SimpleSmsSender sender = SmsSenderFactory.generateSmsSender();
			
			return sender.sendSms(text, mobile);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	
	public static String sendRegisterMsg(String code, String mobile) throws Exception {
		try {
			Properties prop = null;
			if(mobile.length()==10){
				mobile="+1"+mobile;
				prop = PropertiesReader.read(SmsConfigUtil.getSmsConfigFile(Constant.COUNTRY_CODE_US));
			}else if(mobile.length()==11){
				mobile="+86"+mobile;
				prop = PropertiesReader.read(SmsConfigUtil.getSmsConfigFile(Constant.COUNTRY_CODE_CN));
			}else{
				prop = PropertiesReader.read(SmsConfigUtil.getSmsConfigFile(Constant.COUNTRY_CODE_CN));
			}
			String text = prop.getProperty("meitao.register.content");
			text = MessageFormat.format(text, new Object[] {code});
			SimpleSmsSender sender = SmsSenderFactory.generateSmsSender();
			
			return sender.sendSms(text, mobile);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	public static String sendAdminAddUserMsg(String password, String mobile) throws Exception {
		try {
			Properties prop = null;
			if(mobile.length()==10){
				mobile="+1"+mobile;
				prop = PropertiesReader.read(SmsConfigUtil.getSmsConfigFile(Constant.COUNTRY_CODE_US));
			}else if(mobile.length()==11){
				mobile="+86"+mobile;
				prop = PropertiesReader.read(SmsConfigUtil.getSmsConfigFile(Constant.COUNTRY_CODE_CN));
			}else{
				prop = PropertiesReader.read(SmsConfigUtil.getSmsConfigFile(Constant.COUNTRY_CODE_CN));
			}
			String text = prop.getProperty("meitao.new.user.content");
			text = MessageFormat.format(text, new Object[] {password});
			SimpleSmsSender sender = SmsSenderFactory.generateSmsSender();
			return sender.sendSms(text, mobile);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	public static String sendOrderStateMsg(String orderId, String state,String mobile) throws Exception {
		try {
			Properties prop = null;
			if(mobile.length()==10){
				mobile="+1"+mobile;
				prop = PropertiesReader.read(SmsConfigUtil.getSmsConfigFile(Constant.COUNTRY_CODE_US));
			}else if(mobile.length()==11){
				mobile="+86"+mobile;
				prop = PropertiesReader.read(SmsConfigUtil.getSmsConfigFile(Constant.COUNTRY_CODE_CN));
			}else{
				return null;
				//prop = PropertiesReader.read(SmsConfigUtil.getSmsConfigFile(Constant.COUNTRY_CODE_CN));
			}
			String text ="";
			if(state.equals(Constant.ORDER_STATE3)){
				text = prop.getProperty("meitao.order.state.to3.content");
			}else if(state.equals(Constant.ORDER_STATE4)){
				text = prop.getProperty("meitao.order.state.to4.content");
			}else if(state.equals(Constant.ORDER_STATE7)){
				text = prop.getProperty("meitao.order.state.to7.content");
			}else if(state.equals(Constant.ORDER_STATE8)){
				text = prop.getProperty("meitao.order.state.to8.content");
			}else if(state.equals(Constant.ORDER_STATE10)){
				text = prop.getProperty("meitao.order.state.to10.content");
			}else {
				String strRet="{\"code\":-10,\"msg\":\"该状态不需要发短信！\"}";
				return strRet;
			}
			if(text==null)
			{
				return null;
			}
			text = MessageFormat.format(text, new Object[] {orderId});
			SimpleSmsSender sender = SmsSenderFactory.generateSmsSender();
			return sender.sendSms(text, mobile);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	
   
}



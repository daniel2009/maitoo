package com.meitao.common.util.sms;

import java.text.MessageFormat;
import java.util.Properties;

import com.meitao.common.constants.Constant;
import com.meitao.common.util.PropertiesReader;
import com.meitao.model.SendEmail;


public class MailSendUtil {
	/**
	 * 发送修改密码的邮件
	 * 
	 * @param baseUrl
	 *            TODO
	 * @param recipient
	 *            邮件地址
	 * @param token
	 *            生成的token
	 * 
	 * @return
	 * @throws Exception
	 */
	public static boolean sendResetPwdMsg(String baseUrl, String recipient, String token) throws Exception {
		try {
			Properties prop = PropertiesReader.read("/email.properties");
			String url = baseUrl + prop.getProperty("meitao.reset.pwd.page.name", "resetemailredirct.html") + "?email="
			        + recipient + "&token=" + token;
			String str = prop.getProperty("meitao.reset.pwd.content");
			str = MessageFormat.format(str, new Object[] { Constant.RESET_PWD_TOKEN_TIME_OF_HOUR, url, url });
			SimpleMailSender sender = MailSenderFactory.generateMailSender();
			return sender.send(recipient, prop.getProperty("meitao.reset.pwd.title"), str);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	/*
	 * 邮箱发送验证码
	 * kai 20151103
	 * baseUrl--公司链接
	 * recipient--接收邮箱
	 * code --验证码
	 * */
	public static boolean sendverifyMsg(String baseUrl, String recipient, String code) throws Exception {
		try {
			Properties prop = PropertiesReader.read("/email.properties");
			String url = baseUrl;
			String str = prop.getProperty("meitao.verify.code.content");
			str=str+"<span style='color:red;'>"+code+"</span><br/>";
			str=str+"公司网址:"+url+"<br/>";
			str = MessageFormat.format(str, new Object[] { Constant.RESET_PWD_TOKEN_TIME_OF_HOUR, url, url });
			SimpleMailSender sender = MailSenderFactory.generateMailSender();
			return sender.send(recipient, prop.getProperty("meitao.verify.code.title"), str);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	public static boolean send(SendEmail sendEmail) throws Exception{
		try {
			MailSenderFactory.send(sendEmail);
		} catch (Exception e) {
			throw new Exception(e);
		}
		return true;
	}
}

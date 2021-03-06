package com.meitao.common.util.sms;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

//import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeUtility;

//import sun.misc.BASE64Encoder;

import com.meitao.common.constants.Constant;
import com.meitao.common.util.PropertiesReader;
import com.meitao.model.SendEmail;


public class MailSenderFactory {
	private static Map<String, SimpleMailSender> cache = new ConcurrentHashMap<String, SimpleMailSender>();

	/**
	 * 根据配置参数获取发送邮件信息的类。
	 * 
	 * @return
	 */
	public static SimpleMailSender generateMailSender() {
		String userName = "";
		String password = "";
		Properties prop = null;

		try {
			Properties props = PropertiesReader.read(Constant.EMAIL_PROPERTIES_FILE);
			userName = props.getProperty("mail.user.name");
			password = props.getProperty("mail.password");
			prop = new Properties();
			prop.put("mail.smtp.host", props.get("mail.smtp.host"));
			prop.put("mail.smtp.auth", props.get("mail.smtp.auth"));
			
			
		//	props.put("mail.smtp.host", "smtp.gmail.com");
		   // props.put("mail.smtp.user", "myname");
		   // props.put("mail.smtp.socketFactory.port", "465");
		   // props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		   // props.put("mail.smtp.auth", "true");
		   // props.put("mail.smtp.port", "465");
			
			
			if (userName == null || userName.trim().isEmpty() || password == null || password.trim().isEmpty()) {
			
			}
		} catch (Exception e) {
			return null;
			//throw new Exception("");
		
		}
		SimpleMailSender sender = cache.get(userName);

		if (sender == null) {
			sender = new SimpleMailSender(userName, password, prop);
			cache.put(userName, sender);
		}
		return sender;
	}
	
	public static void send(SendEmail sendEmail) throws MessagingException{
		Properties properties = new Properties();
		properties.put("mail.smtp.host", sendEmail.getSmtpHost());
		properties.put("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(properties, new MailAuthenticator(sendEmail.getUserName(), sendEmail.getPassword()));
		MimeMessage mimeMessage = new MimeMessage(session);
		//sun.misc.BASE64Encoder base64 = new BASE64Encoder();
		//String subject = new String(base64.encode((mailInfo.getSubject()).getBytes("UTF-8")));
		
		mimeMessage.setFrom(new InternetAddress(sendEmail.getUserName()));
		mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(sendEmail.getRecipient()));
		mimeMessage.setSubject(sendEmail.getTitle(),"UTF-8");
		mimeMessage.setText(sendEmail.getContent(),"UTF-8");
		mimeMessage.saveChanges();
		
		Transport.send(mimeMessage);
	}
}

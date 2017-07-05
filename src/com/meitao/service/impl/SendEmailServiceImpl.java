package com.meitao.service.impl;



import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitao.dao.globalargsDao;
import com.meitao.service.SendEmailService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.ExceptionUtil;

import com.meitao.common.util.sms.MailSendUtil;
import com.meitao.exception.ServiceException;

import com.meitao.model.ResponseObject;

import com.meitao.model.SendEmail;

@Service("sendEmailService")
public class SendEmailServiceImpl implements SendEmailService{

	@Autowired
	private globalargsDao globalargsDao;
	
	@Override
	public ResponseObject<Object> sendGroupByAdmin(SendEmail sendEmail) throws ServiceException {
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		String recipients[] = StringUtils.split(sendEmail.getRecipient(), ",");
		int successNumber = 0;
		try{
			sendEmail.setSmtpHost(globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SEND_EMAIL_SMTP_HOST));
			sendEmail.setUserName(this.globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SEND_EMAIL_USER_NAME));
			sendEmail.setPassword(this.globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SEND_EMAIL_PASSWORD));
			for(String recipient : recipients){
				sendEmail.setRecipient(recipient);
				if(MailSendUtil.send(sendEmail)){
					successNumber++;
				}
			}
			if(successNumber > 0){
				responseObject.setCode(ResponseCode.SUCCESS_CODE);
				responseObject.setData(successNumber);
			}else{
				responseObject.setCode(ResponseCode.SEND_EMAIL_NONE_SUCCESS);
				responseObject.setMessage("没有发送成功的email");
			}
		}catch(Exception e){
			throw ExceptionUtil.handle2ServiceException("群发邮件功能异常", e);
		}
		return responseObject;
	}
}

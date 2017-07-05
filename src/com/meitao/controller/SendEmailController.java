package com.meitao.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import jxl.common.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.ResponseBody;



import com.meitao.service.SendEmailService;

import com.meitao.common.constants.ResponseCode;

import com.meitao.model.ResponseObject;

import com.meitao.model.SendEmail;

@Controller
public class SendEmailController extends BasicController{
	private static final long serialVersionUID = 5386221788663238370L;
	private static final Logger log = Logger.getLogger(SendEmailController.class);
	@Resource(name = "sendEmailService")
	private SendEmailService sendEmailService;
	@Value(value = "${page_size}")
	private int defaultPageSize;
	
	@RequestMapping(value = "/admin/sendEmail/send", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<Object> sendGroupByAdmin(HttpServletRequest request, SendEmail sendEmail){
		try{
			return sendEmailService.sendGroupByAdmin(sendEmail);
		}catch(Exception e){
			log.error("admin发送email出现异常", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "群发邮件功能异常");
		}
	}
}

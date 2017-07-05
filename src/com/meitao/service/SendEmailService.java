package com.meitao.service;

import com.meitao.exception.ServiceException;
import com.meitao.model.ResponseObject;
import com.meitao.model.SendEmail;

public interface SendEmailService {

	ResponseObject<Object> sendGroupByAdmin(SendEmail sendEmail) throws ServiceException;

}

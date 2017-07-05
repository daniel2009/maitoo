package com.meitao.service.impl;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.meitao.dao.SendUserDao;

import com.meitao.service.SendUserService;

import com.meitao.common.constants.ResponseCode;

import com.meitao.common.util.ExceptionUtil;

import com.meitao.common.util.StringUtil;
import com.meitao.exception.ServiceException;

import com.meitao.model.ResponseObject;

import com.meitao.model.SendUser;


@Service("sendUserService")
public class SendUserServiceImpl implements SendUserService {
	@Autowired
	private SendUserDao sendUserDao;


	public ResponseObject<Object> getallbyid(String userid) throws ServiceException {
		if (StringUtil.isEmpty(userid)) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
					"参数无效");
		}
		try {
			List<SendUser> users=this.sendUserDao.getAllbyuserid(userid);
			ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			obj.setData(users);
			return obj;
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}

	
}

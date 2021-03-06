package com.meitao.service.impl;

import java.util.Date;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitao.dao.CommonDao;
import com.meitao.service.CommonService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.exception.ServiceException;
import com.meitao.model.ResponseObject;

@Service("commonService")
public class CommonServiceImpl implements CommonService {
	@Autowired
	private CommonDao commonDao;

	public ResponseObject<String> checkToken(String email, String token) throws ServiceException {
		try {
			String oldToken = this.getLastToken(email);
			if (token.equals(oldToken)) {
				return new ResponseObject<String>(ResponseCode.SUCCESS_CODE);
			} else {
				return new ResponseObject<String>(ResponseCode.TOKEN_ERROR, "Token 无效");
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		} finally {
			try {
				// 将所有的email对应的改成无效状态。
				this.commonDao.updateStatus(email);
			} catch (Exception e) {
				// ignore
			}
		}
	}

	public ResponseObject<String> insertToken(String email, String token) throws ServiceException {
		try {
			// 将所有的email对应的改成无效状态。
			this.commonDao.updateStatus(email);
			String date = DateUtil.date2String(new Date());
			int i = this.commonDao.insertToken(email, token, date);
			if (i > 0) {
				ResponseObject<String> response = new ResponseObject<String>(ResponseCode.SUCCESS_CODE);
				response.setData(token);
				return response;
			} else {
				return new ResponseObject<String>(ResponseCode.TOKEN_INSERT_ERROR);
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}

	public String getLastToken(String email) throws ServiceException {
		String date = DateUtil.long2String(new Date().getTime() - Constant.RESET_PWD_TOKEN_TIME * 1000);
		try {
			return this.commonDao.retrieveToken(email, date);
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
}

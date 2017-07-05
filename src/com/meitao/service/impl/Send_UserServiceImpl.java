package com.meitao.service.impl;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitao.dao.Send_UserDao;
import com.meitao.model.Send_User;
import com.meitao.service.Send_UserService;

import com.meitao.common.constants.ResponseCode;

import com.meitao.common.util.ExceptionUtil;

import com.meitao.common.util.StringUtil;

import com.meitao.exception.ServiceException;

import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;


@Service("send_UserService")
public class Send_UserServiceImpl implements Send_UserService {
	@Autowired
	private Send_UserDao send_UserDao;


	public ResponseObject<PageSplit<Send_User>> searchSendUserByInfo(String info, int pageSize, int pageNow)
	        throws ServiceException {
		try {
			if(!StringUtil.isEmpty(info))
			{
				info = StringUtil.escapeStringOfSearchKey(info);
			}
			else
			{
				info=null;
			}
			
			
			int rowCount = 0;
			try {
				rowCount = this.send_UserDao.countOfSendUser(info);
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("获取运单个数失败", e);
			}

			ResponseObject<PageSplit<Send_User>> responseObj = new ResponseObject<PageSplit<Send_User>>(
					ResponseCode.SUCCESS_CODE);
			if (rowCount > 0) {
				pageSize = Math.max(pageSize, 1);
				int pageCount = rowCount / pageSize
						+ (rowCount % pageSize == 0 ? 0 : 1);
				pageNow = Math.min(pageNow, pageCount);
				PageSplit<Send_User> pageSplit = new PageSplit<Send_User>();
				pageSplit.setPageCount(pageCount);
				pageSplit.setPageNow(pageNow);
				pageSplit.setRowCount(rowCount);
				pageSplit.setPageSize(pageSize);

				int startIndex = (pageNow - 1) * pageSize;
				try {
					List<Send_User> sendusers =this.send_UserDao.searchSendUser(info, startIndex, pageSize);
						
					pageSplit.setDatas(sendusers);
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("获取发送用户列表失败", e);
				}
				responseObj.setData(pageSplit);
			} else {
				responseObj.setMessage("没有发送用户信息!");
			}
			return responseObj;
		} catch (ServiceException e) {
			throw e;
		}
	}
	
}

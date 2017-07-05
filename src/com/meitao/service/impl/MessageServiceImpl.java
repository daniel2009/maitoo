package com.meitao.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitao.dao.MessageDao;
import com.meitao.service.MessageService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.exception.ServiceException;
import com.meitao.model.Message;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;

@Service("messageService")
public class MessageServiceImpl extends BasicService implements MessageService {
	@Autowired
	private MessageDao messageDao;

	@SuppressWarnings("unchecked")
	public <T> ResponseObject<PageSplit<T>> getByUserId(String userId, int pageSize, int pageNow)
	        throws ServiceException {
		if (StringUtil.isEmpty(userId)) {
			return new ResponseObject<PageSplit<T>>(ResponseCode.PARAMETER_ERROR, "参数无效");
		}

		try {
			int rowCount = 0;
			try {
				rowCount = this.messageDao.count(userId);
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("获取留言信息个数失败", e);
			}

			ResponseObject<PageSplit<T>> responseObj = new ResponseObject<PageSplit<T>>(ResponseCode.SUCCESS_CODE);
			if (rowCount > 0) {
				pageSize = Math.max(pageSize, 1);
				int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
				pageNow = Math.min(pageNow, pageCount);
				PageSplit<T> pageSplit = new PageSplit<T>();
				pageSplit.setPageCount(pageCount);
				pageSplit.setPageNow(pageNow);
				pageSplit.setRowCount(rowCount);
				pageSplit.setPageSize(pageSize);

				int startIndex = (pageNow - 1) * pageSize;
				try {
					List<Message> result = this.messageDao.retrieveMessages(userId, startIndex, pageSize);
					if (result != null && !result.isEmpty()) {
						for (Message t : result) {
							// t.setContent(StringUtil.dealHtmlSpecialCharacters(t.getContent()));
							pageSplit.addData((T) t);
						}
					}
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("获取留言信息列表失败", e);
				}
				responseObj.setData(pageSplit);
			} else {
				responseObj.setMessage("没有留言");
			}
			return responseObj;
		} catch (ServiceException e) {
			throw e;
		}
	}

	public ResponseObject<Object> saveMessage(Message msg) throws ServiceException {
		if (msg == null) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR, "参数无效");
		}

		try {
			ResponseObject<Object> responseObj = new ResponseObject<Object>();
			// 处理留言中的特殊字符串
			msg.setContent(StringUtil.dealHtmlSpecialCharacters(msg.getContent()));
			// 添加时间
			String date = DateUtil.date2String(new Date());
			msg.setCreateDate(date);
			msg.setModifyDate(date);

			int iresult = this.messageDao.insertMessage(msg);
			if (iresult > 0) {
				this.messageDao.updateModifyDate(msg.getParentId(), date, msg.getState());
				responseObj.setCode(ResponseCode.SUCCESS_CODE);
			} else {
				responseObj.setCode(ResponseCode.MESSAGE_INSERT_FAILURE);
				responseObj.setMessage("留言失败");
			}
			return responseObj;
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> ResponseObject<PageSplit<T>> searchByUserId(String key, String userId, String state, int pageSize,
	        int pageNow, String groupid) throws ServiceException {
		try {
			key = StringUtil.escapeStringOfSearchKey(key);
			int rowCount = 0;
			try {
				rowCount = this.messageDao.countByKey(key, state, userId);
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("获取留言信息个数失败", e);
			}

			ResponseObject<PageSplit<T>> responseObj = new ResponseObject<PageSplit<T>>(ResponseCode.SUCCESS_CODE);
			if (rowCount > 0) {
				pageSize = Math.max(pageSize, 1);
				int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
				pageNow = Math.min(pageNow, pageCount);
				PageSplit<T> pageSplit = new PageSplit<T>();
				pageSplit.setPageCount(pageCount);
				pageSplit.setPageNow(pageNow);
				pageSplit.setRowCount(rowCount);
				pageSplit.setPageSize(pageSize);

				int startIndex = (pageNow - 1) * pageSize;
				try {
					List<Message> result = this.messageDao.searchMessageByKey(key, state, userId, startIndex, pageSize,groupid);
					if (result != null && !result.isEmpty()) {
						for (Message t : result) {
							pageSplit.addData((T) t);
						}
					}
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("获取留言信息列表失败", e);
				}
				responseObj.setData(pageSplit);
			} else {
				responseObj.setMessage("没有留言");
			}
			return responseObj;
		} catch (ServiceException e) {
			throw e;
		}
	}

	public ResponseObject<Map<String, String>> getMessageCount(String userId) throws ServiceException {
		try {
			String state = Constant.MESSAGE_STATE_USER_NOT_DEAL;
			if (StringUtil.isEmpty(userId)) {
				state = Constant.MESSAGE_STATE_EMP_NOT_DEAL;
			}
			int totalCount = this.messageDao.countByKey("%", null, userId);
			int nodealCount = this.messageDao.countByKey("%", state, userId);
			Map<String, String> map = new HashMap<String, String>();
			map.put("totalCount", String.valueOf(totalCount));
			map.put("count", String.valueOf(nodealCount));
			ResponseObject<Map<String, String>> responseObject = new ResponseObject<Map<String, String>>(
			        ResponseCode.SUCCESS_CODE);
			responseObject.setData(map);
			return responseObject;
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}

}

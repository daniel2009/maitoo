package com.meitao.service.impl;

import java.util.Date;
import java.util.List;







import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.meitao.dao.ConsigneeSendpersonDao;

import com.meitao.service.ConsigneeSendpersonService;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.exception.ServiceException;

import com.meitao.model.ConsigneeSendperson;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;

@Service("consigneeSendpersonService")
public class ConsigneeSendpersonServiceImpl extends BasicService implements ConsigneeSendpersonService {
	@Autowired
	private ConsigneeSendpersonDao consigneeSendpersonDao;

	public ResponseObject<Object> deleteConsigneeSendperson(String id, String userId) throws ServiceException {
		ResponseObject<Object> responseObj = new ResponseObject<Object>();
		if (StringUtil.isEmpty(id) || StringUtil.isEmpty(userId)) {
			responseObj.setCode(ResponseCode.PARAMETER_ERROR);
			responseObj.setMessage("参数无效");
		} else {
			try {
				int iresult = this.consigneeSendpersonDao.delete(id, userId);
				if (iresult > 0) {
					responseObj.setCode(ResponseCode.SUCCESS_CODE);
				} else {
					responseObj.setCode(ResponseCode.CONSIGNEE_DELETE_FAILURE);
					responseObj.setMessage("根据id删除发件人信息失败，id:" + id);
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("删除发件人信息失败", e);
			}
		}
		return responseObj;
	}

	public ResponseObject<ConsigneeSendperson> getById(String id, String userId) throws ServiceException {
		ResponseObject<ConsigneeSendperson> responseObj = new ResponseObject<ConsigneeSendperson>();
		if (StringUtil.isEmpty(id) || StringUtil.isEmpty(userId)) {
			responseObj.setCode(ResponseCode.PARAMETER_ERROR);
			responseObj.setMessage("参数无效");
		} else {
			try {
				ConsigneeSendperson da = this.consigneeSendpersonDao.getUserinfoById(id, userId);
				responseObj.setCode(ResponseCode.SUCCESS_CODE);
				responseObj.setData(da);
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("根据id获取发件人信息失败", e);
			}
		}
		return responseObj;
	}

	

	

	public ResponseObject<Object> modifySendperson(ConsigneeSendperson consigneeInfo) throws ServiceException {
		ResponseObject<Object> responseObj = new ResponseObject<Object>();
		if (null == consigneeInfo) {
			responseObj.setCode(ResponseCode.PARAMETER_ERROR);
			responseObj.setMessage("参数无效");
		} else {
			try {
				//consigneeInfo.setLastDate(DateUtil.date2String(new Date()));
				consigneeInfo.setModifyDate(DateUtil.date2String(new Date()));
				int iresult = this.consigneeSendpersonDao.update(consigneeInfo);
				if (iresult > 0) {
					responseObj.setCode(ResponseCode.SUCCESS_CODE);
				} else {
					responseObj.setCode(ResponseCode.CONSIGNEE_MODIFY_FAILURE);
					responseObj.setMessage("修改发货地址信息失败");
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("修改发货地址信息失败", e);
			}
		}
		return responseObj;
	}

	
	
	public ResponseObject<Object> saveConsigneeSendperson(ConsigneeSendperson consigneeInfo) throws ServiceException {
		ResponseObject<Object> responseObj = new ResponseObject<Object>();
		if (null == consigneeInfo) {
			responseObj.setCode(ResponseCode.PARAMETER_ERROR);
			responseObj.setMessage("参数无效");
		} else {
			try {
				consigneeInfo.setModifyDate(DateUtil.date2String(new Date()));
				consigneeInfo.setCreateDate(DateUtil.date2String(new Date()));
				int iresult=this.consigneeSendpersonDao.insert(consigneeInfo);
				
				
				
				if (iresult > 0) {
					responseObj.setCode(ResponseCode.SUCCESS_CODE);
				} else {
					responseObj.setCode(ResponseCode.CONSIGNEE_INSERT_FAILURE);
					responseObj.setMessage("添加发货地址信息失败");
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("添加发货地址信息失败", e);
			}
		}
		return responseObj;
	}

	//增加一个根据用户提交的信息模糊查找接口
	public ResponseObject<PageSplit<ConsigneeSendperson>> getByInfo(String userId, String name, int pageSize, int pageNow)
	        throws ServiceException {
		if (StringUtil.isEmpty(userId)) {
			return new ResponseObject<PageSplit<ConsigneeSendperson>>(ResponseCode.PARAMETER_ERROR, "参数无效");
		} else {
			try {
				String key=null;
				if(StringUtil.isEmpty(name))
				{
					key=null;
				}
				else
				{
					key = StringUtil.escapeStringOfSearchKey(name);
				}
				int rowCount = 0;
				try {
					rowCount = this.consigneeSendpersonDao.countByInfo(userId, key);
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("获取发件人信息数量失败", e);
				}

				if (rowCount > 0) {
					pageSize = Math.max(pageSize, 1);
					int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
					pageNow = Math.min(pageNow, pageCount);
					PageSplit<ConsigneeSendperson> pageSplit = new PageSplit<ConsigneeSendperson>();
					pageSplit.setPageCount(pageCount);
					pageSplit.setPageNow(pageNow);
					pageSplit.setRowCount(rowCount);
					pageSplit.setPageSize(pageSize);

					int startIndex = (pageNow - 1) * pageSize;
					try {
						List<ConsigneeSendperson> result = this.consigneeSendpersonDao.searchByInfo(userId, key,
						        startIndex, pageSize);
						if (result != null && !result.isEmpty()) {
							for (ConsigneeSendperson t : result) {
								pageSplit.addData(t);
							}
						}
					} catch (Exception e) {
						throw ExceptionUtil.handle2ServiceException("获取发件人记录出错", e);
					}
					ResponseObject<PageSplit<ConsigneeSendperson>> responseObj = new ResponseObject<PageSplit<ConsigneeSendperson>>();
					responseObj.setCode(ResponseCode.SUCCESS_CODE);
					responseObj.setData(pageSplit);
					return responseObj;
				} else {
					return new ResponseObject<PageSplit<ConsigneeSendperson>>(ResponseCode.SUCCESS_CODE, "没有找到对应的发件人");
				}
			} catch (ServiceException e) {
				throw e;
			}
		}
	}
}

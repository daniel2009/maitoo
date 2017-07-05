package com.meitao.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitao.dao.RenlingDao;
import com.meitao.dao.RenlingPersonDao;
import com.meitao.dao.UserDao;

import com.meitao.service.RenlingService;

import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;

import com.meitao.common.util.ExceptionUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.exception.ServiceException;

import com.meitao.model.PageSplit;
import com.meitao.model.RenlingPersons;
import com.meitao.model.ResponseObject;

import com.meitao.model.User;

import com.meitao.model.RenlingBaoguo;

@Service("renlingService")
public class RenlingServiceImpl implements RenlingService {
	@Autowired
	private RenlingDao renlingdao;
	@Autowired
	private RenlingPersonDao renlingpersondao;
	@Autowired
	private UserDao userdao;
	

	/*
	 * kai 20151020 添加认领包裹的通知信息
	 */
	public ResponseObject<Map<String, String>> addRenling(RenlingBaoguo renling)
			throws ServiceException {
		try {
			if (renling == null || renling.getBaoguoId().equalsIgnoreCase("")
					|| renling.getEmpId().equalsIgnoreCase("")) {
				return new ResponseObject<Map<String, String>>(
						ResponseCode.PARAMETER_ERROR, "参数无效");
			}

			int k = this.renlingdao.insertRenling(renling);
			if (k < 1) {
				return new ResponseObject<Map<String, String>>(
						ResponseCode.PARAMETER_ERROR, "插入失败");
			}

			ResponseObject<Map<String, String>> result = new ResponseObject<Map<String, String>>(
					ResponseCode.SUCCESS_CODE);
			Map<String, String> map = new HashMap<String, String>();
			// map.put("id", order.getId());
			// map.put("orderId", orderId);
			result.setData(map);

			return result;
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}

	/*
	 * 根据条件查找认领通知清单 kai 20151020
	 */
	public ResponseObject<PageSplit<RenlingBaoguo>> searchByKey(String rid,
			String key, String column, String state, String sdate,
			String edate, int pageSize, int pageNow) throws ServiceException {

		try {
			key = StringUtil.escapeStringOfSearchKey(key);
			int rowCount = 0;
			try {
				rowCount=this.renlingdao.countByKey(rid, key, column, sdate, edate, null, state);
				/*rowCount = this.transhipmentBillDao.countByKey(tid, key,
						column, sdate, edate, null, null);*/
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("获取运单数量失败", e);
			}

			ResponseObject<PageSplit<RenlingBaoguo>> responseObj = new ResponseObject<PageSplit<RenlingBaoguo>>(
					ResponseCode.SUCCESS_CODE);
			if (rowCount > 0) {
				pageSize = Math.max(pageSize, 1);
				int pageCount = rowCount / pageSize
						+ (rowCount % pageSize == 0 ? 0 : 1);
				pageNow = Math.min(pageNow, pageCount);
				PageSplit<RenlingBaoguo> pageSplit = new PageSplit<RenlingBaoguo>();
				pageSplit.setPageCount(pageCount);
				pageSplit.setPageNow(pageNow);
				pageSplit.setRowCount(rowCount);
				pageSplit.setPageSize(pageSize);

				int startIndex = (pageNow - 1) * pageSize;
				try {
					
					List<RenlingBaoguo> bills = this.renlingdao.searchByKey(rid, key, column, sdate, edate, null, startIndex, pageSize, state);
						/*	.searchByKey(rid, key, column, sdate, edate, null,
									startIndex, pageSize, null);*/
					if (bills != null && !bills.isEmpty()) {
						for (RenlingBaoguo tb : bills) {
							pageSplit.addData(tb);
						}
					}
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("获取认领失败", e);
				}
				responseObj.setData(pageSplit);
			} else {
				responseObj.setMessage("没有认领信息");
			}
			return responseObj;
		} catch (ServiceException e) {
			throw e;
		}
	}
	/*
	*删除认领单信息
	*/
	public ResponseObject<Object> deleteRenlingbill(List<String> ids) throws ServiceException {
		ResponseObject<Object> responseObj = new ResponseObject<Object>();
		if (ids == null || ids.isEmpty()) {
			responseObj.setCode(ResponseCode.PARAMETER_ERROR);
			responseObj.setMessage("参数无效");
		} else {
			int count = ids.size();
			try {
				
				
				int irt = this.renlingdao.deleteMultiRenlingBill(ids);
				
				if (irt == count) {
					
					//删除相关的认领信息
					this.renlingpersondao.deleteMultiRenlingPersion(ids);
					
					
					responseObj.setCode(ResponseCode.SUCCESS_CODE);
				} else {
					// 进行事务回滚
					throw new Exception();
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return responseObj;
	}
	
	
	/*
	 * 根据id获取对应的认领信息
	 * */
	public ResponseObject<RenlingBaoguo> getRenlingbyid(String id) throws ServiceException
	{
		if (StringUtil.isEmpty(id)) {
			return new ResponseObject<RenlingBaoguo>(ResponseCode.PARAMETER_ERROR, "参数无效");
		}

		try {
			ResponseObject<RenlingBaoguo> responseObj = new ResponseObject<RenlingBaoguo>();
			RenlingBaoguo renling=this.renlingdao.getRenlingBillbyid(id);
			
			
			if (renling != null) {
				List<RenlingPersons> list=this.renlingpersondao.getRenlingPersonbyRenlinginfo(renling.getId(), Constant.RENLING_PERSON_ADMIN);
				if((list!=null)&&(list.size()!=0))
				{
				    renling.setReninfo(list.get(list.size()-1));
				}
				responseObj.setCode(ResponseCode.SUCCESS_CODE);
				responseObj.setData(renling);
			} else {
				responseObj.setCode(ResponseCode.NEWS_NOT_EXISTS);
				responseObj.setMessage("认领信息不存在!");
			}
			return responseObj;
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	
	

	/*
	 * kai 20151020 添加认领包裹的通知信息
	 */
	public ResponseObject<Map<String, String>> modifyRenling(RenlingBaoguo renling)
			throws ServiceException {
		try {
			if (renling == null || renling.getBaoguoId().equalsIgnoreCase("")
					|| renling.getEmpId().equalsIgnoreCase("")) {
				return new ResponseObject<Map<String, String>>(
						ResponseCode.PARAMETER_ERROR, "参数无效");
			}
			
			if((renling.getId()==null)||((renling.getId().equalsIgnoreCase(""))))
			{
				return new ResponseObject<Map<String, String>>(
						ResponseCode.PARAMETER_ERROR, "参数无效");
			}

			//如果是认领状态，要在用户认领信息表中插入信息
			if((renling.getReninfo()!=null)&&(renling.getState().equalsIgnoreCase(Constant.RENLING_STATE1)))//设为认领状态，要插入认领信息
			{
				//设置用户信息
				User user=this.userdao.getUserByAccount(renling.getReninfo().getPhone());
				if(user!=null)
				{
					renling.setUserId(user.getId());
					renling.getReninfo().setUserId(user.getId());
				}
				renling.getReninfo().setRenlingId(renling.getId());
				renling.getReninfo().setBaoguoId(renling.getBaoguoId());
				if((renling.getReninfo().getId()==null)||(renling.getReninfo().getId().equalsIgnoreCase("")))//原来没有创建数据
				{
					renling.getReninfo().setCreatePerson(Constant.RENLING_PERSON_ADMIN);					
					this.renlingpersondao.insertRenlingPersons(renling.getReninfo());
				}
				else
				{
					this.renlingpersondao.updateRenlingPersonbyid(renling.getReninfo());
				}
				
				
				
			}
			int k=this.renlingdao.updateRenling(renling);
			
			
			
			if (k < 1) {
				return new ResponseObject<Map<String, String>>(
						ResponseCode.PARAMETER_ERROR, "修改失败");
			}

			
			
			ResponseObject<Map<String, String>> result = new ResponseObject<Map<String, String>>(
					ResponseCode.SUCCESS_CODE);
			Map<String, String> map = new HashMap<String, String>();
			// map.put("id", order.getId());
			// map.put("orderId", orderId);
			result.setData(map);

			return result;
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	@Override
	public ResponseObject<Integer> countNeedAudit() throws ServiceException {
		ResponseObject<Integer> responseObject = new ResponseObject<Integer>();
		try {
			int count =this.renlingdao.countNeedAudit();
			responseObject.setCode(ResponseCode.SUCCESS_CODE);
			responseObject.setData(count);
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
		return responseObject;
	}

	@Override
	public ResponseObject<Object> updateByAdmin(RenlingBaoguo renling)
			throws ServiceException {
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		try{
			//who can explain why use the fucking map in that method?
			ResponseObject<Map<String, String>> responseMap = this.modifyRenling(renling);
			responseObject.setCode(responseMap.getCode());
			responseObject.setMessage(responseMap.getMessage());
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
		return responseObject;
	}
}

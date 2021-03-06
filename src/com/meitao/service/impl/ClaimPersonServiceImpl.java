package com.meitao.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitao.dao.RenlingDao;
import com.meitao.dao.RenlingPersonDao;
import com.meitao.service.ClaimPersonService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.exception.ServiceException;
import com.meitao.model.PageSplit;
import com.meitao.model.RenlingBaoguo;
import com.meitao.model.RenlingPersons;
import com.meitao.model.ResponseObject;

@Service("claimPersonService")
public class ClaimPersonServiceImpl implements ClaimPersonService{

	@Autowired
	private RenlingPersonDao renlingPersonDao;
	@Autowired
	private RenlingDao renlingDao;
	@Override
	public ResponseObject<Object> add(RenlingPersons renlingPerson) throws ServiceException {
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		if(null == renlingPerson || StringUtil.isEmpty(renlingPerson.getRenlingId())){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数无效");
		}else{
			renlingPerson.setCreatePerson(Constant.RENLING_PERSON_USER);//I guess it only can be user in here
			renlingPerson.setCreateDate(DateUtil.date2String(new Date()));
			renlingPerson.setModifyDate(DateUtil.date2String(new Date()));
			renlingPerson.setState(Constant.CLAIM_PERSON_STATE_AUDIT);
			try{
				int result = this.renlingPersonDao.insertRenlingPersons(renlingPerson);
				//update some info in renlingBaoguo
				if(result > 0){
					/**
					 * there is no need to update renlingbaoguo, dead line of these code : 2015.12.10
					 */
					//RenlingBaoguo renlingBaoguo = new RenlingBaoguo();
					//renlingBaoguo.setId(renlingPerson.getRenlingId());
					//renlingBaoguo.setModifyDate(DateUtil.date2String(new Date()));
					//renlingBaoguo.setTranId("what the fuck!");//wtf is this?
					//renlingBaoguo.setUserId(renlingPerson.getUserId());
					//renlingBaoguo.setState(Constant.CLAIM_PACKAGE_STATE_AUDIT);
//					result = renlingDao.updateClaimedByUser(renlingBaoguo);
//					if(result > 0){
						responseObject.setCode(ResponseCode.SUCCESS_CODE);
//					}else{
//						responseObject.setCode(ResponseCode.CLAIM_PERSON_UPDATE_FAIL);
//						responseObject.setMessage("更新包裹认领记录失败");
//					}
				}else{
					responseObject.setCode(ResponseCode.CLAIM_PERSON_INSERT_FAIL);
					responseObject.setMessage("包裹认领失败");
				}
			}catch(Exception e){
				throw ExceptionUtil.handle2ServiceException("add claimPerson error", e);
			}
		}
		return responseObject;
	}
	@Override
	public ResponseObject<RenlingPersons> findLastClaimedByClaimPackage(
			String renlingId, String userId) throws ServiceException {
		ResponseObject<RenlingPersons> responseObject = new ResponseObject<RenlingPersons>();
		if(StringUtil.isEmpty(renlingId) || StringUtil.isEmpty(userId)){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数错误");
		}else{
			try{
				RenlingPersons renlingPerson = renlingPersonDao.findLastClaimedByClaimPackageAndUser(renlingId, userId);
				if(renlingPerson == null){
					responseObject.setCode(ResponseCode.CLAIM_PERSON_LAST_CLAIMED_NOT_EXISTS);
					responseObject.setMessage("没有找到指定认领数据");
				}else{
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
					responseObject.setData(renlingPerson);
				}
			}catch(Exception e){
				throw ExceptionUtil.handle2ServiceException("find last claimed error", e);
			}
		}
		return responseObject;
	}
	@Override
	public ResponseObject<PageSplit<RenlingBaoguo>> findByUser(String userId, String state, String nameCondition, int pageIndex, int pageSize) throws ServiceException {
		ResponseObject<PageSplit<RenlingBaoguo>> responseObject = new ResponseObject<PageSplit<RenlingBaoguo>>();
		if(StringUtil.isEmpty(userId)){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数无效");
		}else{
			if(!StringUtil.isEmpty(nameCondition)){
				nameCondition = new StringBuilder("%").append(nameCondition).append("%").toString();
			}
			try{
				int rowCount = this.renlingPersonDao.countByUser(userId, state, nameCondition);
				if(rowCount > 0){
					pageSize = Math.max(pageSize, 1);
					int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
					pageIndex = Math.min(pageIndex, pageCount);
					int firstResult = (pageIndex - 1) * pageSize;
					PageSplit<RenlingBaoguo> pageSplit = new PageSplit<RenlingBaoguo>();
					pageSplit.setPageCount(pageCount);
					pageSplit.setPageNow(pageIndex);
					pageSplit.setPageSize(pageSize);
					pageSplit.setRowCount(rowCount);
					pageSplit.setPageSize(pageSize);
					List<RenlingBaoguo> list = renlingPersonDao.findByUser(userId, state, nameCondition, firstResult, pageSize);
					pageSplit.setDatas(list);
					responseObject.setData(pageSplit);
				}else{
					responseObject.setMessage("没有自己的认领包裹数据");
				}
			}catch(Exception e){
				e.printStackTrace();
				ExceptionUtil.handle2ServiceException("claim package find by user fail", e);
			}
		}
		return responseObject;
	}
	@Override
	public ResponseObject<Object> deleteByUser(String id, String userId)
			throws ServiceException {
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		if(StringUtil.isEmpty(id)){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数无效");
		}else if(StringUtil.isEmpty(userId)){
			responseObject.setCode(ResponseCode.NEED_LOGIN);
		}else{
			try{
				int result = this.renlingPersonDao.deleteByUser(id, userId);
				if(result > 0){
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}else{
					responseObject.setCode(ResponseCode.CLAIM_PERSON_DELETE_FAIL);
					responseObject.setMessage("找不到指定删除数据，或不是该数据的用户");
				}
			}catch(Exception e){
				throw ExceptionUtil.handle2ServiceException("delete claimPerson error", e);
			}
		}
		return responseObject;
	}
	@Override
	public ResponseObject<RenlingPersons> findById(String id) throws ServiceException {
		ResponseObject<RenlingPersons> responseObject = new ResponseObject<RenlingPersons>();
		if(StringUtil.isEmpty(id)){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数无效");
		}else{
			try{
				RenlingPersons renlingPerson = this.renlingPersonDao.findById(id);
				if(renlingPerson != null){
					responseObject.setData(renlingPerson);
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}else{
					responseObject.setCode(ResponseCode.CLAIM_PERSON_ID_NO_EXISTS);
					responseObject.setMessage("找不到指定数据");
				}
			}catch(Exception e){
				throw ExceptionUtil.handle2ServiceException("find claimPerson error", e);
			}
		}
		return responseObject;
	}
	@Override
	public ResponseObject<Object> reclaimByUser(RenlingPersons renlingPerson)
			throws ServiceException {
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		if(null == renlingPerson || StringUtil.isEmpty(renlingPerson.getId())){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数无效");
		}else{
			renlingPerson.setModifyDate(DateUtil.date2String(new Date()));
			renlingPerson.setState(Constant.CLAIM_PERSON_STATE_AUDIT);
			try{
				int result = this.renlingPersonDao.updateByReclaim(renlingPerson);
				if(result > 0){
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}else{
					responseObject.setCode(ResponseCode.CLAIM_PERSON_UPDATE_BY_RECLAIM_FAIL);
					responseObject.setMessage("包裹认领失败");
				}
			}catch(Exception e){
				throw ExceptionUtil.handle2ServiceException("update claimPerson by user reclaim error", e);
			}
		}
		return responseObject;
	}
}

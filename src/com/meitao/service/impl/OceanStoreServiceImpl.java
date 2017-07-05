package com.meitao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitao.dao.OceanStoreDao;
import com.meitao.service.OceanStoreService;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.exception.ServiceException;
import com.meitao.model.OceanStore;
import com.meitao.model.ResponseObject;

@Service("oceanStoreService")
public class OceanStoreServiceImpl implements OceanStoreService {

	@Autowired
	private OceanStoreDao oceanStoreDao;
	@Override
	public ResponseObject<Object> add(OceanStore oceanStore)
			throws ServiceException {
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		if(null == oceanStore){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数无效");
		}else{
			try{
				int iresult = this.oceanStoreDao.insert(oceanStore);
				if(iresult > 0){
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}else{
					responseObject.setCode(ResponseCode.OCEAN_STORE_INSERT_FAIL);
					responseObject.setMessage("add oceanStore fail");
				}
			}catch(Exception e){
				throw ExceptionUtil.handle2ServiceException("add oceanStore fail", e);
			}
		}
		return responseObject;
	}

	@Override
	public ResponseObject<OceanStore> getById(String id)
			throws ServiceException {
		ResponseObject<OceanStore> responseObject = new ResponseObject<OceanStore>();
		if(StringUtil.isEmpty(id)){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("parameter invalid");
		}else{
			try{
				OceanStore oceanStore = this.oceanStoreDao.getById(id);
				if(null != oceanStore){
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
					responseObject.setData(oceanStore);
				}else{
					responseObject.setCode(ResponseCode.OCEAN_STORE_ID_NOT_EXISTS);
					responseObject.setMessage("no expect oceanStore data in database");
				}
			}catch(Exception e){
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return responseObject;
	}

	@Override
	public ResponseObject<Object> modify(OceanStore oceanStore)
			throws ServiceException {
		// TODO Auto-generated method stub
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		if(null == oceanStore){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("parameter error");
		}else{
			try{
				int iresult = this.oceanStoreDao.update(oceanStore);
				if(iresult > 0){
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}else{
					responseObject.setCode(ResponseCode.OCEAN_STORE_UPDATE_FAIL);
					responseObject.setMessage("oceanStore update fail");
				}
			}catch(Exception e){
				throw ExceptionUtil.handle2ServiceException("oceanStore update fail", e);
			}
			
		}
		return responseObject;
	}

	@Override
	public ResponseObject<List<OceanStore>> getAll() throws ServiceException {
		ResponseObject<List<OceanStore>> responseObject = new ResponseObject<List<OceanStore>>();
		try{
			List<OceanStore> list = this.oceanStoreDao.getAll();
			responseObject.setCode(ResponseCode.SUCCESS_CODE);
			responseObject.setData(list);
		}catch(Exception e){
			throw ExceptionUtil.handle2ServiceException("fail to get all oceanStore", e);
		}
		return responseObject;
	}

	@Override
	public ResponseObject<Object> delete(String id) throws ServiceException {
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		if(StringUtil.isEmpty(id)){
			responseObject.setMessage("parameter error");
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
		}else{
			try{
				int iresult = this.oceanStoreDao.delete(id);
				if(iresult > 0){
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}else{
					responseObject.setCode(ResponseCode.OCEAN_STORE_DELETE_FAIL);
					responseObject.setMessage("oceanStore delete fail");
				}
			}catch(Exception e){
				throw ExceptionUtil.handle2ServiceException("delelte oceanStore fail", e);
			}
		}
		return responseObject;
	}

}

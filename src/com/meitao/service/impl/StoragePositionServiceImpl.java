package com.meitao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitao.dao.StorageDao;
import com.meitao.dao.StoragePositionDao;
import com.meitao.dao.StoragePositionRecordDao;
import com.meitao.service.StoragePositionService;
import com.meitao.service.StorageService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.common.util.PageSplitUtil;
import com.meitao.common.util.StoragePositionUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.exception.ServiceException;
import com.meitao.model.Order;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.Storage;
import com.meitao.model.StoragePosition;
import com.meitao.model.StoragePositionRecord;
import com.meitao.model.TranshipmentBill;
import com.meitao.model.User;

@Service("storagePositionService")
public class StoragePositionServiceImpl implements StoragePositionService{
	@Autowired
	private StorageDao storageDao;
	@Autowired
	private StoragePositionDao storagePositionDao;
	@Autowired
	private StoragePositionRecordDao storagePositionRecordDao;
	@Resource(name = "storageService")
	private StorageService storageService;

	@Override
	public ResponseObject<Object> useStorage(StoragePosition storagePosition, TranshipmentBill transhipmentBill, int operation) throws ServiceException {
		try {
			if(0==operation){
				User user = new User();
				user.setId(transhipmentBill.getUserId());
				List<StoragePosition> list = this.findSamePackage(transhipmentBill);
				if(list != null && !list.isEmpty()){
					for(StoragePosition removePosition : list){
						this.takeOutFromStorage(removePosition, transhipmentBill.getId());
					}
				}
				return this.bringIntoStorage(storagePosition, user, transhipmentBill.getId());
			}else{
				return this.takeOutFromStorage(storagePosition, transhipmentBill.getId());
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	@Override
	public ResponseObject<Object> useStorage(StoragePosition storagePosition, Order order, int operation) throws ServiceException {
		try {
			if(0==operation){
				User user = new User();
				user.setId(order.getUserId());
				List<StoragePosition> list = this.findSamePackage(order);
				if(list != null){
					for(StoragePosition removePosition : list){
						this.takeOutFromStorage(removePosition, order.getId());
					}
				}
				return this.bringIntoStorage(storagePosition, user, order.getId());
			}else{
				return this.takeOutFromStorage(storagePosition, order.getId());
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	@Override
	public ResponseObject<Object> useStorage(StoragePosition storagePosition, User user, int operation) throws ServiceException {
		try {
			if(0==operation){
				return this.bringIntoStorage(storagePosition, user, null);
			}if (3 == operation) {
				return this.takeOutFromStorage(storagePosition, storagePosition.getRecordList().get(0).getRelateId());
			}else{
				return this.takeOutFromStorage(storagePosition, null);
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	private ResponseObject<Object> bringIntoStorage(StoragePosition storagePosition, User user, String relateId) throws ServiceException {
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		if(null == storagePosition || null == user){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数错误");
		}else{
			try {
				StoragePosition nowStoragePosition = this.storagePositionDao.findById(storagePosition.getId());
				if(StringUtil.isEmpty(nowStoragePosition.getUserId()) || nowStoragePosition.getUserId().equals(user.getId())){//没人使用，或者自己使用
					storagePosition.setAvailable("0");
					storagePosition.setUserId(user.getId());
					storagePosition.setModifyDate(DateUtil.date2String(new Date()));
					int result1 = this.storagePositionDao.updateUseStoragePosition(storagePosition);
					int result2 = 0;
					if(null == relateId){
						Storage storage = this.storageDao.findById(storagePosition.getStorageId());
						String[] stateArray = StoragePositionUtil.getStateArray(storage, 0);
						result2 = this.storagePositionRecordDao.insertByUser(storage, storagePosition, user, stateArray);
					}else{
						result2 = this.storagePositionRecordDao.insert(new StoragePositionRecord(storagePosition.getId(), relateId));
					}
					if(result1 > 0 && result2 > 0){
						responseObject.setCode(ResponseCode.SUCCESS_CODE);
					}else{
						responseObject.setCode(ResponseCode.STORAGE_POSITION_UPDATE_BY_USE_FAIL);
						responseObject.setMessage("给指定用户指定储藏间失败");
					}
				}else{
					responseObject.setCode(ResponseCode.STORAGE_POSITION_UPDATE_BY_USE_FAIL);
					responseObject.setMessage("给指定用户指定储藏间失败");
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return responseObject;
	}
	private ResponseObject<Object> takeOutFromStorage(StoragePosition storagePosition, String relateId) throws ServiceException {
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		if(null == storagePosition){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数错误");
		}else{
			try {
				int result = 0;
				if(StringUtil.isEmpty(relateId)){
					result = this.storagePositionRecordDao.deleteByStoragePosition(storagePosition);
				}else{
					result = this.storagePositionRecordDao.deleteByRelate(new StoragePositionRecord(storagePosition.getId(), relateId));
				}
				if(result <= 0){//指定包裹没放在这里
					responseObject.setCode(ResponseCode.STORAGE_POSITION_RECORD_DELETE_BY_RELATE_FAIL);
					responseObject.setMessage("指定包裹没放在这里");
				}else{
					int count = this.storagePositionRecordDao.countByStoragePosition(storagePosition);
					if(count<=0){
						storagePosition.setUserId(null);
						storagePosition.setAvailable("1");
					}
					storagePosition.setModifyDate(DateUtil.date2String(new Date()));
					result = this.storagePositionDao.updateUseStoragePosition(storagePosition);
					if(result > 0){
						responseObject.setCode(ResponseCode.SUCCESS_CODE);
					}else{
						responseObject.setCode(ResponseCode.STORAGE_POSITION_UPDATE_BY_USE_FAIL);
						responseObject.setMessage("给指定用户提取包裹储藏间失败");
					}
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return responseObject;
	}
	
	@Override
	public ResponseObject<StoragePosition> findStoragePosition(TranshipmentBill transhipmentBill) throws ServiceException {
		try {
			return this.findStoragePosition(transhipmentBill.getWarehouseId(), StoragePositionUtil.getType(transhipmentBill), transhipmentBill.getTranWarehouseId(), transhipmentBill.getId());
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	@Override
	public ResponseObject<StoragePosition> findStoragePosition(Order order) throws ServiceException {
		try {
			return this.findStoragePosition(order.getWarehouseId(), Constant.STORAGE_TYPE_ORDER, order.getChannelId(), order.getId());
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	@Override
	public ResponseObject<StoragePosition> findStoragePosition(String warehouseId, String type, String typeRelateId, String relateId) throws ServiceException {
		try {
			Storage storage = null;
			StoragePositionRecord storagePositionRecord = null; 
			if(StringUtil.hasText(warehouseId, type, typeRelateId, relateId)){
				storage = new Storage();
				storage.setWarehouseId(warehouseId);
				storage.setType(type);
				storage.setTypeRelateId(typeRelateId);
				storagePositionRecord = new StoragePositionRecord(null, relateId);
			}
			return this.findStoragePosition(storage, storagePositionRecord);
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	@Override
	public ResponseObject<StoragePosition> findStoragePosition(Storage storage, StoragePositionRecord storagePositionRecord) throws ServiceException {
		ResponseObject<StoragePosition> responseObject = new ResponseObject<StoragePosition>();
		if(null == storage || null == storagePositionRecord){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数错误");
		}else{
			try {
				StoragePosition storagePosition = this.storagePositionDao.findStoragePositionByStorageAndRecord(storage, storagePositionRecord);
				if(null == storagePosition){
					responseObject.setCode(ResponseCode.STORAGE_POSITION_NO_EXIST);
					responseObject.setMessage("指定储藏间不纯在");
				}else{
					responseObject.setData(storagePosition);
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return responseObject;
	}
	@Override
	public ResponseObject<PageSplit<StoragePosition>> findStoragePosition(User user, Storage storage, int pageIndex, int pageSize) throws ServiceException {
		ResponseObject<PageSplit<StoragePosition>> responseObject = new ResponseObject<PageSplit<StoragePosition>>();
		if(user == null || (StringUtil.isEmpty(user.getId()) && StringUtil.isEmpty(user.getRealName()))){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数错误");
		}else{
			try {
				StoragePosition storagePosition = new StoragePosition();
				storagePosition.setUserId(user.getId());
				storagePosition.setUserName(StringUtil.escapeStringOfSearchKey(user.getRealName()));
				int rowCount = this.storagePositionDao.countStoragePositionByUser(storagePosition, storage);
				if(rowCount > 0){
					PageSplit<StoragePosition> pageSplit = new PageSplit<StoragePosition>();
					int firstResult = PageSplitUtil.setPageAndGetFirstResult(pageSplit, pageIndex, pageSize, rowCount);
					List<StoragePosition> list = this.storagePositionDao.findStoragePositionByUser(storagePosition, storage, firstResult, pageSize);
					pageSplit.setDatas(list);
					responseObject.setData(pageSplit);
				}else{
					responseObject.setMessage("没有储藏间信息");
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return responseObject;
	}
	@Override
	public ResponseObject<StoragePosition> findShouldIn(Order order) throws ServiceException {
		//如果是用来出库，只需返回当前所在
		ResponseObject<StoragePosition> responseObject = this.findStoragePosition(order);
		//如果是用来入库，需要查找一个可以用
		if(!ResponseCode.SUCCESS_CODE.equals(responseObject.getCode()) && StoragePositionUtil.getOperation(order)==0){
			responseObject = this.findStoragePositionByStorage4User(order.getWarehouseId(), StoragePositionUtil.getType(order), order.getChannelId(), order.getUserId());
		}
		return responseObject;
	}
	@Override
	public ResponseObject<StoragePosition> findShouldIn(TranshipmentBill transhipmentBill) throws ServiceException {
		//如果是用来出库，只需返回当前所在
		ResponseObject<StoragePosition> responseObject = this.findStoragePosition(transhipmentBill);
		//如果是用来入库，需要查找一个可以用
		if(!ResponseCode.SUCCESS_CODE.equals(responseObject.getCode()) && StoragePositionUtil.getOperation(transhipmentBill)==0){
			//Object object =  StoragePositionUtil.getType(transhipmentBill);
			//String gwhidString = transhipmentBill.getWarehouseId();
			//String gtwh = transhipmentBill.getTranWarehouseId();
			//String userIdString = transhipmentBill.getUserId();
			responseObject = this.findStoragePositionByStorage4User(transhipmentBill.getWarehouseId(), StoragePositionUtil.getType(transhipmentBill), transhipmentBill.getTranWarehouseId(), transhipmentBill.getUserId());
		}
		return responseObject;
	}
	
	@Override
	public ResponseObject<StoragePosition> findById(String id) throws ServiceException {
		ResponseObject<StoragePosition> responseObject = new ResponseObject<StoragePosition>();
		if(StringUtil.isEmpty(id)){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数错误");
		}else{
			try {
				StoragePosition storagePosition = this.storagePositionDao.findById(id);
				if(storagePosition == null){
					responseObject.setCode(ResponseCode.STORAGE_POSITION_NO_EXIST);
					responseObject.setMessage("指定储藏间不纯在");
				}else{
					responseObject.setData(storagePosition);
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return responseObject;
	}

	
	@Override
	public ResponseObject<StoragePosition> findByIdRelateId(String id, String relateId) throws ServiceException {
		ResponseObject<StoragePosition> responseObject = new ResponseObject<StoragePosition>();
		if(StringUtil.isEmpty(id)){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数错误");
		}else{
			try {
				StoragePosition storagePosition = this.storagePositionDao.findByIdRelateId(id,relateId);
				if(storagePosition == null){
					responseObject.setCode(ResponseCode.STORAGE_POSITION_NO_EXIST);
					responseObject.setMessage("指定储藏间不纯在");
				}else{
					responseObject.setData(storagePosition);
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return responseObject;
	}

	@Override
	public ResponseObject<List<StoragePosition>> findNoPositionUserByStorageId(String storageId) throws ServiceException {
		ResponseObject<List<StoragePosition>> responseObject = new ResponseObject<List<StoragePosition>>();
		if(StringUtil.isEmpty(storageId)){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数无效");
		}else{
			try {
				Storage storage = this.storageDao.findById(storageId);
				List<StoragePosition> list = this.storagePositionDao.findNoPositionUserByStorage(storage, StoragePositionUtil.getStateArray(storage, 0));
				if (list == null || list.size() <= 0) {
					responseObject.setCode(ResponseCode.STORAGE_LIST_ERROR);
					responseObject.setMessage("列表数据为空");
				} else {
					responseObject.setData(list);
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return responseObject;
	}
	@Override
	public ResponseObject<StoragePosition> findNoPositionRecordBySameStoragePosition(StoragePosition storagePosition) throws ServiceException {
		ResponseObject<StoragePosition> responseObject = new ResponseObject<StoragePosition>();
		if(null == storagePosition.getId()){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数无效");
		}else{
			try {
				storagePosition = this.storagePositionDao.findById(storagePosition.getId());
				if(null == storagePosition){
					responseObject.setCode(ResponseCode.STORAGE_POSITION_NO_EXIST);
					responseObject.setMessage("找不到该仓位");
				}else{
					Storage storage = this.storageDao.findById(storagePosition.getStorageId());
					User user = new User();
					user.setId(storagePosition.getUserId());
					StoragePosition result = this.storagePositionDao.findNoPositionRecordByStorageAndUser(storage, user, StoragePositionUtil.getStateArray(storage, 0));
					if (result != null) {
						result.setId(storagePosition.getId());
					}
					responseObject.setData(result);
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return responseObject;
	}

	@Override
	public ResponseObject<StoragePosition> findEmptyStoragePosition(Storage storage) throws ServiceException {
		ResponseObject<StoragePosition> responseObject = new ResponseObject<StoragePosition>();
		try {
			StoragePosition storagePosition = this.storagePositionDao.findNextEmptyStoragePositionByStorage(storage);
			if (storagePosition == null) {
				responseObject.setCode(ResponseCode.STORAGE_POSITION_OF_EMPTY_IN_STORAGE_NOT_EXISTS);
				responseObject.setMessage("本储藏间已经没有空的储藏间");
			} else {
				responseObject.setData(storagePosition);
				responseObject.setCode(ResponseCode.SUCCESS_CODE);
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
		return responseObject;
	}

	@Override
	public ResponseObject<StoragePosition> findStoragePositionByStorage4User(String warehouseId, String type, String typeRelateId, String userId) throws ServiceException {
		ResponseObject<StoragePosition> responseObject = new ResponseObject<StoragePosition>();
		if(!StringUtil.hasText(warehouseId, type, userId) || (!Constant.STORAGE_TYPE_AFTER_OPEN.equals(type) && StringUtil.isEmpty(typeRelateId))){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数无效");
		}else{
			ResponseObject<PageSplit<Storage>> storageResponseObject = this.storageService.findByWarehouseAndTypeAndRelateId(warehouseId, type, typeRelateId, 1, 1);
			if(ResponseCode.SUCCESS_CODE.equals(storageResponseObject.getCode())){
				responseObject = this.findStoragePositionByStorage4User(storageResponseObject.getData().getDatas().get(0), userId);
			}else{
				responseObject.setCode(ResponseCode.STORAGE_LIST_IS_EMPTY);
				responseObject.setMessage("指定储藏间不纯在");
				responseObject.setData(null);
			}
		}
		return responseObject;
	}
	@Override
	public ResponseObject<StoragePosition> findStoragePositionByStorage4User(Storage storage, String userId) throws ServiceException {
		ResponseObject<StoragePosition> responseObject = new ResponseObject<StoragePosition>();
		try {
			StoragePosition storagePosition = this.storagePositionDao.findByStorageIdAndUserId(storage.getId(), userId);
			if(null != storagePosition){//已有仓位
				responseObject.setData(storagePosition);
				responseObject.setCode(ResponseCode.SUCCESS_CODE);
			}else{
				storagePosition = this.storagePositionDao.findNextEmptyStoragePositionByStorage(storage);
				if (storagePosition == null) {
					responseObject.setCode(ResponseCode.STORAGE_POSITION_OF_EMPTY_IN_STORAGE_NOT_EXISTS);
					responseObject.setMessage("本储藏间已经没有空的储藏间");
				} else {
					responseObject.setData(storagePosition);
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}
				
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
		return responseObject;
	}
	
	/**
	 * userId没有用上2/4/16,author:li zhang
	 */
	@Override
	public ResponseObject<List<StoragePosition>> findStoragePositionByStorage4UserList(Storage storage, String userId) throws ServiceException {
		ResponseObject<List<StoragePosition>> responseObject = new ResponseObject<List<StoragePosition>>();
		try {
			/*
			StoragePosition storagePosition = this.storagePositionDao.findByStorageIdAndUserId(storage.getId(), userId);
			if(null != storagePosition){//已有仓位
				responseObject.setData(storagePosition);
				responseObject.setCode(ResponseCode.SUCCESS_CODE);
			}else{
			*/	
				//storagePosition = this.storagePositionDao.findNextEmptyStoragePositionByStorage(storage);
				List<StoragePosition> storagePositionList = this.storagePositionDao.findNextEmptyStoragePositionByStorageList(storage);
				if (null == storagePositionList  || storagePositionList.size() <= 0 ) {
					responseObject.setCode(ResponseCode.STORAGE_POSITION_OF_EMPTY_IN_STORAGE_NOT_EXISTS);
					responseObject.setMessage("本储藏间已经没有空的储藏间");
				} else {
					responseObject.setData(storagePositionList);
					
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}
				
			//}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
		return responseObject;
	}

	@Override
	public ResponseObject<Object> checkStoragePositionByUser(StoragePosition storagePosition, String userId) throws ServiceException {
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		try {
			if(StringUtil.isEmpty(storagePosition.getId())){
				storagePosition = this.storagePositionDao.findByNameAndPosition(storagePosition);
			}else{
				storagePosition = this.storagePositionDao.findById(storagePosition.getId());
			}
			boolean hasTarget = null != storagePosition;
			if(hasTarget){
				StoragePosition selfStoragePosition = this.storagePositionDao.findByStorageIdAndUserId(storagePosition.getStorageId(), userId);
				boolean hasSelf = null != selfStoragePosition;
				boolean emptyTarget = hasTarget && null==storagePosition.getUserId();
				boolean isSame = hasSelf && hasTarget && selfStoragePosition.getUserId().equals(storagePosition.getUserId());
				if(isSame || (!hasSelf && emptyTarget)){
					responseObject.setData(storagePosition.getId());
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}else if(hasSelf && !isSame){
					responseObject.setCode(ResponseCode.STORAGE_POSITION_USER_ALREADY_HAS);
					responseObject.setData(selfStoragePosition);
					responseObject.setMessage("用户已有储藏间,位置是:" + storagePosition.getPosition());
				}else {//1.!isSame && !emptyTarget	2.?
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
					responseObject.setData(false);
				}
			}else{
				responseObject.setCode(ResponseCode.SUCCESS_CODE);
				responseObject.setData(false);
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
		return responseObject;
	}
	
	/**
	 * 用于在非法入库的时候清理元包裹数据, 不管包裹状态，返回数据库中所有此包裹
	 * @param transhipmentBill
	 * @return
	 * @throws ServiceException
	 */
	private List<StoragePosition> findSamePackage(TranshipmentBill transhipmentBill) throws ServiceException{
		List<StoragePosition> list = null;
		if(null != transhipmentBill){
			try {
				Storage storage = new Storage();
				storage.setType("," + Constant.STORAGE_TYPE_AFTER_OPEN + "," + Constant.STORAGE_TYPE_BEFORE_OPEN);
				list = this.storagePositionDao.findSamePackage(storage, transhipmentBill.getId());
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return list;
	}
	private List<StoragePosition> findSamePackage(Order order) throws ServiceException{
		List<StoragePosition> list = null;
		if(null != order){
			try {
				Storage storage = new Storage();
				storage.setType("," + Constant.STORAGE_TYPE_ORDER);
				list = this.storagePositionDao.findSamePackage(storage, order.getId());
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return list;
	}
	
	@Override
	public ResponseObject<StoragePosition> findStoragePositionByWarehouseIdAndTypeAndRelateIdRowCol(
			String warehouseId, String type, String typeRelateId,
			String storageId, String colNumber, String rowNumber)
			throws ServiceException {
		ResponseObject<StoragePosition> responseStoragePosition = new ResponseObject<StoragePosition>();
		try {
			StoragePosition storagePosition = this.storagePositionDao.findStoragePositionByWarehouseIdAndTypeAndRelateIdRowCol(warehouseId, type, typeRelateId, storageId, colNumber, rowNumber);
			boolean hasTarget = null != storagePosition;
			if(hasTarget){
				responseStoragePosition.setCode(ResponseCode.SUCCESS_CODE);
				responseStoragePosition.setData(storagePosition);
			}else{
				responseStoragePosition.setCode(ResponseCode.STORAGE_POSITION_OF_EMPTY_IN_STORAGE_NOT_EXISTS);
				responseStoragePosition.setMessage("储藏间不可用");
			}
			return responseStoragePosition;
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
		
		
	}
	@Override
	public ResponseObject<StoragePosition> findNextEmptyStoragePositionByWarehoseIdTypeRelate(
			String warehouseId, String type, String typeRelateId)
			throws ServiceException {
		ResponseObject<StoragePosition> responsePosition = new ResponseObject<StoragePosition>();
		try{
			StoragePosition storagePosition = this.storagePositionDao.findNextEmptyStoragePositionByWarehoseIdTypeRelate(warehouseId, type, typeRelateId);
			if (null !=storagePosition){
				responsePosition.setCode(ResponseCode.SUCCESS_CODE);
				responsePosition.setData(storagePosition);
			}else{
				responsePosition.setCode(ResponseCode.STORAGE_POSITION_OF_EMPTY_IN_STORAGE_NOT_EXISTS);
				responsePosition.setMessage("本储藏间已经没有空的储藏间");
			}
			return responsePosition;
		}catch(Exception e){
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	@Override
	public ResponseObject<StoragePosition> findStoragePositionByWarehoseIdTypeRelateUser(
			String warehouseId, String type, String typeRelateId, String userId)
			throws ServiceException {
		ResponseObject<StoragePosition> responsePosition = new ResponseObject<StoragePosition>();
		try{
			StoragePosition storagePosition = this.storagePositionDao.findStoragePositionByWarehoseIdTypeRelateUser(warehouseId, type, typeRelateId, userId);
			if(null != storagePosition){
				responsePosition.setCode(ResponseCode.SUCCESS_CODE);
				responsePosition.setData(storagePosition);
			}else{
				responsePosition.setCode(ResponseCode.STORAGE_POSITION_OF_EMPTY_IN_STORAGE_NOT_EXISTS);
				responsePosition.setMessage("没有此用户的储藏间");
			}
		}catch(Exception e){
			throw ExceptionUtil.handle2ServiceException(e);
		}
		return responsePosition;
	}
	@Override
	public ResponseObject<StoragePosition> findStoragePositionByWarehoseIdTypeRelateUserOrNoUser(
			String warehouseId, String type, String typeRelateId, String userId, String relateId)
			throws ServiceException {
		ResponseObject<StoragePosition> responsePosition = new ResponseObject<StoragePosition>();
		try{
			StoragePosition storagePosition = this.storagePositionDao.findStoragePositionByWarehoseIdTypeRelateUser(warehouseId, type, typeRelateId, userId);
			if(null == storagePosition){
				storagePosition = this.storagePositionDao.findNextEmptyStoragePositionByWarehoseIdTypeRelate(warehouseId, type, typeRelateId);
			}
			if(null != storagePosition && null != relateId){
				List<StoragePositionRecord> storagePositionRecords = new ArrayList<StoragePositionRecord>();
				//ralateId为转运id或运单id（OrderId）
				storagePositionRecords.add(new StoragePositionRecord(storagePosition.getId(), relateId));
				storagePosition.setRecordList(storagePositionRecords);
			}
			if(null != storagePosition){
				responsePosition.setCode(ResponseCode.SUCCESS_CODE);
				responsePosition.setData(storagePosition);
			}else{
				responsePosition.setCode(ResponseCode.STORAGE_POSITION_OF_EMPTY_IN_STORAGE_NOT_EXISTS);
				responsePosition.setMessage("没有此用户的储藏间或没可分配的");
			}
		}catch(Exception e){
			throw ExceptionUtil.handle2ServiceException(e);
		}
		return responsePosition;
	}
	
}

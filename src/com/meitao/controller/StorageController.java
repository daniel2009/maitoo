package com.meitao.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import jxl.common.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.meitao.service.StoragePositionRecordService;
import com.meitao.service.StoragePositionService;
import com.meitao.service.StorageService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ParameterConstants;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.StringUtil;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.Storage;
import com.meitao.model.StoragePosition;
import com.meitao.model.StoragePositionRecord;
import com.meitao.model.User;

@Controller
public class StorageController extends BasicController{
	private static final long serialVersionUID = 778494429237529302L;
	
	private static final Logger log = Logger.getLogger(StorageController.class);
	@Resource(name = "storageService")
	private StorageService storageService;
	@Resource(name = "storagePositionService")
	private StoragePositionService storagePositionService;
	@Resource(name = "storagePositionRecordService")
	private StoragePositionRecordService storagePositionRecordService;
	@Value(value = "${page_size}")
	private int defaultPageSize;
	
	@RequestMapping(value = "/admin/storage/add", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<Object> add(HttpServletRequest request, Storage storage,
			@RequestParam(value = ParameterConstants.STORAGE_COL_NUMBER, required = false, defaultValue = "1") String colNumber,
			@RequestParam(value = ParameterConstants.STORAGE_ROW_NUMBER, required = false, defaultValue = "1") String rowNumber){
		try{
			return this.storageService.add(storage, colNumber, rowNumber);
		}catch(Exception e){
			log.error("add storage fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "添加储藏间发生异常");
		}
	}
	@RequestMapping(value = "/admin/storagePosition/useStorage", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<Object> useStorage(HttpServletRequest request, 
			@RequestParam(value = "userId") String userId,
			@RequestParam(value = ParameterConstants.STORAGE_POSITION_ID, required = false) String storagePositionId){
		try{
			User user = new User();
			user.setId(userId);
			return this.storagePositionService.useStorage(this.storagePositionService.findById(storagePositionId).getData(), user, 0);
		}catch(Exception e){
			log.error("link user to storage position fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "注入用户失败");
		}
	}
	@RequestMapping(value = "/admin/storagePosition/clearStorage", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<Object> clearStorage(HttpServletRequest request, User user,
			@RequestParam(value = ParameterConstants.STORAGE_POSITION_ID, required = false) String storagePositionId){
		try{
			return this.storagePositionService.useStorage(this.storagePositionService.findById(storagePositionId).getData(), new User(), 1);
		}catch(Exception e){
			log.error("清空仓位失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "清空仓位失败");
		}
	}
	@RequestMapping(value = "/admin/storage/findByWarehouse", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject<PageSplit<Storage>> add(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.STORAGE_WAREHOUSE_ID, required = false) String warehouseId,
			@RequestParam(value = "type") String type,
			@RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "0") int pageIndex){
		try{
			return this.storageService.findByWarehouse(warehouseId, type, pageIndex, defaultPageSize);
		}catch(Exception e){
			log.error("get storage list fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取指定储藏间列表失败");
		}
	}
	/**
	 * author: li zhang
	 * @param request
	 * @param userId
	 * @param typeRelateId
	 * @param warehouseId
	 * @return
	 */
	@RequestMapping(value = "/admin/storage/findByWarehouseIdAndTypeAndRelateIdList", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<List<Storage>> findByWarehouseIdAndTypeAndRelateIdList(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.STORAGE_POSITION_USER_ID) String userId,
			@RequestParam(value = "typeRelateId", required=false) String typeRelateId,
			@RequestParam(value = "warehouseId", required=false) String warehouseId){
		try{
			ResponseObject<List<Storage>> responseStorage = this.storageService.findByWarehouseIdAndTypeAndRelateIdList(warehouseId,Constant.STORAGE_TYPE_BEFORE_OPEN,typeRelateId);
			if(ResponseCode.SUCCESS_CODE.equals(responseStorage.getCode())){
				return responseStorage;
			}else{
				return new ResponseObject<List<Storage>>(responseStorage.getCode(), responseStorage.getMessage());
			}
		}catch(Exception e){
			log.error("get storage position for user fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取指定储藏间列表失败");
		}
	}
	
	/**
	 * author: li zhang
	 * @param request
	 * @param userId
	 * @param typeRelateId
	 * @param warehouseId
	 * @return
	 */
	@RequestMapping(value = "/admin/storage/findByWarehouseIdAndTypeAndRelateIdListAfter", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<List<Storage>> findByWarehouseIdAndTypeAndRelateIdListAfter(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.STORAGE_POSITION_USER_ID) String userId,
			@RequestParam(value = "typeRelateId", required=false) String typeRelateId,
			@RequestParam(value = "warehouseId", required=false) String warehouseId){
		try{
			ResponseObject<List<Storage>> responseStorage = this.storageService.findByWarehouseIdAndTypeAndRelateIdList(warehouseId,Constant.STORAGE_TYPE_AFTER_OPEN,"-1");
			if(ResponseCode.SUCCESS_CODE.equals(responseStorage.getCode())){
				return responseStorage;
			}else{
				return new ResponseObject<List<Storage>>(responseStorage.getCode(), responseStorage.getMessage());
			}
		}catch(Exception e){
			log.error("get storage position for user fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取指定储藏间列表失败");
		}
	}
	
	/**
	 * author: li zhang
	 * @param request
	 * @param userId
	 * @param typeRelateId
	 * @param warehouseId
	 * @return
	 */
	@RequestMapping(value = "/admin/storage/findByWarehouseIdAndTypeAndRelateIdListOrder", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<List<Storage>> findByWarehouseIdAndTypeAndRelateIdListOrder(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.STORAGE_POSITION_USER_ID) String userId,
			@RequestParam(value = "typeRelateId", required=false) String typeRelateId,
			@RequestParam(value = "warehouseId", required=false) String warehouseId){
		try{
			ResponseObject<List<Storage>> responseStorage = this.storageService.findByWarehouseIdAndTypeAndRelateIdList(warehouseId,Constant.STORAGE_TYPE_ORDER,typeRelateId);
			if(ResponseCode.SUCCESS_CODE.equals(responseStorage.getCode())){
				return responseStorage;
			}else{
				return new ResponseObject<List<Storage>>(responseStorage.getCode(), responseStorage.getMessage());
			}
		}catch(Exception e){
			log.error("get storage position for user fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取指定储藏间列表失败");
		}
	}
	/**
	 * author: Li Zhang
	 * @param request
	 * @param userId
	 * @param typeRelateId
	 * @param warehouseId
	 * @return
	 */
	@RequestMapping(value = "/admin/storagePosition/findStoragePositionByWarehouseIdAndTypeAndRelateIdRowCol", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<StoragePosition> findStoragePositionByWarehouseIdAndTypeAndRelateId(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.STORAGE_POSITION_USER_ID) String userId,
			@RequestParam(value = "typeRelateId") String typeRelateId,
			@RequestParam(value = "warehouseId") String warehouseId,
			@RequestParam(value = "storageId") String storageId,
			@RequestParam(value = "colNumber") String colNumber,
			@RequestParam(value = "rowNumber") String rowNumber){
		try{
			System.out.println("warehouseId:"+warehouseId);
			
			ResponseObject<StoragePosition> resStorageByUser = this.storagePositionService.findStoragePositionByWarehoseIdTypeRelateUser(warehouseId, Constant.STORAGE_TYPE_BEFORE_OPEN, typeRelateId, userId);
			
			if(null != resStorageByUser.getData()){
				return resStorageByUser;
			}else{
				ResponseObject<StoragePosition> responseStorage = this.storagePositionService.findStoragePositionByWarehouseIdAndTypeAndRelateIdRowCol(warehouseId,Constant.STORAGE_TYPE_BEFORE_OPEN,typeRelateId,storageId,colNumber,rowNumber);
				if(ResponseCode.SUCCESS_CODE.equals(responseStorage.getCode())){
					return responseStorage;
				}else{
					return new ResponseObject<StoragePosition>(responseStorage.getCode(), responseStorage.getMessage());
				}
			}
			
		}catch(Exception e){
			log.error("get storage position for user fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取指定储藏间列表失败");
		}
	}
	
	/**
	 * author: Li Zhang
	 * @param request
	 * @param userId
	 * @param typeRelateId
	 * @param warehouseId
	 * @return
	 */
	@RequestMapping(value = "/admin/storagePosition/findStoragePositionByWarehouseIdAndTypeAndRelateIdRowColAfter", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<StoragePosition> findStoragePositionByWarehouseIdAndTypeAndRelateIdRowColAfter(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.STORAGE_POSITION_USER_ID) String userId,
			@RequestParam(value = "typeRelateId") String typeRelateId,
			@RequestParam(value = "warehouseId") String warehouseId,
			@RequestParam(value = "storageId") String storageId,
			@RequestParam(value = "colNumber") String colNumber,
			@RequestParam(value = "rowNumber") String rowNumber){
		try{
			//System.out.println("warehouseId:"+warehouseId);
			//-1是没有关联关系的
			ResponseObject<StoragePosition> resStorageByUser = this.storagePositionService.findStoragePositionByWarehoseIdTypeRelateUser(warehouseId, Constant.STORAGE_TYPE_AFTER_OPEN, "-1", userId);
			
			if(null != resStorageByUser.getData()){
				return resStorageByUser;
			}else{
				ResponseObject<StoragePosition> responseStorage = this.storagePositionService.findStoragePositionByWarehouseIdAndTypeAndRelateIdRowCol(warehouseId,Constant.STORAGE_TYPE_AFTER_OPEN,"-1",storageId,colNumber,rowNumber);
				if(ResponseCode.SUCCESS_CODE.equals(responseStorage.getCode())){
					return responseStorage;
				}else{
					return new ResponseObject<StoragePosition>(responseStorage.getCode(), responseStorage.getMessage());
				}
			}
			
		}catch(Exception e){
			log.error("get storage position for user fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取指定储藏间列表失败");
		}
	}
	
	/**
	 * author: Li Zhang
	 * @param request
	 * @param userId
	 * @param typeRelateId
	 * @param warehouseId
	 * @return
	 */
	@RequestMapping(value = "/admin/storagePosition/findStoragePositionByWarehouseIdAndTypeAndRelateIdRowColOrder", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<StoragePosition> findStoragePositionByWarehouseIdAndTypeAndRelateIdRowColOrder(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.STORAGE_POSITION_USER_ID) String userId,
			@RequestParam(value = "typeRelateId") String typeRelateId,
			@RequestParam(value = "warehouseId") String warehouseId,
			@RequestParam(value = "storageId") String storageId,
			@RequestParam(value = "colNumber") String colNumber,
			@RequestParam(value = "rowNumber") String rowNumber){
		try{
			ResponseObject<StoragePosition> resStorageByUser = this.storagePositionService.findStoragePositionByWarehoseIdTypeRelateUser(warehouseId, Constant.STORAGE_TYPE_ORDER, typeRelateId, userId);
			
			if(null != resStorageByUser.getData()){
				return resStorageByUser;
			}else{
				ResponseObject<StoragePosition> responseStorage = this.storagePositionService.findStoragePositionByWarehouseIdAndTypeAndRelateIdRowCol(warehouseId,Constant.STORAGE_TYPE_ORDER,typeRelateId,storageId,colNumber,rowNumber);
				if(ResponseCode.SUCCESS_CODE.equals(responseStorage.getCode())){
					return responseStorage;
				}else{
					return new ResponseObject<StoragePosition>(responseStorage.getCode(), responseStorage.getMessage());
				}
			}
			
		}catch(Exception e){
			log.error("get storage position for user fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取指定储藏间列表失败");
		}
	}
	
	/**
	 * author: Li Zhang
	 * @param request
	 * @param userId
	 * @param typeRelateId
	 * @param warehouseId
	 * @return
	 */
	@RequestMapping(value = "/admin/storagePosition/findStoragePositionByWarehoseIdTypeRelateUser", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<StoragePosition> findStoragePositionByWarehoseIdTypeRelateUser(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.STORAGE_POSITION_USER_ID) String userId,
			@RequestParam(value = "typeRelateId") String typeRelateId,
			@RequestParam(value = "warehouseId") String warehouseId){
		try{
			System.out.println("warehouseId:"+warehouseId);
			
			ResponseObject<StoragePosition> resStorageByUser = this.storagePositionService.findStoragePositionByWarehoseIdTypeRelateUser(warehouseId, Constant.STORAGE_TYPE_BEFORE_OPEN, typeRelateId, userId);
			
			if(null != resStorageByUser.getData()){
				return resStorageByUser;
			}else{
					ResponseObject<StoragePosition> resStorageByUser1 = this.storagePositionService.findNextEmptyStoragePositionByWarehoseIdTypeRelate(warehouseId, Constant.STORAGE_TYPE_BEFORE_OPEN, typeRelateId);
					if(null != resStorageByUser1.getData()){
						return resStorageByUser1;
					}else{
						return new ResponseObject<StoragePosition>(resStorageByUser.getCode(), resStorageByUser.getMessage());
					}
					
			}
			
		}catch(Exception e){
			log.error("get storage position for user fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取指定储藏间列表失败");
		}
	}
	
	/**
	 * author: Li Zhang
	 * @param request
	 * @param userId
	 * @param typeRelateId
	 * @param warehouseId
	 * @return
	 */
	@RequestMapping(value = "/admin/storagePosition/findStoragePositionByWarehoseIdTypeRelateUserAfter", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<StoragePosition> findStoragePositionByWarehoseIdTypeRelateUserAfter(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.STORAGE_POSITION_USER_ID) String userId,
			@RequestParam(value = "typeRelateId") String typeRelateId,
			@RequestParam(value = "warehouseId") String warehouseId){
		try{
			System.out.println("warehouseId:"+warehouseId);
			
			ResponseObject<StoragePosition> resStorageByUser = this.storagePositionService.findStoragePositionByWarehoseIdTypeRelateUser(warehouseId, Constant.STORAGE_TYPE_AFTER_OPEN, "-1", userId);
			
			if(null != resStorageByUser.getData()){
				return resStorageByUser;
			}else{
					ResponseObject<StoragePosition> resStorageByUser1 = this.storagePositionService.findNextEmptyStoragePositionByWarehoseIdTypeRelate(warehouseId, Constant.STORAGE_TYPE_AFTER_OPEN, "-1");
					if(null != resStorageByUser1.getData()){
						return resStorageByUser1;
					}else{
						return new ResponseObject<StoragePosition>(resStorageByUser.getCode(), resStorageByUser.getMessage());
					}
					
			}
			
		}catch(Exception e){
			log.error("get storage position for user fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取指定储藏间列表失败");
		}
	}
	
	/**
	 * author: Li Zhang
	 * @param request
	 * @param userId
	 * @param typeRelateId
	 * @param warehouseId
	 * @return
	 */
	@RequestMapping(value = "/admin/storagePosition/findStoragePositionByWarehoseIdTypeRelateUserOrder", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<StoragePosition> findStoragePositionByWarehoseIdTypeRelateUserOrder(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.STORAGE_POSITION_USER_ID) String userId,
			@RequestParam(value = "typeRelateId") String typeRelateId,
			@RequestParam(value = "warehouseId") String warehouseId){
		try{
			System.out.println("warehouseId:"+warehouseId);
			
			ResponseObject<StoragePosition> resStorageByUser = this.storagePositionService.findStoragePositionByWarehoseIdTypeRelateUser(warehouseId, Constant.STORAGE_TYPE_ORDER, typeRelateId, userId);
			
			if(null != resStorageByUser.getData()){
				return resStorageByUser;
			}else{
					ResponseObject<StoragePosition> resStorageByUser1 = this.storagePositionService.findNextEmptyStoragePositionByWarehoseIdTypeRelate(warehouseId, Constant.STORAGE_TYPE_ORDER, typeRelateId);
					if(null != resStorageByUser1.getData()){
						return resStorageByUser1;
					}else{
						return new ResponseObject<StoragePosition>(resStorageByUser.getCode(), resStorageByUser.getMessage());
					}
					
			}
			
		}catch(Exception e){
			log.error("get storage position for user fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取指定储藏间列表失败");
		}
	}
	
	@RequestMapping(value = "/admin/storagePosition/findById", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject<StoragePosition> findById(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.STORAGE_POSITION_ID) String id){
		try{
			return this.storagePositionService.findById(id);
		}catch(Exception e){
			log.error("get storage position info fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取指定仓库为止详情失败");
		}
	}

	@RequestMapping(value = "/admin/storage/deleteStorage", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<Object> useStorage(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.STORAGE_POSITION_ID, required = false) String id){
		try{
			return this.storageService.delete(id);
		}catch(Exception e){
			log.error("link user to storage position fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "注入用户失败");
		}
	}
	@RequestMapping(value = "/admin/storagePosition/findNoPositionUserByStorageId", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject<List<StoragePosition>> findNoPositionUserByStorageId(HttpServletRequest request, Storage storage){
		try{
			return this.storagePositionService.findNoPositionUserByStorageId(storage.getId());
		}catch(Exception e){
			log.error("get storage list fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取指定储藏间列表失败");
		}
	}
	@RequestMapping(value = "/admin/storagePosition/findOutPositionRecordByStoragePositionId", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject<StoragePosition> findOutPositionRecordByStoragePositionId(HttpServletRequest request, StoragePosition storagePosition){
		try{
			return this.storagePositionService.findNoPositionRecordBySameStoragePosition(storagePosition);
		}catch(Exception e){
			log.error("查询应该放入仓位的数据失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "查询应该放入仓位的数据失败");
		}
	}
	@RequestMapping(value = "/admin/storagePosition/findEmptyStoragePositionByBeforeType", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject<StoragePosition> findEmptyStoragePositionByBeforeType(HttpServletRequest request,
			@RequestParam(value = "warehouseId") String warehouseId,
			@RequestParam(value = "typeRelateId") String typeRelateId){
		try{
			Storage storage = this.storageService.findByWarehouseAndTypeAndRelateId(warehouseId, Constant.STORAGE_TYPE_BEFORE_OPEN, typeRelateId, 1, defaultPageSize).getData().getDatas().get(0);
			return this.storagePositionService.findEmptyStoragePosition(storage);
		}catch(Exception e){
			log.error("get empty storage position fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取空储藏间失败");
		}
	}
	@RequestMapping(value = "/admin/storagePosition/findEmptyStoragePositionByAfterType", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject<StoragePosition> findEmptyStoragePositionByAfterType(HttpServletRequest request,
			@RequestParam(value = "warehouseId") String warehouseId,
			@RequestParam(value = "typeRelateId") String typeRelateId){
		try{
			Storage storage = this.storageService.findByWarehouseAndTypeAndRelateId(warehouseId, Constant.STORAGE_TYPE_AFTER_OPEN, typeRelateId, 1, defaultPageSize).getData().getDatas().get(0);
			return this.storagePositionService.findEmptyStoragePosition(storage);
		}catch(Exception e){
			log.error("get empty storage position fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取空储藏间失败");
		}
	}
	@RequestMapping(value = "/admin/storagePosition/findEmptyStoragePositionByOrderType", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject<StoragePosition> findEmptyStoragePositionByOrderType(HttpServletRequest request,
			@RequestParam(value = "typeRelateId") String typeRelateId){
		String warehouseId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
		try{
			Storage storage = this.storageService.findByWarehouseAndTypeAndRelateId(warehouseId, Constant.STORAGE_TYPE_ORDER, typeRelateId, 1, defaultPageSize).getData().getDatas().get(0);
			return this.storagePositionService.findEmptyStoragePosition(storage);
		}catch(Exception e){
			log.error("get empty storage position fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取空储藏间失败");
		}
	}
	@RequestMapping(value = "/admin/storagePosition/findStoragePosition4UserByBeforeType", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<StoragePosition> findStoragePosition4UserByBeforeType(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.STORAGE_POSITION_USER_ID) String userId,
			@RequestParam(value = "typeRelateId") String typeRelateId,
			@RequestParam(value = "warehouseId", required=false) String warehouseId){
		try{
			ResponseObject<PageSplit<Storage>> responseStorage = this.storageService.findByWarehouseAndTypeAndRelateId(warehouseId, Constant.STORAGE_TYPE_BEFORE_OPEN, typeRelateId, 1, defaultPageSize);
			if(ResponseCode.SUCCESS_CODE.equals(responseStorage.getCode())){
				return this.storagePositionService.findStoragePositionByStorage4User(responseStorage.getData().getDatas().get(0), userId);
			}
			return new ResponseObject<StoragePosition>(responseStorage.getCode(), responseStorage.getMessage());
		}catch(Exception e){
			log.error("get storage position for user fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取指定储藏间列表失败");
		}
	}
	/**
	 * author:li zhang 返回List<StoragePosition>
	 * @param request
	 * @param userId
	 * @param typeRelateId
	 * @param warehouseId
	 * @return
	 */
	@RequestMapping(value = "/admin/storagePosition/findStoragePosition4UserByBeforeTypeList", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<List<StoragePosition>> findStoragePosition4UserByBeforeTypeList(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.STORAGE_POSITION_USER_ID) String userId,
			@RequestParam(value = "typeRelateId") String typeRelateId,
			@RequestParam(value = "warehouseId", required=false) String warehouseId){
		try{
			ResponseObject<PageSplit<Storage>> responseStorage = this.storageService.findByWarehouseAndTypeAndRelateId(warehouseId, Constant.STORAGE_TYPE_BEFORE_OPEN, typeRelateId, 1, defaultPageSize);
			if(ResponseCode.SUCCESS_CODE.equals(responseStorage.getCode())){
				
				return this.storagePositionService.findStoragePositionByStorage4UserList(responseStorage.getData().getDatas().get(0), userId);
			}
			return new ResponseObject<List<StoragePosition>>(responseStorage.getCode(), responseStorage.getMessage());
		}catch(Exception e){
			log.error("get storage position for user fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取指定储藏间列表失败");
		}
	}
	@RequestMapping(value = "/admin/storagePosition/findStoragePosition4UserByAfterType", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<StoragePosition> findStoragePosition4UserByAfterType(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.STORAGE_POSITION_USER_ID) String userId,
			@RequestParam(value = "typeRelateId", required=false) String typeRelateId,
			@RequestParam(value = "warehouseId", required=false) String warehouseId){
		try{
			ResponseObject<PageSplit<Storage>> responseStorage = this.storageService.findByWarehouseAndTypeAndRelateId(warehouseId, Constant.STORAGE_TYPE_AFTER_OPEN, typeRelateId, 1, defaultPageSize);
			if(ResponseCode.SUCCESS_CODE.equals(responseStorage.getCode())){
				return this.storagePositionService.findStoragePositionByStorage4User(responseStorage.getData().getDatas().get(0), userId);
			}
			return new ResponseObject<StoragePosition>(responseStorage.getCode(), responseStorage.getMessage());
		}catch(Exception e){
			log.error("get storage position for user fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取指定储藏间列表失败");
		}
	}
	/**
	 * author:li zhang
	 * @param request
	 * @param userId
	 * @param typeRelateId
	 * @param warehouseId
	 * @return
	 */
	@RequestMapping(value = "/admin/storagePosition/findStoragePosition4UserByAfterTypeList", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<List<StoragePosition>> findStoragePosition4UserByAfterTypeList(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.STORAGE_POSITION_USER_ID) String userId,
			@RequestParam(value = "typeRelateId", required=false) String typeRelateId,
			@RequestParam(value = "warehouseId", required=false) String warehouseId){
		try{
			ResponseObject<PageSplit<Storage>> responseStorage = this.storageService.findByWarehouseAndTypeAndRelateId(warehouseId, Constant.STORAGE_TYPE_AFTER_OPEN, typeRelateId, 1, defaultPageSize);
			if(ResponseCode.SUCCESS_CODE.equals(responseStorage.getCode())){
				return this.storagePositionService.findStoragePositionByStorage4UserList(responseStorage.getData().getDatas().get(0), userId);
			}
			return new ResponseObject<List<StoragePosition>>(responseStorage.getCode(), responseStorage.getMessage());
		}catch(Exception e){
			log.error("get storage position for user fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取指定储藏间列表失败");
		}
	}
	
	@RequestMapping(value = "/admin/storagePosition/findStoragePosition4UserByOrderType", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<StoragePosition> findStoragePositionByWarehouseId4User(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.STORAGE_POSITION_USER_ID) String userId,
			@RequestParam(value = "typeRelateId") String typeRelateId){
		String warehouseId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
		try{
			//li zhang 02222016
			ResponseObject<StoragePosition> userStorage = this.storagePositionService.findStoragePositionByWarehoseIdTypeRelateUser(warehouseId, Constant.STORAGE_TYPE_ORDER, typeRelateId, userId);
			if(null != userStorage.getData()){
				return userStorage;
			}
			//li zhang 02222016			
			ResponseObject<PageSplit<Storage>> responseStorage = this.storageService.findByWarehouseAndTypeAndRelateId(warehouseId, Constant.STORAGE_TYPE_ORDER, typeRelateId, 1, defaultPageSize);
			if(ResponseCode.SUCCESS_CODE.equals(responseStorage.getCode())){
				return this.storagePositionService.findStoragePositionByStorage4User(responseStorage.getData().getDatas().get(0), userId);
			}
			return new ResponseObject<StoragePosition>(responseStorage.getCode(), responseStorage.getMessage());
		}catch(Exception e){
			log.error("get storage position for user fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取指定储藏间列表失败");
		}
	}
	@RequestMapping(value = "/admin/storagePosition/checkStoragePosition4User", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject<Object> checkStoragePosition4User(HttpServletRequest request, StoragePosition storagePosition){
		try{
			return this.storagePositionService.checkStoragePositionByUser(storagePosition, storagePosition.getUserId());
		}catch(Exception e){
			log.error("check if storage position can user for user fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "检查仓位可用性失败");
		}
	}
	@RequestMapping(value = "/admin/storagePosition/findStoragePositionByUser", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<PageSplit<StoragePosition>> findStoragePositionByUser(HttpServletRequest request, User user,
			@RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue="1") int pageIndex){
		try{
			return this.storagePositionService.findStoragePosition(user, null, pageIndex, defaultPageSize);
		}catch(Exception e){
			log.error("get storage list fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取指定储藏间列表失败");
		}
	}
	@RequestMapping(value = "/admin/storagePosition/takeoutPackageInStoragePosition", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<Object> takeoutPackageInStoragePosition(HttpServletRequest request, StoragePositionRecord storagePositionRecord){
		try{
			//li zhang 02222016
			StoragePosition storagePosition = this.storagePositionService.findById(storagePositionRecord.getStoragePositionId()).getData();
			List<StoragePositionRecord> storagePositionRecords = new ArrayList<StoragePositionRecord>();
			storagePositionRecords.add(storagePositionRecord);
			storagePosition.setRecordList(storagePositionRecords);
			ResponseObject<Object> resObject = this.storagePositionService.useStorage(storagePosition, new User(), 3);
			return resObject;
		}catch(Exception e){
			log.error("get storage list fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取指定储藏间列表失败");
		}
	}
	@RequestMapping(value = "/admin/storagePosition/bringInPackageInStoragePosition", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<Object> bringInPackageInStoragePosition(HttpServletRequest request, StoragePositionRecord storagePositionRecord){
		try{
			return this.storagePositionRecordService.insertByRelate(storagePositionRecord);
		}catch(Exception e){
			log.error("get storage list fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取指定储藏间列表失败");
		}
	}
	@RequestMapping(value = "/admin/storagePosition/findNextEmptyStoragePositionByWarehoseIdTypeRelate", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<StoragePosition> findNextEmptyStoragePositionByWarehoseIdTypeRelate(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.STORAGE_POSITION_USER_ID) String userId,
			@RequestParam(value = "typeRelateId", required=false) String typeRelateId,
			@RequestParam(value = "warehouseId", required=false) String warehouseId){
		try{
			ResponseObject<StoragePosition> responseStoragePosition = this.storagePositionService.findNextEmptyStoragePositionByWarehoseIdTypeRelate(warehouseId,Constant.STORAGE_TYPE_BEFORE_OPEN,typeRelateId);
			if(ResponseCode.SUCCESS_CODE.equals(responseStoragePosition.getCode())){
				return responseStoragePosition;
			}else{
				return new ResponseObject<StoragePosition>(responseStoragePosition.getCode(), responseStoragePosition.getMessage());
			}
		}catch(Exception e){
			log.error("get storage position for user fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取空储藏间列表失败");
		}
	}
	
	
}

package com.meitao.service;

import java.util.List;



import com.meitao.exception.ServiceException;
import com.meitao.model.Order;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.Storage;
import com.meitao.model.StoragePosition;
import com.meitao.model.StoragePositionRecord;
import com.meitao.model.TranshipmentBill;
import com.meitao.model.User;

public interface StoragePositionService {

	/**
	 * 检查该仓位是否每人使用，或者被该用户使用。若用户在别处有仓位，此方法不适用
	 * @param storagePosition
	 * @param userId
	 * @return 如果仓位没人使用或使用者即为用户本身，data为仓位id，否则为false
	 * @throws ServiceException
	 */
	ResponseObject<Object> checkStoragePositionByUser(StoragePosition storagePosition, String userId) throws ServiceException;

	/**
	 * 在指定储藏间内获取一个用户可用的仓位
	 * @param storage
	 * @param userId
	 * @return 
	 * 		用户已有仓位->用户的仓位;
	 * 		若用户没有->空仓位;
	 * 		若无仓位：code=STORAGE_POSITION_OF_EMPTY_IN_STORAGE_NOT_EXISTS
	 * @throws ServiceException
	 */
	ResponseObject<StoragePosition> findStoragePositionByStorage4User(Storage storage, String userId) throws ServiceException;

	/**
	 * 在指定储藏间内获取一个空的仓位
	 * @param storage
	 * @return
	 * @throws ServiceException
	 */
	ResponseObject<StoragePosition> findEmptyStoragePosition(Storage storage) throws ServiceException;

	ResponseObject<StoragePosition> findById(String id) throws ServiceException;
	
	ResponseObject<StoragePosition> findByIdRelateId(String id, String relateId) throws ServiceException;
	

	/**
	 * 操作指定仓位，需要先自行判断该仓位是否无人使用，别人使用，或者为该用户使用
	 * @param storagePosition 计划要使用的仓位
	 * @param order 应包含有id，userId，state
	 * @param operation 入库： 0； 出库： 1；
	 * @return
	 * @throws ServiceException
	 */
	ResponseObject<Object> useStorage(StoragePosition storagePosition, Order order, int operation) throws ServiceException;
	/**
	 * 操作指定仓位，需要先自行判断该仓位是否无人使用，别人使用，或者为该用户使用
	 * @param storagePosition 计划要使用的仓位
	 * @param transhipmentBill 应包含有id，userId，state
	 * @param operation 入库： 0； 出库： 1；
	 * @return
	 * @throws ServiceException
	 */
	ResponseObject<Object> useStorage(StoragePosition storagePosition, TranshipmentBill transhipmentBill, int operation) throws ServiceException;

	ResponseObject<List<StoragePosition>> findNoPositionUserByStorageId(String storageId) throws ServiceException;
	/**
	 * 查询应该放在这个仓位，却没有放进来的包裹
	 * @param storagePosition 要有id
	 * @return
	 * @throws ServiceException
	 */
	ResponseObject<StoragePosition> findNoPositionRecordBySameStoragePosition(StoragePosition storagePosition) throws ServiceException;
	
	/**
	 * 放入或拿出用户在此储藏间(该仓位)的所有包裹
	 * @param storagePosition 仓位，应该有完整信息
	 * @param user 若拿出，= new User()
	 * @param operation  入库： 0； 出库： 1；
	 * @return
	 */
	ResponseObject<Object> useStorage(StoragePosition storagePosition, User user, int operation) throws ServiceException;

	/**
	 * 根据transhipmentBill查询对应的仓位详情(仓位位置可以再position属性中拿到)
	 * @param transhipmentBill 需要包含有：warehouseId， state， tranwarehouseId， id的值
	 * @return
	 * @throws ServiceException
	 */
	ResponseObject<StoragePosition> findStoragePosition(TranshipmentBill transhipmentBill) throws ServiceException;

	/**
	 * 根据order查询对应的仓位详情(仓位位置可以再position属性中拿到)
	 * @param order 需要包含有：warehouseId， state， channelId， id的值
	 * @return
	 * @throws ServiceException
	 */
	ResponseObject<StoragePosition> findStoragePosition(Order order) throws ServiceException;

	/**
	 * 根据条件查询对应仓位详情
	 * @param warehouseId
	 * @param type 可以再在constant中获取； transhipment拆包前：storage_type_before_open ， transhipment拆包后：storage_type_after_open， order：storage_type_order
	 * @param typeRelateId 关联的id： 拆包前：transhipment。tranwarehouseId， 拆包后：任意，order：order。channelId
	 * @param relateId transhipment或order的id
	 * @return
	 * @throws ServiceException
	 */
	ResponseObject<StoragePosition> findStoragePosition(String warehouseId, String type, String typeRelateId, String relateId) throws ServiceException;

	/**
	 * 根据record的relateId和仓库查询
	 * @param storage
	 * @param storagePositionRecord
	 * @return
	 * @throws ServiceException
	 */
	ResponseObject<StoragePosition> findStoragePosition(Storage storage, StoragePositionRecord storagePositionRecord) throws ServiceException;

	/**
	 * 
	 * @param user 若根据id查询，请设置id， 若模糊查询，请把条件放在real_name处，无需对条件进行处理，该方法对其处理
	 * @param storage 若不指定，则null
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @throws ServiceException
	 */
	ResponseObject<PageSplit<StoragePosition>> findStoragePosition(User user, Storage storage, int pageIndex, int pageSize) throws ServiceException;

	/**
	 * 根据order，如果状态是在库，则查询该order应该在的仓位，如果不在库，则查询其是否在仓内
	 * @param order
	 * @return 如果本身有仓位， 返回该仓位，如果没有，返回下一个空仓位
	 * @throws ServiceException
	 */
	ResponseObject<StoragePosition> findShouldIn(Order order) throws ServiceException;
	ResponseObject<StoragePosition> findShouldIn(TranshipmentBill transhipmentBill) throws ServiceException;

	ResponseObject<StoragePosition> findStoragePositionByStorage4User(String warehouseId, String type, String typeRelateId, String userId) throws ServiceException;
	/**
	 * 根据storage,userId没有用上 author li zhang
	 * @param storage
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	ResponseObject<List<StoragePosition>> findStoragePositionByStorage4UserList(
			Storage storage, String userId) throws ServiceException;
	/**
	 * 用于验证存储间
	 * @param warehouseId
	 * @param type
	 * @param typeRelateId
	 * @param storageId
	 * @param colNumber
	 * @param rowNumber
	 * @return
	 * @throws ServiceException
	 */
	ResponseObject<StoragePosition> findStoragePositionByWarehouseIdAndTypeAndRelateIdRowCol(
			 String warehouseId,
			 String type,
			 String typeRelateId,
			 String storageId,
			 String colNumber,
			 String rowNumber) throws ServiceException;
	/**
	 * 用于建议储藏间
	 * @param warehouseId
	 * @param type
	 * @param typeRelateId
	 * @return
	 * @throws ServiceException
	 */
	ResponseObject<StoragePosition> findNextEmptyStoragePositionByWarehoseIdTypeRelate(
			 String warehouseId,
			 String type,
			 String typeRelateId) throws ServiceException;
	
	ResponseObject<StoragePosition> findStoragePositionByWarehoseIdTypeRelateUser(
			 String warehouseId,
			 String type,
			 String typeRelateId,
			 String userId) throws ServiceException;
	/**
	 * 有或没有user都可以查到storagePosition,在把record放入其中
	 * @param warehouseId
	 * @param type
	 * @param typeRelateId
	 * @param userId
	 * @param relateId 为转运id或运单id
	 * @return ResponseObject
	 * @throws ServiceException
	 */
	ResponseObject<StoragePosition> findStoragePositionByWarehoseIdTypeRelateUserOrNoUser(
			 String warehouseId,
			 String type,
			 String typeRelateId,
			 String userId,
			 String relateId) throws ServiceException;
}

package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.meitao.model.TranshipmentBill;


public interface TranshipmentBillDao {
	public int insertTranshipmentBill(TranshipmentBill bill) throws Exception;

	public int deleteTranshipmentBill(@Param("userId") String userId, @Param("list") List<String> list)
	        throws Exception;

	public int deleteMultiTranshipmentBill(@Param("state") String state, @Param("list") List<String> list)
	        throws Exception;

	public int updateTranshipmentBill(TranshipmentBill bill) throws Exception;
	public int updateTranshipmentBillcomposed(TranshipmentBill bill) throws Exception;

	public int updateTranshipmentBillState(@Param("state") String state, @Param("list") List<String> list)
	        throws Exception;

	public int countByKey(@Param("tid") String tid, @Param("key") String key, @Param("column") String column,
	        @Param("sdate") String sdate, @Param("edate") String edate, @Param("userId") String userId,
	        @Param("state") String state,@Param("storeid") String storeid) throws Exception;

	public List<TranshipmentBill> searchByKey(@Param("tid") String tid, @Param("key") String key,
	        @Param("column") String column, @Param("sdate") String sdate, @Param("edate") String edate,
	        @Param("userId") String userId, @Param("index") int index, @Param("size") int size,
	        @Param("state") String state,@Param("storeid") String storeid) throws Exception;

	public TranshipmentBill getById(@Param("id") String id) throws Exception;

	public List<TranshipmentBill> getByOrderId(@Param("id") String id) throws Exception;

	public List<TranshipmentBill> getByIds(@Param("userId") String userId, @Param("list") List<String> ids)
	        throws Exception;
	
	public int countByKeynew(@Param("tid") String tid, @Param("key") String key, @Param("column") String column,
	        @Param("sdate") String sdate, @Param("edate") String edate, @Param("userId") String userId,
	        @Param("state") String state,@Param("transitType") String transitType,@Param("usercode") String usercode,@Param("storeid") String storeid,@Param("tranWarehouseId") String tranWarehouseId,@Param("warehouseId") String warehouseId) throws Exception;
	
	public List<TranshipmentBill> searchByKeynew(@Param("tid") String tid, @Param("key") String key,
	        @Param("column") String column, @Param("sdate") String sdate, @Param("edate") String edate,
	        @Param("userId") String userId, @Param("index") int index, @Param("size") int size,
	        @Param("state") String state,@Param("transitType") String transitType,@Param("usercode") String usercode,@Param("storeid") String storeid,@Param("tranWarehouseId") String tranWarehouseId,@Param("warehouseId") String warehouseId) throws Exception;
	
	
	public int updateTranshipmentBillquicktype(TranshipmentBill bill) throws Exception;
	public int getidByOrderId(@Param("userId") String userId) throws Exception;//kai20151003 获取最新插入的id号
	
	public int updateTranshipmentBillremovecommditys(@Param("modifyDate") String modifyDate,@Param("weight") String weight,@Param("awt") String awt, @Param("tranistNo") String tranistNo,@Param("weightkg") String weightkg,@Param("id") String id)
	        throws Exception;
	public int updateTranshipmentBillremovecommditysnew(@Param("modifyDate") String modifyDate,@Param("weight") String weight,@Param("weightkg") String weightkg,@Param("id") String id)
	        throws Exception;
	public int deleteTranshipmentBillbyid(@Param("id") String id)
	        throws Exception;


	public int addByClaimPackage(TranshipmentBill transhipmentBill) throws Exception;

	public int updateToPreOrder(TranshipmentBill transhipmentBill) throws Exception;

	public int updateStateAndRemark(@Param("id") String id, @Param("state")String state, @Param("remark") String remark, @Param("modifyDate")String modifyDate) throws Exception;

	public List<TranshipmentBill> searchWithRouteByKey(@Param("id")String id, @Param("key")String key, @Param("type")String type,
			@Param("createDateStart")String createDateStart, @Param("createDateEnd")String createDateEnd, @Param("state")String state,
			@Param("warehouseId")String warehouseId, @Param("firstResult")int firstResult, @Param("pageSize")int pageSize) throws Exception;

	public int countWithRouteByKey(@Param("id")String id, @Param("key")String key, @Param("type")String type,
			@Param("createDateStart")String createDateStart, @Param("createDateEnd")String createDateEnd, @Param("state")String state,
			@Param("warehouseId")String warehouseId) throws Exception;

	public void updateWarehouseId(@Param("id")String id, @Param("warehouseId")String warehouseId) throws Exception;

	public void updateFromWarehousePrice(@Param("id")String id, @Param("warehouseId")String warehouseId) throws Exception;
	public int getlastidbyuser(@Param("userId") String userId);

	public TranshipmentBill getByIdAndUserId(@Param("id")String id, @Param("userId")String userId) throws Exception;

	public int updateState(@Param("id")String id,  @Param("state")String state) throws Exception;

	public int cancelReturn(@Param("id")String id, @Param("userId")String userId) throws Exception;
	public int updateorderidbyid(@Param("id")String id,  @Param("orderId")String orderId) throws Exception;
	

	public List<TranshipmentBill> getByTransitNo(@Param("transitNo") String transitNo,@Param("storeId") String storeId) throws Exception;
	//<!-- 更新转运入库和出库 及未入库-->
	public int updateTranshipmentBilltranStatebyid(@Param("state")String state,@Param("warehouseId")String warehouseId,@Param("tranWarehouseId")String tranWarehouseId,@Param("pretranwarehouseId")String pretranwarehouseId,@Param("storeId") String storeId,@Param("id") String id) throws Exception;
	//<!-- 更新转运本地入库，只更新状态-->
	public int updateTranshipmentBilltranStatebyid2(@Param("state")String state,@Param("warehouseId")String warehouseId,@Param("storeId") String storeId,@Param("id") String id) throws Exception;
	
	public int updateTranshipmentBilltranStatebytransit_no(@Param("state")String state,@Param("warehouseId")String warehouseId,@Param("tranWarehouseId")String tranWarehouseId,@Param("pretranwarehouseId")String pretranwarehouseId,@Param("storeId") String storeId,@Param("transitNo") String transitNo) throws Exception;
	//<!-- 更新转运本地入库，只更新状态-->
	public int updateTranshipmentBilltranStatebytransit_no2(@Param("state")String state,@Param("warehouseId")String warehouseId,@Param("storeId") String storeId,@Param("transitNo") String transitNo) throws Exception;
	
	public int updateTranshipmentBillWrongState(@Param("id") String id,@Param("state")String state,@Param("wrongRemark") String wrongRemark,@Param("storeId") String storeId) throws Exception;
	public int updateTranshipmentBillStatebyid(@Param("id") String id,@Param("state")String state,@Param("storeId") String storeId) throws Exception;
}

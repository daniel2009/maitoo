package com.meitao.dao;

import java.util.List;











import org.apache.ibatis.annotations.Param;

import com.meitao.model.RenlingBaoguo;


public interface RenlingDao {
	public int insertRenling(RenlingBaoguo renling) throws Exception;

	public int countByKey(@Param("rid") String rid, @Param("key") String key, @Param("column") String column,
	        @Param("sdate") String sdate, @Param("edate") String edate, @Param("userId") String userId,
	        @Param("state") String state) throws Exception;

	public List<RenlingBaoguo> searchByKey(@Param("rid") String rid, @Param("key") String key,
	        @Param("column") String column, @Param("sdate") String sdate, @Param("edate") String edate,
	        @Param("userId") String userId, @Param("index") int index, @Param("size") int size,
	        @Param("state") String state) throws Exception;
	
	public int deleteMultiRenlingBill(@Param("list") List<String> list)
	        throws Exception;
	
	public RenlingBaoguo getRenlingBillbyid(String id)  throws Exception;;
	
	public int updateRenling(RenlingBaoguo renling) throws Exception;
	
	public int updateClaimedByUser(RenlingBaoguo renlingBaoguo) throws Exception;

	public int countByUser(@Param("userId") String userId, @Param("nameCondition") String nameCondition) throws Exception;

	public List<RenlingBaoguo> findByUser(@Param("userId") String userId, @Param("nameCondition") String nameCondition, @Param("firstResult") int firstResult,
			@Param("maxResult") int maxResult) throws Exception;

	public int countByNotClaimed(@Param("nameCondition") String nameCondition) throws Exception;

	public List<RenlingBaoguo> findByNotClaimed(@Param("nameCondition") String nameCondition, @Param("firstResult") int firstResult, @Param("maxResult") int maxResult) throws Exception;

	public int updateState(@Param("id") String id, @Param("state") String state) throws Exception;

	public int updateTranshipmentId(@Param("id") String id, @Param("tranId") String tranId) throws Exception;

	public int countByNotClaimedAndUser(@Param("userId")String userId, @Param("nameCondition")String nameCondition)  throws Exception;;
	public List<RenlingBaoguo> findByNotClaimedAndUser(@Param("userId")String userId, @Param("nameCondition") String nameCondition, @Param("firstResult") int firstResult, @Param("maxResult") int maxResult) throws Exception;

	public int countNeedAudit() throws Exception;

	public int updateByAuditPass(@Param("state")String state, @Param("claimPersonId")String claimPersonId)  throws Exception;;

	public void updateTransitId(@Param("id")String id, @Param("transitId")String transitId)  throws Exception;;
}

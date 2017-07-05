package com.meitao.dao;

import java.util.List;

















import org.apache.ibatis.annotations.Param;

import com.meitao.model.RenlingBaoguo;
import com.meitao.model.RenlingPersons;


public interface RenlingPersonDao {
/*	public int insertRenling(RenlingBaoguo renling) throws Exception;

	public int countByKey(@Param("rid") String rid, @Param("key") String key, @Param("column") String column,
	        @Param("sdate") String sdate, @Param("edate") String edate, @Param("userId") String userId,
	        @Param("state") String state) throws Exception;

	public List<RenlingBaoguo> searchByKey(@Param("rid") String rid, @Param("key") String key,
	        @Param("column") String column, @Param("sdate") String sdate, @Param("edate") String edate,
	        @Param("userId") String userId, @Param("index") int index, @Param("size") int size,
	        @Param("state") String state) throws Exception;*/
	//删除包含认领单所对应的信息
	public int deleteMultiRenlingPersion(@Param("bills") List<String> bills)
	        throws Exception;
	public int insertRenlingPersons(RenlingPersons renlingpersons) throws Exception;
	public int updateRenlingPersonbyid(RenlingPersons renling) throws Exception;
	public List<RenlingPersons> getRenlingPersonbyRenlinginfo(@Param("renlingId") String renlingId,@Param("createPerson") String createPerson) throws Exception;

	public int countByClaimPackage(@Param("claimPackageId") String claimPackageId) throws Exception;
	public List<RenlingPersons> findByClaimPackage(@Param("claimPackageId") String claimPackageId, @Param("firstResult") int firstResult, @Param("maxResult")int maxResult) throws Exception;
	
	public int countByUser(@Param("userId") String userId, @Param("state")String state, @Param("nameCondition") String nameCondition) throws Exception;
	public List<RenlingBaoguo> findByUser(@Param("userId") String userId, @Param("state")String state, @Param("nameCondition") String nameCondition, @Param("firstResult") int firstResult,
			@Param("maxResult") int maxResult) throws Exception;
	
	public int updateOtherByAudit(@Param("renlingId") String renlingId, @Param("id") String id, @Param("state") String state, @Param("auditRemark") String auditRemark) throws Exception;
	public int updateStateAndRemark(@Param("id") String id, @Param("state") String state, @Param("auditRemark") String auditRemark) throws Exception;
	
	public RenlingPersons findLastClaimedByClaimPackageAndUser(@Param("renlingId") String renlingId, @Param("userId") String userId) throws Exception;
	public int deleteByUser(@Param("id")String id, @Param("userId")String userId) throws Exception;
	public RenlingPersons findById(@Param("id")String id) throws Exception;
	public int updateByReclaim(RenlingPersons renlingPerson) throws Exception;
	
	public int countAllByClaimPackage(@Param("claimPackageId")String claimPackageId) throws Exception;
	public List<RenlingPersons> findAllByClaimPackage(@Param("claimPackageId") String claimPackageId, @Param("firstResult") int firstResult, @Param("maxResult")int maxResult) throws Exception;
	
	public int countByKey(@Param("userId")String userId, @Param("state")String state) throws Exception;
	public int searchByKey(@Param("userId")String userId, @Param("state")String state, @Param("firstResult") int firstResult, @Param("maxResult")int maxResult) throws Exception;
}

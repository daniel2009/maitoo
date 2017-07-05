package com.meitao.dao;

import java.util.List;







import org.apache.ibatis.annotations.Param;

import com.meitao.model.AccountDetail;

public interface AccountDetailDao {
	public int insertAccountDetail(AccountDetail detail) throws Exception;

	public int modifyAccountDetail(AccountDetail detail) throws Exception;

	public int deleteAccountDetailByUserIds(List<String> list) throws Exception;

	public List<AccountDetail> getAccountDetailByUserId(@Param("userId") String userId) throws Exception;

	public AccountDetail getById(String id) throws Exception;

	public int countByKey(@Param("userId") String userId, @Param("key") String key, @Param("state") String state,
	        @Param("type") String type, @Param("groupid") String groupid) throws Exception;

	public List<AccountDetail> searchByKey(@Param("userId") String userId, @Param("key") String key,
	        @Param("state") String state, @Param("type") String type, @Param("index") int index, @Param("size") int size);

	public int updateStateByPaySuccess(@Param("userId")String userId, @Param("remark")String remark, @Param("modifyDate")String modifyDate) throws Exception;

	public String checkIfScanPayDetail(@Param("userId")String userId, @Param("amount")String amount, @Param("id")String id) throws Exception;

	public String checkStateIfPayDetailByRemark(@Param("userId")String userId, @Param("amount")String amount, @Param("remark")String remark) throws Exception;
	//根据交易单号来查找是否存在此交易，即remark中必须包含唯一的交易单号
	public AccountDetail checkStateIfPayDetailByRemarkno(@Param("remark")String remark) throws Exception;
	
	
	public int countByInfo(@Param("userId") String userId, @Param("info") String key, @Param("state") String state,
	        @Param("type") String type) throws Exception;

	public List<AccountDetail> searchByInfo(@Param("userId") String userId, @Param("info") String key,
	        @Param("state") String state, @Param("type") String type, @Param("index") int index, @Param("size") int size)  throws Exception;
	
	public int countByInfo_superadmin(@Param("storeId") String storeId,@Param("confirm_state") String confirm_state,@Param("userId") String userId, @Param("info") String key, @Param("state") String state,
	        @Param("type") String type) throws Exception;

	public List<AccountDetail> searchByInfo_superadmin(@Param("storeId") String storeId,@Param("confirm_state") String confirm_state,@Param("userId") String userId, @Param("info") String key,
	        @Param("state") String state, @Param("type") String type, @Param("index") int index, @Param("size") int size)  throws Exception;
	
	public int updateconfirmstate(@Param("id") String id, @Param("type") String type,@Param("admin_remark") String admin_remark,@Param("confirm_state") String confirm_state,@Param("modifyDate") String modifyDate)throws Exception;;

    public int counttype(@Param("type") String type)throws Exception;;//获取类型数量
    
    public List<String> countamount(@Param("type") String type,@Param("state") String state,@Param("currency") String currency)throws Exception;;//根据类型和状态获取金额
    
    public List<String> countadminamount(@Param("type") String type,@Param("confirm_state") String confirm_state,@Param("currency") String currency)throws Exception;;//获取确认类型
}

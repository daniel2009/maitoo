package com.meitao.dao;



import org.apache.ibatis.annotations.Param;


import com.meitao.model.UserSetting;//用户设置


public interface UserSettingDao {

	int insert(UserSetting userSetting) throws Exception;//
	UserSetting findself(@Param("userId")String userId) throws Exception;
	
	int updateonline(@Param("userId")String userId,@Param("z_store")String z_store,@Param("z_cid")String z_cid,@Param("modifyDate")String modifyDate) throws Exception;//更新在线置单默认设置
	int updateSm(@Param("userId")String userId,@Param("s_store")String s_store,@Param("modifyDate")String modifyDate) throws Exception;//更新上门收货数据


}

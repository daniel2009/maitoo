package com.meitao.dao;

import java.util.List;



import org.apache.ibatis.annotations.Param;

import com.meitao.model.EndiciaLabel;


public interface EndicialLabelDao {
	public int insertEndiciaLabel(EndiciaLabel label) throws Exception;
	public EndiciaLabel getEndiciaLabelById(String id) throws Exception;
	public EndiciaLabel getEndiciaLabelByUserId(String userId) throws Exception;

	public int countByKey(@Param("userId") String userId,@Param("userInfo") String userInfo, @Param("labelInfo") String labelInfo, @Param("sdate") String sdate, @Param("edate") String edate)
	        throws Exception;
	
	
	public List<EndiciaLabel> searchByKey(@Param("userId") String userId,@Param("userInfo") String userInfo, @Param("labelInfo") String labelInfo, @Param("sdate") String sdate, @Param("edate") String edate, @Param("index") int index, @Param("size") int size)
	        throws Exception;
	
	
}

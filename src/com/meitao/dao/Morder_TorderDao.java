package com.meitao.dao;

import java.util.List;





import org.apache.ibatis.annotations.Param;

import com.meitao.model.Morder_Torder;
import com.meitao.model.User;

public interface Morder_TorderDao {
	public Morder_Torder getById(String id) throws Exception;
	public List<String> getorderIds(@Param("torderId") String torderId)throws Exception;
	public List<String> gettorderIds(@Param("orderId") String orderId)throws Exception;
	public int insert(Morder_Torder mtorder);
	
	public List<String> deletebyorderId(@Param("orderId") String orderId)throws Exception;
	public List<String> deletebytorderId(@Param("torderId") String torderId)throws Exception;

	
	
}

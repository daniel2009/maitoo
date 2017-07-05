package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;



import com.meitao.model.FreezeMoney;
import com.meitao.model.TranPrice;

public interface FreezeMoneyDao {
	public int insert(FreezeMoney freeze) throws Exception;
    public int delete(@Param("id") String id) throws Exception;
    public FreezeMoney getbyid(@Param("id") String id);
    public List<FreezeMoney> getbyuserId(@Param("userId") String userId);
    public int deletebyuserId(@Param("userId") String userId);
	
}

package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;


import com.meitao.model.SeaNumber;


public interface SeaNumberDao {
	

	//插入海关单号
	public int insertSeaNumber(SeaNumber seanumber) throws Exception;
	//获取总海关单号数量
	public int getallnumbers()throws Exception;
	//获取还没有用的海关单号数量
	public int getavailablenumbers()throws Exception;
	//获取已用的海关单号数量
	public int getunavailablenumbers()throws Exception;
	//获取一条可用的海关单号
	public SeaNumber getone()throws Exception;
	public int updatestate(SeaNumber seanumber)throws Exception;;
	public int countOfsearchSeaNumber(@Param("orderId")String orderId,@Param("state")String state)throws Exception;;
	public List<SeaNumber> searchSeaNumber(@Param("orderId")String orderId,@Param("state")String state, @Param("index") int index, @Param("size") int size)throws Exception;;
    public int deleteoneseanumber(@Param("id")String id) throws Exception;;
    public int existorderid(@Param("orderId")String orderId)  throws Exception;;
}

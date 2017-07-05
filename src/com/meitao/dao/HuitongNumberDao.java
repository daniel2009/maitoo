package com.meitao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;



import com.meitao.model.HuitongNumber;



public interface HuitongNumberDao {
	

	//插入汇通单号
	public int insertHuitongNumber(HuitongNumber seanumber) throws Exception;
	//获取总汇通单号数量
	public int getallnumbers()throws Exception;
	//获取还没有用的汇通单号数量
	public int getavailablenumbers()throws Exception;
	//获取已用的汇通单号数量
	public int getunavailablenumbers()throws Exception;
	//获取一条可用的汇通单号
	public HuitongNumber getone()throws Exception;
	public int updatestate(HuitongNumber seanumber)throws Exception;;
	public int countOfsearchHuitongNumber(@Param("orderId")String orderId,@Param("state")String state)throws Exception;;
	public List<HuitongNumber> searchHuitongNumber(@Param("orderId")String orderId,@Param("state")String state, @Param("index") int index, @Param("size") int size)throws Exception;;
    public int deleteoneHuitongnumber(@Param("id")String id) throws Exception;;
    public int existorderid(@Param("orderId")String orderId)  throws Exception;;
}

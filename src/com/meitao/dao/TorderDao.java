
package com.meitao.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.meitao.model.M_order;
import com.meitao.model.T_order;

//插入预报信息
public interface TorderDao {

	//插入预报运单信息
	public int insertyb(T_order order) throws Exception;
	public int countOfftorder(@Param("userId") String userId,@Param("torderId") String torderId)throws Exception;
	
	public int countOfSearchTordersbyuser(@Param("userId") String userId,@Param("state") String state,@Param("keyword") String keyword)throws Exception;
	public List<T_order> SearchTordersbyuser(@Param("userId") String userId,@Param("state") String state,@Param("keyword") String keyword,@Param("index") int index, @Param("size") int size)throws Exception;
	
	
	public int countOfSearchTordersbyuser1(@Param("userId") String userId,@Param("state") String state,@Param("keyword") String keyword,@Param("storeId") String storeId,@Param("i_storeId") String i_storeId)throws Exception;
	public List<T_order> SearchTordersbyuser1(@Param("userId") String userId,@Param("state") String state,@Param("keyword") String keyword,@Param("storeId") String storeId,@Param("i_storeId") String i_storeId,@Param("index") int index, @Param("size") int size)throws Exception;
	
	public int deleteone(@Param("userId") String userId,@Param("id") String id)throws Exception;
	
	public int deleteonebyadmin(@Param("id") String id)throws Exception;
	
	public T_order getone(@Param("userId") String userId,@Param("id") String id)throws Exception;
	public T_order getonebyadmin(@Param("storeId") String storeId,@Param("id") String id)throws Exception;//后台管理员获取一条运单
	
	public List<T_order> gettorders(@Param("storeId") String storeId,@Param("state") String state,@Param("torderId") String torderId)throws Exception;//获取转运单信息
	
	public int modify_zy_torder(T_order order)throws Exception;//修改转运单
	
	public int modify_state(@Param("modifyDate") String modifyDate,@Param("storeId") String storeId,@Param("state") String state,@Param("pre_state") List<String> pre_state,@Param("torderId") String torderId)throws Exception;//获取转运单信息
    public List<T_order> gettordersbyprestate(@Param("storeId") String storeId,@Param("pre_state") List<String> pre_state,@Param("torderId") String torderId)throws Exception;//获取转运单信息

    public int modifystatebyId(@Param("state") String state,@Param("id") String id,@Param("modifyDate") String modifyDate,@Param("orderids") String orderids,@Param("tremark") String tremark)throws Exception;//修改状态通过id
    
    public List<T_order> gettordersbystore(@Param("storeId") String storeId,@Param("states") List<String> states,@Param("torderId") String torderId,@Param("type") String type)throws Exception;//修改状态通过id
    
    
    public int modify_local_in_torder(T_order order)throws Exception;//本地接收入库
    
    public int insertfail(T_order order)throws Exception;//本地接收入库
    
    public int insert_zyinlogcal_fail_torder(T_order order)throws Exception;//转运州到本地录入失败后，重新录入
    
    public int insert_local_fail_torder(T_order order)throws Exception;//本地州录入失败运单
    
    
    public int getstatecount(@Param("userId") String userId,@Param("state") String state)throws Exception;//获取转运单数量
    
    public int modify_fail_torder(@Param("id") String id,@Param("state") String state,@Param("remark") String remark,@Param("qremark") String qremark,@Param("userId") String userId,@Param("modifyDate") String modifyDate);
    
    
    public int modify_state_torder(@Param("id") String id,@Param("state") String state,@Param("qremark") String qremark,@Param("modifyDate") String modifyDate);
    
    public int modify_paybyself_torder(T_order order);


    public int countOfSearchTordersbyadmin(@Param("s_cdate") String s_cdate,@Param("e_cdate") String e_cdate,@Param("s_idate") String s_idate,@Param("e_idate") String e_idate,@Param("storeId") String storeId,@Param("i_storeId") String i_storeId,@Param("s_storeId") String s_storeId,@Param("payway") String payway,@Param("state") String state,@Param("keyword") String keyword)throws Exception;
	public List<T_order> SearchTordersbyadmin(@Param("s_cdate") String s_cdate,@Param("e_cdate") String e_cdate,@Param("s_idate") String s_idate,@Param("e_idate") String e_idate,@Param("storeId") String storeId,@Param("i_storeId") String i_storeId,@Param("s_storeId") String s_storeId,@Param("payway") String payway,@Param("state") String state,@Param("keyword") String keyword,@Param("index") int index, @Param("size") int size)throws Exception;
	
	public int clear_positionId(@Param("positionId") String positionId,@Param("modifyDate") String modifyDate)throws Exception;//清空占用的仓位
}

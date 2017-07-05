package com.meitao.dao;

import java.util.List;






















import org.apache.ibatis.annotations.Param;



import com.meitao.model.M_order;


public interface MorderDao {
	/**
	 * 插入运单到数据库中
	 * 
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public int insertMorder(M_order order) throws Exception;
	
	public int insertTranMorder(M_order order) throws Exception;//插入转运运单信息
	
	public int updateOrderid(@Param("id") String id,@Param("orderId") String orderId) throws Exception;
	
	public int modifyMorderPay(@Param("list") List<String> ids, @Param("payDate") String payDate,
	        @Param("state") String state,@Param("payornot") String payornot) throws Exception;
	
	public M_order getById(@Param("id") String id,@Param("storeId") String storeId) throws Exception;
	
	public M_order getByjinId(@Param("id") String id,@Param("storeId") String storeId) throws Exception;
	
	public M_order getByOrderId(@Param("orderId") String orderId,@Param("storeId") String storeId) throws Exception;
	
	public List<M_order> getByOrderIdflag(@Param("orderId") String orderId,@Param("storeId") String storeId) throws Exception;//根据orderId进行模拟查询

	public List<M_order> SearchMOrders(@Param("oid") String oid,@Param("sod")String sod,@Param("god")String god,@Param("flyno") String flyno,@Param("wid") String wid,@Param("cid")String cid,@Param("userinfo")String userinfo,@Param("commudityinfo")String commudityinfo,@Param("state")String state,@Param("type")String type,@Param("payway")String payway,@Param("sdate")String sdate,@Param("edate")String edate,@Param("psdate")String psdate,@Param("pedate")String pedate,@Param("cardurl") String cardurl,@Param("cardother") String cardother,@Param("cardtogether") String cardtogether,@Param("cardid") String cardid,@Param("uploadflag") String uploadflag,@Param("payornot") String payornot,@Param("index") int index, @Param("size") int size)
	        throws Exception;

	public int countOfSearchMOrders(@Param("oid") String oid,@Param("sod")String sod,@Param("god")String god,@Param("flyno") String flyno,@Param("wid") String wid,@Param("cid")String cid,@Param("userinfo")String userinfo,@Param("commudityinfo")String commudityinfo,@Param("state")String state,@Param("type")String type,@Param("payway")String payway,@Param("sdate")String sdate,@Param("edate")String edate,@Param("psdate")String psdate,@Param("pedate")String pedate,@Param("cardurl") String cardurl,@Param("cardother") String cardother,@Param("cardtogether") String cardtogether,@Param("cardid") String cardid,@Param("uploadflag") String uploadflag,@Param("payornot") String payornot) throws Exception;

	public int updateSorderid(M_order order) throws Exception;//更新海关号
	
	public int deleteByIds(@Param("ids") List<String> ids, @Param("states") List<String> states,@Param("storeId") String storeId) throws Exception;
	public int deleteByIdsandUser(@Param("ids") List<String> ids, @Param("states") List<String> states,@Param("userId") String userId) throws Exception;
	
	
	
	public List<String> getOrderIdsbyIds(@Param("ids") List<String> ids) throws Exception;
	
	public String getOrderIdbyId(@Param("id") String id) throws Exception;
	
	public int modifyroutestate(@Param("id") String id,@Param("state") String state,@Param("thirdPNS") String thirdPNS,@Param("thirdNo") String thirdNo,@Param("storeId") String storeId) throws Exception;

	public M_order getRouteArgsbyOrderId(@Param("orderId") String orderId,@Param("storeId") String storeId) throws Exception;
	public int modifyroutestatebyorderId(@Param("modifyDate") String modifyDate,@Param("orderId") String orderId,@Param("state") String state,@Param("thirdPNS") String thirdPNS,@Param("thirdNo") String thirdNo,@Param("storeId") String storeId) throws Exception;

	public M_order getRuser(@Param("orderId") String orderId,@Param("storeId") String storeId) throws Exception;
	
	public M_order getFlyArgsbyOrderId(@Param("orderId") String orderId,@Param("storeId") String storeId) throws Exception;
	
	public int modifyMorderFlyInfo(@Param("orderId") String orderId,@Param("state") String state,@Param("modifyDate") String modifyDate,@Param("flyno") String flyno,@Param("storeId") String storeId) throws Exception;
	public int countOfflynos(@Param("flyno") String flyno) throws Exception;
	
	public List<M_order> searchMordersbyflyno(@Param("flyno")String flyno)  throws Exception;
	
	public int modifyMorderflightnobyflightno(@Param("flyno") String flyno,@Param("newflyno") String newflyno, @Param("date") String date) throws Exception;
	
	public M_order getRuserbyid(@Param("id") String id,@Param("storeId") String storeId) throws Exception;
	
	
	
	public List<M_order> SearchMsMOrders(@Param("empid") String empid,@Param("storeId")String storeId,@Param("cid")String cid,@Param("userinfo") String userinfo,@Param("state") String state,@Param("time")String time,@Param("payornot")String payornot,@Param("index") int index, @Param("size") int size)
	        throws Exception;

	public int countOfSearchMsMOrders(@Param("empid") String empid,@Param("storeId")String storeId,@Param("cid")String cid,@Param("userinfo") String userinfo,@Param("state") String state,@Param("time")String time,@Param("payornot")String payornot) throws Exception;

	public int modifyMsstate(@Param("empid") String empid,@Param("ModifyDate") String ModifyDate,@Param("id") String id,@Param("state") String state,@Param("storeId")String storeId) throws Exception;
	public int modifyMsInfo(M_order order)  throws Exception;//修改门店提交的内容
	
	
	public List<M_order> SearchMOrdersbyUser(@Param("userId") String userId,@Param("oid") String oid,@Param("wid") String wid,@Param("cid")String cid,@Param("info")String info,@Param("state")String state,@Param("type")String type,@Param("payornot") String payornot,@Param("index") int index, @Param("size") int size)
	        throws Exception;

	public int countOfSearchMOrdersbyUser(@Param("userId") String userId,@Param("oid") String oid,@Param("wid") String wid,@Param("cid")String cid,@Param("info")String info,@Param("state")String state,@Param("type")String type,@Param("payornot") String payornot) throws Exception;
	
	
	public int modifyUserMOrderPay(@Param("id") String id,@Param("userId") String userId,@Param("payornot") String payornot,@Param("modifyDate") String modifyDate,@Param("payDate") String payDate,@Param("payWay") String payWay) throws Exception;
	
	public int gettotalQ(@Param("userId") String userId) throws Exception; //总运单数
	public int getonlineQ(@Param("userId") String userId) throws Exception; //在线置单数
	public int getabandonQ(@Param("userId") String userId) throws Exception; //作废运单数
	public int getquestionQ(@Param("userId") String userId) throws Exception; //异常件数
	public int getnopayQ(@Param("userId") String userId) throws Exception; //未付款运单数
	
	public int gettranwaitpQ(@Param("userId") String userId) throws Exception; //获取转运待处理运单数
	
	
	public int updateMorder(M_order order) throws Exception;
	public int updateMorder_cost(M_order order) throws Exception;
	
	public int updateMorder_tran(M_order order) throws Exception;
	
	public int countordersbyid(@Param("orderId") String orderId) throws Exception;//计算运单的个数
	//state 入库修改的路由状态，i_state标识为已入库状态，i_employeeId入库操作员id号，modifyDate修改时间，orderId操作的运单号，i_storeId入库的门店id号，storeId原来门店号
	public int modifyinputstate(@Param("state") String state,@Param("i_state") String i_state,
			@Param("i_employeeId") String i_employeeId,@Param("modifyDate") String modifyDate,@Param("i_date") String i_date,@Param("orderId") String orderId,@Param("i_storeId") String i_storeId,@Param("storeId") String storeId,@Param("i_weight") String i_weight) throws Exception;//修入改库状态


	
//搜索入库运单信息
	public List<M_order> SearchinputMOrders(@Param("oid") String oid,@Param("i_storeId") String i_storeId,@Param("i_employeeId") String i_employeeId,@Param("wid") String wid,
			@Param("cid")String cid,@Param("state")String state,@Param("i_state")String i_state,@Param("payway")String payway,
			@Param("sdate")String sdate,@Param("edate")String edate,@Param("index") int index, @Param("size") int size)
	        throws Exception;

	public int countofSearchinputMOrders(@Param("oid") String oid,@Param("i_storeId") String i_storeId,@Param("i_employeeId") String i_employeeId,@Param("wid") String wid,
			@Param("cid")String cid,@Param("state")String state,@Param("i_state")String i_state,@Param("payway")String payway,
			@Param("sdate")String sdate,@Param("edate")String edate) throws Exception;
	
	public int updateHuitongNo(@Param("id") String id,@Param("huitongNo") String huitongNo,@Param("modifyDate") String modifyDate)throws Exception;
	
	public List<String> getrevphone(@Param("s_paydate") String s_paydate,@Param("e_paydate") String e_paydate)throws Exception;
	
	//根据支付时间和单号关键字查找单号
	public List<M_order> getpayDateMorder(@Param("order_key") String order_key,@Param("s_paydate") String s_paydate,@Param("e_paydate") String e_paydate)throws Exception;



//查找是否支付运单String userId,String orderId,String info,String payornot, int pageSize, int pageNow
	public List<M_order> SearchMOrdersbyUserpayornot(@Param("userId") String userId,@Param("oid") String orderId,@Param("wid") String wid,@Param("cid")String cid,@Param("info")String info,@Param("state")String state,@Param("minstate")String minstate,@Param("type")String type,@Param("payornot") String payornot,@Param("index") int index, @Param("size") int size)
	        throws Exception;

	public int countOfSearchMOrdersbyUserpayornot(@Param("userId") String userId,@Param("oid") String oid,@Param("wid") String wid,@Param("cid")String cid,@Param("info")String info,@Param("state")String state,@Param("minstate")String minstate,@Param("type")String type,@Param("payornot") String payornot) throws Exception;
	

//计算待付款运单数量
	public int countOfSearchMOrders_nopay(@Param("oid") String oid,@Param("sod")String sod,@Param("god")String god,@Param("flyno") String flyno,@Param("wid") String wid,@Param("cid")String cid,@Param("userinfo")String userinfo,@Param("commudityinfo")String commudityinfo,@Param("state")String state,@Param("type")String type,@Param("payway")String payway,@Param("sdate")String sdate,@Param("edate")String edate,@Param("psdate")String psdate,@Param("pedate")String pedate,@Param("cardurl") String cardurl,@Param("cardother") String cardother,@Param("cardtogether") String cardtogether,@Param("cardid") String cardid,@Param("uploadflag") String uploadflag,@Param("payornot") String payornot) throws Exception;
	public List<M_order> SearchMOrders_nopay(@Param("oid") String oid,@Param("sod")String sod,@Param("god")String god,@Param("flyno") String flyno,@Param("wid") String wid,@Param("cid")String cid,@Param("userinfo")String userinfo,@Param("commudityinfo")String commudityinfo,@Param("state")String state,@Param("type")String type,@Param("payway")String payway,@Param("sdate")String sdate,@Param("edate")String edate,@Param("psdate")String psdate,@Param("pedate")String pedate,@Param("cardurl") String cardurl,@Param("cardother") String cardother,@Param("cardtogether") String cardtogether,@Param("cardid") String cardid,@Param("uploadflag") String uploadflag,@Param("payornot") String payornot,@Param("index") int index, @Param("size") int size)
	        throws Exception;
	
	
	public int clear_position(@Param("positionId") String positionId,@Param("modifyDate") String modifyDate) throws Exception;
	
	
	
	
	public int countOfSearchMOrders_paybyuser(@Param("oid") String oid,@Param("sod")String sod,@Param("god")String god,@Param("flyno") String flyno,@Param("wid") String wid,@Param("cid")String cid,@Param("userinfo")String userinfo,@Param("commudityinfo")String commudityinfo,@Param("state")String state,@Param("type")String type,@Param("payway")String payway,@Param("sdate")String sdate,@Param("edate")String edate,@Param("psdate")String psdate,@Param("pedate")String pedate,@Param("cardurl") String cardurl,@Param("cardother") String cardother,@Param("cardtogether") String cardtogether,@Param("cardid") String cardid,@Param("uploadflag") String uploadflag,@Param("payornot") String payornot,@Param("confirm_user_pay") String confirm_user_pay) throws Exception;
	public List<M_order> SearchMOrders_paybyuser(@Param("oid") String oid,@Param("sod")String sod,@Param("god")String god,@Param("flyno") String flyno,@Param("wid") String wid,@Param("cid")String cid,@Param("userinfo")String userinfo,@Param("commudityinfo")String commudityinfo,@Param("state")String state,@Param("type")String type,@Param("payway")String payway,@Param("sdate")String sdate,@Param("edate")String edate,@Param("psdate")String psdate,@Param("pedate")String pedate,@Param("cardurl") String cardurl,@Param("cardother") String cardother,@Param("cardtogether") String cardtogether,@Param("cardid") String cardid,@Param("uploadflag") String uploadflag,@Param("payornot") String payornot,@Param("confirm_user_pay") String confirm_user_pay,@Param("index") int index, @Param("size") int size)
	        throws Exception;
//改变用户自付的处理状态
	public int change_paybyuserstate(@Param("confirm_user_pay") String confirm_user_pay,@Param("modifyDate") String modifyDate,@Param("id") String id) throws Exception;
}

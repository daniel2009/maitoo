package com.meitao.service;

import java.util.List;








import com.meitao.exception.ServiceException;
import com.meitao.model.M_order;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.temp.ImportMorder;
import com.meitao.model.temp.ImportthirdMorder;



public interface MorderService {

	public abstract double calculationM_orderFreight(M_order order) throws ServiceException;
	public double calculationM_orderFreight_bytype(M_order order) throws ServiceException;//计算类型价格，如上门收货，网上置单价格等
	
	public double calculationM_orderFreight_tran(M_order order)
			throws ServiceException;//计算包含转运费用的接口
	
	public double calculationM_orderFreight_usertype_tran(M_order order,String type)
			throws ServiceException;//指定价格类型来获取价格,包含转运费用
	
	public double calculationM_orderFreight_usertype(M_order order,String type)
			throws ServiceException;//指定价格类型来获取价格
	
	
	public abstract double calculationM_OrderCostFreight(M_order order) throws ServiceException;//计算成功
	
	public String sendChinaMeitaoMessage(String message,String phone);
	
	public ResponseObject<Object> add_ms_Morder(M_order order) throws ServiceException;//添加门市运单
	public ResponseObject<Object> modify_ms_Morder(M_order order) throws ServiceException;//修改门市运单
	
	public ResponseObject<Object> modify_orpay_Morder(M_order order,boolean changepayornot,boolean changestate,String payflag,String emplyeeId,String storeId) throws ServiceException;//修改运单或修改直接支付运单,changepayornot表示改变是否支付，changestate路由状态是否改变
	
	//修改用户提交上来的运单
	public ResponseObject<Object> modify_tran_Morder(M_order order,String emplyeeId,String storeId) throws ServiceException;
	
	//修改为未付款运单
	public ResponseObject<Object> modify_tran_Morder_nopay(M_order order,String emplyeeId,String storeId,String shelvesNo) throws ServiceException;
	
	public ResponseObject<Object> getonebyid(String id,String storeId) throws ServiceException;
	
	public ResponseObject<Object> getonebyuser(String id,String userId) throws ServiceException;
	
	public ResponseObject<PageSplit<M_order>> searchMorders(String oid,String sod,String god,String flyno,String wid,String cid,String userinfo,String commudityinfo,String state,String type,String payway,String sdate,String edate, String psdate,String pedate,String cardinfo,String payornot, int pageSize, int pageNow)
	        throws ServiceException;
	
	//获取未付款运单
	public ResponseObject<PageSplit<M_order>> searchMordersnopay(String oid,String sod,String god,String flyno,String wid,String cid,String userinfo,String commudityinfo,String state,String type,String payway,String sdate,String edate, String psdate,String pedate,String cardinfo,String payornot,int pageSize, int pageNow)
	        throws ServiceException;
	
	//获取用户自付运单
	public ResponseObject<PageSplit<M_order>> searchMorderspaybyuser(String oid,String sod,String god,String flyno,String wid,String cid,String userinfo,String commudityinfo,String state,String type,String payway,String sdate,String edate, String psdate,String pedate,String cardinfo,String payornot,String confirm_user_pay,  int pageSize, int pageNow)
	        throws ServiceException;
	
	public ResponseObject<Object> deleteMorderByIds(List<String> ids,List<String> states,String storeId) throws ServiceException;

	public ResponseObject<Object> modifyroutestate(String id,String state,String thirdPNS,String thirdNo,String storeId) throws ServiceException;

	public ResponseObject<Object> getoneMorderRoute(String kuaidiType,String orderId,String storeId) throws ServiceException;
	
	public ResponseObject<Object> importMorderState(List<ImportMorder> importOrders, String empName,String wid,String sendmessage)
	        throws ServiceException;
	public String sendEnglishMessage(String message,String phone);
	public String sendChinaMessage(String message,String phone);
	public String sendChinaMessageCardid(String message,String phone);
	
	//门店的搜索
	public ResponseObject<PageSplit<M_order>> searchMsMorders(String empid,String wid,String cid,String userinfo,String state,String time,String payornot, int pageSize, int pageNow)
	        throws ServiceException;
	
	public ResponseObject<Object> modifyMsroutestate(String empid,String ModifyDate,String id,String state,String storeId) throws ServiceException;
	public ResponseObject<Object> modifyMsInfo(M_order order,String state,String payornot,String emplyeeId,String storeId) throws ServiceException;
	
	//用户搜索自己的运单信息
	public ResponseObject<PageSplit<M_order>> searchMordersbyUser(String userId,String oid,String wid,String cid,String info,String state,String type,String payornot, int pageSize, int pageNow)
	        throws ServiceException;
	
	
	//查询未来支付运单
	public ResponseObject<PageSplit<M_order>> searchMordersbyUserpayornot(String userId,String orderId,String info,String payornot, int pageSize, int pageNow)
	        throws ServiceException;
	
	public ResponseObject<Object> userPayOne(String id, String orderId, String userId, String amount,
	        double newrmb, double newusd, boolean accountPay) throws ServiceException;
	
	
	public ResponseObject<Object> adminPayOne(String id, String orderId, String userId, String amount,String storeName,String storeId,String empName,String empNo,
	        double newrmb, double newusd, boolean accountPay) throws ServiceException;
	
	public ResponseObject<Object> add_online_Morder(M_order order,String rflag,String sflag,String reflag) throws ServiceException;//用户添加在线置单
	public ResponseObject<Object> modify_online_Morder(M_order order,String rflag,String sflag,String reflag) throws ServiceException;//用户添加在线置单
	
	public ResponseObject<Object> import_third_orders(ImportthirdMorder iorders) throws ServiceException;//添加第三方运单
	
	
	
	public ResponseObject<PageSplit<M_order>> searchMordersInput(String oid,String i_storeId,String wid,String cid,String state,String i_state,String payway,String sdate,String edate,String empId, int pageSize, int pageNow)
	        throws ServiceException;
	
	//根据收费时间发送短信
	public ResponseObject<Object> send_rev_message(String resend,String s_date,String e_date,String messagetype) throws ServiceException;//发送揽收信息
	
	public String createOrderIdargnewMT(String id,String widid,String type_no) throws ServiceException;
}

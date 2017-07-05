package com.meitao.service;

import java.util.List;














import org.springframework.web.bind.annotation.RequestParam;

import com.meitao.exception.ServiceException;
import com.meitao.model.Commodity_price;
import com.meitao.model.M_order;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.T_order;
import com.meitao.model.temp.ImportMorder;
import com.meitao.model.temp.ImportthirdMorder;



public interface TorderService {


	
	public ResponseObject<Object> add_zy_Torder(List<T_order> order) throws ServiceException;//添加转运预报
	public String getjwweight(String weight) throws ServiceException;//添加转运预报;
	public String getprice(String type,Commodity_price cobj) throws ServiceException;//添加转运预报;
	
	public double calculationt_orderFreight_usertype(double t_weight,double i_weight,String type,String commid,String cid,String wid)
			throws ServiceException;
	
	public ResponseObject<PageSplit<T_order>> search_zy_byuser(String userId,String state,String keyword,int pageSize, int pageNow) throws ServiceException;
	
	public ResponseObject<PageSplit<T_order>> search_zy_byadmin(String s_cdate,String e_cdate,String s_idate,String e_idate,String storeId,String i_storeId,String s_storeId,String payway,String state,String keyword,int pageSize, int pageNow) throws ServiceException;
	
	//查找入库包裹
	public ResponseObject<PageSplit<T_order>> search_in_byuser(String userId,String state,String i_storeId,String storeId,String keyword,int pageSize, int pageNow) throws ServiceException;
	

	public ResponseObject<Object> deleteone(String userId,String id) throws ServiceException;//删除一条运单
	
	public ResponseObject<Object> getone(String userId,String id) throws ServiceException;//获取一条运单
	
	public ResponseObject<Object> getoneroute(String userId,String id) throws ServiceException;//获取一条运单
	
	public ResponseObject<Object> getoneyb(String torderId) throws ServiceException;//获取一条预报信息
	
	//转运入库处理
	public ResponseObject<Object> zyrkprocess(String id,String torderId,String storeId,String empId,String userId,String weight,String shelvesId) throws ServiceException;//后台进行入库处理
	
	//torderId:转运单号，state--要修改的状态，pre_state之前 的状态在此范围内，storeId--运单所属的门店
	public ResponseObject<Object> modifystate(String torderId,String state,List<String> pre_state,String storeId,String empId,String tremark) throws ServiceException;//后台进行入库处理
	
	public ResponseObject<Object> getonebystore(String torderId,List<String> states,String storeId,String type) throws ServiceException;//获取门店下的一条预报运单
	
	
	
	//本地州入库处理
	public ResponseObject<Object> localkprocess(String id,String torderId,String storeId,String empId,String userId,String weight,String shelvesId) throws ServiceException;//后台进行入库处理
	
	//转运州包裹到本地入库
	public ResponseObject<Object> zytolocalkprocess(String id,String torderId,String storeId,String i_storeId,String empId,String userId,String weight,String shelvesId) throws ServiceException;//后台进行入库处理
	
	

	
	public ResponseObject<Object> set_fail_torder(String torderId,String remark,String shelvesId,String qremark,String storeId,String empId,String weight)throws ServiceException;//转运州入库失败
	
	public ResponseObject<Object> set_zyinlogcal_fail_torder(String torderId,String remark,String shelvesId,String qremark,String storeId,String i_storeId,String empId,String weight)throws ServiceException;//转运州包裹进入本地失败，示入失败运单
	
	public ResponseObject<Object> set_local_fail_torder(String torderId,String remark,String shelvesId,String qremark,String i_storeId,String empId,String weight)throws ServiceException;//本地入库失败运单失败运单
	
	public ResponseObject<Object> add_tran_Morder(List<M_order> orders) throws ServiceException;//用户添加家户提交的转运单并生成运单
	
	public ResponseObject<Object> add_tran_Morder_1(List<M_order> orders,double freezemoney,double tmoney,double tcost) throws ServiceException;//用户添加家户提交的转运单并生成运单,freezemoney表示要冻结的钱
}

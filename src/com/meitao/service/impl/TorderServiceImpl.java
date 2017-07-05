package com.meitao.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;

import jxl.common.Logger;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;















































import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitao.dao.AccountDao;
import com.meitao.dao.AccountDetailDao;
import com.meitao.dao.CardIdManageDao;
import com.meitao.dao.ChannelDao;
import com.meitao.dao.CommudityPriceDao;
import com.meitao.dao.ConsigneeInfoDao;
import com.meitao.dao.ConsigneeSendpersonDao;
import com.meitao.dao.EmployeeDao;
import com.meitao.dao.FlyInfoDao;
import com.meitao.dao.FreezeMoneyDao;
import com.meitao.dao.MdetailDao;
import com.meitao.dao.MorderDao;
import com.meitao.dao.Morder_TorderDao;
import com.meitao.dao.OrderMidfixDao;
import com.meitao.dao.Receive_UserDao;
import com.meitao.dao.RouteDao;
import com.meitao.dao.SeaNumberDao;
import com.meitao.dao.SendUserDao;
import com.meitao.dao.Send_UserDao;
import com.meitao.dao.ShelvesDao;
import com.meitao.dao.Shelves_positionDao;
import com.meitao.dao.TorderDao;
import com.meitao.dao.TranshipmentBillDao;
import com.meitao.dao.TranshipmentCommodityDao;
import com.meitao.dao.TranshipmentRouteDao;
import com.meitao.dao.TrouteDao;
import com.meitao.dao.TtranpriceDao;
import com.meitao.dao.UserDao;
import com.meitao.dao.WarehouseDao;
import com.meitao.dao.globalargsDao;
import com.meitao.service.AutoSendService;
import com.meitao.service.MorderService;
import com.meitao.service.StoragePositionRecordService;
import com.meitao.service.StoragePositionService;
import com.meitao.service.TorderService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.kuaidi.KuaiDiUtil;
import com.meitao.common.util.ConsigneeInfoUtil;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.common.util.OrderUtil;
import com.meitao.common.util.PropertiesReader;
import com.meitao.common.util.StringUtil;
import com.meitao.common.util.TorderUtil;
import com.meitao.common.util.sms.SimpleSmsSender;
import com.meitao.exception.ServiceException;
import com.meitao.model.Account;
import com.meitao.model.AccountDetail;
import com.meitao.model.Channel;
import com.meitao.model.Commodity_price;
import com.meitao.model.ConsigneeInfo;
import com.meitao.model.ConsigneeSendperson;
import com.meitao.model.Employee;
import com.meitao.model.FreezeMoney;
import com.meitao.model.M_OrderDetail;
import com.meitao.model.M_order;
import com.meitao.model.Morder_Torder;
import com.meitao.model.PageSplit;
import com.meitao.model.Receive_User;
import com.meitao.model.ResponseObject;
import com.meitao.model.Route;
import com.meitao.model.SeaNumber;
import com.meitao.model.Shelves_position;
import com.meitao.model.T_order;
import com.meitao.model.T_route;
import com.meitao.model.T_tran_price;
import com.meitao.model.User;
import com.meitao.model.Warehouse;
import com.meitao.model.temp.ImportMorder;
import com.meitao.model.temp.ImportthirdMorder;
import com.meitao.model.temp.MessageTemp;




@Service("torderService")
public class TorderServiceImpl extends BasicService implements TorderService {
	private static final Logger log = Logger.getLogger(TorderServiceImpl.class);
	
	@Autowired
	private TranshipmentBillDao transhipmentBillDao;
	@Autowired
	private TranshipmentCommodityDao transhipmentCommodityDao;
	@Autowired
	private ConsigneeInfoDao consigneeInfoDao;
	@Autowired
	private RouteDao routeDao;
	@Autowired
	private AccountDao accountDao;
	@Autowired
	private AccountDetailDao accountDetailDao;
	@Autowired
	private UserDao userDao;

	@Autowired
	private Morder_TorderDao morder_TorderDao;
	
	@Autowired
	private FreezeMoneyDao freezeMoneyDao;
	
	
	
	@Autowired
	private CommudityPriceDao commudityPriceDao;
	
	@Autowired
	private SendUserDao sendUserDao;

	@Autowired
	private MdetailDao m_DetailDao;
	@Autowired
	private MorderDao m_orderDao;
	
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private TrouteDao trouteDao;	
	
	
	@Autowired
	private ShelvesDao shelvesDao;
	
	@Autowired
	private Shelves_positionDao shelves_positionDao;
	
	@Autowired
	private TtranpriceDao ttranpriceDao;
	
	
	@Resource(name = "storagePositionService")
	private StoragePositionService storagePositionService;
	@Resource(name = "storagePositionRecordService")
	private StoragePositionRecordService storagePositionRecordService;
	@Resource(name = "autoSendService")
	private AutoSendService autoSendService;

	@Autowired
	private OrderMidfixDao orderMidfixDao;
	@Autowired
	private WarehouseDao warehouseDao;

	@Autowired
	private ChannelDao channelDao;
	@Autowired
	private globalargsDao globalargsDao;
	
	@Autowired
	private TorderDao torderDao;

	@Autowired
	private FlyInfoDao flyinfoDao;
	
	@Autowired
	private TranshipmentRouteDao transhipmentRouteDao;
	
	@Autowired
	private Receive_UserDao receive_UserDao;

	@Autowired
	private SeaNumberDao seaNumberDao;
	@Autowired
	private ConsigneeSendpersonDao consigneeSendpersonDao;
	
	@Autowired
	private CardIdManageDao cardIdManageDao;
	
	@Autowired
	private Send_UserDao send_UserDao;
	
	@Resource(name = "m_orderService")
	private MorderService m_orderService;	
	
	public String getjwweight(String weight)
	{
		
		
		try{
			String lowest_weight_value_flag =this.globalargsDao.getcontentbyflag("lowest_weight_value_flag");//最低重量
			String jw_commodity_rate =this.globalargsDao.getcontentbyflag("jw_commodity_rate");//重量进位值String lowest_weight_value_flag =this.globalargsDao.getcontentbyflag("lowest_weight_value_flag");//最低重量
			//String jw_commodity_rate =this.globalargsDao.getcontentbyflag("jw_commodity_rate");//重量进位值
			double lweight=Double.parseDouble(lowest_weight_value_flag);
			double jwrate=Double.parseDouble(jw_commodity_rate);
			if(jwrate>0&&jwrate<1&&lweight>0)
			{
				
				try{
					double w=Double.parseDouble(weight);
					if(w>0)
					{
						if(w<=lweight)
						{
							return lowest_weight_value_flag;
						}
						else
						{
							int aa=(int)w;
							if(w-aa>jwrate)
							{
								aa=aa+1;
								return String.valueOf(aa);
								
							}
							else
							{
								return String.valueOf(aa);
								
							}
						}
					}
				}catch(Exception e){
					return weight;
				}
			}
			return weight;
			
		}
		catch(Exception e)
		{
			return weight;
		}
	}
	
	
	//根据类型获取价格
	public String getprice(String type,Commodity_price cobj) throws ServiceException//添加转运预报;
	{
		/* 运费计算 start */
		
		if(cobj==null)
		{
			return null;
		}
		
	//	String priceType = "price";
	
		
		
		if (Constant.USER_TYPE_NORMAL.equals(type)) {// 普通会员发件
			return cobj.getPrice();
			//priceType = "price";
		} else if (Constant.USER_TYPE_VIP0.equals(type)) { // 白银VIP会员
			return cobj.getVip_0_Price();
			//priceType = "vip_0_Price";
		} else if (Constant.USER_TYPE_VIP1.equals(type)) {// 黄金VIP会员
			return cobj.getVip_1_Price();
			//priceType = "vip_1_Price";
		} else if (Constant.USER_TYPE_VIP2.equals(type)) {// 白金VIP会员
			return cobj.getVip_2_Price();
			//priceType = "vip_2_Price";
		} else if (Constant.USER_TYPE_VIP3.equals(type)) {// 钻石VIP会员
			return cobj.getVip_3_Price();
			//priceType = "vip_3_Price";
		}else if (Constant.USER_TYPE_VIP4.equals(type)) {// 至尊VIP1会员
			return cobj.getVip_4_Price();
			//priceType = "vip_4_Price";
		}else if (Constant.USER_TYPE_VIP5.equals(type)) {// 至尊VIP2会员
			return cobj.getVip_5_Price();
			//priceType = "vip_5_Price";
		}else if (Constant.USER_TYPE_VIP6.equals(type)) {// 至尊VIP3会员
			return cobj.getVip_6_Price();
			//priceType = "vip_6_Price";
		}else if (Constant.USER_TYPE_VIP7.equals(type)) {// 至尊VIP4会员
			return cobj.getVip_7_Price();
			//priceType = "vip_7_Price";
		} else { // 找不到就按普通会员的价格计算
			return cobj.getPrice();
			//priceType = "price";
		}
		
	}
	
	
	
			//t_weight--转运包裹重量，i_weight--入库包裹重量，type--用户类型，commid--商品id,cid渠道id,wid---门店id
			public double calculationt_orderFreight_usertype(double t_weight,double i_weight,String type,String commid,String cid,String wid)
					throws ServiceException {
				
				
			
				/* 运费计算 start */
				String priceType = "price";
			
				
				
				if (Constant.USER_TYPE_NORMAL.equals(type)) {// 普通会员发件
					priceType = "price";
				} else if (Constant.USER_TYPE_VIP0.equals(type)) { // 白银VIP会员
					priceType = "vip_0_Price";
				} else if (Constant.USER_TYPE_VIP1.equals(type)) {// 黄金VIP会员
					priceType = "vip_1_Price";
				} else if (Constant.USER_TYPE_VIP2.equals(type)) {// 白金VIP会员
					priceType = "vip_2_Price";
				} else if (Constant.USER_TYPE_VIP3.equals(type)) {// 钻石VIP会员
					priceType = "vip_3_Price";
				}else if (Constant.USER_TYPE_VIP4.equals(type)) {// 至尊VIP1会员
					priceType = "vip_4_Price";
				}else if (Constant.USER_TYPE_VIP5.equals(type)) {// 至尊VIP2会员
					priceType = "vip_5_Price";
				}else if (Constant.USER_TYPE_VIP6.equals(type)) {// 至尊VIP3会员
					priceType = "vip_6_Price";
				}else if (Constant.USER_TYPE_VIP7.equals(type)) {// 至尊VIP4会员
					priceType = "vip_7_Price";
				} else { // 找不到就按普通会员的价格计算
					
					priceType = "price";
				}
				
				
				String price="";
				
					try {
						price=this.commudityPriceDao.getPriceById(priceType, commid);
						
						if (price == null) {
							throw ExceptionUtil
									.handle2ServiceException("运单中含有系统中没有的商品价格类型！");
						}
						
						if(StringUtil.string2Double(price)<=0)//如果价格是0，直接返回0作为判断
						{
							throw ExceptionUtil
							.handle2ServiceException("运单中含有系统中没有的商品价格类型或价格没设置！");
						}

						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						throw ExceptionUtil.handle2ServiceException("计算商品运费时出现异常！");
					}
					double additivePrice=0;
					double tranprice=0;
					try{
						Channel addtivePrice1=this.channelDao.getChannelById(cid);//渠道的附加价格
						 if(!StringUtil.isEmpty(addtivePrice1.getAdditivePrice()))//
						 {
							 additivePrice=StringUtil.string2Double(addtivePrice1.getAdditivePrice());
						 }
						 
						 if(t_weight>0)
							{
							    T_tran_price ttran= this.ttranpriceDao.getoneBystoreId(wid);
							    if(ttran!=null)
							    {
							    	tranprice=Double.parseDouble(ttran.getPrice())*t_weight;
							    }
							}
						 
					}catch(Exception e){
						
					}
				double i_comm=i_weight*Double.parseDouble(price);//商品价格
				
				
			
				

				BigDecimal money = new BigDecimal(i_comm);
				money=money.add(BigDecimal.valueOf(additivePrice));//添加附加价格
				money=money.add(BigDecimal.valueOf(tranprice));//入库重量价格
				
				return money.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

				/* 运费计算 end */

			}
			
	
	public ResponseObject<Object> add_zy_Torder(List<T_order> torders) throws ServiceException
	{
		if(torders==null||torders.size()<1)
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错!");
		}
		int number=0;
		try{
			for(int i=0;i<torders.size();i++)
			{
				if(!StringUtil.isEmpty(torders.get(i).getTorderId()))
				{
					//在插入之前，先判断此用户是否已经预报过了转运单号
					int kk=this.torderDao.countOfftorder(torders.get(i).getUserId(), torders.get(i).getTorderId());
					if(kk>0)
					{
						throw new Exception("转运单号:"+torders.get(i).getTorderId()+"已经存在");
					}
					String date=DateUtil.date2String(new Date());
					torders.get(i).setModifyDate(date);
					torders.get(i).setCreateDate(date);
					int k=this.torderDao.insertyb(torders.get(i));
					if(k<1)
					{
						throw new Exception("插入失败");
					}
					
					//进行路由操作
					String statestr=TorderUtil.transformerState(0, torders.get(i).getState());
					T_route route=new T_route();
					route.setDate(date);
					route.setEmployeeName("");
					route.setRemark("用户预报转运");
					route.setState(statestr);
					route.setT_id(torders.get(i).getId());
					route.setT_orderId(torders.get(i).getTorderId());
					this.trouteDao.insertTroute(route);//插入路由
					
					
					number++;
				}
			}
			return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"成功保存"+number+"条预报信息");
		}
		catch(Exception e){
			//e.printStackTrace();
			return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"操作发生异常:"+e.getMessage());
		}
		
	}
	
	//查找转运单状态
	public ResponseObject<PageSplit<T_order>> search_zy_byuser(String userId,String state,String keyword,int pageSize, int pageNow) throws ServiceException
	{
		if(StringUtil.isEmpty(userId))
		{
			return new ResponseObject<PageSplit<T_order>>(ResponseCode.PARAMETER_ERROR,"参数出错!");
		}
		
	
		if(StringUtil.isEmpty(keyword))
		{
			keyword=null;
		}
		else
		{
			keyword = StringUtil.escapeStringOfSearchKey(keyword);
		}
		try{
			
			int rowCount = 0;
			try {

				rowCount = this.torderDao.countOfSearchTordersbyuser(userId, state, keyword);
			} catch (Exception e) {
				e.printStackTrace();
				throw ExceptionUtil.handle2ServiceException("获取运单个数失败", e);
			}

			ResponseObject<PageSplit<T_order>> responseObj = new ResponseObject<PageSplit<T_order>>(
					ResponseCode.SUCCESS_CODE);
			if (rowCount > 0) {
				pageSize = Math.max(pageSize, 1);
				int pageCount = rowCount / pageSize
						+ (rowCount % pageSize == 0 ? 0 : 1);
				pageNow = Math.min(pageNow, pageCount);
				PageSplit<T_order> pageSplit = new PageSplit<T_order>();
				pageSplit.setPageCount(pageCount);
				pageSplit.setPageNow(pageNow);
				pageSplit.setRowCount(rowCount);
				pageSplit.setPageSize(pageSize);

				int startIndex = (pageNow - 1) * pageSize;
				try {
					List<T_order> orders = this.torderDao.SearchTordersbyuser(userId, state, keyword, startIndex, pageSize);
						
					for(T_order torder:orders)
					{
						if(!StringUtil.isEmpty(torder.getI_storeId()))
						{
							Warehouse ware=this.warehouseDao.getById(torder.getI_storeId());
							if(ware!=null)
							{
								torder.setI_storeName(ware.getName());
							}
						}
						if(!StringUtil.isEmpty(torder.getStoreId()))
						{
						
							Warehouse ware=this.warehouseDao.getById(torder.getStoreId());
							if(ware!=null)
							{
								torder.setStoreName(ware.getName());
							}
							
						}
						if(!StringUtil.isEmpty(torder.getEmployeeId()))
						{
						
							Employee emp=this.employeeDao.getEmployeeById(torder.getEmployeeId());
							if(emp!=null)
							{
								torder.setEmployeeName(emp.getAccount());
							}
							
						}
						if(!StringUtil.isEmpty(torder.getI_employeeId()))
						{
						
							Employee emp=this.employeeDao.getEmployeeById(torder.getI_employeeId());
							if(emp!=null)
							{
								torder.setI_employeeName(emp.getAccount());
							}
							
						}
						
					}
					
					pageSplit.setDatas(orders);
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("获取运单列表失败", e);
				}
				responseObj.setData(pageSplit);
			} else {
				responseObj.setMessage("没有运单");
			}
			return responseObj;
		}
		catch(Exception e){
			//e.printStackTrace();
			return new ResponseObject<PageSplit<T_order>>(ResponseCode.SHOW_EXCEPTION,"操作发生异常:"+e.getMessage());
		}
		
	}
	
	
	
	//用户根据入库门店搜索
	public ResponseObject<PageSplit<T_order>> search_in_byuser(String userId,String state,String i_storeId,String storeId,String keyword,int pageSize, int pageNow) throws ServiceException
		{
			if(StringUtil.isEmpty(userId))
			{
				return new ResponseObject<PageSplit<T_order>>(ResponseCode.PARAMETER_ERROR,"参数出错!");
			}
			
		
			if(StringUtil.isEmpty(keyword))
			{
				keyword=null;
			}
			else
			{
				keyword = StringUtil.escapeStringOfSearchKey(keyword);
			}
			try{
				
				int rowCount = 0;
				try {

					rowCount = this.torderDao.countOfSearchTordersbyuser1(userId, state, keyword, storeId, i_storeId);
						//	.countOfSearchTordersbyuser(userId, state, keyword);
				} catch (Exception e) {
					e.printStackTrace();
					throw ExceptionUtil.handle2ServiceException("获取运单个数失败", e);
				}

				ResponseObject<PageSplit<T_order>> responseObj = new ResponseObject<PageSplit<T_order>>(
						ResponseCode.SUCCESS_CODE);
				if (rowCount > 0) {
					pageSize = Math.max(pageSize, 1);
					int pageCount = rowCount / pageSize
							+ (rowCount % pageSize == 0 ? 0 : 1);
					pageNow = Math.min(pageNow, pageCount);
					PageSplit<T_order> pageSplit = new PageSplit<T_order>();
					pageSplit.setPageCount(pageCount);
					pageSplit.setPageNow(pageNow);
					pageSplit.setRowCount(rowCount);
					pageSplit.setPageSize(pageSize);

					int startIndex = (pageNow - 1) * pageSize;
					try {
						List<T_order> orders = this.torderDao.SearchTordersbyuser1(userId, state, keyword, storeId, i_storeId, startIndex, pageSize);
								//.SearchTordersbyuser(userId, state, keyword, startIndex, pageSize);
							
						for(T_order torder:orders)
						{
							if(!StringUtil.isEmpty(torder.getI_storeId()))
							{
								Warehouse ware=this.warehouseDao.getById(torder.getI_storeId());
								if(ware!=null)
								{
									torder.setI_storeName(ware.getName());
								}
							}
							if(!StringUtil.isEmpty(torder.getStoreId()))
							{
							
								Warehouse ware=this.warehouseDao.getById(torder.getStoreId());
								if(ware!=null)
								{
									torder.setStoreName(ware.getName());
								}
								
							}
							if(!StringUtil.isEmpty(torder.getEmployeeId()))
							{
							
								Employee emp=this.employeeDao.getEmployeeById(torder.getEmployeeId());
								if(emp!=null)
								{
									torder.setEmployeeName(emp.getAccount());
								}
								
							}
							if(!StringUtil.isEmpty(torder.getI_employeeId()))
							{
							
								Employee emp=this.employeeDao.getEmployeeById(torder.getI_employeeId());
								if(emp!=null)
								{
									torder.setI_employeeName(emp.getAccount());
								}
								
							}
							
						}
						
						pageSplit.setDatas(orders);
					} catch (Exception e) {
						throw ExceptionUtil.handle2ServiceException("获取运单列表失败", e);
					}
					responseObj.setData(pageSplit);
				} else {
					responseObj.setMessage("没有运单");
				}
				return responseObj;
			}
			catch(Exception e){
				//e.printStackTrace();
				return new ResponseObject<PageSplit<T_order>>(ResponseCode.SHOW_EXCEPTION,"操作发生异常:"+e.getMessage());
			}
			
		}
	//删除一条转运单停下
	public ResponseObject<Object> deleteone(String userId,String id) throws ServiceException
	{
		
		if(StringUtil.isEmpty(id))
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错!");
		}
		
		try{
			
			int k=this.torderDao.deleteone(userId, id);
			if(k<1)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"删除失败!");
			}
			//删除原有路由信息
			List<String> list=new ArrayList<String>();
			list.add(id);
			int kk=this.trouteDao.deleteRouteByTids(list);
			return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"删除成功!");
		}
		catch(Exception e){
			//e.printStackTrace();
			return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"操作发生异常");
		}
	}
			
	
	//获取一条转运单
		public ResponseObject<Object> getone(String userId,String id) throws ServiceException
		{
			
			if(StringUtil.isEmpty(id))
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错!");
			}
			
			try{
				
				T_order torder=this.torderDao.getone(userId, id);
				if(null==torder)
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取失败!");
				}
				
				if(!StringUtil.isEmpty(torder.getI_storeId()))
				{
					Warehouse ware=this.warehouseDao.getById(torder.getI_storeId());
					if(ware!=null)
					{
						torder.setI_storeName(ware.getName());
					}
				}
				if(!StringUtil.isEmpty(torder.getStoreId()))
				{
				
					Warehouse ware=this.warehouseDao.getById(torder.getStoreId());
					if(ware!=null)
					{
						torder.setStoreName(ware.getName());
					}
					
				}
				if(!StringUtil.isEmpty(torder.getEmployeeId()))
				{
				
					Employee emp=this.employeeDao.getEmployeeById(torder.getEmployeeId());
					if(emp!=null)
					{
						torder.setEmployeeName(emp.getAccount());
					}
					
				}
				if(!StringUtil.isEmpty(torder.getI_employeeId()))
				{
				
					Employee emp=this.employeeDao.getEmployeeById(torder.getI_employeeId());
					if(emp!=null)
					{
						torder.setI_employeeName(emp.getAccount());
					}
					
				}
				
				ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"获取成功!");
				obj.setData(torder);
				return obj;
			}
			catch(Exception e){
				//e.printStackTrace();
				return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"操作发生异常");
			}
		}
		
		//获取一条转运单的路由
		public ResponseObject<Object> getoneroute(String userId,String id) throws ServiceException
		{
			
			if(StringUtil.isEmpty(id))
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错!");
			}
			
			try{
				
				List<T_route> route=this.trouteDao.getRouteByTid(id);
				if(null==route)
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取失败!");
				}
				ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"获取成功!");
				obj.setData(route);
				return obj;
			}
			catch(Exception e){
				//e.printStackTrace();
				return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"操作发生异常");
			}
		}
		
		//获取转运单号
		public ResponseObject<Object> getoneyb(String torderId) throws ServiceException//获取一条预报信息
		{
			try{
				List<T_order> list =this.torderDao.gettorders(null, Constant.T_ORDER_STATE0, torderId);
				if(list==null||list.size()==0)
				{
					//可能在其它地方入库
					list =this.torderDao.gettorders(null, null, torderId);
					if((list==null)||list.size()==0)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"转运单号不存在!");
					}
					else
					{
						if(list.size()==1)
						{
							if(StringUtil.isEmpty(list.get(0).getStoreId()))
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"预报单号中不存在，但是库存中存在1个相同转运单号");
							}
							else
							{
								Warehouse ware=this.warehouseDao.getById(list.get(0).getStoreId());
								String StoreName="";
								if(ware!=null)
								{
									StoreName=ware.getName();
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"预报单号中不存在，但是仓库："+StoreName+"中存在1个相同单号");
								}
								if(StringUtil.isEmpty(StoreName))
								{
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"预报单号中不存在，但是库存中存在1个相同转运单号");
								}
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"转运单已入库，入库门店"+list.get(0).getStoreName());
							}
						}
						else
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"没有预报单号，但库中有"+list.size()+"个相同转运单号，请检查!");
						}
					}
				}
				else if(list.size()>1)
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"预报单号超过两个，请检查!");
				}
				else
				{
					ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"获取成功!");
					obj.setData(list.get(0));
					return obj;
				}
					
			}catch(Exception e)
			{
				return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"操作发生异常");
			}
		}
		
		
		
		//转运州入库接口
		public ResponseObject<Object> zyrkprocess(String id,String torderId,String storeId,String empId,String userId,String weight,String shelvesId) throws ServiceException
		{
			try{
				String routeremark="";
				T_order torder=null;
				if(!StringUtil.isEmpty(id))
				{
					torder=this.torderDao.getone(null, id);
					if(torder==null)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"没有找到转运单!");
					}
					if(!torderId.equalsIgnoreCase(torder.getTorderId()))
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取到的转运单号不匹配!");
					}
				}
				User user=this.userDao.getUserById(userId);
				if(user==null)
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"归属用户不存在!");
				}
				
				
				
				//如果转运不存在，要建立一个预报单号
				String date=DateUtil.date2String(new Date());
				if(StringUtil.isEmpty(id))
				{
					torder=new T_order();
					routeremark="后台预报";
					torder.setTorderId(torderId);//设置转运单号
					torder.setRemark("");//设置商品备注信息
					torder.setUserId(userId);
					torder.setState(Constant.T_ORDER_STATE0);//用户预报状态
					torder.setType(Constant.T_ORDER_TYPE1);//转运州入库
					
					
					torder.setModifyDate(date);
					torder.setCreateDate(date);
					
					int k=this.torderDao.insertyb(torder);
					
					
					
					
					
					
					
					/*//进行路由操作
					String statestr=TorderUtil.transformerState(0, torders.get(i).getState());
					T_route route=new T_route();
					route.setDate(date);
					route.setEmployeeName("");
					route.setRemark("用户预报转运");
					route.setState(statestr);
					route.setT_id(torders.get(i).getId());
					route.setT_orderId(torders.get(i).getTorderId());
					route.setEmployeeName(employeeName);
					this.trouteDao.insertTroute(route);//插入路由
					
					
					number++;*/
					
				}
				
				
				//获取仓位信息
				
				Shelves_position position=null;
				if(!StringUtil.isEmpty(shelvesId))
				{
					position=this.shelves_positionDao.getone(shelvesId);//获取仓位信息
					if(position==null)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取仓位失败，检查此货架是否存在或仓位是否已经用完!");
					}
					String remark="转运单号："+torder.getTorderId();
					int kk=this.shelves_positionDao.updatestate(position.getId(), Constant.POSITION_STATE1, date, remark);
					if(kk==0)
					{
						throw new Exception("修改仓位状态发生异常！");
					}
					
					
				}
				
				String empName="";
				String storeName="";
				Employee emp=this.employeeDao.getEmployeeById(empId);
				if(emp!=null)
				{
					empName=emp.getAccount();
				}
				Warehouse ware=this.warehouseDao.getById(storeId);
				if(ware!=null)
				{
					storeName=ware.getName();
				}
				torder.setEmployeeId(empId);
				
				torder.setModifyDate(date);
				
				
				
				
				
				torder.setWeight(getjwweight(weight));
				torder.setSjweight(weight);
				torder.setStoreId(storeId);
				torder.setState(Constant.T_ORDER_STATE3);//转运入库
				torder.setType(Constant.T_ORDER_TYPE1);//转运州入库
				if(position!=null)
				{
					torder.setPositionId(position.getId());
				}
				
				//修改预报状态及入库信息
				int k=this.torderDao.modify_zy_torder(torder);
				if(k==0)
				{
					throw new Exception("修改转运单失败！");
				}
				
				
				
				
				
				//添加路由变更
				String statestr=TorderUtil.transformerState(0, torder.getState());
				T_route route=new T_route();
				route.setDate(date);
				route.setEmployeeName("");
				if(StringUtil.isEmpty(routeremark))
				{
					route.setRemark("转运州入库，仓库名称："+storeName);
				}
				else
				{
					route.setRemark(routeremark+",转运州入库，仓库名称："+storeName);
				}
				route.setState(statestr);
				route.setT_id(torder.getId());
				route.setT_orderId(torder.getTorderId());
				route.setEmployeeName(empName);//操作人名称
				this.trouteDao.insertTroute(route);//插入路由
				if(position!=null)
				{
					
					return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,position.getPosition());
				}
				else
				{
					return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"");
				}
			}catch(Exception e){
				return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"操作发生异常："+e.getMessage());
			}
		}
		
		//修改转运单的状态
		public ResponseObject<Object> modifystate(String torderId,String state,List<String> pre_state,String storeId,String empId,String tremark) throws ServiceException
		{
			String date=DateUtil.date2String(new Date());
			
			
			
			
			
			try{
				
				List<T_order> torders=this.torderDao.gettordersbyprestate(storeId, pre_state, torderId);
				
				//int k=this.torderDao.modify_state(date, storeId, state, pre_state, torderId);
				if(torders.size()==0)
				{
					List<T_order> torder=this.torderDao.gettorders(storeId, null, torderId);
					if(torder==null||torder.size()<1)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"修改失败，请检查是否属于本门店或状态是否正确或是否存在！");
					}
					else if(torder.size()==1)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"修改失败，但库存中存在1个想同转运单，状态是："+TorderUtil.transformerState(0, torder.get(0).getState()));
					}
					else 
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"修改失败，但库存中存在"+torder.size()+"个想同转运单，请检查!");
					}
				}
				else if(torders.size()>1)
				{
					throw new Exception("运单中存在"+torders.size()+"个相同的转运单号，请检查!");
				}
				else
				{
					
					String wremark="";
					int k=this.torderDao.modifystatebyId(state, torders.get(0).getId(), date,null,tremark);
					if(k==0)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"修改状态失败！");
					}
					if(!StringUtil.isEmpty(torders.get(0).getPositionId()))//表示原来有仓位，要清空仓位
					{
						//转运出库,已完成,已自提都要清空仓位
						if(state.equalsIgnoreCase(Constant.T_ORDER_STATE4)||state.equalsIgnoreCase(Constant.T_ORDER_STATE6)||state.equalsIgnoreCase(Constant.T_ORDER_STATE7))
						{
							Shelves_position position1=this.shelves_positionDao.getbyid(torders.get(0).getPositionId());
							if(position1!=null)
							{
								wremark="，清空仓位"+position1.getPosition();
							}
							this.shelves_positionDao.updatestate(torders.get(0).getPositionId(), Constant.POSITION_STATE0, date, "");
							//清空占用的他们
							this.torderDao.clear_positionId(torders.get(0).getPositionId(), date);
						}
					}
					
					
					
					
					//添加路由变更
					String statestr=TorderUtil.transformerState(0, state);
					T_route route=new T_route();
					route.setDate(date);
					route.setEmployeeName("");
					String empName="";
					if(!StringUtil.isEmpty(empId))
					{
						Employee ware=this.employeeDao.getEmployeeById(empId);
						if(ware!=null)
						{
							empName=ware.getAccount();
						}
					}
					
					
					
					route.setRemark("修改状态");
					route.setState(statestr);
					route.setT_id(torders.get(0).getId());
					route.setT_orderId(torders.get(0).getTorderId());
					route.setEmployeeName(empName);//操作人名称
					this.trouteDao.insertTroute(route);//插入路由
					
					
					ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
					obj.setMessage("修改成功"+wremark);
					return obj;
				}
			}catch(Exception e)
			{
				return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"修改状态发生异常!"+e.getMessage());
			}
		}
		
		
		public ResponseObject<Object> getonebystore(String torderId,List<String> states,String storeId,String type) throws ServiceException//获取门店下的一条预报运单
		{
			if(StringUtil.isEmpty(torderId))
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错");
			}
			try{
				List<T_order> torders=this.torderDao.gettordersbystore(storeId, states, torderId, type);
				if((torders==null)||(torders.size()==0))
				{
					List<T_order> torders1=this.torderDao.gettordersbystore(null, null, torderId, null);
					if(torders1==null||torders1.size()==0)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"无此转运单号！");
					}
					else if(torders1.size()==1)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取运单号失败，但存在一个转运单并且状态为"+TorderUtil.transformerState(0, torders1.get(0).getState()));
					}
					else
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取运单号失败，但系统存在"+torders1.size()+"个相同运单号！");
					}
				}
				else if(torders.size()>1)
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"同等条件下查找到"+torders.size()+"个转运单，请检查!");
				}
				else
				{
					ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
					obj.setData(torders.get(0));
					return obj;
				}
			}catch(Exception e)
			{
				return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"获取发生异常！");
			}
		}
		
		
		
		//本地州入库接口
		public ResponseObject<Object> localkprocess(String id,String torderId,String storeId,String empId,String userId,String weight,String shelvesId) throws ServiceException
		{
			try{
				String routeremark="";
				T_order torder=null;
				if(!StringUtil.isEmpty(id))
				{
					torder=this.torderDao.getone(null, id);
					if(torder==null)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"没有找到转运单!");
					}
					if(!torderId.equalsIgnoreCase(torder.getTorderId()))
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取到的转运单号不匹配!");
					}
				}
				User user=this.userDao.getUserById(userId);
				if(user==null)
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"归属用户不存在!");
				}
				
				//获取仓位信息
				String date=DateUtil.date2String(new Date());
				Shelves_position position=null;
				if(!StringUtil.isEmpty(shelvesId))
				{
					position=this.shelves_positionDao.getone(shelvesId);//获取仓位信息
					if(position==null)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取仓位失败，检查此货架是否存在或仓位是否已经用完!");
					}
					String remark="转运单号："+torder.getTorderId();
					int kk=this.shelves_positionDao.updatestate(position.getId(), Constant.POSITION_STATE1, date, remark);
					if(kk==0)
					{
						throw new Exception("修改仓位状态发生异常！");
					}
					
					
				}
				
				//如果转运不存在，要建立一个预报单号
				
				if(StringUtil.isEmpty(id))
				{
					
					routeremark="后台预报";
					torder.setTorderId(torderId);//设置转运单号
					torder.setRemark("");//设置商品备注信息
					torder.setUserId(userId);
					torder.setState(Constant.T_ORDER_STATE0);//用户预报状态
					torder.setType(Constant.T_ORDER_TYPE0);//本地州入库
					
					
					torder.setModifyDate(date);
					torder.setCreateDate(date);
					
					int k=this.torderDao.insertyb(torder);
					if(k==0)
					{
						throw new Exception("插入预报单号失败！");
					}
					id=torder.getId();
					
					if(position!=null)
					{
						torder.setPositionId(position.getId());//设置货架的id
					}
					
					
					
					
					
					
					/*//进行路由操作
					String statestr=TorderUtil.transformerState(0, torders.get(i).getState());
					T_route route=new T_route();
					route.setDate(date);
					route.setEmployeeName("");
					route.setRemark("用户预报转运");
					route.setState(statestr);
					route.setT_id(torders.get(i).getId());
					route.setT_orderId(torders.get(i).getTorderId());
					route.setEmployeeName(employeeName);
					this.trouteDao.insertTroute(route);//插入路由
					
					
					number++;*/
					
				}
				String empName="";
				String storeName="";
				Employee emp=this.employeeDao.getEmployeeById(empId);
				if(emp!=null)
				{
					empName=emp.getAccount();
				}
				Warehouse ware=this.warehouseDao.getById(storeId);
				if(ware!=null)
				{
					storeName=ware.getName();
				}
				torder.setEmployeeId(empId);
				
				torder.setModifyDate(date);
				
				
				
				
				
				torder.setWeight(getjwweight(weight));
				torder.setSjweight(weight);
				torder.setStoreId(storeId);
				torder.setState(Constant.T_ORDER_STATE5);//本地州入库
				torder.setType(Constant.T_ORDER_TYPE0);//转运州入库
				
				
				torder.setI_date(date);
				torder.setI_employeeId(empId);
				torder.setI_jwweight(getjwweight(weight));
				torder.setI_weight(weight);
				torder.setI_state(Constant.T_ORDER_I_STATE1);
				torder.setI_storeId(storeId);
				
				if(position!=null)
				{
					torder.setPositionId(position.getId());
				}
				
				//修改预报状态及入库信息
				//int k=this.torderDao.modify_zy_torder(torder);
				int k=this.torderDao.modify_local_in_torder(torder);
				if(k==0)
				{
					throw new Exception("修改转运单失败！");
				}
				//添加路由变更
				String statestr=TorderUtil.transformerState(0, torder.getState());
				T_route route=new T_route();
				route.setDate(date);
				route.setEmployeeName("");
				if(StringUtil.isEmpty(routeremark))
				{
					route.setRemark("转运入库，仓库名称："+storeName);
				}
				else
				{
					route.setRemark(routeremark+",转运入库，仓库名称："+storeName);
				}
				route.setState(statestr);
				route.setT_id(torder.getId());
				route.setT_orderId(torder.getTorderId());
				route.setEmployeeName(empName);//操作人名称
				this.trouteDao.insertTroute(route);//插入路由
				if(position!=null)
				{
					
					return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,position.getPosition());
				}
				else
				{
					return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"");
				}
			}catch(Exception e){
				return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"操作发生异常："+e.getMessage());
			}
		}
		
		
		
		
		
		//本地州入库接口
		public ResponseObject<Object> zytolocalkprocess(String id,String torderId,String storeId,String i_storeId,String empId,String userId,String weight,String shelvesId) throws ServiceException
		{
			try{
				String routeremark="";
				T_order torder=null;
				if(!StringUtil.isEmpty(id))
				{
					torder=this.torderDao.getone(null, id);
					if(torder==null)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"没有找到转运单!");
					}
					if(!torderId.equalsIgnoreCase(torder.getTorderId()))
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取到的转运单号不匹配!");
					}
				}
				User user=this.userDao.getUserById(userId);
				if(user==null)
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"归属用户不存在!");
				}
				
				//获取仓位信息
				String date=DateUtil.date2String(new Date());
				Shelves_position position=null;
				if(!StringUtil.isEmpty(shelvesId))
				{
					position=this.shelves_positionDao.getone(shelvesId);//获取仓位信息
					if(position==null)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取仓位失败，检查此货架是否存在或仓位是否已经用完!");
					}
					String remark="转运单号："+torder.getTorderId();
					int kk=this.shelves_positionDao.updatestate(position.getId(), Constant.POSITION_STATE1, date, remark);
					if(kk==0)
					{
						throw new Exception("修改仓位状态发生异常！");
					}
					
					
				}
				
				//如果转运不存在，要建立一个预报单号
				
				if(StringUtil.isEmpty(id))
				{
					
					routeremark="后台预报";
					torder.setTorderId(torderId);//设置转运单号
					torder.setRemark("");//设置商品备注信息
					torder.setUserId(userId);
					torder.setState(Constant.T_ORDER_STATE0);//用户预报状态
					torder.setType(Constant.T_ORDER_TYPE1);//转运州入库
					
					
					torder.setModifyDate(date);
					torder.setCreateDate(date);
					
					int k=this.torderDao.insertyb(torder);
					if(k==0)
					{
						throw new Exception("插入预报单号失败！");
					}
					id=torder.getId();
					
					if(position!=null)
					{
						torder.setPositionId(position.getId());//设置货架的id
					}
					
					
					
					
					
					
					/*//进行路由操作
					String statestr=TorderUtil.transformerState(0, torders.get(i).getState());
					T_route route=new T_route();
					route.setDate(date);
					route.setEmployeeName("");
					route.setRemark("用户预报转运");
					route.setState(statestr);
					route.setT_id(torders.get(i).getId());
					route.setT_orderId(torders.get(i).getTorderId());
					route.setEmployeeName(employeeName);
					this.trouteDao.insertTroute(route);//插入路由
					
					
					number++;*/
					
				}
				String empName="";
				String storeName="";
				Employee emp=this.employeeDao.getEmployeeById(empId);
				if(emp!=null)
				{
					empName=emp.getAccount();
				}
				Warehouse ware=this.warehouseDao.getById(i_storeId);
				if(ware!=null)
				{
					storeName=ware.getName();
				}
				torder.setEmployeeId(empId);
				
				torder.setModifyDate(date);
				
				//如果之前有仓位，要清空原来仓位
				
				if(!StringUtil.isEmpty(torder.getPositionId()))
				{
					this.shelves_positionDao.updatestate(torder.getPositionId(), Constant.POSITION_STATE0, date, "");//清空原有仓位
				}
	
				
				torder.setI_date(date);
				torder.setI_employeeId(empId);
				torder.setI_jwweight(getjwweight(weight));
				torder.setI_weight(weight);
				torder.setI_state(Constant.T_ORDER_I_STATE1);
				torder.setI_storeId(i_storeId);
				
				torder.setWeight(getjwweight(weight));
				torder.setSjweight(weight);
				torder.setStoreId(storeId);
				torder.setState(Constant.T_ORDER_STATE5);//本地州入库
				//torder.setType(Constant.T_ORDER_TYPE0);//转运州入库
				if(position!=null)
				{
					torder.setPositionId(position.getId());
				}
				
				//修改预报状态及入库信息
				//int k=this.torderDao.modify_zy_torder(torder);
				int k=this.torderDao.modify_local_in_torder(torder);
				if(k==0)
				{
					throw new Exception("修改转运单失败！");
				}
				//添加路由变更
				String statestr=TorderUtil.transformerState(0, torder.getState());
				T_route route=new T_route();
				route.setDate(date);
				route.setEmployeeName("");
				if(StringUtil.isEmpty(routeremark))
				{
					route.setRemark("转运入库，仓库名称："+storeName);
				}
				else
				{
					route.setRemark(routeremark+",转运入库，仓库名称："+storeName);
				}
				route.setState(statestr);
				route.setT_id(torder.getId());
				route.setT_orderId(torder.getTorderId());
				route.setEmployeeName(empName);//操作人名称
				this.trouteDao.insertTroute(route);//插入路由
				if(position!=null)
				{
					
					return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,position.getPosition());
				}
				else
				{
					return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"");
				}
			}catch(Exception e){
				return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"操作发生异常："+e.getMessage());
			}
		}
		
		//转运州入库失败
		public ResponseObject<Object> set_fail_torder(String torderId,String remark,String shelvesId,String qremark,String storeId,String empId,String weight)throws ServiceException//转运州入库失败
		{

			if(StringUtil.isEmpty(torderId))
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数错误");
			}
			try{
				
				int n=this.torderDao.countOfftorder(null, torderId);
				if(n>0)
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"系统中已经存在此存运单号!请检查!");
				}
				
				String date=DateUtil.date2String(new Date());
				Shelves_position position=null;
				if(!StringUtil.isEmpty(shelvesId))
				{
					position=this.shelves_positionDao.getone(shelvesId);//获取仓位信息
					if(position==null)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取仓位失败，检查此货架是否存在或仓位是否已经用完!");
					}
					String remark1=qremark+",转运单号："+torderId;
					int kk=this.shelves_positionDao.updatestate(position.getId(), Constant.POSITION_STATE1, date, remark1);
					if(kk==0)
					{
						throw new Exception("修改仓位状态发生异常！");
					}
					
					
				}
				
				
				
				
				T_order torder= new T_order();
				torder.setTorderId(torderId);
				torder.setRemark(remark);
				torder.setQremark(qremark);
				torder.setStoreId(storeId);
				torder.setEmployeeId(empId);
				torder.setWeight(getjwweight(weight));
				torder.setSjweight(weight);
				torder.setCreateDate(date);
				torder.setModifyDate(date);
				torder.setType(Constant.T_ORDER_TYPE1);//标识转运州的
				torder.setState(Constant.T_ORDER_STATE1);//录入失败 
				torder.setUserId("0");//暂无归属用户
				if(position!=null)
				{
					torder.setPositionId(position.getId());
				}
				
				int k=this.torderDao.insertfail(torder);
				if(k<1)
				{
					throw new Exception("插入失败");
				}
				String empName="";
				if(!StringUtil.isEmpty(empId))
				{
					Employee emp=this.employeeDao.getEmployeeById(empId);
					if(emp!=null)
					{
						empName=emp.getAccount();
					}
				}
				String storeName="";
				if(!StringUtil.isEmpty(storeId))
				{
					Warehouse ware=this.warehouseDao.getById(storeId);
					if(ware!=null)
					{
						storeName=ware.getName();
					}
					
				}
				//进行路由操作
				String statestr=TorderUtil.transformerState(0, torder.getState());
				T_route route=new T_route();
				route.setDate(date);
				route.setEmployeeName(empName);
				route.setRemark("后台标志录入失败!录入仓库："+storeName);
				route.setState(statestr);
				route.setT_id(torder.getId());
				route.setT_orderId(torder.getTorderId());
				
				this.trouteDao.insertTroute(route);//插入路由
				ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
				
				if(position!=null)
				{
					
					obj.setMessage(position.getPosition());
				}
			
				return obj;
			}
			catch(Exception e){
				//e.printStackTrace();
				return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"操作发生异常:"+e.getMessage());
			}
			
		
			
		}
		
		
		//转运州包裹进入本地失败，入失败运单
		public ResponseObject<Object> set_zyinlogcal_fail_torder(String torderId,String remark,String shelvesId,String qremark,String storeId,String i_storeId,String empId,String weight)throws ServiceException
		{


			if(StringUtil.isEmpty(torderId))
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数错误");
			}
			try{
				
				int n=this.torderDao.countOfftorder(null, torderId);
				if(n>0)
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"系统中已经存在此存运单号!请检查!");
				}
				
				String date=DateUtil.date2String(new Date());
				Shelves_position position=null;
				if(!StringUtil.isEmpty(shelvesId))
				{
					position=this.shelves_positionDao.getone(shelvesId);//获取仓位信息
					if(position==null)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取仓位失败，检查此货架是否存在或仓位是否已经用完!");
					}
					String remark1=qremark+",转运单号："+torderId;
					int kk=this.shelves_positionDao.updatestate(position.getId(), Constant.POSITION_STATE1, date, remark1);
					if(kk==0)
					{
						throw new Exception("修改仓位状态发生异常！");
					}
					
					
				}
				
				
				
				
				
				
				T_order torder= new T_order();
				torder.setTorderId(torderId);
				torder.setRemark(remark);
				torder.setQremark(qremark);
				torder.setStoreId(storeId);
				torder.setEmployeeId(empId);
				
				torder.setCreateDate(date);
				torder.setModifyDate(date);
				torder.setType(Constant.T_ORDER_TYPE1);//标识转运州的
				torder.setState(Constant.T_ORDER_STATE1);//录入失败 
				torder.setUserId("0");//暂无归属用户
				
				
				
				torder.setI_employeeId(empId);
				torder.setI_jwweight(getjwweight(weight));
				torder.setI_weight(weight);
				torder.setI_state(Constant.T_ORDER_I_STATE0);
				torder.setI_storeId(i_storeId);
				
				torder.setWeight(getjwweight(weight));
				torder.setSjweight(weight);
				
				
				
				
				if(position!=null)
				{
					torder.setPositionId(position.getId());
				}
				
				int k=this.torderDao.insert_zyinlogcal_fail_torder(torder);
						//insertfail(torder);
				if(k<1)
				{
					throw new Exception("插入失败");
				}
				String empName="";
				if(!StringUtil.isEmpty(empId))
				{
					Employee emp=this.employeeDao.getEmployeeById(empId);
					if(emp!=null)
					{
						empName=emp.getAccount();
					}
				}
				String storeName="";
				if(!StringUtil.isEmpty(storeId))
				{
					Warehouse ware=this.warehouseDao.getById(i_storeId);
					if(ware!=null)
					{
						storeName=ware.getName();
					}
					
				}
				//进行路由操作
				String statestr=TorderUtil.transformerState(0, torder.getState());
				T_route route=new T_route();
				route.setDate(date);
				route.setEmployeeName(empName);
				route.setRemark("后台标志录入失败!录入仓库："+storeName);
				route.setState(statestr);
				route.setT_id(torder.getId());
				route.setT_orderId(torder.getTorderId());
				
				this.trouteDao.insertTroute(route);//插入路由
				ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
				
				if(position!=null)
				{
					
					obj.setMessage(position.getPosition());
				}
			
				return obj;
			}
			catch(Exception e){
				//e.printStackTrace();
				return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"操作发生异常:"+e.getMessage());
			}
			
		
			
		
		}
		
		
		
		//本地州直接入库失败运单
		public ResponseObject<Object> set_local_fail_torder(String torderId,String remark,String shelvesId,String qremark,String i_storeId,String empId,String weight)throws ServiceException//转运州入库失败
		{

			if(StringUtil.isEmpty(torderId))
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数错误");
			}
			try{
				
				int n=this.torderDao.countOfftorder(null, torderId);
				if(n>0)
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"系统中已经存在此存运单号!请检查!");
				}
				
				String date=DateUtil.date2String(new Date());
				Shelves_position position=null;
				if(!StringUtil.isEmpty(shelvesId))
				{
					position=this.shelves_positionDao.getone(shelvesId);//获取仓位信息
					if(position==null)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取仓位失败，检查此货架是否存在或仓位是否已经用完!");
					}
					String remark1=qremark+",转运单号："+torderId;
					int kk=this.shelves_positionDao.updatestate(position.getId(), Constant.POSITION_STATE1, date, remark1);
					if(kk==0)
					{
						throw new Exception("修改仓位状态发生异常！");
					}
					
					
				}
				
				
				
				
				T_order torder= new T_order();
				
				
	//torder.setEmployeeId(empId);
				
				torder.setModifyDate(date);
				
				//如果之前有仓位，要清空原来仓位
				
				if(!StringUtil.isEmpty(torder.getPositionId()))
				{
					this.shelves_positionDao.updatestate(torder.getPositionId(), Constant.POSITION_STATE0, date, "");//清空原有仓位
				}
	
				
				torder.setI_date(date);
				torder.setI_employeeId(empId);
				torder.setI_jwweight(getjwweight(weight));
				torder.setI_weight(weight);
				torder.setI_state(Constant.T_ORDER_I_STATE1);
				torder.setI_storeId(i_storeId);
				
				torder.setWeight(getjwweight(weight));
				torder.setSjweight(weight);
				torder.setStoreId("0");
				//torder.setState(Constant.T_ORDER_ROUTE_STATE1);//本地州入库
				//torder.setType(Constant.T_ORDER_TYPE0);//转运州入库
				if(position!=null)
				{
					torder.setPositionId(position.getId());
				}
				
				
				torder.setTorderId(torderId);
				torder.setRemark(remark);
				torder.setQremark(qremark);



				torder.setCreateDate(date);
				torder.setModifyDate(date);
				torder.setType(Constant.T_ORDER_TYPE0);//标识本地州
				torder.setState(Constant.T_ORDER_STATE1);//录入失败 
				torder.setUserId("0");//暂无归属用户
				if(position!=null)
				{
					torder.setPositionId(position.getId());
				}
				
				int k=this.torderDao.insert_local_fail_torder(torder);//.insertfail(torder);
				
				if(k<1)
				{
					throw new Exception("插入失败");
				}
				String empName="";
				if(!StringUtil.isEmpty(empId))
				{
					Employee emp=this.employeeDao.getEmployeeById(empId);
					if(emp!=null)
					{
						empName=emp.getAccount();
					}
				}
				String storeName="";
				if(!StringUtil.isEmpty(i_storeId))
				{
					Warehouse ware=this.warehouseDao.getById(i_storeId);
					if(ware!=null)
					{
						storeName=ware.getName();
					}
					
				}
				//进行路由操作
				String statestr=TorderUtil.transformerState(0, torder.getState());
				T_route route=new T_route();
				route.setDate(date);
				route.setEmployeeName(empName);
				route.setRemark("后台标志录入失败!录入仓库："+storeName);
				route.setState(statestr);
				route.setT_id(torder.getId());
				route.setT_orderId(torder.getTorderId());
				
				this.trouteDao.insertTroute(route);//插入路由
				ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
				
				if(position!=null)
				{
					
					obj.setMessage(position.getPosition());
				}
			
				return obj;
			}
			catch(Exception e){
				e.printStackTrace();
				return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"操作发生异常:"+e.getMessage());
			}
			
		
			
		}
		
		
		
		
		//添加用户提交上来的转运单并生成运单
		public ResponseObject<Object> add_tran_Morder(List<M_order> orders) throws ServiceException
		//public ResponseObject<Object> add_tran_Morder(M_order order) throws ServiceException
		{
			
			if(orders==null||orders.size()==0)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错!");
			}
			try {
				List<String> tids=null;
				int number=0;
				String orderIds="";
				for(M_order morder:orders)	
				{
					tids=morder.getTorderIds();
					number++;
					String date = DateUtil.date2String(new Date());
					morder.setCreateDate(date);
					morder.setModifyDate(date);
					morder.setPayornot(Constant.ORDER_PAY_STATE0);//状态为未付款
					morder.setState(Constant.ORDER_STATE__9);//设置状态为转动置单
					morder.setType(Constant.ORDER_TYPE_KEY_2);
					
					if(morder.getRuser()!=null)
					{
						morder.getRuser().setCreateDate(date);
						morder.getRuser().setModifyDate(date);
						morder.getRuser().setUserId(morder.getUserId());
						morder.getRuser().setCardid_flag(Constant.VERFY_CARDID__10);//设置不时行检验
						
						
						int k=this.receive_UserDao.insertReceiveUser(morder.getRuser());//插入收件人信息
						  if(k>0)
						  {
							  morder.setRuserId(morder.getRuser().getId());
						  }
						  else
						  {
							  throw new Exception("插入收件人用户失败");
						  }
						  
						
					}
					else
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"收件人为空!");
					}
					if(morder.getSuser()!=null)
					{
						morder.getSuser().setCreateDate(date);
						morder.getSuser().setModifyDate(date);
						morder.getSuser().setUserId(morder.getUserId());
					  int k=this.send_UserDao.insertSendUser(morder.getSuser());//插入发件人信息
					  if(k>0)
					  {
						  morder.setSuserId(morder.getSuser().getId());
					  }
					  else
					  {
						  throw new Exception("插入发件人用户失败");
					  }
					  
					 
					
					}
					else
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"发件人为空!");
					}
					
					
					
					
					
					morder.setPayornot(Constant.ORDER_PAY_STATE0);//设置为未付款状态，1表示已经付款，其它表示付款异常状态
					int k=this.m_orderDao.insertMorder(morder);
					if(k==0)
					{
						throw new Exception("插入运单失败");
					}
					//String orderid=this.createOrderIdarg(order.getId(),order.getStoreId(),order.getType());//获取运单的运单号
					String orderid="";
					if(StringUtil.isEmpty(morder.getUserId())||(morder.getUserId().equalsIgnoreCase("-1")))
					{
						//orderid=this.createOrderIdargnew(order.getId(),order.getStoreId(),order.getType(),false);//获取运单的运单号
						orderid=this.m_orderService.createOrderIdargnewMT(morder.getId(),morder.getStoreId(),morder.getType());//获取美淘运单的运单号
					}
					else
					{
						//orderid=this.createOrderIdargnew(order.getId(),order.getStoreId(),order.getType(),true);//获取运单的运单号
						orderid=this.m_orderService.createOrderIdargnewMT(morder.getId(),morder.getStoreId(),morder.getType());//获取美淘运单的运单号
					}
						
					
					morder.setOrderId(orderid);
					int kk=this.m_orderDao.updateOrderid(morder.getId(),orderid);
					if(kk==0)
					{
						throw new Exception("修改改运单号失败!");
					}
					//开始插入商品信息
					for(M_OrderDetail m:morder.getDetail())
					{
						m.setOrderId(orderid);
						m.setCtype(morder.getType());
					}
					int mm=this.m_DetailDao.insertMDetail(morder.getDetail());
					
					if(mm==morder.getDetail().size())
					{
						
					}
					else
					{
						throw new Exception("插入商品信息出错!");
					}
					
					if(orderIds.equalsIgnoreCase(""))
					{
						orderIds=orderid;
					}
					else
					{
						orderIds=orderIds+"; "+orderid;
					}
					
					//插入影射关系

					for(String tid:morder.getTorderIds())
					{
						Morder_Torder torder= new Morder_Torder();
						torder.setOrderId(orderid);
						torder.setTorderId(tid);
						torder.setDate(date);
						int kkk=this.morder_TorderDao.insert(torder);
						if(kkk==0)
						{
							throw new Exception("插入影射关系失败!");
						}
					}
					
					String torderids="";
					for(String tid:tids)
					{
						T_order torder1=this.torderDao.getone(null, tid);
						if(torder1!=null)
						{
							if(torderids.equalsIgnoreCase(""))
							{
								torderids=torder1.getTorderId();
							}
							else
							{
								torderids=torderids+"; "+torder1.getTorderId();
							}
						}
					}
					
					//插入运单状态，、
					Route route = new Route();
					route.setDate(date);
					route.setOrderId(orderid);
					route.setRemark("转运提交，相关转运单号:"+torderids);
					route.setState(Constant.ORDER_ROUTE__9);
					this.routeDao.insertRoute(route);
					
				}
				String date = DateUtil.date2String(new Date());
				for(String tid:tids)
				{

					int k=this.torderDao.modifystatebyId(Constant.T_ORDER_STATE6, tid, date,null,null);
					if(k==0)
					{
						throw new Exception("修改转运单状态失败!");
					}
					T_route route=new T_route();
					route.setDate(date);
					route.setEmployeeName("");
					route.setRemark("用户提交转运单，生成相关运单号："+orderIds);
					route.setState(TorderUtil.transformerState(0, Constant.T_ORDER_STATE6));
					route.setT_id(tid);
					route.setT_orderId(this.torderDao.getone(null, tid).getTorderId());
					this.trouteDao.insertTroute(route);//插入路由
				}
				
				
				
				ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
				//obj.setData(morder.getId());
				obj.setMessage("共生成"+number+"个包裹，运单号分别是:"+orderIds);
				return obj;
			}
			 catch (Exception e) {
					//throw ExceptionUtil.handle2ServiceException(e.getMessage());
				 return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION," 添加在线置单发生异常！");
				}
		}
		
		
	
		
		
		//用户添加家户提交的转运单并生成运单,freezemoney表示要冻结的钱,tmoney表示转运过程中产生的钱,tcost转运过程中产生的成本
		public ResponseObject<Object> add_tran_Morder_1(List<M_order> orders,double freezemoney,double tmoney,double tcost) throws ServiceException
				//public ResponseObject<Object> add_tran_Morder(M_order order) throws ServiceException
		{
			
			if(orders==null||orders.size()==0)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错!");
			}
			if(freezemoney<0)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数错误!");
			}
			try {
				
				
				
				
				
				List<String> tids=null;
				int number=0;
				String orderIds="";
				String userId="";
				String freezeId="0";
				
				if(!StringUtil.isEmpty(orders.get(0).getUserId()))
				{
					//插入要冻结的资金
					Account account=this.accountDao.getAccountByUserId(orders.get(0).getUserId());
					if(account==null)//查找账号失败
					{
						throw new Exception("查找用户账号信息发生异常!");
					}
					else
					{
						double rmbb=Double.parseDouble(account.getRmb());//获取人民币余额
						double usab=Double.parseDouble(account.getUsd());//美元余额
						double rate1=Double.parseDouble(this.globalargsDao.getcontentbyflag("cur_usa_cn"));
						if(rate1<=1)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"系统汇率设置错误，请稍后再操作");
						}
						double allrmb=rmbb+usab*rate1;
						
						double allfrmb=0;
						List<FreezeMoney> list=this.freezeMoneyDao.getbyuserId(orders.get(0).getUserId());//获取用户冻结的金额
						if(list!=null||list.size()<1)
						{
							for(FreezeMoney free:list)
							{
								allfrmb=Double.parseDouble(free.getRmb())+Double.parseDouble(free.getUsa())*rate1;
							}
						}
						
						
						String date1=DateUtil.date2String(new Date());
						if(allrmb-allfrmb>=(freezemoney*rate1))//有足够的钱
						{
							//插入冻结的资金
							FreezeMoney freezeMoney1=new FreezeMoney();
							freezeMoney1.setRmb("0");
							freezeMoney1.setUsa(String.valueOf(freezemoney));
							freezeMoney1.setCreateDate(date1);
							freezeMoney1.setUserId(orders.get(0).getUserId());
							int k=this.freezeMoneyDao.insert(freezeMoney1);
							if(k==0)
							{
								throw new Exception("插入结余额失败!");
							}
							
							freezeId=freezeMoney1.getId();
							
							
							
						}
						else
						{
							throw new Exception("余额不足!");
						}
					}
				}
				else
				{
					throw new Exception("用户id不能为空!");
				}
				int aa=0;
				for(M_order morder:orders)	
				{
					
					
					
					
					if(StringUtil.isEmpty(morder.getUserId()))
					{
						throw new Exception("用户id不能为空!");
					}
					if(userId.equalsIgnoreCase(""))
					{
						userId=morder.getUserId();
					}
					else
					{
						if(!userId.equalsIgnoreCase(morder.getUserId()))
						{
							throw new Exception("生成的运单号中存在不相同的用户id!");
						}
					}
					
					//转运的成本和费用都记录在第一条运单中
					if(aa==0)
					{
						morder.setTcost(Double.toString(tcost));
						morder.setTmoney(Double.toString(tmoney));
					}
					aa++;
					morder.setFreezId(freezeId);//保存冻结的资金
					
					tids=morder.getTorderIds();
					number++;
					String date = DateUtil.date2String(new Date());
					morder.setCreateDate(date);
					morder.setModifyDate(date);
					morder.setPayornot(Constant.ORDER_PAY_STATE0);//状态为未付款
					morder.setState(Constant.ORDER_STATE__9);//设置状态为转动置单
					morder.setType(Constant.ORDER_TYPE_KEY_2);
					
					if(morder.getRuser()!=null)
					{
						morder.getRuser().setCreateDate(date);
						morder.getRuser().setModifyDate(date);
						morder.getRuser().setUserId(morder.getUserId());
						morder.getRuser().setCardid_flag(Constant.VERFY_CARDID_0);//未进行身份证验证
						
						
						int k=this.receive_UserDao.insertReceiveUser(morder.getRuser());//插入收件人信息
						  if(k>0)
						  {
							  morder.setRuserId(morder.getRuser().getId());
						  }
						  else
						  {
							  throw new Exception("插入收件人用户失败");
						  }
						  
						
					}
					else
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"收件人为空!");
					}
					if(morder.getSuser()!=null)
					{
						morder.getSuser().setCreateDate(date);
						morder.getSuser().setModifyDate(date);
						morder.getSuser().setUserId(morder.getUserId());
					  int k=this.send_UserDao.insertSendUser(morder.getSuser());//插入发件人信息
					  if(k>0)
					  {
						  morder.setSuserId(morder.getSuser().getId());
					  }
					  else
					  {
						  throw new Exception("插入发件人用户失败");
					  }
					  
					 
					
					}
					else
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"发件人为空!");
					}
					
					
					
					
					
					morder.setPayornot(Constant.ORDER_PAY_STATE0);//设置为未付款状态，1表示已经付款，其它表示付款异常状态
					//int k=this.m_orderDao.insertMorder(morder);
					int k=this.m_orderDao.insertTranMorder(morder);
					if(k==0)
					{
						throw new Exception("插入运单失败");
					}
					//String orderid=this.createOrderIdarg(order.getId(),order.getStoreId(),order.getType());//获取运单的运单号
					String orderid="";
					if(StringUtil.isEmpty(morder.getUserId())||(morder.getUserId().equalsIgnoreCase("-1")))
					{
						//orderid=this.createOrderIdargnew(order.getId(),order.getStoreId(),order.getType(),false);//获取运单的运单号
						orderid=this.m_orderService.createOrderIdargnewMT(morder.getId(),morder.getStoreId(),morder.getType());//获取美淘运单的运单号
					}
					else
					{
						//orderid=this.createOrderIdargnew(order.getId(),order.getStoreId(),order.getType(),true);//获取运单的运单号
						orderid=this.m_orderService.createOrderIdargnewMT(morder.getId(),morder.getStoreId(),morder.getType());//获取美淘运单的运单号
					}
						
					
					morder.setOrderId(orderid);
					int kk=this.m_orderDao.updateOrderid(morder.getId(),orderid);
					if(kk==0)
					{
						throw new Exception("修改改运单号失败!");
					}
					//开始插入商品信息
					for(M_OrderDetail m:morder.getDetail())
					{
						m.setOrderId(orderid);
						m.setCtype(morder.getType());
					}
					int mm=this.m_DetailDao.insertMDetail(morder.getDetail());
					
					if(mm==morder.getDetail().size())
					{
						
					}
					else
					{
						throw new Exception("插入商品信息出错!");
					}
					
					if(orderIds.equalsIgnoreCase(""))
					{
						orderIds=orderid;
					}
					else
					{
						orderIds=orderIds+"; "+orderid;
					}
					
					//插入影射关系

					for(String tid:morder.getTorderIds())
					{
						Morder_Torder torder= new Morder_Torder();
						torder.setOrderId(orderid);
						torder.setTorderId(tid);
						torder.setDate(date);
						int kkk=this.morder_TorderDao.insert(torder);
						if(kkk==0)
						{
							throw new Exception("插入影射关系失败!");
						}
					}
					
					String torderids="";
					for(String tid:tids)
					{
						T_order torder1=this.torderDao.getone(null, tid);
						if(torder1!=null)
						{
							if(torderids.equalsIgnoreCase(""))
							{
								torderids=torder1.getTorderId();
							}
							else
							{
								torderids=torderids+"; "+torder1.getTorderId();
							}
						}
					}
					
					//插入运单状态，、
					Route route = new Route();
					route.setDate(date);
					route.setOrderId(orderid);
					route.setRemark("转运提交，相关转运单号:"+torderids);
					route.setState(Constant.ORDER_ROUTE__9);
					this.routeDao.insertRoute(route);
					
				}
				
				String date=DateUtil.date2String(new Date());
				AccountDetail detail = new AccountDetail();
				detail.setAmount(String.valueOf(freezemoney));
				detail.setCreateDate(date);
				detail.setModifyDate(date);
				detail.setState(Constant.ACCOUNT_DETAIL_STATE2);
				detail.setCurrency("美元");
				detail.setName("转运冻结资金");
				detail.setType(Constant.ACCOUNT_DETAIL_TYPE4);//冻结资金
				detail.setConfirm_state("0");
				detail.setUserId(orders.get(0).getUserId());
				
				String orderids="";
				for(M_order morder:orders)	
				{
					if(StringUtil.isEmpty(orderids)|| orderids.equalsIgnoreCase(""))
					{
						orderids=morder.getOrderId();
					}
					else
					{
						orderids=orderids+"; "+morder.getOrderId();
					}
				}
				detail.setRemark("相关运单号："+orderids);
				this.accountDetailDao.insertAccountDetail(detail);
				
				
			//	String date = DateUtil.date2String(new Date());
				for(String tid:tids)
				{

					int k=this.torderDao.modifystatebyId(Constant.T_ORDER_STATE6, tid, date,orderids,null);
					if(k==0)
					{
						throw new Exception("修改转运单状态失败!");
					}
					T_route route=new T_route();
					route.setDate(date);
					route.setEmployeeName("");
					route.setRemark("用户提交转运单，生成相关运单号："+orderIds);
					route.setState(TorderUtil.transformerState(0, Constant.T_ORDER_STATE6));
					route.setT_id(tid);
					route.setT_orderId(this.torderDao.getone(null, tid).getTorderId());
					this.trouteDao.insertTroute(route);//插入路由
				}
				
				
				
				ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
				//obj.setData(morder.getId());
				obj.setMessage("共生成"+number+"个包裹，运单号分别是:"+orderIds);
				return obj;
			}
			 catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("操作发生异常："+e.getMessage());
				// return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION," 添加在线置单发生异常！");
				}
		}
				
		
		
		//查找转运单状态
		public ResponseObject<PageSplit<T_order>> search_zy_byadmin(String s_cdate,String e_cdate,String s_idate,String e_idate,String storeId,String i_storeId,String s_storeId,String payway,String state,String keyword,int pageSize, int pageNow) throws ServiceException
		{
			
			
		
			if(StringUtil.isEmpty(keyword))
			{
				keyword=null;
			}
			else
			{
				keyword = StringUtil.escapeStringOfSearchKey(keyword);
			}
			try{
				
				int rowCount = 0;
				try {

					rowCount =this.torderDao.countOfSearchTordersbyadmin(s_cdate, e_cdate, s_idate, e_idate, storeId, i_storeId, s_storeId, payway, state, keyword);
							//this.torderDao.countOfSearchTordersbyuser(userId, state, keyword);
				} catch (Exception e) {
					e.printStackTrace();
					throw ExceptionUtil.handle2ServiceException("获取运单个数失败", e);
				}

				ResponseObject<PageSplit<T_order>> responseObj = new ResponseObject<PageSplit<T_order>>(
						ResponseCode.SUCCESS_CODE);
				if (rowCount > 0) {
					pageSize = Math.max(pageSize, 1);
					int pageCount = rowCount / pageSize
							+ (rowCount % pageSize == 0 ? 0 : 1);
					pageNow = Math.min(pageNow, pageCount);
					PageSplit<T_order> pageSplit = new PageSplit<T_order>();
					pageSplit.setPageCount(pageCount);
					pageSplit.setPageNow(pageNow);
					pageSplit.setRowCount(rowCount);
					pageSplit.setPageSize(pageSize);

					int startIndex = (pageNow - 1) * pageSize;
					try {
						List<T_order> orders = this.torderDao.SearchTordersbyadmin(s_cdate, e_cdate, s_idate, e_idate, storeId, i_storeId, s_storeId, payway, state, keyword, startIndex, pageSize);
								//SearchTordersbyuser(userId, state, keyword, startIndex, pageSize);
							
						for(T_order torder:orders)
						{
							if(!StringUtil.isEmpty(torder.getI_storeId()))
							{
								Warehouse ware=this.warehouseDao.getById(torder.getI_storeId());
								if(ware!=null)
								{
									torder.setI_storeName(ware.getName());
								}
							}
							if(!StringUtil.isEmpty(torder.getStoreId()))
							{
							
								Warehouse ware=this.warehouseDao.getById(torder.getStoreId());
								if(ware!=null)
								{
									torder.setStoreName(ware.getName());
								}
								
							}
							if(!StringUtil.isEmpty(torder.getEmployeeId()))
							{
							
								Employee emp=this.employeeDao.getEmployeeById(torder.getEmployeeId());
								if(emp!=null)
								{
									torder.setEmployeeName(emp.getAccount());
								}
								
							}
							if(!StringUtil.isEmpty(torder.getI_employeeId()))
							{
							
								Employee emp=this.employeeDao.getEmployeeById(torder.getI_employeeId());
								if(emp!=null)
								{
									torder.setI_employeeName(emp.getAccount());
								}
								
							}
							
						}
						
						pageSplit.setDatas(orders);
					} catch (Exception e) {
						throw ExceptionUtil.handle2ServiceException("获取运单列表失败", e);
					}
					responseObj.setData(pageSplit);
				} else {
					responseObj.setMessage("没有运单");
				}
				return responseObj;
			}
			catch(Exception e){
				//e.printStackTrace();
				return new ResponseObject<PageSplit<T_order>>(ResponseCode.SHOW_EXCEPTION,"操作发生异常:"+e.getMessage());
			}
			
		}
		
}


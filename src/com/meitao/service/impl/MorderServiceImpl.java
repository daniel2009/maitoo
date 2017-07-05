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
import com.meitao.dao.MessageRecordDao;
import com.meitao.dao.MorderDao;
import com.meitao.dao.Morder_TorderDao;
import com.meitao.dao.OrderMidfixDao;
import com.meitao.dao.Receive_UserDao;
import com.meitao.dao.RouteDao;
import com.meitao.dao.SeaNumberDao;
import com.meitao.dao.SendUserDao;
import com.meitao.dao.Send_UserDao;
import com.meitao.dao.Shelves_positionDao;
import com.meitao.dao.TorderDao;
import com.meitao.dao.TranshipmentBillDao;
import com.meitao.dao.TranshipmentCommodityDao;
import com.meitao.dao.TranshipmentRouteDao;
import com.meitao.dao.UserDao;
import com.meitao.dao.WarehouseDao;
import com.meitao.dao.globalargsDao;
import com.meitao.service.AutoSendService;
import com.meitao.service.MorderService;
import com.meitao.service.StoragePositionRecordService;
import com.meitao.service.StoragePositionService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.kuaidi.KuaiDiUtil;
import com.meitao.common.util.ConsigneeInfoUtil;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.common.util.OrderUtil;
import com.meitao.common.util.PropertiesReader;
import com.meitao.common.util.StringUtil;
import com.meitao.common.util.sms.SimpleSmsSender;
import com.meitao.exception.ServiceException;
import com.meitao.model.Account;
import com.meitao.model.AccountDetail;
import com.meitao.model.Channel;
import com.meitao.model.ConsigneeInfo;
import com.meitao.model.ConsigneeSendperson;
import com.meitao.model.Employee;
import com.meitao.model.FreezeMoney;
import com.meitao.model.M_OrderDetail;
import com.meitao.model.M_order;
import com.meitao.model.MessageRecord;
import com.meitao.model.PageSplit;
import com.meitao.model.Receive_User;
import com.meitao.model.ResponseObject;
import com.meitao.model.Route;
import com.meitao.model.SeaNumber;
import com.meitao.model.Shelves_position;
import com.meitao.model.T_order;
import com.meitao.model.User;
import com.meitao.model.Warehouse;
import com.meitao.model.temp.ImportMorder;
import com.meitao.model.temp.ImportthirdMorder;
import com.meitao.model.temp.MessageTemp;




@Service("m_orderService")
public class MorderServiceImpl extends BasicService implements MorderService {
	private static final Logger log = Logger.getLogger(MorderServiceImpl.class);
	
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
	private CommudityPriceDao commudityPriceDao;
	
	@Autowired
	private SendUserDao sendUserDao;

	@Autowired
	private MdetailDao m_DetailDao;
	@Autowired
	private MorderDao m_orderDao;
	
	@Autowired
	private TorderDao torderDao;	
	
	
	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private FreezeMoneyDao freezeMoneyDao;
	
	@Autowired
	private MessageRecordDao messageRecordDao;	
	
	
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
	private FlyInfoDao flyinfoDao;
	
	@Autowired
	private Morder_TorderDao morder_TorderDao;
	
	
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
	
	@Autowired
	private Shelves_positionDao shelves_positionDao;
	
	//运单的钱计算方式
		public double calculationM_orderFreight(M_order order)
				throws ServiceException {
			String jw_commodity_rate="";
			String lowest_weight_value="";
			String price_carry_flag="";
			double additivePrice=0;
			try{
				 jw_commodity_rate=this.globalargsDao.getcontentbyflag("jw_commodity_rate");//商品的进位值
				 lowest_weight_value=this.globalargsDao.getcontentbyflag("lowest_weight_value_flag");//最低值
				 price_carry_flag=this.globalargsDao.getcontentbyflag("price_carry_flag");//重量最小值
				 Channel addtivePrice1=this.channelDao.getChannelById(order.getChannelId());//渠道的附加价格
				 if(!StringUtil.isEmpty(addtivePrice1.getAdditivePrice()))//
				 {
					 additivePrice=StringUtil.string2Double(addtivePrice1.getAdditivePrice());
				 }
			}
			catch (Exception e) {
				//log.error("计算快递价格失败", e);
				//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "计算快递价格失败");
				throw ExceptionUtil.handle2ServiceException("获取后台计算参数失败，请检查全局变量jw_commodity_rate，lowest_weight_value_flag，price_carry_flag,或渠道附加费!");
			}
		
			
			if (order.getStoreId() == null) {
				throw ExceptionUtil.handle2ServiceException("门店为空，无法计算价格");
			}
			/* 运费计算 start */
			String priceType = "price";
			String type=Constant.USER_TYPE_NORMAL;
			if((!StringUtil.isEmpty(order.getUserId())))
			{
				User user=null;
				try{
					user=this.userDao.getUserById(order.getUserId());
				}catch (Exception e) {
					
				}
				
				if(user!=null)
				{
					type=user.getType();
				}
			}
			
			/*public static final String USER_TYPE_NORMAL = "0"; // 普通用户
			public static final String USER_TYPE_VIP0 = "1"; // 白银VIP会员
			public static final String USER_TYPE_VIP1 = "2"; // 黄金VIP会员
			public static final String USER_TYPE_VIP2 = "3"; // 白金VIP会员
			public static final String USER_TYPE_VIP3 = "4"; // 钻石VIP会员
			public static final String USER_TYPE_VIP4 = "5"; // 至尊VIP1会员
			public static final String USER_TYPE_VIP5 = "6"; // 至尊VIP2会员
			public static final String USER_TYPE_VIP6 = "7"; // 至尊VIP3会员
			public static final String USER_TYPE_VIP7 = "8"; //至尊VIP4会员*/
			
			
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
			

			double commodityPrice = 0.0d;
			double lowestprice=0;
			double highestprice=0;
			double commodityweighttotal=0;
			double tariff=0;
			double other=0;
			double or=0;
			//List<OrderDetail> details = Arrays.asList(order.getDetails());
			List<M_OrderDetail> details=order.getDetail();
			for (M_OrderDetail detail : details) {
				try {
					String price=this.commudityPriceDao.getPriceById(priceType, detail.getCommodityId());
					detail.setCprice(price);//保存商品单价
					if (price == null) {
						throw ExceptionUtil
								.handle2ServiceException("运单中含有系统中没有的商品价格类型！");
					}
					
					if(StringUtil.string2Double(price)<=0)//如果价格是0，直接返回0作为判断
					{
						throw ExceptionUtil
						.handle2ServiceException("运单中含有系统中没有的商品价格类型或价格没设置！");
					}

					if(lowestprice==0)
					{
						lowestprice=StringUtil.string2Double(price);
					}
					else
					{
						if(lowestprice>StringUtil.string2Double(price))
						{
							lowestprice=StringUtil.string2Double(price);
						}
					}
					
					if(highestprice==0)
					{
						highestprice=StringUtil.string2Double(price);
					}
					else
					{
						if(highestprice<StringUtil.string2Double(price))
						{
							highestprice=StringUtil.string2Double(price);
						}
					}
					commodityweighttotal=commodityweighttotal+StringUtil.string2Double(detail.getWeight());
					commodityPrice = commodityPrice
							+ StringUtil.string2Double(detail.getWeight())
							* StringUtil.string2Double(price);
					double smallallprice=StringUtil.string2Double(detail.getWeight())
							* StringUtil.string2Double(price);
					detail.setAllcprice(Double.toString(smallallprice));
					
					tariff=tariff+StringUtil.string2Double(detail.getTariff());
					other=other+StringUtil.string2Double(detail.getOther());
					or=or+StringUtil.string2Double(detail.getOr());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw ExceptionUtil.handle2ServiceException("计算商品运费时出现异常！");
				}
			}

			
			order.setSjweight(String.valueOf(commodityweighttotal));
			order.setOther(String.valueOf(other));
			order.setTariff(String.valueOf(tariff));
			
			//double commodityweighttotal=StringUtil.string2Double(order.getWeight());
			double additiveweight=0.00;//附加重量
			if(commodityweighttotal<StringUtil.string2Double(lowest_weight_value))
			{
				if((!price_carry_flag.equalsIgnoreCase("0")))
				{
					additiveweight=StringUtil.string2Double(lowest_weight_value)-commodityweighttotal;
					//commodityweighttotal=StringUtil.string2Double(lowest_weight_value);
				}
				order.setWeight(lowest_weight_value);
			}
			else
			{
				//1：总重量进位，进位重量按价格最高的商品计算，小于部分将退位计算，退位部分按最高价格计算扣除。。

				//2：总重量进位，进位重量按价格最低的商品计算，小于部分将退位计算，退位部分按最低价格计算扣除。。

				//3：总重量进位，进位重量按价格最高的商品计算，小于部分按实际计算。

				//4：总重量进位，进位重量按价格最低的商品计算，小于部分按实际计算。。

				//注意：总重量不能低于，如果低于此值，将按此值来计算价格。
				if((price_carry_flag.equalsIgnoreCase("1"))||(price_carry_flag.equalsIgnoreCase("2"))||(price_carry_flag.equalsIgnoreCase("3"))||(price_carry_flag.equalsIgnoreCase("4")))
				{
					//double sum_jwweight=StringUtil.string2Double(order.getWeight());//总实际重量
					double sum_jwweight=commodityweighttotal;
					if((StringUtil.string2Double(jw_commodity_rate)>0)&&(StringUtil.string2Double(jw_commodity_rate)<1))
					{
						
						
						if((commodityweighttotal-((int)commodityweighttotal))>StringUtil.string2Double(jw_commodity_rate))
						{
							additiveweight=((int)commodityweighttotal)+1-sum_jwweight;
							order.setWeight(String.valueOf(((int)commodityweighttotal)+1));
						}
						else
						{

							if((price_carry_flag.equalsIgnoreCase("1"))||(price_carry_flag.equalsIgnoreCase("2")))//退位计算
							{
								additiveweight=((int)commodityweighttotal)-commodityweighttotal;//退位计算
								order.setWeight(String.valueOf(((int)commodityweighttotal)));
								//commodityweighttotal=j;
							}
							else if((price_carry_flag=="3")||(price_carry_flag=="4"))//按实际计算
							{
								additiveweight=0;
								order.setWeight(String.valueOf((commodityweighttotal)));
							}
							else
							{

								additiveweight=0;
								order.setWeight(String.valueOf((commodityweighttotal)));
							}
						}
						
						
					}
					else
					{
						
						//commodityweighttotal=commodityjwweighttotal;
						additiveweight=0;
						order.setWeight(String.valueOf(commodityweighttotal));
					}
					
				}
				else
				{
					order.setWeight(String.valueOf(commodityweighttotal));
				}
			}
			
			if(additiveweight<=-1)
			{
				additiveweight=0;
			}
			
			

			
			
					if(price_carry_flag.equalsIgnoreCase("1")||price_carry_flag.equalsIgnoreCase("3"))
					{
						additivePrice=additivePrice+highestprice*additiveweight;
					}
					else if(price_carry_flag.equalsIgnoreCase("2")||price_carry_flag.equalsIgnoreCase("4"))
					{
						additivePrice=additivePrice+lowestprice*additiveweight;
					}
			
			
			double premium = StringUtil.string2Double(order.getInsurance());

		

			BigDecimal money = new BigDecimal(premium);
			money=money.add(BigDecimal.valueOf(additivePrice));//添加附加价格
			money = money.add(BigDecimal.valueOf(other));
			money = money.add(BigDecimal.valueOf(or));
			money = money.add(BigDecimal.valueOf(tariff));
			//money = money.add(BigDecimal.valueOf(volumeMoney));
			money = money.add(BigDecimal.valueOf(commodityPrice));

			return money.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

			/* 运费计算 end */

		}
		
		
		
		
		
		
		//计算包含转运的计费计算方式
		public double calculationM_orderFreight_tran(M_order order)
				throws ServiceException {
			String jw_commodity_rate="";
			String lowest_weight_value="";
			String price_carry_flag="";
			double additivePrice=0;
			try{
				 jw_commodity_rate=this.globalargsDao.getcontentbyflag("jw_commodity_rate");//商品的进位值
				 lowest_weight_value=this.globalargsDao.getcontentbyflag("lowest_weight_value_flag");//最低值
				 price_carry_flag=this.globalargsDao.getcontentbyflag("price_carry_flag");//重量最小值
				 Channel addtivePrice1=this.channelDao.getChannelById(order.getChannelId());//渠道的附加价格
				 if(!StringUtil.isEmpty(addtivePrice1.getAdditivePrice()))//
				 {
					 additivePrice=StringUtil.string2Double(addtivePrice1.getAdditivePrice());
				 }
			}
			catch (Exception e) {
				//log.error("计算快递价格失败", e);
				//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "计算快递价格失败");
				throw ExceptionUtil.handle2ServiceException("获取后台计算参数失败，请检查全局变量jw_commodity_rate，lowest_weight_value_flag，price_carry_flag,或渠道附加费!");
			}
		
			
			if (order.getStoreId() == null) {
				throw ExceptionUtil.handle2ServiceException("门店为空，无法计算价格");
			}
			/* 运费计算 start */
			String priceType = "price";
			String type=Constant.USER_TYPE_NORMAL;
			if((!StringUtil.isEmpty(order.getUserId())))
			{
				User user=null;
				try{
					user=this.userDao.getUserById(order.getUserId());
				}catch (Exception e) {
					
				}
				
				if(user!=null)
				{
					type=user.getType();
				}
			}
			
			/*public static final String USER_TYPE_NORMAL = "0"; // 普通用户
			public static final String USER_TYPE_VIP0 = "1"; // 白银VIP会员
			public static final String USER_TYPE_VIP1 = "2"; // 黄金VIP会员
			public static final String USER_TYPE_VIP2 = "3"; // 白金VIP会员
			public static final String USER_TYPE_VIP3 = "4"; // 钻石VIP会员
			public static final String USER_TYPE_VIP4 = "5"; // 至尊VIP1会员
			public static final String USER_TYPE_VIP5 = "6"; // 至尊VIP2会员
			public static final String USER_TYPE_VIP6 = "7"; // 至尊VIP3会员
			public static final String USER_TYPE_VIP7 = "8"; //至尊VIP4会员*/
			
			
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
			

			double commodityPrice = 0.0d;
			double lowestprice=0;
			double highestprice=0;
			double commodityweighttotal=0;
			double tariff=0;
			double other=0;
			double or=0;
			//List<OrderDetail> details = Arrays.asList(order.getDetails());
			List<M_OrderDetail> details=order.getDetail();
			for (M_OrderDetail detail : details) {
				try {
					String price=this.commudityPriceDao.getPriceById(priceType, detail.getCommodityId());
					detail.setCprice(price);//保存商品单价
					if (price == null) {
						throw ExceptionUtil
								.handle2ServiceException("运单中含有系统中没有的商品价格类型！");
					}
					
					if(StringUtil.string2Double(price)<=0)//如果价格是0，直接返回0作为判断
					{
						throw ExceptionUtil
						.handle2ServiceException("运单中含有系统中没有的商品价格类型或价格没设置！");
					}

					if(lowestprice==0)
					{
						lowestprice=StringUtil.string2Double(price);
					}
					else
					{
						if(lowestprice>StringUtil.string2Double(price))
						{
							lowestprice=StringUtil.string2Double(price);
						}
					}
					
					if(highestprice==0)
					{
						highestprice=StringUtil.string2Double(price);
					}
					else
					{
						if(highestprice<StringUtil.string2Double(price))
						{
							highestprice=StringUtil.string2Double(price);
						}
					}
					commodityweighttotal=commodityweighttotal+StringUtil.string2Double(detail.getWeight());
					commodityPrice = commodityPrice
							+ StringUtil.string2Double(detail.getWeight())
							* StringUtil.string2Double(price);
					double smallallprice=StringUtil.string2Double(detail.getWeight())
							* StringUtil.string2Double(price);
					detail.setAllcprice(Double.toString(smallallprice));
					
					tariff=tariff+StringUtil.string2Double(detail.getTariff());
					other=other+StringUtil.string2Double(detail.getOther());
					or=or+StringUtil.string2Double(detail.getOr());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw ExceptionUtil.handle2ServiceException("计算商品运费时出现异常！");
				}
			}

			
			order.setSjweight(String.valueOf(commodityweighttotal));
			order.setOther(String.valueOf(other));
			order.setTariff(String.valueOf(tariff));
			
			//double commodityweighttotal=StringUtil.string2Double(order.getWeight());
			double additiveweight=0.00;//附加重量
			if(commodityweighttotal<StringUtil.string2Double(lowest_weight_value))
			{
				if((!price_carry_flag.equalsIgnoreCase("0")))
				{
					additiveweight=StringUtil.string2Double(lowest_weight_value)-commodityweighttotal;
					//commodityweighttotal=StringUtil.string2Double(lowest_weight_value);
				}
				order.setWeight(lowest_weight_value);
			}
			else
			{
				//1：总重量进位，进位重量按价格最高的商品计算，小于部分将退位计算，退位部分按最高价格计算扣除。。

				//2：总重量进位，进位重量按价格最低的商品计算，小于部分将退位计算，退位部分按最低价格计算扣除。。

				//3：总重量进位，进位重量按价格最高的商品计算，小于部分按实际计算。

				//4：总重量进位，进位重量按价格最低的商品计算，小于部分按实际计算。。

				//注意：总重量不能低于，如果低于此值，将按此值来计算价格。
				if((price_carry_flag.equalsIgnoreCase("1"))||(price_carry_flag.equalsIgnoreCase("2"))||(price_carry_flag.equalsIgnoreCase("3"))||(price_carry_flag.equalsIgnoreCase("4")))
				{
					//double sum_jwweight=StringUtil.string2Double(order.getWeight());//总实际重量
					double sum_jwweight=commodityweighttotal;
					if((StringUtil.string2Double(jw_commodity_rate)>0)&&(StringUtil.string2Double(jw_commodity_rate)<1))
					{
						
						
						if((commodityweighttotal-((int)commodityweighttotal))>StringUtil.string2Double(jw_commodity_rate))
						{
							additiveweight=((int)commodityweighttotal)+1-sum_jwweight;
							order.setWeight(String.valueOf(((int)commodityweighttotal)+1));
						}
						else
						{

							if((price_carry_flag.equalsIgnoreCase("1"))||(price_carry_flag.equalsIgnoreCase("2")))//退位计算
							{
								additiveweight=((int)commodityweighttotal)-commodityweighttotal;//退位计算
								order.setWeight(String.valueOf(((int)commodityweighttotal)));
								//commodityweighttotal=j;
							}
							else if((price_carry_flag=="3")||(price_carry_flag=="4"))//按实际计算
							{
								additiveweight=0;
								order.setWeight(String.valueOf((commodityweighttotal)));
							}
							else
							{

								additiveweight=0;
								order.setWeight(String.valueOf((commodityweighttotal)));
							}
						}
						
						
					}
					else
					{
						
						//commodityweighttotal=commodityjwweighttotal;
						additiveweight=0;
						order.setWeight(String.valueOf(commodityweighttotal));
					}
					
				}
				else
				{
					order.setWeight(String.valueOf(commodityweighttotal));
				}
			}
			
			if(additiveweight<=-1)
			{
				additiveweight=0;
			}
			
			

			
			
					if(price_carry_flag.equalsIgnoreCase("1")||price_carry_flag.equalsIgnoreCase("3"))
					{
						additivePrice=additivePrice+highestprice*additiveweight;
					}
					else if(price_carry_flag.equalsIgnoreCase("2")||price_carry_flag.equalsIgnoreCase("4"))
					{
						additivePrice=additivePrice+lowestprice*additiveweight;
					}
			
			
			double premium = StringUtil.string2Double(order.getInsurance());

		    double tranprice=0;
			try{
				if(!StringUtil.isEmpty(order.getTmoney()))
				{
					tranprice=Double.parseDouble(order.getTmoney());	
					if(tranprice<=0)
					{
						tranprice=0;
					}
				}
			}catch(Exception e){
				
			}
			

			BigDecimal money = new BigDecimal(premium);
			money=money.add(BigDecimal.valueOf(additivePrice));//添加附加价格
			money = money.add(BigDecimal.valueOf(other));
			money = money.add(BigDecimal.valueOf(or));
			money = money.add(BigDecimal.valueOf(tariff));
			money = money.add(BigDecimal.valueOf(tranprice));
			//money = money.add(BigDecimal.valueOf(volumeMoney));
			money = money.add(BigDecimal.valueOf(commodityPrice));

			return money.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

			/* 运费计算 end */

		}
		
		
		//指定类型来计算价格
		public double calculationM_orderFreight_usertype(M_order order,String type)
				throws ServiceException {
			String jw_commodity_rate="";
			String lowest_weight_value="";
			String price_carry_flag="";
			double additivePrice=0;
			try{
				 jw_commodity_rate=this.globalargsDao.getcontentbyflag("jw_commodity_rate");//商品的进位值
				 lowest_weight_value=this.globalargsDao.getcontentbyflag("lowest_weight_value_flag");//最低值
				 price_carry_flag=this.globalargsDao.getcontentbyflag("price_carry_flag");//重量最小值
				 Channel addtivePrice1=this.channelDao.getChannelById(order.getChannelId());//渠道的附加价格
				 if(!StringUtil.isEmpty(addtivePrice1.getAdditivePrice()))//
				 {
					 additivePrice=StringUtil.string2Double(addtivePrice1.getAdditivePrice());
				 }
			}
			catch (Exception e) {
				//log.error("计算快递价格失败", e);
				//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "计算快递价格失败");
				throw ExceptionUtil.handle2ServiceException("获取后台计算参数失败，请检查全局变量jw_commodity_rate，lowest_weight_value_flag，price_carry_flag,或渠道附加费!");
			}
		
			
			if (order.getStoreId() == null) {
				throw ExceptionUtil.handle2ServiceException("门店为空，无法计算价格");
			}
			/* 运费计算 start */
			String priceType = "price";
			/*String type=Constant.USER_TYPE_NORMAL;
			if((!StringUtil.isEmpty(order.getUserId())))
			{
				User user=null;
				try{
					user=this.userDao.getUserById(order.getUserId());
				}catch (Exception e) {
					
				}
				
				if(user!=null)
				{
					type=user.getType();
				}
			}*/
			
			/*public static final String USER_TYPE_NORMAL = "0"; // 普通用户
			public static final String USER_TYPE_VIP0 = "1"; // 白银VIP会员
			public static final String USER_TYPE_VIP1 = "2"; // 黄金VIP会员
			public static final String USER_TYPE_VIP2 = "3"; // 白金VIP会员
			public static final String USER_TYPE_VIP3 = "4"; // 钻石VIP会员
			public static final String USER_TYPE_VIP4 = "5"; // 至尊VIP1会员
			public static final String USER_TYPE_VIP5 = "6"; // 至尊VIP2会员
			public static final String USER_TYPE_VIP6 = "7"; // 至尊VIP3会员
			public static final String USER_TYPE_VIP7 = "8"; //至尊VIP4会员*/
			
			
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
			

			double commodityPrice = 0.0d;
			double lowestprice=0;
			double highestprice=0;
			double commodityweighttotal=0;
			double tariff=0;
			double other=0;
			double or=0;
			//List<OrderDetail> details = Arrays.asList(order.getDetails());
			List<M_OrderDetail> details=order.getDetail();
			for (M_OrderDetail detail : details) {
				try {
					String price=this.commudityPriceDao.getPriceById(priceType, detail.getCommodityId());
					detail.setCprice(price);
					if (price == null) {
						throw ExceptionUtil
								.handle2ServiceException("运单中含有系统中没有的商品价格类型！");
					}
					
					if(StringUtil.string2Double(price)<=0)//如果价格是0，直接返回0作为判断
					{
						throw ExceptionUtil
						.handle2ServiceException("运单中含有系统中没有的商品价格类型或价格没设置！");
					}

					if(lowestprice==0)
					{
						lowestprice=StringUtil.string2Double(price);
					}
					else
					{
						if(lowestprice>StringUtil.string2Double(price))
						{
							lowestprice=StringUtil.string2Double(price);
						}
					}
					
					if(highestprice==0)
					{
						highestprice=StringUtil.string2Double(price);
					}
					else
					{
						if(highestprice<StringUtil.string2Double(price))
						{
							highestprice=StringUtil.string2Double(price);
						}
					}
					commodityweighttotal=commodityweighttotal+StringUtil.string2Double(detail.getWeight());
					commodityPrice = commodityPrice
							+ StringUtil.string2Double(detail.getWeight())
							* StringUtil.string2Double(price);
					
					double smallallprice=StringUtil.string2Double(detail.getWeight())
							* StringUtil.string2Double(price);
					
					detail.setAllcprice(Double.toString(smallallprice));
					
					tariff=tariff+StringUtil.string2Double(detail.getTariff());
					other=other+StringUtil.string2Double(detail.getOther());
					or=or+StringUtil.string2Double(detail.getOr());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw ExceptionUtil.handle2ServiceException("计算商品运费时出现异常！");
				}
			}

			
			order.setSjweight(String.valueOf(commodityweighttotal));
			order.setOther(String.valueOf(other));
			order.setTariff(String.valueOf(tariff));
			
			//double commodityweighttotal=StringUtil.string2Double(order.getWeight());
			double additiveweight=0.00;//附加重量
			if(commodityweighttotal<StringUtil.string2Double(lowest_weight_value))
			{
				if((!price_carry_flag.equalsIgnoreCase("0")))
				{
					additiveweight=StringUtil.string2Double(lowest_weight_value)-commodityweighttotal;
					//commodityweighttotal=StringUtil.string2Double(lowest_weight_value);
				}
				order.setWeight(lowest_weight_value);
			}
			else
			{
				//1：总重量进位，进位重量按价格最高的商品计算，小于部分将退位计算，退位部分按最高价格计算扣除。。

				//2：总重量进位，进位重量按价格最低的商品计算，小于部分将退位计算，退位部分按最低价格计算扣除。。

				//3：总重量进位，进位重量按价格最高的商品计算，小于部分按实际计算。

				//4：总重量进位，进位重量按价格最低的商品计算，小于部分按实际计算。。

				//注意：总重量不能低于，如果低于此值，将按此值来计算价格。
				if((price_carry_flag.equalsIgnoreCase("1"))||(price_carry_flag.equalsIgnoreCase("2"))||(price_carry_flag.equalsIgnoreCase("3"))||(price_carry_flag.equalsIgnoreCase("4")))
				{
					//double sum_jwweight=StringUtil.string2Double(order.getWeight());//总实际重量
					double sum_jwweight=commodityweighttotal;
					if((StringUtil.string2Double(jw_commodity_rate)>0)&&(StringUtil.string2Double(jw_commodity_rate)<1))
					{
						
						
						if((commodityweighttotal-((int)commodityweighttotal))>StringUtil.string2Double(jw_commodity_rate))
						{
							additiveweight=((int)commodityweighttotal)+1-sum_jwweight;
							order.setWeight(String.valueOf(((int)commodityweighttotal)+1));
						}
						else
						{

							if((price_carry_flag.equalsIgnoreCase("1"))||(price_carry_flag.equalsIgnoreCase("2")))//退位计算
							{
								additiveweight=((int)commodityweighttotal)-commodityweighttotal;//退位计算
								order.setWeight(String.valueOf(((int)commodityweighttotal)));
								//commodityweighttotal=j;
							}
							else if((price_carry_flag=="3")||(price_carry_flag=="4"))//按实际计算
							{
								additiveweight=0;
								order.setWeight(String.valueOf((commodityweighttotal)));
							}
							else
							{

								additiveweight=0;
								order.setWeight(String.valueOf((commodityweighttotal)));
							}
						}
						
						
					}
					else
					{
						
						//commodityweighttotal=commodityjwweighttotal;
						additiveweight=0;
						order.setWeight(String.valueOf(commodityweighttotal));
					}
					
				}
				else
				{
					order.setWeight(String.valueOf(commodityweighttotal));
				}
			}
			
			if(additiveweight<=-1)
			{
				additiveweight=0;
			}
			
			

			
			
					if(price_carry_flag.equalsIgnoreCase("1")||price_carry_flag.equalsIgnoreCase("3"))
					{
						additivePrice=additivePrice+highestprice*additiveweight;
					}
					else if(price_carry_flag.equalsIgnoreCase("2")||price_carry_flag.equalsIgnoreCase("4"))
					{
						additivePrice=additivePrice+lowestprice*additiveweight;
					}
			
			
			double premium = StringUtil.string2Double(order.getInsurance());

		

			BigDecimal money = new BigDecimal(premium);
			money=money.add(BigDecimal.valueOf(additivePrice));//添加附加价格
			money = money.add(BigDecimal.valueOf(other));
			money = money.add(BigDecimal.valueOf(or));
			money = money.add(BigDecimal.valueOf(tariff));
			//money = money.add(BigDecimal.valueOf(volumeMoney));
			money = money.add(BigDecimal.valueOf(commodityPrice));

			return money.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

			/* 运费计算 end */

		}
		
		
		
		//指定类型来计算价格，包含转运价格
				public double calculationM_orderFreight_usertype_tran(M_order order,String type)
						throws ServiceException {
					String jw_commodity_rate="";
					String lowest_weight_value="";
					String price_carry_flag="";
					double additivePrice=0;
					try{
						 jw_commodity_rate=this.globalargsDao.getcontentbyflag("jw_commodity_rate");//商品的进位值
						 lowest_weight_value=this.globalargsDao.getcontentbyflag("lowest_weight_value_flag");//最低值
						 price_carry_flag=this.globalargsDao.getcontentbyflag("price_carry_flag");//重量最小值
						 Channel addtivePrice1=this.channelDao.getChannelById(order.getChannelId());//渠道的附加价格
						 if(!StringUtil.isEmpty(addtivePrice1.getAdditivePrice()))//
						 {
							 additivePrice=StringUtil.string2Double(addtivePrice1.getAdditivePrice());
						 }
					}
					catch (Exception e) {
						//log.error("计算快递价格失败", e);
						//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "计算快递价格失败");
						throw ExceptionUtil.handle2ServiceException("获取后台计算参数失败，请检查全局变量jw_commodity_rate，lowest_weight_value_flag，price_carry_flag,或渠道附加费!");
					}
				
					
					if (order.getStoreId() == null) {
						throw ExceptionUtil.handle2ServiceException("门店为空，无法计算价格");
					}
					/* 运费计算 start */
					String priceType = "price";
					/*String type=Constant.USER_TYPE_NORMAL;
					if((!StringUtil.isEmpty(order.getUserId())))
					{
						User user=null;
						try{
							user=this.userDao.getUserById(order.getUserId());
						}catch (Exception e) {
							
						}
						
						if(user!=null)
						{
							type=user.getType();
						}
					}*/
					
					/*public static final String USER_TYPE_NORMAL = "0"; // 普通用户
					public static final String USER_TYPE_VIP0 = "1"; // 白银VIP会员
					public static final String USER_TYPE_VIP1 = "2"; // 黄金VIP会员
					public static final String USER_TYPE_VIP2 = "3"; // 白金VIP会员
					public static final String USER_TYPE_VIP3 = "4"; // 钻石VIP会员
					public static final String USER_TYPE_VIP4 = "5"; // 至尊VIP1会员
					public static final String USER_TYPE_VIP5 = "6"; // 至尊VIP2会员
					public static final String USER_TYPE_VIP6 = "7"; // 至尊VIP3会员
					public static final String USER_TYPE_VIP7 = "8"; //至尊VIP4会员*/
					
					
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
					

					double commodityPrice = 0.0d;
					double lowestprice=0;
					double highestprice=0;
					double commodityweighttotal=0;
					double tariff=0;
					double other=0;
					double or=0;
					//List<OrderDetail> details = Arrays.asList(order.getDetails());
					List<M_OrderDetail> details=order.getDetail();
					for (M_OrderDetail detail : details) {
						try {
							String price=this.commudityPriceDao.getPriceById(priceType, detail.getCommodityId());
							detail.setCprice(price);
							if (price == null) {
								throw ExceptionUtil
										.handle2ServiceException("运单中含有系统中没有的商品价格类型！");
							}
							
							if(StringUtil.string2Double(price)<=0)//如果价格是0，直接返回0作为判断
							{
								throw ExceptionUtil
								.handle2ServiceException("运单中含有系统中没有的商品价格类型或价格没设置！");
							}

							if(lowestprice==0)
							{
								lowestprice=StringUtil.string2Double(price);
							}
							else
							{
								if(lowestprice>StringUtil.string2Double(price))
								{
									lowestprice=StringUtil.string2Double(price);
								}
							}
							
							if(highestprice==0)
							{
								highestprice=StringUtil.string2Double(price);
							}
							else
							{
								if(highestprice<StringUtil.string2Double(price))
								{
									highestprice=StringUtil.string2Double(price);
								}
							}
							commodityweighttotal=commodityweighttotal+StringUtil.string2Double(detail.getWeight());
							commodityPrice = commodityPrice
									+ StringUtil.string2Double(detail.getWeight())
									* StringUtil.string2Double(price);
							
							double smallallprice=StringUtil.string2Double(detail.getWeight())
									* StringUtil.string2Double(price);
							
							detail.setAllcprice(Double.toString(smallallprice));
							
							tariff=tariff+StringUtil.string2Double(detail.getTariff());
							other=other+StringUtil.string2Double(detail.getOther());
							or=or+StringUtil.string2Double(detail.getOr());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							throw ExceptionUtil.handle2ServiceException("计算商品运费时出现异常！");
						}
					}

					
					order.setSjweight(String.valueOf(commodityweighttotal));
					order.setOther(String.valueOf(other));
					order.setTariff(String.valueOf(tariff));
					
					//double commodityweighttotal=StringUtil.string2Double(order.getWeight());
					double additiveweight=0.00;//附加重量
					if(commodityweighttotal<StringUtil.string2Double(lowest_weight_value))
					{
						if((!price_carry_flag.equalsIgnoreCase("0")))
						{
							additiveweight=StringUtil.string2Double(lowest_weight_value)-commodityweighttotal;
							//commodityweighttotal=StringUtil.string2Double(lowest_weight_value);
						}
						order.setWeight(lowest_weight_value);
					}
					else
					{
						//1：总重量进位，进位重量按价格最高的商品计算，小于部分将退位计算，退位部分按最高价格计算扣除。。

						//2：总重量进位，进位重量按价格最低的商品计算，小于部分将退位计算，退位部分按最低价格计算扣除。。

						//3：总重量进位，进位重量按价格最高的商品计算，小于部分按实际计算。

						//4：总重量进位，进位重量按价格最低的商品计算，小于部分按实际计算。。

						//注意：总重量不能低于，如果低于此值，将按此值来计算价格。
						if((price_carry_flag.equalsIgnoreCase("1"))||(price_carry_flag.equalsIgnoreCase("2"))||(price_carry_flag.equalsIgnoreCase("3"))||(price_carry_flag.equalsIgnoreCase("4")))
						{
							//double sum_jwweight=StringUtil.string2Double(order.getWeight());//总实际重量
							double sum_jwweight=commodityweighttotal;
							if((StringUtil.string2Double(jw_commodity_rate)>0)&&(StringUtil.string2Double(jw_commodity_rate)<1))
							{
								
								
								if((commodityweighttotal-((int)commodityweighttotal))>StringUtil.string2Double(jw_commodity_rate))
								{
									additiveweight=((int)commodityweighttotal)+1-sum_jwweight;
									order.setWeight(String.valueOf(((int)commodityweighttotal)+1));
								}
								else
								{

									if((price_carry_flag.equalsIgnoreCase("1"))||(price_carry_flag.equalsIgnoreCase("2")))//退位计算
									{
										additiveweight=((int)commodityweighttotal)-commodityweighttotal;//退位计算
										order.setWeight(String.valueOf(((int)commodityweighttotal)));
										//commodityweighttotal=j;
									}
									else if((price_carry_flag=="3")||(price_carry_flag=="4"))//按实际计算
									{
										additiveweight=0;
										order.setWeight(String.valueOf((commodityweighttotal)));
									}
									else
									{

										additiveweight=0;
										order.setWeight(String.valueOf((commodityweighttotal)));
									}
								}
								
								
							}
							else
							{
								
								//commodityweighttotal=commodityjwweighttotal;
								additiveweight=0;
								order.setWeight(String.valueOf(commodityweighttotal));
							}
							
						}
						else
						{
							order.setWeight(String.valueOf(commodityweighttotal));
						}
					}
					
					if(additiveweight<=-1)
					{
						additiveweight=0;
					}
					
					

					
					
							if(price_carry_flag.equalsIgnoreCase("1")||price_carry_flag.equalsIgnoreCase("3"))
							{
								additivePrice=additivePrice+highestprice*additiveweight;
							}
							else if(price_carry_flag.equalsIgnoreCase("2")||price_carry_flag.equalsIgnoreCase("4"))
							{
								additivePrice=additivePrice+lowestprice*additiveweight;
							}
					
					
					double premium = StringUtil.string2Double(order.getInsurance());

					 double tranprice=0;
						try{
							if(!StringUtil.isEmpty(order.getTmoney()))
							{
								tranprice=Double.parseDouble(order.getTmoney());	
								if(tranprice<=0)
								{
									tranprice=0;
								}
							}
						}catch(Exception e){
							
						}

					BigDecimal money = new BigDecimal(premium);
					money=money.add(BigDecimal.valueOf(additivePrice));//添加附加价格
					money = money.add(BigDecimal.valueOf(other));
					money = money.add(BigDecimal.valueOf(or));
					money = money.add(BigDecimal.valueOf(tariff));
					money = money.add(BigDecimal.valueOf(tranprice));
					//money = money.add(BigDecimal.valueOf(volumeMoney));
					money = money.add(BigDecimal.valueOf(commodityPrice));

					return money.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

					/* 运费计算 end */

				}
				
				
		
		//运单类型的价格计算方式，如果
		public double calculationM_orderFreight_bytype(M_order order)
				throws ServiceException {
			String jw_commodity_rate="";
			String lowest_weight_value="";
			String price_carry_flag="";
			double additivePrice=0;
			try{
				 jw_commodity_rate=this.globalargsDao.getcontentbyflag("jw_commodity_rate");//商品的进位值
				 lowest_weight_value=this.globalargsDao.getcontentbyflag("lowest_weight_value_flag");//最低值
				 price_carry_flag=this.globalargsDao.getcontentbyflag("price_carry_flag");//重量最小值
				 Channel addtivePrice1=this.channelDao.getChannelById(order.getChannelId());//渠道的附加价格
				 if(!StringUtil.isEmpty(addtivePrice1.getAdditivePrice()))//
				 {
					 additivePrice=StringUtil.string2Double(addtivePrice1.getAdditivePrice());
				 }
			}
			catch (Exception e) {
				//log.error("计算快递价格失败", e);
				//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "计算快递价格失败");
				throw ExceptionUtil.handle2ServiceException("获取后台计算参数失败，请检查全局变量jw_commodity_rate，lowest_weight_value_flag，price_carry_flag,或渠道附加费!");
			}
		
			
			if (order.getStoreId() == null) {
				throw ExceptionUtil.handle2ServiceException("门店为空，无法计算价格");
			}
			/* 运费计算 start */
			String priceType = "price";
			String type=Constant.USER_TYPE_NORMAL;
			if((!StringUtil.isEmpty(order.getUserId())))
			{
				User user=null;
				try{
					user=this.userDao.getUserById(order.getUserId());
				}catch (Exception e) {
					
				}
				
				if(user!=null)
				{
					type=user.getType();
				}
			}
			
			/*public static final String USER_TYPE_NORMAL = "0"; // 普通用户
			public static final String USER_TYPE_VIP0 = "1"; // 白银VIP会员
			public static final String USER_TYPE_VIP1 = "2"; // 黄金VIP会员
			public static final String USER_TYPE_VIP2 = "3"; // 白金VIP会员
			public static final String USER_TYPE_VIP3 = "4"; // 钻石VIP会员
			public static final String USER_TYPE_VIP4 = "5"; // 至尊VIP1会员
			public static final String USER_TYPE_VIP5 = "6"; // 至尊VIP2会员
			public static final String USER_TYPE_VIP6 = "7"; // 至尊VIP3会员
			public static final String USER_TYPE_VIP7 = "8"; //至尊VIP4会员*/
	
			String typeprice_1;
			if(Constant.ORDER_TYPE_KEY_3.equals(order.getType()))//获取网上置单价格
			{
				typeprice_1 = "m_price";
			}
			else if(Constant.ORDER_TYPE_KEY_5.equals(order.getType()))//上门收货的价格
			{
				typeprice_1 = "s_price";
			}
			else
			{
				return 0;//其它类型直接返回0表示不做计算
			}
			
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
			

			double commodityPrice = 0.0d;
			double lowestprice=0;
			double highestprice=0;
			double commodityweighttotal=0;
			double tariff=0;
			double other=0;
			double or=0;
			//List<OrderDetail> details = Arrays.asList(order.getDetails());
			List<M_OrderDetail> details=order.getDetail();
			for (M_OrderDetail detail : details) {
				try {
					String price="";
					try{
						price=this.commudityPriceDao.getPriceById(typeprice_1, detail.getCommodityId());//计算类型价格
						
						if(StringUtil.isEmpty(price)||StringUtil.string2Double(price)<=0)
						{
							price=this.commudityPriceDao.getPriceById(priceType, detail.getCommodityId());//以会员价格来计算
						}
						
					}
					catch(Exception e)
					{
						price=this.commudityPriceDao.getPriceById(priceType, detail.getCommodityId());//发生异常，以会员价格为准
					}
					
					detail.setCprice(price);
					
					
					if (price == null) {
						throw ExceptionUtil
								.handle2ServiceException("运单中含有系统中没有的商品价格类型！");
					}
					
					if(StringUtil.string2Double(price)<=0)//如果价格是0，直接返回0作为判断
					{
						throw ExceptionUtil
						.handle2ServiceException("运单中含有系统中没有的商品价格类型或价格没设置！");
					}

					if(lowestprice==0)
					{
						lowestprice=StringUtil.string2Double(price);
					}
					else
					{
						if(lowestprice>StringUtil.string2Double(price))
						{
							lowestprice=StringUtil.string2Double(price);
						}
					}
					
					if(highestprice==0)
					{
						highestprice=StringUtil.string2Double(price);
					}
					else
					{
						if(highestprice<StringUtil.string2Double(price))
						{
							highestprice=StringUtil.string2Double(price);
						}
					}
					commodityweighttotal=commodityweighttotal+StringUtil.string2Double(detail.getWeight());
					commodityPrice = commodityPrice
							+ StringUtil.string2Double(detail.getWeight())
							* StringUtil.string2Double(price);
					
					double smallallprice=StringUtil.string2Double(detail.getWeight())
							* StringUtil.string2Double(price);
					
					detail.setAllcprice(Double.toString(smallallprice));//小汇总
					
					
					tariff=tariff+StringUtil.string2Double(detail.getTariff());
					other=other+StringUtil.string2Double(detail.getOther());
					or=or+StringUtil.string2Double(detail.getOr());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw ExceptionUtil.handle2ServiceException("计算商品运费时出现异常！");
				}
			}

			
			order.setSjweight(String.valueOf(commodityweighttotal));
			order.setOther(String.valueOf(other));
			order.setTariff(String.valueOf(tariff));
			
			//double commodityweighttotal=StringUtil.string2Double(order.getWeight());
			double additiveweight=0.00;//附加重量
			if(commodityweighttotal<StringUtil.string2Double(lowest_weight_value))
			{
				if((!price_carry_flag.equalsIgnoreCase("0")))
				{
					additiveweight=StringUtil.string2Double(lowest_weight_value)-commodityweighttotal;
					//commodityweighttotal=StringUtil.string2Double(lowest_weight_value);
				}
				order.setWeight(lowest_weight_value);
			}
			else
			{
				//1：总重量进位，进位重量按价格最高的商品计算，小于部分将退位计算，退位部分按最高价格计算扣除。。

				//2：总重量进位，进位重量按价格最低的商品计算，小于部分将退位计算，退位部分按最低价格计算扣除。。

				//3：总重量进位，进位重量按价格最高的商品计算，小于部分按实际计算。

				//4：总重量进位，进位重量按价格最低的商品计算，小于部分按实际计算。。

				//注意：总重量不能低于，如果低于此值，将按此值来计算价格。
				if((price_carry_flag.equalsIgnoreCase("1"))||(price_carry_flag.equalsIgnoreCase("2"))||(price_carry_flag.equalsIgnoreCase("3"))||(price_carry_flag.equalsIgnoreCase("4")))
				{
					//double sum_jwweight=StringUtil.string2Double(order.getWeight());//总实际重量
					double sum_jwweight=commodityweighttotal;
					if((StringUtil.string2Double(jw_commodity_rate)>0)&&(StringUtil.string2Double(jw_commodity_rate)<1))
					{
						
						
						if((commodityweighttotal-((int)commodityweighttotal))>StringUtil.string2Double(jw_commodity_rate))
						{
							additiveweight=((int)commodityweighttotal)+1-sum_jwweight;
							order.setWeight(String.valueOf(((int)commodityweighttotal)+1));
						}
						else
						{

							if((price_carry_flag.equalsIgnoreCase("1"))||(price_carry_flag.equalsIgnoreCase("2")))//退位计算
							{
								additiveweight=((int)commodityweighttotal)-commodityweighttotal;//退位计算
								order.setWeight(String.valueOf(((int)commodityweighttotal)));
								//commodityweighttotal=j;
							}
							else if((price_carry_flag=="3")||(price_carry_flag=="4"))//按实际计算
							{
								additiveweight=0;
								order.setWeight(String.valueOf((commodityweighttotal)));
							}
							else
							{

								additiveweight=0;
								order.setWeight(String.valueOf((commodityweighttotal)));
							}
						}
						
						
					}
					else
					{
						
						//commodityweighttotal=commodityjwweighttotal;
						additiveweight=0;
						order.setWeight(String.valueOf(commodityweighttotal));
					}
					
				}
				else
				{
					order.setWeight(String.valueOf(commodityweighttotal));
				}
			}
			
			if(additiveweight<=-1)
			{
				additiveweight=0;
			}
			
			

			
			
					if(price_carry_flag.equalsIgnoreCase("1")||price_carry_flag.equalsIgnoreCase("3"))
					{
						additivePrice=additivePrice+highestprice*additiveweight;
					}
					else if(price_carry_flag.equalsIgnoreCase("2")||price_carry_flag.equalsIgnoreCase("4"))
					{
						additivePrice=additivePrice+lowestprice*additiveweight;
					}
			
			
			double premium = StringUtil.string2Double(order.getInsurance());

		

			BigDecimal money = new BigDecimal(premium);
			money=money.add(BigDecimal.valueOf(additivePrice));//添加附加价格
			money = money.add(BigDecimal.valueOf(other));
			money = money.add(BigDecimal.valueOf(or));
			money = money.add(BigDecimal.valueOf(tariff));
			//money = money.add(BigDecimal.valueOf(volumeMoney));
			money = money.add(BigDecimal.valueOf(commodityPrice));

			return money.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

			/* 运费计算 end */

		}
		
		
				
		
		
		
		
		
		// kai 20160427 添加成本计算接口,只计算商品的成本价，calculationM_orderFreight
		public double calculationM_OrderCostFreight(M_order order)
				throws ServiceException {
			if (order.getStoreId() == null) {
				throw ExceptionUtil.handle2ServiceException("门店为空，无法计算价格");
			}
			if((order.getDetail()==null)||(order.getDetail().size()<1))
			{
				throw ExceptionUtil.handle2ServiceException("商品信息为空，无法计算价格");
			}
			/* 运费计算 start */
			String priceType = "cost";

			double commodityPrice = 0.0d;
			List<M_OrderDetail> details = order.getDetail();
			for (M_OrderDetail detail : details) {
				try {
					String price =this.commudityPriceDao.getPriceById(priceType, detail.getCommodityId());
							//this.commodityDao.getPriceById(priceType,
						//	detail.getCommodityId());
					if (price == null) {
						throw ExceptionUtil
								.handle2ServiceException("运单中含有系统中没有的商品类型或参数,如成本！");
					}

					commodityPrice = commodityPrice
							+ StringUtil.string2Double(detail.getWeight())
							* StringUtil.string2Double(price);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw ExceptionUtil.handle2ServiceException("计算商品运费时出现异常！");
				}
			}
			return commodityPrice;

		}
		
		
		
		
		
		//读取全局变量的参数
		//id-----运单id号
		//widid--门店id
		//运单类型标识符
		

		
		
		
		//读取全局变量的参数
				//id-----运单id号
				//widid--门店id
				//运单类型标识符
		//userornot表示是不是会员，是为true,不是为否
				private String createOrderIdargnew(String id,String widid,String type_no,boolean userornot) throws ServiceException {
					
						try {
							
							//String prefix=this.globalargsDao.getcontentbyflag("print_order_first_2code");//获取首两字母
							
							int numbercount=Integer.parseInt(this.globalargsDao.getcontentbyflag("print_order_no_length"));//获取转运单中间数字的个数
							if(numbercount<8||numbercount>30){
								numbercount = 12;
							}
							
							
							
							
							StringBuffer sb = new StringBuffer((5+numbercount));//
							String widflag="";
							Warehouse house=this.warehouseDao.getById(widid);
							if(house!=null)
							{
							
								widflag=house.getPrintWidCode();//仓库标识
							}
							
								
							
							if(StringUtil.isEmpty(widflag))
							{
								widflag="00";
							}
							else if(widflag.length()==1)
							{
								widflag=widflag+"0";
							}
							else if(widflag.length()>2)
							{
								widflag.substring(0,2);//获取首两个字符
							}
							
							sb.append(widflag);//首两位是门店标识
							//下一位是随机位，会员是0-4，非会员是5-9
							int userflag=(int)(Math.random()*5);
							if(userornot)
							{}
							else//非会员
							{
								userflag=userflag+5;//非会员取值
								if(userflag>9)
								{
									userflag=9;
								}
							}
							
							sb.append(userflag);//加一个会员标志
							
							// 添加前缀和时间缩写
							sb.append(DateUtil.date2String(new Date(), "yy"));
							// 添加递增数字，4位
							sb.append(StringUtil.getTruncationString(id, numbercount-7));
							// 添加随机数字，1位
							sb.append(StringUtil.generateRandomInteger(1));
							
							
							
							if(Constant.ORDER_TYPE_KEY_1.equalsIgnoreCase(type_no))//门市
							{
								sb.append("0");
							}
							else if(Constant.ORDER_TYPE_KEY_2.equalsIgnoreCase(type_no))//转运运单
							{
								sb.append("1");
							}
							else if(Constant.ORDER_TYPE_KEY_3.equalsIgnoreCase(type_no))//网上置单
							{
								sb.append("2");
							}
							else if(Constant.ORDER_TYPE_KEY_4.equalsIgnoreCase(type_no))//第三方运单
							{
								sb.append("3");
							}
							else if(Constant.ORDER_TYPE_KEY_5.equalsIgnoreCase(type_no))//上门收货
							{
								sb.append("4");
							}
							else if(Constant.ORDER_TYPE_KEY_6.equalsIgnoreCase(type_no))//手写单，空运单
							{
								sb.append("5");
							}
							else if(Constant.ORDER_TYPE_KEY_7.equalsIgnoreCase(type_no))//批量生成门市运单
							{
								sb.append("6");
							}
							else 
							{
								sb.append("9");
							}
							
					
							return sb.toString().toUpperCase();//一定要大写，否则条形码可能显示不出来
						} catch (Exception e) {
							throw ExceptionUtil.handle2ServiceException("运单号创建异常，请重试", e);
						}

				

				}
				
				
				
				//读取全局变量的参数
				//id-----运单id号
				//widid--门店id
				//运单类型标识符
		        //直接写死以MT开头的美淘定义
				public String createOrderIdargnewMT(String id,String widid,String type_no) throws ServiceException {
					
						try {
							
							//String prefix=this.globalargsDao.getcontentbyflag("print_order_first_2code");//获取首两字母
							
							int numbercount=Integer.parseInt(this.globalargsDao.getcontentbyflag("print_order_no_length"));//获取转运单号长度
							if(numbercount<8||numbercount>30){
								numbercount = 12;
							}
							
							
							
							
							StringBuffer sb = new StringBuffer((numbercount));//
							String widflag="";
							Warehouse house=this.warehouseDao.getById(widid);
							if(house!=null)
							{
							
								widflag=house.getPrintWidCode();//仓库标识
							}
							
								
							sb.append("MT");//美淘开头的运单号						
							
							if(Constant.ORDER_TYPE_KEY_1.equalsIgnoreCase(type_no))//门市
							{
								sb.append("0");
							}
							else if(Constant.ORDER_TYPE_KEY_2.equalsIgnoreCase(type_no))//转运运单
							{
								sb.append("1");
							}
							else if(Constant.ORDER_TYPE_KEY_3.equalsIgnoreCase(type_no))//网上置单
							{
								sb.append("2");
							}
							else if(Constant.ORDER_TYPE_KEY_4.equalsIgnoreCase(type_no))//第三方运单
							{
								sb.append("3");
							}
							else if(Constant.ORDER_TYPE_KEY_5.equalsIgnoreCase(type_no))//上门收货
							{
								sb.append("4");
							}
							else if(Constant.ORDER_TYPE_KEY_6.equalsIgnoreCase(type_no))//手写单，空运单
							{
								sb.append("5");
							}
							else if(Constant.ORDER_TYPE_KEY_7.equalsIgnoreCase(type_no))//批量生成门市运单
							{
								sb.append("6");
							}
							else 
							{
								sb.append("9");
							}
							// 添加递增数字，由数据库id定义
							sb.append(StringUtil.getTruncationString(id, numbercount-5));
							if(StringUtil.isEmpty(widflag))
							{
								widflag="00";
							}
							else if(widflag.length()==1)
							{
								widflag=widflag+"0";
							}
							else if(widflag.length()>2)
							{
								widflag.substring(0,2);//获取首两个字符
							}
							sb.append(widflag);//最后两位是门店标识
							return sb.toString().toUpperCase();//一定要大写，否则条形码可能显示不出来
						} catch (Exception e) {
							throw ExceptionUtil.handle2ServiceException("运单号创建异常，请重试", e);
						}

				

				}			
		
		
		public ResponseObject<Object> payM_order(List<String> ids, List<String> orderIds,
				String userId, String amount, double newrmb, double newusd,
				boolean accountPay,M_order order) throws ServiceException {
			try {
				String date = DateUtil.date2String(new Date());
				int i=this.m_orderDao.modifyMorderPay(ids, date,
						Constant.ORDER_STATE3,Constant.ORDER_PAY_STATE1);
				
				if (i == ids.size()) {
					// 提交一个账户更改详情
					AccountDetail detail = new AccountDetail();
					detail.setAmount(amount);
					detail.setCreateDate(date);
					detail.setModifyDate(date);
					detail.setState(Constant.ACCOUNT_DETAIL_STATE2);
					detail.setCurrency("美元");
					detail.setName("支付运费");
					detail.setType(Constant.ACCOUNT_DETAIL_TYPE2);
					detail.setUserId(userId);
					detail.setConfirm_state("0");
					if(order!=null)
					{
						Employee emp=this.employeeDao.getEmployeeById(order.getEmployeeId());
						if(emp!=null)
						{
							detail.setEmpId(order.getEmployeeId());
							detail.setEmpName(emp.getAccount());
						}
						
						Warehouse ware=this.warehouseDao.getById(order.getStoreId());
						if(ware!=null)
						{
							detail.setEmpStore(ware.getName());
							detail.setStoreId(ware.getId());
						}
					}
					
					
					String prefix = "现金支付";

					if (accountPay) {
						// 账户支付，修改账户余额
						Account account = new Account();
						account.setUsd(String.valueOf(newusd));
						account.setRmb(String.valueOf(newrmb));
						account.setUserId(userId);
						account.setModifyDate(date);
						if (this.accountDao.modifyAccount(account) > 0) {
							// pass
						} else {
							throw new Exception();
						}
						prefix = "帐户余额支付";
					}
					detail.setRemark(prefix + "；运单id：" + orderIds);
					this.accountDetailDao.insertAccountDetail(detail);

					for (String orderId : orderIds) {
						Route route = new Route();
						route.setDate(date);
						route.setOrderId(orderId);
						route.setRemark(prefix);
						route.setState(Constant.ORDER_ROUTE_STATE3);
						this.routeDao.insertRoute(route);
					}
				} else {
					throw new Exception();
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
			return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
		}
		
		
		
		//进行支付操作并标记指定路由状态
		public ResponseObject<Object> payM_order_state(List<String> ids, List<String> orderIds,
				String userId, String amount, double newrmb, double newusd,
				boolean accountPay,String state,String empid,String storeid) throws ServiceException {
			try {
				String date = DateUtil.date2String(new Date());
				int i=this.m_orderDao.modifyMorderPay(ids, date,
						state,Constant.ORDER_PAY_STATE1);
				
				if (i == ids.size()) {
					// 提交一个账户更改详情
					AccountDetail detail = new AccountDetail();
					detail.setAmount(amount);
					detail.setCreateDate(date);
					detail.setModifyDate(date);
					detail.setState(Constant.ACCOUNT_DETAIL_STATE2);
					detail.setCurrency("美元");
					detail.setName("支付运费");
					detail.setType(Constant.ACCOUNT_DETAIL_TYPE2);
					detail.setUserId(userId);
					detail.setConfirm_state("0");
					String prefix = "现金支付";
					
					
					Employee emp=this.employeeDao.getEmployeeById(empid);
					if(emp!=null)
					{
						detail.setEmpId(empid);
						detail.setEmpName(emp.getAccount());
					}
					
					Warehouse ware=this.warehouseDao.getById(storeid);
					if(ware!=null)
					{
						detail.setEmpStore(ware.getName());
						detail.setStoreId(ware.getId());
					}

					if (accountPay) {
						// 账户支付，修改账户余额
						Account account = new Account();
						account.setUsd(String.valueOf(newusd));
						account.setRmb(String.valueOf(newrmb));
						account.setUserId(userId);
						account.setModifyDate(date);
						if (this.accountDao.modifyAccount(account) > 0) {
							// pass
						} else {
							throw new Exception();
						}
						prefix = "帐户余额支付";
					}
					detail.setRemark(prefix + "；运单id：" + orderIds);
					this.accountDetailDao.insertAccountDetail(detail);

					for (String orderId : orderIds) {
						Route route = new Route();
						route.setDate(date);
						route.setOrderId(orderId);
						route.setRemark(prefix);
						route.setState(OrderUtil.transformerState(0, state));
						this.routeDao.insertRoute(route);
					}
				} else {
					throw new Exception();
				}
			} catch (Exception e) {
				//throw ExceptionUtil.handle2ServiceException(e);
				 return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION," 异常！"+e.getMessage());
			}
			return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
		}
		//获取汇率
		public ResponseObject<String> getCurUsaToCn() throws ServiceException {
			try {

				String usatoch = this.globalargsDao.getcontentbyflag("cur_usa_cn");
				ResponseObject<String> result;
				if ((usatoch != null) && (!usatoch.equalsIgnoreCase(""))) {
					result = new ResponseObject<String>(ResponseCode.SUCCESS_CODE);
					result.setData(usatoch);
				} else {
					result = new ResponseObject<String>(
							ResponseCode.PARAMETER_ERROR, "参数无效");
					result.setData("");

				}
				return result;
			} catch (Exception e) {
				return new ResponseObject<String>(ResponseCode.ORDER_INSERT_ERROR,
						"插入运单失败");

			}
		}
		
		
		
		public String sendEnglishMessage(String message,String phone)
		{
			try{
				
				String companyurl = globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_COMPANY_SEBSITE_URL);
				if((!StringUtil.isEmpty(companyurl))&&(companyurl.length()>3))
				{
					message=message+",Website:"+companyurl;
				}
				String content="Dear Customer,"+message+". Please DO NOT reply this message.";
				//String phone="";
				String companyTitle="";
				if(phone.length()==10)
				{
					phone="+1"+phone;
					companyTitle = globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_COMPANY_TITLE_EN);
				}
				/*else if(phone.length()==11)
				{
					phone="+86"+phone;
					companyTitle = globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_COMPANY_TITLE_CN);
				}*/
				else//电话号码填写不正确
				{
				    return "电话号码填写不正确!";
				}
				if(!phone.equalsIgnoreCase(""))
				{
					content="【"+companyTitle+"】"+content;
					SimpleSmsSender simpleSmsSender = new SimpleSmsSender(globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_API_KEY));
					String aaa=simpleSmsSender.sendSms(content,phone);
					aaa="["+aaa+"]";
					List<Map<String, String>> list1 = jsonStringToList(aaa);
					String code1="";
					String msg1="";
					String result1="";
					for(Map<String, String> a:list1)
					{
						 code1=a.get("code");
						 msg1=a.get("msg");
						 result1=a.get("result");
					}
					
					
					log.info(aaa);
					if(code1.equals("0"))//表示发送成功
					{
						return "0";
					}
					else
					{
						return msg1+","+result1;
					}
				}
				else
				{
					return "电话号码不正确!";
				}
				
			}catch(Exception e)
			{
				return "失送出现异常";
			}
		}
		
		public String sendChinaMessage(String message,String phone)
		{
			try{
				String companyurl = globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_COMPANY_SEBSITE_URL);
				if((!StringUtil.isEmpty(companyurl))&&(companyurl.length()>3))
				{
					message=message+",详情请看:"+companyurl;
				}
				String content="亲爱的用户，"+message+"。请勿回复本信息。";
				//String phone="";
				String companyTitle="";
				/*if(phone.length()==10)
				{
					phone="+1"+phone;
					companyTitle = globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_COMPANY_TITLE_EN);
				}*/
				if(phone.length()==11)
				{
					phone="+86"+phone;
					companyTitle = globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_COMPANY_TITLE_CN);
				}
				else//电话号码填写不正确
				{
				    return "电话号码填写不正确!";
				}
				if(!phone.equalsIgnoreCase(""))
				{
					content="【"+companyTitle+"】"+content;
					SimpleSmsSender simpleSmsSender = new SimpleSmsSender(globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_API_KEY));
					String aaa=simpleSmsSender.sendSms(content,phone);
					aaa="["+aaa+"]";
					List<Map<String, String>> list1 = jsonStringToList(aaa);
					String code1="";
					String msg1="";
					String result1="";
					for(Map<String, String> a:list1)
					{
						 code1=a.get("code");
						 msg1=a.get("msg");
						 result1=a.get("result");
					}
					
					
					log.info(aaa);
					if(code1.equals("0"))//表示发送成功
					{
						return "0";
					}
					else
					{
						return msg1+","+result1;
					}
				}
				else
				{
					return "电话号码不正确!";
				}
				
			}catch(Exception e)
			{
				return "失送出现异常";
			}
		}
		
		//发送身份证信息
		public String sendChinaMessageCardid(String message,String phone)
		{
			try{
				String companyurl = globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_COMPANY_SEBSITE_URL);
				if((!StringUtil.isEmpty(companyurl))&&(companyurl.length()>3))
				{
					message=message+"，详情请看"+companyurl;
				}
				String content="亲："+message+"。请勿回复";
				//String phone="";
				String companyTitle="";
				/*if(phone.length()==10)
				{
					phone="+1"+phone;
					companyTitle = globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_COMPANY_TITLE_EN);
				}*/
				if(phone.length()==11)
				{
					phone="+86"+phone;
					companyTitle = globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_COMPANY_TITLE_CN);
				}
				else//电话号码填写不正确
				{
				    return "电话号码填写不正确!";
				}
				if(!phone.equalsIgnoreCase(""))
				{
					content="【"+companyTitle+"】"+content;
					SimpleSmsSender simpleSmsSender = new SimpleSmsSender(globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_API_KEY));
					String aaa=simpleSmsSender.sendSms(content,phone);
					aaa="["+aaa+"]";
					List<Map<String, String>> list1 = jsonStringToList(aaa);
					String code1="";
					String msg1="";
					String result1="";
					for(Map<String, String> a:list1)
					{
						 code1=a.get("code");
						 msg1=a.get("msg");
						 result1=a.get("result");
					}
					
					
					log.info(aaa);
					if(code1.equals("0"))//表示发送成功
					{
						return "0";
					}
					else
					{
						return msg1+","+result1;
					}
				}
				else
				{
					return "电话号码不正确!";
				}
				
			}catch(Exception e)
			{
				return "失送出现异常";
			}
		}
		
		//发送美淘揽收信息
		public String sendChinaMeitaoMessage(String message,String phone)
		{
			try{
				String companyurl = globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_COMPANY_SEBSITE_URL);
				if((!StringUtil.isEmpty(companyurl))&&(companyurl.length()>3))
				{
					message=message+"，详情:"+companyurl;
				}
				String content="亲："+message+"。请勿回复";
				//String phone="";
				String companyTitle="";
				/*if(phone.length()==10)
				{
					phone="+1"+phone;
					companyTitle = globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_COMPANY_TITLE_EN);
				}*/
				if(phone.length()==11)
				{
					phone="+86"+phone;
					companyTitle = globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_COMPANY_TITLE_CN);
				}
				else//电话号码填写不正确
				{
				    return "电话号码填写不正确!";
				}
				if(!phone.equalsIgnoreCase(""))
				{
					content="【"+companyTitle+"】"+content;
					SimpleSmsSender simpleSmsSender = new SimpleSmsSender(globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_API_KEY));
					String aaa=simpleSmsSender.sendSms(content,phone);
					aaa="["+aaa+"]";
					List<Map<String, String>> list1 = jsonStringToList(aaa);
					String code1="";
					String msg1="";
					String result1="";
					for(Map<String, String> a:list1)
					{
						 code1=a.get("code");
						 msg1=a.get("msg");
						 result1=a.get("result");
					}
					
					
					log.info(aaa);
					if(code1.equals("0"))//表示发送成功
					{
						return "0";
					}
					else
					{
						return msg1+","+result1;
					}
				}
				else
				{
					return "电话号码不正确!";
				}
				
			}catch(Exception e)
			{
				return "失送出现异常";
			}
		}
		
		//在连接后面加空格
		public String sendChinaMessage_havenull(String message,String phone)
		{
			try{
				String companyurl = globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_COMPANY_SEBSITE_URL);
				if((!StringUtil.isEmpty(companyurl))&&(companyurl.length()>3))
				{
					message=message+",详情请看:"+companyurl;
				}
				String content="亲："+message+"。 请勿回复本信息。";
				//String phone="";
				String companyTitle="";
				/*if(phone.length()==10)
				{
					phone="+1"+phone;
					companyTitle = globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_COMPANY_TITLE_EN);
				}*/
				if(phone.length()==11)
				{
					phone="+86"+phone;
					companyTitle = globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_COMPANY_TITLE_CN);
				}
				else//电话号码填写不正确
				{
				    return "电话号码填写不正确!";
				}
				if(!phone.equalsIgnoreCase(""))
				{
					content="【"+companyTitle+"】"+content;
					SimpleSmsSender simpleSmsSender = new SimpleSmsSender(globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_API_KEY));
					String aaa=simpleSmsSender.sendSms(content,phone);
					aaa="["+aaa+"]";
					List<Map<String, String>> list1 = jsonStringToList(aaa);
					String code1="";
					String msg1="";
					String result1="";
					for(Map<String, String> a:list1)
					{
						 code1=a.get("code");
						 msg1=a.get("msg");
						 result1=a.get("result");
					}
					
					
					log.info(aaa);
					if(code1.equals("0"))//表示发送成功
					{
						return "0";
					}
					else
					{
						return msg1+","+result1;
					}
				}
				else
				{
					return "电话号码不正确!";
				}
				
			}catch(Exception e)
			{
				return "失送出现异常";
			}
		}
		
		//添加门市运单
		public ResponseObject<Object> add_ms_Morder(M_order order) throws ServiceException
		{
			
			if(order==null)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错!");
			}
			try {
				String date = DateUtil.date2String(new Date());
				order.setCreateDate(date);
				order.setModifyDate(date);
				
				
				
				if(order.getRuser()!=null)
				{
					order.getRuser().setCreateDate(date);
					order.getRuser().setModifyDate(date);
					order.getRuser().setUserId(order.getUserId());
					int k=this.receive_UserDao.insertReceiveUser(order.getRuser());//插入收件人信息
					
					  if(k>0)
					  {
						  order.setRuserId(order.getRuser().getId());
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
				if(order.getSuser()!=null)
				{
					order.getSuser().setCreateDate(date);
					order.getSuser().setModifyDate(date);
					order.getSuser().setUserId(order.getUserId());
				  int k=this.send_UserDao.insertSendUser(order.getSuser());//插入发件人信息
				  if(k>0)
				  {
					  order.setSuserId(order.getSuser().getId());
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
				
				if(!StringUtil.isEmpty(order.getPrintway())&&(order.getPrintway().equalsIgnoreCase("1")))//热敏4x6海关打印
				{
					//获取海关单
					SeaNumber seano=seaNumberDao.getone();
					if((seano==null)||(StringUtil.isEmpty(seano.getOrderId())))
					{
						if(seano!=null)
						{
							seano.setState("1");
							seano.setModifyDate(date);
							int a=seaNumberDao.updatestate(seano);
						}
						throw new Exception("获取海关号失败!");
					}else
					{
						order.setSorderId(seano.getOrderId());
						seano.setState("1");
						seano.setModifyDate(date);
						int a=seaNumberDao.updatestate(seano);
						if(a==0)
						{
							throw new Exception("修改海关单状态失败!");
						}
					}
					
					//匹配身份证
				}
				
				
				
				order.setPayornot(Constant.ORDER_PAY_STATE0);//设置为未付款状态，1表示已经付款，其它表示付款异常状态
				int k=this.m_orderDao.insertMorder(order);
				
				if(k==0)
				{
					throw new Exception("插入运单失败");
				}
				//String orderid=this.createOrderIdarg(order.getId(),order.getStoreId(),order.getType());//获取运单的运单号
				String orderid="";
				if(StringUtil.isEmpty(order.getUserId())||order.getUserId().equalsIgnoreCase("-1"))
				{
				   // orderid=this.createOrderIdargnew(order.getId(),order.getStoreId(),order.getType(),false);//获取运单的运单号
					 orderid=this.createOrderIdargnewMT(order.getId(),order.getStoreId(),order.getType());//获取美淘运单的运单号
				    
				}
				else
				{
					//orderid=this.createOrderIdargnew(order.getId(),order.getStoreId(),order.getType(),true);//获取运单的运单号
					orderid=this.createOrderIdargnewMT(order.getId(),order.getStoreId(),order.getType());//获取美淘运单的运单号
				}
				
				order.setOrderId(orderid);
				int kk=this.m_orderDao.updateOrderid(order.getId(),orderid);
				if(kk==0)
				{
					throw new Exception("修改改运单号失败!");
				}
				//开始插入商品信息
				for(M_OrderDetail m:order.getDetail())
				{
					m.setOrderId(orderid);
					m.setCtype(order.getType());
				}
				int mm=this.m_DetailDao.insertMDetail(order.getDetail());
				
				if(mm==order.getDetail().size())
				{
					User user=null;
					if(!StringUtil.isEmpty(order.getUserId()))
					{
						user=this.userDao.getUserById(order.getUserId());
					}
					//开始扣除费用记录
					if ("0".equals(order.getPayWay())) {//余额支付
					
						if(user==null)
						{
							throw new Exception("用户账户不存在，不能进行余额支付!");
						}
						double totalMoney = new BigDecimal(order.getTotalmoney())
								.doubleValue();
						if(totalMoney<0)
						{
							throw new Exception("运费不能小于0!");
						}
						
						double rmb = StringUtil.string2Double(user.getRmbBalance());
						double usd = StringUtil.string2Double(user.getUsdBalance());
						
						double usdrate=0;
						ResponseObject<String> obj=this.getCurUsaToCn();
						if((obj.getCode()!=null)&&(obj.getCode().equalsIgnoreCase(ResponseCode.SUCCESS_CODE)))
						{
							if((obj.getData()!=null)&&(!obj.getData().equals("")))
							{
								String money00=obj.getData();
								double dou = Double.parseDouble(money00);
								if(dou>0)
								{
									usdrate=dou;
								}
							}
						}
						
					
						
							double newusd = usd - totalMoney; // 先用美元支付
							double newrmb = rmb;
							if (newusd >= 0) {
								
								ResponseObject<Object> obj1=this.payM_order(Arrays
									.asList(new String[] { order.getId() }), Arrays
									.asList(new String[] { order.getOrderId() }),
									order.getUserId(), order.getTotalmoney(),
									newrmb, newusd, true,order);
								if(!obj1.getCode().equalsIgnoreCase(ResponseCode.SUCCESS_CODE))
								{
									throw new Exception("支付出现异常");
								}
								
								// ignore
							
							} else {
								newusd = 0.0D; // 美元余额全部支付，开始扣人民币的钱
								newrmb = new BigDecimal(rmb - (totalMoney - usd)
										* usdrate).setScale(2,
										BigDecimal.ROUND_HALF_UP).doubleValue();
								if(newrmb<0)
								{
									throw new Exception("余额不足，支付失败!");
								}
								ResponseObject<Object> obj1=this.payM_order(Arrays
										.asList(new String[] { order.getId() }), Arrays
										.asList(new String[] { order.getOrderId() }),
										order.getUserId(), order.getTotalmoney(),
										newrmb, newusd, true,order);
								if(!obj1.getCode().equalsIgnoreCase(ResponseCode.SUCCESS_CODE))
								{
									throw new Exception("支付出现异常");
								}
							}
					}
					else if("1".equals(order.getPayWay()))//现金支付
					{
						String userid="-1";
						if(!StringUtil.isEmpty(order.getUserId()))
						{
							userid=order.getUserId();
						}
						ResponseObject<Object> obj1=this.payM_order(Arrays
								.asList(new String[] { order.getId() }), Arrays
								.asList(new String[] { order.getOrderId() }),
								userid, order.getTotalmoney(),
								0, 0, false,order);
						if(!obj1.getCode().equalsIgnoreCase(ResponseCode.SUCCESS_CODE))
						{
							throw new Exception("支付出现异常");
						}
					}
					else
					{
						throw new Exception("支付方式不正确");
					}
							
					
					
					
					
					// 插入完成后，新建一个Route并插入到数据库中
					/*Route route = new Route();
					route.setOrderId(orderid);
					route.setName("");
					route.setDate(date);
					route.setState(OrderUtil.transformerState(0,
							order.getState()));
					route.setRemark("门市添加，运单生成");
					this.routeDao.insertRoute(route);*/
				}
				else
				{
					throw new Exception("插入商品信息出错!");
				}
				//最后判断是否给用户发送短信
				if((order.getAutomessage()!=null)&&(order.getAutomessage().equalsIgnoreCase("1")))
				{
					try{
						int rmb_nos=0;
						int usa_nos=0;
						if(order.getSuser().getPhone().length()==10)
						{
							String message="your tracking number:"+order.getOrderId()+",Price:$"+order.getTotalmoney()+",Weight:"+order.getWeight()+" lb";
							this.sendEnglishMessage(message, order.getSuser().getPhone());
							usa_nos++;
						}
						else
						{
							String message="你的运单号为:"+order.getOrderId()+",价格:$"+order.getTotalmoney()+",重量："+order.getWeight()+"磅";
							this.sendChinaMessage(message, order.getSuser().getPhone());
							rmb_nos++;
							
						}
						try{
							MessageRecord record =new MessageRecord();
							record.setModifyDate(date);
							record.setRemark("操作人号："+order.getEmployeeId()+",运单号："+order.getOrderId());
							record.setCreateDate(date);
							record.setRmb_nos(String.valueOf(rmb_nos));
							record.setUsa_nos(String.valueOf(usa_nos));
							record.setType(Constant.SEND_MESSAGE_TYPE5);//
							this.messageRecordDao.insert(record);
						}catch(Exception e)
						{
							
						}
						
						
						/*String content="Dear Customer,your tracking number:"+order.getOrderId()+",Price:$"+order.getTotalmoney()+". Please DO NOT reply this message.";
						String phone="";
						String companyTitle="";
						if(order.getSuser().getPhone().length()==10)
						{
							phone="+1"+order.getSuser().getPhone();
							companyTitle = globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_COMPANY_TITLE_EN);
						}
						else if(order.getSuser().getPhone().length()==11)
						{
							phone="+86"+order.getSuser().getPhone();
							companyTitle = globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_COMPANY_TITLE_CN);
						}
						else//电话号码填写不正确
						{
						
						}
						if(!phone.equalsIgnoreCase(""))
						{
							content="【"+companyTitle+"】"+content;
							SimpleSmsSender simpleSmsSender = new SimpleSmsSender(globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_API_KEY));
							String aaa=simpleSmsSender.sendSms(content,phone);
							aaa="["+aaa+"]";
							List<Map<String, String>> list1 = jsonStringToList(aaa);
							String code1="";
							String msg1="";
							String result1="";
							for(Map<String, String> a:list1)
							{
								 code1=a.get("code");
								 msg1=a.get("msg");
								 result1=a.get("result");
							}
							
							
							log.info(aaa);
							if(code1.equals("0"))//表示发送成功
							{
								
							}
							else
							{
							
							}
						}*/
					}catch(Exception e)
					{}
				}
				//Double.parseDouble("aaaaa");//测试异常使用
				ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
				obj.setData(order.getId());
				return obj;
			}
			 catch (Exception e) {
				// throw ExceptionUtil.handle2ServiceException(e);
				 return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"操作发生异常");
					//throw ExceptionUtil.handle2ServiceException(e.getMessage());
				}
		}
		
		private static List<Map<String, String>> jsonStringToList(String rsContent) throws Exception
	    {
	        JSONArray arry = JSONArray.fromObject(rsContent);
	        System.out.println("json字符串内容如下");
	        System.out.println(arry);
	        List<Map<String, String>> rsList = new ArrayList<Map<String, String>>();
	        for (int i = 0; i < arry.size(); i++)
	        {
	            JSONObject jsonObject = arry.getJSONObject(i);
	            Map<String, String> map = new HashMap<String, String>();
	            for (Iterator<?> iter = jsonObject.keys(); iter.hasNext();)
	            {
	                String key = (String) iter.next();
	                String value = jsonObject.get(key).toString();
	                map.put(key, value);
	            }
	            rsList.add(map);
	        }
	        return rsList;
	    }
		//根据id获取运单号
		public ResponseObject<Object> getonebyid(String id,String storeId) throws ServiceException
		{
			if(StringUtil.isEmpty(id))
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数不能为空!");
			}
			try{
				M_order order=this.m_orderDao.getById(id, storeId);
				if(order==null)
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"没有查找到对应的运单!");
				}
				String date = DateUtil.date2String(new Date());
				order.setNowtime(date);
				ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
				obj.setData(order);
				return obj;
					
			} catch (Exception e) {
				return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"查找发生异常!");
				//throw ExceptionUtil.handle2ServiceException(e.getMessage());
			}
		}
		
		//kai 20160324 查找运单接口函数
		public ResponseObject<PageSplit<M_order>> searchMorders(String oid,String sod,String god,String flyno,String wid,String cid,String userinfo,String commudityinfo,String state,String type,String payway,String sdate,String edate, String psdate,String pedate, String cardinfo,String payornot,int pageSize, int pageNow)
		        throws ServiceException{
			try {
				///
				String cardurl=null;//身份证正面图片
				String cardother=null;//身份证反面图片
				String cardtogether=null;//身份证合成图片
				String cardid=null;//身份证号
				String uploadflag=null;//用于保存与身份证号相对应的名称
				if(StringUtil.isEmpty(userinfo))
				{
					userinfo=null;
				}
				else
				{
					userinfo = StringUtil.escapeStringOfSearchKey(userinfo);
				}
				if(StringUtil.isEmpty(commudityinfo))
				{
					commudityinfo=null;
				}
				else
				{
					commudityinfo = StringUtil.escapeStringOfSearchKey(commudityinfo);
				}
				if(StringUtil.isEmpty(oid))
				{
					oid=null;
				}
				else
				{
					oid = StringUtil.escapeStringOfSearchKey(oid);
				}
				if(StringUtil.isEmpty(sod))
				{
					sod=null;
				}
				else
				{
					sod = StringUtil.escapeStringOfSearchKey(sod);
				}
				if(StringUtil.isEmpty(god))
				{
					god=null;
				}
				else
				{
					god = StringUtil.escapeStringOfSearchKey(god);
				}
				
				if(StringUtil.isEmpty(cardinfo))
				{
					cardinfo=null;
				}
				else
				{
					if(userinfo==null)
					{
						userinfo="%";
					}
					String card[]=cardinfo.split(",");
					for(int i=0;i<card.length;i++)
					{
						if(!StringUtil.isEmpty(card[i]))
						{
							if(card[i].equals("0"))
							{
								cardid="1";//仅用于标识是否存在
							}
							else if(card[i].equals("1"))
							{
								cardurl="1";//仅用于标识是否存在
							}
							else if(card[i].equals("2"))
							{
								cardother="1";//仅用于标识是否存在
							}
							else if(card[i].equals("3"))
							{
								cardtogether="1";//仅用于标识是否存在
							}
							else if(card[i].equals("4"))
							{
								uploadflag=Constant.UPLOAD_CARD_TYPE0;
							}
								
							
							
						}
					}
				}

				int rowCount = 0;
				try {

					rowCount = this.m_orderDao.countOfSearchMOrders(oid, sod, god, flyno, wid, cid, userinfo, commudityinfo, state, type,payway, sdate, edate, psdate, pedate,cardurl,cardother,cardtogether,cardid,uploadflag,payornot);
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("获取运单个数失败", e);
				}

				ResponseObject<PageSplit<M_order>> responseObj = new ResponseObject<PageSplit<M_order>>(
						ResponseCode.SUCCESS_CODE);
				if (rowCount > 0) {
					pageSize = Math.max(pageSize, 1);
					int pageCount = rowCount / pageSize
							+ (rowCount % pageSize == 0 ? 0 : 1);
					pageNow = Math.min(pageNow, pageCount);
					PageSplit<M_order> pageSplit = new PageSplit<M_order>();
					pageSplit.setPageCount(pageCount);
					pageSplit.setPageNow(pageNow);
					pageSplit.setRowCount(rowCount);
					pageSplit.setPageSize(pageSize);

					int startIndex = (pageNow - 1) * pageSize;
					try {
						List<M_order> orders = this.m_orderDao.SearchMOrders(oid, sod, god, flyno, wid, cid, userinfo, commudityinfo, state, type,payway, sdate, edate, psdate, pedate,cardurl,cardother,cardtogether,cardid,uploadflag,payornot, startIndex, pageSize);
						
						for(M_order o: orders)
						{
							if(!StringUtil.isEmpty(o.getUserId()))
							{
								o.setUser(this.userDao.getUserById(o.getUserId()));
							}
							List<String> list=this.morder_TorderDao.gettorderIds(o.getOrderId());
							if(list!=null&&list.size()>0)
							{
								List<T_order> torders=new ArrayList<T_order>();
								for(String tid:list)
								{
									T_order torder=this.torderDao.getone(null,tid);
									if(torder!=null)
									{
										torders.add(torder);
									}
								}
								o.setTorders(torders);
							}
							
							if(!StringUtil.isEmpty(o.getPositionId()))
							{
								Shelves_position position=this.shelves_positionDao.getbyid(o.getPositionId());
								o.setSposition(position);
							}
							try{
								if(!StringUtil.isEmpty(o.getFreezId())&&(Integer.parseInt(o.getFreezId())>0))
								{
									FreezeMoney f=this.freezeMoneyDao.getbyid(o.getFreezId());
									o.setFreezeMoney(f);
								}
							}catch(Exception e){
								
							}
							
							/*if((o.getRuser()!=null))
							{
								if(!StringUtil.isEmpty(o.getRuser().getCardurl()))
								{
									o.getRuser().setCardurl(java.net.URLEncoder.encode(o.getRuser().getCardurl(), "utf-8"));
								}
								if(!StringUtil.isEmpty(o.getRuser().getCardother()))
								{
									o.getRuser().setCardother(java.net.URLEncoder.encode(o.getRuser().getCardother(), "utf-8"));
								}
								if(!StringUtil.isEmpty(o.getRuser().getCardtogether()))
								{
									o.getRuser().setCardtogether(java.net.URLEncoder.encode(o.getRuser().getCardtogether(), "utf-8"));
								}
								
							}*/
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
			} catch (ServiceException e) {
				//throw e;
				return new ResponseObject<PageSplit<M_order>>(ResponseCode.SHOW_EXCEPTION,"查找发生异常！");
			}
		}
		
		
		
		public ResponseObject<Object> deleteMorderByIds(List<String> ids,
				List<String> states,String storeId) throws ServiceException {
			if (ids == null || ids.isEmpty() || states == null || states.isEmpty()) {
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
						"参数无效");
			}

			try {
				
				// 删除运单
				List<String> orderids=this.m_orderDao.getOrderIdsbyIds(ids);
				if((orderids==null)||(orderids.size()!=ids.size()))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
							"删除失败，请刷新后重新操作!");
				}
				int i=this.m_orderDao.deleteByIds(ids, states, storeId);
				
				//int i = this.orderDao.deleteByOrderIds(ids, states);
				if (i == ids.size()) {
					// 删除运单详情
					int a=this.m_DetailDao.deleteByOrderIds(orderids);
					
					// 删除了路由信息
					int b=this.routeDao.deleteRouteByOrderIds(orderids);
					return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
				} else {
					// 进行事务回滚
					throw new Exception();
				}
			} catch (Exception e) {
				//throw ExceptionUtil.handle2ServiceException(e);
				return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION," 删除运单运单发生异常！");
			}
		}
		
		
		//删除运单信息
			public ResponseObject<Object> modifyroutestate(String id,String state,String thirdPNS,String thirdNo,String storeId) throws ServiceException
			{
				if(StringUtil.isEmpty(id)||StringUtil.isEmpty(state))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数不能为空!");
				}
				try{
					int k=this.m_orderDao.modifyroutestate(id, state, thirdPNS, thirdNo, storeId);
					if(k==0)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"删除失败!");
					}
					String orderid=this.m_orderDao.getOrderIdbyId(id);
					String date = DateUtil.date2String(new Date());
					Route route = new Route();
					route.setDate(date);
					route.setOrderId(orderid);
					if(!StringUtil.isEmpty(thirdPNS)&&(!StringUtil.isEmpty(thirdNo)))
					{
						//route.setRemark("修改状态,第三方快递公司："+thirdPNS+",单号："+thirdNo);
						route.setRemark("修改状态");
					}
					else
					{
						route.setRemark("修改状态");
					}
					
					route.setState(OrderUtil.transformerState(0,state));
					this.routeDao.insertRoute(route);
					
					ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
					
					return obj;
						
				} catch (Exception e) {
					return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION," 修改状态发生异常！");
					//throw ExceptionUtil.handle2ServiceException(e.getMessage());
				}
			}
			
			
	public ResponseObject<Object> getoneMorderRoute(String kuaidiType,String orderId,String storeId) throws ServiceException
	{
		if (kuaidiType.equals(Constant.KUAIDI_TYPE_KUAIDI100)) {
			if (orderId == null || orderId.equals("")) {
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
						"参数无效，运单有能为空");
			}
			M_order order = null;
			try {
				order=this.m_orderDao.getRouteArgsbyOrderId(orderId, storeId);
				
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("查询异常！，请重试", e);
			}
			if(order==null)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
						"查询失败！没有找到对应的单号!");
			}
			
			if ((order.getThirdNo() == null || order.getThirdNo().equals("")
					|| order.getThirdPNS() == null || order.getThirdPNS()
					.equals(""))
					|| (order.getState() != null)
					&& (Integer.parseInt(order.getState()) < 7)) {
				ResponseObject<Object> responseObject = new ResponseObject<Object>(
						ResponseCode.SUCCESS_CODE);

				List<Route> list;
				try {
					list = this.routeDao.getRouteByOrderId(orderId);
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("查询异常！，请重试", e);
				}
				responseObject.setData(list);
				return responseObject;
			}

			try {
				String key=this.globalargsDao.getcontentbyflag("kuaidi100_key");
				ResponseObject<Object> responseObject = new ResponseObject<Object>(
						ResponseCode.SUCCESS_CODE);
				List<Route> result = new ArrayList<Route>();
				String content = KuaiDiUtil.SearchkuaiDiInfo(
						Constant.KUAIDI_TYPE_KUAIDI100, "",
						order.getThirdPNS(), order.getThirdNo(),key);
				result = this.routeDao.getRouteByOrderId(orderId);// 先导入原有数据
				
				//加入所有快递考虑
				String[] htmllist=null;
				try {
					Properties props = PropertiesReader.read(Constant.SYSTEM_PROPERTIES_FILE);
					//key = props.getProperty("kuaidi.apikey");
					String htmllist_temp=props.getProperty("kuaidi100.use.url.list");
					if(!StringUtil.isEmpty(htmllist_temp))
					{
						htmllist=htmllist_temp.split(";");
					}
				} catch (Exception e) {
					//key = "1a34d5adddbaf841";
				}
				
				String flag="0";
				if((htmllist!=null)&&(htmllist.length>0))
				{
					if(!StringUtil.isEmpty(order.getThirdPNS()))
					{
						for(int i=0;i<htmllist.length;i++)
						{
							if(!StringUtil.isEmpty(htmllist[i]))
							{
								if(order.getThirdPNS().equalsIgnoreCase(htmllist[i]))
								{
									flag="1";
									break;
								}
							}
						}
					}
				}
				
				if ((!StringUtil.isEmpty(order.getThirdPNS()))
						&& (flag.equalsIgnoreCase("1"))) {
					Route route = new Route();
					route.setReturnurl(content);
					route.setOrderId(orderId);
					route.setState("");
					route.setThrid_no(order.getThirdNo());
					route.setThrid_pns(order.getThirdPNS());

					//route.setStateRemark("第三方快递公司：" + order.getThirdPNS()
					//		+ "<br>" + "第三方快递单号：" + order.getThirdNo());
					
					result.add(route);
					responseObject.setData(result);
					return responseObject;
				}

				JSONObject json = JSONObject.fromObject(content);

				// 0：物流单暂无结果，
				// 1：查询成功，
				// 2：接口出现异常，

				if (json.getString("status").equals("1")) {
					JSONArray ja = json.getJSONArray("data");
					//for (int i = 0; i < ja.size(); i++) {
					boolean replaceflag=false;
					for (int i = ja.size()-1; i >=0; i--) {
						JSONObject jo = (JSONObject) ja.get(i);
						Route route = new Route();
						route.setOrderId(orderId);
						//route.setStateRemark("第三方快递公司：" + order.getThirdPNS()
						//		+ "<br>" + "第三方快递单号：" + order.getThirdNo());
						//route.setDate(orderId);
						route.setDate(jo.getString("time"));
						route.setRemark(jo.getString("context"));
						route.setState("");
						if(!StringUtil.isEmpty(route.getRemark())&&(route.getRemark().indexOf("已揽收")>0))//查找到此字符，替换字符串
						{
							String str=this.globalargsDao.getcontentbyflag("kuaidu_replace_yilanshou");
							if(!StringUtil.isEmpty(str)&&(replaceflag==false))
							{
								route.setRemark(str);
								replaceflag=true;
							}
						}
						result.add(route);

					}
				} else if (json.getString("status").equals("0")) {
					/*
					 * Route route = new Route(); SimpleDateFormat df = new
					 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
					 * route.setDate(df.format(new Date()));
					 * route.setDate(oids); route.setRemark("");
					 * route.setState("暂无第三方物流单结果"); result.add(route);
					 */
				} else {
					// log.error("查询运单失败,第三方快递公司="+order.getThirdPNS()+"，快递号="+order.getThirdNo());
					// return new
					// ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,
					// "查询失败,请稍后查询");
				}

				responseObject.setData(result);
				return responseObject;

			} catch (Exception e) {
				log.error("查询运单失败", e);
				return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,
						"查询失败,请稍后查询");
			}
		} else {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
					"参数无效,无此类型查询方式");
		}
	}
				
	//批量修改状态时，进行的修改状态
	public ResponseObject<Object> importMorderState(List<ImportMorder> importOrders, String empName,String wid,String sendmessage)
	        throws ServiceException{
		int usa_nos=0;
		int rmb_nos=0;
		try {
			
			//List<Order> orders = new ArrayList<Order>();

			
			String year1=this.globalargsDao.getcontentbyflag("cardId_modify_year");//获取更新数据库身份证时间
			//String myear="1";
			
			String modate = null;
			if(!StringUtil.isEmpty(year1));
			{
				try{
					int aa=Integer.parseInt(year1);
					if(aa>0)
					{
						modate = DateUtil.date2String(new Date());
						String oldyear=modate.substring(0, 4);
						int year0=Integer.parseInt(oldyear);
						year0=year0-aa;
						String newyear=String.valueOf(year0);
						modate=modate.replace(oldyear, newyear);
					}
					
				}catch(Exception e){
					modate=null;
				}
			}
			List<MessageTemp> messagephone=new ArrayList<MessageTemp>();
			for (ImportMorder io : importOrders) {
				String date = DateUtil.date2String(new Date());
				if (StringUtil.isEmpty(io.getOrderId())) {
					io.setStateResult("失败:单号为空!");
					continue;
				}
				int kk=0;
				try{
					kk=this.m_orderDao.modifyroutestatebyorderId(date, io.getOrderId(), io.getState(), io.getThirdPNS(), io.getThirdNo(), wid);
					if(kk>0)
					{
						io.setStateResult("成功！");
					}
					else
					{
						io.setStateResult("失败!请检查单号是否存在或是否属于本门店!");
					}
				}
				catch (Exception e) {
					io.setStateResult("失败!修改数据库出现异常，请检查字符模式是否符合规范!");
				}
				
				if((kk>0)&&(!StringUtil.isEmpty(io.getState())))
				{
					Route route = new Route();
					route.setOrderId(io.getOrderId());
					if((!StringUtil.isEmpty(io.getThirdNo()))&&(!StringUtil.isEmpty(io.getThirdPNS())))
					{
						//route.setStateRemark("第三方快递公司：" + io.getThirdPNS()
						//		+ "<br>" + "第三方快递单号：" + io.getThirdNo());
						route.setStateRemark("");
					}
					else
					{
						//route.setStateRemark("状态更改!");
						route.setStateRemark("");
					}
					
					route.setDate(date);
					
					route.setRemark("状态更改!");
					route.setState(OrderUtil.transformerState(0,io.getState()));
					try
					{
						this.routeDao.insertRoute(route);
					}
					catch (Exception e) {
						kk=0;
						io.setStateResult("更新运单成功，但设置路由状态异常！");
					}
				}

				//0表示不发送，1表示发送状态更新，2表示发送状态更新附带身份证要求
				if((kk>0)&&(sendmessage!=null)&&(sendmessage.equalsIgnoreCase("1")||sendmessage.equalsIgnoreCase("2")))
				{
					if(!StringUtil.isEmpty(io.getOrderId())&&io.getOrderId().indexOf("MT")>=0)//只发送美淘信息
					{
						M_order order=this.m_orderDao.getRuser(io.getOrderId(), wid);
						//查找到之前有相同的电话，即同一个收件人
						if((order!=null)&&(order.getRuser()!=null)&&(!StringUtil.isEmpty(order.getRuser().getPhone())))
						{
							
							int existphone=this.cardIdManageDao.countphoneandname(order.getRuser().getPhone(), order.getRuser().getName(),modate);
						
							if(existphone!=0)
							{
								continue;
							}
							boolean sendstate=true;
							try{
								double state=Double.parseDouble(order.getState());
								if(sendmessage.equalsIgnoreCase("2")&&(state<Double.parseDouble(Constant.ORDER_STATE3)||state>Double.parseDouble(Constant.ORDER_STATE8)))
								{
									sendstate=false;
								}
								if(sendmessage.equalsIgnoreCase("1")&&(state<Double.parseDouble(Constant.ORDER_STATE3)))
								{
									sendstate=false;
								}
							}catch(Exception e)
							{
								sendstate=false;
							}
							
							if(sendstate==false)
							{
								continue;
							}
							
							
							boolean flag=false;
							for(MessageTemp m:messagephone)
							{
								if(order.getRuser().getPhone().equalsIgnoreCase(m.getPhone()))
								{
									m.getOrderids().add(io.getOrderId());
									m.setPhone(order.getRuser().getPhone());
									m.setState(io.getState());
									flag=true;
									break;
								}
								
							}
							if(flag==false)//之前没有同一个电话号码的
							{
								MessageTemp temp=new MessageTemp();
								temp.getOrderids().add(io.getOrderId());
								temp.setPhone(order.getRuser().getPhone());
								temp.setState(io.getState());
								messagephone.add(temp);
							}
							
						}
						
					}
				}
				
			}
			
			//发送短信
			if((sendmessage!=null)&&(sendmessage.equalsIgnoreCase("1")||sendmessage.equalsIgnoreCase("2")))
			{
				for(MessageTemp m:messagephone)
				{
					if(!ConsigneeInfoUtil.validatePhone(m.getPhone()))//电话号码不正确
					{
						m.setRemark("发送失败：收件人电话号码不正确!");
					}
					else
					{
						if(m.getOrderids().size()==1)//只有一个接收用户
						{
							Set<String> orderids=m.getOrderids();
							String orderid="";
							for (String str :orderids) {  
								orderid=str;  
							}  
							String message="";
							String result="";
							if(m.getPhone().length()==10)
							{
								//message="your tracking number:"+orderid+",state:"+OrderUtil.transformerState(2,m.getState());
								//result=sendEnglishMessage(message,m.getPhone());
								m.setRemark("失败：不是中国电话号码!");
							}
							else
							{
								if(sendmessage.equalsIgnoreCase("1"))
								{
									if(!StringUtil.isEmpty(m.getState())&&Constant.ORDER_STATE9.equalsIgnoreCase(m.getState()))
									{
										message="包裹"+orderid+OrderUtil.transformerState(0,m.getState())+"，请务必检查后签收";
										result=sendChinaMeitaoMessage(message,m.getPhone());
									}
									else
									{
										message="单号"+orderid+"的包裹状态是"+OrderUtil.transformerState(0,m.getState());
										result=sendChinaMessage(message,m.getPhone());
									}
									rmb_nos++;
								}else if(sendmessage.equalsIgnoreCase("2"))//附带身份证要求
								{
									message="单号"+orderid+"的包裹状态是"+OrderUtil.transformerState(0,m.getState())+",因清关需要,请及时上传身份证信息";
									result=sendChinaMessage(message,m.getPhone());
									rmb_nos++;
								}
							}
							//String result=sendEnglishMessage(message,m.getPhone());
							if((result!=null)&&(result.equalsIgnoreCase("0")))//表示发送成功
							{
								m.setRemark("发送成功！");
							}
							else
							{
								m.setRemark("发送失败:"+result);
							}
						}
						else if(m.getOrderids().size()>1)//包含运单数超过1
						{
							String message="";
							String result="";
							if(m.getPhone().length()==10)
							{
								//message="the state of your "+m.getOrderids().size()+" packages is "+OrderUtil.transformerState(2,m.getState());
								//result=sendEnglishMessage(message,m.getPhone());
								m.setRemark("失败：不是中国电话号码!");
							}
							else
							{
								if(sendmessage.equalsIgnoreCase("1"))
								{
									String orderid=m.getOrderids().iterator().next();
									if(!StringUtil.isEmpty(m.getState())&&Constant.ORDER_STATE9.equalsIgnoreCase(m.getState()))
									{
										message="包裹"+orderid+"等"+m.getOrderids().size()+"个"+OrderUtil.transformerState(0,m.getState())+"，请务必检查后签收";
										result=sendChinaMeitaoMessage(message,m.getPhone());
										rmb_nos++;
									}
									else
									{
										message="你的"+m.getOrderids().size()+"个包裹目前状态为 "+OrderUtil.transformerState(0,m.getState());
										result=sendChinaMessage(message,m.getPhone());
										rmb_nos++;
									}
								}
								else if(sendmessage.equalsIgnoreCase("2"))//附带身份证要求
								{
									message="你的"+m.getOrderids().size()+"个包裹目前状态为 "+OrderUtil.transformerState(0,m.getState())+",因清关需要,请及时上传身份证信息";
									result=sendChinaMessage(message,m.getPhone());
									rmb_nos++;
								}
							}
							//String result=sendEnglishMessage(message,m.getPhone());
							if((result!=null)&&(result.equalsIgnoreCase("0")))//表示发送成功
							{
								String sss="";
								for(String a:m.getOrderids())
								{
									if(sss.equalsIgnoreCase(""))
									{
										sss=a;
									}
									else
									{
										sss=sss+","+a;
									}
								}
								m.setRemark("发送成功！合并"+m.getOrderids().size()+"条运单发送!包含："+sss);
							}
							else
							{
								m.setRemark("发送失败:"+result);
							}
						}
					}
					
					
				}
				
				//保存发送结果输出到前端
				for (ImportMorder io1 : importOrders) {
					for(MessageTemp m:messagephone)
					{
						for(String orderid:m.getOrderids())
						{
							if((orderid!=null)&&(orderid.equalsIgnoreCase(io1.getOrderId())))
							{
								io1.setMessageResult(m.getRemark());
							}
						}
					}
				}
				
			}
			
			//int k = this.orderDao.modifyOrderStateOfExcel(orders);
			//if (k != orders.size()) {
				// 插入失败, 修改的时候返回1，所以此处不能通过这个判断
				// throw new Exception();
			//}

			/*for (Order o : orders) {
				if ((o.getState() != null)
						&& (o.getState().equals(Constant.ORDER_STATE3)
								|| o.getState().equals(Constant.ORDER_STATE4)
								|| o.getState().equals(Constant.ORDER_STATE7)
								|| o.getState().equals(Constant.ORDER_STATE8) || o
								.getState().equals(Constant.ORDER_STATE10))) {

					
					Order order = this.orderDao.getByOrderId(o.getOrderId());
					if (order == null) {
						
						continue;
					}
				
					if ((order.getUserId() != null)
							&& (!order.getUserId().equals(""))) {
						User user = this.userDao.getUserById(order.getUserId());
						if (user != null) {
							try{
								this.autoSendService.send(user, order);
							}
							catch(Exception e)
							{
								//在这里捕获的异常不做任何处理
							}
//							log.info(SmsSendUtil.sendOrderStateMsg(
//									o.getOrderId(), o.getState(),
//									user.getPhone()));
						}
					}
				}
			}*/

		} catch (Exception e) {
			return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"更新状态发生异常！");
			//throw ExceptionUtil.handle2ServiceException(e);
		}
		
		
		
		String date=DateUtil.date2String(new Date());
		try{
			if(!StringUtil.isEmpty(sendmessage)&&(sendmessage.equalsIgnoreCase("2")))//身份证信息
			{
				MessageRecord record =new MessageRecord();
				record.setModifyDate(date);
				record.setRemark("操作人："+empName);
				record.setCreateDate(date);
				record.setRmb_nos(String.valueOf(rmb_nos));
				record.setUsa_nos(String.valueOf(usa_nos));
				record.setType(Constant.SEND_MESSAGE_TYPE4);//
				this.messageRecordDao.insert(record);
			}
			else if(!StringUtil.isEmpty(sendmessage)&&(sendmessage.equalsIgnoreCase("1")))
			{
				MessageRecord record =new MessageRecord();
				record.setModifyDate(date);
				record.setRemark("操作人："+empName);
				record.setCreateDate(date);
				record.setRmb_nos(String.valueOf(rmb_nos));
				record.setUsa_nos(String.valueOf(usa_nos));
				record.setType(Constant.SEND_MESSAGE_TYPE3);//
				this.messageRecordDao.insert(record);
			}
		}catch(Exception e)
		{
			
		}
		
		ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
		obj.setData(importOrders);
		return obj;
	}
	
	
	
	//查找门市运单的信息
	public ResponseObject<PageSplit<M_order>> searchMsMorders(String empid,String wid,String cid,String userinfo,String state,String time,String payornot, int pageSize, int pageNow)
	        throws ServiceException{
		try {
		
			if(StringUtil.isEmpty(userinfo))
			{
				userinfo=null;
			}
			else
			{
				userinfo = StringUtil.escapeStringOfSearchKey(userinfo);
			}
			

			int rowCount = 0;
			try {

				rowCount = this.m_orderDao.countOfSearchMsMOrders(empid, wid, cid, userinfo, state, time,payornot);
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("获取运单个数失败", e);
			}

			ResponseObject<PageSplit<M_order>> responseObj = new ResponseObject<PageSplit<M_order>>(
					ResponseCode.SUCCESS_CODE);
			if (rowCount > 0) {
				pageSize = Math.max(pageSize, 1);
				int pageCount = rowCount / pageSize
						+ (rowCount % pageSize == 0 ? 0 : 1);
				pageNow = Math.min(pageNow, pageCount);
				PageSplit<M_order> pageSplit = new PageSplit<M_order>();
				pageSplit.setPageCount(pageCount);
				pageSplit.setPageNow(pageNow);
				pageSplit.setRowCount(rowCount);
				pageSplit.setPageSize(pageSize);

				int startIndex = (pageNow - 1) * pageSize;
				try {
					List<M_order> orders = this.m_orderDao.SearchMsMOrders(empid, wid, cid, userinfo, state, time,payornot, startIndex, pageSize);
						
					for(M_order o: orders)
					{
						if(!StringUtil.isEmpty(o.getUserId()))
						{
							o.setUser(this.userDao.getUserById(o.getUserId()));
						}
						
						if(!StringUtil.isEmpty(o.getEmployeeId()))
						{
							Employee emp=this.employeeDao.getEmployeeById(o.getEmployeeId());
							if(emp!=null)
							{
								o.setEmployeeName(emp.getAccount());
							}
						}
						if(!StringUtil.isEmpty(o.getStoreId()))
						{
							Warehouse ware=this.warehouseDao.getById(o.getStoreId());
							
							if(ware!=null)
							{
								o.setStoreName(ware.getName());
								//o.setEmployeeName(emp.getAccount());
							}
						}
						/*if((o.getRuser()!=null))
						{
							if(!StringUtil.isEmpty(o.getRuser().getCardurl()))
							{
								o.getRuser().setCardurl(java.net.URLEncoder.encode(o.getRuser().getCardurl(), "utf-8"));
							}
							if(!StringUtil.isEmpty(o.getRuser().getCardother()))
							{
								o.getRuser().setCardother(java.net.URLEncoder.encode(o.getRuser().getCardother(), "utf-8"));
							}
							if(!StringUtil.isEmpty(o.getRuser().getCardtogether()))
							{
								o.getRuser().setCardtogether(java.net.URLEncoder.encode(o.getRuser().getCardtogether(), "utf-8"));
							}
							
						}*/
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
		} catch (ServiceException e) {
			//throw e;
			return new ResponseObject<PageSplit<M_order>>(ResponseCode.SHOW_EXCEPTION," 获取运单列表发生异常！");
		}
	}
	
	
	
	
	
	//修改运单路由信息
	public ResponseObject<Object> modifyMsroutestate(String empid,String ModifyDate,String id,String state,String storeId) throws ServiceException
	{
		if(StringUtil.isEmpty(id)||StringUtil.isEmpty(state))
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数不能为空!");
		}
		try{
			//int k=this.m_orderDao.modifyroutestate(id, state, thirdPNS, thirdNo, storeId);
			int k=this.m_orderDao.modifyMsstate(empid, ModifyDate, id,state, storeId);
			if(k==0)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"修改失败!");
			}
			String orderid=this.m_orderDao.getOrderIdbyId(id);
			String date = DateUtil.date2String(new Date());
			Route route = new Route();
			route.setDate(date);
			route.setOrderId(orderid);
			route.setRemark("修改状态");
			
			route.setState(OrderUtil.transformerState(0,state));
			this.routeDao.insertRoute(route);
			
			ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			
			return obj;
				
		} catch (Exception e) {
			//throw ExceptionUtil.handle2ServiceException(e.getMessage());
			return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION," 修改状态发生异常！");
		}
	}
	
	
	
	//门市修改运单路由
	public ResponseObject<Object> modifyMsInfo(M_order order,String state,String payornot,String emplyeeId,String storeId) throws ServiceException
	{
		if(order==null)
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数不正确!");
		}
		try{
			//int k=this.m_orderDao.modifyroutestate(id, state, thirdPNS, thirdNo, storeId);
			String date = DateUtil.date2String(new Date());
			String pre_payornot=order.getPayornot();
			String pre_state=order.getState();
			order.setState(state);
			order.setPayornot(payornot);
			order.setModifyDate(date);
			order.setEmployeeId(null);//不修改员工归属
			int k=this.m_orderDao.modifyMsInfo(order);
			if(k==0)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"修改失败!");
			}
			if((!payornot.equalsIgnoreCase(pre_payornot))||(state.equalsIgnoreCase(Constant.ORDER_STATE1)))//支付状态发生改变，则要记录改变状态
			{
				String payname="";
				if(payornot.equalsIgnoreCase(Constant.ORDER_PAY_STATE1))
				{
					payname="已付款";
				}
				else
				{
					payname="未付款";
				}
				
				AccountDetail detail = new AccountDetail();
				detail.setAmount(order.getTotalmoney());
				detail.setCreateDate(date);
				detail.setModifyDate(date);
				detail.setState(Constant.ACCOUNT_DETAIL_STATE4);
				detail.setCurrency("美元");
				detail.setName("门市标记"+payname);
				detail.setType(Constant.ACCOUNT_DETAIL_TYPE3);
				detail.setConfirm_state("0");
				
				Employee emp=this.employeeDao.getEmployeeById(emplyeeId);
				if(emp!=null)
				{
					detail.setEmpId(emplyeeId);
					detail.setEmpName(emp.getAccount());
				}
				
				Warehouse ware=this.warehouseDao.getById(storeId);
				if(ware!=null)
				{
					detail.setStoreId(ware.getId());
					detail.setEmpStore(ware.getName());
				}
				
				if(StringUtil.isEmpty(order.getUserId()))
				{
					detail.setUserId("-1");
				}
				else
				{
					detail.setUserId(order.getUserId());
				}
				String prefix="";
				if(state.equalsIgnoreCase(Constant.ORDER_STATE1))
				{
					
					 prefix = "修改运单作废";
				}
				else
				{
					 prefix = "修改支付状态";
				}

				if(StringUtil.isEmpty(order.getQremark()))
				{
					detail.setRemark(prefix + "；运单id：" + order.getOrderId()+",操作员工号："+emplyeeId);
				}
				else
				{
					detail.setRemark(prefix + "；运单id：" + order.getOrderId()+",操作员工号："+emplyeeId+",说明："+order.getQremark());
				}
				this.accountDetailDao.insertAccountDetail(detail);
			}
			
			//跌幅发生变化，显示变化信息
			if(!state.equalsIgnoreCase(pre_state))
			{
				String orderid=order.getOrderId();
				//String date = DateUtil.date2String(new Date());
				Route route = new Route();
				route.setDate(date);
				route.setOrderId(orderid);
				route.setRemark("修改状态");
				if(!StringUtil.isEmpty(order.getQremark()))
				{
					route.setStateRemark(order.getQremark());
				}
				
				route.setState(OrderUtil.transformerState(0,order.getState()));
				this.routeDao.insertRoute(route);
			}
			
			ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			
			return obj;
				
		} catch (Exception e) {
			//throw ExceptionUtil.handle2ServiceException(e.getMessage());
			return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION," 修改状态发生异常！");
		}
	}
	
	
	//kai 20160324 查找运单接口函数
	public ResponseObject<PageSplit<M_order>> searchMordersbyUser(String userId,String oid,String wid,String cid,String info,String state,String type,String payornot, int pageSize, int pageNow)
	        throws ServiceException{
				try {
					///
		
					/*if(StringUtil.isEmpty(userId))
					{
						return new ResponseObject<PageSplit<M_order>>(ResponseCode.PARAMETER_ERROR,"所属用户不能为空!");
					}*/
					if(StringUtil.isEmpty(info))
					{
						info=null;
					}
					else
					{
						info = StringUtil.escapeStringOfSearchKey(info);
					}
					
					if(StringUtil.isEmpty(oid))
					{
						oid=null;
					}
					else
					{
						oid = StringUtil.escapeStringOfSearchKey(oid);
					}
				
					int rowCount = 0;
					try {
						rowCount=this.m_orderDao.countOfSearchMOrdersbyUser(userId, oid, wid, cid, info, state, type, payornot);
						//rowCount = this.m_orderDao.countOfSearchMOrders(oid, sod, god, flyno, wid, cid, userinfo, commudityinfo, state, type,payway, sdate, edate, psdate, pedate,cardurl,cardother,cardtogether,cardid,uploadflag,payornot);
					} catch (Exception e) {
						throw ExceptionUtil.handle2ServiceException("获取运单个数失败", e);
					}

					ResponseObject<PageSplit<M_order>> responseObj = new ResponseObject<PageSplit<M_order>>(
							ResponseCode.SUCCESS_CODE);
					if (rowCount > 0) {
						pageSize = Math.max(pageSize, 1);
						int pageCount = rowCount / pageSize
								+ (rowCount % pageSize == 0 ? 0 : 1);
						pageNow = Math.min(pageNow, pageCount);
						PageSplit<M_order> pageSplit = new PageSplit<M_order>();
						pageSplit.setPageCount(pageCount);
						pageSplit.setPageNow(pageNow);
						pageSplit.setRowCount(rowCount);
						pageSplit.setPageSize(pageSize);

						int startIndex = (pageNow - 1) * pageSize;
						try {
							//List<M_order> orders = this.m_orderDao.SearchMOrders(oid, sod, god, flyno, wid, cid, userinfo, commudityinfo, state, type,payway, sdate, edate, psdate, pedate,cardurl,cardother,cardtogether,cardid,uploadflag,payornot, startIndex, pageSize);
							List<M_order> orders=this.m_orderDao.SearchMOrdersbyUser(userId, oid, wid, cid, info, state, type, payornot, startIndex, pageSize);
							for(M_order o: orders)
							{
								if(!StringUtil.isEmpty(o.getUserId()))
								{
									o.setUser(this.userDao.getUserById(o.getUserId()));
								}
								if(!StringUtil.isEmpty(o.getStoreId()))
								{
									Warehouse house=this.warehouseDao.getById(o.getStoreId());
									if(house!=null)
									{
										o.setStoreName(house.getName());
									}
								}
								if(!StringUtil.isEmpty(o.getChannelId()))
								{
									Channel channel=this.channelDao.getChannelById(o.getChannelId());
									if(channel!=null)
									{
										o.setChannelName(channel.getName());
									}
								}
								List<String> tids=this.morder_TorderDao.gettorderIds(o.getOrderId());
								List<T_order> torders=new ArrayList<T_order>(); 
								for(String id:tids)
								{
									T_order torder=this.torderDao.getone(null, id);
									if(torder!=null)
									{
										torders.add(torder);
									}
								}
								o.setTorders(torders);
								
								/*if((o.getRuser()!=null))
								{
									if(!StringUtil.isEmpty(o.getRuser().getCardurl()))
									{
										o.getRuser().setCardurl(java.net.URLEncoder.encode(o.getRuser().getCardurl(), "utf-8"));
									}
									if(!StringUtil.isEmpty(o.getRuser().getCardother()))
									{
										o.getRuser().setCardother(java.net.URLEncoder.encode(o.getRuser().getCardother(), "utf-8"));
									}
									if(!StringUtil.isEmpty(o.getRuser().getCardtogether()))
									{
										o.getRuser().setCardtogether(java.net.URLEncoder.encode(o.getRuser().getCardtogether(), "utf-8"));
									}
									
								}*/
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
				} catch (ServiceException e) {
					//throw e;
					return new ResponseObject<PageSplit<M_order>>(ResponseCode.SHOW_EXCEPTION," 获取运单列表发生异常！");
				}
			}
	
	
	
	//根据id获取运单号
	public ResponseObject<Object> getonebyuser(String id,String userId) throws ServiceException
	{
		if(StringUtil.isEmpty(id))
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数不能为空!");
		}
		try{
			M_order order=this.m_orderDao.getById(id, null);
			if(order==null)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"没有查找到对应的运单!");
			}
			if(!StringUtil.isEmpty(userId))
			{
				if(!userId.equalsIgnoreCase(order.getUserId()))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"没有查找到对应的运单!");
				}
			}
			
			if(!StringUtil.isEmpty(order.getStoreId()))
			{
				Warehouse house=this.warehouseDao.getById(order.getStoreId());
				if(house!=null)
				{
					order.setStoreName(house.getName());
				}
			}
			if(!StringUtil.isEmpty(order.getChannelId()))
			{
				Channel channel=this.channelDao.getChannelById(order.getChannelId());
				if(channel!=null)
				{
					order.setChannelName(channel.getName());
				}
			}
			
			
			String date = DateUtil.date2String(new Date());
			order.setNowtime(date);
			ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			obj.setData(order);
			return obj;
				
		} catch (Exception e) {
			//throw ExceptionUtil.handle2ServiceException(e.getMessage());
			return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION," 查找对应的运单发生异常！");
		}
	}

	
	public ResponseObject<Object> userPayOne(String id, String orderId, String userId, String amount,
	        double newrmb, double newusd, boolean accountPay) throws ServiceException {
		try {
			String date = DateUtil.date2String(new Date());
			int i=this.m_orderDao.modifyUserMOrderPay(id, userId, Constant.ORDER_PAY_STATE1, date, date, "2");//2表示用户支付
			
			
			if (i ==1) {
				// 提交一个账户更改详情
				AccountDetail detail = new AccountDetail();
				detail.setAmount(amount);
				detail.setCreateDate(date);
				detail.setModifyDate(date);
				detail.setState(Constant.ACCOUNT_DETAIL_STATE2);
				detail.setCurrency("美元");
				detail.setName("支付运费");
				detail.setType(Constant.ACCOUNT_DETAIL_TYPE2);
				detail.setUserId(userId);
				detail.setConfirm_state("0");
				detail.setStoreId("-1");
				String prefix = "用户帐户余额支付";

				
					// 账户支付，修改账户余额
				Account account = new Account();
				account.setUsd(String.valueOf(newusd));
				account.setRmb(String.valueOf(newrmb));
				account.setUserId(userId);
				account.setModifyDate(date);
				if (this.accountDao.modifyAccount(account) > 0) {
					// pass
				} else {
					throw new Exception();
				}
				prefix = "用户帐户余额支付";
				
				detail.setRemark(prefix + "；运单号：" + orderId);
				int m=this.accountDetailDao.insertAccountDetail(detail);
				if(m<1)
				{
					throw new Exception();
				}

				/*for (String orderId : orderIds) {
					Route route = new Route();
					route.setDate(date);
					route.setOrderId(orderId);
					route.setRemark(prefix);
					route.setState(Constant.ORDER_ROUTE_STATE3);
					this.routeDao.insertRoute(route);
				}*/
			} else {
				throw new Exception("支付失败！");
			}
		} catch (Exception e) {
			return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION," 支付发生异常！");
			//throw ExceptionUtil.handle2ServiceException(e);
		}
		return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
	}

	
	
	public ResponseObject<Object> adminPayOne(String id, String orderId, String userId, String amount,String storeName,String storeId,String empName,String empNo,
	        double newrmb, double newusd, boolean accountPay) throws ServiceException {
		try {
			String date = DateUtil.date2String(new Date());
			int i=this.m_orderDao.modifyUserMOrderPay(id, userId, Constant.ORDER_PAY_STATE1, date, date, "1");//2余额支付
			
			
			if (i ==1) {
				// 提交一个账户更改详情
				AccountDetail detail = new AccountDetail();
				detail.setAmount(amount);
				detail.setCreateDate(date);
				detail.setModifyDate(date);
				detail.setState(Constant.ACCOUNT_DETAIL_STATE2);
				detail.setCurrency("美元");
				detail.setName("支付运费");
				detail.setType(Constant.ACCOUNT_DETAIL_TYPE2);
				detail.setUserId(userId);
				detail.setConfirm_state("0");
				detail.setStoreId(storeId);
				detail.setEmpStore(storeName);
				detail.setEmpId(empNo);
				detail.setEmpName(empName);
				
				String prefix = "后台帐户余额支付";

				
					// 账户支付，修改账户余额
				Account account = new Account();
				account.setUsd(String.valueOf(newusd));
				account.setRmb(String.valueOf(newrmb));
				account.setUserId(userId);
				account.setModifyDate(date);
				if (this.accountDao.modifyAccount(account) > 0) {
					// pass
				} else {
					throw new Exception();
				}
				prefix = "后台帐户余额支付";
				
				detail.setRemark(prefix + "；运单号：" + orderId);
				int m=this.accountDetailDao.insertAccountDetail(detail);
				if(m<1)
				{
					throw new Exception();
				}

				/*for (String orderId : orderIds) {
					Route route = new Route();
					route.setDate(date);
					route.setOrderId(orderId);
					route.setRemark(prefix);
					route.setState(Constant.ORDER_ROUTE_STATE3);
					this.routeDao.insertRoute(route);
				}*/
			} else {
				throw new Exception("支付失败！");
			}
		} catch (Exception e) {
			return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION," 支付发生异常！");
			//throw ExceptionUtil.handle2ServiceException(e);
		}
		return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
	}
	
	//添加在线置单
	public ResponseObject<Object> add_online_Morder(M_order order,String rflag,String sflag,String reflag) throws ServiceException
	{
		
		if(order==null)
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错!");
		}
		try {
			
			if(!StringUtil.isEmpty(reflag)&&(reflag.equalsIgnoreCase("1")))//已经定不管是否重复都保存
			{}
			else
			{
				String addressstr="";//如果地址簿中存发件人或收件人的电话，返回警告信息
				 if(!StringUtil.isEmpty(sflag)&&(sflag.equalsIgnoreCase("1")))//保存发件人到用户地址簿中，先检查发件人中电话是否存在
				 {
					 int k=this.consigneeSendpersonDao.getcountuserphone(order.getSuser().getPhone(), order.getUserId());
					 if(k>0)
					 {
						 addressstr="发件人电话";
					 }
				 }
				 if(!StringUtil.isEmpty(rflag)&&(rflag.equalsIgnoreCase("1")))//保存收件人到用户地址簿中
				 {
					 int k=this.consigneeInfoDao.getcountuserphone(order.getRuser().getPhone(), order.getUserId());
					 if(k>0)
					 {
						 if(addressstr.equalsIgnoreCase(""))
						 {
							 addressstr="收件人电话";
						 }
						 else
						 {
							 addressstr=addressstr+",收件人电话";
						 }
						 
					 }
				 }
			
				 if(!addressstr.equalsIgnoreCase(""))//保存地址出错
				 {
					 return new ResponseObject<Object>(ResponseCode.sr_address_ERROR,addressstr);
				 }
			
			}
			
			String date = DateUtil.date2String(new Date());
			order.setCreateDate(date);
			order.setModifyDate(date);
			order.setPayornot(Constant.ORDER_PAY_STATE0);//状态为未付款
			
			
			if(order.getRuser()!=null)
			{
				order.getRuser().setCreateDate(date);
				order.getRuser().setModifyDate(date);
				order.getRuser().setUserId(order.getUserId());
				order.getRuser().setCardid_flag(Constant.VERFY_CARDID__10);//设置不时行检验
				
				
				int k=this.receive_UserDao.insertReceiveUser(order.getRuser());//插入收件人信息
				  if(k>0)
				  {
					  order.setRuserId(order.getRuser().getId());
				  }
				  else
				  {
					  throw new Exception("插入收件人用户失败");
				  }
				  
				  if(!StringUtil.isEmpty(rflag)&&(rflag.equalsIgnoreCase("1")))//保存收件人到用户地址簿中
				  {
					  if(!StringUtil.isEmpty(order.getUserId()))//
					  {
						  ConsigneeInfo info=new ConsigneeInfo();
						  info.setCardId(order.getRuser().getCardid());
						  info.setCardUrl(order.getRuser().getCardurl());
						  info.setCardurlother(order.getRuser().getCardother());
						  info.setCardurltogether(order.getRuser().getCardtogether());
						  info.setCity(order.getRuser().getCity());
						  info.setDistrict(order.getRuser().getDist());
						  info.setLastDate(date);
						  info.setName(order.getRuser().getName());
						  info.setPhone(order.getRuser().getPhone());
						  info.setProvince(order.getRuser().getState());
						  info.setStreetAddress(order.getRuser().getAddress());
						  info.setUserId(order.getUserId());
						  info.setZipCode(order.getRuser().getZipcode());
						  int n=this.consigneeInfoDao.insertConsigneeInfo(info);
						  if(n<1)
						  {
							  throw new Exception("添加收件人失败");
						  }
					  }
					  
				  }
			}
			else
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"收件人为空!");
			}
			if(order.getSuser()!=null)
			{
				order.getSuser().setCreateDate(date);
				order.getSuser().setModifyDate(date);
				order.getSuser().setUserId(order.getUserId());
			  int k=this.send_UserDao.insertSendUser(order.getSuser());//插入发件人信息
			  if(k>0)
			  {
				  order.setSuserId(order.getSuser().getId());
			  }
			  else
			  {
				  throw new Exception("插入发件人用户失败");
			  }
			  
			  if(!StringUtil.isEmpty(sflag)&&(sflag.equalsIgnoreCase("1")))//保存收件人到用户地址簿中
			  {
				  if(!StringUtil.isEmpty(order.getUserId()))//
				  {
					  ConsigneeSendperson info=new ConsigneeSendperson();
					  info.setCity(order.getSuser().getCity());
					  info.setCompany(order.getSuser().getCompany());
					  info.setCreateDate(date);
					  info.setDistrict(order.getSuser().getDist());
					  info.setEmail(order.getSuser().getEmail());
					  info.setModifyDate(date);
					  info.setName(order.getSuser().getName());
					  info.setPhone(order.getSuser().getPhone());
					  info.setProvince(order.getSuser().getState());
					  info.setStreetAddress(order.getSuser().getAddress());
					  info.setUserId(order.getUserId());
					  info.setZipCode(order.getSuser().getZipcode());
					  
					 
					  int n=this.consigneeSendpersonDao.insert(info);
					  if(n<1)
					  {
						  throw new Exception("添加发件人失败");
					  }
				  }
				  
			  }
			
			}
			else
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"发件人为空!");
			}
			
			
			
			
			
			order.setPayornot(Constant.ORDER_PAY_STATE0);//设置为未付款状态，1表示已经付款，其它表示付款异常状态
			int k=this.m_orderDao.insertMorder(order);
			if(k==0)
			{
				throw new Exception("插入运单失败");
			}
			//String orderid=this.createOrderIdarg(order.getId(),order.getStoreId(),order.getType());//获取运单的运单号
			String orderid="";
			if(StringUtil.isEmpty(order.getUserId())||(order.getUserId().equalsIgnoreCase("-1")))
			{
				//orderid=this.createOrderIdargnew(order.getId(),order.getStoreId(),order.getType(),false);//获取运单的运单号
				orderid=this.createOrderIdargnewMT(order.getId(),order.getStoreId(),order.getType());//获取美淘运单的运单号
			}
			else
			{
				//orderid=this.createOrderIdargnew(order.getId(),order.getStoreId(),order.getType(),true);//获取运单的运单号
				orderid=this.createOrderIdargnewMT(order.getId(),order.getStoreId(),order.getType());//获取美淘运单的运单号
			}
				
			
			order.setOrderId(orderid);
			int kk=this.m_orderDao.updateOrderid(order.getId(),orderid);
			if(kk==0)
			{
				throw new Exception("修改改运单号失败!");
			}
			//开始插入商品信息
			for(M_OrderDetail m:order.getDetail())
			{
				m.setOrderId(orderid);
				m.setCtype(order.getType());
			}
			int mm=this.m_DetailDao.insertMDetail(order.getDetail());
			
			if(mm==order.getDetail().size())
			{
				
			}
			else
			{
				throw new Exception("插入商品信息出错!");
			}
			
			ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			obj.setData(order.getId());
			obj.setMessage(order.getOrderId());
			return obj;
		}
		 catch (Exception e) {
				//throw ExceptionUtil.handle2ServiceException(e.getMessage());
			 return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION," 添加在线置单发生异常！");
			}
	}
	
	
	
	//添加在线置单
	public ResponseObject<Object> modify_online_Morder(M_order order,String rflag,String sflag,String reflag) throws ServiceException
	{
		
		if(order==null)
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错!");
		}
		try {
			
			if(!StringUtil.isEmpty(reflag)&&(reflag.equalsIgnoreCase("1")))//已经定不管是否重复都保存
			{}
			else
			{
				String addressstr="";//如果地址簿中存发件人或收件人的电话，返回警告信息
				 if(!StringUtil.isEmpty(sflag)&&(sflag.equalsIgnoreCase("1")))//保存发件人到用户地址簿中，先检查发件人中电话是否存在
				 {
					 int k=this.consigneeSendpersonDao.getcountuserphone(order.getSuser().getPhone(), order.getUserId());
					 if(k>0)
					 {
						 addressstr="发件人电话";
					 }
				 }
				 if(!StringUtil.isEmpty(rflag)&&(rflag.equalsIgnoreCase("1")))//保存收件人到用户地址簿中
				 {
					 int k=this.consigneeInfoDao.getcountuserphone(order.getRuser().getPhone(), order.getUserId());
					 if(k>0)
					 {
						 if(addressstr.equalsIgnoreCase(""))
						 {
							 addressstr="收件人电话";
						 }
						 else
						 {
							 addressstr=addressstr+",收件人电话";
						 }
						 
					 }
				 }
			
				 if(!addressstr.equalsIgnoreCase(""))//保存地址出错
				 {
					 return new ResponseObject<Object>(ResponseCode.sr_address_ERROR,addressstr);
				 }
			
			}
			
			
			
			
			String date = DateUtil.date2String(new Date());
			order.setCreateDate(date);
			order.setModifyDate(date);
			order.setPayornot(Constant.ORDER_PAY_STATE0);//状态为未付款
			
			
			if(order.getRuser()!=null)
			{
				order.getRuser().setCreateDate(date);
				order.getRuser().setModifyDate(date);
				order.getRuser().setUserId(order.getUserId());
				order.getRuser().setCardid_flag(Constant.VERFY_CARDID__10);
				int k=this.receive_UserDao.updateRuser(order.getRuser());//更新原有的地址信息
				//int k=this.receive_UserDao.insertReceiveUser(order.getRuser());//插入收件人信息
				  if(k>0)
				  {
					  order.setRuserId(order.getRuser().getId());
				  }
				  else
				  {
					  throw new Exception("修改收件人用户失败");
				  }
				  
				  if(!StringUtil.isEmpty(rflag)&&(rflag.equalsIgnoreCase("1")))//保存收件人到用户地址簿中
				  {
					  if(!StringUtil.isEmpty(order.getUserId()))//
					  {
						  ConsigneeInfo info=new ConsigneeInfo();
						  info.setCardId(order.getRuser().getCardid());
						  info.setCardUrl(order.getRuser().getCardurl());
						  info.setCardurlother(order.getRuser().getCardother());
						  info.setCardurltogether(order.getRuser().getCardtogether());
						  info.setCity(order.getRuser().getCity());
						  info.setDistrict(order.getRuser().getDist());
						  info.setLastDate(date);
						  info.setName(order.getRuser().getName());
						  info.setPhone(order.getRuser().getPhone());
						  info.setProvince(order.getRuser().getState());
						  info.setStreetAddress(order.getRuser().getAddress());
						  info.setUserId(order.getUserId());
						  info.setZipCode(order.getRuser().getZipcode());
						  int n=this.consigneeInfoDao.insertConsigneeInfo(info);
						  if(n<1)
						  {
							  throw new Exception("添加收件人失败");
						  }
					  }
					  
				  }
			}
			else
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"收件人为空!");
			}
			if(order.getSuser()!=null)
			{
				order.getSuser().setCreateDate(date);
				order.getSuser().setModifyDate(date);
				order.getSuser().setUserId(order.getUserId());
			 // int k=this.send_UserDao.insertSendUser(order.getSuser());//插入发件人信息
				int k=this.send_UserDao.updateSuser(order.getSuser());
			  if(k>0)
			  {
				  order.setSuserId(order.getSuser().getId());
			  }
			  else
			  {
				  throw new Exception("修改发件人用户失败");
			  }
			  
			  if(!StringUtil.isEmpty(sflag)&&(sflag.equalsIgnoreCase("1")))//保存收件人到用户地址簿中
			  {
				  if(!StringUtil.isEmpty(order.getUserId()))//
				  {
					  ConsigneeSendperson info=new ConsigneeSendperson();
					  info.setCity(order.getSuser().getCity());
					  info.setCompany(order.getSuser().getCompany());
					  info.setCreateDate(date);
					  info.setDistrict(order.getSuser().getDist());
					  info.setEmail(order.getSuser().getEmail());
					  info.setModifyDate(date);
					  info.setName(order.getSuser().getName());
					  info.setPhone(order.getSuser().getPhone());
					  info.setProvince(order.getSuser().getState());
					  info.setStreetAddress(order.getSuser().getAddress());
					  info.setUserId(order.getUserId());
					  info.setZipCode(order.getSuser().getZipcode());
					  
					 
					  int n=this.consigneeSendpersonDao.insert(info);
					  if(n<1)
					  {
						  throw new Exception("添加发件人失败");
					  }
				  }
				  
			  }
			
			}
			else
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"发件人为空!");
			}
			
			
			
			
			
			order.setPayornot(Constant.ORDER_PAY_STATE0);//设置为未付款状态，1表示已经付款，其它表示付款异常状态
			//int k=this.m_orderDao.insertMorder(order);
			int k=this.m_orderDao.updateMorder(order);
			if(k==0)
			{
				throw new Exception("插入运单失败");
			}
			
			//开始插入商品信息
			//M_order order1=this.m_orderDao.getById(order.getId(), null);
			for(M_OrderDetail m:order.getDetail())
			{
				m.setOrderId(order.getOrderId());
				m.setCtype(order.getType());
			}
			List<String> list =new ArrayList<String>();
			list.add(order.getOrderId());
			int kk=this.m_DetailDao.deleteByOrderIds(list);
			int mm=0;
			if(kk<1)
			{
				throw new Exception("删除商品信息失败!");
			}
			else
			{
				mm=this.m_DetailDao.insertMDetail(order.getDetail());
			}
			
			
			if(mm==order.getDetail().size())
			{
				
			}
			else
			{
				throw new Exception("插入商品信息出错!");
			}
			
			ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			obj.setData(order.getId());
			return obj;
		}
		 catch (Exception e) {
				//throw ExceptionUtil.handle2ServiceException(e.getMessage());
			 return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION," 修改在线置单发生异常！");
			}
	}
	
	//修改门市提交的在线置单
	public ResponseObject<Object> modify_ms_Morder(M_order order) throws ServiceException
	{
		
		if(order==null)
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错!");
		}
		try {
			String date = DateUtil.date2String(new Date());
			order.setCreateDate(date);
			order.setModifyDate(date);
			
			
			
			if(order.getRuser()!=null)
			{
				order.getRuser().setCreateDate(date);
				order.getRuser().setModifyDate(date);
				order.getRuser().setUserId(order.getUserId());
				//int k=this.receive_UserDao.insertReceiveUser(order.getRuser());//插入收件人信息
				int k=this.receive_UserDao.updateRuser(order.getRuser());//更新原有的地址信息
				  if(k>0)
				  {
					  order.setRuserId(order.getRuser().getId());
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
			if(order.getSuser()!=null)
			{
				order.getSuser().setCreateDate(date);
				order.getSuser().setModifyDate(date);
				order.getSuser().setUserId(order.getUserId());
			  //int k=this.send_UserDao.insertSendUser(order.getSuser());//插入发件人信息
			  int k=this.send_UserDao.updateSuser(order.getSuser());
			  if(k>0)
			  {
				  order.setSuserId(order.getSuser().getId());
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
			
			if(!StringUtil.isEmpty(order.getPrintway())&&(order.getPrintway().equalsIgnoreCase("1")))//热敏4x6海关打印
			{
				//获取海关单
				SeaNumber seano=seaNumberDao.getone();
				if((seano==null)||(StringUtil.isEmpty(seano.getOrderId())))
				{
					if(seano!=null)
					{
						seano.setState("1");
						seano.setModifyDate(date);
						int a=seaNumberDao.updatestate(seano);
					}
					throw new Exception("获取海关号失败!");
				}else
				{
					order.setSorderId(seano.getOrderId());
					seano.setState("1");
					seano.setModifyDate(date);
					int a=seaNumberDao.updatestate(seano);
					if(a==0)
					{
						throw new Exception("修改海关单状态失败!");
					}
				}
				
				//匹配身份证
			}
			
			
			
			order.setPayornot(Constant.ORDER_PAY_STATE0);//设置为未付款状态，1表示已经付款，其它表示付款异常状态
			//int k=this.m_orderDao.insertMorder(order);
			//int k=this.m_orderDao.updateMorder(order);
			int k=this.m_orderDao.updateMorder_cost(order);
			
			if(k==0)
			{
				throw new Exception("插入运单失败");
			}
			//String orderid=this.createOrderIdarg(order.getId(),order.getStoreId(),order.getType());//获取运单的运单号
			//String orderid=this.createOrderIdargnew(order.getId(),order.getStoreId(),order.getType());//获取运单的运单号
			
			//order.setOrderId(orderid);
			/*int kk=this.m_orderDao.updateOrderid(order.getId(),orderid);
			if(kk==0)
			{
				throw new Exception("修改改运单号失败!");
			}*/
			//开始插入商品信息
			for(M_OrderDetail m:order.getDetail())
			{
				m.setOrderId(order.getOrderId());
				m.setCtype(order.getType());
			}
			List<String> list =new ArrayList<String>();
			list.add(order.getOrderId());
			int kk=this.m_DetailDao.deleteByOrderIds(list);
			int mm=0;
			if(kk<1)
			{
				throw new Exception("删除商品信息失败!");
			}
			else
			{
				mm=this.m_DetailDao.insertMDetail(order.getDetail());
			}
			
			
			//int mm=this.m_DetailDao.insertMDetail(order.getDetail());
			
			if(mm==order.getDetail().size())
			{
				User user=null;
				if(!StringUtil.isEmpty(order.getUserId()))
				{
					user=this.userDao.getUserById(order.getUserId());
				}
				//开始扣除费用记录
				if ("0".equals(order.getPayWay())) {//余额支付
				
					if(user==null)
					{
						throw new Exception("用户账户不存在，不能进行余额支付!");
					}
					double totalMoney = new BigDecimal(order.getTotalmoney())
							.doubleValue();
					if(totalMoney<0)
					{
						throw new Exception("运费不能小于0!");
					}
					
					double rmb = StringUtil.string2Double(user.getRmbBalance());
					double usd = StringUtil.string2Double(user.getUsdBalance());
					
					double usdrate=0;
					ResponseObject<String> obj=this.getCurUsaToCn();
					if((obj.getCode()!=null)&&(obj.getCode().equalsIgnoreCase(ResponseCode.SUCCESS_CODE)))
					{
						if((obj.getData()!=null)&&(!obj.getData().equals("")))
						{
							String money00=obj.getData();
							double dou = Double.parseDouble(money00);
							if(dou>0)
							{
								usdrate=dou;
							}
						}
					}
					
				
					
						double newusd = usd - totalMoney; // 先用美元支付
						double newrmb = rmb;
						if (newusd >= 0) {
							
							ResponseObject<Object> obj1=this.payM_order(Arrays
								.asList(new String[] { order.getId() }), Arrays
								.asList(new String[] { order.getOrderId() }),
								order.getUserId(), order.getTotalmoney(),
								newrmb, newusd, true,order);
							if(!obj1.getCode().equalsIgnoreCase(ResponseCode.SUCCESS_CODE))
							{
								throw new Exception("支付出现异常");
							}
							
							// ignore
						
						} else {
							newusd = 0.0D; // 美元余额全部支付，开始扣人民币的钱
							newrmb = new BigDecimal(rmb - (totalMoney - usd)
									* usdrate).setScale(2,
									BigDecimal.ROUND_HALF_UP).doubleValue();
							if(newrmb<0)
							{
								throw new Exception("余额不足，支付失败!");
							}
							ResponseObject<Object> obj1=this.payM_order(Arrays
									.asList(new String[] { order.getId() }), Arrays
									.asList(new String[] { order.getOrderId() }),
									order.getUserId(), order.getTotalmoney(),
									newrmb, newusd, true,order);
							if(!obj1.getCode().equalsIgnoreCase(ResponseCode.SUCCESS_CODE))
							{
								throw new Exception("支付出现异常");
							}
						}
				}
				else if("1".equals(order.getPayWay()))//现金支付
				{
					String userid="-1";
					if(!StringUtil.isEmpty(order.getUserId()))
					{
						userid=order.getUserId();
					}
					ResponseObject<Object> obj1=this.payM_order(Arrays
							.asList(new String[] { order.getId() }), Arrays
							.asList(new String[] { order.getOrderId() }),
							userid, order.getTotalmoney(),
							0, 0, false,order);
					if(!obj1.getCode().equalsIgnoreCase(ResponseCode.SUCCESS_CODE))
					{
						throw new Exception("支付出现异常");
					}
				}
				else
				{
					throw new Exception("支付方式不正确");
				}
						
				
				
				
				
				// 插入完成后，新建一个Route并插入到数据库中
				/*Route route = new Route();
				route.setOrderId(orderid);
				route.setName("");
				route.setDate(date);
				route.setState(OrderUtil.transformerState(0,
						order.getState()));
				route.setRemark("门市添加，运单生成");
				this.routeDao.insertRoute(route);*/
			}
			else
			{
				throw new Exception("插入商品信息出错!");
			}
			//最后判断是否给用户发送短信
			if((order.getAutomessage()!=null)&&(order.getAutomessage().equalsIgnoreCase("1")))
			{
				try{
					
					
					
					int rmb_nos=0;
					int usa_nos=0;
					if(order.getSuser().getPhone().length()==10)
					{
						String message="your tracking number:"+order.getOrderId()+",Price:$"+order.getTotalmoney()+",Weight:"+order.getWeight()+" lb";
						this.sendEnglishMessage(message, order.getSuser().getPhone());
						usa_nos++;
					}
					else
					{
						String message="你的运单号为:"+order.getOrderId()+",价格:$"+order.getTotalmoney()+",重量:"+order.getWeight()+"磅";;
						this.sendChinaMessage(message, order.getSuser().getPhone());
						rmb_nos++;
					}
					
					try{
						MessageRecord record =new MessageRecord();
						record.setModifyDate(date);
						record.setRemark("操作人号："+order.getEmployeeId()+",运单号："+order.getOrderId());
						record.setCreateDate(date);
						record.setRmb_nos(String.valueOf(rmb_nos));
						record.setUsa_nos(String.valueOf(usa_nos));
						record.setType(Constant.SEND_MESSAGE_TYPE5);//
						this.messageRecordDao.insert(record);
					}catch(Exception e)
					{
						
					}
					
					/*String content="Dear Customer,your tracking number:"+order.getOrderId()+",Price:$"+order.getTotalmoney()+". Please DO NOT reply this message.";
					String phone="";
					String companyTitle="";
					if(order.getSuser().getPhone().length()==10)
					{
						phone="+1"+order.getSuser().getPhone();
						companyTitle = globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_COMPANY_TITLE_EN);
					}
					else if(order.getSuser().getPhone().length()==11)
					{
						phone="+86"+order.getSuser().getPhone();
						companyTitle = globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_COMPANY_TITLE_CN);
					}
					else//电话号码填写不正确
					{
					
					}
					if(!phone.equalsIgnoreCase(""))
					{
						content="【"+companyTitle+"】"+content;
						SimpleSmsSender simpleSmsSender = new SimpleSmsSender(globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_API_KEY));
						String aaa=simpleSmsSender.sendSms(content,phone);
						aaa="["+aaa+"]";
						List<Map<String, String>> list1 = jsonStringToList(aaa);
						String code1="";
						String msg1="";
						String result1="";
						for(Map<String, String> a:list1)
						{
							 code1=a.get("code");
							 msg1=a.get("msg");
							 result1=a.get("result");
						}
						
						
						log.info(aaa);
						if(code1.equals("0"))//表示发送成功
						{
							
						}
						else
						{
						
						}
					}*/
				}catch(Exception e)
				{}
			}
			ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			obj.setData(order.getId());
			return obj;
		}
		 catch (Exception e) {
				//throw ExceptionUtil.handle2ServiceException(e.getMessage());
			 return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION," 修改门市运单发生异常！");
			}
	}
	
	
	
	
	//直接修改或支付运单,payflag为1表示直接支付
	public ResponseObject<Object> modify_orpay_Morder(M_order order,boolean changepayornot,boolean changestate,String payflag,String emplyeeId,String storeId) throws ServiceException
	{
		
		if(order==null)
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错!");
		}
		try {
			String date = DateUtil.date2String(new Date());
			order.setCreateDate(date);
			order.setModifyDate(date);
			
			
			
			if(order.getRuser()!=null)
			{
				order.getRuser().setCreateDate(date);
				order.getRuser().setModifyDate(date);
				order.getRuser().setUserId(order.getUserId());
				
				//int k=this.receive_UserDao.insertReceiveUser(order.getRuser());//插入收件人信息
				int k=this.receive_UserDao.updateRuser(order.getRuser());//更新原有的地址信息
				  if(k>0)
				  {
					  order.setRuserId(order.getRuser().getId());
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
			if(order.getSuser()!=null)
			{
				order.getSuser().setCreateDate(date);
				order.getSuser().setModifyDate(date);
				order.getSuser().setUserId(order.getUserId());
			  //int k=this.send_UserDao.insertSendUser(order.getSuser());//插入发件人信息
			  int k=this.send_UserDao.updateSuser(order.getSuser());
			  if(k>0)
			  {
				  order.setSuserId(order.getSuser().getId());
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
		
			
			
			if(!StringUtil.isEmpty(payflag)&&(payflag.equalsIgnoreCase("1")))//要进行付款的
			{
				order.setPayornot(Constant.ORDER_PAY_STATE0);//设置为未付款状态，1表示已经付款，其它表示付款异常状态
			}
			//int k=this.m_orderDao.insertMorder(order);
			//int k=this.m_orderDao.updateMorder(order);
			int k=this.m_orderDao.updateMorder_cost(order);
			if(k==0)
			{
				throw new Exception("插入运单失败");
			}
			//String orderid=this.createOrderIdarg(order.getId(),order.getStoreId(),order.getType());//获取运单的运单号
			//String orderid=this.createOrderIdargnew(order.getId(),order.getStoreId(),order.getType());//获取运单的运单号
			
			//order.setOrderId(orderid);
			/*int kk=this.m_orderDao.updateOrderid(order.getId(),orderid);
			if(kk==0)
			{
				throw new Exception("修改改运单号失败!");
			}*/
			//开始插入商品信息
			for(M_OrderDetail m:order.getDetail())
			{
				m.setOrderId(order.getOrderId());
				m.setCtype(order.getType());
			}
			List<String> list =new ArrayList<String>();
			list.add(order.getOrderId());
			int kk=this.m_DetailDao.deleteByOrderIds(list);//先删除原来的
			int mm=0;
			if(kk<1)
			{
				throw new Exception("删除商品信息失败!");
			}
			else
			{
				mm=this.m_DetailDao.insertMDetail(order.getDetail());
			}
			
			
			//int mm=this.m_DetailDao.insertMDetail(order.getDetail());
			
			if(mm==order.getDetail().size())//插入商品成功
			{
				if(!StringUtil.isEmpty(payflag)&&(payflag.equalsIgnoreCase("1")))//要进行付款的
				{
					User user=null;
					if(!StringUtil.isEmpty(order.getUserId()))
					{
						user=this.userDao.getUserById(order.getUserId());
					}
					//开始扣除费用记录
					if ("0".equals(order.getPayWay())) {//余额支付
					
						if(user==null)
						{
							throw new Exception("用户账户不存在，不能进行余额支付!");
						}
						double totalMoney = new BigDecimal(order.getTotalmoney())
								.doubleValue();
						if(totalMoney<0)
						{
							throw new Exception("运费不能小于0!");
						}
						
						double rmb = StringUtil.string2Double(user.getRmbBalance());
						double usd = StringUtil.string2Double(user.getUsdBalance());
						
						double usdrate=0;
						ResponseObject<String> obj=this.getCurUsaToCn();
						if((obj.getCode()!=null)&&(obj.getCode().equalsIgnoreCase(ResponseCode.SUCCESS_CODE)))
						{
							if((obj.getData()!=null)&&(!obj.getData().equals("")))
							{
								String money00=obj.getData();
								double dou = Double.parseDouble(money00);
								if(dou>0)
								{
									usdrate=dou;
								}
							}
						}
						
					
						
							double newusd = usd - totalMoney; // 先用美元支付
							double newrmb = rmb;
							if (newusd >= 0) {
								
								ResponseObject<Object> obj1=this.payM_order_state(Arrays
									.asList(new String[] { order.getId() }), Arrays
									.asList(new String[] { order.getOrderId() }),
									order.getUserId(), order.getTotalmoney(),
									newrmb, newusd, true,order.getState(),emplyeeId,storeId);
								if(!obj1.getCode().equalsIgnoreCase(ResponseCode.SUCCESS_CODE))
								{
									throw new Exception("支付出现异常");
								}
								
								// ignore
							
							} else {
								newusd = 0.0D; // 美元余额全部支付，开始扣人民币的钱
								newrmb = new BigDecimal(rmb - (totalMoney - usd)
										* usdrate).setScale(2,
										BigDecimal.ROUND_HALF_UP).doubleValue();
								if(newrmb<0)
								{
									throw new Exception("余额不足，支付失败!");
								}
								ResponseObject<Object> obj1=this.payM_order_state(Arrays
										.asList(new String[] { order.getId() }), Arrays
										.asList(new String[] { order.getOrderId() }),
										order.getUserId(), order.getTotalmoney(),
										newrmb, newusd, true,order.getState(),emplyeeId,storeId);
								if(!obj1.getCode().equalsIgnoreCase(ResponseCode.SUCCESS_CODE))
								{
									throw new Exception("支付出现异常");
								}
							}
					}
					else if("1".equals(order.getPayWay()))//现金支付
					{
						String userid="-1";
						if(!StringUtil.isEmpty(order.getUserId()))
						{
							userid=order.getUserId();
						}
						ResponseObject<Object> obj1=this.payM_order_state(Arrays
								.asList(new String[] { order.getId() }), Arrays
								.asList(new String[] { order.getOrderId() }),
								userid, order.getTotalmoney(),
								0, 0, false,order.getState(),emplyeeId,storeId);
						if(!obj1.getCode().equalsIgnoreCase(ResponseCode.SUCCESS_CODE))
						{
							throw new Exception("支付出现异常");
						}
					}
					else
					{
						throw new Exception("支付方式不正确");
					}
							
					
				}	
				else//不支付，要记录支付状态的变更及路由状态的变更
				{
					//boolean changepayornot,boolean changestate,
					if(changepayornot)//支付状态变更
					{

						String payname="";
						if(order.getPayornot().equalsIgnoreCase(Constant.ORDER_PAY_STATE1))
						{
							payname="已付款";
						}
						else
						{
							payname="未付款";
						}
						
						AccountDetail detail = new AccountDetail();
						detail.setAmount(order.getTotalmoney());
						detail.setCreateDate(date);
						detail.setModifyDate(date);
						detail.setState(Constant.ACCOUNT_DETAIL_STATE4);
						detail.setCurrency("美元");
						detail.setName("门市标记"+payname);
						detail.setConfirm_state("0");
						detail.setType(Constant.ACCOUNT_DETAIL_TYPE3);
						
						Employee emp=this.employeeDao.getEmployeeById(emplyeeId);
						if(emp!=null)
						{
							detail.setEmpId(emplyeeId);
							detail.setEmpName(emp.getAccount());
						}
						
						Warehouse ware=this.warehouseDao.getById(storeId);
						if(ware!=null)
						{
							detail.setStoreId(ware.getId());
							detail.setEmpStore(ware.getName());
						}
						
						
						if(StringUtil.isEmpty(order.getUserId()))
						{
							detail.setUserId("-1");
						}
						else
						{
							detail.setUserId(order.getUserId());
						}
						String prefix="";
						 prefix = "修改支付状态";

						if(StringUtil.isEmpty(order.getQremark()))
						{
							detail.setRemark(prefix + "；运单id：" + order.getOrderId()+",操作员工号："+emplyeeId);
						}
						else
						{
							detail.setRemark(prefix + "；运单id：" + order.getOrderId()+",操作员工号："+emplyeeId);
						}
						this.accountDetailDao.insertAccountDetail(detail);
					
					}
					
					if(changestate)
					{
						Route route = new Route();
						route.setOrderId(order.getOrderId());
						route.setName("");
						route.setDate(date);
						route.setState(OrderUtil.transformerState(0,
								order.getState()));
						route.setRemark("修改状态");
						this.routeDao.insertRoute(route);
					}
				}
					
					
					// 插入完成后，新建一个Route并插入到数据库中
					/*Route route = new Route();
					route.setOrderId(orderid);
					route.setName("");
					route.setDate(date);
					route.setState(OrderUtil.transformerState(0,
							order.getState()));
					route.setRemark("门市添加，运单生成");
					this.routeDao.insertRoute(route);*/
			}
			else
			{
				throw new Exception("插入商品信息出错!");
			}
			//最后判断是否给用户发送短信
			if((order.getAutomessage()!=null)&&(order.getAutomessage().equalsIgnoreCase("1")))
			{
				try{
					
					
					int rmb_nos=0;
					int usa_nos=0;
					if(order.getSuser().getPhone().length()==10)
					{
						String message="your tracking number:"+order.getOrderId()+",Price:$"+order.getTotalmoney()+",Weight:"+order.getWeight()+" lb";;
						this.sendEnglishMessage(message, order.getSuser().getPhone());
						usa_nos++;
					}
					else
					{
						String message="你的运单号为:"+order.getOrderId()+",价格:$"+order.getTotalmoney()+",重量:"+order.getWeight()+"磅";;
						this.sendChinaMessage(message, order.getSuser().getPhone());
						rmb_nos++;
					}
					
					
					
					try{
						MessageRecord record =new MessageRecord();
						record.setModifyDate(date);
						record.setRemark("运单修改,运单号："+order.getOrderId());
						record.setCreateDate(date);
						record.setRmb_nos(String.valueOf(rmb_nos));
						record.setUsa_nos(String.valueOf(usa_nos));
						record.setType(Constant.SEND_MESSAGE_TYPE5);//
						this.messageRecordDao.insert(record);
					}catch(Exception e)
					{
						
					}
					/*String content="Dear Customer,your tracking number:"+order.getOrderId()+",Price:$"+order.getTotalmoney()+". Please DO NOT reply this message.";
					String phone="";
					String companyTitle="";
					if(order.getSuser().getPhone().length()==10)
					{
						phone="+1"+order.getSuser().getPhone();
						companyTitle = globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_COMPANY_TITLE_EN);
					}
					else if(order.getSuser().getPhone().length()==11)
					{
						phone="+86"+order.getSuser().getPhone();
						companyTitle = globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_COMPANY_TITLE_CN);
					}
					else//电话号码填写不正确
					{
					
					}
					if(!phone.equalsIgnoreCase(""))
					{
						content="【"+companyTitle+"】"+content;
						SimpleSmsSender simpleSmsSender = new SimpleSmsSender(globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_API_KEY));
						String aaa=simpleSmsSender.sendSms(content,phone);
						aaa="["+aaa+"]";
						List<Map<String, String>> list1 = jsonStringToList(aaa);
						String code1="";
						String msg1="";
						String result1="";
						for(Map<String, String> a:list1)
						{
							 code1=a.get("code");
							 msg1=a.get("msg");
							 result1=a.get("result");
						}
						
						
						log.info(aaa);
						if(code1.equals("0"))//表示发送成功
						{
							
						}
						else
						{
						
						}
					}*/
				}catch(Exception e)
				{}
			}
			ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			obj.setData(order.getId());
			return obj;
		}
		 catch (Exception e) {
				//throw ExceptionUtil.handle2ServiceException(e.getMessage());
			 return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION," 修改发生异常！");
			}
	}
	
	
	
	
	
	//添加第三方运单信息
	public ResponseObject<Object> import_third_orders(ImportthirdMorder iorders) throws ServiceException
	{
				
				if(iorders==null||iorders.getOrders()==null)
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"导入信息不能为空!");
				}
				if(StringUtil.isEmpty(iorders.getChannelId())||StringUtil.isEmpty(iorders.getStoreId()))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错，必须包含门店和渠道!");
				}
				//
				try {
					for(M_order order:iorders.getOrders())
					{
						//首先验证合法性
						//运单号信息
						if(StringUtil.isEmpty(order.getOrderId())||StringUtil.isEmpty(order.getOrderId().trim()))
						{
							
							order.setResult("失败!运单号不能为空");
							continue;
						}
						else
						{
							if(order.getOrderId().trim().length()<5)
							{
								order.setResult("失败!运单号长度不能小于5个字符");
								continue;
							}
							int k=this.m_orderDao.countordersbyid(order.getOrderId().trim());
							if(k>0)
							{
								order.setResult("失败!运单号已存在");
								continue;
							}
						}
						//商品信息
						if(order.getDetail()==null||order.getDetail().size()<1)
						{
							order.setResult("失败!没有商品信息!");
							continue;
						}
						else
						{
							boolean dflag=true;
							for(M_OrderDetail de:order.getDetail())
							{
								if(StringUtil.isEmpty(de.getProductName()))//商品名称不能为空
								{
									
									dflag=false;
									break;
								}
								else
								{
									//一些不能为空的信息先填入值
									de.setOrderId(order.getOrderId());
									de.setCommodityId("-1");
									
									if(order.getDetail().size()==1)
									{
										de.setWeight(order.getWeight());
										de.setQuantity("1");
									}
									else
									{
										de.setWeight("0");
										de.setQuantity("0");
									}
									de.setValue("0");
									de.setTariff("0");
									de.setOther("0");
									de.setOr("0");
									de.setCtype(Constant.ORDER_TYPE_KEY_4);//记录为第三方运单
								}
							}
							if(dflag==false)
							{
								order.setResult("失败!没有商品信息!");
								continue;
							}
						}
						
						//检查发件人信息
						if(order.getSuser()==null)
						{
							order.setResult("失败!发件人信息不能为空!");
							continue;
						}
						//检查收件人信息
						if(order.getRuser()==null)
						{
							order.setResult("失败!收件人信息不能为空!");
							continue;
						}
						else
						{
							order.getRuser().setCardlibId("-1");
						}
						//检查状态信息
						if(StringUtil.isEmpty(order.getState()))
						{
							order.setResult("失败！状态值不能为空！");
							continue;
						}
						else
						{
							try{
								int state=Integer.parseInt(order.getState().trim());
								if(state<2||state>9)
								{
									order.setResult("失败！状态值取值范围为2到9！");
									continue;
								}
							}catch(Exception e){
								order.setResult("失败！状态值不正确！");
								continue;
							}
						}
						//检查运单信息
						
						
						
						order.setType(Constant.ORDER_TYPE_KEY_4);//设置为第三方运单类型
						order.setUserId("-1");//不归属于某个用户
						order.setPayWay("-1");//支付方式
						if(StringUtil.isEmpty(order.getTotalmoney()))
						{
							order.setTotalmoney("0");
						}
						else
						{
							try{
								double tom=Double.parseDouble(order.getTotalmoney());
								if(tom<0)
								{
									order.setResult("失败！总金额不能小于0！");
									continue;
								}
							}
							catch(Exception e)
							{
								order.setResult("失败！总金额异常！");
								continue;
							}
						}
						try{
							double weight=Double.parseDouble(order.getWeight());
							if(weight<=0)
							{
								order.setResult("失败！重量值只能大于0！");
								continue;
							}
						}catch(Exception e){
							order.setResult("失败！重量值不正确！");
							continue;
						}
						try{
							double sjweight=Double.parseDouble(order.getSjweight());
							if(sjweight<=0)
							{
								order.setResult("失败！重量值只能大于0！");
								continue;
							}
						}catch(Exception e){
							order.setResult("失败！重量值不正确！");
							continue;
						}
						order.setOther("0");
						order.setQuantity("1");
						order.setTariff("0");
						order.setValue("0");
						order.setInsurance("0");
						order.setChannelId(iorders.getChannelId());
						order.setStoreId(iorders.getStoreId());
						order.setEmployeeId(iorders.getEmpId());
						order.setTotalcost("0");
						//
						
						String date = DateUtil.date2String(new Date());
						order.setCreateDate(date);
						order.setModifyDate(date);
						order.setPayDate(date);
						
						
						
						
						
						if(order.getRuser()!=null)
						{
							order.getRuser().setCreateDate(date);
							order.getRuser().setModifyDate(date);
							order.getRuser().setUserId(order.getUserId());
							int k=this.receive_UserDao.insertReceiveUser(order.getRuser());//插入收件人信息
							  if(k>0)
							  {
								  order.setRuserId(order.getRuser().getId());
							  }
							  else
							  {
								  order.setResult("失败！插入收件人失败！");
									continue;
							  }
						}
						
						if(order.getSuser()!=null)
						{
							order.getSuser().setCreateDate(date);
							order.getSuser().setModifyDate(date);
							order.getSuser().setUserId(order.getUserId());
						  int k=this.send_UserDao.insertSendUser(order.getSuser());//插入发件人信息
						  if(k>0)
						  {
							  order.setSuserId(order.getSuser().getId());
						  }
						  else
						  {
							  	order.setResult("失败！插入发件人失败！");
								continue;
						  }
						
						}
						
						

						int k=this.m_orderDao.insertMorder(order);
						if(k==0)
						{
							order.setResult("失败！插入运单信息失败！");
							continue;
						}
						
						//开始插入商品信息
						for(M_OrderDetail m:order.getDetail())
						{
							m.setOrderId(order.getOrderId());
							m.setCtype(order.getType());
						}
						try{
							//删除之前可能存在的商品信息
							List<String> aaa=new ArrayList<String>();
							aaa.add(order.getOrderId());
							int kk=this.m_DetailDao.deleteByOrderIds(aaa);
						}
						catch(Exception e){}
						int mm=this.m_DetailDao.insertMDetail(order.getDetail());
						
						if(mm==order.getDetail().size())
						{
							
						}
						else
						{
							order.setResult("失败！插入商品信息失败！");
							continue;
						}
						try{
							Route route = new Route();
							route.setDate(date);
							route.setOrderId(order.getOrderId());
							route.setRemark("");
							route.setState(OrderUtil.transformerState(0, order.getState()));
							this.routeDao.insertRoute(route);
						}
						catch(Exception e)
						{
							order.setResult("插入成功，但插入状态失败，因为发生异常!");
						}
						
						order.setResult("成功!");
						
					}
					ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
					obj.setData(iorders);
					return obj;
				}
				 catch (Exception e) {
						//throw ExceptionUtil.handle2ServiceException(e.getMessage());
					 return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION," 异常！"+e.getMessage());
					}
			}
	
	
	
	
	
	//kai 20160324 入库信息查询
	public ResponseObject<PageSplit<M_order>> searchMordersInput(String oid,String i_storeId,String wid,String cid,String state,String i_state,String payway,String sdate,String edate,String empId, int pageSize, int pageNow)
	        throws ServiceException{
				try {
				
					
					if(StringUtil.isEmpty(oid))
					{
						oid=null;
					}
					else
					{
						oid = StringUtil.escapeStringOfSearchKey(oid);
					}
					

					int rowCount = 0;
					try {

						//取消与员工绑定
						rowCount =this.m_orderDao.countofSearchinputMOrders(oid, i_storeId, null, wid, cid, state, i_state, payway, sdate, edate);
						//rowCount = this.m_orderDao.countOfSearchMOrders(oid, sod, god, flyno, wid, cid, userinfo, commudityinfo, state, type,payway, sdate, edate, psdate, pedate,cardurl,cardother,cardtogether,cardid,uploadflag,payornot);
					} catch (Exception e) {
						throw ExceptionUtil.handle2ServiceException("获取运单个数失败", e);
					}

					ResponseObject<PageSplit<M_order>> responseObj = new ResponseObject<PageSplit<M_order>>(
							ResponseCode.SUCCESS_CODE);
					if (rowCount > 0) {
						pageSize = Math.max(pageSize, 1);
						int pageCount = rowCount / pageSize
								+ (rowCount % pageSize == 0 ? 0 : 1);
						pageNow = Math.min(pageNow, pageCount);
						PageSplit<M_order> pageSplit = new PageSplit<M_order>();
						pageSplit.setPageCount(pageCount);
						pageSplit.setPageNow(pageNow);
						pageSplit.setRowCount(rowCount);
						pageSplit.setPageSize(pageSize);

						int startIndex = (pageNow - 1) * pageSize;
						try {
							//List<M_order> orders =null; //this.m_orderDao.SearchMOrders(oid, sod, god, flyno, wid, cid, userinfo, commudityinfo, state, type,payway, sdate, edate, psdate, pedate,cardurl,cardother,cardtogether,cardid,uploadflag,payornot, startIndex, pageSize);
							List<M_order> orders=this.m_orderDao.SearchinputMOrders(oid, i_storeId, null, wid, cid, state, i_state, payway, sdate, edate, startIndex, pageSize);
							for(M_order o: orders)
							{
								if(!StringUtil.isEmpty(o.getUserId()))
								{
									o.setUser(this.userDao.getUserById(o.getUserId()));
								}
								/*if((o.getRuser()!=null))
								{
									if(!StringUtil.isEmpty(o.getRuser().getCardurl()))
									{
										o.getRuser().setCardurl(java.net.URLEncoder.encode(o.getRuser().getCardurl(), "utf-8"));
									}
									if(!StringUtil.isEmpty(o.getRuser().getCardother()))
									{
										o.getRuser().setCardother(java.net.URLEncoder.encode(o.getRuser().getCardother(), "utf-8"));
									}
									if(!StringUtil.isEmpty(o.getRuser().getCardtogether()))
									{
										o.getRuser().setCardtogether(java.net.URLEncoder.encode(o.getRuser().getCardtogether(), "utf-8"));
									}
									
								}*/
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
				} catch (ServiceException e) {
					//throw e;
					 return new ResponseObject<PageSplit<M_order>>(ResponseCode.SHOW_EXCEPTION," 异常！"+e.getMessage());
				}
			}
	
	//根据揽收支付时间发送短信
	public ResponseObject<Object> send_rev_message(String resend,String s_date,String e_date,String messagetype) throws ServiceException
	{
		try
		{
			
			
			int usa_nos=0;
			int rmb_nos=0;
			
			
			String year1=this.globalargsDao.getcontentbyflag("cardId_modify_year");//获取更新数据库身份证时间
			//String myear="1";
			
			String modate = null;
			if(!StringUtil.isEmpty(year1));
			{
				try{
					int aa=Integer.parseInt(year1);
					if(aa>0)
					{
						modate = DateUtil.date2String(new Date());
						String oldyear=modate.substring(0, 4);
						int year0=Integer.parseInt(oldyear);
						year0=year0-aa;
						String newyear=String.valueOf(year0);
						modate=modate.replace(oldyear, newyear);
					}
					
				}catch(Exception e){
					modate=null;
				}
			}
			
			
			List<MessageTemp> messagephone=new ArrayList<MessageTemp>();//保存用户电话信息
			List<M_order> orders=this.m_orderDao.getpayDateMorder("MT%", s_date, e_date);
			
			
		for (M_order ord : orders) {
			
			
				Receive_User user=null;
				
				user=ord.getRuser();
				
				
				if(user!=null)
				{
					if(!StringUtil.isEmpty(user.getPhone())&&(!StringUtil.isEmpty(ord.getState())))
					{
						if(!StringUtil.isEmpty(resend)&&(!resend.equalsIgnoreCase("1")))//等于1的时候表示要重复发送
						{
							int existphone=this.cardIdManageDao.countphoneandname(user.getPhone(), user.getName(),modate);
							
							if(existphone!=0)
							{
								continue;
							}
						}
						
						try{
							double state=Double.parseDouble(ord.getState());
							if(state<Double.parseDouble(Constant.ORDER_STATE3)||state>Double.parseDouble(Constant.ORDER_STATE8))
							{
								continue;
							}
						}
						catch(Exception e)
						{
							continue;
						}
						 
						if((user.getPhone().length()==11)&&(user.getPhone().substring(0, 1).equalsIgnoreCase("1")))//只发中国用户,1开头的电话
						{
							boolean flag=false;
							for(MessageTemp m:messagephone)
							{
								if(user.getPhone().equalsIgnoreCase(m.getPhone()))
								{
									m.getOrderids().add(ord.getOrderId());
									m.setPhone(user.getPhone());
									//m.setState(flyinfo.getState());
									flag=true;
									break;
								}
								
							}
							if(flag==false)//之前没有同一个电话号码的
							{
								MessageTemp temp=new MessageTemp();
								temp.getOrderids().add(ord.getOrderId());
								temp.setPhone(user.getPhone());
								//temp.setState(flyinfo.getState());
								messagephone.add(temp);
							}
						}
					}
				}
			
			}
			
			
		//开始发送已揽收通知
		int countsendno=0;
		for(MessageTemp m:messagephone)
		{
			if(!ConsigneeInfoUtil.validatePhone(m.getPhone()))//电话号码不正确
			{
				m.setRemark("发送失败：收件人电话号码不正确!");
			}
			else
			{
				if(m.getOrderids().size()==1)//只有一个接收用户
				{
					Set<String> orderids=m.getOrderids();
					String orderid="";
					for (String str :orderids) {  
						orderid=str;  
					}  
					String message="";
					String result="";
					if(m.getPhone().length()==10)
					{
						//message="your tracking number:"+orderid+",state:"+OrderUtil.transformerState(2,m.getState());
						//result=this.m_orderService.sendEnglishMessage(message,m.getPhone());
						result="不是中国电话号码";
					}
					else
					{
						
						message="包裹"+orderid+"已揽收，请及时上传身份证信息";
						result=this.sendChinaMessage_havenull(message,m.getPhone());
						countsendno++;
						rmb_nos++;
					}
					//String result=sendEnglishMessage(message,m.getPhone());
					if((result!=null)&&(result.equalsIgnoreCase("0")))//表示发送成功
					{
						m.setRemark("发送成功！");
					}
					else
					{
						m.setRemark("发送失败:"+result);
					}
				}
				else if(m.getOrderids().size()>1)//包含运单数超过1
				{
					String orderid="";
					for (String str :m.getOrderids()) {  
						orderid=str;  
						break;
					}  
					String message="";
					String result="";
					if(m.getPhone().length()==10)
					{
						//message="the state of your "+m.getOrderids().size()+" packages is "+OrderUtil.transformerState(2,m.getState());
						//result=this.m_orderService.sendEnglishMessage(message,m.getPhone());
						result="不是中国电话号码";
					}
					else
					{
						
						message="包裹"+orderid+"等"+m.getOrderids().size()+"个已揽收，请及时上传身份证信息";
						result=this.sendChinaMessage_havenull(message,m.getPhone());
						countsendno++;
						rmb_nos++;
					}
					//String result=sendEnglishMessage(message,m.getPhone());
					if((result!=null)&&(result.equalsIgnoreCase("0")))//表示发送成功
					{
						String sss="";
						for(String a:m.getOrderids())
						{
							if(sss.equalsIgnoreCase(""))
							{
								sss=a;
							}
							else
							{
								sss=sss+","+a;
							}
						}
						m.setRemark("发送成功！合并"+m.getOrderids().size()+"条运单发送!包含："+sss);
					}
					else
					{
						m.setRemark("发送失败:"+result);
					}
				}
			}
			
			
		}
			
		String date=DateUtil.date2String(new Date());
		try{
			MessageRecord record =new MessageRecord();
			record.setModifyDate(date);
			record.setRemark(messagetype);
			record.setCreateDate(date);
			record.setRmb_nos(String.valueOf(rmb_nos));
			record.setUsa_nos(String.valueOf(usa_nos));
			record.setType(Constant.SEND_MESSAGE_TYPE0);//
			this.messageRecordDao.insert(record);
		}catch(Exception e){}
		ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
		obj.setData(countsendno);
			
			return obj;
		}
		catch(Exception e)//
		{
			e.printStackTrace();
			 return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION," 操作异常！"+e.getMessage());
		}
	}
	
	
	
	//kai 用户是否支付，限定状态必须大于1
	public ResponseObject<PageSplit<M_order>> searchMordersbyUserpayornot(String userId,String orderId,String info,String payornot, int pageSize, int pageNow)
	        throws ServiceException{
					try {
						
						if(StringUtil.isEmpty(info))
						{
							info=null;
						}
						else
						{
							info = StringUtil.escapeStringOfSearchKey(info);
						}
						
					
					
						int rowCount = 0;
						try {
							rowCount=this.m_orderDao.countOfSearchMOrdersbyUserpayornot(userId, orderId, null, null, info, null, Constant.ORDER_STATE3, null, Constant.ORDER_PAY_STATE0);
							//rowCount = this.m_orderDao.countOfSearchMOrders(oid, sod, god, flyno, wid, cid, userinfo, commudityinfo, state, type,payway, sdate, edate, psdate, pedate,cardurl,cardother,cardtogether,cardid,uploadflag,payornot);
						} catch (Exception e) {
							throw ExceptionUtil.handle2ServiceException("获取运单个数失败", e);
						}

						ResponseObject<PageSplit<M_order>> responseObj = new ResponseObject<PageSplit<M_order>>(
								ResponseCode.SUCCESS_CODE);
						if (rowCount > 0) {
							pageSize = Math.max(pageSize, 1);
							int pageCount = rowCount / pageSize
									+ (rowCount % pageSize == 0 ? 0 : 1);
							pageNow = Math.min(pageNow, pageCount);
							PageSplit<M_order> pageSplit = new PageSplit<M_order>();
							pageSplit.setPageCount(pageCount);
							pageSplit.setPageNow(pageNow);
							pageSplit.setRowCount(rowCount);
							pageSplit.setPageSize(pageSize);

							int startIndex = (pageNow - 1) * pageSize;
							try {
								//List<M_order> orders = this.m_orderDao.SearchMOrders(oid, sod, god, flyno, wid, cid, userinfo, commudityinfo, state, type,payway, sdate, edate, psdate, pedate,cardurl,cardother,cardtogether,cardid,uploadflag,payornot, startIndex, pageSize);
								List<M_order> orders=this.m_orderDao.SearchMOrdersbyUserpayornot(userId, orderId, null, null, info, null, Constant.ORDER_STATE3, null, Constant.ORDER_PAY_STATE0, startIndex, pageSize);
										//.SearchMOrdersbyUser(userId, oid, wid, cid, info, state, type, payornot, startIndex, pageSize);
								for(M_order o: orders)
								{
									if(!StringUtil.isEmpty(o.getUserId()))
									{
										o.setUser(this.userDao.getUserById(o.getUserId()));
									}
									if(!StringUtil.isEmpty(o.getStoreId()))
									{
										Warehouse house=this.warehouseDao.getById(o.getStoreId());
										if(house!=null)
										{
											o.setStoreName(house.getName());
										}
									}
									if(!StringUtil.isEmpty(o.getChannelId()))
									{
										Channel channel=this.channelDao.getChannelById(o.getChannelId());
										if(channel!=null)
										{
											o.setChannelName(channel.getName());
										}
									}
									
									
									List<String> tids=this.morder_TorderDao.gettorderIds(o.getOrderId());
									List<T_order> torders=new ArrayList<T_order>(); 
									for(String id:tids)
									{
										T_order torder=this.torderDao.getone(null, id);
										if(torder!=null)
										{
											torders.add(torder);
										}
									}
									o.setTorders(torders);
									/*if((o.getRuser()!=null))
									{
										if(!StringUtil.isEmpty(o.getRuser().getCardurl()))
										{
											o.getRuser().setCardurl(java.net.URLEncoder.encode(o.getRuser().getCardurl(), "utf-8"));
										}
										if(!StringUtil.isEmpty(o.getRuser().getCardother()))
										{
											o.getRuser().setCardother(java.net.URLEncoder.encode(o.getRuser().getCardother(), "utf-8"));
										}
										if(!StringUtil.isEmpty(o.getRuser().getCardtogether()))
										{
											o.getRuser().setCardtogether(java.net.URLEncoder.encode(o.getRuser().getCardtogether(), "utf-8"));
										}
										
									}*/
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
					} catch (ServiceException e) {
						//throw e;
						return new ResponseObject<PageSplit<M_order>>(ResponseCode.SHOW_EXCEPTION," 获取运单列表发生异常！");
					}
				}
	
	
	
	
	
	//修改用户提交的转运单
	public ResponseObject<Object> modify_tran_Morder(M_order order,String emplyeeId,String storeId) throws ServiceException
	{
		
		if(order==null)
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错!");
		}
		try {			
			User user=this.userDao.getUserById(order.getUserId());
			order.setPayWay("0");//设置为余额支付
			order.setState(Constant.ORDER_STATE3);//状态设置为已揽收
			//开始扣除费用记录
				if(user==null)
				{
					throw new Exception("用户账户不存在，不能进行余额支付!");
				}
				double totalMoney = new BigDecimal(order.getTotalmoney())
						.doubleValue();
				if(totalMoney<0)
				{
					throw new Exception("运费不能小于0!");
				}
				
				double rmb = StringUtil.string2Double(user.getRmbBalance());
				double usd = StringUtil.string2Double(user.getUsdBalance());
				
				double usdrate=0;
				ResponseObject<String> obj=this.getCurUsaToCn();
				if((obj.getCode()!=null)&&(obj.getCode().equalsIgnoreCase(ResponseCode.SUCCESS_CODE)))
				{
					if((obj.getData()!=null)&&(!obj.getData().equals("")))
					{
						String money00=obj.getData();
						double dou = Double.parseDouble(money00);
						if(dou>0)
						{
							usdrate=dou;
						}
					}
				}
				
			
				
					double newusd = usd - totalMoney; // 先用美元支付
					double newrmb = rmb;
					if (newusd >= 0) {
						
						ResponseObject<Object> obj1=this.payM_order_state(Arrays
							.asList(new String[] { order.getId() }), Arrays
							.asList(new String[] { order.getOrderId() }),
							order.getUserId(), order.getTotalmoney(),
							newrmb, newusd, true,order.getState(),emplyeeId,storeId);
						if(!obj1.getCode().equalsIgnoreCase(ResponseCode.SUCCESS_CODE))
						{
							throw new Exception("支付出现异常");
						}
						
						// ignore
					
					} else {
						newusd = 0.0D; // 美元余额全部支付，开始扣人民币的钱
						newrmb = new BigDecimal(rmb - (totalMoney - usd)
								* usdrate).setScale(2,
								BigDecimal.ROUND_HALF_UP).doubleValue();
						if(newrmb<0)
						{
							return new ResponseObject<Object>(ResponseCode.ACCOUNT_BALANCE_NOT_ENOUGH,"余额不足,需要支付$"+order.getTotalmoney());
							//throw new Exception("余额不足，支付失败!");
						}
						ResponseObject<Object> obj1=this.payM_order_state(Arrays
								.asList(new String[] { order.getId() }), Arrays
								.asList(new String[] { order.getOrderId() }),
								order.getUserId(), order.getTotalmoney(),
								newrmb, newusd, true,order.getState(),emplyeeId,storeId);
						if(!obj1.getCode().equalsIgnoreCase(ResponseCode.SUCCESS_CODE))
						{
							throw new Exception("支付出现异常");
						}
					}
			
			String date = DateUtil.date2String(new Date());
			order.setCreateDate(date);
			order.setModifyDate(date);
			
			
			
			if(order.getRuser()!=null)
			{
				order.getRuser().setCreateDate(date);
				order.getRuser().setModifyDate(date);
				order.getRuser().setUserId(order.getUserId());
				
				//int k=this.receive_UserDao.insertReceiveUser(order.getRuser());//插入收件人信息
				int k=this.receive_UserDao.updateRuser(order.getRuser());//更新原有的地址信息
				  if(k>0)
				  {
					  order.setRuserId(order.getRuser().getId());
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
			

			//int k=this.m_orderDao.updateMorder_cost(order);
			
			order.setPayornot(Constant.ORDER_PAY_STATE1);//设置为已经支付
			order.setPayDate(date);
			int k=this.m_orderDao.updateMorder_tran(order);
			
			if(k==0)
			{
				throw new Exception("修改运单失败");
			}
			
			for(M_OrderDetail m:order.getDetail())
			{
				m.setOrderId(order.getOrderId());
				m.setCtype(order.getType());
			}
			List<String> list =new ArrayList<String>();
			list.add(order.getOrderId());
			int kk=this.m_DetailDao.deleteByOrderIds(list);//先删除原来的
			int mm=0;
			if(kk<1)
			{
				throw new Exception("删除商品信息失败!");
			}
			else
			{
				mm=this.m_DetailDao.insertMDetail(order.getDetail());
			}
			
			
			//int mm=this.m_DetailDao.insertMDetail(order.getDetail());
			
			if(mm==order.getDetail().size())//插入商品成功
			{
				
			}
			else
			{
				throw new Exception("插入商品信息出错!");
			}
			//清空相关转运单号的谷仓
			List<String> tids=this.morder_TorderDao.gettorderIds(order.getOrderId());
			String postionstr="";//用于返回，标记已经清空仓位
			if(tids!=null&&tids.size()>0)
			{
				for(String tid:tids)
				{
					T_order torder=this.torderDao.getone(null, tid);
					if(torder!=null)
					{
						if(torder.getPosition()!=null)
						{
							if(postionstr.equalsIgnoreCase(""))
							{
								postionstr=torder.getPosition().getPosition();
							}
							else
							{
								postionstr+=";"+torder.getPosition().getPosition();
							}
							
							this.shelves_positionDao.updatestate(torder.getPosition().getId(), Constant.POSITION_STATE0, date, "");//更新仓位状态
							this.torderDao.clear_positionId(torder.getPosition().getId(), date);//清空所有仓位
						}
					}
				}
			}
			
			//取消用户冻结的资金
			if(!StringUtil.isEmpty(order.getFreezId()))
			{
				int kkk=this.freezeMoneyDao.delete(order.getFreezId());
			}
			
			
			ResponseObject<Object> obj1=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"成功扣除费用：$"+order.getTotalmoney()+",清空仓位："+postionstr);
			obj1.setData(order.getId());
			return obj1;
		}
		 catch (Exception e) {
				//throw ExceptionUtil.handle2ServiceException(e.getMessage());
			 return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION," 修改发生异常！");
			}
	}
	
	
	//处理提交生成未付款运单
		public ResponseObject<Object> modify_tran_Morder_nopay(M_order order,String emplyeeId,String storeId,String shelvesNo) throws ServiceException
		{
			Shelves_position position=null;
			if(order==null)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错!");
			}
			try {			
				User user=this.userDao.getUserById(order.getUserId());
				if(user==null)
				{
					throw new Exception("获取用户信息出错，参数错误！");
				}
				String date = DateUtil.date2String(new Date());
				try{
					int sid= Integer.parseInt(shelvesNo);
					if(sid>0)
					{
						position=this.shelves_positionDao.getone(shelvesNo);
						if(position==null)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取仓位信息失败，可能已满足！");
						}
						this.shelves_positionDao.updatestate(position.getId(), Constant.POSITION_STATE1, date, "运单号:"+order.getOrderId());//更新占用状态
					
					}
				}catch(Exception e){}
				
				order.setPayWay("0");//设置为余额支付
				order.setState(Constant.ORDER_STATE3);//状态设置为已揽收
				//开始扣除费用记录

				
				order.setCreateDate(date);
				order.setModifyDate(date);
				
				
				
				if(order.getRuser()!=null)
				{
					order.getRuser().setCreateDate(date);
					order.getRuser().setModifyDate(date);
					order.getRuser().setUserId(order.getUserId());
					
					//int k=this.receive_UserDao.insertReceiveUser(order.getRuser());//插入收件人信息
					int k=this.receive_UserDao.updateRuser(order.getRuser());//更新原有的地址信息
					  if(k>0)
					  {
						  order.setRuserId(order.getRuser().getId());
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
				

				//int k=this.m_orderDao.updateMorder_cost(order);
				
				order.setPayornot(Constant.ORDER_PAY_STATE0);//设置为未付款
				order.setPayDate("");
				
				if(position!=null)
				{
					order.setPositionId(position.getId());
					
				}
				
				
				int k=this.m_orderDao.updateMorder_tran(order);
				
				if(k==0)
				{
					throw new Exception("修改运单失败");
				}
				
				for(M_OrderDetail m:order.getDetail())
				{
					m.setOrderId(order.getOrderId());
					m.setCtype(order.getType());
				}
				List<String> list =new ArrayList<String>();
				list.add(order.getOrderId());
				int kk=this.m_DetailDao.deleteByOrderIds(list);//先删除原来的
				int mm=0;
				if(kk<1)
				{
					throw new Exception("删除商品信息失败!");
				}
				else
				{
					mm=this.m_DetailDao.insertMDetail(order.getDetail());
				}
				
				
				//int mm=this.m_DetailDao.insertMDetail(order.getDetail());
				
				if(mm==order.getDetail().size())//插入商品成功
				{
					
				}
				else
				{
					throw new Exception("插入商品信息出错!");
				}
				//清空相关转运单号的谷仓
				List<String> tids=this.morder_TorderDao.gettorderIds(order.getOrderId());
				String postionstr="";//用于返回，标记已经清空仓位
				if(tids!=null&&tids.size()>0)
				{
					for(String tid:tids)
					{
						T_order torder=this.torderDao.getone(null, tid);
						if(torder!=null)
						{
							if(torder.getPosition()!=null)
							{
								if(postionstr.equalsIgnoreCase(""))
								{
									postionstr=torder.getPosition().getPosition();
								}
								else
								{
									postionstr+=";"+torder.getPosition().getPosition();
								}
								
								this.shelves_positionDao.updatestate(torder.getPosition().getId(), Constant.POSITION_STATE0, date, "");//更新仓位状态
								this.torderDao.clear_positionId(torder.getPosition().getId(), date);//清空所有仓位
							}
						}
					}
				}
				
				//取消用户冻结的资金
				if(!StringUtil.isEmpty(order.getFreezId()))
				{
					int kkk=this.freezeMoneyDao.delete(order.getFreezId());
				}
				
				//插入路由状态
				Route route = new Route();
				route.setDate(date);
				route.setOrderId(order.getOrderId());
				
				route.setRemark("包裹已处理，等待付款！");
				
				route.setState(OrderUtil.transformerState(0,order.getState()));
				this.routeDao.insertRoute(route);
				
				
				
				ResponseObject<Object> obj1=null;
				if(position!=null)
				{
					 obj1=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"保存成功，分配仓位:"+position.getPosition());
				}
				else
				{
					obj1=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"保存成功");
				}
				
				obj1.setData(order.getId());
				return obj1;
			}
			 catch (Exception e) {
					//throw ExceptionUtil.handle2ServiceException(e.getMessage());
				 return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION," 修改发生异常！"+e.getMessage());
				}
		}
		
				
	
		
		
		
		//kai 20160628 查找未来付款运单
				public ResponseObject<PageSplit<M_order>> searchMordersnopay(String oid,String sod,String god,String flyno,String wid,String cid,String userinfo,String commudityinfo,String state,String type,String payway,String sdate,String edate, String psdate,String pedate, String cardinfo,String payornot,int pageSize, int pageNow)
				        throws ServiceException{
					try {
						///
						String cardurl=null;//身份证正面图片
						String cardother=null;//身份证反面图片
						String cardtogether=null;//身份证合成图片
						String cardid=null;//身份证号
						String uploadflag=null;//用于保存与身份证号相对应的名称
						if(StringUtil.isEmpty(userinfo))
						{
							userinfo=null;
						}
						else
						{
							userinfo = StringUtil.escapeStringOfSearchKey(userinfo);
						}
						if(StringUtil.isEmpty(commudityinfo))
						{
							commudityinfo=null;
						}
						else
						{
							commudityinfo = StringUtil.escapeStringOfSearchKey(commudityinfo);
						}
						if(StringUtil.isEmpty(oid))
						{
							oid=null;
						}
						else
						{
							oid = StringUtil.escapeStringOfSearchKey(oid);
						}
						if(StringUtil.isEmpty(sod))
						{
							sod=null;
						}
						else
						{
							sod = StringUtil.escapeStringOfSearchKey(sod);
						}
						if(StringUtil.isEmpty(god))
						{
							god=null;
						}
						else
						{
							god = StringUtil.escapeStringOfSearchKey(god);
						}
						
						if(StringUtil.isEmpty(cardinfo))
						{
							cardinfo=null;
						}
						else
						{
							if(userinfo==null)
							{
								userinfo="%";
							}
							String card[]=cardinfo.split(",");
							for(int i=0;i<card.length;i++)
							{
								if(!StringUtil.isEmpty(card[i]))
								{
									if(card[i].equals("0"))
									{
										cardid="1";//仅用于标识是否存在
									}
									else if(card[i].equals("1"))
									{
										cardurl="1";//仅用于标识是否存在
									}
									else if(card[i].equals("2"))
									{
										cardother="1";//仅用于标识是否存在
									}
									else if(card[i].equals("3"))
									{
										cardtogether="1";//仅用于标识是否存在
									}
									else if(card[i].equals("4"))
									{
										uploadflag=Constant.UPLOAD_CARD_TYPE0;
									}
										
									
									
								}
							}
						}

						int rowCount = 0;
						try {

							rowCount = this.m_orderDao.countOfSearchMOrders_nopay(oid, sod, god, flyno, wid, cid, userinfo, commudityinfo, state, type,payway, sdate, edate, psdate, pedate,cardurl,cardother,cardtogether,cardid,uploadflag,payornot);
						} catch (Exception e) {
							throw ExceptionUtil.handle2ServiceException("获取运单个数失败", e);
						}

						ResponseObject<PageSplit<M_order>> responseObj = new ResponseObject<PageSplit<M_order>>(
								ResponseCode.SUCCESS_CODE);
						if (rowCount > 0) {
							pageSize = Math.max(pageSize, 1);
							int pageCount = rowCount / pageSize
									+ (rowCount % pageSize == 0 ? 0 : 1);
							pageNow = Math.min(pageNow, pageCount);
							PageSplit<M_order> pageSplit = new PageSplit<M_order>();
							pageSplit.setPageCount(pageCount);
							pageSplit.setPageNow(pageNow);
							pageSplit.setRowCount(rowCount);
							pageSplit.setPageSize(pageSize);

							int startIndex = (pageNow - 1) * pageSize;
							try {
								List<M_order> orders = this.m_orderDao.SearchMOrders_nopay(oid, sod, god, flyno, wid, cid, userinfo, commudityinfo, state, type,payway, sdate, edate, psdate, pedate,cardurl,cardother,cardtogether,cardid,uploadflag,payornot, startIndex, pageSize);
								
								for(M_order o: orders)
								{
									if(!StringUtil.isEmpty(o.getUserId()))
									{
										o.setUser(this.userDao.getUserById(o.getUserId()));
									}
									List<String> list=this.morder_TorderDao.gettorderIds(o.getOrderId());
									if(list!=null&&list.size()>0)
									{
										List<T_order> torders=new ArrayList<T_order>();
										for(String tid:list)
										{
											T_order torder=this.torderDao.getone(null,tid);
											if(torder!=null)
											{
												torders.add(torder);
											}
										}
										o.setTorders(torders);
									}
									
									//存在失败运单仓位
									if(!StringUtil.isEmpty(o.getPositionId()))
									{
										Shelves_position positions=this.shelves_positionDao.getbyid(o.getPositionId());
										o.setSposition(positions);
									}
									
									/*if((o.getRuser()!=null))
									{
										if(!StringUtil.isEmpty(o.getRuser().getCardurl()))
										{
											o.getRuser().setCardurl(java.net.URLEncoder.encode(o.getRuser().getCardurl(), "utf-8"));
										}
										if(!StringUtil.isEmpty(o.getRuser().getCardother()))
										{
											o.getRuser().setCardother(java.net.URLEncoder.encode(o.getRuser().getCardother(), "utf-8"));
										}
										if(!StringUtil.isEmpty(o.getRuser().getCardtogether()))
										{
											o.getRuser().setCardtogether(java.net.URLEncoder.encode(o.getRuser().getCardtogether(), "utf-8"));
										}
										
									}*/
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
					} catch (ServiceException e) {
						//throw e;
						return new ResponseObject<PageSplit<M_order>>(ResponseCode.SHOW_EXCEPTION,"查找发生异常！");
					}
				}
				
	
				
				
				//kai 20160628 查找用户自付的运单
				public ResponseObject<PageSplit<M_order>> searchMorderspaybyuser(String oid,String sod,String god,String flyno,String wid,String cid,String userinfo,String commudityinfo,String state,String type,String payway,String sdate,String edate, String psdate,String pedate, String cardinfo,String payornot,String confirm_user_pay,int pageSize, int pageNow)
				        throws ServiceException{
					try {
						///
						String cardurl=null;//身份证正面图片
						String cardother=null;//身份证反面图片
						String cardtogether=null;//身份证合成图片
						String cardid=null;//身份证号
						String uploadflag=null;//用于保存与身份证号相对应的名称
						if(StringUtil.isEmpty(userinfo))
						{
							userinfo=null;
						}
						else
						{
							userinfo = StringUtil.escapeStringOfSearchKey(userinfo);
						}
						if(StringUtil.isEmpty(commudityinfo))
						{
							commudityinfo=null;
						}
						else
						{
							commudityinfo = StringUtil.escapeStringOfSearchKey(commudityinfo);
						}
						if(StringUtil.isEmpty(oid))
						{
							oid=null;
						}
						else
						{
							oid = StringUtil.escapeStringOfSearchKey(oid);
						}
						if(StringUtil.isEmpty(sod))
						{
							sod=null;
						}
						else
						{
							sod = StringUtil.escapeStringOfSearchKey(sod);
						}
						if(StringUtil.isEmpty(god))
						{
							god=null;
						}
						else
						{
							god = StringUtil.escapeStringOfSearchKey(god);
						}
						
						if(StringUtil.isEmpty(cardinfo))
						{
							cardinfo=null;
						}
						else
						{
							if(userinfo==null)
							{
								userinfo="%";
							}
							String card[]=cardinfo.split(",");
							for(int i=0;i<card.length;i++)
							{
								if(!StringUtil.isEmpty(card[i]))
								{
									if(card[i].equals("0"))
									{
										cardid="1";//仅用于标识是否存在
									}
									else if(card[i].equals("1"))
									{
										cardurl="1";//仅用于标识是否存在
									}
									else if(card[i].equals("2"))
									{
										cardother="1";//仅用于标识是否存在
									}
									else if(card[i].equals("3"))
									{
										cardtogether="1";//仅用于标识是否存在
									}
									else if(card[i].equals("4"))
									{
										uploadflag=Constant.UPLOAD_CARD_TYPE0;
									}
										
									
									
								}
							}
						}

						int rowCount = 0;
						try {

							rowCount = this.m_orderDao.countOfSearchMOrders_paybyuser(oid, sod, god, flyno, wid, cid, userinfo, commudityinfo, state, type,payway, sdate, edate, psdate, pedate,cardurl,cardother,cardtogether,cardid,uploadflag,payornot,confirm_user_pay);
									//.countOfSearchMOrders_nopay(oid, sod, god, flyno, wid, cid, userinfo, commudityinfo, state, type,payway, sdate, edate, psdate, pedate,cardurl,cardother,cardtogether,cardid,uploadflag,payornot);
						} catch (Exception e) {
							throw ExceptionUtil.handle2ServiceException("获取运单个数失败", e);
						}

						ResponseObject<PageSplit<M_order>> responseObj = new ResponseObject<PageSplit<M_order>>(
								ResponseCode.SUCCESS_CODE);
						if (rowCount > 0) {
							pageSize = Math.max(pageSize, 1);
							int pageCount = rowCount / pageSize
									+ (rowCount % pageSize == 0 ? 0 : 1);
							pageNow = Math.min(pageNow, pageCount);
							PageSplit<M_order> pageSplit = new PageSplit<M_order>();
							pageSplit.setPageCount(pageCount);
							pageSplit.setPageNow(pageNow);
							pageSplit.setRowCount(rowCount);
							pageSplit.setPageSize(pageSize);

							int startIndex = (pageNow - 1) * pageSize;
							try {
								List<M_order> orders = this.m_orderDao.SearchMOrders_paybyuser(oid, sod, god, flyno, wid, cid, userinfo, commudityinfo, state, type,payway, sdate, edate, psdate, pedate,cardurl,cardother,cardtogether,cardid,uploadflag,payornot, confirm_user_pay,startIndex, pageSize);
								
								for(M_order o: orders)
								{
									if(!StringUtil.isEmpty(o.getUserId()))
									{
										o.setUser(this.userDao.getUserById(o.getUserId()));
									}
									List<String> list=this.morder_TorderDao.gettorderIds(o.getOrderId());
									if(list!=null&&list.size()>0)
									{
										List<T_order> torders=new ArrayList<T_order>();
										for(String tid:list)
										{
											T_order torder=this.torderDao.getone(null,tid);
											if(torder!=null)
											{
												torders.add(torder);
											}
										}
										o.setTorders(torders);
									}
									
									//存在失败运单仓位
									if(!StringUtil.isEmpty(o.getPositionId()))
									{
										Shelves_position positions=this.shelves_positionDao.getbyid(o.getPositionId());
										o.setSposition(positions);
									}
									
									/*if((o.getRuser()!=null))
									{
										if(!StringUtil.isEmpty(o.getRuser().getCardurl()))
										{
											o.getRuser().setCardurl(java.net.URLEncoder.encode(o.getRuser().getCardurl(), "utf-8"));
										}
										if(!StringUtil.isEmpty(o.getRuser().getCardother()))
										{
											o.getRuser().setCardother(java.net.URLEncoder.encode(o.getRuser().getCardother(), "utf-8"));
										}
										if(!StringUtil.isEmpty(o.getRuser().getCardtogether()))
										{
											o.getRuser().setCardtogether(java.net.URLEncoder.encode(o.getRuser().getCardtogether(), "utf-8"));
										}
										
									}*/
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
					} catch (ServiceException e) {
						//throw e;
						return new ResponseObject<PageSplit<M_order>>(ResponseCode.SHOW_EXCEPTION,"查找发生异常！");
					}
				}
				
}


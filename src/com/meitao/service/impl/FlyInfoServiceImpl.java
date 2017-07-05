package com.meitao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitao.dao.CardIdManageDao;
import com.meitao.dao.FlyInfoDao;
import com.meitao.dao.MessageRecordDao;
import com.meitao.dao.MorderDao;
import com.meitao.dao.Receive_UserDao;
import com.meitao.dao.RouteDao;
import com.meitao.dao.WarehouseDao;
import com.meitao.dao.globalargsDao;
import com.meitao.service.FlyInfoService;
import com.meitao.service.MorderService;


import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.ConsigneeInfoUtil;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.common.util.OrderUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.exception.ServiceException;
import com.meitao.model.FlyInfo;
import com.meitao.model.M_order;
import com.meitao.model.MessageRecord;
import com.meitao.model.PageSplit;
import com.meitao.model.Receive_User;
import com.meitao.model.ResponseObject;
import com.meitao.model.Route;
import com.meitao.model.Warehouse;
import com.meitao.model.temp.ImportFlyInfo;
import com.meitao.model.temp.MessageTemp;


@Service("flyinfoService")
public class FlyInfoServiceImpl implements FlyInfoService {
	@Autowired
	private FlyInfoDao flyinfoDao;
	
	@Autowired
	private RouteDao routeDao;
	@Autowired
	private WarehouseDao warehouseDao;

	
	@Autowired
	private MorderDao m_orderDao;
	
	@Autowired
	private Receive_UserDao receive_UserDao;
	
	@Autowired
	private globalargsDao globalargsDao;
	
	@Autowired
	private CardIdManageDao cardIdManageDao;
	
	@Autowired
	private MessageRecordDao messageRecordDao;	
	
	@Resource(name = "m_orderService")
	private MorderService m_orderService;
	public ResponseObject<Object> addFlyInfo(FlyInfo flyinfo)
			throws ServiceException {
		if (flyinfo == null) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
					"参数无效");
		}
		try {
			if (StringUtil.isEmpty(flyinfo.getFlightno())) {
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
						"参数无效");
			}
			flyinfo.setCreateDate(DateUtil.date2String(new Date()));
			flyinfo.setModifyDate(DateUtil.date2String(new Date()));

			FlyInfo flyinfo1 = this.flyinfoDao.getByflightno(flyinfo
					.getFlightno());
			if (flyinfo1 != null) {
				return new ResponseObject<Object>(
						ResponseCode.WAREHOUSE_FLYINFO_FAILURE, "插入失败,航班号已经存在！");
			}
			int i = this.flyinfoDao.insertFlyInfo(flyinfo);

			if (i > 0) {
				return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			} else {
				return new ResponseObject<Object>(
						ResponseCode.WAREHOUSE_FLYINFO_FAILURE, "插入失败");
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}

	// kai 20151028 包含仓库信息的搜索
	public ResponseObject<PageSplit<FlyInfo>> searchFlyInfo(String flightno,
			String key, String sdate, String edate, String state, int pageSize,
			int pageNow,String storeId) throws ServiceException {
		try {
			key = StringUtil.escapeStringOfSearchKey(key);
			if (!StringUtil.isEmpty(flightno)) {
				flightno = StringUtil.escapeStringOfSearchKey(flightno);
			}

			int rowCount = 0;
			try {
				rowCount = this.flyinfoDao.countOfSearchKeys(flightno, key,
						state, sdate, edate,storeId);
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("获取运单个数失败", e);
			}

			ResponseObject<PageSplit<FlyInfo>> responseObj = new ResponseObject<PageSplit<FlyInfo>>(
					ResponseCode.SUCCESS_CODE);
			if (rowCount > 0) {
				pageSize = Math.max(pageSize, 1);
				int pageCount = rowCount / pageSize
						+ (rowCount % pageSize == 0 ? 0 : 1);
				pageNow = Math.min(pageNow, pageCount);
				PageSplit<FlyInfo> pageSplit = new PageSplit<FlyInfo>();
				pageSplit.setPageCount(pageCount);
				pageSplit.setPageNow(pageNow);
				pageSplit.setRowCount(rowCount);
				pageSplit.setPageSize(pageSize);

				int startIndex = (pageNow - 1) * pageSize;
				try {
					List<FlyInfo> flyinfos = this.flyinfoDao.searchByKeys(
							flightno, key, state, sdate, edate, startIndex,
							pageSize,storeId);
					
					if (flyinfos != null && !flyinfos.isEmpty()) {
						for (FlyInfo o : flyinfos) {
							
							//o.setOrdersno(Integer.toString(this.orderDao.countOfflights(o.getFlightno())));
							o.setOrdersno(Integer.toString(this.m_orderDao.countOfflynos(o.getFlightno())));
							if(!StringUtil.isEmpty(o.getWarehouseId()))
							{
								Warehouse house =this.warehouseDao.getById(o.getWarehouseId());
								if(house!=null)
								{
									o.setWarehouseName(house.getName());
								}
							}
							
							pageSplit.addData(o);
						}
					}
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("获取航班表失败", e);
				}
				responseObj.setData(pageSplit);
			} else {
				responseObj.setMessage("没有航班信息!");
			}
			return responseObj;
		} catch (ServiceException e) {
			throw e;
		}
	}

	

	public ResponseObject<Object> deleteFlyInfoById(String id)
			throws ServiceException {
		if (id == null || id.equalsIgnoreCase("")) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
					"参数无效");
		}

		try {
			FlyInfo flyinfo = this.flyinfoDao.getById(id);
			if (flyinfo != null) {
				if ((flyinfo.getFlightno() != null)
						&& (!flyinfo.getFlightno().equalsIgnoreCase(""))) {
					
					
					int ii=this.m_orderDao.modifyMorderflightnobyflightno(flyinfo.getFlightno(), "",
							DateUtil.date2String(new Date()));
				}

				int i = this.flyinfoDao.deleteFlyInfoById(id);
				if (i > 0) {
					return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
				} else {
					throw new Exception();
				}
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}

	

	public ResponseObject<FlyInfo> getbyid(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return new ResponseObject<FlyInfo>(ResponseCode.PARAMETER_ERROR,
					"获取信息出错，id不能为空!");
		}
		FlyInfo info=null;
		try {
			info = this.flyinfoDao.getById(id);
			if (info != null) {
				Warehouse house = this.warehouseDao.getById(info
						.getWarehouseId());
				if (house != null) {
					info.setWarehouseName(house.getName());
				}
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
		ResponseObject<FlyInfo> obj=new ResponseObject<FlyInfo>(ResponseCode.SUCCESS_CODE);
		obj.setData(info);
		return obj;
	}
	

	
	//添加m_order的航信息
	public ResponseObject<Object> addFlyForMorder(FlyInfo flyinfo)
			throws ServiceException {
		if (flyinfo == null) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
					"参数无效");
		}
		try {
			if (StringUtil.isEmpty(flyinfo.getFlightno())) {
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
						"参数无效");
			}
			flyinfo.setCreateDate(DateUtil.date2String(new Date()));
			flyinfo.setModifyDate(DateUtil.date2String(new Date()));

			FlyInfo flyinfo1 = this.flyinfoDao.getByflightno(flyinfo
					.getFlightno());
			if (flyinfo1 != null) {
				return new ResponseObject<Object>(
						ResponseCode.WAREHOUSE_FLYINFO_FAILURE, "插入失败,航班号已经存在！");
			}
			int i = this.flyinfoDao.insertFlyInfo(flyinfo);

			if (i > 0) {
				return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			} else {
				return new ResponseObject<Object>(
						ResponseCode.WAREHOUSE_FLYINFO_FAILURE, "插入失败");
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	
	
	//上传个性航班信息
	public ResponseObject<Object> modifyFlyInfoWithExcelForMorder(FlyInfo flyinfo,String changeorderflag,String empName,List<ImportFlyInfo> importflyInfo,String sendmessage,String storeId) throws ServiceException {

		if (flyinfo == null) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
					"参数无效");
		}
		try {
			if (StringUtil.isEmpty(flyinfo.getId())) {
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
						"参数无效");
			}
			flyinfo.setModifyDate(DateUtil.date2String(new Date()));
			FlyInfo info = this.flyinfoDao.getById(flyinfo.getId());
			if (info == null) {
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
						"获取航班信息失败");
			}
			flyinfo.setFlightno(info.getFlightno());
			if (StringUtil.isEmpty(flyinfo.getFlightno())) {
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
						"参数无效");
			}
			
			

			// 检查导入的运单是否存在

			int i = this.flyinfoDao.updateFlyInfo(flyinfo);// 更新当前航班信息
			if(StringUtil.isEmpty(changeorderflag)||(!changeorderflag.equalsIgnoreCase("1")))//不更新路由状态
			{
				flyinfo.setState(null);
			}
			if (i > 0) {

				int number = 0;
				for (ImportFlyInfo orid : importflyInfo) {
					number = number + 1;
					M_order order=null;
					try{
						//order= this.orderDao.getByOrderIdonlyorder(orid.getOrderId(),storeId);
						order=this.m_orderDao.getFlyArgsbyOrderId(orid.getOrderId(), storeId);
					}
					 catch (Exception e) {
						orid.setResult("失败！获取运单信息出现异常!");
					}
					if (order != null) {
						if (StringUtil.isEmpty(order.getState())
								|| (Double.parseDouble(order.getState()) < Double
										.parseDouble(Constant.ORDER_STATE1))) {
							orid.setResult("失败：原订单状态还没处理，如在线置单，空运单等!");
						} else {
							int aa=0;
							try {						
								//更新运单的航班信息		
								String date=DateUtil.date2String(new Date());
								 aa=this.m_orderDao.modifyMorderFlyInfo(orid.getOrderId(), flyinfo.getState(), date, flyinfo.getFlightno(), storeId);
								if (aa > 0) {		
									orid.setResult("成功!");
								} else {
									orid.setResult("失败：请检查单号是否存在或字符格式!");
								}
							} catch (Exception e) {
								//throw ExceptionUtil.handle2ServiceException("");
								orid.setResult("失败：更新运单信息出现异常!");
							}
							if((aa>0)&&(!StringUtil.isEmpty(changeorderflag))&&(changeorderflag.equalsIgnoreCase("1")))
							{
								try {
									Route route = new Route();
									route.setOrderId(orid.getOrderId());
									route.setName(empName);
									route.setDate(DateUtil.date2String(new Date()));
									route.setState(OrderUtil.transformerState(0,
											flyinfo.getState()));
									route.setAddress("航班状态修改!");
									int k=this.routeDao.insertRoute(route);
									if(k==0)
									{
										orid.setResult("修改成功！但更新路由失败!");
									}
								} catch (Exception e) {
									orid.setResult("修改成功！但更新路由发生失异常!");
								}
							}

						}
					} else {
						orid.setResult("失败：请检查单号是否存在或字符格式或是否属于本门店!");
					}
					/*
					 * Order order=this.orderDao.getByOrderId(orid);
					 * if(order==null)//订单不存在 { return new
					 * ResponseObject<ImportFlyInfo>(
					 * ResponseCode.PARAMETER_ERROR,
					 * "第"+number+"行的订单号为("+orid+")不存在,导入失败!"); }
					 */
				}

				
				ResponseObject<Object> obj = new ResponseObject<Object>(
						ResponseCode.SUCCESS_CODE);
				obj.setData(importflyInfo);

				return obj;

			} else {
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
						"修改失败，数据库中没有对应的数据");
			}

		} catch (Exception e) {
			return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,
					"更新航信息发生异常！");
			//throw ExceptionUtil.handle2ServiceException("更新航信息发生异常！");
		}

	}
	
	//上传个性航班信息
	public ResponseObject<Object> modifyFlyInfoByfile(FlyInfo flyinfo,String empName,List<ImportFlyInfo> importflyInfo,String storeId) throws ServiceException {

			if (flyinfo == null) {
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
						"参数无效");
			}
			try {
				if (StringUtil.isEmpty(flyinfo.getId())) {
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
							"参数无效");
				}
				flyinfo.setModifyDate(DateUtil.date2String(new Date()));
				FlyInfo info = this.flyinfoDao.getById(flyinfo.getId());
				if (info == null) {
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
							"获取航班信息失败");
				}
				flyinfo.setFlightno(info.getFlightno());
				if (StringUtil.isEmpty(flyinfo.getFlightno())) {
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
							"参数无效");
				}
				
				

				String date1=DateUtil.date2String(new Date());
				//flyinfo.setModifyDate(date1);
				info.setModifyDate(date1);//原来的信息只更新时间
				int i = this.flyinfoDao.updateFlyInfo(info);// 更新当前航班信息
				
				if (i > 0) {

					int number = 0;
					for (ImportFlyInfo orid : importflyInfo) {
						number = number + 1;
						M_order order=null;
						try{
							//order= this.orderDao.getByOrderIdonlyorder(orid.getOrderId(),storeId);
							order=this.m_orderDao.getFlyArgsbyOrderId(orid.getOrderId(), storeId);
						}
						 catch (Exception e) {
							orid.setResult("失败！获取运单信息出现异常!");
						}
						if (order != null) {
							if (StringUtil.isEmpty(order.getState())
									|| (Double.parseDouble(order.getState()) < Double
											.parseDouble(Constant.ORDER_STATE1))) {
								orid.setResult("失败：原订单状态还没处理，如在线置单，空运单等!");
							} else {
								int aa=0;
								try {						
									//更新运单的航班信息		
									String date=DateUtil.date2String(new Date());
									 aa=this.m_orderDao.modifyMorderFlyInfo(orid.getOrderId(), flyinfo.getState(), date, flyinfo.getFlightno(), storeId);
									if (aa > 0) {		
										orid.setResult("成功!");
									} else {
										orid.setResult("失败：请检查单号是否存在或字符格式!");
									}
								} catch (Exception e) {
									//throw ExceptionUtil.handle2ServiceException("");
									orid.setResult("失败：更新运单信息出现异常!");
								}
								

							}
						} else {
							orid.setResult("失败：请检查单号是否存在或字符格式或是否属于本门店!");
						}
						/*
						 * Order order=this.orderDao.getByOrderId(orid);
						 * if(order==null)//订单不存在 { return new
						 * ResponseObject<ImportFlyInfo>(
						 * ResponseCode.PARAMETER_ERROR,
						 * "第"+number+"行的订单号为("+orid+")不存在,导入失败!"); }
						 */
					}

					
					ResponseObject<Object> obj = new ResponseObject<Object>(
							ResponseCode.SUCCESS_CODE);
					obj.setData(importflyInfo);

					return obj;

				} else {
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
							"修改失败，数据库中没有对应的数据");
				}

			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("更新航信息发生异常！");
			}

		}

	public ResponseObject<Object> modifyFlyInfoMorder(FlyInfo flyinfo,String changeorderflag,String sendmessage,String sendmessage_cardId,String empName,String storeId) throws ServiceException {
		if (flyinfo == null) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
					"参数无效");
		}
		if (StringUtil.isEmpty(flyinfo.getId())) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
					"参数无效");
		}
		try {
			
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
			
			
			
			
			FlyInfo info = this.flyinfoDao.getById(flyinfo.getId());
			if (info == null) {
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
						"获取航班信息失败");
			}
			flyinfo.setFlightno(info.getFlightno());
			if (StringUtil.isEmpty(flyinfo.getFlightno())) {
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
						"参数无效");
			}
			flyinfo.setModifyDate(DateUtil.date2String(new Date()));
			int i = this.flyinfoDao.updateFlyInfo(flyinfo);
			
			if (i > 0) {
				List<MessageTemp> messagephone=new ArrayList<MessageTemp>();
				if ((changeorderflag != null)
						&& (changeorderflag.equalsIgnoreCase("1"))
						&& (flyinfo.getState() != null)
						&& (!flyinfo.getState().equalsIgnoreCase(
								Constant.ORDER_STATE10)))// 修改订单状态
				{
					List<M_order> orders=this.m_orderDao.searchMordersbyflyno(flyinfo.getFlightno());
					
					if (orders.size() > 0) {
						
						for (M_order ord : orders) {
							String date1=DateUtil.date2String(new Date());
							//int ii=this.m_orderDao.modifyroutestate(ord.getId(), flyinfo.getState(), null, null, null);
							int ii=this.m_orderDao.modifyroutestatebyorderId(date1, ord.getOrderId(), flyinfo.getState(), null, null, null);
							if(ii>0)
							{
								Route route = new Route();
								route.setOrderId(ord.getOrderId());
								route.setName(empName);
								route.setDate(DateUtil.date2String(new Date()));
								route.setState(OrderUtil.transformerState(0,
										flyinfo.getState()));
								route.setAddress("航班状态修改!");
								if(!StringUtil.isEmpty(flyinfo.getState())&&(flyinfo.getState().equalsIgnoreCase(Constant.ORDER_STATE7)))
								{
									//在空运中，加入航班信息
									route.setStateRemark("所属航班："+flyinfo.getFlightno());
								}
								int iii=this.routeDao.insertRoute(route);
								if(iii>0)//更新路由成功
								{
									if((!StringUtil.isEmpty(sendmessage)&&(sendmessage.equalsIgnoreCase("1")))||(!StringUtil.isEmpty(sendmessage_cardId)&&(sendmessage_cardId.equalsIgnoreCase("1"))))
									{
										
										if(ord.getOrderId().indexOf("MT")>=0)//只有包含MT的单号才发送短信
										{
											
										}
										else
										{
											continue;
										}
										
										Receive_User user=null;
										try{
											user=this.receive_UserDao.getById(Integer.parseInt(ord.getRuserId()));
										}catch(Exception e)
										{
											
										}
										
										if(user!=null)
										{
											
											if((!StringUtil.isEmpty(sendmessage_cardId)&&(sendmessage_cardId.equalsIgnoreCase("1"))))
											{
												int existphone=this.cardIdManageDao.countphoneandname(user.getPhone(), user.getName(),modate);
												//发送身份证通知sv
												if(existphone!=0)
												{
													continue;
												}
											}
											if(!StringUtil.isEmpty(user.getPhone()))
											{
												if((user.getPhone().length()==10)||(user.getPhone().length()==11))
												{
													boolean flag=false;
													for(MessageTemp m:messagephone)
													{
														if(user.getPhone().equalsIgnoreCase(m.getPhone()))
														{
															m.getOrderids().add(ord.getOrderId());
															m.setPhone(user.getPhone());
															m.setState(flyinfo.getState());
															flag=true;
															break;
														}
														
													}
													if(flag==false)//之前没有同一个电话号码的
													{
														MessageTemp temp=new MessageTemp();
														temp.getOrderids().add(ord.getOrderId());
														temp.setPhone(user.getPhone());
														temp.setState(flyinfo.getState());
														messagephone.add(temp);
													}
												}
											}
										}
									}
								}
								
							}
						}
					}
					

					
				
					
					
					
					if(!StringUtil.isEmpty(sendmessage)&&(sendmessage.equalsIgnoreCase("1")))
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
										//美淘已揽收
										if(!StringUtil.isEmpty(m.getState())&&Constant.ORDER_STATE9.equalsIgnoreCase(m.getState()))
										{
										
										}
										else
										{
											message="your tracking number:"+orderid+",state:"+OrderUtil.transformerState(2,m.getState());
											result=this.m_orderService.sendEnglishMessage(message,m.getPhone());
											usa_nos++;
										}
									}
									else
									{
										//美淘已揽收
										if(!StringUtil.isEmpty(m.getState())&&Constant.ORDER_STATE9.equalsIgnoreCase(m.getState()))
										{
											message="包裹"+orderid+OrderUtil.transformerState(0,m.getState())+"，请务必检查后签收";
											result=this.m_orderService.sendChinaMeitaoMessage(message,m.getPhone());
										}
										else
										{
											message="单号"+orderid+"的包裹状态是"+OrderUtil.transformerState(0,m.getState());
											result=this.m_orderService.sendChinaMessage(message,m.getPhone());
										}
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
									String message="";
									String result="";
									if(m.getPhone().length()==10)
									{
										if(!StringUtil.isEmpty(m.getState())&&Constant.ORDER_STATE9.equalsIgnoreCase(m.getState()))
										{
											
										}
										else
										{
											message="the state of your "+m.getOrderids().size()+" packages is "+OrderUtil.transformerState(2,m.getState());
											result=this.m_orderService.sendEnglishMessage(message,m.getPhone());
											usa_nos++;
										}
									}
									else
									{
										if(!StringUtil.isEmpty(m.getState())&&Constant.ORDER_STATE9.equalsIgnoreCase(m.getState()))
										{
											String orderid=m.getOrderids().iterator().next();
											message="包裹"+orderid+"等"+m.getOrderids().size()+"个"+OrderUtil.transformerState(0,m.getState())+"，请务必检查后签收";
											result=this.m_orderService.sendChinaMeitaoMessage(message,m.getPhone());
										}
										else
										{
											message="你的"+m.getOrderids().size()+"个包裹目前状态为 "+OrderUtil.transformerState(0,m.getState());
											result=this.m_orderService.sendChinaMessage(message,m.getPhone());
										}
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
						
						//保存发送结果输出到前端
						/*for (ImportMorder io1 : importOrders) {
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
						}*/
						
					}
					
				}
				//始检查是否发送身份证通知信息
				if((!StringUtil.isEmpty(sendmessage_cardId)&&(sendmessage_cardId.equalsIgnoreCase("1"))))
				{
				
					if(messagephone!=null&&messagephone.size()>0)//表示更新状态过程中已经组织好数据，不需要重新查找
					{
						
					}
					else//表示没有进行状态更新，要单独处理
					{
						List<M_order> orders=this.m_orderDao.searchMordersbyflyno(flyinfo.getFlightno());//查找此航班信息
						for (M_order ord : orders) {
							if(ord.getOrderId().indexOf("MT")>=0)//只有包含MT的单号才发送短信
							{
								
							}
							else
							{
								continue;
							}
	
							if((!StringUtil.isEmpty(sendmessage)&&(sendmessage.equalsIgnoreCase("1")))||(!StringUtil.isEmpty(sendmessage_cardId)&&(sendmessage_cardId.equalsIgnoreCase("1"))))
							{
								Receive_User user=null;
								try{
									user=this.receive_UserDao.getById(Integer.parseInt(ord.getRuserId()));
								}catch(Exception e)
								{
									
								}
								
								if(user!=null)
								{
									

									int existphone=this.cardIdManageDao.countphoneandname(user.getPhone(), user.getName(),modate);
									
									if(existphone!=0)
									{
										continue;
									}
									
									if(!StringUtil.isEmpty(user.getPhone()))
									{
										if((user.getPhone().length()==10)||(user.getPhone().length()==11))
										{
											boolean flag=false;
											for(MessageTemp m:messagephone)
											{
												if(user.getPhone().equalsIgnoreCase(m.getPhone()))
												{
													m.getOrderids().add(ord.getOrderId());
													m.setPhone(user.getPhone());
													m.setState(flyinfo.getState());
													flag=true;
													break;
												}
												
											}
											if(flag==false)//之前没有同一个电话号码的
											{
												MessageTemp temp=new MessageTemp();
												temp.getOrderids().add(ord.getOrderId());
												temp.setPhone(user.getPhone());
												temp.setState(flyinfo.getState());
												messagephone.add(temp);
											}
										}
									}
								}
							}
						}
					
					}
					
					
					
					//开始发送身份证提醒通知
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
									
									//message="单号"+orderid+"的包裹正为你输送中,因清关需要,请及时上传身份证信息";
									message="包裹"+orderid+"等共1个，因清关需要，请及时上传身份证";
									
									//亲：包裹#content#，因清关需要，请及时上传身份证，详情请看#content#。请勿回复
									//result=this.m_orderService.sendChinaMessage(message,m.getPhone());
									result=this.m_orderService.sendChinaMessageCardid(message,m.getPhone());
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
									Set<String> orderids=m.getOrderids();
									String orderid="";
									for (String str :orderids) {  
										orderid=str;  
										break;
									}  
									message="包裹"+orderid+"等共"+m.getOrderids().size()+"个，因清关需要，请及时上传身份证";
									//message="你的"+m.getOrderids().size()+"个包裹正为你输送中，因清关需要，请及时上传身份证信息";
									//result=this.m_orderService.sendChinaMessage(message,m.getPhone());
									result=this.m_orderService.sendChinaMessageCardid(message,m.getPhone());
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
				}
				
				
				
				String date=DateUtil.date2String(new Date());
				try{
					if((!StringUtil.isEmpty(sendmessage_cardId)&&(sendmessage_cardId.equalsIgnoreCase("1"))))//发送的是身份证信息
					{
						MessageRecord record =new MessageRecord();
						record.setModifyDate(date);
						record.setRemark("操作人："+empName);
						record.setCreateDate(date);
						record.setRmb_nos(String.valueOf(rmb_nos));
						record.setUsa_nos(String.valueOf(usa_nos));
						record.setType(Constant.SEND_MESSAGE_TYPE2);//
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
						record.setType(Constant.SEND_MESSAGE_TYPE1);//
						this.messageRecordDao.insert(record);
					}
				}catch(Exception e)
				{
					
				}
				return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);

			} else {
				return new ResponseObject<Object>(
						ResponseCode.WAREHOUSE_MODIFY_FAILURE,
						"修改失败，数据库中没有对应的数据");
			}

		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
}

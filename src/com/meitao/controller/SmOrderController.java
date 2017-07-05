package com.meitao.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.meitao.service.CallOrderService;
import com.meitao.common.constants.Constant;

import com.meitao.common.constants.ResponseCode;

import com.meitao.common.util.ConsigneeInfoUtil;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;

import com.meitao.common.util.StringUtil;
import com.meitao.common.util.UserUtil;
import com.meitao.dao.SmOrderDao;
import com.meitao.dao.WarehouseDao;

import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.SmOrder;
import com.meitao.model.Warehouse;

@Controller
public class SmOrderController extends BasicController {

	private static final long serialVersionUID = 1455632086294967055L;
	//private static final Logger log = Logger.getLogger(SmOrderController.class);

	@Resource(name = "callOrderService")
	private CallOrderService callOrderService;
	@Value(value = "${page_size}")
	private int defaultPageSize;

	
	
	@Autowired
	private WarehouseDao warehouseDao;
	
	@Autowired
	private SmOrderDao smOrderDao;
	
	@RequestMapping(value = "/sm_order/user_add", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> addCallOrderByUser(HttpServletRequest request, SmOrder smOrder) {
		String userId = (String) request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY);
		if(StringUtil.isEmpty(userId))
		{
			return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作！");
		}
		//检查上门收货门店是否支付的门店
		try{
			Warehouse ware=this.warehouseDao.getById(smOrder.getStoreId());
			if(ware==null)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取门店信息失败！");
			}
			
			if(StringUtil.isEmpty(ware.getCallOrderAvailable())||!ware.getCallOrderAvailable().equalsIgnoreCase("1"))
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"此门店暂不支持上门收货！");
			}
		}
		catch(Exception e)
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取门店信息失败！");
		}
		smOrder.setUserId(userId);
		
		if(StringUtil.isEmpty(smOrder.getName())){
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"联系人姓名不能为空！");
		}else if(!ConsigneeInfoUtil.validatePhone(smOrder.getPhone())){
			
			return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "联系人电话错误！");
			
		}else {
			smOrder.setState(Constant.CALL_ORDER_STATE_NOT_TREAT);//设置没处理
			//callOrder.setType("1");
			String date=DateUtil.date2String(new Date());
			smOrder.setCreateDate(date);
			smOrder.setModifyDate(date);
			try {
				int result = this.smOrderDao.insert(smOrder);
				if (result > 0) {
					return generateResponseObject(ResponseCode.SUCCESS_CODE, "添加成功！");
				} else {
					return generateResponseObject(ResponseCode.PARAMETER_ERROR, "添加失败！");
				}
			} catch (Exception e) {
				return generateResponseObject(ResponseCode.PARAMETER_ERROR, "插入数据发生异常");
			}
		}
		
		
		
		
		
	}
	

	
	
	
	//获取用户的上门收货信息
		@RequestMapping(value = "/sm_order/user_search", method = { RequestMethod.GET,RequestMethod.POST })
		@ResponseBody
		public ResponseObject<PageSplit<SmOrder>> getsmorderbyuser(HttpServletRequest request,
				@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
				@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
				@RequestParam(value = "rows", required = false, defaultValue = "10") int rows) {
			try {
				
				String userId = (String) request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY);
				
				if(StringUtil.isEmpty(userId))
				{
					return new ResponseObject<PageSplit<SmOrder>>(ResponseCode.NEED_LOGIN,"请登陆后操作！");
				}
				
				
				int pageSize=rows;
				int pageNow=pageIndex;
				
				
				
				

		
				if(StringUtil.isEmpty(keyword))
				{
					keyword=null;
				}
				else
				{
					keyword = StringUtil.escapeStringOfSearchKey(keyword);
				}
				int rowCount = 0;
				try {
					rowCount=this.smOrderDao.countByKeys(userId, "", "", keyword);
					//rowCount = this.channelDao.countByKeys(id, keyword);
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("根据数量失败", e);
				}

				if (rowCount > 0) {
					pageSize = Math.max(pageSize, 1);
					int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
					pageNow = Math.min(pageNow, pageCount);
					PageSplit<SmOrder> pageSplit = new PageSplit<SmOrder>();
					pageSplit.setPageCount(pageCount);
					pageSplit.setPageNow(pageNow);
					pageSplit.setRowCount(rowCount);
					pageSplit.setPageSize(pageSize);

					int startIndex = (pageNow - 1) * pageSize;
					try {
						//List<SmOrder> result = this.channelDao.searchByKeys(id, keyword, startIndex, pageSize);
						List<SmOrder> result = this.smOrderDao.searchByKeys(userId, "", "", keyword, startIndex, pageSize);
				
						for(SmOrder sm:result)
						{
							if(sm==null)
							{
								continue;
							}
							Warehouse ware=this.warehouseDao.getById(sm.getStoreId());
							if(ware!=null)
							{
								sm.setStoreName(ware.getName());
							}
						}
						pageSplit.setDatas(result);
					} catch (Exception e) {
						throw ExceptionUtil.handle2ServiceException("根据关键字获取纪录列表失败", e);
					}
					ResponseObject<PageSplit<SmOrder>> responseObj = new ResponseObject<PageSplit<SmOrder>>();
					responseObj.setCode(ResponseCode.SUCCESS_CODE);
					responseObj.setData(pageSplit);
					return responseObj;
				} else {
					return new ResponseObject<PageSplit<SmOrder>>(ResponseCode.SUCCESS_CODE, "");
				}
				//return responseObj;
			} catch (Exception e) {
			//	log.error("获取渠道列表失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取列表失败");
			}
		}
		
		@RequestMapping(value = "/sm_order/user_delete", method = { RequestMethod.POST })
		@ResponseBody
		public ResponseObject<Object> cancelCallOrderByUser(HttpServletRequest request, @RequestParam(value = "id", required = false, defaultValue = "") String id) {
			String userId = (String)request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY);
			try {
				if(StringUtil.isEmpty(id))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错！");
				}
				int k=this.smOrderDao.deletebyuser(id, userId);
				if(k<1)
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"删除失败！");
				}
				return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"删除成功！");
				//return this.callOrderService.deleteCallOrder(callOrder.getId(), userId);
			} catch (Exception e) {
				
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "删除出现异常");
			}
		}
		
		
		//管理员发布新闻公告，只有店主和高级管理员可以发送
				@RequestMapping(value = "/admin/sm_order/admin_search", method = { RequestMethod.GET,RequestMethod.POST })
				@ResponseBody
				public ResponseObject<PageSplit<SmOrder>> getAllsmorderbyadmin(HttpServletRequest request,
						@RequestParam(value = "storeId", required = false, defaultValue = "") String prestoreId,
						@RequestParam(value = "city_text", required = false, defaultValue = "") String city,
						@RequestParam(value = "createDateStart", required = false, defaultValue = "") String createDateStart,
						@RequestParam(value = "createDateEnd", required = false, defaultValue = "") String createDateEnd,
						@RequestParam(value = "confirmDateStart", required = false, defaultValue = "") String confirmDateStart,
						@RequestParam(value = "confirmDateEnd", required = false, defaultValue = "") String confirmDateEnd,
						@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
						@RequestParam(value = "state", required = false, defaultValue = "") String state,
						@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
						@RequestParam(value = "rows", required = false, defaultValue = "10") int rows) {
					try {
						
						String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
						String storeId=null;
						if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))//不是高级管理员
						{
						
							storeId = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);//获取所在门店id
							if(StringUtil.isEmpty(storeId))
							{
								return new ResponseObject<PageSplit<SmOrder>>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
							}
							prestoreId=storeId;
							//判断是不是店主
							/*String storeMaster=(String)request.getSession().getAttribute(Constant.EMP_STORE_MASTER_SESSION_KEY);
							if(StringUtil.isEmpty(storeMaster)||(!storeMaster.equalsIgnoreCase("1")))
							{
								return new ResponseObject<PageSplit<SmOrder>>(ResponseCode.PARAMETER_ERROR,"对不起，只有店主或高级管理员能够操作!");
							}*/
								
							
							
							
						}
						
						int pageSize=rows;
						int pageNow=pageIndex;
						
						
						if (StringUtil.isEmpty(createDateStart)
								|| !UserUtil.validateExportDate(createDateStart)) {
							createDateStart = "";
						} else {
							createDateStart = UserUtil.transformerDateString(createDateStart, " 00:00:00");
						}

						if (StringUtil.isEmpty(createDateEnd)
								|| !UserUtil.validateExportDate(createDateEnd)) {
							createDateEnd = "";
						} else {
							createDateEnd = UserUtil.transformerDateString(createDateEnd, " 23:59:59");
						}
						
						if (StringUtil.isEmpty(confirmDateStart)
								|| !UserUtil.validateExportDate(confirmDateStart)) {
							confirmDateStart = "";
						} else {
							confirmDateStart = UserUtil.transformerDateString(confirmDateStart, " 00:00:00");
						}

						if (StringUtil.isEmpty(confirmDateEnd)
								|| !UserUtil.validateExportDate(confirmDateEnd)) {
							confirmDateEnd = "";
						} else {
							confirmDateEnd = UserUtil.transformerDateString(confirmDateEnd, " 23:59:59");
						}
						

				
						if(StringUtil.isEmpty(keyword))
						{
							keyword=null;
						}
						else
						{
							keyword = StringUtil.escapeStringOfSearchKey(keyword);
						}
						
						if(StringUtil.isEmpty(city))
						{
							city=null;
						}
					
						
						if(StringUtil.isEmpty(state)||state.equalsIgnoreCase("-1"))
						{
							state=null;
						}
						if(StringUtil.isEmpty(prestoreId)||prestoreId.equalsIgnoreCase("-1"))
						{
							prestoreId=null;
						}
						
						int rowCount = 0;
						try {
							//rowCount=this.smOrderDao.countByKeys("", storeId, "", keyword);
							rowCount=this.smOrderDao.countKeysbyadmin("", prestoreId, city, createDateStart, createDateEnd, confirmDateStart, confirmDateEnd, state, keyword);
							//rowCount = this.channelDao.countByKeys(id, keyword);
						} catch (Exception e) {
							throw ExceptionUtil.handle2ServiceException("根据数量失败", e);
						}

						if (rowCount > 0) {
							pageSize = Math.max(pageSize, 1);
							int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
							pageNow = Math.min(pageNow, pageCount);
							PageSplit<SmOrder> pageSplit = new PageSplit<SmOrder>();
							pageSplit.setPageCount(pageCount);
							pageSplit.setPageNow(pageNow);
							pageSplit.setRowCount(rowCount);
							pageSplit.setPageSize(pageSize);

							int startIndex = (pageNow - 1) * pageSize;
							try {
								//List<SmOrder> result = this.channelDao.searchByKeys(id, keyword, startIndex, pageSize);
								//List<SmOrder> result = this.smOrderDao.searchByKeys("", storeId, "", keyword, startIndex, pageSize);
								List<SmOrder> result = this.smOrderDao.searchKeysByadmin("", prestoreId, city, createDateStart, createDateEnd, confirmDateStart, confirmDateEnd, state, keyword, startIndex, pageSize);
								for(SmOrder sm:result)
								{
									if(sm==null)
									{
										continue;
									}
									Warehouse ware=this.warehouseDao.getById(sm.getStoreId());
									if(ware!=null)
									{
										sm.setStoreName(ware.getName());
									}
								}
								pageSplit.setDatas(result);
							} catch (Exception e) {
								throw ExceptionUtil.handle2ServiceException("根据关键字获取纪录列表失败", e);
							}
							ResponseObject<PageSplit<SmOrder>> responseObj = new ResponseObject<PageSplit<SmOrder>>();
							responseObj.setCode(ResponseCode.SUCCESS_CODE);
							responseObj.setData(pageSplit);
							return responseObj;
						} else {
							return new ResponseObject<PageSplit<SmOrder>>(ResponseCode.SUCCESS_CODE, "");
						}
						//return responseObj;
					} catch (Exception e) {
					//	log.error("获取渠道列表失败", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取列表失败");
					}
				}
				
				
				
				
				@RequestMapping(value = "/admin/sm_order/admin_modify", method = { RequestMethod.POST })
				@ResponseBody
				public ResponseObject<Object> modifysmorder(HttpServletRequest request, SmOrder smOrder) {
					String empId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY));
					if(StringUtil.isEmpty(empId))
					{
						return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作！");
					}
					if(StringUtil.isEmpty(smOrder.getId()))
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数错误！");
					}
					String date=DateUtil.date2String(new Date());
					try{
						int k=this.smOrderDao.updateInfo(smOrder.getId(), smOrder.getState(), empId, date, smOrder.getConfirmDate(),smOrder.getRemark());
						if(k==0)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"修改失败！");
						}
						return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"修改成功！");
					}catch(Exception e)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"修改发生异常！");
					}
				}
			
				
				
				//获取打印清单数据
				@RequestMapping(value = "/admin/sm_order/print_list", method = { RequestMethod.POST,RequestMethod.GET })
				@ResponseBody
				public ResponseObject<Object> printlistsmorderOfAdmin(
						HttpServletRequest request,
						@RequestParam(value = "ids") String[] ids) {
					try {
						String empId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY));
						if(StringUtil.isEmpty(empId))
						{
							return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作！");
						}
						if((ids==null)||ids.length<1)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"必须选择要打印的清单！");
						}
						List<SmOrder> list=this.smOrderDao.selectlistbyids(Arrays.asList(ids));
						ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
						obj.setData(list);
						return obj;
						
						
					} catch (Exception e) {
						
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取清单失败！");
					}
				}
				
				
}

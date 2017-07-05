package com.meitao.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import jxl.common.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.meitao.dao.ChannelDao;
import com.meitao.dao.CommudityAdminDao;
import com.meitao.dao.CommudityPriceDao;
import com.meitao.dao.MessageControlDao;
import com.meitao.dao.MessageRecordDao;
import com.meitao.dao.WarehouseDao;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.common.util.UserUtil;
import com.meitao.model.Channel;
import com.meitao.model.Commodity_price;
import com.meitao.model.CommudityAdmin;
import com.meitao.model.MessageControl;
import com.meitao.model.MessageRecord;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.SmOrder;
import com.meitao.model.Warehouse;
import com.meitao.model.temp.showChannelInfo;
import com.sun.mail.iap.Response;

@Controller
public class MessageControlController extends BasicController {
	private static final long serialVersionUID = 5025512386549625109L;
	private static final Logger log = Logger.getLogger(MessageControlController.class);
	@Autowired
	private MessageControlDao messageControlDao;

	@Autowired
	private MessageRecordDao messageRecordDao;
	

	//保存自动发送短信接口
	@RequestMapping(value = "/admin/MessageControlDao/update_auto_2", method = { RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public ResponseObject<Object> updateauto2(HttpServletRequest request,
			@RequestParam(value = "content", required = false) String content,
			@RequestParam(value = "flag", required = false) String flag,
			@RequestParam(value = "auto", required = false) String auto//自动发送标志，1表示要发送的标志
			) {
		try {
			
			
			if(StringUtil.isEmpty(content))
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"内容参数标志不能为空");
			}
			
			if(StringUtil.isEmpty(flag))
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数标志不能为空");
			}
			if(StringUtil.isEmpty(auto))
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"自动发送参数标志不能为空");
			}
			
			
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			String storeId = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);//门店id
			String empName=(String)request.getSession().getAttribute(Constant.EMP_ACCOUNT_SESSION_KEY);//员工名称
			if(StringUtil.isEmpty(storeId))
			{
				return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作");
			}
			
			if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
			{
			
			}
			else
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"对不起，只有最高管理员可以设置此参数!");
			}
			
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			String datenow=sdf.format(new Date());
			//String date=DateUtil.date2String(new Date());
			//System.out.println(sdf.format(date)
			
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd"); 
			String date1=sdf1.format(new Date());
			content =UserUtil.transformerDateString(date1, " "+content);
			if(DateUtil.compareDate(sdf.parse(datenow), sdf.parse(content))<0)//表示记录时间大于当前时间，直接保存
			{
			}
			else//记录时间小于等于当前时间，要把天数加到下一天
			{
				
				Date d=DateUtil.getNextDate(sdf.parse(content));
				content=sdf.format(d);
			}
			
			MessageControl msg=new MessageControl();//
			msg.setAuto(auto);
			msg.setContent(content);
			msg.setFlag(flag);
			msg.setModifyDate(datenow);
			msg.setProcessman(empName);
			
			int k=this.messageControlDao.updatebyflag(msg);
			if(k==0)
			{
				msg.setCreateDate(datenow);
				int kk=this.messageControlDao.insert(msg);
				if(kk==0)
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"保存出错！");
				}
				else
				{
					return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"保存成功！");
				}
			}
			else
			{
				return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"保存成功！");
			}
			
		} catch (Exception e) {
			return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"保存发生异常！");
		}
	}
	
	
	
	//保存自动发送短信接口
		@RequestMapping(value = "/admin/MessageControlDao/getbyflag", method = { RequestMethod.GET, RequestMethod.POST})
		@ResponseBody
		public ResponseObject<Object> getbyflag(HttpServletRequest request,
				@RequestParam(value = "flag", required = false) String flag
				) {
			try{
				if(StringUtil.isEmpty(flag))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数错误，标志不能为空");
				}
				MessageControl  msg=this.messageControlDao.getonebyflag(flag);
				
				ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
				obj.setData(msg);
				return obj;
			}catch(Exception e){
				return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"发生异常");
			}
		}
		
		
		
		
		//获取短信记录信息
		@RequestMapping(value = "/admin/MessageControlDao/record", method = { RequestMethod.GET,RequestMethod.POST })
		@ResponseBody
		public ResponseObject<PageSplit<MessageRecord>> getmessagerecord(HttpServletRequest request,
				@RequestParam(value = "createDateStart", required = false, defaultValue = "") String createDateStart,
				@RequestParam(value = "createDateEnd", required = false, defaultValue = "") String createDateEnd,
				@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
				@RequestParam(value = "type", required = false, defaultValue = "") String type,
				@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
				@RequestParam(value = "rows", required = false, defaultValue = "10") int rows) {
			try {
				
				
				
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
				
			
				

		
				if(StringUtil.isEmpty(keyword))
				{
					keyword=null;
				}
				else
				{
					keyword = StringUtil.escapeStringOfSearchKey(keyword);
				}
				
			
				
				if(StringUtil.isEmpty(type)||type.equalsIgnoreCase("-1"))
				{
					type=null;
				}
				
				
				int rowCount = 0;
				try {
					//rowCount=this.smOrderDao.countByKeys("", storeId, "", keyword);
					rowCount=this.messageRecordDao.countKeysbyadmin(createDateStart, createDateEnd, type, keyword);
					//rowCount=this.smOrderDao.countKeysbyadmin("", prestoreId, city, createDateStart, createDateEnd, confirmDateStart, confirmDateEnd, state, keyword);
					//rowCount = this.channelDao.countByKeys(id, keyword);
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("根据数量失败", e);
				}

				if (rowCount > 0) {
					pageSize = Math.max(pageSize, 1);
					int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
					pageNow = Math.min(pageNow, pageCount);
					PageSplit<MessageRecord> pageSplit = new PageSplit<MessageRecord>();
					pageSplit.setPageCount(pageCount);
					pageSplit.setPageNow(pageNow);
					pageSplit.setRowCount(rowCount);
					pageSplit.setPageSize(pageSize);

					int startIndex = (pageNow - 1) * pageSize;
					try {
						//List<SmOrder> result = this.channelDao.searchByKeys(id, keyword, startIndex, pageSize);
						//List<SmOrder> result = this.smOrderDao.searchByKeys("", storeId, "", keyword, startIndex, pageSize);
						List<MessageRecord> result =this.messageRecordDao.searchKeysByadmin(createDateStart, createDateEnd, type, keyword, startIndex, pageSize);
						//List<SmOrder> result = this.smOrderDao.searchKeysByadmin("", prestoreId, city, createDateStart, createDateEnd, confirmDateStart, confirmDateEnd, state, keyword, startIndex, pageSize);
						/*for(SmOrder sm:result)
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
						}*/
						pageSplit.setDatas(result);
					} catch (Exception e) {
						throw ExceptionUtil.handle2ServiceException("根据关键字获取纪录列表失败", e);
					}
					ResponseObject<PageSplit<MessageRecord>> responseObj = new ResponseObject<PageSplit<MessageRecord>>();
					responseObj.setCode(ResponseCode.SUCCESS_CODE);
					responseObj.setData(pageSplit);
					return responseObj;
				} else {
					return new ResponseObject<PageSplit<MessageRecord>>(ResponseCode.SUCCESS_CODE, "");
				}
				//return responseObj;
			} catch (Exception e) {
			//	log.error("获取渠道列表失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取列表失败");
			}
		}
		
}

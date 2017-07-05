package com.meitao.controller;

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
import com.meitao.dao.WarehouseDao;
import com.meitao.common.constants.Constant;

import com.meitao.common.constants.ResponseCode;

import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.model.Channel;

import com.meitao.model.Commodity_price;
import com.meitao.model.CommudityAdmin;

import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.Warehouse;
import com.meitao.model.temp.showChannelInfo;

@Controller
public class ChannelController extends BasicController {
	private static final long serialVersionUID = 5270532661049625109L;
	private static final Logger log = Logger.getLogger(ChannelController.class);
	@Autowired
	private ChannelDao channelDao;
	
	@Autowired
	private WarehouseDao warehouseDao;
	
	@Autowired
	private CommudityPriceDao commudityPriceDao;//管理员添加的商品管理
	
	@Autowired
	private CommudityAdminDao commudityAdminDao;//管理员添加的商品管理

	@RequestMapping(value = "/channel/allgoodorstodp", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<List<Channel>> getAllChannel(HttpServletRequest request,
			@RequestParam(value = "wid", required = false) String wid ) {
		try {
			List<Channel> list = new ArrayList<Channel>();
			
			if(wid==null||wid==""){
				String storeId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
				list = this.channelDao.getByWarhouseId(storeId, null);
			}else{
				list = this.channelDao.getByWarhouseId(wid, null);
			}
			
			ResponseObject<List<Channel>> responseObj = new ResponseObject<List<Channel>>(ResponseCode.SUCCESS_CODE);
			if (list != null && !list.isEmpty()) {
				responseObj.setData(list);
			} else {
				responseObj.setMessage("没有渠道数据");
			}
			return responseObj;
		} catch (Exception e) {
			log.error("获取渠道列表失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取渠道列表失败");
		}
	}
	
	@RequestMapping(value = "/channel/all", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<List<Channel>> getAllgoodChannel(HttpServletRequest request,
			@RequestParam(value = "wid", required = false) String wid ) {
		try {
			List<Channel> list = new ArrayList<Channel>();
			
			if(wid==null||wid==""){
				String storeId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
				list = this.channelDao.getByWarhouseId(storeId, "1");
			}else{
				list = this.channelDao.getByWarhouseId(wid, "1");
			}
			
			ResponseObject<List<Channel>> responseObj = new ResponseObject<List<Channel>>(ResponseCode.SUCCESS_CODE);
			if (list != null && !list.isEmpty()) {
				responseObj.setData(list);
			} else {
				responseObj.setMessage("没有渠道数据");
			}
			return responseObj;
		} catch (Exception e) {
			log.error("获取渠道列表失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取渠道列表失败");
		}
	}
	
	@RequestMapping(value = "/admin/channel/all", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<List<Channel>> getAllgoodChannelbyadmin(HttpServletRequest request,
			@RequestParam(value = "wid", required = false) String wid ) {
		try {
			List<Channel> list = new ArrayList<Channel>();
			
			if(wid==null||wid==""){
				String storeId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
				list = this.channelDao.getByWarhouseId(storeId, "1");
			}else{
				list = this.channelDao.getByWarhouseId(wid, "1");
			}
			
			ResponseObject<List<Channel>> responseObj = new ResponseObject<List<Channel>>(ResponseCode.SUCCESS_CODE);
			if (list != null && !list.isEmpty()) {
				responseObj.setData(list);
			} else {
				responseObj.setMessage("没有渠道数据");
			}
			return responseObj;
		} catch (Exception e) {
			log.error("获取渠道列表失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取渠道列表失败");
		}
	}
	
	@RequestMapping(value = "/admin/channel/modify", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> modifyChannelOfAdmin(HttpServletRequest request, Channel channel) {
		
		String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
		{
			return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权修改渠道!");
			
		}
		
		if (channel == null) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数无效");
		}
		
		if (StringUtil.isEmpty(channel.getId())||StringUtil.isEmpty(channel.getWarehouseId())) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数无效,无法通过参数验证");
		}
		if (!channel.getState().equals("1")&&!channel.getState().equals("2")) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "渠道状态错误");
		}
		if (StringUtil.isEmpty(channel.getName())) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "渠道名不能为空");
		}
		if (StringUtil.isEmpty(channel.getAlias())) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "渠道别名不能为空");
		}
		if (StringUtil.isEmpty(channel.getAdditivePrice())) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "附加费不能为空");
		}
		try {
			int nRet = this.channelDao.modifyChannel(channel);
			if(nRet>0){
				return generateResponseObject(ResponseCode.SUCCESS_CODE, "");
			}else{
				return generateResponseObject(ResponseCode.CHANNEL_UPDATE_FAILURE, "修改渠道信息失败");
			}
		} catch (Exception e) {
			log.error("修改渠道信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改渠道信息失败");
		}
	}
	
	
	@RequestMapping(value = "/admin/channel/add", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> addChannel(
			HttpServletRequest request, Channel channel) {
		String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
		{
			return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权添加渠道!");
			
		}
		
		if (channel == null) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数无效");
		}
		
		if (StringUtil.isEmpty(channel.getWarehouseId())) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数无效,无法通过参数验证");
		}
		if (!channel.getState().equals("1")&&!channel.getState().equals("2")) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "渠道状态错误");
		}
		if (StringUtil.isEmpty(channel.getName())) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "渠道名不能为空");
		}
		if (StringUtil.isEmpty(channel.getAlias())) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "渠道别名不能为空");
		}
		if (StringUtil.isEmpty(channel.getAdditivePrice())) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "附加费不能为空");
		}
		if(channel.getWarehouseId().equals("-1")){
			try {
				List<Warehouse> warehouses = this.warehouseDao.getAll();
				if (warehouses != null && !warehouses.isEmpty()) {
					List<Channel> channels = new ArrayList<Channel>();
					int count = 0;
					for (Warehouse w : warehouses) {
						Channel tmpChannel = new Channel();
						tmpChannel.setName(channel.getName());
						tmpChannel.setAlias(channel.getAlias());
						tmpChannel.setState(channel.getState());
						tmpChannel.setWarehouseId(w.getId());
						
						channels.add(tmpChannel);
						count++;
					}
					
					int nRet = channelDao.insertChannelByList(channels);
					if (count == 0 || nRet != count) {
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "添加渠道失败");
					}else{
						return generateResponseObject(ResponseCode.SUCCESS_CODE, "");
					}
					
				} else{
					return generateResponseObject(ResponseCode.CHANNEL_INSTER_FAILURE, "没有仓库信息，增加失败");
				}
			} catch (Exception e) {
				log.error("获取仓库列表失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取仓库列表失败");
			}
		}
		
	
		try {
			int nRet =channelDao.insertChannel(channel);
			if(nRet>0){
				return generateResponseObject(ResponseCode.SUCCESS_CODE, "");
			}else{
				return generateResponseObject(ResponseCode.CHANNEL_INSTER_FAILURE, "添加渠道失败");
			}
		} catch (Exception e) {
			log.error("添加渠道失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "添加渠道失败");
		}
		
		
	}
	
	@RequestMapping(value = "/channel/getcannelbyid", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Channel> getChannelbyid(HttpServletRequest request,
			@RequestParam(value = "cid", required = false) String cid ) {
		try {
			
			if(StringUtil.isEmpty(cid))
			{
				return generateResponseObject(ResponseCode.PARAMETER_ERROR, "获取渠道信息失败，参数出错!");
			}
			Channel list = this.channelDao.getChannelById(cid);
			
	
			ResponseObject<Channel> responseObj = new ResponseObject<Channel>(ResponseCode.SUCCESS_CODE);
			if (list != null) {
				responseObj.setData(list);
			} else {
				responseObj.setCode(ResponseCode.SHOW_EXCEPTION);
				responseObj.setMessage("没有渠道数据");
			}
			return responseObj;
		} catch (Exception e) {
			log.error("获取渠道列表失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取渠道列表失败");
		}
	}
	
	
	
	//渠道添加
	@RequestMapping(value = "/admin/channel/add_admin", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> addChannelbyadmin(
			HttpServletRequest request, Channel channel,
			@RequestParam(value = "auth", required = false) String[] auth 
			
			) {
		String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
		{
			return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权添加渠道!");
			
		}
		
		if (channel == null) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数无效");
		}
		
	
		
		if (StringUtil.isEmpty(channel.getName())) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "渠道名不能为空");
		}
		
		if (StringUtil.isEmpty(channel.getAdditivePrice())) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "附加费不能为空");
		}
	if((auth!=null)&&(auth.length>0))
	{
		String storelist="";
		for(String au:auth)
		{
			if(StringUtil.isEmpty(au))
			{
				continue;
			}
			if(storelist.equalsIgnoreCase(""))
			{
				storelist=au;
			}
			else
			{
				storelist=storelist+";"+au;
			}
				
		}
		
		channel.setStoreList(storelist);
	}
		
	
		try {
			
			String date2 = DateUtil.date2String(new Date());
			channel.setCreateDate(date2);
			channel.setModifyDate(date2);
			
			int nRet =channelDao.insertChannel(channel);
			if(nRet>0){
				return generateResponseObject(ResponseCode.SUCCESS_CODE, "");
			}else{
				return generateResponseObject(ResponseCode.CHANNEL_INSTER_FAILURE, "添加渠道失败");
			}
		} catch (Exception e) {
			log.error("添加渠道失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "添加渠道失败");
		}
		
		
	}
	
	//获取所有渠道
	@RequestMapping(value = "/admin/channel/search", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<PageSplit<Channel>> getAllgoodChannelbyadmin(HttpServletRequest request,
			@RequestParam(value = "channelId", required = false, defaultValue = "") String id,
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
			@RequestParam(value = "rows", required = false, defaultValue = "10") int rows) {
		try {
			//List<Channel> list = new ArrayList<Channel>();
			
			//list=this.channelDao.getall();
			
			
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
			{
				
				
			}
			else
			{
				return new ResponseObject<PageSplit<Channel>>(ResponseCode.PARAMETER_ERROR,"对不起，你无权查看渠道信息!");
			}
			
			
			int pageSize=rows;
			int pageNow=pageIndex;
			
			
			
			

			if(StringUtil.isEmpty(id))
			{
				id=null;
			}
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
				rowCount = this.channelDao.countByKeys(id, keyword);
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("根据关键字获取渠道数量失败", e);
			}

			if (rowCount > 0) {
				pageSize = Math.max(pageSize, 1);
				int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
				pageNow = Math.min(pageNow, pageCount);
				PageSplit<Channel> pageSplit = new PageSplit<Channel>();
				pageSplit.setPageCount(pageCount);
				pageSplit.setPageNow(pageNow);
				pageSplit.setRowCount(rowCount);
				pageSplit.setPageSize(pageSize);

				int startIndex = (pageNow - 1) * pageSize;
				try {
					List<Channel> result = this.channelDao.searchByKeys(id, keyword, startIndex, pageSize);
					if (result != null && !result.isEmpty()) {
						for (Channel ch : result) {
							if(StringUtil.isEmpty(ch.getStoreList()))
							{
								continue;
							}
							else
							{
								String storeNamelist="";
								String storelist=ch.getStoreList();
								String[] list1=storelist.split(";");
								for(String storeId:list1)
								{
									if(StringUtil.isEmpty(storeId))
									{
										continue;
									}
									String name=this.warehouseDao.getNamebyId(storeId);
									
									if(storeNamelist.equalsIgnoreCase(""))
									{
										storeNamelist=name;
									}
									else
									{
										storeNamelist=storeNamelist+";"+name;
									}
									
								}
								ch.setStoreListName(storeNamelist);
							}
						}
					}
					
					pageSplit.setDatas(result);
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("根据关键字获取渠道纪录列表失败", e);
				}
				ResponseObject<PageSplit<Channel>> responseObj = new ResponseObject<PageSplit<Channel>>();
				responseObj.setCode(ResponseCode.SUCCESS_CODE);
				responseObj.setData(pageSplit);
				return responseObj;
			} else {
				return new ResponseObject<PageSplit<Channel>>(ResponseCode.SUCCESS_CODE, "没有获取的渠道信息");
			}
			//return responseObj;
		} catch (Exception e) {
			log.error("获取渠道列表失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取渠道列表失败");
		}
	}
	
	
	//修改运单费用
	@RequestMapping(value = "/admin/channel/modify_admin", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> modifyChannelbyAdmin(HttpServletRequest request, Channel channel,
			@RequestParam(value = "auth", required = false) String[] auth) {
		
		String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
		{
			return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权修改渠道!");
			
		}
		
		if (channel == null) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数无效");
		}
		
		if (StringUtil.isEmpty(channel.getId())) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数无效,无法通过参数验证");
		}
		
		if (StringUtil.isEmpty(channel.getName())) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "渠道名不能为空");
		}
		
		if (StringUtil.isEmpty(channel.getAdditivePrice())) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "附加费不能为空");
		}
		try {
			
			if((auth!=null)&&(auth.length>0))
			{
				String storelist="";
				for(String au:auth)
				{
					if(StringUtil.isEmpty(au))
					{
						continue;
					}
					if(storelist.equalsIgnoreCase(""))
					{
						storelist=au;
					}
					else
					{
						storelist=storelist+";"+au;
					}
						
				}
				
				channel.setStoreList(storelist);
			}
			String date2 = DateUtil.date2String(new Date());
			channel.setModifyDate(date2);
			int nRet = this.channelDao.modifyChannel(channel);
			if(nRet>0){
				return generateResponseObject(ResponseCode.SUCCESS_CODE, "");
			}else{
				return generateResponseObject(ResponseCode.CHANNEL_UPDATE_FAILURE, "修改渠道信息失败");
			}
		} catch (Exception e) {
			log.error("修改渠道信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改渠道信息失败");
		}
	}
	
	
	
	//修改运单费用
		@RequestMapping(value = "/admin/channel/delete_admin", method = { RequestMethod.POST })
		@ResponseBody
		public ResponseObject<Object> deleteChannelbyAdmin(HttpServletRequest request, 
				@RequestParam(value = "id", required = false) String id) {
			
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
			{
				return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权修改渠道!");
				
			}
			
			
			
			if (StringUtil.isEmpty(id)) {
				return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数无效,无法通过参数验证");
			}
		
			try {
				int i=this.channelDao.deletebyid(id);
				if(i<1)
				{
					return generateResponseObject(ResponseCode.PARAMETER_ERROR, "删除失败！"); 
				}
				this.commudityPriceDao.deletebychannelId(id);
				return generateResponseObject(ResponseCode.SUCCESS_CODE, "删除成功"); 
			} catch (Exception e) {
				log.error("删除渠道信息失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "删除失败");
			}
		}
		
		//修改运单费用
		@RequestMapping(value = "/admin/channel/get_all", method = { RequestMethod.POST })
		@ResponseBody
		public ResponseObject<Object> getchannels(HttpServletRequest request, 
				@RequestParam(value = "id", required = false) String id) {
			
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
			{
				return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权获取渠道信息!");
				
			}
			
			
			
			if (StringUtil.isEmpty(id)||(id.equalsIgnoreCase("-1"))) {
				id=null;
			}
		
			try {
				List<Channel> channels=this.channelDao.getall(id);
				ResponseObject<Object> obj=new ResponseObject<Object>();
				obj.setData(channels);
				return obj; 
			} catch (Exception e) {
				//log.error("删除渠道信息失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取渠道失败");
			}
		}
		
		
		//获取门店下的授权的渠道,wid表示要查找的门店id,
		@RequestMapping(value = "/admin/channel/get_by_store", method = { RequestMethod.POST })
		@ResponseBody
		public ResponseObject<Object> getchannelsbystore(HttpServletRequest request, 
				@RequestParam(value = "wid", required = false) String wid) {
			
			if(StringUtil.isEmpty(wid)||(wid.equalsIgnoreCase("-1")))
			{
				wid=null;
			}
			
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			String storeId=null;
			if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))//不是高级管理员
			{
			
				storeId = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);//获取所在门店id
				if(StringUtil.isEmpty(wid)||(!wid.equalsIgnoreCase(storeId)))
				{
					return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你不能够查询其它门店的渠道信息!");
				}
				else
				{
					//判断是不是店主
					String storeMaster=(String)request.getSession().getAttribute(Constant.EMP_STORE_MASTER_SESSION_KEY);
					if(StringUtil.isEmpty(storeMaster)||(!storeMaster.equalsIgnoreCase("1")))
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"对不起，只有店主或高级管理员能够操作!");
					}
					
				}
				
				
			}
			
			
			
		
		
			try {
				List<Channel> channels=this.channelDao.getall(null);//获取所有渠道
				List<Channel> channels1=new ArrayList<Channel>();
				if((wid==null)&&(storeId==null))//高级管理员同时查询全部
				{
					channels1=channels;
				}
				else //查找特定门店的渠道
				{
					for(Channel ch:channels)
					{
						if(ch==null)
						{
							continue;
						}
						String storelist=ch.getStoreList();
						if(StringUtil.isEmpty(storelist))
						{
							continue;
						}
						String[] list= storelist.split(";");
						for(int i=0;i<list.length;i++)
						{
							if((!StringUtil.isEmpty(list[i]))&&(list[i].equalsIgnoreCase(wid)))
							{
								channels1.add(ch);
								break;
							}
						}
						list=null;
					}
				}
				
				
				ResponseObject<Object> obj=new ResponseObject<Object>();
				obj.setData(channels1);
				return obj; 
			} catch (Exception e) {
				//log.error("删除渠道信息失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取渠道失败");
			}
		}
		
		
		
		
		//根据自己所在门店获取所有授权的渠道并且都包含有商品信息
		@RequestMapping(value = "/admin/channel/get_by_self", method = { RequestMethod.POST,RequestMethod.GET })
		@ResponseBody
		public ResponseObject<Object> getchannelsbystoreself(HttpServletRequest request) {
			
			
			String storeId=(String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);//门店id
			
			
			if(StringUtil.isEmpty(storeId))
			{
				return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请先登陆！");
			}
			
			try {
				List<Channel> channels=this.channelDao.getall(null);//获取所有渠道
				List<Channel> channels1=new ArrayList<Channel>();//记录授权的渠道
				//检查是否授权
				for(Channel ch:channels)
				{
					if(ch==null)
					{
						continue;
					}
					String storelist=ch.getStoreList();
					if(StringUtil.isEmpty(storelist))
					{
						continue;
					}
					String[] list= storelist.split(";");
					for(int i=0;i<list.length;i++)
					{
						if((!StringUtil.isEmpty(list[i]))&&(list[i].equalsIgnoreCase(storeId)))
						{
							channels1.add(ch);
							break;
						}
					}
					list=null;
				}
				
				//检查是否包含有可用商品
				List<Channel> channels2=new ArrayList<Channel>();//记录授权的渠道
				for(Channel ch:channels1)
				{
					
					List<Commodity_price> price=this.commudityPriceDao.getByChannelIdandStoreId(ch.getId(), storeId);
					if(price==null)
					{
						continue;
					}
					boolean flag=false;
					for(Commodity_price p:price)
					{
						if(StringUtil.isEmpty(p.getState())||(!p.getState().equalsIgnoreCase("1")))
						{
							continue;
						}
						
						if(p.getCommudityAdmin()==null)
						{
							continue;
						}
						if(!StringUtil.isEmpty(p.getCommudityAdmin().getState())&&(p.getCommudityAdmin().getState().equalsIgnoreCase("1")))
						{
							flag=true;
							break;
						}
					}
					
					if(flag==true)
					{
						channels2.add(ch);
					}
				}
				
				
				ResponseObject<Object> obj=new ResponseObject<Object>();
				obj.setData(channels2);
				return obj; 
			} catch (Exception e) {
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取渠道失败");
			}
		}
		
		
		
		//获取所有的渠道，不受限制
				@RequestMapping(value = "/admin/channel/getall_unlimit", method = { RequestMethod.POST })
				@ResponseBody
				public ResponseObject<Object> getallchannelswithunlimit(HttpServletRequest request) {
					
		
					

				
					try {
						List<Channel> channels=this.channelDao.getall(null);
						ResponseObject<Object> obj=new ResponseObject<Object>();
						obj.setData(channels);
						return obj; 
					} catch (Exception e) {
						//log.error("删除渠道信息失败", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取渠道失败");
					}
				}
				
				
				
				
				//获取指定门店并被授权的渠道
				@RequestMapping(value = "/admin/channel/get_by_store_available", method = { RequestMethod.POST,RequestMethod.GET })
				@ResponseBody
				public ResponseObject<Object> getchannelsbystoreavailable(HttpServletRequest request, 
						@RequestParam(value = "wid", required = false) String wid) {
					
			
					
					
					if(StringUtil.isEmpty(wid)||(wid.equalsIgnoreCase("-1")))
					{
						wid=null;
					}
					
					String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
					String storeId=null;
					if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))//不是高级管理员
					{
					
						storeId = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);//获取所在门店id
						if(StringUtil.isEmpty(wid)||(!wid.equalsIgnoreCase(storeId)))
						{
							return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你不能够查询其它门店的渠道信息!");
						}
						else
						{
							//判断是不是店主
						/*	String storeMaster=(String)request.getSession().getAttribute(Constant.EMP_STORE_MASTER_SESSION_KEY);
							if(StringUtil.isEmpty(storeMaster)||(!storeMaster.equalsIgnoreCase("1")))
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"对不起，只有店主或高级管理员能够操作!");
							}*/
							
						}
						
						
					}
					
					
					
				
				
					try {
						List<Channel> channels=this.channelDao.getall(null);//获取所有渠道
						List<Channel> channels1=new ArrayList<Channel>();
						if((wid==null)&&(storeId==null))//高级管理员同时查询全部
						{
							channels1=channels;
						}
						else //查找特定门店的渠道
						{
							for(Channel ch:channels)
							{
								if(ch==null)
								{
									continue;
								}
								String storelist=ch.getStoreList();
								if(StringUtil.isEmpty(storelist))
								{
									continue;
								}
								String[] list= storelist.split(";");
								for(int i=0;i<list.length;i++)
								{
									if((!StringUtil.isEmpty(list[i]))&&(list[i].equalsIgnoreCase(wid)))
									{
										channels1.add(ch);
										break;
									}
								}
							}
						}
						
						
						ResponseObject<Object> obj=new ResponseObject<Object>();
						obj.setData(channels1);
						return obj; 
					} catch (Exception e) {
						//log.error("删除渠道信息失败", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取渠道失败");
					}
				}
				
				
				
				
				//用户端提交的查找门店的可用渠道
				//获取指定门店并被授权的渠道
				@RequestMapping(value = "/channel/get_by_store_available", method = { RequestMethod.POST,RequestMethod.GET })
				@ResponseBody
				public ResponseObject<Object> getchannelsbystoreavailablewithuser(HttpServletRequest request, 
						@RequestParam(value = "wid", required = false) String wid) {
					
					String userId = StringUtil.obj2String(request.getSession()
							.getAttribute(Constant.USER_ID_SESSION_KEY));
					if(StringUtil.isEmpty(userId))
					{
						return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
					}
					
					
					if(StringUtil.isEmpty(wid)||(wid.equalsIgnoreCase("-1")))
					{
						wid=null;
					}
					
				
					
					
					
				
				
					try {
						List<Channel> channels=this.channelDao.getall(null);//获取所有渠道
						List<Channel> channels1=new ArrayList<Channel>();
						
						
						for(Channel ch:channels)
						{
							boolean flag=false;
							if(ch==null)
							{
								continue;
							}
							String storelist=ch.getStoreList();
							if(StringUtil.isEmpty(storelist))
							{
								continue;
							}
							String[] list= storelist.split(";");
							for(int i=0;i<list.length;i++)
							{
								if((!StringUtil.isEmpty(list[i]))&&(list[i].equalsIgnoreCase(wid)))
								{
									flag=true;
									break;
								}
							}
							
							if(flag==true)//开始检查渠道下是否有合江商品信息
							{
								List<Commodity_price> colist=this.commudityPriceDao.getByChannelIdandStoreId(ch.getId(), wid);
							
								for(Commodity_price co:colist)
								{
									if(!StringUtil.isEmpty(co.getState())&&(co.getState().equalsIgnoreCase("1")))//门店自已开启
									{
										if(co.getCommudityAdmin()!=null)
										{
											if(!StringUtil.isEmpty(co.getCommudityAdmin().getState())&&(co.getCommudityAdmin().getState().equalsIgnoreCase("1")))//商品类型开启
											{
												channels1.add(ch);
												break;
											}
										}
									}
								}
							
							}
							
						}
						
						
						
						ResponseObject<Object> obj=new ResponseObject<Object>();
						obj.setData(channels1);
						return obj; 
					} catch (Exception e) {
						//log.error("删除渠道信息失败", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取渠道失败");
					}
				}
				
				//获取渠道信息，用于显示前端首页的位置
				@RequestMapping(value = "/channel/get_show_info", method = { RequestMethod.POST,RequestMethod.GET })
				@ResponseBody
				public ResponseObject<Object> getchannelsbygest(HttpServletRequest request) {
					List<Channel> channels=null;
				
					List<showChannelInfo> showinfo=new ArrayList<showChannelInfo>();
					try{
						
						
						channels=this.channelDao.getall(null);//获取所有渠道
						
						
						for(Channel ch:channels)
						{
							showChannelInfo info=new showChannelInfo();
							List<String> strlist=new ArrayList<String>();
							info.setChannelName(ch.getName());
							info.setShow_remark(ch.getShow_remark());
							info.setShow_type(ch.getShow_type());
							List<CommudityAdmin> commuditys=this.commudityAdminDao.getByChannelIdandstate(ch.getId(), "1");
							
							for(CommudityAdmin com:commuditys)//
							{
								strlist.add(com.getName());
							}
							info.setCommudityName(strlist);
							showinfo.add(info);
						}
					}
					catch(Exception e)
					{
						return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"获取渠道显示信息异常！");
					}
					ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
					obj.setData(showinfo);
					return obj;
					
				}		
}

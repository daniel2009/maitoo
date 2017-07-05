package com.meitao.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;




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
import com.meitao.model.temp.TranChannels;

@Controller
public class CommudityAdminController extends BasicController {
	private static final long serialVersionUID = 5270532661049625109L;
	//private static final Logger log = Logger.getLogger(CommudityAdminController.class);
	@Autowired
	private ChannelDao channelDao;
	
	@Autowired
	private WarehouseDao warehouseDao;
	
	@Autowired
	private CommudityAdminDao commudityAdminDao;//管理员添加的商品管理
	
	@Autowired
	private CommudityPriceDao commudityPriceDao;//管理员添加的商品管理
	
	
	
//获取一个渠道下的所有商品
	@RequestMapping(value = "/admin/commudityAdmin/getbychannelId", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<List<CommudityAdmin>> getAllChannel(HttpServletRequest request,
			@RequestParam(value = "channelId", required = false) String channelId ) {
		try {
			
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
			{
				return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权获取高级管理员商品信息!");
				
			}
			if(StringUtil.isEmpty(channelId)){
				
				return new ResponseObject<List<CommudityAdmin>>(ResponseCode.PARAMETER_ERROR,"参数出错！");
				
				
			}
			
			
			List<CommudityAdmin> list = new ArrayList<CommudityAdmin>();
			list=this.commudityAdminDao.getByChannelId(channelId);
			
			ResponseObject<List<CommudityAdmin>> responseObj = new ResponseObject<List<CommudityAdmin>>(ResponseCode.SUCCESS_CODE);
			if (list != null && !list.isEmpty()) {
				responseObj.setData(list);
			} else {
				responseObj.setMessage("没有商品数据");
			}
			return responseObj;
		} catch (Exception e) {
			
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取商品列表失败");
		}
	}
	
	
	@RequestMapping(value = "/admin/commudityAdmin/insert", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> insertCommudityAdmin(HttpServletRequest request, CommudityAdmin commudity) {
		
		String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
		{
			return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权添加商品!");
			
		}
		
		if (commudity == null) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数无效");
		}
		
		
		if (StringUtil.isEmpty(commudity.getName())) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "商品名称不能为空！");
		}
		if(StringUtil.isEmpty(commudity.getChannelId())||(commudity.getChannelId().equalsIgnoreCase("-1")))
		{
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "必须选择渠道");
		}
		
		

		try {
			String date2 = DateUtil.date2String(new Date());
			commudity.setModifyDate(date2);
			commudity.setCreateDate(date2);
			int nRet=this.commudityAdminDao.insert(commudity);

			if(nRet>0){
				return generateResponseObject(ResponseCode.SUCCESS_CODE, "");
			}else{
				return generateResponseObject(ResponseCode.CHANNEL_UPDATE_FAILURE, "插入信息失败");
			}
		} catch (Exception e) {
			//log.error("修改渠道信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "插入商品信息失败");
		}
	}
	
	
	@RequestMapping(value = "/admin/commudityAdmin/modity", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> modifyCommudityAdmin(HttpServletRequest request, CommudityAdmin commudity) {
		
		String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
		{
			return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权添加商品!");
			
		}
		
		if (commudity == null) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数无效");
		}
		
		
		if (StringUtil.isEmpty(commudity.getName())) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "商品名称不能为空！");
		}
		if(StringUtil.isEmpty(commudity.getChannelId())||(commudity.getChannelId().equalsIgnoreCase("-1")))
		{
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "必须选择渠道");
		}
		if(StringUtil.isEmpty(commudity.getId()))
		{
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "商品id不能为空!");
		}
		
		

		try {
			String date2 = DateUtil.date2String(new Date());
			commudity.setModifyDate(date2);
			//commudity.setCreateDate(date2);
			int nRet=this.commudityAdminDao.modify(commudity);

			if(nRet>0){
				return generateResponseObject(ResponseCode.SUCCESS_CODE, "");
			}else{
				return generateResponseObject(ResponseCode.CHANNEL_UPDATE_FAILURE, "修改信息失败");
			}
		} catch (Exception e) {
			//log.error("修改渠道信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改商品信息失败");
		}
	}
	
	
	
	
	
	
	//获取所有渠道
	@RequestMapping(value = "/admin/commudityAdmin/search", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<PageSplit<CommudityAdmin>> searchcommudityAdmin(HttpServletRequest request,
			@RequestParam(value = "id", required = false, defaultValue = "") String id,
			@RequestParam(value = "select_channels", required = false, defaultValue = "") String channelId,
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
			@RequestParam(value = "rows", required = false, defaultValue = "10") int rows) {
		try {
			//List<Channel> list = new ArrayList<Channel>();
			
			//list=this.channelDao.getall();
			
			int pageSize=rows;
			int pageNow=pageIndex;
			
			
			
			

			if(StringUtil.isEmpty(id))
			{
				id=null;
			}
			if(StringUtil.isEmpty(channelId)||(channelId.equalsIgnoreCase("-1")))
			{
				channelId=null;
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
				rowCount=this.commudityAdminDao.countByKeys(id, keyword, channelId);
				//rowCount = this.channelDao.countByKeys(id, keyword);
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("根据关键字获取商品数量失败", e);
			}

			if (rowCount > 0) {
				pageSize = Math.max(pageSize, 1);
				int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
				pageNow = Math.min(pageNow, pageCount);
				PageSplit<CommudityAdmin> pageSplit = new PageSplit<CommudityAdmin>();
				pageSplit.setPageCount(pageCount);
				pageSplit.setPageNow(pageNow);
				pageSplit.setRowCount(rowCount);
				pageSplit.setPageSize(pageSize);

				int startIndex = (pageNow - 1) * pageSize;
				try {
					List<CommudityAdmin> result = this.commudityAdminDao.searchByKeys(id, keyword, channelId, startIndex, pageSize);
					
					
					pageSplit.setDatas(result);
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("根据关键字获取商品纪录列表失败", e);
				}
				ResponseObject<PageSplit<CommudityAdmin>> responseObj = new ResponseObject<PageSplit<CommudityAdmin>>();
				responseObj.setCode(ResponseCode.SUCCESS_CODE);
				responseObj.setData(pageSplit);
				return responseObj;
			} else {
				return new ResponseObject<PageSplit<CommudityAdmin>>(ResponseCode.SUCCESS_CODE, "没有获取的商品信息");
			}
			//return responseObj;
		} catch (Exception e) {
			//log.error("获取渠道列表失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取商品列表失败");
		}
	}
	
	
	
	
	//删除商品信息
		@RequestMapping(value = "/admin/commudityAdmin/delete", method = { RequestMethod.POST })
		@ResponseBody
		public ResponseObject<Object> deleteChannelbyAdmin(HttpServletRequest request, 
				@RequestParam(value = "id", required = false) String id,@RequestParam(value = "channelId", required = false, defaultValue = "") String channelId) {
			
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
			{
				return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权删除商品!");
				
			}
			
			
			
			if (StringUtil.isEmpty(id)) {
				return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数无效,无法通过参数验证");
			}
			if (StringUtil.isEmpty(channelId)) {
				return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数无效,无法通过参数验证");
			}
			try {
				int i=this.commudityAdminDao.delete(id, channelId);
				//int i=this.channelDao.deletebyid(id);
				if(i<1)
				{
					return generateResponseObject(ResponseCode.PARAMETER_ERROR, "删除失败！"); 
				}
				//删除此商品的价格信息
				int kk=this.commudityPriceDao.deletebycommudityId(id);
				
				return generateResponseObject(ResponseCode.SUCCESS_CODE, "删除成功"); 
			} catch (Exception e) {
				
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "删除失败");
			}
		}
		
		//根据门店的获取对应的商品，必须上传门店号和渠道号
		@RequestMapping(value = "/admin/commudityAdmin/searchbyChannel", method = { RequestMethod.GET })
		@ResponseBody
		public ResponseObject<PageSplit<CommudityAdmin>> searchcommuditywithChannel(HttpServletRequest request,
				@RequestParam(value = "id", required = false, defaultValue = "") String id,//商品类型id
				@RequestParam(value = "select_stores", required = false, defaultValue = "") String storeId,//门店号
				@RequestParam(value = "select_channels", required = false, defaultValue = "") String channelId,
				@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
				@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
				@RequestParam(value = "rows", required = false, defaultValue = "10") int rows) {
			try {
				//List<Channel> list = new ArrayList<Channel>();
				
				//list=this.channelDao.getall();
				
				int pageSize=rows;
				int pageNow=pageIndex;
				
				
				
				

				if(StringUtil.isEmpty(id))//商品类型的id号
				{
					id=null;
				}
				//必须同时上传渠道和门店id才能够搜索
				if(StringUtil.isEmpty(channelId)||(channelId.equalsIgnoreCase("-1"))||StringUtil.isEmpty(storeId)||(storeId.equalsIgnoreCase("-1")))
				{
					return new ResponseObject<PageSplit<CommudityAdmin>>(ResponseCode.SUCCESS_CODE,"必须包含门店和渠道信息");
				}
				//判断渠道的合法性
				Channel channels=this.channelDao.getChannelById(channelId);


				if(channels==null)
				{
					return new ResponseObject<PageSplit<CommudityAdmin>>(ResponseCode.PARAMETER_ERROR,"查找渠道失败");
				}
				String storelist=channels.getStoreList();
				if(StringUtil.isEmpty(storelist))
				{
					return new ResponseObject<PageSplit<CommudityAdmin>>(ResponseCode.PARAMETER_ERROR,"该渠道没有找到授权的门店!");
				}
				boolean flag=false;
				String[] list= storelist.split(";");
				for(int i=0;i<list.length;i++)
				{
					if((!StringUtil.isEmpty(list[i]))&&(list[i].equalsIgnoreCase(storeId)))
					{
						flag=true;
						break;
					}
				}
				list=null;
				if(flag!=true)//没有找到授权的门店
				{
					return new ResponseObject<PageSplit<CommudityAdmin>>(ResponseCode.PARAMETER_ERROR,"此门店没有在此渠道中找到授权信息!");
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
					rowCount=this.commudityAdminDao.countByKeys(id, keyword, channelId);
					//rowCount = this.channelDao.countByKeys(id, keyword);
				} catch (Exception e) {
					return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "根据关键字获取商品数量失败");
					//throw ExceptionUtil.handle2ServiceException("根据关键字获取商品数量失败", e);
				}

				if (rowCount > 0) {
					pageSize = Math.max(pageSize, 1);
					int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
					pageNow = Math.min(pageNow, pageCount);
					PageSplit<CommudityAdmin> pageSplit = new PageSplit<CommudityAdmin>();
					pageSplit.setPageCount(pageCount);
					pageSplit.setPageNow(pageNow);
					pageSplit.setRowCount(rowCount);
					pageSplit.setPageSize(pageSize);

					int startIndex = (pageNow - 1) * pageSize;
					try {
						List<CommudityAdmin> result = this.commudityAdminDao.searchByKeys(id, keyword, channelId, startIndex, pageSize);
						
						for(CommudityAdmin co:result)//获取门店价格信息
						{
							Commodity_price price=this.commudityPriceDao.getByInfo(co.getId(), channelId, storeId, null);
							co.setPrice(price);
						}
						pageSplit.setDatas(result);
					} catch (Exception e) {
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取商品列表失败");
					}
					ResponseObject<PageSplit<CommudityAdmin>> responseObj = new ResponseObject<PageSplit<CommudityAdmin>>();
					responseObj.setCode(ResponseCode.SUCCESS_CODE);
					responseObj.setData(pageSplit);
					return responseObj;
				} else {
					return new ResponseObject<PageSplit<CommudityAdmin>>(ResponseCode.SUCCESS_CODE, "没有获取的商品信息");
				}
				//return responseObj;
			} catch (Exception e) {
				//log.error("获取渠道列表失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取商品列表失败");
			}
		}
		
		
		//门店的价格修改
		@RequestMapping(value = "/admin/commudityAdmin/modity_store_price", method = { RequestMethod.POST })
		@ResponseBody
		public ResponseObject<Object> modifyCommuditypricebystore(HttpServletRequest request, CommudityAdmin commudity) {
			if (commudity == null) {
				return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数无效");
			}
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
			{
				String storeMaster=(String)request.getSession().getAttribute(Constant.EMP_STORE_MASTER_SESSION_KEY);//1表示是店长
				String storeId=(String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);//1表示是店长
				if(!StringUtil.isEmpty(storeMaster)&&(storeMaster.equalsIgnoreCase("1")))//店长
				{
					if(!(StringUtil.isEmpty(storeId))&&(storeId.equalsIgnoreCase(commudity.getPrice().getStoreId())))//修改的必须是本门店的，不能是其它门店的信息
					{
						
					}
					else
					{
						return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你不能够修改其它门店的信息!");
					}
				}
				else
				{
					return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权修改商品!");
				}
				
			}
			
			if(StringUtil.isEmpty(commudity.getPrice().getStoreId())||(commudity.getPrice().getStoreId().equalsIgnoreCase("-1")))
			{
				return generateResponseObject(ResponseCode.PARAMETER_ERROR, "必须选择门店");
			}
			
			
			
			if(StringUtil.isEmpty(commudity.getChannelId())||(commudity.getChannelId().equalsIgnoreCase("-1")))
			{
				return generateResponseObject(ResponseCode.PARAMETER_ERROR, "必须选择渠道");
			}
			if(StringUtil.isEmpty(commudity.getId()))
			{
				return generateResponseObject(ResponseCode.PARAMETER_ERROR, "商品id不能为空!");
			}
			
			else
			{
				//检查此渠道是否被授权
				try{
					Channel channel=this.channelDao.getChannelById(commudity.getChannelId());
					if(channel==null)
					{
						return generateResponseObject(ResponseCode.PARAMETER_ERROR, "获取渠道信息失败!");
					}
					else
					{
						String storelist=channel.getStoreList();
						if(StringUtil.isEmpty(storelist))
						{
							return generateResponseObject(ResponseCode.PARAMETER_ERROR, "该渠道没有找到授权的门店!");
						}
						boolean flag=false;
						String[] list= storelist.split(";");
						for(int i=0;i<list.length;i++)
						{
							if((!StringUtil.isEmpty(list[i]))&&(list[i].equalsIgnoreCase(commudity.getPrice().getStoreId())))
							{
								flag=true;
								break;
							}
						}
						if(flag!=true)//没有找到授权的门店
						{
							return generateResponseObject(ResponseCode.PARAMETER_ERROR, "此门店没有在此渠道中找到授权信息!");
						}
					
					}
					
					
					//判断渠道是否存在
					if (StringUtil.isEmpty(commudity.getId())) {
						return generateResponseObject(ResponseCode.PARAMETER_ERROR, "商品id号不能为空！");
					}
					else
					{
						CommudityAdmin commudity1=this.commudityAdminDao.getById(commudity.getId());
						if(commudity1==null)
						{
							return generateResponseObject(ResponseCode.PARAMETER_ERROR, "获取商品信息失败！");
						}
					}
					
					//判断门店是否存在
					if (StringUtil.isEmpty(commudity.getPrice().getStoreId())) {
						return generateResponseObject(ResponseCode.PARAMETER_ERROR, "商品id号不能为空！");
					}
					else
					{
						Warehouse ware=this.warehouseDao.getById(commudity.getPrice().getStoreId());
						if(ware==null)
						{
							return generateResponseObject(ResponseCode.PARAMETER_ERROR, "获取仓库信息失败！");
						}
					}
				}catch(Exception e)
				{
					return generateResponseObject(ResponseCode.PARAMETER_ERROR, "获取渠道或商品信息发生异常!");
				}
				
			}
			
			
			
			

			try {
				String date2 = DateUtil.date2String(new Date());
				commudity.setModifyDate(date2);
				//commudity.setCreateDate(date2);
				
				
			
					commudity.getPrice().setChannelId(commudity.getChannelId());
					commudity.getPrice().setCommudityId(commudity.getId());
					int nRet=this.commudityPriceDao.insert(commudity.getPrice());
			
			

				if(nRet>0){
					return generateResponseObject(ResponseCode.SUCCESS_CODE, "");
				}else{
					return generateResponseObject(ResponseCode.CHANNEL_UPDATE_FAILURE, "修改信息失败");
				}
			} catch (Exception e) {
				//log.error("修改渠道信息失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改商品信息失败");
			}
		}
		//根据门店的获取对应的商品，必须上传门店号和渠道号
		@RequestMapping(value = "/admin/commudityAdmin/searchbyinfo", method = { RequestMethod.GET })
		@ResponseBody
		public ResponseObject<PageSplit<CommudityAdmin>> searchcommuditywithInfo(HttpServletRequest request,
				@RequestParam(value = "id", required = false, defaultValue = "") String id,//商品类型id
				@RequestParam(value = "select_stores", required = false, defaultValue = "") String storeId,//门店号
				@RequestParam(value = "select_channels", required = false, defaultValue = "") String channelId,
				@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
				@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
				@RequestParam(value = "rows", required = false, defaultValue = "10") int rows) {
			try {
				//List<Channel> list = new ArrayList<Channel>();
				
				//list=this.channelDao.getall();
				
				int pageSize=rows;
				int pageNow=pageIndex;			
				if(StringUtil.isEmpty(id))//商品类型的id号
				{
					id=null;
				}
				//必须同时上传渠道和门店id才能够搜索
				if(StringUtil.isEmpty(channelId)||(channelId.equalsIgnoreCase("-1"))||StringUtil.isEmpty(storeId)||(storeId.equalsIgnoreCase("-1")))
				{
					return new ResponseObject<PageSplit<CommudityAdmin>>(ResponseCode.SUCCESS_CODE,"必须包含门店和渠道信息");
				}
				//判断渠道的合法性
				Channel channels=this.channelDao.getChannelById(channelId);


				if(channels==null)
				{
					return new ResponseObject<PageSplit<CommudityAdmin>>(ResponseCode.PARAMETER_ERROR,"查找渠道失败");
				}
				//用于设置商品的成本，即使该门店没有授权仍然可以设置
				
				/*String storelist=channels.getStoreList();
				if(StringUtil.isEmpty(storelist))
				{
					return new ResponseObject<PageSplit<CommudityAdmin>>(ResponseCode.PARAMETER_ERROR,"该渠道没有找到授权的门店!");
				}
				boolean flag=false;
				String[] list= storelist.split(";");
				for(int i=0;i<list.length;i++)
				{
					if((!StringUtil.isEmpty(list[i]))&&(list[i].equalsIgnoreCase(storeId)))
					{
						flag=true;
						break;
					}
				}
				if(flag!=true)//没有找到授权的门店
				{
					return new ResponseObject<PageSplit<CommudityAdmin>>(ResponseCode.PARAMETER_ERROR,"此门店没有在此渠道中找到授权信息!");
				}*/
			
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
					rowCount=this.commudityAdminDao.countByKeys(id, keyword, channelId);
					//rowCount = this.channelDao.countByKeys(id, keyword);
				} catch (Exception e) {
					return new ResponseObject<PageSplit<CommudityAdmin>>(ResponseCode.SUCCESS_CODE, "根据关键字获取商品数量失败");
					//throw ExceptionUtil.handle2ServiceException("根据关键字获取商品数量失败", e);
				}

				if (rowCount > 0) {
					pageSize = Math.max(pageSize, 1);
					int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
					pageNow = Math.min(pageNow, pageCount);
					PageSplit<CommudityAdmin> pageSplit = new PageSplit<CommudityAdmin>();
					pageSplit.setPageCount(pageCount);
					pageSplit.setPageNow(pageNow);
					pageSplit.setRowCount(rowCount);
					pageSplit.setPageSize(pageSize);

					int startIndex = (pageNow - 1) * pageSize;
					try {
						List<CommudityAdmin> result = this.commudityAdminDao.searchByKeys(id, keyword, channelId, startIndex, pageSize);
						
						for(CommudityAdmin co:result)//获取门店价格信息
						{
							Commodity_price price=this.commudityPriceDao.getByInfo(co.getId(), channelId, storeId, null);
							co.setPrice(price);
						}
						pageSplit.setDatas(result);
					} catch (Exception e) {
						return new ResponseObject<PageSplit<CommudityAdmin>>(ResponseCode.SUCCESS_CODE, "根据关键字获取商品纪录列表失败");
						//throw ExceptionUtil.handle2ServiceException("根据关键字获取商品纪录列表失败", e);
					}
					ResponseObject<PageSplit<CommudityAdmin>> responseObj = new ResponseObject<PageSplit<CommudityAdmin>>();
					responseObj.setCode(ResponseCode.SUCCESS_CODE);
					responseObj.setData(pageSplit);
					return responseObj;
				} else {
					return new ResponseObject<PageSplit<CommudityAdmin>>(ResponseCode.SUCCESS_CODE, "没有获取的商品信息");
				}
				//return responseObj;
			} catch (Exception e) {
				//log.error("获取渠道列表失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取商品列表失败");
			}
		}
		
		
		
		//修改成本费用
		//门店的价格修改
				@RequestMapping(value = "/admin/commudityAdmin/modity_store_cost", method = { RequestMethod.POST })
				@ResponseBody
				public ResponseObject<Object> modifyCommuditypricebystore(HttpServletRequest request, 
						@RequestParam(value = "id", required = false, defaultValue = "") String id,
						@RequestParam(value = "channelId", required = false, defaultValue = "") String channelId,
						@RequestParam(value = "storeId", required = false, defaultValue = "") String storeId,
						@RequestParam(value = "cost", required = false, defaultValue = "") String cost,
						@RequestParam(value = "state", required = false, defaultValue = "") String state) {
					if (StringUtil.isEmpty(id)) {
						return generateResponseObject(ResponseCode.PARAMETER_ERROR, "商品id参数无效");
					}
					if (StringUtil.isEmpty(channelId)||channelId.equalsIgnoreCase("-1")) {
						return generateResponseObject(ResponseCode.PARAMETER_ERROR, "渠道id参数无效");
					}
					if (StringUtil.isEmpty(storeId)||(storeId.equalsIgnoreCase("-1"))) {
						return generateResponseObject(ResponseCode.PARAMETER_ERROR, "门店id参数无效");
					}
					if (StringUtil.isEmpty(state)) {
						return generateResponseObject(ResponseCode.PARAMETER_ERROR, "状态参数无效");
					}
					else
					{
						if(state.equalsIgnoreCase("0")||state.equalsIgnoreCase("1"))
						{}
						else
						{
							return generateResponseObject(ResponseCode.PARAMETER_ERROR, "状态参数无效");
						}
					}
					if (StringUtil.isEmpty(cost)) {
						return generateResponseObject(ResponseCode.PARAMETER_ERROR, "成本参数无效");
					}
					else
					{
						double cost1=Double.parseDouble(cost);
						if(cost1<=0)
						{
							return generateResponseObject(ResponseCode.PARAMETER_ERROR, "成本费用只能大于0");
						}
					}
					String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
					if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
					{
						return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权修改商品成本费用!");
						
					}
					
					
					
					

					try {
						String date2 = DateUtil.date2String(new Date());
						
						CommudityAdmin admin=this.commudityAdminDao.getById(id);
						if(admin==null)
						{
							throw new Exception("没有找到商品类型!");
						}
						else
						{
							if(!channelId.equalsIgnoreCase(admin.getChannelId()))
							{
								throw new Exception("商品渠道信息对应出错!");
							}
							admin.setState(state);
							admin.setModifyDate(date2);
							int k=this.commudityAdminDao.modify(admin);
							if(k<1)
							{
								throw new Exception("更新商品类型状态失败!");
							}
						}
						
						
						Commodity_price preprice=this.commudityPriceDao.getByInfo(id, channelId, storeId, null);
						if(preprice==null)//原来没有的价格要添加
						{
							Commodity_price price=new Commodity_price();
							price.setCost(cost);
							price.setCommudityId(id);
							price.setStoreId(storeId);
							price.setChannelId(channelId);
							price.setModifyDate(date2);
							price.setCreateDate(date2);
							price.setState("0");
							int k=this.commudityPriceDao.insert(price);
							if(k<1)
							{
								throw new Exception("插入商品成本出现异常！");
							}
						}
						else
						{
							preprice.setCost(cost);
							preprice.setModifyDate(date2);
							int k=this.commudityPriceDao.modifyCost(preprice);
							if(k<1)
							{
								throw new Exception("修改商品成本出现异常！");
							}
						}
						
						
						
						
						
						
					
					
					
						return generateResponseObject(ResponseCode.SUCCESS_CODE, "");
					} catch (Exception e) {
						//log.error("修改渠道信息失败", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改商品信息失败");
					}
				}
				
				
				
				
				//修改价格费用
				//门店的价格修改
						@RequestMapping(value = "/admin/commudityAdmin/modity_store_price_admin", method = { RequestMethod.POST })
						@ResponseBody
						public ResponseObject<Object> modifyCommuditypricebystoreadmin(HttpServletRequest request, 
								Commodity_price price) {
							if (price==null) {
								return generateResponseObject(ResponseCode.PARAMETER_ERROR, "商品参数出错");
							}
							if (StringUtil.isEmpty(price.getId())) {
								return generateResponseObject(ResponseCode.PARAMETER_ERROR, "提交的id号不能出错");
							}
							
						
							if (StringUtil.isEmpty(price.getPrice())) {
								return generateResponseObject(ResponseCode.PARAMETER_ERROR, "普通会员价格参数无效");
							}
							else
							{
								double cost1=Double.parseDouble(price.getPrice());
								if(cost1<=0)
								{
									return generateResponseObject(ResponseCode.PARAMETER_ERROR, "普通会员价格参数只能大于0");
								}
							}
							if (StringUtil.isEmpty(price.getVip_0_Price())) {
								return generateResponseObject(ResponseCode.PARAMETER_ERROR, "银牌会员价格参数无效");
							}
							else
							{
								double cost1=Double.parseDouble(price.getVip_0_Price());
								if(cost1<=0)
								{
									return generateResponseObject(ResponseCode.PARAMETER_ERROR, "银牌会员价格参数只能大于0");
								}
							}
							
							if (StringUtil.isEmpty(price.getVip_1_Price())) {
								return generateResponseObject(ResponseCode.PARAMETER_ERROR, "黄金会员价格参数无效");
							}
							else
							{
								double cost1=Double.parseDouble(price.getVip_1_Price());
								if(cost1<=0)
								{
									return generateResponseObject(ResponseCode.PARAMETER_ERROR, "黄金会员价格参数只能大于0");
								}
							}
							if (StringUtil.isEmpty(price.getVip_2_Price())) {
								return generateResponseObject(ResponseCode.PARAMETER_ERROR, "黄金会员价格参数无效");
							}
							else
							{
								double cost1=Double.parseDouble(price.getVip_2_Price());
								if(cost1<=0)
								{
									return generateResponseObject(ResponseCode.PARAMETER_ERROR, "白金会员价格参数只能大于0");
								}
							}
							if (StringUtil.isEmpty(price.getVip_3_Price())) {
								return generateResponseObject(ResponseCode.PARAMETER_ERROR, "钻石会员价格参数无效");
							}
							else
							{
								double cost1=Double.parseDouble(price.getVip_3_Price());
								if(cost1<=0)
								{
									return generateResponseObject(ResponseCode.PARAMETER_ERROR, "钻石会员价格参数只能大于0");
								}
							}
							
							if (StringUtil.isEmpty(price.getVip_4_Price())) {
								return generateResponseObject(ResponseCode.PARAMETER_ERROR, "至尊会员价格参数无效");
							}
							else
							{
								double cost1=Double.parseDouble(price.getVip_4_Price());
								if(cost1<=0)
								{
									return generateResponseObject(ResponseCode.PARAMETER_ERROR, "至尊会员价格参数只能大于0");
								}
							}
							if (StringUtil.isEmpty(price.getVip_5_Price())) {
								return generateResponseObject(ResponseCode.PARAMETER_ERROR, "至尊会员2价格参数无效");
							}
							else
							{
								double cost1=Double.parseDouble(price.getVip_5_Price());
								if(cost1<=0)
								{
									return generateResponseObject(ResponseCode.PARAMETER_ERROR, "至尊会员2价格参数只能大于0");
								}
							}
							if (StringUtil.isEmpty(price.getVip_6_Price())) {
								return generateResponseObject(ResponseCode.PARAMETER_ERROR, "至尊会员3价格参数无效");
							}
							else
							{
								double cost1=Double.parseDouble(price.getVip_6_Price());
								if(cost1<=0)
								{
									return generateResponseObject(ResponseCode.PARAMETER_ERROR, "至尊会员3价格参数只能大于0");
								}
							}
							if (StringUtil.isEmpty(price.getVip_7_Price())) {
								return generateResponseObject(ResponseCode.PARAMETER_ERROR, "至尊会员4价格参数无效");
							}
							else
							{
								double cost1=Double.parseDouble(price.getVip_7_Price());
								if(cost1<=0)
								{
									return generateResponseObject(ResponseCode.PARAMETER_ERROR, "至尊会员价格参数只能大于0");
								}
							}
							
							if (StringUtil.isEmpty(price.getM_price())) {
								return generateResponseObject(ResponseCode.PARAMETER_ERROR, "门店价格参数无效");
							}
							else
							{
								double cost1=Double.parseDouble(price.getM_price());
								if(cost1<=0)
								{
									return generateResponseObject(ResponseCode.PARAMETER_ERROR, "门店价格参数只能大于0");
								}
							}
							if (StringUtil.isEmpty(price.getS_price())) {
								return generateResponseObject(ResponseCode.PARAMETER_ERROR, "上门收货价格参数无效");
							}
							else
							{
								double cost1=Double.parseDouble(price.getS_price());
								if(cost1<=0)
								{
									return generateResponseObject(ResponseCode.PARAMETER_ERROR, "上门收货价格参数只能大于0");
								}
							}
							
							
							
							
							

							try {
								String date2 = DateUtil.date2String(new Date());
								
								Commodity_price preprice=this.commudityPriceDao.getById(price.getId());
								
								
								String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
								if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))//不是高级管理员
								{
									String storeMaster = (String)request.getSession().getAttribute(Constant.EMP_STORE_MASTER_SESSION_KEY);
									if(!StringUtil.isEmpty(storeMaster)&&(storeMaster.equalsIgnoreCase("1")))//只有店主才能够修改商品价格
									{
										String storeId = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);//门店id
										if(!storeId.equalsIgnoreCase(preprice.getStoreId()))
										{
											return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权修改其它门店的商品价格!");
										}
									}
									else
									{
										return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权修改商品成本费用!");
									}
									
									
								}
								
								
								
								String storelist=preprice.getChannel().getStoreList();//先判断是否授权此门店使用此渠道
								if(StringUtil.isEmpty(storelist))
								{
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"此渠道在门店中并没有得到授权");
								}
								boolean flag=false;
								String[] list= storelist.split(";");
								for(int i=0;i<list.length;i++)
								{
									if((!StringUtil.isEmpty(list[i]))&&(list[i].equalsIgnoreCase(price.getStoreId())))
									{
										flag=true;
										break;
									}
								}
								list=null;
								if(flag!=true)//没有找到授权的门店
								{
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"此渠道在门店中并没有得到授权");
								}
								price.setModifyDate(date2);//设置修改价格
								
								preprice.setPrice(price.getPrice());
								preprice.setVip_0_Price(price.getVip_0_Price());
								preprice.setVip_1_Price(price.getVip_1_Price());
								preprice.setVip_2_Price(price.getVip_2_Price());
								preprice.setVip_3_Price(price.getVip_3_Price());
								preprice.setVip_4_Price(price.getVip_4_Price());
								preprice.setVip_5_Price(price.getVip_5_Price());
								preprice.setVip_6_Price(price.getVip_6_Price());
								preprice.setVip_7_Price(price.getVip_7_Price());
								preprice.setState(price.getState());
								preprice.setModifyDate(date2);
								preprice.setM_price(price.getM_price());
								preprice.setS_price(price.getS_price());

								
								int k=this.commudityPriceDao.modifyPricebyid(preprice);
								if(k<1)
								{
									throw new Exception("修改价格失败");
								}

								return generateResponseObject(ResponseCode.SUCCESS_CODE, "");
							} catch (Exception e) {
								//log.error("修改渠道信息失败", e);
								return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改商品信息失败");
							}
						}
						
						
									
				
				//获取后台修改价格的参数
				@RequestMapping(value = "/admin/commudityPrice/searchbyAdmin", method = { RequestMethod.GET })
				@ResponseBody
				public ResponseObject<PageSplit<Commodity_price>> searchcommuditypricebyadmin(HttpServletRequest request,
						@RequestParam(value = "id", required = false, defaultValue = "") String id,//商品类型id
						@RequestParam(value = "select_stores", required = false, defaultValue = "") String storeId,//门店号
						@RequestParam(value = "commudity_state", required = false, defaultValue = "") String commudity_state,//商品的状态
						@RequestParam(value = "store_state", required = false, defaultValue = "") String store_state,//门店自己的状态开关
						@RequestParam(value = "select_channels", required = false, defaultValue = "") String channelId,
						@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
						@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
						@RequestParam(value = "rows", required = false, defaultValue = "10") int rows) {
					try {
						//List<Channel> list = new ArrayList<Channel>();
						
						//list=this.channelDao.getall();
						
						int pageSize=rows;
						int pageNow=pageIndex;
						
						
						
						if(StringUtil.isEmpty(commudity_state)||(commudity_state.equalsIgnoreCase("-1")))
						{
							commudity_state=null;
						}
						if(StringUtil.isEmpty(store_state)||(store_state.equalsIgnoreCase("-1")))
						{
							store_state=null;
						}
					

						if(StringUtil.isEmpty(id))//商品类型的id号
						{
							id=null;
						}
						//必须同时上传渠道和门店id才能够搜索
						if(StringUtil.isEmpty(channelId)||(channelId.equalsIgnoreCase("-1"))||StringUtil.isEmpty(storeId)||(storeId.equalsIgnoreCase("-1")))
						{
							return new ResponseObject<PageSplit<Commodity_price>>(ResponseCode.SUCCESS_CODE,"必须包含门店和渠道信息");
						}
						//判断渠道的合法性
						Channel channels=this.channelDao.getChannelById(channelId);


						if(channels==null)
						{
							return new ResponseObject<PageSplit<Commodity_price>>(ResponseCode.PARAMETER_ERROR,"查找渠道失败");
						}
						String storelist=channels.getStoreList();
						if(StringUtil.isEmpty(storelist))
						{
							return new ResponseObject<PageSplit<Commodity_price>>(ResponseCode.PARAMETER_ERROR,"该渠道没有找到授权的门店!");
						}
						boolean flag=false;
						String[] list= storelist.split(";");
						for(int i=0;i<list.length;i++)
						{
							if((!StringUtil.isEmpty(list[i]))&&(list[i].equalsIgnoreCase(storeId)))
							{
								flag=true;
								break;
							}
						}
						list=null;
						if(flag!=true)//没有找到授权的门店
						{
							return new ResponseObject<PageSplit<Commodity_price>>(ResponseCode.PARAMETER_ERROR,"此门店没有在此渠道中找到授权信息!");
						}
					
						if(StringUtil.isEmpty(keyword))
						{
							keyword=null;
						}
						else
						{
							keyword = StringUtil.escapeStringOfSearchKey(keyword);
							//keyword="\'"+keyword+"\'";
						}
						int rowCount = 0;
						try {
							rowCount=this.commudityPriceDao.countBysearchPrice(id, storeId, keyword, channelId,store_state,commudity_state);
							
						} catch (Exception e) {
							return new ResponseObject<PageSplit<Commodity_price>>(ResponseCode.SUCCESS_CODE, "根据关键字获取商品数量失败");
							//throw ExceptionUtil.handle2ServiceException("根据关键字获取商品数量失败", e);
						}

						if (rowCount > 0) {
							pageSize = Math.max(pageSize, 1);
							int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
							pageNow = Math.min(pageNow, pageCount);
							PageSplit<Commodity_price> pageSplit = new PageSplit<Commodity_price>();
							pageSplit.setPageCount(pageCount);
							pageSplit.setPageNow(pageNow);
							pageSplit.setRowCount(rowCount);
							pageSplit.setPageSize(pageSize);

							int startIndex = (pageNow - 1) * pageSize;
							try {
								
								List<Commodity_price> result=this.commudityPriceDao.searchPrice(id, storeId, keyword, channelId,store_state,commudity_state, startIndex, pageSize);
								
								
								pageSplit.setDatas(result);
							} catch (Exception e) {
								return new ResponseObject<PageSplit<Commodity_price>>(ResponseCode.SUCCESS_CODE, "根据关键字获取商品纪录列表失败");
								//throw ExceptionUtil.handle2ServiceException("根据关键字获取商品纪录列表失败", e);
							}
							ResponseObject<PageSplit<Commodity_price>> responseObj = new ResponseObject<PageSplit<Commodity_price>>();
							responseObj.setCode(ResponseCode.SUCCESS_CODE);
							responseObj.setData(pageSplit);
							return responseObj;
						} else {
							return new ResponseObject<PageSplit<Commodity_price>>(ResponseCode.SUCCESS_CODE, "没有获取的商品信息");
						}
						//return responseObj;
					} catch (Exception e) {
						//log.error("获取渠道列表失败", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取商品列表失败");
					}
				}
				
				
				
				
				//根据渠道获取本门市店的可用的商品价格
				@RequestMapping(value = "/admin/commudityPrice/getpricebyself", method = { RequestMethod.GET })
				@ResponseBody
				public ResponseObject<Object> getpricebyself(HttpServletRequest request,
						@RequestParam(value = "cid", required = false, defaultValue = "") String channelId) {
					try {
						String storeId=(String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);//门店id
						
						
						if(StringUtil.isEmpty(storeId))
						{
							return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请先登陆！");
						}
						
						if(StringUtil.isEmpty(channelId)||(channelId.equalsIgnoreCase("-1")))
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"必须上传渠道id号!");
						}
						
						List<Commodity_price> price=this.commudityPriceDao.getByChannelIdandStoreId(channelId, storeId);
						if(price==null||price.size()<1)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"渠道中没有找到可用商品，请修改设置!");
						}
						List<Commodity_price> aprice=new ArrayList<Commodity_price>();
						for(Commodity_price p: price)//开始检查商品中的可用性
						{
							//商品自身是否打开使能开关
							if(StringUtil.isEmpty(p.getState())||(!p.getState().equalsIgnoreCase("1")))//商品类型没有打开开关
							{
								continue;
							}
							
							//渠道授权
							if(p.getChannel()==null)
							{
								continue;
							}
							else
							{
								String storelist=p.getChannel().getStoreList();
								if(StringUtil.isEmpty(storelist))
								{
									continue;
								}
								boolean flag=false;
								String[] list= storelist.split(";");
								for(int i=0;i<list.length;i++)
								{
									if((!StringUtil.isEmpty(list[i]))&&(list[i].equalsIgnoreCase(storeId)))
									{
										flag=true;
										break;
									}
								}
								if(flag!=true)//没有找到授权的门店
								{
									continue;
								}
														
							}
							
							//检查商品类型开关
							if(p.getCommudityAdmin()==null)
							{
								continue;
							}
							else//检查
							{
								if(StringUtil.isEmpty(p.getCommudityAdmin().getState())||(!p.getCommudityAdmin().getState().equalsIgnoreCase("1")))//商品类型没有打开开关
								{
									continue;
								}
							}
							aprice.add(p);
						}
						
						
						if(aprice==null||aprice.size()<1)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"渠道中没有找到可用商品，请修改设置!");
						}
						
						ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
						obj.setData(aprice);
						return obj;
						
						//return responseObj;
					} catch (Exception e) {
						//log.error("获取渠道列表失败", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取商品列表失败");
					}
				}
				
				
				
				//根据渠道获取本门市店的可用的商品价格
				@RequestMapping(value = "/admin/commudityPrice/getpricebycidandwid", method = { RequestMethod.GET })
				@ResponseBody
				public ResponseObject<Object> getpricebycidandwid(HttpServletRequest request,
						@RequestParam(value = "cid", required = false, defaultValue = "") String channelId,
						@RequestParam(value = "wid", required = false, defaultValue = "") String wid) {
					try {
						String storeId=(String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);//门店id
						
						
						if(StringUtil.isEmpty(storeId))
						{
							return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请先登陆！");
						}
						
						if(StringUtil.isEmpty(channelId)||(channelId.equalsIgnoreCase("-1")))
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"必须上传渠道id号!");
						}
						if(StringUtil.isEmpty(wid)||(wid.equalsIgnoreCase("-1")))
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"必须上传门店id号!");
						}
						
						String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
						if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
						{
							if(!wid.equalsIgnoreCase(storeId))
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"不能查询其它门店的商品!");
							}
						}
						List<Commodity_price> price=this.commudityPriceDao.getByChannelIdandStoreId(channelId, wid);
						if(price==null||price.size()<1)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"渠道中没有找到可用商品，请修改设置!");
						}
						List<Commodity_price> aprice=new ArrayList<Commodity_price>();
						for(Commodity_price p: price)//开始检查商品中的可用性
						{
							//商品自身是否打开使能开关
							if(StringUtil.isEmpty(p.getState())||(!p.getState().equalsIgnoreCase("1")))//商品类型没有打开开关
							{
								continue;
							}
							
							//渠道授权
							if(p.getChannel()==null)
							{
								continue;
							}
							else
							{
								String storelist=p.getChannel().getStoreList();
								if(StringUtil.isEmpty(storelist))
								{
									continue;
								}
								boolean flag=false;
								String[] list= storelist.split(";");
								for(int i=0;i<list.length;i++)
								{
									if((!StringUtil.isEmpty(list[i]))&&(list[i].equalsIgnoreCase(wid)))
									{
										flag=true;
										break;
									}
								}
								list=null;
								if(flag!=true)//没有找到授权的门店
								{
									continue;
								}
														
							}
							
							//检查商品类型开关
							if(p.getCommudityAdmin()==null)
							{
								continue;
							}
							else//检查
							{
								if(StringUtil.isEmpty(p.getCommudityAdmin().getState())||(!p.getCommudityAdmin().getState().equalsIgnoreCase("1")))//商品类型没有打开开关
								{
									continue;
								}
							}
							aprice.add(p);
						}
						
						
						if(aprice==null||aprice.size()<1)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"渠道中没有找到可用商品，请修改设置!");
						}
						
						ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
						obj.setData(aprice);
						return obj;
						
						//return responseObj;
					} catch (Exception e) {
						//log.error("获取渠道列表失败", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取商品列表失败");
					}
				}
				
				
				
				//用户端根据门市和渠道获取可用商品
				@RequestMapping(value = "/commudityPrice/getpricebycidandwid", method = { RequestMethod.GET,RequestMethod.POST })
				@ResponseBody
				public ResponseObject<Object> getpricebycidandwidwithuser(HttpServletRequest request,
						@RequestParam(value = "cid", required = false, defaultValue = "") String channelId,
						@RequestParam(value = "wid", required = false, defaultValue = "") String wid) {
					try {
						String userId = StringUtil.obj2String(request.getSession()
								.getAttribute(Constant.USER_ID_SESSION_KEY));
						if(StringUtil.isEmpty(userId))
						{
							return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
						}
						
						
						
						if(StringUtil.isEmpty(channelId)||(channelId.equalsIgnoreCase("-1")))
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"必须上传渠道id号!");
						}
						if(StringUtil.isEmpty(wid)||(wid.equalsIgnoreCase("-1")))
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"必须上传门店id号!");
						}
						
						
						List<Commodity_price> price=this.commudityPriceDao.getByChannelIdandStoreId(channelId, wid);
						if(price==null||price.size()<1)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"渠道中没有找到可用商品，请修改设置!");
						}
						List<Commodity_price> aprice=new ArrayList<Commodity_price>();
						for(Commodity_price p: price)//开始检查商品中的可用性 
						{
							//商品自身是否打开使能开关
							if(StringUtil.isEmpty(p.getState())||(!p.getState().equalsIgnoreCase("1")))//商品类型没有打开开关
							{
								continue;
							}
							
							//渠道授权
							if(p.getChannel()==null)
							{
								continue;
							}
							else
							{
								String storelist=p.getChannel().getStoreList();
								if(StringUtil.isEmpty(storelist))
								{
									continue;
								}
								boolean flag=false;
								String[] list= storelist.split(";");
								for(int i=0;i<list.length;i++)
								{
									if((!StringUtil.isEmpty(list[i]))&&(list[i].equalsIgnoreCase(wid)))
									{
										flag=true;
										break;
									}
								}
								if(flag!=true)//没有找到授权的门店
								{
									continue;
								}
														
							}
							
							//检查商品类型开关
							if(p.getCommudityAdmin()==null)
							{
								continue;
							}
							else//检查
							{
								if(StringUtil.isEmpty(p.getCommudityAdmin().getState())||(!p.getCommudityAdmin().getState().equalsIgnoreCase("1")))//商品类型没有打开开关
								{
									continue;
								}
							}
							aprice.add(p);
						}
						
						
						if(aprice==null||aprice.size()<1)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"渠道中没有找到可用商品，请修改设置!");
						}
						
						ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
						obj.setData(aprice);
						return obj;
						
						//return responseObj;
					} catch (Exception e) {
						//log.error("获取渠道列表失败", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取商品列表失败");
					}
				}
				
				
				//获取转运渠道下的所有商品
				
				
				//用户端根据门市和渠道获取可用商品
				@RequestMapping(value = "/commudityPrice/tran/commudity", method = { RequestMethod.GET,RequestMethod.POST })
				@ResponseBody
				public ResponseObject<Object> getalltrancommudity(HttpServletRequest request) {
					String userId = StringUtil.obj2String(request.getSession()
							.getAttribute(Constant.USER_ID_SESSION_KEY));
						if(StringUtil.isEmpty(userId))
						{
							return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
						}
						try {
							List<Channel> channels=this.channelDao.gettrantype(Constant.CHANNEL_TYRE1);
							if(channels==null||channels.size()==0)
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"没有转运渠道");
							}
							List<List<CommudityAdmin>> tchannels=new ArrayList<List<CommudityAdmin>>();
							for(Channel ch:channels)
							{
								List<CommudityAdmin> comm=this.commudityAdminDao.getByChannelIdandstate(ch.getId(), "1");
								tchannels.add(comm);
							}
						
							TranChannels tranchannels=new TranChannels();
							tranchannels.setChannels(channels);
							tranchannels.setCommudityList(tchannels);
							tranchannels.setNumber(String.valueOf(channels.size()));
							
							ResponseObject<Object> obj= new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
							obj.setData(tranchannels);
							return obj;
							
							
							
						}catch(Exception e){
							return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"获取出现异常");
						}
					}
}

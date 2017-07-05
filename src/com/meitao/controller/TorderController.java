//建立海关打印批次管理相关控制层
package com.meitao.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


















import javax.xml.bind.DatatypeConverter;

import jxl.common.Logger;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.meitao.common.composepics.imgcompose;
import com.meitao.common.constants.ArrayConstant;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ParameterConstants;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.filezip.basiczip;
import com.meitao.common.util.ConsigneeInfoUtil;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.common.util.M_OrderUtil;
import com.meitao.common.util.OrderUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.common.util.TorderUtil;
import com.meitao.common.util.UserUtil;
import com.meitao.model.Account;
import com.meitao.model.AccountDetail;
import com.meitao.model.Channel;
import com.meitao.model.Commodity_price;
import com.meitao.model.CommudityAdmin;
import com.meitao.model.ConsigneeInfo;
import com.meitao.model.CountMorder;
import com.meitao.model.CountTorder;
import com.meitao.model.Employee;
import com.meitao.model.FreezeMoney;
import com.meitao.model.HuitongNumber;
import com.meitao.model.M_OrderDetail;
import com.meitao.model.M_order;
import com.meitao.model.PageSplit;
import com.meitao.model.Receive_User;
import com.meitao.model.ResponseObject;
import com.meitao.model.ResponseString;
import com.meitao.model.Route;
import com.meitao.model.SeaNumber;
import com.meitao.model.Shelves_position;
import com.meitao.model.T_order;
import com.meitao.model.T_route;
import com.meitao.model.T_tran_price;
import com.meitao.model.Tran_order;
import com.meitao.model.User;
import com.meitao.model.Warehouse;
import com.meitao.model.temp.ExportMorder;
import com.meitao.model.temp.ImportMorder;
import com.meitao.model.temp.ImportthirdMorder;


import com.meitao.service.MorderService;
import com.meitao.service.TorderService;
import com.meitao.controller.BasicController;
import com.meitao.cardid.manage.import_t_orders;
import com.meitao.dao.AccountDao;
import com.meitao.dao.AccountDetailDao;
import com.meitao.dao.CardIdManageDao;
import com.meitao.dao.ChannelDao;
import com.meitao.dao.CommudityAdminDao;
import com.meitao.dao.CommudityPriceDao;
import com.meitao.dao.ConsigneeInfoDao;
import com.meitao.dao.EmployeeDao;
import com.meitao.dao.FreezeMoneyDao;
import com.meitao.dao.HuitongNumberDao;
import com.meitao.dao.MorderDao;
import com.meitao.dao.Receive_UserDao;
import com.meitao.dao.RouteDao;
import com.meitao.dao.SeaNumberDao;
import com.meitao.dao.Shelves_positionDao;
import com.meitao.dao.TorderDao;
import com.meitao.dao.TrouteDao;
import com.meitao.dao.TtranpriceDao;
import com.meitao.dao.UserDao;
import com.meitao.dao.WarehouseDao;
import com.meitao.dao.globalargsDao;
import com.meitao.exception.ServiceException;
import com.meitao.cardid.manage.CardId_lib;

@Controller
public class TorderController extends BasicController {

	private static final long serialVersionUID = 1058971052693785202L;
	private static final Logger log = Logger
			.getLogger(TorderController.class);


	@Resource(name = "torderService")
	private TorderService torderService;
	

	
	@Autowired
	private Receive_UserDao receive_UserDao;
	
	@Autowired
	private WarehouseDao warehouseDao;
	
	@Autowired
	private TorderDao torderDao;

	@Autowired
	private Shelves_positionDao shelves_positionDao;
	
	
	@Autowired
	private TrouteDao trouteDao;
	
	@Value(value = "${page_size}")
	private int defaultPageSize;

	@Autowired
	private MorderDao m_orderDao;
	
	@Autowired
	private ChannelDao channelDao;

	@Autowired
	private FreezeMoneyDao freezeMoneyDao;
	
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private CommudityPriceDao commudityPriceDao;
	
	@Autowired
	private CommudityAdminDao commudityAdminDao;//管理员添加的商品管理
	@Autowired
	private globalargsDao globalargsDao;
	
	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private TtranpriceDao ttranpriceDao;
	
	@Autowired
	private AccountDao accountDao;

	@Autowired
	private AccountDetailDao accountDetailDao;
	
	
	
	@Autowired
	private ConsigneeInfoDao consigneeInfoDao;
	
	@Value(value = "${save_card_dir}")
	private String saveCardDir;
	

	
	@Value(value = "${default_excel_type}")
	private String defaultExcelFileType;
	
	@Value(value = "${default_img_size}")
	private long defaultCardFileSize;
	@Value(value = "${save_258_card_url}")
	private String save258CardDir;
	@Value(value = "${default_img_type}")
	private String defaultCardFileType;
	
	
	
	
		
	
	//用户添加预报
	@RequestMapping(value="/user/t_order/yb_add", produces="text/plain;charset=UTF-8",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String adtorderbyuser(HttpServletRequest request,
			@RequestParam(value = "danhao") String[] danhao,
			@RequestParam(value = "goodname") String[] goodname
			){
		
		
		String userId = StringUtil.obj2String(request.getSession()
				.getAttribute(Constant.USER_ID_SESSION_KEY));
			if(StringUtil.isEmpty(userId))
			{
				ResponseString responseString=new ResponseString();
				responseString.setCode(ResponseCode.NEED_LOGIN);
				responseString.setMessage("请登陆后操作!");
				return responseString.toString();
			}
		
			if(danhao==null||danhao.length<1)
			{
				ResponseString responseString=new ResponseString();
				responseString.setCode(ResponseCode.PARAMETER_ERROR);
				responseString.setMessage("参数错误,没有接收到运单信息!");
				return responseString.toString();
			}
			if(goodname==null||goodname.length<1)
			{
				ResponseString responseString=new ResponseString();
				responseString.setCode(ResponseCode.PARAMETER_ERROR);
				responseString.setMessage("参数错误,没有接收到商品备注信息！");
				return responseString.toString();
			}
			int number=0;
			for(int i=0;i<danhao.length;i++)
			{
				if(!StringUtil.isEmpty(danhao[i]))
				{
					if(danhao[i].length()<8)
					{
						ResponseString responseString=new ResponseString();
						responseString.setCode(ResponseCode.PARAMETER_ERROR);
						responseString.setMessage("转运单号长度不能小于8！");
						return responseString.toString();
					}
					
					if(StringUtil.isEmpty(goodname[i]))
					{
						ResponseString responseString=new ResponseString();
						responseString.setCode(ResponseCode.PARAMETER_ERROR);
						responseString.setMessage("转运单号"+danhao[i]+"商品信息不能为空!");
						return responseString.toString();
					}
					number++;
				}
			}
			
			//创建转运单号
			List<T_order> torders=new ArrayList<T_order>();
			for(int i=0;i<danhao.length;i++)
			{
				if(!StringUtil.isEmpty(danhao[i]))
				{
					T_order torder=new T_order();
					torder.setTorderId(danhao[i].trim().toUpperCase());//设置转运单号
					torder.setRemark(goodname[i]);//设置商品备注信息
					torder.setUserId(userId);
					torder.setState(Constant.T_ORDER_STATE0);//用户预报状态
					torder.setType(Constant.T_ORDER_TYPE0);//转运类型
					
					torders.add(torder);
				}
			}
			
			if(number==0)
			{
				ResponseString responseString=new ResponseString();
				responseString.setCode(ResponseCode.PARAMETER_ERROR);
				responseString.setMessage("没有运单信息！");
				return responseString.toString();
			}
			try{
				ResponseObject<Object> obj=this.torderService.add_zy_Torder(torders);
			
				ResponseString responseString=new ResponseString();
				responseString.setCode(obj.getCode());
				responseString.setMessage(obj.getMessage());
				return responseString.toString();
			}catch(Exception e)
			{
				ResponseString responseString=new ResponseString();
				responseString.setCode(ResponseCode.SHOW_EXCEPTION);
				responseString.setMessage("操作发生异常！");
				return responseString.toString();
			}
	}
	
	//根据状态获取用户
	@RequestMapping(value = "/user/t_order/get_t_order", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<PageSplit<T_order>> get(HttpServletRequest request,
			@RequestParam(value = "state", required = false) String state,//搜索状态
			@RequestParam(value = "keyword", required = false) String keyword,//搜索条件
			@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
			@RequestParam(value = "rows", required = false, defaultValue = "10") int rows
			) {
		
		String userId = StringUtil.obj2String(request.getSession()
				.getAttribute(Constant.USER_ID_SESSION_KEY));
			if(StringUtil.isEmpty(userId))
			{
				return new ResponseObject<PageSplit<T_order>>(ResponseCode.NEED_LOGIN);
			}
			if(!StringUtil.isEmpty(state)&&state.equalsIgnoreCase("-1"))
			{
				state=null;
			}
		
	
		
		
	
		

		try {
			return this.torderService.search_zy_byuser(userId, state, keyword, rows, pageIndex);
		} catch (Exception e) {
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取仓失败");
		}
	}
	
	
	
	//根据状态获取用户
		@RequestMapping(value = "/admin/t_order/get_all_t_order", method = { RequestMethod.POST, RequestMethod.GET })
		@ResponseBody
		public ResponseObject<PageSplit<T_order>> getbyadmin(HttpServletRequest request,
				@RequestParam(value = "storeId", required = false) String storeId,//转运仓库
				@RequestParam(value = "i_storeId", required = false) String i_storeId,//本地入库仓库
				@RequestParam(value = "payway", required = false) String payway,//支付方式，用于支付自提时，表示是账号捐款还是现金支付
				@RequestParam(value = "state", required = false) String state,//搜索状态
				@RequestParam(value = "s_createDate", required = false) String s_createDate,//创建的起始时间
				@RequestParam(value = "e_createDate", required = false) String e_createDate,//创建的结束时间
				@RequestParam(value = "s_i_date", required = false) String s_i_date,//入库时间起始时间
				@RequestParam(value = "e_i_date", required = false) String e_i_date,//入库时间结束时间
				@RequestParam(value = "keyword", required = false) String keyword,//搜索条件
				@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
				@RequestParam(value = "rows", required = false, defaultValue = "10") int rows
				) {
			
				
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			String s_storeId=null;
			if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
			{
				s_storeId=null;//表示可以查找所有门店
				
			}else
			{
				s_storeId = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);//可以查找的相关门店
				if(StringUtil.isEmpty(s_storeId))
				{
					return new ResponseObject<PageSplit<T_order>>(ResponseCode.NEED_LOGIN);
				}
			}
			if(StringUtil.isEmpty(storeId)||storeId.equalsIgnoreCase("-1"))
			{
				storeId=null;
			}
			
			if(StringUtil.isEmpty(i_storeId)||i_storeId.equalsIgnoreCase("-1"))
			{
				i_storeId=null;
			}
			
			if(StringUtil.isEmpty(payway)||payway.equalsIgnoreCase("-1"))
			{
				payway=null;
			}
			
	
			
				if(!StringUtil.isEmpty(state)&&state.equalsIgnoreCase("-1"))
				{
					state=null;
				}
			
		
				
				if (StringUtil.isEmpty(s_createDate)
						|| !UserUtil.validateExportDate(s_createDate)) {
					s_createDate = "";
				} else {
					s_createDate = UserUtil.transformerDateString(s_createDate, " 00:00:00");
				}

				if (StringUtil.isEmpty(e_createDate)
						|| !UserUtil.validateExportDate(e_createDate)) {
					e_createDate = "";
				} else {
					e_createDate = UserUtil.transformerDateString(e_createDate, " 23:59:59");
				}
				
				if (StringUtil.isEmpty(s_i_date)
						|| !UserUtil.validateExportDate(s_i_date)) {
					s_i_date = "";
				} else {
					s_i_date = UserUtil.transformerDateString(s_i_date, " 00:00:00");
				}

				if (StringUtil.isEmpty(e_i_date)
						|| !UserUtil.validateExportDate(e_i_date)) {
					e_i_date = "";
				} else {
					e_i_date = UserUtil.transformerDateString(e_i_date, " 23:59:59");
				}
		
			

			try {
			
				
				return this.torderService.search_zy_byadmin(s_createDate,e_createDate,s_i_date,e_i_date,storeId, i_storeId, s_storeId, payway, state, keyword, rows, pageIndex);
				//return this.torderService.search_zy_byuser(userId, state, keyword, rows, pageIndex);
			} catch (Exception e) {
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取仓失败");
			}
		}
		
	
	
		
		//根据状态获取用户
		@RequestMapping(value = "/admin/t_order/get_fail_t_order", method = { RequestMethod.POST, RequestMethod.GET })
		@ResponseBody
		public ResponseObject<PageSplit<T_order>> getfailtorderbyadmin(HttpServletRequest request,
				@RequestParam(value = "storeId", required = false) String storeId,//转运仓库
				@RequestParam(value = "i_storeId", required = false) String i_storeId,//本地入库仓库
				//@RequestParam(value = "payway", required = false) String payway,//支付方式，用于支付自提时，表示是账号捐款还是现金支付
				//@RequestParam(value = "state", required = false) String state,//搜索状态
				@RequestParam(value = "s_createDate", required = false) String s_createDate,//创建的起始时间
				@RequestParam(value = "e_createDate", required = false) String e_createDate,//创建的结束时间
				//@RequestParam(value = "s_i_date", required = false) String s_i_date,//入库时间起始时间
				//@RequestParam(value = "e_i_date", required = false) String e_i_date,//入库时间结束时间
				@RequestParam(value = "keyword", required = false) String keyword,//搜索条件
				@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
				@RequestParam(value = "rows", required = false, defaultValue = "10") int rows
				) {
			
			String state=Constant.T_ORDER_STATE1;//只查询录入失败运单	
			String s_i_date=null;
			String e_i_date=null;
			String payway=null;
		
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			String s_storeId=null;
			if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
			{
				s_storeId=null;//表示可以查找所有门店
				
			}else
			{
				s_storeId = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);//可以查找的相关门店
				if(StringUtil.isEmpty(s_storeId))
				{
					return new ResponseObject<PageSplit<T_order>>(ResponseCode.NEED_LOGIN);
				}
			}
			if(StringUtil.isEmpty(storeId)||storeId.equalsIgnoreCase("-1"))
			{
				storeId=null;
			}
			
			if(StringUtil.isEmpty(i_storeId)||i_storeId.equalsIgnoreCase("-1"))
			{
				i_storeId=null;
			}
			
			if(StringUtil.isEmpty(payway)||payway.equalsIgnoreCase("-1"))
			{
				payway=null;
			}
			
	
			
				if(!StringUtil.isEmpty(state)&&state.equalsIgnoreCase("-1"))
				{
					state=null;
				}
			
		
				
				if (StringUtil.isEmpty(s_createDate)
						|| !UserUtil.validateExportDate(s_createDate)) {
					s_createDate = "";
				} else {
					s_createDate = UserUtil.transformerDateString(s_createDate, " 00:00:00");
				}

				if (StringUtil.isEmpty(e_createDate)
						|| !UserUtil.validateExportDate(e_createDate)) {
					e_createDate = "";
				} else {
					e_createDate = UserUtil.transformerDateString(e_createDate, " 23:59:59");
				}
				
				if (StringUtil.isEmpty(s_i_date)
						|| !UserUtil.validateExportDate(s_i_date)) {
					s_i_date = "";
				} else {
					s_i_date = UserUtil.transformerDateString(s_i_date, " 00:00:00");
				}

				if (StringUtil.isEmpty(e_i_date)
						|| !UserUtil.validateExportDate(e_i_date)) {
					e_i_date = "";
				} else {
					e_i_date = UserUtil.transformerDateString(e_i_date, " 23:59:59");
				}
		
			

			try {
			
				
				return this.torderService.search_zy_byadmin(s_createDate,e_createDate,s_i_date,e_i_date,storeId, i_storeId, s_storeId, payway, state, keyword, rows, pageIndex);
				//return this.torderService.search_zy_byuser(userId, state, keyword, rows, pageIndex);
			} catch (Exception e) {
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取仓失败");
			}
		}
		
		
	
		
		
		//后台获取与入库运单
				@RequestMapping(value = "/admin/t_order/get_input_t_order", method = { RequestMethod.POST, RequestMethod.GET })
				@ResponseBody
				public ResponseObject<PageSplit<T_order>> getinputtorderbyadmin(HttpServletRequest request,
						@RequestParam(value = "storeId", required = false) String storeId,//转运仓库
						@RequestParam(value = "i_storeId", required = false) String i_storeId,//本地入库仓库
						//@RequestParam(value = "payway", required = false) String payway,//支付方式，用于支付自提时，表示是账号捐款还是现金支付
						//@RequestParam(value = "state", required = false) String state,//搜索状态
						@RequestParam(value = "s_createDate", required = false) String s_createDate,//创建的起始时间
						@RequestParam(value = "e_createDate", required = false) String e_createDate,//创建的结束时间
						//@RequestParam(value = "s_i_date", required = false) String s_i_date,//入库时间起始时间
						//@RequestParam(value = "e_i_date", required = false) String e_i_date,//入库时间结束时间
						@RequestParam(value = "keyword", required = false) String keyword,//搜索条件
						@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
						@RequestParam(value = "rows", required = false, defaultValue = "10") int rows
						) {
					
					String state=Constant.T_ORDER_STATE5;//获取已入库运单	
					String s_i_date=null;
					String e_i_date=null;
					String payway=null;
				
					String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
					String s_storeId=null;
					if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
					{
						s_storeId=null;//表示可以查找所有门店
						
					}else
					{
						s_storeId = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);//可以查找的相关门店
						if(StringUtil.isEmpty(s_storeId))
						{
							return new ResponseObject<PageSplit<T_order>>(ResponseCode.NEED_LOGIN);
						}
					}
					if(StringUtil.isEmpty(storeId)||storeId.equalsIgnoreCase("-1"))
					{
						storeId=null;
					}
					
					if(StringUtil.isEmpty(i_storeId)||i_storeId.equalsIgnoreCase("-1"))
					{
						i_storeId=null;
					}
					
					if(StringUtil.isEmpty(payway)||payway.equalsIgnoreCase("-1"))
					{
						payway=null;
					}
					
			
					
						if(!StringUtil.isEmpty(state)&&state.equalsIgnoreCase("-1"))
						{
							state=null;
						}
					
				
						
						if (StringUtil.isEmpty(s_createDate)
								|| !UserUtil.validateExportDate(s_createDate)) {
							s_createDate = "";
						} else {
							s_createDate = UserUtil.transformerDateString(s_createDate, " 00:00:00");
						}

						if (StringUtil.isEmpty(e_createDate)
								|| !UserUtil.validateExportDate(e_createDate)) {
							e_createDate = "";
						} else {
							e_createDate = UserUtil.transformerDateString(e_createDate, " 23:59:59");
						}
						
						if (StringUtil.isEmpty(s_i_date)
								|| !UserUtil.validateExportDate(s_i_date)) {
							s_i_date = "";
						} else {
							s_i_date = UserUtil.transformerDateString(s_i_date, " 00:00:00");
						}

						if (StringUtil.isEmpty(e_i_date)
								|| !UserUtil.validateExportDate(e_i_date)) {
							e_i_date = "";
						} else {
							e_i_date = UserUtil.transformerDateString(e_i_date, " 23:59:59");
						}
				
					

					try {
					
						
						return this.torderService.search_zy_byadmin(s_createDate,e_createDate,s_i_date,e_i_date,storeId, i_storeId, s_storeId, payway, state, keyword, rows, pageIndex);
						//return this.torderService.search_zy_byuser(userId, state, keyword, rows, pageIndex);
					} catch (Exception e) {
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取仓失败");
					}
				}
				
		
		
	//获取所有已入库的包裹
	@RequestMapping(value = "/user/t_order/get_t_order_5", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<PageSplit<T_order>> getallhavein(HttpServletRequest request,
			@RequestParam(value = "keyword", required = false) String keyword,//搜索条件
			@RequestParam(value = "storeId", required = false) String storeId) {
		
		String userId = StringUtil.obj2String(request.getSession()
				.getAttribute(Constant.USER_ID_SESSION_KEY));
			if(StringUtil.isEmpty(userId))
			{
				return new ResponseObject<PageSplit<T_order>>(ResponseCode.NEED_LOGIN);
			}
			
			if(StringUtil.isEmpty(storeId)||storeId.equalsIgnoreCase("-1"))
			{
				storeId=null;
			}
	
		
		
	
		

		try {
			return this.torderService.search_in_byuser(userId, Constant.T_ORDER_STATE5, storeId, null, keyword, 0x7fffffff, 1);
			//return this.torderService.search_zy_byuser(userId, Constant.T_ORDER_STATE5, null, 0x7fffffff, 1);
		} catch (Exception e) {
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取仓失败");
		}
	}
	
	
	//用户删除指定移运单
		@RequestMapping(value = "/user/t_order/delete_one", method = { RequestMethod.POST, RequestMethod.GET })
		@ResponseBody
		public ResponseObject<Object> deleteonebyuser(HttpServletRequest request,
				@RequestParam(value = "id", required = false) String id
				) {
			
			String userId = StringUtil.obj2String(request.getSession()
					.getAttribute(Constant.USER_ID_SESSION_KEY));
				if(StringUtil.isEmpty(userId))
				{
					return new ResponseObject<Object>(ResponseCode.NEED_LOGIN);
				}
			
			try {
				return this.torderService.deleteone(userId, id);
			} catch (Exception e) {
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取仓失败");
			}
		}
		
		
		@RequestMapping(value="/user/t_order/get_one_by_user", method={RequestMethod.POST,RequestMethod.GET})
		@ResponseBody
		public ResponseObject<Object> getonebyuser(HttpServletRequest request,
				@RequestParam(value = "id", required = false) String id){
			try{
				if(StringUtil.isEmpty(id))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数id不能为空!");
				}
				String userId = StringUtil.obj2String(request.getSession()
						.getAttribute(Constant.USER_ID_SESSION_KEY));
				if(StringUtil.isEmpty(userId))
				{
					return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
				}
				
				return this.torderService.getone(userId,id);//获取一条转运单
				
			}catch(Exception e){
				//log.error("get morder fail", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
			}
		}
		
		
		@RequestMapping(value="/user/t_order/get_one_route_by_user", method={RequestMethod.POST,RequestMethod.GET})
		@ResponseBody
		public ResponseObject<Object> getoneroutebyuser(HttpServletRequest request,
				@RequestParam(value = "id", required = false) String id){
			try{
				if(StringUtil.isEmpty(id))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数id不能为空!");
				}
				String userId = StringUtil.obj2String(request.getSession()
						.getAttribute(Constant.USER_ID_SESSION_KEY));
				if(StringUtil.isEmpty(userId))
				{
					return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
				}
				
				return this.torderService.getoneroute(userId, id);//获取一条转运单
				
			}catch(Exception e){
				//log.error("get morder fail", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
			}
		}
		
		
		@RequestMapping(value="/admin/t_order/get_one_route_by_admin", method={RequestMethod.POST,RequestMethod.GET})
		@ResponseBody
		public ResponseObject<Object> getoneroutebyadmin(HttpServletRequest request,
				@RequestParam(value = "id", required = false) String id){
			try{
				if(StringUtil.isEmpty(id))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数id不能为空!");
				}
				String storeId = StringUtil.obj2String(request.getSession()
						.getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
				if(StringUtil.isEmpty(storeId))
				{
					return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
				}
				
				return this.torderService.getoneroute(null, id);//获取一条转运单
				
			}catch(Exception e){
				//log.error("get morder fail", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
			}
		}
		
		//获取一条转运预报，这个预报状态只能是
		@RequestMapping(value="/admin/t_order/get_one_yb", method={RequestMethod.POST,RequestMethod.GET})
		@ResponseBody
		public ResponseObject<Object> getonezy(HttpServletRequest request,
				@RequestParam(value = "torderId", required = false) String torderId){
			try{
				if(StringUtil.isEmpty(torderId))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数运单号不能为为空!");
				}
				torderId=torderId.trim().toUpperCase();
				String storeId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
				if(StringUtil.isEmpty(storeId))
				{
					return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
				}
				
				return this.torderService.getoneyb(torderId);
				
			}catch(Exception e){
				//log.error("get morder fail", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
			}
		}
		
		
		
		//获取一条转运州过来的包裹
		@RequestMapping(value="/admin/t_order/get_one_zy_in", method={RequestMethod.POST,RequestMethod.GET})
		@ResponseBody
		public ResponseObject<Object> getonezy(HttpServletRequest request,
				@RequestParam(value = "storeId", required = false) String storeId,//入库的转运州仓库id
				@RequestParam(value = "state1", required = false) String state1,//转运入库
				@RequestParam(value = "state2", required = false) String state2,//转运出库
				@RequestParam(value = "torderId", required = false) String torderId){//获取的转运单号
			try{
				String storeId1 = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
				if(StringUtil.isEmpty(storeId1))
				{
					return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
				}
				if(StringUtil.isEmpty(torderId))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数运单号不能为为空!");
				}
				else
				{
					torderId=torderId.trim().toUpperCase();
					if(torderId.length()<8)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"转运单号长度过短，请检查是否正确!");
					}
					
				}
				if(StringUtil.isEmpty(storeId)||storeId.equalsIgnoreCase("-1"))//
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"必须选择所属仓库门店!");
				}
				
				if((StringUtil.isEmpty(state1)||(!state1.equalsIgnoreCase("1")))&&(StringUtil.isEmpty(state2)||(!state2.equalsIgnoreCase("1"))))//
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"必须选择状态!");
				}
				
				List<String> states=new ArrayList<String>();
				if((!StringUtil.isEmpty(state1))&&state1.equalsIgnoreCase("1"))
				{
					states.add(Constant.T_ORDER_STATE3);
				}
				if((!StringUtil.isEmpty(state2))&&state2.equalsIgnoreCase("1"))
				{
					states.add(Constant.T_ORDER_STATE4);
				}
				
				if(states.size()==0)
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"必须选择状态!");
				}
				
				return this.torderService.getonebystore(torderId, states, storeId, Constant.T_ORDER_TYPE1);
				//return this.torderService.getoneyb(torderId);
				
			}catch(Exception e){
				//log.error("get morder fail", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
			}
		}
		
		//转运州入库，此是第一次入库
		@RequestMapping(value="/admin/t_order/set_zy_rh", method={RequestMethod.POST,RequestMethod.GET})
		@ResponseBody
		public ResponseObject<Object> zyruku(HttpServletRequest request,
				@RequestParam(value = "id", required = false) String id,//原转运单号的id
				@RequestParam(value = "torderId", required = false) String torderId,//转运单号
				@RequestParam(value = "userId", required = false) String userId,
				@RequestParam(value = "weight", required = false) String weight,//重量
				@RequestParam(value = "shelvesId", required = false) String shelvesId//货架id
				){//所属用户id
			try{
				if(StringUtil.isEmpty(userId))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"用户id不能为空!");
				}
				
				
				try{
					double weight1=Double.parseDouble(weight);
					if(weight1<=0)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"重量必须大于0");
					}
					
				}catch(Exception e){
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"重量错误");
				}
				
				if(StringUtil.isEmpty(torderId))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"转运单号不能为空！");
				}
				else
				{
					if(torderId.trim().length()<8)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"转运单号太短，请检查是否正确");
					}
					torderId=torderId.trim().toUpperCase();
				}
				
				if(!StringUtil.isEmpty(shelvesId)&&shelvesId.equalsIgnoreCase("-1"))
				{
					shelvesId=null;
				}
				
				torderId=torderId.trim().toUpperCase();
				String storeId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
				String empId=StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY));
				if(StringUtil.isEmpty(storeId))
				{
					return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
				}
				
				//return this.torderService.getoneyb(torderId);
				return this.torderService.zyrkprocess(id, torderId, storeId, empId, userId, weight, shelvesId);
			}catch(Exception e){
				//log.error("get morder fail", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
			}
		}
		//本地转运入库接口
		@RequestMapping(value="/admin/t_order/set_rh", method={RequestMethod.POST,RequestMethod.GET})
		@ResponseBody
		public ResponseObject<Object> zyku(HttpServletRequest request,
				@RequestParam(value = "id", required = false) String id,//原转运单号的id
				@RequestParam(value = "torderId", required = false) String torderId,//转运单号
				@RequestParam(value = "userId", required = false) String userId,
				@RequestParam(value = "weight", required = false) String weight,//重量
				@RequestParam(value = "shelvesId", required = false) String shelvesId//货架id
				){//所属用户id
			try{
				if(StringUtil.isEmpty(userId))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"用户id不能为空!");
				}
				
				
				try{
					double weight1=Double.parseDouble(weight);
					if(weight1<=0)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"重量必须大于0");
					}
					
				}catch(Exception e){
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"重量错误");
				}
				
				if(StringUtil.isEmpty(torderId))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"转运单号不能为空！");
				}
				else
				{
					if(torderId.trim().length()<8)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"转运单号太短，请检查是否正确");
					}
					torderId=torderId.trim().toUpperCase();
				}
				
				if(!StringUtil.isEmpty(shelvesId)&&shelvesId.equalsIgnoreCase("-1"))
				{
					shelvesId=null;
				}
				
				torderId=torderId.trim().toUpperCase();
				String storeId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
				String empId=StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY));
				if(StringUtil.isEmpty(storeId))
				{
					return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
				}
				
				//return this.torderService.getoneyb(torderId);
				//return this.torderService.zyrkprocess(id, torderId, storeId, empId, userId, weight, shelvesId);
				return this.torderService.localkprocess(id, torderId, storeId, empId, userId, weight, shelvesId);
			}catch(Exception e){
				//log.error("get morder fail", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
			}
		}
		
		//转运州出库接口
		@RequestMapping(value="/admin/t_order/zy_ck_state", method={RequestMethod.POST,RequestMethod.GET})
		@ResponseBody
		public ResponseObject<Object> zymodifystate(HttpServletRequest request,
				@RequestParam(value = "torderId", required = false) String torderId,//转运单号
				@RequestParam(value = "tremark", required = false) String tremark//转运备注
				){//所属用户id
			try{
				if(StringUtil.isEmpty(torderId))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"转运单号不能为空!");
				}
				
				torderId=torderId.trim().toUpperCase();
				if(torderId.length()<8)
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"转运单号太短，请检查是否正确!");
				}
				String storeId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
				String empId=StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY));
				if(StringUtil.isEmpty(storeId))
				{
					return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
				}
				String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
				if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))//商级管理员可以出库任何一个人的
				{
					storeId=null;
				}
				else
				{
					
				}
				List<String> pre_state=new ArrayList<String>();
				pre_state.add(Constant.T_ORDER_STATE3);//之前的状态限定在转运入库
				return this.torderService.modifystate(torderId, Constant.T_ORDER_STATE4, pre_state, storeId,empId,tremark);
				
			}catch(Exception e){
				//log.error("get morder fail", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
			}
		}
		
		
		//本地接收转运过来的包裹进行入库
		@RequestMapping(value="/admin/t_order/set_rk_from_zy", method={RequestMethod.POST,RequestMethod.GET})
		@ResponseBody
		public ResponseObject<Object> zytolocalyuku(HttpServletRequest request,
				@RequestParam(value = "id", required = false) String id,//原转运单号的id
				@RequestParam(value = "torderId", required = false) String torderId,//转运单号
				@RequestParam(value = "userId", required = false) String userId,
				@RequestParam(value = "shelvesId", required = false) String shelvesId,
				@RequestParam(value = "weight", required = false) String weight,//重量
				@RequestParam(value = "state1", required = false) String state1,//转运入库
				@RequestParam(value = "state2", required = false) String state2,//转运出库
				@RequestParam(value = "storeId", required = false) String storeId//入库的转运州仓库id
				){//所属用户id
			try{
				if(StringUtil.isEmpty(userId))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"用户id不能为空!");
				}
				
				
				try{
					double weight1=Double.parseDouble(weight);
					if(weight1<=0)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"重量必须大于0");
					}
					
				}catch(Exception e){
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"重量错误");
				}
				
				if(StringUtil.isEmpty(torderId))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"转运单号不能为空！");
				}
				else
				{
					if(torderId.trim().length()<8)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"转运单号太短，请检查是否正确");
					}
					torderId=torderId.trim().toUpperCase();
				}
				
				if(!StringUtil.isEmpty(shelvesId)&&shelvesId.equalsIgnoreCase("-1"))
				{
					shelvesId=null;
				}
				
				torderId=torderId.trim().toUpperCase();
				String storeId1 = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
				String empId=StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY));
				if(StringUtil.isEmpty(storeId1))
				{
					return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
				}
				
				//return this.torderService.getoneyb(torderId);
				//return this.torderService.zyrkprocess(id, torderId, storeId, empId, userId, weight, shelvesId);
				//return this.torderService.localkprocess(id, torderId, storeId, empId, userId, weight, shelvesId);
				return this.torderService.zytolocalkprocess(id, torderId, storeId, storeId1, empId, userId, weight, shelvesId);
			}catch(Exception e){
				//log.error("get morder fail", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
			}
		}
		
		
		
		
		
		//转运失败运单
		@RequestMapping(value="/admin/t_order/set_fail_torder", method={RequestMethod.POST,RequestMethod.GET})
		@ResponseBody
		public ResponseObject<Object> setfailtorder(HttpServletRequest request,
				@RequestParam(value = "torderId", required = false) String torderId,//转运单号
				@RequestParam(value = "remark", required = false) String remark,//商品备注
				@RequestParam(value = "shelvesId", required = false) String shelvesId,//货架id
				@RequestParam(value = "qremark", required = false) String qremark,//挫败备注
				@RequestParam(value = "weight", required = false) String weight//重量
				){//所属用户id
			try{
				String storeId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
				String empId=StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY));
				if(StringUtil.isEmpty(storeId))
				{
					return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作");
				}
				try{
					double weight1=Double.parseDouble(weight);
					if(weight1<=0)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"重量必须大于0");
					}
					
				}catch(Exception e){
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"重量错误");
				}
				
				
				if(StringUtil.isEmpty(torderId))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"转运单号不能为空！");
				}
				else
				{
					if(torderId.trim().length()<8)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"转运单号太短，请检查是否正确!");
					}
					torderId=torderId.trim().toUpperCase();
				}
				
				if(StringUtil.isEmpty(remark))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"物品描述不能为空!");
				}
				
				if(StringUtil.isEmpty(qremark))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"失败说明不能为空!");
				}
				if(StringUtil.isEmpty(shelvesId)||shelvesId.equalsIgnoreCase("-1"))
				{
					shelvesId=null;
				}
				
				return this.torderService.set_fail_torder(torderId, remark, shelvesId, qremark, storeId, empId, weight);//插入转运州失败包裹
				//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "待实现");
			}catch(Exception e){
				//log.error("get morder fail", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
			}
		}
		
		
		
		//本地州直接入库
		@RequestMapping(value="/admin/t_order/set_local_fail_torder", method={RequestMethod.POST,RequestMethod.GET})
		@ResponseBody
		public ResponseObject<Object> setfailtorderforlocal(HttpServletRequest request,
				@RequestParam(value = "torderId", required = false) String torderId,//转运单号
				@RequestParam(value = "remark", required = false) String remark,//商品备注
				@RequestParam(value = "shelvesId", required = false) String shelvesId,//货架id
				@RequestParam(value = "qremark", required = false) String qremark,//挫败备注
				@RequestParam(value = "weight", required = false) String weight//重量
				){//所属用户id
			try{
				String storeId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
				String empId=StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY));
				if(StringUtil.isEmpty(storeId))
				{
					return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作");
				}
				try{
					double weight1=Double.parseDouble(weight);
					if(weight1<=0)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"重量必须大于0");
					}
					
				}catch(Exception e){
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"重量错误");
				}
				
				
				if(StringUtil.isEmpty(torderId))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"转运单号不能为空！");
				}
				else
				{
					if(torderId.trim().length()<8)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"转运单号太短，请检查是否正确!");
					}
					torderId=torderId.trim().toUpperCase();
				}
				
				if(StringUtil.isEmpty(remark))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"物品描述不能为空!");
				}
				
				if(StringUtil.isEmpty(qremark))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"失败说明不能为空!");
				}
				if(StringUtil.isEmpty(shelvesId)||shelvesId.equalsIgnoreCase("-1"))
				{
					shelvesId=null;
				}
				return this.torderService.set_local_fail_torder(torderId, remark, shelvesId, qremark, storeId, empId, weight);//本地入库失败运单
				//return this.torderService.set_fail_torder(torderId, remark, shelvesId, qremark, storeId, empId, weight);//插入转运州失败包裹
				//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "待实现");
			}catch(Exception e){
				//log.error("get morder fail", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
			}
		}
		
		//本地州接收转运州过来的包裹失败提交接口
				@RequestMapping(value="/admin/t_order/set_localinzy_fail_torder", method={RequestMethod.POST,RequestMethod.GET})
				@ResponseBody
				public ResponseObject<Object> setfailtorderforlocalinzy(HttpServletRequest request,
						@RequestParam(value = "torderId", required = false) String torderId,//转运单号
						@RequestParam(value = "remark", required = false) String remark,//商品备注
						@RequestParam(value = "shelvesId", required = false) String shelvesId,//货架id
						@RequestParam(value = "qremark", required = false) String qremark,//挫败备注
						@RequestParam(value = "weight", required = false) String weight,//重量
						@RequestParam(value = "storeId", required = false) String storeId//所属门店
						){//所属用户id
					try{
						
						String i_storeId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
						String empId=StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY));
						if(StringUtil.isEmpty(i_storeId))
						{
							return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作");
						}
						try{
							double weight1=Double.parseDouble(weight);
							if(weight1<=0)
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"重量必须大于0");
							}
							
						}catch(Exception e){
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"重量错误");
						}
						
						
						if(StringUtil.isEmpty(torderId))
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"转运单号不能为空！");
						}
						else
						{
							if(torderId.trim().length()<8)
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"转运单号太短，请检查是否正确!");
							}
							torderId=torderId.trim().toUpperCase();
						}
						
						if(StringUtil.isEmpty(remark))
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"物品描述不能为空!");
						}
						
						if(StringUtil.isEmpty(qremark))
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"失败说明不能为空!");
						}
						if(StringUtil.isEmpty(shelvesId)||shelvesId.equalsIgnoreCase("-1"))
						{
							shelvesId=null;
						}
						if(StringUtil.isEmpty(storeId)||storeId.equalsIgnoreCase("-1"))
						{
							return generateResponseObject(ResponseCode.PARAMETER_ERROR, "必须选择所属转运门店");
						}
						
						return this.torderService.set_zyinlogcal_fail_torder(torderId, remark, shelvesId, qremark, storeId, i_storeId, empId, weight);
						//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "待实现");
					}catch(Exception e){
						//log.error("get morder fail", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
					}
				}
				
				
				//核实用户提交的价格
				@RequestMapping(value="/user/t_order/submit_torder_check_price", method={RequestMethod.POST,RequestMethod.GET})
				@ResponseBody
				public ResponseObject<Object> submittorderandcheckprice(HttpServletRequest request,Tran_order tranorder){//所属用户id
					try{
						
 						String userId = StringUtil.obj2String(request.getSession()
								.getAttribute(Constant.USER_ID_SESSION_KEY));
						if(StringUtil.isEmpty(userId))
						{
							return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后再操作");
						}
						User user=this.userDao.getUserById(userId);
						if(tranorder==null)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数错误");
						}
						if(tranorder.getTorderIds()==null||tranorder.getTorderIds().size()==0)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"必须选择要提交的预报单号");
						}
						
						
						double alliweight=0;//入库重量
						double t_weight=0;//转运州包裹入库重量
						double insurnce=0;//保险
						double addtionalprice=0;//渠道附加价格
						
						
						
						
						List<String> storeIds=new ArrayList<String>();//存入入库门店id
						for(String tid:tranorder.getTorderIds())
						{
							
							
							ResponseObject<Object> obj=this.torderService.getone(userId, tid);
							if(obj!=null&&ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj.getCode()))
							{
								T_order torder=(T_order)obj.getData();
								if(torder==null)
								{
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取预报单号出错");
								}
								
								if(!Constant.T_ORDER_STATE5.equalsIgnoreCase(torder.getState()))
								{
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"转运单号："+torder.getTorderId()+"的状态是："+TorderUtil.transformerState(0, torder.getState()));
								}
								if(storeIds.size()==0)
								{
									storeIds.add(torder.getI_storeId());	
								}
								else
								{
									for(String sid:storeIds)
									{
										if(!sid.equalsIgnoreCase(torder.getI_storeId()))
										{
											return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"预报单号中存在不同的入库仓库！");
										}
									}
									storeIds.add(torder.getI_storeId());	
								}
								
								String i_weight=torder.getI_weight();
								alliweight+=Double.parseDouble(i_weight);
								
								if(Constant.T_ORDER_TYPE1.equalsIgnoreCase(torder.getType()))//这是从转运州过来的，要计算转动费用
								{
									t_weight+=Double.parseDouble(i_weight);
								}
								
							}
							else
							{
								return obj;
							}
						}
						
						//String jwweight=this.torderService.getjwweight(String.valueOf(allweight));
						double hprice=0;//保存最高价格
						//检查分箱
						if(tranorder.getMorder()==null||tranorder.getMorder().size()==0)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错，包裹不能为空！");
						}
						int number=1;
						
						for(M_order morder:tranorder.getMorder())
						{
							
							//检查收件人是否存在
							if(StringUtil.isEmpty(morder.getRuserId()))
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"收件人信息不能为空");
							}
							ConsigneeInfo userinfo=this.consigneeInfoDao.retrieveConsigneeInfoById(morder.getRuserId(), userId);
							if(userinfo==null)
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取收件人信息错误");
							}
							
							M_order morder1=TorderUtil.cusertoruserofmorder(userinfo, morder);//复制地址
							if(morder1==null)
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"复制地址错误!");
							}
							
							morder=morder1;
							
							morder1=TorderUtil.usertoruserofmorder(this.userDao.getUserById(userId), morder);//复制发件人地址
							if(morder1==null)
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"复制地址错误!");
							}
							morder=morder1;
							//检查商品是否属于同一渠道
							if(morder.getDetail()==null)
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中存在空商品，请检查！");
							}
							number++;
							
							List<M_OrderDetail> details=morder.getDetail();
							List<String> ids=new ArrayList<String>();
							int allquantity=0;
							double allvalue=0;
							
							for(M_OrderDetail d:details)
							{
								CommudityAdmin  comm=this.commudityAdminDao.getById(d.getCommodityId());
								if(comm==null)
								{
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中商品"+d.getName()+"不存在，请检查！");
									
								}
								if(ids.size()==0)
								{
									ids.add(comm.getChannelId());
								}
								else
								{
									for(String id:ids)
									{
										if(!id.equalsIgnoreCase(comm.getChannelId()))
										{
											return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中商品不能混合打包，请检查！");
										}
									}
									ids.add(comm.getChannelId());
								}
								
								
								try{
									
									double value=Double.parseDouble(d.getValue());
									allvalue=allvalue+value;
									if(value<=0)
									{
										return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中商品的价值必须大于0，请检查！");
									}
									
								}catch(Exception e){
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中商品的价值有问题，请检查！");
								}
								
								try{
									
									int quantity=Integer.parseInt(d.getQuantity());
									allquantity=allquantity+quantity;
									if(quantity<=0)
									{
										return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中商品的数量必须大于0，请检查！");
									}
									
								}catch(Exception e){
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中商品的数量有问题，请检查！");
								}
								
								if(StringUtil.isEmpty(d.getProductName()))
								{
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中商品的品名不能为空，请检查！");
								}
								
								if(StringUtil.isEmpty(d.getBrands()))
								{
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中商品的品牌不能为空，请检查！");
								}
								
								Commodity_price cprice=this.commudityPriceDao.getByInfo(d.getCommodityId(), ids.get(0), storeIds.get(0), null);
								if(cprice!=null)
								{
									String price=this.torderService.getprice(user.getType(), cprice);
									if(price!=null)
									{
										if(hprice<Double.parseDouble(price))
										{
											hprice=Double.parseDouble(price);
										}
									}
								}
								
							}
							
							if(Double.parseDouble(morder.getInsurance())<0)
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中保险不能小于0，请检查！");
							}
							
							insurnce+=Double.parseDouble(morder.getInsurance());
							
							
							
							Channel channel=this.channelDao.getChannelById(ids.get(0));
							
							addtionalprice+=Double.parseDouble(channel.getAdditivePrice());
											
							
							
							
							
							morder.setChannelId(ids.get(0));//标识渠道id
							morder.setStoreId(storeIds.get(0));//商品所属门店id
							morder.setValue(String.valueOf(allvalue));
							morder.setQuantity(String.valueOf(allquantity));
							morder.setTorderIds(tranorder.getTorderIds());//保存转运单id号
							morder.setUserId(userId);//设置运单归属的用户id
							
						}
						
						//计算转运重量
						double tprice=0;
						
						if(t_weight>0)
						{
							
							T_tran_price tp=this.ttranpriceDao.getoneBystoreId(storeIds.get(0));
							if(tp!=null&&!StringUtil.isEmpty(tp.getPrice()))
							{
								try{
									t_weight=Double.parseDouble(this.torderService.getjwweight(String.valueOf(t_weight)));
									double price=Double.parseDouble(tp.getPrice());
									if(price>0)
									{
										tprice=t_weight*price;
									}
								}catch(Exception e){}
							}
						}
						else
						{
							t_weight=0;
						}
						
						//入库重量得到的价格
						alliweight=Double.parseDouble(this.torderService.getjwweight(String.valueOf(alliweight)));
						double iprice=hprice*alliweight;
						if(iprice<=0)
						{
							iprice=0;
						}
						//保险insurnce
						//渠道附加价格addtionalprice
						double freezepirce=tprice+iprice+insurnce+addtionalprice;
						
						
						BigDecimal   b   =   new   BigDecimal(freezepirce);  
						double   f1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
						

						
						double rmbb=Double.parseDouble(user.getRmbBalance());//获取人民币余额
						double usab=Double.parseDouble(user.getUsdBalance());//美元余额
						double rate1=Double.parseDouble(this.globalargsDao.getcontentbyflag("cur_usa_cn"));
						if(rate1<=1)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"系统汇率设置错误，请稍后再操作");
						}
						double allrmb=rmbb+usab*rate1;
						
						double allfrmb=0;
						List<FreezeMoney> list=this.freezeMoneyDao.getbyuserId(userId);//获取用户冻结的金额
						if(list!=null||list.size()<1)
						{
							for(FreezeMoney free:list)
							{
								allfrmb=Double.parseDouble(free.getRmb())+Double.parseDouble(free.getUsa())*rate1;
							}
						}
						
						
						
						if(allrmb-allfrmb>=(f1*rate1))//有足够的钱
						{
							BigDecimal f12=new   BigDecimal((f1*rate1));  
							double   f121   =   f12.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
							return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"需要冻结￥"+String.valueOf((f121))+"($"+String.valueOf((f1))+")");
						}
						else
						{
							if(allrmb-allfrmb>0)
							{
								BigDecimal   aa   =   new   BigDecimal(((allrmb-allfrmb)/rate1));  
								double   ff1   =   aa.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
								
								BigDecimal f12=new   BigDecimal((f1*rate1));  
								double   f121   =   f12.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
								
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"余额不足，需要冻结￥"+String.valueOf((f121))+"($"+String.valueOf((f1))+"),但你可用余额只有￥"+String.valueOf(((allrmb-allfrmb)))+"($"+String.valueOf(ff1)+")");
							}
							else
							{
								BigDecimal f12=new   BigDecimal((f1*rate1));  
								double   f121   =   f12.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"余额不足，需要冻结￥"+String.valueOf((f121))+"($"+String.valueOf((f1))+")");
							}
						}
						
						
						//计算价格
						//this.torderService.calculationt_orderFreight_usertype(t_weight, alliweight, user.getType(), commid, null, storeIds.get(0));
						
						//jwweight 根据进位重量计算价格
						
						//return this.torderService.add_tran_Morder(tranorder.getMorder());
						//return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"操作发生异常");
						
						//ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,String.valueOf(f1));
						//return obj;
					}catch(Exception e){
						return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"操作发生异常");
					}}
				
				
				//提交上来的转运单号
				@RequestMapping(value="/user/t_order/submit_torder", method={RequestMethod.POST,RequestMethod.GET})
				@ResponseBody
				public ResponseObject<Object> submittorder(HttpServletRequest request,Tran_order tranorder){//所属用户id
					try{
						
 						String userId = StringUtil.obj2String(request.getSession()
								.getAttribute(Constant.USER_ID_SESSION_KEY));
						if(StringUtil.isEmpty(userId))
						{
							return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后再操作");
						}
						User user=this.userDao.getUserById(userId);
						if(tranorder==null)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数错误");
						}
						if(tranorder.getTorderIds()==null||tranorder.getTorderIds().size()==0)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"必须选择要提交的预报单号");
						}
						
						
						double alliweight=0;//入库重量
						double t_weight=0;//转运州包裹入库重量
						double insurnce=0;//保险
						double addtionalprice=0;//渠道附加价格
						
						
						
						
						List<String> storeIds=new ArrayList<String>();//存入入库门店id
						for(String tid:tranorder.getTorderIds())
						{
							
							
							ResponseObject<Object> obj=this.torderService.getone(userId, tid);
							if(obj!=null&&ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj.getCode()))
							{
								T_order torder=(T_order)obj.getData();
								if(torder==null)
								{
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取预报单号出错");
								}
								
								if(!Constant.T_ORDER_STATE5.equalsIgnoreCase(torder.getState()))
								{
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"转运单号："+torder.getTorderId()+"的状态是："+TorderUtil.transformerState(0, torder.getState()));
								}
								if(storeIds.size()==0)
								{
									storeIds.add(torder.getI_storeId());	
								}
								else
								{
									for(String sid:storeIds)
									{
										if(!sid.equalsIgnoreCase(torder.getI_storeId()))
										{
											return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"预报单号中存在不同的入库仓库！");
										}
									}
									storeIds.add(torder.getI_storeId());	
								}
								
								String i_weight=torder.getI_weight();
								alliweight+=Double.parseDouble(i_weight);
								
								if(Constant.T_ORDER_TYPE1.equalsIgnoreCase(torder.getType()))//这是从转运州过来的，要计算转动费用
								{
									t_weight+=Double.parseDouble(i_weight);
								}
								
							}
							else
							{
								return obj;
							}
						}
						
						//String jwweight=this.torderService.getjwweight(String.valueOf(allweight));
						double hprice=0;//保存最高价格
						//检查分箱
						if(tranorder.getMorder()==null||tranorder.getMorder().size()==0)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错，包裹不能为空！");
						}
						int number=1;
						
						for(M_order morder:tranorder.getMorder())
						{
							
							//检查收件人是否存在
							if(StringUtil.isEmpty(morder.getRuserId()))
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"收件人信息不能为空");
							}
							ConsigneeInfo userinfo=this.consigneeInfoDao.retrieveConsigneeInfoById(morder.getRuserId(), userId);
							if(userinfo==null)
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取收件人信息错误");
							}
							
							M_order morder1=TorderUtil.cusertoruserofmorder(userinfo, morder);//复制地址
							if(morder1==null)
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"复制地址错误!");
							}
							
							morder=morder1;
							
							morder1=TorderUtil.usertoruserofmorder(this.userDao.getUserById(userId), morder);//复制发件人地址
							if(morder1==null)
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"复制地址错误!");
							}
							morder=morder1;
							//检查商品是否属于同一渠道
							if(morder.getDetail()==null)
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中存在空商品，请检查！");
							}
							number++;
							
							List<M_OrderDetail> details=morder.getDetail();
							List<String> ids=new ArrayList<String>();
							int allquantity=0;
							double allvalue=0;
							
							for(M_OrderDetail d:details)
							{
								CommudityAdmin  comm=this.commudityAdminDao.getById(d.getCommodityId());
								if(comm==null)
								{
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中商品"+d.getName()+"不存在，请检查！");
									
								}
								if(ids.size()==0)
								{
									ids.add(comm.getChannelId());
								}
								else
								{
									for(String id:ids)
									{
										if(!id.equalsIgnoreCase(comm.getChannelId()))
										{
											return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中商品不能混合打包，请检查！");
										}
									}
									ids.add(comm.getChannelId());
								}
								
								
								try{
									
									double value=Double.parseDouble(d.getValue());
									allvalue=allvalue+value;
									if(value<=0)
									{
										return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中商品的价值必须大于0，请检查！");
									}
									
								}catch(Exception e){
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中商品的价值有问题，请检查！");
								}
								
								try{
									
									int quantity=Integer.parseInt(d.getQuantity());
									allquantity=allquantity+quantity;
									if(quantity<=0)
									{
										return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中商品的数量必须大于0，请检查！");
									}
									
								}catch(Exception e){
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中商品的数量有问题，请检查！");
								}
								
								if(StringUtil.isEmpty(d.getProductName()))
								{
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中商品的品名不能为空，请检查！");
								}
								
								if(StringUtil.isEmpty(d.getBrands()))
								{
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中商品的品牌不能为空，请检查！");
								}
								
								Commodity_price cprice=this.commudityPriceDao.getByInfo(d.getCommodityId(), ids.get(0), storeIds.get(0), null);
								if(cprice!=null)
								{
									String price=this.torderService.getprice(user.getType(), cprice);
									if(price!=null)
									{
										if(hprice<Double.parseDouble(price))
										{
											hprice=Double.parseDouble(price);
										}
									}
									d.setCommodityId(cprice.getId());//保存之前的id
								}
								else
								{
									d.setCommodityId("0");
								}
							}
							
							if(Double.parseDouble(morder.getInsurance())<0)
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中保险不能小于0，请检查！");
							}
							
							insurnce+=Double.parseDouble(morder.getInsurance());
							
							
							
							Channel channel=this.channelDao.getChannelById(ids.get(0));
							
							addtionalprice+=Double.parseDouble(channel.getAdditivePrice());
											
							
							
							
							
							morder.setChannelId(ids.get(0));//标识渠道id
							morder.setStoreId(storeIds.get(0));//商品所属门店id
							morder.setValue(String.valueOf(allvalue));
							morder.setQuantity(String.valueOf(allquantity));
							morder.setTorderIds(tranorder.getTorderIds());//保存转运单id号
							morder.setUserId(userId);//设置运单归属的用户id
							
						}
						
						//计算转运重量
						double tprice=0;
						double tcost=0;
						
						if(t_weight>0)
						{
							
							T_tran_price tp=this.ttranpriceDao.getoneBystoreId(storeIds.get(0));
							if(tp!=null&&!StringUtil.isEmpty(tp.getPrice()))
							{
								try{
									t_weight=Double.parseDouble(this.torderService.getjwweight(String.valueOf(t_weight)));
									double price=Double.parseDouble(tp.getPrice());
									if(price>0)
									{
										tprice=t_weight*price;
									}
								}catch(Exception e){}
							}
							
							if(tp!=null&&!StringUtil.isEmpty(tp.getCost()))
							{
								try{
									t_weight=Double.parseDouble(this.torderService.getjwweight(String.valueOf(t_weight)));
									double cost=Double.parseDouble(tp.getCost());
									if(cost>0)
									{
										tcost=t_weight*cost;
									}
								}catch(Exception e){}
							}
							
							
							
						}
						else
						{
							t_weight=0;
						}
						
						//入库重量得到的价格
						alliweight=Double.parseDouble(this.torderService.getjwweight(String.valueOf(alliweight)));
						double iprice=hprice*alliweight;
						if(iprice<=0)
						{
							iprice=0;
						}
						//保险insurnce
						//渠道附加价格addtionalprice
						double freezepirce=tprice+iprice+insurnce+addtionalprice;
						
						
						BigDecimal   b   =   new   BigDecimal(freezepirce);  
						double   f1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
						

						
						double rmbb=Double.parseDouble(user.getRmbBalance());//获取人民币余额
						double usab=Double.parseDouble(user.getUsdBalance());//美元余额
						double rate1=Double.parseDouble(this.globalargsDao.getcontentbyflag("cur_usa_cn"));
						if(rate1<=1)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"系统汇率设置错误，请稍后再操作");
						}
						double allrmb=rmbb+usab*rate1;
						
						double allfrmb=0;
						List<FreezeMoney> list=this.freezeMoneyDao.getbyuserId(userId);//获取用户冻结的金额
						if(list!=null||list.size()<1)
						{
							for(FreezeMoney free:list)
							{
								allfrmb=Double.parseDouble(free.getRmb())+Double.parseDouble(free.getUsa())*rate1;
							}
						}
						double   ff1=0;
						
						
						if(allrmb-allfrmb>=(f1*rate1))//有足够的钱
						{
							
							//return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"需要冻结￥"+String.valueOf((f1*rate1))+"($"+String.valueOf((f1))+")");
						}
						else
						{
							if(allrmb-allfrmb>0)
							{
								BigDecimal   aa   =   new   BigDecimal(((allrmb-allfrmb)/rate1));  
								ff1   =   aa.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"余额不足，需要冻结￥"+String.valueOf((f1*rate1))+"($"+String.valueOf((f1))+"),但你可用余额只有￥"+String.valueOf(((allrmb-allfrmb)))+"($"+String.valueOf(ff1)+")");
							}
							else
							{
								BigDecimal   aa   =   new   BigDecimal(((allrmb-allfrmb)/rate1));  
								ff1   =   aa.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"余额不足，需要冻结￥"+String.valueOf((f1*rate1))+"($"+String.valueOf((f1))+")");
							}
						}
						return this.torderService.add_tran_Morder_1(tranorder.getMorder(), f1, tprice, tcost);
								//add_tran_Morder_1(tranorder.getMorder(), f1);//提交转运单号
						//return this.torderService.add_tran_Morder(tranorder.getMorder());
						//计算价格
						//this.torderService.calculationt_orderFreight_usertype(t_weight, alliweight, user.getType(), commid, null, storeIds.get(0));
						
						//jwweight 根据进位重量计算价格
						
						//return this.torderService.add_tran_Morder(tranorder.getMorder());
						//return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"操作发生异常");
						
						//ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,String.valueOf(f1));
						//return obj;
					}catch(Exception e){
						return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"操作发生异常");
					}}
				
				/*@RequestMapping(value="/user/t_order/submit_torder", method={RequestMethod.POST,RequestMethod.GET})
				@ResponseBody
				public ResponseObject<Object> submityb(HttpServletRequest request,Tran_order tranorder){//所属用户id
					try{
						
 						String userId = StringUtil.obj2String(request.getSession()
								.getAttribute(Constant.USER_ID_SESSION_KEY));
						if(StringUtil.isEmpty(userId))
						{
							return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后再操作");
						}
						
						if(tranorder==null)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数错误");
						}
						if(tranorder.getTorderIds()==null||tranorder.getTorderIds().size()==0)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"必须选择要提交的预报单号");
						}
						List<String> storeIds=new ArrayList<String>();//存入入库门店id
						for(String tid:tranorder.getTorderIds())
						{
							
							
							ResponseObject<Object> obj=this.torderService.getone(userId, tid);
							if(obj!=null&&ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj.getCode()))
							{
								T_order torder=(T_order)obj.getData();
								if(torder==null)
								{
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取预报单号出错");
								}
								
								if(!Constant.T_ORDER_STATE5.equalsIgnoreCase(torder.getState()))
								{
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"转运单号："+torder.getTorderId()+"的状态是："+TorderUtil.transformerState(0, torder.getState()));
								}
								if(storeIds.size()==0)
								{
									storeIds.add(torder.getI_storeId());	
								}
								else
								{
									for(String sid:storeIds)
									{
										if(!sid.equalsIgnoreCase(torder.getI_storeId()))
										{
											return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"预报单号中存在不同的入库仓库！");
										}
									}
									storeIds.add(torder.getI_storeId());	
								}
							}
							else
							{
								return obj;
							}
						}
						
						
						//检查分箱
						if(tranorder.getMorder()==null||tranorder.getMorder().size()==0)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错，包裹不能为空！");
						}
						int number=1;
						
						for(M_order morder:tranorder.getMorder())
						{
							
							//检查收件人是否存在
							if(StringUtil.isEmpty(morder.getRuserId()))
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"收件人信息不能为空");
							}
							ConsigneeInfo userinfo=this.consigneeInfoDao.retrieveConsigneeInfoById(morder.getRuserId(), userId);
							if(userinfo==null)
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取收件人信息错误");
							}
							
							M_order morder1=TorderUtil.cusertoruserofmorder(userinfo, morder);//复制地址
							if(morder1==null)
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"复制地址错误!");
							}
							
							morder=morder1;
							
							morder1=TorderUtil.usertoruserofmorder(this.userDao.getUserById(userId), morder);//复制发件人地址
							if(morder1==null)
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"复制地址错误!");
							}
							morder=morder1;
							//检查商品是否属于同一渠道
							if(morder.getDetail()==null)
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中存在空商品，请检查！");
							}
							number++;
							
							List<M_OrderDetail> details=morder.getDetail();
							List<String> ids=new ArrayList<String>();
							int allquantity=0;
							double allvalue=0;
							
							for(M_OrderDetail d:details)
							{
								CommudityAdmin  comm=this.commudityAdminDao.getById(d.getCommodityId());
								if(comm==null)
								{
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中商品"+d.getName()+"不存在，请检查！");
									
								}
								if(ids.size()==0)
								{
									ids.add(comm.getChannelId());
								}
								else
								{
									for(String id:ids)
									{
										if(!id.equalsIgnoreCase(comm.getChannelId()))
										{
											return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中商品不能混合打包，请检查！");
										}
									}
									ids.add(comm.getChannelId());
								}
								
								
								try{
									
									double value=Double.parseDouble(d.getValue());
									allvalue=allvalue+value;
									if(value<=0)
									{
										return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中商品的价值必须大于0，请检查！");
									}
									
								}catch(Exception e){
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中商品的价值有问题，请检查！");
								}
								
								try{
									
									int quantity=Integer.parseInt(d.getQuantity());
									allquantity=allquantity+quantity;
									if(quantity<=0)
									{
										return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中商品的数量必须大于0，请检查！");
									}
									
								}catch(Exception e){
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中商品的数量有问题，请检查！");
								}
								
								if(StringUtil.isEmpty(d.getProductName()))
								{
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中商品的品名不能为空，请检查！");
								}
								
								if(StringUtil.isEmpty(d.getBrands()))
								{
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"第"+number+"个包裹中商品的品牌不能为空，请检查！");
								}
								
							}
							morder.setChannelId(ids.get(0));//标识渠道id
							morder.setStoreId(storeIds.get(0));//商品所属门店id
							morder.setValue(String.valueOf(allvalue));
							morder.setQuantity(String.valueOf(allquantity));
							morder.setTorderIds(tranorder.getTorderIds());//保存转运单id号
							morder.setUserId(userId);//设置运单归属的用户id
							
						}
						return this.torderService.add_tran_Morder(tranorder.getMorder());
						//return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"操作发生异常");
						
						
					}catch(Exception e){
						return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"操作发生异常");
					}}*/
				
				
				
				//用户获取转运单数量
				@RequestMapping(value="/t_order/get_counts", method={RequestMethod.POST,RequestMethod.GET})
				@ResponseBody
				public ResponseObject<Object> gettordercounts(HttpServletRequest request){
					try{
					
						String userId = StringUtil.obj2String(request.getSession()
								.getAttribute(Constant.USER_ID_SESSION_KEY));
						if(StringUtil.isEmpty(userId))
						{
							return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
						}
						
						
						
						CountTorder torder=new CountTorder();
						torder.setT_totalQ(this.torderDao.getstatecount(userId, null));
						torder.setT_userybQ(this.torderDao.getstatecount(userId, Constant.T_ORDER_STATE0));// 用户预报数量
						torder.setT_wronginputQ(this.torderDao.getstatecount(userId, Constant.T_ORDER_STATE1));//录入失败
						torder.setT_questionQ(this.torderDao.getstatecount(userId, Constant.T_ORDER_STATE2));//包裹异常
						torder.setT_traninputQ(this.torderDao.getstatecount(userId, Constant.T_ORDER_STATE3));//转运入库
						torder.setT_tranoutputQ(this.torderDao.getstatecount(userId, Constant.T_ORDER_STATE4));//转运入库
						torder.setT_haveinputQ(this.torderDao.getstatecount(userId, Constant.T_ORDER_STATE5));//已经入库
						torder.setT_finishedQ(this.torderDao.getstatecount(userId, Constant.T_ORDER_STATE6));//已完成
						torder.setT_getbyselfQ(this.torderDao.getstatecount(userId, Constant.T_ORDER_STATE7));//已自提
						
						
						ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
						obj.setData(torder);
						return obj;
						
					}catch(Exception e){
						log.error("get torder fail", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
					}
				}
				
				
				
				//	//获取转运价格
				//用户获取转运单数量
				@RequestMapping(value="/t_order/get_tranprice", method={RequestMethod.POST,RequestMethod.GET})
				@ResponseBody
				public ResponseObject<PageSplit<T_tran_price>> get_tranprice(HttpServletRequest request,
						@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
						@RequestParam(value = "rows", required = false, defaultValue = "1000") int rows){
					try{
						
						String storeId=null;
						String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
						if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
						{
							storeId=null;//表示可以查找所有门店
							
						}else
						{
							storeId = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
							if((storeId==null)||(storeId.equalsIgnoreCase("")))
							{
								return generateResponseObject(ResponseCode.NEED_LOGIN,
										"你没有登陆!");
							}
						}
						
						
						
						int rowCount=0;
						List<Warehouse> wares=this.warehouseDao.getAll();
						if(wares!=null&&wares.size()>0)
						{
							rowCount=wares.size();
						}
						
						ResponseObject<PageSplit<T_tran_price>> responseObj = new ResponseObject<PageSplit<T_tran_price>>(
								ResponseCode.SUCCESS_CODE);
						int pageSize=rows;
						int pageNow=rows;
						if (rowCount > 0) {
							pageSize = Math.max(pageSize, 1);
							int pageCount = rowCount / pageSize
									+ (rowCount % pageSize == 0 ? 0 : 1);
							pageNow = Math.min(pageNow, pageCount);
							PageSplit<T_tran_price> pageSplit = new PageSplit<T_tran_price>();
							pageSplit.setPageCount(pageCount);
							pageSplit.setPageNow(pageNow);
							pageSplit.setRowCount(rowCount);
							pageSplit.setPageSize(pageSize);

							int startIndex = (pageNow - 1) * pageSize;
							try {
								List<T_tran_price> tranprice=new ArrayList<T_tran_price>();
								if(storeId==null)
								{
									//List<Warehouse> wares=this.warehouseDao.getAll();
									for(Warehouse w:wares)
									{
										T_tran_price price=this.ttranpriceDao.getoneBystoreId(w.getId());
										if(price==null)
										{
											price=new T_tran_price();
											price.setStoreId(w.getId());
										}
										price.setStoreName(w.getName());
										tranprice.add(price);
									}
								}
								else
								{
									T_tran_price price=this.ttranpriceDao.getoneBystoreId(storeId);
									Warehouse ware=this.warehouseDao.getById(storeId);
									if(price==null)
									{
										price=new T_tran_price();
										price.setStoreId(storeId);
									}
									price.setStoreName(ware.getName());
									tranprice.add(price);
									
								}
								pageSplit.setDatas(tranprice);
							} catch (Exception e) {
								throw ExceptionUtil.handle2ServiceException("获取运单列表失败", e);
							}
							responseObj.setData(pageSplit);
						} else {
							responseObj.setMessage("没有运单");
						}
						return responseObj;
						
					}catch(Exception e){
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
					}
				}
				
				//修改转运价格
				@RequestMapping(value="/t_order/tranprice_modify", method={RequestMethod.POST,RequestMethod.GET})
				@ResponseBody
				public ResponseObject<Object> get_tranprice(HttpServletRequest request,
						T_tran_price t_tran_price){
						if(t_tran_price==null||StringUtil.isEmpty(t_tran_price.getStoreId()))
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数错误!");
						}
						
						String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
						if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
						{
							
							
						}else
						{
							String storeId1 = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
							
							if((storeId1==null)||(storeId1.equalsIgnoreCase("")))
							{
								return generateResponseObject(ResponseCode.NEED_LOGIN,
										"你没有登陆!");
							}
							if(!storeId1.equalsIgnoreCase(t_tran_price.getStoreId()))
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"对不起，你不能修改其它门店的数据!");
							}
						}
						String date=DateUtil.date2String(new Date());
						t_tran_price.setModifyDate(date);
						try{
							int k=this.ttranpriceDao.modify(t_tran_price);
							if(k==0)
							{
								//插入一个新的
								t_tran_price.setCreateDate(date);
								int n=this.ttranpriceDao.insert(t_tran_price);
								if(n<1)
								{
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"插入新数据失败!");
								}
							}
							return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"修改成功!");
						}catch(Exception e)
						{
							return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"修改数据发生异常");
						}
				
				}
				
				
				
				//本地州接收转运州过来的包裹失败提交接口
				@RequestMapping(value="/admin/t_order/modify_fail_torder", method={RequestMethod.POST,RequestMethod.GET})
				@ResponseBody
				public ResponseObject<Object> modify_fail_torder(HttpServletRequest request,
						@RequestParam(value = "id", required = false) String id,//转运单号id
						@RequestParam(value = "remark", required = false) String remark,//商品备注
						@RequestParam(value = "qremark", required = false) String qremark,//异常备注
						@RequestParam(value = "userId", required = false) String userId,//归属用户id
						@RequestParam(value = "state", required = false) String state//修改的状态
						){//所属用户id
						try{
							String storeId=null;
							String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
							String empNo=(String)request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY);
							if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
							{
								
								
							}else
							{
								storeId = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
								
								if((storeId==null)||(storeId.equalsIgnoreCase("")))
								{
									return generateResponseObject(ResponseCode.NEED_LOGIN,
											"你没有登陆!");
								}
								
							}
							
							
							if(StringUtil.isEmpty(id))
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数转运单id不能为空!");
							}
							if(StringUtil.isEmpty(userId))
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"归属用户id不能为空!");
							}
							if(StringUtil.isEmpty(state))
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数状态值错误!");
							}
							try{
								int state1=Integer.parseInt(state);
								if(state1<=Integer.parseInt(Constant.T_ORDER_STATE2)||state1>Integer.parseInt(Constant.T_ORDER_STATE7))
								{
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"状态值取值范围不正确!");
								}
							}catch(Exception e){
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数状态值错误!");
							}
							
							T_order torder=this.torderDao.getonebyadmin(storeId, id);
							if(torder==null)
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"请检查转运单号是否存在或是否属于本门店范围内!");
							}
							else
							{
								if(!Constant.T_ORDER_STATE1.equalsIgnoreCase(torder.getState()))
								{
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"当前转运单状态不是录入失败，而是!"+TorderUtil.transformerState(0, torder.getState()));
								}
							}
							String date=DateUtil.date2String(new Date());
							int k=this.torderDao.modify_fail_torder(id, state, remark, qremark, userId,date);
							if(k<1)
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"修改失败!");
							}
							//进行路由操作
							
							String statestr=TorderUtil.transformerState(0, state);
							T_route route=new T_route();
							route.setDate(date);
							route.setEmployeeName("");
							route.setRemark("归属用户，操作员id:"+empNo);
							route.setState(statestr);
							route.setT_id(id);
							route.setT_orderId(torder.getTorderId());
							this.trouteDao.insertTroute(route);//插入路由
							return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"修改成功！");
						}catch(Exception e){
							return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"操作发生异常");
					}}
				//总后台获取一条预报信息
				@RequestMapping(value="/admin/t_order/get_one_by_admin", method={RequestMethod.POST,RequestMethod.GET})
				@ResponseBody
				public ResponseObject<Object> getonebyadmin(HttpServletRequest request,
						@RequestParam(value = "id", required = false) String id){
					try{
						if(StringUtil.isEmpty(id))
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数id不能为空!");
						}
						
						String storeId = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
						if(StringUtil.isEmpty(storeId))
						{
							return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
						}
						
						return this.torderService.getone(null,id);//获取一条转运单
						
					}catch(Exception e){
						//log.error("get morder fail", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
					}
				}
				
				
				//管理员除转运单
				@RequestMapping(value = "/admin/t_order/delete_one", method = { RequestMethod.POST, RequestMethod.GET })
				@ResponseBody
				public ResponseObject<Object> deleteonebyadmin(HttpServletRequest request,
						@RequestParam(value = "id", required = false) String id
						) {
					
					String storeId = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
					if(StringUtil.isEmpty(storeId))
					{
						return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
					}
					
					try {
						T_order torder=this.torderDao.getone(null, id);
						if(torder==null)
						{
							return generateResponseObject(ResponseCode.PARAMETER_ERROR, "根据id获取转运单信息失败");
						}
						if(storeId.equalsIgnoreCase(torder.getStoreId())||storeId.equalsIgnoreCase(torder.getI_storeId()))
						{
							int statevalue=Integer.parseInt(torder.getState());
							if(statevalue==Integer.parseInt(Constant.T_ORDER_STATE0)||statevalue==Integer.parseInt(Constant.T_ORDER_STATE1)||statevalue==Integer.parseInt(Constant.T_ORDER_STATE2))
							{
								int k=this.torderDao.deleteonebyadmin(id);
								if(k>0)
								{
									//清空路由
									List<String> tids=new ArrayList<String>();
									tids.add(id);
									int kk=this.trouteDao.deleteRouteByTids(tids);
									return generateResponseObject(ResponseCode.SUCCESS_CODE, "删除成功");
								}
								else
								{
									return generateResponseObject(ResponseCode.PARAMETER_ERROR, "删除失败");
								}
								
							}
						}
						else
						{
							if(Constant.T_ORDER_STATE0.equalsIgnoreCase(torder.getState()))
							{
								int k=this.torderDao.deleteonebyadmin(id);
								if(k>0)
								{
									//清空路由
									List<String> tids=new ArrayList<String>();
									tids.add(id);
									int kk=this.trouteDao.deleteRouteByTids(tids);
									return generateResponseObject(ResponseCode.SUCCESS_CODE, "删除成功");
								}
								else
								{
									return generateResponseObject(ResponseCode.PARAMETER_ERROR, "删除失败");
								}
							}
						}
						return generateResponseObject(ResponseCode.PARAMETER_ERROR, "当前门店或状态下不能够删除此转运单号1");
					} catch (Exception e) {
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取仓失败");
					}
				}
				
				
				//获取门店的转运价格
				
				//管理员除转运单
				@RequestMapping(value = "/admin/TranPrice/getbyself", method = { RequestMethod.POST, RequestMethod.GET })
				@ResponseBody
				public ResponseObject<Object> gettranprice(HttpServletRequest request) {
					
					String storeId = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
					if(StringUtil.isEmpty(storeId))
					{
						return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
					}
					
					try {
						T_tran_price trpice=this.ttranpriceDao.getoneBystoreId(storeId);
						ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
						obj.setData(trpice);
						return obj;
					} catch (Exception e) {
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取转运价格异常");
					}
				}
				
				
				//待处理运单
				@RequestMapping(value = "/admin/t_order/pay_by_self",method = { RequestMethod.GET, RequestMethod.POST})
				@ResponseBody
				public ResponseObject<Object> sumbitpaybyself(
						HttpServletRequest request,
						@RequestParam(value = "i_weight", required = false) String i_weight,//入库重量
						@RequestParam(value = "i_totalmoney", required = false) String i_totalmoney,//入库总费用
						@RequestParam(value = "payway", required = false) String payway,//支付方式
						@RequestParam(value = "qremark", required = false) String qremark,//问题备注
						@RequestParam(value = "id", required = false) String id//问题备注
						) {
					
					
					String storeId = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
					if(StringUtil.isEmpty(storeId))
					{
						return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
					}
					String storeName=(String)request.getSession().getAttribute(Constant.EMP_STORE_NAME_SESSION_KEY);
					String empNo=(String)request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY);
					String empName=(String)request.getSession().getAttribute(Constant.EMP_ACCOUNT_SESSION_KEY);
					
					if(StringUtil.isEmpty(id))
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数错误");
					}
					if(StringUtil.isEmpty(i_weight))
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"重量不能为空");
					}
					else
					{
						try{
							double iweight=Double.parseDouble(i_weight);
							if(iweight<=0)
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"重量必须大于0");
							}
						}catch(Exception e){
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"重量错误");
						}
					}
					
					if(StringUtil.isEmpty(i_totalmoney))
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"费用不能为空");
					}
					else
					{
						try{
							double itotalmoney=Double.parseDouble(i_totalmoney);
							if(itotalmoney<=0)
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"费用必须大于0");
							}
						}catch(Exception e){
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"费用错误");
						}
					}
					try{
						T_order torder=this.torderDao.getone(null, id);
						if(torder==null)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取运单号失败");
						}
						
						String wremark="";
						String date=DateUtil.date2String(new Date());
						torder.setI_jwweight(this.torderService.getjwweight(i_weight));
						torder.setI_weight(i_weight);
						torder.setTotalmoney(i_totalmoney);
						torder.setQremark(qremark);
						torder.setPayway(payway);
						torder.setModifyDate(date);
						
						//计算成本
						T_tran_price ttranprice=this.ttranpriceDao.getoneBystoreId(storeId);
						double tcost=0;
						if(ttranprice!=null)
						{
							tcost=Double.parseDouble(ttranprice.getCost())*Double.parseDouble(torder.getI_weight());
						}
						torder.setTotalcost(String.valueOf(tcost));
						
						if(!Constant.T_ORDER_STATE5.equalsIgnoreCase(torder.getState()))
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"当前状态不是已入库");
						}
						torder.setState(Constant.T_ORDER_STATE7);//标记为自提
						Account account = new Account();
						if(payway.equalsIgnoreCase("0"))//余额支付
						{
							//余额支付
							
							if(torder.getUser()==null)
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取会员信息错误");
							}
							double rmb=Double.parseDouble(torder.getUser().getRmbBalance());//
							double usa=Double.parseDouble(torder.getUser().getUsdBalance());//美元余额
							double rate1=Double.parseDouble(this.globalargsDao.getcontentbyflag("cur_usa_cn"));
							if(rate1<=1)
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"系统汇率设置错误，请稍后再操作");
							}
							
							double newusa=usa-Double.parseDouble(i_totalmoney);
							double newrmb=rmb;
							if(newusa>=0)//美元足够支付
							{
								account.setUsd(String.valueOf(newusa));
								account.setRmb(String.valueOf(newrmb));
								account.setUserId(torder.getUser().getId());
								account.setModifyDate(date);
								if (this.accountDao.modifyAccount(account) > 0) {}
								else
								{
									throw new Exception("修改账户失败");
								}
							}
							else
							{
								newrmb=rmb+newusa*rate1;
								if(newrmb>=0)//人民币足够支付
								{
									newusa=0;//新的美元
									
									account.setUsd(String.valueOf(newusa));
									account.setRmb(String.valueOf(newrmb));
									account.setUserId(torder.getUser().getId());
									account.setModifyDate(date);
									if (this.accountDao.modifyAccount(account) > 0) {}
									else
									{
										throw new Exception("修改账户失败");
									}
								
								}
								else
								{
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"余额不足");
									
								}
							}
							
							
							AccountDetail detail = new AccountDetail();
							detail.setAmount(torder.getTotalmoney());
							detail.setCreateDate(date);
							detail.setModifyDate(date);
							detail.setState(Constant.ACCOUNT_DETAIL_STATE2);
							detail.setCurrency("美元");
							detail.setName("转运自提");
							detail.setType(Constant.ACCOUNT_DETAIL_TYPE2);
							detail.setUserId(torder.getUser().getId());
							detail.setConfirm_state("0");
							detail.setStoreId(storeId);
							detail.setEmpStore(storeName);
							detail.setEmpId(empNo);
							detail.setEmpName(empName);
							
							detail.setRemark("后台账户支付，转运单号："+torder.getTorderId());
							
							int m=this.accountDetailDao.insertAccountDetail(detail);
							
							
							int k=this.torderDao.modify_paybyself_torder(torder);
							if(k==0)
							{
								throw new Exception("现金支付失败，因为修改数据库失败！");
							}
							if(!StringUtil.isEmpty(torder.getPositionId()))//表示原来有仓位，要清空仓位
							{
							
									Shelves_position position1=this.shelves_positionDao.getbyid(torder.getPositionId());
									if(position1!=null)
									{
										wremark="，清空仓位"+position1.getPosition();
										this.shelves_positionDao.updatestate(torder.getPositionId(), Constant.POSITION_STATE0, date, "");
										//清空占用的他们
										this.torderDao.clear_positionId(torder.getPositionId(), date);
										
									}
							}
							
							
							
							
							//转运路由操作
							String statestr=TorderUtil.transformerState(0, torder.getState());
							T_route route=new T_route();
							route.setDate(date);
							route.setEmployeeName(empName);
							route.setRemark("用户自提，余额支付");
							route.setState(statestr);
							route.setT_id(torder.getId());
							route.setT_orderId(torder.getTorderId());
							this.trouteDao.insertTroute(route);//插入路由
							
							
							return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"账户支付成功。"+wremark);
						}
						else if(payway.equalsIgnoreCase("1"))//现金支付
						{
							int k=this.torderDao.modify_paybyself_torder(torder);
							if(k==0)
							{
								throw new Exception("现金支付失败，因为修改数据库失败！");
							}
							if(!StringUtil.isEmpty(torder.getPositionId()))//表示原来有仓位，要清空仓位
							{
							
									Shelves_position position1=this.shelves_positionDao.getbyid(torder.getPositionId());
									if(position1!=null)
									{
										wremark="，清空仓位"+position1.getPosition();
										this.shelves_positionDao.updatestate(torder.getPositionId(), Constant.POSITION_STATE0, date, "");
										//清空占用的他们
										this.torderDao.clear_positionId(torder.getPositionId(), date);
										
									}
							}
							
							AccountDetail detail = new AccountDetail();
							detail.setAmount(torder.getTotalmoney());
							detail.setCreateDate(date);
							detail.setModifyDate(date);
							detail.setState(Constant.ACCOUNT_DETAIL_STATE2);
							detail.setCurrency("美元");
							detail.setName("转运自提");
							detail.setType(Constant.ACCOUNT_DETAIL_TYPE2);
							detail.setUserId(torder.getUser().getId());
							detail.setConfirm_state("0");
							detail.setStoreId(storeId);
							detail.setEmpStore(storeName);
							detail.setEmpId(empNo);
							detail.setEmpName(empName);
							
							detail.setRemark("现金支付，转运单号："+torder.getTorderId());
							
							int m=this.accountDetailDao.insertAccountDetail(detail);
							
							
							//转运路由操作
							String statestr=TorderUtil.transformerState(0, torder.getState());
							T_route route=new T_route();
							route.setDate(date);
							route.setEmployeeName(empName);
							route.setRemark("用户自提，现金支付");
							route.setState(statestr);
							route.setT_id(torder.getId());
							route.setT_orderId(torder.getTorderId());
							this.trouteDao.insertTroute(route);//插入路由
							
							return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"现金支付成功。"+wremark);
							
						}
						else
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"支付方式错误!");
						}
						
					}catch(Exception e){
						
					}
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"");
				}			
			
				
				//
				//待处理运单
				@RequestMapping(value = "/admin/t_order/modify_state",method = { RequestMethod.GET, RequestMethod.POST})
				@ResponseBody
				public ResponseObject<Object> modifystatebyadmin(
						HttpServletRequest request,
						@RequestParam(value = "state", required = false) String state,//修改状态
						@RequestParam(value = "qremark", required = false) String qremark,//问题备注
						@RequestParam(value = "id", required = false) String id//问题备注
						) {
					
					
					String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
					
					if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
					{
				
						
					}else
					{
						String master = (String)request.getSession().getAttribute(Constant.EMP_STORE_MASTER_SESSION_KEY);//店长标志
						if(StringUtil.isEmpty(master))
						{
							return new ResponseObject<Object>(ResponseCode.NEED_LOGIN);
						}
						if(!master.equalsIgnoreCase("1"))
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"对不起，只有店长和高级管理员可以操作");
						}
					}
					
					if(StringUtil.isEmpty(id))
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数错误！"); 
					}
					if(StringUtil.isEmpty(state)||state.equalsIgnoreCase("-1"))
					{
						state=null; 
					}
					
					String storeName=(String)request.getSession().getAttribute(Constant.EMP_STORE_NAME_SESSION_KEY);
					String empNo=(String)request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY);
					String empName=(String)request.getSession().getAttribute(Constant.EMP_ACCOUNT_SESSION_KEY);
					String date=DateUtil.date2String(new Date());
					try{
						T_order torder=this.torderDao.getone(null, id);
						if(torder==null)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取转运单数据失败"); 
						}
						int k=this.torderDao.modify_state_torder(id, state, qremark, date);
						if(k<1)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"修改失败！"); 
						}
						//转运路由操作
						
						if(state!=null)
						{
							String statestr=TorderUtil.transformerState(0, state);
							T_route route=new T_route();
							route.setDate(date);
							route.setEmployeeName(empName);
							route.setRemark("后台修改状态");
							route.setState(statestr);
							route.setT_id(id);
							route.setT_orderId(torder.getTorderId());
							this.trouteDao.insertTroute(route);//插入路由
						}
						return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"修改成功！"); 
					}catch(Exception e){
						return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"操作发生异常");
					}
				}
}
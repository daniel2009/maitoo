package com.meitao.controller;


import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


import javax.servlet.http.HttpServletResponse;



import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.meitao.service.EndiciaService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ParameterConstants;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.common.util.UserUtil;
import com.meitao.dao.EndicialArgDao;
import com.meitao.model.EndiciaLabel;
import com.meitao.model.Endicial_arg;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
@Controller
public class EndiciaController extends BasicController {

	private static final long serialVersionUID = 6582185005680513923L;
	private static final Logger log = Logger.getLogger(EndiciaController.class);
	@Resource(name = "endiciaService")
	private EndiciaService endiciaService;
	@Value(value = "${page_size}")
	private int defaultPageSize;
	
	@Autowired
	private EndicialArgDao endicialArgDao;

	@RequestMapping(value = "/endicial/user_check_price", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> Endiciaargprice(HttpServletRequest request,EndiciaLabel label) {
		String userId = StringUtil.obj2String(request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY));
		
		if (StringUtil.isEmpty(userId)) {
			return new ResponseObject<Object>(ResponseCode.NEED_LOGIN, "请登录后操作");
		}
		if (label == null) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数无效");
		}
		try {
			//return this.newsService.saveNews(news);
			label.setUserId(userId);
			return this.endiciaService.checkediciaprices(label);			
		} catch (Exception e) {
			log.error("添加失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
		}
	}
	
	
	
	@RequestMapping(value = "/admin/endicial/admin_check_price", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> Endiciaargpricebyadmin1(HttpServletRequest request,EndiciaLabel label) {
		if (label == null) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数无效");
		}
		
		String storeId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
		
		if (StringUtil.isEmpty(storeId)) {
			return new ResponseObject<Object>(ResponseCode.NEED_LOGIN, "请登录后操作");
		}
		
		
		if(StringUtil.isEmpty(label.getUserId()))//没有选择用户，将按普通用户来计算
		{
			label.setUserId("-1");
		}
	/*	String userId = StringUtil.obj2String(request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY));
		
		if (StringUtil.isEmpty(userId)) {
			return new ResponseObject<Object>(ResponseCode.NEED_LOGIN, "请登录后操作");
		}*/
		/*if (label == null) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数无效");
		}*/
		try {
			//return this.newsService.saveNews(news);
			//label.setUserId(userId);
			return this.endiciaService.checkediciapricesbyadminnew(label);			
		} catch (Exception e) {
			log.error("添加失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
		}
	}

	@RequestMapping(value = "/endicial/user_label_print_url", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> Endiciaprintlabel(HttpServletRequest request,HttpServletResponse response,EndiciaLabel label) {
		String userId = StringUtil.obj2String(request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY));
		
		if (StringUtil.isEmpty(userId)) {
			return new ResponseObject<Object>(ResponseCode.NEED_LOGIN, "请登录后操作");
		}
		if (label == null) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数无效");
		}
		
		
		try {
			//return this.newsService.saveNews(news);
			label.setUserId(userId);
			ResponseObject<Object> obj= this.endiciaService.printlabel(label,request);	
			
			
			
			if(obj.getCode().equalsIgnoreCase(ResponseCode.SUCCESS_CODE))
			{
				/*InputStream imgInputStream = null;
				OutputStream os = null;
				try {
					
					// 输出流
					response.reset();
					response.setContentType("application/pdf");
					
					response.setHeader("content-disposition", "attachment; filename=label.pdf"); 
					
					
					
					//response.setHeader("Location","download.zip");
					String url=obj.getData().toString();
					imgInputStream = new FileInputStream(url);
	
					byte[] imgBytes = IOUtils.toByteArray(imgInputStream);
	
					os = response.getOutputStream();
	
					os.write(imgBytes);
				} catch (Exception e) {
					log.error("获取运单数据失败", e);
					throw new Exception("获取导出运单数据出现异常，无法获取数据", e);
				} finally {
					// orders.clear();
					// 6.无论成功与否关闭相应的流
					try {
						if (imgInputStream != null) {
							imgInputStream.close();
						}
						if (os != null) {
							os.close();
						}
					} catch (IOException e) {
						System.err.println(e.getMessage());
					}

				}*/
			}
			
			return obj;
		} catch (Exception e) {
			log.error("添加失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "出现异常");
		}
	}
	
	@RequestMapping(value = "/admin/endicial/searchbykey", method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public ResponseObject<PageSplit<EndiciaLabel>> searchByKeyOfAdmin(
			HttpServletRequest request,
	        @RequestParam(value = "userInfo", required = false) String userInfo,
	        @RequestParam(value = "labelInfo", required = false, defaultValue = "") String labelInfo,
	        @RequestParam(value = ParameterConstants.COMMON_SEARCH_SDATE, required = false) String sdate,
	        @RequestParam(value = ParameterConstants.COMMON_SEARCH_EDATE, required = false) String edate,
	        @RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex) {
		

		try {
			

			
			if (StringUtil.isEmpty(sdate) || !UserUtil.validateExportDate(sdate)) {
				sdate = "";
			} else {
				sdate = UserUtil.transformerDateString(sdate, " 00:00:00");
			}

			if (StringUtil.isEmpty(edate) || !UserUtil.validateExportDate(edate)) {
				edate = "";
			} else {
				edate = UserUtil.transformerDateString(edate, " 23:59:59");
			}



				pageIndex = Math.max(pageIndex, 1);
				return this.endiciaService.searchEndiciaLabelByKeys(null, userInfo, labelInfo, sdate, edate, defaultPageSize, pageIndex);
						
			
		} catch (Exception e) {
			log.error("查询运单失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "查询失败");
		}
	}
	
	@RequestMapping(value = "/endicial/searchbyuser", method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public ResponseObject<PageSplit<EndiciaLabel>> searchByKeyOfuser(
			HttpServletRequest request,
	        @RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex) {
		try {
			String userId = StringUtil.obj2String(request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY));
			if (StringUtil.isEmpty(userId)) {
				return new ResponseObject<PageSplit<EndiciaLabel>>(ResponseCode.NEED_LOGIN, "请登录后操作");
			}
		
            return this.endiciaService.searchEndiciaLabelByKeys(userId, null, null, null, null, defaultPageSize, pageIndex);

				//pageIndex = Math.max(pageIndex, 1);
			//	return this.orderService.searchWebOrdersbybelonguser(oid, key, sdate, edate, state,storeid, defaultPageSize, pageIndex);
			
		} catch (Exception e) {
			log.error("查询运单失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "查询失败");
		}
	}
	
	
	@RequestMapping(value = "/admin/endicial/label_arg_url", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> Endiciaargpricebyadmin(HttpServletRequest request,EndiciaLabel label) {
		String userId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
		
		if (StringUtil.isEmpty(userId)) {
			return new ResponseObject<Object>(ResponseCode.NEED_LOGIN, "请登录后操作");
		}
		if (label == null) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数无效");
		}
		try {
			//return this.newsService.saveNews(news);
			//label.setUserId(userId);
			return this.endiciaService.checkediciapricesbyadmin(label);			
		} catch (Exception e) {
			log.error("添加失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "出现异常");
		}
	}
	
	@RequestMapping(value = "/admin/endicial/label_print_url", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> Endiciaprintlabelbyadmin(HttpServletRequest request,HttpServletResponse response,EndiciaLabel label) {
		String storeId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
		
		if (StringUtil.isEmpty(storeId)) {
			return new ResponseObject<Object>(ResponseCode.NEED_LOGIN, "请登录后操作");
		}
		if (label == null) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数无效");
		}
		
		
		try {
			String adminname="";
			adminname=StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_NAME_SESSION_KEY));
			label.setAdminid(StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY)));
			label.setUserId("-1");
			label.setStoreId(storeId);
			ResponseObject<Object> obj= this.endiciaService.printlabelbyadmin(label, request, adminname);	
			
			
			return obj;
		} catch (Exception e) {
			log.error("添加失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "出现异常");
		}
	}

	
	
	@RequestMapping(value = "/admin/endicial/configarg", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> configArg(HttpServletRequest request,HttpServletResponse response,Endicial_arg arg) {
		
		
		String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
		{
			
			
		}
		else
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"对不起，只有高级管理员能够修改此参数!");
		}
		
		if(arg==null)
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错");
		}
		if(StringUtil.isEmpty(arg.getAccountId()))
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"帐号id不能为空");
		}
		if(StringUtil.isEmpty(arg.getApiUrl()))
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"api接口不能为空");
		}
		if(StringUtil.isEmpty(arg.getId()))
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"提交出错");
		}
		if(StringUtil.isEmpty(arg.getImageFormat()))
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"图片格式不能为空");
		}
		if(StringUtil.isEmpty(arg.getLabelSize()))
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"比例类型不能为空");
		}
		if(StringUtil.isEmpty(arg.getPassPhrase()))
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"密钥不能为空");
		}
		if(StringUtil.isEmpty(arg.getRequesterId()))
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"请求者id不能为空");
		}
		if(StringUtil.isEmpty(arg.getTest()))
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"测试类型不能为空");
		}
		arg.setModifyDate(DateUtil.date2String(new Date()));
		String userprice="";
		try{
			double a=Double.parseDouble(arg.getUser_price_0());
			if(a<=0)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"普通会员价格必须大于0!");
			}
			a=Double.parseDouble(arg.getUser_price_1());
			if(a<=0)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"白银VIP会员价格必须大于0!");
			}
			a=Double.parseDouble(arg.getUser_price_2());
			if(a<=0)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"黄金VIP会员价格必须大于0!");
			}
			a=Double.parseDouble(arg.getUser_price_3());
			if(a<=0)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"白金VIP会员价格必须大于0!");
			}
			a=Double.parseDouble(arg.getUser_price_4());
			if(a<=0)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"钻石VIP会员价格必须大于0!");
			}
			a=Double.parseDouble(arg.getUser_price_5());
			if(a<=0)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"至尊VIP1会员价格必须大于0!");
			}
			a=Double.parseDouble(arg.getUser_price_6());
			if(a<=0)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"至尊VIP2会员价格必须大于0!");
			}
			a=Double.parseDouble(arg.getUser_price_7());
			if(a<=0)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"至尊VIP3会员价格必须大于0!");
			}
			a=Double.parseDouble(arg.getUser_price_8());
			if(a<=0)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"至尊VIP4会员价格必须大于0!");
			}
			//组织数据用户价格比例
			userprice=userprice+Constant.USER_TYPE_NORMAL+"="+arg.getUser_price_0();
			userprice=userprice+";"+Constant.USER_TYPE_VIP0+"="+arg.getUser_price_1();
			userprice=userprice+";"+Constant.USER_TYPE_VIP1+"="+arg.getUser_price_2();
			userprice=userprice+";"+Constant.USER_TYPE_VIP2+"="+arg.getUser_price_3();
			userprice=userprice+";"+Constant.USER_TYPE_VIP3+"="+arg.getUser_price_4();
			userprice=userprice+";"+Constant.USER_TYPE_VIP4+"="+arg.getUser_price_5();
			userprice=userprice+";"+Constant.USER_TYPE_VIP5+"="+arg.getUser_price_6();
			userprice=userprice+";"+Constant.USER_TYPE_VIP6+"="+arg.getUser_price_7();
			userprice=userprice+";"+Constant.USER_TYPE_VIP7+"="+arg.getUser_price_8();
			
			
			
		}
		catch(Exception e){
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"会员价格错误!");
		}
		
		arg.setUserprice(userprice);
		
		try{
			int i=this.endicialArgDao.modifybyid(arg);
			if(i<1)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"修改失败!");
			}
			return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"");
		}catch(Exception e)
		{
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改出现异常");
		}
		
	}
	//endicial 参数获取
	@RequestMapping(value = "/admin/endicial/getargs", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> getargs(HttpServletRequest request,HttpServletResponse response
			) {
		
		try{
			
			Endicial_arg arg=this.endicialArgDao.getone();
			if(arg==null)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取失败!");
			}
			
			if(!StringUtil.isEmpty(arg.getUserprice()))
			{
				String[] list=arg.getUserprice().split(";");
				for(int i=0;i<list.length;i++)
				{
					String[] type=list[i].split("=");
					if(type.length>1)
					{
					
						if(Constant.USER_TYPE_NORMAL.equalsIgnoreCase(type[0]))
						{
							arg.setUser_price_0(type[1]);
						}
						else if(Constant.USER_TYPE_VIP0.equalsIgnoreCase(type[0]))
						{
							arg.setUser_price_1(type[1]);
						}
						else if(Constant.USER_TYPE_VIP1.equalsIgnoreCase(type[0]))
						{
							arg.setUser_price_2(type[1]);
						}
						else if(Constant.USER_TYPE_VIP2.equalsIgnoreCase(type[0]))
						{
							arg.setUser_price_3(type[1]);
						}
						else if(Constant.USER_TYPE_VIP3.equalsIgnoreCase(type[0]))
						{
							arg.setUser_price_4(type[1]);
						}
						else if(Constant.USER_TYPE_VIP4.equalsIgnoreCase(type[0]))
						{
							arg.setUser_price_5(type[1]);
						}
						else if(Constant.USER_TYPE_VIP5.equalsIgnoreCase(type[0]))
						{
							arg.setUser_price_6(type[1]);
						}
						else if(Constant.USER_TYPE_VIP6.equalsIgnoreCase(type[0]))
						{
							arg.setUser_price_7(type[1]);
						}
						else if(Constant.USER_TYPE_VIP7.equalsIgnoreCase(type[0]))
						{
							arg.setUser_price_8(type[1]);
						}
							
						
					}
				}
			}
			
			
			ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			obj.setData(arg);
			return obj;
		}catch(Exception e)
		{
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改出现异常");
		}
		
	}
	
}

package com.meitao.controller;

import java.io.IOException;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.common.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.meitao.service.AlipayService;
import com.meitao.service.GlobalArgsService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.PropertiesReader;
import com.meitao.common.util.StringUtil;
import com.meitao.common.util.alipay.config.AlipayConfig;
import com.meitao.common.util.alipay.util.AlipayNotify;
import com.meitao.common.util.alipay.util.AlipaySubmit;
import com.meitao.exception.ServiceException;
import com.meitao.model.AccountDetail;
import com.meitao.model.ResponseObject;

@Controller
public class AlipayController extends BasicController{
	private static final long serialVersionUID = 1381436765256215019L;

	private static final Logger logger = Logger.getLogger(AlipayController.class);
	
	@Resource(name = "alipayService")
	private AlipayService alipayService;
	
	@Resource(name = "globalargsService")
	private GlobalArgsService globalargsService;
	
	@RequestMapping(value="/user/alipay/recharge", method=RequestMethod.GET)
	@ResponseBody
	public void alipayDirectRecharge(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "amount") double amount){
		String typeflag="0";
		String objectname="支付宝充值";
		String baseurl="";
		
		Properties prop = PropertiesReader.read(Constant.SYSTEM_PROPERTIES_FILE);
		String configstr = prop.getProperty("alipay.url.to.system");
		
		try {
			ResponseObject<String> type=this.globalargsService.getByFlag("alipay_type_pay");
			if((type!=null)&&((type.getData()!=null))&&(type.getData().equalsIgnoreCase("1")))
			{
				typeflag="1";//标识支付宝的支付类型
			}
			ResponseObject<String> obj=this.globalargsService.getByFlag("alipay_subject");
			if((obj!=null)&&((obj.getData()!=null))&&(!StringUtil.isEmpty(obj.getData())))
			{
				objectname=obj.getData();
			}
			if(StringUtil.isEmpty(configstr))
			{
				obj=this.globalargsService.getByFlag("alipay_base_url");
				if((obj!=null)&&((obj.getData()!=null))&&(!StringUtil.isEmpty(obj.getData())))
				{
					baseurl=obj.getData();
				}
			}
			else
			{
				obj=this.globalargsService.getByFlag(configstr);
				if((obj!=null)&&((obj.getData()!=null))&&(!StringUtil.isEmpty(obj.getData())))
				{
					baseurl=obj.getData();
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		Map<String, String> map=null;
		if(typeflag.equalsIgnoreCase("1"))
		{
			map = AlipayConfig.getDefaultMap_partner();//调用支付宝的双接口功能
			map.put("extra_common_param", StringUtil.obj2String(request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY)));
			map.put("logistics_fee", Double.toString( amount));

			map.put("price",Double.toString( amount));
			
			
			
			
		}
		else
		{
			map = AlipayConfig.getDefaultMap();//调用支付宝的即时到账的
			map.put("extra_common_param", StringUtil.obj2String(request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY)));
		}
		try {
			alipayService.setMapFromDataBase(map);
		} catch (ServiceException e1) {
			logger.error("根据数据库设置支付宝数据失败");
			e1.printStackTrace();
		}
		map.put("subject", objectname);//订单名称
		
		String userid=request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY).toString();
		if(StringUtil.isEmpty(userid))
		{
			return;
		}
		String out_trade_no=userid+"-"+new Date().getTime() + StringUtil.generateRandomString(5);
		AlipayConfig.setOtherFields(map, String.valueOf(amount), "余额充值", out_trade_no);
		
		
		if(!baseurl.trim().equalsIgnoreCase(""))
		{
			if(typeflag.equalsIgnoreCase("1"))
			{
				map.put("notify_url", baseurl + "/alipay/rechargeSuccessNotifyUrlDouble");
			}
			else
			{
				map.put("notify_url", baseurl + "/alipay/rechargeSuccessNotifyUrl");
			}
			//map.put("return_url", baseurl + "/alipay/rechargeSuccessNotifyUrl1");
			map.put("return_url", baseurl + "/258/user/index.html");
		}
		else
		{
			//map.put("notify_url", "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/alipay/rechargeSuccessNotifyUrl");
			if(typeflag.equalsIgnoreCase("1"))
			{
				map.put("notify_url", "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/alipay/rechargeSuccessNotifyUrlDouble");
			}
			else
			{
				map.put("notify_url", "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/alipay/rechargeSuccessNotifyUrl");
			}
			map.put("return_url", "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/258/user/index.html");
			//map.put("return_url", "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/alipay/return_url");
		}
		String outTradeNo=map.get("out_trade_no");
		
		
		
		String sHtmlText = AlipaySubmit.buildRequest(map, "get", "确认");
		try {
				AccountDetail detail = this.alipayService.checkbeforeRechargeState(outTradeNo);
				if(detail!=null)
				{
					if(Constant.ACCOUNT_DETAIL_STATE2.equals(detail.getState())){//此运单已经存在
						return ;
					}
				}
				
				ResponseObject<String> obj=this.alipayService.beforerecharge(userid, Double.toString(amount), outTradeNo);
				if(!obj.getCode().equalsIgnoreCase(ResponseCode.SUCCESS_CODE))
				{
					return ;
				}
			response.getWriter().print("<html><meta charset=\"UTF-8\"/>" + sHtmlText + "</html>");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(value="/alipay/rechargeSuccessNotifyUrl", method={RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public void rechargeSuccessNotifyUrl(HttpServletRequest request, HttpServletResponse response){
		Map<String, String> map = new HashMap<String, String>();
		Map parameterMap = request.getParameterMap();
		for(Iterator iterator = parameterMap.keySet().iterator(); iterator.hasNext();){
			String name = (String) iterator.next();
			String[] values = (String[]) parameterMap.get(name);
			String value = "";
			for(int i = 0; i < values.length; i++){
				value = (i == values.length-1) ? value + values[i] : value + values[i] + ",";
			}
			try {
//				value = new String(value.getBytes("ISO-8859-1"), "utf-8");
				map.put(name, value);
				String outTradeNo = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "utf-8");
				String tradeNo = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "utf-8");
				String tradeStatus = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "utf-8");
				if(AlipayNotify.verify(map)){//验证成功
					//////////////////////////////////////////////////////////////////////////////////////////
					//请在这里加上商户的业务逻辑程序代码
					String amount = map.get("total_fee");
					String userId = map.get("extra_common_param");//map.get("attach");//(String) request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY);
					
					//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
					
					if(tradeStatus.equals("TRADE_FINISHED")){
						//判断该笔订单是否在商户网站中已经做过处理
							//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
							//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
							//如果有做过处理，不执行商户的业务程序
							
						//注意：
						//退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
					} else if (tradeStatus.equals("TRADE_SUCCESS")){
						//判断该笔订单是否在商户网站中已经做过处理
							//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
							//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
							//如果有做过处理，不执行商户的业务程序
						if(!StringUtil.isEmpty(userId) && !StringUtil.isEmpty(amount) && !StringUtil.isEmpty(outTradeNo)){
							String state = this.alipayService.checkRechargeState(userId, amount, outTradeNo);
							if(!Constant.ACCOUNT_DETAIL_STATE2.equals(state)){//如果返回的状态不是付款完成
								logger.info(DateUtil.date2String(new Date()) + "---用户" + userId + "支付宝即时到账:" + amount + ", 现开始数据库持久化操作");//万一数据库持久化失败，保存用户存款记录
								ResponseObject<String> responseObject = null;
								try {
									responseObject = alipayService.recharge(userId, amount, outTradeNo);
								} catch (ServiceException e) {
									logger.info(DateUtil.date2String(new Date()) + "---用户" + userId + "支付宝即时到账:" + amount + ", 数据库持久化操作---失败");
									e.printStackTrace();
								}
								if(responseObject.getCode().equals(ResponseCode.SUCCESS_CODE)){
									
									logger.info(DateUtil.date2String(new Date()) + "---用户" + userId + "支付宝即时到账:" + amount + ", 数据库持久化操作---完成");
								}else{
									logger.info(DateUtil.date2String(new Date()) + "---用户" + userId + "支付宝即时到账:" + amount + ", 数据库持久化操作---失败");
									
								}
							}
						}
						//注意：
						//付款完成后，支付宝系统发送该交易状态通知
					}
			
					//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
						
					response.getWriter().println("success");	//请不要修改或删除
			
					//////////////////////////////////////////////////////////////////////////////////////////
				}else{//验证失败
					response.getWriter().println("fail");
				}
			}// catch (IOException | ServiceException e) {
			catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}
	//双接口的返回通知
	@RequestMapping(value="/alipay/rechargeSuccessNotifyUrlDouble", method={RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public void rechargeSuccessNotifyUrlbydouble(HttpServletRequest request, HttpServletResponse response){
		
		//logger.info(DateUtil.date2String(new Date()) + "rev ...................................../alipay/rechargeSuccessNotifyUrlDouble");
		Map<String, String> map = new HashMap<String, String>();
		Map parameterMap = request.getParameterMap();
		
		for(Iterator iterator = parameterMap.keySet().iterator(); iterator.hasNext();){
			String name = (String) iterator.next();
			String[] values = (String[]) parameterMap.get(name);
			String value = "";
			for(int i = 0; i < values.length; i++){
				value = (i == values.length-1) ? value + values[i] : value + values[i] + ",";
			}
			map.put(name, value);
		}
		
		if(map.size()>0){
		
			try {
//				
				String outTradeNo = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "utf-8");
				String tradeNo = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "utf-8");
				String tradeStatus = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "utf-8");
				String seller_id= new String(request.getParameter("seller_id").getBytes("ISO-8859-1"), "utf-8");
				
				boolean vflag;
				ResponseObject<String> type=this.globalargsService.getByFlag("alipay_type_pay");
				if((type!=null)&&((type.getData()!=null))&&(type.getData().equalsIgnoreCase("1")))
				{
					 vflag=AlipayNotify.verifynosign(map);//支付宝双接口
				}
				else
				{
					vflag=AlipayNotify.verify(map);
				}
				
				//boolean vflag=AlipayNotify.verifynosign(map);
				//vflag=true;
				if(vflag){//验证成功
					//////////////////////////////////////////////////////////////////////////////////////////
					//请在这里加上商户的业务逻辑程序代码
					String amount = map.get("total_fee");
					String userId = map.get("extra_common_param");//map.get("attach");//(String) request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY);
					
					//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
					//logger.info(DateUtil.date2String(new Date()) + "tradeStatus....................................."+tradeStatus);
					//logger.info(DateUtil.date2String(new Date()) + "outTradeNo....................................."+outTradeNo);
					if(tradeStatus.equals("TRADE_FINISHED")){
						//判断该笔订单是否在商户网站中已经做过处理
							//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
							//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
							//如果有做过处理，不执行商户的业务程序
							
						//注意：
						//退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
						
						
						//取自TRADE_SUCCESS的代码
						if(!StringUtil.isEmpty(amount) && !StringUtil.isEmpty(outTradeNo)){
							
							boolean flag=false;
							AccountDetail detail = this.alipayService.checkbeforeRechargeState(outTradeNo);
							String userid="";
							
							if(detail!=null)
							{
								if(Double.parseDouble(amount)!=Double.parseDouble(amount))
								{
									response.getWriter().println("fail");
									return;
								}
								userid=detail.getUserId();
								//logger.info(DateUtil.date2String(new Date()) + ":查找到AccountDetail");//万一数据库持久化失败，保存用户存款记录
								if((!Constant.ACCOUNT_DETAIL_STATE2.equals(detail.getState())&&(!StringUtil.isEmpty(detail.getUserId())))){//此运单已经存在
									ResponseObject<String> obj= this.alipayService.modifyrecharge(amount, outTradeNo, detail);
									if(ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj.getCode()))
									{
										//logger.info(DateUtil.date2String(new Date()) + "提交成功success...");	
										response.getWriter().println("success");	//请不要修改或删除
										flag=true;
									}
									else
									{
										response.getWriter().println("fail");
									}
								}
								else
								{
									response.getWriter().println("fail");
								}
							}
							else
							{
								response.getWriter().println("fail");
							}
							//logger.info(DateUtil.date2String(new Date())+"结果："+flag);
							
							
							//String state = this.alipayService.checkRechargeState(userId, amount, outTradeNo);
							/*if((flag!=true))
							{
								
								
								if(!Constant.ACCOUNT_DETAIL_STATE2.equals(detail.getState())){//如果返回的状态不是付款完成
									logger.info(DateUtil.date2String(new Date()) + "---用户" + userId + "支付宝即时到账:" + amount + ", 现开始数据库持久化操作");//万一数据库持久化失败，保存用户存款记录
									ResponseObject<String> responseObject = null;
									try {
										responseObject = alipayService.recharge(userId, amount, outTradeNo);
									} catch (ServiceException e) {
										logger.info(DateUtil.date2String(new Date()) + "---用户" + userId + "支付宝即时到账:" + amount + ", 数据库持久化操作---失败");
										e.printStackTrace();
									}
									if(responseObject.getCode().equals(ResponseCode.SUCCESS_CODE)){
										
										logger.info(DateUtil.date2String(new Date()) + "---用户" + userId + "支付宝即时到账:" + amount + ", 数据库持久化操作---完成");
									}else{
										logger.info(DateUtil.date2String(new Date()) + "---用户" + userId + "支付宝即时到账:" + amount + ", 数据库持久化操作---失败");
										
									}
								}
								else
								{
									logger.info(DateUtil.date2String(new Date()) + "此运单已经支付过或操作过:---用户" + userId + "支付宝即时到账:" + amount + ", 现开始数据库持久化操作,outTradeNo:"+outTradeNo);//万一数据库持久化失败，保存用户存款记录
								}
							}*/
						}
						else
						{
							response.getWriter().println("fail");
							logger.info(DateUtil.date2String(new Date()) + "失败:---用户" + userId + "支付宝即时到账:" + amount + ", 现开始数据库持久化操作,outTradeNo:"+outTradeNo);//万一数据库持久化失败，保存用户存款记录
						}
						
						
					} else if (tradeStatus.equals("TRADE_SUCCESS")){
						//判断该笔订单是否在商户网站中已经做过处理
							//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
							//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
							//如果有做过处理，不执行商户的业务程序
						if(!StringUtil.isEmpty(userId) && !StringUtil.isEmpty(amount) && !StringUtil.isEmpty(outTradeNo)){
							String state = this.alipayService.checkRechargeState(userId, amount, outTradeNo);
							if(!Constant.ACCOUNT_DETAIL_STATE2.equals(state)){//如果返回的状态不是付款完成
								//logger.info(DateUtil.date2String(new Date()) + "---用户" + userId + "支付宝即时到账:" + amount + ", 现开始数据库持久化操作");//万一数据库持久化失败，保存用户存款记录
								ResponseObject<String> responseObject = null;
								try {
									responseObject = alipayService.recharge(userId, amount, outTradeNo);
								} catch (ServiceException e) {
									//logger.info(DateUtil.date2String(new Date()) + "---用户" + userId + "支付宝即时到账:" + amount + ", 数据库持久化操作---失败");
									e.printStackTrace();
								}
								if(responseObject.getCode().equals(ResponseCode.SUCCESS_CODE)){
									//logger.info(DateUtil.date2String(new Date()) + "提交成功success...");	
									response.getWriter().println("success");	//请不要修改或删除
									//logger.info(DateUtil.date2String(new Date()) + "---用户" + userId + "支付宝即时到账:" + amount + ", 数据库持久化操作---完成");
								}else{
									//logger.info(DateUtil.date2String(new Date()) + "---用户" + userId + "支付宝即时到账:" + amount + ", 数据库持久化操作---失败");
									
								}
							}
						}
						//注意：
						//付款完成后，支付宝系统发送该交易状态通知
					}
			
					//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
					
			
					//////////////////////////////////////////////////////////////////////////////////////////
				}else{//验证失败
					logger.info(DateUtil.date2String(new Date()) + "验证失败!");
					response.getWriter().println("fail");
				}
			}// catch (IOException | ServiceException e) {
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	@RequestMapping(value="/alipay/return_url", method={RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public void rechargeSuccessReturnUrl(HttpServletRequest request, HttpServletResponse response){
		Map<String, String> map = new HashMap<String, String>();
		Map parameterMap = request.getParameterMap();
		
		
		for(Iterator iterator = parameterMap.keySet().iterator(); iterator.hasNext();){
			String name = (String) iterator.next();
			String[] values = (String[]) parameterMap.get(name);
			String value = "";
			for(int i = 0; i < values.length; i++){
				value = (i == values.length-1) ? value + values[i] : value + values[i] + ",";
			}
			try {
//				value = new String(value.getBytes("ISO-8859-1"), "utf-8");
				map.put(name, value);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		boolean aaa=AlipayNotify.verify(map);
		
		for(Iterator iterator = parameterMap.keySet().iterator(); iterator.hasNext();){
			String name = (String) iterator.next();
			String[] values = (String[]) parameterMap.get(name);
			String value = "";
			for(int i = 0; i < values.length; i++){
				value = (i == values.length-1) ? value + values[i] : value + values[i] + ",";
			}
			try {
//				value = new String(value.getBytes("ISO-8859-1"), "utf-8");
				map.put(name, value);
				String outTradeNo = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "utf-8");
				String tradeNo = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "utf-8");
				String tradeStatus = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "utf-8");
				if(AlipayNotify.verify(map)){//验证成功
					//////////////////////////////////////////////////////////////////////////////////////////
					//请在这里加上商户的业务逻辑程序代码
					String amount = map.get("total_fee");
					String userId = map.get("extra_common_param");//map.get("attach");//(String) request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY);
					
					//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
					
					if(tradeStatus.equals("TRADE_FINISHED")){
						//判断该笔订单是否在商户网站中已经做过处理
							//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
							//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
							//如果有做过处理，不执行商户的业务程序
							
						//注意：
						//退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
					} else if (tradeStatus.equals("TRADE_SUCCESS")){
						//判断该笔订单是否在商户网站中已经做过处理
							//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
							//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
							//如果有做过处理，不执行商户的业务程序
						if(!StringUtil.isEmpty(userId) && !StringUtil.isEmpty(amount) && !StringUtil.isEmpty(outTradeNo)){
							String state = this.alipayService.checkRechargeState(userId, amount, outTradeNo);
							if(!Constant.ACCOUNT_DETAIL_STATE2.equals(state)){//如果返回的状态不是付款完成
								logger.info(DateUtil.date2String(new Date()) + "---用户" + userId + "支付宝即时到账:" + amount + ", 现开始数据库持久化操作");//万一数据库持久化失败，保存用户存款记录
								ResponseObject<String> responseObject = null;
								try {
									responseObject = alipayService.recharge(userId, amount, outTradeNo);
								} catch (ServiceException e) {
									logger.info(DateUtil.date2String(new Date()) + "---用户" + userId + "支付宝即时到账:" + amount + ", 数据库持久化操作---失败");
									e.printStackTrace();
								}
								if(responseObject.getCode().equals(ResponseCode.SUCCESS_CODE)){
									
									logger.info(DateUtil.date2String(new Date()) + "---用户" + userId + "支付宝即时到账:" + amount + ", 数据库持久化操作---完成");
								}else{
									logger.info(DateUtil.date2String(new Date()) + "---用户" + userId + "支付宝即时到账:" + amount + ", 数据库持久化操作---失败");
									
								}
							}
						}
						//注意：
						//付款完成后，支付宝系统发送该交易状态通知
					}
			
					//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
						
					response.getWriter().println("success");	//请不要修改或删除
			
					//////////////////////////////////////////////////////////////////////////////////////////
				}else{//验证失败
					response.getWriter().println("fail");
				}
			} //catch (IOException | ServiceException e) {
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

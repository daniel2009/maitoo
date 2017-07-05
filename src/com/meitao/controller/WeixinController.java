package com.meitao.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.common.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.meitao.service.WeixinService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.common.util.weixin.QRCodeUtil;
import com.meitao.common.util.weixin.WeixinConfig;
import com.meitao.common.util.weixin.WeixinScanPayUtil;
import com.meitao.exception.ServiceException;
import com.meitao.model.ResponseObject;

@Controller
public class WeixinController extends BasicController{

	private static final long serialVersionUID = -8005606389276718942L;
private static final Logger logger = Logger.getLogger(WeixinController.class);
	
	@Resource(name = "weixinService")
	private WeixinService weixinService;
	
	@RequestMapping(value="/weixin/recharge", method=RequestMethod.GET)
	@ResponseBody
	public ResponseObject<String> weixinRecharge(HttpServletRequest request,
			@RequestParam(value = "amount") double amount){
		ResponseObject<String> responseObject = new ResponseObject<String>();
		String payOrderIdsStr = new Date().getTime() + StringUtil.generateRandomString(5);
		Map<String, String> map = WeixinConfig.getDefaultMap();
		try {
			this.weixinService.setMapFromDataBase(map);
			WeixinConfig.setOtherFields(map, String.valueOf((int)(amount*100)), "扫码充值", payOrderIdsStr, payOrderIdsStr);
			map.put("notify_url", "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/weixin/scanRechargeSuccess");//http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/weixin/scanRechargeSuccess
			map.put("attach", (String) request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY));
			String codeUrl = WeixinScanPayUtil.postDataByMap(map);
			if(StringUtil.isEmpty(codeUrl)){
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "扫码加载异常");
			}else{
				responseObject.setData(codeUrl);
				String userId = (String) request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY);
				ResponseObject<String> addResponseObject = this.weixinService.addPreRecharge(userId, amount, payOrderIdsStr);
				if(!ResponseCode.SUCCESS_CODE.equals(addResponseObject.getCode())){
					responseObject.setCode(addResponseObject.getCode());
					responseObject.setMessage(addResponseObject.getMessage());
				}
				request.getSession().setAttribute(Constant.USER_WEIXIN_SCAN_PAY_SESSION_KEY, addResponseObject.getData());
			}
			
		} catch (ServiceException e) {
			e.printStackTrace();
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "扫码加载异常");
		}
		return responseObject;
	}
	@RequestMapping("qr_code.img")
	@ResponseBody
	public void getQRCode(HttpServletResponse response, String codeUrl){
		QRCodeUtil.encodeQRCode(response, codeUrl);
	}
	@RequestMapping(value="/weixin/scanRechargeSuccess", method={RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public void rechargeSuccess(HttpServletRequest request, HttpServletResponse response){
		String requestXml = "";//微信传过来的
		String responseXml = "";//我们这里返回去给微信的
		String nextLine = "";
		try {
			while((nextLine = request.getReader().readLine()) != null){
				requestXml += nextLine;
			}
			request.getReader().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(requestXml);
		
		Map<String, String> map = WeixinScanPayUtil.xmlToMap(requestXml);
		String returnCode = "FAIL";
		String returnMsg = "报文为空";
		if(map == null){
			//没有数据,用户恶意进入
			return ;
		}else if("SUCCESS".equals(map.get("result_code"))){
			returnCode = "SUCCESS";
			returnMsg = "ok";
		}
		responseXml = "<xml><return_code><![CDATA[" + returnCode + "]]></return_code><return_msg><![CDATA[" + returnMsg + "]]></return_msg></xml>"; 
		BufferedOutputStream bufferedOutputStream = null;
		try {
			bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
			bufferedOutputStream.write(responseXml.getBytes());
			bufferedOutputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(null != bufferedOutputStream){
				try {
					bufferedOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		String amount = String.format("%.2f", StringUtil.string2Double(map.get("total_fee"))/100);// String.format("%.2f", StringUtil.string2Double(map.get("total_fee"))/100);//String.format("%.2f", StringUtil.string2Double("1")/100);
		String userId = map.get("attach");//map.get("attach");//(String) request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY);
		String outTradeNo = map.get("out_trade_no");//map.get("outTradeNo");//(String) request.getSession().getAttribute(Constant.USER_WEIXIN_SCAN_PAY_SESSION_KEY);
		logger.info(DateUtil.date2String(new Date()) + "---用户" + userId + "微信扫码支付:" + amount + ", 现开始数据库持久化操作");//万一数据库持久化失败，保存用户存款记录
		ResponseObject<String> responseObject = null;
		try {
			responseObject = weixinService.scanRecharge(userId, amount, outTradeNo);
		} catch (ServiceException e) {
			logger.info(DateUtil.date2String(new Date()) + "---用户" + userId + "微信扫码支付:" + amount + ", 数据库持久化操作---失败");
			e.printStackTrace();
		}
		if(responseObject.getCode().equals(ResponseCode.SUCCESS_CODE)){
			
			logger.info(DateUtil.date2String(new Date()) + "---用户" + userId + "微信扫码支付:" + amount + ", 数据库持久化操作---完成");
		}else{
			logger.info(DateUtil.date2String(new Date()) + "---用户" + userId + "微信扫码支付:" + amount + ", 数据库持久化操作---失败");
			
		}
	}
	
	@RequestMapping(value="/user/weixin/checkIfScanPay", method=RequestMethod.GET)
	@ResponseBody
	public ResponseObject<Object> rechargeSuccess(HttpServletRequest request,
			@RequestParam(value = "amount") String amount){
		String userId = (String) request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY);
		String accountDetailId = (String) request.getSession().getAttribute(Constant.USER_WEIXIN_SCAN_PAY_SESSION_KEY);
		if(null != accountDetailId){
			try{
				ResponseObject<Object> responseObject =  this.weixinService.checkIfScanPay(userId, amount, accountDetailId, null);
				if(responseObject.getCode().equals(ResponseCode.SUCCESS_CODE)){
					request.getSession().removeAttribute(Constant.USER_WEIXIN_SCAN_PAY_SESSION_KEY);
				}
				return responseObject;
			}catch(Exception e){
				logger.error("检查扫码支付出现异常");
				e.printStackTrace();
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "检查扫码支付出现异常");
			}
		}
		return generateResponseObject(ResponseCode.ACCOUNT_SCAN_PAY_NOT_EXISTS, "已没有待充值的信息");
	}
}

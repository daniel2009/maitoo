package com.meitao.common.util.alipay.config;

import java.util.HashMap;
import java.util.Map;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {
	
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	//public static String partner = "2088021646690596";
	
	// 收款支付宝账号，一般情况下收款账号就是签约账号
	//public static String seller_email = "boss@cn2la.com";
	// 商户的私钥
	//public static String key = "k55beytg09c90pnef8q1rzvzlppr75mr";
	
	public static String partner = "";
	
		// 收款支付宝账号，一般情况下收款账号就是签约账号
		public static String seller_email = "";
		// 商户的私钥
	public static String key = "";

	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	

	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "D:\\";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
	
	// 签名方式 不需修改
	public static String sign_type = "MD5";

	/**
	 * need to set up total_fee, body, notify_url, return_url, out_trade_no by self
	 * @return map with default key: service, partner, seller_email, _input_charset, payment_type, subject 
	 */
	public static Map<String, String> getDefaultMap(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("service", "create_direct_pay_by_user");
		map.put("partner", partner);
		map.put("seller_email", seller_email);
		map.put("_input_charset", input_charset);
		map.put("payment_type", "1");
		//map.put("subject", "支付宝立即到账充值");//订单名称
		return map;
	}
	
	
	
	/**
	 * 支付宝双功能接口默认配置
	 * need to set up total_fee, body, notify_url, return_url, out_trade_no by self
	 * @return map with default key: service, partner, seller_email, _input_charset, payment_type, subject 
	 */
	public static Map<String, String> getDefaultMap_partner(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("service", "trade_create_by_buyer");
		map.put("partner", partner);
		map.put("seller_email", seller_email);
		map.put("_input_charset", input_charset);
		map.put("payment_type", "1");
		//map.put("subject", "支付宝双接口支付充值");//订单名称
		map.put("logistics_type", "EXPRESS");
		//map.put("logistics_fee", "0.00");
		map.put("logistics_payment", "SELLER_PAY");
		//map.put("price", "1.00");
		map.put("quantity", "1");
		
		return map;
	}
	
	/**
	 * set up necessary keys after getDefaultMap() method;
	 * @param map
	 */
	public static void setOtherFields(Map<String, String> map, String totalFee, String body, String outTradeNo){
		map.put("total_fee", totalFee);
		map.put("body", body);
		map.put("out_trade_no", outTradeNo);
	}



	public static String getPartner() {
		return partner;
	}



	public static void setPartner(String partner) {
		AlipayConfig.partner = partner;
	}



	public static String getSeller_email() {
		return seller_email;
	}



	public static void setSeller_email(String seller_email) {
		AlipayConfig.seller_email = seller_email;
	}



	public static String getKey() {
		return key;
	}



	public static void setKey(String key) {
		AlipayConfig.key = key;
	}
	
	
	
}

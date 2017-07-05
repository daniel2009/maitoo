package com.meitao.common.util.weixin;

import java.util.HashMap;
import java.util.Map;

public class WeixinConfig {
	public static final String DEFAULT_KEY = "XW56zlrXLbjZ97R9IocxkGUfqvtbPHyF";
	public static final String DEFAULT_APPID = "wxe1dd8f26dd6189a8";
	public static final String DEFAULT_BODY = "扫码充值";
	public static final String DEFAULT_MCH_ID = "1295576401";
	public static final String DEFAULT_NOTIFY_URL = "http://127.0.0.1:8080/meitao/weixin/scanRechargeSuccess";
	public static final String DEFAULT_TRADE_TYPE = "NATIVE";
	
	public static String key = DEFAULT_KEY;
	public static String appid = DEFAULT_APPID;
	public static String body = DEFAULT_BODY;
	public static String mch_id = DEFAULT_MCH_ID;
	public static String nonce_str = "sdhouigvjasoignwap";//StringUtil.generateRandomString(10)
	public static String notify_url = DEFAULT_NOTIFY_URL;//http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/user/index.jsp
	public static String out_trade_no = "1450088509696vxvtw";
	public static String product_id = "1450088509696vxvtw";
	public static String spbill_create_ip = "127.0.0.1";
	public static String trade_type = DEFAULT_TRADE_TYPE;
	
	/**
	 * need to set up total_fee, body, spbill_create_ip, out_trade_no, product_id by self
	 * @return map with default key: appid, mch_id, nonce_str, notify_url, trade_type 
	 */
	public static Map<String, String> getDefaultMap(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("appid", appid);
		map.put("mch_id", mch_id);
		map.put("nonce_str", nonce_str);
		map.put("notify_url", notify_url);
		map.put("trade_type", trade_type);
		return map;
	}
	
	/**
	 * set up necessary keys after getDefaultMap() method;
	 * @param map
	 */
	public static void setOtherFields(Map<String, String> map, String totalFee, String body, String outTradeNo, String productId){
		map.put("total_fee", totalFee);
		map.put("body", body);
		map.put("spbill_create_ip", WeixinScanPayUtil.getLocalIp());
		map.put("out_trade_no", outTradeNo);
		map.put("product_id", productId);
	}
}

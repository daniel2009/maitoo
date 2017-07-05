package com.meitao.common.kuaidi;

import java.util.HashMap;
import java.util.Map;

import com.meitao.common.constants.Constant;
import com.meitao.common.kuaidi.impl.DcsKuaidi;
import com.meitao.common.kuaidi.impl.Kuaidi100;
import com.meitao.common.kuaidi.impl.MeitaoKuaidi;


public class KuaiDiUtil {
	public static Map<String, KuaiDi> kuaidi = new HashMap<String, KuaiDi>();

	static {
		// 初始化对应用户的快递查询
		KuaiDi dcs = new DcsKuaidi();
		KuaiDi meitao = new MeitaoKuaidi();
		KuaiDi kd100 = new Kuaidi100();
		kuaidi.put(Constant.KUAIDI_TYPE_DCS, dcs);
		kuaidi.put(Constant.KUAIDI_TYPE_MEITAO, meitao);
		kuaidi.put(Constant.KUAIDI_TYPE_KUAIDI100, kd100);
	}

	
	public static String SearchkuaiDiInfo(String type,String orderId, String ...args) throws Exception {
		
		// DCS快递
		if (Constant.KUAIDI_TYPE_DCS.equals(type)) {
			return kuaidi.get(Constant.KUAIDI_TYPE_DCS).SearchkuaiDiInfo(orderId, args);
			
		}
		// Meitao快递
		if (Constant.KUAIDI_TYPE_MEITAO.equals(type)) {
			return kuaidi.get(Constant.KUAIDI_TYPE_MEITAO).SearchkuaiDiInfo(orderId, args);
					
		}
		// Meitao快递
		if (Constant.KUAIDI_TYPE_KUAIDI100.equals(type)) {
			return kuaidi.get(Constant.KUAIDI_TYPE_KUAIDI100).SearchkuaiDiInfo("", args);
							
			}
		return null;

	}
	
	public static String extractValue(String str,String startStr,String endStr){
		int startIndex;
		if(str.indexOf(startStr)<0){
			return "";
		}else {
			startIndex = str.indexOf(startStr)+startStr.length();
		}
			
		if(str.indexOf(endStr,startIndex)<0){
			return "";
		}
		
		return str.substring(startIndex,str.indexOf(endStr,startIndex)).trim();
	}
    
  
}

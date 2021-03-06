package com.meitao.common.util;

import com.meitao.common.constants.Constant;
import com.meitao.model.AutoSend;
import com.meitao.model.Order;

public class AutoSendUtil {
	public static String getName(Object object){
		String name = "";
		if(object instanceof String){
			name = (String) object;
		}else if(object instanceof Order){
			/*switch(((Order) object).getState()){
				case Constant.ORDER_STATE1:
					name = Constant.AUTO_SEND_ORDER_STATE1;
					break;
				case Constant.ORDER_STATE2:
					name = Constant.AUTO_SEND_ORDER_STATE2;
					break;
				case Constant.ORDER_STATE3:
					name = Constant.AUTO_SEND_ORDER_STATE3;
					break;
				case Constant.ORDER_STATE4:
					name = Constant.AUTO_SEND_ORDER_STATE4;
					break;
				case Constant.ORDER_STATE5:
					name = Constant.AUTO_SEND_ORDER_STATE5;
					break;
				case Constant.ORDER_STATE6:
					name = Constant.AUTO_SEND_ORDER_STATE6;
					break;
				case Constant.ORDER_STATE7:
					name = Constant.AUTO_SEND_ORDER_STATE7;
					break;
				case Constant.ORDER_STATE8:
					name = Constant.AUTO_SEND_ORDER_STATE8;
					break;
				case Constant.ORDER_STATE9:
					name = Constant.AUTO_SEND_ORDER_STATE9;
					break;
				case Constant.ORDER_STATE10:
					name = Constant.AUTO_SEND_ORDER_STATE10;
					break;
			}*/
		}
		return name;
	}

	public static String setTemplate(String companyTitle, String countryCode, AutoSend autoSend) {
		StringBuilder result = new StringBuilder("");
		result.append("【").append(companyTitle).append("】");
		if(countryCode.equalsIgnoreCase("+1"))
		{
			result.append("Dear Customer, ").append(autoSend.getEnMessageContent()).append(". Please DO NOT reply this message.");
		}
		else if(countryCode.equalsIgnoreCase("+86"))
		{
			result.append("亲爱的用户，").append(autoSend.getMessageContent()).append("。如非本人操作，请忽略本短信");
		}
		
		/*switch(countryCode){
			case "+1":
				result.append("Dear Customer, ").append(autoSend.getEnMessageContent()).append(". Please DO NOT reply this message.");
				break;
			case "+86":
				result.append("亲爱的用户，").append(autoSend.getMessageContent()).append("。如非本人操作，请忽略本短信");
				break;
		}*/
		return result.toString();
	}
	
	public static void setOrderInfo(AutoSend autoSend, Order order){
		autoSend.setEmailContent(setOrderInfo(autoSend.getEmailContent(), order));
		autoSend.setMessageContent(setOrderInfo(autoSend.getMessageContent(), order));
		autoSend.setEnMessageContent(setOrderInfo(autoSend.getEnMessageContent(), order));
	}
	public static void setCode(AutoSend autoSend, String code){
		autoSend.setEmailContent(replace(autoSend.getEmailContent(), "#code#", code));
		autoSend.setMessageContent(replace(autoSend.getMessageContent(), "#code#", code));
		autoSend.setEnMessageContent(replace(autoSend.getEnMessageContent(), "#code#", code));
	}
	
	public static String setOrderInfo(String content, Order order){
		content = replace(content, "#order#", order.getOrderId());
		content = replace(content, "#state#", order.getState());
		return content;
	}
	
	public static String replace(String content, String type, String code){
		if(StringUtil.isEmpty(content)){
			content = "";
		}else{
			content = content.replace(type, code);
		}
		return content;
	}
}

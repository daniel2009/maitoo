package com.meitao.common.util.stripe;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.meitao.common.constants.Constant;
import com.meitao.common.util.PropertiesReader;

public class StripeUtil {

    public static Charge createPayMoneyByStrip(String name,String creditNo,String securityCode,String expireYear,String expireMonth,int amount,String currency,String order) throws Exception {
    	try {
			Properties props = PropertiesReader.read(Constant.SYSTEM_PROPERTIES_FILE);
			Stripe.apiKey = props.getProperty("stripe.apikey");
		} catch (Exception e) {
			//Stripe.apiKey = "1a34d5adddbaf841";
			throw e; 
		}
		Map<String, Object> chargeMap = new HashMap<String, Object>();
        chargeMap.put("amount", amount);

        chargeMap.put("currency", currency);
        Map<String, Object> cardMap = new HashMap<String, Object>();
        cardMap.put("number", creditNo);
        cardMap.put("exp_month",expireMonth);
        cardMap.put("exp_year", expireYear);
        cardMap.put("cvc", securityCode);
        cardMap.put("name", name);
        chargeMap.put("card", cardMap);
        
        Map<String, String> initialMetadata = new HashMap<String, String>();
        initialMetadata.put("order_id", order);
        initialMetadata.put("type", order);
        
        chargeMap.put("metadata", initialMetadata);
        Charge charge = Charge.create(chargeMap);
		return charge;
    }
}

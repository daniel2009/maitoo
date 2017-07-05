package com.meitao.common.util;

import com.meitao.model.Cart;
import com.meitao.model.ProductRecord;
import com.meitao.model.TranshipmentBill;

public class ProductUtil {
	public static void transfer(Cart cart, ProductRecord productRecord){
		productRecord.setUserId(cart.getUserId());
		productRecord.setProductId(cart.getProductId());
		productRecord.setQuantity(cart.getQuantity());
	}
	public static ProductRecord transfer(Cart cart){
		ProductRecord productRecord = new ProductRecord();
		transfer(cart, productRecord);
		return productRecord;
	}
	public static void transfer2TranshipmentBill(ProductRecord productRecord, TranshipmentBill transhipmentBill) {
		transhipmentBill.setcName(productRecord.getcName());
		transhipmentBill.setcPhone(productRecord.getcPhone());
		transhipmentBill.setcStreetAddress(productRecord.getcAddress());
		transhipmentBill.setcZipCode(productRecord.getcZipCode());
		transhipmentBill.setCardid(productRecord.getcCardId());
		transhipmentBill.setCardurl(productRecord.getcIdCardPic());
		transhipmentBill.setCardurlother(productRecord.getcIdCardOtherPic());
		transhipmentBill.setCardurltogether(productRecord.getcIdCardTogetherPic());
	}
}

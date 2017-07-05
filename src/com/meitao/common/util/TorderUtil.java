package com.meitao.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jxl.Sheet;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;

import com.meitao.common.constants.Constant;
import com.meitao.common.util.excel.ExcelReader;
import com.meitao.common.util.excel.ExcelUtil;
import com.meitao.common.util.excel.ExcelWrite;
import com.meitao.model.ConsigneeInfo;
import com.meitao.model.M_order;
import com.meitao.model.Order;
import com.meitao.model.OrderDetail;
import com.meitao.model.Receive_User;
import com.meitao.model.Send_User;
import com.meitao.model.SumCommodity;
import com.meitao.model.User;
import com.meitao.model.temp.ExportOrder;
import com.meitao.model.temp.ImportOrder;
import com.meitao.model.temp.ImportthirdOrder;


public class TorderUtil {
	
	/**
	 * @param type
	 *            0表示从数字转换成为字符串，1表示从字符串转换为数字
	 * @param value
	 * @return
	 */
	public static String transformerState(int type, String value) {
		if (type == 0) {
			// 转换为字符串
			if (Constant.T_ORDER_STATE0.equals(value)) {
				return Constant.T_ORDER_ROUTE_STATE0;
			}
			if (Constant.T_ORDER_STATE1.equals(value)) {
				return Constant.T_ORDER_ROUTE_STATE1;
			}
			if (Constant.T_ORDER_STATE2.equals(value)) {
				return Constant.T_ORDER_ROUTE_STATE2;
			}
			if (Constant.T_ORDER_STATE3.equals(value)) {
				return Constant.T_ORDER_ROUTE_STATE3;
			}
			if (Constant.T_ORDER_STATE4.equals(value)) {
				return Constant.T_ORDER_ROUTE_STATE4;
			}
			if (Constant.T_ORDER_STATE5.equals(value)) {
				return Constant.T_ORDER_ROUTE_STATE5;
			}
			if (Constant.T_ORDER_STATE6.equals(value)) {
				return Constant.T_ORDER_ROUTE_STATE6;
			}
			if (Constant.T_ORDER_STATE7.equals(value)) {
				return Constant.T_ORDER_ROUTE_STATE7;
			}
			
		} else if (type == 1) {
			
			
			
			// 转换为数字
			if (Constant.T_ORDER_ROUTE_STATE0.equals(value)) {
				return Constant.T_ORDER_STATE0;
			}
			if (Constant.T_ORDER_ROUTE_STATE1.equals(value)) {
				return Constant.T_ORDER_STATE1;
			}
			if (Constant.T_ORDER_ROUTE_STATE2.equals(value)) {
				return Constant.T_ORDER_STATE2;
			}
			if (Constant.T_ORDER_ROUTE_STATE3.equals(value)) {
				return Constant.T_ORDER_STATE3;
			}
			if (Constant.T_ORDER_ROUTE_STATE4.equals(value)) {
				return Constant.T_ORDER_STATE4;
			}
			if (Constant.T_ORDER_ROUTE_STATE5.equals(value)) {
				return Constant.T_ORDER_STATE5;
			}
			if (Constant.T_ORDER_ROUTE_STATE6.equals(value)) {
				return Constant.T_ORDER_STATE6;
			}
			if (Constant.T_ORDER_ROUTE_STATE7.equals(value)) {
				return Constant.T_ORDER_STATE7;
			}
		}
		
		
		
		
		
		return value;
	}
	
	
	
	public static M_order cusertoruserofmorder(ConsigneeInfo user, M_order morder)
	{
		if(morder==null||user==null)
		{
			return null;
		}
		Receive_User ruser=new Receive_User();
		ruser.setAddress(user.getStreetAddress());
		ruser.setCardid(user.getCardId());
		ruser.setCardother(user.getCardurlother());
		ruser.setCardtogether(user.getCardurltogether());
		ruser.setCardurl(user.getCardUrl());
		ruser.setCity(user.getCity());
		ruser.setDist(user.getDistrict());
		ruser.setName(user.getName());
		ruser.setPhone(user.getPhone());
		ruser.setState(user.getProvince());
		ruser.setZipcode(user.getZipCode());
		morder.setRuser(ruser);
		return morder;
	}
	
	public static M_order usertoruserofmorder(User user, M_order morder)
	{
		if(morder==null||user==null)
		{
			return null;
		}
		Send_User suser=new Send_User();
		suser.setAddress(user.getAddress());
		suser.setEmail(user.getEmail());
		suser.setName(user.getRealName());
		suser.setPhone(user.getPhone());
		
		
		morder.setSuser(suser);
		return morder;
	}
}

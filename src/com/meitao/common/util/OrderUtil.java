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
import com.meitao.model.Order;
import com.meitao.model.OrderDetail;
import com.meitao.model.SumCommodity;
import com.meitao.model.User;
import com.meitao.model.temp.ExportOrder;
import com.meitao.model.temp.ImportOrder;
import com.meitao.model.temp.ImportthirdOrder;


public class OrderUtil {
	
	public static void fileChannelCopy(File s, File t) throws Exception{
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        fi = new FileInputStream(s);
        fo = new FileOutputStream(t);
        in = fi.getChannel();//得到对应的文件通道
        out = fo.getChannel();//得到对应的文件通道
        in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
        fi.close();
        in.close();
        fo.close();
        out.close();
    }

	public static boolean validateUserId(String id) {
		return !StringUtil.isEmpty(id);
	}

	public static boolean validateConsignee(Order order) {
		if (StringUtil.isEmpty(order.getConsigneeId())) {
			boolean isEmpty = StringUtil.isEmpty(order.getcName()) || StringUtil.isEmpty(order.getcStreetAddress())
					 || StringUtil.isEmpty(order.getcPhone());
			        //|| StringUtil.isEmpty(order.getcZipCode()) || StringUtil.isEmpty(order.getcPhone());
			
			return !isEmpty;
		}
		return true;
	}

	public static boolean validateOrderCommodity(Order order) {
		return true;
	}

	public static boolean validateMsjOrder(Order order) {
		boolean result = true;
		if (result) {
			// validate 寄件人
			result = validateUserId(order.getUserId());
		}
		if (result) {
			// validate 收件人
			result = validateConsignee(order);
		}

		if (result) {
			// 验证商品数量
			result = validateOrderCommodity(order);
		}

		return result;
	}

	public static boolean validateOnlinejOrder(Order order) {
		boolean result = true;
		if (result) {
			// validate 寄件人
			result = validateUserId(order.getUserId());
		}
		if (result) {
			// validate 收件人
			result = validateConsignee(order);
		}

		if (result) {
			// 验证商品数量
			result = validateOrderCommodity(order);
		}

		return result;
	}
	
	public static boolean validateModifyOrder(Order order) {
		return true;
	}

	private static boolean ini = false;
	private static String prefix = "WY";
	private static String lastfix = "US";
	private static int numbercount = 8;
	/**
	 * 读配置文件
	 */
	public static void readConfig() {
		if(!ini){
			Properties prop = PropertiesReader.read(Constant.SYSTEM_PROPERTIES_FILE);
			prefix = prop.getProperty("order.prefix");
			lastfix = prop.getProperty("order.lastfix");
			numbercount = StringUtil.string2Integer(prop.getProperty("order.mid.num.count"));
			if(numbercount<5||numbercount>30){
				numbercount = 8;
			}
			ini=true;
		}
	}

	/**  
	 * 根据运单记录的主键和前缀产生运单号<br/>
	 * 运单号规则是：MTS YY 0000 1<br/>
	 * 分别组成的是：前缀 年 递增数字 随机数字
	 * 
	 * @param id
	 *            运单记录主键id
	 * @param prefix
	 *            前缀
	 * @return
	 */
	public static String createOrderId(String id, String orderprefix) {
		StringBuffer sb = new StringBuffer(10);
		/*
		// 添加前缀和时间缩写
		sb.append(prefix).append(DateUtil.date2String(new Date(), "yy"));
		// 添加递增数字，4位
		sb.append(StringUtil.getTruncationString(id, 5));
		// 添加随机数字，1位
		sb.append(StringUtil.generateRandomInteger(1));
		*/
		
		//update by chenkanghua 
		readConfig();
		sb.append(prefix);
		
		// 添加前缀和时间缩写
		sb.append(DateUtil.date2String(new Date(), "yy"));
		// 添加递增数字，4位
		sb.append(StringUtil.getTruncationString(id, numbercount-3));
		// 添加随机数字，1位
		sb.append(StringUtil.generateRandomInteger(1));
		
		sb.append(lastfix);

		return sb.toString();
	}

	public static String getSearchColumnByType(String type) {
		if ("0".equals(type)) {
			return "`order`.order_id";
		} else if ("1".equals(type)) {
			return "tb.transit_no";
		} else if ("2".equals(type)) {
			return "u.nick_name";
		} else if ("3".equals(type)) {
			return "`order`.consignee_name";
		}
		return null;
	}

	public static String dealState(String state) {
		try {
			int i = Integer.valueOf(state);
			if ((i >= 0 && i <= 9)|| (i == -10)|| (i == -9)) {
				return state;
			}
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 设置订单地址
	 * 
	 * @param order
	 * @param info
	 */
	public static void setOrderConsignee(Order order, ConsigneeInfo info) {
		order.setConsigneeId(info.getId());
		order.setcCity(info.getCity());
		order.setcDistrict(info.getDistrict());
		order.setcName(info.getName());
		order.setcPhone(info.getPhone());
		order.setcProvince(info.getProvince());
		order.setcStreetAddress(info.getStreetAddress());
		order.setcZipCode(info.getZipCode());
		
		order.setCardid(info.getCardId());//kai
		order.setCardurl(info.getCardUrl());
		order.setCardurlother(info.getCardurlother());
		order.setCardurltogether(info.getCardurltogether());
	}

	/**
	 * @param type
	 *            0表示从数字转换成为字符串，1表示从字符串转换为数字
	 * @param value
	 * @return
	 */
	public static String transformerState(int type, String value) {
		if (type == 0) {
			// kai 20150911-1
			if (Constant.ORDER_STATE__10.equals(value)) {
				return Constant.ORDER_ROUTE__10;
			}
			if (Constant.ORDER_STATE__9.equals(value)) {
				return Constant.ORDER_ROUTE__9;
			}
						
			// 转换为字符串
			if (Constant.ORDER_STATE0.equals(value)) {
				return Constant.ORDER_ROUTE_STATE0;
			}
			if (Constant.ORDER_STATE1.equals(value)) {
				return Constant.ORDER_ROUTE_STATE1;
			}
			if (Constant.ORDER_STATE2.equals(value)) {
				return Constant.ORDER_ROUTE_STATE2;
			}
			if (Constant.ORDER_STATE3.equals(value)) {
				return Constant.ORDER_ROUTE_STATE3;
			}
			if (Constant.ORDER_STATE4.equals(value)) {
				return Constant.ORDER_ROUTE_STATE4;
			}
			if (Constant.ORDER_STATE5.equals(value)) {
				return Constant.ORDER_ROUTE_STATE5;
			}
			if (Constant.ORDER_STATE6.equals(value)) {
				return Constant.ORDER_ROUTE_STATE6;
			}
			if (Constant.ORDER_STATE7.equals(value)) {
				return Constant.ORDER_ROUTE_STATE7;
			}
			if (Constant.ORDER_STATE8.equals(value)) {
				return Constant.ORDER_ROUTE_STATE8;
			}
			if (Constant.ORDER_STATE9.equals(value)) {
				return Constant.ORDER_ROUTE_STATE9;
			}
			if (Constant.ORDER_STATE10.equals(value)) {
				return Constant.ORDER_ROUTE_STATE10;
			}
		} else if (type == 1) {
			
			// kai 20150911-1
			if (Constant.ORDER_ROUTE__10.equals(value)) {
				return Constant.ORDER_STATE__10;
			}

			if (Constant.ORDER_ROUTE__9.equals(value)) {
				return Constant.ORDER_STATE__9;
			}	
						
						
					
			// 转换为数字
			if (Constant.ORDER_ROUTE_STATE1.equals(value)) {
				return Constant.ORDER_STATE1;
			}
			if (Constant.ORDER_ROUTE_STATE2.equals(value)) {
				return Constant.ORDER_STATE2;
			}
			if (Constant.ORDER_ROUTE_STATE3.equals(value)) {
				return Constant.ORDER_STATE3;
			}
			if (Constant.ORDER_ROUTE_STATE4.equals(value)) {
				return Constant.ORDER_STATE4;
			}
			if (Constant.ORDER_ROUTE_STATE5.equals(value)) {
				return Constant.ORDER_STATE5;
			}
			if (Constant.ORDER_ROUTE_STATE6.equals(value)) {
				return Constant.ORDER_STATE6;
			}
			if (Constant.ORDER_ROUTE_STATE7.equals(value)) {
				return Constant.ORDER_STATE7;
			}
			if (Constant.ORDER_ROUTE_STATE8.equals(value)) {
				return Constant.ORDER_STATE8;
			}
			if (Constant.ORDER_ROUTE_STATE9.equals(value)) {
				return Constant.ORDER_STATE9;
			}
			if (Constant.ORDER_ROUTE_STATE10.equals(value)) {
				return Constant.ORDER_STATE10;
			}
		}
		else if (type == 2) {
	
			
			// 转换为英文字符串
			
			if (Constant.ORDER_STATE1.equals(value)) {
				return Constant.ORDER_ROUTE_STATE1_en;
			}
			if (Constant.ORDER_STATE2.equals(value)) {
				return Constant.ORDER_ROUTE_STATE2_en;
			}
			if (Constant.ORDER_STATE3.equals(value)) {
				return Constant.ORDER_ROUTE_STATE3_en;
			}
			if (Constant.ORDER_STATE4.equals(value)) {
				return Constant.ORDER_ROUTE_STATE4_en;
			}
			if (Constant.ORDER_STATE5.equals(value)) {
				return Constant.ORDER_ROUTE_STATE5_en;
			}
			if (Constant.ORDER_STATE6.equals(value)) {
				return Constant.ORDER_ROUTE_STATE6_en;
			}
			if (Constant.ORDER_STATE7.equals(value)) {
				return Constant.ORDER_ROUTE_STATE7_en;
			}
			if (Constant.ORDER_STATE8.equals(value)) {
				return Constant.ORDER_ROUTE_STATE8_en;
			}
			if (Constant.ORDER_STATE9.equals(value)) {
				return Constant.ORDER_ROUTE_STATE9_en;
			}
			if (Constant.ORDER_STATE10.equals(value)) {
				return Constant.ORDER_ROUTE_STATE10_en;
			}
		} 
		
		
		
		
		return value;
	}

	public static OrderDetail[] analyticalOrderDetail(String commodifys) {
		if (StringUtil.isEmpty(commodifys)) {
			return new OrderDetail[0];
		}

		List<OrderDetail> list = new ArrayList<OrderDetail>();
		String[] values = commodifys.split("(;|；)");
		for (String value : values) {
			String[] arr = value.split("(:|：)");

			OrderDetail detail = new OrderDetail();
			detail.setCtype(StringUtil.getValue(arr[0], ""));
			detail.setQuantity(String.valueOf(StringUtil.string2Double(arr[1])));
			list.add(detail);
		}
		return list.toArray(new OrderDetail[0]);
	}
//kai 20151028 添加详情的录入
	public static OrderDetail[] analyticalOrderDetail_weiyi(String commodifys,
			int row, int col) {
		if (StringUtil.isEmpty(commodifys)) {
			return new OrderDetail[0];
		}
		try {
			List<OrderDetail> list = new ArrayList<OrderDetail>();
			String[] values = commodifys.split(";");
			for (String value : values) {
				String[] arr = value.split("\\*");

				OrderDetail detail = new OrderDetail();
				if(arr[0].equalsIgnoreCase(""))
				{
					String str0 = "(物品名称)第" + row + "行," + "第" + col + "列格式不对！";
					throw new RuntimeException(str0);
				}
				else
				{
					detail.setName(StringUtil.getValue(arr[0], ""));
				}
				detail.setXiangqing(String.valueOf(StringUtil.getValue(arr[1], "")));

				detail.setQuantity(String.valueOf(StringUtil
						.string2Double(arr[2])));

				list.add(detail);
			}
			return list.toArray(new OrderDetail[0]);
		} catch (Exception e) {
			String str0 = "(物品名称)第" + row + "行," + "第" + col + "列格式不对！";
			throw new RuntimeException(str0);
		}
	}

	public static void exportEmptyOrderToExcel(List<Map<String, Object>> list, File templetFile, OutputStream os,
	        final String message) throws Exception {
		if (templetFile != null && templetFile.exists()) {
			ExcelUtil.exportExcel(new ExcelWrite<Map<String, Object>>() {
				@Override
				public void write(Map<String, String> headers, Collection<Map<String, Object>> datas,
				        WritableSheet sheet) throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 2, 1);
						Label cell = new Label(0, 1, message);
						sheet.addCell(cell);
						return;
					}

					int row = 1;
					for (Map<String, Object> map : datas) {
						int columnIndex = 0;
						sheet.addCell(new Label(columnIndex++, row, StringUtil.obj2String(map.get("id"))));
						sheet.addCell(new Label(columnIndex++, row, StringUtil.obj2String(map.get("orderId"))));
						row++;
					}
				}
			}, templetFile, list, os);
		} else {
			ExcelUtil.exportExcel(new ExcelWrite<Map<String, Object>>() {
				@Override
				public void write(Map<String, String> headers, Collection<Map<String, Object>> datas,
				        WritableSheet sheet) throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 2, 1);
						Label cell = new Label(0, 1, message);
						sheet.addCell(cell);
						return;
					}

					int row = 1;
					for (Map<String, Object> map : datas) {
						int columnIndex = 0;
						sheet.addCell(new Label(columnIndex++, row, StringUtil.obj2String(map.get("id"))));
						sheet.addCell(new Label(columnIndex++, row, StringUtil.obj2String(map.get("orderId"))));
						row++;
					}
				}
			}, "empty_order", null, list, os);
		}
	}

	/**
	 * 读取要导入的空运单信息，第一行是行头，是不读取的。<br/>
	 * 从第二行开始读。
	 * 
	 * @param input
	 *            要读入的运单二进制excel流，其中第一行是标题行。内容是从第二行开始的
	 * @return
	 * @throws Exception
	 */
	public static List<Order> readEmptyOrderExcel(InputStream input) throws Exception {
		return ExcelUtil.importExcel(new ExcelReader<Order>() {
			@Override
			public List<Order> read(Sheet sheet) throws Exception {
				List<Order> list = new ArrayList<Order>();
				int rows = sheet.getRows();
				if (rows < 2) {
					throw new RuntimeException("运单文件不能为空！");
				}

				for (int i = 1; i < rows; i++) {
					int j = 0;
					String orderId = StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""); // 获取运单号
					String commodifys = StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""); // 获取商品信息
					String userId = StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""); // 获取用户id
					String userName = StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""); // 获取用户名
					String cName = StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""); // 获取收件人姓名
					String cPhone = StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""); // 获取收件人联系方式
					String cProvince = StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""); // 获取收件人的省份
					String cCity = StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""); // 获取收件人市
					String cDistrict = StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""); // 获取收件人区
					String cAddress = StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""); // 获取详细地址
					String cZipCode = StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""); // 获取收件人邮编
					String parceValue = StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""); // 获取商品价值
					String weight = StringUtil.getValue(sheet.getCell(j++, i).getContents(), "0"); // 获取包裹重量
					String premium = StringUtil.getValue(sheet.getCell(j++, i).getContents(), "0"); // 获取保险金额
					String other = StringUtil.getValue(sheet.getCell(j++, i).getContents(), "0"); // 获取其他金额
					String tariff = StringUtil.getValue(sheet.getCell(j++, i).getContents(), "0"); // 获取关税
					String totalMoney = StringUtil.getValue(sheet.getCell(j++, i).getContents(), "0"); // 获取运费

					Order order = new Order();
					order.setOrderId(orderId);
					order.setUserId(userId);
					order.setUserName(userName);
					order.setcName(cName);
					order.setcPhone(cPhone);
					order.setcProvince(cProvince);
					order.setcCity(cCity);
					order.setcDistrict(cDistrict);
					order.setcStreetAddress(cAddress);
					order.setcZipCode(cZipCode);
					order.setWeight(weight);
					order.setPremium(premium);
					order.setOr("0");
					order.setParceValue(parceValue);
					order.setOther(other);
					order.setTariff(tariff);
					order.setTotalMoney(totalMoney);
					order.setRemark(commodifys);
					order.setDetails(analyticalOrderDetail(commodifys));

					list.add(order);
				}
				return list;
			}
		}, input);
	}

	public static void exportOrderToExcel(List<ExportOrder> orders, File templetFile, OutputStream os) throws Exception {
		if (templetFile != null && templetFile.exists()) {
			// 模板复制
			ExcelUtil.exportExcel(new ExcelWrite<ExportOrder>() {
				@Override
				public void write(Map<String, String> headers, Collection<ExportOrder> datas, WritableSheet sheet)
				        throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有用户数据.....");
						sheet.addCell(cell);
						return;
					}

					List<String> tmp = new ArrayList<String>();
					int row = 1;
					for (Order order : datas) {
						int columnIndex = 0;
						// sheet.addCell(new Label(columnIndex++, row,
						// order.getId()));// 是用记录编号
						sheet.addCell(new Label(columnIndex++, row, String.valueOf(row)));// 编号，也就是一个序号，数据库中不存
						sheet.addCell(new Label(columnIndex++, row, order.getOrderId())); // 运单号
						tmp = order.getOrderDetailTranshipmentIds();
						if (tmp != null && tmp.size() > 0) {
							sheet.addCell(new Label(columnIndex++, row, Arrays.toString(tmp.toArray())));
						} else {
							sheet.addCell(new Label(columnIndex++, row, ""));
						}
						// 开始到处商品信息
						OrderDetail[] details = order.getDetails();
						StringBuffer sb = new StringBuffer();
						if (details != null)
							for (OrderDetail detail : details) {
								sb.append(detail.getCtype()).append(":").append(detail.getQuantity()).append(";");
							}
						sheet.addCell(new Label(columnIndex++, row, sb.toString()));
						sheet.addCell(new Label(columnIndex++, row, order.getUserId()));
						sheet.addCell(new Label(columnIndex++, row, order.getUser().getRealName()));
						sheet.addCell(new Label(columnIndex++, row, order.getUser().getPhone()));
						sheet.addCell(new Label(columnIndex++, row, order.getcName()));
						sheet.addCell(new Label(columnIndex++, row, order.getcPhone()));
						sheet.addCell(new Label(columnIndex++, row, order.getAddress()));
						sheet.addCell(new Label(columnIndex++, row, order.getParceValue())); // 商品价值
						sheet.addCell(new Label(columnIndex++, row, order.getOr())); // 是否or
						sheet.addCell(new Label(columnIndex++, row, order.getWeight()));
						sheet.addCell(new Label(columnIndex++, row, order.getPremium())); // 是否保险
						sheet.addCell(new Label(columnIndex++, row, order.getOther()));
						sheet.addCell(new Label(columnIndex++, row, order.getTariff()));
						//update by chenkanghua 为什么不直接写订单的总金额
						//sheet.addCell(new jxl.write.Number(columnIndex++, row, OrderFreightUtil
						//        .calculationOrderFreight(order, order.getUser().getType())));
						sheet.addCell(new Label(columnIndex++, row, order.getTotalMoney()));
						sheet.addCell(new Label(columnIndex++, row, order.getCreateDate()));
						sheet.addCell(new Label(columnIndex++, row, OrderUtil.transformerState(0, order.getState())));
						sheet.addCell(new Label(columnIndex++, row, order.getFlight()));
						row++;
					}
					tmp.clear();
				}
			}, templetFile, orders, os);
		} else {
			// 重新写
			ExcelUtil.exportExcel(new ExcelWrite<ExportOrder>() {
				@Override
				public void write(Map<String, String> headers, Collection<ExportOrder> datas, WritableSheet sheet)
				        throws Exception {
					List<String> tmp = new ArrayList<String>();
					int row = 1;
					for (Order order : datas) {
						int columnIndex = 0;
						sheet.addCell(new Label(columnIndex++, row, String.valueOf(row)));// 编号，也就是一个序号，数据库中不存
						sheet.addCell(new Label(columnIndex++, row, order.getOrderId())); // 运单号
						tmp = order.getOrderDetailTranshipmentIds();
						if (tmp != null) {
							sheet.addCell(new Label(columnIndex++, row, Arrays.toString(tmp.toArray())));
						} else {
							sheet.addCell(new Label(columnIndex++, row, ""));
						}
						sheet.addCell(new Label(columnIndex++, row, order.getUserId()));
						sheet.addCell(new Label(columnIndex++, row, order.getUser().getRealName()));
						sheet.addCell(new Label(columnIndex++, row, order.getUser().getPhone()));
						sheet.addCell(new Label(columnIndex++, row, order.getcName()));
						sheet.addCell(new Label(columnIndex++, row, order.getcPhone()));
						sheet.addCell(new Label(columnIndex++, row, order.getAddress()));
						sheet.addCell(new Label(columnIndex++, row, order.getParceValue())); // 商品价值
						sheet.addCell(new Label(columnIndex++, row, order.getOr())); // 是否or
						sheet.addCell(new Label(columnIndex++, row, order.getWeight()));
						sheet.addCell(new Label(columnIndex++, row, order.getPremium())); // 是否保险
						sheet.addCell(new Label(columnIndex++, row, order.getPremium()));
						//update by chenkanghua 为什么不直接写订单的总金额
						//sheet.addCell(new jxl.write.Number(columnIndex++, row, OrderFreightUtil
						//        .calculationOrderFreight(order, order.getUser().getType())));
						sheet.addCell(new Label(columnIndex++, row, order.getCreateDate()));
						sheet.addCell(new Label(columnIndex++, row, OrderUtil.transformerState(0, order.getState())));
						sheet.addCell(new Label(columnIndex++, row, order.getTariff()));
						sheet.addCell(new Label(columnIndex++, row, ""));
						row++;
					}
					tmp.clear();
				}
			}, "order-datas", null, orders, os);
		}
	}
	
	//meitao
	public static void exportOrderToMeitaoExcel(List<ExportOrder> orders, File templetFile, OutputStream os) throws Exception {
		if (templetFile != null && templetFile.exists()) {
			// 模板复制
			ExcelUtil.exportExcel(new ExcelWrite<ExportOrder>() {
				@Override
				public void write(Map<String, String> headers, Collection<ExportOrder> datas, WritableSheet sheet)
				        throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有用户数据.....");
						sheet.addCell(cell);
						return;
					}

					int row = 1;
					for (Order order : datas) {
						int columnIndex = 0;
						// sheet.addCell(new Label(columnIndex++, row,
						// order.getId()));// 是用记录编号
						
						sheet.addCell(new Label(columnIndex++, row,""));// 入仓号  
						sheet.addCell(new Label(columnIndex++, row,order.getUser().getNickName()));// 箱号  
						sheet.addCell(new Label(columnIndex++, row,order.getUser().getPhone()));// 卡板号   
						sheet.addCell(new Label(columnIndex++, row, order.getOrderId())); // 运单号
						sheet.addCell(new Label(columnIndex++, row,""));// 英文品牌
						/*tmp = order.getOrderDetailTranshipmentIds();
						if (tmp != null && tmp.size() > 0) {
							sheet.addCell(new Label(columnIndex++, row, Arrays.toString(tmp.toArray())));
						} else {
							sheet.addCell(new Label(columnIndex++, row, ""));
						}*/
						
						// 开始到处商品信息
						OrderDetail[] details = order.getDetails();
						StringBuffer sb = new StringBuffer();
						StringBuffer xq = new StringBuffer();
						int total = 0;
						if (details != null)
							for (OrderDetail detail : details) {
								sb.append(detail.getCtype()).append(":").append(detail.getQuantity()).append("件;");
								xq.append(detail.getXiangqing()).append(";");
								total = total + Integer.parseInt(detail.getQuantity());
							}
						sheet.addCell(new Label(columnIndex++, row, sb.toString()));//中文货物名称
						sheet.addCell(new Label(columnIndex++, row, String.valueOf(total)));//数量
						sheet.addCell(new Label(columnIndex++, row, order.getWeight()));//重量
						sheet.addCell(new Label(columnIndex++, row, xq.toString()));//商品详情
						sheet.addCell(new Label(columnIndex++, row, order.getcName()));//收件人姓名
						sheet.addCell(new Label(columnIndex++, row, order.getcPhone()));//收件人电话

						sheet.addCell(new Label(columnIndex++, row, order.getcProvince()+order.getcCity()+order.getcDistrict()+order.getcStreetAddress()));//收件人地址
						sheet.addCell(new Label(columnIndex++, row, order.getcCity()));//收件人城市

						row++;
					}
				}
			}, templetFile, orders, os);
		} else {
			// 重新写
			ExcelUtil.exportExcel(new ExcelWrite<ExportOrder>() {
				@Override
				public void write(Map<String, String> headers, Collection<ExportOrder> datas, WritableSheet sheet)
				        throws Exception {
					
					int row = 1;
					for (Order order : datas) {
						int columnIndex = 0;
						// sheet.addCell(new Label(columnIndex++, row,
						// order.getId()));// 是用记录编号
						sheet.addCell(new Label(columnIndex++, row,""));// 入仓号  
						sheet.addCell(new Label(columnIndex++, row,""));// 箱号   
						sheet.addCell(new Label(columnIndex++, row,""));// 卡板号   
						sheet.addCell(new Label(columnIndex++, row, order.getOrderId())); // 运单号
						sheet.addCell(new Label(columnIndex++, row,""));// 英文品牌
						/*tmp = order.getOrderDetailTranshipmentIds();
						if (tmp != null && tmp.size() > 0) {
							sheet.addCell(new Label(columnIndex++, row, Arrays.toString(tmp.toArray())));
						} else {
							sheet.addCell(new Label(columnIndex++, row, ""));
						}*/
						
						// 开始到处商品信息
						OrderDetail[] details = order.getDetails();
						StringBuffer sb = new StringBuffer();
						if (details != null)
							for (OrderDetail detail : details) {
								sb.append(detail.getCtype()).append(":").append(detail.getQuantity()).append(";");
							}
						sheet.addCell(new Label(columnIndex++, row, sb.toString()));//中文货物名称
						sheet.addCell(new Label(columnIndex++, row, order.getQuantity()));//数量
						sheet.addCell(new Label(columnIndex++, row, order.getWeight()));//重量

						sheet.addCell(new Label(columnIndex++, row, order.getcName()));//收件人姓名
						sheet.addCell(new Label(columnIndex++, row, order.getcPhone()));//收件人电话

						sheet.addCell(new Label(columnIndex++, row, order.getcProvince()+order.getcCity()+order.getcDistrict()+order.getcStreetAddress()));//收件人地址
						sheet.addCell(new Label(columnIndex++, row, order.getcCity()));//收件人城市

						row++;
					}
				}
			}, "order-datas", null, orders, os);
		}
	}
	
	//dcs
	public static void exportOrderToDcsExcel(List<ExportOrder> orders, File templetFile, OutputStream os) throws Exception {
		if (templetFile != null && templetFile.exists()) {
			// 模板复制
			ExcelUtil.exportExcel(new ExcelWrite<ExportOrder>() {
				@Override
				public void write(Map<String, String> headers, Collection<ExportOrder> datas, WritableSheet sheet)
				        throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有用户数据.....");
						sheet.addCell(cell);
						return;
					}

					int row = 1;
					for (Order order : datas) {
						int columnIndex = 0;
						// sheet.addCell(new Label(columnIndex++, row,
						// order.getId()));// 是用记录编号
						
						sheet.addCell(new Label(columnIndex++, row,"中洋快递"));// 入仓号  -->改为快递公司名
						sheet.addCell(new Label(columnIndex++, row,order.getUser().getRealName()));// 箱号   -->改为发件人
						sheet.addCell(new Label(columnIndex++, row,order.getUser().getPhone()));// 卡板号   -->改为发件人电话
						sheet.addCell(new Label(columnIndex++, row, order.getOrderId())); // 运单号
						sheet.addCell(new Label(columnIndex++, row,""));// 英文品牌
						/*tmp = order.getOrderDetailTranshipmentIds();
						if (tmp != null && tmp.size() > 0) {
							sheet.addCell(new Label(columnIndex++, row, Arrays.toString(tmp.toArray())));
						} else {
							sheet.addCell(new Label(columnIndex++, row, ""));
						}*/
						
						// 开始到处商品信息
						OrderDetail[] details = order.getDetails();
						StringBuffer sb = new StringBuffer();
						StringBuffer xq = new StringBuffer();
						int total = 0;
						if (details != null)
							for (OrderDetail detail : details) {
								sb.append(detail.getCtype()).append(":").append(detail.getQuantity()).append("件;");
								xq.append(detail.getXiangqing()).append(";");
								total = total + Integer.parseInt(detail.getQuantity());
							}
						sheet.addCell(new Label(columnIndex++, row, sb.toString()));//中文货物名称
						sheet.addCell(new Label(columnIndex++, row, String.valueOf(total)));//数量
						sheet.addCell(new Label(columnIndex++, row, order.getWeight()));//重量
						sheet.addCell(new Label(columnIndex++, row, xq.toString()));//商品详情
						sheet.addCell(new Label(columnIndex++, row, order.getcName()));//收件人姓名
						sheet.addCell(new Label(columnIndex++, row, order.getcPhone()));//收件人电话

						sheet.addCell(new Label(columnIndex++, row, order.getcProvince()+order.getcCity()+order.getcDistrict()+order.getcStreetAddress()));//收件人地址
						sheet.addCell(new Label(columnIndex++, row, order.getcCity()));//收件人城市

						row++;
					}
				}
			}, templetFile, orders, os);
		} else {
			// 重新写
			ExcelUtil.exportExcel(new ExcelWrite<ExportOrder>() {
				@Override
				public void write(Map<String, String> headers, Collection<ExportOrder> datas, WritableSheet sheet)
				        throws Exception {
					
					int row = 1;
					for (Order order : datas) {
						int columnIndex = 0;
						// sheet.addCell(new Label(columnIndex++, row,
						// order.getId()));// 是用记录编号
						sheet.addCell(new Label(columnIndex++, row,"中洋快递"));// 入仓号  -->改为快递公司名
						sheet.addCell(new Label(columnIndex++, row,order.getUser().getRealName()));// 箱号   -->改为发件人
						sheet.addCell(new Label(columnIndex++, row,order.getUser().getPhone()));// 卡板号   -->改为发件人电话
						sheet.addCell(new Label(columnIndex++, row, order.getOrderId())); // 运单号
						sheet.addCell(new Label(columnIndex++, row,""));// 英文品牌
						/*tmp = order.getOrderDetailTranshipmentIds();
						if (tmp != null && tmp.size() > 0) {
							sheet.addCell(new Label(columnIndex++, row, Arrays.toString(tmp.toArray())));
						} else {
							sheet.addCell(new Label(columnIndex++, row, ""));
						}*/
						
						// 开始到处商品信息
						OrderDetail[] details = order.getDetails();
						StringBuffer sb = new StringBuffer();
						if (details != null)
							for (OrderDetail detail : details) {
								sb.append(detail.getCtype()).append(":").append(detail.getQuantity()).append(";");
							}
						sheet.addCell(new Label(columnIndex++, row, sb.toString()));//中文货物名称
						sheet.addCell(new Label(columnIndex++, row, order.getQuantity()));//数量
						sheet.addCell(new Label(columnIndex++, row, order.getWeight()));//重量

						sheet.addCell(new Label(columnIndex++, row, order.getcName()));//收件人姓名
						sheet.addCell(new Label(columnIndex++, row, order.getcPhone()));//收件人电话

						sheet.addCell(new Label(columnIndex++, row, order.getcProvince()+order.getcCity()+order.getcDistrict()+order.getcStreetAddress()));//收件人地址
						sheet.addCell(new Label(columnIndex++, row, order.getcCity()));//收件人城市

						row++;
					}
				}
			}, "order-datas", null, orders, os);
		}
	}

	// kai 20150915 唯一快递的定单导出
	public  void exportOrderToWeiyiExcel(List<ExportOrder> orders,
			File templetFile, OutputStream os) throws Exception {
		if (templetFile != null && templetFile.exists()) {
			// 模板复制
			ExcelUtil.exportExcel(new ExcelWrite<ExportOrder>() {
				@Override
				public void write(Map<String, String> headers,
						Collection<ExportOrder> datas, WritableSheet sheet)
						throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有用户数据.....");
						sheet.addCell(cell);
						return;
					}
					
					int row = 1;

					for (Order order : datas) {
						if (order == null) {
							continue;
						}
						int columnIndex = 0;
						// sheet.addCell(new Label(columnIndex++, row,
						// order.getId()));// 是用记录编号
						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(row)));// 序号
						sheet.addCell(new Label(columnIndex++, row, order
								.getUserId()));// 所属用户id
						sheet.addCell(new Label(columnIndex++, row, order
								.getCreateDate()));// 收件日期
						sheet.addCell(new Label(columnIndex++, row, order
								.getOrderId()));// 维一运单号
                         //发件人为代理客户，则发件人信息取自订单
						if((order.getUser().getType()!=null)&&(order.getUser().getType().equalsIgnoreCase("5")))
						{
							sheet.addCell(new Label(columnIndex++, row, order.getSenduserName())); // 发件人
							sheet.addCell(new Label(columnIndex++, row, order.getSenduserphone())); // 发件人联系电话
							sheet.addCell(new Label(columnIndex++, row, order.getSenduserAddress())); // 发件人地址							
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, order
									.getUser().getRealName())); // 发件人
							sheet.addCell(new Label(columnIndex++, row, order
									.getUser().getPhone())); // 发件人联系电话
							sheet.addCell(new Label(columnIndex++, row, order
									.getUser().getAddress())); // 发件人地址
						}
						// 开始到处商品信息
						OrderDetail[] details = order.getDetails();
						StringBuffer sb = new StringBuffer();
						StringBuffer xq = new StringBuffer();
						int total = 0;
						if (details != null)
							for (OrderDetail detail : details) {
								/*sb.append(detail.getCtype()).append("*")
										.append(detail.getQuantity())
										.append(",");*/
								sb.append(detail.getXiangqing()).append("*")
								.append(detail.getQuantity())
								.append(";");
								xq.append(detail.getXiangqing()).append(";");
								total = total
										+ Integer.parseInt(detail.getQuantity());
							}
						sheet.addCell(new Label(columnIndex++, row, sb
								.toString()));// 物品名称(中文)
						sheet.addCell(new Label(columnIndex++, row, "1"));// 件数固定为1
						sheet.addCell(new Label(columnIndex++, row, order
								.getWeightKg()));// 重量KG
						sheet.addCell(new Label(columnIndex++, row, order
								.getWeight()));// 重量
						double totalmoney = 0.0;
						double premium = 0.0;// 保险费
						double tariff = 0.0;// 关税
						double basemoney = 0.0;

						// float
						// premium=Float.parseFloat(order.getPremium());//保险费
						// float tariff=Float.parseFloat(order.getTariff());//关税
						// float basemoney=totalmoney-premium-tariff;
						if ((order.getTotalMoney() == null)
								|| (order.getTotalMoney().equals(""))) {

						} else {
							totalmoney = Float
									.parseFloat(order.getTotalMoney());

						}
						if ((order.getPremium() == null)
								|| (order.getPremium().equals(""))) {

						} else {
							premium = Float.parseFloat(order.getPremium());// 保险费

						}
						if ((order.getTariff() == null)
								|| (order.getTariff().equals(""))) {

						} else {

							tariff = Float.parseFloat(order.getTariff());// 关税

						}

						basemoney = totalmoney - premium - tariff;
						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(basemoney)));// 基本运费$
						sheet.addCell(new Label(columnIndex++, row, order
								.getExpressName()));// 快递公司
						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(premium)));// 保险￥
						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(tariff)));// 关税$
						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(totalmoney)));// 费用合计
						sheet.addCell(new Label(columnIndex++, row, order
								.getNamePinyi()));// 收件人姓名拼音
						String temp = "";
						
						
						if ((order.getCardurl() == null)
								|| (order.getCardurl().equals(""))) {
							temp = "否";
						} else {
							temp = "是";
						}
						if ((order.getCardurlother() == null)
								|| (order.getCardurlother().equals(""))) {
							temp =temp+ "|否";
						} else {
							temp =temp+ "|是";
						}
						if ((order.getCardurltogether() == null)
								|| (order.getCardurltogether().equals(""))) {
							temp =temp+ "|否";
						} else {
							temp =temp+ "|是";
						}
						sheet.addCell(new Label(columnIndex++, row, temp));// 是否有身份证
						sheet.addCell(new Label(columnIndex++, row, order
								.getcName()));// 收件人姓名中文
						sheet.addCell(new Label(columnIndex++, row, order
								.getcPhone()));// 收件人联系电话
						// 如果在单独字段中包含了地址，则直接调用，如果不是，则重新构造
						
						sheet.addCell(new Label(columnIndex++, row, order.getcProvince()));// 收件人所在省份
						sheet.addCell(new Label(columnIndex++, row, order.getcCity()));// 收件人所在市
						sheet.addCell(new Label(columnIndex++, row, order.getcDistrict()));// 收件人所在区
						sheet.addCell(new Label(columnIndex++, row, order.getcStreetAddress()));// 收件人详细地址
						/*if ((order.getAddressAll() == null)
								|| (order.getAddressAll().equalsIgnoreCase(""))) {
							sheet.addCell(new Label(columnIndex++, row, order
									.getcProvince()
									+ order.getcCity()
									+ order.getcDistrict()
									+ order.getcStreetAddress()));// 收件人详细地址
						} else {
							sheet.addCell(new Label(columnIndex++, row, order
									.getAddressAll()));// 收件人详细地址
						}*/

						row++;
					}
				}
			}, templetFile, orders, os);
		} else {
			// 重新写
			ExcelUtil.exportExcel(new ExcelWrite<ExportOrder>() {
				@Override
				public void write(Map<String, String> headers,
						Collection<ExportOrder> datas, WritableSheet sheet)
						throws Exception {

					int row = 1;
					for (Order order : datas) {
						int columnIndex = 0;
						// sheet.addCell(new Label(columnIndex++, row,
						// order.getId()));// 是用记录编号
						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(row)));// 序号
						sheet.addCell(new Label(columnIndex++, row, order
								.getUserId()));// 所属用户id
						sheet.addCell(new Label(columnIndex++, row, order
								.getCreateDate()));// 收件日期
						sheet.addCell(new Label(columnIndex++, row, order
								.getOrderId()));// 维一运单号

						sheet.addCell(new Label(columnIndex++, row, order
								.getUser().getRealName())); // 发件人
						sheet.addCell(new Label(columnIndex++, row, order
								.getUser().getPhone())); // 发件人联系电话
						sheet.addCell(new Label(columnIndex++, row, order
								.getUser().getAddress())); // 发件人地址
						// 开始到处商品信息
						OrderDetail[] details = order.getDetails();
						StringBuffer sb = new StringBuffer();
						StringBuffer xq = new StringBuffer();
						int total = 0;
						if (details != null)
							for (OrderDetail detail : details) {
								/*sb.append(detail.getCtype()).append("*")
								.append(detail.getQuantity())
								.append(",");*/
						sb.append(detail.getXiangqing()).append("*")
						.append(detail.getQuantity())
						.append(";");
						xq.append(detail.getXiangqing()).append(";");
						total = total
								+ Integer.parseInt(detail.getQuantity());
							}
						sheet.addCell(new Label(columnIndex++, row, sb
								.toString()));// 物品名称(中文)
						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(total)));// 数量
						sheet.addCell(new Label(columnIndex++, row, order
								.getWeightKg()));// 重量KG
						sheet.addCell(new Label(columnIndex++, row, order
								.getWeight()));// 重量
						float totalmoney = Float.parseFloat(order
								.getTotalMoney());
						float premium = Float.parseFloat(order.getPremium());// 保险费
						float tariff = Float.parseFloat(order.getTariff());// 关税
						float basemoney = totalmoney - premium - tariff;

						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(basemoney)));// 基本运费$
						sheet.addCell(new Label(columnIndex++, row, order
								.getExpressName()));// 快递公司
						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(premium)));// 保险￥
						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(tariff)));// 关税$
						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(totalmoney)));// 费用合计
						sheet.addCell(new Label(columnIndex++, row, order
								.getNamePinyi()));// 收件人姓名拼音

						String temp = "";
						if ((order.getCardurl() == null)
								|| (order.getCardurl().equals(""))) {
							temp = "否";
						} else {
							temp = "是";
						}
						sheet.addCell(new Label(columnIndex++, row, temp));// 是否有身份证
						sheet.addCell(new Label(columnIndex++, row, order
								.getcName()));// 收件人姓名中文
						sheet.addCell(new Label(columnIndex++, row, order
								.getcPhone()));// 收件人联系电话
						// 如果在单独字段中包含了地址，则直接调用，如果不是，则重新构造
						if ((order.getAddressAll() == null)
								|| (order.getAddressAll().equalsIgnoreCase(""))) {
							sheet.addCell(new Label(columnIndex++, row, order
									.getcProvince()
									+ order.getcCity()
									+ order.getcDistrict()
									+ order.getcStreetAddress()));// 收件人详细地址
						} else {
							sheet.addCell(new Label(columnIndex++, row, order
									.getAddressAll()));// 收件人详细地址
						}

						row++;
					}
				}
			}, "order-datas", null, orders, os);
		}
	}
	
	
	// kai 20151027美淘快递的定单导出
	public  void exportOrderToMeitao(List<ExportOrder> orders,
			File templetFile, OutputStream os) throws Exception {
		if (templetFile != null && templetFile.exists()) {
			// 模板复制
			ExcelUtil.exportExcel(new ExcelWrite<ExportOrder>() {
				@Override
				public void write(Map<String, String> headers,
						Collection<ExportOrder> datas, WritableSheet sheet)
						throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有用户数据.....");
						sheet.addCell(cell);
						return;
					}
					
					int row = 1;

					for (ExportOrder order : datas) {
						if (order == null) {
							continue;
						}
						
						int columnIndex = 0;
						// sheet.addCell(new Label(columnIndex++, row,
						// order.getId()));// 是用记录编号
						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(row)));// 序号
						sheet.addCell(new Label(columnIndex++, row, order
								.getUserId()));// 所属用户id
						
						if((order.getType()!=null)&&(order.getType().equalsIgnoreCase("7")))
						{
							sheet.addCell(new Label(columnIndex++, row, order.getThirdCreateDate()));// 收件日期
						}
						else
						{
						sheet.addCell(new Label(columnIndex++, row, order
								.getCreateDate()));// 收件日期
						}
						sheet.addCell(new Label(columnIndex++, row, order
								.getOrderId()));// 维一运单号
                         //发件人为代理客户，则发件人信息取自订单
						if((order.getUser()!=null)&&(order.getUser().getType()!=null)&&(order.getUser().getType().equalsIgnoreCase("5")))
						{
							sheet.addCell(new Label(columnIndex++, row, order.getSenduserName())); // 发件人
							sheet.addCell(new Label(columnIndex++, row, order.getSenduserphone())); // 发件人联系电话
							sheet.addCell(new Label(columnIndex++, row, order.getSenduserAddress())); // 发件人地址							
						}
						else
						{
							if(order.getUser()==null)
							{
								sheet.addCell(new Label(columnIndex++, row, "")); // 发件人
								sheet.addCell(new Label(columnIndex++, row, "")); // 发件人联系电话
								sheet.addCell(new Label(columnIndex++, row, "")); // 发件人地址
							}
							else
							{
								
								
							sheet.addCell(new Label(columnIndex++, row, order
									.getUser().getRealName())); // 发件人
							sheet.addCell(new Label(columnIndex++, row, order
									.getUser().getPhone())); // 发件人联系电话
							sheet.addCell(new Label(columnIndex++, row, order
									.getUser().getAddress())); // 发件人地址
							}
						}
						// 开始到处商品信息
						OrderDetail[] details = order.getDetails();
						StringBuffer sb = new StringBuffer();
						StringBuffer brands = new StringBuffer();
						//StringBuffer xq = new StringBuffer();
						int total = 0;
						String[] Comm_type= order.getComm_type();
						
						if (details != null)
						{
							if((order.getType()!=null)&&(order.getType().equalsIgnoreCase("7")))
							{
								sheet.addCell(new Label(columnIndex++, row, order.getCommodityThirdList()));// 物品名称(中文)
								sheet.addCell(new Label(columnIndex++, row, order.getThirdBrands()));// 物品品牌
								sheet.addCell(new Label(columnIndex++, row, order.getQuantity()));// 件数固定为1
							}
							else
							{
								for (OrderDetail detail : details) {
									for(int nn=0;nn<Comm_type.length;nn++)
									{
										if((Comm_type[nn]!=null)&&(Comm_type[nn].equalsIgnoreCase("0")))//商品类型
										{
											sb.append(detail.getCtype());
										}
										else if((Comm_type[nn]!=null)&&(Comm_type[nn].equalsIgnoreCase("1")))//商品详情
										{
											sb.append(detail.getXiangqing());
										}
										else if((Comm_type[nn]!=null)&&(Comm_type[nn].equalsIgnoreCase("2")))//商品数量
										{
											sb.append(detail.getQuantity());
										}
										if((nn!=(Comm_type.length-1)))
										{
											sb.append("*");
										}
									}
									brands.append(detail.getBrands()+";");
									sb.append(";");
									/*sb.append(detail.getCtype()).append("*")
											.append(detail.getQuantity()).append("*")
											.append(detail.getXiangqing())
											.append(";");*/
									//xq.append(detail.getXiangqing()).append(";");
									total = total
											+ Integer.parseInt(detail.getQuantity());
								}
								sheet.addCell(new Label(columnIndex++, row, sb
										.toString()));// 物品名称(中文)
								
								sheet.addCell(new Label(columnIndex++, row, brands
										.toString()));// 物品名称(中文)
								
								sheet.addCell(new Label(columnIndex++, row, "1"));// 件数固定为1
							}
							
					}
						else
						{
							if((order.getType()!=null)&&(order.getType().equalsIgnoreCase("7")))
							{
								sheet.addCell(new Label(columnIndex++, row, order.getCommodityThirdList()));// 物品名称(中文)
								sheet.addCell(new Label(columnIndex++, row, order.getThirdBrands()));// 物品品牌
								sheet.addCell(new Label(columnIndex++, row, order.getQuantity()));// 件数固定为1
							}
							else
							{
								sheet.addCell(new Label(columnIndex++, row, ""));// 物品名称(中文)
								sheet.addCell(new Label(columnIndex++, row, ""));// 物品品牌
								sheet.addCell(new Label(columnIndex++, row, ""));// 件数固定为1
							}
						}
						//sheet.addCell(new Label(columnIndex++, row, sb
						//		.toString()));// 物品名称(中文)
						//sheet.addCell(new Label(columnIndex++, row, "1"));// 件数固定为1
						sheet.addCell(new Label(columnIndex++, row, order
								.getWeightKg()));// 重量KG
						sheet.addCell(new Label(columnIndex++, row, order
								.getWeight()));// 重量
						sheet.addCell(new Label(columnIndex++, row, order.getJwweight()
								));// 实际重量
						double totalmoney = 0.0;
						double premium = 0.0;// 保险费
						double tariff = 0.0;// 关税
						double basemoney = 0.0;
						double CommodityCost=0.0;

						// float
						// premium=Float.parseFloat(order.getPremium());//保险费
						// float tariff=Float.parseFloat(order.getTariff());//关税
						// float basemoney=totalmoney-premium-tariff;
						if ((order.getTotalMoney() == null)
								|| (order.getTotalMoney().equals(""))) {

						} else {
							totalmoney = Float
									.parseFloat(order.getTotalMoney());

						}
						
						if ((order.getPremium() == null)
								|| (order.getPremium().equals(""))) {

						} else {
							premium = Float.parseFloat(order.getPremium());// 保险费

						}
						if ((order.getTariff() == null)
								|| (order.getTariff().equals(""))) {

						} else {

							tariff = Float.parseFloat(order.getTariff());// 关税

						}
						if((order.getType()!=null)&&(order.getType().equalsIgnoreCase("7")))
						{
							basemoney=Float.parseFloat(order.getBaseMoney());
						}
						else
						{
							basemoney = totalmoney - premium - tariff;
						}
						if ((order.getCommodityCost() == null)//取成本
								|| (order.getCommodityCost().equals(""))) {

						} else {
							CommodityCost = Float
									.parseFloat(order.getCommodityCost());
							BigDecimal   bd  =   new  BigDecimal((double)CommodityCost);    
							bd   =  bd.setScale(2,4);    
							CommodityCost   =  bd.doubleValue();  

						}
						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(basemoney)));// 基本运费$
						sheet.addCell(new Label(columnIndex++, row, order
								.getExpressName()));// 快递公司
						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(premium)));// 保险￥
						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(tariff)));// 关税$
						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(totalmoney)));// 费用合计
						
						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(CommodityCost)));// 费用成本
						
						sheet.addCell(new Label(columnIndex++, row, order
								.getNamePinyi()));// 收件人姓名拼音
						String temp = "";
						if((order.getType()!=null)&&(order.getType().equalsIgnoreCase("7")))
						{
							temp=order.getCardOrNot();
						}
						else
						{
						
							if ((order.getCardurl() == null)
									|| (order.getCardurl().equals(""))) {
								temp = "否";
							} else {
								temp = "是";
							}
							if ((order.getCardurlother() == null)
									|| (order.getCardurlother().equals(""))) {
								temp =temp+ "|否";
							} else {
								temp =temp+ "|是";
							}
							if ((order.getCardurltogether() == null)
									|| (order.getCardurltogether().equals(""))) {
								temp =temp+ "|否";
							} else {
								temp =temp+ "|是";
							}
						}
						sheet.addCell(new Label(columnIndex++, row, temp));// 是否有身份证
						sheet.addCell(new Label(columnIndex++, row, order
								.getcName()));// 收件人姓名中文
						sheet.addCell(new Label(columnIndex++, row, order
								.getcPhone()));// 收件人联系电话
						// 如果在单独字段中包含了地址，则直接调用，如果不是，则重新构造
						
						sheet.addCell(new Label(columnIndex++, row, order.getcProvince()));// 收件人所在省份
						sheet.addCell(new Label(columnIndex++, row, order.getcCity()));// 收件人所在市
						sheet.addCell(new Label(columnIndex++, row, order.getcDistrict()));// 收件人所在区
						sheet.addCell(new Label(columnIndex++, row, order.getcStreetAddress()));// 收件人详细地址
						/*if ((order.getAddressAll() == null)
								|| (order.getAddressAll().equalsIgnoreCase(""))) {
							sheet.addCell(new Label(columnIndex++, row, order
									.getcProvince()
									+ order.getcCity()
									+ order.getcDistrict()
									+ order.getcStreetAddress()));// 收件人详细地址
						} else {
							sheet.addCell(new Label(columnIndex++, row, order
									.getAddressAll()));// 收件人详细地址
						}*/

						row++;
					}
				}
			}, templetFile, orders, os);
		} else {
			// 重新写
			ExcelUtil.exportExcel(new ExcelWrite<ExportOrder>() {
				@Override
				public void write(Map<String, String> headers,
						Collection<ExportOrder> datas, WritableSheet sheet)
						throws Exception {

					int row = 1;
					for (ExportOrder order : datas) {
						int columnIndex = 0;
						// sheet.addCell(new Label(columnIndex++, row,
						// order.getId()));// 是用记录编号
						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(row)));// 序号
						sheet.addCell(new Label(columnIndex++, row, order
								.getUserId()));// 所属用户id
						sheet.addCell(new Label(columnIndex++, row, order
								.getCreateDate()));// 收件日期
						sheet.addCell(new Label(columnIndex++, row, order
								.getOrderId()));// 维一运单号

						sheet.addCell(new Label(columnIndex++, row, order
								.getUser().getRealName())); // 发件人
						sheet.addCell(new Label(columnIndex++, row, order
								.getUser().getPhone())); // 发件人联系电话
						sheet.addCell(new Label(columnIndex++, row, order
								.getUser().getAddress())); // 发件人地址
						// 开始到处商品信息
						OrderDetail[] details = order.getDetails();
						StringBuffer sb = new StringBuffer();
						//StringBuffer xq = new StringBuffer();
						
						int total = 0;
						String[] Comm_type= order.getComm_type();
						if (details != null)
							for (OrderDetail detail : details) {
								for(int nn=0;nn<Comm_type.length;nn++)
								{
									if((Comm_type[nn]!=null)&&(Comm_type[nn].equalsIgnoreCase("0")))//商品类型
									{
										sb.append(detail.getCtype());
									}
									else if((Comm_type[nn]!=null)&&(Comm_type[nn].equalsIgnoreCase("1")))//商品详情
									{
										sb.append(detail.getXiangqing());
									}
									else if((Comm_type[nn]!=null)&&(Comm_type[nn].equalsIgnoreCase("2")))//商品数量
									{
										sb.append(detail.getQuantity());
									}
									if(nn!=(Comm_type.length-1))
									{
										sb.append("*");
									}
								}
								sb.append(";");
								/*sb.append(detail.getCtype()).append("*")
										.append(detail.getQuantity()).append("*")
										.append(detail.getXiangqing())
										.append(";");*/
								//xq.append(detail.getXiangqing()).append(";");
								total = total
										+ Integer.parseInt(detail.getQuantity());
							}
						sheet.addCell(new Label(columnIndex++, row, sb
								.toString()));// 物品名称(中文)
						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(total)));// 数量
						sheet.addCell(new Label(columnIndex++, row, order
								.getWeightKg()));// 重量KG
						sheet.addCell(new Label(columnIndex++, row, order
								.getWeight()));// 重量
						float totalmoney = Float.parseFloat(order
								.getTotalMoney());
						double CommodityCost=0.0;
						if ((order.getCommodityCost() == null)//取成本
								|| (order.getCommodityCost().equals(""))) {

						} else {
							CommodityCost = Float
									.parseFloat(order.getCommodityCost());
							BigDecimal   bd  =   new  BigDecimal((double)CommodityCost);    
							bd   =  bd.setScale(2,4);    
							CommodityCost   =  bd.doubleValue();  

						}
						
						
						float premium = Float.parseFloat(order.getPremium());// 保险费
						float tariff = Float.parseFloat(order.getTariff());// 关税
						float basemoney = totalmoney - premium - tariff;

						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(basemoney)));// 基本运费$
						sheet.addCell(new Label(columnIndex++, row, order
								.getExpressName()));// 快递公司
						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(premium)));// 保险￥
						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(tariff)));// 关税$
						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(totalmoney)));// 费用合计
						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(CommodityCost)));// 费用合计
						sheet.addCell(new Label(columnIndex++, row, order
								.getNamePinyi()));// 收件人姓名拼音

						String temp = "";
						if ((order.getCardurl() == null)
								|| (order.getCardurl().equals(""))) {
							temp = "否";
						} else {
							temp = "是";
						}
						sheet.addCell(new Label(columnIndex++, row, temp));// 是否有身份证
						sheet.addCell(new Label(columnIndex++, row, order
								.getcName()));// 收件人姓名中文
						sheet.addCell(new Label(columnIndex++, row, order
								.getcPhone()));// 收件人联系电话
						// 如果在单独字段中包含了地址，则直接调用，如果不是，则重新构造
						if ((order.getAddressAll() == null)
								|| (order.getAddressAll().equalsIgnoreCase(""))) {
							sheet.addCell(new Label(columnIndex++, row, order
									.getcProvince()
									+ order.getcCity()
									+ order.getcDistrict()
									+ order.getcStreetAddress()));// 收件人详细地址
						} else {
							sheet.addCell(new Label(columnIndex++, row, order
									.getAddressAll()));// 收件人详细地址
						}

						row++;
					}
				}
			}, "order-datas", null, orders, os);
		}
	}
	
	
	//export to internal add by chenkanghua 
	public static void exportOrderToInternalExcel(List<ExportOrder> orders, File templetFile, OutputStream os) throws Exception {
		if (templetFile != null && templetFile.exists()) {
			// 模板复制
			ExcelUtil.exportExcel(new ExcelWrite<ExportOrder>() {
				@Override
				public void write(Map<String, String> headers, Collection<ExportOrder> datas, WritableSheet sheet)
				        throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有用户数据.....");
						sheet.addCell(cell);
						return;
					}

					int row = 1;
					for (Order order : datas) {
						int columnIndex = 0;

						sheet.addCell(new Label(columnIndex++, row, order.getOrderId())); // 运单号
						sheet.addCell(new Label(columnIndex++, row, order.getWeight()));//重量
						sheet.addCell(new Label(columnIndex++, row, "1"));//箱数
						
						// 开始到处商品信息
						OrderDetail[] details = order.getDetails();
						StringBuffer sb = new StringBuffer();
						if (details != null)
							for (OrderDetail detail : details) {
								sb.append(detail.getCtype()).append(":").append(detail.getQuantity()).append(";");
							}
						sheet.addCell(new Label(columnIndex++, row, sb.toString()));//内容
						
						sheet.addCell(new Label(columnIndex++, row, order.getUser().getRealName()));//寄件人姓名

						sheet.addCell(new Label(columnIndex++, row, order.getUser().getAddress()));//寄件人地址
						
						sheet.addCell(new Label(columnIndex++, row, order.getcName()));//收件人姓名
						
						sheet.addCell(new Label(columnIndex++, row, order.getcProvince()));//省
						sheet.addCell(new Label(columnIndex++, row, order.getcCity()));//城市
						sheet.addCell(new Label(columnIndex++, row, order.getcDistrict()));//区/县
						sheet.addCell(new Label(columnIndex++, row, order.getcStreetAddress()));//地址
						sheet.addCell(new Label(columnIndex++, row, order.getcPhone()));//电话
						sheet.addCell(new Label(columnIndex++, row, order.getcZipCode()));//邮编

						row++;
					}
				}
			}, templetFile, orders, os);
		} else {
			// 重新写
			ExcelUtil.exportExcel(new ExcelWrite<ExportOrder>() {
				@Override
				public void write(Map<String, String> headers, Collection<ExportOrder> datas, WritableSheet sheet)
				        throws Exception {
					
					int row = 1;
					for (Order order : datas) {
						int columnIndex = 0;

						sheet.addCell(new Label(columnIndex++, row, order.getOrderId())); // 运单号
						sheet.addCell(new Label(columnIndex++, row, order.getWeight()));//重量
						sheet.addCell(new Label(columnIndex++, row, "1"));//箱数
						
						// 开始到处商品信息
						OrderDetail[] details = order.getDetails();
						StringBuffer sb = new StringBuffer();
						if (details != null)
							for (OrderDetail detail : details) {
								sb.append(detail.getCtype()).append(":").append(detail.getQuantity()).append(";");
							}
						sheet.addCell(new Label(columnIndex++, row, sb.toString()));//内容
						
						sheet.addCell(new Label(columnIndex++, row, order.getUser().getRealName()));//寄件人姓名

						sheet.addCell(new Label(columnIndex++, row, order.getUser().getAddress()));//寄件人地址
						
						sheet.addCell(new Label(columnIndex++, row, order.getcName()));//收件人姓名
						
						sheet.addCell(new Label(columnIndex++, row, order.getcProvince()));//省
						sheet.addCell(new Label(columnIndex++, row, order.getcCity()));//城市
						sheet.addCell(new Label(columnIndex++, row, order.getcDistrict()));//区/县
						sheet.addCell(new Label(columnIndex++, row, order.getcStreetAddress()));//地址
						sheet.addCell(new Label(columnIndex++, row, order.getcPhone()));//电话
						sheet.addCell(new Label(columnIndex++, row, order.getcZipCode()));//邮编


						row++;
					}
				}
			}, "order-datas", null, orders, os);
		}
	}	
	

	public static List<ImportOrder> readOrderExcel(InputStream input)
			throws Exception {
		return ExcelUtil.importExcel(new ExcelReader<ImportOrder>() {
			@Override
			public List<ImportOrder> read(Sheet sheet) throws Exception {
				List<ImportOrder> list = new ArrayList<ImportOrder>();
				int rows = sheet.getRows();
				for (int i = 0; i < rows; i++) {
					int j = 0;
					ImportOrder io = new ImportOrder();
					io.setOrderId(sheet.getCell(j++, i).getContents());
					String state = sheet.getCell(j++, i).getContents();
					io.setState(state);
					if (Constant.ORDER_STATE7.equals(state)) {
						// 是空运，需要获取一个航班信息
						io.setFlight(sheet.getCell(j++, i).getContents());
					}
					if (Constant.ORDER_STATE9.equals(state)) {
						// 需要获取一个第三方快递信息
						io.setThirdPNS(sheet.getCell(j++, i).getContents());
						io.setThirdNo(sheet.getCell(j++, i).getContents());
					}
					list.add(io);
				}
				return list;
			}
		}, input);
	}
	
	//add by chenkanghua
	public static List<ImportOrder> readOrderFromMeitaoExcel(InputStream input)
			throws Exception {
		return ExcelUtil.importExcel(new ExcelReader<ImportOrder>() {
			@Override
			public List<ImportOrder> read(Sheet sheet) throws Exception {
				List<ImportOrder> list = new ArrayList<ImportOrder>();
				int rows = sheet.getRows();
				for (int i = 1; i < rows; i++) {
					//int j = 0;
					
					ImportOrder io = new ImportOrder();

					io.setOrderId(sheet.getCell(3, i).getContents());
					String state = "8";
					io.setState(state);
					
					// 需要获取一个第三方快递信息
					io.setThirdNo(sheet.getCell(13, i).getContents());
					if(sheet.getColumns()<14){
						io.setThirdPNS("ems");
					}else{
						if (StringUtil.isEmpty(sheet.getCell(14, i).getContents()) ) {
							io.setThirdPNS("ems");
						}else{
							io.setThirdPNS(sheet.getCell(14, i).getContents());
						}
					}
					
					list.add(io);
				}
				return list;
			}
		}, input);
	}


	
//判断字符串是否为数字
	public static boolean isNumeric(String str){
		for (int i = 0; i < str.length(); i++){
		   System.out.println(str.charAt(i));
		   if (!Character.isDigit(str.charAt(i))){
		    return false;
		   }
		}
		return true;
		}
	//kai 20150918  导入唯一快递状态 
	public static List<ImportOrder> readOrderExcel_weiyi_state(InputStream input)
			throws Exception {
		return ExcelUtil.importExcel(new ExcelReader<ImportOrder>() {
			@Override
			public List<ImportOrder> read(Sheet sheet) throws Exception {
				List<ImportOrder> list = new ArrayList<ImportOrder>();
				int rows = sheet.getRows();
				for (int i = 1; i < rows; i++) {
					int j = 1;
					
					
					ImportOrder io = new ImportOrder();
					String orderid=sheet.getCell(j++, i).getContents();
					if((orderid!=null)&&(!orderid.equalsIgnoreCase("")))
					{
						io.setOrderId(orderid.trim());//运单号
						String state = sheet.getCell(j++, i).getContents();//运单状态
						if((state!=null)&&(!state.equalsIgnoreCase(""))&&(isNumeric(state)))
						{
							io.setState(state.trim());
							String stateremark = sheet.getCell(j++, i).getContents();//状态说明
							io.setStateremark(stateremark);
							String thirdPNS = sheet.getCell(j++, i).getContents();//快递代码		
							
							if(StringUtil.isEmpty(thirdPNS))
							{
								io.setThirdPNS("");
							}
							else
							{
								io.setThirdPNS(thirdPNS.trim());
							}
							String thirdNo = sheet.getCell(j++, i).getContents();//快递代码	
							if(StringUtil.isEmpty(thirdNo))
							{
								io.setThirdNo("");
							}
							else
							{
								io.setThirdNo(thirdNo.trim());
							}
							
							
						}
						else if((state==null)||(state.equalsIgnoreCase("")))//留空表示原状态不变
						{
							io.setState(null);
							String stateremark = sheet.getCell(j++, i).getContents();//状态说明
							io.setStateremark(stateremark);
							String thirdPNS = sheet.getCell(j++, i).getContents();//快递代码						
							if(StringUtil.isEmpty(thirdPNS))
							{
								io.setThirdPNS("");
							}
							else
							{
								io.setThirdPNS(thirdPNS.trim());
							}
							String thirdNo = sheet.getCell(j++, i).getContents();//快递代码	
							if(StringUtil.isEmpty(thirdNo))
							{
								io.setThirdNo("");
							}
							else
							{
								io.setThirdNo(thirdNo.trim());
							}
						}
						else
						{
							String str0 = "第" + i + "行，第"+j+"列只能为数字格式或留空!";
							throw new RuntimeException(str0);
						}
						
					   				
					}
					else
					{
						break;
					}
					
					
					list.add(io);
				}
				return list;
			}
		}, input);
	}
	
	  public static void exportOrderSumToExcel(List<SumCommodity> sumcommdity, File templetFile, OutputStream os) throws Exception {
		  if ((templetFile != null) && (templetFile.exists())){
		        ExcelUtil.exportExcel(new ExcelWrite<SumCommodity>()
		        {
		          public void write(Map<String, String> headers, Collection<SumCommodity> datas, WritableSheet sheet) throws Exception
		          {
		            if ((datas == null) || (datas.isEmpty())) {
		              sheet.mergeCells(0, 1, 11, 1);
		              Label cell = new Label(0, 1, "没有用户数据.....");
		              sheet.addCell(cell);
		              return;
		            }
		            int row = 1;
		            double a[] = {0.00,0.00,0.00};
		            for (SumCommodity sumcommdity : datas) {
		              int columnIndex = 0;
		              
		              sheet.addCell(new Label(columnIndex++, row, sumcommdity.getCommodityid()));
		              sheet.addCell(new Label(columnIndex++, row, sumcommdity.getCommodityname()));

		              sheet.addCell(new Label(columnIndex++, row, sumcommdity.getSumquantity()));
		              sheet.addCell(new Label(columnIndex++, row, sumcommdity.getSumsjweight()));
		              //sheet.addCell(new Label(columnIndex++, row, sumcommdity.getSumjfweight()));
		              
		              a[0]=a[0]+StringUtil.string2Double(sumcommdity.getSumquantity());
		              a[1]=a[1]+StringUtil.string2Double(sumcommdity.getSumsjweight());
		              //a[2]=a[2]+StringUtil.string2Double(sumcommdity.getSumjfweight());
		              row++;
		            }
		            row++;
		            sheet.addCell(new Label(1, row, "合计："));
		            DecimalFormat df   =new DecimalFormat("#.00");   
		            sheet.addCell(new Label(2, row, df.format(a[0])));
		            sheet.addCell(new Label(3, row, df.format(a[1])));
		            sheet.addCell(new Label(4, row, df.format(a[2])));
		          }
		        }
		        , templetFile, sumcommdity, os);
		      }
		      else
		        ExcelUtil.exportExcel(new ExcelWrite<SumCommodity>()
		        {
		          public void write(Map<String, String> headers, Collection<SumCommodity> datas, WritableSheet sheet) throws Exception
		          {
		           
		            int row = 1;
		            double a[] = {0.00,0.00,0.00};
		            for (SumCommodity sumcommdity : datas) {
		              int columnIndex = 0;

		              sheet.addCell(new Label(columnIndex++, row, sumcommdity.getCommodityid()));
		              sheet.addCell(new Label(columnIndex++, row, sumcommdity.getCommodityname()));

		              sheet.addCell(new Label(columnIndex++, row, sumcommdity.getSumquantity()));
		              sheet.addCell(new Label(columnIndex++, row, sumcommdity.getSumsjweight()));
		              //sheet.addCell(new Label(columnIndex++, row, sumcommdity.getSumjfweight()));
		              
		              a[0]=a[0]+StringUtil.string2Double(sumcommdity.getSumquantity());
		              a[1]=a[1]+StringUtil.string2Double(sumcommdity.getSumsjweight());
		              //a[2]=a[2]+StringUtil.string2Double(sumcommdity.getSumjfweight());
		              
		              row++;
		            }
		            
		            row++;
		            sheet.addCell(new Label(1, row, "合计："));
		            DecimalFormat df   =new DecimalFormat("#.00");   
		            sheet.addCell(new Label(2, row, df.format(a[0])));
		            sheet.addCell(new Label(3, row, df.format(a[1])));
		            sheet.addCell(new Label(4, row, df.format(a[2])));
		          }
		        }
		        , "order-datas", null, sumcommdity, os); 
		  }
	  
	  
	  
	  

		// kai 20150916 添加唯一快递批量导入
		public static List<Order> readOrderFromWeiyiExcel(InputStream input)
				throws Exception {
			return ExcelUtil.importExcel(new ExcelReader<Order>() {
				@Override
				public List<Order> read(Sheet sheet) throws Exception {
					List<Order> list = new ArrayList<Order>();
					int rows = sheet.getRows();
					if (rows < 2) {
						throw new RuntimeException("运单文件不能为空！");
					}
					for (int i = 1; i < rows; i++) {

						int j = 0;
						
						Order order = new Order();
						// 序号 所属用户 收件日期 维一运单号 发件人 发件人联系电话 发件人地址 物品名称(中文) 件数 重量KG
						// 重量（LB） 基本运费$ 快递公司 保险￥ 关税$ 费用合计 收件人姓名拼音 是否有身份证 收件人姓名中文
						// 收件人联系电话 收件人详细地址
						//String isno = StringUtil.getValue(sheet.getCell(j++, i)
							//	.getContents(), ""); // 序号
						String userId = StringUtil.getValue(sheet.getCell(j++, i)
								.getContents(), ""); // 获取用户id
						if ((userId == null) || (userId.equalsIgnoreCase(""))) {//如果是空，直接跳出
							//int row_temp=i+1;
							//String str0 = "(所属用户)第" + row_temp + "行," + "第" + j + "列不能为空!";
							//throw new RuntimeException(str0);
							break;
						}

						String createDate = StringUtil.getValue(
								sheet.getCell(j++, i).getContents(), ""); // 收件日期
						//String orderId = StringUtil.getValue(sheet.getCell(j++, i)
						//		.getContents(), ""); // 维一运单号
						String userName = StringUtil.getValue(sheet.getCell(j++, i)
								.getContents(), ""); // 发件人
						String senduserphone = StringUtil.getValue(
								sheet.getCell(j++, i).getContents(), ""); // 发件人联系电话
						String saddr = StringUtil.getValue(sheet.getCell(j++, i)
								.getContents(), ""); // 发件人地址
						String commodifys = StringUtil.getValue(
								sheet.getCell(j++, i).getContents(), ""); // 物品名称(中文)
						if ((commodifys == null)
								|| (commodifys.equalsIgnoreCase(""))) {
							String str0 = "(物品名称)第" + i + "行," + "第" + j + "列不能为空!";
							throw new RuntimeException(str0);
						}
						order.setDetails(analyticalOrderDetail_weiyi(commodifys, i+1,
								j));
						//String quantitys = StringUtil.getValue(sheet
						//		.getCell(j++, i).getContents(), ""); // 件数
						String weightKg = StringUtil.getValue(sheet.getCell(j++, i)
								.getContents(), ""); // 重量KG
						String weight = StringUtil.getValue(sheet.getCell(j++, i)
								.getContents(), ""); // 重量
						//String basemoney = StringUtil.getValue(sheet
						//		.getCell(j++, i).getContents(), ""); // 基本运费$
						String expressName = StringUtil.getValue(
								sheet.getCell(j++, i).getContents(), ""); // 快递公司
						String premium = StringUtil.getValue(sheet.getCell(j++, i)
								.getContents(), ""); // 保险￥
						String tariff = StringUtil.getValue(sheet.getCell(j++, i)
								.getContents(), ""); // 关税$
						String totalMoney = StringUtil.getValue(
								sheet.getCell(j++, i).getContents(), ""); // $ 费用合计
						String NamePinyi = StringUtil.getValue(sheet
								.getCell(j++, i).getContents(), ""); // 收件人姓名拼音
						//String cardurl0 = StringUtil.getValue(sheet.getCell(j++, i)
						//		.getContents(), ""); // 是否有身份证
						String cName = StringUtil.getValue(sheet.getCell(j++, i)
								.getContents(), "");// 收件人姓名中文
						String cphone = StringUtil.getValue(sheet.getCell(j++, i)
								.getContents(), "");// 收件人联系电话
						/*String addressAll = StringUtil.getValue(
								sheet.getCell(j++, i).getContents(), "");// 收件人姓名中文*/
						String cProvince= StringUtil.getValue(sheet.getCell(j++, i)
								.getContents(), "");// 收件人所在省
						String cCity= StringUtil.getValue(sheet.getCell(j++, i)
								.getContents(), "");// 收件人所在市
						String cDistrict= StringUtil.getValue(sheet.getCell(j++, i)
								.getContents(), "");// 收件人所在区
						String cStreetAddress= StringUtil.getValue(sheet.getCell(j++, i)
								.getContents(), "");// 收件人详细地址
						
						order.setUserId(userId);
						order.setCreateDate(createDate);
						order.setOrderId("");
						//order.setUserName(userName);
						order.setSenduserName(userName);
						order.setSenduserphone(senduserphone);
						order.setSenduserAddress(saddr);
						
						try{
								if((weightKg==null)||(weightKg.equalsIgnoreCase("")))
								{
									order.setWeightKg("0.0");
								}
								else
								{
									order.setWeightKg(String.valueOf(StringUtil
											.string2Double(weightKg)));
									
								}
								
								if((weight==null)||(weight.equalsIgnoreCase("")))
								{
									order.setWeight("0");
								}
								else
								{
									order.setWeight(String.valueOf(StringUtil
											.string2Double(weight)));
									
								}
								
								if((premium==null)||(premium.equalsIgnoreCase("")))
								{
									order.setPremium("0");
								}
								else
								{
									order.setPremium(String.valueOf(StringUtil
											.string2Double(premium)));
									
								}
								if((tariff==null)||(tariff.equalsIgnoreCase("")))
								{
									order.setTariff("0");
								}
								else
								{
									order.setTariff(String.valueOf(StringUtil
											.string2Double(tariff)));
									
								}
			
								if((totalMoney==null)||(totalMoney.equalsIgnoreCase("")))
								{
									order.setTotalMoney("0");
								}
								else
								{
									order.setTotalMoney(String.valueOf(StringUtil
											.string2Double(totalMoney)));
									
								}
						
						
						}
						catch(Exception e)
						{
							String str0 = "(重量，税，保险，总价钱)第" + i + "行，只能为数字格式或留空!";
							throw new RuntimeException(str0);
						
						}
						order.setNamePinyi(NamePinyi);
						order.setcName(cName);
						order.setcPhone(cphone);
						//order.setAddressAll(addressAll);
						order.setcProvince(cProvince);
						order.setcCity(cCity);
						order.setcDistrict(cDistrict);
						order.setcStreetAddress(cStreetAddress);
						order.setExpressName(expressName);
						list.add(order);
					}
					return list;
				}
			}, input);
		}
		
	
		// kai 20151106 导入第三方快递信息，直接写入数据
		public static List<Order> readOrderFromWeiyithirdExcel(InputStream input)
				throws Exception {
			return ExcelUtil.importExcel(new ExcelReader<Order>() {
				@Override
				public List<Order> read(Sheet sheet) throws Exception {
					List<Order> list = new ArrayList<Order>();
					int rows = sheet.getRows();
					if (rows < 2) {
						throw new RuntimeException("运单文件不能为空！");
					}
					for (int i = 1; i < rows; i++) {

						int j = 0;
						
						Order order = new Order();
						//先把所有的数据，不管对错，先保存到缓存中
						String isno = StringUtil.getValue(sheet.getCell(j++, i)
								.getContents(), ""); // 序号
						order.setId(isno);//仅仅保存数据，插入的时候不会使用，方便返回错误时显示内容
						
						
						order.setThirdCreateDate(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//第三方收件日期
						
						
						order.setOrderId(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//第三方订单号
						
						if(StringUtil.isEmpty(order.getOrderId()))
						{
							break;
						}
						else
						{
							order.setOrderId(order.getOrderId().toUpperCase());
						}
						
						
						order.setFlight(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//第三方航班号
						order.setBoxNo(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//第三方集装箱号
						order.setPayOrNot(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//第三方是否已经付款
						order.setSenduserName(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//第三方导入是的发件人用户名
						order.setSenduserphone(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//第三方联系电话
						order.setCommodityThirdList(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//发件人的商品清单
						order.setThirdBrands(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//发件人的商品品牌
						
						order.setQuantity(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//发件人的数量
						
						order.setWeightKg(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//重量kg
						order.setWeight(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//重量
						order.setBaseMoney(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//基本费用
						order.setExpressName(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//快递公司名称
						order.setPremium(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//保险费用
						order.setTotalMoney(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//总费用
						order.setNamePinyi(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//拼音
						order.setCardOrNot(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//是否有身份证
						order.setcName(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//收件人姓名
						order.setcPhone(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//收件人电话
						order.setcStreetAddress(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//收件人地址
						order.setImportRemark(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//导入备注
						order.setRemark(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//运单备注
						order.setState(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//导入状态
						order.setStateRemark(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//导入状态说明
						
						
						list.add(order);
					}
					return list;
				}
			}, input);
		}
		
		
		
		
		//当读取第三方数据时，出错返回数据
		public static void exportOrderTocheckthirdExcel(List<ImportthirdOrder> orders, File templetFile, OutputStream os) throws Exception {
			if (templetFile != null && templetFile.exists()) {
				// 模板复制
				ExcelUtil.exportExcel(new ExcelWrite<ImportthirdOrder>() {
					@Override
					public void write(Map<String, String> headers, Collection<ImportthirdOrder> datas, WritableSheet sheet)
					        throws Exception {
						if (datas == null || datas.isEmpty()) {
							sheet.mergeCells(0, 1, 11, 1);
							Label cell = new Label(0, 1, "没有用户数据.....");
							sheet.addCell(cell);
							return;
						}

						int row = 1;
						
						for (ImportthirdOrder iorder : datas)
						{
							WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
				                    10, WritableFont.NO_BOLD);// 字体样式
							
							WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
							redcolor.setBackground(Colour.RED);
							
							WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
							oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
							String[] orderflag=iorder.getOrderflag();
							String[] userflag=iorder.getUserflag();
							for (Order order : iorder.getOrders()) {
								int columnIndex = 0;
								 sheet.addCell(new Label(columnIndex++, row,
								 order.getId()));// 用户上传的编号 
								
								sheet.addCell(new Label(columnIndex++, row,order.getThirdCreateDate()));// 入仓号  
								
								if((orderflag[row-1]!=null)&&(orderflag[row-1].equalsIgnoreCase("0")))
								{
									sheet.addCell(new Label(columnIndex++, row,order.getOrderId()));// 第三方订单号不存在
								}
								else if((orderflag[row-1]!=null)&&(orderflag[row-1].equalsIgnoreCase("1")))
								{
									sheet.addCell(new Label(columnIndex++, row,order.getOrderId(),redcolor));// 第三方订单号 存在
								}
								else
								{
									sheet.addCell(new Label(columnIndex++, row,order.getOrderId(),oceanbluecolor));// 第三方订单号 异常
								}
								sheet.addCell(new Label(columnIndex++, row,order.getFlight()));// 第三方航班号
								
								sheet.addCell(new Label(columnIndex++, row,order.getBoxNo()));// 第三方集装箱号
								sheet.addCell(new Label(columnIndex++, row,order.getPayOrNot()));// 第三方是否已经付款
								sheet.addCell(new Label(columnIndex++, row,order.getSenduserName()));// 第三方导入是的发件人用户名
								sheet.addCell(new Label(columnIndex++, row,order.getSenduserphone()));// 第三方发件人联系电话
								sheet.addCell(new Label(columnIndex++, row,order.getCommodityThirdList()));// 发件人的商品清单
								sheet.addCell(new Label(columnIndex++, row,order.getThirdBrands()));// 发件人的商品品牌
								sheet.addCell(new Label(columnIndex++, row,order.getQuantity()));// 发件数量
								sheet.addCell(new Label(columnIndex++, row,order.getWeightKg()));//重量kg
								sheet.addCell(new Label(columnIndex++, row,order.getWeight()));//重量
								sheet.addCell(new Label(columnIndex++, row,order.getBaseMoney()));////基本费用
								sheet.addCell(new Label(columnIndex++, row,order.getExpressName()));//快递公司名称
								sheet.addCell(new Label(columnIndex++, row,order.getPremium()));//保险费用
								sheet.addCell(new Label(columnIndex++, row,order.getTotalMoney()));//总费用
								sheet.addCell(new Label(columnIndex++, row,order.getNamePinyi()));//拼音
								sheet.addCell(new Label(columnIndex++, row,order.getCardOrNot()));//是否有身份证
								sheet.addCell(new Label(columnIndex++, row,order.getcName()));//收件人姓名
								sheet.addCell(new Label(columnIndex++, row,order.getcPhone()));//收件人电话
								sheet.addCell(new Label(columnIndex++, row,order.getcStreetAddress()));//收件人地址
								sheet.addCell(new Label(columnIndex++, row,order.getImportRemark()));//导入备注
								sheet.addCell(new Label(columnIndex++, row,order.getRemark()));//运单备注
								sheet.addCell(new Label(columnIndex++, row,order.getState()));//导入状态
								sheet.addCell(new Label(columnIndex++, row,order.getStateRemark()));//导入状态说明
								
								if((userflag[row-1]!=null)&&(userflag[row-1].equalsIgnoreCase("0")))
								{
									sheet.addCell(new Label(columnIndex++, row,"用户已注册"));//导入状态
								}
								else if((userflag[row-1]!=null)&&(userflag[row-1].equalsIgnoreCase("1")))
								{
									
									sheet.addCell(new Label(columnIndex++, row,"用户没注册",redcolor));//导入状态
								}
								else
								{
									sheet.addCell(new Label(columnIndex++, row,"用户信息异常",oceanbluecolor));//导入状态
								}
								
								
								
								row++;
							}
						}
					}
				}, templetFile, orders, os);
			} else {
				// 重新写
				ExcelUtil.exportExcel(new ExcelWrite<ImportthirdOrder>() {
					@Override
					public void write(Map<String, String> headers, Collection<ImportthirdOrder> datas, WritableSheet sheet)
					        throws Exception {
						
						//int row = 1;
					//	for (Order order : datas) {
						//	int columnIndex = 0;
							// sheet.addCell(new Label(columnIndex++, row,
							// order.getId()));// 是用记录编号
						/*	sheet.addCell(new Label(columnIndex++, row,""));// 入仓号  
							sheet.addCell(new Label(columnIndex++, row,""));// 箱号   
							sheet.addCell(new Label(columnIndex++, row,""));// 卡板号   
							sheet.addCell(new Label(columnIndex++, row, order.getOrderId())); // 运单号
							sheet.addCell(new Label(columnIndex++, row,""));// 英文品牌*/
							/*tmp = order.getOrderDetailTranshipmentIds();
							if (tmp != null && tmp.size() > 0) {
								sheet.addCell(new Label(columnIndex++, row, Arrays.toString(tmp.toArray())));
							} else {
								sheet.addCell(new Label(columnIndex++, row, ""));
							}*/
							
							// 开始到处商品信息
							/*OrderDetail[] details = order.getDetails();
							StringBuffer sb = new StringBuffer();
							if (details != null)
								for (OrderDetail detail : details) {
									sb.append(detail.getCtype()).append(":").append(detail.getQuantity()).append(";");
								}
							sheet.addCell(new Label(columnIndex++, row, sb.toString()));//中文货物名称
							sheet.addCell(new Label(columnIndex++, row, order.getQuantity()));//数量
							sheet.addCell(new Label(columnIndex++, row, order.getWeight()));//重量

							sheet.addCell(new Label(columnIndex++, row, order.getcName()));//收件人姓名
							sheet.addCell(new Label(columnIndex++, row, order.getcPhone()));//收件人电话

							sheet.addCell(new Label(columnIndex++, row, order.getcProvince()+order.getcCity()+order.getcDistrict()+order.getcStreetAddress()));//收件人地址
							sheet.addCell(new Label(columnIndex++, row, order.getcCity()));//收件人城市
*/
							//row++;
						//}
					}
				}, "order-datas", null, orders, os);
			}
		}
	
		
		
		// kai 20160120 中华快递导出模版
		public  void exportOrderToZhongHua(List<ExportOrder> orders,
				File templetFile, OutputStream os) throws Exception {
			if (templetFile != null && templetFile.exists()) {
				// 模板复制
				ExcelUtil.exportExcel(new ExcelWrite<ExportOrder>() {
					@Override
					public void write(Map<String, String> headers,
							Collection<ExportOrder> datas, WritableSheet sheet)
							throws Exception {
						if (datas == null || datas.isEmpty()) {
							sheet.mergeCells(0, 1, 11, 1);
							Label cell = new Label(0, 1, "没有用户数据.....");
							sheet.addCell(cell);
							return;
						}
						
						int row = 1;

						for (ExportOrder order : datas) {
							if (order == null) {
								continue;
							}
							
							int columnIndex = 0;
							// sheet.addCell(new Label(columnIndex++, row,
							// order.getId()));// 是用记录编号
							sheet.addCell(new Label(columnIndex++, row, order
									.getOrderId()));// 运单号
							
							sheet.addCell(new Label(columnIndex++, row, ""));//麻袋号
							sheet.addCell(new Label(columnIndex++, row, order.getSeaNo()));//海关单号
							String sendname=order.getSenduserName();
							if(StringUtil.isEmpty(sendname))
							{
								if(order.getUser()!=null)
								{
									if(!StringUtil.isEmpty(order.getUser().getRealName()))
									{
										sendname=order.getUser().getRealName();
									}
								}
								
							}
							sheet.addCell(new Label(columnIndex++, row, sendname));//发件人
							String sendaddr=order.getSenduserAddress();
							if(StringUtil.isEmpty(sendaddr))
							{
								if(order.getUser()!=null)
								{
									
									if(!StringUtil.isEmpty(order.getUser().getAddress()))
									{
										sendaddr=order.getUser().getAddress();
									}
								}
								
							}
							sheet.addCell(new Label(columnIndex++, row, sendaddr));//发件地址
							
							String sendphone=order.getSenduserphone();
							if(StringUtil.isEmpty(sendphone))
							{
								if(order.getUser()!=null)
								{
									
									if(!StringUtil.isEmpty(order.getUser().getPhone()))
									{
										sendphone=order.getUser().getPhone();
									}
								}
								
							}
							sheet.addCell(new Label(columnIndex++, row, sendphone));//发件人电话
							
							sheet.addCell(new Label(columnIndex++, row, order.getcName()));//面单收货人
							
							String revaddress=order.getcProvince()+order.getcCity()+order.getcCity()+order.getcStreetAddress();
							if(StringUtil.isEmpty(revaddress))
							{
								revaddress=order.getAddressAll();
							}
							sheet.addCell(new Label(columnIndex++, row, revaddress));//收件人地址
							
							sheet.addCell(new Label(columnIndex++, row, order.getcPhone()));//面单收货电话
							
							
							
							
							sheet.addCell(new Label(columnIndex++, row, order.getcName()));//收件人名称
							sheet.addCell(new Label(columnIndex++, row, order.getcName()));//收件人名称
							
							sheet.addCell(new Label(columnIndex++, row, revaddress));//收件人地址
							
							sheet.addCell(new Label(columnIndex++, row, order.getcPhone()));//收件人电话
							sheet.addCell(new Label(columnIndex++, row, order.getcProvince()));//收件人省份
							
							
							
							
							// 开始到处商品信息
							OrderDetail[] details = order.getDetails();
							StringBuffer sb = new StringBuffer();
							StringBuffer brands = new StringBuffer();
							//StringBuffer xq = new StringBuffer();
							//int total = 0;
							String[] Comm_type= order.getComm_type();
							if ((details != null)&&(details.length>0))
							{
								if((order.getType()!=null)&&(order.getType().equalsIgnoreCase("7")))
								{
									sheet.addCell(new Label(columnIndex++, row, order.getCommodityThirdList()));// 物品列表
								}
								else
								{
									for (OrderDetail detail : details) {
										for(int nn=0;nn<Comm_type.length;nn++)
										{
											if((Comm_type[nn]!=null)&&(Comm_type[nn].equalsIgnoreCase("0")))//商品类型
											{
												sb.append(detail.getCtype());
											}
											else if((Comm_type[nn]!=null)&&(Comm_type[nn].equalsIgnoreCase("1")))//商品详情
											{
												sb.append(detail.getXiangqing());
											}
											else if((Comm_type[nn]!=null)&&(Comm_type[nn].equalsIgnoreCase("2")))//商品数量
											{
												sb.append(detail.getQuantity());
											}
											else if((Comm_type[nn]!=null)&&(Comm_type[nn].equalsIgnoreCase("3")))//商品重量
											{
												sb.append(detail.getSjweight());
											}
											if((nn!=(Comm_type.length-1)))
											{
												sb.append("*");
											}
										}
										if(StringUtil.isEmpty(detail.getBrands()))
										{
											brands.append(";");
										}
										else
										{
											brands.append(detail.getBrands()+";");
										}
										
										sb.append(";");
										/*sb.append(detail.getCtype()).append("*")
												.append(detail.getQuantity()).append("*")
												.append(detail.getXiangqing())
												.append(";");*/
										//xq.append(detail.getXiangqing()).append(";");
										//total = total
											//	+ Integer.parseInt(detail.getQuantity());
									}
									sheet.addCell(new Label(columnIndex++, row, sb
											.toString()));// 物品名称
									
									
								}
								
						}
							else
							{
								if((order.getType()!=null)&&(order.getType().equalsIgnoreCase("7")))
								{
									sheet.addCell(new Label(columnIndex++, row, order.getCommodityThirdList()));// 物品名称(中文)
									
								}
								else
								{
									sheet.addCell(new Label(columnIndex++, row, ""));// 物品名称(中文)
									
								}
								brands.append(order.getThirdBrands());
							}
							
							sheet.addCell(new Label(columnIndex++, row, order.getParceValue()));// 申报价
							sheet.addCell(new Label(columnIndex++, row, ""));// 数量
							
							String weight=order.getJwweight();
							
							if(StringUtil.isEmpty(weight))
							{
								weight=order.getWeight();
							}
							sheet.addCell(new Label(columnIndex++, row, weight));// 重量
							sheet.addCell(new Label(columnIndex++, row, ""));// 税号
							sheet.addCell(new Label(columnIndex++, row, ""));// 税率
							sheet.addCell(new Label(columnIndex++, row, ""));// 生产厂商
							sheet.addCell(new Label(columnIndex++, row, ""));// 国别
							
							
							sheet.addCell(new Label(columnIndex++, row, brands
									.toString()));// 物品品牌
							sheet.addCell(new Label(columnIndex++, row, ""));// 段数
							sheet.addCell(new Label(columnIndex++, row, ""));// 克数
							
							  //发件人为代理客户，则发件人信息取自订单
							
							row++;
						}
					}
				}, templetFile, orders, os);
			} else {
				// 重新写
				ExcelUtil.exportExcel(new ExcelWrite<ExportOrder>() {
					@Override
					public void write(Map<String, String> headers,
							Collection<ExportOrder> datas, WritableSheet sheet)
							throws Exception {
						if (datas == null || datas.isEmpty()) {
							sheet.mergeCells(0, 1, 11, 1);
							Label cell = new Label(0, 1, "没有用户数据.....");
							sheet.addCell(cell);
							return;
						}
						
						int row = 1;

						for (ExportOrder order : datas) {
							if (order == null) {
								continue;
							}
							
							int columnIndex = 0;
							// sheet.addCell(new Label(columnIndex++, row,
							// order.getId()));// 是用记录编号
							sheet.addCell(new Label(columnIndex++, row, order
									.getOrderId()));// 运单号
							
							sheet.addCell(new Label(columnIndex++, row, ""));//麻袋号
							sheet.addCell(new Label(columnIndex++, row, order.getSeaNo()));//海关单号
							String sendname=order.getSenduserName();
							if(StringUtil.isEmpty(sendname))
							{
								if(order.getUser()!=null)
								{
									if(!StringUtil.isEmpty(order.getUser().getRealName()))
									{
										sendname=order.getUser().getRealName();
									}
								}
								
							}
							sheet.addCell(new Label(columnIndex++, row, sendname));//发件人
							String sendaddr=order.getSenduserAddress();
							if(StringUtil.isEmpty(sendaddr))
							{
								if(order.getUser()!=null)
								{
									
									if(!StringUtil.isEmpty(order.getUser().getAddress()))
									{
										sendaddr=order.getUser().getAddress();
									}
								}
								
							}
							sheet.addCell(new Label(columnIndex++, row, sendaddr));//发件地址
							
							String sendphone=order.getSenduserphone();
							if(StringUtil.isEmpty(sendphone))
							{
								if(order.getUser()!=null)
								{
									
									if(!StringUtil.isEmpty(order.getUser().getPhone()))
									{
										sendphone=order.getUser().getPhone();
									}
								}
								
							}
							sheet.addCell(new Label(columnIndex++, row, sendphone));//发件人电话
							
							sheet.addCell(new Label(columnIndex++, row, order.getcName()));//面单收货人
							
							String revaddress=order.getcProvince()+order.getcCity()+order.getcCity()+order.getcStreetAddress();
							if(StringUtil.isEmpty(revaddress))
							{
								revaddress=order.getAddressAll();
							}
							sheet.addCell(new Label(columnIndex++, row, revaddress));//收件人地址
							
							sheet.addCell(new Label(columnIndex++, row, order.getcPhone()));//面单收货电话
							
							
							
							
							sheet.addCell(new Label(columnIndex++, row, order.getcName()));//收件人名称
							sheet.addCell(new Label(columnIndex++, row, order.getcName()));//收件人名称
							
							sheet.addCell(new Label(columnIndex++, row, revaddress));//收件人地址
							
							sheet.addCell(new Label(columnIndex++, row, order.getcPhone()));//收件人电话
							sheet.addCell(new Label(columnIndex++, row, order.getcProvince()));//收件人省份
							
							
							
							
							// 开始到处商品信息
							OrderDetail[] details = order.getDetails();
							StringBuffer sb = new StringBuffer();
							StringBuffer brands = new StringBuffer();
							//StringBuffer xq = new StringBuffer();
							//int total = 0;
							String[] Comm_type= order.getComm_type();
							if ((details != null)&&(details.length>0))
							{
								if((order.getType()!=null)&&(order.getType().equalsIgnoreCase("7")))
								{
									sheet.addCell(new Label(columnIndex++, row, order.getCommodityThirdList()));// 物品列表
								}
								else
								{
									for (OrderDetail detail : details) {
										for(int nn=0;nn<Comm_type.length;nn++)
										{
											if((Comm_type[nn]!=null)&&(Comm_type[nn].equalsIgnoreCase("0")))//商品类型
											{
												sb.append(detail.getCtype());
											}
											else if((Comm_type[nn]!=null)&&(Comm_type[nn].equalsIgnoreCase("1")))//商品详情
											{
												sb.append(detail.getXiangqing());
											}
											else if((Comm_type[nn]!=null)&&(Comm_type[nn].equalsIgnoreCase("2")))//商品数量
											{
												sb.append(detail.getQuantity());
											}
											else if((Comm_type[nn]!=null)&&(Comm_type[nn].equalsIgnoreCase("3")))//商品重量
											{
												sb.append(detail.getSjweight());
											}
											if((nn!=(Comm_type.length-1)))
											{
												sb.append("*");
											}
										}
										if(StringUtil.isEmpty(detail.getBrands()))
										{
											brands.append(";");
										}
										else
										{
											brands.append(detail.getBrands()+";");
										}
										sb.append(";");
										/*sb.append(detail.getCtype()).append("*")
												.append(detail.getQuantity()).append("*")
												.append(detail.getXiangqing())
												.append(";");*/
										//xq.append(detail.getXiangqing()).append(";");
										//total = total
											//	+ Integer.parseInt(detail.getQuantity());
									}
									sheet.addCell(new Label(columnIndex++, row, sb
											.toString()));// 物品名称
									
									
								}
								
						}
							else
							{
								if((order.getType()!=null)&&(order.getType().equalsIgnoreCase("7")))
								{
									sheet.addCell(new Label(columnIndex++, row, order.getCommodityThirdList()));// 物品名称(中文)
									
								}
								else
								{
									sheet.addCell(new Label(columnIndex++, row, ""));// 物品名称(中文)
									
								}
								brands.append(order.getThirdBrands());
							}
							
							sheet.addCell(new Label(columnIndex++, row, order.getParceValue()));// 申报价
							sheet.addCell(new Label(columnIndex++, row, ""));// 数量
							sheet.addCell(new Label(columnIndex++, row, order.getWeight()));// 重量
							sheet.addCell(new Label(columnIndex++, row, ""));// 税号
							sheet.addCell(new Label(columnIndex++, row, ""));// 税率
							sheet.addCell(new Label(columnIndex++, row, ""));// 生产厂商
							sheet.addCell(new Label(columnIndex++, row, ""));// 国别
							
							
							sheet.addCell(new Label(columnIndex++, row, brands
									.toString()));// 物品品牌
							sheet.addCell(new Label(columnIndex++, row, ""));// 段数
							sheet.addCell(new Label(columnIndex++, row, ""));// 克数
							
							  //发件人为代理客户，则发件人信息取自订单
							
							row++;
						}
					}
				}, "order-datas", null, orders, os);
			}
		}
		
		
		
		
		// kai 20151123美淘快递的定单导出
				public  void exportOrderToMeitao20151123(List<ExportOrder> orders,
						File templetFile, OutputStream os) throws Exception {
					if (templetFile != null && templetFile.exists()) {
						// 模板复制
						ExcelUtil.exportExcel(new ExcelWrite<ExportOrder>() {
							@Override
							public void write(Map<String, String> headers,
									Collection<ExportOrder> datas, WritableSheet sheet)
									throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有用户数据.....");
									sheet.addCell(cell);
									return;
								}
								
								int row = 1;

								for (ExportOrder order : datas) {
									if (order == null) {
										continue;
									}
									
									int columnIndex = 0;
									// sheet.addCell(new Label(columnIndex++, row,
									// order.getId()));// 是用记录编号
									sheet.addCell(new Label(columnIndex++, row, order
											.getOrderId()));// 运单号
									
									sheet.addCell(new Label(columnIndex++, row, order.getWeight()));//重量
									sheet.addCell(new Label(columnIndex++, row, order.getJwweight()));//实际重量
									sheet.addCell(new Label(columnIndex++, row, "1"));//箱数
									// 开始到处商品信息
									OrderDetail[] details = order.getDetails();
									StringBuffer sb = new StringBuffer();
									StringBuffer brands = new StringBuffer();
									//StringBuffer xq = new StringBuffer();
									int total = 0;
									String[] Comm_type= order.getComm_type();
									if ((details != null)&&(details.length>0))
									{
										if((order.getType()!=null)&&(order.getType().equalsIgnoreCase("7")))
										{
											sheet.addCell(new Label(columnIndex++, row, order.getCommodityThirdList()));// 物品列表
										}
										else
										{
											for (OrderDetail detail : details) {
												for(int nn=0;nn<Comm_type.length;nn++)
												{
													if((Comm_type[nn]!=null)&&(Comm_type[nn].equalsIgnoreCase("0")))//商品类型
													{
														sb.append(detail.getCtype());
													}
													else if((Comm_type[nn]!=null)&&(Comm_type[nn].equalsIgnoreCase("1")))//商品详情
													{
														sb.append(detail.getXiangqing());
													}
													else if((Comm_type[nn]!=null)&&(Comm_type[nn].equalsIgnoreCase("2")))//商品数量
													{
														sb.append(detail.getQuantity());
													}
													else if((Comm_type[nn]!=null)&&(Comm_type[nn].equalsIgnoreCase("3")))//商品重量
													{
														sb.append(detail.getSjweight());
													}
													if((nn!=(Comm_type.length-1)))
													{
														sb.append("：");
													}
												}
												brands.append(detail.getBrands()+";");
												sb.append(";");
												/*sb.append(detail.getCtype()).append("*")
														.append(detail.getQuantity()).append("*")
														.append(detail.getXiangqing())
														.append(";");*/
												//xq.append(detail.getXiangqing()).append(";");
												total = total
														+ Integer.parseInt(detail.getQuantity());
											}
											sheet.addCell(new Label(columnIndex++, row, sb
													.toString()));// 物品名称
											
											
										}
										
								}
									else
									{
										if((order.getType()!=null)&&(order.getType().equalsIgnoreCase("7")))
										{
											sheet.addCell(new Label(columnIndex++, row, order.getCommodityThirdList()));// 物品名称(中文)
											
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row, ""));// 物品名称(中文)
											
										}
										brands.append(order.getThirdBrands());
									}
									
									sheet.addCell(new Label(columnIndex++, row, brands
											.toString()));// 物品品牌
									  //发件人为代理客户，则发件人信息取自订单
									if((order.getUser()!=null)&&(order.getUser().getType()!=null)&&(order.getUser().getType().equalsIgnoreCase("5")))
									{
										sheet.addCell(new Label(columnIndex++, row, order.getSenduserName())); // 发件人
										sheet.addCell(new Label(columnIndex++, row, order.getSenduserphone())); // 发件人联系电话
										sheet.addCell(new Label(columnIndex++, row, "")); // 发件人地址
										sheet.addCell(new Label(columnIndex++, row, "")); // 发件人城市	
										sheet.addCell(new Label(columnIndex++, row, "")); // 发件人州名	
										sheet.addCell(new Label(columnIndex++, row, "")); // 发件人邮编	
									}
									else
									{
										if(order.getUser()==null)
										{
											if((order.getType()!=null)&&(order.getType().equalsIgnoreCase("7")))
											{
												sheet.addCell(new Label(columnIndex++, row, order.getSenduserName())); // 发件人
												sheet.addCell(new Label(columnIndex++, row, order.getSenduserphone())); // 发件人联系电话
												sheet.addCell(new Label(columnIndex++, row, order.getSenduserAddress())); // 发件人地址
												
												sheet.addCell(new Label(columnIndex++, row, order.getSenduserCity())); // 发件人城市	
												sheet.addCell(new Label(columnIndex++, row, order.getSenduserState())); // 发件人州名	
												sheet.addCell(new Label(columnIndex++, row, order.getSenduserZipcode())); // 发件人邮编	
											}
											else
											{
												sheet.addCell(new Label(columnIndex++, row, "")); // 发件人
												sheet.addCell(new Label(columnIndex++, row, "")); // 发件人联系电话
												sheet.addCell(new Label(columnIndex++, row, "")); // 发件人地址
												sheet.addCell(new Label(columnIndex++, row, "")); // 发件人城市	
												sheet.addCell(new Label(columnIndex++, row, "")); // 发件人州名	
												sheet.addCell(new Label(columnIndex++, row, "")); // 发件人邮编	
											}
										}
										else
										{
											
											if((order.getType()!=null)&&(order.getType().equalsIgnoreCase("7")))
											{
												sheet.addCell(new Label(columnIndex++, row, order.getSenduserName())); // 发件人
												sheet.addCell(new Label(columnIndex++, row, order.getSenduserphone())); // 发件人联系电话
												sheet.addCell(new Label(columnIndex++, row, order.getSenduserAddress())); // 发件人地址
												
												sheet.addCell(new Label(columnIndex++, row, order.getSenduserCity())); // 发件人城市	
												sheet.addCell(new Label(columnIndex++, row, order.getSenduserState())); // 发件人州名	
												sheet.addCell(new Label(columnIndex++, row, order.getSenduserZipcode())); // 发件人邮编	
											}
											else
											{
												sheet.addCell(new Label(columnIndex++, row, order
														.getUser().getRealName())); // 发件人
												sheet.addCell(new Label(columnIndex++, row, order
														.getUser().getPhone())); // 发件人联系电话
												sheet.addCell(new Label(columnIndex++, row, order.getUser().getAddress()));// 发件人地址
												sheet.addCell(new Label(columnIndex++, row, "")); // 发件人城市	
												sheet.addCell(new Label(columnIndex++, row, "")); // 发件人州名	
												sheet.addCell(new Label(columnIndex++, row, "")); // 发件人邮编
											}
										}
									}
									sheet.addCell(new Label(columnIndex++, row, order
											.getcName()));// 收件人姓名
									sheet.addCell(new Label(columnIndex++, row, order.getcProvince()));// 收件人所在省份
									sheet.addCell(new Label(columnIndex++, row, order.getcCity()));// 收件人所在市
									sheet.addCell(new Label(columnIndex++, row, order.getcDistrict()));// 收件人所在区
									sheet.addCell(new Label(columnIndex++, row, order.getcStreetAddress()));// 收件人详细地址
									sheet.addCell(new Label(columnIndex++, row, order.getcPhone()));// 收件人电话
									sheet.addCell(new Label(columnIndex++, row, order.getcZipCode()));// 收件人邮编
									
								
								
									double totalmoney = 0.0;
									double premium = 0.0;// 保险费
									double tariff = 0.0;// 关税
									double basemoney = 0.0;
									double CommodityCost=0.0;

									// float
									// premium=Float.parseFloat(order.getPremium());//保险费
									// float tariff=Float.parseFloat(order.getTariff());//关税
									// float basemoney=totalmoney-premium-tariff;
									if ((order.getTotalMoney() == null)
											|| (order.getTotalMoney().equals(""))) {

									} else {
										totalmoney = Float
												.parseFloat(order.getTotalMoney());

									}
									
									if ((order.getPremium() == null)
											|| (order.getPremium().equals(""))) {

									} else {
										premium = Float.parseFloat(order.getPremium());// 保险费

									}
									if ((order.getTariff() == null)
											|| (order.getTariff().equals(""))) {

									} else {

										tariff = Float.parseFloat(order.getTariff());// 关税

									}
									if((order.getType()!=null)&&(order.getType().equalsIgnoreCase("7")))
									{
										if ((order.getBaseMoney() == null)
												|| (order.getBaseMoney().equals(""))) {
											basemoney=0.0;
										}
										else{
										basemoney=Float.parseFloat(order.getBaseMoney());
										}
									}
									else
									{
										basemoney = totalmoney - premium - tariff;
									}
									if ((order.getCommodityCost() == null)//取成本
											|| (order.getCommodityCost().equals(""))) {

									} else {
										CommodityCost = Float
												.parseFloat(order.getCommodityCost());
										BigDecimal   bd  =   new  BigDecimal((double)CommodityCost);    
										bd   =  bd.setScale(2,4);    
										CommodityCost   =  bd.doubleValue();  

									}
									/*sheet.addCell(new Label(columnIndex++, row, String
											.valueOf(basemoney)));// 基本运费$
									sheet.addCell(new Label(columnIndex++, row, order
											.getExpressName()));// 快递公司
									sheet.addCell(new Label(columnIndex++, row, String
											.valueOf(premium)));// 保险￥
									sheet.addCell(new Label(columnIndex++, row, String
											.valueOf(tariff)));// 关税$*/
									sheet.addCell(new Label(columnIndex++, row, String
											.valueOf(CommodityCost)));// 费用成本
									sheet.addCell(new Label(columnIndex++, row, String
											.valueOf(totalmoney)));// 费用合计

									sheet.addCell(new Label(columnIndex++, row, String
											.valueOf(order.getState())));// 状态
									row++;
								}
							}
						}, templetFile, orders, os);
					} else {
						// 重新写
						ExcelUtil.exportExcel(new ExcelWrite<ExportOrder>() {
							@Override
							public void write(Map<String, String> headers,
									Collection<ExportOrder> datas, WritableSheet sheet)
									throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有用户数据.....");
									sheet.addCell(cell);
									return;
								}
								
								int row = 1;

								for (ExportOrder order : datas) {
									if (order == null) {
										continue;
									}
									
									int columnIndex = 0;
									// sheet.addCell(new Label(columnIndex++, row,
									// order.getId()));// 是用记录编号
									sheet.addCell(new Label(columnIndex++, row, order
											.getOrderId()));// 运单号
									
									sheet.addCell(new Label(columnIndex++, row, order.getWeight()));//重量
									sheet.addCell(new Label(columnIndex++, row, order.getJwweight()));//实际重量
									sheet.addCell(new Label(columnIndex++, row, "1"));//箱数
									// 开始到处商品信息
									OrderDetail[] details = order.getDetails();
									StringBuffer sb = new StringBuffer();
									StringBuffer brands = new StringBuffer();
									//StringBuffer xq = new StringBuffer();
									int total = 0;
									String[] Comm_type= order.getComm_type();
									if ((details != null)&&(details.length>0))
									{
										if((order.getType()!=null)&&(order.getType().equalsIgnoreCase("7")))
										{
											sheet.addCell(new Label(columnIndex++, row, order.getCommodityThirdList()));// 物品列表
										}
										else
										{
											for (OrderDetail detail : details) {
												for(int nn=0;nn<Comm_type.length;nn++)
												{
													if((Comm_type[nn]!=null)&&(Comm_type[nn].equalsIgnoreCase("0")))//商品类型
													{
														sb.append(detail.getCtype());
													}
													else if((Comm_type[nn]!=null)&&(Comm_type[nn].equalsIgnoreCase("1")))//商品详情
													{
														sb.append(detail.getXiangqing());
													}
													else if((Comm_type[nn]!=null)&&(Comm_type[nn].equalsIgnoreCase("2")))//商品数量
													{
														sb.append(detail.getQuantity());
													}
													else if((Comm_type[nn]!=null)&&(Comm_type[nn].equalsIgnoreCase("3")))//商品重量
													{
														sb.append(detail.getSjweight());
													}
													if((nn!=(Comm_type.length-1)))
													{
														sb.append("：");
													}
												}
												brands.append(detail.getBrands()+";");
												sb.append(";");
												/*sb.append(detail.getCtype()).append("*")
														.append(detail.getQuantity()).append("*")
														.append(detail.getXiangqing())
														.append(";");*/
												//xq.append(detail.getXiangqing()).append(";");
												total = total
														+ Integer.parseInt(detail.getQuantity());
											}
											sheet.addCell(new Label(columnIndex++, row, sb
													.toString()));// 物品名称
											
											
										}
										
								}
									else
									{
										if((order.getType()!=null)&&(order.getType().equalsIgnoreCase("7")))
										{
											sheet.addCell(new Label(columnIndex++, row, order.getCommodityThirdList()));// 物品名称(中文)
											
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row, ""));// 物品名称(中文)
											
										}
										brands.append(order.getThirdBrands());
									}
									
									sheet.addCell(new Label(columnIndex++, row, brands
											.toString()));// 物品品牌
									  //发件人为代理客户，则发件人信息取自订单
									if((order.getUser()!=null)&&(order.getUser().getType()!=null)&&(order.getUser().getType().equalsIgnoreCase("5")))
									{
										sheet.addCell(new Label(columnIndex++, row, order.getSenduserName())); // 发件人
										sheet.addCell(new Label(columnIndex++, row, order.getSenduserphone())); // 发件人联系电话
										sheet.addCell(new Label(columnIndex++, row, "")); // 发件人地址
										sheet.addCell(new Label(columnIndex++, row, "")); // 发件人城市	
										sheet.addCell(new Label(columnIndex++, row, "")); // 发件人州名	
										sheet.addCell(new Label(columnIndex++, row, "")); // 发件人邮编	
									}
									else
									{
										if(order.getUser()==null)
										{
											if((order.getType()!=null)&&(order.getType().equalsIgnoreCase("7")))
											{
												sheet.addCell(new Label(columnIndex++, row, order.getSenduserName())); // 发件人
												sheet.addCell(new Label(columnIndex++, row, order.getSenduserphone())); // 发件人联系电话
												sheet.addCell(new Label(columnIndex++, row, order.getSenduserAddress())); // 发件人地址
												
												sheet.addCell(new Label(columnIndex++, row, order.getSenduserCity())); // 发件人城市	
												sheet.addCell(new Label(columnIndex++, row, order.getSenduserState())); // 发件人州名	
												sheet.addCell(new Label(columnIndex++, row, order.getSenduserZipcode())); // 发件人邮编	
											}
											else
											{
												sheet.addCell(new Label(columnIndex++, row, "")); // 发件人
												sheet.addCell(new Label(columnIndex++, row, "")); // 发件人联系电话
												sheet.addCell(new Label(columnIndex++, row, "")); // 发件人地址
												sheet.addCell(new Label(columnIndex++, row, "")); // 发件人城市	
												sheet.addCell(new Label(columnIndex++, row, "")); // 发件人州名	
												sheet.addCell(new Label(columnIndex++, row, "")); // 发件人邮编	
											}
										}
										else
										{
											
											if((order.getType()!=null)&&(order.getType().equalsIgnoreCase("7")))
											{
												sheet.addCell(new Label(columnIndex++, row, order.getSenduserName())); // 发件人
												sheet.addCell(new Label(columnIndex++, row, order.getSenduserphone())); // 发件人联系电话
												sheet.addCell(new Label(columnIndex++, row, order.getSenduserAddress())); // 发件人地址
												
												sheet.addCell(new Label(columnIndex++, row, order.getSenduserCity())); // 发件人城市	
												sheet.addCell(new Label(columnIndex++, row, order.getSenduserState())); // 发件人州名	
												sheet.addCell(new Label(columnIndex++, row, order.getSenduserZipcode())); // 发件人邮编	
											}
											else
											{
												sheet.addCell(new Label(columnIndex++, row, order
														.getUser().getRealName())); // 发件人
												sheet.addCell(new Label(columnIndex++, row, order
														.getUser().getPhone())); // 发件人联系电话
												sheet.addCell(new Label(columnIndex++, row, order.getUser().getAddress()));// 发件人地址
												sheet.addCell(new Label(columnIndex++, row, "")); // 发件人城市	
												sheet.addCell(new Label(columnIndex++, row, "")); // 发件人州名	
												sheet.addCell(new Label(columnIndex++, row, "")); // 发件人邮编
											}
										}
									}
									sheet.addCell(new Label(columnIndex++, row, order
											.getcName()));// 收件人姓名
									sheet.addCell(new Label(columnIndex++, row, order.getcProvince()));// 收件人所在省份
									sheet.addCell(new Label(columnIndex++, row, order.getcCity()));// 收件人所在市
									sheet.addCell(new Label(columnIndex++, row, order.getcDistrict()));// 收件人所在区
									sheet.addCell(new Label(columnIndex++, row, order.getcStreetAddress()));// 收件人详细地址
									sheet.addCell(new Label(columnIndex++, row, order.getcPhone()));// 收件人电话
									sheet.addCell(new Label(columnIndex++, row, order.getcZipCode()));// 收件人邮编
									
								
								
									double totalmoney = 0.0;
									double premium = 0.0;// 保险费
									double tariff = 0.0;// 关税
									double basemoney = 0.0;
									double CommodityCost=0.0;

									// float
									// premium=Float.parseFloat(order.getPremium());//保险费
									// float tariff=Float.parseFloat(order.getTariff());//关税
									// float basemoney=totalmoney-premium-tariff;
									if ((order.getTotalMoney() == null)
											|| (order.getTotalMoney().equals(""))) {

									} else {
										totalmoney = Float
												.parseFloat(order.getTotalMoney());

									}
									
									if ((order.getPremium() == null)
											|| (order.getPremium().equals(""))) {

									} else {
										premium = Float.parseFloat(order.getPremium());// 保险费

									}
									if ((order.getTariff() == null)
											|| (order.getTariff().equals(""))) {

									} else {

										tariff = Float.parseFloat(order.getTariff());// 关税

									}
									if((order.getType()!=null)&&(order.getType().equalsIgnoreCase("7")))
									{
										if ((order.getBaseMoney() == null)
												|| (order.getBaseMoney().equals(""))) {
											basemoney=0.0;
										}
										else{
										basemoney=Float.parseFloat(order.getBaseMoney());
										}
									}
									else
									{
										basemoney = totalmoney - premium - tariff;
									}
									if ((order.getCommodityCost() == null)//取成本
											|| (order.getCommodityCost().equals(""))) {

									} else {
										CommodityCost = Float
												.parseFloat(order.getCommodityCost());
										BigDecimal   bd  =   new  BigDecimal((double)CommodityCost);    
										bd   =  bd.setScale(2,4);    
										CommodityCost   =  bd.doubleValue();  

									}
									/*sheet.addCell(new Label(columnIndex++, row, String
											.valueOf(basemoney)));// 基本运费$
									sheet.addCell(new Label(columnIndex++, row, order
											.getExpressName()));// 快递公司
									sheet.addCell(new Label(columnIndex++, row, String
											.valueOf(premium)));// 保险￥
									sheet.addCell(new Label(columnIndex++, row, String
											.valueOf(tariff)));// 关税$*/
									sheet.addCell(new Label(columnIndex++, row, String
											.valueOf(CommodityCost)));// 费用成本
									sheet.addCell(new Label(columnIndex++, row, String
											.valueOf(totalmoney)));// 费用合计

									sheet.addCell(new Label(columnIndex++, row, String
											.valueOf(order.getState())));// 状态
									row++;
								}
							}
						}, "order-datas", null, orders, os);
					}
				}
				
		
		// kai 20151123 导入美淘第三方数据
		public static List<Order> readOrderFromMeitaothirdExcel(InputStream input)
				throws Exception {
			return ExcelUtil.importExcel(new ExcelReader<Order>() {
				@Override
				public List<Order> read(Sheet sheet) throws Exception {
					List<Order> list = new ArrayList<Order>();
					int rows = sheet.getRows();
					if (rows < 2) {
						throw new RuntimeException("运单文件不能为空！");
					}
					for (int i = 1; i < rows; i++) {

						int j = 0;
						
						Order order = new Order();
						//先把所有的数据，不管对错，先保存到缓存中
						//String isno = StringUtil.getValue(sheet.getCell(j++, i)
						//		.getContents(), ""); // 序号
						//order.setId(isno);//仅仅保存数据，插入的时候不会使用，方便返回错误时显示内容
						
						
					//	order.setThirdCreateDate(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//第三方收件日期
						
						
						order.setOrderId(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//第三方订单号
						
						if(StringUtil.isEmpty(order.getOrderId()))
						{
							break;//如果运单号为空，即跳出。
						}
						else
						{
							order.setOrderId(order.getOrderId().toUpperCase());
						}
						
						order.setWeight(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//重量
						j++;//箱数
						order.setCommodityThirdList(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//发件人的商品清单
						order.setThirdBrands(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//发件人的商品品牌
						order.setSenduserName(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//第三方导入是的发件人用户名
						order.setSenduserphone(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//第三方联系电话
						order.setSenduserAddress(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//第三方联系地址
						order.setSenduserCity(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//城市
						order.setSenduserState(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//州
						order.setSenduserZipcode(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//发送人邮篇
						
						order.setcName(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//收件人姓名
						order.setcProvince(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//收件人所在省
						order.setcCity(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//收件人所在市
						order.setcDistrict(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//收件人所在区
						order.setcStreetAddress(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//收件人所在地址
						order.setcPhone(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//收件人电话
						order.setcZipCode(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//收件人邮编
						order.setCommodityCost(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//成本
						order.setTotalMoney(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//总费用
						order.setState(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//状态
						
						
						
						list.add(order);
					}
					return list;
				}
			}, input);
		}
		
		//当读取第三方数据时，出错返回数据
				public static void exportOrderTocheckthirdExcelmeitao(List<ImportthirdOrder> orders, File templetFile, OutputStream os) throws Exception {
					if (templetFile != null && templetFile.exists()) {
						// 模板复制
						ExcelUtil.exportExcel(new ExcelWrite<ImportthirdOrder>() {
							@Override
							public void write(Map<String, String> headers, Collection<ImportthirdOrder> datas, WritableSheet sheet)
							        throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有用户数据.....");
									sheet.addCell(cell);
									return;
								}

								int row = 1;
								
								for (ImportthirdOrder iorder : datas)
								{
									WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
						                    10, WritableFont.NO_BOLD);// 字体样式
									
									WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
									redcolor.setBackground(Colour.RED);
									
									WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
									oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
									String[] orderflag=iorder.getOrderflag();
									//String[] userflag=iorder.getUserflag();
									for (Order order : iorder.getOrders()) {
										int columnIndex = 0;
										
										if((orderflag[row-1]!=null)&&(orderflag[row-1].equalsIgnoreCase("0")))
										{
											sheet.addCell(new Label(columnIndex++, row,order.getOrderId()));// 第三方订单号不存在
										}
										else if((orderflag[row-1]!=null)&&(orderflag[row-1].equalsIgnoreCase("1")))
										{
											sheet.addCell(new Label(columnIndex++, row,order.getOrderId(),redcolor));// 第三方订单号 存在
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row,order.getOrderId(),oceanbluecolor));// 第三方订单号 异常
										}
										
										sheet.addCell(new Label(columnIndex++, row,order.getWeight()));//重量
										sheet.addCell(new Label(columnIndex++, row,"1"));//箱数
										sheet.addCell(new Label(columnIndex++, row,order.getCommodityThirdList()));// 发件人的商品清单
										sheet.addCell(new Label(columnIndex++, row,order.getThirdBrands()));// 发件人的商品品牌
										sheet.addCell(new Label(columnIndex++, row,order.getSenduserName()));// 第三方导入是的发件人用户名
										sheet.addCell(new Label(columnIndex++, row,order.getSenduserphone()));// 第三方联系电话
										sheet.addCell(new Label(columnIndex++, row,order.getSenduserAddress()));// 第三方联系地址
										sheet.addCell(new Label(columnIndex++, row,order.getSenduserCity()));// 城市
										sheet.addCell(new Label(columnIndex++, row,order.getSenduserState()));//州
										sheet.addCell(new Label(columnIndex++, row,order.getSenduserZipcode()));//发送人邮篇
										sheet.addCell(new Label(columnIndex++, row,order.getcName()));//收件人姓名
										sheet.addCell(new Label(columnIndex++, row,order.getcProvince()));//收件人所在省
										sheet.addCell(new Label(columnIndex++, row,order.getcCity()));//城市
										sheet.addCell(new Label(columnIndex++, row,order.getcDistrict()));///收件人所在区
										sheet.addCell(new Label(columnIndex++, row,order.getcStreetAddress()));///收件人所在地址
										sheet.addCell(new Label(columnIndex++, row,order.getcPhone()));//收件人电话
										sheet.addCell(new Label(columnIndex++, row,order.getcZipCode()));//收件人邮编
										sheet.addCell(new Label(columnIndex++, row,order.getCommodityCost()));//成本
										sheet.addCell(new Label(columnIndex++, row,order.getTotalMoney()));//总费用
										sheet.addCell(new Label(columnIndex++, row,order.getState()));//状态
										
										
										row++;
									}
								}
							}
						}, templetFile, orders, os);
					} else {
						// 重新写
						ExcelUtil.exportExcel(new ExcelWrite<ImportthirdOrder>() {
							@Override
							public void write(Map<String, String> headers, Collection<ImportthirdOrder> datas, WritableSheet sheet)
							        throws Exception {
								
								//int row = 1;
							//	for (Order order : datas) {
								//	int columnIndex = 0;
									// sheet.addCell(new Label(columnIndex++, row,
									// order.getId()));// 是用记录编号
								/*	sheet.addCell(new Label(columnIndex++, row,""));// 入仓号  
									sheet.addCell(new Label(columnIndex++, row,""));// 箱号   
									sheet.addCell(new Label(columnIndex++, row,""));// 卡板号   
									sheet.addCell(new Label(columnIndex++, row, order.getOrderId())); // 运单号
									sheet.addCell(new Label(columnIndex++, row,""));// 英文品牌*/
									/*tmp = order.getOrderDetailTranshipmentIds();
									if (tmp != null && tmp.size() > 0) {
										sheet.addCell(new Label(columnIndex++, row, Arrays.toString(tmp.toArray())));
									} else {
										sheet.addCell(new Label(columnIndex++, row, ""));
									}*/
									
									// 开始到处商品信息
									/*OrderDetail[] details = order.getDetails();
									StringBuffer sb = new StringBuffer();
									if (details != null)
										for (OrderDetail detail : details) {
											sb.append(detail.getCtype()).append(":").append(detail.getQuantity()).append(";");
										}
									sheet.addCell(new Label(columnIndex++, row, sb.toString()));//中文货物名称
									sheet.addCell(new Label(columnIndex++, row, order.getQuantity()));//数量
									sheet.addCell(new Label(columnIndex++, row, order.getWeight()));//重量

									sheet.addCell(new Label(columnIndex++, row, order.getcName()));//收件人姓名
									sheet.addCell(new Label(columnIndex++, row, order.getcPhone()));//收件人电话

									sheet.addCell(new Label(columnIndex++, row, order.getcProvince()+order.getcCity()+order.getcDistrict()+order.getcStreetAddress()));//收件人地址
									sheet.addCell(new Label(columnIndex++, row, order.getcCity()));//收件人城市
		*/
									//row++;
								//}
							}
						}, "order-datas", null, orders, os);
					}
				}
				public static List<Order>	orders_clear(List<Order> orders)
				{
					//List<Order> list=null;
					for(Order order : orders)
					{
						if(StringUtil.isEmpty(order.getWeight()))
						{
							order.setWeight("0");						
						}
						if(StringUtil.isEmpty(order.getWeightKg()))
						{
							order.setWeightKg("0");;						
						}
						if(StringUtil.isEmpty(order.getPremium()))
						{
							order.setPremium("0");
						}
						if(StringUtil.isEmpty(order.getTotalMoney()))
						{
							order.setTotalMoney("0");
						}
						if(StringUtil.isEmpty(order.getState()))
						{
							order.setState("2");;
						}
						if(StringUtil.isEmpty(order.getLength()))
						{
							order.setLength("0");
						}
						if(StringUtil.isEmpty(order.getHeight()))
						{
							order.setHeight("0");
						}
						if(StringUtil.isEmpty(order.getWidth()))
						{
							order.setWidth("0");
						}
						if(StringUtil.isEmpty(order.getBaseMoney()))
						{
							order.setBaseMoney("0");
						}
						
					}
					
					return orders;
				}
				
				
				
				// kai 20151027美淘快递的定单导出
				public static void exportOrderStateToResult(List<ImportOrder> orders,
						File templetFile, OutputStream os) throws Exception {
					if (templetFile != null && templetFile.exists()) {
						// 模板复制
						ExcelUtil.exportExcel(new ExcelWrite<ImportOrder>() {
							@Override
							public void write(Map<String, String> headers,
									Collection<ImportOrder> datas, WritableSheet sheet)
									throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有用户数据.....");
									sheet.addCell(cell);
									return;
								}
								
								int row = 1;

								for (ImportOrder order : datas) {
									if (order == null) {
										continue;
									}
									WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
						                    10, WritableFont.NO_BOLD);// 字体样式
									
									WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
									redcolor.setBackground(Colour.RED);
									
									WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
									oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
									int columnIndex = 0;
									// sheet.addCell(new Label(columnIndex++, row,
									// order.getId()));// 是用记录编号
									sheet.addCell(new Label(columnIndex++, row, String
											.valueOf(row)));// 序号
									
									sheet.addCell(new Label(columnIndex++, row, order.getOrderId()));//运单号
									sheet.addCell(new Label(columnIndex++, row, order.getState()));//状态值
									sheet.addCell(new Label(columnIndex++, row, order.getStateremark()));//状态说明
									sheet.addCell(new Label(columnIndex++, row, order.getThirdPNS()));//状态说明
									sheet.addCell(new Label(columnIndex++, row, order.getThirdNo()));//状态说明
									if(!StringUtil.isEmpty(order.getStateResult())&&(order.getStateResult().indexOf("成功")!=-1))//成功信息
									{
										sheet.addCell(new Label(columnIndex++, row, order.getStateResult()));//结果
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, order.getStateResult(),redcolor));//结果
									}
									
									

									row++;
								}
							}
						}, templetFile, orders, os);
					} else {
						// 重新写
						ExcelUtil.exportExcel(new ExcelWrite<ImportOrder>() {
							@Override
							public void write(Map<String, String> headers,
									Collection<ImportOrder> datas, WritableSheet sheet)
									throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有用户数据.....");
									sheet.addCell(cell);
									return;
								}
								
								int row = 1;

								for (ImportOrder order : datas) {
									if (order == null) {
										continue;
									}
									WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
						                    10, WritableFont.NO_BOLD);// 字体样式
									
									WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
									redcolor.setBackground(Colour.RED);
									
									WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
									oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
									int columnIndex = 0;
									// sheet.addCell(new Label(columnIndex++, row,
									// order.getId()));// 是用记录编号
									sheet.addCell(new Label(columnIndex++, row, String
											.valueOf(row)));// 序号
									
									sheet.addCell(new Label(columnIndex++, row, order.getOrderId()));//运单号
									sheet.addCell(new Label(columnIndex++, row, order.getState()));//状态值
									sheet.addCell(new Label(columnIndex++, row, order.getStateremark()));//状态说明
									sheet.addCell(new Label(columnIndex++, row, order.getThirdPNS()));//状态说明
									sheet.addCell(new Label(columnIndex++, row, order.getThirdNo()));//状态说明
									if(!StringUtil.isEmpty(order.getStateResult())&&(order.getStateResult().indexOf("成功")!=-1))//成功信息
									{
										sheet.addCell(new Label(columnIndex++, row, order.getStateResult()));//结果
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, order.getStateResult(),redcolor));//结果
									}
									
									

									row++;
								}
							}
						}, "order-datas", null, orders, os);
					}
				}
				
				
				
				//kai 20150119导入海关对应号码，系统运单<->海关单号
				public static List<ImportOrder> readOrderSeaTaxExcel1(InputStream input)
						throws Exception {
					return ExcelUtil.importExcel(new ExcelReader<ImportOrder>() {
						@Override
						public List<ImportOrder> read(Sheet sheet) throws Exception {
							List<ImportOrder> list = new ArrayList<ImportOrder>();
							int rows = sheet.getRows();
							for (int i = 0; i < rows; i++) {
								int j = 0;
								
								
								ImportOrder io = new ImportOrder();
								String orderid=sheet.getCell(j++, i).getContents();
								if((orderid!=null)&&(!orderid.equalsIgnoreCase("")))
								{
									io.setOrderId(orderid.trim());//运单号
									String seataxno = sheet.getCell(j++, i).getContents();//运单状态
									if((seataxno==null)||(seataxno.trim().equalsIgnoreCase("")))
									{
										io.setSeataxno("");
									}
									else
									{
										io.setSeataxno(seataxno.trim());
									}

								}
								else
								{
									break;
								}
								
								
								list.add(io);
							}
							return list;
						}
					}, input);
				}
				
				
				// kai 10160119 导出修改海关单号修改结果
				public static void exportOrderSeaNoModifyToResult(List<ImportOrder> orders,
						File templetFile, OutputStream os) throws Exception {
					if (templetFile != null && templetFile.exists()) {
						// 模板复制
						ExcelUtil.exportExcel(new ExcelWrite<ImportOrder>() {
							@Override
							public void write(Map<String, String> headers,
									Collection<ImportOrder> datas, WritableSheet sheet)
									throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有用户数据.....");
									sheet.addCell(cell);
									return;
								}
								
								int row = 0;

								for (ImportOrder order : datas) {
									if (order == null) {
										continue;
									}
									WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
						                    10, WritableFont.NO_BOLD);// 字体样式
									
									WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
									redcolor.setBackground(Colour.RED);
									
									WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
									oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
									int columnIndex = 0;
									// sheet.addCell(new Label(columnIndex++, row,
									// order.getId()));// 是用记录编号
									
									sheet.addCell(new Label(columnIndex++, row, order.getOrderId()));// 运单号
									sheet.addCell(new Label(columnIndex++, row, order.getSeataxno()));// 海关单号
									
									
									if(!StringUtil.isEmpty(order.getStateResult())&&(order.getStateResult().indexOf("成功")!=-1))//成功信息
									{
										sheet.addCell(new Label(columnIndex++, row, order.getStateResult()));//结果
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, order.getStateResult(),redcolor));//结果
									}
									
									

									row++;
								}
							}
						}, templetFile, orders, os);
					} else {
						// 重新写
						ExcelUtil.exportExcel(new ExcelWrite<ImportOrder>() {
							@Override
							public void write(Map<String, String> headers,
									Collection<ImportOrder> datas, WritableSheet sheet)
									throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有用户数据.....");
									sheet.addCell(cell);
									return;
								}
								
								int row = 0;

								for (ImportOrder order : datas) {
									if (order == null) {
										continue;
									}
									WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
						                    10, WritableFont.NO_BOLD);// 字体样式
									
									WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
									redcolor.setBackground(Colour.RED);
									
									WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
									oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
									int columnIndex = 0;
									// sheet.addCell(new Label(columnIndex++, row,
									// order.getId()));// 是用记录编号
									
									sheet.addCell(new Label(columnIndex++, row, order.getOrderId()));// 运单号
									sheet.addCell(new Label(columnIndex++, row, order.getSeataxno()));// 海关单号
									
									
									if(!StringUtil.isEmpty(order.getStateResult())&&(order.getStateResult().indexOf("成功")!=-1))//成功信息
									{
										sheet.addCell(new Label(columnIndex++, row, order.getStateResult()));//结果
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, order.getStateResult(),redcolor));//结果
									}
									
									

									row++;
								}
							}
						}, "order-datas", null, orders, os);
					}
				}
				//读取海关单号对应第三方单号及状态
				public static List<ImportOrder> readOrderSeaTaxtothirdExcel2(InputStream input)
						throws Exception {
					return ExcelUtil.importExcel(new ExcelReader<ImportOrder>() {
						@Override
						public List<ImportOrder> read(Sheet sheet) throws Exception {
							List<ImportOrder> list = new ArrayList<ImportOrder>();
							int rows = sheet.getRows();
							for (int i = 1; i < rows; i++) {
								int j = 0;
								
								
								ImportOrder io = new ImportOrder();
								String seaNo=sheet.getCell(j++, i).getContents();
								if((seaNo!=null)&&(!seaNo.equalsIgnoreCase("")))
								{
									
									io.setSeataxno(seaNo.trim());//海关运单号
									
									
									
									String state = sheet.getCell(j++, i).getContents();//运单状态
									if((state!=null)&&(!state.equalsIgnoreCase(""))&&(isNumeric(state)))
									{
										io.setState(state.trim());
										String stateremark = sheet.getCell(j++, i).getContents();//状态说明
										io.setStateremark(stateremark);
											
										
										
										
									}
									else if((state==null)||(state.equalsIgnoreCase("")))//留空表示原状态不变
									{
										io.setState(null);
										String stateremark = sheet.getCell(j++, i).getContents();//状态说明
										io.setStateremark(stateremark);
										
									}
									else
									{
										String str0 = "第" + i + "行，第"+j+"列只能为数字格式或留空!";
										throw new RuntimeException(str0);
									}
									
									
									
									
									String thirdpn = sheet.getCell(j++, i).getContents();//第三方运单公司代码
									if((thirdpn==null)||(thirdpn.trim().equalsIgnoreCase("")))
									{
										io.setThirdPNS("");
									}
									else
									{
										io.setThirdPNS(thirdpn.trim());
									}
									
									String thirdno = sheet.getCell(j++, i).getContents();//第三方运单公司运单号
									if((thirdno==null)||(thirdno.trim().equalsIgnoreCase("")))
									{
										io.setThirdNo("");
									}
									else
									{
										io.setThirdNo(thirdno.trim());
									}

								}
								else
								{
									break;
								}
								
								
								list.add(io);
							}
							return list;
						}
					}, input);
				}
				
				// kai 10160119 海关单号修改状态结果导出
				public static void exportOrderSeaNoModifystateToResult2(List<ImportOrder> orders,
						File templetFile, OutputStream os) throws Exception {
					if (templetFile != null && templetFile.exists()) {
						// 模板复制
						ExcelUtil.exportExcel(new ExcelWrite<ImportOrder>() {
							@Override
							public void write(Map<String, String> headers,
									Collection<ImportOrder> datas, WritableSheet sheet)
									throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有用户数据.....");
									sheet.addCell(cell);
									return;
								}
								
								int row = 1;

								for (ImportOrder order : datas) {
									if (order == null) {
										continue;
									}
									WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
						                    10, WritableFont.NO_BOLD);// 字体样式
									
									WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
									redcolor.setBackground(Colour.RED);
									
									WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
									oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
									int columnIndex = 0;
									// sheet.addCell(new Label(columnIndex++, row,
									// order.getId()));// 是用记录编号
									
									
									sheet.addCell(new Label(columnIndex++, row, order.getSeataxno()));// 海关单号
									sheet.addCell(new Label(columnIndex++, row, order.getState()));// 状态值
									sheet.addCell(new Label(columnIndex++, row, order.getStateremark()));// 状态备注
									sheet.addCell(new Label(columnIndex++, row, order.getThirdPNS()));// 第三方公司代码
									sheet.addCell(new Label(columnIndex++, row, order.getThirdNo()));// 第三方单号
									sheet.addCell(new Label(columnIndex++, row, order.getOrderId()));// 返回系统单号
									
									if(!StringUtil.isEmpty(order.getStateResult())&&(order.getStateResult().indexOf("成功")!=-1))//成功信息
									{
										sheet.addCell(new Label(columnIndex++, row, order.getStateResult()));//结果
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, order.getStateResult(),redcolor));//结果
									}
									
									

									row++;
								}
							}
						}, templetFile, orders, os);
					} else {
						// 重新写
						ExcelUtil.exportExcel(new ExcelWrite<ImportOrder>() {
							@Override
							public void write(Map<String, String> headers,
									Collection<ImportOrder> datas, WritableSheet sheet)
									throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有用户数据.....");
									sheet.addCell(cell);
									return;
								}
								
								int row = 1;

								for (ImportOrder order : datas) {
									if (order == null) {
										continue;
									}
									WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
						                    10, WritableFont.NO_BOLD);// 字体样式
									
									WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
									redcolor.setBackground(Colour.RED);
									
									WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
									oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
									int columnIndex = 0;
									// sheet.addCell(new Label(columnIndex++, row,
									// order.getId()));// 是用记录编号
									
									
									sheet.addCell(new Label(columnIndex++, row, order.getSeataxno()));// 海关单号
									sheet.addCell(new Label(columnIndex++, row, order.getState()));// 状态值
									sheet.addCell(new Label(columnIndex++, row, order.getStateremark()));// 状态备注
									sheet.addCell(new Label(columnIndex++, row, order.getThirdPNS()));// 第三方公司代码
									sheet.addCell(new Label(columnIndex++, row, order.getThirdNo()));// 第三方单号
									sheet.addCell(new Label(columnIndex++, row, order.getOrderId()));// 返回系统单号
									
									if(!StringUtil.isEmpty(order.getStateResult())&&(order.getStateResult().indexOf("成功")!=-1))//成功信息
									{
										sheet.addCell(new Label(columnIndex++, row, order.getStateResult()));//结果
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, order.getStateResult(),redcolor));//结果
									}
									
									

									row++;
								}
							}
						}, "order-datas", null, orders, os);
					}
				}	
				
				
				
				
				//当读取第三方数据时，返回操作结果
				public static void exportOrderTothirdExcelresult_weiyi(List<ImportthirdOrder> orders, File templetFile, OutputStream os) throws Exception {
					if (templetFile != null && templetFile.exists()) {
						// 模板复制
						ExcelUtil.exportExcel(new ExcelWrite<ImportthirdOrder>() {
							@Override
							public void write(Map<String, String> headers, Collection<ImportthirdOrder> datas, WritableSheet sheet)
							        throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有用户数据.....");
									sheet.addCell(cell);
									return;
								}

								int row = 1;
								
								for (ImportthirdOrder iorder : datas)
								{
									WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
						                    10, WritableFont.NO_BOLD);// 字体样式
									
									WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
									redcolor.setBackground(Colour.RED);
									
									WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
									oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
									//String[] orderflag=iorder.getOrderflag();
									//String[] userflag=iorder.getUserflag();
									for (Order order : iorder.getOrders()) {
										int columnIndex = 0;
										 sheet.addCell(new Label(columnIndex++, row,
												 Integer.toString(row)));// 用户上传的编号 
										
										sheet.addCell(new Label(columnIndex++, row,order.getThirdCreateDate()));// 入仓号  
										
										if((!StringUtil.isEmpty(order.getResult()))&&(order.getResult().indexOf("失败") == -1)&&(order.getResult().indexOf("成功") != -1))
										{
											sheet.addCell(new Label(columnIndex++, row,order.getOrderId()));// 成功
										}
										else if((!StringUtil.isEmpty(order.getResult()))&&(order.getResult().indexOf("失败")!= -1))//包含失败
										{
											sheet.addCell(new Label(columnIndex++, row,order.getOrderId(),redcolor));// 失败
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row,order.getOrderId(),oceanbluecolor));// 不确定的用蓝色标识
										}
										sheet.addCell(new Label(columnIndex++, row,order.getFlight()));// 第三方航班号
										
										sheet.addCell(new Label(columnIndex++, row,order.getBoxNo()));// 第三方集装箱号
										sheet.addCell(new Label(columnIndex++, row,order.getPayOrNot()));// 第三方是否已经付款
										sheet.addCell(new Label(columnIndex++, row,order.getSenduserName()));// 第三方导入是的发件人用户名
										sheet.addCell(new Label(columnIndex++, row,order.getSenduserphone()));// 第三方发件人联系电话
										sheet.addCell(new Label(columnIndex++, row,order.getCommodityThirdList()));// 发件人的商品清单
										sheet.addCell(new Label(columnIndex++, row,order.getThirdBrands()));// 发件人的商品品牌
										sheet.addCell(new Label(columnIndex++, row,order.getQuantity()));// 发件数量
										sheet.addCell(new Label(columnIndex++, row,order.getWeightKg()));//重量kg
										sheet.addCell(new Label(columnIndex++, row,order.getWeight()));//重量
										sheet.addCell(new Label(columnIndex++, row,order.getBaseMoney()));////基本费用
										sheet.addCell(new Label(columnIndex++, row,order.getExpressName()));//快递公司名称
										sheet.addCell(new Label(columnIndex++, row,order.getPremium()));//保险费用
										sheet.addCell(new Label(columnIndex++, row,order.getTotalMoney()));//总费用
										sheet.addCell(new Label(columnIndex++, row,order.getNamePinyi()));//拼音
										sheet.addCell(new Label(columnIndex++, row,order.getCardOrNot()));//是否有身份证
										sheet.addCell(new Label(columnIndex++, row,order.getcName()));//收件人姓名
										sheet.addCell(new Label(columnIndex++, row,order.getcPhone()));//收件人电话
										sheet.addCell(new Label(columnIndex++, row,order.getcStreetAddress()));//收件人地址
										sheet.addCell(new Label(columnIndex++, row,order.getImportRemark()));//导入备注
										sheet.addCell(new Label(columnIndex++, row,order.getRemark()));//运单备注
										sheet.addCell(new Label(columnIndex++, row,order.getState()));//导入状态
										sheet.addCell(new Label(columnIndex++, row,order.getStateRemark()));//导入状态说明
										
										
										
										if((!StringUtil.isEmpty(order.getResult()))&&(order.getResult().indexOf("失败") == -1)&&(order.getResult().indexOf("成功") != -1))
										{
											sheet.addCell(new Label(columnIndex++, row,order.getResult()));// 成功
										}
										else if((!StringUtil.isEmpty(order.getResult()))&&(order.getResult().indexOf("失败")!= -1))//包含失败
										{
											sheet.addCell(new Label(columnIndex++, row,order.getResult(),redcolor));// 失败
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row,order.getResult(),oceanbluecolor));// 不确定的用蓝色标识
										}
										
										row++;
									}
								}
							}
						}, templetFile, orders, os);
					} else {
						// 重新写
						ExcelUtil.exportExcel(new ExcelWrite<ImportthirdOrder>() {
							@Override
							public void write(Map<String, String> headers, Collection<ImportthirdOrder> datas, WritableSheet sheet)
							        throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有用户数据.....");
									sheet.addCell(cell);
									return;
								}

								int row = 1;
								
								for (ImportthirdOrder iorder : datas)
								{
									WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
						                    10, WritableFont.NO_BOLD);// 字体样式
									
									WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
									redcolor.setBackground(Colour.RED);
									
									WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
									oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
									//String[] orderflag=iorder.getOrderflag();
									//String[] userflag=iorder.getUserflag();
									for (Order order : iorder.getOrders()) {
										int columnIndex = 0;
										 sheet.addCell(new Label(columnIndex++, row,
										 order.getId()));// 用户上传的编号 
										
										sheet.addCell(new Label(columnIndex++, row,order.getThirdCreateDate()));// 入仓号  
										
										if((!StringUtil.isEmpty(order.getResult()))&&(order.getResult().indexOf("失败") == -1)&&(order.getResult().indexOf("成功") != -1))
										{
											sheet.addCell(new Label(columnIndex++, row,order.getOrderId()));// 成功
										}
										else if((!StringUtil.isEmpty(order.getResult()))&&(order.getResult().indexOf("失败")!= -1))//包含失败
										{
											sheet.addCell(new Label(columnIndex++, row,order.getOrderId(),redcolor));// 失败
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row,order.getOrderId(),oceanbluecolor));// 不确定的用蓝色标识
										}
										sheet.addCell(new Label(columnIndex++, row,order.getFlight()));// 第三方航班号
										
										sheet.addCell(new Label(columnIndex++, row,order.getBoxNo()));// 第三方集装箱号
										sheet.addCell(new Label(columnIndex++, row,order.getPayOrNot()));// 第三方是否已经付款
										sheet.addCell(new Label(columnIndex++, row,order.getSenduserName()));// 第三方导入是的发件人用户名
										sheet.addCell(new Label(columnIndex++, row,order.getSenduserphone()));// 第三方发件人联系电话
										sheet.addCell(new Label(columnIndex++, row,order.getCommodityThirdList()));// 发件人的商品清单
										sheet.addCell(new Label(columnIndex++, row,order.getThirdBrands()));// 发件人的商品品牌
										sheet.addCell(new Label(columnIndex++, row,order.getQuantity()));// 发件数量
										sheet.addCell(new Label(columnIndex++, row,order.getWeightKg()));//重量kg
										sheet.addCell(new Label(columnIndex++, row,order.getWeight()));//重量
										sheet.addCell(new Label(columnIndex++, row,order.getBaseMoney()));////基本费用
										sheet.addCell(new Label(columnIndex++, row,order.getExpressName()));//快递公司名称
										sheet.addCell(new Label(columnIndex++, row,order.getPremium()));//保险费用
										sheet.addCell(new Label(columnIndex++, row,order.getTotalMoney()));//总费用
										sheet.addCell(new Label(columnIndex++, row,order.getNamePinyi()));//拼音
										sheet.addCell(new Label(columnIndex++, row,order.getCardOrNot()));//是否有身份证
										sheet.addCell(new Label(columnIndex++, row,order.getcName()));//收件人姓名
										sheet.addCell(new Label(columnIndex++, row,order.getcPhone()));//收件人电话
										sheet.addCell(new Label(columnIndex++, row,order.getcStreetAddress()));//收件人地址
										sheet.addCell(new Label(columnIndex++, row,order.getImportRemark()));//导入备注
										sheet.addCell(new Label(columnIndex++, row,order.getRemark()));//运单备注
										sheet.addCell(new Label(columnIndex++, row,order.getState()));//导入状态
										sheet.addCell(new Label(columnIndex++, row,order.getStateRemark()));//导入状态说明
										
										
										
										if((!StringUtil.isEmpty(order.getResult()))&&(order.getResult().indexOf("失败") == -1)&&(order.getResult().indexOf("成功") != -1))
										{
											sheet.addCell(new Label(columnIndex++, row,order.getResult()));// 成功
										}
										else if((!StringUtil.isEmpty(order.getResult()))&&(order.getResult().indexOf("失败")!= -1))//包含失败
										{
											sheet.addCell(new Label(columnIndex++, row,order.getResult(),redcolor));// 失败
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row,order.getResult(),oceanbluecolor));// 不确定的用蓝色标识
										}
										
										row++;
									}
								}
							}
						}, "order-datas", null, orders, os);
					}
				}
				
				
				//当读取第三方数据时，返回美淘的结果数据
				public static void exportOrderToresultthirdExcel_meitao(List<ImportthirdOrder> orders, File templetFile, OutputStream os) throws Exception {
					if (templetFile != null && templetFile.exists()) {
						// 模板复制
						ExcelUtil.exportExcel(new ExcelWrite<ImportthirdOrder>() {
							@Override
							public void write(Map<String, String> headers, Collection<ImportthirdOrder> datas, WritableSheet sheet)
							        throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有用户数据.....");
									sheet.addCell(cell);
									return;
								}

								int row = 1;
								
								for (ImportthirdOrder iorder : datas)
								{
									WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
						                    10, WritableFont.NO_BOLD);// 字体样式
									
									WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
									redcolor.setBackground(Colour.RED);
									
									WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
									oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
									//String[] orderflag=iorder.getOrderflag();
									//String[] userflag=iorder.getUserflag();
									for (Order order : iorder.getOrders()) {
										int columnIndex = 0;
																													
										if((!StringUtil.isEmpty(order.getResult()))&&(order.getResult().indexOf("失败") == -1)&&(order.getResult().indexOf("成功") != -1))
										{
											sheet.addCell(new Label(columnIndex++, row,order.getOrderId()));// 成功
										}
										else if((!StringUtil.isEmpty(order.getResult()))&&(order.getResult().indexOf("失败")!= -1))//包含失败
										{
											sheet.addCell(new Label(columnIndex++, row,order.getOrderId(),redcolor));// 失败
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row,order.getOrderId(),oceanbluecolor));// 不确定的用蓝色标识
										}
										
										
										sheet.addCell(new Label(columnIndex++, row,order.getWeight()));//重量
										sheet.addCell(new Label(columnIndex++, row,"1"));//箱数
										sheet.addCell(new Label(columnIndex++, row,order.getCommodityThirdList()));// 发件人的商品清单
										sheet.addCell(new Label(columnIndex++, row,order.getThirdBrands()));// 发件人的商品品牌
										sheet.addCell(new Label(columnIndex++, row,order.getSenduserName()));// 第三方导入是的发件人用户名
										sheet.addCell(new Label(columnIndex++, row,order.getSenduserphone()));// 第三方联系电话
										sheet.addCell(new Label(columnIndex++, row,order.getSenduserAddress()));// 第三方联系地址
										sheet.addCell(new Label(columnIndex++, row,order.getSenduserCity()));// 城市
										sheet.addCell(new Label(columnIndex++, row,order.getSenduserState()));//州
										sheet.addCell(new Label(columnIndex++, row,order.getSenduserZipcode()));//发送人邮篇
										sheet.addCell(new Label(columnIndex++, row,order.getcName()));//收件人姓名
										sheet.addCell(new Label(columnIndex++, row,order.getcProvince()));//收件人所在省
										sheet.addCell(new Label(columnIndex++, row,order.getcCity()));//城市
										sheet.addCell(new Label(columnIndex++, row,order.getcDistrict()));///收件人所在区
										sheet.addCell(new Label(columnIndex++, row,order.getcStreetAddress()));///收件人所在地址
										sheet.addCell(new Label(columnIndex++, row,order.getcPhone()));//收件人电话
										sheet.addCell(new Label(columnIndex++, row,order.getcZipCode()));//收件人邮编
										sheet.addCell(new Label(columnIndex++, row,order.getCommodityCost()));//成本
										sheet.addCell(new Label(columnIndex++, row,order.getTotalMoney()));//总费用
										sheet.addCell(new Label(columnIndex++, row,order.getState()));//状态
										
										
										if((!StringUtil.isEmpty(order.getResult()))&&(order.getResult().indexOf("失败") == -1)&&(order.getResult().indexOf("成功") != -1))
										{
											sheet.addCell(new Label(columnIndex++, row,order.getResult()));// 成功
										}
										else if((!StringUtil.isEmpty(order.getResult()))&&(order.getResult().indexOf("失败")!= -1))//包含失败
										{
											sheet.addCell(new Label(columnIndex++, row,order.getResult(),redcolor));// 失败
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row,order.getResult(),oceanbluecolor));// 不确定的用蓝色标识
										}
										
										
										row++;
									}
								}
							}
						}, templetFile, orders, os);
					} else {
						// 重新写
						ExcelUtil.exportExcel(new ExcelWrite<ImportthirdOrder>() {
							@Override
							public void write(Map<String, String> headers, Collection<ImportthirdOrder> datas, WritableSheet sheet)
							        throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有用户数据.....");
									sheet.addCell(cell);
									return;
								}

								int row = 1;
								
								for (ImportthirdOrder iorder : datas)
								{
									WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
						                    10, WritableFont.NO_BOLD);// 字体样式
									
									WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
									redcolor.setBackground(Colour.RED);
									
									WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
									oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
									//String[] orderflag=iorder.getOrderflag();
									//String[] userflag=iorder.getUserflag();
									for (Order order : iorder.getOrders()) {
										int columnIndex = 0;
																													
										if((!StringUtil.isEmpty(order.getResult()))&&(order.getResult().indexOf("失败") == -1)&&(order.getResult().indexOf("成功") != -1))
										{
											sheet.addCell(new Label(columnIndex++, row,order.getOrderId()));// 成功
										}
										else if((!StringUtil.isEmpty(order.getResult()))&&(order.getResult().indexOf("失败")!= -1))//包含失败
										{
											sheet.addCell(new Label(columnIndex++, row,order.getOrderId(),redcolor));// 失败
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row,order.getOrderId(),oceanbluecolor));// 不确定的用蓝色标识
										}
										
										
										sheet.addCell(new Label(columnIndex++, row,order.getWeight()));//重量
										sheet.addCell(new Label(columnIndex++, row,"1"));//箱数
										sheet.addCell(new Label(columnIndex++, row,order.getCommodityThirdList()));// 发件人的商品清单
										sheet.addCell(new Label(columnIndex++, row,order.getThirdBrands()));// 发件人的商品品牌
										sheet.addCell(new Label(columnIndex++, row,order.getSenduserName()));// 第三方导入是的发件人用户名
										sheet.addCell(new Label(columnIndex++, row,order.getSenduserphone()));// 第三方联系电话
										sheet.addCell(new Label(columnIndex++, row,order.getSenduserAddress()));// 第三方联系地址
										sheet.addCell(new Label(columnIndex++, row,order.getSenduserCity()));// 城市
										sheet.addCell(new Label(columnIndex++, row,order.getSenduserState()));//州
										sheet.addCell(new Label(columnIndex++, row,order.getSenduserZipcode()));//发送人邮篇
										sheet.addCell(new Label(columnIndex++, row,order.getcName()));//收件人姓名
										sheet.addCell(new Label(columnIndex++, row,order.getcProvince()));//收件人所在省
										sheet.addCell(new Label(columnIndex++, row,order.getcCity()));//城市
										sheet.addCell(new Label(columnIndex++, row,order.getcDistrict()));///收件人所在区
										sheet.addCell(new Label(columnIndex++, row,order.getcStreetAddress()));///收件人所在地址
										sheet.addCell(new Label(columnIndex++, row,order.getcPhone()));//收件人电话
										sheet.addCell(new Label(columnIndex++, row,order.getcZipCode()));//收件人邮编
										sheet.addCell(new Label(columnIndex++, row,order.getCommodityCost()));//成本
										sheet.addCell(new Label(columnIndex++, row,order.getTotalMoney()));//总费用
										sheet.addCell(new Label(columnIndex++, row,order.getState()));//状态
										
										
										if((!StringUtil.isEmpty(order.getResult()))&&(order.getResult().indexOf("失败") == -1)&&(order.getResult().indexOf("成功") != -1))
										{
											sheet.addCell(new Label(columnIndex++, row,order.getResult()));// 成功
										}
										else if((!StringUtil.isEmpty(order.getResult()))&&(order.getResult().indexOf("失败")!= -1))//包含失败
										{
											sheet.addCell(new Label(columnIndex++, row,order.getResult(),redcolor));// 失败
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row,order.getResult(),oceanbluecolor));// 不确定的用蓝色标识
										}
										
										
										row++;
									}
								}
							}
						}, "order-datas", null, orders, os);
					}
				}
}

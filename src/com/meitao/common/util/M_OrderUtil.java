package com.meitao.common.util;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;



import jxl.Sheet;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;



import com.meitao.common.util.excel.ExcelReader;
import com.meitao.common.util.excel.ExcelUtil;
import com.meitao.common.util.excel.ExcelWrite;
import com.meitao.model.M_OrderDetail;
import com.meitao.model.M_order;
import com.meitao.model.Receive_User;
import com.meitao.model.Send_User;
import com.meitao.model.temp.ExportMorder;
import com.meitao.model.temp.ImportMorder;
import com.meitao.model.temp.ImportthirdMorder;



public class M_OrderUtil {//判断字符串是否为数字
	//获取报关品名顺序，如果不包含里面，将取第一个，与类型名匹配
	static String[] conf_serio = {"包", "保健品", "手表", "香水", "干货"};
	
	public static boolean isNumeric(String str){
		for (int i = 0; i < str.length(); i++){
		   System.out.println(str.charAt(i));
		   if (!Character.isDigit(str.charAt(i))){
		    return false;
		   }
		}
		return true;
		}
	//kai 20160330  导入美淘快递状态 
	public static List<ImportMorder> readMorderExcel_meitao_state(InputStream input)
			throws Exception {
		return ExcelUtil.importExcel(new ExcelReader<ImportMorder>() {
			@Override
			public List<ImportMorder> read(Sheet sheet) throws Exception {
				List<ImportMorder> list = new ArrayList<ImportMorder>();
				int rows = sheet.getRows();
				for (int i = 1; i < rows; i++) {
					int j = 1;
					
					
					ImportMorder io = new ImportMorder();
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
	
	
	// kai 20160330
	public static void exportOrderStateToResult(List<ImportMorder> orders,
			File templetFile, OutputStream os) throws Exception {
		if (templetFile != null && templetFile.exists()) {
			// 模板复制
			ExcelUtil.exportExcel(new ExcelWrite<ImportMorder>() {
				@Override
				public void write(Map<String, String> headers,
						Collection<ImportMorder> datas, WritableSheet sheet)
						throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有用户数据.....");
						sheet.addCell(cell);
						return;
					}
					
					int row = 1;

					for (ImportMorder order : datas) {
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
						
						if(StringUtil.isEmpty(order.getMessageResult())||(order.getMessageResult().indexOf("成功")!=-1))//成功信息
						{
							sheet.addCell(new Label(columnIndex++, row, order.getMessageResult()));//结果
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, order.getMessageResult(),redcolor));//结果
						}
						
						

						row++;
					}
				}
			}, templetFile, orders, os);
		} else {
			ExcelUtil.exportExcel(new ExcelWrite<ImportMorder>() {
				@Override
				public void write(Map<String, String> headers,
						Collection<ImportMorder> datas, WritableSheet sheet)
						throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有用户数据.....");
						sheet.addCell(cell);
						return;
					}
					
					int row = 1;

					for (ImportMorder order : datas) {
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
	
	
	// kai 20160406 美淘导出运单
	public static void exportOrderToMeitao(List<M_order> orders,
			File templetFile, OutputStream os) throws Exception {
		if (templetFile != null && templetFile.exists()) {
			// 模板复制
			ExcelUtil.exportExcel(new ExcelWrite<M_order>() {
				@Override
				public void write(Map<String, String> headers,
						Collection<M_order> datas, WritableSheet sheet)
						throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有用户数据.....");
						sheet.addCell(cell);
						return;
					}
					
					int row = 1;

					for (M_order order : datas) {
						if (order == null) {
							continue;
						}
						
						int columnIndex = 0;
						// sheet.addCell(new Label(columnIndex++, row,
						// order.getId()));// 是用记录编号
						sheet.addCell(new Label(columnIndex++, row, order
								.getOrderId()));// 运单号
						if(StringUtil.isEmpty(order.getSjweight()))
						{
							sheet.addCell(new Label(columnIndex++, row, order.getWeight()));//重量
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, order.getSjweight()));//实际重量
						}
						//sheet.addCell(new Label(columnIndex++, row, order.getJwweight()));//实际重量
						sheet.addCell(new Label(columnIndex++, row, "1"));//箱数
						// 开始到处商品信息
						List<M_OrderDetail> details = order.getDetail();
						////StringBuffer sb = new StringBuffer();
						//StringBuffer brands = new StringBuffer();
						//StringBuffer xq = new StringBuffer();
						//int total = 0;
						String commudity="";
						String brands="";
						if ((details != null)&&(details.size()>0))
						{
							//String str="";
							for(M_OrderDetail d:details)
							{
								if(commudity.equalsIgnoreCase(""))
								{
									commudity=d.getProductName()+"*"+d.getQuantity();
								}
								else
								{
									commudity=commudity+";"+d.getProductName()+"*"+d.getQuantity();
								}
								if(brands.equalsIgnoreCase(""))
								{
									brands=d.getBrands();
								}
								else
								{
									brands=brands+";"+d.getBrands();
								}
								
							}
						}
						
						sheet.addCell(new Label(columnIndex++, row, commudity
								.toString()));// 物品品名
						
					
						
						if(brands!=null)
						{
						sheet.addCell(new Label(columnIndex++, row, brands
								.toString()));// 物品品牌
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, ""));// 物品品牌
						}
						  //发件人为代理客户，则发件人信息取自订单
						if((order.getSuser()!=null))
						{
							sheet.addCell(new Label(columnIndex++, row, order.getSuser().getName())); // 发件人
							sheet.addCell(new Label(columnIndex++, row, order.getSuser().getPhone())); // 发件人联系电话
							sheet.addCell(new Label(columnIndex++, row, order.getSuser().getAddress())); // 发件人地址
							sheet.addCell(new Label(columnIndex++, row, order.getSuser().getCity())); // 发件人城市	
							sheet.addCell(new Label(columnIndex++, row, order.getSuser().getState())); // 发件人州名	
							sheet.addCell(new Label(columnIndex++, row, order.getSuser().getZipcode())); // 发件人邮编	
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
						
						
						if((order.getRuser()!=null))
						{
							sheet.addCell(new Label(columnIndex++, row, order.getRuser().getName()));// 收件人姓名
							sheet.addCell(new Label(columnIndex++, row, order.getRuser().getState()));// 收件人所在省份
							sheet.addCell(new Label(columnIndex++, row, order.getRuser().getCity()));// 收件人所在市
							sheet.addCell(new Label(columnIndex++, row, order.getRuser().getDist()));// 收件人所在区
							sheet.addCell(new Label(columnIndex++, row, order.getRuser().getAddress()));// 收件人详细地址
							sheet.addCell(new Label(columnIndex++, row, order.getRuser().getPhone()));// 收件人电话
							sheet.addCell(new Label(columnIndex++, row, order.getRuser().getZipcode()));// 收件人邮编
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, ""));// 收件人姓名
							sheet.addCell(new Label(columnIndex++, row, ""));// 收件人所在省份
							sheet.addCell(new Label(columnIndex++, row, ""));// 收件人所在市
							sheet.addCell(new Label(columnIndex++, row, ""));// 收件人所在区
							sheet.addCell(new Label(columnIndex++, row, ""));// 收件人详细地址
							sheet.addCell(new Label(columnIndex++, row, ""));// 收件人电话
							sheet.addCell(new Label(columnIndex++, row, ""));// 收件人邮编
						}
				
						
					
					
						
						//sheet.addCell(new Label(columnIndex++, row, String
						//		.valueOf(CommodityCost)));// 费用成本
						sheet.addCell(new Label(columnIndex++, row, ""));// 成本费用
						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(order.getTotalmoney())));// 费用合计

						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(order.getState())));// 状态
						row++;
					}
				}
			}, templetFile, orders, os);
		} else {
			// 重新写
			ExcelUtil.exportExcel(new ExcelWrite<M_order>() {
				@Override
				public void write(Map<String, String> headers,
						Collection<M_order> datas, WritableSheet sheet)
						throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有用户数据.....");
						sheet.addCell(cell);
						return;
					}
					
					int row = 1;

					for (M_order order : datas) {
						if (order == null) {
							continue;
						}
						
						int columnIndex = 0;
						// sheet.addCell(new Label(columnIndex++, row,
						// order.getId()));// 是用记录编号
						sheet.addCell(new Label(columnIndex++, row, order
								.getOrderId()));// 运单号
						if(StringUtil.isEmpty(order.getSjweight()))
						{
							sheet.addCell(new Label(columnIndex++, row, order.getWeight()));//重量
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, order.getSjweight()));//实际重量
						}
						//sheet.addCell(new Label(columnIndex++, row, order.getJwweight()));//实际重量
						sheet.addCell(new Label(columnIndex++, row, "1"));//箱数
						// 开始到处商品信息
						List<M_OrderDetail> details = order.getDetail();
						////StringBuffer sb = new StringBuffer();
						//StringBuffer brands = new StringBuffer();
						//StringBuffer xq = new StringBuffer();
						//int total = 0;
						String commudity="";
						String brands="";
						if ((details != null)&&(details.size()>0))
						{
							//String str="";
							for(M_OrderDetail d:details)
							{
								if(commudity.equalsIgnoreCase(""))
								{
									commudity=d.getProductName()+"*"+d.getQuantity();
								}
								else
								{
									commudity=commudity+";"+d.getProductName()+"*"+d.getQuantity();
								}
								if(brands.equalsIgnoreCase(""))
								{
									brands=d.getBrands();
								}
								else
								{
									brands=brands+";"+d.getBrands();
								}
								
							}
						}
						
						sheet.addCell(new Label(columnIndex++, row, commudity
								.toString()));// 物品品名
						if(brands!=null)
						{
						sheet.addCell(new Label(columnIndex++, row, brands
								.toString()));// 物品品牌
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, ""));// 物品品牌
						}
						  //发件人为代理客户，则发件人信息取自订单
						if((order.getSuser()!=null))
						{
							sheet.addCell(new Label(columnIndex++, row, order.getSuser().getName())); // 发件人
							sheet.addCell(new Label(columnIndex++, row, order.getSuser().getPhone())); // 发件人联系电话
							sheet.addCell(new Label(columnIndex++, row, order.getSuser().getAddress())); // 发件人地址
							sheet.addCell(new Label(columnIndex++, row, order.getSuser().getCity())); // 发件人城市	
							sheet.addCell(new Label(columnIndex++, row, order.getSuser().getState())); // 发件人州名	
							sheet.addCell(new Label(columnIndex++, row, order.getSuser().getZipcode())); // 发件人邮编	
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
						
						
						if((order.getRuser()!=null))
						{
							sheet.addCell(new Label(columnIndex++, row, order.getRuser().getName()));// 收件人姓名
							sheet.addCell(new Label(columnIndex++, row, order.getRuser().getState()));// 收件人所在省份
							sheet.addCell(new Label(columnIndex++, row, order.getRuser().getCity()));// 收件人所在市
							sheet.addCell(new Label(columnIndex++, row, order.getRuser().getDist()));// 收件人所在区
							sheet.addCell(new Label(columnIndex++, row, order.getRuser().getAddress()));// 收件人详细地址
							sheet.addCell(new Label(columnIndex++, row, order.getRuser().getPhone()));// 收件人电话
							sheet.addCell(new Label(columnIndex++, row, order.getRuser().getZipcode()));// 收件人邮编
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, ""));// 收件人姓名
							sheet.addCell(new Label(columnIndex++, row, ""));// 收件人所在省份
							sheet.addCell(new Label(columnIndex++, row, ""));// 收件人所在市
							sheet.addCell(new Label(columnIndex++, row, ""));// 收件人所在区
							sheet.addCell(new Label(columnIndex++, row, ""));// 收件人详细地址
							sheet.addCell(new Label(columnIndex++, row, ""));// 收件人电话
							sheet.addCell(new Label(columnIndex++, row, ""));// 收件人邮编
						}
				
						
					
					
						
						//sheet.addCell(new Label(columnIndex++, row, String
						//		.valueOf(CommodityCost)));// 费用成本
						sheet.addCell(new Label(columnIndex++, row, ""));// 成本费用
						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(order.getTotalmoney())));// 费用合计

						sheet.addCell(new Label(columnIndex++, row, String
								.valueOf(order.getState())));// 状态
						row++;
					}
				}
			}, "order-datas", null, orders, os);
		}
	}
	
	
	
	
	// kai 20160426  获取导入的运单号
	public static List<ExportMorder> readorderidExcel(InputStream input)
			throws Exception {
		return ExcelUtil.importExcel(new ExcelReader<ExportMorder>() {
			@Override
			public List<ExportMorder> read(Sheet sheet) throws Exception {
				List<ExportMorder> list = new ArrayList<ExportMorder>();
				int rows = sheet.getRows();
				for (int i = 0; i < rows; i++) {

					int j = 0;
					
					ExportMorder order = new ExportMorder();
				
					String orderid=StringUtil.getValue(sheet.getCell(j++, i).getContents(), "");//运单号
					order.setOrderId(orderid);
					list.add(order);
				}
				return list;
			}
		}, input);
	}
	

	
	// 导出新的内蒙古核对模板 kai 20160316
		public static void exportneimenggumode(List<ExportMorder> orders,
				File templetFile, OutputStream os) throws Exception {
			if (templetFile != null && templetFile.exists()) {
				// 模板复制
				ExcelUtil.exportExcel(new ExcelWrite<ExportMorder>() {
					@Override
					public void write(Map<String, String> headers,
							Collection<ExportMorder> datas, WritableSheet sheet)
							throws Exception {
						if (datas == null || datas.isEmpty()) {
							sheet.mergeCells(0, 1, 11, 1);
							Label cell = new Label(0, 1, "没有导入数据.....");
							sheet.addCell(cell);
							return;
						}
						
						int row = 4;

						String strurl="";
						for (ExportMorder order1 : datas) {
							if (order1 == null) {
								continue;
							}
							if(StringUtil.isEmpty(order1.getSavezipurl()))
							{}
							else
							{
								if(StringUtil.isEmpty(strurl))
								{
									strurl=order1.getSavezipurl();
								}
							}
							String rowresult="成功!";
							
							WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
				                    10, WritableFont.NO_BOLD);// 字体样式
							
							WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
							redcolor.setBackground(Colour.RED);
							
							WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
							oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
							
							if(order1.getOrder()==null)
							{
								sheet.addCell(new Label(0, row, ""));//海关单号
								sheet.addCell(new Label(1, row, order1.getOrderId()));//运单号
								sheet.addCell(new Label(25, row, order1.getOrderResult(),redcolor));//导出结果
								sheet.addCell(new Label(26, row, order1.getCardResult(),redcolor));//导出结果
								sheet.addCell(new Label(27, row, "失败",redcolor));//导出失败
								sheet.addCell(new Label(28, row, "海关单号失败",redcolor));//导出失败
								row++;
								continue;
							}
							else
							{
								
								//检查状态
								try
								{
									if(Double.parseDouble(order1.getOrder().getState())<2)//没有揽收的包裹
									{
										if(StringUtil.isEmpty(order1.getOrderResult()))
										{
											order1.setOrderResult("状态异常，必须是已揽收的");
										}
										else
										{
											order1.setOrderResult(order1.getOrderResult()+",必须是已揽收的");
										}
										//sheet.addCell(new Label(0, row, ""));//海关单号
										sheet.addCell(new Label(0, row, order1.getOrder().getSorderId()));//海关单号
										sheet.addCell(new Label(1, row, order1.getOrderId()));//运单号
										sheet.addCell(new Label(24, row, order1.getOrder().getSorderId()));//海关单号
										sheet.addCell(new Label(25, row, order1.getOrderResult(),redcolor));//导出结果
										sheet.addCell(new Label(26, row, order1.getCardResult(),redcolor));//导出结果
										sheet.addCell(new Label(27, row, "失败",redcolor));//导出失败
										sheet.addCell(new Label(28, row, order1.getGetSeaNoResult(),redcolor));//导出失败
										row++;
										continue;
									}
								}
								catch(Exception e)
								{
									if(StringUtil.isEmpty(order1.getOrderResult()))
									{
										order1.setOrderResult("状态异常，必须是已揽收的");
									}
									else
									{
										order1.setOrderResult(order1.getOrderResult()+",必须是已揽收的");
									}
									sheet.addCell(new Label(0, row, order1.getOrder().getSorderId()));//海关单号
									sheet.addCell(new Label(1, row, order1.getOrderId()));//运单号
									sheet.addCell(new Label(24, row, order1.getOrder().getSorderId()));//海关单号
									sheet.addCell(new Label(25, row, order1.getOrderResult(),redcolor));//导出结果
									sheet.addCell(new Label(26, row, order1.getCardResult(),redcolor));//导出结果
									sheet.addCell(new Label(27, row, "失败",redcolor));//导出失败
									sheet.addCell(new Label(28, row, order1.getGetSeaNoResult(),redcolor));//导出失败
									row++;
									continue;
								}
								
								if(!StringUtil.isEmpty(order1.getOrderResult())&&(order1.getOrderResult().indexOf("失败")!=-1||order1.getOrderResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(25, row, order1.getOrderResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(25, row, order1.getOrderResult()));
								}
								if(!StringUtil.isEmpty(order1.getCardResult())&&(order1.getCardResult().indexOf("失败")!=-1||order1.getCardResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(26, row, order1.getCardResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(26, row, order1.getCardResult()));
								}
								
								if(!StringUtil.isEmpty(order1.getGetSeaNoResult())&&(order1.getGetSeaNoResult().indexOf("失败")!=-1||order1.getGetSeaNoResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(28, row, order1.getGetSeaNoResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(28, row, order1.getGetSeaNoResult()));
								}
								
							}
							
							
							int columnIndex = 0;
							//sheet.addCell(new Label(columnIndex++, row, ""));//海关单号
							sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getSorderId()));//海关单号
							sheet.addCell(new Label(24, row, order1.getOrder().getSorderId()));//海关单号
							sheet.addCell(new Label(columnIndex++, row, order1.getOrderId()));//运单号
							int addindex=0;
							int startindex=columnIndex;
							for(M_OrderDetail d :order1.getOrder().getDetail())
							{
								if(d==null)
								{
									continue;
								}
								String qvalue="";
								try{
									
								
									if((!StringUtil.isEmpty(d.getQuantity()))&&(!StringUtil.isEmpty(d.getValue())))
									{
										if((Double.parseDouble(d.getQuantity())!=0)&&(Double.parseDouble(d.getValue())!=0))
										{
											double aaa=Double.parseDouble(d.getValue())/Double.parseDouble(d.getQuantity());
											if(aaa>=1)
											{
												qvalue=Integer.toString((int)aaa);
											}
											else
											{
												qvalue=Double.toString(aaa);
											}
										}
									}
									else
									{
										rowresult="失败!商品数量信息异常";
									}
									sheet.addCell(new Label(columnIndex++, row+addindex, d.getProductName()));//物品名称
									sheet.addCell(new Label(columnIndex++, row+addindex, qvalue));//申报单价
									sheet.addCell(new Label(columnIndex++, row+addindex, d.getQuantity()));//数量
									sheet.addCell(new Label(columnIndex++, row+addindex, d.getValue()));//申报实价
								}
								catch(Exception e)
								{
									sheet.addCell(new Label(columnIndex++, row+addindex, ""));//物品名称
									sheet.addCell(new Label(columnIndex++, row+addindex, ""));//申报单价
									sheet.addCell(new Label(columnIndex++, row+addindex, ""));//数量
									sheet.addCell(new Label(columnIndex++, row+addindex, ""));//申报实价
									rowresult="失败!商品数量信息异常";
								}
								columnIndex=startindex;
								addindex++;
								
							}
							columnIndex=startindex+4;
							
					
							
							try{
								String xweight="0";
								boolean flag=false;
								xweight=order1.getOrder().getI_weight();
								try{
									double temp=Double.parseDouble(xweight);
									if(temp>0)
									{
										flag=true;
									}
								}catch(Exception e){}
								if(flag==false)
								{
								
									if(StringUtil.isEmpty(order1.getOrder().getSjweight())||order1.getOrder().getSjweight().equalsIgnoreCase("0")||order1.getOrder().getSjweight().equalsIgnoreCase("0.00"))
									{
										xweight=order1.getOrder().getWeight();
									}
									else
									{
										xweight=order1.getOrder().getSjweight();
									}
								
								}
								
								
								//double weight=Double.parseDouble(order1.getOrder().getWeight());
								double weight=Double.parseDouble(xweight);
								if(weight<=0)
								{
									rowresult="重量信息异常";
								}
								weight=weight*0.4536;
								
								
								  BigDecimal   bd   =   new   BigDecimal(weight);   
								  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
								  sheet.addCell(new Label(columnIndex++, row, String.valueOf(bd)));//千克
							}catch(Exception e)
							{
								sheet.addCell(new Label(columnIndex++, row, ""));//数字发生异常
								rowresult="重量信息异常";
							}
							
							
							sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getSuser().getName()));//发货人姓名
							//要匹配身份证
							if((!StringUtil.isEmpty(order1.getCardmodel()))&&(order1.getCardmodel().equals("0")||order1.getCardmodel().equals("1")))
							{
								if(order1.getOrder().getRuser().getCardlib()!=null)//使用身份证信息
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardlib().getCname()));//收货人姓名
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardlib().getCardid()));//收货人身份证号
								}
								else
								{
									/*if(!StringUtil.isEmpty(order1.getOrder().getRuser().getCardid())&&!StringUtil.isEmpty(order1.getOrder().getRuser().getCardtogether()))
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardid()));//收货人身份证号
										
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人姓名
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
										rowresult="失败!没有身份证信息!";
									}*/
									
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
									sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
									rowresult="失败!没有身份证信息!";
								}
							}
							else
							{
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardid()));//收货人身份证号
							}
							
							String address="";
							
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getState()))
							{
								address=address+order1.getOrder().getRuser().getState();
							}
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getCity()))
							{
								address=address+order1.getOrder().getRuser().getCity();
							}
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getDist()))
							{
								address=address+order1.getOrder().getRuser().getDist();
							}
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getAddress()))
							{
								address=address+order1.getOrder().getRuser().getAddress();
							}
							
							sheet.addCell(new Label(columnIndex++, row, address));//收货人地址
							
							
							
							sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
							sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getZipcode()));//收货人邮编
							sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCity()));//收货人城市
							
							sheet.addCell(new Label(24, row, order1.getOrder().getSorderId()));//海关单号
							if(!StringUtil.isEmpty(rowresult)&&(rowresult.indexOf("失败")!=-1||rowresult.indexOf("异常")!=-1))
							{
								sheet.addCell(new Label(27, row, rowresult,redcolor));
							}
							else
							{
								sheet.addCell(new Label(27, row, rowresult));
							}
							

							if(addindex==0)
							{
								row++;
							}
							else
							{
								row=addindex+row;
							}
						}
					
						if(!StringUtil.isEmpty(strurl))
						{
							strurl="图片保存路径："+strurl;
							sheet.addCell(new Label(0, row, strurl));
						}
					}
				}, templetFile, orders, os);
			} else {
				// 模板复制
				ExcelUtil.exportExcel(new ExcelWrite<ExportMorder>() {
					@Override
					public void write(Map<String, String> headers,
							Collection<ExportMorder> datas, WritableSheet sheet)
							throws Exception {
						if (datas == null || datas.isEmpty()) {
							sheet.mergeCells(0, 1, 11, 1);
							Label cell = new Label(0, 1, "没有导入数据.....");
							sheet.addCell(cell);
							return;
						}
						
						int row = 4;

						String strurl="";
						for (ExportMorder order1 : datas) {
							if (order1 == null) {
								continue;
							}
							if(StringUtil.isEmpty(order1.getSavezipurl()))
							{}
							else
							{
								if(StringUtil.isEmpty(strurl))
								{
									strurl=order1.getSavezipurl();
								}
							}
							String rowresult="成功!";
							
							WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
				                    10, WritableFont.NO_BOLD);// 字体样式
							
							WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
							redcolor.setBackground(Colour.RED);
							
							WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
							oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
							
							if(order1.getOrder()==null)
							{
								sheet.addCell(new Label(0, row, ""));//海关单号
								sheet.addCell(new Label(1, row, order1.getOrderId()));//运单号
								sheet.addCell(new Label(25, row, order1.getOrderResult(),redcolor));//导出结果
								sheet.addCell(new Label(26, row, order1.getCardResult(),redcolor));//导出结果
								sheet.addCell(new Label(27, row, "失败",redcolor));//导出失败
								sheet.addCell(new Label(28, row, "海关单号失败",redcolor));//导出失败
								row++;
								continue;
							}
							else
							{
								
								//检查状态
								try
								{
									if(Double.parseDouble(order1.getOrder().getState())<2)//没有揽收的包裹
									{
										if(StringUtil.isEmpty(order1.getOrderResult()))
										{
											order1.setOrderResult("状态异常，必须是已揽收的");
										}
										else
										{
											order1.setOrderResult(order1.getOrderResult()+",必须是已揽收的");
										}
										//sheet.addCell(new Label(0, row, ""));//海关单号
										sheet.addCell(new Label(0, row, order1.getOrder().getSorderId()));//海关单号
										sheet.addCell(new Label(1, row, order1.getOrderId()));//运单号
										sheet.addCell(new Label(24, row, order1.getOrder().getSorderId()));//海关单号
										sheet.addCell(new Label(25, row, order1.getOrderResult(),redcolor));//导出结果
										sheet.addCell(new Label(26, row, order1.getCardResult(),redcolor));//导出结果
										sheet.addCell(new Label(27, row, "失败",redcolor));//导出失败
										sheet.addCell(new Label(28, row, order1.getGetSeaNoResult(),redcolor));//导出失败
										row++;
										continue;
									}
								}
								catch(Exception e)
								{
									if(StringUtil.isEmpty(order1.getOrderResult()))
									{
										order1.setOrderResult("状态异常，必须是已揽收的");
									}
									else
									{
										order1.setOrderResult(order1.getOrderResult()+",必须是已揽收的");
									}
									sheet.addCell(new Label(0, row, order1.getOrder().getSorderId()));//海关单号
									sheet.addCell(new Label(1, row, order1.getOrderId()));//运单号
									sheet.addCell(new Label(24, row, order1.getOrder().getSorderId()));//海关单号
									sheet.addCell(new Label(25, row, order1.getOrderResult(),redcolor));//导出结果
									sheet.addCell(new Label(26, row, order1.getCardResult(),redcolor));//导出结果
									sheet.addCell(new Label(27, row, "失败",redcolor));//导出失败
									sheet.addCell(new Label(28, row, order1.getGetSeaNoResult(),redcolor));//导出失败
									row++;
									continue;
								}
								
								if(!StringUtil.isEmpty(order1.getOrderResult())&&(order1.getOrderResult().indexOf("失败")!=-1||order1.getOrderResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(25, row, order1.getOrderResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(25, row, order1.getOrderResult()));
								}
								if(!StringUtil.isEmpty(order1.getCardResult())&&(order1.getCardResult().indexOf("失败")!=-1||order1.getCardResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(26, row, order1.getCardResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(26, row, order1.getCardResult()));
								}
								
								if(!StringUtil.isEmpty(order1.getGetSeaNoResult())&&(order1.getGetSeaNoResult().indexOf("失败")!=-1||order1.getGetSeaNoResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(28, row, order1.getGetSeaNoResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(28, row, order1.getGetSeaNoResult()));
								}
								
							}
							
							
							int columnIndex = 0;
							//sheet.addCell(new Label(columnIndex++, row, ""));//海关单号
							sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getSorderId()));//海关单号
							sheet.addCell(new Label(24, row, order1.getOrder().getSorderId()));//海关单号
							sheet.addCell(new Label(columnIndex++, row, order1.getOrderId()));//运单号
							int addindex=0;
							int startindex=columnIndex;
							for(M_OrderDetail d :order1.getOrder().getDetail())
							{
								if(d==null)
								{
									continue;
								}
								String qvalue="";
								try{
									
								
									if((!StringUtil.isEmpty(d.getQuantity()))&&(!StringUtil.isEmpty(d.getValue())))
									{
										if((Double.parseDouble(d.getQuantity())!=0)&&(Double.parseDouble(d.getValue())!=0))
										{
											double aaa=Double.parseDouble(d.getValue())/Double.parseDouble(d.getQuantity());
											if(aaa>=1)
											{
												qvalue=Integer.toString((int)aaa);
											}
											else
											{
												qvalue=Double.toString(aaa);
											}
										}
									}
									else
									{
										rowresult="失败!商品数量信息异常";
									}
									sheet.addCell(new Label(columnIndex++, row+addindex, d.getProductName()));//物品名称
									sheet.addCell(new Label(columnIndex++, row+addindex, qvalue));//申报单价
									sheet.addCell(new Label(columnIndex++, row+addindex, d.getQuantity()));//数量
									sheet.addCell(new Label(columnIndex++, row+addindex, d.getValue()));//申报实价
								}
								catch(Exception e)
								{
									sheet.addCell(new Label(columnIndex++, row+addindex, ""));//物品名称
									sheet.addCell(new Label(columnIndex++, row+addindex, ""));//申报单价
									sheet.addCell(new Label(columnIndex++, row+addindex, ""));//数量
									sheet.addCell(new Label(columnIndex++, row+addindex, ""));//申报实价
									rowresult="失败!商品数量信息异常";
								}
								columnIndex=startindex;
								addindex++;
								
							}
							columnIndex=startindex+4;
							
					
							
							try{
								String xweight="0";
								boolean flag=false;
								xweight=order1.getOrder().getI_weight();
								try{
									double temp=Double.parseDouble(xweight);
									if(temp>0)
									{
										flag=true;
									}
								}catch(Exception e){}
								if(flag==false)
								{
								
									if(StringUtil.isEmpty(order1.getOrder().getSjweight())||order1.getOrder().getSjweight().equalsIgnoreCase("0")||order1.getOrder().getSjweight().equalsIgnoreCase("0.00"))
									{
										xweight=order1.getOrder().getWeight();
									}
									else
									{
										xweight=order1.getOrder().getSjweight();
									}
								
								}
								
								
								//double weight=Double.parseDouble(order1.getOrder().getWeight());
								double weight=Double.parseDouble(xweight);
								if(weight<=0)
								{
									rowresult="重量信息异常";
								}
								weight=weight*0.4536;
								
								
								  BigDecimal   bd   =   new   BigDecimal(weight);   
								  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
								  sheet.addCell(new Label(columnIndex++, row, String.valueOf(bd)));//千克
							}catch(Exception e)
							{
								sheet.addCell(new Label(columnIndex++, row, ""));//数字发生异常
								rowresult="重量信息异常";
							}
							
							
							sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getSuser().getName()));//发货人姓名
							//要匹配身份证
							if((!StringUtil.isEmpty(order1.getCardmodel()))&&(order1.getCardmodel().equals("0")||order1.getCardmodel().equals("1")))
							{
								if(order1.getOrder().getRuser().getCardlib()!=null)//使用身份证信息
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardlib().getCname()));//收货人姓名
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardlib().getCardid()));//收货人身份证号
								}
								else
								{
									/*if(!StringUtil.isEmpty(order1.getOrder().getRuser().getCardid())&&!StringUtil.isEmpty(order1.getOrder().getRuser().getCardtogether()))
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardid()));//收货人身份证号
										
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人姓名
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
										rowresult="失败!没有身份证信息!";
									}*/
									
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
									sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
									rowresult="失败!没有身份证信息!";
								}
							}
							else
							{
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardid()));//收货人身份证号
							}
							
							String address="";
							
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getState()))
							{
								address=address+order1.getOrder().getRuser().getState();
							}
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getCity()))
							{
								address=address+order1.getOrder().getRuser().getCity();
							}
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getDist()))
							{
								address=address+order1.getOrder().getRuser().getDist();
							}
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getAddress()))
							{
								address=address+order1.getOrder().getRuser().getAddress();
							}
							
							sheet.addCell(new Label(columnIndex++, row, address));//收货人地址
							
							
							
							sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
							sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getZipcode()));//收货人邮编
							sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCity()));//收货人城市
							
							sheet.addCell(new Label(24, row, order1.getOrder().getSorderId()));//海关单号
							if(!StringUtil.isEmpty(rowresult)&&(rowresult.indexOf("失败")!=-1||rowresult.indexOf("异常")!=-1))
							{
								sheet.addCell(new Label(27, row, rowresult,redcolor));
							}
							else
							{
								sheet.addCell(new Label(27, row, rowresult));
							}
							

							if(addindex==0)
							{
								row++;
							}
							else
							{
								row=addindex+row;
							}
						}
					
						if(!StringUtil.isEmpty(strurl))
						{
							strurl="图片保存路径："+strurl;
							sheet.addCell(new Label(0, row, strurl));
						}
					}
				}, templetFile, orders, os);
			}
			
			
			
		}
	
	
	
		
		
		// 导出内蒙古的核对模板
		public static void exportneimenggu(List<ExportMorder> orders,
				File templetFile, OutputStream os) throws Exception {
			if (templetFile != null && templetFile.exists()) {
				// 模板复制
				ExcelUtil.exportExcel(new ExcelWrite<ExportMorder>() {
					@Override
					public void write(Map<String, String> headers,
							Collection<ExportMorder> datas, WritableSheet sheet)
							throws Exception {
						if (datas == null || datas.isEmpty()) {
							sheet.mergeCells(0, 1, 11, 1);
							Label cell = new Label(0, 1, "没有导入数据.....");
							sheet.addCell(cell);
							return;
						}
						
						int row = 2;

						//String zipurl="";
						String strurl="";
						for (ExportMorder order1 : datas) {
							if (order1 == null) {
								continue;
							}
						//	if(!StringUtil.isEmpty(order.getZipurl())&&(zipurl.equalsIgnoreCase("")))
							//{
							//	zipurl=order.getZipurl();
							//}
							WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
				                    10, WritableFont.NO_BOLD);// 字体样式
							
							WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
							redcolor.setBackground(Colour.RED);
							
							WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
							oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
							int columnIndex = 0;
							
							sheet.addCell(new Label(columnIndex++, row, String.valueOf(row-1)));//序号
							sheet.addCell(new Label(columnIndex++, row, order1.getOrderId()));//运单号
							//sheet.addCell(new Label(columnIndex++, row, ""));//分单号
							
							if(StringUtil.isEmpty(order1.getSavezipurl()))
							{}
							else
							{
								if(StringUtil.isEmpty(strurl))
								{
									strurl=order1.getSavezipurl();
								}
							}
							String rowresult="成功!";
							if(order1.getOrder()==null)
							{
								sheet.addCell(new Label(columnIndex++, row, ""));//分单号
								if(!StringUtil.isEmpty(order1.getOrderResult())&&(order1.getOrderResult().indexOf("失败")!=-1||order1.getOrderResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(18, row, order1.getOrderResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(18, row, order1.getOrderResult()));
								}
								if(!StringUtil.isEmpty(order1.getCardResult())&&(order1.getCardResult().indexOf("失败")!=-1||order1.getCardResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(19, row, order1.getCardResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(19, row, order1.getCardResult()));
								}
								sheet.addCell(new Label(20, row, "失败!",redcolor));
								sheet.addCell(new Label(21, row, "海关单号失败",redcolor));//导出失败
								row++;
								continue;
							}
							else
							{
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getSorderId()));//分单号
								try
								{
									if(Double.parseDouble(order1.getOrder().getState())<2)//没有揽收的包裹
									{
										if(StringUtil.isEmpty(order1.getOrderResult()))
										{
											order1.setOrderResult("状态异常，必须是已揽收的");
										}
										else
										{
											order1.setOrderResult(order1.getOrderResult()+",必须是已揽收的");
										}
										
										sheet.addCell(new Label(18, row, order1.getOrderResult(),redcolor));//导出结果
										sheet.addCell(new Label(19, row, order1.getCardResult(),redcolor));//导出结果
										sheet.addCell(new Label(20, row, "失败",redcolor));//导出失败
										sheet.addCell(new Label(21, row, order1.getGetSeaNoResult(),redcolor));//导出失败
										row++;
										continue;
									}
								}
								catch(Exception e)
								{
									if(StringUtil.isEmpty(order1.getOrderResult()))
									{
										order1.setOrderResult("状态异常，必须是已揽收的");
									}
									else
									{
										order1.setOrderResult(order1.getOrderResult()+",必须是已揽收的");
									}
									sheet.addCell(new Label(18, row, order1.getOrderResult(),redcolor));//导出结果
									sheet.addCell(new Label(19, row, order1.getCardResult(),redcolor));//导出结果
									sheet.addCell(new Label(20, row, "失败",redcolor));//导出失败
									sheet.addCell(new Label(21, row, order1.getGetSeaNoResult(),redcolor));//导出失败
									row++;
									continue;
								}
							}
							
							
							//获取报关品名
							String productname="";
							if(order1.getOrder().getDetail().size()>1)
							{
								boolean flag=false;
								for(String name:conf_serio)
								{
									for(M_OrderDetail d:order1.getOrder().getDetail())
									{
										if(d.getName().indexOf(name)!=-1)//查找到匹配的选项
										{
											productname=d.getProductName();
											if(!StringUtil.isEmpty(productname))
											{
												flag=true;
												break;
											}
										}
									}
									if(flag==true)
									{
										break;
									}
								}
							}
							else
							{
								if(order1.getOrder().getDetail().size()==1)
								{
									productname=order1.getOrder().getDetail().get(0).getProductName();
								}
								else
								{
									rowresult="失败，商品信息发生异常";
								}
							}
							if(StringUtil.isEmpty(productname))
							{
								if(order1.getOrder().getDetail().size()!=0)
								{
									productname=order1.getOrder().getDetail().get(0).getProductName();
								}
							}
							
							sheet.addCell(new Label(columnIndex++, row, productname));//报关品名
							
							//获取品名
							String prduct="";
							String brandstr="";
							
							for(M_OrderDetail d:order1.getOrder().getDetail())
							{
								if(prduct.equalsIgnoreCase(""))
								{
									prduct=d.getProductName();
								}
								else
								{
									prduct=prduct+";"+d.getProductName();
								}
								if(brandstr.equalsIgnoreCase(""))
								{
									brandstr=d.getBrands();
								}
								else
								{
									brandstr=prduct+";"+d.getBrands();
								}
								
							}
							
							
							sheet.addCell(new Label(columnIndex++, row, prduct));//品名
							sheet.addCell(new Label(columnIndex++, row, brandstr));//品牌
							sheet.addCell(new Label(columnIndex++, row, ""));//规格
							
							if(StringUtil.isEmpty(order1.getOrder().getQuantity())||order1.getOrder().getQuantity().equalsIgnoreCase("0")||order1.getOrder().getQuantity().equalsIgnoreCase("0.00"))
							{
								sheet.addCell(new Label(columnIndex++, row, ""));//件数
							}
							else
							{
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getQuantity()));//件数
							}
							//获取重量
							if(StringUtil.isEmpty(order1.getOrder().getWeight())||order1.getOrder().getWeight().equalsIgnoreCase("0")||order1.getOrder().getWeight().equalsIgnoreCase("0.00"))
							{
								sheet.addCell(new Label(columnIndex++, row, ""));//重量
							}
							else
							{
								try{
									
									String xweight="0";
									boolean flag=false;
									xweight=order1.getOrder().getI_weight();
									try{
										double temp=Double.parseDouble(xweight);
										if(temp>0)
										{
											flag=true;
										}
									}catch(Exception e){}
									if(flag==false)
									{
										
										if(StringUtil.isEmpty(order1.getOrder().getSjweight())||order1.getOrder().getSjweight().equalsIgnoreCase("0")||order1.getOrder().getSjweight().equalsIgnoreCase("0.00"))
										{
											xweight=order1.getOrder().getWeight();
										}
										else
										{
											xweight=order1.getOrder().getSjweight();
										}
									}
									
									//double weight=Double.parseDouble(order1.getOrder().getWeight());
									double weight=Double.parseDouble(xweight);
									if(weight<=0)
									{
										rowresult="失败，商品重量发生异常!";
									}
									weight=weight*0.4536;
									
									  BigDecimal   bd   =   new   BigDecimal(weight);   
									  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
									  sheet.addCell(new Label(columnIndex++, row, String.valueOf(bd)));//千克
								}catch(Exception e)
								{
									rowresult="失败，商品重量发生异常!";
									sheet.addCell(new Label(columnIndex++, row, ""));//数字发生异常
								}
							}
							
							
							
							sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getValue()));//价值
							
							sheet.addCell(new Label(columnIndex++, row, order1.getCompanyName()));//发货公司
							sheet.addCell(new Label(columnIndex++, row, order1.getCompanyAddress()));//发货地址
							sheet.addCell(new Label(columnIndex++, row, ""));//收货人公司
							String address="";
							
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getState()))
							{
								address=address+order1.getOrder().getRuser().getState();
							}
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getCity()))
							{
								address=address+order1.getOrder().getRuser().getCity();
							}
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getDist()))
							{
								address=address+order1.getOrder().getRuser().getDist();
							}
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getAddress()))
							{
								address=address+order1.getOrder().getRuser().getAddress();
							}
							
							sheet.addCell(new Label(columnIndex++, row, address));//收货人地址
							
							
							//要匹配身份证
							if((!StringUtil.isEmpty(order1.getCardmodel()))&&(order1.getCardmodel().equals("0")||order1.getCardmodel().equals("1")))
							{
								if(order1.getOrder().getRuser().getCardlib()!=null)//使用身份证信息
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardlib().getCname()));//收货人姓名
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
									sheet.addCell(new Label(columnIndex++, row, ""));//箱号
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardlib().getCardid()));//收货人身份证号
								}
								else
								{
									/*if(!StringUtil.isEmpty(order1.getOrder().getRuser().getCardid())&&!StringUtil.isEmpty(order1.getOrder().getRuser().getCardtogether()))
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
										sheet.addCell(new Label(columnIndex++, row, ""));//箱号
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardid()));//收货人身份证号
									}
									else
									{
										rowresult="失败，匹配身份证失败!";
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人姓名
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
										sheet.addCell(new Label(columnIndex++, row, ""));//箱号
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
									}*/
									rowresult="失败，匹配身份证失败!";
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
									sheet.addCell(new Label(columnIndex++, row, ""));//箱号
									sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
									
									
								}
							}
							else
							{
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
								sheet.addCell(new Label(columnIndex++, row, ""));//箱号
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardid()));//收货人身份证号
							}
							
							
							
							if(order1.getOrder()==null)
							{
								sheet.addCell(new Label(columnIndex++, row, order1.getOrderResult(),redcolor));//导出结果
								sheet.addCell(new Label(columnIndex++, row, order1.getCardResult(),redcolor));//导出结果
							}
							else
							{
								
								
								if(!StringUtil.isEmpty(order1.getOrderResult())&&(order1.getOrderResult().indexOf("失败")!=-1||order1.getOrderResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getOrderResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getOrderResult()));
								}
								if(!StringUtil.isEmpty(order1.getCardResult())&&(order1.getCardResult().indexOf("失败")!=-1||order1.getCardResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getCardResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getCardResult()));
								}
							}
							
							if(!StringUtil.isEmpty(rowresult)&&(rowresult.indexOf("失败")!=-1||rowresult.indexOf("异常")!=-1))
							{
								sheet.addCell(new Label(20, row,rowresult,redcolor));
							}
							else
							{
								sheet.addCell(new Label(20, row, rowresult));
							}
							
							if(!StringUtil.isEmpty(order1.getGetSeaNoResult())&&(order1.getGetSeaNoResult().indexOf("失败")!=-1||order1.getGetSeaNoResult().indexOf("异常")!=-1))
							{
								sheet.addCell(new Label(21, row,order1.getGetSeaNoResult(),redcolor));
							}
							else
							{
								sheet.addCell(new Label(21, row, order1.getGetSeaNoResult()));
							}

							row++;
						}
						if(!StringUtil.isEmpty(strurl))
						{
							strurl="图片保存路径："+strurl;
							sheet.addCell(new Label(0, row, strurl));
						}
						/*if((!zipurl.equalsIgnoreCase("")))
						{
							WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
				                    10, WritableFont.NO_BOLD);// 字体样式
							WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
							oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
							sheet.addCell(new Label(0, row, zipurl,oceanbluecolor));//显示压缩包路径
							
						}*/
					}
				}, templetFile, orders, os);
			} else {
				// 模板复制
				ExcelUtil.exportExcel(new ExcelWrite<ExportMorder>() {
					@Override
					public void write(Map<String, String> headers,
							Collection<ExportMorder> datas, WritableSheet sheet)
							throws Exception {
						if (datas == null || datas.isEmpty()) {
							sheet.mergeCells(0, 1, 11, 1);
							Label cell = new Label(0, 1, "没有导入数据.....");
							sheet.addCell(cell);
							return;
						}
						
						int row = 2;

						//String zipurl="";
						String strurl="";
						for (ExportMorder order1 : datas) {
							if (order1 == null) {
								continue;
							}
						//	if(!StringUtil.isEmpty(order.getZipurl())&&(zipurl.equalsIgnoreCase("")))
							//{
							//	zipurl=order.getZipurl();
							//}
							WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
				                    10, WritableFont.NO_BOLD);// 字体样式
							
							WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
							redcolor.setBackground(Colour.RED);
							
							WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
							oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
							int columnIndex = 0;
							
							sheet.addCell(new Label(columnIndex++, row, String.valueOf(row-1)));//序号
							sheet.addCell(new Label(columnIndex++, row, order1.getOrderId()));//运单号
							//sheet.addCell(new Label(columnIndex++, row, ""));//分单号
							
							if(StringUtil.isEmpty(order1.getSavezipurl()))
							{}
							else
							{
								if(StringUtil.isEmpty(strurl))
								{
									strurl=order1.getSavezipurl();
								}
							}
							String rowresult="成功!";
							if(order1.getOrder()==null)
							{
								sheet.addCell(new Label(columnIndex++, row, ""));//分单号
								if(!StringUtil.isEmpty(order1.getOrderResult())&&(order1.getOrderResult().indexOf("失败")!=-1||order1.getOrderResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(18, row, order1.getOrderResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(18, row, order1.getOrderResult()));
								}
								if(!StringUtil.isEmpty(order1.getCardResult())&&(order1.getCardResult().indexOf("失败")!=-1||order1.getCardResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(19, row, order1.getCardResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(19, row, order1.getCardResult()));
								}
								sheet.addCell(new Label(20, row, "失败!",redcolor));
								sheet.addCell(new Label(21, row, "海关单号失败",redcolor));//导出失败
								row++;
								continue;
							}
							else
							{
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getSorderId()));//分单号
								try
								{
									if(Double.parseDouble(order1.getOrder().getState())<2)//没有揽收的包裹
									{
										if(StringUtil.isEmpty(order1.getOrderResult()))
										{
											order1.setOrderResult("状态异常，必须是已揽收的");
										}
										else
										{
											order1.setOrderResult(order1.getOrderResult()+",必须是已揽收的");
										}
										
										sheet.addCell(new Label(18, row, order1.getOrderResult(),redcolor));//导出结果
										sheet.addCell(new Label(19, row, order1.getCardResult(),redcolor));//导出结果
										sheet.addCell(new Label(20, row, "失败",redcolor));//导出失败
										sheet.addCell(new Label(21, row, order1.getGetSeaNoResult(),redcolor));//导出失败
										row++;
										continue;
									}
								}
								catch(Exception e)
								{
									if(StringUtil.isEmpty(order1.getOrderResult()))
									{
										order1.setOrderResult("状态异常，必须是已揽收的");
									}
									else
									{
										order1.setOrderResult(order1.getOrderResult()+",必须是已揽收的");
									}
									sheet.addCell(new Label(18, row, order1.getOrderResult(),redcolor));//导出结果
									sheet.addCell(new Label(19, row, order1.getCardResult(),redcolor));//导出结果
									sheet.addCell(new Label(20, row, "失败",redcolor));//导出失败
									sheet.addCell(new Label(21, row, order1.getGetSeaNoResult(),redcolor));//导出失败
									row++;
									continue;
								}
							}
							
							
							//获取报关品名
							String productname="";
							if(order1.getOrder().getDetail().size()>1)
							{
								boolean flag=false;
								for(String name:conf_serio)
								{
									for(M_OrderDetail d:order1.getOrder().getDetail())
									{
										if(d.getName().indexOf(name)!=-1)//查找到匹配的选项
										{
											productname=d.getProductName();
											if(!StringUtil.isEmpty(productname))
											{
												flag=true;
												break;
											}
										}
									}
									if(flag==true)
									{
										break;
									}
								}
							}
							else
							{
								if(order1.getOrder().getDetail().size()==1)
								{
									productname=order1.getOrder().getDetail().get(0).getProductName();
								}
								else
								{
									rowresult="失败，商品信息发生异常";
								}
							}
							if(StringUtil.isEmpty(productname))
							{
								if(order1.getOrder().getDetail().size()!=0)
								{
									productname=order1.getOrder().getDetail().get(0).getProductName();
								}
							}
							
							sheet.addCell(new Label(columnIndex++, row, productname));//报关品名
							
							//获取品名
							String prduct="";
							String brandstr="";
							
							for(M_OrderDetail d:order1.getOrder().getDetail())
							{
								if(prduct.equalsIgnoreCase(""))
								{
									prduct=d.getProductName();
								}
								else
								{
									prduct=prduct+";"+d.getProductName();
								}
								if(brandstr.equalsIgnoreCase(""))
								{
									brandstr=d.getBrands();
								}
								else
								{
									brandstr=prduct+";"+d.getBrands();
								}
								
							}
							
							
							sheet.addCell(new Label(columnIndex++, row, prduct));//品名
							sheet.addCell(new Label(columnIndex++, row, brandstr));//品牌
							sheet.addCell(new Label(columnIndex++, row, ""));//规格
							
							if(StringUtil.isEmpty(order1.getOrder().getQuantity())||order1.getOrder().getQuantity().equalsIgnoreCase("0")||order1.getOrder().getQuantity().equalsIgnoreCase("0.00"))
							{
								sheet.addCell(new Label(columnIndex++, row, ""));//件数
							}
							else
							{
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getQuantity()));//件数
							}
							//获取重量
							if(StringUtil.isEmpty(order1.getOrder().getWeight())||order1.getOrder().getWeight().equalsIgnoreCase("0")||order1.getOrder().getWeight().equalsIgnoreCase("0.00"))
							{
								sheet.addCell(new Label(columnIndex++, row, ""));//重量
							}
							else
							{
								try{
									
									String xweight="0";
									
									if(StringUtil.isEmpty(order1.getOrder().getSjweight())||order1.getOrder().getSjweight().equalsIgnoreCase("0")||order1.getOrder().getSjweight().equalsIgnoreCase("0.00"))
									{
										xweight=order1.getOrder().getWeight();
									}
									else
									{
										xweight=order1.getOrder().getSjweight();
									}
									
									
									//double weight=Double.parseDouble(order1.getOrder().getWeight());
									double weight=Double.parseDouble(xweight);
									if(weight<=0)
									{
										rowresult="失败，商品重量发生异常!";
									}
									weight=weight*0.4536;
									
									  BigDecimal   bd   =   new   BigDecimal(weight);   
									  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
									  sheet.addCell(new Label(columnIndex++, row, String.valueOf(bd)));//千克
								}catch(Exception e)
								{
									rowresult="失败，商品重量发生异常!";
									sheet.addCell(new Label(columnIndex++, row, ""));//数字发生异常
								}
							}
							
							
							
							sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getValue()));//价值
							
							sheet.addCell(new Label(columnIndex++, row, order1.getCompanyName()));//发货公司
							sheet.addCell(new Label(columnIndex++, row, order1.getCompanyAddress()));//发货地址
							sheet.addCell(new Label(columnIndex++, row, ""));//收货人公司
							String address="";
							
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getState()))
							{
								address=address+order1.getOrder().getRuser().getState();
							}
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getCity()))
							{
								address=address+order1.getOrder().getRuser().getCity();
							}
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getDist()))
							{
								address=address+order1.getOrder().getRuser().getDist();
							}
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getAddress()))
							{
								address=address+order1.getOrder().getRuser().getAddress();
							}
							
							sheet.addCell(new Label(columnIndex++, row, address));//收货人地址
							
							
							//要匹配身份证
							if((!StringUtil.isEmpty(order1.getCardmodel()))&&(order1.getCardmodel().equals("0")||order1.getCardmodel().equals("1")))
							{
								if(order1.getOrder().getRuser().getCardlib()!=null)//使用身份证信息
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardlib().getCname()));//收货人姓名
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
									sheet.addCell(new Label(columnIndex++, row, ""));//箱号
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardlib().getCardid()));//收货人身份证号
								}
								else
								{
									/*if(!StringUtil.isEmpty(order1.getOrder().getRuser().getCardid())&&!StringUtil.isEmpty(order1.getOrder().getRuser().getCardtogether()))
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
										sheet.addCell(new Label(columnIndex++, row, ""));//箱号
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardid()));//收货人身份证号
									}
									else
									{
										rowresult="失败，匹配身份证失败!";
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人姓名
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
										sheet.addCell(new Label(columnIndex++, row, ""));//箱号
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
									}*/
									rowresult="失败，匹配身份证失败!";
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
									sheet.addCell(new Label(columnIndex++, row, ""));//箱号
									sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
									
									
								}
							}
							else
							{
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
								sheet.addCell(new Label(columnIndex++, row, ""));//箱号
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardid()));//收货人身份证号
							}
							
							
							
							if(order1.getOrder()==null)
							{
								sheet.addCell(new Label(columnIndex++, row, order1.getOrderResult(),redcolor));//导出结果
								sheet.addCell(new Label(columnIndex++, row, order1.getCardResult(),redcolor));//导出结果
							}
							else
							{
								
								
								if(!StringUtil.isEmpty(order1.getOrderResult())&&(order1.getOrderResult().indexOf("失败")!=-1||order1.getOrderResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getOrderResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getOrderResult()));
								}
								if(!StringUtil.isEmpty(order1.getCardResult())&&(order1.getCardResult().indexOf("失败")!=-1||order1.getCardResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getCardResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getCardResult()));
								}
							}
							
							if(!StringUtil.isEmpty(rowresult)&&(rowresult.indexOf("失败")!=-1||rowresult.indexOf("异常")!=-1))
							{
								sheet.addCell(new Label(20, row,rowresult,redcolor));
							}
							else
							{
								sheet.addCell(new Label(20, row, rowresult));
							}
							
							if(!StringUtil.isEmpty(order1.getGetSeaNoResult())&&(order1.getGetSeaNoResult().indexOf("失败")!=-1||order1.getGetSeaNoResult().indexOf("异常")!=-1))
							{
								sheet.addCell(new Label(21, row,order1.getGetSeaNoResult(),redcolor));
							}
							else
							{
								sheet.addCell(new Label(21, row, order1.getGetSeaNoResult()));
							}

							row++;
						}
						if(!StringUtil.isEmpty(strurl))
						{
							strurl="图片保存路径："+strurl;
							sheet.addCell(new Label(0, row, strurl));
						}
						/*if((!zipurl.equalsIgnoreCase("")))
						{
							WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
				                    10, WritableFont.NO_BOLD);// 字体样式
							WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
							oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
							sheet.addCell(new Label(0, row, zipurl,oceanbluecolor));//显示压缩包路径
							
						}*/
					}
				}, templetFile, orders, os);
			}
			
			
			
		}
		
		
		
		// 导出深圳的核对模板
		public static void exportshenzhen(List<ExportMorder> orders,
				File templetFile, OutputStream os) throws Exception {
			if (templetFile != null && templetFile.exists()) {
				// 模板复制
				ExcelUtil.exportExcel(new ExcelWrite<ExportMorder>() {
					@Override
					public void write(Map<String, String> headers,
							Collection<ExportMorder> datas, WritableSheet sheet)
							throws Exception {
						if (datas == null || datas.isEmpty()) {
							sheet.mergeCells(0, 1, 11, 1);
							Label cell = new Label(0, 1, "没有导入数据.....");
							sheet.addCell(cell);
							return;
						}
						
						int row = 2;

						//String zipurl="";
						String strurl="";
						for (ExportMorder order1 : datas) {
							if (order1 == null) {
								continue;
							}
						//	if(!StringUtil.isEmpty(order.getZipurl())&&(zipurl.equalsIgnoreCase("")))
							//{
							//	zipurl=order.getZipurl();
							//}
							WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
				                    10, WritableFont.NO_BOLD);// 字体样式
							
							WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
							redcolor.setBackground(Colour.RED);
							
							WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
							oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
							int columnIndex = 0;
							
							sheet.addCell(new Label(columnIndex++, row, String.valueOf(row-1)));//序号
							
							//sheet.addCell(new Label(columnIndex++, row, ""));//分单号
							
							if(StringUtil.isEmpty(order1.getSavezipurl()))
							{}
							else
							{
								if(StringUtil.isEmpty(strurl))
								{
									strurl=order1.getSavezipurl();
								}
							}
							
							
							if(order1.getOrder()==null)
							{
								sheet.addCell(new Label(columnIndex++, row, ""));//分单号
								sheet.addCell(new Label(columnIndex++, row, order1.getOrderId()));//运单号
								if(!StringUtil.isEmpty(order1.getOrderResult())&&(order1.getOrderResult().indexOf("失败")!=-1||order1.getOrderResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(18, row, order1.getOrderResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(18, row, order1.getOrderResult()));
								}
								if(!StringUtil.isEmpty(order1.getCardResult())&&(order1.getCardResult().indexOf("失败")!=-1||order1.getCardResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(19, row, order1.getCardResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(19, row, order1.getCardResult()));
								}
								sheet.addCell(new Label(20, row, "失败!",redcolor));
								row++;
								continue;
							}
							else
							{
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getSorderId()));//分单号
								sheet.addCell(new Label(columnIndex++, row, order1.getOrderId()));//运单号
								try
								{
									if(Double.parseDouble(order1.getOrder().getState())<2)//没有揽收的包裹
									{
										if(StringUtil.isEmpty(order1.getOrderResult()))
										{
											order1.setOrderResult("状态异常，必须是已揽收的");
										}
										else
										{
											order1.setOrderResult(order1.getOrderResult()+",必须是已揽收的");
										}
										
										sheet.addCell(new Label(18, row, order1.getOrderResult(),redcolor));//导出结果
										sheet.addCell(new Label(19, row, order1.getCardResult(),redcolor));//导出结果
										sheet.addCell(new Label(21, row, "失败",redcolor));//导出失败
										row++;
										continue;
									}
								}
								catch(Exception e)
								{
									if(StringUtil.isEmpty(order1.getOrderResult()))
									{
										order1.setOrderResult("状态异常，必须是已揽收的");
									}
									else
									{
										order1.setOrderResult(order1.getOrderResult()+",必须是已揽收的");
									}
									sheet.addCell(new Label(18, row, order1.getOrderResult(),redcolor));//导出结果
									sheet.addCell(new Label(19, row, order1.getCardResult(),redcolor));//导出结果
									sheet.addCell(new Label(21, row, "失败",redcolor));//导出失败
									row++;
									continue;
								}
							}
							
							String rowresult="成功!";
							
							//获取报关品名
							String productname="";
							String brandsName="";
							if(order1.getOrder().getDetail().size()>1)
							{
								boolean flag=false;
								for(String name:conf_serio)
								{
									for(M_OrderDetail d:order1.getOrder().getDetail())
									{
										if(d.getName().indexOf(name)!=-1)//查找到匹配的选项
										{
											productname=d.getProductName();
											brandsName=d.getBrands();
											if(!StringUtil.isEmpty(productname))
											{
												flag=true;
												break;
											}
										}
									}
									if(flag==true)
									{
										break;
									}
								}
							}
							else
							{
								if(order1.getOrder().getDetail().size()==1)
								{
									productname=order1.getOrder().getDetail().get(0).getProductName();
									brandsName=order1.getOrder().getDetail().get(0).getBrands();
								}
								else
								{
									rowresult="失败，商品信息发生异常";
								}
							}
							if(StringUtil.isEmpty(productname))
							{
								if(order1.getOrder().getDetail().size()!=0)
								{
									productname=order1.getOrder().getDetail().get(0).getProductName();
								}
							}
							
							if(StringUtil.isEmpty(brandsName))
							{
								if(order1.getOrder().getDetail().size()!=0)
								{
									brandsName=order1.getOrder().getDetail().get(0).getBrands();
								}
							}
							
							sheet.addCell(new Label(columnIndex++, row, productname));//报关品名
							
							//修改详细品名规则，详细品名使用品名加：加品牌
							
							//获取品名
							String prduct="";
							String brandstr="";
							
							for(M_OrderDetail d:order1.getOrder().getDetail())
							{
								if(prduct.equalsIgnoreCase(""))
								{
									prduct=d.getProductName()+"*"+d.getQuantity();
								}
								else
								{
									//prduct=prduct+";"+d.getProductName();
									prduct=prduct+"；"+d.getProductName()+"*"+d.getQuantity();
								}
								if(brandstr.equalsIgnoreCase(""))
								{
									brandstr=d.getBrands();
								}
								else
								{
									brandstr=brandstr+";"+d.getBrands();
								}
								
							}
							
							
							sheet.addCell(new Label(columnIndex++, row, prduct));//品名
							sheet.addCell(new Label(columnIndex++, row, brandstr));//品牌
							//sheet.addCell(new Label(columnIndex++, row, brandsName));//品牌
							sheet.addCell(new Label(columnIndex++, row, ""));//规格
							
							if(StringUtil.isEmpty(order1.getOrder().getQuantity())||order1.getOrder().getQuantity().equalsIgnoreCase("0")||order1.getOrder().getQuantity().equalsIgnoreCase("0.00"))
							{
								sheet.addCell(new Label(columnIndex++, row, ""));//件数
							}
							else
							{
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getQuantity()));//件数
							}
							//获取重量
							if(StringUtil.isEmpty(order1.getOrder().getWeight())||order1.getOrder().getWeight().equalsIgnoreCase("0")||order1.getOrder().getWeight().equalsIgnoreCase("0.00"))
							{
								sheet.addCell(new Label(columnIndex++, row, ""));//重量
							}
							else
							{
								try{
									
									
									String xweight="0";
									boolean flag=false;
									xweight=order1.getOrder().getI_weight();
									try{
										double temp=Double.parseDouble(xweight);
										if(temp>0)
										{
											flag=true;
										}
									}catch(Exception e){}
									if(flag==false)
									{
										if(StringUtil.isEmpty(order1.getOrder().getSjweight())||order1.getOrder().getSjweight().equalsIgnoreCase("0")||order1.getOrder().getSjweight().equalsIgnoreCase("0.00"))
										{
											xweight=order1.getOrder().getWeight();
										}
										else
										{
											xweight=order1.getOrder().getSjweight();
										}
									}
									//double weight=Double.parseDouble(order1.getOrder().getWeight());
									double weight=Double.parseDouble(xweight);
									if(weight<=0)
									{
										rowresult="失败，商品重量发生异常";
									}
									
									weight=weight*0.4536;
									
									  BigDecimal   bd   =   new   BigDecimal(weight);   
									  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
									  sheet.addCell(new Label(columnIndex++, row, String.valueOf(bd)));//千克
								}catch(Exception e)
								{
									sheet.addCell(new Label(columnIndex++, row, ""));//数字发生异常
									
										rowresult="失败，商品重量发生异常";
									
								}
							}
							
							
							
							sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getValue()));//价值
							
							sheet.addCell(new Label(columnIndex++, row, order1.getCompanyName()));//发货公司
							sheet.addCell(new Label(columnIndex++, row, order1.getCompanyAddress()));//发货地址
							sheet.addCell(new Label(columnIndex++, row, ""));//收货人公司
							String address="";
							
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getState()))
							{
								address=address+order1.getOrder().getRuser().getState();
							}
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getCity()))
							{
								address=address+order1.getOrder().getRuser().getCity();
							}
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getDist()))
							{
								address=address+order1.getOrder().getRuser().getDist();
							}
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getAddress()))
							{
								address=address+order1.getOrder().getRuser().getAddress();
							}
							
							sheet.addCell(new Label(columnIndex++, row, address));//收货人地址
							
							
							//要匹配身份证
							if((!StringUtil.isEmpty(order1.getCardmodel()))&&(order1.getCardmodel().equals("0")||order1.getCardmodel().equals("1")))
							//if(false)//不匹配身分证
							{
								if(order1.getOrder().getRuser().getCardlib()!=null)//使用身份证信息
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardlib().getCname()));//收货人姓名
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
									sheet.addCell(new Label(columnIndex++, row, ""));//箱号
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardlib().getCardid()));//收货人身份证号
									//sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardlib().getCardid()));//收货人身份证号
								}
								else
								{
									
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
									sheet.addCell(new Label(columnIndex++, row, ""));//箱号
									sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
									
									/*if(!StringUtil.isEmpty(order1.getOrder().getRuser().getCardid())&&!StringUtil.isEmpty(order1.getOrder().getRuser().getCardtogether()))
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
										sheet.addCell(new Label(columnIndex++, row, ""));//箱号
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
										//sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardid()));//收货人身份证号
									}
									else
									{
										
											rowresult="失败，匹配身份证信息发生异常";
										
										//sheet.addCell(new Label(columnIndex++, row, ""));//收货人姓名
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
										sheet.addCell(new Label(columnIndex++, row, ""));//箱号
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
										//sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
									}*/
								}
							}
							else
							{
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
								sheet.addCell(new Label(columnIndex++, row, ""));//箱号
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardid()));//收货人身份证号
								//sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardid()));//收货人身份证号
							}
							
							
							
							if(order1.getOrder()==null)
							{
								sheet.addCell(new Label(columnIndex++, row, order1.getOrderResult(),redcolor));//导出结果
								sheet.addCell(new Label(columnIndex++, row, order1.getCardResult(),redcolor));//导出结果
								sheet.addCell(new Label(columnIndex++, row, order1.getGetSeaNoResult(),redcolor));//导出结果
							}
							else
							{
								
								
								if(!StringUtil.isEmpty(order1.getOrderResult())&&(order1.getOrderResult().indexOf("失败")!=-1||order1.getOrderResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getOrderResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getOrderResult()));
								}
								if(!StringUtil.isEmpty(order1.getCardResult())&&(order1.getCardResult().indexOf("失败")!=-1||order1.getCardResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getCardResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getCardResult()));
								}
								
								if(!StringUtil.isEmpty(order1.getGetSeaNoResult())&&(order1.getGetSeaNoResult().indexOf("失败")!=-1||order1.getGetSeaNoResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getGetSeaNoResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getGetSeaNoResult()));
								}
							}
							
							if(!StringUtil.isEmpty(rowresult)&&(rowresult.indexOf("失败")!=-1||rowresult.indexOf("异常")!=-1))
							{
								sheet.addCell(new Label(21, row, rowresult,redcolor));
							}
							else
							{
								sheet.addCell(new Label(21, row, rowresult));
							}
							

							row++;
						}
						
						if(!StringUtil.isEmpty(strurl))
						{
							strurl="图片保存路径："+strurl;
							sheet.addCell(new Label(0, row, strurl));
						}
						/*if((!zipurl.equalsIgnoreCase("")))
						{
							WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
				                    10, WritableFont.NO_BOLD);// 字体样式
							WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
							oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
							sheet.addCell(new Label(0, row, zipurl,oceanbluecolor));//显示压缩包路径
							
						}*/
					}
				}, templetFile, orders, os);
			} else {
				// 模板复制
				ExcelUtil.exportExcel(new ExcelWrite<ExportMorder>() {
					@Override
					public void write(Map<String, String> headers,
							Collection<ExportMorder> datas, WritableSheet sheet)
							throws Exception {
						if (datas == null || datas.isEmpty()) {
							sheet.mergeCells(0, 1, 11, 1);
							Label cell = new Label(0, 1, "没有导入数据.....");
							sheet.addCell(cell);
							return;
						}
						
						int row = 2;

						//String zipurl="";
						String strurl="";
						for (ExportMorder order1 : datas) {
							if (order1 == null) {
								continue;
							}
						//	if(!StringUtil.isEmpty(order.getZipurl())&&(zipurl.equalsIgnoreCase("")))
							//{
							//	zipurl=order.getZipurl();
							//}
							WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
				                    10, WritableFont.NO_BOLD);// 字体样式
							
							WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
							redcolor.setBackground(Colour.RED);
							
							WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
							oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
							int columnIndex = 0;
							
							sheet.addCell(new Label(columnIndex++, row, String.valueOf(row-1)));//序号
							
							//sheet.addCell(new Label(columnIndex++, row, ""));//分单号
							
							if(StringUtil.isEmpty(order1.getSavezipurl()))
							{}
							else
							{
								if(StringUtil.isEmpty(strurl))
								{
									strurl=order1.getSavezipurl();
								}
							}
							
							
							if(order1.getOrder()==null)
							{
								sheet.addCell(new Label(columnIndex++, row, ""));//分单号
								sheet.addCell(new Label(columnIndex++, row, order1.getOrderId()));//运单号
								if(!StringUtil.isEmpty(order1.getOrderResult())&&(order1.getOrderResult().indexOf("失败")!=-1||order1.getOrderResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(18, row, order1.getOrderResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(18, row, order1.getOrderResult()));
								}
								if(!StringUtil.isEmpty(order1.getCardResult())&&(order1.getCardResult().indexOf("失败")!=-1||order1.getCardResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(19, row, order1.getCardResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(19, row, order1.getCardResult()));
								}
								sheet.addCell(new Label(20, row, "失败!",redcolor));
								row++;
								continue;
							}
							else
							{
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getSorderId()));//分单号
								sheet.addCell(new Label(columnIndex++, row, order1.getOrderId()));//运单号
								try
								{
									if(Double.parseDouble(order1.getOrder().getState())<2)//没有揽收的包裹
									{
										if(StringUtil.isEmpty(order1.getOrderResult()))
										{
											order1.setOrderResult("状态异常，必须是已揽收的");
										}
										else
										{
											order1.setOrderResult(order1.getOrderResult()+",必须是已揽收的");
										}
										
										sheet.addCell(new Label(18, row, order1.getOrderResult(),redcolor));//导出结果
										sheet.addCell(new Label(19, row, order1.getCardResult(),redcolor));//导出结果
										sheet.addCell(new Label(21, row, "失败",redcolor));//导出失败
										row++;
										continue;
									}
								}
								catch(Exception e)
								{
									if(StringUtil.isEmpty(order1.getOrderResult()))
									{
										order1.setOrderResult("状态异常，必须是已揽收的");
									}
									else
									{
										order1.setOrderResult(order1.getOrderResult()+",必须是已揽收的");
									}
									sheet.addCell(new Label(18, row, order1.getOrderResult(),redcolor));//导出结果
									sheet.addCell(new Label(19, row, order1.getCardResult(),redcolor));//导出结果
									sheet.addCell(new Label(21, row, "失败",redcolor));//导出失败
									row++;
									continue;
								}
							}
							
							String rowresult="成功!";
							
							//获取报关品名
							String productname="";
							String brandsName="";
							if(order1.getOrder().getDetail().size()>1)
							{
								boolean flag=false;
								for(String name:conf_serio)
								{
									for(M_OrderDetail d:order1.getOrder().getDetail())
									{
										if(d.getName().indexOf(name)!=-1)//查找到匹配的选项
										{
											productname=d.getProductName();
											brandsName=d.getBrands();
											if(!StringUtil.isEmpty(productname))
											{
												flag=true;
												break;
											}
										}
									}
									if(flag==true)
									{
										break;
									}
								}
							}
							else
							{
								if(order1.getOrder().getDetail().size()==1)
								{
									productname=order1.getOrder().getDetail().get(0).getProductName();
									brandsName=order1.getOrder().getDetail().get(0).getBrands();
								}
								else
								{
									rowresult="失败，商品信息发生异常";
								}
							}
							if(StringUtil.isEmpty(productname))
							{
								if(order1.getOrder().getDetail().size()!=0)
								{
									productname=order1.getOrder().getDetail().get(0).getProductName();
								}
							}
							
							if(StringUtil.isEmpty(brandsName))
							{
								if(order1.getOrder().getDetail().size()!=0)
								{
									brandsName=order1.getOrder().getDetail().get(0).getBrands();
								}
							}
							
							sheet.addCell(new Label(columnIndex++, row, productname));//报关品名
							
							//修改详细品名规则，详细品名使用品名加：加品牌
							
							//获取品名
							String prduct="";
							String brandstr="";
							
							for(M_OrderDetail d:order1.getOrder().getDetail())
							{
								if(prduct.equalsIgnoreCase(""))
								{
									prduct=d.getProductName()+"*"+d.getQuantity();
								}
								else
								{
									//prduct=prduct+";"+d.getProductName();
									prduct=prduct+"；"+d.getProductName()+"*"+d.getQuantity();
								}
								if(brandstr.equalsIgnoreCase(""))
								{
									brandstr=d.getBrands();
								}
								else
								{
									brandstr=brandstr+";"+d.getBrands();
								}
								
							}
							
							
							sheet.addCell(new Label(columnIndex++, row, prduct));//品名
							sheet.addCell(new Label(columnIndex++, row, brandstr));//品牌
							//sheet.addCell(new Label(columnIndex++, row, brandsName));//品牌
							sheet.addCell(new Label(columnIndex++, row, ""));//规格
							
							if(StringUtil.isEmpty(order1.getOrder().getQuantity())||order1.getOrder().getQuantity().equalsIgnoreCase("0")||order1.getOrder().getQuantity().equalsIgnoreCase("0.00"))
							{
								sheet.addCell(new Label(columnIndex++, row, ""));//件数
							}
							else
							{
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getQuantity()));//件数
							}
							//获取重量
							if(StringUtil.isEmpty(order1.getOrder().getWeight())||order1.getOrder().getWeight().equalsIgnoreCase("0")||order1.getOrder().getWeight().equalsIgnoreCase("0.00"))
							{
								sheet.addCell(new Label(columnIndex++, row, ""));//重量
							}
							else
							{
								try{
									
									
									String xweight="0";
									boolean flag=false;
									xweight=order1.getOrder().getI_weight();
									try{
										double temp=Double.parseDouble(xweight);
										if(temp>0)
										{
											flag=true;
										}
									}catch(Exception e){}
									if(flag==false)
									{
										if(StringUtil.isEmpty(order1.getOrder().getSjweight())||order1.getOrder().getSjweight().equalsIgnoreCase("0")||order1.getOrder().getSjweight().equalsIgnoreCase("0.00"))
										{
											xweight=order1.getOrder().getWeight();
										}
										else
										{
											xweight=order1.getOrder().getSjweight();
										}
									}
									//double weight=Double.parseDouble(order1.getOrder().getWeight());
									double weight=Double.parseDouble(xweight);
									if(weight<=0)
									{
										rowresult="失败，商品重量发生异常";
									}
									
									weight=weight*0.4536;
									
									  BigDecimal   bd   =   new   BigDecimal(weight);   
									  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
									  sheet.addCell(new Label(columnIndex++, row, String.valueOf(bd)));//千克
								}catch(Exception e)
								{
									sheet.addCell(new Label(columnIndex++, row, ""));//数字发生异常
									
										rowresult="失败，商品重量发生异常";
									
								}
							}
							
							
							
							sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getValue()));//价值
							
							sheet.addCell(new Label(columnIndex++, row, order1.getCompanyName()));//发货公司
							sheet.addCell(new Label(columnIndex++, row, order1.getCompanyAddress()));//发货地址
							sheet.addCell(new Label(columnIndex++, row, ""));//收货人公司
							String address="";
							
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getState()))
							{
								address=address+order1.getOrder().getRuser().getState();
							}
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getCity()))
							{
								address=address+order1.getOrder().getRuser().getCity();
							}
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getDist()))
							{
								address=address+order1.getOrder().getRuser().getDist();
							}
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getAddress()))
							{
								address=address+order1.getOrder().getRuser().getAddress();
							}
							
							sheet.addCell(new Label(columnIndex++, row, address));//收货人地址
							
							
							//要匹配身份证
							if((!StringUtil.isEmpty(order1.getCardmodel()))&&(order1.getCardmodel().equals("0")||order1.getCardmodel().equals("1")))
							//if(false)//不匹配身分证
							{
								if(order1.getOrder().getRuser().getCardlib()!=null)//使用身份证信息
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardlib().getCname()));//收货人姓名
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
									sheet.addCell(new Label(columnIndex++, row, ""));//箱号
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardlib().getCardid()));//收货人身份证号
									//sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardlib().getCardid()));//收货人身份证号
								}
								else
								{
									
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
									sheet.addCell(new Label(columnIndex++, row, ""));//箱号
									sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
									
									/*if(!StringUtil.isEmpty(order1.getOrder().getRuser().getCardid())&&!StringUtil.isEmpty(order1.getOrder().getRuser().getCardtogether()))
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
										sheet.addCell(new Label(columnIndex++, row, ""));//箱号
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
										//sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardid()));//收货人身份证号
									}
									else
									{
										
											rowresult="失败，匹配身份证信息发生异常";
										
										//sheet.addCell(new Label(columnIndex++, row, ""));//收货人姓名
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
										sheet.addCell(new Label(columnIndex++, row, ""));//箱号
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
										//sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
									}*/
								}
							}
							else
							{
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
								sheet.addCell(new Label(columnIndex++, row, ""));//箱号
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardid()));//收货人身份证号
								//sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardid()));//收货人身份证号
							}
							
							
							
							if(order1.getOrder()==null)
							{
								sheet.addCell(new Label(columnIndex++, row, order1.getOrderResult(),redcolor));//导出结果
								sheet.addCell(new Label(columnIndex++, row, order1.getCardResult(),redcolor));//导出结果
								sheet.addCell(new Label(columnIndex++, row, order1.getGetSeaNoResult(),redcolor));//导出结果
							}
							else
							{
								
								
								if(!StringUtil.isEmpty(order1.getOrderResult())&&(order1.getOrderResult().indexOf("失败")!=-1||order1.getOrderResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getOrderResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getOrderResult()));
								}
								if(!StringUtil.isEmpty(order1.getCardResult())&&(order1.getCardResult().indexOf("失败")!=-1||order1.getCardResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getCardResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getCardResult()));
								}
								
								if(!StringUtil.isEmpty(order1.getGetSeaNoResult())&&(order1.getGetSeaNoResult().indexOf("失败")!=-1||order1.getGetSeaNoResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getGetSeaNoResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getGetSeaNoResult()));
								}
							}
							
							if(!StringUtil.isEmpty(rowresult)&&(rowresult.indexOf("失败")!=-1||rowresult.indexOf("异常")!=-1))
							{
								sheet.addCell(new Label(21, row, rowresult,redcolor));
							}
							else
							{
								sheet.addCell(new Label(21, row, rowresult));
							}
							

							row++;
						}
						
						if(!StringUtil.isEmpty(strurl))
						{
							strurl="图片保存路径："+strurl;
							sheet.addCell(new Label(0, row, strurl));
						}
						/*if((!zipurl.equalsIgnoreCase("")))
						{
							WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
				                    10, WritableFont.NO_BOLD);// 字体样式
							WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
							oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
							sheet.addCell(new Label(0, row, zipurl,oceanbluecolor));//显示压缩包路径
							
						}*/
					}
				}, templetFile, orders, os);
			}
			
			
			
		}
		
		
		
		public static void exportshanghaimode(List<ExportMorder> orders,
				File templetFile, OutputStream os) throws Exception {
			if (templetFile != null && templetFile.exists()) {
				// 模板复制
				ExcelUtil.exportExcel(new ExcelWrite<ExportMorder>() {
					@Override
					public void write(Map<String, String> headers,
							Collection<ExportMorder> datas, WritableSheet sheet)
							throws Exception {
						if (datas == null || datas.isEmpty()) {
							sheet.mergeCells(0, 1, 11, 1);
							Label cell = new Label(0, 1, "没有导入数据.....");
							sheet.addCell(cell);
							return;
						}
						//String zipurl="";
						int row = 4;
						String strurl="";
						int serno=1;
						for (ExportMorder order1 : datas) {
							if (order1 == null) {
								continue;
							}
							
							//if(!StringUtil.isEmpty(order.getZipurl())&&(zipurl.equalsIgnoreCase("")))
							//{
							//	zipurl=order.getZipurl();
							//}
							
							WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
				                    10, WritableFont.NO_BOLD);// 字体样式
							
							WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
							redcolor.setBackground(Colour.RED);
							
							WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
							oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
							if(StringUtil.isEmpty(order1.getSavezipurl()))
							{}
							else
							{
								if(StringUtil.isEmpty(strurl))
								{
									strurl=order1.getSavezipurl();
								}
							}
							
							if(order1.getOrder()==null)
							{
								sheet.addCell(new Label(0, row, String.valueOf(serno++)));//序号
								sheet.addCell(new Label(1, row, ""));//货号条码
								sheet.addCell(new Label(2, row, order1.getOrderId()));//运单号
								sheet.addCell(new Label(3, row, ""));//行邮税号
								sheet.addCell(new Label(29, row, order1.getOrderResult(),redcolor));//导出结果
								sheet.addCell(new Label(30, row, order1.getCardResult(),redcolor));//导出结果
								sheet.addCell(new Label(31, row, "失败!",redcolor));
								sheet.addCell(new Label(32, row, "失败!",redcolor));//导出结果
								row++;
								continue;
							}
							else
							{
								
								//检查状态
								try
								{
									if(Double.parseDouble(order1.getOrder().getState())<2)//没有揽收的包裹
									{
										if(StringUtil.isEmpty(order1.getOrderResult()))
										{
											order1.setOrderResult("状态异常，必须是已揽收的");
										}
										else
										{
											order1.setOrderResult(order1.getOrderResult()+",必须是已揽收的");
										}
										sheet.addCell(new Label(0, row, String.valueOf(serno++)));//序号
										//sheet.addCell(new Label(1, row, ""));//货号条码
										sheet.addCell(new Label(1, row, order1.getOrder().getSorderId()));//货号条码
										sheet.addCell(new Label(2, row, order1.getOrderId()));//运单号
										sheet.addCell(new Label(3, row, ""));//行邮税号
										sheet.addCell(new Label(29, row, order1.getOrderResult(),redcolor));//导出结果
										sheet.addCell(new Label(30, row, order1.getCardResult(),redcolor));//导出结果
										sheet.addCell(new Label(31, row, "失败!",redcolor));//导出结果
										
										row++;
										continue;
									}
								}
								catch(Exception e)
								{
									if(StringUtil.isEmpty(order1.getOrderResult()))
									{
										order1.setOrderResult("状态异常，必须是已揽收的");
									}
									else
									{
										order1.setOrderResult(order1.getOrderResult()+",必须是已揽收的");
									}
									sheet.addCell(new Label(0, row, String.valueOf(serno++)));//序号
									//sheet.addCell(new Label(1, row, ""));//货号条码
									sheet.addCell(new Label(1, row, order1.getOrder().getSorderId()));//货号条码
									sheet.addCell(new Label(2, row, order1.getOrderId()));//运单号
									sheet.addCell(new Label(3, row, ""));//行邮税号
									sheet.addCell(new Label(29, row, order1.getOrderResult(),redcolor));//导出结果
									sheet.addCell(new Label(30, row, order1.getCardResult(),redcolor));//导出结果
									sheet.addCell(new Label(31, row, "失败!",redcolor));//导出结果
									row++;
									continue;
								}
								
								
								
								if(!StringUtil.isEmpty(order1.getOrderResult())&&(order1.getOrderResult().indexOf("失败")!=-1||order1.getOrderResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(29, row, order1.getOrderResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(29, row, order1.getOrderResult()));
								}
								if(!StringUtil.isEmpty(order1.getCardResult())&&(order1.getCardResult().indexOf("失败")!=-1||order1.getCardResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(30, row, order1.getCardResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(30, row, order1.getCardResult()));
								}
								
								if(!StringUtil.isEmpty(order1.getGetSeaNoResult())&&(order1.getGetSeaNoResult().indexOf("失败")!=-1||order1.getGetSeaNoResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(31, row, order1.getGetSeaNoResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(31, row, order1.getGetSeaNoResult()));
								}
							}
							
							
							String rowresult="成功!";
							
							int columnIndex = 0;
							sheet.addCell(new Label(columnIndex++, row, String.valueOf(serno++)));//序号
							//sheet.addCell(new Label(columnIndex++, row, ""));//货号条码
							sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getSorderId()));//货号条码
							sheet.addCell(new Label(columnIndex++, row, order1.getOrderId()));//运单号
							sheet.addCell(new Label(columnIndex++, row, ""));//行邮税号
							
							
							
							
							int addindex=0;
							int startindex=columnIndex;
							for(M_OrderDetail d :order1.getOrder().getDetail())
							{
								if(d==null)
								{
									continue;
								}
								String qvalue="";
								try{
									
								
									if((!StringUtil.isEmpty(d.getQuantity()))&&(!StringUtil.isEmpty(d.getValue())))
									{
										if((Double.parseDouble(d.getQuantity())!=0)&&(Double.parseDouble(d.getValue())!=0))
										{
											double aaa=Double.parseDouble(d.getValue())/Double.parseDouble(d.getQuantity());
											if(aaa>=1)
											{
												qvalue=Integer.toString((int)aaa);
											}
											else
											{
												qvalue=Double.toString(aaa);
											}
										}
									}
									sheet.addCell(new Label(columnIndex++, row+addindex, d.getProductName()));//物品名称
									sheet.addCell(new Label(columnIndex++, row+addindex, d.getBrands()));//品牌
									sheet.addCell(new Label(columnIndex++, row+addindex, ""));//规格型号
									
									sheet.addCell(new Label(columnIndex++, row+addindex, d.getQuantity()));//数量
									sheet.addCell(new Label(columnIndex++, row+addindex, "个"));//单位
									sheet.addCell(new Label(columnIndex++, row+addindex, qvalue));//申报单价
									//sheet.addCell(new Label(columnIndex++, row+addindex, d.getValue()));//申报实价
								}
								catch(Exception e)
								{
									rowresult="失败，商品信息异常!";
									sheet.addCell(new Label(columnIndex++, row+addindex, d.getProductName()));//物品名称
									sheet.addCell(new Label(columnIndex++, row+addindex, d.getBrands()));//品牌
									sheet.addCell(new Label(columnIndex++, row+addindex, ""));//规格型号
									
									sheet.addCell(new Label(columnIndex++, row+addindex, d.getQuantity()));//数量
									sheet.addCell(new Label(columnIndex++, row+addindex, "个"));//单位
									sheet.addCell(new Label(columnIndex++, row+addindex, ""));//申报单价,发生异常
								}
								columnIndex=startindex;
								addindex++;
								
							}
							columnIndex=startindex+6;
							order1.getOrder().getQuantity();
							
							sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getValue()));//物品总价值
							sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getQuantity()));//件数
							
							try{
								
								String xweight="0";
								boolean flag=false;
								xweight=order1.getOrder().getI_weight();
								try{
									double temp=Double.parseDouble(xweight);
									if(temp>0)
									{
										flag=true;
									}
								}catch(Exception e){}
								if(flag==false)
								{
								
								
									if(StringUtil.isEmpty(order1.getOrder().getSjweight())||order1.getOrder().getSjweight().equalsIgnoreCase("0")||order1.getOrder().getSjweight().equalsIgnoreCase("0.00"))
									{
										xweight=order1.getOrder().getWeight();
									}
									else
									{
										xweight=order1.getOrder().getSjweight();
									}
								
								}
								//double weight=Double.parseDouble(order1.getOrder().getWeight());
								double weight=Double.parseDouble(xweight);
								if(weight<=0)
								{
									rowresult="失败，商品重量异常!";
								}
								weight=weight*0.4536;
								
								  BigDecimal   bd   =   new   BigDecimal(weight);   
								  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
								  sheet.addCell(new Label(columnIndex++, row, String.valueOf(bd)));//千克
							}catch(Exception e)
							{
								rowresult="失败，商品重量异常!";
								sheet.addCell(new Label(columnIndex++, row, ""));//数字发生异常
							}
							sheet.addCell(new Label(columnIndex++, row, ""));//毛重
							
							
							
							
							//要匹配身份证
							if((!StringUtil.isEmpty(order1.getCardmodel()))&&(order1.getCardmodel().equals("0")||order1.getCardmodel().equals("1")))
							{
								if(order1.getOrder().getRuser().getCardlib()!=null)//使用身份证信息
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardlib().getCname()));//收货人姓名
									//sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
									//sheet.addCell(new Label(columnIndex++, row, ""));//箱号
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardlib().getCardid()));//收货人身份证号
								}
								else
								{
									/*if(!StringUtil.isEmpty(order1.getOrder().getRuser().getCardid())&&!StringUtil.isEmpty(order1.getOrder().getRuser().getCardtogether()))
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
										//sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
										//sheet.addCell(new Label(columnIndex++, row, ""));//箱号
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardid()));//收货人身份证号
									}
									else
									{
										rowresult="失败，匹配身份证信息失败!";
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人姓名
										//sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
										//sheet.addCell(new Label(columnIndex++, row, ""));//箱号
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
									}*/
									if(order1.getOrder().getRuser()==null)
									{
										rowresult="失败，匹配身份证信息失败,没有用户信息!";
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人姓名
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
									}
								}
							}
							else
							{
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
								//sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
								//sheet.addCell(new Label(columnIndex++, row, ""));//箱号
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardid()));//收货人身份证号
							}
							String address="";
							
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getState()))
							{
								address=address+order1.getOrder().getRuser().getState();
							}
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getCity()))
							{
								address=address+order1.getOrder().getRuser().getCity();
							}
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getDist()))
							{
								address=address+order1.getOrder().getRuser().getDist();
							}
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getAddress()))
							{
								address=address+order1.getOrder().getRuser().getAddress();
							}
							
							sheet.addCell(new Label(columnIndex++, row, address));//收货人地址
							
							sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
							
							sheet.addCell(new Label(columnIndex++, row, order1.getCompanyName()));//发货人名称
							sheet.addCell(new Label(columnIndex++, row, ""));//订单号
							sheet.addCell(new Label(columnIndex++, row, ""));//备注
							
							sheet.addCell(new Label(columnIndex++, row, order1.getCompanyAddress()));//发货人地址
							String prduct="";
							for(M_OrderDetail d:order1.getOrder().getDetail())
							{
								if(prduct.equalsIgnoreCase(""))
								{
									prduct=d.getProductName();
								}
								else
								{
									prduct=prduct+";"+d.getProductName();
								}
							}
							sheet.addCell(new Label(columnIndex++, row, prduct));//商品描述
							sheet.addCell(new Label(columnIndex++, row, ""));//总运费
						/*	try{
								  sheet.addCell(new Label(columnIndex++, row, String.valueOf(100*addindex)));//总运费
							}catch(Exception e)
							{
								sheet.addCell(new Label(columnIndex++, row, ""));//总运费
							}*/
							sheet.addCell(new Label(columnIndex++, row, ""));//支付编号
							
							if(!StringUtil.isEmpty(rowresult)&&(rowresult.indexOf("失败")!=-1||rowresult.indexOf("异常")!=-1))
							{
								sheet.addCell(new Label(32, row, rowresult,redcolor));
							}
							else
							{
								sheet.addCell(new Label(32, row, rowresult));
							}
							
							

							if(addindex==0)
							{
								row++;
							}
							else
							{
								row=addindex+row;
							}
						}
						
						if(!StringUtil.isEmpty(strurl))
						{
							strurl="图片保存路径："+strurl;
							sheet.addCell(new Label(0, row, strurl));
						}
						/*if((!zipurl.equalsIgnoreCase("")))
						{
							WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
				                    10, WritableFont.NO_BOLD);// 字体样式
							WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
							oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
							sheet.addCell(new Label(0, row, zipurl,oceanbluecolor));//显示压缩包路径
							
						}*/
						
					}
				}, templetFile, orders, os);
			} else {
				// 模板复制
				ExcelUtil.exportExcel(new ExcelWrite<ExportMorder>() {
					@Override
					public void write(Map<String, String> headers,
							Collection<ExportMorder> datas, WritableSheet sheet)
							throws Exception {
						if (datas == null || datas.isEmpty()) {
							sheet.mergeCells(0, 1, 11, 1);
							Label cell = new Label(0, 1, "没有导入数据.....");
							sheet.addCell(cell);
							return;
						}
						//String zipurl="";
						int row = 4;
						String strurl="";
						int serno=1;
						for (ExportMorder order1 : datas) {
							if (order1 == null) {
								continue;
							}
							
							//if(!StringUtil.isEmpty(order.getZipurl())&&(zipurl.equalsIgnoreCase("")))
							//{
							//	zipurl=order.getZipurl();
							//}
							
							WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
				                    10, WritableFont.NO_BOLD);// 字体样式
							
							WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
							redcolor.setBackground(Colour.RED);
							
							WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
							oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
							if(StringUtil.isEmpty(order1.getSavezipurl()))
							{}
							else
							{
								if(StringUtil.isEmpty(strurl))
								{
									strurl=order1.getSavezipurl();
								}
							}
							
							if(order1.getOrder()==null)
							{
								sheet.addCell(new Label(0, row, String.valueOf(serno++)));//序号
								sheet.addCell(new Label(1, row, ""));//货号条码
								sheet.addCell(new Label(2, row, order1.getOrderId()));//运单号
								sheet.addCell(new Label(3, row, ""));//行邮税号
								sheet.addCell(new Label(29, row, order1.getOrderResult(),redcolor));//导出结果
								sheet.addCell(new Label(30, row, order1.getCardResult(),redcolor));//导出结果
								sheet.addCell(new Label(31, row, "失败!",redcolor));
								sheet.addCell(new Label(32, row, "失败!",redcolor));//导出结果
								row++;
								continue;
							}
							else
							{
								
								//检查状态
								try
								{
									if(Double.parseDouble(order1.getOrder().getState())<2)//没有揽收的包裹
									{
										if(StringUtil.isEmpty(order1.getOrderResult()))
										{
											order1.setOrderResult("状态异常，必须是已揽收的");
										}
										else
										{
											order1.setOrderResult(order1.getOrderResult()+",必须是已揽收的");
										}
										sheet.addCell(new Label(0, row, String.valueOf(serno++)));//序号
										//sheet.addCell(new Label(1, row, ""));//货号条码
										sheet.addCell(new Label(1, row, order1.getOrder().getSorderId()));//货号条码
										sheet.addCell(new Label(2, row, order1.getOrderId()));//运单号
										sheet.addCell(new Label(3, row, ""));//行邮税号
										sheet.addCell(new Label(29, row, order1.getOrderResult(),redcolor));//导出结果
										sheet.addCell(new Label(30, row, order1.getCardResult(),redcolor));//导出结果
										sheet.addCell(new Label(31, row, "失败!",redcolor));//导出结果
										
										row++;
										continue;
									}
								}
								catch(Exception e)
								{
									if(StringUtil.isEmpty(order1.getOrderResult()))
									{
										order1.setOrderResult("状态异常，必须是已揽收的");
									}
									else
									{
										order1.setOrderResult(order1.getOrderResult()+",必须是已揽收的");
									}
									sheet.addCell(new Label(0, row, String.valueOf(serno++)));//序号
									//sheet.addCell(new Label(1, row, ""));//货号条码
									sheet.addCell(new Label(1, row, order1.getOrder().getSorderId()));//货号条码
									sheet.addCell(new Label(2, row, order1.getOrderId()));//运单号
									sheet.addCell(new Label(3, row, ""));//行邮税号
									sheet.addCell(new Label(29, row, order1.getOrderResult(),redcolor));//导出结果
									sheet.addCell(new Label(30, row, order1.getCardResult(),redcolor));//导出结果
									sheet.addCell(new Label(31, row, "失败!",redcolor));//导出结果
									row++;
									continue;
								}
								
								
								
								if(!StringUtil.isEmpty(order1.getOrderResult())&&(order1.getOrderResult().indexOf("失败")!=-1||order1.getOrderResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(29, row, order1.getOrderResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(29, row, order1.getOrderResult()));
								}
								if(!StringUtil.isEmpty(order1.getCardResult())&&(order1.getCardResult().indexOf("失败")!=-1||order1.getCardResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(30, row, order1.getCardResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(30, row, order1.getCardResult()));
								}
								
								if(!StringUtil.isEmpty(order1.getGetSeaNoResult())&&(order1.getGetSeaNoResult().indexOf("失败")!=-1||order1.getGetSeaNoResult().indexOf("异常")!=-1))
								{
									sheet.addCell(new Label(31, row, order1.getGetSeaNoResult(),redcolor));
								}
								else
								{
									sheet.addCell(new Label(31, row, order1.getGetSeaNoResult()));
								}
							}
							
							
							String rowresult="成功!";
							
							int columnIndex = 0;
							sheet.addCell(new Label(columnIndex++, row, String.valueOf(serno++)));//序号
							//sheet.addCell(new Label(columnIndex++, row, ""));//货号条码
							sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getSorderId()));//货号条码
							sheet.addCell(new Label(columnIndex++, row, order1.getOrderId()));//运单号
							sheet.addCell(new Label(columnIndex++, row, ""));//行邮税号
							
							
							
							
							int addindex=0;
							int startindex=columnIndex;
							for(M_OrderDetail d :order1.getOrder().getDetail())
							{
								if(d==null)
								{
									continue;
								}
								String qvalue="";
								try{
									
								
									if((!StringUtil.isEmpty(d.getQuantity()))&&(!StringUtil.isEmpty(d.getValue())))
									{
										if((Double.parseDouble(d.getQuantity())!=0)&&(Double.parseDouble(d.getValue())!=0))
										{
											double aaa=Double.parseDouble(d.getValue())/Double.parseDouble(d.getQuantity());
											if(aaa>=1)
											{
												qvalue=Integer.toString((int)aaa);
											}
											else
											{
												qvalue=Double.toString(aaa);
											}
										}
									}
									sheet.addCell(new Label(columnIndex++, row+addindex, d.getProductName()));//物品名称
									sheet.addCell(new Label(columnIndex++, row+addindex, d.getBrands()));//品牌
									sheet.addCell(new Label(columnIndex++, row+addindex, ""));//规格型号
									
									sheet.addCell(new Label(columnIndex++, row+addindex, d.getQuantity()));//数量
									sheet.addCell(new Label(columnIndex++, row+addindex, "个"));//单位
									sheet.addCell(new Label(columnIndex++, row+addindex, qvalue));//申报单价
									//sheet.addCell(new Label(columnIndex++, row+addindex, d.getValue()));//申报实价
								}
								catch(Exception e)
								{
									rowresult="失败，商品信息异常!";
									sheet.addCell(new Label(columnIndex++, row+addindex, d.getProductName()));//物品名称
									sheet.addCell(new Label(columnIndex++, row+addindex, d.getBrands()));//品牌
									sheet.addCell(new Label(columnIndex++, row+addindex, ""));//规格型号
									
									sheet.addCell(new Label(columnIndex++, row+addindex, d.getQuantity()));//数量
									sheet.addCell(new Label(columnIndex++, row+addindex, "个"));//单位
									sheet.addCell(new Label(columnIndex++, row+addindex, ""));//申报单价,发生异常
								}
								columnIndex=startindex;
								addindex++;
								
							}
							columnIndex=startindex+6;
							order1.getOrder().getQuantity();
							
							sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getValue()));//物品总价值
							sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getQuantity()));//件数
							
							try{
								
								String xweight="0";
								boolean flag=false;
								xweight=order1.getOrder().getI_weight();
								try{
									double temp=Double.parseDouble(xweight);
									if(temp>0)
									{
										flag=true;
									}
								}catch(Exception e){}
								if(flag==false)
								{
								
								
									if(StringUtil.isEmpty(order1.getOrder().getSjweight())||order1.getOrder().getSjweight().equalsIgnoreCase("0")||order1.getOrder().getSjweight().equalsIgnoreCase("0.00"))
									{
										xweight=order1.getOrder().getWeight();
									}
									else
									{
										xweight=order1.getOrder().getSjweight();
									}
								
								}
								//double weight=Double.parseDouble(order1.getOrder().getWeight());
								double weight=Double.parseDouble(xweight);
								if(weight<=0)
								{
									rowresult="失败，商品重量异常!";
								}
								weight=weight*0.4536;
								
								  BigDecimal   bd   =   new   BigDecimal(weight);   
								  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
								  sheet.addCell(new Label(columnIndex++, row, String.valueOf(bd)));//千克
							}catch(Exception e)
							{
								rowresult="失败，商品重量异常!";
								sheet.addCell(new Label(columnIndex++, row, ""));//数字发生异常
							}
							sheet.addCell(new Label(columnIndex++, row, ""));//毛重
							
							
							
							
							//要匹配身份证
							if((!StringUtil.isEmpty(order1.getCardmodel()))&&(order1.getCardmodel().equals("0")||order1.getCardmodel().equals("1")))
							{
								if(order1.getOrder().getRuser().getCardlib()!=null)//使用身份证信息
								{
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardlib().getCname()));//收货人姓名
									//sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
									//sheet.addCell(new Label(columnIndex++, row, ""));//箱号
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardlib().getCardid()));//收货人身份证号
								}
								else
								{
									/*if(!StringUtil.isEmpty(order1.getOrder().getRuser().getCardid())&&!StringUtil.isEmpty(order1.getOrder().getRuser().getCardtogether()))
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
										//sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
										//sheet.addCell(new Label(columnIndex++, row, ""));//箱号
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardid()));//收货人身份证号
									}
									else
									{
										rowresult="失败，匹配身份证信息失败!";
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人姓名
										//sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
										//sheet.addCell(new Label(columnIndex++, row, ""));//箱号
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
									}*/
									if(order1.getOrder().getRuser()==null)
									{
										rowresult="失败，匹配身份证信息失败,没有用户信息!";
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人姓名
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
									}
								}
							}
							else
							{
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
								//sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
								//sheet.addCell(new Label(columnIndex++, row, ""));//箱号
								sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardid()));//收货人身份证号
							}
							String address="";
							
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getState()))
							{
								address=address+order1.getOrder().getRuser().getState();
							}
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getCity()))
							{
								address=address+order1.getOrder().getRuser().getCity();
							}
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getDist()))
							{
								address=address+order1.getOrder().getRuser().getDist();
							}
							if(!StringUtil.isEmpty(order1.getOrder().getRuser().getAddress()))
							{
								address=address+order1.getOrder().getRuser().getAddress();
							}
							
							sheet.addCell(new Label(columnIndex++, row, address));//收货人地址
							
							sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
							
							sheet.addCell(new Label(columnIndex++, row, order1.getCompanyName()));//发货人名称
							sheet.addCell(new Label(columnIndex++, row, ""));//订单号
							sheet.addCell(new Label(columnIndex++, row, ""));//备注
							
							sheet.addCell(new Label(columnIndex++, row, order1.getCompanyAddress()));//发货人地址
							String prduct="";
							for(M_OrderDetail d:order1.getOrder().getDetail())
							{
								if(prduct.equalsIgnoreCase(""))
								{
									prduct=d.getProductName();
								}
								else
								{
									prduct=prduct+";"+d.getProductName();
								}
							}
							sheet.addCell(new Label(columnIndex++, row, prduct));//商品描述
							sheet.addCell(new Label(columnIndex++, row, ""));//总运费
						/*	try{
								  sheet.addCell(new Label(columnIndex++, row, String.valueOf(100*addindex)));//总运费
							}catch(Exception e)
							{
								sheet.addCell(new Label(columnIndex++, row, ""));//总运费
							}*/
							sheet.addCell(new Label(columnIndex++, row, ""));//支付编号
							
							if(!StringUtil.isEmpty(rowresult)&&(rowresult.indexOf("失败")!=-1||rowresult.indexOf("异常")!=-1))
							{
								sheet.addCell(new Label(32, row, rowresult,redcolor));
							}
							else
							{
								sheet.addCell(new Label(32, row, rowresult));
							}
							
							

							if(addindex==0)
							{
								row++;
							}
							else
							{
								row=addindex+row;
							}
						}
						
						if(!StringUtil.isEmpty(strurl))
						{
							strurl="图片保存路径："+strurl;
							sheet.addCell(new Label(0, row, strurl));
						}
						/*if((!zipurl.equalsIgnoreCase("")))
						{
							WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
				                    10, WritableFont.NO_BOLD);// 字体样式
							WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
							oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
							sheet.addCell(new Label(0, row, zipurl,oceanbluecolor));//显示压缩包路径
							
						}*/
						
					}
				}, templetFile, orders, os);
			}
			
			
			
		}
		
		
		// kai 20160429 导入美淘旧模板
		public static List<M_order> readOrderFromMeitaothirdExcel(InputStream input)
				throws Exception {
			return ExcelUtil.importExcel(new ExcelReader<M_order>() {
				@Override
				public List<M_order> read(Sheet sheet) throws Exception {
					List<M_order> list = new ArrayList<M_order>();
					int rows = sheet.getRows();
					if (rows < 2) {
						throw new RuntimeException("运单文件不能为空！");
					}
					for (int i = 1; i < rows; i++) {

						int j = 0;
						
						M_order order = new M_order();
						
						
						
						order.setOrderId(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//第三方订单号
						
						if(StringUtil.isEmpty(order.getOrderId()))//如果为空,能过以后来判断不插入就可以了
						{
							//break;//如果运单号为空，即跳出。
						}
						else
						{
							order.setOrderId(order.getOrderId().toUpperCase());
						}
						String weight=StringUtil.getValue(sheet.getCell(j++, i).getContents(), "");
						order.setWeight(weight);//重量
						order.setSjweight(weight);//实际重量
						
						j++;//箱数
						List<M_OrderDetail> detail=new ArrayList<M_OrderDetail>();
						M_OrderDetail de=new M_OrderDetail();
						de.setProductName(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//发件人的商品清单
						detail.add(de);
						order.setDetail(detail);
						Send_User suser=new Send_User();
						suser.setName(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//第三方导入是的发件人用户名
						suser.setAddress(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//第三方联系地址
						suser.setCity(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//城市
						suser.setState(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//州
						suser.setZipcode(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//邮编
						order.setSuser(suser);
						
						Receive_User ruser=new Receive_User();
						ruser.setName(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//收件人姓名
						ruser.setState(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//州 
						ruser.setCity(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//城市
						ruser.setAddress(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//地址
						ruser.setPhone(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//电话
						ruser.setZipcode(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//邮编
						order.setRuser(ruser);
						String state="";
						try{
							state=StringUtil.getValue(sheet.getCell(j++, i).getContents(), "");
						}catch(Exception e){
							state="";
						}
						order.setState(state);//设置状态值
						
						
						
					
						
						
						
						list.add(order);
					}
					return list;
				}
			}, input);
		}
		
		
		
		// kai 20160429 导入美淘旧模板,并包含发件人电话的模版
		public static List<M_order> readOrderFromMeitaothird1Excel(InputStream input)
				throws Exception {
			return ExcelUtil.importExcel(new ExcelReader<M_order>() {
				@Override
				public List<M_order> read(Sheet sheet) throws Exception {
					List<M_order> list = new ArrayList<M_order>();
					int rows = sheet.getRows();
					if (rows < 2) {
						throw new RuntimeException("运单文件不能为空！");
					}
					for (int i = 1; i < rows; i++) {

						int j = 0;
						
						M_order order = new M_order();
						
						
						
						order.setOrderId(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//第三方订单号
						
						if(StringUtil.isEmpty(order.getOrderId()))//如果为空,能过以后来判断不插入就可以了
						{
							//break;//如果运单号为空，即跳出。
						}
						else
						{
							order.setOrderId(order.getOrderId().toUpperCase());
						}
						
						Send_User suser=new Send_User();
						
						suser.setName(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//第三方导入是的发件人用户名
						suser.setPhone(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//第三方导入是的发件人电话
						suser.setAddress(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//第三方联系地址
						//suser.setCity(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//城市
						//suser.setState(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//州
						//suser.setZipcode(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//邮编
						order.setSuser(suser);
						
						
						String weight=StringUtil.getValue(sheet.getCell(j++, i).getContents(), "0");
						order.setWeight(weight);//重量
						order.setSjweight(weight);//实际重量
						
						//j++;//箱数
						List<M_OrderDetail> detail=new ArrayList<M_OrderDetail>();
						M_OrderDetail de=new M_OrderDetail();
						de.setProductName(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//发件人的商品清单
						detail.add(de);
						order.setDetail(detail);
						
						//总金额
						order.setTotalmoney(StringUtil.getValue(sheet.getCell(j++, i).getContents(), "0"));
						
						
						Receive_User ruser=new Receive_User();
						ruser.setName(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//收件人姓名
						ruser.setPhone(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//电话
						ruser.setState(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//州 
						ruser.setCity(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//城市
						ruser.setAddress(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//地址
						
						ruser.setZipcode(StringUtil.getValue(sheet.getCell(j++, i).getContents(), ""));//邮编
						order.setRuser(ruser);
						String state="";
						try{
							state=StringUtil.getValue(sheet.getCell(j++, i).getContents(), "");
						}catch(Exception e){
							state="";
						}
						order.setState(state);//设置状态值
						
						
						
					
						
						
						
						list.add(order);
					}
					return list;
				}
			}, input);
		}
		
		
		
		
		// 20160519 读取第表格头内容
				public static List<String> readexcelfirstrow(InputStream input)
						throws Exception {
					return ExcelUtil.importExcel(new ExcelReader<String>() {
						@Override
						public List<String> read(Sheet sheet) throws Exception {
							//List<String> list = new ArrayList<String>();
						
							int colomns=sheet.getColumns();
							if (colomns < 1) {
								throw new RuntimeException("运单文件不能为空！");
							}
							List<String> header = new ArrayList<String>();
							for(int j=0;j<colomns;j++)
							{
								header.add(StringUtil.getValue(sheet.getCell(j, 0).getContents(), ""));//获取第一行的表表头内容
								
							}
							
							
							
							return header;
						}
					}, input);
				}
				
				
		
		
		//导入美淘第三方模版
		public static void exportthirdMordersMeitao(List<ImportthirdMorder> orders, File templetFile, OutputStream os) throws Exception {
			if (templetFile != null && templetFile.exists()) {
				// 模板复制
				ExcelUtil.exportExcel(new ExcelWrite<ImportthirdMorder>() {
					@Override
					public void write(Map<String, String> headers, Collection<ImportthirdMorder> datas, WritableSheet sheet)
					        throws Exception {
						if (datas == null || datas.isEmpty()) {
							sheet.mergeCells(0, 1, 11, 1);
							Label cell = new Label(0, 1, "没有用户数据.....");
							sheet.addCell(cell);
							return;
						}

						int row = 1;
						
						for (ImportthirdMorder iorder : datas)
						{
							WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
				                    10, WritableFont.NO_BOLD);// 字体样式
							
							WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
							redcolor.setBackground(Colour.RED);
							
							WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
							oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
					
							for (M_order order : iorder.getOrders()) {
								if(order==null)
								{
									continue;
								}
								int columnIndex = 0;
								
								sheet.addCell(new Label(columnIndex++, row,order.getOrderId()));//运单号
								
								sheet.addCell(new Label(columnIndex++, row,order.getWeight()));//重量
								sheet.addCell(new Label(columnIndex++, row,"1"));//箱数
								String productstr="";
								for(M_OrderDetail de:order.getDetail())
								{
									if(productstr.equalsIgnoreCase(""))
									{
										productstr=de.getProductName();
									}
									else
									{
										productstr=productstr+";"+de.getProductName();
									}
								}
								sheet.addCell(new Label(columnIndex++, row,productstr));// 发件人的商品清单
								sheet.addCell(new Label(columnIndex++, row,order.getSuser().getName()));// 第三方导入是的发件人用户名
								sheet.addCell(new Label(columnIndex++, row,order.getSuser().getAddress()));//地址
								sheet.addCell(new Label(columnIndex++, row,order.getSuser().getCity()));//城市
								sheet.addCell(new Label(columnIndex++, row,order.getSuser().getState()));//州
								sheet.addCell(new Label(columnIndex++, row,order.getSuser().getZipcode()));//邮编
								
								
								sheet.addCell(new Label(columnIndex++, row,order.getRuser().getName()));// 收件人
								sheet.addCell(new Label(columnIndex++, row,order.getRuser().getState()));//省份
								sheet.addCell(new Label(columnIndex++, row,order.getRuser().getCity()));//城市
								sheet.addCell(new Label(columnIndex++, row,order.getRuser().getAddress()));//地址
								sheet.addCell(new Label(columnIndex++, row,order.getRuser().getPhone()));//电话
								sheet.addCell(new Label(columnIndex++, row,order.getRuser().getZipcode()));//邮编
								sheet.addCell(new Label(columnIndex++, row,order.getState()));//状态
								
								if(!StringUtil.isEmpty(order.getResult())&&order.getResult().indexOf("失败")!=-1)
								{
									sheet.addCell(new Label(columnIndex++, row, order.getResult(),redcolor));//导出结果
								}
								else
								{
									sheet.addCell(new Label(columnIndex++, row, order.getResult()));//导出结果
								}
								
								row++;
							}
						}
					}
				}, templetFile, orders, os);
			} else {
				// 重新写
				ExcelUtil.exportExcel(new ExcelWrite<ImportthirdMorder>() {
					@Override
					public void write(Map<String, String> headers, Collection<ImportthirdMorder> datas, WritableSheet sheet)
					        throws Exception {
						if (datas == null || datas.isEmpty()) {
							sheet.mergeCells(0, 1, 11, 1);
							Label cell = new Label(0, 1, "没有用户数据.....");
							sheet.addCell(cell);
							return;
						}

						int row = 1;
						
						for (ImportthirdMorder iorder : datas)
						{
							WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
				                    10, WritableFont.NO_BOLD);// 字体样式
							
							WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
							redcolor.setBackground(Colour.RED);
							
							WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
							oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
					
							for (M_order order : iorder.getOrders()) {
								if(order==null)
								{
									continue;
								}
								int columnIndex = 0;
								
								sheet.addCell(new Label(columnIndex++, row,order.getOrderId()));//运单号
								
								sheet.addCell(new Label(columnIndex++, row,order.getWeight()));//重量
								sheet.addCell(new Label(columnIndex++, row,"1"));//箱数
								String productstr="";
								for(M_OrderDetail de:order.getDetail())
								{
									if(productstr.equalsIgnoreCase(""))
									{
										productstr=de.getProductName();
									}
									else
									{
										productstr=productstr+";"+de.getProductName();
									}
								}
								sheet.addCell(new Label(columnIndex++, row,productstr));// 发件人的商品清单
								sheet.addCell(new Label(columnIndex++, row,order.getSuser().getName()));// 第三方导入是的发件人用户名
								sheet.addCell(new Label(columnIndex++, row,order.getSuser().getAddress()));//地址
								sheet.addCell(new Label(columnIndex++, row,order.getSuser().getCity()));//城市
								sheet.addCell(new Label(columnIndex++, row,order.getSuser().getState()));//州
								sheet.addCell(new Label(columnIndex++, row,order.getSuser().getZipcode()));//邮编
								
								
								sheet.addCell(new Label(columnIndex++, row,order.getRuser().getName()));// 收件人
								sheet.addCell(new Label(columnIndex++, row,order.getRuser().getState()));//省份
								sheet.addCell(new Label(columnIndex++, row,order.getRuser().getCity()));//城市
								sheet.addCell(new Label(columnIndex++, row,order.getRuser().getAddress()));//地址
								sheet.addCell(new Label(columnIndex++, row,order.getRuser().getPhone()));//电话
								sheet.addCell(new Label(columnIndex++, row,order.getRuser().getZipcode()));//邮编
								sheet.addCell(new Label(columnIndex++, row,order.getState()));//状态
								
								if(order.getResult().indexOf("失败")!=-1)
								{
									sheet.addCell(new Label(columnIndex++, row, order.getResult(),redcolor));//导出结果
								}
								else
								{
									sheet.addCell(new Label(columnIndex++, row, order.getResult()));//导出结果
								}
								
								row++;
							}
						}
					}
				}, "order-datas", null, orders, os);
			}
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//导出美淘第三方模版，包含发发件人电话的模板
				public static void exportthirdMordersMeitao1(List<ImportthirdMorder> orders, File templetFile, OutputStream os) throws Exception {
					if (templetFile != null && templetFile.exists()) {
						// 模板复制
						ExcelUtil.exportExcel(new ExcelWrite<ImportthirdMorder>() {
							@Override
							public void write(Map<String, String> headers, Collection<ImportthirdMorder> datas, WritableSheet sheet)
							        throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有用户数据.....");
									sheet.addCell(cell);
									return;
								}

								int row = 1;
								
								for (ImportthirdMorder iorder : datas)
								{
									WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
						                    10, WritableFont.NO_BOLD);// 字体样式
									
									WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
									redcolor.setBackground(Colour.RED);
									
									WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
									oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
							
									for (M_order order : iorder.getOrders()) {
										if(order==null)
										{
											continue;
										}
										int columnIndex = 0;
										
										sheet.addCell(new Label(columnIndex++, row,order.getOrderId()));//运单号
										
										if(order.getSuser()!=null)
										{
											sheet.addCell(new Label(columnIndex++, row,order.getSuser().getName()));//发件人姓名
											sheet.addCell(new Label(columnIndex++, row,order.getSuser().getPhone()));//发件人电话
											sheet.addCell(new Label(columnIndex++, row,order.getSuser().getAddress()));//发件人地址
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row,""));//发件人姓名
											sheet.addCell(new Label(columnIndex++, row,""));//发件人电话
											sheet.addCell(new Label(columnIndex++, row,""));//发件人地址
										}
										
										sheet.addCell(new Label(columnIndex++, row,order.getWeight()));//重量
										
										String productstr="";
										for(M_OrderDetail de:order.getDetail())
										{
											if(productstr.equalsIgnoreCase(""))
											{
												productstr=de.getProductName();
											}
											else
											{
												productstr=productstr+";"+de.getProductName();
											}
										}
										sheet.addCell(new Label(columnIndex++, row,productstr));// 发件人的商品清单
										
										sheet.addCell(new Label(columnIndex++, row,order.getTotalmoney()));// 发件人的总金额
										
										if(order.getRuser()!=null)
										{
											sheet.addCell(new Label(columnIndex++, row,order.getRuser().getName()));//收件人姓名
											sheet.addCell(new Label(columnIndex++, row,order.getRuser().getPhone()));//收件人电话
											sheet.addCell(new Label(columnIndex++, row,order.getRuser().getState()));//收件人所在省
											sheet.addCell(new Label(columnIndex++, row,order.getRuser().getCity()));//收件人所在市
											sheet.addCell(new Label(columnIndex++, row,order.getRuser().getAddress()));//收件人所在地址
											sheet.addCell(new Label(columnIndex++, row,order.getRuser().getZipcode()));//收件人所在的邮编
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row,""));//收件人姓名
											sheet.addCell(new Label(columnIndex++, row,""));//收件人电话
											sheet.addCell(new Label(columnIndex++, row,""));//收件人所在省
											sheet.addCell(new Label(columnIndex++, row,""));//收件人所在市
											sheet.addCell(new Label(columnIndex++, row,""));//收件人所在地址
											sheet.addCell(new Label(columnIndex++, row,""));//收件人所在的邮编
										}
										
										
										
										sheet.addCell(new Label(columnIndex++, row,order.getState()));//状态
										
										if(!StringUtil.isEmpty(order.getResult())&&order.getResult().indexOf("失败")!=-1)
										{
											sheet.addCell(new Label(columnIndex++, row, order.getResult(),redcolor));//导出结果
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row, order.getResult()));//导出结果
										}
										
										row++;
									}
								}
							}
						}, templetFile, orders, os);
					} else {
						// 重新写
						ExcelUtil.exportExcel(new ExcelWrite<ImportthirdMorder>() {
							@Override
							public void write(Map<String, String> headers, Collection<ImportthirdMorder> datas, WritableSheet sheet)
							        throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有用户数据.....");
									sheet.addCell(cell);
									return;
								}

								int row = 1;
								
								for (ImportthirdMorder iorder : datas)
								{
									WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
						                    10, WritableFont.NO_BOLD);// 字体样式
									
									WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
									redcolor.setBackground(Colour.RED);
									
									WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
									oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
							
									for (M_order order : iorder.getOrders()) {
										if(order==null)
										{
											continue;
										}
										int columnIndex = 0;
										
										sheet.addCell(new Label(columnIndex++, row,order.getOrderId()));//运单号
										
										if(order.getSuser()!=null)
										{
											sheet.addCell(new Label(columnIndex++, row,order.getSuser().getName()));//发件人姓名
											sheet.addCell(new Label(columnIndex++, row,order.getSuser().getPhone()));//发件人电话
											sheet.addCell(new Label(columnIndex++, row,order.getSuser().getAddress()));//发件人地址
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row,""));//发件人姓名
											sheet.addCell(new Label(columnIndex++, row,""));//发件人电话
											sheet.addCell(new Label(columnIndex++, row,""));//发件人地址
										}
										
										sheet.addCell(new Label(columnIndex++, row,order.getWeight()));//重量
										
										String productstr="";
										for(M_OrderDetail de:order.getDetail())
										{
											if(productstr.equalsIgnoreCase(""))
											{
												productstr=de.getProductName();
											}
											else
											{
												productstr=productstr+";"+de.getProductName();
											}
										}
										sheet.addCell(new Label(columnIndex++, row,productstr));// 发件人的商品清单
										
										sheet.addCell(new Label(columnIndex++, row,order.getTotalmoney()));// 发件人的总金额
										
										if(order.getRuser()!=null)
										{
											sheet.addCell(new Label(columnIndex++, row,order.getRuser().getName()));//收件人姓名
											sheet.addCell(new Label(columnIndex++, row,order.getRuser().getPhone()));//收件人电话
											sheet.addCell(new Label(columnIndex++, row,order.getRuser().getState()));//收件人所在省
											sheet.addCell(new Label(columnIndex++, row,order.getRuser().getCity()));//收件人所在市
											sheet.addCell(new Label(columnIndex++, row,order.getRuser().getAddress()));//收件人所在地址
											sheet.addCell(new Label(columnIndex++, row,order.getRuser().getZipcode()));//收件人所在的邮编
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row,""));//收件人姓名
											sheet.addCell(new Label(columnIndex++, row,""));//收件人电话
											sheet.addCell(new Label(columnIndex++, row,""));//收件人所在省
											sheet.addCell(new Label(columnIndex++, row,""));//收件人所在市
											sheet.addCell(new Label(columnIndex++, row,""));//收件人所在地址
											sheet.addCell(new Label(columnIndex++, row,""));//收件人所在的邮编
										}
										
										
										
										sheet.addCell(new Label(columnIndex++, row,order.getState()));//状态
										
										if(!StringUtil.isEmpty(order.getResult())&&order.getResult().indexOf("失败")!=-1)
										{
											sheet.addCell(new Label(columnIndex++, row, order.getResult(),redcolor));//导出结果
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row, order.getResult()));//导出结果
										}
										
										row++;
									}
								}
							}
						}, "order-datas", null, orders, os);
					}
				}
				
				
				
				// 导出汇通数据 20160514
				public static void exporthuitongorders(List<ExportMorder> orders,
						File templetFile, OutputStream os) throws Exception {
					if (templetFile != null && templetFile.exists()) {
						// 模板复制
						ExcelUtil.exportExcel(new ExcelWrite<ExportMorder>() {
							@Override
							public void write(Map<String, String> headers,
									Collection<ExportMorder> datas, WritableSheet sheet)
									throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有导入数据.....");
									sheet.addCell(cell);
									return;
								}
								
								int row = 1;

								
								for (ExportMorder order1 : datas) {
									if (order1 == null) {
										continue;
									}
									
									String rowresult="成功!";
									
									WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
						                    10, WritableFont.NO_BOLD);// 字体样式
									
									WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
									redcolor.setBackground(Colour.RED);
									
									WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
									oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
									
									if(order1.getOrder()==null)
									{
										
										sheet.addCell(new Label(0, row, order1.getOrderId()));//运单号
									
										sheet.addCell(new Label(15, row, order1.getOrderResult(),redcolor));//导出结果
										
										row++;
										continue;
									}
									else
									{

										//String wrongStr="";//错误标志，只要是检查汇通单号是否存在
										int columnIndex = 0;
										
										sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getOrderId()));//运单号
										if(StringUtil.isEmpty(order1.getOrder().getHuitongNo()))//汇通单号为空
										{
											sheet.addCell(new Label(columnIndex++, row,""));//汇通单号
											rowresult="失败!没包含汇通单号";
										}
										else{
											sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getHuitongNo()));//汇通单号
										}
										if(order1.getOrder().getSuser()!=null)
										{
											sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getSuser().getName()));//发件人姓名
											sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getSuser().getPhone()));//发件人电话
											sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getSuser().getAddress()));//发件人地址
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row,""));//发件人姓名
											sheet.addCell(new Label(columnIndex++, row,""));//发件人电话
											sheet.addCell(new Label(columnIndex++, row,""));//发件人地址
										}
										
										sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getWeight()));//重量
										
										String productstr="";
										for(M_OrderDetail de:order1.getOrder().getDetail())
										{
											if(productstr.equalsIgnoreCase(""))
											{
												productstr=de.getProductName();
											}
											else
											{
												productstr=productstr+";"+de.getProductName();
											}
										}
										sheet.addCell(new Label(columnIndex++, row,productstr));// 发件人的商品清单
										
										sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getTotalmoney()));// 发件人的总金额
										
										if(order1.getOrder().getRuser()!=null)
										{
											sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getRuser().getName()));//收件人姓名
											sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getRuser().getPhone()));//收件人电话
											sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getRuser().getState()));//收件人所在省
											sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getRuser().getCity()));//收件人所在市
											sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getRuser().getAddress()));//收件人所在地址
											sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getRuser().getZipcode()));//收件人所在的邮编
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row,""));//收件人姓名
											sheet.addCell(new Label(columnIndex++, row,""));//收件人电话
											sheet.addCell(new Label(columnIndex++, row,""));//收件人所在省
											sheet.addCell(new Label(columnIndex++, row,""));//收件人所在市
											sheet.addCell(new Label(columnIndex++, row,""));//收件人所在地址
											sheet.addCell(new Label(columnIndex++, row,""));//收件人所在的邮编
										}
										
										
										
										sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getState()));//状态
										
										if(!StringUtil.isEmpty(rowresult)&&rowresult.indexOf("失败")!=-1)
										{
											sheet.addCell(new Label(columnIndex++, row, rowresult,redcolor));//导出结果
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row, rowresult));//导出结果
										}
										
										//row++;
									
										
									}
									
									
									
									row++;

									
								}
							
								
							}
						}, templetFile, orders, os);
					} else {
						// 模板复制
						ExcelUtil.exportExcel(new ExcelWrite<ExportMorder>() {
							@Override
							public void write(Map<String, String> headers,
									Collection<ExportMorder> datas, WritableSheet sheet)
									throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有导入数据.....");
									sheet.addCell(cell);
									return;
								}
								
								int row = 1;

								
								for (ExportMorder order1 : datas) {
									if (order1 == null) {
										continue;
									}
									
									String rowresult="成功!";
									
									WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
						                    10, WritableFont.NO_BOLD);// 字体样式
									
									WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
									redcolor.setBackground(Colour.RED);
									
									WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
									oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
									
									if(order1.getOrder()==null)
									{
										
										sheet.addCell(new Label(0, row, order1.getOrderId()));//运单号
									
										sheet.addCell(new Label(14, row, order1.getOrderResult(),redcolor));//导出结果
										
										row++;
										continue;
									}
									else
									{

										String wrongStr="";//错误标志，只要是检查汇通单号是否存在
										int columnIndex = 0;
										
										sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getOrderId()));//运单号
										if(StringUtil.isEmpty(order1.getOrder().getHuitongNo()))//汇通单号为空
										{
											sheet.addCell(new Label(columnIndex++, row,""));//汇通单号
											wrongStr="失败!没包含汇通单号";
										}
										else{
											sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getHuitongNo()));//汇通单号
										}
										if(order1.getOrder().getSuser()!=null)
										{
											sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getSuser().getName()));//发件人姓名
											sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getSuser().getPhone()));//发件人电话
											sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getSuser().getAddress()));//发件人地址
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row,""));//发件人姓名
											sheet.addCell(new Label(columnIndex++, row,""));//发件人电话
											sheet.addCell(new Label(columnIndex++, row,""));//发件人地址
										}
										
										sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getWeight()));//重量
										
										String productstr="";
										for(M_OrderDetail de:order1.getOrder().getDetail())
										{
											if(productstr.equalsIgnoreCase(""))
											{
												productstr=de.getProductName();
											}
											else
											{
												productstr=productstr+";"+de.getProductName();
											}
										}
										sheet.addCell(new Label(columnIndex++, row,productstr));// 发件人的商品清单
										
										sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getTotalmoney()));// 发件人的总金额
										
										if(order1.getOrder().getRuser()!=null)
										{
											sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getRuser().getName()));//收件人姓名
											sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getRuser().getPhone()));//收件人电话
											sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getRuser().getState()));//收件人所在省
											sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getRuser().getCity()));//收件人所在市
											sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getRuser().getAddress()));//收件人所在地址
											sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getRuser().getZipcode()));//收件人所在的邮编
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row,""));//收件人姓名
											sheet.addCell(new Label(columnIndex++, row,""));//收件人电话
											sheet.addCell(new Label(columnIndex++, row,""));//收件人所在省
											sheet.addCell(new Label(columnIndex++, row,""));//收件人所在市
											sheet.addCell(new Label(columnIndex++, row,""));//收件人所在地址
											sheet.addCell(new Label(columnIndex++, row,""));//收件人所在的邮编
										}
										
										
										
										sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getState()));//状态
										
										if(!StringUtil.isEmpty(wrongStr)&&wrongStr.indexOf("失败")!=-1)
										{
											sheet.addCell(new Label(columnIndex++, row, wrongStr,redcolor));//导出结果
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row, wrongStr));//导出结果
										}
										
										//row++;
									
										
									}
									
									
									
									row++;

									
								}
							
								
							}
						}, templetFile, orders, os);
					}
					
					
					
				}
				
				
				
				// 根据运单号下载运单信息
				public static void exportMOrderToMeitao(List<ExportMorder> orders,
						File templetFile, OutputStream os) throws Exception {
					if (templetFile != null && templetFile.exists()) {
						// 模板复制
						ExcelUtil.exportExcel(new ExcelWrite<ExportMorder>() {
							@Override
							public void write(Map<String, String> headers,
									Collection<ExportMorder> datas, WritableSheet sheet)
									throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有用户数据.....");
									sheet.addCell(cell);
									return;
								}
								
								int row = 1;
								WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
					                    10, WritableFont.NO_BOLD);// 字体样式
								WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
								redcolor.setBackground(Colour.RED);

								for (ExportMorder eorder : datas) {
									if (eorder.getOrder() == null) {
										sheet.addCell(new Label(0, row, eorder
												.getOrderId()));// 运单号
										sheet.addCell(new Label(21, row, eorder.getOrderResult(),redcolor));// 导出结果
										row++;
										continue;
									}
									
									int columnIndex = 0;
									// sheet.addCell(new Label(columnIndex++, row,
									// order.getId()));// 是用记录编号
									sheet.addCell(new Label(columnIndex++, row, eorder.getOrder()
											.getOrderId()));// 运单号
									if(StringUtil.isEmpty( eorder.getOrder().getSjweight()))
									{
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getWeight()));//重量
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getSjweight()));//实际重量
									}
									//sheet.addCell(new Label(columnIndex++, row, order.getJwweight()));//实际重量
									sheet.addCell(new Label(columnIndex++, row, "1"));//箱数
									// 开始到处商品信息
									List<M_OrderDetail> details =  eorder.getOrder().getDetail();
									////StringBuffer sb = new StringBuffer();
									//StringBuffer brands = new StringBuffer();
									//StringBuffer xq = new StringBuffer();
									//int total = 0;
									String commudity="";
									String brands="";
									if ((details != null)&&(details.size()>0))
									{
										//String str="";
										for(M_OrderDetail d:details)
										{
											if(commudity.equalsIgnoreCase(""))
											{
												commudity=d.getProductName()+"*"+d.getQuantity();
											}
											else
											{
												commudity=commudity+";"+d.getProductName()+"*"+d.getQuantity();
											}
											if(brands.equalsIgnoreCase(""))
											{
												brands=d.getBrands();
											}
											else
											{
												brands=brands+";"+d.getBrands();
											}
											
										}
									}
									
									sheet.addCell(new Label(columnIndex++, row, commudity
											.toString()));// 物品品名
									
								
									
									if(brands!=null)
									{
									sheet.addCell(new Label(columnIndex++, row, brands
											.toString()));// 物品品牌
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));// 物品品牌
									}
									  //发件人为代理客户，则发件人信息取自订单
									if(( eorder.getOrder().getSuser()!=null))
									{
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getSuser().getName())); // 发件人
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getSuser().getPhone())); // 发件人联系电话
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getSuser().getAddress())); // 发件人地址
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getSuser().getCity())); // 发件人城市	
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getSuser().getState())); // 发件人州名	
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getSuser().getZipcode())); // 发件人邮编	
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
									
									
									if(( eorder.getOrder().getRuser()!=null))
									{
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getName()));// 收件人姓名
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getState()));// 收件人所在省份
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getCity()));// 收件人所在市
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getDist()));// 收件人所在区
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getAddress()));// 收件人详细地址
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getPhone()));// 收件人电话
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getZipcode()));// 收件人邮编
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人姓名
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人所在省份
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人所在市
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人所在区
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人详细地址
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人电话
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人邮编
									}
							
									
								
								
									
									//sheet.addCell(new Label(columnIndex++, row, String
									//		.valueOf(CommodityCost)));// 费用成本
									sheet.addCell(new Label(columnIndex++, row, ""));// 成本费用
									sheet.addCell(new Label(columnIndex++, row, String
											.valueOf( eorder.getOrder().getTotalmoney())));// 费用合计

									sheet.addCell(new Label(columnIndex++, row, String
											.valueOf( eorder.getOrder().getState())));// 状态
									
									sheet.addCell(new Label(columnIndex++, row, eorder.getOrder().getPayDate()));// 支付时间
									sheet.addCell(new Label(columnIndex++, row, eorder.getOrder().getThirdPNS()));// 第三方公司
									sheet.addCell(new Label(columnIndex++, row, eorder.getOrder().getThirdNo()));// 第三方单号
									
									sheet.addCell(new Label(columnIndex++, row, eorder.getOrderResult()));// 导出结果
									
									row++;
								}
							}
						}, templetFile, orders, os);
					} else {
						// 重新写
						ExcelUtil.exportExcel(new ExcelWrite<ExportMorder>() {
							@Override
							public void write(Map<String, String> headers,
									Collection<ExportMorder> datas, WritableSheet sheet)
									throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有用户数据.....");
									sheet.addCell(cell);
									return;
								}
								
								int row = 1;
								WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
					                    10, WritableFont.NO_BOLD);// 字体样式
								WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
								redcolor.setBackground(Colour.RED);

								for (ExportMorder eorder : datas) {
									if (eorder.getOrder() == null) {
										sheet.addCell(new Label(0, row, eorder
												.getOrderId()));// 运单号
										sheet.addCell(new Label(21, row, eorder.getOrderResult(),redcolor));// 导出结果
										row++;
										continue;
									}
									
									int columnIndex = 0;
									// sheet.addCell(new Label(columnIndex++, row,
									// order.getId()));// 是用记录编号
									sheet.addCell(new Label(columnIndex++, row, eorder.getOrder()
											.getOrderId()));// 运单号
									if(StringUtil.isEmpty( eorder.getOrder().getSjweight()))
									{
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getWeight()));//重量
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getSjweight()));//实际重量
									}
									//sheet.addCell(new Label(columnIndex++, row, order.getJwweight()));//实际重量
									sheet.addCell(new Label(columnIndex++, row, "1"));//箱数
									// 开始到处商品信息
									List<M_OrderDetail> details =  eorder.getOrder().getDetail();
									////StringBuffer sb = new StringBuffer();
									//StringBuffer brands = new StringBuffer();
									//StringBuffer xq = new StringBuffer();
									//int total = 0;
									String commudity="";
									String brands="";
									if ((details != null)&&(details.size()>0))
									{
										//String str="";
										for(M_OrderDetail d:details)
										{
											if(commudity.equalsIgnoreCase(""))
											{
												commudity=d.getProductName()+"*"+d.getQuantity();
											}
											else
											{
												commudity=commudity+";"+d.getProductName()+"*"+d.getQuantity();
											}
											if(brands.equalsIgnoreCase(""))
											{
												brands=d.getBrands();
											}
											else
											{
												brands=brands+";"+d.getBrands();
											}
											
										}
									}
									
									sheet.addCell(new Label(columnIndex++, row, commudity
											.toString()));// 物品品名
									
								
									
									if(brands!=null)
									{
									sheet.addCell(new Label(columnIndex++, row, brands
											.toString()));// 物品品牌
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));// 物品品牌
									}
									  //发件人为代理客户，则发件人信息取自订单
									if(( eorder.getOrder().getSuser()!=null))
									{
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getSuser().getName())); // 发件人
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getSuser().getPhone())); // 发件人联系电话
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getSuser().getAddress())); // 发件人地址
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getSuser().getCity())); // 发件人城市	
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getSuser().getState())); // 发件人州名	
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getSuser().getZipcode())); // 发件人邮编	
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
									
									
									if(( eorder.getOrder().getRuser()!=null))
									{
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getName()));// 收件人姓名
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getState()));// 收件人所在省份
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getCity()));// 收件人所在市
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getDist()));// 收件人所在区
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getAddress()));// 收件人详细地址
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getPhone()));// 收件人电话
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getZipcode()));// 收件人邮编
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人姓名
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人所在省份
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人所在市
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人所在区
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人详细地址
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人电话
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人邮编
									}
							
									
								
								
									
									//sheet.addCell(new Label(columnIndex++, row, String
									//		.valueOf(CommodityCost)));// 费用成本
									sheet.addCell(new Label(columnIndex++, row, ""));// 成本费用
									sheet.addCell(new Label(columnIndex++, row, String
											.valueOf( eorder.getOrder().getTotalmoney())));// 费用合计

									sheet.addCell(new Label(columnIndex++, row, String
											.valueOf( eorder.getOrder().getState())));// 状态
									
									sheet.addCell(new Label(columnIndex++, row, eorder.getOrder().getPayDate()));// 支付时间
									sheet.addCell(new Label(columnIndex++, row, eorder.getOrder().getThirdPNS()));// 第三方公司
									sheet.addCell(new Label(columnIndex++, row, eorder.getOrder().getThirdNo()));// 第三方单号
									sheet.addCell(new Label(columnIndex++, row, eorder.getOrderResult()));// 导出结果
									row++;
								}
							}
						}, "order-datas", null, orders, os);
					}
				}
				
				
				
				
				
				
				// 导出入库运单
				public static void exportInputMOrderToMeitao(List<M_order> orders,
						File templetFile, OutputStream os) throws Exception {
					if (templetFile != null && templetFile.exists()) {
						// 模板复制
						ExcelUtil.exportExcel(new ExcelWrite<M_order>() {
							@Override
							public void write(Map<String, String> headers,
									Collection<M_order> datas, WritableSheet sheet)
									throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有用户数据.....");
									sheet.addCell(cell);
									return;
								}
								
								int row = 1;
								WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
					                    10, WritableFont.NO_BOLD);// 字体样式
								WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
								redcolor.setBackground(Colour.RED);

								for (M_order order : datas) {
									if (order == null) {
										
										continue;
									}
									
									
									int columnIndex = 0;
									// sheet.addCell(new Label(columnIndex++, row,
									// order.getId()));// 是用记录编号
									sheet.addCell(new Label(columnIndex++, row, order.getOrderId()));// 运单号
									sheet.addCell(new Label(columnIndex++, row, order.getI_storeName()));// 入库门店
									sheet.addCell(new Label(columnIndex++, row, order.getStoreName()));// 所属门店
									//sheet.addCell(new Label(columnIndex++, row, order.getState()));// 状态值
									sheet.addCell(new Label(columnIndex++, row, order.getTotalmoney()));// 总价值
									sheet.addCell(new Label(columnIndex++, row, order.getTotalcost()));// 总成本
									sheet.addCell(new Label(columnIndex++, row, order.getWeight()));// 计费重量
									sheet.addCell(new Label(columnIndex++, row, order.getSjweight()));// 实际重量
									sheet.addCell(new Label(columnIndex++, row, order.getI_weight()));// 入库重量
									sheet.addCell(new Label(columnIndex++, row, order.getI_jwweight()));// 入库进位重量
									sheet.addCell(new Label(columnIndex++, row, order.getI_date()));// 入库时间
									
									List<M_OrderDetail> details =  order.getDetail();
									////StringBuffer sb = new StringBuffer();
									//StringBuffer brands = new StringBuffer();
									//StringBuffer xq = new StringBuffer();
									//int total = 0;
									String commudity="";
									String brands="";
									if ((details != null)&&(details.size()>0))
									{
										//String str="";
										for(M_OrderDetail d:details)
										{
											if(commudity.equalsIgnoreCase(""))
											{
												commudity=d.getProductName()+"*"+d.getQuantity();
											}
											else
											{
												commudity=commudity+";"+d.getProductName()+"*"+d.getQuantity();
											}
											if(brands.equalsIgnoreCase(""))
											{
												brands=d.getBrands();
											}
											else
											{
												brands=brands+";"+d.getBrands();
											}
											
										}
									}
									
									sheet.addCell(new Label(columnIndex++, row, commudity
											.toString()));// 物品品名
									
								
									
									if(brands!=null)
									{
									sheet.addCell(new Label(columnIndex++, row, brands
											.toString()));// 物品品牌
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));// 物品品牌
									}
									  //发件人为代理客户，则发件人信息取自订单
									if(( order.getSuser()!=null))
									{
										sheet.addCell(new Label(columnIndex++, row,  order.getSuser().getName())); // 发件人
										sheet.addCell(new Label(columnIndex++, row,  order.getSuser().getPhone())); // 发件人联系电话
										sheet.addCell(new Label(columnIndex++, row,  order.getSuser().getAddress())); // 发件人地址
										sheet.addCell(new Label(columnIndex++, row,  order.getSuser().getCity())); // 发件人城市	
										sheet.addCell(new Label(columnIndex++, row,  order.getSuser().getState())); // 发件人州名	
										sheet.addCell(new Label(columnIndex++, row,  order.getSuser().getZipcode())); // 发件人邮编	
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
									
									
									if(( order.getRuser()!=null))
									{
										sheet.addCell(new Label(columnIndex++, row,  order.getRuser().getName()));// 收件人姓名
										sheet.addCell(new Label(columnIndex++, row,  order.getRuser().getState()));// 收件人所在省份
										sheet.addCell(new Label(columnIndex++, row,  order.getRuser().getCity()));// 收件人所在市
										sheet.addCell(new Label(columnIndex++, row,  order.getRuser().getDist()));// 收件人所在区
										sheet.addCell(new Label(columnIndex++, row,  order.getRuser().getAddress()));// 收件人详细地址
										sheet.addCell(new Label(columnIndex++, row,  order.getRuser().getPhone()));// 收件人电话
										sheet.addCell(new Label(columnIndex++, row,  order.getRuser().getZipcode()));// 收件人邮编
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人姓名
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人所在省份
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人所在市
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人所在区
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人详细地址
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人电话
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人邮编
									}
							
									
								
									
									row++;
								}
							}
						}, templetFile, orders, os);
					} else {
						// 重新写
						ExcelUtil.exportExcel(new ExcelWrite<M_order>() {
							@Override
							public void write(Map<String, String> headers,
									Collection<M_order> datas, WritableSheet sheet)
									throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有用户数据.....");
									sheet.addCell(cell);
									return;
								}
								
								int row = 1;
								WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
					                    10, WritableFont.NO_BOLD);// 字体样式
								WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
								redcolor.setBackground(Colour.RED);

								for (M_order order : datas) {
									if (order == null) {
										
										continue;
									}
									
									
									int columnIndex = 0;
									// sheet.addCell(new Label(columnIndex++, row,
									// order.getId()));// 是用记录编号
									sheet.addCell(new Label(columnIndex++, row, order.getOrderId()));// 运单号
									sheet.addCell(new Label(columnIndex++, row, order.getI_storeName()));// 入库门店
									sheet.addCell(new Label(columnIndex++, row, order.getStoreName()));// 所属门店
									//sheet.addCell(new Label(columnIndex++, row, order.getState()));// 状态值
									sheet.addCell(new Label(columnIndex++, row, order.getTotalmoney()));// 总价值
									sheet.addCell(new Label(columnIndex++, row, order.getTotalcost()));// 总成本
									sheet.addCell(new Label(columnIndex++, row, order.getWeight()));// 计费重量
									sheet.addCell(new Label(columnIndex++, row, order.getSjweight()));// 实际重量
									sheet.addCell(new Label(columnIndex++, row, order.getI_weight()));// 入库重量
									sheet.addCell(new Label(columnIndex++, row, order.getI_jwweight()));// 入库进位重量
									sheet.addCell(new Label(columnIndex++, row, order.getI_date()));// 入库时间
									
									List<M_OrderDetail> details =  order.getDetail();
									////StringBuffer sb = new StringBuffer();
									//StringBuffer brands = new StringBuffer();
									//StringBuffer xq = new StringBuffer();
									//int total = 0;
									String commudity="";
									String brands="";
									if ((details != null)&&(details.size()>0))
									{
										//String str="";
										for(M_OrderDetail d:details)
										{
											if(commudity.equalsIgnoreCase(""))
											{
												commudity=d.getProductName()+"*"+d.getQuantity();
											}
											else
											{
												commudity=commudity+";"+d.getProductName()+"*"+d.getQuantity();
											}
											if(brands.equalsIgnoreCase(""))
											{
												brands=d.getBrands();
											}
											else
											{
												brands=brands+";"+d.getBrands();
											}
											
										}
									}
									
									sheet.addCell(new Label(columnIndex++, row, commudity
											.toString()));// 物品品名
									
								
									
									if(brands!=null)
									{
									sheet.addCell(new Label(columnIndex++, row, brands
											.toString()));// 物品品牌
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));// 物品品牌
									}
									  //发件人为代理客户，则发件人信息取自订单
									if(( order.getSuser()!=null))
									{
										sheet.addCell(new Label(columnIndex++, row,  order.getSuser().getName())); // 发件人
										sheet.addCell(new Label(columnIndex++, row,  order.getSuser().getPhone())); // 发件人联系电话
										sheet.addCell(new Label(columnIndex++, row,  order.getSuser().getAddress())); // 发件人地址
										sheet.addCell(new Label(columnIndex++, row,  order.getSuser().getCity())); // 发件人城市	
										sheet.addCell(new Label(columnIndex++, row,  order.getSuser().getState())); // 发件人州名	
										sheet.addCell(new Label(columnIndex++, row,  order.getSuser().getZipcode())); // 发件人邮编	
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
									
									
									if(( order.getRuser()!=null))
									{
										sheet.addCell(new Label(columnIndex++, row,  order.getRuser().getName()));// 收件人姓名
										sheet.addCell(new Label(columnIndex++, row,  order.getRuser().getState()));// 收件人所在省份
										sheet.addCell(new Label(columnIndex++, row,  order.getRuser().getCity()));// 收件人所在市
										sheet.addCell(new Label(columnIndex++, row,  order.getRuser().getDist()));// 收件人所在区
										sheet.addCell(new Label(columnIndex++, row,  order.getRuser().getAddress()));// 收件人详细地址
										sheet.addCell(new Label(columnIndex++, row,  order.getRuser().getPhone()));// 收件人电话
										sheet.addCell(new Label(columnIndex++, row,  order.getRuser().getZipcode()));// 收件人邮编
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人姓名
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人所在省份
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人所在市
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人所在区
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人详细地址
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人电话
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人邮编
									}
							
									
								
									
									row++;
								}
							}
						}, "order-datas", null, orders, os);
					}
				}
				
				
			
				
				
				// 美淘A渠道奶粉模板下载
				public static void exportMOrderToAnf_Meitao(List<ExportMorder> orders,
						File templetFile, OutputStream os) throws Exception {
					if (templetFile != null && templetFile.exists()) {
						// 模板复制
						ExcelUtil.exportExcel(new ExcelWrite<ExportMorder>() {
							@Override
							public void write(Map<String, String> headers,
									Collection<ExportMorder> datas, WritableSheet sheet)
									throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有用户数据.....");
									sheet.addCell(cell);
									return;
								}
								
								int row = 1;
								WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
					                    10, WritableFont.NO_BOLD);// 字体样式
								WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
								redcolor.setBackground(Colour.RED);
								
								for (ExportMorder eorder : datas) {
									if (eorder.getOrder() == null) {
										
										
										sheet.addCell(new Label(0, row, String.valueOf(row)));// 序号
										sheet.addCell(new Label(0, row, ""));// 客户账号
										sheet.addCell(new Label(0, row, ""));// 标签
										sheet.addCell(new Label(0, row, ""));// 所属部门
										
										sheet.addCell(new Label(0, row, eorder
												.getOrderId()));// 内单号（客户自己单号）
										sheet.addCell(new Label(34, row, eorder.getOrderResult(),redcolor));// 导出结果
										row++;
										continue;
									}
									String rowresult="";
									int columnIndex = 0;
									// sheet.addCell(new Label(columnIndex++, row,
									// order.getId()));// 是用记录编号
									sheet.addCell(new Label(0, row, String.valueOf(row)));// 序号
									sheet.addCell(new Label(0, row, ""));// 客户账号
									sheet.addCell(new Label(0, row, ""));// 标签
									sheet.addCell(new Label(0, row, ""));// 所属部门
									
									
									sheet.addCell(new Label(columnIndex++, row, eorder.getOrder()
											.getOrderId()));// 内单号（客户自己单号）
									
									sheet.addCell(new Label(columnIndex++, row, eorder.getOrder().getThirdNo()));// 转单号(国内物流单号)
									
									sheet.addCell(new Label(columnIndex++, row, ""));//参考号
									sheet.addCell(new Label(columnIndex++, row, "1"));//件数（一个包裹就是件数1）
									
									
									
									try{
										String weight="";
										weight=eorder.getOrder().getSjweight();
										if(!StringUtil.isEmpty(weight))
										{
											if(Double.parseDouble(weight)<=0)
											{
												weight="";
											}
										}
										
										
										if(StringUtil.isEmpty(weight))
										{
											weight=eorder.getOrder().getWeight();
										}
										
									double weight1=Double.parseDouble(weight);
									if(weight1<=0)
									{
										rowresult="重量信息异常";
									}
									weight1=weight1*0.4536;
									
									
									  BigDecimal   bd   =   new   BigDecimal(weight1);   
									  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
									  sheet.addCell(new Label(columnIndex++, row, String.valueOf(bd)));//千克
									
									}catch(Exception e)
									{
										rowresult="重量异常";
										sheet.addCell(new Label(columnIndex++, row, ""));//重量（kg）
									}
									
									
									
									// 开始到处商品信息
									List<M_OrderDetail> details =  eorder.getOrder().getDetail();
									////StringBuffer sb = new StringBuffer();
									//StringBuffer brands = new StringBuffer();
									//StringBuffer xq = new StringBuffer();
									//int total = 0;
									String commudity="";
								    
									if ((details != null)&&(details.size()>0))
									{
										//String str="";
										for(M_OrderDetail d:details)
										{
											if(commudity.equalsIgnoreCase(""))
											{
												commudity=d.getBrands()+d.getProductName()+d.getQuantity();
											}
											else
											{
												commudity=commudity+"、 "+d.getBrands()+d.getProductName()+d.getQuantity();
											}
										}
									}
									
									sheet.addCell(new Label(columnIndex++, row, commudity
											.toString()));//物品描述（英文品牌+中文品名+ 总克数）
									
									sheet.addCell(new Label(columnIndex++, row, ""));//备用四
									sheet.addCell(new Label(columnIndex++, row, ""));//备用三
									sheet.addCell(new Label(columnIndex++, row, eorder.getOrder().getQuantity()));//物品数量
									//物品单价（人民币）
									if(eorder.getRate()>0)//包含汇率
									{
										try{
											int q=Integer.parseInt(eorder.getOrder().getQuantity());
											double value=Double.parseDouble(eorder.getOrder().getValue());
											if(value>0&&q>0)
											{
												double qvalue=(value*eorder.getRate())/q;
												BigDecimal   bd   =   new   BigDecimal(qvalue);   
												bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
												sheet.addCell(new Label(columnIndex++, row, String.valueOf(bd)));//单价
												
												
												
											}
											else
											{
												sheet.addCell(new Label(columnIndex++, row, ""));//单价
											}
										}catch(Exception e){
											sheet.addCell(new Label(columnIndex++, row, ""));//单价
										}
										
										try{
											
											double value=Double.parseDouble(eorder.getOrder().getValue());
											if(value>0)
											{
												double allvalue=(value*eorder.getRate());
												BigDecimal   bd   =   new   BigDecimal(allvalue);   
												bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
												sheet.addCell(new Label(columnIndex++, row, String.valueOf(allvalue)));//人民币价值											
											}
											else
											{
												sheet.addCell(new Label(columnIndex++, row, ""));//人民币价值
											}
										}catch(Exception e){
											sheet.addCell(new Label(columnIndex++, row, ""));//人民币价值
										}
										
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));//单价
										sheet.addCell(new Label(columnIndex++, row,eorder.getOrder().getValue() ));//总价值
									}

									//收件人姓名
									if(( eorder.getOrder().getRuser()!=null))
									{
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getName()));// 收件人姓名
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getState()));// 收件人所在省份
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getCity()));// 收件人所在市
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getDist()));// 收件人所在区
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getAddress()));// 收件人详细地址
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getPhone()));// 收件人电话
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getZipcode()));// 收件人邮编
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人姓名
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人所在省份
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人所在市
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人所在区
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人详细地址
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人电话
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人邮编
									}
							
									
									  //发件人为代理客户，则发件人信息取自订单
									if(( eorder.getOrder().getSuser()!=null))
									{
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getSuser().getName())); // 发件人
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getSuser().getPhone())); // 发件人联系电话
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getSuser().getAddress())); // 发件人地址
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getSuser().getCity())); // 发件人城市	
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getSuser().getState())); // 发件人州名	
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getSuser().getZipcode())); // 发件人邮编	
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
								
									
									//sheet.addCell(new Label(columnIndex++, row, String
									//		.valueOf(CommodityCost)));// 费用成本
									sheet.addCell(new Label(columnIndex++, row, ""));// 成本费用
									sheet.addCell(new Label(columnIndex++, row, String
											.valueOf( eorder.getOrder().getTotalmoney())));// 费用合计

									sheet.addCell(new Label(columnIndex++, row, String
											.valueOf( eorder.getOrder().getState())));// 状态
									
									sheet.addCell(new Label(columnIndex++, row, eorder.getOrder().getPayDate()));// 支付时间
									sheet.addCell(new Label(columnIndex++, row, eorder.getOrder().getThirdPNS()));// 第三方公司
									sheet.addCell(new Label(columnIndex++, row, eorder.getOrder().getThirdNo()));// 第三方单号
									
									sheet.addCell(new Label(columnIndex++, row, eorder.getOrderResult()));// 导出结果
									
									row++;
								}
							}
						}, templetFile, orders, os);
					} else {
						// 重新写
						ExcelUtil.exportExcel(new ExcelWrite<ExportMorder>() {
							@Override
							public void write(Map<String, String> headers,
									Collection<ExportMorder> datas, WritableSheet sheet)
									throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有用户数据.....");
									sheet.addCell(cell);
									return;
								}
								
								int row = 1;
								WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
					                    10, WritableFont.NO_BOLD);// 字体样式
								WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
								redcolor.setBackground(Colour.RED);

								for (ExportMorder eorder : datas) {
									if (eorder.getOrder() == null) {
										sheet.addCell(new Label(0, row, eorder
												.getOrderId()));// 运单号
										sheet.addCell(new Label(21, row, eorder.getOrderResult(),redcolor));// 导出结果
										row++;
										continue;
									}
									
									int columnIndex = 0;
									// sheet.addCell(new Label(columnIndex++, row,
									// order.getId()));// 是用记录编号
									sheet.addCell(new Label(columnIndex++, row, eorder.getOrder()
											.getOrderId()));// 运单号
									if(StringUtil.isEmpty( eorder.getOrder().getSjweight()))
									{
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getWeight()));//重量
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getSjweight()));//实际重量
									}
									//sheet.addCell(new Label(columnIndex++, row, order.getJwweight()));//实际重量
									sheet.addCell(new Label(columnIndex++, row, "1"));//箱数
									// 开始到处商品信息
									List<M_OrderDetail> details =  eorder.getOrder().getDetail();
									////StringBuffer sb = new StringBuffer();
									//StringBuffer brands = new StringBuffer();
									//StringBuffer xq = new StringBuffer();
									//int total = 0;
									String commudity="";
									String brands="";
									if ((details != null)&&(details.size()>0))
									{
										//String str="";
										for(M_OrderDetail d:details)
										{
											if(commudity.equalsIgnoreCase(""))
											{
												commudity=d.getProductName()+"*"+d.getQuantity();
											}
											else
											{
												commudity=commudity+";"+d.getProductName()+"*"+d.getQuantity();
											}
											if(brands.equalsIgnoreCase(""))
											{
												brands=d.getBrands();
											}
											else
											{
												brands=brands+";"+d.getBrands();
											}
											
										}
									}
									
									sheet.addCell(new Label(columnIndex++, row, commudity
											.toString()));// 物品品名
									
								
									
									if(brands!=null)
									{
									sheet.addCell(new Label(columnIndex++, row, brands
											.toString()));// 物品品牌
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));// 物品品牌
									}
									  //发件人为代理客户，则发件人信息取自订单
									if(( eorder.getOrder().getSuser()!=null))
									{
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getSuser().getName())); // 发件人
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getSuser().getPhone())); // 发件人联系电话
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getSuser().getAddress())); // 发件人地址
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getSuser().getCity())); // 发件人城市	
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getSuser().getState())); // 发件人州名	
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getSuser().getZipcode())); // 发件人邮编	
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
									
									
									if(( eorder.getOrder().getRuser()!=null))
									{
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getName()));// 收件人姓名
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getState()));// 收件人所在省份
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getCity()));// 收件人所在市
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getDist()));// 收件人所在区
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getAddress()));// 收件人详细地址
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getPhone()));// 收件人电话
										sheet.addCell(new Label(columnIndex++, row,  eorder.getOrder().getRuser().getZipcode()));// 收件人邮编
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人姓名
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人所在省份
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人所在市
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人所在区
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人详细地址
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人电话
										sheet.addCell(new Label(columnIndex++, row, ""));// 收件人邮编
									}
							
									
								
								
									
									//sheet.addCell(new Label(columnIndex++, row, String
									//		.valueOf(CommodityCost)));// 费用成本
									sheet.addCell(new Label(columnIndex++, row, ""));// 成本费用
									sheet.addCell(new Label(columnIndex++, row, String
											.valueOf( eorder.getOrder().getTotalmoney())));// 费用合计

									sheet.addCell(new Label(columnIndex++, row, String
											.valueOf( eorder.getOrder().getState())));// 状态
									
									sheet.addCell(new Label(columnIndex++, row, eorder.getOrder().getPayDate()));// 支付时间
									sheet.addCell(new Label(columnIndex++, row, eorder.getOrder().getThirdPNS()));// 第三方公司
									sheet.addCell(new Label(columnIndex++, row, eorder.getOrder().getThirdNo()));// 第三方单号
									sheet.addCell(new Label(columnIndex++, row, eorder.getOrderResult()));// 导出结果
									row++;
								}
							}
						}, "order-datas", null, orders, os);
					}
				}
				
			
				
				
				
				// A奶粉
				public static void exportA_NF(List<ExportMorder> orders,
						File templetFile, OutputStream os) throws Exception {
					if (templetFile != null && templetFile.exists()) {
						// 模板复制
						ExcelUtil.exportExcel(new ExcelWrite<ExportMorder>() {
							@Override
							public void write(Map<String, String> headers,
									Collection<ExportMorder> datas, WritableSheet sheet)
									throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有导入数据.....");
									sheet.addCell(cell);
									return;
								}
								
								int row = 1;

								//String zipurl="";
								String strurl="";
								for (ExportMorder order1 : datas) {
									if (order1 == null) {
										continue;
									}
								//	if(!StringUtil.isEmpty(order.getZipurl())&&(zipurl.equalsIgnoreCase("")))
									//{
									//	zipurl=order.getZipurl();
									//}
									WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
						                    10, WritableFont.NO_BOLD);// 字体样式
									
									WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
									redcolor.setBackground(Colour.RED);
									
									WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
									oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
									int columnIndex = 0;
									
									sheet.addCell(new Label(columnIndex++, row, String.valueOf(row)));//序号
									sheet.addCell(new Label(columnIndex++, row, ""));// 客户账号
									sheet.addCell(new Label(columnIndex++, row, ""));// 标签
									sheet.addCell(new Label(columnIndex++, row, ""));// 所属部门
									sheet.addCell(new Label(columnIndex++, row, order1
											.getOrderId()));// 内单号（客户自己单号）
									
									if(StringUtil.isEmpty(order1.getSavezipurl()))
									{}
									else
									{
										if(StringUtil.isEmpty(strurl))
										{
											strurl=order1.getSavezipurl();
										}
									}
									String rowresult="成功!";
									if(order1.getOrder()==null)
									{
										//sheet.addCell(new Label(columnIndex++, row, ""));//分单号
										if(!StringUtil.isEmpty(order1.getOrderResult())&&(order1.getOrderResult().indexOf("失败")!=-1||order1.getOrderResult().indexOf("异常")!=-1))
										{
											sheet.addCell(new Label(33, row, order1.getOrderResult(),redcolor));
										}
										else
										{
											sheet.addCell(new Label(33, row, order1.getOrderResult()));
										}
										if(!StringUtil.isEmpty(order1.getCardResult())&&(order1.getCardResult().indexOf("失败")!=-1||order1.getCardResult().indexOf("异常")!=-1))
										{
											sheet.addCell(new Label(34, row, order1.getCardResult(),redcolor));
										}
										else
										{
											sheet.addCell(new Label(34, row, order1.getCardResult()));
										}
										sheet.addCell(new Label(35, row, "失败!",redcolor));
										sheet.addCell(new Label(36, row, "海关单号失败",redcolor));//导出失败
										row++;
										continue;
									}
									else
									{
										//sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getSorderId()));//分单号
										try
										{
											if(Double.parseDouble(order1.getOrder().getState())<2)//没有揽收的包裹
											{
												if(StringUtil.isEmpty(order1.getOrderResult()))
												{
													order1.setOrderResult("状态异常，必须是已揽收的");
												}
												else
												{
													order1.setOrderResult(order1.getOrderResult()+",必须是已揽收的");
												}
												
												sheet.addCell(new Label(33, row, order1.getOrderResult(),redcolor));//导出结果
												sheet.addCell(new Label(34, row, order1.getCardResult(),redcolor));//导出结果
												sheet.addCell(new Label(35, row, "失败",redcolor));//导出失败
												sheet.addCell(new Label(36, row, order1.getGetSeaNoResult(),redcolor));//导出失败
												row++;
												continue;
											}
										}
										catch(Exception e)
										{
											if(StringUtil.isEmpty(order1.getOrderResult()))
											{
												order1.setOrderResult("状态异常，必须是已揽收的");
											}
											else
											{
												order1.setOrderResult(order1.getOrderResult()+",必须是已揽收的");
											}
											sheet.addCell(new Label(33, row, order1.getOrderResult(),redcolor));//导出结果
											sheet.addCell(new Label(34, row, order1.getCardResult(),redcolor));//导出结果
											sheet.addCell(new Label(35, row, "失败",redcolor));//导出失败
											sheet.addCell(new Label(36, row, order1.getGetSeaNoResult(),redcolor));//导出失败
											row++;
											continue;
										}
									}
									
									//转单号(国内物流单号)
                                    sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getThirdNo()));// 转单号(国内物流单号)
									
									sheet.addCell(new Label(columnIndex++, row, ""));//参考号
									sheet.addCell(new Label(columnIndex++, row, "1"));//件数（一个包裹就是件数1）
									
									
									
									try{
										String weight="";
										weight=order1.getOrder().getSjweight();
										if(!StringUtil.isEmpty(weight))
										{
											if(Double.parseDouble(weight)<=0)
											{
												weight="";
											}
										}
										
										
										if(StringUtil.isEmpty(weight))
										{
											weight=order1.getOrder().getWeight();
										}
										
									double weight1=Double.parseDouble(weight);
									if(weight1<=0)
									{
										rowresult="重量信息异常";
									}
									weight1=weight1*0.4536;
									
									
									  BigDecimal   bd   =   new   BigDecimal(weight1);   
									  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数  
									  
									  DecimalFormat format = new DecimalFormat("#.##");
									  String sMoney = format.format(bd);
									  
									  sheet.addCell(new Label(columnIndex++, row, String.valueOf(sMoney)));//千克
									
									}catch(Exception e)
									{
										rowresult="重量异常";
										sheet.addCell(new Label(columnIndex++, row, ""));//重量（kg）
									}
									
									
									
									
									// 开始到处商品信息
									List<M_OrderDetail> details =  order1.getOrder().getDetail();
								
									String commudity="";
								    
									if ((details != null)&&(details.size()>0))
									{
										//String str="";
										for(M_OrderDetail d:details)
										{
											if(commudity.equalsIgnoreCase(""))
											{
												if(StringUtil.isEmpty(d.getBrands()))//这部分表示导入的是第三方数据，不能加入数量
												{
													commudity=d.getProductName();
												}
												else
												{
													commudity=d.getBrands()+d.getProductName()+d.getQuantity();
												}
											}
											else
											{
												if(StringUtil.isEmpty(d.getBrands()))//这部分表示导入的是第三方数据，不能加入数量
												{
													commudity=commudity+"、 "+d.getProductName();
												}
												else
												{
													commudity=commudity+"、 "+d.getBrands()+d.getProductName()+d.getQuantity();
												}
											}
										}
									}
									
									sheet.addCell(new Label(columnIndex++, row, commudity
											.toString()));//物品描述（英文品牌+中文品名+ 总克数）
									
									sheet.addCell(new Label(columnIndex++, row, ""));//备用四
									sheet.addCell(new Label(columnIndex++, row, ""));//备用三
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getQuantity()));//物品数量
									
									
									
									if(order1.getRate()>0)//包含汇率
									{
										try{
											int q=Integer.parseInt(order1.getOrder().getQuantity());
											double value=Double.parseDouble(order1.getOrder().getValue());
											if(value>0&&q>0)
											{
												double qvalue=(value*order1.getRate())/q;
												BigDecimal   bd   =   new   BigDecimal(qvalue);   
												bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数  
												
												
												 DecimalFormat format = new DecimalFormat("#.##");
												  String sMoney = format.format(bd);
												
												sheet.addCell(new Label(columnIndex++, row, String.valueOf(sMoney)));//单价
												
												
												
											}
											else
											{
												sheet.addCell(new Label(columnIndex++, row, ""));//单价
											}
										}catch(Exception e){
											sheet.addCell(new Label(columnIndex++, row, ""));//单价
										}
										
										try{
											
											double value=Double.parseDouble(order1.getOrder().getValue());
											if(value>0)
											{
												double allvalue=(value*order1.getRate());
												BigDecimal   bd   =   new   BigDecimal(allvalue);   
												bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数  
												
												
												 DecimalFormat format = new DecimalFormat("#.##");
												  String sMoney = format.format(bd);
												
												sheet.addCell(new Label(columnIndex++, row, String.valueOf(sMoney)));//人民币价值											
											}
											else
											{
												sheet.addCell(new Label(columnIndex++, row, ""));//人民币价值
											}
										}catch(Exception e){
											sheet.addCell(new Label(columnIndex++, row, ""));//人民币价值
										}
										
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));//物品单价（人民币）
										sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getValue() ));//声明价值（人民币）
									}
									
									
									//要匹配身份证
									if((order1.getCardmodel().equals("0")||order1.getCardmodel().equals("1")))
									{
										if(order1.getOrder().getRuser().getCardlib()!=null)//使用身份证信息
										{
											sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardlib().getCname()));//收货人姓名
											sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
										
											sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardlib().getCardid()));//收货人身份证号
										}
										else
										{
										
											rowresult="失败，匹配身份证失败!";
											sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
											sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
											
											sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
											
											
										}
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardid()));//收货人身份证号
									}
									
									
									if(order1.getOrder().getRuser()!=null)
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getZipcode()));//收件人邮编
										String address="";
										
										if(!StringUtil.isEmpty(order1.getOrder().getRuser().getState()))
										{
											address=address+order1.getOrder().getRuser().getState();
										}
										if(!StringUtil.isEmpty(order1.getOrder().getRuser().getCity()))
										{
											address=address+order1.getOrder().getRuser().getCity();
										}
										if(!StringUtil.isEmpty(order1.getOrder().getRuser().getDist()))
										{
											address=address+order1.getOrder().getRuser().getDist();
										}
										if(!StringUtil.isEmpty(order1.getOrder().getRuser().getAddress()))
										{
											address=address+order1.getOrder().getRuser().getAddress();
										}
										sheet.addCell(new Label(columnIndex++, row, address));//收货人地址
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCity()));//收货人城市
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getState()));//收货人省
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));//邮篇
										sheet.addCell(new Label(columnIndex++, row, ""));//收件人地址
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人城市
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人省
									}
									
									sheet.addCell(new Label(columnIndex++, row, ""));//批次
									
									if(order1.getOrder().getSuser()!=null)
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getSuser().getName()));//发件人
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getSuser().getPhone()));//发件人电话
										
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));//发件人
										sheet.addCell(new Label(columnIndex++, row, ""));//发件人电话
									}
									//发件人所在门店地址
									if(order1.getStore()!=null)
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getStore().getZipCode()));//发邮编
										sheet.addCell(new Label(columnIndex++, row, order1.getStore().getAddress()));//发件地址
										sheet.addCell(new Label(columnIndex++, row, order1.getStore().getCountry()));//发件国家
										sheet.addCell(new Label(columnIndex++, row, order1.getStore().getProvince()));//发件省
										sheet.addCell(new Label(columnIndex++, row, order1.getStore().getCity()));//发件城市
										
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));//
										sheet.addCell(new Label(columnIndex++, row, ""));//
										sheet.addCell(new Label(columnIndex++, row, ""));//
										sheet.addCell(new Label(columnIndex++, row, ""));//
										sheet.addCell(new Label(columnIndex++, row, ""));//
									
									}
									sheet.addCell(new Label(columnIndex++, row, ""));//备用1
									sheet.addCell(new Label(columnIndex++, row, ""));//备用2
									sheet.addCell(new Label(columnIndex++, row, ""));//备用3
									
									
									
									
									if(order1.getOrder()==null)
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getOrderResult(),redcolor));//导出结果
										sheet.addCell(new Label(columnIndex++, row, order1.getCardResult(),redcolor));//导出结果
									}
									else
									{
										
										
										if(!StringUtil.isEmpty(order1.getOrderResult())&&(order1.getOrderResult().indexOf("失败")!=-1||order1.getOrderResult().indexOf("异常")!=-1))
										{
											sheet.addCell(new Label(columnIndex++, row, order1.getOrderResult(),redcolor));
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row, order1.getOrderResult()));
										}
										if(!StringUtil.isEmpty(order1.getCardResult())&&(order1.getCardResult().indexOf("失败")!=-1||order1.getCardResult().indexOf("异常")!=-1))
										{
											sheet.addCell(new Label(columnIndex++, row, order1.getCardResult(),redcolor));
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row, order1.getCardResult()));
										}
									}
									
									if(!StringUtil.isEmpty(rowresult)&&(rowresult.indexOf("失败")!=-1||rowresult.indexOf("异常")!=-1))
									{
										sheet.addCell(new Label(35, row,rowresult,redcolor));
									}
									else
									{
										sheet.addCell(new Label(35, row, rowresult));
									}
									
									if(!StringUtil.isEmpty(order1.getGetSeaNoResult())&&(order1.getGetSeaNoResult().indexOf("失败")!=-1||order1.getGetSeaNoResult().indexOf("异常")!=-1))
									{
										sheet.addCell(new Label(36, row,order1.getGetSeaNoResult(),redcolor));
									}
									else
									{
										sheet.addCell(new Label(36, row, order1.getGetSeaNoResult()));
									}

									row++;
								}
								if(!StringUtil.isEmpty(strurl))
								{
									strurl="图片保存路径："+strurl;
									sheet.addCell(new Label(0, row, strurl));
								}
								/*if((!zipurl.equalsIgnoreCase("")))
								{
									WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
						                    10, WritableFont.NO_BOLD);// 字体样式
									WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
									oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
									sheet.addCell(new Label(0, row, zipurl,oceanbluecolor));//显示压缩包路径
									
								}*/
							}
						}, templetFile, orders, os);
					} else {
						// 模板复制
						ExcelUtil.exportExcel(new ExcelWrite<ExportMorder>() {
							@Override
							public void write(Map<String, String> headers,
									Collection<ExportMorder> datas, WritableSheet sheet)
									throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有导入数据.....");
									sheet.addCell(cell);
									return;
								}
								
								int row = 1;

								//String zipurl="";
								String strurl="";
								for (ExportMorder order1 : datas) {
									if (order1 == null) {
										continue;
									}
								//	if(!StringUtil.isEmpty(order.getZipurl())&&(zipurl.equalsIgnoreCase("")))
									//{
									//	zipurl=order.getZipurl();
									//}
									WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
						                    10, WritableFont.NO_BOLD);// 字体样式
									
									WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
									redcolor.setBackground(Colour.RED);
									
									WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
									oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
									int columnIndex = 0;
									
									sheet.addCell(new Label(columnIndex++, row, String.valueOf(row)));//序号
									sheet.addCell(new Label(columnIndex++, row, ""));// 客户账号
									sheet.addCell(new Label(columnIndex++, row, ""));// 标签
									sheet.addCell(new Label(columnIndex++, row, ""));// 所属部门
									sheet.addCell(new Label(columnIndex++, row, order1
											.getOrderId()));// 内单号（客户自己单号）
									
									if(StringUtil.isEmpty(order1.getSavezipurl()))
									{}
									else
									{
										if(StringUtil.isEmpty(strurl))
										{
											strurl=order1.getSavezipurl();
										}
									}
									String rowresult="成功!";
									if(order1.getOrder()==null)
									{
										//sheet.addCell(new Label(columnIndex++, row, ""));//分单号
										if(!StringUtil.isEmpty(order1.getOrderResult())&&(order1.getOrderResult().indexOf("失败")!=-1||order1.getOrderResult().indexOf("异常")!=-1))
										{
											sheet.addCell(new Label(33, row, order1.getOrderResult(),redcolor));
										}
										else
										{
											sheet.addCell(new Label(33, row, order1.getOrderResult()));
										}
										if(!StringUtil.isEmpty(order1.getCardResult())&&(order1.getCardResult().indexOf("失败")!=-1||order1.getCardResult().indexOf("异常")!=-1))
										{
											sheet.addCell(new Label(34, row, order1.getCardResult(),redcolor));
										}
										else
										{
											sheet.addCell(new Label(34, row, order1.getCardResult()));
										}
										sheet.addCell(new Label(35, row, "失败!",redcolor));
										sheet.addCell(new Label(36, row, "海关单号失败",redcolor));//导出失败
										row++;
										continue;
									}
									else
									{
										//sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getSorderId()));//分单号
										try
										{
											if(Double.parseDouble(order1.getOrder().getState())<2)//没有揽收的包裹
											{
												if(StringUtil.isEmpty(order1.getOrderResult()))
												{
													order1.setOrderResult("状态异常，必须是已揽收的");
												}
												else
												{
													order1.setOrderResult(order1.getOrderResult()+",必须是已揽收的");
												}
												
												sheet.addCell(new Label(33, row, order1.getOrderResult(),redcolor));//导出结果
												sheet.addCell(new Label(34, row, order1.getCardResult(),redcolor));//导出结果
												sheet.addCell(new Label(35, row, "失败",redcolor));//导出失败
												sheet.addCell(new Label(36, row, order1.getGetSeaNoResult(),redcolor));//导出失败
												row++;
												continue;
											}
										}
										catch(Exception e)
										{
											if(StringUtil.isEmpty(order1.getOrderResult()))
											{
												order1.setOrderResult("状态异常，必须是已揽收的");
											}
											else
											{
												order1.setOrderResult(order1.getOrderResult()+",必须是已揽收的");
											}
											sheet.addCell(new Label(33, row, order1.getOrderResult(),redcolor));//导出结果
											sheet.addCell(new Label(34, row, order1.getCardResult(),redcolor));//导出结果
											sheet.addCell(new Label(35, row, "失败",redcolor));//导出失败
											sheet.addCell(new Label(36, row, order1.getGetSeaNoResult(),redcolor));//导出失败
											row++;
											continue;
										}
									}
									
									//转单号(国内物流单号)
                                    sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getThirdNo()));// 转单号(国内物流单号)
									
									sheet.addCell(new Label(columnIndex++, row, ""));//参考号
									sheet.addCell(new Label(columnIndex++, row, "1"));//件数（一个包裹就是件数1）
									
									
									
									try{
										String weight="";
										weight=order1.getOrder().getSjweight();
										if(!StringUtil.isEmpty(weight))
										{
											if(Double.parseDouble(weight)<=0)
											{
												weight="";
											}
										}
										
										
										if(StringUtil.isEmpty(weight))
										{
											weight=order1.getOrder().getWeight();
										}
										
									double weight1=Double.parseDouble(weight);
									if(weight1<=0)
									{
										rowresult="重量信息异常";
									}
									weight1=weight1*0.4536;
									
									
									  BigDecimal   bd   =   new   BigDecimal(weight1);   
									  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数  
									  
									  DecimalFormat format = new DecimalFormat("#.##");
									  String sMoney = format.format(bd);
									  
									  sheet.addCell(new Label(columnIndex++, row, String.valueOf(sMoney)));//千克
									
									}catch(Exception e)
									{
										rowresult="重量异常";
										sheet.addCell(new Label(columnIndex++, row, ""));//重量（kg）
									}
									
									
									
									
									// 开始到处商品信息
									List<M_OrderDetail> details =  order1.getOrder().getDetail();
								
									String commudity="";
								    
									if ((details != null)&&(details.size()>0))
									{
										//String str="";
										for(M_OrderDetail d:details)
										{
											if(commudity.equalsIgnoreCase(""))
											{
												if(StringUtil.isEmpty(d.getBrands()))//这部分表示导入的是第三方数据，不能加入数量
												{
													commudity=d.getProductName();
												}
												else
												{
													commudity=d.getBrands()+d.getProductName()+d.getQuantity();
												}
											}
											else
											{
												if(StringUtil.isEmpty(d.getBrands()))//这部分表示导入的是第三方数据，不能加入数量
												{
													commudity=commudity+"、 "+d.getProductName();
												}
												else
												{
													commudity=commudity+"、 "+d.getBrands()+d.getProductName()+d.getQuantity();
												}
											}
										}
									}
									
									sheet.addCell(new Label(columnIndex++, row, commudity
											.toString()));//物品描述（英文品牌+中文品名+ 总克数）
									
									sheet.addCell(new Label(columnIndex++, row, ""));//备用四
									sheet.addCell(new Label(columnIndex++, row, ""));//备用三
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getQuantity()));//物品数量
									
									
									
									if(order1.getRate()>0)//包含汇率
									{
										try{
											int q=Integer.parseInt(order1.getOrder().getQuantity());
											double value=Double.parseDouble(order1.getOrder().getValue());
											if(value>0&&q>0)
											{
												double qvalue=(value*order1.getRate())/q;
												BigDecimal   bd   =   new   BigDecimal(qvalue);   
												bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数  
												
												
												 DecimalFormat format = new DecimalFormat("#.##");
												  String sMoney = format.format(bd);
												
												sheet.addCell(new Label(columnIndex++, row, String.valueOf(sMoney)));//单价
												
												
												
											}
											else
											{
												sheet.addCell(new Label(columnIndex++, row, ""));//单价
											}
										}catch(Exception e){
											sheet.addCell(new Label(columnIndex++, row, ""));//单价
										}
										
										try{
											
											double value=Double.parseDouble(order1.getOrder().getValue());
											if(value>0)
											{
												double allvalue=(value*order1.getRate());
												BigDecimal   bd   =   new   BigDecimal(allvalue);   
												bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数  
												
												
												 DecimalFormat format = new DecimalFormat("#.##");
												  String sMoney = format.format(bd);
												
												sheet.addCell(new Label(columnIndex++, row, String.valueOf(sMoney)));//人民币价值											
											}
											else
											{
												sheet.addCell(new Label(columnIndex++, row, ""));//人民币价值
											}
										}catch(Exception e){
											sheet.addCell(new Label(columnIndex++, row, ""));//人民币价值
										}
										
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));//物品单价（人民币）
										sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getValue() ));//声明价值（人民币）
									}
									
									
									//要匹配身份证
									if((order1.getCardmodel().equals("0")||order1.getCardmodel().equals("1")))
									{
										if(order1.getOrder().getRuser().getCardlib()!=null)//使用身份证信息
										{
											sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardlib().getCname()));//收货人姓名
											sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
										
											sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardlib().getCardid()));//收货人身份证号
										}
										else
										{
										
											rowresult="失败，匹配身份证失败!";
											sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
											sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
											
											sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
											
											
										}
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardid()));//收货人身份证号
									}
									
									
									if(order1.getOrder().getRuser()!=null)
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getZipcode()));//收件人邮编
										String address="";
										
										if(!StringUtil.isEmpty(order1.getOrder().getRuser().getState()))
										{
											address=address+order1.getOrder().getRuser().getState();
										}
										if(!StringUtil.isEmpty(order1.getOrder().getRuser().getCity()))
										{
											address=address+order1.getOrder().getRuser().getCity();
										}
										if(!StringUtil.isEmpty(order1.getOrder().getRuser().getDist()))
										{
											address=address+order1.getOrder().getRuser().getDist();
										}
										if(!StringUtil.isEmpty(order1.getOrder().getRuser().getAddress()))
										{
											address=address+order1.getOrder().getRuser().getAddress();
										}
										sheet.addCell(new Label(columnIndex++, row, address));//收货人地址
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCity()));//收货人城市
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getState()));//收货人省
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));//邮篇
										sheet.addCell(new Label(columnIndex++, row, ""));//收件人地址
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人城市
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人省
									}
									
									sheet.addCell(new Label(columnIndex++, row, ""));//批次
									
									if(order1.getOrder().getSuser()!=null)
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getSuser().getName()));//发件人
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getSuser().getPhone()));//发件人电话
										
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));//发件人
										sheet.addCell(new Label(columnIndex++, row, ""));//发件人电话
									}
									//发件人所在门店地址
									if(order1.getStore()!=null)
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getStore().getZipCode()));//发邮编
										sheet.addCell(new Label(columnIndex++, row, order1.getStore().getAddress()));//发件地址
										sheet.addCell(new Label(columnIndex++, row, order1.getStore().getCountry()));//发件国家
										sheet.addCell(new Label(columnIndex++, row, order1.getStore().getProvince()));//发件省
										sheet.addCell(new Label(columnIndex++, row, order1.getStore().getCity()));//发件城市
										
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));//
										sheet.addCell(new Label(columnIndex++, row, ""));//
										sheet.addCell(new Label(columnIndex++, row, ""));//
										sheet.addCell(new Label(columnIndex++, row, ""));//
										sheet.addCell(new Label(columnIndex++, row, ""));//
									
									}
									sheet.addCell(new Label(columnIndex++, row, ""));//备用1
									sheet.addCell(new Label(columnIndex++, row, ""));//备用2
									sheet.addCell(new Label(columnIndex++, row, ""));//备用3
									
									
									
									
									if(order1.getOrder()==null)
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getOrderResult(),redcolor));//导出结果
										sheet.addCell(new Label(columnIndex++, row, order1.getCardResult(),redcolor));//导出结果
									}
									else
									{
										
										
										if(!StringUtil.isEmpty(order1.getOrderResult())&&(order1.getOrderResult().indexOf("失败")!=-1||order1.getOrderResult().indexOf("异常")!=-1))
										{
											sheet.addCell(new Label(columnIndex++, row, order1.getOrderResult(),redcolor));
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row, order1.getOrderResult()));
										}
										if(!StringUtil.isEmpty(order1.getCardResult())&&(order1.getCardResult().indexOf("失败")!=-1||order1.getCardResult().indexOf("异常")!=-1))
										{
											sheet.addCell(new Label(columnIndex++, row, order1.getCardResult(),redcolor));
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row, order1.getCardResult()));
										}
									}
									
									if(!StringUtil.isEmpty(rowresult)&&(rowresult.indexOf("失败")!=-1||rowresult.indexOf("异常")!=-1))
									{
										sheet.addCell(new Label(35, row,rowresult,redcolor));
									}
									else
									{
										sheet.addCell(new Label(35, row, rowresult));
									}
									
									if(!StringUtil.isEmpty(order1.getGetSeaNoResult())&&(order1.getGetSeaNoResult().indexOf("失败")!=-1||order1.getGetSeaNoResult().indexOf("异常")!=-1))
									{
										sheet.addCell(new Label(36, row,order1.getGetSeaNoResult(),redcolor));
									}
									else
									{
										sheet.addCell(new Label(36, row, order1.getGetSeaNoResult()));
									}

									row++;
								}
								if(!StringUtil.isEmpty(strurl))
								{
									strurl="图片保存路径："+strurl;
									sheet.addCell(new Label(0, row, strurl));
								}
								/*if((!zipurl.equalsIgnoreCase("")))
								{
									WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
						                    10, WritableFont.NO_BOLD);// 字体样式
									WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
									oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
									sheet.addCell(new Label(0, row, zipurl,oceanbluecolor));//显示压缩包路径
									
								}*/
							}
						}, templetFile, orders, os);
					}
					
					
					
				}
				
			
				
				
				// A普货
				public static void exportA_PF(List<ExportMorder> orders,
						File templetFile, OutputStream os) throws Exception {
					if (templetFile != null && templetFile.exists()) {
						// 模板复制
						ExcelUtil.exportExcel(new ExcelWrite<ExportMorder>() {
							@Override
							public void write(Map<String, String> headers,
									Collection<ExportMorder> datas, WritableSheet sheet)
									throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有导入数据.....");
									sheet.addCell(cell);
									return;
								}
								
								int row = 1;

								//String zipurl="";
								String strurl="";
								for (ExportMorder order1 : datas) {
									if (order1 == null) {
										continue;
									}
								//	if(!StringUtil.isEmpty(order.getZipurl())&&(zipurl.equalsIgnoreCase("")))
									//{
									//	zipurl=order.getZipurl();
									//}
									WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
						                    10, WritableFont.NO_BOLD);// 字体样式
									
									WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
									redcolor.setBackground(Colour.RED);
									
									WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
									oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
									int columnIndex = 0;
									
									sheet.addCell(new Label(columnIndex++, row, String.valueOf(row)));//序号
									sheet.addCell(new Label(columnIndex++, row, ""));// 客户账号
									sheet.addCell(new Label(columnIndex++, row, ""));// 标签
									sheet.addCell(new Label(columnIndex++, row, ""));// 所属部门
									sheet.addCell(new Label(columnIndex++, row, order1
											.getOrderId()));// 内单号（客户自己单号）
									
									if(StringUtil.isEmpty(order1.getSavezipurl()))
									{}
									else
									{
										if(StringUtil.isEmpty(strurl))
										{
											strurl=order1.getSavezipurl();
										}
									}
									String rowresult="成功!";
									if(order1.getOrder()==null)
									{
										//sheet.addCell(new Label(columnIndex++, row, ""));//分单号
										if(!StringUtil.isEmpty(order1.getOrderResult())&&(order1.getOrderResult().indexOf("失败")!=-1||order1.getOrderResult().indexOf("异常")!=-1))
										{
											sheet.addCell(new Label(34, row, order1.getOrderResult(),redcolor));
										}
										else
										{
											sheet.addCell(new Label(34, row, order1.getOrderResult()));
										}
										if(!StringUtil.isEmpty(order1.getCardResult())&&(order1.getCardResult().indexOf("失败")!=-1||order1.getCardResult().indexOf("异常")!=-1))
										{
											sheet.addCell(new Label(35, row, order1.getCardResult(),redcolor));
										}
										else
										{
											sheet.addCell(new Label(35, row, order1.getCardResult()));
										}
										sheet.addCell(new Label(36, row, "失败!",redcolor));
										sheet.addCell(new Label(37, row, "海关单号失败",redcolor));//导出失败
										row++;
										continue;
									}
									else
									{
										//sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getSorderId()));//分单号
										try
										{
											if(Double.parseDouble(order1.getOrder().getState())<2)//没有揽收的包裹
											{
												if(StringUtil.isEmpty(order1.getOrderResult()))
												{
													order1.setOrderResult("状态异常，必须是已揽收的");
												}
												else
												{
													order1.setOrderResult(order1.getOrderResult()+",必须是已揽收的");
												}
												
												sheet.addCell(new Label(34, row, order1.getOrderResult(),redcolor));//导出结果
												sheet.addCell(new Label(35, row, order1.getCardResult(),redcolor));//导出结果
												sheet.addCell(new Label(36, row, "失败",redcolor));//导出失败
												sheet.addCell(new Label(37, row, order1.getGetSeaNoResult(),redcolor));//导出失败
												row++;
												continue;
											}
										}
										catch(Exception e)
										{
											if(StringUtil.isEmpty(order1.getOrderResult()))
											{
												order1.setOrderResult("状态异常，必须是已揽收的");
											}
											else
											{
												order1.setOrderResult(order1.getOrderResult()+",必须是已揽收的");
											}
											sheet.addCell(new Label(34, row, order1.getOrderResult(),redcolor));//导出结果
											sheet.addCell(new Label(35, row, order1.getCardResult(),redcolor));//导出结果
											sheet.addCell(new Label(36, row, "失败",redcolor));//导出失败
											sheet.addCell(new Label(37, row, order1.getGetSeaNoResult(),redcolor));//导出失败
											row++;
											continue;
										}
									}
									
									//转单号(国内物流单号)
                                    sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getThirdNo()));// 转单号(国内物流单号)
									
									sheet.addCell(new Label(columnIndex++, row, ""));//参考号
									sheet.addCell(new Label(columnIndex++, row, "1"));//件数（一个包裹就是件数1）
									
									
									
									try{
										String weight="";
										weight=order1.getOrder().getSjweight();
										if(!StringUtil.isEmpty(weight))
										{
											if(Double.parseDouble(weight)<=0)
											{
												weight="";
											}
										}
										
										
										if(StringUtil.isEmpty(weight))
										{
											weight=order1.getOrder().getWeight();
										}
										
									double weight1=Double.parseDouble(weight);
									if(weight1<=0)
									{
										rowresult="重量信息异常";
									}
									weight1=weight1*0.4536;
									
									
									  BigDecimal   bd   =   new   BigDecimal(weight1);   
									  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数  
									  
									  DecimalFormat format = new DecimalFormat("#.##");
									  String sMoney = format.format(bd);
									  
									  sheet.addCell(new Label(columnIndex++, row, String.valueOf(sMoney)));//重量
									  sheet.addCell(new Label(columnIndex++, row, String.valueOf(sMoney)));//重量（kg）
									}catch(Exception e)
									{
										rowresult="重量异常";
										sheet.addCell(new Label(columnIndex++, row, ""));//重量
										sheet.addCell(new Label(columnIndex++, row, ""));//重量（kg）
									}
									
									
									
									
									// 开始到处商品信息
									List<M_OrderDetail> details =  order1.getOrder().getDetail();
								
									String commudity="";
								    
									if ((details != null)&&(details.size()>0))
									{
										//String str="";
										for(M_OrderDetail d:details)
										{
											if(commudity.equalsIgnoreCase(""))
											{
												if(StringUtil.isEmpty(d.getBrands()))//这部分表示导入的是第三方数据，不能加入数量
												{
													commudity=d.getProductName();
												}
												else
												{
													commudity=d.getBrands()+d.getProductName()+d.getQuantity();
												}
											}
											else
											{
												if(StringUtil.isEmpty(d.getBrands()))//这部分表示导入的是第三方数据，不能加入数量
												{
													commudity=commudity+"、 "+d.getProductName();
												}
												else
												{
													commudity=commudity+"、 "+d.getBrands()+d.getProductName()+d.getQuantity();
												}
											}
										}
									}
									
									sheet.addCell(new Label(columnIndex++, row, commudity
											.toString()));//物品描述（英文品牌+中文品名+ 总克数）
									
									sheet.addCell(new Label(columnIndex++, row, ""));//备用四
									sheet.addCell(new Label(columnIndex++, row, ""));//备用三
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getQuantity()));//物品数量
									
									
									
									if(order1.getRate()>0)//包含汇率
									{
										try{
											int q=Integer.parseInt(order1.getOrder().getQuantity());
											double value=Double.parseDouble(order1.getOrder().getValue());
											if(value>0&&q>0)
											{
												double qvalue=(value*order1.getRate())/q;
												BigDecimal   bd   =   new   BigDecimal(qvalue);   
												bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数  
												
												
												 DecimalFormat format = new DecimalFormat("#.##");
												  String sMoney = format.format(bd);
												
												sheet.addCell(new Label(columnIndex++, row, String.valueOf(sMoney)));//单价
												
												
												
											}
											else
											{
												sheet.addCell(new Label(columnIndex++, row, ""));//单价
											}
										}catch(Exception e){
											sheet.addCell(new Label(columnIndex++, row, ""));//单价
										}
										
										try{
											
											double value=Double.parseDouble(order1.getOrder().getValue());
											if(value>0)
											{
												double allvalue=(value*order1.getRate());
												BigDecimal   bd   =   new   BigDecimal(allvalue);   
												bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数  
												
												
												 DecimalFormat format = new DecimalFormat("#.##");
												  String sMoney = format.format(bd);
												
												sheet.addCell(new Label(columnIndex++, row, String.valueOf(sMoney)));//人民币价值											
											}
											else
											{
												sheet.addCell(new Label(columnIndex++, row, ""));//人民币价值
											}
										}catch(Exception e){
											sheet.addCell(new Label(columnIndex++, row, ""));//人民币价值
										}
										
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));//物品单价（人民币）
										sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getValue() ));//声明价值（人民币）
									}
									
									
									//要匹配身份证
									if((order1.getCardmodel().equals("0")||order1.getCardmodel().equals("1")))
									{
										if(order1.getOrder().getRuser().getCardlib()!=null)//使用身份证信息
										{
											sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardlib().getCname()));//收货人姓名
											sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
										
											sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardlib().getCardid()));//收货人身份证号
										}
										else
										{
										
											rowresult="失败，匹配身份证失败!";
											sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
											sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
											
											sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
											
											
										}
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardid()));//收货人身份证号
									}
									
									
									if(order1.getOrder().getRuser()!=null)
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getZipcode()));//收件人邮编
										String address="";
										
										if(!StringUtil.isEmpty(order1.getOrder().getRuser().getState()))
										{
											address=address+order1.getOrder().getRuser().getState();
										}
										if(!StringUtil.isEmpty(order1.getOrder().getRuser().getCity()))
										{
											address=address+order1.getOrder().getRuser().getCity();
										}
										if(!StringUtil.isEmpty(order1.getOrder().getRuser().getDist()))
										{
											address=address+order1.getOrder().getRuser().getDist();
										}
										if(!StringUtil.isEmpty(order1.getOrder().getRuser().getAddress()))
										{
											address=address+order1.getOrder().getRuser().getAddress();
										}
										sheet.addCell(new Label(columnIndex++, row, address));//收货人地址
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCity()));//收货人城市
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getState()));//收货人省
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));//邮篇
										sheet.addCell(new Label(columnIndex++, row, ""));//收件人地址
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人城市
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人省
									}
									
									sheet.addCell(new Label(columnIndex++, row, ""));//批次
									
									if(order1.getOrder().getSuser()!=null)
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getSuser().getName()));//发件人
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getSuser().getPhone()));//发件人电话
										
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));//发件人
										sheet.addCell(new Label(columnIndex++, row, ""));//发件人电话
									}
									//发件人所在门店地址
									if(order1.getStore()!=null)
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getStore().getZipCode()));//发邮编
										sheet.addCell(new Label(columnIndex++, row, order1.getStore().getAddress()));//发件地址
										sheet.addCell(new Label(columnIndex++, row, order1.getStore().getCountry()));//发件国家
										sheet.addCell(new Label(columnIndex++, row, order1.getStore().getProvince()));//发件省
										sheet.addCell(new Label(columnIndex++, row, order1.getStore().getCity()));//发件城市
										
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));//
										sheet.addCell(new Label(columnIndex++, row, ""));//
										sheet.addCell(new Label(columnIndex++, row, ""));//
										sheet.addCell(new Label(columnIndex++, row, ""));//
										sheet.addCell(new Label(columnIndex++, row, ""));//
									
									}
									sheet.addCell(new Label(columnIndex++, row, ""));//备用1
									sheet.addCell(new Label(columnIndex++, row, ""));//备用2
									sheet.addCell(new Label(columnIndex++, row, ""));//备用5
									
									
									
									
									if(order1.getOrder()==null)
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getOrderResult(),redcolor));//导出结果
										sheet.addCell(new Label(columnIndex++, row, order1.getCardResult(),redcolor));//导出结果
									}
									else
									{
										
										
										if(!StringUtil.isEmpty(order1.getOrderResult())&&(order1.getOrderResult().indexOf("失败")!=-1||order1.getOrderResult().indexOf("异常")!=-1))
										{
											sheet.addCell(new Label(columnIndex++, row, order1.getOrderResult(),redcolor));
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row, order1.getOrderResult()));
										}
										if(!StringUtil.isEmpty(order1.getCardResult())&&(order1.getCardResult().indexOf("失败")!=-1||order1.getCardResult().indexOf("异常")!=-1))
										{
											sheet.addCell(new Label(columnIndex++, row, order1.getCardResult(),redcolor));
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row, order1.getCardResult()));
										}
									}
									
									if(!StringUtil.isEmpty(rowresult)&&(rowresult.indexOf("失败")!=-1||rowresult.indexOf("异常")!=-1))
									{
										sheet.addCell(new Label(36, row,rowresult,redcolor));
									}
									else
									{
										sheet.addCell(new Label(36, row, rowresult));
									}
									
									if(!StringUtil.isEmpty(order1.getGetSeaNoResult())&&(order1.getGetSeaNoResult().indexOf("失败")!=-1||order1.getGetSeaNoResult().indexOf("异常")!=-1))
									{
										sheet.addCell(new Label(37, row,order1.getGetSeaNoResult(),redcolor));
									}
									else
									{
										sheet.addCell(new Label(37, row, order1.getGetSeaNoResult()));
									}

									row++;
								}
								if(!StringUtil.isEmpty(strurl))
								{
									strurl="图片保存路径："+strurl;
									sheet.addCell(new Label(0, row, strurl));
								}
								/*if((!zipurl.equalsIgnoreCase("")))
								{
									WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
						                    10, WritableFont.NO_BOLD);// 字体样式
									WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
									oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
									sheet.addCell(new Label(0, row, zipurl,oceanbluecolor));//显示压缩包路径
									
								}*/
							}
						}, templetFile, orders, os);
					} else {
						// 模板复制
						ExcelUtil.exportExcel(new ExcelWrite<ExportMorder>() {
							@Override
							public void write(Map<String, String> headers,
									Collection<ExportMorder> datas, WritableSheet sheet)
									throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有导入数据.....");
									sheet.addCell(cell);
									return;
								}
								
								int row = 1;

								//String zipurl="";
								String strurl="";
								for (ExportMorder order1 : datas) {
									if (order1 == null) {
										continue;
									}
								//	if(!StringUtil.isEmpty(order.getZipurl())&&(zipurl.equalsIgnoreCase("")))
									//{
									//	zipurl=order.getZipurl();
									//}
									WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
						                    10, WritableFont.NO_BOLD);// 字体样式
									
									WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
									redcolor.setBackground(Colour.RED);
									
									WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
									oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
									int columnIndex = 0;
									
									sheet.addCell(new Label(columnIndex++, row, String.valueOf(row)));//序号
									sheet.addCell(new Label(columnIndex++, row, ""));// 客户账号
									sheet.addCell(new Label(columnIndex++, row, ""));// 标签
									sheet.addCell(new Label(columnIndex++, row, ""));// 所属部门
									sheet.addCell(new Label(columnIndex++, row, order1
											.getOrderId()));// 内单号（客户自己单号）
									
									if(StringUtil.isEmpty(order1.getSavezipurl()))
									{}
									else
									{
										if(StringUtil.isEmpty(strurl))
										{
											strurl=order1.getSavezipurl();
										}
									}
									String rowresult="成功!";
									if(order1.getOrder()==null)
									{
										//sheet.addCell(new Label(columnIndex++, row, ""));//分单号
										if(!StringUtil.isEmpty(order1.getOrderResult())&&(order1.getOrderResult().indexOf("失败")!=-1||order1.getOrderResult().indexOf("异常")!=-1))
										{
											sheet.addCell(new Label(34, row, order1.getOrderResult(),redcolor));
										}
										else
										{
											sheet.addCell(new Label(34, row, order1.getOrderResult()));
										}
										if(!StringUtil.isEmpty(order1.getCardResult())&&(order1.getCardResult().indexOf("失败")!=-1||order1.getCardResult().indexOf("异常")!=-1))
										{
											sheet.addCell(new Label(35, row, order1.getCardResult(),redcolor));
										}
										else
										{
											sheet.addCell(new Label(35, row, order1.getCardResult()));
										}
										sheet.addCell(new Label(36, row, "失败!",redcolor));
										sheet.addCell(new Label(37, row, "海关单号失败",redcolor));//导出失败
										row++;
										continue;
									}
									else
									{
										//sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getSorderId()));//分单号
										try
										{
											if(Double.parseDouble(order1.getOrder().getState())<2)//没有揽收的包裹
											{
												if(StringUtil.isEmpty(order1.getOrderResult()))
												{
													order1.setOrderResult("状态异常，必须是已揽收的");
												}
												else
												{
													order1.setOrderResult(order1.getOrderResult()+",必须是已揽收的");
												}
												
												sheet.addCell(new Label(34, row, order1.getOrderResult(),redcolor));//导出结果
												sheet.addCell(new Label(35, row, order1.getCardResult(),redcolor));//导出结果
												sheet.addCell(new Label(36, row, "失败",redcolor));//导出失败
												sheet.addCell(new Label(37, row, order1.getGetSeaNoResult(),redcolor));//导出失败
												row++;
												continue;
											}
										}
										catch(Exception e)
										{
											if(StringUtil.isEmpty(order1.getOrderResult()))
											{
												order1.setOrderResult("状态异常，必须是已揽收的");
											}
											else
											{
												order1.setOrderResult(order1.getOrderResult()+",必须是已揽收的");
											}
											sheet.addCell(new Label(34, row, order1.getOrderResult(),redcolor));//导出结果
											sheet.addCell(new Label(35, row, order1.getCardResult(),redcolor));//导出结果
											sheet.addCell(new Label(36, row, "失败",redcolor));//导出失败
											sheet.addCell(new Label(37, row, order1.getGetSeaNoResult(),redcolor));//导出失败
											row++;
											continue;
										}
									}
									
									//转单号(国内物流单号)
                                    sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getThirdNo()));// 转单号(国内物流单号)
									
									sheet.addCell(new Label(columnIndex++, row, ""));//参考号
									sheet.addCell(new Label(columnIndex++, row, "1"));//件数（一个包裹就是件数1）
									
									
									
									try{
										String weight="";
										weight=order1.getOrder().getSjweight();
										if(!StringUtil.isEmpty(weight))
										{
											if(Double.parseDouble(weight)<=0)
											{
												weight="";
											}
										}
										
										
										if(StringUtil.isEmpty(weight))
										{
											weight=order1.getOrder().getWeight();
										}
										
									double weight1=Double.parseDouble(weight);
									if(weight1<=0)
									{
										rowresult="重量信息异常";
									}
									weight1=weight1*0.4536;
									
									
									  BigDecimal   bd   =   new   BigDecimal(weight1);   
									  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数  
									  
									  DecimalFormat format = new DecimalFormat("#.##");
									  String sMoney = format.format(bd);
									  
									  sheet.addCell(new Label(columnIndex++, row, String.valueOf(sMoney)));//重量
									  sheet.addCell(new Label(columnIndex++, row, String.valueOf(sMoney)));//重量（kg）
									}catch(Exception e)
									{
										rowresult="重量异常";
										sheet.addCell(new Label(columnIndex++, row, ""));//重量
										sheet.addCell(new Label(columnIndex++, row, ""));//重量（kg）
									}
									
									
									
									
									// 开始到处商品信息
									List<M_OrderDetail> details =  order1.getOrder().getDetail();
								
									String commudity="";
								    
									if ((details != null)&&(details.size()>0))
									{
										//String str="";
										for(M_OrderDetail d:details)
										{
											if(commudity.equalsIgnoreCase(""))
											{
												if(StringUtil.isEmpty(d.getBrands()))//这部分表示导入的是第三方数据，不能加入数量
												{
													commudity=d.getProductName();
												}
												else
												{
													commudity=d.getBrands()+d.getProductName()+d.getQuantity();
												}
											}
											else
											{
												if(StringUtil.isEmpty(d.getBrands()))//这部分表示导入的是第三方数据，不能加入数量
												{
													commudity=commudity+"、 "+d.getProductName();
												}
												else
												{
													commudity=commudity+"、 "+d.getBrands()+d.getProductName()+d.getQuantity();
												}
											}
										}
									}
									
									sheet.addCell(new Label(columnIndex++, row, commudity
											.toString()));//物品描述（英文品牌+中文品名+ 总克数）
									
									sheet.addCell(new Label(columnIndex++, row, ""));//备用四
									sheet.addCell(new Label(columnIndex++, row, ""));//备用三
									sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getQuantity()));//物品数量
									
									
									
									if(order1.getRate()>0)//包含汇率
									{
										try{
											int q=Integer.parseInt(order1.getOrder().getQuantity());
											double value=Double.parseDouble(order1.getOrder().getValue());
											if(value>0&&q>0)
											{
												double qvalue=(value*order1.getRate())/q;
												BigDecimal   bd   =   new   BigDecimal(qvalue);   
												bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数  
												
												
												 DecimalFormat format = new DecimalFormat("#.##");
												  String sMoney = format.format(bd);
												
												sheet.addCell(new Label(columnIndex++, row, String.valueOf(sMoney)));//单价
												
												
												
											}
											else
											{
												sheet.addCell(new Label(columnIndex++, row, ""));//单价
											}
										}catch(Exception e){
											sheet.addCell(new Label(columnIndex++, row, ""));//单价
										}
										
										try{
											
											double value=Double.parseDouble(order1.getOrder().getValue());
											if(value>0)
											{
												double allvalue=(value*order1.getRate());
												BigDecimal   bd   =   new   BigDecimal(allvalue);   
												bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数  
												
												
												 DecimalFormat format = new DecimalFormat("#.##");
												  String sMoney = format.format(bd);
												
												sheet.addCell(new Label(columnIndex++, row, String.valueOf(sMoney)));//人民币价值											
											}
											else
											{
												sheet.addCell(new Label(columnIndex++, row, ""));//人民币价值
											}
										}catch(Exception e){
											sheet.addCell(new Label(columnIndex++, row, ""));//人民币价值
										}
										
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));//物品单价（人民币）
										sheet.addCell(new Label(columnIndex++, row,order1.getOrder().getValue() ));//声明价值（人民币）
									}
									
									
									//要匹配身份证
									if((order1.getCardmodel().equals("0")||order1.getCardmodel().equals("1")))
									{
										if(order1.getOrder().getRuser().getCardlib()!=null)//使用身份证信息
										{
											sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardlib().getCname()));//收货人姓名
											sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
										
											sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardlib().getCardid()));//收货人身份证号
										}
										else
										{
										
											rowresult="失败，匹配身份证失败!";
											sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
											sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
											
											sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
											
											
										}
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getName()));//收货人姓名
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getPhone()));//收货人电话
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCardid()));//收货人身份证号
									}
									
									
									if(order1.getOrder().getRuser()!=null)
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getZipcode()));//收件人邮编
										String address="";
										
										if(!StringUtil.isEmpty(order1.getOrder().getRuser().getState()))
										{
											address=address+order1.getOrder().getRuser().getState();
										}
										if(!StringUtil.isEmpty(order1.getOrder().getRuser().getCity()))
										{
											address=address+order1.getOrder().getRuser().getCity();
										}
										if(!StringUtil.isEmpty(order1.getOrder().getRuser().getDist()))
										{
											address=address+order1.getOrder().getRuser().getDist();
										}
										if(!StringUtil.isEmpty(order1.getOrder().getRuser().getAddress()))
										{
											address=address+order1.getOrder().getRuser().getAddress();
										}
										sheet.addCell(new Label(columnIndex++, row, address));//收货人地址
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getCity()));//收货人城市
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getRuser().getState()));//收货人省
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));//邮篇
										sheet.addCell(new Label(columnIndex++, row, ""));//收件人地址
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人城市
										sheet.addCell(new Label(columnIndex++, row, ""));//收货人省
									}
									
									sheet.addCell(new Label(columnIndex++, row, ""));//批次
									
									if(order1.getOrder().getSuser()!=null)
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getSuser().getName()));//发件人
										sheet.addCell(new Label(columnIndex++, row, order1.getOrder().getSuser().getPhone()));//发件人电话
										
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));//发件人
										sheet.addCell(new Label(columnIndex++, row, ""));//发件人电话
									}
									//发件人所在门店地址
									if(order1.getStore()!=null)
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getStore().getZipCode()));//发邮编
										sheet.addCell(new Label(columnIndex++, row, order1.getStore().getAddress()));//发件地址
										sheet.addCell(new Label(columnIndex++, row, order1.getStore().getCountry()));//发件国家
										sheet.addCell(new Label(columnIndex++, row, order1.getStore().getProvince()));//发件省
										sheet.addCell(new Label(columnIndex++, row, order1.getStore().getCity()));//发件城市
										
									}
									else
									{
										sheet.addCell(new Label(columnIndex++, row, ""));//
										sheet.addCell(new Label(columnIndex++, row, ""));//
										sheet.addCell(new Label(columnIndex++, row, ""));//
										sheet.addCell(new Label(columnIndex++, row, ""));//
										sheet.addCell(new Label(columnIndex++, row, ""));//
									
									}
									sheet.addCell(new Label(columnIndex++, row, ""));//备用1
									sheet.addCell(new Label(columnIndex++, row, ""));//备用2
									sheet.addCell(new Label(columnIndex++, row, ""));//备用5
									
									
									
									
									if(order1.getOrder()==null)
									{
										sheet.addCell(new Label(columnIndex++, row, order1.getOrderResult(),redcolor));//导出结果
										sheet.addCell(new Label(columnIndex++, row, order1.getCardResult(),redcolor));//导出结果
									}
									else
									{
										
										
										if(!StringUtil.isEmpty(order1.getOrderResult())&&(order1.getOrderResult().indexOf("失败")!=-1||order1.getOrderResult().indexOf("异常")!=-1))
										{
											sheet.addCell(new Label(columnIndex++, row, order1.getOrderResult(),redcolor));
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row, order1.getOrderResult()));
										}
										if(!StringUtil.isEmpty(order1.getCardResult())&&(order1.getCardResult().indexOf("失败")!=-1||order1.getCardResult().indexOf("异常")!=-1))
										{
											sheet.addCell(new Label(columnIndex++, row, order1.getCardResult(),redcolor));
										}
										else
										{
											sheet.addCell(new Label(columnIndex++, row, order1.getCardResult()));
										}
									}
									
									if(!StringUtil.isEmpty(rowresult)&&(rowresult.indexOf("失败")!=-1||rowresult.indexOf("异常")!=-1))
									{
										sheet.addCell(new Label(36, row,rowresult,redcolor));
									}
									else
									{
										sheet.addCell(new Label(36, row, rowresult));
									}
									
									if(!StringUtil.isEmpty(order1.getGetSeaNoResult())&&(order1.getGetSeaNoResult().indexOf("失败")!=-1||order1.getGetSeaNoResult().indexOf("异常")!=-1))
									{
										sheet.addCell(new Label(37, row,order1.getGetSeaNoResult(),redcolor));
									}
									else
									{
										sheet.addCell(new Label(37, row, order1.getGetSeaNoResult()));
									}

									row++;
								}
								if(!StringUtil.isEmpty(strurl))
								{
									strurl="图片保存路径："+strurl;
									sheet.addCell(new Label(0, row, strurl));
								}
								/*if((!zipurl.equalsIgnoreCase("")))
								{
									WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
						                    10, WritableFont.NO_BOLD);// 字体样式
									WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
									oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
									sheet.addCell(new Label(0, row, zipurl,oceanbluecolor));//显示压缩包路径
									
								}*/
							}
						}, templetFile, orders, os);
					}
					
					
					
				}
				
				
	}

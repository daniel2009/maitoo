package com.meitao.cardid.manage;

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

import com.meitao.cardid.manage.importcardargs;
import com.meitao.common.constants.Constant;
import com.meitao.common.util.StringUtil;
import com.meitao.common.util.excel.ExcelReader;
import com.meitao.common.util.excel.ExcelUtil;
import com.meitao.common.util.excel.ExcelWrite;
import com.meitao.model.CardId;
import com.meitao.model.ConsigneeInfo;
import com.meitao.model.M_OrderDetail;
import com.meitao.model.Order;
import com.meitao.model.OrderDetail;
import com.meitao.model.Porder;
import com.meitao.model.SumCommodity;
import com.meitao.model.User;
import com.meitao.model.temp.ExportMorder;
import com.meitao.model.temp.ExportOrder;
import com.meitao.model.temp.ImportOrder;
import com.meitao.model.temp.ImportthirdOrder;


public class CardidManageControllerUtil {
	


	
	// 批量导入身份证结果导出
	public static void exportzipcardidsToResult(List<importcardargs> orders,
			File templetFile, OutputStream os) throws Exception {
		if (templetFile != null && templetFile.exists()) {
			// 模板复制
			ExcelUtil.exportExcel(new ExcelWrite<importcardargs>() {
				@Override
				public void write(Map<String, String> headers,
						Collection<importcardargs> datas, WritableSheet sheet)
						throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有导入数据.....");
						sheet.addCell(cell);
						return;
					}
					
					int row = 1;

					for (importcardargs order : datas) {
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
					
						if(order.isFlag())//成功信息
						{
							sheet.addCell(new Label(columnIndex++, row, order.getCardid()));
							sheet.addCell(new Label(columnIndex++, row, order.getCardname()));
							sheet.addCell(new Label(columnIndex++, row, order.getFilename()));
							sheet.addCell(new Label(columnIndex++, row, order.getResult()));
						
						
							
						}
						else
						{
							
							
							sheet.addCell(new Label(columnIndex++, row, order.getCardid(),redcolor));
							sheet.addCell(new Label(columnIndex++, row, order.getCardname(),redcolor));
							sheet.addCell(new Label(columnIndex++, row, order.getFilename(),redcolor));
							sheet.addCell(new Label(columnIndex++, row, order.getResult(),redcolor));
						
						}
						
						

						row++;
					}
				}
			}, templetFile, orders, os);
		} else {
			// 模板复制
			ExcelUtil.exportExcel(new ExcelWrite<importcardargs>() {
				@Override
				public void write(Map<String, String> headers,
						Collection<importcardargs> datas, WritableSheet sheet)
						throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有导入数据.....");
						sheet.addCell(cell);
						return;
					}
					
					int row = 1;

					for (importcardargs order : datas) {
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
					
						if(order.isFlag())//成功信息
						{
							sheet.addCell(new Label(columnIndex++, row, order.getCardid()));
							sheet.addCell(new Label(columnIndex++, row, order.getCardname()));
							sheet.addCell(new Label(columnIndex++, row, order.getFilename()));
							sheet.addCell(new Label(columnIndex++, row, order.getResult()));
							
						
						
							
						}
						else
						{
							
							
							sheet.addCell(new Label(columnIndex++, row, order.getCardid(),redcolor));
							sheet.addCell(new Label(columnIndex++, row, order.getCardname(),redcolor));
							sheet.addCell(new Label(columnIndex++, row, order.getFilename(),redcolor));
							sheet.addCell(new Label(columnIndex++, row, order.getResult(),redcolor));
						
						}
						
						

						row++;
					}
				}
			}, templetFile, orders, os);
		}
		
		
		
	}
	
	
	
	// kai 20160315 导入匹配身份证模板时，读取导入的数据，meitao
	public static List<import_t_orders> readgetCardidFromMeitaoExcel(InputStream input)
			throws Exception {
		return ExcelUtil.importExcel(new ExcelReader<import_t_orders>() {
			@Override
			public List<import_t_orders> read(Sheet sheet) throws Exception {
				List<import_t_orders> list = new ArrayList<import_t_orders>();
				int rows = sheet.getRows();
				if (rows < 2) {
					throw new RuntimeException("运单文件不能为空！");
				}
				for (int i = 1; i < rows; i++) {

					int j = 0;
					
					import_t_orders order = new import_t_orders();
				
					String orderid=StringUtil.getValue(sheet.getCell(j++, i).getContents(), "");//运单号
					String weight=StringUtil.getValue(sheet.getCell(j++, i).getContents(), "");//导入的重量
					String boxq=StringUtil.getValue(sheet.getCell(j++, i).getContents(), "");//保存导入的箱数
					String commudityslist=StringUtil.getValue(sheet.getCell(j++, i).getContents(), "");//内容
					String s_name=StringUtil.getValue(sheet.getCell(j++, i).getContents(), "");//发件人的姓名
					String s_address=StringUtil.getValue(sheet.getCell(j++, i).getContents(), "");////保存发件人的地址
					String s_city=StringUtil.getValue(sheet.getCell(j++, i).getContents(), "");////保存发件人的城市
					String s_privice=StringUtil.getValue(sheet.getCell(j++, i).getContents(), "");////保存发件人的省
					String s_zipcode=StringUtil.getValue(sheet.getCell(j++, i).getContents(), "");////保存发件人的邮箱
					String r_name=StringUtil.getValue(sheet.getCell(j++, i).getContents(), "");////保存发件人的姓名
					String r_privice=StringUtil.getValue(sheet.getCell(j++, i).getContents(), "");////保存发件人的省
					String r_city=StringUtil.getValue(sheet.getCell(j++, i).getContents(), "");////保存发件人的城市
					String r_address=StringUtil.getValue(sheet.getCell(j++, i).getContents(), "");////保存发件人的地址
					String r_phone=StringUtil.getValue(sheet.getCell(j++, i).getContents(), "");////保存发件人的电话
					String r_zipcode=StringUtil.getValue(sheet.getCell(j++, i).getContents(), "");////保存发件人的邮编
					order.setOrderid(orderid);
					order.setWeight(weight);
					order.setBoxq(boxq);
					order.setCommudityslist(commudityslist);
					order.setS_name(s_name);
					order.setS_address(s_address);
					order.setS_city(s_city);
					order.setS_privice(s_privice);
					order.setS_zipcode(s_zipcode);
					order.setR_name(r_name);
					order.setR_privice(r_privice);
					order.setR_city(r_city);
					order.setR_address(r_address);
					order.setR_phone(r_phone);
					order.setR_zipcode(r_zipcode);
					
					order.setResultflag(false);
					
					list.add(order);
				}
				return list;
			}
		}, input);
	}
	
	
	// 导出内蒙古的核对模板
	public static void exportneimenggu(List<import_t_orders> orders,
			File templetFile, OutputStream os) throws Exception {
		if (templetFile != null && templetFile.exists()) {
			// 模板复制
			ExcelUtil.exportExcel(new ExcelWrite<import_t_orders>() {
				@Override
				public void write(Map<String, String> headers,
						Collection<import_t_orders> datas, WritableSheet sheet)
						throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有导入数据.....");
						sheet.addCell(cell);
						return;
					}
					
					int row = 2;

					String zipurl="";
					for (import_t_orders order : datas) {
						if (order == null) {
							continue;
						}
						if(!StringUtil.isEmpty(order.getZipurl())&&(zipurl.equalsIgnoreCase("")))
						{
							zipurl=order.getZipurl();
						}
						WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
			                    10, WritableFont.NO_BOLD);// 字体样式
						
						WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
						redcolor.setBackground(Colour.RED);
						
						WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
						oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
						int columnIndex = 0;
						sheet.addCell(new Label(columnIndex++, row, String.valueOf(row-1)));//序号
						sheet.addCell(new Label(columnIndex++, row, order.getOrderid()));//运单号
						sheet.addCell(new Label(columnIndex++, row, ""));//分单号
						
						
						if(!StringUtil.isEmpty(order.getCommudityslist()))
						{
							
							String commundity1=order.getCommudityslist();
							commundity1=commundity1.replace("，", ";");
							commundity1=commundity1.replace(",", ";");
							commundity1=commundity1.replace("；", ";");//把其它字符换成统一的，方便区分
							
							String[] comm=commundity1.split(";");
							int flag=0;
							String taxcomm="";//海关品名
							String brand="";
							int number=0;
							for(int i=0;i<comm.length;i++)
							{
								
								if(!StringUtil.isEmpty(comm[i]))
								{
									String[] name1=comm[i].split(":");
									
									/*if(!StringUtil.isEmpty((name1[0])))
									{
										name1[0]=name1[0].substring(0, 5);
										name1[0]=name1[0]+"*1";
									}*/
									if(brand.equalsIgnoreCase(""))
									{
										//brand=name1[0].substring(0,5);
										String[] name2=name1[0].split("\\*");
										
										
										try{
											number+=Integer.parseInt(name2[1]);
										}catch(Exception e){
											number+=1;
										}
										
										if(name2[0].length()>=5)
										{
											
											brand=name2[0].substring(0,5);
											
										}
										else
										{
											brand=name2[0];
										}
									}
									else
									{
										String[] name2=name1[0].split("\\*");
										
										try{
											number+=Integer.parseInt(name2[1]);
										}catch(Exception e){
											number+=1;
										}
										
										//brand=name1[0].substring(0,5);
										if(name2[0].length()>=5)
										{
											brand=brand+","+name2[0].substring(0,5);
										}
										else
										{
											brand=brand+","+name2[0];
										}
									}
									//name1[0]=name1[0]+"*1";
									if(flag!=0)
									{
										taxcomm=taxcomm+","+name1[0];
									}
									else
									{
										taxcomm=name1[0];
									}

									flag++;	
								}
								
							}
							sheet.addCell(new Label(columnIndex++, row, taxcomm));//报关品名
							sheet.addCell(new Label(columnIndex++, row, order.getCommudityslist()));//品名
							sheet.addCell(new Label(columnIndex++, row, brand));//品牌
							sheet.addCell(new Label(columnIndex++, row, ""));//规格
							//sheet.addCell(new Label(columnIndex++, row, String.valueOf(flag)));//数量number
							sheet.addCell(new Label(columnIndex++, row, String.valueOf(number)));//数量
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, ""));//报关品名
							sheet.addCell(new Label(columnIndex++, row, ""));//品名
							sheet.addCell(new Label(columnIndex++, row, ""));//品牌
							sheet.addCell(new Label(columnIndex++, row, ""));//规格
							sheet.addCell(new Label(columnIndex++, row, ""));//数量
						}
						
						try{
							double weight=Double.parseDouble(order.getWeight());
							weight=weight*0.4536;
							
							  BigDecimal   bd   =   new   BigDecimal(weight);   
							  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
							  sheet.addCell(new Label(columnIndex++, row, String.valueOf(bd)));//千克
						}catch(Exception e)
						{
							sheet.addCell(new Label(columnIndex++, row, ""));//数字发生异常
						}
						sheet.addCell(new Label(columnIndex++, row, "100"));//价值默认100
						sheet.addCell(new Label(columnIndex++, row, "MEITAO EXPRESS MAIL"));//发货人公司
						sheet.addCell(new Label(columnIndex++, row, "357 E GARVEY AVE,MONTEREY PARK CA91755"));//发货人地址
						sheet.addCell(new Label(columnIndex++, row, ""));//发货人公司
						String address="";
						
						if(!StringUtil.isEmpty(order.getR_privice()))
						{
							address=address+order.getR_privice();
						}
						if(!StringUtil.isEmpty(order.getR_city()))
						{
							address=address+order.getR_city();
						}
						if(!StringUtil.isEmpty(order.getR_dist()))
						{
							address=address+order.getR_dist();
						}
						if(!StringUtil.isEmpty(order.getR_address()))
						{
							address=address+order.getR_address();
						}
						
						sheet.addCell(new Label(columnIndex++, row, address));//收货人地址
						if(order.isResultflag())
						{
							sheet.addCell(new Label(columnIndex++, row, order.getCardname()));//收货人姓名
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, order.getR_name()));//收货人姓名
						}
						sheet.addCell(new Label(columnIndex++, row, order.getR_phone()));//收货人电话
						sheet.addCell(new Label(columnIndex++, row, ""));//箱号
						sheet.addCell(new Label(columnIndex++, row, order.getCardid()));//身份证号
						if(order.isResultflag())
						{
							sheet.addCell(new Label(columnIndex++, row, order.getResult()));//导出结果
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, order.getResult(),redcolor));
						}
						
						
						

						row++;
					}
					
					if((!zipurl.equalsIgnoreCase("")))
					{
						WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
			                    10, WritableFont.NO_BOLD);// 字体样式
						WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
						oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
						sheet.addCell(new Label(0, row, zipurl,oceanbluecolor));//显示压缩包路径
						
					}
				}
			}, templetFile, orders, os);
		} else {
			// 模板复制
			ExcelUtil.exportExcel(new ExcelWrite<import_t_orders>() {
				@Override
				public void write(Map<String, String> headers,
						Collection<import_t_orders> datas, WritableSheet sheet)
						throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有导入数据.....");
						sheet.addCell(cell);
						return;
					}
					
					int row = 2;

					String zipurl="";
					for (import_t_orders order : datas) {
						if (order == null) {
							continue;
						}
						if(!StringUtil.isEmpty(order.getZipurl())&&(zipurl.equalsIgnoreCase("")))
						{
							zipurl=order.getZipurl();
						}
						WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
			                    10, WritableFont.NO_BOLD);// 字体样式
						
						WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
						redcolor.setBackground(Colour.RED);
						
						WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
						oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
						int columnIndex = 0;
						sheet.addCell(new Label(columnIndex++, row, String.valueOf(row-1)));//序号
						sheet.addCell(new Label(columnIndex++, row, order.getOrderid()));//运单号
						sheet.addCell(new Label(columnIndex++, row, ""));//分单号
						
						
						if(!StringUtil.isEmpty(order.getCommudityslist()))
						{
							
							String commundity1=order.getCommudityslist();
							commundity1=commundity1.replace("，", ";");
							commundity1=commundity1.replace(",", ";");
							commundity1=commundity1.replace("；", ";");//把其它字符换成统一的，方便区分
							
							String[] comm=commundity1.split(";");
							int flag=0;
							String taxcomm="";//海关品名
							String brand="";
							for(int i=0;i<comm.length;i++)
							{
								
								if(!StringUtil.isEmpty(comm[i]))
								{
									String[] name1=comm[i].split(":");
									
									/*if(!StringUtil.isEmpty((name1[0])))
									{
										name1[0]=name1[0].substring(0, 5);
										name1[0]=name1[0]+"*1";
									}*/
									if(brand.equalsIgnoreCase(""))
									{
										//brand=name1[0].substring(0,5);
										String[] name2=name1[0].split("\\*");
										if(name2[0].length()>=5)
										{
											
											brand=name2[0].substring(0,5);
											
										}
										else
										{
											brand=name2[0];
										}
									}
									else
									{
										String[] name2=name1[0].split("\\*");
										//brand=name1[0].substring(0,5);
										if(name2[0].length()>=5)
										{
											brand=brand+","+name2[0].substring(0,5);
										}
										else
										{
											brand=brand+","+name2[0];
										}
									}
									//name1[0]=name1[0]+"*1";
									if(flag!=0)
									{
										taxcomm=taxcomm+","+name1[0];
									}
									else
									{
										taxcomm=name1[0];
									}

									flag++;	
								}
								
							}
							sheet.addCell(new Label(columnIndex++, row, taxcomm));//报关品名
							sheet.addCell(new Label(columnIndex++, row, order.getCommudityslist()));//品名
							sheet.addCell(new Label(columnIndex++, row, brand));//品牌
							sheet.addCell(new Label(columnIndex++, row, ""));//规格
							sheet.addCell(new Label(columnIndex++, row, String.valueOf(flag)));//数量
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, ""));//报关品名
							sheet.addCell(new Label(columnIndex++, row, ""));//品名
							sheet.addCell(new Label(columnIndex++, row, ""));//品牌
							sheet.addCell(new Label(columnIndex++, row, ""));//规格
							sheet.addCell(new Label(columnIndex++, row, ""));//数量
						}
						
						try{
							double weight=Double.parseDouble(order.getWeight());
							weight=weight*0.4536;
							
							  BigDecimal   bd   =   new   BigDecimal(weight);   
							  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
							  sheet.addCell(new Label(columnIndex++, row, String.valueOf(bd)));//千克
						}catch(Exception e)
						{
							sheet.addCell(new Label(columnIndex++, row, ""));//数字发生异常
						}
						sheet.addCell(new Label(columnIndex++, row, "100"));//价值默认100
						sheet.addCell(new Label(columnIndex++, row, "MEITAO EXPRESS MAIL"));//发货人公司
						sheet.addCell(new Label(columnIndex++, row, "357 E GARVEY AVE,MONTEREY PARK CA91755"));//发货人地址
						sheet.addCell(new Label(columnIndex++, row, ""));//发货人公司
						String address="";
						
						if(!StringUtil.isEmpty(order.getR_privice()))
						{
							address=address+order.getR_privice();
						}
						if(!StringUtil.isEmpty(order.getR_city()))
						{
							address=address+order.getR_city();
						}
						if(!StringUtil.isEmpty(order.getR_dist()))
						{
							address=address+order.getR_dist();
						}
						if(!StringUtil.isEmpty(order.getR_address()))
						{
							address=address+order.getR_address();
						}
						
						sheet.addCell(new Label(columnIndex++, row, address));//收货人地址
						if(order.isResultflag())
						{
							sheet.addCell(new Label(columnIndex++, row, order.getCardname()));//收货人姓名
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, order.getR_name()));//收货人姓名
						}
						sheet.addCell(new Label(columnIndex++, row, order.getR_phone()));//收货人电话
						sheet.addCell(new Label(columnIndex++, row, ""));//箱号
						sheet.addCell(new Label(columnIndex++, row, order.getCardid()));//身份证号
						if(order.isResultflag())
						{
							sheet.addCell(new Label(columnIndex++, row, order.getResult()));//导出结果
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, order.getResult(),redcolor));
						}
						
						
						

						row++;
					}
					
					if((!zipurl.equalsIgnoreCase("")))
					{
						WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
			                    10, WritableFont.NO_BOLD);// 字体样式
						WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
						oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
						sheet.addCell(new Label(0, row, zipurl,oceanbluecolor));//显示压缩包路径
						
					}
				}
			}, templetFile, orders, os);
		}
		
		
		
	}
	
	// 导出新的内蒙古核对模板 kai 20160316
	public static void exportneimenggumode(List<import_t_orders> orders,
			File templetFile, OutputStream os) throws Exception {
		if (templetFile != null && templetFile.exists()) {
			// 模板复制
			ExcelUtil.exportExcel(new ExcelWrite<import_t_orders>() {
				@Override
				public void write(Map<String, String> headers,
						Collection<import_t_orders> datas, WritableSheet sheet)
						throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有导入数据.....");
						sheet.addCell(cell);
						return;
					}
					String zipurl="";
					int row = 4;

					
					for (import_t_orders order : datas) {
						if (order == null) {
							continue;
						}
						
						if(!StringUtil.isEmpty(order.getZipurl())&&(zipurl.equalsIgnoreCase("")))
						{
							zipurl=order.getZipurl();
						}
						
						WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
			                    10, WritableFont.NO_BOLD);// 字体样式
						
						WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
						redcolor.setBackground(Colour.RED);
						
						WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
						oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
						
						if(order.isResultflag())
						{
							sheet.addCell(new Label(25, row, order.getResult()));//导出结果
						}
						else
						{
							sheet.addCell(new Label(25, row, order.getResult(),redcolor));
						}
						
						
						int columnIndex = 0;
						sheet.addCell(new Label(columnIndex++, row, ""));//海关单号
						sheet.addCell(new Label(columnIndex++, row, order.getOrderid()));//运单号
						
						
						int addindex=0;
						if(!StringUtil.isEmpty(order.getCommudityslist()))
						{
							String commundity1=order.getCommudityslist();
							commundity1=commundity1.replace("，", ";");
							commundity1=commundity1.replace(",", ";");
							commundity1=commundity1.replace("；", ";");//把其它字符换成统一的，方便区分
							
							String[] comm=commundity1.split(";");
							
							String taxcomm="";//海关品名
							String brand="";
							addindex=comm.length;
							int colindex=columnIndex;
							int flag=0;
							for(int i=0;i<comm.length;i++)
							{
								columnIndex=colindex;
								int number=1;
								if(!StringUtil.isEmpty(comm[i]))
								{
									String[] name1=comm[i].split(":");
									
									
									
									/*if(!StringUtil.isEmpty((name1[0])))
									{
										name1[0]=name1[0].substring(0, 5);
										name1[0]=name1[0]+"*1";
									}*/
									/*if(brand.equalsIgnoreCase(""))
									{
										//brand=name1[0].substring(0,5);
										if(name1[0].length()>=5)
										{
											brand=name1[0].substring(0,5);
										}
										else
										{
											brand=name1[0];
										}
									}*/
									//name1[0]=name1[0]+"*1";
									taxcomm=name1[0];
									
									String[] name2=name1[0].split("\\*");
									if((name2.length>1)&&(!StringUtil.isEmpty(name2[1])))
									{
										try{
											
											number=Integer.parseInt(name2[1].trim()); 
											
										}
										catch(Exception e)
										{
											number=1;
										}
									}
									
								/*	if(flag!=0)
									{
										taxcomm=","+name1[0];
									}
									else
									{
										taxcomm=name1[0];
									}*/

									flag++;	
								}
								
							
								
								sheet.addCell(new Label(columnIndex++, row+i, taxcomm));//物品名称
								sheet.addCell(new Label(columnIndex++, row+i, "100"));//申报单价
								
								sheet.addCell(new Label(columnIndex++, row+i, String.valueOf(number)));//数量
								sheet.addCell(new Label(columnIndex++, row+i, "100"));//申报实价
							}
							
							
							
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, ""));//物品名称
							sheet.addCell(new Label(columnIndex++, row, ""));//申报单价
							sheet.addCell(new Label(columnIndex++, row, ""));//数量
							sheet.addCell(new Label(columnIndex++, row, "100"));//申报实价
						}
						
						try{
							double weight=Double.parseDouble(order.getWeight());
							weight=weight*0.4536;
							
							  BigDecimal   bd   =   new   BigDecimal(weight);   
							  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
							  sheet.addCell(new Label(columnIndex++, row, String.valueOf(bd)));//千克
						}catch(Exception e)
						{
							sheet.addCell(new Label(columnIndex++, row, ""));//数字发生异常
						}
						
						
						sheet.addCell(new Label(columnIndex++, row, ""));//发货人姓名
						if(order.isResultflag())
						{
							sheet.addCell(new Label(columnIndex++, row, order.getCardname()));//收货人姓名
							sheet.addCell(new Label(columnIndex++, row, order.getCardid()));//收货人身份证号
							
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, order.getR_name()));//收货人姓名
							sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
						}
						
						
						String address="";
						
						if(!StringUtil.isEmpty(order.getR_privice()))
						{
							address=address+order.getR_privice();
						}
						if(!StringUtil.isEmpty(order.getR_city()))
						{
							address=address+order.getR_city();
						}
						if(!StringUtil.isEmpty(order.getR_dist()))
						{
							address=address+order.getR_dist();
						}
						if(!StringUtil.isEmpty(order.getR_address()))
						{
							address=address+order.getR_address();
						}
						
						sheet.addCell(new Label(columnIndex++, row, address));//收货人地址
						
						sheet.addCell(new Label(columnIndex++, row, order.getR_phone()));//收货人电话
						sheet.addCell(new Label(columnIndex++, row, order.getR_zipcode()));//收货人邮编
						sheet.addCell(new Label(columnIndex++, row, order.getR_city()));//收货人城市
						
						
						
						
						

						row=addindex+row;
					}
					if((!zipurl.equalsIgnoreCase("")))
					{
						WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
			                    10, WritableFont.NO_BOLD);// 字体样式
						WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
						oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
						sheet.addCell(new Label(0, row, zipurl,oceanbluecolor));//显示压缩包路径
						
					}
					
				}
			}, templetFile, orders, os);
		} else {
			// 模板复制
			ExcelUtil.exportExcel(new ExcelWrite<import_t_orders>() {
				@Override
				public void write(Map<String, String> headers,
						Collection<import_t_orders> datas, WritableSheet sheet)
						throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有导入数据.....");
						sheet.addCell(cell);
						return;
					}
					String zipurl="";
					int row = 4;

					
					for (import_t_orders order : datas) {
						if (order == null) {
							continue;
						}
						
						if(!StringUtil.isEmpty(order.getZipurl())&&(zipurl.equalsIgnoreCase("")))
						{
							zipurl=order.getZipurl();
						}
						
						WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
			                    10, WritableFont.NO_BOLD);// 字体样式
						
						WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
						redcolor.setBackground(Colour.RED);
						
						WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
						oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
						
						if(order.isResultflag())
						{
							sheet.addCell(new Label(25, row, order.getResult()));//导出结果
						}
						else
						{
							sheet.addCell(new Label(25, row, order.getResult(),redcolor));
						}
						
						
						int columnIndex = 0;
						sheet.addCell(new Label(columnIndex++, row, ""));//海关单号
						sheet.addCell(new Label(columnIndex++, row, order.getOrderid()));//运单号
						
						
						int addindex=0;
						if(!StringUtil.isEmpty(order.getCommudityslist()))
						{
							String commundity1=order.getCommudityslist();
							commundity1=commundity1.replace("，", ";");
							commundity1=commundity1.replace(",", ";");
							commundity1=commundity1.replace("；", ";");//把其它字符换成统一的，方便区分
							
							String[] comm=commundity1.split(";");
							
							String taxcomm="";//海关品名
							String brand="";
							addindex=comm.length;
							int colindex=columnIndex;
							int flag=0;
							for(int i=0;i<comm.length;i++)
							{
								columnIndex=colindex;
								int number=1;
								if(!StringUtil.isEmpty(comm[i]))
								{
									String[] name1=comm[i].split(":");
									
									
									
									/*if(!StringUtil.isEmpty((name1[0])))
									{
										name1[0]=name1[0].substring(0, 5);
										name1[0]=name1[0]+"*1";
									}*/
									/*if(brand.equalsIgnoreCase(""))
									{
										//brand=name1[0].substring(0,5);
										if(name1[0].length()>=5)
										{
											brand=name1[0].substring(0,5);
										}
										else
										{
											brand=name1[0];
										}
									}*/
									//name1[0]=name1[0]+"*1";
									taxcomm=name1[0];
									
									String[] name2=name1[0].split("\\*");
									if((name2.length>1)&&(!StringUtil.isEmpty(name2[1])))
									{
										try{
											
											number=Integer.parseInt(name2[1].trim()); 
											
										}
										catch(Exception e)
										{
											number=1;
										}
									}
									
								/*	if(flag!=0)
									{
										taxcomm=","+name1[0];
									}
									else
									{
										taxcomm=name1[0];
									}*/

									flag++;	
								}
								
							
								
								sheet.addCell(new Label(columnIndex++, row+i, taxcomm));//物品名称
								sheet.addCell(new Label(columnIndex++, row+i, "100"));//申报单价
								
								sheet.addCell(new Label(columnIndex++, row+i, String.valueOf(number)));//数量
								sheet.addCell(new Label(columnIndex++, row+i, "100"));//申报实价
							}
							
							
							
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, ""));//物品名称
							sheet.addCell(new Label(columnIndex++, row, ""));//申报单价
							sheet.addCell(new Label(columnIndex++, row, ""));//数量
							sheet.addCell(new Label(columnIndex++, row, "100"));//申报实价
						}
						
						try{
							double weight=Double.parseDouble(order.getWeight());
							weight=weight*0.4536;
							
							  BigDecimal   bd   =   new   BigDecimal(weight);   
							  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
							  sheet.addCell(new Label(columnIndex++, row, String.valueOf(bd)));//千克
						}catch(Exception e)
						{
							sheet.addCell(new Label(columnIndex++, row, ""));//数字发生异常
						}
						
						
						sheet.addCell(new Label(columnIndex++, row, ""));//发货人姓名
						if(order.isResultflag())
						{
							sheet.addCell(new Label(columnIndex++, row, order.getCardname()));//收货人姓名
							sheet.addCell(new Label(columnIndex++, row, order.getCardid()));//收货人身份证号
							
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, order.getR_name()));//收货人姓名
							sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
						}
						
						
						String address="";
						
						if(!StringUtil.isEmpty(order.getR_privice()))
						{
							address=address+order.getR_privice();
						}
						if(!StringUtil.isEmpty(order.getR_city()))
						{
							address=address+order.getR_city();
						}
						if(!StringUtil.isEmpty(order.getR_dist()))
						{
							address=address+order.getR_dist();
						}
						if(!StringUtil.isEmpty(order.getR_address()))
						{
							address=address+order.getR_address();
						}
						
						sheet.addCell(new Label(columnIndex++, row, address));//收货人地址
						
						sheet.addCell(new Label(columnIndex++, row, order.getR_phone()));//收货人电话
						sheet.addCell(new Label(columnIndex++, row, order.getR_zipcode()));//收货人邮编
						sheet.addCell(new Label(columnIndex++, row, order.getR_city()));//收货人城市
						
						
						
						
						

						row=addindex+row;
					}
					if((!zipurl.equalsIgnoreCase("")))
					{
						WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
			                    10, WritableFont.NO_BOLD);// 字体样式
						WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
						oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
						sheet.addCell(new Label(0, row, zipurl,oceanbluecolor));//显示压缩包路径
						
					}
					
				}
			}, templetFile, orders, os);
		}
		
		
		
	}
	
	// 导出深圳的核对模板
	public static void exportshenzhen(List<import_t_orders> orders,
			File templetFile, OutputStream os) throws Exception {
		if (templetFile != null && templetFile.exists()) {
			// 模板复制
			ExcelUtil.exportExcel(new ExcelWrite<import_t_orders>() {
				@Override
				public void write(Map<String, String> headers,
						Collection<import_t_orders> datas, WritableSheet sheet)
						throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有导入数据.....");
						sheet.addCell(cell);
						return;
					}
					
					int row = 2;

					
					for (import_t_orders order : datas) {
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
						sheet.addCell(new Label(columnIndex++, row, String.valueOf(row-1)));//序号
						sheet.addCell(new Label(columnIndex++, row, ""));//分单号
						sheet.addCell(new Label(columnIndex++, row, order.getOrderid()));//运单号
						
						
						
						if(!StringUtil.isEmpty(order.getCommudityslist()))
						{
							
							String commundity1=order.getCommudityslist();
							commundity1=commundity1.replace("，", ";");
							commundity1=commundity1.replace(",", ";");
							commundity1=commundity1.replace("；", ";");//把其它字符换成统一的，方便区分
							String[] comm=commundity1.split(";");
							int flag=0;
							String taxcomm="";//海关品名
							String brand="";
							int number=0;
							for(int i=0;i<comm.length;i++)
							{
								
								if(!StringUtil.isEmpty(comm[i]))
								{
									String[] name1=comm[i].split(":");
									/*if(!StringUtil.isEmpty((name1[0])))
									{
										name1[0]=name1[0].substring(0, 5);
										name1[0]=name1[0]+"*1";
									}*/
									if(brand.equalsIgnoreCase(""))
									{
										//brand=name1[0].substring(0,5);
										String[] name2=name1[0].split("\\*");
										
										try{
											number+=Integer.parseInt(name2[1]);
										}catch(Exception e){
											number+=1;
										}
										if(name2[0].length()>=5)
										{
											brand=name2[0].substring(0,5);
										}
										else
										{
											brand=name2[0];
										}
									}
									else
									{
										String[] name2=name1[0].split("\\*");
										try{
											number+=Integer.parseInt(name2[1]);
										}catch(Exception e){
											number+=1;
										}
										if(name2[0].length()>=5)
										{
											brand=brand+","+name2[0].substring(0,5);
										}
										else
										{
											brand=brand+","+name2[0];
										}
									}
									//name1[0]=name1[0]+"*1";
									if(flag!=0)
									{
										taxcomm=taxcomm+","+name1[0];
									}
									else
									{
										taxcomm=name1[0];
									}

									flag++;	
								}
								
							}
							String[] temp=taxcomm.split(",");//此模板只取第一个品名
							sheet.addCell(new Label(columnIndex++, row, temp[0]));//报关品名
							sheet.addCell(new Label(columnIndex++, row, order.getCommudityslist()));//详细品名
							String[] temp1=brand.split(",");//此模板只取第一个品牌
							sheet.addCell(new Label(columnIndex++, row, temp1[0]));//品牌
							sheet.addCell(new Label(columnIndex++, row, ""));//规格
							//sheet.addCell(new Label(columnIndex++, row, String.valueOf(flag)));//数量
							sheet.addCell(new Label(columnIndex++, row, String.valueOf(number)));//数量
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, ""));//报关品名
							sheet.addCell(new Label(columnIndex++, row, ""));//品名
							sheet.addCell(new Label(columnIndex++, row, ""));//品牌
							sheet.addCell(new Label(columnIndex++, row, ""));//规格
							sheet.addCell(new Label(columnIndex++, row, ""));//数量
						}
						
						try{
							double weight=Double.parseDouble(order.getWeight());
							weight=weight*0.4536;
							
							  BigDecimal   bd   =   new   BigDecimal(weight);   
							  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
							  sheet.addCell(new Label(columnIndex++, row, String.valueOf(bd)));//千克
						}catch(Exception e)
						{
							sheet.addCell(new Label(columnIndex++, row, ""));//数字发生异常
						}
						sheet.addCell(new Label(columnIndex++, row, "100"));//价值默认100
						sheet.addCell(new Label(columnIndex++, row, ""));//虚拟发货人公司
						sheet.addCell(new Label(columnIndex++, row, ""));//虚拟发货人地址
						sheet.addCell(new Label(columnIndex++, row, ""));//虚拟收货人公司
				
						String address="";
						
						if(!StringUtil.isEmpty(order.getR_privice()))
						{
							address=address+order.getR_privice();
						}
						if(!StringUtil.isEmpty(order.getR_city()))
						{
							address=address+order.getR_city();
						}
						if(!StringUtil.isEmpty(order.getR_dist()))
						{
							address=address+order.getR_dist();
						}
						if(!StringUtil.isEmpty(order.getR_address()))
						{
							address=address+order.getR_address();
						}
						
						sheet.addCell(new Label(columnIndex++, row, address));//收货人地址
						sheet.addCell(new Label(columnIndex++, row, order.getR_name()));//收货人姓名
						sheet.addCell(new Label(columnIndex++, row, order.getR_phone()));//收货人电话
						sheet.addCell(new Label(columnIndex++, row, ""));//箱号
						
						
						
						

						row++;
					}
				}
			}, templetFile, orders, os);
		} else {
			// 模板复制
			ExcelUtil.exportExcel(new ExcelWrite<import_t_orders>() {
				@Override
				public void write(Map<String, String> headers,
						Collection<import_t_orders> datas, WritableSheet sheet)
						throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有导入数据.....");
						sheet.addCell(cell);
						return;
					}
					
					int row = 2;

					
					for (import_t_orders order : datas) {
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
						sheet.addCell(new Label(columnIndex++, row, String.valueOf(row-1)));//序号
						sheet.addCell(new Label(columnIndex++, row, ""));//分单号
						sheet.addCell(new Label(columnIndex++, row, order.getOrderid()));//运单号
						
						
						
						if(!StringUtil.isEmpty(order.getCommudityslist()))
						{
							
							String commundity1=order.getCommudityslist();
							commundity1=commundity1.replace("，", ";");
							commundity1=commundity1.replace(",", ";");
							commundity1=commundity1.replace("；", ";");//把其它字符换成统一的，方便区分
							String[] comm=commundity1.split(";");
							int flag=0;
							String taxcomm="";//海关品名
							String brand="";
							for(int i=0;i<comm.length;i++)
							{
								
								if(!StringUtil.isEmpty(comm[i]))
								{
									String[] name1=comm[i].split(":");
									/*if(!StringUtil.isEmpty((name1[0])))
									{
										name1[0]=name1[0].substring(0, 5);
										name1[0]=name1[0]+"*1";
									}*/
									if(brand.equalsIgnoreCase(""))
									{
										//brand=name1[0].substring(0,5);
										String[] name2=name1[0].split("\\*");
										if(name2[0].length()>=5)
										{
											brand=name2[0].substring(0,5);
										}
										else
										{
											brand=name2[0];
										}
									}
									else
									{
										String[] name2=name1[0].split("\\*");
										if(name2[0].length()>=5)
										{
											brand=brand+","+name2[0].substring(0,5);
										}
										else
										{
											brand=brand+","+name2[0];
										}
									}
									//name1[0]=name1[0]+"*1";
									if(flag!=0)
									{
										taxcomm=taxcomm+","+name1[0];
									}
									else
									{
										taxcomm=name1[0];
									}

									flag++;	
								}
								
							}
							sheet.addCell(new Label(columnIndex++, row, taxcomm));//报关品名
							sheet.addCell(new Label(columnIndex++, row, order.getCommudityslist()));//详细品名
							sheet.addCell(new Label(columnIndex++, row, brand));//品牌
							sheet.addCell(new Label(columnIndex++, row, ""));//规格
							sheet.addCell(new Label(columnIndex++, row, String.valueOf(flag)));//数量
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, ""));//报关品名
							sheet.addCell(new Label(columnIndex++, row, ""));//品名
							sheet.addCell(new Label(columnIndex++, row, ""));//品牌
							sheet.addCell(new Label(columnIndex++, row, ""));//规格
							sheet.addCell(new Label(columnIndex++, row, ""));//数量
						}
						
						try{
							double weight=Double.parseDouble(order.getWeight());
							weight=weight*0.4536;
							
							  BigDecimal   bd   =   new   BigDecimal(weight);   
							  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
							  sheet.addCell(new Label(columnIndex++, row, String.valueOf(bd)));//千克
						}catch(Exception e)
						{
							sheet.addCell(new Label(columnIndex++, row, ""));//数字发生异常
						}
						sheet.addCell(new Label(columnIndex++, row, "100"));//价值默认100
						sheet.addCell(new Label(columnIndex++, row, ""));//虚拟发货人公司
						sheet.addCell(new Label(columnIndex++, row, ""));//虚拟发货人地址
						sheet.addCell(new Label(columnIndex++, row, ""));//虚拟收货人公司
				
						String address="";
						
						if(!StringUtil.isEmpty(order.getR_privice()))
						{
							address=address+order.getR_privice();
						}
						if(!StringUtil.isEmpty(order.getR_city()))
						{
							address=address+order.getR_city();
						}
						if(!StringUtil.isEmpty(order.getR_dist()))
						{
							address=address+order.getR_dist();
						}
						if(!StringUtil.isEmpty(order.getR_address()))
						{
							address=address+order.getR_address();
						}
						
						sheet.addCell(new Label(columnIndex++, row, address));//收货人地址
						sheet.addCell(new Label(columnIndex++, row, order.getR_name()));//收货人姓名
						sheet.addCell(new Label(columnIndex++, row, order.getR_phone()));//收货人电话
						sheet.addCell(new Label(columnIndex++, row, ""));//箱号
						
						
						
						

						row++;
					}
				}
			}, templetFile, orders, os);
		}
		
		
		
	}
	
	
	
	public static void exportshanghaimode(List<import_t_orders> orders,
			File templetFile, OutputStream os) throws Exception {
		if (templetFile != null && templetFile.exists()) {
			// 模板复制
			ExcelUtil.exportExcel(new ExcelWrite<import_t_orders>() {
				@Override
				public void write(Map<String, String> headers,
						Collection<import_t_orders> datas, WritableSheet sheet)
						throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有导入数据.....");
						sheet.addCell(cell);
						return;
					}
					String zipurl="";
					int row = 4;

					int serno=1;
					for (import_t_orders order : datas) {
						if (order == null) {
							continue;
						}
						
						if(!StringUtil.isEmpty(order.getZipurl())&&(zipurl.equalsIgnoreCase("")))
						{
							zipurl=order.getZipurl();
						}
						
						WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
			                    10, WritableFont.NO_BOLD);// 字体样式
						
						WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
						redcolor.setBackground(Colour.RED);
						
						WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
						oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
						
						if(order.isResultflag())
						{
							sheet.addCell(new Label(29, row, order.getResult()));//导出结果
						}
						else
						{
							sheet.addCell(new Label(29, row, order.getResult(),redcolor));
						}
						
						
						int columnIndex = 0;
						sheet.addCell(new Label(columnIndex++, row, String.valueOf(serno++)));//序号
						sheet.addCell(new Label(columnIndex++, row, ""));//货号条码
						sheet.addCell(new Label(columnIndex++, row, order.getOrderid()));//运单号
						sheet.addCell(new Label(columnIndex++, row, ""));//行邮税号
						
						int addindex=0;
						int num=0;
						
						if(!StringUtil.isEmpty(order.getCommudityslist()))
						{
							String commundity1=order.getCommudityslist();
							commundity1=commundity1.replace("，", ";");
							commundity1=commundity1.replace(",", ";");
							commundity1=commundity1.replace("；", ";");//把其它字符换成统一的，方便区分
							
							String[] comm=commundity1.split(";");
							int flag=0;
							String taxcomm="";//海关品名
							String brand="";
							addindex=comm.length;
							int colindex=columnIndex;
							int number=0;
							for(int i=0;i<comm.length;i++)
							{
								columnIndex=colindex;
								if(!StringUtil.isEmpty(comm[i]))
								{
									String[] name1=comm[i].split(":");
									
									
									
									/*if(!StringUtil.isEmpty((name1[0])))
									{
										name1[0]=name1[0].substring(0, 5);
										name1[0]=name1[0]+"*1";
									}*/
									//if(brand.equalsIgnoreCase(""))
									//{
										//brand=name1[0].substring(0,5);
									String[] name2=name1[0].split("\\*");
									
									try{
										number+=Integer.parseInt(name2[1]);
									}catch(Exception e){
										number+=1;
									}
									
										if(name2[0].length()>=5)
										{
											brand=name2[0].substring(0,5);
										}
										else
										{
											brand=name2[0];
										}
									//}
									//name1[0]=name1[0]+"*1";
									taxcomm=name1[0];
								/*	if(flag!=0)
									{
										taxcomm=","+name1[0];
									}
									else
									{
										taxcomm=name1[0];
									}*/

									flag++;	
								}
								
							
								
								sheet.addCell(new Label(columnIndex++, row+i, taxcomm));//商品名称
								sheet.addCell(new Label(columnIndex++, row+i, brand));//品牌
								sheet.addCell(new Label(columnIndex++, row+i,""));//规格型号
								
								//sheet.addCell(new Label(columnIndex++, row+i, "1"));//数量
								sheet.addCell(new Label(columnIndex++, row+i, String.valueOf(number)));//数量
								num=flag;
								number=0;
							}
							
							
							
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, ""));//物品名称
							sheet.addCell(new Label(columnIndex++, row, ""));//品牌
							sheet.addCell(new Label(columnIndex++, row, ""));//规格型号
							sheet.addCell(new Label(columnIndex++, row, ""));//数量
						}
						sheet.addCell(new Label(columnIndex++, row, "个"));//单位
						sheet.addCell(new Label(columnIndex++, row, "100"));//单价
						sheet.addCell(new Label(columnIndex++, row, "100"));//总价值
						
						if(StringUtil.isEmpty(order.getBoxq()))
						{
							sheet.addCell(new Label(columnIndex++, row, order.getBoxq()));//件数
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, "1"));//件数
						}
						
						
						try{
							double weight=Double.parseDouble(order.getWeight());
							weight=weight*0.4536;
							
							  BigDecimal   bd   =   new   BigDecimal(weight);   
							  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
							  sheet.addCell(new Label(columnIndex++, row, String.valueOf(bd)));//，净重，千克
						}catch(Exception e)
						{
							sheet.addCell(new Label(columnIndex++, row, ""));//净重
						}
						sheet.addCell(new Label(columnIndex++, row, ""));//毛重
						
					
						
						if(order.isResultflag())
						{
							sheet.addCell(new Label(columnIndex++, row, order.getCardname()));//收货人姓名
							sheet.addCell(new Label(columnIndex++, row, order.getCardid()));//收货人身份证号
							
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, order.getR_name()));//收货人姓名
							sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
						}
						
						
						String address="";
						
						if(!StringUtil.isEmpty(order.getR_privice()))
						{
							address=address+order.getR_privice();
						}
						if(!StringUtil.isEmpty(order.getR_city()))
						{
							address=address+order.getR_city();
						}
						if(!StringUtil.isEmpty(order.getR_dist()))
						{
							address=address+order.getR_dist();
						}
						if(!StringUtil.isEmpty(order.getR_address()))
						{
							address=address+order.getR_address();
						}
						
						sheet.addCell(new Label(columnIndex++, row, address));//收货人地址
						
						sheet.addCell(new Label(columnIndex++, row, order.getR_phone()));//收货人电话
						
						sheet.addCell(new Label(columnIndex++, row, "MEITAO EXPRESS MAIL"));//发货人名称
						sheet.addCell(new Label(columnIndex++, row, ""));//订单号
						sheet.addCell(new Label(columnIndex++, row, ""));//备注
						sheet.addCell(new Label(columnIndex++, row, "357 E GARVEY AVE,MONTEREY PARK CA91755"));//发货人地址
						
						
						sheet.addCell(new Label(columnIndex++, row, order.getCommudityslist()));//商品描述
						
						try{
							
							  sheet.addCell(new Label(columnIndex++, row, String.valueOf(100*addindex)));//总运费
						}catch(Exception e)
						{
							sheet.addCell(new Label(columnIndex++, row, ""));//总运费
						}
						sheet.addCell(new Label(columnIndex++, row, ""));//支付编号
						
						
						
						

						row=addindex+row;
					}
					if((!zipurl.equalsIgnoreCase("")))
					{
						WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
			                    10, WritableFont.NO_BOLD);// 字体样式
						WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
						oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
						sheet.addCell(new Label(0, row, zipurl,oceanbluecolor));//显示压缩包路径
						
					}
					
				}
			}, templetFile, orders, os);
		} else {
			// 模板复制
			ExcelUtil.exportExcel(new ExcelWrite<import_t_orders>() {
				@Override
				public void write(Map<String, String> headers,
						Collection<import_t_orders> datas, WritableSheet sheet)
						throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有导入数据.....");
						sheet.addCell(cell);
						return;
					}
					String zipurl="";
					int row = 4;

					int serno=1;
					for (import_t_orders order : datas) {
						if (order == null) {
							continue;
						}
						
						if(!StringUtil.isEmpty(order.getZipurl())&&(zipurl.equalsIgnoreCase("")))
						{
							zipurl=order.getZipurl();
						}
						
						WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
			                    10, WritableFont.NO_BOLD);// 字体样式
						
						WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
						redcolor.setBackground(Colour.RED);
						
						WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
						oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
						
						if(order.isResultflag())
						{
							sheet.addCell(new Label(29, row, order.getResult()));//导出结果
						}
						else
						{
							sheet.addCell(new Label(29, row, order.getResult(),redcolor));
						}
						
						
						int columnIndex = 0;
						sheet.addCell(new Label(columnIndex++, row, String.valueOf(serno++)));//序号
						sheet.addCell(new Label(columnIndex++, row, ""));//货号条码
						sheet.addCell(new Label(columnIndex++, row, order.getOrderid()));//运单号
						sheet.addCell(new Label(columnIndex++, row, ""));//行邮税号
						
						int addindex=0;
						int num=0;
						
						if(!StringUtil.isEmpty(order.getCommudityslist()))
						{
							String commundity1=order.getCommudityslist();
							commundity1=commundity1.replace("，", ";");
							commundity1=commundity1.replace(",", ";");
							commundity1=commundity1.replace("；", ";");//把其它字符换成统一的，方便区分
							
							String[] comm=commundity1.split(";");
							int flag=0;
							String taxcomm="";//海关品名
							String brand="";
							addindex=comm.length;
							int colindex=columnIndex;
							
							for(int i=0;i<comm.length;i++)
							{
								columnIndex=colindex;
								if(!StringUtil.isEmpty(comm[i]))
								{
									String[] name1=comm[i].split(":");
									
									
									
									/*if(!StringUtil.isEmpty((name1[0])))
									{
										name1[0]=name1[0].substring(0, 5);
										name1[0]=name1[0]+"*1";
									}*/
									//if(brand.equalsIgnoreCase(""))
									//{
										//brand=name1[0].substring(0,5);
									String[] name2=name1[0].split("\\*");
										if(name2[0].length()>=5)
										{
											brand=name2[0].substring(0,5);
										}
										else
										{
											brand=name2[0];
										}
									//}
									//name1[0]=name1[0]+"*1";
									taxcomm=name1[0];
								/*	if(flag!=0)
									{
										taxcomm=","+name1[0];
									}
									else
									{
										taxcomm=name1[0];
									}*/

									flag++;	
								}
								
							
								
								sheet.addCell(new Label(columnIndex++, row+i, taxcomm));//商品名称
								sheet.addCell(new Label(columnIndex++, row+i, brand));//品牌
								sheet.addCell(new Label(columnIndex++, row+i,""));//规格型号
								
								sheet.addCell(new Label(columnIndex++, row+i, "1"));//数量
								num=flag;
							}
							
							
							
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, ""));//物品名称
							sheet.addCell(new Label(columnIndex++, row, ""));//品牌
							sheet.addCell(new Label(columnIndex++, row, ""));//规格型号
							sheet.addCell(new Label(columnIndex++, row, ""));//数量
						}
						sheet.addCell(new Label(columnIndex++, row, "个"));//单位
						sheet.addCell(new Label(columnIndex++, row, "100"));//单价
						sheet.addCell(new Label(columnIndex++, row, "100"));//总价值
						
						if(StringUtil.isEmpty(order.getBoxq()))
						{
							sheet.addCell(new Label(columnIndex++, row, order.getBoxq()));//件数
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, "1"));//件数
						}
						
						
						try{
							double weight=Double.parseDouble(order.getWeight());
							weight=weight*0.4536;
							
							  BigDecimal   bd   =   new   BigDecimal(weight);   
							  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);//取二位小数   
							  sheet.addCell(new Label(columnIndex++, row, String.valueOf(bd)));//，净重，千克
						}catch(Exception e)
						{
							sheet.addCell(new Label(columnIndex++, row, ""));//净重
						}
						sheet.addCell(new Label(columnIndex++, row, ""));//毛重
						
					
						
						if(order.isResultflag())
						{
							sheet.addCell(new Label(columnIndex++, row, order.getCardname()));//收货人姓名
							sheet.addCell(new Label(columnIndex++, row, order.getCardid()));//收货人身份证号
							
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, order.getR_name()));//收货人姓名
							sheet.addCell(new Label(columnIndex++, row, ""));//收货人身份证号
						}
						
						
						String address="";
						
						if(!StringUtil.isEmpty(order.getR_privice()))
						{
							address=address+order.getR_privice();
						}
						if(!StringUtil.isEmpty(order.getR_city()))
						{
							address=address+order.getR_city();
						}
						if(!StringUtil.isEmpty(order.getR_dist()))
						{
							address=address+order.getR_dist();
						}
						if(!StringUtil.isEmpty(order.getR_address()))
						{
							address=address+order.getR_address();
						}
						
						sheet.addCell(new Label(columnIndex++, row, address));//收货人地址
						
						sheet.addCell(new Label(columnIndex++, row, order.getR_phone()));//收货人电话
						
						sheet.addCell(new Label(columnIndex++, row, "MEITAO EXPRESS MAIL"));//发货人名称
						sheet.addCell(new Label(columnIndex++, row, ""));//订单号
						sheet.addCell(new Label(columnIndex++, row, ""));//备注
						sheet.addCell(new Label(columnIndex++, row, "357 E GARVEY AVE,MONTEREY PARK CA91755"));//发货人地址
						
						
						sheet.addCell(new Label(columnIndex++, row, order.getCommudityslist()));//商品描述
						
						try{
							
							  sheet.addCell(new Label(columnIndex++, row, String.valueOf(100*addindex)));//总运费
						}catch(Exception e)
						{
							sheet.addCell(new Label(columnIndex++, row, ""));//总运费
						}
						sheet.addCell(new Label(columnIndex++, row, ""));//支付编号
						
						
						
						

						row=addindex+row;
					}
					if((!zipurl.equalsIgnoreCase("")))
					{
						WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
			                    10, WritableFont.NO_BOLD);// 字体样式
						WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
						oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
						sheet.addCell(new Label(0, row, zipurl,oceanbluecolor));//显示压缩包路径
						
					}
					
				}
			}, templetFile, orders, os);
		}
		
		
		
	}
	
	
	
	
	
	// 导出身份证库的信息
	public static void exportcardlibwordToMeitao(List<CardId_lib> orders,
			File templetFile, OutputStream os) throws Exception {
		if (templetFile != null && templetFile.exists()) {
			// 模板复制
			ExcelUtil.exportExcel(new ExcelWrite<CardId_lib>() {
				@Override
				public void write(Map<String, String> headers,
						Collection<CardId_lib> datas, WritableSheet sheet)
						throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有用户数据.....");
						sheet.addCell(cell);
						return;
					}
					
					int row = 1;
					

					for (CardId_lib cardids : datas) {
						
						
						int columnIndex = 0;
						// sheet.addCell(new Label(columnIndex++, row,
						// order.getId()));// 是用记录编号
						
						sheet.addCell(new Label(columnIndex++, row,cardids.getCname()));// 姓名
						sheet.addCell(new Label(columnIndex++, row,cardids.getProvince()));// 省
						sheet.addCell(new Label(columnIndex++, row,cardids.getCity()));// 市
						sheet.addCell(new Label(columnIndex++, row,cardids.getDist()));// 区县
						sheet.addCell(new Label(columnIndex++, row,cardids.getAddress()));// 地址
						sheet.addCell(new Label(columnIndex++, row,cardids.getPicflag()));// 有没图片
						sheet.addCell(new Label(columnIndex++, row,cardids.getCardid()));// 有id
						
					
						
						row++;
					}
				}
			}, templetFile, orders, os);
		} else {
			// 重新写
			ExcelUtil.exportExcel(new ExcelWrite<CardId_lib>() {
				@Override
				public void write(Map<String, String> headers,
						Collection<CardId_lib> datas, WritableSheet sheet)
						throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有用户数据.....");
						sheet.addCell(cell);
						return;
					}
					
					int row = 1;
					

					for (CardId_lib cardids : datas) {
						
						
						int columnIndex = 0;
						// sheet.addCell(new Label(columnIndex++, row,
						// order.getId()));// 是用记录编号
						
						sheet.addCell(new Label(columnIndex++, row,cardids.getCname()));// 姓名
						sheet.addCell(new Label(columnIndex++, row,cardids.getProvince()));// 省
						sheet.addCell(new Label(columnIndex++, row,cardids.getCity()));// 市
						sheet.addCell(new Label(columnIndex++, row,cardids.getDist()));// 区县
						sheet.addCell(new Label(columnIndex++, row,cardids.getAddress()));// 地址
						sheet.addCell(new Label(columnIndex++, row,cardids.getPicflag()));// 有没图片
						sheet.addCell(new Label(columnIndex++, row,cardids.getCardid()));// 有id
						
					
						
						row++;
					}
				}
			}, "order-datas", null, orders, os);
		}
	}
	
}

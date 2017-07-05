package com.meitao.common.util;
import java.io.File;

import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;

import java.util.Collection;

import java.util.List;
import java.util.Map;


import com.meitao.common.util.excel.ExcelReader;
import com.meitao.common.util.excel.ExcelUtil;
import com.meitao.common.util.excel.ExcelWrite;

import com.meitao.model.temp.ImportFlyInfo;

import jxl.Sheet;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;

public class FlyInfoUtil {
	//kai 20150918  导入唯一快递状态 
	public static List<String> readOrderExcel_weiyi_state(InputStream input)
			throws Exception {
		return ExcelUtil.importExcel(new ExcelReader<String>() {
			@Override
			public List<String> read(Sheet sheet) throws Exception {
				List<String> list = new ArrayList<String>();
				int rows = sheet.getRows();
				for (int i = 0; i < rows; i++) {
					int j = 0;				
					String orderid=sheet.getCell(j++, i).getContents();
					if((orderid==null)||(orderid.equalsIgnoreCase("")))
					{
						break;//遇到空即跳出
					}
					list.add(orderid.trim());
				}
				return list;
			}
		}, input);
	}
	
	//kai 20160114  导入航班信息的运单号提取
	public static List<ImportFlyInfo> readOrderExcel_flyno(InputStream input)
			throws Exception {
		return ExcelUtil.importExcel(new ExcelReader<ImportFlyInfo>() {
			@Override
			public List<ImportFlyInfo> read(Sheet sheet) throws Exception {
				List<ImportFlyInfo> list = new ArrayList<ImportFlyInfo>();
				int rows = sheet.getRows();
				for (int i = 0; i < rows; i++) {
					int j = 0;				
					String orderid=sheet.getCell(j++, i).getContents();
					if(StringUtil.isEmpty(orderid))
					{
						break;//遇到空即跳出
					}
					ImportFlyInfo temp=new ImportFlyInfo();
					temp.setOrderId(orderid.trim());
					list.add(temp);
				}
				return list;
			}
		}, input);
	}
	
	//导出批量导入状态结果导出
			public static void export_flyno_state_result(List<ImportFlyInfo> orders, File templetFile, OutputStream os) throws Exception {
				if (templetFile != null && templetFile.exists()) {
					// 模板复制
					ExcelUtil.exportExcel(new ExcelWrite<ImportFlyInfo>() {
						@Override
						public void write(Map<String, String> headers, Collection<ImportFlyInfo> datas, WritableSheet sheet)
						        throws Exception {
							if (datas == null || datas.isEmpty()) {
								sheet.mergeCells(0, 1, 11, 1);
								Label cell = new Label(0, 1, "没有用户数据.....");
								sheet.addCell(cell);
								return;
							}

							int row = 1;
							
							for (ImportFlyInfo iorder : datas)
							{
								if(iorder.getResult()==null)
								{
									continue;
								}
								int columnIndex = 0;
								WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
					                    10, WritableFont.NO_BOLD);// 字体样式
								
								WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
								redcolor.setBackground(Colour.RED);
								
								WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
								oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
								
								if(!StringUtil.isEmpty(iorder.getResult())&&(iorder.getResult().indexOf("成功")!=-1))//成功信息
								{
									sheet.addCell(new Label(columnIndex++, row,iorder.getOrderId()));// 导入的单号
									sheet.addCell(new Label(columnIndex++, row,iorder.getResult()));// 结果
									
								}
								else
								{
									sheet.addCell(new Label(columnIndex++, row,iorder.getOrderId(),redcolor));// 导入的单号
									sheet.addCell(new Label(columnIndex++, row,iorder.getResult(),redcolor));// 导入的包裹单号
									
								}
								row++;
								
								
						
							}
						}
					}, templetFile, orders, os);
				} else {
					// 重新写
					ExcelUtil.exportExcel(new ExcelWrite<ImportFlyInfo>() {
						@Override
						public void write(Map<String, String> headers, Collection<ImportFlyInfo> datas, WritableSheet sheet)
						        throws Exception {
							if (datas == null || datas.isEmpty()) {
								sheet.mergeCells(0, 1, 11, 1);
								Label cell = new Label(0, 1, "没有用户数据.....");
								sheet.addCell(cell);
								return;
							}

							int row = 1;
							
							for (ImportFlyInfo iorder : datas)
							{
								if(iorder.getResult()==null)
								{
									continue;
								}
								int columnIndex = 0;
								WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
					                    10, WritableFont.NO_BOLD);// 字体样式
								
								WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
								redcolor.setBackground(Colour.RED);
								
								WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
								oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
								
								if(!StringUtil.isEmpty(iorder.getResult())&&(iorder.getResult().indexOf("成功")!=-1))//成功信息
								{
									sheet.addCell(new Label(columnIndex++, row,iorder.getOrderId()));// 导入的单号
									sheet.addCell(new Label(columnIndex++, row,iorder.getResult()));// 结果
									
								}
								else
								{
									sheet.addCell(new Label(columnIndex++, row,iorder.getOrderId(),redcolor));// 导入的单号
									sheet.addCell(new Label(columnIndex++, row,iorder.getResult(),redcolor));// 导入的包裹单号
									
								}
								row++;
								
								
						
							}
						}
					}, "order-datas", null, orders, os);
				}
			}
			
			
}

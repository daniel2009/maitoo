package com.meitao.common.util;

import java.io.File;

import java.io.InputStream;
import java.io.OutputStream;

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

import com.meitao.model.SeaNumber;



public class SeaNumberUtil {
	

	public static List<SeaNumber> readOrderExcel_seanumer(InputStream input)
			throws Exception {
		return ExcelUtil.importExcel(new ExcelReader<SeaNumber>() {
			@Override
			public List<SeaNumber> read(Sheet sheet) throws Exception {
				List<SeaNumber> list = new ArrayList<SeaNumber>();
				int rows = sheet.getRows();
				for (int i = 0; i < rows; i++) {
					int j = 0;
					SeaNumber io = new SeaNumber();
					String orderid=sheet.getCell(j++, i).getContents();
					io.setOrderId(orderid);
					io.setState("0");
					list.add(io);
				}
				return list;
			}
		}, input);
	}
	
	// kai 20160326 导出海关单号结果
	public static void exportOrderStateToResult(List<SeaNumber> orders,
			File templetFile, OutputStream os) throws Exception {
		if (templetFile != null && templetFile.exists()) {
			// 模板复制
			ExcelUtil.exportExcel(new ExcelWrite<SeaNumber>() {
				@Override
				public void write(Map<String, String> headers,
						Collection<SeaNumber> datas, WritableSheet sheet)
						throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有用户数据.....");
						sheet.addCell(cell);
						return;
					}
					
					int row = 0;

					for (SeaNumber order : datas) {
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
					
						
						sheet.addCell(new Label(columnIndex++, row, order.getOrderId()));//海关运单号
						
						if(!StringUtil.isEmpty(order.getResult())&&(order.getResult().indexOf("成功")!=-1))//成功信息
						{
							sheet.addCell(new Label(columnIndex++, row, order.getResult()));//结果
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, order.getResult(),redcolor));//结果
						}
						
						

						row++;
					}
				}
			}, templetFile, orders, os);
		} else {
			// 重新写
			ExcelUtil.exportExcel(new ExcelWrite<SeaNumber>() {
				@Override
				public void write(Map<String, String> headers,
						Collection<SeaNumber> datas, WritableSheet sheet)
						throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有用户数据.....");
						sheet.addCell(cell);
						return;
					}
					
					int row = 0;

					for (SeaNumber order : datas) {
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
					
						
						sheet.addCell(new Label(columnIndex++, row, order.getOrderId()));//海关运单号
						
						if(!StringUtil.isEmpty(order.getResult())&&(order.getResult().indexOf("成功")!=-1))//成功信息
						{
							sheet.addCell(new Label(columnIndex++, row, order.getResult()));//结果
						}
						else
						{
							sheet.addCell(new Label(columnIndex++, row, order.getResult(),redcolor));//结果
						}
						
						

						row++;
					}
				}
			}, "order-datas", null, orders, os);
		}
	}
	
}

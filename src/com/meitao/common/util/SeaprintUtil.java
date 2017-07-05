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
import com.meitao.model.CardId;

import com.meitao.model.Porder;



public class SeaprintUtil {
	

	//导入身份证信息
	public static List<CardId> readcardidExcel(InputStream input)
			throws Exception {
		return ExcelUtil.importExcel(new ExcelReader<CardId>() {
			@Override
			public List<CardId> read(Sheet sheet) throws Exception {
				List<CardId> list = new ArrayList<CardId>();
				int rows = sheet.getRows();
				for (int i = 1; i < rows; i++) {
					int j = 0;
					
					//姓名
					CardId card = new CardId();
					String name=sheet.getCell(j++, i).getContents();
					if(StringUtil.isEmpty(name))
					{
						name="";
					}
					else
					{
						name=name.trim();
					}
					card.setCname(name);
					//省
					String province=sheet.getCell(j++, i).getContents();
					if(StringUtil.isEmpty(province))
					{
						province="";
					}
					else
					{
						province=province.trim();
					}
					card.setProvince(province);
					
					//市
					String city=sheet.getCell(j++, i).getContents();
					if(StringUtil.isEmpty(city))
					{
						city="";
					}
					else
					{
						city=city.trim();
					}
					card.setCity(city);
					
					//区
					String dist=sheet.getCell(j++, i).getContents();
					if(StringUtil.isEmpty(dist))
					{
						dist="";
					}
					else
					{
						dist=dist.trim();
					}
					card.setDist(dist);
					//详细地址
					String address=sheet.getCell(j++, i).getContents();
					if(StringUtil.isEmpty(address))
					{
						address="";
					}
					else
					{
						address=address.trim();
					}
					card.setAddress(address);
					
					//身份证号
					String cardid=sheet.getCell(j++, i).getContents();
					if(StringUtil.isEmpty(cardid))
					{
						cardid="";
					}
					else
					{
						cardid=cardid.trim();
					}
					card.setCardid(cardid);
					
					
					
					
					
					list.add(card);
				}
				return list;
			}
		}, input);
	}
	
	// kai身份证结果导出
	public static void exportcardidsToResult(List<CardId> orders,
			File templetFile, OutputStream os) throws Exception {
		if (templetFile != null && templetFile.exists()) {
			// 模板复制
			ExcelUtil.exportExcel(new ExcelWrite<CardId>() {
				@Override
				public void write(Map<String, String> headers,
						Collection<CardId> datas, WritableSheet sheet)
						throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有用户数据.....");
						sheet.addCell(cell);
						return;
					}
					
					int row = 1;

					for (CardId order : datas) {
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
					
						if(!StringUtil.isEmpty(order.getResult())&&(order.getResult().indexOf("成功")!=-1))//成功信息
						{
							sheet.addCell(new Label(columnIndex++, row, order.getCname()));//name
							sheet.addCell(new Label(columnIndex++, row, order.getProvince()));//省
							sheet.addCell(new Label(columnIndex++, row, order.getCity()));//市
							sheet.addCell(new Label(columnIndex++, row, order.getDist()));//区
							sheet.addCell(new Label(columnIndex++, row, order.getAddress()));//地址
							sheet.addCell(new Label(columnIndex++, row, order.getCardid()));//身份证号
							sheet.addCell(new Label(columnIndex++, row, order.getResult()));//结果说明
							
						}
						else
						{
							
							
							sheet.addCell(new Label(columnIndex++, row, order.getCname(),redcolor));//name
							sheet.addCell(new Label(columnIndex++, row, order.getProvince(),redcolor));//省
							sheet.addCell(new Label(columnIndex++, row, order.getCity(),redcolor));//市
							sheet.addCell(new Label(columnIndex++, row, order.getDist(),redcolor));//区
							sheet.addCell(new Label(columnIndex++, row, order.getAddress(),redcolor));//地址
							sheet.addCell(new Label(columnIndex++, row, order.getCardid(),redcolor));//身份证号
							sheet.addCell(new Label(columnIndex++, row, order.getResult(),redcolor));//结果说明
						}
						
						

						row++;
					}
				}
			}, templetFile, orders, os);
		} else {
			// 模板复制
			ExcelUtil.exportExcel(new ExcelWrite<CardId>() {
				@Override
				public void write(Map<String, String> headers,
						Collection<CardId> datas, WritableSheet sheet)
						throws Exception {
					if (datas == null || datas.isEmpty()) {
						sheet.mergeCells(0, 1, 11, 1);
						Label cell = new Label(0, 1, "没有用户数据.....");
						sheet.addCell(cell);
						return;
					}
					
					int row = 1;

					for (CardId order : datas) {
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
					
						if(!StringUtil.isEmpty(order.getResult())&&(order.getResult().indexOf("成功")!=-1))//成功信息
						{
							sheet.addCell(new Label(columnIndex++, row, order.getCname()));//name
							sheet.addCell(new Label(columnIndex++, row, order.getProvince()));//省
							sheet.addCell(new Label(columnIndex++, row, order.getCity()));//市
							sheet.addCell(new Label(columnIndex++, row, order.getDist()));//区
							sheet.addCell(new Label(columnIndex++, row, order.getAddress()));//地址
							sheet.addCell(new Label(columnIndex++, row, order.getCardid()));//身份证号
							sheet.addCell(new Label(columnIndex++, row, order.getResult()));//结果说明
							
						}
						else
						{
							
							
							sheet.addCell(new Label(columnIndex++, row, order.getCname(),redcolor));//name
							sheet.addCell(new Label(columnIndex++, row, order.getProvince(),redcolor));//省
							sheet.addCell(new Label(columnIndex++, row, order.getCity(),redcolor));//市
							sheet.addCell(new Label(columnIndex++, row, order.getDist(),redcolor));//区
							sheet.addCell(new Label(columnIndex++, row, order.getAddress(),redcolor));//地址
							sheet.addCell(new Label(columnIndex++, row, order.getCardid(),redcolor));//身份证号
							sheet.addCell(new Label(columnIndex++, row, order.getResult(),redcolor));//结果说明
						}
						
						

						row++;
					}
				}
			}, templetFile, orders, os);
		}
	}
	
	//导入海关打单列表
		public static List<Porder> readPorderExcel(InputStream input)
				throws Exception {
			return ExcelUtil.importExcel(new ExcelReader<Porder>() {
				@Override
				public List<Porder> read(Sheet sheet) throws Exception {
					List<Porder> list = new ArrayList<Porder>();
					int rows = sheet.getRows();
					for (int i = 1; i < rows; i++) {
						int j = 1;//第一列不读取
						
						
						Porder card = new Porder();
						//系统运单号
						String orderid=sheet.getCell(j++, i).getContents();
						if(StringUtil.isEmpty(orderid))
						{
							break;//遇到空单号即停止
						}
						else
						{
							orderid=orderid.trim();
						}
						card.setOrderId(orderid);
						//分运单号
						String divideorderid=sheet.getCell(j++, i).getContents();
						if(StringUtil.isEmpty(divideorderid))
						{
							divideorderid="";
						}
						else
						{
							divideorderid=divideorderid.trim();
						}
						card.setDivideOrderId(divideorderid);
						
						//报关物品
						String taxcommditylists=sheet.getCell(j++, i).getContents();
						if(StringUtil.isEmpty(taxcommditylists))
						{
							taxcommditylists="";
						}
						else
						{
							taxcommditylists=taxcommditylists.trim();
						}
						card.setTaxCommuditylists(taxcommditylists);
						
						//品名
						String commditylists=sheet.getCell(j++, i).getContents();
						if(StringUtil.isEmpty(commditylists))
						{
							commditylists="";
						}
						else
						{
							commditylists=commditylists.trim();
						}
						card.setCommuditylists(commditylists);
						//品牌
						String brands=sheet.getCell(j++, i).getContents();
						if(StringUtil.isEmpty(brands))
						{
							brands="";
						}
						else
						{
							brands=brands.trim();
						}
						card.setBrands(brands);
						
						//规格
						String guiguo=sheet.getCell(j++, i).getContents();
						if(StringUtil.isEmpty(guiguo))
						{
							guiguo="";
						}
						else
						{
							guiguo=guiguo.trim();
						}
						card.setGuiguo(guiguo);
						
						
						//件数
						String numbers=sheet.getCell(j++, i).getContents();
						if(StringUtil.isEmpty(numbers))
						{
							numbers="";
						}
						else
						{
							numbers=numbers.trim();
						}
						card.setNumbers(numbers);
						
						//重量
						String weight=sheet.getCell(j++, i).getContents();
						if(StringUtil.isEmpty(weight))
						{
							weight="";
						}
						else
						{
							weight=weight.trim();
						}
						card.setWeight(weight);
						
						//价值
						String value=sheet.getCell(j++, i).getContents();
						if(StringUtil.isEmpty(value))
						{
							value="";
						}
						else
						{
							value=value.trim();
						}
						card.setValue(value);
						
						//发货公司
						String sendcompany=sheet.getCell(j++, i).getContents();
						if(StringUtil.isEmpty(sendcompany))
						{
							sendcompany="";
						}
						else
						{
							sendcompany=sendcompany.trim();
						}
						card.setSendcompany(sendcompany);
						
						//发货人地址
						String sendaddress=sheet.getCell(j++, i).getContents();
						if(StringUtil.isEmpty(sendaddress))
						{
							sendaddress="";
						}
						else
						{
							sendaddress=sendaddress.trim();
						}
						card.setSendaddress(sendaddress);
						
						//收货公司
						String revcompany=sheet.getCell(j++, i).getContents();
						if(StringUtil.isEmpty(revcompany))
						{
							revcompany="";
						}
						else
						{
							revcompany=revcompany.trim();
						}
						card.setRevcompany(revcompany);
						
						//收货人省
						String cprovince=sheet.getCell(j++, i).getContents();
						if(StringUtil.isEmpty(cprovince))
						{
							cprovince="";
						}
						else
						{
							cprovince=cprovince.trim();
						}
						card.setCprovince(cprovince);;
						
						//收货人市
						String ccity=sheet.getCell(j++, i).getContents();
						if(StringUtil.isEmpty(ccity))
						{
							ccity="";
						}
						else
						{
							ccity=ccity.trim();
						}
						card.setCcity(ccity);
						
						//收货人区/县
						String cdist=sheet.getCell(j++, i).getContents();
						if(StringUtil.isEmpty(cdist))
						{
							cdist="";
						}
						else
						{
							cdist=cdist.trim();
						}
						card.setCdist(cdist);
						
						//收货人详细地址
						String caddress=sheet.getCell(j++, i).getContents();
						if(StringUtil.isEmpty(caddress))
						{
							caddress="";
						}
						else
						{
							caddress=caddress.trim();
						}
						card.setCaddress(caddress);
						
						//收货人姓名
						String cname=sheet.getCell(j++, i).getContents();
						if(StringUtil.isEmpty(cname))
						{
							cname="";
						}
						else
						{
							cname=cname.trim();
						}
						card.setCname(cname);
						
						//收货人电话
						String cphone=sheet.getCell(j++, i).getContents();
						if(StringUtil.isEmpty(cphone))
						{
							cphone="";
						}
						else
						{
							cphone=cphone.trim();
						}
						card.setCphone(cphone);
						
						//箱号
						String boxno=sheet.getCell(j++, i).getContents();
						if(StringUtil.isEmpty(boxno))
						{
							boxno="";
						}
						else
						{
							boxno=boxno.trim();
						}
						card.setBoxno(boxno);
						
						//身份证号
						String cardid=sheet.getCell(j++, i).getContents();
						if(StringUtil.isEmpty(cardid))
						{
							cardid="";
						}
						else
						{
							cardid=cardid.trim();
						}
						card.setCardId(cardid);
						
						
						list.add(card);
					}
					return list;
				}
			}, input);
		}
		
		//检查导入的是否有重复的运单
		public  static List<Porder> checkpordersbyself(List<Porder> list)
				throws Exception {
			if((list==null)||(list.size()<1))
			{
				return list;
			}
			
			if(list.size()==1)
			{
				return list;
			}
			
			for(int i=0;i<list.size()-1;i++)
			{
				for(int j=i+1;j<list.size();j++)
				{			
					/*if(!StringUtil.isEmpty(list.get(j).getRepeatflag())&&(list.get(j).getRepeatflag().equalsIgnoreCase("1")))
					{
						continue;
					}*/
					if(list.get(i).getOrderId().equalsIgnoreCase(list.get(j).getOrderId()))
					{
						list.get(i).setRepeatflag("1");
						list.get(j).setRepeatflag("1");
					}
				}
			}
			
			return list;
			
		}
		
		
		
		// 导出错误列表，海关运单
		public static void exportssesaprintwrong(List<Porder> orders,
				File templetFile, OutputStream os) throws Exception {
			if (templetFile != null && templetFile.exists()) {
				// 模板复制
				ExcelUtil.exportExcel(new ExcelWrite<Porder>() {
					@Override
					public void write(Map<String, String> headers,
							Collection<Porder> datas, WritableSheet sheet)
							throws Exception {
						if (datas == null || datas.isEmpty()) {
							sheet.mergeCells(0, 1, 11, 1);
							Label cell = new Label(0, 1, "没有用户数据.....");
							sheet.addCell(cell);
							return;
						}
						
						int row = 1;

						for (Porder order : datas) {
							if (order == null) {
								continue;
							}
							WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
				                    10, WritableFont.NO_BOLD);// 字体样式
							
							WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
							redcolor.setBackground(Colour.RED);
							
							WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
							oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
							int columnIndex = 1;
							
							if(!StringUtil.isEmpty(order.getRepeatflag())&&(order.getRepeatflag().equalsIgnoreCase("1")))//重复信息
							{
								sheet.addCell(new Label(columnIndex++, row, order.getOrderId(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getDivideOrderId(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getTaxCommuditylists(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCommuditylists(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getBrands(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getGuiguo(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getNumbers(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getWeight(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getValue(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getSendcompany(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getSendaddress(),oceanbluecolor));//name
								
								sheet.addCell(new Label(columnIndex++, row, order.getRevcompany(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCprovince(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCcity(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCdist(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCaddress(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCname(),oceanbluecolor));//name
							
								sheet.addCell(new Label(columnIndex++, row, order.getCphone(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getBoxno(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCardId(),oceanbluecolor));//name
								
								sheet.addCell(new Label(columnIndex++, row, "本表重复",oceanbluecolor));//name
								
							}
							else
							{
								
								
								sheet.addCell(new Label(columnIndex++, row, order.getOrderId()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getDivideOrderId()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getTaxCommuditylists()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCommuditylists()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getBrands()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getGuiguo()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getNumbers()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getWeight()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getValue()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getSendcompany()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getSendaddress()));//name
								
								sheet.addCell(new Label(columnIndex++, row, order.getRevcompany()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCprovince()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCcity()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCdist()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCaddress()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCname()));//name
							
								sheet.addCell(new Label(columnIndex++, row, order.getCphone()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getBoxno()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCardId()));//name
							}
							
							

							row++;
						}
					}
				}, templetFile, orders, os);
			} else {
				// 模板复制
				ExcelUtil.exportExcel(new ExcelWrite<Porder>() {
					@Override
					public void write(Map<String, String> headers,
							Collection<Porder> datas, WritableSheet sheet)
							throws Exception {
						if (datas == null || datas.isEmpty()) {
							sheet.mergeCells(0, 1, 11, 1);
							Label cell = new Label(0, 1, "没有用户数据.....");
							sheet.addCell(cell);
							return;
						}
						
						int row = 1;

						for (Porder order : datas) {
							if (order == null) {
								continue;
							}
							WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
				                    10, WritableFont.NO_BOLD);// 字体样式
							
							WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
							redcolor.setBackground(Colour.RED);
							
							WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
							oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
							int columnIndex = 1;
							
							if(!StringUtil.isEmpty(order.getRepeatflag())&&(order.getRepeatflag().equalsIgnoreCase("1")))//重复信息
							{
								sheet.addCell(new Label(columnIndex++, row, order.getOrderId(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getDivideOrderId(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getTaxCommuditylists(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCommuditylists(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getBrands(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getGuiguo(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getNumbers(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getWeight(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getValue(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getSendcompany(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getSendaddress(),oceanbluecolor));//name
								
								sheet.addCell(new Label(columnIndex++, row, order.getRevcompany(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCprovince(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCcity(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCdist(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCaddress(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCname(),oceanbluecolor));//name
							
								sheet.addCell(new Label(columnIndex++, row, order.getCphone(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getBoxno(),oceanbluecolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCardId(),oceanbluecolor));//name
								
								sheet.addCell(new Label(columnIndex++, row, "本表重复",oceanbluecolor));//name
								
							}
							else
							{
								
								
								sheet.addCell(new Label(columnIndex++, row, order.getOrderId()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getDivideOrderId()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getTaxCommuditylists()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCommuditylists()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getBrands()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getGuiguo()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getNumbers()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getWeight()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getValue()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getSendcompany()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getSendaddress()));//name
								
								sheet.addCell(new Label(columnIndex++, row, order.getRevcompany()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCprovince()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCcity()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCdist()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCaddress()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCname()));//name
							
								sheet.addCell(new Label(columnIndex++, row, order.getCphone()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getBoxno()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCardId()));//name
							}
							
							

							row++;
						}
					}
				}, templetFile, orders, os);
			}
		}
		
		
		
		// 插入海关运单结果导出
		public static void exportporderinsertresult(List<Porder> orders,
				File templetFile, OutputStream os) throws Exception {
			if (templetFile != null && templetFile.exists()) {
				// 模板复制
				ExcelUtil.exportExcel(new ExcelWrite<Porder>() {
					@Override
					public void write(Map<String, String> headers,
							Collection<Porder> datas, WritableSheet sheet)
							throws Exception {
						if (datas == null || datas.isEmpty()) {
							sheet.mergeCells(0, 1, 11, 1);
							Label cell = new Label(0, 1, "没有用户数据.....");
							sheet.addCell(cell);
							return;
						}
						
						int row = 1;

						for (Porder order : datas) {
							if (order == null) {
								continue;
							}
							WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
				                    10, WritableFont.NO_BOLD);// 字体样式
							
							WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
							redcolor.setBackground(Colour.RED);
							
							WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
							oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
							int columnIndex = 1;
							
							if(!StringUtil.isEmpty(order.getResult())&&(order.getResult().indexOf("成功")!=-1))//成功信息
							{
								
								sheet.addCell(new Label(columnIndex++, row, order.getOrderId()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getDivideOrderId()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getTaxCommuditylists()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCommuditylists()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getBrands()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getGuiguo()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getNumbers()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getWeight()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getValue()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getSendcompany()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getSendaddress()));//name
								
								sheet.addCell(new Label(columnIndex++, row, order.getRevcompany()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCprovince()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCcity()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCdist()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCaddress()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCname()));//name
							
								sheet.addCell(new Label(columnIndex++, row, order.getCphone()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getBoxno()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCardId()));//name
								
								sheet.addCell(new Label(columnIndex++, row, order.getResult()));//name
							}
							else
							{
								
								

								
								
								sheet.addCell(new Label(columnIndex++, row, order.getOrderId(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getDivideOrderId(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getTaxCommuditylists(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCommuditylists(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getBrands(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getGuiguo(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getNumbers(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getWeight(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getValue(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getSendcompany(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getSendaddress(),redcolor));//name
								
								sheet.addCell(new Label(columnIndex++, row, order.getRevcompany(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCprovince(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCcity(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCdist(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCaddress(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCname(),redcolor));//name
							
								sheet.addCell(new Label(columnIndex++, row, order.getCphone(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getBoxno(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCardId(),redcolor));//name
								
								sheet.addCell(new Label(columnIndex++, row, order.getResult(),redcolor));//name
							}
							
							

							row++;
						}
					}
				}, templetFile, orders, os);
			} else {
				// 模板复制
				ExcelUtil.exportExcel(new ExcelWrite<Porder>() {
					@Override
					public void write(Map<String, String> headers,
							Collection<Porder> datas, WritableSheet sheet)
							throws Exception {
						if (datas == null || datas.isEmpty()) {
							sheet.mergeCells(0, 1, 11, 1);
							Label cell = new Label(0, 1, "没有用户数据.....");
							sheet.addCell(cell);
							return;
						}
						
						int row = 1;

						for (Porder order : datas) {
							if (order == null) {
								continue;
							}
							WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
				                    10, WritableFont.NO_BOLD);// 字体样式
							
							WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
							redcolor.setBackground(Colour.RED);
							
							WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
							oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
							int columnIndex = 1;
							
							if(!StringUtil.isEmpty(order.getResult())&&(order.getResult().indexOf("成功")!=-1))//成功信息
							{
								
								sheet.addCell(new Label(columnIndex++, row, order.getOrderId()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getDivideOrderId()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getTaxCommuditylists()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCommuditylists()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getBrands()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getGuiguo()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getNumbers()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getWeight()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getValue()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getSendcompany()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getSendaddress()));//name
								
								sheet.addCell(new Label(columnIndex++, row, order.getRevcompany()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCprovince()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCcity()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCdist()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCaddress()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCname()));//name
							
								sheet.addCell(new Label(columnIndex++, row, order.getCphone()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getBoxno()));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCardId()));//name
								
								sheet.addCell(new Label(columnIndex++, row, order.getResult()));//name
							}
							else
							{
								
								

								
								
								sheet.addCell(new Label(columnIndex++, row, order.getOrderId(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getDivideOrderId(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getTaxCommuditylists(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCommuditylists(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getBrands(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getGuiguo(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getNumbers(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getWeight(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getValue(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getSendcompany(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getSendaddress(),redcolor));//name
								
								sheet.addCell(new Label(columnIndex++, row, order.getRevcompany(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCprovince(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCcity(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCdist(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCaddress(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCname(),redcolor));//name
							
								sheet.addCell(new Label(columnIndex++, row, order.getCphone(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getBoxno(),redcolor));//name
								sheet.addCell(new Label(columnIndex++, row, order.getCardId(),redcolor));//name
								
								sheet.addCell(new Label(columnIndex++, row, order.getResult(),redcolor));//name
							}
							
							

							row++;
						}
					}
				}, templetFile, orders, os);
			}
		}
		
		
		
		// 直接导出海关清单
				public static void exportporders(List<Porder> orders,
						File templetFile, OutputStream os) throws Exception {
					if (templetFile != null && templetFile.exists()) {
						// 模板复制
						ExcelUtil.exportExcel(new ExcelWrite<Porder>() {
							@Override
							public void write(Map<String, String> headers,
									Collection<Porder> datas, WritableSheet sheet)
									throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有用户数据.....");
									sheet.addCell(cell);
									return;
								}
								
								int row = 1;

								for (Porder order : datas) {
									if (order == null) {
										continue;
									}
									WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
						                    10, WritableFont.NO_BOLD);// 字体样式
									
									WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
									redcolor.setBackground(Colour.RED);
									
									WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
									oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
									int columnIndex = 1;
									
									
										
										sheet.addCell(new Label(columnIndex++, row, order.getOrderId()));//name
										sheet.addCell(new Label(columnIndex++, row, order.getDivideOrderId()));//name
										sheet.addCell(new Label(columnIndex++, row, order.getTaxCommuditylists()));//name
										sheet.addCell(new Label(columnIndex++, row, order.getCommuditylists()));//name
										sheet.addCell(new Label(columnIndex++, row, order.getBrands()));//name
										sheet.addCell(new Label(columnIndex++, row, order.getGuiguo()));//name
										sheet.addCell(new Label(columnIndex++, row, order.getNumbers()));//name
										sheet.addCell(new Label(columnIndex++, row, order.getWeight()));//name
										sheet.addCell(new Label(columnIndex++, row, order.getValue()));//name
										sheet.addCell(new Label(columnIndex++, row, order.getSendcompany()));//name
										sheet.addCell(new Label(columnIndex++, row, order.getSendaddress()));//name
										
										sheet.addCell(new Label(columnIndex++, row, order.getRevcompany()));//name
										
										String address=order.getCprovince()+order.getCcity()+order.getCdist()+order.getCaddress();
										
										//sheet.addCell(new Label(columnIndex++, row, order.getCprovince()));//name
										//sheet.addCell(new Label(columnIndex++, row, order.getCcity()));//name
										//sheet.addCell(new Label(columnIndex++, row, order.getCdist()));//name
										//sheet.addCell(new Label(columnIndex++, row, order.getCaddress()));//name
										sheet.addCell(new Label(columnIndex++, row, address));//name
										
										
										sheet.addCell(new Label(columnIndex++, row, order.getCname()));//name
									
										sheet.addCell(new Label(columnIndex++, row, order.getCphone()));//name
										sheet.addCell(new Label(columnIndex++, row, order.getBoxno()));//name
										sheet.addCell(new Label(columnIndex++, row, order.getCardId()));//name
										
										sheet.addCell(new Label(columnIndex++, row, order.getMarkresult()));//name
									
									
									

									row++;
								}
							}
						}, templetFile, orders, os);
					} else {
						// 模板复制
						ExcelUtil.exportExcel(new ExcelWrite<Porder>() {
							@Override
							public void write(Map<String, String> headers,
									Collection<Porder> datas, WritableSheet sheet)
									throws Exception {
								if (datas == null || datas.isEmpty()) {
									sheet.mergeCells(0, 1, 11, 1);
									Label cell = new Label(0, 1, "没有用户数据.....");
									sheet.addCell(cell);
									return;
								}
								
								int row = 1;

								for (Porder order : datas) {
									if (order == null) {
										continue;
									}
									WritableFont font = new WritableFont(WritableFont.createFont("宋体"),
						                    10, WritableFont.NO_BOLD);// 字体样式
									
									WritableCellFormat redcolor = new WritableCellFormat(font);// 单元格样式
									redcolor.setBackground(Colour.RED);
									
									WritableCellFormat oceanbluecolor = new WritableCellFormat(font);// 单元格样式
									oceanbluecolor.setBackground(Colour.OCEAN_BLUE);
									int columnIndex = 1;
									
									
										
										sheet.addCell(new Label(columnIndex++, row, order.getOrderId()));//name
										sheet.addCell(new Label(columnIndex++, row, order.getDivideOrderId()));//name
										sheet.addCell(new Label(columnIndex++, row, order.getTaxCommuditylists()));//name
										sheet.addCell(new Label(columnIndex++, row, order.getCommuditylists()));//name
										sheet.addCell(new Label(columnIndex++, row, order.getBrands()));//name
										sheet.addCell(new Label(columnIndex++, row, order.getGuiguo()));//name
										sheet.addCell(new Label(columnIndex++, row, order.getNumbers()));//name
										sheet.addCell(new Label(columnIndex++, row, order.getWeight()));//name
										sheet.addCell(new Label(columnIndex++, row, order.getValue()));//name
										sheet.addCell(new Label(columnIndex++, row, order.getSendcompany()));//name
										sheet.addCell(new Label(columnIndex++, row, order.getSendaddress()));//name
										
										sheet.addCell(new Label(columnIndex++, row, order.getRevcompany()));//name
										
										
										String address=order.getCprovince()+order.getCcity()+order.getCdist()+order.getCaddress();
										
										//sheet.addCell(new Label(columnIndex++, row, order.getCprovince()));//name
										//sheet.addCell(new Label(columnIndex++, row, order.getCcity()));//name
										//sheet.addCell(new Label(columnIndex++, row, order.getCdist()));//name
										//sheet.addCell(new Label(columnIndex++, row, order.getCaddress()));//name
										sheet.addCell(new Label(columnIndex++, row, address));//name
										
										
										sheet.addCell(new Label(columnIndex++, row, order.getCname()));//name
									
										sheet.addCell(new Label(columnIndex++, row, order.getCphone()));//name
										sheet.addCell(new Label(columnIndex++, row, order.getBoxno()));//name
										sheet.addCell(new Label(columnIndex++, row, order.getCardId()));//name
										
										sheet.addCell(new Label(columnIndex++, row, order.getMarkresult()));//name
									
									
									

									row++;
								}
							}
						}, templetFile, orders, os);
					}
				}
}

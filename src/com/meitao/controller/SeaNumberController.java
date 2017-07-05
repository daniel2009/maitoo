package com.meitao.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.common.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;




import com.meitao.dao.SeaNumberDao;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;


import com.meitao.common.util.SeaNumberUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.ResponseString;
import com.meitao.model.SeaNumber;
import com.meitao.model.temp.SeaNumberNo;

@Controller
public class SeaNumberController extends BasicController {
	private static final long serialVersionUID = 6102269932986791708L;
	private static final Logger log = Logger.getLogger(SeaNumberController.class);
	@Value(value = "${default_file_size}")
	private long defaultFileSize;
	
	

	@Value(value = "${meitao.sea.number.result.templets}")
	private String meiseanumberresulttempletsFile;
	
	@Value(value = "${default_excel_type}")
	private String defaultExcelFileType;
	@Autowired
	private SeaNumberDao seaNumberDao;
	

	@RequestMapping(value = "/admin/seanumber/search", method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public ResponseObject<PageSplit<SeaNumber>> getAllSeaNumber(HttpServletRequest request,
			@RequestParam(value = "orderid", required = false) String orderid,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
			@RequestParam(value = "rows", required = false, defaultValue = "10") int rows
			) {
		
		int pageSize=rows;
		int pageNow=pageIndex;
		try {
			if(StringUtil.isEmpty(orderid))
			{
				orderid=null;
			}
			else
			{
				orderid=StringUtil.escapeStringOfSearchKey(orderid);
			}
			
		
			
			
			int rowCount = 0;
			try {
				rowCount=this.seaNumberDao.countOfsearchSeaNumber(orderid, state);
				//rowCount = this.gonggaoDao.countByKey(key);
			} catch (Exception e) {
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "根据关键字获取海关单号失败!");
				//throw ExceptionUtil.handle2ServiceException("根据关键字获取海关单号失败!", e);
			}

			if (rowCount > 0) {
				pageSize = Math.max(pageSize, 1);
				int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
				pageNow = Math.min(pageNow, pageCount);
				PageSplit<SeaNumber> pageSplit = new PageSplit<SeaNumber>();
				pageSplit.setPageCount(pageCount);
				pageSplit.setPageNow(pageNow);
				pageSplit.setRowCount(rowCount);
				pageSplit.setPageSize(pageSize);

				int startIndex = (pageNow - 1) * pageSize;
				
				
				try {
					List<SeaNumber> result = this.seaNumberDao.searchSeaNumber(orderid, state, startIndex, pageSize);
					pageSplit.setDatas(result);	
					
				} catch (Exception e) {
					return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "根据关键字获海关单号列表失败");
					//throw ExceptionUtil.handle2ServiceException("根据关键字获海关单号列表失败", e);
				}
				ResponseObject<PageSplit<SeaNumber>> responseObj = new ResponseObject<PageSplit<SeaNumber>>();
				responseObj.setCode(ResponseCode.SUCCESS_CODE);
				responseObj.setData(pageSplit);
				return responseObj;
			} else {
				return new ResponseObject<PageSplit<SeaNumber>>(ResponseCode.SUCCESS_CODE, "没有海关单号");
			}
		} catch (Exception e) {
			log.error("获取海关单出现异常", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取海关单出现异常");
		}
	}
	@RequestMapping(value = "/admin/seanumber/delone", method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> deleteoneSeaNumber(HttpServletRequest request,
			@RequestParam(value = "id", required = false) String id) {
		
		String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
		{
		
		}
		else
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"对不起，只有高级管理员能够删除海关单号!");
		}
		
		
		
		
		if(StringUtil.isEmpty(id))
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错!"); 
		}
		try {
			int k=this.seaNumberDao.deleteoneseanumber(id);
			if(k<1)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"删除失败!"); 
			}
			return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"删除失败!");
		} catch (Exception e) {
			log.error("删除海关单出现异常", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "删除海关单出现异常");
		}
	}
	
	
	// 批量导入海关单号
	@RequestMapping(value = "/admin/seanumber/import_seanumber",produces="text/plain;charset=UTF-8", method = { RequestMethod.POST,RequestMethod.GET })
	@ResponseBody
	public String importOrderDataFromWeiyiStateExcel(
			HttpServletResponse response,
			HttpServletRequest request, MultipartFile file) {
		
		
		ResponseString responseString=new ResponseString();
		String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
		{
		
		}
		else
		{
			responseString.setCode(ResponseCode.PARAMETER_ERROR);
			responseString.setMessage("对不起，只有高级管理员能够导入海关单号!");
			return responseString.toString();
			//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"对不起，只有高级管理员能够导入海关单号!");
		}
		
	
		if (file != null && file.getSize() > 0) {
			List<SeaNumber> importSeaNumber = null;
			try {
				
				//kai 20151006 判定是不是excel表格
				String originalName = file.getOriginalFilename();
				if (!StringUtil.boolpicisgoodornot(originalName, defaultExcelFileType)) {
					responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
					responseString.setMessage("必须上传excel 2003表格,请重新尝试!");
					return responseString.toString();
					//return generateResponseObject(
					//		ResponseCode.CONSIGNEE_CARD_ERROR, "必须上传excel 2003表格,请重新尝试!");
				}
				
				importSeaNumber=SeaNumberUtil.readOrderExcel_seanumer(file
						.getInputStream());
				
			} catch (OutOfMemoryError e) {
				log.error("内存不够",e);
				responseString.setCode(ResponseCode.SHOW_EXCEPTION);
				responseString.setMessage("内存不够,"+e.getMessage());
				return responseString.toString();
				//return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
				//		"内存不够,"+e.getMessage());
			} catch (Exception e) {
				log.error("读取数据出错", e);
				responseString.setCode(ResponseCode.SHOW_EXCEPTION);
				responseString.setMessage("读取出错,"+e.getMessage());
				return responseString.toString();
				//return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
				//		"读取出错,"+e.getMessage());
			}
			if (!importSeaNumber.isEmpty()) {
				try {
					
					for(SeaNumber number:importSeaNumber)
					{
						try{
							if(StringUtil.isEmpty(number.getOrderId()))
							{
								number.setResult("失败！单号不能为空!");
							}
							else
							{
								int k=this.seaNumberDao.existorderid(number.getOrderId());
								if(k!=0)
								{
									number.setResult("失败！单号已存在!");
								}
								else
								{
									String date = DateUtil.date2String(new Date());
									number.setCreateDate(date);
									number.setModifyDate(date);
									number.setState("0");
									int n=this.seaNumberDao.insertSeaNumber(number);
									if(n<1)
									{
										number.setResult("失败！插入失败!");
									}
									else
									{
										number.setResult("成功!");
									}
								}
							}
						}
						catch(Exception e)
						{
							number.setResult("失败！操作异常!");
						}
					}
					
					
							
							
					OutputStream os=null;

					try {
						
						
						
						String fileName = "update_sea_number_result_" + importSeaNumber.size() + ".xls";
						// key = new String(key.getBytes("ISO-8859-1"), "utf-8");
						response.setContentType("application/vnd.ms-excel");
						response.setHeader("Content-disposition",
								"attachment;filename="
										+ new String(fileName.getBytes(), "utf-8"));
						// orders = this.orderService.getExportOrders(sdate, edate);

						File templeFile = new File(request.getSession()
								.getServletContext().getRealPath("/")
								+ this.meiseanumberresulttempletsFile);
						 os = response.getOutputStream();
						 SeaNumberUtil.exportOrderStateToResult(importSeaNumber, templeFile, os);
						
						
						
						

					} catch (Exception e) {
						//log.error("获取海关单数据失败", e);
						responseString.setCode(ResponseCode.SHOW_EXCEPTION);
						responseString.setMessage("获取导出海关运单数据出现异常，无法获取数据");
						return responseString.toString();
						//throw new Exception("获取导出海关运单数据出现异常，无法获取数据", e);
					} finally {
					
						if (os != null) {
							try {
								os.flush();
								os.close();
							} catch (IOException e) {
								// ignore
							}
						}
					}
						
							
						
							
						
				
				} catch (Exception e) {
					log.error("修改数据库失败", e);
					responseString.setCode(ResponseCode.SHOW_EXCEPTION);
					responseString.setMessage("修改数据库失败,原因" + e.getMessage());
					return responseString.toString();
				//	return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					//		"修改数据库失败,原因" + e.getMessage());
				}
			} else {
				//log.error("修改数据库失败", e);
				responseString.setCode(ResponseCode.PARAMETER_ERROR);
				responseString.setMessage("文件内容不能为空,请检查是否有空行或运单号为空!");
				return responseString.toString();
				//return generateResponseObject(ResponseCode.PARAMETER_ERROR,
				//		"文件内容不能为空,请检查是否有空行或运单号为空!");
			}
		}
		responseString.setCode(ResponseCode.PARAMETER_ERROR);
		responseString.setMessage("文件不能为空");
		return responseString.toString();
		//return generateResponseObject(ResponseCode.PARAMETER_ERROR, "文件不能为空");
	}

	
	
	//获取海关单号已使用和没使用的数量
	@RequestMapping(value = "/admin/seanumber/getstateno", method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> getthenumberforseanumber(HttpServletRequest request) {

		
		
		
		
		
		try {
			SeaNumberNo no=new SeaNumberNo();
			no.setAvailableNo(this.seaNumberDao.getavailablenumbers());
			no.setUnavailableNo(this.seaNumberDao.getunavailablenumbers());
			ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			obj.setData(no);
			return obj;
		} catch (Exception e) {
			
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取海关单号数量发生异常");
		}
	}
	
}

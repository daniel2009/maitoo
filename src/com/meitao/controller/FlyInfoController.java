package com.meitao.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;



import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.common.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.meitao.service.FlyInfoService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.FlyInfoUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.common.util.UserUtil;
import com.meitao.model.FlyInfo;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.temp.ImportFlyInfo;


@Controller
public class FlyInfoController extends BasicController {

	private static final long serialVersionUID = 1058971052693785202L;
	private static final Logger log = Logger.getLogger(FlyInfoController.class);

	@Resource(name = "flyinfoService")
	public FlyInfoService flyinfoService;
	@Value(value = "${page_size}")
	private int defaultPageSize;
	@Value(value = "${default_img_type}")
	private String defaultCardFileType;

	@Value(value = "${sava_global_pic_dir}")
	private String saveGlobalDir;
	@Value(value = "${default_img_size}")
	private long defaultCardFileSize;
	
	@Value(value = "${default_excel_type}")
	private String defaultExcelFileType;
	
	@Value("${flyno.output.to.updatestate.templets}")
	private String flynooutputtoupdatestatetemplets;
	
	@Value(value = "${order.import.flightexampleTemplets}")
	private String orderImportflightexampleTempletsFile;


	@RequestMapping(value = "/admin/flyinfo/add", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> addFlyInfo(HttpServletRequest request,
			FlyInfo flyinfo) {

		String supperadmin = (String) request.getSession().getAttribute(
				Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		String storeid=null;
		if ((supperadmin == null) || (!supperadmin.equalsIgnoreCase("1"))) {
			
			storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
			if((storeid==null)||(storeid.equalsIgnoreCase("")))
			{
				return generateResponseObject(ResponseCode.NEED_LOGIN,
						"你没有登陆!");
			}
			
			

		}

		if (flyinfo == null) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数无效");
		}

		if (flyinfo.getFlightno() == null) {
			return generateResponseObject(ResponseCode.WAREHOUSE_ADDRESS_ERROR,
					"航班号不能为空，请重新输入！");
		} else {
			if (flyinfo.getFlightno().equalsIgnoreCase("")) {
				return generateResponseObject(
						ResponseCode.WAREHOUSE_ADDRESS_ERROR, "航班号不能为空，请重新输入！");
			}
		}
		if (flyinfo.getState() == null) {
			return generateResponseObject(ResponseCode.WAREHOUSE_ADDRESS_ERROR,
					"状态错误，请重新输入！");
		} else {
			if (flyinfo.getState().equalsIgnoreCase("")) {
				return generateResponseObject(
						ResponseCode.WAREHOUSE_ADDRESS_ERROR, "状态错误，请重新输入！");
			}
		}

		try {
			// return this.warehouseService.addWarehouse(house);
			storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
			flyinfo.setWarehouseId(storeid);
			
			return this.flyinfoService.addFlyInfo(flyinfo);

		} catch (Exception e) {
			log.error("添加仓库失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					"添加航班出现象异常");
		}
	}
	@RequestMapping(value = "/admin/flyinfo/search", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public ResponseObject<PageSplit<FlyInfo>> searchByKeyOfAdmin(
			HttpServletRequest request,
	        @RequestParam(value = "flyno", required = false, defaultValue = "") String flyon,
	        @RequestParam(value = "key", required = false) String key,
	        @RequestParam(value = "state", required = false) String state,
	        @RequestParam(value = "sd", required = false) String sdate,
	        @RequestParam(value = "ed", required = false) String edate,	        
	       // @RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex
			@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
			@RequestParam(value = "rows", required = false, defaultValue = "10") int rows
			) {	
		try {
			if(!StringUtil.isEmpty(state)&&(state.equalsIgnoreCase("-1")))
			{
				state=null;
			}

			String storeid=null;
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
			{
				storeid=null;//表示可以查找所有门店
				
			}else
			{
				storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
				if((storeid==null)||(storeid.equalsIgnoreCase("")))
				{
					return generateResponseObject(ResponseCode.NEED_LOGIN,
							"你没有登陆!");
				}
				
				String storeMaster=(String)request.getSession().getAttribute(Constant.EMP_STORE_MASTER_SESSION_KEY);//1表示是店长
			
				if(!StringUtil.isEmpty(storeMaster)&&(storeMaster.equalsIgnoreCase("1")))//店长
				{
					
				}
				else
				{
					return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，只有店长的管理员有权查看航班信息!");
				}
			}
			
			
			if((key!=null)&&(!key.equalsIgnoreCase("")))
			{
			key =  new String(key.getBytes("ISO-8859-1"), "utf-8");
			}
			if (StringUtil.isEmpty(sdate) || !UserUtil.validateExportDate(sdate)) {
				sdate = "";
			} else {
				sdate = UserUtil.transformerDateString(sdate, " 00:00:00");
			}

			if (StringUtil.isEmpty(edate) || !UserUtil.validateExportDate(edate)) {
				edate = "";
			} else {
				edate = UserUtil.transformerDateString(edate, " 23:59:59");
			}

			flyon = StringUtil.isEmpty(flyon) ? null : flyon;
			state = StringUtil.isEmpty(state) ? null : state;
			return this.flyinfoService.searchFlyInfo(flyon, key, sdate, edate, state,defaultPageSize, pageIndex,storeid);
			
		} catch (Exception e) {
			log.error("查询航班信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "查询失败");
		}
	}
	


	@RequestMapping(value = "/admin/flyinfo/del", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> deleteWarehouse(HttpServletRequest request,@RequestParam(value = "id") String id) {
		try {
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			String storeid=null;

			if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
			{
				
				
				storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
				if((storeid==null)||(storeid.equalsIgnoreCase("")))
				{
					return generateResponseObject(ResponseCode.NEED_LOGIN,
							"你没有登陆!");
				}
				else
				{
					ResponseObject<FlyInfo> info=this.flyinfoService.getbyid(id);
					if((info!=null)&&(ResponseCode.SUCCESS_CODE.equalsIgnoreCase(info.getCode())))
					{
						if(storeid.equalsIgnoreCase(info.getData().getWarehouseId()))
						{
							
						}
						else
						{
							return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权删除航班信息!只能由高级管理员或"+info.getData().getWarehouseName()+"修改！");
						}
					}
					else
					{
						return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，获取航班信息失败!");
					}
				}
			
				
				
			}
			return this.flyinfoService.deleteFlyInfoById(id);
		} catch (Exception e) {
			log.error("删除仓库失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "根据id删除仓库出现异常");
		}
	}
	
	
	
	//kai 20160331 添加航号
	@RequestMapping(value = "/admin/flyinfo/add_morder", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> addFlyforMorder(HttpServletRequest request,
			FlyInfo flyinfo) {

		String supperadmin = (String) request.getSession().getAttribute(
				Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		String storeid=null;
		if ((supperadmin == null) || (!supperadmin.equalsIgnoreCase("1"))) {
			
			storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
			if((storeid==null)||(storeid.equalsIgnoreCase("")))
			{
				return generateResponseObject(ResponseCode.NEED_LOGIN,
						"你没有登陆!");
			}
			
			String storeMaster=(String)request.getSession().getAttribute(Constant.EMP_STORE_MASTER_SESSION_KEY);//1表示是店长
			
			if(!StringUtil.isEmpty(storeMaster)&&(storeMaster.equalsIgnoreCase("1")))//店长
			{
				
			}
			else
			{
				return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，只有店长的管理员有权添加航班信息!");
			}

		}

		if (flyinfo == null) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数无效");
		}

		if (flyinfo.getFlightno() == null) {
			return generateResponseObject(ResponseCode.WAREHOUSE_ADDRESS_ERROR,
					"航班号不能为空，请重新输入！");
		} else {
			if (flyinfo.getFlightno().equalsIgnoreCase("")) {
				return generateResponseObject(
						ResponseCode.WAREHOUSE_ADDRESS_ERROR, "航班号不能为空，请重新输入！");
			}
		}
		if (flyinfo.getState() == null) {
			return generateResponseObject(ResponseCode.WAREHOUSE_ADDRESS_ERROR,
					"状态错误，请重新输入！");
		} else {
			if (flyinfo.getState().equalsIgnoreCase("")) {
				return generateResponseObject(
						ResponseCode.WAREHOUSE_ADDRESS_ERROR, "状态错误，请重新输入！");
			}
		}

		try {
			// return this.warehouseService.addWarehouse(house);
			storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
			flyinfo.setWarehouseId(storeid);
			
			return this.flyinfoService.addFlyForMorder(flyinfo);

		} catch (Exception e) {
			log.error("添加仓库失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					"添加航班出现象异常");
		}
	}
	
	
	// 下载订单批量上传的模板
			@RequestMapping(value = "/admin/flyinfo/download_flight_example", method = { RequestMethod.GET })
			public void getImportflightexampleDataExcelFile(HttpServletRequest request,
					HttpServletResponse response) {
				InputStream input = null;
				ServletOutputStream os=null;
				try {
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-disposition", "attachment;filename="
							+ new String("fly_upload_example.xls".getBytes(),
									"utf-8"));
					
					input = request
							.getSession()
							.getServletContext()
							.getResourceAsStream(
									this.orderImportflightexampleTempletsFile);
					os = response.getOutputStream();
					byte[] buffer = new byte[1024];
					int n = 0;
					while ((n = input.read(buffer)) > 0) {
						os.write(buffer, 0, n);
					}
					buffer=null;
					os.flush();
				} catch (Exception e) {
					log.error("下载文件失败", e);
				} finally {
					if (input != null) {
						try {
							input.close();
						} catch (IOException e) {
							// ignore
						}
					}
					if (os != null) {
						try {
							os.flush();
							os.close();
						} catch (IOException e) {
							// ignore
						}
					}
				}
			}
			
			@RequestMapping(value = "/admin/flyinfo/upload_file", method = { RequestMethod.POST })
			@ResponseBody
			public ResponseObject<Object> uploadexcel(HttpServletRequest request,
					HttpServletResponse response,
					@RequestParam(value = "s_id", required = false, defaultValue = "") String id,
					@RequestParam(value = "file", required = false) MultipartFile file) {
				try {

					if(StringUtil.isEmpty(id))
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错");
					}
				
					
					
					String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
					String storeid=null;

					if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
					{
						
						
						storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
						if((storeid==null)||(storeid.equalsIgnoreCase("")))
						{
							return generateResponseObject(ResponseCode.NEED_LOGIN,
									"你没有登陆!");
						}
						else
						{
							ResponseObject<FlyInfo> info=this.flyinfoService.getbyid(id);
							if((info!=null)&&(ResponseCode.SUCCESS_CODE.equalsIgnoreCase(info.getCode())))
							{
								if(storeid.equalsIgnoreCase(info.getData().getWarehouseId()))
								{
									
								}
								else
								{
									return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权修改航班信息!只能由高级管理员或"+info.getData().getWarehouseName()+"修改！");
								}
							}
							else
							{
								return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，获取航班信息失败!");
							}
						}
					
						
						
					}
					//List<String> importOrderids = null;
					List<ImportFlyInfo> importFlyInfo=null;
					if (file != null && file.getSize() > 0) {
						
						try {
							
							//kai 20151006 判定是不是excel表格
							String originalName = file.getOriginalFilename();
							if (!StringUtil.boolpicisgoodornot(originalName, defaultExcelFileType)) {
								return generateResponseObject(
										ResponseCode.CONSIGNEE_CARD_ERROR, "必须上传excel 2003表格,请重新尝试!");
							}
							
							
						//	importOrderids = FlyInfoUtil.readOrderExcel_weiyi_state(file
							//		.getInputStream());
							
							importFlyInfo = FlyInfoUtil.readOrderExcel_flyno(file
											.getInputStream());
						} catch (OutOfMemoryError e) {
							log.error("内存不够", e);
							return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
									"内存不够");
						} catch (Exception e) {
							log.error("读取数据出错", e);
							return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
									"读取数据出错");
						}
					
						if((importFlyInfo!=null)&&(importFlyInfo.size()<1))
						{
							
							return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "读取表格数据失败");
						}
					
						FlyInfo flyinfo=new FlyInfo();
						flyinfo.setId(id);;
						
						
						
						String empName = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ACCOUNT_SESSION_KEY));
						
						ResponseObject<Object> obj=this.flyinfoService.modifyFlyInfoByfile(flyinfo, empName, importFlyInfo, storeid);
						//ResponseObject<Object> obj=this.flyinfoService.modifyFlyInfoWithExcelForMorder(flyinfo, order_flag, empName, importFlyInfo, sendmessage, storeid);
						if((obj!=null)&&ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj.getCode()))
						{
							
							importFlyInfo=null;
							if(obj.getData()!=null)
							{
								importFlyInfo=(List<ImportFlyInfo>)obj.getData();
							}
							else
							{
								return generateResponseObject(ResponseCode.WAREHOUSE_ADDRESS_ERROR,
										"返回数据错误！");
							}
							OutputStream os = null;
							try {
								

								if((importFlyInfo==null)||(importFlyInfo.size()<1))
								{
									return generateResponseObject(ResponseCode.WAREHOUSE_ADDRESS_ERROR,
											"返回数据错误！");
								}
								/*
								 * for (ExportOrder o : result.getData()) {
								 * 
								 * orders.add(o); }
								 */
							
								String fileName = "flyno_update_list_" + importFlyInfo.size() + ".xls";
								// key = new String(key.getBytes("ISO-8859-1"), "utf-8");
								response.setContentType("application/vnd.ms-excel");
								response.setHeader("Content-disposition",
										"attachment;filename="
												+ new String(fileName.getBytes(), "utf-8"));
							
								File templeFile;
								templeFile = new File(request.getSession()
										.getServletContext().getRealPath("/")
										+ this.flynooutputtoupdatestatetemplets);
								
								os = response.getOutputStream();
								FlyInfoUtil.export_flyno_state_result(importFlyInfo, templeFile, os);
								//TranshipmentUtil.export_tranorder_list(datas, templeFile, os);
								
								//return new ResponseObject<Map<String, String>>(
								//		obj.getCode(), obj.getMessage());
								return obj;

							} catch (Exception e) {
								log.error("获取运单数据失败", e);
								//throw new Exception("获取导出运单数据出现异常，无法获取数据", e);
								return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"获取导出运单数据出现异常，无法获取数据");
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
						}
						else
						{
							return obj;
						}
						
					}
					else//没有包含上传文件的修改
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"必须包含上传文件!");
					}
					


				
					
					
					//return new ResponseObject<Object>();
				} catch (Exception e) {
					log.error("提交航班信息失败", e);
					return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "操作异常，"+e.getMessage());
				}
				
			}
			@RequestMapping(value = "/admin/flyinfo/modify_morder", method = { RequestMethod.POST })
			@ResponseBody
			public ResponseObject<Object> modifyflyno(HttpServletRequest request,
					HttpServletResponse response,
					@RequestParam(value = "id", required = false, defaultValue = "") String id,
			        @RequestParam(value = "state", required = false) String state,
			        @RequestParam(value = "modifystate", required = false) String order_flag,
			        @RequestParam(value = "remark", required = false) String remark,
			        @RequestParam(value = "sendmessage", required = false) String sendmessage,//更新状态短信通知
			        @RequestParam(value = "sendmessage_cardId", required = false) String sendmessage_cardId) {//提醒用户提交身份证信息
				try {
					if(StringUtil.isEmpty(id))
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错");
					}
					
					
					String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
					String storeid=null;

					if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
					{
						
						
						storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
						if((storeid==null)||(storeid.equalsIgnoreCase("")))
						{
							return generateResponseObject(ResponseCode.NEED_LOGIN,
									"你没有登陆!");
						}
						else
						{
							ResponseObject<FlyInfo> info=this.flyinfoService.getbyid(id);
							if((info!=null)&&(ResponseCode.SUCCESS_CODE.equalsIgnoreCase(info.getCode())))
							{
								if(storeid.equalsIgnoreCase(info.getData().getWarehouseId()))
								{
									
								}
								else
								{
									return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权修改航班信息!只能由高级管理员或"+info.getData().getWarehouseName()+"修改！");
								}
							}
							else
							{
								return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，获取航班信息失败!");
							}
						}
					
						
						
					}
					
					FlyInfo flyinfo=new FlyInfo();
					flyinfo.setId(id);
					flyinfo.setRemark(remark);
					flyinfo.setState(state);
					
					if (flyinfo.getId() == null) {
						return generateResponseObject(ResponseCode.PARAMETER_ERROR,
								"参数出错，id不能为空，请重新输入！");
					} else {
						if (flyinfo.getId().equalsIgnoreCase("")) {
							return generateResponseObject(
									ResponseCode.PARAMETER_ERROR, "参数出错，id不能为空，请重新输入！");
						}
					}
					if (flyinfo.getState() == null||(flyinfo.getState().equalsIgnoreCase("-1"))) {
						return generateResponseObject(ResponseCode.PARAMETER_ERROR,
								"状态错误，请重新输入！");
					} else {
						if (flyinfo.getState().equalsIgnoreCase("")) {
							return generateResponseObject(
									ResponseCode.PARAMETER_ERROR, "状态错误，请重新输入！");
						}
					}
					String empName = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ACCOUNT_SESSION_KEY));
					return this.flyinfoService.modifyFlyInfoMorder(flyinfo, order_flag, sendmessage,sendmessage_cardId, empName, storeid);
				
					
					


				} catch (Exception e) {
					log.error("提交航班信息失败", e);
					return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "操作异常，"+e.getMessage());
				}
				
			}


}

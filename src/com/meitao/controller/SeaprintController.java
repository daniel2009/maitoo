//建立海关打印批次管理相关控制层
package com.meitao.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;

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


import com.meitao.service.SeaprintService;

import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ParameterConstants;
import com.meitao.common.constants.ResponseCode;

import com.meitao.common.util.SeaprintUtil;
import com.meitao.common.util.StringUtil;

import com.meitao.model.CardId;

import com.meitao.model.PageSplit;
import com.meitao.model.Porder;
import com.meitao.model.ResponseObject;
import com.meitao.model.Seaprint;


@Controller
public class SeaprintController extends BasicController {

	private static final long serialVersionUID = 1058971052693785202L;
	private static final Logger log = Logger
			.getLogger(SeaprintController.class);

	@Resource(name = "seaprintService")
	public SeaprintService seaprintService;

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
	
	
	@Value("${cardid.output.to.result.templets}")
	private String cardidoutputtoresulttemplets;
	
	@Value("${porder.output.to.result.templets}")
	private String porderoutputtoresulttemplets;
	
	@Value("${porder.output.to.list.templets}")
	private String porderoutputtolisttemplets;
	
	
	@Value(value = "${porder.import.form.templets}")
	private String porderimportformTempletsFile;

	@Value(value = "${cardid.import.form.templets}")
	private String cardidimportformTempletsFile;
	
	@RequestMapping(value = "/admin/seaprint/add", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> addSeaprint(HttpServletRequest request,
			Seaprint seaprint) {

		String supperadmin = (String) request.getSession().getAttribute(
				Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		String storeid = null;
		if ((supperadmin == null) || (!supperadmin.equalsIgnoreCase("1"))) {

			storeid = (String) request.getSession().getAttribute(
					Constant.EMP_STORE_ID_SESSION_KEY);
			if ((storeid == null) || (storeid.equalsIgnoreCase(""))) {
				return generateResponseObject(ResponseCode.NEED_LOGIN, "你没有登陆!");
			}

		}

		if (seaprint == null) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数无效");
		}

		if (StringUtil.isEmpty(seaprint.getSeaprintno())) {
			return generateResponseObject(ResponseCode.WAREHOUSE_ADDRESS_ERROR,
					"海关批次号不能为空，请重新输入！");
		}

		try {
			// return this.warehouseService.addWarehouse(house);
			storeid = (String) request.getSession().getAttribute(
					Constant.EMP_STORE_ID_SESSION_KEY);
			seaprint.setWarehouseId(storeid);

			return this.seaprintService.addSeaprint(seaprint);

		} catch (Exception e) {
			log.error("添加仓库失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					"添加仓库出现异常");
		}
	}

	// 搜索批次
	@RequestMapping(value = "/admin/seaprint/searchlist", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseObject<PageSplit<Seaprint>> searchByKeyOfSeaprint(
			HttpServletRequest request,
			@RequestParam(value = "seaprintno", required = false) String seaprintno,
			@RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex) {
		try {

			String storeid = null;
			String supperadmin = (String) request.getSession().getAttribute(
					Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			if ((supperadmin != null) && (supperadmin.equalsIgnoreCase("1"))) {
				storeid = null;// 表示可以查找所有门店

			} else {
				storeid = (String) request.getSession().getAttribute(
						Constant.EMP_STORE_ID_SESSION_KEY);
				if ((storeid == null) || (storeid.equalsIgnoreCase(""))) {
					return generateResponseObject(ResponseCode.NEED_LOGIN,
							"你没有登陆!");
				}
			}

			return this.seaprintService.searchSeaprint(seaprintno,
					defaultPageSize, pageIndex, storeid);

			// return this.flyinfoService.searchFlyInfo(flyon, key, sdate,
			// edate, state,defaultPageSize, pageIndex,storeid);

		} catch (Exception e) {
			log.error("查询打印批次信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "查询失败");
		}
	}

	@RequestMapping(value = "/admin/seaprint/get_one", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> getseaprintone(HttpServletRequest request,
			@RequestParam(value = "id", required = false) String id) {
		try {

			String storeid = null;
			String supperadmin = (String) request.getSession().getAttribute(
					Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			if ((supperadmin != null) && (supperadmin.equalsIgnoreCase("1"))) {
				storeid = null;// 表示可以查找所有门店

			} else {
				storeid = (String) request.getSession().getAttribute(
						Constant.EMP_STORE_ID_SESSION_KEY);
				if ((storeid == null) || (storeid.equalsIgnoreCase(""))) {
					return generateResponseObject(ResponseCode.NEED_LOGIN,
							"你没有登陆!");
				}
			}

			return this.seaprintService.getbyid(id, storeid);

		} catch (Exception e) {
			log.error("查询打印批次信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "查询失败");
		}
	}

	@RequestMapping(value = "/admin/seaprint/del", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> deleteseaprint(HttpServletRequest request,
			@RequestParam(value = "id") String id) {
		try {
			String supperadmin = (String) request.getSession().getAttribute(
					Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			String storeid = null;

			if ((supperadmin == null) || (!supperadmin.equalsIgnoreCase("1"))) {

				storeid = (String) request.getSession().getAttribute(
						Constant.EMP_STORE_ID_SESSION_KEY);
				if ((storeid == null) || (storeid.equalsIgnoreCase(""))) {
					return generateResponseObject(ResponseCode.NEED_LOGIN,
							"你没有登陆!");
				} else {

				}

			}
			ResponseObject<Object> info = this.seaprintService.getbyid(id,
					storeid);
			if ((info != null)
					&& (ResponseCode.SUCCESS_CODE.equalsIgnoreCase(info
							.getCode())) && (info.getData() != null)) {

			} else {
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
						"获取批此信息失败，可能存在或不属于本门店!");
			}
			return this.seaprintService.deleteSeaprintById(id);
		} catch (Exception e) {
			log.error("删除仓库失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					"根据id删除仓库出现异常");
		}
	}

	// 身体份证导入表格，目前只开放高级管理员
	@RequestMapping(value = "/admin/cardid/import", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> importOrderDataFromWeiyiStateExcel(
			HttpServletResponse response, HttpServletRequest request,
			MultipartFile file) {

		//String wid = null;
		String supperadmin = (String) request.getSession().getAttribute(
				Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);

		if ((supperadmin == null) || (!supperadmin.equalsIgnoreCase("1"))) {

			return generateResponseObject(
					ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权进行此操作或没登陆!");

		}
		if (file != null && file.getSize() > 0) {
			List<CardId> cardids = null;
			try {

				// kai 20151006 判定是不是excel表格
				String originalName = file.getOriginalFilename();
				if (!StringUtil.boolpicisgoodornot(originalName,
						defaultExcelFileType)) {
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR,
							"必须上传excel 2003表格,请重新尝试!");
				}

				cardids=SeaprintUtil.readcardidExcel(file.getInputStream());// 读取身份证信息

			} catch (OutOfMemoryError e) {
				log.error("内存不够", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
						"内存不够," + e.getMessage());
			} catch (Exception e) {
				log.error("读取数据出错", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
						"读取出错," + e.getMessage());
			}
			if (!cardids.isEmpty()) {
				try {
					// 导入员工名称
					String empName = StringUtil.obj2String(request.getSession()
							.getAttribute(Constant.EMP_ACCOUNT_SESSION_KEY));
					ResponseObject<Object> obj = this.seaprintService
							.importcardids(cardids);
					if (ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj
							.getCode())) {

						cardids = (List<CardId>) obj.getData();
						if ((cardids == null) || (cardids.size() < 1)) {
							return obj;
						}

						OutputStream os = null;

						try {

							String fileName = "cardid_import_result_"
									+ cardids.size() + ".xls";
							// key = new String(key.getBytes("ISO-8859-1"),
							// "utf-8");
							response.setContentType("application/vnd.ms-excel");
							response.setHeader("Content-disposition",
									"attachment;filename="
											+ new String(fileName.getBytes(),
													"utf-8"));
							// orders = this.orderService.getExportOrders(sdate,
							// edate);

							File templeFile = new File(request.getSession()
									.getServletContext().getRealPath("/")
									+ this.cardidoutputtoresulttemplets);
							os = response.getOutputStream();

							SeaprintUtil.exportcardidsToResult(cardids,
									templeFile, os);

							

						} catch (Exception e) {
							log.error("获取运单数据失败", e);
							throw new Exception("获取导出运单数据出现异常，无法获取数据", e);
						} finally {

							if (os != null) {
								try {
									os.close();
								} catch (IOException e) {
									// ignore
								}
							}
						}

					} else {
						return obj;
					}

				} catch (Exception e) {
					log.error("读取身份证信息失败", e);
					return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
							"修改数据库失败,原因" + e.getMessage());
				}
			} else {
				return generateResponseObject(ResponseCode.PARAMETER_ERROR,
						"文件内容不能为空,请检查!");
			}
		}
		return generateResponseObject(ResponseCode.PARAMETER_ERROR, "文件不能为空");
	}

	
	
	   //导入海关打单列表
		@RequestMapping(value = "/admin/seaprint/porder_form", method = { RequestMethod.POST })
		@ResponseBody
		public ResponseObject<Object> importporderform(
				HttpServletRequest request,
				HttpServletResponse response,
				@RequestParam(value = "seaprintid", required = false) String seaprintid,
				MultipartFile file) {

			String storeid = null;
			String supperadmin = (String) request.getSession().getAttribute(
					Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			if ((supperadmin != null) && (supperadmin.equalsIgnoreCase("1"))) {
				storeid = null;// 表示可以查找所有门店

			} else {
				storeid = (String) request.getSession().getAttribute(
						Constant.EMP_STORE_ID_SESSION_KEY);
				if ((storeid == null) || (storeid.equalsIgnoreCase(""))) {
					return generateResponseObject(ResponseCode.NEED_LOGIN,
							"你没有登陆!");
				}else
				{
					ResponseObject<Object> obj=null;
					try{
					 obj=this.seaprintService.getbyid(seaprintid, storeid);
					}
					catch (Exception e) {
						
						return generateResponseObject("查找批次号出现异常!");
					}
					
					
					if(ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj.getCode()))
					{}
					else
					{
						return obj;
					}
				}
				
				
			}
			
			if (file != null && file.getSize() > 0) {
				List<Porder> Orders = null;
				try {
					//kai 20151006 判定是不是excel表格
					String originalName = file.getOriginalFilename();
					if (!StringUtil.boolpicisgoodornot(originalName, defaultExcelFileType)) {
						return generateResponseObject(
								ResponseCode.CONSIGNEE_CARD_ERROR, "必须上传excel 2003表格,请重新尝试!");
					}
					
					Orders = SeaprintUtil.readPorderExcel(file
							.getInputStream());
				} catch (OutOfMemoryError e) {
					log.error("内存不够", e);
					return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
							"内存不够");
				} catch (Exception e) {
					log.error("读取数据出错", e);
					String str = e.getMessage();// java.lang.RuntimeException:
					if ((str != null) && (!str.equalsIgnoreCase(""))) {
						str = str.replace("java.lang.RuntimeException:", "");
					}

					return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
							"读取数据出错原因:" + str);
				}

				try {
					Orders=SeaprintUtil.checkpordersbyself(Orders);
					//导出重复的运单
					int flag=0;
					for(Porder porder:Orders)
					{
						if(!StringUtil.isEmpty(porder.getRepeatflag())&&(porder.getRepeatflag().equalsIgnoreCase("1")))
						{
							flag=1;
							break;
						}
					}
					if(flag==1)//导出重复结果
					{

						OutputStream os = null;

						try {

							String fileName = "wrong_porders_"
									+ Orders.size() + ".xls";
							// key = new String(key.getBytes("ISO-8859-1"),
							// "utf-8");
							response.setContentType("application/vnd.ms-excel");
							response.setHeader("Content-disposition",
									"attachment;filename="
											+ new String(fileName.getBytes(),
													"utf-8"));
							// orders = this.orderService.getExportOrders(sdate,
							// edate);

							File templeFile = new File(request.getSession()
									.getServletContext().getRealPath("/")
									+ this.porderoutputtoresulttemplets);
							os = response.getOutputStream();

							SeaprintUtil.exportssesaprintwrong(Orders,
									templeFile, os);

							

						} catch (Exception e) {
							log.error("获取运单数据失败", e);
							//throw new Exception("获取导出运单数据出现异常，无法获取数据", e);
							return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
									"无法获取数据：" + e.getMessage());
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
						ResponseObject<Object> obj = this.seaprintService
								.importporders(Orders,seaprintid);
						
						if((obj!=null)&&(ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj.getCode())))
						{
							if(obj.getData()==null)
							{
								return obj;
							}
							 Orders = (List<Porder>)obj.getData();
							 if((Orders==null)||(Orders.size()<1))
							 {
								 return obj;
							 }
							 obj.setData(null);
							 obj=null;
							 OutputStream os = null;

								try {

									String fileName = "success_result_"
											+ Orders.size() + ".xls";
									// key = new String(key.getBytes("ISO-8859-1"),
									// "utf-8");
									response.setContentType("application/vnd.ms-excel");
									response.setHeader("Content-disposition",
											"attachment;filename="
													+ new String(fileName.getBytes(),
															"utf-8"));
									// orders = this.orderService.getExportOrders(sdate,
									// edate);

									File templeFile = new File(request.getSession()
											.getServletContext().getRealPath("/")
											+ this.porderoutputtoresulttemplets);
									os = response.getOutputStream();

									SeaprintUtil.exportporderinsertresult(Orders,
											templeFile, os);

									

								} catch (Exception e) {
									log.error("获取运单数据失败", e);
									//throw new Exception("获取导出运单数据出现异常，无法获取数据", e);
									return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
											"获取导出运单数据出现异常，无法获取数据：" + e.getMessage());
								} finally {
									Orders.clear();
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
						
					}
					
					
					return generateResponseObject(ResponseCode.SUCCESS_CODE);
				} catch (Exception e) {
					log.error("修改数据库失败", e);
					return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
							"提交失败：" + e.getMessage());
				}
			}
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "文件不能为空");
		}

		
		//搜索海关运单号
		@RequestMapping(value = "/admin/seaprint/porder", method = { RequestMethod.GET })
		@ResponseBody
		public ResponseObject<PageSplit<Porder>> searchByKeyOfAdmin(
				HttpServletRequest request,
				@RequestParam(value = "seaprintid", required = false, defaultValue = "") String seaprintid,
		        @RequestParam(value = "porder", required = false, defaultValue = "") String porder,
		        @RequestParam(value = "content", required = false, defaultValue = "") String content,
		        @RequestParam(value = "state", required = false) String state,
		        @RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex) {
			

			
			try {
				

				String storeid = null;
				String supperadmin = (String) request.getSession().getAttribute(
						Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
				if ((supperadmin != null) && (supperadmin.equalsIgnoreCase("1"))) {
					storeid = null;// 表示可以查找所有门店

				} else {
					storeid = (String) request.getSession().getAttribute(
							Constant.EMP_STORE_ID_SESSION_KEY);
					if ((storeid == null) || (storeid.equalsIgnoreCase(""))) {
						return generateResponseObject(ResponseCode.NEED_LOGIN,
								"你没有登陆!");
					}else
					{
						ResponseObject<Object> obj=null;
						try{
						 obj=this.seaprintService.getbyid(seaprintid, storeid);
						}
						catch (Exception e) {
							
							return generateResponseObject("查找批次号出现异常!");
						}
						
						
						if(ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj.getCode()))
						{}
						else
						{
							return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
									"没有查找到批次信息!");
						}
					}
					
					
				}
				
				
				if(StringUtil.isEmpty(state)||(state.equalsIgnoreCase("-1")))
				{
					state=null;
				}
				
				 content = URLDecoder.decode(content, "UTF-8");
					pageIndex = Math.max(pageIndex, 1);
					return this.seaprintService.searchPorders(seaprintid,porder, content, state,defaultPageSize, pageIndex, storeid);
					
				
			} catch (Exception e) {
				
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "查询失败");
			}
		}
		
		
		
		@RequestMapping(value = "/admin/seaprint/get_one_porder", method = {
				RequestMethod.GET, RequestMethod.POST })
		@ResponseBody
		public ResponseObject<Object> getseaprintoneporder(HttpServletRequest request,
				@RequestParam(value = "id", required = false) String id) {
			try {

				String storeid = null;
				String supperadmin = (String) request.getSession().getAttribute(
						Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
				if ((supperadmin != null) && (supperadmin.equalsIgnoreCase("1"))) {
					storeid = null;// 表示可以查找所有门店

				} else {
					storeid = (String) request.getSession().getAttribute(
							Constant.EMP_STORE_ID_SESSION_KEY);
					if ((storeid == null) || (storeid.equalsIgnoreCase(""))) {
						return generateResponseObject(ResponseCode.NEED_LOGIN,
								"你没有登陆!");
					}
				}

				return this.seaprintService.getporderbyid(id);

			} catch (Exception e) {
				log.error("查询打印批次信息失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "查询失败");
			}
		}
		
		//修改为打印状态
		@RequestMapping(value = "/admin/seaprint/modify_porder_state", method = {
				RequestMethod.GET, RequestMethod.POST })
		@ResponseBody
		public ResponseObject<Object> modifyporderstate(HttpServletRequest request,
				@RequestParam(value = "id", required = false) String id) {
			try {

				String storeid = null;
				String supperadmin = (String) request.getSession().getAttribute(
						Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
				if ((supperadmin != null) && (supperadmin.equalsIgnoreCase("1"))) {
					storeid = null;// 表示可以查找所有门店

				} else {
					storeid = (String) request.getSession().getAttribute(
							Constant.EMP_STORE_ID_SESSION_KEY);
					if ((storeid == null) || (storeid.equalsIgnoreCase(""))) {
						return generateResponseObject(ResponseCode.NEED_LOGIN,
								"你没有登陆!");
					}
				}

				return this.seaprintService.modifyporderstate(id, "1");

			} catch (Exception e) {
				log.error("更新运单状态失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "更新失败");
			}
		}
		
		
		//删除海关运单通过id
				@RequestMapping(value = "/admin/seaprint/delete_porder_byid", method = {
						RequestMethod.GET, RequestMethod.POST })
				@ResponseBody
				public ResponseObject<Object> deleteporderbyid(HttpServletRequest request,
						@RequestParam(value = "id", required = false) String id) {
					try {

						String storeid = null;
						String supperadmin = (String) request.getSession().getAttribute(
								Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
						if ((supperadmin != null) && (supperadmin.equalsIgnoreCase("1"))) {
							storeid = null;// 表示可以查找所有门店

						} else {
							storeid = (String) request.getSession().getAttribute(
									Constant.EMP_STORE_ID_SESSION_KEY);
							if ((storeid == null) || (storeid.equalsIgnoreCase(""))) {
								return generateResponseObject(ResponseCode.NEED_LOGIN,
										"你没有登陆!");
							}
						}

						return this.seaprintService.deleteporderbyid(id);

					} catch (Exception e) {
						log.error("更新运单状态失败", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "更新失败");
					}
				}
				
				
				//下载海关清单
				
				@RequestMapping(value = "/admin/seaprint/down_porderlist", method = { RequestMethod.GET,RequestMethod.POST })
				@ResponseBody
				public void downlisttemsOfAdmin(
						HttpServletRequest request,
						HttpServletResponse response,
						@RequestParam(value = "search_contect", required = false, defaultValue = "") String search_contect,
						@RequestParam(value = "seaprintid1", required = false, defaultValue = "") String seaprintid1,
						@RequestParam(value = "printflag", required = false, defaultValue = "") String printflag
					) {
					try {
						

						String storeid = null;
						String supperadmin = (String) request.getSession().getAttribute(
								Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
						if ((supperadmin != null) && (supperadmin.equalsIgnoreCase("1"))) {
							storeid = null;// 表示可以查找所有门店

						} else {
							storeid = (String) request.getSession().getAttribute(
									Constant.EMP_STORE_ID_SESSION_KEY);
							if ((storeid == null) || (storeid.equalsIgnoreCase(""))) {
							return;
							}else
							{
								ResponseObject<Object> obj=null;
								try{
								 obj=this.seaprintService.getbyid(seaprintid1, storeid);
								}
								catch (Exception e) {
									
									return;
								}
								
								
								if(ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj.getCode()))
								{}
								else
								{
									return ;
								}
							}
							
							
						}
						
						
						if(StringUtil.isEmpty(printflag)||(printflag.equalsIgnoreCase("-1")))
						{
							printflag=null;
						}
					
						ResponseObject<PageSplit<Porder>> obj= this.seaprintService.searchPorders(seaprintid1,"", search_contect, printflag,0x7fffffff, 1, null);
						
						if(ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj.getCode()))
						{
							
							PageSplit<Porder> pageSplit = null;
							pageSplit=obj.getData();
							List<Porder> datas=null;
							if(pageSplit!=null)
							{
								datas=pageSplit.getDatas();
							}
							else
							{
								/*return  new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
										"对不起，没有可下载的数据!");*/
								return;
							}
							OutputStream os = null;
							try {
								

								if((datas==null)||(datas.size()<1))
								{
									/*return new  ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
											"对不起，没有可下载的数据!");*/
									return;
								}
								String fileName = "download_result_"
										+ datas.size() + ".xls";
								// key = new String(key.getBytes("ISO-8859-1"),
								// "utf-8");
								response.setContentType("application/vnd.ms-excel");
								response.setHeader("Content-disposition",
										"attachment;filename="
												+ new String(fileName.getBytes(),
														"utf-8"));
								// orders = this.orderService.getExportOrders(sdate,
								// edate);

								File templeFile = new File(request.getSession()
										.getServletContext().getRealPath("/")
										+ this.porderoutputtolisttemplets);
								os = response.getOutputStream();

								SeaprintUtil.exportporders(datas,
										templeFile, os);

								
								
								

							} catch (Exception e) {
								log.error("获取运单数据失败", e);
								//return null;
								//throw new Exception("获取导出运单数据出现异常，无法获取数据", e);
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
							
						
					} catch (Exception e) {
						
						return ;
					}
				}
				
				
				//下载海关清单模板
				// 下载订单批量上传的模板
				@RequestMapping(value = "/admin/seaprint/download_porder_example", method = { RequestMethod.GET })
				public void getdownloadporderexamplesDataExcelFile(HttpServletRequest request,
						HttpServletResponse response) {
					InputStream input = null;
					ServletOutputStream os=null;
					try {
						response.setContentType("application/vnd.ms-excel");
						response.setHeader("Content-disposition", "attachment;filename="
								+ new String("upload_porders_example.xls".getBytes(),
										"utf-8"));
						input = request
								.getSession()
								.getServletContext()
								.getResourceAsStream(
										this.porderimportformTempletsFile);
						os = response.getOutputStream();
						byte[] buffer = new byte[1024];
						int n = 0;
						while ((n = input.read(buffer)) > 0) {
							os.write(buffer, 0, n);
						}
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
								os.close();
							} catch (IOException e) {
								// ignore
							}
						}
					}
				}
				
				///admin/cardid/download_cardid_example
				
				//下载海关清单模板
				// 下载身份证上传模版
				@RequestMapping(value = "/admin/cardid/download_cardid_example", method = { RequestMethod.GET })
				public void getcardiddownloadexampleDataExcelFile(HttpServletRequest request,
						HttpServletResponse response) {
					InputStream input = null;
					ServletOutputStream os=null;
					try {
						response.setContentType("application/vnd.ms-excel");
						response.setHeader("Content-disposition", "attachment;filename="
								+ new String("upload_cardids_example.xls".getBytes(),
										"utf-8"));
						input = request
								.getSession()
								.getServletContext()
								.getResourceAsStream(
										this.cardidimportformTempletsFile);
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
}

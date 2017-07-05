//建立海关打印批次管理相关控制层
package com.meitao.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;



import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meitao.common.filezip.basiczip;

import jxl.common.Logger;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;



import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ParameterConstants;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.ConsigneeInfoUtil;
import com.meitao.common.util.M_OrderUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.model.M_order;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.temp.ExportMorder;
import com.meitao.cardid.manage.CardId_lib;
import com.meitao.cardid.manage.filesearch;
import com.meitao.cardid.manage.import_t_orders;
import com.meitao.cardid.manage.importcardargs;
import com.meitao.cardid.manage.CardidManageControllerUtil;


@Controller
public class CardidManageController extends BasicController {

	private static final long serialVersionUID = 1058971052693785202L;
	private static final Logger log = Logger
			.getLogger(CardidManageController.class);



	@Value(value = "${page_size}")
	private int defaultPageSize;
	@Value(value = "${default_zip_file_type}")
	private String defaultZipFileType;

	@Value(value = "${sava_global_pic_dir}")
	private String saveGlobalDir;
	@Value(value = "${default_img_size}")
	private long defaultCardFileSize;
	
	@Value(value = "${default_file_size}")
	private long defaultFileSize;
	
	@Value(value = "${default_excel_type}")
	private String defaultExcelFileType;
	
	@Value(value = "${default_img_type}")
	private String defaultCardFileType;

	@Value("${flyno.output.to.updatestate.templets}")
	private String flynooutputtoupdatestatetemplets;
	
	
	@Value("${cardid.output.to.result.templets}")
	private String cardidoutputtoresulttemplets;
	
	@Value("${zip.cardids.export.form.templets}")
	private String zipcardidsexportformtemplets;
	
	
	
	@Value("${porder.output.to.result.templets}")
	private String porderoutputtoresulttemplets;
	
	@Value("${porder.output.to.list.templets}")
	private String porderoutputtolisttemplets;
	
	
	@Value("${cardid.down.word}")
	private String cardiddownword;
	
	
	@Value(value = "${porder.import.form.templets}")
	private String porderimportformTempletsFile;

	@Value(value = "${cardid.import.form.templets}")
	private String cardidimportformTempletsFile;
	
	@Value(value = "${meitao.export.sea.order.example.templets}")
	private String meitaoexportseaorderexampletemplets;
	
	@Value(value = "${meitao.neimenggu.import.sea.order.other.templets}")
	private String meitaoneimengguimportseaordertotheremplets;
	
	@Value(value = "${meitao.neimenggu.import.sea.order.templets}")
	private String meitaoneimengguimportseaordertemplets;
	
	@Value(value = "${meitao.shenzhen.import.sea.order.templets}")
	private String meitaoshenzhenimportseaordertemplets;
	
	@Value(value = "${meitao.shanghai.sea.order.mode.templets}")
	private String meitaoshanghaiseaordermodetemplets;
	
	
	@Value(value = "${save_cards_lib_dir}")
	private String saveCardsLibDir;
	
	@Value(value = "${pre_save_cards_lib_dir}")
	private String presaveCardsLibDir;
	
	@Resource(name = "cardidManageService")
	public com.meitao.service.CardidManageService cardidManageService;
	
	@Value(value = "${save_card_dir}")
	private String saveCardDir;
	
	// 批量导入身份证，只能以压缩包的形式导入
	@RequestMapping(value = "/admin/cardid/import_zip", method = { RequestMethod.POST,RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> importCardidFromZip(
			HttpServletResponse response, HttpServletRequest request,
			@RequestParam(value = "file", required = false) MultipartFile file) {
		
		String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
		{
		
		}
		else
		{
			String master = (String)request.getSession().getAttribute(Constant.EMP_STORE_MASTER_SESSION_KEY);
			if((master!=null)&&(master.equalsIgnoreCase("1")))
			{
				
			}
			else
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"对不起，只有高级管理员或店长能够导入身份证信息!");
			}
		}
		
		
		
		List<importcardargs> importargs = new ArrayList<importcardargs>();
		if (file != null && file.getSize() > 0) {
			if (file.getSize() > this.defaultFileSize) {
				return generateResponseObject(
						ResponseCode.CONSIGNEE_CARD_ERROR, "文件过大,请重新尝试！");
			}
			
			// 获取当时的时间缀
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyyMMddHHmmss");
			String timestr = sdf.format(date);
			
			String filetype = this.defaultZipFileType;// 要上传的文件类型
			String strtest = this.saveCardsLibDir;
			String strseparator = "";
			if (strtest.indexOf("/") >= 0)// 包含有“/”字符，分隔符用此字符
			{
				strseparator = "/";
			} else {
				strseparator = File.separator;
			}
			
			String fileNameother = null;
			String originalName = file.getOriginalFilename();
			
			if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
				return generateResponseObject(
						ResponseCode.CONSIGNEE_CARD_ERROR, "上传文件格式不对,请重新尝试!");
			}
			
			String midfilename="";
			int index = originalName.lastIndexOf('.');
			index = Math.max(index, 0);
			
			midfilename=timestr + "_"+ StringUtil.generateRandomInteger(5);
			
			fileNameother = this.saveCardsLibDir + strseparator +"temp"+strseparator+ midfilename
					+ originalName.substring(index);
			
			String unzipdir;
			File filezip;
			try {
				filezip = new File(request.getSession().getServletContext()
						.getRealPath("/")
						+ fileNameother);
				
				
				file.transferTo(filezip);
				
				
				unzipdir = request.getSession().getServletContext()
						.getRealPath("/")+this.saveCardsLibDir + strseparator +"temp"+ strseparator+midfilename+strseparator;
				
				basiczip.unZipFiles1(filezip, unzipdir);
				
				
				
				
				
				String[] typepic=this.defaultCardFileType.split(";");	
				File folder = new File(unzipdir);// 默认目录
				filesearch files=new filesearch();
				
				
				
				for(int i=0;i<typepic.length;i++)
				{
					String str1=typepic[i].toLowerCase();					
					File[] result = files.searchFile(folder, "."+str1);// 调用方法获得文件数组
					 for (int n = 0; n < result.length; n++) {// 循环显示文件
				           String filname= result[n].getName();
				           if(!StringUtil.isEmpty(filname))
				           {
				        	   filname=filname.trim();
				           }
				           
				           
				           importcardargs temp=new importcardargs();
				           temp.setAllfileurl(result[n].getAbsolutePath());
				           temp.setFilename(filname);
				           
				           int fileindex = filname.lastIndexOf('.');
				           if(fileindex<18)
				           {
				        	   temp.setFlag(false);
				        	   temp.setResult("失败！图片名称格式不对!");
				           }
				           else
				           {
					           filname=filname.substring(0, fileindex);
					           String id=filname.substring(0, 18);
					           String name=filname.substring(18, fileindex);
					           temp.setCardid(id);
					           temp.setCardname(name);
					           if (!ConsigneeInfoUtil.validateCardId(id)) {
					        	   temp.setResult("失败！身份证号不正确!");
					        	   temp.setFlag(false);
					   			}
					           else if(StringUtil.isEmpty(name))
					           {
					        	   temp.setResult("失败！姓名不能为空!");
					        	   temp.setFlag(false);
					           }
					           else
					           {
					        	   temp.setFlag(true);
					           }

				           }
				           
				           importargs.add(temp);
				           
					 }
				}
				//迁移图片到身份证库中
				for(importcardargs arg: importargs)
				{
					
					String saveurl = request.getSession().getServletContext()
							.getRealPath("/")+this.saveCardsLibDir+"/pics/"+arg.getFilename() ;
					if(arg.isFlag())//正确地查找到身份证信息
					{
						
						   FileInputStream fis = new FileInputStream(arg.getAllfileurl());
						   
					        FileOutputStream fos = new FileOutputStream(saveurl);
					 
					        int len = 0;
					        byte[] buf = new byte[1024];
					        while ((len = fis.read(buf)) != -1) {
					            fos.write(buf, 0, len);
					            fos.flush();
					        }
					        buf=null;
					        fos.flush();
					        fis.close();
					        fos.close();
					        fos=null;
					        fis=null;
					        arg.setSaveurl(this.saveCardsLibDir+"/pics/"+arg.getFilename());
					}
					//arg.setAllfileurl("");
				}
				deleteDir(new File(unzipdir));//删除解压后的临时文件
				//boolean success=file2.delete();//删除上传的文件
				if(filezip!=null)
				{
					//String aaa=filezip.getAbsolutePath();
					//filezip.
					//filezip=null;
					//doDeleteEmptyDir(aaa);
					filezip.deleteOnExit();
				}
				
			} catch (Exception e) {
				
				return generateResponseObject(
						ResponseCode.CONSIGNEE_CARD_ERROR,
						"操作失败!");
			}
		}
		
		
		
		
		
		if (importargs != null && importargs.size() >0) {
			
			
			
				try {

				
					OutputStream os = null;

					try {
						ResponseObject<Object> obj=this.cardidManageService.addcardids(importargs);
						if((obj!=null)&&(ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj.getCode())))
						{
							importargs=(List<importcardargs>)obj.getData();
							obj.setData(null);
							obj=null;
							String fileName = "cardids_zip_import_result_"
									+ importargs.size() + ".xls";
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
									+ this.zipcardidsexportformtemplets);
							os = response.getOutputStream();
							
						
	
							CardidManageControllerUtil.exportzipcardidsToResult(importargs, templeFile, os);
						}
						else
						{
							return obj;
						}
						

						

					} catch (Exception e) {
						
						//throw new Exception("获取导出运单数据出现异常，无法获取数据", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
								"修改数据库失败,原因" + e.getMessage());
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
					log.error("读取身份证信息失败", e);
					return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
							"修改数据库失败,原因" + e.getMessage());
				}
		
		}
		return generateResponseObject(ResponseCode.PARAMETER_ERROR, "查找图片为空!");
	}

	
	/**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    private  boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
//递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
    

	 
    
    
	@RequestMapping(value = "/admin/cardid_lib/searchbykey", method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public ResponseObject<PageSplit<CardId_lib>> searchByKeyOfAdmin(
			HttpServletRequest request,
	        @RequestParam(value = "searchInfo", required = false) String searchInfo,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
			@RequestParam(value = "rows", required = false, defaultValue = "10") int rows
	        ) {
		

		try {
			

			
		if(!StringUtil.isEmpty(searchInfo))
		{

			searchInfo = URLDecoder.decode(searchInfo, "UTF-8");
		}

				pageIndex = Math.max(pageIndex, 1);
				return this.cardidManageService.searchbykey(searchInfo,request, rows, pageIndex);
				
						
			
		} catch (Exception e) {
			log.error("查询身份证失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "查询失败");
		}
		
		
	}
	
	
	/*
	 * 修改文本类型
	 * 创建：kai 20151013
	 * */
	@RequestMapping(value = "/admin/cardid_lib/delone", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> deleteonecard(HttpServletRequest request,
			@RequestParam(value = "id") String id
	        ) {

		String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
		{
		
		}
		else
		{
			String master = (String)request.getSession().getAttribute(Constant.EMP_STORE_MASTER_SESSION_KEY);
			if((master!=null)&&(master.equalsIgnoreCase("1")))
			{
				
			}
			else
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"对不起，只有高级管理员或店长能够导入身份证信息!");
			}
		}
		if(id==""||id==null||id.equalsIgnoreCase(""))
		{
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数出错！");
		}

		
		try {	
			
			
		
			return this.cardidManageService.deleteonecard(id,request);
			
		} catch (Exception e) {
			log.error("删除图片出现异常!", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "删除图片出现异常!");
		}
	}



/*
 * 预先检查
 * 创建：kai 20160315
 * */
@RequestMapping(value = "/admin/cardid_lib/pre_check", method = { RequestMethod.POST, RequestMethod.GET })
@ResponseBody
public ResponseObject<Object> precheckfile(HttpServletRequest request) {
	try {	
		
		String strseparator = "";
		if (this.presaveCardsLibDir.indexOf("/") >= 0)// 包含有“/”字符，分隔符用此字符
		{
			strseparator = "/";
		} else {
			strseparator = File.separator;
		}
		
		String prefile = request.getSession().getServletContext()
				.getRealPath("/")+this.presaveCardsLibDir + strseparator +"pictures";
		
		String[] typepic=this.defaultCardFileType.split(";");	
		File folder = new File(prefile);// 默认目录
		
		if (!folder.exists())//如果文件夹不存在，直接返回错误
		{
			
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
					"没有检测到pictures文件夹!");
		}
		
	
		
		filesearch files=new filesearch();
		int picnum=0;
		for(int i=0;i<typepic.length;i++)
		{
			String str1=typepic[i].toLowerCase();					
			File[] result = files.searchFile(folder, "."+str1);// 调用方法获得文件数组
			 for (int n = 0; n < result.length; n++) {// 循环显示文件
		       
				 picnum=picnum+1;
		           
			 }
			 result=null;
		}
		
		if(picnum==0)
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
					"pictures文件夹中没有检测到类型为"+this.defaultCardFileType+"的图片!");
		}
		typepic=null;
		ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
		obj.setData(picnum);
		return obj;
		
	} catch (Exception e) {
		log.error("查找文件夹图片失败", e);
		return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "查找文件夹图片失败");
	}
}


// 扫描指定文件夹的图片并导入
@RequestMapping(value = "/admin/cardid_lib/import_scan", method = { RequestMethod.POST,RequestMethod.GET })
@ResponseBody
public ResponseObject<Object> scanimportCardidFromZip(HttpServletResponse response, HttpServletRequest request) {
	List<importcardargs> importargs = new ArrayList<importcardargs>();

	
	
	String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
	if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
	{
	
	}
	else
	{
		String master = (String)request.getSession().getAttribute(Constant.EMP_STORE_MASTER_SESSION_KEY);
		if((master!=null)&&(master.equalsIgnoreCase("1")))
		{
			
		}
		else
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"对不起，只有高级管理员或店长能够导入身份证信息!");
		}
	}
	
	
	String strseparator = "";
	if (this.presaveCardsLibDir.indexOf("/") >= 0)// 包含有“/”字符，分隔符用此字符
	{
		strseparator = "/";
	} else {
		strseparator = File.separator;
	}
	
	String prefile = request.getSession().getServletContext()
			.getRealPath("/")+this.presaveCardsLibDir + strseparator +"pictures";
	
	String[] typepic=this.defaultCardFileType.split(";");	
	File folder = new File(prefile);// 默认目录
	
	if (!folder.exists())//如果文件夹不存在，直接返回错误
	{
		
		return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
				"没有检测到pictures文件夹!");
	}
	

	
	filesearch files=new filesearch();
	//int picnum=0;
	for(int i=0;i<typepic.length;i++)
	{
		String str1=typepic[i].toLowerCase();					
		File[] result = files.searchFile(folder, "."+str1);// 调用方法获得文件数组
		 for (int n = 0; n < result.length; n++) {// 循环显示文件
	           String filname= result[n].getName();
	           if(!StringUtil.isEmpty(filname))
	           {
	        	   filname=filname.trim();
	           }
	           importcardargs temp=new importcardargs();
	           temp.setAllfileurl(result[n].getAbsolutePath());
	           temp.setFilename(filname);
	           
	           int fileindex = filname.lastIndexOf('.');
	           if(fileindex<18)
	           {
	        	   temp.setFlag(false);
	        	   temp.setResult("失败！图片名称格式不对!");
	           }
	           else
	           {
		           filname=filname.substring(0, fileindex);
		           String id=filname.substring(0, 18);
		           String name=filname.substring(18, fileindex);
		           temp.setCardid(id);
		           temp.setCardname(name);
		           if (!ConsigneeInfoUtil.validateCardId(id)) {
		        	   temp.setResult("失败！身份证号不正确!");
		        	   temp.setFlag(false);
		   			}
		           else if(StringUtil.isEmpty(name))
		           {
		        	   temp.setResult("失败！姓名不能为空!");
		        	   temp.setFlag(false);
		           }
		           else
		           {
		        	   temp.setFlag(true);
		           }

	           }
	           
	           importargs.add(temp);
	           
		 }
		 result=null;
	}
	
	
	
	//迁移图片到身份证库中
	try
	{
		for(importcardargs arg: importargs)
		{
			
			String saveurl = request.getSession().getServletContext()
					.getRealPath("/")+this.saveCardsLibDir+"/pics/"+arg.getFilename() ;
			if(arg.isFlag())//正确地查找到身份证信息
			{
				
				   FileInputStream fis = new FileInputStream(arg.getAllfileurl());
				   
			        FileOutputStream fos = new FileOutputStream(saveurl);
			 
			        int len = 0;
			        byte[] buf = new byte[1024];
			        while ((len = fis.read(buf)) != -1) {
			            fos.write(buf, 0, len);
			            fos.flush();
			        }
			        fis.close();
			        fos.flush();
			        fos.close();
			        fos=null;
			        fis=null;
			        arg.setSaveurl(this.saveCardsLibDir+"/pics/"+arg.getFilename());
			}
			//arg.setAllfileurl("");
		}
		//deleteDir(new File(prefile));//删除解压后的临时文件
	}
	catch(Exception e)
	{
		return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
				"迁移图片出现异常,原因" + e.getMessage());
	}
	
	if (importargs != null && importargs.size() >0) {
		
		
		
			try {

			
				OutputStream os = null;

				try {
					ResponseObject<Object> obj=this.cardidManageService.addcardids(importargs);
					if((obj!=null)&&(ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj.getCode())))
					{
						importargs=(List<importcardargs>)obj.getData();
						obj.setData(null);
						obj=null;
						String fileName = "cardids_zip_import_result_"
								+ importargs.size() + ".xls";
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
								+ this.zipcardidsexportformtemplets);
						os = response.getOutputStream();
						
					

						CardidManageControllerUtil.exportzipcardidsToResult(importargs, templeFile, os);
					}
					else
					{
						return obj;
					}
					

					

				} catch (Exception e) {
					log.error("获取运单数据失败", e);
					return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
							"修改数据库失败,原因" + e.getMessage());
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
				log.error("读取身份证信息失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
						"修改数据库失败,原因" + e.getMessage());
			}
	
	}
	return generateResponseObject(ResponseCode.PARAMETER_ERROR, "查找图片为空!");
}


//匹配身份证信息并返回kai 20161503

@RequestMapping(value = "/admin/CardId_lib/import_table_meitao", method = { RequestMethod.POST })
@ResponseBody
public ResponseObject<Object> importOrderFromWeiyiExcel(
		HttpServletRequest request,
		HttpServletResponse response, 
		@RequestParam(value = "inputmode", required = false) String inputmode,//导出模板
		@RequestParam(value = "inputrude", required = false) String inputrude,//匹配原则
		@RequestParam(value = "filemeitao", required = false) MultipartFile file) {

	if(StringUtil.isEmpty(inputmode)||StringUtil.isEmpty(inputrude))
	{
		return generateResponseObject(ResponseCode.PARAMETER_ERROR,
				"参数有误！");
	}
	
	
	String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
	if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
	{
	
	}
	else
	{
		String master = (String)request.getSession().getAttribute(Constant.EMP_STORE_MASTER_SESSION_KEY);
		if((master!=null)&&(master.equalsIgnoreCase("1")))
		{
			
		}
		else
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"对不起，只有高级管理员或店长能够导入身份证信息!");
		}
	}

	
/*	String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
	if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
	{
		return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权进行此操作!");
		
	}*/
	// 图片流
	InputStream imgInputStream = null;
	OutputStream os = null;
	OutputStream os1 = null;
	List<import_t_orders> Orders = null;
	if (file != null && file.getSize() > 0) {
		
		try {
			//kai 20160315判定是不是符合excel表格
			String originalName = file.getOriginalFilename();
			if (!StringUtil.boolpicisgoodornot(originalName, defaultExcelFileType)) {
				return new ResponseObject<Object>(
						ResponseCode.CONSIGNEE_CARD_ERROR, "必须上传excel 2003表格,请重新尝试!");
			}
			
			Orders = CardidManageControllerUtil.readgetCardidFromMeitaoExcel(file
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
			ResponseObject<Object> obj=null;
			if(!inputmode.equalsIgnoreCase("2"))
			{
				obj=this.cardidManageService.verifycardid(Orders, inputrude,request);
			}
			else//深圳海关的不用匹配图片
			{
				obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
				obj.setData(Orders);
			}
			String fileName="";
			if(inputmode.equalsIgnoreCase("0")||inputmode.equalsIgnoreCase("1")||inputmode.equalsIgnoreCase("3"))
			{
				
				
				//ArrayList arlList=new ArrayList();
				ArrayList<String> arlList=new ArrayList();
				for(import_t_orders order1:Orders)
				{
					if(order1.isResultflag())
					{
						arlList.add(order1.getCardurl());
					}
				}
				HashSet h = new HashSet(arlList);   
				arlList.clear();   
				arlList.addAll(h);
				File[] srcfile = new File[arlList.size()];
				int nn=0;
				for(String url:arlList)
				{
					
					srcfile[nn]=new File(request.getSession()
							.getServletContext().getRealPath("/")
							+ url);
					
					nn++;
				}
				
				
				String strtest = this.saveCardDir;
				String strseparator = "";
				if (strtest.indexOf("/") >= 0)// 包含有“/”字符，分隔符用此字符
				{
					strseparator = "/";
				} else {
					strseparator = File.separator;
				}
				
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyyMMddHHmmss");
				String str = sdf.format(date);

				String originalName = str + ".zip";
				int index = originalName.lastIndexOf('.');
				index = Math.max(index, 0);
				 fileName = this.saveCardDir + File.separator + "temp"
						+ strseparator + StringUtil.generateRandomString(5)
						+ "_" + StringUtil.generateRandomInteger(5) + "_"
						// + originalName.substring(index);
						+ originalName;

				File file1 = new File(request.getSession()
						.getServletContext().getRealPath("/")
						+ fileName);
				
				
				if ((srcfile.length > 0) && (srcfile[0] != null)) {
					basiczip.zipFilesSameName(srcfile, file1);
					
					StringBuffer url = request.getRequestURL();  
					String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString(); 
					
					
					Orders.get(0).setZipurl(tempContextUrl+fileName);
					//response.setContentType("application/zip");
					//response.setHeader("Content-Disposition", 
					//		"attachment; filename=\"" + originalName + "\""); 
					//response.setHeader("Location","download.zip");
				//	imgInputStream = new FileInputStream(file1);

					//byte[] imgBytes = IOUtils.toByteArray(imgInputStream);

				//	os1 = response.getOutputStream();

					//os1.write(imgBytes);
				} else {
					// return "订单中没有身份证图片，请退回重新选择！";
				}
			
			}
			
			if((obj!=null)&&(ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj.getCode())))
			{
				if(obj.getData()==null)
				{
					return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
							"返回数据为空！");
				}
				Orders=	(List<import_t_orders>)obj.getData();
				obj.setData(null);
				obj=null;
				if (Orders != null && Orders.size() >0) {	
					
					
					 fileName = "success_verify_import_result_"+inputmode+"_"
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

				if(inputmode.equalsIgnoreCase("0"))
				{
					File templeFile = new File(request.getSession()
							.getServletContext().getRealPath("/")
							+ this.meitaoneimengguimportseaordertemplets);
					os = response.getOutputStream();
					CardidManageControllerUtil.exportneimenggumode(Orders, templeFile, os);
				}
				else if(inputmode.equalsIgnoreCase("1"))
				{
					File templeFile = new File(request.getSession()
							.getServletContext().getRealPath("/")
							+ this.meitaoneimengguimportseaordertotheremplets);
					os = response.getOutputStream();
					CardidManageControllerUtil.exportneimenggu(Orders, templeFile, os);
				}
				else if(inputmode.equalsIgnoreCase("2"))
				{
					File templeFile = new File(request.getSession()
							.getServletContext().getRealPath("/")
							+ this.meitaoshenzhenimportseaordertemplets);
					os = response.getOutputStream();
					CardidManageControllerUtil.exportshenzhen(Orders, templeFile, os);
				}
				else if(inputmode.equalsIgnoreCase("3"))//上海模板
				{
					File templeFile = new File(request.getSession()
							.getServletContext().getRealPath("/")
							+ this.meitaoshanghaiseaordermodetemplets);
					os = response.getOutputStream();
					CardidManageControllerUtil.exportshanghaimode(Orders, templeFile, os);
				}
				
				/*if(inputmode.equalsIgnoreCase("0")||inputmode.equalsIgnoreCase("1"))
				{
					
					
					//ArrayList arlList=new ArrayList();
					ArrayList<String> arlList=new ArrayList();
					for(import_t_orders order1:Orders)
					{
						if(order1.isResultflag())
						{
							arlList.add(order1.getCardurl());
						}
					}
					HashSet h = new HashSet(arlList);   
					arlList.clear();   
					arlList.addAll(h);
					File[] srcfile = new File[arlList.size()];
					int nn=0;
					for(String url:arlList)
					{
						
						srcfile[nn]=new File(request.getSession()
								.getServletContext().getRealPath("/")
								+ url);
						
						nn++;
					}
					
					
					String strtest = this.saveCardDir;
					String strseparator = "";
					if (strtest.indexOf("/") >= 0)// 包含有“/”字符，分隔符用此字符
					{
						strseparator = "/";
					} else {
						strseparator = File.separator;
					}
					
					Date date = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyyMMddHHmmss");
					String str = sdf.format(date);

					String originalName = str + ".zip";
					int index = originalName.lastIndexOf('.');
					index = Math.max(index, 0);
					 fileName = this.saveCardDir + File.separator + "temp"
							+ strseparator + StringUtil.generateRandomString(5)
							+ "_" + StringUtil.generateRandomInteger(5) + "_"
							// + originalName.substring(index);
							+ originalName;

					File file1 = new File(request.getSession()
							.getServletContext().getRealPath("/")
							+ fileName);
					
					
					if ((srcfile.length > 0) && (srcfile[0] != null)) {
						basiczip.zipFilesSameName(srcfile, file1);
						response.setContentType("application/zip");
						response.setHeader("Content-Disposition", 
								"attachment; filename=\"" + originalName + "\""); 
						//response.setHeader("Location","download.zip");
						imgInputStream = new FileInputStream(file1);
	
						byte[] imgBytes = IOUtils.toByteArray(imgInputStream);
	
						os1 = response.getOutputStream();
	
						os1.write(imgBytes);
					} else {
						// return "订单中没有身份证图片，请退回重新选择！";
					}
				
				}*/
				//CardidManageControllerUtil.exportzipcardidsToResult(importargs, templeFile, os);
				}
				else
				{
					return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
							"返回数据为空！");
				}
			}
		
			//return generateResponseObject(ResponseCode.SUCCESS_CODE);
		} catch (Exception e) {
			log.error("修改数据库失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					"提交失败：" + e.getMessage());
		}finally {
			// orders.clear();
			// 6.无论成功与否关闭相应的流
			try {
				if (imgInputStream != null) {
					
					imgInputStream.close();
				}
				if (os != null) {
					os.flush();
					os.close();
				}
				if (os1 != null) {
					os1.flush();
					os1.close();
				}
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}

		}
	}
	return generateResponseObject(ResponseCode.PARAMETER_ERROR, "文件不能为空");
}

//上传模板下载 
@RequestMapping(value = "/admin/cardid_lib/upload_orders_example", method = { RequestMethod.GET })
public void getuploadordersexampleDataExcelFile(HttpServletRequest request,
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
						this.meitaoexportseaorderexampletemplets);
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




//下载身份证除图片外的所有信息
@RequestMapping(value = "/admin/cardid_lib/import_cardid_word", method = { RequestMethod.POST })
@ResponseBody
public ResponseObject<Object> importcardidwordtoExcel(
		HttpServletRequest request,
		HttpServletResponse response) {


	
	
	String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
	String storeId=null;
	if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
	{
	
	}
	else
	{
		String master = (String)request.getSession().getAttribute(Constant.EMP_STORE_MASTER_SESSION_KEY);
		storeId=(String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
		if((master!=null)&&(master.equalsIgnoreCase("1")))
		{
			
		}
		else
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"对不起，只有高级管理员或店长能够导出运单信息!");
		}
	}

	
/*	String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
	if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
	{
		return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权进行此操作!");
		
	}*/
	// 图片流
	InputStream imgInputStream = null;
	OutputStream os = null;
	OutputStream os1 = null;
	
	
		
	

		try {
			
			ResponseObject<PageSplit<CardId_lib>> obj=this.cardidManageService.searchbykey(null,request, 0x7fffffff, 1);
			
			if(obj==null||!ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj.getCode()))
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取数据失败!");
			}
			//ResponseObject<Object> obj=null;
			//导出信息
		
			
			String fileName="";
		
			
			List<CardId_lib> cardlibs=obj.getData().getDatas();
			
				if (cardlibs != null && cardlibs.size() >0) {	
					
					
					
					fileName = "download_cardids_" + cardlibs.size() + ".xls";
					// key = new String(key.getBytes("ISO-8859-1"), "utf-8");
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-disposition",
							"attachment;filename="
									+ new String(fileName.getBytes(), "utf-8"));
					// orders = this.orderService.getExportOrders(sdate, edate);

					/*File templeFile = new File(request.getSession()
							.getServletContext().getRealPath("/")
							+ this.orderOutputToWeiyiTempletsFile);*/
					File templeFile = new File(request.getSession()
							.getServletContext().getRealPath("/")
							+ this.cardiddownword);;
					
					os = response.getOutputStream();
					
					// 导出数据唯一快递单子
					
					CardidManageControllerUtil.exportcardlibwordToMeitao(cardlibs, templeFile, os);
					
					
				}
					
				
			
		
			return generateResponseObject(ResponseCode.SUCCESS_CODE);
		} catch (Exception e) {
			log.error("修改数据库失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					"获取失败：" + e.getMessage());
		}finally {
			// orders.clear();
			// 6.无论成功与否关闭相应的流
			try {
				if (imgInputStream != null) {
					imgInputStream.close();
				}
				if (os != null) {
					os.flush();
					os.close();
				}
				if (os1 != null) {
					os1.flush();
					os1.close();
				}
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}

		}
	
	//return generateResponseObject(ResponseCode.PARAMETER_ERROR, "文件不能为空");
}


}
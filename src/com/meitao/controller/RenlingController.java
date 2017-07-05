package com.meitao.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import jxl.common.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.meitao.service.ClaimPackageService;
import com.meitao.service.RenlingService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ParameterConstants;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.RenlingUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.common.util.UserUtil;
import com.meitao.model.PageSplit;
import com.meitao.model.RenlingBaoguo;
import com.meitao.model.RenlingPersons;
import com.meitao.model.ResponseObject;

@Controller
public class RenlingController extends BasicController {

	private static final long serialVersionUID = 1044371094993785202L;
	private static final Logger log = Logger.getLogger(RenlingController.class);

	@Resource(name = "renlingService")
	public RenlingService renlingService;
	@Resource(name = "claimPackageService")
	public ClaimPackageService claimPackageService;
	@Value(value = "${page_size}")
	private int defaultPageSize;
	@Value(value = "${default_img_type}")
	private String defaultCardFileType;
	
	@Value(value = "${sava_global_pic_dir}")
	private String saveGlobalDir;
	@Value(value = "${default_img_size}")
	private long defaultCardFileSize;

/*	@RequestMapping(value = { "/warehouse/all", "/user/warehouse/all" }, method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<List<Warehouse>> getAllWarehouse() {
		try {
			return this.warehouseService.getAll();
		} catch (Exception e) {
			log.error("获取仓库列表失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取仓库列表失败");
		}
	}*/

	
	@RequestMapping(value = "/admin/renling/add", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Map<String, String>> addwarehousehavepic(
			HttpServletRequest request,
			@RequestParam(value = "baoguoId", required = false, defaultValue = "") String baoguoId,
			@RequestParam(value = "company", required = false, defaultValue = "") String company,	
			@RequestParam(value = "revName", required = false, defaultValue = "") String revName,	
			@RequestParam(value = "baoguoPhone", required = false, defaultValue = "") String baoguoPhone,	
			@RequestParam(value = "title", required = false, defaultValue = "") String title,	
			@RequestParam(value = "content", required = false, defaultValue = "") String content,			
			@RequestParam(value = "file1", required = false) MultipartFile file1,
			@RequestParam(value = "file2", required = false) MultipartFile file2,
			@RequestParam(value = "file3", required = false) MultipartFile file3) {
		
	try {
			
		if((file1 != null && file1.getSize() > 0)||(file2 != null && file2.getSize() > 0)||(file3 != null && file3.getSize() > 0))
		{
			
		}
		else
		{
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "至少要提交一个图片！");
		}
		
		if((baoguoId==null)||(baoguoId.equalsIgnoreCase("")))
		{
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "包裹单号不能为空！");
		}
		if((title==null)||(title.equalsIgnoreCase("")))
		{
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "认领标题不能为空！");
		}
		if((content==null)||(content.equalsIgnoreCase("")))
		{
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "认领内容不能为空！");
		}
		
		
		RenlingBaoguo renling=new RenlingBaoguo();
			// 处理提交上来的图片
			// 解决火狐的反斜杠问题 kai 20151006
			String filetype = this.defaultCardFileType;// 要上传的文件类型
			String strtest = this.saveGlobalDir;
			String strseparator = "";
			if (strtest.indexOf("/") >= 0)// 包含有“/”字符，分隔符用此字符
			{
				strseparator = "/";
			} else {
				strseparator = File.separator;
			}
			String fileName = null;
			if (file1 != null && file1.getSize() > 0) {
				if (file1.getSize() > this.defaultCardFileSize) {
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
				}

				String originalName = file1.getOriginalFilename();

				if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
				}

				int index = originalName.lastIndexOf('.');
				index = Math.max(index, 0);
				fileName = this.saveGlobalDir + strseparator + "Renling" + "_"+StringUtil.generateRandomString(5)+"_"
						+ StringUtil.generateRandomString(5) + "_"
						+ StringUtil.generateRandomInteger(5)
						+ originalName.substring(index);
				try {
					File filenew = new File(request.getSession().getServletContext()
							.getRealPath("/")
							+ fileName);
					file1.transferTo(filenew);
				} catch (Exception e) {
					log.error("保存用户图像失败,请不要上传图像！", e);
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR,
							"保存用户图像失败，请去除上传图像后再尝试!");
				}
			}
			renling.setPic1(fileName);
			

			if (file2 != null && file2.getSize() > 0) {
				if (file2.getSize() > this.defaultCardFileSize) {
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
				}

				String originalName = file2.getOriginalFilename();

				if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
				}

				int index = originalName.lastIndexOf('.');
				index = Math.max(index, 0);
				fileName = this.saveGlobalDir + strseparator + "Renling" + "_"+StringUtil.generateRandomString(5)+"_"
						+ StringUtil.generateRandomString(5) + "_"
						+ StringUtil.generateRandomInteger(5)
						+ originalName.substring(index);
				try {
					File filenew = new File(request.getSession().getServletContext()
							.getRealPath("/")
							+ fileName);
					file2.transferTo(filenew);
				} catch (Exception e) {
					log.error("保存用户图像失败,请不要上传图像！", e);
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR,
							"保存用户图像失败，请去除上传图像后再尝试!");
				}
			}
			else
			{
				fileName="";
			}
			renling.setPic2(fileName);
			
			if (file3 != null && file3.getSize() > 0) {
				if (file3.getSize() > this.defaultCardFileSize) {
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
				}

				String originalName = file3.getOriginalFilename();

				if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
				}

				int index = originalName.lastIndexOf('.');
				index = Math.max(index, 0);
				fileName = this.saveGlobalDir + strseparator + "Renling" + "_"+StringUtil.generateRandomString(5)+"_"
						+ StringUtil.generateRandomString(5) + "_"
						+ StringUtil.generateRandomInteger(5)
						+ originalName.substring(index);
				try {
					File filenew = new File(request.getSession().getServletContext()
							.getRealPath("/")
							+ fileName);
					file3.transferTo(filenew);
				} catch (Exception e) {
					log.error("保存用户图像失败,请不要上传图像！", e);
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR,
							"保存用户图像失败，请去除上传图像后再尝试!");
				}
			}
			else
			{
				fileName="";
			}
			renling.setPic3(fileName);
			
			renling.setBaoguoPhone(baoguoPhone);
			renling.setCompany(company);
			renling.setContent(content);
			renling.setTitle(title);
			renling.setRevName(revName);
			renling.setBaoguoId(baoguoId);
			String empId = StringUtil.obj2String(request.getSession().getAttribute(
					Constant.EMP_ID_SESSION_KEY));
			
			String storeId = StringUtil.obj2String(request.getSession()
					.getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
			renling.setEmpId(empId);
			renling.setWarehouseId(storeId);
			renling.setState(Constant.RENLING_STATE0);//设置为未认领状态
			
			SimpleDateFormat df = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");// 设置日期格式
			
			renling.setCreateDate(df.format(new Date()));
			renling.setModifyDate(df.format(new Date()));
			
			
			
			return this.renlingService.addRenling(renling);
			
			
		} catch (Exception e) {
			log.error("添加认领失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "添加认领出现异常");
		}
		
	}
	
	
	
		
	@RequestMapping(value = "/admin/renling/search", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<PageSplit<RenlingBaoguo>> searchrenlingOfAdmin(
			@RequestParam(value = "rid", required = false, defaultValue = "") String rid,
			@RequestParam(value = ParameterConstants.COMMON_SEARCH_KEY, required = false, defaultValue = "") String key,
			@RequestParam(value = ParameterConstants.COMMON_SEARCH_TYPE, required = false, defaultValue = "") String type,
			@RequestParam(value = ParameterConstants.COMMON_SEARCH_STATE, required = false, defaultValue = "") String state,
			@RequestParam(value = ParameterConstants.COMMON_SEARCH_SDATE, required = false, defaultValue = "") String sdate,
			@RequestParam(value = ParameterConstants.COMMON_SEARCH_EDATE, required = false, defaultValue = "") String edate,
			@RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex) {
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

		try {
			rid = StringUtil.isEmpty(rid) ? null : rid;
			String column = RenlingUtil.getSearchColumnByType(type);
			pageIndex = Math.max(pageIndex, 1);
			return this.renlingService.searchByKey(rid, key, column, state, sdate, edate, defaultPageSize, pageIndex);
			
		} catch (Exception e) {
			log.error("获取用户运单失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					"获取用户运单失败");
		}
	}
	

	@RequestMapping(value = "/admin/renling/delete", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> deleteOfAdmin(
			@RequestParam(value = ParameterConstants.RENLING_ID) String[] ids) {
		if (ids == null || ids.length == 0) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR,
					"参数无效");
		}
		try {
			//return this.transhipmentBillService.deleteTranshipment(
					//Arrays.asList(ids), Constant.TRANSHIPMENT_STATE4);
			return this.renlingService.deleteRenlingbill(Arrays.asList(ids));
			
			
		} catch (Exception e) {
			log.error("删除运单失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "删除认领单失败");
		}
	}
	
	
	@RequestMapping(value = "/admin/renling/get_one", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<RenlingBaoguo> admingetById(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.RENLING_ID) String id) {
		if ((id==null)||(id.equalsIgnoreCase(""))) {
			return generateResponseObject(ResponseCode.NEWS_ID_ERROR, "参数无效");
		}

		try {
			
			return this.renlingService.getRenlingbyid(id);
		} catch (Exception e) {
			log.error("根据id获取认领信息对象失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取认领信息失败，出现异常!");
		}
	}
	
	
	@RequestMapping(value = "/admin/renling/modifyhavepic", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Map<String, String>> modifyrenlinghavepic(
			HttpServletRequest request,		
			@RequestParam(value = "modifyid", required = false, defaultValue = "") String id,
			@RequestParam(value = "baoguoId", required = false, defaultValue = "") String baoguoId,
			@RequestParam(value = "company", required = false, defaultValue = "") String company,	
			@RequestParam(value = "revName", required = false, defaultValue = "") String revName,	
			@RequestParam(value = "baoguoPhone", required = false, defaultValue = "") String baoguoPhone,	
			@RequestParam(value = "title", required = false, defaultValue = "") String title,	
			@RequestParam(value = "content", required = false, defaultValue = "") String content,
			@RequestParam(value = "renling_state", required = false, defaultValue = "") String state,			
			@RequestParam(value = "file_1", required = false, defaultValue = "") String file_1,
			@RequestParam(value = "file_2", required = false, defaultValue = "") String file_2,
			@RequestParam(value = "file_3", required = false, defaultValue = "") String file_3,
			@RequestParam(value = "file1", required = false) MultipartFile file1,
			@RequestParam(value = "file2", required = false) MultipartFile file2,
			@RequestParam(value = "file3", required = false) MultipartFile file3,
			//以下是设置为认领状态时的设置
			@RequestParam(value = "phone", required = false, defaultValue = "") String phone,//认领人电话
			@RequestParam(value = "username", required = false, defaultValue = "") String username,//认领人姓名
			@RequestParam(value = "userremark", required = false, defaultValue = "") String userremark,//认领人电话
			@RequestParam(value = "renlingPersonId", required = false, defaultValue = "") String renlingPersonId,
			
			@RequestParam(value = "p_file_1", required = false, defaultValue = "") String p_file_1,//认领状态时，原来图片
			@RequestParam(value = "p_file_2", required = false, defaultValue = "") String p_file_2,
			@RequestParam(value = "p_file_3", required = false, defaultValue = "") String p_file_3,
			@RequestParam(value = "p_file_4", required = false, defaultValue = "") String p_file_4,
			@RequestParam(value = "p_file_5", required = false, defaultValue = "") String p_file_5,
			
			@RequestParam(value = "p_file1", required = false) MultipartFile p_file1,//设置为已认领状态的状态
			@RequestParam(value = "p_file2", required = false) MultipartFile p_file2,
			@RequestParam(value = "p_file3", required = false) MultipartFile p_file3,
			@RequestParam(value = "p_file4", required = false) MultipartFile p_file4,
			@RequestParam(value = "p_file5", required = false) MultipartFile p_file5
			) {
		
	try {
			
		if((id==null)||(id.equalsIgnoreCase("")))
		{
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "传送标识id出错！");
		}
		if((state==null)||(state.equalsIgnoreCase(""))||(state.equalsIgnoreCase("-1")))
		{
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "必须选择状态！");
		}
		if((file1 != null && file1.getSize() > 0)||(file2 != null && file2.getSize() > 0)||(file3 != null && file3.getSize() > 0))
		{
			
		}
		else
		{
			if(((file_1!=null)&&(!file_1.equalsIgnoreCase("")))||((file_2!=null)&&(!file_2.equalsIgnoreCase("")))||((file_3!=null)&&(!file_3.equalsIgnoreCase(""))))
			{}
			else
			{
				return generateResponseObject(ResponseCode.PARAMETER_ERROR, "至少要提交一个图片！");
			}
		}
		
		if((baoguoId==null)||(baoguoId.equalsIgnoreCase("")))
		{
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "包裹单号不能为空！");
		}
		if((title==null)||(title.equalsIgnoreCase("")))
		{
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "认领标题不能为空！");
		}
		if((content==null)||(content.equalsIgnoreCase("")))
		{
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "认领内容不能为空！");
		}
		if(state.equalsIgnoreCase(Constant.RENLING_STATE1))//是已认领状态
		{
			if((phone==null)||(phone.equalsIgnoreCase("")))
			{
				return generateResponseObject(ResponseCode.PARAMETER_ERROR, "在 已认领 状态下，必须填写 认领人 的电话!");
			}
		}
		
		RenlingBaoguo renling=new RenlingBaoguo();
			// 处理提交上来的图片
			// 解决火狐的反斜杠问题 kai 20151006
			String filetype = this.defaultCardFileType;// 要上传的文件类型
			String strtest = this.saveGlobalDir;
			String strseparator = "";
			if (strtest.indexOf("/") >= 0)// 包含有“/”字符，分隔符用此字符
			{
				strseparator = "/";
			} else {
				strseparator = File.separator;
			}
			String fileName = null;
			if (file1 != null && file1.getSize() > 0) {
				if (file1.getSize() > this.defaultCardFileSize) {
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
				}

				String originalName = file1.getOriginalFilename();

				if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
				}

				int index = originalName.lastIndexOf('.');
				index = Math.max(index, 0);
				fileName = this.saveGlobalDir + strseparator + "Renling" + "_"+StringUtil.generateRandomString(5)+"_"
						+ StringUtil.generateRandomString(5) + "_"
						+ StringUtil.generateRandomInteger(5)
						+ originalName.substring(index);
				try {
					File filenew = new File(request.getSession().getServletContext()
							.getRealPath("/")
							+ fileName);
					file1.transferTo(filenew);
				} catch (Exception e) {
					log.error("保存用户图像失败,请不要上传图像！", e);
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR,
							"保存用户图像失败，请去除上传图像后再尝试!");
				}
			}
			else
			{
				fileName=file_1;
			}
			renling.setPic1(fileName);
			

			if (file2 != null && file2.getSize() > 0) {
				if (file2.getSize() > this.defaultCardFileSize) {
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
				}

				String originalName = file2.getOriginalFilename();

				if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
				}

				int index = originalName.lastIndexOf('.');
				index = Math.max(index, 0);
				fileName = this.saveGlobalDir + strseparator + "Renling" + "_"+StringUtil.generateRandomString(5)+"_"
						+ StringUtil.generateRandomString(5) + "_"
						+ StringUtil.generateRandomInteger(5)
						+ originalName.substring(index);
				try {
					File filenew = new File(request.getSession().getServletContext()
							.getRealPath("/")
							+ fileName);
					file2.transferTo(filenew);
				} catch (Exception e) {
					log.error("保存用户图像失败,请不要上传图像！", e);
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR,
							"保存用户图像失败，请去除上传图像后再尝试!");
				}
			}
			else
			{
				fileName=file_2;
			}
			renling.setPic2(fileName);
			
			if (file3 != null && file3.getSize() > 0) {
				if (file3.getSize() > this.defaultCardFileSize) {
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
				}

				String originalName = file3.getOriginalFilename();

				if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
				}

				int index = originalName.lastIndexOf('.');
				index = Math.max(index, 0);
				fileName = this.saveGlobalDir + strseparator + "Renling" + "_"+StringUtil.generateRandomString(5)+"_"
						+ StringUtil.generateRandomString(5) + "_"
						+ StringUtil.generateRandomInteger(5)
						+ originalName.substring(index);
				try {
					File filenew = new File(request.getSession().getServletContext()
							.getRealPath("/")
							+ fileName);
					file3.transferTo(filenew);
				} catch (Exception e) {
					log.error("保存用户图像失败,请不要上传图像！", e);
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR,
							"保存用户图像失败，请去除上传图像后再尝试!");
				}
			}
			else
			{
				fileName=file_3;
			}
			renling.setPic3(fileName);
			
			if(state.equalsIgnoreCase(Constant.RENLING_STATE1))
			{
				RenlingPersons person=new RenlingPersons();
				renling.setReninfo(person);
				
				if (p_file1 != null && p_file1.getSize() > 0) {
					if (p_file1.getSize() > this.defaultCardFileSize) {
						return generateResponseObject(
								ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
					}
	
					String originalName = p_file1.getOriginalFilename();
	
					if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
						return generateResponseObject(
								ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
					}
	
					int index = originalName.lastIndexOf('.');
					index = Math.max(index, 0);
					fileName = this.saveGlobalDir + strseparator + "Renling" + "_"+StringUtil.generateRandomString(5)+"_"
							+ StringUtil.generateRandomString(5) + "_"
							+ StringUtil.generateRandomInteger(5)
							+ originalName.substring(index);
					try {
						File filenew = new File(request.getSession().getServletContext()
								.getRealPath("/")
								+ fileName);
						p_file1.transferTo(filenew);
					} catch (Exception e) {
						log.error("保存用户图像失败,请不要上传图像！", e);
						return generateResponseObject(
								ResponseCode.CONSIGNEE_CARD_ERROR,
								"保存用户图像失败，请去除上传图像后再尝试!");
					}
				}
				else
				{
					fileName=p_file_1;
				}
				renling.getReninfo().setPic1(fileName);
				
				if (p_file2 != null && p_file2.getSize() > 0) {
					if (p_file2.getSize() > this.defaultCardFileSize) {
						return generateResponseObject(
								ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
					}
	
					String originalName = p_file2.getOriginalFilename();
	
					if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
						return generateResponseObject(
								ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
					}
	
					int index = originalName.lastIndexOf('.');
					index = Math.max(index, 0);
					fileName = this.saveGlobalDir + strseparator + "Renling" + "_"+StringUtil.generateRandomString(5)+"_"
							+ StringUtil.generateRandomString(5) + "_"
							+ StringUtil.generateRandomInteger(5)
							+ originalName.substring(index);
					try {
						File filenew = new File(request.getSession().getServletContext()
								.getRealPath("/")
								+ fileName);
						p_file2.transferTo(filenew);
					} catch (Exception e) {
						log.error("保存用户图像失败,请不要上传图像！", e);
						return generateResponseObject(
								ResponseCode.CONSIGNEE_CARD_ERROR,
								"保存用户图像失败，请去除上传图像后再尝试!");
					}
				}
				else
				{
					fileName=p_file_2;
				}
				renling.getReninfo().setPic2(fileName);
	
				
				if (p_file3 != null && p_file3.getSize() > 0) {
					if (p_file3.getSize() > this.defaultCardFileSize) {
						return generateResponseObject(
								ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
					}
	
					String originalName = p_file3.getOriginalFilename();
	
					if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
						return generateResponseObject(
								ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
					}
	
					int index = originalName.lastIndexOf('.');
					index = Math.max(index, 0);
					fileName = this.saveGlobalDir + strseparator + "Renling" + "_"+StringUtil.generateRandomString(5)+"_"
							+ StringUtil.generateRandomString(5) + "_"
							+ StringUtil.generateRandomInteger(5)
							+ originalName.substring(index);
					try {
						File filenew = new File(request.getSession().getServletContext()
								.getRealPath("/")
								+ fileName);
						p_file3.transferTo(filenew);
					} catch (Exception e) {
						log.error("保存用户图像失败,请不要上传图像！", e);
						return generateResponseObject(
								ResponseCode.CONSIGNEE_CARD_ERROR,
								"保存用户图像失败，请去除上传图像后再尝试!");
					}
				}
				else
				{
					fileName=p_file_3;
				}
				renling.getReninfo().setPic3(fileName);
				
				
				if (p_file4 != null && p_file4.getSize() > 0) {
					if (p_file4.getSize() > this.defaultCardFileSize) {
						return generateResponseObject(
								ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
					}
	
					String originalName = p_file4.getOriginalFilename();
	
					if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
						return generateResponseObject(
								ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
					}
	
					int index = originalName.lastIndexOf('.');
					index = Math.max(index, 0);
					fileName = this.saveGlobalDir + strseparator + "Renling" + "_"+StringUtil.generateRandomString(5)+"_"
							+ StringUtil.generateRandomString(5) + "_"
							+ StringUtil.generateRandomInteger(5)
							+ originalName.substring(index);
					try {
						File filenew = new File(request.getSession().getServletContext()
								.getRealPath("/")
								+ fileName);
						p_file4.transferTo(filenew);
					} catch (Exception e) {
						log.error("保存用户图像失败,请不要上传图像！", e);
						return generateResponseObject(
								ResponseCode.CONSIGNEE_CARD_ERROR,
								"保存用户图像失败，请去除上传图像后再尝试!");
					}
				}
				else
				{
					fileName=p_file_4;
				}
				renling.getReninfo().setPic4(fileName);
				
				
				if (p_file5 != null && p_file5.getSize() > 0) {
					if (p_file5.getSize() > this.defaultCardFileSize) {
						return generateResponseObject(
								ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
					}
	
					String originalName = p_file5.getOriginalFilename();
	
					if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
						return generateResponseObject(
								ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
					}
	
					int index = originalName.lastIndexOf('.');
					index = Math.max(index, 0);
					fileName = this.saveGlobalDir + strseparator + "Renling" + "_"+StringUtil.generateRandomString(5)+"_"
							+ StringUtil.generateRandomString(5) + "_"
							+ StringUtil.generateRandomInteger(5)
							+ originalName.substring(index);
					try {
						File filenew = new File(request.getSession().getServletContext()
								.getRealPath("/")
								+ fileName);
						p_file5.transferTo(filenew);
					} catch (Exception e) {
						log.error("保存用户图像失败,请不要上传图像！", e);
						return generateResponseObject(
								ResponseCode.CONSIGNEE_CARD_ERROR,
								"保存用户图像失败，请去除上传图像后再尝试!");
					}
				}
				else
				{
					fileName=p_file_5;
				}
				renling.getReninfo().setPic5(fileName);
			}
			
			renling.setBaoguoPhone(baoguoPhone);
			renling.setCompany(company);
			renling.setContent(content);
			renling.setTitle(title);
			renling.setRevName(revName);
			renling.setBaoguoId(baoguoId);
			renling.setId(id);
			renling.setState(state);
			
			
			
			String empId = StringUtil.obj2String(request.getSession().getAttribute(
					Constant.EMP_ID_SESSION_KEY));
			
			String storeId = StringUtil.obj2String(request.getSession()
					.getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
			renling.setEmpId(empId);
			renling.setWarehouseId(storeId);
			//renling.setState(Constant.RENLING_STATE0);//设置为未认领状态
			
			SimpleDateFormat df = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");// 设置日期格式
			
			//renling.setCreateDate(df.format(new Date()));
			renling.setModifyDate(df.format(new Date()));
			if(renling.getReninfo()!=null)
			{
				renling.getReninfo().setCreateDate(df.format(new Date()));
				renling.getReninfo().setModifyDate(df.format(new Date()));
				renling.getReninfo().setId(renlingPersonId);
				renling.getReninfo().setBaoguoRemark(userremark);
				renling.getReninfo().setPhone(phone);
				renling.getReninfo().setUserName(username);
			}
			
			
			
		return	this.renlingService.modifyRenling(renling);
			
			
		} catch (Exception e) {
			log.error("添加认领失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "添加认领出现异常");
		}
		
	}
	

	
	@RequestMapping(value = "/admin/renling/modifyhavenopic", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> modifyhavenopic(HttpServletRequest request, RenlingBaoguo renling
	        ) throws Exception {
		try {
			renling.setEmpId(StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY)));
			return renlingService.updateByAdmin(renling);
		} catch (Exception e) {
			log.error("修改新闻对象失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改认领单出现异常");
		}
	}
	
	@RequestMapping(value = "/claimPackage/get", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject<RenlingBaoguo> getById(HttpServletRequest request, @RequestParam(value = "id", required = false) String id) throws Exception{
		try{
			return this.renlingService.getRenlingbyid(id);
		} catch ( Exception e){
			log.error("fail to get renlingbaoguo by id", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取认领包裹详情失败");
		}
	}
	
	@RequestMapping(value = "/admin/claimPackage/countNeedAudit", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject<Integer> countClaimPackageByNeedAudit(HttpServletRequest request) throws Exception{
		try{
			return this.renlingService.countNeedAudit();
		} catch ( Exception e){
			log.error("fail to get renlingbaoguo by id", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取认领包裹详情失败");
		}
	}
	@RequestMapping(value = "/admin/claimPackage/getAllStateCount", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject<String[]> getAllStateCount(HttpServletRequest request){
		try{
			return this.claimPackageService.getAllStateCount();
		}catch(Exception e){
			log.error("根据用户获取事件数量出现异常");
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "根据用户获取事件数量出现异常");
		}
	}
}

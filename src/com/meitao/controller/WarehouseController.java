package com.meitao.controller;

import java.io.File;
import java.util.List;
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

import com.meitao.service.WarehouseService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ParameterConstants;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.StringUtil;
import com.meitao.common.util.WarehouseUtil;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.ResponseString;
import com.meitao.model.TranPrice;
import com.meitao.model.Warehouse;

@Controller
public class WarehouseController extends BasicController {

	private static final long serialVersionUID = 104214521683255202L;
	private static final Logger log = Logger.getLogger(WarehouseController.class);

	@Resource(name = "warehouseService")
	public WarehouseService warehouseService;
	@Value(value = "${page_size}")
	private int defaultPageSize;
	@Value(value = "${default_img_type}")
	private String defaultCardFileType;
	
	@Value(value = "${sava_global_pic_dir}")
	private String saveGlobalDir;
	@Value(value = "${default_img_size}")
	private long defaultCardFileSize;

	@RequestMapping(value = { "/user/warehouse/all" }, method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<List<Warehouse>> getAllWarehouse() {
		try {
			return this.warehouseService.getAll();
		} catch (Exception e) {
			log.error("获取门店列表失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取门店列表失败");
		}
	}

	
	@RequestMapping(value = { "/admin/warehouse/all" }, method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public ResponseObject<List<Warehouse>> getAllWarehousebyadmin() {
		try {
			return this.warehouseService.getAll();
		} catch (Exception e) {
			log.error("获取门店列表失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取门店列表失败");
		}
	}
	@RequestMapping(value = "/admin/warehouse/add",produces="text/plain;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String addWarehouse(HttpServletRequest request,Warehouse house) {
		ResponseString responseString=new ResponseString();
		String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
		{
			responseString.setCode(ResponseCode.EMPLOYEE_STORE_NAME_ERROR);
			responseString.setMessage("对不起，你无权添加门店!");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权添加门店!");
			
		}
		
		
		if (house == null) {
			responseString.setCode(ResponseCode.PARAMETER_ERROR);
			responseString.setMessage("参数无效!");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数无效");
		}

		if (!WarehouseUtil.validateAddress(house.getAddress())) {
			responseString.setCode(ResponseCode.WAREHOUSE_ADDRESS_ERROR);
			responseString.setMessage("地址格式错误，请重新输入");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.WAREHOUSE_ADDRESS_ERROR, "地址格式错误，请重新输入！");
		}

		if (!WarehouseUtil.validateCity(house.getCity())) {
			responseString.setCode(ResponseCode.WAREHOUSE_CITY_ERROR);
			responseString.setMessage("市格式错误，请重新输入！");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.WAREHOUSE_CITY_ERROR, "市格式错误，请重新输入！");
		}

		if (!WarehouseUtil.validateCompany(house.getCompany())) {
			responseString.setCode(ResponseCode.WAREHOUSE_COMPANY_ERROR);
			responseString.setMessage("公司名称输入错误，请重新输入！");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.WAREHOUSE_COMPANY_ERROR, "公司名称输入错误，请重新输入！");
		}

		if (!WarehouseUtil.validateContactName(house.getContactName())) {
			responseString.setCode(ResponseCode.WAREHOUSE_CONTACT_NAME_ERROR);
			responseString.setMessage("联系人输入错误，请重新输入！");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.WAREHOUSE_CONTACT_NAME_ERROR, "联系人输入错误，请重新输入！");
		}

		if (!WarehouseUtil.validateCountry(house.getCountry())) {
			responseString.setCode(ResponseCode.WAREHOUSE_COUNTRY_ERROR);
			responseString.setMessage("国家输入格式错误，请重新输入！");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.WAREHOUSE_COUNTRY_ERROR, "国家输入格式错误，请重新输入！");
		}

		if (!WarehouseUtil.validateName(house.getName())) {
			responseString.setCode(ResponseCode.WAREHOUSE_NAME_ERROR);
			responseString.setMessage("门店名称输入格式错误，请重新输入！");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.WAREHOUSE_NAME_ERROR, "门店名称输入格式错误，请重新输入！");
		}

		if (!WarehouseUtil.validateProvince(house.getProvince())) {
			responseString.setCode(ResponseCode.WAREHOUSE_PROVINCE_ERROR);
			responseString.setMessage("州格式错误，请重新输入！");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.WAREHOUSE_PROVINCE_ERROR, "州格式错误，请重新输入！");
		}

		if (!WarehouseUtil.validateRemark(house.getRemark())) {
			responseString.setCode(ResponseCode.WAREHOUSE_REMARK_ERROR);
			responseString.setMessage("备注格式错误，请重新输入！");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.WAREHOUSE_REMARK_ERROR, "备注格式错误，请重新输入！");
		}

	
		if (!WarehouseUtil.validateTelephone(house.getTelephone())) {
			responseString.setCode(ResponseCode.WAREHOUSE_TELEPHONE_ERROR);
			responseString.setMessage("电话号码格式错误，请重新输入！");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.WAREHOUSE_TELEPHONE_ERROR, "电话号码格式错误，请重新输入！");
		}

		if (!WarehouseUtil.validateZipCode(house.getZipCode())) {
			responseString.setCode(ResponseCode.WAREHOUSE_ZIP_CODE_ERROR);
			responseString.setMessage("邮编格式错误，请重新输入！");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.WAREHOUSE_ZIP_CODE_ERROR, "邮编格式错误，请重新输入！");
		}
		String groupid = (String)request.getSession().getAttribute("emp_groupid_session_key");
	    house.setGroupId(groupid);
		try {
			ResponseObject<Object> obj= this.warehouseService.addWarehouse(house);
			responseString.setCode(obj.getCode());
			responseString.setMessage(obj.getMessage());
			return responseString.toString();
		} catch (Exception e) {
			log.error("添加门店失败", e);
			responseString.setCode(ResponseCode.SHOW_EXCEPTION);
			responseString.setMessage("添加门店出现异常");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "添加门店出现异常");
		}
	}

	@RequestMapping(value = "/admin/warehouse/modify",produces="text/plain;charset=UTF-8",  method = { RequestMethod.POST })
	@ResponseBody
	public String modifyWarehouse(HttpServletRequest request,Warehouse house) {
		ResponseString responseString=new ResponseString();
		String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
		{
			responseString.setCode(ResponseCode.EMPLOYEE_STORE_NAME_ERROR);
			responseString.setMessage("对不起，你无权修改门店!");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权修改门店!");
			
		}
		if (house == null) {
			responseString.setCode(ResponseCode.PARAMETER_ERROR);
			responseString.setMessage("参数无效");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数无效");
		}

		if (!WarehouseUtil.validateAddress(house.getAddress())) {
			responseString.setCode(ResponseCode.WAREHOUSE_ADDRESS_ERROR);
			responseString.setMessage("地址格式错误，请重新输入！");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.WAREHOUSE_ADDRESS_ERROR, "地址格式错误，请重新输入！");
		}

		if (!WarehouseUtil.validateCity(house.getCity())) {
			responseString.setCode(ResponseCode.WAREHOUSE_CITY_ERROR);
			responseString.setMessage("市格式错误，请重新输入！");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.WAREHOUSE_CITY_ERROR, "市格式错误，请重新输入！");
		}

		if (!WarehouseUtil.validateCompany(house.getCompany())) {
			responseString.setCode(ResponseCode.WAREHOUSE_COMPANY_ERROR);
			responseString.setMessage("公司名称输入错误，请重新输入！");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.WAREHOUSE_COMPANY_ERROR, "公司名称输入错误，请重新输入！");
		}

		if (!WarehouseUtil.validateContactName(house.getContactName())) {
			responseString.setCode(ResponseCode.WAREHOUSE_CONTACT_NAME_ERROR);
			responseString.setMessage("联系人输入错误，请重新输入！");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.WAREHOUSE_CONTACT_NAME_ERROR, "联系人输入错误，请重新输入！");
		}

		if (!WarehouseUtil.validateCountry(house.getCountry())) {
			responseString.setCode(ResponseCode.WAREHOUSE_COUNTRY_ERROR);
			responseString.setMessage("国家输入格式错误，请重新输入！");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.WAREHOUSE_COUNTRY_ERROR, "国家输入格式错误，请重新输入！");
		}

		if (!WarehouseUtil.validateName(house.getName())) {
			responseString.setCode(ResponseCode.WAREHOUSE_NAME_ERROR);
			responseString.setMessage("门店名称输入格式错误，请重新输入！");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.WAREHOUSE_NAME_ERROR, "门店名称输入格式错误，请重新输入！");
		}

		if (!WarehouseUtil.validateProvince(house.getProvince())) {
			responseString.setCode(ResponseCode.WAREHOUSE_PROVINCE_ERROR);
			responseString.setMessage("州格式错误，请重新输入！");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.WAREHOUSE_PROVINCE_ERROR, "州格式错误，请重新输入！");
		}

		if (!WarehouseUtil.validateRemark(house.getRemark())) {
			responseString.setCode(ResponseCode.WAREHOUSE_REMARK_ERROR);
			responseString.setMessage("备注格式错误，请重新输入！");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.WAREHOUSE_REMARK_ERROR, "备注格式错误，请重新输入！");
		}

	

		if (!WarehouseUtil.validateTelephone(house.getTelephone())) {
			responseString.setCode(ResponseCode.WAREHOUSE_TELEPHONE_ERROR);
			responseString.setMessage("电话号码格式错误，请重新输入！");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.WAREHOUSE_TELEPHONE_ERROR, "电话号码格式错误，请重新输入！");
		}

		if (!WarehouseUtil.validateZipCode(house.getZipCode())) {
			responseString.setCode(ResponseCode.WAREHOUSE_ZIP_CODE_ERROR);
			responseString.setMessage("邮编格式错误，请重新输入！");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.WAREHOUSE_ZIP_CODE_ERROR, "邮编格式错误，请重新输入！");
		}

		/*if(!StringUtil.isEmpty(house.getCallOrderCity()))
		{
			house.setCallOrderCity(house.getCallOrderCity().replaceAll("%0", "\n"));
		}*/
		try {
			ResponseObject<Object> obj= this.warehouseService.modifyWarehouse(house);
			responseString.setCode(obj.getCode());
			responseString.setMessage(obj.getMessage());
			return responseString.toString();
		} catch (Exception e) {
			
			//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改门店出现异常");
			responseString.setCode(ResponseCode.SHOW_EXCEPTION);
			responseString.setMessage("修改门店出现异常");
			return responseString.toString();
		}
	}

	@RequestMapping(value = "/admin/warehouse/del", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> deleteWarehouse(HttpServletRequest request,@RequestParam(value = "id") String[] ids) {
		try {
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
			{
				return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权删除门店!");
				
			}
			return this.warehouseService.deleteWarehouseByIds(ids);
		} catch (Exception e) {
			log.error("删除门店失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "根据id删除门店出现异常");
		}
	}

	@RequestMapping(value = "/admin/warehouse/get_one", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Warehouse> getOneWarehouse(@RequestParam(value = "id") String id) {
		try {
			return this.warehouseService.getWarehouseById(id);
		} catch (Exception e) {
			//log.error("根据id获取仓库失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "根据id获取门店出现异常");
		}
	}

	@RequestMapping(value = "/admin/warehouse/search", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<PageSplit<Warehouse>> searchWarehouse(
			HttpServletRequest request,
	        @RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex) {
		try {
			String groupid = (String)request.getSession().getAttribute(Constant.EMP_GROUPID_SESSION_KEY);
			pageIndex = Math.max(pageIndex, 1);
			return this.warehouseService.searchPageSplit(defaultPageSize, pageIndex,groupid);
		} catch (Exception e) {
			//log.error("获取仓库列表出现异常", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取参考列表失败!");
		}
	}
	
	
	@RequestMapping(value = "/admin/warehouse/addhavepic", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Map<String, String>> addwarehousehavepic(
			HttpServletRequest request,
			@RequestParam(value = "name", required = false, defaultValue = "") String name,	
			@RequestParam(value = "company", required = false, defaultValue = "") String company,
			@RequestParam(value = "country", required = false, defaultValue = "") String country,
			@RequestParam(value = "province", required = false, defaultValue = "") String province,
			@RequestParam(value = "city", required = false, defaultValue = "") String city,
			@RequestParam(value = "address", required = false, defaultValue = "") String address,
			@RequestParam(value = "zipCode", required = false, defaultValue = "") String zipCode,
			@RequestParam(value = "serialNo", required = false, defaultValue = "") String serialNo,
			@RequestParam(value = "contactName", required = false, defaultValue = "") String contactName,
			@RequestParam(value = "telephone", required = false, defaultValue = "") String telephone,
			@RequestParam(value = "telephoneofusa", required = false, defaultValue = "") String telephoneofusa,
			@RequestParam(value = "telephoneofcn", required = false, defaultValue = "") String telephoneofcn,
			@RequestParam(value = "remark", required = false, defaultValue = "") String remark,	
			@RequestParam(value = "widtype1", required = false, defaultValue = "") String type,
			@RequestParam(value = "printwidurl", required = false, defaultValue = "") String printwidurl,
			@RequestParam(value = "wid_code_char2", required = false, defaultValue = "") String printWidCode,
			@RequestParam(value = "filelogo", required = false) MultipartFile filelogo,
			@RequestParam(value = "file", required = false) MultipartFile file) {
		try {
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
			{
				return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权添加门店!");
				
			}
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
			if (file != null && file.getSize() > 0) {
				if (file.getSize() > this.defaultCardFileSize) {
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
				}

				String originalName = file.getOriginalFilename();

				if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
				}

				int index = originalName.lastIndexOf('.');
				index = Math.max(index, 0);
				fileName = this.saveGlobalDir + strseparator + "printpic" + "_"
						+ StringUtil.generateRandomString(5) + "_"
						+ StringUtil.generateRandomInteger(5)
						+ originalName.substring(index);
				try {
					File file1 = new File(request.getSession().getServletContext()
							.getRealPath("/")
							+ fileName);
					file.transferTo(file1);
				} catch (Exception e) {
					log.error("保存用户图像失败,请不要上传图像！", e);
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR,
							"保存用户图像失败，请去除上传图像后再尝试!");
				}
			}
			String filelogoname = null;
			if (filelogo != null && filelogo.getSize() > 0) {
				if (filelogo.getSize() > this.defaultCardFileSize) {
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
				}

				String originalName = filelogo.getOriginalFilename();

				if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
				}

				int index = originalName.lastIndexOf('.');
				index = Math.max(index, 0);
				filelogoname = this.saveGlobalDir + strseparator + "printpic" + "_"
						+ StringUtil.generateRandomString(5) + "_"
						+ StringUtil.generateRandomInteger(5)
						+ originalName.substring(index);
				try {
					File file1 = new File(request.getSession().getServletContext()
							.getRealPath("/")
							+ filelogoname);
					filelogo.transferTo(file1);
				} catch (Exception e) {
					log.error("保存用户图像失败,请不要上传图像！", e);
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR,
							"保存用户图像失败，请去除上传图像后再尝试!");
				}
			}
			
	
			Warehouse house=new Warehouse();
			house.setName(name);
			house.setCompany(company);
			house.setCountry(country);
			house.setProvince(province);
			house.setCity(city);
			house.setAddress(address);
			house.setZipCode(zipCode);
			house.setContactName(contactName);
			house.setTelephone(telephone);
			house.setRemark(remark);
			house.setPrintP_CN(telephoneofcn);
			house.setPrintP_USA(telephoneofusa);


			house.setType(type);

			house.setPrintWidCode(printWidCode);
			
			
			if (!WarehouseUtil.validateAddress(house.getAddress())) {
				return generateResponseObject(ResponseCode.WAREHOUSE_ADDRESS_ERROR, "地址格式错误，请重新输入！");
			}

			if (!WarehouseUtil.validateCity(house.getCity())) {
				return generateResponseObject(ResponseCode.WAREHOUSE_CITY_ERROR, "市格式错误，请重新输入！");
			}

			if (!WarehouseUtil.validateCompany(house.getCompany())) {
				return generateResponseObject(ResponseCode.WAREHOUSE_COMPANY_ERROR, "公司名称输入错误，请重新输入！");
			}

			if (!WarehouseUtil.validateContactName(house.getContactName())) {
				return generateResponseObject(ResponseCode.WAREHOUSE_CONTACT_NAME_ERROR, "联系人输入错误，请重新输入！");
			}

			if (!WarehouseUtil.validateCountry(house.getCountry())) {
				return generateResponseObject(ResponseCode.WAREHOUSE_COUNTRY_ERROR, "国家输入格式错误，请重新输入！");
			}

			if (!WarehouseUtil.validateName(house.getName())) {
				return generateResponseObject(ResponseCode.WAREHOUSE_NAME_ERROR, "门店名称输入格式错误，请重新输入！");
			}

			if (!WarehouseUtil.validateProvince(house.getProvince())) {
				return generateResponseObject(ResponseCode.WAREHOUSE_PROVINCE_ERROR, "州格式错误，请重新输入！");
			}

			if (!WarehouseUtil.validateRemark(house.getRemark())) {
				return generateResponseObject(ResponseCode.WAREHOUSE_REMARK_ERROR, "备注格式错误，请重新输入！");
			}

	

			if (!WarehouseUtil.validateTelephone(house.getTelephone())) {
				return generateResponseObject(ResponseCode.WAREHOUSE_TELEPHONE_ERROR, "电话号码格式错误，请重新输入！");
			}

			if (!WarehouseUtil.validateZipCode(house.getZipCode())) {
				return generateResponseObject(ResponseCode.WAREHOUSE_ZIP_CODE_ERROR, "邮编格式错误，请重新输入！");
			}
			
			
			String groupid = (String)request.getSession().getAttribute("emp_groupid_session_key");
		    house.setGroupId(groupid);
			
		    ResponseObject<Object> obj= this.warehouseService.addWarehouse(house);
			if(obj.getCode().equalsIgnoreCase(ResponseCode.SUCCESS_CODE))
			{
			
					return new ResponseObject<Map<String, String>>(
							ResponseCode.SUCCESS_CODE);
				
			}
			else
			{
				return new ResponseObject<Map<String, String>>(
						ResponseCode.PARAMETER_ERROR, obj.getMessage());
			}
		} catch (Exception e) {
			//log.error("添加仓库失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "添加门店出现异常");
		}
	}
	
	@RequestMapping(value = "/admin/warehouse/modifyhavepic", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Map<String, String>> modifywarehousehavepic(
			HttpServletRequest request,
			@RequestParam(value = "id", required = false, defaultValue = "") String id,	
			@RequestParam(value = "name", required = false, defaultValue = "") String name,	
			@RequestParam(value = "company", required = false, defaultValue = "") String company,
			@RequestParam(value = "country", required = false, defaultValue = "") String country,
			@RequestParam(value = "province", required = false, defaultValue = "") String province,
			@RequestParam(value = "city", required = false, defaultValue = "") String city,
			@RequestParam(value = "address", required = false, defaultValue = "") String address,
			@RequestParam(value = "zipCode", required = false, defaultValue = "") String zipCode,
			@RequestParam(value = "serialNo", required = false, defaultValue = "") String serialNo,
			@RequestParam(value = "contactName", required = false, defaultValue = "") String contactName,
			@RequestParam(value = "telephone", required = false, defaultValue = "") String telephone,
			@RequestParam(value = "telephoneofusa", required = false, defaultValue = "") String telephoneofusa,
			@RequestParam(value = "telephoneofcn", required = false, defaultValue = "") String telephoneofcn,
			@RequestParam(value = "remark", required = false, defaultValue = "") String remark,
			@RequestParam(value = "print2codepic", required = false, defaultValue = "") String print2codepic,
			@RequestParam(value = "printlogopic", required = false, defaultValue = "") String printlogopic,
			@RequestParam(value = "widtype1", required = false, defaultValue = "") String type,
			@RequestParam(value = "tran_wid_id_pic", required = false, defaultValue = "") String[] tran_wid_id_pic,
			@RequestParam(value = "tran_wid_cost", required = false, defaultValue = "") String[] tran_wid_cost,
			@RequestParam(value = "tran_wid_price", required = false, defaultValue = "") String[] tran_wid_price,
			@RequestParam(value = "tran_wid_selfPrice", required = false, defaultValue = "") String[] tran_wid_selfPrice,
			@RequestParam(value = "callOrderAvailable", required = false, defaultValue = "") String callOrderAvailable1,
			@RequestParam(value = "callOrderCity", required = false, defaultValue = "") String callOrderCity,
			@RequestParam(value = "wid_code_char2", required = false, defaultValue = "") String printWidCode,
			@RequestParam(value = "printwidurl", required = false, defaultValue = "") String printUrl,
			@RequestParam(value = "filelogo", required = false) MultipartFile filelogo,
			@RequestParam(value = "file", required = false) MultipartFile file) {
		try {
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
			{
				return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权添加门店!");
				
			}
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
			if (file != null && file.getSize() > 0) {
				if (file.getSize() > this.defaultCardFileSize) {
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
				}

				String originalName = file.getOriginalFilename();

				if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
				}

				int index = originalName.lastIndexOf('.');
				index = Math.max(index, 0);
				fileName = this.saveGlobalDir + strseparator + "printpic" + "_"
						+ StringUtil.generateRandomString(5) + "_"
						+ StringUtil.generateRandomInteger(5)
						+ originalName.substring(index);
				try {
					File file1 = new File(request.getSession().getServletContext()
							.getRealPath("/")
							+ fileName);
					file.transferTo(file1);
				} catch (Exception e) {
					log.error("保存用户图像失败,请不要上传图像！", e);
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR,
							"保存用户图像失败，请去除上传图像后再尝试!");
				}
			}
			else
			{
				fileName=print2codepic;
			}
			
			String filelogoName = null;
			if (filelogo != null && filelogo.getSize() > 0) {
				if (filelogo.getSize() > this.defaultCardFileSize) {
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
				}

				String originalName = filelogo.getOriginalFilename();

				if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
				}

				int index = originalName.lastIndexOf('.');
				index = Math.max(index, 0);
				filelogoName = this.saveGlobalDir + strseparator + "printpic" + "_"
						+ StringUtil.generateRandomString(5) + "_"
						+ StringUtil.generateRandomInteger(5)
						+ originalName.substring(index);
				try {
					File file1 = new File(request.getSession().getServletContext()
							.getRealPath("/")
							+ filelogoName);
					filelogo.transferTo(file1);
				} catch (Exception e) {
					log.error("保存用户图像失败,请不要上传图像！", e);
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR,
							"保存用户图像失败，请去除上传图像后再尝试!");
				}
			}
			else
			{
				filelogoName=printlogopic;
			}
	
			Warehouse house=new Warehouse();
			house.setName(name);
			house.setCompany(company);
			house.setCountry(country);
			house.setProvince(province);
			house.setCity(city);
			house.setAddress(address);
			house.setZipCode(zipCode);

			house.setContactName(contactName);
			house.setTelephone(telephone);
			house.setRemark(remark);
			
			house.setPrintP_CN(telephoneofcn);
			house.setPrintP_USA(telephoneofusa);
	

			house.setId(id);
			house.setType(type);
			house.setCallOrderCity(callOrderCity);
			house.setCallOrderAvailable(callOrderAvailable1);
			house.setPrintWidCode(printWidCode);

			
		
			
			if((tran_wid_id_pic!=null)&&(tran_wid_id_pic.length>0))
			{
				TranPrice[] tranprice= new TranPrice[tran_wid_id_pic.length];
				for(int i=0;i<tran_wid_id_pic.length;i++)
				{
					tranprice[i]=new TranPrice();
					tranprice[i].setCost(tran_wid_cost[i]);
					tranprice[i].setPrice(tran_wid_price[i]);
					tranprice[i].setSelfPrice(tran_wid_selfPrice[i]);
					tranprice[i].setTranwarehouseId(tran_wid_id_pic[i]);
					tranprice[i].setWarehouseId(id);;
				}
				//house.setTranprice(tranprice);
			}
		

			if (!WarehouseUtil.validateAddress(house.getAddress())) {
				return generateResponseObject(ResponseCode.WAREHOUSE_ADDRESS_ERROR, "地址格式错误，请重新输入！");
			}

			if (!WarehouseUtil.validateCity(house.getCity())) {
				return generateResponseObject(ResponseCode.WAREHOUSE_CITY_ERROR, "市格式错误，请重新输入！");
			}

			if (!WarehouseUtil.validateCompany(house.getCompany())) {
				return generateResponseObject(ResponseCode.WAREHOUSE_COMPANY_ERROR, "公司名称输入错误，请重新输入！");
			}

			if (!WarehouseUtil.validateContactName(house.getContactName())) {
				return generateResponseObject(ResponseCode.WAREHOUSE_CONTACT_NAME_ERROR, "联系人输入错误，请重新输入！");
			}

			if (!WarehouseUtil.validateCountry(house.getCountry())) {
				return generateResponseObject(ResponseCode.WAREHOUSE_COUNTRY_ERROR, "国家输入格式错误，请重新输入！");
			}

			if (!WarehouseUtil.validateName(house.getName())) {
				return generateResponseObject(ResponseCode.WAREHOUSE_NAME_ERROR, "门店名称输入格式错误，请重新输入！");
			}

			if (!WarehouseUtil.validateProvince(house.getProvince())) {
				return generateResponseObject(ResponseCode.WAREHOUSE_PROVINCE_ERROR, "州格式错误，请重新输入！");
			}

			if (!WarehouseUtil.validateRemark(house.getRemark())) {
				return generateResponseObject(ResponseCode.WAREHOUSE_REMARK_ERROR, "备注格式错误，请重新输入！");
			}

	

			if (!WarehouseUtil.validateTelephone(house.getTelephone())) {
				return generateResponseObject(ResponseCode.WAREHOUSE_TELEPHONE_ERROR, "电话号码格式错误，请重新输入！");
			}

			if (!WarehouseUtil.validateZipCode(house.getZipCode())) {
				return generateResponseObject(ResponseCode.WAREHOUSE_ZIP_CODE_ERROR, "邮编格式错误，请重新输入！");
			}
			
			String groupid = (String)request.getSession().getAttribute("emp_groupid_session_key");
		    house.setGroupId(groupid);
			

		    
		    ResponseObject<Object> obj=this.warehouseService.modifyWarehouse(house);
			if(obj.getCode().equalsIgnoreCase(ResponseCode.SUCCESS_CODE))
			{
			
					return new ResponseObject<Map<String, String>>(
							ResponseCode.SUCCESS_CODE);
				
			}
			else
			{
				return new ResponseObject<Map<String, String>>(
						ResponseCode.PARAMETER_ERROR, obj.getMessage());
			}
		} catch (Exception e) {
			//log.error("修改仓库失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改门店出现异常");
		}
	}
	
	@RequestMapping(value = "/user/warehouse/get_one", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Warehouse> getOneWarehousebyuser(HttpServletRequest request,@RequestParam(value = "id") String id) {
		try {
		/*	String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
			{
				String storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
				if(storeid!=id)
				{
					return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权查看此仓库门店!");
				}
			}
			else
			{
				
			}*/
			if(StringUtil.isEmpty(id))
			{
				return generateResponseObject(ResponseCode.PARAMETER_ERROR, "查询门店id不能为空!");
			}
			return this.warehouseService.getWarehouseById(id);
		} catch (Exception e) {
			//log.error("根据id获取仓库失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "根据id获取门店出现异常");
		}
	}
	
	
	
	//获得门店信息，要与权限结合
	@RequestMapping(value = {"/admin/warehouse/getself" }, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public ResponseObject<List<Warehouse>> getselfWarehouse(HttpServletRequest request) {
		try {
			
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			String storeids=null;
			if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
			{
				String storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
				if(StringUtil.isEmpty(storeid))
				{
					return new ResponseObject<List<Warehouse>>(ResponseCode.NEED_LOGIN,"请先登陆后操作");
				}
				else
				{
					
					storeids=storeid;
				}
				
			}
			return this.warehouseService.getwarehousebyadmin(storeids);
		} catch (Exception e) {
			//log.error("获取仓库列表失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取门店列表失败");
		}
	}
	
	
	
	
	@RequestMapping(value = "/admin/warehouse/search_admin", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<PageSplit<Warehouse>> searchWarehousebyadmin(
			HttpServletRequest request,
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
			@RequestParam(value = "rows", required = false, defaultValue = "10") int rows) {
		try {
			//String groupid = (String)request.getSession().getAttribute(Constant.EMP_GROUPID_SESSION_KEY);
			pageIndex = Math.max(pageIndex, 1);
			
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			String storeids=null;
			if((!StringUtil.isEmpty(supperadmin))&&(supperadmin.equalsIgnoreCase("1")))
			{
				
			}
			else
			{
				storeids = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
			}
			
			
			
			return this.warehouseService.searchPageSplitbyadmin(keyword, storeids,rows, pageIndex);
			//return this.warehouseService.searchPageSplit(rows, pageIndex,groupid);
		} catch (Exception e) {
			//log.error("获取仓库列表出现异常", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取参考列表失败!");
		}
	}
	
	
	
	
	//获得仓库信息，要与权限结合,并且一定要是店长才能够操作
		@RequestMapping(value = {"/admin/warehouse/getself_store" }, method = { RequestMethod.GET,RequestMethod.POST})
		@ResponseBody
		public ResponseObject<List<Warehouse>> getselfStoreByAdmin(HttpServletRequest request) {
			try {
				
				String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
				String storeids=null;
				if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
				{
					String storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
					if(StringUtil.isEmpty(storeid))
					{
						return new ResponseObject<List<Warehouse>>(ResponseCode.NEED_LOGIN,"请先登陆后操作");
					}
					else
					{
						String storeMaster=(String)request.getSession().getAttribute(Constant.EMP_STORE_MASTER_SESSION_KEY);
						if(StringUtil.isEmpty(storeMaster)||(!storeMaster.equalsIgnoreCase("1")))
						{
							return new ResponseObject<List<Warehouse>>(ResponseCode.PARAMETER_ERROR,"对不起，只有店主或高级管理员能够操作!");
						}
						else
						{
							storeids=storeid;
						}
					}
					
				}
				return this.warehouseService.getwarehousebyadmin(storeids);
			} catch (Exception e) {
				//log.error("获取仓库列表失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取门店列表失败");
			}
		}
	
		//前端获取门店信息
		@RequestMapping(value = { "/store/guest_store_list" }, method = { RequestMethod.GET })
		@ResponseBody
		public ResponseObject<List<Warehouse>> getmdinfobyguest() {
			try {
				ResponseObject<List<Warehouse>> obj= this.warehouseService.getAll();
				if((obj!=null)&&(ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj.getCode())))
				{
					//替换里面不必分的数据
					List<Warehouse> list=(List<Warehouse>)obj.getData();
					for(Warehouse are:list)
					{
						are.setCallOrderCity("");
						are.setCallOrderAvailable("");
						are.setPrintP_USA("");
						are.setPrintP_CN("");
						are.setPrintWidCode("");
						are.setRemark("");
					}
					obj.setData(list);
				}
				return obj;
			} catch (Exception e) {
				log.error("获取门店列表失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取门店列表失败");
			}
		}
}

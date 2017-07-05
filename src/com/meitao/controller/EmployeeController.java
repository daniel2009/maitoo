package com.meitao.controller;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;






import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.meitao.service.EmployeeService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ParameterConstants;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.EmployeeUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.exception.ServiceException;
import com.meitao.model.Authority_url;
import com.meitao.model.Employee;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;

@Controller
public class EmployeeController extends BasicController {
	private static final long serialVersionUID = -8357674012674301073L;
	private static final Logger log = Logger.getLogger(EmployeeController.class);

	@Resource(name = "employeeService")
	private EmployeeService employeeService;
	@Value(value = "${page_size}")
	private int defaultPageSize;
	@Value(value = "${save_emp_pic_dir}")
	private String saveEmpPicDir;
	@Value(value = "${default_img_size}")
	private long defaultCardFileSize;
	


	@Value(value = "${default_img_type}")
	private String defaultCardFileType;

	@RequestMapping(value = "/admin/emp/login", method = { RequestMethod.POST })
	@ResponseBody
	public Object login(HttpServletRequest request,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_ACCOUNT) String account,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_PASSWORD) String password,
	        @RequestParam(value = ParameterConstants.COMMON_VALIDATE_CODE) String vcode) {
		ResponseObject<Employee> responseObj = null;
		if (!checkVerifyCode(request, vcode)) {
			responseObj = generateResponseObject(ResponseCode.VALIDATE_CODE_ERROR, "验证码错误，请重新输入！");
		} else if (!EmployeeUtil.validateAccountName(account)) {
			responseObj = new ResponseObject<Employee>(ResponseCode.EMPLOYEE_ACCOUNT_ERROR, "请重新输入账号！");
		} else if (!EmployeeUtil.validatePassword(password)) {
			responseObj = new ResponseObject<Employee>(ResponseCode.EMPLOYEE_PASSWORD_ERROR, "请重新输入密码！");
		} else {
			try {
				responseObj = this.employeeService.checkLogin(account, password);
				String code = responseObj.getCode();
				if (ResponseCode.SUCCESS_CODE.equals(code)) {
					HttpSession session = request.getSession();
					session.setAttribute(Constant.EMP_ID_SESSION_KEY, responseObj.getData().getId());
					session.setAttribute(Constant.EMP_ACCOUNT_SESSION_KEY, responseObj.getData().getAccount());
					session.setAttribute(Constant.EMP_GROUPID_SESSION_KEY, responseObj.getData().getGroupId());
					session.setAttribute(Constant.EMP_STORE_ID_SESSION_KEY, responseObj.getData().getStoreId());
					session.setAttribute(Constant.EMP_STORE_NAME_SESSION_KEY, responseObj.getData().getStoreName());
					
					//添加店主记录
					if(!StringUtil.isEmpty(responseObj.getData().getStoreMaster())&&(responseObj.getData().getStoreMaster().equalsIgnoreCase("1")))
					{
						session.setAttribute(Constant.EMP_STORE_MASTER_SESSION_KEY, "1");//这是店主
					}
					else
					{
						session.setAttribute(Constant.EMP_STORE_MASTER_SESSION_KEY, "0");
					}
					
					
					session.setAttribute(Constant.LOGIN_ACCOUNT_TYPE, "1");
					if((responseObj.getData().getSuperadmin()!=null)&&(responseObj.getData().getSuperadmin().equalsIgnoreCase("1")))
					{
						
						session.setAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY, "1");
						session.setAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_flag, "admin");
						
					}
					else
					{
						session.setAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY, "0");
						session.setAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_flag, "unadmin");
					}
					
				}
			} catch (Exception e) {
				log.error("调用后台代码出错", e);
				responseObj = generateResponseObject(ResponseCode.SHOW_EXCEPTION, "根据id获取职工信息失败!");
			}
		}
		return responseObj;
	}

	@RequestMapping(value = "/admin/emp/logout", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute(Constant.EMP_ID_SESSION_KEY);
		session.removeAttribute(Constant.EMP_ACCOUNT_SESSION_KEY);
		session.removeAttribute(Constant.LOGIN_ACCOUNT_TYPE);
		session.removeAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		session.invalidate();
		return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
	}

	@RequestMapping(value = "/admin/emp/add", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> addEmployee(
	        HttpServletRequest request,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_ACCOUNT) String account,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_PASSWORD) String password,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_STORE_ID, required = false, defaultValue = "") String storeId,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_STORE_NAME, required = false, defaultValue = "") String storeName,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_PHONE, required = false) String phone,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_PIC, required = false) MultipartFile pic,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_AUTH, required = false) List<String> authorityids){
		
		String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
		{
			return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权添加职工!");
			
		}
		
		if (!EmployeeUtil.validateAccountName(account)) {
			return generateResponseObject(ResponseCode.EMPLOYEE_ACCOUNT_ERROR, "账户格式不正确，请重新输入");
		}

		if (!EmployeeUtil.validatePassword(password)) {
			return generateResponseObject(ResponseCode.EMPLOYEE_PASSWORD_ERROR, "密码格式不正确，请重新输入！");
		}

		if (!EmployeeUtil.validateStoreId(storeId)) {
			return generateResponseObject(ResponseCode.EMPLOYEE_STORE_ID_ERROR, "门市编号格式不正确，请重新输入！");
		}

		if (!EmployeeUtil.validateStoreName(storeName)) {
			return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "门市名称格式不正确，请重新输入！");
		}

		if (!EmployeeUtil.validatePhone(phone)) {
			return generateResponseObject(ResponseCode.EMPLOYEE_PHONE_ERROR, "手机号码格式不正确，请重新输入!");
		}

		
		
		
		//解决火狐的反斜杠问题 kai 20151006
				String filetype=this.defaultCardFileType;//要上传的文件类型
				String strtest=this.saveEmpPicDir;
				String strseparator="";
				if(strtest.indexOf("/")>=0)//包含有“/”字符，分隔符用此字符
				{
					strseparator="/";
				}
				else
				{
					strseparator=File.separator;
				}
		
		String fileName = null;
		if (pic != null && pic.getSize() > 0) {
			if (pic.getSize() > this.defaultCardFileSize) {
				return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
			}
			String originalName = pic.getOriginalFilename();
			if(!StringUtil.boolpicisgoodornot(originalName,filetype))
			{
				return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
			}
			
			int index = originalName.lastIndexOf('.');
			index = Math.max(index, 0);
			fileName = this.saveEmpPicDir + strseparator + StringUtil.generateRandomString(5) + "_"
			        + StringUtil.generateRandomInteger(5) + originalName.substring(index);
			try {
				File file1 = new File(request.getSession().getServletContext().getRealPath("/") + fileName);
				pic.transferTo(file1);
			} catch (Exception e) {
				log.error("保存图像失败,请不要上传图像！");
				return generateResponseObject(ResponseCode.EMPLOYEE_PIC_ERROR, "保存图像失败!");
			}
		}
		String groupid = (String)request.getSession().getAttribute(Constant.EMP_GROUPID_SESSION_KEY);
		Employee employee = new Employee();
		employee.setAccount(account);
		employee.setPassword(password);
		employee.setStoreId(storeId);
		employee.setStoreName(storeName);
		employee.setPhone(phone);
		employee.setPicUrl(fileName);
		employee.setGroupId(groupid);
		try {
			ResponseObject<Employee> emObject = this.employeeService.getByAccoutName(account);
			if (ResponseCode.EMPLOYEE_ACCOUNT_NOT_EXISTS.equals(emObject.getCode())) {
				return this.employeeService.addEmployee(employee,authorityids);
				//return this.employeeService.addEmployee(employee);
				
			} else {
				return generateResponseObject(ResponseCode.EMPLOYEE_ACCOUNT_EXISTS, "该用户已经存在");
			}
		} catch (Exception e) {
			log.error("添加职工失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "添加员工失败!");
		}
	}

	@RequestMapping(value = "/admin/emp/add_no_pic", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> addEmployeeNoPic(
			HttpServletRequest request,
			@RequestParam(value = ParameterConstants.EMPLOYEE_STORE_Master) String storeMaster,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_ACCOUNT) String account,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_PASSWORD) String password,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_STORE_ID, required = false, defaultValue = "") String storeId,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_STORE_NAME, required = false, defaultValue = "") String storeName,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_PHONE, required = false) String phone,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_AUTH, required = false) List<String> authorityids){
		//System.out.println("====#########打印权限列表1111111#######=======");
		//System.out.println(authorityids);
		String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
		{
			return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权添加职工!");
			
		}
		
		if (!EmployeeUtil.validateAccountName(account)) {
			return generateResponseObject(ResponseCode.EMPLOYEE_ACCOUNT_ERROR, "账户格式不正确，请重新输入");
		}

		if (!EmployeeUtil.validatePassword(password)) {
			return generateResponseObject(ResponseCode.EMPLOYEE_PASSWORD_ERROR, "密码格式不正确，请重新输入！");
		}

		if (!EmployeeUtil.validateStoreId(storeId)) {
			return generateResponseObject(ResponseCode.EMPLOYEE_STORE_ID_ERROR, "门市编号格式不正确，请重新输入！");
		}

		if (!EmployeeUtil.validateStoreName(storeName)) {
			return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "门市名称格式不正确，请重新输入！");
		}

		if (!EmployeeUtil.validatePhone(phone)) {
			return generateResponseObject(ResponseCode.EMPLOYEE_PHONE_ERROR, "手机号码格式不正确，请重新输入!");
		}

		String fileName = null;
		String groupid = (String)request.getSession().getAttribute(Constant.EMP_GROUPID_SESSION_KEY);
		Employee employee = new Employee();
		employee.setAccount(account);
		employee.setPassword(password);
		employee.setStoreId(storeId);
		employee.setStoreName(storeName);
		employee.setPhone(phone);
		employee.setPicUrl(fileName);
		employee.setGroupId(groupid);
		employee.setStoreMaster(storeMaster);
		
		
		try {
			ResponseObject<Employee> emObject = this.employeeService.getByAccoutName(account);
			if (ResponseCode.EMPLOYEE_ACCOUNT_NOT_EXISTS.equals(emObject.getCode())) {
				//System.out.println("====#########进入if方法#######=======");
				//System.out.println("====#########打印权限列表22222222222#######=======");
				//System.out.println(authorityids);
				return this.employeeService.addEmployee(employee,authorityids);
			} else {
				return generateResponseObject(ResponseCode.EMPLOYEE_ACCOUNT_EXISTS, "该用户名已经存在");
			}
		} catch (Exception e) {
			log.error("添加职工失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "添加员工失败!");
		}
	}

	@RequestMapping(value = "/admin/emp/delete", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> delEmployee(HttpServletRequest request,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_ID) String id) {
		
		String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
		{
			return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权删除职工信息!");
			
		}
		
		if (!EmployeeUtil.validateId(id)) {
			return generateResponseObject(ResponseCode.EMPLOYEE_ID_ERROR, "参数无效");
		}

		String empId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY));
		if (id.equals(empId)) {
			return generateResponseObject(ResponseCode.EMPLOYEE_DELETE_FAILURE, "不能删除自己");
		}

		try {
			return this.employeeService.deleteEmployee(id);
		} catch (Exception e) {
			log.error("根据id删除职工失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "删除员工失败!");
		}
	}

	@RequestMapping(value = "/admin/emp/modifybyadmin", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> modifyEmployee(
			HttpServletRequest request,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_ID) String id,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_STORE_Master, required = false, defaultValue = "") String storeMaster,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_STORE_ID, required = false, defaultValue = "") String storeId,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_STORE_NAME, required = false, defaultValue = "") String storeName,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_PHONE, required = false) String phone,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_PASSWORD, required = false) String password,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_AUTH, required = false) List<String> authorityids) {
		//打印验证
		
		String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
		{
			return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权修改职工信息!");
			
		}
		//System.out.println("=======######这里是无图controller修改员工的员工id########======");
		//System.out.println(id);
		//System.out.println("=======######这里是无图controller修改员工的员工的 权限列表########======");
		//System.out.println(authorityids);
		if (!EmployeeUtil.validateStoreId(storeId)) {
			return generateResponseObject(ResponseCode.EMPLOYEE_STORE_ID_ERROR, "门市编号格式不正确，请重新输入！");
		}

		if (!EmployeeUtil.validateStoreName(storeName)) {
			return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "门市名称格式不正确，请重新输入！");
		}

		if (!EmployeeUtil.validatePhone(phone)) {
			return generateResponseObject(ResponseCode.EMPLOYEE_PHONE_ERROR, "电话号码格式不正确，请重新输入！");
		}

		if (!EmployeeUtil.validatePassword(password)&&password!=null&&!password.equals("")) {
			return generateResponseObject(ResponseCode.EMPLOYEE_PASSWORD_ERROR, "密码格式不正确，请重新输入！");
		}
		Employee employee = new Employee();
		employee.setId(id);
		employee.setStoreId(storeId);
		employee.setStoreName(storeName);
		employee.setPhone(phone);
		employee.setStoreMaster(storeMaster);
		if (password!=null&&!password.equals("")) {
			employee.setPassword(password);
		}
		try {
			return this.employeeService.modifyEmployee(employee,authorityids);
		} catch (Exception e) {
			log.error("修改职工信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改职工信息失败!");
		}
	}

	@RequestMapping(value = "/admin/emp/modify", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> modifyEmployeeWithPic(
	        HttpServletRequest request,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_ID) String id,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_STORE_ID, required = false, defaultValue = "") String storeId,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_STORE_NAME, required = false, defaultValue = "") String storeName,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_PHONE, required = false) String phone,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_PASSWORD, required = false) String password,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_PIC, required = false) MultipartFile pic,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_AUTH, required = false) List<String> authorityids) {
		
		String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
		{
			return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权修改职工信息!");
			
		}
		
		System.out.println("====这是controller有图修改员工信息的 员工id====");
		System.out.println(id);
		if (!EmployeeUtil.validateStoreId(storeId)) {
			return generateResponseObject(ResponseCode.EMPLOYEE_STORE_ID_ERROR, "门市编号格式不正确，请重新输入！");
		}

		if (!EmployeeUtil.validateStoreName(storeName)) {
			return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "门市名称格式不正确，请重新输入！");
		}

		if (!EmployeeUtil.validatePhone(phone)) {
			return generateResponseObject(ResponseCode.EMPLOYEE_PHONE_ERROR, "电话号码格式不正确，请重新输入！");
		}
		
		if (!EmployeeUtil.validatePassword(password)&&password!=null&&!password.equals("")) {
			return generateResponseObject(ResponseCode.EMPLOYEE_PASSWORD_ERROR, "密码格式不正确，请重新输入！");
		}

		//解决火狐的反斜杠问题 kai 20151006
		String filetype=this.defaultCardFileType;//要上传的文件类型
		String strtest=this.saveEmpPicDir;
		String strseparator="";
		if(strtest.indexOf("/")>=0)//包含有“/”字符，分隔符用此字符
		{
			strseparator="/";
		}
		else
		{
			strseparator=File.separator;
		}
		
		String fileName = null;
		if (pic != null && pic.getSize() > 0) {
			if (pic.getSize() > this.defaultCardFileSize) {
				return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
			}
			String originalName = pic.getOriginalFilename();
			int index = originalName.lastIndexOf('.');
			if(!StringUtil.boolpicisgoodornot(originalName,filetype))
			{
				return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
			}
			
			index = Math.max(index, 0);
			fileName = this.saveEmpPicDir + strseparator + StringUtil.generateRandomString(5) + "_"
			        + StringUtil.generateRandomInteger(5) + originalName.substring(index);
			try {
				File file1 = new File(request.getSession().getServletContext().getRealPath("/") + fileName);
				pic.transferTo(file1);
			} catch (Exception e) {
				log.error("保存图像失败,请不要上传图像！");
				return generateResponseObject(ResponseCode.EMPLOYEE_PIC_ERROR, "保存图像失败!");
			}
		}

		String oldFileName = null;
		if (!StringUtil.isEmpty(fileName)) {
			try {
				ResponseObject<Employee> tmpEmployee = this.employeeService.getById(id);
				if (tmpEmployee != null && ResponseCode.SUCCESS_CODE.equals(tmpEmployee.getCode())) {
					oldFileName = tmpEmployee.getData().getPicUrl();
				}
			} catch (Exception e) {
				log.error("根据id获取职工数据失败", e);
			}
		}

		Employee employee = new Employee();
		employee.setId(id);
		employee.setStoreId(storeId);
		employee.setStoreName(storeName);
		employee.setPhone(phone);
		employee.setPicUrl(fileName);

		if (password!=null&&!password.equals("")) {
			employee.setPassword(password);
		}
		try {
			ResponseObject<Object> result = this.employeeService.modifyEmployee(employee,authorityids);
			if (result != null && ResponseCode.SUCCESS_CODE.equals(result.getCode()) && !StringUtil.isEmpty(fileName)
			        && !StringUtil.isEmpty(oldFileName)) {
				try {
					File file1 = new File(request.getSession().getServletContext().getRealPath("/") + oldFileName);
					if (file1.exists()) {
						file1.delete();
					}
				} catch (Exception e) {
					log.error("删除原有的职工图像失败", e);
				}
			}
			return result;
		} catch (Exception e) {
			log.error("修改职工信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改职工信息失败!");
		}
	}

	@RequestMapping(value = "/admin/emp/reset_pwd", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> modifyPassword(HttpServletRequest request,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_PASSWORD) String password,
	        @RequestParam(value = ParameterConstants.COMMON_OLD_PASSWORD) String oldpwd,
	        @RequestParam(value = ParameterConstants.COMMON_VALIDATE_CODE) String vcode) {

		if (!checkVerifyCode(request, vcode)) {
			return generateResponseObject(ResponseCode.VALIDATE_CODE_ERROR, "验证码错误，请重新输入！");
		}

		if (!EmployeeUtil.validatePassword(password)) {
			return generateResponseObject(ResponseCode.EMPLOYEE_PASSWORD_ERROR, "密码格式不正确，请重新输入！");
		}

		if (!EmployeeUtil.validatePassword(oldpwd)) {
			return generateResponseObject(ResponseCode.EMPLOYEE_PASSWORD_ERROR, "原密码格式不正确，请重新输入！");
		}

		try {
			String empId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY));
			return this.employeeService.modifyPassword(empId, password, oldpwd);
		} catch (Exception e) {
			log.error("修改密码失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改用户密码失败!");
		}
	}

	@RequestMapping(value = "/admin/emp/get_self", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Employee> getSelf(HttpServletRequest request) {
		try {
			String id = (String)request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY);
			if(StringUtil.isEmpty(id))
			{
				return new ResponseObject<Employee>(ResponseCode.NEED_LOGIN);
			}
			return this.employeeService.getById(id);
		} catch (Exception e) {
			log.error("根据我的信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取我的信息失败!");
		}
	}
	
	@RequestMapping(value = "/admin/emp/get_one", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Employee> getOne(HttpServletRequest request,@RequestParam(value = ParameterConstants.EMPLOYEE_ID) String id) {
		if (!EmployeeUtil.validateId(id)) {
			return generateResponseObject(ResponseCode.EMPLOYEE_ID_ERROR, "参数无效");
		}

		String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		String self_id = (String)request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY);
		
		if((supperadmin!=null)&&(!supperadmin.equalsIgnoreCase("1")))//不是超级管理员
		{
			if(!id.equalsIgnoreCase(self_id))
			{
			return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权查看其它职工信息!");
			}
			
		}
		try {
			return this.employeeService.getById(id);
		} catch (Exception e) {
			log.error("根据id获取职工信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取一个职工信息失败!");
		}
	}

	@RequestMapping(value = "/admin/emp/search", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<PageSplit<Employee>> searchAll(HttpServletRequest request,
	        @RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex) {
		try {
			pageIndex = Math.max(pageIndex, 1);
			
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			String self_id =null;
			
			if((supperadmin!=null)&&(!supperadmin.equalsIgnoreCase("1")))//不是超级管理员
			{
				self_id = (String)request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY);
				
			}
			
			String groupid = (String)request.getSession().getAttribute(Constant.EMP_GROUPID_SESSION_KEY);
			return this.employeeService.searchAll(defaultPageSize, pageIndex, groupid,self_id);
		} catch (Exception e) {
			log.error("获取职工列表出现异常", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取职工列表失败!");
		}
	}
	
	/**
	 * 获取员工权限信息接口
	 * 张敬琦
	 * 2015-01-28
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/admin/emp/getmenuinfo", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<List<Authority_url>> getMenuInfo(HttpServletRequest request) {
		//System.out.println("====#########获取权限列表#######=======");
		String eid = (String) request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY);
		//System.out.println("====#########获取权限列表#######=======");
		//System.out.println(eid);
		String superadmin = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY)); 
		try {
			//System.out.println("====#########开始获取权限列表#######=======");
			if(superadmin.equals("1")){
				return this.employeeService.getAllMenuInfo();
			}
	        return this.employeeService.getMenuInfoByEmployeeId(eid);  
        } catch (ServiceException e) {
        	log.error("获取员工可操作权限失败", e);
        	return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取员工可操作权限失败");
        }
		
	}
	/**
	 * 修改员工权限接口
	 * 张敬琦
	 * 2015-01-29
	 * @param request
	 * @param eid
	 * @param authorityids
	 * @return
	 */
	@RequestMapping(value = "/admin/emp/modifyauthority", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> modifyAuthority( HttpServletRequest request,
			 @RequestParam(value = ParameterConstants.EMPLOYEE_ID) String eid,
	        @RequestParam(value = ParameterConstants.EMPLOYEE_AUTH, required = false) List<String> authorityids){
		//System.out.println("====#########开始获取职工id#######=======");
		//System.out.println(eid);
		//System.out.println("======########获取当前职工的权限数列###########===========");
		//System.out.println(authorityids);
		try {
			return this.employeeService.modifyAuthority(eid, authorityids);
        } catch (ServiceException e) {
        	log.error("获取员工可操作权限失败", e);
        	return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取员工可操作权限失败");
        }
		
	}
	@RequestMapping(value = "/admin/emp/getRealTimeCountInNav", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject<String[]> getRealTimeCountInNav(HttpServletRequest request){
		String warehouseId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
		if("1".equals(StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY)))){
			warehouseId = null;
		}
		try{
			return this.employeeService.getRealTimeCount4AdminNav(warehouseId);
		}catch(Exception e){
			log.error("根据管理员门店获取菜单实时数量异常");
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "根据管理员门店获取菜单实时数量异常");
		}
	}
	
	
	
	
	@RequestMapping(value = "/admin/emp/search_admin", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<PageSplit<Employee>> searchAllbyadmin(HttpServletRequest request,
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(value = "empId", required = false, defaultValue = "") String empId,
			@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
			@RequestParam(value = "rows", required = false, defaultValue = "10") int rows) {
		try {
			pageIndex = Math.max(pageIndex, 1);
			
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			//String self_id =(String)request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY);
			String storeId=null;
			if((supperadmin!=null)&&(!supperadmin.equalsIgnoreCase("1")))//不是超级管理员
			{
				storeId=(String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
				
			}
			
			//String groupid = (String)request.getSession().getAttribute(Constant.EMP_GROUPID_SESSION_KEY);
			//return this.employeeService.searchAll(rows, pageIndex, groupid,self_id);
			return this.employeeService.searchAllbyadmin(rows, pageIndex, keyword, storeId, empId);
		} catch (Exception e) {
			log.error("获取职工列表出现异常", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取职工列表失败!");
		}
	}
} 
	
	
	
	

	
	

	
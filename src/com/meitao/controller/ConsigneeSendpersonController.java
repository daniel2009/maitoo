package com.meitao.controller;



import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;







import com.meitao.service.ConsigneeSendpersonService;
import com.meitao.service.UserService;

import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ParameterConstants;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.ConsigneeInfoUtil;
import com.meitao.common.util.StringUtil;

import com.meitao.model.ConsigneeSendperson;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;


@Controller
public class ConsigneeSendpersonController extends BasicController {

	private static final long serialVersionUID = 1455632086294967055L;
	private static final Logger log = Logger
			.getLogger(ConsigneeSendpersonController.class);

	@Resource(name = "consigneeSendpersonService")
	private ConsigneeSendpersonService consigneeSendpersonService;
	@Value(value = "${save_card_dir}")
	private String saveCardDir;
	@Value(value = "${default_img_size}")
	private long defaultCardFileSize;
	@Value(value = "${default_img_type}")
	private String defaultCardFileType;

	@Value(value = "${page_size}")
	private int defaultPageSize;

	@Resource(name = "userService")
	private UserService userService;
	
	@Value(value = "${save_258_card_url}")
	private String save258CardDir;



	
	@RequestMapping(value = "/consignee_sendperson/add", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> addConsignee(
			HttpServletRequest request,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "province") String province,
			@RequestParam(value = "city") String city,
			@RequestParam(value = "address") String sAdd,
			@RequestParam(value = "zipcode") String zcode,
			@RequestParam(value = "phone") String phone,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "company", required = false, defaultValue = "") String company) {
		if (StringUtil.isEmpty(name)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_NAME_ERROR,
					"发件人姓名不能留空，请重新输入!");
		}

	


		if ((!StringUtil.isEmpty(zcode))&&!ConsigneeInfoUtil.validateZipCode(zcode)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_ZIP_CODE_ERROR, "邮编填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validatePhone(phone)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR,
					"手机号码填写不正确，请重新输入！");
		}

	



		String userId = StringUtil.obj2String(request.getSession()
				.getAttribute(Constant.USER_ID_SESSION_KEY));
		if(StringUtil.isEmpty(userId))
		{
			return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
		}

		

		ConsigneeSendperson consigneeInfo = new ConsigneeSendperson();
		consigneeInfo.setUserId(userId);
		consigneeInfo.setName(name);
		consigneeInfo.setProvince(province);
		consigneeInfo.setCity(city);
		//consigneeInfo.setDistrict(district);
		consigneeInfo.setStreetAddress(sAdd);
		consigneeInfo.setPhone(phone);
		//consigneeInfo.setTelephone(telephone);
		consigneeInfo.setZipCode(zcode);
		consigneeInfo.setEmail(email);
		consigneeInfo.setCompany(company);
		
		try {
			
			
			return this.consigneeSendpersonService.saveConsigneeSendperson(consigneeInfo);
			//return this.consigneeInfoService.(consigneeInfo);
		} catch (Exception e) {
			log.error("保存用户发件地址到数据库失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					"添加发件人失败，请重新尝试或者联系客服！");
		}
	}

	//用户端要进行后搜索
	@RequestMapping(value = "/consignee_sendperson/search", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<PageSplit<ConsigneeSendperson>> searchByName(
			HttpServletRequest request,
			@RequestParam(value = "keyword", required = false, defaultValue = "") String key,
			@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
			@RequestParam(value = "rows", required = false, defaultValue = "10") int rows) {
		try {
			String userId = StringUtil.obj2String(request.getSession()
					.getAttribute(Constant.USER_ID_SESSION_KEY));
			
			if(StringUtil.isEmpty(userId))
			{
				return new ResponseObject<PageSplit<ConsigneeSendperson>>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
			}
			
			
			
			
			pageIndex = Math.max(pageIndex, 1);
			
			
			
			 
				pageIndex = Math.max(pageIndex, 1);
				return this.consigneeSendpersonService.getByInfo(userId, key,
						rows, pageIndex);
			
			
			
		} catch (Exception e) {
			log.error("根据发件地址id获取信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					"获取信息出现异常!");
		}
	}
	
	
	//修改发件人信息
		@RequestMapping(value = "/consignee_sendperson/modify_info", method = { RequestMethod.POST,RequestMethod.GET })
		@ResponseBody
		public ResponseObject<Object> modifyConsigneeByurser(
				HttpServletRequest request,
				@RequestParam(value = "id", required = false) String id,
				@RequestParam(value = "name") String name,
				@RequestParam(value = "province") String province,
				@RequestParam(value = "city") String city,
				@RequestParam(value = "streetAddress") String sAdd,
				@RequestParam(value = "zipCode") String zcode,
				@RequestParam(value = "phone") String phone,
				@RequestParam(value = "email", required = false) String email,
				@RequestParam(value = "company", required = false, defaultValue = "") String company) {
			
			
			String userId = StringUtil.obj2String(request.getSession()
					.getAttribute(Constant.USER_ID_SESSION_KEY));
			if(StringUtil.isEmpty(userId))
			{
				return generateResponseObject(ResponseCode.NEED_LOGIN,
						"你没有登陆!");
			}
			if(StringUtil.isEmpty(id))
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错！"); 
			}
			if(StringUtil.isEmpty(name))
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"姓名不能为空！"); 
			}
		
			
			if(StringUtil.isEmpty(phone))
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"电话号不能为空！"); 
			}
			if (!ConsigneeInfoUtil.validatePhone(phone)) {
			      return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "电话号码填写错误，请检查！");
			}
			
			
		
			
			ConsigneeSendperson consigneeInfo = new ConsigneeSendperson();
			consigneeInfo.setId(id);
			consigneeInfo.setUserId(userId);
			consigneeInfo.setName(name);
			consigneeInfo.setProvince(province);
			consigneeInfo.setCity(city);
			consigneeInfo.setStreetAddress(sAdd);
			consigneeInfo.setPhone(phone);
			//consigneeInfo.setTelephone(phone);
			consigneeInfo.setZipCode(zcode);
			consigneeInfo.setCompany(company);
			consigneeInfo.setEmail(email);
			try {
				return this.consigneeSendpersonService.modifySendperson(consigneeInfo);
				//return this.consigneeInfoService.modifyConsigneeInfo(consigneeInfo);
			} catch (Exception e) {
				log.error("修改用户发件地址信息失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
						"修改发件地址信息失败，请重新尝试！");
			}
		}
		
		
		@RequestMapping(value = "/consignee_sendperson/del", method = { RequestMethod.POST })
		@ResponseBody
		public ResponseObject<Object> delConsignee(HttpServletRequest request,
				@RequestParam(value = ParameterConstants.CONSIGNEE_ID) String id) {
			if (!ConsigneeInfoUtil.validateId(id)) {
				return generateResponseObject(ResponseCode.CONSIGNEE_ID_ERROR,
						"发件地址id不符合规定");
			}
			try {
				String userId = StringUtil.obj2String(request.getSession()
						.getAttribute(Constant.USER_ID_SESSION_KEY));
				return this.consigneeSendpersonService.deleteConsigneeSendperson(id, userId);
				//return this.consigneeInfoService.deleteConsigneeInfo(id, userId);
			} catch (Exception e) {
				log.error("根据发件人id删除信息失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
						"删除失败，请重新尝试!");
			}
		}
}

package com.meitao.controller;

import java.io.File;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.meitao.service.ConsigneeInfoService;
import com.meitao.service.UserService;
import com.meitao.common.composepics.imgcompose;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ParameterConstants;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.ConsigneeInfoUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.exception.ServiceException;
import com.meitao.model.ConsigneeInfo;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.ResponseString;
import com.meitao.model.User;

@Controller
public class ConsigneeInfoController extends BasicController {

	private static final long serialVersionUID = 1455632086294967055L;
	private static final Logger log = Logger
			.getLogger(ConsigneeInfoController.class);

	@Resource(name = "consigneeInfoService")
	private ConsigneeInfoService consigneeInfoService;
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

	@RequestMapping(value = "/admin/consignee/add", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> addConsigneeByAdmin(
			HttpServletRequest request,
			@RequestParam(value = "cUserId", required = false) String userId,
			@RequestParam(value = "cName", required = false) String name,
			@RequestParam(value = "cProvince", required = false) String province,
			@RequestParam(value = "cCity", required = false) String city,
			@RequestParam(value = "cDistrict", required = false) String district,
			@RequestParam(value = "cStreetAddress", required = false) String sAdd,
			@RequestParam(value = "cZipCode", required = false) String zcode,
			@RequestParam(value = "cPhone", required = false) String phone,
			@RequestParam(value = "cPhone", required = false) String telephone,
			// @RequestParam(value = "cid", required = false, defaultValue = "")
			// String cid,
			@RequestParam(value = "cardidname", required = false, defaultValue = "") String cid,
			@RequestParam(value = ParameterConstants.CONSIGNEE_CARD_FILE, required = false) MultipartFile file,
			@RequestParam(value = ParameterConstants.CONSIGNEE_CARD_FILEOTHER, required = false) MultipartFile fileother) {
		if (!ConsigneeInfoUtil.validateConsigneeName(name)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_NAME_ERROR,
					"收货人姓名不正确，请重新输入!");
		}

		if (!ConsigneeInfoUtil.validateProvince(province)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_PROVINCE_ERROR, "省份填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validateCity(city)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_CITY_ERROR,
					"市填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validateDistrict(district)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_DISTRICT_ERROR, "区填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validateStreetAddress(sAdd)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_STREET_ADDRESS_ERROR,
					"街道地址填写不正确，请重新输入！");
		}

		if ((!StringUtil.isEmpty(zcode))&&(!ConsigneeInfoUtil.validateZipCode(zcode))) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_ZIP_CODE_ERROR, "邮编填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validatePhone(phone)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR,
					"手机号码填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validateTelephone(telephone)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_TELEPHONE_ERROR, "固定电话填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validateCardId(cid)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
					"身份证号码填写错误，请重新填写！");
		}

		// 解决火狐的反斜杠问题 kai 20151006
		String filetype = this.defaultCardFileType;// 要上传的文件类型
		String strtest = this.saveCardDir;
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
			fileName = this.saveCardDir + strseparator + userId + "_"
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

		String fileNameother = null;
		if (fileother != null && fileother.getSize() > 0) {
			if (fileother.getSize() > this.defaultCardFileSize) {
				return generateResponseObject(
						ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
			}

			String originalName = fileother.getOriginalFilename();

			if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
				return generateResponseObject(
						ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
			}

			int index = originalName.lastIndexOf('.');
			index = Math.max(index, 0);
			fileNameother = this.saveCardDir + strseparator + userId + "_"
					+ StringUtil.generateRandomString(5) + "_" + "other" + "_"
					+ StringUtil.generateRandomInteger(5)
					+ originalName.substring(index);
			//String temp = originalName.substring(index);
			try {
				File file2 = new File(request.getSession().getServletContext()
						.getRealPath("/")
						+ fileNameother);
				fileother.transferTo(file2);
			} catch (Exception e) {
				log.error("保存用户图像失败,请不要上传图像！", e);
				return generateResponseObject(
						ResponseCode.CONSIGNEE_CARD_ERROR,
						"保存用户图像失败，请去除上传图像后再尝试!");
			}
		}

		ConsigneeInfo consigneeInfo = new ConsigneeInfo();
		consigneeInfo.setUserId(userId);
		consigneeInfo.setName(name);
		consigneeInfo.setProvince(province);
		consigneeInfo.setCity(city);
		consigneeInfo.setDistrict(district);
		consigneeInfo.setStreetAddress(sAdd);
		consigneeInfo.setPhone(phone);
		consigneeInfo.setTelephone(telephone);
		consigneeInfo.setZipCode(zcode);
		consigneeInfo.setCardId(cid);
		consigneeInfo.setCardUrl(fileName);
		consigneeInfo.setCardurlother(fileNameother);
		if ((fileName != null) && (fileNameother != null)) {
			imgcompose img = new imgcompose();
			String str1 = request.getSession().getServletContext()
					.getRealPath("/")
					+ fileName;
			String str2 = request.getSession().getServletContext()
					.getRealPath("/")
					+ fileNameother;
			String str3 = this.saveCardDir + File.separator + userId + "_"
					+ StringUtil.generateRandomString(5) + "_" + "together"
					+ "_" + StringUtil.generateRandomInteger(5);
			String filecardtemp = str3;
			str3 = request.getSession().getServletContext().getRealPath("/")
					+ str3;
			if (img.createcompics(str1, str2, str3)) {
				filecardtemp = filecardtemp + ".jpg";
				consigneeInfo.setCardurltogether(filecardtemp);
			}
		}

		try {
			return this.consigneeInfoService.saveConsigneeInfo(consigneeInfo);
		} catch (Exception e) {
			log.error("保存用户收货地址到数据库失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					"添加收件人失败，请重新尝试或者联系客服！");
		}
	}

	@RequestMapping(value = "/admin/consignee/add_no_pic", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> addConsigneeWithNoPicByAdmin(
			HttpServletRequest request,
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "from_user_name") String userPhone,
			@RequestParam(value = "from_user_real_name") String userName,
			@RequestParam(value = ParameterConstants.CONSIGNEE_NAME) String name,
			@RequestParam(value = ParameterConstants.CONSIGNEE_PROVINCE) String province,
			@RequestParam(value = ParameterConstants.CONSIGNEE_CITY) String city,
			@RequestParam(value = ParameterConstants.CONSIGNEE_DISTRICT) String district,
			@RequestParam(value = ParameterConstants.CONSIGNEE_STREET_ADDRESS) String sAdd,
			@RequestParam(value = ParameterConstants.CONSIGNEE_ZIP_CODE) String zcode,
			@RequestParam(value = ParameterConstants.CONSIGNEE_PHONE) String phone,
			@RequestParam(value = ParameterConstants.CONSIGNEE_TELEPHONE, required = false) String telephone,
			@RequestParam(value = ParameterConstants.CONSIGNEE_CARD_ID, required = false, defaultValue = "") String cid) {
		if (!ConsigneeInfoUtil.validateConsigneeName(name)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_NAME_ERROR,
					"收货人姓名不正确，请重新输入!");
		}

		/*if (!ConsigneeInfoUtil.validateProvince(province)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_PROVINCE_ERROR, "省份填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validateCity(city)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_CITY_ERROR,
					"市填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validateDistrict(district)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_DISTRICT_ERROR, "区填写不正确，请重新输入！");
		}*/

		if (!ConsigneeInfoUtil.validateStreetAddress(sAdd)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_STREET_ADDRESS_ERROR,
					"街道地址填写不正确，请重新输入！");
		}

		if ((!StringUtil.isEmpty(zcode))&&(!ConsigneeInfoUtil.validateZipCode(zcode))) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_ZIP_CODE_ERROR, "邮编填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validatePhone(userPhone)) {
			return generateResponseObject(ResponseCode.USER_PHONE_ERROR,
					"用户手机号码填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validatePhone(phone)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR,
					"手机号码填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validateTelephone(telephone)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_TELEPHONE_ERROR, "固定电话填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validateCardId(cid)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
					"2身份证号码填写错误，请重新填写！");
		}

		String fileName = null;

		ConsigneeInfo consigneeInfo = new ConsigneeInfo();
		User user = null;
		try {
			//user=this.userService.getUserById(id)
			if(!StringUtil.isEmpty(id))
			{
				 ResponseObject<User> obj = this.userService.getUserById(id);
				 if((obj!=null)&&(ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj.getCode()))&&(obj.getData())!=null)
				 {
					 user=obj.getData();
				 }
				 else
				 {
					 user = this.userService.getUserByPhone(userPhone).getData();
				 }
			}else
			{
				user = this.userService.getUserByPhone(userPhone).getData();
			}
		} catch (ServiceException e) {
			log.error("获取用户信息异常，请重试！");
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					"获取用户信息异常，请重试！");
		}
		if (user != null) {
			consigneeInfo.setUserId(user.getId());
		} else {
			User u = new User();
			
			u.setPhone(StringUtil.isEmpty(userPhone) ? null : userPhone);
			u.setRealName(userName);
			u.setNickName(userPhone);
			u.setType(Constant.USER_TYPE_NORMAL);
			String password = "";
			for (int i = 0; i < 8; i++) {
				password += (int) (Math.random() * 10);
			}
			u.setPassword(password);
			ResponseObject<Object> obj = null;
			try {
				obj = this.userService.addUser(u);
			} catch (ServiceException e) {
				log.error("增加新用户信息异常，请重试！");
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
						"增加新用户信息异常，请重试！");
			}

			if (obj.getCode() == ResponseCode.SUCCESS_CODE) {
				Map<String, String> map = (Map<String, String>) obj.getData();
				obj.setData(null);
				obj=null;
				String userId = map.get("id");
				consigneeInfo.setUserId(userId);
			} else {
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
						"增加用户信息异常！");
			}
		}

		consigneeInfo.setName(name);
		consigneeInfo.setProvince(province);
		consigneeInfo.setCity(city);
		consigneeInfo.setDistrict(district);
		consigneeInfo.setStreetAddress(sAdd);
		consigneeInfo.setPhone(phone);
		consigneeInfo.setTelephone(telephone);
		consigneeInfo.setZipCode(zcode);
		consigneeInfo.setCardId(cid);
		consigneeInfo.setCardUrl(fileName);

		try {
			return this.consigneeInfoService.saveConsigneeInfo(consigneeInfo);
		} catch (Exception e) {
			log.error("保存用户收货地址到数据库失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					"添加收件人失败，请重新尝试或者联系客服！");
		}
	}

	@RequestMapping(value = "/consignee/add", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> addConsignee(
			HttpServletRequest request,
			@RequestParam(value = ParameterConstants.CONSIGNEE_NAME) String name,
			@RequestParam(value = ParameterConstants.CONSIGNEE_PROVINCE) String province,
			@RequestParam(value = ParameterConstants.CONSIGNEE_CITY) String city,
			@RequestParam(value = ParameterConstants.CONSIGNEE_DISTRICT) String district,
			@RequestParam(value = ParameterConstants.CONSIGNEE_STREET_ADDRESS) String sAdd,
			@RequestParam(value = ParameterConstants.CONSIGNEE_ZIP_CODE) String zcode,
			@RequestParam(value = ParameterConstants.CONSIGNEE_PHONE) String phone,
			@RequestParam(value = ParameterConstants.CONSIGNEE_TELEPHONE, required = false) String telephone,
			@RequestParam(value = ParameterConstants.CONSIGNEE_CARD_ID, required = false, defaultValue = "") String cid,
			@RequestParam(value = ParameterConstants.CONSIGNEE_CARD_FILE, required = false) MultipartFile file,
			@RequestParam(value = ParameterConstants.CONSIGNEE_CARD_FILEOTHER, required = false) MultipartFile fileother) {
		if (!ConsigneeInfoUtil.validateConsigneeName(name)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_NAME_ERROR,
					"收货人姓名不正确，请重新输入!");
		}

		/*if (!ConsigneeInfoUtil.validateProvince(province)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_PROVINCE_ERROR, "省份填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validateCity(city)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_CITY_ERROR,
					"市填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validateDistrict(district)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_DISTRICT_ERROR, "区填写不正确，请重新输入！");
		}*/

		if (!ConsigneeInfoUtil.validateStreetAddress(sAdd)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_STREET_ADDRESS_ERROR,
					"街道地址填写不正确，请重新输入！");
		}

		if ((!StringUtil.isEmpty(zcode))&&!ConsigneeInfoUtil.validateZipCode(zcode)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_ZIP_CODE_ERROR, "邮编填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validatePhone(phone)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR,
					"手机号码填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validateTelephone(telephone)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_TELEPHONE_ERROR, "固定电话填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validateCardId(cid)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
					"3身份证号码填写错误，请重新填写！");
		}

		String userId = StringUtil.obj2String(request.getSession()
				.getAttribute(Constant.USER_ID_SESSION_KEY));

		// 解决火狐的反斜杠问题 kai 20151006
		String filetype = this.defaultCardFileType;// 要上传的文件类型
		String strtest = this.saveCardDir;
		String strseparator;
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

			fileName = this.saveCardDir + strseparator + userId + "_"
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

		String fileNameother = null;
		if (fileother != null && fileother.getSize() > 0) {
			if (fileother.getSize() > this.defaultCardFileSize) {
				return generateResponseObject(
						ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
			}

			String originalName = fileother.getOriginalFilename();

			if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
				return generateResponseObject(
						ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
			}

			int index = originalName.lastIndexOf('.');

			index = Math.max(index, 0);
			fileNameother = this.saveCardDir + strseparator + userId + "_"
					+ StringUtil.generateRandomString(5) + "_" + "other" + "_"
					+ StringUtil.generateRandomInteger(5)
					+ originalName.substring(index);
			//String temp = originalName.substring(index);
			try {
				File file2 = new File(request.getSession().getServletContext()
						.getRealPath("/")
						+ fileNameother);
				fileother.transferTo(file2);
			} catch (Exception e) {
				log.error("保存用户图像失败,请不要上传图像！", e);
				return generateResponseObject(
						ResponseCode.CONSIGNEE_CARD_ERROR,
						"保存用户图像失败，请去除上传图像后再尝试!");
			}
		}

		ConsigneeInfo consigneeInfo = new ConsigneeInfo();
		consigneeInfo.setUserId(userId);
		consigneeInfo.setName(name);
		consigneeInfo.setProvince(province);
		consigneeInfo.setCity(city);
		consigneeInfo.setDistrict(district);
		consigneeInfo.setStreetAddress(sAdd);
		consigneeInfo.setPhone(phone);
		consigneeInfo.setTelephone(telephone);
		consigneeInfo.setZipCode(zcode);
		consigneeInfo.setCardId(cid);
		consigneeInfo.setCardUrl(fileName);
		consigneeInfo.setCardurlother(fileNameother);
		if ((fileName != null) && (fileNameother != null)) {
			imgcompose img = new imgcompose();
			String str1 = request.getSession().getServletContext()
					.getRealPath("/")
					+ fileName;
			String str2 = request.getSession().getServletContext()
					.getRealPath("/")
					+ fileNameother;
			String str3 = this.saveCardDir + strseparator + userId + "_"
					+ StringUtil.generateRandomString(5) + "_" + "together"
					+ "_" + StringUtil.generateRandomInteger(5);
			String filecardtemp = str3;
			str3 = request.getSession().getServletContext().getRealPath("/")
					+ str3;
			if (img.createcompics(str1, str2, str3)) {
				filecardtemp = filecardtemp + ".jpg";
				consigneeInfo.setCardurltogether(filecardtemp);
			}
		}
		try {
			return this.consigneeInfoService.saveConsigneeInfo(consigneeInfo);
		} catch (Exception e) {
			log.error("保存用户收货地址到数据库失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					"添加收件人失败，请重新尝试或者联系客服！");
		}
	}

	@RequestMapping(value = "/consignee/add_no_pic", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> addConsigneeWithNoPic(
			HttpServletRequest request,
			@RequestParam(value = ParameterConstants.CONSIGNEE_NAME) String name,
			@RequestParam(value = ParameterConstants.CONSIGNEE_PROVINCE) String province,
			@RequestParam(value = ParameterConstants.CONSIGNEE_CITY) String city,
			@RequestParam(value = ParameterConstants.CONSIGNEE_DISTRICT) String district,
			@RequestParam(value = ParameterConstants.CONSIGNEE_STREET_ADDRESS) String sAdd,
			@RequestParam(value = ParameterConstants.CONSIGNEE_ZIP_CODE) String zcode,
			@RequestParam(value = ParameterConstants.CONSIGNEE_PHONE) String phone,
			@RequestParam(value = ParameterConstants.CONSIGNEE_TELEPHONE, required = false) String telephone,
			@RequestParam(value = ParameterConstants.CONSIGNEE_CARD_ID, required = false, defaultValue = "") String cid) {
		if (!ConsigneeInfoUtil.validateConsigneeName(name)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_NAME_ERROR,
					"收货人姓名不正确，请重新输入!");
		}

		/*if (!ConsigneeInfoUtil.validateProvince(province)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_PROVINCE_ERROR, "省份填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validateCity(city)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_CITY_ERROR,
					"市填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validateDistrict(district)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_DISTRICT_ERROR, "区填写不正确，请重新输入！");
		}*/

		if (!ConsigneeInfoUtil.validateStreetAddress(sAdd)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_STREET_ADDRESS_ERROR,
					"街道地址填写不正确，请重新输入！");
		}

		if ((!StringUtil.isEmpty(zcode))&&!ConsigneeInfoUtil.validateZipCode(zcode)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_ZIP_CODE_ERROR, "邮编填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validatePhone(phone)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR,
					"手机号码填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validateTelephone(telephone)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_TELEPHONE_ERROR, "固定电话填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validateCardId(cid)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
					"4身份证号码填写错误，请重新填写！");
		}

		String userId = StringUtil.obj2String(request.getSession()
				.getAttribute(Constant.USER_ID_SESSION_KEY));
		String fileName = null;

		ConsigneeInfo consigneeInfo = new ConsigneeInfo();
		consigneeInfo.setUserId(userId);
		consigneeInfo.setName(name);
		consigneeInfo.setProvince(province);
		consigneeInfo.setCity(city);
		consigneeInfo.setDistrict(district);
		consigneeInfo.setStreetAddress(sAdd);
		consigneeInfo.setPhone(phone);
		consigneeInfo.setTelephone(telephone);
		consigneeInfo.setZipCode(zcode);
		consigneeInfo.setCardId(cid);
		consigneeInfo.setCardUrl(fileName);

		try {
			return this.consigneeInfoService.saveConsigneeInfo(consigneeInfo);
		} catch (Exception e) {
			log.error("保存用户收货地址到数据库失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					"添加收件人失败，请重新尝试或者联系客服！");
		}
	}

	@RequestMapping(value = "/consignee/del", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> delConsignee(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.CONSIGNEE_ID) String id) {
		if (!ConsigneeInfoUtil.validateId(id)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_ID_ERROR,
					"收货地址id不符合规定");
		}
		try {
			String userId = StringUtil.obj2String(request.getSession()
					.getAttribute(Constant.USER_ID_SESSION_KEY));
			return this.consigneeInfoService.deleteConsigneeInfo(id, userId);
		} catch (Exception e) {
			log.error("根据收货人id删除信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					"删除失败，请重新尝试!");
		}
	}

	// kai 20150921 添加身份反面图片处理
	@RequestMapping(value = "/consignee/modify", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> modifyConsignee(
			HttpServletRequest request,
			@RequestParam(value = ParameterConstants.CONSIGNEE_ID) String id,
			@RequestParam(value = ParameterConstants.CONSIGNEE_NAME) String name,
			@RequestParam(value = ParameterConstants.CONSIGNEE_PROVINCE) String province,
			@RequestParam(value = ParameterConstants.CONSIGNEE_CITY) String city,
			@RequestParam(value = ParameterConstants.CONSIGNEE_DISTRICT) String district,
			@RequestParam(value = ParameterConstants.CONSIGNEE_STREET_ADDRESS) String sAdd,
			@RequestParam(value = ParameterConstants.CONSIGNEE_ZIP_CODE) String zcode,
			@RequestParam(value = ParameterConstants.CONSIGNEE_PHONE) String phone,
			@RequestParam(value = ParameterConstants.CONSIGNEE_TELEPHONE, required = false) String telephone,
			@RequestParam(value = ParameterConstants.CONSIGNEE_CARD_ID, required = false, defaultValue = "") String cid,
			@RequestParam(value = "old_file", required = false, defaultValue = "") String oldFilePath,
			@RequestParam(value = "old_fileother", required = false, defaultValue = "") String oldFilePathother,
			@RequestParam(value = ParameterConstants.CONSIGNEE_CARD_FILE, required = false) MultipartFile file,
			@RequestParam(value = ParameterConstants.CONSIGNEE_CARD_FILEOTHER, required = false) MultipartFile fileother) {
		if (!ConsigneeInfoUtil.validateId(id)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_ID_ERROR,
					"收件人信息id不正确，请重新尝试！");
		}
		if (!ConsigneeInfoUtil.validateConsigneeName(name)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_NAME_ERROR,
					"收货人姓名不正确，请重新输入!");
		}

		/*if (!ConsigneeInfoUtil.validateProvince(province)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_PROVINCE_ERROR, "省份填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validateCity(city)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_CITY_ERROR,
					"市填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validateDistrict(district)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_DISTRICT_ERROR, "区填写不正确，请重新输入！");
		}
*/
		if (!ConsigneeInfoUtil.validateStreetAddress(sAdd)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_STREET_ADDRESS_ERROR,
					"街道地址填写不正确，请重新输入！");
		}

		if ((!StringUtil.isEmpty(zcode))&&!ConsigneeInfoUtil.validateZipCode(zcode)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_ZIP_CODE_ERROR, "邮编填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validatePhone(phone)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR,
					"手机号码填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validateTelephone(telephone)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_TELEPHONE_ERROR, "固定电话填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validateCardId(cid)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
					"5身份证号码填写错误，请重新填写！");
		}

		// 解决火狐的反斜杠问题 kai 20151006
		String filetype = this.defaultCardFileType;// 要上传的文件类型
		String strtest = this.saveCardDir;
		String strseparator = "";
		if (strtest.indexOf("/") >= 0)// 包含有“/”字符，分隔符用此字符
		{
			strseparator = "/";
		} else {
			strseparator = File.separator;
		}

		String userId = StringUtil.obj2String(request.getSession()
				.getAttribute(Constant.USER_ID_SESSION_KEY));
		String fileName = null;
		boolean togetherflag = false;
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
			fileName = this.saveCardDir + strseparator + userId + "_"
					+ StringUtil.generateRandomString(5) + "_"
					+ StringUtil.generateRandomInteger(5)
					+ originalName.substring(index);
			try {
				File file1 = new File(request.getSession().getServletContext()
						.getRealPath("/")
						+ fileName);
				file.transferTo(file1);
				togetherflag = true;
				// 删除原来的图片
				File oldFile = new File(request.getSession()
						.getServletContext().getRealPath(oldFilePath));
				if (oldFile.exists()) {
					try {
						oldFile.delete();
					} catch (Exception e) {
						// ignore
					}
				}
			} catch (Exception e) {
				log.error("保存用户图像失败,请不要上传图像！");
				return generateResponseObject(
						ResponseCode.CONSIGNEE_CARD_ERROR,
						"保存用户图像失败，请去除上传图像后再尝试!");
			}
		} else {

			if ((oldFilePath == null) || (oldFilePath.equalsIgnoreCase(""))) {
				fileName = null;
			} else {
				// fileName=oldFilePath;
				File file3 = new File(request.getSession().getServletContext()
						.getRealPath("/")
						+ oldFilePath);
				if (file3.exists()) {
					fileName = oldFilePath;
				} else {
					fileName = null;
				}
			}
		}

		String fileNameother = null;
		if (fileother != null && fileother.getSize() > 0) {
			if (fileother.getSize() > this.defaultCardFileSize) {
				return generateResponseObject(
						ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
			}

			String originalName = fileother.getOriginalFilename();

			if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
				return generateResponseObject(
						ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
			}

			int index = originalName.lastIndexOf('.');
			index = Math.max(index, 0);
			fileNameother = this.saveCardDir + strseparator + userId + "_"
					+ StringUtil.generateRandomString(5) + "_" + "other" + "_"
					+ StringUtil.generateRandomInteger(5)
					+ originalName.substring(index);
			try {
				File file2 = new File(request.getSession().getServletContext()
						.getRealPath("/")
						+ fileNameother);
				fileother.transferTo(file2);
				togetherflag = true;
				// 删除原来的图片
				File oldFile = new File(request.getSession()
						.getServletContext().getRealPath(oldFilePathother));
				if (oldFile.exists()) {
					try {
						oldFile.delete();
					} catch (Exception e) {
						// ignore
					}
				}
			} catch (Exception e) {
				log.error("保存用户图像失败,请不要上传图像！");
				return generateResponseObject(
						ResponseCode.CONSIGNEE_CARD_ERROR,
						"保存用户图像失败，请去除上传图像后再尝试!");
			}
		} else {

			if ((oldFilePathother == null)
					|| (oldFilePathother.equalsIgnoreCase(""))) {
				fileNameother = null;
			} else {
				// fileNameother=oldFilePathother;
				File file3 = new File(request.getSession().getServletContext()
						.getRealPath("/")
						+ oldFilePathother);
				if (file3.exists()) {
					fileNameother = oldFilePathother;
				} else {
					fileNameother = null;
				}
			}
		}
		String cardtogether = null;
		if (togetherflag == true) {
			if ((fileName != null) && (fileNameother != null)) {
				imgcompose img = new imgcompose();
				String str1 = request.getSession().getServletContext()
						.getRealPath("/")
						+ fileName;
				String str2 = request.getSession().getServletContext()
						.getRealPath("/")
						+ fileNameother;
				String str3 = this.saveCardDir + File.separator + userId + "_"
						+ StringUtil.generateRandomString(5) + "_" + "together"
						+ "_" + StringUtil.generateRandomInteger(5);
				String filecardtemp = str3;
				str3 = request.getSession().getServletContext()
						.getRealPath("/")
						+ str3;
				if (img.createcompics(str1, str2, str3)) {
					filecardtemp = filecardtemp + ".jpg";
					cardtogether = filecardtemp;

				}
			}
		}

		ConsigneeInfo consigneeInfo = new ConsigneeInfo();
		consigneeInfo.setId(id);
		consigneeInfo.setUserId(userId);
		consigneeInfo.setName(name);
		consigneeInfo.setProvince(province);
		consigneeInfo.setCity(city);
		consigneeInfo.setDistrict(district);
		consigneeInfo.setStreetAddress(sAdd);
		consigneeInfo.setPhone(phone);
		consigneeInfo.setTelephone(telephone);
		consigneeInfo.setZipCode(zcode);
		consigneeInfo.setCardId(cid);
		consigneeInfo.setCardUrl(fileName);
		consigneeInfo.setCardurlother(fileNameother);
		consigneeInfo.setCardurltogether(cardtogether);

		try {
			return this.consigneeInfoService.modifyConsigneeInfo(consigneeInfo);
		} catch (Exception e) {
			log.error("修改用户收货地址信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					"修改收货地址信息失败，请重新尝试！");
		}
	}

	@RequestMapping(value = "/consignee/modify_no_pic", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> modifyConsigneeNoPic(
			HttpServletRequest request,
			@RequestParam(value = ParameterConstants.CONSIGNEE_ID) String id,
			@RequestParam(value = ParameterConstants.CONSIGNEE_NAME) String name,
			@RequestParam(value = ParameterConstants.CONSIGNEE_PROVINCE) String province,
			@RequestParam(value = ParameterConstants.CONSIGNEE_CITY) String city,
			@RequestParam(value = ParameterConstants.CONSIGNEE_DISTRICT) String district,
			@RequestParam(value = ParameterConstants.CONSIGNEE_STREET_ADDRESS) String sAdd,
			@RequestParam(value = ParameterConstants.CONSIGNEE_ZIP_CODE) String zcode,
			@RequestParam(value = ParameterConstants.CONSIGNEE_PHONE) String phone,
			@RequestParam(value = ParameterConstants.CONSIGNEE_TELEPHONE, required = false) String telephone,
			@RequestParam(value = ParameterConstants.CONSIGNEE_CARD_ID, required = false, defaultValue = "") String cid) {
		if (!ConsigneeInfoUtil.validateId(id)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_ID_ERROR,
					"收件人信息id不正确，请重新尝试！");
		}
		if (!ConsigneeInfoUtil.validateConsigneeName(name)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_NAME_ERROR,
					"收货人姓名不正确，请重新输入!");
		}

		/*if (!ConsigneeInfoUtil.validateProvince(province)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_PROVINCE_ERROR, "省份填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validateCity(city)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_CITY_ERROR,
					"市填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validateDistrict(district)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_DISTRICT_ERROR, "区填写不正确，请重新输入！");
		}*/

		if (!ConsigneeInfoUtil.validateStreetAddress(sAdd)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_STREET_ADDRESS_ERROR,
					"街道地址填写不正确，请重新输入！");
		}

		if ((!StringUtil.isEmpty(zcode))&&!ConsigneeInfoUtil.validateZipCode(zcode)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_ZIP_CODE_ERROR, "邮编填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validatePhone(phone)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR,
					"手机号码填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validateTelephone(telephone)) {
			return generateResponseObject(
					ResponseCode.CONSIGNEE_TELEPHONE_ERROR, "固定电话填写不正确，请重新输入！");
		}

		if (!ConsigneeInfoUtil.validateCardId(cid)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
					"6身份证号码填写错误，请重新填写！");
		}

		String userId = StringUtil.obj2String(request.getSession()
				.getAttribute(Constant.USER_ID_SESSION_KEY));
		String fileName = null;

		ConsigneeInfo consigneeInfo = new ConsigneeInfo();
		consigneeInfo.setId(id);
		consigneeInfo.setUserId(userId);
		consigneeInfo.setName(name);
		consigneeInfo.setProvince(province);
		consigneeInfo.setCity(city);
		consigneeInfo.setDistrict(district);
		consigneeInfo.setStreetAddress(sAdd);
		consigneeInfo.setPhone(phone);
		consigneeInfo.setTelephone(telephone);
		consigneeInfo.setZipCode(zcode);
		consigneeInfo.setCardId(cid);
		consigneeInfo.setCardUrl(fileName);
		// consigneeInfo.setCardurlother(fileName);

		try {
			return this.consigneeInfoService.modifyConsigneeInfo(consigneeInfo);
		} catch (Exception e) {
			log.error("修改用户收货地址信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					"修改收货地址信息失败，请重新尝试！");
		}
	}

	@RequestMapping(value = "/consignee/get_one", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<ConsigneeInfo> getById(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.CONSIGNEE_ID) String id) {
		if (!ConsigneeInfoUtil.validateId(id)) {
			return generateResponseObject(ResponseCode.CONSIGNEE_ID_ERROR,
					"收件人信息id不正确，请重新尝试！");
		}
		try {
			String userId = StringUtil.obj2String(request.getSession()
					.getAttribute(Constant.USER_ID_SESSION_KEY));
			return this.consigneeInfoService.getById(id, userId);
		} catch (Exception e) {
			log.error("根据收货地址id获取信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					"获取信息出现异常!");
		}
	}

	@RequestMapping(value = "/consignee/search", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<PageSplit<ConsigneeInfo>> searchByName(
			HttpServletRequest request,
			@RequestParam(value = "keyword", required = false, defaultValue = "") String key,
			@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
			@RequestParam(value = "rows", required = false, defaultValue = "10") int rows) {
		try {
			String userId = StringUtil.obj2String(request.getSession()
					.getAttribute(Constant.USER_ID_SESSION_KEY));
			pageIndex = Math.max(pageIndex, 1);
			
			
			
			 
				pageIndex = Math.max(pageIndex, 1);
				return this.consigneeInfoService.getByInfo(userId, key,
						rows, pageIndex);
			
			
			
		} catch (Exception e) {
			log.error("根据收货地址id获取信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					"获取信息出现异常!");
		}
	}

	@RequestMapping(value = "/consignee/all", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<List<ConsigneeInfo>> getAllConsignee(
			HttpServletRequest request) {
		try {
			String userId = StringUtil.obj2String(request.getSession()
					.getAttribute(Constant.USER_ID_SESSION_KEY));
			ResponseObject<PageSplit<ConsigneeInfo>> tmpResult = this.consigneeInfoService
					.getByName(userId, null, Integer.MAX_VALUE, 1);
			if (ResponseCode.SUCCESS_CODE.equals(tmpResult.getCode())
					&& tmpResult.getData() != null) {
				List<ConsigneeInfo> infos = tmpResult.getData().getDatas();
				ResponseObject<List<ConsigneeInfo>> result = new ResponseObject<List<ConsigneeInfo>>(
						ResponseCode.SUCCESS_CODE);
				result.setData(infos);
				return result;
			} else {
				return generateResponseObject(tmpResult.getCode(),
						tmpResult.getMessage());
			}
		} catch (Exception e) {
			log.error("获取用户收货地址信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					"获取用户收货地址信息出现异常!");
		}
	}

	// 门市件中的收件人
	@RequestMapping(value = "/admin/consignee/search_by_emp", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<PageSplit<ConsigneeInfo>> searchByNameOfEmp(
			HttpServletRequest request,
			@RequestParam(value = ParameterConstants.COMMON_SEARCH_KEY, required = false, defaultValue = "") String key,
			@RequestParam(value = ParameterConstants.USER_ID, required = false, defaultValue = "") String userId,
			@RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex) {
		String storeId = StringUtil.obj2String(request.getSession()
				.getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
		if (StringUtil.isEmpty(storeId)) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR,
					"您不是门店管理员，不能添加查看该用户的收件人地址");
		}

		try {
			// String userId =
			// StringUtil.obj2String(request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY));

			pageIndex = Math.max(pageIndex, 1);
			return this.consigneeInfoService.getByName(userId, key,
					defaultPageSize, pageIndex);
		} catch (Exception e) {
			log.error("根据收货地址id获取信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					"获取信息出现异常!");
		}
	}
	
	@RequestMapping(value = "/consignee/searchbyinfo", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<PageSplit<ConsigneeInfo>> searchByinfo(
			HttpServletRequest request,
			@RequestParam(value = ParameterConstants.COMMON_SEARCH_KEY, required = false, defaultValue = "") String key,
			@RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex) {
		try {
			
			 key = URLDecoder.decode(key, "UTF-8");
			String userId = StringUtil.obj2String(request.getSession()
					.getAttribute(Constant.USER_ID_SESSION_KEY));
			pageIndex = Math.max(pageIndex, 1);
			return this.consigneeInfoService.getByInfo(userId, key,
					defaultPageSize, pageIndex);
		} catch (Exception e) {
			log.error("根据收货地址id获取信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					"获取信息出现异常!");
		}
	}
	
	
	//用户收件人地址簿中添加,为了与ie兼空，返回字符串
	
	@RequestMapping(value = "/user/rev_user/add",produces="text/plain;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String addConsigneeByurser(
			HttpServletRequest request,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "province", required = false) String province,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "district", required = false) String district,
			@RequestParam(value = "streetAddress", required = false) String streetAddress,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "zipCode", required = false) String zipCode,
			@RequestParam(value = "cardId", required = false) String cardId,

			@RequestParam(value = "cardUrl", required = false) MultipartFile cardUrl,
			@RequestParam(value = "cardurlother", required = false) MultipartFile fileother,
			@RequestParam(value = "cardurltogether", required = false) MultipartFile cardurltogether
			
			) {
		
		ResponseString responseString=new ResponseString();
		String userId = StringUtil.obj2String(request.getSession()
				.getAttribute(Constant.USER_ID_SESSION_KEY));
		if(StringUtil.isEmpty(userId))
		{
			responseString.setCode(ResponseCode.NEED_LOGIN);
			responseString.setMessage("你没有登陆!");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.NEED_LOGIN,
			//		"你没有登陆!");
		}
		
		if(StringUtil.isEmpty(name))
		{
			responseString.setCode(ResponseCode.PARAMETER_ERROR);
			responseString.setMessage("姓名不能为空！");
			return responseString.toString();
			//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"姓名不能为空！"); 
		}
		if(StringUtil.isEmpty(streetAddress))
		{
			responseString.setCode(ResponseCode.PARAMETER_ERROR);
			responseString.setMessage("详细地址不能为空！");
			return responseString.toString();
			//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"详细地址不能为空！"); 
		}
		
		if(StringUtil.isEmpty(phone))
		{
			responseString.setCode(ResponseCode.PARAMETER_ERROR);
			responseString.setMessage("电话号不能为空！");
			return responseString.toString();
			//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"电话号不能为空！"); 
		}
		if (!ConsigneeInfoUtil.validatePhone(phone)) {
			responseString.setCode(ResponseCode.CONSIGNEE_PHONE_ERROR);
			responseString.setMessage("电话号码填写错误，请检查！");
			return responseString.toString();
		     // return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "电话号码填写错误，请检查！");
		}
		if ((!StringUtil.isEmpty(cardId))&&(!ConsigneeInfoUtil.validateCardId(cardId))) {
			responseString.setCode(ResponseCode.CONSIGNEE_CARD_ID_ERROR);
			responseString.setMessage("身份证号码填写错误，请重新填写！");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
			//		"身份证号码填写错误，请重新填写！");
		}
		
		//正反面图必须同时上传
		if(cardUrl != null && cardUrl.getSize() > 0)
		{
			if(fileother == null || fileother.getSize() == 0)
			{
				responseString.setCode(ResponseCode.PARAMETER_ERROR);
				responseString.setMessage("正反面必须同时上传！");
				return responseString.toString();
				//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"正反面必须同时上传！"); 
			}
		}
		if(fileother != null && fileother.getSize() > 0)
		{
			if(cardUrl == null || cardUrl.getSize() == 0)
			{
				responseString.setCode(ResponseCode.PARAMETER_ERROR);
				responseString.setMessage("正反面必须同时上传！");
				return responseString.toString();
				//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"正反面必须同时上传！"); 
			}
		}
		
		
		// 处理提交上来的图片
		// 解决火狐的反斜杠问题 kai 20151006
		
		
			
		String filetype = this.defaultCardFileType;// 要上传的文件类型
		String strtest = this.save258CardDir;
		String strseparator = "";
		if (strtest.indexOf("/") >= 0)// 包含有“/”字符，分隔符用此字符
		{
			strseparator = "/";
		} else {
			strseparator = File.separator;
		}
		//开始处理图片
		// 获取当时的时间缀
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyyMMddHHmmss");
		String timestr = sdf.format(date);
		//如果上传的图片中包含合成图片，那么即会忽略其它的正反两面图片
		String togetherfile="";//保存合成图片
		String fontfile="";//保存正面图片路径
		String backfile="";//保存背面图片路径
		
		if(cardurltogether != null && cardurltogether.getSize() > 0)//包含合成图，要清空正反面图
		{
			fileother=null;
			cardUrl=null;
			if (cardurltogether.getSize() > this.defaultCardFileSize) {
				
				responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
				responseString.setMessage("图像文件过大,请重新尝试!");
				return responseString.toString();
				//return generateResponseObject(
				//		ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
			}

			String originalName = cardurltogether.getOriginalFilename();

			if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
				responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
				responseString.setMessage("上传图像文件格式不对,请重新尝试!");
				return responseString.toString();
				//return generateResponseObject(
				//		ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
			}
			int index = originalName.lastIndexOf('.');
			index = Math.max(index, 0);
			
			togetherfile = this.save258CardDir + strseparator + timestr + "_"+userId+ "_"+"hc"+"_"
					+ StringUtil.generateRandomString(5) + "_"
					+ StringUtil.generateRandomInteger(5)
					+ originalName.substring(index);
			try {
				File file1 = new File(request.getSession().getServletContext()
						.getRealPath("/")
						+ togetherfile);
				cardurltogether.transferTo(file1);
			} catch (Exception e) {
				log.error("保存用户合成图片失败,请不要上传图像！", e);
				
				responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
				responseString.setMessage("保存用户合成图失败，请去除上传图像后再尝试!");
				return responseString.toString();
				//return generateResponseObject(
				//		ResponseCode.CONSIGNEE_CARD_ERROR,
				//		"保存用户合成图失败，请去除上传图像后再尝试!");
			}
			
		}
		//正面图的处理方式
		if(cardUrl != null && cardUrl.getSize() > 0)//包含合成图，要清空正反面图
		{

			if (cardUrl.getSize() > this.defaultCardFileSize) {
				
				responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
				responseString.setMessage("图像文件过大,请重新尝试!");
				return responseString.toString();
				//return generateResponseObject(
				//		ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
			}

			String originalName = cardUrl.getOriginalFilename();

			if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
				
				responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
				responseString.setMessage("上传图像文件格式不对,请重新尝试!");
				return responseString.toString();
				//return generateResponseObject(
					//	ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
			}
			int index = originalName.lastIndexOf('.');
			index = Math.max(index, 0);
			
			fontfile = this.save258CardDir + strseparator + timestr + "_"+userId+ "_"+"zm"+"_"
					+ StringUtil.generateRandomString(5) + "_"
					+ StringUtil.generateRandomInteger(5)
					+ originalName.substring(index);
			try {
				File file1 = new File(request.getSession().getServletContext()
						.getRealPath("/")
						+ fontfile);
				cardUrl.transferTo(file1);
			} catch (Exception e) {
				log.error("保存用户正面图片失败,请不要上传图像！", e);
				responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
				responseString.setMessage("保存用户正面图失败，请去除上传图像后再尝试!");
				return responseString.toString();
				//return generateResponseObject(
				//		ResponseCode.CONSIGNEE_CARD_ERROR,
					//	"保存用户正面图失败，请去除上传图像后再尝试!");
			}
			
		}
		
		
		//反面图的处理方式 
		if(fileother != null && fileother.getSize() > 0)//包含合成图，要清空正反面图
		{

			if (fileother.getSize() > this.defaultCardFileSize) {
				
				responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
				responseString.setMessage("图像文件过大,请重新尝试!");
				return responseString.toString();
				//return generateResponseObject(
				//		ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
			}

			String originalName = fileother.getOriginalFilename();

			if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
				
				responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
				responseString.setMessage("上传图像文件格式不对,请重新尝试!");
				return responseString.toString();
				
				//return generateResponseObject(
				//		ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
			}
			int index = originalName.lastIndexOf('.');
			index = Math.max(index, 0);
			
			backfile = this.save258CardDir + strseparator + timestr + "_"+userId+ "_"+"fm"+"_"
					+ StringUtil.generateRandomString(5) + "_"
					+ StringUtil.generateRandomInteger(5)
					+ originalName.substring(index);
			try {
				File file1 = new File(request.getSession().getServletContext()
						.getRealPath("/")
						+ backfile);
				fileother.transferTo(file1);
			} catch (Exception e) {
				log.error("保存用户正面图片失败,请不要上传图像！", e);
				responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
				responseString.setMessage("保存用户正面图失败，请去除上传图像后再尝试!");
				return responseString.toString();
				//return generateResponseObject(
				//		ResponseCode.CONSIGNEE_CARD_ERROR,
				//		"保存用户正面图失败，请去除上传图像后再尝试!");
			}
			
		}
	
		//合成处理
		if(!StringUtil.isEmpty(fontfile)&&(!StringUtil.isEmpty(backfile)))
		{

			imgcompose img = new imgcompose();
			String str1 = request.getSession().getServletContext()
					.getRealPath("/")
					+ fontfile;
			String str2 = request.getSession().getServletContext()
					.getRealPath("/")
					+ backfile;
			
			
			File file4 = new File(str1);
			File file5 = new File(str2);
			if((file4.exists())&&(file5.exists()))
			{
				String str3 = this.save258CardDir + strseparator + timestr + "_"+userId+ "_"+"hc2"+"_"
						+ StringUtil.generateRandomString(5) + "_"
						+ StringUtil.generateRandomInteger(5);
				togetherfile = str3;
				str3 = request.getSession().getServletContext().getRealPath("/")
						+ str3;
				if (img.createcompics(str1, str2, str3)) {
					togetherfile = togetherfile + ".jpg";
				}
			}
			else
			{
				//return generateResponseObject(
				//		ResponseCode.CONSIGNEE_CARD_ERROR,
				//		"生成合成图发生异常!");
				responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
				responseString.setMessage("生成合成图发生异常!");
				return responseString.toString();
			}
			
		}
		
		
		ConsigneeInfo consigneeInfo = new ConsigneeInfo();
		consigneeInfo.setUserId(userId);
		consigneeInfo.setName(name);
		consigneeInfo.setProvince(province);
		consigneeInfo.setCity(city);
		consigneeInfo.setDistrict(district);
		consigneeInfo.setStreetAddress(streetAddress);
		consigneeInfo.setPhone(phone);
		//consigneeInfo.setTelephone(phone);
		consigneeInfo.setZipCode(zipCode);
		consigneeInfo.setCardId(cardId);
		consigneeInfo.setCardUrl(fontfile);
		consigneeInfo.setCardurlother(backfile);
		consigneeInfo.setCardurltogether(togetherfile);
		
		try {
			 ResponseObject<Object> obj= this.consigneeInfoService.saveConsigneeInfo(consigneeInfo);
			responseString.setCode(obj.getCode());
			responseString.setMessage(obj.getMessage());
			return responseString.toString();
		} catch (Exception e) {
			log.error("保存用户收货地址到数据库失败", e);
			responseString.setCode(ResponseCode.SHOW_EXCEPTION);
			responseString.setMessage("添加收件人失败，请重新尝试或者联系客服！");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
			//		"添加收件人失败，请重新尝试或者联系客服！");
		}
	}
	
	
	
	//用户收件人地址簿中添加
	@RequestMapping(value = "/user/rev_user/modify",produces="text/plain;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String modifyConsigneeByurser(
			HttpServletRequest request,
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "province", required = false) String province,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "district", required = false) String district,
			@RequestParam(value = "streetAddress", required = false) String streetAddress,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "zipCode", required = false) String zipCode,
			@RequestParam(value = "cardId", required = false) String cardId,
			@RequestParam(value = "precardUrl", required = false) String precardUrl,
			@RequestParam(value = "precardurlother", required = false) String precardurlother,
			@RequestParam(value = "precardurltogether", required = false) String precardurltogether,
			@RequestParam(value = "cardUrl", required = false) MultipartFile cardUrl,
			@RequestParam(value = "cardurlother", required = false) MultipartFile fileother,
			@RequestParam(value = "cardurltogether", required = false) MultipartFile cardurltogether
			
			) {
		
		ResponseString responseString=new ResponseString();
		String userId = StringUtil.obj2String(request.getSession()
				.getAttribute(Constant.USER_ID_SESSION_KEY));
		if(StringUtil.isEmpty(userId))
		{
			responseString.setCode(ResponseCode.NEED_LOGIN);
			responseString.setMessage("你没有登陆!");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.NEED_LOGIN,
			//		"你没有登陆!");
		}
		if(StringUtil.isEmpty(id))
		{
			responseString.setCode(ResponseCode.PARAMETER_ERROR);
			responseString.setMessage("参数出错！");
			return responseString.toString();
			//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错！"); 
		}
		if(StringUtil.isEmpty(name))
		{
			responseString.setCode(ResponseCode.PARAMETER_ERROR);
			responseString.setMessage("姓名不能为空！");
			return responseString.toString();
			//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"姓名不能为空！"); 
		}
		if(StringUtil.isEmpty(streetAddress))
		{
			responseString.setCode(ResponseCode.PARAMETER_ERROR);
			responseString.setMessage("详细地址不能为空！");
			return responseString.toString();
			//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"详细地址不能为空！"); 
		}
		
		if(StringUtil.isEmpty(phone))
		{
			responseString.setCode(ResponseCode.PARAMETER_ERROR);
			responseString.setMessage("电话号不能为空！");
			return responseString.toString();
			//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"电话号不能为空！"); 
		}
		if (!ConsigneeInfoUtil.validatePhone(phone)) {
			responseString.setCode(ResponseCode.CONSIGNEE_PHONE_ERROR);
			responseString.setMessage("电话号码填写错误，请检查！");
			return responseString.toString();
		     // return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "电话号码填写错误，请检查！");
		}
		
		
		if ((!StringUtil.isEmpty(cardId))&&(!ConsigneeInfoUtil.validateCardId(cardId))) {
			responseString.setCode(ResponseCode.CONSIGNEE_PHONE_ERROR);
			responseString.setMessage("身份证号码填写错误，请重新填写！");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
			//		"身份证号码填写错误，请重新填写！");
		}
		
		//正反面图必须同时上传
		if(cardUrl != null && cardUrl.getSize() > 0)
		{
			if((fileother == null || fileother.getSize() == 0)&&(StringUtil.isEmpty(precardurlother)))
			{
				responseString.setCode(ResponseCode.PARAMETER_ERROR);
				responseString.setMessage("正反面必须同时上传！");
				return responseString.toString();
				//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"正反面必须同时上传！"); 
			}
		}
		if(fileother != null && fileother.getSize() > 0)
		{
			if((cardUrl == null || cardUrl.getSize() == 0)&&(StringUtil.isEmpty(precardUrl)))
			{
				responseString.setCode(ResponseCode.PARAMETER_ERROR);
				responseString.setMessage("正反面必须同时上传！");
				return responseString.toString();
				//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"正反面必须同时上传！"); 
			}
		}
		
		
		// 处理提交上来的图片
		// 解决火狐的反斜杠问题 kai 20151006
		
		
			
		String filetype = this.defaultCardFileType;// 要上传的文件类型
		String strtest = this.save258CardDir;
		String strseparator = "";
		if (strtest.indexOf("/") >= 0)// 包含有“/”字符，分隔符用此字符
		{
			strseparator = "/";
		} else {
			strseparator = File.separator;
		}
		//开始处理图片
		// 获取当时的时间缀
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyyMMddHHmmss");
		String timestr = sdf.format(date);
		//如果上传的图片中包含合成图片，那么即会忽略其它的正反两面图片
		String togetherfile=precardurltogether;//保存合成图片
		String fontfile=precardUrl;//保存正面图片路径
		String backfile=precardurlother;//保存背面图片路径
		boolean flag=false;//标识是否要生成合成图
		if(cardurltogether != null && cardurltogether.getSize() > 0)//包含合成图，要清空正反面图
		{
			fileother=null;
			cardUrl=null;
			if (cardurltogether.getSize() > this.defaultCardFileSize) {
				responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
				responseString.setMessage("图像文件过大,请重新尝试!");
				return responseString.toString();
				//return generateResponseObject(
				//		ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
			}

			String originalName = cardurltogether.getOriginalFilename();

			if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
				responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
				responseString.setMessage("上传图像文件格式不对,请重新尝试!");
				return responseString.toString();
				//return generateResponseObject(
				//		ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
			}
			int index = originalName.lastIndexOf('.');
			index = Math.max(index, 0);
			
			togetherfile = this.save258CardDir + strseparator + timestr + "_"+userId+ "_"+"hc"+"_"
					+ StringUtil.generateRandomString(5) + "_"
					+ StringUtil.generateRandomInteger(5)
					+ originalName.substring(index);
			try {
				File file1 = new File(request.getSession().getServletContext()
						.getRealPath("/")
						+ togetherfile);
				cardurltogether.transferTo(file1);
				flag=true;//生成过合成图上，后面将不做处理
			} catch (Exception e) {
				log.error("保存用户合成图片失败,请不要上传图像！", e);
				responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
				responseString.setMessage("保存用户合成图失败，请去除上传图像后再尝试!");
				return responseString.toString();
				//return generateResponseObject(
				//		ResponseCode.CONSIGNEE_CARD_ERROR,
				//		"保存用户合成图失败，请去除上传图像后再尝试!");
			}
			
		}
		//正面图的处理方式
		if(cardUrl != null && cardUrl.getSize() > 0)//包含合成图，要清空正反面图
		{

			if (cardUrl.getSize() > this.defaultCardFileSize) {
				responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
				responseString.setMessage("图像文件过大,请重新尝试!");
				return responseString.toString();
				//return generateResponseObject(
				//		ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
			}

			String originalName = cardUrl.getOriginalFilename();

			if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
				responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
				responseString.setMessage("上传图像文件格式不对,请重新尝试!");
				return responseString.toString();
				//return generateResponseObject(
				//		ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
			}
			int index = originalName.lastIndexOf('.');
			index = Math.max(index, 0);
			
			fontfile = this.save258CardDir + strseparator + timestr + "_"+userId+ "_"+"zm"+"_"
					+ StringUtil.generateRandomString(5) + "_"
					+ StringUtil.generateRandomInteger(5)
					+ originalName.substring(index);
			try {
				File file1 = new File(request.getSession().getServletContext()
						.getRealPath("/")
						+ fontfile);
				cardUrl.transferTo(file1);
			} catch (Exception e) {
				log.error("保存用户正面图片失败,请不要上传图像！", e);
				responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
				responseString.setMessage("保存用户正面图失败，请去除上传图像后再尝试!");
				return responseString.toString();
				//return generateResponseObject(
				//		ResponseCode.CONSIGNEE_CARD_ERROR,
				//		"保存用户正面图失败，请去除上传图像后再尝试!");
			}
			
		}
		
		
		//反面图的处理方式 
		if(fileother != null && fileother.getSize() > 0)//包含合成图，要清空正反面图
		{

			if (fileother.getSize() > this.defaultCardFileSize) {
				
				responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
				responseString.setMessage("图像文件过大,请重新尝试!");
				return responseString.toString();
				//return generateResponseObject(
				//		ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
			}

			String originalName = fileother.getOriginalFilename();

			if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
				responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
				responseString.setMessage("上传图像文件格式不对,请重新尝试!");
				return responseString.toString();
				//return generateResponseObject(
				//		ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
			}
			int index = originalName.lastIndexOf('.');
			index = Math.max(index, 0);
			
			backfile = this.save258CardDir + strseparator + timestr + "_"+userId+ "_"+"fm"+"_"
					+ StringUtil.generateRandomString(5) + "_"
					+ StringUtil.generateRandomInteger(5)
					+ originalName.substring(index);
			try {
				File file1 = new File(request.getSession().getServletContext()
						.getRealPath("/")
						+ backfile);
				fileother.transferTo(file1);
			} catch (Exception e) {
				log.error("保存用户正面图片失败,请不要上传图像！", e);
				responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
				responseString.setMessage("保存用户正面图失败，请去除上传图像后再尝试!");
				return responseString.toString();
				//return generateResponseObject(
				//		ResponseCode.CONSIGNEE_CARD_ERROR,
				//		"保存用户正面图失败，请去除上传图像后再尝试!");
			}
			
		}
	
		//合成处理
		if(!StringUtil.isEmpty(fontfile)&&(!StringUtil.isEmpty(backfile))&&(flag==false))
		{

			boolean flag1=true;
			if((!StringUtil.isEmpty(togetherfile)))//原来不为空，检查是否存在
			{
				String strt = request.getSession().getServletContext()
						.getRealPath("/")
						+ togetherfile;
				File filet = new File(strt);
				if(filet.exists())
				{
					flag1=false;
				}
			}
			if(flag1==true)//之前的文件不存在
			{
				imgcompose img = new imgcompose();
				String str1 = request.getSession().getServletContext()
						.getRealPath("/")
						+ fontfile;
				String str2 = request.getSession().getServletContext()
						.getRealPath("/")
						+ backfile;
				
				
				File file4 = new File(str1);
				File file5 = new File(str2);
				if((file4.exists())&&(file5.exists()))
				{
					String str3 = this.save258CardDir + strseparator + timestr + "_"+userId+ "_"+"hc2"+"_"
							+ StringUtil.generateRandomString(5) + "_"
							+ StringUtil.generateRandomInteger(5);
					togetherfile = str3;
					str3 = request.getSession().getServletContext().getRealPath("/")
							+ str3;
					if (img.createcompics(str1, str2, str3)) {
						togetherfile = togetherfile + ".jpg";
					}
				}
				else
				{
					responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
					responseString.setMessage("生成合成图发生异常!");
					return responseString.toString();
					//return generateResponseObject(
					//		ResponseCode.CONSIGNEE_CARD_ERROR,
					//		"生成合成图发生异常!");
				}
			}
			
		}
		
		
		ConsigneeInfo consigneeInfo = new ConsigneeInfo();
		consigneeInfo.setId(id);
		consigneeInfo.setUserId(userId);
		consigneeInfo.setName(name);
		consigneeInfo.setProvince(province);
		consigneeInfo.setCity(city);
		consigneeInfo.setDistrict(district);
		consigneeInfo.setStreetAddress(streetAddress);
		consigneeInfo.setPhone(phone);
		//consigneeInfo.setTelephone(phone);
		consigneeInfo.setZipCode(zipCode);
		consigneeInfo.setCardId(cardId);
		consigneeInfo.setCardUrl(fontfile);
		consigneeInfo.setCardurlother(backfile);
		consigneeInfo.setCardurltogether(togetherfile);
		
		try {
			 ResponseObject<Object> obj= this.consigneeInfoService.modifyConsigneeInfo(consigneeInfo);
			
			responseString.setCode(obj.getCode());
			responseString.setMessage(obj.getMessage());
			return responseString.toString();
		} catch (Exception e) {
			log.error("修改用户收货地址信息失败", e);
			
			responseString.setCode(ResponseCode.SHOW_EXCEPTION);
			responseString.setMessage("修改收货地址信息失败，请重新尝试！");
			return responseString.toString();
			
			//return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
			//		"修改收货地址信息失败，请重新尝试！");
		}
	}
}

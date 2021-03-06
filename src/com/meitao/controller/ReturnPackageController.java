package com.meitao.controller;

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

import com.meitao.service.ReturnPackageService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ParameterConstants;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.StringUtil;
import com.meitao.common.util.UploadUtil;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.ReturnPackage;

@Controller
public class ReturnPackageController extends BasicController{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4082140452026930414L;
	private static final Logger log = Logger.getLogger(ReturnPackageController.class);
	@Resource(name = "returnPackageService")
	private ReturnPackageService returnPackageService;
	@Value(value = "${page_size}")
	private int defaultPageSize;
	private static final String IMG_PACKAGE = "/img/returnPackage";
	@Value(value = "${default_img_type}")
	private String defaultImgType;
	@Value(value = "${default_img_size}")
	private int defaultImgSize;
	
	@RequestMapping(value = "/admin/returnPackage/search", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<PageSplit<ReturnPackage>> find(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_ID, required = false, defaultValue = "") String id,
			@RequestParam(value = ParameterConstants.COMMON_SEARCH_KEY, required = false, defaultValue = "") String key,
			@RequestParam(value = ParameterConstants.COMMON_SEARCH_TYPE, required = false, defaultValue = "") String type,
			@RequestParam(value = ParameterConstants.COMMON_SEARCH_STATE, required = false, defaultValue = "") String state,
			@RequestParam(value = ParameterConstants.COMMON_SEARCH_CREATE_DATE_START, required = false, defaultValue = "") String createDateStart,
			@RequestParam(value = ParameterConstants.COMMON_SEARCH_CREATE_DATE_END, required = false, defaultValue = "") String createDateEnd,
			@RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex){
		try{
			return returnPackageService.findByKey(id, key, type, state, createDateStart, createDateEnd, pageIndex, defaultPageSize);
		}catch(Exception e){
			log.error("get all return package in admin fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "查询所有退货记录失败");
		}
	}
	@RequestMapping(value = "/user/returnPackage/findByLoginUser", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject<PageSplit<ReturnPackage>> findByLoginUser(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_STATE, required = false) String state,
			@RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required =false, defaultValue = "1") int pageIndex){
		String userId = (String) request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY);
		try{
			return returnPackageService.findByUser(userId, state, pageIndex, defaultPageSize);
		}catch(Exception e){
			log.error("get return package by login user id fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "查询用户的退货记录列表失败");
		}
	}
	
	@RequestMapping(value = "/user/returnPackage/add", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<Object> add(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_TRANSHIPMENT_ID) String transhipmentId,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_PACKAGE_NO) String packageNo,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_RECEIPT) String receipt,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_PICTURE1, required = false) MultipartFile picture1,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_PICTURE2, required = false) MultipartFile picture2,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_PICTURE3, required = false) MultipartFile picture3,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_PICTURE4, required = false) MultipartFile picture4,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_PICTURE5, required = false) MultipartFile picture5,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_PHONE, required = false) String phone,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_ADDRESS, required = false) String address,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_REMRAK, required = false) String remark){
		String userId = (String) request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY);
		if(StringUtil.isEmpty(userId)){
			return generateResponseObject(ResponseCode.NEED_LOGIN, "未找到登陆会员信息");
		}
		if(StringUtil.isEmpty(packageNo)){
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "包裹号不能为空");
		}
		ReturnPackage returnPackage = new ReturnPackage();
		if(Constant.RETURN_PACKAGE_HAS_RECEIPT.equals(receipt)){// has receipt, must upload 1 picture at least
			if(!UploadUtil.hasFile(picture1, picture2, picture3, picture4, picture5)){
				return generateResponseObject(ResponseCode.PARAMETER_ERROR, "最少要有一张图片");
			}
			String realPath = request.getSession().getServletContext().getRealPath("/");
			String[] fileNames = UploadUtil.uploadFile(IMG_PACKAGE, realPath, defaultImgSize, defaultImgType, false, 0, picture1, picture2, picture3, picture4, picture5);
			if(fileNames[0].indexOf(".") < 0){
				return generateResponseObject(ResponseCode.RETURN_PACKAGE_PICTURE_ERROR, fileNames[0]);
			}else{
				returnPackage.setPic1(fileNames[0]);
				returnPackage.setPic2(fileNames[1]);
				returnPackage.setPic3(fileNames[2]);
				returnPackage.setPic4(fileNames[3]);
				returnPackage.setPic5(fileNames[4]);
			}
		}else{
			if(StringUtil.isEmpty(address) || StringUtil.isEmpty(remark)){
				return generateResponseObject(ResponseCode.PARAMETER_ERROR, "地址或备注不能为空");
			}
		}
		
		returnPackage.setUserId(userId);
		returnPackage.setReceipt(receipt);
		returnPackage.setPackageNo(packageNo);
		returnPackage.setTranshipmentId(transhipmentId);
		returnPackage.setAddress(address);
		returnPackage.setRemark(remark);
		returnPackage.setPhone(phone);
		try{
			return returnPackageService.add(returnPackage);
		}catch(Exception e){
			log.error("add return package data fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "添加退回包裹数据失败");
		}
		
	}
	@RequestMapping(value = {"/admin/returnPackage/getById", "/user/returnPackage/getById"}, method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject<ReturnPackage> getById(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_ID) String id){
		try{
			return this.returnPackageService.findById(id);
		}catch(Exception e){
			log.error("get return package data by id fail");
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取指定退货数据失败");
		}
	}
	@RequestMapping(value = "/admin/returnPackage/audit", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<Object> audit(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_ID) String id,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_STATE) String state,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_SHIPPING_FEE) String shippingFee,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_EMP_REMARK) String empRemark,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_EMP_EXPRESS_NO) String empExpressNo,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_EMP_EXPRESS_COMPANY) String empExpressCompany,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_EMP_PICTURE, required = false) MultipartFile empPicture){
		if(state.equals(Constant.RETURN_PACKAGE_STATE_NOT_TREAT)){
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "还没选择审核结果");
		}
		String empId = (String) request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY);
		String empPic = "";
		if(UploadUtil.hasFile(empPicture)){
			String realPath = request.getSession().getServletContext().getRealPath("/");
			empPic = UploadUtil.uploadFile(empPicture, IMG_PACKAGE, realPath, defaultImgSize, defaultImgType, false, 0);
		}
		try{
			return this.returnPackageService.audit(id, empId, state, empRemark, empExpressNo, empExpressCompany, empPic, shippingFee);
		}catch(Exception e){
			log.error("audit return package by emp fail");
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "审核退货操作失败");
		}
	}
	@RequestMapping(value = "/user/returnPackage/modify", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<Object> modify(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_ID) String id,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_ADDRESS, required = false) String address,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_PACKAGE_NO, required = false) String packageNo,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_PHONE, required = false) String phone,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_PICTURE1, required = false) MultipartFile picture1,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_PICTURE2, required = false) MultipartFile picture2,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_PICTURE3, required = false) MultipartFile picture3,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_PICTURE4, required = false) MultipartFile picture4,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_PICTURE5, required = false) MultipartFile picture5,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_PIC1, required = false) String pic1,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_PIC2, required = false) String pic2,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_PIC3, required = false) String pic3,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_PIC4, required = false) String pic4,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_PIC5, required = false) String pic5,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_RECEIPT, required = false) String receipt,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_REMRAK, required = false) String remark,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_STATE) String state){
		if(state.equals(Constant.RETURN_PACKAGE_STATE_TREAT)){
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "不能对已成功的退货进行修改");
		}
		if(StringUtil.isEmpty(packageNo)){
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "包裹号不能为空");
		}
		ReturnPackage returnPackage = new ReturnPackage();
		if(Constant.RETURN_PACKAGE_HAS_RECEIPT.equals(receipt)){// has receipt, must upload 1 picture at least
			if(!UploadUtil.hasFile(picture1, picture2, picture3, picture4, picture5) && !UploadUtil.isFileUrl(pic1, pic2, pic3, pic4, pic5)){
				return generateResponseObject(ResponseCode.PARAMETER_ERROR, "最少要有一张图片");
			}
			String realPath = request.getSession().getServletContext().getRealPath("/");
			String[] fileNames = UploadUtil.uploadFileWithoutSort(IMG_PACKAGE, realPath, defaultImgSize, defaultImgType, false, 0, picture1, picture2, picture3, picture4, picture5);
			if(fileNames[0].indexOf(".") < 0){
				return generateResponseObject(ResponseCode.RETURN_PACKAGE_PICTURE_ERROR, fileNames[0]);
			}else{
				if(fileNames[0].indexOf(".") > 0){
					returnPackage.setPic1(fileNames[0]);
				}else{
					returnPackage.setPic1(pic1);
				}
				if(fileNames[1].indexOf(".") > 0){
					returnPackage.setPic2(fileNames[1]);
				}else{
					returnPackage.setPic2(pic2);
				}
				if(fileNames[2].indexOf(".") > 0){
					returnPackage.setPic3(fileNames[2]);
				}else{
					returnPackage.setPic3(pic3);
				}
				if(fileNames[3].indexOf(".") > 0){
					returnPackage.setPic4(fileNames[3]);
				}else{
					returnPackage.setPic4(pic4);
				}
				if(fileNames[4].indexOf(".") > 0){
					returnPackage.setPic5(fileNames[4]);
				}else{
					returnPackage.setPic5(pic5);
				}
			}
		}else{
			if(StringUtil.isEmpty(address) || StringUtil.isEmpty(remark)){
				return generateResponseObject(ResponseCode.PARAMETER_ERROR, "地址或备注不能为空");
			}
			returnPackage.setPic1("");
			returnPackage.setPic2("");
			returnPackage.setPic3("");
			returnPackage.setPic4("");
			returnPackage.setPic5("");
		}
		
		returnPackage.setId(id);
		returnPackage.setReceipt(receipt);
		returnPackage.setPackageNo(packageNo);
		returnPackage.setAddress(address);
		returnPackage.setRemark(remark);
		returnPackage.setPhone(phone);
		try{
			return returnPackageService.modifyByUser(returnPackage);
		}catch(Exception e){
			log.error("add return package data fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "添加退回包裹数据失败");
		}
	}
	@RequestMapping(value = "/user/returnPackage/cancel", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<Object> cancelByUser(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_TRANSHIPMENT_ID) String transhipmentId){
		String userId = StringUtil.obj2String(request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY));
		try{
			return returnPackageService.cancelByUser(transhipmentId, userId);
		}catch(Exception e){
			log.error("add return package data fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "取消退回包裹数据失败");
		}
	}
	@RequestMapping(value = "/admin/returnPackage/getAllStateCount", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject<String[]> getAllStateCount(HttpServletRequest request){
		try{
			return this.returnPackageService.getAllStateCount();
		}catch(Exception e){
			log.error("根据用户获取事件数量出现异常");
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "根据用户获取事件数量出现异常");
		}
	}
}

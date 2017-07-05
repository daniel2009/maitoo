package com.meitao.controller;



import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


import jxl.common.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.ResponseBody;

import com.meitao.service.SendUserService;

import com.meitao.common.constants.Constant;

import com.meitao.common.constants.ResponseCode;

import com.meitao.common.util.StringUtil;

import com.meitao.model.ResponseObject;


@Controller
public class SendUserController extends BasicController {

	private static final long serialVersionUID = 1058971052693785202L;
	private static final Logger log = Logger.getLogger(SendUserController.class);

	@Resource(name = "sendUserService")
	public SendUserService sendUserService;
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


	@RequestMapping(value = "/user/get_sendusers", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> getSendUsers(HttpServletRequest request) {

		String userId = StringUtil.obj2String(request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY));
		if((userId==null)||(userId.equalsIgnoreCase("")))
		{
			return new ResponseObject<Object>(ResponseCode.NEED_LOGIN, "请登录后操作!");
		}
		try {
			return this.sendUserService.getallbyid(userId);
			

		} catch (Exception e) {
			log.error("获取历史记录出现异常", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					"获取历史记录出现异常");
		}
	}
	
}

//建立海关打印批次管理相关控制层
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

import com.meitao.common.constants.ParameterConstants;
import com.meitao.common.constants.ResponseCode;

import com.meitao.model.PageSplit;

import com.meitao.model.ResponseObject;

import com.meitao.controller.BasicController;

import com.meitao.model.Send_User;
import com.meitao.service.Send_UserService;

@Controller
public class Send_UserController extends BasicController {

	private static final long serialVersionUID = 1058971052693785202L;
	private static final Logger log = Logger
			.getLogger(Send_UserController.class);


	@Resource(name = "send_UserService")
	private Send_UserService send_UserService;
	@Value(value = "${page_size}")
	private int defaultPageSize;

	
	@RequestMapping(value="/admin/send_user/search", method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseObject<PageSplit<Send_User>> getsenduserbyadmin(HttpServletRequest request,
			@RequestParam(value = "info") String info,
			@RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex){
		try{
			pageIndex = Math.max(pageIndex, 1);
			return this.send_UserService.searchSendUserByInfo(info, defaultPageSize, pageIndex);
			
		}catch(Exception e){
			log.error("get sendusers fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "get sendusers fail");
		}
	}
	
	

}
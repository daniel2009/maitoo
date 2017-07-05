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

import com.meitao.model.Receive_User;
import com.meitao.model.ResponseObject;


import com.meitao.service.Receive_UserService;

import com.meitao.controller.BasicController;


@Controller
public class Receive_UserController extends BasicController {

	private static final long serialVersionUID = 1058971052693785202L;
	private static final Logger log = Logger
			.getLogger(Receive_UserController.class);


	@Resource(name = "receive_UserService")
	private Receive_UserService receive_UserService;
	@Value(value = "${page_size}")
	private int defaultPageSize;

	
	@RequestMapping(value="/admin/receive_user/search", method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseObject<PageSplit<Receive_User>> getsenduserbyadmin(HttpServletRequest request,
			@RequestParam(value = "info") String info,
			@RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex){
		try{
			pageIndex = Math.max(pageIndex, 1);
			return this.receive_UserService.searchReceiveUserByInfo(info, defaultPageSize, pageIndex);
			
		}catch(Exception e){
			log.error("get receive user fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "get receive user fail");
		}
	}
	
	//获取一条待验证的信息
	@RequestMapping(value="/admin/receive_user/getoneverfy", method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseObject<Object> getoneverfy(HttpServletRequest request){
		try{
			
			return this.receive_UserService.getOneVerfyMessage();
			
		}catch(Exception e){
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取一条待验证信息失败");
		}
	}
	
		//获取总的待验证身份证信息数量
		@RequestMapping(value="/admin/receive_user/getVerfyNumbers", method={RequestMethod.POST,RequestMethod.GET})
		@ResponseBody
		public ResponseObject<Object> getVerfyNumbers(HttpServletRequest request){
			try{
				
				return this.receive_UserService.getNumbersVerfyMessage();
				
			}catch(Exception e){
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取一条待验证信息失败");
			}
		}
		
		//修改验证结果请求
		@RequestMapping(value="/admin/receive_user/saveVerify", method={RequestMethod.POST,RequestMethod.GET})
		@ResponseBody
		public ResponseObject<Object> saveVerify(HttpServletRequest request,
				@RequestParam(value = "id") String id,
				@RequestParam(value = "modifyDate") String modifyDate,
				@RequestParam(value = "isPass") String isPass
				){
			try{
				return this.receive_UserService.saveverfyresult(id, modifyDate, isPass, request);
				//return this.receive_UserService.getNumbersVerfyMessage();
				//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取一条待验证信息失败");
			}catch(Exception e){
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改发生异常");
			}
		}
}
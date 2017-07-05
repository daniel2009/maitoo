package com.meitao.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;





import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.meitao.service.NewsService;
import com.meitao.common.constants.Constant;

import com.meitao.common.constants.ResponseCode;

import com.meitao.common.util.StringUtil;

import com.meitao.model.ResponseObject;
@Controller
public class M_StorageController extends BasicController {

	private static final long serialVersionUID = 6582185005680513923L;
	//private static final Logger log = Logger.getLogger(M_StorageController.class);
	@Resource(name = "newsService")
	private NewsService newsService;
	@Value(value = "${page_size}")
	private int defaultPageSize;
	//private static final String IMG_PACKAGE = "/img/news";
	@Value(value = "${default_img_type}")
	private String defaultImgType;
	@Value(value = "${default_img_size}")
	private int defaultImgSize;

	//获取唯一的仓位号，如果原来同一个用户中包含了相同类型的仓位号，将返回此仓位号
	@RequestMapping(value = "/m_storage/get_unique_no", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> addNews(HttpServletRequest request,
			@RequestParam(value = "type", required = false) String type) {
		
		if(StringUtil.isEmpty(type))
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数类型错误！");
		}
		
		
		if(Constant.STORAGE_TYPE0.equalsIgnoreCase(type)||Constant.STORAGE_TYPE1.equalsIgnoreCase(type)||Constant.STORAGE_TYPE2.equalsIgnoreCase(type))
		{
			
		}
		else
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数类型错误！");
		}
		

		try {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数类型错误！");
		} catch (Exception e) {
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取仓失败");
		}
	}

}

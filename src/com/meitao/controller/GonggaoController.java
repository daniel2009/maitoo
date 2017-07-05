package com.meitao.controller;

import java.net.URLDecoder;
import java.util.Date;

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

import com.meitao.service.GonggaoService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ParameterConstants;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.GonggaoUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.common.util.UploadUtil;
import com.meitao.exception.ServiceException;
import com.meitao.model.Gonggao;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.ResponseString;
@Controller
public class GonggaoController extends BasicController {

	private static final long serialVersionUID = 6582185005680513923L;
	private static final Logger log = Logger.getLogger(GonggaoController.class);
	@Resource(name = "gonggaoService")
	private GonggaoService gonggaoService;
	@Value(value = "${page_size}")
	private int defaultPageSize;
	@Value(value = "${default_img_type}")
	private String defaultImgType;
	@Value(value = "${default_img_size}")
	private int defaultImgSize;

	private static final String IMG_PACKAGE = "/img/news/";
	@RequestMapping(value = "/gonggao/add", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> addgonggao(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.GONGGAO_TITLE) String title,
	        @RequestParam(value = ParameterConstants.GONGGAO_CONTENT) String content
	        //@RequestParam(value = ParameterConstants.gonggao_AUTHOR) String author,
	       // @RequestParam(value = ParameterConstants.gonggao_RELEASETIME) Date releasetime
	        ) {
		//System.out.println("=======##############======");
		//System.out.println(releasetime);
		// 重新写
		Gonggao gonggao = new Gonggao();
		gonggao.setTitle(title);
		gonggao.setContent(content);
		//gonggao.setAuthor(author);
		//gonggao.setReleaseTime(releasetime);
		try {
			return this.gonggaoService.saveGonggao(gonggao);
		} catch (Exception e) {
			log.error("添加新闻失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "添加新闻出现异常");
		}
	}

	@RequestMapping(value = "/gonggao/modify", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> modifygonggao() {
		Gonggao gonggao = new Gonggao();
		try {
			return this.gonggaoService.modifyGonggao(gonggao);
		} catch (Exception e) {
			log.error("修改新闻对象失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改新闻出现异常");
		}
	}

	@RequestMapping(value = "/gonggao/del", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> delgonggao(@RequestParam(value = ParameterConstants.GONGGAO_ID) String id) {
		if (!GonggaoUtil.validateId(id)) {
			return generateResponseObject(ResponseCode.GONGGAO_ID_ERROR, "参数无效");
		}
		try {
			return this.gonggaoService.deleteGonggao(id);
		} catch (ServiceException e) {
			log.error("根据id获取新闻失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "删除新闻失败");
		}
	}

	@RequestMapping(value = "/gonggao/get_one", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Gonggao> getById(@RequestParam(value = ParameterConstants.GONGGAO_ID) String id) {
		if (!GonggaoUtil.validateId(id)) {
			return generateResponseObject(ResponseCode.GONGGAO_ID_ERROR, "参数无效");
		}

		try {
			return this.gonggaoService.getGonggaoById(id);
		} catch (Exception e) {
			log.error("根据id获取新闻对象失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取新闻失败，出现异常!");
		}
	}

	/*@RequestMapping(value = "/gonggao/search", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<PageSplit<gonggao>> searchByTitle(
	        @RequestParam(value = ParameterConstants.gonggao_TITLE, required = false, defaultValue = "") String key,
	        @RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex) {
		try {
			pageIndex = Math.max(pageIndex, 1);
			return this.gonggaoService.getgonggaoByKey(key,"", defaultPageSize, pageIndex);
		} catch (Exception e) {
			log.error("获取新闻列表失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取新闻列表失败！");
		}
	}*/
	
	//查找接口，用于客户端
	@RequestMapping(value = "/gonggao/search", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<PageSplit<Gonggao>> searchpagetbyanyone(HttpServletRequest request,
	        @RequestParam(value = "key", required = false, defaultValue = "") String key,
	        @RequestParam(value = "column", required = false, defaultValue = "") String column,
	        @RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex) {
		try {
			
			 key = URLDecoder.decode(key, "UTF-8");
			pageIndex = Math.max(pageIndex, 1);
			//return this.gonggaoService.getGonggaoByKey(key, column,defaultPageSize, pageIndex);
			return this.gonggaoService.getgonggaoByKey(key, column,defaultPageSize, pageIndex);
			//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取新闻列表失败！");
		} catch (Exception e) {
			log.error("获取新闻列表失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取新闻列表失败！");
		}
	}	
	
	
	@RequestMapping(value = "/admin/gonggao/search", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<PageSplit<Gonggao>> searchpageyadmin(HttpServletRequest request,
	        @RequestParam(value = "key", required = false, defaultValue = "") String key,
	        @RequestParam(value = "column", required = false, defaultValue = "") String column,
	        @RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex) {
		try {
			 key = URLDecoder.decode(key, "UTF-8");
			pageIndex = Math.max(pageIndex, 1);
			return this.gonggaoService.getgonggaoByKey(key, column,defaultPageSize, pageIndex);
			//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取新闻列表失败！");
		} catch (Exception e) {
			log.error("获取新闻列表失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取新闻列表失败！");
		}
	}	
	
	@RequestMapping(value = "/admin/gonggao/add", produces="text/plain;charset=UTF-8",method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String addgonggaobyadmin(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.GONGGAO_TITLE, required = false, defaultValue = "") String title,
			@RequestParam(value = ParameterConstants.GONGGAO_CONTENT, required = false, defaultValue = "") String content,
			@RequestParam(value = "picture", required = false) MultipartFile picture
	        //@RequestParam(value = ParameterConstants.gonggao_AUTHOR) String author,
	        //@RequestParam(value = ParameterConstants.gonggao_RELEASETIME) Date releasetime
	        ) throws Exception {
		//System.out.println("=======##############======");
		//System.out.println(releasetime);
		// 重新写
		ResponseString responseString=new ResponseString();
		String pictureurl="";
		if(UploadUtil.hasFile(picture)){
			String realPath = request.getSession().getServletContext().getRealPath("/");
			String[] fileNames = UploadUtil.uploadFile(IMG_PACKAGE, realPath, defaultImgSize, defaultImgType, true, 380, picture);
			if(fileNames[0].indexOf(".") < 0){
				responseString.setCode(ResponseCode.NEWS_PICUTER_FAILURE);
				responseString.setMessage(fileNames[0]);
				return responseString.toString();
				//return generateResponseObject(ResponseCode.NEWS_PICUTER_FAILURE, fileNames[0]);
			}else{
				//news.setPic1(fileNames[0]);
				pictureurl=fileNames[0];
			}
		}
		
		
		Gonggao gonggao = new Gonggao();
		gonggao.setTitle(title);
		gonggao.setContent(content);
		gonggao.setViewcontent(content);
		gonggao.setImg(pictureurl);
		
		String empName = StringUtil.obj2String(request.getSession()
				.getAttribute(Constant.EMP_ACCOUNT_SESSION_KEY));
		String empId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY));
		gonggao.setAuthorid(empId);
		gonggao.setAuthor(empName);
		gonggao.setRemark(empName+" 创建！");
		//gonggao.setAuthor(author);
		//gonggao.setReleaseTime(releasetime);
		try {
			ResponseObject<Object> obj= this.gonggaoService.saveGonggao(gonggao);
			responseString.setCode(obj.getCode());
			responseString.setMessage(obj.getMessage());
			return responseString.toString();
			//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "添加新闻出现异常");
		} catch (Exception e) {
			log.error("添加新闻失败", e);
			responseString.setCode(ResponseCode.SHOW_EXCEPTION);
			responseString.setMessage("添加公告出现异常");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "添加公告出现异常");
		}
	}
	
	
	@RequestMapping(value = "/admin/gonggao/del", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> delgonggaobyadmin(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.GONGGAO_IDS) String[] ids) {
		if ((ids != null) && (ids.length >0)) {
			for(String id :ids)
			{
				if (!GonggaoUtil.validateId(id)) {
					return generateResponseObject(ResponseCode.GONGGAO_ID_ERROR, "参数无效");
				}
			}
		}
		try {
			return this.gonggaoService.deletegonggaobyadmin(ids);
			//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "删除新闻失败");
		} catch (ServiceException e) {
		//} catch (Exception e) {
			log.error("根据id获取新闻失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "删除新闻失败");
		}
	}
	@RequestMapping(value = "/gonggao/getonegonggao", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Gonggao> anyonegetById(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.GONGGAO_ID) String id) {
		if (!GonggaoUtil.validateId(id)) {
			return generateResponseObject(ResponseCode.GONGGAO_ID_ERROR, "参数无效");
		}

		try {
			//return this.gonggaoService.GetGonggaoById(id);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取新闻失败，出现异常!");
		} catch (Exception e) {
			log.error("根据id获取新闻对象失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取新闻失败，出现异常!");
		}
	}
	
	@RequestMapping(value = "/admin/gonggao/get_one", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Gonggao> admingetById(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.GONGGAO_ID) String id) {
		if (!GonggaoUtil.validateId(id)) {
			return generateResponseObject(ResponseCode.GONGGAO_ID_ERROR, "参数无效");
		}

		try {
			return this.gonggaoService.getGonggaoById(id);
			//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取新闻失败，出现异常!");
		} catch (Exception e) {
			log.error("根据id获取新闻对象失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取新闻失败，出现异常!");
		}
	}

	@RequestMapping(value = "/admin/gonggao/modify",produces="text/plain;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String modifygonggaobyadmin(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.GONGGAO_TITLE, required = false, defaultValue = "") String title,
			@RequestParam(value = ParameterConstants.GONGGAO_CONTENT, required = false, defaultValue = "") String content,
			@RequestParam(value = ParameterConstants.GONGGAO_ID, required = false, defaultValue = "") String id,
			@RequestParam(value = "img", required = false, defaultValue = "") String img,
			@RequestParam(value = "picture1", required = false) MultipartFile picture1
	        ) throws Exception {
		
		
		
		ResponseString responseString=new ResponseString();
		
		if (!GonggaoUtil.validateId(id)) {
			responseString.setCode(ResponseCode.GONGGAO_ID_ERROR);
			responseString.setMessage("参数无效");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.GONGGAO_ID_ERROR, "参数无效");
		}
		
		if(UploadUtil.hasFile(picture1)){
			String realPath = request.getSession().getServletContext().getRealPath("/");
			String[] fileNames = UploadUtil.uploadFile(IMG_PACKAGE, realPath, defaultImgSize, defaultImgType, true, 380, picture1);
			if(fileNames[0].indexOf(".") < 0){
				responseString.setCode(ResponseCode.NEWS_PICUTER_FAILURE);
				responseString.setMessage(fileNames[0]);
				return responseString.toString();
				//return generateResponseObject(ResponseCode.NEWS_PICUTER_FAILURE, fileNames[0]);
			}else{
				if(fileNames[0].indexOf(".") > 0){
					
					img=fileNames[0];
				}
			}
		}
		
		
		
		Gonggao gonggao = new Gonggao();
		gonggao.setId(id);
		gonggao.setContent(content);
		gonggao.setViewcontent(content);
		gonggao.setTitle(title);
		gonggao.setImg(img);
		String empName = StringUtil.obj2String(request.getSession()
				.getAttribute(Constant.EMP_ACCOUNT_SESSION_KEY));
		gonggao.setRemark(empName+" 修改!");
		
		String date = DateUtil.date2String(new Date());// 修改时间
		gonggao.setModifytime(date);
		try {
			ResponseObject<Object> obj= this.gonggaoService.modifyGonggao(gonggao);
			responseString.setCode(obj.getCode());
			responseString.setMessage(obj.getMessage());
			return responseString.toString();
			//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改新闻出现异常");
		} catch (Exception e) {
			log.error("修改新闻对象失败", e);
			responseString.setCode(ResponseCode.SHOW_EXCEPTION);
			responseString.setMessage("修改公告出现异常");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改新闻出现异常");
		}
	}
	
	//查找接口，用于客户端
		@RequestMapping(value = "/gonggao/search_size", method = { RequestMethod.POST, RequestMethod.GET })
		@ResponseBody
		public ResponseObject<PageSplit<Gonggao>> searchpagetbyanyonesize(HttpServletRequest request,
		        @RequestParam(value = "key", required = false, defaultValue = "") String key,
		        @RequestParam(value = "column", required = false, defaultValue = "") String column,
		        @RequestParam(value = "size", required = false, defaultValue = "") String size,
		        @RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex) {
			try {
				
				 key = URLDecoder.decode(key, "UTF-8");
				pageIndex = Math.max(pageIndex, 1);
				if(StringUtil.isEmpty(size))
				{
					size=Integer.toString(defaultPageSize);
				}
				//return this.gonggaoService.getGonggaoByKey(key, column,defaultPageSize, pageIndex);
				return this.gonggaoService.getgonggaoByKey(key, column,Integer.parseInt(size), pageIndex);
				//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取新闻列表失败！");
			} catch (Exception e) {
				log.error("获取新闻列表失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取新闻列表失败！");
			}
		}	
		
		
		
		@RequestMapping(value = "/gonggao/getbyid", method = { RequestMethod.POST, RequestMethod.GET })
		@ResponseBody
		public ResponseObject<Gonggao> getgongaotById(HttpServletRequest request,
				@RequestParam(value = ParameterConstants.GONGGAO_ID) String id) {
			if (!GonggaoUtil.validateId(id)) {
				return generateResponseObject(ResponseCode.GONGGAO_ID_ERROR, "参数无效");
			}

			try {
				return this.gonggaoService.getGonggaoById(id);
				//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取新闻失败，出现异常!");
			} catch (Exception e) {
				log.error("根据id获取公告对象失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取公告失败，出现异常!");
			}
		}

		
		//查找接口，用于客户端
		@RequestMapping(value = "/gonggao/search_byguest", method = { RequestMethod.POST, RequestMethod.GET })
		@ResponseBody
		public ResponseObject<PageSplit<Gonggao>> searchbyguest(HttpServletRequest request,
		        @RequestParam(value = "content", required = false, defaultValue = "") String content,
		        @RequestParam(value = "size", required = false, defaultValue = "") String size,
		        @RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex) {
			try {
				
				content = URLDecoder.decode(content, "UTF-8");
				pageIndex = Math.max(pageIndex, 1);
				if(StringUtil.isEmpty(size))
				{
					size=Integer.toString(defaultPageSize);
				}
				//return this.gonggaoService.getGonggaoByKey(key, column,defaultPageSize, pageIndex);
				return this.gonggaoService.getgonggaoByKey(content, "",Integer.parseInt(size), pageIndex);
				//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取新闻列表失败！");
			} catch (Exception e) {
				log.error("获取公告列表失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取公告列表失败！");
			}
		}	
}

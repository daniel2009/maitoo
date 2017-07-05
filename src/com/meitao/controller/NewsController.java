package com.meitao.controller;

import java.net.URLDecoder;



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

import com.meitao.service.NewsService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ParameterConstants;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.NewsUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.common.util.UploadUtil;
import com.meitao.exception.ServiceException;
import com.meitao.model.News;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.ResponseString;
@Controller
public class NewsController extends BasicController {

	private static final long serialVersionUID = 6582185005680513923L;
	private static final Logger log = Logger.getLogger(NewsController.class);
	@Resource(name = "newsService")
	private NewsService newsService;
	@Value(value = "${page_size}")
	private int defaultPageSize;
	private static final String IMG_PACKAGE = "/img/news/";
	@Value(value = "${default_img_type}")
	private String defaultImgType;
	@Value(value = "${default_img_size}")
	private int defaultImgSize;

	@RequestMapping(value = "/news/add", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> addNews(HttpServletRequest request, News news,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_PICTURE1, required = false) MultipartFile picture1) {
		if(UploadUtil.hasFile(picture1)){
			String realPath = request.getSession().getServletContext().getRealPath("/");
			String[] fileNames = UploadUtil.uploadFile(IMG_PACKAGE, realPath, defaultImgSize, defaultImgType, true, 380, picture1);
			if(fileNames[0].indexOf(".") < 0){
				return generateResponseObject(ResponseCode.NEWS_PICUTER_FAILURE, fileNames[0]);
			}else{
				news.setPic1(fileNames[0]);
			}
		}
		try {
			return this.newsService.saveNews(news);
		} catch (Exception e) {
			log.error("添加新闻失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "添加新闻出现异常");
		}
	}

	@RequestMapping(value = "/news/modify", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> modifyNews(HttpServletRequest request, News news,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_PICTURE1, required = false) MultipartFile picture1) {
		if(UploadUtil.hasFile(picture1)){
			String realPath = request.getSession().getServletContext().getRealPath("/");
			String[] fileNames = UploadUtil.uploadFile(IMG_PACKAGE, realPath, defaultImgSize, defaultImgType, true, 380, picture1);
			if(fileNames[0].indexOf(".") < 0){
				return generateResponseObject(ResponseCode.NEWS_PICUTER_FAILURE, fileNames[0]);
			}else{
				if(fileNames[0].indexOf(".") > 0){
					news.setPic1(fileNames[0]);
				}
			}
		}
		try {
			return this.newsService.modifyNews(news);
		} catch (Exception e) {
			log.error("修改新闻对象失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改新闻出现异常");
		}
	}

	@RequestMapping(value = "/news/del", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> delNews(@RequestParam(value = ParameterConstants.NEWS_ID) String id) {
		if (!NewsUtil.validateId(id)) {
			return generateResponseObject(ResponseCode.NEWS_ID_ERROR, "参数无效");
		}
		try {
			return this.newsService.deleteNews(id);
		} catch (ServiceException e) {
			log.error("根据id获取新闻失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "删除新闻失败");
		}
	}

	@RequestMapping(value = "/news/get_one", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<News> getById(@RequestParam(value = ParameterConstants.NEWS_ID) String id) {
		if (!NewsUtil.validateId(id)) {
			return generateResponseObject(ResponseCode.NEWS_ID_ERROR, "参数无效");
		}

		try {
			return this.newsService.getNewsById(id);
		} catch (Exception e) {
			log.error("根据id获取新闻对象失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取新闻失败，出现异常!");
		}
	}

	/*@RequestMapping(value = "/news/search", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<PageSplit<News>> searchByTitle(
	        @RequestParam(value = ParameterConstants.NEWS_TITLE, required = false, defaultValue = "") String key,
	        @RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex) {
		try {
			pageIndex = Math.max(pageIndex, 1);
			return this.newsService.getNewsByKey(key,"", defaultPageSize, pageIndex);
		} catch (Exception e) {
			log.error("获取新闻列表失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取新闻列表失败！");
		}
	}*/
	
	//查找接口，用于客户端
	@RequestMapping(value = "/news/search", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<PageSplit<News>> searchpagetbyanyone(HttpServletRequest request,
	        @RequestParam(value = "key", required = false, defaultValue = "") String key,
	        @RequestParam(value = "column", required = false, defaultValue = "") String column,
	        @RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex) {
		try {
			
			 key = URLDecoder.decode(key, "UTF-8");
			pageIndex = Math.max(pageIndex, 1);
			return this.newsService.getNewsByKey(key, column,defaultPageSize, pageIndex);
		} catch (Exception e) {
			log.error("获取新闻列表失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取新闻列表失败！");
		}
	}	
	
	
	@RequestMapping(value = "/admin/news/search", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<PageSplit<News>> searchpageyadmin(HttpServletRequest request,
	        @RequestParam(value = "key", required = false, defaultValue = "") String key,
	        @RequestParam(value = "column", required = false, defaultValue = "") String column,
	        @RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex) {
		try {
			 key = URLDecoder.decode(key, "UTF-8");
			pageIndex = Math.max(pageIndex, 1);
			return this.newsService.getNewsByKey(key, column,defaultPageSize, pageIndex);
		} catch (Exception e) {
			log.error("获取新闻列表失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取新闻列表失败！");
		}
	}	
	
	@RequestMapping(value = "/admin/news/add",produces="text/plain;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String addNewsbyadmin(HttpServletRequest request, News news,
			@RequestParam(value = "picture1", required = false) MultipartFile picture1) {
		ResponseString responseString=new ResponseString();
		
		if(UploadUtil.hasFile(picture1)){
			String realPath = request.getSession().getServletContext().getRealPath("/");
			String[] fileNames = UploadUtil.uploadFile(IMG_PACKAGE, realPath, defaultImgSize, defaultImgType, true, 380, picture1);
			if(fileNames[0].indexOf(".") < 0){
				responseString.setCode(ResponseCode.NEWS_PICUTER_FAILURE);
				responseString.setMessage(fileNames[0]);
				return responseString.toString();
				//return generateResponseObject(ResponseCode.NEWS_PICUTER_FAILURE, fileNames[0]);
			}else{
				news.setPic1(fileNames[0]);
			}
		}
		String empId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY));
		String empName = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ACCOUNT_SESSION_KEY));
		news.setAuthorid(empId);
		news.setAuthor(empName);
		news.setViewcontent(news.getContent());
		news.setRemark(empName+" 创建！");
		try {
			
			ResponseObject<Object> obj= this.newsService.saveNews(news);
			
			responseString.setCode(obj.getCode());
			responseString.setMessage(obj.getMessage());
			return responseString.toString();
			
		} catch (Exception e) {
			log.error("添加新闻失败", e);
			responseString.setCode(ResponseCode.SHOW_EXCEPTION);
			responseString.setMessage("添加新闻出现异常");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "添加新闻出现异常");
		}
	}
	
	
	@RequestMapping(value = "/admin/news/del", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> delNewsbyadmin(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.NEWS_IDS) String[] ids) {
		if ((ids != null) && (ids.length >0)) {
			for(String id :ids)
			{
				if (!NewsUtil.validateId(id)) {
					return generateResponseObject(ResponseCode.NEWS_ID_ERROR, "参数无效");
				}
			}
		}
		try {
			return this.newsService.deleteNewsbyadmin(ids);
		} catch (ServiceException e) {
			log.error("根据id获取新闻失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "删除新闻失败");
		}
	}
	@RequestMapping(value = "/news/getonenews", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<News> anyonegetById(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.NEWS_ID) String id) {
		if (!NewsUtil.validateId(id)) {
			return generateResponseObject(ResponseCode.NEWS_ID_ERROR, "参数无效");
		}

		try {
			return this.newsService.getNewsById(id);
		} catch (Exception e) {
			log.error("根据id获取新闻对象失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取新闻失败，出现异常!");
		}
	}
	
	@RequestMapping(value = "/admin/news/get_one", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<News> admingetById(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.NEWS_ID) String id) {
		if (!NewsUtil.validateId(id)) {
			return generateResponseObject(ResponseCode.NEWS_ID_ERROR, "参数无效");
		}

		try {
			return this.newsService.getNewsById(id);
		} catch (Exception e) {
			log.error("根据id获取新闻对象失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取新闻失败，出现异常!");
		}
	}

	@RequestMapping(value = "/admin/news/modify",produces="text/plain;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String modifyNewsbyadmin(HttpServletRequest request, News news,
			@RequestParam(value = ParameterConstants.RETURN_PACKAGE_PICTURE1, required = false) MultipartFile picture1) {
		ResponseString responseString=new ResponseString();
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
					news.setPic1(fileNames[0]);
				}
			}
		}
		news.setViewcontent(news.getContent());
		String empName = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ACCOUNT_SESSION_KEY));
		news.setRemark(empName+" 修改!");
		try {
			 ResponseObject<Object> obj= this.newsService.modifyNews(news);
			 responseString.setCode(obj.getCode());
				responseString.setMessage(obj.getMessage());
				return responseString.toString();
		} catch (Exception e) {
			log.error("修改新闻对象失败", e);
			responseString.setCode(ResponseCode.SHOW_EXCEPTION);
			responseString.setMessage("修改新闻出现异常");
			return responseString.toString();
			//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改新闻出现异常");
		}
	}
	
	
	//查找接口，用于客户端
		@RequestMapping(value = "/news/search_size", method = { RequestMethod.POST, RequestMethod.GET })
		@ResponseBody
		public ResponseObject<PageSplit<News>> searchpagetbyanyonesize(HttpServletRequest request,
		        @RequestParam(value = "key", required = false, defaultValue = "") String key,
		        @RequestParam(value = "column", required = false, defaultValue = "") String column,
		        @RequestParam(value = "size", required = false, defaultValue = "") String size,
		        @RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex) {
			try {
				if(StringUtil.isEmpty(size))
				{
					size=Integer.toString(defaultPageSize);
				}
				
				 key = URLDecoder.decode(key, "UTF-8");
				pageIndex = Math.max(pageIndex, 1);
				return this.newsService.getNewsByKey(key, column,Integer.parseInt(size), pageIndex);
			} catch (Exception e) {
				log.error("获取新闻列表失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取新闻列表失败！");
			}
		}	
		
		
		@RequestMapping(value = "/news/getbyid", method = { RequestMethod.POST, RequestMethod.GET })
		@ResponseBody
		public ResponseObject<News> getbyid_guest(@RequestParam(value = ParameterConstants.NEWS_ID) String id) {
			if (!NewsUtil.validateId(id)) {
				return generateResponseObject(ResponseCode.NEWS_ID_ERROR, "参数无效");
			}

			try {
				return this.newsService.getNewsById(id);
			} catch (Exception e) {
				log.error("根据id获取新闻对象失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取新闻失败，出现异常!");
			}
		}
		
		//查找接口，用于客户端
		@RequestMapping(value = "/news/search_byguest", method = { RequestMethod.POST, RequestMethod.GET })
		@ResponseBody
		public ResponseObject<PageSplit<News>> searchpagetbyanyonesize(HttpServletRequest request,
		        @RequestParam(value = "content", required = false, defaultValue = "") String content,
		        @RequestParam(value = "size", required = false, defaultValue = "") String size,
		        @RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex) {
			try {
				if(StringUtil.isEmpty(size))
				{
					size=Integer.toString(defaultPageSize);
				}
				
				content = URLDecoder.decode(content, "UTF-8");
				pageIndex = Math.max(pageIndex, 1);
				return this.newsService.getNewsByKey(content, "",Integer.parseInt(size), pageIndex);
			} catch (Exception e) {
				log.error("获取新闻列表失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取新闻列表失败！");
			}
		}	
}

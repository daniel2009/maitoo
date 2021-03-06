package com.meitao.controller;

import java.io.File;
import java.net.URLDecoder;
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

import com.meitao.service.GlobalArgsService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ParameterConstants;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.model.ResponseObject;
import com.meitao.model.ResponseString;
import com.meitao.model.globalargsExport;
import com.meitao.model.globalargsIndex;
import com.meitao.model.globalargs;


@Controller
public class GlobalArgsController extends BasicController {

	private static final long serialVersionUID = 6582185005680513923L;
	private static final Logger log = Logger.getLogger(GlobalArgsController.class);
	@Resource(name = "globalargsService")
	private GlobalArgsService globalargsService;
	@Value(value = "${page_size}")
	private int defaultPageSize;	
	@Value(value = "${default_img_type}")
	private String defaultCardFileType;
	
	@Value(value = "${sava_global_pic_dir}")
	private String saveGlobalDir;
	@Value(value = "${default_img_size}")
	private long defaultCardFileSize;
	/*
	 * 获取全局变量中所有清单
	 * 创建：kai 20151012
	 * */
	@RequestMapping(value = "/admin/globalargs/all", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<List<globalargs>> getallArgsbyAdmin() {
		try {
			 //key = URLDecoder.decode(key, "UTF-8");
			ResponseObject<List<globalargs>> list=this.globalargsService.getAll();
			return list;
		} catch (Exception e) {
			log.error("获取全局参数列表失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取全局参数列表失败！");
		}
	}	
	
	/*
	 * 提交文本类型的插入参数
	 * 创建：kai 20151012
	 * */
	@RequestMapping(value = "/admin/globalargs/textadd", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> addtextglobalargs(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.GLOBALARGS_FLAG) String flag,
	        @RequestParam(value = ParameterConstants.GLOBALARGS_CONTENT) String content,
	        @RequestParam(value = ParameterConstants.GLOBALARGS_REMARK) String remark,
	        @RequestParam(value = ParameterConstants.GLOBALARGS_INDEX) String index
	        ) {
		String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
		{
			return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权添加全局变量!");
			
		}
		
		globalargs args=new globalargs();

		args.setArgtype(Constant.GLOBALARGS_TYPE_TEXT);
		
		String date = DateUtil.date2String(new Date());// 修改时间
		args.setCreatetime(date);
		args.setModifytime(date);
		if(StringUtil.isEmpty(index))
		{
			index="0";
		}
		args.setIndex(index);
			
		
		try {
			flag = URLDecoder.decode(flag, "UTF-8");
			content = URLDecoder.decode(content, "UTF-8");
			remark = URLDecoder.decode(remark, "UTF-8");
			
			args.setArgcontent(content);
			args.setArgflag(flag);
			args.setArgremark(remark);
			
			return this.globalargsService.saveglobalargs(args);
			
		} catch (Exception e) {
			log.error("添加新闻失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "添加新闻出现异常");
		}
	}




@RequestMapping(value = "/admin/globalargs/picadd", method = { RequestMethod.POST })
@ResponseBody
public ResponseObject<Map<String, String>> addglobalargshavepic(
		HttpServletRequest request,
		@RequestParam(value = "argflag", required = false, defaultValue = "") String flag,
		@RequestParam(value = "a_remark", required = false, defaultValue = "") String remark,
		@RequestParam(value = "selectindex", required = false, defaultValue = "") String index1,
		@RequestParam(value = ParameterConstants.CONSIGNEE_CARD_FILE, required = false) MultipartFile file) {
	
	String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
	if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
	{
		return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权添加全局变量!");
		
	}
	
	// 处理提交上来的图片
	// 解决火狐的反斜杠问题 kai 20151006
	String filetype = this.defaultCardFileType;// 要上传的文件类型
	String strtest = this.saveGlobalDir;
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
		fileName = this.saveGlobalDir + strseparator + "G" + "_"
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
	
	
	globalargs args=new globalargs();

	args.setArgtype(Constant.GLOBALARGS_TYPE_PIC);		
	String date = DateUtil.date2String(new Date());// 修改时间
	args.setCreatetime(date);
	args.setModifytime(date);
	
	if(StringUtil.isEmpty(index1))
	{
		index1="0";
	}
	args.setIndex(index1);
	try {
		args.setArgcontent(fileName);
		args.setArgflag(flag);
		args.setArgremark(remark);
		ResponseObject<Object> obj= this.globalargsService.saveglobalargs(args);
		if(obj.getCode().equalsIgnoreCase(ResponseCode.SUCCESS_CODE))
		{
			return new ResponseObject<Map<String, String>>(
					ResponseCode.SUCCESS_CODE);
		}
		else
		{
			return new ResponseObject<Map<String, String>>(
					ResponseCode.PARAMETER_ERROR, "添加失败");
		}
		
	} catch (Exception e) {
		log.error("提交运单失败", e);
		return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "提交运单失败");
	}
}
	
/*
 * 获取全局变量中所有清单
 * 创建：kai 20151012
 * */
@RequestMapping(value = "/admin/globalargs/getone", method = { RequestMethod.POST, RequestMethod.GET })
@ResponseBody
public ResponseObject<globalargs> getoneArgsbyAdmin(HttpServletRequest request,
		@RequestParam(value = "id") String id
		) {
	try {
		if(id==""||id==null||id.equalsIgnoreCase(""))
		{
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取全局参数列表失败！");
		}
		return this.globalargsService.getonebyid(id);
		
	
	} catch (Exception e) {
		log.error("获取全局参数列表失败", e);
		return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取全局参数列表失败！");
	}
}	


/*
 * 修改文本类型
 * 创建：kai 20151013
 * */
@RequestMapping(value = "/admin/globalargs/modifytext", method = { RequestMethod.POST, RequestMethod.GET })
@ResponseBody
public ResponseObject<Object> modifytextglobalargs(HttpServletRequest request,
		@RequestParam(value = "id") String id,
        @RequestParam(value = ParameterConstants.GLOBALARGS_CONTENT) String content,
        @RequestParam(value = ParameterConstants.GLOBALARGS_REMARK) String remark,
        @RequestParam(value = ParameterConstants.GLOBALARGS_FLAG) String flag
        ) {
	
	String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
	if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
	{
		return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权修改全局变量!");
		
	}

	if(id==""||id==null||id.equalsIgnoreCase(""))
	{
		return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取全局参数列表失败！");
	}
	if(flag==""||flag==null||flag.equalsIgnoreCase(""))
	{
		return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取全局参数列表失败！");
	}
	if(content==""||content==null||content.equalsIgnoreCase(""))
	{
		return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "变量内容不能为空！");
	}
	if(remark==""||remark==null||remark.equalsIgnoreCase(""))
	{
		return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "备注不能为空！");
	}
	globalargs args=new globalargs();
	args.setId(id);
	String date = DateUtil.date2String(new Date());// 修改时间
	args.setModifytime(date);
	
	try {	
		content = URLDecoder.decode(content, "UTF-8");
		remark = URLDecoder.decode(remark, "UTF-8");
		flag= URLDecoder.decode(flag, "UTF-8");
		args.setArgremark(remark);
		args.setArgcontent(content);
		args.setArgflag(flag);
		
		return this.globalargsService.modifyglobalargs(args);
		
		
	} catch (Exception e) {
		log.error("添加新闻失败", e);
		return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "添加新闻出现异常");
	}
}

/*
 * 修改文本类型
 * 创建：kai 20160422
 * */
@RequestMapping(value = "/admin/globalargs/modifytext_admin", method = { RequestMethod.POST, RequestMethod.GET })
@ResponseBody
public ResponseObject<Object> modifytextglobalargsbyadmin(HttpServletRequest request,
		@RequestParam(value = "id") String id,
        @RequestParam(value = ParameterConstants.GLOBALARGS_CONTENT) String content,
        @RequestParam(value = ParameterConstants.GLOBALARGS_FLAG) String flag
        ) {
	
	String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
	if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
	{
		return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权修改全局变量!");
		
	}

	if(id==""||id==null||id.equalsIgnoreCase(""))
	{
		return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取全局参数列表失败！");
	}
	if(flag==""||flag==null||flag.equalsIgnoreCase(""))
	{
		return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取全局参数列表失败！");
	}
	if(content==""||content==null||content.equalsIgnoreCase(""))
	{
		return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "变量内容不能为空！");
	}

	globalargs args=new globalargs();
	args.setId(id);
	String date = DateUtil.date2String(new Date());// 修改时间
	args.setModifytime(date);
	
	try {	
		

		args.setArgcontent(content);
		args.setArgflag(flag);
		
		return this.globalargsService.modifyglobalargs1(args);
		
		
	} catch (Exception e) {
		
		return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改发现异常");
	}
}




/*
 * 修改包含图片的全局变量
 * kai 20151013
 * */

@RequestMapping(value = "/admin/globalargs/modifypic", method = { RequestMethod.POST })
@ResponseBody
public ResponseObject<Map<String, String>> modifyglobalhavepic(
		HttpServletRequest request,
		@RequestParam(value = "gid", required = false, defaultValue = "") String id,
		@RequestParam(value = "gflag", required = false, defaultValue = "") String flag,
		@RequestParam(value = "argremark", required = false, defaultValue = "") String remark,
		@RequestParam(value = ParameterConstants.CONSIGNEE_CARD_FILE, required = false) MultipartFile file) {
	
	String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
	if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
	{
		return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权修改全局变量!");
		
	}
	// 处理提交上来的图片
	// 解决火狐的反斜杠问题 kai 20151006
	String filetype = this.defaultCardFileType;// 要上传的文件类型
	String strtest = this.saveGlobalDir;
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
		fileName = this.saveGlobalDir + strseparator + flag + "_"
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
	
	
	globalargs args=new globalargs();

	args.setArgtype(Constant.GLOBALARGS_TYPE_PIC);		
	String date = DateUtil.date2String(new Date());// 修改时间
	args.setModifytime(date);
	try {
		args.setArgcontent(fileName);
		args.setArgflag(flag);
		args.setArgremark(remark);
		args.setId(id);
		//ResponseObject<Object> obj= this.globalargsService.saveglobalargs(args);
		ResponseObject<Object> obj=this.globalargsService.modifyglobalargs(args);
		if(obj.getCode().equalsIgnoreCase(ResponseCode.SUCCESS_CODE))
		{
			return new ResponseObject<Map<String, String>>(
					ResponseCode.SUCCESS_CODE);
		}
		else
		{
			return new ResponseObject<Map<String, String>>(
					ResponseCode.PARAMETER_ERROR, "添加失败");
		}
		
	} catch (Exception e) {
		log.error("提交运单失败", e);
		return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "提交运单失败");
	}
}
	


/*
 * 修改包含图片的全局变量
 * kai 20151013
 * */

@RequestMapping(value = "/admin/globalargs/modifypic_admin",produces="text/plain;charset=UTF-8", method = { RequestMethod.POST })
@ResponseBody
public String modifyglobalhavepicbyadmin(
		HttpServletRequest request,
		@RequestParam(value = "id", required = false, defaultValue = "") String id,
		@RequestParam(value = "flag", required = false, defaultValue = "") String flag,
		@RequestParam(value = ParameterConstants.CONSIGNEE_CARD_FILE, required = false) MultipartFile file) {
	ResponseString responseString=new ResponseString();
	String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
	if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
	{
		responseString.setCode(ResponseCode.EMPLOYEE_STORE_NAME_ERROR);
		responseString.setMessage("对不起，你无权修改全局变量!");
		return responseString.toString();
		//return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权修改全局变量!");
		
	}
	// 处理提交上来的图片
	// 解决火狐的反斜杠问题 kai 20151006
	String filetype = this.defaultCardFileType;// 要上传的文件类型
	String strtest = this.saveGlobalDir;
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
			responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
			responseString.setMessage("图像文件过大,请重新尝试!");
			return responseString.toString();
			//return generateResponseObject(
			//		ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
		}

		String originalName = file.getOriginalFilename();

		if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
			
			responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
			responseString.setMessage("上传图像文件格式不对,请重新尝试!");
			return responseString.toString();
			//return generateResponseObject(
			//		ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
		}

		int index = originalName.lastIndexOf('.');
		index = Math.max(index, 0);
		fileName = this.saveGlobalDir + strseparator + flag + "_"
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
			
			responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
			responseString.setMessage("保存用户图像失败，请去除上传图像后再尝试!");
			return responseString.toString();
			//return generateResponseObject(
			//		ResponseCode.CONSIGNEE_CARD_ERROR,
			//		"保存用户图像失败，请去除上传图像后再尝试!");
		}
	}
	
	
	globalargs args=new globalargs();

	args.setArgtype(Constant.GLOBALARGS_TYPE_PIC);		
	String date = DateUtil.date2String(new Date());// 修改时间
	args.setModifytime(date);
	try {
		args.setArgcontent(fileName);
		args.setArgflag(flag);
		args.setId(id);
		//ResponseObject<Object> obj= this.globalargsService.saveglobalargs(args);
		ResponseObject<Object> obj=this.globalargsService.modifyglobalargs1(args);
		if(obj.getCode().equalsIgnoreCase(ResponseCode.SUCCESS_CODE))
		{
			responseString.setCode(ResponseCode.SUCCESS_CODE);
			responseString.setMessage(obj.getMessage());
			return responseString.toString();
			//return new ResponseObject<Map<String, String>>(
			//		ResponseCode.SUCCESS_CODE);
		}
		else
		{
			
				responseString.setCode(obj.getCode());
				responseString.setMessage(obj.getMessage());
				return responseString.toString();
			
			
			//return new ResponseObject<Map<String, String>>(
			//		ResponseCode.PARAMETER_ERROR, "添加失败");
		}
		
	} catch (Exception e) {
		log.error("修改失败", e);
		responseString.setCode(ResponseCode.SHOW_EXCEPTION);
		responseString.setMessage("添加失败");
		return responseString.toString();
		//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改全局变量失败");
	}
}

/*
 * 修改文本类型
 * 创建：kai 20151013
 * */
@RequestMapping(value = "/admin/globalargs/delone", method = { RequestMethod.POST, RequestMethod.GET })
@ResponseBody
public ResponseObject<Object> deleteoneglobalargs(HttpServletRequest request,
		@RequestParam(value = "id") String id
        ) {

	String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
	if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
	{
		return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权删除全局变量!");
		
	}
	if(id==""||id==null||id.equalsIgnoreCase(""))
	{
		return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取全局参数列表失败！");
	}

	
	try {	
		
		
	
		return this.globalargsService.deleteoneglobalargs(id);
		
	} catch (Exception e) {
		log.error("添加新闻失败", e);
		return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "添加新闻出现异常");
	}
}
/*
 * 获取全局变量中所有清单
 * 创建：kai 20151012
 * */
@RequestMapping(value = "/globalargs/getcontent", method = { RequestMethod.POST, RequestMethod.GET })
@ResponseBody
public ResponseObject<String> getcontentbyuser(HttpServletRequest request,
		@RequestParam(value = ParameterConstants.GLOBALARGS_FLAG) String flag
		) {
	try {
		if(flag==""||flag==null||flag.equalsIgnoreCase(""))
		{
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取全局参数列表失败！");
		}
		//return this.globalargsService.getonebyid(id);
		return this.globalargsService.getcontentbyuser(flag);
		
	
	} catch (Exception e) {
		log.error("获取全局参数失败", e);
		return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取全局参数失败！");
	}
}	


/*
 * 获取全局变量中所有清单
 * 创建：kai 20151012
 * 参数flags:使用“,”分隔的字符串，上传后自动会分为数组
 * */
@RequestMapping(value = "/admin/globalargs/getcontents", method = { RequestMethod.POST, RequestMethod.GET })
@ResponseBody
public ResponseObject<List<String>> getcontentsbyadmin(HttpServletRequest request,
		@RequestParam(value = "flags") String[] flags
		) {
	try {
		if((flags==null)||(flags.length<1))
		{
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取参数失败！");
		}
		return this.globalargsService.getcontenstbyuser(flags);
	
	} catch (Exception e) {
		log.error("获取全局参数失败", e);
		return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取全局参数失败！");
	}
}	

@RequestMapping(value = "/user/globalargs/getcontents", method = { RequestMethod.POST, RequestMethod.GET })
@ResponseBody
public ResponseObject<List<String>> getcontentsbyuser000(HttpServletRequest request,
		@RequestParam(value = "flags") String[] flags
		) {
	try {
		if((flags==null)||(flags.length<1))
		{
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取参数失败！");
		}
		return this.globalargsService.getcontenstbyuser(flags);
	
	} catch (Exception e) {
		log.error("获取全局参数失败", e);
		return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取全局参数失败！");
	}
}
	/*
	 * 获取全局变量中所有清单，用户端
	 * 创建：kai 20151027
	 * 参数flags:使用“,”分隔的字符串，上传后自动会分为数组
	 * */
	@RequestMapping(value = "/globalargs/getcontents", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseObject<List<String>> getcontentsbyuser(HttpServletRequest request,
			@RequestParam(value = "flags") String[] flags
			) {
		try {
			if((flags==null)||(flags.length<1))
			{
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取参数失败！");
			}
			return this.globalargsService.getcontenstbyuser(flags);
		
		} catch (Exception e) {
			log.error("获取全局参数失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取全局参数失败！");
		}
	}	
	@RequestMapping(value = "/user/globalArgs/getUseIdCard", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject<String> getUseIdCard(HttpServletRequest request) {
		try {
			return this.globalargsService.getByFlag(Constant.GLOBALARGS_FLAG_USE_IDCARD);
		} catch (Exception e) {
			log.error("获取全局参数失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取全局参数失败！");
		}
	}
	@RequestMapping(value = "/user/globalArgs/getWeightRoundUp", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject<String> getWeightRoundUp(HttpServletRequest request) {
		try {
			return this.globalargsService.getByFlag(Constant.GLOBALARGS_FLAG_WEIGHT_ROUND_UP);
		} catch (Exception e) {
			log.error("获取全局参数失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取全局参数失败！");
		}
	}
	@RequestMapping(value = "/globalArgs/getReturnPackageReturnFee", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject<String> getReturnPackageReturnFee(HttpServletRequest request) {
		try {
			return this.globalargsService.getByFlag(Constant.GLOBALARGS_FLAG_RETURN_PACKAGE_RETURN_FEE);
		} catch (Exception e) {
			log.error("获取全局参数失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取全局参数失败！");
		}
	}
	
	@RequestMapping(value = "/globalArgs/getallbyindexs", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject<List<globalargsExport>> getallbyindexs(HttpServletRequest request) {
		try {
			return this.globalargsService.getargsbyindexs();
		} catch (Exception e) {
			log.error("获取全局参数失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取全局参数失败！");
		}
	}
	
	
	@RequestMapping(value = "/admin/globalArgs/getallbyindex", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject<List<globalargs>> getallbyindex(HttpServletRequest request,
			@RequestParam(value = "index") String index) {
		try {
			
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
			{
			}
			else
			{
				return new ResponseObject<List<globalargs>>(ResponseCode.PARAMETER_ERROR,"对不起，你无权查看此参数信息!");
			}
			
			
			if(StringUtil.isEmpty(index))
			{
				return new ResponseObject<List<globalargs>>(ResponseCode.PARAMETER_ERROR,"参数不能为空");
			}
			
			return this.globalargsService.getargsbyindex(index);
		} catch (Exception e) {
			log.error("获取全局参数失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取全局参数失败！");
		}
	}
	
	
	
	//获取分类索引
	@RequestMapping(value = "/globalArgs/get_indexs", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject<List<globalargsIndex>> getglobalindexs(HttpServletRequest request) {
		try {
			return this.globalargsService.getindex();
		} catch (Exception e) {
			log.error("获取全局参数失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取全局参数失败！");
		}
	}
}


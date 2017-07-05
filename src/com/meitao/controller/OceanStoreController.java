package com.meitao.controller;


import java.util.List;

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

import com.meitao.service.OceanStoreService;
import com.meitao.common.constants.ParameterConstants;
import com.meitao.common.constants.ResponseCode;

import com.meitao.common.util.UploadUtil;
import com.meitao.model.OceanStore;
import com.meitao.model.ResponseObject;

@Controller
public class OceanStoreController extends BasicController{
	private static final long serialVersionUID = -5596383708126507855L;
	private static final Logger log = Logger.getLogger(OceanStoreController.class);
	@Resource(name="oceanStoreService")
	private OceanStoreService oceanStoreSerivce;
	@Value(value = "${default_img_type}")
	private String defaultImgType;
	@Value(value = "${default_img_size}")
	private long defaultImgSize;
	private static final String IMG_PACKAGE = "/img/oceanStore/";
	private String uploadFile(MultipartFile file, String realPath){
		return UploadUtil.uploadFile(file, IMG_PACKAGE, realPath, defaultImgSize, defaultImgType, true, 250);
	}
	@RequestMapping(value="/admin/oceanStore/add", method={RequestMethod.POST})
	@ResponseBody
	public ResponseObject<Object> addOceanStore(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.OCEAN_STORE_NAME) String name,
			@RequestParam(value = ParameterConstants.OCEAN_STORE_PICTURE, required = false) MultipartFile picture,
			@RequestParam(value = ParameterConstants.OCEAN_STORE_NOTE, required = false) String note,
			@RequestParam(value = ParameterConstants.OCEAN_STORE_URL, required = false) String url){
		String fileName = null;
		int dotIndex = -1;
		fileName = this.uploadFile(picture, request.getSession().getServletContext().getRealPath("/"));
		dotIndex = fileName.indexOf(".");
		if(dotIndex < 0){
			return generateResponseObject(ResponseCode.OCEAN_STORE_PICTURE_ERROR, fileName);
		}
		OceanStore oceanStore = new OceanStore();
		oceanStore.setName(name);
		oceanStore.setImgUrl(fileName);
		oceanStore.setNote(note);
		oceanStore.setUrl(url);
		try{
			return this.oceanStoreSerivce.add(oceanStore);
		}catch(Exception e){
			log.error("save oceanStore to database fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "海淘推荐添加失败，请重试或联系客服");
		}
	}
	
	@RequestMapping(value="/admin/oceanStore/modify", method={RequestMethod.POST})
	@ResponseBody
	public ResponseObject<Object> modifyOceanStore(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.OCEAN_STORE_ID) String id,
			@RequestParam(value = ParameterConstants.OCEAN_STORE_NAME) String name,
			@RequestParam(value = ParameterConstants.OCEAN_STORE_NOTE, required = false) String note,
			@RequestParam(value = ParameterConstants.OCEAN_STORE_IMGURL, required = false) String imgUrl,
			@RequestParam(value = ParameterConstants.OCEAN_STORE_PICTURE, required = false) MultipartFile picture,
			@RequestParam(value = ParameterConstants.OCEAN_STORE_URL, required = false) String url){
		String fileName = null;
		int dotIndex = -1;
		if(picture!=null)
		{
			fileName = this.uploadFile(picture, request.getSession().getServletContext().getRealPath("/"));
			dotIndex = fileName.indexOf(".");
			if(dotIndex < 0){
				return generateResponseObject(ResponseCode.OCEAN_STORE_PICTURE_ERROR, fileName);
			}else{
				imgUrl = fileName;
			}
		}
		OceanStore oceanStore = new OceanStore();
		oceanStore.setId(id);
		oceanStore.setNote(note);
		oceanStore.setName(name);
		oceanStore.setImgUrl(imgUrl);
		oceanStore.setUrl(url);
		try{
			return this.oceanStoreSerivce.modify(oceanStore);
		}catch(Exception e){
			log.error("modify oceanStore fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "海淘推荐更新失败，请重试或联系客服");
		}
	}
	@RequestMapping(value="/admin/oceanStore/delete", method={RequestMethod.POST})
	@ResponseBody
	public ResponseObject<Object> deleteOceanStore(@RequestParam(value="id") String id){
		try{
			return this.oceanStoreSerivce.delete(id);
		}catch(Exception e){
			log.error("fail to delete oceanStore", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "删除海淘推荐失败");
		}
	}
	@RequestMapping(value="/admin/oceanStore/getById", method={RequestMethod.POST})
	@ResponseBody
	public ResponseObject<OceanStore> getOceanStoreById(@RequestParam(value="id") String id){
		try{
			return this.oceanStoreSerivce.getById(id);
		}catch(Exception e){
			log.error("fail to get oceanStore by id");
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "无法获取到指定oceanStore");
		}
	}
	@RequestMapping(value={"/admin/oceanStore/all", "/oceanStore/all"}, method={RequestMethod.GET})
	@ResponseBody
	public ResponseObject<List<OceanStore>> getAllOceanStore(){
		try{
			return this.oceanStoreSerivce.getAll();
		}catch(Exception e){
			log.error("fail to get all data of oceanStore");
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取海淘推荐列表失败");
		}
	}
}

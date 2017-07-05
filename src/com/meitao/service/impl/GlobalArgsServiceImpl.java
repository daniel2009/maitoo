package com.meitao.service.impl;

import java.util.ArrayList;

import java.util.List;


















import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.meitao.dao.globalargsDao;
import com.meitao.service.GlobalArgsService;

import com.meitao.common.constants.ResponseCode;

import com.meitao.common.util.ExceptionUtil;

import com.meitao.common.util.StringUtil;
import com.meitao.exception.ServiceException;

import com.meitao.model.ResponseObject;

import com.meitao.model.globalargsExport;
import com.meitao.model.globalargsIndex;
import com.meitao.model.globalargs;

@Service("globalargsService")
public class GlobalArgsServiceImpl extends BasicService implements GlobalArgsService {
	@Autowired
	private globalargsDao globalargsDao;
	
	/*
	 * 获取所有全局变量信息
	 * */
	public ResponseObject<List<globalargs>> getAll() throws ServiceException {
		try {
			List<globalargs> list = this.globalargsDao.searchallofglobalargs();
			ResponseObject<List<globalargs>> responseObj = new ResponseObject<List<globalargs>>(ResponseCode.SUCCESS_CODE);
			if (list != null && !list.isEmpty()) {
				responseObj.setData(list);
			} else {
				responseObj.setData(new ArrayList<globalargs>());
				responseObj.setMessage("没有全局参数信息");
			}
			return responseObj;
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	/*
	 * 保存全局变量参数
	 * */
	public ResponseObject<Object> saveglobalargs(globalargs args) throws ServiceException {
		if (args == null) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR, "参数无效");
		}
		try {
			//int iresult = this.globalargsDao.insertglobalargs(argflag, argcontent, argtype, argremark)
			int num=this.globalargsDao.countByargflag(args.getArgflag());
			if(num>0)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR, "插入失败,插入唯一标志已经存在！");
			}
			int iresult = this.globalargsDao.insertglobalargsbyobject(args);
			if (iresult > 0) {
				return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			} else {
				return new ResponseObject<Object>(ResponseCode.NEWS_INSERT_FAILURE, "插入变量失败");
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	//获取一条变量记录
	public ResponseObject<globalargs> getonebyid(String id) throws ServiceException
	{
		try {
			
			globalargs args = this.globalargsDao.getglobalargsById(id);
			if(args==null)
			{
				return new ResponseObject<globalargs>(ResponseCode.PARAMETER_ERROR, "获取失败");
			}
			ResponseObject<globalargs> responseObj = new ResponseObject<globalargs>(ResponseCode.SUCCESS_CODE);
			responseObj.setData(args);
			
			return responseObj;

		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	/*
	 * 修改记录
	 * */
	public ResponseObject<Object> modifyglobalargs(globalargs args) throws ServiceException
	{
		if (args == null) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR, "参数无效");
		}
		try {
			
			
			int iresult = this.globalargsDao.updateglobalargsbyflag(args);
			if (iresult > 0) {
				return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			} else {
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR, "插入变量失败");
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	
	
	/*
	 * 修改记录
	 * */
	public ResponseObject<Object> modifyglobalargs1(globalargs args) throws ServiceException
	{
		if (args == null) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR, "参数无效");
		}
		try {
			
			
			int iresult = this.globalargsDao.updateglobalargsbyflag1(args);
			if (iresult > 0) {
				return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			} else {
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR, "插入变量失败");
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	
	public ResponseObject<Object> deleteoneglobalargs(String id) throws ServiceException
	{
		if ((id == null)||(id.equalsIgnoreCase(""))) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR, "参数无效");
		}
		try {
			
			int iresult =this.globalargsDao.deleteglobalargsbyadminid(id);
			
			if (iresult > 0) {
				return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			} else {
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR, "删除变量失败");
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	
	public ResponseObject<String> getcontentbyuser(String flag) throws ServiceException
	{
		if ((flag == null)||(flag.equalsIgnoreCase(""))) {
			return new ResponseObject<String>(ResponseCode.PARAMETER_ERROR, "参数无效");
		}
		try {
			
			
			String content=this.globalargsDao.getcontentbyflag(flag);
			
			ResponseObject<String> responseObj = new ResponseObject<String>(ResponseCode.SUCCESS_CODE);
			responseObj.setData(content);
			return responseObj;
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	
	public ResponseObject<List<String>> getcontenstbyuser(String[] flags) throws ServiceException
	{
		if ((flags == null)||(flags.length<1)) {
			return new ResponseObject<List<String>>(ResponseCode.PARAMETER_ERROR, "参数无效");
		}
		try {
			List<String> flag = new ArrayList<String>();
			for(int i=0;i<flags.length;i++)
			{
				
				String content=this.globalargsDao.getcontentbyflag(flags[i]);
				if((content==null)||content.equalsIgnoreCase("null"))
				{
					content="";
				}
				
				flag.add(content);
			}
			
			
			ResponseObject<List<String>> responseObj = new ResponseObject<List<String>>(ResponseCode.SUCCESS_CODE);			
			responseObj.setData(flag);
			return responseObj;
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	@Override
	public ResponseObject<String> getByFlag(String flag)
			throws ServiceException {
		ResponseObject<String> responseObject = new ResponseObject<String>();
		if (StringUtil.isEmpty(flag)) {
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数无效");
		}
		try {
			String content = this.globalargsDao.getcontentbyflag(flag);
			if(StringUtil.isEmpty(content)){
				responseObject.setCode(ResponseCode.GLOBALARGS_FLAG_NOT_EXISTS);
				responseObject.setMessage("全局参数无效");
			}else{
				responseObject.setCode(ResponseCode.SUCCESS_CODE);
				responseObject.setData(content);				
			}
			
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
		return responseObject;
	}
	
	
	//根据分类来获取获取全局变量globalargsExport
	public ResponseObject<List<globalargsExport>> getargsbyindexs() throws ServiceException {
		
		List<globalargsExport> args_result = new  ArrayList<globalargsExport>();//获取全局变量
		try {
			List<globalargsIndex> indexs=this.globalargsDao.getallindexs();
			for(globalargsIndex arg:indexs)
			{
				globalargsExport export=new globalargsExport();
				export.setId(arg.getId());
				export.setName(arg.getName());
				export.setArgs(this.globalargsDao.getallbyindex(arg.getId()));
				args_result.add(export);
			}
			List<globalargs> args=this.globalargsDao.getallbyindex("0");//获取没有分类的情况下的全局变量
			if((args!=null)&&(args.size()>0))
			{
				globalargsExport export=new globalargsExport();
				export.setId("0");
				export.setName("");
				export.setArgs(args);
				args_result.add(export);
			}
			
			ResponseObject<List<globalargsExport>> result=null;
			//没有获取到数据，失败
			if((indexs==null)||(args_result==null)||(args_result.size()<1))
			{
				result= new ResponseObject<List<globalargsExport>>(
						ResponseCode.PARAMETER_ERROR);
			}
			else
			{
			    result = new ResponseObject<List<globalargsExport>>(
							ResponseCode.SUCCESS_CODE);
			    result.setData(args_result);
			}
			
			return result;
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	
	public ResponseObject<List<globalargsIndex>> getindex() throws ServiceException
	{
		//List<globalargsIndex> args_result = new  ArrayList<globalargsIndex>();//获取全局变量
		try {
			List<globalargsIndex> indexs=this.globalargsDao.getallindexs();
			
			
			ResponseObject<List<globalargsIndex>> result=null;
			//没有获取到数据，失败
			if((indexs==null)||(indexs.size()<1))
			{
				result= new ResponseObject<List<globalargsIndex>>(
						ResponseCode.PARAMETER_ERROR);
			}
			else
			{
			    result = new ResponseObject<List<globalargsIndex>>(
							ResponseCode.SUCCESS_CODE);
			    result.setData(indexs);
			}
			
			return result;
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	
	
	
	//根据分类来获取获取全局变量globalargsExport
		public ResponseObject<List<globalargs>> getargsbyindex(String index) throws ServiceException {
			
		
			try {
				
				
				List<globalargs> args=this.globalargsDao.getallbyindex(index);//获取没有分类的情况下的全局变量
				
				
				ResponseObject<List<globalargs>> obj=new ResponseObject<List<globalargs>>(ResponseCode.SUCCESS_CODE);
				obj.setData(args);
				
				return obj;
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		
}

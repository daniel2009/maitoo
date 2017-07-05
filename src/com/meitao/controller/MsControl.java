package com.meitao.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.meitao.common.constants.Constant;

import com.meitao.common.constants.ResponseCode;

import com.meitao.common.util.DateUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.dao.MenshidadanControlDao;

import com.meitao.model.MenshidadanControl;

import com.meitao.model.ResponseObject;

@Controller
public class MsControl extends BasicController {

	private static final long serialVersionUID = 14556320235667055L;
	private static final Logger log = Logger.getLogger(MsControl.class);


	
	@Autowired
	private MenshidadanControlDao menshidadanControlDao;
	
	@RequestMapping(value = "/admin/ms_control/getself", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> msgetsel(HttpServletRequest request) {
		
		String empid = (String)request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY);
		String storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
		
		
		try {
			MenshidadanControl ms= this.menshidadanControlDao.findself(storeid, empid);
			ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			obj.setData(ms);
			return obj;
		} catch (Exception e) {
			log.error("获取打印信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取打印信息失败");
		}
	}
	
	@RequestMapping(value = "/admin/ms_control/update", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> getAllCallOrder(HttpServletRequest request,
			@RequestParam(value = "items", required = false, defaultValue = "") String items,
			@RequestParam(value = "pitem", required = false, defaultValue = "") String pitem){
		
		
		try{
			String itema[]=null;
			if(!StringUtil.isEmpty(items))
			{
				itema=items.split(","); 
			}
			else
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"必须选择一个显示项");
			}
			String strlen="";
			if(!StringUtil.isEmpty(pitem))
			{
				boolean flag=false;
				if((itema!=null)&&(itema.length>0))
				{
					for(int i=0;i<itema.length;i++)
					{
						if((itema[i]!=null)&&(itema[i].equalsIgnoreCase(pitem)))
						{
							flag=true;
							
						}
						if(strlen.equalsIgnoreCase(""))
						{
							strlen=itema[i];
						}
						else
						{
							strlen=strlen+";"+itema[i];
						}
					}
					
				}
				else
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"必须选择一个显示项");
				}
				if(flag==false)
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"打印选项必须包含在显示项内!");
				}
			}
			
			
			String empid = (String)request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY);
			String storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
			
			
			try {
				MenshidadanControl ms= this.menshidadanControlDao.findself(storeid, empid);
				
				String date = DateUtil.date2String(new Date());
				if(ms==null)//原来没有，插入新的选项
				{
					MenshidadanControl control=new MenshidadanControl();
					control.setCreateDate(date);
					control.setModifyDate(date);
					control.setEmployeeId(empid);
					control.setStoreId(storeid);
					control.setKeydownItem(pitem);
					control.setPrintItems(strlen);;
					int k=this.menshidadanControlDao.insert(control);
					if(k<1)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"插入数据库失败!");
					}
				}
				else
				{
					//原来有，修改原来的选项
					int k=this.menshidadanControlDao.update(storeid, empid, date, strlen, pitem);
					
					if(k<1)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"修改数据库失败!");
					}
				}
				ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
				
				return obj;
			} catch (Exception e) {
				
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "出现异常！");
			}
		}catch(Exception e){
			
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "出现异常!");
		}
	}
	
	
}

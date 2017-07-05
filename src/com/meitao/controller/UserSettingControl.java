//用户默认设置接口
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


import com.meitao.dao.UserSettingDao;
import com.meitao.model.UserSetting;
import com.meitao.model.ResponseObject;

@Controller
public class UserSettingControl extends BasicController {

	private static final long serialVersionUID = 14556320235667055L;
	private static final Logger log = Logger.getLogger(UserSettingControl.class);


	
	@Autowired
	private UserSettingDao userSettingDao;
	
	@RequestMapping(value = "/user/user_setting/getself", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> usersettinggetsel(HttpServletRequest request) {
		
		String userId=(String)request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY);//用户id
		if(StringUtil.isEmpty(userId))//用户
		{
			return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作");
		}
		
		
		
		try {
			//MenshidadanControl ms= this.menshidadanControlDao.findself(storeid, empid);
			UserSetting set=this.userSettingDao.findself(userId);
			ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			obj.setData(set);
			return obj;
		} catch (Exception e) {
			log.error("获取用户默认设置失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取用户默认设置失败");
		}
	}
	//更新在线置单默认配置
	@RequestMapping(value = "/user/user_setting/update_online", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> setonlinesetting(HttpServletRequest request,
			@RequestParam(value = "storeId", required = false, defaultValue = "") String storeId,
			@RequestParam(value = "cid", required = false, defaultValue = "") String cid){
		
		
		try{
	
			
			
			String userId=(String)request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY);//用户id
			if(StringUtil.isEmpty(userId))//用户
			{
				return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作");
			}
			
			String date2 = DateUtil.date2String(new Date());
			try {
				
				int i= this.userSettingDao.updateonline(userId, storeId, cid,date2);
				
				if(i==0)//表示原来是空的，要生新插入
				{
					UserSetting set=new UserSetting();
					set.setUserId(userId);
					set.setZ_store(storeId);
					set.setZ_cid(cid);
					set.setS_store("-1");
					set.setCreateDate(date2);
					set.setModifyDate(date2);
					int k=this.userSettingDao.insert(set);
					if(k<1)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"插入数据失败!");
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
	
	
	//更新上门收货默认配置
	@RequestMapping(value = "/user/user_setting/update_sm", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> setswarg(HttpServletRequest request,
			@RequestParam(value = "storeId", required = false, defaultValue = "") String storeId){
		
		
		try{
	
			
			
			String userId=(String)request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY);//用户id
			if(StringUtil.isEmpty(userId))//用户
			{
				return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作");
			}
			
			String date2 = DateUtil.date2String(new Date());
			try {
				
				int i= this.userSettingDao.updateSm(userId, storeId,date2);//
					
				
				if(i==0)//表示原来是空的，要生新插入
				{
					UserSetting set=new UserSetting();
					set.setUserId(userId);
					set.setZ_store("-1");
					set.setZ_cid("-1");
					set.setS_store(storeId);
					set.setCreateDate(date2);
					set.setModifyDate(date2);
					int k=this.userSettingDao.insert(set);
					if(k<1)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"插入数据失败!");
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

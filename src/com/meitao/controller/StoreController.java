package com.meitao.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;








import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.meitao.service.MessageService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ParameterConstants;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.MessageUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.dao.ConsigneeInfoDao;
import com.meitao.dao.StoreControlDao;
import com.meitao.model.Message;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.StoreControl;

@Controller
public class StoreController extends BasicController {

	private static final long serialVersionUID = -8158552367757915055L;
	private static final Logger log = Logger.getLogger(StoreController.class);

	@Autowired
	private StoreControlDao storeControlDao;
	


	@RequestMapping(value = "/admin/store_control/getbyself", method = { RequestMethod.POST,RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> getbyself(
	        HttpServletRequest request,
	        @RequestParam(value = "flag") String flag) {
		if (!MessageUtil.validateContent(flag)) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "标志值不能为空!");
		}

		String storeId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
		if(StringUtil.isEmpty(storeId))
		{
			return generateResponseObject(ResponseCode.NEED_LOGIN, "请登陆后操作!");
		}
		try {
			ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"获取成功");
			
			StoreControl store=this.storeControlDao.findbyflag(storeId, flag);
			if(store==null)
			{
				return generateResponseObject(ResponseCode.PARAMETER_ERROR, "获取失败!");
			}
			obj.setData(store);
			return obj;
			
		} catch (Exception e) {
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取发生异常");
		}
	}

	@RequestMapping(value = "/admin/store_control/savebyflag", method = { RequestMethod.POST ,RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> savebyflag(
	        HttpServletRequest request,
	        @RequestParam(value = "flag") String flag,
	        @RequestParam(value = "value") String value) {
		if (!MessageUtil.validateContent(flag)) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "标志值不能为空!");
		}
		if (!MessageUtil.validateContent(value)) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "标志值的内容不能为空!");
		}

		String storeId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
		if(StringUtil.isEmpty(storeId))
		{
			return generateResponseObject(ResponseCode.NEED_LOGIN, "请登陆后操作!");
		}
		try {
			ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"获取成功");
			String date=DateUtil.date2String(new Date());
			int k=this.storeControlDao.update(storeId, flag, date, value);
			if(k==0)
			{
				StoreControl control=new StoreControl();
				control.setCreateDate(date);
				control.setModifyDate(date);
				control.setFlag(flag);
				control.setValue(value);
				control.setStoreId(storeId);
				//原来没有，要插入新的
				int kk=this.storeControlDao.insert(control);
				if(kk>0)
				{
					return obj;
				}
				return generateResponseObject(ResponseCode.PARAMETER_ERROR, "获取失败!"); 
			}
			return obj;
			
			
		} catch (Exception e) {
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改发生异常");
		}
	}

}

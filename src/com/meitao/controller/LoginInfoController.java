package com.meitao.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jxl.common.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.meitao.dao.LoginInfoDao;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ParameterConstants;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.StringUtil;
import com.meitao.model.LoginInfo;
import com.meitao.model.ResponseObject;

@Controller
public class LoginInfoController extends BasicController {
	private static final long serialVersionUID = -4432869353448470217L;
	private static final Logger log = Logger.getLogger(LoginInfoController.class);
	@Autowired
	private LoginInfoDao loginInfoDao;

	@RequestMapping(value = "/logininfo/get_self", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<LoginInfo> getSelfLoginInfo(HttpServletRequest request,
	        @RequestParam(value = "type", required = false, defaultValue = "1") String type) {
		ResponseObject<LoginInfo> infos = new ResponseObject<LoginInfo>(ResponseCode.SUCCESS_CODE);
		HttpSession session = request.getSession();
		String userId = StringUtil.obj2String(session.getAttribute(Constant.EMP_ID_SESSION_KEY));
		if ("0".equals(type)) {
			userId = StringUtil.obj2String(session.getAttribute(Constant.USER_ID_SESSION_KEY));
		} else if (!"1".equals(type)) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "type的值只能是0或者1,0表示会员,1表示管理员。");
		}

		try {
			infos.setData(this.loginInfoDao.getById(userId, type));
		} catch (Exception e) {
			log.error("获取登录信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取个人登录信息失败");
		}
		return infos;
	}

	@RequestMapping(value = "/logininfo/get_user", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<LoginInfo> getUserLastLoingInfo(@RequestParam(value = ParameterConstants.USER_ID) String id) {
		ResponseObject<LoginInfo> infos = new ResponseObject<LoginInfo>(ResponseCode.SUCCESS_CODE);
		String type = "0"; // 用户

		try {
			infos.setData(this.loginInfoDao.getById(id, type));
		} catch (Exception e) {
			log.error("获取登录信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取用户登录信息失败");
		}
		return infos;
	}
}

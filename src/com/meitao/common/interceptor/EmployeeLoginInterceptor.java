package com.meitao.common.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.StringUtil;

public class EmployeeLoginInterceptor extends HandlerInterceptorAdapter {
	private List<String> needLoginUrlPrefix;
	private List<String> notNeedLoginUrl;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		boolean needLoged = false;
		String URI = request.getRequestURI();
		String projectName = request.getContextPath();
		URI = URI.substring(projectName.length());
		String responseCode = null;
		String message = null;

		if (URI.indexOf(".") < 0) {
			for (String prefix : needLoginUrlPrefix) {
				if (URI.startsWith(prefix)) {
					needLoged = true;
					break;
				}
			}
		}
		//JPG;PNG;BMP;JPEG,访问身份证图片权限设置
		if ((URI.indexOf("cards_lib") >0)&&((URI.indexOf(".png") >0)||(URI.indexOf(".jpg") >0)||(URI.indexOf(".bmp") >0)||(URI.indexOf(".jpeg") >0))) {
			for (String prefix : needLoginUrlPrefix) {
				if (URI.startsWith(prefix)) {
					needLoged = true;
					break;
				}
			}
		}
		

		if (needLoged) {
			for (String url : notNeedLoginUrl) {
				if (URI.startsWith(url)) {
					needLoged = false;
				}
			}
		}

		HttpSession session = request.getSession();
		if (needLoged) {
			Object empId = session.getAttribute(Constant.EMP_ID_SESSION_KEY);
			if (empId == null || StringUtil.isEmpty(empId.toString())) {
				responseCode = ResponseCode.NEED_LOGIN;
				message = "请登录系统后再操作";
			}
		}

		if (responseCode != null) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", responseCode);
			jsonObject.put("message", message);
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(jsonObject.toString());
			return false;
		}

		return super.preHandle(request, response, handler);
	}

	public void setNeedLoginUrlPrefix(List<String> needLoginUrlPrefix) {
		this.needLoginUrlPrefix = needLoginUrlPrefix;
	}

	public void setNotNeedLoginUrl(List<String> notNeedLoginUrl) {
		this.notNeedLoginUrl = notNeedLoginUrl;
	}
}

package com.meitao.common.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.meitao.common.constants.ResponseCode;


public class AuthorityInterceptor extends HandlerInterceptorAdapter {
    private List<String> needLoginUrl;
    private List<String> adminNeedLoginUrl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String URI = request.getRequestURI();
        String projectName = request.getContextPath();
        URI = URI.substring(projectName.length());
        String responseCode = null;
        String message = null;

        Object obj = session.getAttribute("id");
        if (obj != null) {
            // 已经登录,判断登录用户是否有权限操作此url
            // 获取该登录的用户是客户还是管理员
            Object type = session.getAttribute("type");
            if ("0".equals(type)) {
                // 是客户
                boolean result = false;
                for (String url : adminNeedLoginUrl) {
                    if (URI.endsWith(url)) {
                        result = true;
                        break;
                    }
                }

                if (result) {
                    // 无权限操作
                    responseCode = ResponseCode.NEED_LOGIN;
                    message = "您没有权限操作！";
                }
            } else if ("1".equals(type)) {
                // 是管理员
                boolean result = false;
                for (String url : needLoginUrl) {
                    if (URI.endsWith(url)) {
                        result = true;
                        break;
                    }
                }

                if (result) {
                    // 无权限操作
                    responseCode = ResponseCode.NEED_LOGIN;
                    message = "您没有权限操作！";
                }
            } else {
                // 无权限操作
            	responseCode = ResponseCode.NEED_LOGIN;
                message = "您没有权限操作！";
            }
        }

        if (responseCode != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("CODE", responseCode);
            jsonObject.put("MESSAGE", message);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(jsonObject.toString());
            return false;
        }

        return super.preHandle(request, response, handler);
    }

    /**
     * Set the needLoginUrl value.
     * 
     * @param needLoginUrl
     *            the needLoginUrl to set
     */
    public void setNeedLoginUrl(List<String> needLoginUrl) {
        this.needLoginUrl = needLoginUrl;
    }

    /**
     * Set the adminNeedLoginUrl value.
     * 
     * @param adminNeedLoginUrl
     *            the adminNeedLoginUrl to set
     */
    public void setAdminNeedLoginUrl(List<String> adminNeedLoginUrl) {
        this.adminNeedLoginUrl = adminNeedLoginUrl;
    }
}

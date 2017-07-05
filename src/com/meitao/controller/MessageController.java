package com.meitao.controller;

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

import com.meitao.service.MessageService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ParameterConstants;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.MessageUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.model.Message;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;

@Controller
public class MessageController extends BasicController {

	private static final long serialVersionUID = -8158555007757915055L;
	private static final Logger log = Logger.getLogger(MessageController.class);

	@Resource(name = "messageService")
	private MessageService messageService;
	@Value(value = "${page_size}")
	private int defaultPageSize;

	@RequestMapping(value = "/message/add", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> addMessage(
	        HttpServletRequest request,
	        @RequestParam(value = ParameterConstants.MESSAGE_CONTENT) String content,
	        @RequestParam(value = ParameterConstants.MESSAGE_PARENT_ID, required = false, defaultValue = "") String parentId) {
		if (!MessageUtil.validateContent(content)) {
			return generateResponseObject(ResponseCode.MESSAGE_CONTENT_ERROR, "留言信息输入不正确，请重新输入!");
		}

		if (!MessageUtil.validateParentId(parentId)) {
			return generateResponseObject(ResponseCode.MESSAGE_FATHER_ID_ERROR, "请重新输入!");
		}

		if (StringUtil.isEmpty(parentId)) {
			parentId = "-1";
		}

		String userId = StringUtil.obj2String(request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY));
		String userName = StringUtil.obj2String(request.getSession().getAttribute(Constant.USER_NICK_NAME_SESSION_KEY));
		Message msg = new Message();
		msg.setContent(content);
		msg.setParentId(parentId); 
		msg.setUserId(userId);
		msg.setUserName(userName);
		msg.setState(Constant.MESSAGE_STATE_EMP_NOT_DEAL);
		try {
			return this.messageService.saveMessage(msg);
		} catch (Exception e) {
			log.error("保存用户留言信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "保存用户留言信息失败");
		}
	}

	@RequestMapping(value = "/message/search", method = {RequestMethod.GET })
	@ResponseBody
	public ResponseObject<PageSplit<Message>> searchAll(
	        HttpServletRequest request,
	        @RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex) {
		String userId = StringUtil.obj2String(request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY));
		try {
			pageIndex = Math.max(pageIndex, 1);
			return this.messageService.getByUserId(userId, defaultPageSize, pageIndex);
		} catch (Exception e) {
			log.error("获取用户留言列表出现异常", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取留言列表失败!");
		}
	}

	@RequestMapping(value = "/message/getcount", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Map<String, String>> getSelfMessageCount(HttpServletRequest request) {
		try {
			String userId = StringUtil.obj2String(request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY));
			return this.messageService.getMessageCount(userId);
		} catch (Exception e) {
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取留言数量失败");
		}
	}

	@RequestMapping(value = "/admin/message/add", method = { RequestMethod.POST})
	@ResponseBody
	public ResponseObject<Object> replyMessage(HttpServletRequest request,
	        @RequestParam(value = ParameterConstants.MESSAGE_CONTENT) String content,
	        @RequestParam(value = ParameterConstants.MESSAGE_PARENT_ID) String parentId) {
		if (!MessageUtil.validateContent(content)) {
			return generateResponseObject(ResponseCode.MESSAGE_CONTENT_ERROR, "留言信息输入不正确，请重新输入!");
		}

		if (StringUtil.isEmpty(parentId) || !MessageUtil.validateParentId(parentId)) {
			return generateResponseObject(ResponseCode.MESSAGE_FATHER_ID_ERROR, "被回复的留言id错误，请重新输入!");
		}

		String userId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY));
		String userName = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ACCOUNT_SESSION_KEY));
		Message msg = new Message();
		msg.setContent(content);
		msg.setParentId(parentId);
		msg.setUserId(userId);
		msg.setUserName(userName);
		msg.setState(Constant.MESSAGE_STATE_USER_NOT_DEAL);
		try {
			return this.messageService.saveMessage(msg);
		} catch (Exception e) {
			log.error("保存用户留言信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "保存留言信息失败");
		}
	}

	@RequestMapping(value = "/admin/message/search", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<PageSplit<Message>> searchAll(
	        HttpServletRequest request,
	        @RequestParam(value = ParameterConstants.COMMON_SEARCH_KEY, required = false, defaultValue = "") String key,
	        @RequestParam(value = "user_id", required = false, defaultValue = "") String userId,
	        @RequestParam(value = ParameterConstants.MESSAGE_STATE, required = false, defaultValue = "") String state,
	        @RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex) {
		try {
			String groupid = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_GROUPID_SESSION_KEY));
			state = MessageUtil.getSearchColumnByState(state);
			pageIndex = Math.max(pageIndex, 1);
			return this.messageService.searchByUserId(key, userId, state, defaultPageSize, pageIndex,groupid);
		} catch (Exception e) {
			log.error("获取用户留言列表出现异常", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取留言列表失败!");
		}
	}

	@RequestMapping(value = "/admin/message/getcount", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Map<String, String>> getCount(HttpServletRequest request) {
		try {
			return this.messageService.getMessageCount(null);
		} catch (Exception e) {
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取留言数量失败");
		}
	}
	@RequestMapping(value = "/message/guest", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> addMessageByGuest(HttpServletRequest request, Message message){
		message.setContent(message.getUserId() + ":" + message.getContent());
		message.setUserId("-1");
		message.setState(Constant.MESSAGE_STATE_EMP_NOT_DEAL);
		message.setParentId("-1");
		try {
			return this.messageService.saveMessage(message);
		} catch (Exception e) {
			log.error("保存用户留言信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "保存用户留言信息失败");
		}
	}
}

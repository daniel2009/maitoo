package com.meitao.controller;

import java.util.List;

import javax.annotation.Resource;

import jxl.common.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.meitao.service.AutoSendService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ParameterConstants;
import com.meitao.common.constants.ResponseCode;

import com.meitao.model.ResponseObject;
import com.meitao.model.User;

@Controller
public class SmsController extends BasicController {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(SmsController.class);

	@Resource(name = "autoSendService")
	private AutoSendService autoSendService;
	
	@RequestMapping(value = "/admin/sms/send", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseObject<List<String>> sendSms(
			@RequestParam(value = ParameterConstants.SMS_PHONE) String[] phones,
			@RequestParam(value = ParameterConstants.SMS_CONTENT) String content
			) {
		if (phones == null||content==null) {
			return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数无效");
		}
		try {
			content =  new String(content.getBytes("ISO-8859-1"), "utf-8");
			ResponseObject<List<String>> responseObj = new ResponseObject<List<String>>();
			
			for (String phone : phones) {
				User user = new User();
				user.setPhone(phone);
				this.autoSendService.send(user, Constant.AUTO_SEND_NEW_USER_PASSWORD);
//				log.info(SmsSendUtil.sendGeneralMsg(content, phone));
			}

			/*ResponseObject<Object> result = null;
			result = this.orderService.checkNoSubmit(wayBill);
			if (result != null && ResponseCode.SUCCESS_CODE.equals(result.getCode())) {
				// 数据正确, 进行运单/订单添加到数据中
				
				responseObj = this.orderService.addOrdersOfWayBill(wayBill);
			}
			 */
			responseObj.setCode("200");
			
			return responseObj;
		} catch (Exception e) {
			log.error("提交发送短信失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "提交发送短信失败");
		}
	}
	
}

package com.meitao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitao.dao.AutoSendDao;
import com.meitao.dao.globalargsDao;
import com.meitao.service.AutoSendService;
import com.meitao.service.SendEmailService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.AutoSendUtil;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.common.util.UserUtil;
import com.meitao.common.util.sms.MailSendUtil;
import com.meitao.common.util.sms.SimpleSmsSender;
import com.meitao.exception.ServiceException;
import com.meitao.model.AutoSend;
import com.meitao.model.Order;
import com.meitao.model.ResponseObject;
import com.meitao.model.SendEmail;
import com.meitao.model.User;

@Service("autoSendService")
public class AutoSendServiceImpl implements AutoSendService{
	//private static final Logger log = Logger.getLogger(AutoSendServiceImpl.class);
	@Autowired
	private AutoSendDao autoSendDao;
	@Autowired
	private globalargsDao globalargsDao;
	@Resource(name = "sendEmailService")
	private SendEmailService sendEmailService;
	@Override
	public ResponseObject<List<AutoSend>> findAll()	throws ServiceException {
		ResponseObject<List<AutoSend>> responseObject = new ResponseObject<List<AutoSend>>();
		try{
			List<AutoSend> list = this.autoSendDao.findAll();
			if(list == null || list.isEmpty()){
				responseObject.setCode(ResponseCode.AUTO_SEND_EMPTY_LIST);
				responseObject.setMessage("自动发送列表没有数据");
			}else{
				responseObject.setCode(ResponseCode.SUCCESS_CODE);
				responseObject.setData(list);
			}
		}catch(Exception e){
			e.printStackTrace();
			throw ExceptionUtil.handle2ServiceException(e);
		}
		return responseObject;
	}
	
	@Override
	public ResponseObject<Object> add(AutoSend autoSend) throws ServiceException {
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		if(autoSend == null){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数错误");
		}else{
			autoSend.setModifyDate(DateUtil.date2String(new Date()));
			try{
				int result = this.autoSendDao.insert(autoSend);
				if(result > 0){
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}else{
					responseObject.setCode(ResponseCode.AUTO_SEND_INSERT_ERROR);
					responseObject.setMessage("添加自动发送数据失败");
				}
			}catch(Exception e){
				e.printStackTrace();
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return responseObject;
	}


	@Override
	public ResponseObject<Object> modify(AutoSend autoSend) throws ServiceException{
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		if(StringUtil.isEmpty(autoSend.getId())){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数错误");
		}else{
			try{
				autoSend.setModifyDate(DateUtil.date2String(new Date()));
				int result = this.autoSendDao.update(autoSend);
				if(result > 0){
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}else{
					responseObject.setCode(ResponseCode.AUTO_SEND_UPDATE_ERROR);
					responseObject.setMessage("更新自动发送失败");
				}
			}catch(Exception e){
				e.printStackTrace();
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return responseObject;
	}

	@Override
	public ResponseObject<Object> delete(AutoSend autoSend) throws ServiceException {
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		if(StringUtil.isEmpty(autoSend.getId())){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数错误");
		}else{
			autoSend.setModifyDate(DateUtil.date2String(new Date()));
			try{
				int result = this.autoSendDao.delete(autoSend);
				if(result > 0){
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}else{
					responseObject.setCode(ResponseCode.AUTO_SEND_DELETE_ERROR);
					responseObject.setMessage("删除自动发送数据失败");
				}
			}catch(Exception e){
				e.printStackTrace();
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return responseObject;
	}
	
	@Override
	public ResponseObject<AutoSend> findByName(AutoSend autoSend) throws ServiceException {
		ResponseObject<AutoSend> responseObject = new ResponseObject<AutoSend>();
		if(StringUtil.isEmpty(autoSend.getName())){
			responseObject.setCode(ResponseCode.PARAMETER_ERROR);
			responseObject.setMessage("参数错误");
		}else{
			try{
				autoSend = this.autoSendDao.findLastLikeName(autoSend);
				if(autoSend == null){
					responseObject.setCode(ResponseCode.AUTO_SEND_NOT_EXISTS);
					responseObject.setMessage("找不到对应名称的自动发送数据");
				}else{
					responseObject.setData(autoSend);
					responseObject.setCode(ResponseCode.SUCCESS_CODE);
				}
			}catch(Exception e){
				e.printStackTrace();
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return responseObject;
	}

	@Override
	public ResponseObject<Object> send(User user, Object object) throws ServiceException{
		return this.send(user, object, "");
	}
	@Override
	public ResponseObject<Object> send(User user, Object object, String code) throws ServiceException{
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		responseObject.setCode(ResponseCode.AUTO_SEND_NOT_EXISTS);
		String email = user.getEmail();
		String phone = user.getPhone();
		String name = AutoSendUtil.getName(object);
		AutoSend autoSend = new AutoSend();
		autoSend.setName(name);
		ResponseObject<AutoSend> autoSendResponseObject = this.findByName(autoSend);
		autoSend = autoSendResponseObject.getData();
		if(autoSend == null){
			responseObject.setCode(ResponseCode.AUTO_SEND_NOT_EXISTS);
			responseObject.setMessage("没找到对应自动发送数据");
		}else{
			try{
				if(object instanceof Order){
					AutoSendUtil.setOrderInfo(autoSend, (Order)object);
					if("1".equals(autoSend.getSend2Recipient())){
						phone = ((Order) object).getcPhone();
					}
				}
				AutoSendUtil.setCode(autoSend, code);
				if("1".equals(autoSend.getAutoEmail()) && UserUtil.validateEmail(email)){//send email
					

					SendEmail sendEmail = new SendEmail();
					sendEmail.setSmtpHost(globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SEND_EMAIL_SMTP_HOST));
					sendEmail.setUserName(globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SEND_EMAIL_USER_NAME));
					sendEmail.setPassword(globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SEND_EMAIL_PASSWORD));
					
					String title=globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SEND_EMAIL_TITLE);
					if(StringUtil.isEmpty(title))
					{
						title="找回密码";
					}
					//title="测试";
					
					
					String content = "We received a request to reset the password associated with this e-mail address. If you made this request, please follow the instructions below.\r\n";

					content+="Copy and paste the link into your browser's address window  to reset your password using our secure server:\r\n\r\n";
					content+=code+"\r\n\r\n";
					content+="If you did not request to have your password reset you can safely ignore this email. Rest assured your customer account is safe.\r\n";
					String websit=globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SEND_EMAIL_WEBSITE);
					if(!StringUtil.isEmpty(websit))
					{
						content+="Website Address:"+websit;
					}
					//content="测试内容";
					
					sendEmail.setTitle(title);
					
					//sendEmail.setContent(autoSend.getEmailContent().replace("#code#", code));
					sendEmail.setContent(content);
					sendEmail.setRecipient(email);
					boolean result = MailSendUtil.send(sendEmail);
					//log.info("向" + email + "发送自动邮件:" + content + ";" + result);
					if(result)
					{
						responseObject.setCode(ResponseCode.SUCCESS_CODE);
					}
					else
					{
						responseObject.setCode(ResponseCode.PARAMETER_ERROR);
					}
				}
				if("1".equals(autoSend.getAutoMessage()) && !StringUtil.isEmpty(phone)){//send message
					SimpleSmsSender simpleSmsSender = new SimpleSmsSender(globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_API_KEY));
					String countryCode = null;
					String companyTitle = null;
					if(phone.length()==10){
						companyTitle = globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_COMPANY_TITLE_EN);
						countryCode = "+1";
					}else if(phone.length()==11){
						companyTitle = globalargsDao.getcontentbyflag(Constant.GLOBALARGS_FLAG_SMS_COMPANY_TITLE_CN);
						countryCode = "+86";
					}else{
						return responseObject;
					}
					phone = countryCode + phone;
					String content = AutoSendUtil.setTemplate(companyTitle, countryCode, autoSend);
					String aaa=simpleSmsSender.sendSms(content, phone);
					aaa="["+aaa+"]";
					List<Map<String, String>> list1 = jsonStringToList(aaa);
					String code1="";
					String msg1="";
					String result1="";
					for(Map<String, String> a:list1)
					{
						 code1=a.get("code");
						 msg1=a.get("msg");
						 result1=a.get("result");
					}
					
					
					//log.info(aaa);
					if(code1.equals("0"))//表示发送成功
					{
						responseObject.setCode(ResponseCode.SUCCESS_CODE);
					}
					else
					{
						responseObject.setCode(ResponseCode.PARAMETER_ERROR);
						responseObject.setMessage(msg1);
					}
				}
			}catch(Exception e){//不能影响其它操作，总之不能出现异常
				e.printStackTrace();
				//throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return responseObject;
	}
	
	private static List<Map<String, String>> jsonStringToList(String rsContent) throws Exception
    {
        JSONArray arry = JSONArray.fromObject(rsContent);
        System.out.println("json字符串内容如下");
        System.out.println(arry);
        List<Map<String, String>> rsList = new ArrayList<Map<String, String>>();
        for (int i = 0; i < arry.size(); i++)
        {
            JSONObject jsonObject = arry.getJSONObject(i);
            Map<String, String> map = new HashMap<String, String>();
            for (Iterator<?> iter = jsonObject.keys(); iter.hasNext();)
            {
                String key = (String) iter.next();
                String value = jsonObject.get(key).toString();
                map.put(key, value);
            }
            rsList.add(map);
        }
        return rsList;
    }

	@Override
	public ResponseObject<Object> getNeedResiterCode() throws ServiceException {
		ResponseObject<Object> responseObject = new ResponseObject<Object>();
		AutoSend autoSend = new AutoSend();
		autoSend.setName(Constant.AUTO_SEND_REGISTER_CODE);
		ResponseObject<AutoSend> autoSendResponse = this.findByName(autoSend);
		if(ResponseCode.SUCCESS_CODE.equals(autoSendResponse.getCode())){
			responseObject.setCode(ResponseCode.SUCCESS_CODE);
			responseObject.setData(autoSendResponse.getData().getAutoMessage());
		}else{
			responseObject.setCode(ResponseCode.AUTO_SEND_NOT_EXISTS);
			responseObject.setMessage("找不到是否需要注册码的设置");
		}
		return responseObject;
	}
	
}

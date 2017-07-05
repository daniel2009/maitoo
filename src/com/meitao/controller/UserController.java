package com.meitao.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.meitao.service.AutoSendService;
import com.meitao.service.CommonService;
import com.meitao.service.EmployeeService;
import com.meitao.service.GlobalArgsService;
import com.meitao.service.UserService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ParameterConstants;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.EmployeeUtil;
import com.meitao.common.util.PropertiesReader;
import com.meitao.common.util.StringUtil;
import com.meitao.common.util.UserUtil;
import com.meitao.dao.AccountDetailDao;
import com.meitao.dao.FreezeMoneyDao;
import com.meitao.dao.UserDao;
import com.meitao.exception.ServiceException;
import com.meitao.model.Account;
import com.meitao.model.AccountDetail;
import com.meitao.model.FreezeMoney;
import com.meitao.model.M_order;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.User;

@Controller
public class UserController extends BasicController {
	private static final long serialVersionUID = 1562088471869155201L;
	private static final Logger log = Logger.getLogger(UserController.class);
	
	@Resource(name = "globalargsService")
	private GlobalArgsService globalargsService;

	@Resource(name = "userService")
	private UserService userService;
	//@Resource(name="userCodeService")
	//private UserCodeService userCodeService;

	@Resource(name = "employeeService")
	private EmployeeService employeeService;
	@Resource(name = "commonService")
	private CommonService commonService;
	@Resource(name = "autoSendService")
	private AutoSendService autoSendService;
	
	@Value(value = "${page_size}")
	private int defaultPageSize;
	@Value(value = "${user.output.templets}")
	private String userOutputTempletsFile;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private AccountDetailDao accountDetailDao;
	
	
	
	@Autowired
	private FreezeMoneyDao freezeMoneyDao;

	@RequestMapping(value = "/user/login", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<User> login(HttpServletRequest request,
	        @RequestParam(value = ParameterConstants.COMMON_ACCOUNT) String account,
	        @RequestParam(value = ParameterConstants.USER_PASSWORD) String password,
	        @RequestParam(value = ParameterConstants.COMMON_VALIDATE_CODE) String vcode) {
		ResponseObject<User> responseObj = null;

		if (!checkVerifyCode(request, vcode)) {
			responseObj = generateResponseObject(ResponseCode.VALIDATE_CODE_ERROR, "验证码输入错误，请重新输入");
		} else if (!UserUtil.validatePhone(account)&&!UserUtil.validateEmail(account)) {
			responseObj = generateResponseObject(ResponseCode.USER_LOGIN_ACCOUNT_ERROR, "手机或者邮箱输入错误，请重新输入。");
		} else if (!UserUtil.validatePassword(password)) {
			responseObj = generateResponseObject(ResponseCode.USER_PASSWORD_ERROR, "密码不符合规定，请重新输入！");
		} else {
			try {
				ResponseObject<User> ruser = this.userService.checkLogin(account, password);
				String code = ruser.getCode();
				if (ResponseCode.SUCCESS_CODE.equals(code)) {
					HttpSession session = request.getSession();
					session.setAttribute(Constant.USER_ID_SESSION_KEY, ruser.getData().getId());
					session.setAttribute(Constant.USER_NICK_NAME_SESSION_KEY, ruser.getData().getNickName());
					session.setAttribute(Constant.USER_EMAIL_SESSION_KEY, ruser.getData().getEmail());
					session.setAttribute(Constant.USER_PHONE_SESSION_KEY, ruser.getData().getPhone());
					session.setAttribute(Constant.USER_TYPE_SESSION_KEY, ruser.getData().getType());
					//session.setAttribute(Constant.USER_USERCODE_SESSION_KEY, ruser.getData().getUsercode());
					//session.setAttribute(Constant.USER_USERALIAS_SESSION_KEY, ruser.getData().getUseralias());
					session.setAttribute(Constant.LOGIN_ACCOUNT_TYPE, "0");
				} else {
					log.error("用户登录失败,code:" + code);
				}
				responseObj = ruser;
			} catch (Exception e) {
				log.error("调用后台代码出错", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取个人信息失败!");
			}
		}
		return responseObj;
	}

	@RequestMapping(value = "/user/logout", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute(Constant.USER_EMAIL_SESSION_KEY);
		session.removeAttribute(Constant.USER_TYPE_SESSION_KEY);
		session.removeAttribute(Constant.USER_ID_SESSION_KEY);
		session.removeAttribute(Constant.USER_NICK_NAME_SESSION_KEY);
		session.invalidate();
		return generateResponseObject(ResponseCode.SUCCESS_CODE); 
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/user/email_register", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> registerByEmail(
	        HttpServletRequest request,
	        @RequestParam(value = ParameterConstants.USER_EMAIL) String email,
	        @RequestParam(value = ParameterConstants.USER_PASSWORD) String password,
	        @RequestParam(value = ParameterConstants.USER_RECOMMENDER, required = false, defaultValue = "") String recommender,
	        @RequestParam(value = "isloging", required = false, defaultValue = "0") String isLoging) {
		ResponseObject<Object> responseObj = null;

		if (!UserUtil.validateEmail(email)) {
			responseObj = generateResponseObject(ResponseCode.USER_EMAIL_ERROR, "邮箱输入不正确，请重新输入!");
		} else if (!UserUtil.validatePassword(password)) {
			responseObj = generateResponseObject(ResponseCode.USER_PASSWORD_ERROR, "密码输入不正确，请重新输入!");
		} else if (!UserUtil.validateRecommender(recommender)) {
			responseObj = generateResponseObject(ResponseCode.USER_RECOMMENDER_ERROR, "推荐人账号不正确，请重新输入！");
		} else {
			try {
					if (!this.userService.checkExistsByEmail(email)) {
						User user = new User();
						user.setNickName("");
						user.setPassword(password);
						user.setEmail(email);
						user.setRecommender(StringUtil.isEmpty(recommender) ? "-1" : recommender);
						user.setType(Constant.USER_TYPE_NORMAL);
						user.setAddress("");
						user.setCountry("");
						user.setQq("");
						user.setRealName("");
						user.setPhone(null);//数据库已经设置了唯一值，不能设置为""
						
						Date date = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String str = sdf.format(date);
					
						user.setCreateDate(str);
						user.setModifyDate(str);
						user.setRegType(Constant.USER_REG_TYPE_EMAIL);//注册类型为手机
						user.setPhoneState(Constant.USER_PHONE_STATE0);//表示无验证的电话
						user.setEmailState(Constant.USER_EMAIL_STATE0);//邮箱状态为未验证
						
						//UserCode userCode = new UserCode();
				        //userCode = this.userCodeService.findUserCodeByState0();
				        //user.setUsercode(userCode.getUsercode());
						responseObj = this.userService.addUser(user);
						if ("1".equals(isLoging) && responseObj != null
						        && ResponseCode.SUCCESS_CODE.equals(responseObj.getCode())) {
							// 注册成功, 需要直接登录的
							String userId = ((Map<String, String>) responseObj.getData()).get("id");
							HttpSession session = request.getSession();
							session.setAttribute(Constant.USER_ID_SESSION_KEY, userId);
							session.setAttribute(Constant.USER_NICK_NAME_SESSION_KEY, user.getNickName());
							session.setAttribute(Constant.USER_EMAIL_SESSION_KEY, user.getEmail());
							session.setAttribute(Constant.USER_TYPE_SESSION_KEY, user.getType());
							session.setAttribute(Constant.USER_PHONE_SESSION_KEY, user.getPhone());
							session.setAttribute(Constant.LOGIN_ACCOUNT_TYPE, "0");
							
						}
					} else {
						responseObj = generateResponseObject(ResponseCode.USER_EMAIL_EXISTS, "该邮箱已经注册过了，你可以登录或者找回密码！");
				}
			} catch (Exception e) {
				log.error("用户注册失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "用户注册失败!");
			}
		}

		return responseObj;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/user/register", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> register(
	        HttpServletRequest request,
	        @RequestParam(value = ParameterConstants.USER_PHONE) String telphone,
	        @RequestParam(value = ParameterConstants.USER_PASSWORD) String password,
	        @RequestParam(value = ParameterConstants.COMMON_VALIDATE_CODE) String vcode,
	        @RequestParam(value = ParameterConstants.USER_RECOMMENDER, required = false, defaultValue = "") String recommender,
	        @RequestParam(value = "isloging", required = false, defaultValue = "1") String isLoging) {
		ResponseObject<Object> responseObj = null;
		
		//Properties prop = PropertiesReader.read(Constant.SYSTEM_PROPERTIES_FILE);
		int sendCode=0;
		//可能要加入全局变量的判断
		try {
			ResponseObject<String> obj=this.globalargsService.getByFlag("phone_register_codeornot");
			if((obj!=null)&&(ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj.getCode())))
			{
				if(obj.getData().equalsIgnoreCase("yes"))
				{
					sendCode=1;
				}
			}
		}
		catch (Exception e) {}
		
		//sendCode = StringUtil.string2Integer(prop.getProperty("user.register.phone.send.code"));
		
		
		
		if (!checkVerifyPhoneCode(request, telphone,vcode)&&sendCode>0) {
			responseObj = generateResponseObject(ResponseCode.VALIDATE_CODE_ERROR, "验证码输入错误，请重新输入");
		} else if (!UserUtil.validatePhone(telphone)) {
			responseObj = generateResponseObject(ResponseCode.USER_NICK_NAME_ERROR, "手机号码输入不正确，请重新输入!");
		} else if (!UserUtil.validatePassword(password)) {
			responseObj = generateResponseObject(ResponseCode.USER_PASSWORD_ERROR, "密码输入不正确，请重新输入!");
		} else if (!UserUtil.validateRecommender(recommender)) {
			responseObj = generateResponseObject(ResponseCode.USER_RECOMMENDER_ERROR, "推荐人账号不正确，请重新输入！");
		} else {
			try {
					if (!this.userService.checkExistsByPhone(telphone)) { 
						User user = new User();
						user.setNickName("");
						user.setPassword(password);
						user.setEmail(null);
						user.setRecommender(StringUtil.isEmpty(recommender) ? "-1" : recommender);
						user.setType(Constant.USER_TYPE_NORMAL);
						user.setAddress("");
						user.setCountry("");
						user.setPhone(StringUtil.isEmpty(telphone) ? null : telphone);
						//u.setPhone(StringUtil.isEmpty(telphone) ? null : telphone);
						user.setQq("");
						user.setRealName("");
						
						
						Date date = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String str = sdf.format(date);
					
						user.setCreateDate(str);
						user.setModifyDate(str);
						user.setRegType(Constant.USER_REG_TYPE_PHONE);//注册类型为手机
						if(sendCode>0)
						{
							user.setPhoneState(Constant.USER_PHONE_STATE1);//表示已经验证的电话
						}
						else
						{
							user.setPhoneState(Constant.USER_PHONE_STATE0);//表示没有验证的电话
						}
						user.setEmailState(Constant.USER_EMAIL_STATE0);//邮箱状态为未验证
						//UserCode userCode = new UserCode();
				        //userCode = this.userCodeService.findUserCodeByState0();
				        //user.setUsercode(userCode.getUsercode()); newwaybill
						responseObj = this.userService.addUser(user);
						if ("1".equals(isLoging) && responseObj != null
						        && ResponseCode.SUCCESS_CODE.equals(responseObj.getCode())) {
							// 注册成功, 需要直接登录的
							String userId = ((Map<String, String>) responseObj.getData()).get("id");
							HttpSession session = request.getSession();
							session.setAttribute(Constant.USER_ID_SESSION_KEY, userId);
							session.setAttribute(Constant.USER_NICK_NAME_SESSION_KEY, user.getNickName());
							session.setAttribute(Constant.USER_EMAIL_SESSION_KEY, user.getEmail());
							session.setAttribute(Constant.USER_TYPE_SESSION_KEY, user.getType());
							session.setAttribute(Constant.USER_PHONE_SESSION_KEY, user.getPhone());
							session.setAttribute(Constant.LOGIN_ACCOUNT_TYPE, "0");
							
							session.removeAttribute(Constant.PHONE_KEY);
							session.removeAttribute(Constant.PHONE_SEND_CODE);
							session.removeAttribute(Constant.PHONE_SEND_CODE_TIME);
						}
					} else {
						responseObj = generateResponseObject(ResponseCode.USER_PHONE_EXISTS, "该手机号码已经注册过了，找回密码或者登录！");
				}
			} catch (Exception e) {
				log.error("用户注册失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "用户注册失败!");
			}
		}

		return responseObj;
	}

	@RequestMapping(value = "/user/save_info", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> saveUserInfo(
	        HttpServletRequest request,
	        @RequestParam(value = ParameterConstants.USER_REAL_NAME, defaultValue = "", required = false) String realName,
	        @RequestParam(value = ParameterConstants.USER_NICK_NAME, defaultValue = "", required = false) String nickname,
	        @RequestParam(value = ParameterConstants.USER_PHONE, defaultValue = "", required = false) String phone,
	        @RequestParam(value = ParameterConstants.USER_EMAIL, defaultValue = "", required = false) String email,
	        @RequestParam(value = ParameterConstants.USER_QQ, defaultValue = "", required = false) String qq,
	        @RequestParam(value = ParameterConstants.USER_COUNTRY, defaultValue = "", required = false) String country,
	        @RequestParam(value = ParameterConstants.USER_ADDRESS, defaultValue = "", required = false) String address) {
		if (!UserUtil.validateRealName(realName)) {
			return generateResponseObject(ResponseCode.USER_REAL_NAME_ERROR, "用户真实姓名不正确，请重新输入！");
		}
		if((email!=null)&&(!email.equalsIgnoreCase("")))
		{
			if (! UserUtil.validateEmail(email)) {
				return generateResponseObject(ResponseCode.USER_EMAIL_ERROR, "用户邮箱不正确，请重新输入！");
			}
			
		}
		if((phone!=null)&&(!phone.equalsIgnoreCase("")))
		{
			if (! UserUtil.validatePhone(phone)) {
				return generateResponseObject(ResponseCode.USER_PHONE_ERROR, "用户电话不正确，请重新输入！");
			}
			
		}
		if (!UserUtil.validateQQ(qq)) {
			return generateResponseObject(ResponseCode.USER_QQ_ERROR, "用户QQ输入不正确，请重新输入！");
		}

		if (!UserUtil.validateCountry(country)) {
			return generateResponseObject(ResponseCode.USER_COUNTRY_ERROR, "国家不符合规定，请重新输入！");
		}
		if (!UserUtil.validateAddress(address)) {
			return generateResponseObject(ResponseCode.USER_ADDRESS_ERROR, "地址信息不符合规定，请重新输入！");
		}

		//User user = new User();
		try {
		String id = StringUtil.obj2String(request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY));
		
		List<User> list=this.userDao.checkexistsforphone(phone);
		if((list!=null)&&(list.size()>0))
		{
			for(User user1:list)
			{
				if(!id.equalsIgnoreCase(user1.getId()))
				{
					return generateResponseObject(ResponseCode.USER_PHONE_ERROR, "用户电话已存在，请重新输入！");
				}
			}
		}
		if(!StringUtil.isEmpty(email))
		{
			list=this.userDao.checkexistsforemail(email);
			if((list!=null)&&(list.size()>0))
			{
				for(User user1:list)
				{
					if(!id.equalsIgnoreCase(user1.getId()))
					{
						return generateResponseObject(ResponseCode.USER_PHONE_ERROR, "用户邮箱已存在，请重新输入！");
					}
				}
			}
		}
		
		ResponseObject<User> ruser=this.userService.getUserById(id);
		if(ruser==null)
		{
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "读取个人信息失败!");
		}
		
		User user = ruser.getData();
		user.setId(id);
		user.setRealName(StringUtil.isEmpty(realName) ? "" : realName);
		
		if((user.getEmailState()!=null)&&(! user.getEmailState().equalsIgnoreCase(Constant.USER_EMAIL_STATE1)))
		{
			user.setEmail(StringUtil.isEmpty(email) ? null : email);
		}
		user.setQq(StringUtil.isEmpty(qq) ? "" : qq);
		user.setCountry(StringUtil.isEmpty(country) ? "" : country);
		user.setAddress(StringUtil.isEmpty(address) ? "" : address);
		user.setNickName(StringUtil.isEmpty(nickname) ? "" : nickname);
		
		if((user.getPhoneState()!=null)&&(! user.getPhoneState().equalsIgnoreCase(Constant.USER_PHONE_STATE1)))
		{
			user.setPhone(StringUtil.isEmpty(phone) ? "" : phone);
		}

		
			return this.userService.modifyUser(user);
		} catch (Exception e) {
			log.error("修改用户数据出现exception", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改个人信息失败!");
		}
	}

	@RequestMapping(value = "/user/reset_pwd", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> resetPwd(HttpServletRequest request,
	        @RequestParam(value = ParameterConstants.USER_PHONE) String phone,
	        @RequestParam(value = ParameterConstants.USER_PASSWORD) String password,
	        @RequestParam(value = ParameterConstants.COMMON_VALIDATE_CODE) String vcode) {
		ResponseObject<Object> responseObj = null;
		if (!checkVerifyPhoneCode(request, phone,vcode)) {
			responseObj = generateResponseObject(ResponseCode.VALIDATE_CODE_ERROR, "验证码输入错误，请重新输入");
		} else if (!UserUtil.validatePhone(phone)) {
			responseObj = generateResponseObject(ResponseCode.USER_EMAIL_ERROR, "手机号码输入不正确，请重新输入！");
		} else if (!UserUtil.validatePassword(password)) {
			responseObj = generateResponseObject(ResponseCode.USER_PASSWORD_ERROR, "密码输入不正确，请重新输入！");
		}  else {
			// 验证通过, 检测用户是否存在
			boolean exists = false;
			try {
				exists = this.userService.checkExistsByPhone(phone);
			} catch (ServiceException e) {
				log.error("根据phone获取账户失败,phone=" + phone, e);
				responseObj = generateResponseObject(ResponseCode.SHOW_EXCEPTION, "根据phone检测会员是否存在失败!");
			}

			if (exists) {
				try {
					ResponseObject<Object> tresult = this.userService.modifyPassword(null, phone, password, null);
					
					if (tresult != null && ResponseCode.SUCCESS_CODE.equals(tresult.getCode())) {
						responseObj = generateResponseObject(ResponseCode.SUCCESS_CODE);
					} else {
						responseObj = generateResponseObject(ResponseCode.USER_MODIFY_FAILURE, "修改密码失败");
					}
				} catch (ServiceException e) {
					log.error("修改/重置密码失败!", e);
					responseObj = generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改/重置密码失败!");
				}
				
			} else {
				responseObj = generateResponseObject(ResponseCode.USER_PHONE_NOT_EXISTS, "该手机没有注册!");
			}
		}
		return responseObj;
	}
	
	@RequestMapping(value = "/user/reset_email_pwd", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> resetPwdOfEmail(HttpServletRequest request,
	        @RequestParam(value = ParameterConstants.USER_EMAIL) String email,
	        @RequestParam(value = ParameterConstants.COMMON_VALIDATE_CODE) String vcode) {
		ResponseObject<Object> responseObj = null;

		if (!checkVerifyCode(request, vcode)) {
			responseObj = generateResponseObject(ResponseCode.VALIDATE_CODE_ERROR, "验证码输入错误，请重新输入");
		} else if (!UserUtil.validateEmail(email)) {
			responseObj = generateResponseObject(ResponseCode.USER_EMAIL_ERROR, "邮箱输入不正确，请重新输入！");
		} else {
			// 验证通过, 检测用户是否存在
			boolean exists = false;
			try {
				exists = this.userService.checkExistsByEmail(email);
			} catch (ServiceException e) {
				log.error("根据email获取账户失败,email=" + email, e);
				responseObj = generateResponseObject(ResponseCode.SHOW_EXCEPTION, "根据email检测会员是否存在失败!");
			}

			if (exists) {
				try {
					String token = this.commonService.getLastToken(email);
					if (StringUtil.isEmpty(token)) {
						token = this.generateResetPwdToken(email);
					}

					ResponseObject<String> result = this.commonService.insertToken(email, token);
					if (request != null && ResponseCode.SUCCESS_CODE.equals(result.getCode())) {
						try {
							String baseUrl = "http://" + request.getServerName() + ":" + request.getServerPort()
							        + request.getContextPath() + "/";
							User user = new User();
							user.setEmail(email);
							this.autoSendService.send(user, Constant.AUTO_SEND_FORGET_PASSWORD, baseUrl + "resetemailredirct.html?email="+user.getEmail()+"&token="+result.getData());
//							MailSendUtil.sendResetPwdMsg(baseUrl, email, result.getData());
							responseObj = generateResponseObject(ResponseCode.SUCCESS_CODE, "重置密码邮件已发送");
						} catch (Throwable e) {
							log.error("发送邮件失败", e);
							responseObj = generateResponseObject(ResponseCode.SHOW_EXCEPTION, "发送邮件失败!");
						}
					} else {
						responseObj = generateResponseObject(ResponseCode.TOKEN_INSERT_ERROR, "发送邮件失败");
					}
				} catch (Exception e) {
					log.error("保存token失败", e);
					responseObj = generateResponseObject(ResponseCode.SHOW_EXCEPTION, "发送邮件失败!");
				}
			} else {
				responseObj = generateResponseObject(ResponseCode.USER_EMAIL_NOT_EXISTS, "该email没有注册!");
			}
		}
		return responseObj;
	}

	@RequestMapping(value = "/user/reset_pwd2", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> resetPwd2(
	        HttpServletRequest request,
	        @RequestParam(value = ParameterConstants.USER_EMAIL, required = false, defaultValue = "") String email,
	        @RequestParam(value = ParameterConstants.COMMON_TOKEN, required = false, defaultValue = "") String token,
	        @RequestParam(value = ParameterConstants.USER_PASSWORD) String password,
	        @RequestParam(value = ParameterConstants.COMMON_OLD_PASSWORD, required = false, defaultValue = "") String oldPassword,
	        @RequestParam(value = ParameterConstants.COMMON_VALIDATE_CODE) String vcode) {
		ResponseObject<Object> responseObj = null;

		if (StringUtil.isEmpty(email)) {
			email = StringUtil.obj2String(request.getSession().getAttribute(Constant.USER_EMAIL_SESSION_KEY));
		}

		if (StringUtil.isEmpty(email)) {
			return new ResponseObject<Object>(ResponseCode.NEED_LOGIN, "请登录后操作，email不能为空");
		}

		if (!checkVerifyCode(request, vcode)) {
			responseObj = generateResponseObject(ResponseCode.VALIDATE_CODE_ERROR, "验证码输入错误，请重新输入");
		} else if (!UserUtil.validateEmail(email)) {
			responseObj = generateResponseObject(ResponseCode.USER_EMAIL_ERROR, "邮箱输入不正确，请重新输入！");
		} else if (!UserUtil.validatePassword(password)) {
			responseObj = generateResponseObject(ResponseCode.USER_PASSWORD_ERROR, "密码输入不正确，请重新输入！");
		} else {
			try {
				if (!StringUtil.isEmpty(token)) {
					ResponseObject<String> result = this.commonService.checkToken(email, token);
					if (result != null && ResponseCode.SUCCESS_CODE.equals(result.getCode())) {
						// 可以修改用户密码
						ResponseObject<Object> tresult = this.userService.modifyPasswordByEmail(null, email, password, null);
						if (tresult != null && ResponseCode.SUCCESS_CODE.equals(tresult.getCode())) {
							responseObj = generateResponseObject(ResponseCode.SUCCESS_CODE);
						} else {
							responseObj = generateResponseObject(ResponseCode.USER_MODIFY_FAILURE, "重置密码失败");
						}
					} else if (!StringUtil.isEmpty(oldPassword) && UserUtil.validatePassword(oldPassword)) {
						// 可以修改用户密码
						ResponseObject<Object> tresult = this.userService.modifyPassword(null, email, password,
						        oldPassword);
						if (tresult != null && ResponseCode.SUCCESS_CODE.equals(tresult.getCode())) {
							responseObj = generateResponseObject(ResponseCode.SUCCESS_CODE);
						} else {
							responseObj = generateResponseObject(ResponseCode.USER_MODIFY_FAILURE, "修改密码失败");
						}
					} else {
						responseObj = generateResponseObject(ResponseCode.TOKEN_ERROR, "已过期或者是参数无效，请重新操作！");
					}
				} else if (UserUtil.validatePassword(oldPassword)) {
					// 可以修改用户密码
					ResponseObject<Object> tresult = this.userService
					        .modifyPassword(null, email, password, oldPassword);
					if (tresult != null && ResponseCode.SUCCESS_CODE.equals(tresult.getCode())) {
						responseObj = generateResponseObject(ResponseCode.SUCCESS_CODE);
					} else {
						responseObj = generateResponseObject(ResponseCode.USER_MODIFY_FAILURE, "原密码错误");
					}
				} else {
					responseObj = generateResponseObject(ResponseCode.TOKEN_ERROR, "已过期或者是参数无效，请重新操作！");
				}

			} catch (Exception e) {
				log.error("检测token出现异常", e);
				responseObj = generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改/重置密码失败!");
			}
		}
		return responseObj;
	}

	@RequestMapping(value = "/user/get_self", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<User> getSelf(HttpServletRequest request) {
		ResponseObject<User> responseObj = null;
		try {
			String userId = StringUtil.obj2String(request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY));
			responseObj = this.userService.getUserById(userId);
		} catch (Exception e) {
			log.error("获取自身信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取个人信息失败!");
		}
		return responseObj;
	}

	@RequestMapping(value = "/admin/user/search", method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public ResponseObject<PageSplit<User>> searchAll(
			HttpServletRequest request, 
	        @RequestParam(value = "userId", required = false, defaultValue = "") String userId,
	        @RequestParam(value = ParameterConstants.COMMON_SEARCH_KEY, required = false, defaultValue = "") String key,
	        @RequestParam(value = ParameterConstants.COMMON_SEARCH_TYPE, required = false, defaultValue = "all") String type,
	        @RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex) {
		try {
			key =  new String(key.getBytes("ISO-8859-1"), "utf-8");
			String groupid = (String)request.getSession().getAttribute(Constant.EMP_GROUPID_SESSION_KEY);
			userId = StringUtil.isEmpty(userId) ? null : userId.trim();
			String searchColumn = UserUtil.getSearchColumnByType(type);
			pageIndex = Math.max(pageIndex, 1);
			return this.userService.searchByKey(userId, key, searchColumn, defaultPageSize, pageIndex, groupid);
		} catch (Exception e) {
			log.error("获取会员列表出现异常", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取会员列表失败!");
		}
	}
	
	//用于门市会员搜索
	@RequestMapping(value = "/admin/user/m_search", method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public ResponseObject<PageSplit<User>> searchAll(
			HttpServletRequest request, 
	        @RequestParam(value = "usersinfo", required = false, defaultValue = "") String usersinfo,
	        @RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex) {
		try {
			
			pageIndex = Math.max(pageIndex, 1);
			return this.userService.searchByinfo(usersinfo, defaultPageSize, pageIndex);
					
		} catch (Exception e) {
			log.error("获取会员列表出现异常", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取会员列表失败!");
		}
	}

	@RequestMapping(value = "/admin/user/all", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<PageSplit<User>> getAllUsers(HttpServletRequest request, 
	        @RequestParam(value = ParameterConstants.COMMON_SEARCH_KEY, required = false) String key) {
		try {
			key =  new String(key.getBytes("ISO-8859-1"), "utf-8");
			String groupid = (String)request.getSession().getAttribute(Constant.EMP_GROUPID_SESSION_KEY);
			return this.userService.searchByKey(null, key,null , defaultPageSize, 1, groupid);
		} catch (Exception e) {
			log.error("获取会员列表出现异常", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取会员列表失败!");
		}
	}

	@RequestMapping(value = "/admin/user/show_one", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<User> getOne(@RequestParam(value = ParameterConstants.USER_ID) String id) {
		ResponseObject<User> responseObj = null;
		if (!UserUtil.validateId(id)) {
			responseObj = generateResponseObject(ResponseCode.USER_ID_ERROR, "会员id错误,不符合格式要求！");
		} else {
			try {
				responseObj = this.userService.getUserById(id);
			} catch (Exception e) {
				log.error("根据用户id获取用户数据失败");
				responseObj = generateResponseObject(ResponseCode.SHOW_EXCEPTION, "根据id获取会员信息失败!");
			}
		}
		return responseObj;
	}
	
	@RequestMapping(value = "/admin/user/show_one_by_phone", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<User> getOneByPhone(@RequestParam(value = ParameterConstants.USER_PHONE) String phone) {
		ResponseObject<User> responseObj = null;
		if (!UserUtil.validatePhone(phone)) {
			responseObj = generateResponseObject(ResponseCode.USER_PHONE_ERROR, "寄件人手机号码错误,不符合格式要求！");
		} else {
			try {
				responseObj = this.userService.getUserByPhone(phone);
			} catch (Exception e) {
				log.error("根据用户手机号码获取用户数据失败");
				responseObj = generateResponseObject(ResponseCode.SHOW_EXCEPTION, "根据手机号码获取会员信息失败!");
			}
		}
		return responseObj;
	}

	@RequestMapping(value = "/admin/user/del", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> delUser(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.USER_ID) String[] ids) {
		ResponseObject<Object> responseObj = null;
		
		
		//String storeId=null;
		String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
		{
			//storeId=null;//表示可以查找所有门店
			
		}else
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"对不起，你无权删除用户!");
			//storeId = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
			//if((storeId==null)||(storeId.equalsIgnoreCase("")))
			//{
			//	return generateResponseObject(ResponseCode.NEED_LOGIN,
			//			"你没有登陆!");
			//}
		}
		
		
		if (ids == null || ids.length < 1) {
			
			responseObj = generateResponseObject(ResponseCode.USER_ID_ERROR, "必须给定要删除的用户id");
		} else {
			try {
				responseObj = this.userService.deleteUserByIds(ids);
			} catch (Exception e) {
				log.error("根据用户id获取用户数据失败");
				responseObj = generateResponseObject(ResponseCode.SHOW_EXCEPTION, "删除会员失败!");
			}
		}
		return responseObj;
	}

	@RequestMapping(value = "/admin/user/add", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> addUser(
	        HttpServletRequest request,
	        @RequestParam(value = ParameterConstants.USER_ID, required = false, defaultValue = "") String id,
	        @RequestParam(value = ParameterConstants.USER_NICK_NAME, required = false, defaultValue = "") String name,
	        @RequestParam(value = ParameterConstants.USER_PASSWORD, required = false, defaultValue = "") String password,
	        @RequestParam(value = ParameterConstants.USER_EMAIL, required = false, defaultValue = "") String email,
	        @RequestParam(value = ParameterConstants.USER_REAL_NAME, defaultValue = "", required = false) String realName,
	        @RequestParam(value = ParameterConstants.USER_PHONE, defaultValue = "", required = false) String phone,
	        @RequestParam(value = ParameterConstants.USER_QQ, defaultValue = "", required = false) String qq,
	        @RequestParam(value = ParameterConstants.USER_COUNTRY, defaultValue = "", required = false) String country,
	        @RequestParam(value = ParameterConstants.USER_ADDRESS, defaultValue = "", required = false) String address,	        
	        @RequestParam(value = ParameterConstants.USER_TYPE, defaultValue = Constant.USER_TYPE_NORMAL, required = false) String type) {
		User user = new User();

		if (StringUtil.isEmpty(id)) {
			//if (!UserUtil.validateNickName(name)) {
				//return generateResponseObject(ResponseCode.USER_NICK_NAME_ERROR, "用户名输入不正确，请重新输入!");
			//} else 
			if (!UserUtil.validatePassword(password)) {
				return generateResponseObject(ResponseCode.USER_PASSWORD_ERROR, "密码输入不正确，请重新输入!");
			} else if (!UserUtil.validatePhone(phone)) {
				return generateResponseObject(ResponseCode.USER_PHONE_ERROR, "手机号码不正确，请重新输入！");
			}
			//user.setNickName(name);
			//user.setPhone(phone);
			user.setPhone(StringUtil.isEmpty(phone) ? null : phone);
			user.setPassword(password);
		}

		if (!UserUtil.validateRealName(realName)) {
			return generateResponseObject(ResponseCode.USER_REAL_NAME_ERROR, "用户真实姓名不正确，请重新输入！");
		} else if (!UserUtil.validatePhone(phone)) {
			return generateResponseObject(ResponseCode.USER_EMAIL_ERROR, "手机号码输入不正确，请重新输入！");
		} else if (!UserUtil.validateQQ(qq)) {
			return generateResponseObject(ResponseCode.USER_QQ_ERROR, "用户QQ输入不正确，请重新输入！");
		} else if (!UserUtil.validateCountry(country)) {
			return generateResponseObject(ResponseCode.USER_COUNTRY_ERROR, "国家不符合规定，请重新输入！");
		} else if (!UserUtil.validateAddress(address)) {
			return generateResponseObject(ResponseCode.USER_ADDRESS_ERROR, "地址信息格式不正确，请重新输入！");
		} else if ((!StringUtil.isEmpty(email))&&(!UserUtil.validateEmail(email))) {
			return generateResponseObject(ResponseCode.USER_ADDRESS_ERROR, "邮箱不正确，请重新输入！");
		}

		user.setId(id);
		
		user.setRealName(StringUtil.isEmpty(realName) ? "" : realName);
		user.setEmail(StringUtil.isEmpty(email) ? null : email);
		user.setQq(StringUtil.isEmpty(qq) ? "" : qq);
		user.setCountry(StringUtil.isEmpty(country) ? "" : country);
		user.setAddress(StringUtil.isEmpty(address) ? "" : address);
		user.setType(type);
		
		user.setRegType(Constant.USER_REG_TYPE_PHONE);//注册类型为手机
		user.setPhoneState(Constant.USER_PHONE_STATE0);//表示无验证的电话
		user.setEmailState(Constant.USER_EMAIL_STATE0);//邮箱状态为未验证

		if (StringUtil.isEmpty(id)) {
			// add user
			try {
				if ((phone!=null)&&(! phone.equalsIgnoreCase(""))&&(!this.userService.checkExistsByPhone(phone))) {
					
					if ((email!=null)&&(! email.equalsIgnoreCase(""))&&(this.userService.checkExistsByEmail(email)))
					{
						return generateResponseObject(ResponseCode.USER_EMAIL_ERROR, "该邮箱已经注册过了，找回密码或者登录！");
					}
					
						String account = StringUtil.obj2String(request.getSession().getAttribute(
						        Constant.EMP_ACCOUNT_SESSION_KEY));
						String groupid = (String)request.getSession().getAttribute(Constant.EMP_GROUPID_SESSION_KEY);
						user.setEmpaccount(account);
						user.setRecommender("-1");
						user.setGroupId(groupid);
				        //UserCode userCode = new UserCode();
				        //userCode = this.userCodeService.findUserCodeByState0();
				        //user.setUsercode(userCode.getUsercode());
			            return this.userService.addUser(user);
			            
				}
				else if ((email!=null)&&(! email.equalsIgnoreCase(""))&&(!this.userService.checkExistsByEmail(email)))
				{
					if ((phone!=null)&&(! phone.equalsIgnoreCase(""))&&(this.userService.checkExistsByPhone(phone)))
					{
						return generateResponseObject(ResponseCode.USER_EMAIL_ERROR, "该手机已经注册过了，找回密码或者登录！");
					}
					String account = StringUtil.obj2String(request.getSession().getAttribute(
					        Constant.EMP_ACCOUNT_SESSION_KEY));
					String groupid = (String)request.getSession().getAttribute(Constant.EMP_GROUPID_SESSION_KEY);
					user.setEmpaccount(account);
					user.setRecommender("-1");
					user.setGroupId(groupid);
			        //UserCode userCode = new UserCode();
			        //userCode = this.userCodeService.findUserCodeByState0();
			        //user.setUsercode(userCode.getUsercode());
		            return this.userService.addUser(user);
					
				}				
				else {
					return generateResponseObject(ResponseCode.USER_PHONE_EXISTS, "该手机号码或邮箱已经注册过了，找回密码或者登录！");
			}
					
			} catch (Exception e) {
				log.error("用户注册失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "添加会员失败!");
			}
		} else {
			
			
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
			{
				//storeId=null;//表示可以查找所有门店
				
			}else
			{
				String storeMaster = (String)request.getSession().getAttribute(Constant.EMP_STORE_MASTER_SESSION_KEY);//店主标志
				if(!StringUtil.isEmpty(storeMaster)&&(storeMaster.equalsIgnoreCase("1")))//店主可以修改
				{
					
				}
				else
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"对不起，你无权修改用户!");
				}
				//storeId = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
				//if((storeId==null)||(storeId.equalsIgnoreCase("")))
				//{
				//	return generateResponseObject(ResponseCode.NEED_LOGIN,
				//			"你没有登陆!");
				//}
			}
			// modify user
			try {
				user.setNickName(name);
				//user.setPhone(phone);
				user.setPhone(StringUtil.isEmpty(phone) ? null : phone);
				return this.userService.modifyUser(user);
			} catch (Exception e) {
				log.error("修改用户数据出现exception", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改会员信息失败!");
			}
		}
	}

	
	@RequestMapping(value = "/admin/user/addms", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> addUserbyms(
	        HttpServletRequest request,
	        @RequestParam(value = ParameterConstants.USER_ID, required = false, defaultValue = "") String id,
	        @RequestParam(value = ParameterConstants.USER_NICK_NAME, required = false, defaultValue = "") String name,
	        @RequestParam(value = ParameterConstants.USER_PASSWORD, required = false, defaultValue = "") String password,
	        @RequestParam(value = ParameterConstants.USER_EMAIL, required = false, defaultValue = "") String email,
	        @RequestParam(value = ParameterConstants.USER_REAL_NAME, defaultValue = "", required = false) String realName,
	        @RequestParam(value = ParameterConstants.USER_PHONE, defaultValue = "", required = false) String phone,
	        @RequestParam(value = ParameterConstants.USER_QQ, defaultValue = "", required = false) String qq,
	        @RequestParam(value = ParameterConstants.USER_COUNTRY, defaultValue = "", required = false) String country,
	        @RequestParam(value = ParameterConstants.USER_ADDRESS, defaultValue = "", required = false) String address,	        
	        @RequestParam(value = ParameterConstants.USER_TYPE, defaultValue = Constant.USER_TYPE_NORMAL, required = false) String type) {
		User user = new User();

		if (StringUtil.isEmpty(id)) {
			if (!UserUtil.validateNickName(name)) {
				return generateResponseObject(ResponseCode.USER_NICK_NAME_ERROR, "用户名输入不正确，请重新输入!");
			
			} else if (!UserUtil.validatePhone(phone)) {
				return generateResponseObject(ResponseCode.USER_PHONE_ERROR, "手机号码不正确，请重新输入！");
			}
			user.setNickName(name);
			//user.setPhone(phone);
			password="111111";
			user.setPhone(StringUtil.isEmpty(phone) ? null : phone);
			user.setPassword(password);
			user.setRegType(Constant.USER_REG_TYPE_PHONE);//注册类型为手机
			user.setPhoneState(Constant.USER_PHONE_STATE0);//表示无验证的电话
			user.setEmailState(Constant.USER_EMAIL_STATE0);//邮箱状态为未验证
		}

		if (!UserUtil.validateRealName(realName)) {
			return generateResponseObject(ResponseCode.USER_REAL_NAME_ERROR, "用户真实姓名不正确，请重新输入！");
		} else if (!UserUtil.validatePhone(phone)) {
			return generateResponseObject(ResponseCode.USER_EMAIL_ERROR, "手机号码输入不正确，请重新输入！");
		} else if (!UserUtil.validateQQ(qq)) {
			return generateResponseObject(ResponseCode.USER_QQ_ERROR, "用户QQ输入不正确，请重新输入！");
		} else if (!UserUtil.validateCountry(country)) {
			return generateResponseObject(ResponseCode.USER_COUNTRY_ERROR, "国家不符合规定，请重新输入！");
		} else if (!UserUtil.validateAddress(address)) {
			return generateResponseObject(ResponseCode.USER_ADDRESS_ERROR, "地址信息格式不正确，请重新输入！");
		} 

		user.setId(id);
		user.setRealName(StringUtil.isEmpty(realName) ? "" : realName);
		user.setEmail(StringUtil.isEmpty(email) ? null : email);
		user.setQq(StringUtil.isEmpty(qq) ? "" : qq);
		user.setCountry(StringUtil.isEmpty(country) ? "" : country);
		user.setAddress(StringUtil.isEmpty(address) ? "" : address);
		user.setType(type);

		if (StringUtil.isEmpty(id)) {
			// add user
			try {
				if ((phone!=null)&&(! phone.equalsIgnoreCase(""))&&(!this.userService.checkExistsByPhone(phone))) {
					
					if ((email!=null)&&(! email.equalsIgnoreCase(""))&&(this.userService.checkExistsByEmail(email)))
					{
						return generateResponseObject(ResponseCode.USER_EMAIL_ERROR, "该邮箱已经注册过了，找回密码或者登录！");
					}
					
						String account = StringUtil.obj2String(request.getSession().getAttribute(
						        Constant.EMP_ACCOUNT_SESSION_KEY));
						String groupid = (String)request.getSession().getAttribute(Constant.EMP_GROUPID_SESSION_KEY);
						user.setEmpaccount(account);
						user.setRecommender("-1");
						user.setGroupId(groupid);
				        //UserCode userCode = new UserCode();
				        //userCode = this.userCodeService.findUserCodeByState0();
				        //user.setUsercode(userCode.getUsercode());
			            return this.userService.addUser(user);
			            
				}
				else if ((email!=null)&&(! email.equalsIgnoreCase(""))&&(!this.userService.checkExistsByEmail(email)))
				{
					if ((phone!=null)&&(! phone.equalsIgnoreCase(""))&&(this.userService.checkExistsByPhone(phone)))
					{
						return generateResponseObject(ResponseCode.USER_EMAIL_ERROR, "该手机已经注册过了，找回密码或者登录！");
					}
					String account = StringUtil.obj2String(request.getSession().getAttribute(
					        Constant.EMP_ACCOUNT_SESSION_KEY));
					String groupid = (String)request.getSession().getAttribute(Constant.EMP_GROUPID_SESSION_KEY);
					user.setEmpaccount(account);
					user.setRecommender("-1");
					user.setGroupId(groupid);
			        //UserCode userCode = new UserCode();
			        //userCode = this.userCodeService.findUserCodeByState0();
			        //user.setUsercode(userCode.getUsercode());
		            return this.userService.addUser(user);
					
				}				
				else {
					return generateResponseObject(ResponseCode.USER_PHONE_EXISTS, "该手机号码或邮箱已经注册过了，找回密码或者登录！");
			}
					
			} catch (Exception e) {
				log.error("用户注册失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "添加会员失败!");
			}
		} else {
			// modify user
			try {
				user.setNickName(name);
				//user.setPhone(phone);
				user.setPhone(StringUtil.isEmpty(phone) ? null : phone);
				return this.userService.modifyUser(user);
			} catch (Exception e) {
				log.error("修改用户数据出现exception", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改会员信息失败!");
			}
		}
	}

	
	@RequestMapping(value = "/admin/user/modify_pwd", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> modifyUserPwdByAdmin(HttpServletRequest request,
	        @RequestParam(value = ParameterConstants.USER_ID) String id, @RequestParam(value = "emppwd") String emppwd,
	        @RequestParam(value = ParameterConstants.USER_PASSWORD) String password) {
		if (StringUtil.isEmpty(emppwd) || !EmployeeUtil.validatePassword(emppwd)) {
			return generateResponseObject(ResponseCode.EMPLOYEE_PASSWORD_ERROR, "会员密码格式不正确");
		}

		if (StringUtil.isEmpty(password) || !UserUtil.validatePassword(password)) {
			return generateResponseObject(ResponseCode.USER_PASSWORD_ERROR, "用户密码格式不正确");
		}

		try {
			String empId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY));
			ResponseObject<Object> result = this.employeeService.checkEmployee(empId, emppwd);
			if (ResponseCode.SUCCESS_CODE.equals(result.getCode())) {
				result = this.userService.modifyPassword(id, null, password, null);
			}
			return result;
		} catch (Exception e) {
			log.error("修改会员密码失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改会员密码出现异常");

		}
	}

	@RequestMapping(value = "/admin/user/export", method = { RequestMethod.GET })
	public void exportUserDataToExcel(HttpServletRequest request, HttpServletResponse response,
	        @RequestParam(value = ParameterConstants.COMMON_SEARCH_SDATE, required = false) String sdate,
	        @RequestParam(value = ParameterConstants.COMMON_SEARCH_EDATE, required = false) String edate,
	        @RequestParam(value = ParameterConstants.USER_TYPE, required = false) String type) throws Exception {
		Calendar calendar = Calendar.getInstance();
		if (StringUtil.isEmpty(sdate) || !UserUtil.validateExportDate(sdate)) {
			sdate = DateUtil.date2String(calendar.getTime(), "yyyy-MM-dd");
		}
		if (StringUtil.isEmpty(edate) || !UserUtil.validateExportDate(edate)) {
			edate = DateUtil.date2String(calendar.getTime(), "yyyy-MM-dd");
		}
		String fileName = "会员数据_" + sdate + "_" + edate + ".xls";
		sdate = UserUtil.transformerDateString(sdate, " 00:00:00");
		edate = UserUtil.transformerDateString(edate, " 23:59:59");
		String groupid = (String)request.getSession().getAttribute(Constant.EMP_GROUPID_SESSION_KEY);
		OutputStream os=null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename="
			        + new String(fileName.getBytes(), "utf-8"));
			List<User> list = new ArrayList<User>();;
			if (groupid.equals("1")) {
				list = this.userService.getExportUserAllDatas(sdate, edate, type);
		      }
		      else {
		    	list = this.userService.getExportUserDatas(sdate, edate, type, groupid);
		      }
			
			
			File templeFile = new File(request.getSession().getServletContext().getRealPath("/")
			        + this.userOutputTempletsFile);
			os = response.getOutputStream();
			// 导出数据
			UserUtil.exportUsersToExcel(list, templeFile, os);
		} catch (Exception e) {
			log.error("获取用户数据失败", e);
			throw new Exception("获取导出用户数据出现异常，无法获取数据", e);
		}
		finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}
	

	@RequestMapping(value = "/user/reset_pwd_byuser", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> resetPwdbyuser(HttpServletRequest request,
	        @RequestParam(value = ParameterConstants.USER_OLD_CODE) String oldecode,
	        @RequestParam(value = ParameterConstants.USER_NEW_CODE1) String newcode1,
	        @RequestParam(value = ParameterConstants.USER_NEW_CODE2) String newcode2) {
		ResponseObject<Object> responseObj = null;

		String userId = StringUtil.obj2String(request.getSession()
				.getAttribute(Constant.USER_ID_SESSION_KEY));
		
		if((userId==null)||(userId.equalsIgnoreCase("")))
		{
			return new ResponseObject<Object>(ResponseCode.NEED_LOGIN, "请登录后操作!");
		}
	    if((newcode1!=null)&&(newcode2!=null)&&(! newcode1.equalsIgnoreCase(""))&&(!newcode2.equalsIgnoreCase("")))
	    {
	    	if(newcode1.equals(newcode2))
	    	{
	    		
	    	}
	    	else
	    	{
	    		responseObj = generateResponseObject(ResponseCode.USER_MODIFY_FAILURE, "重置密码失败,新密码前后不一致!");
	    	}
	    }
	    else
	    {
	    	responseObj = generateResponseObject(ResponseCode.USER_MODIFY_FAILURE, "重置密码失败,新密码不能为空!");
	    }

	

		if (!UserUtil.validatePassword(newcode1)) {
			responseObj = generateResponseObject(ResponseCode.USER_PASSWORD_ERROR, "新密码输入不正确，至少6位，至多20位字符，请重新输入！");
		} else {
			try {
				ResponseObject<Object> tresult = this.userService.modifyPasswordbyuser(userId, newcode1, oldecode);
				
				if (tresult != null && ResponseCode.SUCCESS_CODE.equals(tresult.getCode())) {
					
					responseObj = generateResponseObject(ResponseCode.SUCCESS_CODE);
				} else {
					responseObj = generateResponseObject(ResponseCode.USER_MODIFY_FAILURE, tresult.getMessage());
				}
				
				

			} catch (Exception e) {
				log.error("检测token出现异常", e);
				responseObj = generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改/重置密码失败!");
			}
		}
		return responseObj;
	}
	
	
	/*
	 * kai 用户修改手机时进行验证并保存
	 * */
	//@SuppressWarnings("unchecked")
	@RequestMapping(value = "/user/verify_info", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> verifyphonebyuser(
	        HttpServletRequest request,
	        @RequestParam(value = ParameterConstants.USER_PHONE) String telphone,
	        @RequestParam(value = ParameterConstants.COMMON_VALIDATE_CODE) String vcode) {
		ResponseObject<Object> responseObj = null;
		
		Properties prop = PropertiesReader.read(Constant.SYSTEM_PROPERTIES_FILE);
		int sendCode = StringUtil.string2Integer(prop.getProperty("user.register.phone.send.code"));
		
		if (!checkVerifyPhoneCode(request, telphone,vcode)&&sendCode>0) {
			responseObj = generateResponseObject(ResponseCode.VALIDATE_CODE_ERROR, "验证码输入错误，请重新输入");
		} else if (!UserUtil.validatePhone(telphone)) {
			responseObj = generateResponseObject(ResponseCode.USER_NICK_NAME_ERROR, "手机号码输入不正确，请重新输入!");
		
		} else {
			String userId = StringUtil.obj2String(request.getSession()
					.getAttribute(Constant.USER_ID_SESSION_KEY));
			
			if((userId==null)||(userId.equalsIgnoreCase("")))
			{
				return new ResponseObject<Object>(ResponseCode.NEED_LOGIN, "请登录后操作!");
			}
			try {
				ResponseObject<User> obj=this.userService.getUserById(userId);
				User user=obj.getData();
				if((user==null)||(user.equals("")))
				{
					return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取用户信息错误！");
				}
			    if((user.getPhoneState()!=null)&&(user.getPhoneState().equalsIgnoreCase(Constant.USER_PHONE_STATE1)))
			    {
			    	return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "电话已经校验过，不能修改！");
			    }
			   // user.setPhone(telphone);
			    user.setPhone(StringUtil.isEmpty(telphone) ? null : telphone);
			    user.setPhoneState(Constant.USER_PHONE_STATE1);
			    Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String str = sdf.format(date);
				user.setModifyDate(str);
			    
				return this.userService.modifyphonebyuser(user);
			} catch (Exception e) {
				log.error("用户注册失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "用户注册失败!");
			}
		}

		return responseObj;
	}
	
	
	/*
	 * kai 用户修改手机时进行验证并保存
	 * */
	
	@RequestMapping(value = "/user/verify_info_email", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> verifyemailbyuser(
	        HttpServletRequest request,
	        @RequestParam(value = ParameterConstants.USER_EMAIL) String email,
	        @RequestParam(value = ParameterConstants.COMMON_VALIDATE_CODE) String vcode) {
		ResponseObject<Object> responseObj = null;
		
		//Properties prop = PropertiesReader.read(Constant.SYSTEM_PROPERTIES_FILE);
		//int sendCode = StringUtil.string2Integer(prop.getProperty("user.register.phone.send.code"));
		
		//if (!checkVerifyPhoneCode(request, telphone,vcode)&&sendCode>0) {
		//	responseObj = generateResponseObject(ResponseCode.VALIDATE_CODE_ERROR, "验证码输入错误，请重新输入");
		//} else 
		if(!checkVerifyEmailCode(request, email,vcode))
		{
			responseObj = generateResponseObject(ResponseCode.VALIDATE_CODE_ERROR, "验证码输入错误，请重新输入");
		}
		else if (!UserUtil.validateEmail(email)) {
			responseObj = generateResponseObject(ResponseCode.USER_NICK_NAME_ERROR, "手机号码输入不正确，请重新输入!");
		
		} else {
			String userId = StringUtil.obj2String(request.getSession()
					.getAttribute(Constant.USER_ID_SESSION_KEY));
			
			if((userId==null)||(userId.equalsIgnoreCase("")))
			{
				return new ResponseObject<Object>(ResponseCode.NEED_LOGIN, "请登录后操作!");
			}
			try {
				ResponseObject<User> obj=this.userService.getUserById(userId);
				User user=obj.getData();
				
				
				if((user==null)||(user.equals("")))
				{
					return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取用户信息错误！");
				}
			    if((user.getEmailState()!=null)&&(user.getEmailState().equalsIgnoreCase(Constant.USER_EMAIL_STATE1)))
			    {
			    	return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "邮箱已经校验过，不能修改！");
			    }
			    user.setEmail(StringUtil.isEmpty(email) ? null : email);
			   // user.setEmail(email);
			    user.setEmailState(Constant.USER_EMAIL_STATE1);
			    Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String str = sdf.format(date);
				user.setModifyDate(str);
			    
				return this.userService.modifyemailbyuser(user);
			} catch (Exception e) {
				log.error("用户注册失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "用户注册失败!");
			}
		}

		return responseObj;
	}
	@RequestMapping(value = "/admin/user/loginAsUser", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<Object> loginAsUser(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.USER_ID) String id){
		String empId = (String) request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY);
		if(StringUtil.isEmpty(empId)){
			return generateResponseObject(ResponseCode.NEED_LOGIN);
		}
		
		
		String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
		{
			//storeId=null;//表示可以查找所有门店
			
		}else
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"对不起，你无权登陆用户!");
			//storeId = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
			//if((storeId==null)||(storeId.equalsIgnoreCase("")))
			//{
			//	return generateResponseObject(ResponseCode.NEED_LOGIN,
			//			"你没有登陆!");
			//}
		}
		
		
		try{
			ResponseObject<User> responseObject = this.userService.getUserById(id);
			if(responseObject.getCode().equals(ResponseCode.SUCCESS_CODE)){
				HttpSession session = request.getSession();
				User user = responseObject.getData();
				session.setAttribute(Constant.USER_ID_SESSION_KEY, id);
				session.setAttribute(Constant.USER_NICK_NAME_SESSION_KEY, user.getNickName());
				session.setAttribute(Constant.USER_EMAIL_SESSION_KEY, user.getEmail());
				session.setAttribute(Constant.USER_TYPE_SESSION_KEY, user.getType());
				session.setAttribute(Constant.USER_PHONE_SESSION_KEY, user.getPhone());
				session.setAttribute(Constant.LOGIN_ACCOUNT_TYPE, "0");
				return generateResponseObject(ResponseCode.SUCCESS_CODE);
			}else{
				return generateResponseObject(responseObject.getCode(), responseObject.getMessage());
			}
		}catch(Exception e){
			log.error("error when admin wants login as user");
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "作为用户身份登录时发生异常");
		}
	}
	@RequestMapping(value = "/user/getNeedFocusCount", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject<String[]> getNeedFocusCount(HttpServletRequest request){
		String userId = StringUtil.obj2String(request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY));
		try{
			return this.userService.getNeedFocusCount(userId);
		}catch(Exception e){
			log.error("根据用户获取异常事件出现异常");
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "根据用户获取异常事件出现异常");
		}
	}
	@RequestMapping(value = "/user/getRealTimeCountInNav", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject<String[]> getRealTimeCountInNav(HttpServletRequest request){
		String userId = StringUtil.obj2String(request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY));
		try{
			return this.userService.getRealTimeCount4UserNav(userId);
		}catch(Exception e){
			log.error("根据用户获取菜单实时数量异常");
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "根据用户获取菜单实时数量异常");
		}
	}
	
	
	
	@RequestMapping(value = "/admin/user/search_admin", method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public ResponseObject<PageSplit<User>> searchAllbykeyword(
			HttpServletRequest request, 
	        @RequestParam(value = "userId", required = false, defaultValue = "") String userId,//会员id号
	        @RequestParam(value = "keyword", required = false, defaultValue = "") String key,//搜索内容
	        @RequestParam(value = "type", required = false, defaultValue = "") String type,//会员等级
	        @RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
			@RequestParam(value = "rows", required = false, defaultValue = "10") int rows) {
		try {
			//key =  new String(key.getBytes("ISO-8859-1"), "utf-8");
			//String groupid = (String)request.getSession().getAttribute(Constant.EMP_GROUPID_SESSION_KEY);
			userId = StringUtil.isEmpty(userId) ? null : userId.trim();
			//String searchColumn = UserUtil.getSearchColumnByType(type);
			if(!StringUtil.isEmpty(type)&&(type.equalsIgnoreCase("-1")))
			{
				type=null;
			}
			
			pageIndex = Math.max(pageIndex, 1);
			return this.userService.searchByadminwithkeyword(userId, key, type, rows, pageIndex);
			//return this.userService.searchByKey(userId, key, searchColumn, defaultPageSize, pageIndex, null);
		} catch (Exception e) {
			log.error("获取会员列表出现异常", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取会员列表失败!");
		}
	}
	
	
	//
	//转运用户标识和用户代码查找
	@RequestMapping(value = "/admin/user/zy_code_search", method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public ResponseObject<PageSplit<User>> searchAllbycode(
			HttpServletRequest request, 
	        @RequestParam(value = "usersinfo", required = false, defaultValue = "") String usersinfo,//查找的用户代码或用户标识
	        @RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,//查找的第几页
			@RequestParam(value = "rows", required = false, defaultValue = "10") int rows){//查找的每页行数
	       // @RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex) {
		try {
			
			pageIndex = Math.max(pageIndex, 1);
			//return this.userService.searchByinfo(usersinfo, defaultPageSize, pageIndex);
			return this.userService.searchByinfouseforzy(usersinfo, rows, pageIndex);
					//.searchByinfo(usersinfo, defaultPageSize, pageIndex);
					
		} catch (Exception e) {
			log.error("获取会员列表出现异常", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取会员列表失败!");
		}
	}
	
	
	//获取冻结资金
	@RequestMapping(value = "/user/get_freezmoney", method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> getfreezemoney(
			HttpServletRequest request){//查找的每页行数
	       // @RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex) {
		try {
			//获取用户的
			String userId = StringUtil.obj2String(request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY));
			if(StringUtil.isEmpty(userId))
			{
				return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作！");
			}
			FreezeMoney free=new FreezeMoney();
			free.setUserId(userId);
			if(!StringUtil.isEmpty(userId))//
			{
				List<FreezeMoney> list=this.freezeMoneyDao.getbyuserId(userId);
				double rmb=0;
				double usa=0;
				for(FreezeMoney f:list)
				{
					rmb+=Double.parseDouble(f.getRmb());
					usa+=Double.parseDouble(f.getUsa());
				}
				
				BigDecimal   rmbb   =   new   BigDecimal(rmb);  
				double   rmbbf1   =   rmbb.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
				
				free.setRmb(Double.toString(rmbbf1));
				
				BigDecimal   usab   =   new   BigDecimal(usa);  
				double   usaf1   =   usab.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
				free.setUsa(Double.toString(usaf1));
			}
			ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			obj.setData(free);
			return obj;
					
		} catch (Exception e) {
			//log.error("获取会员列表出现异常", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取冻结资金发生异常!");
		}
	}
	
	
	//清空用户冻结的资金
	@RequestMapping(value = "/admin/user/clear_freeze", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<Object> rechargeAdminOfMsd(HttpServletRequest request,
			@RequestParam(value = "userId", required = false) String userId) {
		
		String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
		{
			//return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权此操作!");
			String storeMaster = (String)request.getSession().getAttribute(Constant.EMP_STORE_MASTER_SESSION_KEY);//店主标志
			if(!StringUtil.isEmpty(storeMaster)&&(storeMaster.equalsIgnoreCase("1")))//店主可以修改
			{
				
			}
			else
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"对不起，你无权此操作!");
			}
			
		}
		String empno = (String)request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY);//操作人id
		String storeId=(String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);//门店id
		String empName=(String)request.getSession().getAttribute(Constant.EMP_ACCOUNT_SESSION_KEY);//操作人名称
		String storeName=(String)request.getSession().getAttribute(Constant.EMP_STORE_NAME_SESSION_KEY);//操作人门店名称
		if(StringUtil.isEmpty(userId))
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数错误!");
		}
		try {
			List<FreezeMoney> fre=this.freezeMoneyDao.getbyuserId(userId);
			if(fre==null||fre.size()<1)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"没有可清空的数据!");
			}
			double rmb=0;
			double usa=0;
			for(FreezeMoney f:fre)
			{
				if(!StringUtil.isEmpty(f.getRmb()))
				{
					rmb+=Double.parseDouble(f.getRmb());
				}
				if(!StringUtil.isEmpty(f.getUsa()))
				{
					usa+=Double.parseDouble(f.getUsa());
				}
			}
			
			int k=this.freezeMoneyDao.deletebyuserId(userId);
			if(k==0)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"清空失败或没有可清空的数据!");
			}
			
			BigDecimal   rmbb   =   new   BigDecimal(rmb);  
			rmb   =   rmbb.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
			
			BigDecimal   usab   =   new   BigDecimal(usa);  
			usa  =   usab.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
			
			
			String date=DateUtil.date2String(new Date());
			AccountDetail detail = new AccountDetail();
			detail.setAmount(String.valueOf(usa));
			detail.setCreateDate(date);
			detail.setModifyDate(date);
			detail.setState(Constant.ACCOUNT_DETAIL_STATE2);
			detail.setCurrency("美元");
			detail.setName("清空冻结资金");
			detail.setType(Constant.ACCOUNT_DETAIL_TYPE4);//冻结资金
			detail.setConfirm_state("0");
			detail.setUserId(userId);
			
			
			detail.setRemark("清空￥"+rmb+"$"+usa);
			detail.setEmpId(empno);
			detail.setStoreId(storeId);
			detail.setEmpName(empName);
			detail.setEmpStore(storeName);
			this.accountDetailDao.insertAccountDetail(detail);
			
			
			return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"清空成功!");
		} catch (Exception e) {
			
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "清空冻结资金失败");
		}
	
	}

}

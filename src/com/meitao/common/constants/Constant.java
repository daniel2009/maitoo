package com.meitao.common.constants;


public class Constant {
	public static final String PHONE_SEND_CODE = "phone_validate_code";
	public static final String PHONE_KEY = "phone_key";
	public static final String PHONE_SEND_CODE_TIME = "phone_send_code_time";
	
	public static final String PHONE_SEND_REG_CODE = "phone_reg_code";
	
	public static final String PHONE_SEND_RESET_PWD_CODE = "phone_reset_pwd_code";
	public static final String PHONE_RESET_PWD_KEY = "phone_reset_pwd_key";
	public static final String PHONE_SEND_RESET_PWD_CODE_TIME = "phone_send_reset_pwd_code_time";
	
	public static final String SECURITY_CODE_KEY = "";
	public static final String LOGIN_ACCOUNT_TYPE = "login_account_type";

	// user
	public static final String USER_NICK_NAME_SESSION_KEY = "user_nick_name_session_key";
	public static final String USER_ID_SESSION_KEY = "user_id_session_key";
	public static final String USER_EMAIL_SESSION_KEY = "user_email_session_key";
	public static final String USER_TYPE_SESSION_KEY = "user_type_session_key";
	public static final String USER_PHONE_SESSION_KEY = "user_phone_session_key";
	public static final String USER_USERCODE_SESSION_KEY = "user_usercode_session_key";
	public static final String USER_USERALIAS_SESSION_KEY = "user_useralias_session_key";
	public static final String USER_WEIXIN_SCAN_PAY_SESSION_KEY = "user_weixin_scan_pay_session_key";
	//brother
	public static final String BROTHER_TYPE_SESSION_KEY = "brother_type_session_key";

	// employee
	public static final String EMP_ID_SESSION_KEY = "emp_id_session_key";
	public static final String EMP_ACCOUNT_SESSION_KEY = "emp_account_session_key";
	public static final String EMP_STORE_NAME_SESSION_KEY = "emp_store_name";
	public static final String EMP_GROUPID_SESSION_KEY = "emp_groupid_session_key";
	public static final String EMP_STORE_ID_SESSION_KEY = "emp_store_id";
	public static final String EMP_STORE_SUPPERADMIN_SESSION_KEY = "emp_store_supperadmin_kay";
	
	public static final String EMP_STORE_MASTER_SESSION_KEY = "emp_store_master_key";//表示登陆的员工是否是店主
	
	public static final String EMP_STORE_SUPPERADMIN_SESSION_flag = "emp_store_supperadmin_flag";

	public static final String EMAIL_PROPERTIES_FILE = "/email.properties";
	public static final long RESET_PWD_TOKEN_TIME_OF_HOUR = 24L;
	public static final long RESET_PWD_TOKEN_TIME = RESET_PWD_TOKEN_TIME_OF_HOUR * 60 * 60; // 一天，多少秒

	public static final String SYSTEM_PROPERTIES_FILE = "/system.properties";
	public static final String SYSTEM_CN_PROPERTIES_FILE = "/system_cn.properties";
	public static final String SYSTEM_US_PROPERTIES_FILE = "/system_us.properties";
	
	public static final String GLOBALARGS_FLAG_SMS_COMPANY_TITLE_EN = "sms_company_title_en";
	public static final String GLOBALARGS_FLAG_SMS_COMPANY_TITLE_CN = "sms_company_title_cn";
	public static final String GLOBALARGS_FLAG_SMS_COMPANY_SEBSITE_URL = "sms_company_website_url";
	
	public static final String COUNTRY_CODE_US = "US";
	public static final String COUNTRY_CODE_CN = "CN";
	
	
	public static final String USER_TYPE_NORMAL = "0"; // 普通用户
	public static final String USER_TYPE_VIP0 = "1"; // 白银VIP会员
	public static final String USER_TYPE_VIP1 = "2"; // 黄金VIP会员
	public static final String USER_TYPE_VIP2 = "3"; // 白金VIP会员
	public static final String USER_TYPE_VIP3 = "4"; // 钻石VIP会员
	public static final String USER_TYPE_VIP4 = "5"; // 至尊VIP1会员
	public static final String USER_TYPE_VIP5 = "6"; // 至尊VIP2会员
	public static final String USER_TYPE_VIP6 = "7"; // 至尊VIP3会员
	public static final String USER_TYPE_VIP7 = "8"; //至尊VIP4会员

	public static final String ORDER_TYPE_PRICE_ONLINE = "20"; //网上置单价格
	public static final String ORDER_TYPE_PRICE_SM = "21"; //上门收货价格
	
	public static final String USER_REG_TYPE_PHONE="0";//电话注册类型
	public static final String USER_REG_TYPE_EMAIL="1";//邮箱注册类型
	
	public static final String UPLOAD_CARD_TYPE0="0";//表示前端着面上传身份证信息
	
	public static final String USER_EMAIL_STATE0="0";//邮箱注册待验证状态
	public static final String USER_EMAIL_STATE1="1";//邮箱注册已验证状态
	
	public static final String USER_PHONE_STATE0="0";//电话注册待验证状态
	public static final String USER_PHONE_STATE1="1";//电话注册已验证状态

	public static final String TRANSHIPMENT_STATE_10 = "-10"; // 包裹异常
	public static final String TRANSHIPMENT_STATE_9 = "-9"; // 退货中
	public static final String TRANSHIPMENT_STATE_8 = "-8"; // 已退货
	public static final String TRANSHIPMENT_STATE_7 = "-7"; // 转运入库
	public static final String TRANSHIPMENT_STATE_6 = "-6"; // 转运出库
	public static final String TRANSHIPMENT_STATE_5 = "-5"; // 待检入库
	//状态不要取值-1
	public static final String TRANSHIPMENT_STATE0 = "0"; // 未入库
	public static final String TRANSHIPMENT_STATE1 = "1"; // 已揽收
	public static final String TRANSHIPMENT_STATE2 = "2"; // 运单作废
	public static final String TRANSHIPMENT_STATE3 = "3"; // 等待付款
	public static final String TRANSHIPMENT_STATE4 = "4"; // 已付款
	public static final String TRANSHIPMENT_STATE5 = "5"; // 分拣完成
	

	public static final String ORDER_TYPE_KEY_1="1";//门市运单
	public static final String ORDER_TYPE_KEY_2="2";//转运运单
	public static final String ORDER_TYPE_KEY_3="3";//网上置单
	public static final String ORDER_TYPE_KEY_4="4";//第三方运单
	public static final String ORDER_TYPE_KEY_5="5";//上门收货
	public static final String ORDER_TYPE_KEY_6="6";//手写单，空运单
	public static final String ORDER_TYPE_KEY_7="7";//批量生成门市运单
	
	

	
	
	
	
	public static final String TRANSHIPMENT_ROUTE_STATE_CREATE = "0";
	public static final String TRANSHIPMENT_ROUTE_STATE_INTO_WAREHOUSE = "1";
	public static final String TRANSHIPMENT_ROUTE_STATE_OUT_OF_WAREHOUSE = "2";//出库，车运
	public static final String TRANSHIPMENT_ROUTE_STATE_TOKEN_BY_SELF = "3";//被自提
	public static final String TRANSHIPMENT_ROUTE_STATE_FINISH = "4";//成为订单
	
	public static final String TRANSHIPMENT_PRO_STATE0 = "0"; // 待处理状态
	public static final String TRANSHIPMENT_PRO_STATE1 = "1"; // 已处理状态

	public static final String ORDER_SUBMIT_TYPE_MUBOX = "0"; // 合箱操作，正常操作
	public static final String ORDER_SUBMIT_TYPE_PREUNBOX = "1"; // 预拆箱
	public static final String ORDER_MAIL_TYPE1 = "0"; // 邮包
	public static final String ORDER_MAIL_TYPE2 = "1"; // 电商
	public static final String ORDER_MAIL_TYPE3 = "2"; // 快件

	public static final String ORDER_TYPE_WEB = "1"; // 网上发件(转运)
	public static final String ORDER_TYPE_STORE = "2"; // 门市客户发件
	public static final String ORDER_TYPE_VIP = "3"; // vip客户发件
	public static final String ORDER_TYPE_SH = "4"; // 散户发件
	public static final String ORDER_TYPE_ONLINE = "5"; // 网上置件
	public static final String ORDER_TYPE_GROUP = "6"; // 代理会员客户
	public static final String ORDER_TYPE_THIRD = "7"; // 第三方公司导入单号

	public static final String ORDER_ID_PREFIX_WEB = "868"; // 网上发件的运单id前缀
	public static final String ORDER_ID_PREFIX_STORE = "866"; // 门市店发件的运单id前缀
	public static final String ORDER_ID_PREFIX_VIP = "MTV"; // vip发件的运单id前缀
	public static final String ORDER_ID_PREFIX_SH = "686"; // 散户发件的运单id前缀
	public static final String ORDER_ID_PREFIX_UNKNOWN = "MTU"; // 未知发件的运单id前缀


	//kai 20150911-1
	public static final String ORDER_STATE__10 = "-10"; // 这是网上置单的标志
	public static final String ORDER_STATE__9 = "-9"; // 这是网上置单已经收货但没付款的标志

	//kai 20150911-1
	public static final String ORDER_ROUTE__10 = "网上置单"; // 这是网上置单的标志
	public static final String ORDER_ROUTE__9 = "转运置单"; // 转运置单

	public static final String ORDER_PAY_STATE0 = "0"; // 没有支付运单费用
	public static final String ORDER_PAY_STATE1= "1"; // 已经支付运单费用
	
	public static final String ORDER_STATE0 = "-1"; // 这个是空运单标志
	public static final String ORDER_STATE1 = "0"; // 运单作废
	public static final String ORDER_STATE2 = "1"; // 包裹异常
	public static final String ORDER_STATE3 = "2"; // 已揽收
	public static final String ORDER_STATE4 = "3"; // 送往集散中心
	public static final String ORDER_STATE5 = "4"; // 已打件
	public static final String ORDER_STATE6 = "5"; // 送往机场
	public static final String ORDER_STATE7 = "6"; // 空运中
	public static final String ORDER_STATE8 = "7"; // 海关清关
	public static final String ORDER_STATE9 = "8"; // 美淘中国分部已揽收
	public static final String ORDER_STATE10 = "9"; // 收件人已接收
	



	public static final String ORDER_ROUTE_STATE0 = "运单创建";  
	public static final String ORDER_ROUTE_STATE1 = "运单作废";
	public static final String ORDER_ROUTE_STATE2 = "包裹异常";
	public static final String ORDER_ROUTE_STATE3 = "已揽收";//已揽收
	public static final String ORDER_ROUTE_STATE4 = "送往集散中心";//
	public static final String ORDER_ROUTE_STATE5 = "已打件";//已打件
	public static final String ORDER_ROUTE_STATE6 = "送往机场";
	public static final String ORDER_ROUTE_STATE7 = "空运中";
	public static final String ORDER_ROUTE_STATE8 = "清关中";
	public static final String ORDER_ROUTE_STATE9 = "美淘中国分部已揽收";//美淘中国分部揽收
	public static final String ORDER_ROUTE_STATE10 = "收件人已接收";
	public static final String ORDER_ROUTE_STATE_PAY_TO_CHINA = "回国费用扣除";
	public static final String ORDER_ROUTE_STATE_PAY_CHANGE_WAREHOUSE = "美国门店间转移费用扣除";
	
	
	public static final String ORDER_ROUTE_STATE1_en = "Wait For Audit";  
	public static final String ORDER_ROUTE_STATE2_en = "No Payment";
	public static final String ORDER_ROUTE_STATE3_en = "Already In Storage";
	public static final String ORDER_ROUTE_STATE4_en = "Rushed To The Center";
	public static final String ORDER_ROUTE_STATE5_en = "Storage In The Center ";
	public static final String ORDER_ROUTE_STATE6_en = "Rushed To The Airport";
	public static final String ORDER_ROUTE_STATE7_en = "In Air";
	public static final String ORDER_ROUTE_STATE8_en = "Customs Clearance";
	public static final String ORDER_ROUTE_STATE9_en = "Destination Third Party Express";
	public static final String ORDER_ROUTE_STATE10_en = "Recipient Received";
	
	
	
	
	

	public static final String MESSAGE_STATE_EMP_NOT_DEAL = "0"; // 管理员未处理的留言状态
	public static final String MESSAGE_STATE_USER_NOT_DEAL = "1"; // 用户未处理的留言状态

	public static final String ACCOUNT_DETAIL_TYPE11 = "11"; // 表示信用卡充值
	public static final String ACCOUNT_DETAIL_TYPE12 = "12"; // 表示人民币充值,只用于表示支付宝充值
	public static final String ACCOUNT_DETAIL_TYPE13 = "13"; // 后台管理员操作充值
	public static final String ACCOUNT_DETAIL_TYPE2 = "2"; // 表示消费
	public static final String ACCOUNT_DETAIL_TYPE3="3";//员工更改支付状态
	public static final String ACCOUNT_DETAIL_TYPE4="4";//用户提交冻结资金

	public static final String ACCOUNT_DETAIL_STATE1 = "0"; // 待处理
	public static final String ACCOUNT_DETAIL_STATE2 = "1"; // 完成
	public static final String ACCOUNT_DETAIL_STATE3 = "2"; // 操作失败
	public static final String ACCOUNT_DETAIL_STATE4 = "3"; // 修改运单支付状态

	public static final String ERROR_ORDER_STATE1 = "0"; // 表示异常件正在处理中
	public static final String ERROR_ORDER_STATE2 = "1"; // 表示异常件已经处理完成

	public static final String ERROR_TRANSH_STATE1 = "0"; // 表示异常预报单正在处理中
	public static final String ERROR_TRANSH_STATE2 = "1"; // 表示异常预报单已经处理完成
	
	public static final String POSITION_STATE0="0";//仓位没有使用
	public static final String POSITION_STATE1="1";//仓位已使用
	

	public static final String KUAIDI_TYPE_KUAIDI100 = "0";
	public static final String KUAIDI_TYPE_MEITAO = "1"; 
	public static final String KUAIDI_TYPE_DCS = "2"; 
	
	public static final String GLOBALARGS_TYPE_PIC = "0"; //图画类型
	public static final String GLOBALARGS_TYPE_TEXT = "1"; //文本类型
	public static final String GLOBALARGS_TYPE_FOOT = "2"; //页脚,没有使用
	public static final String GLOBALARGS_FLAG_USE_IDCARD = "use_idcard";
	public static final String GLOBALARGS_FLAG_WEIGHT_ROUND_UP = "weight_round_up";
	public static final String GLOBALARGS_FLAG_RETURN_PACKAGE_RETURN_FEE = "return_package_return_fee";
	public static final String GLOBALARGS_FLAG_SEND_EMAIL_SMTP_HOST = "send_email_smtp_host";
	public static final String GLOBALARGS_FLAG_SEND_EMAIL_USER_NAME = "send_email_user_name";
	public static final String GLOBALARGS_FLAG_SEND_EMAIL_PASSWORD = "send_email_password";
	
	
	public static final String GLOBALARGS_FLAG_SEND_EMAIL_TITLE = "send_email_title";
	public static final String GLOBALARGS_FLAG_SEND_EMAIL_WEBSITE = "send_email_website";
	
	public static final String GLOBALARGS_FLAG_SMS_API_KEY = "sms_api_key";
	
	public static final String RENLING_STATE0="0";//未认领状态
	public static final String RENLING_STATE1="1";//已认领状态
	public static final String RENLING_STATE2="2";//已失效
	
	public static final String CLAIM_PERSON_STATE_AUDIT = "3";//待审
	public static final String CLAIM_PERSON_STATE_FAIL = "4";//不通过，可再次认领
	public static final String CLAIM_PERSON_STATE_PASS = "1";//通过
	public static final String CLAIM_PERSON_STATE_INVALID = "2";//失效，当有用户认领成功后，其他人列为该状态
	
	public static final String RENLING_PERSON_USER="0";
	public static final String RENLING_PERSON_ADMIN="1";
	
	public static final String EMAIL_SEND_CODE = "email_validate_code";
	public static final String EMAIL_KEY = "email_key";
	
	public static final String RETURN_PACKAGE_HAS_RECEIPT = "1";
	public static final String RETURN_PACKAGE_STATE_NOT_TREAT = "0";//还没处理
	public static final String RETURN_PACKAGE_STATE_TREAT = "1";//已处理
	public static final String RETURN_PACKAGE_STATE_ISSUE = "2";//有问题
	
	public static final String CALL_ORDER_STATE_NOT_TREAT = "0";//没处理
	public static final String CALL_ORDER_STATE_TREAT = "1";//确认时间
	public static final String CALL_ORDER_STATE_FINISHED = "2";//已完成
	public static final String CALL_ORDER_STATE_ISSUE = "-10";//处理异常
	
	public static final String STORAGE_TYPE_BEFORE_OPEN = "0";
	public static final String STORAGE_TYPE_AFTER_OPEN = "1";
	public static final String STORAGE_TYPE_ORDER = "2";
	

	public static final String AUTO_SEND_FORGET_PASSWORD = "%forget%password%";
	public static final String AUTO_SEND_REGISTER_CODE = "%register%code%";
	public static final String AUTO_SEND_ORDER_STATE1 = "%order%state%1";
	public static final String AUTO_SEND_ORDER_STATE2 = "%order%state%2";
	public static final String AUTO_SEND_ORDER_STATE3 = "%order%state%3";
	public static final String AUTO_SEND_ORDER_STATE4 = "%order%state%4";
	public static final String AUTO_SEND_ORDER_STATE5 = "%order%state%5";
	public static final String AUTO_SEND_ORDER_STATE6 = "%order%state%6";
	public static final String AUTO_SEND_ORDER_STATE7 = "%order%state%7";
	public static final String AUTO_SEND_ORDER_STATE8 = "%order%state%8";
	public static final String AUTO_SEND_ORDER_STATE9 = "%order%state%9";
	public static final String AUTO_SEND_ORDER_STATE10 = "%order%state%10";
	public static final String AUTO_SEND_NEW_USER_PASSWORD = "%new%user%";
	
	public static final String SPIDER_ORDER_STATE_AUDIT = "0";
	public static final String SPIDER_ORDER_STATE_ISSUE = "1";
	public static final String SPIDER_ORDER_STATE_FINISHED = "2";
	
	public static final String PRODUCT_RECORD_STATE_CREATE = "0";
	public static final String PRODUCT_RECORD_STATE_ISSUE = "1";
	public static final String PRODUCT_RECORD_STATE_FINISHED = "2";
	
	public static final String STORAGE_TYPE0="0";//转运州仓位划分
	public static final String STORAGE_TYPE1="1";//本地转运仓位划分
	public static final String STORAGE_TYPE2="2";//运单转运仓位划分
	
	public static final String VERFY_CARDID_0="0";//身份证未验证
	public static final String VERFY_CARDID_1="1";//身份证已验证
	public static final String VERFY_CARDID_2="2";//身份证验证未通过
	public static final String VERFY_CARDID__10="-10";//不进行验证
	
	public static final String T_ORDER_TYPE0 = "0"; // 本地州入库的转运单
	public static final String T_ORDER_TYPE1 = "1"; // 转运州入库的转运单
	
	
	public static final String T_ORDER_I_STATE0 = "0"; // 本地并没有入库
	public static final String T_ORDER_I_STATE1 = "1"; //本地已经入库
	
	public static final String T_ORDER_STATE0 = "0"; // 用户预报
	public static final String T_ORDER_STATE1 = "1"; //录入失败
	public static final String T_ORDER_STATE2 = "2"; //包裹异常
	public static final String T_ORDER_STATE3 = "3"; //转运入库
	public static final String T_ORDER_STATE4 = "4"; //转运出库
	public static final String T_ORDER_STATE5 = "5"; //已入库
	//public static final String T_ORDER_STATE6 = "6"; //待处理
	//public static final String T_ORDER_STATE7 = "7"; //待付款
	//public static final String T_ORDER_STATE8 = "8"; //用户已付款
	//public static final String T_ORDER_STATE9 = "9"; //已完成
	public static final String T_ORDER_STATE6 = "6"; //已完成
	public static final String T_ORDER_STATE7 = "7"; //已自提


	public static final String T_ORDER_ROUTE_STATE0 = "用户预报";
	public static final String T_ORDER_ROUTE_STATE1 = "录入失败";
	public static final String T_ORDER_ROUTE_STATE2 = "包裹异常";
	public static final String T_ORDER_ROUTE_STATE3 = "转运入库";
	public static final String T_ORDER_ROUTE_STATE4 = "转运出库";
	public static final String T_ORDER_ROUTE_STATE5 = "已入仓库";
	//public static final String T_ORDER_ROUTE_STATE6 = "待处理";
	public static final String T_ORDER_ROUTE_STATE6 = "已完成";
	public static final String T_ORDER_ROUTE_STATE7 = "已自提";
	//public static final String T_ORDER_ROUTE_STATE7 = "待付款";
	//public static final String T_ORDER_ROUTE_STATE8 = "用户已付款";
	//public static final String T_ORDER_ROUTE_STATE9 = "已完成";
	//public static final String T_ORDER_ROUTE_STATE10 = "已自提";
	
	public static final String CHANNEL_TYRE0="0";//不支付转运的渠道
	public static final String CHANNEL_TYRE1="1";//支付转运的渠道
	
	public static final String USER_PAY_CONFIRM_0="0";//未确认
	public static final String USER_PAY_CONFIRM_1="1";//已确认
	
	
	public static final String SEND_MESSAGE_TYPE0="0";//发送揽收记录短信
	public static final String SEND_MESSAGE_TYPE1="1";//状态变更通知
	public static final String SEND_MESSAGE_TYPE2="2";//状态变身份证通知
	public static final String SEND_MESSAGE_TYPE3="3";//航班变更通知
	public static final String SEND_MESSAGE_TYPE4="4";//航班变更身份证通知
	public static final String SEND_MESSAGE_TYPE5="5";//门店单短信通知
	public static final String SEND_MESSAGE_TYPE6="6";//用户注册短信
	public static final String SEND_MESSAGE_TYPE7="7";//用户找回密码短信
}
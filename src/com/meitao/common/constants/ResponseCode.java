package com.meitao.common.constants;

/**
 * 返回的code类<br/>
 * 描述的是后台给层次之间调用返回的code值。
 * 
 */
public class ResponseCode {
	public static final String SUCCESS_CODE = "200";
	public static final String SHOW_EXCEPTION = "600";
	public static final String REQUEST_ALL_EXCEPTION = "601";
	public static final String PARAMETER_ERROR = "603";
	public static final String VALIDATE_CODE_ERROR = "604";
	public static final String TOKEN_ERROR = "605";
	public static final String TOKEN_INSERT_ERROR = "606";
	public static final String NEED_LOGIN="607";

	public static final String BROTHER_LOGIN="608";
	
	
	public static final String EMPLOYEE_ID_ERROR = "11001";
	public static final String EMPLOYEE_PASSWORD_ERROR = "11002";
	public static final String EMPLOYEE_ACCOUNT_ERROR = "11003";
	public static final String EMPLOYEE_STORE_ID_ERROR = "11004";
	public static final String EMPLOYEE_STORE_NAME_ERROR = "11005";
	public static final String EMPLOYEE_PHONE_ERROR = "11006";
	public static final String EMPLOYEE_PIC_ERROR = "11007";
	public static final String EMPLOYEE_DB_NO_VALUE_BY_ACCOUNT_NAME = "11008";
	public static final String EMPLOYEE_INSERT_FAILURE = "11009";
	public static final String EMPLOYEE_DELETE_FAILURE = "11010";
	public static final String EMPLOYEE_UPDATE_FAILURE = "11011";
	public static final String EMPLOYEE_ID_NOT_EXISTS = "11012";
	public static final String EMPLOYEE_ACCOUNT_NOT_EXISTS = "11013";
	public static final String EMPLOYEE_ACCOUNT_EXISTS = "11014";

	// 会员
	public static final String USER_ID_EMPTY = "12000";
	public static final String USER_ID_ERROR = "12001";
	public static final String USER_NICK_NAME_ERROR = "12002";
	public static final String USER_PASSWORD_ERROR = "12003";
	public static final String USER_REAL_NAME_ERROR = "12004";
	public static final String USER_PHONE_ERROR = "12005";
	public static final String USER_EMAIL_ERROR = "12006";
	public static final String USER_QQ_ERROR = "12007";
	public static final String USER_RECOMMENDER_ERROR = "12008";
	public static final String USER_COUNTRY_ERROR = "12009";
	public static final String USER_ADDRESS_ERROR = "12020";
	public static final String USER_ID_EXISTS = "12010";
	public static final String USER_NAME_EXISTS = "12011";
	public static final String USER_NAME_NOT_EXISTS = "12012";
	public static final String USER_EMAIL_EXISTS = "12013";
	public static final String USER_EMAIL_NOT_EXISTS = "12014";
	public static final String USER_LOGIN_ACCOUNT_EMPTY = "12015";
	public static final String USER_LOGIN_ACCOUNT_ERROR = "12016";
	public static final String USER_INSERT_ERROR = "12017";
	public static final String USER_DELETE_ERROR = "12018";
	public static final String USER_MODIFY_FAILURE = "12019";
	
	public static final String USER_PHONE_EXISTS = "12020";
	public static final String USER_PHONE_NOT_EXISTS = "12021";
	
	public static final String NAME_OR_PHONE_NOT_EXIST_OF_USER = "12022";
	public static final String USER_BALANCE_NOT_ENOUGH = "12023";

	// 收件人
	public static final String CONSIGNEE_DELETE_FAILURE = "13001";
	public static final String CONSIGNEE_MODIFY_FAILURE = "13002";
	public static final String CONSIGNEE_INSERT_FAILURE = "13003";
	public static final String CONSIGNEE_ID_ERROR = "13004";
	public static final String CONSIGNEE_NAME_ERROR = "13005";
	public static final String CONSIGNEE_PROVINCE_ERROR = "13006";
	public static final String CONSIGNEE_CITY_ERROR = "13007";
	public static final String CONSIGNEE_DISTRICT_ERROR = "13008";
	public static final String CONSIGNEE_STREET_ADDRESS_ERROR = "13009";
	public static final String CONSIGNEE_ZIP_CODE_ERROR = "13010";
	public static final String CONSIGNEE_PHONE_ERROR = "13011";
	public static final String CONSIGNEE_TELEPHONE_ERROR = "13012";
	public static final String CONSIGNEE_CARD_ID_ERROR = "13013";
	public static final String CONSIGNEE_CARD_ERROR = "13014";
	
	public static final String CONSIGNEE_CARD_LIB_ERROR = "13015";//身份证库存错误

	// 留言
	public static final String MESSAGE_INSERT_FAILURE = "14001";
	public static final String MESSAGE_CONTENT_ERROR = "14002";
	public static final String MESSAGE_FATHER_ID_ERROR = "14003";

	// 新闻
	public static final String NEWS_INSERT_FAILURE = "15001";
	public static final String NEWS_DELETE_FAILURE = "15002";
	public static final String NEWS_MODIFY_FAILURE = "15003";
	public static final String NEWS_NOT_EXISTS = "15004";
	public static final String NEWS_ID_ERROR = "15005";
	public static final String NEWS_PICUTER_FAILURE = "15006";
	
	// 公告
	public static final String GONGGAO_INSERT_FAILURE = "15051";
	public static final String GONGGAO_DELETE_FAILURE = "15052";
	public static final String GONGGAO_MODIFY_FAILURE = "15053";
	public static final String GONGGAO_NOT_EXISTS = "15054";
	public static final String GONGGAO_ID_ERROR = "15055";

	// 友好链接
	public static final String LINK_INSERT_FAILURE = "16001";
	public static final String LINK_DELETE_FAILURE = "16002";
	public static final String LINK_MODIFY_FAILURE = "16003";
	public static final String LINK_NOT_EXISTS = "16004";
	public static final String LINK_ID_ERROR = "16005";

	// 转运单/预报单
	public static final String TRANSHIPMENT_INSERT_ERROR = "17001";
	public static final String TRANSHIPMENT_MODIFY_ERROR = "17002";
	public static final String TRANSHIPMENT_ID_NOT_EXISTS = "17003";
	public static final String TRANSHIPMENT_ID_ERROR = "17004";
	public static final String TRANSHIPMENT_COMMODITY_ERROR = "17005";
	public static final String TRANSHIPMENT_TRANSIT_NO_ERROR = "17006";
	public static final String TRANSHIPMENT_TRANSIT_COMPANY_ERROR = "17007";
	public static final String TRANSHIPMENT_FROM_USER_ERROR = "17008";
	public static final String TRANSHIPMENT_REMARK_ERROR = "17009";
	public static final String TRANSHIPMENT_PARCEL_VALUE_ERROR = "17010";
	public static final String TRANSHIPMENT_ETA_ERROR = "17011";
	public static final String TRANSHIPMENT_WEIGHT_ERROR = "17012";
	public static final String TRANSHIPMENT_THRIFF_ERROR = "17013";
	public static final String TRANSHIPMENT_OR_ERROR = "17014";
	public static final String TRANSHIPMENT_ADD_AFTER_CLAIM_PACKAGE_ERROR = "17015";
	public static final String TRANSHIPMENT_CREATE_PREORDER_ERROR = "17016";
	public static final String TRANSHIPMENT_UPDATE_AUDIT_ERROR = "17017";
	public static final String TRANSHIPMENT_EMPTY_LIST = "17018";
	public static final String TRANSHIPMENT_ROUTE_INSERT_ERROR = "17019";

	public static final String ORDER_DELETE_ERROR = "18001";
	public static final String ORDER_INSERT_ERROR = "18002";
	public static final String ORDER_NUMBER_ERROR = "18003";
	public static final String ORDER_IS_SUBMITED = "18004";
	public static final String ORDER_PAY_ACCOUNT_NOT_MONEY = "18005";
	public static final String ORDER_UPDATE_STATE_ERROR = "18006";
	public static final String ORDER_EMPTY_LIST = "18007";
	
	public static final String WAREHOUSE_ID_ERROR = "19001";
	public static final String WAREHOUSE_NAME_ERROR = "19002";
	public static final String WAREHOUSE_COMPANY_ERROR = "19003";
	public static final String WAREHOUSE_COUNTRY_ERROR = "19004";
	public static final String WAREHOUSE_PROVINCE_ERROR = "19005";
	public static final String WAREHOUSE_CITY_ERROR = "19006";
	public static final String WAREHOUSE_ADDRESS_ERROR = "19007";
	public static final String WAREHOUSE_ZIP_CODE_ERROR = "19008";
	public static final String WAREHOUSE_TELEPHONE_ERROR = "19009";
	public static final String WAREHOUSE_CONTACT_NAME_ERROR = "19010";
	public static final String WAREHOUSE_SERIAL_NO_ERROR = "19011";
	public static final String WAREHOUSE_REMARK_ERROR = "19012";
	public static final String WAREHOUSE_CREATE_DATE_ERROR = "19013";
	public static final String WAREHOUSE_INSERT_FAILURE = "19014";
	public static final String WAREHOUSE_MODIFY_FAILURE = "19015";
	
	public static final String ACCOUNT_INSERT_FAILURE = "20001";
	public static final String ACCOUNT_RECHARGE_RMB_FAILURE = "20002";
	public static final String ACCOUNT_SCAN_PAY_NOT_COMPLETE = "20003";//仅仅表示没检测到用户付款,并非出错
	public static final String ACCOUNT_SCAN_PAY_NOT_EXISTS = "20004";//找不到对应要添加的扫码充值信息session里面
	public static final String ACCOUNT_UPDATE_BY_PAYMENT_EXCEPTION = "20005";
	public static final String ACCOUNT_BALANCE_NOT_ENOUGH = "20006";

	//商品类型
	public static final String COMMODITY_UPDATE_FAILURE = "21001";
	public static final String COMMODITY_INSTER_FAILURE = "21002";
	
	//上门取件
	public static final String CALL_ORDER_UPDATE_FAILURE = "22001";
	public static final String CALL_ORDER_INSERT_FAILURE = "22002";
	public static final String CALL_ORDER_DELETE_FAILURE = "22003";
	public static final String CALL_ORDER_MODIFY_FAILURE = "22004";
	public static final String CALL_ORDER_ID_ERROR = "22005";
	public static final String CALL_ORDER_NAME_ERROR = "22006";
	public static final String CALL_ORDER_PROVINCE_ERROR = "22007";
	public static final String CALL_ORDER_CITY_ERROR = "22008";
	public static final String CALL_ORDER_DISTRICT_ERROR = "22009";
	public static final String CALL_ORDER_STREET_ADDRESS_ERROR = "22010";
	public static final String CALL_ORDER_ZIP_CODE_ERROR = "22011";
	public static final String CALL_ORDER_PHONE_ERROR = "22012";
	public static final String CALL_ORDER_LIST_EMPTY = "22013";
	
	//渠道
	public static final String CHANNEL_UPDATE_FAILURE = "23001";
	public static final String CHANNEL_INSTER_FAILURE = "23002";
	
	public static final String ORDER_RECEIVER_ERROR = "24001";	
	

	
	//海淘推荐 oceanStore
	public static final String OCEAN_STORE_INSERT_FAIL = "25001";
	public static final String OCEAN_STORE_ID_NOT_EXISTS = "25002";
	public static final String OCEAN_STORE_UPDATE_FAIL = "25003";
	public static final String OCEAN_STORE_DELETE_FAIL = "25004";
	public static final String OCEAN_STORE_PICTURE_ERROR = "25005";
	
	//认领包裹claimPackage/renlingbaoguo
	public static final String CLAIM_PACKAGE_ID_NO_EXISTS = "26001";
	public static final String CLAIM_PACKAGE_UPDATE_STATE_FAIL = "26002";
	public static final String CLAIM_PACKAGE_FIND_BY_NOT_CLAIMED_FAIL = "26003";
	//认领记录claimPerson/renlingPerson
	public static final String CLAIM_PERSON_INSERT_FAIL = "27001";
	public static final String CLAIM_PERSON_UPDATE_FAIL = "27002";
	public static final String CLAIM_PERSON_PICTURE_ERROR = "27003";
	public static final String CLAIM_PERSON_LAST_CLAIMED_NOT_EXISTS = "27004";
	public static final String CLAIM_PERSON_ID_NO_EXISTS = "27005";
	public static final String CLAIM_PERSON_UPDATE_REMARK_FAIL = "27006";
	public static final String CLAIM_PERSON_DELETE_FAIL = "27007";
	public static final String CLAIM_PERSON_UPDATE_BY_RECLAIM_FAIL = "27008";

	public static final String RETURN_PACKAGE_PICTURE_ERROR = "28001";
	public static final String RETURN_PACKAGE_INSERT_ERROR = "28002";
	public static final String RETURN_PACKAGE_AUDIT_ERROR = "28003";
	public static final String RETURN_PACKAGE_FIND_BY_ID_ERROR = "28004";
	public static final String RETURN_PACKAGE_UPDATE_BY_USER_ERROR = "28005";
	public static final String RETURN_PACKAGE_CHECK_STATE_ERROR = "28006";
	public static final String RETURN_PACKAGE__EMPTY_LIST = "28007";
	
	public static final String ALIPAY_SELLER_LIST_IS_EMPTY = "29001";
	public static final String ALIPAY_SELLER_INSERT_ERROR = "29002";
	public static final String ALIPAY_SELLER_UPDATE_ERROR = "29003";
	public static final String ALIPAY_SELLER_DELETE_ERROR = "29004";

	public static final String WAREHOUSE_FLYINFO_FAILURE = "30000";

	
	public static final String STORAGE_POSITION_USER_ALREADY_HAS = "31001";
	public static final String STORAGE_POSITION_UPDATE_BY_USE_FAIL = "31002";
	public static final String STORAGE_POSITION_NO_EXIST = "31003";
	public static final String STORAGE_POSITION_INSERT_ERROR = "31004";
	public static final String STORAGE_INSERT_ERROR = "31005";
	public static final String STORAGE_LIST_ERROR = "31006";
	public static final String STORAGE_LIST_IS_EMPTY = "31007";
	public static final String STORAGE_DELETE_ERROR = "31008";
	public static final String STORAGE_POSITION_DELETE_ERROR = "31009";
	public static final String STORAGE_POSITION_OF_EMPTY_IN_WAREHOUSE_NOT_EXISTS = "31010";
	public static final String STORAGE_POSITION_OF_EMPTY_IN_STORAGE_NOT_EXISTS = "31011";
	public static final String STORAGE_POSITION_RECORD_DELETE_BY_RELATE_FAIL = "31012";
	
	public static final String GLOBALARGS_FLAG_NOT_EXISTS = "32001";
	
	public static final String SEND_EMAIL_NONE_SUCCESS = "33001";
	
	public static final String AUTO_SEND_EMPTY_LIST = "34001";
	public static final String AUTO_SEND_INSERT_ERROR = "34002";
	public static final String AUTO_SEND_UPDATE_ERROR = "34003";
	public static final String AUTO_SEND_DELETE_ERROR = "34004";
	public static final String AUTO_SEND_NOT_EXISTS = "34005";
	
	public static final String SPIDER_ORDER_NOT_EXISTS = "35001";
	public static final String SPIDER_ORDER_INSERT_FAIL = "35002";
	public static final String SPIDER_ORDER_DELETE_FAIL = "35003";
	public static final String SPIDER_ORDER_UPDATE_FAIL = "35004";
	public static final String SPIDER_ORDER_EMPTY_LIST = "35005";
	
	public static final String PRODUCT_TYPE_INSERT_FAIL = "36001";
	public static final String PRODUCT_TYPE_NO_EXISTS = "36002";
	public static final String PRODUCT_TYPE_UPDATE_FAIL = "36003";
	public static final String PRODUCT_TYPE_GET_LIST_FAIL = "36004";
	public static final String PRODUCT_TYPE_EMPTY_LIST = "36005";
	public static final String PRODUCT_TYPE_DELETE_FAIL = "36006";
	public static final String PRODUCT_INSERT_FAIL = "36007";
	public static final String PRODUCT_NO_EXISTS = "36008";
	public static final String PRODUCT_UPDATE_FAIL = "36009";
	public static final String PRODUCT_DELETE_FAIL = "36010";
	public static final String PRODUCT_EMPTY_LIST = "36011";
	public static final String PRODUCT_GET_LIST_FAIL = "36012";
	public static final String PRODUCT_REMARK_UPDATE_FAIL = "36013";
	public static final String PRODUCT_RECORD_GET_LIST_FAIL = "36014";
	public static final String PRODUCT_RECORD_EMPTY_LIST = "36015";
	public static final String CART_NO_EXISTS = "36016";
	public static final String CART_DELETE_FAIL = "36017";
	public static final String PRODUCT_REMARK_STATE_NOT_CORRECT = "36018";
	public static final String CART_LIST_EMPTY = "36019";
	public static final String PRODUCT_RECORD_INSERT_FAIL = "36020";
	public static final String PRODUCT_RECORD_DELETE_FAIL = "36021";
	public static final String PRODUCT_QUANTITY_NOT_ENOUGH = "36022";
	public static final String PRODUCT_RECORD_UPDATE_FAIL = "36023";
	
	public static final String TEST_EXCEPTION = "37001";
	
	public static final String UPLOAD_IMG_ERROR = "38001";
	
	public static final String M_ORDER_OLINR_ERROR="40000";//表示在线置单不是本门店的运单
	public static final String sr_address_ERROR="40001";//表示发件或发件地址簿中已存在相应的电话信息
	public static final String HUITONG_ORDERID_REP="41000";//汇通单号已经打印过了
}
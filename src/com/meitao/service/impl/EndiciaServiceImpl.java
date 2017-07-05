package com.meitao.service.impl;

import java.io.File;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;


import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.meitao.dao.EndicialArgDao;

import com.meitao.service.EndiciaService;

import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;

import com.meitao.common.endicia.endicia_interface;

import com.meitao.common.util.ExceptionUtil;

import com.meitao.common.util.StringUtil;
import com.meitao.exception.ServiceException;
import com.meitao.model.Account;
import com.meitao.model.AccountDetail;
import com.meitao.model.EndiciaLabel;
import com.meitao.model.Endicial_arg;
import com.meitao.model.ExportEndiciaResult;

import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.User;

import javax.xml.bind.DatatypeConverter;

import java.io.FileOutputStream;
import java.math.BigDecimal;

@Service("endiciaService")
public class EndiciaServiceImpl extends BasicService implements EndiciaService {
	@Autowired
	private com.meitao.dao.globalargsDao globalargsDao;

	@Autowired
	private com.meitao.dao.UserDao userDao;

	@Autowired
	private com.meitao.dao.AccountDao accountDao;

	@Autowired
	private com.meitao.dao.AccountDetailDao accountDetailDao;
	
	@Autowired
	private com.meitao.dao.EndicialLabelDao endicialLabelDao;
	
	

	@Value(value = "${save_endicia_printlabel}")
	private String saveendicia_printlabel;
	
	@Autowired
	private EndicialArgDao endicialArgDao;

	public ResponseObject<Object> checkediciaprices(EndiciaLabel label)
			throws ServiceException {
		if (label == null) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
					"参数无效");
		}

		if (StringUtil.isEmpty(label.getUserId())) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
					"参数无效");
		}

		try {

			endicia_interface endicia = new endicia_interface();
			//Endicialinfo info = new Endicialinfo();
			Endicial_arg info=this.endicialArgDao.getone();
			if(info==null)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
						"获得取配置参数失败");
			}
			
			/*info.setAccountID(globalargsDao
					.getcontentbyflag("Endicia_AccountID"));
			info.setAPI_url(globalargsDao
					.getcontentbyflag("Endicia_Label_API_Url"));
			info.setImageFormat(globalargsDao
					.getcontentbyflag("Endicia_Print_ImageFormat"));
			info.setLabelSize(globalargsDao
					.getcontentbyflag("Endicia_Print_LabelSize"));
			info.setPassPhrase(globalargsDao
					.getcontentbyflag("Endicia_PassPhrase"));
			info.setRequesterID(globalargsDao
					.getcontentbyflag("Endicia_RequesterID"));
			info.setTest(globalargsDao.getcontentbyflag("Endicia_Print_test"));*/
			
			
			String price = endicia.checklabelprice(info, label);
			if (price != "") {
				ResponseObject<Object> obj = new ResponseObject<Object>(
						ResponseCode.SUCCESS_CODE);
				//Double price1 = Double.valueOf(price);
				
				
				User user=this.userDao.getUserById(label.getUserId());
				if(user==null)
				{
					obj.setCode(ResponseCode.PARAMETER_ERROR);
					obj.setMessage("获取用户信息出错!");
					return obj;
				}
				if(StringUtil.isEmpty(user.getType()))
				{
					obj.setCode(ResponseCode.PARAMETER_ERROR);
					obj.setMessage("获取用户类型出错!");
					return obj;
				}
				//String rate=get_Endicia_Get_User_Profit(user.getType());
				//Double revprice=Double.valueOf(price)*(1+Double.valueOf(rate));
				
				String rate=get_Endicia_Get_User_Profit_arg(user.getType(),info.getUserprice());
				if(StringUtil.isEmpty(rate))
				{
					rate="1";
				}
				double revprice=0;
				try{
					revprice=Double.valueOf(price)*(Double.valueOf(rate));
					if(revprice<=0)
					{
						obj.setCode(ResponseCode.PARAMETER_ERROR);
						obj.setMessage("计费参数错误!");
						return obj;
					}
				}
				catch(Exception e)
				{
					obj.setCode(ResponseCode.SHOW_EXCEPTION);
					obj.setMessage("计费参数错误!");
					return obj;
				}
				
				
				BigDecimal   b   =   new   BigDecimal(revprice);  //
				double revprice1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); //四舍五入取两位
				Account account_u=this.accountDao.getAccountByUserId(label.getUserId());
				if(account_u==null)
				{
					obj.setCode(ResponseCode.PARAMETER_ERROR);
					obj.setMessage("获取用户帐号信息!");
					return obj;
				}
				double renm=Double.valueOf(user.getRmbBalance());//人民币余额
				double usam=Double.valueOf(user.getUsdBalance());//美元余额
				
				if(revprice1<=usam)//美元有足够的余额
				{
					obj.setData(revprice1);
				}
				else
				{
					double rate0=Double.valueOf(this.globalargsDao.getcontentbyflag("cur_usa_cn"));//获取美元汇率
					//double renmtousd=(renm/rate0);
					//BigDecimal   bn   =   new   BigDecimal(renmtousd);  //
					 //renmtousd   =   bn.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); //四舍五入取两位
					
					if((usam*usam+renm)>=revprice1*rate0)
					{
						obj.setData(revprice1);
					}
					else
					{
						obj.setCode(ResponseCode.PARAMETER_ERROR);
						obj.setMessage("你的余额不足，请充值后使用,包裹价格为 "+revprice1+" 美元!");
						return obj;
					}
					
				}
				
				

				obj.setCode(ResponseCode.SUCCESS_CODE);
				return obj;
			} else {

				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
						"获取价格出错，请检查参数是否正确.");
			}
			/*
			 * int iresult = 0; if (iresult > 0) { return new
			 * ResponseObject<Object>(ResponseCode.SUCCESS_CODE); } else {
			 * return new
			 * ResponseObject<Object>(ResponseCode.NEWS_INSERT_FAILURE,
			 * "插入新闻失败"); }
			 */
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException("发生异常，"+e.getMessage());
		}

	}

	
	
	
	public ResponseObject<Object> checkediciapricesbyadminnew(EndiciaLabel label)
			throws ServiceException {
		if (label == null) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
					"参数无效");
		}

		if (StringUtil.isEmpty(label.getUserId())) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
					"参数无效");
		}

		try {

			endicia_interface endicia = new endicia_interface();
			//Endicialinfo info = new Endicialinfo();
			Endicial_arg info=this.endicialArgDao.getone();
			if(info==null)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
						"获得取配置参数失败");
			}
			
			/*info.setAccountID(globalargsDao
					.getcontentbyflag("Endicia_AccountID"));
			info.setAPI_url(globalargsDao
					.getcontentbyflag("Endicia_Label_API_Url"));
			info.setImageFormat(globalargsDao
					.getcontentbyflag("Endicia_Print_ImageFormat"));
			info.setLabelSize(globalargsDao
					.getcontentbyflag("Endicia_Print_LabelSize"));
			info.setPassPhrase(globalargsDao
					.getcontentbyflag("Endicia_PassPhrase"));
			info.setRequesterID(globalargsDao
					.getcontentbyflag("Endicia_RequesterID"));
			info.setTest(globalargsDao.getcontentbyflag("Endicia_Print_test"));*/
			
			
			String price = endicia.checklabelprice(info, label);
			if (price != "") {
				ResponseObject<Object> obj = new ResponseObject<Object>(
						ResponseCode.SUCCESS_CODE);
				//Double price1 = Double.valueOf(price);
				
				
				User user=this.userDao.getUserById(label.getUserId());
				if(user==null)
				{
					if(label.getPayway().equalsIgnoreCase("0"))
					{
						obj.setCode(ResponseCode.PARAMETER_ERROR);
						obj.setMessage("用户不存在，无法进行余额支付!");
						return obj;
					}
				}
				else
				{
					if(StringUtil.isEmpty(user.getType()))
					{
						obj.setCode(ResponseCode.PARAMETER_ERROR);
						obj.setMessage("获取用户类型出错!");
						return obj;
					}
				}
				//String rate=get_Endicia_Get_User_Profit(user.getType());
				//Double revprice=Double.valueOf(price)*(1+Double.valueOf(rate));
				
				String rate=get_Endicia_Get_User_Profit_arg(user.getType(),info.getUserprice());
				if(StringUtil.isEmpty(rate))
				{
					rate="1";
				}
				double revprice=0;
				try{
					revprice=Double.valueOf(price)*(Double.valueOf(rate));
					if(revprice<=0)
					{
						obj.setCode(ResponseCode.PARAMETER_ERROR);
						obj.setMessage("计费参数错误!");
						return obj;
					}
				}
				catch(Exception e)
				{
					obj.setCode(ResponseCode.SHOW_EXCEPTION);
					obj.setMessage("计费参数错误!");
					return obj;
				}
				
				
				BigDecimal   b   =   new   BigDecimal(revprice);  //
				double revprice1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); //四舍五入取两位
				Account account_u=this.accountDao.getAccountByUserId(label.getUserId());
				if(account_u==null)
				{
					obj.setCode(ResponseCode.PARAMETER_ERROR);
					obj.setMessage("获取用户帐号信息!");
					return obj;
				}
				double renm=Double.valueOf(user.getRmbBalance());//人民币余额
				double usam=Double.valueOf(user.getUsdBalance());//美元余额
				
				if(revprice1<=usam)//美元有足够的余额
				{
					obj.setData(revprice1);
				}
				else
				{
					double rate0=Double.valueOf(this.globalargsDao.getcontentbyflag("cur_usa_cn"));//获取美元汇率
					//double renmtousd=(renm/rate0);
					//BigDecimal   bn   =   new   BigDecimal(renmtousd);  //
					 //renmtousd   =   bn.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); //四舍五入取两位
					
					if((usam*usam+renm)>=revprice1*rate0)
					{
						obj.setData(revprice1);
					}
					else
					{
						obj.setCode(ResponseCode.PARAMETER_ERROR);
						obj.setMessage("你的余额不足，请充值后使用,包裹价格为 "+revprice1+" 美元!");
						return obj;
					}
					
				}
				
				

				obj.setCode(ResponseCode.SUCCESS_CODE);
				return obj;
			} else {

				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
						"获取价格出错，请检查参数是否正确.");
			}
			/*
			 * int iresult = 0; if (iresult > 0) { return new
			 * ResponseObject<Object>(ResponseCode.SUCCESS_CODE); } else {
			 * return new
			 * ResponseObject<Object>(ResponseCode.NEWS_INSERT_FAILURE,
			 * "插入新闻失败"); }
			 */
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException("发生异常，"+e.getMessage());
		}

	}
	
	
	public ResponseObject<Object> checkediciapricesbyadmin(EndiciaLabel label)
			throws ServiceException {
		if (label == null) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
					"参数无效");
		}

	

		try {

			endicia_interface endicia = new endicia_interface();
			//Endicialinfo info = new Endicialinfo();
			Endicial_arg info=this.endicialArgDao.getone();
			/*info.setAccountID(globalargsDao
					.getcontentbyflag("Endicia_AccountID"));
			info.setAPI_url(globalargsDao
					.getcontentbyflag("Endicia_Label_API_Url"));
			info.setImageFormat(globalargsDao
					.getcontentbyflag("Endicia_Print_ImageFormat"));
			info.setLabelSize(globalargsDao
					.getcontentbyflag("Endicia_Print_LabelSize"));
			info.setPassPhrase(globalargsDao
					.getcontentbyflag("Endicia_PassPhrase"));
			info.setRequesterID(globalargsDao
					.getcontentbyflag("Endicia_RequesterID"));
			info.setTest(globalargsDao.getcontentbyflag("Endicia_Print_test"));*/
			String price = endicia.checklabelprice(info, label);
			if (price != "") {
				ResponseObject<Object> obj = new ResponseObject<Object>(
						ResponseCode.SUCCESS_CODE);
				
				String rate=this.globalargsDao.getcontentbyflag("Endicia_Get_User_Profit_byadmin");
				price=Double.toString((Double.valueOf(rate)+1)*Double.valueOf(price));
				
			    obj.setData(price);
				
				

				obj.setCode(ResponseCode.SUCCESS_CODE);
				return obj;
			} else {

				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
						"参数无效");
			}
			/*
			 * int iresult = 0; if (iresult > 0) { return new
			 * ResponseObject<Object>(ResponseCode.SUCCESS_CODE); } else {
			 * return new
			 * ResponseObject<Object>(ResponseCode.NEWS_INSERT_FAILURE,
			 * "插入新闻失败"); }
			 */
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}

	}
	
	public ResponseObject<Object> printlabel(EndiciaLabel label,
			HttpServletRequest request) throws ServiceException {
		if (label == null) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
					"参数无效");
		}

		if (StringUtil.isEmpty(label.getUserId())) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
					"参数无效");
		}

		try {
			ResponseObject<Object> priceobj = this.checkediciaprices(label);
			double price;
			if((priceobj!=null)&&(priceobj.getCode().equalsIgnoreCase(ResponseCode.SUCCESS_CODE)))
			{
				price=Double.valueOf(priceobj.getData().toString());
			}
			else
			{
				ResponseObject<Object> returnobj= new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
						"打印失败");
				returnobj.setCode(priceobj.getCode());
				returnobj.setMessage(priceobj.getMessage());
				return returnobj;
			}
			if(price<=0)
			{
				ResponseObject<Object> returnobj= new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
						"读取价格出错!");

				return returnobj;
			}
			
			endicia_interface endicia = new endicia_interface();
			//Endicialinfo info = new Endicialinfo();
			Endicial_arg info=this.endicialArgDao.getone();
			/*info.setAccountID(globalargsDao
					.getcontentbyflag("Endicia_AccountID"));
			info.setAPI_url(globalargsDao
					.getcontentbyflag("Endicia_Label_API_Url"));
			info.setImageFormat(globalargsDao
					.getcontentbyflag("Endicia_Print_ImageFormat"));
			info.setLabelSize(globalargsDao
					.getcontentbyflag("Endicia_Print_LabelSize"));
			info.setPassPhrase(globalargsDao
					.getcontentbyflag("Endicia_PassPhrase"));
			info.setRequesterID(globalargsDao
					.getcontentbyflag("Endicia_RequesterID"));
			info.setTest(globalargsDao.getcontentbyflag("Endicia_Print_test"));*/
			if (label.getLabelType() == null) {
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
						"参数无效");
			} else {
				//ResponseObject<Map<String, String>> obj = endicia.printLabel(
				//		info, label);
				
				
				ResponseObject<ExportEndiciaResult> obj = endicia.printLabel_manyimage(info, label);
				if (obj != null) {
					if ((obj.getCode() != null)
							&& (obj.getCode()
									.equalsIgnoreCase(ResponseCode.SUCCESS_CODE))) {
						//String money = obj.getData().get("TotalAmount");
						//String image = obj.getData().get("image");
						String money=obj.getData().getTotalmoney();
						label.setRealmoney(money);
						label.setTrackingNumber(obj.getData().getTrackingNumber());
						
						
						
						List<String> images=obj.getData().getImages();
						if((Double.valueOf(money)>0)&&(images!=null))
						{
							
						}else{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
									"读取label失败!");
						}
						User user=this.userDao.getUserById(label.getUserId());
						//String rate=get_Endicia_Get_User_Profit(user.getType());
						//double totalMoney =Double.valueOf(money)*(1+Double.valueOf(rate));
						
						
						
						String rate=get_Endicia_Get_User_Profit_arg(user.getType(),info.getUserprice());
						if(StringUtil.isEmpty(rate))
						{
							rate="1";
						}
						double totalMoney =Double.valueOf(money)*(Double.valueOf(rate));
						
						BigDecimal   b   =   new   BigDecimal(totalMoney);  //
						totalMoney   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); //四舍五入取两位,总价钱
						//因为此时已经扣费，只能直接扣除用户帐号的钱,不再核对账户信息
						
						double rmb = StringUtil.string2Double(user.getRmbBalance());
						double usd = StringUtil.string2Double(user.getUsdBalance());
						double newusd = usd - totalMoney; // 先用美元支付
						double newrmb = rmb;
						
						double rate0=Double.valueOf(this.globalargsDao.getcontentbyflag("cur_usa_cn"));//获取美元汇率
						//double renmtousd=(rmb/rate0);
						//BigDecimal   bn   =   new   BigDecimal(renmtousd);  //
						// renmtousd   =   bn.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); //四舍五入取两位
						
							if (newusd >= 0) {
								// ignore
							} else {
								newusd = 0.0D; // 美元余额全部支付，开始扣人民币的钱
								newrmb = new BigDecimal(rmb - ((totalMoney - usd)*rate0)).setScale(2,
										BigDecimal.ROUND_HALF_UP).doubleValue();
							}
							
							Date date = new Date();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String str = sdf.format(date);
							
							Account account = new Account();
							account.setUsd(String.valueOf(newusd));
							account.setRmb(String.valueOf(newrmb));
							account.setUserId(label.getUserId());
							account.setModifyDate(str);
							
							if (this.accountDao.modifyAccount(account) > 0) {
								// pass
							} else {
								throw new Exception();
							}
							AccountDetail detail = new AccountDetail();
							detail.setAmount(Double.toString(totalMoney));
							detail.setCreateDate(str);
							detail.setModifyDate(str);
							detail.setState(Constant.ACCOUNT_DETAIL_STATE2);
							detail.setCurrency("美元");
							detail.setName("美国邮政USPS(Endicia)打印");
							detail.setType(Constant.ACCOUNT_DETAIL_TYPE2);
							detail.setUserId(label.getUserId());
							String prefix = "帐户余额支付";
							
							prefix = "帐户余额支付";
							
							detail.setStoreId("-1");
							detail.setConfirm_state("0");
							detail.setRemark(prefix + ",网上打印label");
							this.accountDetailDao.insertAccountDetail(detail);
							
						
						label.setCreateDate(str);
						label.setModifyDate(str);
						label.setAmount(Double.toString(totalMoney));
							
							SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
							str = sdf1.format(date);
						

						String strseparator = "";
						if (this.saveendicia_printlabel.indexOf("/") >= 0)// 包含有“/”字符，分隔符用此字符
						{
							strseparator = "/";
						} else {
							strseparator = File.separator;
						}
						// 获取当时的时间缀
						int page=0;
						String splitstr="";
						for(String image:images)
						{
							page=page+1;
						//strseparator = File.separator;
						String fileName = this.saveendicia_printlabel
								+ strseparator
								+ str
								+ "_"
								+ StringUtil.generateRandomString(5)
								+ "_"
								+ StringUtil.generateRandomInteger(5)
								+"_"
								+page
								+ "."
								+info.getImageFormat();
								/*+ globalargsDao.getcontentbyflag(
										"Endicia_Print_ImageFormat")
										.toLowerCase();*/

						String saveurl1 = request.getSession()
								.getServletContext().getRealPath("/")
								+ fileName;
						byte[] datas = DatatypeConverter
								.parseBase64Binary(image);
						File imageFile = new File(saveurl1);
						//File imageFile = new File("aaaaaaaa.png");
						FileOutputStream outStream = new FileOutputStream(
								imageFile);
						outStream.write(datas);
						outStream.close();
						if(page!=1)
						{
							splitstr=splitstr+";"+fileName;
						}
						else
						{
							splitstr=fileName;
						}
						}

						
						label.setLabelUrl(splitstr);
						this.endicialLabelDao.insertEndiciaLabel(label);
						
						// 开始保存记录

						ResponseObject<Object> returnobj = new ResponseObject<Object>(
								ResponseCode.SUCCESS_CODE);
						returnobj.setData(splitstr);
						return returnobj;
					} else if ((obj.getCode() != null)
							&& (obj.getCode()
									.equalsIgnoreCase(ResponseCode.PARAMETER_ERROR))) {
						//String ErrorMessage = obj.getData().get("ErrorMessage");
						ResponseObject<Object> returnobj = new ResponseObject<Object>(
								ResponseCode.PARAMETER_ERROR);
						if(obj.getData()!=null)
						{
							ExportEndiciaResult exp=obj.getData();
							returnobj.setMessage(exp.getErrorMessage());
						}
						//returnobj.setMessage(ErrorMessage);
						return returnobj;
					} else {
						return new ResponseObject<Object>(
								ResponseCode.PARAMETER_ERROR, "打印失败");
					}
				}
			}

			/*
			 * int iresult = 0; if (iresult > 0) { return new
			 * ResponseObject<Object>(ResponseCode.SUCCESS_CODE); } else {
			 * return new
			 * ResponseObject<Object>(ResponseCode.NEWS_INSERT_FAILURE,
			 * "插入新闻失败"); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
 			throw ExceptionUtil.handle2ServiceException(e);
		}
		return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR, "打印失败");
	}

	public ResponseObject<Object> printlabelbyadmin(EndiciaLabel label,
			HttpServletRequest request,String adminname) throws ServiceException {
		if (label == null) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
					"参数无效");
		}

		try {
			
			
			endicia_interface endicia = new endicia_interface();
			//Endicialinfo info = new Endicialinfo();
			Endicial_arg info=this.endicialArgDao.getone();
			/*info.setAccountID(globalargsDao
					.getcontentbyflag("Endicia_AccountID"));
			info.setAPI_url(globalargsDao
					.getcontentbyflag("Endicia_Label_API_Url"));
			info.setImageFormat(globalargsDao
					.getcontentbyflag("Endicia_Print_ImageFormat"));
			info.setLabelSize(globalargsDao
					.getcontentbyflag("Endicia_Print_LabelSize"));
			info.setPassPhrase(globalargsDao
					.getcontentbyflag("Endicia_PassPhrase"));
			info.setRequesterID(globalargsDao
					.getcontentbyflag("Endicia_RequesterID"));
			info.setTest(globalargsDao.getcontentbyflag("Endicia_Print_test"));*/
			if (label.getLabelType() == null) {
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
						"参数无效");
			} else {
			//	ResponseObject<Map<String, String>> obj = endicia.printLabel(
			//			info, label);
				//此接口打印返回多个图片时的处理方式
				ResponseObject<ExportEndiciaResult> obj = endicia.printLabel_manyimage(info, label);
				
				if (obj != null) {
					if ((obj.getCode() != null)
							&& (obj.getCode()
									.equalsIgnoreCase(ResponseCode.SUCCESS_CODE))) {
						//String money = obj.getData().get("TotalAmount");
						String money = obj.getData().getTotalmoney();
						List<String> images=obj.getData().getImages();
					//	String image = obj.getData().get("image");
						
							
							Date date = new Date();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String str = sdf.format(date);
							
							String rate=this.globalargsDao.getcontentbyflag("Endicia_Get_User_Profit_byadmin");
							money=Double.toString((Double.valueOf(rate)+1)*Double.valueOf(money));
							
						   
							
							AccountDetail detail = new AccountDetail();
							detail.setAmount(money);
							detail.setCreateDate(str);
							detail.setModifyDate(str);
							detail.setState(Constant.ACCOUNT_DETAIL_STATE2);
							detail.setCurrency("美元");
							detail.setName("Endicia label打印");
							detail.setType(Constant.ACCOUNT_DETAIL_TYPE2);
							detail.setEmpId(label.getAdminid());
							detail.setUserId(label.getUserId());
							detail.setStoreId(label.getStoreId());
							detail.setConfirm_state("0");
							
							
							String prefix = "门市打印";
							
							//prefix = "帐户余额支付";
							
							
							detail.setRemark(prefix + ",打印门店："+adminname);
							this.accountDetailDao.insertAccountDetail(detail);
							
						
						label.setCreateDate(str);
						label.setModifyDate(str);
						label.setAmount(money);
					//label.setAmount(Double.toString(totalMoney));
							
							SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
							str = sdf1.format(date);
						

						String strseparator = "";
						if (this.saveendicia_printlabel.indexOf("/") >= 0)// 包含有“/”字符，分隔符用此字符
						{
							strseparator = "/";
						} else {
							strseparator = File.separator;
						}
						// 获取当时的时间缀
						
						//strseparator = File.separator;
						int page=0;
						String splitstr="";
						for(String image:images)
						{
							page=page+1;
							String fileName = this.saveendicia_printlabel
									+ strseparator
									+ str
									+ "_"
									+ StringUtil.generateRandomString(5)
									+ "_"
									+ StringUtil.generateRandomInteger(5)
									+"_"
									+page
									+ "."
									+ globalargsDao.getcontentbyflag(
											"Endicia_Print_ImageFormat")
											.toLowerCase();
	
							String saveurl1 = request.getSession()
									.getServletContext().getRealPath("/")
									+ fileName;
							
							byte[] datas = DatatypeConverter
									.parseBase64Binary(image);
							File imageFile = new File(saveurl1);
							//File imageFile = new File("aaaaaaaa.png");
							FileOutputStream outStream = new FileOutputStream(
									imageFile);
							outStream.write(datas);
							outStream.close();
							
							if(page!=1)
							{
								splitstr=splitstr+";"+fileName;
							}
							else
							{
								splitstr=fileName;
							}
							
						}

						
						label.setLabelUrl(splitstr);
						this.endicialLabelDao.insertEndiciaLabel(label);
						
						// 开始保存记录

						ResponseObject<Object> returnobj = new ResponseObject<Object>(
								ResponseCode.SUCCESS_CODE);
						returnobj.setData(splitstr);
						return returnobj;
					} else if ((obj.getCode() != null)
							&& (obj.getCode()
									.equalsIgnoreCase(ResponseCode.PARAMETER_ERROR))) {
						//String ErrorMessage = obj.getData().get("ErrorMessage");
						String ErrorMessage = obj.getData().getErrorMessage();
						ResponseObject<Object> returnobj = new ResponseObject<Object>(
								ResponseCode.PARAMETER_ERROR);
						returnobj.setMessage(ErrorMessage);
						return returnobj;
					} else {
						return new ResponseObject<Object>(
								ResponseCode.PARAMETER_ERROR, "打印失败");
					}
				}
			}

			/*
			 * int iresult = 0; if (iresult > 0) { return new
			 * ResponseObject<Object>(ResponseCode.SUCCESS_CODE); } else {
			 * return new
			 * ResponseObject<Object>(ResponseCode.NEWS_INSERT_FAILURE,
			 * "插入新闻失败"); }
			 */
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
		return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR, "打印失败");
	}
	public String get_Endicia_Get_User_Profit(String type) throws ServiceException {
		try {
			String rate = this.globalargsDao
					.getcontentbyflag("Endicia_Get_User_Profit");// 收取会员的手续费用比率
			
			String price="";
			String[] rate_temp = rate.split(";");
			if (rate_temp.length > 0) {
				for (int n = 0; n < rate_temp.length; n++) {
					String[] rate_temp1 = rate_temp[n].split("=");
					if(rate_temp1.length!=2)
					{
						price="";
						break;
					}
					
					if(rate_temp1[0].equalsIgnoreCase(type))
					{
						price=rate_temp1[1];
						break;
					}

				}

			} 
			return price;
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}

	}
	
	
	public String get_Endicia_Get_User_Profit_arg(String type,String userprice) throws ServiceException {
		try {
			String rate = userprice;// 收取会员的手续费用比率
			
			String price="";
			String[] rate_temp = rate.split(";");
			if (rate_temp.length > 0) {
				for (int n = 0; n < rate_temp.length; n++) {
					String[] rate_temp1 = rate_temp[n].split("=");
					if(rate_temp1.length!=2)
					{
						price="";
						break;
					}
					
					if(rate_temp1[0].equalsIgnoreCase(type))
					{
						price=rate_temp1[1];
						break;
					}

				}

			} 
			return price;
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}

	}
	
	
	public ResponseObject<PageSplit<EndiciaLabel>> searchEndiciaLabelByKeys(String userId,String userInfo,  String labelInfo, String sdate,  String edate,int pageSize, int pageNow) throws ServiceException
	{
		try {
			if(!StringUtil.isEmpty(userInfo) )
			{
				userInfo = StringUtil.escapeStringOfSearchKey(userInfo);
			}
			else
			{
				userInfo=null;
			}
			if(!StringUtil.isEmpty(labelInfo) )
			{
			labelInfo = StringUtil.escapeStringOfSearchKey(labelInfo);
			}
			else
			{
				labelInfo=null;
			}

			int rowCount = 0;
			try {
				rowCount = this.endicialLabelDao.countByKey(userId, userInfo, labelInfo, sdate, edate);
						
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("获取endicia个数失败", e);
			}

			ResponseObject<PageSplit<EndiciaLabel>> responseObj = new ResponseObject<PageSplit<EndiciaLabel>>(
					ResponseCode.SUCCESS_CODE);
			if (rowCount > 0) {
				pageSize = Math.max(pageSize, 1);
				int pageCount = rowCount / pageSize
						+ (rowCount % pageSize == 0 ? 0 : 1);
				pageNow = Math.min(pageNow, pageCount);
				PageSplit<EndiciaLabel> pageSplit = new PageSplit<EndiciaLabel>();
				pageSplit.setPageCount(pageCount);
				pageSplit.setPageNow(pageNow);
				pageSplit.setRowCount(rowCount);
				pageSplit.setPageSize(pageSize);

				int startIndex = (pageNow - 1) * pageSize;
				try {
					List<EndiciaLabel> endicial00 = this.endicialLabelDao.searchByKey(userId, userInfo, labelInfo, sdate, edate, startIndex, pageSize);
						
							
					pageSplit.setDatas(endicial00);
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("获取label列表失败", e);
				}
				responseObj.setData(pageSplit);
			} else {
				responseObj.setMessage("没有label");
			}
			return responseObj;
		} catch (ServiceException e) {
			throw e;
		}
	}
}

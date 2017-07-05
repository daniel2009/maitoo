//建立海关打印批次管理相关控制层
package com.meitao.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


















import javax.xml.bind.DatatypeConverter;

import jxl.common.Logger;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.meitao.common.composepics.imgcompose;
import com.meitao.common.constants.ArrayConstant;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.filezip.basiczip;
import com.meitao.common.util.ConsigneeInfoUtil;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.M_OrderUtil;
import com.meitao.common.util.OrderUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.common.util.UserUtil;
import com.meitao.model.Channel;
import com.meitao.model.CountMorder;
import com.meitao.model.Employee;
import com.meitao.model.HuitongNumber;
import com.meitao.model.M_OrderDetail;
import com.meitao.model.M_order;
import com.meitao.model.PageSplit;
import com.meitao.model.Receive_User;
import com.meitao.model.ResponseObject;
import com.meitao.model.ResponseString;
import com.meitao.model.Route;
import com.meitao.model.SeaNumber;
import com.meitao.model.Shelves_position;
import com.meitao.model.User;
import com.meitao.model.Warehouse;
import com.meitao.model.temp.ExportMorder;
import com.meitao.model.temp.ImportMorder;
import com.meitao.model.temp.ImportthirdMorder;


import com.meitao.service.MorderService;
import com.meitao.controller.BasicController;
import com.meitao.cardid.manage.import_t_orders;
import com.meitao.dao.CardIdManageDao;
import com.meitao.dao.ChannelDao;
import com.meitao.dao.EmployeeDao;
import com.meitao.dao.HuitongNumberDao;
import com.meitao.dao.MorderDao;
import com.meitao.dao.Receive_UserDao;
import com.meitao.dao.RouteDao;
import com.meitao.dao.SeaNumberDao;
import com.meitao.dao.Shelves_positionDao;
import com.meitao.dao.UserDao;
import com.meitao.dao.WarehouseDao;
import com.meitao.dao.globalargsDao;
import com.meitao.cardid.manage.CardId_lib;

@Controller
public class MorderController extends BasicController {

	private static final long serialVersionUID = 1058971052693785202L;
	private static final Logger log = Logger
			.getLogger(MorderController.class);


	@Resource(name = "m_orderService")
	private MorderService m_orderService;
	
	@Autowired
	private SeaNumberDao seaNumberDao;
	
	@Autowired
	private Receive_UserDao receive_UserDao;
	
	@Autowired
	private WarehouseDao warehouseDao;
	
	@Value(value = "${page_size}")
	private int defaultPageSize;

	@Autowired
	private MorderDao m_orderDao;
	
	@Autowired
	private ChannelDao channelDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RouteDao routeDao;
	
	@Autowired
	private HuitongNumberDao huitongNumberDao;
	
	@Autowired
	private CardIdManageDao cardIdManageDao;
	
	@Autowired
	private globalargsDao globalargsDao;
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private Shelves_positionDao shelves_positionDao;
	
	@Value(value = "${save_card_dir}")
	private String saveCardDir;
	
	@Resource(name = "cardidManageService")
	public com.meitao.service.CardidManageService cardidManageService;
	
	@Value(value = "${default_excel_type}")
	private String defaultExcelFileType;
	
	@Value(value = "${default_img_size}")
	private long defaultCardFileSize;
	@Value(value = "${save_258_card_url}")
	private String save258CardDir;
	@Value(value = "${default_img_type}")
	private String defaultCardFileType;
	
	@Value(value = "${meitao.neimenggu.import.sea.order.other.templets}")
	private String meitaoneimengguimportseaordertotheremplets;
	
	@Value(value = "${a.meitao.nf.model.templets}")
	private String ameitaonfmodeltemplets;
	
	@Value(value = "${a.meitao.pf.model.templets}")
	private String ameitaopfmodeltemplets;
	
	
	
	@Value(value = "${meitao.neimenggu.import.sea.order.templets}")
	private String meitaoneimengguimportseaordertemplets;
	
	@Value(value = "${morder.down.huitong.orders.templets}")
	private String morderdownhuitongorderstemplets;
	
	@Value(value = "${meitao.shenzhen.import.sea.order.templets}")
	private String meitaoshenzhenimportseaordertemplets;
	
	@Value(value = "${meitao.shanghai.sea.order.mode.templets}")
	private String meitaoshanghaiseaordermodetemplets;
	
	@Value("${order.output.to.state.result.templets}")
	private String orderOutputToStateResultTempletsFile;
	
	@Value(value = "${order.import.meitao.mordersstate.templets}")
	private String orderImportMeitaoOrdersstateTempletsFile;
	
	@Value("${order.output.to.meitaonew.templets}")
	private String orderOutputToMeitaonewTempletsFile;
	
	@Value("${morder.third.return.model.templets}")
	private String morderthirdreturnmodeltemplets;
	
	@Value("${morder.third.return.model2.templets}")
	private String morderthirdreturnmodel2templets;
	
	@Value("${morder.third.old.model.example}")
	private String morderthirdoldmodelexample;
	
	@Value("${morder.third.old.model2.example}")
	private String morderthirdoldmodel2example;
	
	@Value(value = "${meitao.import.orderid.example.templets}")
	private String meitaoimportsorderidexampletemplets;
	
	@Value(value = "${input.order.down.load.templets}")
	private String inputorderdownloadtemplets;
	
	
	@RequestMapping(value="/admin/m_order/ms_add", method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseObject<Object> getsenduserbyadmin(HttpServletRequest request,M_order order,
			@RequestParam(value = "cardurlfile", required = false) MultipartFile file,
			@RequestParam(value = "cardurlotherfile", required = false) MultipartFile fileother,
			@RequestParam(value = "cardurltogetherfile", required = false) MultipartFile filetogether){
		try{
			String storeId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
			if(StringUtil.isEmpty(storeId)){
				return generateResponseObject(ResponseCode.NEED_LOGIN,
						"请登陆！");
			}
			
			if(order==null)
			{
				return generateResponseObject(ResponseCode.PARAMETER_ERROR,
						"参数出错！");
			}
			
			//身份证号不为空，要检查合法性
			if(!StringUtil.isEmpty(order.getRuser().getCardid()))
			{
				if (!ConsigneeInfoUtil.validateCardId(order.getRuser().getCardid())) {
					return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
							"身份证号码填写错误，请重新填写！");
				}
			}
			//只要有一个上传不为空，身份证号就不为空
			if ((file != null && file.getSize() > 0)||(fileother != null && fileother.getSize() > 0)||(filetogether != null && filetogether.getSize() > 0))
			{
				if(StringUtil.isEmpty(order.getRuser().getCardid()))
				{
					return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
							"上传图片时，身份证号码不能为空，请重新填写！");
				}
				
				//上传正反两面图片时，必须保证两面图片都正确并存在
				if((file != null && file.getSize()>0)&&(fileother==null))
				{
					if(!StringUtil.isEmpty(order.getRuser().getCardother()))
					{
						File file3 = new File(request.getSession().getServletContext()
								.getRealPath("/")
								+ order.getRuser().getCardother());
						if(!file3.exists())
						{
							return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
									"正反两面照片必须同时存在！");
						}
					}
					else
					{
						return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
								"正反两面照片必须同时存在！");
					}
				}
				if((fileother != null && fileother.getSize()>0)&&(file==null))
				{
					if(!StringUtil.isEmpty(order.getRuser().getCardurl()))
					{
						File file3 = new File(request.getSession().getServletContext()
								.getRealPath("/")
								+ order.getRuser().getCardurl());
						if(!file3.exists())
						{
							return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
									"正反两面照片必须同时存在！");
						}
					}
					else
					{
						return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
								"正反两面照片必须同时存在！");
					}
				}
				
				
		
			}
			// 处理提交上来的图片
			// 解决火狐的反斜杠问题 kai 20151006
			String filetype = this.defaultCardFileType;// 要上传的文件类型
			String strtest = this.save258CardDir;
			String strseparator = "";
			if (strtest.indexOf("/") >= 0)// 包含有“/”字符，分隔符用此字符
			{
				strseparator = "/";
			} else {
				strseparator = File.separator;
			}
			//开始处理图片
			// 获取当时的时间缀
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyyMMddHHmmss");
			String timestr = sdf.format(date);
			//如果上传的图片中包含合成图片，那么即会忽略其它的正反两面图片
			if (filetogether != null && filetogether.getSize() > 0)
			{
				String fileName = null;
			
				if (filetogether.getSize() > this.defaultCardFileSize) {
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
				}

				String originalName = filetogether.getOriginalFilename();

				if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
				}
				int index = originalName.lastIndexOf('.');
				index = Math.max(index, 0);
				
				fileName = this.save258CardDir + strseparator + timestr + "_"+storeId+ "_"+"hc"+"_"
						+ StringUtil.generateRandomString(5) + "_"
						+ StringUtil.generateRandomInteger(5)
						+ originalName.substring(index);
				try {
					File file1 = new File(request.getSession().getServletContext()
							.getRealPath("/")
							+ fileName);
					filetogether.transferTo(file1);
				} catch (Exception e) {
					log.error("保存用户合成图像失败,请不要上传图像！", e);
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR,
							"保存用户合成图像失败，请去除上传图像后再尝试!");
				}
				
				order.getRuser().setCardtogether(fileName);
			}
			else
			{
				//正反图片有上传的图片
				if ((file != null && file.getSize() > 0)||(fileother != null && fileother.getSize() > 0))
				{
					//正面图片处理
					if(file != null && file.getSize() > 0)
					{
						String fileName = null;
						if (file.getSize() > this.defaultCardFileSize) {
							return generateResponseObject(
									ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
						}
		
						String originalName = file.getOriginalFilename();
		
						if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
							return generateResponseObject(
									ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
						}
						int index = originalName.lastIndexOf('.');
						index = Math.max(index, 0);
						
						fileName = this.save258CardDir + strseparator + timestr + "_"+storeId+ "_"+"zm"+"_"
								+ StringUtil.generateRandomString(5) + "_"
								+ StringUtil.generateRandomInteger(5)
								+ originalName.substring(index);
						try {
							File file1 = new File(request.getSession().getServletContext()
									.getRealPath("/")
									+ fileName);
							file.transferTo(file1);
						} catch (Exception e) {
							log.error("保存用户正面图像失败,请不要上传图像！", e);
							return generateResponseObject(
									ResponseCode.CONSIGNEE_CARD_ERROR,
									"保存用户正面图像失败，请去除上传图像后再尝试!");
						}
						
						order.getRuser().setCardurl(fileName);
					}
					
					//反面图片处理
					if(fileother != null && fileother.getSize() > 0)
					{
						String fileName = null;
						if (fileother.getSize() > this.defaultCardFileSize) {
							return generateResponseObject(
									ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
						}
		
						String originalName = fileother.getOriginalFilename();
		
						if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
							return generateResponseObject(
									ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
						}
						int index = originalName.lastIndexOf('.');
						index = Math.max(index, 0);
						
						fileName = this.save258CardDir + strseparator + timestr + "_"+storeId+ "_"+"fm"+"_"
								+ StringUtil.generateRandomString(5) + "_"
								+ StringUtil.generateRandomInteger(5)
								+ originalName.substring(index);
						try {
							File file1 = new File(request.getSession().getServletContext()
									.getRealPath("/")
									+ fileName);
							fileother.transferTo(file1);
						} catch (Exception e) {
							log.error("保存用户反面图像失败,请不要上传图像！", e);
							return generateResponseObject(
									ResponseCode.CONSIGNEE_CARD_ERROR,
									"保存用户反面图像失败，请去除上传图像后再尝试!");
						}
						
						order.getRuser().setCardother(fileName);
					}
					
					
					//合成处理
					String filecardtemp = "";
					if ((!StringUtil.isEmpty(order.getRuser().getCardurl())) && (!StringUtil.isEmpty(order.getRuser().getCardother()))) {
						imgcompose img = new imgcompose();
						String str1 = request.getSession().getServletContext()
								.getRealPath("/")
								+ order.getRuser().getCardurl();
						String str2 = request.getSession().getServletContext()
								.getRealPath("/")
								+ order.getRuser().getCardother();
						
						
						File file4 = new File(str1);
						File file5 = new File(str2);
						if((file4.exists())&&(file5.exists()))
						{
							String str3 = this.save258CardDir + strseparator + timestr + "_"+storeId+ "_"+"hc"+"_"
									+ StringUtil.generateRandomString(5) + "_"
									+ StringUtil.generateRandomInteger(5);
							filecardtemp = str3;
							str3 = request.getSession().getServletContext().getRealPath("/")
									+ str3;
							if (img.createcompics(str1, str2, str3)) {
								filecardtemp = filecardtemp + ".jpg";
							}
						}
						
					}
					
					if(!StringUtil.isEmpty(filecardtemp))
					{
						order.getRuser().setCardtogether(filecardtemp);
					}
					
				}
				
				
				
			}
			
			
	
			
			//pageIndex = Math.max(pageIndex, 1);
			if (!ConsigneeInfoUtil.validatePhone(order.getRuser().getPhone())) {
			      return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "收件人手机号码填写错误，请检查！");
			}
			if (!ConsigneeInfoUtil.validatePhone(order.getSuser().getPhone())) {
			      return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "发件人手机号码填写错误，请检查！");
			}
			String empId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY));
			//String empName = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ACCOUNT_SESSION_KEY));
			//String empStoreName = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_NAME_SESSION_KEY));
			//String storeId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
			order.setEmployeeId(empId);
			order.setStoreId(storeId);
			order.setType(Constant.ORDER_TYPE_KEY_1);//设置为门市运单
			
			double price =this.m_orderService.calculationM_orderFreight(order);
			
			if(price<=0)
			{
				return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "总价钱不能小于或等于0，值为:"+Double.toString(price));
			}
			double cost=this.m_orderService.calculationM_OrderCostFreight(order);
			if(cost<=0)
			{
				return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "成本不能小于或等于0，请检查配置，值为:"+Double.toString(cost));
			}
			
			//计算普通会员与会员之间的价格并保存
			double nprice=this.m_orderService.calculationM_orderFreight_usertype(order,Constant.USER_TYPE_NORMAL);//计算普通会员的价格
			if(nprice>price)
			{
				order.setUser_price(Double.toString(nprice));
			}
			else
			{
				order.setUser_price(Double.toString(price));
			}
			
			order.setState(Constant.ORDER_STATE2);//设置为待付款状态
			order.getRuser().setUseState("1");
			order.getSuser().setUseState("1");
			order.setTotalmoney(Double.toString(price));
			order.setTotalcost(Double.toString(cost));
			
			if(!StringUtil.isEmpty(order.getPrintway())&&(order.getPrintway().equalsIgnoreCase("1")))//海关匹配打单，所以要匹配身份证
			{
				//上传的包含身份证信息就不用匹配身份证了
				if(StringUtil.isEmpty(order.getRuser().getCardid())||StringUtil.isEmpty(order.getRuser().getCardtogether())||StringUtil.isEmpty(order.getRuser().getName()))
				{
					List<import_t_orders> list1 = new ArrayList<import_t_orders>();
					import_t_orders temp=new import_t_orders();
					temp.setR_name(order.getRuser().getName());
					list1.add(temp);
					ResponseObject<Object> obj=this.cardidManageService.verifycardid(list1, "1",request);//进行模糊匹配
					if((obj!=null)&&(ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj.getCode())))
					{
						list1=(List<import_t_orders>)obj.getData();
						obj.setData(null);
						obj=null;
						if((list1==null)||(list1.size()==0))
						{
							throw new Exception("匹配身份证信息失败");
						}
						if(list1.get(0).isResultflag())//匹配成功
						{
							//order.getRuser().setCardid(list1.get(0).getCardid());
							order.getRuser().setSecondName(list1.get(0).getCardname());
							//order.getRuser().setCardtogether(list1.get(0).getCardurl());
							order.getRuser().setCardlibId(list1.get(0).getCardlibId());
							order.getRuser().setRemark(list1.get(0).getResult());
						}
						else
						{
							throw new Exception("匹配身份证信息失败");
						}
					}
					else
					{
						return obj;
						//throw new Exception("匹配身份证信息失败");
					}
				}
				
				
				//海关单号
				/*String date2 = DateUtil.date2String(new Date());
				if(StringUtil.isEmpty(order.getSorderId()))
				{
					//获取海关单
					SeaNumber seano=seaNumberDao.getone();
					if((seano==null)||(StringUtil.isEmpty(seano.getOrderId())))
					{
						if(seano!=null)
						{
							seano.setState("1");
							seano.setModifyDate(date2);
							int a=seaNumberDao.updatestate(seano);
							if(a==0)
							{
								throw new Exception("修改海关单状态失败!");
							}
							order.setModifyDate(date2);
							a=this.m_orderDao.updateSorderid(order);
							if(a==0)
							{
								throw new Exception("个性海关单号到运单中失败!");
							}
						}
						throw new Exception("获取海关号失败!");
					}else
					{
						order.setSorderId(seano.getOrderId());
						seano.setState("1");
						//seano.setModifyDate(date);
						int a=seaNumberDao.updatestate(seano);
						
						if(a==0)
						{
							throw new Exception("修改海关单状态失败!");
						}
						//order.setModifyDate(date);
						//a=this.m_orderDao.updateSorderid(order);
						//if(a==0)
					//	{
						//	throw new Exception("修改海关单号到运单中失败!");
						//}
						//changeflage=true;
					}
				}*/
			}
			
			return this.m_orderService.add_ms_Morder(order);
			
		}catch(Exception e){
			log.error("添加运单失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
			//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
		}
	}
	//查询门店价格
	@RequestMapping(value="/admin/m_order/check_price", method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseObject<Object> checkmsprice(HttpServletRequest request,M_order order,
			@RequestParam(value = "cardurlfile", required = false) MultipartFile file,
			@RequestParam(value = "cardurlotherfile", required = false) MultipartFile fileother,
			@RequestParam(value = "cardurltogetherfile", required = false) MultipartFile filetogether){
		try{
			if(order==null)
			{
				return generateResponseObject(ResponseCode.PARAMETER_ERROR,
						"参数出错！");
			}
			
			//身份证号不为空，要检查合法性
			if(!StringUtil.isEmpty(order.getRuser().getCardid()))
			{
				if (!ConsigneeInfoUtil.validateCardId(order.getRuser().getCardid())) {
					return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
							"身份证号码填写错误，请重新填写！");
				}
			}
			
			
			//只要有一个上传不为空，身份证号就不为空
			if ((file != null && file.getSize() > 0)||(fileother != null && fileother.getSize() > 0)||(filetogether != null && filetogether.getSize() > 0))
			{
				if(StringUtil.isEmpty(order.getRuser().getCardid()))
				{
					return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
							"上传图片时，身份证号码不能为空，请重新填写！");
				}
				
				//上传正反两面图片时，必须保证两面图片都正确并存在
				if((file != null && file.getSize()>0)&&(fileother==null))
				{
					if(!StringUtil.isEmpty(order.getRuser().getCardother()))
					{
						File file3 = new File(request.getSession().getServletContext()
								.getRealPath("/")
								+ order.getRuser().getCardother());
						if(!file3.exists())
						{
							return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
									"正反两面照片必须同时存在！");
						}
					}
					else
					{
						return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
								"正反两面照片必须同时存在！");
					}
				}
				if((fileother != null && fileother.getSize()>0)&&(file==null))
				{
					if(!StringUtil.isEmpty(order.getRuser().getCardurl()))
					{
						File file3 = new File(request.getSession().getServletContext()
								.getRealPath("/")
								+ order.getRuser().getCardurl());
						if(!file3.exists())
						{
							return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
									"正反两面照片必须同时存在！");
						}
					}
					else
					{
						return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
								"正反两面照片必须同时存在！");
					}
				}
				
				
		
			}
			
			//pageIndex = Math.max(pageIndex, 1);
			if (!ConsigneeInfoUtil.validatePhone(order.getRuser().getPhone())) {
			      return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "收件人手机号码填写错误，请检查！");
			}
			if (!ConsigneeInfoUtil.validatePhone(order.getSuser().getPhone())) {
			      return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "发件人手机号码填写错误，请检查！");
			}
			
			if(!StringUtil.isEmpty(order.getPayWay())&&(order.getPayWay().equalsIgnoreCase("0")))//余额支付
			{
				if(StringUtil.isEmpty(order.getUserId()))
				{
					 return generateResponseObject(ResponseCode.PARAMETER_ERROR, "余额支付必须在会员下操作！");
				}
			}
			
			
			
			
			
			String empId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY));
			//String empName = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ACCOUNT_SESSION_KEY));
			//String empStoreName = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_NAME_SESSION_KEY));
			String storeId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
			order.setEmployeeId(empId);
			order.setStoreId(storeId);
			
			
			
			
			if(!StringUtil.isEmpty(order.getType())&&(order.getType().equalsIgnoreCase("-10")))//表示提交网上置单修改
			{
				order.setType(Constant.ORDER_TYPE_KEY_3);//设置为网上置单
				if(!StringUtil.isEmpty(order.getOrderId()))
				{
					if(StringUtil.isEmpty(order.getUserId()))
					{
						return generateResponseObject(ResponseCode.PARAMETER_ERROR, "没有包含会员信息!");
					}
					M_order order2=this.m_orderDao.getByOrderId(order.getOrderId(), null);//根据运单号查找
					
					if(order2==null)
					{
						return generateResponseObject(ResponseCode.PARAMETER_ERROR, "查找不到对应的运单！");
					}
					else
					{
						if(!Constant.ORDER_STATE__10.equalsIgnoreCase(order2.getState()))
						{
							return generateResponseObject(ResponseCode.PARAMETER_ERROR, "此运单不是在线运单！");
						}
						if(!order.getUserId().equalsIgnoreCase(order2.getUserId()))
						{
							return generateResponseObject(ResponseCode.PARAMETER_ERROR, "提交的运单与之前保存的所属用户不一样！");
						}
					}
					order.getRuser().setId(order2.getRuser().getId());
					order.getSuser().setId(order2.getSuser().getId());
				}
				else
				{
					return generateResponseObject(ResponseCode.PARAMETER_ERROR, "提交单号不能为空！");
				}
				
				
			}
			else if(!StringUtil.isEmpty(order.getType())&&(order.getType().equalsIgnoreCase("-9")))//表示提交网上置单修改
			{
				order.setType(Constant.ORDER_TYPE_KEY_5);//设置为上门收货
				if(!StringUtil.isEmpty(order.getOrderId()))
				{
					if(StringUtil.isEmpty(order.getUserId()))
					{
						return generateResponseObject(ResponseCode.PARAMETER_ERROR, "没有包含会员信息!");
					}
					M_order order2=this.m_orderDao.getByOrderId(order.getOrderId(), null);//根据运单号查找
					
					if(order2==null)
					{
						return generateResponseObject(ResponseCode.PARAMETER_ERROR, "查找不到对应的运单！");
					}
					else
					{
						if(!Constant.ORDER_STATE__10.equalsIgnoreCase(order2.getState()))
						{
							return generateResponseObject(ResponseCode.PARAMETER_ERROR, "此运单不是在线运单！");
						}
						if(!order.getUserId().equalsIgnoreCase(order2.getUserId()))
						{
							return generateResponseObject(ResponseCode.PARAMETER_ERROR, "提交的运单与之前保存的所属用户不一样！");
						}
					}
					order.getRuser().setId(order2.getRuser().getId());
					order.getSuser().setId(order2.getSuser().getId());
				}
				else
				{
					return generateResponseObject(ResponseCode.PARAMETER_ERROR, "提交单号不能为空！");
				}
				
				
			}
			else
			{
				order.setType(Constant.ORDER_TYPE_KEY_1);//设置为门市运单
			}
			
			
			
			
			
			
			double price =this.m_orderService.calculationM_orderFreight(order);
			if(price<=0)
			{
				return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "价格不能小于或等于0，请检查配置，值为:"+Double.toString(price));
			}
			double cost=this.m_orderService.calculationM_OrderCostFreight(order);
			if(cost<=0)
			{
				return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "成本不能小于或等于0，请检查配置，值为:"+Double.toString(cost));
			}
			
			
			double typeprice=0;
			if(Constant.ORDER_TYPE_KEY_3.equalsIgnoreCase(order.getType()))//网上置单，要计算网上置单价格，并与会员价格进行比较，以低价格为准
			{
				//order.setType(Constant.ORDER_TYPE_KEY_3);
				typeprice=this.m_orderService.calculationM_orderFreight_bytype(order);//计算网上置单价格
				if(typeprice<=0.01)//类型价格小于等于0表示计算失败
				{
					
				}
				else
				{
					if(typeprice<price)//类型价格
					{
						price=typeprice;//网上置单以小价格为准
						
					}
					else
					{
						
					}
				}
			}
			else if(Constant.ORDER_TYPE_KEY_5.equalsIgnoreCase(order.getType()))//上门收货价格设置
			{
				order.setType(Constant.ORDER_TYPE_KEY_5);
				typeprice=this.m_orderService.calculationM_orderFreight_bytype(order);//计算上门收货价格
				if(typeprice<=0.01)//类型价格小于等于0表示计算失败
				{
					//price=typeprice;//网上置单以小价格为准
				}
				else
				{
					if(typeprice>price)
					{
						price=typeprice;//价格以高者为准
					}
				}
			}
			else
			{
				
			}
			
			ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			
			obj.setData(price);
			if(!StringUtil.isEmpty(order.getPayWay())&&(order.getPayWay().equalsIgnoreCase("0")))//余额支付,要检查用户信息
			{
				
				User user=this.userDao.getUserById(order.getUserId());
				
				if(user==null)//用户不能为空
				{
					return generateResponseObject(ResponseCode.PARAMETER_ERROR, "查找不到会员信息");
				}
				else
				{
					obj.setMessage("归属会员："+user.getId()+"。余额支付!");
					double usa=0;
					double rmb=0;
					try{
						usa=StringUtil.string2Double(user.getUsdBalance());//美元余额
						rmb=StringUtil.string2Double(user.getRmbBalance());//人民币余额
					}
					catch(Exception e)
					{
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取用户余额失败");
					}
					double newusa=usa-price;
					double newrmb=rmb;
					if(newusa>=0)//美元足够支付
					{
					}
					else
					{
						String rateString = this.globalargsDao.getcontentbyflag("cur_usa_cn");
						if(StringUtil.isEmpty(rateString) )
						{
							return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "会员余额不足!");
						}
						else
						{
							try{
								double rate=StringUtil.string2Double(rateString);//汇率
								if(rate<=0)
								{
									return generateResponseObject(ResponseCode.PARAMETER_ERROR, "汇率参数设置不正确!");
								}
								else
								{
									if((newusa*rate+newrmb)<0)//余额不足
									{
										return generateResponseObject(ResponseCode.PARAMETER_ERROR, "余额不足!");
									}
								}
							}
							catch(Exception e)
							{
								return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "汇率参数设置不正确!");
							}
						}
					}
					
				}
			}
			else
			{
				if(!StringUtil.isEmpty(order.getUserId()))//会员id存在，要检查其是否存在
				{
					User user=this.userDao.getUserById(order.getUserId());
					if(user==null)
					{
						return generateResponseObject(ResponseCode.PARAMETER_ERROR, "会员不存在!");
					}
					obj.setMessage("归属会员："+user.getId()+"。现金支付!");
				}
			}
			
			return obj;
			
		}catch(Exception e){
			log.error("get receive user fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
		}
	}
	
	
	
	
	//核实修改运单的价格
	@RequestMapping(value="/admin/m_order/modify_check_price", method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseObject<Object> checkmodifyprice(HttpServletRequest request,M_order order,
			@RequestParam(value = "cardurlfile", required = false) MultipartFile file,
			@RequestParam(value = "cardurlotherfile", required = false) MultipartFile fileother,
			@RequestParam(value = "cardurltogetherfile", required = false) MultipartFile filetogether){
		try{
			if(order==null)
			{
				return generateResponseObject(ResponseCode.PARAMETER_ERROR,
						"参数出错！");
			}
			
			
			
			//身份证号不为空，要检查合法性
			if(!StringUtil.isEmpty(order.getRuser().getCardid()))
			{
				if (!ConsigneeInfoUtil.validateCardId(order.getRuser().getCardid())) {
					return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
							"身份证号码填写错误，请重新填写！");
				}
			}
			
			
			//只要有一个上传不为空，身份证号就不为空
			if ((file != null && file.getSize() > 0)||(fileother != null && fileother.getSize() > 0)||(filetogether != null && filetogether.getSize() > 0))
			{
				if(StringUtil.isEmpty(order.getRuser().getCardid()))
				{
					return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
							"上传图片时，身份证号码不能为空，请重新填写！");
				}
				
				//上传正反两面图片时，必须保证两面图片都正确并存在
				if((file != null && file.getSize()>0)&&(fileother==null))
				{
					if(!StringUtil.isEmpty(order.getRuser().getCardother()))
					{
						File file3 = new File(request.getSession().getServletContext()
								.getRealPath("/")
								+ order.getRuser().getCardother());
						if(!file3.exists())
						{
							return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
									"正反两面照片必须同时存在！");
						}
					}
					else
					{
						return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
								"正反两面照片必须同时存在！");
					}
				}
				if((fileother != null && fileother.getSize()>0)&&(file==null))
				{
					if(!StringUtil.isEmpty(order.getRuser().getCardurl()))
					{
						File file3 = new File(request.getSession().getServletContext()
								.getRealPath("/")
								+ order.getRuser().getCardurl());
						if(!file3.exists())
						{
							return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
									"正反两面照片必须同时存在！");
						}
					}
					else
					{
						return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
								"正反两面照片必须同时存在！");
					}
				}
				
				
		
			}
			
			//pageIndex = Math.max(pageIndex, 1);
			if (!ConsigneeInfoUtil.validatePhone(order.getRuser().getPhone())) {
			      return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "收件人手机号码填写错误，请检查！");
			}
			if (!ConsigneeInfoUtil.validatePhone(order.getSuser().getPhone())) {
			      return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "发件人手机号码填写错误，请检查！");
			}
			
			if(!StringUtil.isEmpty(order.getPayWay())&&(order.getPayWay().equalsIgnoreCase("0")))//余额支付
			{
				if(StringUtil.isEmpty(order.getUserId()))
				{
					 return generateResponseObject(ResponseCode.PARAMETER_ERROR, "余额支付必须在会员下操作！");
				}
			}
			
			
			
			
			
			//String empId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY));
			//String empName = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ACCOUNT_SESSION_KEY));
			//String empStoreName = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_NAME_SESSION_KEY));
		//	String storeId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
		//	order.setEmployeeId(empId);
		//	order.setStoreId(storeId);
			
			String storeId=null;
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
			{
				storeId=null;//表示可以查找所有门店
				
			}else
			{
				storeId = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
				if((storeId==null)||(storeId.equalsIgnoreCase("")))
				{
					return generateResponseObject(ResponseCode.NEED_LOGIN,
							"你没有登陆!");
				}
			}
			
			if(StringUtil.isEmpty(order.getId()))
			{
				return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数出错,没有id号!");
			}
			else
			{
				M_order order1=this.m_orderDao.getById(order.getId(), storeId);
				if(order1==null)
				{
					return generateResponseObject(ResponseCode.PARAMETER_ERROR, "没有找到对应的运单的id号,参数可能出错!");
				}
				if(Constant.ORDER_PAY_STATE0.equalsIgnoreCase(order.getPayWay()))//余额支付要检查用户是否存在
				{
					if(order1.getUser()==null)
					{
						return generateResponseObject(ResponseCode.PARAMETER_ERROR, "只有会员在有可能进行余额支付!");
					}
				}
				
				order.setUserId(order1.getUserId());
				order.setType(order1.getType());//获取类型
				order.setUser(order1.getUser());
				if(Constant.ORDER_PAY_STATE1.equalsIgnoreCase(order1.getPayornot()))
				{
					return generateResponseObject(ResponseCode.PARAMETER_ERROR, "已经支付的运单不能再进行支付!");
				}
				
			}
			
			/*if(!StringUtil.isEmpty(order.getType())&&(order.getType().equalsIgnoreCase("-10")))//表示提交网上置单修改
			{
				order.setType(Constant.ORDER_TYPE_KEY_3);//设置为网上置单
				if(!StringUtil.isEmpty(order.getOrderId()))
				{
					if(StringUtil.isEmpty(order.getUserId()))
					{
						return generateResponseObject(ResponseCode.PARAMETER_ERROR, "没有包含会员信息!");
					}
					M_order order2=this.m_orderDao.getByOrderId(order.getOrderId(), storeId);//
					if(order2==null)
					{
						return generateResponseObject(ResponseCode.PARAMETER_ERROR, "在本门店中查找不到对应的运单！");
					}
					else
					{
						if(!Constant.ORDER_STATE__10.equalsIgnoreCase(order2.getState()))
						{
							return generateResponseObject(ResponseCode.PARAMETER_ERROR, "此运单不是在线运单！");
						}
						if(!order.getUserId().equalsIgnoreCase(order2.getUserId()))
						{
							return generateResponseObject(ResponseCode.PARAMETER_ERROR, "提交的运单与之前保存的所属用户不一样！");
						}
					}
					order.getRuser().setId(order2.getRuser().getId());
					order.getSuser().setId(order2.getSuser().getId());
				}
				else
				{
					return generateResponseObject(ResponseCode.PARAMETER_ERROR, "提交在线置单不能为空！");
				}
				
				
			}
			else
			{
				order.setType(Constant.ORDER_TYPE_KEY_1);//设置为门市运单
			}*/
			
			double price =this.m_orderService.calculationM_orderFreight(order);
			
			if(price<=0)
			{
				return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "价格不能小于或等于0，请检查配置，值为:"+Double.toString(price));
			}
			
			double cost=this.m_orderService.calculationM_OrderCostFreight(order);
			if(cost<=0)
			{
				return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "成本不能小于或等于0，请检查配置，值为:"+Double.toString(cost));
			}
			
			
			
			
			
			double typeprice=0;
			if(Constant.ORDER_TYPE_KEY_3.equalsIgnoreCase(order.getType()))//网上置单，要计算网上置单价格，并与会员价格进行比较，以低价格为准
			{
				order.setType(Constant.ORDER_TYPE_KEY_3);
				typeprice=this.m_orderService.calculationM_orderFreight_bytype(order);//计算网上置单价格
				if(typeprice<=0.01)//类型价格小于等于0表示计算失败
				{
					
				}
				else
				{
					if(typeprice<price)//类型价格
					{
						price=typeprice;//网上置单以小价格为准
						
					}
					else
					{
						
					}
				}
			}
			else if(Constant.ORDER_TYPE_KEY_5.equalsIgnoreCase(order.getType()))//上门收货价格设置
			{
				order.setType(Constant.ORDER_TYPE_KEY_5);
				typeprice=this.m_orderService.calculationM_orderFreight_bytype(order);//计算上门收货价格
				if(typeprice<=0.01)//类型价格小于等于0表示计算失败
				{
					//price=typeprice;//网上置单以小价格为准
				}
				else
				{
					if(typeprice>price)
					{
						price=typeprice;//价格以高者为准
					}
				}
			}
			else
			{
				
			}
			
			
			
			
			
			
			
			
			
			ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			
			obj.setData(price);
			if(!StringUtil.isEmpty(order.getPayWay())&&(order.getPayWay().equalsIgnoreCase("0")))//余额支付,要检查用户信息
			{
				
				User user=this.userDao.getUserById(order.getUserId());
				
				if(user==null)//用户不能为空
				{
					return generateResponseObject(ResponseCode.PARAMETER_ERROR, "查找不到会员信息");
				}
				else
				{
					obj.setMessage("归属会员："+user.getId()+"。余额支付!");
					double usa=0;
					double rmb=0;
					try{
						usa=StringUtil.string2Double(user.getUsdBalance());//美元余额
						rmb=StringUtil.string2Double(user.getRmbBalance());//人民币余额
					}
					catch(Exception e)
					{
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取用户余额失败");
					}
					double newusa=usa-price;
					double newrmb=rmb;
					if(newusa>=0)//美元足够支付
					{
					}
					else
					{
						String rateString = this.globalargsDao.getcontentbyflag("cur_usa_cn");
						if(StringUtil.isEmpty(rateString) )
						{
							return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "会员余额不足!");
						}
						else
						{
							try{
								double rate=StringUtil.string2Double(rateString);//汇率
								if(rate<=0)
								{
									return generateResponseObject(ResponseCode.PARAMETER_ERROR, "汇率参数设置不正确!");
								}
								else
								{
									if((newusa*rate+newrmb)<0)//余额不足
									{
										return generateResponseObject(ResponseCode.PARAMETER_ERROR, "余额不足!");
									}
								}
							}
							catch(Exception e)
							{
								return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "汇率参数设置不正确!");
							}
						}
					}
					
				}
			}
			else
			{
				if(!StringUtil.isEmpty(order.getUserId()))//会员id存在，要检查其是否存在
				{
					User user=this.userDao.getUserById(order.getUserId());
					if(user==null)
					{
						return generateResponseObject(ResponseCode.PARAMETER_ERROR, "会员不存在!");
					}
					obj.setMessage("归属会员："+user.getId()+"。现金支付!");
				}
			}
			
			return obj;
			
		}catch(Exception e){
			log.error("get receive user fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
		}
	}
	
	
	@RequestMapping(value="/admin/m_order/get_one_by_id", method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseObject<Object> getonebtid(HttpServletRequest request,
			@RequestParam(value = "id", required = false) String id){
		try{
			if(StringUtil.isEmpty(id))
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数id不能为空!");
			}
			
			String storeid=null;
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
			{
				storeid=null;//表示可以查找所有门店
				
			}else
			{
				storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
				if((storeid==null)||(storeid.equalsIgnoreCase("")))
				{
					return generateResponseObject(ResponseCode.NEED_LOGIN,
							"你没有登陆!");
				}
			}
			return this.m_orderService.getonebyid(id, storeid);
			
		}catch(Exception e){
			log.error("get morder fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
		}
	}
	
	//热敏打单4*6
	@RequestMapping(value="/admin/m_order/get_one_by_id_46", method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseObject<Object> getonebyidwithhotpaper(HttpServletRequest request,
			@RequestParam(value = "id", required = false) String id){
		try{
			if(StringUtil.isEmpty(id))
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数id不能为空!");
			}
			
			String storeid=null;
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
			{
				storeid=null;//表示可以查找所有门店
				
			}else
			{
				storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
				if((storeid==null)||(storeid.equalsIgnoreCase("")))
				{
					return generateResponseObject(ResponseCode.NEED_LOGIN,
							"你没有登陆!");
				}
			}
			ResponseObject<Object> obj= this.m_orderService.getonebyid(id, storeid);
			if((obj!=null)&&(ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj.getCode())))
			{
				boolean changeflage=false;
				M_order order=(M_order)obj.getData();
				
				
				boolean cardtrue=false;	
				if(!StringUtil.isEmpty(order.getRuser().getCardlibId()))
				{
					CardId_lib lib=this.cardIdManageDao.getbyid(order.getRuser().getCardlibId());
					if(lib==null)//为空要重新查找
					{
						
					}
					else
					{
						cardtrue=true;
						order.getRuser().setCardlib(lib);
					}
					
				}
				
				//原来收件人的身份信息为空，要重新提取
				if((cardtrue==false)&&(StringUtil.isEmpty(order.getRuser().getSecondName())||StringUtil.isEmpty(order.getRuser().getCardid())||StringUtil.isEmpty(order.getRuser().getCardtogether())))
				{
					
					List<import_t_orders> list1 = new ArrayList<import_t_orders>();
					import_t_orders temp=new import_t_orders();
					temp.setR_name(order.getRuser().getName());
					list1.add(temp);
					ResponseObject<Object> obj1=this.cardidManageService.verifycardid(list1, "1",request);//进行模糊匹配
					if((obj1!=null)&&(ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj1.getCode())))
					{
						list1=(List<import_t_orders>)obj1.getData();
						obj.setData(null);
						obj=null;
						
						if((list1==null)||(list1.size()==0))
						{
							throw new Exception("匹配身份证信息失败");
						}
						if(list1.get(0).isResultflag())//匹配成功
						{
							//order.getRuser().setCardid(list1.get(0).getCardid());
							order.getRuser().setSecondName(list1.get(0).getCardname());
							//order.getRuser().setCardtogether(list1.get(0).getCardurl());
							order.getRuser().setRemark(list1.get(0).getResult());
							String date = DateUtil.date2String(new Date());
							order.getRuser().setModifyDate(date);
							order.getRuser().setCardlibId(list1.get(0).getCardlibId());
							
							CardId_lib lib=this.cardIdManageDao.getbyid(order.getRuser().getCardlibId());
							if(lib==null)
							{
								throw new Exception("获取身份证信息失败！");
							}
							else
							{
								order.getRuser().setCardlib(lib);
							}
							
							
							int k=this.receive_UserDao.modifymatchinfo(order.getRuser());
							if(k==0)
							{
								throw new Exception("修改收件人信息失败！");
							}
							
						}
						else
						{
							throw new Exception("匹配身份证信息失败");
						}
						changeflage=true;
					}
					else
					{
						return obj1;
					}
				
				}
				//海关单号
				String date = DateUtil.date2String(new Date());
				if(StringUtil.isEmpty(order.getSorderId()))
				{
					//获取海关单
					SeaNumber seano=seaNumberDao.getone();
					if((seano==null)||(StringUtil.isEmpty(seano.getOrderId())))
					{
						if(seano!=null)
						{
							seano.setState("1");
							seano.setModifyDate(date);
							int a=seaNumberDao.updatestate(seano);
							if(a==0)
							{
								throw new Exception("修改海关单状态失败!");
							}
							order.setModifyDate(date);
							a=this.m_orderDao.updateSorderid(order);
							if(a==0)
							{
								throw new Exception("个性海关单号到运单中失败!");
							}
						}
						throw new Exception("获取海关号失败!");
					}else
					{
						order.setSorderId(seano.getOrderId());
						seano.setState("1");
						seano.setModifyDate(date);
						int a=seaNumberDao.updatestate(seano);
						
						if(a==0)
						{
							throw new Exception("修改海关单状态失败!");
						}
						order.setModifyDate(date);
						a=this.m_orderDao.updateSorderid(order);
						if(a==0)
						{
							throw new Exception("个性海关单号到运单中失败!");
						}
						changeflage=true;
					}
				}
				
			
				ResponseObject<Object> objend=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
				objend.setData(order);
				return objend;
				
			}
			else
			{
				return obj;
			}
			
		}catch(Exception e){
			log.error("get morder fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
		}
	}
	//搜索运单
	@RequestMapping(value = "/admin/m_order/search",method = { RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public ResponseObject<PageSplit<M_order>> searchByKeyOfAdmin(
			HttpServletRequest request,
			@RequestParam(value = "oid", required = false) String oid,//运单号
			@RequestParam(value = "sod", required = false) String sod,//海关单号，即本单号关联的海关单号
			@RequestParam(value = "god", required = false) String god,//国内第三方快递单号
			@RequestParam(value = "wid", required = false) String wid,//所属门店id
			@RequestParam(value = "cid", required = false, defaultValue = "") String cid,//渠道id
			@RequestParam(value = "userinfo", required = false) String userinfo,//用户信息
			@RequestParam(value = "commudityinfo", required = false) String commudityinfo,//商品信息
			@RequestParam(value = "state", required = false) String state,//状态
			@RequestParam(value = "type", required = false) String type,//类型
			@RequestParam(value = "flyno", required = false) String flyno,//航班号
			@RequestParam(value = "sdate", required = false) String sdate,//创建的起始时间
			@RequestParam(value = "edate", required = false) String edate,//创建的结束时间
			@RequestParam(value = "psdate", required = false) String psdate,//支付的起始时间
			@RequestParam(value = "pedate", required = false) String pedate,//支付的结束时间
			@RequestParam(value = "payway", required = false) String payway,//支付方式
			@RequestParam(value = "cardinfo", required = false) String cardinfo,//身份证搜索选择
			@RequestParam(value = "payornot", required = false) String payornot,//是否已付款
			
			@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
			@RequestParam(value = "rows", required = false, defaultValue = "10") int rows
			) {
		try {
			
			if(StringUtil.isEmpty(payornot))
			{
				payornot=null;
			}
			else
			{
				if((!payornot.equalsIgnoreCase(Constant.ORDER_PAY_STATE0))&&(!payornot.equalsIgnoreCase(Constant.ORDER_PAY_STATE1)))
				{
					payornot=null;
				}
			}
			
			if(!StringUtil.isEmpty(wid)&&(wid.equalsIgnoreCase("-1")))
			{
				wid=null;
			}
			if(!StringUtil.isEmpty(cid)&&(cid.equalsIgnoreCase("-1")))
			{
				cid=null;
			}
			if(!StringUtil.isEmpty(state)&&(state.equalsIgnoreCase("-1")))
			{
				state=null;
			}
			if(!StringUtil.isEmpty(type)&&(type.equalsIgnoreCase("-1")))
			{
				type=null;
			}
			if(!StringUtil.isEmpty(payway)&&(payway.equalsIgnoreCase("-1")))
			{
				payway=null;
			}
			String storeid=null;
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
			{
				storeid=wid;//表示可以查找所有门店
				
			}else
			{
				storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
				if((!StringUtil.isEmpty(wid))&&(!wid.equalsIgnoreCase(storeid)))
				{
					return generateResponseObject(ResponseCode.PARAMETER_ERROR,
							"对不起，你不能访问其它门店!");
				}
				
				if((storeid==null)||(storeid.equalsIgnoreCase("")))
				{
					return generateResponseObject(ResponseCode.NEED_LOGIN,
							"你没有登陆!");
				}
			}
			
			
			/*if(userinfo!=null)
			{
			 userinfo = new String(request.getParameter("userinfo").getBytes("ISO-8859-1"), "utf-8");
				userinfo = URLDecoder.decode(userinfo, "UTF-8");
			}*/
			
			//key = new String(key.getBytes("ISO-8859-1"), "utf-8");
			if (StringUtil.isEmpty(sdate)
					|| !UserUtil.validateExportDate(sdate)) {
				sdate = "";
			} else {
				sdate = UserUtil.transformerDateString(sdate, " 00:00:00");
			}

			if (StringUtil.isEmpty(edate)
					|| !UserUtil.validateExportDate(edate)) {
				edate = "";
			} else {
				edate = UserUtil.transformerDateString(edate, " 23:59:59");
			}
			
			if (StringUtil.isEmpty(psdate)
					|| !UserUtil.validateExportDate(psdate)) {
				psdate = "";
			} else {
				psdate = UserUtil.transformerDateString(psdate, " 00:00:00");
			}

			if (StringUtil.isEmpty(pedate)
					|| !UserUtil.validateExportDate(pedate)) {
				pedate = "";
			} else {
				pedate = UserUtil.transformerDateString(pedate, " 23:59:59");
			}

			oid = StringUtil.isEmpty(oid) ? null : oid;
			sod = StringUtil.isEmpty(sod) ? null : sod;
			god = StringUtil.isEmpty(god) ? null : god;
			//String column = OrderUtil.getSearchColumnByType(type);
			state = OrderUtil.dealState(state);
			pageIndex = Math.max(pageIndex, 1);
			
			return this.m_orderService.searchMorders(oid, sod, god, flyno, storeid, cid, userinfo, commudityinfo, state, type,payway, sdate, edate, psdate, pedate,cardinfo,payornot, rows, pageIndex);
			//return new ResponseObject<PageSplit<Order>>();
			//return this.orderService.searchOrdersByKeys(oid, null, key, column,
			//		sdate, edate, state,typekey,storeid, defaultPageSize, pageIndex);
		} catch (Exception e) {
			log.error("查询运单失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "查询失败");
		}
	}
	
	
	
	
	//删除运单
	@RequestMapping(value = "/admin/m_order/delete", method = { RequestMethod.POST,RequestMethod.GET })
	@ResponseBody
	public ResponseObject<Object> deleteMorderOfAdmin(
			HttpServletRequest request,
			@RequestParam(value = "ids[]") String[] ids) {
		try {
			
			
			String storeid=null;
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
			{
				storeid=null;
				
			}else
			{
				storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
			
				
				if((storeid==null)||(storeid.equalsIgnoreCase("")))
				{
					return generateResponseObject(ResponseCode.NEED_LOGIN,
							"你没有登陆!");
				}
			}
			
			List<String> states=Arrays.asList(new String[] { Constant.ORDER_STATE0,
					Constant.ORDER_STATE1, Constant.ORDER_STATE2,
					Constant.ORDER_STATE10,Constant.ORDER_STATE__10,Constant.ORDER_STATE__9,"",null,"null" });
			return this.m_orderService.deleteMorderByIds(Arrays.asList(ids), states, storeid);
			//return this.m_orderService.deleteMorderByIds(ids, states, storeid);
			
		} catch (Exception e) {
			log.error("删除订单失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "删除订单失败");
		}
	}
	
	
	
	
	
	
	//一条运单的查询接口
		@RequestMapping(value = "/admin/m_route/getone", method = { RequestMethod.GET,
				RequestMethod.POST })
		@ResponseBody
		public ResponseObject<Object> getOneRouteByadmin(HttpServletRequest request,
				@RequestParam(value = "kuaidi_type") String kuaidiType,
				@RequestParam(value = "oid", required = false) String oid) {
			try {
				String storeid=null;
				String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
				if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
				{
					storeid=null;
					
				}else
				{
					storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
				
					
					if((storeid==null)||(storeid.equalsIgnoreCase("")))
					{
						return generateResponseObject(ResponseCode.NEED_LOGIN,
								"你没有登陆!");
					}
				}
				
				if(StringUtil.isEmpty(oid))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错!");
				}
				return this.m_orderService.getoneMorderRoute(kuaidiType, oid,storeid);
			} catch (Exception e) {
				log.error("获取运单路由失败");
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取运单路由失败");
			}
		}

	//一条运单的查询接口
	@RequestMapping(value = "/m_route/guest", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> getRouteByGust(HttpServletRequest request,
			@RequestParam(value = "kuaidi_type") String kuaidiType,
			@RequestParam(value = "oid", required = false) String oid) {
		try {
			if(StringUtil.isEmpty(oid))
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错!");
			}
			return this.m_orderService.getoneMorderRoute(kuaidiType, oid,null);
		} catch (Exception e) {
			log.error("获取运单路由失败");
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取运单路由失败");
		}
	}
	
	
	//用户一次获取多条运单的路由
		@RequestMapping(value = "/m_route/guest_routes", method = { RequestMethod.GET,
				RequestMethod.POST })
		@ResponseBody
		public ResponseObject<List<Map<String, Object>>> getRouteBymoreRoutes(HttpServletRequest request,
				@RequestParam(value = "oids", required = false) String[] oids) {
			try {
				if(oids==null||(oids.length<1))
				{
					return new ResponseObject<List<Map<String, Object>>>(ResponseCode.PARAMETER_ERROR,"参数出错!");
				}
				List<Map<String, Object>> route_result = new  ArrayList<Map<String, Object>>();
				
				
				
				
				
				for(int i=0;i<oids.length;i++)
				{
					if(!StringUtil.isEmpty(oids[i]))
					{
						oids[i] = oids[i].replaceAll("\r|\n", "");
					}
					else
					{
						continue;
					}
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("orderId", oids[i]);
					ResponseObject<Object> obj= this.m_orderService.getoneMorderRoute(Constant.KUAIDI_TYPE_KUAIDI100, oids[i],null);
					map.put("route", obj);
					/*if((obj==null)||(!ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj.getCode())))//查询失败
					{
						map.put("route", null);
					}
					else
					{
						map.put("route", obj.getData());
					}*/
					route_result.add(map);
				}
				ResponseObject<List<Map<String, Object>>> robj=new ResponseObject<List<Map<String, Object>>>(ResponseCode.SUCCESS_CODE);
				robj.setData(route_result);
				return robj;
			} catch (Exception e) {
				log.error("获取运单路由失败");
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取运单路由失败");
			}
		}
	
	//修改跟由状态,包含状态、第三方快递公司名称和单号
	@RequestMapping(value="/admin/m_order/modify_route_state", method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseObject<Object> modifyroutestate(HttpServletRequest request,
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "thirdPNS", required = false) String thirdPNS,
			@RequestParam(value = "thirdNo", required = false) String thirdNo){
		try{
			if(StringUtil.isEmpty(id))
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数id不能为空!");
			}
			if(StringUtil.isEmpty(state))
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"状态参数不能为空!");
			}
			
			String storeid=null;
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
			{
				storeid=null;//表示可以查找所有门店
				
			}else
			{
				storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
				if((storeid==null)||(storeid.equalsIgnoreCase("")))
				{
					return generateResponseObject(ResponseCode.NEED_LOGIN,
							"你没有登陆!");
				}
			}
			return this.m_orderService.modifyroutestate(id, state, thirdPNS, thirdNo, storeid);
			
		}catch(Exception e){
			log.error("get morder fail", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/admin/m_order/import_meitao_state", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> importOrderDataFromMeitaoStateExcel(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "sendmessage", required = false) String sendmessage,//发送短信标志
			@RequestParam(value = "uploadstate", required = false) MultipartFile file) {
		
		String wid=null;
		String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		
		if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
		{			
			String empId = StringUtil.obj2String(request.getSession().getAttribute(
					Constant.EMP_STORE_ID_SESSION_KEY));
			if(!StringUtil.isEmpty(empId))
			{
				wid=empId;
			}
			else
			{
				return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权进行此操作或没登陆!");
			}
			
			
			
		}
		if (file != null && file.getSize() > 0) {
			List<ImportMorder> importOrders = null;
			try {
				
				//kai 20151006 判定是不是excel表格
				String originalName = file.getOriginalFilename();
				if (!StringUtil.boolpicisgoodornot(originalName, defaultExcelFileType)) {
					return generateResponseObject(
							ResponseCode.CONSIGNEE_CARD_ERROR, "必须上传excel 2003表格,请重新尝试!");
				}
				
				
				importOrders = M_OrderUtil.readMorderExcel_meitao_state(file.getInputStream());
			} catch (OutOfMemoryError e) {
				log.error("内存不够",e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
						"内存不够,"+e.getMessage());
			} catch (Exception e) {
				log.error("读取数据出错", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
						"读取出错,"+e.getMessage());
			}
			if (!importOrders.isEmpty()) {
				try {
					// 导入员工名称
					String empName = StringUtil.obj2String(request.getSession()
							.getAttribute(Constant.EMP_ACCOUNT_SESSION_KEY));
					
					ResponseObject<Object> obj=this.m_orderService.importMorderState(importOrders, empName,wid,sendmessage);
					//ResponseObject<Object> obj= this.orderService.importExcelOfOrderState(
					//		importOrders, empName,wid);
					if(ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj.getCode()))
					{
						
	
							
					
							
							
							
							importOrders=(List<ImportMorder>)obj.getData();
							obj.setData(null);
							obj=null;
							if((importOrders==null)||(importOrders.size()<1))
							{
								return obj;
							}
							
							
							OutputStream os=null;
	
							try {
								
								
								
								String fileName = "excel_state_result_" + importOrders.size() + ".xls";
								// key = new String(key.getBytes("ISO-8859-1"), "utf-8");
								response.setContentType("application/vnd.ms-excel");
								response.setHeader("Content-disposition",
										"attachment;filename="
												+ new String(fileName.getBytes(), "utf-8"));
								// orders = this.orderService.getExportOrders(sdate, edate);
	
								File templeFile = new File(request.getSession()
										.getServletContext().getRealPath("/")
										+ this.orderOutputToStateResultTempletsFile);
								 os = response.getOutputStream();
	
								 M_OrderUtil.exportOrderStateToResult(importOrders, templeFile, os);
								
								//util.exportOrderToWeiyiExcel(orders, templeFile, os);
								//util.exportOrderToMeitao(orders, templeFile, os);//kai 20151027 导出美淘模板
								
	
							} catch (Exception e) {
								
								throw new Exception("获取导出运单数据出现异常，无法获取数据", e);
							} finally {
							
								if (os != null) {
									try {
										os.flush();
										os.close();
									} catch (IOException e) {
										// ignore
									}
								}
							}
						
							
						
							
						
						
					}
					else
					{
						return obj;
					}
				} catch (Exception e) {
					log.error("修改数据库失败", e);
					return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
							"修改数据库失败,原因" + e.getMessage());
				}
			} else {
				return generateResponseObject(ResponseCode.PARAMETER_ERROR,
						"文件内容不能为空,请检查是否有空行或运单号为空!");
			}
		}
		return generateResponseObject(ResponseCode.PARAMETER_ERROR, "文件不能为空");
	}
	
	
	
	// 下载订单批量上传的模板
	@RequestMapping(value = "/admin/m_order/download_exceluploadstate", method = { RequestMethod.GET })
	public void getImportmeitaoMordersStateDataExcelFile(
			HttpServletRequest request, HttpServletResponse response) {
		InputStream input = null;
		ServletOutputStream os=null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename="
					+ new String("state_upload_excel.xls".getBytes(),
							"utf-8"));
			input = request
					.getSession()
					.getServletContext()
					.getResourceAsStream(
							this.orderImportMeitaoOrdersstateTempletsFile);
			os = response.getOutputStream();
			byte[] buffer = new byte[1024];
			int n = 0;
			while ((n = input.read(buffer)) > 0) {
				os.write(buffer, 0, n);
			}
			buffer=null;
			os.flush();
		} catch (Exception e) {
			log.error("下载文件失败", e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					// ignore
				}
			}
			if (os != null) {
				try {
					os.flush();
					os.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}
	
	
	
	@RequestMapping(value = "/admin/m_order/download_info", method = { RequestMethod.GET , RequestMethod.POST})
	@ResponseBody
	public ResponseObject<Object> searchByKeyforexporepics(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "button_submit", required = false, defaultValue = "") String button_submit,
			@RequestParam(value = "choose_pics", required = false, defaultValue = "") String[] pics_type,
			//@RequestParam(value = "o_order_checked_commoditys", required = false, defaultValue = "") String[] comm_type,//商品下载类型
			@RequestParam(value = "checked_ids", required = false, defaultValue = "") String[] ids)
			throws Exception {
		if((ids==null)||(ids.length<1))
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"必须选择一条运单!");
		}
		if(StringUtil.isEmpty(button_submit))
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错！");
		}
		else
		{
			//String storeId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
			
			String storeId=null;
			String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
			{
				storeId=null;//表示可以查找所有门店
				
			}else
			{
				storeId = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
				if((storeId==null)||(storeId.equalsIgnoreCase("")))
				{
					return generateResponseObject(ResponseCode.NEED_LOGIN,
							"你没有登陆!");
				}
			}
			
			
			
			if(button_submit.equalsIgnoreCase("pictures"))//下载图片
			{
				if((pics_type==null)||(pics_type.length<1))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"必须选择图片类型!");
				}
				else
				{


				

					InputStream imgInputStream = null;
					// 输出流
					OutputStream os = null;
					ZipOutputStream out=null;
					FileInputStream in=null;
					try {
						List<Receive_User> ruser=new ArrayList<Receive_User>();
						for(String id:ids)
						{
							M_order order=this.m_orderDao.getRuserbyid(id, storeId);
							if((order!=null)&&(order.getRuser()!=null))
							{
								boolean flag=false;
								for(String type:pics_type)
								{
									if((type!=null)&&(type.equalsIgnoreCase("0")))//正面图片
									{
										if(!StringUtil.isEmpty(order.getRuser().getCardurl()))
										{
											flag=true;
											break;
										}
									}
									else if((type!=null)&&(type.equalsIgnoreCase("1")))//反面图片
									{
										if(!StringUtil.isEmpty(order.getRuser().getCardother()))
										{
											flag=true;
											break;
										}
									}
									else if((type!=null)&&(type.equalsIgnoreCase("2")))//合图
									{
										if(!StringUtil.isEmpty(order.getRuser().getCardtogether()))
										{
											flag=true;
											break;
										}
									}
								}
								if(flag==true)
								{
									ruser.add(order.getRuser());
								}
								
							}
						}
						
						if((ruser==null)||(ruser.size()<1))
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"没有找到任何图片!");
						}
						
						
						

						// 解决火狐的反斜杠问题 kai 20151006
						//String filetype = this.defaultCardFileType;// 要上传的文件类型
						String strtest = this.save258CardDir;
						String strseparator = "";
						if (strtest.indexOf("/") >= 0)// 包含有“/”字符，分隔符用此字符
						{
							strseparator = "/";
						} else {
							strseparator = File.separator;
						}
						// 获取当时的时间缀
						Date date = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyyMMddHHmmss");
						String str = sdf.format(date);

						String originalName = str + ".zip";
						int index = originalName.lastIndexOf('.');
						index = Math.max(index, 0);
						String fileName = this.save258CardDir + File.separator + "temp"
								+ strseparator + StringUtil.generateRandomString(5)
								+ "_" + StringUtil.generateRandomInteger(5) + "_"
								// + originalName.substring(index);
								+ originalName;

						File zipfile = new File(request.getSession()
								.getServletContext().getRealPath("/")
								+ fileName);
						

							//String fileName = null;
							File filetemp;
							//File[] srcfile = new File[(ruser.size())
							//		* pics_type.length];//保存文件图片路径
							//String[] rename = new String[(ruser.size())
							 // 							* pics_type.length];// 保存用于检查是否存在重复的图片
							List<String> rename=new ArrayList<String>();
							out=new ZipOutputStream(new FileOutputStream(zipfile));
						   byte[] buf=new byte[1024];
							//int ii = 0;
							for (int j = 0; j < pics_type.length; j++) {
								for (Receive_User o : ruser) {
									String ordurl = "";
									
									if (pics_type[j].equalsIgnoreCase("0"))// 身份证正面图
									{
										ordurl = o.getCardurl();
										
									} else if (pics_type[j].equalsIgnoreCase("1"))// 身份证反面图
									{
										ordurl = o.getCardother();
										
									} else if (pics_type[j].equalsIgnoreCase("2"))// 身份证合成图
									{
										ordurl = o.getCardtogether();
										
									} else {
										continue;
									}
									if (StringUtil.isEmpty(ordurl)) {
										continue;
									}
									File file1 = new File(request.getSession()
											.getServletContext().getRealPath("/")
											+ ordurl);
									if ((!(StringUtil.isEmpty(ordurl)))
											&& (file1.exists())) {// 文件存在时才操作
										filetemp = new File(request.getSession()
												.getServletContext().getRealPath("/")
												+ ordurl);
										//srcfile[ii] = filetemp;
										

										    in=new FileInputStream(filetemp);
							                String strname;//要保证不重名，否则会出错
							              
							                strname=o.getCardid()+o.getName()+".jpg";
							                boolean nameflag=false;
							                if(strname.equalsIgnoreCase(".jpg"))//合成文件路径
							                {
							                	continue;
							                }
							                else
							                {
							                	for(String namejpg:rename)
							                	{
							                		if(StringUtil.isEmpty(namejpg))
							                		{
							                			continue;
							                		}
							                		if(strname.equalsIgnoreCase(namejpg))
							                		{
							                			nameflag=true;
							                			break;
							                		}
							                	}
							                }
							                if(nameflag==true)//表示已经有重复
							                {
							                	continue;
							                }
							                else
							                {
							                	rename.add(strname);
							                }
							                
							                
							                out.putNextEntry(new ZipEntry(strname));
							                int len;
							                while((len=in.read(buf))>0){
							                    out.write(buf,0,len);
							                }
							                buf=null;
							                out.closeEntry();
							                in.close();
										//ii++;
									}

								}
							}
							out.close();
							

							// 压缩后的文件
							

					
							//basiczip.zipFiles(srcfile, file1, prename);
							response.setContentType("application/zip");
							response.setHeader("Content-Disposition", 
									"attachment; filename=\"" + originalName + "\""); 
							//response.setHeader("Location","download.zip");
							imgInputStream = new FileInputStream(zipfile);

							byte[] imgBytes = IOUtils.toByteArray(imgInputStream);

							os = response.getOutputStream();

							os.write(imgBytes);
							

					

						// return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
						// "查询失败");
					} catch (Exception e) {
						log.error("压缩出现异常", e);
						return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"压缩出现异常");
					} finally {
						// orders.clear();
						// 6.无论成功与否关闭相应的流
						try {
							if (imgInputStream != null) {
								imgInputStream.close();
							}
							if (os != null) {
								os.flush();
								os.close();
							}
							if (out != null) {
								os.flush();
								os.close();
							}
							if (in != null) {
								
								in.close();
							}
						} catch (IOException e) {
							System.err.println(e.getMessage());
						}

					}
				
				}
			}
			else if(button_submit.equalsIgnoreCase("orders"))//下载运单
			{
				// 添加订单下载功能
				
				

				List<M_order> orders = new ArrayList<M_order>();
				OutputStream os = null;
				try {
					for(String id:ids)
					{
						M_order order=this.m_orderDao.getById(id, storeId);
						if(order!=null)
						{
							orders.add(order);
						}
					}
					
					

				
					
					String fileName = "download_orders_" + orders.size() + ".xls";
					// key = new String(key.getBytes("ISO-8859-1"), "utf-8");
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-disposition",
							"attachment;filename="
									+ new String(fileName.getBytes(), "utf-8"));
					// orders = this.orderService.getExportOrders(sdate, edate);

					/*File templeFile = new File(request.getSession()
							.getServletContext().getRealPath("/")
							+ this.orderOutputToWeiyiTempletsFile);*/
					File templeFile = new File(request.getSession()
							.getServletContext().getRealPath("/")
							+ this.orderOutputToMeitaonewTempletsFile);;
					
					os = response.getOutputStream();
					
					// 导出数据唯一快递单子
					
					
					M_OrderUtil.exportOrderToMeitao(orders, templeFile, os);
					


				} catch (Exception e) {
					if (os != null) {
						
						os.flush();
						
				
					}
					log.error("获取运单数据失败", e);
					//throw new Exception("获取导出运单数据出现异常，无法获取数据", e);
					return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"发生异常");
				} finally {
					orders.clear();
					if (os != null) {
						try {
							os.flush();
						//	os.clear();  
							//out = pageContext.pushBody();  
							os.close();
						} catch (IOException e) {
							// ignore
						}
					}
					//return null;
				}
			
			}
			else
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错！");
			}
		}
		
		
		return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"成功!");
	}
	
	
	
	//搜索运单
		@RequestMapping(value = "/admin/m_order/search_download",method = { RequestMethod.GET, RequestMethod.POST})
		@ResponseBody
		public ResponseObject<PageSplit<M_order>> downloadbydirect(
				HttpServletRequest request,
				HttpServletResponse response,
				@RequestParam(value = "oid", required = false) String oid,//运单号
				@RequestParam(value = "sod", required = false) String sod,//海关单号，即本单号关联的海关单号
				@RequestParam(value = "god", required = false) String god,//国内第三方快递单号
				@RequestParam(value = "wid", required = false) String wid,//所属门店id
				@RequestParam(value = "cid", required = false, defaultValue = "") String cid,//渠道id
				@RequestParam(value = "userinfo", required = false) String userinfo,//用户信息
				@RequestParam(value = "commudityinfo", required = false) String commudityinfo,//商品信息
				@RequestParam(value = "state", required = false) String state,//状态
				@RequestParam(value = "type", required = false) String type,//类型
				@RequestParam(value = "flyno", required = false) String flyno,//航班号
				@RequestParam(value = "sdate", required = false) String sdate,//创建的起始时间
				@RequestParam(value = "edate", required = false) String edate,//创建的结束时间
				@RequestParam(value = "psdate", required = false) String psdate,//支付的起始时间
				@RequestParam(value = "pedate", required = false) String pedate,//支付的结束时间
				@RequestParam(value = "payway", required = false) String payway,//支付方式
				@RequestParam(value = "payornot", required = false) String payornot,//是否支付
				//@RequestParam(value = "cardinfo", required = false) String cardinfo,//身份证搜索选择
				@RequestParam(value = "card_id", required = false) String card_id,
				@RequestParam(value = "card_url", required = false) String card_url,
				@RequestParam(value = "card_other", required = false) String card_other,
				@RequestParam(value = "card_together", required = false) String card_together,
				@RequestParam(value = "card_upload", required = false) String card_upload
				//@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
				//@RequestParam(value = "rows", required = false, defaultValue = "10") int rows
				) {
			try {
		
				
				
				
				String cardinfo="";
				if(!StringUtil.isEmpty(card_id)&&(card_id.equalsIgnoreCase("on")))//
				{
					if(cardinfo.equalsIgnoreCase(""))
					{
						cardinfo="0";
					}
					else
					{
						cardinfo=cardinfo+",0";
					}
				}
				if(!StringUtil.isEmpty(card_url)&&(card_url.equalsIgnoreCase("on")))//
				{
					if(cardinfo.equalsIgnoreCase(""))
					{
						cardinfo="1";
					}
					else
					{
						cardinfo=cardinfo+",1";
					}
				}
				if(!StringUtil.isEmpty(card_other)&&(card_other.equalsIgnoreCase("on")))//
				{
					if(cardinfo.equalsIgnoreCase(""))
					{
						cardinfo="2";
					}
					else
					{
						cardinfo=cardinfo+",2";
					}
				}
				if(!StringUtil.isEmpty(card_together)&&(card_together.equalsIgnoreCase("on")))//
				{
					if(cardinfo.equalsIgnoreCase(""))
					{
						cardinfo="3";
					}
					else
					{
						cardinfo=cardinfo+",3";
					}
				}
				if(!StringUtil.isEmpty(card_upload)&&(card_upload.equalsIgnoreCase("on")))//
				{
					if(cardinfo.equalsIgnoreCase(""))
					{
						cardinfo="4";
					}
					else
					{
						cardinfo=cardinfo+",4";
					}
				}
				ResponseObject<PageSplit<M_order>> obj=this.searchByKeyOfAdmin(request, null, null, null, wid, cid, userinfo, commudityinfo, state, type, flyno, sdate, edate, psdate, pedate, payway, cardinfo,payornot, 1, 0x7fffffff);
				List<M_order> orders=null;
				if((obj!=null)&&(obj.getCode().equalsIgnoreCase(ResponseCode.SUCCESS_CODE)))//成功
				{
					orders=(List<M_order>)obj.getData().getDatas();
					obj.getData().setDatas(null);
					obj.setData(null);
					obj=null;
				}
				else
				{
					return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取导出运单数据出现异常，无法获取数据");
				}
				
				if(orders!=null)
				{

					// 添加订单下载功能
					
					

					
					OutputStream os = null;
					try {
						
						
						

					
						
						String fileName = "download_orders_" + orders.size() + ".xls";
						// key = new String(key.getBytes("ISO-8859-1"), "utf-8");
						response.setContentType("application/vnd.ms-excel");
						response.setHeader("Content-disposition",
								"attachment;filename="
										+ new String(fileName.getBytes(), "utf-8"));
						// orders = this.orderService.getExportOrders(sdate, edate);

						/*File templeFile = new File(request.getSession()
								.getServletContext().getRealPath("/")
								+ this.orderOutputToWeiyiTempletsFile);*/
						File templeFile = new File(request.getSession()
								.getServletContext().getRealPath("/")
								+ this.orderOutputToMeitaonewTempletsFile);;
						
						os = response.getOutputStream();

						// 导出数据唯一快递单子
						
						
						M_OrderUtil.exportOrderToMeitao(orders, templeFile, os);
						


					} catch (Exception e) {
						log.error("获取运单数据失败", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取导出运单数据出现异常，无法获取数据");
						//throw new Exception("获取导出运单数据出现异常，无法获取数据", e);
					} finally {
						orders.clear();
						if (os != null) {
							try {
								os.flush();
								os.close();
							} catch (IOException e) {
								// ignore
							}
						}
					}
				
				
				}
				
				//pageIndex = Math.max(pageIndex, 1);
				return generateResponseObject(ResponseCode.SUCCESS_CODE, "操作成功");
				//return this.m_orderService.searchMorders(oid, sod, god, flyno, storeid, cid, userinfo, commudityinfo, state, type,payway, sdate, edate, psdate, pedate,cardinfo, rows, pageIndex);
				//return new ResponseObject<PageSplit<Order>>();
				//return this.orderService.searchOrdersByKeys(oid, null, key, column,
				//		sdate, edate, state,typekey,storeid, defaultPageSize, pageIndex);
			} catch (Exception e) {
				log.error("操作运单失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "操作失败");
			}
		}
		
		
		//门市店中的搜索运单
		@RequestMapping(value = "/admin/m_order/search_emp",method = { RequestMethod.GET, RequestMethod.POST})
		@ResponseBody
		public ResponseObject<PageSplit<M_order>> searchByKeyOfEmp(
				HttpServletRequest request,
				@RequestParam(value = "history_cid", required = false, defaultValue = "") String cid,//要查找的渠道信息
				@RequestParam(value = "history_userinfo", required = false) String userinfo,//查找的用户信息
				@RequestParam(value = "history_state", required = false) String state,//商品信息
				@RequestParam(value = "history_time", required = false) String time,//1是今天，-1或其它表示任何时间	
				@RequestParam(value = "history_payornot", required = false) String payornot,//支付状态	
				@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
				@RequestParam(value = "rows", required = false, defaultValue = "10") int rows
				) {
			try {
				
				if(StringUtil.isEmpty(payornot))
				{
					payornot=null;
				}
				else
				{
					if((!payornot.equalsIgnoreCase(Constant.ORDER_PAY_STATE0))&&(!payornot.equalsIgnoreCase(Constant.ORDER_PAY_STATE1)))
					{
						payornot=null;
					}
				}
				
				if(!StringUtil.isEmpty(cid)&&(cid.equalsIgnoreCase("-1")))
				{
					cid=null;
				}
				if(!StringUtil.isEmpty(state)&&(state.equalsIgnoreCase("-1")))
				{
					state=null;
				}
				if(StringUtil.isEmpty(state))
				{
					 return generateResponseObject(ResponseCode.PARAMETER_ERROR, "搜索状态不能为空!");
				}
				else
				{
					if(state.equalsIgnoreCase(Constant.ORDER_STATE1)||state.equalsIgnoreCase(Constant.ORDER_STATE2)||state.equalsIgnoreCase(Constant.ORDER_STATE3)||state.equalsIgnoreCase(Constant.ORDER_STATE__10))
					{
						
					}
					else
					{
						return generateResponseObject(ResponseCode.PARAMETER_ERROR, "搜索状态值不存在");
					}
				}
				String storeid=null;
				String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
				String empId=null;
				if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
				{
					storeid=null;//表示查找所有门店
					
				}else
				{
					storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
					empId=(String)request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY);//员工id
				
					if((storeid==null)||(storeid.equalsIgnoreCase("")))
					{
						return generateResponseObject(ResponseCode.NEED_LOGIN,
								"你没有登陆!");
					}
				}
				
				if(state.equalsIgnoreCase(Constant.ORDER_STATE__10))
				{
					//storeid=null;
					empId=null;
				}
				
				String today1=null;
				if(!StringUtil.isEmpty(time)&&(time.equalsIgnoreCase("1")))//表示今天的运单
				{
					//String date = DateUtil.date2String(new Date());
					Date date = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd");
					String timestr = sdf.format(date);
					
					today1 = UserUtil.transformerDateString(timestr, " 00:00:00");//设置为今天的时间
					
				}
			
				//String column = OrderUtil.getSearchColumnByType(type);
				state = OrderUtil.dealState(state);
				pageIndex = Math.max(pageIndex, 1);
				return this.m_orderService.searchMsMorders(empId, storeid, cid, userinfo, state, today1, payornot,rows, pageIndex);
			
			} catch (Exception e) {
				log.error("查询运单失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "查询失败");
			}
		}
		
		
		//直接修改状态
		@RequestMapping(value="/admin/m_order/modify_ms_state", method={RequestMethod.POST,RequestMethod.GET})
		@ResponseBody
		public ResponseObject<Object> modifyroutestatebyMs(HttpServletRequest request,
				@RequestParam(value = "id", required = false) String id,
				@RequestParam(value = "state", required = false) String state){
			try{
				if(StringUtil.isEmpty(id))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数id不能为空!");
				}
				if(StringUtil.isEmpty(state))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"状态参数不能为空!");
				}
				else
				{
					if(state.equalsIgnoreCase(Constant.ORDER_STATE1)||state.equalsIgnoreCase(Constant.ORDER_STATE2)||state.equalsIgnoreCase(Constant.ORDER_STATE3))
					{
					}
					else
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"状态参数出错!");
					}
				}
				
				String storeid=null;
				String empid = (String)request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY);
				String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
				if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
				{
					storeid=null;//表示可以查找所有门店
					
				}else
				{
					storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
					
					if((storeid==null)||(storeid.equalsIgnoreCase("")))
					{
						return generateResponseObject(ResponseCode.NEED_LOGIN,
								"你没有登陆!");
					}
				}
				String date = DateUtil.date2String(new Date());
				return this.m_orderService.modifyMsroutestate(empid, date, id,state, storeid);
				
				
				
				//return this.m_orderService.modifyroutestate(id, state, thirdPNS, thirdNo, storeid);
				
			}catch(Exception e){
				log.error("get morder fail", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
			}
		}
		
		
		
		//门店修改信息
				@RequestMapping(value="/admin/m_order/modify_ms_info", method={RequestMethod.POST,RequestMethod.GET})
				@ResponseBody
				public ResponseObject<Object> modifyMorderInfobyMs(HttpServletRequest request,
						@RequestParam(value = "id", required = false) String id,
						@RequestParam(value = "state", required = false) String state,
						@RequestParam(value = "payornot", required = false) String payornot,
						@RequestParam(value = "qremark", required = false) String qremark
						){
					try{
						if(StringUtil.isEmpty(id))
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数id不能为空!");
						}
						String storeid=null;
						String empid = (String)request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY);
						String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
						String storeid1=(String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
						if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
						{
							storeid=null;//表示可以查找所有门店
							
						}else
						{
							storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
							
							if((storeid==null)||(storeid.equalsIgnoreCase("")))
							{
								return generateResponseObject(ResponseCode.NEED_LOGIN,
										"你没有登陆!");
							}
						}
						
						M_order order=this.m_orderDao.getById(id, storeid);
						if(order==null)//查找运单失败
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"查找不到对应的运单!");
						}
						//order.setEmployeeId(empid);
						order.setSorderId(storeid);
						order.setQremark(qremark);
						
						//作废运单不能操作
						if((order.getState()!=null)&&(order.getState().equalsIgnoreCase(Constant.ORDER_STATE1)))
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"已作废的运单不能再操作!");
						}
						
						
					
						if(StringUtil.isEmpty(payornot)||((!payornot.equalsIgnoreCase(Constant.ORDER_PAY_STATE0))&&(!payornot.equalsIgnoreCase(Constant.ORDER_PAY_STATE1))))
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"支付状态只能是未付款或已付款!");
						}
						else
						{
							if(!payornot.equalsIgnoreCase(order.getPayornot()))//支付状态发生改变
							{
								if(!StringUtil.isEmpty(order.getPayWay())&&((order.getPayWay().equalsIgnoreCase("0")||order.getPayWay().equalsIgnoreCase("2"))))//余额支付，用户自付不能更改
								{
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"余额支付或自户自付的运单不能更改支付状态");
								}
							}
						}
						
						
						
						if(StringUtil.isEmpty(state))
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"状态参数不能为空!");
						}
						else
						{
							if(state.equalsIgnoreCase(Constant.ORDER_STATE1)||state.equalsIgnoreCase(Constant.ORDER_STATE2)||state.equalsIgnoreCase(Constant.ORDER_STATE3))
							{
								
							}
							else
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"状态参数出错!");
							}
						}
						
						
						
						
						return this.m_orderService.modifyMsInfo(order, state, payornot,empid,storeid1);
						
						//return this.m_orderService.modifyMsroutestate(empid, date, id,state, storeid);
						
						
						
						//return this.m_orderService.modifyroutestate(id, state, thirdPNS, thirdNo, storeid);
						
					}catch(Exception e){
						log.error("get morder fail", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
					}
				}
		
		
		//共取打印清单列表
		@RequestMapping(value="/admin/m_order/get_many", method={RequestMethod.POST,RequestMethod.GET})
		@ResponseBody
		public ResponseObject<Object> getmanyorderids(HttpServletRequest request,
				@RequestParam(value = "ids", required = false) String[] ids){
			try{
				if((ids==null)||ids.length<1)
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数id不能为空!");
				}
				
				String storeid=null;
				String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
				if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
				{
					storeid=null;//表示可以查找所有门店
					
				}else
				{
					storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
					if((storeid==null)||(storeid.equalsIgnoreCase("")))
					{
						return generateResponseObject(ResponseCode.NEED_LOGIN,
								"你没有登陆!");
					}
				}
				List<M_order> orders=new ArrayList<M_order>();
				for(int i=0;i<ids.length;i++)
				{
					M_order order=this.m_orderDao.getById(ids[i], storeid);
					if(order!=null)
					{
						Employee emp=this.employeeDao.getEmployeeById(order.getEmployeeId());
						if(emp!=null)
						{
							order.setEmployeeName(emp.getAccount());
						}
						emp=this.employeeDao.getEmployeeById(order.getI_employeeId());
						if(emp!=null)
						{
							order.setI_employeeName(emp.getAccount());
						}
						Warehouse ware=this.warehouseDao.getById(order.getStoreId());
						if(ware!=null)
						{
							order.setStoreName(ware.getName());
						}
						ware=this.warehouseDao.getById(order.getI_storeId());
						
						if(ware!=null)
						{
							order.setI_storeName(ware.getName());
						}
						orders.add(order);
					}
				}
				if(orders.size()<1)
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取运单失败!");
				}
				ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
				obj.setData(orders);
				return obj;
				
				
			}catch(Exception e){
				log.error("get morder fail", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
			}
		}
		
		
		
		//获取入库清单信息
				@RequestMapping(value="/admin/m_order/get_in_many", method={RequestMethod.POST,RequestMethod.GET})
				@ResponseBody
				public ResponseObject<Object> getmanyorderidsandinput(HttpServletRequest request,
						@RequestParam(value = "ids", required = false) String[] ids){
					try{
						if((ids==null)||ids.length<1)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数id不能为空!");
						}
						
						String storeid=null;
						String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
						if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
						{
							storeid=null;//表示可以查找所有门店
							
						}else
						{
							storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
							if((storeid==null)||(storeid.equalsIgnoreCase("")))
							{
								return generateResponseObject(ResponseCode.NEED_LOGIN,
										"你没有登陆!");
							}
						}
						List<M_order> orders=new ArrayList<M_order>();
						for(int i=0;i<ids.length;i++)
						{
							M_order order=this.m_orderDao.getByjinId(ids[i], storeid);
							if(order!=null)
							{
								Employee emp=this.employeeDao.getEmployeeById(order.getEmployeeId());
								if(emp!=null)
								{
									order.setEmployeeName(emp.getAccount());
								}
								emp=this.employeeDao.getEmployeeById(order.getI_employeeId());
								if(emp!=null)
								{
									order.setI_employeeName(emp.getAccount());
								}
								Warehouse ware=this.warehouseDao.getById(order.getStoreId());
								if(ware!=null)
								{
									order.setStoreName(ware.getName());
								}
								ware=this.warehouseDao.getById(order.getI_storeId());
								
								if(ware!=null)
								{
									order.setI_storeName(ware.getName());
								}
								orders.add(order);
							}
						}
						if(orders.size()<1)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取运单失败!");
						}
						ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
						obj.setData(orders);
						return obj;
						
						
					}catch(Exception e){
						log.error("get morder fail", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
					}
				}
		
		
		@RequestMapping(value="/guest/upload_cardid", produces="text/plain;charset=UTF-8",method={RequestMethod.POST,RequestMethod.GET})
		@ResponseBody
		public String uploadcardid(HttpServletRequest request,
				HttpServletResponse response,
				@RequestParam(value = "orderId", required = false) String orderId,//上传身份证的运单号
				@RequestParam(value = "username", required = false) String username,//上传身份证收件人对应的姓名
				@RequestParam(value = "cardId", required = false) String cardId,//上传的身份证号码
				//@RequestParam(value = "cardfront", required = false) String cardfront,//上传的身份证前端图
				//@RequestParam(value = "cardother", required = false) String cardother//上传的身份证反端图
				
				
				@RequestParam(value = "cardfront_data", required = false) String cardfront_data,//压缩后的正面图片
				@RequestParam(value = "cardother_data", required = false) String cardother_data,//压缩后的反面图片
				
				@RequestParam(value = "cardfront", required = false) MultipartFile cardfront,
				@RequestParam(value = "cardother", required = false) MultipartFile cardother
				){
			//return "{\"code\":200}";
			
			
			
			
				ResponseString responseString=new ResponseString();
		
				
				//测试压缩的图片
				
				
				
				
				
				
				if(StringUtil.isEmpty(orderId))
				{
					responseString.setCode(ResponseCode.PARAMETER_ERROR);
					responseString.setMessage("运单号不能为空！");
					return responseString.toString();
					//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"运单号不能为空！"); 
				}
				orderId=orderId.trim();
				if(StringUtil.isEmpty(username))
				{
					responseString.setCode(ResponseCode.PARAMETER_ERROR);
					responseString.setMessage("身份证姓名不能为空！");
					return responseString.toString();
					//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"身份证姓名不能为空！"); 
				}
				username=username.trim();
				if(StringUtil.isEmpty(cardId))
				{
					responseString.setCode(ResponseCode.PARAMETER_ERROR);
					responseString.setMessage("身份证号码不能为空！");
					return responseString.toString();
					//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"身份证号码不能为空！"); 
				}
				else
				{
					cardId=cardId.trim();
					if (!ConsigneeInfoUtil.validateCardId(cardId)) {
						responseString.setCode(ResponseCode.CONSIGNEE_CARD_ID_ERROR);
						responseString.setMessage("身份证号码填写错误，请重新填写！");
						return responseString.toString();
						//return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
						//		"身份证号码填写错误，请重新填写！");
					}
				}
				
				if(((cardfront==null)||(cardfront.getSize()==0))&&(StringUtil.isEmpty(cardfront_data)||cardfront_data.length()<23))
				{
					responseString.setCode(ResponseCode.PARAMETER_ERROR);
					responseString.setMessage("必须上传身份证正面图片！");
					return responseString.toString();
					//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"必须上传身份证正面图片！"); 
				}
				if(((cardother==null)||(cardother.getSize()==0))&&(StringUtil.isEmpty(cardother_data)||cardother_data.length()<23))
				{
					responseString.setCode(ResponseCode.PARAMETER_ERROR);
					responseString.setMessage("必须上传身份证背面图片！");
					return responseString.toString();
					//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"必须上传身份证背面图片！"); 
				}
			
				
				//获取运单号是否存在并判断是否合法
				Receive_User rev=null;
				try
				{
				M_order order=this.m_orderDao.getRouteArgsbyOrderId(orderId.trim(), null);
				if(order==null)
				{
					responseString.setCode(ResponseCode.PARAMETER_ERROR);
					responseString.setMessage("上传的运单号不存在！");
					return responseString.toString();
					//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"上传的运单号不存在！");
				}
				else//检查运单号收件人信息
				{
					if(StringUtil.isEmpty(order.getRuserId()))//收件人信息为空
					{
						responseString.setCode(ResponseCode.PARAMETER_ERROR);
						responseString.setMessage("上传的运单信息有误！");
						return responseString.toString();
						//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"上传的运单信息有误！");
					}
					else
					{
						 rev=this.receive_UserDao.getById(Integer.parseInt(order.getRuserId()));
						if(rev==null)
						{
							responseString.setCode(ResponseCode.PARAMETER_ERROR);
							responseString.setMessage("上传的运单信息有误！");
							return responseString.toString();
							//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"上传的运单信息有误！");
						}
						else
						{
							if(!username.trim().equalsIgnoreCase(rev.getName().trim()))
							{
								responseString.setCode(ResponseCode.PARAMETER_ERROR);
								responseString.setMessage("上传的运单号姓名不匹配！");
								return responseString.toString();
								//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"上传的运单号姓名不匹配！");
							}
						}
					}
				}
				
				// 处理提交上来的图片
				// 解决火狐的反斜杠问题 kai 20151006
				String filetype = this.defaultCardFileType;// 要上传的文件类型
				String strtest = this.save258CardDir;
				String strseparator = "";
				if (strtest.indexOf("/") >= 0)// 包含有“/”字符，分隔符用此字符
				{
					strseparator = "/";
				} else {
					strseparator = File.separator;
				}
				//开始处理图片
				// 获取当时的时间缀
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyyMMddHHmmss");
				String timestr = sdf.format(date);
				//如果上传的图片中包含合成图片，那么即会忽略其它的正反两面图片
				
				String fileName = null;
				boolean flag=false;
				if(!(StringUtil.isEmpty(cardfront_data)||cardfront_data.length()<23))//没有压缩代码
				{
					int aa1=cardfront_data.indexOf(",");
					if(aa1>0)
					{
						cardfront_data=cardfront_data.substring(aa1+1, cardfront_data.length());
					}
					fileName = this.save258CardDir + strseparator + timestr + "_"+orderId+ "_"+"zm"+"_"
							+ StringUtil.generateRandomString(5) + "_"
							+ StringUtil.generateRandomInteger(5)
							+ ".jpeg";
					
					String daveurl=request.getSession().getServletContext()
							.getRealPath("/")+fileName;
					
					byte[] datas = DatatypeConverter
							.parseBase64Binary(cardfront_data);
					File imageFile = new File(daveurl);
					//File imageFile = new File("aaaaaaaa.png");
					try{
					FileOutputStream outStream = new FileOutputStream(
							imageFile);
					outStream.write(datas);
					outStream.close();
					flag=true;
					}
					catch(Exception e)
					{
						e.getStackTrace();
					}
				}
				
				String originalName="";
				int index=0;
				if(flag==false)//没有压缩代码
				{
					if (cardfront.getSize() > this.defaultCardFileSize) {
						responseString.setCode(ResponseCode.PARAMETER_ERROR);
						responseString.setMessage("图像文件过大,请重新尝试!");
						return responseString.toString();
						//return generateResponseObject(
						//		ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
					}

					originalName = cardfront.getOriginalFilename();

					if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
						responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
						responseString.setMessage("上传图像文件格式不对,请重新尝试!");
						return responseString.toString();
						//return generateResponseObject(
						//		ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
					}
					index = originalName.lastIndexOf('.');
					index = Math.max(index, 0);
					
					fileName = this.save258CardDir + strseparator + timestr + "_"+orderId+ "_"+"zm"+"_"
							+ StringUtil.generateRandomString(5) + "_"
							+ StringUtil.generateRandomInteger(5)
							+ originalName.substring(index);
					try {
						File file1 = new File(request.getSession().getServletContext()
								.getRealPath("/")
								+ fileName);
						cardfront.transferTo(file1);
					} catch (Exception e) {
						log.error("保存用户正图像失败,请不要上传图像！", e);
						responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
						responseString.setMessage("保存用户正图像失败，请去除上传图像后再尝试!");
						return responseString.toString();
						//return generateResponseObject(
						//		ResponseCode.CONSIGNEE_CARD_ERROR,
						//		"保存用户正图像失败，请去除上传图像后再尝试!");
					}
				}
				
					
					//处理反面图片
					String fileotherName = null;
					
					
					
					boolean flago=false;
					if(!(StringUtil.isEmpty(cardother_data)||cardother_data.length()<23))//没有压缩代码
					{
						int aa1=cardother_data.indexOf(",");
						if(aa1>0)
						{
							cardother_data=cardother_data.substring(aa1+1, cardother_data.length());
						}
						fileotherName = this.save258CardDir + strseparator + timestr + "_"+orderId+ "_"+"fm"+"_"
								+ StringUtil.generateRandomString(5) + "_"
								+ StringUtil.generateRandomInteger(5)
								+ ".jpeg";
						
						String daveurl=request.getSession().getServletContext()
								.getRealPath("/")+fileotherName;
						
						byte[] datas = DatatypeConverter
								.parseBase64Binary(cardother_data);
						File imageFile = new File(daveurl);
						//File imageFile = new File("aaaaaaaa.png");
						try{
						FileOutputStream outStream = new FileOutputStream(
								imageFile);
						outStream.write(datas);
						outStream.close();
						flago=true;
						}
						catch(Exception e)
						{
							
						}
					}
					
					

					if(flago==false)
					{
						if (cardother.getSize() > this.defaultCardFileSize) {
							responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
							responseString.setMessage("图像文件过大,请重新尝试!");
							return responseString.toString();
							//return generateResponseObject(
								//	ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
						}
						 originalName = cardother.getOriginalFilename();
	
						if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
							responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
							responseString.setMessage("上传图像文件格式不对,请重新尝试!");
							return responseString.toString();
							//return generateResponseObject(
							//		ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
						}
						index = originalName.lastIndexOf('.');
						index = Math.max(index, 0);
						
						fileotherName = this.save258CardDir + strseparator + timestr + "_"+orderId+ "_"+"fm"+"_"
								+ StringUtil.generateRandomString(5) + "_"
								+ StringUtil.generateRandomInteger(5)
								+ originalName.substring(index);
						try {
							File file1 = new File(request.getSession().getServletContext()
									.getRealPath("/")
									+ fileotherName);
							cardother.transferTo(file1);
						} catch (Exception e) {
							log.error("保存用户反面图像失败,请不要上传图像！", e);
							
							responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
							responseString.setMessage("保存用户反面图像失败，请去除上传图像后再尝试!");
							return responseString.toString();
							//return generateResponseObject(
							//		ResponseCode.CONSIGNEE_CARD_ERROR,
							//		"保存用户反面图像失败，请去除上传图像后再尝试!");
						}
					}
					
					
					//合成图片处理
					
					//合成处理
					String filecardtemp = "";
					if ((StringUtil.isEmpty(fileotherName)) || (StringUtil.isEmpty(fileName))) {
						//return generateResponseObject(
						//		ResponseCode.CONSIGNEE_CARD_ERROR,
						//		"正反面生成路径出错!");
						responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
						responseString.setMessage("正反面生成路径出错");
						return responseString.toString();
					}
					else{
						imgcompose img = new imgcompose();
						String str1 = request.getSession().getServletContext()
								.getRealPath("/")
								+ fileName;
						String str2 = request.getSession().getServletContext()
								.getRealPath("/")
								+ fileotherName;
						
						
						File file4 = new File(str1);
						File file5 = new File(str2);
						if((file4.exists())&&(file5.exists()))
						{
							String str3 = this.save258CardDir + strseparator + timestr + "_"+orderId+ "_"+"hc"+"_"
									+ StringUtil.generateRandomString(5) + "_"
									+ StringUtil.generateRandomInteger(5);
							filecardtemp = str3;
							str3 = request.getSession().getServletContext().getRealPath("/")
									+ str3;
							if (img.createcompics(str1, str2, str3)) {
								filecardtemp = filecardtemp + ".jpg";
							}
						}
						else
						{
							responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
							responseString.setMessage("生成合成图发生异常!");
							return responseString.toString();
							//return generateResponseObject(
							//		ResponseCode.CONSIGNEE_CARD_ERROR,
							//		"生成合成图发生异常!");
						}
						
						
						if(rev!=null)
						{
						
							try{
								File filetemp=null;
								if(!StringUtil.isEmpty(rev.getCardurl()))
								{
									filetemp = new File(request.getSession().getServletContext()
											.getRealPath("/")
											+ rev.getCardurl());
									if(filetemp!=null&&filetemp.exists())//原来正面图片存在，删除
									{
										if(rev.getCardurl().indexOf(orderId)>=0)
										{
											filetemp.delete();
										}
									}
								}
								
								if(!StringUtil.isEmpty(rev.getCardother()))
								{
									filetemp = new File(request.getSession().getServletContext()
											.getRealPath("/")
											+ rev.getCardother());
									if(filetemp!=null&&filetemp.exists())//原来的反面图片，删除
									{
										if(rev.getCardother().indexOf(orderId)>=0)
										{
											filetemp.delete();
										}
									}
								}
								
								
								if(!StringUtil.isEmpty(rev.getCardtogether()))
								{
									filetemp = new File(request.getSession().getServletContext()
											.getRealPath("/")
											+ rev.getCardtogether());
									if(filetemp!=null&&filetemp.exists())//原来的反面图片，删除
									{
										if(rev.getCardother().indexOf(orderId)>=0)
										{
											filetemp.delete();
										}
									}
								}
							}
							catch(Exception e)
							{
								//不影响原来操作
							}
						}
						
					}
					
					if(filecardtemp.equalsIgnoreCase(""))
					{
						responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
						responseString.setMessage("生成合成图失败!");
						return responseString.toString();
						//return generateResponseObject(
						//		ResponseCode.CONSIGNEE_CARD_ERROR,
						//		"生成合成图失败!");
					}
					
					rev.setCardid(cardId);
					rev.setCardurl(fileName);
					rev.setCardother(fileotherName);
					rev.setCardtogether(filecardtemp);
					
					rev.setUploadflag(Constant.UPLOAD_CARD_TYPE0);//表示用户前端页面上传
					
					String date1 = DateUtil.date2String(new Date());
					rev.setModifyDate(date1);
					rev.setCardid_flag(Constant.VERFY_CARDID_0);
					int k=this.receive_UserDao.modifyuploadcardinfo(rev);
					if(k<1)
					{
						responseString.setCode(ResponseCode.PARAMETER_ERROR);
						responseString.setMessage("上传失败！");
						return responseString.toString();
						//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"上传失败！"); 
					}
					//response.setContentType("text/html");
					//response.set
					responseString.setCode(ResponseCode.SUCCESS_CODE);
					responseString.setMessage("上传成功！");
					return responseString.toString();
					//return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE); 
					//return null;
				
				}catch(Exception e)
				{
					log.error("生成身份证图片失败", e);
					responseString.setCode(ResponseCode.SHOW_EXCEPTION);
					responseString.setMessage("生成图片出现异常！");
					return responseString.toString();
					//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "生成图片出现异常");
				}
			
		}
		
		//当进行压缩时，不会再传身份证图片上来，而是传转换后的数码信息
		@RequestMapping(value="/guest/upload_cardid_nopic", produces="text/plain;charset=UTF-8",method={RequestMethod.POST,RequestMethod.GET})
		@ResponseBody
		public String uploadcardidnopic(HttpServletRequest request,
				HttpServletResponse response,
				@RequestParam(value = "orderId", required = false) String orderId,//上传身份证的运单号
				@RequestParam(value = "username", required = false) String username,//上传身份证收件人对应的姓名
				@RequestParam(value = "cardId", required = false) String cardId,//上传的身份证号码
				//@RequestParam(value = "cardfront", required = false) String cardfront,//上传的身份证前端图
				//@RequestParam(value = "cardother", required = false) String cardother//上传的身份证反端图
				
				
				@RequestParam(value = "cardfront_data", required = false) String cardfront_data,//压缩后的正面图片
				@RequestParam(value = "cardother_data", required = false) String cardother_data//压缩后的反面图片
				
				//@RequestParam(value = "cardfront", required = false) MultipartFile cardfront,
				//@RequestParam(value = "cardother", required = false) MultipartFile cardother
				){
			//return "{\"code\":200}";
			
			
			
			
				ResponseString responseString=new ResponseString();
		
				
				//测试压缩的图片
				
				
				
				
				
				
				if(StringUtil.isEmpty(orderId))
				{
					responseString.setCode(ResponseCode.PARAMETER_ERROR);
					responseString.setMessage("运单号不能为空！");
					return responseString.toString();
					//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"运单号不能为空！"); 
				}
				orderId=orderId.trim();
				if(StringUtil.isEmpty(username))
				{
					responseString.setCode(ResponseCode.PARAMETER_ERROR);
					responseString.setMessage("身份证姓名不能为空！");
					return responseString.toString();
					//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"身份证姓名不能为空！"); 
				}
				username=username.trim();
				if(StringUtil.isEmpty(cardId))
				{
					responseString.setCode(ResponseCode.PARAMETER_ERROR);
					responseString.setMessage("身份证号码不能为空！");
					return responseString.toString();
					//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"身份证号码不能为空！"); 
				}
				else
				{
					cardId=cardId.trim();
					if (!ConsigneeInfoUtil.validateCardId(cardId)) {
						responseString.setCode(ResponseCode.CONSIGNEE_CARD_ID_ERROR);
						responseString.setMessage("身份证号码填写错误，请重新填写！");
						return responseString.toString();
						//return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
						//		"身份证号码填写错误，请重新填写！");
					}
				}
				
				if((StringUtil.isEmpty(cardfront_data)||cardfront_data.length()<23))
				{
					responseString.setCode(ResponseCode.PARAMETER_ERROR);
					responseString.setMessage("必须上传身份证正面图片！");
					return responseString.toString();
					//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"必须上传身份证正面图片！"); 
				}
				if((StringUtil.isEmpty(cardother_data)||cardother_data.length()<23))
				{
					responseString.setCode(ResponseCode.PARAMETER_ERROR);
					responseString.setMessage("必须上传身份证背面图片！");
					return responseString.toString();
					//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"必须上传身份证背面图片！"); 
				}
			
				
				//获取运单号是否存在并判断是否合法
				Receive_User rev=null;
				try
				{
				M_order order=this.m_orderDao.getRouteArgsbyOrderId(orderId.trim(), null);
				if(order==null)
				{
					responseString.setCode(ResponseCode.PARAMETER_ERROR);
					responseString.setMessage("上传的运单号不存在！");
					return responseString.toString();
					//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"上传的运单号不存在！");
				}
				else//检查运单号收件人信息
				{
					if(StringUtil.isEmpty(order.getRuserId()))//收件人信息为空
					{
						responseString.setCode(ResponseCode.PARAMETER_ERROR);
						responseString.setMessage("上传的运单信息有误！");
						return responseString.toString();
						//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"上传的运单信息有误！");
					}
					else
					{
						 rev=this.receive_UserDao.getById(Integer.parseInt(order.getRuserId()));
						if(rev==null)
						{
							responseString.setCode(ResponseCode.PARAMETER_ERROR);
							responseString.setMessage("上传的运单信息有误！");
							return responseString.toString();
							//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"上传的运单信息有误！");
						}
						else
						{
							if(!username.trim().equalsIgnoreCase(rev.getName().trim()))
							{
								responseString.setCode(ResponseCode.PARAMETER_ERROR);
								responseString.setMessage("上传的运单号姓名不匹配！");
								return responseString.toString();
								//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"上传的运单号姓名不匹配！");
							}
						}
					}
				}
				
				// 处理提交上来的图片
				// 解决火狐的反斜杠问题 kai 20151006
				String filetype = this.defaultCardFileType;// 要上传的文件类型
				String strtest = this.save258CardDir;
				String strseparator = "";
				if (strtest.indexOf("/") >= 0)// 包含有“/”字符，分隔符用此字符
				{
					strseparator = "/";
				} else {
					strseparator = File.separator;
				}
				//开始处理图片
				// 获取当时的时间缀
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyyMMddHHmmss");
				String timestr = sdf.format(date);
				//如果上传的图片中包含合成图片，那么即会忽略其它的正反两面图片
				
				String fileName = null;
				boolean flag=false;
				if(!(StringUtil.isEmpty(cardfront_data)||cardfront_data.length()<10))//没有压缩代码
				{
					int aa1=cardfront_data.indexOf(",");
					if(aa1>0)
					{
						cardfront_data=cardfront_data.substring(aa1+1, cardfront_data.length());
					}
					fileName = this.save258CardDir + strseparator + timestr + "_"+orderId+ "_"+"zm"+"_"
							+ StringUtil.generateRandomString(5) + "_"
							+ StringUtil.generateRandomInteger(5)
							+ ".jpeg";
					
					String daveurl=request.getSession().getServletContext()
							.getRealPath("/")+fileName;
					
					byte[] datas = DatatypeConverter
							.parseBase64Binary(cardfront_data);
					File imageFile = new File(daveurl);
					//File imageFile = new File("aaaaaaaa.png");
					try{
					FileOutputStream outStream = new FileOutputStream(
							imageFile);
					outStream.write(datas);
					outStream.close();
					flag=true;
					}
					catch(Exception e)
					{
						e.getStackTrace();
					}
				}
				
				String originalName="";
				int index=0;
				if(flag==false)//没有压缩代码
				{
					responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
					responseString.setMessage("保存用户正图像失败，请去除上传图像后再尝试!");
					return responseString.toString();
				}
				
					
					//处理反面图片
					String fileotherName = null;
					
					
					
					boolean flago=false;
					if(!(StringUtil.isEmpty(cardother_data)||cardother_data.length()<10))//没有压缩代码
					{
						int aa1=cardother_data.indexOf(",");
						if(aa1>0)
						{
							cardother_data=cardother_data.substring(aa1+1, cardother_data.length());
						}
						fileotherName = this.save258CardDir + strseparator + timestr + "_"+orderId+ "_"+"fm"+"_"
								+ StringUtil.generateRandomString(5) + "_"
								+ StringUtil.generateRandomInteger(5)
								+ ".jpeg";
						
						String daveurl=request.getSession().getServletContext()
								.getRealPath("/")+fileotherName;
						
						byte[] datas = DatatypeConverter
								.parseBase64Binary(cardother_data);
						File imageFile = new File(daveurl);
						//File imageFile = new File("aaaaaaaa.png");
						try{
						FileOutputStream outStream = new FileOutputStream(
								imageFile);
						outStream.write(datas);
						outStream.close();
						flago=true;
						}
						catch(Exception e)
						{
							
						}
					}
					
					
					if(flago==false)
					{
						responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
						responseString.setMessage("保存用户反面图像失败，请去除上传图像后再尝试!");
						return responseString.toString();
					}
					
					
					//合成图片处理
					
					//合成处理
					String filecardtemp = "";
					if ((StringUtil.isEmpty(fileotherName)) || (StringUtil.isEmpty(fileName))) {
						//return generateResponseObject(
						//		ResponseCode.CONSIGNEE_CARD_ERROR,
						//		"正反面生成路径出错!");
						responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
						responseString.setMessage("正反面生成路径出错");
						return responseString.toString();
					}
					else{
						imgcompose img = new imgcompose();
						String str1 = request.getSession().getServletContext()
								.getRealPath("/")
								+ fileName;
						String str2 = request.getSession().getServletContext()
								.getRealPath("/")
								+ fileotherName;
						
						
						File file4 = new File(str1);
						File file5 = new File(str2);
						if((file4.exists())&&(file5.exists()))
						{
							String str3 = this.save258CardDir + strseparator + timestr + "_"+orderId+ "_"+"hc"+"_"
									+ StringUtil.generateRandomString(5) + "_"
									+ StringUtil.generateRandomInteger(5);
							filecardtemp = str3;
							str3 = request.getSession().getServletContext().getRealPath("/")
									+ str3;
							if (img.createcompics(str1, str2, str3)) {
								filecardtemp = filecardtemp + ".jpg";
							}
						}
						else
						{
							responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
							responseString.setMessage("生成合成图发生异常!");
							return responseString.toString();
							//return generateResponseObject(
							//		ResponseCode.CONSIGNEE_CARD_ERROR,
							//		"生成合成图发生异常!");
						}
						
						
						if(rev!=null)
						{
						
							try{
								File filetemp=null;
								if(!StringUtil.isEmpty(rev.getCardurl()))
								{
									filetemp = new File(request.getSession().getServletContext()
											.getRealPath("/")
											+ rev.getCardurl());
									if(filetemp!=null&&filetemp.exists())//原来正面图片存在，删除
									{
										if(rev.getCardurl().indexOf(orderId)>=0)
										{
											filetemp.delete();
										}
									}
								}
								
								if(!StringUtil.isEmpty(rev.getCardother()))
								{
									filetemp = new File(request.getSession().getServletContext()
											.getRealPath("/")
											+ rev.getCardother());
									if(filetemp!=null&&filetemp.exists())//原来的反面图片，删除
									{
										if(rev.getCardother().indexOf(orderId)>=0)
										{
											filetemp.delete();
										}
									}
								}
								
								
								if(!StringUtil.isEmpty(rev.getCardtogether()))
								{
									filetemp = new File(request.getSession().getServletContext()
											.getRealPath("/")
											+ rev.getCardtogether());
									if(filetemp!=null&&filetemp.exists())//原来的反面图片，删除
									{
										if(rev.getCardother().indexOf(orderId)>=0)
										{
											filetemp.delete();
										}
									}
								}
							}
							catch(Exception e)
							{
								//不影响原来操作
							}
						}
						
					}
					
					if(filecardtemp.equalsIgnoreCase(""))
					{
						responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
						responseString.setMessage("生成合成图失败!");
						return responseString.toString();
						//return generateResponseObject(
						//		ResponseCode.CONSIGNEE_CARD_ERROR,
						//		"生成合成图失败!");
					}
					
					rev.setCardid(cardId);
					rev.setCardurl(fileName);
					rev.setCardother(fileotherName);
					rev.setCardtogether(filecardtemp);
					
					rev.setUploadflag(Constant.UPLOAD_CARD_TYPE0);//表示用户前端页面上传
					
					String date1 = DateUtil.date2String(new Date());
					rev.setModifyDate(date1);
					rev.setCardid_flag(Constant.VERFY_CARDID_0);
					int k=this.receive_UserDao.modifyuploadcardinfo(rev);
					if(k<1)
					{
						responseString.setCode(ResponseCode.PARAMETER_ERROR);
						responseString.setMessage("上传失败！");
						return responseString.toString();
						//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"上传失败！"); 
					}
					//response.setContentType("text/html");
					//response.set
					responseString.setCode(ResponseCode.SUCCESS_CODE);
					responseString.setMessage("上传成功！");
					return responseString.toString();
					//return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE); 
					//return null;
				
				}catch(Exception e)
				{
					log.error("生成身份证图片失败", e);
					responseString.setCode(ResponseCode.SHOW_EXCEPTION);
					responseString.setMessage("生成图片出现异常！");
					return responseString.toString();
					//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "生成图片出现异常");
				}
			
		}
		
		//用户端的特定用户搜索
		//搜索运单
		@RequestMapping(value = "/m_order/searchbyUser",method = { RequestMethod.GET, RequestMethod.POST})
		@ResponseBody
		public ResponseObject<PageSplit<M_order>> searchByKeyOfUser(
				HttpServletRequest request,
				@RequestParam(value = "oid", required = false) String oid,//运单号
				@RequestParam(value = "wid", required = false) String wid,//所属门店id
				@RequestParam(value = "cid", required = false, defaultValue = "") String cid,//渠道id
				@RequestParam(value = "info", required = false) String info,//查找的信息				
				@RequestParam(value = "state", required = false) String state,//状态
				@RequestParam(value = "type", required = false) String type,//类型				
				@RequestParam(value = "payornot", required = false) String payornot,//是否已付款
				
				@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
				@RequestParam(value = "rows", required = false, defaultValue = "10") int rows
				) {
			try {
				
				String userId = StringUtil.obj2String(request.getSession()
						.getAttribute(Constant.USER_ID_SESSION_KEY));
				if(StringUtil.isEmpty(userId))
				{
					return new ResponseObject<PageSplit<M_order>>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
				}
				
				if(StringUtil.isEmpty(payornot))
				{
					payornot=null;
				}
				else
				{
					if((!payornot.equalsIgnoreCase(Constant.ORDER_PAY_STATE0))&&(!payornot.equalsIgnoreCase(Constant.ORDER_PAY_STATE1)))
					{
						payornot=null;
					}
				}
				
				if(!StringUtil.isEmpty(wid)&&(wid.equalsIgnoreCase("-1")))
				{
					wid=null;
				}
				if(!StringUtil.isEmpty(cid)&&(cid.equalsIgnoreCase("-1")))
				{
					cid=null;
				}
				if(!StringUtil.isEmpty(state)&&(state.equalsIgnoreCase("-1")))
				{
					state=null;
				}
				if(!StringUtil.isEmpty(type)&&(type.equalsIgnoreCase("-1")))
				{
					type=null;
				}
				
			
				
				
		

				oid = StringUtil.isEmpty(oid) ? null : oid;

				//String column = OrderUtil.getSearchColumnByType(type);
				state = OrderUtil.dealState(state);
				pageIndex = Math.max(pageIndex, 1);
				return this.m_orderService.searchMordersbyUser(userId, oid, wid, cid, info, state, type, payornot, rows, pageIndex);
				//return this.m_orderService.searchMorders(oid, sod, god, flyno, storeid, cid, userinfo, commudityinfo, state, type,payway, sdate, edate, psdate, pedate,cardinfo,payornot, rows, pageIndex);
				//return new ResponseObject<PageSplit<Order>>();
				//return this.orderService.searchOrdersByKeys(oid, null, key, column,
				//		sdate, edate, state,typekey,storeid, defaultPageSize, pageIndex);
			} catch (Exception e) {
				log.error("查询运单失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "查询失败");
			}
		}
		
		
		
		
		
		@RequestMapping(value="/m_order/get_one_by_user", method={RequestMethod.POST,RequestMethod.GET})
		@ResponseBody
		public ResponseObject<Object> getonebyuser(HttpServletRequest request,
				@RequestParam(value = "id", required = false) String id){
			try{
				if(StringUtil.isEmpty(id))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数id不能为空!");
				}
				String userId = StringUtil.obj2String(request.getSession()
						.getAttribute(Constant.USER_ID_SESSION_KEY));
				if(StringUtil.isEmpty(userId))
				{
					return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
				}
				
				return this.m_orderService.getonebyuser(id, userId);
				
			}catch(Exception e){
				log.error("get morder fail", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
			}
		}
		
		
		@RequestMapping(value = "/m_order/user_pay", method = { RequestMethod.POST })
		@ResponseBody
		public ResponseObject<Object> payOrderMoney(HttpServletRequest request,
				@RequestParam(value = "id") String id) {
			try {
				String userId = StringUtil.obj2String(request.getSession()
						.getAttribute(Constant.USER_ID_SESSION_KEY));
				
				
				if(StringUtil.isEmpty(userId))
				{
					return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
				}
				
				double totalMoney = 0;
				double rmb = 0;
				double usd = 0;
				
				double usdrate=0;
				String rate=this.globalargsDao.getcontentbyflag("cur_usa_cn");//汇率
				
				try{
						double dou = Double.parseDouble(rate);
						if(dou>0)
						{
							usdrate=dou;
						}
				
				}catch(Exception e)
				{
					
				}
				M_order order=this.m_orderDao.getById(id, null);
				if(order==null)
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取运单信息失败!");
				}
				else
				{
					if(!userId.equalsIgnoreCase(order.getUserId()))
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取运单信息失败!");
					}
					if(order.getUser()==null)
					{
						User user=this.userDao.getUserById(userId);
						if(user!=null)
						{
							order.setUser(user);
						}
					}
					
					if(!Constant.ORDER_PAY_STATE0.equalsIgnoreCase(order.getPayornot()))
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"此运单不是未付款状态!!");
					}
					try{
						double dou = Double.parseDouble(order.getState());
						if(dou<2)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"此运单状态不能支付!");
						}
					}catch(Exception e)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取状态发发生异常!");
					}
				}
				
				totalMoney=StringUtil.string2Double(order.getTotalmoney());
				if(totalMoney<=0)
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取价钱失败!");
				}
				rmb=StringUtil.string2Double(order.getUser().getRmbBalance());
				usd=StringUtil.string2Double(order.getUser().getUsdBalance());
				//String cur_usa_cn=this
				
				//if (hasPayMoney(usd, rmb, totalMoney)) {
				if (hasPayMoneyusa(usd, rmb, usdrate,totalMoney)) {
					double newusd = usd - totalMoney;
					double newrmb = rmb; // 先用美元支付
					if (newusd >= 0) {
						// ignore
					} else {
						
						if(usdrate==0)
						{
							return generateResponseObject(
									ResponseCode.ORDER_PAY_ACCOUNT_NOT_MONEY, "美元帐户余额不足");
						}
						
						newusd = 0.0D; // 人民币余额全部支付，开始扣美元的钱
						newrmb = new BigDecimal((rmb - (totalMoney - usd)*usdrate)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
						if(newrmb<0)
						{
							return generateResponseObject(
									ResponseCode.ORDER_PAY_ACCOUNT_NOT_MONEY, "帐户余额不足");
						}
					}
					return this.m_orderService.userPayOne(id, order.getOrderId(), userId, String.valueOf(totalMoney), newrmb, newusd, true);
					
				
				} else {
					return generateResponseObject(
							ResponseCode.ORDER_PAY_ACCOUNT_NOT_MONEY, "帐户余额不足");
				}
			} catch (Exception e) {
				log.error("付款失败", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
						"付款失败，请重新尝试");
			}
		}

		
		@RequestMapping(value="/m_order/get_counts", method={RequestMethod.POST,RequestMethod.GET})
		@ResponseBody
		public ResponseObject<Object> getmordercounts(HttpServletRequest request){
			try{
			
				String userId = StringUtil.obj2String(request.getSession()
						.getAttribute(Constant.USER_ID_SESSION_KEY));
				if(StringUtil.isEmpty(userId))
				{
					return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
				}
				CountMorder morder=new CountMorder();
				morder.setAbandonQ(this.m_orderDao.getabandonQ(userId));
				morder.setNopayQ(this.m_orderDao.getnopayQ(userId));
				morder.setOnlineQ(this.m_orderDao.getonlineQ(userId));
				morder.setQuestionQ(this.m_orderDao.getquestionQ(userId));
				morder.setTotalQ(this.m_orderDao.gettotalQ(userId));
				morder.setTranwaitpQ(this.m_orderDao.gettranwaitpQ(userId));
				ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
				obj.setData(morder);
				return obj;
				
			}catch(Exception e){
				log.error("get morder fail", e);
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
			}
		}
		
		
		
		//在线置单添加
		@RequestMapping(value="/m_order/online_add", method={RequestMethod.POST,RequestMethod.GET})
		@ResponseBody
		public ResponseObject<Object> submitonlineorder(HttpServletRequest request,M_order order,
				@RequestParam(value = "savesenderflag", required = false) String sflag,
				@RequestParam(value = "savereverflag", required = false) String rflag,
				@RequestParam(value = "reflag", required = false) String reflag,//为1时，表示保存了
				
				@RequestParam(value = "cardurlfile", required = false) MultipartFile file,
				@RequestParam(value = "cardurlotherfile", required = false) MultipartFile fileother,
				@RequestParam(value = "cardurltogetherfile", required = false) MultipartFile filetogether){
			try{
				String userId = StringUtil.obj2String(request.getSession()
						.getAttribute(Constant.USER_ID_SESSION_KEY));
				
				
				if(StringUtil.isEmpty(userId))
				{
					return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
				}
				
				if(order==null)
				{
					return generateResponseObject(ResponseCode.PARAMETER_ERROR,
							"参数出错！");
				}
				
				//身份证号不为空，要检查合法性
				if(!StringUtil.isEmpty(order.getRuser().getCardid()))
				{
					if (!ConsigneeInfoUtil.validateCardId(order.getRuser().getCardid())) {
						return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
								"身份证号码填写错误，请重新填写！");
					}
				}
				//只要有一个上传不为空，身份证号就不为空
				if ((file != null && file.getSize() > 0)||(fileother != null && fileother.getSize() > 0)||(filetogether != null && filetogether.getSize() > 0))
				{
					if(StringUtil.isEmpty(order.getRuser().getCardid()))
					{
						return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
								"上传图片时，身份证号码不能为空，请重新填写！");
					}
					
					//上传正反两面图片时，必须保证两面图片都正确并存在
					if((file != null && file.getSize()>0)&&(fileother==null))
					{
						if(!StringUtil.isEmpty(order.getRuser().getCardother()))
						{
							File file3 = new File(request.getSession().getServletContext()
									.getRealPath("/")
									+ order.getRuser().getCardother());
							if(!file3.exists())
							{
								return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
										"正反两面照片必须同时存在！");
							}
						}
						else
						{
							return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
									"正反两面照片必须同时存在！");
						}
					}
					if((fileother != null && fileother.getSize()>0)&&(file==null))
					{
						if(!StringUtil.isEmpty(order.getRuser().getCardurl()))
						{
							File file3 = new File(request.getSession().getServletContext()
									.getRealPath("/")
									+ order.getRuser().getCardurl());
							if(!file3.exists())
							{
								return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
										"正反两面照片必须同时存在！");
							}
						}
						else
						{
							return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
									"正反两面照片必须同时存在！");
						}
					}
					
					
			
				}
				// 处理提交上来的图片
				// 解决火狐的反斜杠问题 kai 20151006
				String filetype = this.defaultCardFileType;// 要上传的文件类型
				String strtest = this.save258CardDir;
				String strseparator = "";
				if (strtest.indexOf("/") >= 0)// 包含有“/”字符，分隔符用此字符
				{
					strseparator = "/";
				} else {
					strseparator = File.separator;
				}
				//开始处理图片
				// 获取当时的时间缀
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyyMMddHHmmss");
				String timestr = sdf.format(date);
				//如果上传的图片中包含合成图片，那么即会忽略其它的正反两面图片
				if (filetogether != null && filetogether.getSize() > 0)
				{
					String fileName = null;
				
					if (filetogether.getSize() > this.defaultCardFileSize) {
						return generateResponseObject(
								ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
					}

					String originalName = filetogether.getOriginalFilename();

					if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
						return generateResponseObject(
								ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
					}
					int index = originalName.lastIndexOf('.');
					index = Math.max(index, 0);
					
					fileName = this.save258CardDir + strseparator + timestr + "_"+userId+ "_"+"hc"+"_"
							+ StringUtil.generateRandomString(5) + "_"
							+ StringUtil.generateRandomInteger(5)
							+ originalName.substring(index);
					try {
						File file1 = new File(request.getSession().getServletContext()
								.getRealPath("/")
								+ fileName);
						filetogether.transferTo(file1);
					} catch (Exception e) {
						log.error("保存用户合成图像失败,请不要上传图像！", e);
						return generateResponseObject(
								ResponseCode.CONSIGNEE_CARD_ERROR,
								"保存用户合成图像失败，请去除上传图像后再尝试!");
					}
					
					order.getRuser().setCardtogether(fileName);
				}
				else
				{
					//正反图片有上传的图片
					if ((file != null && file.getSize() > 0)||(fileother != null && fileother.getSize() > 0))
					{
						//正面图片处理
						if(file != null && file.getSize() > 0)
						{
							String fileName = null;
							if (file.getSize() > this.defaultCardFileSize) {
								return generateResponseObject(
										ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
							}
			
							String originalName = file.getOriginalFilename();
			
							if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
								return generateResponseObject(
										ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
							}
							int index = originalName.lastIndexOf('.');
							index = Math.max(index, 0);
							
							fileName = this.save258CardDir + strseparator + timestr + "_"+userId+ "_"+"zm"+"_"
									+ StringUtil.generateRandomString(5) + "_"
									+ StringUtil.generateRandomInteger(5)
									+ originalName.substring(index);
							try {
								File file1 = new File(request.getSession().getServletContext()
										.getRealPath("/")
										+ fileName);
								file.transferTo(file1);
							} catch (Exception e) {
								log.error("保存用户正面图像失败,请不要上传图像！", e);
								return generateResponseObject(
										ResponseCode.CONSIGNEE_CARD_ERROR,
										"保存用户正面图像失败，请去除上传图像后再尝试!");
							}
							
							order.getRuser().setCardurl(fileName);
						}
						
						//反面图片处理
						if(fileother != null && fileother.getSize() > 0)
						{
							String fileName = null;
							if (fileother.getSize() > this.defaultCardFileSize) {
								return generateResponseObject(
										ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
							}
			
							String originalName = fileother.getOriginalFilename();
			
							if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
								return generateResponseObject(
										ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
							}
							int index = originalName.lastIndexOf('.');
							index = Math.max(index, 0);
							
							fileName = this.save258CardDir + strseparator + timestr + "_"+userId+ "_"+"fm"+"_"
									+ StringUtil.generateRandomString(5) + "_"
									+ StringUtil.generateRandomInteger(5)
									+ originalName.substring(index);
							try {
								File file1 = new File(request.getSession().getServletContext()
										.getRealPath("/")
										+ fileName);
								fileother.transferTo(file1);
							} catch (Exception e) {
								log.error("保存用户反面图像失败,请不要上传图像！", e);
								return generateResponseObject(
										ResponseCode.CONSIGNEE_CARD_ERROR,
										"保存用户反面图像失败，请去除上传图像后再尝试!");
							}
							
							order.getRuser().setCardother(fileName);
						}
						
						
						//合成处理
						String filecardtemp = "";
						if ((!StringUtil.isEmpty(order.getRuser().getCardurl())) && (!StringUtil.isEmpty(order.getRuser().getCardother()))) {
							imgcompose img = new imgcompose();
							String str1 = request.getSession().getServletContext()
									.getRealPath("/")
									+ order.getRuser().getCardurl();
							String str2 = request.getSession().getServletContext()
									.getRealPath("/")
									+ order.getRuser().getCardother();
							
							
							File file4 = new File(str1);
							File file5 = new File(str2);
							if((file4.exists())&&(file5.exists()))
							{
								String str3 = this.save258CardDir + strseparator + timestr + "_"+userId+ "_"+"hc"+"_"
										+ StringUtil.generateRandomString(5) + "_"
										+ StringUtil.generateRandomInteger(5);
								filecardtemp = str3;
								str3 = request.getSession().getServletContext().getRealPath("/")
										+ str3;
								if (img.createcompics(str1, str2, str3)) {
									filecardtemp = filecardtemp + ".jpg";
								}
							}
							
						}
						
						if(!StringUtil.isEmpty(filecardtemp))
						{
							order.getRuser().setCardtogether(filecardtemp);
						}
						
					}
					
					
					
				}
				
				
		
				
				//pageIndex = Math.max(pageIndex, 1);
				if (!ConsigneeInfoUtil.validatePhone(order.getRuser().getPhone())) {
				      return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "收件人手机号码填写错误，请检查！");
				}
				if (!ConsigneeInfoUtil.validatePhone(order.getSuser().getPhone())) {
				      return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "发件人手机号码填写错误，请检查！");
				}
				//String empId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY));
				//String empName = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ACCOUNT_SESSION_KEY));
				//String empStoreName = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_NAME_SESSION_KEY));
				//String storeId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
				order.setEmployeeId("-1");//不能留，暂时用-1代替
				//order.setStoreId(storeId);
				order.setType(Constant.ORDER_TYPE_KEY_3);//设置为网上置单
				
				
				order.setState(Constant.ORDER_STATE__10);//设置为待付款状态
				order.getRuser().setUseState("1");
				order.getSuser().setUseState("1");
				order.setTotalmoney("0");
				order.setUserId(userId);
		
				
				
				
				return this.m_orderService.add_online_Morder(order,rflag,sflag,reflag);
				
			}catch(Exception e){
				
				return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
			}
		}
		
		
		//查找用户的在线线置单
				//搜索运单
				@RequestMapping(value = "/m_order/search_online_byUser",method = { RequestMethod.GET, RequestMethod.POST})
				@ResponseBody
				public ResponseObject<PageSplit<M_order>> searchonlineByKeyOfUser(
						HttpServletRequest request,
						@RequestParam(value = "keyword", required = false) String info,//查找的信息								
						@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
						@RequestParam(value = "rows", required = false, defaultValue = "10") int rows
						) {
					try {
						
						String userId = StringUtil.obj2String(request.getSession()
								.getAttribute(Constant.USER_ID_SESSION_KEY));
						if(StringUtil.isEmpty(userId))
						{
							return new ResponseObject<PageSplit<M_order>>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
						}
										
						pageIndex = Math.max(pageIndex, 1);
						return this.m_orderService.searchMordersbyUser(userId, null, null, null, info, Constant.ORDER_STATE__10, null, null, rows, pageIndex);
			
					} catch (Exception e) {
						log.error("查询运单失败", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "查询失败");
					}
				}
				
				
				//删除运单
				@RequestMapping(value = "/m_order/user_delete", method = { RequestMethod.POST,RequestMethod.GET })
				@ResponseBody
				public ResponseObject<Object> deleteoneonlineorder(
						HttpServletRequest request,
						@RequestParam(value = "id") String id) {
					try {
						
						String userId = StringUtil.obj2String(request.getSession()
								.getAttribute(Constant.USER_ID_SESSION_KEY));
						
						
						if(StringUtil.isEmpty(userId))
						{
							return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
						}
						
						if(StringUtil.isEmpty(id))
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错！");
						}
						
						 String[] ids = new String[1];
						 ids[0]=id;
						 
						List<String> states=Arrays.asList(new String[] { Constant.ORDER_STATE0,
								Constant.ORDER_STATE1, Constant.ORDER_STATE2,
								Constant.ORDER_STATE10,Constant.ORDER_STATE__10,Constant.ORDER_STATE__9,"",null,"null" });
						int k=this.m_orderDao.deleteByIdsandUser(Arrays.asList(ids), states, userId);
						if(k<1)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"删除失败");
						}
						return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
						//return this.m_orderService.deleteMorderByIds(Arrays.asList(ids), states, storeid);
						//return this.m_orderService.deleteMorderByIds(ids, states, storeid);
						
					} catch (Exception e) {
						log.error("删除运单失败", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "删除运单失败");
					}
				}
				
				
				
				//在线置单添加
				@RequestMapping(value="/m_order/online_modify", method={RequestMethod.POST,RequestMethod.GET})
				@ResponseBody
				public ResponseObject<Object> modifyonlineorder(HttpServletRequest request,M_order order,
						@RequestParam(value = "savesenderflag", required = false) String sflag,
						@RequestParam(value = "savereverflag", required = false) String rflag,
						@RequestParam(value = "reflag", required = false) String reflag,//为1时，表示保存了
						@RequestParam(value = "cardurlfile", required = false) MultipartFile file,
						@RequestParam(value = "cardurlotherfile", required = false) MultipartFile fileother,
						@RequestParam(value = "cardurltogetherfile", required = false) MultipartFile filetogether){
					try{
						String userId = StringUtil.obj2String(request.getSession()
								.getAttribute(Constant.USER_ID_SESSION_KEY));
						
						
						if(StringUtil.isEmpty(userId))
						{
							return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
						}
						
						if(order==null)
						{
							return generateResponseObject(ResponseCode.PARAMETER_ERROR,
									"参数出错！");
						}
						if(StringUtil.isEmpty(order.getId()))
						{
							return generateResponseObject(ResponseCode.PARAMETER_ERROR,
									"参数出错！");
						}
						
						
						//身份证号不为空，要检查合法性
						if(!StringUtil.isEmpty(order.getRuser().getCardid()))
						{
							if (!ConsigneeInfoUtil.validateCardId(order.getRuser().getCardid())) {
								return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
										"身份证号码填写错误，请重新填写！");
							}
						}
						//只要有一个上传不为空，身份证号就不为空
						if ((file != null && file.getSize() > 0)||(fileother != null && fileother.getSize() > 0)||(filetogether != null && filetogether.getSize() > 0))
						{
							if(StringUtil.isEmpty(order.getRuser().getCardid()))
							{
								return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
										"上传图片时，身份证号码不能为空，请重新填写！");
							}
							
							//上传正反两面图片时，必须保证两面图片都正确并存在
							if((file != null && file.getSize()>0)&&(fileother==null))
							{
								if(!StringUtil.isEmpty(order.getRuser().getCardother()))
								{
									File file3 = new File(request.getSession().getServletContext()
											.getRealPath("/")
											+ order.getRuser().getCardother());
									if(!file3.exists())
									{
										return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
												"正反两面照片必须同时存在！");
									}
								}
								else
								{
									return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
											"正反两面照片必须同时存在！");
								}
							}
							if((fileother != null && fileother.getSize()>0)&&(file==null))
							{
								if(!StringUtil.isEmpty(order.getRuser().getCardurl()))
								{
									File file3 = new File(request.getSession().getServletContext()
											.getRealPath("/")
											+ order.getRuser().getCardurl());
									if(!file3.exists())
									{
										return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
												"正反两面照片必须同时存在！");
									}
								}
								else
								{
									return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
											"正反两面照片必须同时存在！");
								}
							}
							
							
					
						}
						// 处理提交上来的图片
						// 解决火狐的反斜杠问题 kai 20151006
						String filetype = this.defaultCardFileType;// 要上传的文件类型
						String strtest = this.save258CardDir;
						String strseparator = "";
						if (strtest.indexOf("/") >= 0)// 包含有“/”字符，分隔符用此字符
						{
							strseparator = "/";
						} else {
							strseparator = File.separator;
						}
						//开始处理图片
						// 获取当时的时间缀
						Date date = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyyMMddHHmmss");
						String timestr = sdf.format(date);
						//如果上传的图片中包含合成图片，那么即会忽略其它的正反两面图片
						if (filetogether != null && filetogether.getSize() > 0)
						{
							String fileName = null;
						
							if (filetogether.getSize() > this.defaultCardFileSize) {
								return generateResponseObject(
										ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
							}

							String originalName = filetogether.getOriginalFilename();

							if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
								return generateResponseObject(
										ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
							}
							int index = originalName.lastIndexOf('.');
							index = Math.max(index, 0);
							
							fileName = this.save258CardDir + strseparator + timestr + "_"+userId+ "_"+"hc"+"_"
									+ StringUtil.generateRandomString(5) + "_"
									+ StringUtil.generateRandomInteger(5)
									+ originalName.substring(index);
							try {
								File file1 = new File(request.getSession().getServletContext()
										.getRealPath("/")
										+ fileName);
								filetogether.transferTo(file1);
							} catch (Exception e) {
								log.error("保存用户合成图像失败,请不要上传图像！", e);
								return generateResponseObject(
										ResponseCode.CONSIGNEE_CARD_ERROR,
										"保存用户合成图像失败，请去除上传图像后再尝试!");
							}
							
							order.getRuser().setCardtogether(fileName);
						}
						else
						{
							//正反图片有上传的图片
							if ((file != null && file.getSize() > 0)||(fileother != null && fileother.getSize() > 0))
							{
								//正面图片处理
								if(file != null && file.getSize() > 0)
								{
									String fileName = null;
									if (file.getSize() > this.defaultCardFileSize) {
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
									}
					
									String originalName = file.getOriginalFilename();
					
									if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
									}
									int index = originalName.lastIndexOf('.');
									index = Math.max(index, 0);
									
									fileName = this.save258CardDir + strseparator + timestr + "_"+userId+ "_"+"zm"+"_"
											+ StringUtil.generateRandomString(5) + "_"
											+ StringUtil.generateRandomInteger(5)
											+ originalName.substring(index);
									try {
										File file1 = new File(request.getSession().getServletContext()
												.getRealPath("/")
												+ fileName);
										file.transferTo(file1);
									} catch (Exception e) {
										log.error("保存用户正面图像失败,请不要上传图像！", e);
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR,
												"保存用户正面图像失败，请去除上传图像后再尝试!");
									}
									
									order.getRuser().setCardurl(fileName);
								}
								
								//反面图片处理
								if(fileother != null && fileother.getSize() > 0)
								{
									String fileName = null;
									if (fileother.getSize() > this.defaultCardFileSize) {
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
									}
					
									String originalName = fileother.getOriginalFilename();
					
									if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
									}
									int index = originalName.lastIndexOf('.');
									index = Math.max(index, 0);
									
									fileName = this.save258CardDir + strseparator + timestr + "_"+userId+ "_"+"fm"+"_"
											+ StringUtil.generateRandomString(5) + "_"
											+ StringUtil.generateRandomInteger(5)
											+ originalName.substring(index);
									try {
										File file1 = new File(request.getSession().getServletContext()
												.getRealPath("/")
												+ fileName);
										fileother.transferTo(file1);
									} catch (Exception e) {
										log.error("保存用户反面图像失败,请不要上传图像！", e);
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR,
												"保存用户反面图像失败，请去除上传图像后再尝试!");
									}
									
									order.getRuser().setCardother(fileName);
								}
								
								
								//合成处理
								String filecardtemp = "";
								if ((!StringUtil.isEmpty(order.getRuser().getCardurl())) && (!StringUtil.isEmpty(order.getRuser().getCardother()))) {
									imgcompose img = new imgcompose();
									String str1 = request.getSession().getServletContext()
											.getRealPath("/")
											+ order.getRuser().getCardurl();
									String str2 = request.getSession().getServletContext()
											.getRealPath("/")
											+ order.getRuser().getCardother();
									
									
									File file4 = new File(str1);
									File file5 = new File(str2);
									if((file4.exists())&&(file5.exists()))
									{
										String str3 = this.save258CardDir + strseparator + timestr + "_"+userId+ "_"+"hc"+"_"
												+ StringUtil.generateRandomString(5) + "_"
												+ StringUtil.generateRandomInteger(5);
										filecardtemp = str3;
										str3 = request.getSession().getServletContext().getRealPath("/")
												+ str3;
										if (img.createcompics(str1, str2, str3)) {
											filecardtemp = filecardtemp + ".jpg";
										}
									}
									
								}
								
								if(!StringUtil.isEmpty(filecardtemp))
								{
									order.getRuser().setCardtogether(filecardtemp);
								}
								
							}
							
							
							
						}
						
						
				
						
						//pageIndex = Math.max(pageIndex, 1);
						if (!ConsigneeInfoUtil.validatePhone(order.getRuser().getPhone())) {
						      return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "收件人手机号码填写错误，请检查！");
						}
						if (!ConsigneeInfoUtil.validatePhone(order.getSuser().getPhone())) {
						      return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "发件人手机号码填写错误，请检查！");
						}
						//String empId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY));
						//String empName = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ACCOUNT_SESSION_KEY));
						//String empStoreName = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_NAME_SESSION_KEY));
						//String storeId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
						order.setEmployeeId("-1");//不能留，暂时用-1代替
						//order.setStoreId(storeId);
						order.setType(Constant.ORDER_TYPE_KEY_3);//设置为网上置单
						
						
						order.setState(Constant.ORDER_STATE__10);//设置为待付款状态
						order.getRuser().setUseState("1");
						order.getSuser().setUseState("1");
						order.setTotalmoney("0");
						order.setUserId(userId);
						M_order order1=this.m_orderDao.getById(order.getId(), null);
						if(order1==null)
						{
							return generateResponseObject(ResponseCode.PARAMETER_ERROR,
									"查找不到运单源！");
						}
						else
						{
							if(!userId.equalsIgnoreCase(order1.getUserId()))
							{
								return generateResponseObject(ResponseCode.PARAMETER_ERROR,
										"修改越权！");
							}
							if(!Constant.ORDER_STATE__10.equalsIgnoreCase(order1.getState()))
							{
								return generateResponseObject(ResponseCode.PARAMETER_ERROR,
										"状态已不是在线置单！");
							}
							order.getRuser().setId(order1.getRuser().getId());
							order.getSuser().setId(order1.getSuser().getId());
							order.setOrderId(order1.getOrderId());
							order.setType(order1.getType());
						}
						
						
						
						//return this.m_orderService.add_online_Morder(order,rflag,sflag);
						return this.m_orderService.modify_online_Morder(order, rflag, sflag,reflag);
					}catch(Exception e){
						
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
					}
				}
				
			//后台获取一条在线置单数据
				@RequestMapping(value="/admin/m_order/get_one_online", method={RequestMethod.POST,RequestMethod.GET})
				@ResponseBody
				public ResponseObject<Object> getoneonlinebyadmin(HttpServletRequest request,
						@RequestParam(value = "orderId", required = false) String orderId){
					try{
						if(StringUtil.isEmpty(orderId))
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数运单号不能为空!");
						}
						
						String storeid=null;
						String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
						if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
						{
							storeid=null;//表示可以查找所有门店
							
						}else
						{
							storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
							if((storeid==null)||(storeid.equalsIgnoreCase("")))
							{
								return generateResponseObject(ResponseCode.NEED_LOGIN,
										"你没有登陆!");
							}
						}
						
						M_order order=null;
						orderId="%"+orderId+"%";
						List<M_order> orders=this.m_orderDao.getByOrderIdflag(orderId, null);
						if(orders==null||orders.size()<1)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"查找运单号失败!");
						}
						else if(orders.size()>1)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"查找运单超过两个，请填写更完整的运单号!");
						}
						else
						{
							order=orders.get(0);
						}
						//M_order order=this.m_orderDao.getByOrderId(orderId,null);//在线置单是可以查找所有门店的
						
						
						
						
						if(order==null)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"查找运单号失败!");
						}
						else
						{
							if(!Constant.ORDER_STATE__10.equalsIgnoreCase(order.getState()))//检查是否是在线置单
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"查找的单号不是在线置单!");
							}
							storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
							if(!storeid.equalsIgnoreCase(order.getStoreId()))
							{
								ResponseObject<Object> obj1= new ResponseObject<Object>(ResponseCode.M_ORDER_OLINR_ERROR,"查找的单号不是本门店的单号!");
								obj1.setData(order);
								return obj1;
							}
						}
						ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
						obj.setData(order);
						return obj;
						
					}catch(Exception e){
						log.error("get morder fail", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
					}
				}
				//门店修改在线置单或上门收货运单
				@RequestMapping(value="/admin/m_order/ms_modify_online", method={RequestMethod.POST,RequestMethod.GET})
				@ResponseBody
				public ResponseObject<Object> modifyonlieorder(HttpServletRequest request,M_order order,
						@RequestParam(value = "cardurlfile", required = false) MultipartFile file,
						@RequestParam(value = "cardurlotherfile", required = false) MultipartFile fileother,
						@RequestParam(value = "cardurltogetherfile", required = false) MultipartFile filetogether){
					try{
						String storeId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
						if(StringUtil.isEmpty(storeId)){
							return generateResponseObject(ResponseCode.NEED_LOGIN,
									"请登陆！");
						}
						
						if(order==null)
						{
							return generateResponseObject(ResponseCode.PARAMETER_ERROR,
									"参数出错！");
						}
						
						//身份证号不为空，要检查合法性
						if(!StringUtil.isEmpty(order.getRuser().getCardid()))
						{
							if (!ConsigneeInfoUtil.validateCardId(order.getRuser().getCardid())) {
								return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
										"身份证号码填写错误，请重新填写！");
							}
						}
						//只要有一个上传不为空，身份证号就不为空
						if ((file != null && file.getSize() > 0)||(fileother != null && fileother.getSize() > 0)||(filetogether != null && filetogether.getSize() > 0))
						{
							if(StringUtil.isEmpty(order.getRuser().getCardid()))
							{
								return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
										"上传图片时，身份证号码不能为空，请重新填写！");
							}
							
							//上传正反两面图片时，必须保证两面图片都正确并存在
							if((file != null && file.getSize()>0)&&(fileother==null))
							{
								if(!StringUtil.isEmpty(order.getRuser().getCardother()))
								{
									File file3 = new File(request.getSession().getServletContext()
											.getRealPath("/")
											+ order.getRuser().getCardother());
									if(!file3.exists())
									{
										return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
												"正反两面照片必须同时存在！");
									}
								}
								else
								{
									return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
											"正反两面照片必须同时存在！");
								}
							}
							if((fileother != null && fileother.getSize()>0)&&(file==null))
							{
								if(!StringUtil.isEmpty(order.getRuser().getCardurl()))
								{
									File file3 = new File(request.getSession().getServletContext()
											.getRealPath("/")
											+ order.getRuser().getCardurl());
									if(!file3.exists())
									{
										return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
												"正反两面照片必须同时存在！");
									}
								}
								else
								{
									return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
											"正反两面照片必须同时存在！");
								}
							}
							
							
					
						}
						// 处理提交上来的图片
						// 解决火狐的反斜杠问题 kai 20151006
						String filetype = this.defaultCardFileType;// 要上传的文件类型
						String strtest = this.save258CardDir;
						String strseparator = "";
						if (strtest.indexOf("/") >= 0)// 包含有“/”字符，分隔符用此字符
						{
							strseparator = "/";
						} else {
							strseparator = File.separator;
						}
						//开始处理图片
						// 获取当时的时间缀
						Date date = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyyMMddHHmmss");
						String timestr = sdf.format(date);
						//如果上传的图片中包含合成图片，那么即会忽略其它的正反两面图片
						if (filetogether != null && filetogether.getSize() > 0)
						{
							String fileName = null;
						
							if (filetogether.getSize() > this.defaultCardFileSize) {
								return generateResponseObject(
										ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
							}

							String originalName = filetogether.getOriginalFilename();

							if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
								return generateResponseObject(
										ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
							}
							int index = originalName.lastIndexOf('.');
							index = Math.max(index, 0);
							
							fileName = this.save258CardDir + strseparator + timestr + "_"+storeId+ "_"+"hc"+"_"
									+ StringUtil.generateRandomString(5) + "_"
									+ StringUtil.generateRandomInteger(5)
									+ originalName.substring(index);
							try {
								File file1 = new File(request.getSession().getServletContext()
										.getRealPath("/")
										+ fileName);
								filetogether.transferTo(file1);
							} catch (Exception e) {
								log.error("保存用户合成图像失败,请不要上传图像！", e);
								return generateResponseObject(
										ResponseCode.CONSIGNEE_CARD_ERROR,
										"保存用户合成图像失败，请去除上传图像后再尝试!");
							}
							
							order.getRuser().setCardtogether(fileName);
						}
						else
						{
							//正反图片有上传的图片
							if ((file != null && file.getSize() > 0)||(fileother != null && fileother.getSize() > 0))
							{
								//正面图片处理
								if(file != null && file.getSize() > 0)
								{
									String fileName = null;
									if (file.getSize() > this.defaultCardFileSize) {
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
									}
					
									String originalName = file.getOriginalFilename();
					
									if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
									}
									int index = originalName.lastIndexOf('.');
									index = Math.max(index, 0);
									
									fileName = this.save258CardDir + strseparator + timestr + "_"+storeId+ "_"+"zm"+"_"
											+ StringUtil.generateRandomString(5) + "_"
											+ StringUtil.generateRandomInteger(5)
											+ originalName.substring(index);
									try {
										File file1 = new File(request.getSession().getServletContext()
												.getRealPath("/")
												+ fileName);
										file.transferTo(file1);
									} catch (Exception e) {
										log.error("保存用户正面图像失败,请不要上传图像！", e);
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR,
												"保存用户正面图像失败，请去除上传图像后再尝试!");
									}
									
									order.getRuser().setCardurl(fileName);
								}
								
								//反面图片处理
								if(fileother != null && fileother.getSize() > 0)
								{
									String fileName = null;
									if (fileother.getSize() > this.defaultCardFileSize) {
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
									}
					
									String originalName = fileother.getOriginalFilename();
					
									if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
									}
									int index = originalName.lastIndexOf('.');
									index = Math.max(index, 0);
									
									fileName = this.save258CardDir + strseparator + timestr + "_"+storeId+ "_"+"fm"+"_"
											+ StringUtil.generateRandomString(5) + "_"
											+ StringUtil.generateRandomInteger(5)
											+ originalName.substring(index);
									try {
										File file1 = new File(request.getSession().getServletContext()
												.getRealPath("/")
												+ fileName);
										fileother.transferTo(file1);
									} catch (Exception e) {
										log.error("保存用户反面图像失败,请不要上传图像！", e);
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR,
												"保存用户反面图像失败，请去除上传图像后再尝试!");
									}
									
									order.getRuser().setCardother(fileName);
								}
								
								
								//合成处理
								String filecardtemp = "";
								if ((!StringUtil.isEmpty(order.getRuser().getCardurl())) && (!StringUtil.isEmpty(order.getRuser().getCardother()))) {
									imgcompose img = new imgcompose();
									String str1 = request.getSession().getServletContext()
											.getRealPath("/")
											+ order.getRuser().getCardurl();
									String str2 = request.getSession().getServletContext()
											.getRealPath("/")
											+ order.getRuser().getCardother();
									
									
									File file4 = new File(str1);
									File file5 = new File(str2);
									if((file4.exists())&&(file5.exists()))
									{
										String str3 = this.save258CardDir + strseparator + timestr + "_"+storeId+ "_"+"hc"+"_"
												+ StringUtil.generateRandomString(5) + "_"
												+ StringUtil.generateRandomInteger(5);
										filecardtemp = str3;
										str3 = request.getSession().getServletContext().getRealPath("/")
												+ str3;
										if (img.createcompics(str1, str2, str3)) {
											filecardtemp = filecardtemp + ".jpg";
										}
									}
									
								}
								
								if(!StringUtil.isEmpty(filecardtemp))
								{
									order.getRuser().setCardtogether(filecardtemp);
								}
								
							}
							
							
							
						}
						
						
				
						
						//pageIndex = Math.max(pageIndex, 1);
						if (!ConsigneeInfoUtil.validatePhone(order.getRuser().getPhone())) {
						      return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "收件人手机号码填写错误，请检查！");
						}
						if (!ConsigneeInfoUtil.validatePhone(order.getSuser().getPhone())) {
						      return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "发件人手机号码填写错误，请检查！");
						}
						String empId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY));
						//String empName = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ACCOUNT_SESSION_KEY));
						//String empStoreName = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_NAME_SESSION_KEY));
						//String storeId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
						order.setEmployeeId(empId);
						order.setStoreId(storeId);
						//order.setType(Constant.ORDER_TYPE_KEY_1);//设置为门市运单
						
						if(!StringUtil.isEmpty(order.getType())&&(order.getType().equalsIgnoreCase("-10")))//表示提交网上置单修改
						{
							order.setType(Constant.ORDER_TYPE_KEY_3);//设置为网上置单
							if(!StringUtil.isEmpty(order.getOrderId()))
							{
								if(StringUtil.isEmpty(order.getUserId()))
								{
									return generateResponseObject(ResponseCode.PARAMETER_ERROR, "没有包含会员信息!");
								}
								M_order order2=this.m_orderDao.getByOrderId(order.getOrderId(), null);//
								if(order2==null)
								{
									return generateResponseObject(ResponseCode.PARAMETER_ERROR, "查找不到对应的运单！");
								}
								else
								{
									if(!Constant.ORDER_STATE__10.equalsIgnoreCase(order2.getState()))
									{
										return generateResponseObject(ResponseCode.PARAMETER_ERROR, "此运单不是在线运单！");
									}
									if(!order.getUserId().equalsIgnoreCase(order2.getUserId()))
									{
										return generateResponseObject(ResponseCode.PARAMETER_ERROR, "提交的运单与之前保存的所属用户不一样！");
									}
								}
								order.getRuser().setId(order2.getRuser().getId());
								order.getSuser().setId(order2.getSuser().getId());
								order.setId(order2.getId());
							}
							else
							{
								return generateResponseObject(ResponseCode.PARAMETER_ERROR, "提交在线置单不能为空！");
							}
							
							
						}
						else if(!StringUtil.isEmpty(order.getType())&&(order.getType().equalsIgnoreCase("-9")))//表示上门收货
						{

							order.setType(Constant.ORDER_TYPE_KEY_5);//设置为上门收货
							if(!StringUtil.isEmpty(order.getOrderId()))
							{
								if(StringUtil.isEmpty(order.getUserId()))
								{
									return generateResponseObject(ResponseCode.PARAMETER_ERROR, "没有包含会员信息!");
								}
								M_order order2=this.m_orderDao.getByOrderId(order.getOrderId(), null);//
								if(order2==null)
								{
									return generateResponseObject(ResponseCode.PARAMETER_ERROR, "查找不到对应的运单！");
								}
								else
								{
									if(!Constant.ORDER_STATE__10.equalsIgnoreCase(order2.getState()))
									{
										return generateResponseObject(ResponseCode.PARAMETER_ERROR, "此运单不是在线运单，不能实现修改！");
									}
									if(!order.getUserId().equalsIgnoreCase(order2.getUserId()))
									{
										return generateResponseObject(ResponseCode.PARAMETER_ERROR, "提交的运单与之前保存的所属用户不一样！");
									}
								}
								order.getRuser().setId(order2.getRuser().getId());
								order.getSuser().setId(order2.getSuser().getId());
								order.setId(order2.getId());
							}
							else
							{
								return generateResponseObject(ResponseCode.PARAMETER_ERROR, "提交在线置单不能为空！");
							}
							
							
						
						}
						else
						{
							order.setType(Constant.ORDER_TYPE_KEY_1);//设置为门市运单
						}
						
						double price =this.m_orderService.calculationM_orderFreight(order);
						
						//先保存已经计算的价格商品list
						List<M_OrderDetail> details=new ArrayList<M_OrderDetail>();
						for(M_OrderDetail md:order.getDetail())
						{
							M_OrderDetail aa=new M_OrderDetail();
							aa.setAllcprice(md.getAllcprice());
							aa.setBrands(md.getBrands());
							aa.setCommodityId(md.getCommodityId());
							aa.setCprice(md.getCprice());
							aa.setCtype(md.getCtype());
							aa.setId(md.getId());
							aa.setName(md.getName());
							aa.setOr(md.getOr());
							aa.setOrderId(md.getOrderId());
							aa.setOther(md.getOther());
							aa.setProductName(md.getProductName());
							aa.setQuantity(md.getQuantity());
							aa.setRemark(md.getRemark());
							aa.setTariff(md.getTariff());
							aa.setValue(md.getValue());
							aa.setWeight(md.getWeight());
							details.add(aa);
						}
								
						
						if(price<=0)
						{
							return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "总价钱不能小于或等于0，值为:"+Double.toString(price));
						}
						
						double cost=this.m_orderService.calculationM_OrderCostFreight(order);
						if(cost<=0)
						{
							return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "成本不能小于或等于0，请检查配置，值为:"+Double.toString(cost));
						}
						double typeprice=0;
						if(Constant.ORDER_TYPE_KEY_3.equalsIgnoreCase(order.getType()))//网上置单，要计算网上置单价格，并与会员价格进行比较，以低价格为准
						{
							typeprice=this.m_orderService.calculationM_orderFreight_bytype(order);//计算网上置单价格
							if(typeprice<=0.01)//类型价格小于等于0表示计算失败
							{
								order.setUser_price(Double.toString(price));
							}
							else
							{
								if(typeprice<price)//类型价格
								{
									order.setUser_price(Double.toString(price));//保存价格高者
									price=typeprice;//网上置单以小价格为准
									
								}
								else
								{
									order.setDetail(details);//保存以会员价格计算的商品信息
									order.setUser_price(Double.toString(typeprice));//保存价格高者
								}
							}
						}
						else if(Constant.ORDER_TYPE_KEY_5.equalsIgnoreCase(order.getType()))//上门收货价格设置
						{
							typeprice=this.m_orderService.calculationM_orderFreight_bytype(order);//计算上门收货价格
							if(typeprice<=0.01)//类型价格小于等于0表示计算失败
							{
								order.setUser_price(Double.toString(price));
							}
							else
							{
								if(typeprice>price)//上门收货以价格高者为准
								{
									order.setUser_price(Double.toString(typeprice));//保存价格高者
									price=typeprice;//网上置单以小价格为准
									
								}
								else
								{
									order.setDetail(details);//保存以会员价格计算的商品信息
									order.setUser_price(Double.toString(price));//保存价格高者
								}
							}
						}
						else
						{
							//计算普通会员与会员之间的价格并保存
							double nprice=this.m_orderService.calculationM_orderFreight_usertype(order,Constant.USER_TYPE_NORMAL);//计算普通会员的价格
							if(nprice>price)
							{
								order.setUser_price(Double.toString(nprice));
							}
							else
							{
								order.setUser_price(Double.toString(price));
							}
							order.setDetail(details);//保存以会员价格计算的商品信息
						}
						
						
						
						
						order.setState(Constant.ORDER_STATE2);//设置为待付款状态
						order.getRuser().setUseState("1");
						order.getSuser().setUseState("1");
						order.setTotalmoney(Double.toString(price));
						order.setTotalcost(Double.toString(cost));
						
						if(!StringUtil.isEmpty(order.getPrintway())&&(order.getPrintway().equalsIgnoreCase("1")))//海关匹配打单，所以要匹配身份证
						{
							//上传的包含身份证信息就不用匹配身份证了
							if(StringUtil.isEmpty(order.getRuser().getCardid())||StringUtil.isEmpty(order.getRuser().getCardtogether())||StringUtil.isEmpty(order.getRuser().getName()))
							{
								List<import_t_orders> list1 = new ArrayList<import_t_orders>();
								import_t_orders temp=new import_t_orders();
								temp.setR_name(order.getRuser().getName());
								list1.add(temp);
								ResponseObject<Object> obj=this.cardidManageService.verifycardid(list1, "1",request);//进行模糊匹配
								if((obj!=null)&&(ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj.getCode())))
								{
									list1=(List<import_t_orders>)obj.getData();
									obj.setData(null);
									obj=null;
									if((list1==null)||(list1.size()==0))
									{
										throw new Exception("匹配身份证信息失败");
									}
									if(list1.get(0).isResultflag())//匹配成功
									{
										//order.getRuser().setCardid(list1.get(0).getCardid());
										order.getRuser().setSecondName(list1.get(0).getCardname());
										//order.getRuser().setCardtogether(list1.get(0).getCardurl());
										order.getRuser().setCardlibId(list1.get(0).getCardlibId());
										order.getRuser().setRemark(list1.get(0).getResult());
									}
									else
									{
										throw new Exception("匹配身份证信息失败");
									}
								}
								else
								{
									throw new Exception("匹配身份证信息失败");
								}
								list1.clear();
							}
							
							
							//海关单号
							/*String date2 = DateUtil.date2String(new Date());
							if(StringUtil.isEmpty(order.getSorderId()))
							{
								//获取海关单
								SeaNumber seano=seaNumberDao.getone();
								if((seano==null)||(StringUtil.isEmpty(seano.getOrderId())))
								{
									if(seano!=null)
									{
										seano.setState("1");
										seano.setModifyDate(date2);
										int a=seaNumberDao.updatestate(seano);
										if(a==0)
										{
											throw new Exception("修改海关单状态失败!");
										}
										order.setModifyDate(date2);
										a=this.m_orderDao.updateSorderid(order);
										if(a==0)
										{
											throw new Exception("个性海关单号到运单中失败!");
										}
									}
									throw new Exception("获取海关号失败!");
								}else
								{
									order.setSorderId(seano.getOrderId());
									seano.setState("1");
									//seano.setModifyDate(date);
									int a=seaNumberDao.updatestate(seano);
									
									if(a==0)
									{
										throw new Exception("修改海关单状态失败!");
									}
									//order.setModifyDate(date);
									//a=this.m_orderDao.updateSorderid(order);
									//if(a==0)
								//	{
									//	throw new Exception("修改海关单号到运单中失败!");
									//}
									//changeflage=true;
								}
							}*/
						}
						
						//return this.m_orderService.add_ms_Morder(order);
						return this.m_orderService.modify_ms_Morder(order);
						
					}catch(Exception e){
						
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
					}
				}
				
				
				
				
				//在清单列表中修改
				@RequestMapping(value="/admin/m_order/list_modify_order", method={RequestMethod.POST,RequestMethod.GET})
				@ResponseBody
				public ResponseObject<Object> modifyhistoryorder(HttpServletRequest request,M_order order,
						@RequestParam(value = "payflag", required = false) String payflag,//1表示支付运单费用，其它表示修改保存运单
						@RequestParam(value = "cardurlfile", required = false) MultipartFile file,
						@RequestParam(value = "cardurlotherfile", required = false) MultipartFile fileother,
						@RequestParam(value = "cardurltogetherfile", required = false) MultipartFile filetogether){
					try{
						String storeId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
						if(StringUtil.isEmpty(storeId)){
							return generateResponseObject(ResponseCode.NEED_LOGIN,
									"请登陆！");
						}
						
						if(order==null)
						{
							return generateResponseObject(ResponseCode.PARAMETER_ERROR,
									"参数出错！");
						}
						
						//身份证号不为空，要检查合法性
						if(!StringUtil.isEmpty(order.getRuser().getCardid()))
						{
							if (!ConsigneeInfoUtil.validateCardId(order.getRuser().getCardid())) {
								return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
										"身份证号码填写错误，请重新填写！");
							}
						}
						//只要有一个上传不为空，身份证号就不为空
						if ((file != null && file.getSize() > 0)||(fileother != null && fileother.getSize() > 0)||(filetogether != null && filetogether.getSize() > 0))
						{
							if(StringUtil.isEmpty(order.getRuser().getCardid()))
							{
								return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
										"上传图片时，身份证号码不能为空，请重新填写！");
							}
							
							//上传正反两面图片时，必须保证两面图片都正确并存在
							if((file != null && file.getSize()>0)&&(fileother==null))
							{
								if(!StringUtil.isEmpty(order.getRuser().getCardother()))
								{
									File file3 = new File(request.getSession().getServletContext()
											.getRealPath("/")
											+ order.getRuser().getCardother());
									if(!file3.exists())
									{
										return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
												"正反两面照片必须同时存在！");
									}
								}
								else
								{
									return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
											"正反两面照片必须同时存在！");
								}
							}
							if((fileother != null && fileother.getSize()>0)&&(file==null))
							{
								if(!StringUtil.isEmpty(order.getRuser().getCardurl()))
								{
									File file3 = new File(request.getSession().getServletContext()
											.getRealPath("/")
											+ order.getRuser().getCardurl());
									if(!file3.exists())
									{
										return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
												"正反两面照片必须同时存在！");
									}
								}
								else
								{
									return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
											"正反两面照片必须同时存在！");
								}
							}
							
							
					
						}
						// 处理提交上来的图片
						// 解决火狐的反斜杠问题 kai 20151006
						String filetype = this.defaultCardFileType;// 要上传的文件类型
						String strtest = this.save258CardDir;
						String strseparator = "";
						if (strtest.indexOf("/") >= 0)// 包含有“/”字符，分隔符用此字符
						{
							strseparator = "/";
						} else {
							strseparator = File.separator;
						}
						//开始处理图片
						// 获取当时的时间缀
						Date date = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyyMMddHHmmss");
						String timestr = sdf.format(date);
						//如果上传的图片中包含合成图片，那么即会忽略其它的正反两面图片
						if (filetogether != null && filetogether.getSize() > 0)
						{
							String fileName = null;
						
							if (filetogether.getSize() > this.defaultCardFileSize) {
								return generateResponseObject(
										ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
							}

							String originalName = filetogether.getOriginalFilename();

							if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
								return generateResponseObject(
										ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
							}
							int index = originalName.lastIndexOf('.');
							index = Math.max(index, 0);
							
							fileName = this.save258CardDir + strseparator + timestr + "_"+storeId+ "_"+"hc"+"_"
									+ StringUtil.generateRandomString(5) + "_"
									+ StringUtil.generateRandomInteger(5)
									+ originalName.substring(index);
							try {
								File file1 = new File(request.getSession().getServletContext()
										.getRealPath("/")
										+ fileName);
								filetogether.transferTo(file1);
							} catch (Exception e) {
								log.error("保存用户合成图像失败,请不要上传图像！", e);
								return generateResponseObject(
										ResponseCode.CONSIGNEE_CARD_ERROR,
										"保存用户合成图像失败，请去除上传图像后再尝试!");
							}
							
							order.getRuser().setCardtogether(fileName);
						}
						else
						{
							//正反图片有上传的图片
							if ((file != null && file.getSize() > 0)||(fileother != null && fileother.getSize() > 0))
							{
								//正面图片处理
								if(file != null && file.getSize() > 0)
								{
									String fileName = null;
									if (file.getSize() > this.defaultCardFileSize) {
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
									}
					
									String originalName = file.getOriginalFilename();
					
									if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
									}
									int index = originalName.lastIndexOf('.');
									index = Math.max(index, 0);
									
									fileName = this.save258CardDir + strseparator + timestr + "_"+storeId+ "_"+"zm"+"_"
											+ StringUtil.generateRandomString(5) + "_"
											+ StringUtil.generateRandomInteger(5)
											+ originalName.substring(index);
									try {
										File file1 = new File(request.getSession().getServletContext()
												.getRealPath("/")
												+ fileName);
										file.transferTo(file1);
									} catch (Exception e) {
										log.error("保存用户正面图像失败,请不要上传图像！", e);
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR,
												"保存用户正面图像失败，请去除上传图像后再尝试!");
									}
									
									order.getRuser().setCardurl(fileName);
								}
								
								//反面图片处理
								if(fileother != null && fileother.getSize() > 0)
								{
									String fileName = null;
									if (fileother.getSize() > this.defaultCardFileSize) {
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
									}
					
									String originalName = fileother.getOriginalFilename();
					
									if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
									}
									int index = originalName.lastIndexOf('.');
									index = Math.max(index, 0);
									
									fileName = this.save258CardDir + strseparator + timestr + "_"+storeId+ "_"+"fm"+"_"
											+ StringUtil.generateRandomString(5) + "_"
											+ StringUtil.generateRandomInteger(5)
											+ originalName.substring(index);
									try {
										File file1 = new File(request.getSession().getServletContext()
												.getRealPath("/")
												+ fileName);
										fileother.transferTo(file1);
									} catch (Exception e) {
										log.error("保存用户反面图像失败,请不要上传图像！", e);
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR,
												"保存用户反面图像失败，请去除上传图像后再尝试!");
									}
									
									order.getRuser().setCardother(fileName);
								}
								
								
								//合成处理
								String filecardtemp = "";
								if ((!StringUtil.isEmpty(order.getRuser().getCardurl())) && (!StringUtil.isEmpty(order.getRuser().getCardother()))) {
									imgcompose img = new imgcompose();
									String str1 = request.getSession().getServletContext()
											.getRealPath("/")
											+ order.getRuser().getCardurl();
									String str2 = request.getSession().getServletContext()
											.getRealPath("/")
											+ order.getRuser().getCardother();
									
									
									File file4 = new File(str1);
									File file5 = new File(str2);
									if((file4.exists())&&(file5.exists()))
									{
										String str3 = this.save258CardDir + strseparator + timestr + "_"+storeId+ "_"+"hc"+"_"
												+ StringUtil.generateRandomString(5) + "_"
												+ StringUtil.generateRandomInteger(5);
										filecardtemp = str3;
										str3 = request.getSession().getServletContext().getRealPath("/")
												+ str3;
										if (img.createcompics(str1, str2, str3)) {
											filecardtemp = filecardtemp + ".jpg";
										}
									}
									
								}
								
								if(!StringUtil.isEmpty(filecardtemp))
								{
									order.getRuser().setCardtogether(filecardtemp);
								}
								
							}
							
							
							
						}
						
						
				
						
						//pageIndex = Math.max(pageIndex, 1);
						if (!ConsigneeInfoUtil.validatePhone(order.getRuser().getPhone())) {
						      return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "收件人手机号码填写错误，请检查！");
						}
						if (!ConsigneeInfoUtil.validatePhone(order.getSuser().getPhone())) {
						      return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "发件人手机号码填写错误，请检查！");
						}
						String empId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY));
						
						
					
						//此部分的目的是把前端修改过的参数全部代入已有的参数中，再调用用修改函数
						boolean payornotchange=false;//记录支付状态是否变更
						boolean changestate=false;//记录路由状态是否变更
						String storeId1=storeId;
						storeId=null;
						String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
						if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
						{
							storeId=null;//表示可以查找所有门店
							
						}else
						{
							storeId = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
							if((storeId==null)||(storeId.equalsIgnoreCase("")))
							{
								return generateResponseObject(ResponseCode.NEED_LOGIN,
										"你没有登陆!");
							}
						}
						M_order order1=null;//查找得到的先前的运单信息
						if(StringUtil.isEmpty(order.getId()))
						{
							return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数出错,没有id号!");
						}
						else
						{
							 order1=this.m_orderDao.getById(order.getId(), storeId);
							if(order1==null)
							{
								return generateResponseObject(ResponseCode.PARAMETER_ERROR, "没有找到对应的运单的id号,参数可能出错!");
							}
							if(!StringUtil.isEmpty(payflag)&&payflag.equalsIgnoreCase("1"))//进行支付
							{
								if(Constant.ORDER_PAY_STATE0.equalsIgnoreCase(order.getPayWay()))//余额支付要检查用户是否存在
								{
									if(order1.getUser()==null)
									{
										return generateResponseObject(ResponseCode.PARAMETER_ERROR, "只有会员才有可能进行余额支付!");
									}
								}
		
								if(Constant.ORDER_PAY_STATE1.equalsIgnoreCase(order1.getPayornot()))
								{
									return generateResponseObject(ResponseCode.PARAMETER_ERROR, "已经支付的运单不能再进行支付!");
								}
								order1.setPayWay(order.getPayWay());
							}
							else
							{
								
							}
							if(!order.getPayornot().equalsIgnoreCase(order1.getPayornot()))
							{
								payornotchange=true;
							}
							if(!order.getState().equalsIgnoreCase(order1.getState()))
							{
								changestate=true;
							}
							//如果原来没有此管理员的，要加上管理员id
							if(StringUtil.isEmpty(order1.getEmployeeId())||order1.getEmployeeId().equalsIgnoreCase("-1")||order1.getI_employeeId().equalsIgnoreCase("0"))
							{
								order1.setEmployeeId(empId);
							}
							
							
							//可能修改的寄件人信息
							order1.getSuser().setName(order.getSuser().getName());
							order1.getSuser().setPhone(order.getSuser().getPhone());
							order1.getSuser().setAddress(order.getSuser().getAddress());
							order1.getSuser().setCity(order.getSuser().getCity());
							order1.getSuser().setState(order.getSuser().getState());
							order1.getSuser().setZipcode(order.getSuser().getZipcode());
							order1.getSuser().setEmail(order.getSuser().getEmail());
							order1.getSuser().setCompany(order.getSuser().getCompany());
							//支付方式可能会修改
							//order1.setPayWay(order.getPayWay());
							
							//收件人可能修改的信息
							order1.getRuser().setName(order.getRuser().getName());
							order1.getRuser().setPhone(order.getRuser().getPhone());
							order1.getRuser().setState(order.getRuser().getState());
							order1.getRuser().setCity(order.getRuser().getCity());
							order1.getRuser().setDist(order.getRuser().getDist());
							order1.getRuser().setAddress(order.getRuser().getAddress());
							order1.getRuser().setZipcode(order.getRuser().getZipcode());
							order1.getRuser().setCardid(order.getRuser().getCardid());
							order1.getRuser().setCardurl(order.getRuser().getCardurl());
							order1.getRuser().setCardother(order.getRuser().getCardother());
							order1.getRuser().setCardtogether(order.getRuser().getCardtogether());
							
							
							//替换原来的商品信息
							order1.setDetail(order.getDetail());
							
							//替换全局参数
							order1.setWeight(order.getWeight());
							order1.setSjweight(order.getSjweight());
							order1.setQuantity(order.getQuantity());
							order1.setValue(order.getValue());
							order1.setInsurance(order.getInsurance());
							order1.setOther(order.getOther());
							order1.setTariff(order.getTariff());
							order1.setRemark(order.getRemark());
							order1.setQremark(order.getQremark());
							order1.setState(order.getState());
							order1.setPayornot(order.getPayornot());
							order1.setAutomessage(order.getAutomessage());
							
							order1.setStoreId(order.getStoreId());
							order1.setChannelId(order.getChannelId());
							
							if(StringUtil.isEmpty(order1.getEmployeeId()))
							{
								order1.setEmployeeId(empId);
							}
						}
						
						//替换成现在的
						order=order1;
						
						
						
						
						
						
						
						
						
						
						double price =this.m_orderService.calculationM_orderFreight(order);
						
						//先保存已经计算的价格商品list
						List<M_OrderDetail> details=new ArrayList<M_OrderDetail>();
						for(M_OrderDetail md:order.getDetail())
						{
							M_OrderDetail aa=new M_OrderDetail();
							aa.setAllcprice(md.getAllcprice());
							aa.setBrands(md.getBrands());
							aa.setCommodityId(md.getCommodityId());
							aa.setCprice(md.getCprice());
							aa.setCtype(md.getCtype());
							aa.setId(md.getId());
							aa.setName(md.getName());
							aa.setOr(md.getOr());
							aa.setOrderId(md.getOrderId());
							aa.setOther(md.getOther());
							aa.setProductName(md.getProductName());
							aa.setQuantity(md.getQuantity());
							aa.setRemark(md.getRemark());
							aa.setTariff(md.getTariff());
							aa.setValue(md.getValue());
							aa.setWeight(md.getWeight());
							details.add(aa);
						}
						
						
						if(price<=0)
						{
							return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "总价钱不能小于或等于0，值为:"+Double.toString(price));
						}
						double cost=this.m_orderService.calculationM_OrderCostFreight(order);
						if(cost<=0)
						{
							return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "成本不能小于或等于0，请检查配置，值为:"+Double.toString(cost));
						}
						
						
						
						double typeprice=0;
						if(Constant.ORDER_TYPE_KEY_3.equalsIgnoreCase(order.getType()))//网上置单，要计算网上置单价格，并与会员价格进行比较，以低价格为准
						{
							order.setType(Constant.ORDER_TYPE_KEY_3);
							typeprice=this.m_orderService.calculationM_orderFreight_bytype(order);//计算网上置单价格
							if(typeprice<=0.01)//类型价格小于等于0表示计算失败
							{
								order.setUser_price(Double.toString(price));
							}
							else
							{
								if(typeprice<price)//类型价格
								{
									order.setUser_price(Double.toString(price));
									price=typeprice;//网上置单以小价格为准
									
								}
								else
								{
									order.setDetail(details);//保存以会员价格计算的商品信息
									order.setUser_price(Double.toString(typeprice));
								}
							}
						}
						else if(Constant.ORDER_TYPE_KEY_5.equalsIgnoreCase(order.getType()))//上门收货价格设置
						{
							order.setType(Constant.ORDER_TYPE_KEY_5);
							typeprice=this.m_orderService.calculationM_orderFreight_bytype(order);//计算上门收货价格
							if(typeprice<=0.01)//类型价格小于等于0表示计算失败
							{
								//price=typeprice;//网上置单以小价格为准
								order.setUser_price(Double.toString(price));
							}
							else
							{
								if(typeprice>price)
								{
									order.setUser_price(Double.toString(typeprice));
									price=typeprice;//价格以高者为准
								}
								else
								{
									order.setDetail(details);//保存以会员价格计算的商品信息
									order.setUser_price(Double.toString(price));
								}
							}
						}
						else
						{
							//计算普通会员与会员之间的价格并保存
							double nprice=this.m_orderService.calculationM_orderFreight_usertype(order,Constant.USER_TYPE_NORMAL);//计算普通会员的价格
							if(nprice>price)
							{
								order.setUser_price(Double.toString(nprice));
							}
							else
							{
								order.setUser_price(Double.toString(price));
							}
							order.setDetail(details);//保存以会员价格计算的商品信息
						}
						
						
						//order.setState(Constant.ORDER_STATE2);//设置为待付款状态
						//order.getRuser().setUseState("1");
						//order.getSuser().setUseState("1");
						order.setTotalmoney(Double.toString(price));
						order.setTotalcost(Double.toString(cost));
						
						if(!StringUtil.isEmpty(order.getPrintway())&&(order.getPrintway().equalsIgnoreCase("1")))//海关匹配打单，所以要匹配身份证
						{
							//上传的包含身份证信息就不用匹配身份证了
							if(StringUtil.isEmpty(order.getRuser().getCardid())||StringUtil.isEmpty(order.getRuser().getCardtogether())||StringUtil.isEmpty(order.getRuser().getName()))
							{
								List<import_t_orders> list1 = new ArrayList<import_t_orders>();
								import_t_orders temp=new import_t_orders();
								temp.setR_name(order.getRuser().getName());
								list1.add(temp);
								ResponseObject<Object> obj=this.cardidManageService.verifycardid(list1, "1",request);//进行模糊匹配
								if((obj!=null)&&(ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj.getCode())))
								{
									list1=(List<import_t_orders>)obj.getData();
									obj.setData(null);
									obj=null;
									if((list1==null)||(list1.size()==0))
									{
										throw new Exception("匹配身份证信息失败");
									}
									if(list1.get(0).isResultflag())//匹配成功
									{
										//order.getRuser().setCardid(list1.get(0).getCardid());
										order.getRuser().setSecondName(list1.get(0).getCardname());
										//order.getRuser().setCardtogether(list1.get(0).getCardurl());
										order.getRuser().setCardlibId(list1.get(0).getCardlibId());
										order.getRuser().setRemark(list1.get(0).getResult());
									}
									else
									{
										throw new Exception("匹配身份证信息失败");
									}
								}
								else
								{
									throw new Exception("匹配身份证信息失败");
								}
								list1.clear();
							}
							
							
							//海关单号
							/*String date2 = DateUtil.date2String(new Date());
							if(StringUtil.isEmpty(order.getSorderId()))
							{
								//获取海关单
								SeaNumber seano=seaNumberDao.getone();
								if((seano==null)||(StringUtil.isEmpty(seano.getOrderId())))
								{
									if(seano!=null)
									{
										seano.setState("1");
										seano.setModifyDate(date2);
										int a=seaNumberDao.updatestate(seano);
										if(a==0)
										{
											throw new Exception("修改海关单状态失败!");
										}
										order.setModifyDate(date2);
										a=this.m_orderDao.updateSorderid(order);
										if(a==0)
										{
											throw new Exception("个性海关单号到运单中失败!");
										}
									}
									throw new Exception("获取海关号失败!");
								}else
								{
									order.setSorderId(seano.getOrderId());
									seano.setState("1");
									//seano.setModifyDate(date);
									int a=seaNumberDao.updatestate(seano);
									
									if(a==0)
									{
										throw new Exception("修改海关单状态失败!");
									}
									//order.setModifyDate(date);
									//a=this.m_orderDao.updateSorderid(order);
									//if(a==0)
								//	{
									//	throw new Exception("修改海关单号到运单中失败!");
									//}
									//changeflage=true;
								}
							}*/
						}
			
						//return this.m_orderService.add_ms_Morder(order);
						return this.m_orderService.modify_orpay_Morder(order,payornotchange,changestate,payflag,empId,storeId1);
						
					}catch(Exception e){
						
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
					}
				}
				
				
				
				
				
				//根据上传的运单号，导出对应海关的模板

				@RequestMapping(value = "/admin/m_order/import_table_meitao", method = { RequestMethod.POST })
				@ResponseBody
				public ResponseObject<Object> importOrderFromMeitaoExcel(
						HttpServletRequest request,
						HttpServletResponse response, 
						@RequestParam(value = "inputmode", required = false) String inputmode,//导出模板
						@RequestParam(value = "inputrude", required = false) String inputrude,//匹配原则
						@RequestParam(value = "picdownload", required = false) String picdownload,//是否下载图片，1表示下载，其它表示不下载
						@RequestParam(value = "getseaNo", required = false) String getseaNo,//是否获取库存海关单号，1表示获取，其它表示不获取
						
						
						@RequestParam(value = "filemeitao", required = false) MultipartFile file) {

					if(StringUtil.isEmpty(inputmode)||StringUtil.isEmpty(inputrude))
					{
						return generateResponseObject(ResponseCode.PARAMETER_ERROR,
								"参数有误！");
					}
					
					
					String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
					String storeId=null;
					if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
					{
					
					}
					else
					{
						String master = (String)request.getSession().getAttribute(Constant.EMP_STORE_MASTER_SESSION_KEY);
						storeId=(String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
						if((master!=null)&&(master.equalsIgnoreCase("1")))
						{
							
						}
						else
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"对不起，只有高级管理员或店长能够导出模板信息!");
						}
					}

					
				/*	String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
					if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
					{
						return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权进行此操作!");
						
					}*/
					// 图片流
					InputStream imgInputStream = null;
					OutputStream os = null;
					OutputStream os1 = null;
					List<ExportMorder> Orders = null;
					if (file != null && file.getSize() > 0) {
						
						try {
							//kai 20160315判定是不是符合excel表格
							String originalName = file.getOriginalFilename();
							if (!StringUtil.boolpicisgoodornot(originalName, defaultExcelFileType)) {
								return new ResponseObject<Object>(
										ResponseCode.CONSIGNEE_CARD_ERROR, "必须上传excel 2003表格,请重新尝试!");
							}
							Orders=M_OrderUtil.readorderidExcel(file
									.getInputStream());
							//Orders = CardidManageControllerUtil.readgetCardidFromMeitaoExcel(file
							//		.getInputStream());
							
						} catch (OutOfMemoryError e) {
							log.error("内存不够", e);
							return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
									"内存不够");
						} catch (Exception e) {
							log.error("读取数据出错", e);
							String str = e.getMessage();// java.lang.RuntimeException:
							if ((str != null) && (!str.equalsIgnoreCase(""))) {
								str = str.replace("java.lang.RuntimeException:", "");
							}

							return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
									"读取数据出错原因:" + str);
						}

						try {
							
							for(ExportMorder eorder:Orders)
							{
								eorder.setExportmodel(inputmode);
								eorder.setCardmodel(inputrude);
								eorder.setDownloadpic(picdownload);
								eorder.setGetSeaNo(getseaNo);
								if(!StringUtil.isEmpty(eorder.getOrderId()))
								{
									M_order order=this.m_orderDao.getByOrderId(eorder.getOrderId(), storeId);
									if(order==null)
									{
										eorder.setOrderResult("获取运单信息失败，请检查单号是否存在或是否属于本门店！");
									}
									else
									{
										eorder.setOrder(order);
									}
								}
								else
								{
									eorder.setOrderResult("失败，运单号不能为空");
								}
							}
							
							
							//ResponseObject<Object> obj=null;
							//深圳海关的在原来基础上最后一列加上身份证号
							//if(inputmode.equalsIgnoreCase("2")||StringUtil.isEmpty(inputrude)||(inputrude.equalsIgnoreCase("2")))//不检查身份证信息
							if(StringUtil.isEmpty(inputrude)||(inputrude.equalsIgnoreCase("2")))//不检查身份证信息
							{
								
								for(ExportMorder eorder:Orders)
								{
									
									//获取公司名称和公司地址
									if(eorder.getOrder()!=null&&(!StringUtil.isEmpty(eorder.getOrder().getStoreId())))
									{
										Warehouse ware=this.warehouseDao.getById(eorder.getOrder().getStoreId());
										if(ware!=null)
										{
											eorder.setCompanyName(ware.getCompany());
											String address=ware.getAddress();
											if(!StringUtil.isEmpty(ware.getCity()))
											{
												if(StringUtil.isEmpty(address))
												{
													address=address+", "+ware.getCity();
												}
												else
												{
													address=address+", "+ware.getCity();
												}
											}
											
											if(!StringUtil.isEmpty(ware.getProvince()))
											{
												if(StringUtil.isEmpty(address))
												{
													address=address+", "+ware.getProvince();
												}
												else
												{
													address=address+", "+ware.getProvince();
												}
											}
											if(!StringUtil.isEmpty(ware.getCountry()))
											{
												if(StringUtil.isEmpty(address))
												{
													address=address+", "+ware.getCountry();
												}
												else
												{
													address=address+", "+ware.getCountry();
												}
											}
											
											if(!StringUtil.isEmpty(ware.getZipCode()))
											{
												if(StringUtil.isEmpty(address))
												{
													address=address+", "+ware.getZipCode();
												}
												else
												{
													address=address+", "+ware.getZipCode();
												}
											}
											eorder.setCompanyAddress(address);
										}
									}
								}
							}
							else
							{
									
								for(ExportMorder eorder:Orders)
								{
									
									//获取公司名称和公司地址
									if(eorder.getOrder()!=null&&(!StringUtil.isEmpty(eorder.getOrder().getStoreId())))
									{
										Warehouse ware=this.warehouseDao.getById(eorder.getOrder().getStoreId());
										if(ware!=null)
										{
											eorder.setCompanyName(ware.getCompany());
											String address=ware.getAddress();
											if(!StringUtil.isEmpty(ware.getCity()))
											{
												if(StringUtil.isEmpty(address))
												{
													address=address+", "+ware.getCity();
												}
												else
												{
													address=address+", "+ware.getCity();
												}
											}
											
											if(!StringUtil.isEmpty(ware.getProvince()))
											{
												if(StringUtil.isEmpty(address))
												{
													address=address+", "+ware.getProvince();
												}
												else
												{
													address=address+", "+ware.getProvince();
												}
											}
											if(!StringUtil.isEmpty(ware.getCountry()))
											{
												if(StringUtil.isEmpty(address))
												{
													address=address+", "+ware.getCountry();
												}
												else
												{
													address=address+", "+ware.getCountry();
												}
											}
											
											if(!StringUtil.isEmpty(ware.getZipCode()))
											{
												if(StringUtil.isEmpty(address))
												{
													address=address+", "+ware.getZipCode();
												}
												else
												{
													address=address+", "+ware.getZipCode();
												}
											}
											eorder.setCompanyAddress(address);
										}
									}
									
									
									if(eorder.getOrder()==null)
									{
										continue;
									}
									
									boolean changeflage=false;
									
									
									
									boolean cardtrue=false;	
									if(!StringUtil.isEmpty(eorder.getOrder().getRuser().getCardlibId()))
									{
										
										CardId_lib lib=this.cardIdManageDao.getbyid(eorder.getOrder().getRuser().getCardlibId());
										if(lib==null)//为空要重新查找
										{
											
										}
										else
										{
											if(!StringUtil.isEmpty(inputrude)&&inputrude.equalsIgnoreCase("0"))//绝对匹配要检查之前的姓名是否相同
											{
												if(!StringUtil.isEmpty(lib.getCname())&&(lib.getCname().equalsIgnoreCase(eorder.getOrder().getRuser().getName())))
												{
													cardtrue=true;
													eorder.getOrder().getRuser().setCardlib(lib);
												}
											}
											else
											{
												cardtrue=true;
												eorder.getOrder().getRuser().setCardlib(lib);
											}
										}
										
									}
									
									//原来收件人的身份信息为空，要重新提取
									if(cardtrue==false)
									{
										
										List<import_t_orders> list1 = new ArrayList<import_t_orders>();
										import_t_orders temp=new import_t_orders();
										temp.setR_name(eorder.getOrder().getRuser().getName());
										temp.setR_phone(eorder.getOrder().getRuser().getPhone());
										list1.add(temp);
										ResponseObject<Object> obj1=null;
										try
										{
											obj1=this.cardidManageService.verifycardid(list1, inputrude,request);//进行匹配
										}
										catch(Exception e)
										{
											eorder.setCardResult("失败，匹配身份证信息发生异常!");
										}
										if((obj1!=null)&&(ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj1.getCode())))
										{
											list1=(List<import_t_orders>)obj1.getData();
											obj1.setData(null);
											obj1=null;
											if((list1==null)||(list1.size()==0))
											{
												eorder.setCardResult("失败，匹配身份证信息发生异常!");
												//throw new Exception("匹配身份证信息失败");
											}
											if(list1.get(0).isResultflag())//匹配成功
											{
												//order.getRuser().setCardid(list1.get(0).getCardid());
												eorder.getOrder().getRuser().setSecondName(list1.get(0).getCardname());
												//order.getRuser().setCardtogether(list1.get(0).getCardurl());
												eorder.getOrder().getRuser().setRemark(list1.get(0).getResult());
												String date = DateUtil.date2String(new Date());
												eorder.getOrder().getRuser().setModifyDate(date);
												eorder.getOrder().getRuser().setCardlibId(list1.get(0).getCardlibId());
												
												CardId_lib lib=this.cardIdManageDao.getbyid(eorder.getOrder().getRuser().getCardlibId());
												if(lib==null)
												{
													eorder.setCardResult("失败，获取身份证信息失败！");
													//throw new Exception("获取身份证信息失败！");
												}
												else
												{
													eorder.getOrder().getRuser().setCardlib(lib);
												}
												
												
												int k=this.receive_UserDao.modifymatchinfo(eorder.getOrder().getRuser());
												if(k==0)
												{
													eorder.setCardResult("失败，修改收件人信息失败!");
													//throw new Exception("修改收件人信息失败！");
												}
												
											}
											else
											{
												eorder.setCardResult("失败，匹配身份证信息发生异常!"+list1.get(0).getResult());
											}
											changeflage=true;
										}
										else
										{
											eorder.setCardResult("失败，匹配身份证信息发生异常!");
											//return obj1;
										}
										list1.clear();
									
									}
								
								}	
								
									//obj=this.cardidManageService.verifycardid(Orders, inputrude,request);
									//obj=this.cardidManageService.verifycardid_ex(Orders, rule, request);
							}
							
							
							
							String fileName="";
							if(inputmode.equalsIgnoreCase("0")||inputmode.equalsIgnoreCase("1")||inputmode.equalsIgnoreCase("3"))
							{}
							
							
							
								if (Orders != null && Orders.size() >0) {
									
									
									//是否匹配海关单号
									
									if(!StringUtil.isEmpty(getseaNo)&&(getseaNo.equalsIgnoreCase("1")||getseaNo.equalsIgnoreCase("2"))&&inputmode.equalsIgnoreCase("2"))//要匹配海关号，只有深圳海关模板起作用
									{
										for(ExportMorder eorder:Orders)
										{
											//获取海关单号
											if(eorder.getOrder()!=null)
											{
												if(!StringUtil.isEmpty(eorder.getOrder().getSorderId())&&(!eorder.getOrder().getSorderId().trim().equalsIgnoreCase(""))&&(!getseaNo.equalsIgnoreCase("2")))//原来包含海关单号的，不匹配,2表示要重新匹配
												{
													if(eorder.getOrder().getSorderId().trim().length()==20)
													{
														eorder.setGetSeaNoResult("原有海关单号");
													}
													else
													{

														SeaNumber number=this.seaNumberDao.getone();
														if(number==null)
														{
															eorder.setGetSeaNoResult("失败：获取海关单号失败，原有单号长度不等于20！");
														}
														else
														{
															if(StringUtil.isEmpty(number.getOrderId()))
															{
																eorder.setGetSeaNoResult("失败：获取到的海关单号为空，原有单号长度不等于20！");
															}
															else
															{
																String date = DateUtil.date2String(new Date());
																eorder.getOrder().setSorderId(number.getOrderId());
																eorder.getOrder().setModifyDate(date);
																int a=this.m_orderDao.updateSorderid(eorder.getOrder());
																if(a==0)
																{
																	throw new Exception("修改海关单号到运单中失败!");
																}
																
																number.setState("1");
																number.setModifyDate(date);
																a=this.seaNumberDao.updatestate(number);
																if(a==0)
																{
																	throw new Exception("修改海关单状态失败!");
																}
																
																eorder.setGetSeaNoResult("获取海关单号成功,替换原来不足20位的海关条形码！");
																
															}
														}
													
													}
												}
												else
												{
													SeaNumber number=this.seaNumberDao.getone();
													if(number==null)
													{
														if(getseaNo.equalsIgnoreCase("2"))
														{
															eorder.setGetSeaNoResult("失败：重新获取海关单号失败！");
														}
														else
														{
															eorder.setGetSeaNoResult("失败：获取海关单号失败！");
														}
														
													}
													else
													{
														if(StringUtil.isEmpty(number.getOrderId()))
														{
															if(getseaNo.equalsIgnoreCase("2"))
															{
																eorder.setGetSeaNoResult("失败：重新获取到的海关单号为空！");
															}
															else
															{
																eorder.setGetSeaNoResult("失败：获取到的海关单号为空！");
															}
														}
														else
														{
															String date = DateUtil.date2String(new Date());
															eorder.getOrder().setSorderId(number.getOrderId());
															eorder.getOrder().setModifyDate(date);
															int a=this.m_orderDao.updateSorderid(eorder.getOrder());
															if(a==0)
															{
																throw new Exception("修改海关单号到运单中失败!");
															}
															
															number.setState("1");
															number.setModifyDate(date);
															a=this.seaNumberDao.updatestate(number);
															if(a==0)
															{
																throw new Exception("修改海关单状态失败!");
															}
															if(getseaNo.equalsIgnoreCase("2"))
															{
																eorder.setGetSeaNoResult("重新获取海关单号成功！");
															}
															else
															{
																eorder.setGetSeaNoResult("获取海关单号成功！");
															}
															
														}
													}
												}
											}
										}
									}
									
									
									
									
									//是否压缩身份证图片
									if(!StringUtil.isEmpty(picdownload)&&picdownload.equalsIgnoreCase("1"))
									{
										
										
										//ArrayList arlList=new ArrayList();
										ArrayList<String> arlList=new ArrayList();
										for(ExportMorder eorder:Orders)
										{
											//获取海关单号
											if(eorder.getOrder()!=null&&eorder.getOrder().getRuser()!=null&&eorder.getOrder().getRuser().getCardlib()!=null)
											{
												if(!StringUtil.isEmpty(eorder.getOrder().getRuser().getCardlib().getPicurl()))
												{
													arlList.add(eorder.getOrder().getRuser().getCardlib().getPicurl());
												}
											}
										}
										HashSet h = new HashSet(arlList);   
										arlList.clear();   
										arlList.addAll(h);
										File[] srcfile = new File[arlList.size()];
										int nn=0;
										for(String url:arlList)
										{
											
											srcfile[nn]=new File(request.getSession()
													.getServletContext().getRealPath("/")
													+ url);
											
											nn++;
										}
										
										
										String strtest = this.saveCardDir;
										String strseparator = "";
										if (strtest.indexOf("/") >= 0)// 包含有“/”字符，分隔符用此字符
										{
											strseparator = "/";
										} else {
											strseparator = File.separator;
										}
										
										Date date = new Date();
										SimpleDateFormat sdf = new SimpleDateFormat(
												"yyyyMMddHHmmss");
										String str = sdf.format(date);

										String originalName = str + ".zip";
										int index = originalName.lastIndexOf('.');
										index = Math.max(index, 0);
										 fileName = this.saveCardDir + File.separator + "temp"
												+ strseparator + StringUtil.generateRandomString(5)
												+ "_" + StringUtil.generateRandomInteger(5) + "_"
												// + originalName.substring(index);
												+ originalName;

										File file1 = new File(request.getSession()
												.getServletContext().getRealPath("/")
												+ fileName);
										
										
										if ((srcfile.length > 0) && (srcfile[0] != null)) {
											basiczip.zipFilesSameName(srcfile, file1);
											
											StringBuffer url = request.getRequestURL();  
											String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString(); 
											
											Orders.get(0).setSavezipurl(tempContextUrl+fileName);
											//Orders.get(0).setZipurl(tempContextUrl+fileName);
											//response.setContentType("application/zip");
											//response.setHeader("Content-Disposition", 
											//		"attachment; filename=\"" + originalName + "\""); 
											//response.setHeader("Location","download.zip");
										//	imgInputStream = new FileInputStream(file1);

											//byte[] imgBytes = IOUtils.toByteArray(imgInputStream);

										//	os1 = response.getOutputStream();

											//os1.write(imgBytes);
										} else {
											// return "订单中没有身份证图片，请退回重新选择！";
										}
									
									}
									
									
								 fileName = "success_result_"+inputmode+"_"
									+ Orders.size() + ".xls";
							
								response.setContentType("application/vnd.ms-excel");
								response.setHeader("Content-disposition",
										"attachment;filename="
												+ new String(fileName.getBytes(),
														"utf-8"));
								// orders = this.orderService.getExportOrders(sdate,
								// edate);

								if(inputmode.equalsIgnoreCase("0"))
								{
									File templeFile = new File(request.getSession()
											.getServletContext().getRealPath("/")
											+ this.meitaoneimengguimportseaordertemplets);
									os = response.getOutputStream();
									M_OrderUtil.exportneimenggumode(Orders, templeFile, os);
									//CardidManageControllerUtil.exportneimenggumode(Orders, templeFile, os);
								}
								else if(inputmode.equalsIgnoreCase("1"))
								{
									File templeFile = new File(request.getSession()
											.getServletContext().getRealPath("/")
											+ this.meitaoneimengguimportseaordertotheremplets);
									os = response.getOutputStream();
									M_OrderUtil.exportneimenggu(Orders, templeFile, os);
								//	CardidManageControllerUtil.exportneimenggu(Orders, templeFile, os);
								}
								else if(inputmode.equalsIgnoreCase("2"))
								{
									File templeFile = new File(request.getSession()
											.getServletContext().getRealPath("/")
											+ this.meitaoshenzhenimportseaordertemplets);
									os = response.getOutputStream();
									M_OrderUtil.exportshenzhen(Orders, templeFile, os);
									//CardidManageControllerUtil.exportshenzhen(Orders, templeFile, os);
								}
								else if(inputmode.equalsIgnoreCase("3"))//上海模板
								{
									File templeFile = new File(request.getSession()
											.getServletContext().getRealPath("/")
											+ this.meitaoshanghaiseaordermodetemplets);
									os = response.getOutputStream();
									M_OrderUtil.exportshanghaimode(Orders, templeFile, os);
									//CardidManageControllerUtil.exportshanghaimode(Orders, templeFile, os);
								}
								else if(inputmode.equalsIgnoreCase("4"))//A奶粉
								{
									
									String rate=this.globalargsDao.getcontentbyflag("cur_usa_cn");
									if(!StringUtil.isEmpty(rate))
									{
										try{
											double rate1=Double.parseDouble(rate);
											if(rate1>0)//保存汇率
											{
												for(ExportMorder eorder:Orders)
												{
													eorder.setRate(rate1);
												}
											}
										}catch(Exception e){}
									}
									
									for(ExportMorder eorder:Orders)
									{
										if(eorder.getOrder()!=null)
										{
											if(!StringUtil.isEmpty(eorder.getOrder().getStoreId()))
											{
												Warehouse ware=this.warehouseDao.getById(eorder.getOrder().getStoreId());
												eorder.setStore(ware);
											}
										}
										
									}
									
									
									File templeFile = new File(request.getSession()
											.getServletContext().getRealPath("/")
											+ this.ameitaonfmodeltemplets);
									os = response.getOutputStream();
									M_OrderUtil.exportA_NF(Orders, templeFile, os);
									
								}
								else if(inputmode.equalsIgnoreCase("5"))//A普货
								{
									
									String rate=this.globalargsDao.getcontentbyflag("cur_usa_cn");
									if(!StringUtil.isEmpty(rate))
									{
										try{
											double rate1=Double.parseDouble(rate);
											if(rate1>0)//保存汇率
											{
												for(ExportMorder eorder:Orders)
												{
													eorder.setRate(rate1);
												}
											}
										}catch(Exception e){}
									}
									
									for(ExportMorder eorder:Orders)
									{
										if(eorder.getOrder()!=null)
										{
											if(!StringUtil.isEmpty(eorder.getOrder().getStoreId()))
											{
												Warehouse ware=this.warehouseDao.getById(eorder.getOrder().getStoreId());
												eorder.setStore(ware);
											}
										}
										
									}
									
									
									File templeFile = new File(request.getSession()
											.getServletContext().getRealPath("/")
											+ this.ameitaopfmodeltemplets);
									os = response.getOutputStream();
									M_OrderUtil.exportA_PF(Orders, templeFile, os);
									
								}
								
								}
								else
								{
									return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
											"返回数据为空！");
								}
							
						
							return generateResponseObject(ResponseCode.SUCCESS_CODE);
						} catch (Exception e) {
							log.error("修改数据库失败", e);
							return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
									"提交失败：" + e.getMessage());
						}finally {
							// orders.clear();
							// 6.无论成功与否关闭相应的流
							try {
								if (imgInputStream != null) {
									imgInputStream.close();
								}
								if (os != null) {
									os.flush();
									os.close();
								}
								if (os1 != null) {
									os1.flush();
									os1.close();
								}
							} catch (IOException e) {
								System.err.println(e.getMessage());
							}

						}
					}
					return generateResponseObject(ResponseCode.PARAMETER_ERROR, "文件不能为空");
				}

				
				//上传模板下载 
				@RequestMapping(value = "/admin/m_order/upload_sno", method = { RequestMethod.GET })
				public void getuploadordersexampleDataExcelFile(HttpServletRequest request,
						HttpServletResponse response) {
					InputStream input = null;
					ServletOutputStream os=null;
					try {
						response.setContentType("application/vnd.ms-excel");
						response.setHeader("Content-disposition", "attachment;filename="
								+ new String("upload_cardids_example.xls".getBytes(),
										"utf-8"));
						input = request
								.getSession()
								.getServletContext()
								.getResourceAsStream(
										this.meitaoimportsorderidexampletemplets);
						os = response.getOutputStream();
						byte[] buffer = new byte[1024];
						int n = 0;
						while ((n = input.read(buffer)) > 0) {
							os.write(buffer, 0, n);
						}
						buffer=null;
						os.flush();
					} catch (Exception e) {
						log.error("下载文件失败", e);
					} finally {
						if (input != null) {
							try {
								
								input.close();
							} catch (IOException e) {
								// ignore
							}
						}
						if (os != null) {
							try {
								os.flush();
								os.close();
							} catch (IOException e) {
								// ignore
							}
						}
					}
				}

				
				
				// 20160429 导入美淘旧系统模板,是指没有发件人电话的模版
				@RequestMapping(value = "/admin/m_order/import_old_orders", method = { RequestMethod.POST })
				@ResponseBody
				public ResponseObject<Object> thirdimportOrderIdFromMeitaoExcel(
						HttpServletRequest request,
						HttpServletResponse response,
						@RequestParam(value = "uploadwid", required = false) String wid,
						@RequestParam(value = "uploadcid", required = false) String cid,
						@RequestParam(value = "uploadfile", required = false) MultipartFile file) {

					if(StringUtil.isEmpty(wid)||StringUtil.isEmpty(cid)||wid.equalsIgnoreCase("-1")||cid.equalsIgnoreCase("-1"))
					{
						return generateResponseObject(
								ResponseCode.PARAMETER_ERROR, "必须选择门店和渠道!");
					}
					String empId = StringUtil.obj2String(request.getSession().getAttribute(
							Constant.EMP_ID_SESSION_KEY));
					
					String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
					if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
					{
						String storeId = StringUtil.obj2String(request.getSession().getAttribute(
								Constant.EMP_STORE_ID_SESSION_KEY));
						String master=StringUtil.obj2String(request.getSession().getAttribute(
								Constant.EMP_STORE_MASTER_SESSION_KEY));
						
						if(!StringUtil.isEmpty(master)&&(master.equalsIgnoreCase("1")))//店长
						{
							if(!StringUtil.isEmpty(storeId)&&(storeId.equalsIgnoreCase(wid)))
							{
								
							}
							else
							{
								return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你不能够操作其它门店!");
							}
						}
						else
						{
							return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，只有高级管理员和店长能够进行此操作!");
						}
						
					}
					if (file != null && file.getSize() > 0) {
						List<M_order> Orders = null;
						try {
							//kai  判定是不是excel表格
							String originalName = file.getOriginalFilename();
							if (!StringUtil.boolpicisgoodornot(originalName, defaultExcelFileType)) {
								return generateResponseObject(
										ResponseCode.CONSIGNEE_CARD_ERROR, "必须上传excel 2003表格,请重新尝试!");
							}
							
							//先检查门店和渠道是否存在
							Warehouse ware=this.warehouseDao.getById(wid);
							if(ware==null)
							{
								return generateResponseObject(
										ResponseCode.PARAMETER_ERROR, "你选择导入的门店不存在!");
							}
							Channel channel=this.channelDao.getChannelById(cid);
							if(channel==null)
							{
								return generateResponseObject(
										ResponseCode.PARAMETER_ERROR, "没有找到对应的渠道信息!");
							}
							else
							{
								//判断此渠道是否已授权给此门店
								String storelist=channel.getStoreList();
								if(StringUtil.isEmpty(storelist))
								{
									return generateResponseObject(
											ResponseCode.PARAMETER_ERROR, "此门店并没有在渠道中得到授权!");
								}
								else
								{
									boolean flag=false;
									String[] list= storelist.split(";");
									for(int i=0;i<list.length;i++)
									{
										if((!StringUtil.isEmpty(list[i]))&&(list[i].equalsIgnoreCase(wid)))
										{
											flag=true;
											break;
										}
									}
									if(flag==false)
									{
										return generateResponseObject(
												ResponseCode.PARAMETER_ERROR, "此门店并没有在渠道中得到授权!");
									}
									
								}
								
								
							}
							
							
							//kai 20160429 获取美淘旧模板的数据，并在最后一列加入了状态
							Orders=M_OrderUtil.readOrderFromMeitaothirdExcel(file.getInputStream());
							
							
						} catch (OutOfMemoryError e) {
							log.error("内存不够", e);
							return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
									"内存不够,"+e.getMessage());
						} catch (Exception e) {
							log.error("读取数据出错,"+e.getMessage());
							String str = e.getMessage();// java.lang.RuntimeException:
							if ((str != null) && (!str.equalsIgnoreCase(""))) {
								str = str.replace("java.lang.RuntimeException:", "");
							}

							return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
									"读取数据出错原因:" + str);
						}
						OutputStream os = null;
						try {
							if(Orders==null)
							{
								return generateResponseObject(ResponseCode.PARAMETER_ERROR, "没有读取到数据!");
							}
							
							ImportthirdMorder iorder=new ImportthirdMorder();//导入的信息
							iorder.setOrders(Orders);
							iorder.setChannelId(cid);
							iorder.setStoreId(wid);
							

							iorder.setEmpId(empId);
							
							
							ResponseObject<Object> obj=this.m_orderService.import_third_orders(iorder);
							
							if(obj!=null&&obj.getCode().equalsIgnoreCase(ResponseCode.SUCCESS_CODE))
							{
								//插入成功
								ImportthirdMorder iorders=(ImportthirdMorder)obj.getData();
								if(iorders!=null)
								{
									String fileName = "orders_" + iorders.getOrders().size() + ".xls";
									// key = new String(key.getBytes("ISO-8859-1"), "utf-8");
									response.setContentType("application/vnd.ms-excel");
									response.setHeader("Content-disposition",
											"attachment;filename="
													+ new String(fileName.getBytes(), "utf-8"));
									File templeFile = new File(request.getSession()
											.getServletContext().getRealPath("/")
											+ this.morderthirdreturnmodeltemplets);
									os = response.getOutputStream();
									List<ImportthirdMorder> list1=new ArrayList<ImportthirdMorder>();
									list1.add(iorders);
									M_OrderUtil.exportthirdMordersMeitao(list1, templeFile, os);
									
								}
								else
								{
									return obj;
								}
								
							}
							else
							{
								return obj;
							}
							
							
							
							
							
							
						} catch (Exception e) {
							log.error("导入发生异常："+e.getMessage());
							return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
									"导入发生异常：" + e.getMessage());
						}
						finally {
							if (os != null) {
								try {
									os.flush();
									os.close();
								} catch (IOException e) {
									// ignore
								}
							}
						}
					}
					return generateResponseObject(ResponseCode.PARAMETER_ERROR, "文件不能为空");
					
				}
				
				
				
				
				// 20160512导入美淘旧系统模板,是指清单更表，包含发件人电话
				@RequestMapping(value = "/admin/m_order/import_old1_orders", method = { RequestMethod.POST })
				@ResponseBody
				public ResponseObject<Object> thirdimportOrderFromMeitaoExcel(
						HttpServletRequest request,
						HttpServletResponse response,
						@RequestParam(value = "uploadwid", required = false) String wid,
						@RequestParam(value = "uploadcid", required = false) String cid,
						@RequestParam(value = "uploadfile", required = false) MultipartFile file) {

					if(StringUtil.isEmpty(wid)||StringUtil.isEmpty(cid)||wid.equalsIgnoreCase("-1")||cid.equalsIgnoreCase("-1"))
					{
						return generateResponseObject(
								ResponseCode.PARAMETER_ERROR, "必须选择门店和渠道!");
					}
					String empId = StringUtil.obj2String(request.getSession().getAttribute(
							Constant.EMP_ID_SESSION_KEY));
					
					String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
					if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
					{
						String storeId = StringUtil.obj2String(request.getSession().getAttribute(
								Constant.EMP_STORE_ID_SESSION_KEY));
						String master=StringUtil.obj2String(request.getSession().getAttribute(
								Constant.EMP_STORE_MASTER_SESSION_KEY));
						
						if(!StringUtil.isEmpty(master)&&(master.equalsIgnoreCase("1")))//店长
						{
							if(!StringUtil.isEmpty(storeId)&&(storeId.equalsIgnoreCase(wid)))
							{
								
							}
							else
							{
								return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你不能够操作其它门店!");
							}
						}
						else
						{
							return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，只有高级管理员和店长能够进行此操作!");
						}
						
					}
					int getmode=0;//标志导入的模板
					if (file != null && file.getSize() > 0) {
						List<M_order> Orders = null;
						try {
							//kai  判定是不是excel表格
							String originalName = file.getOriginalFilename();
							if (!StringUtil.boolpicisgoodornot(originalName, defaultExcelFileType)) {
								return generateResponseObject(
										ResponseCode.CONSIGNEE_CARD_ERROR, "必须上传excel 2003表格,请重新尝试!");
							}
							
							//先检查门店和渠道是否存在
							Warehouse ware=this.warehouseDao.getById(wid);
							if(ware==null)
							{
								return generateResponseObject(
										ResponseCode.PARAMETER_ERROR, "你选择导入的门店不存在!");
							}
							Channel channel=this.channelDao.getChannelById(cid);
							if(channel==null)
							{
								return generateResponseObject(
										ResponseCode.PARAMETER_ERROR, "没有找到对应的渠道信息!");
							}
							else
							{
								//判断此渠道是否已授权给此门店
								String storelist=channel.getStoreList();
								if(StringUtil.isEmpty(storelist))
								{
									return generateResponseObject(
											ResponseCode.PARAMETER_ERROR, "此门店并没有在渠道中得到授权!");
								}
								else
								{
									boolean flag=false;
									String[] list= storelist.split(";");
									for(int i=0;i<list.length;i++)
									{
										if((!StringUtil.isEmpty(list[i]))&&(list[i].equalsIgnoreCase(wid)))
										{
											flag=true;
											break;
										}
									}
									if(flag==false)
									{
										return generateResponseObject(
												ResponseCode.PARAMETER_ERROR, "此门店并没有在渠道中得到授权!");
									}
									
								}
								
								
							}
							
							List<String> ihead1=M_OrderUtil.readexcelfirstrow(file.getInputStream());//读取导入的表头，用于判断导入的模板
							if(ihead1==null||ihead1.size()<1)
							{
								return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
										"表头数据不能为空!");
							}
							else
							{
								boolean flag=true;
								String header[]=ArrayConstant.THIRD_ORDER_HEADER_0;//比较模板
								if(header.length<=ihead1.size())//
								{
									
									for(int i=0;i<header.length;i++)
									{
										
										if(header[i].equalsIgnoreCase(ihead1.get(i).trim()))//
										{
											
										}
										else
										{
											flag=false;
											break;
										}
									}
								}
								if(flag==true)
								{
									getmode=1;//匹配成功第一个模板
								}
								else
								{
									flag=true;
									String header1[]=ArrayConstant.THIRD_ORDER_HEADER_1;//比较模板
									if(header1.length<=ihead1.size())//
									{
										
										for(int i=0;i<header1.length;i++)
										{
											
											if(header1[i].equalsIgnoreCase(ihead1.get(i).trim()))//
											{
												
											}
											else
											{
												flag=false;
												break;
											}
										}
									}
									if(flag==true)
									{
										getmode=2;//匹配成功第二个模板
									}
								}
								
								
							}
							
							if(getmode==0)
							{
								return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
										"匹配表头数据失败，请检查导入的表头是否与规定的表头一样!");
							}
							if(getmode==1)
							{
								//kai 20160429 获取美淘旧模板的数据，并在最后一列加入了状态
								Orders=M_OrderUtil.readOrderFromMeitaothird1Excel(file.getInputStream());
							}
							else if(getmode==2)
							{
								Orders=M_OrderUtil.readOrderFromMeitaothirdExcel(file.getInputStream());
							}
							else
							{
								return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
										"此模板数据尚未处理!");
							}
							
							
						} catch (OutOfMemoryError e) {
							log.error("内存不够", e);
							return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
									"内存不够,"+e.getMessage());
						} catch (Exception e) {
							log.error("读取数据出错,"+e.getMessage());
							String str = e.getMessage();// java.lang.RuntimeException:
							if ((str != null) && (!str.equalsIgnoreCase(""))) {
								str = str.replace("java.lang.RuntimeException:", "");
							}

							return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
									"读取数据出错原因:" + str);
						}
						OutputStream os = null;
						try {
							if(Orders==null)
							{
								return generateResponseObject(ResponseCode.PARAMETER_ERROR, "没有读取到数据!");
							}
							
							ImportthirdMorder iorder=new ImportthirdMorder();//导入的信息
							iorder.setOrders(Orders);
							iorder.setChannelId(cid);
							iorder.setStoreId(wid);
							

							iorder.setEmpId(empId);
							
							
							ResponseObject<Object> obj=this.m_orderService.import_third_orders(iorder);
							
							if(obj!=null&&obj.getCode().equalsIgnoreCase(ResponseCode.SUCCESS_CODE))
							{
								//插入成功
								ImportthirdMorder iorders=(ImportthirdMorder)obj.getData();
								if(iorders!=null)
								{
									if(getmode==1)
									{
										String fileName = "orders1_" + iorders.getOrders().size() + ".xls";
										// key = new String(key.getBytes("ISO-8859-1"), "utf-8");
										response.setContentType("application/vnd.ms-excel");
										response.setHeader("Content-disposition",
												"attachment;filename="
														+ new String(fileName.getBytes(), "utf-8"));
										File templeFile = new File(request.getSession()
												.getServletContext().getRealPath("/")
												+ this.morderthirdreturnmodeltemplets);
										os = response.getOutputStream();
										List<ImportthirdMorder> list1=new ArrayList<ImportthirdMorder>();
										list1.add(iorders);
										//M_OrderUtil.exportthirdMordersMeitao(list1, templeFile, os);
										M_OrderUtil.exportthirdMordersMeitao1(list1, templeFile, os);
									}
									else if(getmode==2)
									{
										String fileName = "orders2_" + iorders.getOrders().size() + ".xls";
										// key = new String(key.getBytes("ISO-8859-1"), "utf-8");
										response.setContentType("application/vnd.ms-excel");
										response.setHeader("Content-disposition",
												"attachment;filename="
														+ new String(fileName.getBytes(), "utf-8"));
										File templeFile = new File(request.getSession()
												.getServletContext().getRealPath("/")
												+ this.morderthirdreturnmodel2templets);
										os = response.getOutputStream();
										List<ImportthirdMorder> list1=new ArrayList<ImportthirdMorder>();
										list1.add(iorders);
										M_OrderUtil.exportthirdMordersMeitao(list1, templeFile, os);
										//M_OrderUtil.exportthirdMordersMeitao1(list1, templeFile, os);
									}
									else
									{
										return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
												"导入成功，但是没有导出模板");
									}
									
								}
								else
								{
									return obj;
								}
								
							}
							else
							{
								return obj;
							}
							
							
							
							
							
							
						} catch (Exception e) {
							log.error("导入发生异常："+e.getMessage());
							return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
									"导入发生异常：" + e.getMessage());
						}
						finally {
							if (os != null) {
								try {
									os.flush();
									os.close();
								} catch (IOException e) {
									// ignore
								}
							}
						}
					}
					return generateResponseObject(ResponseCode.PARAMETER_ERROR, "文件不能为空");
					
				}
				
				
				
				
				
				//上传第三运单的美淘老系统模板下载1 
				@RequestMapping(value = "/admin/m_order/down_third_old_model", method = { RequestMethod.GET })
				public void getuploadoldmeitaothirdmodel(HttpServletRequest request,
						HttpServletResponse response) {
					InputStream input = null;
					ServletOutputStream os=null;
					try {
						response.setContentType("application/vnd.ms-excel");
						response.setHeader("Content-disposition", "attachment;filename="
								+ new String("upload_third_old_example.xls".getBytes(),
										"utf-8"));
						input = request
								.getSession()
								.getServletContext()
								.getResourceAsStream(
										this.morderthirdoldmodelexample);
						os = response.getOutputStream();
						byte[] buffer = new byte[1024];
						int n = 0;
						while ((n = input.read(buffer)) > 0) {
							os.write(buffer, 0, n);
						}
						buffer=null;
						os.flush();
					} catch (Exception e) {
						log.error("下载文件失败", e);
					} finally {
						if (input != null) {
							try {
								input.close();
							} catch (IOException e) {
								// ignore
							}
						}
						if (os != null) {
							try {
								os.flush();
								os.close();
							} catch (IOException e) {
								// ignore
							}
						}
					}
				}
				
				//上传第三运单的美淘老系统模板下载2 
				@RequestMapping(value = "/admin/m_order/down_third_old_model2", method = { RequestMethod.GET })
				public void getuploadoldmeitaothirdmodel2(HttpServletRequest request,
						HttpServletResponse response) {
					InputStream input = null;
					ServletOutputStream os=null;
					try {
						response.setContentType("application/vnd.ms-excel");
						response.setHeader("Content-disposition", "attachment;filename="
								+ new String("upload_third_old_example2.xls".getBytes(),
										"utf-8"));
						
						input = request
								.getSession()
								.getServletContext()
								.getResourceAsStream(
										this.morderthirdoldmodel2example);
						os = response.getOutputStream();
						byte[] buffer = new byte[1024];
						int n = 0;
						while ((n = input.read(buffer)) > 0) {
							os.write(buffer, 0, n);
						}
						buffer=null;
						os.flush();
					} catch (Exception e) {
						log.error("下载文件失败", e);
					} finally {
						if (input != null) {
							try {
								input.close();
							} catch (IOException e) {
								// ignore
							}
						}
						if (os != null) {
							try {
								os.flush();
								os.close();
							} catch (IOException e) {
								// ignore
							}
						}
					}
				}
				
		
		
				
				
				@RequestMapping(value = "/admin/m_order/get_by_orderId", method = { RequestMethod.POST, RequestMethod.GET})
				@ResponseBody
				public ResponseObject<Object> getMorderbyorderId(
						HttpServletRequest request,
						HttpServletResponse response,
						@RequestParam(value = "orderId", required = false) String orderId,
						@RequestParam(value = "storeId", required = false) String storeId) {
					
					
					String storeId0 = StringUtil.obj2String(request.getSession().getAttribute(
							Constant.EMP_STORE_ID_SESSION_KEY));
					if(StringUtil.isEmpty(storeId0))
					{
						return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请先登陆或登陆已失效!");
					}
					
					if(StringUtil.isEmpty(orderId))
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"运单号不能为空!");
					}
					if(StringUtil.isEmpty(storeId)||storeId.equalsIgnoreCase("-1"))
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"所属门店不能为空！");
					}
					try{
						M_order order=this.m_orderDao.getByOrderId(orderId, storeId);
					
						if(order==null)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取运单失败，请检查运单号是否正确或是否属于此门店!");
						}
						if(!StringUtil.isEmpty(order.getI_state())&&(order.getI_state().equalsIgnoreCase("1")))//运单已经入过库了
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"此运单已经入库！");
						}
						//判断状态
						try{
							int a=Integer.parseInt(order.getState());
							String str="状态异常!";
							if(a<2)
							{
								if(a==-10)
								{
									str="在线置单";
								}
								else if(a==0)
								{
									str="运单作废";
								}
								else if(a==1)
								{
									str="包裹异常";
								}
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"运单状态不正确，当前状态是："+str);
							}
							
						}catch(Exception e)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取运单状态发生异常!");
						}
						
						ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
						obj.setData(order);
						return obj;
					}
					catch(Exception e)
					{
						return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"获取运单发生异常!");
					}
				}
				
				
				
				
				//入库操作，如果入库，将
				@RequestMapping(value = "/admin/m_order/set_istate_orderId", method = { RequestMethod.POST, RequestMethod.GET})
				@ResponseBody
				public ResponseObject<Object> setmorderistate(
						HttpServletRequest request,
						HttpServletResponse response,
						@RequestParam(value = "orderId", required = false) String orderId,//运单id
						@RequestParam(value = "storeId", required = false) String storeId,//所属门店id
						@RequestParam(value = "state", required = false) String state,//修改状态
						@RequestParam(value = "i_weight", required = false) String i_weight//入库重量
						) {
					
					
					String empId = StringUtil.obj2String(request.getSession().getAttribute(
							Constant.EMP_ID_SESSION_KEY));
					if(StringUtil.isEmpty(empId))
					{
						return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请先登陆或登陆已失效!");
					}
					String i_storeId = StringUtil.obj2String(request.getSession().getAttribute(
							Constant.EMP_STORE_ID_SESSION_KEY));
					if(StringUtil.isEmpty(i_storeId))
					{
						return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请先登陆或登陆已失效!");
					}
					
					
					if(StringUtil.isEmpty(orderId))
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"运单号不能为空!");
					}
					if(StringUtil.isEmpty(storeId)||storeId.equalsIgnoreCase("-1"))
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"所属门店不能为空！");
					}
					if(StringUtil.isEmpty(state)||storeId.equalsIgnoreCase("-1"))
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"状态值不能为空！");
					}
					else
					{
						try{
							int a=Integer.parseInt(state);
							if(a<3||a>9)
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"状态值必须大于已揽收并在已有状态范围内");
							}
						}catch(Exception e){
							return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"状态值不正确");
						}
						
						
						try{
							i_weight=i_weight.trim();
							double a=Double.parseDouble(i_weight);//
							if(a<=0)
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"入库重量不能小于等于0");
							}
						}catch(Exception e){
							return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"入库重量不正确");
						}
					}
					
					try{
						String date = DateUtil.date2String(new Date());
						int k=this.m_orderDao.modifyinputstate(state, "1", empId, date,date, orderId,i_storeId, storeId,i_weight);
						if(k>0)
						{
							//修改成功要插入路由状态
							
							//String orderid=this.m_orderDao.getOrderIdbyId(id);
							//String date = DateUtil.date2String(new Date());
							Route route = new Route();
							route.setDate(date);
							route.setOrderId(orderId);
							route.setRemark("入库修改状态");
							
							route.setState(OrderUtil.transformerState(0,state));
							this.routeDao.insertRoute(route);
							
							return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
						}
						else
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"修改失败!");
						}
						
					}
					catch(Exception e)
					{
						return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"获取运单发生异常!");
					}
				}
				
				
				
				//搜索运单
				@RequestMapping(value = "/admin/m_order/search_input",method = { RequestMethod.GET, RequestMethod.POST})
				@ResponseBody
				public ResponseObject<PageSplit<M_order>> searchbyinputstate(
						HttpServletRequest request,
						@RequestParam(value = "oid", required = false) String oid,//运单号
						@RequestParam(value = "wid", required = false) String wid,//所属门店id
						@RequestParam(value = "cid", required = false, defaultValue = "") String cid,//渠道id
						@RequestParam(value = "state", required = false) String state,//状态
						@RequestParam(value = "payway", required = false) String payway,//支付方式
						@RequestParam(value = "sdate", required = false) String sdate,//创建的起始时间
						@RequestParam(value = "edate", required = false) String edate,//创建的结束时间
						@RequestParam(value = "inputstore", required = false) String inputstore,//入库所在门店
						
						@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
						@RequestParam(value = "rows", required = false, defaultValue = "10") int rows
						) {
					try {
						
					
						
						if(!StringUtil.isEmpty(wid)&&(wid.equalsIgnoreCase("-1")))
						{
							wid=null;
						}
						if(!StringUtil.isEmpty(cid)&&(cid.equalsIgnoreCase("-1")))
						{
							cid=null;
						}
						if(!StringUtil.isEmpty(state)&&(state.equalsIgnoreCase("-1")))
						{
							state=null;
						}
						
						if(!StringUtil.isEmpty(payway)&&(payway.equalsIgnoreCase("-1")))
						{
							payway=null;
						}
					
						String empId = StringUtil.obj2String(request.getSession().getAttribute(
								Constant.EMP_ID_SESSION_KEY));
						if(StringUtil.isEmpty(empId))
						{
							return new ResponseObject<PageSplit<M_order>>(ResponseCode.NEED_LOGIN,"请先登陆或登陆已失效!");
						}
						
						String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
						
						if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
						{
							empId=null;//查找全部
							if(StringUtil.isEmpty(inputstore)||inputstore.equalsIgnoreCase("-1"))
							{
								inputstore=null;
							}
							
						}
						else
						{
							inputstore = StringUtil.obj2String(request.getSession().getAttribute(
									Constant.EMP_STORE_ID_SESSION_KEY));//不是高级管理员，这里的输入门店只能够是查找自己的入库门店
							
						}
						
						
						if (StringUtil.isEmpty(sdate)
								|| !UserUtil.validateExportDate(sdate)) {
							sdate = "";
						} else {
							sdate = UserUtil.transformerDateString(sdate, " 00:00:00");
						}

						if (StringUtil.isEmpty(edate)
								|| !UserUtil.validateExportDate(edate)) {
							edate = "";
						} else {
							edate = UserUtil.transformerDateString(edate, " 23:59:59");
						}
						
						

						oid = StringUtil.isEmpty(oid) ? null : oid;
						
						//String column = OrderUtil.getSearchColumnByType(type);
						state = OrderUtil.dealState(state);
						pageIndex = Math.max(pageIndex, 1);
						
						return this.m_orderService.searchMordersInput(oid,inputstore, wid, cid, state, "1", payway, sdate, edate,empId, rows, pageIndex);
						//return this.m_orderService.searchMorders(oid, sod, god, flyno, storeid, cid, userinfo, commudityinfo, state, type,payway, sdate, edate, psdate, pedate,cardinfo,payornot, rows, pageIndex);
						//return new ResponseObject<PageSplit<Order>>();
						//return this.orderService.searchOrdersByKeys(oid, null, key, column,
						//		sdate, edate, state,typekey,storeid, defaultPageSize, pageIndex);
					} catch (Exception e) {
						log.error("查询运单失败", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "查询失败");
					}
				}
				
				
				
				
				//下载入库的运单
				@RequestMapping(value = "/admin/m_order/search_download_input",method = { RequestMethod.GET, RequestMethod.POST})
				@ResponseBody
				public ResponseObject<PageSplit<M_order>> searchbyinputstatefordownloas(
						HttpServletRequest request,
						HttpServletResponse response,
						@RequestParam(value = "oid", required = false) String oid,//运单号
						@RequestParam(value = "wid", required = false) String wid,//所属门店id
						@RequestParam(value = "cid", required = false, defaultValue = "") String cid,//渠道id
						@RequestParam(value = "state", required = false) String state,//状态
						@RequestParam(value = "payway", required = false) String payway,//支付方式
						@RequestParam(value = "sdate", required = false) String sdate,//创建的起始时间
						@RequestParam(value = "edate", required = false) String edate,//创建的结束时间
						@RequestParam(value = "inputstore", required = false) String inputstore,//入库所在门店
						
						@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
						@RequestParam(value = "rows", required = false, defaultValue = "10") int rows
						) {
					try {
						
						pageIndex=1;
						rows=0x7fffffff;
						
						if(!StringUtil.isEmpty(wid)&&(wid.equalsIgnoreCase("-1")))
						{
							wid=null;
						}
						if(!StringUtil.isEmpty(cid)&&(cid.equalsIgnoreCase("-1")))
						{
							cid=null;
						}
						if(!StringUtil.isEmpty(state)&&(state.equalsIgnoreCase("-1")))
						{
							state=null;
						}
						
						if(!StringUtil.isEmpty(payway)&&(payway.equalsIgnoreCase("-1")))
						{
							payway=null;
						}
					
						String empId = StringUtil.obj2String(request.getSession().getAttribute(
								Constant.EMP_ID_SESSION_KEY));
						if(StringUtil.isEmpty(empId))
						{
							return new ResponseObject<PageSplit<M_order>>(ResponseCode.NEED_LOGIN,"请先登陆或登陆已失效!");
						}
						
						String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
						
						if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
						{
							empId=null;//查找全部
							if(StringUtil.isEmpty(inputstore)||inputstore.equalsIgnoreCase("-1"))
							{
								inputstore=null;
							}
							
						}
						else
						{
							inputstore = StringUtil.obj2String(request.getSession().getAttribute(
									Constant.EMP_STORE_ID_SESSION_KEY));//不是高级管理员，这里的输入门店只能够是查找自己的入库门店
							
						}
						
						
						if (StringUtil.isEmpty(sdate)
								|| !UserUtil.validateExportDate(sdate)) {
							sdate = "";
						} else {
							sdate = UserUtil.transformerDateString(sdate, " 00:00:00");
						}

						if (StringUtil.isEmpty(edate)
								|| !UserUtil.validateExportDate(edate)) {
							edate = "";
						} else {
							edate = UserUtil.transformerDateString(edate, " 23:59:59");
						}
						
						

						oid = StringUtil.isEmpty(oid) ? null : oid;
						
						//String column = OrderUtil.getSearchColumnByType(type);
						state = OrderUtil.dealState(state);
						pageIndex = Math.max(pageIndex, 1);
						
						ResponseObject<PageSplit<M_order>> obj= this.m_orderService.searchMordersInput(oid,inputstore, wid, cid, state, "1", payway, sdate, edate,empId, rows, pageIndex);
						OutputStream os = null;
						try{
							if((obj!=null)&&((obj.getData()!=null))&&(ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj.getCode()))&&obj.getData().getDatas()!=null)
							{
								
								
								
								List<M_order> Orders=obj.getData().getDatas();
								String lowest_weight_value_flag =this.globalargsDao.getcontentbyflag("lowest_weight_value_flag");//最低重量
								String jw_commodity_rate =this.globalargsDao.getcontentbyflag("jw_commodity_rate");//重量进位值
								try{
									double lweight=Double.parseDouble(lowest_weight_value_flag);
									double jwrate=Double.parseDouble(jw_commodity_rate);
									if(jwrate>0&&jwrate<1&&lweight>0)
									{
										for(M_order order:Orders)
										{
											try{
												double w=Double.parseDouble(order.getI_weight());
												if(w>0)
												{
													if(w<=lweight)
													{
														order.setI_jwweight(lowest_weight_value_flag);
													}
													else
													{
														int aa=(int)w;
														if(w-aa>jwrate)
														{
															aa=aa+1;
															order.setI_jwweight(String.valueOf(aa));
														}
														else
														{
															order.setI_jwweight(String.valueOf(aa));
														}
													}
												}
											}catch(Exception e){
												
											}
										}
									}
								}
								catch(Exception e)
								{
									
								}
								
								
								
								String fileName = "download_orders_" + Orders.size() + ".xls";
								// key = new String(key.getBytes("ISO-8859-1"), "utf-8");
								response.setContentType("application/vnd.ms-excel");
								response.setHeader("Content-disposition",
										"attachment;filename="
												+ new String(fileName.getBytes(), "utf-8"));
								// orders = this.orderService.getExportOrders(sdate, edate);
	
								/*File templeFile = new File(request.getSession()
										.getServletContext().getRealPath("/")
										+ this.orderOutputToWeiyiTempletsFile);*/
								File templeFile = new File(request.getSession()
										.getServletContext().getRealPath("/")
										+ this.inputorderdownloadtemplets);;
								
								os = response.getOutputStream();
								
								// 导出数据唯一快递单子
								
								
								M_OrderUtil.exportInputMOrderToMeitao(Orders, templeFile, os);
								
							
							}
							else
							{
								return obj;
							}
						} catch (Exception e) {
							log.error("修改数据库失败", e);
							return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
									"提交失败：" + e.getMessage());
						}finally {
							// orders.clear();
							// 6.无论成功与否关闭相应的流
							try {
								
								if (os != null) {
									os.flush();
									os.close();
								}
								
							} catch (IOException e) {
								System.err.println(e.getMessage());
							}

						}
						return generateResponseObject(ResponseCode.SUCCESS_CODE);
						//return this.m_orderService.searchMorders(oid, sod, god, flyno, storeid, cid, userinfo, commudityinfo, state, type,payway, sdate, edate, psdate, pedate,cardinfo,payornot, rows, pageIndex);
						//return new ResponseObject<PageSplit<Order>>();
						//return this.orderService.searchOrdersByKeys(oid, null, key, column,
						//		sdate, edate, state,typekey,storeid, defaultPageSize, pageIndex);
					} catch (Exception e) {
						log.error("查询运单失败", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "查询失败");
					}
				}
				
				
			//根据单号并获取汇通单号来打印汇通派送单
				@RequestMapping(value = "/admin/m_order/print_huitong_orderId", method = { RequestMethod.POST, RequestMethod.GET})
				@ResponseBody
				public ResponseObject<Object> getprintMorderbyorderId(
						HttpServletRequest request,
						HttpServletResponse response,
						@RequestParam(value = "orderId", required = false) String orderId,
						@RequestParam(value = "date", required = false) String date) //表示打印的日期
						
						{
					
					String storeId = StringUtil.obj2String(request.getSession().getAttribute(
							Constant.EMP_STORE_ID_SESSION_KEY));
					if(StringUtil.isEmpty(storeId))
					{
						return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请先登陆或登陆已失效!");
					}
					
					String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
					if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
					{
						storeId=null;
					}
					else
					{
						
					}
					
					
					if(StringUtil.isEmpty(orderId))
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"运单号不能为空!");
					}
					if(StringUtil.isEmpty(date)||date.equalsIgnoreCase("-1"))
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"打印时间不能为空！");
					}
					
					try{
						M_order order=this.m_orderDao.getByOrderId(orderId, storeId);
					
						if(order==null)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取运单失败，请检查运单号是否正确或是否属于此门店!");
						}
						//获取当前时间
						String modifyDate=DateUtil.date2String(new Date());
						if(!StringUtil.isEmpty(date))
						{
							String str="";
						    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
						    str = df.format(new Date());
						    str=date+" "+str;
							order.setNowtime(str);
						}
						else
						{
							order.setNowtime(modifyDate);
						}
						
						//原来就保存有通单号
						if(!StringUtil.isEmpty(order.getHuitongNo()))
						{
							ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.HUITONG_ORDERID_REP);
							obj.setData(order);
							return obj;
						}

						//获取汇通单号
						HuitongNumber seano=this.huitongNumberDao.getone();
						if((seano==null)||(StringUtil.isEmpty(seano.getOrderId())))
						{
							if(seano!=null)
							{
								seano.setState("1");
								seano.setModifyDate(modifyDate);
								int a=this.huitongNumberDao.updatestate(seano);
								//int a=seaNumberDao.updatestate(seano);
							}
							throw new Exception("获取汇通单号失败!");
						}else
						{
							order.setHuitongNo(seano.getOrderId());
						
							seano.setState("1");
							seano.setModifyDate(modifyDate);
							int a=this.huitongNumberDao.updatestate(seano);
							if(a==0)
							{
								throw new Exception("修改汇通单状态失败!");
							}
							
							int aa=this.m_orderDao.updateHuitongNo(order.getId(), seano.getOrderId(), modifyDate);
							if(aa==0)
							{
								throw new Exception("修改汇通单号到运单失败");
							}
						}
						
						//匹配身份证
					
						
						ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
						obj.setData(order);
						return obj;
					}
					catch(Exception e)
					{
						return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"获取运单发生异常!");
					}
				}
				
				
				
				
				//根据上传的运单号或汇通单号，导出对应的运单信息

				@RequestMapping(value = "/admin/m_order/import_table_huitong", method = { RequestMethod.POST })
				@ResponseBody
				public ResponseObject<Object> importOrderFromhuitongExcel(
						HttpServletRequest request,
						HttpServletResponse response, 
						//@RequestParam(value = "inputmode", required = false) String inputmode,//导入的运单类型
						@RequestParam(value = "filemeitao", required = false) MultipartFile file) {

				
					
					
					String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
					String storeId=null;
					if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
					{
					
					}
					else
					{
						//String master = (String)request.getSession().getAttribute(Constant.EMP_STORE_MASTER_SESSION_KEY);
						storeId=(String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
						if(StringUtil.isEmpty(storeId))
						{
							return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"登陆超时或没有登陆，请登陆后操作!");
						}
						/*if((master!=null)&&(master.equalsIgnoreCase("1")))
						{
							
						}
						else
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"对不起，只有高级管理员或店长能够导出模板信息!");
						}*/
					}

					
				/*	String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
					if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
					{
						return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权进行此操作!");
						
					}*/
					// 图片流
					InputStream imgInputStream = null;
					OutputStream os = null;
					OutputStream os1 = null;
					List<ExportMorder> Orders = null;
					if (file != null && file.getSize() > 0) {
						
						try {
							//kai 20160315判定是不是符合excel表格
							String originalName = file.getOriginalFilename();
							if (!StringUtil.boolpicisgoodornot(originalName, defaultExcelFileType)) {
								return new ResponseObject<Object>(
										ResponseCode.CONSIGNEE_CARD_ERROR, "必须上传excel 2003表格,请重新尝试!");
							}
							Orders=M_OrderUtil.readorderidExcel(file
									.getInputStream());
							//Orders = CardidManageControllerUtil.readgetCardidFromMeitaoExcel(file
							//		.getInputStream());
							
						} catch (OutOfMemoryError e) {
							log.error("内存不够", e);
							return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
									"内存不够");
						} catch (Exception e) {
							log.error("读取数据出错", e);
							String str = e.getMessage();// java.lang.RuntimeException:
							if ((str != null) && (!str.equalsIgnoreCase(""))) {
								str = str.replace("java.lang.RuntimeException:", "");
							}

							return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
									"读取数据出错原因:" + str);
						}

						try {
							
							
							//根据运单号类型获取运单信息
							List<M_order> exportMorder = new ArrayList<M_order>();
							
							
							
							
							
							for(ExportMorder eorder:Orders)
							{
								
								if(!StringUtil.isEmpty(eorder.getOrderId()))
								{
									M_order order=this.m_orderDao.getByOrderId(eorder.getOrderId(), storeId);
									if(order==null)
									{
										eorder.setOrderResult("获取运单信息失败，请检查单号是否存是或是否属于本门店！");
									}
									else
									{
										eorder.setOrder(order);
									}
								}
								else
								{
									eorder.setOrderResult("失败，运单号不能为空");
								}
							}
							
							
							
							//导出单号信息				
							if (Orders != null && Orders.size() >0) {	
								
								
							String fileName = "success_result_"
								+ Orders.size() + ".xls";
						
							response.setContentType("application/vnd.ms-excel");
							response.setHeader("Content-disposition",
									"attachment;filename="
											+ new String(fileName.getBytes(),
													"utf-8"));
							
							File templeFile = new File(request.getSession()
									.getServletContext().getRealPath("/")
									+ this.morderdownhuitongorderstemplets);
							os = response.getOutputStream();
							M_OrderUtil.exporthuitongorders(Orders, templeFile, os);
							
							
							
							
							}
							else
							{
								return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
										"返回数据为空！");
							}
							
						
							return generateResponseObject(ResponseCode.SUCCESS_CODE);
						} catch (Exception e) {
							log.error("修改数据库失败", e);
							return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
									"提交失败：" + e.getMessage());
						}finally {
							// orders.clear();
							// 6.无论成功与否关闭相应的流
							try {
								if (imgInputStream != null) {
									imgInputStream.close();
								}
								if (os != null) {
									os.flush();
									os.close();
								}
								if (os1 != null) {
									os1.flush();
									os1.close();
								}
							} catch (IOException e) {
								System.err.println(e.getMessage());
							}

						}
					}
					return generateResponseObject(ResponseCode.PARAMETER_ERROR, "文件不能为空");
				}
				
				
				
				//在运单下载中，进行的导入运单号然后导出运单信息的操作

				@RequestMapping(value = "/admin/m_order/import_morders_meitao", method = { RequestMethod.POST })
				@ResponseBody
				public ResponseObject<Object> importOrderIdFromMeitaoExcel(
						HttpServletRequest request,
						HttpServletResponse response, 
						@RequestParam(value = "inputfileorderId", required = false) MultipartFile file) {

				
					
					
					String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
					String storeId=null;
					if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
					{
					
					}
					else
					{
						String master = (String)request.getSession().getAttribute(Constant.EMP_STORE_MASTER_SESSION_KEY);
						storeId=(String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
						if((master!=null)&&(master.equalsIgnoreCase("1")))
						{
							
						}
						else
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"对不起，只有高级管理员或店长能够导出运单信息!");
						}
					}

					
				/*	String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
					if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
					{
						return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权进行此操作!");
						
					}*/
					// 图片流
					InputStream imgInputStream = null;
					OutputStream os = null;
					OutputStream os1 = null;
					List<ExportMorder> Orders = null;
					if (file != null && file.getSize() > 0) {
						
						try {
							//kai 20160315判定是不是符合excel表格
							String originalName = file.getOriginalFilename();
							if (!StringUtil.boolpicisgoodornot(originalName, defaultExcelFileType)) {
								return new ResponseObject<Object>(
										ResponseCode.CONSIGNEE_CARD_ERROR, "必须上传excel 2003表格,请重新尝试!");
							}
							Orders=M_OrderUtil.readorderidExcel(file
									.getInputStream());
							//Orders = CardidManageControllerUtil.readgetCardidFromMeitaoExcel(file
							//		.getInputStream());
							
						} catch (OutOfMemoryError e) {
							log.error("内存不够", e);
							return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
									"内存不够");
						} catch (Exception e) {
							log.error("读取数据出错", e);
							String str = e.getMessage();// java.lang.RuntimeException:
							if ((str != null) && (!str.equalsIgnoreCase(""))) {
								str = str.replace("java.lang.RuntimeException:", "");
							}

							return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
									"读取数据出错原因:" + str);
						}

						try {
							
							for(ExportMorder eorder:Orders)
							{
								
								if(!StringUtil.isEmpty(eorder.getOrderId()))
								{
									M_order order=this.m_orderDao.getByOrderId(eorder.getOrderId(), storeId);
									if(order==null)
									{
										eorder.setOrderResult("获取运单信息失败，请检查单号是否存是或是否属于本门店！");
									}
									else
									{
										eorder.setOrder(order);
										eorder.setOrderResult("成功!");
									}
								}
								else
								{
									eorder.setOrderResult("失败，运单号不能为空");
								}
							}
							
							
							//ResponseObject<Object> obj=null;
							//导出信息
						
							
							String fileName="";
						
							
							
							
								if (Orders != null && Orders.size() >0) {	
									
									
									
									fileName = "download_orders_" + Orders.size() + ".xls";
									// key = new String(key.getBytes("ISO-8859-1"), "utf-8");
									response.setContentType("application/vnd.ms-excel");
									response.setHeader("Content-disposition",
											"attachment;filename="
													+ new String(fileName.getBytes(), "utf-8"));
									// orders = this.orderService.getExportOrders(sdate, edate);

									/*File templeFile = new File(request.getSession()
											.getServletContext().getRealPath("/")
											+ this.orderOutputToWeiyiTempletsFile);*/
									File templeFile = new File(request.getSession()
											.getServletContext().getRealPath("/")
											+ this.orderOutputToMeitaonewTempletsFile);;
									
									os = response.getOutputStream();
									
									// 导出数据唯一快递单子
									
									
									M_OrderUtil.exportMOrderToMeitao(Orders, templeFile, os);
									
								}
									
								
							
						
							return generateResponseObject(ResponseCode.SUCCESS_CODE);
						} catch (Exception e) {
							log.error("修改数据库失败", e);
							return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
									"提交失败：" + e.getMessage());
						}finally {
							// orders.clear();
							// 6.无论成功与否关闭相应的流
							try {
								if (imgInputStream != null) {
									imgInputStream.close();
								}
								if (os != null) {
									os.flush();
									os.close();
								}
								if (os1 != null) {
									os1.flush();
									os1.close();
								}
							} catch (IOException e) {
								System.err.println(e.getMessage());
							}

						}
					}
					return generateResponseObject(ResponseCode.PARAMETER_ERROR, "文件不能为空");
				}
				
				
				
				
				
				
				
				
				
				
				
				
//美淘A渠道模板下载
				@RequestMapping(value = "/admin/m_order/import_morders_A_meitao", method = { RequestMethod.POST })
				@ResponseBody
				public ResponseObject<Object> importOrderIdFromAMeitaoExcel(
						HttpServletRequest request,
						HttpServletResponse response, 
						@RequestParam(value = "model", required = false) String model,//模板类型，0是奶粉渠道，1是普货渠道
						@RequestParam(value = "inputfileorderId", required = false) MultipartFile file) {

				
					
					
					String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
					String storeId=null;
					if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
					{
					
					}
					else
					{
						String master = (String)request.getSession().getAttribute(Constant.EMP_STORE_MASTER_SESSION_KEY);
						storeId=(String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
						if((master!=null)&&(master.equalsIgnoreCase("1")))
						{
							
						}
						else
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"对不起，只有高级管理员或店长能够导出运单信息!");
						}
					}

					if(StringUtil.isEmpty(model))
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数（下载类型）不能为空");
					}
					else
					{
						if(model.equalsIgnoreCase("0")||model.equalsIgnoreCase("1"))
						{}
						else
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数（下载类型）错误!");
						}
					}
					
				/*	String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
					if((supperadmin==null)||(!supperadmin.equalsIgnoreCase("1")))
					{
						return generateResponseObject(ResponseCode.EMPLOYEE_STORE_NAME_ERROR, "对不起，你无权进行此操作!");
						
					}*/
					// 图片流
					InputStream imgInputStream = null;
					OutputStream os = null;
					OutputStream os1 = null;
					List<ExportMorder> Orders = null;
					if (file != null && file.getSize() > 0) {
						
						try {
							//kai 20160315判定是不是符合excel表格
							String originalName = file.getOriginalFilename();
							if (!StringUtil.boolpicisgoodornot(originalName, defaultExcelFileType)) {
								return new ResponseObject<Object>(
										ResponseCode.CONSIGNEE_CARD_ERROR, "必须上传excel 2003表格,请重新尝试!");
							}
							Orders=M_OrderUtil.readorderidExcel(file
									.getInputStream());
							//Orders = CardidManageControllerUtil.readgetCardidFromMeitaoExcel(file
							//		.getInputStream());
							
						} catch (OutOfMemoryError e) {
							log.error("内存不够", e);
							return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
									"内存不够");
						} catch (Exception e) {
							log.error("读取数据出错", e);
							String str = e.getMessage();// java.lang.RuntimeException:
							if ((str != null) && (!str.equalsIgnoreCase(""))) {
								str = str.replace("java.lang.RuntimeException:", "");
							}

							return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
									"读取数据出错原因:" + str);
						}

						try {
							
							for(ExportMorder eorder:Orders)
							{
								
								if(!StringUtil.isEmpty(eorder.getOrderId()))
								{
									M_order order=this.m_orderDao.getByOrderId(eorder.getOrderId(), storeId);
									if(order==null)
									{
										eorder.setOrderResult("获取运单信息失败，请检查单号是否存是或是否属于本门店！");
									}
									else
									{
										eorder.setOrder(order);
										eorder.setOrderResult("成功!");
									}
								}
								else
								{
									eorder.setOrderResult("失败，运单号不能为空");
								}
							}
							
							
							//ResponseObject<Object> obj=null;
							//导出信息
						
							
							String fileName="";
						
							
							
							
								if (Orders != null && Orders.size() >0) {	
									
									
									
									fileName = "download_orders_" + Orders.size() + ".xls";
									// key = new String(key.getBytes("ISO-8859-1"), "utf-8");
									response.setContentType("application/vnd.ms-excel");
									response.setHeader("Content-disposition",
											"attachment;filename="
													+ new String(fileName.getBytes(), "utf-8"));
									// orders = this.orderService.getExportOrders(sdate, edate);

									/*File templeFile = new File(request.getSession()
											.getServletContext().getRealPath("/")
											+ this.orderOutputToWeiyiTempletsFile);*/
									File templeFile = new File(request.getSession()
											.getServletContext().getRealPath("/")
											+ this.orderOutputToMeitaonewTempletsFile);;
									
									os = response.getOutputStream();
									
									// 导出数据唯一快递单子
									
									String rate=this.globalargsDao.getcontentbyflag("cur_usa_cn");
									if(!StringUtil.isEmpty(rate))
									{
										try{
											double rate1=Double.parseDouble(rate);
											if(rate1>0)//保存汇率
											{
												for(ExportMorder eorder:Orders)
												{
													eorder.setRate(rate1);
												}
											}
										}catch(Exception e){}
									}
									M_OrderUtil.exportMOrderToAnf_Meitao(Orders, templeFile, os);
									
								}
									
								
							
						
							return generateResponseObject(ResponseCode.SUCCESS_CODE);
						} catch (Exception e) {
							log.error("修改数据库失败", e);
							return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
									"提交失败：" + e.getMessage());
						}finally {
							// orders.clear();
							// 6.无论成功与否关闭相应的流
							try {
								if (imgInputStream != null) {
									imgInputStream.close();
								}
								if (os != null) {
									os.flush();
									os.close();
								}
								if (os1 != null) {
									os1.flush();
									os1.close();
								}
							} catch (IOException e) {
								System.err.println(e.getMessage());
							}

						}
					}
					return generateResponseObject(ResponseCode.PARAMETER_ERROR, "文件不能为空");
				}
				
				
				
				
				
				//发送的揽收通知
				@RequestMapping(value = "/admin/m_order/send_rev_message", method = { RequestMethod.POST, RequestMethod.GET})
				@ResponseBody
				public ResponseObject<Object> sendrevmessage(
						HttpServletRequest request,
						@RequestParam(value = "s_date", required = false) String s_date,
						@RequestParam(value = "e_date", required = false) String e_date,
						@RequestParam(value = "resendmessage", required = false) String resendmessage
						) //
						{
							
							String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
							if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
							{
								
								
							}else
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"对不起，只有高级管理员有此权限");
							}
							
							//当前时间
							String empName = (String)request.getSession().getAttribute(Constant.EMP_ACCOUNT_SESSION_KEY);//操作人名称
							String newdate = DateUtil.date2String(new Date());
							try{
								int k=DateUtil.daysBetween(s_date, newdate);
								if(k>2)//
								{
									return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"起始时间不能起过2天");
								}
								else if(k<0)
								{
									return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"起始时间设置错误！");
								}
									
							}catch(Exception e)
							{
								return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"计算时间出错异常");
							}
							
							
							
							if(StringUtil.isEmpty(s_date))
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"必须选择起始时间");
							}
							if(StringUtil.isEmpty(e_date))
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"必须选择结束时间");
							}
							s_date = UserUtil.transformerDateString(s_date, " 00:00:00");
							e_date = UserUtil.transformerDateString(e_date, " 23:59:59");
							try{
								return this.m_orderService.send_rev_message(resendmessage,s_date, e_date,"操作人:"+empName+",手动发送揽收短信");
							}catch(Exception e)
							{
								return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"操作发生异常"+e.getMessage());
							}
							
						}
				
				
				//会员在用户中心上传已有运单号的身份证
				@RequestMapping(value="/user/upload_cardid", produces="text/plain;charset=UTF-8",method={RequestMethod.POST,RequestMethod.GET})
				@ResponseBody
				public String uploadcardidbyuser(HttpServletRequest request,
						HttpServletResponse response,
						@RequestParam(value = "orderId", required = false) String orderId,//上传身份证的运单号
						@RequestParam(value = "username", required = false) String username,//上传身份证收件人对应的姓名
						@RequestParam(value = "cardId", required = false) String cardId,//上传的身份证号码
						//@RequestParam(value = "cardfront", required = false) String cardfront,//上传的身份证前端图
						//@RequestParam(value = "cardother", required = false) String cardother//上传的身份证反端图
						@RequestParam(value = "radioupload", required = false) String radioupload,
						@RequestParam(value = "zmpicture", required = false) MultipartFile zmpicture,
						@RequestParam(value = "fmpicture", required = false) MultipartFile fmpicture,
						@RequestParam(value = "hcpicture", required = false) MultipartFile hcpicture
						){
					//return "{\"code\":200}";
						ResponseString responseString=new ResponseString();
						
						String userId = (String)request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY);
						if(StringUtil.isEmpty(userId))
						{
							responseString.setCode(ResponseCode.NEED_LOGIN);
							responseString.setMessage("请登陆后操作");
							return responseString.toString();
						}
						
						
						if(StringUtil.isEmpty(orderId))
						{
							responseString.setCode(ResponseCode.PARAMETER_ERROR);
							responseString.setMessage("运单号不能为空！");
							return responseString.toString();
							//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"运单号不能为空！"); 
						}
						if(StringUtil.isEmpty(username))
						{
							responseString.setCode(ResponseCode.PARAMETER_ERROR);
							responseString.setMessage("身份证姓名不能为空！");
							return responseString.toString();
							//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"身份证姓名不能为空！"); 
						}
						if(StringUtil.isEmpty(cardId))
						{
							responseString.setCode(ResponseCode.PARAMETER_ERROR);
							responseString.setMessage("身份证号码不能为空！");
							return responseString.toString();
							//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"身份证号码不能为空！"); 
						}
						else
						{
							if (!ConsigneeInfoUtil.validateCardId(cardId)) {
								responseString.setCode(ResponseCode.CONSIGNEE_CARD_ID_ERROR);
								responseString.setMessage("身份证号码填写错误，请重新填写！");
								return responseString.toString();
								//return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
								//		"身份证号码填写错误，请重新填写！");
							}
						}
						if(!StringUtil.isEmpty(radioupload))
						{
							if(radioupload.equalsIgnoreCase("zf"))
							{
								
								if((zmpicture==null)||(zmpicture.getSize()==0))
								{
									responseString.setCode(ResponseCode.PARAMETER_ERROR);
									responseString.setMessage("必须上传身份证正面图片！");
									return responseString.toString();
									//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"必须上传身份证正面图片！"); 
								}
								if((fmpicture==null)||(fmpicture.getSize()==0))
								{
									responseString.setCode(ResponseCode.PARAMETER_ERROR);
									responseString.setMessage("必须上传身份证背面图片！");
									return responseString.toString();
									//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"必须上传身份证背面图片！"); 
								}
								hcpicture=null;
							}
							else if(radioupload.equalsIgnoreCase("hc"))
							{
								if((hcpicture==null)||(hcpicture.getSize()==0))
								{
									responseString.setCode(ResponseCode.PARAMETER_ERROR);
									responseString.setMessage("必须上传身份证图片！");
									return responseString.toString();
									//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"必须上传身份证背面图片！"); 
								}
								fmpicture=null;
								zmpicture=null;
								
							}
							else
							{
								responseString.setCode(ResponseCode.PARAMETER_ERROR);
								responseString.setMessage("参数错误！");
								return responseString.toString();
							}
						}
						else
						{
							responseString.setCode(ResponseCode.PARAMETER_ERROR);
							responseString.setMessage("参数错误！");
							return responseString.toString();
						}
					
						
						//获取运单号是否存在并判断是否合法
						Receive_User rev=null;
						try
						{
						M_order order=this.m_orderDao.getRouteArgsbyOrderId(orderId.trim(), null);
						if(order==null)
						{
							responseString.setCode(ResponseCode.PARAMETER_ERROR);
							responseString.setMessage("上传的运单号不存在！");
							return responseString.toString();
							//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"上传的运单号不存在！");
						}
						else//检查运单号收件人信息
						{
							if(StringUtil.isEmpty(order.getRuserId()))//收件人信息为空
							{
								responseString.setCode(ResponseCode.PARAMETER_ERROR);
								responseString.setMessage("上传的运单信息有误！");
								return responseString.toString();
								//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"上传的运单信息有误！");
							}
							else
							{
								
								if(!userId.equalsIgnoreCase(order.getUserId()))
								{
									responseString.setCode(ResponseCode.PARAMETER_ERROR);
									responseString.setMessage("运单号不属于本用户！");
									return responseString.toString();
								}
								 rev=this.receive_UserDao.getById(Integer.parseInt(order.getRuserId()));
								if(rev==null)
								{
									responseString.setCode(ResponseCode.PARAMETER_ERROR);
									responseString.setMessage("上传的运单信息有误！");
									return responseString.toString();
									//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"上传的运单信息有误！");
								}
								else
								{
									if(!username.trim().equalsIgnoreCase(rev.getName().trim()))
									{
										responseString.setCode(ResponseCode.PARAMETER_ERROR);
										responseString.setMessage("上传的运单号姓名不匹配！");
										return responseString.toString();
										//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"上传的运单号姓名不匹配！");
									}
								}
							}
						}
						
						// 处理提交上来的图片
						// 解决火狐的反斜杠问题 kai 20151006
						String filetype = this.defaultCardFileType;// 要上传的文件类型
						String strtest = this.save258CardDir;
						String strseparator = "";
						if (strtest.indexOf("/") >= 0)// 包含有“/”字符，分隔符用此字符
						{
							strseparator = "/";
						} else {
							strseparator = File.separator;
						}
						//开始处理图片
						// 获取当时的时间缀
						Date date = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyyMMddHHmmss");
						String timestr = sdf.format(date);
						//如果上传的图片中包含合成图片，那么即会忽略其它的正反两面图片
						//处理反面图片
						String fileName = "";
						String fileotherName = "";
						//合成处理
						String filecardtemp = "";
						if(radioupload.equalsIgnoreCase("zf"))
						{
							
						
							if (zmpicture.getSize() > this.defaultCardFileSize) {
								responseString.setCode(ResponseCode.PARAMETER_ERROR);
								responseString.setMessage("图像文件过大,请重新尝试!");
								return responseString.toString();
								//return generateResponseObject(
								//		ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
							}

							String originalName = zmpicture.getOriginalFilename();

							if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
								responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
								responseString.setMessage("上传图像文件格式不对,请重新尝试!");
								return responseString.toString();
								//return generateResponseObject(
								//		ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
							}
							int index = originalName.lastIndexOf('.');
							index = Math.max(index, 0);
							
							fileName = this.save258CardDir + strseparator + timestr + "_"+orderId+ "_"+"zm"+"_"
									+ StringUtil.generateRandomString(5) + "_"
									+ StringUtil.generateRandomInteger(5)
									+ originalName.substring(index);
							try {
								File file1 = new File(request.getSession().getServletContext()
										.getRealPath("/")
										+ fileName);
								zmpicture.transferTo(file1);
							} catch (Exception e) {
								log.error("保存用户正图像失败,请不要上传图像！", e);
								responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
								responseString.setMessage("保存用户正图像失败，请去除上传图像后再尝试!");
								return responseString.toString();
								//return generateResponseObject(
								//		ResponseCode.CONSIGNEE_CARD_ERROR,
								//		"保存用户正图像失败，请去除上传图像后再尝试!");
							}
							
							
							
							
							if (fmpicture.getSize() > this.defaultCardFileSize) {
								responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
								responseString.setMessage("图像文件过大,请重新尝试!");
								return responseString.toString();
								//return generateResponseObject(
									//	ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
							}

							 originalName = fmpicture.getOriginalFilename();

							if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
								responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
								responseString.setMessage("上传图像文件格式不对,请重新尝试!");
								return responseString.toString();
								//return generateResponseObject(
								//		ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
							}
							index = originalName.lastIndexOf('.');
							index = Math.max(index, 0);
							
							fileotherName = this.save258CardDir + strseparator + timestr + "_"+orderId+ "_"+"fm"+"_"
									+ StringUtil.generateRandomString(5) + "_"
									+ StringUtil.generateRandomInteger(5)
									+ originalName.substring(index);
							try {
								File file1 = new File(request.getSession().getServletContext()
										.getRealPath("/")
										+ fileotherName);
								fmpicture.transferTo(file1);
							} catch (Exception e) {
								log.error("保存用户反面图像失败,请不要上传图像！", e);
								
								responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
								responseString.setMessage("保存用户反面图像失败，请去除上传图像后再尝试!");
								return responseString.toString();
								//return generateResponseObject(
								//		ResponseCode.CONSIGNEE_CARD_ERROR,
								//		"保存用户反面图像失败，请去除上传图像后再尝试!");
							}
							
							
							//合成图片处理
							
							
							if ((StringUtil.isEmpty(fileotherName)) || (StringUtil.isEmpty(fileName))) {
								//return generateResponseObject(
								//		ResponseCode.CONSIGNEE_CARD_ERROR,
								//		"正反面生成路径出错!");
								responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
								responseString.setMessage("正反面生成路径出错");
								return responseString.toString();
							}
							else{
								imgcompose img = new imgcompose();
								String str1 = request.getSession().getServletContext()
										.getRealPath("/")
										+ fileName;
								String str2 = request.getSession().getServletContext()
										.getRealPath("/")
										+ fileotherName;
								
								
								File file4 = new File(str1);
								File file5 = new File(str2);
								if((file4.exists())&&(file5.exists()))
								{
									String str3 = this.save258CardDir + strseparator + timestr + "_"+orderId+ "_"+"hc"+"_"
											+ StringUtil.generateRandomString(5) + "_"
											+ StringUtil.generateRandomInteger(5);
									filecardtemp = str3;
									str3 = request.getSession().getServletContext().getRealPath("/")
											+ str3;
									if (img.createcompics(str1, str2, str3)) {
										filecardtemp = filecardtemp + ".jpg";
									}
								}
								else
								{
									responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
									responseString.setMessage("生成合成图发生异常!");
									return responseString.toString();
									//return generateResponseObject(
									//		ResponseCode.CONSIGNEE_CARD_ERROR,
									//		"生成合成图发生异常!");
								}
							}
							
							if(filecardtemp.equalsIgnoreCase(""))
							{
								responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
								responseString.setMessage("生成合成图失败!");
								return responseString.toString();
								//return generateResponseObject(
								//		ResponseCode.CONSIGNEE_CARD_ERROR,
								//		"生成合成图失败!");
							}
							
							
							
							if(rev!=null)
							{
							
								try{
									File filetemp=null;
									if(!StringUtil.isEmpty(rev.getCardurl()))
									{
										filetemp = new File(request.getSession().getServletContext()
												.getRealPath("/")
												+ rev.getCardurl());
										if(filetemp!=null&&filetemp.exists())//原来正面图片存在，删除
										{
											if(rev.getCardurl().indexOf(orderId)>=0)
											{
												filetemp.delete();
											}
										}
									}
									
									if(!StringUtil.isEmpty(rev.getCardother()))
									{
										filetemp = new File(request.getSession().getServletContext()
												.getRealPath("/")
												+ rev.getCardother());
										if(filetemp!=null&&filetemp.exists())//原来的反面图片，删除
										{
											if(rev.getCardother().indexOf(orderId)>=0)
											{
												filetemp.delete();
											}
										}
									}
									
									
									if(!StringUtil.isEmpty(rev.getCardtogether()))
									{
										filetemp = new File(request.getSession().getServletContext()
												.getRealPath("/")
												+ rev.getCardtogether());
										if(filetemp!=null&&filetemp.exists())//原来的反面图片，删除
										{
											if(rev.getCardother().indexOf(orderId)>=0)
											{
												filetemp.delete();
											}
										}
									}
								}
								catch(Exception e)
								{
									//不影响原来操作
								}
							}
							
						}
						else if(radioupload.equalsIgnoreCase("hc"))
						{
							
							
							
							if (hcpicture.getSize() > this.defaultCardFileSize) {
								responseString.setCode(ResponseCode.PARAMETER_ERROR);
								responseString.setMessage("图像文件过大,请重新尝试!");
								return responseString.toString();
								//return generateResponseObject(
								//		ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
							}

							String originalName = hcpicture.getOriginalFilename();

							if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
								responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
								responseString.setMessage("上传图像文件格式不对,请重新尝试!");
								return responseString.toString();
								//return generateResponseObject(
								//		ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
							}
							int index = originalName.lastIndexOf('.');
							index = Math.max(index, 0);
							
							filecardtemp = this.save258CardDir + strseparator + timestr + "_"+orderId+ "_"+"hc"+"_"
									+ StringUtil.generateRandomString(5) + "_"
									+ StringUtil.generateRandomInteger(5)
									+ originalName.substring(index);
							try {
								File file1 = new File(request.getSession().getServletContext()
										.getRealPath("/")
										+ filecardtemp);
								hcpicture.transferTo(file1);
							} catch (Exception e) {
								log.error("保存用户正图像失败,请不要上传图像！", e);
								responseString.setCode(ResponseCode.CONSIGNEE_CARD_ERROR);
								responseString.setMessage("保存用户正图像失败，请去除上传图像后再尝试!");
								return responseString.toString();
								//return generateResponseObject(
								//		ResponseCode.CONSIGNEE_CARD_ERROR,
								//		"保存用户正图像失败，请去除上传图像后再尝试!");
							}
							
							if(rev!=null)
							{
							
								try{
									File filetemp=null;
									if(!StringUtil.isEmpty(rev.getCardurl()))
									{
										filetemp = new File(request.getSession().getServletContext()
												.getRealPath("/")
												+ rev.getCardurl());
										if(filetemp!=null&&filetemp.exists())//原来正面图片存在，删除
										{
											if(rev.getCardurl().indexOf(orderId)>=0)
											{
												filetemp.delete();
											}
										}
									}
									fileName="";
									if(!StringUtil.isEmpty(rev.getCardother()))
									{
										filetemp = new File(request.getSession().getServletContext()
												.getRealPath("/")
												+ rev.getCardother());
										if(filetemp!=null&&filetemp.exists())//原来的反面图片，删除
										{
											if(rev.getCardother().indexOf(orderId)>=0)
											{
												filetemp.delete();
											}
										}
									}
									fileotherName="";
									
									if(!StringUtil.isEmpty(rev.getCardtogether()))
									{
										filetemp = new File(request.getSession().getServletContext()
												.getRealPath("/")
												+ rev.getCardtogether());
										if(filetemp!=null&&filetemp.exists())//原来的反面图片，删除
										{
											if(rev.getCardother().indexOf(orderId)>=0)
											{
												filetemp.delete();
											}
										}
									}
								}
								catch(Exception e)
								{
									//不影响原来操作
								}
							}
						}
						else
						{
							responseString.setCode(ResponseCode.PARAMETER_ERROR);
							responseString.setMessage("类型参数错误!");
							return responseString.toString();
						}
							
							rev.setCardid(cardId);
							rev.setCardurl(fileName);
							rev.setCardother(fileotherName);
							rev.setCardtogether(filecardtemp);
							
							rev.setUploadflag(Constant.UPLOAD_CARD_TYPE0);//表示用户前端页面上传
							
							String date1 = DateUtil.date2String(new Date());
							rev.setModifyDate(date1);
							rev.setCardid_flag(Constant.VERFY_CARDID_0);
							int k=this.receive_UserDao.modifyuploadcardinfo(rev);
							if(k<1)
							{
								responseString.setCode(ResponseCode.PARAMETER_ERROR);
								responseString.setMessage("上传失败！");
								return responseString.toString();
								//return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"上传失败！"); 
							}
							//response.setContentType("text/html");
							//response.set
							responseString.setCode(ResponseCode.SUCCESS_CODE);
							responseString.setMessage("上传成功！");
							return responseString.toString();
							//return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE); 
							//return null;
						
						}catch(Exception e)
						{
							log.error("生成身份证图片失败", e);
							responseString.setCode(ResponseCode.SHOW_EXCEPTION);
							responseString.setMessage("生成图片出现异常！");
							return responseString.toString();
							//return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "生成图片出现异常");
						}
					
				}
				
	
				
				
				//查找用户的在转运置单
				//搜索运单
				@RequestMapping(value = "/m_order/search_tran_byUser",method = { RequestMethod.GET, RequestMethod.POST})
				@ResponseBody
				public ResponseObject<PageSplit<M_order>> searchtranByKeyOfUser(
						HttpServletRequest request,
						@RequestParam(value = "keyword", required = false) String info,//查找的信息								
						@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
						@RequestParam(value = "rows", required = false, defaultValue = "10") int rows
						) {
					try {
						
						String userId = StringUtil.obj2String(request.getSession()
								.getAttribute(Constant.USER_ID_SESSION_KEY));
						if(StringUtil.isEmpty(userId))
						{
							return new ResponseObject<PageSplit<M_order>>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
						}
										
						pageIndex = Math.max(pageIndex, 1);
						return this.m_orderService.searchMordersbyUser(userId, null, null, null, info, Constant.ORDER_STATE__9, null, null, rows, pageIndex);
			
					} catch (Exception e) {
						log.error("查询运单失败", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "查询失败");
					}
				}	
				
				
				
				
				//用户端的获取待付款运单
				//搜索运单
				@RequestMapping(value = "/m_order/searchbyUsernopay",method = { RequestMethod.GET, RequestMethod.POST})
				@ResponseBody
				public ResponseObject<PageSplit<M_order>> searchByKeyOfUsernopay(
						HttpServletRequest request,
						@RequestParam(value = "orderId", required = false) String orderId,//查找的信息
						@RequestParam(value = "info", required = false) String info,//查找的信息								
						@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
						@RequestParam(value = "rows", required = false, defaultValue = "10") int rows
						) {
					try {
						
						String userId = StringUtil.obj2String(request.getSession()
								.getAttribute(Constant.USER_ID_SESSION_KEY));
						if(StringUtil.isEmpty(userId))
						{
							return new ResponseObject<PageSplit<M_order>>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
						}
						
						
					
						pageIndex = Math.max(pageIndex, 1);
						return this.m_orderService.searchMordersbyUserpayornot(userId, orderId, info, Constant.ORDER_PAY_STATE0, rows, pageIndex);
						//return this.m_orderService.searchMordersbyUser(userId, oid, wid, cid, info, state, type, payornot, rows, pageIndex);
						//return this.m_orderService.searchMorders(oid, sod, god, flyno, storeid, cid, userinfo, commudityinfo, state, type,payway, sdate, edate, psdate, pedate,cardinfo,payornot, rows, pageIndex);
						//return new ResponseObject<PageSplit<Order>>();
						//return this.orderService.searchOrdersByKeys(oid, null, key, column,
						//		sdate, edate, state,typekey,storeid, defaultPageSize, pageIndex);
					} catch (Exception e) {
						log.error("查询运单失败", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "查询失败");
					}
				}
				
				
				
				
				
				
				
				
				
				
				
				//自理转运用户提交上来的运单
				@RequestMapping(value="/admin/m_order/process_torder_submit", method={RequestMethod.POST,RequestMethod.GET})
				@ResponseBody
				public ResponseObject<Object> processtordersubmit(HttpServletRequest request,M_order order,
						@RequestParam(value = "cardurlfile", required = false) MultipartFile file,
						@RequestParam(value = "cardurlotherfile", required = false) MultipartFile fileother,
						@RequestParam(value = "cardurltogetherfile", required = false) MultipartFile filetogether){
					try{
						String storeId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
						if(StringUtil.isEmpty(storeId)){
							return generateResponseObject(ResponseCode.NEED_LOGIN,
									"请登陆！");
						}
						
						if(order==null)
						{
							return generateResponseObject(ResponseCode.PARAMETER_ERROR,
									"参数出错！");
						}
						
						//身份证号不为空，要检查合法性
						if(!StringUtil.isEmpty(order.getRuser().getCardid()))
						{
							if (!ConsigneeInfoUtil.validateCardId(order.getRuser().getCardid())) {
								return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
										"身份证号码填写错误，请重新填写！");
							}
						}
						//只要有一个上传不为空，身份证号就不为空
						if ((file != null && file.getSize() > 0)||(fileother != null && fileother.getSize() > 0)||(filetogether != null && filetogether.getSize() > 0))
						{
							if(StringUtil.isEmpty(order.getRuser().getCardid()))
							{
								return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
										"上传图片时，身份证号码不能为空，请重新填写！");
							}
							
							//上传正反两面图片时，必须保证两面图片都正确并存在
							if((file != null && file.getSize()>0)&&(fileother==null))
							{
								if(!StringUtil.isEmpty(order.getRuser().getCardother()))
								{
									File file3 = new File(request.getSession().getServletContext()
											.getRealPath("/")
											+ order.getRuser().getCardother());
									if(!file3.exists())
									{
										return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
												"正反两面照片必须同时存在！");
									}
								}
								else
								{
									return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
											"正反两面照片必须同时存在！");
								}
							}
							if((fileother != null && fileother.getSize()>0)&&(file==null))
							{
								if(!StringUtil.isEmpty(order.getRuser().getCardurl()))
								{
									File file3 = new File(request.getSession().getServletContext()
											.getRealPath("/")
											+ order.getRuser().getCardurl());
									if(!file3.exists())
									{
										return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
												"正反两面照片必须同时存在！");
									}
								}
								else
								{
									return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
											"正反两面照片必须同时存在！");
								}
							}
							
							
					
						}
						// 处理提交上来的图片
						// 解决火狐的反斜杠问题 kai 20151006
						String filetype = this.defaultCardFileType;// 要上传的文件类型
						String strtest = this.save258CardDir;
						String strseparator = "";
						if (strtest.indexOf("/") >= 0)// 包含有“/”字符，分隔符用此字符
						{
							strseparator = "/";
						} else {
							strseparator = File.separator;
						}
						//开始处理图片
						// 获取当时的时间缀
						Date date = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyyMMddHHmmss");
						String timestr = sdf.format(date);
						//如果上传的图片中包含合成图片，那么即会忽略其它的正反两面图片
						if (filetogether != null && filetogether.getSize() > 0)
						{
							String fileName = null;
						
							if (filetogether.getSize() > this.defaultCardFileSize) {
								return generateResponseObject(
										ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
							}

							String originalName = filetogether.getOriginalFilename();

							if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
								return generateResponseObject(
										ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
							}
							int index = originalName.lastIndexOf('.');
							index = Math.max(index, 0);
							
							fileName = this.save258CardDir + strseparator + timestr + "_"+storeId+ "_"+"hc"+"_"
									+ StringUtil.generateRandomString(5) + "_"
									+ StringUtil.generateRandomInteger(5)
									+ originalName.substring(index);
							try {
								File file1 = new File(request.getSession().getServletContext()
										.getRealPath("/")
										+ fileName);
								filetogether.transferTo(file1);
							} catch (Exception e) {
								log.error("保存用户合成图像失败,请不要上传图像！", e);
								return generateResponseObject(
										ResponseCode.CONSIGNEE_CARD_ERROR,
										"保存用户合成图像失败，请去除上传图像后再尝试!");
							}
							
							order.getRuser().setCardtogether(fileName);
						}
						else
						{
							//正反图片有上传的图片
							if ((file != null && file.getSize() > 0)||(fileother != null && fileother.getSize() > 0))
							{
								//正面图片处理
								if(file != null && file.getSize() > 0)
								{
									String fileName = null;
									if (file.getSize() > this.defaultCardFileSize) {
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
									}
					
									String originalName = file.getOriginalFilename();
					
									if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
									}
									int index = originalName.lastIndexOf('.');
									index = Math.max(index, 0);
									
									fileName = this.save258CardDir + strseparator + timestr + "_"+storeId+ "_"+"zm"+"_"
											+ StringUtil.generateRandomString(5) + "_"
											+ StringUtil.generateRandomInteger(5)
											+ originalName.substring(index);
									try {
										File file1 = new File(request.getSession().getServletContext()
												.getRealPath("/")
												+ fileName);
										file.transferTo(file1);
									} catch (Exception e) {
										log.error("保存用户正面图像失败,请不要上传图像！", e);
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR,
												"保存用户正面图像失败，请去除上传图像后再尝试!");
									}
									
									order.getRuser().setCardurl(fileName);
								}
								
								//反面图片处理
								if(fileother != null && fileother.getSize() > 0)
								{
									String fileName = null;
									if (fileother.getSize() > this.defaultCardFileSize) {
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
									}
					
									String originalName = fileother.getOriginalFilename();
					
									if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
									}
									int index = originalName.lastIndexOf('.');
									index = Math.max(index, 0);
									
									fileName = this.save258CardDir + strseparator + timestr + "_"+storeId+ "_"+"fm"+"_"
											+ StringUtil.generateRandomString(5) + "_"
											+ StringUtil.generateRandomInteger(5)
											+ originalName.substring(index);
									try {
										File file1 = new File(request.getSession().getServletContext()
												.getRealPath("/")
												+ fileName);
										fileother.transferTo(file1);
									} catch (Exception e) {
										log.error("保存用户反面图像失败,请不要上传图像！", e);
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR,
												"保存用户反面图像失败，请去除上传图像后再尝试!");
									}
									
									order.getRuser().setCardother(fileName);
								}
								
								
								//合成处理
								String filecardtemp = "";
								if ((!StringUtil.isEmpty(order.getRuser().getCardurl())) && (!StringUtil.isEmpty(order.getRuser().getCardother()))) {
									imgcompose img = new imgcompose();
									String str1 = request.getSession().getServletContext()
											.getRealPath("/")
											+ order.getRuser().getCardurl();
									String str2 = request.getSession().getServletContext()
											.getRealPath("/")
											+ order.getRuser().getCardother();
									
									
									File file4 = new File(str1);
									File file5 = new File(str2);
									if((file4.exists())&&(file5.exists()))
									{
										String str3 = this.save258CardDir + strseparator + timestr + "_"+storeId+ "_"+"hc"+"_"
												+ StringUtil.generateRandomString(5) + "_"
												+ StringUtil.generateRandomInteger(5);
										filecardtemp = str3;
										str3 = request.getSession().getServletContext().getRealPath("/")
												+ str3;
										if (img.createcompics(str1, str2, str3)) {
											filecardtemp = filecardtemp + ".jpg";
										}
									}
									
								}
								
								if(!StringUtil.isEmpty(filecardtemp))
								{
									order.getRuser().setCardtogether(filecardtemp);
								}
								
							}
							
							
							
						}
						
						
				
						
						//pageIndex = Math.max(pageIndex, 1);
						if (!ConsigneeInfoUtil.validatePhone(order.getRuser().getPhone())) {
						      return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "收件人手机号码填写错误，请检查！");
						}
						/*if (!ConsigneeInfoUtil.validatePhone(order.getSuser().getPhone())) {
						      return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "发件人手机号码填写错误，请检查！");
						}*/
						String empId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY));
						
						
					
						//此部分的目的是把前端修改过的参数全部代入已有的参数中，再调用用修改函数
						boolean payornotchange=false;//记录支付状态是否变更
						boolean changestate=false;//记录路由状态是否变更
						String storeId1=storeId;
						storeId=null;
						String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
						if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
						{
							storeId=null;//表示可以查找所有门店
							
						}else
						{
							storeId = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
							if((storeId==null)||(storeId.equalsIgnoreCase("")))
							{
								return generateResponseObject(ResponseCode.NEED_LOGIN,
										"你没有登陆!");
							}
						}
						M_order order1=null;//查找得到的先前的运单信息
						if(StringUtil.isEmpty(order.getId()))
						{
							return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数出错,没有id号!");
						}
						else
						{
							 order1=this.m_orderDao.getById(order.getId(), storeId);
							if(order1==null)
							{
								return generateResponseObject(ResponseCode.PARAMETER_ERROR, "没有找到对应的运单的id号,参数可能出错!");
							}
							
							if(order1.getUser()==null)
							{
								return generateResponseObject(ResponseCode.PARAMETER_ERROR, "查找会员信息出错!");
							}
							
							//
							
							//如果原来没有此管理员的，要加上管理员id
							if(StringUtil.isEmpty(order1.getEmployeeId())||order1.getEmployeeId().equalsIgnoreCase("-1")||order1.getI_employeeId().equalsIgnoreCase("0"))
							{
								order1.setEmployeeId(empId);
							}
							
							
							//可能修改的寄件人信息
							/*order1.getSuser().setName(order.getSuser().getName());
							order1.getSuser().setPhone(order.getSuser().getPhone());
							order1.getSuser().setAddress(order.getSuser().getAddress());
							order1.getSuser().setCity(order.getSuser().getCity());
							order1.getSuser().setState(order.getSuser().getState());
							order1.getSuser().setZipcode(order.getSuser().getZipcode());
							order1.getSuser().setEmail(order.getSuser().getEmail());
							order1.getSuser().setCompany(order.getSuser().getCompany());*/
							//支付方式可能会修改
							//order1.setPayWay(order.getPayWay());
							
							//收件人可能修改的信息
							order1.getRuser().setName(order.getRuser().getName());
							order1.getRuser().setPhone(order.getRuser().getPhone());
							order1.getRuser().setState(order.getRuser().getState());
							order1.getRuser().setCity(order.getRuser().getCity());
							order1.getRuser().setDist(order.getRuser().getDist());
							order1.getRuser().setAddress(order.getRuser().getAddress());
							order1.getRuser().setZipcode(order.getRuser().getZipcode());
							order1.getRuser().setCardid(order.getRuser().getCardid());
							order1.getRuser().setCardurl(order.getRuser().getCardurl());
							order1.getRuser().setCardother(order.getRuser().getCardother());
							order1.getRuser().setCardtogether(order.getRuser().getCardtogether());
							
							
							//替换原来的商品信息
							order1.setDetail(order.getDetail());
							
							//替换全局参数
							order1.setWeight(order.getWeight());
							order1.setSjweight(order.getSjweight());
							order1.setQuantity(order.getQuantity());
							order1.setValue(order.getValue());
							order1.setInsurance(order.getInsurance());
							order1.setOther(order.getOther());
							order1.setTariff(order.getTariff());
							order1.setRemark(order.getRemark());
							order1.setQremark(order.getQremark());
							//order1.setState(order.getState());
							//order1.setPayornot(order.getPayornot());
							//order1.setAutomessage(order.getAutomessage());
							
							order1.setStoreId(order.getStoreId());
							order1.setChannelId(order.getChannelId());
							order1.setTmoney(order.getTmoney());
							
							if(StringUtil.isEmpty(order1.getEmployeeId()))
							{
								order1.setEmployeeId(empId);
							}
						}
						
						//替换成现在的
						order=order1;
						
						
						
						
						
						
						
						
						
						
						double price =this.m_orderService.calculationM_orderFreight_tran(order);//计算包含转运费用的接口
						
						
								//.calculationM_orderFreight(order);
						
						//先保存已经计算的价格商品list
						List<M_OrderDetail> details=new ArrayList<M_OrderDetail>();
						for(M_OrderDetail md:order.getDetail())
						{
							M_OrderDetail aa=new M_OrderDetail();
							aa.setAllcprice(md.getAllcprice());
							aa.setBrands(md.getBrands());
							aa.setCommodityId(md.getCommodityId());
							aa.setCprice(md.getCprice());
							aa.setCtype(md.getCtype());
							aa.setId(md.getId());
							aa.setName(md.getName());
							aa.setOr(md.getOr());
							aa.setOrderId(md.getOrderId());
							aa.setOther(md.getOther());
							aa.setProductName(md.getProductName());
							aa.setQuantity(md.getQuantity());
							aa.setRemark(md.getRemark());
							aa.setTariff(md.getTariff());
							aa.setValue(md.getValue());
							aa.setWeight(md.getWeight());
							details.add(aa);
						}
						
						
						if(price<=0)
						{
							return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "总价钱不能小于或等于0，值为:"+Double.toString(price));
						}
						double cost=this.m_orderService.calculationM_OrderCostFreight(order);
						try{
							double tcost=Double.parseDouble(order1.getTcost());
							if(tcost>=0)
							{
								cost=cost+tcost;
							}
						}catch(Exception e){
							
						}
						if(cost<=0)
						{
							return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "成本不能小于或等于0，请检查配置，值为:"+Double.toString(cost));
						}
						
						
						
						double typeprice=0;
						//计算普通会员与会员之间的价格并保存
						double nprice=this.m_orderService.calculationM_orderFreight_usertype(order,Constant.USER_TYPE_NORMAL);//计算普通会员的价格
						if(nprice>price)
						{
							order.setUser_price(Double.toString(nprice));
						}
						else
						{
							order.setUser_price(Double.toString(price));
						}
						order.setDetail(details);//保存以会员价格计算的商品信息
						
						
						//order.setState(Constant.ORDER_STATE2);//设置为待付款状态
						//order.getRuser().setUseState("1");
						//order.getSuser().setUseState("1");
						order.setTotalmoney(Double.toString(price));
						order.setTotalcost(Double.toString(cost));
						
						
			
						//return this.m_orderService.add_ms_Morder(order);
						return this.m_orderService.modify_tran_Morder(order1, empId, storeId1);
						//return this.m_orderService.modify_orpay_Morder(order,payornotchange,changestate,payflag,empId,storeId1);
						
					}catch(Exception e){
						
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
					}
				}
				
					
			
				
				
				//当判断用户余额不足时，提交生成未付款运单
				@RequestMapping(value="/admin/m_order/process_torder_submit_nopay", method={RequestMethod.POST,RequestMethod.GET})
				@ResponseBody
				public ResponseObject<Object> processtordersubmitnopay(HttpServletRequest request,M_order order,
						@RequestParam(value = "cardurlfile", required = false) MultipartFile file,
						@RequestParam(value = "cardurlotherfile", required = false) MultipartFile fileother,
						@RequestParam(value = "cardurltogetherfile", required = false) MultipartFile filetogether,
						@RequestParam(value = "shelvesId", required = false) String shelvesId){
					try{
						String storeId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY));
						if(StringUtil.isEmpty(storeId)){
							return generateResponseObject(ResponseCode.NEED_LOGIN,
									"请登陆！");
						}
						
						if(order==null)
						{
							return generateResponseObject(ResponseCode.PARAMETER_ERROR,
									"参数出错！");
						}
						
						//身份证号不为空，要检查合法性
						if(!StringUtil.isEmpty(order.getRuser().getCardid()))
						{
							if (!ConsigneeInfoUtil.validateCardId(order.getRuser().getCardid())) {
								return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
										"身份证号码填写错误，请重新填写！");
							}
						}
						//只要有一个上传不为空，身份证号就不为空
						if ((file != null && file.getSize() > 0)||(fileother != null && fileother.getSize() > 0)||(filetogether != null && filetogether.getSize() > 0))
						{
							if(StringUtil.isEmpty(order.getRuser().getCardid()))
							{
								return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
										"上传图片时，身份证号码不能为空，请重新填写！");
							}
							
							//上传正反两面图片时，必须保证两面图片都正确并存在
							if((file != null && file.getSize()>0)&&(fileother==null))
							{
								if(!StringUtil.isEmpty(order.getRuser().getCardother()))
								{
									File file3 = new File(request.getSession().getServletContext()
											.getRealPath("/")
											+ order.getRuser().getCardother());
									if(!file3.exists())
									{
										return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
												"正反两面照片必须同时存在！");
									}
								}
								else
								{
									return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
											"正反两面照片必须同时存在！");
								}
							}
							if((fileother != null && fileother.getSize()>0)&&(file==null))
							{
								if(!StringUtil.isEmpty(order.getRuser().getCardurl()))
								{
									File file3 = new File(request.getSession().getServletContext()
											.getRealPath("/")
											+ order.getRuser().getCardurl());
									if(!file3.exists())
									{
										return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
												"正反两面照片必须同时存在！");
									}
								}
								else
								{
									return generateResponseObject(ResponseCode.CONSIGNEE_CARD_ID_ERROR,
											"正反两面照片必须同时存在！");
								}
							}
							
							
					
						}
						// 处理提交上来的图片
						// 解决火狐的反斜杠问题 kai 20151006
						String filetype = this.defaultCardFileType;// 要上传的文件类型
						String strtest = this.save258CardDir;
						String strseparator = "";
						if (strtest.indexOf("/") >= 0)// 包含有“/”字符，分隔符用此字符
						{
							strseparator = "/";
						} else {
							strseparator = File.separator;
						}
						//开始处理图片
						// 获取当时的时间缀
						Date date = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyyMMddHHmmss");
						String timestr = sdf.format(date);
						//如果上传的图片中包含合成图片，那么即会忽略其它的正反两面图片
						if (filetogether != null && filetogether.getSize() > 0)
						{
							String fileName = null;
						
							if (filetogether.getSize() > this.defaultCardFileSize) {
								return generateResponseObject(
										ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
							}

							String originalName = filetogether.getOriginalFilename();

							if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
								return generateResponseObject(
										ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
							}
							int index = originalName.lastIndexOf('.');
							index = Math.max(index, 0);
							
							fileName = this.save258CardDir + strseparator + timestr + "_"+storeId+ "_"+"hc"+"_"
									+ StringUtil.generateRandomString(5) + "_"
									+ StringUtil.generateRandomInteger(5)
									+ originalName.substring(index);
							try {
								File file1 = new File(request.getSession().getServletContext()
										.getRealPath("/")
										+ fileName);
								filetogether.transferTo(file1);
							} catch (Exception e) {
								log.error("保存用户合成图像失败,请不要上传图像！", e);
								return generateResponseObject(
										ResponseCode.CONSIGNEE_CARD_ERROR,
										"保存用户合成图像失败，请去除上传图像后再尝试!");
							}
							
							order.getRuser().setCardtogether(fileName);
						}
						else
						{
							//正反图片有上传的图片
							if ((file != null && file.getSize() > 0)||(fileother != null && fileother.getSize() > 0))
							{
								//正面图片处理
								if(file != null && file.getSize() > 0)
								{
									String fileName = null;
									if (file.getSize() > this.defaultCardFileSize) {
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
									}
					
									String originalName = file.getOriginalFilename();
					
									if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
									}
									int index = originalName.lastIndexOf('.');
									index = Math.max(index, 0);
									
									fileName = this.save258CardDir + strseparator + timestr + "_"+storeId+ "_"+"zm"+"_"
											+ StringUtil.generateRandomString(5) + "_"
											+ StringUtil.generateRandomInteger(5)
											+ originalName.substring(index);
									try {
										File file1 = new File(request.getSession().getServletContext()
												.getRealPath("/")
												+ fileName);
										file.transferTo(file1);
									} catch (Exception e) {
										log.error("保存用户正面图像失败,请不要上传图像！", e);
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR,
												"保存用户正面图像失败，请去除上传图像后再尝试!");
									}
									
									order.getRuser().setCardurl(fileName);
								}
								
								//反面图片处理
								if(fileother != null && fileother.getSize() > 0)
								{
									String fileName = null;
									if (fileother.getSize() > this.defaultCardFileSize) {
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR, "图像文件过大,请重新尝试!");
									}
					
									String originalName = fileother.getOriginalFilename();
					
									if (!StringUtil.boolpicisgoodornot(originalName, filetype)) {
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR, "上传图像文件格式不对,请重新尝试!");
									}
									int index = originalName.lastIndexOf('.');
									index = Math.max(index, 0);
									
									fileName = this.save258CardDir + strseparator + timestr + "_"+storeId+ "_"+"fm"+"_"
											+ StringUtil.generateRandomString(5) + "_"
											+ StringUtil.generateRandomInteger(5)
											+ originalName.substring(index);
									try {
										File file1 = new File(request.getSession().getServletContext()
												.getRealPath("/")
												+ fileName);
										fileother.transferTo(file1);
									} catch (Exception e) {
										log.error("保存用户反面图像失败,请不要上传图像！", e);
										return generateResponseObject(
												ResponseCode.CONSIGNEE_CARD_ERROR,
												"保存用户反面图像失败，请去除上传图像后再尝试!");
									}
									
									order.getRuser().setCardother(fileName);
								}
								
								
								//合成处理
								String filecardtemp = "";
								if ((!StringUtil.isEmpty(order.getRuser().getCardurl())) && (!StringUtil.isEmpty(order.getRuser().getCardother()))) {
									imgcompose img = new imgcompose();
									String str1 = request.getSession().getServletContext()
											.getRealPath("/")
											+ order.getRuser().getCardurl();
									String str2 = request.getSession().getServletContext()
											.getRealPath("/")
											+ order.getRuser().getCardother();
									
									
									File file4 = new File(str1);
									File file5 = new File(str2);
									if((file4.exists())&&(file5.exists()))
									{
										String str3 = this.save258CardDir + strseparator + timestr + "_"+storeId+ "_"+"hc"+"_"
												+ StringUtil.generateRandomString(5) + "_"
												+ StringUtil.generateRandomInteger(5);
										filecardtemp = str3;
										str3 = request.getSession().getServletContext().getRealPath("/")
												+ str3;
										if (img.createcompics(str1, str2, str3)) {
											filecardtemp = filecardtemp + ".jpg";
										}
									}
									
								}
								
								if(!StringUtil.isEmpty(filecardtemp))
								{
									order.getRuser().setCardtogether(filecardtemp);
								}
								
							}
							
							
							
						}
						
						
				
						
						//pageIndex = Math.max(pageIndex, 1);
						if (!ConsigneeInfoUtil.validatePhone(order.getRuser().getPhone())) {
						      return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "收件人手机号码填写错误，请检查！");
						}
						/*if (!ConsigneeInfoUtil.validatePhone(order.getSuser().getPhone())) {
						      return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "发件人手机号码填写错误，请检查！");
						}*/
						String empId = StringUtil.obj2String(request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY));
						
						
					
						//此部分的目的是把前端修改过的参数全部代入已有的参数中，再调用用修改函数
						boolean payornotchange=false;//记录支付状态是否变更
						boolean changestate=false;//记录路由状态是否变更
						String storeId1=storeId;
						storeId=null;
						String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
						if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
						{
							storeId=null;//表示可以查找所有门店
							
						}else
						{
							storeId = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
							if((storeId==null)||(storeId.equalsIgnoreCase("")))
							{
								return generateResponseObject(ResponseCode.NEED_LOGIN,
										"你没有登陆!");
							}
						}
						M_order order1=null;//查找得到的先前的运单信息
						if(StringUtil.isEmpty(order.getId()))
						{
							return generateResponseObject(ResponseCode.PARAMETER_ERROR, "参数出错,没有id号!");
						}
						else
						{
							 order1=this.m_orderDao.getById(order.getId(), storeId);
							if(order1==null)
							{
								return generateResponseObject(ResponseCode.PARAMETER_ERROR, "没有找到对应的运单的id号,参数可能出错!");
							}
							
							if(order1.getUser()==null)
							{
								return generateResponseObject(ResponseCode.PARAMETER_ERROR, "查找会员信息出错!");
							}
							
							//
							
							//如果原来没有此管理员的，要加上管理员id
							if(StringUtil.isEmpty(order1.getEmployeeId())||order1.getEmployeeId().equalsIgnoreCase("-1")||order1.getI_employeeId().equalsIgnoreCase("0"))
							{
								order1.setEmployeeId(empId);
							}
							
							
							//可能修改的寄件人信息
							/*order1.getSuser().setName(order.getSuser().getName());
							order1.getSuser().setPhone(order.getSuser().getPhone());
							order1.getSuser().setAddress(order.getSuser().getAddress());
							order1.getSuser().setCity(order.getSuser().getCity());
							order1.getSuser().setState(order.getSuser().getState());
							order1.getSuser().setZipcode(order.getSuser().getZipcode());
							order1.getSuser().setEmail(order.getSuser().getEmail());
							order1.getSuser().setCompany(order.getSuser().getCompany());*/
							//支付方式可能会修改
							//order1.setPayWay(order.getPayWay());
							
							//收件人可能修改的信息
							order1.getRuser().setName(order.getRuser().getName());
							order1.getRuser().setPhone(order.getRuser().getPhone());
							order1.getRuser().setState(order.getRuser().getState());
							order1.getRuser().setCity(order.getRuser().getCity());
							order1.getRuser().setDist(order.getRuser().getDist());
							order1.getRuser().setAddress(order.getRuser().getAddress());
							order1.getRuser().setZipcode(order.getRuser().getZipcode());
							order1.getRuser().setCardid(order.getRuser().getCardid());
							order1.getRuser().setCardurl(order.getRuser().getCardurl());
							order1.getRuser().setCardother(order.getRuser().getCardother());
							order1.getRuser().setCardtogether(order.getRuser().getCardtogether());
							
							
							//替换原来的商品信息
							order1.setDetail(order.getDetail());
							
							//替换全局参数
							order1.setWeight(order.getWeight());
							order1.setSjweight(order.getSjweight());
							order1.setQuantity(order.getQuantity());
							order1.setValue(order.getValue());
							order1.setInsurance(order.getInsurance());
							order1.setOther(order.getOther());
							order1.setTariff(order.getTariff());
							order1.setRemark(order.getRemark());
							order1.setQremark(order.getQremark());
							//order1.setState(order.getState());
							//order1.setPayornot(order.getPayornot());
							//order1.setAutomessage(order.getAutomessage());
							
							order1.setStoreId(order.getStoreId());
							order1.setChannelId(order.getChannelId());
							order1.setTmoney(order.getTmoney());
							
							if(StringUtil.isEmpty(order1.getEmployeeId()))
							{
								order1.setEmployeeId(empId);
							}
						}
						
						//替换成现在的
						order=order1;
						
						
						
						
						
						
						
						
						
						
						double price =this.m_orderService.calculationM_orderFreight_tran(order);//计算包含转运费用的接口
						
						
								//.calculationM_orderFreight(order);
						
						//先保存已经计算的价格商品list
						List<M_OrderDetail> details=new ArrayList<M_OrderDetail>();
						for(M_OrderDetail md:order.getDetail())
						{
							M_OrderDetail aa=new M_OrderDetail();
							aa.setAllcprice(md.getAllcprice());
							aa.setBrands(md.getBrands());
							aa.setCommodityId(md.getCommodityId());
							aa.setCprice(md.getCprice());
							aa.setCtype(md.getCtype());
							aa.setId(md.getId());
							aa.setName(md.getName());
							aa.setOr(md.getOr());
							aa.setOrderId(md.getOrderId());
							aa.setOther(md.getOther());
							aa.setProductName(md.getProductName());
							aa.setQuantity(md.getQuantity());
							aa.setRemark(md.getRemark());
							aa.setTariff(md.getTariff());
							aa.setValue(md.getValue());
							aa.setWeight(md.getWeight());
							details.add(aa);
						}
						
						
						if(price<=0)
						{
							return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "总价钱不能小于或等于0，值为:"+Double.toString(price));
						}
						double cost=this.m_orderService.calculationM_OrderCostFreight(order);
						try{
							double tcost=Double.parseDouble(order1.getTcost());
							if(tcost>=0)
							{
								cost=cost+tcost;
							}
						}catch(Exception e){
							
						}
						if(cost<=0)
						{
							return generateResponseObject(ResponseCode.CONSIGNEE_PHONE_ERROR, "成本不能小于或等于0，请检查配置，值为:"+Double.toString(cost));
						}
						
						
						
						double typeprice=0;
						//计算普通会员与会员之间的价格并保存
						double nprice=this.m_orderService.calculationM_orderFreight_usertype(order,Constant.USER_TYPE_NORMAL);//计算普通会员的价格
						if(nprice>price)
						{
							order.setUser_price(Double.toString(nprice));
						}
						else
						{
							order.setUser_price(Double.toString(price));
						}
						order.setDetail(details);//保存以会员价格计算的商品信息
						
						
						//order.setState(Constant.ORDER_STATE2);//设置为待付款状态
						//order.getRuser().setUseState("1");
						//order.getSuser().setUseState("1");
						order.setTotalmoney(Double.toString(price));
						order.setTotalcost(Double.toString(cost));
						
						
			
						//return this.m_orderService.add_ms_Morder(order);
						return this.m_orderService.modify_tran_Morder_nopay(order1, empId, storeId1,shelvesId);
						//return this.m_orderService.modify_orpay_Morder(order,payornotchange,changestate,payflag,empId,storeId1);
						
					}catch(Exception e){
						
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, e.getMessage());
					}
				}
				
						
				
				
				
				
				
				//待处理运单
				@RequestMapping(value = "/admin/m_order/search_nopay",method = { RequestMethod.GET, RequestMethod.POST})
				@ResponseBody
				public ResponseObject<PageSplit<M_order>> searchnopayeorderByKeyOfAdmin(
						HttpServletRequest request,
						@RequestParam(value = "oid", required = false) String oid,//运单号
						@RequestParam(value = "sod", required = false) String sod,//海关单号，即本单号关联的海关单号
						@RequestParam(value = "god", required = false) String god,//国内第三方快递单号
						@RequestParam(value = "wid", required = false) String wid,//所属门店id
						@RequestParam(value = "cid", required = false, defaultValue = "") String cid,//渠道id
						@RequestParam(value = "userinfo", required = false) String userinfo,//用户信息
						@RequestParam(value = "commudityinfo", required = false) String commudityinfo,//商品信息
						@RequestParam(value = "state", required = false) String state,//状态
						@RequestParam(value = "type", required = false) String type,//类型
						@RequestParam(value = "flyno", required = false) String flyno,//航班号
						@RequestParam(value = "sdate", required = false) String sdate,//创建的起始时间
						@RequestParam(value = "edate", required = false) String edate,//创建的结束时间
						@RequestParam(value = "psdate", required = false) String psdate,//支付的起始时间
						@RequestParam(value = "pedate", required = false) String pedate,//支付的结束时间
						@RequestParam(value = "payway", required = false) String payway,//支付方式
						@RequestParam(value = "cardinfo", required = false) String cardinfo,//身份证搜索选择
						//@RequestParam(value = "payornot", required = false) String payornot,//是否已付款
						
						@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
						@RequestParam(value = "rows", required = false, defaultValue = "10") int rows
						) {
					try {
						//String state=Constant.ORDER_STATE__9;//
						String payornot=Constant.ORDER_PAY_STATE0;
						if(StringUtil.isEmpty(payornot))
						{
							payornot=null;
						}
						else
						{
							if((!payornot.equalsIgnoreCase(Constant.ORDER_PAY_STATE0))&&(!payornot.equalsIgnoreCase(Constant.ORDER_PAY_STATE1)))
							{
								payornot=null;
							}
						}
						
						if(!StringUtil.isEmpty(wid)&&(wid.equalsIgnoreCase("-1")))
						{
							wid=null;
						}
						if(!StringUtil.isEmpty(cid)&&(cid.equalsIgnoreCase("-1")))
						{
							cid=null;
						}
						if(!StringUtil.isEmpty(state)&&(state.equalsIgnoreCase("-1")))
						{
							state=null;
						}
						if(!StringUtil.isEmpty(type)&&(type.equalsIgnoreCase("-1")))
						{
							type=null;
						}
						if(!StringUtil.isEmpty(payway)&&(payway.equalsIgnoreCase("-1")))
						{
							payway=null;
						}
						String storeid=null;
						String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
						if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
						{
							storeid=wid;//表示可以查找所有门店
							
						}else
						{
							storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
							if((!StringUtil.isEmpty(wid))&&(!wid.equalsIgnoreCase(storeid)))
							{
								return generateResponseObject(ResponseCode.PARAMETER_ERROR,
										"对不起，你不能访问其它门店!");
							}
							
							if((storeid==null)||(storeid.equalsIgnoreCase("")))
							{
								return generateResponseObject(ResponseCode.NEED_LOGIN,
										"你没有登陆!");
							}
						}
						
						
						/*if(userinfo!=null)
						{
						 userinfo = new String(request.getParameter("userinfo").getBytes("ISO-8859-1"), "utf-8");
							userinfo = URLDecoder.decode(userinfo, "UTF-8");
						}*/
						
						//key = new String(key.getBytes("ISO-8859-1"), "utf-8");
						if (StringUtil.isEmpty(sdate)
								|| !UserUtil.validateExportDate(sdate)) {
							sdate = "";
						} else {
							sdate = UserUtil.transformerDateString(sdate, " 00:00:00");
						}

						if (StringUtil.isEmpty(edate)
								|| !UserUtil.validateExportDate(edate)) {
							edate = "";
						} else {
							edate = UserUtil.transformerDateString(edate, " 23:59:59");
						}
						
						if (StringUtil.isEmpty(psdate)
								|| !UserUtil.validateExportDate(psdate)) {
							psdate = "";
						} else {
							psdate = UserUtil.transformerDateString(psdate, " 00:00:00");
						}

						if (StringUtil.isEmpty(pedate)
								|| !UserUtil.validateExportDate(pedate)) {
							pedate = "";
						} else {
							pedate = UserUtil.transformerDateString(pedate, " 23:59:59");
						}

						oid = StringUtil.isEmpty(oid) ? null : oid;
						sod = StringUtil.isEmpty(sod) ? null : sod;
						god = StringUtil.isEmpty(god) ? null : god;
						//String column = OrderUtil.getSearchColumnByType(type);
						state = OrderUtil.dealState(state);
						pageIndex = Math.max(pageIndex, 1);
						
						return this.m_orderService.searchMordersnopay(oid, sod, god, flyno, storeid, cid, userinfo, commudityinfo, state, type,payway, sdate, edate, psdate, pedate,cardinfo,payornot, rows, pageIndex);
						//return new ResponseObject<PageSplit<Order>>();
						//return this.orderService.searchOrdersByKeys(oid, null, key, column,
						//		sdate, edate, state,typekey,storeid, defaultPageSize, pageIndex);
					} catch (Exception e) {
						log.error("查询运单失败", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "查询失败");
					}
				}
				
						
				
				//待处理运单
				@RequestMapping(value = "/admin/m_order/search_wait",method = { RequestMethod.GET, RequestMethod.POST})
				@ResponseBody
				public ResponseObject<PageSplit<M_order>> searchwaiteorderByKeyOfAdmin(
						HttpServletRequest request,
						@RequestParam(value = "oid", required = false) String oid,//运单号
						@RequestParam(value = "sod", required = false) String sod,//海关单号，即本单号关联的海关单号
						@RequestParam(value = "god", required = false) String god,//国内第三方快递单号
						@RequestParam(value = "wid", required = false) String wid,//所属门店id
						@RequestParam(value = "cid", required = false, defaultValue = "") String cid,//渠道id
						@RequestParam(value = "userinfo", required = false) String userinfo,//用户信息
						@RequestParam(value = "commudityinfo", required = false) String commudityinfo,//商品信息
						//@RequestParam(value = "state", required = false) String state,//状态
						@RequestParam(value = "type", required = false) String type,//类型
						@RequestParam(value = "flyno", required = false) String flyno,//航班号
						@RequestParam(value = "sdate", required = false) String sdate,//创建的起始时间
						@RequestParam(value = "edate", required = false) String edate,//创建的结束时间
						@RequestParam(value = "psdate", required = false) String psdate,//支付的起始时间
						@RequestParam(value = "pedate", required = false) String pedate,//支付的结束时间
						@RequestParam(value = "payway", required = false) String payway,//支付方式
						@RequestParam(value = "cardinfo", required = false) String cardinfo,//身份证搜索选择
						@RequestParam(value = "payornot", required = false) String payornot,//是否已付款
						
						@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
						@RequestParam(value = "rows", required = false, defaultValue = "10") int rows
						) {
					try {
						String state=Constant.ORDER_STATE__9;//
						if(StringUtil.isEmpty(payornot))
						{
							payornot=null;
						}
						else
						{
							if((!payornot.equalsIgnoreCase(Constant.ORDER_PAY_STATE0))&&(!payornot.equalsIgnoreCase(Constant.ORDER_PAY_STATE1)))
							{
								payornot=null;
							}
						}
						
						if(!StringUtil.isEmpty(wid)&&(wid.equalsIgnoreCase("-1")))
						{
							wid=null;
						}
						if(!StringUtil.isEmpty(cid)&&(cid.equalsIgnoreCase("-1")))
						{
							cid=null;
						}
						if(!StringUtil.isEmpty(state)&&(state.equalsIgnoreCase("-1")))
						{
							state=null;
						}
						if(!StringUtil.isEmpty(type)&&(type.equalsIgnoreCase("-1")))
						{
							type=null;
						}
						if(!StringUtil.isEmpty(payway)&&(payway.equalsIgnoreCase("-1")))
						{
							payway=null;
						}
						String storeid=null;
						String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
						if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
						{
							storeid=wid;//表示可以查找所有门店
							
						}else
						{
							storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
							if((!StringUtil.isEmpty(wid))&&(!wid.equalsIgnoreCase(storeid)))
							{
								return generateResponseObject(ResponseCode.PARAMETER_ERROR,
										"对不起，你不能访问其它门店!");
							}
							
							if((storeid==null)||(storeid.equalsIgnoreCase("")))
							{
								return generateResponseObject(ResponseCode.NEED_LOGIN,
										"你没有登陆!");
							}
						}
						
						
						/*if(userinfo!=null)
						{
						 userinfo = new String(request.getParameter("userinfo").getBytes("ISO-8859-1"), "utf-8");
							userinfo = URLDecoder.decode(userinfo, "UTF-8");
						}*/
						
						//key = new String(key.getBytes("ISO-8859-1"), "utf-8");
						if (StringUtil.isEmpty(sdate)
								|| !UserUtil.validateExportDate(sdate)) {
							sdate = "";
						} else {
							sdate = UserUtil.transformerDateString(sdate, " 00:00:00");
						}

						if (StringUtil.isEmpty(edate)
								|| !UserUtil.validateExportDate(edate)) {
							edate = "";
						} else {
							edate = UserUtil.transformerDateString(edate, " 23:59:59");
						}
						
						if (StringUtil.isEmpty(psdate)
								|| !UserUtil.validateExportDate(psdate)) {
							psdate = "";
						} else {
							psdate = UserUtil.transformerDateString(psdate, " 00:00:00");
						}

						if (StringUtil.isEmpty(pedate)
								|| !UserUtil.validateExportDate(pedate)) {
							pedate = "";
						} else {
							pedate = UserUtil.transformerDateString(pedate, " 23:59:59");
						}

						oid = StringUtil.isEmpty(oid) ? null : oid;
						sod = StringUtil.isEmpty(sod) ? null : sod;
						god = StringUtil.isEmpty(god) ? null : god;
						//String column = OrderUtil.getSearchColumnByType(type);
						state = OrderUtil.dealState(state);
						pageIndex = Math.max(pageIndex, 1);
						
						return this.m_orderService.searchMorders(oid, sod, god, flyno, storeid, cid, userinfo, commudityinfo, state, type,payway, sdate, edate, psdate, pedate,cardinfo,payornot, rows, pageIndex);
						//return new ResponseObject<PageSplit<Order>>();
						//return this.orderService.searchOrdersByKeys(oid, null, key, column,
						//		sdate, edate, state,typekey,storeid, defaultPageSize, pageIndex);
					} catch (Exception e) {
						log.error("查询运单失败", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "查询失败");
					}
				}
				
				
				
				
				@RequestMapping(value = "/admin/m_order/user_pay", method = { RequestMethod.POST })
				@ResponseBody
				public ResponseObject<Object> payOrderMoneybyadmin(HttpServletRequest request,
						@RequestParam(value = "id") String id) {
					try {
						
						if(StringUtil.isEmpty(id))
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数错误");
						}
						
						String storeId=null;
						String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
						if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
						{
							storeId=null;//表示可以查找所有门店
							
						}else
						{
							storeId = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
							if((storeId==null)||(storeId.equalsIgnoreCase("")))
							{
								return generateResponseObject(ResponseCode.NEED_LOGIN,
										"你没有登陆!");
							}
						}
						
						
						String storeName = (String)request.getSession().getAttribute(Constant.EMP_STORE_NAME_SESSION_KEY);
						String empNo= (String)request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY);
						String empName=(String)request.getSession().getAttribute(Constant.EMP_ACCOUNT_SESSION_KEY);
				
						
						
						
						
						double totalMoney = 0;
						double rmb = 0;
						double usd = 0;
						
						double usdrate=0;
						String rate=this.globalargsDao.getcontentbyflag("cur_usa_cn");//汇率
						
						try{
								double dou = Double.parseDouble(rate);
								if(dou>0)
								{
									usdrate=dou;
								}
						
						}catch(Exception e)
						{
							
						}
						M_order order=this.m_orderDao.getById(id, storeId);
						
						if(order==null)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取运单信息失败!");
						}
						else
						{
							
							
							
							if(!Constant.ORDER_PAY_STATE0.equalsIgnoreCase(order.getPayornot()))
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"此运单不是未付款状态!!");
							}
							try{
								double dou = Double.parseDouble(order.getState());
								if(dou<2)
								{
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"此运单状态不能支付!");
								}
							}catch(Exception e)
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取状态发发生异常!");
							}
						}
						
						totalMoney=StringUtil.string2Double(order.getTotalmoney());
						if(totalMoney<=0)
						{
							return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取价钱失败!");
						}
						rmb=StringUtil.string2Double(order.getUser().getRmbBalance());
						usd=StringUtil.string2Double(order.getUser().getUsdBalance());
						//String cur_usa_cn=this
						
						//if (hasPayMoney(usd, rmb, totalMoney)) {
						if (hasPayMoneyusa(usd, rmb, usdrate,totalMoney)) {
							double newusd = usd - totalMoney;
							double newrmb = rmb; // 先用美元支付
							if (newusd >= 0) {
								// ignore
							} else {
								
								if(usdrate==0)
								{
									return generateResponseObject(
											ResponseCode.ORDER_PAY_ACCOUNT_NOT_MONEY, "美元帐户余额不足");
								}
								
								newusd = 0.0D; // 人民币余额全部支付，开始扣美元的钱
								newrmb = new BigDecimal((rmb - (totalMoney - usd)*usdrate)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
								if(newrmb<0)
								{
									return generateResponseObject(
											ResponseCode.ORDER_PAY_ACCOUNT_NOT_MONEY, "帐户余额不足");
								}
							}
							//return this.m_orderService.userPayOne(id, order.getOrderId(), userId, String.valueOf(totalMoney), newrmb, newusd, true);
							ResponseObject<Object> obj= this.m_orderService.adminPayOne(id, order.getOrderId(), order.getUserId(), String.valueOf(totalMoney), storeName, storeId, empName, empNo, newrmb, newusd, true);
							if(obj!=null&&ResponseCode.SUCCESS_CODE.equalsIgnoreCase(obj.getCode()))//成功，清空之前的仓位
							{
								Shelves_position position=this.shelves_positionDao.getbyid(order.getPositionId());
								if(position==null)
								{
									return obj;
								}
								String date=DateUtil.date2String(new Date());
								this.shelves_positionDao.updatestate(position.getId(), Constant.POSITION_STATE0, date, "");
								this.m_orderDao.clear_position(position.getId(), date);
								obj.setMessage("删除仓位"+position.getPosition());
								return obj;
							}
							else
							{
								return obj;
							}
							
						
						} else {
							return generateResponseObject(
									ResponseCode.ORDER_PAY_ACCOUNT_NOT_MONEY, "帐户余额不足");
						}
					} catch (Exception e) {
						log.error("付款失败", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
								"付款失败，请重新尝试");
					}
				}
				
				
				
				//待处理运单
				@RequestMapping(value = "/admin/m_order/search_paybyuser",method = { RequestMethod.GET, RequestMethod.POST})
				@ResponseBody
				public ResponseObject<PageSplit<M_order>> searchpaybyuserByKeyOfAdmin(
						HttpServletRequest request,
						@RequestParam(value = "oid", required = false) String oid,//运单号
						@RequestParam(value = "sod", required = false) String sod,//海关单号，即本单号关联的海关单号
						@RequestParam(value = "god", required = false) String god,//国内第三方快递单号
						@RequestParam(value = "wid", required = false) String wid,//所属门店id
						@RequestParam(value = "cid", required = false, defaultValue = "") String cid,//渠道id
						@RequestParam(value = "userinfo", required = false) String userinfo,//用户信息
						@RequestParam(value = "commudityinfo", required = false) String commudityinfo,//商品信息
						@RequestParam(value = "state", required = false) String state,//状态
						@RequestParam(value = "type", required = false) String type,//类型
						@RequestParam(value = "flyno", required = false) String flyno,//航班号
						@RequestParam(value = "sdate", required = false) String sdate,//创建的起始时间
						@RequestParam(value = "edate", required = false) String edate,//创建的结束时间
						@RequestParam(value = "psdate", required = false) String psdate,//支付的起始时间
						@RequestParam(value = "pedate", required = false) String pedate,//支付的结束时间
						//@RequestParam(value = "payway", required = false) String payway,//支付方式
						@RequestParam(value = "cardinfo", required = false) String cardinfo,//身份证搜索选择
						@RequestParam(value = "payornot", required = false) String payornot,//是否已付款
						@RequestParam(value = "confirm_user_pay", required = false) String confirm_user_pay,//处理状态
						
						@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
						@RequestParam(value = "rows", required = false, defaultValue = "10") int rows
						) {
					try {
						//String state=Constant.ORDER_STATE__9;//
						String payway="2";
						if(StringUtil.isEmpty(payornot))
						{
							payornot=null;
						}
						else
						{
							if((!payornot.equalsIgnoreCase(Constant.ORDER_PAY_STATE0))&&(!payornot.equalsIgnoreCase(Constant.ORDER_PAY_STATE1)))
							{
								payornot=null;
							}
						}
						if(!StringUtil.isEmpty(confirm_user_pay)&&(confirm_user_pay.equalsIgnoreCase("-1")))
						{
							confirm_user_pay=null;
						}
						if(!StringUtil.isEmpty(wid)&&(wid.equalsIgnoreCase("-1")))
						{
							wid=null;
						}
						if(!StringUtil.isEmpty(cid)&&(cid.equalsIgnoreCase("-1")))
						{
							cid=null;
						}
						if(!StringUtil.isEmpty(state)&&(state.equalsIgnoreCase("-1")))
						{
							state=null;
						}
						if(!StringUtil.isEmpty(type)&&(type.equalsIgnoreCase("-1")))
						{
							type=null;
						}
						if(!StringUtil.isEmpty(payway)&&(payway.equalsIgnoreCase("-1")))
						{
							payway=null;
						}
						String storeid=null;
						String supperadmin = (String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
						if((supperadmin!=null)&&(supperadmin.equalsIgnoreCase("1")))
						{
							storeid=wid;//表示可以查找所有门店
							
						}else
						{
							storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
							if((!StringUtil.isEmpty(wid))&&(!wid.equalsIgnoreCase(storeid)))
							{
								return generateResponseObject(ResponseCode.PARAMETER_ERROR,
										"对不起，你不能访问其它门店!");
							}
							
							if((storeid==null)||(storeid.equalsIgnoreCase("")))
							{
								return generateResponseObject(ResponseCode.NEED_LOGIN,
										"你没有登陆!");
							}
						}
						
						
						/*if(userinfo!=null)
						{
						 userinfo = new String(request.getParameter("userinfo").getBytes("ISO-8859-1"), "utf-8");
							userinfo = URLDecoder.decode(userinfo, "UTF-8");
						}*/
						
						//key = new String(key.getBytes("ISO-8859-1"), "utf-8");
						if (StringUtil.isEmpty(sdate)
								|| !UserUtil.validateExportDate(sdate)) {
							sdate = "";
						} else {
							sdate = UserUtil.transformerDateString(sdate, " 00:00:00");
						}

						if (StringUtil.isEmpty(edate)
								|| !UserUtil.validateExportDate(edate)) {
							edate = "";
						} else {
							edate = UserUtil.transformerDateString(edate, " 23:59:59");
						}
						
						if (StringUtil.isEmpty(psdate)
								|| !UserUtil.validateExportDate(psdate)) {
							psdate = "";
						} else {
							psdate = UserUtil.transformerDateString(psdate, " 00:00:00");
						}

						if (StringUtil.isEmpty(pedate)
								|| !UserUtil.validateExportDate(pedate)) {
							pedate = "";
						} else {
							pedate = UserUtil.transformerDateString(pedate, " 23:59:59");
						}

						oid = StringUtil.isEmpty(oid) ? null : oid;
						sod = StringUtil.isEmpty(sod) ? null : sod;
						god = StringUtil.isEmpty(god) ? null : god;
						//String column = OrderUtil.getSearchColumnByType(type);
						state = OrderUtil.dealState(state);
						pageIndex = Math.max(pageIndex, 1);
						
						return this.m_orderService.searchMorderspaybyuser(oid, sod, god, flyno, storeid, cid, userinfo, commudityinfo, state, type,payway, sdate, edate, psdate, pedate,cardinfo,payornot,confirm_user_pay, rows, pageIndex);
								//.searchMordersnopay(oid, sod, god, flyno, storeid, cid, userinfo, commudityinfo, state, type,payway, sdate, edate, psdate, pedate,cardinfo,payornot, rows, pageIndex);
						//return new ResponseObject<PageSplit<Order>>();
						//return this.orderService.searchOrdersByKeys(oid, null, key, column,
						//		sdate, edate, state,typekey,storeid, defaultPageSize, pageIndex);
					} catch (Exception e) {
						log.error("查询运单失败", e);
						return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "查询失败");
					}
				}
				//待处理运单
				@RequestMapping(value = "/admin/m_order/confirm_user_pay",method = { RequestMethod.GET, RequestMethod.POST})
				@ResponseBody
				public ResponseObject<Object> changeconfirmid(
						HttpServletRequest request,
						@RequestParam(value = "confirm_user_pay", required = false) String confirm_user_pay,
						@RequestParam(value = "id", required = false) String id
						) {
							String storeid = (String)request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
							if(StringUtil.isEmpty(storeid))
							{
								return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作");
							}
							if(StringUtil.isEmpty(id))
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数错误");
							}
							if(Constant.USER_PAY_CONFIRM_0.equalsIgnoreCase(confirm_user_pay)||Constant.USER_PAY_CONFIRM_1.equalsIgnoreCase(confirm_user_pay))
							{
								
							}
							else
							{
								return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"状态参数错误");
							}
							
							try {
								
								String date=DateUtil.date2String(new Date());
								int k=this.m_orderDao.change_paybyuserstate(confirm_user_pay, date, id);
								if(k>0)
								{
									
									M_order morder=this.m_orderDao.getById(id, null);
									Shelves_position position=this.shelves_positionDao.getbyid(morder.getPositionId());
									if(position==null)
									{
										return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"修改成功！");
									}
									//String date=DateUtil.date2String(new Date());
									this.shelves_positionDao.updatestate(position.getId(), Constant.POSITION_STATE0, date, "");
									this.m_orderDao.clear_position(position.getId(), date);
									
									return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"删除仓位"+position.getPosition());
								}
								else
								{
									return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"修改失败！");
								}
							}catch(Exception e)
							{
								return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"修改发生异常！");
							}
				}
		
			
				
}
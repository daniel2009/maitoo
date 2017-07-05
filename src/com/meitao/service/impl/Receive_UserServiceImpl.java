package com.meitao.service.impl;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;














import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;














import com.meitao.cardid.manage.CardId_lib;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.ConsigneeInfoUtil;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.common.util.StringUtil;


import com.meitao.dao.CardIdManageDao;
import com.meitao.dao.Receive_UserDao;
import com.meitao.dao.globalargsDao;
import com.meitao.exception.ServiceException;
import com.meitao.model.PageSplit;
import com.meitao.model.Receive_User;
import com.meitao.model.ResponseObject;


@Service("receive_UserService")
public class Receive_UserServiceImpl implements com.meitao.service.Receive_UserService {
	@Autowired
	private Receive_UserDao receive_UserDao;
	
	@Value(value = "${save_cards_lib_dir}")
	private String saveCardsLibDir;
	
	@Autowired
	private CardIdManageDao dardIdManageDao;
	
	@Autowired
	private globalargsDao globalargsDao;


	public ResponseObject<PageSplit<Receive_User>> searchReceiveUserByInfo(String info, int pageSize, int pageNow)
	        throws ServiceException {
		try {
			if(!StringUtil.isEmpty(info))
			{
				info = StringUtil.escapeStringOfSearchKey(info);
			}
			else
			{
				info=null;
			}
			
			
			int rowCount = 0;
			try {
				rowCount = this.receive_UserDao.countOfReceiveUser(info);
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("获取运单个数失败", e);
			}

			ResponseObject<PageSplit<Receive_User>> responseObj = new ResponseObject<PageSplit<Receive_User>>(
					ResponseCode.SUCCESS_CODE);
			if (rowCount > 0) {
				pageSize = Math.max(pageSize, 1);
				int pageCount = rowCount / pageSize
						+ (rowCount % pageSize == 0 ? 0 : 1);
				pageNow = Math.min(pageNow, pageCount);
				PageSplit<Receive_User> pageSplit = new PageSplit<Receive_User>();
				pageSplit.setPageCount(pageCount);
				pageSplit.setPageNow(pageNow);
				pageSplit.setRowCount(rowCount);
				pageSplit.setPageSize(pageSize);

				int startIndex = (pageNow - 1) * pageSize;
				try {
					List<Receive_User> recerveusers =this.receive_UserDao.searchReceiveUser(info, startIndex, pageSize);
						
					pageSplit.setDatas(recerveusers);
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("获取接收用户列表失败", e);
				}
				responseObj.setData(pageSplit);
			} else {
				responseObj.setMessage("没有接收用户信息!");
			}
			return responseObj;
		} catch (ServiceException e) {
			throw e;
		}
	}
	public ResponseObject<Object> getOneVerfyMessage() throws ServiceException
	{
		try{
			Receive_User rec=this.receive_UserDao.getOneNeedverfyCardid();
			if(rec==null)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取信息发生异常");
			}
			
			ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			obj.setData(rec);
			return obj;
		}
		catch(Exception e)
		{
			return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"获取信息发生异常");
		}
	}
	//获取总的等待验证信息
	public ResponseObject<Object> getNumbersVerfyMessage() throws ServiceException
	{
		try{
			int a=this.receive_UserDao.get_allneedverfycardid();
			
			ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			obj.setData(a);
			return obj;
		}
		catch(Exception e)
		{
			return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"获取信息发生异常");
		}
	}
	
	//保存修改过后的验证方式，并要转移图片到身份证库中
	public ResponseObject<Object> saveverfyresult(String id,String modifyDate,String isPass,HttpServletRequest request) throws ServiceException
	{
		String date = DateUtil.date2String(new Date());// 修改时间
		try{
			
			
			Receive_User rev=this.receive_UserDao.getById(Integer.parseInt(id));
			if(rev==null)
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"获取信息失败!");
			}
			
			
			if(isPass.equalsIgnoreCase("false"))//验证失败
			{
				int k=this.receive_UserDao.updatecardid_flag(id, Constant.VERFY_CARDID_2, date);//标识验证失败
				if(k<1)
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"修改信息失败!");
				}
				else
				{
					return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"修改成功!");
				}
			}
			
			if(!StringUtil.isEmpty(modifyDate))
			{
				if(!modifyDate.equalsIgnoreCase(rev.getModifyDate()))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"信息已经被修改，请重新验证");
				}
			}
			else
			{
				if(!StringUtil.isEmpty(rev.getModifyDate()))
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"信息已经被修改，请重新验证");
				}
			}
			
			if (!ConsigneeInfoUtil.validateCardId(rev.getCardid())) {
				int k=this.receive_UserDao.updatecardid_flag(id, Constant.VERFY_CARDID_2, date);//标识验证失败
				return new ResponseObject<Object>(ResponseCode.CONSIGNEE_CARD_ID_ERROR,"身份证号码错误，提交失败！");
			}
			
			
			int k=0;
			try
			{
				k=this.dardIdManageDao.countbycardid(rev.getCardid());
			}
			catch (Exception e) {
				this.receive_UserDao.updatecardid_flag(id, Constant.VERFY_CARDID_2, date);//标识验证失败
				return new ResponseObject<Object>(ResponseCode.CONSIGNEE_CARD_ID_ERROR,"检查身份证号是否存在出现异常！");
			}
			if(k==0)
			{}
			else if(k==1)
			{
				List<CardId_lib> cards=this.dardIdManageDao.getbycardid(rev.getCardid().trim());
				if(cards!=null&&cards.size()>0)
				{
					if(cards.size()==1)
					{
						if(!rev.getName().trim().equalsIgnoreCase(cards.get(0).getCname().trim()))
						{
							k=this.receive_UserDao.updatecardid_flag(id, Constant.VERFY_CARDID_2, date);//标识验证失败
							return new ResponseObject<Object>(ResponseCode.CONSIGNEE_CARD_LIB_ERROR,"严重异常，与库存中相同身份证号但姓名却不相同的信息，请检查！");
						}
						
					}
					else
					{
						k=this.receive_UserDao.updatecardid_flag(id, Constant.VERFY_CARDID_2, date);//标识验证失败
						return new ResponseObject<Object>(ResponseCode.CONSIGNEE_CARD_LIB_ERROR,"严重异常，库中存在"+cards.size()+"个相同身份证号，请检查库存！");
					}
				}
				
				//判断是否覆盖之前的图片
				String flag=this.globalargsDao.getcontentbyflag("cardId_replace_old_pic");//1表示覆盖之前的身份证图片
				if(!StringUtil.isEmpty(flag)&&flag.equalsIgnoreCase("1"))//覆盖之前的身份证信息
				{
					
					File file1=new File(request.getSession().getServletContext()
							.getRealPath("/")+rev.getCardtogether());
					if(!file1.exists())
					{
						 k=this.receive_UserDao.updatecardid_flag(id, Constant.VERFY_CARDID_2, date);//标识验证失败
						return new ResponseObject<Object>(ResponseCode.CONSIGNEE_CARD_ID_ERROR,"验证失败，原合成图片不存在！");
					}
					
					String saveurl = request.getSession().getServletContext()
							.getRealPath("/")+this.saveCardsLibDir+"/pics/"+rev.getCardid().trim()+rev.getName().trim()+".jpg" ;
					
						
					   FileInputStream fis = new FileInputStream(request.getSession().getServletContext()
								.getRealPath("/")+rev.getCardtogether());
					   
				        FileOutputStream fos = new FileOutputStream(saveurl);
				 
				        int len = 0;
				        byte[] buf = new byte[1024];
				        while ((len = fis.read(buf)) != -1) {
				            fos.write(buf, 0, len);
				            fos.flush();
				        }
				        fis.close();
				        fos.flush();
				        fos.close();
				        fos=null;
				        fis=null;
				    String picurl=this.saveCardsLibDir+"/pics/"+rev.getCardid().trim()+rev.getName().trim()+".jpg";
					
					int kk=this.dardIdManageDao.updatepicurl(cards.get(0).getId(), picurl, date,rev.getPhone());
					if(kk<1)
					{
						this.receive_UserDao.updatecardid_flag(id, Constant.VERFY_CARDID_2, date);//标识验证失败
						return new ResponseObject<Object>(ResponseCode.CONSIGNEE_CARD_ID_ERROR,"更新已有信息失败");
					}
					//判断之前是否已经有库存信息，如果有，将不更新
					if(StringUtil.isEmpty(rev.getCardlibId()))
					{
						rev.setCardlibId(cards.get(0).getId());
						rev.setSecondName(rev.getName());
						rev.setRemark("身份证库存id:"+cards.get(0).getId());
						this.receive_UserDao.modifymatchinfo(rev);
					}
					
					
					
					
					this.receive_UserDao.updatecardid_flag(id, Constant.VERFY_CARDID_1, date);//标识验证失败
					return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"身份证号已经存在,并替换成功！");
				}
				
				//判断之前是否已经有库存信息，如果有，将不更新
				if(StringUtil.isEmpty(rev.getCardlibId()))
				{
					rev.setCardlibId(cards.get(0).getId());
					rev.setSecondName(rev.getName());
					rev.setRemark("身份证库存id:"+cards.get(0).getId());
					this.receive_UserDao.modifymatchinfo(rev);
				}
				
				this.receive_UserDao.updatecardid_flag(id, Constant.VERFY_CARDID_1, date);//标识验证失败
				return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"身份证号已经存在！");
			}
			else
			{
				k=this.receive_UserDao.updatecardid_flag(id, Constant.VERFY_CARDID_2, date);//标识验证失败
				return new ResponseObject<Object>(ResponseCode.CONSIGNEE_CARD_LIB_ERROR,"严重异常，库中存在"+k+"个相同身份证号，请检查库存！");
			}
			
			//验证图片是否存在并转移
			
			
			
			File file=new File(request.getSession().getServletContext()
					.getRealPath("/")+rev.getCardtogether());
			if(!file.exists())
			{
				 k=this.receive_UserDao.updatecardid_flag(id, Constant.VERFY_CARDID_2, date);//标识验证失败
				return new ResponseObject<Object>(ResponseCode.CONSIGNEE_CARD_ID_ERROR,"验证失败，原合成图片不存在！");
			}
			
			String saveurl = request.getSession().getServletContext()
					.getRealPath("/")+this.saveCardsLibDir+"/pics/"+rev.getCardid().trim()+rev.getName().trim()+".jpg" ;
			
				
			   FileInputStream fis = new FileInputStream(request.getSession().getServletContext()
						.getRealPath("/")+rev.getCardtogether());
			   
		        FileOutputStream fos = new FileOutputStream(saveurl);
		 
		        int len = 0;
		        byte[] buf = new byte[1024];
		        while ((len = fis.read(buf)) != -1) {
		            fos.write(buf, 0, len);
		            fos.flush();
		        }
		        fis.close();
		        fos.flush();
		        fos.close();
		        fos=null;
		        fis=null;
		        

				
				
				
				
				CardId_lib carid=new CardId_lib();
				carid.setCname(rev.getName());
				carid.setCardid(rev.getCardid());
				carid.setPicurl(this.saveCardsLibDir+"/pics/"+rev.getCardid().trim()+rev.getName().trim()+".jpg");
				carid.setCreateDate(DateUtil.date2String(new Date()));
				carid.setModifyDate(DateUtil.date2String(new Date()));
				carid.setPhone(rev.getPhone());
				try
				{
					int kk=this.dardIdManageDao.insertcaridinfo(carid);
					
					if(kk<1)
					{
						this.receive_UserDao.updatecardid_flag(id, Constant.VERFY_CARDID_2, date);//标识验证失败
						return new ResponseObject<Object>(ResponseCode.CONSIGNEE_CARD_ID_ERROR,"插入数据库失败！");
					}
					else
					{
						
						//判断之前是否已经有库存信息，如果有，将不更新
						if(StringUtil.isEmpty(rev.getCardlibId()))
						{
							rev.setCardlibId(carid.getId());
							rev.setSecondName(rev.getName());
							rev.setRemark("身份证库存id:"+carid.getId());
							this.receive_UserDao.modifymatchinfo(rev);
						}
						this.receive_UserDao.updatecardid_flag(id, Constant.VERFY_CARDID_1, date);//标识插入成功
						return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"操作成功！");
					}
				}
				catch (Exception e) {
					this.receive_UserDao.updatecardid_flag(id, Constant.VERFY_CARDID_2, date);//标识验证失败
					return new ResponseObject<Object>(ResponseCode.CONSIGNEE_CARD_ID_ERROR,"插入发生异常！");
				}
				
			
			
			//arg.setAllfileurl("");
		
			
			
			//ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			//obj.setData(a);
			//return obj;
		}
		catch(Exception e)
		{
			this.receive_UserDao.updatecardid_flag(id, Constant.VERFY_CARDID_2, date);//获取信息发生异常，验证失败
			return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,"获取信息发生异常，验证失败");
		}
	}
}

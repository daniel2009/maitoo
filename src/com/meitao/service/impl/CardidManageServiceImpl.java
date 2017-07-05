package com.meitao.service.impl;

import java.io.File;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;










import com.meitao.cardid.manage.CardId_lib;
import com.meitao.cardid.manage.import_t_orders;
import com.meitao.cardid.manage.importcardargs;

import com.meitao.dao.CardIdManageDao;



import com.meitao.service.CardidManageService;

import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;

import com.meitao.common.util.StringUtil;
import com.meitao.exception.ServiceException;

import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;


@Service("cardidManageService")
public class CardidManageServiceImpl implements CardidManageService {
	@Autowired
	private CardIdManageDao dardIdManageDao;



	public ResponseObject<Object> addcardids(List<importcardargs> importflyInfo) throws ServiceException {
		if (importflyInfo == null) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
					"参数无效");
		}
		try {
			
			for(importcardargs arg:importflyInfo)
			{
				if(!arg.isFlag())
				{
					
				}
				else if(StringUtil.isEmpty(arg.getCardid()))
				{
					arg.setFlag(false);
					arg.setResult("失败!身份证号不能为空");
				}
				else if(StringUtil.isEmpty(arg.getCardname()))
				{
					arg.setFlag(false);
					arg.setResult("失败!姓名不能为空");
				}
				else if(StringUtil.isEmpty(arg.getSaveurl()))
				{
					arg.setFlag(false);
					arg.setResult("失败!图片路径不能为空");
				}
				else
				{
					int k=0;
					try
					{
						k=this.dardIdManageDao.countbycardid(arg.getCardid());
					}
					catch (Exception e) {
						arg.setFlag(false);
						arg.setResult("失败!查询数据库异常！");
					}
					
					if(k>0)
					{
						arg.setFlag(false);
						arg.setResult("失败!身份证号已经存在！");
					}
					else
					{
						CardId_lib carid=new CardId_lib();
						carid.setCname(arg.getCardname());
						carid.setCardid(arg.getCardid());
						carid.setPicurl(arg.getSaveurl());
						carid.setCreateDate(DateUtil.date2String(new Date()));
						carid.setModifyDate(DateUtil.date2String(new Date()));
						try
						{
							int kk=this.dardIdManageDao.insertcaridinfo(carid);
							
							if(kk<1)
							{
								arg.setFlag(false);
								arg.setResult("失败!插入数据库失败！");
							}
							else
							{
								arg.setFlag(true);
								arg.setResult("成功！");
							}
						}
						catch (Exception e) {
							arg.setFlag(false);
							arg.setResult("失败!插入数据库异常！");
						}
					}
				}
			}
			
			ResponseObject<Object> obj= new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			obj.setData(importflyInfo);

			return obj;
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}

	public ResponseObject<PageSplit<CardId_lib>> searchbykey(String content,HttpServletRequest request, int pageSize, int pageNow) throws ServiceException
	{
		try {
			if(!StringUtil.isEmpty(content) )
			{
				content = StringUtil.escapeStringOfSearchKey(content);
			}
			else
			{
				content=null;
			}
			

			int rowCount = 0;
			try {
				rowCount=this.dardIdManageDao.countOfSearchKeys(content);
				//rowCount = this.endicialLabelDao.countByKey(userId, userInfo, labelInfo, sdate, edate);
						
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("获取个数失败", e);
			}

			ResponseObject<PageSplit<CardId_lib>> responseObj = new ResponseObject<PageSplit<CardId_lib>>(
					ResponseCode.SUCCESS_CODE);
			if (rowCount > 0) {
				pageSize = Math.max(pageSize, 1);
				int pageCount = rowCount / pageSize
						+ (rowCount % pageSize == 0 ? 0 : 1);
				pageNow = Math.min(pageNow, pageCount);
				PageSplit<CardId_lib> pageSplit = new PageSplit<CardId_lib>();
				pageSplit.setPageCount(pageCount);
				pageSplit.setPageNow(pageNow);
				pageSplit.setRowCount(rowCount);
				pageSplit.setPageSize(pageSize);

				int startIndex = (pageNow - 1) * pageSize;
				try {
					List<CardId_lib> endicial00 = this.dardIdManageDao.searchByKeys(content,  startIndex, pageSize);
					for(CardId_lib lib:endicial00)
					{
						if(StringUtil.isEmpty(lib.getPicurl()))
						{
							lib.setPicurl("无");
						}
						else
						{
							File file3 = new File(request.getSession().getServletContext()
									.getRealPath("/")
									+ lib.getPicurl());
							if (file3.exists()) {
								//lib.setPicurl("有");
								lib.setPicflag("有");
							} else {
								//lib.setPicurl("无");
								lib.setPicflag("无");
							}
						}
					}
					
							
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
	
	public ResponseObject<Object> deleteonecard(String id,HttpServletRequest request) throws ServiceException
	{
		if (StringUtil.isEmpty(id)) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
					"参数无效");
		}
		try{
		CardId_lib lib=	this.dardIdManageDao.getbyid(id);
		if(lib==null)
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
					"获取数据失败!");
		}
		else
		{
			if(StringUtil.isEmpty(lib.getPicurl()))
			{
				
			}
			else
			{
				File file3 = new File(request.getSession().getServletContext()
						.getRealPath("/")
						+ lib.getPicurl());
				if (file3.exists()) {
					file3.delete();
				} else {
					
				}
			}
		}
		
		int k=this.dardIdManageDao.deleteonecard(id);
		if(k<1)
		{
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
					"删除失败!");
		}
		else
		{
			
		}
		
		ResponseObject<Object> obj= new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
		

		return obj;
		}catch(Exception e)
		{
			return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION);
		}
		
	}
	
	public ResponseObject<Object> verifycardid(List<import_t_orders> imorders,String rule,HttpServletRequest request) throws ServiceException
	{
		try{
		int maxid=0;
		int minid=0;
		for(import_t_orders order:imorders)
		{
			if(order==null)
			{
				continue;
			}
			List<CardId_lib> cardidlist=null;			
			cardidlist=this.dardIdManageDao.getcardidbyname(order.getR_name());//根据收货人来搜索
			java.util.Random random=new java.util.Random();// 定义随机类
			if((cardidlist!=null)&&(cardidlist.size()>0))//查找到用户
			{
				if(cardidlist.size()>1)//包含多个，要模糊查找
				{
					int num=0;
					CardId_lib lib=null;
					//先判断电话是否相同，如果相同，将优先选择
					for(int k=0;k<cardidlist.size();k++)
					{
						num=k;
						lib=cardidlist.get(k);
						if(lib!=null&&!StringUtil.isEmpty(cardidlist.get(k).getPhone())&&(cardidlist.get(k).getPhone().equalsIgnoreCase(order.getR_phone())))//
						{
							if((!StringUtil.isEmpty(lib.getPicurl()))&&(!StringUtil.isEmpty(lib.getCardid())))
							{
								File file3 = new File(request.getSession().getServletContext()
										.getRealPath("/")
										+ lib.getPicurl());
								if (file3.exists()) {
									order.setResultflag(true);
									order.setResult("匹配成功");
									order.setCardurl(lib.getPicurl());
									order.setCardid(lib.getCardid());
									order.setCardname(lib.getCname());
									order.setCardlibId(lib.getId());
									
									break;
								} else {
									
								}
							}
							
						}
					}
					if(order.isResultflag()!=true)
					{
						num=random.nextInt(cardidlist.size());// 返回[0,10)集合中的整数，注意不包括10
						lib=cardidlist.get(num);//获取随机的姓名
						if(lib!=null)
						{
							if((!StringUtil.isEmpty(lib.getPicurl()))&&(!StringUtil.isEmpty(lib.getCardid())))
							{
								File file3 = new File(request.getSession().getServletContext()
										.getRealPath("/")
										+ lib.getPicurl());
								if (file3.exists()) {
									order.setResultflag(true);
									order.setResult("匹配成功");
									order.setCardurl(lib.getPicurl());
									order.setCardid(lib.getCardid());
									order.setCardname(lib.getCname());
									order.setCardlibId(lib.getId());
									
									
								} else {
									
								}
							}
							
						}
					}
					//随机取的没有匹配到图片，再查找其它的
					if(!order.isResultflag())
					{
						for(int k=0;k<cardidlist.size();k++)
						{
							if(k!=num)
							{
								lib=cardidlist.get(k);//获取随机的姓名
								if(lib!=null)
								{
									if((!StringUtil.isEmpty(lib.getPicurl()))&&(!StringUtil.isEmpty(lib.getCardid())))
									{
										File file3 = new File(request.getSession().getServletContext()
												.getRealPath("/")
												+ lib.getPicurl());
										if (file3.exists()) {
											order.setResultflag(true);
											order.setResult("匹配成功");
											order.setCardurl(lib.getPicurl());
											order.setCardid(lib.getCardid());
											order.setCardname(lib.getCname());
											order.setCardlibId(lib.getId());
										} else {
											
										}
									}
									
								}
							}
						}
					}
					
					
				}
				else//只查到一个
				{
					CardId_lib lib=cardidlist.get(0);//获取随机的姓名
					if(lib!=null)
					{
						if((!StringUtil.isEmpty(lib.getPicurl()))&&(!StringUtil.isEmpty(lib.getCardid())))
						{
							File file3 = new File(request.getSession().getServletContext()
									.getRealPath("/")
									+ lib.getPicurl());
							if (file3.exists()) {
								order.setResultflag(true);
								order.setResult("匹配成功");
								order.setCardurl(lib.getPicurl());
								order.setCardid(lib.getCardid());
								order.setCardname(lib.getCname());
								order.setCardlibId(lib.getId());
							} else {
								
							}
						}
						
					}
				}
				
				
				
			}
		
			
			
			//根据姓名没有匹配成功
			
			if((!order.isResultflag())&&(rule!=null)&&(rule.equalsIgnoreCase("1"))&&(!StringUtil.isEmpty(order.getR_name())))//模糊匹配
			{
				int length=order.getR_name().length();
				if(length>0)
				{
					for(int n=length;n>0;n--)
					{
						String searchname=order.getR_name().substring(0,n);
						if(!StringUtil.isEmpty(searchname))
						{
							searchname = searchname+"%";
							cardidlist=this.dardIdManageDao.getcardidbysearcnames(searchname);
							if((cardidlist!=null)&&(cardidlist.size()>0))//查找到用户
							{
								if(cardidlist.size()>1)//包含多个，要模糊查找
								{
									
									int num=random.nextInt(cardidlist.size());// 返回[0,10)集合中的整数，注意不包括10
									CardId_lib lib=cardidlist.get(num);//获取随机的姓名
									if(lib!=null)
									{
										if((!StringUtil.isEmpty(lib.getPicurl()))&&(!StringUtil.isEmpty(lib.getCardid())))
										{
											File file3 = new File(request.getSession().getServletContext()
													.getRealPath("/")
													+ lib.getPicurl());
											if (file3.exists()) {
												order.setResultflag(true);
												order.setResult("模糊匹配成功");
												order.setCardurl(lib.getPicurl());
												order.setCardid(lib.getCardid());
												order.setCardname(lib.getCname());
												order.setCardlibId(lib.getId());
												
											} else {
												
											}
										}
										
									}
									//随机取的没有匹配到图片，再查找其它的
									if(!order.isResultflag())
									{
										for(int k=0;k<cardidlist.size();k++)
										{
											if(k!=num)
											{
												lib=cardidlist.get(k);//获取随机的姓名
												if(lib!=null)
												{
													if((!StringUtil.isEmpty(lib.getPicurl()))&&(!StringUtil.isEmpty(lib.getCardid())))
													{
														File file3 = new File(request.getSession().getServletContext()
																.getRealPath("/")
																+ lib.getPicurl());
														if (file3.exists()) {
															order.setResultflag(true);
															order.setResult("模糊匹配成功");
															order.setCardurl(lib.getPicurl());
															order.setCardid(lib.getCardid());
															order.setCardname(lib.getCname());
															order.setCardlibId(lib.getId());
														} else {
															
														}
													}
													
												}
											}
										}
									}
									
									
								}
								else//只查到一个
								{
									CardId_lib lib=cardidlist.get(0);//获取随机的姓名
									if(lib!=null)
									{
										if((!StringUtil.isEmpty(lib.getPicurl()))&&(!StringUtil.isEmpty(lib.getCardid())))
										{
											File file3 = new File(request.getSession().getServletContext()
													.getRealPath("/")
													+ lib.getPicurl());
											if (file3.exists()) {
												order.setResultflag(true);
												order.setResult("模糊匹配成功");
												order.setCardurl(lib.getPicurl());
												order.setCardid(lib.getCardid());
												order.setCardname(lib.getCname());
												order.setCardlibId(lib.getId());
											} else {
												
											}
										}
										
									}
								}
								
								
								
							}
						}
					}
				}
				
				//模糊查找不成功，随机查找
				if(order.isResultflag())
				{
					continue;
				}
				
				if(maxid==0)
				{
					String maxid1=this.dardIdManageDao.getmaxid();
					if(!StringUtil.isEmpty(maxid1))
					{
						maxid= Integer.parseInt(maxid1);
					}
				}
				if(minid==0)
				{
					String mini1=this.dardIdManageDao.getminid();
					if(!StringUtil.isEmpty(mini1))
					{
						minid= Integer.parseInt(mini1);
					}
				}
				
				for(int kk=0;kk<20;kk++)//随机查找不超过20次
				{
					double number=0;
					if(minid<=maxid)
					{
						 number=((maxid-minid)*Math.random())+minid;
					}
					CardId_lib lib=null;
					//java.util.Random random1=new java.util.Random();// 定义随机类
					lib=this.dardIdManageDao.getonebyrand((int)number);//随机获取一条数据
					if(lib!=null)
					{
						if((!StringUtil.isEmpty(lib.getPicurl()))&&(!StringUtil.isEmpty(lib.getCardid())))
						{
							File file3 = new File(request.getSession().getServletContext()
									.getRealPath("/")
									+ lib.getPicurl());
							if (file3.exists()) {
								order.setResultflag(true);
								order.setResult("随机匹配成功");
								order.setCardurl(lib.getPicurl());
								order.setCardid(lib.getCardid());
								order.setCardname(lib.getCname());
								order.setCardlibId(lib.getId());
								break;
							} else {
								
							}
						}
						
					}
				}
				
			}
			
			
			if(!order.isResultflag())//最后还是不成功
			{
				order.setResult("匹配失败");
			}
			
		}
		
		ResponseObject<Object> obj= new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
		obj.setData(imorders);

		return obj;
		}catch(Exception e)
		{
			return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION);
		}
		
	}

}

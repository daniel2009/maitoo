package com.meitao.service.impl;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitao.dao.CardIdDao;


import com.meitao.dao.PorderDao;
import com.meitao.dao.RouteDao;
import com.meitao.dao.SeaprintDao;
import com.meitao.dao.WarehouseDao;

import com.meitao.service.SeaprintService;

import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.ConsigneeInfoUtil;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;

import com.meitao.common.util.StringUtil;
import com.meitao.exception.ServiceException;
import com.meitao.model.CardId;



import com.meitao.model.PageSplit;
import com.meitao.model.Porder;
import com.meitao.model.ResponseObject;

import com.meitao.model.Seaprint;

import com.meitao.model.Warehouse;


@Service("seaprintService")
public class SeaprintServiceImpl implements SeaprintService {
	@Autowired
	private SeaprintDao seaprintDao;
	
	@Autowired
	private RouteDao routeDao;
	@Autowired
	private WarehouseDao warehouseDao;

	@Autowired
	private CardIdDao cardIdDao;
	
	@Autowired
	private PorderDao porderDao;
	

	public ResponseObject<Object> addSeaprint(Seaprint seaprint)
			throws ServiceException {
		if (seaprint == null) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
					"参数无效");
		}
		try {
			if (StringUtil.isEmpty(seaprint.getSeaprintno())) {
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
						"参数无效");
			}
			seaprint.setCreateDate(DateUtil.date2String(new Date()));
			seaprint.setModifyDate(DateUtil.date2String(new Date()));

			int k = this.seaprintDao
					.countbyseaprintno(seaprint.getSeaprintno());// 检查是否存在此批次号
			if (k > 0) {
				return new ResponseObject<Object>(
						ResponseCode.WAREHOUSE_FLYINFO_FAILURE, "插入失败,批次号已经存在！");
			}
			int i = this.seaprintDao.insertSeaprint(seaprint);

			if (i > 0) {
				return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			} else {
				return new ResponseObject<Object>(
						ResponseCode.WAREHOUSE_FLYINFO_FAILURE, "插入失败");
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}

	// 航班批次查询
	public ResponseObject<PageSplit<Seaprint>> searchSeaprint(
			String seaprintno, int pageSize, int pageNow, String storeId)
			throws ServiceException {
		try {

			if (!StringUtil.isEmpty(seaprintno)) {
				seaprintno = StringUtil.escapeStringOfSearchKey(seaprintno);
			}

			int rowCount = 0;
			try {
				rowCount = this.seaprintDao.countOfSearchKeys(seaprintno,
						storeId);
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("获取批次个数失败", e);
			}

			ResponseObject<PageSplit<Seaprint>> responseObj = new ResponseObject<PageSplit<Seaprint>>(
					ResponseCode.SUCCESS_CODE);
			if (rowCount > 0) {
				pageSize = Math.max(pageSize, 1);
				int pageCount = rowCount / pageSize
						+ (rowCount % pageSize == 0 ? 0 : 1);
				pageNow = Math.min(pageNow, pageCount);
				PageSplit<Seaprint> pageSplit = new PageSplit<Seaprint>();
				pageSplit.setPageCount(pageCount);
				pageSplit.setPageNow(pageNow);
				pageSplit.setRowCount(rowCount);
				pageSplit.setPageSize(pageSize);

				int startIndex = (pageNow - 1) * pageSize;
				try {
					List<Seaprint> flyinfos = this.seaprintDao.searchByKeys(
							seaprintno, startIndex, pageSize, storeId);

					if (flyinfos != null && !flyinfos.isEmpty()) {
						for (Seaprint o : flyinfos) {

							String temp=Integer.toString(this.porderDao.countOfSearchKeys(o.getId(), "", "", "", ""));
							 o.setPordersno(temp);

							if (!StringUtil.isEmpty(o.getWarehouseId())) {
								Warehouse house = this.warehouseDao.getById(o
										.getWarehouseId());
								if (house != null) {
									o.setWarehouseName(house.getName());
								}
							}

							pageSplit.addData(o);
						}
					}
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("获取航班表失败", e);
				}
				responseObj.setData(pageSplit);
			} else {
				responseObj.setMessage("没有航班信息!");
			}
			return responseObj;
		} catch (ServiceException e) {
			throw e;
		}
	}

	public ResponseObject<Object> getbyid(String id, String storeId)
			throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
					"参数无效");
		}
		try {

			Seaprint seaprint = this.seaprintDao.getbyid(id, storeId);
			if (seaprint == null) {
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
						"获取批次信息失败!");
			}
			if (!StringUtil.isEmpty(seaprint.getWarehouseId())) {
				Warehouse house = this.warehouseDao.getById(seaprint
						.getWarehouseId());
				if (house != null) {
					seaprint.setWarehouseName(house.getName());
				}
			}

			ResponseObject<Object> obj = new ResponseObject<Object>(
					ResponseCode.SUCCESS_CODE);
			obj.setData(seaprint);
			return obj;

		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}

	public ResponseObject<Object> deleteSeaprintById(String id)
			throws ServiceException {
		if (id == null || id.equalsIgnoreCase("")) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
					"参数无效");
		}

		try {

			int i = this.seaprintDao.deleteseaprint(id);
			if (i > 0) {
				this.porderDao.deletebyseaprintid(id);
				
				return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			} else {
				throw new Exception("删除失败!");
			}

		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}

	public ResponseObject<Object> importcardids(List<CardId> cardids)
			throws ServiceException {
		try {
			String date = DateUtil.date2String(new Date());
		//	List<Order> orders = new ArrayList<Order>();

			for (CardId io : cardids) {
				if (StringUtil.isEmpty(io.getCardid())) {
					io.setResult("失败:身份证号为空!");

					continue;
				} else {
					if (!ConsigneeInfoUtil.validateCardId(io.getCardid())) {
						io.setResult("失败:身份证号填写不正确!");
						continue;
					} else {
						// 检查身份证是否存在
						int k=0;
						try {
						 k = this.cardIdDao.countbycardid(io.getCardid());
						}
						catch (Exception e) {
							throw ExceptionUtil
									.handle2ServiceException("查询数据库出现异常，请检查姓名为："
											+ io.getCname() + ",身份证号为：" + io.getCardid()
											+ " 所在的行!");
						}
						if (k > 0) {
							io.setResult("失败:身份证号已经存在!");
							continue;
						}
					}
				}

				if (StringUtil.isEmpty(io.getCname())) {
					io.setResult("失败:姓名不能为空!");

					continue;
				}

				int kk = 0;
				try {
					io.setCreateDate(date);
					io.setModifyDate(date);
					kk = this.cardIdDao.insertcaridinfo(io);
				} catch (Exception e) {
					//throw new Exception();
					throw ExceptionUtil
							.handle2ServiceException("插入数据库出现异常，请检查姓名为："
									+ io.getCname() + ",身份证号为：" + io.getCardid()
									+ " 所在的行!");
				}
				if (kk > 0) {
					io.setResult("成功!");
				} else {
					io.setResult("失败!");
				}

			}

		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
		ResponseObject<Object> obj = new ResponseObject<Object>(
				ResponseCode.SUCCESS_CODE);
		obj.setData(cardids);
		return obj;
	}
	
	
	//导入打印海关单
	public ResponseObject<Object> importporders(List<Porder> porder,String seaprintid) throws ServiceException {
		try {
			
			if (StringUtil.isEmpty(seaprintid)) {
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
						"参数无效");
			}
			
			//String date = DateUtil.date2String(new Date());
			//List<Order> orders = new ArrayList<Order>();

			for (Porder io : porder) {
				if (StringUtil.isEmpty(io.getOrderId())) {
					io.setResult("失败:运单号不能为空!");
					continue;
				} 
				
				if (StringUtil.isEmpty(io.getDivideOrderId())) {
					io.setResult("失败:海关单号不能为空!");
					continue;
				} 

				if (StringUtil.isEmpty(io.getCphone())) {
					io.setResult("失败:收件人电话不能为空!");
					continue;
				}

				int kk = 0;
				try {
					io.setCreateDate(DateUtil.date2String(new Date()));
					io.setModifyDate(DateUtil.date2String(new Date()));
					io.setPrintflag("0");
					
					int num=this.porderDao.countbyorderId(io.getOrderId(), null);
					if(num>0)
					{
						io.setResult("失败:此运单号在系统中已经存在，请检查是否正确!");
						continue;
					}
					io.setSeaprintId(seaprintid);
					kk = this.porderDao.insertPorder(io);
				} catch (Exception e) {
					
					throw ExceptionUtil
							.handle2ServiceException("插入数据库出现异常，请检查运单号为："
									+ io.getOrderId() + ",海关单号：" + io.getDivideOrderId()
									+ " 所在的行!");
				}
				if (kk > 0) {
					io.setResult("成功!");
				} else {
					io.setResult("失败!");
				}

			}

		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
		ResponseObject<Object> obj = new ResponseObject<Object>(
				ResponseCode.SUCCESS_CODE);
		obj.setData(porder);
		return obj;
	}
	
	
	
	// 海关打单清单列表
	public ResponseObject<PageSplit<Porder>> searchPorders(String seaprintno,String porder,String content,String state, int pageSize, int pageNow,String storeId) throws ServiceException {
			try {

				if(StringUtil.isEmpty(seaprintno))
				{
					return new ResponseObject<PageSplit<Porder>>(ResponseCode.PARAMETER_ERROR,
							"参数无效");
				}
				if (!StringUtil.isEmpty(porder)) {
					porder = StringUtil.escapeStringOfSearchKey(porder);
				}
				
				if (!StringUtil.isEmpty(content)) {
					content = StringUtil.escapeStringOfSearchKey(content);
				}

				int rowCount = 0;
				try {
					rowCount = this.porderDao.countOfSearchKeys(seaprintno, porder, state, content, null);
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("获取批次个数失败", e);
				}

				ResponseObject<PageSplit<Porder>> responseObj = new ResponseObject<PageSplit<Porder>>(
						ResponseCode.SUCCESS_CODE);
				if (rowCount > 0) {
					pageSize = Math.max(pageSize, 1);
					int pageCount = rowCount / pageSize
							+ (rowCount % pageSize == 0 ? 0 : 1);
					pageNow = Math.min(pageNow, pageCount);
					PageSplit<Porder> pageSplit = new PageSplit<Porder>();
					pageSplit.setPageCount(pageCount);
					pageSplit.setPageNow(pageNow);
					pageSplit.setRowCount(rowCount);
					pageSplit.setPageSize(pageSize);

					int startIndex = (pageNow - 1) * pageSize;
					try {
						
						
						
						List<Porder> flyinfos = this.porderDao.searchByKeys(seaprintno, porder, state, content, null, startIndex, pageSize);

						if (flyinfos != null && !flyinfos.isEmpty()) {
							for (Porder o : flyinfos) {

								int flag=0;
								if(!StringUtil.isEmpty(o.getCardId()))
								{
									if (ConsigneeInfoUtil.validateCardId(o.getCardId()))
									{
										flag=1;
									}
									else
									{
										o.setCardId("");
									}
								}
								if(flag==0)//没有身份证，将根据姓名查找
								{
									List<CardId> cards=this.cardIdDao.getcardidbyname(o.getCname());
									if((cards!=null)&&(cards.size()>0))
									{
										if(cards.size()==1)
										{
											String cardid=cards.get(0).getCardid();
											o.setCardId(cardid);
											o.setMarkresult("匹配身份证");
										}
										else
										{
											if(!StringUtil.isEmpty(o.getCprovince()))
											{
												List<CardId> list1 = new ArrayList<CardId>();//保存有相同省份的人
												
												for(CardId id:cards)
												{
													if(o.getCprovince().equalsIgnoreCase(id.getProvince()))
													{
														list1.add(id);
													}
												}
												
												
												if(((list1!=null)&&(list1.size()>0))&&(!StringUtil.isEmpty(o.getCcity())))
												{
													if(list1.size()==1)
													{
														String cardid=list1.get(0).getCardid();
														o.setCardId(cardid);
														o.setMarkresult("匹配身份证");
													}
													else
													{
														int flagc=-1;
														for(int c=0;c<list1.size();c++)
														{
															
															if(o.getCcity().equalsIgnoreCase(list1.get(c).getCity()))
															{
																if(flagc<0)
																{
																	flagc=c;
																}
															}
														}
														if(flagc<0)
														{
															flagc=0;
														}
														
														String cardid=list1.get(flagc).getCardid();
														o.setCardId(cardid);
														o.setMarkresult("匹配身份证");
													}
												}
												else
												{
													String cardid=cards.get(0).getCardid();
													if((list1!=null)&&(list1.size()>0))
													{
														cardid=list1.get(0).getCardid();
													}
													
													o.setCardId(cardid);
													o.setMarkresult("匹配身份证");
												}
											}
											else
											{
												String cardid=cards.get(0).getCardid();
												o.setCardId(cardid);
												o.setMarkresult("匹配身份证");
											}
										}
										
										
									}
								}

								pageSplit.addData(o);
							}
						}
					} catch (Exception e) {
						throw ExceptionUtil.handle2ServiceException("获取航班表失败", e);
					}
					responseObj.setData(pageSplit);
				} else {
					responseObj.setMessage("没有运单信息!");
				}
				return responseObj;
			} catch (ServiceException e) {
				throw e;
			}
		}
	
	
	//获取一条海关运单
	public ResponseObject<Object> getporderbyid(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
					"参数无效");
		}
		try {

			Porder porder=this.porderDao.getbyid(id);
			
			if (porder == null) {
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
						"获取运单信息失败!");
			}
			

			ResponseObject<Object> obj = new ResponseObject<Object>(
					ResponseCode.SUCCESS_CODE);
			obj.setData(porder);
			return obj;

		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}

	
	//修改运单状态为打
	public ResponseObject<Object> modifyporderstate(String id,String state) throws ServiceException{
			if (StringUtil.isEmpty(id)) {
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
						"参数无效");
			}
			try {

				int k=this.porderDao.modifystate(id, state, DateUtil.date2String(new Date()));
				
				if(k<1)
				{
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
							"更新状态失败");
				}
				

				ResponseObject<Object> obj = new ResponseObject<Object>(
						ResponseCode.SUCCESS_CODE);
				//obj.setData(porder);
				return obj;

			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
	
	
	
	//删除一条运单
	public ResponseObject<Object> deleteporderbyid(String id) throws ServiceException{
				if (StringUtil.isEmpty(id)) {
					return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
							"参数无效");
				}
				try {

					int k=this.porderDao.deletebyid(id);
					
					if(k<1)
					{
						return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
								"删除失败!");
					}
					

					ResponseObject<Object> obj = new ResponseObject<Object>(
							ResponseCode.SUCCESS_CODE);
					//obj.setData(porder);
					return obj;

				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException(e);
				}
			}
}

package com.meitao.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitao.dao.CommudityPriceDao;
import com.meitao.dao.EmployeeDao;
import com.meitao.dao.TranPriceDao;
import com.meitao.dao.WarehouseDao;
import com.meitao.service.WarehouseService;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.exception.ServiceException;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.TranPrice;
import com.meitao.model.Warehouse;

@Service("warehouseService")
public class WarehouseServiceImpl implements WarehouseService {
	@Autowired
	private WarehouseDao warehouseDao;

	@Autowired
	private CommudityPriceDao commudityPriceDao;//管理员添加的商品管理
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private TranPriceDao tranPriceDao;
	public ResponseObject<Object> addWarehouse(Warehouse warehouse) throws ServiceException {
		if (warehouse == null) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR, "参数无效");
		}
		try {
			//if (StringUtil.isEmpty(warehouse.getSerialNo())) {
				//warehouse.setSerialNo(String.valueOf(Integer.MAX_VALUE));
		//	}
			warehouse.setCreateDate(DateUtil.date2String(new Date()));
			warehouse.setModifyDate(DateUtil.date2String(new Date()));
			int i = this.warehouseDao.insertWarehouse(warehouse);
			if (i > 0) {
				return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			} else {
				return new ResponseObject<Object>(ResponseCode.WAREHOUSE_INSERT_FAILURE, "插入失败");
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}

	public ResponseObject<List<Warehouse>> getAll() throws ServiceException {
		try {
			List<Warehouse> list = this.warehouseDao.getAll();
			ResponseObject<List<Warehouse>> responseObj = new ResponseObject<List<Warehouse>>(ResponseCode.SUCCESS_CODE);
			if (list != null && !list.isEmpty()) {
				responseObj.setData(list);
			} else {
				responseObj.setData(new ArrayList<Warehouse>());
				responseObj.setMessage("没有仓库数据");
			}
			return responseObj;
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}

	public ResponseObject<PageSplit<Warehouse>> searchPageSplit(int pageSize, int pageNow, String groupid) throws ServiceException {
		try {
			int rowCount = 0;
			try {
				rowCount = this.warehouseDao.count();
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("获取仓库个数失败", e);
			}

			ResponseObject<PageSplit<Warehouse>> responseObj = new ResponseObject<PageSplit<Warehouse>>(
			        ResponseCode.SUCCESS_CODE);
			if (rowCount > 0) {
				pageSize = Math.max(pageSize, 1);
				int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
				pageNow = Math.min(pageNow, pageCount);
				PageSplit<Warehouse> pageSplit = new PageSplit<Warehouse>();
				pageSplit.setPageCount(pageCount);
				pageSplit.setPageNow(pageNow);
				pageSplit.setRowCount(rowCount);
				pageSplit.setPageSize(pageSize);

				int startIndex = (pageNow - 1) * pageSize;
				try {
					List<Warehouse> warehouses = this.warehouseDao.searchWarehouse(startIndex, pageSize, groupid);
					if (warehouses != null && !warehouses.isEmpty()) {
						for (Warehouse w : warehouses) {
							pageSplit.addData(w);
						}
					}
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("获取仓库列表失败", e);
				}
				responseObj.setData(pageSplit);
			} else {
				responseObj.setMessage("没有仓库");
			}
			return responseObj;
		} catch (ServiceException e) {
			throw e;
		}
	}

	public ResponseObject<Warehouse> getWarehouseById(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return new ResponseObject<Warehouse>(ResponseCode.PARAMETER_ERROR, "参数无效");
		} else {
			try {
				Warehouse house = this.warehouseDao.getById(id);
				
				if (house != null) {
					List<TranPrice> list=this.tranPriceDao.getAllbyWarehouseId(id);
					if((list!=null)&&(list.size()>0))
					{
						//house.setTranprice(list.toArray(new TranPrice[list.size()]));
					}
					
					ResponseObject<Warehouse> result = new ResponseObject<Warehouse>(ResponseCode.SUCCESS_CODE);
					result.setData(house);
					return result;
				} else {
					return new ResponseObject<Warehouse>(ResponseCode.WAREHOUSE_ID_ERROR, "没有对应的仓库记录");
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
	}

	public ResponseObject<Object> modifyWarehouse(Warehouse warehouse) throws ServiceException {
		if (warehouse == null) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR, "参数无效");
		}
		try {
			//if (StringUtil.isEmpty(warehouse.getSerialNo())) {
			//	warehouse.setSerialNo(String.valueOf(Integer.MAX_VALUE));
			//}else if(!StringUtil.isEmpty(warehouse.getCallOrderCity())){
				//Pattern pattern = Pattern.compile(",");
				//Matcher matcher = pattern.matcher(warehouse.getCallOrderCity());
				//warehouse.setCallOrderCity(matcher.replaceAll("\r\n"));
			//}
			int i = this.warehouseDao.updateWarehouse(warehouse);
			if (i > 0) {
				/*TranPrice[] trd1=warehouse.getTranprice();
				if((trd1!=null)&&(trd1.length>0))
				{
					for(int ii=0;ii<trd1.length;ii++)
					{
						if((!StringUtil.isEmpty(trd1[ii].getWarehouseId()))&&(!StringUtil.isEmpty(trd1[ii].getTranwarehouseId())))
						{
							TranPrice trd=this.tranPriceDao.getby2id(trd1[ii].getWarehouseId(), trd1[ii].getTranwarehouseId());
							if(trd!=null)
							{
								int aa=this.tranPriceDao.modifytranpricebi2id(trd1[ii]);
								if(aa<1)
								{
									throw new Exception("修改转运价格失败!");
								}
							}
							else//原来没有，重新创建
							{
								int aa=this.tranPriceDao.inserttranprice(trd1[ii]);
								if(aa<1)
								{
									throw new Exception("插入转运价格失败!");
								}
								
							}
						}
					}
				}*/
				return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			} else {
				return new ResponseObject<Object>(ResponseCode.WAREHOUSE_MODIFY_FAILURE, "修改失败，数据库中没有对应的数据");
			}
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}

	public ResponseObject<Object> deleteWarehouseByIds(String[] ids) throws ServiceException {
		if (ids == null || ids.length == 0) {
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR, "参数无效");
		}

		try {
			int i = this.warehouseDao.deleteWarehouseByIds(Arrays.asList(ids));
			
			
			if (i == ids.length) {
				//删除商品价格
				for(int n=0;n<ids.length;n++)
				{
					this.commudityPriceDao.deletebystoreId(ids[n]);
					//删除员工
					this.employeeDao.deleteEmployeebystoreId(ids[n]);
				}
				
				
				return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			} else {
				throw new Exception();
			}
			
			
			
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	
	
	
	
	
	public ResponseObject<List<Warehouse>> getwarehousebyadmin(String storeids) throws ServiceException {
		try {
			List<Warehouse> list = this.warehouseDao.getwarehousebyadmin(storeids);
			ResponseObject<List<Warehouse>> responseObj = new ResponseObject<List<Warehouse>>(ResponseCode.SUCCESS_CODE);
			if (list != null && !list.isEmpty()) {
				responseObj.setData(list);
			} else {
				responseObj.setData(new ArrayList<Warehouse>());
				responseObj.setMessage("没有仓库数据");
			}
			return responseObj;
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
	}
	
	
	
	
	public ResponseObject<PageSplit<Warehouse>> searchPageSplitbyadmin(String keyword,String storeids,int pageSize, int pageNow) throws ServiceException {
		try {
			if(StringUtil.isEmpty(keyword))
			{
				keyword=null;
			}
			else
			{
				keyword=StringUtil.escapeStringOfSearchKey(keyword);
			}
			
			int rowCount = 0;
			try {
				rowCount = this.warehouseDao.countbyadmin(keyword,storeids);
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("获取仓库个数失败", e);
			}

			ResponseObject<PageSplit<Warehouse>> responseObj = new ResponseObject<PageSplit<Warehouse>>(
			        ResponseCode.SUCCESS_CODE);
			if (rowCount > 0) {
				pageSize = Math.max(pageSize, 1);
				int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
				pageNow = Math.min(pageNow, pageCount);
				PageSplit<Warehouse> pageSplit = new PageSplit<Warehouse>();
				pageSplit.setPageCount(pageCount);
				pageSplit.setPageNow(pageNow);
				pageSplit.setRowCount(rowCount);
				pageSplit.setPageSize(pageSize);

				int startIndex = (pageNow - 1) * pageSize;
				try {
					List<Warehouse> warehouses = this.warehouseDao.searchWarehousebyadmin(keyword, storeids,startIndex, pageSize);
						//	this.warehouseDao.searchWarehouse(startIndex, pageSize, groupid);
					if (warehouses != null && !warehouses.isEmpty()) {
						for (Warehouse w : warehouses) {
							pageSplit.addData(w);
						}
					}
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("获取仓库列表失败", e);
				}
				responseObj.setData(pageSplit);
			} else {
				responseObj.setMessage("没有仓库");
			}
			return responseObj;
		} catch (ServiceException e) {
			throw e;
		}
	}
}

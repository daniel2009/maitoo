package com.meitao.service;

import java.util.List;

import com.meitao.exception.ServiceException;
import com.meitao.model.PageSplit;
import com.meitao.model.ProductRecord;
import com.meitao.model.ResponseObject;
import com.meitao.model.TranshipmentBill;
import com.meitao.model.TranshipmentCommodity;
import com.meitao.model.WayBill;
import com.meitao.model.temp.ExportTranshipmentBill;


public interface TranshipmentBillService {
	/**
	 * 添加运单信息到数据库，运单为bill，运单商量列表为lists<br/>
	 * 用户操作
	 * 
	 * @param bill
	 * @param lists
	 * @return
	 * @throws ServiceException1
	 */
	public ResponseObject<Object> addTranshipment(TranshipmentBill bill, List<TranshipmentCommodity> lists)
	        throws ServiceException;

	/**
	 * 修改运单信息到数据库中<br/>
	 * 管理员操作
	 * 
	 * @param bill
	 * @param lists
	 *            if this lists is null, will not modify the transhipment
	 *            commodity.
	 * @param empName TODO
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Object> modifyTranshipment(TranshipmentBill bill, List<TranshipmentCommodity> lists, String empName)
	        throws ServiceException;

	/**
	 * 根据id删除运单信息<br/>
	 * 管理员操作
	 * 
	 * @param ids
	 *            运单id，主键
	 * @param state
	 *            TODO
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Object> deleteTranshipment(List<String> ids, String state) throws ServiceException;

	/**
	 * 根据id删除运单信息<br/>
	 * 用户操作
	 * 
	 * @param ids
	 *            运单id，主键
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<Object> deleteTranshipmentByUserId(List<String> ids, String userId) throws ServiceException;

	/**
	 * 根据id获取运单对象
	 * 
	 * @param id
	 *            运单id，主键
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<TranshipmentBill> getById(String id) throws ServiceException;

	public ResponseObject<List<TranshipmentBill>> getByIds(String userId, List<String> ids) throws ServiceException;

	public ResponseObject<List<TranshipmentBill>> getTranshipmentByOrderId(String orderId) throws ServiceException;

	/**
	 * 根据userid获取用户提交的运单列表，分页
	 * 
	 * @param tid
	 *            TODO
	 * @param userId
	 * @param object 
	 * @param pageSize
	 * @param pageNow
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<PageSplit<TranshipmentBill>> searchByUserId(String tid, String userId, String state, String storeid, int pageSize,
	        int pageNow) throws ServiceException;

	/**
	 * 根据key模糊查询，开始时间为state，结束时间为edate。如果这个时间为空，则表示时间不限制。
	 * 
	 * @param tid
	 *            TODO
	 * @param key
	 * @param column
	 * @param sdate
	 * @param edate
	 * @param pageSize
	 * @param pageNow
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<PageSplit<TranshipmentBill>> searchByKey(String tid, String key, String column, String sdate,
	        String edate,String storeid, int pageSize, int pageNow) throws ServiceException;
	
	/**
	 * 根据key模糊查询，开始时间为state，结束时间为edate。如果这个时间为空，则表示时间不限制。
	 * 
	 * @param tid
	 *            TODO
	 * @param key
	 * @param column
	 * @param sdate
	 * @param edate
	 * @param pageSize
	 * @param pageNow
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public ResponseObject<PageSplit<TranshipmentBill>> searchByKeynew(String tid, String key, String column, String sdate,
	        String edate, int pageSize, int pageNow,String state,String trantype,String storeid,String tranwid,String towid) throws ServiceException;
	
	public ResponseObject<Object> createPreOrder(String id, WayBill wayBill) throws ServiceException;

	public ResponseObject<Object> addByAdmin(TranshipmentBill transhipmentBill) throws ServiceException;

	public ResponseObject<Object> auditPreOrder(TranshipmentBill transhipmentBill) throws ServiceException;

	public ResponseObject<PageSplit<TranshipmentBill>> searchWithRouteByKey(String id, String key, String type, String state, String warehouseId, String createDateStart,	String createDateEnd, int pageSize, int pageIndex) throws ServiceException;

	public ResponseObject<Object> takeBySelf(String id, String warehouseId) throws ServiceException;

	public ResponseObject<TranshipmentBill> getByIdAndUser(String id, String userId) throws ServiceException;
	
	public ResponseObject<Object> check_mux_Submit(TranshipmentBill bill,String[] commoditylist) throws ServiceException;
	public ResponseObject<Object> muxboxSubmit(TranshipmentBill bill,String[] commoditylist)
			throws ServiceException;
	
	public ResponseObject<Object> trantToOrder(String id) throws ServiceException;
	public ResponseObject<Object> importExcelOfOrderState(
			List<ExportTranshipmentBill> importOrders, String empName,String storeId)
			throws ServiceException;
	public boolean useStoragePosition(TranshipmentBill transhipmentBill) throws ServiceException;
	public ResponseObject<Object> savewrongmessage(String id,String message,String storeId) throws ServiceException;

	public ResponseObject<String[]> getAllStateCount(String warehouseId) throws ServiceException;

	public ResponseObject<Object> addByProductRecord(TranshipmentBill transhipmentBill, ProductRecord productRecord) throws ServiceException;
}
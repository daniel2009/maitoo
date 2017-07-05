package com.meitao.controller;



import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.meitao.service.CallOrderService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ParameterConstants;
import com.meitao.common.constants.ResponseCode;

import com.meitao.common.util.StringUtil;
import com.meitao.dao.WarehouseDao;
import com.meitao.model.CallOrder;

import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;

@Controller
public class CallOrderController extends BasicController {

	private static final long serialVersionUID = 1455632086294967055L;
	private static final Logger log = Logger.getLogger(CallOrderController.class);

	@Resource(name = "callOrderService")
	private CallOrderService callOrderService;
	@Value(value = "${page_size}")
	private int defaultPageSize;

	

	@Autowired
	private WarehouseDao warehouseDao;
	
	@RequestMapping(value = "/m_call_order/user_add", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> addCallOrderByUser(HttpServletRequest request, CallOrder callOrder) {
		String userId = (String) request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY);
		if(StringUtil.isEmpty(userId))
		{
			return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作！");
		}
		//检查上门收货门店是否支付的门店
		
		//this.warehouseDao.getById(callOrder.getWarehouseId());
		callOrder.setUserId(userId);
		
		try {
			return this.callOrderService.saveCallOrder(callOrder);
		} catch (Exception e) {
			log.error("保存上门信息到数据库失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "提交申请上门取件失败，请重新尝试或者联系客服！");
		}
	}
	
	@RequestMapping(value = "/admin/callOrder/searchByKey", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<PageSplit<CallOrder>> getAllCallOrder(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.COMMON_SEARCH_KEY, required = false, defaultValue = "") String key,
			@RequestParam(value = ParameterConstants.COMMON_SEARCH_TYPE, required = false, defaultValue = "") String type,
			@RequestParam(value = ParameterConstants.COMMON_SEARCH_STATE, required = false, defaultValue = "") String state,
			@RequestParam(value = ParameterConstants.COMMON_SEARCH_CREATE_DATE_START, required = false, defaultValue = "") String createDateStart,
			@RequestParam(value = ParameterConstants.COMMON_SEARCH_CREATE_DATE_END, required = false, defaultValue = "") String createDateEnd,
			@RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex){
		String warehouseId;
		if(((String)request.getSession().getAttribute(Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY)).equals("1")){
			warehouseId = null;
		}else{
			warehouseId = (String) request.getSession().getAttribute(Constant.EMP_STORE_ID_SESSION_KEY);
		}
		try{
			return callOrderService.findByKey(key, type, state, warehouseId, createDateStart, createDateEnd, pageIndex, defaultPageSize);
		}catch(Exception e){
			log.error("获取上门取件信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取上门取件信息出现异常!");
		}
	}
	
	@RequestMapping(value = "/user/callOrder/all", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<PageSplit<CallOrder>> getCallOrderByUser(HttpServletRequest request,
			@RequestParam(value = ParameterConstants.COMMON_PAGESPLIT_PAGE_INDEX, required = false, defaultValue = "1") int pageIndex) {
		String userId = (String) request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY);
		try {
			return this.callOrderService.getCallOrderByUserId(userId, pageIndex, defaultPageSize);
		} catch (Exception e) {
			log.error("获取上门取件信息失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取上门取件信息出现异常!");
		}
	}
	
	@RequestMapping(value = "/user/callOrder/modify", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> modifyflyinfo(HttpServletRequest request, CallOrder callOrder) {
		String userId = (String)request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY);
		callOrder.setUserId(userId);
		try {
			return this.callOrderService.modifyByUser(callOrder);
		} catch (Exception e) {
			log.error("修改仓库失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "修改仓库出现异常");
		}
	}
	@RequestMapping(value = "/user/callOrder/cancel", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> cancelCallOrderByUser(HttpServletRequest request, CallOrder callOrder) {
		String userId = (String)request.getSession().getAttribute(Constant.USER_ID_SESSION_KEY);
		try {
			return this.callOrderService.deleteCallOrder(callOrder.getId(), userId);
		} catch (Exception e) {
			log.error("取消失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "取消出现异常");
		}
	}
	@RequestMapping(value = {"/admin/callOrder/findById", "/user/callOrder/findById"}, method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<CallOrder> findById(HttpServletRequest request, CallOrder callOrder) {
		try {
			return this.callOrderService.findById(callOrder);
		} catch (Exception e) {
			log.error("取消失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "取消出现异常");
		}
	}
	@RequestMapping(value = "/admin/callOrder/audit", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> audit(HttpServletRequest request, CallOrder callOrder) {
		String empId = (String)request.getSession().getAttribute(Constant.EMP_ID_SESSION_KEY);
		callOrder.setEmpId(empId);
		try {
			return this.callOrderService.audit(callOrder);
		} catch (Exception e) {
			log.error("取消失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "取消出现异常");
		}
	}
}

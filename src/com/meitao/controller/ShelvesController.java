package com.meitao.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import jxl.common.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.meitao.dao.MorderDao;
import com.meitao.dao.ShelvesDao;
import com.meitao.dao.Shelves_positionDao;
import com.meitao.dao.TorderDao;
import com.meitao.dao.WarehouseDao;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.common.util.StringUtil;
import com.meitao.model.M_order;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.Shelves;
import com.meitao.model.Shelves_position;
import com.meitao.model.Warehouse;

@Controller
public class ShelvesController extends BasicController {
	private static final long serialVersionUID = 5290532123456625109L;
	private static final Logger log = Logger.getLogger(ShelvesController.class);

	@Autowired
	private ShelvesDao shelvesDao;// 管理员添加的商品管理

	@Autowired
	private Shelves_positionDao shelves_positionDao;// 管理员添加的商品管理
	
	@Autowired
	private TorderDao torderDao;
	
	@Autowired
	private MorderDao m_orderDao;

	@Autowired
	private WarehouseDao warehouseDao;

	@RequestMapping(value = "/admin/Shelves/add", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> addshelves(HttpServletRequest request,
			Shelves shelves) {
		try {

			String supperadmin = (String) request.getSession().getAttribute(
					Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			String storeId = (String) request.getSession().getAttribute(
					Constant.EMP_STORE_ID_SESSION_KEY);

			if ((supperadmin != null) && (supperadmin.equalsIgnoreCase("1"))) {

			} else {
				String master = (String) request.getSession().getAttribute(
						Constant.EMP_STORE_MASTER_SESSION_KEY);

				if ((master != null) && (master.equalsIgnoreCase("1"))) {
					if (!storeId.equalsIgnoreCase(shelves.getStoreId())) {
						return new ResponseObject<Object>(
								ResponseCode.PARAMETER_ERROR,
								"对不起，你无权添加其它门店的货架!");
					}
				} else {
					return new ResponseObject<Object>(
							ResponseCode.PARAMETER_ERROR,
							"对不起，只有高级管理员或店长能够创建货架!");
				}
			}

			String date = DateUtil.date2String(new Date());
			shelves.setModifyDate(date);
			shelves.setCreateDate(date);

			if (shelves.getShelvesNo().length() != 1) {
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,
						"货架标识只能取一个字符!");
			}
			shelves.setShelvesNo(shelves.getShelvesNo().toUpperCase());
			int number = 0;
			try {
				number = Integer.parseInt(shelves.getwNo());
				if (number > 0 && number <= 1000) {
				} else {
					return new ResponseObject<Object>(
							ResponseCode.PARAMETER_ERROR, "仓位数量取值范围错误!");
				}
			} catch (Exception e) {
				return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,
						"仓位数量取值异常!");
			}

			int a = this.shelvesDao.existShelves(shelves.getStoreId(),
					shelves.getShelvesNo());
			if (a > 0) {
				return new ResponseObject<Object>(ResponseCode.SHOW_EXCEPTION,
						"添加失败，货架标识已存在!");
			}

			int k = this.shelvesDao.insert(shelves);
			if (k > 0) {
				// 生成仓位值

				for (int i = 0; i < number; i++) {
					StringBuffer sb = new StringBuffer(4);// 约定4位长度
					String widflag = "";
					if (i < 10) {
						widflag = shelves.getShelvesNo() + "00" + i;
					} else if (i >= 10 && i < 100) {
						widflag = shelves.getShelvesNo() + "0" + i;
					} else {
						widflag = shelves.getShelvesNo() + i;
					}
					sb.append(widflag);// 最后两位是门店标识
					String position = sb.toString();
					Shelves_position spo = new Shelves_position();
					String date1 = DateUtil.date2String(new Date());
					spo.setCreateDate(date1);
					spo.setModifyDate(date1);
					spo.setIndexId(shelves.getId());
					spo.setPosition(position);
					spo.setRemark("");
					spo.setState(Constant.POSITION_STATE0);
					int b = this.shelves_positionDao.insert(spo);
					if (b < 1) {
						throw new Exception("生成仓位出现异常");
					}
				}

				return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
			} else {
				throw new Exception("保存失败!");
			}

			// return new
			// ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"保存失败");
		} catch (Exception e) {

			return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					"添加货架失败!");
		}
	}

	// 搜索运单
	@RequestMapping(value = "/admin/Shelves/search", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseObject<PageSplit<Shelves>> searchByKeyOfAdmin(
			HttpServletRequest request,
			@RequestParam(value = "s_storeId", required = false) String storeId,// 所属的门店
			@RequestParam(value = "wordkey", required = false) String wordkey,// 查找的关键字

			@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
			@RequestParam(value = "rows", required = false, defaultValue = "10") int rows) {
		String supperadmin = (String) request.getSession().getAttribute(
				Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
		String storeId1 = (String) request.getSession().getAttribute(
				Constant.EMP_STORE_ID_SESSION_KEY);
		if (!StringUtil.isEmpty(storeId) && storeId.equalsIgnoreCase("-1")) {
			storeId = null;
		}

		if ((supperadmin != null) && (supperadmin.equalsIgnoreCase("1")))// 高级管理员，
		{

		} else {
			if (!storeId1.equalsIgnoreCase(storeId)) {
				return new ResponseObject<PageSplit<Shelves>>(
						ResponseCode.PARAMETER_ERROR, "不能够查看其它门店的货架");
			}

		}
		if(!StringUtil.isEmpty(wordkey))
		{
			wordkey = StringUtil.escapeStringOfSearchKey(wordkey);
		}
		int rowCount = 0;
		try {

			rowCount = this.shelvesDao.countOfsearchShelves(storeId, wordkey);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseObject<PageSplit<Shelves>>(
					ResponseCode.SHOW_EXCEPTION, "操作发生异常");
		}

		ResponseObject<PageSplit<Shelves>> responseObj = new ResponseObject<PageSplit<Shelves>>(
				ResponseCode.SUCCESS_CODE);
		int pageSize = rows;
		int pageNow = pageIndex;
		if (rowCount > 0) {
			pageSize = Math.max(pageSize, 1);
			int pageCount = rowCount / pageSize
					+ (rowCount % pageSize == 0 ? 0 : 1);
			pageNow = Math.min(pageNow, pageCount);
			PageSplit<Shelves> pageSplit = new PageSplit<Shelves>();
			pageSplit.setPageCount(pageCount);
			pageSplit.setPageNow(pageNow);
			pageSplit.setRowCount(rowCount);
			pageSplit.setPageSize(pageSize);

			int startIndex = (pageNow - 1) * pageSize;
			try {
				List<Shelves> shelves = this.shelvesDao.searchShelves(storeId,
						wordkey, startIndex, pageSize);

				for (Shelves o : shelves) {

					if (o == null) {
						continue;
					}
					if (!StringUtil.isEmpty(o.getStoreId())) {
						Warehouse ware = this.warehouseDao.getById(o
								.getStoreId());
						if (ware != null) {
							o.setStoreName(ware.getName());
						}
					}
					
					int aa=this.shelves_positionDao.countOfunused(o.getId());
					o.setUnused_wNo(String.valueOf(aa));
					aa=this.shelves_positionDao.countOfused(o.getId());
					o.setUsed_wNo(String.valueOf(aa));

				}
				pageSplit.setDatas(shelves);
			} catch (Exception e) {
				//e.printStackTrace();
				return new ResponseObject<PageSplit<Shelves>>(
						ResponseCode.SHOW_EXCEPTION, "操作发生异常");
			}
			responseObj.setData(pageSplit);
		} else {
			responseObj.setMessage("没有货架");
		}
		return responseObj;
	}
	
	
	@RequestMapping(value = "/admin/Shelves/delete", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> delshelves(HttpServletRequest request,
			@RequestParam(value = "id", required = false) String id) {
		try {
			
			if(StringUtil.isEmpty(id))
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错!");
			}
			

			String supperadmin = (String) request.getSession().getAttribute(
					Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			String storeId = null;

			if ((supperadmin != null) && (supperadmin.equalsIgnoreCase("1"))) {
				
			} else {
				storeId = (String) request.getSession().getAttribute(
						Constant.EMP_STORE_ID_SESSION_KEY);
				String master = (String) request.getSession().getAttribute(
						Constant.EMP_STORE_MASTER_SESSION_KEY);

				if ((master != null) && (master.equalsIgnoreCase("1"))) {
					
				} else {
					return new ResponseObject<Object>(
							ResponseCode.PARAMETER_ERROR,
							"对不起，只有高级管理员或店长能够进行此操作!");
				}
			}

			Shelves she=this.shelvesDao.getone(id, storeId);
			if(she==null)
			{
				return new ResponseObject<Object>(
						ResponseCode.PARAMETER_ERROR,
						"查找货架失败!");
			}
			int k=this.shelves_positionDao.countOfused(she.getId());
			if(k>0)
			{
				return new ResponseObject<Object>(
						ResponseCode.PARAMETER_ERROR,
						"删除失败，原因：有"+k+"个仓位仍然在使用中。");
			}
			int d=this.shelvesDao.deleteone(id);
			if(d>0)
			{
				int dd=this.shelves_positionDao.detelebyindex(id);
				if(dd>0)
				{
					return new ResponseObject<Object>(
							ResponseCode.SUCCESS_CODE,
							"删除成功");
				}
				else
				{
					throw new Exception("删除仓位失败!");
				}
			}
			else
			{
				throw new Exception("删除货架失败!");
			}
		} catch (Exception e) {

			return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					"添加货架失败!");
		}
	}
	
	
	//清空仓位
	@RequestMapping(value = "/admin/Shelves_position/clear", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> clearposition(HttpServletRequest request,
			@RequestParam(value = "id", required = false) String id) {
		try {
			
			if(StringUtil.isEmpty(id))
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错!");
			}
			

			
			String  storeId = (String) request.getSession().getAttribute(
					Constant.EMP_STORE_ID_SESSION_KEY);
			if(StringUtil.isEmpty(storeId))
			{
				return new ResponseObject<Object>(ResponseCode.NEED_LOGIN,"请登陆后操作!");
			}

			

		    String date=DateUtil.date2String(new Date());
			int k=this.shelves_positionDao.updatestate(id, Constant.POSITION_STATE0, date, "");
			if(k>0)
			{
				
				this.m_orderDao.clear_position(id, date);//清空运单中的仓位
				this.torderDao.clear_positionId(id, date);//清空转运单号的仓位
				
				return new ResponseObject<Object>(
						ResponseCode.SUCCESS_CODE,
						"清空成功!");
			}
			return new ResponseObject<Object>(
					ResponseCode.PARAMETER_ERROR,
					"清空失败!");
			
		} catch (Exception e) {

			return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					"清空仓位失败!");
		}
	}

//修改备注
	@RequestMapping(value = "/admin/Shelves/modify_remark", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public ResponseObject<Object> modifyshelves(HttpServletRequest request,
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "remark", required = false) String remark,
			@RequestParam(value = "nickName", required = false) String nickName) {
		try {
			
			if(StringUtil.isEmpty(id))
			{
				return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"参数出错!");
			}
			

			String supperadmin = (String) request.getSession().getAttribute(
					Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			String storeId = null;

			if ((supperadmin != null) && (supperadmin.equalsIgnoreCase("1"))) {
				
			} else {
				storeId = (String) request.getSession().getAttribute(
						Constant.EMP_STORE_ID_SESSION_KEY);
				String master = (String) request.getSession().getAttribute(
						Constant.EMP_STORE_MASTER_SESSION_KEY);

				if ((master != null) && (master.equalsIgnoreCase("1"))) {
					
				} else {
					return new ResponseObject<Object>(
							ResponseCode.PARAMETER_ERROR,
							"对不起，只有高级管理员或店长能够进行此操作!");
				}
			}
			Shelves shelves=new Shelves();
			shelves.setId(id);
			shelves.setRemark(remark);
			shelves.setNickName(nickName);
			String date=DateUtil.date2String(new Date());
			shelves.setModifyDate(date);
			int k=this.shelvesDao.updateRemark(shelves);
			if(k>0)
			{
				return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"修改成功！");
			}
			return new ResponseObject<Object>(ResponseCode.PARAMETER_ERROR,"修改失败！");
		} catch (Exception e) {

			return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
					"添加货架失败!");
		}
	}

	
	
	
	
	// 搜索仓位信息
		@RequestMapping(value = "/admin/Shelves/search_position", method = {
				RequestMethod.GET, RequestMethod.POST })
		@ResponseBody
		public ResponseObject<PageSplit<Shelves_position>> searchByKeyOfAdmin(
				HttpServletRequest request,
				@RequestParam(value = "id", required = false) String id,// 架id
				@RequestParam(value = "state", required = false) String state,// 查找状态
				@RequestParam(value = "wordkey", required = false) String wordkey,// 查找的关键字

				@RequestParam(value = "page", required = false, defaultValue = "1") int pageIndex,
				@RequestParam(value = "rows", required = false, defaultValue = "10") int rows) {
		//	String supperadmin = (String) request.getSession().getAttribute(
		//			Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
			String storeId1 = (String) request.getSession().getAttribute(
					Constant.EMP_STORE_ID_SESSION_KEY);
			
			if(StringUtil.isEmpty(storeId1))
			{
				return new ResponseObject<PageSplit<Shelves_position>>(
						ResponseCode.NEED_LOGIN, "请登陆后操作");
			}
			
			if(StringUtil.isEmpty(id))
			{
				return new ResponseObject<PageSplit<Shelves_position>>(
						ResponseCode.PARAMETER_ERROR, "参数错误");
			}
			
			if(StringUtil.isEmpty(state)||state.equalsIgnoreCase("-1"))
			{
				state=null;
			}

			if(!StringUtil.isEmpty(wordkey))
			{
				wordkey = StringUtil.escapeStringOfSearchKey(wordkey);
			}
			int rowCount = 0;
			try {

				rowCount=this.shelves_positionDao.countOfsearchShelvesPosition(id, state, wordkey);
				

			} catch (Exception e) {
				//e.printStackTrace();
				return new ResponseObject<PageSplit<Shelves_position>>(
						ResponseCode.SHOW_EXCEPTION, "操作发生异常");
			}

			ResponseObject<PageSplit<Shelves_position>> responseObj = new ResponseObject<PageSplit<Shelves_position>>(
					ResponseCode.SUCCESS_CODE);
			int pageSize = rows;
			int pageNow = pageIndex;
			if (rowCount > 0) {
				pageSize = Math.max(pageSize, 1);
				int pageCount = rowCount / pageSize
						+ (rowCount % pageSize == 0 ? 0 : 1);
				pageNow = Math.min(pageNow, pageCount);
				PageSplit<Shelves_position> pageSplit = new PageSplit<Shelves_position>();
				pageSplit.setPageCount(pageCount);
				pageSplit.setPageNow(pageNow);
				pageSplit.setRowCount(rowCount);
				pageSplit.setPageSize(pageSize);

				int startIndex = (pageNow - 1) * pageSize;
				try {
					List<Shelves_position> shelves = this.shelves_positionDao.searchShelvesPosition(id, state, wordkey, startIndex, pageSize);
							

					for (Shelves_position o : shelves) {

						if (o == null) {
							continue;
						}
						
						
						
					}
					pageSplit.setDatas(shelves);
				} catch (Exception e) {
					//e.printStackTrace();
					return new ResponseObject<PageSplit<Shelves_position>>(
							ResponseCode.SHOW_EXCEPTION, "操作发生异常");
				}
				responseObj.setData(pageSplit);
			} else {
				responseObj.setMessage("没有货架");
			}
			return responseObj;
		}
		
		
		//转运州入库获取货架列表
		@RequestMapping(value = "/admin/Shelves/get_by_or", method = { RequestMethod.GET,
				RequestMethod.POST })
		@ResponseBody
		public ResponseObject<Object> getshelvesbyor(HttpServletRequest request) {
			try {
				
				
				

				/*String supperadmin = (String) request.getSession().getAttribute(
						Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
				String storeId = null;

				if ((supperadmin != null) && (supperadmin.equalsIgnoreCase("1"))) {
					
				} else {
					storeId = (String) request.getSession().getAttribute(
							Constant.EMP_STORE_ID_SESSION_KEY);
					
				}*/
				
				String storeId = (String) request.getSession().getAttribute(
						Constant.EMP_STORE_ID_SESSION_KEY);
				List<Shelves> slv=this.shelvesDao.getbytstoreId(storeId);
				ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"获取成功");
				obj.setData(slv);
				return obj;
			} catch (Exception e) {

				return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
						"获取货架失败!");
			}
		}
		
		//本地州入库获取货架列表
		@RequestMapping(value = "/admin/Shelves/get_by_local", method = { RequestMethod.GET,
				RequestMethod.POST })
		@ResponseBody
		public ResponseObject<Object> getshelvesbylocal(HttpServletRequest request) {
			try {

				/*String supperadmin = (String) request.getSession().getAttribute(
						Constant.EMP_STORE_SUPPERADMIN_SESSION_KEY);
				String storeId = null;

				if ((supperadmin != null) && (supperadmin.equalsIgnoreCase("1"))) {
					
				} else {
					storeId = (String) request.getSession().getAttribute(
							Constant.EMP_STORE_ID_SESSION_KEY);
					
				}*/
				String storeId = (String) request.getSession().getAttribute(
						Constant.EMP_STORE_ID_SESSION_KEY);
				List<Shelves> slv=this.shelvesDao.getbytstoreId(storeId);
				ResponseObject<Object> obj=new ResponseObject<Object>(ResponseCode.SUCCESS_CODE,"获取成功");
				obj.setData(slv);
				return obj;
			} catch (Exception e) {

				return generateResponseObject(ResponseCode.SHOW_EXCEPTION,
						"获取货架失败!");
			}
		}

		
}

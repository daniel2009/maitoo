package com.meitao.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import jxl.common.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.ResponseBody;



import com.meitao.dao.CountrysDao;

import com.meitao.common.constants.ResponseCode;

import com.meitao.model.Countrys;
import com.meitao.model.ResponseObject;


@Controller
public class CountrysController extends BasicController {
	private static final long serialVersionUID = 5270532661049625109L;
	private static final Logger log = Logger.getLogger(ChannelController.class);
	@Autowired
	private CountrysDao countrylDao;
	
	//@Autowired
	//private WarehouseDao warehouseDao;

	@RequestMapping(value = "/countrys/all", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseObject<List<Countrys>> getAllChannel(HttpServletRequest request ) {
		try {
			List<Countrys> list = new ArrayList<Countrys>();
			list=this.countrylDao.getAllCountrys();
			
			ResponseObject<List<Countrys>> responseObj = new ResponseObject<List<Countrys>>(ResponseCode.SUCCESS_CODE);
			if (list != null && !list.isEmpty()) {
				responseObj.setData(list);
			} else {
				responseObj.setCode(ResponseCode.PARAMETER_ERROR);
				responseObj.setMessage("没有国家数据");
			}
			return responseObj;
		} catch (Exception e) {
			log.error("获取国家列表失败", e);
			return generateResponseObject(ResponseCode.SHOW_EXCEPTION, "获取国家列表失败");
		}
	}
	
	
}

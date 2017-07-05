package com.meitao.service;




import javax.servlet.http.HttpServletRequest;

import com.meitao.exception.ServiceException;
import com.meitao.model.PageSplit;
import com.meitao.model.Receive_User;
import com.meitao.model.ResponseObject;


public interface Receive_UserService {
	public ResponseObject<PageSplit<Receive_User>> searchReceiveUserByInfo(String info, int pageSize, int pageNow)
	        throws ServiceException;
	
	public ResponseObject<Object> getOneVerfyMessage() throws ServiceException;//获取一条待验证信息
	public ResponseObject<Object> getNumbersVerfyMessage() throws ServiceException;//获取所有待验证的信息数量
	
	public ResponseObject<Object> saveverfyresult(String id,String modifyDate,String isPass,HttpServletRequest request) throws ServiceException;//保存修改结果

}

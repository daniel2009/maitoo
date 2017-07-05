package com.meitao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitao.dao.UserCodeDao;
import com.meitao.service.UserCodeService;
import com.meitao.exception.ServiceException;
import com.meitao.model.ResponseObject;
import com.meitao.model.UserCode;

@Service("userCodeService")
public class UserCodeServiceImpl
  implements UserCodeService
{

  @Autowired
  private UserCodeDao userCodeDao;

  public UserCode findUserCodeById(int id)
    throws ServiceException
  {
    System.out.println("======serviceimpl获得的随机数==========");
    System.out.println(id);

    UserCode usercode = new UserCode();
    try
    {
      String state = this.userCodeDao.findUserCodeById(id).getState();
      while (state.equals("1"))
      {
        state = this.userCodeDao.findUserCodeById(id).getState();

        id++;
      }

      usercode = this.userCodeDao.findUserCodeById(id);

      System.out.println("======serviceimpl获得的usercode==========");
      System.out.println(usercode.getUsercode());
    }
    catch (Exception e1) {
      e1.printStackTrace();
    }
    return usercode;
  }
  
	public UserCode findUserCodeByState0() throws ServiceException {

		UserCode usercode = new UserCode();
		try {
			usercode = this.userCodeDao.findUserCodeByState("0");
			usercode.setState("1");
			this.userCodeDao.updateUserCode(usercode);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return usercode;
	}

  public ResponseObject<Object> modifyUserCode(String usercode, String state)
    throws ServiceException
  {
    ResponseObject<Object> responseObj = new ResponseObject<Object>();
    if ((usercode == null) && (state == null)) {
      responseObj.setCode("603");
      responseObj.setMessage("参数无效");
    } else {
      System.out.println("======UserCodeServiceImpl==========");
      System.out.println(usercode);
      System.out.println("======UserCodeServiceImpl==========");
      System.out.println(state);
      try {
        UserCode u = new UserCode();
        u.setState(state);
        u.setUsercode(usercode);
        this.userCodeDao.updateUserCode(u);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }

    return responseObj;
  }
}
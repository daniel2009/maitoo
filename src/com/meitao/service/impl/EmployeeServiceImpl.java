package com.meitao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitao.dao.AuthorityDao;
import com.meitao.dao.AuthorityemployeesDao;
import com.meitao.dao.CallOrderDao;
import com.meitao.dao.EmployeeDao;
import com.meitao.dao.LoginInfoDao;
import com.meitao.dao.ProductDao;
import com.meitao.dao.ProductRecordDao;
import com.meitao.dao.ReturnPackageDao;
import com.meitao.dao.TranshipmentBillDao;
import com.meitao.dao.UserDao;
import com.meitao.dao.WarehouseDao;
import com.meitao.service.EmployeeService;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.DateUtil;
import com.meitao.common.util.ExceptionUtil;
import com.meitao.common.util.MD5Util;
import com.meitao.common.util.StringUtil;
import com.meitao.exception.ServiceException;
import com.meitao.model.Authority;
import com.meitao.model.Authority_url;
import com.meitao.model.Employee;
import com.meitao.model.PageSplit;
import com.meitao.model.Product;
import com.meitao.model.ProductRecord;
import com.meitao.model.ResponseObject;
import com.meitao.model.Warehouse;


@Service("employeeService")
public class EmployeeServiceImpl extends BasicService implements EmployeeService {

	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private AuthorityemployeesDao authorityemployeesDao;
	
	@Autowired
	private AuthorityDao authorityDao;
	@Autowired
	private AuthorityemployeesDao authorityemplouyeesdao;
	@Autowired
	private TranshipmentBillDao transhipmentBillDao;

	@Autowired
	private CallOrderDao callOrderDao;
	@Autowired
	private ReturnPackageDao returnPackageDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductRecordDao productRecordDao;
	
	@Autowired
	private WarehouseDao warehouseDao;
	

	@Autowired
	private LoginInfoDao loginInfoDao;

	public ResponseObject<Object> addEmployee(Employee employee,List<String> authorityids) throws ServiceException {
		ResponseObject<Object> result = new ResponseObject<Object>();
		if (null == employee) {
			result.setCode(ResponseCode.PARAMETER_ERROR);
			result.setMessage("参数无效");
		} else {
			// md5加密密码
			employee.setPassword(MD5Util.encode(employee.getPassword()));
			employee.setSignDate(DateUtil.date2String(new Date()));
			try {
				int iresult = this.employeeDao.insertEmployee(employee);
				if (iresult > 0) {
					List<Authority> authoritys = new ArrayList<Authority>();
					
					if(authorityids!=null)
					{
						for (String authorityId : authorityids) {
	                        authoritys.add(new Authority(authorityId,employee.getId()));
	                    }
						int k = this.authorityemployeesDao.insertAuthority(authoritys);
						if(k>0){
						result.setCode(ResponseCode.SUCCESS_CODE);
						}else{
							//进行事务回滚
							throw new Exception();
						}
					}
				} else {
					result.setCode(ResponseCode.EMPLOYEE_INSERT_FAILURE);
					result.setMessage("添加职工到数据库失败");
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("添加职工失败", e);
			}
		}
		return result;
	}

	public ResponseObject<Employee> checkLogin(String accountName, String password) throws ServiceException {
		ResponseObject<Employee> result = new ResponseObject<Employee>();
		if (StringUtil.isEmpty(accountName) || StringUtil.isEmpty(password)) {
			result.setCode(ResponseCode.PARAMETER_ERROR);
			result.setMessage("参数无效");
		} else {
			try {
				String pwd = MD5Util.encode(password);
				Employee employee = this.employeeDao.getEmployeeByAccount(accountName);
				if (employee != null && accountName.equals(employee.getAccount()) && pwd.equals(employee.getPassword())) {
					result.setCode(ResponseCode.SUCCESS_CODE);
					result.setData(getSecurityValue(employee));
					try {
						this.loginInfoDao.insertUpdate(employee.getId(), "1", DateUtil.date2String(new Date()));
					} catch (Exception e) {
						// ignore
					}
				} else {
					result.setCode(ResponseCode.EMPLOYEE_ACCOUNT_NOT_EXISTS);
					result.setMessage("账户或者密码错误!");
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("检验职工登录失败", e);
			}
		}
		return result;
	}

	public ResponseObject<Object> deleteEmployee(String id) throws ServiceException {
		ResponseObject<Object> result = new ResponseObject<Object>();
		if (StringUtil.isEmpty(id)) {
			result.setCode(ResponseCode.PARAMETER_ERROR);
			result.setMessage("参数无效");
		} else {
			try {
				int iresult = this.employeeDao.deleteEmployee(id);
				int k = this.authorityemployeesDao.dellAuthorityEmployeeById(id);
				if (iresult > 0) {
					result.setCode(ResponseCode.SUCCESS_CODE);
				} else {
					result.setCode(ResponseCode.EMPLOYEE_DELETE_FAILURE);
					result.setMessage("删除职工失败");
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return result;
	}

	public ResponseObject<Employee> getById(String id) throws ServiceException {
		ResponseObject<Employee> result = new ResponseObject<Employee>();
		if (StringUtil.isEmpty(id)) {
			result.setCode(ResponseCode.PARAMETER_ERROR);
			result.setMessage("参数无效");
		} else {
			try {
				Employee employee = this.employeeDao.getEmployeeById(id);
				if (employee != null) {
					employee.setPassword("");
					if(!StringUtil.isEmpty(employee.getStoreId()))
					{
						Warehouse ware=this.warehouseDao.getById(employee.getStoreId());
						if(ware!=null)
						{
							employee.setStoreName(ware.getName());
						}
					}
					
					
					result.setCode(ResponseCode.SUCCESS_CODE);
					result.setData(getSecurityValue(employee));
				} else {
					result.setCode(ResponseCode.EMPLOYEE_ID_NOT_EXISTS);
					result.setMessage("数据库中没有对应的职工");
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return result;
	}

	public ResponseObject<Object> modifyEmployee(Employee employee,List<String> authorityids) throws ServiceException {
		ResponseObject<Object> result = new ResponseObject<Object>();
		if (null == employee) {
			result.setCode(ResponseCode.PARAMETER_ERROR);
			result.setMessage("参数无效");
		} else {
			// md5加密密码
			if (employee.getPassword()!=null&&!employee.getPassword().equals("")) {
				employee.setPassword(MD5Util.encode(employee.getPassword()));
			}
			try {
				int iresult = this.employeeDao.updateEmployee(employee);
				if (iresult > 0) {
					List<Authority> authoritys = new ArrayList<Authority>();
					String id=employee.getId();
	
					if((authorityids!=null)&&authorityids.size()>0)
					{
						for (String authorityId : authorityids) {
	                        authoritys.add(new Authority(authorityId,id));
	                        
	                    }
					
						int i=this.authorityemployeesDao.dellAuthorityEmployeeById(id);
						int k = this.authorityemployeesDao.insertAuthority(authoritys);
						if(k>0){
						result.setCode(ResponseCode.SUCCESS_CODE);
						}else{
							//进行事务回滚
							throw new Exception();
						}
					}
					result.setCode(ResponseCode.SUCCESS_CODE);
				} else {
					result.setCode(ResponseCode.EMPLOYEE_UPDATE_FAILURE);
					result.setMessage("修改职工信息失败");
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("添加职工失败", e);
			}
		}
		return result;
	}

	public ResponseObject<PageSplit<Employee>> searchAll(int pageSize, int pageNow, String groupid,String empid) throws ServiceException {
		try {
			int rowCount = 0;
			try {
				if(!StringUtil.isEmpty(empid))
				{
					ResponseObject<Employee> obj=this.getById(empid);
					if((obj!=null)&&(obj.getData()!=null))
					{
						rowCount=1;
					}
				}
				else
				{
					rowCount = this.employeeDao.count();
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("获取职工个数失败", e);
			}

			ResponseObject<PageSplit<Employee>> responseObj = new ResponseObject<PageSplit<Employee>>(
			        ResponseCode.SUCCESS_CODE);
			if (rowCount > 0) {
				pageSize = Math.max(pageSize, 1);
				int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
				pageNow = Math.min(pageNow, pageCount);
				PageSplit<Employee> pageSplit = new PageSplit<Employee>();
				pageSplit.setPageCount(pageCount);
				pageSplit.setPageNow(pageNow);
				pageSplit.setRowCount(rowCount);
				pageSplit.setPageSize(pageSize);

				int startIndex = (pageNow - 1) * pageSize;
				try {
					List<Employee> employees=null;
					if(!StringUtil.isEmpty(empid))
					{
						ResponseObject<Employee> obj=this.getById(empid);
						if((obj!=null)&&(obj.getData()!=null))
						{
							employees=new ArrayList<Employee>();
							employees.add(obj.getData());
							
						}
					}
					else
					{
						employees= this.employeeDao.searchAllEmployee(startIndex, pageSize, groupid);
					}
					if (employees != null && !employees.isEmpty()) {
						for (Employee emp : employees) {
							pageSplit.addData(getSecurityValue(emp));
						}
					}
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("获取用户列表失败", e);
				}
				responseObj.setData(pageSplit);
			} else {
				responseObj.setMessage("没有用户");
			}
			return responseObj;
		} catch (ServiceException e) {
			throw e;
		}
	}

	public ResponseObject<Object> modifyPassword(String id, String password, String oldpwd) throws ServiceException {
		ResponseObject<Object> result = new ResponseObject<Object>();
		if (StringUtil.isEmpty(id) || StringUtil.isEmpty(password) || StringUtil.isEmpty(oldpwd)) {
			result.setCode(ResponseCode.PARAMETER_ERROR);
			result.setMessage("参数无效");
		} else {
			// 修改密码
			try {
				int iresult = this.employeeDao.updatePassword(id, MD5Util.encode(password), MD5Util.encode(oldpwd));
				if (iresult > 0) {
					result.setCode(ResponseCode.SUCCESS_CODE);
				} else {
					result.setCode(ResponseCode.EMPLOYEE_UPDATE_FAILURE);
					result.setMessage("原密码错误");
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return result;
	}

	public ResponseObject<Employee> getByAccoutName(String accountName) throws ServiceException {
		ResponseObject<Employee> result = new ResponseObject<Employee>();
		if (StringUtil.isEmpty(accountName)) {
			result.setCode(ResponseCode.PARAMETER_ERROR);
			result.setMessage("参数无效");
		} else {
			try {
				Employee employee = this.employeeDao.getEmployeeByAccount(accountName);
				if (employee != null) {
					result.setCode(ResponseCode.SUCCESS_CODE);
					result.setData(employee);
				} else {
					result.setCode(ResponseCode.EMPLOYEE_ACCOUNT_NOT_EXISTS);
					result.setMessage("该账号不存在");
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return result;
	}

	public ResponseObject<Object> checkEmployee(String id, String password) throws ServiceException {
		ResponseObject<Object> result = new ResponseObject<Object>();
		if (StringUtil.isEmpty(id) || StringUtil.isEmpty(password)) {
			result.setCode(ResponseCode.PARAMETER_ERROR);
			result.setMessage("参数无效");
		} else {
			try {
				Employee employee = this.employeeDao.getEmployeeById(id);
				password = MD5Util.encode(password);
				if (employee != null && employee.getPassword().equals(password)) {
					result.setCode(ResponseCode.SUCCESS_CODE);
				} else {
					result.setCode(ResponseCode.EMPLOYEE_ACCOUNT_NOT_EXISTS);
					result.setMessage("职工密码不正确");
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return result;
    }
	/**
	 * 删除权限具体实现方法
	 * 张敬琦
	 * 2015-01-28
	 */
	@Override
	public ResponseObject<Object> deleteAuthority(String id)
			throws ServiceException {
		ResponseObject<Object> result = new ResponseObject<Object>();
		if (StringUtil.isEmpty(id)) {
			result.setCode(ResponseCode.PARAMETER_ERROR);
			result.setMessage("参数无效");
		} else {
			try {
				int iresult = this.authorityemployeesDao.dellAuthorityEmployeeById(id);
				if (iresult > 0) {
					result.setCode(ResponseCode.SUCCESS_CODE);
				} else {
					result.setCode(ResponseCode.EMPLOYEE_DELETE_FAILURE);
					result.setMessage("删除职工权限失败");
				}
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException(e);
			}
		}
		return result;
	}
	/**
	 * 修改权限具体实现方法
	 * 张敬琦
	 * 2015-01-28
	 */
	

	@Override
	public ResponseObject<Object> modifyAuthority(String eid,
			List<String> authorityids) throws ServiceException {
		try {
			// 第一步，先删除
	        int k = this.authorityemployeesDao.dellAuthorityEmployeeById(eid);
	        if (k > 0) {
	        	// 第二步插入
	        	List<Authority> authoritys = new ArrayList<Authority>();
                for (String authorityId : authorityids) {
                    authoritys.add(new Authority(eid,authorityId));
                }
	        	k = this.authorityemployeesDao.insertAuthority(authoritys);
	        	if (k > 0) {
	        		return new ResponseObject<Object>(ResponseCode.SUCCESS_CODE);
	        	} else {
	        		// 进行事务的会滚滚
	        		throw new Exception();
	        	}
	        } else {
	        	// 进行事务的回滚
	        	throw new Exception();
	        }
        } catch (Exception e) {
        	throw ExceptionUtil.handle2ServiceException(e);
        }
		// TODO Auto-generated method stub
	}

	@Override
	public ResponseObject<Object> addAuthorityEmplyees(List<String> authority)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}
/**
 * 获取权限的具体实现方法
 * 张敬琦
 * 2015-01-28
 */
	@Override
	public ResponseObject<List<Authority_url>> getMenuInfoByEmployeeId(String employeeId) throws ServiceException {
        ResponseObject<List<Authority_url>> result = new ResponseObject<List<Authority_url>>();
        if (StringUtil.isEmpty(employeeId)) {
            result.setCode(ResponseCode.PARAMETER_ERROR);
            result.setMessage("参数无效");
            return result;
        } else {
            try {
                System.out.println(employeeId);
                List<Authority_url> authority_url = this.authorityDao.selectAuthority_ul(employeeId);
                System.out.println("获取的权限列表：" + authority_url);
                result.setData(authority_url);
                result.setCode(ResponseCode.SUCCESS_CODE);
                return result;
            } catch (Exception e) {
                throw ExceptionUtil.handle2ServiceException(e);
            }
        }
    }
	
	@Override
	public ResponseObject<List<Authority_url>> getAllMenuInfo() throws ServiceException {
        ResponseObject<List<Authority_url>> result = new ResponseObject<List<Authority_url>>();
        try {
            List<Authority_url> authority_url = this.authorityDao.findAll();
            result.setData(authority_url);
            result.setCode(ResponseCode.SUCCESS_CODE);
            return result;
        } catch (Exception e) {
            throw ExceptionUtil.handle2ServiceException(e);
        }
    }

	@Override
	public ResponseObject<String[]> getRealTimeCount4AdminNav(String warehouseId) throws ServiceException {
		ResponseObject<String[]> responseObject = new ResponseObject<String[]>();
		try {
			String[] array = new String[14];
			int i = 0;
			//1
			array[i++] = "countTranshipmentBillInNav";
			array[i++] = String.valueOf(this.transhipmentBillDao.countByKey(null, null, null, null, null, null, null, warehouseId));
			//array[i++] = "countOrderInNav";
			//array[i++] = String.valueOf(this.orderDao.countOfSearchKeys(null, null, null, null, null, null, null,null, warehouseId));
			array[i++] = "countCallOrderInNav";
			array[i++] = String.valueOf(this.callOrderDao.countByKey(null, null, null, warehouseId, null, null));
			array[i++] = "countReturnPackageInNav";
			array[i++] = String.valueOf(this.returnPackageDao.countByKey(null, null, null, null, null, null, null));
			array[i++] = "countUserInNav";
			array[i++] = String.valueOf(this.userDao.countByKey(null, "%", null));
			array[i++] = "countProductInNav";
			array[i++] = String.valueOf(this.productDao.countByKey(new Product(), null, null, null));
			array[i++] = "countProductRecordInNav";
			array[i++] = String.valueOf(this.productRecordDao.countByKey(new ProductRecord(), null, null));
			//11
			responseObject.setCode(ResponseCode.SUCCESS_CODE);
			responseObject.setData(array);
		} catch (Exception e) {
			throw ExceptionUtil.handle2ServiceException(e);
		}
		return responseObject;
	}

	
	//查找搜索员工信息
	public ResponseObject<PageSplit<Employee>> searchAllbyadmin(int pageSize, int pageNow,String keyword,String storeId,String empid) throws ServiceException {
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
				rowCount=this.employeeDao.countbyadmin(empid, keyword, storeId);
			} catch (Exception e) {
				throw ExceptionUtil.handle2ServiceException("获取职工个数失败", e);
			}

			ResponseObject<PageSplit<Employee>> responseObj = new ResponseObject<PageSplit<Employee>>(
			        ResponseCode.SUCCESS_CODE);
			if (rowCount > 0) {
				pageSize = Math.max(pageSize, 1);
				int pageCount = rowCount / pageSize + (rowCount % pageSize == 0 ? 0 : 1);
				pageNow = Math.min(pageNow, pageCount);
				PageSplit<Employee> pageSplit = new PageSplit<Employee>();
				pageSplit.setPageCount(pageCount);
				pageSplit.setPageNow(pageNow);
				pageSplit.setRowCount(rowCount);
				pageSplit.setPageSize(pageSize);

				int startIndex = (pageNow - 1) * pageSize;
				try {
					List<Employee> employees=null;
					employees=this.employeeDao.searchAllbyadmin(empid, keyword, storeId, startIndex, pageSize);
					if (employees != null && !employees.isEmpty()) {
						for (Employee emp : employees) {
							pageSplit.addData(getSecurityValue(emp));
						}
					}
				} catch (Exception e) {
					throw ExceptionUtil.handle2ServiceException("获取用户列表失败", e);
				}
				responseObj.setData(pageSplit);
			} else {
				responseObj.setMessage("没有用户");
			}
			return responseObj;
		} catch (ServiceException e) {
			throw e;
		}
	}

}

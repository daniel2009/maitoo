package com.meitao.service;

import com.meitao.exception.ServiceException;
import com.meitao.model.PageSplit;
import com.meitao.model.RenlingBaoguo;
import com.meitao.model.RenlingPersons;
import com.meitao.model.ResponseObject;

public interface ClaimPersonService {
	/**
	 * it's going to update some info of renlingBaoguo. So, what is the transId in renlingBaoguo, and how can I get it?
	 * @param renlingPerson
	 * @return
	 * @throws ServiceException
	 */
	ResponseObject<Object> add(RenlingPersons renlingPerson) throws ServiceException;

	ResponseObject<RenlingPersons> findLastClaimedByClaimPackage(
			String renlingId, String userId) throws ServiceException;

	ResponseObject<PageSplit<RenlingBaoguo>> findByUser(String userId, String state,
			String nameCondition, int pageIndex, int defaultPageSize) throws ServiceException;

	ResponseObject<Object> deleteByUser(String id, String userId) throws ServiceException;

	ResponseObject<RenlingPersons> findById(String id) throws ServiceException;

	ResponseObject<Object> reclaimByUser(RenlingPersons renlingPerson) throws ServiceException;

}

package com.meitao.service;



import com.meitao.exception.ServiceException;
import com.meitao.model.PageSplit;
import com.meitao.model.RenlingBaoguo;
import com.meitao.model.RenlingPersons;
import com.meitao.model.ResponseObject;

public interface ClaimPackageService {

	ResponseObject<PageSplit<RenlingBaoguo>> findByUser(String userId,
			String nameCondition, int pageIndex, int pageSize) throws ServiceException;

	ResponseObject<PageSplit<RenlingBaoguo>> findByNotClaimed(String nameCondition, int pageIndex,
			int pageSize) throws ServiceException;

	ResponseObject<RenlingBaoguo> getByIdWithClaimPerson(String id) throws ServiceException;

	ResponseObject<Object> updateByAudit(String id, String state,
			String claimPersonId, String auditRemark) throws ServiceException;

	ResponseObject<PageSplit<RenlingPersons>> getByClaimPackageId(
			String claimPackageId, int pageIndex, int pageSize) throws ServiceException;

	ResponseObject<PageSplit<RenlingBaoguo>> findByNotClaimedAndUser(
			String userId, String nameCondition, int pageIndex, int defaultPageSize) throws ServiceException;

	ResponseObject<PageSplit<RenlingPersons>> findAllByClaimPackageId(String claimPackageId, int pageIndex, int pageSize) throws ServiceException;

	ResponseObject<String[]> getAllStateCount() throws ServiceException;

}

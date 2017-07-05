package com.meitao.dao;

import org.apache.ibatis.annotations.Param;

import com.meitao.model.Storage;
import com.meitao.model.StoragePosition;
import com.meitao.model.StoragePositionRecord;
import com.meitao.model.User;

public interface StoragePositionRecordDao {

	int insert(StoragePositionRecord storagePositionRecord) throws Exception;

	int deleteByRelate(StoragePositionRecord storagePositionRecord) throws Exception;

	int countByStoragePosition(StoragePosition storagePosition) throws Exception;

	int insertByUser(@Param("storage")Storage storage, @Param("storagePosition")StoragePosition storagePosition, @Param("user")User user, @Param("array")String[] stateArray) throws Exception;

	int deleteByStoragePosition(StoragePosition storagePosition) throws Exception;

}

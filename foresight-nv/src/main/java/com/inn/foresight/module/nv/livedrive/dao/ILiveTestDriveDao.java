package com.inn.foresight.module.nv.livedrive.dao;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.livedrive.model.LiveTestDrive;

/** The Interface ILiveTestDriveDao. */
public interface ILiveTestDriveDao extends IGenericDao<Integer, LiveTestDrive> {

	/**
	 * Gets the drive list by id.
	 *
	 * @param driveId
	 *            the drive id
	 * @return the drive list by id
	 * @throws RestException
	 *             the rest exception
	 */
	LiveTestDrive getDriveListById(int driveId);

}

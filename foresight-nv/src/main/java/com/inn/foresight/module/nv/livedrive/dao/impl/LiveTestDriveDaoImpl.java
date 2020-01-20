package com.inn.foresight.module.nv.livedrive.dao.impl;

import java.util.Set;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.livedrive.constants.LiveDriveConstant;
import com.inn.foresight.module.nv.livedrive.dao.ILiveTestDriveDao;
import com.inn.foresight.module.nv.livedrive.model.LiveTestDrive;
import com.inn.product.um.role.model.Role;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;
import com.inn.product.um.user.utils.UmConstants;
import com.inn.product.um.user.utils.UmUtils;

/** 
 * @author Innoeye
 * 
 *         The Class LiveTestDriveDaoImpl.
 */
@Repository("LiveTestDriveDaoImpl")
public class LiveTestDriveDaoImpl extends HibernateGenericDao<Integer, LiveTestDrive> implements ILiveTestDriveDao {

	/** The logger. */
	private static Logger logger = LogManager.getLogger(LiveTestDriveDaoImpl.class);

	/** Instantiates a new live test drive dao impl. */
	public LiveTestDriveDaoImpl() {
		super(LiveTestDrive.class);
	}

	/**
	 * Gets the drive list by driveId.
	 *
	 * @param driveId
	 *            the drive id
	 * @return the drive list by id
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	public LiveTestDrive getDriveListById(int driveId) {
		LiveTestDrive listTestDrive = null;
		logger.info("inside Live Drive Doa Impl getting Drive List by driveId : {}", driveId);
		try {
			Query query = getEntityManager().createNamedQuery("getLiveDriveById").setParameter("driveId", driveId);
			listTestDrive = (LiveTestDrive) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("Error occured @class:" + this.getClass().getName() + " @Method: getDriveListById {}",
					Utils.getStackTrace(e));
			throw new RestException(LiveDriveConstant.DRIVE_ID_DOES_NOT_EXIST);
		} catch (Exception e) {
			logger.error("Error occured @class:" + this.getClass().getName() + " @Method: getDriveListById:{}",
					Utils.getStackTrace(e));
			throw new RestException(LiveDriveConstant.SOME_PROBLEM_IN_GETTING_DATA);
		}
		return listTestDrive;
	}



}

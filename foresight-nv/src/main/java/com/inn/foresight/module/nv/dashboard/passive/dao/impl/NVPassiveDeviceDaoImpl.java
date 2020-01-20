package com.inn.foresight.module.nv.dashboard.passive.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.dashboard.passive.dao.INVPassiveDeviceDao;
import com.inn.foresight.module.nv.dashboard.passive.model.NVPassiveDevice;
import com.inn.foresight.module.nv.dashboard.passive.utils.NVPassiveConstants;
import com.inn.foresight.module.nv.dashboard.passive.wrapper.NVPassiveDeviceWrapper;

@Repository("NVPassiveDeviceDaoImpl")
public class NVPassiveDeviceDaoImpl extends HibernateGenericDao<Integer, NVPassiveDevice>
		implements INVPassiveDeviceDao {

	private static final String NO_RESULT_AVAILABLE_FOR_REQUEST = "No result available for request";
	Logger logger = LogManager.getLogger(NVPassiveDeviceDaoImpl.class);

	public NVPassiveDeviceDaoImpl() {
		super(NVPassiveDevice.class);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<NVPassiveDeviceWrapper> getMakeWisePassiveDeviceCounts(Map<String, List> filterMetaData,
			List<String> top5Make) {

		List<NVPassiveDeviceWrapper> dataList = new ArrayList<>();
		try {
			logger.info("Fetching data for passive device counts make wise");
			Query query = getEntityManager().createNamedQuery(NVPassiveConstants.GET_MAKE_WISE_PASSIVE_DEVICE_COUNTS);
			if (top5Make != null && !top5Make.isEmpty()) {
				query.setParameter(NVPassiveConstants.MAKE, top5Make);
			}
			dataList = query.getResultList();
			logger.info("Make Wise Result List Size:{}", dataList.size());
			logger.info("Make Wise Result List:{}", dataList);

			return dataList;
		} catch (NoResultException noResultException) {
			logger.info(NO_RESULT_AVAILABLE_FOR_REQUEST);
			return dataList;
		} catch (BusinessException e) {
			logger.info("Error while getting make wise device counts: {}", ExceptionUtils.getStackTrace(e));
			throw new BusinessException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<NVPassiveDeviceWrapper> getMakeModelWisePassiveDeviceCounts(Map<String, List> filterMetaData,
			List<String> top5Make) {

		List<NVPassiveDeviceWrapper> dataList = new ArrayList<>();
		try {
			logger.info("Fetching data for passive device counts make, model wise");
			Query query = getEntityManager()
					.createNamedQuery(NVPassiveConstants.GET_MAKE_MODEL_WISE_PASSIVE_DEVICE_COUNTS);
			if (top5Make != null && !top5Make.isEmpty()) {
				query.setParameter(NVPassiveConstants.MAKE, top5Make);
			}
			dataList = query.getResultList();
			logger.info("Make Model Wise Result List Size:{}", dataList.size());
			logger.info("Make Model Wise Result List {}", dataList);

			return dataList;
		} catch (NoResultException noResultException) {
			logger.error(NO_RESULT_AVAILABLE_FOR_REQUEST);
			return dataList;
		} catch (BusinessException e) {
			logger.info("Error while getting make, model wise device counts: {}", ExceptionUtils.getStackTrace(e));
			throw new BusinessException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<NVPassiveDeviceWrapper> getMakeModelOsWisePassiveDeviceCounts(Map<String, List> filterMetaData,
			List<String> top5Make) {

		List<NVPassiveDeviceWrapper> dataList = new ArrayList<>();
		try {
			logger.info("Fetching data for passive device counts make, model, os wise");
			Query query = getEntityManager()
					.createNamedQuery(NVPassiveConstants.GET_MAKE_MODEL_OS_WISE_PASSIVE_DEVICE_COUNTS);
			if (top5Make != null && !top5Make.isEmpty()) {
				query.setParameter(NVPassiveConstants.MAKE, top5Make);
			}
			dataList = query.getResultList();
			logger.info("Make, Model, Os Wise Result List Size:{}", dataList.size());
			logger.info("Make, Model, Os Wise Result List {}", dataList);

			return dataList;
		} catch (NoResultException noResultException) {
			logger.info(NO_RESULT_AVAILABLE_FOR_REQUEST);
			return dataList;
		} catch (BusinessException e) {
			logger.info("Error while getting make, model, os wise device counts: {}", ExceptionUtils.getStackTrace(e));
			throw new BusinessException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<String> getTop5Make(Map<String, List> filterMetaData) {
		List<String> resultList = new ArrayList<>();
		logger.info("Going to get top 5 make");
		try {
			Query query = getEntityManager().createNamedQuery(NVPassiveConstants.GET_TOP5_MAKE);
			query.setMaxResults(ForesightConstants.FIVE);
			resultList = query.getResultList();
			logger.info("resultList: {}", resultList);
			return resultList;
		} catch (NoResultException noResultException) {
			logger.error(NO_RESULT_AVAILABLE_FOR_REQUEST);
			return resultList;
		} catch (BusinessException e) {
			logger.error("Error while getting top 5 make: {}", ExceptionUtils.getStackTrace(e));
			throw new BusinessException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}



}

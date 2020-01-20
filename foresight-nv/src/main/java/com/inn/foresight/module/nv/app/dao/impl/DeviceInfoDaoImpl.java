package com.inn.foresight.module.nv.app.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.inn.commons.lang.NumberUtils;
import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.exceptions.ValueNotFoundException;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.app.constants.AppConstants;
import com.inn.foresight.module.nv.app.dao.IDeviceInfoDao;
import com.inn.foresight.module.nv.app.model.DeviceInfo;
import com.inn.foresight.module.nv.app.model.DeviceInfo.Source;
import com.inn.foresight.module.nv.app.wrapper.DeviceInfoWrapper;
import com.inn.foresight.module.nv.device.constant.DeviceConstant;
import com.inn.foresight.module.nv.device.util.NVDeviceUtil;
import com.inn.foresight.module.nv.profile.constants.NVProfileConstants;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants;
import com.inn.foresight.module.nv.wpt.analytics.constants.WPTAnalyticsConstants;

/** The Class DeviceInfoDaoImpl. */
@Repository("DeviceInfoDaoImpl")
public class DeviceInfoDaoImpl extends HibernateGenericDao<Integer, DeviceInfo> implements IDeviceInfoDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(DeviceInfoDaoImpl.class);

	/** Instantiates a new device info dao impl. */
	public DeviceInfoDaoImpl() {
		super(DeviceInfo.class);
	}

	/**
	 * Creates the.
	 *
	 * @param anEntity
	 *            the an entity
	 * @return the device info
	 * @throws DaoException
	 *             the dao exception
	 */
	@Override
	public DeviceInfo create(DeviceInfo anEntity)  {
		logger.info("Going to create record in DeviceInfo");
		return super.create(anEntity);
	}

	/**
	 * Update.
	 *
	 * @param anEntity
	 *            the an entity
	 * @return the device info
	 * @throws DaoException
	 *             the dao exception
	 */
	@Override
	public DeviceInfo update(DeviceInfo anEntity)  {
		logger.info("Going to update record in DeviceInfo for id {}", anEntity.getId());
		return super.update(anEntity);
	}

	/**
	 * Finding DeviceInfo record with given imei, imsi, deviceOS and apkId(from
	 * ApkDetail).
	 *
	 * @param imei
	 *            the imei
	 * @param imsi
	 *            the imsi
	 * @param deviceOS
	 *            the device OS
	 * @param apkId
	 *            the apk id
	 * @return DeviceInfo Object or null in case of No result found with given
	 *         details
	 * @throws RestException
	 *             the rest exception
	 * @throws ValueNotFoundException
	 *             the value not found exception
	 */
	@Override
	public DeviceInfo getDeviceInfoByApkId(String imei, String imsi) throws  ValueNotFoundException {
		logger.info("Finding latest DeviceInfo By imsi {}, imei {}", imsi, imei);
		try {
			Query query = getEntityManager().createNamedQuery(AppConstants.GET_DEVICEINFO_BY_APKID)
					.setParameter(AppConstants.IMEI, imei).setParameter(AppConstants.IMSI, imsi);

			return (DeviceInfo) query.getSingleResult();
		} catch (NoResultException e) {
			logger.info(AppConstants.NO_RECORD_FOUND_LOGGER);
			throw new ValueNotFoundException(e);
		} catch (Exception e) {
			logger.error(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Gets the device info by user.
	 *
	 * @param userId
	 *            the user id
	 * @return the device info by user
	 * @throws DaoException
	 *             the dao exception
	 */
	@Override
	public DeviceInfo getDeviceInfoByUser(Integer userId)  {
		try {
			Query query = getEntityManager().createNamedQuery(AppConstants.GET_DEVICE_INFO_BY_USER_ID);
			query.setParameter(AppConstants.UID, userId);
			query.setMaxResults(1);
			return (DeviceInfo) query.getSingleResult();
		} catch (Exception e) {
			logger.error(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
	}

	/**
	 * Gets the device info by imei and imsi.
	 *
	 * @param imei
	 *            the imei
	 * @param imsi
	 *            the imsi
	 * @return the device info by imei and imsi
	 * @throws DaoException
	 *             the dao exception
	 * @throws ValueNotFoundException
	 *             the value not found exception
	 */
	@Override
	public DeviceInfo getDeviceInfoByImeiAndImsi(String imei, String imsi) {
		DeviceInfo objToReturn = null;
		try {
			Query query = getEntityManager().createNamedQuery(AppConstants.GET_DEVICEINFO_BY_IMEI_AND_IMSI);
			query.setParameter(AppConstants.IMEI, imei);
			query.setParameter(AppConstants.IMSI, imsi);
			objToReturn = (DeviceInfo) query.getSingleResult();
		} catch (NoResultException e) {
			logger.warn(AppConstants.NO_RECORD_FOUND_LOGGER);
		} catch (Exception e) {
			logger.error(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
		}
		return objToReturn;
	}

	/**
	 * This method is used to update all deviceInfo instance in DB.
	 *
	 * @param deviceInfos
	 *            the device infos
	 * @throws DaoException
	 *             the dao exception
	 */
	@Override
	public void updateAll(List<DeviceInfo> deviceInfos)  {
		logger.info("Inside updateAll");
		try {
			EntityManager entityManager = getEntityManager();
			AtomicInteger counter = new AtomicInteger();
			Integer batchSize = AppConstants.BULK_UPDATE_BATCH_SIZE;
			deviceInfos.forEach(d -> {
				entityManager.persist(d);
				counter.incrementAndGet();
				if (counter.intValue() % batchSize == AppConstants.EMPTY_BATCH_SIZE) {
					entityManager.flush();
					entityManager.clear();
				}
			});
			entityManager.flush();
			entityManager.clear();
			logger.info("Done updateAll");
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	/**
	 * Gets the distinct make.
	 *
	 * @return the distinct make
	 * @throws DaoException
	 *             the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getDistinctMake()  {
		logger.info("getDistinctMake");
		try {
			Query query = getEntityManager().createNamedQuery(AppConstants.GET_DISTINCT_MAKE);
			List<String> makes = query.getResultList();
			logger.info("Done getDistinctMake resultSize : {}", makes.size());
			return makes;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	/**
	 * Gets the distinct model.
	 *
	 * @param make
	 *            the make
	 * @return the distinct model
	 * @throws DaoException
	 *             the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getDistinctModel(String make) {
		logger.info("getDistinctModel");
		try {
			Query query = getEntityManager().createNamedQuery(AppConstants.GET_DISTINCT_MODEL);
			query.setParameter(NVProfileConstants.MAKE, make);
			List<String> models = query.getResultList();
			logger.info("Done getDistinctModel resultSize : {}", models.size());
			return models;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	/**
	 * Gets the distinct OS.
	 *
	 * @return the distinct OS
	 * @throws DaoException
	 *             the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getDistinctOS()  {
		logger.info("getDistinctOS");
		try {
			Query query = getEntityManager().createNamedQuery(AppConstants.GET_DISTINCT_OS);
			List<String> os = query.getResultList();
			logger.info("Done getDistinctOS resultSize : {}", os.size());
			return os;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	/**
	 * Gets the device info by device id.
	 *
	 * @param deviceId the device id
	 * @return the device info by device id
	 * @throws DaoException the dao exception
	 */
	@Override
	@Transactional(readOnly = true)
	public DeviceInfo getDeviceInfoByDeviceId(String deviceId)  {
		DeviceInfo deviceInfo = null;
		try {
			Query query = getEntityManager().createNamedQuery(AppConstants.GET_DEVICEINFO_BY_DEVICE_ID);
			query.setParameter(AppConstants.DEVICE_ID, deviceId);
			deviceInfo = (DeviceInfo) query.getSingleResult();
		} catch (NoResultException e) {
			logger.warn(AppConstants.NO_RECORD_FOUND_LOGGER);
		} catch (Exception e) {
			logger.error(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
		return deviceInfo;
	}

	/**
	 * Gets the device info by imsi.
	 *
	 * @param imsi the imsi
	 * @return the device info by imsi
	 */
	@Override
	public DeviceInfo getDeviceInfoByImsi(String imsi) {
		DeviceInfo objToReturn = null;
		try {
			Query query = getEntityManager().createNamedQuery(AppConstants.GET_DEVICEINFO_BY_IMSI);
			query.setParameter(AppConstants.IMSI, imsi);
			query.setMaxResults(DeviceConstant.MAX_DEVICE_COUNT);
			objToReturn = (DeviceInfo) query.getSingleResult();
		} catch (NoResultException e) {
			logger.warn(AppConstants.NO_RECORD_FOUND_LOGGER);
		} catch (Exception e) {
			logger.error(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
		}
		return objToReturn;
	}

	/**
	 * Gets the device info list.
	 *
	 * @param imei
	 *            the imei
	 * @param imsi
	 *            the imsi
	 * @return the device info list
	 * @throws DaoException
	 *             the dao exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<DeviceInfo> getDeviceInfoList(String imei, String imsi)  {
		List<DeviceInfo> objToReturn = null;
		Query query = null;
		Map<String, List> filters = null;
		try {
			filters = NVDeviceUtil.getFilterAndValueMapForDeviceInfo(imei, imsi);
		} catch (RestException e1) {
			logger.error(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e1));
		}
		try {
			query = getEntityManager().createNamedQuery(AppConstants.GET_DEVICEINFO_LIST_BY_IMEI_AND_IMSI);
			if (query != null&&filters != null) {
					enableFilters(filters);
					objToReturn = query.getResultList();
					disableFilters(filters);
			}
		} catch (NoResultException e) {
			logger.warn(AppConstants.NO_RECORD_FOUND_LOGGER);
		} catch (Exception e) {
			logger.error(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
		}
		return objToReturn;
	}

	/**
	 * Enable filters.
	 *
	 * @param filters
	 *            the filters
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void enableFilters(Map<String, List> filters) {
		List<String> filterNames = filters.get(WPTAnalyticsConstants.FILTER_NAME);
		List<String> filterParams = filters.get(WPTAnalyticsConstants.FILTER_PARAM);
		List<Object> filterValues = filters.get(WPTAnalyticsConstants.FILTER_VALUE);
		int listSize = filterNames.size();
		for (int count = NumberUtils.INTEGER_ZERO; count < listSize; count++) {
			if (filterValues.get(count) instanceof List) {
				enableFilter(filterNames.get(count), filterParams.get(count), (List) filterValues.get(count));
			} else {
				enableFilter(filterNames.get(count), filterParams.get(count), filterValues.get(count));
			}
		}
	}

	/**
	 * Disable filters.
	 *
	 * @param filters
	 *            the filters
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void disableFilters(Map<String, List> filters) {
		List<String> filterNames = filters.get(WPTAnalyticsConstants.FILTER_NAME);
		int listSize = filterNames.size();
		for (int count = NumberUtils.INTEGER_ZERO; count < listSize; count++) {
			disableFilter(filterNames.get(count));
		}
	}

	/**
	 * Enable filter.
	 *
	 * @param filterName
	 *            the filter name
	 * @param filterParam
	 *            the filter param
	 * @param filterValue
	 *            the filter value
	 */
	private void enableFilter(String filterName, String filterParam, Object filterValue) {
		Session s = (Session) getEntityManager().getDelegate();
		Filter filter = s.enableFilter(filterName);
		if (filterParam != null && filterValue != null) {
			filter.setParameter(filterParam, filterValue);
		}
	}

	/**
	 * Disable filter.
	 *
	 * @param filterName
	 *            the filter name
	 */
	private void disableFilter(String filterName) {
		Session s = (Session) getEntityManager().getDelegate();
		s.disableFilter(filterName);
	}

	/**
	 * Gets the device info by MSISDN.
	 *
	 * @param msisdn the msisdn
	 * @return the device info by MSISDN
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceInfo> getDeviceInfoByMSISDN(List<String> msisdnList)  {
		logger.info("Going to getDeviceInfoByMSISDN msisdnList {}",msisdnList);
		List<DeviceInfo> deviceInfo = null;
		try {
			Query query = getEntityManager().createNamedQuery(AppConstants.GET_DEVICEINFO_BY_MSISDN);
			query.setParameter(NVConstant.MSISDNLIST, msisdnList);
			deviceInfo = query.getResultList();
		} catch (NoResultException e) {
			logger.warn(AppConstants.NO_RECORD_FOUND_LOGGER);
		} catch (Exception e) {
			logger.error("Exception in getDeviceInfoByMSISDN {}", ExceptionUtils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
		return deviceInfo;
	}

	/**
	 * Gets the device info list by user name.
	 *
	 * @param userName the user name
	 * @param llimit the llimit
	 * @param ulimit the ulimit
	 * @return the device info list by user name
	 * @throws DaoException the dao exception
	 */
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DeviceInfoWrapper> getDeviceInfoListByUserName(List<String> username, Integer llimit, Integer ulimit,Boolean isInstalled)
		 {
		List<DeviceInfoWrapper> list=null;
		try {
			Query query = getEntityManager().createNamedQuery(AppConstants.GET_DEVICE_INFO_BY_USER_NAME);
			query.setParameter(AppConstants.USERNAME, username);
			setPaginationLimits(ulimit, llimit, query);
			applyInstalledFilter(isInstalled);
			list =query.getResultList();
			disableFilter(DeviceConstant.INSTALLED_FILTER);
		} catch (Exception e) {
			logger.error(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
		return list;
	}

	/**
	 * Gets the device info list count by user name.
	 *
	 * @param userName the user name
	 * @return the device info list count by user name
	 * @throws DaoException the dao exception
	 */
	@Override
	public long getDeviceInfoListCountByUserName(String userName,Boolean isInstalled) {
		long count;
		try {
			Query query = getEntityManager().createNamedQuery(AppConstants.GET_DEVICE_INFO_LIST_COUNT_BY_USER_NAME);
			query.setParameter(AppConstants.USERNAME, userName);
			applyInstalledFilter(isInstalled);
			count= (long) query.getSingleResult();
			disableFilter(DeviceConstant.INSTALLED_FILTER);
		} catch (Exception e) {
			logger.error(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
		return count;
	}

	/**
	 * Sets the pagination limits.
	 *
	 * @param ulimit the ulimit
	 * @param llimit the llimit
	 * @param query the query
	 */
	private void setPaginationLimits(Integer ulimit, Integer llimit, Query query) {
		if (ulimit != null && llimit != null) {
			if (ulimit >= NumberUtils.INTEGER_ZERO) {
				query.setMaxResults(ulimit - llimit + NumberUtils.INTEGER_ONE);
			}
			if (llimit >= NumberUtils.INTEGER_ZERO) {
				query.setFirstResult(llimit);
			}
		}
	}

	/**
	 * Gets the device list.
	 *
	 * @param llimit the llimit
	 * @param ulimit the ulimit
	 * @return the device list
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceInfo> getDeviceList(Integer llimit, Integer ulimit)  {
		List<DeviceInfo> list=null;
		try {
			Query query = getEntityManager().createNamedQuery(AppConstants.GET_DEVICE_LIST);
			setPaginationLimits(ulimit, llimit, query);
			list= query.getResultList();
		} catch (Exception e) {
			logger.error(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
		return list;
	}

	/**
	 * Gets the device list count.
	 *
	 * @return the device list count
	 * @throws DaoException the dao exception
	 */
	@Override
	public long getDeviceListCount()  {
		Long count;
		try {
			Query query = getEntityManager().createNamedQuery(AppConstants.GET_DEVICE_INFO_LIST_COUNT);
			count= (Long) query.getSingleResult();
		} catch (Exception e) {
			logger.error(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
		return count;
	}

	
	/**
	 * Gets the device id by IMEI.
	 *
	 * @param imei the imei
	 * @return the device id by IMEI
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getDeviceIdByIMEI(String imei) {
		logger.info("Going to getDeviceIdByIMEI for imei {}", imei);
		List<String> deviceIds = null;
		try {
			Query query = getEntityManager().createNamedQuery(AppConstants.GET_DEVICE_ID_BY_IMEI);
			query.setParameter(StealthConstants.IMEI, imei);
			deviceIds = query.getResultList();
		} catch (Exception ex) {
			logger.info("Error occur while getDeviceIdByIMEI,err msg {}", ExceptionUtils.getStackTrace(ex));
		}
		logger.info("Returning result from getDeviceIdByIMEI{}", deviceIds);
		return deviceIds;
	}

	
	@Override
	public DeviceInfo getBBMLocationByMsisdn(String msisdn) {
		logger.info("Going to get BBMLocation By Msisdn {} ", msisdn);
		DeviceInfo deviceInfo = new DeviceInfo();
		try {
			Query query = getEntityManager().createNamedQuery(AppConstants.GET_DEVICE_INFO_BY_MDN);
			query.setParameter(AppConstants.SOURCE, Source.BBM);
			query.setParameter(AppConstants.MSISDN, msisdn);
			deviceInfo = (DeviceInfo) query.getSingleResult();
		} catch (Exception exception) {
			logger.error("Unable to fetch BBM Location By msisdn {} Exception {} ", msisdn, Utils.getStackTrace(exception));
		}
		return deviceInfo;
	}
     @SuppressWarnings("unchecked")
	@Override
	 public DeviceInfo getDeviceInfoByImei(String imei) {
		 try {
				Query query = getEntityManager().createNamedQuery("getDeviceIdForImei");
				query.setParameter(NVWorkorderConstant.IMEI, imei);
				List<DeviceInfo> list = query.getResultList();
				logger.info(NVWorkorderConstant.RESULT_SIZE_LOGGER, list!=null?list.size():0);
				return list!=null?list.get(0):null;
			} catch (NoResultException e) {
				throw new DaoException(NVWorkorderConstant.DATA_NOT_FOUND);
			} 
	 }
     /**
 	 * Gets the device info by device id.
 	 *
 	 * @param deviceId the device id
 	 * @return the device info by device id
 	 * @throws DaoException the dao exception
 	 */
 	@SuppressWarnings("unchecked")
	@Override
 	public List<DeviceInfo> getDeviceInfoByDeviceIdList(List<String> deviceIdList)  {
 		logger.info("Going to getDeviceInfoByDeviceIdList for deviceIdList : {}",deviceIdList);
 		List<DeviceInfo> deviceInfo = null;
 		try {
 			Query query = getEntityManager().createNamedQuery(NVConstant.GET_DEVICEINFO_BY_DEVICE_ID_LIST);
 			query.setParameter(NVConstant.DEVICE_ID_LIST, deviceIdList);
 			deviceInfo =  query.getResultList();
 		} catch (NoResultException e) {
 			logger.warn(AppConstants.NO_RECORD_FOUND_LOGGER);
 		} catch (Exception e) {
 			logger.error(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
 			throw new DaoException(e.getMessage());
 		}
 		return deviceInfo;
 	}

	@Override
	public DeviceInfo getDeviceInfoByApkIdAndImei(String imei) throws ValueNotFoundException {
		logger.info("Going to getDeviceInfoByApkIdAndImei By  imei {}", imei);
		try {
			Query query = getEntityManager().createNamedQuery(NVConstant.GET_DEVICEINFO_BY_APKID_AND_IMEI)
					.setParameter(AppConstants.IMEI, imei);

			return (DeviceInfo) query.getSingleResult();
		} catch (NoResultException e) {
			logger.info(AppConstants.NO_RECORD_FOUND_LOGGER);
			throw new ValueNotFoundException(e);
		} catch (Exception e) {
			logger.error(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}
	
	@Override
	public int updateDeviceInfoByDeviceId(String deviceId, String imsi, String msisdn) {
		logger.info("Going to update device info for device id : {} , imsi : {} and msisdn : {}", deviceId, imsi, msisdn);
		int updateDevice = 0;
		try {
			Query query = getEntityManager().createQuery("update DeviceInfo d set d.imsi=:imsi,d.msisdn=:msisdn,d.modificationTime=:modificationTime where d.deviceId=:deviceId");
			query.setParameter(AppConstants.DEVICE_ID, deviceId);
			query.setParameter(AppConstants.IMSI, imsi);
			query.setParameter(AppConstants.MSISDN, msisdn);
			query.setParameter(AppConstants.MODIFICATION_TIME, new Date());
			updateDevice = query.executeUpdate();
		} catch (Exception e) {
			logger.error("Error in updating device info for device id : {} : {}", deviceId, Utils.getStackTrace(e));
		}
		return updateDevice;
	}
	private void applyInstalledFilter(Boolean isInstalled) {
		if (isInstalled != null) {
			Session s = (Session) getEntityManager().getDelegate();
			Filter filter = s.enableFilter(DeviceConstant.INSTALLED_FILTER);
			filter.setParameter("isInstalled", isInstalled);
		}
	}

}

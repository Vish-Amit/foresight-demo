package com.inn.foresight.module.nv.workorder.stealth.kpi.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper;
import com.inn.foresight.module.nv.report.utils.StealthUtils;
import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants;
import com.inn.foresight.module.nv.workorder.stealth.kpi.dao.IDriveTestKPIDao;
import com.inn.foresight.module.nv.workorder.stealth.kpi.model.DriveTestKPI;
import com.inn.foresight.module.nv.workorder.stealth.kpi.service.IDriveTestKPIService;

/**
 * The Class DriveTestKPIServiceImpl.
 *
 * @author ist
 */
@Service("DriveTestKPIServiceImpl")
public class DriveTestKPIServiceImpl extends AbstractService<Integer, DriveTestKPI> implements IDriveTestKPIService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(DriveTestKPIServiceImpl.class);

	/** The i drive test KPI dao. */
	@Autowired
	private IDriveTestKPIDao iDriveTestKPIDao;

	/**
	 * Gets the KPI data.
	 *
	 * @param startDate  the start date
	 * @param endDate    the end date
	 * @param dataType   the data type
	 * @param kpi        the kpi
	 * @param threshold  the threshold
	 * @param uLimit     the u limit
	 * @param lLimit     the l limit
	 * @param resultType the result type
	 * @return the KPI data
	 * @throws RestException the rest exception
	 */
	@Override
	public List<NVDeviceDataWrapper> getKPIData(Long startDate, Long endDate, String dataType, String kpi,
			Double threshold, Integer uLimit, Integer lLimit, String resultType) {
		logger.info(
				"Going to getDeviceData for startDate{}, endDate{}, dataType{}, kpi{}, threshold{}, uLimit {}, lLimit {},resultType {}",
				startDate, endDate, dataType, kpi, threshold, uLimit, lLimit, resultType);
		try {
			if (dataType.equalsIgnoreCase(StealthConstants.KPI)) {
				List<NVDeviceDataWrapper> deviceData = iDriveTestKPIDao.getKPIData(startDate, endDate, kpi, threshold,
						uLimit, lLimit, StealthUtils.getFilterAndValueMap(StealthConstants.IS_DEVICEWISE, kpi,
								threshold, new Date(startDate), new Date(endDate), true, resultType));
				logger.info("Returning result from getDeviceData with size {}", deviceData.size());
				return deviceData;
			} else {
				List<NVDeviceDataWrapper> failuredDeviceData = iDriveTestKPIDao.getFailureData(startDate, endDate, kpi,
						threshold, uLimit, lLimit);
				for (NVDeviceDataWrapper dataWrapper : failuredDeviceData) {
					NVDeviceDataWrapper deviceDataWrapper = iDriveTestKPIDao.getFailureDeviceRemark(startDate, endDate,
							dataWrapper.getDeviceId());
					dataWrapper.setRemark(deviceDataWrapper.getRemark());
					dataWrapper.setStartTime(deviceDataWrapper.getStartTime());
				}
				logger.info("Returning result from getFailureDeviceData with size {}", failuredDeviceData.size());
				return failuredDeviceData;
			}
		} catch (Exception e) {
			logger.error("Exception in getDeviceData : {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Gets the KPI data for day wise.
	 *
	 * @param startDate  the start date
	 * @param endDate    the end date
	 * @param dataType   the data type
	 * @param deviceid   the deviceid
	 * @param kpi        the kpi
	 * @param threshold  the threshold
	 * @param uLimit     the u limit
	 * @param lLimit     the l limit
	 * @param resultType the result type
	 * @return the KPI data for day wise
	 * @throws RestException
	 */
	@Override
	public List<NVDeviceDataWrapper> getKPIDataForDayWise(Long startDate, Long endDate, String dataType,
			String deviceid, String kpi, Double threshold, Integer uLimit, Integer lLimit, String resultType) {
		logger.info(
				"Going to getDeviceData for startDate{}, endDate{}, dataType{}, deviceid{}, kpi {}, threshold {}, uLimit {}, lLimit {},resultType{}",
				startDate, endDate, dataType, deviceid, kpi, threshold, uLimit, lLimit, resultType);
		List<NVDeviceDataWrapper> deviceDataForDayWise = null;
		try {
			if (dataType.equalsIgnoreCase(StealthConstants.KPI)) {
				// new Date(startDate), new Date(endDate)
				deviceDataForDayWise = iDriveTestKPIDao.getKPIDataForDayWise(startDate, endDate, deviceid, uLimit,
						lLimit, StealthUtils.getFilterForDayWise(new Date(startDate), new Date(endDate), kpi, threshold,
								resultType));
				logger.info("Returning result from getDeviceDataForDayWise with size {}", deviceDataForDayWise.size());
				return deviceDataForDayWise;
			} else if (dataType.equalsIgnoreCase(StealthConstants.FAILURE)) {
				deviceDataForDayWise = iDriveTestKPIDao.getFailureDataForDayWise(startDate, endDate, deviceid, uLimit,
						lLimit);
				logger.info("Returning result from getFailureDeviceDataForDayWise with size {}",
						deviceDataForDayWise.size());
			}
		} catch (Exception e) {
			logger.error("Exception in getDeviceData : {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
		return deviceDataForDayWise;
	}

	/**
	 * Gets the KPI data count.
	 *
	 * @param startDate  the start date
	 * @param endDate    the end date
	 * @param dataType   the data type
	 * @param kpi        the kpi
	 * @param threshold  the threshold
	 * @param resultType the result type
	 * @return the KPI data count
	 * @throws RestException the rest exception
	 */
	@Override
	public Long getKPIDataCount(Long startDate, Long endDate, String dataType, String kpi, Double threshold,
			String resultType) {
		logger.info(
				"Going to getDeviceDataCount for startDate{}, endDate{}, dataType{}, kpi {}, threshold {}, resultType {}",
				startDate, endDate, dataType, kpi, threshold, resultType);
		try {
			if (dataType.equalsIgnoreCase(StealthConstants.KPI)) {
				Long deviceDataCount = iDriveTestKPIDao.getKPIDataCount(startDate, endDate,
						StealthUtils.getFilterAndValueMap(StealthConstants.IS_DEVICEWISE, kpi, threshold,
								new Date(startDate), new Date(endDate), true, resultType));
				logger.info("Returning result from getDeviceDataCount {}", deviceDataCount);
				return deviceDataCount;
			} else {
				Long failuredDeviceDataCount = iDriveTestKPIDao.getFailureDataCount(startDate, endDate);
				logger.info("Returning result from getFailureDeviceDataCount {}", failuredDeviceDataCount);
				return failuredDeviceDataCount;
			}
		} catch (Exception e) {
			logger.error("Exception in getDeviceDataCount : {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Gets the KPI data count for day wise.
	 *
	 * @param startDate  the start date
	 * @param endDate    the end date
	 * @param dataType   the data type
	 * @param deviceId   the device id
	 * @param kpi        the kpi
	 * @param threshold  the threshold
	 * @param resultType the result type
	 * @return the KPI data count for day wise
	 * @throws RestException the rest exception
	 */
	@Override
	public Long getKPIDataCountForDayWise(Long startDate, Long endDate, String dataType, String deviceId, String kpi,
			Double threshold, String resultType) {
		logger.info("Going to getDeviceDataCountForDayWise for startDate{}, endDate{}, dataType{}, deviceId {}",
				startDate, endDate, dataType, deviceId);
		try {
			if (dataType.equalsIgnoreCase(StealthConstants.KPI)) {
				Long deviceDataCount = iDriveTestKPIDao.getKPIDataCountForDayWise(startDate, endDate, deviceId,
						StealthUtils.getFilterForDayWise(new Date(startDate), new Date(endDate), kpi, threshold,
								resultType));
				logger.info("Returning result from getDeviceDataCountForDayWise {}", deviceDataCount);
				return deviceDataCount;
			} else {
				Long failuredDeviceDataCount = iDriveTestKPIDao.getFailureDataCountForDayWise(startDate, endDate,
						deviceId);
				logger.info("Returning result from getFailureDeviceDataCount {}", failuredDeviceDataCount);
				return failuredDeviceDataCount;
			}
		} catch (Exception e) {
			logger.error("Exception in getDeviceDataCount : {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

}
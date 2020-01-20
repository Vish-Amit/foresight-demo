package com.inn.foresight.module.nv.livedrive.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.inn.commons.http.HttpGetRequest;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.product.legends.dao.ILegendRangeDao;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.livedrive.constants.LiveDriveConstant;
import com.inn.foresight.module.nv.livedrive.dao.service.ILiveDriveHbaseDao;
import com.inn.foresight.module.nv.livedrive.service.ILiveDriveKafkaService;
import com.inn.foresight.module.nv.livedrive.service.ILiveDriveService;
import com.inn.foresight.module.nv.livedrive.utils.LiveDriveUtil;
import com.inn.foresight.module.nv.livedrive.wrapper.TrackSpanSummaryWrapper;
import com.inn.foresight.module.nv.livedrive.wrapper.TrackSpanWrapper;
import com.inn.product.security.utils.AuthenticationCommonUtil;

/**
 * @author Innoeye
 *
 *         LiveDriveServiceImpl
 */

@Service("LiveDriveServiceImpl")
public class LiveDriveServiceImpl implements ILiveDriveService {

	/** Logger. */
	private static Logger logger = LogManager.getLogger(LiveDriveServiceImpl.class);

	/** Live Drive Kafka Service. */
	@Autowired
	private ILiveDriveHbaseDao liveDriveHbaseDao;
	
	@Autowired
	ILiveDriveKafkaService ilivedrivekafkaservice;

	@Autowired
	ILegendRangeDao legendRangeDao;

	/**
	 * Sync live drive json into Kafka.
	 * 
	 * @param inputFile
	 * @return application/json
	 * @throws RestException
	 */

	@Override
	@Transactional
	public String syncJsonDataToKafka(TrackSpanWrapper trackSpanWrapper) {
		try {
			logger.info("In Class " + this.getClass().getName() + " Method syncJsonDataToKafka trackSpanWrapper : {} ",
					trackSpanWrapper.getDriveid());
			processLiveDriveJsonData(trackSpanWrapper);
			return AuthenticationCommonUtil.checkForValueEncryption(ForesightConstants.SUCCESS_JSON,null);
		} catch (RestException e) {
			logger.error("Error occurred @class" + this.getClass().getName() + " Method : syncJsonDataToKafka {}",
					Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.FAILURE_JSON);

		} catch (Exception e) {
			logger.error("Error occurred  @class" + this.getClass().getName() + " Method : syncJsonDataToKafka {}",
					Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.FAILURE_JSON);
		}
	}

	/**
	 * Send live drive remaining data to kafka.
	 *
	 * @param inputFile
	 *            the input file
	 * @return the string
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	public String recordAllLocation(InputStream inputFile) {
		try {
			return ilivedrivekafkaservice.sendInputStreamToKafka(inputFile);
		} catch (RestException e) {
			Utils.getStackTrace(e);
		}
		return ForesightConstants.FAILURE_JSON;
	}

	/**
	 * Find trips by imei and tripid.
	 *
	 * @param imei
	 *            the imei
	 * @param driveid
	 *            the driveid
	 * @param userid
	 *            the userid
	 * @param trackid
	 *            the trackid
	 * @return the track span summary wrapper
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	public String findTripsByImeiAndTripid(Integer driveid, Integer trackid, String type) {
		logger.info("Inside @class:" + this.getClass().getName()
				+ " Method findTripsByImeiAndTripid for driveid {} and trackid {}", driveid, trackid);
		try {

			String url = ConfigUtils.getString(ConfigUtil.MICRO_SERVICE_BASE_URL)+ConfigUtils.getString(NVConfigUtil.LIVE_DRIVE_MICRO_SERVICE_URL)+ driveid + "&limit=" + trackid;
         	String csvFile = null;
			if (type.equalsIgnoreCase(LiveDriveConstant.ONGOING)) {
				csvFile = LiveDriveUtil.convertJsonToCsv(sendRequestToGetDataFromHbase(url));
			} else if (type.equalsIgnoreCase(LiveDriveConstant.SUMMARY)) {
				csvFile = LiveDriveUtil.getSummaryCSV(sendRequestToGetDataFromHbase(url));
			}
			return csvFile;
		} catch (Exception e) {
			logger.error("Error occured @class:" + this.getClass().getName() + " @Method: findTripsByImeiAndTripid: {}",
					e.getMessage());
		}
		return null;
	}

	/**
	 * Process live drive json data.
	 * 
	 * @param trackSpanWrapper
	 * @throws RestException
	 * @throws DaoException
	 */
	private void processLiveDriveJsonData(TrackSpanWrapper trackSpanWrapper) {
		ilivedrivekafkaservice.sendDataToKafka(trackSpanWrapper);
	}

	/**
	 * Send get.
	 *
	 * @param url
	 *            the url
	 * @return the track span summary wrapper
	 * @throws Exception
	 *             the exception
	 */
	private TrackSpanSummaryWrapper sendRequestToGetDataFromHbase(String url)  {
		logger.info("sendGet live drive hbase server url : {}", url);

		String result = (new HttpGetRequest(url)).toString();
		logger.info("Microservice result : {}",!result.isEmpty());

		TrackSpanSummaryWrapper spanSummaryWrapper = null;
		if (result != null && LiveDriveUtil.isValidJsonObject(result)) {
			Gson gson = new Gson();
			spanSummaryWrapper = gson.fromJson(result, TrackSpanSummaryWrapper.class);
			logger.info("Inside @class:" + this.getClass().getName() + " Method sendGet for live drive Json :{}",
					spanSummaryWrapper.toString());
		}
		return spanSummaryWrapper;
	}

@Override
	public TrackSpanSummaryWrapper getLiveDriveDataByTaskId(Integer taskId, Integer limit) {

		logger.info("getLiveDriveDataByTaskId taskid {} ", taskId);
		TrackSpanSummaryWrapper trackSpanSummaryWrapper = new TrackSpanSummaryWrapper();
		try {
			List<TrackSpanWrapper> trackSpanWrapperList = new ArrayList<>();
			HBaseResult result = liveDriveHbaseDao.getResultForDriveResultByTaskId(taskId);
			if (result != null && result.listCells() != null && !result.listCells().isEmpty()) {
				int size = result.listCells().size();
				logger.info("size {}", size);
				getTrackSpanWrapperList(limit, result,trackSpanWrapperList);
				trackSpanSummaryWrapper.setTrackSpanWrappers(trackSpanWrapperList);
				return trackSpanSummaryWrapper;
			}
		} catch (Exception e) {
			logger.info("error in getting json for task id : {} with trace ", taskId, ExceptionUtils.getStackTrace(e));
		}

		return trackSpanSummaryWrapper;
	}

	private void getTrackSpanWrapperList(Integer limit, HBaseResult result,
			List<TrackSpanWrapper> trackSpanWrapperList) {
		byte[] columfamily = Bytes.toBytes(ConfigUtils.getString(LiveDriveConstant.LIVE_DRIVE_COLUMNS_FAMILY));
		logger.info("columfamily {}", columfamily);
		List<byte[]> hbaseKeyList = new ArrayList<>(result.get().getFamilyMap(columfamily).keySet());
		List<Integer> keyList = hbaseKeyList.parallelStream().map(b -> Integer.parseInt(Bytes.toString(b)))
				.collect(Collectors.toList());
		Collections.sort(keyList);
		logger.info("Found the list {}",keyList);
		if (keyList.size()>0) {
			for (int i = limit + 1; i <= keyList.get(keyList.size() - 1); i++) {
				byte[] dataString = result.get().getValue(columfamily, Bytes.toBytes(String.valueOf(i)));
				if (dataString != null) {
					String data = Bytes.toString(dataString);
					Gson gson = new Gson();
					TrackSpanWrapper trackSpanWrapper = gson.fromJson(data, TrackSpanWrapper.class);
					trackSpanWrapperList.add(trackSpanWrapper);
				}

			} 
		}
	}
}

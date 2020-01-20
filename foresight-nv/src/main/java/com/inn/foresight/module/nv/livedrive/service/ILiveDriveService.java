package com.inn.foresight.module.nv.livedrive.service;

import java.io.InputStream;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.livedrive.wrapper.TrackSpanSummaryWrapper;
import com.inn.foresight.module.nv.livedrive.wrapper.TrackSpanWrapper;

public interface ILiveDriveService {

	/**
	 * Sync Live Drive Json in Kafka.
	 * 
	 * @param trackSpanWrapper
	 * @return application/Json
	 * @throws RestException
	 */
	String syncJsonDataToKafka(TrackSpanWrapper trackSpanWrapper);

	/**
	 * Record all location.
	 *
	 * @param inputFile
	 *            the input file
	 * @return the string
	 * @throws RestException
	 *             the rest exception
	 */
	String recordAllLocation(InputStream inputFile);
	
	TrackSpanSummaryWrapper getLiveDriveDataByTaskId(Integer taskId, Integer limit);

	/**
	 * Find trips by imei and tripid.
	 *
	 * @param imei
	 *            the imei
	 * @param tripid
	 *            the tripid
	 * 
	 * @param trackid
	 *            the trackid
	 *            
	 * @param type
	 *            the type
	 * @return the track span summary wrapper
	 * @throws RestException
	 *             the rest exception
	 */
	String findTripsByImeiAndTripid(Integer tripid, Integer trackid, String type);
}

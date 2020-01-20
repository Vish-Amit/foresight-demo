package com.inn.foresight.module.nv.livedrive.service.impl;

import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.kafka.KafkaUtils;
import com.inn.foresight.module.nv.livedrive.service.ILiveDriveKafkaService;
import com.inn.foresight.module.nv.livedrive.wrapper.TrackSpanWrapper;

@Service("LiveDriveKafkaServiceImpl")
public class LiveDriveKafkaServiceImpl implements ILiveDriveKafkaService {

	/** Logger. */
	private static Logger logger = LogManager.getLogger(LiveDriveKafkaServiceImpl.class);

	@Override
	public String sendDataToKafka(TrackSpanWrapper trackSpanWrapper) {
		logger.info("send live drive data in kafka");
		String data = new Gson().toJson(trackSpanWrapper);
		String topicName = ConfigUtils.getString(ConfigUtil.LIVE_DRIVE_TOPIC);
		try {
			KafkaUtils.syncDataInKafka(topicName, data);
		} catch (Exception e) {
			logger.error("error sending data to Kafka producer, error : {} ", Utils.getStackTrace(e));
			throw new RestException("error sending data to Kafka producer");
		}
		return ForesightConstants.SUCCESS_JSON;
	}

	/**
	 * Method for sending the file data to the Kafka cluster to the topic.
	 *
	 * @param input
	 *            the input
	 * @param topicName
	 *            the topic name
	 * @return the string
	 */
	@Override
	public String sendInputStreamToKafka(InputStream input) {
		logger.info("Inside sendDataToKafka method");
		String topicName = ConfigUtils.getString(ConfigUtil.LIVE_DRIVE_REMAINING_TOPIC);
		try {
			KafkaUtils.syncInputStreamInKafka(topicName, input);
			logger.info("Kafka Producer Produce  Data Successfully : {}", topicName);
			return ForesightConstants.SUCCESS_JSON;

		} catch (Exception e) {
			logger.error("error sending data to Kafka producer, error " + e.getMessage());
		}
		return ForesightConstants.FAILURE_JSON;
	}

}

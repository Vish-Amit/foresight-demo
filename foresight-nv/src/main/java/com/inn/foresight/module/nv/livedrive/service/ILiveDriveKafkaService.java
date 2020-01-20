package com.inn.foresight.module.nv.livedrive.service;

import java.io.InputStream;

import com.inn.foresight.module.nv.livedrive.wrapper.TrackSpanWrapper;

public interface ILiveDriveKafkaService {

	String sendDataToKafka(TrackSpanWrapper trackSpanWrapper);
	
	/**
	 * Send data to kafka.
	 *
	 * @param input the input
	 * @param topicName the topic name
	 * @return the string
	 */
	String sendInputStreamToKafka(InputStream input);
	
}

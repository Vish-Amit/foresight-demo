package com.inn.foresight.module.nv.kafka;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;

/** The Class KafkaUtils. */
/**
 * @author ist
 *
 */
public class KafkaUtils {

	/** The logger. */
	private static Logger logger = LogManager.getLogger(KafkaUtils.class);


	private KafkaUtils() {
		super();
	}

	
	/**
	 * Gets the topic name with partitions.
	 *
	 * @return the topic name with partitions
	 */
	public static Map<String, Integer> getTopicNameWithPartitions() {
		Iterator<String> topicList = ConfigUtils.getKeys("TOPIC");

		logger.info("getting topic List : {}", topicList.toString());

		Map<String, Integer> topicCountMap = new HashMap<>();

		while (topicList.hasNext()) {
			String topicKey = topicList.next();

			logger.info("loading topic keys : {}", topicKey);

			String topic = ConfigUtils.getString(topicKey);
			String partition = ConfigUtils.getString("PARTITION_" + topicKey);

			logger.info("Topic Key : {} , Topic: {},partition: {}" , topicKey, topic,partition);
			logger.info("getting topic  : {} and partition value  : {}", topic, partition);

			if (topic != null && topic.length() > 0 && partition != null && partition.length() > 0) {
				topicCountMap.put(topic, Integer.parseInt(partition));
			}
		}
		return topicCountMap;
	}

	/**
	 * Gets the total partitions.
	 *
	 * @param topicCountMap
	 *            the topic count map
	 * @return the total partitions
	 */
	public static int getTotalPartitions(Map<String, Integer> topicCountMap) {
		Set<String> set = topicCountMap.keySet();
		int totalPartition = 0;
		for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
			String topic = iterator.next();
			totalPartition = totalPartition + topicCountMap.get(topic);
		}

		return totalPartition;
	}

	public static void syncDataInKafka(String topicName, String data) {
		logger.info("topicName {}", topicName);
		Producer<String, String> producer = createProducer();
		ProducerRecord<String, String> record = new ProducerRecord<>(topicName, data);
		producer.send(record);
		producer.close();
		logger.info("successfully save live drive json");
	}

	public static void syncInputStreamInKafka(String topicName, InputStream inputStream) {
		logger.info("topicName {}", topicName);
		Producer<String, byte[]> producer = null;
		try {
			if (inputStream != null) {
				byte[] fileData = convertToByteArray(inputStream);
				producer = createByteProducer();
				ProducerRecord<String, byte[]> record = new ProducerRecord<>(topicName, fileData);
				producer.send(record);
				logger.info("successfully save live drive json");
			}
		} catch (IOException e) {
			Utils.getStackTrace(e);
		} finally {
			if (producer != null)
				producer.close();
		}
	}

	/**
	 * Convert to byte array.
	 *
	 * @param input
	 *            the input
	 * @return the byte[]
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static byte[] convertToByteArray(InputStream input) throws IOException {
		byte[] buffer = new byte[ForesightConstants.THOUSAND * ForesightConstants.TEN];
		int bytesRead;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}

		return output.toByteArray();

	}

	public static Producer<String, String> createProducer() {
		Properties props = new Properties();
		logger.info("Properties : {}", ConfigUtils.getString(ConfigUtil.META_BROKER_LIST).replace('_', ',') + " , "
				+ ConfigUtils.getString(ConfigUtil.CLIENT_ID) + " , " + ConfigUtils.getString(ConfigUtil.KEY_SERIALIZER)
				+ " , " + ConfigUtils.getString(ConfigUtil.VALUE_SERIALIZER));
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
				ConfigUtils.getString(ConfigUtil.META_BROKER_LIST).replace('_', ','));
		props.put(ProducerConfig.CLIENT_ID_CONFIG, ConfigUtils.getString(ConfigUtil.CLIENT_ID));
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ConfigUtils.getString(ConfigUtil.KEY_SERIALIZER));
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ConfigUtils.getString(ConfigUtil.VALUE_SERIALIZER));
		return new KafkaProducer<>(props);
	} 

	public static Producer<String, byte[]> createByteProducer() {
		Properties props = new Properties();
		logger.info("Properties : {}", ConfigUtils.getString(ConfigUtil.META_BROKER_LIST).replace('_', ',') + " , "
				+ ConfigUtils.getString(ConfigUtil.CLIENT_ID) + " , " + ConfigUtils.getString(ConfigUtil.KEY_SERIALIZER)
				+ " , " + ConfigUtils.getString(ConfigUtil.VALUE_SERIALIZER));
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
				ConfigUtils.getString(ConfigUtil.META_BROKER_LIST).replace('_', ','));
		props.put(ProducerConfig.CLIENT_ID_CONFIG, ConfigUtils.getString(ConfigUtil.CLIENT_ID));
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ConfigUtils.getString(ConfigUtil.KEY_SERIALIZER));
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ConfigUtils.getString(ConfigUtil.VALUE_SERIALIZER));
		return new KafkaProducer<>(props);
	}

}

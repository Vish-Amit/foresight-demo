package com.inn.foresight.module.nv.livedrive.utils;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

/** The Class KafkaPartitioner. */
@SuppressWarnings("deprecation")
public class KafkaPartitioner implements Partitioner {
	
	/**
	 * Instantiates a new kafka partitioner.
	 *
	 * @param props the props
	 */
	public KafkaPartitioner(VerifiableProperties props) {
		//empty
	}

	/**
	 * Partition.
	 *
	 * @param key the key
	 * @param a_numPartitions the a num partitions
	 * @return the int
	 */
	public int partition(Object key, int a_numPartitions) {
		int partition = 0;
		String stringKey = (String) key;
		int offset = stringKey.lastIndexOf('.');
		if (offset > 0) {
			partition = Integer.parseInt(stringKey.substring(offset + 1)) % a_numPartitions;
		}
		return partition;
	}
}
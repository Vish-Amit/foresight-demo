package com.inn.foresight.module.nv.pushnotification.notificationfile.dao;

import java.io.IOException;
import java.io.InputStream;



/** The Interface INotificationFileHDFSDao. */
public interface INotificationFileHDFSDao {

	/**
	 * Persist notification file to HDFS.
	 *
	 * @param inputStream the input stream
	 * @param destination the destination
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void persistNotificationFileToHDFS(InputStream inputStream , String destination) throws IOException;

}

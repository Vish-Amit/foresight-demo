package com.inn.foresight.module.nv.pushnotification.notificationfile.dao.impl;


import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.foresight.core.generic.dao.impl.hdfs.AbstractHDFSDao;
import com.inn.foresight.module.nv.pushnotification.notificationfile.dao.INotificationFileHDFSDao;

/**
 * The Class NotificationFileHDFSDaoImpl.
 *
 * @author innoeye
 */
@Repository("NotificationFileHDFSDaoImpl")
public class NotificationFileHDFSDaoImpl extends AbstractHDFSDao implements INotificationFileHDFSDao{
	
	/** The logger. */
	Logger logger = LogManager.getLogger(NotificationFileHDFSDaoImpl.class);

	/**
	 * Persist  inputStream of Notification file to HDFS.
	 *
	 * @param inputStream the inputStream of file to insert in HDFS
	 * @param destination the HDFS path where to copy given inputStream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public void persistNotificationFileToHDFS(InputStream inputStream, String destination) throws IOException {
		logger.info("Going to persistNotificationFileToHDFS fileName : {}",destination);
		createFileInHdfs(inputStream, destination);
		logger.info("Done persistNotificationFileToHDFS");
	}

	
}

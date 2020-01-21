package com.inn.foresight.core.attachment.dao;

import java.io.IOException;
import java.io.InputStream;

public interface IAttachmentDao {

	/**
	 * Save attachment.
	 *
	 * @param inputStream
	 *            the input stream
	 * @param path
	 *            the path
	 * @return the string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	void saveAttachment(InputStream inputStream, String path) throws IOException;
	
	/**
	 * Download from HDFS.
	 *
	 * @param filePath the file path
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	byte[] downloadFromHDFS(String filePath) throws IOException;

	String isFileExistInHDFS(String filePath) throws IOException;
}

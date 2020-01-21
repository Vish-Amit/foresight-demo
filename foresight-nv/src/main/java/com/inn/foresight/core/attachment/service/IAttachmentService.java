package com.inn.foresight.core.attachment.service;

import java.io.IOException;
import java.io.InputStream;

public interface IAttachmentService {

	/**
	 * Save attachment.
	 *
	 * @param inputStream
	 *            the input stream
	 * @param path
	 *            the path
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void saveAttachmentInHDFS(InputStream inputStream, String path) throws IOException;

	public void saveAttachment(InputStream inputStream, String path) throws IOException;

	/**
	 * Download from HDFS.
	 *
	 * @param filePath the file path
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	byte[] downloadFromHDFS(String filePath) throws IOException;

	String isFileExistInHDFS(String filePath) throws IOException;

}

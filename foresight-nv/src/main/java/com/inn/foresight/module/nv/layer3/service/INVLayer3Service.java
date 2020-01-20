package com.inn.foresight.module.nv.layer3.service;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.app.exceptions.InvalidEncryptionException;

/**
 * The Interface INVLayer3Service.
 *
 * @author innoeye
 * date - 29-Dec-2017 1:44:52 PM
 */
public interface INVLayer3Service {
	
	
	/**
	 * Persist WO recipe QMDL files.
	 *
	 * @param mappingId the mapping id
	 * @param isRetried the is retried
	 * @param fileStream the file stream
	 * @param fileName the name of file to insert in HDFS
	 * @param fileType the file type
	 * @param operatorName 
	 * @return file Path where file got save in hdfs
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws DaoException Signals that an Dao exception has occurred.
	 * @throws RestException Signals that an Business exception has occurred.
	 * @throws NumberFormatException the number format exception
	 * @throws InvalidEncryptionException the invalid encryption exception
	 */
	/*void syncWorkorderFile(Integer woRecipeMappingId, Boolean isRetried, InputStream fileStream, String fileName, String fileType)
			throws IOException, InvalidEncryptionException;*/

	
	String syncMultipleWorkorderFiles(HttpServletRequest httpServletRequest);

	
	void syncStealthWorkorderFile(Integer stealthTaskResultId, InputStream fileStream,
			String fileName, String fileType) throws IOException, InvalidEncryptionException;

	void syncWorkOrderFileForBenchMarking(String woRecipeMappingIdList, Boolean isRetried, InputStream inputFile,
			String fileName, String fileType) throws IOException, InvalidEncryptionException;

	String syncWorkorderFile(Integer woRecipeMappingId, Boolean isRetried, InputStream fileStream, String fileName,
			String fileType, String taskId, String localFilePath) throws IOException, InvalidEncryptionException;
}

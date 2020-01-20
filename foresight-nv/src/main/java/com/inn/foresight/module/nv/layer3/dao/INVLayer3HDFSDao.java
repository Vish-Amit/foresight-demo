package com.inn.foresight.module.nv.layer3.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.apache.hadoop.fs.FileStatus;

import com.inn.core.generic.exceptions.application.DaoException;

/**
 * The Interface INVLayerHDFSDao.
 *
 * @author innoeye
 * date - 29-Dec-2017 6:58:15 PM
 */
public interface INVLayer3HDFSDao {

	/**
	 * Persist WO recipe QMDL file to HDFS.
	 *
	 * @param inputStream the inputStream of file to insert in HDFS
	 * @param destination the HDFS path where to copy given inputStream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void persistWORecipeQMDLFileToHDFS(InputStream inputStream , String destination) throws IOException;

	/**
	 * Copy fil from hdfs to local.
	 * @param hdfsFile hdfs absolute path
	 * @param localpath local path
	 * @param fileName name of file saved on local
	 * @return result
	 * @throws DaoException
	 */
	 String copyFileFromHdfsToLocalPath(String hdfsFile, String localpath, String fileName) ;
	 
	 /**
		 * Gets File From Hdfs To byte[].
		 * @param filePath
		 * @return byte[] fileData
		 * @throws DaoException
		 */
	 byte[] getFileFromHDFS(String filePath) ;

	boolean isFileExist(String filePath) ;
	ZipInputStream getZipSteamFromPath(String hdfsFile, String fileName);
	
	List<FileStatus> getAllFilesFromDirectory(String filePath);

	void deleteFileFromHDFS(String filePath) ;

	void copyFileFromLocalToHDFS(String processedFilePath, String trim) throws IOException;

	void deleteFileFromHDFSRecursively(String filePath);

	Boolean persistFileToHDFS(InputStream inputStream, String destination) throws IOException;

	Long getHDFSFileSize(String filePath);
}

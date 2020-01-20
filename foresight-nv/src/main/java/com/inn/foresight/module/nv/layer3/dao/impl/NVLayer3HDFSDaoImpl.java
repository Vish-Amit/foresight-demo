package com.inn.foresight.module.nv.layer3.dao.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.dao.impl.hdfs.AbstractHDFSDao;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.layer3.dao.INVLayer3HDFSDao;

/**
 * The Class NVLayerHDFSDaoImpl.
 *
 * @author innoeye
 * date - 29-Dec-2017 6:58:46 PM
 */
@Repository("NVLayer3HDFSDaoImpl")
public class NVLayer3HDFSDaoImpl extends AbstractHDFSDao implements INVLayer3HDFSDao{
	
	/** The logger. */
	Logger logger = LogManager.getLogger(NVLayer3HDFSDaoImpl.class);

	/**
	 * Persist WO recipe inputStream of QMDL file to HDFS.
	 *
	 * @param inputStream the inputStream of file to insert in HDFS
	 * @param destination the HDFS path where to copy given inputStream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public void persistWORecipeQMDLFileToHDFS(InputStream inputStream, String destination) throws IOException {
		logger.info("Going to persitRecipeQMDLFilesToHDFS fileName : {}",destination);
		createFileInHdfs(inputStream, destination);
		logger.info("Done persitRecipeQMDLFilesToHDFS");
	}


	/**
	 * Copy File From Hdfs To Local Path.
	 * @param hdfsFile
	 * @param localpath
	 * @param fileName
	 * @return
	 * @throws DaoException
	 */
	@Override
	public String copyFileFromHdfsToLocalPath(String hdfsFile,String localpath,String fileName )  {
		logger.info("going to Copy File from hdfs to Local {}   local {}  ", hdfsFile, localpath);
		Path hdfsFilePath=new Path(hdfsFile);
		File f = new File(localpath);	
		try {
			FileSystem fileSystem = getFileSystem();
			fileSystem.setWriteChecksum(false);
			fileSystem.setVerifyChecksum(false);
			if(fileSystem.exists(hdfsFilePath)&&f.exists()){
					fileSystem.copyToLocalFile(hdfsFilePath, new Path(localpath+fileName));
					return localpath+fileName;
			}
			else {
				throw new FileNotFoundException("file not found in HDFS");
			}
		} catch (IOException e) {
			logger.error("Error in copy File from hdfs to local {}  ", Utils.getStackTrace(e));
			throw new DaoException("Error in Copy File from HDFS to Local");
 		}		
	}
	
	/**
	 * Gets File From Hdfs To byte[].
	 * @param filePath
	 * @return byte[] fileData
	 * @throws DaoException
	 */
	@Override
	public byte[] getFileFromHDFS(String filePath)  {
		byte[] fileData = null;
		try {
			FileSystem fileSystem = getFileSystem();
			Path path = new Path(filePath);
			if(!fileSystem.exists(path)) {
				throw new DaoException("Unable to find file in HDFS: " + filePath);
			}
			try(FSDataInputStream dataInputStream = fileSystem.open(path)){
			fileData = new byte[dataInputStream.available()];
			while(dataInputStream.read(fileData)>0) ;
			}
			
		} catch (IOException e) {
			logger.error("Error while getting byte[] from HDFS {}  ", Utils.getStackTrace(e));
		}
		return fileData;
	}
	
	/**
	 * Gets File From Hdfs To byte[].
	 * @param filePath
	 * @return byte[] fileData
	 * @throws DaoException
	 */
	@Override
	public boolean isFileExist(String filePath)  {
		try {
			FileSystem fileSystem = getFileSystem();
			Path path = new Path(filePath);
			if(fileSystem.exists(path)) {
				return true;
			}
		} catch (IOException e) {
			logger.error("Error while checking file in HDFS {}  ", Utils.getStackTrace(e));
		}
		return false;
	}
	@Override
	public void deleteFileFromHDFS(String filePath)  {
		logger.info("inside the method deleteFileFromHDFS {}",filePath);
		try {
		FileSystem fileSystem = getFileSystem();
		Path path = new Path(filePath);
		
			if (fileSystem.exists(path)) {
				fileSystem.delete(path, false);
			}
		} catch (Exception e) {
			logger.error("Exception inside the method deleteFileFromHDFS {}", Utils.getStackTrace(e));
		}
	}
	
	
	@Override
	public void deleteFileFromHDFSRecursively(String filePath)  {
		logger.info("inside the method deleteFileFromHDFS {}",filePath);
		try {
		FileSystem fileSystem = getFileSystem();
		Path path = new Path(filePath);
		
			if (fileSystem.exists(path)) {
				fileSystem.delete(path, true);
			}
		} catch (Exception e) {
			logger.error("Exception inside the method deleteFileFromHDFS {}", Utils.getStackTrace(e));
		}
	}
	
	@Override
	public ZipInputStream getZipSteamFromPath(String hdfsFile, String fileName) {
		logger.info("going to getZipSteamFromPath  {},{} ", hdfsFile, fileName);
		try {
			// Please do not use try-with-resources
			FileSystem fileSystem = getFileSystem();
			try(FSDataInputStream a = fileSystem.open(new Path(hdfsFile))){
				return new ZipInputStream(a);
			}
			
		} catch (Exception e) {
			logger.error("Error while fetching zipStream {}", Utils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public List<FileStatus> getAllFilesFromDirectory(String filePath) {
		try {
			FileSystem fileSystem = getFileSystem();
			Path path = new Path(filePath);
			if (fileSystem.getFileStatus(path).isDirectory()) {
				return Arrays.asList(fileSystem.listStatus(path));
			}
		} catch (IOException e) {
			logger.error("Error while checking file in HDFS {}  ", Utils.getStackTrace(e));
		}
		return new ArrayList<>();
	}


	@Override
	public void copyFileFromLocalToHDFS(String processedFilePath, String destination) throws IOException {
		logger.info("Going to copyFileFromLocalToHDFS fileName : {}",destination);
		copyFromLocalFile(processedFilePath, destination);
		logger.info("Done copyFileFromLocalToHDFS");
	}
	
	@Override
	public Boolean persistFileToHDFS(InputStream inputStream, String destination) throws IOException {
		logger.info("Going to write data into hdfs filepath : {}",destination);
		try {
			createFileInHdfs(inputStream, destination);
			logger.info("file write successfully into hdfs");
			return true;
		} catch (Exception e) {
			logger.info("Exception while writing data into hdfs {}",ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public Long getHDFSFileSize(String filePath) {
		logger.info("Going to return file size for file at path : {}", filePath);
		Long fileSize = null;
		if (!StringUtils.isBlank(filePath)) {
			try {
				FileSystem fileSystem = getFileSystem();
				Path path = new Path(filePath);
				if (fileSystem.exists(path)) {
					fileSize = fileSystem.getFileStatus(path).getLen();
					logger.info("file exists! file size is: {}", fileSize);
				}
			} catch (IOException e) {
				logger.error("File not found while fetching size at path: {}, Exception: {}", filePath,
						Utils.getStackTrace(e));
			}
		}
		return fileSize;
	}

}
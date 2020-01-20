package com.inn.foresight.module.nv.inbuilding.hdfs.dao.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.foresight.core.generic.dao.impl.hdfs.AbstractHDFSDao;
import com.inn.foresight.module.nv.inbuilding.hdfs.dao.IInBuildingHDFSDao;

/**
 * The Class InBuildingHDFSDaoImpl.
 *
 * @author innoeye
 * date - 29-Dec-2017 6:58:46 PM
 */

public class InBuildingHDFSDaoImpl extends AbstractHDFSDao implements IInBuildingHDFSDao{
	  /** The logger. */
	  private static Logger logger = LogManager.getLogger(InBuildingHDFSDaoImpl.class);

	@Override
	public String copyFileFromHdfsToLocalPath(String hdfsFile,String localpath,String fileName ) throws IOException {
		logger.info("going to Copy File from hdfs to Local {}   local {}  ", hdfsFile, localpath);
		Path hdfsFilePath=new Path(hdfsFile);
		File f = new File(localpath);
		try {
			FileSystem fileSystem = getFileSystem();
			if(fileSystem.exists(hdfsFilePath)&&f.exists()){
					fileSystem.copyToLocalFile(hdfsFilePath, new Path(localpath+fileName));
					return localpath+fileName;
			}
		} catch (IOException e) {
			logger.error("Error in copy File from hdfs to local {}  ", e);
			throw new IOException("Error in Copy File from HDFS to Local");
 		}
		return null;

	}

	/**
	 * Persist WO recipe inputStream of QMDL file to HDFS.
	 *
	 * @param inputStream the inputStream of file to insert in HDFS
	 * @param destination the HDFS path where to copy given inputStream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public void persistInBuildingFileToHDFS(InputStream inputStream, String destination) throws IOException {
		createFileInHdfs(inputStream, destination);
	}


}

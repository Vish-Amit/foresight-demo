package com.inn.foresight.module.nv.inbuilding.hdfs.dao;

import java.io.IOException;
import java.io.InputStream;


/** The Interface IInBuildingHDFSDao. */
public interface IInBuildingHDFSDao {

	/**
	 * Persist in building file to HDFS.
	 *
	 * @param inputStream the input stream
	 * @param destination the destination
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void persistInBuildingFileToHDFS(InputStream inputStream , String destination) throws IOException;


	 /**
 	 * Copy file from hdfs to local path.
 	 *
 	 * @param hdfsFile the hdfs file
 	 * @param localpath the localpath
 	 * @param fileName the file name
 	 * @return the string
 	 * @throws IOException Signals that an I/O exception has occurred.
 	 */
 	String copyFileFromHdfsToLocalPath(String hdfsFile, String localpath, String fileName) throws IOException;

}

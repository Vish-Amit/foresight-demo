package com.inn.foresight.core.report.dao;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.hadoop.fs.FSDataOutputStream;

import com.inn.commons.hadoop.hbase.HBaseResult;

public interface IReportGenerationHDFSDao {
	boolean saveFileToHdfs(File file, String filePath);

	byte[] getReportFromHdfs(Integer analyticsrepositoryid,String path) throws Exception;

	InputStream getInputStreamFromHdfs(String hdfsPath);
	
	void copyHdfsToServer(String serverPath,String path);

	FSDataOutputStream getHDFSOutputStream(String filePath, String fileName);

	Map<String, InputStream> getAllfiles(String directoryPath) throws IOException;

	
}

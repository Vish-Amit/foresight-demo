package com.inn.foresight.module.nv.report.dao;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.hadoop.fs.FSDataOutputStream;

public interface INVReportHdfsDao {
	boolean saveFileToHdfs(File file, String filePath);

	byte[] getReportFromHdfs(Integer analyticsrepositoryid,String path) throws Exception;

	byte[] getInputStreamFromHdfs(Integer analyticsrepositoryid, String hdfsPath);
	
	void copyHdfsToServer(String serverPath,String path);

	FSDataOutputStream getHDFSOutputStream(String filePath, String fileName);

	Map<String, byte[]> getAllfiles(String directoryPath) throws IOException;
}

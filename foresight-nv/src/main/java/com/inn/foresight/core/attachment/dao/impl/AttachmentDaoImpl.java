package com.inn.foresight.core.attachment.dao.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.foresight.core.attachment.dao.IAttachmentDao;
import com.inn.foresight.core.generic.dao.impl.hdfs.AbstractHDFSDao;
import com.inn.foresight.core.generic.utils.ForesightConstants;

@Repository("AttachmentDaoImpl")
public class AttachmentDaoImpl extends AbstractHDFSDao implements IAttachmentDao {

	private static Logger logger = LogManager.getLogger(AttachmentDaoImpl.class);
	
	@Override
	public void saveAttachment(InputStream inputStream, String path) throws IOException {
		createFileInHdfs(inputStream, path);
	}

	@Override
	public byte[] downloadFromHDFS(String filePath) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		InputStream inputStream = null;
		try {
	        FileSystem fileSystem = getFileSystem();
			URI uri = fileSystem.getUri();
			Path path = new Path(uri + filePath);
			if (!fileSystem.exists(path)) {
				throw new FileNotFoundException("filepath not exists");
			}
			inputStream = fileSystem.open(path);
			IOUtils.copyBytes(inputStream, outputStream, 4096, false);
		} catch (IOException ioEx) {
			logger.error("IOException while getting data from HDFS due to {}", ioEx);
		} catch (Exception ex) {
			logger.error("Exception while getting data from HDFS due to {}", ex);
		} finally {
			try {
			    logger.info("Inside downloadFromHDFS, Closing fileSystem");
			    if(inputStream != null) {
				inputStream.close();
			    }
				outputStream.close();
			} catch (IOException e) {
				logger.error("Exception while closing input/output stream while getting data from HDFS {}", e);
			}
		}
		return outputStream.toByteArray();
	}
	
	
	
	
	@Override
	public String isFileExistInHDFS(String filePath) throws IOException {
		logger.info("Inside isFileExistInHDFS filePath={} ",filePath);
		try {
	        FileSystem fileSystem = getFileSystem();
			URI uri = fileSystem.getUri();
			Path path = new Path(uri + filePath);
			if(fileSystem.exists(path)) {
				return ForesightConstants.TRUE_LOWERCASE;
			}else {
				return ForesightConstants.FALSE_LOWERCASE;
			}
		} catch (IOException ioEx) {
			logger.error("IOException while checking file exist in HDFS due to {}", ioEx);
			throw ioEx;
		} catch (Exception ex) {
			logger.error("Exception while checking file exist in HDFS due to {}", ex);
		} 
		return ForesightConstants.FALSE_LOWERCASE;
	}
}
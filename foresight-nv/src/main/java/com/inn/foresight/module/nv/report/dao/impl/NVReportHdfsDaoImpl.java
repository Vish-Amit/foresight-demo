package com.inn.foresight.module.nv.report.dao.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.inn.commons.Symbol;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.dao.impl.hdfs.AbstractHDFSDao;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.report.dao.INVReportHdfsDao;

@Service("NVReportHdfsDaoImpl")
public class NVReportHdfsDaoImpl extends AbstractHDFSDao implements INVReportHdfsDao {
	/** The logger. */
	private Logger logger = LogManager.getLogger(NVReportHdfsDaoImpl.class);
	private String gettingReport = "going to get Report from hdfs from path {}  {}  ";
	private String errorGettingBytes = "Error while getting byte[] from HDFS {}  ";

	@Override
	public boolean saveFileToHdfs(File file, String filePath) {
		logger.info("Inside method saveFileToHdfs on filePath {} ", filePath);
		try {
			createFileInHdfsForReport(file, filePath);
			logger.info("File successfully inserted in Hdfs");
			return true;
		} catch (IOException e) {
			logger.error("IOException inside method saveFileToHbase {} ", Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("Exception inside method saveFileToHbase {} ", Utils.getStackTrace(e));
		}
		return false;
	}

	private void createFileInHdfsForReport(File file, String filePath) throws IOException {
		if (file != null && filePath != null) {
			FileSystem fileSystem = getFileSystem();
			fileSystem.copyFromLocalFile(new Path(file.getAbsolutePath()), new Path(filePath));
		}
	}

	@Override
	public byte[] getReportFromHdfs(Integer analyticsrepositoryid, String hdfsPath) throws Exception {

		logger.info(gettingReport, analyticsrepositoryid, hdfsPath);

		byte[] fileData = null;
			// Please do not change to try-with-resources.
			FileSystem fileSystem = getFileSystem();
			Path path = new Path(hdfsPath);
			if (!fileSystem.exists(path)) {
				throw new IOException("Unable to find file in HDFS :   {}" + hdfsPath)   ;
			}
			try (FSDataInputStream dataInputStream = fileSystem.open(path)) {
				fileData = new byte[dataInputStream.available()];
				int read = dataInputStream.read(fileData);
				logger.debug("Bytes Read from Stream {}", read);
			}
			fileSystem.close();
		return fileData;
	}

	@Override
	public byte[] getInputStreamFromHdfs(Integer analyticsrepositoryid, String hdfsPath) {
		logger.info(gettingReport, analyticsrepositoryid, hdfsPath);

		try {
			// Please do not change to try-with-resources.
			FileSystem fileSystem = getFileSystem();
			Path path = new Path(hdfsPath);
			if (!fileSystem.exists(path)) {
				throw new DaoException("Unable to find file in HDFS: " + hdfsPath);
			}
			byte[] bytes;
			try (InputStream inputStream = fileSystem.open(path).getWrappedStream()) {
				bytes = IOUtils.toByteArray(inputStream);
			}
			return bytes;
		} catch (IOException e) {
			logger.error(errorGettingBytes, Utils.getStackTrace(e));
		}
		return new byte[]{};
	}

	@Override
	public void copyHdfsToServer(String serverPath, String hdfsPath) {
		logger.info(gettingReport, serverPath, hdfsPath);
		FSDataInputStream dataInputStream = null;
		FileSystem fileSystem = null;
		try {
			fileSystem = getFileSystem();
			Path path = new Path(hdfsPath);
			if (!fileSystem.exists(path)) {
				new File(serverPath);
				return;
			}
			dataInputStream = fileSystem.open(path);
			fileSystem.copyToLocalFile(path, new Path(serverPath));
		} catch (IOException e) {
			logger.error(errorGettingBytes, Utils.getStackTrace(e));
		} finally {
			try {
				if (dataInputStream != null) {
					dataInputStream.close();
				}
				if (fileSystem != null) {
					fileSystem.close();
				}
			} catch (Exception e) {
				logger.error("ERROR in Closing Stream {}", Utils.getStackTrace(e));
			}
		}
	}

	@Override
	public FSDataOutputStream getHDFSOutputStream(String filePath, String fileName) {
		try {
			FileSystem fileSystem = getFileSystem();
			Path hdfswritepath = new Path(filePath + Symbol.SLASH_FORWARD + fileName);
			return fileSystem.create(hdfswritepath);
		} catch (Exception e) {
			logger.error("Error while fetching  HDFS Output Stream {}", Utils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public Map<String, byte[]> getAllfiles(String directoryPath) throws IOException {
		Map<String, byte[]> filesMap = new HashMap<>();
		Path directory = new Path(directoryPath);
		FileSystem fs = getFileSystem();
		RemoteIterator<LocatedFileStatus> fileStatusListIterator = fs.listFiles(directory, true);
		while (fileStatusListIterator.hasNext()) {
			LocatedFileStatus fileStatus = fileStatusListIterator.next();
			// Please do not change to try-with-resources.
			try(InputStream wrappedStream = fs.open(fileStatus.getPath()).getWrappedStream()){
			byte[] bytes = com.amazonaws.util.IOUtils.toByteArray(wrappedStream);
			filesMap.put(fileStatus.getPath().getName(), bytes);
			}
		}
		fs.close();
		return filesMap;
	}
}

package com.inn.foresight.core.report.dao.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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
import com.inn.foresight.core.report.dao.IReportGenerationHDFSDao;

@Service("ReportGenerationHDFSDaoImpl")
public class ReportGenerationHDFSDaoImpl extends AbstractHDFSDao implements  IReportGenerationHDFSDao {
	/** The logger. */
	private Logger logger = LogManager.getLogger(ReportGenerationHDFSDaoImpl.class);
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
		try {
			// Please do not change to try-with-resources.
			FileSystem fileSystem = getFileSystem();
			Path path = new Path(hdfsPath);
			if (!fileSystem.exists(path)) {
				throw new Exception("Unable to find file in HDFS: " + hdfsPath);
			}
			FSDataInputStream dataInputStream = fileSystem.open(path);
			fileData = new byte[dataInputStream.available()];
			dataInputStream.read(fileData);

			dataInputStream.close();
			fileSystem.close();
		} catch (IOException e) {
			logger.error(errorGettingBytes, Utils.getStackTrace(e));
		}
		return fileData;
	}

	@Override
	public InputStream getInputStreamFromHdfs(String hdfsPath) {
		logger.info(gettingReport,  hdfsPath);

		try {
			// Please do not change to try-with-resources.
			FileSystem fileSystem = getFileSystem();
			Path path = new Path(hdfsPath);
			if (!fileSystem.exists(path)) {
				throw new DaoException("Unable to find file in HDFS: " + hdfsPath);
			}
			return fileSystem	.open(path)
								.getWrappedStream();
		} catch (IOException e) {
			logger.error(errorGettingBytes, Utils.getStackTrace(e));
		}
		return null;
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
				logger.error("ERROR in Closing Stream", Utils.getStackTrace(e));
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
	public Map<String, InputStream> getAllfiles(String directoryPath) throws IOException {
		Map<String, InputStream> filesMap = new HashMap<>();
		Path directory = new Path(directoryPath);
		FileSystem fs = getFileSystem();
		RemoteIterator<LocatedFileStatus> fileStatusListIterator = fs.listFiles(directory, true);
		while (fileStatusListIterator.hasNext()) {
			LocatedFileStatus fileStatus = fileStatusListIterator.next();
			filesMap.put(fileStatus	.getPath()
									.getName(),
					fs	.open(fileStatus.getPath())
						.getWrappedStream());
		}
		return filesMap;
	}
	
	
}

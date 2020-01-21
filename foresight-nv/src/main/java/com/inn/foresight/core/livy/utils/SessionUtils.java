package com.inn.foresight.core.livy.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.http.client.ClientProtocolException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.parser.JSONParser;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.livy.constants.LivyConstants;
import com.inn.foresight.core.livy.model.LivyApplication;
import com.inn.foresight.core.livy.model.Session;

/**
 * The Class SessionUtils.
 *
 * @author Harsh Pandya
 * 
 * @author Zafar
 */
public class SessionUtils {

	/** The Constant logger. */
	private static final  Logger logger = LogManager.getLogger(SessionUtils.class);
    
    /** The session request data. */
    private static String sessionRequestData = "{\"kind\": \"kindVal\",\"queue\": \"queueVal\",\"name\": \"sessionName\",\"driverMemory\":\"driverMemoryVal\",\"executorMemory\":\"executorMemoryVal\",\"numExecutors\":numExecutorsVal,\"jars\":[\"jarFileToUpload\"]}";

	/** The curr server counter. */
	public static Integer currServerCounter = 0;
	
	/** The pending server session map. */
	public static Map<String, List<String>> pendingServerSessionMap = new HashMap<>();
	
	/** The parser. */
	static JSONParser parser = new JSONParser();

	/**
	 * The Enum SessionStates.
	 */
	public enum SessionStates {
		STARTING, NOT_STARTED, IDLE, SHUTTING_DOWN, ERROR, DEAD, SUCCESS, BUSY
	}

	/**
	 * The Enum StatementStates.
	 */
	public enum StatementStates {
		WAITING, RUNNING, AVAILABLE, ERROR, CANCELLING, CANCELLED
	}

	/**
	 * Creates the session.
	 *
	 * @param app the app
	 * @param sessionUrl the session url
	 * @param hdfsConnectionUrl the hdfs connection url
	 * @param jarPath the jar path
	 * @return the session
	 */
	public static Session createSession(LivyApplication app, String sessionUrl, String jarPath) {
		String jarFileToUpload = getJarFiles(jarPath);
		String data = sessionRequestData;
		data = data.replace("driverMemoryVal", app.getDriverMemory());
		data = data.replace("executorMemoryVal", app.getExecutorMemory());
		data = data.replace("numExecutorsVal", app.getNumExecutor().toString());
		data = data.replace("kindVal", "spark");
		data = data.replace("queueVal", app.getQueue());
		data = data.replace("sessionName", app.getName());
		data = data.replace("jarFileToUpload", jarFileToUpload);
		return postCreateSessionRequest(data, sessionUrl);
	}

	/**
	 * Post create session request.
	 *
	 * @param payLoad the pay load
	 * @param sessionUrl the session url
	 * @return the session
	 */
	public static Session postCreateSessionRequest(String payLoad, String sessionUrl) {
		String response = null;
		Session session = null;
		try {
			logger.info("payLoad---------{}===========sessionUrl---------{}",sessionUrl,payLoad);
			response = HttpUtils.sendPostRequest(sessionUrl, payLoad);
			logger.info("response---------{}",response);
			response = response.substring(0, response.indexOf("appInfo") - 2) + "}";
			session = StatementUtils.objectMapper.fromJson(response, Session.class);
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException in postCreateSessionRequest----{}",ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			logger.error("IOException in postCreateSessionRequest----{}",ExceptionUtils.getStackTrace(e));
		}
		return session;
	}

	/**
	 * Gets the livy session.
	 *
	 * @param sessionId the session id
	 * @param sessionUrl the session url
	 * @return the livy session
	 */
	public static Session getLivySession(Integer sessionId, String sessionUrl) {
		Session session = null;
		try {
			String response = HttpUtils.sendGetRequest(sessionUrl + "/" + sessionId);
			response = response.substring(0, response.indexOf("appInfo") - 2) + "}";
			session = StatementUtils.objectMapper.fromJson(response, Session.class);
		} catch (Exception e) {
			logger.error("error in getLivySession   {}", ExceptionUtils.getStackTrace(e));
		}
		return session;
	}

	/**
	 * Check existing session.
	 *
	 * @param sessionId the session id
	 * @param sessionUrl the session url
	 * @return true, if successful
	 */
	public static boolean checkExistingSession(Integer sessionId, String sessionUrl) {
		Session session = getLivySession(sessionId, sessionUrl);
		try {
			if (session != null && (session.getState().equalsIgnoreCase(SessionStates.IDLE.name()) || session.getState().equalsIgnoreCase(SessionStates.BUSY.name())))
				return true;
		} catch (Exception e) {
			logger.error("error in checkExistingSession , Error Msg  {}", ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	/**
	 * Delete session.
	 *
	 * @param sessionId the session id
	 * @param sessionUrl the session url
	 * @return the string
	 */
	public static String deleteSession(Integer sessionId, String sessionUrl) {
		String response = null;
		try {
			response = HttpUtils.sendDeleteRequest(sessionUrl + LivyConstants.FORWARD_SLASH + sessionId);
		} catch (Exception e) {
			logger.error("error in postCreateSessionRequest   {}", ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	/**
	 * Gets the file names from hdfs dir.
	 *
	 * @param dirPath the dir path
	 * @return the file names from hdfs dir
	 */
	public static List<String> getFileNamesFromHdfsDir(String dirPath) {
		List<String> fileList = new ArrayList<>();
		try {
			FileSystem fileSystem = getHDFSConnection();
			if(fileSystem != null) {
				FileStatus[] fileStatus = fileSystem.listStatus(new Path(dirPath));
				for (FileStatus status : fileStatus) {
					fileList.add(status.getPath().toString());
				}	
			}
		} catch (Exception e) {
			logger.error("Livy jars not found {}", ExceptionUtils.getStackTrace(e));
		}
		return fileList;
	}
	
	/**
	 * Gets the HDFS connection.
	 *
	 * @return the HDFS connection
	 */
	public static FileSystem getHDFSConnection() {
		FileSystem fileSystem = null;
		try {
			final Configuration conf = new Configuration();
			conf.addResource(new Path(ConfigUtils.getString(ConfigEnum.HDFS_CONF_PATH) + LivyConstants.CORE_SITE_XML));
			conf.addResource(new Path(ConfigUtils.getString(ConfigEnum.HDFS_CONF_PATH) + LivyConstants.HDFS_SITE_XML));
			logger.info("Connecting to -- " + conf.get(LivyConstants.FS_PARAM_NAME));
			fileSystem = FileSystem.get(conf);
		} catch (final IOException e) {
			logger.info("IOException in connecting with HDFS {}", ExceptionUtils.getStackTrace(e));
		}
		return fileSystem;
	}

	/**
	 * Gets the jar files.
	 *
	 * @param hdfsConnectionUrl the hdfs connection url
	 * @param jarPath the jar path
	 * @return the jar files
	 */
	public static String getJarFiles(String jarPath) {
		List<String> fileList = getFileNamesFromHdfsDir(jarPath);
		String jarFiles = null;
		for (String file : fileList) {
			if (jarFiles == null)
				jarFiles = file;
			else
				jarFiles = jarFiles + "," + file;
		}
		return jarFiles;
	}
	
	

	/**
	 * Check if time already updated.
	 *
	 * @param lastModificationDate the last modification date
	 * @param properties the properties
	 * @return true, if successful
	 */
	public static boolean checkIfTimeAlreadyUpdated(String lastModificationDate, Map<String, String> properties) {
		String date = new SimpleDateFormat(LivyConstants.FORMAT_DDMMYY).format(new Date());
		logger.info("old date =={} new Date = {}", lastModificationDate, date);
		if (date.equalsIgnoreCase(lastModificationDate))
			return true;
		else {
			properties.put(LivyConstants.LASTUPDATIONDATE, date);
			return false;
		}
	}

	/**
	 * Gets the next server pos.
	 *
	 * @param activeServerList the server list
	 * @return the next server pos
	 */
	public static synchronized Integer getNextServerPos(List<String> activeServerList) {
		Integer length = activeServerList.size();
		int currPos = currServerCounter >= length ? 0 : currServerCounter;
		currServerCounter = currPos + 1;
		return currPos;
	}
	
	/**
	 * Checks if is pending requests.
	 *
	 * @param appName the app name
	 * @param url the url
	 * @return true, if is pending requests
	 */
	public static synchronized boolean isPendingRequests(String appName, String url) {
		List<String> serverList = SessionUtils.pendingServerSessionMap.get(appName);
		logger.info("pending requests ----------   {}  and current request {}",SessionUtils.pendingServerSessionMap,url);
		if (serverList == null) {
			serverList = new ArrayList<>();
			serverList.add(url);
			SessionUtils.pendingServerSessionMap.put(appName, serverList);
		} else {
			if (serverList.contains(url))
				return true;
			else
				SessionUtils.pendingServerSessionMap.get(appName).add(url);
		}
		logger.info("final pending requests ----------   ",SessionUtils.pendingServerSessionMap);
		return false;
	}
	
	/**
	 * Sets the time range.
	 *
	 * @param app the new time range
	 */
	public static void setTimeRange(LivyApplication app) {
		Map<String, String> properties = app.getPropertyMap();
		if (properties != null) {
			String lastModificationDate = properties.get(LivyConstants.LASTUPDATIONDATE);
			if (!SessionUtils.checkIfTimeAlreadyUpdated(lastModificationDate, properties)) {
				Long cellStartTime = Long.valueOf(properties.get(LivyConstants.CELLSTARTTIME));
				Long millisToAdd = Long.valueOf(properties.get(LivyConstants.MILLISTOADD));
				Long cellEndTime = cellStartTime + millisToAdd;
				properties.put(LivyConstants.CELLSTARTTIME, cellStartTime.toString());
				properties.put(LivyConstants.CELLENDTIME, cellEndTime.toString());
				app.setProperties(LivyApplication.objectMapper.toJson(properties));
			}
		}
	}
}
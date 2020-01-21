package com.inn.foresight.module.tribe.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;
import org.apache.tika.io.TikaInputStream;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.module.tribe.exceptions.ContentTypeNotMatchException;
import com.inn.product.um.user.model.User;

/**
 * The Class TRBUtils.
 */
public class TRBUtils {
	
	/** The logger. */
	private static Logger logger = LogManager.getLogger(TRBUtils.class);
	
	/** The Constant FILES_DIR. */
	public static final String FILES_DIR = System.getProperty("catalina.base") + "/webapps/foresight"
			+ ConfigUtils.getString(ConfigUtil.DOC_PATH_TRIBE);
	
	/** The Constant TRIBE_FILES. */
	public static final String TRIBE_FILES = "/TRIBE_FILES";
	
	/** The Constant SPACES. */
	public static final String SPACES = "/spaces";
	
	/** The Constant SPACE_IMAGE. */
	public static final String SPACE_IMAGE = "/space_image";
	
	/** The Constant TOPICS. */
	public static final String TOPICS = "/topics";
	
	/** The Constant TOPICS_ATTACHMENT. */
	public static final String TOPICS_ATTACHMENT = "/attachment";
	
	/** The Constant POSTS. */
	public static final String POSTS = "/posts";
	
	/** The Constant POST_ATTACHMENT. */
	public static final String POST_ATTACHMENT = "/attachment";
	
	/** The Constant POST_COMMENTS_ATTACHMENT. */
	public static final String POST_COMMENTS_ATTACHMENT = "/comment";
	
	/** The Constant BANNERS. */
	public static final String BANNERS = "/banners";
	
	/** The Constant BANNER_IMAGE. */
	public static final String BANNER_IMAGE = "/banner_image";
	
	/** The Constant BANNER_ATTACHMENT. */
	public static final String BANNER_ATTACHMENT = "/banner_attachment";
	
	/** The Constant PATH_FOR_TRIBE_SPACE_FILES. */
	public static final String PATH_FOR_TRIBE_SPACE_FILES = TRIBE_FILES + SPACES;
	
	/** The Constant PATH_FOR_TRIBE_TOPICS_FILES. */
	public static final String PATH_FOR_TRIBE_TOPICS_FILES = TRIBE_FILES + TOPICS;
	
	/** The Constant PATH_FOR_TRIBE_POST_FILES. */
	public static final String PATH_FOR_TRIBE_POST_FILES = TRIBE_FILES + POSTS;
	
	/** The Constant PATH_FOR_TRIBE_BANNER_FILES. */
	public static final String PATH_FOR_TRIBE_BANNER_FILES = TRIBE_FILES + BANNERS;
	
	/** The Constant TOPICS_ATTACHMENTS. */
	public static final String TOPICS_ATTACHMENTS = "/topics_attachment";
	
	/** The Constant REPLY_ATTACHMENTS. */
	public static final String REPLY_ATTACHMENTS = "/reply_attachment";

	public static final String TRIBE_BANNER_VM = "tribeBanner.vm";

	public static final String BANNER_DATA = "bannerdata";

	/**
	 * The Enum TRBShareTypes.
	 */
	public static enum TRBShareTypes {
		
		/** The my feed. */
		MY_FEED, 
 /** The my connection. */
 MY_CONNECTION, 
 /** The my spaces. */
 MY_SPACES
	}

	/**
	 * The Enum TRBFileTypes.
	 */
	// add other file types if required
	public enum TRBFileTypes {
		
		/** The image. */
		IMAGE, 
 /** The video. */
 VIDEO, 
 /** The audio. */
 AUDIO, 
 /** The doc. */
 DOC,
 HTML
	}

	/**
	 * Check content type from file types.
	 *
	 * @param inputStream the input stream
	 * @param fileType the file type
	 * @return true, if successful
	 * @throws RestException the rest exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	// add other cases for different file types if required
	public static boolean checkContentTypeFromFileTypes(InputStream inputStream, TRBFileTypes fileType)
			throws RestException, IOException {
		logger.info("inputStream in checkContentTypeFromFileTypes : " + inputStream.available());
		Tika tika = new Tika();
		TikaInputStream tikaInputStream = TikaInputStream.get(inputStream);
		String detectedType = tika.detect(tikaInputStream);
		logger.info("detectedType : " + detectedType);
		
		switch (fileType) {
		case IMAGE: {
			if (TRBContentType.IMAGE_JPEG.equalsIgnoreCase(detectedType)
					|| TRBContentType.IMAGE_PNG.equalsIgnoreCase(detectedType)) {
				return true;
			} 
		}
		case HTML: {
			if (TRBContentType.HTML.equalsIgnoreCase(detectedType) || TRBContentType.HTML_PLAIN.equalsIgnoreCase(detectedType) || TRBContentType.XHTML.equalsIgnoreCase(detectedType)|| TRBContentType.OCTET_STREAM.equalsIgnoreCase(detectedType)) {
				return true;
			} 
		}
		default:
			return false;
		}
	}
	
	
	
	/**
	 * Store files in folder.
	 *
	 * @param inputStream the input stream
	 * @param filePath the file path
	 * @param fileName the file name
	 * @return the string
	 * @throws RestException the rest exception
	 */
	public static String storeFilesInFolder(InputStream inputStream, String filePath, String fileName)
			throws RestException {
		String response = TRBConstants.FAILURE;
		try {
			String url =ConfigUtils.getString(ConfigUtil.TRIBE_BASE_URL)+ 
					ConfigUtils.getString(ConfigUtil.FILE_UPLOADS_TRIBE_URL) + "?filePath=" + filePath
					+ "&fileName=" + fileName;
			logger.info("url in storeFilesInFolder :  " + url);
			response = Utils.sendPostRequestWithInputStream(url, inputStream);
			logger.info("response arrived in storeFilesInFolder : " + response);
			return response;
		} catch (Exception e) {
			logger.info("some problem in uploading file : " + e.getMessage());
			throw new RestException("some problem in uploading file");
		}
	}

	/**
	 * Delete files from folder.
	 *
	 * @param filePath the file path
	 * @return the string
	 * @throws RestException the rest exception
	 */
	public static String deleteFilesFromFolder(String filePath) throws RestException {
		String response = TRBConstants.FAILURE;
		try {
			String url =ConfigUtils.getString(ConfigUtil.TRIBE_BASE_URL)+  
					ConfigUtils.getString(ConfigUtil.DELETE_FILE_UPLOADS_TRIBE_URL) + "?filePath=" + filePath;
			logger.info("url in deleteFilesFromFolder : " + url);
			response = Utils.sendPostRequest(url, null);
			logger.info("response arrived in deleteFilesFromFolder : " + response);
		} catch (Exception e) {
			logger.info("some problem in deleting file : " + e.getMessage());
		}
		return response;
	}

	/**
	 * Gets the user full name.
	 *
	 * @param user the user
	 * @return the user full name
	 */
	public static String getUserFullName(User user) {
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		String fullName = "";
		if (firstName != null && (!(firstName.isEmpty())))
			fullName = fullName.concat(firstName + " ");
		if (lastName != null && (!(lastName.isEmpty())))
			fullName = fullName.concat(lastName);
		return fullName;
	}

	/**
	 * Gets the file name.
	 *
	 * @param header the header
	 * @return the file name
	 */
	public static String getFileName(MultivaluedMap<String, String> header) {
		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {
				String[] name = filename.split("=");
				String finalFileName = name[1].trim().replaceAll("\"", "");
				return finalFileName;
			}
		}
		return "unknown";
	}

	/**
	 * Generate random string.
	 *
	 * @return the string
	 */
	public static String generateRandomString() {
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[20];
		random.nextBytes(bytes);
		Long randomNumber = random.nextLong();
		String newToken = randomNumber.toString();
		newToken = newToken.substring(2, newToken.length() - 1);
		return convertHexToBase36(newToken);
	}

	/**
	 * Convert hex to base 36.
	 *
	 * @param hex the hex
	 * @return the string
	 */
	public static String convertHexToBase36(String hex) {
		BigInteger big = new BigInteger(hex, 16);
		return big.toString(32);
	}

	/**
	 * Date format to string.
	 *
	 * @param date the date
	 * @return the string
	 */
	public static String dateFormatToString(Date date) {
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
		String formatedDate = DATE_FORMAT.format(date);
		logger.info("Today in dd-MM-yyyy format : " + formatedDate);
		return formatedDate;
	}

	/**
	 * Check input file size.
	 *
	 * @param inputStream the input stream
	 * @return true, if successful
	 * @throws RestException the rest exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean checkInputFileSize(InputStream inputStream) throws RestException, IOException {
		logger.info("inputStream in checkInputFileSize : " + inputStream.available());
		double fileSizeInBytes = inputStream.available();
		logger.info("file size in Bytes : " + fileSizeInBytes);
		double fileSizeInKB = fileSizeInBytes / 1024;
		logger.info("file size in KB : " + fileSizeInKB);
		double fileSizeInMB = fileSizeInKB / 1024;
		logger.info("file size in MB : " + fileSizeInMB);
		if (fileSizeInMB <= Double.parseDouble(ConfigUtils.getString(ConfigUtil.FILE_SIZE_TRIBE_IN_MB))) {
			return true;
		} else {
			throw new RestException("file size exceeds the defined limit");
		}
	}
}

/*
 * 
 */
package com.inn.foresight.core.generic.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.ConnectException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.aerogear.security.otp.Totp;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.security.authentication.AuthenticationServiceException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.encoder.AESUtils;
import com.inn.commons.http.HttpException;
import com.inn.commons.http.HttpGetRequest;
import com.inn.commons.http.HttpPostRequest;
import com.inn.commons.http.HttpRequest;
import com.inn.commons.maps.tiles.Tile;
import com.inn.commons.unit.Duration;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.utils.MySSLSocketFactory;
import com.inn.foresight.core.fileupload.utils.FileUploadConstant;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.utils.enums.Technology;
import com.inn.foresight.core.infra.utils.enums.Vendor;
import com.inn.foresight.core.report.wrapper.ReportInfoWrapper;
import com.inn.foresight.core.workorder.wrapper.Email;
import com.inn.foresight.core.workorder.wrapper.EmailAttachment;
import com.inn.product.security.authentication.GenerateReportURL;
import com.inn.product.security.utils.enums.AuthenticationFailureAction.PostAuthenticationAction;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * The Class Utils.
 */
public class Utils {

	/** The logger. */
	private static Logger logger = LogManager.getLogger(Utils.class);

	/** The space. */
	public static String SPACE = " ";

	/** The space encoded. */
	public static String SPACE_ENCODED = "%20";

	/**
	 * Gets the stack trace.
	 *
	 * @param e the e
	 * @return the stack trace
	 */
	public static final String getStackTrace(Throwable e) {
		return ExceptionUtils.getStackTrace(e);
	}

	/**
	 * Make http get call.
	 * 
	 * <pre>
	 * HttpRequest http = new HttpGetRequest(URI);
	 * http.addHeader("Content-Type", "application/json");
	 * return http.getString();
	 * </pre>
	 *
	 * @param uri the uri
	 * @return the string
	 * @deprecated replaced by {@link HttpRequest#getString()}
	 */
	@Deprecated
	public static String makeHttpGetCall(String uri) {
		logger.info("going to make httpGetCall: " + uri);
		logger.debug("toInteger(ConfigUtil.getConfigProp(ConfigUtil.TIMEOUT_VALUE)) = "
				+ toInteger(ConfigUtils.getString(ConfigUtil.TIMEOUT_VALUE)));
		String msg = ForesightConstants.EXCEPTION_NO_RECORD_FOUND;
		try {
			RequestConfig defaultRequestConfig = RequestConfig.custom()
					.setCookieSpec(org.apache.http.client.config.CookieSpecs.DEFAULT).setExpectContinueEnabled(true)
					.build();
			CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
			RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
					.setSocketTimeout(toInteger(ConfigUtils.getString(ConfigUtil.TIMEOUT_VALUE)))
					.setConnectionRequestTimeout(toInteger(ConfigUtils.getString(ConfigUtil.TIMEOUT_VALUE))).build();
			HttpGet httpGet = new HttpGet(uri);
			httpGet.setConfig(requestConfig);
			httpGet.addHeader("Content-Type", "application/json");
			CloseableHttpResponse response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			msg = readData(entity.getContent());
			EntityUtils.consume(entity);
			logger.info("makeHttpGetCall ENDED [URI={} | RES={}]", uri, response.getStatusLine().getStatusCode());
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException  : {}", getStackTrace(e));
			return ForesightConstants.EXCEPTION_NO_RECORD_FOUND;
		} catch (ConnectException e) {
			logger.error("ConnectException in: {}", getStackTrace(e));
			return ForesightConstants.EXCEPTION_ON_CONNECTION;
		} catch (Exception e) {
			logger.error("Exception :   {}", getStackTrace(e));
			return ForesightConstants.EXCEPTION_NO_RECORD_FOUND;
		}

		return msg;
	}

	/**
	 * Checks for valid value.
	 *
	 * @param value the value
	 * @return true, if successful
	 */

	public static String getDivisionDuplexByBand(String band) {
		if (ForesightConstants.BAND850.equalsIgnoreCase(band)) {
			return ForesightConstants.DIVISION_DUPLEX_FDD;
		} else if (ForesightConstants.BAND2300.equalsIgnoreCase(band)) {
			return ForesightConstants.DIVISION_DUPLEX_TDD;
		} else if ("1800".equalsIgnoreCase(band)) {
			return ForesightConstants.DIVISION_DUPLEX_FDD;
		}
		return null;
	}

	/**
	 * Gets the string date by format.
	 *
	 * @param date   the date
	 * @param format the format
	 * @return the string date by format
	 */
	public static String getStringDateByFormat(Date date, String format) {
		try {
			if (date != null) {
				SimpleDateFormat s = new SimpleDateFormat(format);
				return s.format(date);
			}
		} catch (Exception e) {
			logger.error("error wihile formatting date " + Utils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * Parses the date to string format.
	 *
	 * @param date   the date
	 * @param format the format
	 * @return the string
	 */
	public static String parseDateToStringFormat(Date date, String format) {
		try {
			DateFormat df = new SimpleDateFormat(format);
			return df.format(date);
		} catch (Exception ex) {
			logger.error("Unable to parse Date : " + date + " in format " + format);
		}
		return null;
	}

	/**
	 * Parses the string to date.
	 *
	 * @param dateString the date string
	 * @param format     the format
	 * @return the date
	 */
	public static Date parseStringToDate(String dateString, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date date = sdf.parse(dateString);
			logger.info("date {}", date);
			return date;
		} catch (ParseException e) {
			logger.error("Exception while parseStringToDate:{}", e.getMessage());
			return null;
		}
	}

	/**
	 * Checks if is valid parameter.
	 *
	 * @param parameters the parameters
	 * @return true, if is valid parameter
	 */
	public static boolean isValidParameter(Object... parameters) {
		for (Object parameter : parameters) {
			if (parameter == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if is valid string.
	 *
	 * @param string the string
	 * @return true, if is valid string
	 */
	public static boolean isValidString(String string) {
		final boolean VALID = true;
		if (string == null || ForesightConstants.BLANK_STRING.equals(string) || "null".equalsIgnoreCase(string))
			return !VALID;
		return VALID;
	}

	/**
	 * To integer.
	 *
	 * @param intStr the int str
	 * @return the integer
	 */
	public static Integer toInteger(String intStr) {
		try {
			if (intStr != null && !"null".equalsIgnoreCase(intStr) && intStr.contains("."))
				return toDouble(intStr).intValue();

			return Integer.parseInt(intStr);
		} catch (Exception e) {
			logger.error("Exception while toInteger :intStr = {}", intStr);
		}
		return null;
	}

	/**
	 * To double.
	 *
	 * @param str the str
	 * @return the double
	 */
	public static Double toDouble(String str) {
		try {
			if (isValidString(str)) {

				return Double.parseDouble(str);
			}
		} catch (Exception e) {
			logger.error("exception on converting to double " + str);
		}
		return Double.parseDouble(str);
	}

	/**
	 * Gets the size wize lat long.
	 *
	 * @param southWestLong the south west long
	 * @param southWestLat  the south west lat
	 * @param northEastLong the north east long
	 * @param northEastLat  the north east lat
	 * @return the size wize lat long
	 */
	public static HashMap<String, Double> getSizeWizeLatLong(Double southWestLong, Double southWestLat,
			Double northEastLong, Double northEastLat) {
		Double largeLat = null;
		Double largeLong = null;
		Double smallLat = null;
		Double smallLong = null;

		if (southWestLong > northEastLong) {
			largeLong = southWestLong;
			smallLong = northEastLong;
		} else {
			largeLong = northEastLong;
			smallLong = southWestLong;
		}

		if (southWestLat > northEastLat) {
			largeLat = southWestLat;
			smallLat = northEastLat;
		} else {
			largeLat = northEastLat;
			smallLat = southWestLat;
		}

		HashMap<String, Double> sizeWiseLatLongMap = new HashMap<>();
		sizeWiseLatLongMap.put("largeLat", largeLat);
		sizeWiseLatLongMap.put("largeLong", largeLong);
		sizeWiseLatLongMap.put("smallLat", smallLat);
		sizeWiseLatLongMap.put("smallLong", smallLong);
		return sizeWiseLatLongMap;
	}

	/**
	 * Checks for value.
	 *
	 * @param value the value
	 * @return the boolean
	 */
	public static Boolean hasValue(Object value) {
		if (value instanceof String) {
			String val = String.valueOf(value);
			if (val != null && !val.isEmpty()) {
				return true;
			} else {
				return false;
			}
		} else {
			if (value != null) {
				return true;
			} else {
				return false;
			}
		}

	}

	/**
	 * Parses the time stamp to date.
	 *
	 * @param timeStamp the time stamp
	 * @return the date
	 */
	public static Date parseTimeStampToDate(Long timeStamp) {
		try {
			if (timeStamp != null) {
				return new Date(timeStamp);
			}
		} catch (Exception e) {
			// empty
		}
		return null;
	}

	/**
	 * Gets the valid path.
	 *
	 * @param path the path
	 * @return the valid path
	 */
	public static String getValidPath(String path) {
		return (!path.endsWith("/")) ? path + "/" : path;
	}

	/**
	 * Checks if is valid double.
	 *
	 * @param value the value
	 * @return true, if is valid double
	 */
	public static boolean isValidDouble(Double value) {
		try {
			return (value != null && !Double.isNaN(value));
		} catch (Exception e) {
			logger.error("getting exception on validate double = {}", value);
			return false;
		}
	}

	/**
	 * Generate MD5 Hash String
	 * 
	 * @param source
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String toMD5(String source) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			logger.error("NoSuchAlgorithmException : {}", e.getStackTrace());
		}
		if (digest != null)
			digest.update(source.getBytes());
		if (digest != null) {
			byte[] hash = digest.digest();
			return Utils.byteArrayToHexString(hash);
		}
		return null;
	}

	/**
	 * Convert byte array to String
	 * 
	 * @param hash
	 * @return
	 */
	public static String byteArrayToHexString(byte[] hash) {
		StringBuilder hexString = new StringBuilder();
		for (int i = 0; i < hash.length; i++) {
			hexString.append(byteToHexString(hash[i]));
		}
		return hexString.toString();
	}

	/**
	 * Convert byte to String
	 * 
	 * @param aByte
	 * @return
	 */
	public static String byteToHexString(byte aByte) {
		String hex = Integer.toHexString(0xFF & aByte);
		return (hex.length() == 1 ? "0" : "") + hex;
	}

	public static String makeHttpPostWithSSLByPassing(String url, String json, Map<String, String> headers) {

		String msg = ForesightConstants.EXCEPTION_NO_RECORD_FOUND;
		try {

			CloseableHttpClient httpclient = getNewHttpClient();

			logger.info("going to make call  to Edge server with URL: " + url);
			HttpPost httpPost = new HttpPost(url);
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpPost.addHeader(entry.getKey(), entry.getValue());
			}
			logger.info("requesting jsonString : " + json);
			httpPost.setEntity(new StringEntity(json));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			msg = readData(entity.getContent());
			EntityUtils.consume(entity);

		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException : {}", getStackTrace(e));
			return ForesightConstants.EXCEPTION_NO_RECORD_FOUND;
		} catch (ConnectException e) {
			logger.error("ConnectException : {}", getStackTrace(e));
			return ForesightConstants.EXCEPTION_ON_CONNECTION;
		} catch (Exception e) {
			logger.error("Exception : {}", getStackTrace(e));
			return ForesightConstants.EXCEPTION_NO_RECORD_FOUND;
		}
		return msg;

	}

	/**
	 * Gets the new http client.
	 *
	 * @return the new http client
	 */
	@SuppressWarnings("deprecation")
	public static DefaultHttpClient getNewHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);
			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("https", sf, 443));
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	/**
	 * Read data.
	 *
	 * @param rd the rd
	 * @return the string
	 */
	public static String readData(InputStream rd) {
		try {
			return IOUtils.toString(rd);
		} catch (IOException e) {
			logger.error("error in reading data: " + Utils.getStackTrace(e));
		} finally {
			IOUtils.closeQuietly(rd);
		}
		return "";
	}

	public static String uploadBulkUploadFile(String attachmentUrI, String filePath) {
		logger.info("Inside uploadFile");
		logger.info("filePath {} ", filePath);
		String message = "";
		try {
			try {
				InputStream inputStream = new FileInputStream(new File(filePath));
				ContentBody content = new InputStreamBody(inputStream, "file");
				MultipartEntity reqEntity = new MultipartEntity();
				reqEntity.addPart("file", content);
				logger.info("attachmentUrI : {} ", attachmentUrI);
				HttpRequest http = new HttpPostRequest(attachmentUrI, reqEntity);
				http.setConnectionTimeout(Duration.seconds(120));
				String jsonResponse = http.getString();
				logger.info("Response while Uploading attachment:{}", jsonResponse);
				if (jsonResponse.toLowerCase().indexOf("success") >= 0) {
					message = FileUploadConstant.UPLOADED_SUCCESSFULLY;
				} else {
					message = FileUploadConstant.FAIL_TO_UPLOAD;
				}
			} catch (Exception e) {
				logger.error("File upload fail for  Exception :  {}  ", Utils.getStackTrace(e));
				message = FileUploadConstant.FAIL_TO_UPLOAD;
			}
		} catch (Exception e) {
			logger.error("Error while uploadFile {} ", Utils.getStackTrace(e));
		}
		return message;

	}

	/**
	 * <pre>
	 * HttpRequest http = new HttpPostRequest(URI, new StringEntity(json));
	 * http.addHeader("Content-Type", "application/json");
	 * return http.getString();
	 * </pre>
	 *
	 * @param URI  the uri
	 * @param json the json
	 * @return the string
	 * @deprecated replaced by {@link HttpPostRequest#getString()}
	 */
	@Deprecated
	public static String makeHttpPostCall(String URI, String json) {

		String msg = ForesightConstants.EXCEPTION_NO_RECORD_FOUND;
		try {
			RequestConfig defaultRequestConfig = RequestConfig.custom()
					.setCookieSpec(org.apache.http.client.config.CookieSpecs.DEFAULT).setExpectContinueEnabled(true)
					.build();

			CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
			RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
					.setSocketTimeout(toInteger(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())))
					.setConnectTimeout(toInteger(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())))
					.setConnectionRequestTimeout(toInteger(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())))
					.build();

			logger.info("going to make call to Edge server with URL: " + URI);
			HttpPost httpPost = new HttpPost(URI);
			httpPost.setConfig(requestConfig);
			httpPost.addHeader("Content-Type", "application/json");
			logger.info("requesting jsonString: " + json);
			httpPost.setEntity(new StringEntity(json));
			CloseableHttpResponse response = client.execute(httpPost);
			HttpEntity entity = response.getEntity();
			msg = readData(entity.getContent());
			EntityUtils.consume(entity);
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException : {}", getStackTrace(e));
			return ForesightConstants.EXCEPTION_NO_RECORD_FOUND;
		} catch (ConnectException e) {
			logger.error("ConnectException : {}", getStackTrace(e));
			return ForesightConstants.EXCEPTION_ON_CONNECTION;
		} catch (Exception e) {
			logger.error("Exception : {}", getStackTrace(e));
			return ForesightConstants.EXCEPTION_NO_RECORD_FOUND;
		}
		return msg;
	}

	public static void createEmailNotification(String recieverEmailIds, String ccMailIds, String bccMailIds,
			String mailSubject, String message, List<EmailAttachment> attachmentList, String remark) {
		Email email = new Email();
		email.setBcc(bccMailIds);
		email.setCc(ccMailIds);
		email.setEmailAttachments(attachmentList);
		email.setMessage(message);
		email.setRemark(remark);
		email.setSubject(mailSubject);
		email.setTo(recieverEmailIds);
		logger.info("createEmailNotification mail:{}", email.toString());
		try {
			StringEntity entity = new StringEntity(new Gson().toJson(email), ContentType.APPLICATION_JSON);
			HttpRequest http = new HttpPostRequest(ConfigUtils.getString(ConfigEnum.KYLO_BASE_URL)
					+ ConfigUtils.getString(ConfigEnum.CREATE_EMAIL_NOTIFICATION_URL), entity);
			logger.info("in createEmailNotification CREATE_EMAIL_NOTIFICATION_URL:{}",
					ConfigUtils.getString(ConfigEnum.CREATE_EMAIL_NOTIFICATION_URL));
			http.setConnectionTimeout(Duration.seconds(120));
			http.getString();
		} catch (Exception ex) {
			logger.error("Error while createEmailNotification, StackTrace:{}", getStackTrace(ex));
		}

	}

	/**
	 * Gets the dropwizard url.
	 *
	 * @param request the request
	 * @return the dropwizard url
	 */
	public static String getMicroServiceBaseUrl(HttpServletRequest request) {
		String url = ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue()) + StringUtils.replace(
				request.getRequestURI(), ConfigUtils.getString(ConfigEnum.APPSERVER_PROJECTNAME.getValue()),
				ConfigUtils.getString(ConfigEnum.MICROSERVICE_PROJECTNAME_REPLACE_BY.getValue()));
		String queryString = request.getQueryString();
		if (queryString != null && queryString.length() != 0) {
			url += "?" + queryString;
		}
		return url;
	}

	/**
	 * Gets the micro service url.
	 *
	 * @param request the request
	 * @return the micro service url
	 */

	public static String getDropwizardUrlWithPrefix(HttpServletRequest request) {
		String url = ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue()) + request.getRequestURI();
		url = StringUtils.replace(url, "rest", "rest/ms");
		String queryString = request.getQueryString();
		if (queryString != null && queryString.length() != 0) {
			url += "?" + queryString;
		}
		logger.info("Micro service Url {}", url);
		return url;
	}

	/**
	 * Parses the date to string.
	 *
	 * @param date   the date
	 * @param format the format
	 * @return the string
	 * @throws RestException the rest exception
	 */
	public static String parseDateToString(Date date, String format) {
		try {
			DateFormat df = new SimpleDateFormat(format);
			return df.format(date);
		} catch (Exception ex) {
			throw new RestException("Unable to parse Date : " + date + " in format " + format);
		}
	}

	public static Date[] getStartAndEndDay(Date date) {
		Date[] dateArray = new Date[2];
		Calendar currentDay = Calendar.getInstance();
		currentDay.setTime(date);

		currentDay.add(Calendar.DAY_OF_WEEK, 7 - currentDay.get(Calendar.DAY_OF_WEEK));

		dateArray[1] = currentDay.getTime();
		currentDay.add(Calendar.WEEK_OF_YEAR, -4);
		currentDay.add(Calendar.DAY_OF_WEEK, -6);

		dateArray[0] = currentDay.getTime();

		return dateArray;
	}

	public static String makeHttpGetWithSSLByPassing(String url, Map<String, String> headers) {

		logger.info("Inside makeHttpGetWithSSLByPassing  ");
		logger.info("url: " + url);

		String msg = ForesightConstants.EXCEPTION_NO_RECORD_FOUND;
		try {

			CloseableHttpClient httpclient = getNewHttpClient();

			HttpGet httpget = new HttpGet(url);
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpget.addHeader(entry.getKey(), entry.getValue());
			}

			CloseableHttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			msg = readData(entity.getContent());
			EntityUtils.consume(entity);

		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException: {}", getStackTrace(e));
			return ForesightConstants.EXCEPTION_NO_RECORD_FOUND;
		} catch (ConnectException e) {
			logger.error("ConnectException: {}", getStackTrace(e));
			return ForesightConstants.EXCEPTION_ON_CONNECTION;
		} catch (Exception e) {
			logger.error("Exception: {}", getStackTrace(e));
			return ForesightConstants.EXCEPTION_NO_RECORD_FOUND;
		}
		return msg;

	}

	/**
	 * Enable limits.
	 *
	 * @param query  the query
	 * @param llimit the llimit
	 * @param ulimit the ulimit
	 */
	public static void enableLimits(Query query, Integer llimit, Integer ulimit) {
		if (ulimit != null) {
			if (ulimit >= 0 && query != null) {
				if (llimit == null) {
					llimit = 0;
				}
				query.setMaxResults((ulimit - llimit + 1));
			}
		}
		if (llimit != null) {
			if (llimit >= 0 && query != null) {
				query.setFirstResult(llimit);
			}
		}
	}

	/**
	 * Gets the previous day.
	 *
	 * @param goback the goback
	 * @return the previous day
	 */
	public static String getPreviousDay(Integer goback) {
		String day = "";
		try {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, goback);
			SimpleDateFormat dayformat = new SimpleDateFormat("dd");
			day = dayformat.format(cal.getTime());

		} catch (Exception e) {
			logger.error("Exception while processing date inside getPreviousDayOfMonth:  " + e);
		}
		return day;
	}

	/**
	 * Checks for value.
	 *
	 * @param value the value
	 * @return true, if successful
	 */
	public static boolean hasValue(String value) {
		if (value != null && !value.trim().equalsIgnoreCase("nan") && !value.trim().equalsIgnoreCase("null")
				&& !value.trim().equalsIgnoreCase("") && !value.trim().equals("-") && !value.trim().equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * Checks for value.
	 *
	 * @param value the value
	 * @return true, if successful
	 */
	public static boolean hasValue(Double value) {
		return value != null ? Boolean.TRUE : Boolean.FALSE;
	}

	/**
	 * Reduce key.
	 *
	 * @param finalKey the final key
	 * @return the string
	 */
	public static String reduceKey(String finalKey) {
		finalKey = finalKey.replaceAll(Constants.UNDERSCORE, Constants.DASH);
		if (finalKey.contains(Constants.ENB_DASH)) {
			finalKey = finalKey.replaceFirst(Constants.I_DASH, Constants.BLANK_STRING_FOR_CONF_AUDIT)
					.replace(Constants.ENB_DASH, Constants.E_DASH);
		} else if (finalKey.contains(Constants.OSC_DASH)) {
			finalKey = finalKey.replaceFirst(Constants.I_DASH, Constants.BLANK_STRING_FOR_CONF_AUDIT)
					.replace(Constants.OSC_DASH, Constants.O_DASH);
		}
		return finalKey.replaceAll(Constants.DASH, Constants.BLANK_STRING_FOR_CONF_AUDIT);
	}

	/**
	 * Gets the percentage.
	 *
	 * @param result the result
	 * @param total  the total
	 * @return the percentage
	 */
	public static Double getPercentage(Integer result, Integer total) {
		Double proportion = 0.0;
		try {
			if (total != 0) {
				proportion = (result * 1.0) / (total);
			}
			return proportion * 100;
		} catch (Exception e) {
			logger.info("Exception in  getPercentage result: " + result + " and  total: " + total + ",  EXCEPTION: "
					+ getStackTrace(e));
			return proportion;
		}
	}

	/**
	 * Calculate integer list average.
	 *
	 * @param list the list
	 * @return the int
	 */
	public static int calculateIntegerListAverage(List<Integer> list) {
		if (list == null || list.isEmpty()) {
			return 0;
		}

		int sum = 0;
		for (int value : list) {
			sum += value;
		}

		return sum / list.size();
	}

	/**
	 * Gets the max value from double list.
	 *
	 * @param list the list
	 * @return the max value from double list
	 */
	public static Double getMaxValueFromDoubleList(List<Double> list) {
		return list.stream().mapToDouble(Double::doubleValue).max().orElse(0);
	}

	/**
	 * Gets the average from double list.
	 *
	 * @param list the list
	 * @return the average from double list
	 */
	public static Double getAverageFromDoubleList(List<Double> list) {
		return list.stream().mapToDouble(Double::doubleValue).average().orElse(0);
	}

	/**
	 * Gets the average from integer list.
	 *
	 * @param list the list
	 * @return the average from integer list
	 */
	public static Double getAverageFromIntegerList(List<Integer> list) {
		return list.stream().mapToInt(Integer::intValue).average().orElse(0);
	}

	/**
	 * Removes the brackets.
	 *
	 * @param value the value
	 * @return the string
	 */
	public static String removeBrackets(String value) {

		if (hasValue(value)) {
			value = value.replaceAll("[\\[\\]]", "");
		}
		return value;
	}

	/**
	 * Gets the masked imsi.
	 *
	 * @param imsi the imsi
	 * @return masked imsi (last 4 digit will be visible) e.g. XXXXXXXXXXX6797
	 */
	public static String getMaskedImsi(String imsi) {
		int length = imsi.length();
		if (length >= 4) {
			return ForesightConstants.MASKED_IMSI_PREFIX + StringUtils.substring(imsi, length - 4);
		}
		return imsi;
	}

	/**
	 * Encode URL.
	 *
	 * @param url the url
	 * @return the string
	 */
	public static String encodeURL(String url) {
		if (url != null) {
			url = url.trim().replaceAll(SPACE, SPACE_ENCODED);
		}
		return url;
	}

	/**
	 * Gets the date before seven days.
	 *
	 * @param date the date
	 * @return DateBeforeSevenDays
	 */
	public static Date getDateBeforeSevenDays(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -7);
		return calendar.getTime();
	}

	/**
	 * Gets the date from string format.
	 *
	 * @param date   the date
	 * @param format the format
	 * @return the date from string format
	 */
	public static Date getDateFromStringFormat(String date, String format) {
		try {
			if (date != null) {
				SimpleDateFormat s = new SimpleDateFormat(format);
				return s.parse(date);
			}
		} catch (Exception e) {
			logger.error("error wihile parsing date " + Utils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * Gets the percentage.
	 *
	 * @param result the result
	 * @param total  the total
	 * @return the percentage
	 */
	public static Double getPercentage(Double result, Double total) {
		Double proportion = 0.0;
		try {
			if (total != 0) {
				proportion = (result) / (total);
			}
			return roundOff(proportion * 100, 2);
		} catch (Exception e) {
			logger.info("Exception in getPercentage result: " + result + " and total: " + total + ", EXCEPTION: "
					+ getStackTrace(e));
			return proportion;
		}
	}

	/**
	 * Gets the percentage.
	 *
	 * @param result the result
	 * @param total  the total
	 * @return the percentage
	 */
	public static Double getPercentage(Long result, Long total) {
		Double proportion = 0.0;
		try {
			if (result != null && total != 0) {
				proportion = (result * 1.0) / (total);
			}
			return proportion * 100;
		} catch (Exception e) {
			logger.debug("Exception in getPercentage result: " + result + " and total: " + total + ", EXCEPTION: "
					+ getStackTrace(e));
			return proportion;
		}
	}

	/**
	 * Gets the period difference between dates.
	 *
	 * @param fromDate the from date
	 * @param toDate   the to date
	 * @return the period difference between dates
	 * @throws ParseException the parse exception
	 */
	public static String getPeriodDifferenceBetweenDates(Date fromDate, Date toDate) {
		DateTime fromDateTime = new DateTime(fromDate.getTime());
		DateTime toDateTime = new DateTime(toDate.getTime());
		Period period = new Period(fromDateTime, toDateTime);
		String timePeriod = ForesightConstants.BLANK_STRING;
		if (period.getYears() != 0) {
			timePeriod += period.getYears() + " Years ";
		}
		if (period.getMonths() != 0) {
			timePeriod += period.getMonths() + " Months ";
		}
		if (period.getWeeks() != 0) {
			timePeriod += period.getWeeks() + " Weeks ";
		}
		if (period.getDays() != 0) {
			timePeriod += period.getDays() + " Days ";
		}
		if (period.getHours() != 0) {
			timePeriod += period.getHours() + " Hours ";
		}
		if (period.getMinutes() != 0) {
			timePeriod += period.getMinutes() + " Minutes ";
		}
		if (period.getSeconds() != 0) {
			timePeriod += period.getSeconds() + " Seconds";
		}
		return timePeriod;
	}

	/**
	 * Round off.
	 *
	 * @param value                           the value
	 * @param numberOfDigitsAfterDecimalPoint the number of digits after decimal
	 *                                        point
	 * @return the double
	 */
	public static Double roundOff(Double value, int numberOfDigitsAfterDecimalPoint) {
		try {
			BigDecimal bigDecimal = BigDecimal.valueOf(value);
			bigDecimal = bigDecimal.setScale(numberOfDigitsAfterDecimalPoint, BigDecimal.ROUND_HALF_UP);
			return bigDecimal.doubleValue();
		} catch (NumberFormatException ne) {
			logger.info("Error in parsing value to double");
			return null;
		}
	}

	/**
	 * Convert K mfrom meter.
	 *
	 * @param meter the meter
	 * @return the double
	 */
	public static Double ConvertKMfromMeter(Double meter) {
		logger.debug("ConvertKMfromMeter value in meter " + meter);
		try {
			if (meter != null && !Double.isNaN(meter))
				return roundOff(meter / 1000.0, 3);
		} catch (Exception e) {
			logger.error("ConvertKMfromMeter Exception: " + e.getMessage());
		}
		return null;
	}

	/**
	 * Gets the rounded double value.
	 *
	 * @param value the value
	 * @return the rounded double value
	 */
	public static Double getRoundedDoubleValue(Double value) {
		DecimalFormat df = new DecimalFormat(ForesightConstants.DECIMAL_FORMAT);
		return Double.valueOf(df.format(value));
	}

	public static String saveFile(InputStream fileInputStream, String directoryPath, String fileName)
			throws IOException {
		File destinationFile = new File(directoryPath + fileName);
		logger.info("directoryPath :{}, fileName :{}", directoryPath, fileName);
		if (!destinationFile.exists()) {
			destinationFile.getParentFile().mkdirs();
			destinationFile.createNewFile();
		}
		FileOutputStream outStream = new FileOutputStream(destinationFile);

		try {
			byte[] buffer = new byte[ForesightConstants.FILE_READ_BUFFER_SIZE];
			int read = 0;
			while ((read = fileInputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, read);
			}
		} catch (IOException exception) {
			exception.getMessage();
		} finally {
			outStream.close();

		}
		return destinationFile.getAbsolutePath();
	}

	@Deprecated
	public static String sendGetRequestWithoutTimeOut(String URI) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		logger.info("going to request {}", URI);
		HttpGet httpGet = new HttpGet(URI);
		try {
			CloseableHttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("error in http get request " + URI);
				throw new RestException("Failed with  HTTP error code : " + statusCode);
			}

			try {
				HttpEntity entity = response.getEntity();
				String responseString = EntityUtils.toString(entity);
				EntityUtils.consume(entity);
				return responseString;
			} finally {
				response.close();

			}
		} catch (Exception e) {
			logger.error("error in http get request {} due to {}", URI, getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_ON_CONNECTION);
		} finally {

			httpGet.releaseConnection();

			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error("error in  closing client for uri {}", URI);
			}
		}
	}

	/**
	 * Make a POST Request and return response string
	 * 
	 * <pre>
	 * HttpRequest http = new HttpPostRequest(URI, httpEntity);
	 * return http.getString();
	 * </pre>
	 *
	 * @param URI        the uri
	 * @param httpEntity the http entity
	 * @return the string
	 * @throws RestException the rest exception
	 * @deprecated replaced by {@link HttpRequest#getString()}
	 */
	@Deprecated
	public static String sendPostRequest(String URI, HttpEntity httpEntity) {
		RequestConfig defaultRequestConfig = RequestConfig.custom()
				.setCookieSpec(org.apache.http.client.config.CookieSpecs.DEFAULT).setExpectContinueEnabled(true)
				.build();
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
		RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
				.setSocketTimeout(toInteger(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())))
				.setConnectTimeout(toInteger(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())))
				.setConnectionRequestTimeout(toInteger(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())))
				.build();
		logger.info("going to send post request {}", URI);
		HttpPost httpPost = new HttpPost(URI);
		httpPost.setConfig(requestConfig);
		httpPost.setEntity(httpEntity);
		try {
			CloseableHttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("error in http post request " + URI);
				throw new RestException("Failed with HTTP error code : " + statusCode);
			}

			try {
				HttpEntity entity = response.getEntity();
				if (entity == null) {
					logger.error("response contains no content " + URI);
					throw new ClientProtocolException("Response contains no content");
				}

				String responseString = readData(entity.getContent());

				EntityUtils.consume(entity);
				return responseString;
			} finally {
				if (response != null) {
					response.close();
				}
			}
		} catch (Exception e) {
			logger.error("error in http post request {} due to {}", URI, getStackTrace(e));
			throw new RestException(e.getMessage());
		} finally {
			if (httpPost != null) {
				httpPost.releaseConnection();
			}
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					logger.error("error in closing client for uri {}", URI);
				}
			}
		}
	}

	/**
	 * Unescape string.
	 *
	 * @param string the string
	 * @return the string
	 */
	public static String unescapeString(String string) {
		if (!Strings.isNullOrEmpty(string)) {
			string = StringEscapeUtils.unescapeHtml(string);
		}
		return string;
	}

	/**
	 * Validate vendor enum value.
	 *
	 * @param vendor the vendor
	 * @return true, if successful
	 */
	public static boolean validateVendorEnumValue(String vendor) {
		try {
			if (vendor != null) {
				Vendor.valueOf(vendor.trim().toUpperCase());
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	/**
	 * Validate domain enum value.
	 *
	 * @param domain the domain
	 * @return true, if successful
	 */
	public static boolean validateDomainEnumValue(String domain) {
		try {
			if (domain != null) {
				Domain.valueOf(domain.trim().toUpperCase());
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	/**
	 * Validate technology enum value.
	 *
	 * @param technology the technology
	 * @return true, if successful
	 */
	public static boolean validateTechnologyEnumValue(String technology) {
		try {
			if (technology != null) {
				Technology.valueOf(technology.trim().toUpperCase());
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	/**
	 * Validate NE type enum value.
	 *
	 * @param neType the ne type
	 * @return true, if successful
	 */
	public static boolean validateNETypeEnumValue(String neType) {
		try {
			if (neType != null) {
				NEType.valueOf(neType.trim().toUpperCase());
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public static String saveAndGetFilePath(HttpResponse response, String basePath) throws IOException {
		String authpath = ConfigUtils.getString(ConfigEnum.DOWNLOAD_AUTH_PATH.getValue());
		String fileName = filterFileNamefromContentDisposition(
				response.getFirstHeader(ForesightConstants.CONTENT_DISPOSITION).getValue());
		File file = new File(Symbol.SLASH_FORWARD + authpath + Symbol.SLASH_FORWARD + basePath + fileName);
		logger.info("Getting File Path {}", file.getAbsolutePath());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		response.getEntity().writeTo(baos);
		byte[] bytes = baos.toByteArray();
		FileUtils.writeByteArrayToFile(file, bytes);
		GenerateReportURL obj = new GenerateReportURL(
				file.getAbsolutePath().replace(Symbol.SLASH_FORWARD + authpath, ""));
		return obj.toUriString();
	}

	private static String filterFileNamefromContentDisposition(String value) {
		if (Utils.hasValidValue(value)) {
			if (value.contains("attachment") || value.contains(";") || value.contains("filename")
					|| value.contains("=")) {
				return value.replace("attachment", "").replace(";", "").replace("filename", "").replace("=", "")
						.replace("\"", "").replace(" ", "").trim();
			}
		}
		return value;
	}

	/**
	 * Checks for valid value.
	 *
	 * @param value the value
	 * @return true, if successful
	 */
	public static boolean hasValidValue(String value) {
		return (value != null && !value.trim().equalsIgnoreCase("null") && !value.trim().equalsIgnoreCase("nan")
				&& !value.trim().equalsIgnoreCase("-") && !value.trim().equalsIgnoreCase(""));
	}

	public static String getWeekNumberUsingYear(String dateString) {
		Date date;
		try {
			date = new Date(Long.parseLong(dateString));
			Integer weekNum = Utils.getWeekNumber(date);
			return (new SimpleDateFormat("yyyy").format(date) + "" + weekNum);
		} catch (Exception e) {
			logger.error("error in getWeekNumberUsingYear {}", dateString);
		}
		return dateString;
	}

	/**
	 * Gets the week number.
	 *
	 * @param date the date
	 * @return the week number
	 */
	public static Integer getWeekNumber(Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}

	public static String getWeekRange(Date date) {
		String stringDate = null;
		try {
			stringDate = null;
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek() - cal.get(Calendar.DAY_OF_WEEK));
			stringDate = parseDateToStringFormat(cal.getTime(), "ddMMMyy") + ForesightConstants.UNDERSCORE;
			cal.add(Calendar.DAY_OF_MONTH, 6);
			stringDate += parseDateToStringFormat(cal.getTime(), "ddMMMyy");
		} catch (Exception e) {
			logger.error("error in getWeekRange {}", date);
		}
		return stringDate;

	}

	public static String getWeekNumberWithYear(Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String week = String.valueOf(cal.get(Calendar.WEEK_OF_YEAR));
		return cal.getWeekYear() + "" + StringUtils.leftPad(week, 2, "0");
	}

	/**
	 * Gets the padded tile id withzoom.
	 *
	 * @param tileList the tile list
	 * @return the padded tile id withzoom
	 */
	public static List<String> getPaddedTileIdWithzoom(List<Tile> tileList) {
		List<String> tiles = new ArrayList<>();
		for (Tile tile : tileList) {
			tiles.add(tile.getPaddedTile().getIdWithZoom());
		}
		return tiles;
	}

	/**
	 * Gets the tile id with zoom.
	 *
	 * @param tileList the tile list
	 * @return the tile id with zoom
	 */
	public static List<String> getTileIdWithZoom(List<Tile> tileList) {
		List<String> tiles = new ArrayList<>();
		for (Tile tile : tileList) {
			tiles.add(tile.getIdWithZoom());
		}
		return tiles;
	}

	public static String getDateString(Date date) {
		return new SimpleDateFormat("dd-MM-yy").format(date);
	}

	/**
	 * Gets the tile id.
	 *
	 * @param tileList the tile list
	 * @return the tile id
	 */
	public static List<String> getTileId(List<Tile> tileList) {
		List<String> tiles = new ArrayList<>();
		for (Tile tile : tileList) {
			tiles.add(tile.getId());
		}
		return tiles;
	}

	public static ReportInfoWrapper parseConfigToWrapper(String configuration) {
		ReportInfoWrapper reportInfo = new ReportInfoWrapper();
		String configJson = null;
		try {
			if (Utils.isValidString(configuration)) {
				configJson = configuration.replace(ForesightConstants.SINGLEQUOTE, ForesightConstants.QUOTE);
				logger.info("configJson {} ", configJson);
				reportInfo = new Gson().fromJson(configJson, ReportInfoWrapper.class);
				reportInfo.setStartDate(new Date(reportInfo.getStartTime()));
				reportInfo.setEndDate(new Date(reportInfo.getEndTime()));
			}
		} catch (Exception e) {
			logger.error("Exception getting parseing configuration Exception for {} : {}", configJson,
					Utils.getStackTrace(e));
		}
		return reportInfo;
	}

	public static String sendPostRequestWithOutTimeOut(String URI, HttpEntity httpEntity) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		logger.info("going to request {}", URI);
		HttpPost httpPost = new HttpPost(URI);
		httpPost.setEntity(httpEntity);
		try {
			CloseableHttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("error in http post request " + URI);
				throw new RestException("Failed with HTTP error code : " + statusCode);
			}
			try {
				HttpEntity entity = response.getEntity();
				if (entity == null) {
					logger.error("response contains no content " + URI);
					throw new ClientProtocolException("Response contains no content");
				}

				String responseString = readData(entity.getContent());

				EntityUtils.consume(entity);
				return responseString;
			} finally {
				response.close();

			}
		} catch (Exception e) {
			logger.error("error in http post request {} due to {}", URI, getStackTrace(e));
			throw new RestException(e.getMessage());
		} finally {
			httpPost.releaseConnection();

			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error("error in closing client for uri {}", URI);

			}
		}
	}

	/**
	 * Checks if is not null.
	 *
	 * @param object the object
	 * @return true, if is not null
	 */
	public static boolean isNotNull(Object object) {
		final boolean IS_NULL = false;
		if (object == null)
			return IS_NULL;
		return !IS_NULL;
	}

	/**
	 * Checks if is valid list.
	 *
	 * @param list the list
	 * @return true, if is valid list
	 */
	public static boolean isValidList(List list) {
		final boolean IS_VALID = false;
		if (list != null && !list.isEmpty()) {
			return !IS_VALID;
		}
		return IS_VALID;
	}
	
	
	public static boolean isValidMap(Map map) {
		final boolean IS_VALID = false;
		if (map != null && !map.isEmpty()) {
			return !IS_VALID;
		}
		return IS_VALID;
	}
	
	public static <T extends Enum<T>> List<T> convertStringToEnumList(Class<T> clazz, List<String> values) {
		List<T> list = null;
		try {
			EnumSet<T> set = EnumSet.noneOf(clazz);
			for (String value : values) {
				try {
					set.add(Enum.valueOf(clazz, value));
				} catch (Exception e) {
					logger.error("Unable to convert string to enum for : {}", value, e.getMessage());
				}
			}
			list = set.stream().collect(Collectors.toList());
		} catch (Exception e) {
			logger.error("Error in converting string to enum Exception : {}", Utils.getStackTrace(e));
		}
		return list;
	}
	
	public static List<String> getListFromStringSeperatedByPattern(String data, String separatedBy) {
		try {
			Pattern pattern = Pattern.compile(separatedBy);
			return pattern.splitAsStream(data).map(String::valueOf).collect(Collectors.toList());
		} catch (Exception e) {
			throw new RestException(e);
		}
	}
	
	/**
	 * Make a GET Request and return response string.
	 *
	 * @param URI the uri
	 * @return the string
	 * @throws RestException the rest exception
	 * @deprecated replaced by {@link HttpGetRequest#getString()}
	 */
	@Deprecated
	public static String sendGetRequest(String URI) {
		try {
			return new HttpGetRequest(URI).getString();
		} catch (HttpException e) {
			throw new RestException(e);
		}
	}

	/**
	 * Check if String is Blank
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		return (str == null) || (str.length() == 0);
	}

	/**
	 * Checks if is valid string value.
	 *
	 * @param value the value
	 * @return true, if is valid string value
	 */
	public static boolean isValidStringValue(String value) {
		if (value != null && !value.equalsIgnoreCase(ForesightConstants.NULL_STRING))
			return true;

		return false;
	}

	/**
	 * Upto two decimal.
	 *
	 * @param number the number
	 * @return the double
	 */
	public static Double uptoTwoDecimal(Double number) {
		try {
			if (number != null && !Double.isNaN(number)) {
				DecimalFormat df = new DecimalFormat("#.##");
				return Double.parseDouble(df.format(number));
			}
		} catch (Exception e) {
			logger.error(" Exception uptoThreeDecimal: " + number + "ERROR==>" + getStackTrace(e));
		}
		return number;
	}

	/**
	 * <pre>
	 * HttpRequest http = new HttpGetRequest(URI);
	 * http.setHeader("ACCEPT", "application/octet-stream");
	 * return http.getImage();
	 * </pre>
	 *
	 * @param url the url
	 * @return the image from url
	 * @throws RestException the rest exception
	 * @deprecated replaced by {@link HttpRequest#getImage()}
	 */
	@Deprecated
	public static BufferedImage getImageFromUrl(String url) {

		BufferedImage image = null;
		try {

			RequestConfig defaultRequestConfig = RequestConfig.custom()
					.setCookieSpec(org.apache.http.client.config.CookieSpecs.DEFAULT).setExpectContinueEnabled(true)
					.build();

			org.apache.http.impl.client.CloseableHttpClient client = org.apache.http.impl.client.HttpClients.custom()
					.setDefaultRequestConfig(defaultRequestConfig).build();

			HttpGet get = new HttpGet(url);
			RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig).setSocketTimeout(90000)
					.setConnectTimeout(50000).setConnectionRequestTimeout(50000).build();

			get.setConfig(requestConfig);
			get.setHeader("ACCEPT", "application/octet-stream");
			org.apache.http.client.methods.CloseableHttpResponse response = client.execute(get);
			response.setHeader("ACCEPT", "application/octet-stream");
			image = ImageIO.read(response.getEntity().getContent());
		} catch (Exception e) {
			logger.error("Exception in  url {} ,getImagesUrl with message {} ", url, "" + Utils.getStackTrace(e));
			throw new RestException("Exception in getImagesUrl with message" + Utils.getStackTrace(e));
		}

		return image;
	}

	/**
	 * Checks if is valid integer.
	 *
	 * @param value the value
	 * @return true, if is valid integer
	 */
	public static boolean isValidInteger(Integer value) {
		try {
			return (value != null);
		} catch (Exception e) {
			logger.error("getting exception on validate Integer = {}", value);
			return false;
		}
	}

	public static List<String> getFormattedDateForTable(String mindate, String maxdate) throws ParseException {

		List<String> dateRange = new ArrayList<>();
		SimpleDateFormat format;
		format = new SimpleDateFormat(ForesightConstants.DATE_FORMAT_yyyy_MM_dd);
		dateRange.add(getFormattedDate(mindate, format));
		dateRange.add(getFormattedDate(maxdate, format));
		return dateRange;
	}

	private static String getFormattedDate(String maxdate, SimpleDateFormat format) throws ParseException {
		Date date = format.parse(maxdate);
		SimpleDateFormat format2 = new SimpleDateFormat(ForesightConstants.YYYY_MM_DD_HH_MM_SS_FORMAT);
		return format2.format(date);
	}

	public static String HttpPostCallWithHeader(String URI, Map<String, String> map, Map<String, String> header) {

		String msg = ForesightConstants.EXCEPTION_NO_RECORD_FOUND;
		try {
			RequestConfig defaultRequestConfig = RequestConfig.custom()
					.setCookieSpec(org.apache.http.client.config.CookieSpecs.DEFAULT).setExpectContinueEnabled(true)
					.build();

			CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
			RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
					.setSocketTimeout(toInteger(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())))
					.setConnectTimeout(toInteger(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())))
					.setConnectionRequestTimeout(toInteger(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())))
					.build();

			logger.info("going to make call to Edge server with URL: " + URI);
			HttpPost httpPost = new HttpPost(URI);
			httpPost.setConfig(requestConfig);
			for (Map.Entry<String, String> entry : header.entrySet()) {
				httpPost.addHeader(entry.getKey(), entry.getValue());
			}

			List<NameValuePair> value = new ArrayList<>();
			for (Map.Entry<String, String> entry : map.entrySet()) {
				value.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(value));
			CloseableHttpResponse response = client.execute(httpPost);
			HttpEntity entity = response.getEntity();
			msg = readData(entity.getContent());
			logger.info("Response from URL : {} ", msg);
			EntityUtils.consume(entity);
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException: {}", getStackTrace(e));
			return ForesightConstants.EXCEPTION_NO_RECORD_FOUND;
		} catch (ConnectException e) {
			logger.error("ConnectException: {}", getStackTrace(e));
			return ForesightConstants.EXCEPTION_ON_CONNECTION;
		} catch (Exception e) {
			logger.error("Exception: {}", getStackTrace(e));
			return ForesightConstants.EXCEPTION_NO_RECORD_FOUND;
		}
		return msg;
	}

	public static Boolean checkForValueInString(String value) {
		if (value != null && !value.isEmpty() && !value.equalsIgnoreCase(ForesightConstants.NULL_STRING)
				&& !value.equalsIgnoreCase(ForesightConstants.HIPHEN)) {
			return true;
		}
		return false;
	}

	public String getDropwizardUrl(String query, String restCall) {
		String url = ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue()) + restCall;
		logger.info("Inside getDropwizardUrl, url {}, query {}", url, query);
		if (query != null && query.length() != 0) {
			url += "?" + query;
		}
		logger.info("Final dropwizard URL {}", url);
		return url;
	}

	public static void checkVerificationCode(String verificationCode, String secretKey) {
		logger.info("Inside checkVerificationCode :{}", verificationCode);
		logger.info("IS_TWO_FACTOR_AUTHENTICATION_ENABLED : {}",
				ConfigUtils.getString(ConfigUtil.IS_TWO_FACTOR_AUTHENTICATION_ENABLED));
		logger.info("MVCODE : {}", AESUtils.decrypt(ConfigUtils.getString(ConfigUtil.MVCODE)));
		if (Boolean.valueOf(ConfigUtils.getString(ConfigUtil.IS_TWO_FACTOR_AUTHENTICATION_ENABLED))
				&& !verificationCode.equals(AESUtils.decrypt(ConfigUtils.getString(ConfigUtil.MVCODE)))) {
			logger.info("going to check IS_TWO_FACTOR_AUTHENTICATION_ENABLED :{}", verificationCode);
			Totp totp = new Totp(secretKey);
			if (!totp.now().equalsIgnoreCase(verificationCode)) {
				throw new AuthenticationServiceException(PostAuthenticationAction.INVALID_VERIFICATION_CODE.toString());
			}
		}
	}

	public static Map<String, String> getMd5HashForUri(String uri) {
		Map<String, String> hash = new HashMap<>();
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			String key = AESUtils.decrypt(ConfigUtils.getString("MD5_KEY")) + "_" + uri;
			byte[] digest2 = digest.digest(key.getBytes());
			BigInteger bigInt = new BigInteger(1, digest2);
			String hashtext = bigInt.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			hash.put("URL", uri + "?key=" + hashtext);
		} catch (NoSuchAlgorithmException e) {
			logger.error("Error while creating hash for URI ", ExceptionUtils.getStackTrace(e));
		}

		return hash;
	}

	public static List<String> removeEmptyStringFromList(List<String> fullList) {

		ArrayList<String> result = new ArrayList<>();
		for (String str : fullList) {
			if (str != null && !str.trim().isEmpty()) {
				result.add(str);
			}
		}
		return result;
	}

	/**
	 * Gets the list of file items.
	 *
	 * @param request the request
	 * @return the list of file items
	 * @throws FileUploadException the file upload exception
	 */
	public static List<FileItem> getListOfFileItems(HttpServletRequest request) throws FileUploadException {
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload fileUpload = new ServletFileUpload(factory);
		return fileUpload.parseRequest(request);
	}

	public static String getNextHexValue(String currentHexValue, Integer hexLength) {

		BigInteger value = new BigInteger(currentHexValue, 16);
		value = value.add(new BigInteger("1"));
		String hexString = value.toString(16);

		Integer appendNoOfZero = hexLength - hexString.length();
		String prefix = Symbol.EMPTY_STRING;
		for (int count = 0; count < appendNoOfZero; count++) {

			prefix += "0";
		}
		return prefix + hexString;

	}

	public static String getHexValueWithZeroPadding(String currentHexValue, Integer hexLength) {

		logger.info("Current Hex Digit Value is :{}", currentHexValue);
		String hexString = currentHexValue != null ? currentHexValue.replaceFirst("^0+(?!$)", "") : "0";
		logger.info("Hex String Value is :{}", hexString);
		Integer appendNoOfZero = hexLength - hexString.length();
		String prefix = Symbol.EMPTY_STRING;
		for (int count = 0; count < appendNoOfZero; count++) {

			prefix += "0";
		}
		return prefix + hexString;

	}

	public static String convertDecimalToHexWithZeroPadding(String decimalValue, Integer hexLength) {
		String hexString = new BigInteger(decimalValue).toString(16);
		Integer appendNoOfZero = hexLength - hexString.length();
		String prefix = Symbol.EMPTY_STRING;
		for (int count = 0; count < appendNoOfZero; count++) {
			prefix += "0";
		}
		return prefix + hexString;
	}

	/**
	 * Check if executed by test.
	 */
	public static boolean isTestBuild() {
		return ConfigUtils.contains("spring.test.build") && ConfigUtils.getBoolean("spring.test.build");
	}

	
	public static String getJsonFromObject(Object entity) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(entity);
		} catch (Exception e) {
			logger.error("Exception while getting getJsonFromObject : {} ", Utils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * This method is used for put file on the remote server all parameter's are
	 * mandatory
	 * 
	 * @param remoteHostIP   : server IP where you want to sftp(put) file
	 * @param port           : server port(Default :22)
	 * @param user           : user name of server(Ex. user@XXXXXX)
	 * @param password       : password
	 * @param sourceFilePath : source file with absolute path
	 * @param remoteFilePath : remote server path where you want to put file.
	 * @return
	 */
	public static Map<String, String> putFileOnRemoteServer(String remoteHostIP, Integer port, String user,
			String password, String sourceFilePath, String remoteFilePath) {
		logger.info(
				"inside @putFileOnRemoteServer method input param remoteHostIP :{}, port :{}, user :{}, password :{}, sourceFilePath :{}, remoteFilePath :{}",
				remoteHostIP, port, user, password, sourceFilePath, remoteFilePath);
		if (remoteHostIP != null && port != null && user != null && password != null && sourceFilePath != null
				&& remoteFilePath != null) {
			Map<String, String> responseMap = new HashMap<>();
			JSch jsch = new JSch();
			Hashtable<String, String> config = new Hashtable<String, String>();
			try {
				Session session = jsch.getSession(user, remoteHostIP, port);
				config.put(ForesightConstants.STRICT_HOST_KEY_CHECKING, ForesightConstants.NO);
				session.setConfig(config);
				session.setPassword(password);
				session.connect();
				logger.debug("session connected successfully !!!!");
				ChannelSftp channel = null;
				channel = (ChannelSftp) session.openChannel(ForesightConstants.SFTP);
				channel.connect();
				logger.debug("channel connected successfully !!!!");
				File localFile = new File(sourceFilePath);
				channel.cd(remoteFilePath);
				channel.put(new FileInputStream(localFile), localFile.getName());
				logger.info("File successfully sftp on remote host :{} on this path :{}", remoteHostIP, remoteFilePath);
				channel.disconnect();
				session.disconnect();
				responseMap.put(ForesightConstants.STATUS, ForesightConstants.SUCCESS);
				return responseMap;
			} catch (Exception exception) {
				logger.error("Exception occured while sftp file on remote server :{}",
						ExceptionUtils.getStackTrace(exception));
				responseMap.put(ForesightConstants.STATUS, ForesightConstants.FAILURE);
				return responseMap;
			}
		} else {
			throw new BusinessException("All input parameter are mandatory for sftp file on remote server");
		}
	}
	
	/**
	 * This method can used for create compressed(zip) file of mutiple or single
	 * file
	 * 
	 * @param zipFilePath     : path of zip file where you want to save
	 *                        compressed(ZIP) file on disk (if file path does't exist
	 *                        this will create automatically)
	 * @param zipFileName     : provide name of file by which you want to save
	 *                        compressed(ZIP) file
	 * @param srcFilePathList : provide source file path as list which files you
	 *                        want to compress.
	 */
	public static void createZipOfMultipleFile(String zipFilePath, String zipFileName, List<String> srcFilePathList) {
		logger.info(
				"inside @createZipOfMultipleFile method going to create ZIP file for input file zipFilePath :{}, srcFilePathList :{}",
				zipFilePath, srcFilePathList);
		if (Utils.hasValue(zipFilePath) && srcFilePathList != null && !srcFilePathList.isEmpty()) {
			try {
				File file = new File(zipFilePath);
				if (!file.exists()) {
					logger.debug("Path is not available going to create");
					file.mkdirs();
				}
				byte[] buffer = new byte[1024];
				FileOutputStream fos = new FileOutputStream(zipFilePath+zipFileName);
				ZipOutputStream zos = new ZipOutputStream(fos);

				for (String path : srcFilePathList) {
					File srcFile = new File(path);
					FileInputStream fis = new FileInputStream(srcFile);
					zos.putNextEntry(new ZipEntry(srcFile.getName()));
					int length;
					while ((length = fis.read(buffer)) > 0) {
						zos.write(buffer, 0, length);
					}
					zos.closeEntry();
					fis.close();
				}
				zos.close();
			} catch (Exception exception) {
				logger.error("Exception occured while creating zip file >>: {}", ExceptionUtils.getStackTrace(exception));
				throw new BusinessException(ExceptionUtils.getMessage(exception));
			}
		} else {
			throw new BusinessException("Both parameters are mandatory !!");
		}
	}

	public static String getFloorPlanRowKeyWithTemplateType(Integer unitId, String template) {
		logger.info("Going to getFloorPlanRowKeyWithTemplateType for unitID {}, templateType {}", unitId, template);
		String rowKey = "";
		if (template != null) {
			rowKey = StringUtils
					.reverse(StringUtils.leftPad(String.valueOf(unitId), InBuildingConstants.ROW_KEY_LENGTH,
							InBuildingConstants.PADDING_CHAR))
					.concat(String.valueOf(template.charAt(InBuildingConstants.FIRST_INDEX)));
		} else {
			rowKey = StringUtils.reverse(StringUtils.leftPad(String.valueOf(unitId), InBuildingConstants.ROW_KEY_LENGTH,
					InBuildingConstants.PADDING_CHAR));
		}
		logger.info("row key getFloorPlanRowKeyWithTemplateType {}", rowKey);
		return rowKey;
	}
}

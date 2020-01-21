package com.inn.foresight.module.tribe.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.http.HttpRequest;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ConfigUtil;

/**
 * The Class Utils.
 */
public class Utils {
	
	/** The logger. */
	private static Logger logger = LogManager.getLogger(Utils.class);

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
			logger.error(" Exception uptoThreeDecimal: " + number + "ERROR==>" + com.inn.foresight.core.generic.utils.Utils.getStackTrace(e));
		}
		return number;
	}

	/**
	 * Send post request.
	 * 
	 * <pre>
     * HttpRequest http = new HttpPostRequest(URI, httpEntity);
     * return http.getString();
     * </pre>
	 *
	 * @param URI the uri
	 * @param httpEntity the http entity
	 * @return the string
	 * @throws RestException the business exception
	 * @deprecated replaced by {@link HttpRequest#getString()}
	 */
	@Deprecated
	public static String sendPostRequest(String URI, HttpEntity httpEntity) throws RestException {
		RequestConfig defaultRequestConfig = RequestConfig.custom()
				.setCookieSpec(org.apache.http.client.config.CookieSpecs.DEFAULT).setExpectContinueEnabled(true)
				.build();
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
		RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
				.setSocketTimeout(toInteger(ConfigUtils.getString(ConfigUtil.TIMEOUT_VALUE)))
				.setConnectTimeout(toInteger(ConfigUtils.getString(ConfigUtil.TIMEOUT_VALUE)))
				.setConnectionRequestTimeout(toInteger(ConfigUtils.getString(ConfigUtil.TIMEOUT_VALUE))).build();
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

				// String responseString = EntityUtils.toString(entity);
				String responseString = readData(entity.getContent());

				EntityUtils.consume(entity);
				return responseString;
			} finally {
				if (response != null) {
					response.close();
				}
			}
		} catch (ClientProtocolException e) {
			logger.error("error in http post request {} due to {}", URI, com.inn.foresight.core.generic.utils.Utils.getStackTrace(e));
			throw new RestException(e.getMessage());
		} catch (IOException e) {
			logger.error("error in http post request {} due to {}", URI, com.inn.foresight.core.generic.utils.Utils.getStackTrace(e));
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			logger.error("error in http post request {} due to {}", URI, com.inn.foresight.core.generic.utils.Utils.getStackTrace(e));
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
	 * Read data.
	 *
	 * @param rd the rd
	 * @return the string
	 */
	public static String readData(InputStream rd) {
		try {
			return IOUtils.toString(rd);
		} catch (IOException e) {
			logger.error("error in reading data: " + com.inn.foresight.core.generic.utils.Utils.getStackTrace(e));
		} finally {
			IOUtils.closeQuietly(rd);
		}
		return "";
	}

	/**
	 * Send post request with input stream.
	 *
	 * <pre>
     * HttpRequest http = new HttpPostRequest(URI, httpEntity);
     * return http.getString();
     * </pre>
     * 
	 * @param url the url
	 * @param inputFile the input file
	 * @return the string
	 * @deprecated replaced by {@link HttpRequest#getString()}
	 */
	@Deprecated
	public static String sendPostRequestWithInputStream(String url, InputStream inputFile) {
		logger.info("Inside sendPostToReportingWithInputStream Sending Http request to drop wizard url & fileName:"
				+ url + "  , ");
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			ContentBody content = new InputStreamBody(inputFile, "file");
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("file", content);
			httppost.setEntity(reqEntity);
			HttpResponse response = httpclient.execute(httppost);
			String responseJson = TRBConstants.FAILURE_JSON;
			if (response.getEntity() != null) {
				responseJson = IOUtils.toString(response.getEntity().getContent());
				logger.info(" Http response from drop wizard :" + responseJson);
			} else {
				logger.info(" Getting null response from drop wizard ");
			}
			return responseJson;
		} catch (ClientProtocolException e) {
			logger.error("Exception class:{} and message:{}", e.getClass(), e.getMessage());
			throw new BusinessException(e.getMessage());
		} catch (IOException e) {
			logger.error("Exception class:{} and message:{}", e.getClass(), e.getMessage());
			throw new BusinessException(e.getMessage());
		}
	}

	/**
	 * To integer.
	 *
	 * @param intStr the int str
	 * @return the integer
	 */
	public static Integer toInteger(String intStr) {
		try {
			if (intStr != null && !"null".equalsIgnoreCase(intStr)) {
				if (intStr.contains(".")) {
					return toDouble(intStr).intValue();
				}
				return Integer.parseInt(intStr);
			}
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
				// logger.info("going to convert to double " + str);
				return Double.parseDouble(str);
			}
		} catch (Exception e) {
			logger.error("exception on converting to double " + str);
		}
		return null;
	}

	/**
	 * Checks if is valid string.
	 *
	 * @param string the string
	 * @return true, if is valid string
	 */
	public static boolean isValidString(String string) {
		final boolean VALID = true;
		if (string == null || TRBConstants.BLANK_STRING.equals(string) || "null".equalsIgnoreCase(string))
			return !VALID;
		return VALID;
	}

	/**
	 * Removes the special char.
	 *
	 * @param value the value
	 * @return the string
	 */
	public static String removeSpecialChar(String value) {
		value = value.replace("*", "").replace("%", "").replace("'", "").replace("\"", "");
		return value;
	}

	
	/**
	 * Gets the start and end date of week.
	 *
	 * @param date the date
	 * @return the start and end date of week
	 */
	public static Date[] getStartAndEndDateOfWeek(Date date) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		c1.add(Calendar.DATE, -1);
		c1.setFirstDayOfWeek(Calendar.MONDAY);
		Calendar first = (Calendar) c1.clone();
		first.add(Calendar.DAY_OF_WEEK, first.getFirstDayOfWeek() - first.get(Calendar.DAY_OF_WEEK));

		Calendar last = (Calendar) first.clone();
		last.add(Calendar.DAY_OF_YEAR, 6);

		return new Date[] { first.getTime(), last.getTime() };
	}

	/**
	 * Parses the string to date.
	 *
	 * @param dateString the date string
	 * @param format the format
	 * @return the date
	 */
	public static Date parseStringToDate(String dateString, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date date = sdf.parse(dateString);
			return date;
		} catch (ParseException e) {
			logger.error("Exception while parseStringToDate:{}", e.getMessage());
			return null;
		}
	}

	/**
	 * Parses the date to string.
	 *
	 * @param date the date
	 * @param format the format
	 * @return the string
	 * @throws RestException the business exception
	 */
	public static String parseDateToString(Date date, String format) throws RestException {
		try {
			DateFormat df = new SimpleDateFormat(format);
			String reportDate = df.format(date);
			return reportDate;
		} catch (Exception ex) {
			throw new RestException("Unable to parse Date : " + date + " in format " + format);
		}
	}

	/**
	 * Prints the logs.
	 *
	 * @param msg the msg
	 */
	public static void printLogs(String msg) {
		try {
			logger.info(msg);
		} catch (Exception e) {
			logger.info("Exception on printLogs");
		}
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
	
}

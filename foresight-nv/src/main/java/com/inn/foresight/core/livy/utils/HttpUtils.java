package com.inn.foresight.core.livy.utils;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.foresight.core.generic.utils.Utils;


/**
 * The Class HttpUtils.
 * 
 * @author Zafar
 */
public class HttpUtils {

	/** The Constant logger. */
	private static final  Logger logger = LogManager.getLogger(HttpUtils.class);

	/** The Constant ACCEPT. */
	public static final String ACCEPT = "Accept";
	
	/** The Constant APPLICATION_JSON. */
	public static final String APPLICATION_JSON= "application/json";

	/** The Constant CONTENT_TYPE. */
	public static final String CONTENT_TYPE = "Content-type";
	
	/** The Constant UNICODE_8. */
	public static final String UNICODE_8=  "UTF-8";
	
	/**
	 * Instantiates a new http utils.
	 */
	private HttpUtils() {
		super();
	}

	/**
	 * Send post request.
	 *
	 * @param stringUrl the string url
	 * @param json the json
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String sendPostRequest(String stringUrl, String json) throws IOException {
		String data =null;
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(stringUrl);
			StringEntity entity = new StringEntity(json);
			httpPost.setEntity(entity);
			httpPost.setHeader(ACCEPT, APPLICATION_JSON);
			httpPost.setHeader(CONTENT_TYPE, APPLICATION_JSON);
			CloseableHttpResponse response = client.execute(httpPost);
			data = EntityUtils.toString(response.getEntity(), UNICODE_8);
		} catch (ParseException e) {
			logger.info("ParseException sendGetRequest :{}", Utils.getStackTrace(e));
		} finally {
			client.close();
		}
		return data;
	}

	/**
	 * Send get request.
	 *
	 * @param stringUrl the string url
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String sendGetRequest(String stringUrl) throws IOException {
		HttpGet httpGet = new HttpGet(stringUrl);
		String response = null;
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			httpGet.setHeader(ACCEPT, APPLICATION_JSON);
			httpGet.setHeader(CONTENT_TYPE, APPLICATION_JSON);
			CloseableHttpResponse httpResponse = client.execute(httpGet);
			response = EntityUtils.toString(httpResponse.getEntity(), UNICODE_8);
		} catch (ParseException e) {
			logger.info("ParseException sendGetRequest :{}", Utils.getStackTrace(e));
		} finally {
			client.close();
		}
		return response;
	}
	
	/**
	 * Send delete request.
	 *
	 * @param stringUrl the string url
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String sendDeleteRequest(String stringUrl) throws IOException {
		HttpDelete httpDelete = new HttpDelete(stringUrl);
		String response = null;
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			httpDelete.setHeader(ACCEPT, APPLICATION_JSON);
			httpDelete.setHeader(CONTENT_TYPE, APPLICATION_JSON);
			CloseableHttpResponse httpResponse = client.execute(httpDelete);
			response = EntityUtils.toString(httpResponse.getEntity(), UNICODE_8);
		} catch (ParseException e) {
			logger.info("ParseException sendDeleteRequest :{}", Utils.getStackTrace(e));
		} finally {
			client.close();
		}
		return response;
	}
}
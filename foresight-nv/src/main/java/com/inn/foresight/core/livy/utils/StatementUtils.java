/*
 * 
 */
package com.inn.foresight.core.livy.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.inn.foresight.core.livy.model.Statement;

/**
 * The Class StatementUtils.
 *
 * @author Zafar
 */
public class StatementUtils {

	/** The object mapper. */
	public static Gson objectMapper = new Gson();
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(StatementUtils.class);

	/**
	 * Instantiates a new statement utils.
	 */
	private StatementUtils() {
		super();
	}

	/**
	 * Submit statement.
	 *
	 * @param payLoad the pay load
	 * @param sessionId the session id
	 * @param sessionUrl the session url
	 * @return the statement
	 */
	public static Statement submitStatement(String payLoad, Integer sessionId, String sessionUrl) {
		String response = null;
		Statement statement = null;
		try {
			response = HttpUtils.sendPostRequest(sessionUrl + "/" + sessionId + "/statements", payLoad);
			logger.info("32--------------------------------------{}", payLoad);
			statement = objectMapper.fromJson(response, Statement.class);
		} catch (IOException e) {
			logger.error("IOException in submitStatement   {}", ExceptionUtils.getStackTrace(e));
		} catch (JsonParseException e) {
			logger.error("JsonParseException in submitStatement   {}", ExceptionUtils.getStackTrace(e));
		}
		return statement;

	}
	
	/**
	 * Gets the statement output.
	 *
	 * @param statement the statement
	 * @param sessionId the session id
	 * @param sessionUrl the session url
	 * @return the statement output
	 */
	public static String getStatementOutput(Statement statement, Integer sessionId, String sessionUrl) {
		String response = "Error in fetching data";
		List<String> data = new ArrayList<>();
		statement = getStatementFromSession(sessionId, statement.getId(), sessionUrl);
		try {
			if (statement != null)
				response = statement.getOutput().getData().toString();
			data = extractDataFromResponse(response);
		} catch (Exception e) {
			logger.error("Exception in getStatementOutput------------{}", ExceptionUtils.getStackTrace(e));
			data.add(response);
		}
		return data.toString();
	}

	/**
	 * Extract data from response.
	 *
	 * @param response the response
	 * @return the list
	 */
	private static List<String> extractDataFromResponse(String response) {
		response = response.replace("{text/plain=", "");
		String[] lines = response.split("\\r?\\n");
		List<String> rowList = new ArrayList<>();
		for (String line : lines) {
			if (line.startsWith("{")) {
				rowList.add(line);
			}
		}
		return rowList;
	}

	/**
	 * Gets the statement from session.
	 *
	 * @param sessionId the session id
	 * @param statementId the statement id
	 * @param sessionUrl the session url
	 * @return the statement from session
	 */
	public static Statement getStatementFromSession(Integer sessionId, String statementId, String sessionUrl) {
		String getStatementURL = sessionUrl + "/" + sessionId + "/statements/" + statementId;
		String response = null;
		Statement statement = null;
		try {
			logger.info("Statement URL ===> {}",getStatementURL);
			response = HttpUtils.sendGetRequest(getStatementURL);
			logger.info("Get Statement From Session response :{}  ", response);
			statement = objectMapper.fromJson(response, Statement.class);
			logger.info("Get Statement From Session statement :{}  ", statement);
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException in get Statement From Session------------{}", ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			logger.error("IOException in get Statement From Session------------{}", ExceptionUtils.getStackTrace(e));
		} catch (JsonParseException e) {
			logger.error("JsonParseException in get Statement From Session   {}", ExceptionUtils.getStackTrace(e));
		}
		return statement;
	}

	/**
	 * Submit statement.
	 *
	 * @param sessionId the session id
	 * @param code the code
	 * @param sessionUrl the session url
	 * @return the statement
	 */
	public static Statement submitStatement(Integer sessionId, String code, String sessionUrl) {
		Statement statement = null;
		if (code != null && code != "") {
			String payLoad = ScalaUtils.createStatement(code);
			statement = StatementUtils.submitStatement(payLoad, sessionId, sessionUrl);
		}
		return statement;
	}
}

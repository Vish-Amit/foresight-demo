package com.inn.foresight.core.livy.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.inn.commons.Symbol;
import com.inn.foresight.core.livy.constants.LivyConstants;

/**
 * The Class ScalaUtils.
 *
 * @author Zafar
 */
public class ScalaUtils {
	
	/** The Constant logger. */
	private static final  Logger logger = LogManager.getLogger(ScalaUtils.class);

	/**
	 * Instantiates a new scala utils.
	 */
	private ScalaUtils() {
		super();
	}

	/**
	 * Sets the caching statement.
	 *
	 * @param code the code
	 * @param caching the caching
	 * @param dfName the df name
	 * @return the string
	 */
	public static String setCachingStatement(String code, Boolean caching, String dfName) {
		if (caching)
			code = code + dfName + LivyConstants.CACHING_SYNTAX;
		return code;
	}

	/**
	 * Gets the option.
	 *
	 * @param param the param
	 * @param value the value
	 * @return the option
	 */
	public static String getOption(String param, String value) {
		return LivyConstants.ADD_OPTION_SYNTAX.replace("param", param).replace("value", value);
	}

	/**
	 * Gets the df name.
	 *
	 * @param val the val
	 * @return the df name
	 */
	public static String getDfName(String val) {
		if (val == null)
			val = "temp";
		return val + "DF";
	}

	/**
	 * Gets the register table syntex.
	 *
	 * @param dfSyntax the df syntax
	 * @param tableName the table name
	 * @return the register table syntex
	 */
	public static String getRegisterTableSyntex(String dfSyntax, String tableName) {
		return dfSyntax + LivyConstants.CREATE_TEMPVIEW_SYNTAX.replace("viewName", tableName);
	}

	/**
	 * Gets the catalog options.
	 *
	 * @param dfName the df name
	 * @param tableName the table name
	 * @param startTS the start TS
	 * @param endTS the end TS
	 * @return the catalog options
	 */
	public static String getCatalogOptions(String dfName, String tableName, Long startTS, Long endTS) {
		String catalogStament = tableName;
		if (isValid(startTS) && isValid(endTS)) {
			String timeRangeCode = LivyConstants.TIME_RANGE_SYNTAX.replace("startTS", startTS.toString()).replace("endTS", endTS.toString());
			catalogStament = catalogStament + LivyConstants.COMMA + timeRangeCode;
		}
		return LivyConstants.LOAD_CATALOG_SYNTAX.replace("catalogParams", catalogStament).replace(LivyConstants.DATAFRAME, dfName);
	}

	/**
	 * Checks if is valid.
	 *
	 * @param value the value
	 * @return true, if is valid
	 */
	public static boolean isValid(Long value) {
		return (value != null && value != 0);
	}

	/**
	 * Creates the statement.
	 *
	 * @param code the code
	 * @return the string
	 */
	public static String createStatement(String code) {
		return "{\"code\":\"" + code + "\"}";
	}

	/**
	 * Read scala code.
	 *
	 * @param path the path
	 * @return the string
	 */
	public static String readScalaCode(String path) {
		String code = null;
		try {
			code = new String(Files.readAllBytes(Paths.get(path)));
			code = StringUtils.chomp(code);
		} catch (IOException e) {
			logger.error("error in readScalaCode {}", ExceptionUtils.getStackTrace(e));
		}
		return code;
	}

	/**
	 * Process code template.
	 *
	 * @param sessionId the session id
	 * @param sessionUrl the session url
	 * @return the string
	 */
	public static String processCodeTemplate(Integer sessionId, String sessionUrl) {
		StatementUtils.submitStatement(sessionId, LivyConstants.SHC_TEMPLATE, sessionUrl);
		return LivyConstants.SHC_TEMPLATE;
	}
	
	/**
	 * Process code basic template.
	 *
	 * @param sessionId the session id
	 * @param sessionUrl the session url
	 * @param basicTemplate the basic template
	 * @return the string
	 */
	public static String processCodeBasicTemplate(Integer sessionId, String sessionUrl, String basicTemplate) {
		StatementUtils.submitStatement(sessionId, basicTemplate, sessionUrl);
		return basicTemplate;
	}

	/**
	 * Gets the table catalog statement code.
	 *
	 * @param tableName the table name
	 * @param tableCatalog the table catalog
	 * @param startTS the start TS
	 * @param endTS the end TS
	 * @param caching the caching
	 * @return the table catalog statement code
	 */
	public static String getTableCatalogStatementCode(String tableName, String tableCatalog, Long startTS, Long endTS, Boolean caching) {
		tableCatalog = tableCatalog.replace(LivyConstants.ATRATE_SYMBOL, LivyConstants.COMMA);
		tableCatalog = LivyConstants.ADD_CATALOG_DEF_SYNTAX.replace("varName", tableName).replace("tableCatalog", tableCatalog);
		String dfSyntax = getDfName(tableName);
		String catalogOptions = getCatalogOptions(dfSyntax, tableName, startTS, endTS);
		String registerTableSyntax = getRegisterTableSyntex(dfSyntax, tableName);
		String catalogStatement = tableCatalog + catalogOptions;
		catalogStatement = setCachingStatement(catalogStatement, caching, dfSyntax);
		catalogStatement = catalogStatement + registerTableSyntax;
		return catalogStatement;
	}

	/**
	 * Gets the load JDBC table code.
	 *
	 * @param tableName the table name
	 * @param caching the caching
	 * @return the load JDBC table code
	 */
	public static String getLoadJDBCTableCode(String tableName, Boolean caching) {
		String username = com.inn.commons.configuration.ConfigUtils.getString(LivyConstants.USER);
		String password = com.inn.commons.configuration.ConfigUtils.getString(LivyConstants.KEY_PASSWORD);
		String connection = com.inn.commons.configuration.ConfigUtils.getString(LivyConstants.CONNECTION);
		String driver = com.inn.commons.configuration.ConfigUtils.getString(LivyConstants.DRIVER);
		String dfName = getDfName(tableName);
		String code = LivyConstants.READ_JDBC_SYNTAX.replace(LivyConstants.DATAFRAME, dfName)  + Symbol.DOT + getOption(LivyConstants.DRIVER, driver)
				+ Symbol.DOT + getOption(LivyConstants.URL, connection) + Symbol.DOT + getOption(LivyConstants.USER, username)
				+ Symbol.DOT + getOption(LivyConstants.KEY_PASSWORD, password)
				+ Symbol.DOT + getOption(LivyConstants.DBTABLE, tableName) + LivyConstants.LOAD_DATA_SOURCE_SYNTAX;
		code = setCachingStatement(code, caching, dfName);
		code = code + getRegisterTableSyntex(dfName, tableName);
		return code;
	}

	/**
	 * Gets the spark sql code.
	 *
	 * @param query the query
	 * @param tempTable the temp table
	 * @param caching the caching
	 * @param show the show
	 * @return the spark sql code
	 */
	public static String getSparkSqlCode(String query, String tempTable, Boolean caching, boolean show) {
		String dfName = getDfName(tempTable);
		String code = LivyConstants.SPARK_SQL_SYNTEX.replace(LivyConstants.DATAFRAME, dfName).replace("query", query);
		code = setCachingStatement(code, caching, dfName);
		if (tempTable != null)
			code = code + getRegisterTableSyntex(dfName, tempTable);
		if (show)
			code = code + dfName + ".toJSON.collect.foreach(a => println(a));";
		return code;
	}
	
	/**
	 * Gets the read parquet statement.
	 *
	 * @param path the path
	 * @param source the source
	 * @param caching the caching
	 * @return the read parquet statement
	 */
	public static String getReadParquetStatement(String path,String source , Boolean caching) {
		String dfName = getDfName(source);
		String code = LivyConstants.READ_PARQUET_SYNTAX.replace(LivyConstants.DATAFRAME, dfName).replace(LivyConstants.SOURCE, path);
		code = setCachingStatement(code, caching, dfName);
		return code;
	}


	/**
	 * Gets the json for hbase load.
	 *
	 * @param type the type
	 * @param sourceName the source name
	 * @param caching the caching
	 * @param appName the app name
	 * @return the json for hbase load
	 */
	public static String getJsonForHbaseLoad(String type, String sourceName, boolean caching, String appName) {
		JSONObject json = new JSONObject();
		try {
			json.put(LivyConstants.TYPE, type);
			json.put(LivyConstants.SOURCENAME, sourceName);
			json.put(LivyConstants.CACHING, caching);
			json.put(LivyConstants.APPNAME, appName);
		} catch (JSONException e) {
			logger.info("JSONException getting Json For HbaseLoad Error Msg {}", ExceptionUtils.getStackTrace(e));
		}
		return json.toString();
	}
	
	/**
	 * Gets the json for parquet read.
	 *
	 * @param type the type
	 * @param path the path
	 * @param sourceName the source name
	 * @param caching the caching
	 * @param appName the app name
	 * @return the json for parquet read
	 */
	public static String getJsonForParquetRead(String type,String path, String sourceName, boolean caching, String appName) {
		JSONObject json = new JSONObject();
		try {
			json.put(LivyConstants.TYPE, type);
			json.put(LivyConstants.PARQUETDATAPATH, path);
			json.put(LivyConstants.SOURCENAME, sourceName);
			json.put(LivyConstants.CACHING, caching);
			json.put(LivyConstants.APPNAME, appName);
		} catch (JSONException e) {
			logger.info("JSONException getting Json For Parque Read Error Msg {}", ExceptionUtils.getStackTrace(e));
		}
		return json.toString();
	}

	/**
	 * Gets the json for spark sql.
	 *
	 * @param type the type
	 * @param query the query
	 * @param caching the caching
	 * @param appName the app name
	 * @param tempTable the temp table
	 * @param show the show
	 * @return the json for spark sql
	 */
	public static String getJsonForSparkSql(String type, String query, boolean caching, String appName, String tempTable, boolean show) {
		JSONObject json = new JSONObject();
		try {
			json.put(LivyConstants.TYPE, type);
			json.put(LivyConstants.QUERY, query);
			json.put(LivyConstants.CACHING, caching);
			json.put(LivyConstants.APPNAME, appName);
			json.put(LivyConstants.TEMPTABLE, tempTable);
			json.put(LivyConstants.SHOW, show);
		} catch (JSONException e) {
			logger.info("JSONException getting Json For Spark Sql Error Msg {}", ExceptionUtils.getStackTrace(e));
		}
		return json.toString();
	}
}
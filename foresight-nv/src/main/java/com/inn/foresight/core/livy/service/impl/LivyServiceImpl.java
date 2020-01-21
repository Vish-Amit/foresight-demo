package com.inn.foresight.core.livy.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.foresight.core.livy.conf.LivyConfiguration;
import com.inn.foresight.core.livy.constants.LivyConstants;
import com.inn.foresight.core.livy.dao.ICatalogDao;
import com.inn.foresight.core.livy.dao.ILivyApplicationDao;
import com.inn.foresight.core.livy.model.CodeTemplate;
import com.inn.foresight.core.livy.model.LivyApplication;
import com.inn.foresight.core.livy.model.Statement;
import com.inn.foresight.core.livy.service.ILivyService;
import com.inn.foresight.core.livy.utils.CuratorClient;
import com.inn.foresight.core.livy.utils.LivyNegotiator;
import com.inn.foresight.core.livy.utils.ScalaUtils;
import com.inn.foresight.core.livy.utils.SessionUtils;
import com.inn.foresight.core.livy.utils.StatementUtils;

/**
 * The Class LivyServiceImpl.
 * 
 * @author Zafar
 */
@Service("LivyServiceImpl")
public class LivyServiceImpl implements ILivyService {

	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(LivyServiceImpl.class);

	/** The livy app dao impl. */
	@Autowired
	private ILivyApplicationDao livyApplicationDao;

	/** The catalog dao. */
	@Autowired
	private ICatalogDao catalogDao;

	/** The livy configuration. */
	@Autowired
	LivyConfiguration livyConfiguration;

	/** The curator client. */
	CuratorClient curatorClient;

	/**
	 * Submit spark sql.
	 *
	 * @param query     the query
	 * @param show      the show
	 * @param tempTable the temp table
	 * @param caching   the caching
	 * @param appName   the app name
	 * @return the string
	 */
	@Override
	public String submitSparkSql(String query, boolean show, String tempTable, Boolean caching, String appName) {
		LivyApplication app = null;
		String response = LivyConstants.NO_LIVE_SESSION_MESSAGE;
		List<String> activeServerList = getServerList();
		app = setValidSessionId(appName, activeServerList);
		Map<String, Integer> sessionServerMap = app.getServerSessionMap();
		int pos = SessionUtils.getNextServerPos(activeServerList);
		Integer sessionId = sessionServerMap.get(activeServerList.get(pos));
		if (sessionId != null) {
			String code = ScalaUtils.getSparkSqlCode(query, tempTable, caching, show);
			Statement statement = StatementUtils.submitStatement(sessionId, code, activeServerList.get(pos));
			response = StatementUtils.getStatementOutput(statement, sessionId, activeServerList.get(pos));
		}
		return response;
	}

	/**
	 * Delete session.
	 *
	 * @param appName the app name
	 * @return the string
	 */
	@Override
	public String deleteSession(String appName) {
		LivyApplication app = livyApplicationDao.getLivyApplication(appName);
		app.setServerSessionMap();
		Iterator<Entry<String, Integer>> iterator = app.getServerSessionMap().entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Integer> entry = iterator.next();
			SessionUtils.deleteSession(entry.getValue(), entry.getKey());
		}
		return "Session deleted successfully";
	}

	/**
	 * Sets the valid session id.
	 *
	 * @param appName    the app name
	 * @param serverList the server list
	 * @return the livy application
	 */
	public LivyApplication setValidSessionId(String appName, List<String> serverList) {
		LivyApplication app = null;
		try {
			app = livyApplicationDao.getLivyApplication(appName);
			app.setServerSessionMap();
			logger.info("appName :{} of session info :{}", appName, app.getSessioninfo());
			createSessionOnAllLivyServers(serverList, app);
			app.setSessioninfo(LivyApplication.objectMapper.toJson(app.getServerSessionMap()));
			livyApplicationDao.updateApp(app);
			LivyApplication updatedApp = livyApplicationDao.getLivyApplication(appName);
			logger.info("appName :{} of updated session info :{}", appName, updatedApp.getSessioninfo());
		} catch (NullPointerException e) {
			logger.error("NullPointer Exception getting on valid Session Id :{}", e.toString());
		} catch (Exception e) {
			logger.error("Exception getting on valid Session Id :{}", ExceptionUtils.getStackTrace(e));
		}
		return app;
	}

	/**
	 * Submit code template.
	 *
	 * @param codeTemplate the code template
	 * @param appName      the app name
	 * @return the string
	 */
	@Override
	public String submitCodeTemplate(String codeTemplate, String appName) {
		LivyApplication app = null;
		String response = LivyConstants.SOMETHING_WENT_WRONG_MESSAGE;
		List<String> activeServerList = getServerList();
		app = setValidSessionId(appName, activeServerList);
		Map<String, Integer> sessionServerMap = app.getServerSessionMap();
		int pos = SessionUtils.getNextServerPos(activeServerList);
		Integer sessionId = sessionServerMap.get(activeServerList.get(pos));
		String code = generateCodeFromCodeTemplate(codeTemplate, app);
		Statement statement = StatementUtils.submitStatement(sessionId, code, activeServerList.get(pos));
		if (statement != null)
			response = StatementUtils.getStatementOutput(statement, sessionId, activeServerList.get(pos));
		return response;
	}

	/**
	 * Gets the code from stored template.
	 *
	 * @param app the app
	 * @return the code from stored template
	 */
	public String getCodeFromStoredTemplate(LivyApplication app) {
		String codeTemplate = app.getCodeTemplate();
		return generateCodeFromCodeTemplate(codeTemplate, app);
	}

	/**
	 * Generate code from code template.
	 *
	 * @param template the template
	 * @param app      the app
	 * @return the string
	 */
	public String generateCodeFromCodeTemplate(String template, LivyApplication app) {
		String code = "";
		try {
			JSONArray array = new JSONArray(template);
			logger.info("JSON --0000000000000000000-Array :{}", array);
			for (int i = 0; i < array.length(); i++) {
				String json = array.get(i).toString();
				JSONObject jsonObj =  new JSONObject(json) ; 
				logger.info("JSONObject to String :{}" ,jsonObj.toString());
				String tempCode = getCodeFromTemplate(jsonObj, app);
				if (tempCode != null)
					code = code + tempCode;
			}
		} catch (JSONException e) {
			logger.error("JSONException in getCodeFromStoredTemplate {}", ExceptionUtils.getStackTrace(e));
		} 
		return code;
	}
	
	public static void main(String[] args) {
		String template ="[{\"appName\":\"CMNokiaRanLivyService\",\"tempTable\":\"FinalNonComplianceData1\",\"query\":\"select * from FinalNonComplianceData1 where rowNum2 between 1 and 100\",\"show\":false,\"type\":\"sql\",\"caching\":false},{\"appName\":\"CMNokiaRanLivyService\",\"tempTable\":\"FinalNonComplianceData2\",\"show\":true,\"type\":\"sql\",\"caching\":false}]";
		JSONArray array = new JSONArray(template);
		for (int i = 0; i < array.length(); i++) {
			//String json = array.getJSONObject(i);
			//JSONObject jsonObj =  array.getJSONObject(i);//new JSONObject(json);
			
			String json = array.get(i).toString();
			System.out.println("json "+json);
			JSONObject jsonObj =  new JSONObject(json) ; 
			System.out.println(jsonObj.toString());
		}
	}

	/**
	 * Gets the code from template.
	 *
	 * @param json the json
	 * @param app the app
	 * @return the code from template
	 * @throws JSONException the JSON exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private String getCodeFromTemplate(JSONObject json, LivyApplication app) throws JSONException {
		String type = json.getString(LivyConstants.CALL_TYPE);
		String code = null;
		CodeTemplate template = StatementUtils.objectMapper.fromJson(json.toString(), CodeTemplate.class);
		switch (type) {
		case LivyConstants.HBASE_TEMPLATE:
			Long startTime = 0L;
			Long endTime = 0L;
			if (app.getPropertyMap() != null) {
				startTime = Long.valueOf(app.getPropertyMap().get(LivyConstants.CELLSTARTTIME));
				endTime = Long.valueOf(app.getPropertyMap().get(LivyConstants.CELLENDTIME));
			}
			String catalog = catalogDao.getCatalogFromPRunner(template.getSourceName());
			if (catalog != null)
				code = ScalaUtils.getTableCatalogStatementCode(template.getSourceName(), catalog, startTime, endTime, template.isCaching());
			break;
		case LivyConstants.JDBC_TEMPLATE:
			code = ScalaUtils.getLoadJDBCTableCode(template.getSourceName(), template.isCaching());
			break;
		case LivyConstants.SPARK_SQL_TEMPLATE:
			code = ScalaUtils.getSparkSqlCode(template.getQuery(), template.getTempTable(), template.isCaching(), template.isShow());
			break;
		case LivyConstants.PARQUET_TEMPLATE:
			code = ScalaUtils.getReadParquetStatement(template.getParquetDataPath(), template.getSourceName(), template.isCaching());
			break;
		default:
			logger.info(LivyConstants.INVALID_DATA_SOURCE_MESSAGE);
		}
		return code;
	}

	/**
	 * Gets the server list.
	 *
	 * @return the server list
	 */
	
	private List<String> getServerList() {
		List<String> urlList = new ArrayList<>();
		if (curatorClient == null) {
			this.curatorClient = new CuratorClient(livyConfiguration.getZkAddress());
		}
		logger.info("Parent Node Path {}",livyConfiguration.getParentNodePath());
		for (String url : curatorClient.getChildNodes(livyConfiguration.getParentNodePath())) {
			urlList.add(this.livyConfiguration.getSessionUrl().replace(LivyConstants.IP, url));
		}
		return urlList;
	}

	
	/**
	 * Creates the session on all livy servers.
	 *
	 * @param serverList the server list
	 * @param app the app
	 */
	private void createSessionOnAllLivyServers(List<String> serverList, LivyApplication app) {
		List<Future<String>> futures = new ArrayList<>();
		if (serverList != null && !serverList.isEmpty()) {
			ExecutorService executor = Executors.newFixedThreadPool(serverList.size());
			for (String url : serverList) {
				LivyNegotiator task = new LivyNegotiator(this, url, app, this.livyConfiguration.getHdfsConnectionUrl(), this.livyConfiguration.getJarPath(), this.livyConfiguration.getCodeTemplatePath(), this.livyConfiguration.getShcTemplateName());
				futures.add(executor.submit(task));
			}
			for (Future<String> f : futures) {
				try {
					f.get();
				} catch (ExecutionException e) {
					logger.error("ExecutionException getting when create Session On All Livy Servers, Error Msg :{} ",ExceptionUtils.getStackTrace(e));
				} catch (InterruptedException e) {
					logger.error("InterruptedException getting when create Session On All Livy Servers, Error Msg :{} ",ExceptionUtils.getStackTrace(e));
				} finally {
					executor.shutdown();
				}
			}
		}
	}

	/**
	 * Gets the application name by module.
	 *
	 * @param vendor the vendor
	 * @param domain the domain
	 * @param technology the technology
	 * @param module the module
	 * @return the application name by module
	 */
	@Override
	public Map<String, String> getApplicationNameByModule(String vendor, String domain, String technology, String module) {
		Map<String, String> appNameMap = new HashMap<>();
		appNameMap.put("appName", livyApplicationDao.getApplicationNameByModule(vendor, domain, technology, module));
		return appNameMap;
	}
}	
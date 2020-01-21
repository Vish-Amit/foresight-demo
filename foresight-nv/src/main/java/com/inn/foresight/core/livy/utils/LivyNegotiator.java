package com.inn.foresight.core.livy.utils;

import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.livy.model.LivyApplication;
import com.inn.foresight.core.livy.model.Session;
import com.inn.foresight.core.livy.service.impl.LivyServiceImpl;

/**
 * The Class LivyNegotiator.
 * 
 * @author Zafar
 */
public class LivyNegotiator implements Callable<String> {

	/** The logger. */
	private Logger logger = LogManager.getLogger(LivyNegotiator.class);

	/** The service. */
	private LivyServiceImpl service;
	
	/** The session url. */
	private String sessionUrl;
	
	/** The jar path. */
	private String jarPath;
	
	/** The app. */
	private LivyApplication app;
	
	/**
	 * Instantiates a new livy negotiator.
	 *
	 * @param service the service
	 * @param sessionUrl the session url
	 * @param app the app
	 * @param hdfsConnectionUrl the hdfs connection url
	 * @param jarPath the jar path
	 * @param codeTemplatePath the code template path
	 * @param shcTemplateName the shc template name
	 */
	public LivyNegotiator(LivyServiceImpl service, String sessionUrl, LivyApplication app, String hdfsConnectionUrl, String jarPath, String codeTemplatePath, String shcTemplateName) {
		super();
		this.service = service;
		this.sessionUrl = sessionUrl;
		this.jarPath = jarPath;
		this.app = app;
	}

	@Override
	public String call() {
		String appName = app.getName();
		Map<String, Integer> sessionServerMap = app.getServerSessionMap();
		Integer sessionId = sessionServerMap.get(sessionUrl);
		logger.info("sessionUrl--------  {}  --- session ID-----------  {}", sessionUrl, sessionId);
		if (sessionId == null || !SessionUtils.checkExistingSession(sessionId, sessionUrl) && !SessionUtils.isPendingRequests(appName, sessionUrl)) {
			try {
				Session session = SessionUtils.createSession(app, sessionUrl, jarPath);
				if (session != null) {
					sessionId = Integer.parseInt(session.getId());
					SessionUtils.setTimeRange(app);
					String shcTemplate = ScalaUtils.processCodeTemplate(sessionId, sessionUrl);
					logger.info("{} SHCTemplate for Catelog => {}", appName, shcTemplate);
					if ((app.getBasicTemplate() != null) && (!app.getBasicTemplate().isEmpty())) {
						String basicTemplate = ScalaUtils.processCodeBasicTemplate(sessionId, sessionUrl, app.getBasicTemplate());
						logger.info("{} basicTemplate is => {}", appName, basicTemplate);
					}
					String code = service.getCodeFromStoredTemplate(app);
					if ((code != null) && (code != ""))
						StatementUtils.submitStatement(sessionId, code, sessionUrl);
					sessionServerMap.put(sessionUrl, sessionId);
				}
			} catch (Exception e) {
				logger.error("Getting Exception createSession , Error Msg {}", Utils.getStackTrace(e));
			}
			SessionUtils.pendingServerSessionMap.get(appName).remove(sessionUrl);
			logger.info("pending request map after removal ---  {}", SessionUtils.pendingServerSessionMap);
		}
		return sessionUrl;
	}
}
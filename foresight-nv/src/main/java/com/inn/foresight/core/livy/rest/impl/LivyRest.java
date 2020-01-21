package com.inn.foresight.core.livy.rest.impl;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.net.util.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.foresight.core.livy.service.ILivyService;
import com.inn.foresight.core.livy.utils.SessionUtils;
import com.inn.foresight.core.livy.wrapper.LivyRestWrapper;

/**
 * The Class LivyRest.
 * 
 * @author Zafar
 */
@Path("/livyDataRest")
@Produces(MediaType.APPLICATION_JSON)
public class LivyRest {

	/** The logger. */
	public Logger logger = LogManager.getLogger(LivyRest.class);

	/** The livy service. */
	@Autowired
	private ILivyService livyService;

	/**
	 * Delete session.
	 *
	 * @param appName the app name
	 * @return the string
	 */
	@GET
	@Path(value = "deleteSession")
	public String deleteSession(@QueryParam("appName") String appName) {
		logger.info("In the @Method getStatementFromLivySession");
		return livyService.deleteSession(appName);
	}

	/**
	 * Submit query to livy.
	 *
	 * @param query the query
	 * @param show the show
	 * @param tempTable the temp table
	 * @param caching the caching
	 * @param appName the app name
	 * @return the string
	 */
	@GET
	@Path(value = "submitSparkSql")
	public String submitSparkSql(@QueryParam("query") String query, @QueryParam("show") boolean show,
			@QueryParam("tempTable") String tempTable, @QueryParam("caching") boolean caching,
			@QueryParam("appName") String appName) {
		logger.info("In the @Method submitQueryToLivy-----{}",SessionUtils.pendingServerSessionMap);
		show = true;
		if ("DataPlotter".equalsIgnoreCase(appName)) {
			byte[] decodedchecksum = Base64.decodeBase64(query.getBytes());
			query = new String(decodedchecksum);
		}
		return livyService.submitSparkSql(query, show, tempTable, caching, appName);
	}

	
	/**
	 * Submit code template.
	 *
	 * @param code the code
	 * @param appName the app name
	 * @return the string
	 */
	@POST
	@Path(value = "submitCodeTemplate")
	public String submitCodeTemplate(String code,@QueryParam("appName") String appName) {
		logger.info("In the @Method submitCodeTemplate");
		return livyService.submitCodeTemplate(code,appName);
	}

	/**
	 * Clean pending server session map.
	 *
	 * @return the string
	 */
	@GET
	@Path(value = "cleanPendingServerSessionMap")
	public String cleanPendingServerSessionMap(){
		SessionUtils.pendingServerSessionMap.clear();
		return null;
	}
	
	/**
	 * Submit spark sql post.
	 *
	 * @param wrapper the wrapper
	 * @return the string
	 */
	@POST
	@Path(value = "submitSparkSqlPost")
	public String submitSparkSqlPost(LivyRestWrapper wrapper) {
		logger.info("In the @Method submitQueryToLivy-----{}",SessionUtils.pendingServerSessionMap);
		Boolean show = true;
		logger.info("Query for spark sql {}",wrapper.getQuery());
		return livyService.submitSparkSql(wrapper.getQuery(), show, wrapper.getTempTable(), wrapper.getCaching(), wrapper.getAppName());
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
	@GET
	@Path("getApplicationNameByModule")
	public Map<String, String> getApplicationNameByModule(@QueryParam("vendor") String vendor, @QueryParam("domain") String domain, @QueryParam("technology") String technology, @QueryParam("module") String module) {
		logger.info("Going to getting Application Name By Vendor :{}, domain :{}, technology :{}, module :{}", vendor, domain, technology, module);
		return livyService.getApplicationNameByModule(vendor, domain, technology, module);
	}
}

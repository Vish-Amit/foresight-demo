package com.inn.foresight.core.livy.dao.impl;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.livy.constants.LivyConstants;
import com.inn.foresight.core.livy.dao.ILivyApplicationDao;
import com.inn.foresight.core.livy.model.LivyApplication;


/**
 * The Class LivyApplicationDaoImpl.
 * 
 * @author Zafar
 */
@Service("LivyApplicationDaoImpl")
public class LivyApplicationDaoImpl extends HibernateGenericDao<Integer,LivyApplication> implements ILivyApplicationDao {

	/**
	 * Instantiates a new livy application dao impl.
	 */
	public LivyApplicationDaoImpl() {
		super(LivyApplication.class);
	}

	/** The logger. */
	private Logger logger = LogManager.getLogger(LivyApplicationDaoImpl.class);

	/**
	 * Gets the livy application.
	 *
	 * @param appName the app name
	 * @return the livy application
	 */
	public LivyApplication getLivyApplication(String appName) {
		LivyApplication app = null;
		logger.info("Going to getting LivyApplication by appName :{}", appName);
		try {
			Query query = getEntityManager().createNamedQuery("getLivyApplicationByName").setParameter(LivyConstants.NAME, appName);
			app = (LivyApplication) query.getSingleResult();
			logger.info("LivyApplication found in DB :{}", app);
		} catch (NoResultException nre) {
			logger.warn("NoResultException caught while getting getLivyApplication Exception : {}", nre.getMessage());
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while getting getLivyApplication  Exception : {}", persistenceException.getMessage());
		} catch (Exception ex) {
			logger.error("Exception in geting livy application {} ", ExceptionUtils.getStackTrace(ex));
		}
		return app;
	}

	/**
	 * Update app.
	 *
	 * @param app the app
	 */
	@Override
	@Transactional
	public void updateApp(LivyApplication app) {
		logger.info("Going to update sessionId by appName :{} on LivyApplication Table", app.getName());
		update(app);
		logger.info("app updated successfully in DB");
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
	public String getApplicationNameByModule(String vendor, String domain, String technology, String module) {
		String appName = "";
		logger.info("Going to getting Application Name By Module :{}", module);
		try {
			Query query = getEntityManager().createNamedQuery("getApplicationNameByModule");
			enableFilters(domain, vendor, technology);
			moduleFilter(module);
			appName = (String) query.getSingleResult();
			logger.info("LivyApplication found in DB :{}", appName);
		} catch (NoResultException nre) {
			logger.warn("NoResultException caught while getting Application Name By Module");
		} catch (PersistenceException e) {
			logger.error("PersistenceException caught while getting Application Name By Module Err Msg : {}",Utils.getStackTrace(e));
		} 
		return appName;
	}
	
	/**
	 * Network element domain filter.
	 *
	 * @param domain
	 *            the domain
	 */
	private void domainFilter(String domain) {
		logger.info("Domain Filter :{}", domain);
		Session s = (Session) getEntityManager().getDelegate();
		s.enableFilter("domainFilterForLivy").setParameter("domain", domain);
	}

	/**
	 * Network element vendor filter.
	 *
	 * @param vendor
	 *            the vendor
	 */
	private void vendorFilter(String vendor) {
		logger.info("Vendor Filter :{}", vendor);
		Session s = (Session) getEntityManager().getDelegate();
		s.enableFilter("vendorFilterForLivy").setParameter("vendor", vendor);
	}
	
	/**
	 * Technology filter.
	 *
	 * @param technology the technology
	 */
	private void technologyFilter(String technology) {
		logger.info("technology Filter :{}", technology);
		Session s = (Session) getEntityManager().getDelegate();
		s.enableFilter("technologyFilterForLivy").setParameter("technology", technology);
	}
	
	private void moduleFilter(String module) {
		logger.info("Module Filter :{}", module);
		Session s = (Session) getEntityManager().getDelegate();
		s.enableFilter("moduleFilterForLivy").setParameter("module", module);
	}
	
	/**
	 * Enable filters.
	 *
	 * @param domain the domain
	 * @param vendor the vendor
	 * @param technology the technology
	 */
	private void enableFilters(String domain, String vendor, String technology) {
		if (domain != null && !domain.isEmpty()) {
			domainFilter(domain.toUpperCase());
		}
		if (vendor != null && !vendor.isEmpty()) {
			vendorFilter(vendor.toUpperCase());
		}
		if (technology != null && !technology.isEmpty()) {
			technologyFilter(technology.toUpperCase());
		}
	}
}
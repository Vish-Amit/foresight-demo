package com.inn.foresight.core.livy.dao.impl;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.livy.conf.LivyConfiguration;
import com.inn.foresight.core.livy.constants.LivyConstants;
import com.inn.foresight.core.livy.dao.ICatalogDao;
import com.inn.foresight.core.livy.model.HBaseTableCatelog;

/**
 * The Class CatalogDaoImpl.
 * 
 * @author Zafar
 */
@Service("CatalogDaoImpl")
public class CatalogDaoImpl extends HibernateGenericDao<Integer,HBaseTableCatelog> implements ICatalogDao {
	
	public CatalogDaoImpl() {
		super(HBaseTableCatelog.class);
	}

	/** The config. */
	@Autowired
	private LivyConfiguration config;

	/** The logger. */
	private Logger logger = LogManager.getLogger(CatalogDaoImpl.class);

	/**
	 * Implement for getting HBase table CATALOG from sequel database.
	 *
	 * @param tableName the table name
	 * @return String
	 */
	public String getTableCatalogByName(String tableName) {
		String catalog = null;
		logger.info("going to get catalog by Name  {}", tableName);
		try {
			Query query = getEntityManager().createNamedQuery("getCatalogByName").setParameter(LivyConstants.NAME, tableName);
			catalog = ((HBaseTableCatelog) query.getResultList().get(0)).getCatalog();
			logger.info("catalog found in DB");
		} catch (NoResultException nre) {
			logger.warn("NoResultException caught while getting TableCatalogByName Exception : {}", nre.getMessage());
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while getting TableCatalogByName  Exception : {}",
					persistenceException.getMessage());
		} catch (Exception ex) {
			logger.error("error in getting catalog {} ", ExceptionUtils.getStackTrace(ex));
		}

		return catalog;
	}
	
	/**
	 * Gets the catalog from P runner.
	 *
	 * @param tableName the table name
	 * @return the catalog from P runner
	 */
	public String getCatalogFromPRunner(String tableName) {
		String catalog = null;
		logger.info("going to get catalog by Name from process runner  {}", tableName);
		try {
			EntityManager entityManger = getEntityManager();
			Query query = entityManger.createNativeQuery(config.getCatalogQuery()).setParameter(LivyConstants.NAME, tableName);
			catalog =  query.getSingleResult().toString();
			logger.info("catalog found in DB");
		} catch (NoResultException nre) {
			logger.warn("NoResultException caught while getting TableCatalogByName Exception : {}", nre.getMessage());
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while getting TableCatalogByName  Exception : {}", persistenceException.getMessage());
		} catch (Exception ex) {
			logger.error("error in geting catalog {} ", ExceptionUtils.getStackTrace(ex));
		}
		return catalog;
	}
}
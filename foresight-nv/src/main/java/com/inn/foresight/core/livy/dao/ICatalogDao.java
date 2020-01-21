package com.inn.foresight.core.livy.dao;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.livy.model.HBaseTableCatelog;

/**
 * The Interface ICatalogDao.
 * 
 * @author Zafar
 */
public interface ICatalogDao extends IGenericDao<Integer,HBaseTableCatelog> {

	/**
	 * Implement for getting HBase table CATALOG from sequel database.
	 *
	 * @param tableName the table name
	 * @return String
	 */
	public String getTableCatalogByName(String tableName);
	
	/**
	 * Gets the catalog from P runner.
	 *
	 * @param tableName the table name
	 * @return the catalog from P runner
	 */
	public String getCatalogFromPRunner(String tableName);
}
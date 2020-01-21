package com.inn.foresight.core.infra.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.infra.dao.HbaseRowPrefixDao;
import com.inn.foresight.core.infra.model.HbaseRowPrefix;
import com.inn.foresight.core.infra.utils.InfraConstants;

@SuppressWarnings("unchecked")
@Repository("HbaseRowPrefixDaoImpl")
@Deprecated
public class HbaseRowPrefixDaoImpl extends HibernateGenericDao<Integer, HbaseRowPrefix> implements HbaseRowPrefixDao {

	private Logger logger = LogManager.getLogger(HbaseRowPrefixDaoImpl.class);

	public HbaseRowPrefixDaoImpl() {
		super(HbaseRowPrefix.class);

	}

	@Override
	public List<HbaseRowPrefix> getAllHbaseRowPrefixData() throws Exception	{
		logger.info("inside getAllHbaseRowPrefixData method in Dao");
		Query query = getEntityManager().createNamedQuery("getAllHbaseRowPrefixData");
		return query.getResultList();
	}
	
	@Override
	public List<String> getAlphaNumericCodeByVendorDomainAndGeographyType(String domain,String vendor,String geographyType,String geographyValue) {
		logger.info("Going to get AlphaNumericCode By Vendor {},Domain {},GeographyType {}, and GeographyValue {}",vendor,domain,geographyType,geographyValue);
		List<String> dataList = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getAlphaNumericCode");
			enableDomainVendorFilter(vendor, domain);
			enableGeographyFilter(geographyType, geographyValue);
			dataList = query.getResultList();
		}catch (PersistenceException ex) {
			logger.error("Exception while getting AlphaNumericCode By Vendor ,Domain and GeographyType,err msg {}", ex.toString());
		}catch(Exception e) {
			logger.error("Exception while getting AlphaNumericCode By Vendor ,Domain and GeographyType,err msg {} ",com.inn.foresight.core.generic.utils.Utils.getStackTrace(e));
			throw new DaoException(e);
		}
		logger.info("Returning AlphaNumericCode with size {}",dataList.size());
		return dataList;
	}
	
	@Override
	public List<String> getAlphaNumericCodeByVendorDomainAndGeographyList(String domain,String vendor,String geographyType,List<String> geographyValue) {
		logger.info("Going to get AlphaNumericCode By Vendor {},Domain {},GeographyType {}, and GeographyValue {}",vendor,domain,geographyType,geographyValue);
		List<String> dataList = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getAlphaNumericCode");
			enableDomainVendorFilter(vendor, domain);
			enableGeographyListFilter(geographyType, geographyValue);
			dataList = query.getResultList();
		}catch (PersistenceException ex) {
			logger.error("Exception while getting AlphaNumericCode By Vendor ,Domain and GeographyType,err msg {}", ex.toString());
		}catch(Exception e) {
			logger.error("Exception while getting AlphaNumericCode By Vendor ,Domain and GeographyType,err msg {} ",com.inn.foresight.core.generic.utils.Utils.getStackTrace(e));
			throw new DaoException(e);
		}
		logger.info("Returning AlphaNumericCode with size {}",dataList.size());
		return dataList;
	}
	
	@Override
	public String getAlphaNumericCodeByDomain(String domain) {
		try {
			return (String)getEntityManager().createNamedQuery("getAlphaNumericCodeByDomain").setParameter("domain", domain).getSingleResult();
		} catch(Exception e) {
			logger.error("Error inside getCodeForGeographyName Dao {} ",com.inn.foresight.core.generic.utils.Utils.getStackTrace(e));
			throw new DaoException(e);
		}
	}
	
	private void enableGeographyFilter(String geographyType,String geographyValue) {
		if (geographyType != null) {
			if (InfraConstants.GEOGRAPHYL1_TABLE.equalsIgnoreCase(geographyType)) {
				enableGeographyL1Filter(geographyValue);
			} else if (InfraConstants.GEOGRAPHYL2_TABLE.equalsIgnoreCase(geographyType)) {
				enableGeographyL2Filter(geographyValue);
			} else if (InfraConstants.GEOGRAPHYL3_TABLE.equalsIgnoreCase(geographyType)) {
				enableGeographyL3Filter(geographyValue);
			} 
		}
	}
	
	private void enableGeographyListFilter(String geographyType,List<String> geographyValue) {
		if (geographyType != null) {
			if (InfraConstants.GEOGRAPHYL1_TABLE.equalsIgnoreCase(geographyType)) {
				enableGeographyL1ListFilter(geographyValue);
			} else if (InfraConstants.GEOGRAPHYL2_TABLE.equalsIgnoreCase(geographyType)) {
				enableGeographyL2ListFilter(geographyValue);
			} else if (InfraConstants.GEOGRAPHYL3_TABLE.equalsIgnoreCase(geographyType)) {
				enableGeographyL3ListFilter(geographyValue);
			} 
		}
	}
	
	private void enableGeographyL1Filter(String geographyL1) {
		if (geographyL1 != null) {
			Session session = (Session) getEntityManager().getDelegate();
			session.enableFilter("hbaseRowPrefixL1Filter").setParameter("l1", geographyL1.toUpperCase());
		}
	}
	
	private void enableGeographyL2Filter(String geographyL2) {
		if (geographyL2 != null) {
			Session session = (Session) getEntityManager().getDelegate();
			session.enableFilter("hbaseRowPrefixL2Filter").setParameter("l2", geographyL2.toUpperCase());
		}
	}
	
	private void enableGeographyL3Filter(String geographyL3) {
		if (geographyL3 != null) {
			Session session = (Session) getEntityManager().getDelegate();
			session.enableFilter("hbaseRowPrefixL3Filter").setParameter("l3", geographyL3.toUpperCase());
		}
	}
	
	private void enableGeographyL1ListFilter(List<String> geographyL1) {
		if (geographyL1 != null) {
			Session session = (Session) getEntityManager().getDelegate();
			session.enableFilter("hbaseRowPrefixL1ListFilter").setParameterList("l1List", geographyL1);
		}
	}
	
	private void enableGeographyL2ListFilter(List<String> geographyL2) {
		if (geographyL2 != null) {
			Session session = (Session) getEntityManager().getDelegate();
			session.enableFilter("hbaseRowPrefixL2ListFilter").setParameterList("l2List", geographyL2);
		}
	}
	
	private void enableGeographyL3ListFilter(List<String> geographyL3) {
		if (geographyL3 != null) {
			Session session = (Session) getEntityManager().getDelegate();
			session.enableFilter("hbaseRowPrefixL3ListFilter").setParameterList("l3List", geographyL3);
		}
	}
	
	private void enableDomainVendorFilter(String vendor, String domain) {
		if (vendor != null) {
			hbaseRowPrefixVendorFilter(vendor);
		} 
		if (domain != null) {
			hbaseRowPrefixDomainFilter(domain);
		}
	}
	
	private void hbaseRowPrefixVendorFilter(String vendor) {
		Session session = (Session) getEntityManager().getDelegate();
		session.enableFilter("hbaseRowPrefixVendorFilter").setParameter("vendor", vendor.toUpperCase());
	}
	
	private void hbaseRowPrefixDomainFilter(String domain) {
		Session session = (Session) getEntityManager().getDelegate();
		session.enableFilter("hbaseRowPrefixDomainFilter").setParameter("domain", domain.toUpperCase());
	}
	
}

package com.inn.foresight.core.infra.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.utils.ExceptionUtil;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.IEmsServerDao;
import com.inn.foresight.core.infra.model.EmsServer;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.EMSType;
import com.inn.foresight.core.infra.utils.enums.Vendor;

/**
 * The Class EmsServerDaoImpl.
 */
@Repository("EmsServerDaoImpl")
public class EmsServerDaoImpl extends HibernateGenericDao<Integer, EmsServer> implements IEmsServerDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(EmsServerDaoImpl.class);

	/**
	 * Instantiates a new ems server dao impl.
	 */
	public EmsServerDaoImpl() {
		super(EmsServer.class);
	}

	/**
	 * Find all in transaction.
	 *
	 * @return the list
	 * @throws DaoException the dao exception
	 */
	@Transactional(readOnly = true)
	@Override
	public List<EmsServer> findAllInTransaction() {
		return super.findAll();
	}
	
	/**
	 * Gets the ems server by name.
	 *
	 * @param name the name
	 * @return the ems server by name
	 * @throws DaoException the dao exception
	 */
	@Override
	public EmsServer getEmsServerByName(String name) {
		logger.info("Going to get EmsServer By Name {}",name);
		EmsServer emsServer = new EmsServer();
		try {
			Query query = getEntityManager().createNamedQuery("getEmsServerByName");
			query.setParameter("name", name);
			return (EmsServer) query.getSingleResult();
		} catch (Exception e) {
			throw new DaoException(ExceptionUtil.generateExceptionCode(InfraConstants.DAO, "EmsServer", e));
		}
	}
	
	@Override
	public List<Object[]> getEmsServer(Integer geographyId, String emsType , String geoType,String domain,String vendor,String technology) {
		List<Object[]> list = new ArrayList<>();
		logger.info("Going to getIpListFromEmsServer for geoType {}, geographyId {}, domain {}, vendor {} and emsType {} and technology {}", geoType,geographyId, domain,vendor,emsType,technology);
		try {
			Query query = getEntityManager().createNamedQuery("getIpListFromEmsServer");
			enableFilters(geographyId,geoType,emsType,domain,vendor,technology);
			list = query.getResultList();
		} catch (IllegalStateException | PersistenceException e) {
			logger.warn("Exception in getting ip list err msg {}", e);
		}
		logger.info("Returing a getIpListFromEmsServer list size {}",list.size());
		return list;
	}
	
	@Override
	public List<EmsServer> getAllEmsServerByEmsType(Integer geographyId, String emsType,String geoType,String domain,String vendor,String technology) {
		logger.info("Going to get All EmsServer By EmsType");
		List<EmsServer> emsServers=Collections.emptyList();
		try {
			Query query = getEntityManager().createNamedQuery("getAllEmsServerByEmsType");
			enableFilters(geographyId,geoType,emsType,domain,vendor,technology);
			emsServers = query.getResultList();
		} catch (PersistenceException e) {
			logger.warn("Exception in getting All EmsServer By EmsType err msg {}",e);
		} 
		logger.info("Returing a get All EmsServer list size {}",emsServers.size());
		return emsServers;
	}
	
	@Override
	public EmsServer getEmsServerByIpAndEmsType(String ip, String emsType) {
		logger.info("Going to get EmsServer By dns {} And EmsType {}",ip,emsType);
		EmsServer emsServer=null;
		try {
			Query query = getEntityManager().createNamedQuery("getEmsServerByIpAndEmsType").setParameter("dns", ip);
			enableFilters(null,null,emsType,null,null,null);
			emsServer = (EmsServer) query.getSingleResult();
		} catch (NoResultException | NonUniqueResultException | IllegalStateException | QueryTimeoutException | TransactionRequiredException e) {
			logger.warn("Error in get EmsServer By Ip {} And EmsType {} error msg {}",ip,emsType,e.toString());
		} catch (PersistenceException e) {
			logger.warn("PersistenceException in get EmsServer By Ip {} And EmsType {} error msg {}",ip,emsType,e.getMessage());
		}
		return emsServer; 
	}
	
	@Override
	public List<Object[]> getEmsNameAndIpByDomainAndVendor(String domain, String vendor, String technology) {
		try {
			Query query = getEntityManager().createNamedQuery("getEmsNameAndIpByDomainAndVendor");
			enableFilters(null, null, null, domain, vendor, technology);
			return query.getResultList();
		} catch (NoResultException | NonUniqueResultException | IllegalStateException | QueryTimeoutException
				| TransactionRequiredException e) {
			logger.error("Error in getEmsNameAndIpByDomainAndVendor By error msg {}", ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("Exception in getEmsNameAndIpByDomainAndVendor By error msg {}",
					ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
	
	@Override
	public List<String> getDistinctEmsName() {
		logger.info("Going to get getDistincEmsName {}");

		try {
			Query query = getEntityManager().createNamedQuery("getDistinctEmsNameList");
			return  query.getResultList();
		} catch (Exception e) {
			throw new DaoException(ExceptionUtil.generateExceptionCode(InfraConstants.DAO, "EmsServer", e));
		}
	}
	
	@Override
	public List<String> getDistinctEmsIP() {
		logger.info("Going to get getDistinctEmsIPList {}");

		try {
			Query query = getEntityManager().createNamedQuery("getDistinctEmsIPList");
			return  query.getResultList();
		} catch (Exception e) {
			throw new DaoException(ExceptionUtil.generateExceptionCode(InfraConstants.DAO, "EmsServer", e));
		}
	}
	
	
	private void enableFilters(Integer geographyId,String geoType,String emsType,String domain,String vendor,String technology) {
		if (emsType != null) {
			enableEmsTypeFilter(emsType.toUpperCase());
		}
		if (domain != null) {
			enableDomainFilter(domain.toUpperCase());
		}
		if (vendor != null) {
			enableVendorFilter(vendor.toUpperCase());
		}
		if (technology != null) {
			enableTechnologyFilter(technology.toUpperCase());
		}
	}
	
	private void enableEmsTypeFilter(String emsType) {
		logger.info("ems server emsType Filter {}", emsType);
		Session s = (Session) getEntityManager().getDelegate();
		s.enableFilter("emsServerEmstypeFilter").setParameter("emsType", emsType);
	}
	
	private void enableDomainFilter(String domain) {
		logger.info("ems server domain Filter {}", domain);
		Session s = (Session) getEntityManager().getDelegate();
		s.enableFilter("emsServerDomainFilter").setParameter("domain", domain);
	}
	
	private void enableTechnologyFilter(String technology) {
		logger.info("ems server technology Filter {}", technology);
		Session s = (Session) getEntityManager().getDelegate();
		s.enableFilter("emsServerTechnologyFilter").setParameter("technology", technology);
	}
	
	private void enableVendorFilter(String vendor) {
		logger.info("ems server vendor Filter {}", vendor);
		Session s = (Session) getEntityManager().getDelegate();
		s.enableFilter("emsServerVendorFilter").setParameter("vendor", vendor);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<EmsServer> getEmsServerByVendorDomainAndEmsType(Vendor vendor, Domain domain, EMSType emsType) {
		logger.info("Going to get EmsServer By vendor {},domain {} And EmsType {}",vendor,domain,emsType);
		List<EmsServer> emsServerList= new ArrayList<EmsServer>();
		try {
			Query query = getEntityManager().createNamedQuery("getEmsServerByVendorDomainAndEmsType");
			if(vendor != null){
				query.setParameter("vendor", vendor);
			}
			if(domain != null){
				query.setParameter("domain", domain);
			}
			if(emsType != null){
				query.setParameter("emsType", emsType);
			}
			emsServerList = query.getResultList();
		} catch (Exception e) {
			logger.error("Exception in get EmsServer By Vendor {}, Domain {} And EmsType {} error trace {}",vendor,domain,emsType,Utils.getStackTrace(e));
		}
		return emsServerList; 
	}
	
}

package com.inn.foresight.core.infra.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.utils.ExceptionUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.IBackhaulDetailDao;
import com.inn.foresight.core.infra.model.BackhaulDetail;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.Vendor;
import com.inn.foresight.core.infra.wrapper.BackHaulDetailWrapper;
@Repository("BackhaulDetailDaoImpl")
public class BackhaulDetailDaoImpl extends HibernateGenericDao<Integer, BackhaulDetail>  implements IBackhaulDetailDao{
	private Logger logger = LogManager.getLogger(BackhaulDetailDaoImpl.class);


	public BackhaulDetailDaoImpl() {
      super(BackhaulDetail.class);
	}
	@Override
	public BackHaulDetailWrapper getHubSiteDetailByNeName(String neName) {
		logger.info("inside the method  getHubSiteDetailByNeName {}",neName);
		try {
			Query query = getEntityManager().createNamedQuery("getHubSiteDetailByNeName");
			query.setParameter(ForesightConstants.NE_NAME, neName);
			logger.info("going to get result for query");
			return (BackHaulDetailWrapper) query.getSingleResult();
 
		} catch (NonUniqueResultException e) {
			logger.error("Non Unique Result Exception in getting overview, err msg= {}", e.getMessage());
			throw new DaoException(ExceptionUtil.generateExceptionCode("Dao", InfraConstants.NETWORKELEMENT_TABLE, e));
		} catch (Exception e) {
			logger.error("Exception inisde the method getHubSiteDetailByNeName {}",Utils.getStackTrace(e));

			throw new DaoException(ExceptionUtil.generateExceptionCode("Dao", InfraConstants.NETWORKELEMENT_TABLE, e));
		}
	}
	
	@Override
	public String getDeviceLocationByNeName(String neName) {
		logger.info("inside the method  getHubSiteDetailByNeName {}",neName);
		try {
			Query query = getEntityManager().createNamedQuery("getDeviceLocationByNeName");
			query.setParameter(ForesightConstants.NE_NAME, neName);
			logger.info("going to get result for query");
			return (String)query.getSingleResult();
 
		} catch (NonUniqueResultException e) {
			logger.error("Non Unique Result Exception in getting overview, err msg= {}", e.getMessage());
			throw new DaoException(ExceptionUtil.generateExceptionCode("Dao", InfraConstants.NETWORKELEMENT_TABLE, e));
		} catch (Exception e) {
			logger.error("Exception inisde the method getHubSiteDetailByNeName {}",Utils.getStackTrace(e));

			throw new DaoException(ExceptionUtil.generateExceptionCode("Dao", InfraConstants.NETWORKELEMENT_TABLE, e));
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<BackhaulDetail> getBackhaulDetailByNeName(List<String> neNameList) {
		logger.info("inside the method  getBackhaulDetailByNeName {}",neNameList);
		try {
			Query query = getEntityManager().createNamedQuery("getBackhaulDetailByNeName");
			query.setParameter(ForesightConstants.NE_NAME, neNameList);
			return query.getResultList();
 
		} catch (Exception e) {
			logger.error("Exception inisde the method getBackhaulDetailByNeName {}",Utils.getStackTrace(e));
		}
		return Collections.emptyList();
	}
	
	
	@Override
	public List<Object[]> getDeviceLocationByNeNameList(String domain, String vendor, List<String> neName) {
		logger.info("Getting DeviceLocationByNeNameList for domain : {} and vendor : {} and nenamelist : {}",domain,vendor,neName.size());
		List<Object[]> networkElement = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getDeviceLocationByNeNameList");
			query.setParameter(ForesightConstants.DOMAIN_SMALL, Domain.valueOf(domain));
			query.setParameter(ForesightConstants.VENDOR_SMALL, Vendor.valueOf(vendor));
			query.setParameter(ForesightConstants.NE_NAME, neName);
			logger.info("going to get result for query");
			networkElement =  (List<Object []>)query.getResultList();
		} catch (NonUniqueResultException e) {
			logger.error("Non Unique Result Exception in getting device location, err msg= {}", e.getMessage());
			throw new DaoException(ExceptionUtil.generateExceptionCode("Dao", InfraConstants.NETWORKELEMENT_TABLE, e));
		} catch (Exception e) {
			logger.error("Exception inisde the method get DeviceLocationByNeNameList {}",Utils.getStackTrace(e));

			throw new DaoException(ExceptionUtil.generateExceptionCode("Dao", InfraConstants.NETWORKELEMENT_TABLE, e));
		}
		return networkElement;
	}
	
	@Override
	public List<Object[]> getHopTypeByDomainVendor(String domain, String vendor, List<String> hopType) {
		logger.info("Getting getHopTypeByDomainVendor for domain : {} and vendor : {} and hopType : {}",domain,vendor,hopType.size());
		List<Object[]> networkElement = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getHopTypeByDomainVendor");
			query.setParameter(ForesightConstants.DOMAIN_SMALL, Domain.valueOf(domain));
			query.setParameter(ForesightConstants.VENDOR_SMALL, Vendor.valueOf(vendor));
			query.setParameter("hopType", hopType);
			logger.info("going to get result for hopType query");
			networkElement =  (List<Object []>)query.getResultList();
		} catch (NonUniqueResultException e) {
			logger.error("Non Unique Result Exception in getting HopType, err msg= {}", e.getMessage());
			throw new DaoException(ExceptionUtil.generateExceptionCode("Dao", InfraConstants.NETWORKELEMENT_TABLE, e));
		} catch (Exception e) {
			logger.error("Exception inisde the method get getHopTypeByDomainVendor {}",Utils.getStackTrace(e));

			throw new DaoException(ExceptionUtil.generateExceptionCode("Dao", InfraConstants.NETWORKELEMENT_TABLE, e));
		}
		return networkElement;
	}
}

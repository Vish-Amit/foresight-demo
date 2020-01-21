package com.inn.foresight.core.infra.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.infra.dao.INEGeographyMappingDao;
import com.inn.foresight.core.infra.model.NEGeographyMapping;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.utils.enums.Vendor;


@Repository("NEGeographyMappingDaoImpl")
public class NEGeographyMappingDaoImpl extends HibernateGenericDao<Integer, NEGeographyMapping> implements INEGeographyMappingDao {
	
	private Logger logger = LogManager.getLogger(NEGeographyMappingDaoImpl.class);
   
	public NEGeographyMappingDaoImpl() {
		super(NEGeographyMapping.class);
	}
	 
	@Override
	public List<String> getNodeDetailByDomainVendor(String domain, String vendor,String node) {
		logger.info("Going to get Other Geography Node Detail by domain {},vendor {} domain enum :{}",domain,
				vendor,node);
		List<String> networkElement = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getNodeDetailByDomainVendor");
			query.setParameter(InfraConstants.NE_DOMAIN_KEY, Domain.valueOf(domain.toUpperCase()));
			query.setParameter(InfraConstants.VENDOR_KEY, Vendor.valueOf(vendor.toUpperCase()));
			query.setParameter("node", NEType.valueOf(node.toUpperCase()));

			networkElement =  query.getResultList();
			logger.info("networkElement  :"+networkElement.size());
		} catch (IllegalStateException | PersistenceException e) {
			logger.warn("Error while getting Other Geography  Node Detail By DomainVendor ByNename,err msg {}", e.getMessage());
		}
		return networkElement;
	}
	
	@Override
	public List<String> getNodeDetailByGeography(String domain, String vendor,String node,String geography) {
		logger.info("Going to get Other Geography Node Detail by geography:{}",domain, vendor,geography);
		List<String> networkElement = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getNodeDetailByGeography");
			query.setParameter("domain", Domain.valueOf(domain.toUpperCase()));
			query.setParameter("vendor", Vendor.valueOf(vendor.toUpperCase()));
			query.setParameter("node", NEType.valueOf(node.toUpperCase()));
			query.setParameter("geography", (geography.toUpperCase()));

			networkElement =  query.getResultList();
			logger.info("networkElement :"+networkElement.size());
		} catch (IllegalStateException | PersistenceException e) {
			logger.warn("Error while getting Other Geography Node Detail By DomainVendor ByNename,err msg {}", e.getMessage());
		}
		return networkElement;
	}
	
	@Override
	
	public List<String> getDistinctOtherGeographyDetail(String domain, String vendor) {
		logger.info("Going to get Other Geography Node Detail by domain {},vendor {} domain enum :{}",domain,
				vendor,Domain.valueOf(domain.toUpperCase()));
		List<String> otherGeographyDetail = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getDistinctOtherGeographyDetail");
			query.setParameter("domain", Domain.valueOf(domain.toUpperCase()));
			query.setParameter("vendor", Vendor.valueOf(vendor.toUpperCase()));
			otherGeographyDetail =  query.getResultList();
			logger.info("networkElement :"+otherGeographyDetail.size());
		} catch (IllegalStateException | PersistenceException e) {
			logger.warn("Error while getting Other Geography Node Detail By DomainVendor ByNename,err msg {}", e.getMessage());
		}
		return otherGeographyDetail;
	}

	@Override
	public long getNEMappingCountByNEId(Integer networkElementId, Integer otherGeographyId) {
		logger.info("***Going to get NEGeographyMapping By networkElementId: {} And otherGeographyId: {}***",networkElementId,otherGeographyId);
		long count = 0;
		try {
			Query query = getEntityManager().createNamedQuery("getNEMappingCountByNEId");
			query.setParameter("networkElementId",networkElementId);
			query.setParameter("otherGeographyId", otherGeographyId);
			count = (long) query.getSingleResult();
		logger.info("Total NEMapping Gathered : {}",count);
		}catch (Exception e) {
			logger.error("Error while getting Other Geography  Detail By networkElementId :{} otherGeographyId :{},err msg {}",networkElementId,otherGeographyId,e.getMessage());
		}
		return count;
	}

	@Override
	@Transactional
	public void createNEGeographyMapping(Integer networkelementId, Integer otherGeographyId) {
		logger.info("Going to Create NEGeographyMapping");
		try {
			Query query = getEntityManager().createNativeQuery(
					"insert into NEGeographyMapping (networkelementid_fk,othergeographyid_fk) values (?,?)");
			query.setParameter(1, networkelementId);
			query.setParameter(2, otherGeographyId);
			query.executeUpdate();
			logger.info("NEGeographyMapping Populated ..!");
		} catch (Exception e) {
			logger.error("Error occured While Creating NEGeographyMapping , Error:{}", e.getMessage());
		}
	}
}

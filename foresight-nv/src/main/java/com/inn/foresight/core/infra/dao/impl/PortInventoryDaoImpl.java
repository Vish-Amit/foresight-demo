
package com.inn.foresight.core.infra.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.IPortInventoryDao;
import com.inn.foresight.core.infra.model.PortInventory;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.Vendor;

@Repository("PortInventoryDaoImpl")
public class PortInventoryDaoImpl extends HibernateGenericDao<Integer, PortInventory>implements IPortInventoryDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(PortInventoryDaoImpl.class);

	public PortInventoryDaoImpl() {
		super(PortInventory.class);
	}

	@Override
	public List<String> getAllPorts(String neid) {
		try {
			Query query = getEntityManager().createNamedQuery("getAllPortsByRouterName");
			query.setParameter("neId", neid);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception while getting port inventory data", Utils.getStackTrace(e));
		}
		return null;
	}
	
	@Override
	public List<String> getAllActivePhysicalPorts(String domain, String vendor,String neid, Date currentDate) {
		try {
			Query query = getEntityManager().createNamedQuery("getAllActivePortsByRouterName");
			query.setParameter("neId", neid);
			query.setParameter("domain", Domain.valueOf(domain));
			query.setParameter("vendor", Vendor.valueOf(vendor.toUpperCase()));
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception while getting port inventory data", Utils.getStackTrace(e));
		}
		return null;
	}
	
	@Override
	public List<String> getActivePhysicalPortsByPortType(String domain, String vendor,String neid, String portType) {
		try {
			Query query = getEntityManager().createNamedQuery("getAllActivePortsByRouterNameAndPortType");
			query.setParameter("neId", neid);
			query.setParameter("domain", Domain.valueOf(domain));
			query.setParameter("vendor", Vendor.valueOf(vendor.toUpperCase()));
			if(portType !=null && !"".equalsIgnoreCase(portType.trim())) {
				query.setParameter("portType", portType.trim().toLowerCase() + "%");
			} else {
				query.setParameter("portType",portType);
			}
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception while getting port inventory data", Utils.getStackTrace(e));
		}
		return null;
	}
	
	@Override
	public List<Object[]> getAllActivePhysicalPortByRouters(String domain, String vendor,List<String> neNames) {
		List<Object[]> networkElement = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getAllActivePhysicalPortByRouters");
			query.setParameter("neName", neNames);
			query.setParameter("domain", Domain.valueOf(domain));
			query.setParameter("vendor", Vendor.valueOf(vendor.toUpperCase()));
			networkElement =  (List<Object []>)query.getResultList();
		} catch (Exception e) {
			logger.error("Exception while getting all port inventory data", Utils.getStackTrace(e));
		}
		return networkElement;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getPhysicalAndLogicalPortsByRouter(String neid) {
		try {
			Query query = getEntityManager().createNamedQuery("getPhysicalAndLogicalPortsByRouter");
			query.setParameter("neId", neid);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception while getting port inventory data {}", Utils.getStackTrace(e));
		}
		return Collections.emptyList();
	}
}

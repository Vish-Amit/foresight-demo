package com.inn.foresight.core.ztepower.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.ztepower.constants.ZTEPowerConstants;
import com.inn.foresight.core.ztepower.dao.IZTEPowerRegionDao;
import com.inn.foresight.core.ztepower.model.ZTEPowerNEMeteMapping;
import com.inn.foresight.core.ztepower.model.ZTEPowerRegion;
import com.inn.foresight.core.ztepower.model.ZTEPowerStationNEMapping;
import com.inn.foresight.core.ztepower.utils.ZTEPowerUtils;
import com.inn.foresight.core.ztepower.wrapper.ZTEPowerRequestWrapper;
import com.inn.foresight.core.ztepower.wrapper.ZTEPowerResultWrapper;


@Repository("ZTEPowerRegionDaoImpl")
public class ZTEPowerRegionDaoImpl extends  HibernateGenericDao<Integer, ZTEPowerRegion>  implements IZTEPowerRegionDao {
	Logger logger = LogManager.getLogger(ZTEPowerRegionDaoImpl.class);
	public ZTEPowerRegionDaoImpl() {
		super(ZTEPowerRegion.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ZTEPowerResultWrapper> getZTEPowerAreaWiseCount(ZTEPowerRequestWrapper wrapper) {
		logger.info("arg inside the method  getZTEPowerAreaWiseCount  ");
		List<ZTEPowerResultWrapper> list=Collections.emptyList();
		try {
			Query query=null;
			if(wrapper.getIsForDescripency()) {
			query =getEntityManager().createNamedQuery("getZTEPowerAreaWiseCountForDescripency");
			}else {
			query =getEntityManager().createNamedQuery("getZTEPowerAreaWiseCount");
			}
			list =(List<ZTEPowerResultWrapper>) query.getResultList();
		} catch (NoResultException e) {
			logger.error("NoResultException in getZTEPowerAreaWiseCount, err = {}", Utils.getStackTrace(e));
			return Collections.emptyList();
		} catch (Exception e) {
			logger.error("error in getZTEPowerAreaWiseCount, err = {}", Utils.getStackTrace(e));
			throw e;
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ZTEPowerResultWrapper> getZTEPowerStationWiseCount(ZTEPowerRequestWrapper wrapper) {
		logger.info("arg inside the method  getZTEPowerStationWiseCount");
		List<ZTEPowerResultWrapper> list=Collections.emptyList();
		try {
			Query query =null;
			if(wrapper.getIsForDescripency()) {
			 query = getEntityManager().createNamedQuery("getZTEPowerStationWiseCountForDescripency").setParameter("areaName", wrapper.getAreaName());
			}else {
			 query = getEntityManager().createNamedQuery("getZTEPowerStationWiseCount").setParameter("areaName", wrapper.getAreaName());
			}
			list =(List<ZTEPowerResultWrapper>) query.getResultList();
		} catch (NoResultException e) {
			logger.error("NoResultException in getZTEPowerStationWiseCount, err = {}", Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("error in getZTEPowerStationWiseCount, err = {}", Utils.getStackTrace(e));
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ZTEPowerResultWrapper> getZTEPowerDeviceWiseCount(ZTEPowerRequestWrapper wrapper) {
		logger.info("arg inside the method  getZTEPowerDeviceWiseCount ");
		try {
			Query query = getEntityManager().createNamedQuery("getZTEPowerDeviceWiseCount").setParameter("stationName", wrapper.getStationName());
			return (List<ZTEPowerResultWrapper>) query.getResultList();
		} catch (NoResultException e) {
			logger.error("NoResultException in getZTEPowerDeviceWiseCount, err = {}", Utils.getStackTrace(e));
			return Collections.emptyList();
		} catch (Exception e) {
			logger.error("error in getZTEPowerDeviceWiseCount, err = {}", Utils.getStackTrace(e));
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getZTEPowerData(ZTEPowerRequestWrapper wrapper) {
		List<ZTEPowerResultWrapper>ztePowerResultWrapperList;
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ZTEPowerResultWrapper> criteriaQuery = criteriaBuilder
				.createQuery(ZTEPowerResultWrapper.class);
		List<Predicate> predicates = new ArrayList<>();
		
		
		Root<ZTEPowerStationNEMapping> stationDevice = criteriaQuery.from(ZTEPowerStationNEMapping.class);
		Root<ZTEPowerNEMeteMapping> deviceMete = criteriaQuery.from(ZTEPowerNEMeteMapping.class);
		
		
		ZTEPowerUtils.addPredicateForZTEPowerData(wrapper,stationDevice,deviceMete,predicates);
		predicates.add(criteriaBuilder.equal(stationDevice.get("networkElement").get("id"), deviceMete.get("networkElement").get("id")));
		criteriaQuery.where(predicates.toArray(new Predicate[] {}));
		
		getConstructorStatementForZTEPowerData(criteriaBuilder, stationDevice,deviceMete, criteriaQuery);
		
		Query query = getEntityManager().createQuery(criteriaQuery);
		if (wrapper.getOffset() != null && wrapper.getLimit() != null) {
			query.setFirstResult(wrapper.getOffset());
			query.setMaxResults(wrapper.getLimit());
		}
		ztePowerResultWrapperList = (List<ZTEPowerResultWrapper>) query.getResultList();
		return ZTEPowerUtils.getResponseToReturn(ztePowerResultWrapperList,wrapper);
	}

	private void getConstructorStatementForZTEPowerData(CriteriaBuilder criteriaBuilder,
			Root<ZTEPowerStationNEMapping> stationDevice, Root<ZTEPowerNEMeteMapping> deviceMete,
			 CriteriaQuery<ZTEPowerResultWrapper> criteriaQuery) {

		criteriaQuery.select(criteriaBuilder.construct(ZTEPowerResultWrapper.class,
				stationDevice.get(ZTEPowerConstants.KEY_ZTE_POWER_STATION_TABLE).get(ZTEPowerConstants.KEY_ZTE_POWER_REGION_TABLE).get(ZTEPowerConstants.KEY_LSC_ID)
				,stationDevice.get(ZTEPowerConstants.KEY_ZTE_POWER_STATION_TABLE).get(ZTEPowerConstants.KEY_ZTE_POWER_REGION_TABLE).get(ZTEPowerConstants.KEY_LSC_NAME)
				,stationDevice.get(ZTEPowerConstants.KEY_ZTE_POWER_STATION_TABLE).get(ZTEPowerConstants.KEY_ZTE_POWER_REGION_TABLE).get(ZTEPowerConstants.KEY_AREA_ID)
				,stationDevice.get(ZTEPowerConstants.KEY_ZTE_POWER_STATION_TABLE).get(ZTEPowerConstants.KEY_ZTE_POWER_REGION_TABLE).get(ZTEPowerConstants.KEY_AREA_NAME)
				,stationDevice.get(ZTEPowerConstants.KEY_ZTE_POWER_STATION_TABLE).get(ZTEPowerConstants.KEY_STATION_ID)
				,stationDevice.get(ZTEPowerConstants.KEY_ZTE_POWER_STATION_TABLE).get(ZTEPowerConstants.KEY_STATION_NAME)
				
				,stationDevice.get(ZTEPowerConstants.KEY_NETWORK_ELEMENT_TABLE).get(ZTEPowerConstants.KEY_NE_ID)
				,stationDevice.get(ZTEPowerConstants.KEY_NETWORK_ELEMENT_TABLE).get(ZTEPowerConstants.KEY_NE_NAME)
				,stationDevice.get(ZTEPowerConstants.KEY_NETWORK_ELEMENT_TABLE).get(ZTEPowerConstants.KEY_VENDOR)
				,stationDevice.get(ZTEPowerConstants.KEY_NETWORK_ELEMENT_TABLE).get(ZTEPowerConstants.KEY_DOMAIN)
				
				,deviceMete.get(ZTEPowerConstants.KEY_ZTE_POWER_METE_INFO_TABLE).get(ZTEPowerConstants.KEY_METE_ID)
				,deviceMete.get(ZTEPowerConstants.KEY_ZTE_POWER_METE_INFO_TABLE).get(ZTEPowerConstants.KEY_METE_NAME)
				,deviceMete.get(ZTEPowerConstants.KEY_ZTE_POWER_METE_INFO_TABLE).get(ZTEPowerConstants.KEY_METE_KIND)
				
				,stationDevice.get(ZTEPowerConstants.KEY_NETWORK_ELEMENT_TABLE).get(ZTEPowerConstants.KEY_IPV4)
				));
	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> searchZTEPowerFields(ZTEPowerRequestWrapper wrapper) {
		
		logger.info("arg inside the method  searchZTEPowerFields  ");
		String dynamicQuery = null;
		try {
			if(ZTEPowerConstants.TYPE_AREA_NAME.equalsIgnoreCase(wrapper.getType())) {
			dynamicQuery = "select distinct "+wrapper.getType()+"  from ZTEPowerRegion r where r.areaName like (:areaName)";
			}else if(ZTEPowerConstants.TYPE_STATION_NAME.equalsIgnoreCase(wrapper.getType())) {
				dynamicQuery = "select distinct "+wrapper.getType()+"  from ZTEPowerStation r where r.stationName like (:stationName)";
			}else if(ZTEPowerConstants.TYPE_DEVICE_NAME.equalsIgnoreCase(wrapper.getType())) {
				dynamicQuery = "select distinct "+wrapper.getType()+"  from NetworkElement r where r.domain='POWER' and r.vendor='ZTE' and r.neName like (:neName)";
			}else if(ZTEPowerConstants.TYPE_DEVICE_ID.equalsIgnoreCase(wrapper.getType())) {
				dynamicQuery = "select distinct "+wrapper.getType()+"  from NetworkElement r where r.domain='POWER' and r.vendor='ZTE' and r.neId like (:neId)";
			}else if(ZTEPowerConstants.TYPE_METE_ID.equalsIgnoreCase(wrapper.getType())) {
				dynamicQuery = "select distinct "+wrapper.getType()+"  from ZTEPowerMeteInfo r where r.meteId like (:meteId)";
			}else if(ZTEPowerConstants.TYPE_METE_KIND.equalsIgnoreCase(wrapper.getType())) {
				dynamicQuery = "select distinct "+wrapper.getType()+"  from ZTEPowerMeteInfo r where r.meteKind like (:meteKind)";
			}else if(ZTEPowerConstants.TYPE_METE_NAME.equalsIgnoreCase(wrapper.getType())) {
				dynamicQuery = "select distinct "+wrapper.getType()+"  from ZTEPowerMeteInfo r where r.meteName like (:meteName)";
			}
			Query query = getEntityManager().createQuery(dynamicQuery);
			query.setParameter(wrapper.getType(),"%"+wrapper.getName()+"%");
			return (List<String>) query.getResultList();
		} catch (Exception e) {
			logger.error("error in searchZTEPowerFields, err = {}", Utils.getStackTrace(e));
		}
		return Collections.emptyList();
	}
}


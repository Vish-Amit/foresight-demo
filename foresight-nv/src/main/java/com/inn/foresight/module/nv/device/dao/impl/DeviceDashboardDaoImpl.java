package com.inn.foresight.module.nv.device.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.device.dao.DeviceDashboardDao;
import com.inn.foresight.module.nv.device.model.DeviceDashboard;
import com.inn.foresight.module.nv.device.wrapper.DeviceDashboardWrapper;

@Repository("DeviceDashboardDaoImpl")
public class DeviceDashboardDaoImpl extends HibernateGenericDao<Integer, DeviceDashboard> implements DeviceDashboardDao {

	private static final String DEVICE_ID = "deviceId";
	private static Logger logger=LoggerFactory.getLogger(DeviceDashboardDaoImpl.class);
	public DeviceDashboardDaoImpl() {
		super(DeviceDashboard.class);
	}

	@Override
	public DeviceDashboard persistDashboardData(DeviceDashboard dashboard) {
		if(dashboard.getId()!=null) {
		return super.update(dashboard);
		}
		else {
			return super.create(dashboard);
		}
	}

	@Override
	public DeviceDashboard findDeviceDataByDeviceId(String deviceId) {
	DeviceDashboard dashboardData=null;
	try {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<DeviceDashboard> query = criteriaBuilder.createQuery(DeviceDashboard.class);
		Root<DeviceDashboard> root = query.from(DeviceDashboard.class);
		Predicate predicate=criteriaBuilder.equal(root.get(DEVICE_ID), deviceId);
		query.where(predicate);
		dashboardData = getEntityManager().createQuery(query).getResultList().get(ForesightConstants.ZERO);
	}catch(Exception e) {
		logger.info("No Device Found {}",e.getMessage());
	}
		return dashboardData;
	}
	
	@Override
	public List<DeviceDashboard> findDeviceList(DeviceDashboardWrapper wrapper,Integer llimit,Integer ulimit){
		try{
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		
			
          CriteriaQuery<DeviceDashboard> query = criteriaBuilder.createQuery(DeviceDashboard.class);
          
         Root<DeviceDashboard> root = query.from(DeviceDashboard.class); 
         List<Predicate> predicateListForDevices = getPredicateListForDevices(root, criteriaBuilder, query, wrapper);
         query.where(predicateListForDevices.toArray(new Predicate[] {}));
         query.orderBy(criteriaBuilder.desc(root.get("modificationTime")));
         TypedQuery<DeviceDashboard> typedQuery = getEntityManager().createQuery(query);
         if(llimit!=null&&ulimit!=null) {
        	 typedQuery.setFirstResult(llimit).setMaxResults(ulimit - llimit + 1);
         }
        
         return typedQuery.getResultList();
		}
		catch(Exception e)
		{
			logger.error("Error While Getting data {}",Utils.getStackTrace(e));
		}
		return Collections.emptyList();
	}
	
	List<Predicate> getPredicateListForDevices(Root<DeviceDashboard> root ,CriteriaBuilder criteriaBuilder ,CriteriaQuery query,DeviceDashboardWrapper wrapper) {
		List<Predicate> list=new ArrayList<>();
		if(wrapper!=null) {
			
		list.add(wrapper.getMake()!=null?criteriaBuilder.like(root.get("make"), ForesightConstants.MODULUS+wrapper.getMake()+ForesightConstants.MODULUS):null);
		list.add(wrapper.getModel()!=null?criteriaBuilder.like(root.get("model"), ForesightConstants.MODULUS+wrapper.getModel()+ForesightConstants.MODULUS):null);
		list.add(CollectionUtils.isNotEmpty(wrapper.getDeviceOSList())?root.get("apkDetail").get("apkOS").in( wrapper.getDeviceOSList()):null);
		list.add(wrapper.getVersionName()!=null?criteriaBuilder.like(root.get("apkDetail").get("versionName"), ForesightConstants.MODULUS+wrapper.getVersionName()+ForesightConstants.MODULUS):null);
		list.add(CollectionUtils.isNotEmpty(wrapper.getBadgeList())?root.get("badge").in(wrapper.getBadgeList()):null);
		list.add(wrapper.getDeviceId()!=null?criteriaBuilder.like(root.get(DEVICE_ID), ForesightConstants.MODULUS+wrapper.getDeviceId()+ForesightConstants.MODULUS):null);
		list.add(wrapper.getOperator()!=null?criteriaBuilder.like(root.get("operator"), ForesightConstants.MODULUS+wrapper.getOperator()+ForesightConstants.MODULUS):null);
		}
		list.add(criteriaBuilder.isNotNull(root.get("operator")));
		list.removeIf(Objects::isNull);
		return list;
	}

	@Override
	public Long findDevicesCount(DeviceDashboardWrapper wrapper) {
		try{
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		
			
          CriteriaQuery<Tuple> query = criteriaBuilder.createQuery(Tuple.class);
         
         Root<DeviceDashboard> root = query.from(DeviceDashboard.class); 
         List<Predicate> predicateListForDevices = getPredicateListForDevices(root, criteriaBuilder, query, wrapper);
          query.multiselect(criteriaBuilder.count(root.get(DEVICE_ID)));         
         query.where(predicateListForDevices.toArray(new Predicate[] {})); 
         TypedQuery<Tuple> typedQuery = getEntityManager().createQuery(query);

         
         return (Long)typedQuery.getSingleResult().get(0);
		}
		catch(Exception e)
		{
			logger.error("Error While Getting deviceCount {}",Utils.getStackTrace(e));
		}
		return 0L;
	}

	@Override
	public List<DeviceDashboardWrapper> getTop10DeviceWithRank(String deviceId) {

		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<DeviceDashboardWrapper> query = criteriaBuilder.createQuery(DeviceDashboardWrapper.class);

		Root<DeviceDashboard> root = query.from(DeviceDashboard.class);
		query.select(criteriaBuilder.construct(DeviceDashboardWrapper.class, root.get(DEVICE_ID),root.get("consumption"),root.get("totalTest"),root.get("badge"),root.get("cellServed"),root.get("rank")));
		Predicate predicateDeviceId = criteriaBuilder.like(root.get(DEVICE_ID),deviceId);
		Predicate predicateRank = criteriaBuilder.or(predicateDeviceId,criteriaBuilder.le(root.get("rank"),10));
		query.where(predicateRank);
		
		query.orderBy(criteriaBuilder.desc(root.get("rank")));

		TypedQuery<DeviceDashboardWrapper> typedQuery = getEntityManager().createQuery(query);

		return typedQuery.getResultList();
	}



}

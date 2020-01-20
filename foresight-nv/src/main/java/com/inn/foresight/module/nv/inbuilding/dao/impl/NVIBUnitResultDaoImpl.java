package com.inn.foresight.module.nv.inbuilding.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.core.infra.constants.InBuildingConstants.BuildingType;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.inbuilding.dao.INVIBUnitResultDao;
import com.inn.foresight.module.nv.inbuilding.model.NVIBUnitResult;
import com.inn.foresight.module.nv.inbuilding.wrapper.NVIBResultWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.NVIBResultUnitWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.WalkTestSummaryWrapper;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;
import com.inn.product.systemconfiguration.dao.SystemConfigurationDao;
import com.inn.product.systemconfiguration.utils.SystemConfigurationUtils;

/**
 * The Class NVIBUnitResultDaoImpl.
 *
 * @author innoeye
 * date - 15-Mar-2018 7:46:40 PM
 */
@Repository("NVIBUnitResultDaoImpl")
public class NVIBUnitResultDaoImpl
extends HibernateGenericDao<Integer, NVIBUnitResult> implements INVIBUnitResultDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NVIBUnitResultDaoImpl.class);

	/** Construct NVIBUnitResultDaoImpl object. */
	public NVIBUnitResultDaoImpl() {
		super(NVIBUnitResult.class);
	}

	@Autowired
	SystemConfigurationDao   systemConfigurationDao; 

	/**
	 * Search NVIB buildings.
	 *
	 * @param swLat the sw lat
	 * @param swLng the sw lng
	 * @param neLat the ne lat
	 * @param neLng the ne lng
	 * @param technology the technology
	 * @param buildingType the building type
	 * @param zoomLevel the zoom level
	 * @return the list
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NVIBResultWrapper> searchNVIBBuildings(Double swLat,
			Double swLng, Double neLat, Double neLng, String technology,
			String buildingType, Integer zoomLevel) {
		logger.info("Going to searchNVIBBuildings for sWLat {},sWLong {},nELat {},nELong {},technology {},buildingType {} and zoomLevel {}",swLat,swLng,neLat,neLng,technology,buildingType,zoomLevel);
		List<NVIBResultWrapper> buildings=new ArrayList<>();
		try {
			Integer geographyL1Zoom = Integer.parseInt(systemConfigurationDao.getValueByName(InBuildingConstants.GEOGRAPHY_L1_ZOOM));
			Integer geographyL2Zoom = Integer.parseInt(systemConfigurationDao.getValueByName(InBuildingConstants.GEOGRAPHY_L2_ZOOM));
			Integer geographyL3Zoom = Integer.parseInt(systemConfigurationDao.getValueByName(InBuildingConstants.GEOGRAPHY_L3_ZOOM));
			Integer geographyL4Zoom = Integer.parseInt(systemConfigurationDao.getValueByName(InBuildingConstants.GEOGRAPHY_L4_ZOOM));
			Integer buildingZoom = Integer.parseInt(systemConfigurationDao.getValueByName(InBuildingConstants.BUILDING_ZOOM));
			Query query = null;
			if(zoomLevel >= geographyL1Zoom && zoomLevel < geographyL2Zoom){
				query = getEntityManager().createNamedQuery(InBuildingConstants.GET_BUILDING_COUNT_GROUP_BY_GEOL1);
			} else if(zoomLevel >= geographyL2Zoom && zoomLevel < geographyL3Zoom){
				query = getEntityManager().createNamedQuery(InBuildingConstants.GET_BUILDING_COUNT_GROUP_BY_GEOL2);
			} else if(zoomLevel >= geographyL3Zoom && zoomLevel < geographyL4Zoom){
				query = getEntityManager().createNamedQuery(InBuildingConstants.GET_BUILDING_COUNT_GROUP_BY_GEOL3);
			} else if(zoomLevel >= geographyL4Zoom && zoomLevel < buildingZoom) {
				query = getEntityManager().createNamedQuery(InBuildingConstants.GET_BUILDING_COUNT_GROUP_BY_GEOL4);
			} else if(zoomLevel >= buildingZoom) {
				query = getEntityManager().createNamedQuery(InBuildingConstants.GET_BUILDING_BY_VIEWPORT);
			} else {
				return buildings;
			}
			query.setParameter(InBuildingConstants.NORTH_EAST_LAT, neLat);
			query.setParameter(InBuildingConstants.NORTH_EAST_LONG, neLng);
			query.setParameter(InBuildingConstants.SOUTH_WEST_LAT, swLat);
			query.setParameter(InBuildingConstants.SOUTH_WEST_LONG, swLng);
			query.setParameter(InBuildingConstants.BUILDING_TYPE, BuildingType.valueOf(buildingType));
			query.setParameter(InBuildingConstants.TECHNOLOGY, technology);
			buildings = query.getResultList();
			logger.info("result list size : {}",buildings.size());
		} catch(Exception ex){
			logger.info("Error occur while searchNVIBBuildings,err msg {}",ExceptionUtils.getStackTrace(ex));
		}
		logger.info("Returning result from searchNVIBBuildings with size {}",buildings.size());
		return buildings;
	}

	/**
	 * Gets the wings.
	 *
	 * @param buildingId the building id
	 * @param technology the technology
	 * @return the wings
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NVIBResultWrapper> getWings(Integer buildingId,
			String technology) {
		logger.info("Going to getWings for buildingId {} and technology {} ",buildingId,technology);
		List<NVIBResultWrapper> wings=new ArrayList<>();
		try{
			Query query = getEntityManager().createNamedQuery(InBuildingConstants.GET_WING_BY_BUILDING)
					.setParameter(InBuildingConstants.BUILDING_ID, buildingId)
					.setParameter(InBuildingConstants.TECHNOLOGY, technology);
			wings = query.getResultList();
		} catch (Exception e) {
			logger.error("Error in getWings : {} " ,ExceptionUtils.getStackTrace(e));
			throw new DaoException(
					e.getMessage());
		}
		logger.info("Returning result from getWings with size {}",wings.size());
		return wings;
	}

	/**
	 * Gets the floors.
	 *
	 * @param wingId the wing id
	 * @param technology the technology
	 * @return the floors
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NVIBResultWrapper> getFloors(Integer wingId,
			String technology) {
		logger.info("Going to getFloors for floorId {} and technology {} ",wingId,technology);
		List<NVIBResultWrapper> floors=new ArrayList<>();
		try{
			Query query = getEntityManager().createNamedQuery(InBuildingConstants.GET_FLOOR_BY_WING)
					.setParameter(InBuildingConstants.WING_ID, wingId)
					.setParameter(InBuildingConstants.TECHNOLOGY, technology);
			floors = query.getResultList();
		} catch (Exception e) {
			logger.error("Error in getFloors : {} " ,ExceptionUtils.getStackTrace(e));
			throw new DaoException(
					e.getMessage());
		}
		logger.info("Returning result from getFloors with size {}",floors.size());
		return floors;
	}
	
	/**
	 * Gets the units.
	 *
	 * @param floorId the floor id
	 * @param technology the technology
	 * @return the units
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NVIBResultWrapper> getUnits(Integer floorId,
			String technology) {
		logger.info("Going to getUnits for floorId {} and technology {} ",floorId,technology);
		List<NVIBResultWrapper> units=new ArrayList<>();
		try{
			Query query = getEntityManager().createNamedQuery(InBuildingConstants.GET_UNIT_BY_FLOOR)
					.setParameter(InBuildingConstants.FLOOR_ID, floorId)
					.setParameter(InBuildingConstants.TECHNOLOGY, technology);
			units = query.getResultList();
		} catch (Exception e) {
			logger.error("Error in getUnits : {} " ,ExceptionUtils.getStackTrace(e));
			throw new DaoException(
					e.getMessage());
		}
		logger.info("Returning result from getUnits with size {}",units.size());
		return units;
	}
	
	
	/**
	 * Gets the recipe file name.
	 *
	 * @param unitId the unit id
	 * @param technology the technology
	 * @return the recipe file name
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NVIBResultWrapper> getRecipeFileName(Integer unitId,
			String technology) {
		logger.info("Going to getRecipeFileName for unitId {} and technology {} ",unitId,technology);
		List<NVIBResultWrapper> units=new ArrayList<>();
		try{
			Query query = getEntityManager().createNamedQuery(InBuildingConstants.GET_RECIPE_FILE_NAME_BY_UNIT)
					.setParameter(InBuildingConstants.UNIT_ID, unitId)
					.setParameter(InBuildingConstants.TECHNOLOGY, technology);
			units = query.getResultList();
		} catch (Exception e) {
			logger.error("Error in getRecipeFileName : {} " ,ExceptionUtils.getStackTrace(e));
			throw new DaoException(
					e.getMessage());
		}
		logger.info("Returning result from getRecipeFileName with size {}",units.size());
		return units;
	}


	@Override
	public void insertUnitResult(NVIBUnitResult nvResult) {
				try {
					update(nvResult);
				} catch (DaoException e) {
				    logger.error("Error inside the method insertUnitResult {}",e.getMessage());
				}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WalkTestSummaryWrapper> getIBUnitResultForRecipe(List<Integer> woRecipeMappingId) {
		List<WalkTestSummaryWrapper> walkTestSummaryWrapper = new ArrayList<>();
		try {
			walkTestSummaryWrapper = getEntityManager().createNamedQuery(InBuildingConstants.GET_IB_UNIT_RESULT_FOR_RECIPE)
					.setParameter(InBuildingConstants.KEY_WO_RECIPE_MAPPING_ID, woRecipeMappingId).getResultList();
		} catch (Exception e) {
			logger.error("Error in retrieving Unit result {}",Utils.getStackTrace(e));
			throw new RestException(e.getMessage());
		} 
		return walkTestSummaryWrapper;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NVIBUnitResult> getListOfWorkOrderIdByFloorIdAndTemplateType(Integer buildingId, Integer floorId,
			List<TemplateType> templateList) {
		try {
		return getEntityManager().createNamedQuery("getRecordByFloorIdAndTemplateType").setParameter("buildingId", buildingId)
				.setParameter("floorId", floorId).setParameter("templateType", templateList).getResultList();
		} catch (Exception e) {
			logger.error("Error in retrieving getListOfWorkOrderIdByFloorIdAndTemplateType result {}",Utils.getStackTrace(e));
			throw new RestException(e.getMessage());
		} 
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GenericWorkorder> getUnitWiseWorkorder(Integer unitId,String technology)  {
		try {
			List<GenericWorkorder> workorderList= null;
			Query query = getEntityManager().createNamedQuery("getUnitWiseWorkorder");
			query.setParameter(InBuildingConstants.UNIT_ID, unitId);
			query.setParameter(NVConstant.KEY_TECHNOLOGY, technology);
			workorderList = query.getResultList();
			if(workorderList != null && !workorderList.isEmpty()) {
				logger.info("total: {} work order found for unitId: {} ",workorderList.size(),unitId);
				return workorderList;
			}else {
				logger.info("Workorders not found for unitId: {}",unitId);
				throw new DaoException(ForesightConstants.EXCEPTION_NO_RECORD_FOUND);
			}
		
		}catch(Exception e){
			logger.info("Error while fetching unit wise workorders: {}", e.getMessage());
			throw e;
		}
	}

	@Override
	public Long getUnitWiseWorkorderCount(Integer unitId,String technology)  {
		try {
			Query query = getEntityManager().createNamedQuery("getUnitWiseWorkorderCount");
			query.setParameter(InBuildingConstants.UNIT_ID, unitId);
			query.setParameter(NVConstant.KEY_TECHNOLOGY, technology);
			Long workorderCount = (Long) query.getSingleResult();
			if(workorderCount != null) {
				logger.info("total: {} work order found for unitId: {} ",workorderCount,unitId);
				return workorderCount;
			}else {
				logger.info("Workorders not found for unitId: {}",unitId);
				throw new DaoException(ForesightConstants.EXCEPTION_NO_RECORD_FOUND);
			}
		}catch(Exception e){
			logger.info("Error while fetching unit wise workorder counts: {}", e.getMessage());
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<NVIBResultUnitWrapper> getWingLevelWiseIBUnitResult(Integer id) {
		logger.info("Going to get NV IB Unit Result Data for Id: {}", id);
		try {
			Query query= getEntityManager().createQuery("Select new com.inn.foresight.module.nv.report.wrapper.inbuilding.NVIBResultUnitWrapper(n.unit.id,n.unit.unitName,avg(n.totalRsrp),avg(n.totalRssi),avg(n.totalSinr),avg(n.totalDl),avg(n.totalUl),n.operator,sum(n.handOverInitiate), " + 
					"	sum(n.handOverSuccess),n.unit.floor.wing.building.latitude,n.unit.floor.wing.building.longitude,n.unit,n.unit.floor,n.unit.floor.wing) " + 
					"	 from NVIBUnitResult n " + 
					"	 where n.unit.floor.wing.building.id=:id " + 
					"	 group by n.unit.id,n.unit.unitName,n.unit.floor.wing.building.latitude,n.unit.floor.wing.building.longitude,n.unit,n.unit.floor,n.unit.floor.wing");
		query.setParameter("id",id);
			return query.getResultList();
		} catch (Exception exception) {
			logger.error("Unable to get NV IB Unit result data for Id: {}; Exception {} ", id, Utils.getStackTrace(exception));
			throw new DaoException(exception.getMessage());
		}

	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<NVIBResultUnitWrapper> getWingLevelWiseIBUnitResult(Integer id,String type,String technology) {
		logger.info("Going to get NV IB Unit Result Data for type  {} and  id {} ", type, id);
		try {
			CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<NVIBResultUnitWrapper> criteriaQuery = builder.createQuery(NVIBResultUnitWrapper.class);
			Root<NVIBUnitResult> rootElements = criteriaQuery.from(NVIBUnitResult.class);
			criteriaQuery = getConstructorStatement(builder, rootElements, criteriaQuery);
			List<Predicate> finalPredicates = getPredicate(builder, rootElements,id,type,technology);
			criteriaQuery = addPredicateInCreateriaQuery(criteriaQuery, finalPredicates);
			criteriaQuery = getAggregationInCreateriaQuery(rootElements, criteriaQuery);
			Query query = getEntityManager().createQuery(criteriaQuery);
			return query.getResultList();
		} catch (Exception exception) {
			logger.error("Unable to get NV IB Unit result data for Type: {} ; Id: {} ; Exception {} ", type, id, Utils.getStackTrace(exception));
			throw new DaoException(exception.getMessage());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private CriteriaQuery<NVIBResultUnitWrapper> getConstructorStatement(CriteriaBuilder builder,
			Root ibUnit, CriteriaQuery<NVIBResultUnitWrapper> criteriaQuery) {
		try {
			criteriaQuery.select(builder.construct(NVIBResultUnitWrapper.class,
					ibUnit.get("unit").get("id"),
					ibUnit.get("unit").get("unitName"),
					builder.sum(ibUnit.<Double>get("totalRsrp")),
					builder.sum(ibUnit.<Double>get("totalRssi")),
					builder.sum(ibUnit.<Double>get("totalSinr")),
					builder.sum(ibUnit.<Double>get("totalDl")),
					builder.sum(ibUnit.<Double>get("totalUl")),
					builder.sum(ibUnit.get("countRsrp")),
					builder.sum(ibUnit.get("countRssi")),
					builder.sum(ibUnit.get("countSinr")),
					builder.sum(ibUnit.get("countDl")),
					builder.sum(ibUnit.get("countUl")),
					ibUnit.get("operator"),
					builder.sum(ibUnit.<Long>get("handOverInitiate")),
					builder.sum(ibUnit.<Long>get("handOverSuccess")),
					ibUnit.get(InBuildingConstants.WO_RECIPE_MAPPING).get(InBuildingConstants.GENERIC_WORKORDER).get("id"),
					ibUnit.get("unit").get(InBuildingConstants.FLOOR).get("wing").get(InBuildingConstants.CRITERIA_BUILDING).get("latitude"),
					ibUnit.get("unit").get(InBuildingConstants.FLOOR).get("wing").get(InBuildingConstants.CRITERIA_BUILDING).get("longitude"),
					ibUnit.get("unit").get(InBuildingConstants.FLOOR),
					ibUnit.get("unit").get(InBuildingConstants.FLOOR).get("wing"),
					
					
					builder.sum(ibUnit.<Long>get("callInitiateCount")),
					builder.sum(ibUnit.<Long>get("callSuccessCount")),
					builder.sum(ibUnit.<Long>get("callFailureCount")),
					builder.sum(ibUnit.<Long>get("callDropCount")),
					builder.sum(ibUnit.<Long>get("callSetupSuccessCount")),
					builder.sum(ibUnit.<Double>get("totalPdschDl")),
					builder.sum(ibUnit.get("countPdschDl")),
					builder.sum(ibUnit.<Double>get("totalPuschUl")),
					builder.sum(ibUnit.get("countPuschUl")),
					builder.sum(ibUnit.<Double>get("totalMos")),
					builder.sum(ibUnit.get("countMos")))
			
					);
		} catch (Exception exception) {
			logger.error("Error in creating constructor for IB Unit Result {}", Utils.getStackTrace(exception));
		}
		return criteriaQuery;
	}
	
	@SuppressWarnings("rawtypes")
	private List<Predicate> getPredicate(CriteriaBuilder builder, Root networkRoot, Integer id,String type,String technology) {
		List<Predicate> predicateList = new ArrayList<>();
		try {
			if(type.equalsIgnoreCase(InBuildingConstants.CRITERIA_BUILDING)) {
			predicateList.add(builder.equal(networkRoot.get("unit").get(InBuildingConstants.FLOOR).get("wing").get(InBuildingConstants.CRITERIA_BUILDING).get("id"),id));
			predicateList.add(builder.equal(networkRoot.get("technology"),technology));
			}
			else {
			predicateList.add(builder.equal(networkRoot.get(InBuildingConstants.WO_RECIPE_MAPPING).get(InBuildingConstants.GENERIC_WORKORDER).get("id"),id));
			}
			predicateList.add(builder.equal(networkRoot.get(InBuildingConstants.WO_RECIPE_MAPPING).get("status"), WORecipeMapping.Status.COMPLETED));
//			predicateList.add(builder.equal(networkRoot.get(InBuildingConstants.WO_RECIPE_MAPPING).get(InBuildingConstants.GENERIC_WORKORDER).get("status"), GenericWorkorder.Status.COMPLETED));

		} catch (Exception exception) {
			logger.error("Unable to create predicate for Actual Site visualisation {}", Utils.getStackTrace(exception));
		}
		return predicateList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public NVIBResultWrapper getBuildingInFoByWoId(Integer woId) {
		logger.info("Going to getRecipeFileName for woId {} ",woId);
		List<NVIBResultWrapper> buildingInfo=null;
		try {
			Query query = getEntityManager().createNamedQuery("getBuildingInFoByWoId").setParameter("woId", woId);
			buildingInfo = query.getResultList();
			return buildingInfo.get(0);

		} catch (Exception e) {
			logger.error("Error in getRecipeFileName : {} ", ExceptionUtils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public NVIBUnitResult getNVIBUnitResultByWoRecipeId(Integer woRecipeMappingId) {
		logger.info("Going to getNVIBUnitResultByWoRecipeId for woRecipeMappingId {} ",woRecipeMappingId);
		List<NVIBUnitResult> nvIbUnitResultInfo=new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getNVIbResultByRecipeId").setParameter("woRecipeMappingId", woRecipeMappingId);
			nvIbUnitResultInfo = query.getResultList();
			return nvIbUnitResultInfo!=null && !nvIbUnitResultInfo.isEmpty()?nvIbUnitResultInfo.get(0):null;
		}catch(NoResultException nre) {
			logger.error("NoResultException occured inside method getNVIBUnitResultByWoRecipeId {} ",nre.getMessage());
			return null;
		}catch (Exception e) {
			logger.error("Error in getNVIBUnitResultByWoRecipeId : {} ",Utils.getStackTrace(e));
			return null;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static CriteriaQuery<NVIBResultUnitWrapper> getAggregationInCreateriaQuery(Root ibUnit, CriteriaQuery<NVIBResultUnitWrapper> criteriaQuery) {
		criteriaQuery.groupBy(ibUnit.get("unit").get("id"),
					ibUnit.get("unit").get("unitName"),
					ibUnit.get("operator"),
					ibUnit.get("woRecipeMapping").get("genericWorkorder").get("id"),
					ibUnit.get("unit").get(InBuildingConstants.FLOOR).get("wing").get("building").get("latitude"),
					ibUnit.get("unit").get(InBuildingConstants.FLOOR).get("wing").get("building").get("longitude"),
					ibUnit.get("unit").get(InBuildingConstants.FLOOR),
					ibUnit.get("unit").get(InBuildingConstants.FLOOR).get("wing"));
		return criteriaQuery;
	}
	
	public static CriteriaQuery<NVIBResultUnitWrapper> addPredicateInCreateriaQuery(CriteriaQuery<NVIBResultUnitWrapper> criteriaQuery, List<Predicate> predicates) {
		criteriaQuery.where(predicates.toArray(new Predicate[] {}));
		return criteriaQuery;
	}
	
	@Override
	public List<NVIBResultWrapper> getNVIbResultByBuildingId(Integer buildingId, Integer floorId, Date startTime, Date endTime) {
		logger.info("Going to getNVIbResultByBuildingId for buildingId {} ",buildingId);
		try {
			Query query = getEntityManager().createNamedQuery("getNVIbResultByBuildingId").setParameter("buildingId", buildingId).setParameter("floorId", floorId);
			if (startTime != null && endTime != null) {
				enableDateRangeFilter(startTime, endTime);
			}
			return query.getResultList();

		} 
		catch(NoResultException ne) {
			logger.error("no entity found for  buildingId=== {}",buildingId);
			throw ne ;
		}
		catch (Exception e) {
			logger.error("Error in getNVIbResultByBuildingId == : {} ", ExceptionUtils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
	}

	@Override
	public NVIBResultWrapper getKpiAvgByFloorId(Integer floorId, Integer band, Date startTime, Date endTime) {
		logger.info("Going to getKpiAvgByFloorId for floorId =:  {} band ={} startTime =: {} endTime =: {}", floorId,
				band, startTime, endTime);
		try {
			Query query = getEntityManager().createNamedQuery("getKpiAvgByFloorId").setParameter("floorId", floorId);
			if (startTime != null && endTime != null) {
				enableDateRangeFilter(startTime, endTime);
			}
			if (band != null) {
				enableBandFilter(band);
			}
			NVIBResultWrapper resultWrapper = (NVIBResultWrapper) query.getSingleResult();
			if (startTime != null && endTime != null) {
				disableFilter("dateRangeFilter");
			}
			if (band != null) {
				disableFilter("bandFilter");
			}
			return resultWrapper;

		} catch (NoResultException ne) {
			logger.error("no entity found for  buildingId {}", floorId);
			 return null;
		} catch (Exception e) {
			logger.error("Error in getNVIbResultByBuildingId : {} ", ExceptionUtils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
	}
	
	 
	private void enableDateRangeFilter(Date startDate,Date endDate) {
		Session s = (Session) getEntityManager().getDelegate();
		s.enableFilter("dateRangeFilter").setParameter("startDate", startDate).setParameter("endDate", endDate);
	}
	private void enableBandFilter(Integer band) {
		Session s = (Session) getEntityManager().getDelegate();
		s.enableFilter("bandFilter").setParameter("band", band);
	}
	private void disableFilter(String filterName) {
		Session s = (Session) getEntityManager().getDelegate();
		s.disableFilter(filterName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NVIBResultWrapper> getDateWiseTestResultByBuildingId(Integer buildingId, Integer floorId) {
		try {
		Query query = getEntityManager().createNamedQuery("getDateWiseTestResultByBuildingId").setParameter("buildingId", buildingId).setParameter("floorId", floorId);
		 return query.getResultList();
		} 
		catch (NoResultException ne) {
			logger.error("no entity found for  buildingId {}", floorId);
			 return null;
		} 
		catch (Exception e) {
			logger.error("Error in getNVIbResultByBuildingId : {} ", ExceptionUtils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}}
}

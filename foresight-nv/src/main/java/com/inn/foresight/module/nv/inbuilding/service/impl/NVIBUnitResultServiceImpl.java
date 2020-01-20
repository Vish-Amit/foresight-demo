package com.inn.foresight.module.nv.inbuilding.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.core.infra.dao.IUnitDataDao;
import com.inn.foresight.core.infra.model.Floor;
import com.inn.foresight.core.infra.model.Unit;
import com.inn.foresight.core.infra.model.Wing;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.inbuilding.dao.INVIBUnitResultDao;
import com.inn.foresight.module.nv.inbuilding.model.NVIBUnitResult;
import com.inn.foresight.module.nv.inbuilding.service.INVIBUnitResultService;
import com.inn.foresight.module.nv.inbuilding.wrapper.NVIBResultWrapper;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.Layer3SummaryWrapper;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.NVIBResultUnitWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.WalkTestSummaryWrapper;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;

/**
 * The Class NVIBUnitResultServiceImpl.
 *
 * @author innoeye
 * date - 15-Mar-2018 7:50:38 PM
 */
@Service("NVIBUnitResultServiceImpl")
public class NVIBUnitResultServiceImpl
		extends AbstractService<Integer, NVIBUnitResult>
		implements INVIBUnitResultService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NVIBUnitResultServiceImpl.class);
	
	/** The nvib unit result dao. */
	@Autowired
	INVIBUnitResultDao nvibUnitResultDao;

	@Autowired
	private IUnitDataDao unitDataDao;
	/**
	 * Sets the dao.
	 *
	 * @param nvibUnitResultDao the new dao
	 */
	@Autowired
	public void setDao(INVIBUnitResultDao nvibUnitResultDao) {
		this.nvibUnitResultDao = nvibUnitResultDao;
		super.setDao(nvibUnitResultDao);
	}

	/**
	 * Search NVIB buildings.
	 *
	 * @param sWLat the s W lat
	 * @param sWLong the s W long
	 * @param nELat the n E lat
	 * @param nELong the n E long
	 * @param technology the technology
	 * @param buildingType the building type
	 * @param zoomLevel the zoom level
	 * @return the list
	 * @throws RestException the rest exception
	 */
	@Override
	@Transactional
	public List<NVIBResultWrapper> searchNVIBBuildings(Double sWLat,
			Double sWLong, Double nELat, Double nELong, String technology,
			String buildingType, Integer zoomLevel) {
		logger.info(
				"Going to searchNVIBBuildings for sWLat {},sWLong {},nELat {},nELong {},technology {},buildingType {} and zoomLevel {}",
				sWLat, sWLong, nELat, nELong, technology, buildingType,
				zoomLevel);
		try {
			List<NVIBResultWrapper> buildings = nvibUnitResultDao.searchNVIBBuildings(
					sWLat, sWLong, nELat, nELong, technology, buildingType,
					zoomLevel);
			logger.info(
					"Returning result from searchNVIBBuildings with size {}",buildings.size());
			return buildings;
		} catch (Exception e) {
			logger.error("Exception in searchNVIBBuildings : {} ",ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}
	
	/**
	 * Gets the wings.
	 *
	 * @param buildingId the building id
	 * @param technology the technology
	 * @return the wings
	 * @throws RestException the rest exception
	 */
	@Override
	public List<NVIBResultWrapper> getWings(Integer buildingId, String technology) {
		logger.info("Going to getWings for buildingId {} and technology {} ",buildingId,technology);
		try{
			List<NVIBResultWrapper> wings = nvibUnitResultDao.getWings(buildingId,technology);
			logger.info("Returning result from getWings with size {}",wings.size());
			return wings;
		} catch (Exception e) {
			logger.error("Exception in getWings : {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}
	
	
	/**
	 * Gets the floors.
	 *
	 * @param buildingId the building id
	 * @param technology the technology
	 * @return the floors
	 * @throws RestException the rest exception
	 */
	@Override
	public List<NVIBResultWrapper> getFloors(Integer buildingId, String technology) {
		logger.info("Going to getFloors for floorId {} and technology {} ",buildingId,technology);
		try{
			List<NVIBResultWrapper> floors = nvibUnitResultDao.getFloors(buildingId,technology);
			logger.info("Returning result from getFloors with size {}",floors.size());
			return floors;
		} catch (Exception e) {
			logger.error("Exception in getFloors : {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Gets the units.
	 *
	 * @param floorId the floor id
	 * @param technology the technology
	 * @return the units
	 * @throws RestException the rest exception
	 */
	@Override
	public List<NVIBResultWrapper> getUnits(Integer floorId,
			String technology) {
		logger.info("Going to getUnits for floorId {} and technology {} ",floorId,technology);
		try{
			List<NVIBResultWrapper> units =nvibUnitResultDao.getUnits(floorId,technology);
			logger.info("Returning result from getUnits with size {}",units.size());
			return units;
		} catch (Exception e) {
			logger.error("Exception in getUnits : {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}
	
	
	/**
	 * Gets the recipe file name.
	 *
	 * @param unitId the unit id
	 * @param technology the technology
	 * @return the recipe file name
	 * @throws RestException the rest exception
	 */
	@Override
	public List<NVIBResultWrapper> getRecipeFileName(Integer unitId,
			String technology) {
		logger.info("Going to getRecipeFileName for unitId {} and technology {} ",unitId,technology);
		try{
			List<NVIBResultWrapper> recipeFile =nvibUnitResultDao.getRecipeFileName(unitId,technology);
			logger.info("Returning result from getRecipeFileName with size {}",recipeFile.size());
			return recipeFile;
		} catch (Exception e) {
			logger.error("Exception in getRecipeFileName : {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateRecordIntoInbuilding(WORecipeMapping woRecipeMapping, Layer3SummaryWrapper aggrigateWrapperData,String assignTo) {
		try {
			NVIBUnitResult nvResult = nvibUnitResultDao.getNVIBUnitResultByWoRecipeId(woRecipeMapping.getId());
			if(nvResult!=null && nvResult.getWoRecipeMapping()!=null) {
				nvResult = setValuesIntoNvResultFromAggrigatedData(aggrigateWrapperData,nvResult);
			}else {
				nvResult = setValuesIntoNvResultFromAggrigatedData(aggrigateWrapperData,new NVIBUnitResult());
			}
			if (nvResult != null) {
				nvResult.setWoRecipeMapping(woRecipeMapping);
				nvResult.setUnit(getUnitFromGWOMap(woRecipeMapping));
				nvResult.setCreationTime(new Date());
				nvResult.setModificationTime(new Date());
				nvResult.setAssignto(assignTo);
				nvibUnitResultDao.update(nvResult);
			}

			logger.info("Successfullly Inserted ==>");
		} catch (Exception e) {
			logger.error("Exception inside method updateRecordIntoInbuilding {} ",Utils.getStackTrace(e));
		}
	}

	private NVIBUnitResult setValuesIntoNvResultFromAggrigatedData(
			Layer3SummaryWrapper aggrigateWrapperData,NVIBUnitResult nvResult) {
		try {
			setDeviceAndDriveInfoIntoWrapper(nvResult, aggrigateWrapperData);
			setKpiValuesIntoInBuidlingWrapper(nvResult, aggrigateWrapperData);
			nvResult.setCountMimo(aggrigateWrapperData.getMimoCount());
			nvResult.setTotalMimo(aggrigateWrapperData.getMimoTotalCount());
			setEventValuesIntoWrapper(nvResult, aggrigateWrapperData);
			setKpivalueCount(nvResult, aggrigateWrapperData);
			setWifiParamIntoNvResult(nvResult, aggrigateWrapperData);
			return nvResult;
		} catch (Exception e) {
			logger.error("Exception inside method setValuesIntoNvResultFromAggrigatedData {} ",Utils.getStackTrace(e));
			return null;
		}
	}

	private void setWifiParamIntoNvResult(NVIBUnitResult nvResult, Layer3SummaryWrapper aggrigateWrapperData) {
		nvResult.setCntrssigrtthn90dbm(aggrigateWrapperData.getCntrssigrtthn90dbm());
		nvResult.setCntsnrgrtthn25dbm(aggrigateWrapperData.getCntsnrgrtthn25dbm());
		setWifiRssiValues(nvResult, aggrigateWrapperData);
		setWifiSnrValues(nvResult, aggrigateWrapperData);
		nvResult.setWifiSSID(NVLayer3Utils.getStringFromSetValues(aggrigateWrapperData.getSsid()));
	}

	private void setWifiSnrValues(NVIBUnitResult nvResult, Layer3SummaryWrapper aggrigateWrapperData) {
		if (aggrigateWrapperData.getWifiSnr() != null) {
			nvResult.setTotalWifiSnr(aggrigateWrapperData.getWifiSnr()[QMDLConstant.VALUE_INDEX]);
			nvResult.setCountWifiSnr(aggrigateWrapperData.getWifiSnr()[QMDLConstant.COUNT_INDEX].intValue());
		}
	}

	private void setWifiRssiValues(NVIBUnitResult nvResult, Layer3SummaryWrapper aggrigateWrapperData) {
		if (aggrigateWrapperData.getWifiRssi() != null) {
			nvResult.setTotalWifiRssi(aggrigateWrapperData.getWifiRssi()[QMDLConstant.VALUE_INDEX]);
			nvResult.setCountWifiRssi(aggrigateWrapperData.getWifiRssi()[QMDLConstant.COUNT_INDEX].intValue());
		}
	}

	private void setKpivalueCount(NVIBUnitResult nvResult, Layer3SummaryWrapper aggrigateWrapperData) {
		nvResult.setCountRsrpGreaterThan100Dbm(aggrigateWrapperData.getCountRsrpGreaterThan100Dbm());
		nvResult.setCountDlGreaterThan5Mbps(aggrigateWrapperData.getCountDlGreaterThan5Mbps());
		nvResult.setCountSinrGreaterThan12Db(aggrigateWrapperData.getCountSinrGreaterThan12Db());
	}

	private void setDeviceAndDriveInfoIntoWrapper(NVIBUnitResult nvResult, Layer3SummaryWrapper aggrigateWrapperData) {
		nvResult.setCellId(NVLayer3Utils.getStringFromIntegerSetValues(aggrigateWrapperData.getCellidSet()));
		nvResult.setTechnology(NVLayer3Utils.getStringFromSetValues(aggrigateWrapperData.getOperator()));			

		if(nvResult.getTechnology()!=null && nvResult.getTechnology().equalsIgnoreCase(QMDLConstant.WIFI)) {
			nvResult.setOperator(NVLayer3Utils.getStringFromSetValues(aggrigateWrapperData.getOperator()));
		}else if(aggrigateWrapperData.getOperatorName()!=null) {
			nvResult.setOperator(aggrigateWrapperData.getOperatorName().toUpperCase());
		}
		nvResult.setCountTDD(aggrigateWrapperData.getTddCount());
		nvResult.setCountFDD(aggrigateWrapperData.getFddCount());
		nvResult.setPci(getUniquePciValuesFromMap(aggrigateWrapperData.getPciMap()));
		setPciStrongest(nvResult,aggrigateWrapperData.getPciMap());
		setCellStrongest(nvResult, aggrigateWrapperData.getCellIdMap());
		setEnodbIdAndSectorIdFromCellId(nvResult,aggrigateWrapperData);
		setBandFromSetValue(nvResult,aggrigateWrapperData);
		
		
	}

	private void setBandFromSetValue(NVIBUnitResult nvResult, Layer3SummaryWrapper aggrigateWrapperData) {
		if (aggrigateWrapperData.getBandSet() != null && !aggrigateWrapperData.getBandSet().isEmpty()) {
			nvResult.setBand(NVLayer3Utils.getFrequencyFromBand(Integer.valueOf(NVLayer3Utils.getValidValueFromSet(aggrigateWrapperData.getBandSet()))));
		}
	}

	private void setEventValuesIntoWrapper(NVIBUnitResult nvResult, Layer3SummaryWrapper aggrigateWrapperData) {
		nvResult.setHandOverInitiate(aggrigateWrapperData.getNewHandOverIntiateCount());
		nvResult.setHandOverSuccess(aggrigateWrapperData.getNewHandOverSuccessCount());
		nvResult.setErabDrop(aggrigateWrapperData.getErabDrop());
		nvResult.setErabSuccess(aggrigateWrapperData.getTotalErab());
		nvResult.setRrcInitiate(aggrigateWrapperData.getRrcInitiate());
		nvResult.setRrcSuccess(aggrigateWrapperData.getRrcSucess());
		nvResult.setCallInitiateCount(aggrigateWrapperData.getCallInitiateCount());
		nvResult.setCallSetupSuccessCount(aggrigateWrapperData.getCallSetupSuccessCount());
		nvResult.setCallDropCount(aggrigateWrapperData.getCallDropCount());
		nvResult.setCallFailureCount(aggrigateWrapperData.getCallFailureCount());
		nvResult.setCallSuccessCount(aggrigateWrapperData.getCallSuccessCount());		
	}

	private void setKpiValuesIntoInBuidlingWrapper(NVIBUnitResult nvResult, Layer3SummaryWrapper aggrigateWrapperData) {
		setDlKpiValue(nvResult, aggrigateWrapperData);
		setUlKpiValue(nvResult, aggrigateWrapperData);
		setSinrKpiValue(nvResult, aggrigateWrapperData);
		setRssiKpiValue(nvResult, aggrigateWrapperData);
		setRsrpKpiValue(nvResult, aggrigateWrapperData);
		setCqiKpiValue(nvResult, aggrigateWrapperData);
		setmosKpiValue(nvResult, aggrigateWrapperData);
		setPdschDlKpiValue(nvResult, aggrigateWrapperData);
		setPuschUlKpiValue(nvResult, aggrigateWrapperData);
	}

	private void setPuschUlKpiValue(NVIBUnitResult nvResult, Layer3SummaryWrapper aggrigateWrapperData) {
		if (aggrigateWrapperData.getPuschThroughput()!=null) {
			nvResult.setCountPuschUl(aggrigateWrapperData.getPuschThroughput()[QMDLConstant.COUNT_INDEX].intValue());
			nvResult.setTotalPuschUl(aggrigateWrapperData.getPuschThroughput()[QMDLConstant.VALUE_INDEX]);
		}

	}

	private void setPdschDlKpiValue(NVIBUnitResult nvResult, Layer3SummaryWrapper aggrigateWrapperData) {
		if (aggrigateWrapperData.getPDSCHThroughput()!=null) {
			nvResult.setCountPdschDl(aggrigateWrapperData.getPDSCHThroughput()[QMDLConstant.COUNT_INDEX].intValue());
			nvResult.setTotalPdschDl(aggrigateWrapperData.getPDSCHThroughput()[QMDLConstant.VALUE_INDEX]);
		}		
	}

	private void setmosKpiValue(NVIBUnitResult nvResult, Layer3SummaryWrapper aggrigateWrapperData) {
		if (aggrigateWrapperData.getInstantaneousMos()!=null) {
			nvResult.setCountMos(aggrigateWrapperData.getInstantaneousMos()[QMDLConstant.COUNT_INDEX].intValue());
			nvResult.setTotalMos(aggrigateWrapperData.getInstantaneousMos()[QMDLConstant.VALUE_INDEX]);
		}
	}

	private void setCqiKpiValue(NVIBUnitResult nvResult, Layer3SummaryWrapper aggrigateWrapperData) {
		if (aggrigateWrapperData.getCqiCwo()!= null) {
			nvResult.setCountCqi(aggrigateWrapperData.getCqiCwo()[QMDLConstant.COUNT_INDEX].intValue());
			nvResult.setTotalCqi(aggrigateWrapperData.getCqiCwo()[QMDLConstant.VALUE_INDEX]);
		}
	}

	private void setRsrpKpiValue(NVIBUnitResult nvResult, Layer3SummaryWrapper aggrigateWrapperData) {
		if (aggrigateWrapperData.getMeasureRSRPData()!= null) {
			nvResult.setCountRsrp(aggrigateWrapperData.getMeasureRSRPData()[QMDLConstant.COUNT_INDEX].intValue());
			nvResult.setTotalRsrp(aggrigateWrapperData.getMeasureRSRPData()[QMDLConstant.VALUE_INDEX]);
		}
	}

	private void setRssiKpiValue(NVIBUnitResult nvResult, Layer3SummaryWrapper aggrigateWrapperData) {
		if (aggrigateWrapperData.getrSSIData()!= null) {
			nvResult.setCountRssi(aggrigateWrapperData.getrSSIData()[QMDLConstant.COUNT_INDEX].intValue());
			nvResult.setTotalRssi(aggrigateWrapperData.getrSSIData()[QMDLConstant.VALUE_INDEX]);
		}
	}

	private void setSinrKpiValue(NVIBUnitResult nvResult, Layer3SummaryWrapper aggrigateWrapperData) {
		if (aggrigateWrapperData.getsINRData() != null) {
			nvResult.setCountSinr(aggrigateWrapperData.getsINRData()[QMDLConstant.COUNT_INDEX].intValue());
			nvResult.setTotalSinr(aggrigateWrapperData.getsINRData()[QMDLConstant.VALUE_INDEX]);
		}
	}

	private void setUlKpiValue(NVIBUnitResult nvResult, Layer3SummaryWrapper aggrigateWrapperData) {
		if (aggrigateWrapperData.getUlThroughPut() != null) {
			nvResult.setCountUl(aggrigateWrapperData.getUlThroughPut()[QMDLConstant.COUNT_INDEX].intValue());
			nvResult.setTotalUl(aggrigateWrapperData.getUlThroughPut()[QMDLConstant.VALUE_INDEX]);
		}
	}

	private void setDlKpiValue(NVIBUnitResult nvResult, Layer3SummaryWrapper aggrigateWrapperData) {
		if (aggrigateWrapperData.getDlThroughPut() != null) {
			nvResult.setCountDl(aggrigateWrapperData.getDlThroughPut()[QMDLConstant.COUNT_INDEX].intValue());
			nvResult.setTotalDl(aggrigateWrapperData.getDlThroughPut()[QMDLConstant.VALUE_INDEX]);
		}
	}

	private void setEnodbIdAndSectorIdFromCellId(NVIBUnitResult nvResult, Layer3SummaryWrapper aggrigateWrapperData) {
			setEnodebID(nvResult,aggrigateWrapperData.getEnodeBMap());
			setSectorID(nvResult);
	
	}

	private void setSectorID(NVIBUnitResult nvResult) {
		if(nvResult.getCellIdStrongest()!=null){
			nvResult.setSectorId(String.valueOf(nvResult.getCellIdStrongest()));
		}
	}

	private void setEnodebID(NVIBUnitResult nvResult, Map<Integer, Integer> map) {
		if (map != null) {
			Integer enodeBId = null;
			Integer value = ForesightConstants.ZERO;
			for (Entry<Integer, Integer> cellEntry : map.entrySet()) {
				if (value < cellEntry.getValue()) {
					enodeBId = cellEntry.getKey();
					value = cellEntry.getValue();
				}
			}
			
			nvResult.seteNodeBID(enodeBId);
		}
	}

	private void setCellStrongest(NVIBUnitResult nvResult, Map<Integer, Integer> cellIDMap) {
		if (cellIDMap != null) {
			Integer cellId = null;
			Integer value = ForesightConstants.ZERO;
			for (Entry<Integer, Integer> cellEntry : cellIDMap.entrySet()) {
				if (value < cellEntry.getValue()) {
					cellId = cellEntry.getKey();
					value = cellEntry.getValue();
				}
			}
			nvResult.setCellIdStrongest(cellId);
			nvResult.setCellIdCount(value);
		}

	}

	private void setPciStrongest(NVIBUnitResult nvResult, Map<Integer, Integer> pciMap) {
		if (pciMap != null) {
			Integer pci = null;
			Integer value = ForesightConstants.ZERO;
			for (Entry<Integer, Integer> pciEntry : pciMap.entrySet()) {
				if (value < pciEntry.getValue()) {
					pci = pciEntry.getKey();
					value = pciEntry.getValue();
				}
			}
			nvResult.setPciStrongest(pci);
			nvResult.setPciCount(value);
		}
	}

	private String getUniquePciValuesFromMap(Map<Integer, Integer> pciMap) {
		if(pciMap!=null){
		return	NVLayer3Utils.getStringFromIntegerSetValues(new HashSet<>(pciMap.keySet()));
		}
		return null;
	}

	private Unit getUnitFromGWOMap(WORecipeMapping woRecipeMapping) {
		if (woRecipeMapping.getGenericWorkorder().getGwoMeta() != null) {
			Map<String, String> gwoMap = woRecipeMapping.getGenericWorkorder().getGwoMeta();
			String inBuildingMapJson = gwoMap.get(QMDLConstant.IN_BUILDING_MAP_KEY);
			if (inBuildingMapJson != null) {
				Map<Integer, Integer> inBuildingMap = new Gson().fromJson(inBuildingMapJson,
						new TypeToken<HashMap<Integer, Integer>>() {}.getType());
				Integer unitId = inBuildingMap.get(woRecipeMapping.getId());
				try {
					return unitDataDao.findByPk(unitId);
				} catch (DaoException e) {
					logger.error("Error inside the method getUnitFromGWOMap {}",e.getMessage());
				}
			}
		}
		return null;
	}

	@Override
	public List<WalkTestSummaryWrapper> getIBUnitResultForRecipe(List<Integer> woRecipeMappingId) {
		List<WalkTestSummaryWrapper> walkTestSummaryWrapper = new ArrayList<>();
		try {
			walkTestSummaryWrapper = nvibUnitResultDao.getIBUnitResultForRecipe(woRecipeMappingId);
		} catch (RestException e) {
			logger.error("Error retrieving IB Unit Result for Recipe {} ",e.getMessage());
		}
		return walkTestSummaryWrapper;
	}

	@Override
	public List<NVIBUnitResult> getListOfWorkOrderIdByFloorIdAndTemplateType(Integer buildingId, Integer floorId,
			List<TemplateType> templateList) {

		return nvibUnitResultDao.getListOfWorkOrderIdByFloorIdAndTemplateType(buildingId,floorId,templateList);
	}

	@Override
	@Transactional
	public List<GenericWorkorder> getUnitWiseWorkorder(Integer unitId,String technology) {
		try {
			return nvibUnitResultDao.getUnitWiseWorkorder(unitId,technology);
		}catch(Exception e) {
			logger.info("Error while getting unit wise work order: {}",ExceptionUtils.getStackTrace(e));
		}
		return Collections.emptyList();
	}

	@Override
	public String getUnitWiseWorkorderCount(Integer unitId,String technology) {
		try {
	     Long count = nvibUnitResultDao.getUnitWiseWorkorderCount(unitId,technology);
		return String.valueOf(count);
		}catch(Exception e) {
			logger.info("Error while getting unit wise work order: {}",ExceptionUtils.getStackTrace(e));
			return InBuildingConstants.FAILURE_JSON;
		}
	}

	@Override
	@org.springframework.transaction.annotation.Transactional
	public List<NVIBResultUnitWrapper> getWingLevelWiseIBUnitResult(Integer id,String type,String technology){
		logger.info("Going to get NV IB Unit  for  type {} and id {}  ",type ,id );
		try {
			List<NVIBResultUnitWrapper> list = nvibUnitResultDao.getWingLevelWiseIBUnitResult(id,type,technology);
			if (list != null && !list.isEmpty()) {
				return getWingWiseIBunitResult(list);
			} else
				throw new RestException("Data Not avaiable for "+id);
		}catch (DaoException daoException) {
			logger.error("Unable to get Wing Level wise IB Unit Result for type {}  and id {} :Exception {} ",type,id,Utils.getStackTrace(daoException));
			throw new RestException(daoException.getMessage());
		}catch (Exception exception) {
			logger.error("Unable to get Wing Level wise IB Unit Result for type {} and id {} :Exception {} ",type,id,Utils.getStackTrace(exception));
			throw new RestException(exception.getMessage());
		}

	}

	private List<NVIBResultUnitWrapper> getWingWiseIBunitResult(List<NVIBResultUnitWrapper> wrapperList) {
		try {
			List<NVIBResultUnitWrapper> finalFloorList = getAggregratedFloorList(wrapperList);
			return getAggregratedWingList(finalFloorList);
		} catch (Exception exception) {
			logger.error("Unable to get Aggregrated Data for Wing Level  Exception {} ",Utils.getStackTrace(exception));
			throw new RestException("Unable to get Aggregrate Data");
		}
	}

	private List<NVIBResultUnitWrapper> getAggregratedWingList(List<NVIBResultUnitWrapper> finalFloorList) {
		List<NVIBResultUnitWrapper> mainList = new ArrayList<>();
		try {
			Map<Wing, List<NVIBResultUnitWrapper>> wingList = finalFloorList.stream()
					.collect(Collectors.groupingBy(NVIBResultUnitWrapper::getWing));
			wingList.forEach((key, nvIBResultUnitWrapper) -> {
				NVIBResultUnitWrapper wrapper = getAvgScore(nvIBResultUnitWrapper);
				mainList.add(new NVIBResultUnitWrapper(key.getId(), key.getWingName(),
						Long.valueOf(nvIBResultUnitWrapper.size()), nvIBResultUnitWrapper.get(ForesightConstants.ZERO).getLatitude(),
						nvIBResultUnitWrapper.get(ForesightConstants.ZERO).getLongitude(), wrapper.getDlSum(), wrapper.getUlSum(),
						wrapper.getRsrpSum(), wrapper.getSinrSum(), wrapper.getDlCount(), wrapper.getUlCount(),
						wrapper.getRsrpCount(), wrapper.getSinrCount(), wrapper.getSnr(),
						wrapper.getHoFailure(), wrapper.getHoInitiate(),
						wrapper.getHoSuccess(), nvIBResultUnitWrapper.get(ForesightConstants.ZERO).getWing(),
						nvIBResultUnitWrapper,null,

						wrapper.getCallInitate(), wrapper.getCallSuccess(), wrapper.getCallFailure(),
						wrapper.getCallDrop(), wrapper.getCallSetupSuccess(), wrapper.getPdschDlSum(),
						wrapper.getPdschDlCount(), wrapper.getPuschUlSum(), wrapper.getPuschUlCount(),
						wrapper.getMosSum(), wrapper.getMosCount()));
			});
		} catch (Exception exception) {
			logger.error("Unable to aggregrate Wing wise data Exception {} ", Utils.getStackTrace(exception));
		}
		return mainList;
	}
	private List<NVIBResultUnitWrapper> getAggregratedFloorList(List<NVIBResultUnitWrapper> wrapperList) {

		List<NVIBResultUnitWrapper> finalFloorList = new ArrayList<>();
		try {
			Map<Floor, List<NVIBResultUnitWrapper>> floorList = wrapperList.stream()
					.collect(Collectors.groupingBy(NVIBResultUnitWrapper::getFloor));

			floorList.forEach((key, nvIBResultUnitWrapperList) -> {
				Map<Integer, Map<String, List<NVIBResultUnitWrapper>>> distinctUnitMap = nvIBResultUnitWrapperList
						.stream().filter(n -> n.getOperator() != null && n.getId() != null)
						.collect(Collectors.groupingBy(NVIBResultUnitWrapper::getId,
								Collectors.groupingBy(NVIBResultUnitWrapper::getOperator)));
				List<NVIBResultUnitWrapper> unitlist = getAggregatedUnitList(distinctUnitMap);
				NVIBResultUnitWrapper wrapper = getAvgScore(nvIBResultUnitWrapperList);
				finalFloorList
						.add(new NVIBResultUnitWrapper(key.getId(), key.getFloorName(), Long.valueOf(unitlist.size()),
								nvIBResultUnitWrapperList.get(ForesightConstants.ZERO).getLatitude(),
								nvIBResultUnitWrapperList.get(ForesightConstants.ZERO).getLongitude(),
								wrapper.getDlSum(), wrapper.getUlSum(), wrapper.getRsrpSum(), wrapper.getSinrSum(),
								wrapper.getDlCount(), wrapper.getUlCount(), wrapper.getRsrpCount(),
								wrapper.getSinrCount(), wrapper.getSnr(), wrapper.getHoFailure().longValue(),
								wrapper.getHoInitiate().longValue(), wrapper.getHoSuccess().longValue(),
								nvIBResultUnitWrapperList.get(ForesightConstants.ZERO).getWing(), unitlist, null,

								wrapper.getCallInitate(), wrapper.getCallSuccess(), wrapper.getCallFailure(),
								wrapper.getCallDrop(), wrapper.getCallSetupSuccess(), wrapper.getPdschDlSum(),
								wrapper.getPdschDlCount(), wrapper.getPuschUlSum(), wrapper.getPuschUlCount(),
								wrapper.getMosSum(), wrapper.getMosCount()));
			});
		} catch (Exception exception) {
			logger.error("Unable to aggregrate Floor wise data Exception {} ", Utils.getStackTrace(exception));
		}
		return finalFloorList;
	}

	private List<NVIBResultUnitWrapper> getAggregatedUnitList(Map<Integer, Map<String, List<NVIBResultUnitWrapper>>> distinctUnitMap) {
		List<NVIBResultUnitWrapper> finalUnitList=new ArrayList<>();
		try {
			distinctUnitMap.forEach((unitId,list1)->
				list1.forEach((operator,list2)->{
					NVIBResultUnitWrapper  wrapper=getAvgScore(list2);
					finalUnitList.add(new NVIBResultUnitWrapper(unitId, list2.get(ForesightConstants.ZERO).getName(),
							Long.valueOf(list2.size()), list2.get(ForesightConstants.ZERO).getLatitude(),
							list2.get(ForesightConstants.ZERO).getLongitude(), wrapper.getDlSum(), wrapper.getUlSum(),
							wrapper.getRsrpSum(), wrapper.getSinrSum(), wrapper.getDlCount(), wrapper.getUlCount(),
							wrapper.getRsrpCount(), wrapper.getSinrCount(), wrapper.getSnr(),
							wrapper.getHoFailure().longValue(), wrapper.getHoInitiate().longValue(),
							wrapper.getHoSuccess().longValue(), list2.get(ForesightConstants.ZERO).getWing(),
							null,list2.get(ForesightConstants.ZERO).getOperator(),

							wrapper.getCallInitate(), wrapper.getCallSuccess(), wrapper.getCallFailure(),
							wrapper.getCallDrop(), wrapper.getCallSetupSuccess(), wrapper.getPdschDlSum(),
							wrapper.getPdschDlCount(), wrapper.getPuschUlSum(), wrapper.getPuschUlCount(),
							wrapper.getMosSum(), wrapper.getMosCount()));
				})
			);
		}catch(Exception e) {
			logger.error("Unable to aggregrate Unit wise data Exception {} ", Utils.getStackTrace(e));
		}
		return finalUnitList;
	}

	private  NVIBResultUnitWrapper getAvgScore(List<NVIBResultUnitWrapper> nvIBResultUnitWrapper) {
		NVIBResultUnitWrapper wrapper = new NVIBResultUnitWrapper();
		
		Double sumValue = nvIBResultUnitWrapper.stream().filter(s->s.getDlSum()!=null).mapToDouble(NVIBResultUnitWrapper::getDlSum).sum();
		Long countValue = nvIBResultUnitWrapper.stream().filter(s->s.getDlCount()!=null).mapToLong(NVIBResultUnitWrapper::getDlCount).sum();
		wrapper.setDlSum(sumValue != null ? sumValue : null);
		wrapper.setDlCount(countValue != null ? countValue : null);
		
		sumValue = nvIBResultUnitWrapper.stream().filter(s->s.getUlSum()!=null).mapToDouble(NVIBResultUnitWrapper::getUlSum).sum();
		countValue = nvIBResultUnitWrapper.stream().filter(s->s.getUlCount()!=null).mapToLong(NVIBResultUnitWrapper::getUlCount).sum();
		wrapper.setUlSum(sumValue != null ? sumValue : null);
		wrapper.setUlCount(countValue != null ? countValue : null);
		
		sumValue = nvIBResultUnitWrapper.stream().filter(s->s.getRsrpSum()!=null).mapToDouble(NVIBResultUnitWrapper::getRsrpSum).sum();
		countValue = nvIBResultUnitWrapper.stream().filter(s->s.getRsrpCount()!=null).mapToLong(NVIBResultUnitWrapper::getRsrpCount).sum();
		wrapper.setRsrpSum(sumValue != null ? sumValue : null);
		wrapper.setRsrpCount(countValue != null ? countValue : null);
		
		sumValue = nvIBResultUnitWrapper.stream().filter(s->s.getSinrSum()!=null).mapToDouble(NVIBResultUnitWrapper::getSinrSum).sum();
		countValue = nvIBResultUnitWrapper.stream().filter(s->s.getSinrCount()!=null).mapToLong(NVIBResultUnitWrapper::getSinrCount).sum();
		wrapper.setSinrSum(sumValue != null ? sumValue : null);
		wrapper.setSinrCount(countValue != null ? countValue : null);
		
		
		sumValue = nvIBResultUnitWrapper.stream().filter(s->s.getPdschDlSum()!=null).mapToDouble(NVIBResultUnitWrapper::getPdschDlSum).sum();
		countValue = nvIBResultUnitWrapper.stream().filter(s->s.getPdschDlCount()!=null).mapToLong(NVIBResultUnitWrapper::getPdschDlCount).sum();
		wrapper.setPdschDlSum(sumValue != null ? sumValue : null);
		wrapper.setPdschDlCount(countValue != null ? countValue : null);
	
		sumValue = nvIBResultUnitWrapper.stream().filter(s->s.getPuschUlSum()!=null).mapToDouble(NVIBResultUnitWrapper::getPuschUlSum).sum();
		countValue = nvIBResultUnitWrapper.stream().filter(s->s.getPuschUlCount()!=null).mapToLong(NVIBResultUnitWrapper::getPuschUlCount).sum();
		wrapper.setPuschUlSum(sumValue != null ? sumValue : null);
		wrapper.setPuschUlCount(countValue != null ? countValue : null);
	
		sumValue = nvIBResultUnitWrapper.stream().filter(s->s.getMosSum()!=null).mapToDouble(NVIBResultUnitWrapper::getMosSum).sum();
		countValue = nvIBResultUnitWrapper.stream().filter(s->s.getMosCount()!=null).mapToLong(NVIBResultUnitWrapper::getMosCount).sum();
		wrapper.setMosSum(sumValue != null ? sumValue : null);
		wrapper.setMosCount(countValue != null ? countValue : null);
	
		
		wrapper.setHoFailure(nvIBResultUnitWrapper.stream().filter(s->s.getHoFailure()!=null).mapToLong(s->s.getHoFailure().intValue()).sum());
		wrapper.setHoInitiate(nvIBResultUnitWrapper.stream().filter(s->s.getHoInitiate()!=null).mapToLong(s->s.getHoInitiate().intValue()).sum());
		wrapper.setHoSuccess(nvIBResultUnitWrapper.stream().filter(s->s.getHoSuccess()!=null).mapToLong(s->s.getHoSuccess().intValue()).sum());
		
		wrapper.setCallInitate(nvIBResultUnitWrapper.stream().filter(s->s.getCallInitate()!=null).mapToLong(s->s.getCallInitate().intValue()).sum());
		wrapper.setCallDrop(nvIBResultUnitWrapper.stream().filter(s->s.getCallDrop()!=null).mapToLong(s->s.getCallDrop().intValue()).sum());
		wrapper.setCallFailure(nvIBResultUnitWrapper.stream().filter(s->s.getCallFailure()!=null).mapToLong(s->s.getCallFailure().intValue()).sum());
		wrapper.setCallSuccess(nvIBResultUnitWrapper.stream().filter(s->s.getCallSuccess()!=null).mapToLong(s->s.getCallSuccess().intValue()).sum());
		wrapper.setCallSetupSuccess(nvIBResultUnitWrapper.stream().filter(s->s.getCallSetupSuccess()!=null).mapToLong(s->s.getCallSetupSuccess().intValue()).sum());
		
		
		return wrapper;
	}
}

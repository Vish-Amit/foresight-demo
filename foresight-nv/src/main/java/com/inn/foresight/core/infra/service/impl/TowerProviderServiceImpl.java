package com.inn.foresight.core.infra.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.maps.Corner;
import com.inn.commons.maps.geometry.BoundaryUtils;
import com.inn.commons.maps.geometry.gis.GIS2DPolygon;
import com.inn.commons.maps.geometry.gis.GISGeometry;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.core.generic.utils.ApplicationContextProvider;
import com.inn.core.generic.utils.ExceptionUtil;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.INetworkElementDao;
import com.inn.foresight.core.infra.dao.ITowerProviderDao;
import com.inn.foresight.core.infra.model.Tower;
import com.inn.foresight.core.infra.model.TowerProvider;
import com.inn.foresight.core.infra.service.INEReportService;
import com.inn.foresight.core.infra.service.ITowerProviderService;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.InfraUtils;
import com.inn.foresight.core.infra.wrapper.KPISummaryDataWrapper;
import com.inn.foresight.core.infra.wrapper.TowerProviderWrapper;
import com.inn.foresight.core.report.utils.ReportUtils;
import com.inn.product.systemconfiguration.utils.SystemConfigurationUtils;

@Service("TowerProviderServiceImpl")
public class TowerProviderServiceImpl extends AbstractService<Integer, TowerProvider> implements ITowerProviderService {

	private Logger logger = LogManager.getLogger(TowerProviderServiceImpl.class);
	static Map<String, String> systemConfigMap = SystemConfigurationUtils.systemConfMap;
	static Integer geographyL1Zoom = 0;
	static Integer geographyL2Zoom = 0;
	static Integer geographyL3Zoom = 0;
	static Integer geographyL4Zoom = 0;
    Map<String, String> sysConfMap = new HashMap<>();
	@Autowired
	private ITowerProviderDao iTowerProviderDao;

	@Autowired
	private INetworkElementDao iNetworkElementDao;
	
	@Override
	public List<TowerProviderWrapper> getTowerDetailsForProviders(Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat, List<String> name, Integer displayTowers,
			Integer zoomLevel) {
		List<TowerProviderWrapper> towerProviderWrapperList = new ArrayList<>();
		logger.info("going to get Tower Details by Providers.");
		try {
			if (zoomLevel < displayTowers) {
				String geographyLevel = NEVisualizationServiceImpl.getGeographyForAggregation(zoomLevel);
				List<String> geographyList = getDistinctGeography(geographyLevel, southWestLong, southWestLat, northEastLong, northEastLat);
				if (geographyList != null && !geographyList.isEmpty()) {
					towerProviderWrapperList = iTowerProviderDao.getCountOfTowersByProvider(southWestLong, southWestLat, northEastLong, northEastLat, geographyLevel, geographyList, name);
				} else {
					logger.warn("No Geography Found in ViewPort");
					throw new RestException("No Geography Found in ViewPort");
				}

			} else {
				List<Tower> towerProviderList=iTowerProviderDao.getTowerDetailsByProvider(name, southWestLat, northEastLat, southWestLong, northEastLong);
				towerProviderWrapperList = getTowerProviderDetailWrapper(towerProviderList);
			}
		} catch (DaoException daoException) {
			logger.info("DaoException Inside getTowerDetailsForProviders method {}", daoException);
			throw new RestException(ExceptionUtil.generateExceptionCode("Service", "TowerProvider", daoException));
		} catch (Exception exception) {
			logger.error("error inside getTowerDetailsForProviders, err = {}", Utils.getStackTrace(exception));
		}
		return towerProviderWrapperList;
	}

	private List<String> getDistinctGeography(String geographyLevel, Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat) {
		List<String> geographyList = new ArrayList<>();
		try {
			logger.info("Going top get distinct Geography for geographyLevel {} ", geographyLevel);
			geographyList = iNetworkElementDao.getDistinctGeography(geographyLevel, southWestLong, southWestLat, northEastLong, northEastLat);
			logger.info("Distinct Geography in viewport is {}", geographyList);
		} catch (Exception exception) {
			logger.error("Unable to get geography For geogrphylevel {} due to exception {}", geographyLevel, Utils.getStackTrace(exception));
		}
		return geographyList;
	}

	private Map<String, Long> getAggregatedCountsForKpi(List<TowerProviderWrapper> towerDetailList) {
		Map<String, Long> kpiCountMap = new HashMap<>();
		try {
			Integer totalCount = towerDetailList.size();
			kpiCountMap = towerDetailList.stream().collect(Collectors.groupingBy(TowerProviderWrapper::getTowerProviderName, Collectors.counting()));
			kpiCountMap.put(InfraConstants.TOTAL_SITE_COUNTS, new Long(totalCount));
		} catch (Exception exception) {
			logger.error("Unable to get the aggregated Count for kpi {}", Utils.getStackTrace(exception));
		}
		return kpiCountMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Long> getTowerCountsForProviders(KPISummaryDataWrapper filterConfiguration, Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat,
			Integer zoomLevel){
		logger.info("Going to get the tower counts.");
		Map<String, Long> mapForSiteCounts = new HashMap<>();
		try {
				List<String> providersList = new ArrayList<>();
				if (filterConfiguration.getKpiMap() != null) {
					Map kpiMap = filterConfiguration.getKpiMap();
					if(kpiMap.get(InfraConstants.TOWER_PROVIDERS) != null && !((List<String>) kpiMap.get(InfraConstants.TOWER_PROVIDERS)).isEmpty()) {
					providersList = (List<String>) kpiMap.get(InfraConstants.TOWER_PROVIDERS);
					List<Tower> towerProviderList= iTowerProviderDao.getTowerDetailsByProvider(providersList, southWestLat, northEastLat, southWestLong, northEastLong);
					List<TowerProviderWrapper> combinedList = getTowerProviderDetailWrapper(towerProviderList);
     				mapForSiteCounts = getAggregatedCountsForKpi(combinedList);
     				}
			}
			return mapForSiteCounts;
		} catch (Exception exception) {
			logger.error("Exception in  getTowersCountsForKPI  {} ", ExceptionUtils.getStackTrace(exception));
		}
		return mapForSiteCounts;
	}

	private List<TowerProviderWrapper> getTowerProviderDetailWrapper(List<Tower> towerList) {
		List<TowerProviderWrapper> towerProviderWrapperList = new ArrayList<>();
		TowerProviderWrapper towerProviderWrapper=new TowerProviderWrapper();
		for (Tower tower : towerList) {
		try {
			if(tower != null) {
			   towerProviderWrapper=new TowerProviderWrapper();
			   if(tower.getId() != null) {
				towerProviderWrapper.setId(tower.getId());
			   }
				towerProviderWrapper.setTowerId(tower.getTowerId() != null ? tower.getTowerId() : ForesightConstants.HIPHEN);
				towerProviderWrapper.setTowerName(tower.getTowerName() != null && !(tower.getTowerName().equalsIgnoreCase(ForesightConstants.BLANK_STRING)) ? StringEscapeUtils.unescapeHtml(tower.getTowerName()) : ForesightConstants.HIPHEN);
				if(tower.getLatitude() != null) {
				towerProviderWrapper.setLatitude(tower.getLatitude());
				}
				if(tower.getLongitude() != null) {
				towerProviderWrapper.setLongitude(tower.getLongitude());
				}
				towerProviderWrapper.setTowerHeight(tower.getTowerHeight());
				if(tower.getTowerProvider() != null) {
				towerProviderWrapper.setTowerProviderName(tower.getTowerProvider().getName() != null && !(tower.getTowerProvider().getName().equalsIgnoreCase(ForesightConstants.BLANK_STRING)) ? StringEscapeUtils.unescapeHtml(tower.getTowerProvider().getName()) : ForesightConstants.HIPHEN);
				towerProviderWrapper.setServiceType(tower.getTowerProvider().getServiceType() != null && !(tower.getTowerProvider().getServiceType().equalsIgnoreCase(ForesightConstants.BLANK_STRING)) ? tower.getTowerProvider().getServiceType() : ForesightConstants.HIPHEN);
				if(tower.getTowerProvider().getLegendColor() != null)
				towerProviderWrapper.setLegendColor(tower.getTowerProvider().getLegendColor());
				towerProviderWrapper.setProviderRank(tower.getTowerProvider().getProviderRank());
				}
				if(tower.getCreationTime() != null)
				towerProviderWrapper.setCreationTime(tower.getCreationTime());
				if(tower.getModificationTime() != null)
				towerProviderWrapper.setModificationTime(tower.getModificationTime());
				if(tower.getGeographyL4() != null) {
				towerProviderWrapper.setGeographyL4(tower.getGeographyL4().getDisplayName() != null? tower.getGeographyL4().getDisplayName() :  ForesightConstants.HIPHEN);
				if(tower.getGeographyL4().getGeographyL3() != null) {
				towerProviderWrapper.setGeographyL3(tower.getGeographyL4().getGeographyL3().getDisplayName() != null? tower.getGeographyL4().getGeographyL3().getDisplayName() :  ForesightConstants.HIPHEN);
				if(tower.getGeographyL4().getGeographyL3().getGeographyL2() != null) {
				towerProviderWrapper.setGeographyL2(tower.getGeographyL4().getGeographyL3().getGeographyL2().getDisplayName() != null? tower.getGeographyL4().getGeographyL3().getGeographyL2().getDisplayName() :  ForesightConstants.HIPHEN);
				if(tower.getGeographyL4().getGeographyL3().getGeographyL2().getGeographyL1() != null) {
				towerProviderWrapper.setGeographyL1(tower.getGeographyL4().getGeographyL3().getGeographyL2().getGeographyL1().getDisplayName() != null? tower.getGeographyL4().getGeographyL3().getGeographyL2().getGeographyL1().getDisplayName() :  ForesightConstants.HIPHEN);
				}
				}
				}
				}
				towerProviderWrapperList.add(towerProviderWrapper);
				towerProviderWrapper=null;
			}else
				logger.error("Unable to set Tower provider parameters in wrapper for towerid {}", tower.getTowerId());
		} catch (Exception exception) {
			logger.error("Unable to get tower provider list due to exception {}",  Utils.getStackTrace(exception));
		}
	}
		return towerProviderWrapperList;
	}
	@Override
	public Map<String, Long> getTowerCountsInsidePolygon(KPISummaryDataWrapper filterConfiguration, Integer zoomLevel) {
		Map<String, Long> kpiMap = new HashMap<>();
		try {
			List<List<List<Double>>> polygons = filterConfiguration.getPolyList();
			for (List<List<Double>> polygon : polygons) {
				GISGeometry gisPolygon = new GIS2DPolygon(polygon);
				Corner bounds = BoundaryUtils.getCornerOfBoundary(polygon);
				Double minlat = bounds.getMinLatitude();
				Double maxlat = bounds.getMaxLatitude();
				Double minlon = bounds.getMinLongitude();
				Double maxlon = bounds.getMaxLongitude();
				kpiMap = getTowerCountsForProviders(filterConfiguration, minlon, minlat, maxlon, maxlat, zoomLevel);
			}
		} catch (Exception exception) {
			logger.error("Error in getting tower counts for KPI in polygon. Trace: ");
		}
		return kpiMap;
	}
	@Override
	public Map<String, Long> getTowerCountsForGeographies(KPISummaryDataWrapper filterConfiguration) {
		Map<String, List<String>> geographyNames = new HashMap<>();
		List<String> providersList = new ArrayList<>();
		Map<String, Long> mapForSiteCounts = new HashMap<>();
		Map kpiMap = new HashMap<>();
		try {
			if (filterConfiguration != null ) {
				if( filterConfiguration.getKpiMap() != null) {
			    kpiMap = filterConfiguration.getKpiMap();
				providersList = (List<String>) kpiMap.get(InfraConstants.TOWER_PROVIDERS);
				}
				if( filterConfiguration.getGeographyList() != null && kpiMap.get(InfraConstants.GEOGRAPHY_TYPE) != null) {
				geographyNames.put((String) kpiMap.get(InfraConstants.GEOGRAPHY_TYPE), filterConfiguration.getGeographyList());
				}
			}
			List<TowerProviderWrapper> towerDetailList = iTowerProviderDao.getTowerDetailsByGeography(providersList,geographyNames);
			mapForSiteCounts = getAggregatedCountsForKpi(towerDetailList);
			
		} catch (Exception exception) {
			logger.error("Unable to get custonm count for KPI for  Exception {}", Utils.getStackTrace(exception));
		}
		return mapForSiteCounts;
	}
	@Override
	public  Map<String, String> getTowerDetailReport(String towerId) {
		logger.info("going to get Tower Detail Report for Tower :{}",towerId);
		Map<String, String> fileMap = new HashMap<>();
		try {
			fileMap.put(InfraConstants.KEY_FILENAME, createReportForTowerDetail(towerId));
		} catch (Exception e) {
			logger.error("Execption in generating Tower Detail Report {}", Utils.getStackTrace(e));
		}
		return fileMap;
	}
	private String createReportForTowerDetail(String towerId) throws IOException, Exception {
		   logger.info("Going to create the report for towerId {} ",towerId);
		   try {
			   String excelFileName = createFileNameForTowerDetailReport(towerId);
			   XSSFWorkbook workbook = new XSSFWorkbook();
			   Map<String, XSSFCellStyle> style = NEReportServiceImpl.createStylesForReport(workbook);
			   String sheetName="TowerDetail";
			try {
					  generateTowerDetailSheet(workbook, towerId, sheetName, style);
					  logger.info("tower detail sheet created successfully.");
				} catch (Exception exception) {
						   logger.error("Unable to create tower detail sheet", Utils.getStackTrace(exception));
					   }
			   String filePath = InfraUtils.exportExcelFile(workbook,ConfigUtils.getString(ConfigEnum.SITE_REPORT_FILE_PATH.getValue()), excelFileName, true);
			   logger.info("The uri is as follows -> "+filePath);
			   return filePath;
		   } catch (Exception exception) {
			   logger.info("Unable to generate Report for Tower {}", Utils.getStackTrace(exception));
			   throw new RestException("No Data Found");
		   }
	   }
	  private void generateTowerDetailSheet(XSSFWorkbook workbook, String towerId,String sheetName,Map<String, XSSFCellStyle> style) throws Exception {
		   logger.info("going to generate Tower Detail sheet.");
		   try {
			   
			   int rowNum = 0;
			   XSSFSheet sheet = workbook.createSheet(sheetName);
			   TowerProviderWrapper towerProviderWrapper=new TowerProviderWrapper();
			   try {
				  List<Tower> towerList= iTowerProviderDao.getTowerDetailsByTowerId(towerId);
               	if(towerList != null && !towerList.isEmpty()) {
				   towerProviderWrapper=   getTowerProviderDetailWrapper(towerList).get(0);
			   }
			   }catch(Exception exception) {
				   throw new RestException("unable to get Tower detail.");
			   }
			   if (towerProviderWrapper != null) {
				   INEReportService neReportService = ApplicationContextProvider.getApplicationContext().getBean(INEReportService.class);
				   Map<String,String> geographyMap=neReportService.getMappingForGeographies();
				   String[] firstHeader = {"Tower Id","Tower Name","Latitude","Longitude","Tower Height","Service Type","Tower Provider Name","Provider Rank", geographyMap.get(ForesightConstants.GEOGRAPHY_L1),geographyMap.get(ForesightConstants.GEOGRAPHY_L2), geographyMap.get(ForesightConstants.GEOGRAPHY_L3),geographyMap.get(ForesightConstants.GEOGRAPHY_L4)};
				   sheet.createRow(rowNum).setHeightInPoints(40);
				   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.ORANGEHEADER), false, sheet, firstHeader);
				   rowNum++;
				   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.DATACELL_KEY), false, sheet,
						   InfraUtils.checkNullString(towerProviderWrapper.getTowerId()), 
						   InfraUtils.checkNullString(towerProviderWrapper.getTowerName()),
						   (towerProviderWrapper.getLatitude() != null ? towerProviderWrapper.getLatitude() : ForesightConstants.HIPHEN),
						   (towerProviderWrapper.getLongitude() != null ? towerProviderWrapper.getLongitude() : ForesightConstants.HIPHEN),
						   (towerProviderWrapper.getTowerHeight() != null ? towerProviderWrapper.getTowerHeight() : ForesightConstants.HIPHEN),
						   InfraUtils.checkNullString(towerProviderWrapper.getServiceType()),
						   InfraUtils.checkNullString(towerProviderWrapper.getTowerProviderName()),
						   (towerProviderWrapper.getProviderRank() != null ? towerProviderWrapper.getProviderRank() : ForesightConstants.HIPHEN),
						   InfraUtils.checkNullString(towerProviderWrapper.getGeographyL1()),
						   InfraUtils.checkNullString(towerProviderWrapper.getGeographyL2()),
						   InfraUtils.checkNullString(towerProviderWrapper.getGeographyL3()),
						   InfraUtils.checkNullString(towerProviderWrapper.getGeographyL4()));
				   
				   neReportService.setColumnWidthForReport(sheet);
				    
			   }
		   } catch (Exception exception) {
			   logger.error("Unable to create tower detail sheet {}", Utils.getStackTrace(exception));
		   }
	   }
	  
	private String createFileNameForTowerDetailReport(String towerName) {
		   String excelFileName = null;
		   excelFileName =  "TowerDetail"  + ForesightConstants.UNDERSCORE + towerName.toUpperCase();
		   excelFileName += ForesightConstants.EXCEL_EXTENSION;
		   logger.debug("generated Tower Detail report file name = {}", excelFileName);
		   return excelFileName;
	   }
	
	@Override
	public List<TowerProviderWrapper> getAllTowerProviderDetails() {
		logger.info("Going to get tower provider list.");
		List<TowerProviderWrapper> towerProviderList = new ArrayList<>();
		try {
			towerProviderList = iTowerProviderDao.getAllTowerProviderDetails();
		} catch (Exception exception) {
			logger.error("Unable to get tower provider list due to exception {}", Utils.getStackTrace(exception));
		}
		return towerProviderList;
	}
}

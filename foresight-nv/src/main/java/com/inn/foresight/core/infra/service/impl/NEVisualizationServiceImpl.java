package com.inn.foresight.core.infra.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.Tuple;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.commons.maps.Corner;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.geometry.BoundaryUtils;
import com.inn.commons.maps.geometry.gis.GIS2DPolygon;
import com.inn.commons.maps.geometry.gis.GISGeometry;
import com.inn.commons.maps.geometry.gis.GISPoint;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.utils.ExceptionUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.IMacroSiteDetailDao;
import com.inn.foresight.core.infra.dao.INEBandDetailDao;
import com.inn.foresight.core.infra.dao.INETaskDetailDao;
import com.inn.foresight.core.infra.dao.INetworkElementDao;
import com.inn.foresight.core.infra.dao.IRANDetailDao;
import com.inn.foresight.core.infra.dao.IWifiSiteDetailDao;
import com.inn.foresight.core.infra.model.MacroSiteDetail;
import com.inn.foresight.core.infra.model.NEBandDetail;
import com.inn.foresight.core.infra.model.NETaskDetail;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.foresight.core.infra.model.RANDetail;
import com.inn.foresight.core.infra.service.CustomNEVisualizationService;
import com.inn.foresight.core.infra.service.INEVisualizationService;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.InfraUtils;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.NEStatus;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.utils.enums.Technology;
import com.inn.foresight.core.infra.utils.enums.Vendor;
import com.inn.foresight.core.infra.wrapper.KPISummaryDataWrapper;
import com.inn.foresight.core.infra.wrapper.MacroSiteDetailWrapper;
import com.inn.foresight.core.infra.wrapper.NETaskDetailWrapper;
import com.inn.foresight.core.infra.wrapper.NetworkElementWrapper;
import com.inn.foresight.core.infra.wrapper.SectorSummaryWrapper;
import com.inn.foresight.core.infra.wrapper.SiteConnectionPointWrapper;
import com.inn.foresight.core.infra.wrapper.SiteEmsDetailsWrapper;
import com.inn.foresight.core.infra.wrapper.SiteGeographicalDetail;
import com.inn.foresight.core.infra.wrapper.SiteLayerSelection;
import com.inn.foresight.core.infra.wrapper.SiteMileStoneDetailWrapper;
import com.inn.foresight.core.infra.wrapper.SiteSelectionWrapper;
import com.inn.foresight.core.infra.wrapper.SiteSummaryOverviewWrapper;
import com.inn.foresight.core.infra.wrapper.WifiWrapper;
import com.inn.product.systemconfiguration.utils.SystemConfigurationUtils;

/**
 * The Class NEVisualizationServiceImpl.
 */


@Service("NEVisualizationServiceImpl")
public class NEVisualizationServiceImpl implements INEVisualizationService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NEVisualizationServiceImpl.class);

	/** The dao. */
	@Autowired
	private INetworkElementDao networkElementDao;

	/** The dao. */
	@Autowired
	private INEBandDetailDao ineBandDetailDao;

	/** The macro site detail dao. */
	@Autowired
	private IMacroSiteDetailDao macroSiteDetailDao;
	
	@Autowired
	private IRANDetailDao iranDetailDao;

	@Autowired
	private IWifiSiteDetailDao wifiSiteDetailDao;
	
	@Autowired
	private INETaskDetailDao ineTaskDetailDao;

	/** The sys conf map. */
	Map<String, String> sysConfMap = new HashMap<>();

	/** The geography L 1 zoom. */
	static Integer geographyL1Zoom = 0;

	/** The geography L 2 zoom. */
	static Integer geographyL2Zoom = 0;

	/** The geography L 3 zoom. */
	static Integer geographyL3Zoom = 0;

	/** The geography L 4 zoom. */
	static Integer geographyL4Zoom = 0;

	private String GEOGRAPHY = "geography";

	/** The alpha bandwidth first. */
	private String ALPHA_BANDWIDTH_FIRST = ForesightConstants.BLANK_STRING;

	/** The beta bandwidth first. */
	private String BETA_BANDWIDTH_FIRST = ForesightConstants.BLANK_STRING;

	/** The gamma bandwidth first. */
	private String GAMMA_BANDWIDTH_FIRST = ForesightConstants.BLANK_STRING;

	/** The alpha bandwidth second. */
	private String ALPHA_BANDWIDTH_SECOND = ForesightConstants.BLANK_STRING;

	/** The beta bandwidth second. */
	private String BETA_BANDWIDTH_SECOND = ForesightConstants.BLANK_STRING;

	/** The gamma bandwidth second. */
	private String GAMMA_BANDWIDTH_SECOND = ForesightConstants.BLANK_STRING;
	private String ALPHA_ADDITIONAL_BANDWIDTH_FIRST = ForesightConstants.BLANK_STRING;
	/** The beta bandwidth first. */
	private String BETA_ADDITIONAL_BANDWIDTH_FIRST = ForesightConstants.BLANK_STRING;

	/** The gamma bandwidth first. */
	private String GAMMA_ADDITIONAL_BANDWIDTH_FIRST = ForesightConstants.BLANK_STRING;

	/** The alpha bandwidth second. */
	private String ALPHA_ADDITIONAL_BANDWIDTH_SECOND = ForesightConstants.BLANK_STRING;

	/** The beta bandwidth second. */
	private String BETA_ADDITIONAL_BANDWIDTH_SECOND = ForesightConstants.BLANK_STRING;

	/** The gamma bandwidth second. */
	private String GAMMA_ADDITIONAL_BANDWIDTH_SECOND = ForesightConstants.BLANK_STRING;

	static Map<String, String> systemConfigMap = SystemConfigurationUtils.systemConfMap;
	
	@Autowired(required = false)
	@Qualifier("CustomNEVisualizationServiceImpl")
	private CustomNEVisualizationService customNEVisualizationService;

	public List<String> getBandForMacroOnAir(SiteSelectionWrapper siteSelectionWrapper) {
		List<String> bandMacroOnAir = new ArrayList<>();
		try {
			boolean band850 = false;
			boolean band2300 = false;
			boolean band1800 = false;
			if (siteSelectionWrapper.getMacroOnair() != null && !siteSelectionWrapper.getMacroOnair().isEmpty()) {
				if (siteSelectionWrapper.getMacroOnair().get(InfraConstants.BAND2300_STR) != null) {
					band2300 = (boolean) siteSelectionWrapper.getMacroOnair().get(InfraConstants.BAND2300_STR);
				}
				if (siteSelectionWrapper.getMacroOnair().get(InfraConstants.BAND1800_STR) != null) {
					band1800 = (boolean) siteSelectionWrapper.getMacroOnair().get(InfraConstants.BAND1800_STR);
				}
				if (siteSelectionWrapper.getMacroOnair().get(InfraConstants.BAND850_STR) != null) {
					band850 = (boolean) siteSelectionWrapper.getMacroOnair().get(InfraConstants.BAND850_STR);
				}
				if (band2300) {

					bandMacroOnAir.add(InfraConstants.BAND_2300);
				}
				if (band1800) {
					bandMacroOnAir.add(InfraConstants.BAND_1800);
				}
				if (band850) {
					bandMacroOnAir.add(InfraConstants.BAND_850);
				}
			}

		} catch (Exception e) {
			logger.error("error in getBandForMacroOnAir, err  = {}", Utils.getStackTrace(e));

		}
		return bandMacroOnAir;
	}

	/**
	 * Gets the band for macro planned.
	 *
	 * @param siteSelectionWrapper the site selection wrapper
	 * @return the band for macro planned
	 */
	public List<String> getBandForMacroPlanned(SiteSelectionWrapper siteSelectionWrapper) {
		List<String> bandMacroPlanned = new ArrayList<>();
		try {
			if (siteSelectionWrapper.getMacroPlanned() != null && !siteSelectionWrapper.getMacroPlanned().isEmpty()) {
				boolean band2300 = (Boolean) siteSelectionWrapper.getMacroOnair().get(InfraConstants.BAND2300_STR);
				boolean band1800 = (Boolean) siteSelectionWrapper.getMacroOnair().get(InfraConstants.BAND1800_STR);
				boolean band850 = (Boolean) siteSelectionWrapper.getMacroOnair().get(InfraConstants.BAND850_STR);
				if (band2300) {
					bandMacroPlanned.add(InfraConstants.BAND_2300);
				}
				if (band1800) {
					bandMacroPlanned.add(InfraConstants.BAND_1800);
				}
				if (band850) {
					bandMacroPlanned.add(InfraConstants.BAND_850);
				}
			}
		} catch (Exception e) {
			logger.error("error in  getProgressStateForMacro, err  = {} ", Utils.getStackTrace(e));
		}
		return bandMacroPlanned;
	}

	/**
	 * Gets the band for SC outdoor onair.
	 *
	 * @param siteSelectionWrapper the site selection wrapper
	 * @return the band for SC outdoor onair
	 */
	public List<String> getBandForSCOutdoorOnair(SiteSelectionWrapper siteSelectionWrapper) {
		List<String> bandMacroPlanned = new ArrayList<>();
		try {
			if (siteSelectionWrapper.getOnairOutdoorSmallCell() != null
					&& !siteSelectionWrapper.getOnairOutdoorSmallCell().isEmpty()) {
				boolean band2300 = (Boolean) siteSelectionWrapper.getOnairOutdoorSmallCell()
						.get(InfraConstants.BAND2300_STR);
				boolean band1800 = (Boolean) siteSelectionWrapper.getOnairOutdoorSmallCell()
						.get(InfraConstants.BAND1800_STR);
				boolean band850 = (Boolean) siteSelectionWrapper.getOnairOutdoorSmallCell()
						.get(InfraConstants.BAND850_STR);
				if (band2300) {
					bandMacroPlanned.add(InfraConstants.BAND_2300);
				}
				if (band1800) {
					bandMacroPlanned.add(InfraConstants.BAND_1800);
				}
				if (band850) {
					bandMacroPlanned.add(InfraConstants.BAND_850);
				}
			}
		} catch (Exception e) {
			logger.error("error in getProgressStateForMacro, err  = {}", Utils.getStackTrace(e));
		}
		return bandMacroPlanned;
	}

	/**
	 * Gets the band for SC outdoor planned.
	 *
	 * @param siteSelectionWrapper the site selection wrapper
	 * @return the band for SC outdoor planned
	 */
	public List<String> getBandForSCOutdoorPlanned(SiteSelectionWrapper siteSelectionWrapper) {
		List<String> bandMacroPlanned = new ArrayList<>();
		try {
			if (siteSelectionWrapper.getPlannedOutdoorSmallCell() != null
					&& !siteSelectionWrapper.getPlannedOutdoorSmallCell().isEmpty()) {
				boolean band2300 = (Boolean) siteSelectionWrapper.getPlannedOutdoorSmallCell()
						.get(InfraConstants.BAND2300_STR);
				boolean band1800 = (Boolean) siteSelectionWrapper.getPlannedOutdoorSmallCell()
						.get(InfraConstants.BAND1800_STR);
				boolean band850 = (Boolean) siteSelectionWrapper.getPlannedOutdoorSmallCell()
						.get(InfraConstants.BAND850_STR);
				if (band2300) {
					bandMacroPlanned.add(InfraConstants.BAND_2300);
				}
				if (band1800) {
					bandMacroPlanned.add(InfraConstants.BAND_1800);
				}
				if (band850) {
					bandMacroPlanned.add(InfraConstants.BAND_850);
				}
			}
		} catch (Exception e) {
			logger.error("error in getProgressStateForMacro, err  = {}", Utils.getStackTrace(e));
		}
		return bandMacroPlanned;
	}

	/**
	 * Gets the band for SC indoor onair.
	 *
	 * @param siteSelectionWrapper the site selection wrapper
	 * @return the band for SC indoor onair
	 */
	public List<String> getBandForSCIndoorOnair(SiteSelectionWrapper siteSelectionWrapper) {
		List<String> bandMacroPlanned = new ArrayList<>();
		try {
			if (siteSelectionWrapper.getOnairIndoorSmallCell() != null
					&& !siteSelectionWrapper.getOnairIndoorSmallCell().isEmpty()) {
				boolean band2300 = (Boolean) siteSelectionWrapper.getOnairIndoorSmallCell()
						.get(InfraConstants.BAND2300_STR);
				boolean band1800 = (Boolean) siteSelectionWrapper.getOnairIndoorSmallCell()
						.get(InfraConstants.BAND1800_STR);
				boolean band850 = (Boolean) siteSelectionWrapper.getOnairIndoorSmallCell()
						.get(InfraConstants.BAND850_STR);
				if (band2300) {
					bandMacroPlanned.add(InfraConstants.BAND_2300);
				}
				if (band1800) {
					bandMacroPlanned.add(InfraConstants.BAND_1800);
				}
				if (band850) {
					bandMacroPlanned.add(InfraConstants.BAND_850);
				}
			}
		} catch (Exception e) {
			logger.error("error in getProgressStateForMacro, err  = {}", Utils.getStackTrace(e));
		}
		return bandMacroPlanned;
	}

	/**
	 * Gets the band for SC indoor planned.
	 *
	 * @param siteSelectionWrapper the site selection wrapper
	 * @return the band for SC indoor planned
	 */
	public List<String> getBandForSCIndoorPlanned(SiteSelectionWrapper siteSelectionWrapper) {
		List<String> bandMacroPlanned = new ArrayList<>();
		try {
			if (siteSelectionWrapper.getPlannedIndoorSmallCell() != null
					&& !siteSelectionWrapper.getPlannedIndoorSmallCell().isEmpty()) {
				boolean band2300 = (Boolean) siteSelectionWrapper.getPlannedIndoorSmallCell()
						.get(InfraConstants.BAND2300_STR);
				boolean band1800 = (Boolean) siteSelectionWrapper.getPlannedIndoorSmallCell()
						.get(InfraConstants.BAND1800_STR);
				boolean band850 = (Boolean) siteSelectionWrapper.getPlannedIndoorSmallCell()
						.get(InfraConstants.BAND850_STR);
				if (band2300) {
					bandMacroPlanned.add(InfraConstants.BAND_2300);
				}
				if (band1800) {
					bandMacroPlanned.add(InfraConstants.BAND_1800);
				}
				if (band850) {
					bandMacroPlanned.add(InfraConstants.BAND_850);
				}
			}
		} catch (Exception e) {
			logger.error("error in getProgressStateForMacro, err  = {}", Utils.getStackTrace(e));
		}
		return bandMacroPlanned;
	}

	/**
	 * Gets the site table data.
	 *
	 * @param southWestLong the south west long
	 * @param southWestLat  the south west lat
	 * @param northEastLong the north east long
	 * @param northEastLat  the north east lat
	 * @param zoomLevel     the zoom level
	 * @param neType        the ne type
	 * @param progressState the progress state
	 * @param band2300      the band 2300
	 * @param band1800      the band 1800
	 * @param band850       the band 850
	 * @param displyaSite   the displya site
	 * @return the site table data
	 * @throws RestException the rest exception
	 */
	@Override
	public List<NetworkElementWrapper> getSiteTableData(Double southWestLong, Double southWestLat, Double northEastLong,
			Double northEastLat, Integer zoomLevel, NEType neType, String progressState, Boolean band2300,
			Boolean band1800, Boolean band850, Integer displyaSite) {
		List<NetworkElementWrapper> neDataList = new ArrayList<>();
		try {
			Map<String, Double> latlngMap = Utils.getSizeWizeLatLong(southWestLong, southWestLat, northEastLong,
					northEastLat);
			if (zoomLevel >= geographyL1Zoom && zoomLevel < geographyL3Zoom) {
				neDataList = networkElementDao.getSiteTableDataForGeographyL2(latlngMap.get(InfraConstants.SMALL_LONG),
						latlngMap.get(InfraConstants.SMALL_LAT), latlngMap.get(InfraConstants.LARGE_LONG),
						latlngMap.get(InfraConstants.LARGE_LAT), progressState, neType, null);
			} else if (zoomLevel >= geographyL3Zoom && zoomLevel < geographyL4Zoom) {
				neDataList = networkElementDao.getSiteTableDataForGeographyL3(latlngMap.get(InfraConstants.SMALL_LONG),
						latlngMap.get(InfraConstants.SMALL_LAT), latlngMap.get(InfraConstants.LARGE_LONG),
						latlngMap.get(InfraConstants.LARGE_LAT), progressState, neType, null);
			} else if (zoomLevel >= geographyL4Zoom) {
				neDataList = networkElementDao.getSiteTableDataForGeographyL4(latlngMap.get(InfraConstants.SMALL_LONG),
						latlngMap.get(InfraConstants.SMALL_LAT), latlngMap.get(InfraConstants.LARGE_LONG),
						latlngMap.get(InfraConstants.LARGE_LAT), progressState, neType, null);
			}
		} catch (DaoException e) {
			logger.info("Exception Inside getSiteTableData method " + e);
			throw new RestException(ExceptionUtil.generateExceptionCode("Service", "NetworkElement", e));

		} catch (Exception e) {
			logger.error("error in getSiteTableData, err = {}", Utils.getStackTrace(e));
		}
		return neDataList;
	}

	/**
	 * Gets the site overview detail data.
	 *
	 * @param neName the ne name
	 * @return the site overview detail data
	 * @throws RestException the rest exception
	 */
	@Override
	public SiteGeographicalDetail getSiteOverviewDetailData(String neName) {
		SiteGeographicalDetail siteDetailWrapper = new SiteGeographicalDetail();
		logger.info("going to get overview of site {}", neName);
		try {
			if (Utils.isValidString(neName)) {
				siteDetailWrapper = networkElementDao.getSiteOverviewDetailData(neName);
			}
		} catch (DaoException e) {
			logger.info("DaoException Inside getSiteOverviewDetailData method " + e);
			throw new RestException(ExceptionUtil.generateExceptionCode("Service", "NetworkElement", e));
		} catch (Exception e) {
			logger.error("error ingetSiteOverviewDetailData, err = {}", Utils.getStackTrace(e));
		}
		return siteDetailWrapper;
	}

	/**
	 * Gets the ems detail data for sites.
	 *
	 * @param sapid the sapid
	 * @return the ems detail data for sites
	 * @throws RestException the rest exception
	 */
	@Override
	public SiteEmsDetailsWrapper getEmsDetailDataForSites(String neName) {
		List<SiteEmsDetailsWrapper> siteEmsDetailsWrapper = new ArrayList<>();
		try {
			if (neName != null) {
				siteEmsDetailsWrapper = networkElementDao.getEmsDetailDataForSites(neName);
				if (!siteEmsDetailsWrapper.isEmpty() && siteEmsDetailsWrapper.get(0) != null)
					return siteEmsDetailsWrapper.get(0);
				else
					logger.warn("data is not available for nename: {}", neName);
			} else {
				throw new RestException("site can not null site : " + neName);
			}
		} catch (Exception exception) {
			logger.error("Unable to get EmsDetailData For Sites {} Exception {} ", neName,
					Utils.getStackTrace(exception));
		}
		return null;

	}

	/**
	 * Gets the geography for aggregation.
	 *
	 * @param zoomLevel the zoom level
	 * @return the geography for aggregation
	 * @throws RestException the rest exception
	 */
	public static String getGeographyForAggregation(Integer zoomLevel) {
		try {
			geographyL1Zoom = Utils.toInteger(systemConfigMap.get(ForesightConstants.GEOGRAPHYL1_ZOOM));
			geographyL2Zoom = Utils.toInteger(systemConfigMap.get(ForesightConstants.GEOGRAPHYL2_ZOOM));
			geographyL3Zoom = Utils.toInteger(systemConfigMap.get(ForesightConstants.GEOGRAPHYL3_ZOOM));
			geographyL4Zoom = Utils.toInteger(systemConfigMap.get(ForesightConstants.GEOGRAPHYL4_ZOOM));
			if (zoomLevel >= geographyL1Zoom && zoomLevel < geographyL2Zoom) {
				return InfraConstants.GEOGRAPHYL1;
			}
			if (zoomLevel >= geographyL2Zoom && zoomLevel < geographyL3Zoom) {
				return InfraConstants.GEOGRAPHYL2;
			}
			if (zoomLevel >= geographyL3Zoom && zoomLevel < geographyL4Zoom) {
				return InfraConstants.GEOGRAPHYL3;
			}
			if (zoomLevel >= geographyL4Zoom) {
				return InfraConstants.GEOGRAPHYL4;
			}
			return ForesightConstants.BLANK_STRING;
		} catch (Exception exception) {
			throw new RestException(exception.getMessage());
		}
	}

	/**
	 * Gets the aggregated counts for sites.
	 *
	 * @param siteSelectionWrapper the site selection wrapper
	 * @param combinedList         the combined list
	 * @return the aggregated counts for sites
	 */
	public List<NetworkElementWrapper> getAggregatedCountsForSites(SiteSelectionWrapper siteSelectionWrapper,
			List<NetworkElementWrapper> combinedList) {
		List<NetworkElementWrapper> countStatusWiseList = new ArrayList<>();
		try {
			Map<NEType, Map<String, List<NetworkElementWrapper>>> layerCountMap = combinedList.stream()
					.collect(Collectors.groupingBy(NetworkElementWrapper::getNeType,
							Collectors.groupingBy(NetworkElementWrapper::getStatus, Collectors.toList())));
			countStatusWiseList = getSidePanelDetails(siteSelectionWrapper, combinedList, layerCountMap, layerCountMap);
		} catch (Exception exception) {
			logger.error("Unable to get the aggregated Count for layer {}", Utils.getStackTrace(exception));
		}
		return countStatusWiseList;
	}

	/**
	 * Gets the count status and ne type.
	 *
	 * @param map                 the map
	 * @param countMap            the count map
	 * @param countStatusWiseList the count status wise list
	 * @return the count status and ne type
	 * 
	 */
	private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		try {
			Map<Object, Boolean> seen = new ConcurrentHashMap<>();
			return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
		} catch (Exception exception) {
			logger.error("error in finding distinctByKey  Exception {} ", Utils.getStackTrace(exception));
		}
		return null;
	}

	private List<NetworkElementWrapper> getCountStatusAndNeType(Map map,
			Map<NEType, Map<String, List<NetworkElementWrapper>>> countMap,
			List<NetworkElementWrapper> countStatusWiseList) {
		try {
			countMap.forEach((netype, statusMap) -> {
				try {
					statusMap.forEach((status, wrapperList) -> {
						try {
							NetworkElementWrapper elementWrapper = new NetworkElementWrapper();
							elementWrapper.setNeType(netype);
							if (netype.toString().equalsIgnoreCase(NEType.MACRO_CELL.name())
									|| netype.toString().equalsIgnoreCase(NEType.PICO_CELL.name())
									|| netype.toString().equalsIgnoreCase(NEType.IBS_CELL.name())
									|| netype.toString().equalsIgnoreCase(NEType.SHOOTER_CELL.name())
									|| netype.toString().equalsIgnoreCase(NEType.ODSC_CELL.name())
									|| netype.toString().equalsIgnoreCase(NEType.IDSC_CELL.name())
									|| netype.toString().equalsIgnoreCase(NEType.GALLERY_CELL.name())) {
								elementWrapper.setTotalCount(wrapperList.stream()
										.filter(distinctByKey(p -> p.getSapid() + p.getNeFrequency())).count());
							} else {
								elementWrapper.setTotalCount(
										wrapperList.stream().mapToLong(NetworkElementWrapper::getTotalCount).sum());
							}
							elementWrapper.setProgressState(status);
							countStatusWiseList.add(elementWrapper);
						} catch (Exception exception) {
							logger.error("Unable to get NetworkElementWrapper for key {}, value {} due to exception {}",
									status, wrapperList, Utils.getStackTrace(exception));
						}
					});
				} catch (Exception exception) {
					logger.error("Invalid key value for map {}", Utils.getStackTrace(exception));
				}

			});
		} catch (Exception exception) {
			logger.error("Invalid key value for map {}", Utils.getStackTrace(exception));
		}
		return countStatusWiseList;
	}

	/**
	 * Gets the side panel details.
	 *
	 * @param siteSelectionWrapper the site selection wrapper
	 * @param combinedList         the combined list
	 * @param map                  the map
	 * @param countMap             the count map
	 * @return the side panel details
	 */
	private List<NetworkElementWrapper> getSidePanelDetails(SiteSelectionWrapper siteSelectionWrapper,
			List<NetworkElementWrapper> combinedList, Map map,
			Map<NEType, Map<String, List<NetworkElementWrapper>>> countMap) {
		List<NetworkElementWrapper> countStatusWise = new ArrayList<>();
		try {
			countStatusWise = getCountStatusAndNeType(siteSelectionWrapper.getMacroOnair(), countMap, countStatusWise);

			countStatusWise = getCountStatusAndNeType(siteSelectionWrapper.getMacroPlanned(), countMap,
					countStatusWise);
			countStatusWise = getCountStatusAndNeType(siteSelectionWrapper.getPlannedOutdoorSmallCell(), countMap,
					countStatusWise);
			countStatusWise = getCountStatusAndNeType(siteSelectionWrapper.getOnairOutdoorSmallCell(), countMap,
					countStatusWise);
			countStatusWise = getCountStatusAndNeType(siteSelectionWrapper.getOnairOutdoorWifi(), countMap,
					countStatusWise);

			countStatusWise = getCountStatusAndNeType(siteSelectionWrapper.getPicoOnAir(), countMap, countStatusWise);

			countStatusWise = getCountStatusAndNeType(siteSelectionWrapper.getPlannedPico(), countMap, countStatusWise);

			countStatusWise = getCountStatusAndNeType(siteSelectionWrapper.getShooterOnAir(), countMap,
					countStatusWise);

			countStatusWise = getCountStatusAndNeType(siteSelectionWrapper.getPlannedShooter(), countMap,
					countStatusWise);

			countStatusWise = getCountStatusAndNeType(siteSelectionWrapper.getDasOnair(), countMap, countStatusWise);

			countStatusWise = getCountStatusAndNeType(siteSelectionWrapper.getDasPlanned(), countMap, countStatusWise);

			countStatusWise = getCountStatusAndNeType(siteSelectionWrapper.getMacroNonradiating(), countMap,
					countStatusWise);

			countStatusWise = getCountStatusAndNeType(siteSelectionWrapper.getMacroDecommissioned(), countMap,
					countStatusWise);

		} catch (Exception exception) {
			logger.error("Unable to get list for SidePanelDetails {} ", Utils.getStackTrace(exception));
		}
		return countStatusWise;
	}

	/**
	 * Gets the mapping for overview detail.
	 *
	 * @param siteSummaryOverviewWrapper the site summary overview wrapper
	 * @return the mapping for overview detail
	 */
	private Map<String, List<SectorSummaryWrapper>> getMappingForOverviewDetail(
			SiteSummaryOverviewWrapper siteSummaryOverviewWrapper,
			SiteSummaryOverviewWrapper siteSummaryOverviewWrapperSecond, String neName, String neFrequency,
			String neStatus) {
		Map<String, List<SectorSummaryWrapper>> map = new HashMap<>();
		try {
			List<SectorSummaryWrapper> overviewParameters = new ArrayList<>();
			Boolean isAlpha = false;
			Boolean isBeta = false;
			Boolean isGamma = false;
			Boolean isAddAlpha = false;
			Boolean isAddBeta = false;
			Boolean isAddGamma = false;
			Boolean isAddSecondAlpha = false;
			Boolean isAddSecondBeta = false;
			Boolean isAddSecondGamma = false;
			List<Integer> sectorIdList = new ArrayList<>();
			try {
				sectorIdList = macroSiteDetailDao.getSectorIdForSiteOverViewDetails(neName, neFrequency, neStatus,
						ForesightConstants.FIRST);

			} catch (Exception exception) {
				logger.warn("Unable to get Fourth Sector Data for SiteOverview Details Message {} ",
						exception.getMessage());
			}
			if (sectorIdList != null && !sectorIdList.isEmpty()) {
				for (Integer sector : sectorIdList) {
					if (sector == ForesightConstants.INTEGER_SECTOR_4) {
						isAlpha = true;
					}
					if (sector == ForesightConstants.INTEGER_SECTOR_5) {
						isBeta = true;
					}
					if (sector == ForesightConstants.INTEGER_SECTOR_6) {
						isGamma = true;
					}
				}
			}
			try {
				sectorIdList = macroSiteDetailDao.getSectorIdForSiteOverViewDetails(neName, neFrequency, neStatus,
						ForesightConstants.SECOND);

			} catch (Exception exception) {
				logger.warn("Unable to get Fourth Sector Data for SiteOverview Details Message {} ",
						exception.getMessage());
			}

			if (sectorIdList != null && !sectorIdList.isEmpty()) {
				for (Integer sector : sectorIdList) {
					if (sector == ForesightConstants.INTEGER_SECTOR_1) {
						isAddSecondAlpha = true;
					}
					if (sector == ForesightConstants.INTEGER_SECTOR_2) {
						isAddSecondBeta = true;
					}
					if (sector == ForesightConstants.INTEGER_SECTOR_3) {
						isAddSecondGamma = true;
					}
					if (sector == ForesightConstants.INTEGER_SECTOR_4) {
						isAddAlpha = true;
					}
					if (sector == ForesightConstants.INTEGER_SECTOR_5) {
						isAddBeta = true;
					}
					if (sector == ForesightConstants.INTEGER_SECTOR_6) {
						isAddGamma = true;
					}
				}
			}
			SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
			sectorSummaryWrapper = getEcgiMappingForSector(siteSummaryOverviewWrapper,
					siteSummaryOverviewWrapperSecond);
			if (sectorSummaryWrapper != null) {
				sectorSummaryWrapper.setIndex(1);
				sectorSummaryWrapper.setParameter("ECGI");
				sectorSummaryWrapper = setValueForSector(sectorSummaryWrapper, isAlpha, isBeta, isGamma, isAddAlpha,
						isAddBeta, isAddGamma, isAddSecondAlpha, isAddSecondBeta, isAddSecondGamma);
			}

			overviewParameters.add(sectorSummaryWrapper);
			sectorSummaryWrapper = null;
			sectorSummaryWrapper = getCellidMappingForSector(siteSummaryOverviewWrapper,
					siteSummaryOverviewWrapperSecond);
			if (sectorSummaryWrapper != null) {
				sectorSummaryWrapper.setIndex(2);
				sectorSummaryWrapper.setParameter(ForesightConstants.CELL_ID_CAMEL);
				sectorSummaryWrapper = setValueForSector(sectorSummaryWrapper, isAlpha, isBeta, isGamma, isAddAlpha,
						isAddBeta, isAddGamma, isAddSecondAlpha, isAddSecondBeta, isAddSecondGamma);
			}
			overviewParameters.add(sectorSummaryWrapper);
			sectorSummaryWrapper = null;
			sectorSummaryWrapper = getPCIMappingForSector(siteSummaryOverviewWrapper, siteSummaryOverviewWrapperSecond);
			if (sectorSummaryWrapper != null) {
				sectorSummaryWrapper.setIndex(3);
				sectorSummaryWrapper.setParameter(ForesightConstants.PCI);
				sectorSummaryWrapper = setValueForSector(sectorSummaryWrapper, isAlpha, isBeta, isGamma, isAddAlpha,
						isAddBeta, isAddGamma, isAddSecondAlpha, isAddSecondBeta, isAddSecondGamma);
			}
			overviewParameters.add(sectorSummaryWrapper);
			sectorSummaryWrapper = null;
			sectorSummaryWrapper = getSectorIdMappingForSector(siteSummaryOverviewWrapper,
					siteSummaryOverviewWrapperSecond);
			if (sectorSummaryWrapper != null) {
				sectorSummaryWrapper.setIndex(4);
				sectorSummaryWrapper.setParameter(ForesightConstants.SECTORID_);
				sectorSummaryWrapper = setValueForSector(sectorSummaryWrapper, isAlpha, isBeta, isGamma, isAddAlpha,
						isAddBeta, isAddGamma, isAddSecondAlpha, isAddSecondBeta, isAddSecondGamma);
			}
			overviewParameters.add(sectorSummaryWrapper);
			sectorSummaryWrapper = null;
			sectorSummaryWrapper = getBandwidthMappingForSector(siteSummaryOverviewWrapper,
					siteSummaryOverviewWrapperSecond);
			if (sectorSummaryWrapper != null) {
				sectorSummaryWrapper.setIndex(5);
				sectorSummaryWrapper.setParameter(ForesightConstants.BANDWIDTH_CAMAL_CASE);
				sectorSummaryWrapper = setValueForSector(sectorSummaryWrapper, isAlpha, isBeta, isGamma, isAddAlpha,
						isAddBeta, isAddGamma, isAddSecondAlpha, isAddSecondBeta, isAddSecondGamma);
			}
			overviewParameters.add(sectorSummaryWrapper);
			sectorSummaryWrapper = null;
			sectorSummaryWrapper = getOperationalStatusForSector(siteSummaryOverviewWrapper,
					siteSummaryOverviewWrapperSecond);
			if (sectorSummaryWrapper != null) {
				sectorSummaryWrapper.setIndex(6);
				sectorSummaryWrapper.setParameter("Operational Status");
				sectorSummaryWrapper = setValueForSector(sectorSummaryWrapper, isAlpha, isBeta, isGamma, isAddAlpha,
						isAddBeta, isAddGamma, isAddSecondAlpha, isAddSecondBeta, isAddSecondGamma);
			}
			overviewParameters.add(sectorSummaryWrapper);
			
			sectorSummaryWrapper = null;
			sectorSummaryWrapper = getAdminStateForSector(siteSummaryOverviewWrapper,
					siteSummaryOverviewWrapperSecond);
			if (sectorSummaryWrapper != null) {
				sectorSummaryWrapper.setIndex(7);
				sectorSummaryWrapper.setParameter("Administrative State");
				sectorSummaryWrapper = setValueForSector(sectorSummaryWrapper, isAlpha, isBeta, isGamma, isAddAlpha,
						isAddBeta, isAddGamma, isAddSecondAlpha, isAddSecondBeta, isAddSecondGamma);
			}
			overviewParameters.add(sectorSummaryWrapper);
			
			map.put(InfraConstants.OVERVIEW_PARAMETER, overviewParameters);

		} catch (Exception exception) {
			logger.error("Exception in getting Mapping for sector Information {} ", Utils.getStackTrace(exception));

		}
		return map;
	}

	private SectorSummaryWrapper setValueForSector(SectorSummaryWrapper sectorSummaryWrapper, Boolean isAlpha,
			Boolean isBeta, Boolean isGamma, Boolean isAddAlpha, Boolean isAddBeta, Boolean isAddGamma,
			Boolean isAddSecondAlpha, Boolean isAddSecondBeta, Boolean isAddSecondGamma) {
		sectorSummaryWrapper.setIsAlpha(isAlpha);
		sectorSummaryWrapper.setIsBeta(isBeta);
		sectorSummaryWrapper.setIsGamma(isGamma);
		sectorSummaryWrapper.setIsAddAlpha(isAddAlpha);
		sectorSummaryWrapper.setIsAddBeta(isAddBeta);
		sectorSummaryWrapper.setIsAddGamma(isAddGamma);
		sectorSummaryWrapper.setIsAddSecondAlpha(isAddSecondAlpha);
		sectorSummaryWrapper.setIsAddSecondBeta(isAddSecondBeta);
		sectorSummaryWrapper.setIsAddSecondGamma(isAddSecondGamma);
		return sectorSummaryWrapper;
	}

	private SectorSummaryWrapper getEcgiMappingForSector(SiteSummaryOverviewWrapper siteSummaryOverviewWrapper,
			SiteSummaryOverviewWrapper siteSummaryOverviewWrapperSecond) {
		SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
		try {
			if (siteSummaryOverviewWrapper.getAlphaecgi() != null)
				sectorSummaryWrapper.setAlpha(siteSummaryOverviewWrapper.getAlphaecgi());
			if (siteSummaryOverviewWrapper.getBetaecgi() != null)
				sectorSummaryWrapper.setBeta(siteSummaryOverviewWrapper.getBetaecgi());
			if (siteSummaryOverviewWrapper.getGammaecgi() != null)
				sectorSummaryWrapper.setGamma(siteSummaryOverviewWrapper.getGammaecgi());
			if (siteSummaryOverviewWrapper.getAlphaecgiadd() != null)
				sectorSummaryWrapper.setAddAlpha(siteSummaryOverviewWrapper.getAlphaecgiadd());
			if (siteSummaryOverviewWrapper.getBetaecgiadd() != null)
				sectorSummaryWrapper.setAddBeta(siteSummaryOverviewWrapper.getBetaecgiadd());
			if (siteSummaryOverviewWrapper.getGammaecgiadd() != null)
				sectorSummaryWrapper.setAddGamma(siteSummaryOverviewWrapper.getGammaecgiadd());

			if (siteSummaryOverviewWrapperSecond.getAlphaecgiSecond() != null)
				sectorSummaryWrapper.setAlphaSecond(siteSummaryOverviewWrapperSecond.getAlphaecgiSecond());
			if (siteSummaryOverviewWrapperSecond.getBetaecgiSecond() != null)
				sectorSummaryWrapper.setBetaSecond(siteSummaryOverviewWrapperSecond.getBetaecgiSecond());
			if (siteSummaryOverviewWrapperSecond.getGammaecgiSecond() != null)
				sectorSummaryWrapper.setGammaSecond(siteSummaryOverviewWrapperSecond.getGammaecgiSecond());
			if (siteSummaryOverviewWrapperSecond.getAlphaecgiaddSecond() != null)
				sectorSummaryWrapper.setAddAlphaSecond(siteSummaryOverviewWrapperSecond.getAlphaecgiaddSecond());
			if (siteSummaryOverviewWrapperSecond.getBetaecgiaddSecond() != null)
				sectorSummaryWrapper.setAddBetaSecond(siteSummaryOverviewWrapperSecond.getBetaecgiaddSecond());
			if (siteSummaryOverviewWrapperSecond.getGammaecgiaddSecond() != null)
				sectorSummaryWrapper.setAddGammaSecond(siteSummaryOverviewWrapperSecond.getGammaecgiaddSecond());

		} catch (Exception exception) {
			logger.error("Unable to get the Ecgi sector data", Utils.getStackTrace(exception));
		}
		return sectorSummaryWrapper;
	}

	private SectorSummaryWrapper getCellidMappingForSector(SiteSummaryOverviewWrapper siteSummaryOverviewWrapper,
			SiteSummaryOverviewWrapper siteSummaryOverviewWrapperSecond) {
		SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
		try {
			if (siteSummaryOverviewWrapper.getAlphacellid() != null)
				sectorSummaryWrapper.setAlpha(siteSummaryOverviewWrapper.getAlphacellid().toString());
			if (siteSummaryOverviewWrapper.getBetacellid() != null)
				sectorSummaryWrapper.setBeta(siteSummaryOverviewWrapper.getBetacellid().toString());
			if (siteSummaryOverviewWrapper.getGammacellid() != null)
				sectorSummaryWrapper.setGamma(siteSummaryOverviewWrapper.getGammacellid().toString());
			if (siteSummaryOverviewWrapper.getAlphacellidadd() != null)
				sectorSummaryWrapper.setAddAlpha(String.valueOf(siteSummaryOverviewWrapper.getAlphacellidadd()));
			if (siteSummaryOverviewWrapper.getBetacellidadd() != null)
				sectorSummaryWrapper.setAddBeta(String.valueOf(siteSummaryOverviewWrapper.getBetacellidadd()));
			if (siteSummaryOverviewWrapper.getGammacellidadd() != null)
				sectorSummaryWrapper.setAddGamma(String.valueOf(siteSummaryOverviewWrapper.getGammacellidadd()));

			if (siteSummaryOverviewWrapperSecond.getAlphacellidSecond() != null)
				sectorSummaryWrapper.setAlphaSecond(siteSummaryOverviewWrapperSecond.getAlphacellidSecond().toString());
			if (siteSummaryOverviewWrapperSecond.getBetacellidSecond() != null)
				sectorSummaryWrapper.setBetaSecond(siteSummaryOverviewWrapperSecond.getBetacellidSecond().toString());
			if (siteSummaryOverviewWrapperSecond.getGammacellidSecond() != null)
				sectorSummaryWrapper.setGammaSecond(siteSummaryOverviewWrapperSecond.getGammacellidSecond().toString());
			if (siteSummaryOverviewWrapperSecond.getAlphacellidaddSecond() != null)
				sectorSummaryWrapper
						.setAddAlphaSecond(String.valueOf(siteSummaryOverviewWrapperSecond.getAlphacellidaddSecond()));
			if (siteSummaryOverviewWrapperSecond.getBetacellidaddSecond() != null)
				sectorSummaryWrapper
						.setAddBetaSecond(String.valueOf(siteSummaryOverviewWrapperSecond.getBetacellidaddSecond()));
			if (siteSummaryOverviewWrapperSecond.getGammacellidaddSecond() != null)
				sectorSummaryWrapper
						.setAddGammaSecond(String.valueOf(siteSummaryOverviewWrapperSecond.getGammacellidaddSecond()));
		} catch (Exception exception) {
			logger.error("Unable to get the cellid sector data", Utils.getStackTrace(exception));
		}
		return sectorSummaryWrapper;
	}

	private SectorSummaryWrapper getBandwidthMappingForSector(SiteSummaryOverviewWrapper siteSummaryOverviewWrapper,
			SiteSummaryOverviewWrapper siteSummaryOverviewWrapperSecond) {
		SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
		try {
			if (siteSummaryOverviewWrapper.getAlphabandwidth() != null)
				sectorSummaryWrapper.setAlpha(siteSummaryOverviewWrapper.getAlphabandwidth());
			if (siteSummaryOverviewWrapper.getBetabandwidth() != null)
				sectorSummaryWrapper.setBeta(siteSummaryOverviewWrapper.getBetabandwidth());
			if (siteSummaryOverviewWrapper.getGammabandwidth() != null)
				sectorSummaryWrapper.setGamma(siteSummaryOverviewWrapper.getGammabandwidth());
			if (siteSummaryOverviewWrapper.getAlphabandwidthadd() != null)
				sectorSummaryWrapper.setAddAlpha(String.valueOf(siteSummaryOverviewWrapper.getAlphabandwidthadd()));
			if (siteSummaryOverviewWrapper.getBetabandwidthadd() != null)
				sectorSummaryWrapper.setAddBeta(String.valueOf(siteSummaryOverviewWrapper.getBetabandwidthadd()));
			if (siteSummaryOverviewWrapper.getGammabandwidthadd() != null)
				sectorSummaryWrapper.setAddGamma(String.valueOf(siteSummaryOverviewWrapper.getGammabandwidthadd()));

			if (siteSummaryOverviewWrapperSecond.getAlphabandwidthSecond() != null)
				sectorSummaryWrapper.setAlphaSecond(siteSummaryOverviewWrapperSecond.getAlphabandwidthSecond());
			if (siteSummaryOverviewWrapperSecond.getBetabandwidthSecond() != null)
				sectorSummaryWrapper.setBetaSecond(siteSummaryOverviewWrapperSecond.getBetabandwidthSecond());
			if (siteSummaryOverviewWrapperSecond.getGammabandwidthSecond() != null)
				sectorSummaryWrapper.setGammaSecond(siteSummaryOverviewWrapperSecond.getGammabandwidthSecond());
			if (siteSummaryOverviewWrapperSecond.getAlphabandwidthaddSecond() != null)
				sectorSummaryWrapper.setAddAlphaSecond(
						String.valueOf(siteSummaryOverviewWrapperSecond.getAlphabandwidthaddSecond()));
			if (siteSummaryOverviewWrapperSecond.getBetabandwidthaddSecond() != null)
				sectorSummaryWrapper
						.setAddBetaSecond(String.valueOf(siteSummaryOverviewWrapperSecond.getBetabandwidthaddSecond()));
			if (siteSummaryOverviewWrapperSecond.getGammabandwidthaddSecond() != null)
				sectorSummaryWrapper.setAddGammaSecond(
						String.valueOf(siteSummaryOverviewWrapperSecond.getGammabandwidthaddSecond()));
		} catch (Exception exception) {
			logger.error("Unable to get the bandwidth sector data", Utils.getStackTrace(exception));
		}
		return sectorSummaryWrapper;
	}

	/**
	 * Gets the mapping for antenna parameter.
	 *
	 * @param siteSummaryOverviewWrapper the site summary overview wrapper
	 * @return the mapping for antenna parameter
	 */
	private Map<String, List<SectorSummaryWrapper>> getMappingForAntennaParameter(
			SiteSummaryOverviewWrapper siteSummaryOverviewWrapper, String neName, String neFrequency, String neStatus) {
		Map<String, List<SectorSummaryWrapper>> map = new HashMap<>();
		try {
			List<SectorSummaryWrapper> antennaParameters = new ArrayList<>();
			SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
			Boolean isAlpha = false;
			Boolean isBeta = false;
			Boolean isGamma = false;
			Boolean isAddAlpha = false;
			Boolean isAddBeta = false;
			Boolean isAddGamma = false;
			Boolean isAddSecondAlpha = false;
			Boolean isAddSecondBeta = false;
			Boolean isAddSecondGamma = false;
			List<Integer> sectorIdList = new ArrayList<>();
			try {
				sectorIdList = macroSiteDetailDao.getSectorIdForSiteOverViewDetails(neName, neFrequency, neStatus,
						ForesightConstants.FIRST);

			} catch (Exception exception) {
				logger.warn("Unable to get Fourth Sector Data for SiteOverview Details Message {} ",
						exception.getMessage());
			}
			if (sectorIdList != null && !sectorIdList.isEmpty()) {
				for (Integer sector : sectorIdList) {
					if (sector == ForesightConstants.INTEGER_SECTOR_4) {
						isAlpha = true;
					}
					if (sector == ForesightConstants.INTEGER_SECTOR_5) {
						isBeta = true;
					}
					if (sector == ForesightConstants.INTEGER_SECTOR_6) {
						isGamma = true;
					}
				}
			}
			try {
				sectorIdList = macroSiteDetailDao.getSectorIdForSiteOverViewDetails(neName, neFrequency, neStatus,
						ForesightConstants.SECOND);

			} catch (Exception exception) {
				logger.warn("Unable to get Fourth Sector Data for SiteOverview Details Message {} ",
						exception.getMessage());
			}

			if (sectorIdList != null && !sectorIdList.isEmpty()) {
				for (Integer sector : sectorIdList) {
					if (sector == ForesightConstants.INTEGER_SECTOR_1) {
						isAddSecondAlpha = true;
					}
					if (sector == ForesightConstants.INTEGER_SECTOR_2) {
						isAddSecondBeta = true;
					}
					if (sector == ForesightConstants.INTEGER_SECTOR_3) {
						isAddSecondGamma = true;
					}
					if (sector == ForesightConstants.INTEGER_SECTOR_4) {
						isAddAlpha = true;
					}
					if (sector == ForesightConstants.INTEGER_SECTOR_5) {
						isAddBeta = true;
					}
					if (sector == ForesightConstants.INTEGER_SECTOR_6) {
						isAddGamma = true;
					}
				}
			}
			sectorSummaryWrapper = getSectorIdMappingForSector(siteSummaryOverviewWrapper, null);
			if (sectorSummaryWrapper != null) {
				sectorSummaryWrapper.setIndex(1);
				sectorSummaryWrapper.setParameter(InfraConstants.SECTORID_);
				sectorSummaryWrapper = setValueForSector(sectorSummaryWrapper, isAlpha, isBeta, isGamma, isAddAlpha,
						isAddBeta, isAddGamma, isAddSecondAlpha, isAddSecondBeta, isAddSecondGamma);
			}
			antennaParameters.add(sectorSummaryWrapper);
			sectorSummaryWrapper = null;
			sectorSummaryWrapper = getAntennaTypeMappingForSector(siteSummaryOverviewWrapper);
			if (sectorSummaryWrapper != null) {
				sectorSummaryWrapper.setIndex(2);
				sectorSummaryWrapper.setParameter(InfraConstants.ANTENNA_TYPE_CAMEL_CASE);
				sectorSummaryWrapper = setValueForSector(sectorSummaryWrapper, isAlpha, isBeta, isGamma, isAddAlpha,
						isAddBeta, isAddGamma, isAddSecondAlpha, isAddSecondBeta, isAddSecondGamma);
			}
			antennaParameters.add(sectorSummaryWrapper);
			sectorSummaryWrapper = null;
			sectorSummaryWrapper = getAntennaVendorNameMappingForSector(siteSummaryOverviewWrapper);
			if (sectorSummaryWrapper != null) {
				sectorSummaryWrapper.setIndex(3);
				sectorSummaryWrapper.setParameter(InfraConstants.ANTENNA_VENDOR);
				sectorSummaryWrapper = setValueForSector(sectorSummaryWrapper, isAlpha, isBeta, isGamma, isAddAlpha,
						isAddBeta, isAddGamma, isAddSecondAlpha, isAddSecondBeta, isAddSecondGamma);
			}
			antennaParameters.add(sectorSummaryWrapper);
			sectorSummaryWrapper = null;
			sectorSummaryWrapper = getAzimuthMappingForSector(siteSummaryOverviewWrapper);
			if (sectorSummaryWrapper != null) {
				sectorSummaryWrapper.setIndex(4);
				sectorSummaryWrapper.setParameter(InfraConstants.AZIMUTH);
				sectorSummaryWrapper = setValueForSector(sectorSummaryWrapper, isAlpha, isBeta, isGamma, isAddAlpha,
						isAddBeta, isAddGamma, isAddSecondAlpha, isAddSecondBeta, isAddSecondGamma);
			}
			antennaParameters.add(sectorSummaryWrapper);
			sectorSummaryWrapper = null;

			sectorSummaryWrapper = getAntennaHeightMappingForSector(siteSummaryOverviewWrapper);
			if (sectorSummaryWrapper != null) {
				sectorSummaryWrapper.setIndex(5);
				sectorSummaryWrapper.setParameter(InfraConstants.ANTENNA_HEIGHT);
				sectorSummaryWrapper = setValueForSector(sectorSummaryWrapper, isAlpha, isBeta, isGamma, isAddAlpha,
						isAddBeta, isAddGamma, isAddSecondAlpha, isAddSecondBeta, isAddSecondGamma);
			}

			antennaParameters.add(sectorSummaryWrapper);
			sectorSummaryWrapper = null;

			sectorSummaryWrapper = getElecTiltMappingForSector(siteSummaryOverviewWrapper);
			if (sectorSummaryWrapper != null) {
				sectorSummaryWrapper.setIndex(6);
				sectorSummaryWrapper.setParameter(InfraConstants.ELECTRICAL_TILT);
				sectorSummaryWrapper = setValueForSector(sectorSummaryWrapper, isAlpha, isBeta, isGamma, isAddAlpha,
						isAddBeta, isAddGamma, isAddSecondAlpha, isAddSecondBeta, isAddSecondGamma);
			}
			antennaParameters.add(sectorSummaryWrapper);
			sectorSummaryWrapper = null;

			sectorSummaryWrapper = getMechTiltMappingForSector(siteSummaryOverviewWrapper);
			if (sectorSummaryWrapper != null) {
				sectorSummaryWrapper.setIndex(7);
				sectorSummaryWrapper.setParameter(InfraConstants.MECHANICAL_TILT);
				sectorSummaryWrapper = setValueForSector(sectorSummaryWrapper, isAlpha, isBeta, isGamma, isAddAlpha,
						isAddBeta, isAddGamma, isAddSecondAlpha, isAddSecondBeta, isAddSecondGamma);
			}
			antennaParameters.add(sectorSummaryWrapper);
			sectorSummaryWrapper = null;

			sectorSummaryWrapper = getTotalTiltMappingForSector(siteSummaryOverviewWrapper);
			if (sectorSummaryWrapper != null) {
				sectorSummaryWrapper.setIndex(8);
				sectorSummaryWrapper.setParameter(InfraConstants.TOTALTILT);
				sectorSummaryWrapper = setValueForSector(sectorSummaryWrapper, isAlpha, isBeta, isGamma, isAddAlpha,
						isAddBeta, isAddGamma, isAddSecondAlpha, isAddSecondBeta, isAddSecondGamma);
			}
			antennaParameters.add(sectorSummaryWrapper);
			sectorSummaryWrapper = null;

			sectorSummaryWrapper = getAntennaGainMappingForSector(siteSummaryOverviewWrapper);
			if (sectorSummaryWrapper != null) {
				sectorSummaryWrapper.setIndex(9);
				sectorSummaryWrapper.setParameter(InfraConstants.ANTENNA_GAIN);
				sectorSummaryWrapper = setValueForSector(sectorSummaryWrapper, isAlpha, isBeta, isGamma, isAddAlpha,
						isAddBeta, isAddGamma, isAddSecondAlpha, isAddSecondBeta, isAddSecondGamma);
			}
			antennaParameters.add(sectorSummaryWrapper);
			sectorSummaryWrapper = null;

			sectorSummaryWrapper = getHorizontalBeamWidthMappingForSector(siteSummaryOverviewWrapper);
			if (sectorSummaryWrapper != null) {
				sectorSummaryWrapper.setIndex(10);
				sectorSummaryWrapper.setParameter(InfraConstants.HORIZONTAL_BEAMWIDTH);
				sectorSummaryWrapper = setValueForSector(sectorSummaryWrapper, isAlpha, isBeta, isGamma, isAddAlpha,
						isAddBeta, isAddGamma, isAddSecondAlpha, isAddSecondBeta, isAddSecondGamma);
			}
			antennaParameters.add(sectorSummaryWrapper);
			sectorSummaryWrapper = null;

			sectorSummaryWrapper = getVerticalBeamWidthMappingForSector(siteSummaryOverviewWrapper);
			if (sectorSummaryWrapper != null) {
				sectorSummaryWrapper.setIndex(11);
				sectorSummaryWrapper.setParameter(InfraConstants.VERTICAL_BEAMWIDTH);
				sectorSummaryWrapper = setValueForSector(sectorSummaryWrapper, isAlpha, isBeta, isGamma, isAddAlpha,
						isAddBeta, isAddGamma, isAddSecondAlpha, isAddSecondBeta, isAddSecondGamma);
			}
			antennaParameters.add(sectorSummaryWrapper);
			sectorSummaryWrapper = null;

			map.put(InfraConstants.ANTENNA_PARAMETER, antennaParameters);

		} catch (Exception exception) {
			logger.error("Exception in getting Mapping for sector Information {} ", Utils.getStackTrace(exception));

		}
		return map;
	}

	/**
	 * Gets the PCI mapping for sector.
	 *
	 * @param siteSummaryOverviewWrapper the site summary overview wrapper
	 * @return the PCI mapping for sector
	 */
	private SectorSummaryWrapper getPCIMappingForSector(SiteSummaryOverviewWrapper siteSummaryOverviewWrapper,
			SiteSummaryOverviewWrapper siteSummaryOverviewWrapperSecond) {
		SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
		try {
			if (siteSummaryOverviewWrapper.getAlphapci() != null)
				sectorSummaryWrapper.setAlpha(siteSummaryOverviewWrapper.getAlphapci().toString());
			if (siteSummaryOverviewWrapper.getBetapci() != null)
				sectorSummaryWrapper.setBeta(siteSummaryOverviewWrapper.getBetapci().toString());
			if (siteSummaryOverviewWrapper.getGammapci() != null)
				sectorSummaryWrapper.setGamma(siteSummaryOverviewWrapper.getGammapci().toString());
			if (siteSummaryOverviewWrapper.getAlphapciadd() != null)
				sectorSummaryWrapper.setAddAlpha(String.valueOf(siteSummaryOverviewWrapper.getAlphapciadd()));
			if (siteSummaryOverviewWrapper.getBetapciadd() != null)
				sectorSummaryWrapper.setAddBeta(String.valueOf(siteSummaryOverviewWrapper.getBetapciadd()));
			if (siteSummaryOverviewWrapper.getGammapciadd() != null)
				sectorSummaryWrapper.setAddGamma(String.valueOf(siteSummaryOverviewWrapper.getGammapciadd()));

			if (siteSummaryOverviewWrapperSecond.getAlphapciSecond() != null)
				sectorSummaryWrapper.setAlphaSecond(siteSummaryOverviewWrapperSecond.getAlphapciSecond().toString());
			if (siteSummaryOverviewWrapperSecond.getBetapciSecond() != null)
				sectorSummaryWrapper.setBetaSecond(siteSummaryOverviewWrapperSecond.getBetapciSecond().toString());
			if (siteSummaryOverviewWrapperSecond.getGammapciSecond() != null)
				sectorSummaryWrapper.setGammaSecond(siteSummaryOverviewWrapperSecond.getGammapciSecond().toString());
			if (siteSummaryOverviewWrapperSecond.getAlphapciaddSecond() != null)
				sectorSummaryWrapper
						.setAddAlphaSecond(String.valueOf(siteSummaryOverviewWrapperSecond.getAlphapciaddSecond()));
			if (siteSummaryOverviewWrapperSecond.getBetapciaddSecond() != null)
				sectorSummaryWrapper
						.setAddBetaSecond(String.valueOf(siteSummaryOverviewWrapperSecond.getBetapciaddSecond()));
			if (siteSummaryOverviewWrapperSecond.getGammapciaddSecond() != null)
				sectorSummaryWrapper
						.setAddGammaSecond(String.valueOf(siteSummaryOverviewWrapperSecond.getGammapciaddSecond()));
		} catch (Exception exception) {
			logger.error("Unable to get the pci sector data", Utils.getStackTrace(exception));
		}
		return sectorSummaryWrapper;
	}

	/**
	 * Gets the mech tilt mapping for sector.
	 *
	 * @param siteSummaryOverviewWrapper the site summary overview wrapper
	 * @return the mech tilt mapping for sector
	 */
	private SectorSummaryWrapper getMechTiltMappingForSector(SiteSummaryOverviewWrapper siteSummaryOverviewWrapper) {
		SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
		try {
			if (siteSummaryOverviewWrapper.getAlphaMechTilt() != null)
				sectorSummaryWrapper.setAlpha(siteSummaryOverviewWrapper.getAlphaMechTilt().toString());
			if (siteSummaryOverviewWrapper.getBetaMechTilt() != null)
				sectorSummaryWrapper.setBeta(siteSummaryOverviewWrapper.getBetaMechTilt().toString());
			if (siteSummaryOverviewWrapper.getGammaMechTilt() != null)
				sectorSummaryWrapper.setGamma(siteSummaryOverviewWrapper.getGammaMechTilt().toString());
			if (siteSummaryOverviewWrapper.getAlphaMechTiltadd() != null)
				sectorSummaryWrapper.setAddAlpha(siteSummaryOverviewWrapper.getAlphaMechTiltadd().toString());
			if (siteSummaryOverviewWrapper.getBetaMechTiltadd() != null)
				sectorSummaryWrapper.setAddBeta(siteSummaryOverviewWrapper.getBetaMechTiltadd().toString());
			if (siteSummaryOverviewWrapper.getGammaMechTiltadd() != null)
				sectorSummaryWrapper.setAddGamma(siteSummaryOverviewWrapper.getGammaMechTiltadd().toString());

		} catch (Exception exception) {
			logger.error("Unable to get the MechTilt sector data", Utils.getStackTrace(exception));
		}
		return sectorSummaryWrapper;
	}

	/**
	 * Gets the total tilt mapping for sector.
	 * 
	 * @param siteSummaryOverviewWrapper the site summary overview wrapper
	 * @return the total tilt mapping for sector
	 */
	private SectorSummaryWrapper getTotalTiltMappingForSector(SiteSummaryOverviewWrapper siteSummaryOverviewWrapper) {
		SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
		try {
			if (siteSummaryOverviewWrapper.getAlphaTotalTilt() != null)
				sectorSummaryWrapper.setAlpha(siteSummaryOverviewWrapper.getAlphaTotalTilt().toString());
			if (siteSummaryOverviewWrapper.getBetaTotalTilt() != null)
				sectorSummaryWrapper.setBeta(siteSummaryOverviewWrapper.getBetaTotalTilt().toString());
			if (siteSummaryOverviewWrapper.getGammaTotalTilt() != null)
				sectorSummaryWrapper.setGamma(siteSummaryOverviewWrapper.getGammaTotalTilt().toString());
			if (siteSummaryOverviewWrapper.getAlphaTotalTiltadd() != null)
				sectorSummaryWrapper.setAddAlpha(siteSummaryOverviewWrapper.getAlphaTotalTiltadd().toString());
			if (siteSummaryOverviewWrapper.getBetaTotalTiltadd() != null)
				sectorSummaryWrapper.setAddBeta(siteSummaryOverviewWrapper.getBetaTotalTiltadd().toString());
			if (siteSummaryOverviewWrapper.getGammaTotalTiltadd() != null)
				sectorSummaryWrapper.setAddGamma(siteSummaryOverviewWrapper.getGammaTotalTiltadd().toString());

		} catch (Exception exception) {
			logger.error("Unable to get the TotalTilt sector data", Utils.getStackTrace(exception));
		}
		return sectorSummaryWrapper;
	}

	/**
	 * Gets the elec tilt mapping for sector.
	 *
	 * @param siteSummaryOverviewWrapper the site summary overview wrapper
	 * @return the elec tilt mapping for sector
	 */
	private SectorSummaryWrapper getElecTiltMappingForSector(SiteSummaryOverviewWrapper siteSummaryOverviewWrapper) {
		SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
		try {
			if (siteSummaryOverviewWrapper.getAlphaElecTilt() != null)
				sectorSummaryWrapper.setAlpha(siteSummaryOverviewWrapper.getAlphaElecTilt().toString());
			if (siteSummaryOverviewWrapper.getBetaElecTilt() != null)
				sectorSummaryWrapper.setBeta(siteSummaryOverviewWrapper.getBetaElecTilt().toString());
			if (siteSummaryOverviewWrapper.getGammaElecTilt() != null)
				sectorSummaryWrapper.setGamma(siteSummaryOverviewWrapper.getGammaElecTilt().toString());
			if (siteSummaryOverviewWrapper.getAlphaElecTiltadd() != null)
				sectorSummaryWrapper.setAddAlpha(siteSummaryOverviewWrapper.getAlphaElecTiltadd().toString());
			if (siteSummaryOverviewWrapper.getBetaElecTiltadd() != null)
				sectorSummaryWrapper.setAddBeta(siteSummaryOverviewWrapper.getBetaElecTiltadd().toString());
			if (siteSummaryOverviewWrapper.getGammaElecTiltadd() != null)
				sectorSummaryWrapper.setAddGamma(siteSummaryOverviewWrapper.getGammaElecTiltadd().toString());
		} catch (Exception exception) {
			logger.error("Unable to get the ElecTilt sector data", Utils.getStackTrace(exception));
		}
		return sectorSummaryWrapper;
	}

	/**
	 * Gets the antenna height mapping for sector.
	 *
	 * @param siteSummaryOverviewWrapper the site summary overview wrapper
	 * @return the antenna height mapping for sector
	 */
	private SectorSummaryWrapper getAntennaHeightMappingForSector(
			SiteSummaryOverviewWrapper siteSummaryOverviewWrapper) {
		SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
		try {
			if (siteSummaryOverviewWrapper.getAlphaAntennaHeight() != null)
				sectorSummaryWrapper.setAlpha(siteSummaryOverviewWrapper.getAlphaAntennaHeight().toString());
			if (siteSummaryOverviewWrapper.getBetaAntennaHeight() != null)
				sectorSummaryWrapper.setBeta(siteSummaryOverviewWrapper.getBetaAntennaHeight().toString());
			if (siteSummaryOverviewWrapper.getGammaAntennaHeight() != null)
				sectorSummaryWrapper.setGamma(siteSummaryOverviewWrapper.getGammaAntennaHeight().toString());
			if (siteSummaryOverviewWrapper.getAlphaAntennaHeightadd() != null)
				sectorSummaryWrapper.setAddAlpha(siteSummaryOverviewWrapper.getAlphaAntennaHeightadd().toString());
			if (siteSummaryOverviewWrapper.getBetaAntennaHeightadd() != null)
				sectorSummaryWrapper.setAddBeta(siteSummaryOverviewWrapper.getBetaAntennaHeightadd().toString());
			if (siteSummaryOverviewWrapper.getGammaAntennaHeightadd() != null)
				sectorSummaryWrapper.setAddGamma(siteSummaryOverviewWrapper.getGammaAntennaHeightadd().toString());
		} catch (Exception exception) {
			logger.error("Unable to get the AntennaHeight sector data", Utils.getStackTrace(exception));
		}
		return sectorSummaryWrapper;
	}

	/**
	 * Gets the azimuth mapping for sector.
	 *
	 * @param siteSummaryOverviewWrapper the site summary overview wrapper
	 * @return the azimuth mapping for sector
	 */
	private SectorSummaryWrapper getAzimuthMappingForSector(SiteSummaryOverviewWrapper siteSummaryOverviewWrapper) {
		SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
		try {
			if (siteSummaryOverviewWrapper.getAlphaAzimuth() != null)
				sectorSummaryWrapper.setAlpha(siteSummaryOverviewWrapper.getAlphaAzimuth().toString());
			if (siteSummaryOverviewWrapper.getBetaAzimuth() != null)
				sectorSummaryWrapper.setBeta(siteSummaryOverviewWrapper.getBetaAzimuth().toString());
			if (siteSummaryOverviewWrapper.getGammaAzimuth() != null)
				sectorSummaryWrapper.setGamma(siteSummaryOverviewWrapper.getGammaAzimuth().toString());
			if (siteSummaryOverviewWrapper.getAlphaAzimuthadd() != null)
				sectorSummaryWrapper.setAddAlpha(siteSummaryOverviewWrapper.getAlphaAzimuthadd().toString());
			if (siteSummaryOverviewWrapper.getBetaAzimuthadd() != null)
				sectorSummaryWrapper.setAddBeta(siteSummaryOverviewWrapper.getBetaAzimuthadd().toString());
			if (siteSummaryOverviewWrapper.getGammaAzimuthadd() != null)
				sectorSummaryWrapper.setAddGamma(siteSummaryOverviewWrapper.getGammaAzimuthadd().toString());

		} catch (Exception exception) {
			logger.error("Unable to get the Azimuth sector data", Utils.getStackTrace(exception));
		}
		return sectorSummaryWrapper;
	}

	/**
	 * Gets the sector id mapping for sector.
	 *
	 * @param siteSummaryOverviewWrapper the site summary overview wrapper
	 * @return the sector id mapping for sector
	 */
	private SectorSummaryWrapper getSectorIdMappingForSector(SiteSummaryOverviewWrapper siteSummaryOverviewWrapper,
			SiteSummaryOverviewWrapper siteSummaryOverviewWrapperSecond) {
		SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
		try {
			if (siteSummaryOverviewWrapper.getAlphaSectorId() != null)
				sectorSummaryWrapper.setAlpha(siteSummaryOverviewWrapper.getAlphaSectorId().toString());
			if (siteSummaryOverviewWrapper.getBetaSectorId() != null)
				sectorSummaryWrapper.setBeta(siteSummaryOverviewWrapper.getBetaSectorId().toString());
			if (siteSummaryOverviewWrapper.getGammaSectorId() != null)
				sectorSummaryWrapper.setGamma(siteSummaryOverviewWrapper.getGammaSectorId().toString());
			if (siteSummaryOverviewWrapper.getAlphaSectorIdadd() != null)
				sectorSummaryWrapper.setAddAlpha(siteSummaryOverviewWrapper.getAlphaSectorIdadd().toString());
			if (siteSummaryOverviewWrapper.getBetaSectorIdadd() != null)
				sectorSummaryWrapper.setAddBeta(siteSummaryOverviewWrapper.getBetaSectorIdadd().toString());
			if (siteSummaryOverviewWrapper.getGammaSectorIdadd() != null)
				sectorSummaryWrapper.setAddGamma(siteSummaryOverviewWrapper.getGammaSectorIdadd().toString());

			if (siteSummaryOverviewWrapperSecond.getAlphaSectorIdSecond() != null)
				sectorSummaryWrapper
						.setAlphaSecond(siteSummaryOverviewWrapperSecond.getAlphaSectorIdSecond().toString());
			if (siteSummaryOverviewWrapperSecond.getBetaSectorIdSecond() != null)
				sectorSummaryWrapper.setBetaSecond(siteSummaryOverviewWrapperSecond.getBetaSectorIdSecond().toString());
			if (siteSummaryOverviewWrapperSecond.getGammaSectorIdSecond() != null)
				sectorSummaryWrapper
						.setGammaSecond(siteSummaryOverviewWrapperSecond.getGammaSectorIdSecond().toString());
			if (siteSummaryOverviewWrapperSecond.getAlphaSectorIdaddSecond() != null)
				sectorSummaryWrapper
						.setAddAlphaSecond(siteSummaryOverviewWrapperSecond.getAlphaSectorIdaddSecond().toString());
			if (siteSummaryOverviewWrapperSecond.getBetaSectorIdaddSecond() != null)
				sectorSummaryWrapper
						.setAddBetaSecond(siteSummaryOverviewWrapperSecond.getBetaSectorIdaddSecond().toString());
			if (siteSummaryOverviewWrapperSecond.getGammaSectorIdaddSecond() != null)
				sectorSummaryWrapper
						.setAddGammaSecond(siteSummaryOverviewWrapperSecond.getGammaSectorIdaddSecond().toString());
		} catch (Exception exception) {
			logger.error("Unable to get the SectorId sector data", Utils.getStackTrace(exception));
		}
		return sectorSummaryWrapper;
	}

	/**
	 * Gets the antenna type mapping for sector.
	 *
	 * @param siteSummaryOverviewWrapper the site summary overview wrapper
	 * @return the antenna type mapping for sector
	 */
	private SectorSummaryWrapper getAntennaTypeMappingForSector(SiteSummaryOverviewWrapper siteSummaryOverviewWrapper) {
		SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
		try {
			if (siteSummaryOverviewWrapper.getAlphaantennaType() != null)
				sectorSummaryWrapper.setAlpha(siteSummaryOverviewWrapper.getAlphaantennaType());
			if (siteSummaryOverviewWrapper.getBetaantennaType() != null)
				sectorSummaryWrapper.setBeta(siteSummaryOverviewWrapper.getBetaantennaType());
			if (siteSummaryOverviewWrapper.getGammaantennaType() != null)
				sectorSummaryWrapper.setGamma(siteSummaryOverviewWrapper.getGammaantennaType());
			if (siteSummaryOverviewWrapper.getAlphaantennaTypeadd() != null)
				sectorSummaryWrapper.setAddAlpha(siteSummaryOverviewWrapper.getAlphaantennaTypeadd());
			if (siteSummaryOverviewWrapper.getBetaantennaTypeadd() != null)
				sectorSummaryWrapper.setAddBeta(siteSummaryOverviewWrapper.getBetaantennaTypeadd());
			if (siteSummaryOverviewWrapper.getGammaantennaTypeadd() != null)
				sectorSummaryWrapper.setAddGamma(siteSummaryOverviewWrapper.getGammaantennaTypeadd());

		} catch (Exception exception) {
			logger.error("Unable to get the AntennaModelName sector data", Utils.getStackTrace(exception));
		}
		return sectorSummaryWrapper;
	}

	/**
	 * Gets the site overview data.
	 *
	 * @param neName the ne name
	 * @return the site overview data
	 * @throws RestException the rest exception
	 */

	@Override
	@Transactional
	public SiteGeographicalDetail getSiteOverviewData(String neName) {
		logger.info("Going to get SiteOverviewDetail data for neName {}", neName);
		SiteGeographicalDetail siteGeographicalDetail = new SiteGeographicalDetail();
		try {
			List<NEBandDetail> neBandDetailDataList = networkElementDao.getNetworkElementDataForNE(neName);
			if (neBandDetailDataList != null && neBandDetailDataList.size() > 0)
				siteGeographicalDetail = getSiteOverviewDetailWrapper(neBandDetailDataList.get(0));
			else
				logger.warn("data is not available.");
		} catch (Exception exception) {
			logger.error("Error in getting SiteOverviewDetail data for neName {} Exception {}", neName,
					Utils.getStackTrace(exception));
		}
		return siteGeographicalDetail;
	}

	/**
	 * Gets the site geographic detail data.
	 *
	 * @param neName the ne name
	 * @return the site geographic detail data
	 * @throws RestException the rest exception
	 */
	@Override
	@Transactional
	public SiteGeographicalDetail getSiteGeographicDetailData(String neName, String neType) {
		logger.info("Going to get SiteGeographicDetail data for neName {}", neName);
		SiteGeographicalDetail siteGeographicalDetail = new SiteGeographicalDetail();
		try {
			if (neType!=null && neType.equalsIgnoreCase(InfraConstants.MACRO_CELL)) {
				neType = InfraConstants.MACRO;
			}
			if (neType!=null && neType.equalsIgnoreCase(InfraConstants.IBS_CELL)) {
				neType = InfraConstants.IBS_SITE;
			}
			if (neType!=null && neType.equalsIgnoreCase(InfraConstants.ODSC_CELL)) {
				neType = InfraConstants.ODSC_SITE;
			}
			if (neType!=null && neType.equalsIgnoreCase(InfraConstants.PICO_CELL)) {
				neType = InfraConstants.PICO_SITE;
			}
			if (neType!=null && neType.equalsIgnoreCase(InfraConstants.SHOOTER_CELL)) {
				neType = InfraConstants.SHOOTER_SITE;
			}
			if (neType!=null && neType.equalsIgnoreCase(InfraConstants.GALLERY_CELL)) {
				neType = InfraConstants.GALLERY_SITE;
			}
			List<NEBandDetail> neBandDetailDataList = networkElementDao.getNetworkElementDataForNE(neName, neType);
			if (neBandDetailDataList != null && neBandDetailDataList.size() > 0)
				siteGeographicalDetail = getSiteGeographicDetailWrapper(neBandDetailDataList.get(0));
			else
				logger.warn("data is not available.");
		} catch (Exception exception) {
			logger.error("Error in getting SiteGeographicDetail data for nename {} Exception {}", neName,
					Utils.getStackTrace(exception));
		}
		return siteGeographicalDetail;
	}

	/**
	 * Gets the site summary overview by band.
	 *
	 * @param neName      the ne name
	 * @param neFrequency the ne frequency
	 * @param neStatus    the ne status
	 * @return the site summary overview by band
	 * @throws RestException the rest exception
	 */
	@Override
	public Map<String, List<SectorSummaryWrapper>> getSiteSummaryOverviewByBand(String neName, String neFrequency,
			String neStatus) {
		logger.info("Going to get Site Summary Overview detail for neName {} ,neFrequency {} ", neName, neFrequency);
		try {
			SiteSummaryOverviewWrapper overviewWrapper = new SiteSummaryOverviewWrapper();
			SiteSummaryOverviewWrapper overviewWrapperSecond = new SiteSummaryOverviewWrapper();
			overviewWrapper = iranDetailDao.getSiteSummaryOverviewByBand(neName, neFrequency, neStatus);
			overviewWrapperSecond = iranDetailDao.getSiteSummaryOverviewByBandForSecondCarrier(neName, neFrequency,
					neStatus);
			return getMappingForOverviewDetail(overviewWrapper, overviewWrapperSecond, neName, neFrequency, neStatus);
		} catch (Exception exception) {
			logger.error("Unable to get Site Summary Overview detail for neName {}, neFrequency {} ", neName,
					neFrequency);
			throw new RestException(InfraConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	/**
	 * Gets the site summary antenna parameters by band.
	 *
	 * @param neName      the ne name
	 * @param neFrequency the ne frequency
	 * @param neStatus    the ne status
	 * @return the site summary antenna parameters by band
	 * @throws RestException the rest exception
	 */
	@Override
	public Map<String, List<SectorSummaryWrapper>> getSiteSummaryAntennaParametersByBand(String neName,
			String neFrequency, String neStatus) {
		logger.info("Going to get Site Summary Antenna Parameters for neName {}, neFrequency {} ", neName, neFrequency);
		try {
			SiteSummaryOverviewWrapper overviewWrapper = new SiteSummaryOverviewWrapper();
			overviewWrapper = iranDetailDao.getAntennaParametersByBand(neName, neFrequency, neStatus);
			return getMappingForAntennaParameter(overviewWrapper, neName, neFrequency, neStatus);
		} catch (Exception exception) {
			logger.error("Unable to get Site Summary Antenna Parameters for neName {}, neFrequency {} ", neName,
					neFrequency);
			throw new RestException(InfraConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	/**
	 * Gets the site overview detail wrapper.
	 *
	 * @param macroSiteDetail the macro site detail
	 * @return the site overview detail wrapper
	 */
	private SiteGeographicalDetail getSiteOverviewDetailWrapper(NEBandDetail neBandDetailData) {
		logger.info("going to get siteoverview detail wrapper.");
		SiteGeographicalDetail siteGeographicalDetail = new SiteGeographicalDetail();
		try {
			if (neBandDetailData != null) {
				Map<String, String> map = new HashMap<>();
				List<NetworkElementWrapper> networkElementWrapperList=iranDetailDao.getMacroSiteDetailByNameAndType(neBandDetailData.getNetworkElement().getNeName(),neBandDetailData.getNetworkElement().getNeType().toString());
				if(networkElementWrapperList != null) {
					for (NetworkElementWrapper networkElementWrapper : networkElementWrapperList) {
						if(networkElementWrapper.getNeFrequency() != null) {
							if(networkElementWrapper.getSectorId() != null && (networkElementWrapper.getSectorId() == 1 || networkElementWrapper.getSectorId() == 2 || networkElementWrapper.getSectorId() == 3)) {
								if(networkElementWrapper.getOnAirDate() != null)
									map.put(InfraConstants.ONAIRDATE+ForesightConstants.UNDERSCORE+networkElementWrapper.getNeFrequency(), InfraUtils.getSiteTaskDateForSectorProperty(networkElementWrapper.getOnAirDate(),true));
								if(networkElementWrapper.getBandwidth() != null)
									map.put(InfraConstants.BANDWIDTH+ForesightConstants.UNDERSCORE+networkElementWrapper.getNeFrequency(), getFormattedBandWidthForSites(networkElementWrapper.getBandwidth()));
								if(networkElementWrapper.getNeStatus() != null)
									map.put(InfraConstants.PROGRESSSTATE_CAP+ForesightConstants.UNDERSCORE+networkElementWrapper.getNeFrequency(), networkElementWrapper.getNeStatus().toString());

							}
							if(networkElementWrapper.getSectorId() != null && (networkElementWrapper.getSectorId() == 4 || networkElementWrapper.getSectorId() == 5 || networkElementWrapper.getSectorId() == 6)) {
								if(networkElementWrapper.getOnAirDateFourthSector() != null)
									map.put(InfraConstants.FOURTHSECTOR + ForesightConstants.UNDERSCORE+InfraConstants.ONAIRDATE+ForesightConstants.UNDERSCORE+networkElementWrapper.getNeFrequency(), InfraUtils.getSiteTaskDateForSectorProperty(networkElementWrapper.getOnAirDateFourthSector(),true));
								if(networkElementWrapper.getBandwidthFourthSector() != null)
									map.put(InfraConstants.FOURTHSECTOR +ForesightConstants.UNDERSCORE+ InfraConstants.BANDWIDTH+ForesightConstants.UNDERSCORE+networkElementWrapper.getNeFrequency(), getFormattedBandWidthForSites(networkElementWrapper.getBandwidthFourthSector()));
								if(networkElementWrapper.getNeStatusFourthSector() != null)
									map.put(InfraConstants.FOURTHSECTOR + ForesightConstants.UNDERSCORE + InfraConstants.PROGRESSSTATE_CAP+ForesightConstants.UNDERSCORE+networkElementWrapper.getNeFrequency(), networkElementWrapper.getNeStatusFourthSector().toString());

							}

						}
					}
					siteGeographicalDetail.setSectorMap(map);
				}
			}
			return siteGeographicalDetail;
		} catch (Exception exception) {
			logger.error("Error in adding SiteOverviewDetail parameters in wrapper Message {}", exception.getMessage());
		}
		return siteGeographicalDetail;
	}

	/**
	 * Gets the site geographic detail wrapper.
	 *
	 * @param macroSiteDetail the macro site detail
	 * @return the site geographic detail wrapper
	 */
	private SiteGeographicalDetail getSiteGeographicDetailWrapper(NEBandDetail neBandDetailData) {
		SiteGeographicalDetail siteGeographicalDetail = new SiteGeographicalDetail();
		try {
			if (neBandDetailData != null) {
				if (neBandDetailData.getNetworkElement() != null) {
					siteGeographicalDetail.setSiteId(neBandDetailData.getNetworkElement().getNeName());
					if (neBandDetailData.getNetworkElement().getGeographyL4() != null) {
						siteGeographicalDetail.setGeographyL4(neBandDetailData.getNetworkElement().getGeographyL4().getDisplayName());
						siteGeographicalDetail.setGeographyL4Code(neBandDetailData.getNetworkElement().getGeographyL4().getCode());
						siteGeographicalDetail.setDisplayNameL4(neBandDetailData.getNetworkElement().getGeographyL4().getDisplayName());
						if (neBandDetailData.getNetworkElement().getGeographyL4().getGeographyL3() != null) {
							siteGeographicalDetail.setGeographyL3(neBandDetailData.getNetworkElement().getGeographyL4().getGeographyL3().getDisplayName());
							siteGeographicalDetail.setGeographyL3Code(neBandDetailData.getNetworkElement().getGeographyL4().getGeographyL3().getCode());
							siteGeographicalDetail.setDisplayNameL3(neBandDetailData.getNetworkElement().getGeographyL4().getGeographyL3().getDisplayName());
							if (neBandDetailData.getNetworkElement().getGeographyL4().getGeographyL3()
									.getGeographyL2() != null) {
								siteGeographicalDetail.setGeographyL2(neBandDetailData.getNetworkElement().getGeographyL4().getGeographyL3().getGeographyL2().getDisplayName());
								siteGeographicalDetail.setDisplayNameL2(neBandDetailData.getNetworkElement().getGeographyL4()
										.getGeographyL3().getGeographyL2().getDisplayName());
								if (neBandDetailData.getNetworkElement().getGeographyL4().getGeographyL3()
										.getGeographyL2().getGeographyL1() != null) {
									siteGeographicalDetail.setGeographyL1(neBandDetailData.getNetworkElement().getGeographyL4().getGeographyL3().getGeographyL2().getGeographyL1().getDisplayName());
									siteGeographicalDetail.setDisplayNameL1(neBandDetailData.getNetworkElement().getGeographyL4()
											.getGeographyL3().getGeographyL2().getGeographyL1().getDisplayName());
								}
							}
						}
					}
				}
				if(neBandDetailData.getNeDetail()!=null) {
					if (neBandDetailData.getNeDetail().getMorphology() != null) {
						if (neBandDetailData.getNeDetail().getMorphology().equalsIgnoreCase(InfraConstants.DU_CATEGORY)) {
							siteGeographicalDetail.setMorphologyName(InfraConstants.DENSE_URBAN);
						} else if (neBandDetailData.getNeDetail().getMorphology().equalsIgnoreCase(InfraConstants.UR_CATEGORY)) {
							siteGeographicalDetail.setMorphologyName(InfraConstants.URBAN);
						} else if (neBandDetailData.getNeDetail().getMorphology().equalsIgnoreCase(InfraConstants.SU_CATEGORY)) {
							siteGeographicalDetail.setMorphologyName(InfraConstants.SUB_URBAN);
						} else if (neBandDetailData.getNeDetail().getMorphology().equalsIgnoreCase(InfraConstants.RU_CATEGORY)) {
							siteGeographicalDetail.setMorphologyName(InfraConstants.RURAL);
						} else if (neBandDetailData.getNeDetail().getMorphology().equalsIgnoreCase(InfraConstants.IN_CATEGORY)) {
							siteGeographicalDetail.setMorphologyName(InfraConstants.INDUSTRIAL);
						} else if (neBandDetailData.getNeDetail().getMorphology().equalsIgnoreCase(InfraConstants.WB_CATEGORY)) {
							siteGeographicalDetail.setMorphologyName(InfraConstants.WATERBODY);
						} else if (neBandDetailData.getNeDetail().getMorphology().equalsIgnoreCase(InfraConstants.RO_CATEGORY)) {
							siteGeographicalDetail.setMorphologyName(InfraConstants.ROAD);
						}
					}
				}
			}
			return siteGeographicalDetail;
		} catch (Exception exception) {
			logger.error("Error in adding SiteGeographicDetail parameters in wrapper Message {}",
					exception.getMessage());
		}
		return siteGeographicalDetail;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<NetworkElement> getNetworkelementByGeographyLevel(SiteSelectionWrapper siteSelectionWrapper) {
		List<NetworkElement> networkElementList = new ArrayList<>();
		List<NEType> neTypeList = InfraUtils.convertListElementToEnum(siteSelectionWrapper.getNeTypeList(),
				InfraConstants.NETYPE_ENUM_KEY);
		List<NEStatus> neStatusList = InfraUtils.convertListElementToEnum(siteSelectionWrapper.getNeStatusList(),
				InfraConstants.NESTATUS_ENUM_KEY);
		Map<String, List<String>> geographyLevels = siteSelectionWrapper.getGeographyLevels();
		try {
			return networkElementDao.getNetworkElementsForSiteLevelDetail(neTypeList, null, null, neStatusList, null,
					null, null, null, geographyLevels);
		} catch (Exception exception) {
			logger.error("Unable to get NetworkElement by Geography Exception : {} ", Utils.getStackTrace(exception));
		}
		return networkElementList;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<NetworkElement> getSitesByGeography(SiteSelectionWrapper siteSelectionWrapper) {
		List<NetworkElement> networkElementList = new ArrayList<>();
		List<NEType> neTypeList = InfraUtils.convertListElementToEnum(siteSelectionWrapper.getNeTypeList(),
				InfraConstants.NETYPE_ENUM_KEY);
		List<NEStatus> neStatusList = InfraUtils.convertListElementToEnum(siteSelectionWrapper.getNeStatusList(),
				InfraConstants.NESTATUS_ENUM_KEY);
		Map<String, List<String>> geographyLevels = siteSelectionWrapper.getGeographyLevels();
		try {
			return networkElementDao.getNetworkElementsForSiteLevelDetail(neTypeList, null, null, neStatusList, null,
					null, null, null, geographyLevels);
		} catch (Exception exception) {
			logger.error("Unable to get NetworkElement by Geography Exception : {} ", Utils.getStackTrace(exception));
		}
		return networkElementList;
	}

	@Override
	public Map<String, Long> getsiteCountsForKPI(Double southWestLong, Double southWestLat, Double northEastLong,
			Double northEastLat, Integer zoomLevel, KPISummaryDataWrapper filterConfiguration) {
		try {
			logger.info("Going to get the site counts.");
			Map<String, Long> mapForSiteCounts = new HashMap<>();
			if (southWestLong != null && northEastLong != null && northEastLat != null && southWestLat != null
					&& filterConfiguration != null && zoomLevel != null) {
				mapForSiteCounts = getSiteCountsForKPI(southWestLong, southWestLat, northEastLong, northEastLat,
						filterConfiguration, zoomLevel);
			}
			return mapForSiteCounts;
		} catch (Exception exception) {
			logger.error("Exception in  getsiteCountsForKPI  {} ", ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to find data");
		}
	}

	private Map<String, Long> getSiteCountsForKPI(Double southWestLong, Double southWestLat, Double northEastLong,
			Double northEastLat, KPISummaryDataWrapper filterConfiguration, Integer zoomLevel) {
		Map<String, Long> mapForSiteCounts = new HashMap<>();
		Map kpiMap = filterConfiguration.getKpiMap();
		try {
			HashMap<String, Double> sizeWiseLatLongMap = getLargeAndSmallLatLong(southWestLong, southWestLat,
					northEastLong, northEastLat);
			Double largeLat = sizeWiseLatLongMap.get(ForesightConstants.LARGE_LAT);
			Double largeLong = sizeWiseLatLongMap.get(ForesightConstants.LARGE_LNG);
			Double smallLat = sizeWiseLatLongMap.get(ForesightConstants.SMALL_LAT);
			Double smallLong = sizeWiseLatLongMap.get(ForesightConstants.SMALL_LNG);
			try {
				logger.info("largeLat :{} largeLong : {} smallLat : {} smallLong :{}", largeLat, largeLong, smallLat,
						smallLong);
				if (kpiMap.get(ForesightConstants.SITE_TYPE) != null) {

					if (kpiMap.get(ForesightConstants.SITE_TYPE).toString().equalsIgnoreCase(InfraConstants.MACRO)) {
						mapForSiteCounts = getMapForSiteCounts(zoomLevel, smallLat, largeLat, smallLong, largeLong,
								kpiMap, NEType.MACRO);
					}
					if (kpiMap.get(ForesightConstants.SITE_TYPE).toString()
							.equalsIgnoreCase(InfraConstants.MACRO_CELL)) {
						mapForSiteCounts = getMapForSiteCounts(zoomLevel, smallLat, largeLat, smallLong, largeLong,
								kpiMap, NEType.MACRO_CELL);
					}

					if (kpiMap.get(ForesightConstants.SITE_TYPE).toString()
							.equalsIgnoreCase(InfraConstants.ODSC_SITE)) {
						mapForSiteCounts = getMapForSiteCounts(zoomLevel, smallLat, largeLat, smallLong, largeLong,
								kpiMap, NEType.ODSC_SITE);
					}
					if (kpiMap.get(ForesightConstants.SITE_TYPE).toString()
							.equalsIgnoreCase(InfraConstants.ODSC_CELL)) {
						mapForSiteCounts = getMapForSiteCounts(zoomLevel, smallLat, largeLat, smallLong, largeLong,
								kpiMap, NEType.ODSC_CELL);
					}
					if (kpiMap.get(ForesightConstants.SITE_TYPE).toString()
							.equalsIgnoreCase(InfraConstants.IDSC_SITE)) {
						mapForSiteCounts = getMapForSiteCounts(zoomLevel, smallLat, largeLat, smallLong, largeLong,
								kpiMap, NEType.IDSC_SITE);
					}
					if (kpiMap.get(ForesightConstants.SITE_TYPE).toString().equalsIgnoreCase(InfraConstants.IBS_SITE)) {
						mapForSiteCounts = getMapForSiteCounts(zoomLevel, smallLat, largeLat, smallLong, largeLong,
								kpiMap, NEType.IBS_SITE);
					}
					if (kpiMap.get(ForesightConstants.SITE_TYPE).toString().equalsIgnoreCase(InfraConstants.IBS_CELL)) {
						mapForSiteCounts = getMapForSiteCounts(zoomLevel, smallLat, largeLat, smallLong, largeLong,
								kpiMap, NEType.IBS_CELL);
					}
					if (kpiMap.get(ForesightConstants.SITE_TYPE).toString()
							.equalsIgnoreCase(InfraConstants.PICO_SITE)) {
						mapForSiteCounts = getMapForSiteCounts(zoomLevel, smallLat, largeLat, smallLong, largeLong,
								kpiMap, NEType.PICO_SITE);
					}
					if (kpiMap.get(ForesightConstants.SITE_TYPE).toString()
							.equalsIgnoreCase(InfraConstants.PICO_CELL)) {
						mapForSiteCounts = getMapForSiteCounts(zoomLevel, smallLat, largeLat, smallLong, largeLong,
								kpiMap, NEType.PICO_CELL);
					}
					if (kpiMap.get(ForesightConstants.SITE_TYPE).toString()
							.equalsIgnoreCase(InfraConstants.SHOOTER_SITE)) {
						mapForSiteCounts = getMapForSiteCounts(zoomLevel, smallLat, largeLat, smallLong, largeLong,
								kpiMap, NEType.SHOOTER_SITE);
					}
					if (kpiMap.get(ForesightConstants.SITE_TYPE).toString()
							.equalsIgnoreCase(InfraConstants.SHOOTER_CELL)) {
						mapForSiteCounts = getMapForSiteCounts(zoomLevel, smallLat, largeLat, smallLong, largeLong,
								kpiMap, NEType.SHOOTER_CELL);
					}
					if (kpiMap.get(ForesightConstants.SITE_TYPE).toString().equalsIgnoreCase(InfraConstants.WIFI)) {
						mapForSiteCounts = getMapForSiteCounts(zoomLevel, smallLat, largeLat, smallLong, largeLong,
								kpiMap, NEType.WIFI);
					}
				}
			} catch (Exception exception) {
				logger.error("unable to get site count. Exception : {} ", Utils.getStackTrace(exception));
			}
		} catch (Exception exception) {
			logger.error("Unable to get Viewport. Exception : {} ", Utils.getStackTrace(exception));
		}
		return mapForSiteCounts;
	}

	/**
	 * Gets the large and small latlong.
	 *
	 * @param southWestLong the south west long
	 * @param southWestLat  the south west lat
	 * @param northEastLong the north east long
	 * @param northEastLat  the north east lat
	 * @return the large and small lat long
	 */
	private HashMap<String, Double> getLargeAndSmallLatLong(Double southWestLong, Double southWestLat,
			Double northEastLong, Double northEastLat) {
		if (southWestLong != null && southWestLat != null && northEastLong != null && northEastLat != null) {
			Double largeLat = null;
			Double largeLong = null;
			Double smallLat = null;
			Double smallLong = null;
			if (southWestLong > northEastLong) {
				largeLong = southWestLong;
				smallLong = northEastLong;
			} else {
				largeLong = northEastLong;
				smallLong = southWestLong;
			}
			if (southWestLat > northEastLat) {
				largeLat = southWestLat;
				smallLat = northEastLat;
			} else {
				largeLat = northEastLat;
				smallLat = southWestLat;
			}
			HashMap<String, Double> sizeWiseLatLongMap = new HashMap<>();
			sizeWiseLatLongMap.put(ForesightConstants.LARGE_LAT, largeLat);
			sizeWiseLatLongMap.put(ForesightConstants.LARGE_LNG, largeLong);
			sizeWiseLatLongMap.put(ForesightConstants.SMALL_LAT, smallLat);
			sizeWiseLatLongMap.put(ForesightConstants.SMALL_LNG, smallLong);
			return sizeWiseLatLongMap;
		} else {
			return new HashMap<>();
		}
	}

	private Map<String, Long> getMapForSiteCounts(Integer zoomLevel, Double smallLat, Double largeLat, Double smallLong,
			Double largeLong, Map kpiMap, NEType neType) {
		Map<String, Long> mapForSiteCounts = new HashMap<>();
		List<NetworkElementWrapper> networkElementDataList = new ArrayList<>();
		List<Vendor> vendors = new ArrayList<>();
		List<Technology> technologies = new ArrayList<>();
		try {
			if (kpiMap.get(InfraConstants.BANDS) != null && kpiMap.get(ForesightConstants.PROGRESS_STATE) != null
					&& kpiMap.get(ForesightConstants.DISPLAY_SITES) != null) {
				List<String> neFrequencyList = (List<String>) kpiMap.get(InfraConstants.BANDS);
				List<String> morphologyList = new ArrayList<>();
				String neStatus = (String) kpiMap.get(ForesightConstants.PROGRESS_STATE);
				Integer displaySite = Integer.valueOf(kpiMap.get(ForesightConstants.DISPLAY_SITES).toString());
				String criteria = null;
				String failureRate = null;
				if (kpiMap.get(InfraConstants.CRITERIA_KEY) != null)
					criteria = kpiMap.get(InfraConstants.CRITERIA_KEY).toString();
				if (kpiMap.get(InfraConstants.FAILURE_RATE_KEY) != null)
					failureRate = kpiMap.get(InfraConstants.FAILURE_RATE_KEY).toString();
				if (kpiMap.get(InfraConstants.SITE_VENDORS) != null)
					vendors = getVendorList(kpiMap);
				if (kpiMap.get(InfraConstants.NE_TECHNOLOGY_KEY) != null)
					technologies = getTechnologyList(kpiMap);
				if (kpiMap.get("Morphology") != null)
					morphologyList = (List<String>) kpiMap.get("Morphology");
				if (zoomLevel < displaySite) {
					String geographyLevel = getGeographyForAggregation(zoomLevel);
					List<String> geographyList = getDistinctGeography(geographyLevel, smallLong, smallLat, largeLong,
							largeLat);
					if (geographyList != null && !(geographyList.isEmpty())) {
						if (neStatus.equalsIgnoreCase(InfraConstants.ONAIR)
								|| neStatus.equalsIgnoreCase(InfraConstants.PLANNED)) {
							logger.info(
									"Going to get counts For geographyLevel {} status {} neFrequency {} neTypes {} ",
									geographyLevel, neStatus, neFrequencyList, neType);
							networkElementDataList = networkElementDao.getNetworkElementForAggregateLayerCount(
									neType.name(), neStatus, smallLong, smallLat, largeLong, largeLat, geographyLevel,
									neFrequencyList, null, null, vendors, technologies, geographyList, morphologyList);
							mapForSiteCounts = getAggregatedCountsForKpi(networkElementDataList);
						} else if (neStatus.equalsIgnoreCase(InfraConstants.OFFAIR)) {
							List<NetworkElementWrapper> networkElementData = new ArrayList<>();
							if (kpiMap.get(InfraConstants.ISNONRADIATING) != null) {
								Boolean isNonradiating = (Boolean) kpiMap.get(InfraConstants.ISNONRADIATING);
								if (isNonradiating) {
									networkElementData = networkElementDao.getNetworkElementForAggregateLayerCount(
											neType.name(), InfraConstants.NONRADIATING, smallLong, smallLat, largeLong,
											largeLat, geographyLevel, neFrequencyList, null, null, vendors,
											technologies, geographyList, morphologyList);
									networkElementDataList.addAll(networkElementData);
								}
							}
							if (kpiMap.get(ForesightConstants.IS_DECOMMISSIONED) != null) {
								Boolean isDecommissioned = (Boolean) kpiMap.get(ForesightConstants.IS_DECOMMISSIONED);
								if (isDecommissioned) {
									networkElementData = networkElementDao.getNetworkElementForAggregateLayerCount(
											neType.name(), InfraConstants.DECOMMISSIONED, smallLong, smallLat,
											largeLong, largeLat, geographyLevel, neFrequencyList, null, null, vendors,
											technologies, geographyList, morphologyList);
									networkElementDataList.addAll(networkElementData);
								}
							}
							mapForSiteCounts = getAggregatedCountsForKpi(networkElementDataList, null);
						}
					} else {
						Long totalCount = 0L;
						mapForSiteCounts.put(InfraConstants.TOTAL_SITE_COUNTS, totalCount);
					}
				} else {
					if (neStatus.equalsIgnoreCase(InfraConstants.PLANNED)) {
						networkElementDataList = networkElementDao.getActualPlannedSites(neType.name(), neStatus,
								smallLong, smallLat, largeLong, largeLat, neFrequencyList, null, null, vendors,
								technologies, morphologyList);
						mapForSiteCounts = getAggregatedSiteCountsForKpi(networkElementDataList);
					} else if (neStatus.equalsIgnoreCase(InfraConstants.OFFAIR)) {
						if (kpiMap.get(InfraConstants.ISNONRADIATING) != null) {
							Boolean isNonradiating = (Boolean) kpiMap.get(InfraConstants.ISNONRADIATING);
							if (isNonradiating) {
								networkElementDataList = networkElementDao.getNetworkElementForActualSites(
										neType.name(), InfraConstants.NONRADIATING, smallLong, smallLat, largeLong,
										largeLat, neFrequencyList, null, null, criteria, failureRate, vendors,
										technologies, morphologyList, null);
							}
						}
						if (kpiMap.get(ForesightConstants.IS_DECOMMISSIONED) != null) {
							Boolean isDecommissioned = (Boolean) kpiMap.get(ForesightConstants.IS_DECOMMISSIONED);
							if (isDecommissioned) {
								networkElementDataList = networkElementDao.getNetworkElementForActualSites(
										neType.name(), InfraConstants.DECOMMISSIONED, smallLong, smallLat, largeLong,
										largeLat, neFrequencyList, null, null, criteria, failureRate, vendors,
										technologies, morphologyList, null);
							}
						}
						mapForSiteCounts = getAggregatedCountsForOffairSites(networkElementDataList);
					} else {
						networkElementDataList = networkElementDao.getNetworkElementForActualSites(neType.name(),
								neStatus, smallLong, smallLat, largeLong, largeLat, neFrequencyList, null, null,
								criteria, failureRate, vendors, technologies, morphologyList, null);
						mapForSiteCounts = getAggregatedSiteCountsForKpi(networkElementDataList);
					}
				}

			}
		} catch (Exception exception) {
			logger.error("Unable to get map for site counts. Exception : {} ", Utils.getStackTrace(exception));
		}
		return mapForSiteCounts;
	}

	@Override
	public Map getSiteCountForLayerVisualisation(SiteSelectionWrapper siteSelectionWrapper, Double southWestLong,
			Double southWestLat, Double northEastLong, Double northEastLat, Integer zoomLevel) {
		Map sitesVisualisationMap = new HashMap<>();
		try {
			logger.info("Going to get count for SiteLayer where SiteSelectionWrapper : {} and zoomLevel : {} ",
					siteSelectionWrapper, zoomLevel);
			List<NetworkElementWrapper> sitesCountList = new ArrayList<>();
			List<NetworkElementWrapper> sitesListGeographyWise = new ArrayList<>();
			List<NetworkElementWrapper> actualSiteList = new ArrayList<>();
			List<NetworkElementWrapper> sidePanelList = new ArrayList<>();
			Map<String, List<NetworkElementWrapper>> sitesVisualizationMap = new HashMap<>();
			if (siteSelectionWrapper != null) {
				sitesVisualizationMap = getSitesCountByNEParameter(siteSelectionWrapper, southWestLong, southWestLat,
						northEastLong, northEastLat, zoomLevel, InfraConstants.VISUALISATION);
				logger.info("Size of sitesVisualizationMap {}", sitesVisualizationMap.size());
				if (sitesVisualizationMap != null && !sitesVisualizationMap.isEmpty()) {
					sitesCountList = sitesVisualizationMap.get(InfraConstants.SITES_COUNT_IN_NETWORK);
					sidePanelList = getAggregatedCountsForSites(siteSelectionWrapper, sitesCountList);
					sitesListGeographyWise = getFilteredCountGeographyWise(sitesCountList);
					actualSiteList = sitesVisualizationMap.get(InfraConstants.SITES_VISUALIZE_IN_NETWORK);
					if (actualSiteList != null && !actualSiteList.isEmpty()) {
						sidePanelList.addAll(getAggregatedCountsForSites(siteSelectionWrapper, actualSiteList));
						logger.info("Size of sitesListGeographyWise {},sidePanelList {},actualSiteList {} ",
								sitesListGeographyWise.size(), sidePanelList.size(), actualSiteList.size());
					}
					sitesVisualisationMap = getMapForSiteLayer(sitesListGeographyWise, sidePanelList, actualSiteList);
				} else {
					throw new RestException(ForesightConstants.INVALID_PARAMETERS);
				}
			}
		} catch (IllegalArgumentException | NullPointerException exception) {
			logger.error("Unable to get Sites for Visualisation due to Exception  {}", Utils.getStackTrace(exception));
		} catch (Exception exception) {
			logger.error("Unable to get Site Count for layer {}", Utils.getStackTrace(exception));
		}
		logger.info(" SiteLayer Map For Visualization and Count : {}", sitesVisualisationMap.size());
		return sitesVisualisationMap;
	}

	private NEStatus getProgressStateForSiteLayer(Map map) {
		try {
			if (map != null && !map.isEmpty()) {
				if (map.get(InfraConstants.LAYER_PROGRESSSTATE) != null) {
					return NEStatus.valueOf(map.get(InfraConstants.LAYER_PROGRESSSTATE).toString());
				}
			}
		} catch (Exception exception) {
			logger.error("Unable to  ProgresssState for Sites and exception occurs  : {} ",
					Utils.getStackTrace(exception));
		}
		return null;
	}

	private List<NetworkElementWrapper> getFilteredCountGeographyWise(List<NetworkElementWrapper> filteredList) {
		List<NetworkElementWrapper> sitesListGeographyWise = new ArrayList<>();
		try {
			Map<String, List<NetworkElementWrapper>> map = filteredList.stream()
					.collect(Collectors.groupingBy(NetworkElementWrapper::getGeographyName, Collectors.toList()));
			map.forEach((k, v) -> {
				try {
					if (v != null && !v.isEmpty()) {
						Long totalCount = v.stream().mapToLong(nw -> nw.getTotalCount()).sum();
						NetworkElementWrapper elementWrapper = new NetworkElementWrapper();
						if (v.get(0) != null) {
							elementWrapper = v.get(0);
							elementWrapper.setTotalCount(totalCount);
							sitesListGeographyWise.add(elementWrapper);
						}
					}
				} catch (Exception exception) {
					logger.error("Error occur while getting sitesListGeographyWise {}", Utils.getStackTrace(exception));
				}
			});
		} catch (Exception exception) {
			logger.error("Unable to Filter Count Zone wise from list {}", Utils.getStackTrace(exception));
		}
		return sitesListGeographyWise;
	}

	private Map getMapForSiteLayer(List<NetworkElementWrapper> siteListGeographyWise,
			List<NetworkElementWrapper> siteTotalCountStatusWise, List<NetworkElementWrapper> actualSiteList) {
		Map map = new HashMap<>();
		try {
			if (siteListGeographyWise != null && !siteListGeographyWise.isEmpty()) {
				map.put(InfraConstants.AGGREGATED_LAYER_COUNT_KEY, siteListGeographyWise);
			}
			if (siteTotalCountStatusWise != null && !siteTotalCountStatusWise.isEmpty()) {
				map.put(InfraConstants.SITES_COUNT_IN_NETWORK, siteTotalCountStatusWise);
			}
			if (actualSiteList != null && !actualSiteList.isEmpty()) {
				map.put(InfraConstants.SITE_VISUALISATION_KEY, actualSiteList);
			}
		} catch (Exception exception) {
			logger.error("Unable to get map for Site Layer {}", Utils.getStackTrace(exception));
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	private Map<String, List<NetworkElementWrapper>> mergeMapByKey(String key, Map map, List list) {
		try {
			if (key != null && map != null && !map.isEmpty() && !list.isEmpty() && list != null) {
				map.merge(key, list, (l1, l2) -> {
					List tempList = new ArrayList<>((Collection) l1);
					tempList.addAll((Collection) l2);
					return tempList;
				});
			}
		} catch (Exception exception) {
			logger.error("Unable merge list by map key");
		}
		return map;
	}

	private NEType getNETypes(Map map) {
		NEType neType = null;
		try {
			if (map != null && !map.isEmpty()) {
				if (map.get(InfraConstants.SITETYPE) != null) {
					if (map.get(InfraConstants.SITETYPE).toString().equals(NEType.MACRO_CELL.name())) {
						neType = NEType.MACRO_CELL;
					}
					if (map.get(InfraConstants.SITETYPE).toString().equals(NEType.MACRO.name())) {
						neType = NEType.MACRO;
					}
					if (map.get(InfraConstants.SITETYPE).toString().equals(NEType.PICO_SITE.name())) {
						neType = NEType.PICO_SITE;
					}
					if (map.get(InfraConstants.SITETYPE).toString().equals(NEType.PICO_CELL.name())) {
						neType = NEType.PICO_CELL;
					}
					if (map.get(InfraConstants.SITETYPE).toString().equals(NEType.SHOOTER_SITE.name())) {
						neType = NEType.SHOOTER_SITE;
					}
					if (map.get(InfraConstants.SITETYPE).toString().equals(NEType.SHOOTER_CELL.name())) {
						neType = NEType.SHOOTER_CELL;
					}
					if (map.get(InfraConstants.SITETYPE).toString().equals(NEType.MACRO_CELL.name())) {
						neType = NEType.MACRO_CELL;
					} else if (map.get(InfraConstants.SITETYPE).toString().equals(NEType.ODSC_CELL.name())) {
						neType = NEType.ODSC_CELL;
					} else if (map.get(InfraConstants.SITETYPE).toString().equals(NEType.ODSC_SITE.name())) {
						neType = NEType.ODSC_SITE;
					} else if (map.get(InfraConstants.SITETYPE).toString().equals(NEType.IBS_SITE.name())) {
						neType = NEType.IBS_SITE;
					} else if (map.get(InfraConstants.SITETYPE).toString().equals(NEType.IBS_CELL.name())) {
						neType = NEType.IBS_CELL;
					}

				}
			}
			return neType;
		} catch (Exception exception) {
			logger.error("Unable to get NEType for Sites {}", Utils.getStackTrace(exception));
		}
		return neType;
	}

	private Map<String, List<SectorSummaryWrapper>> getMappingForRadioParameter(
			SiteSummaryOverviewWrapper siteSummaryOverviewWrapper, String neName, String neFrequency, String neStatus) {
		Map<String, List<SectorSummaryWrapper>> map = new HashMap<>();
		try {
			List<SectorSummaryWrapper> radioParameters = new ArrayList<>();
			SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
			Boolean isAlpha = false;
			Boolean isBeta = false;
			Boolean isGamma = false;
			Boolean isAddAlpha = false;
			Boolean isAddBeta = false;
			Boolean isAddGamma = false;
			Boolean isAddSecondAlpha = false;
			Boolean isAddSecondBeta = false;
			Boolean isAddSecondGamma = false;
			List<Integer> sectorIdList = new ArrayList<>();
			try {
				sectorIdList = macroSiteDetailDao.getSectorIdForSiteOverViewDetails(neName, neFrequency, neStatus,
						ForesightConstants.FIRST);

			} catch (Exception exception) {
				logger.warn("Unable to get Fourth Sector Data for SiteOverview Details Message {} ",
						exception.getMessage());
			}
			if (sectorIdList != null && !sectorIdList.isEmpty()) {
				for (Integer sector : sectorIdList) {
					if (sector == ForesightConstants.INTEGER_SECTOR_4) {
						isAlpha = true;
					}
					if (sector == ForesightConstants.INTEGER_SECTOR_5) {
						isBeta = true;
					}
					if (sector == ForesightConstants.INTEGER_SECTOR_6) {
						isGamma = true;
					}
				}
			}
			try {
				sectorIdList = macroSiteDetailDao.getSectorIdForSiteOverViewDetails(neName, neFrequency, neStatus,
						ForesightConstants.SECOND);

			} catch (Exception exception) {
				logger.warn("Unable to get Fourth Sector Data for SiteOverview Details Message {} ",
						exception.getMessage());
			}

			if (sectorIdList != null && !sectorIdList.isEmpty()) {
				for (Integer sector : sectorIdList) {
					if (sector == ForesightConstants.INTEGER_SECTOR_1) {
						isAddSecondAlpha = true;
					}
					if (sector == ForesightConstants.INTEGER_SECTOR_2) {
						isAddSecondBeta = true;
					}
					if (sector == ForesightConstants.INTEGER_SECTOR_3) {
						isAddSecondGamma = true;
					}
					if (sector == ForesightConstants.INTEGER_SECTOR_4) {
						isAddAlpha = true;
					}
					if (sector == ForesightConstants.INTEGER_SECTOR_5) {
						isAddBeta = true;
					}
					if (sector == ForesightConstants.INTEGER_SECTOR_6) {
						isAddGamma = true;
					}
				}
			}
			sectorSummaryWrapper = getClutterCategoryMappingForSector(siteSummaryOverviewWrapper);
			if (sectorSummaryWrapper != null) {
				sectorSummaryWrapper.setIndex(1);
				sectorSummaryWrapper.setParameter(InfraConstants.CLUTTER_CATEGORY);
				sectorSummaryWrapper = setValueForSector(sectorSummaryWrapper, isAlpha, isBeta, isGamma, isAddAlpha,
						isAddBeta, isAddGamma, isAddSecondAlpha, isAddSecondBeta, isAddSecondGamma);
			}
			radioParameters.add(sectorSummaryWrapper);
			sectorSummaryWrapper = null;

			sectorSummaryWrapper = getPropagationModelMappingForSector(siteSummaryOverviewWrapper);
			if (sectorSummaryWrapper != null) {
				sectorSummaryWrapper.setIndex(2);
				sectorSummaryWrapper.setParameter(InfraConstants.PROPAGATION_MODEL);
				sectorSummaryWrapper = setValueForSector(sectorSummaryWrapper, isAlpha, isBeta, isGamma, isAddAlpha,
						isAddBeta, isAddGamma, isAddSecondAlpha, isAddSecondBeta, isAddSecondGamma);
			}
			radioParameters.add(sectorSummaryWrapper);
			sectorSummaryWrapper = null;

			sectorSummaryWrapper = getTxPowerMappingForSector(siteSummaryOverviewWrapper);
			if (sectorSummaryWrapper != null) {
				sectorSummaryWrapper.setIndex(3);
				sectorSummaryWrapper.setParameter(InfraConstants.TX_POWER);
				sectorSummaryWrapper = setValueForSector(sectorSummaryWrapper, isAlpha, isBeta, isGamma, isAddAlpha,
						isAddBeta, isAddGamma, isAddSecondAlpha, isAddSecondBeta, isAddSecondGamma);
			}
			radioParameters.add(sectorSummaryWrapper);
			sectorSummaryWrapper = null;

			sectorSummaryWrapper = getEirpMappingForSector(siteSummaryOverviewWrapper);
			if (sectorSummaryWrapper != null) {
				sectorSummaryWrapper.setIndex(4);
				sectorSummaryWrapper.setParameter(InfraConstants.EIRP);
				sectorSummaryWrapper = setValueForSector(sectorSummaryWrapper, isAlpha, isBeta, isGamma, isAddAlpha,
						isAddBeta, isAddGamma, isAddSecondAlpha, isAddSecondBeta, isAddSecondGamma);
			}
			radioParameters.add(sectorSummaryWrapper);
			sectorSummaryWrapper = null;

			sectorSummaryWrapper = getPilotChannelTxPowerMappingForSector(siteSummaryOverviewWrapper);
			if (sectorSummaryWrapper != null) {
				sectorSummaryWrapper.setIndex(5);
				sectorSummaryWrapper.setParameter(InfraConstants.PILOT_CHANNEL_TXPOWER);
				sectorSummaryWrapper = setValueForSector(sectorSummaryWrapper, isAlpha, isBeta, isGamma, isAddAlpha,
						isAddBeta, isAddGamma, isAddSecondAlpha, isAddSecondBeta, isAddSecondGamma);
			}
			radioParameters.add(sectorSummaryWrapper);
			sectorSummaryWrapper = null;

			sectorSummaryWrapper = getRadiusThresholdMappingForSector(siteSummaryOverviewWrapper);
			if (sectorSummaryWrapper != null) {
				sectorSummaryWrapper.setIndex(6);
				sectorSummaryWrapper.setParameter(InfraConstants.RADIUS_THRESHOLD);
				sectorSummaryWrapper = setValueForSector(sectorSummaryWrapper, isAlpha, isBeta, isGamma, isAddAlpha,
						isAddBeta, isAddGamma, isAddSecondAlpha, isAddSecondBeta, isAddSecondGamma);
			}
			radioParameters.add(sectorSummaryWrapper);
			sectorSummaryWrapper = null;

			sectorSummaryWrapper = getRSRPThresholdMappingForSector(siteSummaryOverviewWrapper);
			if (sectorSummaryWrapper != null) {
				sectorSummaryWrapper.setIndex(7);
				sectorSummaryWrapper.setParameter(InfraConstants.RSRP_THRESHOLD);
				sectorSummaryWrapper = setValueForSector(sectorSummaryWrapper, isAlpha, isBeta, isGamma, isAddAlpha,
						isAddBeta, isAddGamma, isAddSecondAlpha, isAddSecondBeta, isAddSecondGamma);
			}
			radioParameters.add(sectorSummaryWrapper);
			sectorSummaryWrapper = null;

			sectorSummaryWrapper = getBaseChannelFreqMappingForSector(siteSummaryOverviewWrapper);
			if (sectorSummaryWrapper != null) {
				sectorSummaryWrapper.setIndex(8);
				sectorSummaryWrapper.setParameter(InfraConstants.BASE_CHANNEL_FREQ);
				sectorSummaryWrapper = setValueForSector(sectorSummaryWrapper, isAlpha, isBeta, isGamma, isAddAlpha,
						isAddBeta, isAddGamma, isAddSecondAlpha, isAddSecondBeta, isAddSecondGamma);
			}
			radioParameters.add(sectorSummaryWrapper);
			sectorSummaryWrapper = null;

			sectorSummaryWrapper = getSFCarrierMappingForSector(siteSummaryOverviewWrapper);
			if (sectorSummaryWrapper != null) {
				sectorSummaryWrapper.setIndex(9);
				sectorSummaryWrapper.setParameter(InfraConstants.CARRIER);
				sectorSummaryWrapper = setValueForSector(sectorSummaryWrapper, isAlpha, isBeta, isGamma, isAddAlpha,
						isAddBeta, isAddGamma, isAddSecondAlpha, isAddSecondBeta, isAddSecondGamma);
			}
			radioParameters.add(sectorSummaryWrapper);
			sectorSummaryWrapper = null;

			map.put(InfraConstants.RADIO_PARAMETER, radioParameters);

		} catch (Exception exception) {
			logger.error("Exception in getting Mapping for sector Information {} ", Utils.getStackTrace(exception));

		}
		return map;
	}

	/**
	 * Gets the SF carrier mapping for sector.
	 * 
	 * @param siteSummaryOverviewWrapper the site summary overview wrapper
	 * @return the SF carrier mapping for sector
	 */
	private SectorSummaryWrapper getSFCarrierMappingForSector(SiteSummaryOverviewWrapper siteSummaryOverviewWrapper) {
		SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
		try {
			if (siteSummaryOverviewWrapper.getAlphaSFCarrier() != null)
				sectorSummaryWrapper.setAlpha(siteSummaryOverviewWrapper.getAlphaSFCarrier().toString());
			if (siteSummaryOverviewWrapper.getBetaSFCarrier() != null)
				sectorSummaryWrapper.setBeta(siteSummaryOverviewWrapper.getBetaSFCarrier().toString());
			if (siteSummaryOverviewWrapper.getGammaSFCarrier() != null)
				sectorSummaryWrapper.setGamma(siteSummaryOverviewWrapper.getGammaSFCarrier().toString());
			if (siteSummaryOverviewWrapper.getAlphaSFCarrieradd() != null)
				sectorSummaryWrapper.setAddAlpha(siteSummaryOverviewWrapper.getAlphaSFCarrieradd().toString());
			if (siteSummaryOverviewWrapper.getBetaSFCarrieradd() != null)
				sectorSummaryWrapper.setAddBeta(siteSummaryOverviewWrapper.getBetaSFCarrieradd().toString());
			if (siteSummaryOverviewWrapper.getGammaSFCarrieradd() != null)
				sectorSummaryWrapper.setAddGamma(siteSummaryOverviewWrapper.getGammaSFCarrieradd().toString());
		} catch (Exception exception) {
			logger.error("Unable to get the SFCarrier sector data {}", Utils.getStackTrace(exception));
		}
		return sectorSummaryWrapper;
	}

	/**
	 * Gets the base channel freq mapping for sector.
	 * 
	 * @param siteSummaryOverviewWrapper the site summary overview wrapper
	 * @return the base channel freq mapping for sector
	 */
	private SectorSummaryWrapper getBaseChannelFreqMappingForSector(
			SiteSummaryOverviewWrapper siteSummaryOverviewWrapper) {
		SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
		try {
			if (siteSummaryOverviewWrapper.getAlphabaseChannelFreq() != null)
				sectorSummaryWrapper.setAlpha(siteSummaryOverviewWrapper.getAlphabaseChannelFreq().toString());
			if (siteSummaryOverviewWrapper.getBetabaseChannelFreq() != null)
				sectorSummaryWrapper.setBeta(siteSummaryOverviewWrapper.getBetabaseChannelFreq().toString());
			if (siteSummaryOverviewWrapper.getGammabaseChannelFreq() != null)
				sectorSummaryWrapper.setGamma(siteSummaryOverviewWrapper.getGammabaseChannelFreq().toString());
			if (siteSummaryOverviewWrapper.getAlphabaseChannelFreqadd() != null)
				sectorSummaryWrapper.setAddAlpha(siteSummaryOverviewWrapper.getAlphabaseChannelFreqadd().toString());
			if (siteSummaryOverviewWrapper.getBetabaseChannelFreqadd() != null)
				sectorSummaryWrapper.setAddBeta(siteSummaryOverviewWrapper.getBetabaseChannelFreqadd().toString());
			if (siteSummaryOverviewWrapper.getGammabaseChannelFreqadd() != null)
				sectorSummaryWrapper.setAddGamma(siteSummaryOverviewWrapper.getGammabaseChannelFreqadd().toString());
		} catch (Exception exception) {
			logger.error("Unable to get the BaseChannelFreq sector data {}", Utils.getStackTrace(exception));
		}
		return sectorSummaryWrapper;
	}

	/**
	 * Gets the RSRP threshold mapping for sector.
	 * 
	 * @param siteSummaryOverviewWrapper the site summary overview wrapper
	 * @return the RSRP threshold mapping for sector
	 */
	private SectorSummaryWrapper getRSRPThresholdMappingForSector(
			SiteSummaryOverviewWrapper siteSummaryOverviewWrapper) {
		SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
		try {
			if (siteSummaryOverviewWrapper.getAlpharsrpThreshold() != null)
				sectorSummaryWrapper.setAlpha(siteSummaryOverviewWrapper.getAlpharsrpThreshold().toString());
			if (siteSummaryOverviewWrapper.getBetarsrpThreshold() != null)
				sectorSummaryWrapper.setBeta(siteSummaryOverviewWrapper.getBetarsrpThreshold().toString());
			if (siteSummaryOverviewWrapper.getGammarsrpThreshold() != null)
				sectorSummaryWrapper.setGamma(siteSummaryOverviewWrapper.getGammarsrpThreshold().toString());
			if (siteSummaryOverviewWrapper.getAlpharsrpThresholdadd() != null)
				sectorSummaryWrapper.setAddAlpha(siteSummaryOverviewWrapper.getAlpharsrpThresholdadd().toString());
			if (siteSummaryOverviewWrapper.getBetarsrpThresholdadd() != null)
				sectorSummaryWrapper.setAddBeta(siteSummaryOverviewWrapper.getBetarsrpThresholdadd().toString());
			if (siteSummaryOverviewWrapper.getGammarsrpThresholdadd() != null)
				sectorSummaryWrapper.setAddGamma(siteSummaryOverviewWrapper.getGammarsrpThresholdadd().toString());
		} catch (Exception exception) {
			logger.error("Unable to get the RSRPThreshold sector data {}", Utils.getStackTrace(exception));
		}
		return sectorSummaryWrapper;
	}

	/**
	 * Gets the radius threshold mapping for sector.
	 * 
	 * @param siteSummaryOverviewWrapper the site summary overview wrapper
	 * @return the radius threshold mapping for sector
	 */
	private SectorSummaryWrapper getRadiusThresholdMappingForSector(
			SiteSummaryOverviewWrapper siteSummaryOverviewWrapper) {
		SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
		try {
			if (siteSummaryOverviewWrapper.getAlpharadiusThreshold() != null)
				sectorSummaryWrapper.setAlpha(siteSummaryOverviewWrapper.getAlpharadiusThreshold().toString());
			if (siteSummaryOverviewWrapper.getBetaradiusThreshold() != null)
				sectorSummaryWrapper.setBeta(siteSummaryOverviewWrapper.getBetaradiusThreshold().toString());
			if (siteSummaryOverviewWrapper.getGammaradiusThreshold() != null)
				sectorSummaryWrapper.setGamma(siteSummaryOverviewWrapper.getGammaradiusThreshold().toString());
			if (siteSummaryOverviewWrapper.getAlpharadiusThresholdadd() != null)
				sectorSummaryWrapper.setAddAlpha(siteSummaryOverviewWrapper.getAlpharadiusThresholdadd().toString());
			if (siteSummaryOverviewWrapper.getBetaradiusThresholdadd() != null)
				sectorSummaryWrapper.setAddBeta(siteSummaryOverviewWrapper.getBetaradiusThresholdadd().toString());
			if (siteSummaryOverviewWrapper.getGammaradiusThresholdadd() != null)
				sectorSummaryWrapper.setAddGamma(siteSummaryOverviewWrapper.getGammaradiusThresholdadd().toString());

		} catch (Exception exception) {
			logger.error("Unable to get the RadiusThreshold sector data {}", Utils.getStackTrace(exception));
		}
		return sectorSummaryWrapper;
	}

	/**
	 * Gets the pilot channel tx power mapping for sector.
	 * 
	 * @param siteSummaryOverviewWrapper the site summary overview wrapper
	 * @return the pilot channel tx power mapping for sector
	 */
	private SectorSummaryWrapper getPilotChannelTxPowerMappingForSector(
			SiteSummaryOverviewWrapper siteSummaryOverviewWrapper) {
		SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
		try {
			if (siteSummaryOverviewWrapper.getAlphapilotChannelTxPower() != null)
				sectorSummaryWrapper.setAlpha(siteSummaryOverviewWrapper.getAlphapilotChannelTxPower().toString());
			if (siteSummaryOverviewWrapper.getBetapilotChannelTxPower() != null)
				sectorSummaryWrapper.setBeta(siteSummaryOverviewWrapper.getBetapilotChannelTxPower().toString());
			if (siteSummaryOverviewWrapper.getGammapilotChannelTxPower() != null)
				sectorSummaryWrapper.setGamma(siteSummaryOverviewWrapper.getGammapilotChannelTxPower().toString());
			if (siteSummaryOverviewWrapper.getAlphapilotChannelTxPoweradd() != null)
				sectorSummaryWrapper
						.setAddAlpha(siteSummaryOverviewWrapper.getAlphapilotChannelTxPoweradd().toString());
			if (siteSummaryOverviewWrapper.getBetapilotChannelTxPoweradd() != null)
				sectorSummaryWrapper.setAddBeta(siteSummaryOverviewWrapper.getBetapilotChannelTxPoweradd().toString());
			if (siteSummaryOverviewWrapper.getGammapilotChannelTxPoweradd() != null)
				sectorSummaryWrapper
						.setAddGamma(siteSummaryOverviewWrapper.getGammapilotChannelTxPoweradd().toString());
		} catch (Exception exception) {
			logger.error("Unable to get the PilotChannelTxPower sector data {}", Utils.getStackTrace(exception));
		}
		return sectorSummaryWrapper;
	}

	/**
	 * Gets the eirp mapping for sector.
	 * 
	 * @param siteSummaryOverviewWrapper the site summary overview wrapper
	 * @return the eirp mapping for sector
	 */
	private SectorSummaryWrapper getEirpMappingForSector(SiteSummaryOverviewWrapper siteSummaryOverviewWrapper) {
		SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
		try {
			if (siteSummaryOverviewWrapper.getAlphaeirp() != null)
				sectorSummaryWrapper.setAlpha(siteSummaryOverviewWrapper.getAlphaeirp());
			if (siteSummaryOverviewWrapper.getBetaeirp() != null)
				sectorSummaryWrapper.setBeta(siteSummaryOverviewWrapper.getBetaeirp());
			if (siteSummaryOverviewWrapper.getGammaeirp() != null)
				sectorSummaryWrapper.setGamma(siteSummaryOverviewWrapper.getGammaeirp());
			if (siteSummaryOverviewWrapper.getAlphaeirpadd() != null)
				sectorSummaryWrapper.setAddAlpha(siteSummaryOverviewWrapper.getAlphaeirpadd());
			if (siteSummaryOverviewWrapper.getBetaeirpadd() != null)
				sectorSummaryWrapper.setAddBeta(siteSummaryOverviewWrapper.getBetaeirpadd());
			if (siteSummaryOverviewWrapper.getGammaeirpadd() != null)
				sectorSummaryWrapper.setAddGamma(siteSummaryOverviewWrapper.getGammaeirpadd());
		} catch (Exception exception) {
			logger.error("Unable to get the Eirp sector data {}", Utils.getStackTrace(exception));
		}
		return sectorSummaryWrapper;
	}

	/**
	 * Gets the tx power mapping for sector.
	 * 
	 * @param siteSummaryOverviewWrapper the site summary overview wrapper
	 * @return the tx power mapping for sector
	 */
	private SectorSummaryWrapper getTxPowerMappingForSector(SiteSummaryOverviewWrapper siteSummaryOverviewWrapper) {
		SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
		try {
			if (siteSummaryOverviewWrapper.getAlphatxPower() != null)
				sectorSummaryWrapper.setAlpha(siteSummaryOverviewWrapper.getAlphatxPower());
			if (siteSummaryOverviewWrapper.getBetatxPower() != null)
				sectorSummaryWrapper.setBeta(siteSummaryOverviewWrapper.getBetatxPower());
			if (siteSummaryOverviewWrapper.getGammatxPower() != null)
				sectorSummaryWrapper.setGamma(siteSummaryOverviewWrapper.getGammatxPower());
			if (siteSummaryOverviewWrapper.getAlphatxPoweradd() != null)
				sectorSummaryWrapper.setAddAlpha(siteSummaryOverviewWrapper.getAlphatxPoweradd());
			if (siteSummaryOverviewWrapper.getBetatxPoweradd() != null)
				sectorSummaryWrapper.setAddBeta(siteSummaryOverviewWrapper.getBetatxPoweradd());
			if (siteSummaryOverviewWrapper.getGammatxPoweradd() != null)
				sectorSummaryWrapper.setAddGamma(siteSummaryOverviewWrapper.getGammatxPoweradd());
		} catch (Exception exception) {
			logger.error("Unable to get the TxPower sector data {}", Utils.getStackTrace(exception));
		}
		return sectorSummaryWrapper;
	}

	/**
	 * Gets the propagation model mapping for sector.
	 * 
	 * @param siteSummaryOverviewWrapper the site summary overview wrapper
	 * @return the propagation model mapping for sector
	 */
	private SectorSummaryWrapper getPropagationModelMappingForSector(
			SiteSummaryOverviewWrapper siteSummaryOverviewWrapper) {
		SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
		try {
			if (siteSummaryOverviewWrapper.getAlphapropagationModel() != null)
				sectorSummaryWrapper.setAlpha(siteSummaryOverviewWrapper.getAlphapropagationModel());
			if (siteSummaryOverviewWrapper.getBetapropagationModel() != null)
				sectorSummaryWrapper.setBeta(siteSummaryOverviewWrapper.getBetapropagationModel());
			if (siteSummaryOverviewWrapper.getGammapropagationModel() != null)
				sectorSummaryWrapper.setGamma(siteSummaryOverviewWrapper.getGammapropagationModel());
			if (siteSummaryOverviewWrapper.getAlphapropagationModeladd() != null)
				sectorSummaryWrapper.setAddAlpha(siteSummaryOverviewWrapper.getAlphapropagationModeladd());
			if (siteSummaryOverviewWrapper.getBetapropagationModeladd() != null)
				sectorSummaryWrapper.setAddBeta(siteSummaryOverviewWrapper.getBetapropagationModeladd());
			if (siteSummaryOverviewWrapper.getGammapropagationModeladd() != null)
				sectorSummaryWrapper.setAddGamma(siteSummaryOverviewWrapper.getGammapropagationModeladd());
		} catch (Exception exception) {
			logger.error("Unable to get the PropagationModel sector data {}", Utils.getStackTrace(exception));
		}
		return sectorSummaryWrapper;
	}

	/**
	 * Gets the clutter category mapping for sector.
	 * 
	 * @param siteSummaryOverviewWrapper the site summary overview wrapper
	 * @return the clutter category mapping for sector
	 */
	private SectorSummaryWrapper getClutterCategoryMappingForSector(
			SiteSummaryOverviewWrapper siteSummaryOverviewWrapper) {
		SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
		try {
			if (siteSummaryOverviewWrapper.getAlphaclutterCategory() != null)
				sectorSummaryWrapper.setAlpha(siteSummaryOverviewWrapper.getAlphaclutterCategory());
			if (siteSummaryOverviewWrapper.getBetaclutterCategory() != null)
				sectorSummaryWrapper.setBeta(siteSummaryOverviewWrapper.getBetaclutterCategory());
			if (siteSummaryOverviewWrapper.getGammaclutterCategory() != null)
				sectorSummaryWrapper.setGamma(siteSummaryOverviewWrapper.getGammaclutterCategory());
			if (siteSummaryOverviewWrapper.getAlphaclutterCategoryadd() != null)
				sectorSummaryWrapper.setAddAlpha(siteSummaryOverviewWrapper.getAlphaclutterCategoryadd());
			if (siteSummaryOverviewWrapper.getBetaclutterCategoryadd() != null)
				sectorSummaryWrapper.setAddBeta(siteSummaryOverviewWrapper.getBetaclutterCategoryadd());
			if (siteSummaryOverviewWrapper.getGammaclutterCategoryadd() != null)
				sectorSummaryWrapper.setAddGamma(siteSummaryOverviewWrapper.getGammaclutterCategoryadd());
		} catch (Exception exception) {
			logger.error("Unable to get the ClutterCategory sector data {}", Utils.getStackTrace(exception));
		}
		return sectorSummaryWrapper;
	}

	@Override
	public Map<String, List<SectorSummaryWrapper>> getSiteSummaryRadioParametersByBand(String neName,
			String neFrequency, String neStatus) {
		logger.info("Going to get Site Summary Radio Parameters for neName {}, neFrequency {}", neName, neFrequency);
		try {
			SiteSummaryOverviewWrapper overviewWrapper = new SiteSummaryOverviewWrapper();
			overviewWrapper = macroSiteDetailDao.getRadioParametersByBand(neName, neFrequency, neStatus);
			return getMappingForRadioParameter(overviewWrapper, neName, neFrequency, neStatus);
		} catch (Exception exception) {
			logger.error("Unable to get Site Summary Radio Parameters for neName {}, neFrequency {}", neName,
					neFrequency);
			throw new RestException(InfraConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	/**
	 * Gets the vertical beam width mapping for sector.
	 * 
	 * @param siteSummaryOverviewWrapper the site summary overview wrapper
	 * @return the vertical beam width mapping for sector
	 */
	private SectorSummaryWrapper getVerticalBeamWidthMappingForSector(
			SiteSummaryOverviewWrapper siteSummaryOverviewWrapper) {
		SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
		try {
			if (siteSummaryOverviewWrapper.getAlphaVerticalBeamWidth() != null)
				sectorSummaryWrapper.setAlpha(Utils.roundOff(siteSummaryOverviewWrapper.getAlphaVerticalBeamWidth(), 2).toString());
			if (siteSummaryOverviewWrapper.getBetaVerticalBeamWidth() != null)
				sectorSummaryWrapper.setBeta(Utils.roundOff(siteSummaryOverviewWrapper.getBetaVerticalBeamWidth(),2).toString());
			if (siteSummaryOverviewWrapper.getGammaVerticalBeamWidth() != null)
				sectorSummaryWrapper.setGamma(Utils.roundOff(siteSummaryOverviewWrapper.getGammaVerticalBeamWidth(),2).toString());
			if (siteSummaryOverviewWrapper.getAlphaVerticalBeamWidthadd() != null)
				sectorSummaryWrapper.setAddAlpha(Utils.roundOff(siteSummaryOverviewWrapper.getAlphaVerticalBeamWidthadd(),2).toString());
			if (siteSummaryOverviewWrapper.getBetaVerticalBeamWidthadd() != null)
				sectorSummaryWrapper.setAddBeta(Utils.roundOff(siteSummaryOverviewWrapper.getBetaVerticalBeamWidthadd(),2).toString());
			if (siteSummaryOverviewWrapper.getGammaVerticalBeamWidthadd() != null)
				sectorSummaryWrapper.setAddGamma(Utils.roundOff(siteSummaryOverviewWrapper.getGammaVerticalBeamWidthadd(),2).toString());
		} catch (Exception exception) {
			logger.error("Unable to get the VerticalBeamWidth sector data {}", Utils.getStackTrace(exception));
		}
		return sectorSummaryWrapper;
	}

	/**
	 * Gets the horizontal beam width mapping for sector.
	 * 
	 * @param siteSummaryOverviewWrapper the site summary overview wrapper
	 * @return the horizontal beam width mapping for sector
	 */
	private SectorSummaryWrapper getHorizontalBeamWidthMappingForSector(
			SiteSummaryOverviewWrapper siteSummaryOverviewWrapper) {
		SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
		try {
			if (siteSummaryOverviewWrapper.getAlphaHorizontalBeamWidth() != null)
				sectorSummaryWrapper.setAlpha(siteSummaryOverviewWrapper.getAlphaHorizontalBeamWidth().toString());
			if (siteSummaryOverviewWrapper.getBetaHorizontalBeamWidth() != null)
				sectorSummaryWrapper.setBeta(siteSummaryOverviewWrapper.getBetaHorizontalBeamWidth().toString());
			if (siteSummaryOverviewWrapper.getGammaHorizontalBeamWidth() != null)
				sectorSummaryWrapper.setGamma(siteSummaryOverviewWrapper.getGammaHorizontalBeamWidth().toString());
			if (siteSummaryOverviewWrapper.getAlphaHorizontalBeamWidthadd() != null)
				sectorSummaryWrapper
						.setAddAlpha(siteSummaryOverviewWrapper.getAlphaHorizontalBeamWidthadd().toString());
			if (siteSummaryOverviewWrapper.getBetaHorizontalBeamWidthadd() != null)
				sectorSummaryWrapper.setAddBeta(siteSummaryOverviewWrapper.getBetaHorizontalBeamWidthadd().toString());
			if (siteSummaryOverviewWrapper.getGammaHorizontalBeamWidthadd() != null)
				sectorSummaryWrapper
						.setAddGamma(siteSummaryOverviewWrapper.getGammaHorizontalBeamWidthadd().toString());
		} catch (Exception exception) {
			logger.error("Unable to get the HorizontalBeamWidth sector data {}", Utils.getStackTrace(exception));
		}
		return sectorSummaryWrapper;
	}

	/**
	 * Gets the antenna gain mapping for sector.
	 * 
	 * @param siteSummaryOverviewWrapper the site summary overview wrapper
	 * @return the antenna gain mapping for sector
	 */
	private SectorSummaryWrapper getAntennaGainMappingForSector(SiteSummaryOverviewWrapper siteSummaryOverviewWrapper) {
		SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
		try {
			if (siteSummaryOverviewWrapper.getAlphaAntennaGain() != null)
				sectorSummaryWrapper.setAlpha(siteSummaryOverviewWrapper.getAlphaAntennaGain());
			if (siteSummaryOverviewWrapper.getBetaAntennaGain() != null)
				sectorSummaryWrapper.setBeta(siteSummaryOverviewWrapper.getBetaAntennaGain());
			if (siteSummaryOverviewWrapper.getGammaAntennaGain() != null)
				sectorSummaryWrapper.setGamma(siteSummaryOverviewWrapper.getGammaAntennaGain());
			if (siteSummaryOverviewWrapper.getAlphaAntennaGainadd() != null)
				sectorSummaryWrapper.setAddAlpha(siteSummaryOverviewWrapper.getAlphaAntennaGainadd());
			if (siteSummaryOverviewWrapper.getBetaAntennaGainadd() != null)
				sectorSummaryWrapper.setAddBeta(siteSummaryOverviewWrapper.getBetaAntennaGainadd());
			if (siteSummaryOverviewWrapper.getGammaAntennaGainadd() != null)
				sectorSummaryWrapper.setAddGamma(siteSummaryOverviewWrapper.getGammaAntennaGainadd());
		} catch (Exception exception) {
			logger.error("Unable to get the AntennaGain sector data {}", Utils.getStackTrace(exception));
		}
		return sectorSummaryWrapper;
	}

	/**
	 * Gets the antenna vendor name mapping for sector.
	 * 
	 * @param siteSummaryOverviewWrapper the site summary overview wrapper
	 * @return the antenna vendor name mapping for sector
	 */
	private SectorSummaryWrapper getAntennaVendorNameMappingForSector(
			SiteSummaryOverviewWrapper siteSummaryOverviewWrapper) {
		SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
		try {
			if (siteSummaryOverviewWrapper.getAlphaAntennaVendorName() != null)
				sectorSummaryWrapper.setAlpha(siteSummaryOverviewWrapper.getAlphaAntennaVendorName());
			if (siteSummaryOverviewWrapper.getBetaAntennaVendorName() != null)
				sectorSummaryWrapper.setBeta(siteSummaryOverviewWrapper.getBetaAntennaVendorName());
			if (siteSummaryOverviewWrapper.getGammaAntennaVendorName() != null)
				sectorSummaryWrapper.setGamma(siteSummaryOverviewWrapper.getGammaAntennaVendorName());
			if (siteSummaryOverviewWrapper.getAlphaAntennaVendorNameadd() != null)
				sectorSummaryWrapper.setAddAlpha(siteSummaryOverviewWrapper.getAlphaAntennaVendorNameadd());
			if (siteSummaryOverviewWrapper.getBetaAntennaVendorNameadd() != null)
				sectorSummaryWrapper.setAddBeta(siteSummaryOverviewWrapper.getBetaAntennaVendorNameadd());
			if (siteSummaryOverviewWrapper.getGammaAntennaVendorNameadd() != null)
				sectorSummaryWrapper.setAddGamma(siteSummaryOverviewWrapper.getGammaAntennaVendorNameadd());
		} catch (Exception exception) {
			logger.error("Unable to get the AntennaVendorName sector data {}", Utils.getStackTrace(exception));
		}
		return sectorSummaryWrapper;
	}

	@Override
	@Transactional
	public SiteGeographicalDetail getSectorPropertyDataByBand(String neName, String neFrequency, String neStatus, String neType) {
		logger.info("into the method getSectorPropertyDataByBand for getting the sector data");
		List<RANDetail> ranDetailList = new ArrayList<>();
		SiteGeographicalDetail geographicalDetail = new SiteGeographicalDetail();
		try {
			ranDetailList = iranDetailDao.getSectorPropertyData(neName, neFrequency, neStatus,neType);
			logger.info("Total records fetched from NetworkElement is {}", ranDetailList.size());

			for (RANDetail ranDetailData : ranDetailList) {
				if (ranDetailData.getSector() != null) {
					geographicalDetail = getDataForSectorProperty(ranDetailData, ranDetailData.getSector(),
							geographicalDetail);
				}
			}
			String bandwidthFinal = ALPHA_BANDWIDTH_FIRST + ForesightConstants.COMMA + ALPHA_BANDWIDTH_SECOND;
			bandwidthFinal = getFormattedBandWidthForSites(bandwidthFinal);
			if (bandwidthFinal != null && !bandwidthFinal.isEmpty()) {
				geographicalDetail.setAlphaBandwidth(bandwidthFinal);
			} else {
				geographicalDetail.setAlphaBandwidth(ForesightConstants.HIPHEN);
			}

			bandwidthFinal = ForesightConstants.BLANK_STRING;
			bandwidthFinal = BETA_BANDWIDTH_FIRST + ForesightConstants.COMMA + BETA_BANDWIDTH_SECOND;
			bandwidthFinal = getFormattedBandWidthForSites(bandwidthFinal);
			if (bandwidthFinal != null && !bandwidthFinal.isEmpty()) {
				geographicalDetail.setBetaBandwidth(bandwidthFinal);
			} else {
				geographicalDetail.setBetaBandwidth(ForesightConstants.HIPHEN);
			}
			bandwidthFinal = ForesightConstants.BLANK_STRING;
			bandwidthFinal = GAMMA_BANDWIDTH_FIRST + ForesightConstants.COMMA + GAMMA_BANDWIDTH_SECOND;
			bandwidthFinal = getFormattedBandWidthForSites(bandwidthFinal);
			if (bandwidthFinal != null && !bandwidthFinal.isEmpty()) {
				geographicalDetail.setGammaBandwidth(bandwidthFinal);
			} else {
				geographicalDetail.setGammaBandwidth(ForesightConstants.HIPHEN);
			}
			bandwidthFinal = ForesightConstants.BLANK_STRING;
			bandwidthFinal = ALPHA_ADDITIONAL_BANDWIDTH_FIRST + ForesightConstants.COMMA
					+ ALPHA_ADDITIONAL_BANDWIDTH_SECOND;
			bandwidthFinal = getFormattedBandWidthForSites(bandwidthFinal);
			if (bandwidthFinal != null && !bandwidthFinal.isEmpty()) {
				geographicalDetail.setAlphaAdditionalBandwidth(bandwidthFinal);
			} else {
				geographicalDetail.setAlphaAdditionalBandwidth(ForesightConstants.HIPHEN);
			}

			bandwidthFinal = ForesightConstants.BLANK_STRING;
			bandwidthFinal = BETA_ADDITIONAL_BANDWIDTH_FIRST + ForesightConstants.COMMA
					+ BETA_ADDITIONAL_BANDWIDTH_SECOND;
			bandwidthFinal = getFormattedBandWidthForSites(bandwidthFinal);
			if (bandwidthFinal != null && !bandwidthFinal.isEmpty()) {
				geographicalDetail.setBetaAdditionalBandwidth(bandwidthFinal);
			} else {
				geographicalDetail.setBetaAdditionalBandwidth(ForesightConstants.HIPHEN);
			}
			bandwidthFinal = ForesightConstants.BLANK_STRING;
			bandwidthFinal = GAMMA_ADDITIONAL_BANDWIDTH_FIRST + ForesightConstants.COMMA
					+ GAMMA_ADDITIONAL_BANDWIDTH_SECOND;
			bandwidthFinal = getFormattedBandWidthForSites(bandwidthFinal);
			if (bandwidthFinal != null && !bandwidthFinal.isEmpty()) {
				geographicalDetail.setGammaAdditionalBandwidth(bandwidthFinal);
			} else {
				geographicalDetail.setGammaAdditionalBandwidth(ForesightConstants.HIPHEN);
			}
			reInitializeSectorParameters();
		} catch (Exception exception) {
			logger.error("Unable to get Sector Property Data by neFrequency {} for neName {} ", neFrequency, neName);
		}
		return geographicalDetail;
	}

	private static String getFormattedBandWidthForSites(String bandwidth) {
		if (bandwidth != null) {
			String b1 = ForesightConstants.BLANK_STRING;
			String b[] = bandwidth.split(ForesightConstants.COMMA);
			for (int i = 0; i < b.length; i++) {

				if (i != 0) {
					if(!b1.equalsIgnoreCase(ForesightConstants.BLANK_STRING))
					b1 = b1.concat(ForesightConstants.SPACE).concat(ForesightConstants.PLUS_SIGN).concat(ForesightConstants.SPACE);
				}
				if (b[i] != null && !b[i].equalsIgnoreCase(ForesightConstants.BLANK_STRING)) {
					b1 = b1.concat(b[i]).concat(ForesightConstants.SPACE);
				}
				
			}
			bandwidth = b1;

		} else {
			bandwidth = ForesightConstants.BLANK_STRING;
		}
		return bandwidth;
	}

	/**
	 * Re initialize sector parameters.
	 */
	private void reInitializeSectorParameters() {
		ALPHA_BANDWIDTH_FIRST = ForesightConstants.BLANK_STRING;
		BETA_BANDWIDTH_FIRST = ForesightConstants.BLANK_STRING;
		GAMMA_BANDWIDTH_FIRST = ForesightConstants.BLANK_STRING;
		ALPHA_BANDWIDTH_SECOND = ForesightConstants.BLANK_STRING;
		BETA_BANDWIDTH_SECOND = ForesightConstants.BLANK_STRING;
		GAMMA_BANDWIDTH_SECOND = ForesightConstants.BLANK_STRING;
		ALPHA_ADDITIONAL_BANDWIDTH_FIRST = ForesightConstants.BLANK_STRING;
		BETA_ADDITIONAL_BANDWIDTH_FIRST = ForesightConstants.BLANK_STRING;
		GAMMA_ADDITIONAL_BANDWIDTH_FIRST = ForesightConstants.BLANK_STRING;
		ALPHA_ADDITIONAL_BANDWIDTH_SECOND = ForesightConstants.BLANK_STRING;
		BETA_ADDITIONAL_BANDWIDTH_SECOND = ForesightConstants.BLANK_STRING;
		GAMMA_ADDITIONAL_BANDWIDTH_SECOND = ForesightConstants.BLANK_STRING;
	}

	private SiteGeographicalDetail getDataForSectorProperty(RANDetail ranDetail, Integer Sectorid,
			SiteGeographicalDetail geographicalDetail) {
		try {
			if (Sectorid != null) {
				if(ranDetail != null) {
				if(ranDetail.getNetworkElement() != null) {
					if(ranDetail.getNetworkElement().getNetworkElement() != null)
					geographicalDetail.setSapid(ranDetail.getNetworkElement().getNetworkElement().getNeName() != null ? ranDetail.getNetworkElement().getNetworkElement().getNeName() : ForesightConstants.HIPHEN);
					geographicalDetail.setBand(ranDetail.getNetworkElement().getNeFrequency() != null ? ranDetail.getNetworkElement().getNeFrequency() : ForesightConstants.HIPHEN);
					geographicalDetail.setEnodeBId(ranDetail.getNetworkElement().getEnbid() != null ? ranDetail.getNetworkElement().getEnbid().toString(): ForesightConstants.HIPHEN);
					if (ranDetail.getNetworkElement().getGeographyL4() != null && ranDetail.getNetworkElement().getGeographyL4().getGeographyL3() != null && ranDetail.getNetworkElement().getGeographyL4().getGeographyL3().getGeographyL2() != null	&& ranDetail.getNetworkElement().getGeographyL4().getGeographyL3().getGeographyL2().getDisplayName() != null) {
						geographicalDetail.setGeographyL2(ranDetail.getNetworkElement().getGeographyL4().getGeographyL3().getGeographyL2().getDisplayName());
					}
					if (ranDetail.getNetworkElement().getGeographyL4() != null && ranDetail.getNetworkElement().getGeographyL4().getDisplayName() != null) {
						geographicalDetail.setGeographyL4(ranDetail.getNetworkElement().getGeographyL4().getDisplayName());
					}
					geographicalDetail.setSiteName(ranDetail.getNetworkElement().getFriendlyname() != null? ranDetail.getNetworkElement().getFriendlyname(): ForesightConstants.HIPHEN);
				}
				if (ranDetail.getNeBandDetail() != null) {
					if(ranDetail.getNeBandDetail().getBackhauInfo() != null)
						geographicalDetail.setBackhaulMedia(ranDetail.getNeBandDetail().getBackhauInfo());
					if (ranDetail.getNeBandDetail().getNumberOfSector() != null)
						geographicalDetail.setNumberOfSector(ranDetail.getNeBandDetail().getNumberOfSector());
				
						if(ranDetail.getNeBandDetail().getNetworkElement()!=null) {
							if(ranDetail.getNeBandDetail().getNetworkElement().getNeLocation()!=null) {
								geographicalDetail.setSiteAddress(ranDetail.getNeBandDetail().getNetworkElement().getNeLocation().getAddress() != null
										? ranDetail.getNeBandDetail().getNetworkElement().getNeLocation().getAddress(): ForesightConstants.HIPHEN);
							}	
						}
					if (ranDetail.getNeBandDetail().getBandStatus() != null)
						geographicalDetail.setStatus(ranDetail.getNeBandDetail().getBandStatus());
					if (Sectorid == 1) {
						if(ranDetail.getNeBandDetail().getCarrier() != null ) {
						if (ranDetail.getNeBandDetail().getCarrier().equalsIgnoreCase(ForesightConstants.FIRST)) {
							if (ranDetail.getBandwidth() != null) {
								ALPHA_BANDWIDTH_FIRST = ranDetail.getBandwidth();
							}
							if (ranDetail.getCellOnairDate() != null)
								geographicalDetail.setAlphaOnAirTime(ranDetail.getCellOnairDate());
							if (ranDetail.getNeBandDetail().getBandStatus() != null)
								geographicalDetail.setAlphaStatus(ranDetail.getNeBandDetail().getBandStatus());
						}
						if (ranDetail.getNeBandDetail().getCarrier().equalsIgnoreCase(ForesightConstants.SECOND)) {
							if (ranDetail.getBandwidth() != null) {
								ALPHA_BANDWIDTH_SECOND = ranDetail.getBandwidth();
							}
						}
					}
						
						
					}
					if (Sectorid == 2) {
						if(ranDetail.getNeBandDetail().getCarrier() != null ) {
						if (ranDetail.getNeBandDetail().getCarrier().equalsIgnoreCase(ForesightConstants.FIRST)) {
							if (ranDetail.getBandwidth() != null) {
								BETA_BANDWIDTH_FIRST = ranDetail.getBandwidth();
							}
							if (ranDetail.getCellOnairDate() != null)
								geographicalDetail.setBetaOnAirTime(ranDetail.getCellOnairDate());
							if (ranDetail.getNeBandDetail().getBandStatus() != null)
								geographicalDetail.setBetaStatus(ranDetail.getNeBandDetail().getBandStatus());
						}
						if (ranDetail.getNeBandDetail().getCarrier().equalsIgnoreCase(ForesightConstants.SECOND)) {
							if (ranDetail.getBandwidth() != null) {
								BETA_BANDWIDTH_SECOND = ranDetail.getBandwidth();
							}
						}
					}
						
					}
					if (Sectorid == 3) {
						if(ranDetail.getNeBandDetail().getCarrier() != null ) {
						if (ranDetail.getNeBandDetail().getCarrier().equalsIgnoreCase(ForesightConstants.FIRST)) {
							if (ranDetail.getBandwidth() != null) {
								GAMMA_BANDWIDTH_FIRST = ranDetail.getBandwidth();
							}
							if (ranDetail.getCellOnairDate() != null)
								geographicalDetail.setGammaOnAirTime(ranDetail.getCellOnairDate());
							if (ranDetail.getNeBandDetail().getBandStatus() != null)
								geographicalDetail.setGammaStatus(ranDetail.getNeBandDetail().getBandStatus());
						}
						if (ranDetail.getNeBandDetail().getCarrier().equalsIgnoreCase(ForesightConstants.SECOND)) {
							if (ranDetail.getBandwidth() != null) {
								GAMMA_BANDWIDTH_SECOND = ranDetail.getBandwidth();
							}
						}
					}
						
					}
					if (Sectorid == 4) {
						geographicalDetail.setCheckAlphaAddSectorId4(true);
						if(ranDetail.getNeBandDetail().getCarrier() != null ) {
						if (ranDetail.getNeBandDetail().getCarrier().equalsIgnoreCase(ForesightConstants.FIRST)) {
							if (ranDetail.getBandwidth() != null) {
								ALPHA_ADDITIONAL_BANDWIDTH_FIRST = ranDetail.getBandwidth();
							}
							if (ranDetail.getCellOnairDate() != null)
								geographicalDetail.setAlphaAdditionalOnAirTime(ranDetail.getCellOnairDate());
							if(ranDetail.getNeBandDetail().getBandStatus() != null)
								geographicalDetail.setAlphaAdditionalStatus(ranDetail.getNeBandDetail().getBandStatus());
						}
						if (ranDetail.getNeBandDetail().getCarrier().equalsIgnoreCase(ForesightConstants.SECOND)) {
							if (ranDetail.getBandwidth() != null) {
								ALPHA_ADDITIONAL_BANDWIDTH_SECOND = ranDetail.getBandwidth();
							}
						}
						}
						
					}

					if (Sectorid == 5) {
						geographicalDetail.setCheckBetaAddSectorId5(true);
						if(ranDetail.getNeBandDetail().getCarrier() != null ) {
						if (ranDetail.getNeBandDetail().getCarrier().equalsIgnoreCase(ForesightConstants.FIRST)) {
							if (ranDetail.getBandwidth() != null) {
								BETA_ADDITIONAL_BANDWIDTH_FIRST = ranDetail.getBandwidth();
							}
							if (ranDetail.getCellOnairDate() != null)
								geographicalDetail.setBetaAdditionalOnAirTime(ranDetail.getCellOnairDate());
							if(ranDetail.getNeBandDetail().getBandStatus() != null)
								geographicalDetail.setBetaAdditionalStatus(ranDetail.getNeBandDetail().getBandStatus());
						}
						if (ranDetail.getNeBandDetail().getCarrier().equalsIgnoreCase(ForesightConstants.SECOND)) {
							if (ranDetail.getBandwidth() != null) {
								BETA_ADDITIONAL_BANDWIDTH_SECOND = ranDetail.getBandwidth();
							}
						}
						}
						
					}
					if (Sectorid == 6) {
						geographicalDetail.setCheckGammaAddSectorId6(true);
						if(ranDetail.getNeBandDetail().getCarrier() != null ) {
						if (ranDetail.getNeBandDetail().getCarrier().equalsIgnoreCase(ForesightConstants.FIRST)) {
							if (ranDetail.getBandwidth() != null) {
								GAMMA_ADDITIONAL_BANDWIDTH_FIRST = ranDetail.getBandwidth();
							}
							if (ranDetail.getCellOnairDate() != null)
								geographicalDetail.setGammaAdditionalOnAirTime(ranDetail.getCellOnairDate());
							if(ranDetail.getNeBandDetail().getBandStatus() != null)
								geographicalDetail.setGammaAdditionalStatus(ranDetail.getNeBandDetail().getBandStatus());
						}
						if (ranDetail.getNeBandDetail().getCarrier().equalsIgnoreCase(ForesightConstants.SECOND)) {
							if (ranDetail.getBandwidth() != null) {
								GAMMA_ADDITIONAL_BANDWIDTH_SECOND = ranDetail.getBandwidth();
							}
						}
						}
						
					}
				}
			}
			}
		} catch (Exception exception) {
			logger.error("Error in setting data into the wrapper from Sector Object Exception {}",Utils.getStackTrace(exception));
		}
		return geographicalDetail;
	}

	@Override
	@Transactional
	public List<MacroSiteDetail> getMacroSiteDetails(MacroSiteDetailWrapper wrapper) {
		try {
			return macroSiteDetailDao.getMacroSiteDetailsForCellLevel(wrapper.getNeTypeList(), wrapper.getNeNameList(),
					wrapper.getNeFrequencyList(), wrapper.getNeStatusList(), wrapper.getVendorList(),
					wrapper.getTechnologyList(), wrapper.getDomainList(), wrapper.getGeographyNames(), null);
		} catch (Exception e) {
			logger.error("Exception Occoured In Processing Request {} ", Utils.getStackTrace(e));
			throw new RestException(e);
		}
	}

	@Override
	public WifiWrapper getWifiDetailByNEId(String neId) {
		logger.info("Going to get Wifi Detail by neId {}", neId);
		try {
			return wifiSiteDetailDao.getWifiDetailByNEId(neId);
		} catch (Exception e) {
			logger.error("Error while getting wifi detail err msg {}", Utils.getStackTrace(e));
		}
		return null;
	}

	private Map<String, Long> getAggregatedCountsForKpi(List<NetworkElementWrapper> combinedList) {
		Map<String, Long> kpiCountMap = new HashMap<>();
		try {
			kpiCountMap = combinedList.stream().collect(Collectors.groupingBy(NetworkElementWrapper::getNeFrequency,
					Collectors.summingLong(NetworkElementWrapper::getTotalCount)));
			Long totalCount = combinedList.stream().mapToLong(nw -> nw.getTotalCount()).sum();
			kpiCountMap.put(InfraConstants.TOTAL_SITE_COUNTS, totalCount);
		} catch (Exception exception) {
			logger.error("Unable to get the aggregated Count for kpi {}", Utils.getStackTrace(exception));
		}
		return kpiCountMap;
	}

	private Map<String, List<NetworkElementWrapper>> getSitesCountByNEParameter(
			SiteSelectionWrapper siteSelectionWrapper, Double southWestLong, Double southWestLat, Double northEastLong,
			Double northEastLat, Integer zoomLevel, String type) {
		Map<String, List<NetworkElementWrapper>> sitesVisailizationMap = new HashMap<>();
		try {
			if (siteSelectionWrapper != null) {
				logger.info("Going to Site count  and Site Visualisation map ");
				if (siteSelectionWrapper.getMacroOnair() != null && !siteSelectionWrapper.getMacroOnair().isEmpty())
					sitesVisailizationMap = getNetworkElementsForSiteLayer(siteSelectionWrapper.getMacroOnair(),
							southWestLong, southWestLat, northEastLong, northEastLat, zoomLevel, type);
				if (siteSelectionWrapper.getMacroPlanned() != null && !siteSelectionWrapper.getMacroPlanned().isEmpty())
					sitesVisailizationMap = getSiteVisualizeMap(siteSelectionWrapper.getMacroPlanned(), southWestLong,
							southWestLat, northEastLong, northEastLat, zoomLevel, sitesVisailizationMap, type);
				if (siteSelectionWrapper.getDasOnair() != null && !siteSelectionWrapper.getDasOnair().isEmpty())
					sitesVisailizationMap = getSiteVisualizeMap(siteSelectionWrapper.getDasOnair(), southWestLong,
							southWestLat, northEastLong, northEastLat, zoomLevel, sitesVisailizationMap, type);
				if (siteSelectionWrapper.getDasPlanned() != null && !siteSelectionWrapper.getDasPlanned().isEmpty())
					sitesVisailizationMap = getSiteVisualizeMap(siteSelectionWrapper.getDasPlanned(), southWestLong,
							southWestLat, northEastLong, northEastLat, zoomLevel, sitesVisailizationMap, type);
				if (siteSelectionWrapper.getOnairOutdoorSmallCell() != null
						&& !siteSelectionWrapper.getOnairOutdoorSmallCell().isEmpty())
					sitesVisailizationMap = getSiteVisualizeMap(siteSelectionWrapper.getOnairOutdoorSmallCell(),
							southWestLong, southWestLat, northEastLong, northEastLat, zoomLevel, sitesVisailizationMap,
							type);
				if (siteSelectionWrapper.getPlannedOutdoorSmallCell() != null
						&& !siteSelectionWrapper.getPlannedOutdoorSmallCell().isEmpty())
					sitesVisailizationMap = getSiteVisualizeMap(siteSelectionWrapper.getPlannedOutdoorSmallCell(),
							southWestLong, southWestLat, northEastLong, northEastLat, zoomLevel, sitesVisailizationMap,
							type);
				if (siteSelectionWrapper.getMacroNonradiating() != null
						&& !siteSelectionWrapper.getMacroNonradiating().isEmpty())
					sitesVisailizationMap = getSiteVisualizeMap(siteSelectionWrapper.getMacroNonradiating(),
							southWestLong, southWestLat, northEastLong, northEastLat, zoomLevel, sitesVisailizationMap,
							type);

				if (siteSelectionWrapper.getMacroDecommissioned() != null
						&& !siteSelectionWrapper.getMacroDecommissioned().isEmpty())
					sitesVisailizationMap = getSiteVisualizeMap(siteSelectionWrapper.getMacroDecommissioned(),
							southWestLong, southWestLat, northEastLong, northEastLat, zoomLevel, sitesVisailizationMap,
							type);
				if (siteSelectionWrapper.getOnairOutdoorWifi() != null
						&& !siteSelectionWrapper.getOnairOutdoorWifi().isEmpty())
					sitesVisailizationMap = getSiteVisualiseMapForWifi(siteSelectionWrapper.getOnairOutdoorWifi(),
							southWestLong, southWestLat, northEastLong, northEastLat, zoomLevel, sitesVisailizationMap,
							type);

				if (siteSelectionWrapper.getPicoOnAir() != null && !siteSelectionWrapper.getPicoOnAir().isEmpty())
					sitesVisailizationMap = getSiteVisualizeMap(siteSelectionWrapper.getPicoOnAir(), southWestLong,
							southWestLat, northEastLong, northEastLat, zoomLevel, sitesVisailizationMap, type);

				if (siteSelectionWrapper.getShooterOnAir() != null && !siteSelectionWrapper.getShooterOnAir().isEmpty())
					sitesVisailizationMap = getSiteVisualizeMap(siteSelectionWrapper.getShooterOnAir(), southWestLong,
							southWestLat, northEastLong, northEastLat, zoomLevel, sitesVisailizationMap, type);

				if (siteSelectionWrapper.getPlannedPico() != null && !siteSelectionWrapper.getPlannedPico().isEmpty())
					sitesVisailizationMap = getSiteVisualizeMap(siteSelectionWrapper.getPlannedPico(), southWestLong,
							southWestLat, northEastLong, northEastLat, zoomLevel, sitesVisailizationMap, type);
				if (siteSelectionWrapper.getPlannedShooter() != null
						&& !siteSelectionWrapper.getPlannedShooter().isEmpty())
					sitesVisailizationMap = getSiteVisualizeMap(siteSelectionWrapper.getPlannedShooter(), southWestLong,
							southWestLat, northEastLong, northEastLat, zoomLevel, sitesVisailizationMap, type);

			}
		} catch (Exception exception) {
			logger.error("Unable to get count for sites for NEParameter {} ", Utils.getStackTrace(exception));
		}
		return sitesVisailizationMap;
	}

	private Map<String, List<NetworkElementWrapper>> getSiteVisualiseMapForWifi(Map map, Double southWestLong,
			Double southWestLat, Double northEastLong, Double northEastLat, Integer zoomLevel,
			Map<String, List<NetworkElementWrapper>> sitesVisailizeMap, String type) {
		Map<String, List<NetworkElementWrapper>> sitesMap = new HashMap<>();
		sitesMap = getNetworkElementsForSiteLayer(map, southWestLong, southWestLat, northEastLong, northEastLat,
				zoomLevel, InfraConstants.WIFI);
		try {
			List<NetworkElementWrapper> tempSiteCountList = new ArrayList<>();
			List<NetworkElementWrapper> tempSiteVisualizeList = new ArrayList<>();
			if (sitesVisailizeMap != null) {
				if (sitesMap != null && !sitesMap.isEmpty()) {
					tempSiteCountList = sitesMap.get(InfraConstants.SITES_COUNT_IN_NETWORK);
					tempSiteVisualizeList = sitesMap.get(InfraConstants.SITES_VISUALIZE_IN_NETWORK);
					if (tempSiteCountList != null && !tempSiteCountList.isEmpty()) {
						sitesVisailizeMap = mergeMapByKey(InfraConstants.SITES_COUNT_IN_NETWORK, sitesVisailizeMap,
								tempSiteCountList);
					}
					if (tempSiteVisualizeList != null && !tempSiteVisualizeList.isEmpty()) {
						sitesVisailizeMap = mergeMapByKey(InfraConstants.SITES_VISUALIZE_IN_NETWORK, sitesVisailizeMap,
								tempSiteVisualizeList);
					}
				}
			} else {
				sitesVisailizeMap = sitesMap;
			}

		} catch (Exception exception) {
			logger.error("Unable to get data for wifi", Utils.getStackTrace(exception));
		}
		return sitesVisailizeMap;
	}

	private Map<String, List<NetworkElementWrapper>> getSiteVisualizeMap(Map map, Double southWestLong,
			Double southWestLat, Double northEastLong, Double northEastLat, Integer zoomLevel,
			Map<String, List<NetworkElementWrapper>> sitesVisailizeMap, String type) {
		Map<String, List<NetworkElementWrapper>> sitesMap = new HashMap<>();
		List<NetworkElementWrapper> tempSiteCountList = new ArrayList<>();
		List<NetworkElementWrapper> tempSiteVisualizeList = new ArrayList<>();
		try {
			logger.info("Going to get SiteVisualize Map for type {} ", type);
			sitesMap = getNetworkElementsForSiteLayer(map, southWestLong, southWestLat, northEastLong, northEastLat,
					zoomLevel, type);
			if (sitesVisailizeMap != null && !sitesVisailizeMap.isEmpty()) {
				if (sitesMap != null && !sitesMap.isEmpty()) {
					tempSiteCountList = sitesMap.get(InfraConstants.SITES_COUNT_IN_NETWORK);
					tempSiteVisualizeList = sitesMap.get(InfraConstants.SITES_VISUALIZE_IN_NETWORK);
					if (tempSiteCountList != null && !tempSiteCountList.isEmpty()) {
						sitesVisailizeMap = mergeMapByKey(InfraConstants.SITES_COUNT_IN_NETWORK, sitesVisailizeMap,
								tempSiteCountList);
					}
					if (tempSiteVisualizeList != null && !tempSiteVisualizeList.isEmpty()) {
						sitesVisailizeMap = mergeMapByKey(InfraConstants.SITES_VISUALIZE_IN_NETWORK, sitesVisailizeMap,
								tempSiteVisualizeList);
					}
				}
			} else {
				sitesVisailizeMap = sitesMap;
			}
		} catch (NullPointerException exception) {
			logger.error("Unable get SiteVisualize Map due to Exception {}", Utils.getStackTrace(exception));
		} catch (Exception exception) {
			logger.error("Unable to get site Visualize map {} ", Utils.getStackTrace(exception));
		}
		return sitesVisailizeMap;
	}

	private Map<String, List<NetworkElementWrapper>> getNetworkElementsForSiteLayer(Map map, Double southWestLong,
			Double southWestLat, Double northEastLong, Double northEastLat, Integer zoomLevel, String type) {
		List<NetworkElementWrapper> networkElementVisualList = new ArrayList<>();
		List<NetworkElementWrapper> networkElementList = new ArrayList<>();
		Map<String, List<NetworkElementWrapper>> sitesMap = new HashMap<>();
		List<String> siteCategory = new ArrayList<>();
		List<NEType> netypeWifi = new ArrayList<>();
		NEType neType = null;
		try {
			if (map != null && !map.isEmpty()) {
				Integer displaySites = getDisplaySiteValue(map);
				List<String> bandList = getNEFrequencies(map);
				NEStatus neStatus = getProgressStateForSiteLayer(map);
				String criteria = getNECriteriaForPCI(map);
				String failureRate = getNEFailureRateForPCI(map);
				List<String> morphology = getMorphology(map);
				List<Vendor> vendor = getVendorList(map);
				List<Technology> technologies = getTechnologyList(map);
				String geography = (String) map.get(GEOGRAPHY);
				if (type.equalsIgnoreCase(InfraConstants.WIFI)) {
					netypeWifi = getNETypeWifi(map);
				} else {
					neType = getNETypes(map);
				}
				List<String> taskStatus = getNeTasksForSite(map);
				if (zoomLevel < displaySites) {
					String geographyLevel = getGeographyForAggregation(zoomLevel);
					List<String> geographyList = getDistinctGeography(geographyLevel, southWestLong, southWestLat,
							northEastLong, northEastLat);
					if (geographyList != null && !geographyList.isEmpty()) {
						logger.info(
								"Going to get counts For geographyLevel {} status {} band {} neTypes {} displaySites {} vendor {} ,technologies {} ,morphology {}",
								geographyLevel, neStatus, bandList, neType, displaySites, vendor, technologies,
								morphology);
						networkElementList = getSiteLayerCount(type, networkElementList, southWestLong, southWestLat,
								northEastLong, northEastLat, bandList, neStatus, criteria, failureRate, neType, vendor,
								technologies, netypeWifi, siteCategory, taskStatus, geographyList, geographyLevel,
								morphology);
					} else {
						logger.warn("No Geography Found in ViewPort");
						throw new RestException("No Geography Found in ViewPort");
					}
				} else {
					networkElementVisualList = getSiteVisualisationLayer(type, networkElementVisualList, southWestLong,
							southWestLat, northEastLong, northEastLat, bandList, neStatus, criteria, failureRate,
							neType, vendor, technologies, netypeWifi, siteCategory, taskStatus, morphology, geography);
				}
			}
			sitesMap.put(InfraConstants.SITES_COUNT_IN_NETWORK, networkElementList);
			sitesMap.put(InfraConstants.SITES_VISUALIZE_IN_NETWORK, networkElementVisualList);
		} catch (IllegalArgumentException | NullPointerException exception) {
			logger.error("Unable to get Network elements for Site layer due to exception {}",
					Utils.getStackTrace(exception));
		} catch (Exception exception) {
			logger.error("Unable to get Network elements for Site layer {}", Utils.getStackTrace(exception));
		}
		return sitesMap;
	}

	private List<NetworkElementWrapper> getSiteLayerCount(String type, List<NetworkElementWrapper> networkElementList,
			Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat, List<String> bandList,
			NEStatus neStatus, String criteria, String failureRate, NEType neType, List<Vendor> vendor,
			List<Technology> technologies, List<NEType> netypeWifi, List<String> siteCategory, List<String> taskStatus,
			List<String> geographyList, String geographyLevel, List<String> morphology) {
		try {
			if (type.equalsIgnoreCase(InfraConstants.WIFI)) {
				networkElementList = networkElementDao.getAggregateCountForWifi(netypeWifi, neStatus.name(),
						southWestLong, southWestLat, northEastLong, northEastLat, geographyLevel, bandList,
						siteCategory, taskStatus);
			} else if (type.equalsIgnoreCase(InfraConstants.TABLEVIEW)) {
				networkElementList = networkElementDao.getNetworkElementForTableView(neType.name(), neStatus.name(),
						southWestLong, southWestLat, northEastLong, northEastLat, geographyLevel, bandList,
						siteCategory, taskStatus, vendor, technologies, geographyList, morphology);
			} else {
				networkElementList = networkElementDao.getNetworkElementForAggregateLayerCount(neType.name(),
						neStatus.name(), southWestLong, southWestLat, northEastLong, northEastLat, geographyLevel,
						bandList, siteCategory, taskStatus, vendor, technologies, geographyList, morphology);
			}
		} catch (IllegalArgumentException | NullPointerException exception) {
			logger.error("Unable to get Network elements for Site layer due to exception {}",
					Utils.getStackTrace(exception));
		} catch (Exception exception) {
			logger.error("Unable to get Network elements for Site layer {}", Utils.getStackTrace(exception));
		}
		return networkElementList;
	}

	private List<NetworkElementWrapper> getSiteVisualisationLayer(String type,
			List<NetworkElementWrapper> networkElementVisualList, Double southWestLong, Double southWestLat,
			Double northEastLong, Double northEastLat, List<String> bandList, NEStatus neStatus, String criteria,
			String failureRate, NEType neType, List<Vendor> vendor, List<Technology> technologies,
			List<NEType> netypeWifi, List<String> siteCategory, List<String> taskStatus, List<String> morphology,
			String geography) {
		try {
			logger.info("Going to get counts for status {} band {} neTypes {} displaySites {},netypeWifi {}", neStatus,
					bandList, neType, netypeWifi);
			if (type.equalsIgnoreCase(InfraConstants.WIFI)) {
				networkElementVisualList = networkElementDao.getActualCountForWifi(netypeWifi, neStatus.name(),
						southWestLong, southWestLat, northEastLong, northEastLat, bandList, siteCategory, taskStatus);
			} else {
				if (neStatus.name().equalsIgnoreCase(InfraConstants.PLANNED)) {
					networkElementVisualList = networkElementDao.getActualPlannedSites(neType.name(), neStatus.name(),
							southWestLong, southWestLat, northEastLong, northEastLat, bandList, siteCategory,
							taskStatus, vendor, technologies, morphology);
					NEType sitetype = getCellSitetype(neType);
					networkElementVisualList
							.addAll(networkElementDao.getNetworkElementForActualSites(sitetype.name(), neStatus.name(),
									southWestLong, southWestLat, northEastLong, northEastLat, bandList, siteCategory,
									taskStatus, criteria, failureRate, vendor, technologies, morphology, geography));

				} else {
					networkElementVisualList = networkElementDao.getNetworkElementForActualSites(neType.name(),
							neStatus.name(), southWestLong, southWestLat, northEastLong, northEastLat, bandList,
							siteCategory, taskStatus, criteria, failureRate, vendor, technologies, morphology,
							geography);
				}
			}
		} catch (IllegalArgumentException | NullPointerException exception) {
			logger.error("Unable to get Network elements for Site layer due to exception {}",
					Utils.getStackTrace(exception));
		} catch (Exception exception) {
			logger.error("Unable to get Network elements for Site layer {}", Utils.getStackTrace(exception));
		}
		return networkElementVisualList;
	}

	private List<NEType> getNETypeWifi(Map map) {
		List netype = new ArrayList<>();
		try {
			if (map != null && !map.isEmpty()) {
				if (map.get(InfraConstants.SITETYPE) != null) {
					netype = (List) map.get(InfraConstants.SITETYPE);
				}
			}
		} catch (Exception exception) {
			logger.error("Unable to get NEType for Wifi {}", Utils.getStackTrace(exception));
		}
		return netype;
	}

	private Integer getDisplaySiteValue(Map map) {
		try {
			if (map != null) {
				if (map.get(ForesightConstants.DISPLAY_SITES) != null) {
					return Integer.parseInt(map.get(ForesightConstants.DISPLAY_SITES).toString());
				}
			}
		} catch (Exception exception) {
			logger.error("Unable to get Display sites value {}", Utils.getStackTrace(exception));
		}
		return null;
	}

	private List<String> getNeTasksForSite(Map map) {
		try {
			if (map != null && !map.isEmpty()) {
				if (map.get(InfraConstants.NE_SITE_TASK_KEY) != null) {
					return (List<String>) map.get(InfraConstants.NE_SITE_TASK_KEY);
				}
			}
		} catch (Exception exception) {
			logger.error("Unable to get NeTasks For sites Exception {}", Utils.getStackTrace(exception));
		}
		return new ArrayList<>();
	}

	private List<String> getNEFrequencies(Map map) {
		try {
			if (map != null && !map.isEmpty()) {
				return (List) map.get(InfraConstants.LAYER_BANDS);
			}
		} catch (Exception exception) {
			logger.error("Unable to get NEFrequencies for layer count {}", Utils.getStackTrace(exception));
		}
		return null;
	}

	private List<String> getMorphology(Map map) {
		try {
			if (map != null && !map.isEmpty() && map.get("Morphology") != null) {
				return (List<String>) map.get("Morphology");
			}
		} catch (Exception exception) {
			logger.error("Unable to get Morphology for layer count {}", Utils.getStackTrace(exception));
		}
		return null;
	}

	private List<Vendor> getVendorList(Map map) {
		List<Vendor> vendorList = new ArrayList<>();
		try {
			if (map != null && !map.isEmpty() && map.get(InfraConstants.SITE_VENDORS) != null) {
				for (String string : (List<String>) map.get(InfraConstants.SITE_VENDORS)) {
					vendorList.add(Vendor.valueOf(string));
				}
			}
			return vendorList;
		} catch (Exception exception) {
			logger.error("Unable to get NEFrequencies for layer count {}", Utils.getStackTrace(exception));
		}
		return vendorList;
	}

	@Override
	public List<NetworkElementWrapper> getAggregatedCountByGeographyLevel(SiteSelectionWrapper siteSelectionWrapper,
			Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat, Integer zoomLevel) {
		Map<String, List<NetworkElementWrapper>> sitesVisualizationMap = new HashMap<>();
		List<NetworkElementWrapper> aggregateCountList = new ArrayList<>();
		try {
			sitesVisualizationMap = getSitesCountByNEParameter(siteSelectionWrapper, southWestLong, southWestLat,
					northEastLong, northEastLat, zoomLevel, InfraConstants.TABLEVIEW);

			if (sitesVisualizationMap != null && !sitesVisualizationMap.isEmpty()) {
				if (sitesVisualizationMap.get(InfraConstants.SITES_COUNT_IN_NETWORK) != null
						&& !sitesVisualizationMap.get(InfraConstants.SITES_COUNT_IN_NETWORK).isEmpty()) {
					aggregateCountList = sitesVisualizationMap.get(InfraConstants.SITES_COUNT_IN_NETWORK);
				} else {
					aggregateCountList = sitesVisualizationMap.get(InfraConstants.SITES_VISUALIZE_IN_NETWORK);
				}
			}
		} catch (Exception exception) {
			logger.error("Error in getting aggregated count for geographylevel and netype {} ",
					Utils.getStackTrace(exception));
		}
		return aggregateCountList;
	}

	private Map<String, Long> getAggregatedCountsForKpi(List<NetworkElementWrapper> combinedList, String neStatus) {
		Map<String, Long> kpiCountMap = new HashMap<>();
		try {
			kpiCountMap = combinedList.stream().collect(Collectors.groupingBy(NetworkElementWrapper::getStatus,
					Collectors.summingLong(NetworkElementWrapper::getTotalCount)));
			Long totalCount = combinedList.stream().mapToLong(nw -> nw.getTotalCount()).sum();
			kpiCountMap.put(InfraConstants.TOTAL_SITE_COUNTS, totalCount);
		} catch (Exception exception) {
			logger.error("Unable to get the aggregated Count for kpi {}", Utils.getStackTrace(exception));
		}
		return kpiCountMap;
	}

	private Map<String, Long> getAggregatedCountsForOffairSites(List<NetworkElementWrapper> combinedList) {
		Map<String, Long> kpiCountMap = new HashMap<>();
		try {
			List<NetworkElementWrapper> networkElementWrappers = new ArrayList<>();

			networkElementWrappers = getAggregatedCountsForSites(combinedList);
			for (NetworkElementWrapper networkElementWrapper : networkElementWrappers) {
				kpiCountMap.put(networkElementWrapper.getProgressState(), networkElementWrapper.getTotalCount());
			}

			Long totalCount = networkElementWrappers.stream().mapToLong(nw -> nw.getTotalCount()).sum();
			kpiCountMap.put(InfraConstants.TOTAL_SITE_COUNTS, totalCount);
		} catch (Exception exception) {
			logger.error("Unable to get the aggregated Count for kpi {}", Utils.getStackTrace(exception));
		}
		return kpiCountMap;
	}

	private String getNECriteriaForPCI(Map map) {
		try {
			if (map.get("criteria") != null) {
				return map.get("criteria").toString();
			}
		} catch (Exception exception) {
			logger.error("Error in getting PCI criteria Message {}", exception.getMessage());
		}
		return null;
	}

	private String getNEFailureRateForPCI(Map map) {
		try {
			if (map.get("failureRate") != null) {
				return map.get("failureRate").toString();
			}
		} catch (Exception exception) {
			logger.error("Error in getting PCI value Message {}", exception.getMessage());
		}
		return null;
	}

	@Override
	@Transactional
	public List<SiteMileStoneDetailWrapper> getSiteMilestoneDetails(String neName) {
		List<SiteMileStoneDetailWrapper> tasksList = new ArrayList<>();
		try {
			logger.info("Going to get SiteMilestoneDetails for neName  {}", neName);
			List<NETaskDetail> neTaskDetailDataList = networkElementDao.getNETaskDetailForNE(neName);
			if (neTaskDetailDataList != null && !neTaskDetailDataList.isEmpty()) {
				logger.info("Total {} records retrived from NETaskDetail for neName {}", neTaskDetailDataList.size(),
						neName);
				return getSiteMilestoneDataForSites(neTaskDetailDataList);
			}
		} catch (Exception exception) {
			logger.error("Error in getting SiteMilestoneDetails for neName {} Exception {}", neName,
					Utils.getStackTrace(exception));
		}
		return tasksList;
	}

	private List<SiteMileStoneDetailWrapper> getSiteMilestoneDataForSites(List<NETaskDetail> neTaskDetailDataList) {
		logger.info("Going to get SiteMilestoneDetails for sites");
		List<SiteMileStoneDetailWrapper> tasksList = new ArrayList<>();
		Date initialDate = null;
		for (NETaskDetail neTaskDetailData : neTaskDetailDataList) {
			try {
				if (neTaskDetailData != null && neTaskDetailData.getNeBandDetail().getNeFrequency() != null) {
					SiteMileStoneDetailWrapper siteMileStoneDetailWrapper = new SiteMileStoneDetailWrapper();
					String newDate = null;
					if (neTaskDetailData.getTaskName() != null
							&& neTaskDetailData.getTaskName().equalsIgnoreCase(InfraConstants.NOMINAL)) {
						siteMileStoneDetailWrapper.setBand(neTaskDetailData.getNeBandDetail().getNeFrequency());
						siteMileStoneDetailWrapper.setSiteStage(InfraConstants.NOMINAL_RELEASE);
						siteMileStoneDetailWrapper
								.setTaskName(neTaskDetailData.getTaskName() != null ? neTaskDetailData.getTaskName()
										: ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskStatus(
								neTaskDetailData.getTaskStatus() != null ? neTaskDetailData.getTaskStatus()
										: ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskCompletionStatus(
								neTaskDetailData.getCompletionStatus() != null ? neTaskDetailData.getCompletionStatus()
										: ForesightConstants.HIPHEN);
						newDate = InfraUtils.getSiteTaskDateForSectorProperty(neTaskDetailData.getActualEndDate(),
								false);
						siteMileStoneDetailWrapper
								.setTaskCompletionDate(newDate != null ? newDate : ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskDays(0);
						if (neTaskDetailData.getActualEndDate() != null) {
							initialDate = neTaskDetailData.getActualEndDate();
						}
						tasksList.add(siteMileStoneDetailWrapper);
					}

					siteMileStoneDetailWrapper = null;

					if (neTaskDetailData.getTaskName() != null
							&& neTaskDetailData.getTaskName().equalsIgnoreCase(ForesightConstants.STATUS_RFA)) {
						siteMileStoneDetailWrapper = new SiteMileStoneDetailWrapper();
						siteMileStoneDetailWrapper.setBand(neTaskDetailData.getNeBandDetail().getNeFrequency());
						siteMileStoneDetailWrapper.setSiteStage(ForesightConstants.STATUS_RFA);
						siteMileStoneDetailWrapper
								.setTaskName(neTaskDetailData.getTaskName() != null ? neTaskDetailData.getTaskName()
										: ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskStatus(
								neTaskDetailData.getTaskStatus() != null ? neTaskDetailData.getTaskStatus()
										: ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskCompletionStatus(
								neTaskDetailData.getCompletionStatus() != null ? neTaskDetailData.getCompletionStatus()
										: ForesightConstants.HIPHEN);
						newDate = InfraUtils.getSiteTaskDateForSectorProperty(neTaskDetailData.getActualEndDate(),
								false);
						siteMileStoneDetailWrapper
								.setTaskCompletionDate(newDate != null ? newDate : ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskDays(
								InfraUtils.getDaysBetweenTwoDates(initialDate, neTaskDetailData.getActualEndDate()));
						if (neTaskDetailData.getActualEndDate() != null) {
							initialDate = neTaskDetailData.getActualEndDate();
						}
						tasksList.add(siteMileStoneDetailWrapper);
					}

					siteMileStoneDetailWrapper = null;

					if (neTaskDetailData.getTaskName() != null
							&& neTaskDetailData.getTaskName().equalsIgnoreCase(ForesightConstants.STATUS_RFC)) {
						siteMileStoneDetailWrapper = new SiteMileStoneDetailWrapper();
						siteMileStoneDetailWrapper.setBand(neTaskDetailData.getNeBandDetail().getNeFrequency());
						siteMileStoneDetailWrapper.setSiteStage(ForesightConstants.STATUS_RFC);
						siteMileStoneDetailWrapper
								.setTaskName(neTaskDetailData.getTaskName() != null ? neTaskDetailData.getTaskName()
										: ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskStatus(
								neTaskDetailData.getTaskStatus() != null ? neTaskDetailData.getTaskStatus()
										: ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskCompletionStatus(
								neTaskDetailData.getCompletionStatus() != null ? neTaskDetailData.getCompletionStatus()
										: ForesightConstants.HIPHEN);
						newDate = InfraUtils.getSiteTaskDateForSectorProperty(neTaskDetailData.getActualEndDate(),
								false);
						siteMileStoneDetailWrapper
								.setTaskCompletionDate(newDate != null ? newDate : ForesightConstants.HIPHEN);

						siteMileStoneDetailWrapper.setTaskDays(
								InfraUtils.getDaysBetweenTwoDates(initialDate, neTaskDetailData.getActualEndDate()));
						if (neTaskDetailData.getActualEndDate() != null) {
							initialDate = neTaskDetailData.getActualEndDate();
						}
						tasksList.add(siteMileStoneDetailWrapper);
					}

					siteMileStoneDetailWrapper = null;

					if (neTaskDetailData.getTaskName() != null
							&& neTaskDetailData.getTaskName().equalsIgnoreCase(InfraConstants.STATUS_RFE1SURVEY)) {
						siteMileStoneDetailWrapper = new SiteMileStoneDetailWrapper();
						siteMileStoneDetailWrapper.setBand(neTaskDetailData.getNeBandDetail().getNeFrequency());
						siteMileStoneDetailWrapper.setSiteStage(ForesightConstants.RFE1_SURVEY);
						siteMileStoneDetailWrapper
								.setTaskName(neTaskDetailData.getTaskName() != null ? neTaskDetailData.getTaskName()
										: ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskStatus(
								neTaskDetailData.getTaskStatus() != null ? neTaskDetailData.getTaskStatus()
										: ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskCompletionStatus(
								neTaskDetailData.getCompletionStatus() != null ? neTaskDetailData.getCompletionStatus()
										: ForesightConstants.HIPHEN);
						newDate = InfraUtils.getSiteTaskDateForSectorProperty(neTaskDetailData.getActualEndDate(),
								false);
						siteMileStoneDetailWrapper
								.setTaskCompletionDate(newDate != null ? newDate : ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskDays(
								InfraUtils.getDaysBetweenTwoDates(initialDate, neTaskDetailData.getActualEndDate()));
						initialDate = neTaskDetailData.getActualEndDate();
						tasksList.add(siteMileStoneDetailWrapper);
					}

					siteMileStoneDetailWrapper = null;

					if (neTaskDetailData.getTaskName() != null
							&& neTaskDetailData.getTaskName().equalsIgnoreCase(InfraConstants.STATUS_RFE1ACCEPTANCE)) {
						siteMileStoneDetailWrapper = new SiteMileStoneDetailWrapper();
						siteMileStoneDetailWrapper.setBand(neTaskDetailData.getNeBandDetail().getNeFrequency());
						siteMileStoneDetailWrapper.setSiteStage(ForesightConstants.RFE1_ACCEPTANCE);
						siteMileStoneDetailWrapper
								.setTaskName(neTaskDetailData.getTaskName() != null ? neTaskDetailData.getTaskName()
										: ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskStatus(
								neTaskDetailData.getTaskStatus() != null ? neTaskDetailData.getTaskStatus()
										: ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskCompletionStatus(
								neTaskDetailData.getCompletionStatus() != null ? neTaskDetailData.getCompletionStatus()
										: ForesightConstants.HIPHEN);
						newDate = InfraUtils.getSiteTaskDateForSectorProperty(neTaskDetailData.getActualEndDate(),
								false);
						siteMileStoneDetailWrapper
								.setTaskCompletionDate(newDate != null ? newDate : ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskDays(
								InfraUtils.getDaysBetweenTwoDates(initialDate, neTaskDetailData.getActualEndDate()));
						if (neTaskDetailData.getActualEndDate() != null) {
							initialDate = neTaskDetailData.getActualEndDate();
						}
						tasksList.add(siteMileStoneDetailWrapper);
					}

					siteMileStoneDetailWrapper = null;

					if (neTaskDetailData.getTaskName() != null
							&& neTaskDetailData.getTaskName().equalsIgnoreCase(InfraConstants.STATUS_INSTALLENODEB)) {
						siteMileStoneDetailWrapper = new SiteMileStoneDetailWrapper();
						siteMileStoneDetailWrapper.setBand(neTaskDetailData.getNeBandDetail().getNeFrequency());
						siteMileStoneDetailWrapper.setSiteStage(ForesightConstants.INSTALL_ENODEB);
						siteMileStoneDetailWrapper
								.setTaskName(neTaskDetailData.getTaskName() != null ? neTaskDetailData.getTaskName()
										: ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskStatus(
								neTaskDetailData.getTaskStatus() != null ? neTaskDetailData.getTaskStatus()
										: ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskCompletionStatus(
								neTaskDetailData.getCompletionStatus() != null ? neTaskDetailData.getCompletionStatus()
										: ForesightConstants.HIPHEN);
						newDate = InfraUtils.getSiteTaskDateForSectorProperty(neTaskDetailData.getActualEndDate(),
								false);
						siteMileStoneDetailWrapper
								.setTaskCompletionDate(newDate != null ? newDate : ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskDays(
								InfraUtils.getDaysBetweenTwoDates(initialDate, neTaskDetailData.getActualEndDate()));
						if (neTaskDetailData.getActualEndDate() != null) {
							initialDate = neTaskDetailData.getActualEndDate();
						}
						tasksList.add(siteMileStoneDetailWrapper);
					}

					siteMileStoneDetailWrapper = null;

					if (neTaskDetailData.getTaskName() != null && neTaskDetailData.getTaskName()
							.equalsIgnoreCase(InfraConstants.STATUS_COMMISSIONENODEB)) {
						siteMileStoneDetailWrapper = new SiteMileStoneDetailWrapper();
						siteMileStoneDetailWrapper.setBand(neTaskDetailData.getNeBandDetail().getNeFrequency());
						siteMileStoneDetailWrapper.setSiteStage(ForesightConstants.COMMMISSION_ENODEB);
						siteMileStoneDetailWrapper
								.setTaskName(neTaskDetailData.getTaskName() != null ? neTaskDetailData.getTaskName()
										: ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskStatus(
								neTaskDetailData.getTaskStatus() != null ? neTaskDetailData.getTaskStatus()
										: ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskCompletionStatus(
								neTaskDetailData.getCompletionStatus() != null ? neTaskDetailData.getCompletionStatus()
										: ForesightConstants.HIPHEN);
						newDate = InfraUtils.getSiteTaskDateForSectorProperty(neTaskDetailData.getActualEndDate(),
								false);
						siteMileStoneDetailWrapper
								.setTaskCompletionDate(newDate != null ? newDate : ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskDays(
								InfraUtils.getDaysBetweenTwoDates(initialDate, neTaskDetailData.getActualEndDate()));
						if (neTaskDetailData.getActualEndDate() != null) {
							initialDate = neTaskDetailData.getActualEndDate();
						}
						tasksList.add(siteMileStoneDetailWrapper);
					}

					siteMileStoneDetailWrapper = null;

					if (neTaskDetailData.getTaskName() != null
							&& neTaskDetailData.getTaskName().equalsIgnoreCase(InfraConstants.STATUS_EXECUTEATP11A)) {
						siteMileStoneDetailWrapper = new SiteMileStoneDetailWrapper();
						siteMileStoneDetailWrapper.setBand(neTaskDetailData.getNeBandDetail().getNeFrequency());
						siteMileStoneDetailWrapper.setSiteStage(ForesightConstants.EXECUTE_ATP_11A);
						siteMileStoneDetailWrapper
								.setTaskName(neTaskDetailData.getTaskName() != null ? neTaskDetailData.getTaskName()
										: ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskStatus(
								neTaskDetailData.getTaskStatus() != null ? neTaskDetailData.getTaskStatus()
										: ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskCompletionStatus(
								neTaskDetailData.getCompletionStatus() != null ? neTaskDetailData.getCompletionStatus()
										: ForesightConstants.HIPHEN);
						newDate = InfraUtils.getSiteTaskDateForSectorProperty(neTaskDetailData.getActualEndDate(),
								false);
						siteMileStoneDetailWrapper
								.setTaskCompletionDate(newDate != null ? newDate : ForesightConstants.HIPHEN);

						siteMileStoneDetailWrapper.setTaskDays(
								InfraUtils.getDaysBetweenTwoDates(initialDate, neTaskDetailData.getActualEndDate()));
						if (neTaskDetailData.getActualEndDate() != null) {
							initialDate = neTaskDetailData.getActualEndDate();
						}
						tasksList.add(siteMileStoneDetailWrapper);
					}

					siteMileStoneDetailWrapper = null;

					if (neTaskDetailData.getTaskName() != null
							&& neTaskDetailData.getTaskName().equalsIgnoreCase(InfraConstants.STATUS_EXECUTEATP11B)) {
						siteMileStoneDetailWrapper = new SiteMileStoneDetailWrapper();
						siteMileStoneDetailWrapper.setBand(neTaskDetailData.getNeBandDetail().getNeFrequency());
						siteMileStoneDetailWrapper.setSiteStage(ForesightConstants.EXECUTE_ATP_11B);
						siteMileStoneDetailWrapper
								.setTaskName(neTaskDetailData.getTaskName() != null ? neTaskDetailData.getTaskName()
										: ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskStatus(
								neTaskDetailData.getTaskStatus() != null ? neTaskDetailData.getTaskStatus()
										: ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskCompletionStatus(
								neTaskDetailData.getCompletionStatus() != null ? neTaskDetailData.getCompletionStatus()
										: ForesightConstants.HIPHEN);
						newDate = InfraUtils.getSiteTaskDateForSectorProperty(neTaskDetailData.getActualEndDate(),
								false);
						siteMileStoneDetailWrapper
								.setTaskCompletionDate(newDate != null ? newDate : ForesightConstants.HIPHEN);

						siteMileStoneDetailWrapper.setTaskDays(
								InfraUtils.getDaysBetweenTwoDates(initialDate, neTaskDetailData.getActualEndDate()));
						if (neTaskDetailData.getActualEndDate() != null) {
							initialDate = neTaskDetailData.getActualEndDate();
						}
						tasksList.add(siteMileStoneDetailWrapper);
					}

					siteMileStoneDetailWrapper = null;
					if (neTaskDetailData.getTaskName() != null
							&& neTaskDetailData.getTaskName().equalsIgnoreCase(InfraConstants.STATUS_EXECUTEEMF)) {
						siteMileStoneDetailWrapper = new SiteMileStoneDetailWrapper();
						siteMileStoneDetailWrapper.setBand(neTaskDetailData.getNeBandDetail().getNeFrequency());
						siteMileStoneDetailWrapper.setSiteStage(ForesightConstants.EXECUTE_EMF);
						siteMileStoneDetailWrapper
								.setTaskName(neTaskDetailData.getTaskName() != null ? neTaskDetailData.getTaskName()
										: ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskStatus(
								neTaskDetailData.getTaskStatus() != null ? neTaskDetailData.getTaskStatus()
										: ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskCompletionStatus(
								neTaskDetailData.getCompletionStatus() != null ? neTaskDetailData.getCompletionStatus()
										: ForesightConstants.HIPHEN);
						newDate = InfraUtils.getSiteTaskDateForSectorProperty(neTaskDetailData.getActualEndDate(),
								false);
						siteMileStoneDetailWrapper
								.setTaskCompletionDate(newDate != null ? newDate : ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskDays(
								InfraUtils.getDaysBetweenTwoDates(initialDate, neTaskDetailData.getActualEndDate()));
						if (neTaskDetailData.getActualEndDate() != null) {
							initialDate = neTaskDetailData.getActualEndDate();
						}
						tasksList.add(siteMileStoneDetailWrapper);
					}

					siteMileStoneDetailWrapper = null;

					if (neTaskDetailData.getTaskName() != null
							&& neTaskDetailData.getTaskName().equalsIgnoreCase(InfraConstants.STATUS_EXECUTESCFT)) {
						siteMileStoneDetailWrapper = new SiteMileStoneDetailWrapper();
						siteMileStoneDetailWrapper.setBand(neTaskDetailData.getNeBandDetail().getNeFrequency());
						siteMileStoneDetailWrapper.setSiteStage(ForesightConstants.EXECUTE_SCFT);
						siteMileStoneDetailWrapper
								.setTaskName(neTaskDetailData.getTaskName() != null ? neTaskDetailData.getTaskName()
										: ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskStatus(
								neTaskDetailData.getTaskStatus() != null ? neTaskDetailData.getTaskStatus()
										: ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskCompletionStatus(
								neTaskDetailData.getCompletionStatus() != null ? neTaskDetailData.getCompletionStatus()
										: ForesightConstants.HIPHEN);
						newDate = InfraUtils.getSiteTaskDateForSectorProperty(neTaskDetailData.getActualEndDate(),
								false);
						siteMileStoneDetailWrapper
								.setTaskCompletionDate(newDate != null ? newDate : ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskDays(
								InfraUtils.getDaysBetweenTwoDates(initialDate, neTaskDetailData.getActualEndDate()));
						if (neTaskDetailData.getActualEndDate() != null) {
							initialDate = neTaskDetailData.getActualEndDate();
						}
						tasksList.add(siteMileStoneDetailWrapper);
					}

					siteMileStoneDetailWrapper = null;

					if (neTaskDetailData.getTaskName() != null
							&& neTaskDetailData.getTaskName().equalsIgnoreCase(InfraConstants.STATUS_EXECUTESSCVT)) {
						siteMileStoneDetailWrapper = new SiteMileStoneDetailWrapper();
						siteMileStoneDetailWrapper.setBand(neTaskDetailData.getNeBandDetail().getNeFrequency());
						siteMileStoneDetailWrapper.setSiteStage(ForesightConstants.EXECUTE_SSCVT);
						siteMileStoneDetailWrapper
								.setTaskName(neTaskDetailData.getTaskName() != null ? neTaskDetailData.getTaskName()
										: ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskStatus(
								neTaskDetailData.getTaskStatus() != null ? neTaskDetailData.getTaskStatus()
										: ForesightConstants.HIPHEN);
						siteMileStoneDetailWrapper.setTaskCompletionStatus(
								neTaskDetailData.getCompletionStatus() != null ? neTaskDetailData.getCompletionStatus()
										: ForesightConstants.HIPHEN);
						newDate = InfraUtils.getSiteTaskDateForSectorProperty(neTaskDetailData.getActualEndDate(),
								false);
						siteMileStoneDetailWrapper
								.setTaskCompletionDate(newDate != null ? newDate : ForesightConstants.HIPHEN);

						siteMileStoneDetailWrapper.setTaskDays(
								InfraUtils.getDaysBetweenTwoDates(initialDate, neTaskDetailData.getActualEndDate()));
						if (neTaskDetailData.getActualEndDate() != null) {
							initialDate = neTaskDetailData.getActualEndDate();
						}
						tasksList.add(siteMileStoneDetailWrapper);

					}

				}
			} catch (Exception exception) {
				logger.warn("Error in getting SiteMilestoneDetails for sites Exception {}", exception.getMessage());
			}
		}
		return tasksList;
	}

	private List<NetworkElementWrapper> getAggregatedCountsByBand(List<NetworkElementWrapper> combinedList) {
		List<NetworkElementWrapper> countStatusWiseList = new ArrayList<>();
		try {
			Map<String, Map<String, List<NetworkElementWrapper>>> layerCountMap = combinedList.stream()
					.collect(Collectors.groupingBy(NetworkElementWrapper::getNeFrequency,
							Collectors.groupingBy(NetworkElementWrapper::getProgressState, Collectors.toList())));
			countStatusWiseList = getAggregatedCountsByBand(layerCountMap);
		} catch (Exception exception) {
			logger.error("Unable to get the aggregated Count for layer {}", Utils.getStackTrace(exception));
		}
		return countStatusWiseList;
	}

	private List<NetworkElementWrapper> getSidePanelDetails(
			Map<NEType, Map<String, List<NetworkElementWrapper>>> countMap) {
		List<NetworkElementWrapper> countStatusWise = new ArrayList<>();
		try {
			countStatusWise = getCountStatusAndNeType(countMap, countStatusWise);
		} catch (Exception exception) {
			logger.error("Unable to get list for SidePanelDetails {} ", Utils.getStackTrace(exception));
		}
		return countStatusWise;
	}

	private List<NetworkElementWrapper> getAggregatedCountsByBand(
			Map<String, Map<String, List<NetworkElementWrapper>>> countMap) {
		List<NetworkElementWrapper> countStatusWise = new ArrayList<>();
		try {
			countStatusWise = getCountStatusAndNeFrequency(countMap, countStatusWise);
		} catch (Exception exception) {
			logger.error("Unable to get list for SidePanelDetails {} ", Utils.getStackTrace(exception));
		}
		return countStatusWise;
	}

	private Map<String, Long> getAggregatedSiteCountsForKpi(List<NetworkElementWrapper> combinedList) {
		Map<String, Long> kpiCountMap = new HashMap<>();
		try {
			List<NetworkElementWrapper> networkElementWrappers = new ArrayList<>();

			networkElementWrappers = getAggregatedCountsByBand(combinedList);
			for (NetworkElementWrapper networkElementWrapper : networkElementWrappers) {
				kpiCountMap.put(networkElementWrapper.getNeFrequency(), networkElementWrapper.getTotalCount());
			}
			networkElementWrappers = null;
			networkElementWrappers = getAggregatedCountsForSites(combinedList);
			Long totalCount = networkElementWrappers.stream().mapToLong(nw -> nw.getTotalCount()).sum();
			kpiCountMap.put(InfraConstants.TOTAL_SITE_COUNTS, totalCount);
		} catch (Exception exception) {
			logger.error("Unable to get the aggregated Count for kpi {}", Utils.getStackTrace(exception));
		}
		return kpiCountMap;
	}

	private List<NetworkElementWrapper> getCountStatusAndNeType(
			Map<NEType, Map<String, List<NetworkElementWrapper>>> countMap,
			List<NetworkElementWrapper> countStatusWiseList) {
		try {
			countMap.forEach((k, v) -> {
				try {
					v.forEach((m, n) -> {
						try {
							NetworkElementWrapper elementWrapper = new NetworkElementWrapper();
							elementWrapper.setNeType(k);
							if (k.toString().equalsIgnoreCase(NEType.MACRO_CELL.name())) {
								elementWrapper.setTotalCount(n.stream()
										.filter(distinctByKey(p -> p.getSapid() + p.getNeFrequency())).count());
							} else if (k.toString().equalsIgnoreCase(NEType.ODSC_CELL.name())) {
								elementWrapper.setTotalCount(n.stream()
										.filter(distinctByKey(p -> p.getSapid() + p.getNeFrequency())).count());
							} else {
								elementWrapper.setTotalCount(
										n.stream().mapToLong(NetworkElementWrapper::getTotalCount).sum());
							}
							elementWrapper.setProgressState(m);
							countStatusWiseList.add(elementWrapper);
						} catch (Exception exception) {
							logger.error("Unable to get NetworkElementWrapper for key {}, value {} due to exception {}",
									m, n, Utils.getStackTrace(exception));
						}
					});
				} catch (Exception exception) {
					logger.error("Invalid key value for map {}", Utils.getStackTrace(exception));
				}

			});
		} catch (Exception exception) {
			logger.error("Invalid key value for map {}", Utils.getStackTrace(exception));
		}
		return countStatusWiseList;
	}

	private List<NetworkElementWrapper> getCountStatusAndNeFrequency(
			Map<String, Map<String, List<NetworkElementWrapper>>> countMap,
			List<NetworkElementWrapper> countStatusWiseList) {
		try {
			countMap.forEach((k, v) -> {
				try {
					v.forEach((m, n) -> {
						try {
							NetworkElementWrapper elementWrapper = new NetworkElementWrapper();
							elementWrapper.setNeFrequency(k);
							elementWrapper.setTotalCount(
									n.stream().filter(distinctByKey(p -> p.getSapid() + p.getNeFrequency())).count());
							elementWrapper.setProgressState(m);
							countStatusWiseList.add(elementWrapper);
						} catch (Exception exception) {
							logger.error("Unable to get NetworkElementWrapper for key {}, value {} due to exception {}",
									m, n, Utils.getStackTrace(exception));
						}
					});
				} catch (Exception exception) {
					logger.error("Invalid key value for map {}", Utils.getStackTrace(exception));
				}

			});
		} catch (Exception exception) {
			logger.error("Invalid key value for map {}", Utils.getStackTrace(exception));
		}
		return countStatusWiseList;
	}

	private List<NetworkElementWrapper> getAggregatedCountsForSites(List<NetworkElementWrapper> combinedList) {
		List<NetworkElementWrapper> countStatusWiseList = new ArrayList<>();
		try {
			Map<NEType, Map<String, List<NetworkElementWrapper>>> layerCountMap = combinedList.stream()
					.collect(Collectors.groupingBy(NetworkElementWrapper::getNeType,
							Collectors.groupingBy(NetworkElementWrapper::getStatus, Collectors.toList())));
			countStatusWiseList = getSidePanelDetails(layerCountMap);
		} catch (Exception exception) {
			logger.error("Unable to get the aggregated Count for layer {}", Utils.getStackTrace(exception));
		}
		return countStatusWiseList;
	}

	@Override
	@Transactional
	public Map<String, Long> getKPICountForPolygon(KPISummaryDataWrapper filterConfiguration, Integer zoomLevel) {
		try {
			Map<String, Long> countMapKpi = new HashMap<>();
			logger.info("Going to get the count for polygon drawn.");
			if (filterConfiguration != null && zoomLevel != null) {
				countMapKpi = getCustomCountForKpi(filterConfiguration);
			}
			Long sum = countMapKpi.values().stream().mapToLong(Number::longValue).sum();
			countMapKpi.put(InfraConstants.TOTAL_SITE_COUNTS, sum);
			logger.info("Band wise count is {}", countMapKpi);
			return countMapKpi;
		} catch (Exception exception) {
			logger.error("Exception in getting KPI count For Polygon  {} ", ExceptionUtils.getStackTrace(exception));
			throw new RestException(InfraConstants.INVALID_PARAMETERS);
		}
	}

	private Map<String, Long> getCustomCountForKpi(KPISummaryDataWrapper filterConfiguration) {
		Map<String, Long> kpiFinalMap = new HashMap<>();
		Map kpiMap = new HashMap<>();
		String progressState = null;
		String status = null;
		List<NEType> neTypeList = new ArrayList<>();
		List<NEStatus> neStatusList = new ArrayList<>();
		List<String> frequencyList = new ArrayList<>();
		List<List<List<Double>>> polygons = new ArrayList<>();
		try {
			if (filterConfiguration.getKpiMap() != null) {
				kpiMap = filterConfiguration.getKpiMap();
			}
			progressState = (String) kpiMap.get(InfraConstants.LAYER_PROGRESSSTATE);
			status = (String) kpiMap.get(ForesightConstants.SITE_TYPE);
			if (status.equalsIgnoreCase(InfraConstants.MACRO_CELL)) {
				status = InfraConstants.MACRO;
			}
			if (status.equalsIgnoreCase(InfraConstants.IBS_CELL)) {
				status = InfraConstants.IBS_SITE;
			}
			if (status.equalsIgnoreCase(InfraConstants.ODSC_CELL)) {
				status = InfraConstants.ODSC_SITE;
			}
			if (status.equalsIgnoreCase(InfraConstants.PICO_CELL)) {
				status = InfraConstants.PICO_SITE;
			}
			if (status.equalsIgnoreCase(InfraConstants.SHOOTER_CELL)) {
				status = InfraConstants.SHOOTER_SITE;
			}
			frequencyList = (List<String>) kpiMap.get(InfraConstants.BAND);
			polygons = filterConfiguration.getPolyList();
			neTypeList.add(NEType.valueOf(status));
			if (progressState.equalsIgnoreCase(InfraConstants.OFFAIR)) {

				if (kpiMap.get(ForesightConstants.IS_DECOMMISSIONED) != null) {
					boolean isDecommissioned = (boolean) kpiMap.get(ForesightConstants.IS_DECOMMISSIONED);
					if (isDecommissioned)
						neStatusList.add(NEStatus.valueOf(InfraConstants.DECOMMISSIONED));
				}
				if (kpiMap.get(InfraConstants.ISNONRADIATING) != null) {
					boolean isNonradiating = (boolean) kpiMap.get(InfraConstants.ISNONRADIATING);
					if (isNonradiating)
						neStatusList.add(NEStatus.valueOf(InfraConstants.NONRADIATING));
				}
			} else {
				neStatusList.add(NEStatus.valueOf(progressState));
			}

			kpiFinalMap = getKpiCountForPolygons(kpiFinalMap, polygons, frequencyList, neTypeList, neStatusList,
					progressState);
		} catch (Exception exception) {
			logger.error("Error in geting polygon count :  {}", Utils.getStackTrace(exception));
		}
		return kpiFinalMap;
	}

	private Map<String, Long> getKpiCountForPolygons(Map<String, Long> kpiFinalMap, List<List<List<Double>>> polygons,
			List<String> frequencyList, List<NEType> neTypeList, List<NEStatus> neStatusList, String progressState) {
		AtomicLong counter = new AtomicLong(0);
		Map<String, Double> viewportMap = new HashMap<>();
		List<NEBandDetail> listBand = new ArrayList<>();
		try {
			logger.info("No. of polygons are {}", polygons.size());
			for (List<List<Double>> polygon : polygons) {
				GISGeometry gispolygon = new GIS2DPolygon(polygon);
				Corner bounds = BoundaryUtils.getCornerOfBoundary(polygon);
				Double minlat = bounds.getMinLatitude();
				Double maxlat = bounds.getMaxLatitude();
				Double minlon = bounds.getMinLongitude();
				Double maxlon = bounds.getMaxLongitude();
				viewportMap.put(InfraConstants.SOUTHWEST_LATITUDE_KEY, minlat);
				viewportMap.put(InfraConstants.NORTHEAST_LATITUDE_KEY, maxlat);
				viewportMap.put(InfraConstants.SOUTHWEST_LONGITUDE_KEY, minlon);
				viewportMap.put(InfraConstants.NORTHEAST_LONGITUDE_KEY, maxlon);
				listBand = ineBandDetailDao.getNEBandDetails(neTypeList, null, frequencyList, neStatusList, null, null,
						null, viewportMap, null);
				logger.info("Data Recieved from NEBandDetail and size is {}", listBand.size());
				if (progressState.equalsIgnoreCase(InfraConstants.OFFAIR)) {
					Map<NEStatus, List<NEBandDetail>> statusMap = listBand.stream()
							.collect(Collectors.groupingBy(NEBandDetail::getBandStatus, Collectors.toList()));
					statusMap.forEach((status, statusList) -> {
						try {
							List<NetworkElement> networkElementList = statusList.stream()
									.map(n -> n.getNetworkElement()).collect(Collectors.toList());
							networkElementList.forEach(neBandDetail -> {
								try {
									if (gispolygon.contains(new GISPoint(
											new LatLng(neBandDetail.getLatitude(), neBandDetail.getLongitude())))) {
										counter.addAndGet(1);
									}
								} catch (Exception exception) {
									logger.error("Unable to get count of kpi for object {} due to exception {}",
											neBandDetail, Utils.getStackTrace(exception));
								}
							});

							if (kpiFinalMap.get(status.displayName()) != null) {
								Long count = kpiFinalMap.get(status.displayName()) + counter.get();
								kpiFinalMap.put(status.displayName(), count);
							} else {
								kpiFinalMap.put(status.displayName(), counter.get());
							}
							counter.getAndSet(0);
						} catch (Exception exception) {
							logger.error("Unable to get count  Exception {}", Utils.getStackTrace(exception));
						}
					});
				} else {
					Map<String, List<NEBandDetail>> frequencyMap = listBand.stream()
							.collect(Collectors.groupingBy(NEBandDetail::getNeFrequency, Collectors.toList()));
					frequencyMap.forEach((frequency, listNEband) -> {
						try {
							List<NetworkElement> networkElementList = listNEband.stream()
									.map(n -> n.getNetworkElement()).collect(Collectors.toList());
							networkElementList.forEach(neBandDetail -> {
								try {
									if (gispolygon.contains(new GISPoint(
											new LatLng(neBandDetail.getLatitude(), neBandDetail.getLongitude())))) {
										counter.addAndGet(1);
									}
								} catch (Exception exception) {
									logger.error("Unable to get count of kpi for object {} due to exception {}",
											neBandDetail, Utils.getStackTrace(exception));
								}
							});
							logger.info("kpiFinalMap : " + kpiFinalMap);
							if (kpiFinalMap.get(frequency) != null) {
								Long count = kpiFinalMap.get(frequency) + counter.get();
								kpiFinalMap.put(frequency, count);
							} else {
								kpiFinalMap.put(frequency, counter.get());
							}
							counter.getAndSet(0);
						} catch (Exception exception) {
							logger.error("Unable to get count for band {} and value {} Exception {}", frequency,
									listNEband, Utils.getStackTrace(exception));
						}
					});
				}
			}

		} catch (Exception exception) {
			logger.error("Unable to get kpimapcount of polygons due to exception {}", Utils.getStackTrace(exception));
		}
		return kpiFinalMap;
	}

	@Override
	@Transactional
	public Map<String, Long> getCustonmCountForKPI(KPISummaryDataWrapper filterConfiguration) {
		Map<String, List<String>> geographyNames = new HashMap<>();
		Map<String, Long> frequencyMap = new HashMap<>();
		Map<NEStatus, Long> statusMap = new HashMap<>();
		Map kpiMap = new HashMap<>();
		List<NEType> neTypeList = new ArrayList<>();
		List<NEStatus> neStatusList = new ArrayList<>();
		try {
			if (filterConfiguration.getKpiMap() != null) {
				kpiMap = filterConfiguration.getKpiMap();
			}
			String status = (String) kpiMap.get(ForesightConstants.SITE_TYPE);
			if (status.equalsIgnoreCase(InfraConstants.MACRO_CELL)) {
				status = InfraConstants.MACRO;
			}
			if (status.equalsIgnoreCase(InfraConstants.IBS_CELL)) {
				status = InfraConstants.IBS_SITE;
			}
			if (status.equalsIgnoreCase(InfraConstants.ODSC_CELL)) {
				status = InfraConstants.ODSC_SITE;
			}
			if (status.equalsIgnoreCase(InfraConstants.PICO_CELL)) {
				status = InfraConstants.PICO_SITE;
			}
			if (status.equalsIgnoreCase(InfraConstants.SHOOTER_CELL)) {
				status = InfraConstants.SHOOTER_SITE;
			}
			String neStatus = (String) kpiMap.get(InfraConstants.LAYER_PROGRESSSTATE);
			geographyNames.put((String) kpiMap.get(InfraConstants.GEOGRAPHY_TYPE),
					filterConfiguration.getGeographyList());
			neTypeList.add(NEType.valueOf(status));
			if (neStatus.equalsIgnoreCase(InfraConstants.OFFAIR)) {

				if (kpiMap.get(ForesightConstants.IS_DECOMMISSIONED) != null) {
					boolean isDecommissioned = (boolean) kpiMap.get(ForesightConstants.IS_DECOMMISSIONED);
					if (isDecommissioned)
						neStatusList.add(NEStatus.valueOf(InfraConstants.DECOMMISSIONED));
				}
				if (kpiMap.get(InfraConstants.ISNONRADIATING) != null) {
					boolean isNonradiating = (boolean) kpiMap.get(InfraConstants.ISNONRADIATING);
					if (isNonradiating)
						neStatusList.add(NEStatus.valueOf(InfraConstants.NONRADIATING));
				}
			} else {
				neStatusList.add(NEStatus.valueOf(neStatus));
			}

			List<NEBandDetail> neBandDetails = ineBandDetailDao.getNEBandDetails(neTypeList, null,
					(List<String>) kpiMap.get(InfraConstants.BAND), neStatusList, null, null, null, null,
					geographyNames);

			if (neBandDetails != null) {
				if (neStatus.equalsIgnoreCase(InfraConstants.OFFAIR)) {
					statusMap = neBandDetails.stream()
							.collect(Collectors.groupingBy(NEBandDetail::getBandStatus, Collectors.counting()));
					if (statusMap.get(NEStatus.valueOf(InfraConstants.NONRADIATING)) != null) {
						frequencyMap.put(InfraConstants.NONRADIATING,
								statusMap.get(NEStatus.valueOf(InfraConstants.NONRADIATING)));
					}
					if (statusMap.get(NEStatus.valueOf(InfraConstants.DECOMMISSIONED)) != null) {
						frequencyMap.put(InfraConstants.DECOMMISSIONED,
								statusMap.get(NEStatus.valueOf(InfraConstants.DECOMMISSIONED)));
					}
					frequencyMap.put(InfraConstants.TOTAL_SITE_COUNTS,
							frequencyMap.values().stream().mapToLong(Number::longValue).sum());
				} else {
					frequencyMap = neBandDetails.stream()
							.collect(Collectors.groupingBy(NEBandDetail::getNeFrequency, Collectors.counting()));
					frequencyMap.put(InfraConstants.TOTAL_SITE_COUNTS,
							frequencyMap.values().stream().mapToLong(Number::longValue).sum());
				}
			}
		} catch (Exception exception) {
			logger.error("Unable to get custonm count for KPI for  Exception {}", Utils.getStackTrace(exception));
		}
		return frequencyMap;
	}

	@Override
	public List<String> getDistinctVendor() {
		try {
			logger.info("Going to get distinct Vendor for Sites");
			List<String> vendors = new ArrayList<>();
			List<Vendor> vendorList = networkElementDao.getDistinctVendor();
			vendorList.forEach(v -> {
				vendors.add(v.name());
			});
			return vendors;
		} catch (Exception exception) {
			logger.error("Unable to get ditenct vondor of sites", exception.getMessage());

		}
		return null;
	}

	private List<Technology> getTechnologyList(Map map) {
		List<Technology> technologyList = new ArrayList<>();
		List<String> technology = new ArrayList<>();

		try {
			if (map != null && !map.isEmpty() && map.get(InfraConstants.NE_TECHNOLOGY_KEY) != null) {
				technology = (List<String>) map.get(InfraConstants.NE_TECHNOLOGY_KEY);
				for (String string : technology) {
					technologyList.add(Technology.valueOf(string));
				}
			}
			return technologyList;
		} catch (Exception exception) {
			logger.error("Unable to get technologies {}", Utils.getStackTrace(exception));
		}
		return technologyList;
	}

	private List<String> getDistinctGeography(String geographyLevel, Double southWestLong, Double southWestLat,
			Double northEastLong, Double northEastLat) {
		List<String> geographyList = new ArrayList<>();
		try {
			logger.info("Going top get distinct Geography for geographyLevel {} ", geographyLevel);
			geographyList = networkElementDao.getDistinctGeography(geographyLevel, southWestLong, southWestLat,
					northEastLong, northEastLat);
			logger.info("Distinct Geography in viewport is {}", geographyList);
		} catch (Exception exception) {
			logger.error("Unable to get geography For geogrphylevel {} due to exception {}", geographyLevel,
					Utils.getStackTrace(exception));
		}
		return geographyList;
	}

	@Override
	public List<String> getDistinctDomain() {
		List<String> domains = new ArrayList<>();
		try {
			logger.info("Going to get distinct Domains for Sites");
			List<Domain> domainList = networkElementDao.getDistinctDomain();
			domainList.forEach(d -> {
				domains.add(d.name());
			});
			
		} catch (Exception exception) {
			logger.error("Unable to get distinct Domains of sites", exception.getMessage());

		}
		return domains;
	}

	@Override
	public List<String> getDistinctVendorByDomain(List<String> domainList) {
	List<String> vendors = new ArrayList<>();
		try {
			logger.info("Going to get distinct Vendor By Domain.");
			
			List<Domain> domains = new ArrayList<>();
			for (String string : domainList) {
				domains.add(Domain.valueOf(string));
			}
			List<Vendor> vendorList = networkElementDao.getDistinctVendorByDomain(domains);
			vendorList.forEach(v -> {
				vendors.add(v.name());
			});
			logger.info("vendors  : {}", vendors);
		   } catch (Exception exception) {
			logger.error("Unable to get distinct vondor.Exception : {}", exception.getMessage());

		}
		return vendors;
	}

	@Override
	public List<String> getTechnologyByVendor(List<String> vendorList) {
		try {
			logger.info("Going to get distinct Vendor for Sites");
			List<String> technologies = new ArrayList<>();
			List<Vendor> vendors = new ArrayList<>();
			for (String vendor : vendorList) {
				vendors.add(Vendor.valueOf(vendor));
			}
			List<Technology> technologyList = networkElementDao.getTechnologyByVendor(vendors);
			technologyList.forEach(t -> {
				technologies.add(t.name());
			});
			logger.info("technologies  : {}", technologies);
			return technologies;
		} catch (Exception exception) {
			logger.error("Unable to get distinct technologies for sites", exception.getMessage());

		}
		return null;
	}

	@Override
	public List<String> getVendorsByType(List<String> neTypes) {
		try {
			logger.info("Going to get distinct Vendor for Sites {}", neTypes);
			List<String> vendors = new ArrayList<>();
			List<NEType> neTypeList = new ArrayList<>();
			for (String string : neTypes) {
				neTypeList.add(NEType.valueOf(string));
			}
			List<Vendor> vendorList = networkElementDao.getVendorsByType(neTypeList);
			vendorList.forEach(v -> {
				vendors.add(v.name());
			});
			logger.info("vendors  : {}", vendors);
			return vendors;
		} catch (Exception exception) {
			logger.error("Unable to get distinct vendor for sites. Exception", exception.getMessage());

		}
		return null;
	}

	@Override
	@Transactional
	public SiteGeographicalDetail getSiteOverviewData(String neName, String neType) {
		logger.info("Going to get SiteOverviewDetail data for neName {}", neName);
		SiteGeographicalDetail siteGeographicalDetail = new SiteGeographicalDetail();
		try {
			if (neType != null && neType.equalsIgnoreCase(InfraConstants.MACRO_CELL)) {
				neType = InfraConstants.MACRO;
			}
			if (neType != null && neType.equalsIgnoreCase(InfraConstants.IBS_CELL)) {
				neType = InfraConstants.IBS_SITE;
			}
			if (neType != null && neType.equalsIgnoreCase(InfraConstants.ODSC_CELL)) {
				neType = InfraConstants.ODSC_SITE;
			}
			if (neType != null && neType.equalsIgnoreCase(InfraConstants.PICO_CELL)) {
				neType = InfraConstants.PICO_SITE;
			}
			if (neType != null && neType.equalsIgnoreCase(InfraConstants.SHOOTER_CELL)) {
				neType = InfraConstants.SHOOTER_SITE;
			}
			if (neType != null && neType.equalsIgnoreCase(InfraConstants.GALLERY_CELL)) {
				neType = InfraConstants.GALLERY_SITE;
			}
			List<NEBandDetail> neBandDetailDataList = networkElementDao.getNetworkElementDataForNE(neName, neType);
			if (neBandDetailDataList != null && neBandDetailDataList.size() > 0)
				siteGeographicalDetail = getSiteOverviewDetailWrapper(neBandDetailDataList.get(0));
			else
				logger.warn("data is not available.");
		} catch (Exception exception) {
			logger.error("Error in getting SiteOverviewDetail data for neName {} Exception {}", neName,
					Utils.getStackTrace(exception));
		}
		return siteGeographicalDetail;
	}

	public NEType getCellSitetype(NEType neType) {
		logger.info("Going to get CellSitetype data for neType {}", neType);
		NEType sitetype = null;
		try {
			if (neType.toString().equalsIgnoreCase(NEType.MACRO.name())) {
				sitetype = NEType.MACRO_CELL;
			}
			if (neType.toString().equalsIgnoreCase(NEType.IBS_SITE.name())) {
				sitetype = NEType.IBS_CELL;
			}
			if (neType.toString().equalsIgnoreCase(NEType.ODSC_SITE.name())) {
				sitetype = NEType.ODSC_CELL;
			}
			if (neType.toString().equalsIgnoreCase(NEType.PICO_SITE.name())) {
				sitetype = NEType.PICO_CELL;
			}
			if (neType.toString().equalsIgnoreCase(NEType.SHOOTER_SITE.name())) {
				sitetype = NEType.SHOOTER_CELL;
			}

		} catch (Exception exception) {
			logger.error("Error in getting CellSitetype Forsight data for sitetype  {}", sitetype);
		}
		return sitetype;
	}

	@Override
	public List<NetworkElementWrapper> getSiteDataForPolygon(Map map, List<List<Double>> polygon) {
		logger.info("Going to get SiteDataForPolygon:{},{}", polygon.size());
		SiteLayerSelection layerSelection = new SiteLayerSelection();
		Map<String, Double> viewport = InfraUtils.getViewportFromPolygon(polygon);
		List<Tuple> sitesList = new ArrayList();

		logger.info("Viewport for Polygon:{}", viewport);
		if (viewport != null) {
			Map<String, List<Map>> filters = layerSelection.getFilters();
			Map<String, List<String>> projections = layerSelection.getProjection();
			List<Tuple> cellsList = iranDetailDao.getCellsDataForVisualisation(viewport, filters, projections, null);
			logger.info("Going to get CellsDataForVisualisation:{}", cellsList);

			GISGeometry gispolygon = new GIS2DPolygon(polygon);

			for (Iterator iterator = cellsList.iterator(); iterator.hasNext();) {
				Tuple networkElementWrapper = (Tuple) iterator.next();

				if (InfraUtils.isLatLngPresentInPolygon(gispolygon, Double.valueOf(String.valueOf(networkElementWrapper.get("latitude"))),
						Double.valueOf(String.valueOf(networkElementWrapper.get("longitude"))))) {
					sitesList.add(networkElementWrapper);
				}
			}

		} else {
			logger.warn("getSiteDataForPolygon() Viewport is null");
		}
		return new ArrayList<>();
	}
	
	
	@Override
	public List<Tuple> getSiteDataInPolygon(SiteLayerSelection layerSelection, List<List<Double>> polygon) {
		logger.info("Going to get getSiteDataInPolygon:{},{}", polygon.size());
		Map<String, Double> viewport = InfraUtils.getViewportFromPolygon(polygon);
		List<Tuple> sitesList = new ArrayList();

		logger.info("Viewport for Polygon:{}", viewport);
		if (viewport != null) {
			Map<String, List<Map>> filters = layerSelection.getFilters();
			Map<String, List<String>> projections = layerSelection.getProjection();
			List<Tuple> cellsList = iranDetailDao.getCellsDataForVisualisation(viewport, filters, projections, null);
			logger.info("Going to get CellsDataForVisualisation:{}", cellsList);

			GISGeometry gispolygon = new GIS2DPolygon(polygon);

			for (Iterator iterator = cellsList.iterator(); iterator.hasNext();) {
				Tuple networkElementWrapper = (Tuple) iterator.next();

				if (InfraUtils.isLatLngPresentInPolygon(gispolygon, Double.valueOf(String.valueOf(networkElementWrapper.get("latitude"))),
						Double.valueOf(String.valueOf(networkElementWrapper.get("longitude"))))) {
					sitesList.add(networkElementWrapper);
				}
			}

		} else {
			logger.warn("getSiteDataInPolygon() Viewport is null");
		}
		return sitesList;
	}
	
	@Override
	public List<NETaskDetailWrapper> getNETaskDetailData(Map<String, List<Map>> filters, Map<String, List<String>> projection, Boolean isGroupBy) {
		logger.info("Going to get NE task detail Data.");
		List<NETaskDetailWrapper> neTaskDetailWrapperList=new ArrayList<>();
		String neName=null;
		String neType=null;
		try {
			if(filters != null) {
				if(filters.get(InfraConstants.NETWORKELEMENT_TABLE) != null) {
					List<Map> mapList=filters.get(InfraConstants.NETWORKELEMENT_TABLE);
					List<String> neNameList=new ArrayList<>();
					List<String> neTypeList=new ArrayList<>();
					for (Map map : mapList) {
						if(map.get(InfraConstants.NE_LABELTYPE_KEY) != null) {
							if(map.get(InfraConstants.NE_LABELTYPE_KEY).toString().equalsIgnoreCase(InfraConstants.NE_NENAME_KEY)) {
								if(map.get(InfraConstants.NE_VALUE_KEY) != null)
								neNameList=(List<String>)map.get(InfraConstants.NE_VALUE_KEY);
							}
							if(map.get(InfraConstants.NE_LABELTYPE_KEY).toString().equalsIgnoreCase(InfraConstants.NE_NETYPE_KEY)) {
								if(map.get(InfraConstants.NE_VALUE_KEY) != null)
								neTypeList=(List<String>)map.get(InfraConstants.NE_VALUE_KEY);
							}
						}
					}
					if(neNameList != null && !neNameList.isEmpty() && neTypeList != null && !neTypeList.isEmpty()) {
					neName=neNameList.get(0);
					neType=neTypeList.get(0);
				}
				}
			}
			if(neName != null && neType != null)
		neTaskDetailWrapperList=networkElementDao.getNETaskDetailData(neName,neType);
		}catch(Exception exception) {
			logger.warn("Unable to get NE Task Detail data. Exception: {}",exception.getMessage());
		}
		return neTaskDetailWrapperList;
	}
	
	private SectorSummaryWrapper getOperationalStatusForSector(SiteSummaryOverviewWrapper siteSummaryOverviewWrapper,
			SiteSummaryOverviewWrapper siteSummaryOverviewWrapperSecond) {
		SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
		try {
			if (siteSummaryOverviewWrapper.getAlphaOperationalStatus() != null)
				sectorSummaryWrapper.setAlpha(siteSummaryOverviewWrapper.getAlphaOperationalStatus());
			if (siteSummaryOverviewWrapper.getBetaOperationalStatus() != null)
				sectorSummaryWrapper.setBeta(siteSummaryOverviewWrapper.getBetaOperationalStatus());
			if (siteSummaryOverviewWrapper.getGammaOperationalStatus() != null)
				sectorSummaryWrapper.setGamma(siteSummaryOverviewWrapper.getGammaOperationalStatus());
			if (siteSummaryOverviewWrapper.getAlphaOperationalStatusAdd() != null)
				sectorSummaryWrapper.setAddAlpha(String.valueOf(siteSummaryOverviewWrapper.getAlphaOperationalStatusAdd()));
			if (siteSummaryOverviewWrapper.getBetaOperationalStatusAdd() != null)
				sectorSummaryWrapper.setAddBeta(String.valueOf(siteSummaryOverviewWrapper.getBetaOperationalStatusAdd()));
			if (siteSummaryOverviewWrapper.getGammaOperationalStatusAdd() != null)
				sectorSummaryWrapper.setAddGamma(String.valueOf(siteSummaryOverviewWrapper.getGammaOperationalStatusAdd()));

			if (siteSummaryOverviewWrapperSecond.getAlphaOperationalStatusSecond() != null)
				sectorSummaryWrapper.setAlphaSecond(siteSummaryOverviewWrapperSecond.getAlphaOperationalStatusSecond());
			if (siteSummaryOverviewWrapperSecond.getBetaOperationalStatusSecond() != null)
				sectorSummaryWrapper.setBetaSecond(siteSummaryOverviewWrapperSecond.getBetaOperationalStatusSecond());
			if (siteSummaryOverviewWrapperSecond.getGammaOperationalStatusSecond() != null)
				sectorSummaryWrapper.setGammaSecond(siteSummaryOverviewWrapperSecond.getGammaOperationalStatusSecond());
			if (siteSummaryOverviewWrapperSecond.getAlphaOperationalStatusSecond() != null)
				sectorSummaryWrapper.setAddAlphaSecond(
						String.valueOf(siteSummaryOverviewWrapperSecond.getAlphaOperationalStatusAddSecond()));
			if (siteSummaryOverviewWrapperSecond.getBetaOperationalStatusAddSecond() != null)
				sectorSummaryWrapper
						.setAddBetaSecond(String.valueOf(siteSummaryOverviewWrapperSecond.getBetaOperationalStatusAddSecond()));
			if (siteSummaryOverviewWrapperSecond.getGammaOperationalStatusAddSecond() != null)
				sectorSummaryWrapper.setAddGammaSecond(
						String.valueOf(siteSummaryOverviewWrapperSecond.getGammaOperationalStatusAddSecond()));
		} catch (Exception exception) {
			logger.error("Unable to get the operationalstatus sector data", Utils.getStackTrace(exception));
		}
		return sectorSummaryWrapper;
	}
	
	private SectorSummaryWrapper getAdminStateForSector(SiteSummaryOverviewWrapper siteSummaryOverviewWrapper,
			SiteSummaryOverviewWrapper siteSummaryOverviewWrapperSecond) {
		SectorSummaryWrapper sectorSummaryWrapper = new SectorSummaryWrapper();
		try {
			if (siteSummaryOverviewWrapper.getAlphaAdminState() != null)
				sectorSummaryWrapper.setAlpha(siteSummaryOverviewWrapper.getAlphaAdminState());
			if (siteSummaryOverviewWrapper.getBetaAdminState() != null)
				sectorSummaryWrapper.setBeta(siteSummaryOverviewWrapper.getBetaAdminState());
			if (siteSummaryOverviewWrapper.getGammaAdminState() != null)
				sectorSummaryWrapper.setGamma(siteSummaryOverviewWrapper.getGammaAdminState());
			if (siteSummaryOverviewWrapper.getAlphaAdminStateAdd() != null)
				sectorSummaryWrapper.setAddAlpha(String.valueOf(siteSummaryOverviewWrapper.getAlphaAdminStateAdd()));
			if (siteSummaryOverviewWrapper.getBetaAdminStateAdd() != null)
				sectorSummaryWrapper.setAddBeta(String.valueOf(siteSummaryOverviewWrapper.getBetaAdminStateAdd()));
			if (siteSummaryOverviewWrapper.getGammaAdminStateAdd() != null)
				sectorSummaryWrapper.setAddGamma(String.valueOf(siteSummaryOverviewWrapper.getGammaAdminStateAdd()));

			if (siteSummaryOverviewWrapperSecond.getAlphaAdminStateSecond() != null)
				sectorSummaryWrapper.setAlphaSecond(siteSummaryOverviewWrapperSecond.getAlphaAdminStateSecond());
			if (siteSummaryOverviewWrapperSecond.getBetaAdminStateSecond() != null)
				sectorSummaryWrapper.setBetaSecond(siteSummaryOverviewWrapperSecond.getBetaAdminStateSecond());
			if (siteSummaryOverviewWrapperSecond.getGammaAdminStateSecond() != null)
				sectorSummaryWrapper.setGammaSecond(siteSummaryOverviewWrapperSecond.getGammaAdminStateSecond());
			if (siteSummaryOverviewWrapperSecond.getAlphaAdminStateSecond() != null)
				sectorSummaryWrapper.setAddAlphaSecond(
						String.valueOf(siteSummaryOverviewWrapperSecond.getAlphaAdminStateAddSecond()));
			if (siteSummaryOverviewWrapperSecond.getBetaAdminStateAddSecond() != null)
				sectorSummaryWrapper
						.setAddBetaSecond(String.valueOf(siteSummaryOverviewWrapperSecond.getBetaAdminStateAddSecond()));
			if (siteSummaryOverviewWrapperSecond.getGammaAdminStateAddSecond() != null)
				sectorSummaryWrapper.setAddGammaSecond(
						String.valueOf(siteSummaryOverviewWrapperSecond.getGammaAdminStateAddSecond()));
		} catch (Exception exception) {
			logger.error("Unable to get the adminstate sector data", Utils.getStackTrace(exception));
		}
		return sectorSummaryWrapper;
	}
	
	@Override
	public SiteConnectionPointWrapper getSiteConnectionPointDetails(String neName,String neType) {
		logger.info("Going to get SiteConnectionPoint Details data for neName {} :, neType : {}",neName,neType);
		try{
			if(customNEVisualizationService!=null) {
				return customNEVisualizationService.getSiteConnectionPointDetails(neName, neType);
			}else{
				System.out.println("customNEVisualizationService : "+customNEVisualizationService);
				throw new RestException(ForesightConstants.INVALID_PARAMETERS);
			}
		}catch(Exception exception){
			logger.error("Unable to get SiteConnectionPoin Details data for neName {} :, neType : {}. Exception : {} " ,neName,neType,Utils.getStackTrace(exception));
			throw new RestException(exception.getMessage());
		}
	}
	@Override
	public Map<String,String> getSectorMap(String neName,String neType) {
		Map<String, String> map = new HashMap<>();
		try {
			if (neName != null && neType != null) {
				
				List<NetworkElementWrapper> networkElementWrapperList=iranDetailDao.getMacroSiteDetailByNameAndType(neName,neType);
				if(networkElementWrapperList != null) {
					for (NetworkElementWrapper networkElementWrapper : networkElementWrapperList) {
						if(networkElementWrapper.getNeFrequency() != null) {
							if(networkElementWrapper.getSectorId() != null && (networkElementWrapper.getSectorId() == 1 || networkElementWrapper.getSectorId() == 2 || networkElementWrapper.getSectorId() == 3)) {
								if(networkElementWrapper.getOnAirDate() != null)
									map.put(InfraConstants.ONAIRDATE+ForesightConstants.UNDERSCORE+networkElementWrapper.getNeFrequency(), InfraUtils.getSiteTaskDateForSectorProperty(networkElementWrapper.getOnAirDate(),true));
								if(networkElementWrapper.getBandwidth() != null)
									map.put(InfraConstants.BANDWIDTH+ForesightConstants.UNDERSCORE+networkElementWrapper.getNeFrequency(), getFormattedBandWidthForSites(networkElementWrapper.getBandwidth()));
								if(networkElementWrapper.getNeStatus() != null)
									map.put(InfraConstants.PROGRESSSTATE_CAP+ForesightConstants.UNDERSCORE+networkElementWrapper.getNeFrequency(), networkElementWrapper.getNeStatus().toString());

							}
							if(networkElementWrapper.getSectorId() != null && (networkElementWrapper.getSectorId() == 4 || networkElementWrapper.getSectorId() == 5 || networkElementWrapper.getSectorId() == 6)) {
								if(networkElementWrapper.getOnAirDateFourthSector() != null)
									map.put(InfraConstants.FOURTHSECTOR + ForesightConstants.UNDERSCORE+InfraConstants.ONAIRDATE+ForesightConstants.UNDERSCORE+networkElementWrapper.getNeFrequency(), InfraUtils.getSiteTaskDateForSectorProperty(networkElementWrapper.getOnAirDateFourthSector(),true));
								if(networkElementWrapper.getBandwidthFourthSector() != null)
									map.put(InfraConstants.FOURTHSECTOR +ForesightConstants.UNDERSCORE+ InfraConstants.BANDWIDTH+ForesightConstants.UNDERSCORE+networkElementWrapper.getNeFrequency(), getFormattedBandWidthForSites(networkElementWrapper.getBandwidthFourthSector()));
								if(networkElementWrapper.getNeStatusFourthSector() != null)
									map.put(InfraConstants.FOURTHSECTOR + ForesightConstants.UNDERSCORE + InfraConstants.PROGRESSSTATE_CAP+ForesightConstants.UNDERSCORE+networkElementWrapper.getNeFrequency(), networkElementWrapper.getNeStatusFourthSector().toString());

							}

						}
					}
				}
			}
			return map;
		} catch (Exception exception) {
			logger.error("Error in adding SiteOverviewDetail parameters in wrapper Message {}", exception.getMessage());
		}
		return map;
	}
}

package com.inn.foresight.core.infra.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.util.concurrent.AtomicDouble;
import com.inn.commons.maps.Corner;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.geometry.BoundaryUtils;
import com.inn.commons.maps.geometry.gis.GIS2DPolygon;
import com.inn.commons.maps.geometry.gis.GISGeometry;
import com.inn.commons.maps.geometry.gis.GISPoint;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.utils.ApplicationContextProvider;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.INEBandDetailDao;
import com.inn.foresight.core.infra.dao.INetworkElementDao;
import com.inn.foresight.core.infra.dao.IRANDetailDao;
import com.inn.foresight.core.infra.service.INEVisualizationService;
import com.inn.foresight.core.infra.service.ISiteVisualizationService;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.InfraUtils;
import com.inn.foresight.core.infra.wrapper.SiteLayerSelection;

@Service("SiteVisualisationServiceImpl")
public class SiteVisualisationServiceImpl implements ISiteVisualizationService {

	private Logger logger = LogManager.getLogger(SiteVisualisationServiceImpl.class);

	/** The dao. */
	@Autowired
	private INEBandDetailDao ineBandDetailDao;

	@Autowired
	private INetworkElementDao networkElementDao;

	@Autowired
	private IRANDetailDao iranDetailDao;

	/*
	 * @author Vaibhav Get site Details for Different NE Parameters
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, List<Map>> searchSite(List<SiteLayerSelection> siteLayerSelection, Double southWestLong, Double southWestLat,
			Double northEastLong, Double northEastLat, Integer zoomLevel)throws DaoException,RestException {
		try {
			logger.info("Going to search sites data for viewPort southWestLong : {} ,southWestLat : {} northEastLong :" + " {} northEastLat : {} zoomLevel : {}", southWestLong, southWestLat,
					northEastLong, northEastLat, zoomLevel);
			Map<String, List<Map>> siteDetail = new HashMap();
			Optional<Map> viewPortMap = Optional.ofNullable(InfraUtils.validateViewPort(southWestLong, southWestLat, northEastLong, northEastLat));
			if (viewPortMap.isPresent() && !viewPortMap.get().isEmpty() && siteLayerSelection != null) {
				List<Map> layerPanelCount = new ArrayList<>();
				List<Map> geographyCount = new ArrayList<>();
				List<Map> actualSites = new ArrayList<>();
				Map<String, List<Tuple>> siteDataList = new HashMap();
				siteDataList = getSitesForLayerSelections(viewPortMap.get(), zoomLevel, siteLayerSelection);
				logger.info("Size Of Map after getting NE data is : {}", siteDataList.size());
				if (!siteDataList.isEmpty()) {
					layerPanelCount.addAll(getAggregateBandCount(siteDataList.get(InfraConstants.SITES_COUNT_KEY)));
					geographyCount = getFilteredCountGeographyWise(siteDataList.get(InfraConstants.SITES_COUNT_KEY), 
							InfraUtils.getGeographyLevelForAggregation(zoomLevel));
					layerPanelCount.addAll(getAggregateGeographyCount(siteDataList.get(InfraConstants.SITES_DETAIL_KEY)));
					if (siteDataList.get(InfraConstants.SITES_DETAIL_KEY) != null && !siteDataList.get(InfraConstants.SITES_DETAIL_KEY).isEmpty()) {
						actualSites = InfraUtils.getMapFromTupleList(siteDataList.get(InfraConstants.SITES_DETAIL_KEY));
					}
				}
				logger.info("Size Of Site Visualisation List {},Geography List {} ,Panel Count List {} ", actualSites.size(), geographyCount.size(), layerPanelCount.size());
				siteDetail = getMapForSiteLayer(geographyCount, layerPanelCount, actualSites);
			} else {
				throw new RestException("Incorrect Viewport ");
			}
			logger.info(" size of siteDetail {}", siteDetail.size());

			return siteDetail;

		} catch (Exception exception) {
			logger.error("Unable to get Sites data for visualisation {} ", Utils.getStackTrace(exception));
			throw new RestException(exception.getMessage());

		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map getMapForSiteLayer(List<Map> siteListGeographyWise, List<Map> panelCount, List<Map> actualSites) {
		Map map = new HashMap<>();
		try {
			if (siteListGeographyWise != null && !siteListGeographyWise.isEmpty()) {
				map.put(InfraConstants.AGGREGATED_LAYER_COUNT_KEY, siteListGeographyWise);
			}
			if (panelCount != null && !panelCount.isEmpty()) {
				map.put(InfraConstants.SITES_COUNT_IN_NETWORK, panelCount);
			}
			if (actualSites != null && !actualSites.isEmpty()) {
				map.put(InfraConstants.SITE_VISUALISATION_KEY, actualSites);
			}
		} catch (Exception exception) {
			logger.error("Unable to get map for Site Layer {}", Utils.getStackTrace(exception));
		}
		return map;
	}

	private List<Map> getAggregateBandWiseCount(Map<Object, Map<Object, List<Tuple>>> aggregateGeographyCount,
			List<Map> countStatusWiseList) {
		try {
			aggregateGeographyCount.forEach((netype, freqMap) -> {
				try {
					freqMap.forEach((band, wrapperList) -> {

						try {
							logger.info("Inside getAggregateBandWiseCount {} ", wrapperList.size());
							Long sitesCount = wrapperList.stream()
									.filter(distinctByKey(p -> String.valueOf(p.get(InfraConstants.NE_NENAME_KEY))
											+ String.valueOf(p.get(InfraConstants.NE_FREQUENCY))))
									.count();
							logger.info("sitesCount : {}", sitesCount);
							String s = "";
							String p = String.valueOf(netype) + String.valueOf(band);

							if (!s.equalsIgnoreCase(p)) {
								s = String.valueOf(netype) + String.valueOf(band);
								Map map = (Map) InfraUtils.getMapFromTupleList(wrapperList).get(0);
								map.put(InfraConstants.TOTAL_COUNT_KEY, sitesCount);
								countStatusWiseList.add(map);
							}
						} catch (Exception exception) {
							logger.error("Unable to Aggregate sites  data for key {}, value {} due to exception {}",
									band, wrapperList, Utils.getStackTrace(exception));
						}

					});
				} catch (Exception exception) {
					logger.error("Invalid key value for map : {}", Utils.getStackTrace(exception));
				}

			});
		} catch (Exception exception) {
			logger.error(" Invalid key value for map {}", Utils.getStackTrace(exception));
		}
		return countStatusWiseList;
	}

	private List<Map> getPanelCountForKPI(Map<Object, Map<Object, List<Tuple>>> aggregateGeographyCount,
			List<Map> countStatusWiseList) {
		try {

			aggregateGeographyCount.forEach((netype, statusMap) -> {
				try {
					statusMap.forEach((status, wrapperList) -> {
						try {

							Long sitesCount = wrapperList.stream()
									.filter(distinctByKey(p -> String.valueOf(p.get(InfraConstants.NE_NENAME_KEY))
											+ String.valueOf(p.get(InfraConstants.NE_NEFREQUENCY_KEY))))
									.count();
							String s = InfraConstants.BLANK_STRING;
							String p = String.valueOf(netype) + String.valueOf(status);

							if (!s.equalsIgnoreCase(p)) {
								s = String.valueOf(netype) + String.valueOf(status);
								Map map = (Map) InfraUtils.getMapFromTupleList(wrapperList).get(0);
								map.put(InfraConstants.TOTAL_COUNT_KEY, sitesCount);
								countStatusWiseList.add(map);
							}
						} catch (Exception exception) {
							logger.error("Unable to Aggregate sites data for key {}, value {} due to exception {}",
									status, wrapperList, exception.getMessage());
						}

					});
				} catch (Exception exception) {
					logger.error("Invalid key  value for map {}", exception.getMessage());
				}

			});
		} catch (Exception exception) {
			logger.error("Invalid key value  for map {}", exception.getMessage());
		}

		return countStatusWiseList;
	}

	private List<Map> getPanelCount(Map<Object, Map<Object, List<Tuple>>> aggregateGeographyCount,
			List<Map> countStatusWiseList) {
		try {

			aggregateGeographyCount.forEach((netype, statusMap) -> {
				try {
					statusMap.forEach((status, wrapperList) -> {
						try {

							Long sitesCount = wrapperList.stream()
									.filter(distinctByKey(p -> String.valueOf(p.get(InfraConstants.PARENT_NENAME_KEY))
											+ String.valueOf(p.get(InfraConstants.NE_NEFREQUENCY_KEY))))
									.count();
							String s = "";
							String p = String.valueOf(netype) + String.valueOf(status);

							if (!s.equalsIgnoreCase(p)) {
								s = String.valueOf(netype) + String.valueOf(status);
								Map map = (Map) InfraUtils.getMapFromTupleList(wrapperList).get(0);
								map.put(InfraConstants.TOTAL_COUNT_KEY, sitesCount);
								countStatusWiseList.add(map);
							}
						} catch (Exception exception) {
							logger.error("Unable to Aggregate sites data for key {}, value {} due to exception {}",
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

	private List<Map> getAggregateBandCount(List<Tuple> combinedList) {
		List<Map> countStatusWiseList = new ArrayList<>();
		try {
			if (combinedList != null && !combinedList.isEmpty()) {
				Map<Object, Map<Object, List<Tuple>>> layerCountMap = combinedList.stream()
						.collect(Collectors.groupingBy(s -> s.get(InfraConstants.NE_TYPE), Collectors
								.groupingBy(s -> s.get(InfraConstants.NE_BANDSTATUS_KEY), Collectors.toList())));
				getAggregateBandWiseCount(layerCountMap, countStatusWiseList);
			}
		} catch (Exception exception) {
			logger.error("Unable to get the aggregated Count for layer {}", Utils.getStackTrace(exception));
		}
		return countStatusWiseList;
	}

	private List<Map> getAggregateGeographyCount(List<Tuple> combinedList) {
		List<Map> countStatusWiseList = new ArrayList<>();
		try {
			if (combinedList != null && !combinedList.isEmpty()) {
				logger.info("combinedList size " + combinedList.size());
				Map<Object, Map<Object, List<Tuple>>> layerCountMap = combinedList.stream()
						.collect(Collectors.groupingBy(s -> s.get(InfraConstants.NE_TYPE), Collectors
								.groupingBy(s -> s.get(InfraConstants.NE_BANDSTATUS_KEY), Collectors.toList())));
				logger.info("layerCountMap size " + layerCountMap.size());

			  getPanelCount(layerCountMap, countStatusWiseList);
			}
		} catch (Exception exception) {
			logger.error("Unable to get the aggregated Count for layer {}", Utils.getStackTrace(exception));
		}
		logger.info("countStatusWiseList size {}", countStatusWiseList.size());
		return countStatusWiseList;
	}

	private List<Map> getFilteredCountGeographyWise(List<Tuple> filteredList, String geographyLevel) {
		List<Map> sitesListGeographyWise = new ArrayList<>();
		try {
			if (filteredList != null && !filteredList.isEmpty()) {
				Map<Object, List<Tuple>> map = filteredList.stream().collect(Collectors.groupingBy(
						s -> s.get(geographyLevel.substring(9, 11) + InfraConstants.NAME), Collectors.toList()));
				map.forEach((k, v) -> {
					try {
						if (v != null && !v.isEmpty()) {
							Long sitesCount = v.stream()
									.filter(distinctByKey(p -> String.valueOf(p.get(InfraConstants.NE_NENAME_KEY))
											+ String.valueOf(p.get(InfraConstants.NE_NEFREQUENCY_KEY))))
									.count();

							logger.info("geography wise count" + sitesCount);
							Map geographyMap = (Map) InfraUtils.getMapFromTupleList(v).get(0);
							geographyMap.put(InfraConstants.TOTAL_COUNT_KEY, sitesCount);
							sitesListGeographyWise.add(geographyMap);
						}
					} catch (Exception exception) {
						logger.error("Error occur while getting sitesListGeographyWise {}",
								Utils.getStackTrace(exception));
					}
				});
			}
		} catch (Exception exception) {
			logger.error("Unable to Filter Count Zone wise from list {}", Utils.getStackTrace(exception));
		}
		logger.info("sitesListGeographyWise count" + sitesListGeographyWise.size());
		return sitesListGeographyWise;
	}

	@SuppressWarnings("rawtypes")
	private Map<String, List<Tuple>> getSitesForLayerSelections(Map<String, Double> viewPortMap, Integer zoomLevel,
			List<SiteLayerSelection> siteLayerselectionList) throws DaoException {
		Map<String, List<Tuple>> visualize = new HashMap<>();
		logger.info("Going to get sites data according to layer selections for zoomlevel {}", zoomLevel);
		try {
			List<Tuple> siteDataCount = new ArrayList<>();
			List<Tuple> siteDataVisualize = new ArrayList<>();
			siteLayerselectionList.forEach(siteLayerSelection -> {
				try {
					if(siteLayerSelection!=null) {
					List<String> distinctEntity = new ArrayList<>();
					String nestatus = null;
					if (siteLayerSelection.getNestatus() != null)
						nestatus = siteLayerSelection.getNestatus();
					Map<String, List<Map>> filters = siteLayerSelection.getFilters();
					Map<String, List<Map>> plannedfilters = new HashMap<>(filters);
					Map<String, List<String>> projections = siteLayerSelection.getProjection();
					Map<String, List<String>> plannedprojections = new HashMap<>(projections);
					Integer displaySite = siteLayerSelection.getDisplaySite();
					if (zoomLevel < displaySite) {
						String geographyLevel = InfraUtils.getGeographyLevelForAggregation(zoomLevel);
						List<String> geographyList = getDistinctGeography(geographyLevel,
								viewPortMap.get(InfraConstants.SW_LONGITUDE),
								viewPortMap.get(InfraConstants.SW_LATITUDE), viewPortMap.get(InfraConstants.NW_LONG),
								viewPortMap.get(InfraConstants.NW_LAT));
						distinctEntity.add(geographyLevel);
						if (geographyList != null && !geographyList.isEmpty()) {
							logger.info("Total {} geographies retrieved for geographyLevel : {} ", geographyList.size(),
									geographyLevel);
							siteDataCount.addAll(ineBandDetailDao.getSitesDataForVisualisation(viewPortMap, filters,
									projections, distinctEntity, true, geographyList));
						}
					} else {
						logger.info("Going to get actual sites count from tables {} and projection {},nestatus {} ",
								distinctEntity, projections, nestatus);
						distinctEntity = distinctEntity.stream().distinct().collect(Collectors.toList());
						if (nestatus != null && (nestatus.equals(InfraConstants.PLANNED)
								|| nestatus.equals(InfraConstants.NOMINAL))) {
							siteDataVisualize.addAll(iranDetailDao.getCellsDataForVisualisation(viewPortMap, filters,
									projections, distinctEntity));
							siteDataVisualize.addAll(ineBandDetailDao.getSitesPlannedDataForVisualisation(viewPortMap,
									plannedfilters, plannedprojections, distinctEntity, true, null));
						} else {
							siteDataVisualize.addAll(iranDetailDao.getCellsDataForVisualisation(viewPortMap, filters,
									projections, distinctEntity));
						}
					}
					
				} else
				{
				throw new RestException(InfraConstants.INVALID_PARAMETERS);	
				}
				}
				catch (Exception exception) {
					logger.error("Unable to get data for sites due to {}", Utils.getStackTrace(exception));
				}
				
			});
			visualize.put(InfraConstants.SITES_COUNT_KEY, siteDataCount);
			visualize.put(InfraConstants.SITES_DETAIL_KEY, siteDataVisualize);
			logger.info("size of siteDataVisualize " + siteDataVisualize.size());
		} catch (Exception exception) {
			logger.error("Unable to get Sites data due to error {} ", Utils.getStackTrace(exception));
		}
		return visualize;
	}

	public List<String> getDistinctGeography(String geographyLevel, Double southWestLong, Double southWestLat,
			Double northEastLong, Double northEastLat) {
		List<String> geographyList = new ArrayList<>();
		try {
			logger.info(
					"Going to get distinct geographies for geographyLevel {} within viewport southWestLong : {},southWestLat : {},northEastLong : {},northEastLat : {} ",
					geographyLevel, southWestLong, southWestLat, northEastLong, northEastLat);
			geographyList = networkElementDao.getDistinctGeography(geographyLevel, southWestLong, southWestLat,
					northEastLong, northEastLat);
		} catch (Exception exception) {
			logger.error("Error in getting distinct geographies for geogrphylevel {}. Message : {}", geographyLevel,
					exception.getMessage());
		}
		return geographyList;
	}

	private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		try {
			Map<Object, Boolean> seen = new ConcurrentHashMap<>();
			return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
		} catch (Exception exception) {
			logger.error("error in finding distinctByKey  Exception {} ", Utils.getStackTrace(exception));
		}
		return null;
	}

	@Override
	public List<Map> getSitesDetailForTableView(List<SiteLayerSelection> siteLayerSelection, Double southWestLong,
			Double southWestLat, Double northEastLong, Double northEastLat, Integer zoomLevel) {
		Map<String, List<Map>> sitesVisualizationMap = new HashMap();
		List<Map> aggregateCountList = new ArrayList();
		try {
			sitesVisualizationMap = searchSite(siteLayerSelection, southWestLong, southWestLat, northEastLong,
					northEastLat, zoomLevel);
			if (sitesVisualizationMap != null && !sitesVisualizationMap.isEmpty()){
					if( sitesVisualizationMap.get(InfraConstants.AGGREGATED_LAYER_COUNT_KEY) != null
					&& !sitesVisualizationMap.get(InfraConstants.AGGREGATED_LAYER_COUNT_KEY).isEmpty()) {
				aggregateCountList = sitesVisualizationMap.get(InfraConstants.AGGREGATED_LAYER_COUNT_KEY);
			} else {
				aggregateCountList = sitesVisualizationMap.get(InfraConstants.SITE_VISUALISATION_KEY);
			}
			}
		} catch (Exception exception) {
			logger.error("Error in getting aggregated count for geographylevel and netype {} ",
					Utils.getStackTrace(exception));
		}
		return aggregateCountList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map getKPISummaryCount(List<SiteLayerSelection> siteLayerSelection, Double southWestLong,
			Double southWestLat, Double northEastLong, Double northEastLat, Integer zoomLevel) {
		Map<String, List<Tuple>> siteDataList = new HashMap<>();
		Map<String, Long> kpiCountMap = new HashMap();
		try {
			Optional<Map> viewPortMap = Optional
					.ofNullable(InfraUtils.validateViewPort(southWestLong, southWestLat, northEastLong, northEastLat));
			if (viewPortMap.isPresent() && !viewPortMap.get().isEmpty() && siteLayerSelection != null
					&& !siteLayerSelection.isEmpty()) {

				siteDataList = getSitesForLayerSelections(viewPortMap.get(), zoomLevel, siteLayerSelection);
				if (!siteDataList.isEmpty()
						&& siteDataList.get(InfraConstants.SITES_COUNT_KEY) != null
						&& !siteDataList.get(InfraConstants.SITES_COUNT_KEY).isEmpty()) {
					getSitesCountForKPI(kpiCountMap, getAggregatedSitesMap(
							siteDataList.get(InfraConstants.SITES_COUNT_KEY), InfraConstants.NE_NENAME_KEY));
				} else {
					if (siteDataList.get(InfraConstants.SITES_DETAIL_KEY) != null
							&& !siteDataList.get(InfraConstants.SITES_DETAIL_KEY).isEmpty())
						getSitesCountForKPI(kpiCountMap, getAggregatedSitesMap(
								siteDataList.get(InfraConstants.SITES_DETAIL_KEY), InfraConstants.PARENT_NENAME_KEY));
				}

			}

		} catch (Exception exception) {
			logger.error("Error in getting aggregated count for zoomlevel and netype {} ",
					Utils.getStackTrace(exception));
		}
		return kpiCountMap;
	}

	public Map<String, Long> getKPICountGeographyWise(List<SiteLayerSelection> siteLayerSelection, Double southWestLong,
			Double southWestLat, Double northEastLong, Double northEastLat, Integer zoomLevel) {
		Map<String, List<Map>> sitesVisualizationMap = new HashMap<>();
		try {
			Map<String, Long> kpiCountMap = new HashMap();
			siteLayerSelection.forEach(layerSelection -> {

				Optional<Map> viewMap = Optional.ofNullable(
						InfraUtils.validateViewPort(southWestLong, southWestLat, northEastLong, northEastLat));
				if (viewMap.isPresent() && !viewMap.get().isEmpty() && siteLayerSelection != null) {
					List distinctEntity = new ArrayList<>(layerSelection.getProjection().keySet());
					distinctEntity.addAll(new ArrayList<>(layerSelection.getFilters().keySet()));
					List<Tuple> aggregateCountList = ineBandDetailDao.getSitesDataForVisualisation(viewMap.get(),
							layerSelection.getFilters(), layerSelection.getProjection(), distinctEntity, true,
							layerSelection.getGeography());

					getSitesCountForKPI(kpiCountMap,
							getAggregatedSitesMap(aggregateCountList, InfraConstants.NE_NENAME_KEY));
				}
			});
			logger.info("kpiCountMap {}", kpiCountMap);
			return kpiCountMap;
		} catch (Exception exception) {
			logger.error("Error in getting aggregated count for zoomlevel and netype {} ",
					Utils.getStackTrace(exception));
		}
		return new HashMap();
	}

	public Map getKPICoutForPolygons(List<SiteLayerSelection> siteLayerSelection, Double southWestLong,
			Double southWestLat, Double northEastLong, Double northEastLat, Integer zoomLevel) {
		Map kpiCountMap = new HashMap();
		try {
			logger.info("Going to get count for KPI count for Polygon {} ", siteLayerSelection.size());
			for (SiteLayerSelection wrapper : siteLayerSelection) {
				List<List<List<Double>>> polygons = new ArrayList<>();
				polygons = wrapper.getPolygon();
				kpiCountMap = getKpiCountForPolygons(polygons, wrapper);
			}
		} catch (Exception exception) {
			logger.error("Error in getting KPI Poly count {}", Utils.getStackTrace(exception));
		}
		return kpiCountMap;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public List searchNEDetail(List<SiteLayerSelection> siteLayerSelectionList) {
		List outputDataList = new ArrayList();
		try {
			logger.info("Going to search NE Details for input parameters.");
			SiteLayerSelection siteLayerSelection = getSiteLayerSelectionWrapper(siteLayerSelectionList);
			if (siteLayerSelection != null) {
				List groupByList = getGroupByDetails(siteLayerSelection);
				Map viewportMap = getViewportDetails(siteLayerSelection);
				List orderByList = getOrderDetails(siteLayerSelection);
				boolean isDistinct = siteLayerSelection.getIsDistinct();
				List<Tuple> tupleList = networkElementDao.searchNEDetail(siteLayerSelection.getFilters(), siteLayerSelection.getProjection(), groupByList, orderByList, isDistinct, viewportMap);
				for (Tuple t : tupleList) {
					int i = 0;
					Map map = new HashMap<>();
					t.getElements().size();
					for (TupleElement<?> te : t.getElements()) {
						map.put(te.getAlias(), t.get(i));
						i++;
					}
					outputDataList.add(map);
				}
			}
		} catch (Exception exception) {
			logger.error("Unable to get Sites Data for siteLayerSelectionList {} ,due to Exception {}", siteLayerSelectionList, Utils.getStackTrace(exception));
		}
		return outputDataList;
	}

	@SuppressWarnings("rawtypes")
	private SiteLayerSelection getSiteLayerSelectionWrapper(List siteLayerSelectionList) {
		SiteLayerSelection siteLayerSelection = null;
		if (siteLayerSelectionList != null && !siteLayerSelectionList.isEmpty()) {
			siteLayerSelection = (SiteLayerSelection) siteLayerSelectionList.get(0);
		}
		return siteLayerSelection;
	}
	
	@SuppressWarnings("rawtypes")
	private List getGroupByDetails(SiteLayerSelection siteLayerSelection) {
		List groupByColumnsList = null;
		try {
			if (siteLayerSelection != null) {
				Map<?, ?> groupByMap = siteLayerSelection.getGroupByColumns();
				if (groupByMap != null && !groupByMap.isEmpty()) {
					for (Map.Entry groupBy : groupByMap.entrySet()) {
						if (groupBy.getKey() != null) {
							groupByColumnsList = (List) groupBy.getValue();
						}
					}
				}
			}
		} catch (Exception exception) {
			logger.error("Unable to get group by due to exception {}", Utils.getStackTrace(exception));

		}
		return groupByColumnsList;
	}

	@SuppressWarnings("rawtypes")
	private List getOrderDetails(SiteLayerSelection siteLayerSelection) {
		List orderByColumnsList = null;
		try {
			if (siteLayerSelection != null) {
				Map<?, ?> orderByMap = siteLayerSelection.getOrderByColumns();
				if (orderByMap != null && !orderByMap.isEmpty()) {
					for (Map.Entry orderBy : orderByMap.entrySet()) {
						if (orderBy.getKey() != null) {
							orderByColumnsList = (List) orderBy.getValue();
						}
					}
				}
			}
		} catch (Exception exception) {
			logger.error("Unable to get order by due to exception {}", Utils.getStackTrace(exception));

		}
		return orderByColumnsList;
	}

	@SuppressWarnings("rawtypes")
	private Map getViewportDetails(SiteLayerSelection siteLayerSelection) {
		Map viewportMap = null;
		if (siteLayerSelection != null) {
			viewportMap = siteLayerSelection.getViewportMap();
		}
		return viewportMap;
	}

	private Map getKpiCountForPolygons(List<List<List<Double>>> polygons, SiteLayerSelection layerSelection) {
		AtomicDouble counter = new AtomicDouble(0);
		Map<String, Double> viewportMap = new HashMap<>();
		List<Tuple> listBand = new ArrayList<>();
		List<Tuple> bandlist = new ArrayList<>();

		Map kpiCountMap = new HashMap();
		try {
			logger.info("No. of polygons are {}", polygons.size());
			for (List<List<Double>> polygon : polygons) {
				GISGeometry gispolygon = new GIS2DPolygon(polygon);
				Corner bounds = BoundaryUtils.getCornerOfBoundary(polygon);
				Double minlat = bounds.getMinLatitude();
				Double maxlat = bounds.getMaxLatitude();
				Double minlon = bounds.getMinLongitude();
				Double maxlon = bounds.getMaxLongitude();
				viewportMap.put(InfraConstants.SW_LATITUDE, minlat);
				viewportMap.put(InfraConstants.NW_LAT, maxlat);
				viewportMap.put(InfraConstants.SW_LONG, minlon);
				viewportMap.put(InfraConstants.NW_LONG, maxlon);
				List distinctEntity = new ArrayList<>(layerSelection.getProjection().keySet());
				distinctEntity.addAll(new ArrayList<>(layerSelection.getFilters().keySet()));
				logger.info("Going to get count for KPI count for Polygon");
				listBand = ineBandDetailDao.getSiteBandDetail(viewportMap, layerSelection.getFilters(),
						layerSelection.getProjection(), distinctEntity, true, null, false);

				logger.info("Data Recieved from NEBandDetail and size is {}", listBand.get(0));
				listBand = listBand.stream()
						.filter(distinctByKey(p -> String.valueOf(p.get(InfraConstants.NENAME))
								+ String.valueOf(p.get(InfraConstants.NE_NEFREQUENCY_KEY))))
						.collect(Collectors.toList());

				listBand.forEach(site -> {
					if (gispolygon.contains(new GISPoint(new LatLng((Double) site.get(InfraConstants.NE_LATITUDE_KEY),
							(Double) site.get(InfraConstants.NE_LONGITUDE_KEY))))) {
						bandlist.add(site);
					}
				});

				logger.info("size of bandlist is : {}", bandlist);
				getSitesCountForKPI(kpiCountMap, getAggregatedSitesMap(bandlist, InfraConstants.NE_NENAME_KEY));

			}
		} catch (Exception exception) {
			logger.error("Unable to get count for band {} and value {} Exception {}", null, null,
					Utils.getStackTrace(exception));
		}

		return kpiCountMap;
	}

	@Override
	public Map getKPISummaryData(List<SiteLayerSelection> siteLayerSelection, Double southWestLong, Double southWestLat,
			Double northEastLong, Double northEastLat, Integer zoomLevel) {
		Map kpiCountMap = new HashMap();
		try {
			for (SiteLayerSelection layerSite : siteLayerSelection) {
				if (layerSite.getPolygon() != null && !layerSite.getPolygon().isEmpty()) {
					kpiCountMap = getKPICoutForPolygons(siteLayerSelection, southWestLong, southWestLat, northEastLong,
							northEastLat, zoomLevel);
				} else if (layerSite.getGeography() != null && !layerSite.getGeography().isEmpty()) {
					kpiCountMap = getKPICountGeographyWise(siteLayerSelection, southWestLong, southWestLat,
							northEastLong, northEastLat, zoomLevel);
				} else {
					kpiCountMap = getKPISummaryCount(siteLayerSelection, southWestLong, southWestLat, northEastLong,
							northEastLat, zoomLevel);
				}
			}

		} catch (Exception exception) {
			logger.error("Unable to get KPI Summary data due to Exception {}", Utils.getStackTrace(exception));
		}
		return kpiCountMap;

	}

	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	private Map<Object, Map<Object, Long>> getAggregatedSitesMap(List<Tuple> kpiDataList, String nenameKey) {
		Map<Object, Map<Object, Long>> kpiCount = new HashMap();
		try {
			logger.info("Going to get aggregate count for kPI for nenameKey {}", nenameKey);
			AtomicDouble counter = new AtomicDouble(0);
			kpiDataList = kpiDataList.stream()
					.filter(distinctByKey(
							p -> String.valueOf(p.get(nenameKey)) + String.valueOf(p.get(InfraConstants.NE_NETYPE_KEY))
									+ String.valueOf(p.get(InfraConstants.NE_NEFREQUENCY_KEY))))
					.collect(Collectors.toList());
			kpiCount = kpiDataList.stream().collect(Collectors.groupingBy(s -> s.get(InfraConstants.NE_BANDSTATUS_KEY),
					Collectors.groupingBy(s -> s.get(InfraConstants.NE_FREQUENCY), Collectors.counting())));
			logger.info("KPI count frequency and status wise : {}", kpiCount);
		} catch (NullPointerException nullPointerException) {
			logger.error("Unable to get KPI Aggregate KPI status  due to error {} ",
					Utils.getStackTrace(nullPointerException));
		}
		return kpiCount;
	}

	private Map<String, Long> getSitesCountForKPI(Map<String, Long> kpiCountMap,
			Map<Object, Map<Object, Long>> sitesMap) {
		try {
			if (sitesMap != null && !sitesMap.isEmpty()) {
				sitesMap.forEach((k, v) -> {
					try {
						v.forEach((s, count) -> {
							try {
								kpiCountMap.put(String.valueOf(s) + String.valueOf(k), count);
								String key = InfraConstants.TOTAL_COUNT_KEY + String.valueOf(k);
								if (kpiCountMap.containsKey(key)) {
									kpiCountMap.computeIfPresent(key,
											(m, n) -> Long.valueOf((String.valueOf(n))) + count);
								} else {
									kpiCountMap.put(key, count);
								}

							} catch (NullPointerException exception) {
								logger.error("Unable to get aggregated count for key {}  due to exception {}", s,
										Utils.getStackTrace(exception));
							}
						});
					} catch (NullPointerException exception) {
						logger.error("Unable to get aggregated count for key {} and value {}  due to exception {}", k,
								v, Utils.getStackTrace(exception));
					}
				});
			}
		} catch (Exception exception) {
			logger.error("Unable to get KPI Aggregate KPI status  due to error {} ", Utils.getStackTrace(exception));

		}
		logger.info("getSitesCountForKPI return {} ", kpiCountMap);
		return kpiCountMap;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public List searchNetworkElementDetails(List<SiteLayerSelection> siteLayerSelectionList) {
		List outputDataList = new ArrayList();
		try {
			logger.info("Going to search NetworkElement Details for input parameters.");
			SiteLayerSelection siteLayerSelection = getSiteLayerSelectionWrapper(siteLayerSelectionList);
			if (siteLayerSelection != null) {
				List groupByList = getGroupByDetails(siteLayerSelection);
				Map viewportMap = getViewportDetails(siteLayerSelection);
				List orderByList = getOrderDetails(siteLayerSelection);
				boolean isDistinct = siteLayerSelection.getIsDistinct();
				List<Tuple> tupleList = networkElementDao.searchNetworkElementDetails(siteLayerSelection.getFilters(), siteLayerSelection.getProjection(), groupByList, orderByList, isDistinct,
						viewportMap);
				for (Tuple t : tupleList) {
					int i = 0;
					Map map = new HashMap<>();
					t.getElements().size();
					for (TupleElement<?> te : t.getElements()) {
						map.put(te.getAlias(), t.get(i));
						i++;
					}
					outputDataList.add(map);
				}
			}
		} catch (Exception exception) {
			logger.info("Unable to get data for networkElement due to excepton {}", Utils.getStackTrace(exception));
		}
		return outputDataList;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public List sectorPropertiesDetails(List<SiteLayerSelection> siteLayerSelectionList) {
		List outputDataList = new ArrayList();
		try {
			logger.info("inside sectorPropertiesDetails");
			SiteLayerSelection siteLayerSelection = getSiteLayerSelectionWrapper(siteLayerSelectionList);
			List groupByList = getGroupByDetails(siteLayerSelection);
			Map viewportMap = getViewportDetails(siteLayerSelection);
			List orderByList = getOrderDetails(siteLayerSelection);
			boolean isDistinct = siteLayerSelection.getIsDistinct();
			List<Tuple> tupleList = networkElementDao.sectorPropertiesDetails(siteLayerSelection.getFilters(),
					siteLayerSelection.getProjection(), groupByList, orderByList, isDistinct, viewportMap);

			for (Tuple t : tupleList) {
				int i = 0;
				Map map = new HashMap<>();
				t.getElements().size();
				for (TupleElement<?> te : t.getElements()) {
					map.put(te.getAlias(), t.get(i));
					i++;
				}
				outputDataList.add(map);
			}
		} catch (Exception exception) {
			logger.info("Unable to get data for sector Properties due to excepton {}", Utils.getStackTrace(exception));
		}
		return outputDataList;
	}


	private List<Map> getMileStoneMap() {
		Map<String, List<Map>> filters = new HashMap();
		Map filterMap1 = new HashMap();
		filterMap1.put("LABEL_TYPE", "neName");
		filterMap1.put("OPERATION", "EQUALS");
		filterMap1.put("DATATYPE", "String");
		filterMap1.put("VALUE", "NJKT_0870");
		List<Map> neFilterList = new ArrayList<Map>();
		neFilterList.add(filterMap1);
		filters.put(InfraConstants.NETWORKELEMENT_TABLE, neFilterList);

		Map<String, List<String>> projection = new HashMap<String, List<String>>();
		List<String> neBandDetailProjectionList = new ArrayList<String>();
		neBandDetailProjectionList.add("neFrequency");

		List<String> neTaskDetailProjectionList = new ArrayList<String>();
		neTaskDetailProjectionList.add("taskName");
		neTaskDetailProjectionList.add("actualEndDate");
		neTaskDetailProjectionList.add("taskDay");

		projection.put("NEBandDetail", neBandDetailProjectionList);
		projection.put("NETaskDetail", neTaskDetailProjectionList);

		List<SiteLayerSelection> siteLayerSelectionList = new ArrayList<>();
		SiteLayerSelection siteLayerSelection = new SiteLayerSelection();
		siteLayerSelection.setFilters(filters);
		siteLayerSelection.setProjection(projection);
		siteLayerSelection.setIsDistinct(false);
		siteLayerSelectionList.add(siteLayerSelection);
		List<Map> mileStoneMap = searchNetworkElementDetails(siteLayerSelectionList);
		return mileStoneMap;
	}


		@Override
		public Map<String,Map> getSiteOverviewData(SiteLayerSelection siteLayerSelection) {
			Map map = null;
			try {
			if(siteLayerSelection != null ) {
			Map<String, List<Map>> filters=siteLayerSelection.getFilters();
			Map<String, List<String>> projection=siteLayerSelection.getProjection();
			if(filters != null && projection != null) {
			List<Tuple> tupleList=ineBandDetailDao.getSitesOverviewDataForVisualisation(filters, projection);
			if(tupleList != null && !tupleList.isEmpty()) {
			Tuple t=tupleList.get(0);
				map = getTupleElementsInMap(t);
			}
			}
			Map<String, String> sectorMap = new HashMap<>();
			INEVisualizationService ineVisualizationService = ApplicationContextProvider.getApplicationContext().getBean(INEVisualizationService.class);
			if(ineVisualizationService != null) {
				if(map != null) {
				if(map.get(InfraConstants.NE_NENAME_KEY) != null && map.get(InfraConstants.NE_NETYPE_KEY) != null) {
				String neName=map.get(InfraConstants.NE_NENAME_KEY).toString();
				String neType=map.get(InfraConstants.NE_NETYPE_KEY).toString();
				sectorMap = ineVisualizationService.getSectorMap(neName, neType);
				}
				}
			}
			map.put(InfraConstants.SECTOR_MAP, sectorMap);
			return map;
			}
			}catch(Exception exception) {
				logger.error("unable to get site overview data.Exception: {}",exception.getMessage());
			}
			return null;
		}
		@Override
		public Map getSiteGeographicalData(SiteLayerSelection siteLayerSelection) {
			try {
				if(siteLayerSelection != null) {
			Map<String, List<Map>> filters=siteLayerSelection.getFilters();
			Map<String, List<String>> projection=siteLayerSelection.getProjection();
			if(filters != null && projection != null) {
			List<Tuple> tupleList=ineBandDetailDao.getSitesOverviewDataForVisualisation(filters, projection);
			if(tupleList != null && !tupleList.isEmpty()) {
			    Tuple t=tupleList.get(0);
				return getTupleElementsInMap(t);
				}
				}
				}
			}catch(Exception exception) {
			 logger.error("unable to get site geographical data. Exception: {}",exception.getMessage());
			}
			return null;
		}

		private Map getTupleElementsInMap(Tuple t) {
			Map map=new HashMap<>();
			int i = 0;
			for (TupleElement<?> te : t.getElements()) {
				map.put(te.getAlias(), t.get(i));
				i++;
			}
			return map;
		}
		
		@Override
		public List<Map> getNETaskDetailData(SiteLayerSelection siteLayerSelection) {
			logger.info("Going to get NE task detail Data.");
			try {
				Map<String, List<Map>> filters=siteLayerSelection.getFilters();
				
				Map<String, List<String>> projection=siteLayerSelection.getProjection();
				Boolean isGroupBy=siteLayerSelection.getIsGroupBy();
				List<Tuple> tupleList=ineBandDetailDao.getNETaskDetailDataForVisualisation(filters, projection,isGroupBy);
				if(tupleList != null && !tupleList.isEmpty()) {
					List<Map> resultList=new ArrayList<>();
				for (Tuple t : tupleList) {
						resultList.add(getTupleElementsInMap(t));
			   }
				return resultList;
			}
			}catch(Exception exception) {
				logger.warn("Unable to get NE Task Detail data. Exception: {}",exception.getMessage());
			}
			return null;
		}
		@Override
		public List<Map> getSiteSummaryOverviewByBand(SiteLayerSelection siteLayerSelection) {
			try {
				if(siteLayerSelection != null) {
			      return getDataForSectorProperties(siteLayerSelection);
				}
 			}catch(Exception exception) {
				logger.warn("Unable to get site summary overview data by band. Exception: {}",exception.getMessage());
			}

			return null;
		}
		private List<Map> getDataForSectorProperties(SiteLayerSelection siteLayerSelection) {
			List<String> distinctEntity = new ArrayList<>();
			List<Tuple> siteDataVisualize=new ArrayList<>();
			Map<String, List<Map>> filters = siteLayerSelection.getFilters();
			Map<String, List<Map>> plannedfilters = new HashMap<>(filters);
			Map<String, List<String>> projections = siteLayerSelection.getProjection();
			Map<String, List<String>> plannedprojections = new HashMap<>(projections);
			
			String nestatus = null;
			if (siteLayerSelection.getNestatus() != null)
				nestatus = siteLayerSelection.getNestatus();
			distinctEntity = distinctEntity.stream().distinct().collect(Collectors.toList());
			if(filters != null && projections != null) {
			if (nestatus != null && (nestatus.equals(InfraConstants.PLANNED) || nestatus.equals(InfraConstants.NOMINAL))) {
				siteDataVisualize.addAll(iranDetailDao.getCellsDataForVisualisation(null, filters, projections, distinctEntity));
				siteDataVisualize.addAll(ineBandDetailDao.getSitesPlannedDataForVisualisation(null, plannedfilters, plannedprojections, distinctEntity, true, null));
			} else {
				siteDataVisualize.addAll(iranDetailDao.getCellsDataForVisualisation(null, filters, projections, distinctEntity));

			}
		}
			List<Map> mapList = new ArrayList<>();
			for (Tuple tuple : siteDataVisualize) {
				mapList.add(getTupleElementsInMap(tuple));
				
		}
	return mapList;
		}
		@Override
		public List<Map> getSiteSummaryAntennaParametersByBand(SiteLayerSelection siteLayerSelection) {
			try {
			return getDataForSectorProperties(siteLayerSelection);
			}catch(Exception exception) {
				logger.error("unable to get site summary antenna parameter by band. Exception: {}",exception.getMessage());
			}
			return null;

		}

		@Override
		public Map<String, List<Map>> getCellsDataForRAN(SiteLayerSelection siteLayerSelection) {
			Map<String, List<Map>> data = new HashMap<String, List<Map>>();
			try {
				List<Tuple> tuples= iranDetailDao.getCellsDataForRAN(siteLayerSelection.getViewportMap(), siteLayerSelection.getFilters(),siteLayerSelection.getProjection(),siteLayerSelection.getIsDistinct());
				List map = InfraUtils.getMapFromTupleList(tuples);
				data.put(InfraConstants.CELL_VISUALISATION_KEY, map);
			} catch (Exception e) {
				logger.error("Unable to get cells Data for RAN for :{} error={} ",siteLayerSelection,e.getMessage());
			}
			return data;
		}

}

package com.inn.foresight.module.nv.report.service.impl;

import java.awt.Color;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import com.inn.commons.lang.StringUtils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.report.stealthdashboard.wrapper.StealthGeographyDataWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.maps.Corner;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.ZoomFinder;
import com.inn.commons.maps.geometry.GeometryConstants;
import com.inn.commons.maps.geometry.PointUtils;
import com.inn.commons.maps.tiles.Tile;
import com.inn.commons.maps.tiles.TileBoundaryUtils;

import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.coverage.service.ICoverageService;
import com.inn.foresight.module.nv.report.optimizedImage.GoogleMaps;
import com.inn.foresight.module.nv.report.optimizedImage.ImageCreator;
import com.inn.foresight.module.nv.report.service.IMapImagesService;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.ImageOverlay;
import com.inn.foresight.module.nv.report.utils.LegendUtil;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveImageWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.PCIWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWOMapDataWrapper;
import com.inn.product.systemconfiguration.dao.SystemConfigurationDao;
import com.inn.product.systemconfiguration.model.SystemConfiguration;

/** The Class MapImagesServiceImpl. */
@Service("MapImagesServiceImpl")
public class MapImagesServiceImpl implements IMapImagesService {
	/** The logger. */
	private Logger logger = LogManager.getLogger(MapImagesServiceImpl.class);

	/** The image url. */
	String imageUrl = ReportConstants.BLANK_STRING;

	/** The final image path. */
	String finalImagePath = ConfigUtils.getString(ReportConstants.FINAL_IMAGE_PATH);
	
	@Autowired
	ICoverageService coverageService;
	
	@Autowired
    SystemConfigurationDao iSystemConfigurationDao;

	/**
	 * Method return view port by considering DriveData,SiteData and Boundaries.
	 *
	 * @param driveImageWrapper the drive image wrapper
	 * @return the hash map with key as kpiName and bufferedImage as Value
	 */
	@Override
	public HashMap<String, BufferedImage> getDriveImages(DriveImageWrapper driveImageWrapper,List<Double[]> pinLatLonList) throws IOException {
		logger.info("Inside method  getDriveImages ");
		HashMap<String, BufferedImage> imageMap = new HashMap<>();
		String date = ReportUtil.getFormattedDate(new Date(),ReportConstants.DATE_FORMAT_1) ;
		Map<String,Color> earfcnColorMap = DriveHeaderConstants.getEarfcnColorMap();
		String path = finalImagePath + ReportConstants.FORWARD_SLASH + date;
		if(driveImageWrapper!=null && driveImageWrapper.getKpiWrappers()!=null && driveImageWrapper.getDataKPIs()!=null){
			try{
				List<Integer> indexList = driveImageWrapper.getKpiWrappers()
						.stream().parallel().filter(kpiWrapper->kpiWrapper.getIndexKPI()!=null)
						.map(KPIWrapper::getIndexKPI)
						.collect(Collectors.toList());
				logger.info(ReportConstants.INDEX_LIST+ " {} ",indexList);
				HashMap<Integer, PCIWrapper> pciCountMap = new HashMap<>();
				Corner corner = findViewPort(driveImageWrapper);
				if(corner!=null){
					Map<String, Integer> kpiIndexMap=DriveHeaderConstants.getKpiIndexMap();
					kpiIndexMap.put(ReportConstants.CALL_INITIATE, DriveHeaderConstants.INDEX_CALL_INITIATE);
					kpiIndexMap.put(ReportConstants.CALL_DROP, DriveHeaderConstants.INDEX_CALL_DROP);

					kpiIndexMap.put(ReportConstants.CALL_FAILURE, DriveHeaderConstants.INDEX_CALL_FAIL);
					kpiIndexMap.put(ReportConstants.CALL_SUCCESS, DriveHeaderConstants.INDEX_CALL_SUCCESS);
					
					kpiIndexMap.put(ReportConstants.HANDOVER_INITIATE, ForesightConstants.TWENTY_FOUR);

					kpiIndexMap.put(ReportConstants.HANDOVER_FAILURE, 25);

					kpiIndexMap.put(ReportConstants.HANDOVER_SUCCESS, DriveHeaderConstants.INDEX_HANDOVER_SUCCESS);

					kpiIndexMap.put(ReportConstants.TEST_TYPE, DriveHeaderConstants.INDEX_TEST_TYPE);
					kpiIndexMap.put(ReportConstants.CALL_PLOT, DriveHeaderConstants.CALL_PLOT_INDEX);
					kpiIndexMap.put(ReportConstants.HANDOVER_PLOT, DriveHeaderConstants.HANDOVER_PLOT_INDEX);
					return getBufferedImageMap(driveImageWrapper, pinLatLonList, imageMap, earfcnColorMap, path,
							indexList, pciCountMap, corner, kpiIndexMap);
					
				}else{
					throw new BusinessException("Unable to find the view port for corner object "+corner);
				}
			}catch(BusinessException be){
				throw be;
			}
			catch(Exception e){
				logger.error("Exception inside method getDriveImages {} ",Utils.getStackTrace(e));
			}
		}
		logger.info("Finally Going to return the image Map of keySet {} ",imageMap.keySet());
		return imageMap;
	}
	
	@Override
	public HashMap<String, BufferedImage> getDriveImagesForReport(DriveImageWrapper driveImageWrapper,List<Double[]> pinLatLonList, Map<String, Integer> kpiIndexMap) throws IOException {
		logger.info("Inside method  getDriveImages ");
		HashMap<String, BufferedImage> imageMap = new HashMap<>();
		String date = ReportUtil.getFormattedDate(new Date(),ReportConstants.DATE_FORMAT_1) ;
		Map<String,Color> earfcnColorMap = DriveHeaderConstants.getEarfcnColorMap();
		String path = finalImagePath + ReportConstants.FORWARD_SLASH + date;
		if(driveImageWrapper!=null && driveImageWrapper.getKpiWrappers()!=null && driveImageWrapper.getDataKPIs()!=null){
			try{
				List<Integer> indexList = driveImageWrapper.getKpiWrappers()
						.stream().parallel().filter(kpiWrapper->kpiWrapper.getIndexKPI()!=null)
						.map(KPIWrapper::getIndexKPI)
						.collect(Collectors.toList());
				logger.info(ReportConstants.INDEX_LIST+ " {} ",indexList);
				HashMap<Integer, PCIWrapper> pciCountMap = new HashMap<>();
				Corner corner = findViewPort(driveImageWrapper);
				if(corner!=null){
					return getBufferedImageMap(driveImageWrapper, pinLatLonList, imageMap, earfcnColorMap, path,
							indexList, pciCountMap, corner, kpiIndexMap);
				}else{
					throw new BusinessException("Unable to find the view port for corner object "+corner);
				}
			}catch(BusinessException be){
				throw be;
			}
			catch(Exception e){
				logger.error("Exception inside method getDriveImages {} ",Utils.getStackTrace(e));
			}
		}
		logger.info("Finally Going to return the image Map of keySet {} ",imageMap.keySet());
		return imageMap;
	}

	private HashMap<String, BufferedImage> getBufferedImageMap(DriveImageWrapper driveImageWrapper,
			List<Double[]> pinLatLonList, HashMap<String, BufferedImage> imageMap, Map<String, Color> earfcnColorMap,
			String path, List<Integer> indexList, HashMap<Integer, PCIWrapper> pciCountMap, Corner corner, Map<String, Integer> kpiIndexMap)
			throws IOException {
		Map<String, Tile> googleTileMap;
		ImageCreator imagecreator;
		
		int zoomLevel = getZoomLevel(corner);
		logger.info(ReportConstants.FINAL_CORNER_DATA + corner + ReportConstants.ZOOM_LEVEL + zoomLevel);
		googleTileMap = getGoogleTileMap(corner,zoomLevel);
		imagecreator = new ImageCreator(googleTileMap.get(GeometryConstants.GOOGLE_TILE_MIN),
				googleTileMap.get(GeometryConstants.GOOGLE_TILE_MAX), zoomLevel, GoogleMaps.MAP_ROAD, indexList,100.0);
		logger.info(" imagecreator data {} ", imagecreator);
		logger.info("googletile map {}",googleTileMap);
		HashMap<Integer, PCIWrapper> cgiCountMap = new HashMap<>();
		List<String> colorlist = getColorList();
		ReportUtil.populatePciColorMap(driveImageWrapper.getSiteDataList(), pciCountMap, colorlist);
//		ReportUtil.populatecgiColorMap(driveImageWrapper.getSiteDataList(), cgiCountMap);
		Map<String, Long> earfcnCountMap = getEarfcnCountMap(driveImageWrapper, kpiIndexMap);
		Map<String, Color> servingSystemColorMap = ReportUtil.getServingSystemColorMap(driveImageWrapper);
		Map<String, Color> dlBandWidthColorMap = ReportUtil.getDlBandWidthColorMap(driveImageWrapper);
		logger.info("dlBandWidthColorMap {} ", dlBandWidthColorMap);
		imagecreator.drawBoundary(imagecreator.getGooglemaps(), imagecreator.getTerrainmaps(), driveImageWrapper.getBoundaries(),
				Color.BLACK, indexList);
		imagecreator.drawCustomRoute(driveImageWrapper.getDriveRoute(), Color.BLUE);
		Map<String, Color> codecColorMap = getCodecColorMap(driveImageWrapper.getDataKPIs(), kpiIndexMap);
		Integer total = imagecreator.drawDrivePath(imagecreator.getGooglemaps(), pciCountMap, driveImageWrapper,
				earfcnColorMap, servingSystemColorMap, dlBandWidthColorMap, cgiCountMap, kpiIndexMap, colorlist,
				codecColorMap);
		logger.info("After plotting data earfcnColorMap Size {} ", earfcnColorMap.size());
		imagecreator.drawSites(imagecreator.getGooglemaps(), imagecreator.getTerrainmaps(), driveImageWrapper.getSiteDataList(),
				pciCountMap, indexList);
		drawCustomSatelliteImages(imagecreator, driveImageWrapper, pciCountMap, pinLatLonList, kpiIndexMap);
		setCustomImagesInMap(total, imageMap, pciCountMap, earfcnCountMap, earfcnColorMap, servingSystemColorMap,
				driveImageWrapper, dlBandWidthColorMap, cgiCountMap, kpiIndexMap, codecColorMap);
		ReportUtil.createMapofImages(imageMap, driveImageWrapper.getKpiWrappers(), imagecreator,
				path + ReportConstants.FORWARD_SLASH + GoogleMaps.MAP_ROAD, false);
		logger.info("Finally Going to return the image Map of keySet {} ", imageMap.keySet());
		return imageMap;
	}

	private Map<String, Tile> getGoogleTileMap(Corner corner, int zoomLevel) {
		Map<String, Tile> googleTileMap = new HashMap<>();
		try {
			googleTileMap = TileBoundaryUtils.getSquareMinMaxGoogleTile(corner, zoomLevel);
		} catch (Exception e) {
			Corner customCorner = PointUtils.getViewPortAroundPoint(
					new LatLng(corner.getMinLatitude(), corner.getMinLongitude()), ReportConstants.SQUARE_WIDTH_SIZE);
			logger.info("customCorner Object {} ",customCorner);
			googleTileMap = TileBoundaryUtils.getSquareMinMaxGoogleTile(customCorner, zoomLevel);
			logger.info("Exception inside method getGoogleTileMap , Finally google Tile Map is ",e.getMessage());
		}
		logger.info(ReportConstants.GOOGLE_TILE_MAP_MIN+" {} ",googleTileMap.get(GeometryConstants.GOOGLE_TILE_MIN));
		logger.info(ReportConstants.GOOGLE_TILE_MAP_MAX+" {} ",googleTileMap.get(GeometryConstants.GOOGLE_TILE_MAX));
		return googleTileMap;
	}

	@Override
	public Map<String, Long> getEarfcnCountMap(DriveImageWrapper driveImageWrapper, Map<String, Integer> kpiIndexMap) {
		Map<String, Long> earfcnCountMap =null;
		if (kpiIndexMap != null) {
			Integer dlEarfcnIndex = kpiIndexMap.get(ReportConstants.EARFCN);
			// Integer dlEarfcnIndex =
			// ReportUtil.findKPIIndexFromKPIWrapper(driveImageWrapper,
			// ReportConstants.DL_EARFCN);
			logger.info("value of dlEarfcnIndex {}", dlEarfcnIndex);
			try {
				List<Integer> filterIndexes = Arrays.asList(dlEarfcnIndex);
				List<String> earfcnList = new ArrayList<>();
				driveImageWrapper.getDataKPIs().forEach(list -> {
					List<String> originalList = Arrays.asList(list);
					List<String> filteredList = IntStream.range(0, originalList.size())
							.filter(index -> filterIndexes.contains(index)).mapToObj(originalList::get)
							.collect(Collectors.toList());
					earfcnList.addAll(filteredList);
				});
				logger.info("Earfcn List Size {} ", earfcnList.size());
				earfcnCountMap = earfcnList.stream()
						.collect(Collectors.groupingBy(earfcn -> earfcn, Collectors.counting()));
			} catch (Exception e) {
				logger.error("Exception inside method getEarfcnCountMap {} ", Utils.getStackTrace(e));
			}
		    }
		logger.info("Earfcn count map Size {}, map {}  ",earfcnCountMap!=null?earfcnCountMap.size():null,earfcnCountMap);
		return earfcnCountMap;
	}

	private void drawCustomSatelliteImages(ImageCreator imagecreator, DriveImageWrapper driveImageWrapper,
			HashMap<Integer, PCIWrapper> pciCountMap, List<Double[]> pinLonLatList, Map<String, Integer> kpiIndexMap) {
		logger.info("Inside method  drawCustomSatelliteImages  ");
		try {
			if(kpiIndexMap==null) {
				kpiIndexMap=new HashMap<>();
				kpiIndexMap.put(ReportConstants.RSRP, DriveHeaderConstants.INDEX_RSRP);
			
			}
			Integer indexRSRP = ReportUtil.findKPIIndexFromKPIWrapper(driveImageWrapper, ReportConstants.RSRP);
			imagecreator.drawSiteData(imagecreator.getSatellitemaps()[ReportConstants.INDEX_ZER0].getImg(),imagecreator.getSatellitemaps(), driveImageWrapper.getSiteDataList(), pciCountMap,null);
         
			List<KPIWrapper> rsrpWrapper =  driveImageWrapper.getKpiWrappers().stream().filter(kpiWrapper->kpiWrapper.getIndexKPI().equals(indexRSRP)).collect(Collectors.toList());
			
			if (Utils.isValidList(rsrpWrapper)) {
				imagecreator.drawPathOnAnImage(imagecreator.getSatellitemaps()[ReportConstants.INDEX_ONE],
						driveImageWrapper.getDataKPIs(), driveImageWrapper.getIndexLatitude(),
						driveImageWrapper.getIndexLongitude(), rsrpWrapper.get(0), Color.BLACK, kpiIndexMap);

				imagecreator.drawPathOnAnImage(imagecreator.getSatellitemaps()[ReportConstants.INDEX_TWO],
						driveImageWrapper.getDataKPIs(), driveImageWrapper.getIndexLatitude(),
						driveImageWrapper.getIndexLongitude(), rsrpWrapper.get(0), Color.BLACK, kpiIndexMap);
				imagecreator.drawBoundaryOnAnImage(imagecreator.getSatellitemaps()[ReportConstants.INDEX_TWO],
						driveImageWrapper.getBoundaries(), Color.BLACK);

				imagecreator.drawPathOnAnImage(imagecreator.getSatellitemaps()[ReportConstants.INDEX_THREE],
						driveImageWrapper.getDataKPIs(), driveImageWrapper.getIndexLatitude(),
						driveImageWrapper.getIndexLongitude(), rsrpWrapper.get(0), null, kpiIndexMap);
			}
			logger.info("pinLonLatList Data before {} ",pinLonLatList);
			if(pinLonLatList!=null){
				imagecreator.drawPinOnImage(imagecreator.getSatellitemaps()[ReportConstants.INDEX_THREE],pinLonLatList);
			}
		} catch (Exception e) {
			logger.error("Exception inside method drawCustomSatelliteImages  {} ",Utils.getStackTrace(e));
		}
	}

	/**
	 * Find view port.
	 *
	 * @param driveImageWrapper the drive image wrapper
	 * @return the corner
	 */
	private Corner findViewPort(DriveImageWrapper driveImageWrapper) {
		logger.info("inside method findViewPort on the basis of drivedata,boundary and site data ");
		Corner cornerD = null;
		try{
			if(driveImageWrapper.getDataKPIs()!=null) {
				cornerD = ReportUtil.getminmaxLatlOnDriveList(driveImageWrapper.getDataKPIs(), driveImageWrapper.getIndexLatitude(), driveImageWrapper.getIndexLongitude());
				logger.info("corner ObjectD after Drive Data {}  " ,cornerD != null ? cornerD.toString() : null);
			}
			Corner cornerB = ReportUtil.getViewPortFromMultipleBoundaries(driveImageWrapper.getBoundaries());
			logger.info("corner ObjectB after boundaries  {} ", cornerB != null ? cornerB.toString() : null);
			cornerD = cornerD != null ? cornerD.reduce(cornerB) : cornerB;
			logger.info("Reducing DriveCorner and boundaryCorner {} ", cornerD != null ? cornerD.toString() : null);
			Corner cornerS = ReportUtil.getminmaxLatlOnSiteList(driveImageWrapper.getSiteDataList());
			logger.info("cornerS object of siteData  {}  ", cornerS != null ? cornerS.toString() : null);
			cornerD = cornerD != null ? cornerD.reduce(cornerS) : cornerS;
			logger.info("Reducing DriveCorner and SiteCorner {} ",cornerD != null ? cornerD.toString() : null);
		}catch(Exception e){
			logger.error("Exception inside method findViewPort {} ", e.getMessage());
		}
		return cornerD;
	}

	private Corner findViewPort(List<StealthWOMapDataWrapper> stealthMapDataWrapper) {
		logger.info("inside method findViewPort on the basis of drivedata,boundary and site data ");
		Corner cornerD = null;
		try{
			if(stealthMapDataWrapper!=null && !stealthMapDataWrapper.isEmpty()) {
				cornerD = ReportUtil.getminmaxLatLongForStealthMap(stealthMapDataWrapper);
				logger.info("corner ObjectD after Drive Data {}  " ,cornerD != null ? cornerD.toString() : null);
			}
		}catch(Exception e){
			logger.error("Exception inside method findViewPort {} ", e.getMessage());
		}
		return cornerD;
	}

	/**
	 * Gets the zoom level.
	 *
	 * @param corner the map min max lat lon
	 * @return the zoom level
	 */
	private int getZoomLevel(Corner corner) {
		try {
			return ZoomFinder.findZoom(corner.getMaxLatitude(), corner.getMinLatitude(), corner.getMaxLongitude(),corner.getMinLongitude());
		} catch (Exception e) {
			logger.error("Exception in getting zoom level from MinMax Lat Lon {} ",Utils.getStackTrace(e));
			return 0;
		}
	}

	/**
	 * This method saves the drive images at specified path .
	 *
	 * @param driveImageMap the drive image map
	 * @param path the path
	 * @return the hashMap of key as imageName and value as imageLocation
	 */
	@Override
	public HashMap<String,String> saveDriveImages(HashMap<String, BufferedImage> driveImageMap,String path, boolean isAtollImages) {
		logger.info("Inside method saveDriveImages to save the image on disk {} , path {} ",driveImageMap!=null?driveImageMap.size():null,path);
		ReportUtil.createDirectory(path);
		HashMap<String,String> imagePathMap = new HashMap<>();
		if(driveImageMap!=null && !driveImageMap.isEmpty()){
		for (Entry<String, BufferedImage> map : driveImageMap.entrySet()) {
			try {
				if(isAtollImages){
					ImageIO.write(map.getValue(), "PNG", new File(path + ReportConstants.FORWARD_SLASH + map.getKey() + ReportConstants.DOT_PNG));
					imagePathMap.put(map.getKey(), path + ReportConstants.FORWARD_SLASH +  map.getKey()+ ReportConstants.DOT_PNG);
				}else{
					ImageIO.write(map.getValue(), "jpg", new File(path + ReportConstants.FORWARD_SLASH + map.getKey() + ReportConstants.DOT_JPG));
					imagePathMap.put(map.getKey(), path + ReportConstants.FORWARD_SLASH +  map.getKey()+ ReportConstants.DOT_JPG);
				}
			} catch (IOException e) {
				logger.error("IOException during persist of image on disk {} ",Utils.getStackTrace(e));
			}
		  }
		}
		return imagePathMap;
	}
	
	/**
	 * Method return the kpi Legend Images   .
	 *
	 * @param list the list
	 * @return retuns the Map with kpiName as key and BufferedImage as value
	 */
	@Override
	public HashMap<String, BufferedImage> getLegendImages(List<KPIWrapper> list,List<String[]> driveData) {
		logger.info("Inside method getLegendImages to save the image on disk {} ",list!=null?list.size():null);
		HashMap<String,BufferedImage > imageMap = new HashMap<>();
		list = LegendUtil.populateLegendData(list,driveData, DriveHeaderConstants.INDEX_TEST_TYPE);
		for (KPIWrapper wrapper : list) {
			try {
				imageMap.put(ReportConstants.LEGEND+ReportConstants.UNDERSCORE+wrapper.getIndexKPI(), LegendUtil.getLegendImage(wrapper.getTotalCount(), wrapper.getKpiName().replace(ReportConstants.UNDERSCORE, ReportConstants.SPACE),wrapper.getRangeSlabs()));
			}
			catch(Exception e){
				logger.error("Exception occured inside method getLegendImages {} ",Utils.getStackTrace(e));
			}
		}
		return imageMap;
	}
	
	@Override
	public HashMap<String, BufferedImage> getLegendImagesForReport(List<KPIWrapper> list,List<String[]> driveData, Integer testTypeIndex) {
		logger.info("Inside method getLegendImages to save the image on disk {} ",list!=null?list.size():null);
		HashMap<String,BufferedImage > imageMap = new HashMap<>();
		list = LegendUtil.populateLegendData(list,driveData, testTypeIndex);
		for (KPIWrapper wrapper : list) {
			try {
				imageMap.put(ReportConstants.LEGEND+ReportConstants.UNDERSCORE+wrapper.getIndexKPI(), LegendUtil.getLegendImage(wrapper.getTotalCount(), wrapper.getKpiName().replace(ReportConstants.UNDERSCORE, ReportConstants.SPACE),wrapper.getRangeSlabs()));
			}
			catch(Exception e){
				logger.error("Exception occured inside method getLegendImages {} ",Utils.getStackTrace(e));
			}
		}
		return imageMap;
	}

	/**
	 * Method return the kpi Legend Images   .
	 *
	 * @param list the list
	 * @return retuns the Map with kpiName as key and BufferedImage as value
	 */
	@Override
	public HashMap<String, BufferedImage> getLegendImagesForIBBenchmark(List<KPIWrapper> list,List<String[]> driveData) {
		logger.info("Inside method getLegendImagesForIBBenchmark to save the image on disk {} ",list!=null?list.size():null);
		HashMap<String,BufferedImage > imageMap = new HashMap<>();
		list = LegendUtil.populateLegendData(list,driveData, DriveHeaderConstants.INDEX_TEST_TYPE);
		for (KPIWrapper wrapper : list) {
			try {
				imageMap.put(ReportConstants.LEGEND + ReportConstants.UNDERSCORE + wrapper.getKpiName(),
						LegendUtil.getLegendImageForIBBenchmark(wrapper.getTotalCount(),
								wrapper.getKpiName().replace(ReportConstants.UNDERSCORE, ReportConstants.SPACE),
								wrapper.getRangeSlabs()));
			}
			catch(Exception e){
				logger.error("Exception occured inside method getLegendImagesForIBBenchmark {} ",Utils.getStackTrace(e));
			}
		}
		return imageMap;
	}

	private void setCustomImagesInMap(Integer total, HashMap<String, BufferedImage> map,
			HashMap<Integer, PCIWrapper> pciCountMap, Map<String, Long> earfcnCountMap, Map<String, Color> earfcnColorMap, Map<String, Color> servingSystemColorMap,
			DriveImageWrapper driveImageWrapper, Map<String, Color> dlBandWidthColorMap,
			HashMap<Integer, PCIWrapper> cgiCountMap, Map<String, Integer> kpiIndexMap,
			Map<String, Color> codecColorMap) {
		logger.info("Inside method setCustomImagesInMap for PCI , EARFCN Images with total value {} ",total);
		try {
			map.put(ReportConstants.KEY_LEGENDS,LegendUtil.getCustomImage(total, pciCountMap,null,ReportConstants.LTE_SPACE_PCI,null));
			map.put(ReportConstants.DL_EARFCN,LegendUtil.getCustomImage(null, null,earfcnCountMap,ReportConstants.EARFCN,earfcnColorMap));
			map.put(ReportConstants.SERVING_SYSTEM,LegendUtil.writeServingDataFromMap(ReportUtil.getServingSystemMapCount(driveImageWrapper), servingSystemColorMap, ReportConstants.SERVING_SYSTEM));
			map.put(ReportConstants.LEGEND_TECHNOLOGY,LegendUtil.writeServingDataFromMap(ReportUtil.getTechnologyCountMap(driveImageWrapper), null, DriveHeaderConstants.TECHNOLOGY));
			map.put(ReportConstants.DL_BANWIDTH,LegendUtil.writeServingDataFromMap(ReportUtil.getDlBandWidthCountMap(driveImageWrapper), dlBandWidthColorMap, ReportConstants.DL_BANWIDTH.replace("_", "")));
			map.put(DriveHeaderConstants.CGI,LegendUtil.getCustomImage(total, cgiCountMap,null,DriveHeaderConstants.CGI,null));
			map.put(ReportConstants.VOLTE_CODEC,LegendUtil.getLegendImageForVoLTECodec(driveImageWrapper.getDataKPIs(), kpiIndexMap, codecColorMap));
			map.put(ReportConstants.MIMO,LegendUtil.getLegendImageForMimo(driveImageWrapper.getDataKPIs(), kpiIndexMap));
			map.put(ReportConstants.DL_MODULATION_TYPE,LegendUtil.getLegendImageForDlModulationType(driveImageWrapper.getDataKPIs(), kpiIndexMap));
			map.put(ReportConstants.HANDOVER_PLOT,LegendUtil.getLegendImageForHandoverEvents(driveImageWrapper.getDataKPIs(), kpiIndexMap));
			map.put(ReportConstants.ERAB_PLOT, LegendUtil.getLegendImageForRRCEvents(driveImageWrapper.getDataKPIs(), kpiIndexMap));
			map.put(ReportConstants.CALL_PLOT, LegendUtil.getLegendImageForCallEvents(false, driveImageWrapper.getDataKPIs(), kpiIndexMap));
			map.put(ReportConstants.CALL_SETUP_PLOT, LegendUtil.getLegendImageForCallEvents(true, driveImageWrapper.getDataKPIs(), kpiIndexMap));
		} catch (Exception e) {
			logger.error("Exception in method setCustomImagesInMap {} ",Utils.getStackTrace(e));
		}
	}

	@Override
	public HashMap<String, BufferedImage> getStationaryImages(DriveImageWrapper driveImageWrapper,List<Double[]> pinLonLatList,String recipeName) {
		logger.info("Inside method  getStationaryImages  for pinLonLatList {}  ",pinLonLatList);
		HashMap<String, BufferedImage> imageMap = new HashMap<>();
		if(driveImageWrapper!=null){
			try{
				List<Integer> indexList = new ArrayList<>();
				Corner corner = findViewPort(driveImageWrapper);
				ImageCreator imagecreator=null;
				if(corner!=null || !pinLonLatList.isEmpty()){
					HashMap<Integer, PCIWrapper> pciCountMap = new HashMap<>();
					List<String> colorlist = getColorList();
					ReportUtil.populatePciColorMap(driveImageWrapper.getSiteDataList(), pciCountMap,colorlist);
					if(corner == null || corner.getMinLatitude() == null || corner.getMaxLatitude() == null || corner.getMinLongitude() == null || corner.getMaxLongitude() == null) {
						corner = getCornerObjectIfLatLonNull(pinLonLatList, corner);
					}
					int zoomLevel = getZoomLevel(corner);
					logger.info("Final Corner {}, zoomLevel {}",corner, zoomLevel);
					Map<String, Tile> googleTileMap = getGoogleTileMap(corner, zoomLevel);
					logger.info("googleTileMap min {}",googleTileMap.get(GeometryConstants.GOOGLE_TILE_MIN));
					logger.info("googleTileMap Max {}",googleTileMap.get(GeometryConstants.GOOGLE_TILE_MAX));
					imagecreator = new ImageCreator(googleTileMap.get(GeometryConstants.GOOGLE_TILE_MIN), googleTileMap.get(GeometryConstants.GOOGLE_TILE_MAX), zoomLevel,GoogleMaps.MAP_ROAD,indexList);
					imagecreator.drawSites(imagecreator.getGooglemaps(),imagecreator.getTerrainmaps(), driveImageWrapper.getSiteDataList(), pciCountMap,indexList);
					for (Integer index : indexList) {
						if (imagecreator != null) {
							imagecreator.drawBoundary(imagecreator.getGooglemaps(),imagecreator.getTerrainmaps(), driveImageWrapper.getBoundaries(),
									Color.BLACK, indexList);
							if (pinLonLatList != null) {
								imagecreator.drawPinOnImage(imagecreator.getGooglemaps()[index], pinLonLatList);
							}
						}
					}
					imageMap.put(recipeName, imagecreator.getGooglemaps()!=null?imagecreator.getGooglemaps()[0].getImg():null);
				}else{
					imageMap.put(recipeName, null);
				}
					logger.info("Going to return the map of Size {}",imageMap.size());
				return imageMap;
			}catch(Exception e){
				logger.error("Exception inside method getDriveImages {} ",Utils.getStackTrace(e));
			}
		}
		return imageMap;
	}

	private Corner getCornerObjectIfLatLonNull(List<Double[]> pinLonLatList, Corner corner) {
		if(pinLonLatList!=null && !pinLonLatList.isEmpty()){
			corner =corner!=null?corner:new Corner();
			corner.setMinLatitude(pinLonLatList.get(0)[1]);
			corner.setMaxLatitude(pinLonLatList.get(0)[1]);
			corner.setMinLongitude(pinLonLatList.get(0)[0]);
			corner.setMaxLongitude(pinLonLatList.get(0)[0]);
			logger.info("Inside method getCornerObjectIfLatLonNull , corner {} ",corner.toString());
		}
		return corner;
	}

	
	
	private void printSomeIndexedData(DriveImageWrapper driveImageWrapper,int... indexeList) {
		for (int index : indexeList) {
			List<String> data  = driveImageWrapper.getDataKPIs().stream()
					.map(e -> index < e.length ? e[index] : null)
					.filter(e -> e != null && !e.trim().isEmpty())
					.collect(Collectors.toList());
			logger.info("data at index is {} ,  {} ",index,data);
		}
	}
	
	@Override
	public HashMap<String, BufferedImage> getLegendImagesForNVDashboard(List<KPIWrapper> list) {
		logger.info("Inside method getLegendImagesForNVDashboard to save the image on disk {} ", list != null ? list.size() : null);
		HashMap<String, BufferedImage> imageMap = new HashMap<>();
		for (KPIWrapper wrapper : list) {
			try {
				imageMap.put(ReportConstants.LEGEND + ReportConstants.UNDERSCORE + wrapper.getKpiName(),
						LegendUtil.getLegendImageForNVDashboard(wrapper.getKpiName().replace(ReportConstants.UNDERSCORE,ReportConstants.SPACE),
								wrapper.getRangeSlabs()));
			} catch (Exception e) {
				logger.error("Exception occured in generating legend Image {} ", Utils.getStackTrace(e));
			}
		}
		return imageMap;
	}
	
	@Override
	public HashMap<String, BufferedImage> getAtollPredictionImages(DriveImageWrapper driveImageWrapper) {
		logger.info("Inside method  getAtollPredictionImages ");
		HashMap<String, BufferedImage> imageMap = new HashMap<>();
		String date = ReportUtil.getFormattedDate(new Date(),ReportConstants.DATE_FORMAT_1) ;
		String path = finalImagePath + ReportConstants.FORWARD_SLASH + date;
		if(driveImageWrapper!=null && driveImageWrapper.getKpiWrappers()!=null && driveImageWrapper.getDataKPIs()!=null){
			try{
				List<Integer> indexList = driveImageWrapper.getKpiWrappers()
						.stream().parallel().filter(kpiWrapper->kpiWrapper.getIndexKPI()!=null)
						.map(KPIWrapper::getIndexKPI)
						.collect(Collectors.toList());
				Map<String,Integer>kpiIndexMap=new HashMap<>();
				for (KPIWrapper wrapper : driveImageWrapper.getKpiWrappers()) {
					kpiIndexMap.put(wrapper.getKpiName(), wrapper.getIndexKPI());
				}
				HashMap<Integer, PCIWrapper> pciCountMap = new HashMap<>();
				Corner corner = findViewPort(driveImageWrapper);
				ImageCreator imagecreator=null;
				if(corner!=null){
					int zoomLevel = getZoomLevel(corner);
					logger.info("Final Corner {}, zoomLevel {}",corner, zoomLevel);
					Map<String, Tile> googleTileMap = getGoogleTileMap(corner,zoomLevel);
					logger.info("googleTileMap min {}",googleTileMap.get(GeometryConstants.GOOGLE_TILE_MIN));
					logger.info("googleTileMap Max {}",googleTileMap.get(GeometryConstants.GOOGLE_TILE_MAX));
					imagecreator = new ImageCreator(googleTileMap.get(GeometryConstants.GOOGLE_TILE_MIN), googleTileMap.get(GeometryConstants.GOOGLE_TILE_MAX), zoomLevel,GoogleMaps.MAP_ROAD,indexList);
					List<String> colorlist = getColorList();
					ReportUtil.populatePciColorMap(driveImageWrapper.getSiteDataList(), pciCountMap,colorlist);
					overlayingAtollImages(driveImageWrapper, corner, imagecreator, zoomLevel);
					imagecreator.drawSites(imagecreator.getGooglemaps(),imagecreator.getTerrainmaps(), driveImageWrapper.getSiteDataList(), pciCountMap,indexList);
				  imagecreator.drawDrivePath(imagecreator.getGooglemaps(), pciCountMap,driveImageWrapper,null,null,null,null,kpiIndexMap,colorlist,
						  null);

				}
				ReportUtil.createMapofImages(imageMap, driveImageWrapper.getKpiWrappers(), imagecreator, path+ReportConstants.FORWARD_SLASH+GoogleMaps.MAP_ROAD,true);
			}catch(Exception e){
				logger.error("Exception inside method getAtollPredictionImages {} ",Utils.getStackTrace(e));
			}
		}
		logger.info("Finally Going to return the Atoll Image Map of keySet {} ",imageMap.keySet());
		return imageMap;
	}

	private void overlayingAtollImages(DriveImageWrapper driveImageWrapper, Corner corner, ImageCreator imagecreator,
			int zoomLevel) {
		logger.info("Data for Atoll iamge , corenr {} , zoomLevel {} ",corner,zoomLevel);
		for (KPIWrapper kpiwrapper : driveImageWrapper.getKpiWrappers()) {
			if(ReportUtil.isAtollImageRequiredForKpi(kpiwrapper.getKpiName())){
				try {
					BufferedImage fgImage = coverageService.getAtollCoverageImageforKpiInViewPort(corner.getSouthWestLat(), corner.getSouthWestLon(), corner.getNorthEastLat(), 
							corner.getNorthEastLon(), zoomLevel, getKPiNameForAtollData(kpiwrapper.getKpiName()),driveImageWrapper.getBand(), "planned");
					if(fgImage!=null){
						logger.info("Going to overlay kpi {} ",kpiwrapper.getKpiName());
						ImageIO.write(fgImage, "PNG", new File(ConfigUtils.getString(ReportConstants.IMAGE_PATH_FOR_NV_REPORT)+ReportConstants.SSVT+ReportConstants.FORWARD_SLASH+kpiwrapper.getKpiName()+".png"));
						BufferedImage bgImage = imagecreator.getGooglemaps()[kpiwrapper.getIndexKPI()].getImg();
						imagecreator.getGooglemaps()[kpiwrapper.getIndexKPI()].setImg(ImageOverlay.overlayImages(bgImage, fgImage));
					}else{
						logger.info("There is no Atoll Image Data Available for kpiName {} ",kpiwrapper.getKpiName());
					}
				} catch (Exception e) {
					logger.info("Unable to overlay the image of kpi {} ",e.getMessage());
				}
			}
		}
	}

	private String getKPiNameForAtollData(String kpiName) {
		switch (kpiName) {
		case ReportConstants.RSRP:
			return ReportConstants.RSRP;
		case ReportConstants.SINR:
			return ReportConstants.SINR;
		case ReportConstants.UL_THROUGHPUT:
			return "UL";
		case ReportConstants.DL_THROUGHPUT:
			return "DL";
		default :
				return kpiName;
			
		}
	}
	
		/**
	 * Method return the kpi Legend Images   .
	 *
	 * @param list the list
	 * @return retuns the Map with kpiName as key and BufferedImage as value
	 */
	@Override
	public HashMap<String, BufferedImage> getAtollLegendImages(List<KPIWrapper> list) {
		logger.info("Inside method getLegendImages to save the image on disk {} ",list!=null?list.size():null);
		HashMap<String,BufferedImage > imageMap = new HashMap<>();
		for (KPIWrapper wrapper : list) {
			try {
				imageMap.put(ReportConstants.ATOLL_LEGEND+ReportConstants.UNDERSCORE+wrapper.getIndexKPI(), LegendUtil.getAtollLegendImage(wrapper.getKpiName().replace(ReportConstants.UNDERSCORE, ReportConstants.SPACE),wrapper.getRangeSlabs()));
				ImageIO.write(imageMap.get(ReportConstants.ATOLL_LEGEND+ReportConstants.UNDERSCORE+wrapper.getIndexKPI()), ReportConstants.JPG, new File(ConfigUtils.getString(ReportConstants.SSVT_REPORT_PATH)  +  wrapper.getKpiName()+ ReportConstants.DOT_JPG));
			}
			catch(Exception e){
				logger.error("Exception occured in generating legend Image {} ",Utils.getStackTrace(e));
			}
		}
		return imageMap;
	}
	
	@Override
	public BufferedImage getMapImageForStealthWO(List<StealthWOMapDataWrapper> stealthMapDataWrapper) {
		if(stealthMapDataWrapper != null && !stealthMapDataWrapper.isEmpty()){
			try{
				List<Integer> indexList = new ArrayList<>();
				logger.info("indexList is  {} ",indexList);
				Corner corner = findViewPort(stealthMapDataWrapper);
				ImageCreator imagecreator=null;
				if(corner!=null){
					int zoomLevel = getZoomLevel(corner);
					logger.info("Final Corner {}, zoomLevel {}",corner, zoomLevel);
					Map<String, Tile> googleTileMap = getGoogleTileMap(corner, zoomLevel);
					logger.info("googleTileMap min {}",googleTileMap.get(GeometryConstants.GOOGLE_TILE_MIN));
					logger.info("googleTileMap Max {}",googleTileMap.get(GeometryConstants.GOOGLE_TILE_MAX));
					imagecreator = new ImageCreator(googleTileMap.get(GeometryConstants.GOOGLE_TILE_MIN), googleTileMap.get(GeometryConstants.GOOGLE_TILE_MAX), zoomLevel,GoogleMaps.MAP_ROAD,indexList);
					for (Integer index : indexList) {
						imagecreator.drawStealthDevicesOnImage(imagecreator.getGooglemaps()[index], stealthMapDataWrapper);
					}
				}
				return imagecreator!=null?imagecreator.getGooglemaps()[0].getImg():null;
			}catch(Exception e){
				logger.error("Exception inside method getDriveImages {} ",Utils.getStackTrace(e));
			}
		}
		return null;
	}
	
	@Override
	public HashMap<String, BufferedImage> getSitesLocationImage(DriveImageWrapper driveImageWrapper) {
		logger.info("Inside method  getSitesLocationImage ");
		HashMap<String, BufferedImage> imageMap = new HashMap<>();
		if(driveImageWrapper.getSiteDataList()!=null){
			try{
				List<Integer> indexList = new ArrayList<>();
				logger.info("indexList is  {} ",indexList);
				Corner corner = findViewPort(driveImageWrapper);
				ImageCreator imagecreator=null;
				if(corner!=null){
					int zoomLevel = getZoomLevel(corner);
					logger.info("Final Corner {}, zoomLevel {}",corner, zoomLevel);
					Map<String, Tile> googleTileMap = getGoogleTileMap(corner, zoomLevel);
					logger.info("googleTileMap min {}",googleTileMap.get(GeometryConstants.GOOGLE_TILE_MIN));
					logger.info("googleTileMap Max {}",googleTileMap.get(GeometryConstants.GOOGLE_TILE_MAX));
					imagecreator = new ImageCreator(googleTileMap.get(GeometryConstants.GOOGLE_TILE_MIN), googleTileMap.get(GeometryConstants.GOOGLE_TILE_MAX), zoomLevel,GoogleMaps.MAP_ROAD,indexList);
					imagecreator.drawBoundary(imagecreator.getGooglemaps(),imagecreator.getTerrainmaps(),driveImageWrapper.getBoundaries(), Color.BLACK,indexList);
					imagecreator.drawSiteLocation(imagecreator.getGooglemaps(), driveImageWrapper.getSiteDataList(),indexList);
					imageMap.put(ReportConstants.SITE_LOCATION_IMG, imagecreator.getGooglemaps()[ReportConstants.INDEX_ZER0].getImg());
				}
				logger.info("Going to return the map of Size {} , {} ",imageMap.size(),imageMap);
				return imageMap;
			}catch(Exception e){
				logger.error("Exception inside method getSitesLocationImage {} ",Utils.getStackTrace(e));
			}
		}
		return imageMap;
	}

	@Override
	public HashMap<String, BufferedImage> getBuildingLocationImage(DriveImageWrapper driveImageWrapper,List<Double[]> pinLonLatList,String recipeName) {
		logger.info("Inside method  getStationaryImages  for pinLonLatList {}  ",pinLonLatList);
		HashMap<String, BufferedImage> imageMap = new HashMap<>();
		if(driveImageWrapper!=null){
			try{
				List<Integer> indexList = new ArrayList<>();
				Corner corner = findViewPort(driveImageWrapper);
				ImageCreator imagecreator=null;
				if(corner!=null || !pinLonLatList.isEmpty()){
					int zoomLevel = getZoomLevel(corner);
					logger.info("Final Corner {}, zoomLevel {}",corner, zoomLevel);
					Map<String, Tile> googleTileMap = getGoogleTileMap(corner, zoomLevel);
					imagecreator = new ImageCreator(googleTileMap.get(GeometryConstants.GOOGLE_TILE_MIN), googleTileMap.get(GeometryConstants.GOOGLE_TILE_MAX), zoomLevel,GoogleMaps.MAP_HYBRID,indexList);
					logger.info("googleTileMap min {}",googleTileMap.get(GeometryConstants.GOOGLE_TILE_MIN));
					logger.info("googleTileMap Max {}",googleTileMap.get(GeometryConstants.GOOGLE_TILE_MAX));
					for (Integer index : indexList) {
						if (pinLonLatList != null) {
							imagecreator.drawPinOnImage(imagecreator.getGooglemaps()[index], pinLonLatList);
						}
					}
					imageMap.put(recipeName, imagecreator.getGooglemaps()!=null?imagecreator.getGooglemaps()[0].getImg():null);
				} else {
					imageMap.put(recipeName, null);
				}
					logger.info("Going to return the map of Size {} , {} ",imageMap.size(),imageMap);
				return imageMap;
			}catch(Exception e){
				logger.error("Exception inside method getDriveImages {} ",Utils.getStackTrace(e));
			}
		}
		return imageMap;
	}

	public List<String> getColorList() {
		List<SystemConfiguration> jsonlist = iSystemConfigurationDao
				.getSystemConfigurationByType("NVReportColorCodeList");

		String colorlist = jsonlist.get(0).getValue();

		List<String> colors = new Gson().fromJson(colorlist, ArrayList.class);
		return colors;
	}

	private Map<String, Color> getCodecColorMap(List<String[]> dataKPIs, Map<String, Integer> kpiIndexMap) {
		Map<String, Color> codecColorMap = new HashMap<>();
		if (kpiIndexMap != null && kpiIndexMap.containsKey(ReportConstants.VOLTE_CODEC)) {
			Integer codecIndex = kpiIndexMap.get(ReportConstants.VOLTE_CODEC);
			Set<String> codecValues = dataKPIs.stream()
											  .filter(x -> x != null && x.length > codecIndex && !StringUtils.isBlank(
													  x[codecIndex]))
											  .map(x -> x[codecIndex])
											  .collect(Collectors.toSet());
			for (String codecValue : codecValues) {
				Color color = ReportUtil.getRandomColor();
				if(codecColorMap.containsValue(color)){
					color = ReportUtil.getRandomColor();
				}
				codecColorMap.put(codecValue, color);
			}
		}
		return codecColorMap;
	}
	
	@Override
	public HashMap<String, BufferedImage> getBuildingImage(List<Double[]> pinLonLatList,String recipeName,LatLng centroid) {
		logger.info("Inside method  getStationaryImages  for pinLonLatList {}  ",pinLonLatList);
		HashMap<String, BufferedImage> imageMap = new HashMap<>();
			try{
				List<Integer> indexList = new ArrayList<>();
				Corner corner = PointUtils.getViewPortAroundPoint(centroid, 280.0, 100.0);
				ImageCreator imagecreator=null;
				if(corner!=null || !pinLonLatList.isEmpty()){
					int zoomLevel = 0;
					if (corner!=null && corner.getMaxLatitude() != null && corner.getMaxLongitude() != null) {
						zoomLevel = ZoomFinder.findZoom(corner.getMaxLatitude(), corner.getMinLatitude(),
								corner.getMaxLongitude(), corner.getMinLongitude());
					}
					logger.info("Final Corner {}, zoomLevel {}",corner, zoomLevel);
					Map<String, Tile> googleTileMap = getGoogleTileMap(corner, zoomLevel);
					logger.info("googleTileMap min {}",googleTileMap.get(GeometryConstants.GOOGLE_TILE_MIN));
					logger.info("googleTileMap Max {}",googleTileMap.get(GeometryConstants.GOOGLE_TILE_MAX));
					imagecreator = new ImageCreator(googleTileMap.get(GeometryConstants.GOOGLE_TILE_MIN), googleTileMap.get(GeometryConstants.GOOGLE_TILE_MAX), zoomLevel,GoogleMaps.MAP_HYBRID,indexList);
					for (Integer index : indexList) {
						if (pinLonLatList != null) {
							imagecreator.drawPinOnImage(imagecreator.getGooglemaps()[index], pinLonLatList);
						}
					}
					imageMap.put(recipeName, imagecreator.getGooglemaps()!=null?imagecreator.getGooglemaps()[0].getImg():null);
				}else{
					imageMap.put(recipeName, null);
				}
					logger.info("Going to return the map of Size {} , {} ",imageMap.size(),imageMap);
				return imageMap;
			}catch(Exception e){
				logger.error("Exception inside method getDriveImages {} ",Utils.getStackTrace(e));
			}
		return imageMap;
	}

	@Override
	public HashMap<String, String> getClusterBoundaryImage(
			List<StealthGeographyDataWrapper> geographyWiseDataWrapperList) {
		logger.info("Inside method  getSitesLocationImage");
		HashMap<String, BufferedImage> imageMap = new HashMap<>();
		HashMap<String, String> savedImagesMap = new HashMap<>();
		if(Utils.isValidList(geographyWiseDataWrapperList)){
			try{
				List<Integer> indexList = new ArrayList<>();
				indexList.add(1);
				indexList.add(2);
				DriveImageWrapper driveImageWrapper = new DriveImageWrapper();
				List<List<List<List<Double>>>> boundaries = new ArrayList<>();
				for(StealthGeographyDataWrapper geographyDataWrapper : geographyWiseDataWrapperList){
					boundaries.addAll(geographyDataWrapper.getBoundaryData());
				}
				driveImageWrapper.setBoundaries(boundaries);
				Corner corner = findViewPort(driveImageWrapper);
				ImageCreator imagecreator=null;
				if(corner != null){
					int zoomLevel = getZoomLevel(corner);
					logger.info("Final Corner {}, zoomLevel {}",corner, zoomLevel);
					Map<String, Tile> googleTileMap = getGoogleTileMap(corner, zoomLevel);
					logger.info("googleTileMap min {}",googleTileMap.get(GeometryConstants.GOOGLE_TILE_MIN));
					logger.info("googleTileMap Max {}",googleTileMap.get(GeometryConstants.GOOGLE_TILE_MAX));
					imagecreator = new ImageCreator(googleTileMap.get(GeometryConstants.GOOGLE_TILE_MIN), googleTileMap.get(GeometryConstants.GOOGLE_TILE_MAX), zoomLevel,GoogleMaps.MAP_HYBRID,indexList);
					for (StealthGeographyDataWrapper geographyDataWrapper : geographyWiseDataWrapperList) {
						imagecreator.drawBoundary(imagecreator.getGooglemaps(), imagecreator.getTerrainmaps(),
								geographyDataWrapper.getBoundaryData(), geographyDataWrapper.getRsrpPlotColor(),
								indexList, geographyDataWrapper.getDlPlotColor(), geographyDataWrapper.getAvgRSRP(),
								geographyDataWrapper.getAvgDl(), geographyDataWrapper.getLatitude(), geographyDataWrapper.getLongitude());
					}
					imageMap.put("RSRP_IMAGE", imagecreator.getGooglemaps()[ReportConstants.INDEX_ONE].getImg());
					imageMap.put("DL_IMAGE", imagecreator.getGooglemaps()[ReportConstants.INDEX_TWO].getImg());
					String saveImagePath = (ConfigUtils.getString(ReportConstants.IMAGE_PATH_FOR_NV_REPORT) + ReportConstants.STEALTH
							+ ReportConstants.FORWARD_SLASH
							+ ReportUtil.getFormattedDate(new Date(), ReportConstants.DATE_FORMAT_DD_MM_YY_HH_SS)
							+ ReportConstants.FORWARD_SLASH);
					savedImagesMap.putAll(saveDriveImages(imageMap, saveImagePath, false));
				}
				logger.info("Going to return the map of Size {} , {} ",imageMap.size(),imageMap);
				return savedImagesMap;
			}catch(Exception e){
				logger.error("Exception inside method getSitesLocationImage {} ",Utils.getStackTrace(e));
			}
		}
		return savedImagesMap;
	}
}

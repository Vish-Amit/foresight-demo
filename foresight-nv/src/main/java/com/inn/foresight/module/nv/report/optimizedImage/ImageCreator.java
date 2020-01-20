package com.inn.foresight.module.nv.report.optimizedImage;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import javax.ws.rs.ForbiddenException;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xerces.impl.io.MalformedByteSequenceException;

import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.maps.Corner;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.geometry.MensurationUtils;
import com.inn.commons.maps.geometry.gis.GISLine;
import com.inn.commons.maps.tiles.Tile;
import com.inn.commons.maps.tiles.TileUtils;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.utils.enums.NEStatus;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveImageWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.PCIWrapper;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWOMapDataWrapper;
/** The Class ImageCreator. */
public class ImageCreator implements ColorConstants {
	/** The logger. */
	private static final Logger logger = LogManager.getLogger(ImageCreator.class);

	/** The googlemaps. */
	private GoogleMaps[] googlemaps;

	/** The googlemaps. */
	private GoogleMaps[] satellitemaps;
	
	/** The googlemaps. */
	private GoogleMaps[] terrainmaps;

	/** The bounds. */
	private double[] bounds;

	/** The zoom. */
	private int zoom;

	/** The googlemap renderer. */
	private GoogleMapRenderer googlemapRenderer;

	
	public GoogleMaps[] getGooglemaps() {
		return googlemaps;
	}

	public void setGooglemaps(GoogleMaps[] googlemaps) {
		this.googlemaps = googlemaps;
	}

	public GoogleMaps[] getSatellitemaps() {
		return satellitemaps;
	}

	public void setSatellitemaps(GoogleMaps[] satellitemaps) {
		this.satellitemaps = satellitemaps;
	}

	public GoogleMaps[] getTerrainmaps() {
		return terrainmaps;
	}

	public void setTerrainmaps(GoogleMaps[] terrainmaps) {
		this.terrainmaps = terrainmaps;
	}

	/**
	 * Instantiates a new image creator.
	 *
	 * @param googleTileMin the google tile min
	 * @param googleTileMax the google tile max
	 * @param zoom the zoom
	 * @param count the count
	 * @param mapType the map type
	 * @throws IOException 
	 * @throws MalformedByteSequenceException 
	 * @throws ForbiddenException 
	 */
	public ImageCreator(Tile googleTileMin, Tile googleTileMax, int zoom, String mapType,List<Integer> indexList) throws IOException {
		GoogleMaps googlemap = new GoogleMaps(googleTileMin, googleTileMax, zoom, mapType);
		GoogleMaps satelliteMap = new GoogleMaps(googleTileMin, googleTileMax, zoom, GoogleMaps.MAP_SATELLITE);
		GoogleMaps terrainMap = new GoogleMaps(googleTileMin, googleTileMax, zoom, GoogleMaps.MAP_TERRAIN);
		indexList.add(ReportConstants.INDEX_ZER0);  // This is to get the bounds and pixel length always from fixed Zero Index
		logger.info("Total Count value is {} ",indexList.stream().collect(Collectors.summarizingInt(Integer::intValue)).getMax() + ReportConstants.INDEX_ONE);
		googlemaps = new GoogleMaps[indexList.stream().parallel().collect(Collectors.summarizingInt(Integer::intValue)).getMax()+ReportConstants.INDEX_ONE];
		satellitemaps = new GoogleMaps[indexList.stream().parallel().collect(Collectors.summarizingInt(Integer::intValue)).getMax()+ReportConstants.INDEX_ONE];
		terrainmaps= new GoogleMaps[ReportConstants.INDEX_ONE];
		googlemap.getMapImage();
		satelliteMap.getMapImage();
		terrainMap.getMapImage();
		bounds = googlemap.bounds;
		this.zoom = zoom;
		int i=ReportConstants.INDEX_ZER0;
		for (Integer index : indexList) {
			googlemaps[index] = googlemap.getClone();
			satellitemaps[i++]=satelliteMap.getClone();
		}
		terrainmaps[ReportConstants.INDEX_ZER0]=terrainMap.getClone();
		googlemapRenderer = new GoogleMapRenderer();
		logger.info(" Successfully Created image creator ");
	}

	/**
	 * Draw multiple boundaries on image.
	 *
	 * @param googlemaps the google maps
	 * @param terrainmaps 
	 * @param multipleBoundaries the multiple boundaries
	 * @param indexList
	 * @param Color the c
	 */
	public void drawBoundary(GoogleMaps[] googlemaps, GoogleMaps[] terrainmaps, List<List<List<List<Double>>>> multipleBoundaries, Color c, List<Integer> indexList) {
		logger.info("Going to plot multilple boundaries of Size {} , with color {} ",multipleBoundaries!=null?multipleBoundaries.size():null,c);
		try {
			for (Integer index : indexList) {
				if(multipleBoundaries!=null){
					for (List<List<List<Double>>> boundaries : multipleBoundaries) {
						for (List<List<Double>> boundary : boundaries) {
							googlemapRenderer.drawBoundary(googlemaps[index].getImg(), boundary, googlemaps[index].bounds, c, googlemaps[index].pixelLength);
							googlemapRenderer.drawBoundary(terrainmaps[ReportConstants.INDEX_ZER0].getImg(), boundary, terrainmaps[ReportConstants.INDEX_ZER0].bounds, c, terrainmaps[ReportConstants.INDEX_ZER0].pixelLength);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception inside method during plotting of multiple boundary {} ",e.getMessage());
		}
	}

	public void drawBoundary(GoogleMaps[] googlemaps1, GoogleMaps[] terrainmaps1, List<List<List<List<Double>>>> multipleBoundaries, Color rsrpColor, List<Integer> indexList, Color dlColor, Double rsrpValue, Double dlValue, Double latitude, Double longitude) {
		logger.info("Going to plot multilple boundaries of Size {}",multipleBoundaries!=null?multipleBoundaries.size():null);
		try {
			for (Integer index : indexList) {
				Color c = Color.GRAY;
				String fillValue = null;
				if(index == 1) {
					if(rsrpColor != null) {
						c = rsrpColor;
					}
					fillValue = rsrpValue != null ? rsrpValue.toString() : null;
				} else if(index == 2) {
					if(dlColor != null) {
						c = dlColor;
					}
					fillValue = dlValue != null ? dlValue.toString() : null;
				}
				int lxLy[] = calculateLxLy(googlemaps[0].bounds, latitude, longitude, googlemaps[0].pixelLength);
				int lx = lxLy[0];
				int ly = lxLy[1];
				if(multipleBoundaries!=null) {
					for (List<List<List<Double>>> boundaries : multipleBoundaries) {
						for (List<List<Double>> boundary : boundaries) {
							googlemapRenderer.drawBoundary(googlemaps[index].getImg(), boundary, googlemaps[index].bounds, Color.BLACK, googlemaps[index].pixelLength);
							googlemapRenderer.drawBoundary(terrainmaps[ReportConstants.INDEX_ZER0].getImg(), boundary, terrainmaps[ReportConstants.INDEX_ZER0].bounds, Color.BLACK, terrainmaps[ReportConstants.INDEX_ZER0].pixelLength);
							googlemapRenderer.fillBoundary(googlemaps[index].getImg(), boundary, googlemaps[index].bounds, c, googlemaps[index].pixelLength);
							googlemapRenderer.fillBoundary(terrainmaps[ReportConstants.INDEX_ZER0].getImg(), boundary, terrainmaps[ReportConstants.INDEX_ZER0].bounds, c, terrainmaps[ReportConstants.INDEX_ZER0].pixelLength);
						}
					}
					googlemapRenderer.drawTextOnImage(googlemaps[index].getImg(), lx, ly, Color.BLACK, fillValue);
				}
			}
		} catch (Exception e) {
			logger.error("Exception inside method during plotting of multiple boundary {} ",e.getMessage());
		}
	}

	/**
	 * Draw drive path.
	 *
	 * @param googlemaps the googlemaps
	 * @param pciMap the pci map
	 * @param dataKPIs the data KP is
	 * @param indexLatitude the index latitude
	 * @param indexLongitude the index longitude
	 * @param indexPci the index pci
	 * @param kpiWrappers the kpi wrappers
	 * @param servingCellMap
	 * @param pciCountMap the having count and color for each Pci
	 * @param earfcnColorMap
	 * @param servingSystemColorMap
	 * @param dlBandWidthColorMap
	 * @param cgiCountMap
	 * @param codecColorMap
	 * @return the integer
	 */
	public Integer drawDrivePath(GoogleMaps[] googlemaps2, HashMap<Integer, PCIWrapper> pciCountMap,
			DriveImageWrapper driveImageWrapper, Map<String, Color> earfcnColorMap,
			Map<String, Color> servingSystemColorMap, Map<String, Color> dlBandWidthColorMap,
			HashMap<Integer, PCIWrapper> cgiCountMap, Map<String, Integer> kpiIndexMap, List<String> pciColorMap,
			Map<String, Color> codecColorMap) {
	 	kpiIndexMap=checkAndIntilizeIndexMap(kpiIndexMap);
		List<String[]> dataKPIs = driveImageWrapper.getDataKPIs();
		Integer indexLatitude = driveImageWrapper.getIndexLatitude();
		Integer indexLongitude = driveImageWrapper.getIndexLongitude();
		Integer indexPci = driveImageWrapper.getIndexPci();
		List<KPIWrapper> kpiWrappers = driveImageWrapper.getKpiWrappers();
		Integer cgiIndex = ReportUtil.findKPIIndexFromKPIWrapper(driveImageWrapper, DriveHeaderConstants.CGI);
		
		logger.info("Inside method to drawDrivePath ");
		Integer total = 0;
		Integer cgiTotal = 0;
		Integer servingCellTotal = 0;
		Integer handoverTotal =0;
		Integer handoverFailureTotal =0;
		HashMap<Integer, PCIWrapper> handoverCountMap =new HashMap<>();
		HashMap<Integer, PCIWrapper> handoverFailureCountMap =new HashMap<>();
		
		int size = BUBBLE_SIZE;
		for (String[] data : dataKPIs) {
			if (data != null && data.length > 0) {
				Double lat=NVLayer3Utils.getDoubleFromCsv(data, indexLatitude);
				Double lon=NVLayer3Utils.getDoubleFromCsv(data, indexLongitude);
				if (lat != null && lon != null) {
					// Double lon = Double.parseDouble(data[indexLongitude]);
					double[] lon_lat = new double[] { lon, lat };
					if (lon_lat[0] == 0.0 || lon_lat[1] == 0.0) {
						continue;
					}

					int lxLy[] = calculateLxLy(googlemaps[0].bounds, lon_lat[1], lon_lat[0], googlemaps[0].pixelLength);
					int lx = lxLy[0];
					int ly = lxLy[1];
					boolean isPciPlotted = false;
					for (KPIWrapper kpiwrapper : kpiWrappers) {
						
						try {
							if (ReportConstants.PCI_PLOT.equalsIgnoreCase(kpiwrapper.getKpiName())) {
								Integer value = (data[indexPci] != null && !data[indexPci].isEmpty())
										? Integer.parseInt(data[indexPci])
										: null;
								//TODO Changes
//								int imageIndex=DriveHeaderConstants.getKpiIndexMap().get(kpiwrapper.getKpiName()); 
								int imageIndex = kpiwrapper.getIndexKPI();
								if (ReportUtil.isValidKPiPlot(value, kpiwrapper)) {
									total = drawPciBUbble(pciCountMap, googlemaps, dataKPIs, lx, ly, size, total,kpiWrappers, imageIndex,(data[indexPci] != null && !data[indexPci].isEmpty())? Integer.parseInt(data[indexPci]): null, kpiwrapper.getKpiName(), pciColorMap);
									if (kpiIndexMap.containsKey(ReportConstants.HANDOVER_PLOT)
											&& googlemaps.length > kpiIndexMap.get(ReportConstants.HANDOVER_PLOT)
											&& googlemaps[kpiIndexMap.get(ReportConstants.HANDOVER_PLOT)] != null) {
										drawPciBUbble(handoverCountMap, googlemaps, dataKPIs, lx, ly, size,
												handoverTotal, kpiWrappers,
												kpiIndexMap.get(ReportConstants.HANDOVER_PLOT),
												(data[indexPci] != null && !data[indexPci].isEmpty()) ?
														Integer.parseInt(data[indexPci]) :
														null, kpiwrapper.getKpiName(), pciColorMap);
									}
									if(kpiIndexMap.containsKey(ReportConstants.HANDOVER_FAILURE_PLOT)
											&& googlemaps.length > kpiIndexMap.get(ReportConstants.HANDOVER_FAILURE_PLOT)
											&& googlemaps[kpiIndexMap.get(ReportConstants.HANDOVER_FAILURE_PLOT)] != null){
										logger.info("Going to draw pci on handover failure plot image");
										drawPciBUbble(handoverFailureCountMap, googlemaps, dataKPIs, lx, ly, size,
												handoverFailureTotal, kpiWrappers,
												kpiIndexMap.get(ReportConstants.HANDOVER_FAILURE_PLOT),
												(data[indexPci] != null && !data[indexPci].isEmpty()) ?
														Integer.parseInt(data[indexPci]) :
														null, kpiwrapper.getKpiName(), pciColorMap);
									}
								}
							}
		
							else if (ReportConstants.SERVING_CELL.equalsIgnoreCase(kpiwrapper.getKpiName())) {
								Integer value = (data[indexPci] != null && !data[indexPci].isEmpty())
										? Integer.parseInt(data[indexPci])
										: null;
								//TODO Changes
//								int imageIndex=DriveHeaderConstants.getKpiIndexMap().get(kpiwrapper.getKpiName()); 
								int imageIndex = kpiwrapper.getIndexKPI();
								if (ReportUtil.isValidKPiPlot(value, kpiwrapper)) {
									servingCellTotal = drawPciBUbble(pciCountMap, googlemaps, dataKPIs, lx, ly, size, servingCellTotal,kpiWrappers, imageIndex,(data[indexPci] != null && !data[indexPci].isEmpty())? Integer.parseInt(data[indexPci]): null, kpiwrapper.getKpiName(), pciColorMap);
								}
							} 
							
							else if (DriveHeaderConstants.CGI.equalsIgnoreCase(kpiwrapper.getKpiName())) {
								
								if (cgiIndex != null) {
									Integer value = (data[cgiIndex] != null && !data[cgiIndex].isEmpty())
											? Integer.parseInt(data[cgiIndex])
											: null;

									int imageIndex = kpiwrapper.getIndexKPI();
									if (ReportUtil.isValidKPiPlot(value, kpiwrapper)) {
										drawCgiBUbble(cgiCountMap, googlemaps, dataKPIs, lx, ly, size, cgiTotal,
												kpiWrappers, imageIndex,
												(data[cgiIndex] != null && !data[cgiIndex].isEmpty())
														? Integer.parseInt(data[cgiIndex])
														: null);
									}
								}
							} else {
								plotEachLatlonforkpi(kpiwrapper, lx, ly, size, data, earfcnColorMap,servingSystemColorMap,dlBandWidthColorMap,kpiIndexMap);
							}
						} catch (Exception e) {
							logger.error("Exception during plotting of bubble {}  ", Utils.getStackTrace(e));
						}
					}
				}
			}
		}
		drawCustomPlots(googlemaps, dataKPIs, indexLatitude, indexLongitude, kpiWrappers, kpiIndexMap, codecColorMap);
		return total;
	}
	
	/**
	 * Draw pci Bubble.
	 * @param pciMap the pci map
	 * @param pciCountMap the having count and color for each Pci
	 * @param googlemaps the googlemaps
	 * @param dataKPIs the data KP is
	 * @param lx the lx
	 * @param ly the ly
	 * @param size the size
	 * @param total the total
	 * @param kpiWrappers the data for each kpi
	 * @param pciIndex the index position of pci in dataKPIs
	 * @param pciValue
	 * @param kpiName
	 * @return the integer
	 */
	private Integer drawPciBUbble(HashMap<Integer, PCIWrapper> pciCountMap, GoogleMaps[] googlemaps, List<String[]> dataKPIs, int lx, int ly, int size, Integer total,
			List<KPIWrapper> kpiWrappers, int pciIndex, Integer pciValue, String kpiName, List<String> pciColorList) {
		/* for (String[] data : dataKPIs) { if (data != null && data.length > 0) { */
		/* for (String row : data1) { */
		/* String data[] = row.split(ReportConstants.COMMA); */
		try {
			if (pciValue != null) {
				boolean isServingCell = kpiName.equalsIgnoreCase(ReportConstants.SERVING_CELL);
				Color color = TRANSPARENT;
				total++;
				if (pciCountMap.containsKey(pciValue)) {
					PCIWrapper wrapper = pciCountMap.get(pciValue);
					if(!isServingCell) {
						updatePCIWrapper(wrapper);
					}
					color = pciCountMap.get(pciValue).getColor();
				} else {
					String colorvalue=pciColorList.get(pciValue);
					color = Color.decode(colorvalue);
					PCIWrapper wrapper = initializePCIWrapper(pciValue, color, true, isServingCell);
					pciCountMap.put(pciValue, wrapper);
				}
				googlemapRenderer.drawBubble(googlemaps[pciIndex].getImg(), lx, ly, color, size);
			}
		} catch (Exception e) {
			logger.error("Exception inside  drawPciBUbble method ");
		}
		
		
		/* } */
		/* } } */
		return total;
	}
	
	private Integer drawCgiBUbble(HashMap<Integer, PCIWrapper> pciCountMap,
			GoogleMaps[] googlemaps, List<String[]> dataKPIs, int lx, int ly, int size, Integer total,List<KPIWrapper> kpiWrappers, int pciIndex, Integer pciValue) {
		/* for (String[] data : dataKPIs) { if (data != null && data.length > 0) { */
		/* for (String row : data1) { */
		/* String data[] = row.split(ReportConstants.COMMA); */
		try {
			if (pciValue != null) {
				total++;
				Color color = TRANSPARENT;
				if (pciCountMap.containsKey(pciValue)) {
					PCIWrapper wrapper = pciCountMap.get(pciValue);
					updatePCIWrapper(wrapper);
					color = pciCountMap.get(pciValue).getColor();
				} else {
					color = ReportUtil.getRandomColor();
					PCIWrapper wrapper = initializeCgIWrapper(pciValue, color,true);
					pciCountMap.put(pciValue, wrapper);
				}
				googlemapRenderer.drawBubble(googlemaps[pciIndex].getImg(), lx, ly, color, size);
			}
		} catch (Exception e) {
			logger.error("Exception inside  drawPciBUbble method {} ",Utils.getStackTrace(e));
		}
		
		
		/* } */
		/* } } */
		return total;
	}

	/**
	 * Update PCI wrapper.
	 *
	 * @param wrapper the wrapper
	 */
	private void updatePCIWrapper(PCIWrapper wrapper) {
		try {
			Integer signalcount = wrapper.getCount();
			signalcount = signalcount + 1;
			wrapper.setCount(signalcount);
			wrapper.setIsSampleData(true);
		} catch (Exception e) {
			logger.error("Exception inside method updatePCIWrapper {} ",Utils.getStackTrace(e));
		}
	}

	/**
	 * Initialize PCI wrapper.
	 *
	 * @param pci the pci
	 * @param color the color
	 * @param isSampleData
	 * @param isServingCell
	 * @return the PCI wrapper
	 */
	private PCIWrapper initializePCIWrapper(Integer pci, Color color, boolean isSampleData, boolean isServingCell) {
		PCIWrapper wrapper = new PCIWrapper();
		try {
			wrapper.setPCI(pci);
			wrapper.setColor(color);
			wrapper.setCount(isServingCell ? 0 : 1);
			wrapper.setIsSampleData(isSampleData);
		} catch (Exception e) {
			logger.error("Exception inside method  initializePCIWrapper {} ",Utils.getStackTrace(e));
		}
		return wrapper;
	}
	
	private PCIWrapper initializeCgIWrapper(Integer cgi, Color color, boolean isSampleData) {
		PCIWrapper wrapper = new PCIWrapper();
		try {
			wrapper.setPCI(cgi);
			wrapper.setColor(color);
			wrapper.setCount(1);
			wrapper.setIsSampleData(isSampleData);
		} catch (Exception e) {
			logger.error("Exception inside method  initializePCIWrapper {} ",Utils.getStackTrace(e));
		}
		return wrapper;
	}

	/**
	 * Calculate lx ly.
	 *
	 * @param latLonBounds the lat lon bounds
	 * @param lat the lat
	 * @param lon the lon
	 * @param pixelLength the pixel length
	 * @return the int[]
	 */
	private int[] calculateLxLy(double[] latLonBounds, double lat, double lon, double pixelLength) {
		double latOrigin = latLonBounds[0];
		double lonOrigin = latLonBounds[1];
		double latExtent = latLonBounds[2] - latLonBounds[0];
		double lonExtent = latLonBounds[3] - latLonBounds[1];

		double ly1 = (pixelLength * (lat - latOrigin)) / latExtent;
		double lx1 = (pixelLength * (lon - lonOrigin)) / lonExtent;

		int ly = (int) ((int) pixelLength - ly1);
		int lx = (int) lx1 - 1;
		return new int[] { lx, ly };
	}

	/**
	 * Draw sites.
	 *
	 * @param googlemaps the googlemaps
	 * @param terrainmaps 
	 * @param list the site list
	 * @param pciMap the pci map
	 * @param indexList
	 */
	public void drawSites(GoogleMaps[] googlemaps, GoogleMaps[] terrainmaps, List<SiteInformationWrapper> list, HashMap<Integer, PCIWrapper> pciMap, List<Integer> indexList) {
		logger.info(" list Size {} ",list!=null?list.size():null);
		try {
			if (list != null && !list.isEmpty()){
				for (Integer index : indexList) {
					drawSiteData(googlemaps[index].getImg(),googlemaps, list, pciMap,index);
				}
				// draw Site for 1st Index image
				drawSiteData(googlemaps[ReportConstants.INDEX_ZER0].getImg(),googlemaps, Arrays.asList(list.get(ReportConstants.INDEX_ZER0)), pciMap,null);
				drawSiteData(terrainmaps[ReportConstants.INDEX_ZER0].getImg(),terrainmaps, Arrays.asList(list.get(ReportConstants.INDEX_ZER0)), pciMap,null);
			} else {
				logger.info("Sitelist is null, so sites can't be draw");
			}
		} catch (Exception ne) {
			logger.error("Error in drawing Sites {} ",Utils.getStackTrace(ne));
		}
	}

	/**
	 * Draw sites.
	 *
	 * @param image the image
	 * @param siteList the site list
	 * @param pciMap the pci map
	 * @param index 
	 */
	public void drawSiteData(BufferedImage image, GoogleMaps[] googlemaps2,List<SiteInformationWrapper> siteList, HashMap<Integer, PCIWrapper> pciMap, Integer index) {
				String previousSiteName = null;
			for (SiteInformationWrapper wrapper : siteList) {
			try {
				int azimuth = 0;
				double[] lon_lat = getLatLonForSiteList(wrapper);
				if (lon_lat[0] == 0.0 || lon_lat[1] == 0.0) {
					continue;
				}
				int lxLy[] = calculateLxLy(googlemaps2[0].bounds, lon_lat[ReportConstants.INDEX_ONE],
						lon_lat[ReportConstants.INDEX_ZER0], googlemaps2[0].pixelLength);

				int lx = lxLy[0];
				int ly = lxLy[1];

				Color color = getSectorColor(wrapper, pciMap, index);

				if (wrapper.getActualAzimuth() != null) {
					azimuth = wrapper.getActualAzimuth();
				} else {
					azimuth = wrapper.getPlanAzimuth();
				}
				String siteName = null;
				if (previousSiteName == null || !previousSiteName.equalsIgnoreCase(wrapper.getSiteName())) {
					siteName = wrapper.getSiteName();
				}
				googlemapRenderer.renderSiteData(image, lx, ly, color, azimuth, SITE_SIZE, SITE_FANWIDTH,
						SITE_INNERCIRCLE, SITE_OUTERCIRCLE, siteName);
				previousSiteName = wrapper.getSiteName();
			
			} catch (Exception e) {
				logger.warn("unable to plot index  {} with site data, {} ",index,wrapper,Utils.getStackTrace(e));
				}
			}
	}

	private Color getSectorColor(SiteInformationWrapper wrapper, HashMap<Integer, PCIWrapper> pciMap,Integer index) {
		if(index!=null && DriveHeaderConstants.SERVING_CELL_INDEX.equals(index)){
			if(pciMap.get(wrapper.getPci())!=null && pciMap.get(wrapper.getPci()).getIsSampleData()) {
				return pciMap.get(wrapper.getPci()).getColor();
			}
			return Color.BLACK;
		} else if(wrapper.isSiteAvailable() != null && !wrapper.isSiteAvailable() && wrapper.getNeStatus() != null && wrapper.getNeStatus().equalsIgnoreCase(
				NEStatus.ONAIR.name())){
			return  Color.BLACK;
		} else if(wrapper.getNeStatus() != null && wrapper.getNeStatus().equalsIgnoreCase(
				NEStatus.PLANNED.name())){
			return Color.RED;
		}
		else{
			return pciMap.get(wrapper.getPci())!=null?pciMap.get(wrapper.getPci()).getColor():Color.RED;
		}
	}

	/**
	 * Gets the lat lon for site list.
	 *
	 * @param wrapper the site
	 * @return the lat lon for site list
	 */
	private double[] getLatLonForSiteList(SiteInformationWrapper wrapper) {
		double[] lon_lat = new double[2];
		try {
			lon_lat[1] = wrapper.getLat();
			lon_lat[0] = wrapper.getLon();
		} catch (ClassCastException e) {
			logger.debug("Inside method getLatLonForSiteList {} ",e.getMessage());
		} catch (Exception ne) {
			Utils.getStackTrace(ne);
		}
		return lon_lat;
	}

	/**
	 * Draw holes.
	 *
	 * @param image the image
	 * @param lat the lat
	 * @param lon the lon
	 * @param diameter the diameter
	 * @param sitecolor the sitecolor
	 * @param name the name
	 */
	public void drawHoles(BufferedImage image, double lat, double lon, int diameter, Color sitecolor, String name) {
		int[] lxLy = calculateLxLy(googlemaps[0].bounds, lat, lon, googlemaps[0].pixelLength);
		int lx = lxLy[0];
		int ly = lxLy[1];
		googlemapRenderer.drawHoles(image, lx, ly, diameter, sitecolor, name);
	}

	public void drawBoundaryOnAnImage(GoogleMaps satelliteMap, List<List<List<List<Double>>>> multipleBoundaries, Color color) {
		try {
			if (multipleBoundaries != null) {
				for (List<List<List<Double>>> boundaries : multipleBoundaries) {
					for (List<List<Double>> boundary : boundaries) {
						googlemapRenderer.drawBoundary(satelliteMap.getImg(), boundary, satelliteMap.bounds, color,
								satelliteMap.pixelLength);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exeption inside method drawBoundaryOnAnImage {} ", Utils.getStackTrace(e));
		}
	}

	public void drawPathOnAnImage(GoogleMaps satellitemap,
			List<String[]> dataKPIs, int indexLatitude, int indexLongitude, 
			KPIWrapper kpiWrapper,Color color, Map<String, Integer> kpiIndexMap) {
		logger.info("inside method  drawPathOnAnImage with color {} ",color);
		try {
			if(dataKPIs!=null ){
			for (String[] data : dataKPIs) {
				if (data != null && data.length > indexLatitude && data.length > indexLongitude 
						&& StringUtils.isNotBlank(data[indexLatitude])) {
					Double lat = Double.parseDouble(data[indexLatitude]);
					Double lon = Double.parseDouble(data[indexLongitude]);
					double[] lon_lat = new double[] { lon, lat };
					if (lon_lat[0] == 0.0 || lon_lat[1] == 0.0) {
						continue;
					}

					int[] lxLy = calculateLxLy(satellitemap.bounds, lon_lat[1], lon_lat[0],
							satellitemap.pixelLength);
					int lx = lxLy[0];
					int ly = lxLy[1];
					Double value =null;
					if(color==null){

						value = (data[kpiIndexMap.get(RSRP)] != null && !data[kpiIndexMap.get(RSRP)].isEmpty()) ? Double.parseDouble(data[kpiIndexMap.get(RSRP)]) : null;
					}
					googlemapRenderer.drawBubble(satellitemap.getImg(), lx, ly,(color==null)?ColorFinder.getDriveColor(kpiWrapper.getRangeSlabs(), value):color, BUBBLE_SIZE);
				}
			}
		   }
		} catch (NumberFormatException e) {
			logger.error("NumberFormatException inside method  drawPathOnAnImage {} ",Utils.getStackTrace(e));
		}catch(Exception e){
			logger.debug("Exception inside method  drawPathOnAnImage {} ",Utils.getStackTrace(e));
		}
	}

	public void drawPinOnImage(GoogleMaps satelliteMap, List<Double[]> pinLonLatList) {
		try {
			logger.info("Going to draw pin on image for pin lat lon {} , {} ",pinLonLatList.get(0)[0],pinLonLatList.get(0)[1]);
			for (Double[] lonLat : pinLonLatList) {
				if (lonLat[0] == null || lonLat[1] == null || lonLat[0] == 0.0 || lonLat[1] == 0.0) {
					continue;
				} else {
					int[] lxLy = calculateLxLy(satelliteMap.bounds, lonLat[ReportConstants.INDEX_ONE],
							lonLat[ReportConstants.INDEX_ZER0], satelliteMap.pixelLength);
					googlemapRenderer.pastePinOnImage(satelliteMap.getImg(), lxLy[ReportConstants.INDEX_ZER0],lxLy[ReportConstants.INDEX_ONE],ConfigUtils.getString(ReportConstants.PIN_IMAGE_PATH));
				}
			}
		} catch (Exception e) {
			logger.error("Exception inside method drawPinOnImage {} ",e.getMessage());
		}
	}

	public void drawStealthDevicesOnImage(GoogleMaps satelliteMap, List<StealthWOMapDataWrapper> stealthMapDataWrapper) {
		try {
			logger.info("Going to draw pin on image for pin lat lon {} , {} ",stealthMapDataWrapper.get(0).getLatitude(),stealthMapDataWrapper.get(0).getLongitude());
			for (StealthWOMapDataWrapper data : stealthMapDataWrapper) {
				if (data.getLatitude() == null || data.getLongitude() == null || data.getLatitude() == 0.0 || data.getLongitude() == 0.0) {
					continue;
				} else {
					int[] lxLy = calculateLxLy(satelliteMap.bounds, data.getLatitude(),
							data.getLongitude(), satelliteMap.pixelLength);
					googlemapRenderer.pastePinOnImage(satelliteMap.getImg(), lxLy[ReportConstants.INDEX_ZER0],
							lxLy[ReportConstants.INDEX_ONE], data.getScoreImagePath());
				}
			}
		} catch (Exception e) {
			logger.error("Error while drawing Stealth Devices on map {}", Utils.getStackTrace(e));
		}
	}

	private void plotEachLatlonforkpi(KPIWrapper kpiwrapper, int lx, int ly, int size, String[] data, Map<String, Color> earfcnColorMap,Map<String, Color> servingSystemColorMap, Map<String, Color> dlBandWidthColorMap, Map<String, Integer> kpiIndexMap) {
		/* int index = 0; */
		if (ReportConstants.ROUTE.equalsIgnoreCase(kpiwrapper.getKpiName())) {
			addRouteIntoImageCreator(kpiwrapper, lx, ly, size);
		}else if(ReportUtil.isTestTypeCheckRequired(kpiwrapper.getKpiName())){
			addTestTypeWisePlotIntoImageCreator(kpiwrapper, lx, ly, size, data,kpiIndexMap);
		}
		else if (ReportConstants.MIMO.equalsIgnoreCase(kpiwrapper.getKpiName())) {
			addMIMOPlotIntoImageCreator(kpiwrapper, lx, ly, size, data,kpiIndexMap);
		} else if (DriveHeaderConstants.TECHNOLOGY.equalsIgnoreCase(kpiwrapper.getKpiName())) {
			addTechnologyPlotIntoImageCreator(kpiwrapper, lx, ly, size, data,kpiIndexMap);
		}
		else if (ReportConstants.DL_EARFCN.equalsIgnoreCase(kpiwrapper.getKpiName())) {
			if(earfcnColorMap!=null && !earfcnColorMap.isEmpty()){
			addDLEarfcnIntoImageCreator(kpiwrapper, lx, ly, size, data, earfcnColorMap,kpiIndexMap);
			}
		}
		///////////////////Three Newly Plots Added (Serving System /Carrier Aggregation / MOS)
		else if (ReportConstants.MOS.equalsIgnoreCase(kpiwrapper.getKpiName())) {
			addMosPlotIntoImageCreator(kpiwrapper, lx, ly, size, data,kpiIndexMap);
		} else if (ReportConstants.CA.equalsIgnoreCase(kpiwrapper.getKpiName())) {
			addCAPlotIntoImageCreator(kpiwrapper, lx, ly, size, data,kpiIndexMap);
		} else if (ReportConstants.SERVING_SYSTEM.equalsIgnoreCase(kpiwrapper.getKpiName())) {
			if(servingSystemColorMap!=null && !servingSystemColorMap.isEmpty()){
			addServingSystemPlotIntoImageCreator(kpiwrapper, lx, ly, size, data, servingSystemColorMap,kpiIndexMap);
			}
		} else if (ReportConstants.DL_BANWIDTH.equalsIgnoreCase(kpiwrapper.getKpiName())) {
			if(dlBandWidthColorMap!=null && !dlBandWidthColorMap.isEmpty()){
				adddlBnadWidthPlotIntoImageCreator(kpiwrapper, lx, ly, size, data, dlBandWidthColorMap,kpiIndexMap);
				}
		} else if (ReportConstants.RSRP.equalsIgnoreCase(kpiwrapper.getKpiName())){
			//				|| ReportConstants.RESELECTION_PLOT.equalsIgnoreCase(kpiwrapper.getKpiName())) {
			int index = kpiwrapper.getIndexKPI();
			Double value = NVLayer3Utils.getDoubleFromCsv(data, index);
			if(ReportUtil.isValidKPiPlot(value,kpiwrapper)){
				if(!kpiwrapper.isValidPlot()){kpiwrapper.setValidPlot(true);}
				googlemapRenderer.drawBubble(googlemaps[index].getImg(), lx, ly,
						ColorFinder.getDriveColor(kpiwrapper.getRangeSlabs(), value), size);
				if(kpiIndexMap.containsKey(ReportConstants.ERAB_PLOT)){
					int indexErab = kpiIndexMap.get(ReportConstants.ERAB_PLOT);
					if(googlemaps.length > indexErab && googlemaps[indexErab] != null){
						googlemapRenderer.drawBubble(googlemaps[indexErab].getImg(), lx, ly,
								ColorFinder.getDriveColor(kpiwrapper.getRangeSlabs(), value), size);
					}
				}
				if(kpiIndexMap.containsKey(ReportConstants.CALL_SETUP_PLOT)){
					int indexCallSetup = kpiIndexMap.get(ReportConstants.CALL_SETUP_PLOT);
					if(googlemaps.length > indexCallSetup && googlemaps[indexCallSetup] != null){
						googlemapRenderer.drawBubble(googlemaps[indexCallSetup].getImg(), lx, ly,
								ColorFinder.getDriveColor(kpiwrapper.getRangeSlabs(), value), size);
					}
				}

				if(kpiIndexMap.containsKey(ReportConstants.CALL_PLOT)){
					int indexCallSetup = kpiIndexMap.get(ReportConstants.CALL_PLOT);
					if(googlemaps.length > indexCallSetup && googlemaps[indexCallSetup] != null){
						googlemapRenderer.drawBubble(googlemaps[indexCallSetup].getImg(), lx, ly,
								ColorFinder.getDriveColor(kpiwrapper.getRangeSlabs(), value), size);
					}
				}
			}
		} else if (!kpiIndexMap.containsKey(ReportConstants.PCI_PLOT) && (
				ReportConstants.HANDOVER_PLOT.equalsIgnoreCase(kpiwrapper.getKpiName())
						|| ReportConstants.HANDOVER_FAILURE_PLOT.equalsIgnoreCase(kpiwrapper.getKpiName()))) {
			int index = kpiwrapper.getIndexKPI();
			if (googlemaps.length > index && googlemaps[index] != null) {
				googlemapRenderer.drawBubble(googlemaps[index].getImg(), lx, ly, Color.GRAY, size);
			}
		}
		else if (ReportConstants.ERAB_PLOT.equalsIgnoreCase(kpiwrapper.getKpiName())
				|| ReportConstants.CALL_SETUP_PLOT.equalsIgnoreCase(kpiwrapper.getKpiName())
				|| ReportConstants.VOLTE_CODEC.equalsIgnoreCase(kpiwrapper.getKpiName())
				|| ReportConstants.CALL_PLOT.equalsIgnoreCase(kpiwrapper.getKpiName())
				|| ReportConstants.DL_MODULATION_TYPE.equalsIgnoreCase(kpiwrapper.getKpiName())
				|| ReportConstants.HANDOVER_PLOT.equalsIgnoreCase(kpiwrapper.getKpiName())
				|| ReportConstants.HANDOVER_FAILURE_PLOT.equalsIgnoreCase(kpiwrapper.getKpiName())){
			return;
		} else if(kpiwrapper != null && kpiwrapper.getIndexKPI() != null){
			int index = kpiwrapper.getIndexKPI();
			Double value = NVLayer3Utils.getDoubleFromCsv(data, index);
			if(ReportUtil.isValidKPiPlot(value,kpiwrapper)){
				if(!kpiwrapper.isValidPlot()){kpiwrapper.setValidPlot(true);}
				googlemapRenderer.drawBubble(googlemaps[index].getImg(), lx, ly,
						ColorFinder.getDriveColor(kpiwrapper.getRangeSlabs(), value), size);
			}
		}
	}

	private void addTestTypeWisePlotIntoImageCreator(KPIWrapper kpiwrapper, int lx, int ly, int size, String[] data, Map<String, Integer> kpiIndexMap) {
		int index;
		String testType = ReportUtil.getTestTypeValueOnBasisOfKPi(kpiwrapper.getKpiName());
		if(kpiIndexMap!=null && testType!=null && testType.equalsIgnoreCase(NVLayer3Utils.getStringFromCsv(data,kpiIndexMap.get(ReportConstants.TEST_TYPE)))){
			index = kpiwrapper.getIndexKPI();
//			int dataIndex = ReportUtil.getDataIndexByKPiName(kpiwrapper.getKpiName());
			Double value = NVLayer3Utils.getDoubleFromCsv(data, index);
			if(ReportUtil.isValidKPiPlot(value,kpiwrapper)){
				if(!kpiwrapper.isValidPlot()){kpiwrapper.setValidPlot(true);}
				Color color = ColorFinder.getDriveColor(kpiwrapper.getRangeSlabs(), value);
				googlemapRenderer.drawBubble(googlemaps[index].getImg(), lx, ly,color, size);
			}
		}
	}

	private void addRouteIntoImageCreator(KPIWrapper kpiwrapper, int lx, int ly, int size) {
		int index;
		index = kpiwrapper.getIndexKPI();
		Double value=0.0;
		if(ReportUtil.isValidKPiPlot(value,kpiwrapper)){
			if(!kpiwrapper.isValidPlot()){kpiwrapper.setValidPlot(true);}
			googlemapRenderer.drawBubble(googlemaps[index].getImg(), lx, ly,Color.blue, size);
		}
	}

	private void addServingSystemPlotIntoImageCreator(KPIWrapper kpiwrapper, int lx, int ly, int size, String[] data,
			Map<String, Color> servingSystemColorMap, Map<String, Integer> kpiIndexMap) {
		int index;
		index = kpiwrapper.getIndexKPI();
		if (data[kpiIndexMap.get(DriveHeaderConstants.TECHNOLOGY)]!= null && !data[kpiIndexMap.get(DriveHeaderConstants.TECHNOLOGY)].isEmpty()
				&& data[kpiIndexMap.get(ReportConstants.BAND)]!= null && !data[kpiIndexMap.get(ReportConstants.BAND)].isEmpty()
				&& data[kpiIndexMap.get(ReportConstants.NETWORK_TYPE)]!= null && !data[kpiIndexMap.get(ReportConstants.NETWORK_TYPE)].isEmpty()) {
			kpiwrapper.setValidPlot(true);
			googlemapRenderer.drawBubble(googlemaps[index].getImg(), lx, ly,
					servingSystemColorMap.get(data[kpiIndexMap.get(ReportConstants.NETWORK_TYPE)]+ReportConstants.UNDERSCORE+data[kpiIndexMap.get(DriveHeaderConstants.TECHNOLOGY)]+ReportConstants.UNDERSCORE+data[kpiIndexMap.get(ReportConstants.BAND)]), size);
		}
	}
	
		private void adddlBnadWidthPlotIntoImageCreator(KPIWrapper kpiwrapper, int lx, int ly, int size, String[] data,
			Map<String, Color> dlBandWidthColorMap, Map<String, Integer> kpiIndexMap) {
		int index;
		index = kpiwrapper.getIndexKPI();
		if (data[kpiIndexMap.get(ReportConstants.DL_BANWIDTH)]!= null && !data[kpiIndexMap.get(ReportConstants.DL_BANWIDTH)].isEmpty()) {
			kpiwrapper.setValidPlot(true);
			Color color = dlBandWidthColorMap.get(data[kpiIndexMap.get(ReportConstants.DL_BANWIDTH)]);
			color= color!=null?color:Color.BLACK;
			googlemapRenderer.drawBubble(googlemaps[index].getImg(), lx, ly,color, size);
		}
	}

	private void addCAPlotIntoImageCreator(KPIWrapper kpiwrapper, int lx, int ly, int size, String[] data, Map<String, Integer> kpiIndexMap) {
		int index;
		index = kpiwrapper.getIndexKPI();
		if (NVLayer3Utils.getStringFromCsv(data, kpiIndexMap.get(ReportConstants.CA))!=null) {
			kpiwrapper.setValidPlot(true);
			googlemapRenderer.drawBubble(googlemaps[index].getImg(), lx, ly,
					ColorFinder.getCarrierAggregationColor(data[index]), size);
		}
	}

	private void addMosPlotIntoImageCreator(KPIWrapper kpiwrapper, int lx, int ly, int size, String[] data, Map<String, Integer> kpiIndexMap) {
			int index;
			index = kpiwrapper.getIndexKPI();
			Double value = NVLayer3Utils.getDoubleFromCsv(data, kpiIndexMap.get(ReportConstants.MOS));
			Color color = ColorFinder.getDriveColor(kpiwrapper.getRangeSlabs(), value);
			if (!Color.GRAY.equals(color)) {
				size = ReportConstants.TWELVE;
				kpiwrapper.setValidPlot(true);
			}
			googlemapRenderer.drawBubble(googlemaps[index].getImg(), lx, ly, color, size);
	}

	private void addMIMOPlotIntoImageCreator(KPIWrapper kpiwrapper, int lx, int ly, int size, String[] data, Map<String, Integer> kpiIndexMap) {
		int index;
		index = kpiwrapper.getIndexKPI();
		Double value = null;
		String mimoData = data[index];
		if(!StringUtils.isBlank(mimoData) && mimoData.contains("rank")){
			mimoData = mimoData.replace("rank ", Symbol.EMPTY_STRING);
			if(NumberUtils.isCreatable(mimoData)){
				value = Double.parseDouble(mimoData);
			}
		} else {
			value = NVLayer3Utils.getDoubleFromCsv(data, kpiIndexMap.get(ReportConstants.RI));
		}
		if (ReportUtil.isValidKPiPlot(value, kpiwrapper)) {
			if (!kpiwrapper.isValidPlot()) {
				kpiwrapper.setValidPlot(true);
			}
			if (value != null) {
				Color color = ColorFinder.getMimoColor(value.intValue());
				googlemapRenderer.drawBubble(googlemaps[index].getImg(), lx, ly, color, size);
			}
		}
	}

	private void addDLEarfcnIntoImageCreator(KPIWrapper kpiwrapper, int lx, int ly, int size, String[] data,
			Map<String, Color> earfcnColorMap, Map<String, Integer> kpiIndexMap) {
		int index;
		index = kpiwrapper.getIndexKPI();
		Double value = NVLayer3Utils.getDoubleFromCsv(data, kpiIndexMap.get(ReportConstants.EARFCN));
		if(ReportUtil.isValidKPiPlot(value,kpiwrapper)){
			if(!kpiwrapper.isValidPlot()){kpiwrapper.setValidPlot(true);}
			Color color =earfcnColorMap.get(data[index]);
			if(color==null){
				color = ReportUtil.getRandomColor();
				logger.info("Random Color {} ",color);
				earfcnColorMap.put(data[index], color);
			}
			googlemapRenderer.drawBubble(googlemaps[index].getImg(), lx, ly,color, size);
		}
	}

	private void addCallPlotIntoImageCreator(KPIWrapper kpiwrapper, int lx, int ly, String[] data, Map<String, Integer> kpiIndexMap) {
		int index = kpiwrapper.getIndexKPI();
		boolean var =false;
		var = addCallInitiatePlotIntoImageCreator(kpiwrapper, lx, ly, data, index, var, kpiIndexMap);
		var = addCallDropIntoImageCreator(lx, ly, data, index, var, kpiIndexMap);
		var = addCallFailureIntoImageCreator(lx, ly, data, index, var, kpiIndexMap);
		var = addCallSuccessIntoImageCreator(lx, ly, data, index, var, kpiIndexMap);
	}
	
	private void addHandOverPlotIntoImageCreator(KPIWrapper kpiwrapper, int lx, int ly, String[] data, Map<String, Integer> kpiIndexMap, boolean isFailurePlot) {
		int index = kpiwrapper.getIndexKPI();
		boolean var =false;
		if(!isFailurePlot) {
			var = addHandoverInitiatePlotIntoImageCreator(kpiwrapper, lx, ly, data, index, var, kpiIndexMap);
			var = addHandoverSuccessIntoImageCreator(lx, ly, data, index, var, kpiIndexMap);
		} else{
			kpiwrapper.setValidPlot(true);
		}
		var = addHandoverFailureIntoImageCreator(lx, ly, data, index, var, kpiIndexMap);
	}

	private void addReselectionPlotIntoImageCreator(KPIWrapper kpiwrapper, int lx, int ly, String[] data, Map<String, Integer> kpiIndexMap) {
		int index = kpiwrapper.getIndexKPI();
		addReselectionSuccessIntoImageCreator(lx, ly, data, index, kpiIndexMap);
	}

	private void addErabPlotIntoImageCreator(KPIWrapper kpiwrapper, int lx, int ly, String[] data, Map<String, Integer> kpiIndexMap) {
		int index = kpiwrapper.getIndexKPI();
		boolean var =false;
		var = addErabDropPlotIntoImageCreator(kpiwrapper, lx, ly, data, index, var, kpiIndexMap);
//		var = addErabSuccessIntoImageCreator(lx, ly, data, index, var, kpiIndexMap);
	}

	private void addCallSetupPlotIntoImageCreator(KPIWrapper kpiwrapper, int lx, int ly, String[] data, Map<String, Integer> kpiIndexMap) {
		int index = kpiwrapper.getIndexKPI();
		boolean var =false;
		var = addCallInitiatePlotIntoImageCreator(kpiwrapper, lx, ly, data, index, var, kpiIndexMap);
		var = addCallSetupSuccessIntoImageCreator(lx, ly, data, index, var, kpiIndexMap);
	}

	private void addCodecPlotIntoImageCreator(KPIWrapper kpiwrapper, int lx, int ly, String[] data,
			Map<String, Integer> kpiIndexMap, Map<String, Color> codecColorMap) {
		int index = kpiwrapper.getIndexKPI();
		boolean var =false;
		var = addVolteCodecPlotIntoImageCreator(kpiwrapper, lx, ly, data, index, var, kpiIndexMap, codecColorMap);
	}

	private void addDlModulationTypePlotIntoImageCreator(KPIWrapper kpiwrapper, int lx, int ly, String[] data, Map<String, Integer> kpiIndexMap) {
		int index = kpiwrapper.getIndexKPI();
		boolean var =false;
		var = addDlModulationTypeIntoImageCreator(kpiwrapper, lx, ly, data, index, var, kpiIndexMap);
	}

	private boolean addErabDropPlotIntoImageCreator(KPIWrapper kpiwrapper, int lx, int ly, String[] data,
			int index, boolean var, Map<String, Integer> kpiIndexMap) {
		Integer erabDropIndex = kpiIndexMap.get(RRC_DROPPED);
		if (erabDropIndex != null) {
			String erabRequest = NVLayer3Utils.getStringFromCsv(data, erabDropIndex);
			if (erabRequest != null && !ReportConstants.ZERO.equals(erabRequest)) {
				kpiwrapper.setValidPlot(true);
				googlemapRenderer.drawBubble(googlemaps[index].getImg(), lx, ly, Color.RED, ReportConstants.TWENTY_FIVE);
				var = true;
			}
		}
		return var;
	}

	private boolean addErabSuccessIntoImageCreator(int lx, int ly, String[] data, int index, boolean var, Map<String, Integer> kpiIndexMap) {
		Integer erabSuccessIndex =  kpiIndexMap.get(ERAB_SUCCESS);
		if(erabSuccessIndex != null) {
			String erabSuccess = NVLayer3Utils.getStringFromCsv(data,erabSuccessIndex);
			if(erabSuccess != null && !ReportConstants.ZERO.equals(erabSuccess)){
				googlemapRenderer.drawBubble(googlemaps[index].getImg(), lx, ly,Color.GREEN, ReportConstants.TWENTY);
				var=true;
			}
		}
		return var;
	}

	private boolean addHandoverInitiatePlotIntoImageCreator(KPIWrapper kpiwrapper, int lx, int ly, String[] data,
			int index, boolean var, Map<String, Integer> kpiIndexMap) {
		Integer handoverInitiateIndex = kpiIndexMap.get(HANDOVER_INITIATE);
		if (handoverInitiateIndex != null) {
			String handoverInitiate = NVLayer3Utils.getStringFromCsv(data, handoverInitiateIndex);
			if (handoverInitiate != null && !ReportConstants.ZERO.equals(handoverInitiate)) {
				kpiwrapper.setValidPlot(true);
				googlemapRenderer.drawBubble(googlemaps[index].getImg(), lx, ly, Color.BLUE, ReportConstants.TWENTY_FIVE);
				var = true;
			}
		}
		return var;
	}

	private boolean addHandoverFailureIntoImageCreator(int lx, int ly, String[] data, int index, boolean var, Map<String, Integer> kpiIndexMap) {
		Integer handoverFailureIndex = kpiIndexMap.get(HANDOVER_FAILURE);
		if (handoverFailureIndex != null) {
			String handoverFailure = NVLayer3Utils.getStringFromCsv(data, handoverFailureIndex);
			if (handoverFailure != null && !ReportConstants.ZERO.equals(handoverFailure)) {
				googlemapRenderer.drawBubble(googlemaps[index].getImg(), lx, ly, Color.RED, 18);
				var = true;
			} 
		}
		return var;
	}

	private boolean addHandoverSuccessIntoImageCreator(int lx, int ly, String[] data, int index, boolean var, Map<String, Integer> kpiIndexMap) {
		Integer handoverSuccessIndex =  kpiIndexMap.get(HANDOVER_SUCCESS);
		if(handoverSuccessIndex != null) {
			String handoverSuccess = NVLayer3Utils.getStringFromCsv(data,handoverSuccessIndex);
			
			if(handoverSuccess != null && !ReportConstants.ZERO.equals(handoverSuccess)){
				googlemapRenderer.drawBubble(googlemaps[index].getImg(), lx, ly,Color.GREEN, 18);
				var=true;
			}			
		}
		return var;
	}

	private void addReselectionSuccessIntoImageCreator(int lx, int ly, String[] data, int index, Map<String, Integer> kpiIndexMap) {
		Integer reselectionSuccessIndex = kpiIndexMap.get(DriveHeaderConstants.RESELECTION_SUCCESS);
		try {
			if(reselectionSuccessIndex != null) {
				String reselectionSuccess = NVLayer3Utils.getStringFromCsv(data,reselectionSuccessIndex );
				if(!StringUtils.isBlank(reselectionSuccess)){
					googlemapRenderer.drawBubble(googlemaps[index].getImg(), lx, ly,Color.GREEN, ReportConstants.TWENTY);
				}
			}
		} catch (Exception e) {
			logger.error("Exception inside add reselcetion for index: "+ reselectionSuccessIndex + Utils.getStackTrace(e));
		}
	}

	private boolean addCallSuccessIntoImageCreator(int lx, int ly, String[] data, int index, boolean var, Map<String, Integer> kpiIndexMap) {
		Integer callSuccessIndex = kpiIndexMap.get(CALL_SUCCESS);
		if (callSuccessIndex != null) {
			String callSuccess = NVLayer3Utils.getStringFromCsv(data, callSuccessIndex);
			if (callSuccess != null && !ReportConstants.ZERO.equals(callSuccess)) {
				googlemapRenderer.drawBubble(googlemaps[index].getImg(), lx, ly, Color.green,
						ReportConstants.INDEX_EIGHTEEN);
				var = true;
			} 
		}
		return var;
	}

	private boolean addCallSetupSuccessIntoImageCreator(int lx, int ly, String[] data, int index, boolean var, Map<String, Integer> kpiIndexMap) {
		Integer callSuccessIndex = kpiIndexMap.get(CALL_SETUP_SUCCESS);
		if (callSuccessIndex != null) {
			String callSuccess = NVLayer3Utils.getStringFromCsv(data, callSuccessIndex);
			if (callSuccess != null && !ReportConstants.ZERO.equals(callSuccess)) {
				googlemapRenderer.drawBubble(googlemaps[index].getImg(), lx, ly, Color.green,
						ReportConstants.INDEX_EIGHTEEN);
				var = true;
			}
		}
		return var;
	}

	private boolean addCallFailureIntoImageCreator(int lx, int ly, String[] data, int index, boolean var, Map<String, Integer> kpiIndexMap) {
		Integer callFailureIndex = kpiIndexMap.get(CALL_FAILURE);
		if (callFailureIndex != null) {
			String callFailure = NVLayer3Utils.getStringFromCsv(data, callFailureIndex);
			if (callFailure != null && !ReportConstants.ZERO.equals(callFailure)) {
				googlemapRenderer.drawBubble(googlemaps[index].getImg(), lx, ly, Color.ORANGE,
						ReportConstants.INDEX_EIGHTEEN);
				var = true;
			}
		}
		return var;
	}

	private boolean addCallDropIntoImageCreator(int lx, int ly, String[] data, int index, boolean var, Map<String, Integer> kpiIndexMap) {
		Integer callDropIndex = kpiIndexMap.get(CALL_DROP);
		if (callDropIndex != null) {
			String callDrop = NVLayer3Utils.getStringFromCsv(data, callDropIndex);
			if (callDrop != null && !ReportConstants.ZERO.equals(callDrop)) {
				googlemapRenderer.drawBubble(googlemaps[index].getImg(), lx, ly, Color.RED, ReportConstants.INDEX_EIGHTEEN);
				var = true;
			} 
		}
		return var;
	}

	private boolean addCallInitiatePlotIntoImageCreator(KPIWrapper kpiwrapper, int lx, int ly, String[] data, int index,
			boolean var, Map<String, Integer> kpiIndexMap) {
		Integer callInitiateIndex = kpiIndexMap.get(CALL_INITIATE);
		if (callInitiateIndex != null) {
			String callInitiate = NVLayer3Utils.getStringFromCsv(data, callInitiateIndex);
			if (callInitiate != null && !ReportConstants.ZERO.equals(callInitiate)) {
				kpiwrapper.setValidPlot(true);
				googlemapRenderer.drawBubble(googlemaps[index].getImg(), lx, ly, Color.BLUE,
						ReportConstants.TWENTY_FIVE);
				var = true;
			} 
		}
		return var;
	}

	private boolean addVolteCodecPlotIntoImageCreator(KPIWrapper kpiwrapper, int lx, int ly, String[] data, int index,
			boolean var, Map<String, Integer> kpiIndexMap, Map<String, Color> codecColorMap) {
		Integer codecIndex = kpiIndexMap.get(VOLTE_CODEC);
		if (codecIndex != null) {
			String volteCodec = NVLayer3Utils.getStringFromCsv(data, codecIndex);
			if (!StringUtils.isBlank(volteCodec)) {
				kpiwrapper.setValidPlot(true);
				Color color = Color.GRAY;
				if(codecColorMap != null && codecColorMap.containsKey(volteCodec)){
					color = codecColorMap.get(volteCodec);
				}
				googlemapRenderer.drawBubble(googlemaps[index].getImg(), lx, ly, color, BUBBLE_SIZE);
				var = true;
			}
		}
		return var;
	}

	private boolean addDlModulationTypeIntoImageCreator(KPIWrapper kpiwrapper, int lx, int ly, String[] data, int index,
			boolean var, Map<String, Integer> kpiIndexMap) {
		Integer modulationIndex = kpiIndexMap.get(DL_MODULATION_TYPE);
		if (modulationIndex != null) {
			String dlModulationType = NVLayer3Utils.getStringFromCsv(data, modulationIndex);
			if (!StringUtils.isBlank(dlModulationType)) {
				kpiwrapper.setValidPlot(true);
				Color color = Color.GRAY;
				if (dlModulationType.equalsIgnoreCase(ReportConstants.DL_MODULATION_QPSK)) {
					color = Color.decode("#FC8056");
				} else if(dlModulationType.equalsIgnoreCase(ReportConstants.DL_MODULATION_16_QAM)){
					color = Color.decode("#FA5656");
				} else if(dlModulationType.equalsIgnoreCase(ReportConstants.DL_MODULATION_64_QAM)){
					color = Color.decode("#58BFFB");
				} else if(dlModulationType.equalsIgnoreCase(ReportConstants.DL_MODULATION_256_QAM)){
					color = Color.decode("#20BF55");
				}
				googlemapRenderer.drawBubble(googlemaps[index].getImg(), lx, ly, color, BUBBLE_SIZE);
				var = true;
			}
		}
		return var;
	}
	
	public void drawCustomPlots(GoogleMaps[] googlemaps, List<String[]> dataKPIs, int indexLatitude, int indexLongitude,
			List<KPIWrapper> kpiWrappers, Map<String, Integer> kpiIndexMap, Map<String, Color> codecColorMap) {
		logger.info("Inside method to drawDrivePath ");
		for (String[] data : dataKPIs) {
			if (data != null && data.length > 0) {
				Double lat=NVLayer3Utils.getDoubleFromCsv(data, indexLatitude);
				Double lon=NVLayer3Utils.getDoubleFromCsv(data, indexLongitude);
				if (lat != null && lon != null) {
					double[] lonlat = new double[] { lon, lat };
					if (lonlat[0] == 0.0 || lonlat[1] == 0.0) {
						continue;
					}
					int lxLy[] = calculateLxLy(googlemaps[0].bounds, lonlat[1], lonlat[0], googlemaps[0].pixelLength);
					int lx = lxLy[ReportConstants.INDEX_ZER0];
					int ly = lxLy[ReportConstants.INDEX_ONE];
					for (KPIWrapper kpiwrapper : kpiWrappers) {
						drawCallAndHandoverPlot(data, lx, ly, kpiwrapper, kpiIndexMap, codecColorMap);
					}
				}
			}
		}
	}

	private void drawCallAndHandoverPlot(String[] data, int lx, int ly, KPIWrapper kpiwrapper,
			Map<String, Integer> kpiIndexMap, Map<String, Color> codecColorMap) {
		try {
			kpiIndexMap= checkAndIntilizeIndexMap(kpiIndexMap);
			 if (ReportConstants.CALL_PLOT.equalsIgnoreCase(kpiwrapper.getKpiName())) {
				 addCallPlotIntoImageCreator(kpiwrapper, lx, ly, data, kpiIndexMap);
			}else if (ReportConstants.HANDOVER_PLOT.equalsIgnoreCase(kpiwrapper.getKpiName())) {
				 addHandOverPlotIntoImageCreator(kpiwrapper, lx, ly, data, kpiIndexMap, false);
			}else if (ReportConstants.RESELECTION_PLOT.equalsIgnoreCase(kpiwrapper.getKpiName())) {
				 addReselectionPlotIntoImageCreator(kpiwrapper, lx, ly, data, kpiIndexMap);
			 }
			 else if (ReportConstants.ERAB_PLOT.equalsIgnoreCase(kpiwrapper.getKpiName())) {
				 addErabPlotIntoImageCreator(kpiwrapper, lx, ly, data, kpiIndexMap);
			 }
			 else if (ReportConstants.CALL_SETUP_PLOT.equalsIgnoreCase(kpiwrapper.getKpiName())) {
				 addCallSetupPlotIntoImageCreator(kpiwrapper, lx, ly, data, kpiIndexMap);
			 }
			 else if (ReportConstants.VOLTE_CODEC.equalsIgnoreCase(kpiwrapper.getKpiName())) {
				 addCodecPlotIntoImageCreator(kpiwrapper, lx, ly, data, kpiIndexMap, codecColorMap);
			 }
			 else if (ReportConstants.DL_MODULATION_TYPE.equalsIgnoreCase(kpiwrapper.getKpiName())) {
				 addDlModulationTypePlotIntoImageCreator(kpiwrapper, lx, ly, data, kpiIndexMap);
			 }
			 else if (ReportConstants.HANDOVER_FAILURE_PLOT.equalsIgnoreCase(kpiwrapper.getKpiName())) {
				 addHandOverPlotIntoImageCreator(kpiwrapper, lx, ly, data, kpiIndexMap, true);
			 }
		} catch (Exception e) {
			logger.error("Exception during CustomPlots of bubble {}  ", Utils.getStackTrace(e));
		}
	}

	private Map<String, Integer> checkAndIntilizeIndexMap(Map<String, Integer> kpiIndexMap) {
		if(kpiIndexMap!=null && !kpiIndexMap.isEmpty()) {
			return kpiIndexMap;
		}
		else {
			kpiIndexMap=new HashMap<>();
			kpiIndexMap.put(CALL_INITIATE, DriveHeaderConstants.INDEX_CALL_INITIATE);
			kpiIndexMap.put(CALL_DROP, DriveHeaderConstants.INDEX_CALL_DROP);

			kpiIndexMap.put(CALL_FAILURE, DriveHeaderConstants.INDEX_CALL_FAIL);
			kpiIndexMap.put(CALL_SUCCESS, DriveHeaderConstants.INDEX_CALL_SUCCESS);
			
			kpiIndexMap.put(HANDOVER_INITIATE, 24);

			kpiIndexMap.put(HANDOVER_FAILURE, 25);

			kpiIndexMap.put(HANDOVER_SUCCESS, DriveHeaderConstants.INDEX_HANDOVER_SUCCESS);

			kpiIndexMap.put(ReportConstants.TEST_TYPE, DriveHeaderConstants.INDEX_TEST_TYPE);
			kpiIndexMap.put(ReportConstants.CALL_PLOT, DriveHeaderConstants.CALL_PLOT_INDEX);
			kpiIndexMap.put(ReportConstants.HANDOVER_PLOT, DriveHeaderConstants.HANDOVER_PLOT_INDEX);
		   return kpiIndexMap;
		}
		
	}
	public void drawCustomRoute(List<List<List<Double>>> driveRoute, Color c) {
		logger.info("Going to plot multilple boundaries of Size {} , with color {} ",driveRoute!=null?driveRoute.size():null,c);
		try {
				if(driveRoute!=null && !driveRoute.isEmpty()){
					for (List<List<Double>> boundaries : driveRoute) {
							googlemapRenderer.drawCustomRoute(googlemaps[ReportConstants.INDEX_ZER0].getImg(), boundaries, googlemaps[ReportConstants.INDEX_ZER0].bounds, zoom, c, googlemaps[ReportConstants.INDEX_ZER0].pixelLength);
							googlemapRenderer.drawCustomRoute(terrainmaps[ReportConstants.INDEX_ZER0].getImg(), boundaries, terrainmaps[ReportConstants.INDEX_ZER0].bounds, zoom, c, terrainmaps[ReportConstants.INDEX_ZER0].pixelLength);
					}
				}
		} catch (Exception e) {
			logger.error("Exception inside method during plotting of multiple boundary {} ",e.getMessage());
		}
	}
	
	private void addTechnologyPlotIntoImageCreator(KPIWrapper kpiwrapper, int lx, int ly, int size, String[] data, Map<String, Integer> kpiIndexMap) {
		int index;
		index = kpiwrapper.getIndexKPI();
		String value = NVLayer3Utils.getStringFromCsv(data,kpiIndexMap.get(DriveHeaderConstants.TECHNOLOGY));
			if(!kpiwrapper.isValidPlot()){kpiwrapper.setValidPlot(true);}
			Color color = ColorFinder.getTechnologyColor(value);
			googlemapRenderer.drawBubble(googlemaps[index].getImg(), lx, ly,color, size);
	}
	
	public void drawSiteLocation(GoogleMaps[] googlemaps, List<SiteInformationWrapper> list,List<Integer> indexList) {
		logger.info("Inside method drawSiteLocation, list Size {} ",list!=null?list.size():null);
		try {
			if (list != null && !list.isEmpty()) {
				for (Integer index : indexList) {
					drawSiteLocation(googlemaps[index].getImg(), googlemaps, list, index);
				}
			} else {
				logger.info("Sitelist is null, so sites can't be draw");
			}
		} catch (Exception ne) {
			logger.error("Error in drawing Sites {} ", Utils.getStackTrace(ne));
		}
	}
	
	public void drawSiteLocation(BufferedImage image, GoogleMaps[] googlemaps2,List<SiteInformationWrapper> siteList, Integer index) {
			for (SiteInformationWrapper wrapper : siteList) {
				try {
					double[] lonlat = getLatLonForSiteList(wrapper);
					if (lonlat[0] == 0.0 || lonlat[1] == 0.0) {
						continue;
					}
					int[] lxLy = calculateLxLy(googlemaps2[0].bounds, lonlat[ReportConstants.INDEX_ONE], lonlat[ReportConstants.INDEX_ZER0], googlemaps2[0].pixelLength);
					int lx = lxLy[0];
					int ly = lxLy[1];
					Color color = getColorBySiteStatus(wrapper.getOperationalStatus());
					googlemapRenderer.renderSiteLocation(image, lx, ly, color, SITE_SIZE, SITE_INNERCIRCLE, SITE_OUTERCIRCLE);
				} catch (Exception e) {
					logger.warn("Exception inside method drawSiteLocation {} with err msg {} ",index,Utils.getStackTrace(e));
				}
			}
	}

	public static Color getColorBySiteStatus(String status) {
		if(ReportConstants.ENABLED.equalsIgnoreCase(status)){
			return Color.BLUE;
		}else if(ReportConstants.DISABLED.equalsIgnoreCase(status)){
			return Color.ORANGE;
		}else if(ReportConstants.ONAIR.equalsIgnoreCase(status)){
			return Color.GREEN;
		}else if(ReportConstants.PLANNED.equalsIgnoreCase(status)){
			return Color.YELLOW;
		}else if(ReportConstants.DECOMMISSIONED.equalsIgnoreCase(status)){
			return Color.RED;
		}
		return Color.BLACK;
	}

	public ImageCreator(Tile googleTileMin, Tile googleTileMax, int zoom, String mapType, List<Integer> indexList,
			Double GridSize) throws IOException {
		GoogleMaps googlemap = new GoogleMaps(googleTileMin, googleTileMax, zoom, mapType);
		GoogleMaps satelliteMap = new GoogleMaps(googleTileMin, googleTileMax, zoom, GoogleMaps.MAP_SATELLITE);
		GoogleMaps terrainMap = new GoogleMaps(googleTileMin, googleTileMax, zoom, GoogleMaps.MAP_TERRAIN);
		indexList.add(ReportConstants.INDEX_ZER0); // This is to get the bounds and pixel length always from fixed Zero											// Index
		logger.info("Total Count value is {} ",
				indexList.stream().collect(Collectors.summarizingInt(Integer::intValue)).getMax()
						+ ReportConstants.INDEX_ONE);
		googlemaps = new GoogleMaps[indexList.stream().parallel().collect(Collectors.summarizingInt(Integer::intValue))
				.getMax() + ReportConstants.INDEX_ONE];
		satellitemaps = new GoogleMaps[indexList.stream().parallel()
				.collect(Collectors.summarizingInt(Integer::intValue)).getMax() + ReportConstants.INDEX_ONE];
		terrainmaps = new GoogleMaps[ReportConstants.INDEX_ONE];
		Corner cornerForMinMaxTile = TileUtils.getCornerForMinMaxTile(googleTileMin, googleTileMax, zoom);
		googlemapRenderer = new GoogleMapRenderer();
		googlemap.getMapImage();
		satelliteMap.getMapImage();
		terrainMap.getMapImage();
		bounds = googlemap.bounds;
		int i = ReportConstants.INDEX_ZER0;
		for (Integer index : indexList) {
			googlemaps[index] = googlemap.getClone();
			satellitemaps[i++] = satelliteMap.getClone();
			drawGridOnImage(GridSize, cornerForMinMaxTile, googlemaps[index].getImg(), googlemaps, index);
		}
		logger.info(" Successfully Created images with grid ");
	}

	public List<GISLine> getGridLines(Double gridSizeInMeter, Corner gridCorner) {

		List<GISLine> listofLines = new ArrayList<>();

		int column = (int) Math
				.round(Math.abs(MensurationUtils.distanceBetweenPoints(gridCorner.getBottomRightPosition(),
						gridCorner.getBottomLeftPosition())) / gridSizeInMeter);
		int row = (int) Math.round(Math.abs(MensurationUtils.distanceBetweenPoints(gridCorner.getBottomLeftPosition(),
				gridCorner.getTopLeftPosition())) / gridSizeInMeter);

		for (int rowN = 0; rowN <= row; rowN++) {

			LatLng ref = MensurationUtils.getPositionAtDistance(gridCorner.getTopLeftPosition(), gridSizeInMeter * rowN,
					ReportConstants.BEARING_ANGLE_FOR_DOWN_DIRECTION);

			LatLng rightRef = MensurationUtils.getPositionAtDistance(ref, gridSizeInMeter * column,
					ReportConstants.BEARING_ANGLE_FOR_RIGHT_DIRECTION);

			listofLines.add(new GISLine(ref, rightRef));
		}

		for (int col = 0; col <= column; col++) {

			LatLng ref = MensurationUtils.getPositionAtDistance(gridCorner.getTopLeftPosition(), gridSizeInMeter * col,
					ReportConstants.BEARING_ANGLE_FOR_RIGHT_DIRECTION);

			LatLng downRef = MensurationUtils.getPositionAtDistance(ref, gridSizeInMeter * row,
					ReportConstants.BEARING_ANGLE_FOR_DOWN_DIRECTION);
			listofLines.add(new GISLine(ref, downRef));
		}
		return listofLines;
	}

	private void drawGridOnImage(Double gridSize, Corner cornerForMinMaxTile, BufferedImage image, GoogleMaps[] googlemaps,
			Integer index) {
		List<GISLine> lineList = getGridLines(gridSize, cornerForMinMaxTile);
		if (Utils.isValidList(lineList)) {
			for (GISLine gsline : lineList) {
//				logger.info("gsLine  {}", gsline.getBoundary());
				if (gsline.getBoundary() != null && !gsline.getBoundary().isEmpty()) {
					googlemapRenderer.drawBoundary(image, gsline.getBoundary(), googlemaps[index].bounds, Color.GRAY,
							googlemaps[index].pixelLength);
				}
			}
		}
	}
}

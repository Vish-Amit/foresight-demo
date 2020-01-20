package com.inn.foresight.module.nv.report.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.ForbiddenException;

import org.apache.xerces.impl.io.MalformedByteSequenceException;

import com.inn.commons.maps.LatLng;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.foresight.module.nv.report.stealthdashboard.wrapper.StealthGeographyDataWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveImageWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWOMapDataWrapper;

/** The Interface IMapImagesService. */
public interface IMapImagesService {

	/**
	 * Gets the drive images.
	 *
	 * @param driveImageWrapper the drive image wrapper
	 * @param pinLatLonList
	 * @return the drive images
	 * @throws ForbiddenException 
	 * @throws IOException 
	 * @throws MalformedByteSequenceException 
	 * @throws BusinessException 
	 */
	HashMap<String, BufferedImage> getDriveImages(DriveImageWrapper driveImageWrapper, List<Double[]> pinLatLonList) throws MalformedByteSequenceException, IOException;


	/**
	 * Saves the buffered images on given input path with key of map as image name.
	 *
	 * @param driveImageMap the drive image map
	 * @param path the path
	 * @param isAtollImages TODO
	 * @return the hash map
	 */
	HashMap<String,String > saveDriveImages(HashMap<String, BufferedImage> driveImageMap,String path, boolean isAtollImages);



	/**
	 * Gets the legend images.
	 *
	 * @param list the list of KPidata Wrapper
	 * @param driveData 
	 * @return the legend images
	 */
	HashMap<String, BufferedImage> getLegendImages(List<KPIWrapper> list, List<String[]> driveData);
	
	/**
	 * Gets the legend images.
	 *
	 * @param list the list of KPidata Wrapper
	 * @return the legend images
	 *//*
	public HashMap<String, BufferedImage> getLegendImages(List<KPIWrapper> list);*/

	HashMap<String, BufferedImage> getStationaryImages(DriveImageWrapper driveImageWrapper,
			List<Double[]> pinLonLatList, String recipeName);
	
	/**
	 * Gets the legend images for InBuilding Benchmark.
	 *
	 *
	 * @param list
	 *            the list of KPidata Wrapper
	 * @param driveData
	 * @return the legend images
	 */
	HashMap<String, BufferedImage> getLegendImagesForIBBenchmark(List<KPIWrapper> list, List<String[]> driveData);
	
	HashMap<String, BufferedImage> getLegendImagesForNVDashboard(List<KPIWrapper> list);


	HashMap<String, BufferedImage> getAtollPredictionImages(DriveImageWrapper driveImageWrapper);

	HashMap<String, BufferedImage> getAtollLegendImages(List<KPIWrapper> list);
	
	BufferedImage getMapImageForStealthWO(List<StealthWOMapDataWrapper> stealthMapDataWrapper);


	HashMap<String, BufferedImage> getSitesLocationImage(DriveImageWrapper driveImageWrapper);


	HashMap<String, BufferedImage> getDriveImagesForReport(DriveImageWrapper driveImageWrapper,
			List<Double[]> pinLatLonList, Map<String, Integer> kpiIndexMap) throws IOException;


	HashMap<String, BufferedImage> getLegendImagesForReport(List<KPIWrapper> list, List<String[]> driveData,
			Integer testTypeIndex);

	HashMap<String, BufferedImage> getBuildingLocationImage(DriveImageWrapper driveImageWrapper,
			List<Double[]> pinLonLatList, String recipeName);
	
	HashMap<String, BufferedImage> getBuildingImage(List<Double[]> pinLonLatList,String recipeName,LatLng centroid);

	HashMap<String, String> getClusterBoundaryImage(
			List<StealthGeographyDataWrapper> geographyWiseDataWrapperList);
	
	Map<String, Long> getEarfcnCountMap(DriveImageWrapper driveImageWrapper, Map<String, Integer> kpiIndexMap);
}

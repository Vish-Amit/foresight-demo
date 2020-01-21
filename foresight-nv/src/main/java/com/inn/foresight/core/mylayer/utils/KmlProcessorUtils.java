package com.inn.foresight.core.mylayer.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class KmlProcessorUtils.
 */
public class KmlProcessorUtils
{
	
	/**
	 * Update min max lat lon for point.
	 *
	 * @param kmlProcessorWrapper the kml processor wrapper
	 * @param lon the lon
	 * @param lat the lat
	 */
	public static void updateMinMaxLatLonForPoint(KmlProcessorWrapper kmlProcessorWrapper, Double lon, Double lat)
	{


		if (lat < kmlProcessorWrapper.getMin_lat())
			kmlProcessorWrapper.setMin_lat(lat);
		if (lat > kmlProcessorWrapper.getMax_lat())
			kmlProcessorWrapper.setMax_lat(lat);
		if (lon < kmlProcessorWrapper.getMin_lon())
			kmlProcessorWrapper.setMin_lon(lon);
		if (lon > kmlProcessorWrapper.getMax_lon())
			kmlProcessorWrapper.setMax_lon(lon);
	}

	/**
	 * Update min max lat lon for poly.
	 *
	 * @param kmlProcessorWrapper the kml processor wrapper
	 * @param coordinatesList the coordinates list
	 */
	public static void updateMinMaxLatLonForPoly(KmlProcessorWrapper kmlProcessorWrapper,List<List<Double>> coordinatesList )
	{

		for (List<Double> list : coordinatesList) {
			Double lon=list.get(0);
			Double lat=list.get(1);


			if (lat < kmlProcessorWrapper.getMin_lat())
				kmlProcessorWrapper.setMin_lat(lat);
			if (lat > kmlProcessorWrapper.getMax_lat())
				kmlProcessorWrapper.setMax_lat(lat);
			if (lon < kmlProcessorWrapper.getMin_lon())
				kmlProcessorWrapper.setMin_lon(lon);
			if (lon > kmlProcessorWrapper.getMax_lon())
				kmlProcessorWrapper.setMax_lon(lon);

		}
	}


	/**
	 * Calculate centroid.
	 *
	 * @param kmlProcessorWrapper the kml processor wrapper
	 * @return the list
	 */
	public static List<Double> calculateCentroid(KmlProcessorWrapper kmlProcessorWrapper)
	{
		List<Double> centroid =new ArrayList<>();
		centroid.add((kmlProcessorWrapper.getMax_lon()+kmlProcessorWrapper.getMin_lon())/2);
		centroid.add((kmlProcessorWrapper.getMax_lat()+kmlProcessorWrapper.getMin_lat())/2);
		return centroid;
	}
}

package com.inn.foresight.core.maplayer.wrapper;

import java.util.List;

/**
 * The Class GeographyDataWrapper.
 */
public class GeographyDataWrapper {

	/** The centroid. */
	private List<Double> centroid;
	
	/** The coordinates. */
	private List<List<List<Double>>> coordinates;
	
	/** The geography name. */
	private String geographyName;
	
	/** The is large area. */
	private boolean isLargeArea;
	
	/**
	 * Gets the centroid.
	 *
	 * @return the centroid
	 */
	public List<Double> getCentroid() {
		return centroid;
	}
	
	/**
	 * Sets the centroid.
	 *
	 * @param centroid the new centroid
	 */
	public void setCentroid(List<Double> centroid) {
		this.centroid = centroid;
	}
	
	/**
	 * Gets the coordinates.
	 *
	 * @return the coordinates
	 */
	public List<List<List<Double>>> getCoordinates() {
		return coordinates;
	}
	
	/**
	 * Sets the coordinates.
	 *
	 * @param coordinates the new coordinates
	 */
	public void setCoordinates(List<List<List<Double>>> coordinates) {
		this.coordinates = coordinates;
	}
	
	/**
	 * Gets the geography name.
	 *
	 * @return the geography name
	 */
	public String getGeographyName() {
		return geographyName;
	}
	
	/**
	 * Sets the geography name.
	 *
	 * @param geographyName the new geography name
	 */
	public void setGeographyName(String geographyName) {
		this.geographyName = geographyName;
	}
	
	/**
	 * Checks if is large area.
	 *
	 * @return true, if is large area
	 */
	public boolean isLargeArea() {
		return isLargeArea;
	}
	
	/**
	 * Sets the large area.
	 *
	 * @param isLargeArea the new large area
	 */
	public void setLargeArea(boolean isLargeArea) {
		this.isLargeArea = isLargeArea;
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "GeographyDataWrapper [centroid=" + centroid + ", coordinates=" + coordinates + ", geographyName="
				+ geographyName + ", isLargeArea=" + isLargeArea + "]";
	}
}

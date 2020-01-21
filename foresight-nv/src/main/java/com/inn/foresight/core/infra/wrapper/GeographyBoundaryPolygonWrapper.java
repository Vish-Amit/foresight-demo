package com.inn.foresight.core.infra.wrapper;

import java.util.List;

/**
 * The Class GeographyBoundaryPolygonWrapper.
 */
public class GeographyBoundaryPolygonWrapper {
	

	private Integer id;
	
	/** The polygon 3 D list. */
	List<List<List<Double>>> polygon3DList;
	
	/** The polygon village list. */
	List<Double> polygonVillageList;
	
	private Integer kmlProcessorPK;
	
	private String kmlName;
	
	private String colorCode;
	
	private String kmlType;
	
	List<Double> centroid;
	
	/** The min lat. */
	Double minLat;
	
	/** The max lat. */
	Double maxLat;
	
	/** The min lon. */
	Double minLon;
	
	/** The max lon. */
	Double maxLon;
	
	private String polygon;
	
	private Integer userId;
	

	/**
	 * Gets the polygon 3 D list.
	 *
	 * @return the polygon 3 D list
	 */
	public List<List<List<Double>>> getPolygon3DList() {
		return polygon3DList;
	}

	/**
	 * Sets the polygon 3 D list.
	 *
	 * @param polygon3dList the new polygon 3 D list
	 */
	public void setPolygon3DList(List<List<List<Double>>> polygon3dList) {
		polygon3DList = polygon3dList;
	}

	/**
	 * Gets the polygon village list.
	 *
	 * @return the polygon village list
	 */
	public List<Double> getPolygonVillageList() {
		return polygonVillageList;
	}

	/**
	 * Sets the polygon village list.
	 *
	 * @param polygonVillageList the new polygon village list
	 */
	public void setPolygonVillageList(List<Double> polygonVillageList) {
		this.polygonVillageList = polygonVillageList;
	}

	public Integer getKmlProcessorPK() {
		return kmlProcessorPK;
	}

	public void setKmlProcessorPK(Integer kmlProcessorPK) {
		this.kmlProcessorPK = kmlProcessorPK;
	}

	public String getKmlName() {
		return kmlName;
	}

	public void setKmlName(String kmlName) {
		this.kmlName = kmlName;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public String getKmlType() {
		return kmlType;
	}

	public void setKmlType(String kmlType) {
		this.kmlType = kmlType;
	}

	public List<Double> getCentroid() {
		return centroid;
	}

	public void setCentroid(List<Double> centroid) {
		this.centroid = centroid;
	}

	public Double getMinLat() {
		return minLat;
	}

	public void setMinLat(Double minLat) {
		this.minLat = minLat;
	}

	public Double getMaxLat() {
		return maxLat;
	}

	public void setMaxLat(Double maxLat) {
		this.maxLat = maxLat;
	}

	public Double getMinLon() {
		return minLon;
	}

	public void setMinLon(Double minLon) {
		this.minLon = minLon;
	}

	public Double getMaxLon() {
		return maxLon;
	}

	public void setMaxLon(Double maxLon) {
		this.maxLon = maxLon;
	}

	public String getPolygon() {
		return polygon;
	}

	public void setPolygon(String polygon) {
		this.polygon = polygon;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "GeographyBoundaryPolygonWrapper [id=" + id + ", polygon3DList=" + polygon3DList + ", polygonVillageList=" + polygonVillageList + ", kmlProcessorPK=" + kmlProcessorPK + ", kmlName="
				+ kmlName + ", colorCode=" + colorCode + ", kmlType=" + kmlType + ", centroid=" + centroid + ", minLat=" + minLat + ", maxLat=" + maxLat + ", minLon=" + minLon + ", maxLon=" + maxLon
				+ ", polygon=" + polygon + ", userId=" + userId + "]";
	}
}

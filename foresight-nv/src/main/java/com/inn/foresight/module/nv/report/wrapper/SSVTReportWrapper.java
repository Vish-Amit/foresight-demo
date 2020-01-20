package com.inn.foresight.module.nv.report.wrapper;

import java.util.List;


/** The Class SSVTReportWrapper. */
public class SSVTReportWrapper {

	/** The tester name. */
	private String testerName;

	/** The site name. */
	private String siteName;

	/** The city name. */
	private String cityName;

	/** The technology. */
	private String technology;

	/** The address. */
	private String address;

	/** The file name. */
	private String fileName;

	/** The sub wrapper list. */
	private List<SSVTReportSubWrapper>subWrapperList;

	/** The test date. */
	private String testDate;
	
	private String recipeNo;
	
	
	private String siteAddressTitle;
	
	private List<PSDataWrapper> listPsDataWrapperList;
	
    private String isIBCFull;
    private String isVaildGraph;
    /** The latitude. */
	private Double buildingLatitude;

	/** The longitude. */
	private Double buildingLongitude;
	/**
	 * @return the buildingLatitude
	 */
	public Double getBuildingLatitude() {
		return buildingLatitude;
	}

	/**
	 * @param buildingLatitude the buildingLatitude to set
	 */
	public void setBuildingLatitude(Double buildingLatitude) {
		this.buildingLatitude = buildingLatitude;
	}

	/**
	 * @return the buildingLongitude
	 */
	public Double getBuildingLongitude() {
		return buildingLongitude;
	}

	/**
	 * @param buildingLongitude the buildingLongitude to set
	 */
	public void setBuildingLongitude(Double buildingLongitude) {
		this.buildingLongitude = buildingLongitude;
	}

	public String getSiteAddressTitle() {
		return siteAddressTitle;
	}

	public void setSiteAddressTitle(String siteAddressTitle) {
		this.siteAddressTitle = siteAddressTitle;
	}

	public String getRecipeNo() {
		return recipeNo;
	}

	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
	}

	/**
	 * Gets the file name.
	 *
	 * @return the file name
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Sets the file name.
	 *
	 * @param fileName the new file name
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Gets the test date.
	 *
	 * @return the test date
	 */
	public String getTestDate() {
		return testDate;
	}

	/**
	 * Sets the test date.
	 *
	 * @param testDate the new test date
	 */
	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}

	/**
	 * Gets the tester name.
	 *
	 * @return the tester name
	 */
	public String getTesterName() {
		return testerName;
	}

	/**
	 * Sets the tester name.
	 *
	 * @param testerName the new tester name
	 */
	public void setTesterName(String testerName) {
		this.testerName = testerName;
	}

	/**
	 * Gets the site name.
	 *
	 * @return the site name
	 */
	public String getSiteName() {
		return siteName;
	}

	/**
	 * Sets the site name.
	 *
	 * @param siteName the new site name
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	/**
	 * Gets the city name.
	 *
	 * @return the city name
	 */
	public String getCityName() {
		return cityName;
	}

	/**
	 * Sets the city name.
	 *
	 * @param cityName the new city name
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	/**
	 * Gets the technology.
	 *
	 * @return the technology
	 */
	public String getTechnology() {
		return technology;
	}

	/**
	 * Sets the technology.
	 *
	 * @param technology the new technology
	 */
	public void setTechnology(String technology) {
		this.technology = technology;
	}

	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Gets the sub wrapper list.
	 *
	 * @return the sub wrapper list
	 */
	public List<SSVTReportSubWrapper> getSubWrapperList() {
		return subWrapperList;
	}

	/**
	 * Sets the sub wrapper list.
	 *
	 * @param subWrapperList the new sub wrapper list
	 */
	public void setSubWrapperList(List<SSVTReportSubWrapper> subWrapperList) {
		this.subWrapperList = subWrapperList;
	}

	public List<PSDataWrapper> getListPsDataWrapperList() {
		return listPsDataWrapperList;
	}

	public void setListPsDataWrapperList(List<PSDataWrapper> listPsDataWrapperList) {
		this.listPsDataWrapperList = listPsDataWrapperList;
	}

	

	public String getIsIBCFull() {
		return isIBCFull;
	}

	public void setIsIBCFull(String isIBCFull) {
		this.isIBCFull = isIBCFull;
	}

	
	public String getIsVaildGraph() {
		return isVaildGraph;
	}

	public void setIsVaildGraph(String isVaildGraph) {
		this.isVaildGraph = isVaildGraph;
	}

}

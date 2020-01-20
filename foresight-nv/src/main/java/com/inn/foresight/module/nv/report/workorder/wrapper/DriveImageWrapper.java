package com.inn.foresight.module.nv.report.workorder.wrapper;

import java.util.List;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;

@JpaWrapper
public class DriveImageWrapper {

	List<String[]> dataKPIs;
	Integer indexLatitude;
	Integer indexLongitude;
	Integer indexPci;
	List<KPIWrapper> kpiWrappers;
	List<SiteInformationWrapper> siteDataList;
	

	/** Multi_cluster_boundary. */
	List<List<List<List<Double>>>> boundaries;
	
	List<List<List<Double>>> driveRoute;
	
	String band;
	
	public String getBand() {
		return band;
	}


	public void setBand(String band) {
		this.band = band;
	}


	public List<List<List<Double>>> getDriveRoute() {
		return driveRoute;
	}


	public void setDriveRoute(List<List<List<Double>>> driveRoute) {
		this.driveRoute = driveRoute;
	}


	public DriveImageWrapper(List<String[]> dataKPIs, Integer indexLatitude, Integer indexLongitude, Integer indexPci,
			List<KPIWrapper> kpiWrappers, List<SiteInformationWrapper> siteDataList,
			List<List<List<List<Double>>>> boundaries) {
		super();
		this.dataKPIs = dataKPIs;
		this.indexLatitude = indexLatitude;
		this.indexLongitude = indexLongitude;
		this.indexPci = indexPci;
		this.kpiWrappers = kpiWrappers;
		this.siteDataList = siteDataList;
		this.boundaries = boundaries;
	}
	public DriveImageWrapper(List<String[]> dataKPIs, Integer indexLatitude, Integer indexLongitude, Integer indexPci,
			List<KPIWrapper> kpiWrappers, List<SiteInformationWrapper> siteDataList) {
		super();
		this.dataKPIs = dataKPIs;
		this.indexLatitude = indexLatitude;
		this.indexLongitude = indexLongitude;
		this.indexPci = indexPci;
		this.kpiWrappers = kpiWrappers;
		this.siteDataList = siteDataList;
	}

	
	public DriveImageWrapper(List<String[]> dataKPIs, int indexLatitude, int indexLongitude, int indexPci,
			List<KPIWrapper> kpiWrappers, List<SiteInformationWrapper> siteDataList,
			List<List<List<List<Double>>>> boundaries,List<List<List<Double>>> driveRoute) {
		super();
		this.dataKPIs = dataKPIs;
		this.indexLatitude = indexLatitude;
		this.indexLongitude = indexLongitude;
		this.indexPci = indexPci;
		this.kpiWrappers = kpiWrappers;
		this.siteDataList = siteDataList;
		this.boundaries = boundaries;
		this.driveRoute=driveRoute;
	}
	
	
	public DriveImageWrapper(List<String[]> dataKPIs, Integer indexLatitude, Integer indexLongitude, Integer indexPci,
			List<KPIWrapper> kpiWrappers, List<SiteInformationWrapper> siteDataList,
			List<List<List<List<Double>>>> boundaries,List<List<List<Double>>> driveRoute,String band) {
		super();
		this.dataKPIs = dataKPIs;
		this.indexLatitude = indexLatitude;
		this.indexLongitude = indexLongitude;
		this.indexPci = indexPci;
		this.kpiWrappers = kpiWrappers;
		this.siteDataList = siteDataList;
		this.boundaries = boundaries;
		this.driveRoute=driveRoute;
		this.band=band;
	}


	public DriveImageWrapper() {
		super();
	}


	public List<SiteInformationWrapper> getSiteDataList1() {
		return siteDataList;
	}


	/**Public void setSiteDataList1(List<SiteInformationWrapper> siteDataList1) {
		this.siteDataList1= siteDataList1;
	}*/


	public Integer getIndexPci() {
		return indexPci;
	}

	public void setIndexPci(Integer indexPci) {
		this.indexPci = indexPci;
	}

	public List<String[]> getDataKPIs() {
		return dataKPIs;
	}

	public void setDataKPIs(List<String[]> dataKPIs) {
		this.dataKPIs = dataKPIs;
	}

	public int getIndexLatitude() {
		return indexLatitude;
	}

	public void setIndexLatitude(Integer indexLatitude) {
		this.indexLatitude = indexLatitude;
	}

	public int getIndexLongitude() {
		return indexLongitude;
	}

	public void setIndexLongitude(Integer indexLongitude) {
		this.indexLongitude = indexLongitude;
	}

	public List<KPIWrapper> getKpiWrappers() {
		return kpiWrappers;
	}

	public void setKpiWrappers(List<KPIWrapper> kpiWrappers) {
		this.kpiWrappers = kpiWrappers;
	}

	/**Public List<SiteDataWrapper> getSiteDataList() {
		return siteDataList;
	}

	public void setSiteDataList(List<SiteDataWrapper> siteDataList) {
		this.siteDataList = siteDataList;
	}*/

	public List<List<List<List<Double>>>> getBoundaries() {
		return boundaries;
	}

	public List<SiteInformationWrapper> getSiteDataList() {
		return siteDataList;
	}


	public void setSiteDataList(List<SiteInformationWrapper> siteDataList) {
		this.siteDataList = siteDataList;
	}


	public void setBoundaries(List<List<List<List<Double>>>> boundaries) {
		this.boundaries = boundaries;
	}

}

package com.inn.foresight.core.scatterchart.wrapper;

import java.io.Serializable;
import java.util.List;

import com.inn.core.generic.wrapper.RestWrapper;

/**
 * The Class ScatterChartRequest.
 */
@RestWrapper
public class ScatterChartRequest implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7134805174102372813L;

	/** The columns. */
	private List<String> columns;

	/** The geography list. */
	private List<String> geographyList;

	/** The geography table. */
	public String geographyTable;

	/** The kpi table. */
	public String kpiTable;
	
	/** The kpi 1. */
	public String kpi1;
	
	/** The kpi 2. */
	public String kpi2;
	
	/** The kpi 1 date. */
	public String kpi1Date;
	
	/** The kpi 2 date. */
	public String kpi2Date;
	
	/** The site status. */
	public String siteStatus;

	/**
	 * Gets the kpi table.
	 *
	 * @return the kpi table
	 */
	public String getKpiTable() {
		return kpiTable;
	}

	/**
	 * Sets the kpi table.
	 *
	 * @param kpiTable the new kpi table
	 */
	public void setKpiTable(String kpiTable) {
		this.kpiTable = kpiTable;
	}

	/**
	 * Gets the geography table.
	 *
	 * @return the geography table
	 */
	public String getGeographyTable() {
		return geographyTable;
	}

	/**
	 * Sets the geography table.
	 *
	 * @param geographyTable the new geography table
	 */
	public void setGeographyTable(String geographyTable) {
		this.geographyTable = geographyTable;
	}

	/**
	 * Gets the columns.
	 *
	 * @return the columns
	 */
	public List<String> getColumns() {
		return columns;
	}


	/**
	 * Sets the columns.
	 *
	 * @param columns the new columns
	 */
	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	/**
	 * Gets the geography list.
	 *
	 * @return the geography list
	 */
	public List<String> getGeographyList() {
		return geographyList;
	}

	/**
	 * Sets the geography list.
	 *
	 * @param geographyList the new geography list
	 */
	public void setGeographyList(List<String> geographyList) {
		this.geographyList = geographyList;
	}

	/**
	 * Gets the kpi 1.
	 *
	 * @return the kpi 1
	 */
	public String getKpi1() {
		return kpi1;
	}

	/**
	 * Sets the kpi 1.
	 *
	 * @param kpi1 the new kpi 1
	 */
	public void setKpi1(String kpi1) {
		this.kpi1 = kpi1;
	}

	/**
	 * Gets the kpi 2.
	 *
	 * @return the kpi 2
	 */
	public String getKpi2() {
		return kpi2;
	}

	/**
	 * Sets the kpi 2.
	 *
	 * @param kpi2 the new kpi 2
	 */
	public void setKpi2(String kpi2) {
		this.kpi2 = kpi2;
	}

	/**
	 * Gets the kpi 1 date.
	 *
	 * @return the kpi 1 date
	 */
	public String getKpi1Date() {
		return kpi1Date;
	}

	/**
	 * Sets the kpi 1 date.
	 *
	 * @param kpi1Date the new kpi 1 date
	 */
	public void setKpi1Date(String kpi1Date) {
		this.kpi1Date = kpi1Date;
	}

	/**
	 * Gets the kpi 2 date.
	 *
	 * @return the kpi 2 date
	 */
	public String getKpi2Date() {
		return kpi2Date;
	}

	/**
	 * Sets the kpi 2 date.
	 *
	 * @param kpi2Date the new kpi 2 date
	 */
	public void setKpi2Date(String kpi2Date) {
		this.kpi2Date = kpi2Date;
	}

	/**
	 * Gets the site status.
	 *
	 * @return the site status
	 */
	public String getSiteStatus() {
		return siteStatus;
	}

	/**
	 * Sets the site status.
	 *
	 * @param siteStatus the new site status
	 */
	public void setSiteStatus(String siteStatus) {
		this.siteStatus = siteStatus;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ScatterChartRequest [columns=" + columns + ", geographyList=" + geographyList + ", geographyTable="
				+ geographyTable + ", kpiTable=" + kpiTable + ", kpi1=" + kpi1 + ", kpi2=" + kpi2 + ", kpi1Date="
				+ kpi1Date + ", kpi2Date=" + kpi2Date + "]";
	}


}

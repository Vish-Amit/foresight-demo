package com.inn.foresight.core.kpicomparison.wrapper;

import java.io.Serializable;
import java.util.List;

import com.inn.core.generic.wrapper.RestWrapper;

/**
 * The Class KPIComparisionRequest.
 */
@RestWrapper
public class KPIComparisonRequest implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4756920349504170643L;

	/** The kpi 1. */
	private String kpi1;

	/** The kpi 1 column. */
	private String kpi1Column;

	/** The kpi 1 date. */
	private String kpi1Date;

	/** The kpi 2. */
	private String kpi2;

	/** The kpi 2 column. */
	private String kpi2Column;

	/** The kpi 2 date. */
	private String kpi2Date;

	/** The table. */
	private String table;

	/** The column list. */
	private List<String> columnList;

	/** The zoom. */
	private Integer zoom;

	/** The north east lat. */
	private Double nELat;

	/** The north east lng. */
	private Double nELng;

	/** The south west lat. */
	private Double sWLat;

	/** The south west lng. */
	private Double sWLng;

	/** The min date. */
	private String minDate;

	/** The max date. */
	private String maxDate;

	/** The postfix. */
	private String postfix;

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
	 * @param kpi1
	 *            the new kpi 1
	 */
	public void setKpi1(String kpi1) {
		this.kpi1 = kpi1;
	}

	/**
	 * Gets the kpi 1 column.
	 *
	 * @return the kpi 1 column
	 */
	public String getKpi1Column() {
		return kpi1Column;
	}

	/**
	 * Sets the kpi 1 column.
	 *
	 * @param kpi1Column
	 *            the new kpi 1 column
	 */
	public void setKpi1Column(String kpi1Column) {
		this.kpi1Column = kpi1Column;
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
	 * @param kpi1Date
	 *            the new kpi 1 date
	 */
	public void setKpi1Date(String kpi1Date) {
		this.kpi1Date = kpi1Date;
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
	 * @param kpi2
	 *            the new kpi 2
	 */
	public void setKpi2(String kpi2) {
		this.kpi2 = kpi2;
	}

	/**
	 * Gets the kpi 2 column.
	 *
	 * @return the kpi 2 column
	 */
	public String getKpi2Column() {
		return kpi2Column;
	}

	/**
	 * Sets the kpi 2 column.
	 *
	 * @param kpi2Column
	 *            the new kpi 2 column
	 */
	public void setKpi2Column(String kpi2Column) {
		this.kpi2Column = kpi2Column;
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
	 * @param kpi2Date
	 *            the new kpi 2 date
	 */
	public void setKpi2Date(String kpi2Date) {
		this.kpi2Date = kpi2Date;
	}

	/**
	 * Gets the table.
	 *
	 * @return the table
	 */
	public String getTable() {
		return table;
	}

	/**
	 * Sets the table.
	 *
	 * @param table
	 *            the new table
	 */
	public void setTable(String table) {
		this.table = table;
	}

	/**
	 * Gets the column list.
	 *
	 * @return the column list
	 */
	public List<String> getColumnList() {
		return columnList;
	}

	/**
	 * Sets the column list.
	 *
	 * @param columnList
	 *            the new column list
	 */
	public void setColumnList(List<String> columnList) {
		this.columnList = columnList;
	}

	/**
	 * Gets the zoom.
	 *
	 * @return the zoom
	 */
	public Integer getZoom() {
		return zoom;
	}

	/**
	 * Sets the zoom.
	 *
	 * @param zoom
	 *            the new zoom
	 */
	public void setZoom(Integer zoom) {
		this.zoom = zoom;
	}

	/**
	 * Gets the n E lat.
	 *
	 * @return the n E lat
	 */
	public Double getnELat() {
		return nELat;
	}

	/**
	 * Sets the n E lat.
	 *
	 * @param nELat
	 *            the new n E lat
	 */
	public void setnELat(Double nELat) {
		this.nELat = nELat;
	}

	/**
	 * Gets the n E lng.
	 *
	 * @return the n E lng
	 */
	public Double getnELng() {
		return nELng;
	}

	/**
	 * Sets the n E lng.
	 *
	 * @param nELng
	 *            the new n E lng
	 */
	public void setnELng(Double nELng) {
		this.nELng = nELng;
	}

	/**
	 * Gets the s W lat.
	 *
	 * @return the s W lat
	 */
	public Double getsWLat() {
		return sWLat;
	}

	/**
	 * Sets the s W lat.
	 *
	 * @param sWLat
	 *            the new s W lat
	 */
	public void setsWLat(Double sWLat) {
		this.sWLat = sWLat;
	}

	/**
	 * Gets the s W lng.
	 *
	 * @return the s W lng
	 */
	public Double getsWLng() {
		return sWLng;
	}

	/**
	 * Sets the s W lng.
	 *
	 * @param sWLng
	 *            the new s W lng
	 */
	public void setsWLng(Double sWLng) {
		this.sWLng = sWLng;
	}

	/**
	 * Gets the min date.
	 *
	 * @return the min date
	 */
	public String getMinDate() {
		return minDate;
	}

	/**
	 * Sets the min date.
	 *
	 * @param minDate
	 *            the new min date
	 */
	public void setMinDate(String minDate) {
		this.minDate = minDate;
	}

	/**
	 * Gets the max date.
	 *
	 * @return the max date
	 */
	public String getMaxDate() {
		return maxDate;
	}

	/**
	 * Sets the max date.
	 *
	 * @param maxDate
	 *            the new max date
	 */
	public void setMaxDate(String maxDate) {
		this.maxDate = maxDate;
	}

	/**
	 * Gets the postfix.
	 *
	 * @return the postfix
	 */
	public String getPostfix() {
		return postfix;
	}

	/**
	 * Sets the postfix.
	 *
	 * @param postfix
	 *            the new postfix
	 */
	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}

	@Override
	public String toString() {
		return "KPIComparisionRequest [kpi1=" + kpi1 + ", kpi1Column=" + kpi1Column + ", kpi1Date=" + kpi1Date
				+ ", kpi2=" + kpi2 + ", kpi2Column=" + kpi2Column + ", kpi2Date=" + kpi2Date + ", table=" + table
				+ ", columnList=" + columnList + ", zoom=" + zoom + ", nELat=" + nELat + ", nELng=" + nELng + ", sWLat="
				+ sWLat + ", sWLng=" + sWLng + ", minDate=" + minDate + ", maxDate=" + maxDate + ", postfix=" + postfix
				+ "]";
	}

}

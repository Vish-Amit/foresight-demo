package com.inn.foresight.module.nv.livedrive.wrapper;

import java.util.List;

/**
 * The Class TestDriveAggregationWeekWrapper.
 *
 * @author 
 */
public class TestDriveAggregationWeekWrapper implements java.lang.Comparable<TestDriveAggregationWeekWrapper>{
	
	/** The week. */
	private Integer week;
	
	/** The year. */
	private Integer year;
	
	/** The total drives. */
	private Long totalDrives;
	
	/** Instantiates a new test drive aggregation week wrapper. */
	public TestDriveAggregationWeekWrapper()
	{
		//empty	
	}

	/**
	 * Instantiates a new test drive aggregation week wrapper.
	 *
	 * @param week the week
	 * @param totalCount the total count
	 * @param year the year
	 */
	public TestDriveAggregationWeekWrapper(Integer week, Long totalCount, Integer year) {
		this.week = week;
		this.totalDrives = totalCount;
		this.year = year;
	}

	/**
	 * Gets the total drives.
	 *
	 * @return the total drives
	 */
	public Long getTotalDrives() {
		return totalDrives;
	}

	/**
	 * Sets the total drives.
	 *
	 * @param totalDrives the new total drives
	 */
	public void setTotalDrives(Long totalDrives) {
		this.totalDrives = totalDrives;
	}

	/** The drives. */
	private List<TestDriveAggregationWrapper> drives;

	/**
	 * Gets the week.
	 *
	 * @return the week
	 */
	public Integer getWeek() {
		return week;
	}

	/**
	 * Sets the week.
	 *
	 * @param week the new week
	 */
	public void setWeek(Integer week) {
		this.week = week;
	}

	/**
	 * Gets the year.
	 *
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * Sets the year.
	 *
	 * @param year the new year
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/**
	 * Gets the drives.
	 *
	 * @return the drives
	 */
	public List<TestDriveAggregationWrapper> getDrives() {
		return drives;
	}

	/**
	 * Sets the drives.
	 *
	 * @param drives the new drives
	 */
	public void setDrives(List<TestDriveAggregationWrapper> drives) {
		this.drives = drives;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "TestDriveAggregationWeekWrapper [week=" + week + ", totalDrives=" + totalDrives + ", drives=" + drives + "]";
	}

	
	/**
	 * Compare to.
	 *
	 * @param o the o
	 * @return the int
	 */
	@Override
	public int compareTo(TestDriveAggregationWeekWrapper o) {
		return o.getWeek().compareTo(this.week);
	}

}
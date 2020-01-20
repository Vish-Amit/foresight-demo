package com.inn.foresight.module.nv.livedrive.wrapper;

import java.util.Date;

import com.inn.foresight.module.nv.livedrive.model.LiveTestDrive.Status;
import com.inn.product.um.user.model.User;

/** The Class TestDriveAggregationWrapper. */
public class TestDriveAggregationWrapper {

	/** The user. */
	private User user;
	
	/** The firstname. */
	private String firstname;
	
	/** The lastname. */
	private String lastname;
	
	/** The username. */
	private String username;
	
	/** The userid. */
	private Integer userid;
	
	/** The count. */
	private Long count;
	
	/** The status. */
	private Status status;
	
	/** The week. */
	private Integer week;
	
	/** The year. */
	private Integer year;
	
	/** The week count. */
	private Long weekCount;
	
	/** The start time. */
	private Date startTime;

	/** Instantiates a new test drive aggregation wrapper. */
	public TestDriveAggregationWrapper() {
		super();
	}

	/**
	 * Instantiates a new test drive aggregation wrapper.
	 *
	 * @param user the user
	 * @param count the count
	 * @param status the status
	 */
	public TestDriveAggregationWrapper(User user, Long count, Status status) {
		this.count = count;
		this.status = status;
		if (user != null) {
			this.firstname = user.getFirstName();
			this.lastname = user.getLastName();
			this.username = user.getUserName();
			this.userid = user.getUserid();
		}
	}

	/**
	 * Instantiates a new test drive aggregation wrapper.
	 *
	 * @param week the week
	 * @param user the user
	 * @param count the count
	 * @param year the year
	 */
	public TestDriveAggregationWrapper(Integer week, User user, Long count, Integer year) {
		this.week = week;
		this.count = count;
		this.year = year;
		if (user != null) {
			this.firstname = user.getFirstName();
			this.lastname = user.getLastName();
			this.username = user.getUserName();
			this.userid = user.getUserid();
		}
	}

	
	/**
	 * Instantiates a new test drive aggregation wrapper.
	 *
	 * @param week the week
	 * @param weekCount the week count
	 * @param user the user
	 * @param count the count
	 * @param year the year
	 */
	public TestDriveAggregationWrapper(Integer week,Long weekCount, User user, Long count, Integer year) {
		this.week = week;
		this.count = count;
		this.year = year;
		if (user != null) {
			this.firstname = user.getFirstName();
			this.lastname = user.getLastName();
			this.username = user.getUserName();
			this.userid = user.getUserid();
			this.weekCount=weekCount;
		}
	}
	
	/**
	 * Instantiates a new test drive aggregation wrapper.
	 *
	 * @param week the week
	 * @param weekCount the week count
	 * @param user the user
	 * @param count the count
	 * @param year the year
	 * @param startTime the start time
	 */
	//added drive starttime
	public TestDriveAggregationWrapper(Integer week,Long weekCount, User user, Long count, Integer year,Date startTime) {
		this.week = week;
		this.count = count;
		this.year = year;
		if (user != null) {
			this.firstname = user.getFirstName();
			this.lastname = user.getLastName();
			this.username = user.getUserName();
			this.userid = user.getUserid();
			this.weekCount=weekCount;
		}
		this.startTime=startTime;
	}


	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

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
	 * Sets the user.
	 *
	 * @param user the new user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Gets the count.
	 *
	 * @return the count
	 */
	public Long getCount() {
		return count;
	}

	/**
	 * Sets the count.
	 *
	 * @param count the new count
	 */
	public void setCount(Long count) {
		this.count = count;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * Gets the firstname.
	 *
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * Sets the firstname.
	 *
	 * @param firstname the new firstname
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * Gets the lastname.
	 *
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * Sets the lastname.
	 *
	 * @param lastname the new lastname
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 *
	 * @param username the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the userid.
	 *
	 * @return the userid
	 */
	public Integer getUserid() {
		return userid;
	}

	/**
	 * Sets the userid.
	 *
	 * @param userid the new userid
	 */
	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	
	
	
	/**
	 * Gets the week count.
	 *
	 * @return the week count
	 */
	public Long getWeekCount() {
		return weekCount;
	}

	/**
	 * Sets the week count.
	 *
	 * @param weekCount the new week count
	 */
	public void setWeekCount(Long weekCount) {
		this.weekCount = weekCount;
	}

	
	
	/**
	 * Gets the start time.
	 *
	 * @return the start time
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * Sets the start time.
	 *
	 * @param startTime the new start time
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "TestDriveAggregationWrapper [user=" + user + ", firstname=" + firstname + ", lastname=" + lastname + ", username=" + username + ", userid=" + userid + ", count=" + count + ", status="
				+ status + ", week=" + week + ", year=" + year + ", weekCount=" + weekCount + ", startTime=" + startTime + "]";
	}

	
}
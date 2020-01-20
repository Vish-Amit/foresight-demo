package com.inn.foresight.module.nv.dashboard.wrapper;

import com.inn.core.generic.wrapper.RestWrapper;

/** The Class UserCountWrapper. */
@RestWrapper
public class UserCountWrapper {
	
	/** The user 1. */
	private Long user1;
	
	/** The user 2. */
	private Long user2;
	
	/** The total user. */
	private Long totalUser;
	
	/** The user 1 percent. */
	private Double user1Percent;
	
	/** The user 2 percent. */
	private Double user2Percent; 
	
	
	/**
	 * Gets the user 1.
	 *
	 * @return the user1
	 */
	public Long getUser1() {
		return user1;
	}
	
	/**
	 * Sets the user 1.
	 *
	 * @param user1 the user1 to set
	 */
	public void setUser1(Long user1) {
		this.user1 = user1;
	}
	
	/**
	 * Gets the user 2.
	 *
	 * @return the user2
	 */
	public Long getUser2() {
		return user2;
	}
	
	/**
	 * Sets the user 2.
	 *
	 * @param user2 the user2 to set
	 */
	public void setUser2(Long user2) {
		this.user2 = user2;
	}
	
	/**
	 * Gets the total user.
	 *
	 * @return the totalUser
	 */
	public Long getTotalUser() {
		return totalUser;
	}
	
	/**
	 * Sets the total user.
	 *
	 * @param totalUser the totalUser to set
	 */
	public void setTotalUser(Long totalUser) {
		this.totalUser = totalUser;
	}
	
	/**
	 * Gets the user 1 percent.
	 *
	 * @return the user1Percent
	 */
	public Double getUser1Percent() {
		return user1Percent;
	}
	
	/**
	 * Sets the user 1 percent.
	 *
	 * @param user1Percent the user1Percent to set
	 */
	public void setUser1Percent(Double user1Percent) {
		this.user1Percent = user1Percent;
	}
	
	/**
	 * Gets the user 2 percent.
	 *
	 * @return the user2Percent
	 */
	public Double getUser2Percent() {
		return user2Percent;
	}
	
	/**
	 * Sets the user 2 percent.
	 *
	 * @param user2Percent the user2Percent to set
	 */
	public void setUser2Percent(Double user2Percent) {
		this.user2Percent = user2Percent;
	}
	

}

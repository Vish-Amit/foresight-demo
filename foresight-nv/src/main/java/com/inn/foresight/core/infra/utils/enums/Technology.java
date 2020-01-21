package com.inn.foresight.core.infra.utils.enums;

import com.inn.foresight.core.generic.utils.DisplayName;

/**
 * The Enum Technology.
 */
public enum Technology implements DisplayName {
	
	/** The t2g. */
	T2G("2G"),
	
	/** The t3g. */
	T3G("3G"),
	
	/** The lte. */
	LTE("LTE"),
	
	/** The gsm. */
	GSM("GSM"),
	
	/** The umts. */
	UMTS("UMTS"),
	
	/** The fdd5. */
	FDD5("FDD5"),
	
	/** The fdd10. */
	FDD10("FDD10"),
	
	/** The tdd20. */
	TDD20("TDD20"),
	
	/** The tdd10. */
	TDD10("TDD10"),
	
	/** The fdd. */
	FDD("FDD"),
	
	/** The tdd. */
	TDD("TDD"),
	
	WIFI("WiFi");

	/** The display name. */
	private String displayName;

	/**
	 * Instantiates the Technology.
	 *
	 * @param displayName
	 *            of Technology
	 */
	private Technology(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Gets the Technology of alarm.
	 *
	 * @return the Technology display Name
	 */
	public String displayName() {
		return displayName;
	}

}

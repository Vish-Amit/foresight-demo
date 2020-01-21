package com.inn.foresight.core.infra.utils.enums;

import com.inn.foresight.core.generic.utils.DisplayName;

/**
 * The Enum ServerType.
 */
public enum ServerType implements DisplayName {

	/** The active. */
	ACTIVE("Active"),

	/** The stand by. */
	STANDBY("Stand By");

	/** The display name. */
	private String displayName;

	/**
	 * Instantiates the ServerType.
	 *
	 * @param displayName
	 *            of ServerType
	 */
	private ServerType(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Gets the ServerType of alarm.
	 *
	 * @return the ServerType display Name
	 */
	public String displayName() {
		return displayName;
	}
}

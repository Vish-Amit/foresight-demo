package com.inn.foresight.core.infra.utils.enums;

import com.inn.foresight.core.generic.utils.DisplayName;

/** The Enum NEStatus. */
public enum NEStatus implements DisplayName {
	/** The connect. */
	CONNECT("On-Air"),

	/** The disconnect. */
	DISCONNECT("Offline"),

	/** The onair. */
	ONAIR("ONAIR"),
	
	/** The Ready for Service. */
	READYFORSERVICE("READYFORSERVICE"),
	
	/** The planned. */
	PLANNED("PLANNED"),
	
	/** The scope. */
	SCOPE("SCOPE"),
	
	/** The decommission. */
	DECOMMISSION("DECOMMISSION"),
	
	/** The nonradiating. */
	NONRADIATING("NONRADIATING"),
	
	/** The nominal. */
	NOMINAL("NOMINAL"),
	
	/** The decommissioned. */
	DECOMMISSIONED("DECOMMISSIONED"),
	
	/** The EMS. */
	RFS("RFS"),

	/** The active. */
	ACTIVE("ACTIVE"),

	/** The standby. */
	STANDBY("STANDBY"),

	/** The dismental. */
	DISMENTAL("DISMENTAL"),
	
	INSTANTIATED("INSTANTIATED"),

	/** The inservice. */
	INSERVICE("INSERVICE"),
	
	/** The Enabled. */
	ENABLED("ENABLED"),
	
	/** The Disabled. */
	DISABLED("DISABLED");


	/** The display name. */
	private String displayName;

	/**
	 * Instantiates a new NE status.
	 *
	 * @param displayName the display name
	 */
	private NEStatus(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Display name.
	 *
	 * @return the string
	 */
	@Override
	public String displayName() {
		return displayName;
	}
}

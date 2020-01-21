package com.inn.foresight.core.infra.utils.enums;

import com.inn.foresight.core.generic.utils.DisplayName;

/** The Enum Domain. */
public enum Domain implements DisplayName {
	/** The ran. */
	RAN("RAN"),
	
	/** The application. */
	APPLICATION("APPLICATION"),

	/** The ip. */
	IP("IP"),

	/** The core. */
	CORE("CORE"),

	/** The wifi. */
	WIFI("WIFI"),

	/** The mw. */
	MW("MW"),

	/** The power. */
	POWER("POWER"),

	/** The microwave. */
	MICROWAVE("MICROWAVE"),

	/** The ipran. */
	IPRAN("IPRAN"),

	/** The ipbb. */
	IPBB("IPBB"),

	/** The ims. */
	IMS("IMS"),

	/** The it. */
	IT("IT"),

	/** The vas. */
	VAS("VAS"),

	/** The baremetal. */
	BAREMETAL("BAREMETAL"),

	/** The transport. */
	TRANSPORT("TRANSPORT"),

	/** The infra. */
	INFRA("INFRA"),
	
	/** The igw. */
	IGW("IGW"),
	
	/** The transmission. */
	TRANSMISSION("TRANSMISSION"),
	
	/** The apic. */
	APIC("APIC"),
	
	/** The iot. */
	IOT("IOT"),
	
	/** The security. */
	SECURITY("SECURITY"),

	/** The oss. */
	OSS("OSS");

	/** The display name. */
	private String displayName;

	/**
	 * Instantiates the Domain ex: RAN, IP etc..
	 *
	 * @param displayName the network element Domain
	 */
	private Domain(String displayName) {
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

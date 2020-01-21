package com.inn.foresight.core.infra.utils.enums;

import com.inn.foresight.core.generic.utils.DisplayName;

/** The Enum Vendor. */
public enum Vendor implements DisplayName {
	/** The samsung. */
	SAMSUNG("Samsung"),

	/** The airspan. */
	AIRSPAN("Airspan"),
	
	/** The radcom. */
	RADCOM("RADCOM"),

	/** The nokia. */
	NOKIA("Nokia"),
	
	/** The null. */
	NULL(""),


	/** The zte. */
	ZTE("ZTE"),

	/** The juniper. */
	JUNIPER("Juniper"),
	
	/** The desan. */
	DESAN("Desan"),
	
	/** The generic. */
	GENERIC("Generic"), 

	/** The ceragon. */
	CERAGON("Ceragon"),

	/** The maviner. */
	MAVINER("Maviner"),

	/** The mavenir. */
	MAVENIR("Mavenir"),

	/** The cisco. */
	CISCO("Cisco"),

	/** The sercomm. */
	SERCOMM("Sercomm"),

	/** The Huawei. */
	HUAWEI("Huawei"),

	/** The T-MOB. */
	TMOB("T-MOB"),

	/** The Ericsson. */
	ERICSSON("Ericsson"),

	/** The NSN. */
	NSN("NSN"),

	/** The RUCKUS. */
	RUCKUS("Ruckus"),

	/** The NEC. */
	NEC("NEC"),

	/** The GENBAND. */
	GENBAND("GENBAND"),

	/** The altiostar. */
	ALTIOSTAR("ALTIOSTAR"),

	/** The all. */
	ALL("ALL"),

	/** The netact. */
	NETACT("Netact"),

	/** The oki. */
	OKI("OKI"),

	/** The allot. */
	ALLOT("ALLOT"),

	/** The ciena. */
	CIENA("Ciena"),

	/** The zabbix. */
	ZABBIX("Zabbix"),

	/** The quanta. */
	QUANTA("QUANTA"),

	/** The cambium. */
	CAMBIUM("CAMBIUM"),

	/** The icode. */
	ICODE("ICODE"),

	/** The schneider. */
	SCHNEIDER("SCHNEIDER"),

	/** The dzs. */
	
	DZS("DZS"),

	/** The mitel. */
	MITEL("MITEL"),

	/** The sf. */
	SF("SF"),

	/** The it. */
	IT("IT"),

	/** The dialogic. */
	DIALOGIC("DIALOGIC"),

	/** The rim. */
	RIM("RIM"),

	/** The akamai. */
	AKAMAI("AKAMAI"),

	/** The google. */
	GOOGLE("GOOGLE"),

	/** The sidola. */
	SIDOLA("SIDOLA"),

	/** The telco bridges. */
	TELCO_BRIDGES("TELCO_BRIDGES"),
	
	/** The hitenet. */
	HITENET("HITENET"),

	/** The nokia netact. */
	NOKIA_NETACT("NOKIA_NETACT"),
	
	DASAN("DASAN"),

	/** The Inno eye NEC. */
	InnoEye_NEC("InnoEye-NEC"),
	
	/** The Ribbon. */
	Ribbon("Ribbon");

	/** The display name. */
	private String displayName;

	/**
	 * Instantiates the network element Vendor ex: Samsung, Nokia etc..
	 *
	 * @param displayName the network element vendor
	 */
	private Vendor(String displayName) {
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

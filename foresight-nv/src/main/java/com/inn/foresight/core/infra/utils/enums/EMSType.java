package com.inn.foresight.core.infra.utils.enums;

import com.inn.foresight.core.generic.utils.DisplayName;

/**
 * The Enum EMSType.
 */
public enum EMSType implements DisplayName {

	/** The cisco epnm. */
	CISCO_EPNM("Cisco EPNM"),

	/** The samsung lsmr. */
	SAMSUNG_LSMR("Samsung LSMR"),

	/** The airspan netspan. */
	AIRSPAN_NETSPAN("Airspan Netspan"),

	/** The lsmr. */
	LSMR("LSMR"),

	/** The lsmc. */
	LSMC("LSMC"),
	
	/** The voma. */
	VOMA("Voma"),

	/** The nokia. */
	NOKIA("Nokia"),

	/** The JUNIPER. */
	JUNIPER("JUNIPER"),
	
	CVIMMON("CVIMMON"),

	/** The zte. */
	ZTE("ZTE"),

	/** The ceragon. */
	CERAGON("CERAGON"),

	/** The VIM(openstack). */
	VIM("VIM"),
	
	/** The VIM_MANAGER(OpenStack Manager). */
	VIM_MANAGER("VIM_MANAGER"),

	/** The NETACT. */
	NETACT("NETACT"),

	/** CISCO EPC */
	EPC("EPC"),
	
	/** The ZABBIX. */
	ZABBIX("ZABBIX"),

	/** The EPNMANAGER. */
	EPNMANAGER("EPNMANAGER"),
	
	CIENA_BLUE_PLANET_MCP("CIENA_BLUE_PLANET_MCP"),


	CM_FILE_SFTP("CM_FILE_SFTP"),

	CM_EXECUTION("CM_EXECUTION"),

	CM_RET_SFTP("CM_RET_SFTP"),
	
	PM_ZTE_MW("PM_ZTE_MW"),

	TOPOLOGY_MW_NETWORKELEMENT_REPORT("TOPOLOGY_MW_NETWORKELEMENT_REPORT"), TOPOLOGY_IPBB_IP_INFO(
			"TOPOLOGY_IPBB_IP_INFO"), TOPOLOGY_IPBB_IP_SLOT_INFO(
					"TOPOLOGY_IPBB_IP_SLOT_INFO"), TOPOLOGY_IPBB_IP_SUBCARD_INFO(
							"TOPOLOGY_IPBB_IP_SUBCARD_INFO"), TOPOLOGY_IPRAN_ZTE_NODE(
									"TOPOLOGY_IPRAN_ZTE_NODE"), TOPOLOGY_IPRAN_ZTE_EQUIPMENT(
											"TOPOLOGY_IPRAN_ZTE_EQUIPMENT"), TOPOLOGY_MW_ZTE_LINK_INFO_FILE(
													"TOPOLOGY_MW_ZTE_LINK_INFO_FILE"), TOPOLOGY_MW_CERAGON_FULL_LINK_REPORT(
															"TOPOLOGY_MW_CERAGON_FULL_LINK_REPORT"),TOPOLOGY_BN_MANAGED_ELEMENT("TOPOLOGY_BN_MANAGED_ELEMENT"),TOPOLOGY_ZTE_POWER_INFO("TOPOLOGY_ZTE_POWER_INFO"),
	ZTE_CORE_VIRTUAL_EQUIPMENT("ZTE_CORE_VIRTUAL_EQUIPMENT")
	,CA("CA"),PM_NOKIA_CORE("PM_NOKIA_CORE"),PM_NOKIA_RAN("PM_NOKIA_RAN"),PM_CERAGON_MW("PM_CERAGON_MW"),CM_SWFILE_SFTP("CM_SWFILE_SFTP")
	,PM_AIRSPAN("PM_AIRSPAN"),EQUIPMENT_HOLDER_SFTP("EQUIPMENT_HOLDER_SFTP"),PM_ZTE_CORE("PM_ZTE_CORE"),PM_ZTE_RAN("PM_ZTE_RAN")
	,ZTE_IPBB("ZTE_IPBB"),Equipment_ZTE_CORE_Legacy("Equipment_ZTE_CORE_Legacy"),Equipment_ZTE_CDMA("Equipment_ZTE_CDMA"),Topology_Nokia_Core("'Topology_Nokia_Core"),TOPOLOGY_ZTE_MW_INVENTORY("TOPOLOGY_ZTE_MW_INVENTORY"),DASAN("DASAN"),DCNM("DCNM"),
	ALTIOSTAR_RAN("ALTIOSTAR_RAN"),XMS("XMS"),CMS("CMS"),OF("OF");

	/** The display name. */
	private String displayName;

	/**
	 * Instantiates the EmsType.
	 *
	 * @param displayName
	 *            of EmsType
	 */
	private EMSType(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Gets the EmsType of alarm.
	 *
	 * @return the EmsType display Name
	 */
	public String displayName() {
		return displayName;
	}
}

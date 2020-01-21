package com.inn.foresight.core.report;

public enum TemplateType {


	/** The nv ssvt. */
	NV_SSVT("NVSSVT"),
	/** The nv live drive. */
	NV_LIVE_DRIVE("NVLIVEDRIVE"),
	/** The nv drive. */
	NV_ADHOC_LD("NVADHOC"),
	/** The nv drive. */
	NV_DRIVE("NV_DRIVE"),
	/** The nv clot. */
	NV_CLOT("NVCLOT"),
	/** The nv opendrive. */
	NV_OPENDRIVE("NVOPENDRIVE"),
	/** The nv opendrive. */
	NV_ADHOC_OD("NVADHOC"),
	/** The nv stationary. */
	NV_STATIONARY("NVSTATIONARY"),
	/** The nv inbuilding. */
	NV_INBUILDING("NVINBUILDING"),
	/** The nv brti. */
	NV_ADHOC_IB("NVADHOC"),
	/** The nv brti. */
	NV_BRTI("NVBRTI"),
	/** The nv brti. */
	NV_ADHOC_BRTI_DRIVE("NVADHOC"),
	/** The nv brti. */
	NV_ADHOC_BRTI_ST("NVADHOC"),
	/** The benchmark. */
	NV_BENCHMARK("NVBENCHMARK"),
	/** The NV InBuilding benchmark. */
	NV_IB_BENCHMARK("NVADHOC"),
	/** The nv complaints. */
	NV_COMPLAINTS("NVCOMPLAINTS"),
	/** The nv complaints. */
	NV_STEALTH("NVSTEALTH"),
	
	/** The cm comparision with other. */
	CM_COMPARISION_WITH_OTHER("CM_COMPARISION_WITH_OTHER"),
	/** The cm comparision with self. */
	CM_COMPARISION_WITH_SELF("CM_COMPARISION_WITH_SELF");

	/** The value. */
	private String value;

	/**
	 * Instantiates a new template type.
	 *
	 * @param value
	 *            the value
	 */
	private TemplateType(String value) {
		this.value = value;
	}

	/**
	 * Instantiates a new template type.
	 */
	private TemplateType() {
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value
	 *            the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}

}

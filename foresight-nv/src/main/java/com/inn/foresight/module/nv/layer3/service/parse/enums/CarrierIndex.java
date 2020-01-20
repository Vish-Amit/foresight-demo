package com.inn.foresight.module.nv.layer3.service.parse.enums;


/**
 * The Enum CarrierIndex.
 *
 * @author innoeye
 * date - 19-Jan-2018 2:57:25 PM
 */
public enum CarrierIndex {

	/** The pcc. */
	PCC(1),
/** The scc. */
SCC(2);

	/** The value. */
	private final Integer value;

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public Integer getValue() {
		return value;
	}

	/**
	 * Instantiates a new carrier index.
	 *
	 * @param value the value
	 */
	private CarrierIndex(Integer value) {
		this.value = value;
	}

}

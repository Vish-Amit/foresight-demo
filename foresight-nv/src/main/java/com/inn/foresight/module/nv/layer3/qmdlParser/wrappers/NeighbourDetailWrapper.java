package com.inn.foresight.module.nv.layer3.qmdlParser.wrappers;

public class NeighbourDetailWrapper  implements Comparable<NeighbourDetailWrapper> {
	

	private Double instRsrp;
	private Double instRsrq;
	private Double instRssi;
	private Integer pci;
	private Double lat;
	private Double lng;

	/** The inst rsrq rx 0. */
	private Double instRsrqRx0;

	/** The inst rsrq rx 1. */
	private Double instRsrqRx1;

	/** The inst rssi rx 0. */
	private Double instRssiRx0;

	/** The inst rssi rx 1. */
	private Double instRssiRx1;

	/** The inst rsrp rx 0. */
	private Double instRsrpRx0;

	/** The inst rsrp rx 1. */
	private Double instRsrpRx1;

	public Double getInstRsrp() {
		return instRsrp;
	}
	public void setInstRsrp(Double instRsrp) {
		this.instRsrp = instRsrp;
	}
	public Double getInstRsrq() {
		return instRsrq;
	}
	public void setInstRsrq(Double instRsrq) {
		this.instRsrq = instRsrq;
	}
	public Double getInstRssi() {
		return instRssi;
	}
	public void setInstRssi(Double instRssi) {
		this.instRssi = instRssi;
	}
	public Integer getPci() {
		return pci;
	}
	public void setPci(Integer pci) {
		this.pci = pci;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	/**
	 * @return the instRsrqRx0
	 */
	public Double getInstRsrqRx0() {
		return instRsrqRx0;
	}
	/**
	 * @param instRsrqRx0 the instRsrqRx0 to set
	 */
	public void setInstRsrqRx0(Double instRsrqRx0) {
		this.instRsrqRx0 = instRsrqRx0;
	}
	/**
	 * @return the instRsrqRx1
	 */
	public Double getInstRsrqRx1() {
		return instRsrqRx1;
	}
	/**
	 * @param instRsrqRx1 the instRsrqRx1 to set
	 */
	public void setInstRsrqRx1(Double instRsrqRx1) {
		this.instRsrqRx1 = instRsrqRx1;
	}
	/**
	 * @return the instRssiRx0
	 */
	public Double getInstRssiRx0() {
		return instRssiRx0;
	}
	/**
	 * @param instRssiRx0 the instRssiRx0 to set
	 */
	public void setInstRssiRx0(Double instRssiRx0) {
		this.instRssiRx0 = instRssiRx0;
	}
	/**
	 * @return the instRssiRx1
	 */
	public Double getInstRssiRx1() {
		return instRssiRx1;
	}
	/**
	 * @param instRssiRx1 the instRssiRx1 to set
	 */
	public void setInstRssiRx1(Double instRssiRx1) {
		this.instRssiRx1 = instRssiRx1;
	}
	/**
	 * @return the instRsrpRx0
	 */
	public Double getInstRsrpRx0() {
		return instRsrpRx0;
	}
	/**
	 * @param instRsrpRx0 the instRsrpRx0 to set
	 */
	public void setInstRsrpRx0(Double instRsrpRx0) {
		this.instRsrpRx0 = instRsrpRx0;
	}
	/**
	 * @return the instRsrpRx1
	 */
	public Double getInstRsrpRx1() {
		return instRsrpRx1;
	}
	/**
	 * @param instRsrpRx1 the instRsrpRx1 to set
	 */
	public void setInstRsrpRx1(Double instRsrpRx1) {
		this.instRsrpRx1 = instRsrpRx1;
	}

	@Override
	public int compareTo(NeighbourDetailWrapper neighbourDetailWrapper) {
		if (neighbourDetailWrapper.getInstRsrp()>this.getInstRsrp()){
			return 1;
		}
		if (neighbourDetailWrapper.getInstRsrp()<this.getInstRsrp()){
			return -1;
		}
		else{
			return 0;
		}		
	}

}

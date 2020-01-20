package com.inn.foresight.module.nv.report.wrapper;

/**
 * @author ist
 */
public class SSVTSectorWiseWrapper {

	String cellId;
	String azimuth;
	String mTilt;
	String eTilt;
	String pci;
	String earfcn;

	public String getCellId() {
		return cellId;
	}

	public void setCellId(String cellId) {
		this.cellId = cellId;
	}

	public String getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(String azimuth) {
		this.azimuth = azimuth;
	}

	public String getmTilt() {
		return mTilt;
	}

	public void setmTilt(String mTilt) {
		this.mTilt = mTilt;
	}

	public String geteTilt() {
		return eTilt;
	}

	public void seteTilt(String eTilt) {
		this.eTilt = eTilt;
	}

	public String getPci() {
		return pci;
	}

	public void setPci(String pci) {
		this.pci = pci;
	}

	public String getEarfcn() {
		return earfcn;
	}

	public void setEarfcn(String earfcn) {
		this.earfcn = earfcn;
	}
}

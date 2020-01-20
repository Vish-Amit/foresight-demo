package com.inn.foresight.module.nv.livedrive.wrapper;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.core.generic.wrapper.RestWrapper;

/** The Class TrackSpanWrapper. */
@JsonIgnoreProperties(ignoreUnknown = true)
@RestWrapper
public class TrackSpanWrapper {

	/** The userid. */
	private Integer userid;

	/** The imei. */
	private String imei;

	/** The driveid. */
	private Integer driveid;

	/** The status. */
	private String status;

	private String nvAppVersion;
	private String manufactureBy;
	private String model;
	private String androidOs;
	private Integer recipeId;

	/** The spanlist. */
	private List<TrackSpan> spanlist;

	/**
	 * Gets the userid.
	 *
	 * @return the userid
	 */
	public Integer getUserid() {
		return userid;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the userid.
	 *
	 * @param userid
	 *            the new userid
	 */
	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	/**
	 * Gets the imei.
	 *
	 * @return the imei
	 */
	public String getImei() {
		return imei;
	}

	/**
	 * Sets the imei.
	 *
	 * @param imei
	 *            the new imei
	 */
	public void setImei(String imei) {
		this.imei = imei;
	}

	/**
	 * Gets the driveid.
	 *
	 * @return the driveid
	 */
	public Integer getDriveid() {
		return driveid;
	}

	/**
	 * Sets the driveid.
	 *
	 * @param driveid
	 *            the new driveid
	 */
	public void setDriveid(Integer driveid) {
		this.driveid = driveid;
	}

	/**
	 * Gets the spanlist.
	 *
	 * @return the spanlist
	 */
	public List<TrackSpan> getSpanlist() {
		return spanlist;
	}

	/**
	 * Sets the spanlist.
	 *
	 * @param spanlist
	 *            the new spanlist
	 */
	public void setSpanlist(List<TrackSpan> spanlist) {
		this.spanlist = spanlist;
	}

	/**
	 * Sets the status.
	 *
	 * @param status
	 *            the new status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getNvAppVersion() {
		return nvAppVersion;
	}

	public void setNvAppVersion(String nvAppVersion) {
		this.nvAppVersion = nvAppVersion;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getAndroidOs() {
		return androidOs;
	}

	public void setAndroidOs(String androidOs) {
		this.androidOs = androidOs;
	}

	public String getManufactureBy() {
		return manufactureBy;
	}

	public void setManufactureBy(String manufactureBy) {
		this.manufactureBy = manufactureBy;
	}

	public Integer getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(Integer recipeId) {
		this.recipeId = recipeId;
	}

	@Override
	public String toString() {
		return "TrackSpanWrapper [userid=" + userid + ", imei=" + imei + ", driveid=" + driveid + ", status=" + status
				+ ", nvAppVersion=" + nvAppVersion + ", manufactureBy=" + manufactureBy + ", model=" + model
				+ ", androidOs=" + androidOs + ", recipeId=" + recipeId + ", spanlist=" + spanlist + "]";
	}
}

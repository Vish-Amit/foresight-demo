package com.inn.foresight.module.nv.feedback.wrapper;

import java.io.Serializable;
import java.util.Date;

import com.inn.commons.lang.StringUtils;
import com.inn.foresight.core.generic.utils.ForesightConstants;

public class CustomFeedbackResponseWrapper implements Serializable {


	private static final long serialVersionUID = 1L;

	private Long count;
	private String grid;
	private Integer rating;

	private String make;
	private String model;
	private String nvmodule;
	private String deviceid;
	private Double latitude;
	private Double longitude;
	private String versionname;

	private String reason;
	private Date feedbackDate;
	private String locationType;
	private String feedbackAddress;

	/** Getting data for getGridWiseFeedbackCount for zoom level up to 5 to 14 */
	public CustomFeedbackResponseWrapper(Long count, String grid, Integer rating) {
		super();
		this.count = count;
		this.grid = grid;
		this.rating = rating;
	}

	/**
	 * Constructor for getGridWiseFeedbackCount method for zoom level up to 15 to 18
	 */
	public CustomFeedbackResponseWrapper(String reason, String locationType, Double latitude, Double longitude, String feedbackAddress,
			String deviceid, String nvmodule, String versionname, Integer rating, Date feedbackDate, String make, String model) {
		super();
		this.reason = StringUtils.join(reason.split(ForesightConstants.COMMA+ForesightConstants.SPACE), ForesightConstants.COMMA+ForesightConstants.SPACE);
		this.locationType = locationType;
		this.latitude = latitude;
		this.longitude = longitude;
		this.feedbackAddress = feedbackAddress;
		this.deviceid = deviceid;
		this.nvmodule = nvmodule;
		this.versionname = versionname;
		this.rating = rating;
		this.feedbackDate = feedbackDate;
		this.make = make;
		this.model = model;

	}

	public CustomFeedbackResponseWrapper() {
		super();
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getGrid() {
		return grid;
	}

	public void setGrid(String grid) {
		this.grid = grid;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getNvmodule() {
		return nvmodule;
	}

	public void setNvmodule(String nvmodule) {
		this.nvmodule = nvmodule;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getVersionname() {
		return versionname;
	}

	public void setVersionname(String versionname) {
		this.versionname = versionname;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getFeedbackDate() {
		return feedbackDate;
	}

	public void setFeedbackDate(Date feedbackDate) {
		this.feedbackDate = feedbackDate;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getFeedbackAddress() {
		return feedbackAddress;
	}

	public void setFeedbackAddress(String feedbackAddress) {
		this.feedbackAddress = feedbackAddress;
	}

	@Override
	public String toString() {
		return "CustomFeedbackResponseWrapper [count=" + count + ", grid=" + grid + ", rating=" + rating + ", make=" + make + ", model=" + model
				+ ", nvmodule=" + nvmodule + ", deviceid=" + deviceid + ", latitude=" + latitude + ", longitude=" + longitude + ", versionname="
				+ versionname + ", reason=" + reason + ", feedbackDate=" + feedbackDate + ", locationType=" + locationType + ", feedbackAddress="
				+ feedbackAddress + "]";
	}

}

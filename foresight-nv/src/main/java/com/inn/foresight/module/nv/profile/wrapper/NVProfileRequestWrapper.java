package com.inn.foresight.module.nv.profile.wrapper;

/**
 * The Class NVProfileRequestWrapper.
 *
 * @author innoeye
 * date - 09-Mar-2018 3:58:13 PM
 */
public class NVProfileRequestWrapper {

	/** The mcc. */
	private Integer mcc;

	/** The mnc. */
	private Integer mnc;

	/** The cell id. */
	private Integer cellId;

	/** The make. */
	private String make;

	/** The model. */
	private String model;

	/** The os. */
	private String os;

	/** The latitude. */
	private Double latitude;

	/** The longitude. */
	private Double longitude;

	/** The device id. */
	private String deviceId;
	
	/** The module. */
	private String module;
	
	/** The app version. */
	private String appVersion;

	/**
	 * Gets the mcc.
	 *
	 * @return the mcc
	 */
	public Integer getMcc() {
		return mcc;
	}

	/**
	 * Sets the mcc.
	 *
	 * @param mcc the new mcc
	 */
	public void setMcc(Integer mcc) {
		this.mcc = mcc;
	}

	/**
	 * Gets the mnc.
	 *
	 * @return the mnc
	 */
	public Integer getMnc() {
		return mnc;
	}

	/**
	 * Sets the mnc.
	 *
	 * @param mnc the new mnc
	 */
	public void setMnc(Integer mnc) {
		this.mnc = mnc;
	}

	/**
	 * Gets the cell id.
	 *
	 * @return the cell id
	 */
	public Integer getCellId() {
		return cellId;
	}

	/**
	 * Sets the cell id.
	 *
	 * @param cellId the new cell id
	 */
	public void setCellId(Integer cellId) {
		this.cellId = cellId;
	}

	/**
	 * Gets the make.
	 *
	 * @return the make
	 */
	public String getMake() {
		return make;
	}

	/**
	 * Sets the make.
	 *
	 * @param make the new make
	 */
	public void setMake(String make) {
		this.make = make;
	}

	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Sets the model.
	 *
	 * @param model the new model
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * Gets the os.
	 *
	 * @return the os
	 */
	public String getOs() {
		return os;
	}

	/**
	 * Sets the os.
	 *
	 * @param os the new os
	 */
	public void setOs(String os) {
		this.os = os;
	}

	/**
	 * Gets the latitude.
	 *
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * Sets the latitude.
	 *
	 * @param latitude the new latitude
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * Gets the longitude.
	 *
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * Sets the longitude.
	 *
	 * @param longitude the new longitude
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * Gets the device id.
	 *
	 * @return the device id
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * Sets the device id.
	 *
	 * @param deviceId the new device id
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * Gets the module.
	 *
	 * @return the module
	 */
	public String getModule() {
		return module;
	}

	/**
	 * Sets the module.
	 *
	 * @param module the new module
	 */
	public void setModule(String module) {
		this.module = module;
	}

	/**
	 * @return the appVersion
	 */
	public String getAppVersion() {
		return appVersion;
	}

	/**
	 * @param appVersion the appVersion to set
	 */
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	@Override
	public String toString() {
		return "NVProfileRequestWrapper [mcc=" + mcc + ", mnc=" + mnc + ", cellId=" + cellId + ", make=" + make
				+ ", model=" + model + ", os=" + os + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", deviceId=" + deviceId + ", module=" + module + ", appVersion=" + appVersion + "]";
	}

}
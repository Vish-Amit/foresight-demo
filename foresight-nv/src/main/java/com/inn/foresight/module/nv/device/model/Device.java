package com.inn.foresight.module.nv.device.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.module.nv.device.constant.DeviceConstant;


/** The Class Device. */
@NamedQueries({
	 @NamedQuery(name = DeviceConstant.GET_DEVICE_SPECIFICATION_BY_MODEL, query = "select d from Device d where d.modelName =:modelName"),
	 @NamedQuery(name = DeviceConstant.GET_ALL_DEVICE_LIST, query = "select d from Device d"),
	 @NamedQuery(name = DeviceConstant.GET_DEVICE_DETAIL_BY_MODEL_NAME, query = "select d from Device d where d.modelName=:modelName"),

})
@Entity
@Table(name = "Device")
@XmlRootElement(name = "Device")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class Device implements Serializable {
	
	/** * The Constant serialVersionUID. */
	private static final long serialVersionUID = 7719132699015775188L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "deviceid_pk")
	private Integer id;

	/** The make. */
	@Column(name = "modelname")
	private String modelName;

	/** The creation time. */
	@Column(name = "creationtime")
	private Date creationTime;

	/** The modification time. */
	@Column(name = "modificationtime")
	private Date modificationTime;
	
	/** The band 2 g. */
	@Column(name = "band2g")
	private String band2g;
	
	/** The band 3 g. */
	@Column(name = "band3g")
	private String band3g;
	
	/** The band 4 g. */
	@Column(name = "band4g")
	private String band4g;
	
	/** The battery. */
	@Column(name = "battery")
	private String battery;
	
	/** The sim. */
	@Column(name = "sim")
	private String sim;
	
	/** The wlan. */
	@Column(name = "wlan")
	private String wlan;
	
	/** The technology. */
	@Column(name = "technology")
	private String technology;
	
	/** The speed. */
	@Column(name = "speed")
	private String speed;
	
	/** The gprs. */
	@Column(name = "gprs")
	private String gprs;
	
	/** The edge. */
	@Column(name = "edge")
	private String edge;
	
	/** The announced. */
	@Column(name = "announced")
	private String announced;
	
	/** The status. */
	@Column(name = "status")
	private String status;
	
	/** The dimension. */
	@Column(name = "dimension")
	private String dimension;
	
	/** The display type. */
	@Column(name = "displaytype")
	private String displayType;
	
	/** The resolution. */
	@Column(name = "resolution")
	private String resolution;
	
	/** The multitouch. */
	@Column(name = "multitouch")
	private String multitouch;
	
	/** The os. */
	@Column(name = "os")
	private String os;
	
	/** The chipset. */
	@Column(name = "chipset")
	private String chipset;
	
	/** The cpu. */
	@Column(name = "cpu")
	private String cpu;
	
	/** The gpu. */
	@Column(name = "gpu")
	private String gpu;
	
	/** The cardslot. */
	@Column(name = "cardslot")
	private String cardslot;
	
	/** The internal memory. */
	@Column(name = "internalmemory")
	private String internalMemory;
	
	/** The primary camera. */
	@Column(name = "primarycamera")
	private String primaryCamera;
	
	/** The feature. */
	@Column(name = "feature")
	private String feature;
	
	/** The video. */
	@Column(name = "video")
	private String video;
	
	/** The secondary camera. */
	@Column(name = "secondarycamera")
	private String secondaryCamera;
	
	/** The devicesize. */
	@Column(name = "devicesize")
	private String devicesize;
	
	/** The alert type. */
	@Column(name = "alerttype")
	private String alertType;
	
	/** The loudspeaker. */
	@Column(name = "loudspeaker")
	private String loudspeaker;
	
	/** The bluetooth. */
	@Column(name = "bluetooth")
	private String bluetooth;
	
	/** The gps. */
	@Column(name = "gps")
	private String gps;
	
	/** The radio. */
	@Column(name = "radio")
	private String radio;
	
	
	/** The usb. */
	@Column(name = "usb")
	private String usb;
	
	/** The sensor. */
	@Column(name = "sensor")
	private String sensor;
	
	/** The messaging. */
	@Column(name = "messaging")
	private String messaging;
	
	/** The browser. */
	@Column(name = "browser")
	private String browser;
	
	/** The java. */
	@Column(name = "java")
	private String java;
	
	/** The color. */
	@Column(name = "color")
	private String color;
	
	/** The jack 35 mm. */
	@Column(name = "jack35mm")
	private String jack35mm;
	
	/** The weight. */
	@Column(name = "weight")
	private String weight;
	
	/** The brand. */
	@Column(name = "brand")
	private String brand;
	
	/** The standby. */
	@Column(name = "standby")
	private String standby;
	
	/** The talktime. */
	@Column(name = "talktime")
	private String talktime;
	
	/** The musicplay. */
	@Column(name = "musicplay")
	private String musicplay;
	
	/** The price group. */
	@Column(name = "pricegroup")
	private String priceGroup;
	
	/** The sar us. */
	@Column(name = "sarus")
	private String sarUs;
	
	/** The sar eu. */
	@Column(name = "sareu")
	private String sarEu;
	
	/** The model code. */
	@Column(name = "modelcode")
	private String modelCode;
	
	/** The guid. */
	@Column(name = "guid")
	private String guid;
	
	/** The volte support. */
	@Column(name = "voltesupport")
	private String volteSupport;
	
	/** The sim configuration. */
	@Column(name = "simconfiguration")
	private String simConfiguration;
	
	/** The video call support. */
	@Column(name = "videocallsupport")
	private String videoCallSupport;
	
	/** The firmware version. */
	@Column(name = "firmwareversion")
	private String firmwareVersion;
	
	/** The image url. */
	@Column(name = "imageurl")
	private String imageUrl;
	
	@Column(name = "tacfac")
	private Integer tacFac;
	
	
	public Integer getTacFac() {
		return tacFac;
	}

	public void setTacFac(Integer tacFac) {
		this.tacFac = tacFac;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the model name.
	 *
	 * @return the modelName
	 */
	public String getModelName() {
		return modelName;
	}

	/**
	 * Sets the model name.
	 *
	 * @param modelName the modelName to set
	 */
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	/**
	 * Gets the creation time.
	 *
	 * @return the creationTime
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	/**
	 * Sets the creation time.
	 *
	 * @param creationTime the creationTime to set
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * Gets the modification time.
	 *
	 * @return the modificationTime
	 */
	public Date getModificationTime() {
		return modificationTime;
	}

	/**
	 * Sets the modification time.
	 *
	 * @param modificationTime the modificationTime to set
	 */
	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	/**
	 * Gets the band 2 g.
	 *
	 * @return the band2g
	 */
	public String getBand2g() {
		return band2g;
	}

	/**
	 * Sets the band 2 g.
	 *
	 * @param band2g the band2g to set
	 */
	public void setBand2g(String band2g) {
		this.band2g = band2g;
	}

	/**
	 * Gets the band 3 g.
	 *
	 * @return the band3g
	 */
	public String getBand3g() {
		return band3g;
	}

	/**
	 * Sets the band 3 g.
	 *
	 * @param band3g the band3g to set
	 */
	public void setBand3g(String band3g) {
		this.band3g = band3g;
	}

	/**
	 * Gets the band 4 g.
	 *
	 * @return the band4g
	 */
	public String getBand4g() {
		return band4g;
	}

	/**
	 * Sets the band 4 g.
	 *
	 * @param band4g the band4g to set
	 */
	public void setBand4g(String band4g) {
		this.band4g = band4g;
	}

	/**
	 * Gets the battery.
	 *
	 * @return the battery
	 */
	public String getBattery() {
		return battery;
	}

	/**
	 * Sets the battery.
	 *
	 * @param battery the battery to set
	 */
	public void setBattery(String battery) {
		this.battery = battery;
	}

	/**
	 * Gets the sim.
	 *
	 * @return the sim
	 */
	public String getSim() {
		return sim;
	}

	/**
	 * Sets the sim.
	 *
	 * @param sim the sim to set
	 */
	public void setSim(String sim) {
		this.sim = sim;
	}

	/**
	 * Gets the wlan.
	 *
	 * @return the wlan
	 */
	public String getWlan() {
		return wlan;
	}

	/**
	 * Sets the wlan.
	 *
	 * @param wlan the wlan to set
	 */
	public void setWlan(String wlan) {
		this.wlan = wlan;
	}

	/**
	 * Gets the technology.
	 *
	 * @return the technology
	 */
	public String getTechnology() {
		return technology;
	}

	/**
	 * Sets the technology.
	 *
	 * @param technology the technology to set
	 */
	public void setTechnology(String technology) {
		this.technology = technology;
	}

	/**
	 * Gets the speed.
	 *
	 * @return the speed
	 */
	public String getSpeed() {
		return speed;
	}

	/**
	 * Sets the speed.
	 *
	 * @param speed the speed to set
	 */
	public void setSpeed(String speed) {
		this.speed = speed;
	}

	/**
	 * Gets the gprs.
	 *
	 * @return the gprs
	 */
	public String getGprs() {
		return gprs;
	}

	/**
	 * Sets the gprs.
	 *
	 * @param gprs the gprs to set
	 */
	public void setGprs(String gprs) {
		this.gprs = gprs;
	}

	/**
	 * Gets the edge.
	 *
	 * @return the edge
	 */
	public String getEdge() {
		return edge;
	}

	/**
	 * Sets the edge.
	 *
	 * @param edge the edge to set
	 */
	public void setEdge(String edge) {
		this.edge = edge;
	}

	/**
	 * Gets the announced.
	 *
	 * @return the announced
	 */
	public String getAnnounced() {
		return announced;
	}

	/**
	 * Sets the announced.
	 *
	 * @param announced the announced to set
	 */
	public void setAnnounced(String announced) {
		this.announced = announced;
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
	 * Sets the status.
	 *
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gets the dimension.
	 *
	 * @return the dimension
	 */
	public String getDimension() {
		return dimension;
	}

	/**
	 * Sets the dimension.
	 *
	 * @param dimension the dimension to set
	 */
	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	/**
	 * Gets the display type.
	 *
	 * @return the displayType
	 */
	public String getDisplayType() {
		return displayType;
	}

	/**
	 * Sets the display type.
	 *
	 * @param displayType the displayType to set
	 */
	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	/**
	 * Gets the resolution.
	 *
	 * @return the resolution
	 */
	public String getResolution() {
		return resolution;
	}

	/**
	 * Sets the resolution.
	 *
	 * @param resolution the resolution to set
	 */
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	/**
	 * Gets the multitouch.
	 *
	 * @return the multitouch
	 */
	public String getMultitouch() {
		return multitouch;
	}

	/**
	 * Sets the multitouch.
	 *
	 * @param multitouch the multitouch to set
	 */
	public void setMultitouch(String multitouch) {
		this.multitouch = multitouch;
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
	 * @param os the os to set
	 */
	public void setOs(String os) {
		this.os = os;
	}

	/**
	 * Gets the chipset.
	 *
	 * @return the chipset
	 */
	public String getChipset() {
		return chipset;
	}

	/**
	 * Sets the chipset.
	 *
	 * @param chipset the chipset to set
	 */
	public void setChipset(String chipset) {
		this.chipset = chipset;
	}

	/**
	 * Gets the cpu.
	 *
	 * @return the cpu
	 */
	public String getCpu() {
		return cpu;
	}

	/**
	 * Sets the cpu.
	 *
	 * @param cpu the cpu to set
	 */
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	/**
	 * Gets the gpu.
	 *
	 * @return the gpu
	 */
	public String getGpu() {
		return gpu;
	}

	/**
	 * Sets the gpu.
	 *
	 * @param gpu the gpu to set
	 */
	public void setGpu(String gpu) {
		this.gpu = gpu;
	}

	/**
	 * Gets the cardslot.
	 *
	 * @return the cardslot
	 */
	public String getCardslot() {
		return cardslot;
	}

	/**
	 * Sets the cardslot.
	 *
	 * @param cardslot the cardslot to set
	 */
	public void setCardslot(String cardslot) {
		this.cardslot = cardslot;
	}


	/**
	 * Gets the primary camera.
	 *
	 * @return the primaryCamera
	 */
	public String getPrimaryCamera() {
		return primaryCamera;
	}

	/**
	 * Sets the primary camera.
	 *
	 * @param primaryCamera the primaryCamera to set
	 */
	public void setPrimaryCamera(String primaryCamera) {
		this.primaryCamera = primaryCamera;
	}

	/**
	 * Gets the feature.
	 *
	 * @return the feature
	 */
	public String getFeature() {
		return feature;
	}

	/**
	 * Sets the feature.
	 *
	 * @param feature the feature to set
	 */
	public void setFeature(String feature) {
		this.feature = feature;
	}

	/**
	 * Gets the video.
	 *
	 * @return the video
	 */
	public String getVideo() {
		return video;
	}

	/**
	 * Sets the video.
	 *
	 * @param video the video to set
	 */
	public void setVideo(String video) {
		this.video = video;
	}

	/**
	 * Gets the secondary camera.
	 *
	 * @return the secondaryCamera
	 */
	public String getSecondaryCamera() {
		return secondaryCamera;
	}

	/**
	 * Sets the secondary camera.
	 *
	 * @param secondaryCamera the secondaryCamera to set
	 */
	public void setSecondaryCamera(String secondaryCamera) {
		this.secondaryCamera = secondaryCamera;
	}

	/**
	 * Gets the devicesize.
	 *
	 * @return the devicesize
	 */
	public String getDevicesize() {
		return devicesize;
	}

	/**
	 * Sets the devicesize.
	 *
	 * @param devicesize the devicesize to set
	 */
	public void setDevicesize(String devicesize) {
		this.devicesize = devicesize;
	}

	/**
	 * Gets the alert type.
	 *
	 * @return the alertType
	 */
	public String getAlertType() {
		return alertType;
	}

	/**
	 * Sets the alert type.
	 *
	 * @param alertType the alertType to set
	 */
	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}

	/**
	 * Gets the loudspeaker.
	 *
	 * @return the loudspeaker
	 */
	public String getLoudspeaker() {
		return loudspeaker;
	}

	/**
	 * Sets the loudspeaker.
	 *
	 * @param loudspeaker the loudspeaker to set
	 */
	public void setLoudspeaker(String loudspeaker) {
		this.loudspeaker = loudspeaker;
	}

	/**
	 * Gets the bluetooth.
	 *
	 * @return the bluetooth
	 */
	public String getBluetooth() {
		return bluetooth;
	}

	/**
	 * Sets the bluetooth.
	 *
	 * @param bluetooth the bluetooth to set
	 */
	public void setBluetooth(String bluetooth) {
		this.bluetooth = bluetooth;
	}

	/**
	 * Gets the gps.
	 *
	 * @return the gps
	 */
	public String getGps() {
		return gps;
	}

	/**
	 * Sets the gps.
	 *
	 * @param gps the gps to set
	 */
	public void setGps(String gps) {
		this.gps = gps;
	}

	/**
	 * Gets the radio.
	 *
	 * @return the radio
	 */
	public String getRadio() {
		return radio;
	}

	/**
	 * Sets the radio.
	 *
	 * @param radio the radio to set
	 */
	public void setRadio(String radio) {
		this.radio = radio;
	}

	/**
	 * Gets the usb.
	 *
	 * @return the usb
	 */
	public String getUsb() {
		return usb;
	}

	/**
	 * Sets the usb.
	 *
	 * @param usb the usb to set
	 */
	public void setUsb(String usb) {
		this.usb = usb;
	}

	/**
	 * Gets the sensor.
	 *
	 * @return the sensor
	 */
	public String getSensor() {
		return sensor;
	}

	/**
	 * Sets the sensor.
	 *
	 * @param sensor the sensor to set
	 */
	public void setSensor(String sensor) {
		this.sensor = sensor;
	}

	/**
	 * Gets the messaging.
	 *
	 * @return the messaging
	 */
	public String getMessaging() {
		return messaging;
	}

	/**
	 * Sets the messaging.
	 *
	 * @param messaging the messaging to set
	 */
	public void setMessaging(String messaging) {
		this.messaging = messaging;
	}

	/**
	 * Gets the browser.
	 *
	 * @return the browser
	 */
	public String getBrowser() {
		return browser;
	}

	/**
	 * Sets the browser.
	 *
	 * @param browser the browser to set
	 */
	public void setBrowser(String browser) {
		this.browser = browser;
	}

	/**
	 * Gets the java.
	 *
	 * @return the java
	 */
	public String getJava() {
		return java;
	}

	/**
	 * Sets the java.
	 *
	 * @param java the java to set
	 */
	public void setJava(String java) {
		this.java = java;
	}

	/**
	 * Gets the color.
	 *
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Sets the color.
	 *
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * Gets the jack 35 mm.
	 *
	 * @return the jack35mm
	 */
	public String getJack35mm() {
		return jack35mm;
	}

	/**
	 * Sets the jack 35 mm.
	 *
	 * @param jack35mm the jack35mm to set
	 */
	public void setJack35mm(String jack35mm) {
		this.jack35mm = jack35mm;
	}

	/**
	 * Gets the weight.
	 *
	 * @return the weight
	 */
	public String getWeight() {
		return weight;
	}

	/**
	 * Sets the weight.
	 *
	 * @param weight the weight to set
	 */
	public void setWeight(String weight) {
		this.weight = weight;
	}

	/**
	 * Gets the brand.
	 *
	 * @return the brand
	 */
	public String getBrand() {
		return brand;
	}

	/**
	 * Sets the brand.
	 *
	 * @param brand the brand to set
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}

	/**
	 * Gets the standby.
	 *
	 * @return the standby
	 */
	public String getStandby() {
		return standby;
	}

	/**
	 * Sets the standby.
	 *
	 * @param standby the standby to set
	 */
	public void setStandby(String standby) {
		this.standby = standby;
	}

	/**
	 * Gets the talktime.
	 *
	 * @return the talktime
	 */
	public String getTalktime() {
		return talktime;
	}

	/**
	 * Sets the talktime.
	 *
	 * @param talktime the talktime to set
	 */
	public void setTalktime(String talktime) {
		this.talktime = talktime;
	}

	/**
	 * Gets the musicplay.
	 *
	 * @return the musicplay
	 */
	public String getMusicplay() {
		return musicplay;
	}

	/**
	 * Sets the musicplay.
	 *
	 * @param musicplay the musicplay to set
	 */
	public void setMusicplay(String musicplay) {
		this.musicplay = musicplay;
	}

	/**
	 * Gets the price group.
	 *
	 * @return the priceGroup
	 */
	public String getPriceGroup() {
		return priceGroup;
	}

	/**
	 * Sets the price group.
	 *
	 * @param priceGroup the priceGroup to set
	 */
	public void setPriceGroup(String priceGroup) {
		this.priceGroup = priceGroup;
	}

	/**
	 * Gets the sar us.
	 *
	 * @return the sarUs
	 */
	public String getSarUs() {
		return sarUs;
	}

	/**
	 * Sets the sar us.
	 *
	 * @param sarUs the sarUs to set
	 */
	public void setSarUs(String sarUs) {
		this.sarUs = sarUs;
	}

	/**
	 * Gets the sar eu.
	 *
	 * @return the sarEu
	 */
	public String getSarEu() {
		return sarEu;
	}

	/**
	 * Sets the sar eu.
	 *
	 * @param sarEu the sarEu to set
	 */
	public void setSarEu(String sarEu) {
		this.sarEu = sarEu;
	}

	/**
	 * Gets the model code.
	 *
	 * @return the modelCode
	 */
	public String getModelCode() {
		return modelCode;
	}

	/**
	 * Sets the model code.
	 *
	 * @param modelCode the modelCode to set
	 */
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	/**
	 * Gets the guid.
	 *
	 * @return the guid
	 */
	public String getGuid() {
		return guid;
	}

	/**
	 * Sets the guid.
	 *
	 * @param guid the guid to set
	 */
	public void setGuid(String guid) {
		this.guid = guid;
	}

	/**
	 * Gets the volte support.
	 *
	 * @return the volteSupport
	 */
	public String getVolteSupport() {
		return volteSupport;
	}

	/**
	 * Sets the volte support.
	 *
	 * @param volteSupport the volteSupport to set
	 */
	public void setVolteSupport(String volteSupport) {
		this.volteSupport = volteSupport;
	}

	/**
	 * Gets the sim configuration.
	 *
	 * @return the simConfiguration
	 */
	public String getSimConfiguration() {
		return simConfiguration;
	}

	/**
	 * Sets the sim configuration.
	 *
	 * @param simConfiguration the simConfiguration to set
	 */
	public void setSimConfiguration(String simConfiguration) {
		this.simConfiguration = simConfiguration;
	}

	/**
	 * Gets the video call support.
	 *
	 * @return the videoCallSupport
	 */
	public String getVideoCallSupport() {
		return videoCallSupport;
	}

	/**
	 * Sets the video call support.
	 *
	 * @param videoCallSupport the videoCallSupport to set
	 */
	public void setVideoCallSupport(String videoCallSupport) {
		this.videoCallSupport = videoCallSupport;
	}

	/**
	 * Gets the firmware version.
	 *
	 * @return the firmwareVersion
	 */
	public String getFirmwareVersion() {
		return firmwareVersion;
	}

	/**
	 * Sets the firmware version.
	 *
	 * @param firmwareVersion the firmwareVersion to set
	 */
	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}

	/**
	 * Gets the internal memory.
	 *
	 * @return the internalMemory
	 */
	public String getInternalMemory() {
		return internalMemory;
	}

	/**
	 * Sets the internal memory.
	 *
	 * @param internalMemory the internalMemory to set
	 */
	public void setInternalMemory(String internalMemory) {
		this.internalMemory = internalMemory;
	}

	/**
	 * Gets the image url.
	 *
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * Sets the image url.
	 *
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


}

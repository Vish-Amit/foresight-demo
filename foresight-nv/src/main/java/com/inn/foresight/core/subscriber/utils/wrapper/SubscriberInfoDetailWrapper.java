package com.inn.foresight.core.subscriber.utils.wrapper;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.inn.core.generic.wrapper.RestWrapper;
import com.inn.foresight.core.subscriber.utils.SubscriberInfoConstants;
@RestWrapper
public class SubscriberInfoDetailWrapper {

	private String imsi;
	private String msisdn;
	private String category;
	private String type;
	private String creationTime;
	private String modificationTime;
	private Boolean isEnabled;
	private String createdBy;
	private String lastModifieredBy;

	public SubscriberInfoDetailWrapper(String imsi, String msisdn, String category, String type, Date creationTime, Date modificationTime, Boolean isEnabled, String createdBy,
			String lastModifieredBy) {
		super();
		this.imsi = imsi;
		this.msisdn = msisdn;
		this.category = category!=null?category.toUpperCase():null;
		this.type = type;
		this.creationTime = creationTime != null ? convertDateToString(creationTime, SubscriberInfoConstants.DD_MMM_YYYY_HH_MM_SS) : null;
		this.modificationTime = modificationTime != null ? convertDateToString(modificationTime, SubscriberInfoConstants.DD_MMM_YYYY_HH_MM_SS) : null;
		this.isEnabled = isEnabled;
		this.createdBy = createdBy;
		this.lastModifieredBy = lastModifieredBy;
	}
	
	public SubscriberInfoDetailWrapper() {
		
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public String getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(String modificationTime) {
		this.modificationTime = modificationTime;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastModifieredBy() {
		return lastModifieredBy;
	}

	public void setLastModifieredBy(String lastModifieredBy) {
		this.lastModifieredBy = lastModifieredBy;
	}

	@Override
	public String toString() {
		return "SubscriberInfoDetailWrapper [imsi=" + imsi + ", msisdn=" + msisdn + ", category=" + category + ", type=" + type + ", creationTime=" + creationTime + ", modificationTime="
				+ modificationTime + ", isEnabled=" + isEnabled + ", createdBy=" + createdBy + ", lastModifieredBy=" + lastModifieredBy + "]";
	}

	private String convertDateToString(Date convertDate, String format) {
		return new SimpleDateFormat(format).format(convertDate);
	}

}

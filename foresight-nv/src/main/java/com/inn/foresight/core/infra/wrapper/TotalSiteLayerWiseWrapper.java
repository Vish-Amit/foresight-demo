package com.inn.foresight.core.infra.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.core.generic.wrapper.RestWrapper;
@JpaWrapper
@RestWrapper
public class TotalSiteLayerWiseWrapper {
	private Long totalSiteCount;
	private Long effectedSiteCount;
	private Long effectedAlarmsCount;
	private String geography;
	Double lat;
	Double lng;
	String Type;
	
	public TotalSiteLayerWiseWrapper(String geography, Long siteCount) {
		super();
		this.totalSiteCount = siteCount;
		this.geography = geography;
	}
	public TotalSiteLayerWiseWrapper(String geography, Long siteCount,Double lat,Double lng) {
		super();
		this.totalSiteCount = siteCount;
		this.geography = geography;
		this.lat  = lat;
		this.lng = lng;
	}	
	
	public TotalSiteLayerWiseWrapper() {
		super();
	}

	public Long getEffectedAlarmsCount() {
		return effectedAlarmsCount;
	}
	public void setEffectedAlarmsCount(Long effectedAlarmsCount) {
		this.effectedAlarmsCount = effectedAlarmsCount;
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
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getGeography() {
		return geography;
	}
	public void setGeography(String geography) {
		this.geography = geography;
	}
	public Long getTotalSiteCount() {
		return totalSiteCount;
	}
	public void setTotalSiteCount(Long totalSiteCount) {
		this.totalSiteCount = totalSiteCount;
	}
	public Long getEffectedSiteCount() {
		return effectedSiteCount;
	}
	public void setEffectedSiteCount(Long effectedSiteCount) {
		this.effectedSiteCount = effectedSiteCount;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TotalSiteLayerWiseWrapper [totalSiteCount=");
		builder.append(totalSiteCount);
		builder.append(", effectedSiteCount=");
		builder.append(effectedSiteCount);
		builder.append(", effectedAlarmsCount=");
		builder.append(effectedAlarmsCount);
		builder.append(", geography=");
		builder.append(geography);
		builder.append(", lat=");
		builder.append(lat);
		builder.append(", lng=");
		builder.append(lng);
		builder.append(", Type=");
		builder.append(Type);
		builder.append("]");
		return builder.toString();
	}
}

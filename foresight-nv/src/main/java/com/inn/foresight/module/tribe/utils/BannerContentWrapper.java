package com.inn.foresight.module.tribe.utils;

import com.inn.core.generic.wrapper.RestWrapper;

@RestWrapper
public class BannerContentWrapper {

	private Integer bannerId;
	
	//{\"customRow\":[{\"On-Air Site\":\"14\"},{\"Planned Site\":\"5839\"}],\"location\":\"Futako Tamagawa\"}
	private String bannerContent;

	public Integer getBannerId() {
		return bannerId;
	}

	public void setBannerId(Integer bannerId) {
		this.bannerId = bannerId;
	}

	public String getBannerContent() {
		return bannerContent;
	}

	public void setBannerContent(String bannerContent) {
		this.bannerContent = bannerContent;
	}

	@Override
	public String toString() {
		return "BannerContentWrapper [bannerId=" + bannerId + ", bannerContent=" + bannerContent + "]";
	}
	
	
	
	
}

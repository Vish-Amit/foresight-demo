package com.inn.foresight.core.infra.wrapper;

import java.util.Map;

public class SiteConnectionPointWrapper {

	private Map vcuMap;

	private Map vduMap;

	public Map getVcuMap() {
		return vcuMap;
	}

	public void setVcuMap(Map vcuMap) {
		this.vcuMap = vcuMap;
	}

	public Map getVduMap() {
		return vduMap;
	}

	public void setVduMap(Map vduMap) {
		this.vduMap = vduMap;
	}

	@Override
	public String toString() {
		return "NEVisualizationWrapper [vcuMap=" + vcuMap + ", vduMap=" + vduMap + "]";
	}

}

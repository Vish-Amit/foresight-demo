package com.inn.foresight.core.infra.wrapper;

import java.util.List;

import com.inn.commons.maps.LatLng;

public class ZTESmartFrenWrapper extends LatLng {

	private String enodeBName;
	
	private List<List<List<Double>>> multiPolygon;
	
	private Integer cellId;
	
	private String clusterName;
	
	private String salesCluster;
	
	private String outletAdress;
	
	private String provinci;
	
	public ZTESmartFrenWrapper() {
		super();
	}

	public ZTESmartFrenWrapper(String enodeBName, List<List<List<Double>>> multiPolygon, Integer cellId, String clusterName) {
		super();
		this.enodeBName = enodeBName;
		this.multiPolygon = multiPolygon;
		this.cellId = cellId;
		this.clusterName = clusterName;
	}

	public String getEnodeBName() {
		return enodeBName;
	}

	public void setEnodeBName(String enodeBName) {
		this.enodeBName = enodeBName;
	}

	public List<List<List<Double>>> getMultiPolygon() {
		return multiPolygon;
	}

	public void setMultiPolygon(List<List<List<Double>>> multiPolygon) {
		this.multiPolygon = multiPolygon;
	}

	public Integer getCellId() {
		return cellId;
	}

	public void setCellId(Integer cellId) {
		this.cellId = cellId;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public String getSalesCluster() {
		return salesCluster;
	}

	public void setSalesCluster(String salesCluster) {
		this.salesCluster = salesCluster;
	}

	public String getOutletAdress() {
		return outletAdress;
	}

	public void setOutletAdress(String outletAdress) {
		this.outletAdress = outletAdress;
	}

	public String getProvinci() {
		return provinci;
	}

	public void setProvinci(String provinci) {
		this.provinci = provinci;
	}

	@Override
	public String toString() {
		return "ZTESmartFrenWrapper [enodeBName=" + enodeBName + ", multiPolygon=" + multiPolygon + ", cellId=" + cellId + ", clusterName=" + clusterName + ", salesCluster=" + salesCluster
				+ ", outletAdress=" + outletAdress + ", provinci=" + provinci + "]";
	}

}

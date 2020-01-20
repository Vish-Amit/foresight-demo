package com.inn.foresight.module.nv.livedrive.constants;

public enum KPI {
	RSRP("R"), RSRQ("Q"), SINR("S"), RSCP, ECNO, RSSI, RXQUALITY, DL, UL, LATENCY, JITTER, WEBDELAY, PCI, PILOT, POLQA, N1RSRQ("nQ"), N1RSRP("nR"), COMPARISON, N1PCI("nP"),HTTP_DOWNLOAD,  HTTP_UPLOAD, FTP_DOWNLOAD,  FTP_UPLOAD, HTTP_LINK_DOWNLOAD, HTTP_LINK_UPLOAD;
	
	@SuppressWarnings("unused")
	private String suffix;

	KPI(String suffix){
		this.suffix = suffix;
	}

	KPI(){

	}
}

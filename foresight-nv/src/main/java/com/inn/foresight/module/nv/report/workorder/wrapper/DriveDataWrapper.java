package com.inn.foresight.module.nv.report.workorder.wrapper;

import java.util.Arrays;

import com.inn.core.generic.wrapper.JpaWrapper;
@JpaWrapper

public class DriveDataWrapper {
	private String qmdlData;
	private String summarydata;
	private Integer pci;
	private Double dltpt;
	private Double ultpt;
	private byte img[];
	private Integer cellid;
	private Double latitutde;
	private Double longitude;
	private String json;
	private String filePath;

	private Double xPoint;
	private Double yPoint;
	private String imageName;
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public Double getLatitutde() {
		return latitutde;
	}
	public void setLatitutde(Double latitutde) {
		this.latitutde = latitutde;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Integer getCellid() {
		return cellid;
	}
	public void setCellid(Integer cellid) {
		this.cellid = cellid;
	}
	public Integer getPci() {
		return pci;
	}
	public void setPci(Integer pci) {
		this.pci = pci;
	}
	public Double getDltpt() {
		return dltpt;
	}
	public void setDltpt(Double dltpt) {
		this.dltpt = dltpt;
	}
	public Double getUltpt() {
		return ultpt;
	}
	public void setUltpt(Double ultpt) {
		this.ultpt = ultpt;
	}
	public byte[] getImg() {
		return img;
	}
	public void setImg(byte[] img) {
		this.img = img;
	}
	public String getQmdlData() {
		return qmdlData;
	}
	public void setQmdlData(String qmdlData) {
		this.qmdlData = qmdlData;
	}
	public String getSummarydata() {
		return summarydata;
	}
	public void setSummarydata(String summarydata) {
		this.summarydata = summarydata;
	}

	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}


	public Double getxPoint() {
		return xPoint;
	}
	public void setxPoint(Double xPoint) {
		this.xPoint = xPoint;
	}
	public Double getyPoint() {
		return yPoint;
	}
	public void setyPoint(Double yPoint) {
		this.yPoint = yPoint;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	@Override
	public String toString() {
		return "DriveDataWrapper [qmdlData=" + qmdlData + ", summarydata=" + summarydata + ", pci=" + pci + ", dltpt="
				+ dltpt + ", ultpt=" + ultpt + ", img=" + Arrays.toString(img) + ", cellid=" + cellid + ", latitutde="
				+ latitutde + ", longitude=" + longitude + ", json=" + json + ", filePath=" + filePath + ", xPoint="
				+ xPoint + ", yPoint=" + yPoint + ", imageName=" + imageName + "]";
	}

}

package com.inn.foresight.module.nv.report.workorder.wrapper;

import java.awt.Color;

public class PCIWrapper implements Comparable<PCIWrapper> {
	Integer count;
	Color color;
	Integer PCI;
	float Percentage;
	Long earfcn;
	Boolean isSampleData;
	Integer cgi;
	Color cgiColor;

	public Boolean getIsSampleData() {
		return isSampleData;
	}

	public void setIsSampleData(Boolean isSampleData) {
		this.isSampleData = isSampleData;
	}

	public Long getEarfcn() {
		return earfcn;
	}

	public void setEarfcn(Long earfcn) {
		this.earfcn = earfcn;
	}



	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Integer getPCI() {
		return PCI;
	}

	public void setPCI(Integer pCI) {
		PCI = pCI;
	}

	public float getPercentage() {
		return Percentage;
	}

	public void setPercentage(float percentage) {
		Percentage = percentage;
	}


	public Integer getCgi() {
		return cgi;
	}

	public void setCgi(Integer cgi) {
		this.cgi = cgi;
	}

	public Color getCgiColor() {
		return cgiColor;
	}

	public void setCgiColor(Color cgiColor) {
		this.cgiColor = cgiColor;
	}

	@Override
	public int compareTo(PCIWrapper o) {

		if (this.count - o.count == 0.0) {
            return 0;
        } else if (this.count - o.count > 0) {
            return -1;
        } else if (this.count - o.count < 0) {
            return 1;
        } else {
            return 0;
        }
	}

	@Override
	public String toString() {
		return "PCIWrapper [count=" + count + ", color=" + color + ", PCI=" + PCI + ", Percentage=" + Percentage
				+ ", earfcn=" + earfcn + ", isSampleData=" + isSampleData + ", cgi=" + cgi + ", cgiColor=" + cgiColor
				+ "]";
	}

}

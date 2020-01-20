package com.inn.foresight.module.nv.report.wrapper.stealth;

public class StealthWODetailItemWrapper {

	String itemLabel;
	String itemValue;
	
	public String getItemLabel() {
		return itemLabel;
	}
	public void setItemLabel(String itemLabel) {
		this.itemLabel = itemLabel;
	}
	public String getItemValue() {
		return itemValue;
	}
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	@Override
	public String toString() {
		return "StealthWODetailItemWrapper [itemLabel=" + itemLabel + ", itemValue=" + itemValue + "]";
	}
	
}

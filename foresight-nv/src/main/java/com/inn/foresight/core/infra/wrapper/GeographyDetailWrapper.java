package com.inn.foresight.core.infra.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.core.generic.wrapper.RestWrapper;

@JpaWrapper
@RestWrapper
public class GeographyDetailWrapper {

	private String l1Name;
	private String l2Name;
	private String l3Name;
	private String l4Name;

	public GeographyDetailWrapper(String l1name, String l2name, String l3name, String l4name) {
		l1Name = l1name;
		l2Name = l2name;
		l3Name = l3name;
		l4Name = l4name;
	}

	public String getL1Name() {
		return l1Name;
	}

	public void setL1Name(String l1Name) {
		this.l1Name = l1Name;
	}

	public String getL2Name() {
		return l2Name;
	}

	public void setL2Name(String l2Name) {
		this.l2Name = l2Name;
	}

	public String getL3Name() {
		return l3Name;
	}

	public void setL3Name(String l3Name) {
		this.l3Name = l3Name;
	}

	public String getL4Name() {
		return l4Name;
	}

	public void setL4Name(String l4Name) {
		this.l4Name = l4Name;
	}

	@Override
	public String toString() {
		return "GeographyDetailWrapper [l1Name=" + l1Name + ", l2Name=" + l2Name + ", l3Name=" + l3Name + ", l4Name="
				+ l4Name + "]";
	}
}

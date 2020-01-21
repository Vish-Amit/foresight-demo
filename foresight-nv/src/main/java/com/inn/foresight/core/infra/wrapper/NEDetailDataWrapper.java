package com.inn.foresight.core.infra.wrapper;

import java.io.Serializable;

import com.inn.core.generic.wrapper.RestWrapper;
@RestWrapper
public class NEDetailDataWrapper implements Serializable {

	private static final long serialVersionUID = 4471592817759568755L;
	private String neName;
	private String siteClass;

	public NEDetailDataWrapper(String neName, String siteClass) {
		this.neName = neName;
		this.siteClass = siteClass;
	}

	public NEDetailDataWrapper() {

	}

	public String getNeName() {
		return neName;
	}

	public void setNeName(String neName) {
		this.neName = neName;
	}

	public String getSiteClass() {
		return siteClass;
	}

	public void setSiteClass(String siteClass) {
		this.siteClass = siteClass;
	}

	@Override
	public String toString() {
		return "NEDetailDataWrapper [neName=" + neName + ", siteClass=" + siteClass + "]";
	}

}
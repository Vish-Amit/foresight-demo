package com.inn.foresight.core.infra.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class NeDataWrapper {

	private String siteName;
	
	private String pneId;

	public NeDataWrapper(String siteName, String neId) {
		super();
		this.siteName = siteName;
		this.pneId = neId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getpNeId() {
		return pneId;
	}

	public void setpNeId(String pneId) {
		this.pneId = pneId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NeDataWrapper [");
		if (siteName != null) {
			builder.append("siteName=");
			builder.append(siteName);
			builder.append(", ");
		}
		if (pneId != null) {
			builder.append("pneId=");
			builder.append(pneId);
		}
		builder.append("]");
		return builder.toString();
	}
	
}

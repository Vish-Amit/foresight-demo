package com.inn.foresight.core.infra.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.foresight.core.infra.utils.enums.NEStatus;
import com.inn.foresight.core.infra.utils.enums.NEType;

/* The Wrapper */

@JpaWrapper
public class NEDetailWrapper {

	private String neId;

	private String pneId;

	private String nename;

	private String pneName;

	private String pneType;

	private String netype;

	private String neStatus;

	private String pneStatus;

	public NEDetailWrapper(String neId, String pneId, String nename, String pneName, NEType netype, NEType pneType,
			NEStatus neStatus, NEStatus pneStatus) {
		super();
		this.neId = neId;
		this.pneId = pneId;
		this.nename = nename;
		this.pneName = pneName;
		if (netype != null) {
			this.netype = netype.name();
		}
		if (pneType != null) {
			this.pneType = pneType.name();
		}
		if (neStatus != null) {
			this.neStatus = neStatus.name();
		}
		if (pneStatus != null) {
			this.pneStatus = pneStatus.name();
		}
	}

	public String getPneType() {
		return pneType;
	}

	public void setPneType(String pneType) {
		this.pneType = pneType;
	}

	public String getNeId() {
		return neId;
	}

	public void setNeId(String neId) {
		this.neId = neId;
	}

	public String getPneId() {
		return pneId;
	}

	public void setPneId(String pneId) {
		this.pneId = pneId;
	}

	public String getNename() {
		return nename;
	}

	public void setNename(String nename) {
		this.nename = nename;
	}

	public String getPneName() {
		return pneName;
	}

	public void setPneName(String pneName) {
		this.pneName = pneName;
	}

	public String getNetype() {
		return netype;
	}

	public void setNetype(String netype) {
		this.netype = netype;
	}

	public String getNeStatus() {
		return neStatus;
	}

	public void setNeStatus(String neStatus) {
		this.neStatus = neStatus;
	}

	public String getPneStatus() {
		return pneStatus;
	}

	public void setPneStatus(String pneStatus) {
		this.pneStatus = pneStatus;
	}

	@Override
	public String toString() {
		return "NEDetailWrapper [neId=" + neId + ", pneId=" + pneId + ", nename=" + nename + ", pneName=" + pneName
				+ ", pneType=" + pneType + ", netype=" + netype + ", neStatus=" + neStatus + ", pneStatus=" + pneStatus
				+ "]";
	}

}

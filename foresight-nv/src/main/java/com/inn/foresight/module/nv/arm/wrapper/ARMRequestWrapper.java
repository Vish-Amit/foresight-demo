package com.inn.foresight.module.nv.arm.wrapper;

import java.util.List;

import com.inn.foresight.module.nv.app.model.APKDetail;
import com.inn.foresight.module.nv.app.model.LicenseMaster;
import com.inn.foresight.module.nv.profile.model.NVProfileData;

/**
 * @author ist
 *
 */
public class ARMRequestWrapper {
 
	List<String> appOS;
	Boolean applyReleaseCheck;

	NVProfileData nvProfile;
	APKDetail apkDetail;
	LicenseMaster licenseMaster;

	public Boolean getApplyReleaseCheck() {
		return applyReleaseCheck;
	}
	public void setApplyReleaseCheck(Boolean applyReleaseCheck) {
		this.applyReleaseCheck = applyReleaseCheck;
	}
	public List<String> getAppOS() {
		return appOS;
	}
	public void setAppOS(List<String> appOS) {
		this.appOS = appOS;
	}
	public NVProfileData getNvProfile() {
		return nvProfile;
	}
	public void setNvProfile(NVProfileData nvProfile) {
		this.nvProfile = nvProfile;
	}
	public APKDetail getApkDetail() {
		return apkDetail;
	}
	public void setApkDetail(APKDetail apkDetail) {
		this.apkDetail = apkDetail;
	}
	public LicenseMaster getLicenseMaster() {
		return licenseMaster;
	}
	public void setLicenseMaster(LicenseMaster licenseMaster) {
		this.licenseMaster = licenseMaster;
	}
	
	@Override
	public String toString() {
		return "ARMRequestWrapper [appOS=" + appOS + ", isReleasedRequired=" + applyReleaseCheck + ", nvProfile="
				+ nvProfile + ", apkDetail=" + apkDetail + ", licenseMaster=" + licenseMaster + "]";
	}
}

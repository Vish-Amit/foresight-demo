package com.inn.foresight.module.nv.arm.wrapper;

import java.util.Date;

import com.inn.product.um.user.model.User;

public class ARMResultWrapper {

	/*APK Details fields served*/
	
	Integer apkIdPk;
	String apkId;
	String apkName;
	String apkOs;
	Date apkCreationTime;
	String apkDownloadurl;
	Date apkReleasetime;
	String apkRemark;
	Integer apkVersionCode;
	String apkVersionName;
	User apkCreator;
	String apkReleaseNote;
	
	/*License Master Fields to be served*/
	
	Integer licenseMasterIdPk;
	String licenseClientName;
	Long licensemaxNoOfUser;
	Long licenseValidity;
	Date licenseCreationDate;
	
	/*NV Profile Fields Served*/
	
	Integer profileIdPk;
	String profileId;
	
	
	
}

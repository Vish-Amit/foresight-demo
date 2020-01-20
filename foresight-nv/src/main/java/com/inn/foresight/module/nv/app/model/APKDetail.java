package com.inn.foresight.module.nv.app.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.module.nv.app.constants.AppConstants;
import com.inn.product.um.user.model.User;

/** The Class APKDetail. */

@NamedQuery(name = "getAPKDetailByName", query = "select a from APKDetail a where a.apkId=:apkId and a.apkOS =:appOS and a.isDeleted = false "
		+ "and a.releaseTime is not null order by a.releaseTime desc")

@NamedQuery(name = "getAPKDetailById", query = "select a from APKDetail a where a.apkId=:apkId and a.apkOS = :appOS and a.versionName= :versionName and a.isDeleted = false "
		+ "order by a.releaseTime desc")

@NamedQuery(name = "getAPKDetails", query = "select a from APKDetail a where a.apkOS in (:appOS) and a.isDeleted = false order by a.releaseTime desc")


 @FilterDef(name = AppConstants.APP_RELEASE_TIME_FILTER)
 @FilterDef(name = AppConstants.APK_ID_FILTER, parameters = {
		@ParamDef(name = AppConstants.APK_ID_STR, type = "java.lang.String") })
 @FilterDef(name = AppConstants.APK_VERSION_NAME_FILTER, parameters = {
		@ParamDef(name = AppConstants.VERSION_NAME, type = "java.lang.String") })
 @FilterDef(name = AppConstants.APK_CODE_FILTER, parameters = {
		@ParamDef(name = AppConstants.VERSION_CODE, type = "java.lang.Integer") })


@FilterDef(name = AppConstants.APK_NAME_FILTER, parameters = {
		@ParamDef(name = AppConstants.APK_NAME, type = "java.lang.String") })


@Filter(name = AppConstants.APP_RELEASE_TIME_FILTER, condition = "apkdetailid_pk in (select a.apkdetailid_pk from APKDetail a "
				+ "where a.releasetime is not null)") 
@Filter(name = AppConstants.APK_ID_FILTER, condition = "apkid like (:apkId)")
@Filter(name = AppConstants.APK_VERSION_NAME_FILTER, condition = "versionname like (:versionName)")
@Filter(name = AppConstants.APK_CODE_FILTER, condition = "versioncode=:versionCode")
@Filter(name = AppConstants.APK_NAME_FILTER, condition = "apkname like (:apkName)")

@XmlRootElement(name = "APKDetail")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "APKDetail")
@DynamicUpdate
public class APKDetail implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Column(name = "apkdetailid_pk")
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	private Integer id;

	/** The apk name. */
	@Basic
	@NotNull
	@Column(name = "apkname")
	private String apkName;

	/** The version name. */
	@Basic
	@NotNull
	@Column(name = "versionname")
	private String versionName;

	/** The version code. */
	@Basic
	@Column(name = "versioncode")
	private Integer versionCode;

	/** The release time. */
	@Basic
	@Column(name = "releasetime")
	private Date releaseTime;
	
	/** The release Note */
	@Basic
	@Column(name = "releasenote")
	private String releaseNote;
	
	/** The download URL. */
	@Basic
	@Column(name = "downloadurl")
	private String downloadURL;

	/** The creation time. */
	@Basic
	@Column(name = "creationtime")
	private Date creationTime;

	/** The modification time. */
	@Basic
	@Column(name = "modificationTime")
	private Date modificationTime;

	/** The type. */
	@Basic
	private String type;

	/** The remark. */
	@Basic
	private String remark;

	/** The server upgrade. */
	@Basic
	@Column(name = "serverupgrade")
	private String serverUpgrade;

	/** The sun set time. */
	@Basic
	@Column(name = "sunsettime")
	private Date sunSetTime;

	/** The file size. */
	@Basic
	@Column(name = "filesize")
	private Long fileSize;

	/** The is deleted. */
	@Basic
	@Column(name = "deleted")
	private Boolean isDeleted;

	/** The apk id. */
	@Basic
	@Column(name = "apkid")
//	@Enumerated(javax.persistence.EnumType.STRING)
	private String apkId;

	/** The apk OS. */
	@Basic
	@Column(name = "apkos")
	@Enumerated(javax.persistence.EnumType.STRING)
	private APP_OS apkOS;

	/** The modifier. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lastmodifierid_fk", nullable = true)
	private User modifier;

	/** The creator. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creatorid_fk")
	private User creator;

	/**
	 * Gets the apk OS.
	 *
	 * @return the apk OS
	 */
	public APP_OS getApkOS() {
		return apkOS;
	}

	/**
	 * Sets the apk OS.
	 *
	 * @param apkOS the new apk OS
	 */
	public void setApkOS(APP_OS apkOS) {
		this.apkOS = apkOS;
	}

	/**
	 * Gets the modifier.
	 *
	 * @return the modifier
	 */
	public User getModifier() {
		return modifier;
	}

	/**
	 * Gets the release time.
	 *
	 * @return the release time
	 */
	public Date getReleaseTime() {
		return releaseTime;
	}

	/**
	 * Sets the release time.
	 *
	 * @param releaseTime the new release time
	 */
	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	/**
	 * Gets the download URL.
	 *
	 * @return the download URL
	 */
	public String getDownloadURL() {
		return downloadURL;
	}

	/**
	 * Sets the download URL.
	 *
	 * @param downloadURL the new download URL
	 */
	public void setDownloadURL(String downloadURL) {
		this.downloadURL = downloadURL;
	}

	/**
	 * Gets the creation time.
	 *
	 * @return the creation time
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	/**
	 * Sets the creation time.
	 *
	 * @param creationTime the new creation time
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * Gets the modification time.
	 *
	 * @return the modification time
	 */
	public Date getModificationTime() {
		return modificationTime;
	}

	/**
	 * Sets the modification time.
	 *
	 * @param modificationTime the new modification time
	 */
	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	/**
	 * Gets the sun set time.
	 *
	 * @return the sun set time
	 */
	public Date getSunSetTime() {
		return sunSetTime;
	}

	/**
	 * Sets the sun set time.
	 *
	 * @param sunSetTime the new sun set time
	 */
	public void setSunSetTime(Date sunSetTime) {
		this.sunSetTime = sunSetTime;
	}

	/**
	 * Sets the modifier.
	 *
	 * @param modifier the new modifier
	 */
	public void setModifier(User modifier) {
		this.modifier = modifier;
	}

	/**
	 * Gets the creator.
	 *
	 * @return the creator
	 */
	public User getCreator() {
		return creator;
	}

	/**
	 * Sets the creator.
	 *
	 * @param creator the new creator
	 */
	public void setCreator(User creator) {
		this.creator = creator;
	}

	/**
	 * Gets the file size.
	 *
	 * @return the file size
	 */
	public Long getFileSize() {
		return fileSize;
	}

	/**
	 * Sets the file size.
	 *
	 * @param fileSize the new file size
	 */
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * Gets the checks if is deleted.
	 *
	 * @return the checks if is deleted
	 */
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	/**
	 * Sets the checks if is deleted.
	 *
	 * @param isDeleted the new checks if is deleted
	 */
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}


	/**
	 * Sets the sunset date.
	 *
	 * @param sunsetDate the new sunset date
	 */
	public void setSunsetDate(Date sunsetDate) {
		this.sunSetTime = sunsetDate;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the remark.
	 *
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * Sets the remark.
	 *
	 * @param remark the new remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * Gets the server upgrade.
	 *
	 * @return the server upgrade
	 */
	public String getServerUpgrade() {
		return serverUpgrade;
	}

	/**
	 * Sets the server upgrade.
	 *
	 * @param serverUpgrade the new server upgrade
	 */
	public void setServerUpgrade(String serverUpgrade) {
		this.serverUpgrade = serverUpgrade;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the apk name.
	 *
	 * @return the apk name
	 */
	public String getApkName() {
		return apkName;
	}

	/**
	 * Sets the apk name.
	 *
	 * @param apkName the new apk name
	 */
	public void setApkName(String apkName) {
		this.apkName = apkName;
	}

	/**
	 * Gets the version name.
	 *
	 * @return the version name
	 */
	public String getVersionName() {
		return versionName;
	}

	/**
	 * Sets the version name.
	 *
	 * @param versionName the new version name
	 */
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	/**
	 * Gets the version code.
	 *
	 * @return the version code
	 */
	public Integer getVersionCode() {
		return versionCode;
	}

	/**
	 * Sets the version code.
	 *
	 * @param versionCode the new version code
	 */
	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}
	/**
	 * Sets the release date.
	 *
	 * @param releaseDate the new release date
	 */
	public void setReleaseDate(Date releaseDate) {
		this.releaseTime = releaseDate;
	}


	/**
	 * Gets the apk id.
	 *
	 * @return the apk id
	 */
	public String getApkId() {
		return apkId;
	}

	/**
	 * Sets the apk id.
	 *
	 * @param apkId the new apk id
	 */
	public void setApkId(String apkId) {
		this.apkId = apkId;
	}

	/**
	 * Gets the serialversionuid.
	 *
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/** The Enum APP_OS. */
	public enum APP_OS {
		/** The android. */
		ANDROID,
		/** The iphone. */
		IPHONE
	}
	
	
	/**
	 * @return the releaseNote
	 */
	public String getReleaseNote() {
		return releaseNote;
	}

	/**
	 * @param releaseNote the releaseNote to set
	 */
	public void setReleaseNote(String releaseNote) {
		this.releaseNote = releaseNote;
	}

	
	/**
	 * To string.
	 *
	 * @return the string
	 */

	@Override
	public String toString() {
		return "APKDetail [id=" + id + ", apkName=" + apkName + ", versionName=" + versionName + ", versionCode="
				+ versionCode + ", releaseTime=" + releaseTime + ", releaseNote=" + releaseNote + ", downloadURL="
				+ downloadURL + ", creationTime=" + creationTime + ", modificationTime=" + modificationTime + ", type="
				+ type + ", remark=" + remark + ", serverUpgrade=" + serverUpgrade + ", sunSetTime=" + sunSetTime
				+ ", fileSize=" + fileSize + ", isDeleted=" + isDeleted + ", apkId=" + apkId + ", apkOS=" + apkOS
				+ ", modifier=" + modifier + ", creator=" + creator + "]";
	}


}

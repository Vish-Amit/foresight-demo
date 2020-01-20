package com.inn.foresight.module.nv.profile.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.module.nv.profile.constants.NVProfileConstants;
import com.inn.product.um.geography.model.GeographyL1;
import com.inn.product.um.geography.model.GeographyL2;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;
import com.inn.product.um.user.model.User;

/**
 * The Class NVProfileData.
 *
 * @author innoeye date - 23-Feb-2018 8:46:52 PM
 */
@NamedQueries({
		@NamedQuery(name = NVProfileConstants.GET_PROFILE_DATA_BY_PROFILE_ID, query = "select n from NVProfileData n where n.profileId=:profileId"),
		@NamedQuery(name = NVProfileConstants.GET_ALL_PROFILE, query = "select new com.inn.foresight.module.nv.profile.wrapper.NVProfileDataWrapper(n.id,n.profileId, n.name, n.type, n.status, concat(n.creator.firstName ,' ', n.creator.lastName),n.creationTime,n.module,n.os) from NVProfileData n where n.isDeleted = false order by n.modificationTime desc"),
		@NamedQuery(name = NVProfileConstants.GET_PROFILE_BY_TYPE_AND_MODULE, query = "select n from NVProfileData n where n.type = :type and n.module = :module and n.isDeleted = false"),
		@NamedQuery(name = NVProfileConstants.GET_ALL_VALID_PROFILES, query = "select n from NVProfileData n where Date(n.startTime) <= Date(NOW()) AND Date(n.endTime) >= Date(NOW()) AND n.status=true AND n.type != 'PRE-DEFINE' and n.isDeleted = false"),
		@NamedQuery(name = "getProfileByType", query = "select n from NVProfileData n where n.type = :type and n.isDeleted = false"),
})
@Entity
@Table(name = "NVProfile")
@XmlRootElement(name = "NVProfile")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@DynamicUpdate
public class NVProfileData implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "nvprofileid_pk")
	private Integer id;

	/** The profile id. */
	@Column(name = "id")
	private String profileId;

	/** The name. */
	@Column(name = "name")
	private String name;

	/** The type. */
	@Column(name = "type")
	private String type;

	/** The status. */
	@Column(name = "status")
	private boolean status;

	/** The passive enable. */
	@Column(name = "passiveenable")
	private boolean passiveEnable;

	/** The start time. */
	@Column(name = "starttime")
	private Date startTime;

	/** The end time. */
	@Column(name = "endtime")
	private Date endTime;

	/** The level. */
	@Column(name = "level")
	private String level;

	/** The creator. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creatorid_fk", nullable = false)
	private User creator;

	/** The profile JSON. */
	@Column(name = "profile")
	private String profile;

	/** The geography L 1. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl1id_fk", nullable = false)
	private GeographyL1 geographyL1;

	/** The geography L 2. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl2id_fk", nullable = false)
	private GeographyL2 geographyL2;

	/** The geography L 3. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl3id_fk", nullable = false)
	private GeographyL3 geographyL3;

	/** The geography L 4. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl4id_fk", nullable = false)
	private GeographyL4 geographyL4;

	/** The make. */
	@Column(name = "make")
	private String make;

	/** The model. */
	@Column(name = "model")
	private String model;

	/** The os. */
	@Column(name = "os")
	private String os;

	/** The creation time. */
	@Column(name = "creationtime")
	private Date creationTime;

	/** The modification time. */
	@Column(name = "modificationtime")
	private Date modificationTime;

	/** The description. */
	@Column(name = "description", length = 300)
	private String description;

	/** The module. */
	@Column(name = "module")
	private String module;

	/** The operator. */
	@Column(name = "operator")
	private String operator;

	/** The operator. */
	@Column(name = "deleted")
	private boolean isDeleted;

	/** Instantiates a new NV profile data. */
	public NVProfileData() {
		super();
	}

	/**
	 * Instantiates a new NV profile data.
	 *
	 * @param profileData
	 *            the profile data
	 */
	public NVProfileData(NVProfileData profileData) {
		super();
		this.name = profileData.name;
		this.type = profileData.type;
		this.status = profileData.status;
		this.passiveEnable = profileData.passiveEnable;
		this.startTime = profileData.startTime;
		this.endTime = profileData.endTime;
		this.level = profileData.level;
		this.profile = profileData.profile;
		this.make = profileData.make;
		this.model = profileData.model;
		this.os = profileData.os;
		this.creationTime = new Date();
		this.modificationTime = new Date();
		this.module = profileData.module;
		this.operator = profileData.operator;
		this.description = profileData.description;
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
	 * @param id
	 *            the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the profile id.
	 *
	 * @return the profile id
	 */
	public String getProfileId() {
		return profileId;
	}

	/**
	 * Sets the profile id.
	 *
	 * @param profileId
	 *            the new profile id
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @param type
	 *            the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Checks if is status.
	 *
	 * @return true, if is status
	 */
	public boolean isStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status
	 *            the new status
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}

	/**
	 * Checks if is passive enable.
	 *
	 * @return true, if is passive enable
	 */
	public boolean isPassiveEnable() {
		return passiveEnable;
	}

	/**
	 * Sets the passive enable.
	 *
	 * @param passiveEnable
	 *            the new passive enable
	 */
	public void setPassiveEnable(boolean passiveEnable) {
		this.passiveEnable = passiveEnable;
	}

	/**
	 * Gets the start time.
	 *
	 * @return the start time
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * Sets the start time.
	 *
	 * @param startTime
	 *            the new start time
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * Gets the end time.
	 *
	 * @return the end time
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * Sets the end time.
	 *
	 * @param endTime
	 *            the new end time
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * Gets the level.
	 *
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * Sets the level.
	 *
	 * @param level
	 *            the new level
	 */
	public void setLevel(String level) {
		this.level = level;
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
	 * @param creator
	 *            the new creator
	 */
	public void setCreator(User creator) {
		this.creator = creator;
	}

	/**
	 * Gets the profile JSON.
	 *
	 * @return the profile JSON
	 */
	public String getProfile() {
		return profile;
	}

	/**
	 * Sets the profile JSON.
	 *
	 * @param profileJSON
	 *            the new profile JSON
	 */
	public void setProfile(String profileJSON) {
		this.profile = profileJSON;
	}

	/**
	 * Gets the geography L 1.
	 *
	 * @return the geography L 1
	 */
	public GeographyL1 getGeographyL1() {
		return geographyL1;
	}

	/**
	 * Sets the geography L 1.
	 *
	 * @param geographyL1
	 *            the new geography L 1
	 */
	public void setGeographyL1(GeographyL1 geographyL1) {
		this.geographyL1 = geographyL1;
	}

	/**
	 * Gets the geography L 2.
	 *
	 * @return the geography L 2
	 */
	public GeographyL2 getGeographyL2() {
		return geographyL2;
	}

	/**
	 * Sets the geography L 2.
	 *
	 * @param geographyL2
	 *            the new geography L 2
	 */
	public void setGeographyL2(GeographyL2 geographyL2) {
		this.geographyL2 = geographyL2;
	}

	/**
	 * Gets the geography L 3.
	 *
	 * @return the geography L 3
	 */
	public GeographyL3 getGeographyL3() {
		return geographyL3;
	}

	/**
	 * Sets the geography L 3.
	 *
	 * @param geographyL3
	 *            the new geography L 3
	 */
	public void setGeographyL3(GeographyL3 geographyL3) {
		this.geographyL3 = geographyL3;
	}

	/**
	 * Gets the geography L 4.
	 *
	 * @return the geography L 4
	 */
	public GeographyL4 getGeographyL4() {
		return geographyL4;
	}

	/**
	 * Sets the geography L 4.
	 *
	 * @param geographyL4
	 *            the new geography L 4
	 */
	public void setGeographyL4(GeographyL4 geographyL4) {
		this.geographyL4 = geographyL4;
	}

	/**
	 * Gets the make.
	 *
	 * @return the make
	 */
	public String getMake() {
		return make;
	}

	/**
	 * Sets the make.
	 *
	 * @param make
	 *            the new make
	 */
	public void setMake(String make) {
		this.make = make;
	}

	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Sets the model.
	 *
	 * @param model
	 *            the new model
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * Gets the os.
	 *
	 * @return the os
	 */
	public String getOs() {
		return os;
	}

	/**
	 * Sets the os.
	 *
	 * @param os
	 *            the new os
	 */
	public void setOs(String os) {
		this.os = os;
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
	 * @param creationTime
	 *            the new creation time
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
	 * @param modificationTime
	 *            the new modification time
	 */
	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description
	 *            the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the module.
	 *
	 * @return the module
	 */
	public String getModule() {
		return module;
	}

	/**
	 * Sets the module.
	 *
	 * @param module
	 *            the new module
	 */
	public void setModule(String module) {
		this.module = module;
	}

	/**
	 * Gets the operator.
	 *
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * Sets the operator.
	 *
	 * @param operator
	 *            the new operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * Checks if is deleted.
	 *
	 * @return true, if is deleted
	 */
	public boolean isDeleted() {
		return isDeleted;
	}

	/**
	 * Sets the deleted.
	 *
	 * @param isDeleted
	 *            the new deleted
	 */
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String toString() {
		return "NVProfileData [id=" + id + ", profileId=" + profileId + ", name=" + name + ", type=" + type
				+ ", status=" + status + ", passiveEnable=" + passiveEnable + ", startTime=" + startTime + ", endTime="
				+ endTime + ", level=" + level + ", creator=" + creator + ", profile=" + profile + ", geographyL1="
				+ geographyL1 + ", geographyL2=" + geographyL2 + ", geographyL3=" + geographyL3 + ", geographyL4="
				+ geographyL4 + ", make=" + make + ", model=" + model + ", os=" + os + ", creationTime=" + creationTime
				+ ", modificationTime=" + modificationTime + ", description=" + description + ", module=" + module
				+ ", operator=" + operator + ", isDeleted=" + isDeleted + "]";
	}
}

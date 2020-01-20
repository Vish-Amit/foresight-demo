package com.inn.foresight.module.nv.profile.wrapper;

import java.io.Serializable;
import java.util.Date;

import com.inn.commons.Symbol;
import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.core.generic.wrapper.RestWrapper;
import com.inn.foresight.module.nv.profile.model.NVProfileData;

/**
 * The Class NVProfileDataWrapper.
 *
 * @author innoeye
 * date - 28-Feb-2018 1:09:40 PM
 */
@JpaWrapper
@RestWrapper
public class NVProfileDataWrapper implements Serializable{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	private Integer id;

	/** The profile id. */
	private String profileId;
	
	/** The name. */
	private String name;
	
	/** The type. */
	private String type;
	
	/** The status. */
	private boolean status;
	
	/** The passive enable. */
	private Boolean passiveEnable;
	
	/** The start time. */
	private Date startTime;
	
	/** The end time. */
	private Date endTime;
	
	/** The level. */
	private String level;
	
	/** The creator. */
	private String creator;
	
	/** The profile JSON. */
	private String profile;
	
	/** The geography L 1. */
	private String geographyL1;
	
	/** The geography L 2. */
	private String geographyL2;
	
	/** The geography L 3. */
	private String geographyL3;
	
	/** The geography L 4. */
	private String geographyL4;
	
	/** The make. */
	private String make;
	
	/** The model. */
	private String model;
	
	/** The os. */
	private String os;
	
	/** The creation time. */
	private Date creationTime;
	
	/** The description. */
	private String description;
	
	/** The module. */
	private String module;
	
	/** The operator. */
	private String operator;
	

	/**
	 * Instantiates a new NV profile data wrapper.
	 *
	 * @param id the id
	 * @param profileId the profile id
	 * @param name the name
	 * @param type the type
	 * @param status the status
	 * @param creator the creator
	 * @param creationTime the creation time
	 * @param description the description
	 */
	public NVProfileDataWrapper(Integer id, String profileId, String name, String type, boolean status, String creator,
			Date creationTime, String module,String os) {
		super();
		this.id = id;
		this.profileId = profileId;
		this.name = name;
		this.type = type;
		this.status = status;
		this.creator = creator;
		this.creationTime = creationTime;
		this.module = module;
		this.os=os;
	}
	

	/**
	
	 * Instantiates a new NV profile data wrapper.
	 *
	 * @param profileData the profile data
	 */
	public NVProfileDataWrapper(NVProfileData profileData) {
		super();
		this.id = profileData.getId();
		this.profileId = profileData.getProfileId();
		this.name = profileData.getName();
		this.type = profileData.getType();
		this.status = profileData.isStatus();
		this.passiveEnable = profileData.isPassiveEnable();
		this.startTime = profileData.getStartTime();
		this.endTime = profileData.getEndTime();
		this.level = profileData.getLevel();
		this.creator = profileData.getCreator().getFirstName() + Symbol.SPACE_STRING + profileData.getCreator().getLastName();
		this.profile = profileData.getProfile();
		this.geographyL1 = profileData.getGeographyL1() != null ? profileData.getGeographyL1().getName() : null;
		this.geographyL2 = profileData.getGeographyL2() != null ? profileData.getGeographyL2().getName() : null;
		this.geographyL3 = profileData.getGeographyL3() != null ? profileData.getGeographyL3().getName() : null;
		this.geographyL4 = profileData.getGeographyL4() != null ? profileData.getGeographyL4().getName() : null;
		this.make = profileData.getMake();
		this.model = profileData.getModel();
		this.os = profileData.getOs();
		this.creationTime = profileData.getCreationTime();
		this.description = profileData.getDescription();
		this.module = profileData.getModule();
		this.operator = profileData.getOperator();
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
	 * @param profileId the new profile id
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
	 * @param name the new name
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
	 * @param type the new type
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
	 * @param status the new status
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}

	
	/**
	 * Gets the passive enable.
	 *
	 * @return the passive enable
	 */
	public Boolean getPassiveEnable() {
		return passiveEnable;
	}

	
	/**
	 * Sets the passive enable.
	 *
	 * @param passiveEnable the new passive enable
	 */
	public void setPassiveEnable(Boolean passiveEnable) {
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
	 * @param startTime the new start time
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
	 * @param endTime the new end time
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
	 * @param level the new level
	 */
	public void setLevel(String level) {
		this.level = level;
	}

	
	/**
	 * Gets the creator.
	 *
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}

	
	/**
	 * Sets the creator.
	 *
	 * @param creator the new creator
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	
	/**
	 * Gets the profile.
	 *
	 * @return the profile
	 */
	public String getProfile() {
		return profile;
	}

	
	/**
	 * Sets the profile.
	 *
	 * @param profile the new profile
	 */
	public void setProfile(String profile) {
		this.profile = profile;
	}

	
	/**
	 * Gets the geography L 1.
	 *
	 * @return the geography L 1
	 */
	public String getGeographyL1() {
		return geographyL1;
	}

	
	/**
	 * Sets the geography L 1.
	 *
	 * @param geographyL1 the new geography L 1
	 */
	public void setGeographyL1(String geographyL1) {
		this.geographyL1 = geographyL1;
	}

	
	/**
	 * Gets the geography L 2.
	 *
	 * @return the geography L 2
	 */
	public String getGeographyL2() {
		return geographyL2;
	}

	
	/**
	 * Sets the geography L 2.
	 *
	 * @param geographyL2 the new geography L 2
	 */
	public void setGeographyL2(String geographyL2) {
		this.geographyL2 = geographyL2;
	}

	
	/**
	 * Gets the geography L 3.
	 *
	 * @return the geography L 3
	 */
	public String getGeographyL3() {
		return geographyL3;
	}

	
	/**
	 * Sets the geography L 3.
	 *
	 * @param geographyL3 the new geography L 3
	 */
	public void setGeographyL3(String geographyL3) {
		this.geographyL3 = geographyL3;
	}

	
	/**
	 * Gets the geography L 4.
	 *
	 * @return the geography L 4
	 */
	public String getGeographyL4() {
		return geographyL4;
	}

	
	/**
	 * Sets the geography L 4.
	 *
	 * @param geographyL4 the new geography L 4
	 */
	public void setGeographyL4(String geographyL4) {
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
	 * @param make the new make
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
	 * @param model the new model
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
	 * @param os the new os
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
	 * @param creationTime the new creation time
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
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
	 * @param description the new description
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
	 * @param module the new module
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
	 * @param operator the new operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
}

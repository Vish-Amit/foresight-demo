package com.inn.foresight.module.nv.profile.wrapper;

import java.io.Serializable;
import java.util.List;

import com.inn.core.generic.wrapper.RestWrapper;

/**
 * The Class NVProfileWrapper.
 *
 * @author innoeye
 * date - 19-Mar-2018 7:02:31 PM
 */
@RestWrapper
public class NVProfileWrapper implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	private String id;
	
	/** The profile. */
	private List<NVProfile> profile;

	/**
	 * Instantiates a new NV profile wrapper.
	 *
	 * @param id the id
	 * @param profile the profile
	 */
	public NVProfileWrapper(String id, List<NVProfile> profile) {
		super();
		this.id = id;
		this.profile = profile;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the profile.
	 *
	 * @return the profile
	 */
	public List<NVProfile> getProfile() {
		return profile;
	}

	/**
	 * Sets the profile.
	 *
	 * @param profile the new profile
	 */
	public void setProfile(List<NVProfile> profile) {
		this.profile = profile;
	}

	@Override
	public String toString() {
		return "NVProfileWrapper [id=" + id + ", profile=" + profile + "]";
	}

}

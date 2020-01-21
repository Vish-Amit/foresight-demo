/**
 * 
 */
package com.inn.foresight.module.favouriteView.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.core.generic.wrapper.RestWrapper;
import com.inn.foresight.module.favouriteView.model.FavouriteView.FavouriteViewType;

/**
 * The Class FavouriteViewWrapper.
 *
 * @author innoeye
 */
@JpaWrapper
@RestWrapper
public class FavouriteViewWrapper {

	/** The id. */
	private Integer id;
	
	/** The is deleted. */
	private Boolean isDeleted;
	
	/** The name. */
	private String name;
	
	/** The favourite type. */
	private String favouriteType;
	
	/** The configuration. */
	private String configuration;
	
	/** The is default. */
	private Boolean isDefault;

    /** The sharedBy.userId */
	private String sharedBy;
	
	/**
	 * Instantiates a new favourite view wrapper.
	 */
	public FavouriteViewWrapper() {
		super();
	}

	/**
	 * Instantiates a new favourite view wrapper.
	 *
	 * @param name the name
	 * @param favouriteType the favourite type
	 * @param configuration the configuration
	 * @param isDefault the is default
	 */
	public FavouriteViewWrapper(String name, String favouriteType, String configuration, Boolean isDefault) {
		super();
		this.name = name;
		this.favouriteType = favouriteType;
		this.configuration = configuration;
		this.isDefault = isDefault;
	}

	/**
	 * Instantiates a new favourite view wrapper.
	 *
	 * @param id the id
	 * @param name the name
	 * @param isDefault the is default
	 * @param isDeleted the is deleted
	 */
	public FavouriteViewWrapper(Integer id, String name, Boolean isDefault, Boolean isDeleted) {
		super();
		this.id = id;
		this.name = name;
		this.isDefault = isDefault;
		this.isDeleted = isDeleted;
	}

	/**
	 * Instantiates a new favourite view wrapper.
	 *
	 * @param id the id
	 * @param name the name
	 * @param configuration the configuration
	 * @param isDefault the is default
	 * @param isDeleted the is deleted
	 * @param favouriteType the favourite type
	 */
	public FavouriteViewWrapper(Integer id, String name, String configuration, Boolean isDefault, Boolean isDeleted, FavouriteViewType favouriteType, String sharedBy) {
		super();
		this.id = id;
		this.name = name;
		this.configuration = configuration;
		this.isDefault = isDefault;
		this.isDeleted = isDeleted;
		this.favouriteType = favouriteType.name();
		this.sharedBy = sharedBy;
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
	 * Gets the favourite type.
	 *
	 * @return the favourite type
	 */
	public String getFavouriteType() {
		return favouriteType;
	}

	/**
	 * Sets the favourite type.
	 *
	 * @param favouriteType the new favourite type
	 */
	public void setFavouriteType(String favouriteType) {
		this.favouriteType = favouriteType;
	}

	/**
	 * Gets the configuration.
	 *
	 * @return the configuration
	 */
	public String getConfiguration() {
		return configuration;
	}

	/**
	 * Sets the configuration.
	 *
	 * @param configuration the new configuration
	 */
	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	/**
	 * Gets the checks if is default.
	 *
	 * @return the checks if is default
	 */
	public Boolean getIsDefault() {
		return isDefault;
	}

	/**
	 * Sets the checks if is default.
	 *
	 * @param isDefault the new checks if is default
	 */
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	
    public String getSharedBy() {
        return sharedBy;
    }

    
    public void setSharedBy(String sharedBy) {
        this.sharedBy = sharedBy;
    }

    /**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "FavouriteViewWrapper [name=" + name + ", favouriteType=" + favouriteType + ", configuration=" + configuration + ", isDefault=" + isDefault + "]";
	}

	

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof FavouriteViewWrapper)) {
			return false;
		}
		FavouriteViewWrapper other = (FavouriteViewWrapper) obj;
		if (configuration == null) {
			if (other.configuration != null) {
				return false;
			}
		} else if (!configuration.equals(other.configuration)) {
			return false;
		}
		if (favouriteType == null) {
			if (other.favouriteType != null) {
				return false;
			}
		} else if (!favouriteType.equals(other.favouriteType)) {
			return false;
		}
		if (isDefault == null) {
			if (other.isDefault != null) {
				return false;
			}
		} else if (!isDefault.equals(other.isDefault)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
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

}

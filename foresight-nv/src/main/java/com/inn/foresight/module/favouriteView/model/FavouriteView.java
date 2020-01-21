/**
 * 
 */
package com.inn.foresight.module.favouriteView.model;

/**
 * @author root
 *
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.product.um.user.model.User;

/**
 * The Class FavouriteView.
 *
 * @author innoeye
 */

@NamedQueries({
		@NamedQuery(name = "findByFavouriteViewId", query = "select u from FavouriteView u where u.createdBy.userid =:userID and u.id=:viewid and u.isDeleted=false "),
		@NamedQuery(name = "getAllFavouritesForLoggedInUser", query = "select new com.inn.foresight.module.favouriteView.wrapper.FavouriteViewWrapper( u.id,  u.name,  u.configuration,u.isDefault,  u.isDeleted, u.favouriteType, u.sharedBy.email) from FavouriteView u where u.createdBy.userid =:userID and u.isDeleted=false order by u.modifiedTime desc "),
		@NamedQuery(name = "getDefaultFavourite", query = "select u from FavouriteView u where u.createdBy.userid = :userID and u.isDefault= true and u.isDeleted= false "),
		@NamedQuery(name = "getFavouriteViewByName", query = "select u from FavouriteView u where u.createdBy.userid =:userID and u.name=:name and u.isDeleted=false ")
		})
@XmlRootElement(name = "FavouriteView")
@Entity
@Table(name = "FavouriteView")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class FavouriteView implements Serializable {

	/** system generates serialVersionUID. */
	private static final long serialVersionUID = -6058356319223491592L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "favouriteviewid_pk")
	private Integer id;

	/** The name. */
	@Basic
	@Column(name = "name")
	private String name;

	/** The configuration. */
	@Lob
	@Column(name = "configuration")
	private String configuration;

	/** The is default. */
	@Basic
	@Column(name = "defaultview")
	private Boolean isDefault;

	/** The created by. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creatorid_fk")
	private User createdBy;

	/** The modified by. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lastmodifierid_fk")
	private User modifiedBy;

	/** The created time. */
	@Basic
	@Column(name = "creationtime")
	private Date createdTime;

	/** The is deleted. */
	@Basic
	@Column(name = "deleted")
	private Boolean isDeleted;

	/** The modified time. */
	@Basic
	@Column(name = "modificationtime")
	private Date modifiedTime;

	/** The favourite type. */
	@Enumerated(EnumType.STRING)
	private FavouriteViewType favouriteType;

	   /** The shared by. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sharedid_fk")
    private User sharedBy;
    
	/**
	 * The Enum FavouriteViewType.
	 */
	public enum FavouriteViewType {
		
		/** The Spatial. */
		Spatial(0), 
 /** The Dashboard. */
 Dashboard(1);

		/** The favourite type. */
		private int favouriteType;

		/**
		 * Instantiates a new favourite view type.
		 *
		 * @param favouriteView the favourite view
		 */
		FavouriteViewType(int favouriteView) {
			this.favouriteType = favouriteView;
		}

		/**
		 * Gets the list of below or equal view.
		 *
		 * @return the list of below or equal view
		 */
		public List<FavouriteViewType> getListOfBelowOrEqualView() {
			List<FavouriteViewType> favouriteViews = new ArrayList<>();
			for (FavouriteViewType type : FavouriteViewType.values()) {
				if (type.favouriteType >= this.favouriteType) {
					favouriteViews.add(type);
				}
			}
			return favouriteViews;
		}

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

	

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * Gets the created time.
	 *
	 * @return the created time
	 */
	public Date getCreatedTime() {
		return createdTime;
	}

	/**
	 * Sets the created time.
	 *
	 * @param createdTime the new created time
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
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
	 * Gets the modified time.
	 *
	 * @return the modified time
	 */
	public Date getModifiedTime() {
		return modifiedTime;
	}

	/**
	 * Sets the modified time.
	 *
	 * @param modifiedTime the new modified time
	 */
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	/**
	 * Gets the favourite type.
	 *
	 * @return the favourite type
	 */
	public FavouriteViewType getFavouriteType() {
		return favouriteType;
	}

	/**
	 * Sets the favourite type.
	 *
	 * @param favouriteType the new favourite type
	 */
	public void setFavouriteType(FavouriteViewType favouriteType) {
		this.favouriteType = favouriteType;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "FavouriteView [id=" + id + ", name=" + name + ", configuration=" + configuration + ", isDefault=" + isDefault + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy
				+ ", createdTime=" + createdTime + ", isDeleted=" + isDeleted + ", modifiedTime=" + modifiedTime + ", favouriteType=" + favouriteType + "]";
	}

	
	
    public User getSharedBy() {
        return sharedBy;
    }

    
    public void setSharedBy(User sharedBy) {
        this.sharedBy = sharedBy;
    }

    /**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((configuration == null) ? 0 : configuration.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((createdTime == null) ? 0 : createdTime.hashCode());
		result = prime * result + ((favouriteType == null) ? 0 : favouriteType.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isDefault == null) ? 0 : isDefault.hashCode());
		result = prime * result + ((isDeleted == null) ? 0 : isDeleted.hashCode());
		result = prime * result + ((modifiedBy == null) ? 0 : modifiedBy.hashCode());
		result = prime * result + ((modifiedTime == null) ? 0 : modifiedTime.hashCode());
		return prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (!(obj instanceof FavouriteView)) {
			return false;
		}
		FavouriteView other = (FavouriteView) obj;
		if (configuration == null) {
			if (other.configuration != null) {
				return false;
			}
		} else if (!configuration.equals(other.configuration)) {
			return false;
		}
		if (createdBy == null) {
			if (other.createdBy != null) {
				return false;
			}
		} else if (!createdBy.equals(other.createdBy)) {
			return false;
		}
		if (createdTime == null) {
			if (other.createdTime != null) {
				return false;
			}
		} else if (!createdTime.equals(other.createdTime)) {
			return false;
		}
		if (favouriteType != other.favouriteType) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (isDefault == null) {
			if (other.isDefault != null) {
				return false;
			}
		} else if (!isDefault.equals(other.isDefault)) {
			return false;
		}
		if (isDeleted == null) {
			if (other.isDeleted != null) {
				return false;
			}
		} else if (!isDeleted.equals(other.isDeleted)) {
			return false;
		}
		if (modifiedBy == null) {
			if (other.modifiedBy != null) {
				return false;
			}
		} else if (!modifiedBy.equals(other.modifiedBy)) {
			return false;
		}
		if (modifiedTime == null) {
			if (other.modifiedTime != null) {
				return false;
			}
		} else if (!modifiedTime.equals(other.modifiedTime)) {
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

}

package com.inn.foresight.core.infra.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.core.infra.constants.InBuildingConstants;

/** The Class Floor. */
@NamedQueries({
	    @NamedQuery(name = InBuildingConstants.GET_ALL_FLOOR_FOR_WING, query = "select new com.inn.foresight.core.infra.wrapper.FloorWrapper(f.id,f.floorName,f.creationTime,f.modificationTime) from Floor f where f.wing=:wingPlaceHolder and f.isDeleted=false"),
		@NamedQuery(name = InBuildingConstants.GET_FLOOR_DETAIL_BUILDINGWISE, query = "select f from Floor f where f.wing.building.id=:buildingId"),
		@NamedQuery(name = "getFloorDetailsByBuildingId", query = "SELECT new com.inn.foresight.core.infra.wrapper.FloorWrapper(f.id,f.floorNumber,f.floorName) FROM Floor f WHERE f.wing.building.buildingId=:buildingId AND f.isDeleted=false"),
		@NamedQuery(name = "getFloorCoverage", query = "SELECT new com.inn.foresight.core.infra.wrapper.BuildingWrapper(f.coverage) FROM Floor f WHERE f.wing.building.buildingId=:buildingId and f.id=:floorNumber"),
		@NamedQuery(name = "getNumberOfFloorsByBuildingId", query = "SELECT COUNT(f.id) FROM Floor f WHERE f.wing.building.buildingId=:buildingId")
		})
@Entity
@XmlRootElement(name = "Floor")
@Table(name = "Floor")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Floor  implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "floorid_pk")
	private Integer id;

	/** The floor name. */
	@Column(name = "name")
	private String floorName;

	/** The wing. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "wingid_fk")
	private Wing wing;

	/** The is deleted. */
	@Column(name = "deleted")
	private boolean isDeleted;
	
	/** The creation time. */
	@Column(name="modificationtime")
	private Date modificationTime;

	/** The creation time. */
	@Column(name="creationtime")
	private Date creationTime;
	
	@Column(name = "coverage")
    @Lob
    private String coverage;


	@Column(name = "number")
    private Integer floorNumber;
	
	@Column(name="floorlink")
	private String floorLink;
	
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
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}

	/**
	 * Gets the wing.
	 *
	 * @return the wingData
	 */
	public Wing getWing() {
		return wing;
	}

	/**
	 * Sets the wing.
	 *
	 * @param wingData
	 *            the wingData to set
	 */
	public void setWing(Wing wingData) {
		this.wing = wingData;
	}

	/**
	 * Gets the checks if is deleted.
	 *
	 * @return the isDeleted
	 */
	public boolean getIsDeleted() {
		return isDeleted;
	}

	/**
	 * Sets the checks if is deleted.
	 *
	 * @param isDeleted
	 *            the isDeleted to set
	 */
	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * @return the modificationTime
	 */
	public Date getModificationTime() {
		return modificationTime;
	}

	/**
	 * @param modificationTime the modificationTime to set
	 */
	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * @return the creationTime
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	/**
	 * @param creationTime the creationTime to set
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

    
    public String getCoverage() {
        return coverage;
    }

    
    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    
    public Integer getFloorNumber() {
        return floorNumber;
    }

    
    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }

	public String getFloorLink() {
		return floorLink;
	}

	public void setFloorLink(String floorLink) {
		this.floorLink = floorLink;
	}

	@Override
	public String toString() {
		return "Floor [id=" + id + ", floorName=" + floorName + ", wing=" + wing + ", isDeleted=" + isDeleted
				+ ", modificationTime=" + modificationTime + ", creationTime=" + creationTime + ", coverage=" + coverage
				+ ", floorNumber=" + floorNumber + ", floorLink=" + floorLink + "]";
	}

    
    
}

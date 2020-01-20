package com.inn.foresight.module.nv.workorder.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.product.um.geography.model.GeographyL4;
import com.inn.product.um.user.model.User;

/** The Class CustomGeography. */

@NamedQueries({
	
	@NamedQuery(name = "findAllCustomGeographyForUser",
		query = "SELECT c FROM CustomGeography c where c.creator.userid=:userId order by c.creationTime desc"),
	
//	@NamedQuery(name = "findAllCustomGeographyL1",
//		query = "SELECT c FROM CustomGeography c join c.geographyL4 l4 join l4.geographyL3 l3 join l3.geographyL2 l2 join "
//				+ "l2.geographyL1 l1 where l1.id = :geographyId and c.type in (:typeList) and c.isDeleted=false order by c.creationTime desc"),

//	@NamedQuery(name = "findAllCustomGeographyL2",
//		query = "SELECT c FROM CustomGeography c join c.geographyL4 l4 join l4.geographyL3 l3 join l3.geographyL2 l2 where "
//			+ "l2.id = :geographyId and c.type in (:typeList) and c.isDeleted=false order by c.creationTime desc"),
	
//	@NamedQuery(name = "findAllCustomGeographyL3",
//		query = "SELECT c FROM CustomGeography c join c.geographyL4 l4 join l4.geographyL3 l3 where l3.id = :geographyId "
//				+ "and c.type in (:typeList) and c.isDeleted=false order by c.creationTime desc"),
//	@NamedQuery(name = "findAllCustomGeographyL4",
//		query = "SELECT c FROM CustomGeography c where c.geographyL4.id = :geographyId and c.type in (:typeList) and c.isDeleted=false order by c.creationTime desc"),
	
//	@NamedQuery(name = "findAllCustomGeography",
//		query = "SELECT c FROM CustomGeography c where c.type in (:typeList) and c.isDeleted=false order by c.creationTime desc")
	
	
	@NamedQuery(name = "findAllCustomGeographyL1",
	query = "SELECT new com.inn.foresight.module.nv.workorder.wrapper.CustomGeographyWrapper(c.id,c.creationTime,c.modifiedTime,c.name,c.type,c.routeType,c.landmark,c.startLatitude,c.startLongitude,c.endLatitude,c.endLongitude) FROM CustomGeography c join c.geographyL4 l4 join l4.geographyL3 l3 join l3.geographyL2 l2 join "
			+ "l2.geographyL1 l1 where l1.id = :geographyId and c.type in (:typeList) and c.isDeleted=false order by c.creationTime desc"),
 	
	@NamedQuery(name = "findAllCustomGeographyL2",
	query = "SELECT new com.inn.foresight.module.nv.workorder.wrapper.CustomGeographyWrapper(c.id,c.creationTime,c.modifiedTime,c.name,c.type,c.routeType,c.landmark,c.startLatitude,c.startLongitude,c.endLatitude,c.endLongitude) FROM CustomGeography c join c.geographyL4 l4 join l4.geographyL3 l3 join l3.geographyL2 l2 where "
		+ "l2.id = :geographyId and c.type in (:typeList) and c.isDeleted=false order by c.creationTime desc"),
	
	
	@NamedQuery(name = "findAllCustomGeographyL3",
	query = "SELECT new com.inn.foresight.module.nv.workorder.wrapper.CustomGeographyWrapper(c.id,c.creationTime,c.modifiedTime,c.name,c.type,c.routeType,c.landmark,c.startLatitude,c.startLongitude,c.endLatitude,c.endLongitude) FROM CustomGeography c join c.geographyL4 l4 join l4.geographyL3 l3 where l3.id = :geographyId "
			+ "and c.type in (:typeList) and c.isDeleted=false order by c.creationTime desc"),
	
	
	@NamedQuery(name = "findAllCustomGeographyL4",
	query = "SELECT new com.inn.foresight.module.nv.workorder.wrapper.CustomGeographyWrapper(c.id,c.creationTime,c.modifiedTime,c.name,c.type,c.routeType,c.landmark,c.startLatitude,c.startLongitude,c.endLatitude,c.endLongitude) FROM CustomGeography c where c.geographyL4.id = :geographyId and c.type in (:typeList) and c.isDeleted=false order by c.creationTime desc"),



	@NamedQuery(name = "findAllCustomGeography",
		query = "SELECT new com.inn.foresight.module.nv.workorder.wrapper.CustomGeographyWrapper(c.id,c.creationTime,c.modifiedTime,c.name,c.type,c.routeType,c.landmark,c.startLatitude,c.startLongitude,c.endLatitude,c.endLongitude) FROM CustomGeography c where c.type in (:typeList) and c.isDeleted=false order by c.creationTime desc")
	
})
@Entity
@Table(name = "CustomGeography")
@XmlRootElement(name = "CustomGeography")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class CustomGeography {

	/** The Enum GeographyType. */
	public enum GeographyType {
		
		/** The polygon. */
		POLYGON("PL"), /** The point. */
		POINT("PT"), /** The line. */
		LINE("LN"),
		KML("KML");
		
		/** The value. */
		private String value;

		/**
		 * Instantiates a new geography type.
		 *
		 * @param value the value
		 */
		private GeographyType(String value) {
			this.value = value;
		}

		/** Instantiates a new geography type. */
		private GeographyType() {
		}

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

	}
	
	
	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "customgeographyid_pk")
	private Integer id;

	/** The name. */
	@Basic
	private String name;

	/** The type. */
	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private GeographyType type;
	
	/** The landmark. */
	@Basic
	private String landmark;
	
	/** The landmark. */
	@Column(name = "routetype")
	private String routeType;

	/** The child geography. */
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "customgeographyid_fk", nullable = false)
	private CustomGeography childGeography;

	/** The creation time. */
	@Column(name = "creationtime")
	private Date creationTime;

	/** The modified time. */
	@Column(name = "modificationtime")
	private Date modifiedTime;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creatorid_fk")
	private User creator;
	
	@Column(name = "startlatitude")
	private Double startLatitude;
	
	@Column(name = "startlongitude")
	private Double startLongitude;
	
	@Column(name = "endlatitude")
	private Double endLatitude;
	
	@Column(name = "endlongitude")
	private Double endLongitude;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl4id_fk")
	private GeographyL4 geographyL4;
	
    @Column(name="deleted")
    private boolean isDeleted;
	

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
	 * Gets the type.
	 *
	 * @return the type
	 */
	public GeographyType getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(GeographyType type) {
		this.type = type;
	}

	/**
	 * Gets the landmark.
	 *
	 * @return the landmark
	 */
	public String getLandmark() {
		return landmark;
	}

	/**
	 * Sets the landmark.
	 *
	 * @param landmark the new landmark
	 */
	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	/**
	 * Gets the child geography.
	 *
	 * @return the child geography
	 */
	public CustomGeography getChildGeography() {
		return childGeography;
	}

	/**
	 * Sets the child geography.
	 *
	 * @param childGeography the new child geography
	 */
	public void setChildGeography(CustomGeography childGeography) {
		this.childGeography = childGeography;
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

	public User getCreator() {
		return creator;
	}
	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	public Double getStartLatitude() {
		return startLatitude;
	}

	public void setStartLatitude(Double startLatitude) {
		this.startLatitude = startLatitude;
	}

	public Double getStartLongitude() {
		return startLongitude;
	}

	public void setStartLongitude(Double startLongitude) {
		this.startLongitude = startLongitude;
	}
	
	public GeographyL4 getGeographyL4() {
		return geographyL4;
	}

	public void setGeographyL4(GeographyL4 geographyL4) {
		this.geographyL4 = geographyL4;
	}
	
	public Double getEndLatitude() {
		return endLatitude;
	}

	public void setEndLatitude(Double endLatitude) {
		this.endLatitude = endLatitude;
	}

	public Double getEndLongitude() {
		return endLongitude;
	}

	public void setEndLongitude(Double endLongitude) {
		this.endLongitude = endLongitude;
	}

	public String getRouteType() {
		return routeType;
	}

	public void setRouteType(String routeType) {
		this.routeType = routeType;
	}

	/**
	 * @return the isDeleted
	 */
	public boolean isDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
}

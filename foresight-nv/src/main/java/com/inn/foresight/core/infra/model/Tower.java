package com.inn.foresight.core.infra.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.product.um.geography.model.GeographyL4;

@NamedQueries({
	@NamedQuery(name="getTowerDetailsByProvider",query="select t from Tower t left join fetch t.towerProvider tp left join fetch t.geographyL4 l4 left join fetch l4.geographyL3 l3 left join fetch l3.geographyL2 l2  left join fetch l2.geographyL1 l1 where tp.name in(:name) and t.latitude>=:minLatitude  and t.latitude<=:maxLatitude  and t.longitude>=:minLongitude  and t.longitude<=:maxLongitude"),
	@NamedQuery(name="getTowerDetailByTowerId",query="select t from Tower t  left join fetch t.towerProvider tp left join fetch t.geographyL4 l4 left join fetch l4.geographyL3 l3 left join fetch l3.geographyL2 l2  left join fetch l2.geographyL1 l1 where t.towerId=:towerId "),
	@NamedQuery(name="getAllTowerProviderDetails",query="select new com.inn.foresight.core.infra.wrapper.TowerProviderWrapper(t.name,t.legendColor) from TowerProvider t where t.name is not null"),
	
})
@Entity
@Table(name = "Tower")
@XmlRootElement(name = "Tower")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class Tower implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -328442312967686645L;
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "towerid_pk")
	private Integer id;

	@Basic
	@Column(name = "towerid")
	private String towerId;

	@Basic
	@Column(name = "towername")
	private String towerName;
	
	@Basic
	@Column(name = "latitude")
	private Double latitude;

	@Basic
	@Column(name = "longitude")
	private Double longitude;
	
	@Basic
	@Column(name = "towerheight")
	private Double towerHeight;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl4id_fk", nullable = true)
	private GeographyL4 geographyL4;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "towerproviderid_fk", nullable = true)
	private TowerProvider towerProvider;
	
	@Basic
	@Column(name = "creationtime")
	private Date creationTime;

	@Basic
	@Column(name = "modificationtime")
	private Date modificationTime;

	@Basic
	@Column(name = "tenantscount")
	private Integer tenantsCount;

	@Basic
	@Column(name = "shareablestatus")
	private String shareableStatus;

	
	@Basic
	@Column(name = "structuretype")
	private String structureType;

	@Basic
	@Column(name = "ranking")
	private Integer ranking;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTowerId() {
		return towerId;
	}

	public void setTowerId(String towerId) {
		this.towerId = towerId;
	}

	public String getTowerName() {
		return towerName;
	}

	public void setTowerName(String towerName) {
		this.towerName = towerName;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getTowerHeight() {
		return towerHeight;
	}

	public void setTowerHeight(Double towerHeight) {
		this.towerHeight = towerHeight;
	}

	public GeographyL4 getGeographyL4() {
		return geographyL4;
	}

	public void setGeographyL4(GeographyL4 geographyL4) {
		this.geographyL4 = geographyL4;
	}

	public TowerProvider getTowerProvider() {
		return towerProvider;
	}

	public void setTowerProvider(TowerProvider towerProvider) {
		this.towerProvider = towerProvider;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	public Integer getTenantsCount() {
		return tenantsCount;
	}

	public void setTenantsCount(Integer tenantsCount) {
		this.tenantsCount = tenantsCount;
	}

	public String getShareableStatus() {
		return shareableStatus;
	}

	public void setShareableStatus(String shareableStatus) {
		this.shareableStatus = shareableStatus;
	}

	public String getStructureType() {
		return structureType;
	}

	public void setStructureType(String structureType) {
		this.structureType = structureType;
	}

	public Integer getRanking() {
		return ranking;
	}

	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}

	@Override
	public String toString() {
		return "TowerDetail [id=" + id + ", towerId=" + towerId + ", towerName=" + towerName + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", towerHeight=" + towerHeight + ", geographyL4=" + geographyL4
				+ ", towerProvider=" + towerProvider + ", creationTime=" + creationTime + ", modificationTime="
				+ modificationTime + ", tenantsCount=" + tenantsCount + ", shareableStatus=" + shareableStatus
				+ ", structureType=" + structureType + ", ranking=" + ranking + "]";
	}

	

}

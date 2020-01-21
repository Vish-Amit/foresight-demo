package com.inn.foresight.core.infra.model;

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
import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.core.generic.wrapper.RestWrapper;

@NamedQueries({
	@NamedQuery(name="getNeighbourCellDetailsForSourceCells",query="select n from NeighbourCellDetail n where n.weekNo=:weekNo and n.cellName in (:cellName)"),
	@NamedQuery(name="getNeighbourCellDetailIdByNEId",query="select n.neighbourid_fk.id from NeighbourCellMapping n where n.networkelementid_fk.id =:networkelementid_fk "),
	@NamedQuery(name="getNeighbourCellDetails",query="select r from RANDetail r where r.networkElement.id in (:networkElementidList) and r.networkElement.domain=:domain and r.networkElement.isDeleted=0 "),
	//	@NamedQuery(name="getNeighbourCellDetailsForSourceCells",query="select  from NeighbourCellDetail n where n.weekNo=:weekNo and n.cellName in (:cellName)")	
})
@JpaWrapper
@RestWrapper
@XmlRootElement(name = "NeighbourCellDetail")
@Entity
@Table(name = "NeighbourCellDetail")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class NeighbourCellDetail {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5808080880856604005L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "neighbourcelldetailid_pk")
	private Integer id;
	
	@Basic
	@Column(name = "bearingangle1")
	private Double bearingAngle1;
	
	@Basic
	@Column(name = "bearingangle2")
	private Double bearingAngle2;
	
	@Basic
	@Column(name = "bearingangle3")
	private Double bearingAngle3;
	
	@Basic
	@Column(name = "cellname")
	private String cellName;
	
	@Basic
	@Column(name = "distance1")
	private Double distance1;
	
	@Basic
	@Column(name = "distance2")
	private Double distance2;
	
	@Basic
	@Column(name = "distance3")
	private Double distance3;
	
	@Basic
	@Column(name = "facingcellname1")
	private String facingCellName1;
	
	@Basic
	@Column(name = "facingcellname2")
	private String facingCellName2;
	
	@Basic
	@Column(name = "facingcellname3")
	private String facingCellName3;
	
	@Basic
	@Column(name = "facingsite1", columnDefinition = "TINYINT")
	private Boolean facingSite1;
	
	@Basic
	@Column(name = "facingsite2", columnDefinition = "TINYINT")
	private Boolean facingSite2;
	
	@Basic
	@Column(name = "facingsite3", columnDefinition = "TINYINT")
	private Boolean facingSite3;
	
	@Basic
	@Column(name = "neighbour")
	private Integer neighbour;
	
	@Basic
	@Column(name = "sapid1")
	private String sapid1;
	
	@Basic
	@Column(name = "sapid2")
	private String sapid2;
	
	@Basic
	@Column(name = "sapid3")
	private String sapid3;
	
	@Basic
	@Column(name = "creationTime")
	private Date creationTime;
	
	@Basic
	@Column(name = "modificationTime")
	private Date modificationTime;
	
	@Basic
	@Column(name = "autoCellRange")
	private Double autoCellRange;
	
	@Basic
	@Column(name = "weekNo")
	private Integer weekNo;
	
	@JoinColumn(name = "networkelementid_fk", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private NetworkElement networkElement;

	@JoinColumn(name = "randetailid_fk", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private RANDetail ranDetail;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getBearingAngle1() {
		return bearingAngle1;
	}

	public void setBearingAngle1(Double bearingAngle1) {
		this.bearingAngle1 = bearingAngle1;
	}

	public Double getBearingAngle2() {
		return bearingAngle2;
	}

	public void setBearingAngle2(Double bearingAngle2) {
		this.bearingAngle2 = bearingAngle2;
	}

	public Double getBearingAngle3() {
		return bearingAngle3;
	}

	public void setBearingAngle3(Double bearingAngle3) {
		this.bearingAngle3 = bearingAngle3;
	}

	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	public Double getDistance1() {
		return distance1;
	}

	public void setDistance1(Double distance1) {
		this.distance1 = distance1;
	}

	public Double getDistance2() {
		return distance2;
	}

	public void setDistance2(Double distance2) {
		this.distance2 = distance2;
	}

	public Double getDistance3() {
		return distance3;
	}

	public void setDistance3(Double distance3) {
		this.distance3 = distance3;
	}

	public String getFacingCellName1() {
		return facingCellName1;
	}

	public void setFacingCellName1(String facingCellName1) {
		this.facingCellName1 = facingCellName1;
	}

	public String getFacingCellName2() {
		return facingCellName2;
	}

	public void setFacingCellName2(String facingCellName2) {
		this.facingCellName2 = facingCellName2;
	}

	public String getFacingCellName3() {
		return facingCellName3;
	}

	public void setFacingCellName3(String facingCellName3) {
		this.facingCellName3 = facingCellName3;
	}

	public Boolean getFacingSite1() {
		return facingSite1;
	}

	public void setFacingSite1(Boolean facingSite1) {
		this.facingSite1 = facingSite1;
	}

	public Boolean getFacingSite2() {
		return facingSite2;
	}

	public void setFacingSite2(Boolean facingSite2) {
		this.facingSite2 = facingSite2;
	}

	public Boolean getFacingSite3() {
		return facingSite3;
	}

	public void setFacingSite3(Boolean facingSite3) {
		this.facingSite3 = facingSite3;
	}

	public Integer getNeighbour() {
		return neighbour;
	}

	public void setNeighbour(Integer neighbour) {
		this.neighbour = neighbour;
	}

	public String getSapid1() {
		return sapid1;
	}

	public void setSapid1(String sapid1) {
		this.sapid1 = sapid1;
	}

	public String getSapid2() {
		return sapid2;
	}

	public void setSapid2(String sapid2) {
		this.sapid2 = sapid2;
	}

	public String getSapid3() {
		return sapid3;
	}

	public void setSapid3(String sapid3) {
		this.sapid3 = sapid3;
	}

	public Date getCreationtime() {
		return creationTime;
	}

	public void setCreationtime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModificationtime() {
		return modificationTime;
	}

	public void setModificationtime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	public Double getAutocellrange() {
		return autoCellRange;
	}

	public void setAutocellrange(Double autoCellRange) {
		this.autoCellRange = autoCellRange;
	}

	public Integer getWeekno() {
		return weekNo;
	}

	public void setWeekno(Integer weekNo) {
		this.weekNo = weekNo;
	}

	public NetworkElement getNetworkElement() {
		return networkElement;
	}

	public void setNetworkElement(NetworkElement networkElement) {
		this.networkElement = networkElement;
	}

	public RANDetail getRanDetail() {
		return ranDetail;
	}

	public void setRanDetail(RANDetail ranDetail) {
		this.ranDetail = ranDetail;
	}
	
	@Override
	public String toString() {
		return "NeighbourCellDetail [id=" + id + ", bearingAngle1=" + bearingAngle1 + ", bearingAngle2=" + bearingAngle2
				+ ", bearingAngle3=" + bearingAngle3 + ", cellName=" + cellName + ", distance1=" + distance1
				+ ", distance2=" + distance2 + ", distance3=" + distance3 + ", facingCellName1=" + facingCellName1
				+ ", facingCellName2=" + facingCellName2 + ", facingCellName3=" + facingCellName3 + ", facingSite1="
				+ facingSite1 + ", facingSite2=" + facingSite2 + ", facingSite3=" + facingSite3 + ", neighbour="
				+ neighbour + ", sapid1=" + sapid1 + ", sapid2=" + sapid2 + ", sapid3=" + sapid3 + ", creationtime="
				+ creationTime + ", modificationtime=" + modificationTime + ", autoCellRange=" + autoCellRange
				+ ", weekNo=" + weekNo + "]";
	}

	
}

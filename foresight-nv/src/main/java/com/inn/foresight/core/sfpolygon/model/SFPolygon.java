package com.inn.foresight.core.sfpolygon.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@NamedQuery(name = "sbmPolyCount",
    query = "select cast(count(sfp) as int) from SFPolygon sfp where sfp.isSalesMapped=false and sfp.isDeleted=false")

@NamedQuery(name = "nbmPolyCount",
    query = "select cast(count(sfp) as int) from SFPolygon sfp where sfp.isNetworkMapped=false and sfp.isDeleted=false")

@NamedQuery(name = "sbmPolygonDetails",
    query = "select sfp from SFPolygon sfp where sfp.isSalesMapped=false and sfp.isDeleted=false order by sfp.creationTime desc")

@NamedQuery(name = "nbmPolygonDetails",
    query = "select sfp from SFPolygon sfp where sfp.isNetworkMapped=false and sfp.isDeleted=false order by sfp.creationTime desc")

@NamedQuery(name = "polygonByPolyId",
    query = "select sfp from SFPolygon sfp where sfp.polyId=(:polyId)")

@NamedNativeQuery(name = "updatePolygonStatus",
    query = "update SFPolygon set salesmapped= case when 'sales'=(:geography) then true else salesmapped end, networkmapped=case when 'nw'=(:geography) then true else networkmapped end where polyid=(:polyId)")

@Entity
@Table(name = "SFPolygon")
@XmlRootElement(name = "SFPolygon")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class SFPolygon implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 5139413193283289337L;

  /** The id. */
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  @Column(name = "sfpolygonid_pk")
  private Integer id;

  /** The polygon id. */
  @Basic
  private String polyId;

  /** The name. */
  @Basic
  private String name;

  /** The code. */
  @Basic
  private String cord;

  /** The longitude. */
  @Basic
  private Double longitude;

  /** The latitude. */
  @Basic
  private Double latitude;

  /** The username. */
  @Basic
  @Column(name = "username")
  private String userName;

  /** The status. */
  @Basic
  private String status;

  /** The metadata. */
  @Basic
  @Column(name = "metadata")
  private String metaData;

  /** The is sales mapped. */
  @Basic
  @Column(name = "salesmapped")
  private Boolean isSalesMapped;

  /** The is network mapped. */
  @Basic
  @Column(name = "networkmapped")
  private Boolean isNetworkMapped;

  /** The is deleted. */
  @Basic
  @Column(name = "deleted")
  private Boolean isDeleted;

  /** The creation time. */
  @Basic
  @Column(name = "creationtime")
  private Date creationTime;

  /** The modification time. */
  @Basic
  @Column(name = "modificationtime")
  private Date modificationTime;

  @Basic
  @Column(name = "polygonmodificationtime")
  private Date polygonModificationTime;

  /** The remarks. */
  @Basic
  @Column(name = "remarks")
  private String comments;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getPolyId() {
    return polyId;
  }

  public void setPolyId(String polyId) {
    this.polyId = polyId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCord() {
    return cord;
  }

  public void setCord(String cord) {
    this.cord = cord;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Boolean getIsSalesMapped() {
    return isSalesMapped;
  }

  public void setIsSalesMapped(Boolean isSalesMapped) {
    this.isSalesMapped = isSalesMapped;
  }

  public Boolean getIsNetworkMapped() {
    return isNetworkMapped;
  }

  public void setIsNetworkMapped(Boolean isNetworkMapped) {
    this.isNetworkMapped = isNetworkMapped;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
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

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public String getMetaData() {
    return metaData;
  }

  public void setMetaData(String metaData) {
    this.metaData = metaData;
  }

  public Date getPolygonModificationTime() {
    return polygonModificationTime;
  }

  public void setPolygonModificationTime(Date polygonModificationTime) {
    this.polygonModificationTime = polygonModificationTime;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("SFPolygon [");
    if (id != null) {
      builder.append("id=");
      builder.append(id);
      builder.append(", ");
    }
    if (polyId != null) {
      builder.append("polyId=");
      builder.append(polyId);
      builder.append(", ");
    }
    if (name != null) {
      builder.append("name=");
      builder.append(name);
      builder.append(", ");
    }
    if (cord != null) {
      builder.append("cord=");
      builder.append(cord);
      builder.append(", ");
    }
    if (longitude != null) {
      builder.append("longitude=");
      builder.append(longitude);
      builder.append(", ");
    }
    if (latitude != null) {
      builder.append("latitude=");
      builder.append(latitude);
      builder.append(", ");
    }
    if (userName != null) {
      builder.append("userName=");
      builder.append(userName);
      builder.append(", ");
    }
    if (status != null) {
      builder.append("status=");
      builder.append(status);
      builder.append(", ");
    }
    if (metaData != null) {
      builder.append("metaData=");
      builder.append(metaData);
      builder.append(", ");
    }
    if (isSalesMapped != null) {
      builder.append("isSalesMapped=");
      builder.append(isSalesMapped);
      builder.append(", ");
    }
    if (isNetworkMapped != null) {
      builder.append("isNetworkMapped=");
      builder.append(isNetworkMapped);
      builder.append(", ");
    }
    if (isDeleted != null) {
      builder.append("isDeleted=");
      builder.append(isDeleted);
      builder.append(", ");
    }
    if (creationTime != null) {
      builder.append("creationTime=");
      builder.append(creationTime);
      builder.append(", ");
    }
    if (modificationTime != null) {
      builder.append("modificationTime=");
      builder.append(modificationTime);
      builder.append(", ");
    }
    if (polygonModificationTime != null) {
      builder.append("polygonModificationTime=");
      builder.append(polygonModificationTime);
      builder.append(", ");
    }
    if (comments != null) {
      builder.append("comments=");
      builder.append(comments);
    }
    builder.append("]");
    return builder.toString();
  }

}

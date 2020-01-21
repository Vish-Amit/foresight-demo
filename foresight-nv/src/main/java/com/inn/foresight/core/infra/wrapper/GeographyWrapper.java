package com.inn.foresight.core.infra.wrapper;

import java.util.Date;
import java.util.List;
import com.inn.core.generic.wrapper.RestWrapper;


/**
 * The Class GeographyWrapper.
 */

@RestWrapper
public class GeographyWrapper {

  /** The id. */
  private Integer id;

  /** The name. */
  private String name;

  /** The boundary. */
  private List<List<Double>> boundary;

  /** The longitude. */
  private Double longitude;

  /** The latitude. */
  private Double latitude;

  /** The ne latitude. */
  private Double nELat;

  /** The longtitude. */
  private Double nELng;

  /** The sw latitude. */
  private Double sWLat;

  /** The sw longtitude. */
  private Double sWLng;

  /** The subpath . */
  private String subPath;

  /** The table name . */
  private String tableName;

  /** The code. */
  private String code;

  /** The area . */
  private Double area;

  /** The creation time. */
  private Date creationTime;

  /** The modification time. */
  private Date modificationTime;

  /** The parent id. */
  private Integer parentId;

  /** The is exact. */
  private Boolean isExact;

  /** The tile id. */
  private String tileId;

  /** The img column. */
  private String imgColumn;

  /** The type. */
  private String type;

  /** The is big tile. */
  private Boolean isBigTile;

  /** The base zoom. */
  private Integer baseZoom;

  /** The is color updation. */
  private Boolean isColorUpdation;

  /** The accuracy factor. */
  private Double accuracyFactor;

  /** The geography pk list. */
  private List<Integer> geographyPkList;

  /** The boundary column list. */
  private List<String> boundaryColumnList;

  /** The row key prefix list. */
  private List<String> rowKeyPrefixList;

  /**
   * Gets the row key prefix list.
   *
   * @return the row key prefix list
   */
  public List<String> getRowKeyPrefixList() {
    return rowKeyPrefixList;
  }

  /**
   * Sets the row key prefix list.
   *
   * @param rowKeyPrefixList the new row key prefix list
   */
  public void setRowKeyPrefixList(List<String> rowKeyPrefixList) {
    this.rowKeyPrefixList = rowKeyPrefixList;
  }

  /**
   * Gets the geography pk list.
   *
   * @return the geography pk list
   */
  public List<Integer> getGeographyPkList() {
    return geographyPkList;
  }

  /**
   * Sets the geography pk list.
   *
   * @param geographyPkList the new geography pk list
   */
  public void setGeographyPkList(List<Integer> geographyPkList) {
    this.geographyPkList = geographyPkList;
  }

  /**
   * Gets the boundary column list.
   *
   * @return the boundary column list
   */
  public List<String> getBoundaryColumnList() {
    return boundaryColumnList;
  }

  /**
   * Sets the boundary column list.
   *
   * @param boundaryColumnList the new boundary column list
   */
  public void setBoundaryColumnList(List<String> boundaryColumnList) {
    this.boundaryColumnList = boundaryColumnList;
  }

  /**
   * Instantiates a new geography wrapper.
   *
   * @param name the name
   * @param longitude the longitude
   * @param latitude the latitude
   */
  public GeographyWrapper(String name, Double longitude, Double latitude) {
    this.name = name;
    this.longitude = longitude;
    this.latitude = latitude;
  }

  /**
   * Instantiates a new geography wrapper.
   *
   * @param id the id
   * @param name the name
   */
  public GeographyWrapper(Integer id, String name) {
    super();
    this.id = id;
    this.name = name;
  }

  /**
   * Instantiates a new geography wrapper.
   *
   * @param id the id
   * @param name the name
   * @param latitude the latitude
   * @param longitude the longitude
   */
  public GeographyWrapper(Integer id, String name, Double latitude, Double longitude) {
    super();
    this.id = id;
    this.name = name;
    this.latitude = latitude;
    this.longitude = longitude;

  }

  /**
   * Instantiates a new geography wrapper.
   */
  public GeographyWrapper() {
    super();
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
   * Gets the boundary.
   *
   * @return the boundary
   */
  public List<List<Double>> getBoundary() {
    return boundary;
  }

  /**
   * Sets the boundary.
   *
   * @param boundary the new boundary
   */
  public void setBoundary(List<List<Double>> boundary) {
    this.boundary = boundary;
  }

  /**
   * Gets the longitude.
   *
   * @return the longitude
   */
  public Double getLongitude() {
    return longitude;
  }

  /**
   * Sets the longitude.
   *
   * @param longitude the new longitude
   */
  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  /**
   * Gets the latitude.
   *
   * @return the latitude
   */
  public Double getLatitude() {
    return latitude;
  }

  /**
   * Sets the latitude.
   *
   * @param latitude the new latitude
   */
  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  /**
   * Gets the code.
   *
   * @return the code
   */
  public String getCode() {
    return code;
  }

  /**
   * Sets the code.
   *
   * @param code the new code
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Gets the n E lat.
   *
   * @return the n E lat
   */
  public Double getnELat() {
    return nELat;
  }

  /**
   * Sets the n E lat.
   *
   * @param nELat the new n E lat
   */
  public void setnELat(Double nELat) {
    this.nELat = nELat;
  }

  /**
   * Gets the n E lng.
   *
   * @return the n E lng
   */
  public Double getnELng() {
    return nELng;
  }

  /**
   * Sets the n E lng.
   *
   * @param nELng the new n E lng
   */
  public void setnELng(Double nELng) {
    this.nELng = nELng;
  }

  /**
   * Gets the s W lat.
   *
   * @return the s W lat
   */
  public Double getsWLat() {
    return sWLat;
  }

  /**
   * Sets the s W lat.
   *
   * @param sWLat the new s W lat
   */
  public void setsWLat(Double sWLat) {
    this.sWLat = sWLat;
  }

  /**
   * Gets the s W lng.
   *
   * @return the s W lng
   */
  public Double getsWLng() {
    return sWLng;
  }

  /**
   * Sets the s W lng.
   *
   * @param sWLng the new s W lng
   */
  public void setsWLng(Double sWLng) {
    this.sWLng = sWLng;
  }

  /**
   * Gets the sub path.
   *
   * @return the sub path
   */
  public String getSubPath() {
    return subPath;
  }

  /**
   * Sets the sub path.
   *
   * @param subPath the new sub path
   */
  public void setSubPath(String subPath) {
    this.subPath = subPath;
  }

  /**
   * Gets the table name.
   *
   * @return the table name
   */
  public String getTableName() {
    return tableName;
  }

  /**
   * Sets the table name.
   *
   * @param tableName the new table name
   */
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  /**
   * Gets the area.
   *
   * @return the area
   */
  public Double getArea() {
    return area;
  }

  /**
   * Sets the area.
   *
   * @param area the new area
   */
  public void setArea(Double area) {
    this.area = area;
  }

  /**
   * Gets the checks if is exact.
   *
   * @return the checks if is exact
   */
  public Boolean getIsExact() {
    return isExact;
  }

  /**
   * Sets the checks if is exact.
   *
   * @param isExact the new checks if is exact
   */
  public void setIsExact(Boolean isExact) {
    this.isExact = isExact;
  }

  /**
   * Gets the tile id.
   *
   * @return the tile id
   */
  public String getTileId() {
    return tileId;
  }

  /**
   * Sets the tile id.
   *
   * @param tileId the new tile id
   */
  public void setTileId(String tileId) {
    this.tileId = tileId;
  }

  /**
   * Gets the img column.
   *
   * @return the img column
   */
  public String getImgColumn() {
    return imgColumn;
  }

  /**
   * Sets the img column.
   *
   * @param imgColumn the new img column
   */
  public void setImgColumn(String imgColumn) {
    this.imgColumn = imgColumn;
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
   * Gets the checks if is big tile.
   *
   * @return the checks if is big tile
   */
  public Boolean getIsBigTile() {
    return isBigTile;
  }

  /**
   * Sets the checks if is big tile.
   *
   * @param isBigTile the new checks if is big tile
   */
  public void setIsBigTile(Boolean isBigTile) {
    this.isBigTile = isBigTile;
  }

  /**
   * Gets the base zoom.
   *
   * @return the base zoom
   */
  public Integer getBaseZoom() {
    return baseZoom;
  }

  /**
   * Sets the base zoom.
   *
   * @param baseZoom the new base zoom
   */
  public void setBaseZoom(Integer baseZoom) {
    this.baseZoom = baseZoom;
  }

  /**
   * Gets the checks if is color updation.
   *
   * @return the checks if is color updation
   */
  public Boolean getIsColorUpdation() {
    return isColorUpdation;
  }

  /**
   * Sets the checks if is color updation.
   *
   * @param isColorUpdation the new checks if is color updation
   */
  public void setIsColorUpdation(Boolean isColorUpdation) {
    this.isColorUpdation = isColorUpdation;
  }

  /**
   * Gets the accuracy factor.
   *
   * @return the accuracy factor
   */
  public Double getAccuracyFactor() {
    return accuracyFactor;
  }

  /**
   * Sets the accuracy factor.
   *
   * @param accuracyFactor the new accuracy factor
   */
  public void setAccuracyFactor(Double accuracyFactor) {
    this.accuracyFactor = accuracyFactor;
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
   * @param modificationTime the new modification time
   */
  public void setModificationTime(Date modificationTime) {
    this.modificationTime = modificationTime;
  }

  /**
   * Gets the parent id.
   *
   * @return the parent id
   */
  public Integer getParentId() {
    return parentId;
  }

  /**
   * Sets the parent id.
   *
   * @param parentId the new parent id
   */
  public void setParentId(Integer parentId) {
    this.parentId = parentId;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("GeographyWrapper [");
    if (id != null) {
      builder.append("id=");
      builder.append(id);
      builder.append(", ");
    }
    if (name != null) {
      builder.append("name=");
      builder.append(name);
      builder.append(", ");
    }
    if (boundary != null) {
      builder.append("boundary=");
      builder.append(boundary);
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
    if (nELat != null) {
      builder.append("nELat=");
      builder.append(nELat);
      builder.append(", ");
    }
    if (nELng != null) {
      builder.append("nELng=");
      builder.append(nELng);
      builder.append(", ");
    }
    if (sWLat != null) {
      builder.append("sWLat=");
      builder.append(sWLat);
      builder.append(", ");
    }
    if (sWLng != null) {
      builder.append("sWLng=");
      builder.append(sWLng);
      builder.append(", ");
    }
    if (subPath != null) {
      builder.append("subPath=");
      builder.append(subPath);
      builder.append(", ");
    }
    if (tableName != null) {
      builder.append("tableName=");
      builder.append(tableName);
      builder.append(", ");
    }
    if (code != null) {
      builder.append("code=");
      builder.append(code);
      builder.append(", ");
    }
    if (area != null) {
      builder.append("area=");
      builder.append(area);
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
    if (parentId != null) {
      builder.append("parentId=");
      builder.append(parentId);
      builder.append(", ");
    }
    if (isExact != null) {
      builder.append("isExact=");
      builder.append(isExact);
      builder.append(", ");
    }
    if (tileId != null) {
      builder.append("tileId=");
      builder.append(tileId);
      builder.append(", ");
    }
    if (imgColumn != null) {
      builder.append("imgColumn=");
      builder.append(imgColumn);
      builder.append(", ");
    }
    if (type != null) {
      builder.append("type=");
      builder.append(type);
      builder.append(", ");
    }
    if (isBigTile != null) {
      builder.append("isBigTile=");
      builder.append(isBigTile);
      builder.append(", ");
    }
    if (baseZoom != null) {
      builder.append("baseZoom=");
      builder.append(baseZoom);
      builder.append(", ");
    }
    if (isColorUpdation != null) {
      builder.append("isColorUpdation=");
      builder.append(isColorUpdation);
      builder.append(", ");
    }
    if (accuracyFactor != null) {
      builder.append("accuracyFactor=");
      builder.append(accuracyFactor);
      builder.append(", ");
    }
    if (geographyPkList != null) {
      builder.append("geographyPkList=");
      builder.append(geographyPkList);
      builder.append(", ");
    }
    if (boundaryColumnList != null) {
      builder.append("boundaryColumnList=");
      builder.append(boundaryColumnList);
      builder.append(", ");
    }
    if (rowKeyPrefixList != null) {
      builder.append("rowKeyPrefixList=");
      builder.append(rowKeyPrefixList);
    }
    builder.append("]");
    return builder.toString();
  }
}

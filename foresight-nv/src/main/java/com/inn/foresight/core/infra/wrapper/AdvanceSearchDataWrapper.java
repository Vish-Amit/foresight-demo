package com.inn.foresight.core.infra.wrapper;

import java.io.Serializable;

public class AdvanceSearchDataWrapper implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;


  private String name;
  private String type;
  private String typeCategory;
  private Integer id;
  private String rowKeyPrefix;
  private Integer typereference;

  public AdvanceSearchDataWrapper(String name, String type, String typeCategory, Integer id,
      String rowPrefix,Integer typereference) {
    super();
    this.name = name;
    this.type = type;
    this.typeCategory = typeCategory;
    this.id = id;
    this.rowKeyPrefix = rowPrefix;
    this.typereference = typereference;
  }

  public Integer getTypereference() {
    return typereference;
  }


  public void setTypereference(Integer typereference) {
    this.typereference = typereference;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getTypeCategory() {
    return typeCategory;
  }

  public void setTypeCategory(String typeCategory) {
    this.typeCategory = typeCategory;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getRowPrefix() {
    return rowKeyPrefix;
  }

  public void setRowPrefix(String rowPrefix) {
    this.rowKeyPrefix = rowPrefix;
  }

  @Override
  public String toString() {
    return "AdvanceSearchDataWrapper [name=" + name + ", type=" + type + ", typeCategory="
        + typeCategory + ", id=" + id + ", rowPrefix=" + rowKeyPrefix + ", typereference="
        + typereference + "]";
  }

}

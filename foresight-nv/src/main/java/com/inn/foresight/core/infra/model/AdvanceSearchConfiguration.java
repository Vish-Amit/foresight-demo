package com.inn.foresight.core.infra.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@NamedQueries({
    @NamedQuery(name = "getBeanNameByType",
        query = "select a.bean from AdvanceSearchConfiguration a  where a.type=:type"),
    @NamedQuery(name = "getAdvanceSearchConfigurationByType",
        query = "select a from AdvanceSearchConfiguration a  where a.type=:type"),})

@XmlRootElement(name = "AdvanceSearchConfiguration")
@Entity
@Table(name = "AdvanceSearchConfiguration")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class AdvanceSearchConfiguration implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8685828996490817266L;



  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  @Column(name = "advancesearchconfigurationid_pk")
  private Integer id;



  @Basic
  @Column(name = "searchfieldtype")
  private String type;


  @Basic
  @Column(name = "beanname")
  private String bean;

  @Basic
  @Column(name = "typecategory")
  private String typeCategory;

  public AdvanceSearchConfiguration(String building) {
    setType(building);
  }


  public AdvanceSearchConfiguration() {}


  @JsonIgnore
  public Integer getId() {
    return id;
  }


  public void setId(Integer id) {
    this.id = id;
  }


  public String getType() {
    return type;
  }


  public void setType(String type) {
    this.type = type;
  }


  @JsonIgnore
  public String getBean() {
    return bean;
  }


  public void setBean(String bean) {
    this.bean = bean;
  }


  public String getTypeCategory() {
    return typeCategory;
  }


  public void setTypeCategory(String typeCategory) {
    this.typeCategory = typeCategory;
  }


  @Override
  public String toString() {
    return "AdvanceSearchConfiguration [type=" + type + ", bean=" + bean + ", typeCategory="
        + typeCategory + "]";
  }



}

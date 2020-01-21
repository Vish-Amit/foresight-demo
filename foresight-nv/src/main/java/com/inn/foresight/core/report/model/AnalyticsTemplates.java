package com.inn.foresight.core.report.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@NamedQueries({
		@NamedQuery(name = "getAnalyticsTemplatebyWidgetIdAndType", query = "select a from AnalyticsTemplates a where a.widgetId =:widgetId and a.type =:type") })
@Entity
@Table(name = "AnalyticsTemplate")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class AnalyticsTemplates implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6559300039228327710L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(columnDefinition = "INT", name = "analyticstemplateid_pk")
	private Integer id;

	@Basic
	@Column
	@Size(min = 0, max = 50)
	private String name;

	@Basic
	@Column
	@Size(min = 0, max = 50)
	private String category;

	public static enum TemplateType {

		/** The chart. */
		CHART,

		/** The grid_prd. */
		GRID_PRD,

		/** The grid_web. */
		GRID_WEB,

		/** The custom. */
		CUSTOM;
	}

	public static enum Generation {

		/** The On demand. */
		ON_DEMAND,

		/** The System driven. */
		SYSTEM_DRIVEN
	}

	@Column
	@Enumerated(EnumType.STRING)
	private TemplateType type;

	@Column
	@Enumerated(EnumType.STRING)
	private Generation generation;

	@Column(name = "deleted")
	private Boolean deleted;

	@Column(name = "widgetid")
	private Integer widgetId;

	@Column(name = "widgetname", unique = true)
	private String widgetName;

	@Column(name = "creatorid_fk")
	private Integer creatorid_fk;

	@Column(name = "lastmodifierid_fk")
	private Integer lastmodifierid_fk;

	@Column(name = "modulename")
	private String moduleName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public TemplateType getType() {
		return type;
	}

	public void setType(TemplateType type) {
		this.type = type;
	}

	public Generation getGeneration() {
		return generation;
	}

	public void setGeneration(Generation generation) {
		this.generation = generation;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Integer getWidgetId() {
		return widgetId;
	}

	public void setWidgetId(Integer widgetId) {
		this.widgetId = widgetId;
	}

	public String getWidgetName() {
		return widgetName;
	}

	public void setWidgetName(String widgetName) {
		this.widgetName = widgetName;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public Integer getCreatorid_fk() {
		return creatorid_fk;
	}

	public void setCreatorid_fk(Integer creatorid_fk) {
		this.creatorid_fk = creatorid_fk;
	}

	public Integer getLastmodifierid_fk() {
		return lastmodifierid_fk;
	}

	public void setLastmodifierid_fk(Integer lastmodifierid_fk) {
		this.lastmodifierid_fk = lastmodifierid_fk;
	}

	@Override
	public String toString() {
		return "AnalyticsTemplates [id=" + id + ", name=" + name + ", category=" + category + ", type=" + type
				+ ", generation=" + generation + ", deleted=" + deleted + ", widgetId=" + widgetId + ", widgetName="
				+ widgetName + ", creatorid_fk=" + creatorid_fk + ", lastmodifierid_fk=" + lastmodifierid_fk
				+ ", moduleName=" + moduleName + "]";
	}

	

}

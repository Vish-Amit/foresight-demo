package com.inn.foresight.core.infra.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@NamedQuery(name="getFloorplanMappingByUnitIdAndTemplate",query="select f from  FloorPlanMapping f where f.unit.id=:unitId and f.templateType=:templateType")
@NamedQuery(name="getFloorplanMappingByUnitId",query="select f from  FloorPlanMapping f where f.unit.id=:unitId order by modificationtime desc ")


	@Entity
	@XmlRootElement(name = "FloorPlanMapping")
	@Table(name = "FloorPlanMapping")
	@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
	public class FloorPlanMapping implements Serializable{
		private static final long serialVersionUID = 1L;
		
		@Id
		@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
		@Column(name = "floorplanmappingid_pk")
		private Integer id;
		
	
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "unitid_fk", nullable = true)
		private Unit unit;
		
		@Column(name = "templatetype")
		private String templateType;
		
		@Column(name = "creationtime")
		private Date creationTime;

		@Column(name = "modificationtime")
		private Date modificationTime;
		
		@Column(name = "approved")
		private Boolean isApproved=Boolean.FALSE;

		/**
		 * @return the id
		 */
		public Integer getId() {
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(Integer id) {
			this.id = id;
		}

		/**
		 * @return the unit
		 */
		public Unit getUnit() {
			return unit;
		}

		/**
		 * @param unit the unit to set
		 */
		public void setUnit(Unit unit) {
			this.unit = unit;
		}

		/**
		 * @return the templateType
		 */
		public String getTemplateType() {
			return templateType;
		}

		/**
		 * @param templateType the templateType to set
		 */
		public void setTemplateType(String templateType) {
			this.templateType = templateType;
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
		 * @return the isApproved
		 */
		public Boolean getIsApproved() {
			return isApproved;
		}

		/**
		 * @param isApproved the isApproved to set
		 */
		public void setIsApproved(Boolean isApproved) {
			this.isApproved = isApproved;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "FloorPlanMapping [id=" + id + ", unit=" + unit + ", templateType=" + templateType
					+ ", creationTime=" + creationTime + ", modificationTime=" + modificationTime + ", isApproved="
					+ isApproved + "]";
		}

}

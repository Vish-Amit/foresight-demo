/**
 * 
 */
package com.inn.foresight.module.savedWorkspace.model;

import java.util.Date;

import javax.persistence.Basic;
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.product.um.module.model.Module;
import com.inn.product.um.user.model.User;

/**
 * The Class SavedWorkspace.
 *
 * @author innoeye
 */

@NamedQuery(name="getWorkspaceByName", query="select sw from SavedWorkspace sw where sw.name=(:workspaceName) and sw.user.userid=(:userid)")
@NamedQuery(name="getUserWorkspaces", query="select sw from SavedWorkspace sw where sw.user.userid=(:userid)")
@NamedQuery(name="getUserWorkspaceById", query="select sw from SavedWorkspace sw where sw.id=(:id) and sw.user.userid=(:userid)")
@NamedQuery(name="getSavedPresetListByModuleId", query="select sw from SavedWorkspace sw where sw.module.moduleid=(:moduleId)")

@XmlRootElement(name = "SavedWorkspace")
@Entity
@Table(name = "SavedWorkspace")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class SavedWorkspace {

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "savedworkspaceid_pk")
	private Integer id;

	/** The name. */
	@Basic
	@Column(name = "name")
	private String name;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userid_fk")
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "moduleid_fk")
	private Module module;

	/** The configuration. */
	@Column(name = "configuration", columnDefinition = "json")
	private String configuration;

	/** The created time. */
	@Basic
	@Column(name = "creationtime", columnDefinition="DATETIME DEFAULT CURRENT_TIMESTAMP", insertable=false, updatable=false)
	private Date createdTime;

	/** The is deleted. */
	@Basic
	@Column(name = "deleted")
	private Boolean deleted;

	/** The modified time. */
	@Basic
	@Column(name = "modificationtime", columnDefinition="DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable=false, updatable=false)
	private Date modifiedTime;

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public String getConfiguration() {
		return configuration;
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	@Override
	public String toString() {
		return "SavedWorkspace [id=" + id + ", name=" + name + ", user=" + user + ", module=" + module
				+ ", configuration=" + configuration + ", createdTime=" + createdTime + ", deleted=" + deleted
				+ ", modifiedTime=" + modifiedTime + "]";
	}

		
}

package com.inn.foresight.core.fileupload.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
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
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.product.um.module.model.Module;
import com.inn.product.um.user.model.User;

@NamedQueries({ 
	@NamedQuery(name = "getFileUploadList", query = "select  new com.inn.foresight.core.fileupload.utils.wrapper.FileUploadWrapper( fu.id, fu.name,m.name,u.userName,fu.uploadedOn,fu.type,fu.size,fu.status) from FileUpload fu "
			+ " join fu.module m join fu.uploadedBy u where fu.deleted=0 order by fu.uploadedOn desc"),

	@NamedQuery(name = "getFileUploadListForOtherUser", query = "select  new com.inn.foresight.core.fileupload.utils.wrapper.FileUploadWrapper( fu.id, fu.name,m.name,u.userName,fu.uploadedOn,fu.type,fu.size,fu.status) from FileUpload fu "
			+ " join fu.module m join fu.uploadedBy u where fu.deleted=0 and m.moduleid in (:moduleId) order by fu.uploadedOn desc"),


	@NamedQuery(name = "getFileUploadCount", query = "select count(fu.id) from FileUpload fu join fu.module m where fu.deleted=0  "),

	@NamedQuery(name = "getFileUploadCountForOtherUser", query = "select count(fu.id) from FileUpload fu join fu.module m where fu.deleted=0 and m.moduleid in (:moduleId)  "),
	
	@NamedQuery(name = "deleteFileById", query = "update FileUpload fu set fu.deleted=1 where fu.id in (:id)  "),
	@NamedQuery(name = "getFilePathByFileName", query = "select  fu.path from FileUpload fu where fu.name=:name "),


})


@FilterDefs({
	@FilterDef(name = "fileSearchFilter", parameters = {
			@ParamDef(name = "search", type = "java.lang.String") })
})

@Filters({ 
	@Filter(name = "fileSearchFilter", condition = "search like :search"),

})
@Entity
@Table(name = "FileUpload")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class FileUpload implements Serializable  {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "fileuploadid_pk")
	private Integer id;

	@Basic
	@Column(name = "filename")
	private String name;

	@Basic
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "moduleid_fk", nullable = false)
	private Module module;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uploadedbyid_fk")
	private User uploadedBy;

	@Basic
	@Column(name = "uploadedon")
	private Date uploadedOn;

	@Basic
	@Column(name = "filetype")
	private String type;

	@Basic
	@Column(name = "filesize")
	private Long size;


	@Enumerated(EnumType.STRING)
	private Status status;

	@Basic
	@Column(name = "deleted")
	private Boolean deleted;



	@Basic
	@Column(name = "search")
	private String search;

	

	@Basic
	@Column(name = "filepath")
	private String path;


	public enum Status {


		UN_PROCESSED, 


		PROCESSED, 


		IN_PROGRESS;



		

	}


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




	public Module getModule() {
		return module;
	}




	public void setModule(Module module) {
		this.module = module;
	}




	public User getUploadedBy() {
		return uploadedBy;
	}




	public void setUploadedBy(User uploadedBy) {
		this.uploadedBy = uploadedBy;
	}




	public Date getUploadedOn() {
		return uploadedOn;
	}




	public void setUploadedOn(Date uploadedOn) {
		this.uploadedOn = uploadedOn;
	}




	public String getType() {
		return type;
	}




	public void setType(String type) {
		this.type = type;
	}




	public Long getSize() {
		return size;
	}




	public void setSize(Long size) {
		this.size = size;
	}




	




	public Status getStatus() {
		return status;
	}




	public void setStatus(Status status) {
		this.status = status;
	}




	public Boolean getDeleted() {
		return deleted;
	}




	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}




	public String getSearch() {
		return search;
	}




	public void setSearch(String search) {
		this.search = search;
	}




	public String getPath() {
		return path;
	}




	public void setPath(String path) {
		this.path = path;
	}




	@PostPersist
	@PrePersist
	@PreUpdate
	public void updateFileSearch()
	{
		StringBuilder builder = new StringBuilder();
		if(name!=null)
			builder.append(name);
		if(type!=null)
			builder.append(type);
		if(module!=null && module.getName()!=null)
			builder.append(module.getName());
		if(uploadedBy!=null && uploadedBy.getUserName()!=null)
			builder.append(uploadedBy.getUserName());


		search = builder.toString();
		search = StringUtils.replace(search, " ", "").toLowerCase();

	}

}

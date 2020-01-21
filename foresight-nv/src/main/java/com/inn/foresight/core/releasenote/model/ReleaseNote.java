package com.inn.foresight.core.releasenote.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@NamedQueries({
        @NamedQuery(name = "getAllReleaseNote", query = "select new com.inn.foresight.core.releasenote.utils.ReleaseNoteWrapper(r.id,r.name,r.downloadPath,r.version,r.releasedate, r.createdby, r.modifiedby,r.comment) from ReleaseNote r where r.deleted=false order by r.createdtime desc"),
        @NamedQuery(name = "searchReleaseNoteByNameOrVersion", query = "select new com.inn.foresight.core.releasenote.utils.ReleaseNoteWrapper(r.id,r.name,r.downloadPath,r.version,r.releasedate, r.createdby, r.modifiedby, r.comment) from ReleaseNote r where (Lower(r.name) like CONCAT('%',:searchText,'%') or r.version like CONCAT('%',:searchText,'%')) and r.deleted=false"),
        @NamedQuery(name = "deleteReleaseNote", query = "update ReleaseNote r set r.deleted = true where r.id in (:idList)"),
        @NamedQuery(name = "getReleaseNoteByVersion", query = "select r.id from ReleaseNote r where r.version = :version and r.deleted=false")
})

@XmlRootElement(name = "ReleaseNote")
@Entity
@Table(name = "ReleaseNote")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler"})
public class ReleaseNote implements Serializable {

    /** The id. */
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "releaseid_pk")
    private int id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "version")
    private String version;

    @Basic
    @Column(name = "downloadpath")
    private String downloadPath;

    @Basic
    @Column(name = "releasedate")
    private Date releasedate;

    @Basic
    @Column(name = "comment")
    private String comment;

    @Basic
    @Column(name = "deleted")
    private Boolean deleted;

    @Basic
    @Column(name = "lastmodifierid_fk")
    private Integer modifiedby;

    @Basic
    @Column(name = "creatorid_fk")
    private Integer createdby;

    @Basic
    @Column(name = "modificationtime")
    private Date modifiedtime;

    @Basic
    @Column(name = "creationtime")
    private Date createdtime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public Date getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(Date releasedate) {
        this.releasedate = releasedate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getModifiedby() {
        return modifiedby;
    }

    public void setModifiedby(Integer modifiedby) {
        this.modifiedby = modifiedby;
    }

    public Integer getCreatedby() {
        return createdby;
    }

    public void setCreatedby(Integer createdby) {
        this.createdby = createdby;
    }

    public Date getModifiedtime() {
        return modifiedtime;
    }

    public void setModifiedtime(Date modifiedtime) {
        this.modifiedtime = modifiedtime;
    }

    public Date getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(Date createdtime) {
        this.createdtime = createdtime;
    }

	@Override
	public String toString() {
		return "ReleaseNote [id=" + id + ", name=" + name + ", version=" + version + ", downloadPath=" + downloadPath
				+ ", releasedate=" + releasedate + ", comment=" + comment + ", deleted=" + deleted + ", modifiedby="
				+ modifiedby + ", createdby=" + createdby + ", modifiedtime=" + modifiedtime + ", createdtime="
				+ createdtime + "]";
	}
    
    
    
}

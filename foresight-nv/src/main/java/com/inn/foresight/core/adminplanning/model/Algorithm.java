package com.inn.foresight.core.adminplanning.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Views.TabView;
import com.inn.product.um.user.model.User;

/**
 * The Class AlgorithmProperty.
 */
@NamedQueries({ @NamedQuery(name = "getAllAlgorithms", query = "select distinct a from Algorithm a LEFT JOIN FETCH a.childAlgorithms child where (child is null or child.deleted is false) ORDER BY a.modifiedTime DESC, child.modifiedTime DESC"),
//	 @NamedQuery(name = "searchAlgorithmByDisplayName", query = "select distinct a from Algorithm a LEFT JOIN FETCH a.childAlgorithms child where (child is null) or (child.deleted is false and (child.displayName like concat('%',upper(:displayName),'%') or a.displayName like concat('%',upper(:displayName),'%')))") 
	@NamedQuery(name = "searchAlgorithmByDisplayName", query = "select distinct a from Algorithm a LEFT JOIN FETCH a.childAlgorithms child where ((child is null) or (child.deleted is false)) and (child.displayName like concat('%',upper(:displayName),'%') or a.displayName like concat('%',lower(:displayName),'%'))")
	
})

@FilterDef(name = "topParentFilter")
@Filter(name = "topParentFilter", condition = "algorithmid_fk is null")

@FilterDef(name = "deletedFilter", parameters = { @ParamDef(name = "deleted", type = "java.lang.Boolean") })
@Filter(name = "deletedFilter", condition = "deleted = :deleted")

@FilterDef(name = "childDeleteFilter", defaultCondition = "deleted = false")

@XmlRootElement(name = "Algorithm")
@Entity
//@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Table(name = "Algorithm")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler"})
@JsonSerialize()
public class Algorithm implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1575772688740187585L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@JsonView(value = { TabView.class })
	@Column(name = "algorithmid_pk")
	private Integer id;

	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "algorithmid_fk")
//	@JsonBackReference
	private Algorithm parentAlgorithm;
	
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "algorithmid_fk")
//	@JsonManagedReference
	@Filter(name="childDeleteFilter")
	private Set<Algorithm> childAlgorithms = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JsonManagedReference(value = "algorithmProperty")
	@JoinColumn(name = "algorithmid_fk")
	@JsonProperty
	private Set<AlgorithmProperty> algorithmProperty = new HashSet<>();

	/** The is deleted. */
	@Basic
	private Boolean deleted;

	@Basic
	private Boolean exceptionEnabled;

	@Basic
	private Boolean mailEnabled;

	/** The algorithm Name. */
	@Basic
	private String name;

	/** The parameter name. */
	@Basic
	private String displayName;

	/** The parameter value. */
	@Basic
	private String recipients;

	/** The creation time. */
	@Basic
	@Column(name = "creationtime")
	private Date creationTime;

	/** The modified time. */
	@Basic
	@Column(name = "modificationtime")
	private Date modifiedTime;

	/** The modifier. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lastmodifierid_fk")
	private User modifier;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@JsonIgnore
	@JsonProperty
	public Algorithm getParentAlgorithm() {
		return parentAlgorithm;
	}

	
	public void setParentAlgorithm(Algorithm parentAlgorithm) {
		this.parentAlgorithm = parentAlgorithm;
	}

	
	public Set<Algorithm> getChildAlgorithms() {
		return childAlgorithms;
	}
	
	public void setChildAlgorithms(Set<Algorithm> childAlgorithms) {
		if (childAlgorithms != null) {
			this.childAlgorithms.addAll(childAlgorithms);
			for (Algorithm child : childAlgorithms) {
				child.setParentAlgorithm(this);
			}
		}
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Boolean getExceptionEnabled() {
		return exceptionEnabled;
	}

	public void setExceptionEnabled(Boolean exceptionEnabled) {
		this.exceptionEnabled = exceptionEnabled;
	}

	public Boolean getMailEnabled() {
		return mailEnabled;
	}

	public void setMailEnabled(Boolean mailEnabled) {
		this.mailEnabled = mailEnabled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getRecipients() {
		return recipients;
	}

	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public String getUserName() {
		if (this.modifier != null) {
			return this.modifier.getFirstName() + ForesightConstants.SPACE + this.modifier.getLastName();
		}
		return null;
	}
	
	@JsonIgnore
	public User getModifier() {
		return modifier;
	}

	public void setModifier(User modifier) {
		this.modifier = modifier;
	}
	
	@JsonIgnore
	public Set<AlgorithmProperty> getAlgorithmProperty() {
		return algorithmProperty;
	}

	@JsonProperty
	public void setAlgorithmProperty(Set<AlgorithmProperty> algorithmProperty) {
		if (algorithmProperty != null) {
			this.algorithmProperty.addAll(algorithmProperty);
			for (AlgorithmProperty property : algorithmProperty) {
				property.setAlgorithm(this);
			}
		}
	}

	@Override
	public String toString() {
		return "Algorithm [id=" + id + ", parentAlgorithm=" + parentAlgorithm + ", childAlgorithms=" + childAlgorithms
				+ ", algorithmProperty=" + algorithmProperty + ", deleted=" + deleted + ", exceptionEnabled="
				+ exceptionEnabled + ", mailEnabled=" + mailEnabled + ", name=" + name + ", displayName=" + displayName
				+ ", recipients=" + recipients + ", creationTime=" + creationTime + ", modifiedTime=" + modifiedTime
				+ ", modifier=" + modifier + "]";
	}
	
	/*@PostLoad
	public final void postLoad() {
		if (this.modifier != null) {
			User user = new User();
			user.setUserid(this.modifier.getUserid());
			user.setFirstName(this.modifier.getFirstName());
			user.setLastName(this.modifier.getLastName());
			user.setEmail(this.modifier.getEmail());
			this.modifier = user;
		}
	}*/
}

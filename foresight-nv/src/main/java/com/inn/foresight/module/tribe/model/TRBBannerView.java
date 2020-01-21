package com.inn.foresight.module.tribe.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.product.um.user.model.User;

/**
 * The Class TRBBannerView.
 */
@NamedQueries({
		@NamedQuery(name = "getBannerViewByBannerIdAndUserId", query = "select bv from TRBBannerView "
				+ "bv where bv.banner.id=:bannerId and bv.viewer.userid=:userId"),
		@NamedQuery(name = "deleteBannerViewsByBannerId", query = "delete from TRBBannerView "
				+ "bv where bv.banner.id=:bannerId"),
		@NamedQuery(name = "getBannerCountByBannerId", query = "select count(bv) from TRBBannerView bv"
				+ " where bv.banner.id=:bannerId"),
		@NamedQuery(name = "activeBannerCount", query = "select count(br)  from TRBBanner br where br.status='ACTIVE'"),
		@NamedQuery(name = "inactiveBannerCount", query = "select count(br)  from TRBBanner br where br.status='INACTIVE'"),
		@NamedQuery(name = "draftBannerCount", query = "select count(br)  from TRBBanner br where br.status='DRAFT'")
		
})


@Entity
@Table(name = "BannerView")
@XmlRootElement(name = "TRBBannerView")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class TRBBannerView implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3982831978237277018L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	/*@GeneratedValue(generator = "TRBBannerView_gen", strategy = javax.persistence.GenerationType.SEQUENCE)
	@SequenceGenerator(sequenceName = "TRBBannerView_seq", name = "TRBBannerView_gen", allocationSize = 1)*/
	@Column(name = "bannerviewid_pk")
	private Integer id;

	/** The banner. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bannerid_fk")
	private TRBBanner banner;

	/** The viewer. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userid_fk")
	private User viewer;

	/** The creator. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creatorid_fk")
	private User creator;

	/** The last modifier. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lastModifierid_fk")
	private User lastModifier;

	/** The modified time. */
	@Basic
	@Column(name = "modificationtime")
	private Date modifiedTime;

	/** The created time. */
	@Basic
	@Column(name = "creationtime")
	private Date createdTime;

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
	 * Gets the banner.
	 *
	 * @return the banner
	 */
	public TRBBanner getBanner() {
		return banner;
	}

	/**
	 * Sets the banner.
	 *
	 * @param banner the new banner
	 */
	public void setBanner(TRBBanner banner) {
		this.banner = banner;
	}

	/**
	 * Gets the viewer.
	 *
	 * @return the viewer
	 */
	public User getViewer() {
		return viewer;
	}

	/**
	 * Sets the viewer.
	 *
	 * @param viewer the new viewer
	 */
	public void setViewer(User viewer) {
		this.viewer = viewer;
	}

	/**
	 * Gets the creator.
	 *
	 * @return the creator
	 */
	public User getCreator() {
		return creator;
	}

	/**
	 * Sets the creator.
	 *
	 * @param creator the new creator
	 */
	public void setCreator(User creator) {
		this.creator = creator;
	}

	/**
	 * Gets the last modifier.
	 *
	 * @return the last modifier
	 */
	public User getLastModifier() {
		return lastModifier;
	}

	/**
	 * Sets the last modifier.
	 *
	 * @param lastModifier the new last modifier
	 */
	public void setLastModifier(User lastModifier) {
		this.lastModifier = lastModifier;
	}

	/**
	 * Gets the modified time.
	 *
	 * @return the modified time
	 */
	public Date getModifiedTime() {
		return modifiedTime;
	}

	/**
	 * Sets the modified time.
	 *
	 * @param modifiedTime the new modified time
	 */
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	/**
	 * Gets the created time.
	 *
	 * @return the created time
	 */
	public Date getCreatedTime() {
		return createdTime;
	}

	/**
	 * Sets the created time.
	 *
	 * @param createdTime the new created time
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	public boolean equals(Object obj) {
		boolean returnValue = false;
		if (obj instanceof TRBBannerView) {
			TRBBannerView trbBannerView = (TRBBannerView) obj;
			EqualsBuilder equalsBuilder = new EqualsBuilder();
			equalsBuilder.appendSuper(super.equals(obj));
			equalsBuilder.append(id, trbBannerView.getId());
			equalsBuilder.append(banner, trbBannerView.getBanner());
			equalsBuilder.append(viewer, trbBannerView.getViewer());
			equalsBuilder.append(creator, trbBannerView.getCreator());
			equalsBuilder.append(lastModifier, trbBannerView.getLastModifier());
			equalsBuilder.append(modifiedTime, trbBannerView.getModifiedTime());
			equalsBuilder.append(createdTime, trbBannerView.getCreatedTime());
			returnValue = equalsBuilder.isEquals();
		}
		return returnValue;
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(17, 37);
		hashCodeBuilder.append(id);
		hashCodeBuilder.append(banner);
		hashCodeBuilder.append(viewer);
		hashCodeBuilder.append(creator);
		hashCodeBuilder.append(lastModifier);
		hashCodeBuilder.append(modifiedTime);
		hashCodeBuilder.append(createdTime);
		return hashCodeBuilder.toHashCode();
	}

}

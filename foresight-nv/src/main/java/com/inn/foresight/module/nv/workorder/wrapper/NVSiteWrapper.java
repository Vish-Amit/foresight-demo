package com.inn.foresight.module.nv.workorder.wrapper;

/** The Class NVSiteWrapper. */
public class NVSiteWrapper {

	/** The site name. */
	private String siteName;
	
	/** The sector id. */
	private Integer sectorId;
	
	/** The azimuth. */
	private Integer azimuth;
	
	/** The pci. */
	private Integer pci;
	
	/** The site latitude. */
	private Double siteLatitude;
	
	/** The site longitude. */
	private Double siteLongitude;
	
	/** The site band. */
	private Integer siteBand;
	
	/** The site cell id. */
	private Integer siteCellId;

	private String netype;

	public String getNetype() {
		return netype;
	}

	public void setNetype(String netype) {
		this.netype = netype;
	}

	/**
	 * Gets the site name.
	 *
	 * @return the site name
	 */
	public String getSiteName() {
		return siteName;
	}
	
	/**
	 * Sets the site name.
	 *
	 * @param siteName the new site name
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	
	/**
	 * Gets the sector id.
	 *
	 * @return the sector id
	 */
	public Integer getSectorId() {
		return sectorId;
	}
	
	/**
	 * Sets the sector id.
	 *
	 * @param sectorId the new sector id
	 */
	public void setSectorId(Integer sectorId) {
		this.sectorId = sectorId;
	}
	
	/**
	 * Gets the azimuth.
	 *
	 * @return the azimuth
	 */
	public Integer getAzimuth() {
		return azimuth;
	}
	
	/**
	 * Sets the azimuth.
	 *
	 * @param azimuth the new azimuth
	 */
	public void setAzimuth(Integer azimuth) {
		this.azimuth = azimuth;
	}
	
	/**
	 * Gets the pci.
	 *
	 * @return the pci
	 */
	public Integer getPci() {
		return pci;
	}
	
	/**
	 * Sets the pci.
	 *
	 * @param pci the new pci
	 */
	public void setPci(Integer pci) {
		this.pci = pci;
	}
	
	/**
	 * Gets the site latitude.
	 *
	 * @return the site latitude
	 */
	public Double getSiteLatitude() {
		return siteLatitude;
	}
	
	/**
	 * Sets the site latitude.
	 *
	 * @param siteLatitude the new site latitude
	 */
	public void setSiteLatitude(Double siteLatitude) {
		this.siteLatitude = siteLatitude;
	}
	
	/**
	 * Gets the site longitude.
	 *
	 * @return the site longitude
	 */
	public Double getSiteLongitude() {
		return siteLongitude;
	}
	
	/**
	 * Sets the site longitude.
	 *
	 * @param siteLongitude the new site longitude
	 */
	public void setSiteLongitude(Double siteLongitude) {
		this.siteLongitude = siteLongitude;
	}
	
	/**
	 * Gets the site band.
	 *
	 * @return the site band
	 */
	public Integer getSiteBand() {
		return siteBand;
	}
	
	/**
	 * Sets the site band.
	 *
	 * @param siteBand the new site band
	 */
	public void setSiteBand(Integer siteBand) {
		this.siteBand = siteBand;
	}
	
	/**
	 * Gets the site cell id.
	 *
	 * @return the site cell id
	 */
	public Integer getSiteCellId() {
		return siteCellId;
	}
	
	/**
	 * Sets the site cell id.
	 *
	 * @param siteCellId the new site cell id
	 */
	public void setSiteCellId(Integer siteCellId) {
		this.siteCellId = siteCellId;
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "NVSiteWrapper [siteName=" + siteName + ", sectorId=" + sectorId + ", azimuth=" + azimuth + ", pci="
				+ pci + ", siteLatitude=" + siteLatitude + ", siteLongitude=" + siteLongitude + ", siteBand=" + siteBand
				+ ", siteCellId=" + siteCellId + "]";
	}

	
}

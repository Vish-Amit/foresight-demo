/**
 * 
 */
package com.inn.foresight.core.infra.rowprefix;

import com.inn.core.generic.wrapper.JpaWrapper;

/**
 * @author root
 *
 */

@JpaWrapper
public class HbaseRowPrefixGeoWrapper {
 
    private String domain=null;
    private String vendor=null;
    private Integer numericPrefix=null;    
    
    private Integer l1PK=null;
    private Integer l2PK=null;
    private Integer l3PK=null;
    private String l1Name=null;
    private String l2Name=null;
    private String l3Name=null;
    
    
	public HbaseRowPrefixGeoWrapper(String domain, String vendor, Integer numericPrefix) {
		super();
		this.domain = domain;
		this.vendor = vendor;
		this.numericPrefix = numericPrefix;
	}

	public HbaseRowPrefixGeoWrapper(Integer l1pk, Integer l2pk, Integer l3pk, String l1Name, String l2Name,
			String l3Name) {
		super();
		l1PK = l1pk;
		l2PK = l2pk;
		l3PK = l3pk;
		this.l1Name = l1Name;
		this.l2Name = l2Name;
		this.l3Name = l3Name;
	}

	public HbaseRowPrefixGeoWrapper(String domain, String vendor, Integer l1pk, Integer l2pk, Integer l3pk,
			String l1Name, String l2Name, String l3Name) {
		super();
		this.domain = domain;
		this.vendor = vendor;
		this.l1PK = l1pk;
		this.l2PK = l2pk;
		this.l3PK = l3pk;
		this.l1Name = l1Name;
		this.l2Name = l2Name;
		this.l3Name = l3Name;
	}

		
	public HbaseRowPrefixGeoWrapper() {
		super();
	}

	public HbaseRowPrefixGeoWrapper(HbaseRowPrefixGeoWrapper obj) {
		super();
		this.domain = obj.getDomain();
		this.vendor = obj.getVendor();
		this.l1PK = obj.getL1PK();
		this.l2PK = obj.getL2PK();
		this.l3PK = obj.getL3PK();
		this.l1Name = obj.getL1Name();
		this.l2Name = obj.getL2Name();
		this.l3Name = obj.getL3Name();
		this.numericPrefix=obj.getNumericPrefix();
	}
	
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public Integer getNumericPrefix() {
		return numericPrefix;
	}
	public void setNumericPrefix(Integer numericPrefix) {
		this.numericPrefix = numericPrefix;
	}

	public Integer getL1PK() {
		return l1PK;
	}

	public void setL1PK(Integer l1pk) {
		l1PK = l1pk;
	}

	public Integer getL2PK() {
		return l2PK;
	}

	public void setL2PK(Integer l2pk) {
		l2PK = l2pk;
	}

	public Integer getL3PK() {
		return l3PK;
	}

	public void setL3PK(Integer l3pk) {
		l3PK = l3pk;
	}

	public String getL1Name() {
		return l1Name;
	}

	public void setL1Name(String l1Name) {
		this.l1Name = l1Name;
	}

	public String getL2Name() {
		return l2Name;
	}

	public void setL2Name(String l2Name) {
		this.l2Name = l2Name;
	}

	public String getL3Name() {
		return l3Name;
	}

	public void setL3Name(String l3Name) {
		this.l3Name = l3Name;
	}

	@Override
	public String toString() {
		return "HbaseRowPrefixGeoWrapper [domain=" + domain + ", vendor=" + vendor + ", numericPrefix=" + numericPrefix
				+ ", l1PK=" + l1PK + ", l2PK=" + l2PK + ", l3PK=" + l3PK + ", l1Name=" + l1Name + ", l2Name=" + l2Name
				+ ", l3Name=" + l3Name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((l1Name == null) ? 0 : l1Name.hashCode());
		result = prime * result + ((l1PK == null) ? 0 : l1PK.hashCode());
		result = prime * result + ((l2Name == null) ? 0 : l2Name.hashCode());
		result = prime * result + ((l2PK == null) ? 0 : l2PK.hashCode());
		result = prime * result + ((l3Name == null) ? 0 : l3Name.hashCode());
		result = prime * result + ((l3PK == null) ? 0 : l3PK.hashCode());
		result = prime * result + ((numericPrefix == null) ? 0 : numericPrefix.hashCode());
		result = prime * result + ((vendor == null) ? 0 : vendor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HbaseRowPrefixGeoWrapper other = (HbaseRowPrefixGeoWrapper) obj;
		if (domain == null) {
			if (other.domain != null)
				return false;
		} else if (!domain.equals(other.domain))
			return false;
		if (l1Name == null) {
			if (other.l1Name != null)
				return false;
		} else if (!l1Name.equals(other.l1Name))
			return false;
		if (l1PK == null) {
			if (other.l1PK != null)
				return false;
		} else if (!l1PK.equals(other.l1PK))
			return false;
		if (l2Name == null) {
			if (other.l2Name != null)
				return false;
		} else if (!l2Name.equals(other.l2Name))
			return false;
		if (l2PK == null) {
			if (other.l2PK != null)
				return false;
		} else if (!l2PK.equals(other.l2PK))
			return false;
		if (l3Name == null) {
			if (other.l3Name != null)
				return false;
		} else if (!l3Name.equals(other.l3Name))
			return false;
		if (l3PK == null) {
			if (other.l3PK != null)
				return false;
		} else if (!l3PK.equals(other.l3PK))
			return false;
		if (numericPrefix == null) {
			if (other.numericPrefix != null)
				return false;
		} else if (!numericPrefix.equals(other.numericPrefix))
			return false;
		if (vendor == null) {
			if (other.vendor != null)
				return false;
		} else if (!vendor.equals(other.vendor))
			return false;
		return true;
	}
	
	
}

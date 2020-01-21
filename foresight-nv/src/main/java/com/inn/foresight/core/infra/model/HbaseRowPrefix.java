package com.inn.foresight.core.infra.model;

import java.io.Serializable;

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

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.product.um.geography.model.OtherGeography;

@NamedQueries({
    @NamedQuery(name = "getAllHbaseRowPrefixData", query = "select hrp from HbaseRowPrefix hrp order by hrp.numericCode "),
    @NamedQuery(name = "getAlphaNumericCode", query = "select hrp.alphaNumericCode from HbaseRowPrefix hrp where hrp.l3 is not null and hrp.l3 <> '' "),
    @NamedQuery(name = "getAlphaNumericCodeByDomain", query = "select hrp.alphaNumericCode from HbaseRowPrefix hrp where hrp.domain = :domain and hrp.vendor is null"),
    
})

@FilterDefs({
    @FilterDef(name = "hbaseRowPrefixDomainFilter", parameters = {@ParamDef(name = "domain", type = "java.lang.String")}),
    @FilterDef(name = "hbaseRowPrefixVendorFilter", parameters = {@ParamDef(name = "vendor", type = "java.lang.String")}),
    @FilterDef(name = "hbaseRowPrefixL1Filter", parameters = {@ParamDef(name = "l1", type = "java.lang.String")}),
    @FilterDef(name = "hbaseRowPrefixL2Filter", parameters = {@ParamDef(name = "l2", type = "java.lang.String")}),
    @FilterDef(name = "hbaseRowPrefixL3Filter", parameters = {@ParamDef(name = "l3", type = "java.lang.String")}),
    @FilterDef(name = "hbaseRowPrefixL1ListFilter", parameters = {@ParamDef(name = "l1List", type = "java.lang.String")}),
    @FilterDef(name = "hbaseRowPrefixL2ListFilter", parameters = {@ParamDef(name = "l2List", type = "java.lang.String")}),
    @FilterDef(name = "hbaseRowPrefixL3ListFilter", parameters = {@ParamDef(name = "l3List", type = "java.lang.String")}),
})

@Filters({
    @Filter(name = "hbaseRowPrefixDomainFilter", condition = "upper(domain)=:domain"),
    @Filter(name = "hbaseRowPrefixVendorFilter", condition = "upper(vendor)=:vendor"),
    @Filter(name = "hbaseRowPrefixL1Filter", condition = "upper(l1)=:l1"),
    @Filter(name = "hbaseRowPrefixL2Filter", condition = "upper(l2)=:l2"),
    @Filter(name = "hbaseRowPrefixL3Filter", condition = "upper(l3)=:l3"),
    @Filter(name = "hbaseRowPrefixL1ListFilter", condition = "l1 in (:l1List)"),
    @Filter(name = "hbaseRowPrefixL2ListFilter", condition = "l2 in (:l2List)"),
    @Filter(name = "hbaseRowPrefixL3ListFilter", condition = "l3 in (:l3List)")
})

/**
 * @deprecated replaced by {@link HBaseRowPrefix}
 */
@Entity
@Table(name = "HbaseRowPrefix")
@XmlRootElement(name = "HbaseRowPrefix")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class HbaseRowPrefix implements Serializable {

    private static final long serialVersionUID = 6211235012014187538L;
    
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "hbaserowprefixid_pk")
    private Integer id;

    @Column(name = "domain")
    private String domain;

    @Column(name = "vendor")
    private String vendor;

    @Column(name = "l1")
    private String l1;

    @Column(name = "l2")
    private String l2;

    @Column(name = "l3")
    private String l3;

    @Column(name = "numericcode")
    private Integer numericCode;

    @Id
    @Column(name = "alphanumericcode")
    private String alphaNumericCode;
    
    /** The other Geography. */
	@JoinColumn(name = "othergeographyid_fk", nullable = true)
	@ManyToOne(fetch = FetchType.EAGER)
	private OtherGeography otherGeography;

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

    public String getL1() {
        return l1;
    }

    public void setL1(String l1) {
        this.l1 = l1;
    }

    public String getL2() {
        return l2;
    }

    public void setL2(String l2) {
        this.l2 = l2;
    }

    public String getL3() {
        return l3;
    }

    public void setL3(String l3) {
        this.l3 = l3;
    }

    public Integer getNumericCode() {
        return numericCode;
    }

    public void setNumericCode(Integer numericCode) {
        this.numericCode = numericCode;
    }

    public String getAlphaNumericCode() {
        return alphaNumericCode;
    }

    public void setAlphaNumericCode(String alphaNumericCode) {
        this.alphaNumericCode = alphaNumericCode;
    }

    public OtherGeography getOtherGeography() {
		return otherGeography;
	}

	public void setOtherGeography(OtherGeography otherGeography) {
		this.otherGeography = otherGeography;
	}

	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("HbaseRowPrefix [");
        if (domain != null) {
            builder.append("domain=");
            builder.append(domain);
            builder.append(", ");
        }
        if (vendor != null) {
            builder.append("vendor=");
            builder.append(vendor);
            builder.append(", ");
        }
        if (l1 != null) {
            builder.append("l1=");
            builder.append(l1);
            builder.append(", ");
        }
        if (l2 != null) {
            builder.append("l2=");
            builder.append(l2);
            builder.append(", ");
        }
        if (l3 != null) {
            builder.append("l3=");
            builder.append(l3);
            builder.append(", ");
        }
        if (numericCode != null) {
            builder.append("numericCode=");
            builder.append(numericCode);
            builder.append(", ");
        }
        if (alphaNumericCode != null) {
            builder.append("alphaNumericCode=");
            builder.append(alphaNumericCode);
        }
        builder.append("]");
        return builder.toString();
    }

}

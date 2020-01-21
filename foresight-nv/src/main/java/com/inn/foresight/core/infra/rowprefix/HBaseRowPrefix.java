package com.inn.foresight.core.infra.rowprefix;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.inn.product.um.geography.model.GeographyL1;
import com.inn.product.um.geography.model.GeographyL2;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.OtherGeography;

@NamedQuery(name = "HBaseRowPrefix.getPrefixByDomain", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(domain) = UPPER(:domain)")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByVendor", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(vendor) = UPPER(:vendor) GROUP BY domain")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByDomainAndVendor", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(domain) = UPPER(:domain) AND UPPER(vendor) = UPPER(:vendor)")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByGeographyL1", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(geographyL1.name) = UPPER(:geographyL1) GROUP BY domain, vendor")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByDomainAndGeographyL1", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(domain) = UPPER(:domain) AND UPPER(geographyL1.name) = UPPER(:geographyL1) GROUP BY vendor")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByVendorAndGeographyL1", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(vendor) = UPPER(:vendor) AND UPPER(geographyL1.name) = UPPER(:geographyL1) GROUP BY domain")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByDomainAndVendorAndGeographyL1", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(domain) = UPPER(:domain) AND UPPER(vendor) = UPPER(:vendor) AND UPPER(geographyL1.name) = UPPER(:geographyL1)")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByGeographyL1ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE geographyL1.id = :geographyL1 GROUP BY domain, vendor")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByMultiGeographyL1ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE geographyL1.id IN (:geographyL1) GROUP BY domain, vendor, geographyL1.id")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByDomainAndGeographyL1ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(domain) = UPPER(:domain) AND geographyL1.id = :geographyL1 GROUP BY vendor")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByDomainAndMultiGeographyL1ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(domain) = UPPER(:domain) AND geographyL1.id IN (:geographyL1) GROUP BY vendor,geographyL1.id")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByVendorAndGeographyL1ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(vendor) = UPPER(:vendor) AND geographyL1.id = :geographyL1 GROUP BY domain")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByVendorAndMultiGeographyL1ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(vendor) = UPPER(:vendor) AND geographyL1.id IN (:geographyL1) GROUP BY domain,geographyL1.id")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByDomainAndVendorAndGeographyL1ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(domain) = UPPER(:domain) AND UPPER(vendor) = UPPER(:vendor) AND geographyL1.id = :geographyL1")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByDomainAndVendorAndMultiGeographyL1ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(domain) = UPPER(:domain) AND UPPER(vendor) = UPPER(:vendor) AND geographyL1.id IN (:geographyL1) group by geographyL1.id")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByGeographyL2", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(geographyL2.name) = UPPER(:geographyL2) GROUP BY domain, vendor")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByDomainAndGeographyL2", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(domain) = UPPER(:domain) AND UPPER(geographyL2.name) = UPPER(:geographyL2) GROUP BY vendor")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByVendorAndGeographyL2", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(vendor) = UPPER(:vendor) AND UPPER(geographyL2.name) = UPPER(:geographyL2) GROUP BY domain")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByDomainAndVendorAndGeographyL2", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(domain) = UPPER(:domain) AND UPPER(vendor) = UPPER(:vendor) AND UPPER(geographyL2.name) = UPPER(:geographyL2)")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByGeographyL2ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE geographyL2.id = :geographyL2 GROUP BY domain, vendor")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByMultiGeographyL2ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE geographyL2.id IN (:geographyL2) GROUP BY domain, vendor, geographyL2.id")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByDomainAndGeographyL2ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(domain) = UPPER(:domain) AND geographyL2.id = :geographyL2 GROUP BY vendor")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByDomainAndMultiGeographyL2ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(domain) = UPPER(:domain) AND geographyL2.id IN (:geographyL2) GROUP BY vendor, geographyL2.id")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByVendorAndGeographyL2ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(vendor) = UPPER(:vendor) AND geographyL2.id = :geographyL2 GROUP BY domain")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByVendorAndMultiGeographyL2ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(vendor) = UPPER(:vendor) AND geographyL2.id IN (:geographyL2) GROUP BY domain,geographyL2.id")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByDomainAndVendorAndGeographyL2ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(domain) = UPPER(:domain) AND UPPER(vendor) = UPPER(:vendor) AND geographyL2.id = :geographyL2")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByDomainAndVendorAndMultiGeographyL2ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(domain) = UPPER(:domain) AND UPPER(vendor) = UPPER(:vendor) AND geographyL2.id IN (:geographyL2) GROUP BY geographyL2.id")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByGeographyL3", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE UPPER(geographyL3.name) = UPPER(:geographyL3) GROUP BY domain, vendor")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByDomainAndGeographyL3", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE UPPER(domain) = UPPER(:domain) AND UPPER(geographyL3.name) = UPPER(:geographyL3) GROUP BY vendor")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByVendorAndGeographyL3", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE UPPER(vendor) = UPPER(:vendor) AND UPPER(geographyL3.name) = UPPER(:geographyL3) GROUP BY domain")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByDomainAndVendorAndGeographyL3", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE UPPER(domain) = UPPER(:domain) AND UPPER(vendor) = UPPER(:vendor) AND UPPER(geographyL3.name) = UPPER(:geographyL3)")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByGeographyL3ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE geographyL3.id = :geographyL3 GROUP BY domain, vendor")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByMultiGeographyL3ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE geographyL3.id IN (:geographyL3) GROUP BY domain, vendor, geographyL3.id")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByDomainAndGeographyL3ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE UPPER(domain) = UPPER(:domain) AND geographyL3.id = :geographyL3 GROUP BY vendor")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByDomainAndMultiGeographyL3ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE UPPER(domain) = UPPER(:domain) AND geographyL3.id IN (:geographyL3) GROUP BY vendor, geographyL3.id")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByVendorAndGeographyL3ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE UPPER(vendor) = UPPER(:vendor) AND geographyL3.id = :geographyL3 GROUP BY domain")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByVendorAndMultiGeographyL3ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE UPPER(vendor) = UPPER(:vendor) AND geographyL3.id IN (:geographyL3) GROUP BY domain,geographyL3.id")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByDomainAndVendorAndGeographyL3ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE UPPER(domain) = UPPER(:domain) AND UPPER(vendor) = UPPER(:vendor) AND geographyL3.id = :geographyL3")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByDomainAndVendorAndMultiGeographyL3ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE UPPER(domain) = UPPER(:domain) AND UPPER(vendor) = UPPER(:vendor) AND geographyL3.id IN (:geographyL3) GROUP BY geographyL3.id")


@NamedQuery(name = "HBaseRowPrefix.getPrefixByCoreGeography", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE UPPER(otherGeography.name) = UPPER(:coreGeography) GROUP BY domain, vendor")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByDomainAndCoreGeography", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE UPPER(domain) = UPPER(:domain) AND UPPER(otherGeography.name) = UPPER(:coreGeography) GROUP BY vendor")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByVendorAndCoreGeography", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE UPPER(vendor) = UPPER(:vendor) AND UPPER(otherGeography.name) = UPPER(:coreGeography) GROUP BY domain")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByDomainAndVendorAndCoreGeography", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE UPPER(domain) = UPPER(:domain) AND UPPER(vendor) = UPPER(:vendor) AND UPPER(otherGeography.name) = UPPER(:coreGeography)")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByCoreGeographyID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE otherGeography.id = :coreGeography GROUP BY domain, vendor")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByCoreMultiGeographyID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE otherGeography.id IN (:coreGeography) GROUP BY domain, vendor, otherGeography.id")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByDomainAndCoreGeographyID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE UPPER(domain) = UPPER(:domain) AND otherGeography.id = :coreGeography GROUP BY vendor")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByDomainAndCoreMultiGeographyID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE UPPER(domain) = UPPER(:domain) AND otherGeography.id IN (:coreGeography) GROUP BY vendor, otherGeography.id")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByVendorAndCoreGeographyID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE UPPER(vendor) = UPPER(:vendor) AND otherGeography.id = :coreGeography GROUP BY domain")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByVendorAndCoreMultiGeographyID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE UPPER(vendor) = UPPER(:vendor) AND otherGeography.id IN (:coreGeography) GROUP BY domain, otherGeography.id")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByDomainAndVendorAndCoreGeographyID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE UPPER(domain) = UPPER(:domain) AND UPPER(vendor) = UPPER(:vendor) AND otherGeography.id = :coreGeography")
@NamedQuery(name = "HBaseRowPrefix.getPrefixByDomainAndVendorAndCoreMultiGeographyID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE UPPER(domain) = UPPER(:domain) AND UPPER(vendor) = UPPER(:vendor) AND otherGeography.id IN (:coreGeography) GROUP BY otherGeography.id")

@NamedQuery(name = "HBaseRowPrefix.getMaxRowPrefix", query = "SELECT new com.inn.foresight.core.infra.rowprefix.HbaseRowPrefixGeoWrapper(domain,vendor,MAX(numericPrefix)) FROM HBaseRowPrefix WHERE domain IS NOT NULL AND vendor IS NOT NULL AND UPPER(domain) NOT IN ('CORE','IMS') GROUP BY domain , vendor ")
@NamedQuery(name = "HBaseRowPrefix.getAllNumericCode", query = "SELECT numericPrefix FROM HBaseRowPrefix ")
@NamedQuery(name = "HBaseRowPrefix.getMissingGeography", query = "SELECT new com.inn.foresight.core.infra.rowprefix.HbaseRowPrefixGeoWrapper(l1.id , l2.id, l3.id,l1.name,l2.name,l3.name) FROM GeographyL3 l3 JOIN l3.geographyL2  l2 JOIN l2.geographyL1 l1 WHERE l3.id NOT IN (SELECT DISTINCT h.geographyL3.id FROM HBaseRowPrefix h WHERE h.geographyL3 IS NOT NULL AND h.geographyL3.id IS NOT NULL)")
@NamedQuery(name = "HBaseRowPrefix.getAvailableRowPrefix", query = "SELECT new com.inn.foresight.core.infra.rowprefix.HbaseRowPrefixGeoWrapper(h.domain,h.vendor,l1.id,l2.id,l3.id,l1.name,l2.name,l3.name) FROM HBaseRowPrefix h LEFT JOIN h.geographyL1 l1 LEFT JOIN h.geographyL2 l2 LEFT JOIN h.geographyL3 l3  WHERE domain IS NOT NULL AND vendor IS NOT NULL AND UPPER(domain) NOT IN ('CORE','IMS')")

@NamedQuery(name = "HBaseRowPrefix.getL1PrefixByDomain", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix h WHERE UPPER(h.domain) = UPPER(:domain) AND h.geographyL1 IS NOT NULL AND h.geographyL2 IS NULL AND h.geographyL3 IS NULL")
@NamedQuery(name = "HBaseRowPrefix.getL1PrefixByDomainAndVendor", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix h WHERE UPPER(h.domain) = UPPER(:domain) AND h.vendor IN (:vendor) AND h.geographyL1 IS NOT NULL AND h.geographyL2 IS NULL AND h.geographyL3 IS NULL")

@NamedQuery(name = "HBaseRowPrefix.getPanLevelPrefixByVendorAndDomain", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix  WHERE geographyL1 is null and geographyL2 is null and geographyL3 is null and otherGeography is null and UPPER(domain) = UPPER(:domain) AND vendor = (:vendor)")

@NamedQuery(name = "HBaseRowPrefix.getPrefixRangeByDomainAndVendorList", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(domain) = UPPER(:domain) AND UPPER(vendor) IN (:vendors) GROUP BY domain,vendor")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByMultiDomainAndMultiGeographyL1ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(domain) IN (:domain) AND geographyL1.id IN (:geographyL1) GROUP BY vendor,geographyL1.id")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByMultiVendorAndMultiGeographyL1ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(vendor) IN (:vendor) AND geographyL1.id IN (:geographyL1) GROUP BY domain,geographyL1.id")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByMultiDomainAndMultiVendorAndMultiGeographyL1ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(domain) IN (:domain) AND UPPER(vendor) IN (:vendor) AND geographyL1.id IN (:geographyL1) group by geographyL1.id")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByMultiDomainAndMultiGeographyL2ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(domain) IN (:domain) AND geographyL2.id IN (:geographyL2) GROUP BY vendor, geographyL2.id")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByMultiVendorAndMultiGeographyL2ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(vendor) IN (:vendor) AND geographyL2.id IN (:geographyL2) GROUP BY domain,geographyL2.id")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByMultiDomainAndMultiVendorAndMultiGeographyL2ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefixRange(MIN(numericPrefix), MAX(numericPrefix)) FROM HBaseRowPrefix WHERE UPPER(domain) IN (:domain) AND UPPER(vendor) IN (:vendor) AND geographyL2.id IN (:geographyL2) GROUP BY geographyL2.id")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByMultiDomainAndMultiGeographyL3ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE UPPER(domain) IN (:domain) AND geographyL3.id IN (:geographyL3) GROUP BY vendor, geographyL3.id")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByMultiVendorAndMultiGeographyL3ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE UPPER(vendor) IN (:vendor) AND geographyL3.id IN (:geographyL3) GROUP BY domain,geographyL3.id")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByMultiDomainAndMultiVendorAndMultiGeographyL3ID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE UPPER(domain) IN (:domain) AND UPPER(vendor) IN (:vendor) AND geographyL3.id IN (:geographyL3) GROUP BY geographyL3.id")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByMultiDomainAndMultiCoreGeographyID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE UPPER(domain) IN (:domain) AND otherGeography.id IN (:coreGeography) GROUP BY vendor, otherGeography.id")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByMultiVendorAndMultiCoreGeographyID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE UPPER(vendor) IN (:vendor) AND otherGeography.id IN (:coreGeography) GROUP BY domain, otherGeography.id")

@NamedQuery(name = "HBaseRowPrefix.getPrefixByMultiDomainAndMultiVendorAndMultiCoreGeographyID", query = "SELECT new com.inn.foresight.core.infra.rowprefix.RowPrefix(numericPrefix) FROM HBaseRowPrefix WHERE UPPER(domain) IN (:domain) AND UPPER(vendor) IN (:vendor) AND otherGeography.id IN (:coreGeography) GROUP BY otherGeography.id")

@Entity
@Table(name = "HbaseRowPrefix") // TODO rename to HBaseRowPrefix
@XmlRootElement(name = "HBaseRowPrefix")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class HBaseRowPrefix implements Serializable {

    private static final long serialVersionUID = -8322886465826238987L;

    // TODO rename to HBaseRowPrefixId_Pk
    @Id
    @Column(name = "hbaserowprefixid_pk")
    private Integer id;

    // TODO rename to numericPrefix
    @Column(name = "numericcode", nullable = false, insertable = true, updatable = false, unique = true)
    private Integer numericPrefix;

    @Column(nullable = false, length = 20, insertable = true, updatable = false)
    private String domain;

    @Column(nullable = true, length = 20, insertable = true, updatable = false)
    private String vendor;

    @JoinColumn(name = "Geographyl1id_fk", nullable = true, insertable = true, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private GeographyL1 geographyL1;

    @JoinColumn(name = "Geographyl2id_fk", nullable = true, insertable = true, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private GeographyL2 geographyL2;

    @JoinColumn(name = "Geographyl3id_fk", nullable = true, insertable = true, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private GeographyL3 geographyL3;

    @JoinColumn(name = "othergeographyid_fk", nullable = true, insertable = true, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private OtherGeography otherGeography;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumericPrefix() {
        return numericPrefix;
    }

    public void setNumericPrefix(Integer numericPrefix) {
        this.numericPrefix = numericPrefix;
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

    public GeographyL1 getGeographyL1() {
        return geographyL1;
    }

    public void setGeographyL1(GeographyL1 geographyL1) {
        this.geographyL1 = geographyL1;
    }

    public GeographyL2 getGeographyL2() {
        return geographyL2;
    }

    public void setGeographyL2(GeographyL2 geographyL2) {
        this.geographyL2 = geographyL2;
    }

    public GeographyL3 getGeographyL3() {
        return geographyL3;
    }

    public void setGeographyL3(GeographyL3 geographyL3) {
        this.geographyL3 = geographyL3;
    }

    public OtherGeography getOtherGeography() {
        return otherGeography;
    }

    public void setOtherGeography(OtherGeography otherGeography) {
        this.otherGeography = otherGeography;
    }

}

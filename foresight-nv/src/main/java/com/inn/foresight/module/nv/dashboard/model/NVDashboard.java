package com.inn.foresight.module.nv.dashboard.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.inn.foresight.module.nv.dashboard.passive.utils.NVPassiveConstants;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.module.nv.dashboard.utils.NVDashboardConstants;
import com.inn.product.um.geography.model.GeographyL1;
import com.inn.product.um.geography.model.GeographyL2;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;

/**
 * The Class NVDashboard.
 */

@NamedQuery(name = NVDashboardConstants.GET_NVDASHBOARD_DATA_BY_DATE_QUERY, query = "select d from NVDashboard d where d.band=:band "
        + "and d.technology=:technology and date(d.creationTime) between date(:firstDate) and date(:lastDate) and d.weekNumber=0 and d.monthNumber=0 order by date(d.creationTime)")

@NamedQuery(name = NVDashboardConstants.GET_NVDASHBOARD_DATA_BY_WEEK_QUERY, query = "select d  from NVDashboard d where d.band=:band "
        + "and d.technology=:technology and d.weekNumber between :firstWeek and :lastWeek and d.monthNumber=0 order by d.weekNumber ")

@NamedQuery(name = NVDashboardConstants.GET_NVDASHBOARD_DATA_BY_MONTH_QUERY, query = "select d  from NVDashboard d where d.band=:band "
        + " and d.technology=:technology and d.monthNumber between :firstMonth and :lastMonth and d.weekNumber=0 order by d.monthNumber")

@NamedQuery(name = NVDashboardConstants.GET_UC_DATA_BY_DATE_QUERY, query = "select new com.inn.foresight.module.nv.dashboard.wrapper.NVDashboardWrapper(d.enterpriseUC,d.androidUC,d.iosUC,d.activeUC,d.passiveUC,d.consumerUC,d.totalUC) from NVDashboard d where d.band=:band "
        + "and d.technology=:technology and date(d.creationTime) = date(:lastDate) and name is null and type is null and d.weekNumber=0 and d.monthNumber=0")

@NamedQuery(name = NVDashboardConstants.GET_UC_DATA_BY_WEEK_QUERY, query = "select new com.inn.foresight.module.nv.dashboard.wrapper.NVDashboardWrapper(d.enterpriseUC,d.androidUC,d.iosUC,d.activeUC,d.passiveUC,d.consumerUC,d.totalUC) from NVDashboard d where d.band=:band "
        + "and d.technology=:technology and d.weekNumber =:lastWeek and d.monthNumber=0 and name is null and type is null")

@NamedQuery(name = NVDashboardConstants.GET_UC_DATA_BY_MONTH_QUERY, query = "select new com.inn.foresight.module.nv.dashboard.wrapper.NVDashboardWrapper(d.enterpriseUC,d.androidUC,d.iosUC,d.activeUC,d.passiveUC,d.consumerUC,d.totalUC) from NVDashboard d where d.band=:band "
        + " and d.technology=:technology and d.monthNumber =:lastMonth and d.weekNumber=0 and name is null and type is null")

@NamedQuery(name = NVDashboardConstants.GET_ALL_OPERATOR_COUNT_BY_DATE_QUERY, query = "select new com.inn.foresight.module.nv.dashboard.wrapper.NVDistributionWrapper(d.operator,d.activeSampleCount,d.passiveSampleCount) from NVDashboard d where d.band=:band "
        + "and d.technology=:technology and date(d.creationTime) = date(:lastDate) and d.type is null and d.name is null and d.weekNumber=0 and d.monthNumber=0")

@NamedQuery(name = NVDashboardConstants.GET_ALL_OPERATOR_COUNT_BY_WEEK_QUERY, query = "select new com.inn.foresight.module.nv.dashboard.wrapper.NVDistributionWrapper(d.operator,d.activeSampleCount,d.passiveSampleCount) from NVDashboard d where d.band=:band "
        + "and d.technology=:technology and d.weekNumber =:lastWeek and d.monthNumber=0 and d.type is null and d.name is null")

@NamedQuery(name = NVDashboardConstants.GET_ALL_OPERATOR_COUNT_BY_MONTH_QUERY, query = "select new com.inn.foresight.module.nv.dashboard.wrapper.NVDistributionWrapper(d.operator,d.activeSampleCount,d.passiveSampleCount) from NVDashboard d where d.band=:band "
        + "and d.technology=:technology and d.monthNumber =:lastMonth and d.weekNumber=0 and d.type is null and d.name is null")

@NamedQuery(name = NVDashboardConstants.GET_ALL_OS_DEV_COUNT_BY_DATE_QUERY, query = "select new com.inn.foresight.module.nv.dashboard.wrapper.NVDistributionWrapper(d.name,d.activeSampleCount,d.passiveSampleCount) from NVDashboard d where d.band=:band "
        + "and d.technology=:technology and date(d.creationTime) = date(:lastDate) and type=:type and d.weekNumber=0 and d.monthNumber=0")

@NamedQuery(name = NVDashboardConstants.GET_ALL_OS_DEV_COUNT_BY_DATE_FOR_REPORT_QUERY, query = "select d from NVDashboard d where d.band=:band "
        + "and d.technology=:technology and date(d.creationTime) between date(:firstDate) and date(:lastDate) and d.weekNumber=0 and d.monthNumber=0 ORDER BY (d.activeSampleCount + d.passiveSampleCount) DESC")

@NamedQuery(name = NVDashboardConstants.GET_ALL_OS_DEV_COUNT_BY_WEEK_QUERY, query = "select new com.inn.foresight.module.nv.dashboard.wrapper.NVDistributionWrapper(d.name,d.activeSampleCount,d.passiveSampleCount) from NVDashboard d where d.band=:band "
        + "and d.technology=:technology and d.weekNumber = :lastWeek and d.monthNumber=0 and type=:type")

@NamedQuery(name = NVDashboardConstants.GET_ALL_OS_DEV_COUNT_BY_MONTH_QUERY, query = "select new com.inn.foresight.module.nv.dashboard.wrapper.NVDistributionWrapper(d.name,d.activeSampleCount,d.passiveSampleCount) from NVDashboard d where d.band=:band "
        + "and d.technology=:technology and d.monthNumber =:lastMonth and d.weekNumber=0 and type=:type")

@NamedQuery(name = NVDashboardConstants.GET_TOP_DL_GEOGRAPHY_QUERY, query = "select d from NVDashboard d where d.band=:band "
        + "and d.technology=:technology and d.type is null order by d.avgDlRate desc")

@NamedQuery(name = NVDashboardConstants.GET_TOP_UC_GEOGRAPHY_QUERY, query = "select d from NVDashboard d where d.band=:band "
        + "and d.technology=:technology and d.type is null order by d.totalUC desc")

@NamedQuery(name = NVDashboardConstants.GET_TOP_SIGNAL_STRENGTH_GEOGRAPHY_QUERY, query = "select d from NVDashboard d where d.band=:band "
        + "and d.technology=:technology and d.type is null order by d.signalStrength desc")

@NamedQuery(name = NVDashboardConstants.GET_ANDROID_IOS_TEST_COUNT_BY_DATE, query = "select new com.inn.foresight.module.nv.dashboard.wrapper.NVDashboardWrapper(d.sampleAndroid + d.sampleIos, d.sampleAndroid, d.sampleIos) from NVDashboard d where d.band=:band "
        + "and d.technology=:technology and d.operator=:operator and  date(d.creationTime) between date(:firstDate) and date(:lastDate)")

@FilterDef(name = NVDashboardConstants.GEOGRAPHY_PANL_FILTER)
@FilterDef(name = NVDashboardConstants.GEOGRAPHY_PANL_FILTER_FOR_REPORT)
@FilterDef(name = NVDashboardConstants.TOP_GEOGRAPHYL1_FILTER)
@FilterDef(name = NVDashboardConstants.GEOGRAPHYL1_FILTER, parameters = {
        @ParamDef(name = NVDashboardConstants.GEOGRAPHY, type = "java.lang.Integer")})
@FilterDef(name = NVDashboardConstants.GEOGRAPHYL2_FILTER, parameters = {
        @ParamDef(name = NVDashboardConstants.GEOGRAPHY, type = "java.lang.Integer")})
@FilterDef(name = NVDashboardConstants.GEOGRAPHYL3_FILTER, parameters = {
        @ParamDef(name = NVDashboardConstants.GEOGRAPHY, type = "java.lang.Integer")})
@FilterDef(name = NVDashboardConstants.GEOGRAPHYL4_FILTER, parameters = {
        @ParamDef(name = NVDashboardConstants.GEOGRAPHY, type = "java.lang.Integer")})
@FilterDef(name = NVDashboardConstants.GEOGRAPHYL1_FILTER_FOR_REPORT, parameters = {
        @ParamDef(name = NVDashboardConstants.GEOGRAPHY, type = "java.lang.Integer")})
@FilterDef(name = NVDashboardConstants.GEOGRAPHYL2_FILTER_FOR_REPORT, parameters = {
        @ParamDef(name = NVDashboardConstants.GEOGRAPHY, type = "java.lang.Integer")})
@FilterDef(name = NVDashboardConstants.GEOGRAPHYL3_FILTER_FOR_REPORT, parameters = {
        @ParamDef(name = NVDashboardConstants.GEOGRAPHY, type = "java.lang.Integer")})
@FilterDef(name = NVDashboardConstants.GEOGRAPHYL4_FILTER_FOR_REPORT, parameters = {
        @ParamDef(name = NVDashboardConstants.GEOGRAPHY, type = "java.lang.Integer")})

@FilterDef(name = NVDashboardConstants.TOP_GEOGRAPHYL2_FILTER, parameters = {
        @ParamDef(name = NVDashboardConstants.GEOGRAPHY, type = "java.lang.Integer")})
@FilterDef(name = NVDashboardConstants.TOP_GEOGRAPHYL3_FILTER, parameters = {
        @ParamDef(name = NVDashboardConstants.GEOGRAPHY, type = "java.lang.Integer")})
@FilterDef(name = NVDashboardConstants.TOP_GEOGRAPHYL4_FILTER, parameters = {
        @ParamDef(name = NVDashboardConstants.GEOGRAPHY, type = "java.lang.Integer")})
@FilterDef(name = NVDashboardConstants.NAME_FILTER, parameters = {
        @ParamDef(name = NVDashboardConstants.NAME, type = "java.lang.String")})
@FilterDef(name = NVDashboardConstants.TYPE_NAME_FILTER, parameters = {
        @ParamDef(name = NVDashboardConstants.NAME, type = "java.lang.String"),
        @ParamDef(name = NVDashboardConstants.TYPE, type = "java.lang.String")})
@FilterDef(name = NVDashboardConstants.TYPE_FILTER_FOR_REPORT, parameters = {
        @ParamDef(name = NVDashboardConstants.TYPE, type = "java.lang.String")})
@FilterDef(name = NVDashboardConstants.NAME_LIKE_ANDROID_FILTER)
@FilterDef(name = NVDashboardConstants.NAME_LIKE_IOS_FILTER)
@FilterDef(name = NVDashboardConstants.TYPE_NAME_NULL_FILTER)
@FilterDef(name = NVDashboardConstants.DATE_FILTER, parameters = {
        @ParamDef(name = NVDashboardConstants.CREATION_TIME, type = "java.util.Date")})
@FilterDef(name = NVDashboardConstants.WEEK_FILTER, parameters = {
        @ParamDef(name = NVDashboardConstants.WEEK_NUMBER, type = "java.lang.Integer")})
@FilterDef(name = NVDashboardConstants.MONTH_FILTER, parameters = {
        @ParamDef(name = NVDashboardConstants.MONTH_NUMBER, type = "java.lang.Integer")})
@FilterDef(name = NVDashboardConstants.COUNTRY_FILTER, parameters = {
        @ParamDef(name = NVDashboardConstants.COUNTRY, type = "java.lang.String")})
@FilterDef(name = NVDashboardConstants.OPERATOR_FILTER, parameters = {
        @ParamDef(name = NVDashboardConstants.OPERATOR, type = "java.lang.String")})

@Filter(name = NVDashboardConstants.GEOGRAPHY_PANL_FILTER_FOR_REPORT, condition = " geographyl2id_fk is null and geographyl3id_fk is null and geographyl4id_fk is null")
@Filter(name = NVDashboardConstants.GEOGRAPHY_PANL_FILTER, condition = "geographyl1id_fk is null and geographyl2id_fk is null and geographyl3id_fk is null and geographyl4id_fk is null")
@Filter(name = NVDashboardConstants.TOP_GEOGRAPHYL1_FILTER, condition = " geographyl1id_fk is not null and geographyl2id_fk is null and geographyl3id_fk is null and geographyl4id_fk is null")
@Filter(name = NVDashboardConstants.GEOGRAPHYL1_FILTER, condition = " geographyl1id_fk=:geography and geographyl2id_fk is null and geographyl3id_fk is null and geographyl4id_fk is null")
@Filter(name = NVDashboardConstants.GEOGRAPHYL2_FILTER, condition = " geographyl2id_fk=:geography and geographyl3id_fk is null and geographyl4id_fk is null ")
@Filter(name = NVDashboardConstants.GEOGRAPHYL3_FILTER, condition = " geographyl3id_fk=:geography and geographyl4id_fk is null")
@Filter(name = NVDashboardConstants.GEOGRAPHYL4_FILTER, condition = " geographyl4id_fk=:geography")
@Filter(name = NVDashboardConstants.GEOGRAPHYL1_FILTER_FOR_REPORT, condition = " geographyl1id_fk = (:geography) and geographyl3id_fk is null and geographyl4id_fk is null")
@Filter(name = NVDashboardConstants.GEOGRAPHYL2_FILTER_FOR_REPORT, condition = " geographyl2id_fk = (:geography) and geographyl4id_fk is null ")
@Filter(name = NVDashboardConstants.GEOGRAPHYL3_FILTER_FOR_REPORT, condition = " geographyl3id_fk = (:geography) ")
@Filter(name = NVDashboardConstants.GEOGRAPHYL4_FILTER_FOR_REPORT, condition = " geographyl4id_fk = (:geography) ")

@Filter(name = NVDashboardConstants.TOP_GEOGRAPHYL2_FILTER, condition = " geographyl1id_fk=:geography and geographyl2id_fk is not null and geographyl3id_fk is null and geographyl4id_fk is null")
@Filter(name = NVDashboardConstants.TOP_GEOGRAPHYL3_FILTER, condition = " geographyl2id_fk=:geography and geographyl3id_fk is not null and geographyl4id_fk is null")
@Filter(name = NVDashboardConstants.TOP_GEOGRAPHYL4_FILTER, condition = " geographyl3id_fk=:geography and geographyl4id_fk is not null")
@Filter(name = NVDashboardConstants.NAME_FILTER, condition = " name=:name")
@Filter(name = NVDashboardConstants.TYPE_NAME_FILTER, condition = "name=:name and type=:type")
@Filter(name = NVDashboardConstants.TYPE_FILTER_FOR_REPORT, condition = " (type!=:type or type is null)")
@Filter(name = NVDashboardConstants.NAME_LIKE_ANDROID_FILTER, condition = " name like 'Android%'")
@Filter(name = NVDashboardConstants.NAME_LIKE_IOS_FILTER, condition = " (name like 'iOS%' or name like 'iPhone%')")
@Filter(name = NVDashboardConstants.TYPE_NAME_NULL_FILTER, condition = " type is null and name is null")
@Filter(name = NVDashboardConstants.DATE_FILTER, condition = " creationtime=:creationtime and weeknumber=0 and monthnumber=0")
@Filter(name = NVDashboardConstants.WEEK_FILTER, condition = " weekNumber=:weeknumber and d.monthnumber=0")
@Filter(name = NVDashboardConstants.MONTH_FILTER, condition = " monthNumber=:monthNumber and weeknumber=0")
@Filter(name = NVDashboardConstants.COUNTRY, condition = " country=:country")
@Filter(name = NVDashboardConstants.OPERATOR, condition = " operator=:operator")


@Entity
@Table(name = "NVDashboard")
@XmlRootElement(name = "NVDashboard")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class NVDashboard implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The id.
     */
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "nvdashboardid_pk")
    private Integer id;

    /**
     * The creation time.
     */
    @Basic
    @Column(name = "creationtime")
    private Date creationTime;

    /**
     * The modification time.
     */
    @Basic
    @Column(name = "modificationtime")
    private Date modificationTime;

    /**
     * The avg dl rate.
     */
    @Basic
    @Column(name = "avgdlrate")
    private Double avgDlRate;

    /**
     * The max dl rate.
     */
    @Basic
    @Column(name = "maxdlrate")
    private Double maxDlRate;

    /**
     * The avg ul rate.
     */
    @Basic
    @Column(name = "avgulrate")
    private Double avgUlRate;

    /**
     * The max ul rate.
     */
    @Basic
    @Column(name = "maxulrate")
    private Double maxUlRate;

    /**
     * The latency.
     */
    @Basic
    @Column(name = "latency")
    private Double latency;

    /**
     * The jitter.
     */
    @Basic
    @Column(name = "jitter")
    private Double jitter;

    /**
     * The packet loss.
     */
    @Basic
    @Column(name = "packetloss")
    private Double packetLoss;

    /**
     * The url 1 browse time.
     */
    @Basic
    @Column(name = "url1browsetime")
    private Double url1BrowseTime;

    /**
     * The url 2 browse time.
     */
    @Basic
    @Column(name = "url2browsetime")
    private Double url2BrowseTime;

    /**
     * The url 3 browse time.
     */
    @Basic
    @Column(name = "url3browsetime")
    private Double url3BrowseTime;

    /**
     * The star rating.
     */
    @Basic
    @Column(name = "starrating")
    private Double starRating;

    /**
     * The enterprise UC.
     */
    @Basic
    @Column(name = "enterpriseuc")
    private Long enterpriseUC;

    /**
     * The android UC.
     */
    @Basic
    @Column(name = "androiduc")
    private Long androidUC;

    /**
     * The ios UC.
     */
    @Basic
    @Column(name = "iosuc")
    private Long iosUC;

    /**
     * The active UC.
     */
    @Basic
    @Column(name = "activeuc")
    private Long activeUC;

    /**
     * The passive UC.
     */
    @Basic
    @Column(name = "passiveuc")
    private Long passiveUC;

    /**
     * The consumer UC.
     */
    @Basic
    @Column(name = "consumeruc")
    private Long consumerUC;

    /**
     * The total UC.
     */
    @Basic
    @Column(name = "totaluc")
    private Long totalUC;

    /**
     * The week number.
     */
    @Basic
    @Column(name = "weeknumber")
    private Integer weekNumber;

    /**
     * The month number.
     */
    @Basic
    @Column(name = "monthnumber")
    private Integer monthNumber;

    /**
     * The band.
     */
    @Basic
    @Column(name = "band")
    private String band;

    /**
     * The technology.
     */
    @Basic
    @Column(name = "technology")
    private String technology;

    /**
     * The signal strength.
     */
    @Basic
    @Column(name = "signalstrength")
    private Double signalStrength;

    /**
     * The quality.
     */
    @Basic
    @Column(name = "quality")
    private Double quality;

    /**
     * The sinr.
     */
    @Basic
    @Column(name = "sinr")
    private Double sinr;

    /**
     * The operator.
     */
    @Basic
    @Column(name = "operator")
    private String operator;

    /**
     * The name.
     */
    @Basic
    @Column(name = "name")
    private String name;

    /**
     * The type.
     */
    @Basic
    @Column(name = "type")
    private String type;

    /**
     * The geography L 1.
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "geographyl1id_fk", nullable = false)
    private GeographyL1 geographyL1;

    /**
     * The geography L 2.
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "geographyl2id_fk", nullable = false)
    private GeographyL2 geographyL2;

    /**
     * The geography L 3.
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "geographyl3id_fk", nullable = false)
    private GeographyL3 geographyL3;

    /**
     * The geography L 4.
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "geographyl4id_fk", nullable = false)

    private GeographyL4 geographyL4;
    /**
     * The active sample count.
     */
    @Basic
    @Column(name = "activesamplecount")
    private Long activeSampleCount;

    /**
     * The passive sample count.
     */
    @Basic
    @Column(name = "passivesamplecount")
    private Long passiveSampleCount;

    /**
     * The passive sample count.
     */
    @Basic
    @Column(name = "sampleandroid")
    private Long sampleAndroid;

    /**
     * The passive sample count.
     */
    @Basic
    @Column(name = "sampleios")
    private Long sampleIos;

    /**
     * country
     */
    @Basic
    @Column(name = "country")
    private String country;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getSampleAndroid() {
        return sampleAndroid;
    }

    public void setSampleAndroid(Long sampleAndroid) {
        this.sampleAndroid = sampleAndroid;
    }

    public Long getSampleIos() {
        return sampleIos;
    }

    public void setSampleIos(Long sampleIos) {
        this.sampleIos = sampleIos;
    }

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
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the creation time.
     *
     * @return the creationTime
     */
    public Date getCreationTime() {
        return creationTime;
    }

    /**
     * Sets the creation time.
     *
     * @param creationTime the creationTime to set
     */
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    /**
     * Gets the modification time.
     *
     * @return the modificationTime
     */
    public Date getModificationTime() {
        return modificationTime;
    }

    /**
     * Sets the modification time.
     *
     * @param modificationTime the modificationTime to set
     */
    public void setModificationTime(Date modificationTime) {
        this.modificationTime = modificationTime;
    }

    /**
     * Gets the avg dl rate.
     *
     * @return the avgDlRate
     */
    public Double getAvgDlRate() {
        return avgDlRate;
    }

    /**
     * Sets the avg dl rate.
     *
     * @param avgDlRate the avgDlRate to set
     */
    public void setAvgDlRate(Double avgDlRate) {
        this.avgDlRate = avgDlRate;
    }

    /**
     * Gets the max dl rate.
     *
     * @return the maxDlRate
     */
    public Double getMaxDlRate() {
        return maxDlRate;
    }

    /**
     * Sets the max dl rate.
     *
     * @param maxDlRate the maxDlRate to set
     */
    public void setMaxDlRate(Double maxDlRate) {
        this.maxDlRate = maxDlRate;
    }

    /**
     * Gets the avg ul rate.
     *
     * @return the avgUlRate
     */
    public Double getAvgUlRate() {
        return avgUlRate;
    }

    /**
     * Sets the avg ul rate.
     *
     * @param avgUlRate the avgUlRate to set
     */
    public void setAvgUlRate(Double avgUlRate) {
        this.avgUlRate = avgUlRate;
    }

    /**
     * Gets the max ul rate.
     *
     * @return the maxUlRate
     */
    public Double getMaxUlRate() {
        return maxUlRate;
    }

    /**
     * Sets the max ul rate.
     *
     * @param maxUlRate the maxUlRate to set
     */
    public void setMaxUlRate(Double maxUlRate) {
        this.maxUlRate = maxUlRate;
    }

    /**
     * Gets the latency.
     *
     * @return the latency
     */
    public Double getLatency() {
        return latency;
    }

    /**
     * Sets the latency.
     *
     * @param latency the latency to set
     */
    public void setLatency(Double latency) {
        this.latency = latency;
    }

    /**
     * Gets the jitter.
     *
     * @return the jitter
     */
    public Double getJitter() {
        return jitter;
    }

    /**
     * Sets the jitter.
     *
     * @param jitter the jitter to set
     */
    public void setJitter(Double jitter) {
        this.jitter = jitter;
    }

    /**
     * Gets the packet loss.
     *
     * @return the packetLoss
     */
    public Double getPacketLoss() {
        return packetLoss;
    }

    /**
     * Sets the packet loss.
     *
     * @param packetLoss the packetLoss to set
     */
    public void setPacketLoss(Double packetLoss) {
        this.packetLoss = packetLoss;
    }

    /**
     * Gets the url 1 browse time.
     *
     * @return the url1BrowseTime
     */
    public Double getUrl1BrowseTime() {
        return url1BrowseTime;
    }

    /**
     * Sets the url 1 browse time.
     *
     * @param url1BrowseTime the url1BrowseTime to set
     */
    public void setUrl1BrowseTime(Double url1BrowseTime) {
        this.url1BrowseTime = url1BrowseTime;
    }

    /**
     * Gets the url 2 browse time.
     *
     * @return the url2BrowseTime
     */
    public Double getUrl2BrowseTime() {
        return url2BrowseTime;
    }

    /**
     * Sets the url 2 browse time.
     *
     * @param url2BrowseTime the url2BrowseTime to set
     */
    public void setUrl2BrowseTime(Double url2BrowseTime) {
        this.url2BrowseTime = url2BrowseTime;
    }

    /**
     * Gets the url 3 browse time.
     *
     * @return the url3BrowseTime
     */
    public Double getUrl3BrowseTime() {
        return url3BrowseTime;
    }

    /**
     * Sets the url 3 browse time.
     *
     * @param url3BrowseTime the url3BrowseTime to set
     */
    public void setUrl3BrowseTime(Double url3BrowseTime) {
        this.url3BrowseTime = url3BrowseTime;
    }

    /**
     * Gets the star rating.
     *
     * @return the starRating
     */
    public Double getStarRating() {
        return starRating;
    }

    /**
     * Sets the star rating.
     *
     * @param starRating the starRating to set
     */
    public void setStarRating(Double starRating) {
        this.starRating = starRating;
    }

    /**
     * Gets the enterprise UC.
     *
     * @return the enterpriseUC
     */
    public Long getEnterpriseUC() {
        return enterpriseUC;
    }

    /**
     * Sets the enterprise UC.
     *
     * @param enterpriseUC the enterpriseUC to set
     */
    public void setEnterpriseUC(Long enterpriseUC) {
        this.enterpriseUC = enterpriseUC;
    }

    /**
     * Gets the android UC.
     *
     * @return the androidUC
     */
    public Long getAndroidUC() {
        return androidUC;
    }

    /**
     * Sets the android UC.
     *
     * @param androidUC the androidUC to set
     */
    public void setAndroidUC(Long androidUC) {
        this.androidUC = androidUC;
    }

    /**
     * Gets the ios UC.
     *
     * @return the iosUC
     */
    public Long getIosUC() {
        return iosUC;
    }

    /**
     * Sets the ios UC.
     *
     * @param iosUC the iosUC to set
     */
    public void setIosUC(Long iosUC) {
        this.iosUC = iosUC;
    }

    /**
     * Gets the active UC.
     *
     * @return the activeUC
     */
    public Long getActiveUC() {
        return activeUC;
    }

    /**
     * Sets the active UC.
     *
     * @param activeUC the activeUC to set
     */
    public void setActiveUC(Long activeUC) {
        this.activeUC = activeUC;
    }

    /**
     * Gets the passive UC.
     *
     * @return the passiveUC
     */
    public Long getPassiveUC() {
        return passiveUC;
    }

    /**
     * Sets the passive UC.
     *
     * @param passiveUC the passiveUC to set
     */
    public void setPassiveUC(Long passiveUC) {
        this.passiveUC = passiveUC;
    }

    /**
     * Gets the consumer UC.
     *
     * @return the consumerUC
     */
    public Long getConsumerUC() {
        return consumerUC;
    }

    /**
     * Sets the consumer UC.
     *
     * @param consumerUC the consumerUC to set
     */
    public void setConsumerUC(Long consumerUC) {
        this.consumerUC = consumerUC;
    }

    /**
     * Gets the total UC.
     *
     * @return the totalUC
     */
    public Long getTotalUC() {
        return totalUC;
    }

    /**
     * Sets the total UC.
     *
     * @param totalUC the totalUC to set
     */
    public void setTotalUC(Long totalUC) {
        this.totalUC = totalUC;
    }

    /**
     * Gets the week number.
     *
     * @return the weekNumber
     */
    public Integer getWeekNumber() {
        return weekNumber;
    }

    /**
     * Sets the week number.
     *
     * @param weekNumber the weekNumber to set
     */
    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    /**
     * Gets the month number.
     *
     * @return the monthNumber
     */
    public Integer getMonthNumber() {
        return monthNumber;
    }

    /**
     * Sets the month number.
     *
     * @param monthNumber the monthNumber to set
     */
    public void setMonthNumber(Integer monthNumber) {
        this.monthNumber = monthNumber;
    }

    /**
     * Gets the band.
     *
     * @return the band
     */
    public String getBand() {
        return band;
    }

    /**
     * Sets the band.
     *
     * @param band the band to set
     */
    public void setBand(String band) {
        this.band = band;
    }

    /**
     * Gets the technology.
     *
     * @return the technology
     */
    public String getTechnology() {
        return technology;
    }

    /**
     * Sets the technology.
     *
     * @param technology the technology to set
     */
    public void setTechnology(String technology) {
        this.technology = technology;
    }

    /**
     * Gets the signal strength.
     *
     * @return the signalStrength
     */
    public Double getSignalStrength() {
        return signalStrength;
    }

    /**
     * Sets the signal strength.
     *
     * @param signalStrength the signalStrength to set
     */
    public void setSignalStrength(Double signalStrength) {
        this.signalStrength = signalStrength;
    }

    /**
     * Gets the quality.
     *
     * @return the quality
     */
    public Double getQuality() {
        return quality;
    }

    /**
     * Sets the quality.
     *
     * @param quality the quality to set
     */
    public void setQuality(Double quality) {
        this.quality = quality;
    }

    /**
     * Gets the sinr.
     *
     * @return the sinr
     */
    public Double getSinr() {
        return sinr;
    }

    /**
     * Sets the sinr.
     *
     * @param sinr the sinr to set
     */
    public void setSinr(Double sinr) {
        this.sinr = sinr;
    }

    /**
     * Gets the operator.
     *
     * @return the operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Sets the operator.
     *
     * @param operator the operator to set
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the geography L 1.
     *
     * @return the geographyL1
     */
    public GeographyL1 getGeographyL1() {
        return geographyL1;
    }

    /**
     * Sets the geography L 1.
     *
     * @param geographyL1 the geographyL1 to set
     */
    public void setGeographyL1(GeographyL1 geographyL1) {
        this.geographyL1 = geographyL1;
    }

    /**
     * Gets the geography L 2.
     *
     * @return the geographyL2
     */
    public GeographyL2 getGeographyL2() {
        return geographyL2;
    }

    /**
     * Sets the geography L 2.
     *
     * @param geographyL2 the geographyL2 to set
     */
    public void setGeographyL2(GeographyL2 geographyL2) {
        this.geographyL2 = geographyL2;
    }

    /**
     * Gets the geography L 3.
     *
     * @return the geographyL3
     */
    public GeographyL3 getGeographyL3() {
        return geographyL3;
    }

    /**
     * Sets the geography L 3.
     *
     * @param geographyL3 the geographyL3 to set
     */
    public void setGeographyL3(GeographyL3 geographyL3) {
        this.geographyL3 = geographyL3;
    }

    /**
     * Gets the geography L 4.
     *
     * @return the geographyL4
     */
    public GeographyL4 getGeographyL4() {
        return geographyL4;
    }

    /**
     * Sets the geography L 4.
     *
     * @param geographyL4 the geographyL4 to set
     */
    public void setGeographyL4(GeographyL4 geographyL4) {
        this.geographyL4 = geographyL4;
    }

    /**
     * Gets the active sample count.
     *
     * @return the active sample count
     */
    public Long getActiveSampleCount() {
        return activeSampleCount;
    }

    /**
     * Sets the active sample count.
     *
     * @param activeSampleCount the new active sample count
     */
    public void setActiveSampleCount(Long activeSampleCount) {
        this.activeSampleCount = activeSampleCount;
    }

    /**
     * Gets the passive sample count.
     *
     * @return the passive sample count
     */
    public Long getPassiveSampleCount() {
        return passiveSampleCount;
    }

    /**
     * Sets the passive sample count.
     *
     * @param passiveSampleCount the new passive sample count
     */
    public void setPassiveSampleCount(Long passiveSampleCount) {
        this.passiveSampleCount = passiveSampleCount;
    }

    @Override
    public String toString() {
        return "NVDashboard [id=" + id + ", creationTime=" + creationTime + ", modificationTime=" + modificationTime
                + ", avgDlRate=" + avgDlRate + ", maxDlRate=" + maxDlRate + ", avgUlRate=" + avgUlRate + ", maxUlRate="
                + maxUlRate + ", latency=" + latency + ", jitter=" + jitter + ", packetLoss=" + packetLoss
                + ", url1BrowseTime=" + url1BrowseTime + ", url2BrowseTime=" + url2BrowseTime + ", url3BrowseTime="
                + url3BrowseTime + ", starRating=" + starRating + ", enterpriseUC=" + enterpriseUC + ", androidUC="
                + androidUC + ", iosUC=" + iosUC + ", activeUC=" + activeUC + ", passiveUC=" + passiveUC
                + ", consumerUC=" + consumerUC + ", totalUC=" + totalUC + ", weekNumber=" + weekNumber
                + ", monthNumber=" + monthNumber + ", band=" + band + ", technology=" + technology + ", signalStrength="
                + signalStrength + ", quality=" + quality + ", sinr=" + sinr + ", operator=" + operator + ", name="
                + name + ", type=" + type + ", geographyL1=" + geographyL1 + ", geographyL2=" + geographyL2
                + ", geographyL3=" + geographyL3 + ", geographyL4=" + geographyL4 + ", activeSampleCount="
                + activeSampleCount + ", passiveSampleCount=" + passiveSampleCount + ", sampleAndroid=" + sampleAndroid
                + ", sampleIos=" + sampleIos + "]";
    }

}

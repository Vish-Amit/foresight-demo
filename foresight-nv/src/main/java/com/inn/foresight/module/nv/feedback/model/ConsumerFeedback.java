package com.inn.foresight.module.nv.feedback.model;

import java.io.Serializable;
import java.util.Date;

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

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.module.nv.feedback.constants.ConsumerFeedbackConstant;
import com.inn.foresight.module.nv.profile.model.NVProfileData;
import com.inn.product.um.geography.model.GeographyL4;

	
	@NamedQuery(name = "getFeedBackRatingWithCount", query = "SELECT new com.inn.foresight.module.nv.feedback.wrapper.ConsumerFeedbackWrapper("
			+ "sum(case when c.starrating!=0 then 1 else 0 end), sum(case when c.starratingdata!=0 then 1 else 0 end ), sum( case when c.starratingcvg!=0 then 1 else 0 end)"
			+ ",c.feedbacktype, c.starrating,c.starratingdata, c.starratingcvg)"
			+ " FROM ConsumerFeedback c where c.feedbacktype IS NOT NULL and c.geographyL4 IS NOT NULL and Date(c.feedbacktime)=Date(:date) and  c.operatorname=:operatorname group by c.feedbacktype, c.starrating, c.starratingdata, c.starratingcvg")
	
	@NamedQuery(name = "getFeedBackAvgRating", query = "SELECT new com.inn.foresight.module.nv.feedback.wrapper.ConsumerFeedbackWrapper("
			+ "count(c), c.feedbacktype, ROUND(avg(c.starrating),2))"
			+ " FROM ConsumerFeedback c where c.feedbacktype IS NOT NULL and c.geographyL4 IS NOT NULL and Date(c.feedbacktime)=Date(:date) and c.operatorname=:operatorname group by c.feedbacktype")
	
	@NamedQuery(name = "getDateWiseFeedBackCount", query = "SELECT new com.inn.foresight.module.nv.feedback.wrapper.ConsumerFeedbackWrapper("
			+ "Date(c.feedbacktime), c.feedbacktype, count(c))"
			+ " FROM ConsumerFeedback c where c.feedbacktype IS NOT NULL and c.geographyL4 IS NOT NULL and Date(c.feedbacktime) between Date(:fromDate) and Date(:toDate) and c.operatorname=:operatorname group by Date(c.feedbacktime),c.feedbacktype order by c.feedbacktime asc")
	
	@NamedQuery(name = "getDateWiseTestArea", query ="SELECT new com.inn.foresight.module.nv.feedback.wrapper.ConsumerFeedbackWrapper("
			+ "count(c), c.testarea, c.feedbacktime)"
			+ " FROM ConsumerFeedback c where c.feedbacktype IS NOT NULL and c.geographyL4 IS NOT NULL and c.feedbacktype=:type and c.operatorname=:operatorname and Date(c.feedbacktime) between Date(:fromDate) and Date(:toDate) group by Date(c.feedbacktime), c.testarea having c.testarea is not null order by c.feedbacktime asc")
	
	@NamedQuery(name = "getDateWiseFeedBack", query = "SELECT new com.inn.foresight.module.nv.feedback.wrapper.ConsumerFeedbackWrapper("
			+ "sum(case when c.starrating!=0 then 1 else 0 end), sum(case when c.starratingdata!=0 then 1 else 0 end ), sum( case when c.starratingcvg!=0 then 1 else 0 end), c.feedbacktime)"
			+ " FROM ConsumerFeedback c where c.feedbacktype IS NOT NULL and c.geographyL4 IS NOT NULL and c.feedbacktype=:type and c.operatorname=:operatorname and Date(c.feedbacktime) between Date(:fromDate) and Date(:toDate) group by Date(c.feedbacktime) order by c.feedbacktime asc")
	
	@NamedQuery(name = "getDateWiseVoiceCount", query = "SELECT new com.inn.foresight.module.nv.feedback.wrapper.ConsumerFeedbackWrapper("
			+ "Date(c.feedbacktime), sum(case when c.vcunabletocall!=0 then 1 else 0 end), sum(case when c.vccalldrop!=0 then 1 else 0 end), sum(case when c.vcpooraudio!=0 then 1 else 0 end), sum(case when c.voicemute!=0 then 1 else 0 end), sum(case when c.vconewyaudio!=0 then 1 else 0 end))"
			+ " FROM ConsumerFeedback c where c.feedbacktype IS NOT NULL and c.geographyL4 IS NOT NULL and Date(c.feedbacktime) between Date(:fromDate) and Date(:toDate) and c.operatorname=:operatorname  group by Date(c.feedbacktime) order by c.feedbacktime asc")
	
	@NamedQuery(name = "getDateWiseDataCount", query = "SELECT new com.inn.foresight.module.nv.feedback.wrapper.ConsumerFeedbackWrapper("
			+ "Date(c.feedbacktime), sum(case when c.dataslowspeed!=0 then 1 else 0 end),sum(case when c.dataunavletocon!=0 then 1 else 0 end))"
			+ " FROM ConsumerFeedback c where c.feedbacktype IS NOT NULL and c.geographyL4 IS NOT NULL and Date(c.feedbacktime) between Date(:fromDate) and Date(:toDate) and c.operatorname=:operatorname group by Date(c.feedbacktime) order by c.feedbacktime asc")
	
	@NamedQuery(name = "getDateWiseCoverageCount", query = "SELECT new com.inn.foresight.module.nv.feedback.wrapper.ConsumerFeedbackWrapper("
			+ "sum(case when c.cvgpoorcvgind!=0 then 1 else 0 end), sum(case when c.cvgPoorCvgOut!=0 then 1 else 0 end), Date(c.feedbacktime))"
			+ " FROM ConsumerFeedback c where c.feedbacktype IS NOT NULL and c.geographyL4 IS NOT NULL and Date(c.feedbacktime) between Date(:fromDate) and Date(:toDate) and c.operatorname=:operatorname  group by Date(c.feedbacktime) order by c.feedbacktime asc")
	
	
	@NamedQuery(name = "getTopFeedbackLocations", query = "SELECT new com.inn.foresight.module.nv.feedback.wrapper.ConsumerFeedbackWrapper("
			+ "sum(CASE WHEN c.feedbacktype IN('Campaign Feedback','Event Feedback') THEN 1 ELSE 0 end ) AS co,"
			+ " SUM(CASE WHEN c.feedbacktype='Campaign Feedback' THEN 1 ELSE 0 end ) AS cf,"
			+ "SUM(CASE WHEN c.feedbacktype='Event Feedback' THEN 1 ELSE 0 end) AS ef,"
			+ " c.geographyL4.geographyL3.name, c.latitude, c.longitude ) from ConsumerFeedback c where c.feedbacktype IS NOT NULL and c.geographyL4 IS NOT NULL and Date(c.feedbacktime) between Date(:fromDate) and Date(:toDate) and c.operatorname=:operatorname group by c.geographyL4.geographyL3.name order by co desc")
	
	@NamedQuery(name = "getTopLocationsWithType", query = "select new com.inn.foresight.module.nv.feedback.wrapper.ConsumerFeedbackWrapper("
			+ " count(c) AS co, c.geographyL4.geographyL3.name, c.latitude, c.longitude, c.feedbacktype ) from ConsumerFeedback c "
			+ "where c.feedbacktype IS NOT NULL and c.geographyL4 IS NOT NULL and Date(c.feedbacktime) between Date(:fromDate) and Date(:toDate) and c.operatorname=:operatorname group by c.geographyL4.geographyL3.name,c.feedbacktype order by co desc")
	
	@NamedQuery(name = "getLayerData", query = "select c from ConsumerFeedback c "
			+ "where c.feedbacktype IS NOT NULL and Date(c.feedbacktime) between Date(:fromDate) and Date(:toDate) and" 
			+ " c.operatorname like (:operatorname) and networktype='LTE' ")
	
    @NamedQuery(name = "getPciWiseStarRating", query = "SELECT c.starRatingVoiceRcs, c.starRatingVideoRcs, c.starRatingMessagingRcs FROM ConsumerFeedback c where c.feedbacktype IS NOT NULL and c.pci=:pci and c.cellid=:cellid order by c.feedbacktime desc")

	@NamedQuery(name = "getFeedbackDashboard", query = "SELECT c FROM ConsumerFeedback c where "
			+ "CRC32(upper(c.nvmodule)) in (:nvmoduleCode) and Date(c.feedbacktime)=Date(:date) and c.networktype='LTE' and "
			+ "c.data is not null and c.operatorname in (:operatorname)")
	


	@FilterDef(name = "GWGeographyL1Filter", parameters = {
			@ParamDef(name = "geographyId", type = "java.lang.Integer") })

	@FilterDef(name = "GWGeographyL2Filter", parameters = {
			@ParamDef(name = "geographyId", type = "java.lang.Integer") })

	@FilterDef(name = "GWGeographyL3Filter", parameters = {
			@ParamDef(name = "geographyId", type = "java.lang.Integer") })
	
	@FilterDef(name = "GWGeographyL4Filter", parameters = {
			@ParamDef(name = "geographyId", type = "java.lang.Integer") })
	
	@FilterDef(name = ConsumerFeedbackConstant.TEST_AREA_FILTER, parameters = {
			@ParamDef(name = "testArea", type = "java.lang.String") })
	
	@FilterDef(name = ConsumerFeedbackConstant.FEEDBACK_TYPE_FILTER, parameters = {
			@ParamDef(name = "feedbackType", type = "java.lang.String") })
	
	@FilterDef(name = ConsumerFeedbackConstant.BAND_2300_FILTER)
	
	@FilterDef(name = ConsumerFeedbackConstant.BAND_1800_FILTER)
	
	@FilterDef(name = ConsumerFeedbackConstant.BAND_850_FILTER)
	@FilterDef(name = ConsumerFeedbackConstant.VOICE_UNABLE_TO_MAKE_CALL)
	@FilterDef(name = ConsumerFeedbackConstant.VOICE_CALL_DROP)
	@FilterDef(name = ConsumerFeedbackConstant.VOICE_POOR_AUDIO)
	@FilterDef(name = ConsumerFeedbackConstant.VOICE_MUTE)
	@FilterDef(name = ConsumerFeedbackConstant.VOICE_ONE_WAY_AUDIO)
	@FilterDef(name = ConsumerFeedbackConstant.CVG_POOR_COVERAGE_INDOOR)
	@FilterDef(name = ConsumerFeedbackConstant.CVG_POOR_COVERAGE_OUTDOOR)
	@FilterDef(name = ConsumerFeedbackConstant.VIBER_UNABLE_TO_MAKE_CALL)
	@FilterDef(name = ConsumerFeedbackConstant.VIBER_CALL_DROP)
	@FilterDef(name = ConsumerFeedbackConstant.VIBER_POOR_AUDIO)
	@FilterDef(name = ConsumerFeedbackConstant.VIBER_MUTE)
	@FilterDef(name = ConsumerFeedbackConstant.VIBER_ONE_WAY_AUDIO)
	@FilterDef(name = ConsumerFeedbackConstant.RCS_UNABLE_TO_MAKE_CALL_TO_OTHER_RCS)
	@FilterDef(name = ConsumerFeedbackConstant.RCS_UNABLE_TO_MAKE_CALL_TO_NON_RCS)
	@FilterDef(name = ConsumerFeedbackConstant.RCS_UNABLE_TO_MAKE_GROUPCALL)
	@FilterDef(name = ConsumerFeedbackConstant.RCS_CALL_DISCONNECTED)
	@FilterDef(name = ConsumerFeedbackConstant.RCS_POOR_AUDIO)
	@FilterDef(name = ConsumerFeedbackConstant.RCS_UNABLE_TO_SEND_MSG_TO_RCS)
	@FilterDef(name = ConsumerFeedbackConstant.RCS_UNABLE_TO_SEND_MSG_TO_NON_RCS)
	@FilterDef(name = ConsumerFeedbackConstant.RCS_UNABLE_TO_MAKE_GROUP_CHAT)
	@FilterDef(name = ConsumerFeedbackConstant.RCS_UNABLE_TO_SEND_MULTIMEDIA)
	@FilterDef(name = ConsumerFeedbackConstant.WHATSAPP_UNABLE_TO_MAKE_CALL)
	@FilterDef(name = ConsumerFeedbackConstant.WHATSAPP_CALL_DROP)
	@FilterDef(name = ConsumerFeedbackConstant.WHATSAPP_POOR_AUDIO)
	@FilterDef(name = ConsumerFeedbackConstant.WHATSAPP_MUTE)
	@FilterDef(name = ConsumerFeedbackConstant.WHATSAPP_ONE_WAY_AUDIO)
	@FilterDef(name = ConsumerFeedbackConstant.SKYPE_UNABLE_TO_MAKE_CALL)
	@FilterDef(name = ConsumerFeedbackConstant.SKYPE_CALL_DROP)
	@FilterDef(name = ConsumerFeedbackConstant.SKYPE_POOR_AUDIO)
	@FilterDef(name = ConsumerFeedbackConstant.SKYPE_MUTE)
	@FilterDef(name = ConsumerFeedbackConstant.SKYPE_ONE_WAY_AUDIO)
	@FilterDef(name = ConsumerFeedbackConstant.DATA_SLOW_SPEED)
	@FilterDef(name = ConsumerFeedbackConstant.DATA_UNABLE_TO_CONNECT)
	
	@FilterDef(name = ConsumerFeedbackConstant.VOIP_STAR_RATING_FILTER, parameters = {@ParamDef(name = ConsumerFeedbackConstant.FROM_STAR_RATING, type = "java.lang.Integer") 
	,@ParamDef(name = ConsumerFeedbackConstant.TO_STAR_RATING, type = "java.lang.Integer") })
	@FilterDef(name = ConsumerFeedbackConstant.VOICE_STAR_RATING_FILTER, parameters = {@ParamDef(name = ConsumerFeedbackConstant.FROM_STAR_RATING, type = "java.lang.Integer") 
	,@ParamDef(name = ConsumerFeedbackConstant.TO_STAR_RATING, type = "java.lang.Integer") })
	@FilterDef(name = ConsumerFeedbackConstant.DATA_STAR_RATING_FILTER, parameters = {@ParamDef(name = ConsumerFeedbackConstant.FROM_STAR_RATING, type = "java.lang.Integer")
	,@ParamDef(name = ConsumerFeedbackConstant.TO_STAR_RATING, type = "java.lang.Integer") })
	@FilterDef(name = ConsumerFeedbackConstant.COVERAGE_STAR_RATING_FILTER, parameters = {@ParamDef(name =ConsumerFeedbackConstant.FROM_STAR_RATING, type = "java.lang.Integer")
	,@ParamDef(name = ConsumerFeedbackConstant.TO_STAR_RATING, type = "java.lang.Integer") })
			

@Filters(value = {

		@Filter(name = "GWGeographyL1Filter", condition = "geographyl4id_fk in (select n.geographyl4id_fk from GeographyL1 g1 inner join GeographyL2 g2 on g1.geographyl1id_pk=g2.geographyl1id_fk inner join GeographyL3 g3 on g2.geographyl2id_pk=g3.geographyl2id_fk inner join GeographyL4 g4 on g3.geographyl3id_pk=g4.geographyl3id_fk inner join ConsumerFeedback n on g4.geographyl4id_pk=n.geographyl4id_fk and g1.geographyl1id_pk=:geographyId ) "),

		@Filter(name = "GWGeographyL2Filter", condition = " geographyl4id_fk in (select n.geographyl4id_fk from GeographyL2 g2 inner join GeographyL3 g3 on g2.geographyl2id_pk=g3.geographyl2id_fk inner join GeographyL4 g4 on g3.geographyl3id_pk=g4.geographyl3id_fk inner join ConsumerFeedback n on g4.geographyl4id_pk=n.geographyl4id_fk and g2.geographyl2id_pk=:geographyId) "),

		@Filter(name = "GWGeographyL3Filter", condition = " geographyl4id_fk in (select n.geographyl4id_fk from GeographyL3 g3 inner join GeographyL4 g4 on g3.geographyl3id_pk=g4.geographyl3id_fk inner join ConsumerFeedback n on g4.geographyl4id_pk=n.geographyl4id_fk and g3.geographyl3id_pk=:geographyId) "),

		@Filter(name = "GWGeographyL4Filter", condition = "geographyl4id_fk in (:geographyId)"),
				
		@Filter(name = ConsumerFeedbackConstant.TEST_AREA_FILTER, condition = "testarea like (:testArea)"),
		
		@Filter(name = ConsumerFeedbackConstant.VOICE_UNABLE_TO_MAKE_CALL, condition = "vcunabletocall"),
		@Filter(name = ConsumerFeedbackConstant.VOICE_CALL_DROP, condition = "vccalldrop"),
		@Filter(name = ConsumerFeedbackConstant.VOICE_POOR_AUDIO, condition = "vcpooraudio"),
		@Filter(name = ConsumerFeedbackConstant.VOICE_MUTE, condition = "voicemute"),
		@Filter(name = ConsumerFeedbackConstant.VOICE_ONE_WAY_AUDIO, condition = "vconewyaudio"),
		@Filter(name = ConsumerFeedbackConstant.CVG_POOR_COVERAGE_INDOOR, condition = "cvgpoorcvgind"),
		@Filter(name = ConsumerFeedbackConstant.CVG_POOR_COVERAGE_OUTDOOR, condition = "cvgPoorCvgOut"),
		@Filter(name = ConsumerFeedbackConstant.VIBER_UNABLE_TO_MAKE_CALL, condition = "voIpViberUnableToMakeCall"),
		@Filter(name = ConsumerFeedbackConstant.VIBER_CALL_DROP, condition = "voIpViberCallDrop"),
		@Filter(name = ConsumerFeedbackConstant.VIBER_POOR_AUDIO, condition = "voIpViberPoorAudio"),
		@Filter(name = ConsumerFeedbackConstant.VIBER_MUTE, condition = "voIpViberMute"),
		@Filter(name = ConsumerFeedbackConstant.VIBER_ONE_WAY_AUDIO, condition = "voIpViberOneWayAudio"),
		@Filter(name = ConsumerFeedbackConstant.RCS_UNABLE_TO_MAKE_CALL_TO_OTHER_RCS, condition = "voIpRcsUTMCToRcs"),
		@Filter(name = ConsumerFeedbackConstant.RCS_UNABLE_TO_MAKE_CALL_TO_NON_RCS, condition = "voIpRcsUTMCToNonRcs"),
		@Filter(name = ConsumerFeedbackConstant.RCS_UNABLE_TO_MAKE_GROUPCALL, condition = "voIpRcsUTMGroupCall"),
		@Filter(name = ConsumerFeedbackConstant.RCS_CALL_DISCONNECTED, condition = "voIpRcsCallDisconnect"),
		@Filter(name = ConsumerFeedbackConstant.RCS_POOR_AUDIO, condition = "voIpRcsPoorAudio"),
		@Filter(name = ConsumerFeedbackConstant.RCS_UNABLE_TO_SEND_MSG_TO_RCS, condition = "voIpRcsUTSMsgToRcs"),
		@Filter(name = ConsumerFeedbackConstant.RCS_UNABLE_TO_SEND_MSG_TO_NON_RCS, condition = "voIpRcsUTSMsgToNonRcs"),
		@Filter(name = ConsumerFeedbackConstant.RCS_UNABLE_TO_MAKE_GROUP_CHAT, condition = "voIpRcsUTMGroupChat"),
		@Filter(name = ConsumerFeedbackConstant.RCS_UNABLE_TO_SEND_MULTIMEDIA, condition = "voIpRcsUTSMultimedia"),
		@Filter(name = ConsumerFeedbackConstant.WHATSAPP_UNABLE_TO_MAKE_CALL, condition = "voIpWhatsAppUnableToMakeCall"),
		@Filter(name = ConsumerFeedbackConstant.WHATSAPP_CALL_DROP, condition = "voIpWhatsAppCallDrop"),
		@Filter(name = ConsumerFeedbackConstant.WHATSAPP_POOR_AUDIO, condition = "voIpWhatsAppPoorAudio"),
		@Filter(name = ConsumerFeedbackConstant.WHATSAPP_MUTE, condition = "voIpWhatsAppMute"),
		@Filter(name = ConsumerFeedbackConstant.WHATSAPP_ONE_WAY_AUDIO, condition = "voIpWhatsAppOneWayAudio"),
		@Filter(name = ConsumerFeedbackConstant.SKYPE_UNABLE_TO_MAKE_CALL, condition = "voIpSkypeUnableToMakeCall"),
		@Filter(name = ConsumerFeedbackConstant.SKYPE_CALL_DROP, condition = "voIpSkypeCallDrop"),
		@Filter(name = ConsumerFeedbackConstant.SKYPE_POOR_AUDIO, condition = "voIpSkypePoorAudio"),
		@Filter(name = ConsumerFeedbackConstant.SKYPE_MUTE, condition = "voIpSkypeMute"),
		@Filter(name = ConsumerFeedbackConstant.SKYPE_ONE_WAY_AUDIO, condition = "voIpSkypeOneWayAudio"),
		@Filter(name = ConsumerFeedbackConstant.DATA_SLOW_SPEED, condition = "dataslowspeed"),
		@Filter(name = ConsumerFeedbackConstant.DATA_UNABLE_TO_CONNECT, condition = "dataunavletocon"),
		
		@Filter(name = ConsumerFeedbackConstant.FEEDBACK_TYPE_FILTER, condition = "feedbacktype like (:feedbackType)"),
		
		@Filter(name = ConsumerFeedbackConstant.BAND_2300_FILTER, condition = "band in ('30','40','2300')"),
		@Filter(name = ConsumerFeedbackConstant.BAND_1800_FILTER, condition = "band in ('1800','3')"),
		@Filter(name = ConsumerFeedbackConstant.BAND_850_FILTER, condition = "band in ('5','18','19','26','850')"),
		
		@Filter(name = ConsumerFeedbackConstant.VOICE_STAR_RATING_FILTER, condition = "starrating >= (:fromStarRating) and starrating <= (:toStarRating)"),
		@Filter(name = ConsumerFeedbackConstant.VOIP_STAR_RATING_FILTER, condition = "starRatingVoIp >= (:fromStarRating) and starRatingVoIp <= (:toStarRating)"),
		@Filter(name = ConsumerFeedbackConstant.DATA_STAR_RATING_FILTER, condition = "starratingdata >= (:fromStarRating) and starratingdata <= (:toStarRating)"),
		@Filter(name = ConsumerFeedbackConstant.COVERAGE_STAR_RATING_FILTER, condition = "starratingcvg >= (:fromStarRating) and starratingcvg <= (:toStarRating)"),
		
})

/**
 * The Class ConsumerFeedback.
 * 
 * @author innoeye
 */


@Entity
@XmlRootElement(name = "ConsumerFeedback")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Table(name = "ConsumerFeedback")
public class ConsumerFeedback implements Serializable {

	/** The id. */
	@Id
	@GeneratedValue(strategy=javax.persistence.GenerationType.IDENTITY) 
	@Column(name = "consumerfeedbackid_pk")
	private Integer id;

	/** The cluster. */
	@ManyToOne
	@JoinColumn(name = "geographyl4id_fk", nullable = false)
	private GeographyL4 geographyL4;
	
	/** The nvProfileData. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nvprofileid_fk", nullable = false)
	private NVProfileData nvProfileData;
	
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4901011392402603668L;
	
	/** The feedback time. */
	private Date feedbacktime;
	

	/** The feedback location . */
	private String feedbacklocation;
	

	/** The test area. */
	private String testarea;
	
	/** The problem type. */
	private String problemtype;
	
	/** The problem subtype. */
	private String problemsubtype;
	
	/** The notes. */
	private String notes;
	
	/** The imsi. */
	private String imsi;
	
	/** The imei. */
	private String imei;
	
	/** The make. */
	private String make;
	
	/** The model. */
	private String model;
	
	/** The device OS. */
	private String deviceos;
	
	/** The latitude. */
	private Double latitude;
	
	/** The longitude. */
	private Double longitude;
	
	/** The feed back type. */
	private String feedbacktype;
	
	/** The star rating. */
	private Integer starrating;

	/** The star rating data. */
	private Integer starratingdata;
	
	/** The star rating cvg. */
	private Integer starratingcvg;
	
	/** The vc unable to call. */
	private Boolean vcunabletocall;
	
	/** The vc call drop. */
	private Boolean vccalldrop;
	
	/** The vc poor audio. */
	private Boolean vcpooraudio;
	
	/** The voice mute. */
	private Boolean voicemute;
	
	/** The vc onewy audio. */
	private Boolean vconewyaudio;
	
	/** The data slow speed. */
	private Boolean dataslowspeed;
	
	/** The data unavle to con. */
	private Boolean dataunavletocon;
	
	/** The cvg poor cvg ind. */
	private Boolean cvgpoorcvgind;
	
	/** The cvg poor cvg out. */
	private Boolean cvgPoorCvgOut;
	
	/** The operator name. */
	private String operatorname;
	
	/** The network type. */
	private String networktype;
	
	/** The mcc. */
	private Integer mcc;

	/** The mnc. */
	private Integer mnc;
	
	/** The pci. */
	private Integer pci;
	
	/** The cell id. */
	private Integer cellid;
	
	/** The tac. */
	private Integer tac;
	
	/** The wifi name. */
	private String wifiname;       
	
	/** The rssi wifi. */
	private Integer rssiwifi;
	
	/** The snr wifi. */
	private Integer snrWifi;
	
	/** The ul rate. */
	private Double ulrate;
	
	/** The dl rate. */
	private Double dlrate;
	
	/** The version name. */
	private String versionname;     
	
	/** The nv module. */
	private String nvmodule;        
	
	/** The baseband. */
	private String baseband;        
	
	/** The build number. */
	private String buildnumber;     
	
	/** The mobile number. */
	private String mobilenumber;    

	/** The rsrp LTE. */
	/* LTE Signal Parameters */
	private Integer rsrplte; 
	
	/** The rsrq LTE. */
	private Integer rsrqlte;
	
	/** The rssi LTE. */
	private Integer rssilte;
	
	/** The sinr LTE. */
	private Double  sinrlte;

	/** The rx level 2 G. */
	/* 2G Signal Parameters */
	private Integer rxlevel2g;
	
	/** The rx quality 2 G. */
	private Integer rxquality2g;

	/** The rscp 3 G. */
	/* 3G Signal Parameters */
	private Integer rscp3g;
	
	/** The ecno 3 G. */
	private Integer ecno3g;

	/** The battery level. */
	private String batterylevel;

	/** The voltage. */
	private String voltage;

	/** The temperature. */
	private String temperature;

	/** The band. */
	private String band;
	
	/** The deviceId. */
	private String deviceid;
	
	/** The androidId. */
	private String androidid;
	
	/** The enterprise. */
	private Boolean enterprise;
	
	/** The layer 3 enabled. */
	private Boolean layer3enabled;

	/** The chipset. */
	private String chipset;
	
	/** The serialnumber. */
	private String serialnumber;
	
	/** The socmodel. */
	private String socmodel;
	
	/** The corearchitecture. */
	private String corearchitecture;
	
	/** The devicefingerprint. */
	private String devicefingerprint;
	
	/** The networksubtype. */
	private String networksubtype;
	
	
	/** The macaddress. */
	private String macaddress;
	
	/** The bssid. */
	private String bssid;
	
	/** The neighbourinfo. */
	private String neighbourinfo;
	
	/** The cgilte. */
	private Integer cgilte;
	
	/** The enodeblte. */
	private Integer enodeblte;
	
	/** The internalip. */
	private String internalip;
	
	/** The wifinetworktype. */
	private String wifinetworktype;

	
	/** The avglinkspeed. */
	private String avglinkspeed;
	
	/** The wificonnected. */
	private String wificonnected;
	
	/** The chargerstatus. */
	private String chargerstatus;
	
	/** The locationaccuracy. */
	private String locationaccuracy;
	
	/** The locationaltitude. */
	private String locationaltitude;
	
	/** The dualsim. */
	private String dualsim;
	
	/** The autodatetimeenabled. */
	private String autodatetimeenabled;
	
	/** The gpsenabled. */
	private String gpsenabled;
	
	/**
	 * The starRatingVoIp
	 */
	private Float starRatingVoIp;
	
	private Boolean voIpSkypeUnableToMakeCall;
	private Boolean voIpSkypeCallDrop;
	private Boolean voIpSkypePoorAudio;
	private Boolean voIpSkypeMute;
	private Boolean voIpSkypeOneWayAudio;
	private Boolean voIpViberUnableToMakeCall;
	private Boolean voIpViberCallDrop;
	private Boolean voIpViberPoorAudio;
	private Boolean voIpViberMute;
	private Boolean voIpViberOneWayAudio;
	private Boolean voIpWhatsAppUnableToMakeCall;
	private Boolean voIpWhatsAppCallDrop;
	private Boolean voIpWhatsAppPoorAudio;
	private Boolean voIpWhatsAppMute;
	private Boolean voIpWhatsAppOneWayAudio;
	
	/** The floorNo. */
	private String floorno;
	
	private Boolean voIpRcsUTMCToRcs;
	private Boolean voIpRcsUTMCToNonRcs;
	private Boolean voIpRcsUTMGroupCall;
	private Boolean voIpRcsCallDisconnect;
	private Boolean voIpRcsPoorAudio;
	private Boolean voIpRcsUTSMsgToRcs;
	private Boolean voIpRcsUTSMsgToNonRcs;
	private Boolean voIpRcsUTMGroupChat;
	private Boolean voIpRcsUTSMultimedia;
	
	@Column(name="highspeeddata")
	private Boolean highSpeedData;
	
	@Column(name="excellentcoverage")
	private Boolean excellentCoverage;
	
	@Column(name="excellentaudioquality")
	private Boolean excellentAudioQuality;
	
	@Column(name="poorcoverage")
	private Boolean poorCoverage;
	
	@Column(name="apnname")
	private String  apnName;
	
	/**
	 * The starRatingVoiceLine
	 */
	@Column(name="starratingvoiceline")
	private Float starRatingVoiceLine;
	
	@Column(name="starratingvideoline")
	private Float starRatingVideoLine;
	
	@Column(name="starratingmessagingline")
	private Float starRatingMessagingLine;
	
	@Column(name="starratingvoicercs")
	private Float starRatingVoiceRcs;
	
	@Column(name="starratingvideorcs")
	private Float starRatingVideoRcs;
	
	@Column(name="starratingmessagingrcs")
	private Float starRatingMessagingRcs;
	
	@Column(name="starratingvoiceskype")
	private Float starRatingVoiceSkype;
	
	@Column(name="starratingvideoskype")
	private Float starRatingVideoSkype;
	
	@Column(name="starratingmessagingskype")
	private Float starRatingMessagingSkype;
	
	@Column(name="starratingvoicewhatsapp")
	private Float starRatingVoiceWhatsapp;
	
	@Column(name="starratingvideowhatsapp")
	private Float starRatingVideoWhatsapp;
	
	@Column(name="starratingmessagingwhatsapp")
	private Float starRatingMessagingWhatsapp;
	
	@Column(name="starratingvoiceviber")
	private Float starRatingVoiceViber;
	
	@Column(name="starratingvideoviber")
	private Float starRatingVideoViber;
	
	@Column(name="starratingmessagingviber")
	private Float starRatingMessagingViber;
	
	@Column(name="starratingrakutentv")
	private Float starRatingRakutenTv;
	
	@Column(name="starratingrakutenlive")
	private Float starRatingRakutenLive;
	
	private String data;
	private String sdkversion;
	
	
	
	
	
	

	/**
	 * Gets the star rating.
	 *
	 * @return the star rating
	 */
	public Integer getStarRating() {
		return starrating;
	}

	/**
	 * Sets the star rating.
	 *
	 * @param starRating the new star rating
	 */
	public void setStarRating(Integer starRating) {
		this.starrating = starRating;
	}

	/**
	 * Gets the star rating data.
	 *
	 * @return the star rating data
	 */
	public Integer getStarRatingData() {
		return starratingdata;
	}

	/**
	 * Sets the star rating data.
	 *
	 * @param starRatingData the new star rating data
	 */
	public void setStarRatingData(Integer starRatingData) {
		this.starratingdata = starRatingData;
	}

	/**
	 * Gets the star rating cvg.
	 *
	 * @return the star rating cvg
	 */
	public Integer getStarRatingCvg() {
		return starratingcvg;
	}

	/**
	 * Sets the star rating cvg.
	 *
	 * @param starRatingCvg the new star rating cvg
	 */
	public void setStarRatingCvg(Integer starRatingCvg) {
		this.starratingcvg = starRatingCvg;
	}

	/**
	 * Gets the vc unable to call.
	 *
	 * @return the vc unable to call
	 */
	public Boolean getVcUnableToCall() {
		return vcunabletocall;
	}

	/**
	 * Sets the vc unable to call.
	 *
	 * @param vcUnableToCall the new vc unable to call
	 */
	public void setVcUnableToCall(Boolean vcUnableToCall) {
		this.vcunabletocall = vcUnableToCall;
	}

	/**
	 * Gets the vc call drop.
	 *
	 * @return the vc call drop
	 */
	public Boolean getVcCallDrop() {
		return vccalldrop;
	}

	/**
	 * Sets the vc call drop.
	 *
	 * @param vcCallDrop the new vc call drop
	 */
	public void setVcCallDrop(Boolean vcCallDrop) {
		this.vccalldrop = vcCallDrop;
	}

	/**
	 * Gets the vc poor audio.
	 *
	 * @return the vc poor audio
	 */
	public Boolean getVcPoorAudio() {
		return vcpooraudio;
	}

	/**
	 * Sets the vc poor audio.
	 *
	 * @param vcPoorAudio the new vc poor audio
	 */
	public void setVcPoorAudio(Boolean vcPoorAudio) {
		this.vcpooraudio = vcPoorAudio;
	}

	/**
	 * Gets the voice mute.
	 *
	 * @return the voice mute
	 */
	public Boolean getVoiceMute() {
		return voicemute;
	}

	/**
	 * Sets the voice mute.
	 *
	 * @param voiceMute the new voice mute
	 */
	public void setVoiceMute(Boolean voiceMute) {
		this.voicemute = voiceMute;
	}

	/**
	 * Gets the vc onewy audio.
	 *
	 * @return the vc onewy audio
	 */
	public Boolean getVcOnewyAudio() {
		return vconewyaudio;
	}

	/**
	 * Sets the vc onewy audio.
	 *
	 * @param vcOnewyAudio the new vc onewy audio
	 */
	public void setVcOnewyAudio(Boolean vcOnewyAudio) {
		this.vconewyaudio = vcOnewyAudio;
	}

	/**
	 * Gets the data slow speed.
	 *
	 * @return the data slow speed
	 */
	public Boolean getDataSlowSpeed() {
		return dataslowspeed;
	}

	/**
	 * Sets the data slow speed.
	 *
	 * @param dataSlowSpeed the new data slow speed
	 */
	public void setDataSlowSpeed(Boolean dataSlowSpeed) {
		this.dataslowspeed = dataSlowSpeed;
	}

	/**
	 * Gets the data unavle to con.
	 *
	 * @return the data unavle to con
	 */
	public Boolean getDataUnavleToCon() {
		return dataunavletocon;
	}

	/**
	 * Sets the data unavle to con.
	 *
	 * @param dataUnavleToCon the new data unavle to con
	 */
	public void setDataUnavleToCon(Boolean dataUnavleToCon) {
		this.dataunavletocon = dataUnavleToCon;
	}

	/**
	 * Gets the cvg poor cvg ind.
	 *
	 * @return the cvg poor cvg ind
	 */
	public Boolean getCvgPoorCvgInd() {
		return cvgpoorcvgind;
	}

	/**
	 * Sets the cvg poor cvg ind.
	 *
	 * @param cvgPoorCvgInd the new cvg poor cvg ind
	 */
	public void setCvgPoorCvgInd(Boolean cvgPoorCvgInd) {
		this.cvgpoorcvgind = cvgPoorCvgInd;
	}

	/**
	 * Gets the cvg poor cvg out.
	 *
	 * @return the cvg poor cvg out
	 */
	public Boolean getCvgPoorCvgOut() {
		return cvgPoorCvgOut;
	}

	/**
	 * Sets the cvg poor cvg out.
	 *
	 * @param cvgPoorCvgOut the new cvg poor cvg out
	 */
	public void setCvgPoorCvgOut(Boolean cvgPoorCvgOut) {
		this.cvgPoorCvgOut = cvgPoorCvgOut;
	}


	/** Instantiates a new consumer feedback. */
	public ConsumerFeedback() {
	}

	/**
	 * Instantiates a new consumer feedback.
	 *
	 * @param id the id
	 */
	public ConsumerFeedback(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the operator name.
	 *
	 * @return the operator name
	 */
	public String getOperatorName() {
		return operatorname;
	}

	/**
	 * Sets the operator name.
	 *
	 * @param operatorName the new operator name
	 */
	public void setOperatorName(String operatorName) {
		this.operatorname = operatorName;
	}

	

	/**
	 * Gets the mcc.
	 *
	 * @return the mcc
	 */
	public Integer getMcc() {
		return mcc;
	}

	/**
	 * Sets the mcc.
	 *
	 * @param mcc the new mcc
	 */
	public void setMcc(Integer mcc) {
		this.mcc = mcc;
	}

	/**
	 * Gets the mnc.
	 *
	 * @return the mnc
	 */
	public Integer getMnc() {
		return mnc;
	}

	/**
	 * Sets the mnc.
	 *
	 * @param mnc the new mnc
	 */
	public void setMnc(Integer mnc) {
		this.mnc = mnc;
	}


	/**
	 * Gets the test area.
	 *
	 * @return the test area
	 */
	public String getTestArea() {
		return testarea;
	}

	/**
	 * Sets the test area.
	 *
	 * @param testArea the new test area
	 */
	public void setTestArea(String testArea) {
		this.testarea = testArea;
	}

	/**
	 * Gets the problem type.
	 *
	 * @return the problem type
	 */
	public String getProblemType() {
		return problemtype;
	}

	/**
	 * Sets the problem type.
	 *
	 * @param problemType the new problem type
	 */
	public void setProblemType(String problemType) {
		this.problemtype = problemType;
	}

	/**
	 * Gets the problem subtype.
	 *
	 * @return the problem subtype
	 */
	public String getProblemSubtype() {
		return problemsubtype;
	}

	/**
	 * Sets the problem subtype.
	 *
	 * @param problemSubtype the new problem subtype
	 */
	public void setProblemSubtype(String problemSubtype) {
		this.problemsubtype = problemSubtype;
	}

	/**
	 * Gets the notes.
	 *
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * Sets the notes.
	 *
	 * @param notes the new notes
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * Gets the imsi.
	 *
	 * @return the imsi
	 */
	public String getImsi() {
		return imsi;
	}

	/**
	 * Sets the imsi.
	 *
	 * @param imsi the new imsi
	 */
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	/**
	 * Gets the imei.
	 *
	 * @return the imei
	 */
	public String getImei() {
		return imei;
	}

	/**
	 * Sets the imei.
	 *
	 * @param imei the new imei
	 */
	public void setImei(String imei) {
		this.imei = imei;
	}

	/**
	 * Gets the make.
	 *
	 * @return the make
	 */
	public String getMake() {
		return make;
	}

	/**
	 * Sets the make.
	 *
	 * @param make the new make
	 */
	public void setMake(String make) {
		this.make = make;
	}

	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Sets the model.
	 *
	 * @param model the new model
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * Gets the device OS.
	 *
	 * @return the device OS
	 */
	public String getDeviceOS() {
		return deviceos;
	}

	/**
	 * Sets the device OS.
	 *
	 * @param deviceOS the new device OS
	 */
	public void setDeviceOS(String deviceOS) {
		this.deviceos = deviceOS;
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
	 * @param id the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the latitude.
	 *
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * Sets the latitude.
	 *
	 * @param latitude the new latitude
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * Gets the longitude.
	 *
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * Sets the longitude.
	 *
	 * @param longitude the new longitude
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * Gets the cluster.
	 *
	 * @return the cluster
	 */
	public GeographyL4 getGeographyL4() {
		return geographyL4;
	}

	/**
	 * Sets the cluster.
	 *
	 * @param geographyL4 the new geography L 4
	 */
	public void setGeographyL4(GeographyL4 geographyL4) {
		this.geographyL4 = geographyL4;
	}
	
	/**
	 * Gets the feed back type.
	 *
	 * @return the feed back type
	 */
	public String getFeedBackType() {
		return feedbacktype;
	}

	/**
	 * Sets the feed back type.
	 *
	 * @param feedBackType the new feed back type
	 */
	public void setFeedBackType(String feedBackType) {
		this.feedbacktype = feedBackType;
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
	 * Gets the cell id.
	 *
	 * @return the cell id
	 */
	public Integer getCellId() {
		return cellid;
	}

	/**
	 * Sets the cell id.
	 *
	 * @param cellId the new cell id
	 */
	public void setCellId(Integer cellId) {
		this.cellid = cellId;
	}

	/**
	 * Gets the tac.
	 *
	 * @return the tac
	 */
	public Integer getTac() {
		return tac;
	}

	/**
	 * Sets the tac.
	 *
	 * @param tac the new tac
	 */
	public void setTac(Integer tac) {
		this.tac = tac;
	}



	/**
	 * Gets the wifi name.
	 *
	 * @return the wifi name
	 */
	public String getWifiName() {
		return wifiname;
	}

	/**
	 * Sets the wifi name.
	 *
	 * @param wifiName the new wifi name
	 */
	public void setWifiName(String wifiName) {
		this.wifiname = wifiName;
	}

	/**
	 * Gets the rssi wifi.
	 *
	 * @return the rssi wifi
	 */
	public Integer getRssiWifi() {
		return rssiwifi;
	}

	/**
	 * Sets the rssi wifi.
	 *
	 * @param rssiWifi the new rssi wifi
	 */
	public void setRssiWifi(Integer rssiWifi) {
		this.rssiwifi = rssiWifi;
	}

	/**
	 * Gets the snr wifi.
	 *
	 * @return the snr wifi
	 */
	public Integer getSnrWifi() {
		return snrWifi;
	}

	/**
	 * Sets the snr wifi.
	 *
	 * @param snrWifi the new snr wifi
	 */
	public void setSnrWifi(Integer snrWifi) {
		this.snrWifi = snrWifi;
	}

	/**
	 * Gets the ul rate.
	 *
	 * @return the ul rate
	 */
	public Double getUlRate() {
		return ulrate;
	}

	/**
	 * Sets the ul rate.
	 *
	 * @param ulRate the new ul rate
	 */
	public void setUlRate(Double ulRate) {
		this.ulrate = ulRate;
	}

	/**
	 * Gets the dl rate.
	 *
	 * @return the dl rate
	 */
	public Double getDlRate() {
		return dlrate;
	}

	/**
	 * Sets the dl rate.
	 *
	 * @param dlRate the new dl rate
	 */
	public void setDlRate(Double dlRate) {
		this.dlrate = dlRate;
	}

	/**
	 * Gets the version name.
	 *
	 * @return the version name
	 */
	public String getVersionName() {
		return versionname;
	}

	/**
	 * Sets the version name.
	 *
	 * @param versionName the new version name
	 */
	public void setVersionName(String versionName) {
		this.versionname = versionName;
	}

	/**
	 * Gets the nv module.
	 *
	 * @return the nv module
	 */
	public String getNvModule() {
		return nvmodule;
	}

	/**
	 * Sets the nv module.
	 *
	 * @param nvModule the new nv module
	 */
	public void setNvModule(String nvModule) {
		this.nvmodule = nvModule;
	}

	/**
	 * Gets the baseband.
	 *
	 * @return the baseband
	 */
	public String getBaseband() {
		return baseband;
	}

	/**
	 * Sets the baseband.
	 *
	 * @param baseband the new baseband
	 */
	public void setBaseband(String baseband) {
		this.baseband = baseband;
	}

	/**
	 * Gets the builds the number.
	 *
	 * @return the builds the number
	 */
	public String getBuildNumber() {
		return buildnumber;
	}

	/**
	 * Sets the builds the number.
	 *
	 * @param buildNumber the new builds the number
	 */
	public void setBuildNumber(String buildNumber) {
		this.buildnumber = buildNumber;
	}

	/**
	 * Gets the mobile number.
	 *
	 * @return the mobile number
	 */
	public String getMobileNumber() {
		return mobilenumber;
	}

	/**
	 * Sets the mobile number.
	 *
	 * @param mobileNumber the new mobile number
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobilenumber = mobileNumber;
	}

	/**
	 * Gets the rsrp LTE.
	 *
	 * @return the rsrp LTE
	 */
	public Integer getRsrpLTE() {
		return rsrplte;
	}

	/**
	 * Sets the rsrp LTE.
	 *
	 * @param rsrpLTE the new rsrp LTE
	 */
	public void setRsrpLTE(Integer rsrpLTE) {
		this.rsrplte = rsrpLTE;
	}

	/**
	 * Gets the rsrq LTE.
	 *
	 * @return the rsrq LTE
	 */
	public Integer getRsrqLTE() {
		return rsrqlte;
	}

	/**
	 * Sets the rsrq LTE.
	 *
	 * @param rsrqLTE the new rsrq LTE
	 */
	public void setRsrqLTE(Integer rsrqLTE) {
		this.rsrqlte = rsrqLTE;
	}

	/**
	 * Gets the rssi LTE.
	 *
	 * @return the rssi LTE
	 */
	public Integer getRssiLTE() {
		return rssilte;
	}

	/**
	 * Sets the rssi LTE.
	 *
	 * @param rssiLTE the new rssi LTE
	 */
	public void setRssiLTE(Integer rssiLTE) {
		this.rssilte = rssiLTE;
	}

	/**
	 * Gets the sinr LTE.
	 *
	 * @return the sinr LTE
	 */
	public Double getSinrLTE() {
		return sinrlte;
	}

	/**
	 * Sets the sinr LTE.
	 *
	 * @param sinrLTE the new sinr LTE
	 */
	public void setSinrLTE(Double sinrLTE) {
		this.sinrlte = sinrLTE;
	}

	/**
	 * Gets the rx level 2 G.
	 *
	 * @return the rx level 2 G
	 */
	public Integer getRxLevel2G() {
		return rxlevel2g;
	}

	/**
	 * Sets the rx level 2 G.
	 *
	 * @param rxLevel2G the new rx level 2 G
	 */
	public void setRxLevel2G(Integer rxLevel2G) {
		this.rxlevel2g = rxLevel2G;
	}

	/**
	 * Gets the rx quality 2 G.
	 *
	 * @return the rx quality 2 G
	 */
	public Integer getRxQuality2G() {
		return rxquality2g;
	}

	/**
	 * Sets the rx quality 2 G.
	 *
	 * @param rxQuality2G the new rx quality 2 G
	 */
	public void setRxQuality2G(Integer rxQuality2G) {
		this.rxquality2g = rxQuality2G;
	}

	/**
	 * Gets the rscp 3 G.
	 *
	 * @return the rscp 3 G
	 */
	public Integer getRscp3G() {
		return rscp3g;
	}

	/**
	 * Sets the rscp 3 G.
	 *
	 * @param rscp3g the new rscp 3 G
	 */
	public void setRscp3G(Integer rscp3g) {
		this.rscp3g = rscp3g;
	}

	/**
	 * Gets the ecno 3 G.
	 *
	 * @return the ecno 3 G
	 */
	public Integer getEcno3G() {
		return ecno3g;
	}

	/**
	 * Sets the ecno 3 G.
	 *
	 * @param ecno3g the new ecno 3 G
	 */
	public void setEcno3G(Integer ecno3g) {
		this.ecno3g = ecno3g;
	}

	/**
	 * Gets the battery level.
	 *
	 * @return the battery level
	 */
	public String getBatteryLevel() {
		return batterylevel;
	}

	/**
	 * Sets the battery level.
	 *
	 * @param batteryLevel the new battery level
	 */
	public void setBatteryLevel(String batteryLevel) {
		this.batterylevel = batteryLevel;
	}

	/**
	 * Gets the voltage.
	 *
	 * @return the voltage
	 */
	public String getVoltage() {
		return voltage;
	}

	
	/**
	 * Sets the voltage.
	 *
	 * @param voltage the new voltage
	 */
	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}

	/**
	 * Gets the temperature.
	 *
	 * @return the temperature
	 */
	public String getTemperature() {
		return temperature;
	}

	/**
	 * Sets the temperature.
	 *
	 * @param temperature the new temperature
	 */
	public void setTemperature(String temperature) {
		this.temperature = temperature;
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
	 * @param band the new band
	 */
	public void setBand(String band) {
		this.band = band;
	}
	
	/**
	 *  Get feedback time.
	 *
	 * @return feedbacktime
	 */
	public Date getFeedbacktime() {
		return feedbacktime;
	}

	/**
	 * Set feedback time.
	 *
	 * @param feedbacktime the new feedbacktime
	 */
	public void setFeedbacktime(Date feedbacktime) {
		this.feedbacktime = feedbacktime;
	}
	
	/**
	 * Get feedback location.
	 *
	 * @return feedback location
	 */
	public String getFeedbacklocation() {
		return feedbacklocation;
	}
	
	/**
	 *  Set feedback location.
	 *
	 * @param feedbacklocation the new feedbacklocation
	 */
	public void setFeedbacklocation(String feedbacklocation) {
		this.feedbacklocation = feedbacklocation;
	}
	
	/**
	 * Gets the device id.
	 *
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceid;
	}

	/**
	 * Sets the device id.
	 *
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceid = deviceId;
	}

	/**
	 * Gets the android id.
	 *
	 * @return the androidId
	 */
	public String getAndroidId() {
		return androidid;
	}

	/**
	 * Sets the android id.
	 *
	 * @param androidId the androidId to set
	 */
	public void setAndroidId(String androidId) {
		this.androidid = androidId;
	}
	

	
	
	/**
	 * Gets the networktype.
	 *
	 * @return the networktype
	 */
	public String getNetworktype() {
		return networktype;
	}

	/**
	 * Sets the networktype.
	 *
	 * @param networktype the networktype to set
	 */
	public void setNetworktype(String networktype) {
		this.networktype = networktype;
	}

	




	


	/**
	 * Gets the enterprise.
	 *
	 * @return the enterprise
	 */
	public Boolean getEnterprise() {
		return enterprise;
	}

	/**
	 * Sets the enterprise.
	 *
	 * @param enterprise the enterprise to set
	 */
	public void setEnterprise(Boolean enterprise) {
		this.enterprise = enterprise;
	}

	/**
	 * Gets the layer 3 enabled.
	 *
	 * @return the layer3enabled
	 */
	public Boolean getLayer3enabled() {
		return layer3enabled;
	}

	/**
	 * Sets the layer 3 enabled.
	 *
	 * @param layer3enabled the layer3enabled to set
	 */
	public void setLayer3enabled(Boolean layer3enabled) {
		this.layer3enabled = layer3enabled;
	}

	

	/**
	 * Gets the chipset.
	 *
	 * @return the chipset
	 */
	public String getChipset() {
		return chipset;
	}

	/**
	 * Sets the chipset.
	 *
	 * @param chipset the chipset to set
	 */
	public void setChipset(String chipset) {
		this.chipset = chipset;
	}

	/**
	 * Gets the serialnumber.
	 *
	 * @return the serialnumber
	 */
	public String getSerialnumber() {
		return serialnumber;
	}

	/**
	 * Sets the serialnumber.
	 *
	 * @param serialnumber the serialnumber to set
	 */
	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}

	/**
	 * Gets the socmodel.
	 *
	 * @return the socmodel
	 */
	public String getSocmodel() {
		return socmodel;
	}

	/**
	 * Sets the socmodel.
	 *
	 * @param socmodel the socmodel to set
	 */
	public void setSocmodel(String socmodel) {
		this.socmodel = socmodel;
	}

	/**
	 * Gets the corearchitecture.
	 *
	 * @return the corearchitecture
	 */
	public String getCorearchitecture() {
		return corearchitecture;
	}

	/**
	 * Sets the corearchitecture.
	 *
	 * @param corearchitecture the corearchitecture to set
	 */
	public void setCorearchitecture(String corearchitecture) {
		this.corearchitecture = corearchitecture;
	}

	/**
	 * Gets the devicefingerprint.
	 *
	 * @return the devicefingerprint
	 */
	public String getDevicefingerprint() {
		return devicefingerprint;
	}

	/**
	 * Sets the devicefingerprint.
	 *
	 * @param devicefingerprint the devicefingerprint to set
	 */
	public void setDevicefingerprint(String devicefingerprint) {
		this.devicefingerprint = devicefingerprint;
	}



	/**
	 * Gets the networksubtype.
	 *
	 * @return the networksubtype
	 */
	public String getNetworksubtype() {
		return networksubtype;
	}

	/**
	 * Sets the networksubtype.
	 *
	 * @param networksubtype the networksubtype to set
	 */
	public void setNetworksubtype(String networksubtype) {
		this.networksubtype = networksubtype;
	}


	/**
	 * Gets the macaddress.
	 *
	 * @return the macaddress
	 */
	public String getMacaddress() {
		return macaddress;
	}

	/**
	 * Sets the macaddress.
	 *
	 * @param macaddress the macaddress to set
	 */
	public void setMacaddress(String macaddress) {
		this.macaddress = macaddress;
	}

	/**
	 * Gets the bssid.
	 *
	 * @return the bssid
	 */
	public String getBssid() {
		return bssid;
	}

	/**
	 * Sets the bssid.
	 *
	 * @param bssid the bssid to set
	 */
	public void setBssid(String bssid) {
		this.bssid = bssid;
	}

	/**
	 * Gets the neighbourinfo.
	 *
	 * @return the neighbourinfo
	 */
	public String getNeighbourinfo() {
		return neighbourinfo;
	}

	/**
	 * Sets the neighbourinfo.
	 *
	 * @param neighbourinfo the neighbourinfo to set
	 */
	public void setNeighbourinfo(String neighbourinfo) {
		this.neighbourinfo = neighbourinfo;
	}

	/**
	 * Gets the cgilte.
	 *
	 * @return the cgilte
	 */
	public Integer getCgilte() {
		return cgilte;
	}

	/**
	 * Sets the cgilte.
	 *
	 * @param cgilte the cgilte to set
	 */
	public void setCgilte(Integer cgilte) {
		this.cgilte = cgilte;
	}

	/**
	 * Gets the e nodeblte.
	 *
	 * @return the eNodeblte
	 */
	public Integer geteNodeblte() {
		return enodeblte;
	}

	/**
	 * Sets the e nodeblte.
	 *
	 * @param eNodeblte the eNodeblte to set
	 */
	public void seteNodeblte(Integer eNodeblte) {
		this.enodeblte = eNodeblte;
	}

	/**
	 * Gets the internalip.
	 *
	 * @return the internalip
	 */
	public String getInternalip() {
		return internalip;
	}

	/**
	 * Sets the internalip.
	 *
	 * @param internalip the internalip to set
	 */
	public void setInternalip(String internalip) {
		this.internalip = internalip;
	}


	/**
	 * Gets the serialversionuid.
	 *
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * Gets the wifinetworktype.
	 *
	 * @return the wifinetworktype
	 */
	public String getWifinetworktype() {
		return wifinetworktype;
	}

	/**
	 * Sets the wifinetworktype.
	 *
	 * @param wifinetworktype the new wifinetworktype
	 */
	public void setWifinetworktype(String wifinetworktype) {
		this.wifinetworktype = wifinetworktype;
	}

	

	/**
	 * Gets the avglinkspeed.
	 *
	 * @return the avglinkspeed
	 */
	public String getAvglinkspeed() {
		return avglinkspeed;
	}

	/**
	 * Sets the avglinkspeed.
	 *
	 * @param avglinkspeed the avglinkspeed to set
	 */
	public void setAvglinkspeed(String avglinkspeed) {
		this.avglinkspeed = avglinkspeed;
	}

	/**
	 * Gets the wificonnected.
	 *
	 * @return the wificonnected
	 */
	public String getWificonnected() {
		return wificonnected;
	}

	/**
	 * Sets the wificonnected.
	 *
	 * @param wificonnected the wificonnected to set
	 */
	public void setWificonnected(String wificonnected) {
		this.wificonnected = wificonnected;
	}

	/**
	 * Gets the chargerstatus.
	 *
	 * @return the chargerstatus
	 */
	public String getChargerstatus() {
		return chargerstatus;
	}

	/**
	 * Sets the chargerstatus.
	 *
	 * @param chargerstatus the chargerstatus to set
	 */
	public void setChargerstatus(String chargerstatus) {
		this.chargerstatus = chargerstatus;
	}

	/**
	 * Gets the locationaccuracy.
	 *
	 * @return the locationaccuracy
	 */
	public String getLocationaccuracy() {
		return locationaccuracy;
	}

	/**
	 * Sets the locationaccuracy.
	 *
	 * @param locationaccuracy the locationaccuracy to set
	 */
	public void setLocationaccuracy(String locationaccuracy) {
		this.locationaccuracy = locationaccuracy;
	}

	/**
	 * Gets the locationaltitude.
	 *
	 * @return the locationaltitude
	 */
	public String getLocationaltitude() {
		return locationaltitude;
	}

	/**
	 * Sets the locationaltitude.
	 *
	 * @param locationaltitude the locationaltitude to set
	 */
	public void setLocationaltitude(String locationaltitude) {
		this.locationaltitude = locationaltitude;
	}

	/**
	 * Gets the dualsim.
	 *
	 * @return the dualsim
	 */
	public String getDualsim() {
		return dualsim;
	}

	/**
	 * Sets the dualsim.
	 *
	 * @param dualsim the dualsim to set
	 */
	public void setDualsim(String dualsim) {
		this.dualsim = dualsim;
	}

	/**
	 * Gets the autodatetimeenabled.
	 *
	 * @return the autodatetimeenabled
	 */
	public String getAutodatetimeenabled() {
		return autodatetimeenabled;
	}

	/**
	 * Sets the autodatetimeenabled.
	 *
	 * @param autodatetimeenabled the autodatetimeenabled to set
	 */
	public void setAutodatetimeenabled(String autodatetimeenabled) {
		this.autodatetimeenabled = autodatetimeenabled;
	}

	/**
	 * Gets the gpsenabled.
	 *
	 * @return the gpsenabled
	 */
	public String getGpsenabled() {
		return gpsenabled;
	}

	/**
	 * Sets the gpsenabled.
	 *
	 * @param gpsenabled the gpsenabled to set
	 */
	public void setGpsenabled(String gpsenabled) {
		this.gpsenabled = gpsenabled;
	}

	/**
	 * @return the nvProfileData
	 */
	public NVProfileData getNvProfileData() {
		return nvProfileData;
	}

	/**
	 * @param nvProfileData the nvProfileData to set
	 */
	public void setNvProfileData(NVProfileData nvProfileData) {
		this.nvProfileData = nvProfileData;
	}

	public Float getStarRatingVoIp() {
		return starRatingVoIp;
	}

	public void setStarRatingVoIp(Float starRatingVoIp) {
		this.starRatingVoIp = starRatingVoIp;
	}

	public Boolean isVoIpSkypeUnableToMakeCall() {
		return voIpSkypeUnableToMakeCall;
	}

	public void setVoIpSkypeUnableToMakeCall(Boolean voIpSkypeUnableToMakeCall) {
		this.voIpSkypeUnableToMakeCall = voIpSkypeUnableToMakeCall;
	}

	public Boolean isVoIpSkypeCallDrop() {
		return voIpSkypeCallDrop;
	}

	public void setVoIpSkypeCallDrop(Boolean voIpSkypeCallDrop) {
		this.voIpSkypeCallDrop = voIpSkypeCallDrop;
	}

	public Boolean isVoIpSkypePoorAudio() {
		return voIpSkypePoorAudio;
	}

	public void setVoIpSkypePoorAudio(Boolean voIpSkypePoorAudio) {
		this.voIpSkypePoorAudio = voIpSkypePoorAudio;
	}

	public Boolean isVoIpSkypeMute() {
		return voIpSkypeMute;
	}

	public void setVoIpSkypeMute(Boolean voIpSkypeMute) {
		this.voIpSkypeMute = voIpSkypeMute;
	}

	public Boolean isVoIpSkypeOneWayAudio() {
		return voIpSkypeOneWayAudio;
	}

	public void setVoIpSkypeOneWayAudio(Boolean voIpSkypeOneWayAudio) {
		this.voIpSkypeOneWayAudio = voIpSkypeOneWayAudio;
	}

	public Boolean isVoIpViberUnableToMakeCall() {
		return voIpViberUnableToMakeCall;
	}

	public void setVoIpViberUnableToMakeCall(Boolean voIpViberUnableToMakeCall) {
		this.voIpViberUnableToMakeCall = voIpViberUnableToMakeCall;
	}

	public Boolean isVoIpViberCallDrop() {
		return voIpViberCallDrop;
	}

	public void setVoIpViberCallDrop(Boolean voIpViberCallDrop) {
		this.voIpViberCallDrop = voIpViberCallDrop;
	}

	public Boolean isVoIpViberPoorAudio() {
		return voIpViberPoorAudio;
	}

	public void setVoIpViberPoorAudio(Boolean voIpViberPoorAudio) {
		this.voIpViberPoorAudio = voIpViberPoorAudio;
	}

	public Boolean isVoIpViberMute() {
		return voIpViberMute;
	}

	public void setVoIpViberMute(Boolean voIpViberMute) {
		this.voIpViberMute = voIpViberMute;
	}

	public Boolean isVoIpViberOneWayAudio() {
		return voIpViberOneWayAudio;
	}

	public void setVoIpViberOneWayAudio(Boolean voIpViberOneWayAudio) {
		this.voIpViberOneWayAudio = voIpViberOneWayAudio;
	}

	public Boolean isVoIpWhatsAppUnableToMakeCall() {
		return voIpWhatsAppUnableToMakeCall;
	}

	public void setVoIpWhatsAppUnableToMakeCall(Boolean voIpWhatsAppUnableToMakeCall) {
		this.voIpWhatsAppUnableToMakeCall = voIpWhatsAppUnableToMakeCall;
	}

	public Boolean isVoIpWhatsAppCallDrop() {
		return voIpWhatsAppCallDrop;
	}

	public void setVoIpWhatsAppCallDrop(Boolean voIpWhatsAppCallDrop) {
		this.voIpWhatsAppCallDrop = voIpWhatsAppCallDrop;
	}

	public Boolean isVoIpWhatsAppPoorAudio() {
		return voIpWhatsAppPoorAudio;
	}

	public void setVoIpWhatsAppPoorAudio(Boolean voIpWhatsAppPoorAudio) {
		this.voIpWhatsAppPoorAudio = voIpWhatsAppPoorAudio;
	}

	public Boolean isVoIpWhatsAppMute() {
		return voIpWhatsAppMute;
	}

	public void setVoIpWhatsAppMute(Boolean voIpWhatsAppMute) {
		this.voIpWhatsAppMute = voIpWhatsAppMute;
	}

	public Boolean isVoIpWhatsAppOneWayAudio() {
		return voIpWhatsAppOneWayAudio;
	}

	public void setVoIpWhatsAppOneWayAudio(Boolean voIpWhatsAppOneWayAudio) {
		this.voIpWhatsAppOneWayAudio = voIpWhatsAppOneWayAudio;
	}

	public String getFloorNo() {
		return floorno;
	}

	public void setFloorNo(String floorNo) {
		this.floorno = floorNo;
	}
	
	public Boolean getVoIpRcsUTMCToRcs() {
		return voIpRcsUTMCToRcs;
	}

	public void setVoIpRcsUTMCToRcs(Boolean voIpRcsUTMCToRcs) {
		this.voIpRcsUTMCToRcs = voIpRcsUTMCToRcs;
	}

	public Boolean getVoIpRcsUTMCToNonRcs() {
		return voIpRcsUTMCToNonRcs;
	}

	public void setVoIpRcsUTMCToNonRcs(Boolean voIpRcsUTMCToNonRcs) {
		this.voIpRcsUTMCToNonRcs = voIpRcsUTMCToNonRcs;
	}

	public Boolean getVoIpRcsUTMGroupCall() {
		return voIpRcsUTMGroupCall;
	}

	public void setVoIpRcsUTMGroupCall(Boolean voIpRcsUTMGroupCall) {
		this.voIpRcsUTMGroupCall = voIpRcsUTMGroupCall;
	}

	public Boolean getVoIpRcsCallDisconnect() {
		return voIpRcsCallDisconnect;
	}

	public void setVoIpRcsCallDisconnect(Boolean voIpRcsCallDisconnect) {
		this.voIpRcsCallDisconnect = voIpRcsCallDisconnect;
	}

	public Boolean getVoIpRcsPoorAudio() {
		return voIpRcsPoorAudio;
	}

	public void setVoIpRcsPoorAudio(Boolean voIpRcsPoorAudio) {
		this.voIpRcsPoorAudio = voIpRcsPoorAudio;
	}

	public Boolean getVoIpRcsUTSMsgToRcs() {
		return voIpRcsUTSMsgToRcs;
	}

	public void setVoIpRcsUTSMsgToRcs(Boolean voIpRcsUTSMsgToRcs) {
		this.voIpRcsUTSMsgToRcs = voIpRcsUTSMsgToRcs;
	}

	public Boolean getVoIpRcsUTSMsgToNonRcs() {
		return voIpRcsUTSMsgToNonRcs;
	}

	public void setVoIpRcsUTSMsgToNonRcs(Boolean voIpRcsUTSMsgToNonRcs) {
		this.voIpRcsUTSMsgToNonRcs = voIpRcsUTSMsgToNonRcs;
	}

	public Boolean getVoIpRcsUTMGroupChat() {
		return voIpRcsUTMGroupChat;
	}

	public void setVoIpRcsUTMGroupChat(Boolean voIpRcsUTMGroupChat) {
		this.voIpRcsUTMGroupChat = voIpRcsUTMGroupChat;
	}

	public Boolean getVoIpRcsUTSMultimedia() {
		return voIpRcsUTSMultimedia;
	}

	public void setVoIpRcsUTSMultimedia(Boolean voIpRcsUTSMultimedia) {
		this.voIpRcsUTSMultimedia = voIpRcsUTSMultimedia;
	}
	
	
	

	
	
	

	/**
	 * @return the highSpeedData
	 */
	public Boolean getHighSpeedData() {
		return highSpeedData;
	}

	/**
	 * @param highSpeedData the highSpeedData to set
	 */
	public void setHighSpeedData(Boolean highSpeedData) {
		this.highSpeedData = highSpeedData;
	}

	/**
	 * @return the excellentCoverage
	 */
	public Boolean getExcellentCoverage() {
		return excellentCoverage;
	}

	/**
	 * @param excellentCoverage the excellentCoverage to set
	 */
	public void setExcellentCoverage(Boolean excellentCoverage) {
		this.excellentCoverage = excellentCoverage;
	}

	/**
	 * @return the excellentAudioQuality
	 */
	public Boolean getExcellentAudioQuality() {
		return excellentAudioQuality;
	}

	/**
	 * @param excellentAudioQuality the excellentAudioQuality to set
	 */
	public void setExcellentAudioQuality(Boolean excellentAudioQuality) {
		this.excellentAudioQuality = excellentAudioQuality;
	}

	/**
	 * @return the poorCoverage
	 */
	public Boolean getPoorCoverage() {
		return poorCoverage;
	}

	/**
	 * @param poorCoverage the poorCoverage to set
	 */
	public void setPoorCoverage(Boolean poorCoverage) {
		this.poorCoverage = poorCoverage;
	}

	/**
	 * @return the apnName
	 */
	public String getApnName() {
		return apnName;
	}

	/**
	 * @param apnName the apnName to set
	 */
	public void setApnName(String apnName) {
		this.apnName = apnName;
	}
	
	

	/**
	 * @return the starRatingVoiceLine
	 */
	public Float getStarRatingVoiceLine() {
		return starRatingVoiceLine;
	}

	/**
	 * @param starRatingVoiceLine the starRatingVoiceLine to set
	 */
	public void setStarRatingVoiceLine(Float starRatingVoiceLine) {
		this.starRatingVoiceLine = starRatingVoiceLine;
	}

	/**
	 * @return the starRatingVideoLine
	 */
	public Float getStarRatingVideoLine() {
		return starRatingVideoLine;
	}

	/**
	 * @param starRatingVideoLine the starRatingVideoLine to set
	 */
	public void setStarRatingVideoLine(Float starRatingVideoLine) {
		this.starRatingVideoLine = starRatingVideoLine;
	}

	/**
	 * @return the starRatingMessagingLine
	 */
	public Float getStarRatingMessagingLine() {
		return starRatingMessagingLine;
	}

	/**
	 * @param starRatingMessagingLine the starRatingMessagingLine to set
	 */
	public void setStarRatingMessagingLine(Float starRatingMessagingLine) {
		this.starRatingMessagingLine = starRatingMessagingLine;
	}

	/**
	 * @return the starRatingVoiceRcs
	 */
	public Float getStarRatingVoiceRcs() {
		return starRatingVoiceRcs;
	}

	/**
	 * @param starRatingVoiceRcs the starRatingVoiceRcs to set
	 */
	public void setStarRatingVoiceRcs(Float starRatingVoiceRcs) {
		this.starRatingVoiceRcs = starRatingVoiceRcs;
	}

	/**
	 * @return the starRatingVideoRcs
	 */
	public Float getStarRatingVideoRcs() {
		return starRatingVideoRcs;
	}

	/**
	 * @param starRatingVideoRcs the starRatingVideoRcs to set
	 */
	public void setStarRatingVideoRcs(Float starRatingVideoRcs) {
		this.starRatingVideoRcs = starRatingVideoRcs;
	}

	/**
	 * @return the starRatingMessagingRcs
	 */
	public Float getStarRatingMessagingRcs() {
		return starRatingMessagingRcs;
	}

	/**
	 * @param starRatingMessagingRcs the starRatingMessagingRcs to set
	 */
	public void setStarRatingMessagingRcs(Float starRatingMessagingRcs) {
		this.starRatingMessagingRcs = starRatingMessagingRcs;
	}

	/**
	 * @return the starRatingVoiceSkype
	 */
	public Float getStarRatingVoiceSkype() {
		return starRatingVoiceSkype;
	}

	/**
	 * @param starRatingVoiceSkype the starRatingVoiceSkype to set
	 */
	public void setStarRatingVoiceSkype(Float starRatingVoiceSkype) {
		this.starRatingVoiceSkype = starRatingVoiceSkype;
	}

	/**
	 * @return the starRatingVideoSkype
	 */
	public Float getStarRatingVideoSkype() {
		return starRatingVideoSkype;
	}

	/**
	 * @param starRatingVideoSkype the starRatingVideoSkype to set
	 */
	public void setStarRatingVideoSkype(Float starRatingVideoSkype) {
		this.starRatingVideoSkype = starRatingVideoSkype;
	}

	/**
	 * @return the starRatingMessagingSkype
	 */
	public Float getStarRatingMessagingSkype() {
		return starRatingMessagingSkype;
	}

	/**
	 * @param starRatingMessagingSkype the starRatingMessagingSkype to set
	 */
	public void setStarRatingMessagingSkype(Float starRatingMessagingSkype) {
		this.starRatingMessagingSkype = starRatingMessagingSkype;
	}

	/**
	 * @return the starRatingVoiceWhatsapp
	 */
	public Float getStarRatingVoiceWhatsapp() {
		return starRatingVoiceWhatsapp;
	}

	/**
	 * @param starRatingVoiceWhatsapp the starRatingVoiceWhatsapp to set
	 */
	public void setStarRatingVoiceWhatsapp(Float starRatingVoiceWhatsapp) {
		this.starRatingVoiceWhatsapp = starRatingVoiceWhatsapp;
	}

	/**
	 * @return the starRatingVideoWhatsapp
	 */
	public Float getStarRatingVideoWhatsapp() {
		return starRatingVideoWhatsapp;
	}

	/**
	 * @param starRatingVideoWhatsapp the starRatingVideoWhatsapp to set
	 */
	public void setStarRatingVideoWhatsapp(Float starRatingVideoWhatsapp) {
		this.starRatingVideoWhatsapp = starRatingVideoWhatsapp;
	}

	/**
	 * @return the starRatingMessagingWhatsapp
	 */
	public Float getStarRatingMessagingWhatsapp() {
		return starRatingMessagingWhatsapp;
	}

	/**
	 * @param starRatingMessagingWhatsapp the starRatingMessagingWhatsapp to set
	 */
	public void setStarRatingMessagingWhatsapp(Float starRatingMessagingWhatsapp) {
		this.starRatingMessagingWhatsapp = starRatingMessagingWhatsapp;
	}

	/**
	 * @return the starRatingVoiceViber
	 */
	public Float getStarRatingVoiceViber() {
		return starRatingVoiceViber;
	}

	/**
	 * @param starRatingVoiceViber the starRatingVoiceViber to set
	 */
	public void setStarRatingVoiceViber(Float starRatingVoiceViber) {
		this.starRatingVoiceViber = starRatingVoiceViber;
	}

	/**
	 * @return the starRatingVideoViber
	 */
	public Float getStarRatingVideoViber() {
		return starRatingVideoViber;
	}

	/**
	 * @param starRatingVideoViber the starRatingVideoViber to set
	 */
	public void setStarRatingVideoViber(Float starRatingVideoViber) {
		this.starRatingVideoViber = starRatingVideoViber;
	}

	/**
	 * @return the starRatingMessagingViber
	 */
	public Float getStarRatingMessagingViber() {
		return starRatingMessagingViber;
	}

	/**
	 * @param starRatingMessagingViber the starRatingMessagingViber to set
	 */
	public void setStarRatingMessagingViber(Float starRatingMessagingViber) {
		this.starRatingMessagingViber = starRatingMessagingViber;
	}

	/**
	 * @return the starRatingRakutenTv
	 */
	public Float getStarRatingRakutenTv() {
		return starRatingRakutenTv;
	}

	/**
	 * @param starRatingRakutenTv the starRatingRakutenTv to set
	 */
	public void setStarRatingRakutenTv(Float starRatingRakutenTv) {
		this.starRatingRakutenTv = starRatingRakutenTv;
	}

	/**
	 * @return the starRatingRakutenLive
	 */
	public Float getStarRatingRakutenLive() {
		return starRatingRakutenLive;
	}

	/**
	 * @param starRatingRakutenLive the starRatingRakutenLive to set
	 */
	public void setStarRatingRakutenLive(Float starRatingRakutenLive) {
		this.starRatingRakutenLive = starRatingRakutenLive;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getSdkversion() {
		return sdkversion;
	}

	public void setSdkversion(String sdkversion) {
		this.sdkversion = sdkversion;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ConsumerFeedback [id=" + id + ", geographyL4=" + geographyL4 + ", nvProfileData=" + nvProfileData
				+ ", feedbacktime=" + feedbacktime + ", feedbacklocation=" + feedbacklocation + ", testarea=" + testarea
				+ ", problemtype=" + problemtype + ", problemsubtype=" + problemsubtype + ", notes=" + notes + ", imsi="
				+ imsi + ", imei=" + imei + ", make=" + make + ", model=" + model + ", deviceos=" + deviceos
				+ ", latitude=" + latitude + ", longitude=" + longitude + ", feedbacktype=" + feedbacktype
				+ ", starrating=" + starrating + ", starratingdata=" + starratingdata + ", starratingcvg="
				+ starratingcvg + ", vcunabletocall=" + vcunabletocall + ", vccalldrop=" + vccalldrop + ", vcpooraudio="
				+ vcpooraudio + ", voicemute=" + voicemute + ", vconewyaudio=" + vconewyaudio + ", dataslowspeed="
				+ dataslowspeed + ", dataunavletocon=" + dataunavletocon + ", cvgpoorcvgind=" + cvgpoorcvgind
				+ ", cvgPoorCvgOut=" + cvgPoorCvgOut + ", operatorname=" + operatorname + ", networktype=" + networktype
				+ ", mcc=" + mcc + ", mnc=" + mnc + ", pci=" + pci + ", cellid=" + cellid + ", tac=" + tac
				+ ", wifiname=" + wifiname + ", rssiwifi=" + rssiwifi + ", snrWifi=" + snrWifi + ", ulrate=" + ulrate
				+ ", dlrate=" + dlrate + ", versionname=" + versionname + ", nvmodule=" + nvmodule + ", baseband="
				+ baseband + ", buildnumber=" + buildnumber + ", mobilenumber=" + mobilenumber + ", rsrplte=" + rsrplte
				+ ", rsrqlte=" + rsrqlte + ", rssilte=" + rssilte + ", sinrlte=" + sinrlte + ", rxlevel2g=" + rxlevel2g
				+ ", rxquality2g=" + rxquality2g + ", rscp3g=" + rscp3g + ", ecno3g=" + ecno3g + ", batterylevel="
				+ batterylevel + ", voltage=" + voltage + ", temperature=" + temperature + ", band=" + band
				+ ", deviceid=" + deviceid + ", androidid=" + androidid + ", enterprise=" + enterprise
				+ ", layer3enabled=" + layer3enabled + ", chipset=" + chipset + ", serialnumber=" + serialnumber
				+ ", socmodel=" + socmodel + ", corearchitecture=" + corearchitecture + ", devicefingerprint="
				+ devicefingerprint + ", networksubtype=" + networksubtype + ", macaddress=" + macaddress + ", bssid="
				+ bssid + ", neighbourinfo=" + neighbourinfo + ", cgilte=" + cgilte + ", enodeblte=" + enodeblte
				+ ", internalip=" + internalip + ", wifinetworktype=" + wifinetworktype + ", avglinkspeed="
				+ avglinkspeed + ", wificonnected=" + wificonnected + ", chargerstatus=" + chargerstatus
				+ ", locationaccuracy=" + locationaccuracy + ", locationaltitude=" + locationaltitude + ", dualsim="
				+ dualsim + ", autodatetimeenabled=" + autodatetimeenabled + ", gpsenabled=" + gpsenabled
				+ ", starRatingVoIp=" + starRatingVoIp + ", voIpSkypeUnableToMakeCall=" + voIpSkypeUnableToMakeCall
				+ ", voIpSkypeCallDrop=" + voIpSkypeCallDrop + ", voIpSkypePoorAudio=" + voIpSkypePoorAudio
				+ ", voIpSkypeMute=" + voIpSkypeMute + ", voIpSkypeOneWayAudio=" + voIpSkypeOneWayAudio
				+ ", voIpViberUnableToMakeCall=" + voIpViberUnableToMakeCall + ", voIpViberCallDrop="
				+ voIpViberCallDrop + ", voIpViberPoorAudio=" + voIpViberPoorAudio + ", voIpViberMute=" + voIpViberMute
				+ ", voIpViberOneWayAudio=" + voIpViberOneWayAudio + ", voIpWhatsAppUnableToMakeCall="
				+ voIpWhatsAppUnableToMakeCall + ", voIpWhatsAppCallDrop=" + voIpWhatsAppCallDrop
				+ ", voIpWhatsAppPoorAudio=" + voIpWhatsAppPoorAudio + ", voIpWhatsAppMute=" + voIpWhatsAppMute
				+ ", voIpWhatsAppOneWayAudio=" + voIpWhatsAppOneWayAudio + ", floorno=" + floorno
				+ ", voIpRcsUTMCToRcs=" + voIpRcsUTMCToRcs + ", voIpRcsUTMCToNonRcs=" + voIpRcsUTMCToNonRcs
				+ ", voIpRcsUTMGroupCall=" + voIpRcsUTMGroupCall + ", voIpRcsCallDisconnect=" + voIpRcsCallDisconnect
				+ ", voIpRcsPoorAudio=" + voIpRcsPoorAudio + ", voIpRcsUTSMsgToRcs=" + voIpRcsUTSMsgToRcs
				+ ", voIpRcsUTSMsgToNonRcs=" + voIpRcsUTSMsgToNonRcs + ", voIpRcsUTMGroupChat=" + voIpRcsUTMGroupChat
				+ ", voIpRcsUTSMultimedia=" + voIpRcsUTSMultimedia + ", highSpeedData=" + highSpeedData
				+ ", excellentCoverage=" + excellentCoverage + ", excellentAudioQuality=" + excellentAudioQuality
				+ ", poorCoverage=" + poorCoverage + ", apnName=" + apnName + ", starRatingVoiceRcs="
				+ starRatingVoiceRcs + ", starRatingVideoRcs=" + starRatingVideoRcs + ", starRatingMessagingRcs="
				+ starRatingMessagingRcs + ", data=" + data + ", sdkversion=" + sdkversion + "]";
	}

	
}

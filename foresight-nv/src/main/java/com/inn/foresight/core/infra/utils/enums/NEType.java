package com.inn.foresight.core.infra.utils.enums;

import com.inn.foresight.core.generic.utils.DisplayNameProperty;

/** The Enum NEType. */
public enum NEType implements DisplayNameProperty {
	/** The wifi ap. */
	WIFI_AP("WIFI_AP", true),

	/** The xms. */
	XMS("XMS", true),

	/** The nec zabbix. */
	NEC_ZABBIX("NEC Zabbix", true),

	/** The wifi. */
	WIFI("WiFi", true),

	/** The aerial. */
	AERIAL("AERIAL", true),

	/** The attached. */
	ATTACHED("ATTACHED", true),

	/** The community. */
	COMMUNITY("COMMUNITY", true),

	/** The smb. */
	SMB("SMB", true),

	/** The venuebase. */
	VENUEBASE("VENUEBASE", true),

	/** The venueplus. */
	VENUEPLUS("VENUEPLUS", true),

	/** The twh. */
	TWH("TWH", true),

	/** The small cell outdoor. */
	SMALL_CELL_OUTDOOR("Smallcell Outdoor", true),

	/** The small cell indoor. */
	SMALL_CELL_INDOOR("Smallcell Indoor", true),

	/** The mme. */
	MME("MME", true),

	/** The saegw. */
	SAEGW("SAEGW", true),

	/** The css. */
	CSS("CSS", true),

	/** The macro. */
	MACRO("Macro", true),

	/** The e SON. */
	ESON("eSON", true),

	/** The macro cell. */
	MACRO_CELL("Macro Cell", true),

	/** The cpnr. */
	CPNR("CPNR", true),

	/** The esr. */
	ESR("ESR", true),

	/** The csm. */
	CSM("CSM", true),

	/** The nso. */
	NSO("NSO", true),

	/** The esc. */
	ESC("ESC", true),

	/** The vms. */
	VMS("VMS", true),

	/** The dsn. */
	DSN("DSN", true),

	/** The aar. */
	AAR("AAR", true),

	/** The par. */
	PAR("PAR", true),

	/** The ccr. */
	CCR("CCR", true),

	/** The enb. */
	ENB("ENB", true),

	/** The macro enb. */
	MACRO_ENB("Macro eNodeB", true),

	/** The fibertake. */
	FIBERTAKE("FIBERTAKE", true),

	/** The microwave. */
	MICROWAVE("MICROWAVE", true),

	/** The cs. */
	CS("CS", true),

	/** The dra. */
	DRA("DRA", true),

	/** The hlr. */
	HLR("HLR", true),

	/** The ngmgw. */
	NGMGW("NGMGW", true),

	/** The xgw. */
	XGW("XGW", true),

	/** The agr. */
	AGR("AGR", true),

	/** The pe. */
	PE("PE", true),

	/** The gallery. */
	GALLERY("GALLERY", true),

	/** The shooter. */
	SHOOTER("SHOOTER", true),

	/** The ibs. */
	IBS("IBS", true),

	/** The pico. */
	PICO("PICO", true),

	/** The p. */
	P("P", true),

	/** The cor. */
	COR("COR", true),

	/** The acc. */
	ACC("ACC", true),

	/** The agw. */
	AGW("AGW", true),

	/** The asbc. */
	ASBC("ASBC", true),

	/** The ccps. */
	CCPS("CCPS", true),

	/** The epdg. */
	EPDG("EPDG", true),

	/** The cscf. */
	CSCF("CSCF", true),

	/** The enum. */
	ENUM("ENUM", true),

	/** The ipsm. */
	IPSM("IPSM", true),

	/** The mrf. */
	MRF("MRF", true),

	/** The prs. */
	PRS("PRS", true),

	/** The pvgw. */
	PVGW("PVGW", true),

	/** The rcs. */
	RCS("RCS", true),

	/** The sps. */
	SPS("SPS", true),

	/** The tas. */
	TAS("TAS", true),

	/** The pgw. */
	PGW("PGW", true),

	/** The msce. */
	MSCE("MSCE", true),

	/** The mscs. */
	MSCS("MSCS", true),

	/** The uspp. */
	USPP("USPP", true),

	/** The uag. */
	UAG("UAG", true),

	/** The cmd. */
	CMD("CMD", true),

	/** The webrtc. */
	WEBRTC("WEBRTC", true),

	/** The xdms. */
	XDMS("XDMS", true),

	/** The ip. */
	IP("IP", true),

	/** The ce. */
	CE("CE", true),

	/** The rr. */
	RR("RR", true),

	/** The macro vcu. */
	MACRO_VCU("MACRO_VCU", true),

	/** The macro vdu. */
	MACRO_VDU("MACRO_VDU", true),

	/** The idsc cell. */
	IDSC_CELL("IDSC Cell", true),

	/** The idsc site. */
	IDSC_SITE("IDSC Site", true),

	/** The idsc vdu. */
	IDSC_VDU("IDSC_VDU", true), 

	/** The odsc cell. */
	ODSC_CELL("ODSC Cell", true),

	/** The odsc site. */
	ODSC_SITE("ODSC Site", true),

	/** The gallery site. */
	GALLERY_SITE("GALLERY_SITE", true),

	/** The gallery cell. */
	GALLERY_CELL("GALLERY_CELL", true),

	/** The shooter site. */
	SHOOTER_SITE("SHOOTER_SITE", true),

	/** The shooter cell. */
	SHOOTER_CELL("SHOOTER_CELL", true),

	/** The ibs site. */
	IBS_SITE("IBS_SITE", true),

	/** The ibs cell. */
	IBS_CELL("IBS_CELL", true),

	/** The pico site. */
	PICO_SITE("PICO_SITE", true),

	/** The pico cell. */
	PICO_CELL("PICO_CELL", true),

	/** The ipbb zte zxr10. */
	IPBB_ZTE_ZXR10("IPBB_ZTE_ZXR10", true),

	/** The ipbb. */
	IPBB("IPBB"),

	/** The trans zte zxctn. */
	TRANS_ZTE_ZXCTN("TRANS_ZTE_ZXCTN", true),

	/** The mw zte zxmw. */
	MW_ZTE_ZXMW("MW_ZTE_ZXMW", true),

	/** The core zte. */
	CORE_ZTE("CORE_ZTE", true),

	/** The igw. */
	IGW("IGW", true),

	/** The brs. */
	BRS("BRS", true),

	/** The mw crg. */
	MW_CRG("MW_CRG", true),

	/** The atlc. */
	ATLC("ATLC", true),

	/** The cell. */
	CELL("CELL", true),

	/** The site. */
	SITE("SITE", true),

	/** The wow. */
	WOW("Wow", true),

	/** The test. */
	TEST("Test", true),

	/** The vault. */
	VAULT("Vault", true),

	/** The small cell. */
	SMALL_CELL("SMALL_CELL", true),

	/** The hss. */
	HSS("HSS", true),

	/** The pcrf. */
	PCRF("PCRF", true),

	/** The mgcf. */
	MGCF("MGCF", true),

	/** The mgw. */
	MGW("MGW", true),

	/** The ems. */
	EMS("EMS", true),

	/** The cgnat. */
	CGNAT("CGNAT", true),

	/** The bgp. */
	BGP("BGP", true),

	/** The vcu. */
	VCU("VCU", true),

	/** The vdu. */
	VDU("VDU", true),

	/** The riu. */
	RIU("RIU", true),

	/** The rrh. */
	RRH("RRH", true),

	/** The csd. */
	CSD("CSD", true),

	/** The cbam. */
	CBAM("CBAM", true),

	/** The server. */
	SERVER("SERVER", true),

	/** The switch. */
	SWITCH("SWITCH", true),

	/** The router. */
	ROUTER("ROUTER", true),

	/** The pod. */
	POD("POD", true),

	/** The sdl. */
	SDL("SDL", true),

	/** The titan. */
	TITAN("TITAN", true),

	/** The eir. */
	EIR("EIR", true),

	/** The vnf. */
	VNF("VNF", true),

	/** The epc. */
	EPC("EPC", true),

	/** The netact. */
	NETACT("NETACT", true),

	/** The zabbix. */
	ZABBIX("Zabbix", true),

	/** The cvimmon. */
	CVIMMON("CVIMMON", true),

	/** The ciena6500. */
	CIENA6500("Ciena 6500", true),

	/** The epnm. */
	EPNM("EPNM", true),

	/** The air hop. */
	AIR_HOP("AirHOP", true),

	/** The baremetal. */
	BAREMETAL("BAREMETAL", true),

	/** The dcnm. */
	DCNM("DCNM", true),

	/** The auc. */
	AUC("AUC", true),

	/** The wdm. */
	WDM("WDM", true),

	/** The msc. */
	MSC("MSC", true),

	/** The mss. */
	MSS("MSS", true),

	/** The mnp. */
	MNP("MNP", true),

	/** The rcp. */
	RCP("RCP", true),

	/** The spr. */
	SPR("SPR", true),

	/** The cg. */
	CG("CG", true),

	/** The vnfm. */
	VNFM("VNFM", true),

	/** The omm. */
	OMM("OMM", true),

	/** The cgw. */
	CGW("CGW", true),

	/** The stp. */
	STP("STP", true),

	/** The other. */
	OTHER("OTHER", true),

	/** The tor. */
	TOR("TOR", true),

	/** The floor poe switch. */
	FLOOR_POE_SWITCH("FLOOR_POE_SWITCH", true),

	/** The floor switch. */
	FLOOR_SWITCH("FLOOR_SWITCH", true),

	/** The building switch. */
	BUILDING_SWITCH("BUILDING_SWITCH", true),

	/** The core room. */
	CORE_ROOM("CORE_ROOM", true),

	/** The dns. */
	DNS("DNS", true),

	/** The firewall. */
	FIREWALL("FIREWALL", true),

	/** The symmetricom. */
	SYMMETRICOM("SYMMETRICOM", true),

	/** The igm. */
	IGM("IGM", true),

	/** The ntp. */
	NTP("NTP", true),

	/** The transmission. */
	TRANSMISSION("TRANSMISSION", true),

	/** The ims. */
	IMS("IMS", true),

	/** The dcn. */
	DCN("DCN", true),

	/** The ran. */
	RAN("RAN", true),

	/** The core. */
	CORE("CORE", true),

	/** The it. */
	IT("IT", true),

	/** The vas. */
	VAS("VAS", true),

	/** The lims. */
	LIMS("LIMS", true),

	/** The power. */
	POWER("POWER", true),

	/** The bfm. */
	BFM("BFM", true),

	/** The partner. */
	PARTNER("PARTNER", true),

	/** The wss. */
	WSS("WSS", true),

	/** The pabx. */
	PABX("PABX", true),

	/** The aci fabric. */
	ACI_FABRIC("ACI_FABRIC", true),

	/** The obm switch. */
	OBM_SWITCH("OBM_SWITCH", true),

	/** The gmlc. */
	GMLC("GMLC", true),

	/** The nls. */
	NLS("NLS", true),

	/** The rems. */
	REMS("REMS", true),

	/** The aaa. */
	AAA("AAA", true),

	/** The ribbon. */
	RIBBON("RIBBON", true),

	/** The ribbon ems. */
	RIBBON_EMS("RIBBON_EMS", true),

	/** The flow. */
	FLOW("FLOW", true),

	/** The sercomm. */
	SERCOMM("SERCOMM", true),

	/** The radcom. */
	RADCOM("RADCOM", true),

	/** The of. */
	OF("OF", true),

	/** The matrix. */
	MATRIX("Matrix", true),

	/** The cgf. */
	CGF("CGF", true),

	/** The etws. */
	ETWS("ETWS", true),

	/** The ncm. */
	NCM("NCM", true),

	/** The ag1. */
	AG1("AG1", true),

	/** The ag2. */
	AG2("AG2", true),

	/** The ag3. */
	AG3("AG3", true),

	/** The ag4. */
	AG4("AG4", true),

	/** The enodeb. */
	ENODEB("ENODEB", true),

	/** The rcs voice. */
	RCS_VOICE("RCS_VOICE", true),

	/** The rcs messaging. */
	RCS_MESSAGING("RCS_MESSAGING", true),

	/** The rcs core. */
	RCS_CORE("RCS_CORE", true),

	/** The wrg. */
	WRG("WRG", true),

	/** The wsg. */
	WSG("WSG", true),

	/** The prx. */
	PRX("PRX", true),

	/** The pns. */
	PNS("PNS", true),

	/** The sdc. */
	SDC("SDC", true),

	/** The vmas. */
	VMAS("VMAS", true),

	/** The mstore. */
	MSTORE("MSTORE", true),

	/** The crdl. */
	CRDL("CRDL", true),

	/** The smsc. */
	SMSC("SMSC", true),

	/** The cms. */
	CMS("CMS", true),

	/** The ipnode. */
	IPNODE("IPNODE", true),

	/** The sdns. */
	SDNS("SDNS", true),

	/** The sm. */
	SM("SM", true),

	/** The smsrouter. */
	SMSROUTER("SMSROUTER", true),

	/** The flowone. */
	FLOWONE("FLOWONE", true),

	/** The ribbon dsi. */
	RIBBON_DSI("RIBBON_DSI", true),

	/** The ribbon gsx. */
	RIBBON_GSX("RIBBON_GSX", true),

	/** The ribbon psx. */
	RIBBON_PSX("RIBBON_PSX", true),

	/** The ribbon sbc. */
	RIBBON_SBC("RIBBON_SBC", true),

	/** The ribbon sgx. */
	RIBBON_SGX("RIBBON_SGX", true),

	/** The stpgw t1. */
	STPGW_T1("STPGW_T1", true),

	/** The stpgw mach7. */
	STPGW_MACH7("STPGW_MACH7", true),

	/** The oob switch. */
	OOB_SWITCH("OOB_SWITCH", true),

	/** The stpgw. */
	STPGW("STPGW", true),

	/** The li thales ulis gw. */
	LI_THALES_ULIS_GW("LI(Thales ULIS [GW])", true),

	/** The li thales spyder mc. */
	LI_THALES_SPYDER_MC("LI(Thales Spyder [MC])", true),

	/** The gw stp telesys t1 nic. */
	GW_STP_TELESYS_T1_NIC("GW-STP(teleSys T1 NIC)", true),

	/** The gw stp telesys t1 mach7. */
	GW_STP_TELESYS_T1_MACH7("GW-STP(Telsys MACH7)", true),

	/** The I SB C ribbon SBC. */
	I_SBC_RIBBON_SBC("I-SBC(Ribbon SBC)", true),

	/** The GML C nokial LS. */
	GMLC_NOKIAL_LS("GMLC(Nokial LS)", true),

	/** The EM S ribbon EMS. */
	EMS_RIBBON_EMS("EMS(Ribbon EMS)", true),

	/** The Prov ribbon PSX. */
	PROV_RIBBON_PSX("Prov.(Ribbon PSX)", true),

	/** The C G mediation ribbon DSI. */
	CG_MEDIATION_RIBBON_DSI("CG Mediation(Ribbon DSI)", true),

	/** The MG W ribbon GSX. */
	MGW_RIBBON_GSX("MGW(Ribbon GSX)", true),

	/** The mgw ribbon sgx. */
	MGW_RIBBON_SGX("MGW(Ribbon SGX)", true),

	/** The MGC F ribbon SGX. */
	MGCF_RIBBON_SGX("MGCF(Ribbon SGX)", true),

	/** The vm. */
	VM("VM"),

	/** The mw repeater. */
	MW_REPEATER("MW_REPEATER", true),

	/** The Fore sight. */
	ForeSight("ForeSight", true),

	/** The Site forge. */
	SiteForge("SiteForge", true),

	/** The ground master. */
	GROUND_MASTER("Ground Master", true),

	/** The building sw. */
	BUILDING_SW("Building SW", true),

	/** The saegw u. */
	SAEGW_U("SAEGW-U", true),

	/** The saegw c. */
	SAEGW_C("SAEGW-C", true),

	/** The nexsus tor. */
	NEXSUS_TOR("Nexsus ToR", true),

	/** The cvim 2 9 14 15. */
	CVIM_2_9_14_15("CVIM 2.9.14/15", true),

	/** The cvim 3 2 1. */
	CVIM_3_2_1("CVIM 3.2.1", true),

	/** The retws. */
	RETWS("RETWS", true),

	/** The Net velocity. */
	Net_Velocity("Net Velocity", true),

	/** The msc cloud. */
	MSC_CLOUD("MSC(MSS Cloud)", true),

	/** The mnp one mnp. */
	MNP_ONE_MNP("MNP(One-MNP)", true),

	/** The eir one eir. */
	EIR_ONE_EIR("EIR(One-EIR)", true),

	/** The prov flowone. */
	PROV_FLOWONE("Prov.(FlowOne)", true),
	
	/** The IDSC_VCU. */
	IDSC_VCU("IDSC_VCU", true),

	/** The cgf data ref. */
	CGF_DATA_REF("CGF(Data Ref.)", true);

	/** The display name. */
	private String displayName;

	/**
	 * To discard Enum participate in display Name
	 * 
	 * <pre>
	 *  To allow Enum in Display Name set second parameter as <code>true</code>
	 * </pre>
	 * 
	 * .
	 */
	private boolean isDisplayElabled;

	/**
	 * Instantiates the group of Alarm ex: Parent Group, Child Group.
	 *
	 * @param displayName the display name
	 */
	private NEType(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Instantiates a new NE type.
	 *
	 * @param displayName      the display name
	 * @param isDisplayElabled the is display elabled
	 */
	private NEType(String displayName, boolean isDisplayElabled) {
		this.isDisplayElabled = isDisplayElabled;
		this.displayName = displayName;
	}

	/**
	 * Display name.
	 *
	 * @return the string
	 */
	@Override
	public String displayName() {
		return displayName;
	}

	/**
	 * Checks if is display enabled.
	 *
	 * @return true, if is display enabled
	 */
	@Override
	public boolean isDisplayEnabled() {
		return isDisplayElabled;
	}
}

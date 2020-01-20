package com.inn.foresight.module.nv.layer3.constants;

// TODO: Auto-generated Javadoc
/** The Class QMDLConstant. */
public class QMDLConstant {

	public static final String SUCCESS = "success";
	public static final String FAILURE = "Failure";


	public static final String EXCEPTION_SOMETHING_WENT_WRONG = "There is some problem in completing the request.";
	public static final String DATA_NOT_FOUND = "Data not found for the request.";
	public static final String INVALID_PARAMETER = "Parameters given are invalid.";

	public static final String MESSAGE_START = "{\"message\":\"";
	public static final String MESSAGE_END = "\"}";
	public static final String EXCEPTION_SOMETHING_WENT_WRONG_JSON = MESSAGE_START+EXCEPTION_SOMETHING_WENT_WRONG+MESSAGE_END;
	public static final String DATA_NOT_FOUND_JSON = MESSAGE_START+DATA_NOT_FOUND+MESSAGE_END;
	public static final String INVALID_PARAMETER_JSON = MESSAGE_START+INVALID_PARAMETER+MESSAGE_END;
	public static final String SUCCESS_JSON = MESSAGE_START+SUCCESS+MESSAGE_END;
	public static final String FAILURE_JSON = MESSAGE_START+FAILURE+MESSAGE_END;

	public static final String RECIPE_DEFAULT = "receipeDefault";

	public static final String OPERATOR_DEFAULT = "operatorDefault";

	public static final String PADDING_VALUE_STR = "1";


	public static final Integer DEFAULT_UNIT = 1;

	public static final String SSVT_MAP_KEY = "recipePCIMap";
	
	public static final String MESSAGE = "message";
		

	public static final Integer KPI_NAME_INDEX = 1;
	public static final Integer KPI_SPLIT_COUNT = 2;

	public static final String BRTI_REPORT_JSON = "{\"data\": {}, \"period\": \"Quarterly\", \"reportType\": \"BRTI Quarterly Report\", \"technology\": \"Unlock\", \"geographyType\": \"GeographyL3\"}";

	public static final String BRTI_DATA_KEY = "data";

	public static final String GEOGRAPHY_LIST = "geographyIdList";

	public static final String WORKORDER_LIST = "workOrderIds";

	public static final String YEAR_KEY = "year";

	public static final String QUATER_KEY = "quarter";
	
	/** The Constant LOG_CODE. */
	public static final String LOG_CODE = "logcode";

	/** The Constant COLUMN_FAMILY. */
	public static final String COLUMN_FAMILY = "r";

	/** The Constant EARFCN. */
	public static final String EARFCN = "earfcn";

	/** The Constant SERVING_CELL_INDEX. */
	public static final String SERVING_CELL_INDEX = "sCellIndex";

	/** The Constant CURRENT_SNF. */
	public static final String CURRENT_SNF = "crrntSNF";

	/** The Constant CELL_TIMING_0. */
	public static final String CELL_TIMING_0 = "ct0";

	/** The Constant CELL_TIMING_1. */
	public static final String CELL_TIMING_1 = "ct1";

	/** The Constant CELL_TIMING_SNF_0. */
	public static final String CELL_TIMING_SNF_0 = "ctSNF0";

	/** The Constant CELL_TIMING_SNF_1. */
	public static final String CELL_TIMING_SNF_1 = "ctSNF1";

	/** The Constant INST_RSRP_RX_0. */
	public static final String INST_RSRP_RX_0 = "RSRPRx0";

	/** The Constant INST_RSRP_RX_1. */
	public static final String INST_RSRP_RX_1 = "RSRPRx1";

	/** The Constant INST_RSRP_RX_0_DATA. */
	public static final String INST_RSRP_RX_0_DATA = "IRSRPRx0D";

	/** The Constant INST_RSRP_RX_1_DATA. */
	public static final String INST_RSRP_RX_1_DATA = "IRSRPRx1D";

	/** The Constant INST_RSRQ_RX_0_DATA. */
	public static final String INST_RSRQ_RX_0_DATA = "IRSRQRx0D";

	/** The Constant INST_RSRQ_RX_1_DATA. */
	public static final String INST_RSRQ_RX_1_DATA = "IRSRQRx1D";

	/** The Constant MEASURED_RSRP_DATA. */
	public static final String MEASURED_RSRP_DATA = "MRSRPD";

	/** The Constant MEASURED_RSRQ_DATA. */
	public static final String MEASURED_RSRQ_DATA = "MRSRQD";

	/** The Constant INST_RSSI_RX_0_DATA. */
	public static final String INST_RSSI_RX_0_DATA = "IRSSIRx0D";

	/** The Constant INST_RSSI_RX_1_DATA. */
	public static final String INST_RSSI_RX_1_DATA = "IRSSIRx1D";

	/** The Constant INST_RSSI_DATA. */
	public static final String INST_RSSI_DATA = "RSSID";

	/** The Constant RESIDUAL_FREQUENCY_ERROR. */
	public static final String RESIDUAL_FREQUENCY_ERROR = "rFErrr";

	/** The Constant FTL_SNR_RX_0_DATA. */
	public static final String FTL_SNR_RX_0_DATA = "FSNRRx0D";

	/** The Constant FTL_SNR_RX_1_DATA. */
	public static final String FTL_SNR_RX_1_DATA = "FSNRRx1D";

	/** The Constant HORXD_MODE. */
	public static final String HORXD_MODE = "hrxdMode";

	/** The Constant NUM_OF_CELL. */
	public static final String NUM_OF_CELL = "numOfCell";

	/** The Constant IS_SERVING_CELL. */
	public static final String IS_SERVING_CELL = "isSrvngCll";

	/** The Constant CURRENT_SUBFRAME_NUMBER. */
	public static final String CURRENT_SUBFRAME_NUMBER = "cSBFrmN";

	/** The Constant IS_RESTRICTED. */
	public static final String IS_RESTRICTED = "isRstrctd";

	/** The Constant INST_RSRP_RX_2_DATA. */
	public static final String INST_RSRP_RX_2_DATA = "iRSRPRx2D";

	/** The Constant INST_RSRP_RX_3_DATA. */
	public static final String INST_RSRP_RX_3_DATA = "iRSRPRx3D";

	/** The Constant INST_RSRQ_RX_2_DATA. */
	public static final String INST_RSRQ_RX_2_DATA = "iRSRQRx2D";

	/** The Constant INST_RSRQ_RX_3_DATA. */
	public static final String INST_RSRQ_RX_3_DATA = "iRSRQRx3D";

	/** The Constant INST_RSSI_RX_2_DATA. */
	public static final String INST_RSSI_RX_2_DATA = "iRSSIRx2D";

	/** The Constant INST_RSSI_RX_3_DATA. */
	public static final String INST_RSSI_RX_3_DATA = "iRSSIRx3D";

	/** The Constant FTL_SNR_RX_2_DATA. */
	public static final String FTL_SNR_RX_2_DATA = "fSNRRx2D";

	/** The Constant FTL_SNR_RX_3_DATA. */
	public static final String FTL_SNR_RX_3_DATA = "fSNRRx3D";

	/** The Constant PROJECTED_SIR_DATA. */
	public static final String PROJECTED_SIR_DATA = "pSirD";

	/** The Constant POST_IC_RSRQ_DATA. */
	public static final String POST_IC_RSRQ_DATA = "pICRSRQD";

	/** The Constant CINR_RX_0. */
	public static final String CINR_RX_0 = "cINRRx0";

	/** The Constant CINR_RX_1. */
	public static final String CINR_RX_1 = "cINRRx1";

	/** The Constant CINR_RX_2. */
	public static final String CINR_RX_2 = "cINRRx2";

	/** The Constant CINR_RX_3. */
	public static final String CINR_RX_3 = "cINRRx3";

	/** The Constant B195_LOG_CODE. */
	public static final Integer B195_LOG_CODE = 45461;

	/** The Constant CNT_SWRP. */
	public static final String CNT_SWRP = "cntSwrp";

	/** The Constant NUM_NCELLS. */
	public static final String NUM_NCELLS = "numNclls";

	/** The Constant DUPLEXING_MODE. */
	public static final String DUPLEXING_MODE = "dMode";

	/** The Constant FTL_CUMU_FREQ_OFFSET. */
	public static final String FTL_CUMU_FREQ_OFFSET ="ftlCmFrqOffset";

	/** The Constant FRAME_BDRY_REF_TIME_0. */
	public static final String FRAME_BDRY_REF_TIME_0 = "fBdryRefT0";

	/** The Constant FRAME_BDRY_REF_TIME_1. */
	public static final String FRAME_BDRY_REF_TIME_1 = "fBdryRefT1";

	/** The Constant TOTAL_TIMING_ADJ_CIR_0. */
	public static final String TOTAL_TIMING_ADJ_CIR_0 = "tTAdjCir0";

	/** The Constant TOTAL_TIMING_ADJ_CIR_1. */
	public static final String TOTAL_TIMING_ADJ_CIR_1 = "tTdjCir1";

	/** The Constant INST_RSRP. */
	public static final String INST_RSRP = "iRSRP";

	/** The Constant INST_RSRQ. */
	public static final String INST_RSRQ = "iRSRQ";

	/** The Constant INST_RSRQ_RX_0. */
	public static final String INST_RSRQ_RX_0 = "iRSRQRx0";

	/** The Constant INST_RSRQ_RX_1. */
	public static final String INST_RSRQ_RX_1 = "iRSRQRx1";

	/** The Constant INST_RSSI. */
	public static final String INST_RSSI = "iRSSI";

	/** The Constant INST_RSSI_RX_0. */
	public static final String INST_RSSI_RX_0 = "iRSSIRx0";

	/** The Constant INST_RSSI_RX_1. */
	public static final String INST_RSSI_RX_1 = "iRSSIRx1";

	/** The Constant PHYSICAL_CELL_ID. */

	public static final String PHYSICAL_CELL_ID = "PCI";
	/** The Constant B14D_LOGCODE. */
	public static final Integer B14D_LOGCODE = 45389;

	/** The Constant RANK_INDEX. */
	public static final String RANK_INDEX = "rnkIndx";

	/** The Constant CQI_CW_0. */
	public static final String CQI_CW_0 = "cqicw0";

	/** The Constant B139_LOGCODE. */
	public static final Integer B139_LOGCODE = 45369;

	/** The Constant NUMBER_OF_RB. */
	public static final String NUMBER_OF_RB = "numOfRB";

	/** The Constant PUSCH_TX_POWER. */
	public static final String PUSCH_TX_POWER = "pTxPower";

	/** The Constant B1B9_LOGCODE. */
	public static final Integer B1B9_LOGCODE = 45497;

	/** The Constant DL_EARFCN. */
	public static final String DL_EARFCN = "dLEarfcn";

	/** The Constant UL_EARFCN. */
	public static final String UL_EARFCN = "uLEarfcn";

	/** The Constant TDD_CONFIG. */
	public static final String TDD_CONFIG = "tddConfig";

	/** The Constant SPECIAL_SUBFRAME_CONFIGURATION. */
	public static final String SPECIAL_SUBFRAME_CONFIGURATION = "sSubFConf";

	/** The Constant DL_CP. */
	public static final String DL_CP = "dlCp";

	/** The Constant UL_CP. */
	public static final String UL_CP = "ulCp";

	/** The Constant B0C0_LOGCODE. */
	public static final Integer B0C0_LOGCODE = 45248;

	/** The Constant B0C0_MSG. */
	public static final String B0C0_MSG = "b0c0";
	
	public static final String LOGCODE_412F_MSG = "412f";
	public static final String LOGCODE_512F_MSG = "512f";
	public static final String LOGCODE_713A_MSG = "713a";

	
	public static final String LOGCODE_412F_MSG_TYPE = "412fMType";
	public static final String LOGCODE_512F_MSG_TYPE = "512fMType";
	public static final String LOGCODE_713A_MSG_TYPE = "713aMType";

	
	public static final String LOGCODE_412F_DIRECTION = "412fDirection";
	public static final String LOGCODE_512F_DIRECTION = "512fDirection";
	public static final String LOGCODE_713A_DIRECTION = "713aDirection";

	
	
	

	/** The Constant B0C0_CHANNEL_TYPE. */
	public static final String B0C0_CHANNEL_TYPE = "b0c0CType";

	/** The Constant B0C0_PHYSICAL_CELL_ID. */
	public static final String B0C0_PHYSICAL_CELL_ID = "b0c0PCI";

	/** The Constant B0C2_LOGCODE. */
	public static final Integer B0C2_LOGCODE = 45250;

	/** The Constant B0C2_PHYSICAL_CELL_ID. */
	public static final String B0C2_PHYSICAL_CELL_ID = "b0c2PCI";

	/** The Constant DL_BANDWIDTH. */
	public static final String DL_BANDWIDTH = "dlBndwdth";

	/** The Constant UL_BANDWIDTH. */
	public static final String UL_BANDWIDTH = "ulBndwdth";

	/** The Constant TRACKING_AREA_CODE. */
	public static final String TRACKING_AREA_CODE = "tACode";

	/** The Constant MCC. */
	public static final String MCC = "mcc";

	/** The Constant MNC. */
	public static final String MNC = "mnc";

	/** The Constant B168_LOGCODE. */
	public static final Integer B168_LOGCODE = 45416;

	/** The Constant TIMING_ADVANCE. */
	public static final String TIMING_ADVANCE = "tAdvnce";

	/** The Constant B169_LOGCODE. */
	public static final Integer B169_LOGCODE = 45417;
	
	/** The Constant B16A_LOGCODE. */
	public static final Integer B16A_LOGCODE = 45418;


	/** The Constant MCS. */
	public static final String MCS = "mcs";

	/** The Constant MODULATION_TYPE. */
	public static final String MODULATION_TYPE = "mType";

	/** The Constant B126_LOGCODE. */
	public static final Integer B126_LOGCODE = 45350;

	/** The Constant NUMBER_OF_TX_ANTENNAS_M. */
	public static final String NUMBER_OF_TX_ANTENNAS_M = "nOfTxAntnnsM";

	/** The Constant NUMBER_OF_RX_ANTENNAS_N. */
	public static final String NUMBER_OF_RX_ANTENNAS_N = "nOfRxAntnnsN";

	/** The Constant RB_ALLOCATION_SLOT_0. */
	public static final String RB_ALLOCATION_SLOT_0 = "rbASlot0";

	/** The Constant RB_ALLOCATION_SLOT_1. */
	public static final String RB_ALLOCATION_SLOT_1 = "rbASlot1";

	/** The Constant B18A_LOGCODE. */
	public static final Integer B18A_LOGCODE = 45450;

	/** The Constant OUT_OF_SYNC_BLER. */
	public static final String OUT_OF_SYNC_BLER = "oOSyncBler";

	/** The Constant SINR. */
	public static final String SINR = "sinr";
	
	/** The Constant SINR_RX0. */
	public static final String SINR_RX0 = "sinr_rx0";
	
	/** The Constant SINR_RX1. */
	public static final String SINR_RX1 = "sinr_rx1";

	/** The Constant RSRP_RX0. */
	public static final String RSRP_RX0 = "rsrp_rx1";
	
	/** The Constant RSRP_RX1. */
	public static final String RSRP_RX1 = "rsrp_rx0";
	
	/** The Constant RSRP. */
	public static final String RSRP = "rsrp";
	
	/** The Constant RSRQ. */
	public static final String RSRQ = "rsrq";

	/** The Constant RSRQ_RX0. */
	public static final String RSRQ_RX0 = "rsrq_rx0";
	
	/** The Constant RSRQ_RX1. */
	public static final String RSRQ_RX1 = "rsrq_rx1";
	
	/** The Constant RSSI. */
	public static final String RSSI = "rssi";
	
	/** The Constant RSSI_RX0. */
	public static final String RSSI_RX0 = "rssi_rx0";
	
	/** The Constant RSSI_RX1. */
	public static final String RSSI_RX1 = "rssi_rx1";

	/** The Constant LATITUDE. */
	public static final String LATITUDE = "lat";
	/** The Constant LONGITUDE. */
	public static final String LONGITUDE = "lon";

	/** The Constant SPATIAL_RANK. */
	public static final String SPATIAL_RANK = "sptlRnk";

	/** The Constant PMI_INDEX. */
	public static final String PMI_INDEX = "pmiI";

	/** The Constant B173_LOGCODE. */
	public static final Integer B173_LOGCODE = 45427;

	/** The Constant PUCCH_REPORTING_MODE. */
	public static final String PUCCH_REPORTING_MODE = "pcchRMde";

	/** The Constant NUM_RBS. */
	public static final String NUM_RBS = "numrbs";

	/** The Constant CARRIER_INDEX. */
	public static final String CARRIER_INDEX = "crrrIndx";

	/** The Constant CQI. */
	public static final String CQI = "cqi";

	/** The Constant QMDL_SOURCE_FILE_PATH. */
	public static final String QMDL_SOURCE_FILE_PATH = "QMDL_SOURCE_FILE_PATH";

	/** The Constant QMDL_FILE_PROCESS_ERROR_MSG. */
	public static final String QMDL_FILE_PROCESS_ERROR_MSG = "{\"result\":\"failure\",\"messaage\":\"problem in replacing and removing file Data\"}";

	/** The Constant QMDL_FILE_PROCESSING_ERROR_MSG. */
	public static final String QMDL_FILE_PROCESSING_ERROR_MSG = "{\"result\":\"failure\",\"messaage\":\"problem in Parsing file\"}";

	/** The Constant B193_LOG_CODE. */
	public static final Integer B193_LOG_CODE = 45459;

	/** The Constant B193_LOG_CODE. */
	public static final Integer B063_LOG_CODE = 45155;

	/** The Constant LAT_LONG_LOG_CODE. */
	public static final Integer LAT_LONG_LOG_CODE = 5238;

	/** The Constant TIMESTAMP. */
	public static final String TIMESTAMP = "ts";

	/** The Constant PADDINGSTRING. */
	public static final String PADDINGSTRING = "000";

	/** The Constant PADDING_LENGTH. */
	public static final int PADDING_LENGTH = 3;

	/** The Constant PADDING_VAR. */
	public static final char PADDING_VAR = '0';

	/** The Constant DELIMETER. */
	public static final String DELIMETER = "_";

	
	/** The Constant FAIL. */
	public static final String FAIL = "FAIL";

	/** The Constant REJECT. */
	public static final String REJECT = "REJECT";

	/** The Constant SINR_MAX. */
	public static final Integer SINR_MAX = 30;

	/** The Constant SINR_MIN. */
	public static final Integer SINR_MIN = -20;

	/** The Constant DECIMAL_PATTERN. */
	public static final String DECIMAL_PATTERN = "#";

	/** The Constant WINDOWS_SIZE_B193. */
	public static final Integer WINDOWS_SIZE_B193 = 1;

	/** The Constant PCI_MAX. */
	public static final Integer PCI_MAX = 504;

	/** The Constant PCI_MIN. */
	public static final Integer PCI_MIN = 0;

	/** The Constant RSSI_MAX. */
	public static final Integer RSSI_MAX = -10;

	/** The Constant RSSI_MIN. */
	public static final Integer RSSI_MIN = -110;

	/** The Constant RSRP_MAX. */
	public static final Integer RSRP_MAX = -40;

	/** The Constant RSRP_MIN. */
	public static final Integer RSRP_MIN = -140;

	/** The Constant RSRQ_MAX. */
	public static final Integer RSRQ_MAX = 0;

	/** The Constant RSRQ_MIN. */
	public static final Integer RSRQ_MIN = -30;

	/** The Constant RANGE_STATS. */
	public static final String RANGE_STATS = "rs";

	/** The Constant PUSCH_TX_POWER_MAX. */
	public static final Integer PUSCH_TX_POWER_MAX = 23;

	/** The Constant PUSCH_TX_POWER_MIN. */
	public static final Integer PUSCH_TX_POWER_MIN = -50;

	/** The Constant OUT_OF_SYNC_BLER_MAX. */
	public static final Integer OUT_OF_SYNC_BLER_MAX = 1000;

	/** The Constant OUT_OF_SYNC_BLER_MIN. */
	public static final Integer OUT_OF_SYNC_BLER_MIN = 0;

	/** The Constant TARGET_PHYSICAL_CELL_ID. */
	public static final String TARGET_PHYSICAL_CELL_ID = "tpci";

	/** The Constant DL_DCCH_MESSSAGE. */
	public static final String DL_DCCH_MESSSAGE = "DL_DCCH_MESSAGE";

	/** The Constant UL_DCCH_MESSSAGE. */
	public static final String UL_DCCH_MESSSAGE = "UL_DCCH_MESSAGE";

	/** The Constant TIMESTAMP_INDEX. */
	public static final int TIMESTAMP_INDEX = 1;

	/** The Constant HANDOVER_TIME. */
	public static final String HANDOVER_TIME = "hTime";


	/** The Constant PCI_END_STRING. */
	public static final String PCI_END_STRING = ",";

	/** The Constant HANDOVER_START_TIME. */
	public static final String HANDOVER_START_TIME = "hsTime";

	/** The Constant HANDOVER_END_TIME. */
	public static final String HANDOVER_END_TIME = "heTime";

	/** The Constant JSON. */
	public static final String JSON = "json";

	/** The Constant DIRECTION. */
	public static final String DIRECTION = "direction";

	/** The Constant MESSAGE_ID. */
	public static final String MESSAGE_ID = "msg";

	/** The Constant RESPONSECODE. */
	public static final String RESPONSECODE = "rcode";

	/** The Constant STATUS. */
	public static final String STATUS = "status";

	/** The Constant PASS. */
	public static final Object PASS = "Pass";

	/** The Constant NETWORK_TO_UE. */
	public static final String NETWORK_TO_UE = "Network to UE";

	/** The Constant IMS_SIP_INVITE. */
	public static final String IMS_SIP_INVITE = "IMS SIP INVITE";
	
	public static final String IMS_SIP_REGISTER = "IMS SIP REGISTER";

	public static final String MESSAGE_200_OK = "200 OK";

	/** The Constant UE_TO_NETWORK. */
	public static final String UE_TO_NETWORK = "UE to network";

	/** The Constant IMS_SIP_ACK. */
	public static final String IMS_SIP_ACK = "IMS SIP ACK";
	
	public static final String IMS_SIP_BYE="IMS SIP BYE";
	
	public static final String IMS_SIP_CANCEL="IMS SIP CANCEL";
	
	public static final String HOINIT_MSG="HOINIT_MSG";
	public static final String HOFAIL_MSG="HOFAIL_MSG";
	public static final String MSG3_MSG="MSG3";
	
	public static final Long MAX_HANDOVER_INTERRUPTION_TIME = 5000L;


	/** The Constant REQUEST. */
	public static final String REQUEST = "request";

	/** The Constant TRYING_100. */
	public static final String TRYING_100 = "100 Trying";

	/** The Constant SESSION_PROGRESS_183. */
	public static final String SESSION_PROGRESS_183 = "183 Session Progress";

	/** The Constant RINGING_180. */
	public static final String RINGING_180 = "180 Ringing";

	/** The Constant MO_EVENT_CONSTANT. */
	public static final Integer MO_EVENT_CONSTANT=1;

	/** The Constant MT_EVENT_CONSTANT. */
	public static final Integer MT_EVENT_CONSTANT=2;

	/** The Constant HANDOVER_EVENT_CONSTANT. */
	public static final Integer HANDOVER_EVENT_CONSTANT=3;

	/** The Constant E156_LOGCODE. */
	public static final Integer E156_LOGCODE = 5486;
	
	/** The Constant B167_LOGCODE. */
	public static final Integer B167_LOGCODE = 45415;


	/** The Constant UL_EARFCN_MIN. */
	public static final Integer UL_EARFCN_MIN = 0;

	/** The Constant UL_EARFCN_MAX. */
	public static final Integer UL_EARFCN_MAX = 65535;

	/** The Constant DL_EARFCN_MIN. */
	public static final Integer DL_EARFCN_MIN = 0;

	/** The Constant DL_EARFCN_MAX. */
	public static final Integer DL_EARFCN_MAX = 65535;

	/** The Constant RI_MAX. */
	public static final Integer RI_MAX = 2;

	/** The Constant RI_MIN. */
	public static final Integer RI_MIN = 0;

	/** The Constant SPATIALRANK_MAX. */
	public static final Integer SPATIALRANK_MAX = 3;

	/** The Constant SPATIALRANK_MIN. */
	public static final Integer SPATIALRANK_MIN = 0;

	/** The Constant PMI_MAX. */
	public static final Integer PMI_MAX = 15;

	/** The Constant PMI_MIN. */
	public static final Integer PMI_MIN = 0;

	/** The Constant NUMRB_MAX. */
	public static final Integer NUMRB_MAX = 110;

	/** The Constant NUMRB_MIN. */
	public static final Integer NUMRB_MIN = 0;

	/** The Constant MCS_MAX. */
	public static final Integer MCS_MAX = 31;

	/** The Constant MCS_MIN. */
	public static final Integer MCS_MIN = 0;

	/** The Constant CARRIER_INDEX_MAX. */
	public static final Integer CARRIER_INDEX_MAX = 1;

	/** The Constant CARRIER_INDEX_MIN. */
	public static final Integer CARRIER_INDEX_MIN = 0;

	/** The Constant TAC_MAX. */
	public static final Integer TAC_MAX = 65535;

	/** The Constant TAC_MIN. */
	public static final Integer TAC_MIN = 0;



	/** The Constant NETWORK_TYPE_4G. */
	public static final String NETWORK_TYPE_4G = "LTE";

	/** The Constant NETWORK_TYPE_3G. */
	public static final String NETWORK_TYPE_3G = "3G";

	/** The Constant NETWORK_TYPE_2G. */
	public static final String NETWORK_TYPE_2G = "2G";

	/** The Constant NETWORK_TYPE_NONE. */
	public static final String NETWORK_TYPE_NONE = "NONE";

	/** The Constant NETWORK_TYPE_LTE. */
	public static final String NETWORK_TYPE_LTE = "LTE";

	/** The Constant NETWORK_TYPE_WIFI. */
	public static final String NETWORK_TYPE_WIFI = "WIFI";

	/** The Constant DOWNLOAD_4G_ID. */
	public static final int DOWNLOAD_4G_ID = 1;

	/** The Constant DOWNLOAD_3G_ID. */
	public static final int DOWNLOAD_3G_ID = 2;

	/** The Constant DOWNLOAD_2G_ID. */
	public static final int DOWNLOAD_2G_ID = 3;

	/** The Constant UPLOAD_4G_ID. */
	public static final int UPLOAD_4G_ID = 4;

	/** The Constant UPLOAD_3G_ID. */
	public static final int UPLOAD_3G_ID = 5;

	/** The Constant UPLOAD_2G_ID. */
	public static final int UPLOAD_2G_ID = 6;

	/** The Constant BROWSE_4G_ID. */
	public static final int BROWSE_4G_ID = 7;

	/** The Constant BROWSE_3G_ID. */
	public static final int BROWSE_3G_ID = 8;

	/** The Constant BROWSE_2G_ID. */
	public static final int BROWSE_2G_ID = 9;

	/** The Constant PING_4G_ID. */
	public static final int PING_4G_ID = 10;

	/** The Constant PING_3G_ID. */
	public static final int PING_3G_ID = 11;

	/** The Constant PING_2G_ID. */
	public static final int PING_2G_ID = 12;

	/** The Constant NETWORK_TYPE_NONE_ID. */
	public static final int NETWORK_TYPE_NONE_ID = 19;

	/** The Constant NETWORK_TYPE_UNKNOWN_ID. */
	public static final int NETWORK_TYPE_UNKNOWN_ID = 0;

	/** The Constant LATITUDE_4G_INDEX. */
	public static final int LATITUDE_4G_INDEX = 14;

	/** The Constant LONGITUDE_4G_INDEX. */
	public static final int LONGITUDE_4G_INDEX = 15;

	/** The Constant TIMESTAMP_4G_INDEX. */
	public static final int TIMESTAMP_4G_INDEX = 17;

	/** The Constant LATITUDE_3G_INDEX. */
	public static final int LATITUDE_3G_INDEX = 10;

	/** The Constant LONGITUDE_3G_INDEX. */
	public static final int LONGITUDE_3G_INDEX = 11;

	/** The Constant TIMESTAMP_3G_INDEX. */
	public static final int TIMESTAMP_3G_INDEX = 13;

	/** The Constant DL_INDEX. */
	public static final int DL_INDEX = 2;

	/** The Constant UL_INDEX. */
	public static final int UL_INDEX = 2;

	/** The Constant LATITUDE_2G_INDEX. */
	public static final int LATITUDE_2G_INDEX = 9;

	/** The Constant LONGITUDE_2G_INDEX. */
	public static final int LONGITUDE_2G_INDEX = 10;

	/** The Constant TIMESTAMP_2G_INDEX. */
	public static final int TIMESTAMP_2G_INDEX = 12;

	/** The Constant HOST_INDEX. */
	public static final int HOST_INDEX = 2;

	/** The Constant LATENCY_INDEX. */
	public static final int LATENCY_INDEX = 3;

	/** The Constant JITTER_INDEX. */
	public static final int JITTER_INDEX = 4;

	/** The Constant PING_4G_LATITUDE_INDEX. */
	public static final int PING_4G_LATITUDE_INDEX = 16;

	/** The Constant PING_4G_LONGITUDE_INDEX. */
	public static final int PING_4G_LONGITUDE_INDEX = 17;

	/** The Constant PING_4G_TIMESTAMP_INDEX. */
	public static final int PING_4G_TIMESTAMP_INDEX = 19;

	/** The Constant PING_3G_LATITUDE_INDEX. */
	public static final int PING_3G_LATITUDE_INDEX = 12;

	/** The Constant PING_3G_LONGITUDE_INDEX. */
	public static final int PING_3G_LONGITUDE_INDEX = 13;

	/** The Constant PING_2G_LATITUDE_INDEX. */
	public static final int PING_2G_LATITUDE_INDEX = 11;

	/** The Constant PING_2G_LONGITUDE_INDEX. */
	public static final int PING_2G_LONGITUDE_INDEX = 12;

	/** The Constant PING_2G_TIMESTAMP_INDEX. */
	public static final int PING_2G_TIMESTAMP_INDEX = 14;

	/** The Constant PING_3G_TIMESTAMP_INDEX. */
	public static final int PING_3G_TIMESTAMP_INDEX = 15;

	/** The Constant UL_THROUGHTPUT. */
	public static final String UL_THROUGHTPUT = "ultpt";

	/** The Constant DL_THROUGHTPUT. */
	public static final String DL_THROUGHTPUT="dltpt";

	/** The Constant LATENCY. */
	public static final String LATENCY = "ltncy";

	/** The Constant UL_THROUGHTPUT_MAX. */
	public static final Integer UL_THROUGHTPUT_MAX = 500;

	/** The Constant UL_THROUGHTPUT_MIN. */
	public static final Integer UL_THROUGHTPUT_MIN = 0;

	/** The Constant DL_THROUGHTPUT_MAX. */
	public static final Integer DL_THROUGHTPUT_MAX = 500;

	/** The Constant DL_THROUGHTPUT_MIN. */
	public static final Integer DL_THROUGHTPUT_MIN = 0;

	/** The Constant LATENCY_MIN. */
	public static final Integer LATENCY_MIN = 0;

	/** The Constant LATENCY_MAX. */
	public static final Integer LATENCY_MAX = 1000;

	/** The Constant WINDOWS_SIZE_CSV. */
	public static final Integer WINDOWS_SIZE_CSV = 1;

	/** The Constant EMPTY_ARRAY_SIZE. */
	public static final int EMPTY_ARRAY_SIZE = 0;

	/** The Constant EMPTY_STRING. */
	public static final String EMPTY_STRING = "";

	/** The Constant MINIMUM_LENTH_OF_CSV_LINE. */
	public static final int MINIMUM_LENTH_OF_CSV_LINE = 0;

	/** The Constant CSV_VALUE_COUNT. */
	public static final Double CSV_VALUE_COUNT = 1D;

	//CsvParsing Constant

	/** The Constant MIN_TIME_STAMP. */
	public static final String MIN_TIME_STAMP = "minTs";

	/** The Constant MAX_TIME_STAMP. */
	public static final String MAX_TIME_STAMP = "maxTs";

	/** The Constant KPI_ARRAY_LIST_SIZE. */
	public static final int KPI_ARRAY_LIST_SIZE = 2;

	/** The Constant KPI_COUNT_INDEX. */
	public static final int KPI_COUNT_INDEX = 0;

	/** The Constant KPI_AVG_INDEX. */
	public static final int KPI_AVG_INDEX = 1;

	/** The Constant WORK_ORDER_ID_INDEX. */
	public static final int WORK_ORDER_ID_INDEX = 5;

	/** The Constant INVALID_FILE_NAME. */
	public static final String INVALID_FILE_NAME = "{\"result\":\"Invalid File Name\"}";

	/** The Constant DECIMAL_PATTERN_FOR_INT. */
	public static final String DECIMAL_PATTERN_FOR_INT = "#";

	/** The Constant ZERO_MILLI_SECOND. */
	public static final int ZERO_MILLI_SECOND = 000;

	/** The Constant ZERO_SECOND. */
	public static final int ZERO_SECOND = 00;

	/** The Constant ZERO_INDEX. */
	public static final int ZERO_INDEX = 0;

	/** The Constant VALUE_INDEX. */
	public static final int VALUE_INDEX = 0;

	/** The Constant COUNT_INDEX. */
	public static final int COUNT_INDEX = 1;

	/** The Constant INITIAL_VALUE_COUNT. */
	public static final Double INITIAL_VALUE_COUNT = 1.0;

	/** The Constant ZERO_ELEMENT. */
	public static final Double ZERO_ELEMENT = 0.0;

	/** The Constant AVERAGE_KPI_ARRAY_SIZE. */
	public static final int AVERAGE_KPI_ARRAY_SIZE = 2;

	/** The Constant QMDL_FILE_TYPE. */
	public static final String QMDL_FILE_TYPE = "qmdl";

	public static final String ZIP_FILE_TYPE = "zip";

	/** The Constant CSV_FILE_TYPE. */
	public static final String CSV_FILE_TYPE = "csv";

	/** The Constant BUFFER_SIZE. */
	public static final int BUFFER_SIZE = 5120000;

	/** The Constant LOG_CODE_LENGTH. */
	public static final int LOG_CODE_LENGTH = 5;

	/** The Constant LOG_CODE_DELIMETER. */
	public static final char LOG_CODE_DELIMETER = 0x7e;

	/** The Constant LOG_CODE_HEX_LENGTH. */
	public static final int LOG_CODE_HEX_LENGTH = 10;

	/** The Constant LOGCODE_1476_BYTES_READ. */
	public static final int LOGCODE_1476_BYTES_READ = 80;

	/** The Constant DEVICE_VERSION_INDEX. */
	public static final int DEVICE_VERSION_INDEX = 17;

	/** The Constant LOGCODE_1476_START_INDEX. */
	public static final int LOGCODE_1476_START_INDEX = 8;

	/** The Constant LOGCODE_1476_END_INDEX. */
	public static final int LOGCODE_1476_END_INDEX = 7;

	/** The Constant TRUE. */
	public static final String TRUE = "true";

	/** The Constant FALSE. */
	public static final String FALSE = "false";

	/** The Constant DATA_HOLDER. */
	public static final String DATA_HOLDER = "dataHolder";

	/** The Constant SKIP_BYTES_FOR_DEVICE_VERSION_7. */
	public static final int SKIP_BYTES_FOR_DEVICE_VERSION_7 = 4;

	/** The Constant VALUE_READ_FOR_DEVICE_VERSION_7. */
	public static final int VALUE_READ_FOR_DEVICE_VERSION_7 = 53;

	/** The Constant NEED_TO_CHECK_DATA_HOLDER. */
	public static final String NEED_TO_CHECK_DATA_HOLDER = "oldDataCheck";

	/** The Constant HASH. */
	public static final String HASH = "#";

	/** The Constant MSG_END_STRING. */
	public static final String MSG_END_STRING = ",#";

	/** The Constant COMMA. */
	public static final String COMMA = ",";

	/** The Constant MSG_START_STRING_LENTH. */
	public static final int MSG_START_STRING_LENTH = 1;

	/** The Constant TWO. */
	public static final int TWO = 2;

	/** The Constant ONE. */
	public static final int ONE = 1;

	/** The Constant DECIMAL_CONVERT_MULTIPLER. */
	public static final int DECIMAL_CONVERT_MULTIPLER = 16;

	/** The Constant EMPTY_STRING_BUILDER_LENGTH. */
	public static final int EMPTY_STRING_BUILDER_LENGTH = 0;

	/** The Constant HEX_CODE_STRING_LENGTH. */
	public static final int HEX_CODE_STRING_LENGTH = 8;

	/** The Constant MAX_SIZE_OF_STRING. */
	public static final int MAX_SIZE_OF_STRING = 9;

	/** The Constant EMPTY_BUFFER. */
	public static final int EMPTY_BUFFER = -1;

	/** The Constant RAW_DATA. */
	public static final String RAW_DATA = "rawData";

	/** The Constant KPI_DATA. */
	public static final Object KPI_DATA = "kpiData";

	/** The Constant STRING_START_INDEX. */
	public static final int STRING_START_INDEX = 0;

	/** The Constant JSONSTRING. */
	public static final String JSONSTRING = "json";

	/** The Constant HANDOVER_INITIATE_STRING. */
	public static final String HANDOVER_INITIATE_STRING = "measurementReport";

	/** The Constant HANDOVER_SUCCESS_STRING. */
	public static final String HANDOVER_SUCCESS_STRING = "rrcConnectionReconfigurationComplete";

	/** The Constant HANDOVER_MSG_TYPE. */
	public static final String HANDOVER_MSG_TYPE = "rrcConnectionReconfiguration";

	/** The Constant HANDOVER_INTERMEDIATE_STRING. */
	public static final String HANDOVER_INTERMEDIATE_STRING = "mobilityControlInfo";

	/** The Constant CALL_SUCCESS_STRING. */
	public static final String CALL_SUCCESS_STRING = "480";

	/** The Constant CALL_DROP_STRING. */
	public static final String CALL_DROP_STRING = "487";

	/** The Constant CALL_FAILURE_TYPE1. */
	public static final String CALL_FAILURE_TYPE1 = "480";

	/** The Constant CALL_FAILURE_TYPE2. */
	public static final String CALL_FAILURE_TYPE2 = "487";

	/** The Constant LOGCODE_1830. */
	public static final int LOGCODE_1830 = 6192;

	/** The Constant CALL_SUCCESS_CODE. */
	public static final int CALL_SUCCESS_CODE = 200;

	/** The Constant HANDOVER_FAILURE_EVENT. */
	public static final String HANDOVER_FAILURE_EVENT = "HandOver Failure";

	/** The Constant HANDOVER_SUCCESS_EVENT. */
	public static final String HANDOVER_SUCCESS_EVENT = "HandOver SuccessFul";

	/** The Constant HANDOVER_INITIATE_EVENT. */
	public static final String HANDOVER_INITIATE_EVENT = "HandOver Initiate";

	/** The Constant CALL_SUCCESS_EVENT. */
	public static final String CALL_SUCCESS_EVENT = "Call SuccessFul";

	/** The Constant CALL_DROP_EVENT. */
	public static final String CALL_DROP_EVENT = "Call Drop";

	/** The Constant CALL_FAILURE_EVENT. */
	public static final String CALL_FAILURE_EVENT = "Call Failure";

	/** The Constant CALL_INITIATE_EVENT. */
	public static final String CALL_INITIATE_EVENT = "Call Initiate";

	/** The Constant FIRST_RECORD_INDEX. */
	public static final int FIRST_RECORD_INDEX = 0;

	/** The Constant CELL_CHANGE_EVENT. */
	public static final String CELL_CHANGE_EVENT = "cell Change";

	/** The Constant RECORD_COUNT. */
	public static final Integer RECORD_COUNT = 1;

	/** The Constant EVENT_JSON. */
	public static final String EVENT_JSON = "evnt";

	/** The Constant SUMMARY_JSON. */
	public static final String SUMMARY_JSON = "smmry";
	
	/** The Constant SUMMARY_JSON_3G. */
	public static final String SUMMARY_JSON_3G = "smmry_3g";
	
	/** The Constant SUMMARY_JSON_2G. */
	public static final String SUMMARY_JSON_2G = "smmry_2g";

	/** The Constant B0C0_MSG_TYPE. */
	public static final String B0C0_MSG_TYPE = "b0c0MType";

	public static final String LTE_ML1 = "LTE ML1";
	public static final String MSG = "MSG";
	public static final String UMTS_NAS = "UMTS NAS";
	
	public static final String GSM_RR = "GSM RR";
	
	public static final String B167_MSG_TYPE="LTE ML1 random access request (MSG1) report";
	public static final String B168_MSG_TYPE="LTE ML1 random access response (MSG2) report";
	public static final String B169_MSG_TYPE="LTE ML1 UE identification message (MSG3) report";
	public static final String B16A_MSG_TYPE="LTE ML1 contention resolution message (MSG4) report";

	public static final String B0A4_MSG_TYPE="LTE PDCP DL statistics(PDCP DL)";
	public static final String B0B4_MSG_TYPE="LTE PDCP UL statistics(PDCP UL)";
 
	/** The Constant B0E0_LOGCODE. */
	public static final Integer B0E0_LOGCODE = 45280;

	/** The Constant EMS_MESSAGE_B0E0. */
	public static final String EMS_MESSAGE_B0E0 = "LTE NAS ESM security protected OTA incoming msg";

	/** The Constant EMS_MESSAGE. */
	public static final String EMS_MESSAGE = "emsMType";

	/** The Constant EMS_BEAN. */
	public static final String EMS_BEAN = "esm";

	/** The Constant B0E1_LOGCODE. */
	public static final Integer B0E1_LOGCODE = 45281;

	/** The Constant EMS_MESSAGE_B0E1. */
	public static final String EMS_MESSAGE_B0E1 = "LTE NAS ESM security protected OTA outgoing msg";

	/** The Constant B0E2_LOGCODE. */
	public static final Integer B0E2_LOGCODE = 45282;

	/** The Constant B0E3_LOGCODE. */
	public static final Integer B0E3_LOGCODE = 45283;

	/** The Constant EMS_MESSAGE_B0E2. */
	public static final String EMS_MESSAGE_B0E2 = "LTE NAS ESM plain OTA incoming msg";

	/** The Constant EMS_MESSAGE_B0E3. */
	public static final String EMS_MESSAGE_B0E3 = "LTE NAS ESM plain OTA outgoing msg";

	/** The Constant B0E4_LOGCODE. */
	public static final Integer B0E4_LOGCODE = 45284;

	/** The Constant B0E5_LOGCODE. */
	public static final Integer B0E5_LOGCODE = 45285;

	/** The Constant EMS_MESSAGE_B0E4. */
	public static final String EMS_MESSAGE_B0E4 = "LTE NAS ESM Bearer Context state";

	/** The Constant EMS_MESSAGE_B0E5. */
	public static final String EMS_MESSAGE_B0E5 = "LTE NAS ESM bearer context info";

	/** The Constant B0EA_LOGCODE. */
	public static final Integer B0EA_LOGCODE = 45290;

	/** The Constant EMS_MESSAGE_B0EA. */
	public static final String EMS_MESSAGE_B0EA = "LTE NAS EMM security protected OTA incoming msg";

	/** The Constant B0EB_LOGCODE. */
	public static final Integer B0EB_LOGCODE = 45291;

	/** The Constant EMS_MESSAGE_B0EB. */
	public static final String EMS_MESSAGE_B0EB = "LTE NAS EMM security protected OTA outgoing msg";

	/** The Constant B0EC_LOGCODE. */
	public static final Integer B0EC_LOGCODE = 45292;

	/** The Constant EMS_MESSAGE_B0EC. */
	public static final String EMS_MESSAGE_B0EC = "LTE NAS EMM plain OTA incoming msg";

	/** The Constant B0ED_LOGCODE. */
	public static final Integer B0ED_LOGCODE = 45293;
	
	/** The Constant 1568_LOGCODE. */
	public static final Integer LOGCODE_1568 = 5480;
	
	
	/** The Constant B0A4_LOGCODE. */
	public static final Integer B0A4_LOGCODE = 45220;
	
	/** The Constant B0B4_LOGCODE. */
	public static final Integer B0B4_LOGCODE = 45236;

	/** The Constant B087_LOGCODE. */
	public static final Integer B087_LOGCODE = 45191;
	
	/** The Constant B097_LOGCODE. */
	public static final Integer B097_LOGCODE = 45207;
	
	
	/** The Constant LOGCODE_1569. */
	public static final Integer LOGCODE_1569 = 5481;
	
	/** The Constant LOGCODE_156A. */
	public static final Integer LOGCODE_156A = 5482;
	
	/** The Constant EMS_MESSAGE_B0ED. */
	public static final String EMS_MESSAGE_B0ED = "LTE NAS EMM plain OTA outgoing msg";

	/** The Constant B0EE_LOGCODE. */
	public static final Integer B0EE_LOGCODE = 45294;

	/** The Constant EMS_MESSAGE_B0EE. */
	public static final String EMS_MESSAGE_B0EE = "LTE NAS EMM state";
	
	public static final String EMM_REGISTERED   ="EMM REGISTERED";

	/** The Constant SIP_EVENT. */
	public static final String SIP_EVENT = "sEvnt";

	/** The Constant EMM_MESSAGE. */
	public static final String EMM_MESSAGE = "emmMTyp";

	/** The Constant EMM_BEAN. */
	public static final String EMM_BEAN = "emm";
	

	public static final String EMM_DIRECTION = "emmDirection";
	public static final String EMS_DIRECTION = "emsDirection";
	public static final String SIP_DIRECTION = "sipDirection";
	public static final String B0C0_DIRECTION = "b0c0Direction";

	/** The Constant HANDOVER_CHECK. */
	public static final Object HANDOVER_CHECK = "handOver";

	public static final String HANDOVER = "handOver";

	/** The Constant DOUBLEVALUE_ROUND. */
	public static final String DOUBLEVALUE_ROUND = "#.##";
	
	/** The Constant INTEGERVALUE_ROUND. */
	public static final String INTEGERVALUE_ROUND = "#";
	
	
	/** The Constant DOUBLEVALUE_ROUND. */
	public static final String DOUBLEVALUE_ROUND_TO_FOUR_DECIMAL = "#.####";

	/** The Constant ENCODE_PREFIX. */
	public static final String ENCODE_PREFIX = "\"'EncodeMsg':'";

	/** The Constant ENCODE_POSTFIX. */
	public static final String ENCODE_POSTFIX = "'";

	/** The Constant DECODE_PREFIX. */
	public static final String DECODE_PREFIX = "'DecodeMsg':'";

	/** The Constant DECODE_POSTFIX. */
	public static final String DECODE_POSTFIX = "'\"";

	/** The Constant ROWKEY. */
	public static final String ROWKEY = "rowkey";

	/** The Constant MSG_TYPE. */
	public static final String MSG_TYPE = "msgType";

	/** The Constant KPI. */
	public static final String KPI = "KPI";

	/** The Constant SINGLE_QUOTES. */
	public static final String SINGLE_QUOTES = "'";

	/** The Constant RRC_INITIATE_MSG. */
	public static final String RRC_INITIATE_MSG = "rrcConnectionRequest";

	/** The Constant RRC_SUCESS_MSG. */
	public static final String RRC_SUCESS_MSG ="rrcConnectionSetupComplete";

	/** The Constant UL_CCCH_MESSSAGE. */
	public static final String UL_CCCH_MESSSAGE = "UL_CCCH_MESSAGE";

	/** The Constant RRC_CONNECTION_SETUP_COMPLETE. */
	public static final String RRC_CONNECTION_SETUP_COMPLETE = "rrcConnectionSetupComplete";

	/** The Constant RRC_CONNECTION_SETUP_COMPLETE_INDEX. */
	public static final int RRC_CONNECTION_SETUP_COMPLETE_INDEX = 1;

	/** The Constant SECURITY_MODE_COMMAND. */
	public static final String SECURITY_MODE_COMMAND_MSG = "securityModeCommand";

	/** The Constant SECURITY_MODE_COMPLETE. */
	public static final String SECURITY_MODE_COMPLETE_MSG = "securityModeComplete";

	/** The Constant SECURITY_MODE_COMMAND_INDEX. */
	public static final int SECURITY_MODE_COMMAND_INDEX = 2;

	/** The Constant SECURITY_MODE_COMPLETE_INDEX. */
	public static final int SECURITY_MODE_COMPLETE_INDEX = 3;

	/** The Constant RRC_CONNECTION_CONFIGURATION_COMPLETE. */
	public static final String RRC_CONNECTION_CONFIGURATION_COMPLETE = "rrcConnectionReconfigurationComplete";

	/** The Constant RRC_CONNECTION_CONFIGURATION_COMPLETE_INDEX. */
	public static final int RRC_CONNECTION_CONFIGURATION_COMPLETE_INDEX = 4;

	/** The Constant ATTACH_REQUEST. */
	public static final String ATTACH_REQUEST = "Attach request";

	/** The Constant TAU_REQUEST_MESSAGE. */
	public static final String TAU_REQUEST_MESSAGE = "tracking area update request";
	public static final String ACTIVATE_DEDICATED_EPS_BEARER_CONTEXT_REQUEST="Activate dedicated EPS bearer context request";
	
	public static final String ACTIVATE_DEDICATED_EPS_BEARER_CONTEXT_ACCEPT="Activate dedicated EPS bearer context accept";
	
	public static final String PDN_CONNECTIVITY_REQUEST="PDN connectivity request";
	
	public static final String DEACTIVATE_EPS_BEARER_CONTEXT_REQUEST="Deactivate EPS bearer context request";
	
	/** The Constant TAU_COMPLETE_MESSAGE. */
	public static final String TAU_COMPLETE_MESSAGE = "tracking area update complete";
	
	/** The Constant TAU_REJECT_MESSAGE. */
	public static final String TAU_REJECT_MESSAGE = "tracking area update reject";


	/** The Constant ATTACH_REQUEST_INDEX. */
	public static final int ATTACH_REQUEST_INDEX = 0;

	/** The Constant ATTACH_ACCEPT. */
	public static final String ATTACH_ACCEPT = "Attach accept";

	/** The Constant ATTACH_ACCEPT_INDEX. */
	public static final int ATTACH_ACCEPT_INDEX = 5;
  
  /** The Constant ATTACH_ACCEPT_INDEX_VALUE. */
  public static final int ATTACH_ACCEPT_INDEX_VALUE = 191;
  
	/** The Constant ATTACH_REQUEST_INDEX_VALUE. */
	public static final int ATTACH_REQUEST_INDEX_VALUE = 192;
	
	/** The Constant DETACH_REQUEST_INDEX_VALUE. */
	public static final int DETACH_REQUEST_INDEX_VALUE = 185;
	
	/** The Constant RESELECTION_SUCCESS_INDEX_VALUE. */
	public static final int RESELECTION_SUCCESS_INDEX_VALUE = 190;
	
	public static final int HANDOVER_INTERRUPTION_MIN_TIME_INDEX = 186;
	
	public static final int HANDOVER_INTERRUPTION_MAX_TIME_INDEX = 187;
	
	public static final int HANDOVER_INTERRUPTION_AVG_TIME_INDEX_SUM = 188;
	public static final int HANDOVER_INTERRUPTION_AVG_TIME_INDEX_COUNT = 189;	
	
	
	public static final int HANDOVER_INTERRUPTION_AVG_TIME_QCI1_INDEX_SUM = 188;
	public static final int HANDOVER_INTERRUPTION_AVG_TIME_QCI1_INDEX_COUNT = 189;	

	public static final int ATTACH_COMPLETE_VALUE_INDEX = 205;
	
	/** The Constant ATTACH_COMPLETE. */
	public static final String ATTACH_COMPLETE = "Attach complete";

	/** The Constant ATTACH_COMPLETE_INDEX. */
	public static final int ATTACH_COMPLETE_INDEX = 6;

	/** The Constant DETACH_REQUEST. */
	public static final String DETACH_REQUEST = "detach request";

	/** The Constant DETACH_REQUEST_INDEX. */
	public static final int DETACH_REQUEST_INDEX = 7;

	/** The Constant DETACH_ACCEPT. */
	public static final String DETACH_ACCEPT = "Detach accept";

	/** The Constant DETACH_ACCEPT_INDEX. */
	public static final int DETACH_ACCEPT_INDEX = 8;

	/** The Constant PCI_WISE_DL_RATE_JSON_QUALIFIER. */
	public static final String PCI_WISE_DL_RATE_JSON_QUALIFIER = "pciWiseDlRate";

	/** The Constant INITIAL_MAX_DL_THROUGHPUT. */
	public static final Double INITIAL_MAX_DL_THROUGHPUT = 0.0D;

	/** The Constant LOG_CODE_1831. */
	public static final Integer LOG_CODE_1831 = 6193;
	

	/** The Constant LOG_CODE_B179. */
	public static final Integer LOG_CODE_B179 = 45433;

	public static final Integer LOG_CODE_B0E2 = 45293;
	public static final Integer LOG_CODE_713A = 28986;
	public static final Integer LOG_CODE_412F = 16687;
	public static final Integer LOG_CODE_512F = 20783;
	public static final int BIT_COVERSION_CONSTANT = 0xff;

	public static final int LOG_CODE_START_INDEX = 2;

	public static final int LOG_CODE_END_INDEX = 3;

	
	public static final int REPLACE_7E60_END_VALUE = 0x60;

	public static final int LAT_LONG_LOG_CODE_START = 0x14;

	public static final byte LAT_LONG_LOG_CODE_END =  0x76;

	public static final Integer CQI_MAX = 15;
	public static final Integer CQI_MIN = 0;


	public static final String INVALID_ARGUMENT = "\"result\":\"Invalid Argument\"";
	public static final String B0C0_ENCODE_MSG = "b0c0EMsg";

	public static final int STRING_EXTRA_LENTH = 1;
	public static final String CSV_START_STRING = "{\"result\":\"[";
	public static final Object QUOTES = "\"";
	public static final String NO_RESULT_FOUND = "{\"result\":\"No Result Found\"}";
	public static final String RESULT = "{\"result\":";
	public static final String MSG_POSTFIX = "}";
	public static final Object RESPONSE_START_JSON = "{\"result\":\"";

	public static final Object SUMMARY_RESPONSE_START_JSON = "{\"result\":";

	public static final String STRING_5E = "5e";
	public static final String STRING_5D = "5d";
	public static final String STRING_7D = "7d";

	public static final Object STRING_7E = "7e";

	public static final String IMAGE_FILE_TYPE = ".jpeg";
	
	public static final String RSRP_THRESHOLD_IMG="RSRP";	
	public static final String SINR_THRESHOLD_IMG="SINR";

	public static final String LAYER3_REPORT_TABLE = "LAYER3_REPORT_TABLE";

	public static final int ROWKEY_LENGTH = 7;

	public static final String SIP_EVENT_MSG = "sipEvents";

	public static final String LIVE_DRIVE_PREFIX = "LIVE_DRIVE";

	public static final String OUTER_JSON_LIST_KEY = "spanlist";

	public static final String INNER_JSON_LIST_KEY = "positions";

	public static final String AVG_DL_JSON_KEY = "dlAvg";

	public static final String AVG_UL_JSON_KEY = "ulAvg";

	public static final String LATITUDE_JSON_KEY = "lat";

	public static final String LONGITUDE_JSON_KEY = "lng";

	public static final Object TIMESTAMP_JSON_KEY = "capturingTime";

	public static final Integer TDD_TECHNOLOGY_BAND_START = 32;

	public static final Integer TDD_TECHNOLOGY_BAND_END = 51;

	public static final String TDD_TECHNOLOGY = "TDD";

	public static final String FDD_TECHNOLOGY = "FDD";

	public static final String IMAGE_FILE = "img";

	public static final String CELL_ID = "cllId";

	public static final String PING_VALUE_HOLDER_JSON_KEY = "pingValueHolder";

	public static final String LATENCY_JSON_KEY = "avgLatency";
	
	public static final String SPEED_TEST_DL_RATE_JSON_KEY="speedTestDlRate";
	
	public static final String SPEED_TEST_UL_RATE_JSON_KEY="speedTestUlRate";

	public static final String GEOGRAPHY_COLUMN = "r:geographyName";

	public static final Integer GEOGRAPHY_LEVEL_4 = 4;

	public static final String GEOGRAPHY_GLOBAL = "PAN,0";

	public static final Integer GEOGRAPHY_LOWER_LEVEL_CONSTANT = 1;

	public static final Integer ZOOM_LEVEL = 0;

	public static final int OPERATOR_NAME = 3;

	public static final int RECEIPE_MAPPING_NAME = 6;

	public static final int START_TIME_INDEX = 0;

	public static final int END_TIME_INDEX = 1;

	public static final int RSSI_ARRAY_INDEX =2 ;

	public static final int GEOGRAPHY_NAME_INDEX = 42;

	public static final int GEOGRAPHY_LEVEL_INDEX = 43;

	public static final int ADDRESS_INDEX = 26;

	public static final int KPI_START_INDEX = 2;

	public static final int KPI_END_INDEX = 16;
	
	
	public static final int HITQCI_START_INDEX = 239;

	public static final int HITQCI_END_INDEX = 245;

	
	public static final int NEW_CALL_EVENT_START_INDEX = 245;

	public static final int NEW_CALL_EVENT_END_INDEX = 252;


	public static final int KPI_3G_START_INDEX = 0;

	public static final int KPI_3G_END_INDEX = 6;
	
	public static final int KPI_2G_START_INDEX = 0;

	public static final int KPI_2G_END_INDEX = 4;

	public static final int EVENT_START_INDEX = 16;

	public static final int EVENT_END_INDEX = 32;
	
	

	public static final int DEVICEINFO_START_INDEX = 32;

	public static final int DEVICEINFO_END_INDEX = 38;

	public static final int DERIVEINFO_START_INDEX = 38;

	public static final int DERIVEINFO_END_INDEX = 42;
	public static final String RECIPE = "recipe";


	public static final String OPERATOR = "operator";
	public static final int OPERATOR_INDEX = 3;

	public static final int SINGLE_RECORD = 1;


	public static final int RANK_INDEX_VALUE = 1;

	public static final Integer RANK_INDEX_MIN = 0;

	public static final Integer RANK_INDEX_MAX = 2;

	public static final Integer TRACKING_AREA_CODE_MIN = 0;

	public static final Integer TRACKING_AREA_CODE_MAX = 65535;

	public static final String INBUILDING_RAW_DATA_JSON_KEY = "listParameter";

	public static final String PCI_JSON_KEY = "pci";

	public static final String XPOINT = "xPoint";

	public static final String YPOINT = "yPoint";

	public static final String TIMESTAMP_INBUILDING_JSON_KEY = "timeStamp";

	public static final String FLOORPLAN_IMAGE_PREFIX = "FloorPlanImage";

	public static final String FLOOR_PLAN_IMAGE="fpi";

	public static final String NO_ACCESS_IMAGE_PREFIX = "NoAccessArea";

	public static final String FLOOR_PLAN_JSON = "floorplanjson";
	
	public static final String PROCESSED="PROCESSED";
	
	public static final String BAND = "band";

	public static final String NO_ACCESS_AREA_IMAGE_KEY = "noAccessAreaImageList";

	public static final String IMAGE_FILE_JSON_KEY = "imageFileName";

	public static final String IS_PORTRAIT_IMAGE = "isPortraitImage";

	public static final String INBUILDING_FILE_PREFIX = "surveyData";

	public static final Double IN_BUILDING_RSRP = -100.0;

	public static final Double IN_BUILDING_SINR = 12.0;

	public static final Double IN_BUILDING_DL = 5.0;

	public static final String IN_BUILDING_MAP_KEY = "recipeUnitIdMap";

	public static final String RANGE_2 = "2";

	public static final String FLOOR_PLAN_JSON_HBASEKEY = "fpj";

	public static final String NETWORK_TYPE = "technology";

	public static final String TECHNOLOGY = "technology";

	public static final String OPERATOR_NAME_JSON = "operatorName";

	public static final String NV_APP_VERSION = "nvAppVersion";

	public static final String MANUFACTURE_JSON = "manufactureBy";

	public static final String MODEL_JSON = "model";

	public static final String ANDROID_VERSION_JSON = "androidOs";

	public static final String CELL_ID_JSON = "cellId";

	public static final String JITTER_JSON_KEY = "avgJitter";
	
	public static final String JITTER_IB_JSON_KEY = "jitter";
	
	public static final String GOOGLE_BROWSE_TIME_JSON_KEY="url1BrowseTime";
	public static final String FACEBOOK_BROWSE_TIME_JSON_KEY="url2BrowseTime";
	public static final String YOUTUBE_BROWSE_TIME_JSON_KEY="url3BrowseTime";
	public static final String SPEED_TEST_PIN_NUMBER_JSON_KEY="speedTestPinNumber";
	


	public static final String BROWSE_HOLDER_JSON_KEY = "browseHolder";

	public static final String RESPONSE_TIME_JSON_KEY = "responseTime";

	public static final String NETWORK_PROVIDER_JSON = "networkProvider";


	public static final String NETWORK_TYPE_JSON = "networkType";

	public static final String DELIMETER_HASH = "#";

	public static final int SCRIPT_INDEX = 41;


	public static final String WORK_ORDER_ID_FILE_PATH = "SSVT_REPORT_PATH";

	public static final String WORK_ORDER_ID_FILE_PREFIX = "WORK_ORDER_ID_FILE_PREFIX";

	public static final int MAX_FILE_PROCESS = 3;

	public static final String SCRIPT_JSON = "script";

	public static final Integer ENOBEBID_ROUND_COSTANT = 256;

	public static final Integer SECTOR_ID_CONSTANT = 256;

	public static final String RECEIPE_ID = "recipeId";

	public static final String BACKGROUND_IMAGE_PREFIX = "Backgroundimage";

	public static final String BACKGROUND_IMAGE_FILE = "bimg";

	public static final String FILE_PATH = "pth";

	public static final String WIFI_RSSI_JSON = "wifiRssi";

	public static final String WIFI_SNR_JSON = "wifiSnr";

	public static final String WIFI_SSID_JSON = "ssid";
	
	public static final String WIFI_BSSID_JSON = "bssid";
	
	public static final String WIFI_CHANNEL_JSON = "channel";
	
	public static final String WIFI_LINKSPEED_JSON = "linkSpeed";

	public static final Double WIFI_SNR_THRESOLD =25.0;

	public static final Double WIFI_RSSI_THRESOLD =  -90.0;

	public static final String DISABLED = "Disabled";

	public static final String ENABLED = "Enabled";

	public static final int MIN_LATENCY_INDEX = 45;

	public static final int MAX_LATENCY_INDEX = 58;

	public static final int MIN_DL_INDEX = 47;

	public static final int MAX_DL_INDEX = 48;

	public static final int MIN_UL_INDEX = 49;

	public static final int MAX_UL_INDEX = 50;

	public static final int MIN_JITTER_INDEX = 51;

	public static final int MAX_JITTER_INDEX = 52;

	public static final int MIN_RESPONSE_TIME_INDEX = 53;

	public static final int MAX_RESPONSE_TIME_INDEX = 54;

	public static final int MIN_RSRP_INDEX = 55;

	public static final int MAX_RSRP_INDEX = 56;

	public static final int MIN_SINR_INDEX = 57;

	public static final int MAX_SINR_INDEX = 58;

	public static final int JITTER_SUM_INDEX = 71;

	public static final int JITTER_COUNT_INDEX = 60;

	public static final int JIITER_KPI_START_INDEX = JITTER_SUM_INDEX;

	public static final String QUICK_TEST = "qt";

	public static final String YOUTUBE_TEST = "yt";

	public static final int SMS_FIELD_COUNT = 4*3;

	public static final String SMS_DELIVERED_STATUS = "SMS_DELIVERED";

	public static final Long SMS_THRESHOLD_TIME = 3*60*1000L;

	public static final int SMS_INFO_START_INDEX = 91;

	public static final int SMS_INFO_END_INDEX = 67;

	public static final String NO_DATA_FOUND = "{\"result\":\"No data found\"}";

	public static final int LAT_INDEX = 0;

	public static final int LONG_INDEX = 1;

	public static final int ITERATIONCOUNT = 5;

	public static final String MINIMUM_DISTANCE_BETWEEN_LATLONG = "MINIMUM_DISTANCE_BETWEEN_LATLONG";

	public static final String IN_PROGRESS_RESPONSE_JSON ="{\"result\":\"In progress\"}";

	public static final int NONE_DATA_SIZE = 6;

	public static final String NO_COVERAGE = "NC";

	public static final long WAITING_TIME_FOR_HANDOVER_FAILURE = 1000;

	public static final Integer INVALID_VALUE = 0;

	public static final Integer MINIMUM_LENGTH_CSV_FIELD = 5;
	public static final String DL_TECHNOLOGY_WISE_PEAK_VALUE = "peakDl";

	public static final String ADDRESS_TABLE = "ADDRESS_TABLE";

	public static final String ADDRESS = "add";

	public static final String TARGET_PCI_STRING = "<targetPhysCellId>";

	public static final String TARGET_PCI_STRING_START = "targetPhysCellId:";

	public static final String TARGET_PCI_END = "</targetPhysCellId>";

	public static final String HANDOVER_FAILURE_STRING="rrcConnectionReestablishmentRequest";

	public static final String HANDOVER_FAILURE_CAUSE_STRING ="handoverfailure";

	public static final String GOOGLE_ADDRESS_API = "GOOGLE_ADDRESS_API";

	public static final String LATITUDE_API_STRING = "latitute";

	public static final String LONGITUDE_API_STRING = "longitute";

	public static final String ADDRESS_RESULT_JSON_KEY = "results";

	public static final String FOREMATTED_ADDRESS_JSON_KEY = "formatted_address";

	public static final int FORMATTED_JSON_INDEX = 1;

	public static final int PCI_INDEX = 0;

	public static final int HANDOVER_LATECY_START_INDEX = 68;

	public static final int HANDOVER_LATECY_END_INDEX = 69;

	public static final String RECIPE_CATEGORY = "category";
	
	public static final int SMS_SUCESSFULL_INDEX = 1;

	public static final int SMS_DELIVERED_TIME_INDEX = 2;

	public static final int SMS_DELIVERED_LESSTHAN_3_TIME_INDEX = 3;

	public static final int SMS_ATTEMPT_INDEX = 0;

	public static final String ON_NET_STATUS = "On-Net";

	public static final Integer LOG_CODE_17F2 = 6130;

	public static final Integer PACKETIZATION_PROCESSING_ILBC_DELAY_CONSTANT = 35;

	public static final Integer FITTING_PAR_A_G711_COSTANT = 0;
	public static final Integer FITTING_PAR_B_G711_COSTANT = 30;
	public static final Integer FITTING_PAR_C_G711_COSTANT = 15;
	public static final Integer FITTING_PAR_A_ILBC_COSTANT = 10;
	public static final Integer FITTING_PAR_B_ILBC_COSTANT = 20;
	public static final Integer FITTING_PAR_C_ILBC_COSTANT = 30;

	public static final Double R0FACTOR_CONSTANT = 93.2;

	public static final Integer PACKETIZATION_PROCESSING_G711_DELAY_CONSTANT = 25;

	public static final Double MOS_UPPER_THRESOLD = 5.0;

	public static final Double MOS_LOWER_THRESOLD = 1.0;

	public static final int MIN_RSSI_INDEX = 57;

	public static final int MAX_RSSI_INDEX = 70;

	public static final int MOS_COUNT_INDEX = 90;

	public static final int SMS_ATTEMPT_SUMMARY_INDEX = 91;

	public static final int HANDOVER_INTRA_SUMMARY_INDEX = 116;

	public static final int START_LATITUDE_INDEX = 117;

	public static final int START_LONGITUDE_INDEX = 118;

	public static final int HTTP_TOTAL_TIME_TAKEN = 55;

	public static final Double PERCENTAGE_MULTIPLIER = 100.0;

	public static final Object EMPTY_JSON_KEY = null;

	public static final String HTTP_LINK_DOWNLOAD = "hld";

	public static final int CALL_NE_STATUS_INDEX = 55;


	public static final int CALL_NE_STATUS_3G_INDEX = 32;


	public static final int CALL_NE_STATUS_2G_INDEX = 34;

	public static final int EARFCN_SUMMARY_INDEX = 44;

	public static final String RRCCONNECTIONREESTABLISHMENTREQUEST = HANDOVER_FAILURE_STRING;

	public static final String PER_FILE = "ip.per";

	public static final String JITTER = "jttr";

	public static final String RESPONSE_TIME = "rt";

	public static final Integer JITTER_MAX = 200;
	public static final Integer JITTER_MIN = 0;
	public static final Integer RESPONSE_TIME_MAX = 30;
	public static final Integer RESPONSE_TIME_MIN = 0;
	public static final int CALL_CONNECTION_SETUP_SUM = 119;

	public static final int CALL_CONNECTION_SETUP_COUNT = 120;

	public static final String IDLE_HANDOVER_JSON = "IDJ";


	public static final String DL_LINK_AVG_JSON = "dlLinkAvg";
	public static final String UL_LINK_AVG_JSON = "ulLinkAvg";

	public static final String TEST_STATUS_JSON = "testStatus";

	public static final String TOTAL_DLUL_TIME_JSON = "totalDlUlTime";

	public static final String SMS_SENT_TIME_JSON = "smsSentTime";

	public static final String SMS_TOTAL_TIME_JSON = "smsTotalTime";

	public static final String SMS_DELIVERED_TIME_JSON = "smsStatusRecivedTime";

	public static final String SMS_STATUS_JSON = "smsStatus";

	public static final String SMS_NE_STATUS_JSON = "netStatus";

	public static final String SMS_ID_JSON = "smsId";

	public static final String VIDEO_LINK_JSON = "videoLink";

	public static final String VIDEO_RESOLUTION_JSON = "videoResolution";

	public static final String VIDEO_DURATION_JSON = "videoDuration";

	public static final String NO_OF_STALLING_JSON = "videoStallingCount";

	public static final String TOTAL_BUFFERING_TIME_JSON = "totalBufferingTime";

	public static final String VIDEO_FREEZING_RATIO_JSON = "videoFreezingRatio";

	public static final int EMPTY_JSON_LENGTH = 11;
	
	public static final Integer CST_INDEX = 71;
	
	public static final Integer CALL_INITIATE_INDEX = 27;
	
	public static final Integer CALL_DROP_INDEX = 28;
	
	public static final Integer CALL_FAILURE_INDEX = 29;
	
	public static final Integer CALL_SUCCESS_INDEX = 30;
	
	public static final Integer HANDOVER_INITIATE_INDEX = 24;
	public static final Integer HANDOVER_SUCCESS_INDEX = 26;
	public static final Integer HANDOVER_FAILURE_INDEX = 25;
	public static final Integer RESELECTION_SUCCESS_INDEX = 174;
	
	public static final String CELLID_APPEND_TEST = "0";
	public static final int RADIX_16 = 16;
	public static final int TWO_DIGIT_SUBTRACTOR = 2;
	public static final String LAYER3_MSG_CSV_HEADER = "LAYER3_MSG_CSV_HEADER";

	public static final String NEXT_LINE = "\n";

	public static final String LAYER3_MSG_CSV_DELIMETER = "LAYER3_MSG_CSV_DELIMETER";

	public static final String UTF_ENCODING = "UTF-8";

	public static final String WORK_ORDER = "workorder_";

	public static final String CSV_EXTENSION = ".csv";

	public static final String ERROR_MSG = "\"result\" :\"Error in Generation Report\"";

	public static final String LAYER3_CSV_PATH = "LAYER3_CSV_PATH";

	public static final String PATH_PREFIX_JSON = "{\"path\":\"";

	public static final String PATH_POSTFIX_JSON = "\"}";

	public static final String IMAGE_FILE_JPG_TYPE = ".jpg";

	public static final String IMAGE_FILE_PNG_TYPE = ".png";

	public static final Integer B179_LOGCODE = 45433;

	public static final String PACKET_LOSS_LIVE_JSON_KEY = "pcktLoss";
	
	public static final String PACKET_LOSS_IB_JSON_KEY = "pcktLoss";
	
	public static final String BUFFERSIZE_IB_JSON_KEY="bufferSize";

	public static final String HOST_IB_JSON_KEY="host";	

	public static final Integer INITIAL_EVENT_COUNT = 0;

	public static final String TEST_TYPE_JSON_KEY = "testType";
	public static final int CALL_CONNECTION_SETUP_ONNET_SUM = 121;

	public static final int CALL_CONNECTION_SETUP_ONNET_COUNT = 122;

	public static final int CALL_CONNECTION_SETUP_OFFNET_SUM = 123;

	public static final int CALL_CONNECTION_SETUP_OFFNET_COUNT = 124;

	

	public static final int FINAL_MOS_G711_ONNET_SUM = 125;

	public static final int FINAL_MOS_G711_ONNET_COUNT = 126;

	public static final int FINAL_MOS_G711_OFFNET_SUM = 127;

	public static final int FINAL_MOS_G711_OFFNET_COUNT = 128;

	public static final int FINAL_MOS_ILBC_ONNET_SUM = 129;

	public static final int FINAL_MOS_ILBC_ONNET_COUNT = 130;

	public static final int FINAL_MOS_ILBC_OFFNET_SUM = 131;

	public static final int FINAL_MOS_ILBC_OFFNET_COUNT = 132;

	public static final int PACKET_LOSS_SUM = 133;
	public static final int PACKET_LOSS_COUNT = 134;
	public static final int HTTP_DOWNLOAD_TIME_SUM = 135;
	public static final int HTTP_DOWNLOAD_TIME_COUNT = 136;
	public static final int HTTP_ATTEMPT = 137;
	public static final int HTTP_SUCCESS = 138;
	public static final int HTTP_FAILURE = 140;
	public static final int HTTP_DROP = 153;
	
	public static final int SMS_FAILURE_INDEX = 141;
	public static final int TOTAL_DISTANCE = 142;

	public static final int TAU_ATTEMPT_INDEX = 146;
	public static final int TAU_SUCCESS_INDEX = 147;
	public static final int TAU_FAILURE_INDEX = 148;


	public static final int THROUGHTPUT_RANGE = 101;

	public static final String CGI_JSON = "cgi";

	public static final String CALL_STATUS_JSON_KEY = "netStatus";

	public static final String RRC_CONNECTION_REESTABLISHMENT_REJECT = "rrcConnectionReestablishmentReject";

	
	/** KPI CONSTANTS. */
	public static final String KPI_RSRP = "RSRP";

	public static final String KPI_SINR = "SINR";

	public static final String KPI_RSSI = "RSSI";
	
	public static final String KPI_RSRQ = "RSRQ";
	
	public static final String KPI_RSRP_RX0 = "RSRP_RX0";

	public static final String KPI_SINR_RX0 = "SINR_RX0";

	public static final String KPI_RSSI_RX0 = "RSSI_RX0";
	
	public static final String KPI_RSRQ_RX0 = "RSRQ_RX0";
	
	public static final String KPI_RSRP_RX1 = "RSRP_RX1";

	public static final String KPI_SINR_RX1 = "SINR_RX1";

	public static final String KPI_RSSI_RX1 = "RSSI_RX1";
	
	public static final String KPI_RSRQ_RX1 = "RSRQ_RX1";
	
	public static final String KPI_RSRP_RX2 = "RSRP_RX2";

	public static final String KPI_SINR_RX2 = "SINR_RX2";

	public static final String KPI_RSSI_RX2 = "RSSI_RX2";

	public static final String KPI_RSRQ_RX2 = "RSRQ_RX2";

	public static final String KPI_RSRP_RX3 = "RSRP_RX3";

	public static final String KPI_SINR_RX3 = "SINR_RX3";

	public static final String KPI_RSSI_RX3 = "RSSI_RX3";

	public static final String KPI_RSRQ_RX3 = "RSRQ_RX3";
	
	public static final String KPI_PCI = "PCI";

	public static final String KPI_DL_THROUGHPUT = "DL_Throughput";

	public static final String KPI_UL_THROUGHPUT = "UL_Throughput";	

	public static final String KPI_LATENCY = "Latency";
	
	public static final String KPI_EARFCN_UL = "Earfcn_UL";

	public static final String KPI_EARFCN_DL = "Earfcn_DL";

	public static final String KPI_PMI = "PMI";

	public static final String KPI_RI = "RI";

	public static final String KPI_BANDWIDTH_UL = "Bandwidth_UL";

	public static final String KPI_BANDWIDTH_DL = "Bandwidth_DL";

	public static final String KPI_SPATIAL_RANK = "Spatial_Rank";

	public static final String KPI_POWER_TX_PUSCH = "Power_Tx_Pusch";

	public static final String KPI_CQI = "CQI";

	public static final String KPI_CARRIER_INDEX = "Carrier_Index";	

	public static final String KPI_TIMING_ADVANCE = "Timing_Advance";	

	public static final String KPI_BLER = "Bler";

	public static final String KPI_MCS= "MCS";

	public static final String KPI_MCC= "MCC";
	
	public static final String KPI_MNC= "MNC";
	
	public static final String KPI_TRACKING_AREA_CODE= "TAC";
	
	
	
	
	public static final String INST_MOS= "MOS";
	public static final String POLQA_MOS= "POLQA_MOS";

	
	/** MOBILITY EVENTS CONSTANT. */
	
	public static final String LTE_RRCCONNECTION_HO_INIT= "LTE_RRCConnection_Ho_Init";
	public static final String LTE_RRCCONNECTION_HO_OK= "LTE_RRCConnection_Ho_OK";
	public static final String LTE_RRCCONNECTION_HO_FAIL= "LTE_RRCConnection_Ho_Fail";
	
	public static final String INTER_HANDOVER_INITIATE= "Inter_Handover_Initiate";
	public static final String INTER_HANDOVER_SUCCESS= "Inter_Handover_Success";
	public static final String INTER_HANDOVER_FAILURE= "Inter_Handover_Failure";
	
	public static final String INTRA_HANDOVER_INITIATE= "Intra_Handover_Initiate";
	public static final String INTRA_HANDOVER_SUCCESS= "Intra_Handover_Success";
	public static final String INTRA_HANDOVER_FAILURE= "Intra_Handover_Failure";

	
	/** CALL EVENTS CONSTANT. */	

	public static final String LTE_EVENT_VOLTE_CALLINITIATE= "LTE_Event_VoLTE_CallInitiate";
	public static final String LTE_EVENT_VOLTE_CALLDROP= "LTE_Event_VoLTE_callDrop";
	public static final String LTE_EVENT_VOLTE_CALLFAILURE= "LTE_Event_VoLTE_CallFailure";
	public static final String LTE_UE_VOLTE_CALLSUCCESS= "LTE_UE_VoLTE_CallSuccess";	
	public static final String LTE_EVENT_VOLTE_CALLSETUPSUCCESS="LTE_Event_VoLTE_CallSetupSuccess";
	public static final String LTE_EVENT_VOLTE_CALLSETUP="LTE_Event_VoLTE_CallSetup";
	
	
	
	public static final String DOWNLOAD_ATTEMPT= "Download_Attempt";	
	public static final String DOWNLOAD_SUCCESS= "Download_Success";	
	public static final String DOWNLOAD_FAILURE= "Download_Failure";	
	
	public static final String SMS_ATTEMPT= "SMS_Attempt";	
	public static final String SMS_SUCCESS= "SMS_Success";	
	public static final String SMS_FAILURE= "SMS_Failure";	
	
	public static final String TAU_ATTEMPT= "TAU_Attempt";	
	public static final String TAU_SUCCESS= "TAU_Success";	
	public static final String TAU_FAILURE= "TAU_Failure";	


	public static final String DEVICEID_JSON_KEY="deviceId";
	public static final String BANDLOCK_JSON_KEY="bandLock";
	public static final String PCILOCK_JSON_KEY="pciLock";
	public static final String EARFCNLOCK_JSON_KEY="earfcnLock";
	public static final String RATLOCK_JSON_KEY="ratLock";
	public static final String HANDSET_MANUFACTURE="make";
	public static final String PLATFORM="deviceOsVersion";
	public static final String BATTERY_INFORMATION = "batteryLevelOnTestStop";
	public static final String CHIPSET_INFORMATION = "deviceChipset";
	public static final String VERSION_NAME = "versionName";
	public static final String CGI_JSON_KEY="cgi";
	public static final String ENODEB_JSON_KEY="enodeB";
	public static final String EARFCN_JSON_KEY="earfcn";
	
	public static final String UL_EARFCN_JSON_KEY="ulEarfcn";

	public static final String ITERATION_JSON_KEY = "iteration";

	public static final String IMEI_JSON = "imei";

	public static final String IMEI_ENTITY_TYPE = "imei";
	
	public static final String DEVICEID_ENTITY_TYPE = "deviceId";
	
	public static final String SECTORWISE_SUMMARY_JSON = "summaryJson";

	public static final String CEMSDATA_ENTITY_TYPE = "cemsData";

	public static final String MOS = "mos";
	
	public static final String INSTANTANEOUS_MOS = "inst_mos";

	public static final String CA_TYPE = "ca";

	public static final Integer MOS_MAX = 5;
	public static final Integer MOS_MIN = 0;
	
	public static final String WPT_RAW_TABLE_NAME="WPT_RAW_TABLE_NAME";
	public static final String NV_YOUTUBE_RAW_TABLE_NAME="NV_YOUTUBE_RAW_TABLE_NAME";
	public static final String NV_ACTIVE_RAW_TABLE_NAME="NV_ACTIVE_RAW_TABLE_NAME";
	
	public static final String DELETE_LAYER3_TEMP_FILES = "DELETE_LAYER3_TEMP_FILES";


	public static final String DEVICE_ID = "deviceid";

	public static final String DL_SPEED = "dlSpeed";
	
	public static final String BUFFER_TIME =  "bufferTime";

	public static final String AVG_DL_RATE = "avgDlRate";
	
	public static final String AVG_UL_RATE = "avgUlRate";
	
	public static final String AVG_LATENCY = "avgLatency";
	
	public static final String PACKET_LOSS = "pcktLoss";
	
	public static final String AVG_DNS_RESOLVE_TIME = "dnsResolveTime";
	
	public static final String WEB_URL = "webUrl";
	
	public static final String IPV_6_LIST = "ipv6list";
	
	public static final String IPV_4_LIST = "ipv4list";

	public static final String DECIMAL_PATTERN_2DIGIT = "#.##";

	public static final String ADHOC_REPORT_DATA = "adhc";
	
	public static final String SSVT_REPORT_DATA = "ssvt";

	public static final String CAPTURED_ON = "capturedOn";

	public static final String TEST_SKIPPED = "TEST_SKIPPED";

	public static final String TEST_SKIP = "testSkip";

	public static final String REMARK = "remark";

	public static final Integer SMS_THRESOLD_VALUE = 180000;

	public static final String NOK = "NOK";
	public static final String OK = "OK";

	public static final String SMS = "sms";

	public static final Integer B195_LOGCODE = 45461;

	public static final String NEIGHBOUR = "neighbour";
	
	public static final String WIFI ="WIFI";

	public static final String LAYER3_NEIGHBOUR_TABLE = "LAYER3_NEIGHBOUR_TABLE";

	public static final String NEIGHBOUR_CONSTANT = "Neighbour ";

	public static final String EMPTY_STRING_RESPONSE = "{\"result\":[]}";

	public static final String FETCHING_VIDEO = "FETCHING_VIDEO";

	public static final String ISBACKGROUNDPIN_INBUILDING_JSON_KEY = "isBackgroundPin";

	public static final String FAILURE_CONSTANT = "FAIL";

	public static final String RECIPE_MAPPING_ENTITY_TYPE = "recipeMappingForCsv";

	public static final int CALL_STATUS_INDEX = 60;

	public static final int CALL_STATUS_3G_INDEX = 37;
	
	public static final int CALL_STATUS_2G_INDEX = 39;
	
	public static final String CALL_END = "CALL_END";

	public static final String BAND_ENTITY_TYPE = "bandType";

	public static final String BAND_UNLOCK = "Unlock";

	public static final String STEALTH_DATA_TABLE = "STEALTH_DATA_TABLE";

	public static final String STEALTH_DATA_DUMP_COLUMN_NAME = "STEALTH_DATA_DUMP_COLUMN_NAME";

	public static final String STEALTH_DUMP_HEADERS = "STEALTH_DUMP_HEADERS";
	
	/** The Empty string. */
	public static final String BLANK_STRING = "";

	public static final String SENDER_REPORT = "Sender Report";
	/** The Constant 4179_LOG_CODE. */
	public static final Integer DATA_4179_LOG_CODE = 16761;

	/** The SPACE string. */
	public static final String SPACE = " ";
	public static final Integer DATA_513A_LOG_CODE = 20794;
	
	public static final Integer DATA_51FC_LOG_CODE = 20988;
	
	/** The Constant RSSI_3G. */
	public static final String RSSI_3G = "rssi_3g";
	
	/** The Constant RSCP_3G. */
	public static final String RSCP_3G = "rscp_3g";
	
	/** The Constant ECIO_3G. */
	public static final String ECIO_3G = "ecio_3g";
	
	/** The Constant RSCP. */
	public static final String RSCP = "RSCP";
	
	/** The Constant ECIO. */
	public static final String ECIO = "ECIO";
	
	/** The Constant ECIO. */
	public static final String WCDMA_RSSI = "RSSI_3G";
	
	/** The Constant RXLEV. */
	public static final String RXLEV = "RXLEV";
	
	/** The Constant RXSUB. */
	public static final String RXQUAL = "RXQUAL";
	
	/** The Constant RXLEV. */
	public static final String BSIC = "BSIC";
	
	/** The Constant RXSUB. */
	public static final String BCCH = "BCCH";
	
	/** The Constant RXLEV_2G. */
	public static final String RXLEV_2G = "rxlev";
	
	/** The Constant RXQUAL_2G. */
	public static final String RXQUAL_2G = "rxqual";
	
	/** The Constant BCCH_2G. */
	public static final String BCCH_2G = "bcch";
	
	/** The Constant BSIC_2G. */
	public static final String BSIC_2G = "bsic";
	
	/** The Constant RSCP_MIN. */
	public static final Integer RSCP_MIN = -128;

	/** The WHITESPACE RegEx string. */
	public static final String WHITESPACE = "\\s+";
	/** The Constant RSCP_MAX. */
	public static final Integer RSCP_MAX = 0;
	
	/** The Constant ECIO_MIN. */
	public static final Integer ECIO_MIN = -63;

	/** The Constant ECIO_MAX. */
	public static final Integer ECIO_MAX = 0;
	
	public static final String RECEIVER_REPORT = "Receiver Report";
	/** The Constant RSSI_MIN. */
	public static final Integer RSSI_3G_MIN = -110;

	/** The Constant RSSI_MAX. */
	public static final Integer RSSI_3G_MAX = -10;
	
	public static final int PROTOCOL_LATENCY = 10;
	
	/** The Constant RSSI_MIN. */
	public static final Integer RXLEV_MIN = -120;

	/** The Constant RSSI_MAX. */
	public static final Integer RXLEV_MAX = -10;

	/** The Constant RSSI_MIN. */
	public static final Integer RXQUAL_MIN = 0;

	/** The Constant RSSI_MAX. */
	public static final Integer RXQUAL_MAX = 7;
	
	/** The Constant DATA_B173_LOG_CODE. */
	public static final Object DATA_B173_LOG_CODE = 45427;
	public static final Long PDSCH_PUSCH_THROUGHPUT_ZERO = 0L;
	public static final Double PDSCH_TB_SIZE_ZERO = 0.0;
	public static final String PDSCH_THROUGHPUT = "pdschthroughput";
	public static final String PDSCH_THROUGHPUT_PCELL = "pdschthroughputpcell";
	public static final String PDSCH_THROUGHPUT_SCELL1 = "pdschthroughputscell1";
	public static final String PDSCH_THROUGHPUT_SCELL2 = "pdschthroughputscell2";
	public static final String PDSCH_THROUGHPUT_SCELL3 = "pdschthroughputscell3";
	public static final String PDSCH_THROUGHPUT_SCELL4 = "pdschthroughputscell4";
	public static final String PDSCH_THROUGHPUT_SCELL5 = "pdschthroughputscell5";
	public static final Integer PDSCH_THROUGHTPUT_MAX =  100;
	public static final Integer PDSCH_THROUGHTPUT_MIN = 0;
	public static final String KPI_PDSCH_THROUGHPUT = "PDSCH_Throughput";
	public static final String KPI_PDSCH_BLER = "PDSCH_BLER";
	
	public static final String PUSCH_THROUGHPUT = "puschthroughput";
	public static final Integer PUSCH_THROUGHTPUT_MAX =  100;
	public static final Integer PUSCH_THROUGHTPUT_MIN = 0;
	public static final Integer PUSCH_VERSION_8 = 8;
	public static final Integer PUSCH_VERSION_26 = 26;
	public static final Integer PUSCH_VERSION_33 = 33;
	
	
	public static final String KPI_PUSCH_THROUGHPUT = "PUSCH_Throughput";
	public static final String MAC_UL_THROUGHPUT = "MAC_UL_Throughput";
	public static final String KPI_PUSCH_MCS_INDEX = "PUSCH_MCS_Index_for_Codeword_0";
	public static final String KPI_PUSCH_MODULATION_TYPE = "PUSCH_Modulation_Codeword_0";

	public static final Integer DATA_B16D_LOG_CODE =45421;
	
			
	public static final  String CSFB_MO_STRING="Service type: mobile originating CS fallback or 1xCS fallback";
	public static final  String CSFB_MT_STRING="Service type: mobile terminating CS fallback or 1xCS fallback";
	public static final  String CSFB_EEM_STRING="Extended Service Request";
	public static final String CSFB_CALL_ALERTING = "Message Type: Alerting";
	
	public static final  String PROCESSED_FILES ="ProcessedFiles";

	public static final String CSFB_CALL_CONNECT = "Message Type: Connect";
	public static final String CSFB_CALL_DISCONNECT="Message Type: Disconnect";
	public static final String CSFB_CALL_SUCCESS_STRING="Normal call clearing";
	public static final String CSFB_CALL_DROP_STRING="Resources unavailable";
	public static final int CSV_TIMESTAMP_INDEX = 3;
	
	public static final String RRC_CONNECTION_FINISHED = "RRC_Connection_Finished";
	public static final String RRC_CONNECTION_SETUPOK = "RRC_Connection_SetupOk";
	public static final String RRC_CONNECTION_COMPLETE = "RRC_Connection_Complete";
	public static final String RRC_CONNECTION_ATTEMPT = "RRC_Connection_Attempt";
	public static final String RRC_CONNECTION_ATTEMPT_FAILURE = "RRC_Connection_Attempt_Failure";
	public static final String RRC_REESTABLISHMENT_SUCCESS = "RRC_Reestablishment_Success";
	public static final String RRC_REESTABLISHMENT_FAILED = "RRC_Reestablishment_Failed";
	public static final String RRC_COMPLETE_MSG = "rrcConnectionComplete";
	public static final String RRC_RELEASE_MSG = "rrcConnectionRelease";
	public static final String RRC_FAILED_MSG = "rrcConnectionReject";
	public static final String RRC_REESTABLISHMENT_REQUEST_MSG = "rrcConnectionReestablishmentRequest";
	public static final String RRC_REESTABLISHMENT_COMPLETE_MSG = "rrcConnectionReestablishmentComplete";
	public static final String RRC_REESTABLISHMENT_REJECT_MSG = "rrcConnectionReestablishmentReject";
	public static final String LINK_ADAPTATION_CQIO = "Wideband_CQI_for_Codeword_0";
	public static final String LINK_ADAPTATION_CQI1 = "Wideband_CQI_for_Codeword_1";
	public static final String LINK_ADAPTATION_DL_PRB = "PRB_Utilization_DL";
	public static final String LINK_ADAPTATION_PDSCH_NUMRB = "LTE_PDSCH_PRBs_Allocated";
	public static final String LINK_ADAPTATION_PDSCH_MODULATION = "PDSCH_Modulation_Codeword_0";
	public static final String LINK_ADAPTATION_PDSCH_MCS = "PDSCH_MCS_Index_for_Codeword_0";
	public static final String LINK_ADAPTATION_NUMRB = "PRB_Utilization_UL";
	
	public static final String RRC_CONNECTION_SETUP_REQUEST= "rrcconnectionsetuprequest";

	public static final Integer INTEGER_ZERO = 0;
	
	public static final Integer DATA_B161_LOG_DATA =45409;
	public static final String LINK_ADAPTATION_TRANSMISSION_MODE = "Transmission_Mode";
	
	public static final String STEALTH_HEADERS_COLUMNS_NAME = "headers";

	public static final Integer HBASE_DRIVE_PCI_INDEX = 3;
	public static final String TAU_ACCEPT_MESSAGE = "tracking area update accept";

	public static final Integer LOG_CODE_B067 = 45159;
	public static final Integer LOG_CODE_B172 = 45426;
	public static final Integer LOG_CODE_B167 = 45415;
	public static final Integer LOG_CODE_B16F = 45423; 

	public static final String QPSK_MODULATION = "QPSK";
	public static final String QAM64_MODULATION = "64QAM";
	public static final String QAM16_MODULATION = "16QAM";
	
	//dominant kpis
	public static final String LTE_DOMINANT_PCI = "Dominant_PCI";
	public static final String LTE_DOMINANT_RSRP = "Dominant_RSRP";
	public static final String LTE_DOMINANT_RSRP_TX1RX1 = "Dominant_RSRP_Tx1Rx1";
	public static final String LTE_DOMINANT_RSRP_TX2RX1 = "Dominant_RSRP_Tx2Rx1";
	public static final String LTE_DOMINANT_RSSI = "Dominant_RSSI";
	public static final String LTE_DOMINANT_RSSI_TX1RX1 = "Dominant_RSSI_Tx1Rx1";
	public static final String LTE_DOMINANT_RSSI_TX2RX1 = "Dominant_RSSI_Tx2Rx1";
	public static final String LTE_DOMINANT_RSRQ = "Dominant_RSRQ";
	public static final String LTE_DOMINANT_RSRQ_TX1RX1 = "Dominant_RSRQ_Tx1Rx1";
	public static final String LTE_DOMINANT_RSRQ_TX2RX1 = "Dominant_RSRQ_Tx2Rx1";
	public static final String LTE_DOMINANT_EARFCN = "Dominant_EARFCN";
	public static final String TX_POWER_HEADROOM = "Tx_Power_Headroom";
	public static final String PRACH_TX_POWER = "PRACH_Tx_Power";

	public static final int INST_MOS_START_INDEX = 149;
	public static final int INST_MOS_END_INDEX = 152;

	public static final int KPI_RATE_START_INDEX = 162;
	public static final int KPI_RATE_END_INDEX = 174;
	
	public static final int CSFB_EVENT_START_INDEX = 156;
	public static final int CSFB_EVENT_END_INDEX = 161;

	public static final int PDSCH_SUM_INDEX = 154;
	public static final int PDSCH_COUNT_INDEX = 155;
	
	public static final int MAC_DL_SUM_INDEX = 223;
	public static final int MAC_DL_COUNT_INDEX = 224;
	
	public static final int JITTER_NEW_SUM_INDEX = 225;
	public static final int JITTER_NEW_COUNT_INDEX = 226;
	public static final int PACKET_LOSS_SUM_INDEX = 227;
	public static final int PACKET_LOSS_COUNT_INDEX = 228;
	
	public static final int UL_PRB_SUM_INDEX = 229;
	public static final int UL_PRB_COUNT_INDEX = 230;
	public static final int DL_PRB_UTILIZATION_SUM_INDEX = 231;
	public static final int DL_PRB_UTILIZATION_COUNT_INDEX = 232; 
	public static final int UL_PRB_UTILIZATION_SUM_INDEX = 233;
	public static final int UL_PRB_UTILIZATION_COUNT_INDEX = 234;
	
	public static final String CSFB_CALL_ATTEMPT = "CSFB_Call_Attempt";
	public static final String CSFB_CALL_SETUP_SUCCESS = "CSFB_Call_Setup_Success";
	public static final String CSFB_CALL_SUCCESS = "CSFB_Call_Success";
	public static final String CSFB_CALL_SETUP_FAILED = "CSFB_Call_Setup_Failure";
	public static final String CSFB_CALL_DROP = "CSFB_Call_Drop";
	public static final String CSFB_CALL_FAILED = "CSFB_Call_Failure";
	
	public static final Integer B061_LOGCODE = 45153;
	public static final String RRC_SETUP_MSG = "rrcConnectionSetup";
	public static final String PAGING_MSG = "paging";
	public static final String SERVICE_REQUEST_MSG = "servicerequest";
	public static final String RADIO_LINK_FAILURE_MSG = "Radio Link Failure";
	public static final String SERVICE_REJECT_MSG = "serviceReject";
	public static final String SERVICE_REQUEST_FAIL_MSG = "serviceFailure";
	public static final String AUTHENTICATION_RESPONSE_MSG = "Authentication response";
	public static final String AUTHENTICATION_FAILURE_MSG = "Authentication failure";
	public static final String AUTHENTICATION_REJECT_MSG = "Authentication reject";
	public static final String AUTHENTICATION_REQUEST_MSG = "Authentication request";
	public static final String RRC_REQUEST_MT_ACCESS_MSG = "mt-Access";
	public static final String RRC_REQUEST_MO_SIGNALLING_MSG = "mo-Signalling";
	public static final String RRC_RECONFIGURATION_FAIL_TAG_MSG = "reconfigurationFailure";
	public static final String RRC_EUTRA_TAG_MSG = "eutra";
	public static final String RRC_GERAN_TAG_MSG = "geran";
	public static final String RRC_UTRAFDD_TAG_MSG = "utrafdd";
	public static final String RRC_UTRATDD_TAG_MSG = "utratdd";
	public static final String PDN_CONNECTION_REQUEST = "pdnConnectionRequest";
	public static final String EMM_REQUEST_MSG = "EMM extended Service Request";
	
	public static final String SERVICE_REQUEST ="SERVICE REQUEST";
	
	public static final String RRC_INTERRAT_CELL_RESELECTION = "interratcellreselection";
	public static final String TAG_DRB_MSG = "ToAddModList";
	public static final String UL_INFORMATION_TRANSFER = "ulInformationTransfer";
	public static final String DEDICATED_NAS_TAG = "dedicatedInfoNAS";
	public static final String SERVICE_TYPE_MO = "MO";
	public static final String SERVICE_TYPE_MT = "MT";



	//mcs info
	public static final String TB0_MCS = "TB0_MCS";
	public static final String TB1_MCS = "TB1_MCS";
	public static final String TB0_MODULATION_TYPE = "TB0_Modulation_Type";
	public static final String TB1_MODULATION_TYPE = "TB1_Modulation_Type";
	
	public static final String AVG_DL_TB0_SIZE = "Avg_DL_TB0_Size";
	public static final String AVG_DL_TB1_SIZE = "Avg_DL_TB1_Size";
	public static final String AVG_DL_TB_SIZE = "Avg_DL_TB_Size";
	public static final String AVG_UL_TB_SIZE = "Avg_UL_TB_Size";
	
	public static final int MAC_SUM_INDEX = 174;
	public static final int MAC_COUNT_INDEX = 175;
	public static final int PUSCH_SUM_INDEX = 176;
	public static final int PUSCH_COUNT_INDEX = 177;
	public static final int CQICWO_SUM_INDEX = 178;
	public static final int CQICWO_COUNT_INDEX = 179;
	public static final int CQICW1_SUM_INDEX = 180;
	public static final int CQICW1_COUNT_INDEX = 181;


	public static final String UE_TX_POWER = "PUSCH";
	
	public static final Integer LOG_CODE_B061 = 45153;
	public static final Integer LOG_CODE_B168 = 45416;

	public static final String UE_TX_POWER_AGG_WRAPPER = "UE_Tx_Power";
	
	public static final String PREAMBLE_COUNT_MAX = "Preamble_Count_Max";
	public static final String PREAMBLE_INITIAL_TX_POWER = "Preamble_Initial_Tx_Power";
	public static final String RA_RNTI = "RA_RNTI";
	public static final String RA_TIMING_ADVANCE = "RA_Timing_Advance";
	
	public static final String LTE_CELL_DL_CP_TYPE = "LTE_Cell_DL_CP";
	public static final String LTE_CELL_UL_CP_TYPE = "LTE_Cell_UL_CP";


	
	public static final String HO_INTRPT_TIME_UE="HO_intrpt_time_UE";
	public static final String HO_INTRPT_TIME_UE_DL="HO_intrpt_time_UE_DL";
	public static final String HO_INTRPT_TIME_UE_UL="HO_intrpt_time_UE_UL";
	
	public static final String IDLE_STATE = "Idle";
	public static final String CONNECT_STATE = "Connected";
	
	//State Info
	public static final String EMM_STATE = "EMM_State";
	public static final String EMM_SUBSTATE = "EMM_Substate";
	public static final String RRC_STATE = "RRC_State";

	public static final String AVG_UL_PRB = "Avg_UL_PRB";
	
	public static final int INTEGER_SIXTY = 60;
	public static final double DISTANCE_CALCULATION_1 = 1.1515; 
	public static final double DISTANCE_CALCULATION_2 = 1.609344;
	
	public static final int INTEGER_THOUSAND = 1000;
	public static final int INTEGER_THREE = 3;
	public static final double RADIAN_DEGREE_CONVERT_MULTIPLIER = 180.0;
	public static final Integer ONE_KB_IN_BYTES = 1024;

	public static final int S_APP_VERSION_INDEX = 25;
	public static final int S_DL_THROUGHPUT_INDEX = 6;
	public static final int S_UL_THROUGHPUT_INDEX = 7;
	public static final int S_AVG_CQI_INDEX = 142;
	
	
	
	public static final Double DOUBLE_TEN = 10.0;

//	HandoverEvents
	public static final String LTE_MO_CSFB_REQUEST = "MO_CSFB_Request";
	public static final String LTE_MT_CSFB_REQUEST = "MT_CSFB_Request";
	public static final String LTE_MO_CSFB_SUCCESS = "MO_CSFB_Success";
	public static final String LTE_MT_CSFB_SUCCESS = "MT_CSFB_Success";
	public static final String IDLE_MODE_LTE_UMTS_REQUEST = "Idle_Mode_LTE_UMTS_Request";
	public static final String IDLE_MODE_LTE_UMTS_SUCCESS = "Idle_Mode_LTE_UMTS_Success";
	public static final String IDLE_MODE_UMTS_LTE_REQUEST = "Idle_Mode_UMTS_LTE_Request";
	public static final String IDLE_MODE_UMTS_LTE_SUCCESS = "Idle_Mode_UMTS_LTE_Success";
	public static final String LTE_NON_OPTIMIZED_IRAT_HANDOVER_EUTRA = "Non_Optimized_IRAT_Handover_EUTRA";
	public static final String LTE_NON_OPTIMIZED_IRAT_HANDOVER_GERAN = "Non_Optimized_IRAT_Handover_GERAN";
	public static final String LTE_NON_OPTIMIZED_IRAT_HANDOVER_UTRAFDD = "Non_Optimized_IRAT_Handover_UTRAFDD";
	public static final String LTE_NON_OPTIMIZED_IRAT_HANDOVER_UTRATDD = "Non_Optimized_IRAT_Handover_UTRATDD";
	public static final String LTE_RADIO_LINK_FAILURE = "Radio_Link_Failure";
	public static final String LTE_RRC_CONNECTIONRECONFIGURATION_FAILURE = "RRC_Connection_Reconfiguration_Faliure";
	public static final String LTE_RRC_CONNECTION_RECONFIGURATION_COMPLETE_MISSING = "RRC_Reconfiguration_Complete_Missing";
	public static final String LTE_IDLE_MODE_RESELECTION = "Idle_Mode_Reselection";
	public static final String LTE_MO_CSFB_FAILURE = "MO_CSFB_Failure";
	public static final String LTE_MT_CSFB_FAILURE = "MT_CSFB_Failure";
	public static final String LTE_RETURN_AFTER_CS_CALL_END = "Return_After_CS_Call_End";
	
//	LTECallEvent
	public static final String LTE_PAGING_COUNT = "Paging";
	public static final String LTE_SERVICE_REJECT = "Service_Reject";
	public static final String LTE_SERVICE_REQUEST = "Service_Request";
	public static final String LTE_RRC_CONNECTION_SETUP = "RRC_Connection_Setup";
	public static final String LTE_SECURITY_MODE_COMPLETE = "Security_Mode_Complete";
	public static final String LTE_AUTHENTICATION_RESPONSE = "Authentication_Response";
	public static final String LTE_AUTHENTICATION_REJECT = "Authentication_Reject";
	public static final String LTE_AUTHENTICATION_FAILURE = "Authentication_Failure"; 
	public static final String LTE_AUTHENTICATION_REQUEST = "Authentication_Request";
	public static final String LTE_SERVICE_REQUEST_FAILURE = "Service_Request_Failure";
	public static final String LTE_MO_RRC_CONNECTION_REQUEST = "MO_RRC_Connection_Request";
	public static final String LTE_MT_RRC_CONNECTION_REQUEST = "MT_RRC_Connection_Request";
	public static final String LTE_RRC_CONNECTION_SETUP_FAILURE = "RRC_Connection_Setup_Failure";
	public static final String LTE_RRC_CONNECTION_RECONFIGURATION = "RRC_Connection_Reconfiguration";
	public static final String LTE_RRC_REESTABLISHMENT_FAILURE = "RRC_Reestablishment_Faliure";
	public static final String LTE_RRC_CONNECTION_RECONFIGURATION_SUCCESS = "RRC_Connection_Reconfiguration_Success";
	public static final String LTE_ERAB_SETUP = "eRAB_Setup";
	public static final String LTE_ERAB_SETUP_SUCCESS = "eRAB_Setup_Success";
	public static final String LTE_RRC_CONNECTION_DROPPED = "RRC_Connection_Dropped";
	public static final String LTE_SERVICE_REQUEST_SUCCESS = "Service_Request_Success";
	
	public static final String LTE_RACH_PREAMBLE_SUCCESS = "RACH_Preamble_Success";
	public static final String LTE_RACH_PROCEDURE_SUCCESS = "RACH_Procedure_Success";
	public static final String LTE_RACH_PREAMBLE_FAILURE = "RACH_Preamble_Failure";
	public static final String LTE_RACH_PROCEDURE_FAILURE = "RACH_Procedure_Failure";

	public static final String LTE_CONVERSATIONAL_VOICE_BEARER_ACTIVATION_REQUEST = "Voice_Bearer_Activation_Request";
	public static final String LTE_CONVERSATIONAL_VOICE_BEARER_ACTIVATION_SUCCESS = "Voice_Bearer_Activation_Success";
	public static final String LTE_CONVERSATIONAL_VOICE_BEARER_DEACTIVATED = "Voice_Bearer_Deactivated";
	public static final String INITIAL_IMS_REGISTRATION_SUCCESS = "IMS_Registration_Success";
	public static final String INITIAL_IMS_REGISTRATION_FAILURE = "IMS_Registration_Failure";

	public static final String LTE_ATTACH_COMPLETE = "Attach_Complete";
	public static final String LTE_ATTACH_ACCEPT = "Attach_Accept";
	public static final String LTE_ATTACH_REQUEST = "Attach_Request";
	public static final String LTE_DETACH_REQUEST = "Detach_Request";
	public static final String LTE_EMM_REGISTERED = "EMM_Registered";
	public static final String LTE_PDN_CONNECTION_REQUEST = "PDN_Connection_Request";

	public static final Integer LOGCODE_4125 = 16677;
	public static final String RRC_STATE_MSG = "Cell Dch";
	public static final String ALERTING_MSG = "1 (Alerting)";

	public static final Double DOUBLE_ZERO = 0.0;
	public static final Long LONG_ZERO = 0L;
	public static final String MSG3 = "MSG3";

	public static final String PCI_CHANGED = "Pci_Changed";


	public static final Integer THIRTY_TWO = 32;
	public static final Integer FIFTEEN_HUNDRED = 1500;
	public static final Integer THOUSAND = 1000;
	
	public static final Integer	MIN_LATENCY_BUFFER_SIZE_32BYTES_INDEX=193;
	public static final Integer	MAX_LATENCY_BUFFER_SIZE_32BYTES_INDEX=194;
	public static final Integer	LATENCY_BUFFER_SIZE_32BYTES_SUM_INDEX=195;
	public static final Integer	LATENCY_BUFFER_SIZE_32BYTES_COUNT_INDEX=196;

	public static final Integer	MIN_LATENCY_BUFFER_SIZE_1000BYTES_INDEX=197;
	public static final Integer	MAX_LATENCY_BUFFER_SIZE_1000BYTES_INDEX=198;
	public static final Integer	LATENCY_BUFFER_SIZE_1000BYTES_SUM_INDEX=199;
	public static final Integer	LATENCY_BUFFER_SIZE_1000BYTES_COUNT_INDEX=200;

	public static final Integer	MIN_LATENCY_BUFFER_SIZE_1500BYTES_INDEX=201;
	public static final Integer	MAX_LATENCY_BUFFER_SIZE_1500BYTES_INDEX=202;
	public static final Integer	LATENCY_BUFFER_SIZE_1500BYTES_SUM_INDEX=203;
	public static final Integer	LATENCY_BUFFER_SIZE_1500BYTES_COUNT_INDEX=204;

	public static final String DL_MODULATION_TYPE = "DL_Modulation_Type";
	public static final String UL_MODULATION_TYPE = "UL_Modulation_Type";
	public static final String DL_PRB = "DL_PRB";

	public static final String PCAP_STRING = "pcap";
	public static final String MSG_TEXT_STRING = "msg_text";

	public static final String RRC_CONNECTION_REESTABLISHMENT_REQUEST = "RRC_Connection_Reestablishment_Request";
	public static final String RRC_CONNECTION_REESTABLISHMENT_COMPLETE = "RRC_Connection_Reestablishment_Complete";
	public static final String RRC_REESTABLISHMENT_REJECT = "RRC_Connection_Reestablishment_Reject";
	public static final String RESELECTION_SUCCESS = "Cell_Reselection";
	public static final String MAC_DL_THROUGHPUT = "MAC_DL_Throughput";

	public static final String NAS_EPS_SESSION_MSG = "NAS EPS session management messages:";
	public static final String NAS_EPS_MOBILITY_MSG =  "NAS EPS Mobility Management Message Type:";

	public static final Integer	DL_PRB_SUM_INDEX=208;
	public static final Integer	DL_PRB_COUNT_INDEX=209;	

	public static final Integer	IMS_REGISTRATION_SETUPTIME_SUM_INDEX=213;
	public static final Integer IMS_REGISTRATION_SETUPTIME_COUNT_INDEX=214;

	public static final Integer RRC_CONNECTION_REESTABLISHMENT_REQUEST_INDEX = 210;
	public static final Integer RRC_CONNECTION_REESTABLISHMENT_COMPLETE_INDEX = 211;
	public static final Integer RRC_CONNECTION_REESTABLISHMENT_REJECT_INDEX = 212;
	
	public static final Integer VOLTE_PAGING_ATTEMPT_INDEX = 215;
	public static final Integer VOLTE_PAGING_SUCCESS_INDEX = 216;

	public static final Integer VOLTE_MT_CALL_ATTEMPT_INDEX=217;
	public static final Integer VOLTE_MO_CALL_SUCCESS_INDEX=220;
	
	public static final Integer PMOS_SUM_INDEX=235;
	public static final Integer PMOS_COUNT_INDEX=236;

	
	
	public static final Integer PDSCH_MIN_INDEX = 221;	
	public static final Integer PDSCH_MAX_INDEX = 222;
	
	public static final Integer PUSCH_MIN_INDEX = 237;	
	public static final Integer PUSCH_MAX_INDEX = 238;
	
	public static final String STATIONARY = "Stationary";
	public static final String STEALTH = "Stealth";
	
	public static final String JITTER_DLF = "VOLTE_Jitter";
	public static final String PACKET_LOSS_DLF = "VOLTE_Packet_Loss";

	public static final String MHZ_1_4 = "1.4 MHz";
	public static final String MHZ_3 = "3 MHz";
	public static final String MHZ_5 = "5 MHz";
	public static final String MHZ_10 = "10 MHz";
	public static final String MHZ_15 = "15 MHz";
	public static final String MHZ_20 = "20 MHz";

	public static final String CALL_INITIATE = "CALL_INITIATE";
	public static final String CALL_FAILURE = "CALL_FAILURE";
	public static final String CALL_SUCCESS = "CALL_SUCCESS";
	public static final String CALL_DROP = "CALL_DROP";
	public static final String HANDOVER_INITIATE = "HANDOVER_INITIATE";
	public static final String HANDOVER_FAILURE = "HANDOVER_FAILURE";
	public static final String HANDOVER_SUCCESS = "HANDOVER_SUCCESS";
	
	public static final String PDCP_THROUGHPUT = "PDCP_Throughput";
	public static final String RLC_THROUGHPUT = "RLC_Throughput";

	public static final int PDCP_THROUGHPUT_SUM_INDEX = 252;
	public static final int PDCP_THROUGHPUT_COUNT_INDEX = 253;
	public static final int RLC_THROUGHPUT_SUM_INDEX = 254;
	public static final int RLC_THROUGHPUT_COUNT_INDEX = 255;
	public static final int MSG3_INDEX=256;
	public static final int MSG1_INDEX=257;

	public static final int PING_BUFFER_SIZE_INDEX=258;
	public static final int NUMBER_OF_RTP_PACKETS_LOST_INDEX=259;
	public static final int TOTAL_PACKET_COUNT_INDEX=260;
	
	public static final int CALL_SETUP_INDEX = 261;
	public static final int VOLTE_MT_CALL_SETUP_INDEX = 262;
	public static final int VOLTE_MO_CALL_SETUP_INDEX = 263;

	
	public static final int CALL_SETUP_SUCCESS_TIME_SUM_INDEX = 264;
	public static final int CALL_SETUP_SUCCESS_TIME_COUNT_INDEX = 265;
	

}

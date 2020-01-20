package com.inn.foresight.module.nv.layer3.qmdlParser.util;

/** Created by ist on 16/9/16. */
public interface ParserConstant {

    //5227

    /** The null. */
    //MAC_MODE
     String NULL="null";

     /** The idle. */
     String IDLE="idle";

     /** The transfer. */
     String TRANSFER="transfer";

    /** The null inactive. */
    //MAC_NULL_STATE_MACHINE
     String NULL_INACTIVE="null inactive";

     /** The null gprs 51 active. */
     String NULL_GPRS_51_ACTIVE="null gprs 51 active";

     /** The null suspended. */
     String NULL_SUSPENDED="null suspended";

     /** The null suspend resel. */
     String NULL_SUSPEND_RESEL="null suspend resel";

     /** The null uplink tbf estab. */
     String NULL_UPLINK_TBF_ESTAB="null uplink tbf estab";

     /** The null uplink sb estab. */
     String NULL_UPLINK_SB_ESTAB="null uplink sb estab";

     /** The null downlink sb estab. */
     String NULL_DOWNLINK_SB_ESTAB="null downlink sb estab";

     /** The null uplink tbf access. */
     String NULL_UPLINK_TBF_ACCESS="null uplink tbf access";

    /** The passive. */
    //MAC_IDLE_STATE_MACHINE
     String PASSIVE="passive";

     /** The ul packet access. */
     String UL_PACKET_ACCESS="ul packet access";

     /** The ul access queued. */
     String UL_ACCESS_QUEUED="ul access queued";

     /** The ul access reject. */
     String UL_ACCESS_REJECT="ul access reject";

     /** The ul 1p ass. */
     String UL_1P_ASS="ul 1p ass";

     /** The ul 2p ass. */
     String UL_2P_ASS="ul 2p ass";

     /** The ul 1p contention res. */
     String UL_1P_CONTENTION_RES="ul 1p contention res";

     /** The dl ass. */
     String DL_ASS="dl ass";

    /** The transfer inactive. */
    //MAC_TRANSFER_STATE_MACHINE
     String TRANSFER_INACTIVE="transer inactive";

     /** The transfer dl. */
     String TRANSFER_DL="transfer dl";

     /** The transfer ul fixed. */
     String TRANSFER_UL_FIXED="transfer ul fixed";

     /** The transfer ul dynamic. */
     String TRANSFER_UL_DYNAMIC="transfer ul dynamic";

     /** The transfer dl reassign. */
     String TRANSFER_DL_REASSIGN="transfer dl reassing";

     /** The transfer ul fixed realloc. */
     String TRANSFER_UL_FIXED_REALLOC="transfer ul fixed realloc";

     /** The transfer ul dynamic realloc. */
     String TRANSFER_UL_DYNAMIC_REALLOC="transfer ul dynamic realloc";

     /** The transfer dl with fixed alloc. */
     String TRANSFER_DL_WITH_FIXED_ALLOC="transfer dl with fixed alloc";

     /** The transfer dl with dynamic alloc. */
     String TRANSFER_DL_WITH_DYNAMIC_ALLOC="transfer dl with dynamic alloc";

     /** The transfer ul fixed dl assign. */
     String TRANSFER_UL_FIXED_DL_ASSIGN="transfer ul fixed dl assign";

     /** The transfer ul dynamic dl assign. */
     String TRANSFER_UL_DYNAMIC_DL_ASSIGN="transfer ul dynamic dl assign";

     /** The transfer dl reassign fixed alloc. */
     String TRANSFER_DL_REASSIGN_FIXED_ALLOC="transfer dl reasssign fixed alloc";

     /** The transfer dl reassign dynamic alloc. */
     String TRANSFER_DL_REASSIGN_DYNAMIC_ALLOC="transfer dl reassign dynamic alloc";

     /** The transfer ul fixed realloc dl assign. */
     String TRANSFER_UL_FIXED_REALLOC_DL_ASSIGN="transfer ul fixed realloc dl assign";

     /** The transfer ul dynamic realloc dl assign. */
     String TRANSFER_UL_DYNAMIC_REALLOC_DL_ASSIGN="transfer ul dynamic realloc dl assign";

     /** The transfer concurrent fixed transfer. */
     String TRANSFER_CONCURRENT_FIXED_TRANSFER="transfer concurrent fixed transfer";

     /** The transfer concurrent dynamic transfer. */
     String TRANSFER_CONCURRENT_DYNAMIC_TRANSFER="transfer concurrent dynamic transfer";

     /** The transfer concurrent fixed dl reassign. */
     String TRANSFER_CONCURRENT_FIXED_DL_REASSIGN="transfer concurrent fixed dl reassign";

     /** The transfer concurrent dynamic dl reassign. */
     String TRANSFER_CONCURRENT_DYNAMIC_DL_REASSIGN="transfer concurrent dynamic dl reassign";

     /** The transfer concurrent fixed realloc. */
     String TRANSFER_CONCURRENT_FIXED_REALLOC="transfer concurrent fixed realloc";

     /** The transfer concurrent dynamic realloc. */
     String TRANSFER_CONCURRENT_DYNAMIC_REALLOC="transfer concurrent dynamic realloc";

     /** The transfer concurrent dl reassign fixed realloc. */
     String TRANSFER_CONCURRENT_DL_REASSIGN_FIXED_REALLOC="transfer concurrent dl reassign fixed realloc";

     /** The transfer concurrent dl reassign dynamic realloc. */
     String TRANSFER_CONCURRENT_DL_REASSIGN_DYNAMIC_REALLOC="transfer concurrent dl reassign dynamic realloc";

    //5228

    /** The one phase case. */
    //TBF_REQ_CAUSE  &  //ACCESS_GRANTED
    String ONE_PHASE_CASE=" one phase case";

    /** The short access. */
    String SHORT_ACCESS="short access";

    /** The two phase case. */
    String TWO_PHASE_CASE="two phase case";

    /** The page response. */
    String PAGE_RESPONSE="page response";

    /** The cell update. */
    String CELL_UPDATE="cell update";

    /** The mm procedure. */
    String MM_PROCEDURE="mm procedure";

    /** The single block. */
    String SINGLE_BLOCK="single block";

    /** The radio priority 1. */
    //RADIO_PRIORITY
    String RADIO_PRIORITY_1="radio priority 1 ";

    /** The radio priority 2. */
    String RADIO_PRIORITY_2="radio priority 2";

    /** The radio priority 3. */
    String RADIO_PRIORITY_3="radio priority 3";

    /** The radio priority 4. */
    String RADIO_PRIORITY_4="radio priority 4";

    /** The acknowledged mode. */
    //RLC_MODE
    String ACKNOWLEDGED_MODE="acknowledged mode";

    /** The uacknowledged mode. */
    String UACKNOWLEDGED_MODE="unacknowledged mode";

    /** The ts0. */
    //UL_TS_BMAP
    String TS0="tso";

    /** The ts7. */
    String TS7="ts7";


    //B116

    /** The connected mode. */
    //Is Idle Mode
    String CONNECTED_MODE="connected mode";

    /** The idle mode. */
    String IDLE_MODE="idle mode";

    /** The mhz 1 44. */
    //Measurement BW
    String MHZ_1_44="1.44 mhz";

    /** The mhz 3. */
    String MHZ_3="3 mhz";

    /** The mhz 5. */
    String MHZ_5="5 mhz";

    /** The mhz 10. */
    String MHZ_10="10 mhz";

    /** The mhz 15. */
    String MHZ_15="15 mhz";

    /** The mhz 20. */
    String MHZ_20="20 mhz";

    /** The reserved. */
    String RESERVED="reserved";

    //B12C

    /** The phich result 0 is valid. */
    //PHICH Enabled 0
    String PHICH_RESULT_0_IS_VALID="phich result 0 is valid";

    /** The phich result 0 is not valid. */
    String PHICH_RESULT_0_IS_NOT_VALID="phich result 0 is not valid";

    /** The phich result 1 is valid. */
    //PHICH Enabled 1
    String PHICH_RESULT_1_IS_VALID="phich result 1 is valid";

    /** The phich result 1 is not valid. */
    String PHICH_RESULT_1_IS_NOT_VALID="phich result 1 is not valid";

    /** The nak. */
    //Decoding Outcome 0 & Decoding Outcome 1
    String NAK="nak";

    /** The ack. */
    String ACK="ack";

    //B12A & //B130

    /** The pcc. */
    //Carrier Index
    String PCC="pcc";

    /** The scc. */
    String SCC="scc";

    //B130

    /** The agg1. */
    //Aggregation Level
    String AGG1="agg1";

    /** The agg2. */
    String AGG2="agg2";

    /** The agg4. */
    String AGG4="agg4";

    /** The agg8. */
    String AGG8="agg8";

    /** The common. */
    //Search Space Type
    String COMMON="comman";

    /** The user. */
    String USER="user";


   
    /** The TPC_RNTI. */
    String TPC_RNTI="TPC RNTI";
    
    /** The M_RNTI. */
    String M_RNTI="M RNTI";

   
    /** The mismatch. */
    //Tail Match
    String MISMATCH="mismatch";

    /** The match. */
    String MATCH="match";

    /** The success dci0. */
    //Prune Status
    String SUCCESS_DCI0="success dci 0";

    /** The success dci1. */
    String SUCCESS_DCI1="success dci 1";

    /** The success dci1a. */
    String SUCCESS_DCI1A="success dci 1a";

    /** The success dci1c. */
    String SUCCESS_DCI1C="success dci 1c";

    /** The success dci1b 1d. */
    String SUCCESS_DCI1B_1D="success dci 1b 1d";

    /** The success dci2 2a 2b. */
    String SUCCESS_DCI2_2A_2B="success dci 2a 2b";

    /** The success dci3 3a. */
    String SUCCESS_DCI3_3A="success dci3 3a";

    /** The tail mismatch. */
    String TAIL_MISMATCH="tail mismatch";

    /** The fail survivor select. */
    String FAIL_SURVIVOR_SELECT="fail survivor select";

    /** The padding error. */
    String PADDING_ERROR="padding error";

    /** The rb alloc io error. */
    String RB_ALLOC_IO_ERROR="rb alloc io error";

    /** The rb alloc zero rb error type0. */
    String RB_ALLOC_ZERO_RB_ERROR_TYPE0="rb alloc zero rb error type0";

    /** The rb alloc set num error type1. */
    String RB_ALLOC_SET_NUM_ERROR_TYPE1="rb alloc zero num error type1";

    /** The rb alloc zero rb error type1. */
    String RB_ALLOC_ZERO_RB_ERROR_TYPE1="rb alloc zero rb error type1";

    /** The tbs io error. */
    String TBS_IO_ERROR="tbs io error";

    /** The mod order io error. */
    String MOD_ORDER_IO_ERROR="mod order io error";

    /** The duplicate harq id error. */
    String DUPLICATE_HARQ_ID_ERROR="duplicate harq id error";

    /** The unknown dci. */
    String UNKNOWN_DCI="unknown dci";

    /** The unexpected payload size. */
    String UNEXPECTED_PAYLOAD_SIZE="unexpected payload size";

    /** The unexpected dci for tpc. */
    String UNEXPECTED_DCI_FOR_TPC="unexpected dci for tpc";

    /** The tpc pusch prune dci 3 3a. */
    String TPC_PUSCH_PRUNE_DCI_3_3A="tpc pusch prune dci 3 3a";

    /** The bad param pdcch interpret. */
    String BAD_PARAM_PDCCH_INTERPRET="bad param pdcch interpret";

    /** The interpret error. */
    String INTERPRET_ERROR="interpret error";

    /** The bad riv dci0. */
    String BAD_RIV_DCI0="bad riv dci 0";

    /** The rb alloc error dci0. */
    String RB_ALLOC_ERROR_DCI0="rb alloc error dci0";

    /** The invalid rb num dci0. */
    String INVALID_RB_NUM_DCI0="invalid rb num dci0";

    /** The mcs error dci0. */
    String MCS_ERROR_DCI0="mcs error dci0";

    /** The ndi error dci0. */
    String NDI_ERROR_DCI0="ndi error dci0";

    /** The cqi error dci0. */
    String CQI_ERROR_DCI0="cqi error dci0";

    /** The invalid rnti dci1. */
    String INVALID_RNTI_DCI1="invalid rnti dci1";

    /** The num layers error dci1. */
    String NUM_LAYERS_ERROR_DCI1="num layers error dci1";

    /** The bad riv dci1a. */
    String BAD_RIV_DCI1A="bad riv dci1a";

    /** The rb alloc error dci1a. */
    String RB_ALLOC_ERROR_DCI1A="rb alloc error dci1a";

    /** The invalid rb num dci1a. */
    String INVALID_RB_NUM_DCI1A="invalid rb num dci1a";

    /** The num layers error dci1a. */
    String NUM_LAYERS_ERROR_DCI1A="num layers error dci1a";

    /** The return dl data arrival dci1a. */
    String RETURN_DL_DATA_ARRIVAL_DCI1A="return dl data arrival dci1a";

    /** The invalid rnti dci1b 1d. */
    String INVALID_RNTI_DCI1B_1D="invalid rnti dci1b 1d";

    /** The bad riv dci1b 1d. */
    String BAD_RIV_DCI1B_1D="bad riv dci1b 1d";

    /** The rb alloc error dci1b 1d. */
    String RB_ALLOC_ERROR_DCI1B_1D="rb alloc error dci1b 1d";

    /** The invalid rb num dci1b 1d. */
    String INVALID_RB_NUM_DCI1B_1D="invalid rb num dci1b 1d";

    /** The num layers error dci1b 1d. */
    String NUM_LAYERS_ERROR_DCI1B_1D="num layers error dci1b 1d";

    /** The invalid rnti dci1c. */
    String INVALID_RNTI_DCI1C="invalid rnti dci1c";

    /** The bad riv dci1c. */
    String BAD_RIV_DCI1C="bad riv dci1c";

    /** The rb alloc error dci1c. */
    String RB_ALLOC_ERROR_DCI1C="rb alloc error dci1c";

    /** The num layers error dci1c. */
    String NUM_LAYERS_ERROR_DCI1C="num layers error dci1c";

    /** The invalid rnti dci2 2a. */
    String INVALID_RNTI_DCI2_2A="invalid rnti dci2 2a";

    /** The tb en error dci2 2a. */
    String TB_EN_ERROR_DCI2_2A="tb en error dci2 2a";

    /** The pmi error dci2 2a. */
    String PMI_ERROR_DCI2_2A="pmi error dci2 2a";

    /** The sps crnti error dci2 2a. */
    String SPS_CRNTI_ERROR_DCI2_2A="sps crnti error dci2 2a";

    /** The num layers error dci2 2a tb1. */
    String NUM_LAYERS_ERROR_DCI2_2A_TB1="num layers error dci2 2a tb1";

    /** The num layers error dci2 2a tb2. */
    String NUM_LAYERS_ERROR_DCI2_2A_TB2="num layers error dci2 2a tb2";

    /** The tbs io error dci2 2a tb1. */
    String TBS_IO_ERROR_DCI2_2A_TB1="tbs io error dci2 2a tb1";

    /** The tbs io error dci2 2a tb2. */
    String TBS_IO_ERROR_DCI2_2A_TB2="tbs io error dci2 2a tb2";

    /** The tbs 2layer error dci1. */
    String TBS_2LAYER_ERROR_DCI1="tbs 2layer error dci1";

    /** The tbs 2layer error dci2. */
    String TBS_2LAYER_ERROR_DCI2="tbs 2layer error dci2";

    /** The mod order error dci2 2a tb1. */
    String MOD_ORDER_ERROR_DCI2_2A_TB1="mod oder error dci2 2a tb1";

    /** The mod order error dci2 2a tb2. */
    String MOD_ORDER_ERROR_DCI2_2A_TB2="mod order error dci2 2a tb2";

    /** The invalid rnti dci0. */
    String INVALID_RNTI_DCI0="invalid rnti dci0";

    /** The invalid dl grant on mbsfn. */
    String INVALID_DL_GRANT_ON_MBSFN="invalid dl grant on mbsfn";

    /** The invalid harq id. */
    String INVALID_HARQ_ID="invalid harq id";

    /** The invalid dai. */
    String INVALID_DAI="invalid dai";

    /** The sps c rnti validation fail. */
    String SPS_C_RNTI_VALIDATION_FAIL="sps c rnti validation fail";

    /** The sps prune after storing. */
    String SPS_PRUNE_AFTER_STORING="sps prune after storing";

    /** The fail ser engymtrc check. */
    String FAIL_SER_ENGYMTRC_CHECK="fail ser engymtrc check";


    //17F2 LTE

    /** The amr nb. */
    //Codec type
    String AMR_NB="amr nb";

    /** The amr wb. */
    String AMR_WB="amr wb";

    /** The mixed. */
    String MIXED="mixed";

    /** The ipv4. */
    //IP version
    String IPV4="ipv4";

    /** The ipv6. */
    String IPV6="ipv6";

    /** The unknown. */
    String UNKNOWN="unknown";

    //1569

    /** The amr. */
    //Codec type
    String AMR="amr";

    /** The h 264. */
    String H_264="h. 264";

    /** The h 263. */
    String H_263="h_263";

    /** The rtp network loss. */
    //Loss type
    String RTP_NETWORK_LOSS="rtp network loss";

    /** The rtp discrad. */
    String RTP_DISCRAD="rtp discrad";

    /** The vdj enqueue delayed. */
    String VDJ_ENQUEUE_DELAYED="vdj enqueue delayed";

    /** The vdj reset. */
    String VDJ_RESET="vdj reset";

    /** The vdj reassembly failure. */
    String VDJ_REASSEMBLY_FAILURE="vdj reassembly failure";

    /** The vdj redundant. */
    String VDJ_REDUNDANT="vdj redundant";

    /** The vdj out of sync. */
    String VDJ_OUT_OF_SYNC="vdj out of sync";

    /** The vdj out of order. */
    String VDJ_OUT_OF_ORDER="vdj out of order";

    /** The qdj enqueue delayed. */
    String QDJ_ENQUEUE_DELAYED="qdj enqueue delayed";

    /** The qdj redundant. */
    String QDJ_REDUNDANT="qdj redundant";

    /** The qdj optimization 2. */
    String QDJ_OPTIMIZATION_2="qdj optimization 2";

    /** The qdj underflow. */
    String QDJ_UNDERFLOW="qdj underflow";

    /** The qdj reset. */
    String QDJ_RESET="qdj reset";

    /** The qdj out of order. */
    String QDJ_OUT_OF_ORDER="qdj out of order";

    /** The entire packet is dropped. */
    //Number of frames
    String ENTIRE_PACKET_IS_DROPPED="entire packet is dropped";

    /** The indicates the number of frames lost. */
    String INDICATES_THE_NUMBER_OF_FRAMES_LOST="indicates the number of frames lost";

    //B14D LTE

    /** The mode 1 0. */
    //PUCCH Reporting Mode
    String MODE_1_0="mode_1_0";

    /** The mode 1 1. */
    String MODE_1_1="mode_1_1";

    /** The mode 2 0. */
    String MODE_2_0="mode_2_0";

    /** The mode 2 1. */
    String MODE_2_1="mode_2_1";

    /** The type 1 subband cqi feedback. */
    //PUCCH Report Type
    String TYPE_1_SUBBAND_CQI_FEEDBACK="Type 1, SubBand CQI feedback";

    /** The type 2 wideband cqi pmi feedback. */
    String TYPE_2_WIDEBAND_CQI_PMI_FEEDBACK="type 2, wideband cqi pmi feedback";

    /** The type 3 ri feedback. */
    String TYPE_3_RI_FEEDBACK="Type 3, RI Feedback";

    /** The type 4 wideband cqi pmi feedback. */
    String TYPE_4_WIDEBAND_CQI_PMI_FEEDBACK="type 4 wideband cqi pmi feedback";

    /** The type 1a subband cqi pmi 2. */
    String TYPE_1A_SUBBAND_CQI_PMI_2="type 1A, subband cqi pmi 2";

    /** The type 5 ri wideband pmi 1. */
    String TYPE_5_RI_WIDEBAND_PMI_1="Type 5, RI Wideband PMI 1";

    /** The type 6 ri pti. */
    String TYPE_6_RI_PTI="Type 6, RI PTI";

    /** The type 2a wideband pmi. */
    String TYPE_2A_WIDEBAND_PMI="Type 2A, Wideband PMI";

    /** The type 2b wb cqi pmi 1 wb cqi 2. */
    String TYPE_2B_WB_CQI_PMI_1_WB_CQI_2="Type 2B, WB CQI, WB PMI 1, WB CQI 2";

    /** The type 2c wb cqi pmi 1 wb cqi 2. */
    String TYPE_2C_WB_CQI_PMI_1_WB_CQI_2="Type 2C, WB CQI, WB PMI 1, WB CQI 2";

    
    //Carrier Index declare above

    /** The tm invalid. */
    //CSF Tx Mode
    String TM_INVALID="tm invalid";

    /** The tm single ant port0. */
    String TM_SINGLE_ANT_PORT0="tm single ant port0";

    /** The tm td rank1. */
    String TM_TD_RANK1="tm td rank1";

    /** The tm ol sm. */
    String TM_OL_SM="tm ol sm";

    /** The tm cl sm. */
    String TM_CL_SM="tm cl sm";

    /** The tm mu mimo. */
    String TM_MU_MIMO="tm mu mimo";

    /** The tm cl rank1 pc. */
    String TM_CL_RANK1_PC="tm cl rank1 pc";

    /** The tm single ant port5. */
    String TM_SINGLE_ANT_PORT5="tm single ant port5";

    /** The tm8. */
    String TM8="tm8";

    /** The tm9. */
    String TM9="tm9";

    /** The invalid submode. */
    //PUCCH Report Type
    String INVALID_SUBMODE="invalid submode";

    /** The submode 1. */
    String SUBMODE_1="submode 1";

    /** The submode 2. */
    String SUBMODE_2="submode 2";

    //1612 LTE ( Event code)

    /** The s failure. */
    //cause
    String S_FAILURE = "s failure";

    /** The sib read failure. */
    String SIB_READ_FAILURE = "sib read failure";

    /** The dl weak ind. */
    String DL_WEAK_IND = "dl weak ind";

    /** The cell barred. */
    String CELL_BARRED = "cell barred";

    /** The non eq plmn. */
    String NON_EQ_PLMN="non eq plmn";

    /** The forbidden ta. */
    String FORBIDDEN_TA="forbidden ta";

    /** The forbidden csg id. */
    String FORBIDDEN_CSG_ID="forbidden csg id";

    /** The irat resel failure. */
    String IRAT_RESEL_FAILURE="irat resel failure";

    /** The freq barred. */
    String FREQ_BARRED="freq barred";

    //1613 LTE ( Event code)

    /** The invalid cfg. */
    //Cause
    String INVALID_CFG="invalid cfg";

    /** The cphy. */
    String CPHY="cphy";

    /** The rach. */
    String RACH="rach";

    /** The rach meas. */
    String RACH_MEAS="rach meas";

    // 3G

    /** The primary antenna and rxd mode0. */
    //AntennaRxdInfo
    String PRIMARY_ANTENNA_AND_RXD_MODE0= "primary antenna and rxd mode 0";

    /** The secondary antenna and rxd mode0. */
    String SECONDARY_ANTENNA_AND_RXD_MODE0 ="secondary antenna and rxd mode 0";

    /** The both antenna in rxd add mode. */
    String BOTH_ANTENNA_IN_RXD_ADD_MODE ="both antenna in rxd and mode";

    /** The both antenna in rxd separate mode. */
    String BOTH_ANTENNA_IN_RXD_SEPARATE_MODE ="both antenna in rxd separate mode";

    /** The primary carrier. */
    String PRIMARY_CARRIER = "primary carrier";

    /** The secondary carrier. */
    String SECONDARY_CARRIER = "secondary carrier";

    /** The intra freq. */
    //SET
    String INTRA_FREQ ="intra freq";

    /** The inter freq. */
    String INTER_FREQ ="inter freq";

    /** The aset. */
    String ASET ="aset";

    /** The intra freq monitored set. */
    String INTRA_FREQ_MONITORED_SET ="intra freq monitored set";

    /** The intra freq unlisted set. */
    String INTRA_FREQ_UNLISTED_SET ="intra freq unlisted set";

    /** The inter freq monitored set. */
    String INTER_FREQ_MONITORED_SET ="inter freq monitored set";

    /** The inter freq virtual active set. */
    String INTER_FREQ_VIRTUAL_ACTIVE_SET="inter freq virtual active set";

    /** The no mbms. */
    String NO_MBMS ="no mbms";

    /** The sync. */
    String SYNC ="sync";

    /** The antenna0. */
    String ANTENNA0="antenna 0";

    /** The antenna1. */
    String ANTENNA1="antenna 1";


    //B12E LTE

    /** The fail. */
    //CRC
    String FAIL = "fail";

    /** The pass. */
    String PASS = "pass";


    //B0C0 LTE

    /** The bcch dl sch message. */
    //PDU_NUM
    String BCCH_DL_SCH_MESSAGE ="BCCH_DL_SCH_MESSAGE";

    /** The mcch message. */
    String MCCH_MESSAGE ="MCCH_MESSAGE";

    /** The pcch message. */
    String PCCH_MESSAGE ="PCCH_MESSAGE";

    /** The dl ccch message. */
    String DL_CCCH_MESSAGE ="DL_CCCH_MESSAGE";

    /** The dl dcch message. */
    String DL_DCCH_MESSAGE="DL_DCCH_MESSAGE";

    /** The ul ccch message. */
    String UL_CCCH_MESSAGE="UL_CCCH_MESSAGE";

    /** The ul dcch message. */
    String UL_DCCH_MESSAGE="UL_DCCH_MESSAGE";

    /** The systeminfomationblocktype1. */
    String SYSTEMINFOMATIONBLOCKTYPE1 =" systeminformationblocktype1";

    /** The systeminfomationblocktype1 v8h0 ies. */
    String SYSTEMINFOMATIONBLOCKTYPE1_V8H0_IES ="systeminformationblocktype1 v8ho ies";

    /** The systeminfomationblocktype2 v8h0 ies. */
    String SYSTEMINFOMATIONBLOCKTYPE2_V8H0_IES = "systeminformationblocktype2 v8ho ies";

    /** The systeminfomationblocktype5 v8h0 ies. */
    String SYSTEMINFOMATIONBLOCKTYPE5_V8H0_IES ="systeminformationblocktype5 v8ho ies";

    /** The systeminfomationblocktype6 v8h0 ies. */
    String SYSTEMINFOMATIONBLOCKTYPE6_V8H0_IES ="systeminformationblocktype6 v8ho ies";

    /** The ue eutra capability message. */
    String UE_EUTRA_CAPABILITY_MESSAGE="ue eutra capability message";

    /** The ue eutra capability v9a0 ies. */
    String UE_EUTRA_CAPABILITY_V9A0_IES="ue eutra capability v9a0 ies";

    /** The ue eutra capability. */
    String UE_EUTRA_CAPABILITY="UE EUTRA Capability";

    /** The varshortmac input. */
    String VARSHORTMAC_INPUT ="varshortmac input";

    /** The logmeasinfo r10. */
    String LOGMEASINFO_R10="LogMeasInfo_r10";

    /** The system information block type1 message. */
    String SYSTEM_INFORMATION_BLOCK_TYPE1_MESSAGE ="system information Block Type1 Message";
    //version 9

    /** The locationinfo ellipsoid point r10. */
    String LOCATIONINFO_ELLIPSOID_POINT_R10 ="LocationInfo_ellipsoid_Point_r10";

    /** The locationinfo ellipsoidpointwithuncertaintycircle r11. */
    String LOCATIONINFO_ELLIPSOIDPOINTWITHUNCERTAINTYCIRCLE_R11 ="LocationInfo_ellipsoidPointWithUncertaintyCircle_r11";

    /** The locationinfo ellipsoidpointwithuncertaintyellipse r11. */
    String LOCATIONINFO_ELLIPSOIDPOINTWITHUNCERTAINTYELLIPSE_R11 = "LocationInfo_ellipsoidPointWithUncertaintyEllipse_r11";

    /** The locationinfo ellipsoidpointwithaltitude r10. */
    String LOCATIONINFO_ELLIPSOIDPOINTWITHALTITUDE_R10 ="LocationInfo_ellipsoidPointWithAltitude_r10";

    /** The locationinfo ellipsoidpointwithaltitudeanduncertaintyellipsoid r11. */
    String LOCATIONINFO_ELLIPSOIDPOINTWITHALTITUDEANDUNCERTAINTYELLIPSOID_R11 ="LocationInfo_ellipsoidPointWithAltitudeAndUncertaintyEllipsoid_r11";

    /** The ellipsoidarc. */
    String ELLIPSOIDARC ="EllipsoidArc";

    /** The horizontalvelocity. */
    String HORIZONTALVELOCITY ="HorizontalVelocity";

    /** The bcch bch message. */
    String BCCH_BCH_MESSAGE ="bcch bch message";

    /** The ueinformationresponse v9ao ies. */
    String UEINFORMATIONRESPONSE_V9AO_IES ="UEInformationResponse_v9e0_IEs";

    /** The lte rrc rrcconnectionrelease v9ae0 ies pdu. */
    //version 10
    String LTE_RRC_RRCCONNECTIONRELEASE_V9AE0_IES_PDU ="lte_rrc_RRCConnectionRelease_v9e0_IEs_PDU";

    /** The lte rrc bcch bch message pdu. */
    //version 13
    String LTE_RRC_BCCH_BCH_MESSAGE_PDU ="lte_rrc_BCCH_BCH_Message_PDU";

    /** The lte rrc bcch dl sch message pdu. */
    String LTE_RRC_BCCH_DL_SCH_MESSAGE_PDU ="lte_rrc_BCCH_DL_SCH_Message_PDU";

    /** The lte rrc mcch message pdu. */
    String LTE_RRC_MCCH_MESSAGE_PDU ="lte_rrc_MCCH_Message_PDU";

    /** The lte rrc pcch message pdu. */
    String LTE_RRC_PCCH_MESSAGE_PDU ="lte_rrc_PCCH_Message_PDU";

    /** The lte rrc dl ccch message pdu. */
    String LTE_RRC_DL_CCCH_MESSAGE_PDU ="lte_rrc_DL_CCCH_Message_PDU";

    /** The lte rrc dl dcch message pdu. */
    String LTE_RRC_DL_DCCH_MESSAGE_PDU ="lte_rrc_DL_DCCH_Message_PDU";

    /** The lte rrc ul ccch message pdu. */
    String LTE_RRC_UL_CCCH_MESSAGE_PDU ="lte_rrc_UL_CCCH_Message_PDU";

    /** The lte rrc ul dcch message pdu. */
    String LTE_RRC_UL_DCCH_MESSAGE_PDU ="lte_rrc_UL_DCCH_Message_PDU";

    /** The lte rrc rrcconnectionreconfiguration pdu. */
    String LTE_RRC_RRCCONNECTIONRECONFIGURATION_PDU="lte_rrc_RRCConnectionReconfiguration_PDU";

    /** The lte rrc rrcconnectionreconfiguration v8mo ies pdu. */
    String LTE_RRC_RRCCONNECTIONRECONFIGURATION_V8MO_IES_PDU ="lte_rrc_RRCConnectionReconfiguration_v8m0_IEs_PDU";

    /** The lte rrc rrcconnectionreconfigurationicomplete pdu. */
    String LTE_RRC_RRCCONNECTIONRECONFIGURATIONICOMPLETE_PDU ="lte_rrc_RRCConnectionReconfigurationComplete_PDU";

    /** The lte rrc rrcconnectionrelease v9e0 ies pdu. */
    String LTE_RRC_RRCCONNECTIONRELEASE_V9E0_IES_PDU="lte_rrc_RRCConnectionRelease_v9e0_IEs_PDU";

    /** The lte rrc systeminfomationblocktype1 pdu. */
    String LTE_RRC_SYSTEMINFOMATIONBLOCKTYPE1_PDU ="lte_rrc_SystemInformationBlockType1_PDU";

    /** The lte rrc systeminfomationblocktype1 v8h0 ies pdu. */
    String LTE_RRC_SYSTEMINFOMATIONBLOCKTYPE1_V8H0_IES_PDU="lte_rrc_SystemInformationBlockType1_v8h0_IEs_PDU";

    /** The lte rrc uecapabilityenquiry pdu. */
    String LTE_RRC_UECAPABILITYENQUIRY_PDU="lte_rrc_UECapabilityEnquiry_PDU";

    /** The lte rrc uecapabilityinformation pdu. */
    String LTE_RRC_UECAPABILITYINFORMATION_PDU="lte_rrc_UECapabilityInformation_PDU";

    /** The lte rrc ueinformationresponse v9e0 ies pdu. */
    String LTE_RRC_UEINFORMATIONRESPONSE_V9E0_IES_PDU="lte_rrc_UEInformationResponse_v9e0_IEs_PDU";

    /** The lte rrc systeminformationblocktype2 v8ho ies pdu. */
    String LTE_RRC_SYSTEMINFORMATIONBLOCKTYPE2_V8HO_IES_PDU="lte_rrc_SystemInformationBlockType2_v8h0_IEs_PDU";

    /** The lte rrc systeminformationblocktype5 v8h0 ies pdu. */
    String LTE_RRC_SYSTEMINFORMATIONBLOCKTYPE5_V8H0_IES_PDU="lte_rrc_SystemInformationBlockType5_v8h0_IEs_PDU";

    /** The lte rrc systeminformationblocktype6 v8ho ies pdu. */
    String LTE_RRC_SYSTEMINFORMATIONBLOCKTYPE6_V8HO_IES_PDU="lte_rrc_SystemInformationBlockType6_v8h0_IEs_PDU";

    /** The lte rrc tdd configsl r12 pdu. */
    String LTE_RRC_TDD_CONFIGSL_R12_PDU ="lte_rrc_TDD_ConfigSL_r12_PDU";

    /** The lte rrc ellipsoid point pdu. */
    String LTE_RRC_ELLIPSOID_POINT_PDU="lte_rrc_Ellipsoid_Point_PDU";

    /** The lte rrc ellipsoidpointwithaltitude pdu. */
    String LTE_RRC_ELLIPSOIDPOINTWITHALTITUDE_PDU="lte_rrc_EllipsoidPointWithAltitude_PDU";

    /** The lte rrc ellipsoidpointwithaltitudeanduncertaintyellipsoid pdu. */
    String LTE_RRC_ELLIPSOIDPOINTWITHALTITUDEANDUNCERTAINTYELLIPSOID_PDU="lte_rrc_EllipsoidPointWithAltitudeAndUncertaintyEllipsoid_PDU";

    /** The lte rrc ellipsoidarc pdu. */
    String LTE_RRC_ELLIPSOIDARC_PDU="lte_rrc_EllipsoidArc_PDU";

    /** The lte rrc ellipsoid pointwithuncertaintycircle pdu. */
    String LTE_RRC_ELLIPSOID_POINTWITHUNCERTAINTYCIRCLE_PDU="lte_rrc_Ellipsoid_PointWithUncertaintyCircle_PDU";

    /** The lte rrc ellipsoidpointwithuncertaintyellipse pdu. */
    String LTE_RRC_ELLIPSOIDPOINTWITHUNCERTAINTYELLIPSE_PDU="lte_rrc_EllipsoidPointWithUncertaintyEllipse_PDU";

    /** The lte rrc horizontalvelocity pdu. */
    String LTE_RRC_HORIZONTALVELOCITY_PDU="lte_rrc_HorizontalVelocity_PDU";

    /** The lte rrc polygon pdu. */
    String LTE_RRC_POLYGON_PDU="lte_rrc_Polygon_PDU";

    /** The lte rrc measurementreferencetime pdu. */
    String LTE_RRC_MEASUREMENTREFERENCETIME_PDU="lte_rrc_MeasurementReferenceTime_PDU";

    /** The lte rrc rsrp rangesl3 r12 pdu. */
    String LTE_RRC_RSRP_RANGESL3_R12_PDU="lte_rrc_RSRP_RangeSL3_r12_PDU";

    /** The lte rrc ue eutra capability pdu. */
    String LTE_RRC_UE_EUTRA_CAPABILITY_PDU="lte_rrc_UE_EUTRA_Capability_PDU";

    /** The lte rrc ue eutra capability v9a0 ies pdu. */
    String LTE_RRC_UE_EUTRA_CAPABILITY_V9A0_IES_PDU="lte_rrc_UE_EUTRA_Capability_v9a0_IEs_PDU";

    /** The lte rrc varshortmac input pdu. */
    String LTE_RRC_VARSHORTMAC_INPUT_PDU = "lte_rrc_VarShortMAC_Input_PDU";

    /** The lte rrc els sib1 signature pdu. */
    String LTE_RRC_ELS_SIB1_SIGNATURE_PDU="lte_rrc_ELS_SIB1_signature_PDU";

    /** The lte rrc els systeminformationblcoktype1 pdu. */
    String LTE_RRC_ELS_SYSTEMINFORMATIONBLCOKTYPE1_PDU="lte_rrc_ELS_SystemInformationBlockType1_PDU";

    /** The lte rrc nhn plmn identitylist pdu. */
    String LTE_RRC_NHN_PLMN_IDENTITYLIST_PDU="lte_rrc_NHN_PLMN_IdentityList_PDU";

    /** The lte rrc els dl ccch message pdu. */
    String LTE_RRC_ELS_DL_CCCH_MESSAGE_PDU= "lte_rrc_ELS_DL_CCCH_Message_PDU";

    /** The lte rrc els dl dcch message pdu. */
    String LTE_RRC_ELS_DL_DCCH_MESSAGE_PDU="lte_rrc_ELS_DL_DCCH_Message_PDU";

    /** The lte rrc els ul dcch message pdu. */
    String LTE_RRC_ELS_UL_DCCH_MESSAGE_PDU="lte_rrc_ELS_UL_DCCH_Message_PDU";
    //version 3 same as versoin 8





    /** The channel type 01. */
    String CHANNEL_TYPE_01="DL_BCCH";

    /** The channel type 02. */
    String CHANNEL_TYPE_02="DL_PCCH";

    /** The channel type 03. */
    String CHANNEL_TYPE_03="DL_CCCH";

    /** The channel type 04. */
    String CHANNEL_TYPE_04="DL_DCCH";

    /** The channel type 05. */
    String CHANNEL_TYPE_05="UL_CCCH";

    /** The channel type 06. */
    String CHANNEL_TYPE_06="UL_DCCH";

    /** The message type 00. */
    String MESSAGE_TYPE_00="MasterInformationBlock";

    /** The message type 01. */
    String MESSAGE_TYPE_01="SystemInformationBlockType1";

    /** The message type 02. */
    String MESSAGE_TYPE_02="SystemInformationBlockType2";

    /** The message type 03. */
    String MESSAGE_TYPE_03="SystemInformationBlockType3";

    /** The message type 04. */
    String MESSAGE_TYPE_04="SystemInformationBlockType4";

    /** The message type 05. */
    String MESSAGE_TYPE_05="SystemInformationBlockType5";

    /** The message type 06. */
    String MESSAGE_TYPE_06="SystemInformationBlockType6";

    /** The message type 07. */
    String MESSAGE_TYPE_07="SystemInformationBlockType7";

    /** The message type 08. */
    String MESSAGE_TYPE_08="SystemInformationBlockType8";

    /** The message type 09. */
    String MESSAGE_TYPE_09="SystemInformationBlockType9";

    /** The message type 0a. */
    String MESSAGE_TYPE_0A="SystemInformationBlockType10";

    /** The message type 0b. */
    String MESSAGE_TYPE_0B="SystemInformationBlockType11";

    /** The message type 40. */
    String MESSAGE_TYPE_40="Paging";

    /** The message type 48. */
    String MESSAGE_TYPE_48="RRCConnectionReestablishment";

    /** The message type 49. */
    String MESSAGE_TYPE_49="RRCConnectionReestablishmentReject";

    /** The message type 4a. */
    String MESSAGE_TYPE_4A="RRCConnectionReject";

    /** The message type 4b. */
    String MESSAGE_TYPE_4B="RRCConnectionSetup";

    /** The message type 80. */
    String MESSAGE_TYPE_80="CSFBParametersRequestCDMA2000";

    /** The message type 81. */
    String MESSAGE_TYPE_81="DLInformationTransfer";

    /** The message type 82. */
    String MESSAGE_TYPE_82="HandoverFromEUTRAPreparationRequest";

    /** The message type 83. */
    String MESSAGE_TYPE_83="MobilityFromEUTRACommand";

    /** The message type 84. */
    String MESSAGE_TYPE_84="RRCConnectionReconfiguration";

    /** The message type 85. */
    String MESSAGE_TYPE_85="RRCConnectionRelease";

    /** The message type 86. */
    String MESSAGE_TYPE_86="SecurityModeCommand";

    /** The message type 87. */
    String MESSAGE_TYPE_87="UECapabilityEnquiry";

    /** The message type 88. */
    String MESSAGE_TYPE_88="CounterCheck";


    /** The message type 00 1910. */
    String MESSAGE_TYPE_00_1910="RRC Connection restablishment request";

    /** The message type 01 1910. */
    String MESSAGE_TYPE_01_1910="RRC ConnectionRequest";

    /** The message type 80 1910. */
    String MESSAGE_TYPE_80_1910="CSFB Parameters Request CDMA 2000";

    /** The message type 81 1910. */
    String MESSAGE_TYPE_81_1910="MeasurementReport";

    /** The message type 82 1910. */
    String MESSAGE_TYPE_82_1910="RRC Connection Reconfiguration Complete";

    /** The message type 83 1910. */
    String MESSAGE_TYPE_83_1910="RRC Connection Re establishment Complete";

    /** The message type 84 1910. */
    String MESSAGE_TYPE_84_1910="RRC Connection Setup Complete";

    /** The message type 85 1910. */
    String MESSAGE_TYPE_85_1910="Security Mode Complete";

    /** The message type 86 1910. */
    String MESSAGE_TYPE_86_1910="Security Mode Failure";

    /** The message type 87 1910. */
    String MESSAGE_TYPE_87_1910="UE Capability Information";

    /** The message type 88 1910. */
    String MESSAGE_TYPE_88_1910="UL Handover Preparation Transfer";

    /** The message type 89 1910. */
    String MESSAGE_TYPE_89_1910="UL Information Transfer";

    /** The message type 8a 1910. */
    String MESSAGE_TYPE_8A_1910="Counter Check Response";



    /** The bcch bch message type. */
    String BCCH_BCH_MESSAGE_TYPE="masterInformationBlock";

    /** The pcch message type. */
    String PCCH_MESSAGE_TYPE ="paging-r9";

    /** The ue eutra capability type. */
    String UE_EUTRA_CAPABILITY_TYPE ="UE-EUTRA-Capability";

    /** The var short mac input type. */
    String VAR_SHORT_MAC_INPUT_TYPE ="VarShortMAC-Input";

    /** The ue eutra capability v9a0 ies type. */
    String UE_EUTRA_CAPABILITY_V9A0_IES_TYPE ="UE-EUTRA-Capability-v9a0-IEs";

    /** The system information block type 1 type. */
    String SYSTEM_INFORMATION_BLOCK_TYPE_1_TYPE ="SystemInformationBlockType1";


    /** The emm null. */
    String EMM_NULL="EMM NULL";

    /** The emm deregistered. */
    String EMM_DEREGISTERED="EMM DEREGISTERED";

    /** The emm registered initiated. */
    String EMM_REGISTERED_INITIATED="EMM REGISTERED INITIATED";

    /** The emm registered. */
    String EMM_REGISTERED="EMM REGISTERED";

    /** The emm tracking area updating initiated. */
    String EMM_TRACKING_AREA_UPDATING_INITIATED="EMM TRACKING AREA UPDATING INITIATED";

    /** The emm service request initiated. */
    String EMM_SERVICE_REQUEST_INITIATED="EMM SERVICE REQUEST INITIATED";

    /** The emm invalid state. */
    String EMM_INVALID_STATE="EMM INVALID STATE";

    /** The emm deregistered initiated. */
    String EMM_DEREGISTERED_INITIATED="EMM DEREGISTERED INITIATED";

    /** The emm deregistered no imsi. */
    String EMM_DEREGISTERED_NO_IMSI="EMM DEREGISTERED NO IMSI";

    /** The emm deregistered plmn search. */
    String EMM_DEREGISTERED_PLMN_SEARCH="EMM DEREGISTERED PLMN SEARCH";

    /** The emm deregistered attach needed. */
    String EMM_DEREGISTERED_ATTACH_NEEDED="EMM DEREGISTERED ATTACH NEEDED";

    /** The emm deregistered no cell available. */
    String EMM_DEREGISTERED_NO_CELL_AVAILABLE="EMM DEREGISTERED NO CELL AVAILABLE";

    /** The emm deregistered attempting to attach. */
    String EMM_DEREGISTERED_ATTEMPTING_TO_ATTACH="EMM DEREGISTERED ATTEMPTING TO ATTACH";

    /** The emm deregistered normal service. */
    String EMM_DEREGISTERED_NORMAL_SERVICE="EMM DEREGISTERED NORMAL SERVICE";

    /** The emm deregistered limited service. */
    String EMM_DEREGISTERED_LIMITED_SERVICE="EMM DEREGISTERED LIMITED SERVICE";

    /** The emm registered normal service. */
    String EMM_REGISTERED_NORMAL_SERVICE="EMM REGISTERED NORMAL SERVICE";

    /** The emm registered update needed. */
    String EMM_REGISTERED_UPDATE_NEEDED="EMM REGISTERED UPDATE NEEDED";

    /** The emm registered attempting to update. */
    String EMM_REGISTERED_ATTEMPTING_TO_UPDATE="EMM REGISTERED ATTEMPTING TO UPDATE";

    /** The emm registered no cell available. */
    String EMM_REGISTERED_NO_CELL_AVAILABLE="EMM REGISTERED NO CELL AVAILABLE";

    /** The emm registered plmn search. */
    String EMM_REGISTERED_PLMN_SEARCH="EMM REGISTERED PLMN SEARCH";

    /** The emm registered limited service. */
    String EMM_REGISTERED_LIMITED_SERVICE="EMM REGISTERED LIMITED SERVICE";

    /** The emm waiting for nw response. */
    String EMM_WAITING_FOR_NW_RESPONSE="EMM WAITING FOR NW RESPONSE";

    /** The emm waiting for esm respons. */
    String EMM_WAITING_FOR_ESM_RESPONS="EMM WAITING FOR ESM RESPONS";

  

    //B1B9 LTE

    /** The event hp event. */
    String EVENT_HP_EVENT = "event hp event";

    /** The event start time. */
    String EVENT_START_TIME ="event start time";

    /** The event end time. */
    String EVENT_END_TIME ="event end time";

    /** The dl earfcn. */
    String DL_EARFCN ="dl earfcn";

    /** The ul earfcn. */
    String UL_EARFCN ="ul earfcn";

    /** The dl bandwidth. */
    String DL_BANDWIDTH="dl bandwidth";

    /** The ul bandwidth. */
    String UL_BANDWIDTH ="ul bandwidth";

    /** The frame struct. */
    String FRAME_STRUCT="frame struct";

    /** The tdd config. */
    String TDD_CONFIG="tdd config";

    /** The tdd ssp. */
    String TDD_SSP="tdd ssp";

    /** The tdd dl cp. */
    String TDD_DL_CP="tdd dl cp";

    /** The tdd ul cp. */
    String TDD_UL_CP="tdd ul cp";

    /** The int evt rpt. */
    String INT_EVT_RPT="int evt rpt";

    /** The mask num. */
    String MASK_NUM ="mask num";

    /** The fdd. */
    //Frame Structure
    String FDD ="fdd";

    /** The tdd. */
    String TDD ="tdd";

    /** The normal cp. */
    String NORMAL_CP ="normal cp";

    /** The extended cp. */
    String EXTENDED_CP = "extended cp";

    /** The mbsfn cp. */
    String MBSFN_CP ="mbsfn cp";

    /** The not sent. */
    String NOT_SENT ="not sent";

    /** The sent. */
    String SENT="sent";


    /** The lte bw idx nrb 6. */
    String LTE_BW_IDX_NRB_6="lte bw idx nrb 6";

    /** The lte bw idx nrb 15. */
    String LTE_BW_IDX_NRB_15="lte bw idx nrb 15";

    /** The lte bw idx nrb 25. */
    String LTE_BW_IDX_NRB_25="lte bw idx nrb 25";

    /** The lte bw idx nrb 50. */
    String LTE_BW_IDX_NRB_50="lte bw idx nrb 50";

    /** The lte bw idx nrb 75. */
    String LTE_BW_IDX_NRB_75="lte bw idx nrb 75";

    /** The lte bw idx nrb 100. */
    String LTE_BW_IDX_NRB_100="lte bw idx nrb 100";

    /** The init. */
    //State ( B131 LTE)
    String INIT ="init";

    /** The acq. */
    String ACQ ="acq";

    /** The acq no adj. */
    String ACQ_NO_ADJ="acq no adj";

    /** The one shot. */
    String ONE_SHOT="one shot";

    /** The track. */
    String TRACK = "track";

    /** The track no adj. */
    String TRACK_NO_ADJ="track no adj";

   

    /** The do not delay. */
    String DO_NOT_DELAY="do not delay";

    /** The delay ul transmission. */
    String DELAY_UL_TRANSMISSION="delay ul transmission";

    /** The bpsk. */
    String BPSK="BPSK";

    /** The qpsk. */
    String QPSK="QPSK";

    /** The qam 16. */
    String QAM_16="16 QAM";

    /** The qam 64. */
    String QAM_64= "64 QAM";

    /** The qam 256. */
    String QAM_256= "256 QAM";


    /** The cdma hrpd. */
    //2480 (LTE )
    String CDMA_HRPD ="cdma hprd";

    /** The cdma 1x. */
    String  CDMA_1X= "cdma 1x";

    /** The gsm. */
    String GSM ="gsm";

    /** The wcdma. */
    String WCDMA ="wcdma";

    /** The lte. */
    String LTE="lte";

    /** The tdscdma. */
    String TDSCDMA="tdscdma";

    /** The active pending. */
    //B0E4
    String ACTIVE_PENDING ="active pending";

    /** The active. */
    String ACTIVE ="active";

    /** The modify. */
    String MODIFY="modify";

    /** The default. */
    //B0E5
    String DEFAULT ="default";

    /** The dedicated. */
    String DEDICATED ="dedicated";

    /** The aborting. */
    String ABORTING ="aborting";



    /** The utra handover. */
    //1615
    String UTRA_HANDOVER = "utra handover";

    /** The geran handover. */
    String GERAN_HANDOVER ="geran handover";

    /** The geran cco. */
    String GERAN_CCO = "geran cco";

    /** The hrpd handover. */
    String HRPD_HANDOVER= "hrpd handover";

    /** The aborted. */
    //1616
    String ABORTED ="aborted ";

    /** The failed unknown. */
    String FAILED_UNKNOWN ="failed unknown";

    /** The failed invalid state. */
    String FAILED_INVALID_STATE ="failed invalid state";

    /** The failed protocol error. */
    String FAILED_PROTOCOL_ERROR ="failed protocol error";

    /** The failed invalid configuration. */
    String FAILED_INVALID_CONFIGURATION ="failed invalid configuration";

    /** The failed physical channel failer. */
    String FAILED_PHYSICAL_CHANNEL_FAILER ="failed physical channel failer";

    /** The failed invalid cipher alogrithm. */
    String FAILED_INVALID_CIPHER_ALOGRITHM ="failed invalid cipher alogrithm";

    /** The failed unsupported frequency. */
    String FAILED_UNSUPPORTED_FREQUENCY = "failed unsupported frequency";

    /** The failed message invalid. */
    String FAILED_MESSAGE_INVALID ="failed message invalid";

    /** The failed unexpected message. */
    String FAILED_UNEXPECTED_MESSAGE="failed unexpected message";

    /** The failed acq failed. */
    String FAILED_ACQ_FAILED ="failed acq failed";

    /** The failed call release release order. */
    String FAILED_CALL_RELEASE_RELEASE_ORDER="failed call release release order";

    /** The failed call release reorder. */
    String FAILED_CALL_RELEASE_REORDER ="failed call release reorder";

    /** The failed call release intercept order. */
    String FAILED_CALL_RELEASE_INTERCEPT_ORDER ="failed call release intercept order";

    /** The failed call release normal. */
    String FAILED_CALL_RELEASE_NORMAL ="failed call release normal";

    /** The failed call release so reject. */
    String FAILED_CALL_RELEASE_SO_REJECT="failed call release so reject";

    /** The failed call release otasp spc error. */
    String FAILED_CALL_RELEASE_OTASP_SPC_ERROR="failed call release otasp spc error";

    /** The Constant NAS_ESM_BEARER_RESOURCE_ALLOC_REQ. */
    //log code 1627
    String NAS_ESM_BEARER_RESOURCE_ALLOC_REQ = "NAS_ESM_BEARER_RESOURCE_ALLOC_REQ";

    /** The Constant NAS_ESM_PDN_CONNECTIVTY_REQ. */
    String NAS_ESM_PDN_CONNECTIVTY_REQ = "NAS_ESM_PDN_CONNECTIVTY_REQ";

    /** The Constant NAS_ESM_PDN_DISCONNECT_REQ. */
    String NAS_ESM_PDN_DISCONNECT_REQ = "NAS_ESM_PDN_DISCONNECT_REQ";

    /** The Constant NAS_ESM_BEARER_RESOURCE_ALLOC_ABORT_REQ. */
    String NAS_ESM_BEARER_RESOURCE_ALLOC_ABORT_REQ = "NAS_ESM_BEARER_RESOURCE_ALLOC_ABORT_REQ";

    /** The Constant NAS_ESM_PDN_CONNECTIVITY_ABORT_REQ. */
    String NAS_ESM_PDN_CONNECTIVITY_ABORT_REQ = "NAS_ESM_PDN_CONNECTIVITY_ABORT_REQ";

    /** The Constant NAS_ESM_BEARER_RESOURCE_MODIFICATION_REQ. */
    String NAS_ESM_BEARER_RESOURCE_MODIFICATION_REQ = "NAS_ESM_BEARER_RESOURCE_MODIFICATION_REQ";


    /** The Constant MM_CM_1XCSFB_ABORT_RSP. */
    //log code 1628
    String MM_CM_1XCSFB_ABORT_RSP = "MM_CM_1XCSFB_ABORT_RSP";

    /** The Constant MM_CM_1XCSFB_CALL_RSP. */
    String MM_CM_1XCSFB_CALL_RSP = "MM_CM_1XCSFB_CALL_RSP";

    /** The Constant MM_CM_ACT_DEDICATED_BEARER_CONTEXT_REQUEST_IND. */
    String MM_CM_ACT_DEDICATED_BEARER_CONTEXT_REQUEST_IND = "MM_CM_ACT_DEDICATED_BEARER_CONTEXT_REQUEST_IND";

    /** The Constant MM_CM_DEACT_BEARER_CONTEXT_REQUEST_IND. */
    String MM_CM_DEACT_BEARER_CONTEXT_REQUEST_IND = "MM_CM_DEACT_BEARER_CONTEXT_REQUEST_IND";

    /** The Constant MM_CM_BEARER_RESOURCE_ALLOC_REJECT_IND. */
    String MM_CM_BEARER_RESOURCE_ALLOC_REJECT_IND = "MM_CM_BEARER_RESOURCE_ALLOC_REJECT_IND";

    /** The Constant MM_CM_MODIFY_BEARER_CONTEXT_REQUEST_IND. */
    String MM_CM_MODIFY_BEARER_CONTEXT_REQUEST_IND = "MM_CM_MODIFY_BEARER_CONTEXT_REQUEST_IND";

    /** The Constant MM_CM_PDN_CONNECTIVITY_REJECT_IND. */
    String MM_CM_PDN_CONNECTIVITY_REJECT_IND = "MM_CM_PDN_CONNECTIVITY_REJECT_IND";

    /** The Constant MM_CM_PDN_DISCONNECT_REJECT_IND. */
    String MM_CM_PDN_DISCONNECT_REJECT_IND = "MM_CM_PDN_DISCONNECT_REJECT_IND";

    /** The Constant MM_CM_ACT_DRB_RELEASED_IND. */
    String MM_CM_ACT_DRB_RELEASED_IND = "MM_CM_ACT_DRB_RELEASED_IND";

    /** The Constant MM_CM_DRB_SETUP_IND. */
    String MM_CM_DRB_SETUP_IND = "MM_CM_DRB_SETUP_IND";

    /** The Constant MM_CM_PDN_CONNECTIVITY_FAILURE_IND. */
    String MM_CM_PDN_CONNECTIVITY_FAILURE_IND = "MM_CM_PDN_CONNECTIVITY_FAILURE_IND";

    /** The Constant MM_CM_BEARER_RESOURCE_ALLOC_FAILURE_IND. */
    String MM_CM_BEARER_RESOURCE_ALLOC_FAILURE_IND = "MM_CM_BEARER_RESOURCE_ALLOC_FAILURE_IND";

    /** The Constant MM_CM_DRB_REESTABLISH_REJECT_IND. */
    String MM_CM_DRB_REESTABLISH_REJECT_IND = "MM_CM_DRB_REESTABLISH_REJECT_IND";

    /** The Constant MM_CM_GET_PDN_CONNECTIVITY_REQUEST_IND. */
    String MM_CM_GET_PDN_CONNECTIVITY_REQUEST_IND = "MM_CM_GET_PDN_CONNECTIVITY_REQUEST_IND";

    /** The Constant MM_CM_BEARER_CONTEXT_MODIFY_REJECT_IND. */
    String MM_CM_BEARER_CONTEXT_MODIFY_REJECT_IND = "MM_CM_BEARER_CONTEXT_MODIFY_REJECT_IND";

    /** The Constant NAS_ESM_DATA_IND. */
    // log code 1635
    String NAS_ESM_DATA_IND = "NAS_ESM_DATA_IND";

    /** The Constant NAS_EMM_1XCSFB_ESR_CALL_REQ. */
    String NAS_EMM_1XCSFB_ESR_CALL_REQ = "NAS_EMM_1XCSFB_ESR_CALL_REQ";

    /** The Constant NAS_EMM_1XCSFB_ESR_CALL_ABORT_REQ. */
    String NAS_EMM_1XCSFB_ESR_CALL_ABORT_REQ = "NAS_EMM_1XCSFB_ESR_CALL_ABORT_REQ";


    // log code 1637

    /** The Constant ESM_TIMER_T3480. */
    String ESM_TIMER_T3480 = "ESM_TIMER_T3480";

    /** The Constant ESM_TIMER_T3482. */
    String ESM_TIMER_T3482 = "ESM_TIMER_T3482";

    /** The Constant ESM_TIMER_T3481. */
    String ESM_TIMER_T3481 = "ESM_TIMER_T3481";

    /** The Constant ESM_TIMER_T3492. */
    String ESM_TIMER_T3492 = "ESM_TIMER_T3492";

    /** The Constant ACTIVATE_DEFAULT_EPS_BEARER_CONTEXT_REQUEST. */
    // log code 1968
    String ACTIVATE_DEFAULT_EPS_BEARER_CONTEXT_REQUEST = "ACTIVATE_DEFAULT_EPS_BEARER_CONTEXT_REQUEST";

    /** The Constant ACTIVATE_DEDICATED_EPS_BEARER_CONTEXT_REQUEST. */
    String ACTIVATE_DEDICATED_EPS_BEARER_CONTEXT_REQUEST = "ACTIVATE_DEDICATED_EPS_BEARER_CONTEXT_REQUEST";

    /** The Constant MODIFY_EPS_BEARER_CONTEXT_REQUEST. */
    String MODIFY_EPS_BEARER_CONTEXT_REQUEST = "MODIFY_EPS_BEARER_CONTEXT_REQUEST";

    /** The Constant DEACTIVATE_EPS_BEARER_CONTEXT_REQUEST. */
    String DEACTIVATE_EPS_BEARER_CONTEXT_REQUEST = "DEACTIVATE_EPS_BEARER_CONTEXT_REQUEST";

    /** The Constant PDN_CONNECTIVITY_REJECT. */
    String PDN_CONNECTIVITY_REJECT = "PDN_CONNECTIVITY_REJECT";

    /** The Constant PDN_DISCONNECT_REJECT. */
    String PDN_DISCONNECT_REJECT = "PDN_DISCONNECT_REJECT";

    /** The Constant BEARER_RESOURCE_ALLOCATION_REJECT. */
    String BEARER_RESOURCE_ALLOCATION_REJECT = "BEARER_RESOURCE_ALLOCATION_REJECT";

    /** The Constant BEARER_RESOURCE_MODIFICATION_REJECT. */
    String BEARER_RESOURCE_MODIFICATION_REJECT = "BEARER_RESOURCE_MODIFICATION_REJECT";

    /** The Constant ESM_INFORMATION_REQUEST. */
    String ESM_INFORMATION_REQUEST = "ESM_INFORMATION_REQUEST";

    /** The Constant ESM_NOTIFICATION. */
    String ESM_NOTIFICATION = "ESM_NOTIFICATION";

    /** The Constant ACTIVATE_DEFAULT_EPS_BEARER_CONTEXT_ACCEPT. */
    // log code 1969
    String ACTIVATE_DEFAULT_EPS_BEARER_CONTEXT_ACCEPT = "ACTIVATE_DEFAULT_EPS_BEARER_CONTEXT_ACCEPT";

    /** The Constant ACTIVATE_DEFAULT_EPS_BEARER_CONTEXT_REJECT. */
    String ACTIVATE_DEFAULT_EPS_BEARER_CONTEXT_REJECT = "ACTIVATE_DEFAULT_EPS_BEARER_CONTEXT_REJECT";

    /** The Constant ACTIVATE_DEDICATED_EPS_BEARER_CONTEXT_ACCEPT. */
    String ACTIVATE_DEDICATED_EPS_BEARER_CONTEXT_ACCEPT  = "ACTIVATE_DEDICATED_EPS_BEARER_CONTEXT_ACCEPT";

    /** The Constant ACTIVATE_DEDICATED_EPS_BEARER_CONTEXT_REJECT. */
    String ACTIVATE_DEDICATED_EPS_BEARER_CONTEXT_REJECT  = "ACTIVATE_DEDICATED_EPS_BEARER_CONTEXT_REJECT";

    /** The Constant MODIFY_EPS_BEARER_CONTEXT_ACCEPT. */
    String MODIFY_EPS_BEARER_CONTEXT_ACCEPT  = "MODIFY_EPS_BEARER_CONTEXT_ACCEPT";

    /** The Constant MODIFY_EPS_BEARER_CONTEXT_REJECT. */
    String MODIFY_EPS_BEARER_CONTEXT_REJECT  = "MODIFY_EPS_BEARER_CONTEXT_REJECT";

    /** The Constant DEACTIVATE_EPS_BEARER_CONTEXT_ACCEPT. */
    String DEACTIVATE_EPS_BEARER_CONTEXT_ACCEPT  = "DEACTIVATE_EPS_BEARER_CONTEXT_ACCEPT";

    /** The Constant PDN_CONNECTIVITY_REQUEST. */
    String PDN_CONNECTIVITY_REQUEST  = "PDN_CONNECTIVITY_REQUEST";

    /** The Constant PDN_DISCONNECT_REQUEST. */
    String PDN_DISCONNECT_REQUEST  = "PDN_DISCONNECT_REQUEST";

    /** The Constant BEARER_RESOURCE_ALLOCATION_REQUEST. */
    String BEARER_RESOURCE_ALLOCATION_REQUEST  = "BEARER_RESOURCE_ALLOCATION_REQUEST";

    /** The Constant BEARER_RESOURCE_MODIFICATION_REQUEST. */
    String BEARER_RESOURCE_MODIFICATION_REQUEST  = "BEARER_RESOURCE_MODIFICATION_REQUEST";

    /** The Constant ESM_INFORMATION_RESPONSE. */
    String ESM_INFORMATION_RESPONSE  = "ESM_INFORMATION_RESPONSE";

    /** The Constant ESM_STATUS. */
    String ESM_STATUS  = "ESM_STATUS";


    /** The failed cell barred. */
    //1618
    String FAILED_CELL_BARRED="failed cell barred";

    /** The failed freq barred. */
    String FAILED_FREQ_BARRED="failed freq barred";

    /** The success. */
    //1808
    String SUCCESS="success";

    /** The failed. */
    String FAILED= "failed";

    /** The aborted service request. */
    String ABORTED_SERVICE_REQUEST="aborted service request";

    /** The aborted mannual plmn search request. */
    String ABORTED_MANNUAL_PLMN_SEARCH_REQUEST="aborted mannual plmn search request";

    /** The aborted deactivate. */
    String ABORTED_DEACTIVATE ="aborted deactivate";

    /** The ul invalid sps. */
    //2040
    String UL_INVALID_SPS ="ul invalid sps";

    /** The ul sps activation. */
    String UL_SPS_ACTIVATION="ul sps activation";

    /** The ul sps release rrc. */
    String UL_SPS_RELEASE_RRC ="ul sps release by rrc";

    /** The ul sps retx. */
    String UL_SPS_RETX= "ul sps retx";

    /** The ul sps implicit release. */
    String UL_SPS_IMPLICIT_RELEASE ="ul sps implicit release";

    /** The ul sps release ho. */
    String UL_SPS_RELEASE_HO="ul sps release ho";

    /** The dl invalid sps event. */
    //2041
    String DL_INVALID_SPS_EVENT ="dl invalid sps event";

    /** The dl sps release rrc. */
    String DL_SPS_RELEASE_RRC="dl sps release rrc";

    /** The dl sps activation. */
    String DL_SPS_ACTIVATION="dl sps activation";

    /** The dl sps config assignment. */
    String DL_SPS_CONFIG_ASSIGNMENT="dl sps config assignment";

    /** The dl sps retransmission. */
    String DL_SPS_RETRANSMISSION="dl sps retransmission";

    /** The dl sps implicit release. */
    String DL_SPS_IMPLICIT_RELEASE="dl sps implicit release";

    /** The dl sps release ho. */
    String DL_SPS_RELEASE_HO="dl sps release ho";


    //1605

    /** The rrc tmri sib event1 wt. */
    String RRC_TMRI_SIB_EVENT1_WT= "rrc tmri sib event1 wt";

    /** The rrc tmri sib event2 wt. */
    String RRC_TMRI_SIB_EVENT2_WT="rrc tmri sib event2 wt";

    /** The rrc tmri sib 3hr clock tick. */
    String RRC_TMRI_SIB_3HR_CLOCK_TICK="rrc tmri sib 3hr clock tick";

    /** The rrc tmri t300. */
    String RRC_TMRI_T300="rrc tmri t300";

    /** The rrc tmri t301. */
    String RRC_TMRI_T301="rrc tmri t301";

    /** The rrc tmri t302. */
    String RRC_TMRI_T302="rrc tmri t302";

    /** The rrc tmri t303. */
    String RRC_TMRI_T303="rrc tmri t303";

    /** The rrc tmri t305. */
    String RRC_TMRI_T305="rrc tmri t305";

    /** The rrc tmri sib2 wait. */
    String RRC_TMRI_SIB2_WAIT ="rrc tmri sib2 wait";

    /** The rrc tmri uoos. */
    String RRC_TMRI_UOOS="rrc tmri uoos";

    /** The rrc tmri cell resel started. */
    String RRC_TMRI_CELL_RESEL_STARTED="rrc tmri cell resel started";

    /** The rrc tmri t304. */
    String RRC_TMRI_T304 ="rrc tmri t304";

    /** The rrc tmri conn rel. */
    String RRC_TMRI_CONN_REL="rrc tmri conn rel";

    /** The rrc tmri deadlock. */
    String RRC_TMRI_DEADLOCK="rrc tmri deadlock";

    /** The rrc tmri cep resel. */
    String RRC_TMRI_CEP_RESEL="rrc tmri cep resel";

    /** The rrc tmri t311. */
    String RRC_TMRI_T311="rrc tmri t311";

    /** The rrc tmri smc fail. */
    String RRC_TMRI_SMC_FAIL="rrc tmri smc fail";

    /** The rrc tmri t320. */
    String RRC_TMRI_T320="rrc tmri t320";

    /** The rrc tmri plmn search guard. */
    String RRC_TMRI_PLMN_SEARCH_GUARD="rrc tmri plmn search guard";

    /** The rrc tmri irat redir list. */
    String RRC_TMRI_IRAT_REDIR_LIST="rrc tmri irat redir list";

    /** The rrc tmri irat redir wait. */
    String RRC_TMRI_IRAT_REDIR_WAIT="rrc tmri irat redir wait";

    /** The rrc tmri from g redir. */
    String RRC_TMRI_FROM_G_REDIR="rrc tmri from g redir";

    /** The rrc tmri irat w resel wait. */
    String RRC_TMRI_IRAT_W_RESEL_WAIT="rrc tmri irat w resel wait";

    /** The rrc tmri periodic sib8. */
    String RRC_TMRI_PERIODIC_SIB8="rrc tmri periodic sib8";

    /** The rrc tmri t321. */
    String RRC_TMRI_T321="rrc tmri t321";

    /** The rrc tmri csg asf. */
    String RRC_TMRI_CSG_ASF="rrc tmri csg asf";

    /** The started. */
    String STARTED="started";

    /** The stopped. */
    String STOPPED="stopped";

    /** The inactive. */
    //1606
    String INACTIVE ="inactive";

    /** The idle not camped. */
    String IDLE_NOT_CAMPED="idle not camped";

    /** The idle camped. */
    String IDLE_CAMPED="idle camped";

    /** The connecting. */
    String CONNECTING="connecting";

    /** The connected. */
    String CONNECTED="connected";

    /** The suspended. */
    String SUSPENDED="suspended";

    /** The irat to lte started. */
    String IRAT_TO_LTE_STARTED="irat to lte started";

    /** The closing. */
    String CLOSING="closing";

    /** The inactive trigger other. */
    //1994
    String INACTIVE_TRIGGER_OTHER="inactive trigger other ";

    /** The camped trigger conn est failure conn aborted. */
    String CAMPED_TRIGGER_CONN_EST_FAILURE_CONN_ABORTED="camped trigger conn est failure conn aborted";

    /** The camped trigger conn est failure t300 expiry. */
    String CAMPED_TRIGGER_CONN_EST_FAILURE_T300_EXPIRY="camped trigger conn est failure t300 expiry";

    /** The camped trigger conn est failure conn reject. */
    String CAMPED_TRIGGER_CONN_EST_FAILURE_CONN_REJECT="camped trigger conn est failure conn reject";

    /** The camped trigger conn est failure cell resel. */
    String CAMPED_TRIGGER_CONN_EST_FAILURE_CELL_RESEL="camped trigger conn est failure cell resel";

    /** The camped trigger conn est failure access barred. */
    String CAMPED_TRIGGER_CONN_EST_FAILURE_ACCESS_BARRED="camped trigger conn est failure access barred";

    /** The camped trigger conn est failure other. */
    String CAMPED_TRIGGER_CONN_EST_FAILURE_OTHER="camped trigger conn est failure other";

    /** The camped trigger other. */
    String CAMPED_TRIGGER_OTHER="camped trigger other ";

    /** The connecting trigger emergency. */
    String  CONNECTING_TRIGGER_EMERGENCY="connecting trigger emergency";

    /** The connecting trigger high pri access. */
    String CONNECTING_TRIGGER_HIGH_PRI_ACCESS="connecting trigger high pri access";

    /** The connecting trigger mt access. */
    String  CONNECTING_TRIGGER_MT_ACCESS="connecting trigger mt access ";

    /** The connecting trigger mo signaling. */
    String CONNECTING_TRIGGER_MO_SIGNALING="connecting trigger mo signaling ";

    /** The connecting trigger mo data. */
    String CONNECTING_TRIGGER_MO_DATA="connecting trigger mo data ";

    /** The connected trigger reest recfg failure. */
    String CONNECTED_TRIGGER_REEST_RECFG_FAILURE="connected trigger reest recfg failure ";

    /** The connected trigger reest ho failure. */
    String CONNECTED_TRIGGER_REEST_HO_FAILURE="connected trigger reest ho failure";

    /** The connected trigger reest other failure. */
    String CONNECTED_TRIGGER_REEST_OTHER_FAILURE="connected trigger reest other failure";

    /** The closing trigger t311 expiry. */
    String CLOSING_TRIGGER_T311_EXPIRY="closing trigger t311 expiry";

    /** The closing trigger t301 expiry. */
    String  CLOSING_TRIGGER_T301_EXPIRY="closing trigger t301 expiry";

    /** The closing trigger reest reject. */
    String CLOSING_TRIGGER_REEST_REJECT="closing trigger reest reject";

    /** The closing trigger load balance. */
    String CLOSING_TRIGGER_LOAD_BALANCE="closing trigger load balance ";

    /** The closing trigger other. */
    String  CLOSING_TRIGGER_OTHER="closing trigger other ";

    /** The closing trigger conn abort. */
    String CLOSING_TRIGGER_CONN_ABORT="closing trigger conn abort";

    /** The closing trigger conn abort irat success. */
    String  CLOSING_TRIGGER_CONN_ABORT_IRAT_SUCCESS="closing trigger conn abort irat success ";

    /** The trigger max. */
    String  TRIGGER_MAX="trigger max";


    /** The mib. */
    //1619
    String MIB="mib";

    /** The sib1. */
    String SIB1="sib1";


    /** The attach accept. */
    //1966
    String ATTACH_ACCEPT="attach accept";

    /** The attach reject. */
    String ATTACH_REJECT="attach reject";

    /** The detach request. */
    String DETACH_REQUEST="detach request";

    /** The detach accept. */
    String DETACH_ACCEPT="detach accept";

    /** The tracking area update accept. */
    String TRACKING_AREA_UPDATE_ACCEPT="tracking area update accept";

    /** The tracking area reject. */
    String TRACKING_AREA_REJECT="tracking area reject";

    /** The service reject. */
    String SERVICE_REJECT="service reject";

    /** The guti reallocation command. */
    String GUTI_REALLOCATION_COMMAND="guti reallocation command";

    /** The emm authentication request. */
    String EMM_AUTHENTICATION_REQUEST="emm authentication request";

    /** The emm authentication reject. */
    String EMM_AUTHENTICATION_REJECT="emm authentication reject";

    /** The emm identity request. */
    String EMM_IDENTITY_REQUEST="emm identify request";

    /** The security mode command. */
    String SECURITY_MODE_COMMAND="security mode command ";

    /** The emm information. */
    String EMM_INFORMATION="emm information";

    /** The downlink nas transport. */
    String DOWNLINK_NAS_TRANSPORT="downlink nas transport";

    /** The cs serv notification. */
    String CS_SERV_NOTIFICATION="cs serv notification";

    /** The dl generic nas transport. */
    String DL_GENERIC_NAS_TRANSPORT="dl generic nas transport";


    /** The attach request. */
    //1967
    String ATTACH_REQUEST="attach request";

    /** The attach complete. */
    String ATTACH_COMPLETE="attach complete";

    /** The tracking area update request. */
    String TRACKING_AREA_UPDATE_REQUEST="tracking area update request";

    /** The tracking area update complete. */
    String TRACKING_AREA_UPDATE_COMPLETE="tracking area update complete";

    /** The ext service request. */
    String EXT_SERVICE_REQUEST="ext service request";

    /** The guti reallocation complete. */
    String GUTI_REALLOCATION_COMPLETE="guti reallocation complete";

    /** The emm authentication response. */
    String EMM_AUTHENTICATION_RESPONSE="emm authentication response";

    /** The emm authentication failure. */
    String EMM_AUTHENTICATION_FAILURE="emm authentication failure";

    /** The emm identity response. */
    String EMM_IDENTITY_RESPONSE= "emm indentity response";

    /** The security mode complete. */
    String SECURITY_MODE_COMPLETE="security mode complete";

    /** The security mode reject. */
    String SECURITY_MODE_REJECT="security mode reject";

    /** The emm status. */
    String EMM_STATUS="emm status";

    /** The uplink nas transport. */
    String UPLINK_NAS_TRANSPORT="uplink nas transport";

    /** The ul generic nas transport. */
    String UL_GENERIC_NAS_TRANSPORT="ul generic nas transport";

    /** The mmr plmn search req. */
    //1633
    String MMR_PLMN_SEARCH_REQ="mmr plmn search req";

    /** The mmr reg req. */
    String MMR_REG_REQ="mmr reg req";

    /** The mmr stop mode req. */
    String MMR_STOP_MODE_REQ="mmr stop mode req";

    /** The mmr plmn search abort req. */
    String MMR_PLMN_SEARCH_ABORT_REQ="mmr plmn search abort req";

    /** The mmr ph status change req. */
    String MMR_PH_STATUS_CHANGE_REQ="mmr ph status change req";

    /** The mmr plmn search cnf. */
    //1634
    String MMR_PLMN_SEARCH_CNF="mmr plmn search cnf";

    /** The mmr reg cnf. */
    String MMR_REG_CNF="mmr reg cnf";

    /** The mmr service ind. */
    String MMR_SERVICE_IND="mmr service ind";

    /** The mmr camped ind. */
    String MMR_CAMPED_IND="mmr camped ind";

    /** The mmr attach failed ind. */
    String MMR_ATTACH_FAILED_IND="mmr attach failed ind";

    /** The nas emm detach cmd. */
    //1629
    String NAS_EMM_DETACH_CMD="nas emm detach cmd";

    /** The nas emm eps bearer status cmd. */
    String NAS_EMM_EPS_BEARER_STATUS_CMD="nas cmm eps bearer status cmd";

    /** The nas emm emc srv status cmd. */
    String NAS_EMM_EMC_SRV_STATUS_CMD="nas emm emc srv status cmd";

    /** The nas emm ps call status cmd. */
    String NAS_EMM_PS_CALL_STATUS_CMD="nas emm ps call status cmd";

    /** The nas emm service req. */
    String NAS_EMM_SERVICE_REQ="nas emm service req";

    /** The nas emm data req. */
    String NAS_EMM_DATA_REQ="nas emm data req";

    /** The nas esm 1xcsfb esr call rsp. */
    //1630
    String NAS_ESM_1XCSFB_ESR_CALL_RSP="nas esm 1xcsfb esr call rsp";

    /** The nas esm 1xcsfb esr call abort rsp. */
    String NAS_ESM_1XCSFB_ESR_CALL_ABORT_RSP="nas esm 1xcsfb esr call abort rsp";

    /** The nas esm failure ind. */
    String NAS_ESM_FAILURE_IND="nas esm failure ind";

    /** The nas esm sig connection released ind. */
    String NAS_ESM_SIG_CONNECTION_RELEASED_IND="nas esm sig connection released ind";

    /** The nas esm active eps ind. */
    String NAS_ESM_ACTIVE_EPS_IND="nas esm active eps ind";

    /** The nas esm detach ind. */
    String NAS_ESM_DETACH_IND="nas esm detach ind";

    /** The nas esm eps bearer status ind. */
    String NAS_ESM_EPS_BEARER_STATUS_IND="nas esm eps bearer status ind";

    /** The nas esm get pdn connectivity req ind. */
    String NAS_ESM_GET_PDN_CONNECTIVITY_REQ_IND="nas esm get pdn connectivity req ind";

    /** The nas esm get isr status ind. */
    String NAS_ESM_GET_ISR_STATUS_IND="nas esm get isr status ind";

    /** The nas esm isr status change ind. */
    String NAS_ESM_ISR_STATUS_CHANGE_IND="nas esm isr status change ind";

    /** The nas esm unblock all apns ind. */
    String NAS_ESM_UNBLOCK_ALL_APNS_IND="nas esm unblock all apns ind";

    /** The timer t3346. */
    //1631  & 1632 & 2620
    String TIMER_T3346="timer t3346";

    /** The emm access barring emergency timer. */
    String EMM_ACCESS_BARRING_EMERGENCY_TIMER="emm access barring emergency timer ";

    /** The emm access barring high priority timer. */
    String EMM_ACCESS_BARRING_HIGH_PRIORITY_TIMER="emm access barring high priority timer";

    /** The emm access barring mt access timer. */
    String EMM_ACCESS_BARRING_MT_ACCESS_TIMER="emm access barring mt access timer";

    /** The emm access barring mo signaling timer. */
    String EMM_ACCESS_BARRING_MO_SIGNALING_TIMER="emm access barring mo signaling timer";

    /** The emm access barring mo data timer. */
    String EMM_ACCESS_BARRING_MO_DATA_TIMER="emm access barring mo data timer";

    /** The timer t3410. */
    String TIMER_T3410="timer t3410";

    /** The timer t3417. */
    String TIMER_T3417="timer t3417";

    /** The timer t3421. */
    String TIMER_T3421="timer t3421";

    /** The timer t3418. */
    String TIMER_T3418="timer t3418";

    /** The timer t3420. */
    String TIMER_T3420="timer t3420";

    /** The timer t3416. */
    String TIMER_T3416="timer t3416";

    /** The timer t3411. */
    String TIMER_T3411="timer t3411";

    /** The timer t3402. */
    String TIMER_T3402="timer t3402";

    /** The timer t3412. */
    String TIMER_T3412="timer t3412";

    /** The timer t3430. */
    String TIMER_T3430="timer t3430";

    /** The timer t3440. */
    String TIMER_T3440="timer t3440";

    /** The timer t3423. */
    String TIMER_T3423="timer t3423";

    /** The timer t3417 ext. */
    String TIMER_T3417_EXT="timer t3417 ext";

    /** The emm poweroff detach timer. */
    String EMM_POWEROFF_DETACH_TIMER="emm poweroff detach timer";

    /** The emm access barring timer. */
    String EMM_ACCESS_BARRING_TIMER="emm access barring timer";

    /** The timer emm ps detach. */
    String TIMER_EMM_PS_DETACH="timer emm ps detach";

    /** The emm forbidden tai clear timer. */
    String EMM_FORBIDDEN_TAI_CLEAR_TIMER="emm forbidden tai clear timer";

    /** The timer t3442. */
    String TIMER_T3442="timer t3442";

    /** The timer emm radio retry. */
    String TIMER_EMM_RADIO_RETRY="timer emm radio retry";

    /** The timer srlte esr. */
    String TIMER_SRLTE_ESR="timer srlte esr";

    /** The timer delay tau. */
    String TIMER_DELAY_TAU="timer delay tau";


    /** The target rat geran. */
    //1970
    String TARGET_RAT_GERAN="target rat is geran";

    /** The target rat utran. */
    String TARGET_RAT_UTRAN="target rat is utran";

    /** The target rat eutran. */
    String TARGET_RAT_EUTRAN="target rat is eutran";

    /** The target rat tds. */
    String TARGET_RAT_TDS="target rat is tds";

    /** The eutran current rat. */
    //1972
    String EUTRAN_CURRENT_RAT="eutran is the current rat";

    /** The geran. */
    //1973
    String GERAN="geran";

    /** The utran. */
    String UTRAN="utran";

    /** The eutran. */
    String EUTRAN="eutran";


    /** The meas obj eutra. */
    //2157
    String MEAS_OBJ_EUTRA="meas obj eutra";

    /** The meas obj utra. */
    String MEAS_OBJ_UTRA="meas obj utra";

    /** The meas obj geran. */
    String MEAS_OBJ_GERAN="meas obj geran";

    /** The meas obj cdma. */
    String MEAS_OBJ_CDMA="meas obj cdma";

    /** The meas obj max. */
    String MEAS_OBJ_MAX="meas obj max";

    /** The meas obj mode fdd. */
    //2481
    String MEAS_OBJ_MODE_FDD="meas obj mode fdd";

    /** The meas obj mode tdd. */
    String MEAS_OBJ_MODE_TDD="meas obj mode tdd";

    /** The meas obj mode not applicable. */
    String MEAS_OBJ_MODE_NOT_APPLICABLE="meas obj mode not applicable";

    /** The meas obj mode max. */
    String MEAS_OBJ_MODE_MAX="meas obj mode max";


    /** The emp warn reason oos unicast. */
    //2227
    String EMP_WARN_REASON_OOS_UNICAST="emp warn reason oos unicast";

    /** The emp warn reason oos multicast. */
    String EMP_WARN_REASON_OOS_MULTICAST="emp warn reason oos mulitcast";

    /** The emp warn reason oos recovered. */
    String EMP_WARN_REASON_OOS_RECOVERED="emp warn reason oos recovered";

    /** The emp warn reason none. */
    String EMP_WARN_REASON_NONE="emp warn reason none";


    /** The connection request. */
    //1501
    String CONNECTION_REQUEST="connection request";

    /** The radio link failure. */
    String RADIO_LINK_FAILURE="radio link failure";

    /** The ul data arrival. */
    String UL_DATA_ARRIVAL="ul data arrival";

    /** The dl data arrival. */
    String DL_DATA_ARRIVAL="dl data arrival";

    /** The handover. */
    String HANDOVER="handover";

    /** The contention free. */
    String CONTENTION_FREE="contention free";

    /** The contention based. */
    String CONTENTION_BASED="contention based";

    /** The raid not matched. */
    //1502
    String RAID_NOT_MATCHED="raid not matched";

    /** The raid matched. */
    String RAID_MATCHED="raid matched";

    /** The rach was aborted. */
    //1503
    String RACH_WAS_ABORTED="rach was aborted";

    /** The rach was successful. */
    String RACH_WAS_SUCCESSFUL="rach was successful";

    /** The rach has problem. */
    String RACH_HAS_PROBLEM="rach has problem";

    /** The rach is failed. */
    String RACH_IS_FAILED="rach is failed";

    /** The none. */
    //1718
    String NONE="none";

    /** The connection release. */
    String CONNECTION_RELEASE="connection release";

    /** The ho. */
    String HO="ho";

    /** The rlf. */
    String RLF="rlf";

    /** The connection canellation. */
    String CONNECTION_CANELLATION="connection cancellation";

    /** The periodic bsr timer expired. */
    //1719
    String PERIODIC_BSR_TIMER_EXPIRED="Periodic BSR timer is expired";

    /** The higher priority data arrival. */
    String HIGHER_PRIORITY_DATA_ARRIVAL="Higher priority data arrival";

    /** The retx bsr timer expired. */
    String RETX_BSR_TIMER_EXPIRED="ReTx BSR timer is expired";

    /** The request to include bsrr report. */
    String REQUEST_TO_INCLUDE_BSRR_REPORT="Request to include BSR report";

    /** The request to send sr. */
    String REQUEST_TO_SEND_SR="request to send sr";

    /** The ta timer. */
    //1720
    String TA_TIMER="ta timer";

    /** The ra timer. */
    String RA_TIMER="ra timer";

    /** The contention timer. */
    String CONTENTION_TIMER ="contention timer";

    /** The rach backoff timer. */
    String RACH_BACKOFF_TIMER="rach bakoff timer";

    /** The start. */
    String START="start";

    /** The stop. */
    String STOP="stop";

    /** The expired. */
    String EXPIRED="expired";

    /** The infinity. */
    //B060
    String INFINITY="infinity";

    /** The radiolinkfailure. */
    //B061
    String RADIOLINKFAILURE ="Radio Link Failure";

    /** The uldataarrival. */
    String ULDATAARRIVAL ="UL Data Arrival";

    /** The dldataarrival. */
    String DLDATAARRIVAL ="DL Data Arrival";

    /** The contentionfreerachprocedure. */
    String CONTENTIONFREERACHPROCEDURE="Contention-free RACH procedure";

    /** The contentionbasedrachprocedure. */
    String CONTENTIONBASEDRACHPROCEDURE="Contention-based RACH procedure";

    /** The groupa. */
    String GROUPA ="Group A";

    /** The groupb. */
    String GROUPB="Group B";

    /** The failure at msg2. */
    //B062
    String FAILURE_AT_MSG2="Failure at MSG2";

    /** The failure at msg4 due to ct timer expired. */
    String FAILURE_AT_MSG4_DUE_TO_CT_TIMER_EXPIRED="Failure at MSG4 due to CT timer expired";

    /** The failure at msg4 due to ct resolution is not passed. */
    String FAILURE_AT_MSG4_DUE_TO_CT_RESOLUTION_IS_NOT_PASSED="Failure at MSG4 due to CT resolution is not passed";

    /** The contention free rach procedure. */
    String CONTENTION_FREE_RACH_PROCEDURE="Contention Free RACH procedure";

    /** The contention based rach procedure. */
    String CONTENTION_BASED_RACH_PROCEDURE="Contention Based RACH procedure";

    /** The msg 1. */
    String MSG_1 = "msg 1";

    /** The msg 2. */
    String MSG_2= "msg 2";

    /** The msg 3. */
    String MSG_3 ="msg 3";


    /** The network to ue. */
    //156E
    String NETWORK_TO_UE="Network to UE";

    /** The ue to network. */
    String UE_TO_NETWORK="UE to network";

    /** The ims sip register. */
    String IMS_SIP_REGISTER="ims sip register";

    /** The ims sip invite. */
    String IMS_SIP_INVITE="ims sip invite";

    /** The ims sip prack. */
    String IMS_SIP_PRACK="ims sip prack";

    /** The ims sip cancel. */
    String IMS_SIP_CANCEL="ims sip cancel";

    /** The ims sip ack. */
    String IMS_SIP_ACK="ims sip ack";

    /** The ims sip bye. */
    String IMS_SIP_BYE="ims sip bye";

    /** The ims sip subscribe. */
    String IMS_SIP_SUBSCRIBE="ims sip subscribe";

    /** The ims sip notify. */
    String IMS_SIP_NOTIFY="ims sip notify";

    /** The ims sip update. */
    String IMS_SIP_UPDATE="ims sip update";

    /** The ims sip refer. */
    String IMS_SIP_REFER="ims sip refer";

    /** The ims sip message. */
    String IMS_SIP_MESSAGE="ims sip message";

    /** The ims sip info. */
    String IMS_SIP_INFO="ims sip info";

    /** The ims sip publish. */
    String IMS_SIP_PUBLISH="ims sip publish";

    /** The ims sip options. */
    String IMS_SIP_OPTIONS="ims sip option";

    /** The request. */
    String REQUEST="request";

    /** The trying 100. */
    String TRYING_100="100 Trying";

    /** The ringing 180. */
    String RINGING_180="180 Ringing";

    /** The call is being forwarded 181. */
    String CALL_IS_BEING_FORWARDED_181="181 Call Is Being Forwarded";

    /** The queued 182. */
    String QUEUED_182="182 Queued";

    /** The session progress 183. */
    String SESSION_PROGRESS_183="183 Session Progress";

    /** The ok 200. */
    String OK_200="200 OK";

    /** The accepted 202. */
    String ACCEPTED_202="202 accepted";

   
    /** The multiple choices 300. */
    String MULTIPLE_CHOICES_300="300 Multiple Choices";

    /** The moverd permanently 301. */
    String MOVERD_PERMANENTLY_301="301 Moved Permanently";

    /** The moved temporarily 302. */
    String MOVED_TEMPORARILY_302="302 Moved Temporarily";

    /** The use proxy 305. */
    String USE_PROXY_305="305 Use Proxy";

    /** The alternative service 380. */
    String ALTERNATIVE_SERVICE_380="380 Alternative Service";

    /** The bad request 400. */
    String BAD_REQUEST_400="400 Bad Request";

    /** The unauthorized 401. */
    String UNAUTHORIZED_401="401 Unauthorized";

    /** The payment required 402. */
    String PAYMENT_REQUIRED_402="402 Payment Required";

    /** The forbidden 403. */
    String FORBIDDEN_403="403 Forbidden";

    /** The not found 404. */
    String NOT_FOUND_404="404 Not Found";

    /** The method not allowed 405. */
    String METHOD_NOT_ALLOWED_405="405 Method Not Allowed";

    /** The not acceptable 406. */
    String NOT_ACCEPTABLE_406="406 Not Acceptable";

    /** The proxy authentication required 407. */
    String PROXY_AUTHENTICATION_REQUIRED_407="407 Proxy Authentication Required";

    /** The request timeout 408. */
    String REQUEST_TIMEOUT_408="408 Request Timeout";

    /** The conflict 409. */
    String CONFLICT_409="409 Conflict";

    /** The server inernal error 500. */
    String SERVER_INERNAL_ERROR_500="500 Server Internal Error";

    /** The not implemneted 501. */
    String NOT_IMPLEMNETED_501="501 Not Implemented";

    /** The bad gateway 502. */
    String BAD_GATEWAY_502="502 Bad Gateway";

    /** The service unavailable 503. */
    String SERVICE_UNAVAILABLE_503="503 Service Unavailable";

    /** The server timeout 504. */
    String SERVER_TIMEOUT_504="504 Server Timeout";

    /** The version not supported 505. */
    String VERSION_NOT_SUPPORTED_505="505 Version Not Supported";

    /** The message too large 513. */
    String MESSAGE_TOO_LARGE_513="513 Message Too Large";

    /** The precondition failure 580. */
    String PRECONDITION_FAILURE_580="580 Precondition Failure";

    /** The busy evetywhere 600. */
    String BUSY_EVETYWHERE_600="600 Busy Everywhere";

    /** The decline 603. */
    String DECLINE_603="603 Decline";

    /** The does not exist anywhere 604. */
    String DOES_NOT_EXIST_ANYWHERE_604="604 Does Not Exist Anywhere";

    /** The not acceptable 606. */
    String NOT_ACCEPTABLE_606="606 Not Acceptable";

    /** The initial. */
    //1832
    String INITIAL="Initial";

    /** The reregistration. */
    String REREGISTRATION="Reregistration";

    /** The deregistration. */
    String DEREGISTRATION="Deregistration";

    /** The emergency registration. */
    String EMERGENCY_REGISTRATION="Emergency registration";

    /** The registration due to reauthorization. */
    String REGISTRATION_DUE_TO_REAUTHORIZATION="Registration due to reauthorizatio";

    /** The mo. */
    //1831
    String MO="mo";

    /** The mt. */
    String MT="mt";

    /** The voice. */
    String VOICE="voice";


    /** The mo initiated. */
    String MO_INITIATED="mo initiated";

    /** The mt initiated. */
    String MT_INITIATED="mt initiated";

    /** The rtp inactivity. */
    String RTP_INACTIVITY="rtp inactivity";

    /** The rtcp inactivity. */
    String RTCP_INACTIVITY="rtcp inactivity";

    /** The srvcc. */
    String SRVCC="srvcc";

    /** The rx. */
    //2141
    String RX="rx";

    /** The tx. */
    String TX="tx";

    /** The dcm rat none. */
    String DCM_RAT_NONE="dcm rat none";

    /** The dcm rat gprs. */
    String DCM_RAT_GPRS="dcm rat gprs";

    /** The dcm rat edge. */
    String DCM_RAT_EDGE="dcm rat edge";

    /** The dcm rat wcdma. */
    String DCM_RAT_WCDMA="dcm rat wcdma";

    /** The dcm rat wlan. */
    String DCM_RAT_WLAN="dcm rat wlan";

    /** The dcm rat cdma. */
    String DCM_RAT_CDMA="dcm rat cdma";

    /** The dcm rat iwlan. */
    String DCM_RAT_IWLAN="dcm rat iwlan";

    /** The dcm rat dor0. */
    String  DCM_RAT_DOR0="dcm rat dor0";

    /** The dcm rat dora. */
    String DCM_RAT_DORA="dcm rat dora";

    /** The dcm rat ehrpd. */
    String DCM_RAT_EHRPD="dcm rat ehrpd";

    /** The dcm rat lte. */
    String DCM_RAT_LTE="dcm rat lte";

    /** The ims reg type na. */
    String IMS_REG_TYPE_NA="ims reg type na";

    /** The ims reg type sms. */
    String IMS_REG_TYPE_SMS="ims reg type sms";

    /** The ims reg type voip. */
    String IMS_REG_TYPE_VOIP="ims reg type voip";

    /** The ims reg type icsi. */
    String IMS_REG_TYPE_ICSI="ims reg type icsi";

    /** The sip session incoming message ev. */
    //2158
    String SIP_SESSION_INCOMING_MESSAGE_EV="sip session incoming message ev";

    /** The sip session outgoing message ev. */
    String SIP_SESSION_OUTGOING_MESSAGE_EV="sip session outgoing message ev";

    /** The sip session outgoing message ok ev. */
    String SIP_SESSION_OUTGOING_MESSAGE_OK_EV="sip session outgoing message ok ev";

    /** The sip session incoming message ok ev. */
    String SIP_SESSION_INCOMING_MESSAGE_OK_EV="sip session incoming message ok ev";

    /** The sip session outgoing ev. */
    String SIP_SESSION_OUTGOING_EV="sip session outgoing ev";

    /** The sip session incoming ev. */
    String SIP_SESSION_INCOMING_EV="sip session incoming ev";

    /** The sip session media param ev. */
    String SIP_SESSION_MEDIA_PARAM_EV="sip session media param ev";

    /** The sip outgoing session progress ev. */
    String SIP_OUTGOING_SESSION_PROGRESS_EV="sip outgoing session progress ev";

    /** The sip session ringing ev. */
    String SIP_SESSION_RINGING_EV="sip session ringing ev";

    /** The sip session incoming cancel ev. */
    String SIP_SESSION_INCOMING_CANCEL_EV="sip session incoming cancel ev";

    /** The sip session outgoing cancel ev. */
    String SIP_SESSION_OUTGOING_CANCEL_EV="sip session outgoing cancel ev";

    /** The sip session incoming established ev. */
    String SIP_SESSION_INCOMING_ESTABLISHED_EV="sip session incoming established ev";

    /** The sip session outgoing established ev. */
    String SIP_SESSION_OUTGOING_ESTABLISHED_EV="sip session outgoing established ev";

    /** The sip session incoming terminated ev. */
    String SIP_SESSION_INCOMING_TERMINATED_EV="sip session incoming terminated ev";

    /** The sip session outgoing terminated ok ev. */
    String SIP_SESSION_OUTGOING_TERMINATED_OK_EV="sip session outgoing terminated ok ev";

    /** The sip session incoming terminated ok ev. */
    String SIP_SESSION_INCOMING_TERMINATED_OK_EV="sip session incoming terminated ok ev";

    /** The sip incoming reinvite ev. */
    String SIP_INCOMING_REINVITE_EV="sip incoming reinvite ev";

    /** The sip outgoing reinvite ev. */
    String SIP_OUTGOING_REINVITE_EV="sip outgoing reinvite ev";

    /** The sip reinvite established ev. */
    String SIP_REINVITE_ESTABLISHED_EV="sip reinvite established ev";

    /** The sip ack reinvite established ev. */
    String  SIP_ACK_REINVITE_ESTABLISHED_EV="sip ack reinvite established ev";

    /** The sip ack reinvite failure ev. */
    String SIP_ACK_REINVITE_FAILURE_EV="sip ack reinvite failure ev";

    /** The sip session failure ev. */
    String SIP_SESSION_FAILURE_EV="sip session failure ev";

    /** The sip session request failure ev. */
    String SIP_SESSION_REQUEST_FAILURE_EV="sip session request failure ev";

    /** The sip reinvte failure ev. */
    String SIP_REINVTE_FAILURE_EV="sip reinvte failure ev";

    /** The sip session outgoing update ev. */
    String SIP_SESSION_OUTGOING_UPDATE_EV="sip session outgoing update ev";

    /** The sip session incoming update ev. */
    String SIP_SESSION_INCOMING_UPDATE_EV="sip session incoming update ev";

    /** The sip session incoming info ev. */
    String SIP_SESSION_INCOMING_INFO_EV="sip session incoming info ev";

    /** The sip session outgoing info ev. */
    String SIP_SESSION_OUTGOING_INFO_EV="sip session outgoing info ev";

    /** The sip session incoming info ok ev. */
    String SIP_SESSION_INCOMING_INFO_OK_EV="sip session incoming info ok ev";

    /** The sip incoming prack ev. */
    String SIP_INCOMING_PRACK_EV="sip incoming prack ev";

    /** The sip session incoming refer ev. */
    String SIP_SESSION_INCOMING_REFER_EV="sip session incoming refer ev";

    /** The sip session outgoing refer ev. */
    String SIP_SESSION_OUTGOING_REFER_EV="sip session outgoing refer ev";

    /** The sip session outgoing refer ok ev. */
    String SIP_SESSION_OUTGOING_REFER_OK_EV="sip session outgoing refer ok ev";

    /** The sip session refer final ev. */
    String SIP_SESSION_REFER_FINAL_EV="sip session refer final ev";

    /** The sip session incoming refer ok ev. */
    String SIP_SESSION_INCOMING_REFER_OK_EV="sip session incoming refer ok ev";

    /** The sip prack success ev. */
    String SIP_PRACK_SUCCESS_EV="sip prack succes ev";

    /** The sip session outgoing info ok ev. */
    String SIP_SESSION_OUTGOING_INFO_OK_EV="sip session outgoing info ok ev";

    /** The sip session call being forwarded ev. */
    String SIP_SESSION_CALL_BEING_FORWARDED_EV="sip session call being forwarded ev";


    /** The call type normal. */
    String CALL_TYPE_NORMAL="call type normal";

    /** The call type hold. */
    String CALL_TYPE_HOLD="call type hold";

    /** The call type unhold. */
    String CALL_TYPE_UNHOLD="call type unhold";

    /** The call type waiting. */
    String CALL_TYPE_WAITING="call type waiting";

    /** The call type conference. */
    String CALL_TYPE_CONFERENCE="call type conference";

    /** The call type forward. */
    String CALL_TYPE_FORWARD="call type forward";

    /** The call type transfer. */
    String CALL_TYPE_TRANSFER="call type transfer";

    /** The cfg failure. */
    //1995
    String CFG_FAILURE="cfg failure";

    /** The ho failure. */
    String HO_FAILURE="ho failure";

    /** The rach problem. */
    String RACH_PROBLEM="rach problem";

    /** The max retrx. */
    String MAX_RETRX="max retrx";

    /** The ip check failure. */
    String IP_CHECK_FAILURE="ip check failure";

    /** The other failure. */
    String OTHER_FAILURE="other failure";

    /** The max. */
    String MAX="max";

    /** The mib change. */
    String MIB_CHANGE="mib change";

    /** The rlf rf unavailable. */
    String RLF_RF_UNAVAILABLE="rlf rf unavailable";


    /** The cm call event orig. */
    //12C1
    String CM_CALL_EVENT_ORIG = "cm call event orig";

    /** The cm call event answer. */
    String CM_CALL_EVENT_ANSWER = "cm call event answer";

    /** The cm call event end req. */
    String CM_CALL_EVENT_END_REQ="cm call event end req";

    /** The cm call event end. */
    String CM_CALL_EVENT_END="cm call event end";

    /** The cm call event sups. */
    String CM_CALL_EVENT_SUPS="cm call event sups";

    /** The cm call event connect. */
    String CM_CALL_EVENT_CONNECT="cm call event connect";

    /** The cm call event srv opt. */
    String CM_CALL_EVENT_SRV_OPT="cm call event srv opt";

    /** The cm call event privacy. */
    String CM_CALL_EVENT_PRIVACY="cm call event privacy";

    /** The cm call event privacy pref. */
    String CM_CALL_EVENT_PRIVACY_PREF="cm call event privacy pref";

    /** The cm call event caller id. */
    String CM_CALL_EVENT_CALLER_ID="cm call event caller id";

    /** The cm call event abrv alert. */
    String CM_CALL_EVENT_ABRV_ALERT="cm call event abrv alert";

    /** The cm call event abrv reorder. */
    String CM_CALL_EVENT_ABRV_REORDER="cm call event abrv reoder";

    /** The cm call event abrv intercept. */
    String CM_CALL_EVENT_ABRV_INTERCEPT="cm call event abrv inercept";

    /** The cm call event signal. */
    String CM_CALL_EVENT_SIGNAL="cm call event signal";

    /** The cm call event display. */
    String CM_CALL_EVENT_DISPLAY="cm call event display";

    /** The cm call event called party. */
    String CM_CALL_EVENT_CALLED_PARTY="cm call event called party";

    /** The cm call event connected num. */
    String CM_CALL_EVENT_CONNECTED_NUM="cm call event connected num";

    /** The cm call event info. */
    String CM_CALL_EVENT_INFO="cm call event info";

    /** The cm call event ext disp. */
    String CM_CALL_EVENT_EXT_DISP="cm call event ext disp";

    /** The cm call event ndss start. */
    String CM_CALL_EVENT_NDSS_START="cm call event ndss start";

    /** The cm call event ndss connect. */
    String CM_CALL_EVENT_NDSS_CONNECT="cm call event ndss connect";

    /** The cm call event ext brst intl. */
    String CM_CALL_EVENT_EXT_BRST_INTL="cm call event ext brst intl";

    /** The cm call event nss clir rec. */
    String CM_CALL_EVENT_NSS_CLIR_REC="cm call event nss clir rec";

    /** The cm call event nss rel rec. */
    String CM_CALL_EVENT_NSS_REL_REC="cm call event nss rel rec";

    /** The cm call event nss aud ctrl. */
    String CM_CALL_EVENT_NSS_AUD_CTRL="cm call event nss aud ctrl";

    /** The cm call event l2ack call hold. */
    String CM_CALL_EVENT_L2ACK_CALL_HOLD="cm call event l2ack call hold";

    /** The cm call event setup ind. */
    String CM_CALL_EVENT_SETUP_IND="cm call event setup ind";

    /** The cm call event setup res. */
    String CM_CALL_EVENT_SETUP_RES="cm call event setup res";

    /** The cm call event call conf. */
    String CM_CALL_EVENT_CALL_CONF="cm call event call conf";

    /** The cm call event pdp activate ind. */
    String CM_CALL_EVENT_PDP_ACTIVATE_IND="cm call event pdp activate ind";

    /** The cm call event pdp activate res. */
    String CM_CALL_EVENT_PDP_ACTIVATE_RES="cm call event pdp activate res";

    /** The cm call event pdp modify req. */
    String CM_CALL_EVENT_PDP_MODIFY_REQ="cm call event pdp modify req";

    /** The cm call event pdp modify ind. */
    String CM_CALL_EVENT_PDP_MODIFY_IND="cm call event pdp modify ind";

    /** The cm call event pdp modify rej. */
    String CM_CALL_EVENT_PDP_MODIFY_REJ="cm call event pdp modify rej";

    /** The cm call event pdp modify conf. */
    String CM_CALL_EVENT_PDP_MODIFY_CONF="cm call event pdp modify conf";

    /** The cm call event rab rel ind. */
    String CM_CALL_EVENT_RAB_REL_IND="cm call event rab rel ind";

    /** The cm call event rab reestab ind. */
    String CM_CALL_EVENT_RAB_REESTAB_IND="cm call event rab reestab ind";

    /** The cm call event rab reestab req. */
    String CM_CALL_EVENT_RAB_REESTAB_REQ="cm call event rab resstab req";

    /** The cm call event rab reestab conf. */
    String CM_CALL_EVENT_RAB_REESTAB_CONF="cm call event rab resstab conf";

    /** The cm call event rab reestab rej. */
    String CM_CALL_EVENT_RAB_REESTAB_REJ="cm call event rab resstab rej";

    /** The cm call event rab reestab fail. */
    String CM_CALL_EVENT_RAB_REESTAB_FAIL="cm call event rab reestab fail";

    /** The cm call event ps data available. */
    String CM_CALL_EVENT_PS_DATA_AVAILABLE="cm call event ps data available";

    /** The cm call event mng calls conf. */
    String CM_CALL_EVENT_MNG_CALLS_CONF="cm call event mng calls conf";

    /** The cm call event call barred. */
    String CM_CALL_EVENT_CALL_BARRED="cm call event call barred";

    /** The cm call event call is waiting. */
    String CM_CALL_EVENT_CALL_IS_WAITING="cm call event call is waiting";

    /** The cm call event call on hold. */
    String CM_CALL_EVENT_CALL_ON_HOLD="cm call event call on hold";

    /** The cm call event call retrieved. */
    String CM_CALL_EVENT_CALL_RETRIEVED="cm call event call retrieved";

    /** The cm call event orig fwd status. */
    String CM_CALL_EVENT_ORIG_FWD_STATUS="cm call event orig fwd status";

    /** The cm call event call forwarded. */
    String CM_CALL_EVENT_CALL_FORWARDED="cm call event call forwarded";

    /** The cm call event call being forwarded. */
    String CM_CALL_EVENT_CALL_BEING_FORWARDED="cm call event call being forwarded";

    /** The cm call event incom fwd call. */
    String CM_CALL_EVENT_INCOM_FWD_CALL="cm call event incom fwd call";

    /** The cm call event call restricted. */
    String CM_CALL_EVENT_CALL_RESTRICTED="cm call event call restricted";

    /** The cm call event cug info received. */
    String CM_CALL_EVENT_CUG_INFO_RECEIVED="cm call event cug info received";

    /** The cm call event cnap info received. */
    String CM_CALL_EVENT_CNAP_INFO_RECEIVED="cm call event cnap info received";

    /** The cm call event emergency flashed. */
    String CM_CALL_EVENT_EMERGENCY_FLASHED="cm call event emergency flashed";

    /** The cm call event progress info ind. */
    String CM_CALL_EVENT_PROGRESS_INFO_IND="cm call event progress info ind";

    /** The cm call event call deflection. */
    String CM_CALL_EVENT_CALL_DEFLECTION="cm call event call deflection";

    /** The cm call event transferred call. */
    String CM_CALL_EVENT_TRANSFERRED_CALL="cm call event transferred call";

    /** The cm call event exit tc. */
    String CM_CALL_EVENT_EXIT_TC="cm call event exit tc";

    /** The cm call event redirecting number. */
    String CM_CALL_EVENT_REDIRECTING_NUMBER="cm call event redirecting number";

    /** The cm call event pdp promote ind. */
    String CM_CALL_EVENT_PDP_PROMOTE_IND="cm call even pdp promote ind";

    /** The cm call event umts cdma handover start. */
    String CM_CALL_EVENT_UMTS_CDMA_HANDOVER_START="cm call event umts cdma handover start";

    /** The cm call event umts cdma handover end. */
    String CM_CALL_EVENT_UMTS_CDMA_HANDOVER_END="cm call event umts cdma handover end";

    /** The cm call event secondary msm. */
    String CM_CALL_EVENT_SECONDARY_MSM="cm call event secondary msm";

    /** The cm call event orig mod to ss. */
    String CM_CALL_EVENT_ORIG_MOD_TO_SS="cm call event orig mod to ss";

    /** The cm call event user data ind. */
    String CM_CALL_EVENT_USER_DATA_IND="cm call event user data ind";

    /** The cm call event user data cong ind. */
    String CM_CALL_EVENT_USER_DATA_CONG_IND="cm call event user data cong ind";

    /** The cm call event modify ind. */
    String CM_CALL_EVENT_MODIFY_IND="cm call event modify ind";

    /** The cm call event modify req. */
    String CM_CALL_EVENT_MODIFY_REQ="cm call event modify req";

    /** The cm call event line ctrl. */
    String CM_CALL_EVENT_LINE_CTRL="cm call event line ctrl";

    /** The origination. */
    String ORIGINATION="origination";

    /** The incoming call. */
    String INCOMING_CALL="incoming call";

    /** The conversation. */
    String CONVERSATION="conversation";

    /** The origination waiting. */
    String ORIGINATION_WAITING="origination waiting";

    /** The circuit switched data. */
    String CIRCUIT_SWITCHED_DATA="circuit switched data";

    /** The packet switched data. */
    String PACKET_SWITCHED_DATA ="packet switched data";

    /** The sms. */
    String SMS="sms";

    /** The position determination. */
    String POSITION_DETERMINATION="position determination";

    /** The test. */
    String TEST="test";

    /** The otasp. */
    String OTASP="otasp";

    /** The standard otasp. */
    String STANDARD_OTASP="standard otasp";

    /** The non standard otasp. */
    String NON_STANDARD_OTASP="non standard otasp";

    /** The emergency. */
    String EMERGENCY="emergency";

    /** The supplementary services. */
    String SUPPLEMENTARY_SERVICES="supplementary services";

    /** The videotelephony. */
    String VIDEOTELEPHONY="videotelephony";

    /** The videotelephony loopback. */
    String VIDEOTELEPHONY_LOOPBACK="videotelephony loopback";

    /** The tone. */
    String TONE="tone";

    /** The isdn. */
    String ISDN="isdn";

    /** The is 54b. */
    String IS_54B="is 54b";

    /** The medium. */
    String MEDIUM="medium";

    /** The high. */
    String HIGH="high";

    /** The low. */
    String LOW="low";

    /** The cm call end offline. */
    String CM_CALL_END_OFFLINE="cm call end offline";

    /** The cm call end cdma lock. */
    String CM_CALL_END_CDMA_LOCK="cm call end cdma lock";

    /** The cm call end no srv. */
    String CM_CALL_END_NO_SRV="cm call end no srv";

    /** The cm call end fade. */
    String CM_CALL_END_FADE="cm call end fade";

    /** The cm call end intercept. */
    String CM_CALL_END_INTERCEPT="cm call end intercept";

    /** The cm call end reorder. */
    String CM_CALL_END_REORDER="cm call end reorder";

    /** The cm call end rel normal. */
    String CM_CALL_END_REL_NORMAL="cm call end rel normal";

    /** The cm call end rel so rej. */
    String CM_CALL_END_REL_SO_REJ="cm call end rel so rej";

    /** The cm call end incom call. */
    String CM_CALL_END_INCOM_CALL="cm call end incom call";

    /** The cm call end alert stop. */
    String CM_CALL_END_ALERT_STOP="cm call end alert stop";

    /** The cm call end client end. */
    String CM_CALL_END_CLIENT_END="cm call end client end";

    /** The cm call end activation. */
    String CM_CALL_END_ACTIVATION="cm call end activation";

    /** The cm call end mc abort. */
    String CM_CALL_END_MC_ABORT="cm call end mc abort";

    /** The cm call end max access probe. */
    String CM_CALL_END_MAX_ACCESS_PROBE="cm call end max access probe";

    /** The cm call end psist ng. */
    String CM_CALL_END_PSIST_NG="cm call end psist ng";

    /** The cm call end uim not present. */
    String CM_CALL_END_UIM_NOT_PRESENT="cm call end uim not present";

    /** The cm call end acc in prog. */
    String CM_CALL_END_ACC_IN_PROG="cm call end acc in prog";

    /** The cm call end acc fail. */
    String CM_CALL_END_ACC_FAIL="cm call end acc fail";

    /** The cm call end retry order. */
    String CM_CALL_END_RETRY_ORDER="cm call end retry order";

    /** The cm call end ccs not supported by bs. */
    String CM_CALL_END_CCS_NOT_SUPPORTED_BY_BS="cm call end ccs not supported by bs";

    /** The cm call end no response from bs. */
    String CM_CALL_END_NO_RESPONSE_FROM_BS="cm call end no response from bs";

    /** The cm call end rejected by bs. */
    String CM_CALL_END_REJECTED_BY_BS="cm call end rejected by bs";

    /** The cm call end incompatible. */
    String CM_CALL_END_INCOMPATIBLE="cm call end incompatible";

    /** The cm call end access block. */
    String CM_CALL_END_ACCESS_BLOCK="cm call end access block";

    /** The cm call end already in tc. */
    String CM_CALL_END_ALREADY_IN_TC="cm call end already in tc";

    /** The cm call end emergency flashed. */
    String CM_CALL_END_EMERGENCY_FLASHED="cm call end emergency flashed";

    /** The cm call end user call orig during gps. */
    String CM_CALL_END_USER_CALL_ORIG_DURING_GPS="cm call end user call orig during gps";

    /** The cm call end user call orig during sms. */
    String CM_CALL_END_USER_CALL_ORIG_DURING_SMS="cm call end user call orig during sms";

    /** The cm call end user call orig during data. */
    String CM_CALL_END_USER_CALL_ORIG_DURING_DATA="cm call end user call orig during data";

    /** The cm call end redir or handoff. */
    String CM_CALL_END_REDIR_OR_HANDOFF="cm call end redir or handoff";

    /** The cm call end ll cause. */
    String CM_CALL_END_LL_CAUSE="cm call end ll cause";

    /** The cm call end conf failed. */
    String CM_CALL_END_CONF_FAILED="cm call end conf failed";

    /** The cm call end incom rej. */
    String CM_CALL_END_INCOM_REJ="cm call end incom rej";

    /** The cm call end setup rej. */
    String CM_CALL_END_SETUP_REJ="cm call end setup rej";

    /** The cm call end network end. */
    String CM_CALL_END_NETWORK_END="cm call end network end";

    /** The cm call end no funds. */
    String CM_CALL_END_NO_FUNDS="cm call end no funds";

    /** The cm call end no gw srv. */
    String CM_CALL_END_NO_GW_SRV="cm call end no gw srv";

    /** The cm call end no cdma srv. */
    String CM_CALL_END_NO_CDMA_SRV="cm call end no cdma srv";

    /** The cm call end no full srv. */
    String CM_CALL_END_NO_FULL_SRV="cm call end no full srv";

    /** The cm call end max ps calls. */
    String CM_CALL_END_MAX_PS_CALLS="cm call end max ps calls";

    /** The cm call end cd gen or busy. */
    String CM_CALL_END_CD_GEN_OR_BUSY="cm call end cd gen or busy";

    /** The cm call end cd bill or auth. */
    String CM_CALL_END_CD_BILL_OR_AUTH="cm call end cd bill or auth";

    /** The cm call end chg hdr. */
    String CM_CALL_END_CHG_HDR="cm call end chg hdr";

    /** The cm call end exit hdr. */
    String CM_CALL_END_EXIT_HDR="cm call end exit hdr";

    /** The cm call end hdr no session. */
    String CM_CALL_END_HDR_NO_SESSION="cm call end hdr no session";

    /** The cm call end cm colloc acq fail. */
    String CM_CALL_END_CM_COLLOC_ACQ_FAIL="cm call end cm colloc acq fail";

    /** The cm call end hdr orig during gps fix. */
    String CM_CALL_END_HDR_ORIG_DURING_GPS_FIX="cm call end hdr orig during gps fix";

    /** The cm call end hdr cs timeout. */
    String CM_CALL_END_HDR_CS_TIMEOUT="cm call end hdr cs timeout";

    /** The cm call end hdr released by cm. */
    String CM_CALL_END_HDR_RELEASED_BY_CM="cm call end hdr released by cm";

    /** The cm call end hold dbm in prog. */
    String CM_CALL_END_HOLD_DBM_IN_PROG="cm call end hold dbm in prog";

    /** The cm call end no hybr hdr srv. */
    String CM_CALL_END_NO_HYBR_HDR_SRV="cm call end no hybr hdr srv";

    /** The cm call end hdr no lock granted. */
    String CM_CALL_END_HDR_NO_LOCK_GRANTED="cm call end hdr no lock granted";

    /** The cm call end video conn. */
    String CM_CALL_END_VIDEO_CONN="cm call end video conn";

    /** The cm call end video setup failure. */
    String CM_CALL_END_VIDEO_SETUP_FAILURE="cm call end video setup failure";

    /** The cm call end video protocol closed. */
    String CM_CALL_END_VIDEO_PROTOCOL_CLOSED="cm call end video protocol closed";

    /** The cm call end video protocol setup closed. */
    String CM_CALL_END_VIDEO_PROTOCOL_SETUP_CLOSED="cm call end video protocol setup closed";

    /** The cm call end internal error. */
    String CM_CALL_END_INTERNAL_ERROR="cm call end internal error";

    /** The no service. */
    String NO_SERVICE="no service";

    /** The amps. */
    String AMPS="amps";

    /** The cdma. */
    String CDMA="cdma";

    /** The gps. */
    String GPS="gps";

    /** The gsm wcdma. */
    String GSM_WCDMA="gsm and wcdma";

    /** The wlan. */
    String WLAN="wlan";

    /** The gsm wcdma lte. */
    String GSM_WCDMA_LTE="gsm,wcdma and lte";


    //B184


    /** The scc config. */
    String SCC_CONFIG="SCC CONFIG";

    /** The scc deconfig. */
    String SCC_DECONFIG="SCC DECONFIG";

    /** The scc activate. */
    String SCC_ACTIVATE="SCC ACTIVATE";

    /** The scc deactivate. */
    String SCC_DEACTIVATE="SCC DEACTIVATE";

    /** The scc vrlf activate. */
    String SCC_VRLF_ACTIVATE="SCC VRLF ACTIVATE";

    /** The scc vrlf deactivate. */
    String SCC_VRLF_DEACTIVATE="SCC VRLF DEACTIVATE";

    /** The activated. */
    String ACTIVATED= "ACTIVATED";

    /** The deactivated. */
    String DEACTIVATED="DEACTIVATED";

    //B17F

    /** The release 8. */
    String RELEASE_8= "Release 8";

    /** The release 9. */
    String RELEASE_9="Release 9";

    /** The false. */
    String FALSE="false";

    /** The true. */
    String TRUE="true";

    /** The no neighbor cell. */
    String NO_NEIGHBOR_CELL ="Measure no neighbor cells";

    /** The intra freq neighbor. */
    String INTRA_FREQ_NEIGHBOR="Measure intra frequency neighbor cells";

    /** The low priority neighbor cell. */
    String LOW_PRIORITY_NEIGHBOR_CELL="Measure low priority neighbor cells";

    /** The equal priority neighbor cell. */
    String EQUAL_PRIORITY_NEIGHBOR_CELL="Measure equal priority neighbor cells";

    /** The high priority neighbor cell. */
    String HIGH_PRIORITY_NEIGHBOR_CELL="Measure high priority neighbor cells";

    /** The all neighbor cell. */
    String ALL_NEIGHBOR_CELL="Measure all neighbor cells";

    /** The pucch. */
    //B16D
    String PUCCH ="pucch";

    /** The pusch. */
    String PUSCH ="pusch";

    /** The ack nak not present. */
    String ACK_NAK_NOT_PRESENT ="ACK/NAK not present";

    /** The ack nak present. */
    String ACK_NAK_PRESENT="ACK/NAK present";

    /** The fdd mode. */
    String FDD_MODE="Fdd Mode";

    /** The tdd multiplexing mode. */
    String TDD_MULTIPLEXING_MODE="Tdd Multiplexing Mode";

    /** The tdd bundling mode. */
    String TDD_BUNDLING_MODE="TDD Bundling Mode";

    /** The tdd single cell pucch. */
    String TDD_SINGLE_CELL_PUCCH="TDD Single Cell PUCCH format 1b with CS";

    /** The tdd ca pucch. */
    String TDD_CA_PUCCH="TDD CA PUCCH format 1b with CS";

    /** The pucch format 3. */
    String PUCCH_FORMAT_3="PUCCH format 3";

    /** The not present. */
    String NOT_PRESENT="Not Present";

    /** The present. */
    String PRESENT="Present";


    //B13C

    /** The normal. */
    String NORMAL="Normal";

    /** The short. */
    String SHORT="Shorten 2nd";

    /** The off. */
    String OFF="OFF";

    /** The on. */
    String ON="ON";

    /** The scc 1. */
    String SCC_1="SCC1";

    /** The scc 2. */
    String SCC_2="SCC2";

    /** The cm ss event srv changed. */
    //134F
    String CM_SS_EVENT_SRV_CHANGED ="CM SS EVENT SRV CHANGED";

    /** The cm ss event rssi. */
    String CM_SS_EVENT_RSSI="CM SS EVENT RSSI";

    /** The cm ss event info. */
    String CM_SS_EVENT_INFO ="CM SS EVENT INFO";

    /** The cm ss event reg success. */
    String CM_SS_EVENT_REG_SUCCESS="CM SS EVENT REG SUCCESS";

    /** The cm ss event reg failure. */
    String CM_SS_EVENT_REG_FAILURE="CM SS EVENT REG FAILURE";

    /** The cm ss event hdr rssi. */
    String CM_SS_EVENT_HDR_RSSI="CM SS EVENT HDR RSSI";

    /** The cm ss event wlan rssi. */
    String CM_SS_EVENT_WLAN_RSSI="CM SS EVENT WLAN RSSI";

    /** The cm ss event srv new. */
    String CM_SS_EVENT_SRV_NEW="CM SS EVENT SRV NEW";

    /** The cm ss event secondary msm. */
    String CM_SS_EVENT_SECONDARY_MSM="CM SS EVENT SECONDARY MSM";

    /** The cm ss event ps data avail. */
    String CM_SS_EVENT_PS_DATA_AVAIL="CM SS EVENT PS DATA AVAIL";

    /** The cm ss event ps data fail. */
    String CM_SS_EVENT_PS_DATA_FAIL="CM SS EVENT_PS DATA FAIL";

    /** The cm ss event ps data success. */
    String CM_SS_EVENT_PS_DATA_SUCCESS="CM SS EVENT PS DATA SUCCESS";

    /** The cm ss event wlan stats. */
    String CM_SS_EVENT_WLAN_STATS="CM SS EVENT WLAN STATS";

    /** The cm ss event orig thr tbl update. */
    String CM_SS_EVENT_ORIG_THR_TBL_UPDATE="CM SS EVENT ORIG THR TBL UPDATE";

    /** The cm ss sid mask. */
    String CM_SS_SID_MASK="CM SS SID MASK";

    /** The cm ss nid mask. */
    String CM_SS_NID_MASK="CM SS NID MASK";

    /** The cm ss reg zone mask. */
    String CM_SS_REG_ZONE_MASK="CM SS REG ZONE MASK";

    /** The cm ss packet zone mask. */
    String CM_SS_PACKET_ZONE_MASK="CM SS PACKET ZONE MASK";

    /** The cm ss bs p rev mask. */
    String CM_SS_BS_P_REV_MASK="CM SS BS P REV MASK";

    /** The cm ss p rev in use mask. */
    String CM_SS_P_REV_IN_USE_MASK="CM SS P REV IN USE MASK";

    /** The cm ss ccs supported mask. */
    String CM_SS_CCS_SUPPORTED_MASK="CM SS CCS SUPPORTED MASK";

    /** The cm ss srv status mask. */
    String CM_SS_SRV_STATUS_MASK="CM SS SRV STATUS MASK";

    /** The cm ss srv domain mask. */
    String CM_SS_SRV_DOMAIN_MASK="CM SS SRV DOMAIN MASK";

    /** The cm ss srv capability mask. */
    String CM_SS_SRV_CAPABILITY_MASK="CM SS SRV CAPABILITY MASK";

    /** The cm ss sys mode mask. */
    String CM_SS_SYS_MODE_MASK="CM SS SYS MODE MASK";

    /** The cm ss roam status mask. */
    String CM_SS_ROAM_STATUS_MASK="CM SS ROAM STATUS MASK";

    /** The cm ss sys id mask. */
    String CM_SS_SYS_ID_MASK="CM SS SYS ID MASK";

    /** The cm ss srv ind mask. */
    String CM_SS_SRV_IND_MASK="CM SS SRV IND MASK";

    /** The cm ss mobility mgmt mask. */
    String CM_SS_MOBILITY_MGMT_MASK="CM SS MOBILITY MGMT MASK";

    /** The cm ss idle digital mode mask. */
    String CM_SS_IDLE_DIGITAL_MODE_MASK="CM SS IDLE DIGITAL MODE MASK";

    /** The cm ss sim state mask. */
    String CM_SS_SIM_STATE_MASK="CM SS SIM STATE MASK";

    /** The cm ss plmn forbidden mask. */
    String CM_SS_PLMN_FORBIDDEN_MASK="CM SS PLMN FORBIDDEN MASK";

    /** The cm ss ps data suspend mask. */
    String CM_SS_PS_DATA_SUSPEND_MASK="CM SS PS DATA SUSPEND MASK";

    /** The cm ss uz changed mask. */
    String CM_SS_UZ_CHANGED_MASK="CM SS UZ CHANGED MASK";

    /** The cm ss hdr srv status mask. */
    String CM_SS_HDR_SRV_STATUS_MASK="CM SS HDR SRV STATUS MASK";

    /** The cm ss hdr roam status mask. */
    String CM_SS_HDR_ROAM_STATUS_MASK="CM SS HDR ROAM STATUS MASK";

    /** The cm ss main bcmcs srv supported mask. */
    String CM_SS_MAIN_BCMCS_SRV_SUPPORTED_MASK="CM SS MAIN BCMCS SRV SUPPORTED MASK";

    /** The cm ss main bcmcs srv status mask. */
    String CM_SS_MAIN_BCMCS_SRV_STATUS_MASK="CM SS MAIN BCMCS SRV STATUS MASK";

    /** The cm ss hybr hdr bcmcs srv supported mask. */
    String CM_SS_HYBR_HDR_BCMCS_SRV_SUPPORTED_MASK="CM SS HYBR HDR BCMCS SRV SUPPORTED MASK";

    /** The cm ss hybr hdr bcmcs srv status mask. */
    String CM_SS_HYBR_HDR_BCMCS_SRV_STATUS_MASK="CM SS HYBR HDR BCMCS SRV STATUS MASK";

    /** The cm ss base station parms chgd mask. */
    String CM_SS_BASE_STATION_PARMS_CHGD_MASK="CM SS BASE STATION PARMS CHGD MASK";

    /** The cm ss wlan srv status mask. */
    String CM_SS_WLAN_SRV_STATUS_MASK="CM SS WLAN SRV STATUS MASK";

    /** The cm ss wlan bss info mask. */
    String CM_SS_WLAN_BSS_INFO_MASK="CM SS WLAN BSS INFO MASK";

    /** The cm ss orig thr status mask. */
    String CM_SS_ORIG_THR_STATUS_MASK="CM SS ORIG THR STATUS MASK";

    /** The cm ss hdr sys id mask. */
    String CM_SS_HDR_SYS_ID_MASK="CM SS HDR SYS ID MASK";

    /** The enqueued. */
    //156B
    String ENQUEUED="ENQUEUED";

    /** The delayed underflow enqueued. */
    String DELAYED_UNDERFLOW_ENQUEUED="DELAYED UNDERFLOW ENQUEUED";

    /** The delayed underflow not enqueued. */
    String DELAYED_UNDERFLOW_NOT_ENQUEUED="DELAYED UNDERFLOW NOT ENQUEUED";

    /** The erased underflow. */
    String ERASED_UNDERFLOW="ERASED UNDERFLOW";

    /** The stale not enqueued. */
    String STALE_NOT_ENQUEUED="STALE NOT ENQUEUED";


    /** The attach request csfb. */
    //B0ED
    String ATTACH_REQUEST_CSFB="EPS mobility management messages: \nAttach request";

    /** The attach complete csfb. */
    String ATTACH_COMPLETE_CSFB="EPS mobility management messages: \nAttach complete";

    /** The csi 0. */
    //B14D
    String CSI_0="CSI0";

    /** The csi 1. */
    String CSI_1="CSI1";

    /** The locationinfo not. */
    //B0CA
    String LOCATIONINFO_NOT="locationInfo is not present";

    /** The locationinfo. */
    String LOCATIONINFO="locationInfo present";

    /** The north. */
    String NORTH="North";

    /** The south. */
    String SOUTH="South";

    /** The eillipsoid point. */
    String EILLIPSOID_POINT ="EillipsoidPoint present";

    /** The eillipsoid point present. */
    String EILLIPSOID_POINT_PRESENT="EillipsoidPointWithAltitude present";

    /** The height. */
    String HEIGHT="Height";

    /** The depth. */
    String DEPTH="Depth";

    /** The servcellidentity not. */
    String SERVCELLIDENTITY_NOT ="servCellIdentity does not present";

    /** The servcellidentity. */
    String SERVCELLIDENTITY="servCellIdentity present";

    /** The initial request. */
    //B0ED
    String INITIAL_REQUEST="Initial Request";

    /** The unused. */
    String UNUSED="Unused";

    /** The ipv4v6. */
    String IPV4V6="IpV4V6";

    /** The one jan 1980. */
    long ONE_JAN_1980 = 315964800000L;

    /** The weighted least squares. */
    //1476
    String WEIGHTED_LEAST_SQUARES = "Weighted Least Squares (WLS)";

    /** The kalman filter. */
    String KALMAN_FILTER="Kalman Filter (KF)";

    /** The externally injected. */
    String EXTERNALLY_INJECTED="Externally injected";

    /** The internal database. */
    String INTERNAL_DATABASE="Internal database";

    /** The internal database. */
    String PLE="Ple";

    /** The wifi. */
    String WIFI="WiFi";

    /** The no ciphering. */
    //5135
    String NO_CIPHERING="No ciphering";

    /** The start ciphering. */
    String START_CIPHERING="Start ciphering";

    /** The a5 0. */
    String A5_0="A5/1";

    /** The a5 1. */
    String A5_1="A5/2";

    /** The a5 2. */
    String A5_2="A5/3";

    /** The a5 3. */
    String A5_3="A5/4";

    /** The a5 4. */
    String A5_4="A5/5";

    /** The a5 5. */
    String A5_5="A5/6";

    /** The a5 6. */
    String A5_6="A5/7";

    /** The signaling only. */
    String SIGNALING_ONLY="Signaling Only";

    /** The speech version1. */
    String SPEECH_VERSION1="Speech Version1";

    /** The speech version2. */
    String SPEECH_VERSION2="Speech Version2";

    /** The speech version3. */
    String SPEECH_VERSION3="Speech Version3";

    /** The data 14400. */
    String DATA_14400="Data 14400";

    /** The data 9600. */
    String DATA_9600="Data 9600";

    /** The data 4800. */
    String DATA_4800="Data 4800";

    /** The data 2400. */
    String DATA_2400="Data 2400";

    /** The full rate traffic. */
    String FULL_RATE_TRAFFIC="Full Rate Traffic";

    /** The half rate traffic. */
    String HALF_RATE_TRAFFIC="Half Rate Traffic";

    /** The sdcch 4. */
    String SDCCH_4="SDCCH/4";

    /** The sdcch 8. */
    String SDCCH_8="SDCCH/8";

    /** The level 0. */
    String LEVEL_0="Level 0";

    /** The level 1. */
    String LEVEL_1="Level 1";

    /** The level 2. */
    String LEVEL_2="Level 2";

    /** The not valid. */
    //5130
    String NOT_VALID="Not Valid";

    /** The valid. */
    String VALID="Valid";

    /** The sysinfo 16 17 not supported. */
    String SYSINFO_16_17_NOT_SUPPORTED="SysInfo 16/17 Not Supported";

    /** The sysinfo 16 17 on bcch. */
    String SYSINFO_16_17_ON_BCCH="SysInfo 16/17 on BCCH";

    /** The not supported. */
    String NOT_SUPPORTED="Not Supported";

    /** The supported. */
    String SUPPORTED="Supported";

    /** The cell reselect offset. */
    String CELL_RESELECT_OFFSET="Cell Reselect Offset";

    /** The disconnected. */
    //4125
    String DISCONNECTED="Disconnected";

    /** The cell fach. */
    String CELL_FACH="CELL FACH";

    /** The cell dch. */
    String CELL_DCH="CELL DCH";

    /** The cell pch. */
    String CELL_PCH="CELL PCH";

    /** The ura pch. */
    String URA_PCH="URA PCH";

    /** The no. */
    //B16C
    String NO="NO";

    /** The yes. */
    String YES="Yes";

    /** The format 3. */
    String FORMAT_3="Format 3";

    /** The format 3a. */
    String FORMAT_3A="Format 3a";

    /** The cell selection reselction procedure. */
    //4126
    String CELL_SELECTION_RESELCTION_PROCEDURE="Cell selection/reselection procedure";

    /** The sib porcessing procedure. */
    String SIB_PORCESSING_PROCEDURE="SIB processing procedure";

    /** The paging type 2 procedure. */
    String PAGING_TYPE_2_PROCEDURE="Paging type 2 procedure";

    /** The measurement control report procedure. */
    String MEASUREMENT_CONTROL_REPORT_PROCEDURE="Measurement control/report procedure";

    /** The rrc connection establishment procedure. */
    String RRC_CONNECTION_ESTABLISHMENT_PROCEDURE="RRC connection establishment procedure";

    /** The rrc connection release procedure. */
    String RRC_CONNECTION_RELEASE_PROCEDURE="RRC connection release procedure";

    /** The ue capability information. */
    String UE_CAPABILITY_INFORMATION="UE capability information";

    /** The ue capability enquiry. */
    String UE_CAPABILITY_ENQUIRY="UE capability enquiry";

    /** The initial direct transfer. */
    String INITIAL_DIRECT_TRANSFER="Initial direct transfer";

    /** The uplink direct transfer. */
    String UPLINK_DIRECT_TRANSFER="Uplink direct transfer";

    /** The downlink direct transfer. */
    String DOWNLINK_DIRECT_TRANSFER="Downlink direct transfer";

    /** The signaling connection release. */
    String SIGNALING_CONNECTION_RELEASE="Signaling connection release";

    /** The signaling connection release request. */
    String SIGNALING_CONNECTION_RELEASE_REQUEST="Signaling connection release request";

    /** The counter check. */
    String COUNTER_CHECK="Counter check";

    /** The radio bearer establishment. */
    String RADIO_BEARER_ESTABLISHMENT="Radio bearer establishment";

    /** The radio bearer reconfiguration. */
    String RADIO_BEARER_RECONFIGURATION="Radio bearer reconfiguration";

    /** The radio bearer release. */
    String RADIO_BEARER_RELEASE="Radio bearer release";

    /** The transport channel reconfiguration. */
    String TRANSPORT_CHANNEL_RECONFIGURATION="Transport channel reconfiguration";

    /** The physical channel reconfiguration. */
    String PHYSICAL_CHANNEL_RECONFIGURATION="Physical channel reconfiguration";

    /** The transport format combination control. */
    String TRANSPORT_FORMAT_COMBINATION_CONTROL="Transport format combination control";

    /** The cell update procedure. */
    String CELL_UPDATE_PROCEDURE="Cell update procedure";

    /** The ura update porcedure. */
    String URA_UPDATE_PORCEDURE="URA update procedure";

    /** The utran mobility information. */
    String UTRAN_MOBILITY_INFORMATION="UTRAN mobility information";

    /** The active set update soft handover. */
    String ACTIVE_SET_UPDATE_SOFT_HANDOVER="Active set update in soft handover";

    /** The intersystem handover from utran. */
    String INTERSYSTEM_HANDOVER_FROM_UTRAN="Intersystem handover from UTRAN";

    /** The intersystem handover to utran. */
    String INTERSYSTEM_HANDOVER_TO_UTRAN="Intersystem handover to UTRAN";

    /** The intersystem cell reselction from utran. */
    String INTERSYSTEM_CELL_RESELCTION_FROM_UTRAN="Intersystem cell reselection from UTRAN";

    /** The intersystem cell reselction to utran. */
    String INTERSYSTEM_CELL_RESELCTION_TO_UTRAN="Intersystem cell reselection to UTRAN";

    /** The paging type 1 procedure. */
    String PAGING_TYPE_1_PROCEDURE="Paging type 1 procedure";

    /** The security mode control procedure. */
    String SECURITY_MODE_CONTROL_PROCEDURE="Security mode control procedure";

    /** The configuration unsupported. */
    String CONFIGURATION_UNSUPPORTED="Configuration unsupported";

    /** The physical channel failure. */
    String PHYSICAL_CHANNEL_FAILURE="Physical channel failure";

    /** The incompatible simultaneous reconfiguration. */
    String INCOMPATIBLE_SIMULTANEOUS_RECONFIGURATION="Incompatible simultaneous reconfiguration";

    /** The protocol error. */
    String PROTOCOL_ERROR="Protocol Error";

    /** The compressed mode runtime error. */
    String COMPRESSED_MODE_RUNTIME_ERROR="Compressed mode runtime error";

    /** The cell reselection. */
    String CELL_RESELECTION="Cell Reselection";

    /** The invalid configuration. */
    String INVALID_CONFIGURATION="Invalid configuration";

    /** The configuration incomplete. */
    String CONFIGURATION_INCOMPLETE="Configuration Incomplete";

    /** The unsupported measurement. */
    String UNSUPPORTED_MEASUREMENT="UnSupported Measurement";

    /** The asn 1 violation or encoding error. */
    String ASN_1_VIOLATION_OR_ENCODING_ERROR="ASN 1 violation or encoding error";

    /** The msg type nonexistent or not implemented. */
    String MSG_TYPE_NONEXISTENT_OR_NOT_IMPLEMENTED="Msg type nonexistent or not implemented";

    /** The msg not compatible with receiver state. */
    String MSG_NOT_COMPATIBLE_WITH_RECEIVER_STATE="Msg not compatible with receiver state";

    /** The ie value not comprehended. */
    String IE_VALUE_NOT_COMPREHENDED="IE value not comprehended";

    /** The conditional ie error. */
    String CONDITIONAL_IE_ERROR="Conditional IE error";

    /** The msg extension not comprehended. */
    String MSG_EXTENSION_NOT_COMPREHENDED="Msg extension not comprehended";

    /** The cfg not present. */
    //B161
    String CFG_NOT_PRESENT="Cfg Not Present";

    /** The cfg present. */
    String CFG_PRESENT="Cfg Present";

    /** The tpc pucch disabled. */
    String TPC_PUCCH_DISABLED="Tpc PUCCH Disabled";

    /** The tpc pucch enabled. */
    String TPC_PUCCH_ENABLED="Tpc PUCCH Enabled";

    /** The dci format 3. */
    String DCI_FORMAT_3="DCI Format 3";

    /** The dci format 3a. */
    String DCI_FORMAT_3A="DCI Format 3A";

    /** The tpc pusch disabled. */
    String TPC_PUSCH_DISABLED="Tpc Pusch Disabled";

    /** The tpc pusch enabled. */
    String TPC_PUSCH_ENABLED="Tpc Pusch Enabled";

    /** The n2 txantenna tm3. */
    String N2_TXANTENNA_TM3="n2TxAntenna-tm3";

    /** The n4 txantenna tm3. */
    String N4_TXANTENNA_TM3="n4TxAntenna-tm3";

    /** The n2 txantenna tm4. */
    String N2_TXANTENNA_TM4="n2TxAntenna-tm4";

    /** The n4 txantenna tm4. */
    String N4_TXANTENNA_TM4="n4TxAntenna-tm4";

    /** The n4 txantenna tm5. */
    String N4_TXANTENNA_TM5="n4TxAntenna-tm5";

    /** The tx mode 1. */
    String TX_MODE_1="Tx mode 1";

    /** The tx mode 2. */
    String TX_MODE_2="Tx mode 2";

    /** The tx mode 3. */
    String TX_MODE_3="Tx mode 3";

    /** The tx mode 4. */
    String TX_MODE_4="Tx mode 4";

    /** The tx mode 5. */
    String TX_MODE_5="Tx mode 5";

    /** The tx mode 6. */
    String TX_MODE_6="Tx mode 6";

    /** The tx mode 7. */
    String TX_MODE_7="Tx mode 7";

    /** The tx mode spare 1. */
    String TX_MODE_SPARE_1="Tx mode spare 1";

    /** The tx mode spare 2. */
    String TX_MODE_SPARE_2="Tx mode spare 2";

    /** The closed loop. */
    String CLOSED_LOOP="Closed Loop";

    /** The open loop. */
    String OPEN_LOOP="Open Loop";

    /** The pa fw hypothesis. */
    String PA_FW_HYPOTHESIS="PA_FW_HYPOTHESIS";

    /** The bundling. */
    //B165
    String BUNDLING="Bundling";

    /** The multiplexing. */
    String MULTIPLEXING="MultiPlexing";

    /** The subframe assign 0. */
    //B1B1
    String SUBFRAME_ASSIGN_0="SubFrame Assign 0";

    /** The subframe assign 1. */
    String SUBFRAME_ASSIGN_1="SubFrame Assign 1";

    /** The subframe assign 2. */
    String SUBFRAME_ASSIGN_2="SubFrame Assign 2";

    /** The subframe assign 3. */
    String SUBFRAME_ASSIGN_3="SubFrame Assign 3";

    /** The subframe assign 4. */
    String SUBFRAME_ASSIGN_4="SubFrame Assign 4";

    /** The subframe assign 5. */
    String SUBFRAME_ASSIGN_5="SubFrame Assign 5";

    /** The subframe assign 6. */
    String SUBFRAME_ASSIGN_6="SubFrame Assign 6";



	/** The Constant MIN_RXLEVEL. */
	double MIN_RXLEVEL = -113;

	/** The Constant MAX_RXLEVEL. */
	double MAX_RXLEVEL = -51;

	/** The Constant MAX_RXQUALITY. */
	double MAX_RXQUALITY = 7;

	/** The Constant MIN_RXQUALITY. */
	double MIN_RXQUALITY = 0;

	/** The Constant MIN_RSRP_NEIGHBOUR. */
	double MIN_RSRP_NEIGHBOUR = -180;

	/** The Constant MAX_RSRP_NEIGHBOUR. */
	double MAX_RSRP_NEIGHBOUR = -30;

	/** The Constant MAX_RSRQ_NEIGHBOUR. */
	double MAX_RSRQ_NEIGHBOUR = 10;

	/** The Constant MIN_RSRQ_NEIGHBOUR. */
	double MIN_RSRQ_NEIGHBOUR = -30;

	/** The Constant MAX_RSSI. */
	double MAX_RSSI = -10;

	/** The Constant MIN_RSSI. */
	double MIN_RSSI = -110;

	/** The Constant MAX_RSRP. */
	double MAX_RSRP = -40;

	/** The Constant MIN_RSRQ. */
	double MIN_RSRQ = -30;

	/** The Constant MAX_RSRQ. */
	double MAX_RSRQ = 0;

	/** The Constant MIN_RSRP. */
	double MIN_RSRP = -140;

	/** The parse. */
	String PARSE_STRING = "PARSE";

	/** The class 1. */
	String CLASS_1 = "Class 1";

	/** The class 2. */
	String CLASS_2 = "Class 2";

	/** The class 3. */
	String CLASS_3 = "Class 3";

	/** The cs ps service. */
	String CS_PS_SERVICE = "CS PS Service";

	/** The cs only. */
	String CS_ONLY = "CS Only";

	/** The ps only. */
	String PS_ONLY = "PS Only";

	/** The limited service. */
	String LIMITED_SERVICE = "Limited Service";


	/** The blank string. */
	// Asn class constants
	String BLANK_STRING = "";

	/** The space. */
	String SPACE = " ";

	/** The ffasn dump path. */
	String FFASN_DUMP_PATH="FFASN_DUMP_PATH";

	/** The asn per file. */
	//LTE
	String ASN_PER_FILE="ASN_PER_FILE";

	/** The asn script path. */
	String ASN_SCRIPT_PATH="ASN_SCRIPT_PATH";

	/** The asn per file 3g. */
	//3G
	String ASN_PER_FILE_3G="ASN_PER_FILE_3G";

	/** The asn 3g script path. */
	String ASN_3G_SCRIPT_PATH="ASN_3G_SCRIPT_PATH";

	/** The asn2g msg file path. */
	String ASN2G_MSG_FILE_PATH="ASN2G_MSG_FILE_PATH";

	/** The pcap file path. */
	String PCAP_FILE_PATH="PCAP_FILE_PATH";

	/** The hex decoded file path. */
	String HEX_DECODED_FILE_PATH="HEX_DECODED_FILE_PATH";

	/** The exe 2g file path. */
	String EXE_2G_FILE_PATH="EXE_2G_FILE_PATH";
	
	/** The exe file path. */
	String EXE_FILE_PATH="EXE_FILE_PATH";

	String ASN_COMPILER_PATH="ASN_COMPILER_PATH";
	/** The zero. */
	//// Magic Numbers
	int ZERO=0;

	/** The one. */
	int ONE=1;

	/** The twelve. */
	int TWELVE=12;

	/** The thirteen. */
	int THIRTEEN=13;

	/** The fourteen. */
	int FOURTEEN=14;

	/** The sixteen. */
	int SIXTEEN=16;

	/** The fourty. */
	int FOURTY=40;

	/** The sixty. */
	int SIXTY=60;

	/** The latitude. */
	String LATITUDE = "LATITUDE";

	/** The longitude. */
	String LONGITUDE = "LONGITUDE";

	/** The dump file path. */
	String DUMP_FILE_PATH = "DUMP_FILE_PATH";

	String PERCENT02X = "%02X";

	String DOT_DLF_FILE_EXTENSION = ".dlf";

	/** The dot qmdl file extension. */
	String DOT_QMDL_FILE_EXTENSION=".qmdl";

	/** The comma. */
	String COMMA=",";

	/** The so file path. */
	String SO_FILE_PATH="SO_FILE_PATH";

    /** The Constant DATE_FROMAT_NV. */
    String DATE_FROMAT_NV = "yyyy_MM_dd_hh_mm_ss";

    /** The Constant NEXT_LINE. */
    String NEXT_LINE = "\n";

    /** The Constant QMDL_SOURCE_FILE. */
    String QMDL_SOURCE_FILE="QMDL_SOURCE_FILE";

    /** The Constant QMDL_SOURCE_FILE. */
    Integer EXTRA_BYTES=0x00;

	String TXT = ".txt";  

	String RANK_1="Rank 1";
    String RANK_2="Rank 2";
    String RANK_3="Rank 3";
    String RANK_4="Rank 4";
    String C_RNTI="C Rnti";
    String SPS_C_RNTI="Sps C Rnti";
    String SPS_RNTI="Sps Rnti";
    String P_RNTI="P Rnti";
    String RA_RNTI="Ra Rnti";
    String T_C_RNTI="T C Rnti";
    String SI_RNTI="Si Rnti";
    String TPC_PUSCH_RNTI="Tpc Pusch Rnti";
    String TPC_PUCCH_RNTI="Tpc Pucch Rnti";
    String MBMS_RNTI="Mbms Rnti";
    String C_RNTI_ANT1="C Rnti Anti";
    String TEMP_RNTI="Temp Rnti";    
    String DISABLED ="Disabled";
    String ENABLED ="Enabled";  
    String NO_BMOD = "NO BMOD";
    String SVS2 = "SVS2";
    String SVS = "SVS";
    String NOM = "NOM";
    String TURBO = "TURBO";
    String BMOD = "BMOD";
    String RMLM = "RMLM";
    String WIDE_BAND = "Wideband";
    String SUB_BAND = "Subband";
    String ANTENNA_1 = "1 Antenna";
    String ANTENNA_2 = "2 Antennas";
    String ANTENNA_3 = "3 Antennas";
    String ANTENNA_4 = "4 Antennas";
    String PORT_7_ONLY = "Port 7 Only";
    String PORT_8_ONLY = "Port 8 Only";
    String PORT_7_8_ONLY = "Port 7 and 8";
    String EXISTS = "Exists";
    String NO_SKIPPED = "Not Skipped";
    String SKIPPED = "Skipped";
    String TEMP_C_RNTI = "Temporary C-RNTI";
    String SW_DISABLE="SW_DISABLE";
    String STRONG_ICELL_CHANGED="STRONG_ICELL_CHANGED";
    String ICELL_MBSFN_SF="ICELL_MBSFN_SF";
    String CLK_NOT_READY="CLK_NOT_READY";
    String SCELL_TDD_SSF="SCELL_TDD_SSF";
    String NON_SUPPORYT_GRANT="NON_SUPPORT_GRANT";
    String PUSCH_FIRST_SYM_BLANK="PUSCH FIRST SYM BLANK";
    String PUSCH_LAST_SYM_BLANK="PUSCH LAST SYM BLANK";
    String FIRST_LAST_SYM_BLANK="FIRST LAST SYM BLANK";
    String NONE_SYM_BLANK= "NONE SYM BLANK";
    String NEW = "NEW";
    String DTX = "DTX";
    String PB0="PB = 0 RhoB/RhoA 1";
    String PB1="PB = 1 RhoB/RhoA 4/5";
    String PB2="PB = 2 RhoB/RhoA 3/5";
    String PB3="PB = 1 RhoB/RhoA 2/5";
    String OPMODE1="TILE0_4RX";
    String OPMODE2="TILE0_3CA";
    String OPMODE3="TILE0_QED";
    String OPMODE4="TILE0_QICE";
    String OPMODE5="TILE1_4RX";
    String OPMODE6="TILE1_2CA";
    String OPMODE7="TILE1_QED";
    String OPMODE8="TILE1_QICE";
}

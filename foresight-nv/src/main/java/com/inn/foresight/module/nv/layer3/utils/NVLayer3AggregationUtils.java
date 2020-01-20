package com.inn.foresight.module.nv.layer3.utils;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.geometry.MensurationUtils;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.Layer3SummaryWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.QMDLLogCodeWrapper;
import com.inn.foresight.module.nv.workorder.model.WOFileDetail;

/**
 * The Class NVLayer3AggregationUtils.
 *
 * @author innoeye date - 02-May-2018 15:29:02
 */
public class NVLayer3AggregationUtils {

	private static final Logger logger = LogManager.getLogger(NVLayer3Utils.class);
	
	public static SortedMap<Long, QMDLLogCodeWrapper> aggregateDataByLatLong(TreeMap<Long, QMDLLogCodeWrapper> woParsedMap, Entry<String, List<WOFileDetail>> woFileDetailEntry,Layer3SummaryWrapper aggrigateWrapperData) {
		logger.info("Inside method aggregateDataByLatLong {}",woParsedMap.size());
		Boolean isInbuidling=getDriveType(woFileDetailEntry);
		if(isInbuidling){
			return processInbuildingAggregation(woParsedMap,aggrigateWrapperData);
		}else{
			return processDriveAggregation(woParsedMap,aggrigateWrapperData,woFileDetailEntry.getValue());
		}
	}

	private static SortedMap<Long, QMDLLogCodeWrapper> processInbuildingAggregation(
			TreeMap<Long, QMDLLogCodeWrapper> woParsedMap, Layer3SummaryWrapper aggrigateWrapperData) {
		TreeMap<Long, QMDLLogCodeWrapper> binnedDataMap = new TreeMap<>();

		Long timeStamp = null;
		Long firstxyPointNotNullTimeStamp = null;

		String testType = null;
		StringBuilder endtimeStamp = new StringBuilder();

		for (Map.Entry<Long, QMDLLogCodeWrapper> entry : woParsedMap.entrySet()) {
			QMDLLogCodeWrapper wrapper = entry.getValue();
			
			setMinMaxKPIValuesForInbuilding(wrapper, aggrigateWrapperData);
			
			if ((wrapper.getXpoint() == null || wrapper.getYpoint() == null) && timeStamp != null) {
				testType = entry.getValue().getTestType() == null ? testType : entry.getValue().getTestType();
				aggregateGridWiseData(binnedDataMap.get(timeStamp), entry.getValue(), testType, endtimeStamp, true,
						aggrigateWrapperData);

			} else {
				timeStamp = entry.getKey();
				binnedDataMap.put(timeStamp, entry.getValue());
				testType = entry.getValue().getTestType() == null ? testType : entry.getValue().getTestType();
			}

			if (firstxyPointNotNullTimeStamp == null && wrapper.getXpoint() != null && wrapper.getYpoint() != null) {
				firstxyPointNotNullTimeStamp = entry.getKey();
			}
		}

		if (binnedDataMap.size() > 1) {
			if (binnedDataMap.firstEntry().getValue().getXpoint() == null) {
				aggregateGridWiseData(binnedDataMap.get(firstxyPointNotNullTimeStamp),
						binnedDataMap.firstEntry().getValue(), testType, endtimeStamp, true, aggrigateWrapperData);
				binnedDataMap.remove(binnedDataMap.firstKey());
			}
		}

		SortedMap<Long, QMDLLogCodeWrapper> sortedBinnedDataMap = new TreeMap<>();
		for (SortedMap.Entry<Long, QMDLLogCodeWrapper> entry2 : binnedDataMap.entrySet()) {
			sortedBinnedDataMap.put(entry2.getValue().getTimeStamp(), entry2.getValue());
		}
		return sortedBinnedDataMap;
	}
	

	private static TreeMap<Long, QMDLLogCodeWrapper> processDriveAggregation(TreeMap<Long, QMDLLogCodeWrapper> woParsedMap,Layer3SummaryWrapper aggrigateWrapperData,List<WOFileDetail> woFileDetail) {
		TreeMap<Long, QMDLLogCodeWrapper> binnedDataMap = new TreeMap<>();
		Long timeStamp = null;
		LatLng grid = null;
		String testType=null;
		String technology=null;
		StringBuilder endtimeStamp=new StringBuilder();//For same grid time
		boolean isStationary=NVLayer3Utils.isStationaryRecord(woFileDetail.get(0));
		for(Map.Entry<Long, QMDLLogCodeWrapper> entry: woParsedMap.entrySet()) {
			QMDLLogCodeWrapper wrapper = entry.getValue();
			if(isStationary) {
				setAggregatedDataForStationary(wrapper, aggrigateWrapperData);
			}
			if(isValidRecord(grid,entry,testType)){		
				aggregateGridWiseData(binnedDataMap.get(timeStamp), wrapper,testType,endtimeStamp,false,aggrigateWrapperData);
			}else{
				grid=new LatLng(wrapper.getLat(), wrapper.getLon());
				timeStamp = entry.getKey();
				initiateRecordForAggrigation(binnedDataMap, timeStamp, endtimeStamp, entry);
			}
			testType=wrapper.getTestType()!=null?wrapper.getTestType():testType;
			technology=wrapper.getCoverage()!=null?wrapper.getCoverage():technology;
		}
		return binnedDataMap;
	}
	
	private static void setAggregatedDataForStationary(QMDLLogCodeWrapper wrapper,Layer3SummaryWrapper aggrigateWrapperData) {
		setMinKPIValuesForStationary(wrapper, aggrigateWrapperData);
		setMaxKPIValuesForStationary(wrapper, aggrigateWrapperData);
	}
	
	
	
	private static void setMinMaxKPIValuesForInbuilding(QMDLLogCodeWrapper wrapper,Layer3SummaryWrapper aggrigateWrapperData){
		aggrigateWrapperData.setMinLatencyBufferSize32Bytes(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getLatencyBufferSize32Bytes()),aggrigateWrapperData.getMinLatencyBufferSize32Bytes()));
		aggrigateWrapperData.setMaxLatencyBufferSize32Bytes(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getLatencyBufferSize32Bytes()),aggrigateWrapperData.getMaxLatencyBufferSize32Bytes()));

		aggrigateWrapperData.setMinLatencyBufferSize1000Bytes(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getLatencyBufferSize1000Bytes()),aggrigateWrapperData.getMinLatencyBufferSize1000Bytes()));
		aggrigateWrapperData.setMaxLatencyBufferSize1000Bytes(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getLatencyBufferSize1000Bytes()),aggrigateWrapperData.getMaxLatencyBufferSize1000Bytes()));

		aggrigateWrapperData.setMinLatencyBufferSize1500Bytes(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getLatencyBufferSize1500Bytes()),aggrigateWrapperData.getMinLatencyBufferSize1500Bytes()));
		aggrigateWrapperData.setMaxLatencyBufferSize1500Bytes(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getLatencyBufferSize1500Bytes()),aggrigateWrapperData.getMaxLatencyBufferSize1500Bytes()));

	}
	
private static void	setMinKPIValuesForStationary(QMDLLogCodeWrapper wrapper,Layer3SummaryWrapper aggrigateWrapperData){

  	aggrigateWrapperData.setMinRSRP(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getMeasureRSRPData()),aggrigateWrapperData.getMinRSRP()));
  	aggrigateWrapperData.setMinRsrq(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getMeasureRSRQData()),aggrigateWrapperData.getMinRsrq()));
  	aggrigateWrapperData.setMinRssi(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getRssi()),aggrigateWrapperData.getMinRssi()));
  	aggrigateWrapperData.setMinSINR(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getsINRData()),aggrigateWrapperData.getMinSINR()));
  	aggrigateWrapperData.setMinDl(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getDlThroughPut()),aggrigateWrapperData.getMinDl()));
  	aggrigateWrapperData.setMinFtpDl(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getFtpDl()),aggrigateWrapperData.getMinFtpDl()));
  	aggrigateWrapperData.setMinHttpDl(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getHttpDl()),aggrigateWrapperData.getMinHttpDl()));
  	aggrigateWrapperData.setMinUl(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getUlThroughPut()),aggrigateWrapperData.getMinUl()));
  	aggrigateWrapperData.setMinFtpUl(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getFtpUl()),aggrigateWrapperData.getMinFtpUl()));
  	aggrigateWrapperData.setMinHttpUl(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getHttpUl()),aggrigateWrapperData.getMinHttpUl()));
  	aggrigateWrapperData.setMinJitter(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getAvgJitter()),aggrigateWrapperData.getMinJitter()));
  	aggrigateWrapperData.setMinLatency(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getLatency()),aggrigateWrapperData.getMinLatency()));
 	aggrigateWrapperData.setMinPdschThroughput(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getPdschThroughput()),aggrigateWrapperData.getMinPdschThroughput()));
	aggrigateWrapperData.setMinPuschThroughput(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getPuschThroughput()),aggrigateWrapperData.getMinPuschThroughput()));
	
	}

private static void	setMaxKPIValuesForStationary(QMDLLogCodeWrapper wrapper,Layer3SummaryWrapper aggrigateWrapperData){
	aggrigateWrapperData.setMaxRSRP(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getMeasureRSRPData()),aggrigateWrapperData.getMaxRSRP()));
	aggrigateWrapperData.setMaxRsrq(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getMeasureRSRQData()),aggrigateWrapperData.getMaxRsrq()));
	aggrigateWrapperData.setMaxRssi(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getRssi()),aggrigateWrapperData.getMaxRssi()));
	aggrigateWrapperData.setMaxSINR(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getsINRData()),aggrigateWrapperData.getMaxSINR()));
	aggrigateWrapperData.setMaxDl(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getDlThroughPut()),aggrigateWrapperData.getMaxDl()));
	aggrigateWrapperData.setMaxFtpDl(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getFtpDl()),aggrigateWrapperData.getMaxFtpDl()));
	aggrigateWrapperData.setMaxHttpDl(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getHttpDl()),aggrigateWrapperData.getMaxHttpDl()));
	aggrigateWrapperData.setMaxUl(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getUlThroughPut()),aggrigateWrapperData.getMaxUl()));
	aggrigateWrapperData.setMaxFtpUl(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getFtpUl()),aggrigateWrapperData.getMaxFtpUl()));
	aggrigateWrapperData.setMaxHttpUl(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getHttpUl()),aggrigateWrapperData.getMaxHttpUl()));
	aggrigateWrapperData.setMaxJitter(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getAvgJitter()),aggrigateWrapperData.getMaxJitter()));
	aggrigateWrapperData.setMaxLatency(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getLatency()),aggrigateWrapperData.getMaxLatency()));
	aggrigateWrapperData.setMaxPdschThroughput(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getPdschThroughput()),aggrigateWrapperData.getMaxPdschThroughput()));
	aggrigateWrapperData.setMaxPuschThroughput(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getPuschThroughput()),aggrigateWrapperData.getMaxPuschThroughput()));
}
	
	

	private static void initiateRecordForAggrigation(TreeMap<Long, QMDLLogCodeWrapper> binnedDataMap, Long timeStamp,
			StringBuilder endtimeStamp, Map.Entry<Long, QMDLLogCodeWrapper> entry) {
		QMDLLogCodeWrapper record = entry.getValue();
		if(!endtimeStamp.toString().isEmpty()){
			record.setTimeStamp(Long.valueOf(endtimeStamp.toString()));
		}
		binnedDataMap.put(timeStamp,record);
		endtimeStamp.setLength(QMDLConstant.EMPTY_STRING_BUILDER_LENGTH);
		endtimeStamp.append(record.getEtimeStamp());
	}
	
	private static boolean isValidRecord(LatLng grid, Entry<Long, QMDLLogCodeWrapper> entry, String testType) {
		QMDLLogCodeWrapper wrapper = entry.getValue();
		if (grid != null) {
			double distance = MensurationUtils.distanceBetweenPoints(grid,
					new LatLng(wrapper.getLat(), wrapper.getLon()));
			return distance < Integer.parseInt(ConfigUtils.getString(NVLayer3Constants.LAYER3_GRID_SIZE))
					&& isValidTestType(testType, entry);
		} else {
			return false;
		}
	}

	private static Boolean getDriveType(Entry<String, List<WOFileDetail>> woFileDetailEntry) {
		if(!woFileDetailEntry.getValue().isEmpty()){
			WOFileDetail woFileDetail = woFileDetailEntry.getValue().get(QMDLConstant.FIRST_RECORD_INDEX);
			return NVLayer3Utils.isInBuidlingRecord(woFileDetail);
		}
		return false;
	}

	private static boolean isValidTestType(String testType, Entry<Long, QMDLLogCodeWrapper> entry) {
		String testTypeTemp = entry.getValue().getTestType()==null ? testType: entry.getValue().getTestType();
		return (testTypeTemp == null || testType==null || (testType.equals(testTypeTemp)));
		}

	
	private static void aggregateGridWiseData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord, String testType, StringBuilder endtimeStamp, Boolean isInbuidling, Layer3SummaryWrapper aggrigateWrapperData) {
		if(!isInbuidling) {
			setStartAndEndTimeIntoWrapper(oldRecord,newRecord,endtimeStamp);
		}
//		aggregateHandoverdata(oldRecord, newRecord);
		aggregateCallData(oldRecord, newRecord);		
		aggregateDlUlData(oldRecord, newRecord);
		aggregateNetworkData(oldRecord, newRecord);
		aggregateMosData(oldRecord, newRecord);
		aggregateSignalData(oldRecord, newRecord);
		aggregateWebPerformanceData(oldRecord, newRecord);
		aggregateLocationData(oldRecord, newRecord);
		aggregateErabData(oldRecord, newRecord);
		aggregateCellData(oldRecord, newRecord);
		aggregateQMDLKpiData(oldRecord, newRecord);
		aggregateBRTIParameter(oldRecord, newRecord);
		aggregateNeighbourData(oldRecord, newRecord);
		aggregateSMSData(oldRecord, newRecord);
		aggregate3GData(oldRecord, newRecord);
		aggregateTAUData(oldRecord, newRecord);
		aggregate2GData(oldRecord, newRecord);
		aggregateInstantaneousMosData(oldRecord,newRecord);
		//aggregateCSFBCallData(oldRecord, newRecord);
		aggregatePDSCHThroughput(oldRecord, newRecord);		
		aggregateSpeedTestParametersForInbuilding(oldRecord, newRecord);
		aggregatePUSCHThroughput(oldRecord, newRecord);
		aggrgateRRCConnectionState(oldRecord,newRecord);
		aggregateLinkAdaptationKPI(oldRecord,newRecord);
		aggregateLteDominantChannelKPI(oldRecord, newRecord);
		aggregateMCSInfo(oldRecord, newRecord);
		aggregateSchedulingGrantInfo(oldRecord, newRecord);
		aggregateMACThroughput(oldRecord, newRecord);
		aggregateKPICount(oldRecord, newRecord);
		aggregateRACHInfo(oldRecord, newRecord);
		aggregateLTECellDLULCPType(oldRecord, newRecord);
		aggregateSignallingRRCStates(oldRecord,newRecord,aggrigateWrapperData);		
		aggregateLatencyBufferSizeData(oldRecord, newRecord);
		aggrigateLTERegistrationEvent(oldRecord, newRecord);
		aggregateDlUlModulationTypeAndDlPRB(oldRecord, newRecord);
		aggregateRrcConnectionReestablishmentRequestCompleteReject(oldRecord, newRecord);		
		aggregateVoltePaginandCallEvents(oldRecord, newRecord);	
		aggregateNewHandoverdata(oldRecord, newRecord);
		aggregateThroughputData(oldRecord, newRecord);
		aggregateJitterAndPacketLossData(oldRecord,newRecord);
		aggregatePRBData(oldRecord, newRecord);
		aggregatePMosData(oldRecord, newRecord);		
		aggregateHITimeOfQci1(oldRecord, newRecord);	     
		setMNCMCCintoAggrigatedWrapper(newRecord, aggrigateWrapperData);	
		aggregatePDCPAndRLCThroughput(oldRecord, newRecord);
		aggregateNumOfRtpPktsLostAndTotalPktCnt(oldRecord, newRecord);
		aggregateHostValue(oldRecord, newRecord);
		
		oldRecord.setTestType(testType);
		//oldRecord.setEtimeStamp(NVLayer3Utils.getLatestValue(oldRecord.getEtimeStamp(), newRecord.getEtimeStamp()));

	}

	public static void aggregateHostValue(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setHost(NVLayer3Utils.getLatestValue(oldRecord.getHost(), newRecord.getHost()));
	}
	
	private static void aggregateNumOfRtpPktsLostAndTotalPktCnt(QMDLLogCodeWrapper oldRecord,
			QMDLLogCodeWrapper newRecord) {
		oldRecord.setNumberOfRtpPacketsLost(NVLayer3Utils.addDoubleValue(oldRecord.getNumberOfRtpPacketsLost(), newRecord.getNumberOfRtpPacketsLost()));
		oldRecord.setTotalPacketCount(NVLayer3Utils.addDoubleValue(oldRecord.getTotalPacketCount(), newRecord.getTotalPacketCount()));
	}

	private static void aggregatePDCPAndRLCThroughput(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setPdcpThroughput(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getPdcpThroughput(), newRecord.getPdcpThroughput()));
		oldRecord.setRlcThroughput(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getRlcThroughput(), newRecord.getRlcThroughput()));

	}

	public static void setMNCMCCintoAggrigatedWrapper(QMDLLogCodeWrapper newRecord,
			Layer3SummaryWrapper aggrigateWrapperData) {
		if(aggrigateWrapperData.getMNC()==null || aggrigateWrapperData.getMCC()==null) {
			aggrigateWrapperData.setMNC(NVLayer3Utils.getLatestValue(aggrigateWrapperData.getMNC(), newRecord.getMnc()));
			aggrigateWrapperData.setMCC(NVLayer3Utils.getLatestValue(aggrigateWrapperData.getMCC(), newRecord.getMcc()));
		}
	}

	public static void aggregateHITimeOfQci1(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setHandoverInterruptionTimeOfQCI1(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getHandoverInterruptionTimeOfQCI1(),newRecord.getHandoverInterruptionTimeOfQCI1()));
		oldRecord.setHandoverInterruptionTimeOfQCI9DL(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getHandoverInterruptionTimeOfQCI9DL(),newRecord.getHandoverInterruptionTimeOfQCI9DL()));
		oldRecord.setHandoverInterruptionTimeOfQCI9UL(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getHandoverInterruptionTimeOfQCI9UL(),newRecord.getHandoverInterruptionTimeOfQCI9UL()));
		
	}

	private static void aggregatePMosData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setpMos(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getpMos(),newRecord.getpMos()));
	}

	private static void aggregatePRBData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setDlPRBUtilization(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getDlPRBUtilization(),newRecord.getDlPRBUtilization()));
		oldRecord.setUlPRBUtilization(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getUlPRBUtilization(),newRecord.getUlPRBUtilization()));
	}

	private static void aggregateJitterAndPacketLossData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setJitter(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getJitter(),newRecord.getJitter()));
		oldRecord.setPacketLossValueAndCount(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getPacketLossValueAndCount(),newRecord.getPacketLossValueAndCount()));
	}

	private static void aggregateThroughputData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setMacDlThroughput(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getMacDlThroughput(), newRecord.getMacDlThroughput()));
	}

	public static void aggregateVoltePaginandCallEvents(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setVoltePagingAttempts(NVLayer3Utils.addIntegerValue(oldRecord.getVoltePagingAttempts(), newRecord.getVoltePagingAttempts()));		
		oldRecord.setVoltePagingSuccess(NVLayer3Utils.addIntegerValue(oldRecord.getVoltePagingSuccess(), newRecord.getVoltePagingSuccess()));

		oldRecord.setVolteMTCallAttempts(NVLayer3Utils.addIntegerValue(oldRecord.getVolteMTCallAttempts(), newRecord.getVolteMTCallAttempts()));		
		oldRecord.setVolteMTCallSuccess(NVLayer3Utils.addIntegerValue(oldRecord.getVolteMTCallSuccess(), newRecord.getVolteMTCallSuccess()));		
		oldRecord.setVolteMTCallDrop(NVLayer3Utils.addIntegerValue(oldRecord.getVolteMTCallDrop(), newRecord.getVolteMTCallDrop()));		
		oldRecord.setVolteMTCallFailure(NVLayer3Utils.addIntegerValue(oldRecord.getVolteMTCallFailure(), newRecord.getVolteMTCallFailure()));		
		oldRecord.setVolteMTCallSetupSuccess(NVLayer3Utils.addIntegerValue(oldRecord.getVolteMTCallSetupSuccess(), newRecord.getVolteMTCallSetupSuccess()));		
		oldRecord.setVolteMTCallSetup(NVLayer3Utils.addIntegerValue(oldRecord.getVolteMTCallSetup(), newRecord.getVolteMTCallSetup()));		

		
		oldRecord.setVolteMOCallAttempts(NVLayer3Utils.addIntegerValue(oldRecord.getVolteMOCallAttempts(), newRecord.getVolteMOCallAttempts()));		
		oldRecord.setVolteMOCallSuccess(NVLayer3Utils.addIntegerValue(oldRecord.getVolteMOCallSuccess(), newRecord.getVolteMOCallSuccess()));
		oldRecord.setVolteMOCallDrop(NVLayer3Utils.addIntegerValue(oldRecord.getVolteMOCallDrop(), newRecord.getVolteMOCallDrop()));		
		oldRecord.setVolteMOCallFailure(NVLayer3Utils.addIntegerValue(oldRecord.getVolteMOCallFailure(), newRecord.getVolteMOCallFailure()));		
		oldRecord.setVolteMOCallSetupSuccess(NVLayer3Utils.addIntegerValue(oldRecord.getVolteMOCallSetupSuccess(), newRecord.getVolteMOCallSetupSuccess()));		
		oldRecord.setVolteMOCallSetup(NVLayer3Utils.addIntegerValue(oldRecord.getVolteMOCallSetup(), newRecord.getVolteMOCallSetup()));		

	}
	
	private static void aggregateRrcConnectionReestablishmentRequestCompleteReject(QMDLLogCodeWrapper oldRecord,
			QMDLLogCodeWrapper newRecord) {
		oldRecord.setRrcConnectionReestablishmentRequest(NVLayer3Utils.addIntegerValue(oldRecord.getRrcConnectionReestablishmentRequest(), newRecord.getRrcConnectionReestablishmentRequest()));
		oldRecord.setRrcConnectionReestablishmentComplete(NVLayer3Utils.addIntegerValue(oldRecord.getRrcConnectionReestablishmentComplete(), newRecord.getRrcConnectionReestablishmentComplete()));
		oldRecord.setRrcConnectionReestablishmentReject(NVLayer3Utils.addIntegerValue(oldRecord.getRrcConnectionReestablishmentReject(), newRecord.getRrcConnectionReestablishmentReject()));
	}

	private static void aggregateDlUlModulationTypeAndDlPRB(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setDlModulationType(NVLayer3Utils.getLatestValue(oldRecord.getDlModulationType(), newRecord.getDlModulationType()));
		oldRecord.setUlModulationType(NVLayer3Utils.getLatestValue(oldRecord.getUlModulationType(), newRecord.getUlModulationType()));
		oldRecord.setB173DlPRB(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getB173DlPRB(),newRecord.getB173DlPRB()));
	}
	
	public static void aggregateLatencyBufferSizeData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setPingBufferSize(NVLayer3Utils.getLatestValue(oldRecord.getPingBufferSize(), newRecord.getPingBufferSize()));		
		oldRecord.setLatencyBufferSize32Bytes(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getLatencyBufferSize32Bytes(), newRecord.getLatencyBufferSize32Bytes()));
		oldRecord.setLatencyBufferSize1500Bytes(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getLatencyBufferSize1500Bytes(), newRecord.getLatencyBufferSize1500Bytes()));
		oldRecord.setLatencyBufferSize1000Bytes(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getLatencyBufferSize1000Bytes(), newRecord.getLatencyBufferSize1000Bytes()));
	}

	private static void aggregateLteEvents(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		aggregateVOLTEEvents(oldRecord, newRecord);				
		aggrigateLTERegistrationEvent(oldRecord, newRecord);
		aggrigateLTERandomAccessEvent(oldRecord, newRecord);
		aggregateLTECallEvents(oldRecord,newRecord);
		aggregateLTEHandoverEvents(oldRecord,newRecord);
	}
	
	private static void aggregateLTEHandoverEvents(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setRrcEutra(NVLayer3Utils.addIntegerValue(oldRecord.getRrcEutra(), newRecord.getRrcEutra()));
		oldRecord.setRrcGeran(NVLayer3Utils.addIntegerValue(oldRecord.getRrcGeran(), newRecord.getRrcGeran()));
		oldRecord.setRrcUtraFdd(NVLayer3Utils.addIntegerValue(oldRecord.getRrcUtraFdd(), newRecord.getRrcUtraFdd()));
		oldRecord.setRrcUtraTdd(NVLayer3Utils.addIntegerValue(oldRecord.getRrcUtraTdd(), newRecord.getRrcUtraTdd()));
		oldRecord.setLteUmtsRequest(NVLayer3Utils.addIntegerValue(oldRecord.getLteUmtsRequest(), newRecord.getLteUmtsRequest()));
		oldRecord.setLteUmtsSuccess(NVLayer3Utils.addIntegerValue(oldRecord.getLteUmtsSuccess(), newRecord.getLteUmtsSuccess()));
		oldRecord.setUmtsLteRequest(NVLayer3Utils.addIntegerValue(oldRecord.getUmtsLteRequest(), newRecord.getUmtsLteRequest()));
		oldRecord.setUmtsLteSuccess(NVLayer3Utils.addIntegerValue(oldRecord.getUmtsLteSuccess(), newRecord.getUmtsLteSuccess()));
		oldRecord.setMoCsfbRequest(NVLayer3Utils.addIntegerValue(oldRecord.getMoCsfbRequest(), newRecord.getMoCsfbRequest()));
		oldRecord.setMtCsfbRequest(NVLayer3Utils.addIntegerValue(oldRecord.getMtCsfbRequest(), newRecord.getMtCsfbRequest()));
		oldRecord.setMoCsfbSuccess(NVLayer3Utils.addIntegerValue(oldRecord.getMoCsfbSuccess(), newRecord.getMoCsfbSuccess()));
		oldRecord.setMtCsfbSuccess(NVLayer3Utils.addIntegerValue(oldRecord.getMtCsfbSuccess(), newRecord.getMtCsfbSuccess()));
		oldRecord.setRrcReconfigCompleteMissing(NVLayer3Utils.addIntegerValue(oldRecord.getRrcReconfigCompleteMissing(), newRecord.getRrcReconfigCompleteMissing()));
		oldRecord.setRadioLinkFailure(NVLayer3Utils.addIntegerValue(oldRecord.getRadioLinkFailure(), newRecord.getRadioLinkFailure()));
		oldRecord.setRrcConnReconfFailure(NVLayer3Utils.addIntegerValue(oldRecord.getRrcConnReconfFailure(), newRecord.getRrcConnReconfFailure()));
		oldRecord.setMoCsfbFailure(NVLayer3Utils.addIntegerValue(oldRecord.getMoCsfbFailure(), newRecord.getMoCsfbFailure()));
		oldRecord.setMtCsfbFailure(NVLayer3Utils.addIntegerValue(oldRecord.getMtCsfbFailure(), newRecord.getMtCsfbFailure()));
		oldRecord.setReturnCSCallEnd(NVLayer3Utils.addIntegerValue(oldRecord.getReturnCSCallEnd(), newRecord.getReturnCSCallEnd()));
	}
	
	private static void aggregateLTECallEvents(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setPagingMessageCount(NVLayer3Utils.addIntegerValue(oldRecord.getPagingMessageCount(), newRecord.getPagingMessageCount()));
		oldRecord.setServiceReject(NVLayer3Utils.addIntegerValue(oldRecord.getServiceReject(), newRecord.getServiceReject()));
		oldRecord.setServiceRequest(NVLayer3Utils.addIntegerValue(oldRecord.getServiceRequest(), newRecord.getServiceRequest()));
		oldRecord.setRrcConnectionSetup(NVLayer3Utils.addIntegerValue(oldRecord.getRrcConnectionSetup(), newRecord.getRrcConnectionSetup()));
		oldRecord.setSecurityModeComplete(NVLayer3Utils.addIntegerValue(oldRecord.getSecurityModeComplete(), newRecord.getSecurityModeComplete()));
		oldRecord.setAuthenticationResponse(NVLayer3Utils.addIntegerValue(oldRecord.getAuthenticationResponse(), newRecord.getAuthenticationResponse()));
		oldRecord.setAuthenticationReject(NVLayer3Utils.addIntegerValue(oldRecord.getAuthenticationReject(), newRecord.getAuthenticationReject()));
		oldRecord.setAuthenticationFailure(NVLayer3Utils.addIntegerValue(oldRecord.getAuthenticationFailure(), newRecord.getAuthenticationFailure()));
		oldRecord.setServiceFailure(NVLayer3Utils.addIntegerValue(oldRecord.getServiceFailure(), newRecord.getServiceFailure()));
		oldRecord.setMoSignalling(NVLayer3Utils.addIntegerValue(oldRecord.getMoSignalling(), newRecord.getMoSignalling()));
		oldRecord.setMtAccess(NVLayer3Utils.addIntegerValue(oldRecord.getMtAccess(), newRecord.getMtAccess()));
		oldRecord.setRrcSetupFailure(NVLayer3Utils.addIntegerValue(oldRecord.getRrcSetupFailure(), newRecord.getRrcSetupFailure()));
		oldRecord.setRrcConnReconfiguration(NVLayer3Utils.addIntegerValue(oldRecord.getRrcConnReconfiguration(), newRecord.getRrcConnReconfiguration()));
		oldRecord.setReestablishmentFailure(NVLayer3Utils.addIntegerValue(oldRecord.getReestablishmentFailure(), newRecord.getReestablishmentFailure()));
		oldRecord.setRrcConnReconfSuccess(NVLayer3Utils.addIntegerValue(oldRecord.getRrcConnReconfSuccess(), newRecord.getRrcConnReconfSuccess()));
		oldRecord.setErabSetup(NVLayer3Utils.addIntegerValue(oldRecord.getErabSetup(), newRecord.getErabSetup()));
		oldRecord.setErabSetupSuccess(NVLayer3Utils.addIntegerValue(oldRecord.getErabSetupSuccess(), newRecord.getErabSetupSuccess()));
		oldRecord.setRrcConnectionDropped(NVLayer3Utils.addIntegerValue(oldRecord.getRrcConnectionDropped(), newRecord.getRrcConnectionDropped()));
		oldRecord.setServiceRequestSuccess(NVLayer3Utils.addIntegerValue(oldRecord.getServiceRequestSuccess(), newRecord.getServiceRequestSuccess()));
	}

	public static void aggrigateLTERandomAccessEvent(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setLteRACHPreambleSuccess(NVLayer3Utils.addIntegerValue(oldRecord.getLteRACHPreambleSuccess(), newRecord.getLteRACHPreambleSuccess()));
		oldRecord.setLteRACHProcedureSuccess(NVLayer3Utils.addIntegerValue(oldRecord.getLteRACHProcedureSuccess(), newRecord.getLteRACHProcedureSuccess()));
		oldRecord.setLteRACHPreambleFailure(NVLayer3Utils.addIntegerValue(oldRecord.getLteRACHPreambleFailure(), newRecord.getLteRACHPreambleFailure()));
		oldRecord.setLteRACHProcedureFailure(NVLayer3Utils.addIntegerValue(oldRecord.getLteRACHProcedureFailure(), newRecord.getLteRACHProcedureFailure()));
	}

	public static void aggrigateLTERegistrationEvent(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setAttachComplete(NVLayer3Utils.addIntegerValue(oldRecord.getAttachComplete(), newRecord.getAttachComplete()));		
		oldRecord.setDetachRequest(NVLayer3Utils.addIntegerValue(oldRecord.getDetachRequest(), newRecord.getDetachRequest()));
		oldRecord.setLteEMMRegisteredEvent(NVLayer3Utils.addIntegerValue(oldRecord.getLteEMMRegisteredEvent(), newRecord.getLteEMMRegisteredEvent()));		
		oldRecord.setLtePDNConnectionRequest(NVLayer3Utils.addIntegerValue(oldRecord.getLtePDNConnectionRequest(), newRecord.getLtePDNConnectionRequest()));
	}

	public static void aggregateVOLTEEvents(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setVoiceBearerActivationRequest(NVLayer3Utils.addIntegerValue(oldRecord.getVoiceBearerActivationRequest(), newRecord.getVoiceBearerActivationRequest()));
		oldRecord.setVoiceBearerActivationSuccess(NVLayer3Utils.addIntegerValue(oldRecord.getVoiceBearerActivationSuccess(), newRecord.getVoiceBearerActivationSuccess()));
		oldRecord.setVoiceBearerActivationFailure(NVLayer3Utils.addIntegerValue(oldRecord.getVoiceBearerActivationFailure(), newRecord.getVoiceBearerActivationFailure()));
		oldRecord.setVoiceBearerDeactivationRequest(NVLayer3Utils.addIntegerValue(oldRecord.getVoiceBearerDeactivationRequest(), newRecord.getVoiceBearerDeactivationRequest()));
		oldRecord.setInitialIMSRegistrationSuccess(NVLayer3Utils.addIntegerValue(oldRecord.getInitialIMSRegistrationSuccess(), newRecord.getInitialIMSRegistrationSuccess()));
		oldRecord.setInitialIMSRegistrationFailure(NVLayer3Utils.addIntegerValue(oldRecord.getInitialIMSRegistrationFailure(), newRecord.getInitialIMSRegistrationFailure()));
	}
	
	private static void aggregateSignallingRRCStates(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord, Layer3SummaryWrapper aggrigateWrapperData) {
		oldRecord.setEmmState(NVLayer3Utils.getLatestValue(oldRecord.getEmmState(), newRecord.getEmmState()));
		oldRecord.setEmmSubState(NVLayer3Utils.getLatestValue(oldRecord.getEmmSubState(), newRecord.getEmmSubState()));
		oldRecord.setRrcState(NVLayer3Utils.getLatestValue(oldRecord.getRrcState(), newRecord.getRrcState()));
		oldRecord.setReselectionSuccess(NVLayer3Utils.addIntegerValue(oldRecord.getReselectionSuccess(),newRecord.getReselectionSuccess()));

		if(aggrigateWrapperData.getRrcStateFirstValue()==null && newRecord.getRrcState()!=null) {
			aggrigateWrapperData.setRrcStateFirstValue(newRecord.getRrcState());

		}
	}

	private static void aggregateRACHInfo(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		aggregatePreambleCountMax(oldRecord, newRecord);
		aggregatePreambleInitialTxPower(oldRecord, newRecord);
		aggregateRaRNTI(oldRecord, newRecord);
		aggregateRaTimingAdvance(oldRecord, newRecord);
	}
	
	private static void aggregateKPICount(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setMessage3Count(NVLayer3Utils.addIntegerValue(oldRecord.getMessage3Count(), newRecord.getMessage3Count()));
		oldRecord.setMessage1Count(NVLayer3Utils.addIntegerValue(oldRecord.getMessage1Count(), newRecord.getMessage1Count()));
		oldRecord.setB0C0Count(NVLayer3Utils.addIntegerValue(oldRecord.getB0C0Count(), newRecord.getB0C0Count()));
		oldRecord.setAttachAccept(NVLayer3Utils.addIntegerValue(oldRecord.getAttachAccept(), newRecord.getAttachAccept()));
		oldRecord.setAttachRequest(NVLayer3Utils.addIntegerValue(oldRecord.getAttachRequest(), newRecord.getAttachRequest()));
	}

	private static void aggregateLTECellDLULCPType(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setDlCp(NVLayer3Utils.getLatestValue(oldRecord.getDlCp(), newRecord.getDlCp()));
		oldRecord.setUlCp(NVLayer3Utils.getLatestValue(oldRecord.getUlCp(), newRecord.getUlCp()));
	}

	private static void aggregateRaTimingAdvance(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setRaTimingAdvance(NVLayer3Utils.getLatestValue(oldRecord.getRaTimingAdvance(), newRecord.getRaTimingAdvance()));
	}

	private static void aggregateRaRNTI(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setRaRNTI(NVLayer3Utils.getLatestValue(oldRecord.getRaRNTI(), newRecord.getRaRNTI()));
	}

	private static void aggregatePreambleInitialTxPower(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setPreambleInitialTxPower(NVLayer3Utils.getLatestValue(oldRecord.getPreambleInitialTxPower(), newRecord.getPreambleInitialTxPower()));
	}

	private static void aggregatePreambleCountMax(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setPreambleCountMax(NVLayer3Utils.getLatestValue(oldRecord.getPreambleCountMax(), newRecord.getPreambleCountMax()));
	}

	private static void aggregateUETxPower(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setUeTxPower(NVLayer3Utils.getLatestValue(oldRecord.getUeTxPower(), newRecord.getUeTxPower()));		
	}

	private static void aggregateMACThroughput(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setMacUlThroughput(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getMacUlThroughput(),newRecord.getMacUlThroughput()));
	}

	private static void aggregateSchedulingGrantInfo(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setAvgDLTb0Size(NVLayer3Utils.getLatestValue(oldRecord.getAvgDLTb0Size(), newRecord.getAvgDLTb0Size()));
		oldRecord.setAvgDLTb1Size(NVLayer3Utils.getLatestValue(oldRecord.getAvgDLTb1Size(), newRecord.getAvgDLTb1Size()));
		oldRecord.setAvgDLTbSize(NVLayer3Utils.getLatestValue(oldRecord.getAvgDLTbSize(), newRecord.getAvgDLTbSize()));
		oldRecord.setAvgULTBSize(NVLayer3Utils.getLatestValue(oldRecord.getAvgULTBSize(), newRecord.getAvgULTBSize()));
		oldRecord.setAvgULPRB(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getAvgULPRB(), newRecord.getAvgULPRB()));
	}

	private static void aggregateMCSInfo(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setTb0McsIndex(NVLayer3Utils.getLatestValue(oldRecord.getTb0McsIndex(), newRecord.getTb0McsIndex()));
		oldRecord.setTb1McsIndex(NVLayer3Utils.getLatestValue(oldRecord.getTb1McsIndex(), newRecord.getTb1McsIndex()));
		oldRecord.setTb0ModulationType(NVLayer3Utils.getLatestValue(oldRecord.getTb0ModulationType(), newRecord.getTb0ModulationType()));
		oldRecord.setTb1ModulationType(NVLayer3Utils.getLatestValue(oldRecord.getTb1ModulationType(), newRecord.getTb1ModulationType()));
	}

	private static void aggregateLteDominantChannelKPI(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setDominantChannelRSSI(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getDominantChannelRSSI(), newRecord.getDominantChannelRSSI()));
		oldRecord.setDominantChannelRSSIRx0(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getDominantChannelRSSIRx0(), newRecord.getDominantChannelRSSIRx0()));
		oldRecord.setDominantChannelRSSIRx1(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getDominantChannelRSSIRx1(), newRecord.getDominantChannelRSSIRx1()));
		oldRecord.setDominantChannelRSRQ(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getDominantChannelRSRQ(), newRecord.getDominantChannelRSRQ()));
		oldRecord.setDominantChannelRSRQRx0(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getDominantChannelRSRQRx0(), newRecord.getDominantChannelRSRQRx0()));
		oldRecord.setDominantChannelRSRQRx1(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getDominantChannelRSRQRx1(), newRecord.getDominantChannelRSRQRx1()));
		oldRecord.setDominantChannelRSRP(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getDominantChannelRSRP(), newRecord.getDominantChannelRSRP()));
		oldRecord.setDominantChannelRSRPRx0(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getDominantChannelRSRPRx0(), newRecord.getDominantChannelRSRPRx0()));
		oldRecord.setDominantChannelRSRPRx1(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getDominantChannelRSRPRx1(), newRecord.getDominantChannelRSRPRx1()));
		oldRecord.setDominantChannelPCI(NVLayer3Utils.getLatestValue(oldRecord.getDominantChannelPCI(), newRecord.getDominantChannelPCI()));
		oldRecord.setLteDominantFrequency(NVLayer3Utils.getLatestValue(oldRecord.getLteDominantFrequency(), newRecord.getLteDominantFrequency()));
		
		oldRecord.setPowerHeadroomdata(NVLayer3Utils.getLatestValue(oldRecord.getPowerHeadroomdata(), newRecord.getPowerHeadroomdata()));
		oldRecord.setPrachTxPower(NVLayer3Utils.getLatestValue(oldRecord.getPrachTxPower(), newRecord.getPrachTxPower()));
		
		aggregateUETxPower(oldRecord, newRecord);
	}

	private static void aggregatePUSCHThroughput(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setPuschThroughput(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getPuschThroughput(),newRecord.getPuschThroughput()));
		oldRecord.setPuschMcsIndex(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getPuschMcsIndex(), newRecord.getPuschMcsIndex()));
		oldRecord.setPuschModulationType(NVLayer3Utils.getLatestValue(oldRecord.getPuschModulationType(), newRecord.getPuschModulationType()));
	}

	private static void aggregateLinkAdaptationKPI(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setCqiCw1(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getCqiCw1(), newRecord.getCqiCw1()));
		oldRecord.setDlPrb(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getDlPrb(), newRecord.getDlPrb()));
		oldRecord.setPdschCwoMcs(NVLayer3Utils.getLatestValue(oldRecord.getPdschCwoMcs(), newRecord.getPdschCwoMcs()));
		oldRecord.setPdschCwoModulation(NVLayer3Utils.getLatestValue(oldRecord.getPdschCwoModulation(), newRecord.getPdschCwoModulation()));
		oldRecord.setTransmissionMode(NVLayer3Utils.getLatestValue(oldRecord.getTransmissionMode(), newRecord.getTransmissionMode()));
		oldRecord.setPdschnumRb(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getPdschnumRb(), newRecord.getPdschnumRb()));
		oldRecord.setHandoverInterruption(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getHandoverInterruption(), newRecord.getHandoverInterruption()));
	}

	private static void aggrgateRRCConnectionState(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setRrcConnectionComplete(NVLayer3Utils.addIntegerValue(oldRecord.getRrcConnectionComplete(), newRecord.getRrcConnectionComplete()));
		oldRecord.setRrcConnectionSetupOk(NVLayer3Utils.addIntegerValue(oldRecord.getRrcConnectionSetupOk(), newRecord.getRrcConnectionSetupOk()));
		oldRecord.setRrcConnectionAttempt(NVLayer3Utils.addIntegerValue(oldRecord.getRrcConnectionAttempt(), newRecord.getRrcConnectionAttempt()));
		oldRecord.setRrcConnectionRelease(NVLayer3Utils.addIntegerValue(oldRecord.getRrcConnectionRelease(), newRecord.getRrcConnectionRelease()));
		oldRecord.setRrcConnectionFailed(NVLayer3Utils.addIntegerValue(oldRecord.getRrcConnectionFailed(), newRecord.getRrcConnectionFailed()));
		oldRecord.setRrcConnectionSetupTime(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getRrcConnectionSetupTime(), newRecord.getRrcConnectionSetupTime()));
		oldRecord.setRrcReestablishmentSuccess(NVLayer3Utils.addIntegerValue(oldRecord.getRrcReestablishmentSuccess(), newRecord.getRrcReestablishmentSuccess()));
		oldRecord.setRrcReestablishmentFailed(NVLayer3Utils.addIntegerValue(oldRecord.getRrcReestablishmentFailed(), newRecord.getRrcReestablishmentFailed()));
	}

	public static void aggregateSpeedTestParametersForInbuilding(QMDLLogCodeWrapper oldRecord,
			QMDLLogCodeWrapper newRecord) {
		oldRecord.setSpeedTestPinNumber(NVLayer3Utils.addIntegerValue(oldRecord.getSpeedTestPinNumber(),newRecord.getSpeedTestPinNumber()));
		oldRecord.setSpeedTestDlRate(NVLayer3Utils.getLatestValue(oldRecord.getSpeedTestDlRate(), newRecord.getSpeedTestDlRate()));
		oldRecord.setSpeedTestUlRate(NVLayer3Utils.getLatestValue(oldRecord.getSpeedTestUlRate(), newRecord.getSpeedTestUlRate()));		
		
		oldRecord.setDownloadTimeGoogle(NVLayer3Utils.getLatestValue(oldRecord.getDownloadTimeGoogle(), newRecord.getDownloadTimeGoogle()));
		oldRecord.setDownloadTimeYoutube(NVLayer3Utils.getLatestValue(oldRecord.getDownloadTimeYoutube(), newRecord.getDownloadTimeYoutube()));
		oldRecord.setDownloadTimeFacebook(NVLayer3Utils.getLatestValue(oldRecord.getDownloadTimeFacebook(), newRecord.getDownloadTimeFacebook()));
	}

	private static void aggregateCSFBCallData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
	  oldRecord.setCsfbCallAttempt(NVLayer3Utils.addIntegerValue(oldRecord.getCsfbCallAttempt() , newRecord.getCsfbCallAttempt()));
      oldRecord.setCsfbCallDrop(NVLayer3Utils.addIntegerValue(oldRecord.getCsfbCallDrop() , newRecord.getCsfbCallDrop()));
      oldRecord.setCsfbCallFailed(NVLayer3Utils.addIntegerValue(oldRecord.getCsfbCallFailed() , newRecord.getCsfbCallFailed()));
      oldRecord.setCsfbCallSetupFailed(NVLayer3Utils.addIntegerValue(oldRecord.getCsfbCallSetupFailed() , newRecord.getCsfbCallSetupFailed()));
      oldRecord.setCsfbCallSetupSuccess(NVLayer3Utils.addIntegerValue(oldRecord.getCsfbCallSetupSuccess() , newRecord.getCsfbCallSetupSuccess()));
      oldRecord.setCsfbCallSuccess(NVLayer3Utils.addIntegerValue(oldRecord.getCsfbCallSuccess() , newRecord.getCsfbCallSuccess()));
	}

	private static void aggregateInstantaneousMosData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		
		oldRecord.setInstantaneousMos(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getInstantaneousMos(), newRecord.getInstantaneousMos()));
	}

	private static void aggregate2GData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setRxLev(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getRxLev(),newRecord.getRxLev()));
		oldRecord.setRxQual(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getRxQual(),newRecord.getRxQual()));
		
		oldRecord.setbSIC(NVLayer3Utils.getLatestValue(oldRecord.getbSIC(),newRecord.getbSIC()));
		oldRecord.setbCCHChannel(NVLayer3Utils.getLatestValue(oldRecord.getbCCHChannel(),newRecord.getbCCHChannel()));		
	}

	private static void aggregate3GData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setRscp(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getRscp(),newRecord.getRscp()));
		oldRecord.setEcio(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getEcio(),newRecord.getEcio()));
		oldRecord.setRssi(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getRssi(),newRecord.getRssi()));
	}


	private static void aggregateTAUData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setTauAttempt(NVLayer3Utils.addIntegerValue(oldRecord.getTauAttempt(), newRecord.getTauAttempt()));
		oldRecord.setTauComplete(NVLayer3Utils.addIntegerValue(oldRecord.getTauComplete(), newRecord.getTauComplete()));
		oldRecord.setTauFailure(NVLayer3Utils.addIntegerValue(oldRecord.getTauFailure(), newRecord.getTauFailure()));	
		oldRecord.setTauTime(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getTauTime(), newRecord.getTauTime()));
	}

	private static void aggregateSMSData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setSmsAttempt(NVLayer3Utils.addIntegerValue(oldRecord.getSmsAttempt(), newRecord.getSmsAttempt()));
		oldRecord.setSmsFailure(NVLayer3Utils.addIntegerValue(oldRecord.getSmsFailure(), newRecord.getSmsFailure()));
		oldRecord.setSmsSucess(NVLayer3Utils.addIntegerValue(oldRecord.getSmsSucess(), newRecord.getSmsSucess()));
			
	}

	private static void aggregateNeighbourData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		if(newRecord.getNeighbourDataList()!=null && !newRecord.getNeighbourDataList().isEmpty() ){
			oldRecord.setNeighbourDataList(newRecord.getNeighbourDataList());
		}
	}

	private static void aggregateBRTIParameter(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setHttpAttempt(NVLayer3Utils.addIntegerValue(oldRecord.getHttpAttempt(), newRecord.getHttpAttempt()));
		oldRecord.setHttpSucess(NVLayer3Utils.addIntegerValue(oldRecord.getHttpSucess(), newRecord.getHttpSucess()));
		oldRecord.setHttpDownLoadTime(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getHttpDownLoadTime(), newRecord.getHttpDownLoadTime()));	
		oldRecord.setHttpFailure(NVLayer3Utils.addIntegerValue(oldRecord.getHttpFailure(), newRecord.getHttpFailure()));	
		oldRecord.setHttpDrop((NVLayer3Utils.addIntegerValue(oldRecord.getHttpDrop(), newRecord.getHttpDrop())));	
			
	}

	private static void setStartAndEndTimeIntoWrapper(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord,
			StringBuilder endtimeStamp) {
		if (!endtimeStamp.toString().isEmpty() && oldRecord.getTimeStamp() > Long.valueOf(endtimeStamp.toString())) {
			oldRecord.setTimeStamp(Long.valueOf(endtimeStamp.toString()));
			endtimeStamp.setLength(QMDLConstant.EMPTY_STRING_BUILDER_LENGTH);
		} else if (oldRecord.getTimeStamp() > newRecord.getTimeStamp()) {
			oldRecord.setTimeStamp(newRecord.getTimeStamp());
		}
		if (oldRecord.getEtimeStamp() < newRecord.getEtimeStamp()) {
			oldRecord.setEtimeStamp(newRecord.getEtimeStamp());
		}
		endtimeStamp.setLength(QMDLConstant.EMPTY_STRING_BUILDER_LENGTH);
		endtimeStamp.append(newRecord.getEtimeStamp());
	}

	private static void aggregateNewHandoverdata(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setNewHandOverIntiateCount(NVLayer3Utils.addIntegerValue(oldRecord.getNewHandOverIntiateCount() , newRecord.getNewHandOverIntiateCount()));
		oldRecord.setNewHandOverFailureCount(NVLayer3Utils.addIntegerValue(oldRecord.getNewHandOverFailureCount() , newRecord.getNewHandOverFailureCount()));
		oldRecord.setNewHandOverSuccessCount(NVLayer3Utils.addIntegerValue(oldRecord.getNewHandOverSuccessCount() , newRecord.getNewHandOverSuccessCount()));
	
		oldRecord.setNewHandOverIntiateInterCount(NVLayer3Utils.addIntegerValue(oldRecord.getNewHandOverIntiateInterCount() , newRecord.getNewHandOverIntiateInterCount()));
		oldRecord.setNewHandOverFailureInterCount(NVLayer3Utils.addIntegerValue(oldRecord.getNewHandOverFailureInterCount() , newRecord.getNewHandOverFailureInterCount()));
		oldRecord.setNewHandOverSuccessInterCount(NVLayer3Utils.addIntegerValue(oldRecord.getNewHandOverSuccessInterCount() , newRecord.getNewHandOverSuccessInterCount()));
		
		oldRecord.setNewHandOverIntiateIntraCount(NVLayer3Utils.addIntegerValue(oldRecord.getNewHandOverIntiateIntraCount() , newRecord.getNewHandOverIntiateIntraCount()));
		oldRecord.setNewHandOverFailureIntraCount(NVLayer3Utils.addIntegerValue(oldRecord.getNewHandOverFailureIntraCount() , newRecord.getNewHandOverFailureIntraCount()));
		oldRecord.setNewHandOverSuccessIntraCount(NVLayer3Utils.addIntegerValue(oldRecord.getNewHandOverSuccessIntraCount() , newRecord.getNewHandOverSuccessIntraCount()));
		oldRecord.setHandoverLatencyTime(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getHandoverLatencyTime(),newRecord.getHandoverLatencyTime()));
		
	}


	

	private static void aggregateCallData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setCallInitiateCount(NVLayer3Utils.addIntegerValue(oldRecord.getCallInitiateCount() , newRecord.getCallInitiateCount()));
		oldRecord.setCallFailureCount(NVLayer3Utils.addIntegerValue(oldRecord.getCallFailureCount() , newRecord.getCallFailureCount()));
		oldRecord.setCallSuccessCount(NVLayer3Utils.addIntegerValue(oldRecord.getCallSuccessCount() , newRecord.getCallSuccessCount()));
		oldRecord.setCallDropCount(NVLayer3Utils.addIntegerValue(oldRecord.getCallDropCount() , newRecord.getCallDropCount()));
		oldRecord.setCallSetupSuccessCount(NVLayer3Utils.addIntegerValue(oldRecord.getCallSetupSuccessCount() , newRecord.getCallSetupSuccessCount()));
		oldRecord.setCallSetupCount(NVLayer3Utils.addIntegerValue(oldRecord.getCallSetupCount() , newRecord.getCallSetupCount()));
		
		
		oldRecord.setRrcInitiate(NVLayer3Utils.addIntegerValue(oldRecord.getRrcInitiate() , newRecord.getRrcInitiate()));
		oldRecord.setRrcSucess(NVLayer3Utils.addIntegerValue(oldRecord.getRrcSucess() , newRecord.getRrcSucess()));
		oldRecord.setVoLTECallDropCount(NVLayer3Utils.addIntegerValue(oldRecord.getVoLTECallDropCount() , newRecord.getVoLTECallDropCount()));
		oldRecord.setCallInitiateOnNetCount(NVLayer3Utils.addIntegerValue(oldRecord.getCallInitiateOnNetCount() , newRecord.getCallInitiateOnNetCount()));
		oldRecord.setCallDropOnNetCount(NVLayer3Utils.addIntegerValue(oldRecord.getCallDropOnNetCount() , newRecord.getCallDropOnNetCount()));
		oldRecord.setCallFailureOnNetCount(NVLayer3Utils.addIntegerValue(oldRecord.getCallFailureOnNetCount() , newRecord.getCallFailureOnNetCount()));
		oldRecord.setCallSuccessOnNetCount(NVLayer3Utils.addIntegerValue(oldRecord.getCallSuccessOnNetCount() , newRecord.getCallSuccessOnNetCount()));
		oldRecord.setCallInitiateOffNetCount(NVLayer3Utils.addIntegerValue(oldRecord.getCallInitiateOffNetCount() , newRecord.getCallInitiateOffNetCount()));
		oldRecord.setCallDropOffNetCount(NVLayer3Utils.addIntegerValue(oldRecord.getCallDropOffNetCount() , newRecord.getCallDropOffNetCount()));
		oldRecord.setCallSuccessOffNetCount(NVLayer3Utils.addIntegerValue(oldRecord.getCallSuccessOffNetCount() , newRecord.getCallSuccessOffNetCount()));
		oldRecord.setCallFailureOffNetCount(NVLayer3Utils.addIntegerValue(oldRecord.getCallFailureOffNetCount() , newRecord.getCallFailureOffNetCount()));
		oldRecord.setCallConnectionSetupTime(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getCallConnectionSetupTime(),newRecord.getCallConnectionSetupTime()));
		oldRecord.setCallConnectionSetupTimeOnnet(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getCallConnectionSetupTimeOnnet(),newRecord.getCallConnectionSetupTimeOnnet()));
		oldRecord.setCallConnectionSetupTimeOffnet(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getCallConnectionSetupTimeOffnet(),newRecord.getCallConnectionSetupTimeOffnet()));
		oldRecord.setCallStatus(NVLayer3Utils.getLatestValue(oldRecord.getCallStatus(), newRecord.getCallStatus()));
		oldRecord.setImsRegistrationSetupTime(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getImsRegistrationSetupTime(),newRecord.getImsRegistrationSetupTime()));
		oldRecord.setCallSetupSuccessTime(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getCallSetupSuccessTime(),newRecord.getCallSetupSuccessTime()));
		
	
	}

	private static void aggregateDlUlData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setHttpDl(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getHttpDl(),newRecord.getHttpDl()));
		oldRecord.setHttpUl(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getHttpUl(),newRecord.getHttpUl()));
		oldRecord.setFtpDl(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getFtpDl(),newRecord.getFtpDl()));
		oldRecord.setFtpUl(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getFtpUl(),newRecord.getFtpUl()));
		oldRecord.setDlThroughPut(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getDlThroughPut(),newRecord.getDlThroughPut()));
		oldRecord.setUlThroughPut(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getUlThroughPut(),newRecord.getUlThroughPut()));
	}
	
	private static void aggregatePDSCHThroughput(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setPdschThroughput(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getPdschThroughput(),newRecord.getPdschThroughput()));
		oldRecord.setPdschThroughputPriCell(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getPdschThroughputPriCell(),newRecord.getPdschThroughputPriCell()));
		oldRecord.setPdschThroughputSecCell1(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getPdschThroughputSecCell1(),newRecord.getPdschThroughputSecCell1()));
		oldRecord.setPdschThroughputSecCell2(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getPdschThroughputSecCell2(),newRecord.getPdschThroughputSecCell2()));
		oldRecord.setPdschThroughputSecCell3(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getPdschThroughputSecCell3(),newRecord.getPdschThroughputSecCell3()));
		oldRecord.setPdschThroughputSecCell4(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getPdschThroughputSecCell4(),newRecord.getPdschThroughputSecCell4()));
		oldRecord.setPdschThroughputSecCell5(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getPdschThroughputSecCell5(),newRecord.getPdschThroughputSecCell5()));
		oldRecord.setPdschBLER(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getPdschBLER(),newRecord.getPdschBLER()));
	}
	
	private static void aggregateNetworkData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setCellid(NVLayer3Utils.getLatestValue(oldRecord.getCellid(), newRecord.getCellid()));
		oldRecord.setSectorId(NVLayer3Utils.getLatestValue(oldRecord.getSectorId(), newRecord.getSectorId()));
		oldRecord.seteNodebId(NVLayer3Utils.getLatestValue(oldRecord.geteNodebId(), newRecord.geteNodebId()));
		oldRecord.setCgi(NVLayer3Utils.getLatestValue(oldRecord.getCgi(), newRecord.getCgi()));
		oldRecord.setBand(NVLayer3Utils.getLatestValue(oldRecord.getBand(), newRecord.getBand()));
		oldRecord.setSsid(NVLayer3Utils.getLatestValue(oldRecord.getSsid(), newRecord.getSsid()));
		oldRecord.setCoverage(NVLayer3Utils.getLatestValue(oldRecord.getCoverage(), newRecord.getCoverage()));
		oldRecord.setSourcePci(NVLayer3Utils.getLatestValue(oldRecord.getSourcePci(), newRecord.getSourcePci()));
		oldRecord.setTargetPci(NVLayer3Utils.getLatestValue(oldRecord.getTargetPci(), newRecord.getTargetPci()));
		oldRecord.setEarfcn(NVLayer3Utils.getLatestValue(oldRecord.getEarfcn(), newRecord.getEarfcn()));
		oldRecord.setPci(NVLayer3Utils.getLatestValue(oldRecord.getPci(), newRecord.getPci()));
		oldRecord.setServingCellIndex(NVLayer3Utils.getLatestValue(oldRecord.getServingCellIndex(), newRecord.getServingCellIndex()));
		oldRecord.setCurrentSFN(NVLayer3Utils.getLatestValue(oldRecord.getCurrentSFN(), newRecord.getCurrentSFN()));
		
		oldRecord.setMcc(NVLayer3Utils.getLatestValue(oldRecord.getMcc(), newRecord.getMcc()));
		oldRecord.setMnc(NVLayer3Utils.getLatestValue(oldRecord.getMnc(), newRecord.getMnc()));
		oldRecord.setBssid(NVLayer3Utils.getLatestValue(oldRecord.getBssid(), newRecord.getBssid()));
		oldRecord.setChannel(NVLayer3Utils.getLatestValue(oldRecord.getChannel(), newRecord.getChannel()));
		oldRecord.setLinkSpeed(NVLayer3Utils.getLatestValue(oldRecord.getLinkSpeed(), newRecord.getLinkSpeed()));		
	}

	private static void aggregateMosData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setFinalMosG711(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getFinalMosG711(),newRecord.getFinalMosG711()));
		oldRecord.setFinalMosILbc(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getFinalMosILbc(),newRecord.getFinalMosILbc()));
		oldRecord.setFinalMosG711Onnet(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getFinalMosG711Onnet(),newRecord.getFinalMosG711Onnet()));
		oldRecord.setFinalMosG711Offnet(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getFinalMosG711Offnet(),newRecord.getFinalMosG711Offnet()));
		oldRecord.setFinalMosILbcOnnet(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getFinalMosILbcOnnet(),newRecord.getFinalMosILbcOnnet()));
		oldRecord.setFinalMosILbcOffnet(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getFinalMosILbcOffnet(),newRecord.getFinalMosILbcOffnet()));
	}

	private static void aggregateSignalData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		aggregateRSRPData(oldRecord, newRecord);
		aggregateRSRQData(oldRecord, newRecord);
		aggregateRSSIData(oldRecord, newRecord);
		aggregateSINRData(oldRecord, newRecord);
		aggregateWifiData(oldRecord, newRecord);
	}
	
	private static void aggregateRSRPData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setrSRPRx0Data(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getrSRPRx0Data(),newRecord.getrSRPRx0Data()));
		oldRecord.setrSRPRx1Data(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getrSRPRx1Data(),newRecord.getrSRPRx1Data()));
		oldRecord.setMeasureRSRPData(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getMeasureRSRPData(),newRecord.getMeasureRSRPData()));
	}
	
	private static void aggregateRSRQData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setrSRQRx0Data(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getrSRQRx0Data(),newRecord.getrSRQRx0Data()));
		oldRecord.setrSRQRx1Data(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getrSRQRx1Data(),newRecord.getrSRQRx1Data()));
		oldRecord.setMeasureRSRQData(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getMeasureRSRQData(),newRecord.getMeasureRSRQData()));
	}
	
	private static void aggregateRSSIData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setrSSIRx0Data(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getrSSIRx0Data(),newRecord.getrSSIRx0Data()));
		oldRecord.setrSSIRx1Data(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getrSSIRx1Data(),newRecord.getrSSIRx1Data()));
		oldRecord.setrSSIData(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getrSSIData(),newRecord.getrSSIData()));
	}
	
	private static void aggregateSINRData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setsINRData(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getsINRData(),newRecord.getsINRData()));
		oldRecord.setsINRRx0Data(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getsINRRx0Data(), newRecord.getsINRRx0Data()));
		oldRecord.setsINRRx1Data(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getsINRRx1Data(), newRecord.getsINRRx1Data()));
	}
	
	private static void aggregateWifiData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setWifiRssi(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getWifiRssi(),newRecord.getWifiRssi()));
		oldRecord.setWifiSnr(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getWifiSnr(),newRecord.getWifiSnr()));
	}
	

	private static void aggregateWebPerformanceData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setAvgJitter(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getAvgJitter(),newRecord.getAvgJitter()));
		oldRecord.setAvgReponseTime(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getAvgReponseTime(),newRecord.getAvgReponseTime()));
		oldRecord.setConnectionSetupTime(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getConnectionSetupTime(),newRecord.getConnectionSetupTime()));
		oldRecord.setPacketLoss(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getPacketLoss(),newRecord.getPacketLoss()));
		oldRecord.setLatency(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getLatency(),newRecord.getLatency()));
	}

	private static void aggregateLocationData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setLat(NVLayer3Utils.getLatestValue(oldRecord.getLat(), newRecord.getLat()));
		oldRecord.setLon(NVLayer3Utils.getLatestValue(oldRecord.getLon(), newRecord.getLon()));
		oldRecord.setAddress(NVLayer3Utils.getLatestValue(oldRecord.getAddress(), newRecord.getAddress()));
	}

	private static void aggregateCellData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setCellChangeCount(NVLayer3Utils.addIntegerValue(oldRecord.getCellChangeCount() , newRecord.getCellChangeCount()));
		oldRecord.setCellTiming0(NVLayer3Utils.getLatestValue(oldRecord.getCellTiming0(), newRecord.getCellTiming0()));
		oldRecord.setCellTiming1(NVLayer3Utils.getLatestValue(oldRecord.getCellTiming1(), newRecord.getCellTiming1()));
		oldRecord.setCellTimingSFN0(NVLayer3Utils.getLatestValue(oldRecord.getCellTimingSFN0(), newRecord.getCellTimingSFN0()));
		oldRecord.setCellTimingSFN1(NVLayer3Utils.getLatestValue(oldRecord.getCellTimingSFN1(), newRecord.getCellTimingSFN1()));

	}

	private static void aggregateErabData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setTotalErab(NVLayer3Utils.addIntegerValue(oldRecord.getTotalErab() , newRecord.getTotalErab()));
		oldRecord.setErabDrop(NVLayer3Utils.addIntegerValue(oldRecord.getErabDrop() ,newRecord.getErabDrop()));
	}

	private static void aggregateQMDLKpiData(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		aggregateDlUlEarfcnAndBandwidthParam(oldRecord, newRecord);
		aggregateLayer3DriveKpisParam(oldRecord, newRecord);
		oldRecord.setSpecialSubframeConfiguration(NVLayer3Utils.getLatestValue(oldRecord.getSpecialSubframeConfiguration(), newRecord.getSpecialSubframeConfiguration()));
		oldRecord.setB0c2Pci(NVLayer3Utils.getLatestValue(oldRecord.getB0c2Pci(), newRecord.getB0c2Pci()));
		oldRecord.setTddConfig(NVLayer3Utils.getLatestValue(oldRecord.getTddConfig(), newRecord.getTddConfig()));
		oldRecord.setModulationType(NVLayer3Utils.getLatestValue(oldRecord.getModulationType(), newRecord.getModulationType()));
		oldRecord.setNumberTxAntennas(NVLayer3Utils.getLatestValue(oldRecord.getNumberTxAntennas(), newRecord.getNumberTxAntennas()));
		oldRecord.setNumberRxAntennas(NVLayer3Utils.getLatestValue(oldRecord.getNumberRxAntennas(), newRecord.getNumberRxAntennas()));
		oldRecord.setSpatialRank(NVLayer3Utils.getLatestValue(oldRecord.getSpatialRank(), newRecord.getSpatialRank()));
		oldRecord.setrBAllocationSlot0(NVLayer3Utils.getLatestValue(oldRecord.getrBAllocationSlot0(), newRecord.getrBAllocationSlot0()));
		oldRecord.setrBAllocationSlot1(NVLayer3Utils.getLatestValue(oldRecord.getrBAllocationSlot1(), newRecord.getrBAllocationSlot1()));
		oldRecord.setB173numRbs(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getB173numRbs(),newRecord.getB173numRbs()));
	}

	private static void aggregateLayer3DriveKpisParam(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setRankIndex(NVLayer3Utils.getLatestValue(oldRecord.getRankIndex(), newRecord.getRankIndex()));
		oldRecord.setCqiCwo(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getCqiCwo(), newRecord.getCqiCwo()));
		oldRecord.setCarrierIndex(NVLayer3Utils.getLatestValue(oldRecord.getCarrierIndex(), newRecord.getCarrierIndex()));
		oldRecord.setPuschTxPower(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getPuschTxPower(),newRecord.getPuschTxPower()));
		oldRecord.setTracking_Area_Code(NVLayer3Utils.getLatestValue(oldRecord.getTracking_Area_Code(), newRecord.getTracking_Area_Code()));
		oldRecord.setTimingAdvance(NVLayer3Utils.getLatestValue(oldRecord.getTimingAdvance(), newRecord.getTimingAdvance()));
		oldRecord.setMcs(NVLayer3Utils.getLatestValue(oldRecord.getMcs(), newRecord.getMcs()));
		oldRecord.setNumRB(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getNumRB(),newRecord.getNumRB()));
		oldRecord.setIndexPMI(NVLayer3Utils.getLatestValue(oldRecord.getIndexPMI(), newRecord.getIndexPMI()));
		oldRecord.setOutOfSyncBler(NVLayer3Utils.getDoubleAggrigateArray(oldRecord.getOutOfSyncBler(),newRecord.getOutOfSyncBler()));
	}

	private static void aggregateDlUlEarfcnAndBandwidthParam(QMDLLogCodeWrapper oldRecord, QMDLLogCodeWrapper newRecord) {
		oldRecord.setDlEarfcn(NVLayer3Utils.getLatestValue(oldRecord.getDlEarfcn(), newRecord.getDlEarfcn()));
		oldRecord.setUlEarfcn(NVLayer3Utils.getLatestValue(oldRecord.getUlEarfcn(), newRecord.getUlEarfcn()));
		oldRecord.setDlCp(NVLayer3Utils.getLatestValue(oldRecord.getDlCp(), newRecord.getDlCp()));
		oldRecord.setUlCp(NVLayer3Utils.getLatestValue(oldRecord.getUlCp(), newRecord.getUlCp()));
		oldRecord.setDl_Bandwidth(NVLayer3Utils.getLatestValue(oldRecord.getDl_Bandwidth(), newRecord.getDl_Bandwidth()));
		oldRecord.setUl_Bandwidth(NVLayer3Utils.getLatestValue(oldRecord.getUl_Bandwidth(), newRecord.getUl_Bandwidth()));
	}
	
	private static Integer updateCellIdIfNull(QMDLLogCodeWrapper logCodeWrapper, Integer previousCellId) {
		logger.debug("Inside Method updateCellIdIfNull wrapper Cell Id: {} previous Cell Id: {}", logCodeWrapper.getCellid(), previousCellId);
		if(logCodeWrapper.getCellid() == null) {
			logCodeWrapper.setCellid(previousCellId);
			logger.debug("returning previous Cell Id: {}", previousCellId);
			return previousCellId;
		} else {
			logger.debug("returning new Cell Id: {}", logCodeWrapper.getCellid());
			return logCodeWrapper.getCellid();
		}
	}
}

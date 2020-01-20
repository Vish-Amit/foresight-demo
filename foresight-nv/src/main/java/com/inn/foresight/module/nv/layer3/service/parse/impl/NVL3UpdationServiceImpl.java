package com.inn.foresight.module.nv.layer3.service.parse.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.TemplateType;
import com.inn.foresight.module.nv.core.workorder.dao.IGWOMetaDao;
import com.inn.foresight.module.nv.core.workorder.model.GWOMeta;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.Layer3SummaryWrapper;
import com.inn.foresight.module.nv.layer3.service.parse.INVL3UpdationService;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.report.customreport.ssvt.wrapper.SiteInfoWrapper;
import com.inn.foresight.module.nv.workorder.model.WOFileDetail;

@Service("NVL3UpdationServiceImpl")
public class NVL3UpdationServiceImpl implements INVL3UpdationService {
	
	/** The logger. */
	private Logger logger = LogManager.getLogger(NVL3UpdationServiceImpl.class);
	

	@Autowired
	private IGWOMetaDao gwoMetaDao;
	
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateDeviceIdAndCemsdataintoGeoMetaData(Layer3SummaryWrapper aggrigateWrapperData,
			Entry<String, List<WOFileDetail>> woFileDetailEntry) {
		try {
			List<WOFileDetail> wofileDetailList = woFileDetailEntry.getValue();
			if(!wofileDetailList.isEmpty()){
				GenericWorkorder genericWorkOrderId = wofileDetailList.get(QMDLConstant.FIRST_RECORD_INDEX).getWoRecipeMapping().getGenericWorkorder();
				if( ((genericWorkOrderId.getTemplateType().name().equals(TemplateType.NV_ADHOC_OD.name()))  ||
						(genericWorkOrderId.getTemplateType().name().equals(TemplateType.NV_OPENDRIVE.name())))
						&& aggrigateWrapperData.getDeviceId()!=null){

					GWOMeta gwoMeta = getGwoMetaWrapperfordeviceid(aggrigateWrapperData,genericWorkOrderId);						
					updateIntoGwoMeta(gwoMeta);						

					GWOMeta gwoMeta2 =	getGwoMetaWrapperforcemsdata(genericWorkOrderId);
					updateIntoGwoMeta(gwoMeta2);

				}
			}
		} catch (Exception e) {
			logger.error("Error in updating gwo meta data for deviceid and cemsData {}", Utils.getStackTrace(e));
		}
	}
	public void updateIntoGwoMeta(GWOMeta gwoMeta) {
		try {
			gwoMetaDao.update(gwoMeta);
		} catch (RestException e) {
			logger.error("Error while updating Into GwoMeta  {}", Utils.getStackTrace(e));

		}catch(Exception e) {
			logger.error("Exception inside method updateIntoGwoMeta {} ",Utils.getStackTrace(e));
		}
	}
	
	public void insertIntoGwoMeta(GWOMeta gwoMeta) {
		try {
			gwoMetaDao.create(gwoMeta);
			logger.info("inside method insertIntoGwoMeta {} ",gwoMeta);
		} catch (RestException e) {
			logger.error("Error while inserting Into GwoMeta  {}", Utils.getStackTrace(e));

		}catch(Exception e) {
			logger.error("Exception inside method insertIntoGwoMeta {} ",Utils.getStackTrace(e));
		}
	}

	private GWOMeta getGwoMetaWrapperforcemsdata(GenericWorkorder genericWorkOrderId) {		
		GWOMeta geoMeta=null;
		try {
			geoMeta=gwoMetaDao.getGwoMetaDataByGenericWorkorderId(genericWorkOrderId.getId(), QMDLConstant.CEMSDATA_ENTITY_TYPE);

		} catch (RestException e) {
			logger.warn("Getting empty Response for workorderId {} {}  ",genericWorkOrderId.getId() ,e);
		}
		logger.info("Going to insert cemsdata into gwometa ");		

		GWOMeta geoMetafinal=geoMeta!=null?geoMeta:new GWOMeta();
		geoMetafinal.setGenericWorkOrder(genericWorkOrderId);
		geoMetafinal.setEntityType(QMDLConstant.CEMSDATA_ENTITY_TYPE);		
		return geoMetafinal;
	}

	private GWOMeta getGwoMetaWrapperfordeviceid(Layer3SummaryWrapper aggrigateWrapperData,
			GenericWorkorder genericWorkOrderId) {
		GWOMeta geoMeta=null;
		try {
			geoMeta=gwoMetaDao.getGwoMetaDataByGenericWorkorderId(genericWorkOrderId.getId(), QMDLConstant.DEVICEID_ENTITY_TYPE);

		} catch (RestException e) {
			logger.warn("Getting empty Response for workorderId {}  ",genericWorkOrderId.getId());
		}
		String deviceid = aggrigateWrapperData.getDeviceId();
		return setValuesIntoGwoMeta(genericWorkOrderId, deviceid,geoMeta,QMDLConstant.DEVICEID_ENTITY_TYPE);

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateSectorWiseSummaryIntoGeoMetaData(List<SiteInfoWrapper> siteInfoWrapper,
			GenericWorkorder genericWorkOrderId) {
		   logger.info("inside method updateSectorWiseSummaryIntoGeoMetaData {}",
				siteInfoWrapper != null ? siteInfoWrapper.size() : null);
		try {
			if (siteInfoWrapper != null && !siteInfoWrapper.isEmpty()) {
				getGwoMetaWrapperforSectorWiseSummary(siteInfoWrapper, genericWorkOrderId);
			}
		} catch (Exception e) {
			logger.info("Error in updating gwo meta data for summary data {}", Utils.getStackTrace(e));
		}
	}
	
	private void getGwoMetaWrapperforSectorWiseSummary(List<SiteInfoWrapper> siteInfoWrapper,
			GenericWorkorder genericWorkOrderId) {
		GWOMeta geoMeta=null;
		logger.info("inside method getGwoMetaWrapperforSectorWiseSummary");
		String json = new Gson().toJson(siteInfoWrapper);
		try {
			geoMeta=gwoMetaDao.getGwoMetaDataByGenericWorkorderIdforReport(genericWorkOrderId.getId(), QMDLConstant.SECTORWISE_SUMMARY_JSON);
		} catch (RestException e) {
			logger.warn("Getting empty Response for workorderId {}  ",genericWorkOrderId.getId());
		}if(geoMeta!=null) {
			geoMeta =  setValuesIntoGwoMeta(genericWorkOrderId, json,geoMeta,QMDLConstant.SECTORWISE_SUMMARY_JSON);
			updateIntoGwoMeta(geoMeta);		
		}else {
			geoMeta =  setValuesIntoGwoMeta(genericWorkOrderId, json,geoMeta,QMDLConstant.SECTORWISE_SUMMARY_JSON);
			insertIntoGwoMeta(geoMeta);	
		}
	}
	

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateReportStatusIntoGWOMeta(Layer3SummaryWrapper aggrigateWrapperData,
			Entry<String, List<WOFileDetail>> woFileDetail) {
		List<WOFileDetail> wofileDetailList = woFileDetail.getValue();
		if(wofileDetailList!=null && !wofileDetailList.isEmpty()){

			GenericWorkorder genericWorkOrderId = wofileDetailList.get(QMDLConstant.FIRST_RECORD_INDEX).getWoRecipeMapping().getGenericWorkorder();	
			GWOMeta geoMeta=getGwoMetaDataForBandLock(genericWorkOrderId);

			if(aggrigateWrapperData.isIsband()){
				setValuesIntoGwoMeta(genericWorkOrderId,NVLayer3Utils.getStringFromSetValues(aggrigateWrapperData.getTechnologyBandSet()), geoMeta, QMDLConstant.BAND_ENTITY_TYPE);
			}else {
				setValuesIntoGwoMeta(genericWorkOrderId, QMDLConstant.BAND_UNLOCK, geoMeta, QMDLConstant.BAND_ENTITY_TYPE);
			}			
			updateIntoGwoMeta(geoMeta);

		}

	}

	private GWOMeta getGwoMetaDataForBandLock(GenericWorkorder genericWorkOrderId) {
		try {
			GWOMeta geoMeta=gwoMetaDao.getGwoMetaDataByGenericWorkorderId(genericWorkOrderId.getId(), QMDLConstant.BAND_ENTITY_TYPE);
			if(geoMeta!=null){
				return geoMeta;
			}
		} catch (RestException e) {
			logger.warn("Getting empty Response for band Lock workorderId {}  ",genericWorkOrderId.getId());
		}
		return new GWOMeta();
	}

	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateImeiIntoGeoMetaData(Layer3SummaryWrapper aggrigateWrapperData,
			Entry<String, List<WOFileDetail>> woFileDetailEntry) {
		try {
			List<WOFileDetail> wofileDetailList = woFileDetailEntry.getValue();
			GWOMeta gwoMeta = getGwoMetaWrapper(aggrigateWrapperData, wofileDetailList);
			if (gwoMeta != null) {
				updateIntoGwoMeta(gwoMeta);
			}
		} catch (Exception e) {
			logger.error("Error in updating gwo meta data for imei {}", Utils.getStackTrace(e));
		}
	}

	private GWOMeta getGwoMetaWrapper(Layer3SummaryWrapper aggrigateWrapperData, List<WOFileDetail> wofileDetailList) {
		String imeiOld=null;
		if(!wofileDetailList.isEmpty()){
		GenericWorkorder genericWorkOrderId = wofileDetailList.get(QMDLConstant.FIRST_RECORD_INDEX).getWoRecipeMapping().getGenericWorkorder();
		GWOMeta geoMeta=null;
		try {
			 geoMeta=gwoMetaDao.getGwoMetaDataByGenericWorkorderId(genericWorkOrderId.getId(), QMDLConstant.IMEI_ENTITY_TYPE);
			if(geoMeta!=null){
				imeiOld=geoMeta.getEntityValue();
			}
		} catch (RestException e) {
			logger.warn("Getting empty Response for workorderId {}  ",genericWorkOrderId.getId());
		}
		String imei = getSetOfImeiByWrapper(imeiOld,aggrigateWrapperData.getImei());
		return setValuesIntoGwoMeta(genericWorkOrderId, imei,geoMeta,QMDLConstant.IMEI_ENTITY_TYPE);
		}
		return null;
	}



	private GWOMeta setValuesIntoGwoMeta(GenericWorkorder genericWorkOrderId, String imei, GWOMeta geoMeta, String imeiEntityType) {
		GWOMeta geoMetafinal=geoMeta!=null?geoMeta:new GWOMeta();
		geoMetafinal.setGenericWorkOrder(genericWorkOrderId);
		geoMetafinal.setEntityType(imeiEntityType);
		geoMetafinal.setEntityValue(imei);
		return geoMetafinal;
	}



	private String getSetOfImeiByWrapper(String imeiOld, Set<String> imei) {
		if(imei!=null && !imei.isEmpty()){
			if(imeiOld!=null){
			return 	mergeImeiWithSet(imeiOld,imei);
			}else{
				return NVLayer3Utils.getStringFromSetValues(imei);
			}
		}
		return imeiOld;
	}



	private String mergeImeiWithSet(String imeiOld, Set<String> imei) {
		List<String> oldImeiList = Arrays.asList(imeiOld.split(Symbol.UNDERSCORE_STRING));
		imei.addAll(oldImeiList);
		return NVLayer3Utils.getStringFromSetValues(imei);
	}

}

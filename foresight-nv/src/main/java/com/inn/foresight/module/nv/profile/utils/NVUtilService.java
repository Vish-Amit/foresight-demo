package com.inn.foresight.module.nv.profile.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inn.commons.lang.MapUtils;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.app.dao.ILicenseInfoDao;
import com.inn.foresight.module.nv.app.wrapper.DeviceInfoWrapper;
import com.inn.foresight.module.nv.profile.constants.NVProfileConstants;
import com.inn.foresight.module.nv.profile.dao.INVProfileDataDao;
import com.inn.foresight.module.nv.profile.model.NVProfileData;
import com.inn.foresight.module.nv.profile.service.INVProfileDataService;
import com.inn.foresight.module.nv.profile.wrapper.SiteLocationInfo;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;
import com.inn.foresight.module.nv.service.ISiteDetailService;
import com.inn.product.systemconfiguration.dao.SystemConfigurationDao;
import com.inn.product.systemconfiguration.model.SystemConfiguration;

@Service("NVUtilService")
public class NVUtilService {
	private static Logger logger = LogManager.getLogger(NVUtilService.class);
	@Autowired
	private SystemConfigurationDao iSystemConfigurationDao;
	
	@Autowired
	private INVProfileDataDao profileDataDao;
	@Autowired
	private INVProfileDataService iNVProfileDataService;
	@Autowired 
	private ISiteDetailService siteDetailService;
	@Autowired
	private ILicenseInfoDao dao;

	private static Map<String, String> mccMncOperatorMap=null;

	private static List<NVProfileData> predefineProfileList=null;

	/** The location map. */
	private static Map<String, SiteLocationInfo> locationMap=null;
	private static Map<Integer,Long> licenceInfoCountMap=null;


	

	@SuppressWarnings("unchecked")
	private void loadMccMncOperatorMap() {
		if (mccMncOperatorMap == null) {
			try {
				logger.info("Going to loadMccMncOperatorMap");
				List<SystemConfiguration> val = iSystemConfigurationDao.getSystemConfigurationByNameAndType(
						NVProfileConstants.OPERATOR_MCC_MNC_MAP, Arrays.asList(NVProfileConstants.SYSTEM));
				if (!val.isEmpty()) {
					mccMncOperatorMap = new ObjectMapper().readValue(val.get(ForesightConstants.INDEX_ZERO)
																		.getValue(),
							HashMap.class);
				}
				logger.info("Done loadMccMncOperatorMap size {} ",mccMncOperatorMap==null?null: mccMncOperatorMap.size());
			} catch (Exception e) {
				logger.error("Exception inside loadMccMncMap {}", Utils.getStackTrace(e));
			}
		} else {
			logger.info("loadMccMncOperatorMap already loaded size {}", mccMncOperatorMap.size());
		}
	}

	private void loadPreDefineProfileList() {
		logger.info("Going to loadPreDefineProfileList");
		predefineProfileList = profileDataDao.getProfileByType(NVProfileConstants.PRE_DEFINE_PROFILE);
		logger.info("Done loadPreDefineProfileList size {}", predefineProfileList.size());
	}

	/**
	 * Update profile map.
	 *
	 * @return the profile map
	 */
	public void loadCustomProfileMap() {
		logger.info("Going to loadCustomProfileMap");
		iNVProfileDataService.updateProfileMap();
	}
	
	
	public void updateLocationMap() {
		if (MapUtils.isEmpty(locationMap)) {
			locationMap = new HashMap<>();
		}
			List<SiteInformationWrapper> siteDetailList = null;
			try {
				siteDetailList = siteDetailService.getMacroSiteDetailsForCellLevelForReport(null, null, null, null,
						false, true, true);
			} catch (DaoException e) {
				logger.error("Exception in updateLocationMap : {}", ExceptionUtils.getStackTrace(e));
			}
			for (SiteInformationWrapper siteDetail : siteDetailList) {
				SiteLocationInfo siteLocationInfo = new SiteLocationInfo();
				try {
					updateSiteLocationInfo(siteDetail, siteLocationInfo);
				} catch (Exception e) {
					logger.error("Error in updateLocationMap while getting Object {} :",
							ExceptionUtils.getStackTrace(e));
				}
				
			}
			logger.info("Getting locationMap size {}", locationMap.size());

		
	}

	private void updateSiteLocationInfo(SiteInformationWrapper siteDetail, SiteLocationInfo siteLocationInfo) {
		siteLocationInfo.setLatitude(siteDetail.getLat());
		siteLocationInfo.setLongitude(siteDetail.getLon());
		if (siteDetail.getCluster() != null) {
			siteLocationInfo.setGeographyL4Name(siteDetail.getCluster());
		}
		if (siteDetail.getCityName() != null) {
			siteLocationInfo.setGeographyL3Name(siteDetail.getCityName());
		}
		if (siteDetail.getRegion() != null) {
			siteLocationInfo.setGeographyL2Name(siteDetail.getRegion());
		}
		if (siteDetail.getZone() != null) {
			siteLocationInfo.setGeographyL1Name(siteDetail.getZone());
		}
		if (siteLocationInfo.getGeographyL1Name() != null || siteLocationInfo.getGeographyL2Name() != null
				|| siteLocationInfo.getGeographyL3Name() != null
				|| siteLocationInfo.getGeographyL4Name() != null) {
			String key = getKey(siteDetail);
			if (key != null) {
				locationMap.put(key, siteLocationInfo);
			}
		}
	}

	private String getKey(SiteInformationWrapper m) {
		return (m != null && m.getEcgi() != null) ? m.getEcgi() : null;
	}
	
	private void updateLicenceInfoCount() {
		try {
			logger.info("Going to updateLicenceInfoCount on startup");
			List<DeviceInfoWrapper> list = dao.getLicenceCountGroupByLicenceMaster();
			if (!list.isEmpty()) {
				licenceInfoCountMap = list	.stream().filter(d->d.getLicenceMasterId()!=null)
											.collect(Collectors.toMap(DeviceInfoWrapper::getLicenceMasterId,
													DeviceInfoWrapper::getCount));
				logger.info("licenceInfoCountMap value {}", licenceInfoCountMap);
			} else {
				logger.error("getLicenceCountGroupByLicenceMaster list is empty");
			}

		} catch (Exception e) {
			logger.error("Exception in getLicenceCountMap{} ", Utils.getStackTrace(e));
		}

	}
	public static void updateLicenceCountForLicenceMaster(Integer licenceMasterId) {
		logger.info("updateLicenceCountForLicenceMaster before update  {}",licenceInfoCountMap);
		if (licenceInfoCountMap == null) {
			licenceInfoCountMap = new HashMap<>();
		}
		if (licenceInfoCountMap.containsKey(licenceMasterId)) {
			licenceInfoCountMap.put(licenceMasterId, licenceInfoCountMap.get(licenceMasterId) + ForesightConstants.ONE_INT);
		} else {
			licenceInfoCountMap.put(licenceMasterId, Long.valueOf(ForesightConstants.ONE_INT));
		}
		logger.info("updateLicenceCountForLicenceMaster after update  {}",licenceInfoCountMap);
	}
	
    public void callScheduler() {
		logger.error("Scheduler start NVUtils ********************");
		if (Utils.isTestBuild()) {
			return;
		}

    	updateLocationMap();
    	loadCustomProfileMap();
    	logger.error("Scheduler end NVUtils ********************");
    }
	public void init() {
		logger.info("Going to start init......");
		if (Utils.isTestBuild()) {
			return;
		}

		//Profile
		loadMccMncOperatorMap();
		loadPreDefineProfileList();
		loadCustomProfileMap();
		updateLocationMap();
		//Device Registration
		updateLicenceInfoCount();
		logger.info("Init End..");
	}

	public void clearAllMap() {
		if (licenceInfoCountMap != null) {
			licenceInfoCountMap.clear();
		}
		if (mccMncOperatorMap != null) {
			mccMncOperatorMap.clear();
		}
		if (locationMap != null) {
			locationMap.clear();
		}
		if (licenceInfoCountMap != null) {
			licenceInfoCountMap.clear();
		}
	}
	
	public static Map<String, SiteLocationInfo> getLocationMap() {
		return locationMap;
	}
	
	public static void setLocationMap(Map<String, SiteLocationInfo> locationMap) {
		NVUtilService.locationMap = locationMap;
	}
	
	public static Map<Integer, Long> getLicenceInfoCountMap() {
		return licenceInfoCountMap;
	}
	
	public static void setLicenceInfoCountMap(Map<Integer, Long> licenceInfoCountMap) {
		NVUtilService.licenceInfoCountMap = licenceInfoCountMap;
	}
	
	public static Map<String, String> getMccMncOperatorMap() {
		return mccMncOperatorMap;
	}
	
	public static void setMccMncOperatorMap(Map<String, String> mccMncOperatorMap) {
		NVUtilService.mccMncOperatorMap = mccMncOperatorMap;
	}
	
	public static List<NVProfileData> getPredefineProfileList() {
		return predefineProfileList;
	}
	
	public static void setPredefineProfileList(List<NVProfileData> predefineProfileList) {
		NVUtilService.predefineProfileList = predefineProfileList;
	}
}


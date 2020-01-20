package com.inn.foresight.module.nv.profile.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inn.commons.Symbol;
import com.inn.commons.lang.MapUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.maps.LatLng;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.maplayer.service.IGenericMapService;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.app.dao.IDeviceInfoDao;
import com.inn.foresight.module.nv.app.utils.AppUtils;
import com.inn.foresight.module.nv.device.service.INVDeviceDataService;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper;
import com.inn.foresight.module.nv.profile.constants.NVProfileConstants;
import com.inn.foresight.module.nv.profile.dao.INVProfileDataDao;
import com.inn.foresight.module.nv.profile.model.NVProfileData;
import com.inn.foresight.module.nv.profile.service.INVProfileDataService;
import com.inn.foresight.module.nv.profile.utils.NVProfileDataUtils;
import com.inn.foresight.module.nv.profile.utils.NVUtilService;
import com.inn.foresight.module.nv.profile.wrapper.NVProfileDataWrapper;
import com.inn.foresight.module.nv.profile.wrapper.NVProfileRequestWrapper;
import com.inn.foresight.module.nv.profile.wrapper.NVProfileTemplate;
import com.inn.foresight.module.nv.profile.wrapper.NVProfileWrapper;
import com.inn.foresight.module.nv.profile.wrapper.SiteLocationInfo;
import com.inn.product.security.utils.AuthenticationCommonUtil;
import com.inn.product.systemconfiguration.dao.SystemConfigurationDao;
import com.inn.product.systemconfiguration.model.SystemConfiguration;
import com.inn.product.um.geography.dao.GeographyL1Dao;
import com.inn.product.um.geography.dao.GeographyL2Dao;
import com.inn.product.um.geography.dao.GeographyL3Dao;
import com.inn.product.um.geography.dao.GeographyL4Dao;
import com.inn.product.um.geography.model.GeographyL2;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;
import com.inn.product.um.user.model.User;

/**
 * The Class NVProfileDataServiceImpl.
 *
 * @author innoeye date - 26-Feb-2018 2:24:30 PM
 */
@Service("NVProfileDataServiceImpl")
public class NVProfileDataServiceImpl extends AbstractService<Integer, NVProfileData> implements INVProfileDataService {

    /** The logger. */
    private static Logger logger = LogManager.getLogger(NVProfileDataServiceImpl.class);

    /** The profile data dao. */
    @Autowired
    private INVProfileDataDao profileDataDao;

    @Autowired
    private INVDeviceDataService deviceDataService;
    
    /** The nv profile map. */
	private static Map<String, NVProfileData> nvProfileMap;

    /**
     * Sets the dao.
     *
     * @param profileDataDao the new dao
     */
    @Autowired
    public void setDao(INVProfileDataDao profileDataDao) {
        super.setDao(profileDataDao);
        this.profileDataDao = profileDataDao;
    }

    /** The system configuration dao. */
    @Autowired
    private SystemConfigurationDao systemConfigurationDao;

    /** The geography L 1 dao. */
    @Autowired
    private GeographyL1Dao geographyL1Dao;

    /** The geography L 2 dao. */
    @Autowired
    private GeographyL2Dao geographyL2Dao;

    /** The geography L 3 dao. */
    @Autowired
    private GeographyL3Dao geographyL3Dao;

    /** The geography L 4 dao. */
    @Autowired
    private GeographyL4Dao geographyL4Dao;

    /** The device info dao. */
    @Autowired
    private IDeviceInfoDao deviceInfoDao;


    /** The i generic map service. */
    @Autowired
    private IGenericMapService iGenericMapService;

    /**
     * Gets the all profiles.
     *
     * @return the all profiles
     * @throws RestException the rest exception
     */
    @Override
    public List<NVProfileDataWrapper> getAllProfiles() {
        try {
            logger.info("Inside getAllProfiles");
            List<NVProfileDataWrapper> profileDatas = profileDataDao.getAllProfiles();
            logger.info("Done getAllProfiles with resultSize : {}", profileDatas.size());
            return profileDatas;
        } catch (Exception e) {
            logger.error("Essception in getAllProfiles : {} ", ExceptionUtils.getStackTrace(e));
            throw new RestException(e.getMessage());
        }
    }

    /**
     * Gets the all profile templates.
     *
     * @param module the module
     * @return the all profile templates
     * @throws RestException the rest exception
     */
    @Override
    public List<NVProfileTemplate> getAllProfileTemplates(String module) {
        try {
            logger.info("Inside getAllProfileTemplates");
            List<SystemConfiguration> systemConfigurations = systemConfigurationDao
                    .getSystemConfigurationByType(module);
            ObjectMapper mapper = new ObjectMapper();
            List<NVProfileTemplate> templates = systemConfigurations.stream().map(s -> {
                try {
                    return mapper.readValue(s.getValue(), NVProfileTemplate.class);
                } catch (Exception e) {
                    return null;
                }
            }).collect(Collectors.toList());
            logger.info("Done getAllProfileTemplates with resultSize : {}", templates.size());
            return templates;
        } catch (Exception e) {
            logger.error("Essception in getAllProfileTemplates : {} ", ExceptionUtils.getStackTrace(e));
            throw new RestException(e.getMessage());
        }
    }

    /**
     * Save profile.
     *
     * @param profileData the profile data
     * @param creator the creator
     * @return the NV profile data
     * @throws RestException the rest exception
     */
    @Override
    @Transactional
    public NVProfileData saveProfile(NVProfileData profileData, User creator) {
        logger.info("Going to create Profile");
        try {
            NVProfileData nvProfileData = createProfile(profileData, creator);
            nvProfileData.setProfileId(
                    AppUtils.generateUniqueIdWithPrefix(nvProfileData.getId(), NVProfileConstants.PROFILE_ID_PREFIX));
            nvProfileData = profileDataDao.update(nvProfileData);
            updateProfileMap(Boolean.TRUE);
            logger.info("Done saveProfile created profileData.id : {}", nvProfileData.getId());
            return nvProfileData;
        } catch (Exception e) {
            logger.error("Essception in saveProfile : {} ", ExceptionUtils.getStackTrace(e));
            throw new RestException(e.getMessage());
        }
    }

    /**
     * Creates the profile.
     *
     * @param profileData the profile data
     * @param creator the creator
     * @return the NV profile data
     * @throws DaoException the dao exception
     * @throws RestException the rest exception
     */
    private NVProfileData createProfile(NVProfileData profileData, User creator) {
        NVProfileData nvProfileData = new NVProfileData(profileData);
        nvProfileData.setCreator(creator);
        updateProfileGeography(profileData, nvProfileData);
        validateProfileData(nvProfileData);
        return profileDataDao.create(nvProfileData);
    }

    /**
     * Validate profile data.
     *
     * @param nvProfileData the nv profile data
     * @throws RestException the rest exception
     */
    private void validateProfileData(NVProfileData nvProfileData) {
        NVProfileData nvProfile = getPublishedProfile(Arrays.asList(getGeographyByProfileLevel(nvProfileData)));
        if (nvProfile != null && nvProfileData.getStartTime().getTime() <= nvProfile.getEndTime().getTime()) {
            throw new RestException("Already Profile is active for given details id : " + nvProfile.getProfileId());
        }
    }

    /**
     * Update profile geography.
     *
     * @param profileData the profile data
     * @param nvProfileData the nv profile data
     * @throws DaoException the dao exception
     */
    private void updateProfileGeography(NVProfileData profileData, NVProfileData nvProfileData) {
        if (profileData.getGeographyL4() != null && profileData.getGeographyL4().getId() != null) {
            GeographyL4 geographyL4 = geographyL4Dao.findByPk(profileData.getGeographyL4().getId());
            nvProfileData.setGeographyL4(geographyL4);
            nvProfileData.setGeographyL3(geographyL4.getGeographyL3());
            nvProfileData.setGeographyL2(geographyL4.getGeographyL3().getGeographyL2());
            nvProfileData.setGeographyL1(geographyL4.getGeographyL3().getGeographyL2().getGeographyL1());
        } else if (profileData.getGeographyL3() != null && profileData.getGeographyL3().getId() != null) {
            GeographyL3 geographyL3 = geographyL3Dao.findByPk(profileData.getGeographyL3().getId());
            nvProfileData.setGeographyL3(geographyL3);
            nvProfileData.setGeographyL2(geographyL3.getGeographyL2());
            nvProfileData.setGeographyL1(geographyL3.getGeographyL2().getGeographyL1());
        } else if (profileData.getGeographyL2() != null && profileData.getGeographyL2().getId() != null) {
            GeographyL2 geographyL2 = geographyL2Dao.findByPk(profileData.getGeographyL2().getId());
            nvProfileData.setGeographyL2(geographyL2);
            nvProfileData.setGeographyL1(geographyL2.getGeographyL1());
        } else if (profileData.getGeographyL1() != null && profileData.getGeographyL1().getId() != null) {
            nvProfileData.setGeographyL1(geographyL1Dao.findByPk(profileData.getGeographyL1().getId()));
        }
    }

    /**
     * Publish profile.
     *
     * @param profileId the profile id
     * @return the NV profile data
     * @throws RestException the rest exception
     */
    @Override
    @Transactional
    public void publishProfile(Integer profileId, Boolean status) {
        logger.info("Inside publishProfile");
        try {
            NVProfileData profileData = profileDataDao.findByPk(profileId);
            profileData.setStatus(status);
            profileData.setModificationTime(new Date());
            profileData = profileDataDao.update(profileData);
            updateProfileMap();
            logger.info("Done publishProfile with status : {}", profileData.isStatus());
        } catch (Exception e) {
            logger.error("Essception in publishProfile : {} ", ExceptionUtils.getStackTrace(e));
            throw new RestException(e.getMessage());
        }
    }

   
    @Override
//    @Transactional
	public String getDeviceProfile(String requestWrapper) {
		logger.info("Inside getUpdatedDeviceProfile");
		NVProfileData profile = null;
		NVProfileRequestWrapper wrapper = null;
		String operator = null;
		Boolean isGCMAlgo = null;
		try {
			isGCMAlgo = AuthenticationCommonUtil.isGCMAlgorithm(requestWrapper);
			wrapper = new Gson().fromJson(AuthenticationCommonUtil.checkForValueDecryption(requestWrapper),
					NVProfileRequestWrapper.class);
			logger.info("decrypted wrapper in getUpdatedDeviceProfile {}", wrapper);
			operator = getProfileRequestOperator(wrapper);
			if (wrapper.getModule() != null) {
				logger.error("{} current request wrapper {}", NVProfileConstants.INVALID_MCC_MNC_MESSAGE,
						wrapper.toString());
				profile = getPreDefineProfile(wrapper.getModule());
				if (!wrapper.getModule().toUpperCase().contains(NVConstant.KEY_PASSIVE)) {
					logger.error("Going to update NVDevice for module {} ", wrapper.getModule());
					deviceDataService.persistNVDeviceData(new NVDeviceDataWrapper(wrapper), null, operator);

				}

			} else {
				throw new RestException("Invalid Module");
			}
		} catch (Exception e) {
			logger.error("Exception in getUpdatedDeviceProfile : {} ", ExceptionUtils.getStackTrace(e));
			if (wrapper != null) {
				profile = getPreDefineProfile(wrapper.getModule());
			}
		}
		if (profile != null) {
			// deviceDataService.persistNVDeviceData(new
			// NVDeviceDataWrapper(wrapper),geographyArray,operator);
			NVProfileWrapper profileWrapper = new NVProfileWrapper(profile.getProfileId(),
					NVProfileDataUtils.parseNVProfile(profile.getProfile()));
			logger.info("Done getDeviceProfile for profile id {}", profileWrapper.getId());
			return AuthenticationCommonUtil.checkForValueEncryption(profileWrapper, isGCMAlgo);
		} else {
			throw new RestException("Profile not found");
		}
	}

    private String []getGeographies(NVProfileRequestWrapper wrapper){
    	logger.info("Going to getGeographies for latitude : {} ,longitude : {}",wrapper.getLatitude(),wrapper.getLongitude());
   	 String[] geographyArray=null;
		if (NVProfileDataUtils.isValidNumber(wrapper.getLatitude())
                && NVProfileDataUtils.isValidNumber(wrapper.getLongitude())) {
			String geographys=iGenericMapService.getAllGeographyName(wrapper.getLatitude(), wrapper.getLongitude());
			if (geographys != null) {
				geographys = geographys.replace("\"", "");
				geographys = geographys.replace("[", "");
				geographys = geographys.replace("]", "");
				geographyArray = geographys.split(Symbol.COMMA_STRING);
			}
        } else {
            SiteLocationInfo macroSiteDetail = getLocationFromMccMncAndCellId(wrapper.getMcc(), wrapper.getMnc(),
                    wrapper.getCellId());
            if (macroSiteDetail != null ) {
           	 geographyArray = getGeographiesFromNetworkElement(macroSiteDetail);
                updateLatLng(wrapper, macroSiteDetail);
            }
        }
		 logger.info("Done getGeographies {}",geographyArray!=null?Arrays.toString(geographyArray):null);
   	return geographyArray;
   }

    private void updateLatLng(NVProfileRequestWrapper wrapper, SiteLocationInfo macroSiteDetail) {
    	logger.info("update NVDevice Lat & Lng from Site Info");
        wrapper.setLatitude(macroSiteDetail.getLatitude());
        wrapper.setLongitude(macroSiteDetail.getLongitude());
    }

    /**
     * Gets the profile request operator.
     *
     * @param wrapper the wrapper
     * @return the profile request operator
     * @throws DaoException the dao exception
     */
	private String getProfileRequestOperator(NVProfileRequestWrapper wrapper) {
		String operator = null;
		try {
			if (NVUtilService.getMccMncOperatorMap() != null) {
				operator = NVUtilService.getMccMncOperatorMap().get(
						wrapper.getMcc() + Symbol.UNDERSCORE_STRING + wrapper.getMnc());
			}
		} catch (Exception e) {
			logger.error("Error in getDeviceOperator : {}", ExceptionUtils.getStackTrace(e));
		}
		logger.info("Operator for device : {} ,mcc : {} , mnc : {} ", operator, wrapper.getMcc(), wrapper.getMnc());
		return operator;
	}

    /**
     * Gets the published or pre define NV profile.
     *
     * @param geographies the geographies
     * @param module the module
     * @return the published or pre define NV profile
     * @throws RestException the rest exception
     */
    private NVProfileData getPublishedOrPreDefineNVProfile(List<String> geographies, String module) {
        logger.info("Geographies : {}",
                geographies != null ? new Gson().toJson(geographies) : NVProfileConstants.BLANK_STRING);
        NVProfileData profile = getPublishedProfile(geographies);
        if (profile == null) {
            profile = getPreDefineProfile(module);
        }
        if (profile != null) {
            logger.info("Got Profile Id : {} type :{}", profile.getId(), profile.getName());
        } else {
            logger.info("Got NULL Profile");
        }
        return profile;
    }

    /**
     * Gets the pre define profile.
     *
     * @param module the module
     * @return the pre define profile
     * @throws RestException the rest exception
     */
	private NVProfileData getPreDefineProfile(String module) {
		logger.info("Going getPreDefineProfile from  list for module : {}", module);
		if (NVUtilService.getPredefineProfileList() != null && !NVUtilService.getPredefineProfileList().isEmpty()) {
			Optional<NVProfileData> optional = NVUtilService.getPredefineProfileList().stream()
					.filter(n -> module.equalsIgnoreCase(n.getModule())).findFirst();
			if (optional.isPresent()) {
				logger.info("Returning profile id from predefineProfileList : {}", optional.get().getProfileId());
				return optional.get();
			} else {
				return NVUtilService.getPredefineProfileList().get(NVProfileConstants.INDEX_ZERO);
			}
		} else {
			logger.error("PRE_DEFINE_PROFILE not exist for module : {}", module);
			return null;
		}
	}
	
    /**
     * Gets the published profile.
     *
     * @param geographies the geographies
     * @return the published profile
     */
	private NVProfileData getPublishedProfile(List<String> geographies) {
		NVProfileData nvProfile = null;
		logger.info("getPublishedProfilemap {}", nvProfileMap != null ? nvProfileMap.keySet() : null);
		if (nvProfileMap == null||geographies==null||geographies.isEmpty()||nvProfileMap.isEmpty()) {
			return null;
		}
		// Need to optimize
		try {
			for (String geo : geographies) {
				for (Map.Entry<String, NVProfileData> entry : nvProfileMap.entrySet()) {
					logger.debug("map key:{}", entry.getKey());
					if (entry	.getKey()
								.equalsIgnoreCase(geo)) {
						nvProfile = entry.getValue();
						logger.info("Profile id to return {}", nvProfile.getProfileId());
						break;
					}
				}
				if (nvProfile != null) {
					break;
				}

			}
		} catch (Exception e) {
			logger.error("Exception in getPublishedProfile {}", Utils.getStackTrace(e));
		}

		return nvProfile;
	}

    /**
     * Gets the location from mcc mnc and cell id.
     *
     * @param mcc the mcc
     * @param mnc the mnc
     * @param cellId the cell id
     * @return the location from mcc mnc and cell id
     */
    private SiteLocationInfo getLocationFromMccMncAndCellId(Integer mcc, Integer mnc, Integer cellId) {
        logger.info("Inside getLocationFromMccMncAndCellId(), Getting mcc {}, mnc {}, cellId {}", mcc, mnc, cellId);
        if (NVUtilService.getLocationMap()!=null&&MapUtils.isNotEmpty(NVUtilService.getLocationMap())) {
            String stringMnc = StringUtils.leftPad(String.valueOf(mnc), ForesightConstants.TWO, ForesightConstants.ZERO_IN_STRING);
            String stringCellId = StringUtils.leftPad(String.valueOf(cellId), ForesightConstants.NINE, ForesightConstants.ZERO_IN_STRING);
            String key = mcc + stringMnc + stringCellId;
            logger.info("Getting key {}", key);
            return NVUtilService.getLocationMap().get(key);
        }
        return null;
    }

    /**
     * Gets the geographies for lat lng.
     *
     * @param latitude the latitude
     * @param longitude the longitude
     * @param module the module
     * @param operator the operator
     * @return the geographies for lat lng
     * @throws RestException the rest exception
     */
    private List<String>getProfileMapKey(String[]geographyArray,String module,String operator){
    	List<String> geographies = new ArrayList<>();
    	if(geographyArray!=null) {
    	  try {
              geographies.add(geographyArray[ForesightConstants.INDEX_THREE]
            		  + NVProfileConstants.PROFILE_MAP_KEY_DELIMITER
                      + NVProfileConstants.GEOGRAPHYL4 + NVProfileConstants.PROFILE_MAP_KEY_DELIMITER + module
                      + NVProfileConstants.PROFILE_MAP_KEY_DELIMITER + operator);
              geographies.add(geographyArray[ForesightConstants.INDEX_TWO]
                      + NVProfileConstants.PROFILE_MAP_KEY_DELIMITER + NVProfileConstants.GEOGRAPHYL3
                      + NVProfileConstants.PROFILE_MAP_KEY_DELIMITER + module
                      + NVProfileConstants.PROFILE_MAP_KEY_DELIMITER + operator);
              geographies.add(geographyArray[ForesightConstants.INDEX_ONE]
                      + NVProfileConstants.PROFILE_MAP_KEY_DELIMITER + NVProfileConstants.GEOGRAPHYL2
                      + NVProfileConstants.PROFILE_MAP_KEY_DELIMITER + module
                      + NVProfileConstants.PROFILE_MAP_KEY_DELIMITER + operator);
              geographies.add(geographyArray[ForesightConstants.INDEX_ZERO]
                      + NVProfileConstants.PROFILE_MAP_KEY_DELIMITER + NVProfileConstants.GEOGRAPHYL1
                      + NVProfileConstants.PROFILE_MAP_KEY_DELIMITER + module
                      + NVProfileConstants.PROFILE_MAP_KEY_DELIMITER + operator);
              geographies.add(NVProfileConstants.PROFILE_MAP_KEY_DELIMITER + NVProfileConstants.PAN_GEOGRAPHY
                      + NVProfileConstants.PROFILE_MAP_KEY_DELIMITER + module
                      + NVProfileConstants.PROFILE_MAP_KEY_DELIMITER + operator);
          } catch (Exception e) {
              logger.error("Exception in getProfileMapKey : {} ", ExceptionUtils.getStackTrace(e));
              throw new RestException(e.getMessage());
          }
    	}
    	return geographies;
    }

   
	private String [] getGeographiesFromNetworkElement(SiteLocationInfo networkElement) {
    	logger.info("Going to getGeographiesForNetworkElement from networkElement {} ",networkElement.toString());
    	String [] geographyArray = new String[ForesightConstants.INDEX_FOUR];
        try {
        	geographyArray[ForesightConstants.INDEX_ZERO]=networkElement.getGeographyL1Name();
        	geographyArray[ForesightConstants.INDEX_ONE]=networkElement.getGeographyL2Name();
        	geographyArray[ForesightConstants.INDEX_TWO]=networkElement.getGeographyL3Name();
        	geographyArray[ForesightConstants.INDEX_THREE]=networkElement.getGeographyL4Name();
        } catch (Exception e) {
            logger.error("Exception in getGeographiesFromNetworkElement : {} ", ExceptionUtils.getStackTrace(e));
        }
		return geographyArray;
    }

    /**
     * Gets the all device make.
     *
     * @return the all device make
     * @throws RestException the rest exception
     */
    @Override
    public List<String> getAllDeviceMake() {
        logger.info("Inside getAllDeviceMake");
        try {
            List<String> makes = deviceInfoDao.getDistinctMake();
            logger.info("Done getAllDeviceMake");
            return makes;
        } catch (Exception e) {
            logger.error("Essception in getAllDeviceMake : {} ", ExceptionUtils.getStackTrace(e));
            throw new RestException(e.getMessage());
        }
    }

    /**
     * Gets the all device model.
     *
     * @param make the make
     * @return the all device model
     * @throws RestException the rest exception
     */
    @Override
    public List<String> getAllDeviceModel(String make) {
        logger.info("Inside getAllDeviceModel");
        try {
            List<String> models = deviceInfoDao.getDistinctModel(make);
            logger.info("Done getAllDeviceModel");
            return models;
        } catch (Exception e) {
            logger.error("Essception in getAllDeviceModel : {} ", ExceptionUtils.getStackTrace(e));
            throw new RestException(e.getMessage());
        }
    }

    /**
     * Gets the all device OS.
     *
     * @return the all device OS
     * @throws RestException the rest exception
     */
    @Override
    public List<String> getAllDeviceOS() {
        logger.info("Inside getAllDeviceOS");
        try {
            List<String> os = deviceInfoDao.getDistinctOS();
            logger.info("Done getAllDeviceOS");
            return os;
        } catch (Exception e) {
            logger.error("Essception in getAllDeviceOS : {} ", ExceptionUtils.getStackTrace(e));
            throw new RestException(e.getMessage());
        }
    }

    /**
     * Gets the all operator.
     *
     * @return the all operator
     * @throws RestException the rest exception
     */
    @Override
    public Set<String> getAllOperator() {
        logger.info("Inside getAllOperator");
        try {
            SystemConfiguration configuration = systemConfigurationDao
                    .getSystemConfigurationByNameAndType(NVProfileConstants.OPERATOR,
                            Arrays.asList(NVProfileConstants.NV_PROFILE_DATA))
                    .get(NVProfileConstants.INDEX_ZERO);
            Map<String, String> operators = new Gson().fromJson(configuration.getValue(),
                    new TypeToken<Map<String, String>>() {}.getType());
            logger.info("Done getAllOperator");
            return new HashSet<>(operators.values());
        } catch (Exception e) {
            logger.error("Essception in getAllOperator : {} ", ExceptionUtils.getStackTrace(e));
            throw new RestException(e.getMessage());
        }
    }

    /**
     * Gets the all technology.
     *
     * @return the all technology
     * @throws RestException the rest exception
     */
    @Override
    public List<String> getAllTechnology() {
        logger.info("Inside getAllTechnology");
        try {
            SystemConfiguration configuration = systemConfigurationDao
                    .getSystemConfigurationByNameAndType(NVProfileConstants.TECHNOLOGY,
                            Arrays.asList(NVProfileConstants.NV_PROFILE_DATA))
                    .get(NVProfileConstants.INDEX_ZERO);
            List<String> technologies = new Gson().fromJson(configuration.getValue(),
                    new TypeToken<List<String>>() {}.getType());
            logger.info("Done getAllTechnology");
            return technologies;
        } catch (Exception e) {
            logger.error("Essception in getAllTechnology : {} ", ExceptionUtils.getStackTrace(e));
            throw new RestException(e.getMessage());
        }
    }

    /**
     * Find profile data.
     *
     * @param id the id
     * @return the NV profile data wrapper
     * @throws RestException the rest exception
     */
    @Override
    public NVProfileDataWrapper findProfileData(Integer id) {
        logger.info("Inside findProfileData");
        try {
            NVProfileData nvProfileData = profileDataDao.findByPk(id);
            NVProfileDataWrapper profileDataWrapper = new NVProfileDataWrapper(nvProfileData);
            logger.info("Done findProfileData");
            return profileDataWrapper;
        } catch (Exception e) {
            logger.error("Essception in findProfileData : {} ", ExceptionUtils.getStackTrace(e));
            throw new RestException(e.getMessage());
        }
    }
    
    /**
     * Update profile map.
     *
     * @param isUpdateRequired the is update required
     */
   
    private void updateProfileMap(boolean isUpdateRequired) {
        try {
            if (MapUtils.isEmpty(nvProfileMap) || isUpdateRequired) {
                List<NVProfileData> profileDatas = profileDataDao.getAllValidProfiles();
                nvProfileMap = profileDatas.stream()
                        .collect(Collectors.toMap(p -> getGeographyByProfileLevel(p), p -> p));
                logger.info("DONE updateProfileMap size :{}",nvProfileMap.size());
            }
        } catch (Exception e) {
            logger.error("Exception in updateProfileMap : {} ", ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * Gets the geography by profile level.
     *
     * @param profile the profile
     * @return the geography by profile level
     */
    private String getGeographyByProfileLevel(NVProfileData profile) {
        String geograhy = null;
        switch (profile.getLevel()) {
            case NVProfileConstants.GEOGRAPHYL1:
                geograhy = profile.getGeographyL1().getName();
                break;
            case NVProfileConstants.GEOGRAPHYL2:
                geograhy = profile.getGeographyL2().getName();
                break;
            case NVProfileConstants.GEOGRAPHYL3:
                geograhy = profile.getGeographyL3().getName();
                break;
            case NVProfileConstants.GEOGRAPHYL4:
                geograhy = profile.getGeographyL4().getName();
                break;
            case NVProfileConstants.PAN_GEOGRAPHY:
                geograhy = NVProfileConstants.BLANK_STRING;
                break;
            default:
                geograhy = NVProfileConstants.BLANK_STRING;
                break;
        }
        return geograhy + NVProfileConstants.PROFILE_MAP_KEY_DELIMITER + profile.getLevel()
                + NVProfileConstants.PROFILE_MAP_KEY_DELIMITER + profile.getModule()
                + NVProfileConstants.PROFILE_MAP_KEY_DELIMITER + profile.getOperator();
    }

    @Override
    @Transactional
    public void updateProfileMap() {
        try {
            updateProfileMap(Boolean.TRUE);
        } catch (Exception e) {
            logger.error("Essception in updateProfileMap : {} ", ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * Gets the profile by type and module.
     *
     * @param type the type
     * @param module the module
     * @return the profile by type and module
     * @throws RestException the rest exception
     */
    @Override
    public List<NVProfileData> getProfileByTypeAndModule(String type, String module) {
        logger.info("Inside getProfileByTypeAndModule type : {} , module : {}",type,module);
        try {
            List<NVProfileData> profiles = profileDataDao.getProfileByTypeAndModule(type, module);
            logger.info("Done getProfileByTypeAndModule size : {}", profiles.size());
            return profiles;
        } catch (Exception e) {
            logger.error("Exception in Service getProfileByTypeAndModule : {} ", ExceptionUtils.getStackTrace(e));
            throw new RestException(e.getMessage());
        }
    }

    /**
     * Delete profile.
     *
     * @param id the id
     * @throws RestException the rest exception
     */
    @Override
    @Transactional
    public void deleteProfile(Integer id) {
        logger.info("Inside deleteProfile");
        try {
            NVProfileData profileData = profileDataDao.findByPk(id);
            profileData.setDeleted(Boolean.TRUE);
            profileData.setModificationTime(new Date());
            profileDataDao.update(profileData);
            updateProfileMap();
            logger.info("Done deleteProfile");
        } catch (Exception e) {
            logger.error("Exception in Service deleteProfile : {} ", ExceptionUtils.getStackTrace(e));
            throw new RestException(e.getMessage());
        }
    }

    /**
     * Gets the NV profile by profile id.
     *
     * @param profileId the profile id
     * @return the NV profile by profile id
     * @throws RestException the rest exception
     */
    @Override
    public NVProfileData getNVProfileByProfileId(String profileId) {
        NVProfileData nvProfileData = null;
        try {
            nvProfileData = profileDataDao.getNVProfileByProfileId(profileId);
        } catch (DaoException e) {
            logger.error("DaoException in getNVProfileByProfileId : {} ", ExceptionUtils.getStackTrace(e));
        }
        return nvProfileData;
    }
}

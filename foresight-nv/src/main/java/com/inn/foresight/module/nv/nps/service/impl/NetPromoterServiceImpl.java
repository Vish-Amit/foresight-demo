
package com.inn.foresight.module.nv.nps.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.DateUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.maplayer.service.IGenericMapService;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;
import com.inn.foresight.module.nv.nps.constants.NetPromoterConstant;
import com.inn.foresight.module.nv.nps.dao.INetPromoterDao;
import com.inn.foresight.module.nv.nps.dao.INetPromoterRawDao;
import com.inn.foresight.module.nv.nps.model.NPSRawDetail;
import com.inn.foresight.module.nv.nps.model.NetPromoterWrapper;
import com.inn.foresight.module.nv.nps.service.INetPromoterService;
import com.inn.foresight.module.nv.nps.utils.NetPromoterUtil;
import com.inn.product.security.utils.AuthenticationCommonUtil;
import com.inn.product.um.geography.model.GeographyL4;
import com.inn.product.um.geography.service.GeographyL4Service;

/**
 * The Class NetPromoterServiceImpl.
 * 
 * @author innoeye
 */

@Service("NetPromoterServiceImpl")
public class NetPromoterServiceImpl implements INetPromoterService {

    /** The logger. */
    private Logger logger = LogManager.getLogger(NetPromoterServiceImpl.class);

    /** The i net promoter raw dao. */
    @org.springframework.beans.factory.annotation.Autowired
    INetPromoterRawDao iNetPromoterRawDao;

    /** The i net promoter dao. */
    @Autowired
    INetPromoterDao iNetPromoterDao;

    /** The service. */
    @Autowired
    private GeographyL4Service iGeographyL4Service;

    /** The i generic map service. */
    @Autowired
    private IGenericMapService iGenericMapService;


    /**
     * (non-Javadoc)
     * 
     * @see com.inn.foresight.module.nv.nps.service.INetPromoterService#
     * persistNPSData( java.lang.String)
     */
    @Override
    @Transactional
    public NPSRawDetail persistNPSData(String npsJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            NPSRawDetail returnWrapper = new NPSRawDetail();
            String decryptedJson = getDecryptedJson(npsJson);

            NPSRawDetail[] jsonToArray = objectMapper.readValue(decryptedJson, NPSRawDetail[].class);
            if (jsonToArray != null && jsonToArray.length > ForesightConstants.ZERO) {
                logger.info("wrapper List size {} " + jsonToArray.length);
                for (NPSRawDetail wrapper : jsonToArray) {
                    logger.info("wrapper List size {} " + wrapper);

                    updateBooleanValueInWrapper(wrapper);
                    if (Utils.hasValue(wrapper.getLatitude()) && Utils.hasValue(wrapper.getLongitude())) {
                        wrapper = tagGeographyL4(wrapper);
                        returnWrapper = iNetPromoterRawDao.createNPSRawData(wrapper);
                    }
                }
                return returnWrapper;
            } else {
                return null;
            }
        } catch (DaoException e) {
            logger.error("error in persistNPSData{}", ExceptionUtils.getStackTrace(e));
            throw new RestException(ExceptionUtils.getStackTrace(e));
        } catch (Exception e) {
            logger.error("error in persistNPSDataerror = {}", ExceptionUtils.getStackTrace(e));

            throw new RestException(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     * @throws InvalidEncryptionException
     */
    

    /**
     * Tag geography L 4.
     *
     * @param wrapper
     *            the wrapper
     * @return the NPS raw detail
     */
    private NPSRawDetail tagGeographyL4(NPSRawDetail wrapper) {
        wrapper.setGeographyL4(getGeographyL4(wrapper));
        wrapper.setIngestdate(DateUtil.parseDateToString(ForesightConstants.DATE_FORMAT_yyyy_MM_dd, new Date()));
        return wrapper;
    }

    /**
     * Get GeographyL4.
     *
     * @param wrapper
     *            the wrapper
     * @return the geography L 4
     */
    public GeographyL4 getGeographyL4(NPSRawDetail wrapper) {
        try {
            String geographyL4Name = getGeographyName(wrapper);
            logger.info("geographyL4Name:{}", geographyL4Name);
            return getGeographyL4Name(geographyL4Name);
        } catch (Exception e) {
            logger.error("Getting Exception in tagging geography, error = {}", ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    private GeographyL4 getGeographyL4Name(String geographyL4Name) {
        try {
            if (geographyL4Name == null || "".equalsIgnoreCase(geographyL4Name)) {
                throw new RestException(ForesightConstants.GEOGRAPHY_NOT_FOUND);
            }
            return iGeographyL4Service.getGeographyL4ByName(geographyL4Name);
        } catch (RestException e) {
            logger.error("Getting Exception getGeographyL4Name error = {}", ExceptionUtils.getStackTrace(e));
            throw new RestException(ExceptionUtils.getMessage(e));
        }
    }

    /**
     * Gets the geography name.
     *
     * @param wrapper
     *            the wrapper
     * @return the geography name
     * @throws RestException
     *             the rest exception
     */
    @SuppressWarnings("serial")
    private String getGeographyName(NPSRawDetail wrapper) {
        String geograpghy = iGenericMapService.getGeographyDataByPoint(GenericMapUtils.getGeoColumnList(),
                GenericMapUtils.GEOGRAPHY_TABLE_NAME, wrapper.getLatitude(), wrapper.getLongitude(), false,
                GenericMapUtils.L4_TYPE);
        List<List<String>> list;
        list = new Gson().fromJson(geograpghy, new TypeToken<List<List<String>>>() {
        }.getType());
        if (list.isEmpty()) {
            throw new RestException(ForesightConstants.GEOGRAPHY_NOT_FOUND);
        }
        return list.get(0).get(0);
    }

    /**
     * Gets the decrypted json.
     *
     * @param encryptedString
     *            the encrypted string
     * @return the decrypted json
     * @throws RestException
     *             the rest exception
     */
    private String getDecryptedJson(String encryptedString) {
        String decryptedJson;
        try {
            decryptedJson = AuthenticationCommonUtil.checkForValueDecryption(encryptedString);
        } catch (Exception e) {
            logger.error("error in decrypting input string, error = {}", ExceptionUtils.getStackTrace(e));
            throw new RestException(ExceptionUtils.getMessage(e));
        }
        return decryptedJson;
    }

    /**
     * Update boolean value in wrapper.
     *
     * @param wrapper
     *            the wrapper
     * @return the NPS raw detail
     */
    private static NPSRawDetail updateBooleanValueInWrapper(NPSRawDetail wrapper) {
        if (wrapper.getIsAutoDateTimeEnabled() != null && Boolean.toString(wrapper.getIsAutoDateTimeEnabled())
                .equalsIgnoreCase(ForesightConstants.TRUE_LOWERCASE)) {
            wrapper.setAutodatetimeenabled(ForesightConstants.ONE);
        } else {
            wrapper.setAutodatetimeenabled(ForesightConstants.ZERO_VALUE);
        }
        if (wrapper.getIsEnterprise() != null
                && Boolean.toString(wrapper.getIsEnterprise()).equalsIgnoreCase(ForesightConstants.TRUE_LOWERCASE)) {
            wrapper.setEnterprise(ForesightConstants.ONE);
        } else {
            wrapper.setEnterprise(ForesightConstants.ZERO_VALUE);
        }
        if (wrapper.getIsGpsEnabled() != null
                && Boolean.toString(wrapper.getIsGpsEnabled()).equalsIgnoreCase(ForesightConstants.TRUE_LOWERCASE)) {
            wrapper.setGpsenabled(ForesightConstants.ONE);
        } else {
            wrapper.setGpsenabled(ForesightConstants.ZERO_VALUE);
        }
        if (wrapper.getIsLayer3Enabled() != null
                && Boolean.toString(wrapper.getIsLayer3Enabled()).equalsIgnoreCase(ForesightConstants.TRUE_LOWERCASE)) {
            wrapper.setLayer3enabled(ForesightConstants.ONE);
        } else {
            wrapper.setLayer3enabled(ForesightConstants.ZERO_VALUE);
        }
        if (wrapper.getIsPhoneDualSim() != null
                && Boolean.toString(wrapper.getIsPhoneDualSim()).equalsIgnoreCase(ForesightConstants.TRUE_LOWERCASE)) {
            wrapper.setDualsim(ForesightConstants.ONE);
        } else {
            wrapper.setDualsim(ForesightConstants.ZERO_VALUE);
        }
        return wrapper;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.inn.foresight.module.nv.nps.service.INetPromoterService#
     * getNPSEventScore( java.lang.Integer, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String getNPSEventScore(Integer geographyId, String geographyType, String startDate, String endDate,
            String operator, String technology) {
        int weekNumber = ForesightConstants.ZERO;
        logger.info("==inside getNPSEventScore===");
        List<NetPromoterWrapper> listOfData = new ArrayList<>();
        Map<String, Map<String, Object>> mapForNPSEventScore = new HashMap<>();
        try {
            if (geographyId != null && geographyType != null && startDate != null && endDate != null && operator != null
                    && technology != null) {
                Map<String, Map<String, Object>> map = new HashMap<>();
                Map<String, Object> callEventMap = new HashMap<>();
                Map<String, Object> speedTestMap = new HashMap<>();
                if (!(startDate.equals(endDate))) {
                    weekNumber = NetPromoterUtil.generateWeekNumber(startDate);
                    listOfData = iNetPromoterDao.getNPSEventScoreData(geographyId, geographyType, startDate, endDate,
                            weekNumber, operator, technology);
                } else {
                    listOfData = iNetPromoterDao.getNPSEventScoreData(geographyId, geographyType, startDate, endDate,
                            weekNumber, operator, technology);
                }

                if (!listOfData.isEmpty()) {
                    mapForNPSEventScore = getValuesFromNetPromoterWrapper(listOfData, map, callEventMap, speedTestMap);
                }

            }

        } catch (Exception e) {
            logger.error("error in getNPSEventScore error = {}", ExceptionUtils.getStackTrace(e));
        }

        return new JSONObject(mapForNPSEventScore).toString();
    }

    /**
     * Gets the values from net promoter wrapper.
     *
     * @param listOfData
     *            the list of data
     * @param map
     *            the map
     * @param callEventMap
     *            the call event map
     * @param speedTestMap
     *            the speed test map
     * @return the values from net promoter wrapper
     */
    private Map<String, Map<String, Object>> getValuesFromNetPromoterWrapper(List<NetPromoterWrapper> listOfData,
            Map<String, Map<String, Object>> map, Map<String, Object> callEventMap, Map<String, Object> speedTestMap) {
        for (NetPromoterWrapper netPromoterWrapper : listOfData) {

            if (netPromoterWrapper.getEventType() != null
                    && netPromoterWrapper.getEventType().equals(NetPromoterConstant.CALL_EVENT)) {
                putEventValuesInMap(callEventMap, netPromoterWrapper);
            } else if (netPromoterWrapper.getEventType() != null
                    && netPromoterWrapper.getEventType().equals(NetPromoterConstant.SPEED_TEST)) {
                putEventValuesInMap(speedTestMap, netPromoterWrapper);
            }
        }
        getMapIfNoValueFound(callEventMap, speedTestMap);
        try {
            calculateNPS(speedTestMap, callEventMap);
        } catch (Exception e) {
            logger.error("error in getValuesFromNetPromoterWrapper error = {}", ExceptionUtils.getStackTrace(e));
        }
        map.put(NetPromoterConstant.EVENT2, speedTestMap);
        map.put(NetPromoterConstant.EVENT1, callEventMap);
        Gson gson = new Gson();
        String json = gson.toJson(map);

        logger.info("====json====={}", json);
        return map;

    }

    /**
     * Gets the map if no value found.
     *
     * @param callEventMap
     *            the call event map
     * @param speedTestMap
     *            the speed test map
     * @return the map if no value found
     */
    private void getMapIfNoValueFound(Map<String, Object> callEventMap, Map<String, Object> speedTestMap) {
        if (speedTestMap.isEmpty()) {
            speedTestMap.put(NetPromoterConstant.NAME, NetPromoterConstant.SPEED_TEST);
            speedTestMap.put(NetPromoterConstant.DETRACTOR_COUNT, null);
            speedTestMap.put(NetPromoterConstant.PASSIVE_COUNT, null);
            speedTestMap.put(NetPromoterConstant.PROMOTER_COUNT, null);
            speedTestMap.put(NetPromoterConstant.TOTAL_COUNT, null);
        }
        if (callEventMap.isEmpty()) {
            callEventMap.put(NetPromoterConstant.NAME, NetPromoterConstant.CALL_EVENT);
            callEventMap.put(NetPromoterConstant.DETRACTOR_COUNT, null);
            callEventMap.put(NetPromoterConstant.PASSIVE_COUNT, null);
            callEventMap.put(NetPromoterConstant.PROMOTER_COUNT, null);
            callEventMap.put(NetPromoterConstant.TOTAL_COUNT, null);
        }
    }

    /**
     * Calculate NPS.
     *
     * @param speedTestMap
     *            the speed test map
     * @param callEventMap
     *            the call event map
     */
    public void calculateNPS(Map<String, Object> speedTestMap, Map<String, Object> callEventMap) {
        if (getTotalCount(speedTestMap) > ForesightConstants.ZERO) {
            Double totalCount = getTotalCount(speedTestMap);
            getNPS(speedTestMap, totalCount);
        }
        if (getTotalCount(callEventMap) > ForesightConstants.ZERO) {
            Double totalCount = getTotalCount(callEventMap);
            getNPS(callEventMap, totalCount);
        }

    }

    /**
     * Gets the nps.
     *
     * @param eventMap
     *            the event map
     * @param totalCount
     *            the total count
     * @return the nps
     */
    public void getNPS(Map<String, Object> eventMap, Double totalCount) {
        Double pramoterPer = NumberUtils.DOUBLE_ZERO;
        Double detacterPer = NumberUtils.DOUBLE_ZERO;

        if (eventMap.get(NetPromoterConstant.PROMOTER_COUNT) != null) {
            pramoterPer = (new Double(eventMap.get(NetPromoterConstant.PROMOTER_COUNT).toString()) / totalCount) * ForesightConstants.HUNDRED;
        } else {
            eventMap.put(NetPromoterConstant.PROMOTER_COUNT, null);
        }
        if (eventMap.get(NetPromoterConstant.DETRACTOR_COUNT) != null) {
            detacterPer = (new Double(eventMap.get(NetPromoterConstant.DETRACTOR_COUNT).toString()) / totalCount) * ForesightConstants.HUNDRED;
        } else {
            eventMap.put(NetPromoterConstant.DETRACTOR_COUNT, null);
        }
        if (eventMap.get(NetPromoterConstant.PASSIVE_COUNT) == null) {
            eventMap.put(NetPromoterConstant.PASSIVE_COUNT, null);
        }

        if (detacterPer > NumberUtils.DOUBLE_ZERO || pramoterPer > NumberUtils.DOUBLE_ZERO) {
            eventMap.put(NetPromoterConstant.NPS, (pramoterPer - detacterPer));
            eventMap.put(NetPromoterConstant.TOTAL_COUNT, totalCount);
        } else {
            eventMap.put(NetPromoterConstant.NPS, null);
            eventMap.put(NetPromoterConstant.TOTAL_COUNT, totalCount);

        }

    }

    /**
     * Gets the total count.
     *
     * @param eventMap
     *            the event map
     * @return the total count
     */
    public Double getTotalCount(Map<String, Object> eventMap) {
        Double totalCount = NumberUtils.DOUBLE_ZERO;
        if (eventMap.get(NetPromoterConstant.DETRACTOR_COUNT) != null) {
            totalCount = totalCount + new Double(eventMap.get(NetPromoterConstant.DETRACTOR_COUNT).toString());
        }
        if (eventMap.get(NetPromoterConstant.PASSIVE_COUNT) != null) {
            totalCount = totalCount + new Double(eventMap.get(NetPromoterConstant.PASSIVE_COUNT).toString());
        }
        if (eventMap.get(NetPromoterConstant.PROMOTER_COUNT) != null) {
            totalCount = totalCount + new Double(eventMap.get(NetPromoterConstant.PROMOTER_COUNT).toString());
        }
        return totalCount;

    }

    /**
     * Gets the values for call end event.
     *
     * @param mapForEvent
     *            the map for event
     * @param netPromoterWrapper
     *            the net promoter wrapper
     * @return the values for call end event
     */

    /**
     * Gets the values for data on event.
     *
     * @param dataOnMap
     *            the data on map
     * @param netPromoterWrapper
     *            the net promoter wrapper
     * @return the values for data on event
     */

    /**
     * Put event values in map.
     *
     * @param mapForEvent
     *            the map for event
     * @param netPromoterWrapper
     *            the net promoter wrapper
     */
    private void putEventValuesInMap(Map<String, Object> mapForEvent, NetPromoterWrapper netPromoterWrapper) {
        mapForEvent.put(NetPromoterConstant.NAME, netPromoterWrapper.getEventType());
        if (netPromoterWrapper.getCustomerType().equalsIgnoreCase(NetPromoterConstant.DETRACTOR)) {
            mapForEvent.put(NetPromoterConstant.DETRACTOR_COUNT, netPromoterWrapper.getCustomerCount());
        } else if (netPromoterWrapper.getCustomerType().equalsIgnoreCase(NetPromoterConstant.PASSIVE)) {
            mapForEvent.put(NetPromoterConstant.PASSIVE_COUNT, netPromoterWrapper.getCustomerCount());
        } else if (netPromoterWrapper.getCustomerType().equalsIgnoreCase(NetPromoterConstant.PROMOTER)) {
            mapForEvent.put(NetPromoterConstant.PROMOTER_COUNT, netPromoterWrapper.getCustomerCount());
        }

    }

    /**
     * (non-Javadoc)
     * 
     * @see com.inn.foresight.module.nv.nps.service.INetPromoterService#
     * getNPSMonthlyAnalysisDetail(java.lang.Integer, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String getNPSMonthlyAnalysisDetail(Integer geographyId, String geographyType, String startDate,
            String operator, String technology) {
        logger.info("======inside getNPSMonthlyAnalysisDetail==");
        Map<String, String> mapForNPSMonthlyAnalysis = new HashMap<>();
        String endDate = NetPromoterUtil.getEndDate(startDate, NetPromoterConstant.LAST_30_DAYS);

        try {
            if (geographyId != null && geographyType != null && startDate != null && operator != null
                    && technology != null && endDate != null) {
                List<NetPromoterWrapper> listOfData = iNetPromoterDao.getNPSMonthlyAnalysisDetail(geographyId,
                        geographyType, startDate, endDate, operator, technology);

                Map<String, List<Long>> mapOfDate = getDateWiseMap(listOfData);
                mapForNPSMonthlyAnalysis = getMonthlyNPSData(mapOfDate, startDate);
            }

        } catch (Exception e) {
            logger.error("error in getNPSMonthlyAnalysisDetail error = {}", ExceptionUtils.getStackTrace(e));
        }

        return new JSONObject(mapForNPSMonthlyAnalysis).toString();
    }

    /**
     * Gets the final map.
     *
     * @param mapOfDate
     *            the map of date
     * @param startDate
     *            the start date
     * @return the final map
     */
    public Map<String, String> getMonthlyNPSData(Map<String, List<Long>> mapOfDate, String startDate) {

        Map<String, String> finalMap = new HashMap<>();
        Map<String, Long> datesContsantMap = NetPromoterUtil.get30daysDateConstantMap(startDate);
        for (Entry<String, Long> entry : datesContsantMap.entrySet()) {
            Long value = entry.getValue();
            if (mapOfDate.get(entry.getKey()) != null) {
                Double nps = generateNPSValue(mapOfDate.get(entry.getKey()));
                if (nps != null) {
                    finalMap.put(String.valueOf(value), String.valueOf(nps));
                } else {
                    finalMap.put(String.valueOf(value), null);
                }
            }

            else {
                finalMap.put(String.valueOf(value), null);
            }
        }

        return finalMap;
    }

    /**
     * Gets the date wise map.
     *
     * @param listOfData
     *            the list of data
     * @return the date wise map
     */
    private Map<String, List<Long>> getDateWiseMap(List<NetPromoterWrapper> listOfData) {
        Map<String, List<Long>> mapOfCustomerType = new HashMap<>();
        for (NetPromoterWrapper netPromoterWrapper : listOfData) {
            createListOfDates(mapOfCustomerType, netPromoterWrapper);

        }

        return mapOfCustomerType;
    }

    /**
     * Creates the list of dates.
     *
     * @param mapOfCustomerType
     *            the map of customer type
     * @param netPromoterWrapper
     *            the net promoter wrapper
     */
    private void createListOfDates(Map<String, List<Long>> mapOfCustomerType, NetPromoterWrapper netPromoterWrapper) {
        if (mapOfCustomerType.get(netPromoterWrapper.getProcessDate()) != null) {
            List<Long> oldCustomerList = mapOfCustomerType.get(netPromoterWrapper.getProcessDate());
            setCustomerTypeInList(netPromoterWrapper, oldCustomerList);
            mapOfCustomerType.put(netPromoterWrapper.getProcessDate(), oldCustomerList);
        } else {
            List<Long> newCustomerList = Arrays.asList(new Long[NetPromoterConstant.SIZE]);
            setCustomerTypeInList(netPromoterWrapper, newCustomerList);
            mapOfCustomerType.put(netPromoterWrapper.getProcessDate(), newCustomerList);
        }
    }

    /**
     * Sets the customer type in list.
     *
     * @param netPromoterWrapper
     *            the net promoter wrapper
     * @param oldCustomerList
     *            the old customer list
     */
    private void setCustomerTypeInList(NetPromoterWrapper netPromoterWrapper, List<Long> oldCustomerList) {
        if (netPromoterWrapper.getCustomerType().equalsIgnoreCase(NetPromoterConstant.PROMOTER)) {
            oldCustomerList.set(NetPromoterConstant.INDEX_0, netPromoterWrapper.getCustomerCount());
        } else if (netPromoterWrapper.getCustomerType().equalsIgnoreCase(NetPromoterConstant.PASSIVE)) {
            oldCustomerList.set(NetPromoterConstant.INDEX_1, netPromoterWrapper.getCustomerCount());
        } else if (netPromoterWrapper.getCustomerType().equalsIgnoreCase(NetPromoterConstant.DETRACTOR)) {
            oldCustomerList.set(NetPromoterConstant.INDEX_2, netPromoterWrapper.getCustomerCount());
        }
    }

    /**
     * Generate NPS value.
     *
     * @param listOfCustomerCount
     *            the list of customer count
     * @return the double
     */

    
    public static Double generateNPSValue(List<Long> listOfCustomerCount) {
        Double promoterPer = NumberUtils.DOUBLE_ZERO;
        Double dectraterPer = NumberUtils.DOUBLE_ZERO;
        Double totalCount = (double) listOfCustomerCount.stream().filter(Objects::nonNull).mapToLong(Long::longValue)
                .sum();
        if (listOfCustomerCount.get(NetPromoterConstant.INDEX_2) != null) {
            dectraterPer = (listOfCustomerCount.get(NetPromoterConstant.INDEX_2) / totalCount) * ForesightConstants.HUNDRED;
        }
        if (listOfCustomerCount.get(NetPromoterConstant.INDEX_0) != null) {
            promoterPer = (listOfCustomerCount.get(NetPromoterConstant.INDEX_0) / totalCount) * ForesightConstants.HUNDRED;
        }
        if (NumberUtils.DOUBLE_ZERO.equals(promoterPer) || NumberUtils.DOUBLE_ZERO.equals(dectraterPer)) {
            return promoterPer - dectraterPer;
        }
        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.inn.foresight.module.nv.nps.service.INetPromoterService#getNPSData(
     * java.lang.Integer, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public String getNPSData(Integer geographyId, String geographyType, String startDate, String callType,
            String operator, String technology) {

        Map<String, Map<String, Object>> mapForCustomerTypesData = new HashMap<>();
        logger.info("==inside getNPSData===");
        String endDate = NetPromoterUtil.getEndDate(startDate, NetPromoterConstant.LAST_7_DAYS);

        List<NetPromoterWrapper> listOfData = new ArrayList<>();
        Map<String, Map<String, Object>> finalMap = new HashMap<>();
        try {
            if (geographyId != null && geographyType != null && startDate != null && operator != null
                    && technology != null) {

                listOfData = getNPSDataByCallType(geographyId, geographyType, startDate, callType, operator, technology,
                        endDate);

                mapForCustomerTypesData = getMapForCustomerType(startDate, listOfData, finalMap, callType);

            }

        } catch (Exception e) {
            logger.error("error in getNPSData, error = {}", ExceptionUtils.getStackTrace(e));
        }

        return new JSONObject(mapForCustomerTypesData).toString();

    }

    /**
     * Gets the NPS data by call type.
     *
     * @param geographyId
     *            the geography id
     * @param geographyType
     *            the geography type
     * @param startDate
     *            the start date
     * @param callType
     *            the call type
     * @param operator
     *            the operator
     * @param technology
     *            the technology
     * @param endDate
     *            the end date
     * @return the NPS data by call type
     * @throws DaoException
     *             the dao exception
     */
    private List<NetPromoterWrapper> getNPSDataByCallType(Integer geographyId, String geographyType, String startDate,
            String callType, String operator, String technology, String endDate) {
        List<NetPromoterWrapper> listOfData;
        List<Integer> weekNumber = new ArrayList<>();
        if (callType.equalsIgnoreCase(NetPromoterConstant.WEEKLY)) {
            weekNumber = NetPromoterUtil.generateWeekNoList(startDate, false);

            listOfData = iNetPromoterDao.getNPSData(geographyId, geographyType, startDate, endDate, weekNumber,
                    operator, technology, callType);
        } else {
            listOfData = iNetPromoterDao.getNPSData(geographyId, geographyType, startDate, endDate, weekNumber,
                    operator, technology, callType);
        }
        return listOfData;
    }

    /**
     * Gets the map for customer type.
     *
     * @param startDate
     *            the start date
     * @param listOfData
     *            the list of data
     * @param finalMap
     *            the final map
     * @param callType
     *            the call type
     * @return the map for customer type
     */
    private Map<String, Map<String, Object>> getMapForCustomerType(String startDate,
            List<NetPromoterWrapper> listOfData, Map<String, Map<String, Object>> finalMap, String callType) {

        Map<String, Object> promoterMapOfDateWithAverage = new HashMap<>();
        Map<String, Object> detractorMapOfDateWithAverage = new HashMap<>();
        Map<String, Object> passiveMapOfDateWithAverage = new HashMap<>();
        Map<String, Object> mapOfNPS;
        Long npsDetractor = NumberUtils.LONG_ZERO;
        Long npsPassive = NumberUtils.LONG_ZERO;
        Long npsPromoter = NumberUtils.LONG_ZERO;

        mapOfNPS = getNPSAvgData(listOfData, promoterMapOfDateWithAverage, detractorMapOfDateWithAverage,
                passiveMapOfDateWithAverage, startDate, npsDetractor, npsPassive, npsPromoter, callType);

        finalMap.put(NetPromoterConstant.NPS, mapOfNPS);
        finalMap.put(NetPromoterConstant.DETRACTOR,
                getFinalDateWiseResponse(detractorMapOfDateWithAverage, startDate, callType));
        finalMap.put(NetPromoterConstant.PROMOTER,
                getFinalDateWiseResponse(promoterMapOfDateWithAverage, startDate, callType));
        finalMap.put(NetPromoterConstant.PASSIVE,
                getFinalDateWiseResponse(passiveMapOfDateWithAverage, startDate, callType));

        return finalMap;
    }

    /**
     * Gets the NPS avg data.
     *
     * @param listOfData
     *            the list of data
     * @param finalMap
     *            the final map
     * @param promoterMapOfDateWithAverage
     *            the promoter map of date with average
     * @param detractorMapOfDateWithAverage
     *            the detractor map of date with average
     * @param passiveMapOfDateWithAverage
     *            the passive map of date with average
     * @param startDate
     *            the start date
     * @param npsDetractor
     *            the NP sdetractor
     * @param npsPassive
     *            the NP spassive
     * @param npsPromoter
     *            the NP spromoter
     * @param callType
     *            the call type
     * @return the NPS avg data
     */
    private Map<String, Object> getNPSAvgData(List<NetPromoterWrapper> listOfData,
            Map<String, Object> promoterMapOfDateWithAverage, Map<String, Object> detractorMapOfDateWithAverage,
            Map<String, Object> passiveMapOfDateWithAverage, String startDate, Long npsDetractor, Long npsPassive,
            Long npsPromoter, String callType) {

        Long detractorCount ;
        Long passiveCount ;
        Long promoterCount ;
        Map<String, Object> npsValueMap = new HashMap<>();
        for (NetPromoterWrapper netPromoterWrapper : listOfData) {
            if (callType.equalsIgnoreCase(NetPromoterConstant.DAILY)) {
                if (netPromoterWrapper.getCustomerType().equalsIgnoreCase(NetPromoterConstant.DETRACTOR)) {
                    detractorCount = netPromoterWrapper.getCustomerCount();
                    npsDetractor = getAvgValueandNPSCountForDetractorPromoterPassive(detractorMapOfDateWithAverage,
                            startDate, npsDetractor, netPromoterWrapper, detractorCount,
                            netPromoterWrapper.getProcessDate());
                } else if (netPromoterWrapper.getCustomerType().equalsIgnoreCase(NetPromoterConstant.PASSIVE)) {
                    passiveCount = netPromoterWrapper.getCustomerCount();
                    npsPassive = getAvgValueandNPSCountForDetractorPromoterPassive(passiveMapOfDateWithAverage,
                            startDate, npsPassive, netPromoterWrapper, passiveCount,
                            netPromoterWrapper.getProcessDate());

                } else if (netPromoterWrapper.getCustomerType().equalsIgnoreCase(NetPromoterConstant.PROMOTER)) {
                    promoterCount = netPromoterWrapper.getCustomerCount();
                    npsPromoter = getAvgValueandNPSCountForDetractorPromoterPassive(promoterMapOfDateWithAverage,
                            startDate, npsPromoter, netPromoterWrapper, promoterCount,
                            netPromoterWrapper.getProcessDate());

                }

            } else {

                if (netPromoterWrapper.getCustomerType().equalsIgnoreCase(NetPromoterConstant.DETRACTOR)) {
                    detractorCount = netPromoterWrapper.getCustomerCount();
                    npsDetractor = getAvgValueandNPSCountForDetractorPromoterPassive(detractorMapOfDateWithAverage,
                            String.valueOf(NetPromoterUtil.getWeekNoByDate(startDate, false)), npsDetractor,
                            netPromoterWrapper, detractorCount, netPromoterWrapper.getweekNo().toString());
                } else if (netPromoterWrapper.getCustomerType().equalsIgnoreCase(NetPromoterConstant.PASSIVE)) {
                    passiveCount = netPromoterWrapper.getCustomerCount();
                    npsPassive = getAvgValueandNPSCountForDetractorPromoterPassive(passiveMapOfDateWithAverage,
                            String.valueOf(NetPromoterUtil.getWeekNoByDate(startDate, false)), npsPassive,
                            netPromoterWrapper, passiveCount, netPromoterWrapper.getweekNo().toString());

                } else if (netPromoterWrapper.getCustomerType().equalsIgnoreCase(NetPromoterConstant.PROMOTER)) {
                    promoterCount = netPromoterWrapper.getCustomerCount();
                    npsPromoter = getAvgValueandNPSCountForDetractorPromoterPassive(promoterMapOfDateWithAverage,
                            String.valueOf(NetPromoterUtil.getWeekNoByDate(startDate, false)), npsPromoter,
                            netPromoterWrapper, promoterCount, netPromoterWrapper.getweekNo().toString());

                }

            }

        }
        npsValueMap.put(NetPromoterConstant.INDEX_0_STRING, calculateNPS(npsPromoter, npsPassive, npsDetractor));
        detractorMapOfDateWithAverage.put(NetPromoterConstant.TOTAL_COUNT, npsDetractor);
        passiveMapOfDateWithAverage.put(NetPromoterConstant.TOTAL_COUNT, npsPassive);
        promoterMapOfDateWithAverage.put(NetPromoterConstant.TOTAL_COUNT, npsPromoter);

        return npsValueMap;
    }

    /**
     * Gets the avg valueand NPS count for detractor promoter passive.
     *
     * @param mapOfCustomerType
     *            the map of customer type
     * @param equalCounter
     *            the equal counter
     * @param npsDetractor
     *            the NP sdetractor
     * @param netPromoterWrapper
     *            the net promoter wrapper
     * @param detractorCount
     *            the detractor count
     * @param date
     *            the date
     * @return the avg valueand NPS count for detractor promoter passive
     */
    private Long getAvgValueandNPSCountForDetractorPromoterPassive(Map<String, Object> mapOfCustomerType,
            String equalCounter, Long npsDetractor, NetPromoterWrapper netPromoterWrapper, Long detractorCount,
            String date) {
        mapOfCustomerType.put(date, netPromoterWrapper.getAverage());

        if (date.replace("-", "").equals(equalCounter)) {

            npsDetractor = npsDetractor + netPromoterWrapper.getCustomerCount();
        }
        return npsDetractor;
    }

    /**
     * Calculate NPS.
     *
     * @param nPSpromoterCount
     *            the n P spromoter count
     * @param nPSpassiveCount
     *            the n P spassive count
     * @param nPSdetractorCount
     *            the n P sdetractor count
     * @return the double
     */
    public Double calculateNPS(Long nPSpromoterCount, Long nPSpassiveCount, Long nPSdetractorCount) {

        Double totalCount = (double) nPSpromoterCount + nPSpassiveCount + nPSdetractorCount;
        if (totalCount > ForesightConstants.ZERO) {
            return (nPSpromoterCount / totalCount) * ForesightConstants.HUNDRED - (nPSdetractorCount / totalCount) * ForesightConstants.HUNDRED;
        }
        return null;
    }

    /**
     * Gets the final date wise response.
     *
     * @param customerTypeWiseMap
     *            the customer type wise map
     * @param startDate
     *            the start date
     * @param callType
     *            the call type
     * @return the final date wise response
     */
    public Map<String, Object> getFinalDateWiseResponse(Map<String, Object> customerTypeWiseMap, String startDate,
            String callType) {

        Map<String, Object> data = new HashMap<>();
        Map<String, Integer> weeklyConstantDateMap;
        if (callType.equalsIgnoreCase(NetPromoterConstant.DAILY)) {
            weeklyConstantDateMap = NetPromoterUtil.get7daysDateConstantMap(startDate);
        } else {
            weeklyConstantDateMap = NetPromoterUtil.get7daysWeekConstantMap(startDate);
        }
        for (Entry<String, Integer> entry : weeklyConstantDateMap.entrySet()) {
            String value = entry.getValue().toString();
            if (customerTypeWiseMap.get(entry.getKey()) != null) {
                data.put(value, customerTypeWiseMap.get(entry.getKey()));
            } else {
                data.put(value,"");
            }
        }
        data.put(NetPromoterConstant.TOTAL_COUNT, customerTypeWiseMap.get(NetPromoterConstant.TOTAL_COUNT));

        return data;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.inn.foresight.module.nv.nps.service.INetPromoterService#
     * getNPSKpiWiseData(java.lang.Integer, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String getNPSKpiWiseData(Integer geographyId, String geographyType, String startDate, String endDate,
            String operator, String technology, String kpi) {

        List<NetPromoterWrapper> wrapperList;
        List<NetPromoterWrapper> customerCountwrapperList;
        logger.info(
                "In getNPSData geographyId, geographyType {} startdate {} enddate {} operator {} technology {} dateType {}, kpi {}",
                geographyId, geographyType, startDate, endDate, operator, technology, kpi);

        List<Integer> weekNumber = NetPromoterUtil.generateWeekNoList(startDate, true);
        if (startDate.equalsIgnoreCase(endDate)) {
            logger.info("IN DAILY");
            wrapperList = iNetPromoterDao.getKpiWiseData(geographyId, geographyType, startDate, endDate, operator,
                    technology, kpi, null);
            customerCountwrapperList = iNetPromoterDao.getKpiWiseData(geographyId, geographyType, startDate, endDate,
                    operator, technology, "ALL", null);

        } else {
            logger.info("IN weekly");
            wrapperList = iNetPromoterDao.getKpiWiseData(geographyId, geographyType, startDate, endDate, operator,
                    technology, kpi, weekNumber);
            customerCountwrapperList = iNetPromoterDao.getKpiWiseData(geographyId, geographyType, startDate, endDate,
                    operator, technology, "ALL", weekNumber);
        }
        return createKPIJson(wrapperList, startDate, startDate.equalsIgnoreCase(endDate), customerCountwrapperList);
    }

    /**
     * Creates the KPI json.
     *
     * @param wrapperList
     *            the wrapper list
     * @param startDate
     *            the start date
     * @param isDaily
     *            the is daily
     * @param customerCountwrapperList
     * @return the string
     */
    private String createKPIJson(List<NetPromoterWrapper> wrapperList, String startDate, boolean isDaily,
            List<NetPromoterWrapper> customerCountwrapperList) {
        Map<String, String> promoterKpiMap = NetPromoterUtil.loadKpiMap();
        Map<String, String> passiveKpiMap = NetPromoterUtil.loadKpiMap();
        Map<String, String> detractorKpiMap = NetPromoterUtil.loadKpiMap();
        Map<String, String> promoterCountMap = NetPromoterUtil.loadKpiMap();
        Map<String, String> passiveCountMap = NetPromoterUtil.loadKpiMap();
        Map<String, String> detractorCountMap = NetPromoterUtil.loadKpiMap();

        if (wrapperList != null && !wrapperList.isEmpty()) {
            if (isDaily) {
                logger.info("IN daily");
                return kpiJsonForDaily(wrapperList, startDate, promoterKpiMap, passiveKpiMap, detractorKpiMap,
                        promoterCountMap, passiveCountMap, detractorCountMap, customerCountwrapperList);

            } else {
                logger.info("IN WEEKLy");
                return kpiJsonForWeekly(wrapperList, startDate, promoterKpiMap, passiveKpiMap, detractorKpiMap,
                        promoterCountMap, passiveCountMap, detractorCountMap);
            }
        }
        return "[]";
    }

    /**
     * Kpi json for weekly.
     *
     * @param wrapperList
     *            the wrapper list
     * @param startDate
     *            the start date
     * @param promoterKpiMap
     *            the promoter kpi map
     * @param passiveKpiMap
     *            the passive kpi map
     * @param detractorKpiMap
     *            the detractor kpi map
     * @param promoterCountMap
     *            the promoter count map
     * @param passiveCountMap
     *            the passive count map
     * @param detractorCountMap
     *            the detractor count map
     * @param customerCountwrapperList
     * @return the string
     */
    private String kpiJsonForWeekly(List<NetPromoterWrapper> wrapperList, String startDate,
            Map<String, String> promoterKpiMap, Map<String, String> passiveKpiMap, Map<String, String> detractorKpiMap,
            Map<String, String> promoterCountMap, Map<String, String> passiveCountMap,
            Map<String, String> detractorCountMap) {
        int promoteKpiCounter=0;int passiveKpiCounter=0;int detractorKpiCounter=0;
        int countPromoter=0;int countPassive=0;int countDetractor=0;
        for (NetPromoterWrapper wrapper : wrapperList) {
            
            if (wrapper.getCustomerType().equalsIgnoreCase(NetPromoterConstant.PROMOTER)) {
                if(promoteKpiCounter<6) {
                    promoterKpiMap.put(
                            String.valueOf(NetPromoterUtil.getWeekNumberForMap(startDate , wrapper.getweekNo(),promoteKpiCounter)),
                            getKpiAverageValue(wrapper.getKpiSum(), wrapper.getCustomerCount()));
                 
                }
                 ++promoteKpiCounter;
            } else if (wrapper.getCustomerType().equalsIgnoreCase(NetPromoterConstant.DETRACTOR)) {
                    if(detractorKpiCounter<6) {
                        detractorKpiMap.put(
                                String.valueOf(NetPromoterUtil.getWeekNumberForMap(startDate , wrapper.getweekNo(),detractorKpiCounter)),
                                getKpiAverageValue(wrapper.getKpiSum(), wrapper.getCustomerCount()));
               
                    }
                        ++detractorKpiCounter;
            } else {
                if(passiveKpiCounter<6) {
                    passiveKpiMap.put(
                            String.valueOf(NetPromoterUtil.getWeekNumberForMap(startDate , wrapper.getweekNo(),passiveKpiCounter)),
                            getKpiAverageValue(wrapper.getKpiSum(), wrapper.getCustomerCount()));
             
                }
                      ++passiveKpiCounter;
            }
        }
        for (NetPromoterWrapper wrapper : wrapperList) {
            if (wrapper.getCustomerType().equalsIgnoreCase(NetPromoterConstant.PROMOTER)) {
              if(countPromoter<6) {
                  promoterCountMap.put(
                          String.valueOf(NetPromoterUtil.getWeekNumberForMap(startDate , wrapper.getweekNo(),countPromoter)),
                          NetPromoterUtil.isValidCustomerCount(wrapper.getCustomerCount()));
        
              }
                         ++countPromoter;
            } else if (wrapper.getCustomerType().equalsIgnoreCase(NetPromoterConstant.DETRACTOR)) {
                if(countDetractor<6) {
                    detractorCountMap.put(
                            String.valueOf(NetPromoterUtil.getWeekNumberForMap(startDate , wrapper.getweekNo(),countDetractor)),
                            NetPromoterUtil.isValidCustomerCount(wrapper.getCustomerCount()));
           
                }
                        ++countDetractor;
            } else {
                if(countPassive<6) {
                    passiveCountMap.put(
                            String.valueOf(NetPromoterUtil.getWeekNumberForMap(startDate , wrapper.getweekNo(),countPassive)),
                            NetPromoterUtil.isValidCustomerCount(wrapper.getCustomerCount()));
          
                }
                          ++countPassive;
            }
        }
        return customerTypeWiseKpiMAp(promoterKpiMap, passiveKpiMap, detractorKpiMap, promoterCountMap, passiveCountMap,
                detractorCountMap);
    }

    /**
     * Kpi json for daily.
     *
     * @param wrapperList
     *            the wrapper list
     * @param startDate
     *            the start date
     * @param promoterKpiMap
     *            the promoter kpi map
     * @param passiveKpiMap
     *            the passive kpi map
     * @param detractorKpiMap
     *            the detractor kpi map
     * @param promoterCountMap
     *            the promoter count map
     * @param passiveCountMap
     *            the passive count map
     * @param detractorCountMap
     *            the detractor count map
     * @param customerCountwrapperList
     * @return the string
     */
    private String kpiJsonForDaily(List<NetPromoterWrapper> wrapperList, String startDate,
            Map<String, String> promoterKpiMap, Map<String, String> passiveKpiMap, Map<String, String> detractorKpiMap,
            Map<String, String> promoterCountMap, Map<String, String> passiveCountMap,
            Map<String, String> detractorCountMap, List<NetPromoterWrapper> customerCountwrapperList) {
        setAvgKpiValueForDaily(wrapperList, startDate, promoterKpiMap, passiveKpiMap, detractorKpiMap);
        setCustomerCountForDaily(startDate, promoterCountMap, passiveCountMap, detractorCountMap,
                customerCountwrapperList);
        return customerTypeWiseKpiMAp(promoterKpiMap, passiveKpiMap, detractorKpiMap, promoterCountMap, passiveCountMap,
                detractorCountMap);
    }

    private void setAvgKpiValueForDaily(List<NetPromoterWrapper> kpiAvgwrapperList, String startDate,
            Map<String, String> promoterKpiMap, Map<String, String> passiveKpiMap,
            Map<String, String> detractorKpiMap) {
        for (NetPromoterWrapper wrapper : kpiAvgwrapperList) {

            if (wrapper.getCustomerType().equalsIgnoreCase(NetPromoterConstant.PROMOTER)) {
                promoterKpiMap.put(NetPromoterUtil.getDateDifference(wrapper.getProcessDate(), startDate),
                        getKpiAverageValue(wrapper.getKpiSum(), wrapper.getCustomerCount()));
            } else if (wrapper.getCustomerType().equalsIgnoreCase(NetPromoterConstant.DETRACTOR)) {
                detractorKpiMap.put(NetPromoterUtil.getDateDifference(wrapper.getProcessDate(), startDate),
                        getKpiAverageValue(wrapper.getKpiSum(), wrapper.getCustomerCount()));
            } else {
                passiveKpiMap.put(NetPromoterUtil.getDateDifference(wrapper.getProcessDate(), startDate),
                        getKpiAverageValue(wrapper.getKpiSum(), wrapper.getCustomerCount()));
            }
        }
    }

    private void setCustomerCountForDaily(String startDate, Map<String, String> promoterCountMap,
            Map<String, String> passiveCountMap, Map<String, String> detractorCountMap,
            List<NetPromoterWrapper> customerCountwrapperList) {
        for (NetPromoterWrapper wrapper : customerCountwrapperList) {
            if (wrapper.getCustomerType().equalsIgnoreCase(NetPromoterConstant.PROMOTER)) {
                promoterCountMap.put(NetPromoterUtil.getDateDifference(wrapper.getProcessDate(), startDate),
                        NetPromoterUtil.isValidCustomerCount(wrapper.getCustomerCount()));
            } else if (wrapper.getCustomerType().equalsIgnoreCase(NetPromoterConstant.DETRACTOR)) {
                detractorCountMap.put(NetPromoterUtil.getDateDifference(wrapper.getProcessDate(), startDate),
                        NetPromoterUtil.isValidCustomerCount(wrapper.getCustomerCount()));
            } else {
                passiveCountMap.put(NetPromoterUtil.getDateDifference(wrapper.getProcessDate(), startDate),
                        NetPromoterUtil.isValidCustomerCount(wrapper.getCustomerCount()));
            }
        }
    }

    /**
     * Gets the kpi average value.
     *
     * @param kpiSum
     *            the kpi sum
     * @param customerCount
     *            the customer count
     * @return the kpi average value
     */
    private String getKpiAverageValue(Double kpiSum, Long customerCount) {
        if (customerCount != null && customerCount > 0 && kpiSum != null) {
            return String.valueOf(kpiSum / Double.valueOf(String.valueOf(customerCount)));
        }
        return "null";
    }

    /**
     * Customer type wise kpi M ap.
     *
     * @param promoterKpiMap
     *            the promoter kpi map
     * @param passiveKpiMap
     *            the passive kpi map
     * @param detractorKpiMap
     *            the detractor kpi map
     * @param promoterCountMap
     *            the promoter count map
     * @param passiveCountMap
     *            the passive count map
     * @param detractorCountMap
     *            the detractor count map
     * @return the string
     */
    private String customerTypeWiseKpiMAp(Map<String, String> promoterKpiMap, Map<String, String> passiveKpiMap,
            Map<String, String> detractorKpiMap, Map<String, String> promoterCountMap,
            Map<String, String> passiveCountMap, Map<String, String> detractorCountMap) {
        Map<String, Map<String, String>> json = new HashMap<>();
        json.put(NetPromoterConstant.KPI_DETRACTOR, detractorKpiMap);
        json.put(NetPromoterConstant.KPI_PASSIVE, passiveKpiMap);
        json.put(NetPromoterConstant.KPI_PROMOTER, promoterKpiMap);
        json.put(NetPromoterConstant.COUNT_DETRACTOR, detractorCountMap);
        json.put(NetPromoterConstant.COUNT_PASSIVE, passiveCountMap);
        json.put(NetPromoterConstant.COUNT_PROMOTER, promoterCountMap);
        return new JSONObject(json).toString();
    }

}
package com.inn.foresight.core.infra.utils;

import static com.inn.foresight.core.generic.utils.ForesightConstants.MACRO;
import static com.inn.foresight.core.generic.utils.ForesightConstants.MACRO_CELL;
import static com.inn.foresight.core.generic.utils.ForesightConstants.MACRO_ENB;
import static com.inn.foresight.core.generic.utils.ForesightConstants.MACRO_VDU;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.maps.Corner;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.geometry.BoundaryUtils;
import com.inn.commons.maps.geometry.gis.GISGeometry;
import com.inn.commons.maps.geometry.gis.GISPoint;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.model.MacroSiteDetail;
import com.inn.foresight.core.infra.model.NEBandDetail;
import com.inn.foresight.core.infra.model.NETaskDetail;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.NELType;
import com.inn.foresight.core.infra.utils.enums.NEStatus;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.utils.enums.Technology;
import com.inn.foresight.core.infra.utils.enums.Vendor;
import com.inn.foresight.core.infra.wrapper.SiteSelectionWrapper;
import com.inn.product.security.authentication.GenerateReportURL;
import com.inn.product.systemconfiguration.utils.SystemConfigurationUtils;

/**
 * The Class InfraUtils.
 */
public class InfraUtils {

    /** The logger. */
    private static Logger logger = LogManager.getLogger(InfraUtils.class);

    static Map<String, String> systemConfigMap = SystemConfigurationUtils.systemConfMap;
    /** The geography L 1 zoom. */
    static Integer geographyL1Zoom = 0;

    /** The geography L 2 zoom. */
    static Integer geographyL2Zoom = 0;

    /** The geography L 3 zoom. */
    static Integer geographyL3Zoom = 0;

    /** The geography L 4 zoom. */
    static Integer geographyL4Zoom = 0;

    /**
     * Gets the progress state for macro.
     *
     * @param siteSelectionWrapper the site selection wrapper
     * @return the progress state for macro
     */
    public static List<String> getProgressStateForMacro(SiteSelectionWrapper siteSelectionWrapper) {
        List<String> progressStateMacroList = new ArrayList<>();
        try {
            if (siteSelectionWrapper.getMacroOnair() != null && !siteSelectionWrapper.getMacroOnair().isEmpty()) {
                progressStateMacroList.add((String) siteSelectionWrapper.getMacroOnair().get(InfraConstants.PROGRESSSTATE));
            }
            if (siteSelectionWrapper.getMacroPlanned() != null && !siteSelectionWrapper.getMacroPlanned().isEmpty()) {
                progressStateMacroList.add((String) siteSelectionWrapper.getMacroPlanned().get(InfraConstants.PROGRESSSTATE));
            }
        } catch (Exception e) {
            logger.error("error in getProgressStateForMacro, err  = {}", Utils.getStackTrace(e));
        }
        return progressStateMacroList;
    }

    /**
     * Gets the progress state for macro on air.
     *
     * @param siteSelectionWrapper the site selection wrapper
     * @return the progress state for macro on air
     */
    public static List<String> getProgressStateForMacroOnAir(SiteSelectionWrapper siteSelectionWrapper) {
        List<String> progressStateMacroList = new ArrayList<>();
        try {
            if (siteSelectionWrapper.getMacroOnair() != null && !siteSelectionWrapper.getMacroOnair().isEmpty()) {
                progressStateMacroList.add((String) siteSelectionWrapper.getMacroOnair().get(InfraConstants.PROGRESSSTATE));
            }
        } catch (Exception e) {
            logger.error("error in getProgressStateForMacroOnAir, err  = {}", e.getMessage());
        }
        return progressStateMacroList;
    }

    /**
     * Gets the progress state for macro planned.
     *
     * @param siteSelectionWrapper the site selection wrapper
     * @return the progress state for macro planned
     */
    public static List<String> getProgressStateForMacroPlanned(SiteSelectionWrapper siteSelectionWrapper) {
        List<String> progressStateMacroList = new ArrayList<>();
        try {
            if (siteSelectionWrapper.getMacroPlanned() != null && !siteSelectionWrapper.getMacroPlanned().isEmpty()) {
                progressStateMacroList.add((String) siteSelectionWrapper.getMacroPlanned().get(InfraConstants.PROGRESSSTATE));
            }
        } catch (Exception e) {
            logger.error("error in getProgressStateForMacroPlanned, err  = {}", e.getMessage());
        }
        return progressStateMacroList;
    }

    /**
     * Gets the NE states for macro.
     *
     * @param siteSelectionWrapper the site selection wrapper
     * @return the NE states for macro
     */
    public static List<NEType> getNEStatesForMacro(SiteSelectionWrapper siteSelectionWrapper) {
        List<NEType> siteTypeList = new ArrayList<>();
        try {
            String siteType = ForesightConstants.BLANK_STRING;
            if (siteSelectionWrapper.getMacroOnair() != null && !siteSelectionWrapper.getMacroOnair().isEmpty()) {
                siteType = (String) siteSelectionWrapper.getMacroOnair().get(InfraConstants.SITETYPE);
                siteTypeList = getNEType(siteTypeList, siteType);
            }
            if (siteSelectionWrapper.getMacroPlanned() != null && !siteSelectionWrapper.getMacroPlanned().isEmpty()) {
                siteType = (String) siteSelectionWrapper.getMacroPlanned().get(InfraConstants.SITETYPE);
                siteTypeList = getNEType(siteTypeList, siteType);
            }
        } catch (Exception e) {
            logger.error("error in getNEStatesForMacro, err = {}", Utils.getStackTrace(e));
        }
        return siteTypeList;
    }

    /**
     * Gets the NE type.
     *
     * @param siteTypeList the site type list
     * @param siteType the site type
     * @return the NE type
     */
    public static List<NEType> getNEType(List<NEType> siteTypeList, String siteType) {
        if (siteType != null) {
            siteTypeList.add((NEType.valueOf(siteType)));
        }
        return siteTypeList;
    }

    /**
     * Gets the site task date for sector property.
     *
     * @param taskDate the task date
     * @param isCheckOverviewDate the is check overview date
     * @return the site task date for sector property
     */
    public static String getSiteTaskDateForSectorProperty(Date taskDate, Boolean isCheckOverviewDate) {
        String convertDate = ForesightConstants.BLANK_STRING;
        if (taskDate != null) {
            SimpleDateFormat simpleDateFormat = null;
            if (isCheckOverviewDate) {
                simpleDateFormat = new SimpleDateFormat(InfraConstants.DATE_MONTH_YEAR);
            } else {
                simpleDateFormat = new SimpleDateFormat(InfraConstants.DATE_MONTH_YEAR_WITH_COMMA_SPACE);
            }
            convertDate = simpleDateFormat.format(taskDate);
        } else {
            convertDate = ForesightConstants.HIPHEN;
        }
        return convertDate;
    }

    /**
     * Convert list elements.
     *
     * @param list the list
     * @param contentCase the content case
     * @return the list
     */
    public static List<String> convertListElements(List<String> list, String contentCase) {
        if (list != null && list.size() > 0) {
            if (contentCase.equalsIgnoreCase(InfraConstants.LIST_TOUPPERCASE)) {
                list = list.stream().map(String::toUpperCase).collect(Collectors.toList());
            } else if (contentCase.equalsIgnoreCase(InfraConstants.LIST_TOLOWERCASE)) {
                list = list.stream().map(String::toLowerCase).collect(Collectors.toList());
            } else if (contentCase.equalsIgnoreCase(InfraConstants.LIST_TOUPPERCASE_WITH_TRIM)) {
                list = list.stream().map(String::toUpperCase).map(String::trim).collect(Collectors.toList());
            } else if (contentCase.equalsIgnoreCase(InfraConstants.LIST_TOLOWERCASE_WITH_TRIM)) {
                list = list.stream().map(String::toLowerCase).map(String::trim).collect(Collectors.toList());
            }
        }
        return list;
    }

    public static List<Predicate> getPredicatesForCriteriaBuilderForNE(CriteriaBuilder criteriaBuilder, Root<NetworkElement> networkElement, List<NEType> neTypeList, List<String> neNameList,
            List<String> neFrequencyList, List<NEStatus> neStatusList, List<Vendor> vendorList, List<Technology> technologyList, List<Domain> domainList, Map<String, List<String>> geographyNames,
            List<String> neIdList) {
        List<Predicate> predicates = new ArrayList<>();
        List<String> geographies = new ArrayList<>();
        if (neTypeList != null && !neTypeList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_NETYPE_KEY)).in(neTypeList));
        }
        if (neFrequencyList != null && !neFrequencyList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_NEFREQUENCY_KEY)).in(neFrequencyList));
        }
        if (neStatusList != null && !neStatusList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_NESTATUS_KEY)).in(neStatusList));
        }
        if (neNameList != null && !neNameList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_NENAME_KEY)).in(neNameList));
        }
        if (vendorList != null && !vendorList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_VENDOR_KEY)).in(vendorList));
        }
        if (technologyList != null && !technologyList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_TECHNOLOGY_KEY)).in(technologyList));
        }
        if (domainList != null && !domainList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_DOMAIN_KEY)).in(domainList));
        }
        if (neIdList != null && !neIdList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_NEID_KEY)).in(neIdList));
        }

        if (geographyNames != null && !geographyNames.isEmpty()) {
            if (geographyNames.get(InfraConstants.GEOGRAPHYL4_TABLE) != null) {
                geographies = geographyNames.get(InfraConstants.GEOGRAPHYL4_TABLE);
                predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.BOUNDARY_NAME)).in(geographies));
            }
            if (geographyNames.get(InfraConstants.GEOGRAPHYL3_TABLE) != null) {
                geographies = geographyNames.get(InfraConstants.GEOGRAPHYL3_TABLE);
                predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.BOUNDARY_NAME)).in(geographies));
            }
            if (geographyNames.get(InfraConstants.GEOGRAPHYL2_TABLE) != null) {
                geographies = geographyNames.get(InfraConstants.GEOGRAPHYL2_TABLE);
                predicates.add(criteriaBuilder
                        .upper(networkElement.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.BOUNDARY_NAME))
                        .in(geographies));
            }
            if (geographyNames.get(InfraConstants.GEOGRAPHYL1_TABLE) != null) {
                geographies = geographyNames.get(InfraConstants.GEOGRAPHYL1_TABLE);
                predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY)
                        .get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.BOUNDARY_NAME)).in(geographies));
            }
        }
        return predicates;
    }

    public static List<Predicate> getPredicatesForCBForMacroSiteDetail(CriteriaBuilder criteriaBuilder, Join<NetworkElement, MacroSiteDetail> networkElementJoin, List<NEType> neTypeList,
            List<String> neNameList, List<String> neFrequencyList, List<NEStatus> neStatusList, List<Vendor> vendorList, List<Technology> technologyList, List<Domain> domainList,
            Map<String, List<String>> geographyNames, List<String> neIdList) {
        List<Predicate> predicates = new ArrayList<>();
        List<String> geographies = new ArrayList<>();
        if (neTypeList != null && !neTypeList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElementJoin.get(InfraConstants.NE_NETYPE_KEY)).in(neTypeList));
        }
        if (neFrequencyList != null && !neTypeList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElementJoin.get(InfraConstants.NE_NEFREQUENCY_KEY)).in(neFrequencyList));
            predicates.add(networkElementJoin.get(InfraConstants.NE_NEFREQUENCY_KEY).isNotNull());
        }
        if (neStatusList != null && !neStatusList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElementJoin.get(InfraConstants.NE_NESTATUS_KEY)).in(neStatusList));
        }
        if (neNameList != null && !neNameList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElementJoin.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NENAME_KEY)).in(neNameList));
        }
        if (vendorList != null && !vendorList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElementJoin.get(InfraConstants.NE_VENDOR_KEY)).in(vendorList));
        }
        if (technologyList != null && !technologyList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElementJoin.get(InfraConstants.NE_TECHNOLOGY_KEY)).in(technologyList));
        }
        if (domainList != null && !domainList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElementJoin.get(InfraConstants.NE_DOMAIN_KEY)).in(domainList));
        }
        if (neIdList != null && !neIdList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElementJoin.get(InfraConstants.NE_NEID_KEY)).in(neIdList));
        }

        if (geographyNames != null && !geographyNames.isEmpty()) {
            if (geographyNames.get(InfraConstants.GEOGRAPHYL4_TABLE) != null) {
                geographies = geographyNames.get(InfraConstants.GEOGRAPHYL4_TABLE);
                predicates.add(criteriaBuilder.upper(networkElementJoin.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.BOUNDARY_NAME)).in(geographies));
            }
            if (geographyNames.get(InfraConstants.GEOGRAPHYL3_TABLE) != null) {
                geographies = geographyNames.get(InfraConstants.GEOGRAPHYL3_TABLE);
                predicates
                        .add(criteriaBuilder.upper(networkElementJoin.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.BOUNDARY_NAME)).in(geographies));
            }
            if (geographyNames.get(InfraConstants.GEOGRAPHYL2_TABLE) != null) {
                geographies = geographyNames.get(InfraConstants.GEOGRAPHYL2_TABLE);
                predicates.add(criteriaBuilder.upper(
                        networkElementJoin.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.BOUNDARY_NAME))
                        .in(geographies));
            }
            if (geographyNames.get(InfraConstants.GEOGRAPHYL1_TABLE) != null) {
                geographies = geographyNames.get(InfraConstants.GEOGRAPHYL1_TABLE);
                predicates.add(criteriaBuilder.upper(networkElementJoin.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY)
                        .get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.BOUNDARY_NAME)).in(geographies));
            }
        }
        return predicates;
    }

    public static List<Predicate> getPredicatesForSiteCounts(CriteriaBuilder criteriaBuilder, Root<NetworkElement> networkElement, NEType neType, List<String> neFrequencyList, NEStatus neStatus,
            Double min_lat, Double max_lat, Double min_lon, Double max_lon) {
        List<Predicate> predicates = new ArrayList<>();
        if (neType != null) {
            predicates.add(criteriaBuilder.equal(networkElement.get(InfraConstants.NE_NETYPE_KEY), neType));
        }
        if (neFrequencyList != null) {
            predicates.add(networkElement.get(InfraConstants.NE_NEFREQUENCY_KEY).in(neFrequencyList));
        }
        if (neStatus != null) {
            predicates.add(criteriaBuilder.equal(networkElement.get(InfraConstants.NE_NESTATUS_KEY), neStatus));
        }
        if (min_lat != null && max_lat != null && min_lon != null && max_lon != null) {
            predicates.add(criteriaBuilder.and(criteriaBuilder.gt(networkElement.get(InfraConstants.NE_LATITUDE_KEY), min_lat),
                    criteriaBuilder.lt(networkElement.get(InfraConstants.NE_LATITUDE_KEY), max_lat)));
            predicates.add(criteriaBuilder.and(criteriaBuilder.gt(networkElement.get(InfraConstants.NE_LONGITUDE_KEY), min_lon),
                    criteriaBuilder.lt(networkElement.get(InfraConstants.NE_LONGITUDE_KEY), max_lon)));
        }
        return predicates;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static List convertListElementToEnum(List targetedList, String enumType) {
        List outputList = new ArrayList();
        try {
            if (targetedList != null && !targetedList.isEmpty() && enumType != null) {
                if (enumType.equalsIgnoreCase(InfraConstants.NETYPE_ENUM_KEY))
                    outputList = (List) targetedList.stream().map(s -> NEType.valueOf(s.toString())).collect(Collectors.toList());
                if (enumType.equalsIgnoreCase(InfraConstants.NESTATUS_ENUM_KEY))
                    outputList = (List) targetedList.stream().map(s -> NEStatus.valueOf(s.toString())).collect(Collectors.toList());
            }
        } catch (Exception exception) {
            logger.error("Error in converting List elements to enum Message : {}", Utils.getStackTrace(exception));
        }
        return outputList;
    }

    public static List<Predicate> getPredicatesForViewPortsForNE(CriteriaBuilder criteriaBuilder, Root outerNetworkElement, Map<String, Double> viewportMap) {
        List<Predicate> predicates = new ArrayList<>();
        try {
            if (viewportMap != null && !viewportMap.isEmpty()) {
                if (viewportMap.get(InfraConstants.SOUTHWEST_LATITUDE_KEY) != null) {
                    predicates.add(criteriaBuilder.greaterThan(outerNetworkElement.get(InfraConstants.NE_LATITUDE_KEY), viewportMap.get(InfraConstants.SOUTHWEST_LATITUDE_KEY)));
                }
                if (viewportMap.get(InfraConstants.NORTHEAST_LATITUDE_KEY) != null) {
                    predicates.add(criteriaBuilder.lessThan(outerNetworkElement.get(InfraConstants.NE_LATITUDE_KEY), viewportMap.get(InfraConstants.NORTHEAST_LATITUDE_KEY)));
                }
                if (viewportMap.get(InfraConstants.SOUTHWEST_LONGITUDE_KEY) != null) {
                    predicates.add(criteriaBuilder.greaterThan(outerNetworkElement.get(InfraConstants.NE_LONGITUDE_KEY), viewportMap.get(InfraConstants.SOUTHWEST_LONGITUDE_KEY)));
                }
                if (viewportMap.get(InfraConstants.NORTHEAST_LONGITUDE_KEY) != null) {
                    predicates.add(criteriaBuilder.lessThan(outerNetworkElement.get(InfraConstants.NE_LONGITUDE_KEY), viewportMap.get(InfraConstants.NORTHEAST_LONGITUDE_KEY)));
                }
            }
            return predicates;
        } catch (Exception exception) {
            logger.error("Error in getting predicates for viewport for NE Message : {}", exception.getMessage());
        }
        return predicates;
    }

    public static List<Predicate> getPredicatesForViewPorts(CriteriaBuilder criteriaBuilder, Root outerNetworkElement, Map<String, Double> viewportMap) {
        List<Predicate> predicates = new ArrayList<>();
        try {
            if (viewportMap != null && !viewportMap.isEmpty()) {
                if (viewportMap.get(InfraConstants.SOUTHWEST_LATITUDE_KEY) != null) {
                    predicates.add(criteriaBuilder.greaterThan(outerNetworkElement.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_LATITUDE_KEY),
                            viewportMap.get(InfraConstants.SOUTHWEST_LATITUDE_KEY)));
                }
                if (viewportMap.get(InfraConstants.NORTHEAST_LATITUDE_KEY) != null) {
                    predicates.add(criteriaBuilder.lessThan(outerNetworkElement.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_LATITUDE_KEY),
                            viewportMap.get(InfraConstants.NORTHEAST_LATITUDE_KEY)));
                }
                if (viewportMap.get(InfraConstants.SOUTHWEST_LONGITUDE_KEY) != null) {
                    predicates.add(criteriaBuilder.greaterThan(outerNetworkElement.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_LONGITUDE_KEY),
                            viewportMap.get(InfraConstants.SOUTHWEST_LONGITUDE_KEY)));
                }
                if (viewportMap.get(InfraConstants.NORTHEAST_LONGITUDE_KEY) != null) {
                    predicates.add(criteriaBuilder.lessThan(outerNetworkElement.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_LONGITUDE_KEY),
                            viewportMap.get(InfraConstants.NORTHEAST_LONGITUDE_KEY)));
                }
            }
            return predicates;
        } catch (Exception exception) {
            logger.error("Error in getting predicates for viewport for NE Message : {}", exception.getMessage());
        }
        return predicates;
    }

    public static Map<String, Double> getViewPortMap(Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat) {
        Map<String, Double> viewportMap = new HashMap<>();
        try {
            viewportMap.put(InfraConstants.SOUTHWEST_LATITUDE_KEY, southWestLat);
            viewportMap.put(InfraConstants.NORTHEAST_LATITUDE_KEY, northEastLat);
            viewportMap.put(InfraConstants.SOUTHWEST_LONGITUDE_KEY, southWestLong);
            viewportMap.put(InfraConstants.NORTHEAST_LONGITUDE_KEY, northEastLong);
        } catch (Exception exception) {
            logger.error("Error in getting viewport Map due to exception {}", Utils.getStackTrace(exception));
        }
        return viewportMap;
    }

    public static String getFormattedBandWidth(String bandwidth) {
        if (bandwidth != null) {
            String b1 = ForesightConstants.BLANK_STRING;
            String b[] = bandwidth.split(ForesightConstants.COMMA);
            for (int i = 0; i < b.length; i++) {

                if (i != 0) {
                    b1 = b1.concat(ForesightConstants.SPACE).concat(ForesightConstants.PLUS_SIGN).concat(ForesightConstants.SPACE);
                }
                if (b[i] != null && b[i] != ForesightConstants.BLANK_STRING) {
                    b1 = b1.concat(b[i]).concat(ForesightConstants.SPACE).concat(InfraConstants.MEGA_HZ).concat(ForesightConstants.SPACE);
                }
            }
            bandwidth = b1;

        } else {
            bandwidth = ForesightConstants.BLANK_STRING;
        }
        return bandwidth;
    }

    public static List<Predicate> getPredicatesForNEBandDetail(CriteriaBuilder criteriaBuilder, Root<NEBandDetail> neBandDetail, List<NEType> neTypeList, List<String> neNameList,
            List<String> neFrequencyList, List<NEStatus> neStatusList, List<Vendor> vendorList, List<Technology> technologyList, List<Domain> domainList, Map<String, List<String>> geographyNames,
            Map<String, Double> viewportMap) {
        List<Predicate> predicates = new ArrayList<>();
        List<String> geographies = new ArrayList<>();
        if (neTypeList != null) {
            predicates.add(criteriaBuilder.upper(neBandDetail.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY)).in(neTypeList));
        }
        if (neFrequencyList != null) {
            predicates.add(criteriaBuilder.upper(neBandDetail.get(InfraConstants.NE_NEFREQUENCY_KEY)).in(neFrequencyList));
        }
        if (neStatusList != null) {
            predicates.add(criteriaBuilder.upper(neBandDetail.get(InfraConstants.NE_BANDSTATUS_KEY)).in(neStatusList));
        }
        if (neNameList != null) {
            predicates.add(criteriaBuilder.upper(neBandDetail.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NENAME_KEY)).in(neNameList));
        }
        if (vendorList != null) {
            predicates.add(criteriaBuilder.upper(neBandDetail.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_VENDOR_KEY)).in(vendorList));
        }
        if (technologyList != null) {
            predicates.add(criteriaBuilder.upper(neBandDetail.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_TECHNOLOGY_KEY)).in(technologyList));
        }
        if (domainList != null) {
            predicates.add(criteriaBuilder.upper(neBandDetail.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_DOMAIN_KEY)).in(domainList));
        }

        if (geographyNames != null && !geographyNames.isEmpty()) {
            if (geographyNames.get(InfraConstants.GEOGRAPHYL4_TABLE) != null) {
                geographies = geographyNames.get(InfraConstants.GEOGRAPHYL4_TABLE);
                predicates.add(criteriaBuilder.upper(neBandDetail.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.BOUNDARY_NAME)).in(geographies));
            }
            if (geographyNames.get(InfraConstants.GEOGRAPHYL3_TABLE) != null) {
                geographies = geographyNames.get(InfraConstants.GEOGRAPHYL3_TABLE);
                predicates.add(criteriaBuilder
                        .upper(neBandDetail.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.BOUNDARY_NAME))
                        .in(geographies));
            }
            if (geographyNames.get(InfraConstants.GEOGRAPHYL2_TABLE) != null) {
                geographies = geographyNames.get(InfraConstants.GEOGRAPHYL2_TABLE);
                predicates.add(criteriaBuilder.upper(neBandDetail.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
                        .get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.BOUNDARY_NAME)).in(geographies));
            }
            if (geographyNames.get(InfraConstants.GEOGRAPHYL1_TABLE) != null) {
                geographies = geographyNames.get(InfraConstants.GEOGRAPHYL1_TABLE);
                predicates.add(criteriaBuilder.upper(neBandDetail.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
                        .get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.BOUNDARY_NAME)).in(geographies));
            }
        }

        if (viewportMap != null && !viewportMap.isEmpty()) {
            if (viewportMap.get(InfraConstants.SOUTHWEST_LATITUDE_KEY) != null) {
                predicates.add(criteriaBuilder.greaterThan(neBandDetail.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_LATITUDE_KEY),
                        viewportMap.get(InfraConstants.SOUTHWEST_LATITUDE_KEY)));
            }
            if (viewportMap.get(InfraConstants.NORTHEAST_LATITUDE_KEY) != null) {
                predicates.add(
                        criteriaBuilder.lessThan(neBandDetail.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_LATITUDE_KEY), viewportMap.get(InfraConstants.NORTHEAST_LATITUDE_KEY)));
            }
            if (viewportMap.get(InfraConstants.SOUTHWEST_LONGITUDE_KEY) != null) {
                predicates.add(criteriaBuilder.greaterThan(neBandDetail.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_LONGITUDE_KEY),
                        viewportMap.get(InfraConstants.SOUTHWEST_LONGITUDE_KEY)));
            }
            if (viewportMap.get(InfraConstants.NORTHEAST_LONGITUDE_KEY) != null) {
                predicates.add(
                        criteriaBuilder.lessThan(neBandDetail.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_LONGITUDE_KEY), viewportMap.get(InfraConstants.NORTHEAST_LONGITUDE_KEY)));
            }
        }
        return predicates;
    }

    public static Integer getDaysBetweenTwoDates(Date firstDate, Date secondDate) {
        Integer differenceDays = ForesightConstants.ZERO_INTEGER;
        if (firstDate != null && secondDate != null) {
            differenceDays = Days.daysBetween(new LocalDate(firstDate), new LocalDate(secondDate)).getDays();
        }
        if (differenceDays > 0)
            return differenceDays;
        else {
            return ForesightConstants.ZERO_INTEGER;
        }
    }

    public static String getCompletionForExecuteScft(String executeScft) {
        String status = "no";
        if (executeScft != null) {
            if (executeScft.toString().equalsIgnoreCase(ForesightConstants.CLOSE) || executeScft.toString().equalsIgnoreCase("Accepted_By_Reliance_CTO")
                    || executeScft.toString().equalsIgnoreCase("On_Hold_For_WCC_Approval")) {
                status = "yes";
            } else {
                status = "no";
            }
        } else {
            status = "no";
        }
        return status;
    }

    public static String getCompletionForSSCVT(String executeSSCVT, String band) {
        String status = "no";
        if (band != null) {
            if (band.equalsIgnoreCase(ForesightConstants.STRING_2300)) {
                if (executeSSCVT != null) {
                    if (executeSSCVT.toString().equalsIgnoreCase("Accepted_By_CTO") || executeSSCVT.toString().equalsIgnoreCase("Assigned_To_Reliance_CLOT_QA_Auditor")
                            || executeSSCVT.toString().equalsIgnoreCase("Passed_By_Reliance_CLOT_QA_Auditor") || executeSSCVT.toString().equalsIgnoreCase("Failed_By_Reliance_CLOT_QA_Auditor")
                            || executeSSCVT.toString().equalsIgnoreCase("Accepted_By_Reliance_CLOT_QA_Manager") || executeSSCVT.toString().equalsIgnoreCase("Rejected_By_Reliance_CLOT_QA_Manager")
                            || executeSSCVT.toString().equalsIgnoreCase("OnHold_By_Reliance_CLOT_QA_Manager") || executeSSCVT.toString().equalsIgnoreCase("Accepted_By_Samsung_CLOT_QA_Coordinator")
                            || executeSSCVT.toString().equalsIgnoreCase("Close")) {

                        status = "yes";
                    } else {
                        status = "no";
                    }
                } else {
                    status = "no";
                }
            } else if (band.equalsIgnoreCase(ForesightConstants.STRING_850) || band.equalsIgnoreCase(ForesightConstants.STRING_1800)) {
                if (executeSSCVT != null) {
                    if (executeSSCVT.toString().equalsIgnoreCase("Accepted_By_Reliance_CTO") || executeSSCVT.toString().equalsIgnoreCase("Assigned_To_Reliance_CLOT_QA_Auditor")
                            || executeSSCVT.toString().equalsIgnoreCase("Passed_By_Reliance_CLOT_QA_Auditor") || executeSSCVT.toString().equalsIgnoreCase("Failed_By_Reliance_CLOT_QA_Auditor")
                            || executeSSCVT.toString().equalsIgnoreCase("Accepted_By_Reliance_CLOT_QA_Manager") || executeSSCVT.toString().equalsIgnoreCase("Rejected_By_Reliance_CLOT_QA_Manager")
                            || executeSSCVT.toString().equalsIgnoreCase("OnHold_By_Reliance_CLOT_QA_Manager") || executeSSCVT.toString().equalsIgnoreCase("Accepted_By_Samsung_CLOT_QA_Coordinator")
                            || executeSSCVT.toString().equalsIgnoreCase("Close")) {

                        status = "yes";
                    } else {
                        status = "no";
                    }
                } else {
                    status = "no";
                }
            }
        }
        return status;
    }

    public static String getCompletionForExecuteEmf(String executeEmf, String band) {
        String status = "no";
        if (band != null) {
            if (band.equalsIgnoreCase(ForesightConstants.STRING_2300) || band.equalsIgnoreCase(ForesightConstants.STRING_1800)) {
                if (executeEmf != null) {
                    if (executeEmf.equalsIgnoreCase(ForesightConstants.CLOSE) || executeEmf.equalsIgnoreCase("Fixed_By_Reliance_RF_Lead") || executeEmf.equalsIgnoreCase("Accepted_By_Reliance_RF")
                            || executeEmf.equalsIgnoreCase("Notified_For_Hard_Copy") || executeEmf.equalsIgnoreCase("Submitted_To_Term_Cell")
                            || executeEmf.equalsIgnoreCase("On_Hold_For_WCC_Approval")) {
                        status = "yes";
                    } else {
                        status = "no";
                    }
                } else {
                    status = "no";
                }
            } else if (band.equalsIgnoreCase(ForesightConstants.STRING_850)) {
                if (executeEmf != null) {
                    if (executeEmf.equalsIgnoreCase(ForesightConstants.CLOSE) || executeEmf.equalsIgnoreCase("Fixed_By_Reliance_RF_Lead") || executeEmf.equalsIgnoreCase("Accepted_By_Reliance_RF")
                            || executeEmf.equalsIgnoreCase("Notified_For_Hard_Copy") || executeEmf.equalsIgnoreCase("Submitted_To_Term_Cell")) {

                        status = "yes";
                    } else {
                        status = "no";
                    }
                } else {
                    status = "no";
                }
            }
        }
        return status;
    }

    public static String getCompletionForExecuteAtp11B(String executeAtp11B, String band) {
        String status = "no";
        if (band != null) {
            if (band.equalsIgnoreCase(ForesightConstants.STRING_2300) || band.equalsIgnoreCase(ForesightConstants.STRING_1800)) {
                if (executeAtp11B != null) {
                    if ((executeAtp11B.equalsIgnoreCase(ForesightConstants.CLOSE) || executeAtp11B.equalsIgnoreCase("Accepted_By_CTO") || executeAtp11B.equalsIgnoreCase("Accepted_By_Reliance_CTO")
                            || executeAtp11B.equalsIgnoreCase("On_Hold_For_WCC_Approval") || executeAtp11B.equalsIgnoreCase("Accepted_By_Auditor")
                            || executeAtp11B.equalsIgnoreCase("Rejected_By_Auditor"))) {
                        status = "yes";
                    } else {
                        status = "no";
                    }
                } else {
                    status = "no";
                }
            } else if (band.equalsIgnoreCase(ForesightConstants.STRING_850)) {
                if (executeAtp11B != null) {
                    if ((executeAtp11B.equalsIgnoreCase(ForesightConstants.CLOSE) || executeAtp11B.equalsIgnoreCase("Accepted_By_Reliance_CTO"))) {
                        status = "yes";
                    } else {
                        status = "no";
                    }
                } else {
                    status = "no";
                }
            }
        }
        return status;
    }

    public static String getCompletionForCommissionENodeB(String commissionEnodeB) {
        String status = "no";
        if (commissionEnodeB != null) {
            if (commissionEnodeB.equalsIgnoreCase(ForesightConstants.CLOSE) || commissionEnodeB.equalsIgnoreCase("On_Hold_For_WCC_Approval")
                    || commissionEnodeB.equalsIgnoreCase("Completed_By_Field_Engineer") || commissionEnodeB.equalsIgnoreCase("Completed_By_FE_And_LSMR")
                    || commissionEnodeB.equalsIgnoreCase("Rejected_By_Reliance") || commissionEnodeB.equalsIgnoreCase("Rejected_By_Reliance_CTO")
                    || commissionEnodeB.equalsIgnoreCase("Accepted_By_Reliance_CTO")) {
                status = "yes";
            } else {
                status = "no";
            }
        } else {
            status = "no";
        }
        return status;
    }

    public static String getCompletionForExecuteAtp11A(String executeAtp11A, String band) {
        String status = "no";
        if (band != null) {
            if (band.equalsIgnoreCase(ForesightConstants.STRING_2300) || band.equalsIgnoreCase(ForesightConstants.STRING_1800)) {
                if (executeAtp11A != null) {
                    if ((executeAtp11A.equalsIgnoreCase(ForesightConstants.CLOSE) || executeAtp11A.equalsIgnoreCase("Accepted_By_CTO") || executeAtp11A.equalsIgnoreCase("Assigned_To_Auditor")
                            || executeAtp11A.equalsIgnoreCase("Rejected_By_Auditor") || executeAtp11A.equalsIgnoreCase("Accepted_By_Auditor")
                            || executeAtp11A.equalsIgnoreCase("Accepted_By_Reliance_CTO") || executeAtp11A.equalsIgnoreCase("On_Hold_For_WCC_Approval")
                            || executeAtp11A.equalsIgnoreCase("Resubmitted_By_Samsung_QA_Manager"))) {
                        status = "yes";
                    } else {
                        status = "no";
                    }
                } else {
                    status = "no";
                }
            } else if (band.equalsIgnoreCase(ForesightConstants.STRING_850)) {
                if (executeAtp11A != null) {
                    if ((executeAtp11A.equalsIgnoreCase(ForesightConstants.CLOSE) || executeAtp11A.equalsIgnoreCase("Accepted_By_CTO") || executeAtp11A.equalsIgnoreCase("Assigned_To_Auditor")
                            || executeAtp11A.equalsIgnoreCase("Rejected_By_Auditor") || executeAtp11A.equalsIgnoreCase("Accepted_By_Auditor")
                            || executeAtp11A.equalsIgnoreCase("Accepted_By_Reliance_CTO") || executeAtp11A.equalsIgnoreCase("Accepted_By_Quality_Lead_For_WCC_Approval"))) {
                        status = "yes";
                    } else {
                        status = "no";
                    }
                } else {
                    status = "no";
                }
            }
        }
        return status;
    }

    public static String getCompletionForInstallENodeB(String installEnodeB) {
        String status = "no";
        if (installEnodeB != null) {
            if (installEnodeB.toString().equalsIgnoreCase(ForesightConstants.CLOSE)) {
                status = "yes";
            } else {
                status = "no";
            }
        } else {
            status = "no";
        }
        return status;
    }

    public static String getCompletionForRfe1Acceptance(String rfe1Acceptance) {
        String status = "no";
        if (rfe1Acceptance != null) {
            if (rfe1Acceptance.equalsIgnoreCase(ForesightConstants.CLOSE)) {
                status = "yes";
            } else {
                status = "no";
            }
        } else {
            status = "no";
        }
        return status;
    }

    public static String getNeIdByCellId(Integer cellId) {
        if (cellId != null) {
            switch (cellId) {
                case 11 :
                    return InfraConstants.NEID_111;
                case 12 :
                    return InfraConstants.NEID_211;
                case 13 :
                    return InfraConstants.NEID_311;
                case 21 :
                    return InfraConstants.NEID_112;
                case 22 :
                    return InfraConstants.NEID_212;
                case 23 :
                    return InfraConstants.NEID_312;
                case 31 :
                    return InfraConstants.NEID_131;
                case 32 :
                    return InfraConstants.NEID_231;
                case 33 :
                    return InfraConstants.NEID_331;
            }
        } else
            throw new RestException("cellId cannot be null.");
        return null;
    }

    public static List<Predicate> getPredicatesForNETaskDetail(CriteriaBuilder criteriaBuilder, Root<NETaskDetail> root, List<NEType> neTypeList, List<String> neNameList, List<String> neFrequencyList,
            List<NEStatus> neStatusList, List<String> neStagesList) {
        List<Predicate> predicates = new ArrayList<>();
        if (neTypeList != null && !neTypeList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(root.get(InfraConstants.NE_BANDDETAIL_KEY).get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY)).in(neTypeList));
        }
        if (neFrequencyList != null && !neTypeList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(root.get(InfraConstants.NE_BANDDETAIL_KEY).get(InfraConstants.NE_NEFREQUENCY_KEY)).in(neFrequencyList));
            predicates.add(root.get(InfraConstants.NE_BANDDETAIL_KEY).get(InfraConstants.NE_NEFREQUENCY_KEY).isNotNull());
        }
        if (neStatusList != null && !neStatusList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(root.get(InfraConstants.NE_BANDDETAIL_KEY).get(InfraConstants.NE_NESTATUS_KEY)).in(neStatusList));
        }
        if (neNameList != null && !neNameList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(root.get(InfraConstants.NE_BANDDETAIL_KEY).get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NENAME_KEY)).in(neNameList));
        }
        if (neStagesList != null && !neStagesList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(root.get(InfraConstants.NE_TASKDETAIL_NAME_KEY)).in(neNameList));
        }
        return predicates;
    }

    public static Integer getBandFromNeId(String neId) {
        if (neId != null) {
            neId = neId.substring(neId.length() - 3);
            try {
                if (neId.equalsIgnoreCase(InfraConstants.NEID_111) || neId.equalsIgnoreCase(InfraConstants.NEID_211) || neId.equalsIgnoreCase(InfraConstants.NEID_311)
                        || neId.equalsIgnoreCase(InfraConstants.NEID_112) || neId.equalsIgnoreCase(InfraConstants.NEID_212) || neId.equalsIgnoreCase(InfraConstants.NEID_312))
                    return InfraConstants.BAND2300;
                else if (neId.equalsIgnoreCase(InfraConstants.NEID_131) || neId.equalsIgnoreCase(InfraConstants.NEID_231) || neId.equalsIgnoreCase(InfraConstants.NEID_331))
                    return InfraConstants.BAND850;
                else
                    throw new RestException("invalid neId.");
            } catch (Exception exception) {
                logger.error("Error in getting band from neId exception {}", Utils.getStackTrace(exception));
            }
        } else
            throw new RestException("neId cannot be null.");
        return null;
    }

    public static Integer getSectorFromNeId(String neId) {
        if (neId != null) {
            neId = neId.substring(neId.length() - 3);
            try {
                if (neId.startsWith(InfraConstants.SECTORONE))
                    return InfraConstants.SECTOR_ONE;
                else if (neId.startsWith(InfraConstants.SECTORTWO))
                    return InfraConstants.SECTOR_TWO;
                else if (neId.startsWith(InfraConstants.SECTORTHREE))
                    return InfraConstants.SECTOR_THREE;
                else
                    throw new RestException("invalid neId.");
            } catch (Exception exception) {
                logger.error("Error in getting sector from neId exception {}", Utils.getStackTrace(exception));
            }
        } else
            throw new RestException("neId cannot be null.");
        return null;
    }

    public static String getCarrierFromNeId(String neId) {
        if (neId != null) {
            neId = neId.substring(neId.length() - 3);
            try {
                if (neId.endsWith(ForesightConstants.STRING_ONE))
                    return InfraConstants.CARRIER_FIRST;
                else if (neId.endsWith(ForesightConstants.STRING_TWO))
                    return InfraConstants.CARRIER_SECOND;
                else
                    throw new RestException("invalid neId.");
            } catch (Exception exception) {
                logger.error("Error in getting carrier from neId exception {}", Utils.getStackTrace(exception));
            }
        } else
            throw new RestException("neId cannot be null.");
        return null;
    }

    /**
     * Gets the carrier.
     *
     * @param carrier the carrier
     * @return the carrier
     */
    public static String getCarrier(String carrier) {
        if (InfraConstants.CARRIER_FIRST.equalsIgnoreCase(carrier))
            carrier = InfraConstants.C1;
        else if (InfraConstants.CARRIER_SECOND.equalsIgnoreCase(carrier))
            carrier = InfraConstants.C2;
        return carrier;
    }

    public static List<String> getNeIdByNenameAndCellid(List<String> list) throws Exception {
        List<String> listOutput = new ArrayList<>();

        try {
            list.stream().forEach(s -> {
                try {
                    if (s != null) {
                        String neId = getNeIdByCellIdAndNename(s.substring(0, s.lastIndexOf("_")), Integer.parseInt(s.substring(s.lastIndexOf("_") + 1)));
                        if (neId != null && neId.length() > 0) {
                            listOutput.add(neId);
                        }
                    }
                } catch (Exception exception) {
                    logger.error("Unable to get neid");
                }
            });
        } catch (Exception exception) {
            logger.error("Unable to get neid due to exception {}", Utils.getStackTrace(exception));
        }
        return listOutput;
    }

    public static String getNeIdByCellIdAndNename(String neName, Integer cellId) {
        String newNename = null;
        if (neName != null) {
            newNename = neName + ForesightConstants.UNDERSCORE + getNeIdByCellId(cellId);
        } else
            throw new RestException("neName cannot be null.");
        return newNename;
    }

    public static Integer getCellIdByNeId(Integer NeId) {
        if (NeId != null) {
            switch (NeId) {
                case 111 :
                    return InfraConstants.CELLID_11;
                case 211 :
                    return InfraConstants.CELLID_12;
                case 311 :
                    return InfraConstants.CELLID_13;
                case 112 :
                    return InfraConstants.CELLID_21;
                case 212 :
                    return InfraConstants.CELLID_22;
                case 312 :
                    return InfraConstants.CELLID_23;
                case 131 :
                    return InfraConstants.CELLID_31;
                case 231 :
                    return InfraConstants.CELLID_32;
                case 331 :
                    return InfraConstants.CELLID_33;
            }
        } else
            throw new RestException("NeId cannot be null.");
        return null;
    }

    public static List<List<Double>> getBoundary(String string) {
        Gson json = new Gson();
        List<List<Double>> boundary = new ArrayList<>();
        try {
            boundary = json.fromJson(string, new TypeToken<List<List<Double>>>() {}.getType());
        } catch (Exception exception) {
            logger.error("unable to get boundary. Message {} ", exception.getMessage());
        }
        return boundary;
    }

    public static String getCellIdAndNenameByNeId(String neName, Integer cellId) {
        String newNename = null;
        if (neName != null) {
            newNename = neName + ForesightConstants.UNDERSCORE + getCellIdByNeId(cellId);
        } else
            throw new RestException("neName cannot be null.");
        return newNename;
    }

    public static List<String> getCellIdByNeIds(List<String> neIdList) {
        List<String> listOutput = new ArrayList<>();
        try {
            if (CollectionUtils.isNotEmpty(neIdList)) {
                neIdList.parallelStream().forEach(s -> {
                    try {
                        if (s != null && s.split(Symbol.UNDERSCORE_STRING).length > 2) {
                            String neId = getCellIdAndNenameByNeId(s.substring(0, s.lastIndexOf(Symbol.UNDERSCORE_STRING)), Integer.parseInt(s.substring(s.lastIndexOf(Symbol.UNDERSCORE_STRING) + 1)));
                            if (neId != null && neId.length() > 0) {
                                listOutput.add(neId);
                            }
                        }
                    } catch (Exception exception) {
                        logger.error("Unable to get cellId");
                    }
                });
            }
        } catch (Exception exception) {
            logger.error("Unable to get cellId due to exception {}", Utils.getStackTrace(exception));
        }
        return listOutput;
    }

    public static String getSiteDetailByCellId(int cellId, String cellType) {

        final String EARFCN = "EARFCN";
        final String TECHNOLOGY = "TECHNOLOGY";
        final String CARRIER = "CARRIER";
        final String SECTOR = "SECTOR";
        final String BAND = "BAND";
        final String ERROR_MSG = "Please Enter Valid Input";

        if (cellType != null && isValidCellId(cellId)) {

            if (cellType.equals(EARFCN)) {
                return getEARFCN(cellId);
            } else if (cellType.equals(TECHNOLOGY)) {
                return getTechnology(cellId);
            } else if (cellType.equals(CARRIER)) {
                return getCarrier(cellId);
            } else if (cellType.equals(SECTOR)) {
                return getSector(cellId);
            } else if (cellType.equals(BAND)) {
                return getBand(cellId);
            }

            return ERROR_MSG;
        }

        return ERROR_MSG;
    }

    public static boolean isValidCellId(int cellId) {
        if (cellId == 11 || cellId == 12 || cellId == 13 || cellId == 21 || cellId == 22 || cellId == 23 || cellId == 31 || cellId == 32 || cellId == 33) {
            return true;
        }
        return false;
    }

    public static String getEARFCN(int cellId) {
        final String FIRST_EARFCN = "39050";
        final String SECOND_EARFCN = "39194";
        final String THIRD_EARFCN = "2450";
        final String ERROR_MSG = "Invalid Cell Id";

        if (cellId == 11 || cellId == 12 || cellId == 13) {

            return FIRST_EARFCN;
        } else if (cellId == 21 || cellId == 22 || cellId == 23) {

            return SECOND_EARFCN;
        } else if (cellId == 31 || cellId == 32 || cellId == 33) {

            return THIRD_EARFCN;
        }
        return ERROR_MSG;

    }

    public static String getBand(int cellId) {
        final String BAND1 = "2300";
        final String BAND2 = "850";
        final String ERROR_MSG = "Invalid Cell Id";
        if (cellId == 11 || cellId == 12 || cellId == 13 || cellId == 21 || cellId == 22 || cellId == 23) {

            return BAND1;
        } else if (cellId == 31 || cellId == 32 || cellId == 33) {

            return BAND2;
        }
        return ERROR_MSG;
    }

    public static String getTechnology(int cellId) {
        final String TDD = "TDD";
        final String FDD = "FDD";
        final String ERROR_MSG = "Invalid Cell Id";

        if (cellId == 11 || cellId == 12 || cellId == 13 || cellId == 21 || cellId == 22 || cellId == 23) {

            return TDD;
        } else if (cellId == 31 || cellId == 32 || cellId == 33) {

            return FDD;
        }
        return ERROR_MSG;
    }

    public static String getCarrier(int cellId) {
        final String FIRST_CARRIER = "FIRST_CARRIER";
        final String SECOND_CARRIER = "SECOND_CARRIER";
        final String ERROR_MSG = "Invalid Cell Id";

        if (cellId == 11 || cellId == 12 || cellId == 13 || cellId == 31 || cellId == 32 || cellId == 33) {

            return FIRST_CARRIER;
        } else if (cellId == 21 || cellId == 22 || cellId == 23) {

            return SECOND_CARRIER;
        }
        return ERROR_MSG;
    }

    public static String getSector(int cellId) {
        final String SECTOR1 = "1";
        final String SECTOR2 = "2";
        final String SECTOR3 = "3";
        final String ERROR_MSG = "Invalid Cell Id";

        if (cellId == 11 || cellId == 21 || cellId == 31) {

            return SECTOR1;
        } else if (cellId == 12 || cellId == 22 || cellId == 32) {

            return SECTOR2;
        } else if (cellId == 13 || cellId == 23 || cellId == 33) {

            return SECTOR3;
        }
        return ERROR_MSG;
    }

    public static String exportExcelFile(XSSFWorkbook workBook, String filePath, String fileName, Boolean generateReport) {
        logger.info("Going to export filename :{} filepath : {}", fileName, filePath);
        String finalUrl = ForesightConstants.BLANK_STRING;
        try {
            if (ConfigUtils.getString(ConfigUtil.DOWNLOAD_AUTH_PATH_AVAILABLE).equalsIgnoreCase("true")) {

                String path = ForesightConstants.FORWARD_SLASH + ConfigUtils.getString(ConfigEnum.DOWNLOAD_AUTH_PATH.getValue()) + ForesightConstants.FORWARD_SLASH + filePath;
                File file = new File(path);
                if (!file.isFile() && !file.getParentFile().exists()) {
                    file.mkdirs();
                    logger.info("Directory has been created successfully, path is :{}", file.getParentFile());
                }
                file = new File(file.getAbsolutePath());
                if (!file.getAbsoluteFile().exists()) {
                    file.mkdirs();
                }
                exportExcel(file.getAbsolutePath() + ForesightConstants.FORWARD_SLASH + fileName, workBook);
                logger.info("path: {}", filePath + fileName);
                if (generateReport) {
                    finalUrl = new GenerateReportURL(filePath + fileName).toString();
                }

            } else {
                String path = System.getProperty(ForesightConstants.TOMCAT_PATH) + ForesightConstants.FORWARD_SLASH + filePath;
                File file = new File(path);
                if (!file.isFile() && !file.getParentFile().exists()) {
                    file.mkdirs();
                    logger.info("Directory has been created successfully, path is :{}", file.getParentFile());
                }
                file = new File(file.getAbsolutePath());
                if (!file.getAbsoluteFile().exists()) {
                    file.mkdirs();
                }
                exportExcel(file.getAbsolutePath() + ForesightConstants.FORWARD_SLASH + fileName, workBook);
                if (generateReport) {
                    finalUrl = ConfigUtils.getString(ConfigEnum.SITE_REPORT_DOWNLOAD_PATH.getValue()) + fileName;
                    logger.info("finalURL : {}", finalUrl);
                }
            }
            logger.info("Return final Generate Report URL : {}", finalUrl);
        } catch (IOException e) {
            logger.error("Exception in writing file, error = {}", Utils.getStackTrace(e));
        }
        return finalUrl;
    }

    public static File exportExcel(String excelFileName, XSSFWorkbook wb) throws IOException {
        logger.info("excelfilename: {}", excelFileName);
        FileOutputStream fileOut = new FileOutputStream(excelFileName);
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        return new File(excelFileName);
    }

    public static String checkNullString(String value) {
        String REPLACEMENT_DASH = InfraConstants.DASH;
        if (value != null && !value.isEmpty() && !value.equalsIgnoreCase(ForesightConstants.NULL_STRING) && !value.equals(ForesightConstants.HIPHEN) && !value.equals(ForesightConstants.DOT)) {
            return value.replace(InfraConstants.COMMA, InfraConstants.SPACE);
        } else {
            return REPLACEMENT_DASH;
        }
    }

    public static String getMorphologyName(String morphology) {
        if (morphology != null) {
            if (morphology.equalsIgnoreCase(InfraConstants.DU_CATEGORY))
                return InfraConstants.DENSE_URBAN;
            else if (morphology.equalsIgnoreCase(InfraConstants.UR_CATEGORY))
                return InfraConstants.URBAN;
            else if (morphology.equalsIgnoreCase(InfraConstants.SU_CATEGORY))
                return InfraConstants.SUB_URBAN;
            else if (morphology.equalsIgnoreCase(InfraConstants.RU_CATEGORY))
                return InfraConstants.RURAL;
            else if (morphology.equalsIgnoreCase(InfraConstants.IN_CATEGORY))
                return InfraConstants.INDUSTRIAL;
            else if (morphology.equalsIgnoreCase(InfraConstants.WB_CATEGORY))
                return InfraConstants.WATERBODY;

        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getValueByDataType(String dataType, T value) {
        logger.info("inside getValueByDataType for dataType : {} value : {}", dataType, value);
        T returnValue = null;
        try {
            if (dataType.equals("Double")) {
                returnValue = (T) Double.valueOf((String) value);
            }
            if (dataType.equals("String")) {
                returnValue = value;
            }
            if (dataType.equals("Integer")) {
                returnValue = (T) Integer.valueOf((String) value);
            }
            if (dataType.equals("NEType")) {
                returnValue = (T) getCorrectSiteType((String) value);
            }
            if (dataType.equals("NELType")) {
                returnValue = (T) getCorrectNELType((String) value);
            }
            
            if (dataType.equals("Domain")) {
                returnValue = (T) getCorrectDomainValue((String) value);
            }
            if (dataType.equals("NEStatus")) {
                returnValue = (T) getCorrectSiteStatus((String) value);
            }
            if (dataType.equals("Vendor")) {
                returnValue = (T) getCorrectVendor((String) value);
            }
            if (dataType.equals("Technology")) {
                returnValue = (T) getCorrectTechnology((String) value);
            }
            if (dataType.equals("TimeStamp")) {
                returnValue = (T) new Date(Long.valueOf((String) value));
            }
            if (dataType.equals("Date")) {
        		SimpleDateFormat format = new SimpleDateFormat(ForesightConstants.DATE_FORMAT_dd_MM_yy);
                returnValue = (T)format.parse((String) value);
            }
        } catch (Exception exception) {
            logger.error("Unable to get Dta type for dataType {} ,value {} ,exception {} ", dataType, value, Utils.getStackTrace(exception));

        }
        return returnValue;
    }

    public static NEType getCorrectSiteType(String sitetype) {
        Optional<String> optionalvalue = Optional.ofNullable(sitetype);
        return optionalvalue.isPresent() ? NEType.valueOf(optionalvalue.get()) : null;

    }
    

    public static NELType getCorrectNELType(String sitetype) {
        Optional<String> optionalvalue = Optional.ofNullable(sitetype);
        return optionalvalue.isPresent() ? NELType.valueOf(optionalvalue.get()) : null;

    }

    public static Domain getCorrectDomainValue(String sitetype) {
        Optional<String> optionalvalue = Optional.ofNullable(sitetype);
        return optionalvalue.isPresent() ? Domain.valueOf(optionalvalue.get()) : null;
    }

    public static NEStatus getCorrectSiteStatus(String sitetype) {
        Optional<String> optionalvalue = Optional.ofNullable(sitetype);
        return optionalvalue.isPresent() ? NEStatus.valueOf(optionalvalue.get()) : null;
    }

    public static Vendor getCorrectVendor(String sitetype) {
        Optional<String> optionalvalue = Optional.ofNullable(sitetype);
        return optionalvalue.isPresent() ? Vendor.valueOf(optionalvalue.get()) : null;
    }

    public static Technology getCorrectTechnology(String sitetype) {
        Optional<String> optionalvalue = Optional.ofNullable(sitetype);
        return optionalvalue.isPresent() ? Technology.valueOf(optionalvalue.get()) : null;
    }

    public static List<Predicate> getPredicateForNetworkElement(CriteriaBuilder criteriaBuilder, List<Predicate> predicateList, Root<NetworkElement> networkRoot, Root<NEBandDetail> neBandDetail) {
        try {
            logger.info("getPredicateForNetworkElement method called");
            predicateList.add(criteriaBuilder.equal(networkRoot.get("id"), neBandDetail.get("networkElement")));
            predicateList.add(criteriaBuilder.equal(networkRoot.get("isDeleted"), 0));
            predicateList.add(networkRoot.get("latitude").isNotNull());
            predicateList.add(networkRoot.get("longitude").isNotNull());
            logger.info("getPredicateForNetworkElement method end");
        } catch (Exception exception) {
            logger.error("Unable to get Predicate for Networkelement ", Utils.getStackTrace(exception));
        }
        return predicateList;
    }

    public static String getGeographyLevelForAggregation(Integer zoomLevel) {
        try {
            geographyL1Zoom = 5;// Utils.toInteger(systemConfigMap.get(InfraConstants.GEOGRAPHYL1));
            geographyL2Zoom = 7;// Utils.toInteger(systemConfigMap.get(InfraConstants.GEOGRAPHYL2));
            geographyL3Zoom = 9;// Utils.toInteger(systemConfigMap.get(InfraConstants.GEOGRAPHYL3));
            geographyL4Zoom = 11; // Utils.toInteger(systemConfigMap.get(InfraConstants.GEOGRAPHYL4));

            if (zoomLevel >= geographyL1Zoom && zoomLevel < geographyL2Zoom) {
                return InfraConstants.GEOGRAPHYL1_TABLE;
            }
            if (zoomLevel >= geographyL2Zoom && zoomLevel < geographyL3Zoom) {
                return InfraConstants.GEOGRAPHYL2_TABLE;
            }
            if (zoomLevel >= geographyL3Zoom && zoomLevel < geographyL4Zoom) {
                return InfraConstants.GEOGRAPHYL3_TABLE;
            }
            if (zoomLevel >= geographyL4Zoom) {
                return InfraConstants.GEOGRAPHYL4_TABLE;
            }
            return ForesightConstants.BLANK_STRING;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RestException(exception.getMessage());
        }
    }

	public static String generateNeId(String neName, String sector, String band, String carrier, String type) {
		String neId = null;
		try {
			logger.info("Going to generate NEId by neName:{},sector: {},band: {},carrier: {},type: {} ", neName, sector, band, carrier, type);
			if (neName != null && sector != null && band != null && carrier != null && type != null && !neName.equalsIgnoreCase(InfraConstants.BLANK_STRING)
					&& !sector.equalsIgnoreCase(InfraConstants.BLANK_STRING) && !band.equalsIgnoreCase(InfraConstants.BLANK_STRING) && !carrier.equalsIgnoreCase(InfraConstants.BLANK_STRING)) {
				if (systemConfigMap != null && !systemConfigMap.isEmpty()) {
					if (type.equalsIgnoreCase("SITE")) {
						String bandValue = getMapFromJsonObject(getJsonFromString(systemConfigMap.get(InfraConstants.FREQUENCY_MAPPING))) != null
								? getMapFromJsonObject(getJsonFromString(systemConfigMap.get(InfraConstants.FREQUENCY_MAPPING))).get(band)
								: null;
						String sectorValue = getMapFromJsonObject(getJsonFromString(systemConfigMap.get(InfraConstants.SECTOR_MAPPING))) != null
								? getMapFromJsonObject(getJsonFromString(systemConfigMap.get(InfraConstants.SECTOR_MAPPING))).get(sector)
								: null;
						String carrierValue = getMapFromJsonObject(getJsonFromString(systemConfigMap.get(InfraConstants.CARRIER_MAPPING))) != null
								? getMapFromJsonObject(getJsonFromString(systemConfigMap.get(InfraConstants.CARRIER_MAPPING))).get(carrier)
								: null;
						if (bandValue != null && sectorValue != null && carrierValue != null) {
							neId = neName.concat(InfraConstants.UNDERSCORE).concat(sectorValue).concat(bandValue).concat(carrierValue);
							logger.info("Generated NEId :{}", neId);
						} else {
							throw new RestException(InfraConstants.EXCEPTION_SOMETHING_WENT_WRONG);
						}
					} else {
						throw new RestException("Invalid Type Key");
					}
				} else {
					throw new RestException("SystemConfiguration Map is Empty");

				}
			} else {
				throw new RestException(InfraConstants.EXCEPTION_INVALID_PARAMS);
			}

		} catch (RestException e) {
			throw new RestException(e.getMessage());
		}
		return neId;
	}
	
	public static JSONObject getJsonFromString(String key) throws RestException {
		try {
			if (key != null && !key.equalsIgnoreCase(InfraConstants.BLANK_STRING)) {
				return (JSONObject) new JSONParser().parse(key);
			} else {
				throw new RestException("Key is Invalid"+key);
			}

		} catch (RestException rest) {
			throw new RestException(rest.getMessage());
		} catch (ParseException e) {
			logger.error("Unable to parse String {} into Json Object ", key, Utils.getStackTrace(e));
			throw new RestException("Invalid input key");
		}
	}

	public static Map<String, String> getMapFromJsonObject(JSONObject jsonKey) throws RestException {
		if (jsonKey != null && !jsonKey.isEmpty()) {
			return new Gson().fromJson(jsonKey.toString(), HashMap.class);
		} else {
			throw new RestException("Invalid jsonKey"+jsonKey);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static Map validateViewPort(Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat) throws RestException {
		logger.info("Going to validate viewport southWestLong : {}, southWestLat : {},  northEastLong : {}, northEastLat : {} ", southWestLong, southWestLat, northEastLong, northEastLat);
		Map<String, Double> viewport = new HashMap();
		viewport.put(InfraConstants.SW_LONGITUDE, validateLong(southWestLong));
		viewport.put(InfraConstants.SW_LATITUDE, validateLat(southWestLat));
		viewport.put(InfraConstants.NW_LONG, validateLong(northEastLong));
		viewport.put(InfraConstants.NW_LAT, validateLat(northEastLat));

		return viewport;
	}

	public static Double validateLat(Double latitude) {
		if (latitude != null && latitude > -90 && latitude < 90) {
			return latitude;
		} else {
			throw new RestException("Invalid Latitude");
		}
	}

	public static Double validateLong(Double longitude) {
		if (longitude != null && longitude > -180 && longitude < 180) {
			return longitude;
		} else {
			throw new RestException("Invalid Longitude");
		}
	}

	public static Double validateLatLong(Double value)  {
		try {
			Optional<Double> optionalLatitude = Optional.ofNullable(value);
			return optionalLatitude.isPresent() ? optionalLatitude.get() : optionalLatitude.orElseThrow(() -> new RestException("INVALID VALUE"));
		} catch (Exception e) {
			logger.error("Invalid LatLong {}",e.getMessage());
			throw new RestException("Invalid LatLong");
		}
	}

    @SuppressWarnings({"rawtypes"})
    public static List<Predicate> getPredicatesForNEDetails(CriteriaBuilder criteriaBuilder, Root<NetworkElement> root, List filterList, Map geographyMap, Map viewportMap) {
        logger.info("Going to apply predicates for NE Details.");
        List<Predicate> predicates = new ArrayList<>();
        try {
            addPredicatesForNEColumnDetails(criteriaBuilder, root, filterList, predicates);
            addPredicatesForGeographyDetails(criteriaBuilder, root, geographyMap, predicates);
            addPredicatesForViewportDetails(criteriaBuilder, root, viewportMap, predicates);
        } catch (Exception exception) {
            logger.error("Error in getting predicates for NE details Message : {}", exception.getMessage());
        }
        return predicates;
    }

    @SuppressWarnings("rawtypes")
    public static List<Predicate> addPredicatesForNEColumnDetails(CriteriaBuilder criteriaBuilder, Root<NetworkElement> root, List filterList, List<Predicate> predicates) {
        try {
            if (filterList != null && !filterList.isEmpty()) {
                for (int index = 0; index < filterList.size(); index++) {
                    try {
                        Map<?, ?> filterDetailMap = (Map) filterList.get(index);
                        if (filterDetailMap != null && !filterDetailMap.isEmpty()) {
                            if (filterDetailMap.containsKey(InfraConstants.NE_OPERATION_KEY)) {
                                if (filterDetailMap.get(InfraConstants.NE_OPERATION_KEY) != null
                                        && filterDetailMap.get(InfraConstants.NE_OPERATION_KEY).toString().equalsIgnoreCase(InfraConstants.IN_OPERATOR)) {
                                    if (filterDetailMap.get(InfraConstants.NE_DATATYPE_KEY) != null && filterDetailMap.get(InfraConstants.NE_VALUE_KEY) != null) {
                                        List resultList = getResultDataList((List) filterDetailMap.get(InfraConstants.NE_VALUE_KEY), filterDetailMap.get(InfraConstants.NE_DATATYPE_KEY).toString());
                                        if (resultList != null) {
                                            predicates.add(criteriaBuilder.upper(root.get(filterDetailMap.get(InfraConstants.NE_LABELTYPE_KEY).toString())).in(resultList));
                                        }
                                    }
                                } else if (filterDetailMap.get(InfraConstants.NE_OPERATION_KEY) != null
                                        && filterDetailMap.get(InfraConstants.NE_OPERATION_KEY).toString().equalsIgnoreCase(InfraConstants.EQUALS_OPERATOR)) {
                                    if (filterDetailMap.get(InfraConstants.NE_DATATYPE_KEY) != null && filterDetailMap.get(InfraConstants.NE_VALUE_KEY) != null) {
                                        if (getValueByDataType(filterDetailMap.get(InfraConstants.NE_DATATYPE_KEY).toString(), filterDetailMap.get(InfraConstants.NE_VALUE_KEY)) != null) {
                                            predicates.add(criteriaBuilder.equal(root.get(filterDetailMap.get(InfraConstants.NE_LABELTYPE_KEY).toString()),
                                                    getValueByDataType(filterDetailMap.get(InfraConstants.NE_DATATYPE_KEY).toString(), filterDetailMap.get(InfraConstants.NE_VALUE_KEY))));
                                        }
                                    }
                                }
                                predicates.add(root.get(filterDetailMap.get(InfraConstants.NE_LABELTYPE_KEY).toString()).isNotNull());
                            }
                        }
                    } catch (Exception exception) {
                        logger.error("Error in adding predicates for NEDetail Message : {}", exception.getMessage());
                    }
                }
            }
        } catch (Exception exception) {
            logger.error("Unable to add predicates for column details Message : {}", exception.getMessage());
        }
        return predicates;
    }

    @SuppressWarnings({"rawtypes"})
    public static List<Predicate> addPredicatesForGeographyDetails(CriteriaBuilder criteriaBuilder, Root<NetworkElement> root, Map geographyMap, List<Predicate> predicates) {
        try {
            if (geographyMap != null && !geographyMap.isEmpty()) {
                if (geographyMap.containsKey(InfraConstants.NE_GEOGRAPHYL4_KEY)) {
                    predicates.add(criteriaBuilder.upper(root.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.BOUNDARY_NAME)).in(geographyMap.get(InfraConstants.NE_GEOGRAPHYL4_KEY)));
                } else if (geographyMap.containsKey(InfraConstants.NE_GEOGRAPHYL3_KEY)) {
                    predicates.add(criteriaBuilder.upper(root.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.BOUNDARY_NAME))
                            .in(geographyMap.get(InfraConstants.NE_GEOGRAPHYL3_KEY)));
                } else if (geographyMap.containsKey(InfraConstants.NE_GEOGRAPHYL2_KEY)) {
                    predicates.add(criteriaBuilder
                            .upper(root.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.BOUNDARY_NAME))
                            .in(geographyMap.get(InfraConstants.NE_GEOGRAPHYL2_KEY)));
                } else if (geographyMap.containsKey(InfraConstants.NE_GEOGRAPHYL1_KEY)) {
                    predicates.add(criteriaBuilder.upper(root.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY)
                            .get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.BOUNDARY_NAME)).in(geographyMap.get(InfraConstants.NE_GEOGRAPHYL1_KEY)));
                }
            }
        } catch (Exception exception) {
            logger.error("Unable to add predicates for geography details Message : {}", exception.getMessage());
        }
        return predicates;
    }

    @SuppressWarnings({"rawtypes"})
    public static List<Predicate> addPredicatesForViewportDetails(CriteriaBuilder criteriaBuilder, Root<NetworkElement> root, Map viewportMap, List<Predicate> predicates) {
        try {
            if (viewportMap != null && !viewportMap.isEmpty()) {
                if (viewportMap.containsKey(InfraConstants.SOUTHWEST_LATITUDE_KEY)) {
                    predicates.add(criteriaBuilder.greaterThan(root.get(InfraConstants.NE_LATITUDE_KEY), viewportMap.get(InfraConstants.SOUTHWEST_LATITUDE_KEY).toString()));
                }
                if (viewportMap.containsKey(InfraConstants.NORTHEAST_LATITUDE_KEY)) {
                    predicates.add(criteriaBuilder.lessThan(root.get(InfraConstants.NE_LATITUDE_KEY), viewportMap.get(InfraConstants.NORTHEAST_LATITUDE_KEY).toString()));
                }
                if (viewportMap.containsKey(InfraConstants.SOUTHWEST_LONGITUDE_KEY)) {
                    predicates.add(criteriaBuilder.greaterThan(root.get(InfraConstants.NE_LONGITUDE_KEY), viewportMap.get(InfraConstants.SOUTHWEST_LONGITUDE_KEY).toString()));
                }
                if (viewportMap.containsKey(InfraConstants.NORTHEAST_LONGITUDE_KEY)) {
                    predicates.add(criteriaBuilder.lessThan(root.get(InfraConstants.NE_LONGITUDE_KEY), viewportMap.get(InfraConstants.NORTHEAST_LONGITUDE_KEY).toString()));
                }
            }
        } catch (Exception exception) {
            logger.error("Unable to add predicates for viewport details Message : {}", exception.getMessage());
        }
        return predicates;
    }

    @SuppressWarnings({"rawtypes"})
    public static List getResultDataList(List dataList, String dataType) {
        return populateResultDataList(dataList, dataType);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List populateResultDataList(List dataList, String dataType) {
        List resultList = new ArrayList();
        for (int index = 0; index < dataList.size(); index++) {
            try {
                if (dataType.equals(InfraConstants.VENDOR_ENUM_KEY)) {
                    resultList.add(Vendor.valueOf(dataList.get(index).toString()));
                } else if (dataType.equals(InfraConstants.NETYPE_ENUM_KEY)) {
                    resultList.add(NEType.valueOf(dataList.get(index).toString()));
                } else if (dataType.equals(InfraConstants.DOMAIN_ENUM_KEY)) {
                    resultList.add(Domain.valueOf(dataList.get(index).toString()));
                } else if (dataType.equals(InfraConstants.INTEGER_DATATYPE_KEY)) {
                    resultList.add(Integer.valueOf(dataList.get(index).toString()));
                } else if (dataType.equals(InfraConstants.STRING_DATATYPE_KEY)) {
                    resultList.add(String.valueOf(dataList.get(index).toString()));
                } else if (dataType.equals(InfraConstants.DOUBLE_DATATYPE_KEY)) {
                    resultList.add(Double.valueOf(dataList.get(index).toString()));
                }
            } catch (Exception exception) {
                logger.error("Error in populating data in result list Message : {}", exception.getMessage());
            }
        }
        return resultList;
    }

    @SuppressWarnings("rawtypes")
    public static Order[] getOrderObject(Root<NetworkElement> root, List orderByList, CriteriaBuilder criteriaBuilder) {
        Order[] orders = null;
        try {
            if (orderByList != null && !orderByList.isEmpty()) {
                orders = new Order[orderByList.size()];
                for (int index = 0; index < orderByList.size(); index++) {
                    try {
                        orders[index] = criteriaBuilder.asc(root.get(orderByList.get(index).toString()));
                    } catch (Exception exception) {
                        logger.error("Error in adding parameters Message : {}", exception.getMessage());
                    }
                }
            }
        } catch (Exception exception) {
            logger.error("Error in adding parameters in Order list Message : {}", exception.getMessage());
        }
        return orders;
    }

    @SuppressWarnings("rawtypes")
    public static Expression<?>[] getExpressionObject(Root<NetworkElement> root, List groupByList) {
        Expression<?>[] expressions = null;
        if (groupByList != null && !groupByList.isEmpty()) {
            expressions = new Expression<?>[groupByList.size()];
            for (int index = 0; index < groupByList.size(); index++) {
                try {
                    expressions[index] = root.get(groupByList.get(index).toString());
                } catch (Exception exception) {
                    logger.error("Error in adding parameters in Expression list Message : {}", exception.getMessage());
                }
            }
        }
        return expressions;
    }

    public static CriteriaQuery<Tuple> getCriteriaQueryForGroupBy(CriteriaQuery<Tuple> criteriaTupleQuery, Expression<?>[] expressions) {
        try {
            if (expressions != null && expressions.length > 0) {
                criteriaTupleQuery = criteriaTupleQuery.groupBy(expressions);
            }
        } catch (Exception exception) {
            logger.error("Error in getting CriteriaQuery for GroupBy expression list Message : {}", exception.getMessage());
        }
        return criteriaTupleQuery;
    }

    public static CriteriaQuery<Tuple> getCriteriaQueryForOrderBy(CriteriaQuery<Tuple> criteriaTupleQuery, Order[] orders) {
        try {
            if (orders != null && orders.length > 0) {
                criteriaTupleQuery = criteriaTupleQuery.orderBy(orders);
            }
        } catch (Exception exception) {
            logger.error("Error in getting CriteriaQuery for Orderby list Message : {}", exception.getMessage());
        }
        return criteriaTupleQuery;
    }

    public static CriteriaQuery<Tuple> getCriteriaQueryForSelection(CriteriaBuilder criteriaBuilder, CriteriaQuery<Tuple> criteriaTupleQuery, Root<NetworkElement> networkElement,
            Selection<?>[] selections, List<Predicate> predicates, boolean isDistinctClause) {
        try {
            if (selections != null && selections.length > 0) {
                if (predicates != null && !predicates.isEmpty()) {
                    criteriaTupleQuery = criteriaTupleQuery.select(criteriaBuilder.tuple(selections)).where(predicates.toArray(new Predicate[]{})).distinct(isDistinctClause);
                } else {
                    criteriaTupleQuery = criteriaTupleQuery.select(criteriaBuilder.tuple(selections)).distinct(isDistinctClause);
                }
            } else {
                if (predicates != null && !predicates.isEmpty()) {
                    return criteriaTupleQuery.select(criteriaBuilder.tuple(networkElement)).where(predicates.toArray(new Predicate[]{})).distinct(isDistinctClause);
                } else {
                    criteriaTupleQuery = criteriaTupleQuery.select(criteriaBuilder.tuple(networkElement)).distinct(isDistinctClause);
                }
            }
        } catch (Exception exception) {
            logger.error("Error in getting CriteriaQuery for Selection list Message : {}", Utils.getStackTrace(exception));
        }
        return criteriaTupleQuery;
    }

    public static List<Selection<?>> getSelectionForSitesForViewPort(Map<String, Double> viewMap, Root<NetworkElement> elementRoot, List colunmsName, List<Selection<?>> selection,
            CriteriaBuilder criteriaBuilder) {
        try {
            if (elementRoot != null && colunmsName != null && !colunmsName.isEmpty()) {
                for (int i = 0; i < colunmsName.size(); i++) {
                    if (colunmsName.get(i).toString().equalsIgnoreCase(InfraConstants.NE_LATITUDE_KEY)) {
                        selection
                                .add(criteriaBuilder
                                        .avg(criteriaBuilder.<Number>selectCase()
                                                .when(criteriaBuilder.between(elementRoot.get(InfraConstants.NE_LATITUDE_KEY), viewMap.get(InfraConstants.SW_LATITUDE),
                                                        viewMap.get(InfraConstants.NW_LAT)), elementRoot.get(InfraConstants.NE_LATITUDE_KEY))
                                                .otherwise(criteriaBuilder.nullLiteral(Number.class)))
                                        .alias(InfraConstants.NE_LATITUDE_KEY));

                    } else if (colunmsName.get(i).toString().equalsIgnoreCase(InfraConstants.NE_LONGITUDE_KEY)) {
                        selection
                                .add(criteriaBuilder
                                        .avg(criteriaBuilder.<Number>selectCase()
                                                .when(criteriaBuilder.between(elementRoot.get(InfraConstants.NE_LONGITUDE_KEY), viewMap.get(InfraConstants.SW_LONGITUDE),
                                                        viewMap.get(InfraConstants.NW_LONG)), elementRoot.get(InfraConstants.NE_LONGITUDE_KEY))
                                                .otherwise(criteriaBuilder.nullLiteral(Number.class)))
                                        .alias(InfraConstants.NE_LONGITUDE_KEY));
                    } else {
                        selection.add(elementRoot.get(colunmsName.get(i).toString()).alias(colunmsName.get(i).toString()));
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            logger.error("Unable to get lat long");
        }

        return selection;
    }

    @SuppressWarnings("rawtypes")
    public static Selection<?>[] getSelectionObject(Root<NetworkElement> parentRoot, Root<NetworkElement> childRoot, List projectionList, List orderByList, boolean isDistinct, List groupByList) {
        Selection<?>[] selections = null;
        if (projectionList != null && !projectionList.isEmpty()) {
            List targetList = new ArrayList();
            if (isDistinct) {
                if (orderByList != null && !orderByList.isEmpty()) {
                    targetList = orderByList;
                } else {
                    targetList = projectionList;
                }
            } else if (groupByList != null && !groupByList.isEmpty()) {
                targetList = groupByList;
            } else {
                targetList = projectionList;
            }
            selections = new Selection<?>[targetList.size()];
            String targetColumn = ForesightConstants.BLANK_STRING;
            String aliasName = ForesightConstants.BLANK_STRING;
            for (int index = 0; index < targetList.size(); index++) {
                try {
                    targetColumn = targetList.get(index).toString();
                    aliasName = targetList.get(index).toString();
                    if (targetColumn.equalsIgnoreCase(InfraConstants.NE_PARENTNENAME_KEY)) {
                        selections[index] = parentRoot.get(InfraConstants.NE_NENAME_KEY).alias(InfraConstants.NE_PARENTNENAME_KEY);
                    } else if (targetColumn.equalsIgnoreCase(InfraConstants.NE_PARENTNEID_KEY)) {
                        selections[index] = parentRoot.get(InfraConstants.NE_NEID_KEY).alias(InfraConstants.NE_PARENTNEID_KEY);
                    } else {
                        selections[index] = childRoot.get(targetColumn).alias(aliasName);
                    }
                } catch (Exception exception) {
                    logger.error("Error in adding parameters in Selection list Message : {}", exception.getMessage());
                }
            }
        }
        return selections;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Map mergeMapByKey(String key, Map map, List list) {
        try {
            if (key != null && map != null && !map.isEmpty() && !list.isEmpty() && list != null) {
                map.merge(key, list, (l1, l2) -> {
                    List tempList = new ArrayList<>((Collection) l1);
                    tempList.addAll((Collection) l2);
                    return tempList;
                });
            }
        } catch (Exception exception) {
            logger.error("Unable merge list by map key {} due to exception {}", key, Utils.getStackTrace(exception));
        }
        return map;
    }

    // Get Distinct Table
	public static List<String> getDistinctEntity(List<String> distinctEntity, Map<String, List<Map>> filters, Map<String, List<String>> projection) {
		try {
			if (filters != null && !filters.isEmpty())
				distinctEntity = new ArrayList<>(filters.keySet());

			if (projection != null && !projection.isEmpty())
				distinctEntity.addAll(new ArrayList<>(projection.keySet()));

			if (distinctEntity.contains(InfraConstants.GEOGRAPHYL1_TABLE)) {
				distinctEntity.add(InfraConstants.GEOGRAPHYL3_TABLE);
				distinctEntity.add(InfraConstants.GEOGRAPHYL4_TABLE);
				distinctEntity.add(InfraConstants.GEOGRAPHYL2_TABLE);
			} else if (distinctEntity.contains(InfraConstants.GEOGRAPHYL2_TABLE)) {
				distinctEntity.add(InfraConstants.GEOGRAPHYL3_TABLE);
				distinctEntity.add(InfraConstants.GEOGRAPHYL4_TABLE);
			} else if (distinctEntity.contains(InfraConstants.GEOGRAPHYL3_TABLE)) {
				distinctEntity.add(InfraConstants.GEOGRAPHYL4_TABLE);
			}
			distinctEntity = distinctEntity.stream().distinct().collect(Collectors.toList());
		} catch (Exception exception) {
			logger.error("Unable to get distinct entity List {}", Utils.getStackTrace(exception));
		}
		return distinctEntity;
	}
	
	public static List<String> getDistinctEntityForGeography(List<String> distinctEntity, Map<String, List<Map>> filters, Map<String, List<String>> projection) {
		try {
			if (filters != null && !filters.isEmpty())
				distinctEntity = new ArrayList<>(filters.keySet());

			if (projection != null && !projection.isEmpty())
				distinctEntity.addAll(new ArrayList<>(projection.keySet()));

			distinctEntity = distinctEntity.stream().distinct().collect(Collectors.toList());
		} catch (Exception exception) {
			logger.error("Unable to get distinct entity List {}", Utils.getStackTrace(exception));
		}
		return distinctEntity;
	}

	public static List getMapFromTupleList(List<Tuple> tuples) {
		List<Map> resultList = new ArrayList<>();
		for (Tuple tuple : tuples) {
			Map map = new HashMap<>();
			int i = 0;
			for (TupleElement<?> tupleElement : tuple.getElements()) {
				map.put(tupleElement.getAlias(), tuple.get(i));
				i++;
			}
			resultList.add(map);
		}
		return resultList;
	}

    public static List<Predicate> getPredicatesForCriteriaBuilderForSiteAndCell(CriteriaBuilder criteriaBuilder, Root<NetworkElement> networkElement, List<NEType> neTypeList, List<String> neNameList,
            List<String> neFrequencyList, List<NEStatus> neStatusList, List<Vendor> vendorList, List<Technology> technologyList, List<Domain> domainList, Map<String, List<Integer>> geographyNames,
            List<String> neIdList) {
        List<Predicate> predicates = new ArrayList<>();
        List<Integer> geographies = new ArrayList<>();
        if (neTypeList != null && !neTypeList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_NETYPE_KEY)).in(neTypeList));
        }
        if (neFrequencyList != null && !neFrequencyList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_NEFREQUENCY_KEY)).in(neFrequencyList));
        }
        if (neStatusList != null && !neStatusList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_NESTATUS_KEY)).in(neStatusList));
        }
        if (neNameList != null && !neNameList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_NENAME_KEY)).in(neNameList));
        }
        if (vendorList != null && !vendorList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_VENDOR_KEY)).in(vendorList));
        }
        if (technologyList != null && !technologyList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_TECHNOLOGY_KEY)).in(technologyList));
        }
        if (domainList != null && !domainList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_DOMAIN_KEY)).in(domainList));
        }
        if (neIdList != null && !neIdList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_NEID_KEY)).in(neIdList));
        }

        if (geographyNames != null && !geographyNames.isEmpty()) {
            if (geographyNames.get(InfraConstants.GEOGRAPHYL4_TABLE) != null) {
                geographies = geographyNames.get(InfraConstants.GEOGRAPHYL4_TABLE);
                predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.ID)).in(geographies));
            }
            if (geographyNames.get(InfraConstants.GEOGRAPHYL3_TABLE) != null) {
                geographies = geographyNames.get(InfraConstants.GEOGRAPHYL3_TABLE);
                predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.ID)).in(geographies));
            }
            if (geographyNames.get(InfraConstants.GEOGRAPHYL2_TABLE) != null) {
                geographies = geographyNames.get(InfraConstants.GEOGRAPHYL2_TABLE);
                predicates.add(criteriaBuilder
                        .upper(networkElement.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.ID))
                        .in(geographies));
            }
            if (geographyNames.get(InfraConstants.GEOGRAPHYL1_TABLE) != null) {
                geographies = geographyNames.get(InfraConstants.GEOGRAPHYL1_TABLE);
                predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY)
                        .get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.ID)).in(geographies));
            }
        }
        return predicates;
    }

    public static Map<String, Double> getViewportFromPolygon(List<List<Double>> polygon) {
        Map<String, Double> viewportMap = new HashMap<>();
        Corner bounds = BoundaryUtils.getCornerOfBoundary(polygon);
        Double minlat = bounds.getMinLatitude();
        Double maxlat = bounds.getMaxLatitude();
        Double minlon = bounds.getMinLongitude();
        Double maxlon = bounds.getMaxLongitude();
        if (minlat != null && maxlat != null && minlon != null && maxlon != null) {
            viewportMap.put(InfraConstants.SOUTHWEST_LATITUDE_KEY, minlat);
            viewportMap.put(InfraConstants.NORTHEAST_LATITUDE_KEY, maxlat);
            viewportMap.put(InfraConstants.SOUTHWEST_LONGITUDE_KEY, minlon);
            viewportMap.put(InfraConstants.NORTHEAST_LONGITUDE_KEY, maxlon);
            return viewportMap;
        }
        return null;
    }

    public static boolean isLatLngPresentInPolygon(GISGeometry polygon, Double latitude, Double longitude) {
        try {
            if (polygon.contains(new GISPoint(new LatLng(latitude, longitude)))) {
                return true;
            }
        } catch (Exception exception) {
            logger.error("Unable to check lat long {} ,{} and exception is {}", latitude, longitude, Utils.getStackTrace(exception));
        }
        return false;
    }

    public static String genrateFMEMSId(NetworkElement networkElement) {
        String neType = networkElement.getNeType().name();
        Integer enbid = networkElement.getEnbid();
        logger.info("inside @insertENBInfoBasedOnNeType metod neType: {}, enbid :{}", neType, enbid);
        String generatedVal = null;
        switch (neType) {
            case MACRO_ENB :
                generatedVal = ForesightConstants.ENODE_B_PREFIX + Symbol.UNDERSCORE_STRING + enbid;
                break;
            case MACRO_VDU :
                generatedVal = ForesightConstants.ENODE_B_PREFIX + Symbol.UNDERSCORE_STRING + enbid + Symbol.UNDERSCORE_STRING + ForesightConstants.VDU + Symbol.UNDERSCORE_STRING
                        + networkElement.getCellNum();
                break;
            case MACRO :
                generatedVal = ForesightConstants.ENODE_B_PREFIX + Symbol.UNDERSCORE_STRING + enbid + Symbol.UNDERSCORE_STRING + ForesightConstants.RIU + Symbol.UNDERSCORE_STRING
                        + networkElement.getCellNum();
                break;
            case MACRO_CELL :
                generatedVal = ForesightConstants.ENODE_B_PREFIX + Symbol.UNDERSCORE_STRING + enbid + Symbol.UNDERSCORE_STRING + ForesightConstants.RRH + Symbol.UNDERSCORE_STRING
                        + networkElement.getCellNum();
                break;
            default :
                logger.error("NeType Does't matched with any cases");
                break;
        }
        return generatedVal;
    }
    

    public static void setProjectionList(Map<String, List<String>> projections, String tableName, String... columns) {
          List<String> projectionList = new ArrayList<>();
          for (String column : columns) {
              projectionList.add(column);
          }
          projections.put(tableName, projectionList);
      }
    @SuppressWarnings({"rawtypes"})
    public static <T> void setFilterMap(Map<String, List<Map>> filterMap, List<Map> neFilterList, String tableName,
            String columnName, T columnValue, String dataType, String operation) {
        neFilterList.add(CriteriaUtils.getFilterMapForInputParameters(columnName, operation, dataType, columnValue));
        filterMap.put(tableName, neFilterList);
    }

    public static List<Predicate> getPredicatesForCriteriaBuilderForNE(CriteriaBuilder criteriaBuilder, Root<NetworkElement> networkElement, List<NEType> neTypeList, List<String> neNameList,
            List<String> neFrequencyList, List<NEStatus> neStatusList, List<Vendor> vendorList, List<Technology> technologyList, List<Domain> domainList, Map<String, List<String>> geographyNames,
            List<String> neIdList, String geoLevelKey) {
        List<Predicate> predicates = new ArrayList<>();
        if (neTypeList != null && !neTypeList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_NETYPE_KEY)).in(neTypeList));
        }
        if (neFrequencyList != null && !neFrequencyList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_NEFREQUENCY_KEY)).in(neFrequencyList));
        }
        if (neStatusList != null && !neStatusList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_NESTATUS_KEY)).in(neStatusList));
        }
        if (neNameList != null && !neNameList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_NENAME_KEY)).in(neNameList));
        }
        if (vendorList != null && !vendorList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_VENDOR_KEY)).in(vendorList));
        }
        if (technologyList != null && !technologyList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_TECHNOLOGY_KEY)).in(technologyList));
        }
        if (domainList != null && !domainList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_DOMAIN_KEY)).in(domainList));
        }
        if (neIdList != null && !neIdList.isEmpty()) {
            predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_NEID_KEY)).in(neIdList));
        }

        if (geographyNames != null && !geographyNames.isEmpty()) {
        	getGeoPredicateByGeographyMapping(criteriaBuilder, networkElement, geographyNames, predicates, geoLevelKey);
        }
        return predicates;
    }
    
    public static void getGeoPredicateByGeographyMapping(
    		CriteriaBuilder criteriaBuilder,
    		Root<NetworkElement> networkElement,
    		Map<String, List<String>> geographyNames, List<Predicate> predicates, String geoLevelKey) {
    	List<String> geographies = new ArrayList<>();
    	if (geographyNames != null && !geographyNames.isEmpty()) {
    		if (geographyNames.get(InfraConstants.GEOGRAPHYL4_TABLE) != null) {
    			geographies = geographyNames.get(InfraConstants.GEOGRAPHYL4_TABLE);
    			predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.BOUNDARY_NAME)).in(geographies));
    		}
    		if (geographyNames.get(InfraConstants.GEOGRAPHYL3_TABLE) != null) {
    			geographies = geographyNames.get(InfraConstants.GEOGRAPHYL3_TABLE);
    			if(InfraConstants.GEOGRAPHYL4.equalsIgnoreCase(geoLevelKey)){
    				predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.BOUNDARY_NAME)).in(geographies));
    			}else if(InfraConstants.GEOGRAPHYL3.equalsIgnoreCase(geoLevelKey)){
    				predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.BOUNDARY_NAME)).in(geographies));
    			}
    		}
    		if (geographyNames.get(InfraConstants.GEOGRAPHYL2_TABLE) != null) {
    			geographies = geographyNames.get(InfraConstants.GEOGRAPHYL2_TABLE);
    			if(InfraConstants.GEOGRAPHYL4.equalsIgnoreCase(geoLevelKey)){
    				predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
    						.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.BOUNDARY_NAME)).in(geographies));
    			}else if(InfraConstants.GEOGRAPHYL3.equalsIgnoreCase(geoLevelKey)){
    				predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_GEOGRAPHYL3_KEY)
    						.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.BOUNDARY_NAME)) .in(geographies));
    			}else if(InfraConstants.GEOGRAPHYL2.equalsIgnoreCase(geoLevelKey)){
    				predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.BOUNDARY_NAME)).in(geographies));
    			}
    		}
    		if (geographyNames.get(InfraConstants.GEOGRAPHYL1_TABLE) != null) {
    			geographies = geographyNames.get(InfraConstants.GEOGRAPHYL1_TABLE);
    			if(InfraConstants.GEOGRAPHYL4.equalsIgnoreCase(geoLevelKey)){
    				predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY)
    						.get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.BOUNDARY_NAME)).in(geographies));
    			}else if(InfraConstants.GEOGRAPHYL3.equalsIgnoreCase(geoLevelKey)){
    				predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY)
    						.get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.BOUNDARY_NAME)).in(geographies));
    			}else if(InfraConstants.GEOGRAPHYL2.equalsIgnoreCase(geoLevelKey)){
    				predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_GEOGRAPHYL2_KEY)
    						.get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.BOUNDARY_NAME)).in(geographies));
    			}else if(InfraConstants.GEOGRAPHYL1.equalsIgnoreCase(geoLevelKey)){
    				predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.BOUNDARY_NAME)).in(geographies));
    			}

    		}
    	}
    }
    
}

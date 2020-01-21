package com.inn.foresight.core.infra.dao;

import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.infra.model.NEBandDetail;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.NEStatus;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.utils.enums.Technology;
import com.inn.foresight.core.infra.utils.enums.Vendor;
import com.inn.foresight.core.infra.wrapper.NeDataWrapper;
import com.inn.foresight.core.infra.wrapper.NetworkElementWrapper;
import com.inn.foresight.core.infra.wrapper.SiteDataWrapper;

public interface INEBandDetailDao extends IGenericDao<Integer, NEBandDetail> {

	List<NEBandDetail> getNEBandDetails(List<NEType> neTypeList, List<String> neNameList, List<String> neFrequencyList, List<NEStatus> neStatusList, List<Vendor> vendorList,
			List<Technology> technologyList, List<Domain> domainList, Map<String, Double> viewportMap, Map<String, List<String>> geographyNames);

	List<SiteDataWrapper> getNeBandDetailBySite(List<String> neId);

	List<NEBandDetail> checkForSitesIsOnAirOrNot(NEStatus status, String siteName, String band);

	String getMorphologyByCode(String btsName);

	List<NeDataWrapper> getNeData();

	void intializeNeMap();

	Map<String, NeDataWrapper> getNeMap();

	List<NetworkElementWrapper> getNetworkElementWrapperForNename(List<String> neName);

	List<String> getNefrequencyForNename(String neName);

	List<Tuple> getSitesDataForVisualisation(Map<String, Double> viewMap, Map<String, List<Map>> filters, Map<String, List<String>> projection, List<String> distinctEntity, Boolean isGroupBy,
			List<String> geographyList);

	List<Tuple> getSiteBandDetail(Map<String, Double> viewMap, Map<String, List<Map>> filters, Map<String, List<String>> projection, List<String> distinctEntity, Boolean isGroupBy,
			List<String> geographyList, Boolean distinctValue);

	NEBandDetail checkNetworkElementInNeBandDetail(int networkElementFk);

	List<Tuple> getSitesPlannedDataForVisualisation(Map<String, Double> viewPortMap, Map<String, List<Map>> filters, Map<String, List<String>> projection, List<String> distinctEntity,
			Boolean isGroupBy, List<String> geographyList);

	List<Object> executeQuery(String userQuery);

	void updateNeBandStatusByNetworkElement(Integer id, NEStatus status);

	List<Object []> getOnAirSiteDetailsWithOnAirDate(String neStatus, List<NEType> neType, String domain, String vendor, String previousDate
			,String currentDate,String geographyType,String geographyValue);

	Integer getOnAirSitesCountForDayMinusOne(List<NEType> neType, String domain, String vendor, String neStatus,
			String date);

	Map<String, Long> getSitesCountByNestatus(List<NEType> neType, String date);

	Map<String, Long> getSitesCountNestatusWiseAndGeographyWise(List<NEType> neType, String domain, String vendor,
			List<String> neStatus, String geographyType, String geographyValue);

	List<Tuple> getSitesOverviewDataForVisualisation(Map<String, List<Map>> filters,Map<String, List<String>> projection);

	List<Tuple> getNETaskDetailDataForVisualisation(Map<String, List<Map>> filters,Map<String, List<String>> projection, Boolean isGroupBy);
}

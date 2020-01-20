package com.inn.foresight.module.nv.dashboard.wifi.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.ws.rs.core.UriBuilder;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.http.HttpException;
import com.inn.commons.http.HttpGetRequest;
import com.inn.commons.http.HttpPostRequest;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.dao.IFloorDataDao;
import com.inn.foresight.core.infra.model.Floor;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.foresight.core.infra.wrapper.WifiWrapper;
import com.inn.foresight.module.nv.dashboard.wifi.dao.IWIFIAPDetailHbaseDao;
import com.inn.foresight.module.nv.dashboard.wifi.service.IWIFIAPDetailService;
import com.inn.foresight.module.nv.dashboard.wifi.utils.constants.WIFIPerformanceConstants;
import com.inn.foresight.module.nv.dashboard.wifi.wrapper.APDetailWrapper;
import com.inn.foresight.module.nv.dashboard.wifi.wrapper.WIFIAPDashboardWrapper;
import com.inn.foresight.module.nv.service.ISiteDetailService;

@Service("WIFIAPDetailServiceImpl")
public class WIFIAPDetailServiceImpl implements IWIFIAPDetailService {

	private Logger logger = LogManager.getLogger(WIFIAPDetailServiceImpl.class);

	@Autowired
	ISiteDetailService siteDetailService;

	@Autowired
	IFloorDataDao iFloorPlanDao;

	@Autowired
	IWIFIAPDetailHbaseDao iWifiAPDao;

	@Override
	public List<APDetailWrapper> getFloorDataForBuilding(Integer buildingId) {
		List<APDetailWrapper> response = new ArrayList<>();
		try {
			List<Floor> floorDetail = iFloorPlanDao.getFloorDetailByBuilding(buildingId);
			logger.info("Response of floor response size {}", floorDetail.size());
			if (CollectionUtils.isNotEmpty(floorDetail)) {
				List<Integer> floorIds = floorDetail.stream().map(Floor::getId).collect(Collectors.toList());
				List<NetworkElement> neDetail = siteDetailService.getWifiAPDetailByFloorId(floorIds);
				logger.info("Response of networkElement Reponse size {}", neDetail.size());
				Map<Floor, Long> floorWiseCount = neDetail.stream()
						.collect(Collectors.groupingBy(NetworkElement::getFloor, Collectors.counting()));
				for (Entry<Floor, Long> floorData : floorWiseCount.entrySet()) {
					calculateResponse(response, floorData);
				}
			}

		} catch (Exception e) {
			logger.error(ForesightConstants.LOG_EXCEPTION, Utils.getStackTrace(e));
		}
		return response;
	}

	private void calculateResponse(List<APDetailWrapper> response, Entry<Floor, Long> floorWiseEntry) {
		APDetailWrapper floor = new APDetailWrapper();
		floor.setFloorNumber(floorWiseEntry.getKey().getFloorName());
		floor.setFloorId(floorWiseEntry.getKey().getId());
		floor.setWingName(floorWiseEntry.getKey().getWing().getWingName());
		floor.setApCount(floorWiseEntry.getValue());
		response.add(floor);
	}

	@Override
	public String getFloorPerformance(Integer floorId, String startDate) {

		try {
			String url = getMicroserviceUrl() + WIFIPerformanceConstants.FLOOR_PERFORMANCE_MICROSERVICE_REST_URL
					+ ForesightConstants.QUESTIONMARK + WIFIPerformanceConstants.FLOORID + ForesightConstants.EQUALS
					+ floorId + ForesightConstants.AMPERSAND + WIFIPerformanceConstants.STARTDATE
					+ ForesightConstants.EQUALS + startDate;
			return new HttpGetRequest(url).getString();
		} catch (HttpException e) {
			logger.error(ForesightConstants.LOG_EXCEPTION + Utils.getStackTrace(e));
			return ForesightConstants.ERROR;

		}

	}

	public static String getMicroserviceUrl() {
		return ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue());
	}

	public List<WIFIAPDashboardWrapper> getFloorWiseAPDataFromHbase(Integer floorId, String date,
			Map<String, String> macAddressMap) {
		List<WIFIAPDashboardWrapper> wifiAPwrapperList = new ArrayList<>();
		for (Entry<String, String> entry : macAddressMap.entrySet()) {
			WIFIAPDashboardWrapper wrapper = iWifiAPDao.getPerformanceDataFromHbase(floorId, entry.getValue(), date,
					null, true);
			wrapper.setApName(entry.getKey());
			wifiAPwrapperList.add(wrapper);
		}
		return wifiAPwrapperList;

	}

	@Override
	public Object getFloorStats(Integer floorId, String startDate) {
		Map<String, Object> response = new HashMap<>();
		Map<String, String> buildingDetail = new HashMap<>();
		Map<String, String> macAddressMap = new HashMap();
		List<Integer> floorIds = new ArrayList<>();
		floorIds.add(floorId);
		List<NetworkElement> networkElementList = siteDetailService.getWifiAPDetailByFloorId(floorIds);
		logger.info("Response of the networkElement {}", networkElementList.size());
		if (!CollectionUtils.isEmpty(networkElementList)) {
			for (NetworkElement neElement : networkElementList) {
				String macaddress = neElement.getMacaddress();
				if (macaddress != null) {
					macAddressMap.put(neElement.getNeId(), macaddress);
				}
			}
			setFloorDetail(buildingDetail, networkElementList);
			String floorWiseAP = getFloorWiseAPDataFromMicroService(floorId, startDate, macAddressMap);
			response.put("AP_RECORDS_DETAIL",
					floorWiseAP != null && !floorWiseAP.equalsIgnoreCase("null") ? floorWiseAP : "[]");
			response.put("BUILDING_DETAIL", buildingDetail);
			logger.info("Final Response Size {}", response.size());
		}
		return response;
	}

	public String getFloorWiseAPDataFromMicroService(Integer floorId, String date, Map<String, String> macAddressMap) {
		try {
			String url = getMicroserviceUrl() + WIFIPerformanceConstants.FLOOR_STATS_MICROSERVICE_REST_URL
					+ ForesightConstants.QUESTIONMARK + WIFIPerformanceConstants.FLOORID + ForesightConstants.EQUALS
					+ floorId + ForesightConstants.AMPERSAND + WIFIPerformanceConstants.STARTDATE
					+ ForesightConstants.EQUALS + date;
			logger.info("Micro Service URL {}", url);
			StringEntity stringEntity = new StringEntity(new Gson().toJson(macAddressMap),
					ContentType.APPLICATION_JSON);
			return new HttpPostRequest(url, stringEntity).getString();
		} catch (HttpException e) {
			logger.error(ForesightConstants.LOG_EXCEPTION + Utils.getStackTrace(e));
		}
		return null;
	}

	private void setFloorDetail(Map<String, String> buildingDetail, List<NetworkElement> networkElement) {
		buildingDetail.put("name",
				networkElement.get(ForesightConstants.ZERO).getFloor().getWing().getBuilding().getBuildingName());
		buildingDetail.put("address",
				networkElement.get(ForesightConstants.ZERO).getFloor().getWing().getBuilding().getAddress());
		buildingDetail.put("type", networkElement.get(ForesightConstants.ZERO).getFloor().getWing().getBuilding()
				.getBuildingType().toString());
		buildingDetail.put("longitude", networkElement.get(ForesightConstants.ZERO).getFloor().getWing().getBuilding()
				.getLongitude().toString());
		buildingDetail.put("latitude", networkElement.get(ForesightConstants.ZERO).getFloor().getWing().getBuilding()
				.getLatitude().toString());
		buildingDetail.put("wing", networkElement.get(ForesightConstants.ZERO).getFloor().getWing().getWingName());
		buildingDetail.put("floor", networkElement.get(ForesightConstants.ZERO).getFloor().getFloorName());
	}

	@Override
	public WIFIAPDashboardWrapper getFloorPerformanceDataFromHbase(Integer floorId, String startDate) {
		return iWifiAPDao.getPerformanceDataFromHbase(floorId, null, startDate, null, false);

	}

	@Override
	public String getAPPerformance(Integer floorId, String macAddress, String startDate, String hour) {

		try {
			String url = getMicroserviceUrl() + WIFIPerformanceConstants.AP_PERFORMANCE_MICROSERVICE_REST_URL;

			UriBuilder uri = UriBuilder.fromPath(url);

			uri.queryParam(WIFIPerformanceConstants.FLOORID, floorId);
			uri.queryParam(WIFIPerformanceConstants.MACADDRESS, macAddress);
			uri.queryParam(WIFIPerformanceConstants.STARTDATE, startDate);

			if (hour != null && !hour.isEmpty()) {
				uri.queryParam(WIFIPerformanceConstants.HOUR, hour);
			}

			logger.info("Micro Service URL {}", uri.build().toString());
			return new HttpGetRequest(uri.build().toString()).getString();
		} catch (HttpException e) {
			logger.error(ForesightConstants.LOG_EXCEPTION + Utils.getStackTrace(e));

		}
		return null;

	}

	@Override
	public WIFIAPDashboardWrapper getAPPerformanceFromHbase(Integer floorId, String macAddress, String startDate,
			String hour) {
		if (Utils.hasValue(floorId) && Utils.hasValue(macAddress) && Utils.hasValue(startDate)) {
			return iWifiAPDao.getPerformanceDataFromHbase(floorId, macAddress, startDate, hour, false);
		}
		return null;
	}

	@Override
	public APDetailWrapper getAPDetail(String macAddress) {

		APDetailWrapper apDetailWrapper = new APDetailWrapper();
		apDetailWrapper.setMacAddress(macAddress);
		setAPDetailWrapper(apDetailWrapper,siteDetailService.getWIFIAPDetail(macAddress));
		return apDetailWrapper;
	}

	private void setAPDetailWrapper(APDetailWrapper ap,WifiWrapper wifiapDetail) {
		if(wifiapDetail!=null) {
		ap.setApName(wifiapDetail.getNeId());
		ap.setChannel(Integer.valueOf(wifiapDetail.getChannel()));
		ap.setLatitude(wifiapDetail.getLatitude());
		ap.setLongitude(wifiapDetail.getLongitude());
		ap.setApStatus(wifiapDetail.getStatus());
		ap.setIpAddress(wifiapDetail.getIp());
		}
	}
}

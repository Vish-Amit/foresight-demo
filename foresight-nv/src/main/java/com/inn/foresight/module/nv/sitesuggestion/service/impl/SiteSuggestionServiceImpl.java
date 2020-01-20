package com.inn.foresight.module.nv.sitesuggestion.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.grid.DegreeGrid;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.feedback.constants.ConsumerFeedbackConstant;
import com.inn.foresight.module.nv.feedback.model.ConsumerFeedback;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.dao.INVLayer3HDFSDao;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.sitesuggestion.dao.ISiteSuggestionDao;
import com.inn.foresight.module.nv.sitesuggestion.model.FriendlySiteSuggestion;
import com.inn.foresight.module.nv.sitesuggestion.service.ISiteSuggestionService;
import com.inn.foresight.module.nv.sitesuggestion.wrapper.FriendlySiteSuggestionWrapper;
import com.inn.product.systemconfiguration.dao.SystemConfigurationDao;
import com.inn.product.systemconfiguration.utils.SystemConfigurationUtils;
import com.inn.product.um.user.dao.UserDao;
import com.inn.product.um.user.model.User;

@Service("SiteSuggestionServiceImpl")
public class SiteSuggestionServiceImpl implements ISiteSuggestionService {

	private Logger logger = LogManager.getLogger(SiteSuggestionServiceImpl.class);

	@Autowired(required = false)
	private INVLayer3HDFSDao nvLayerHDFSDao;

	@Autowired
	private ISiteSuggestionDao siteSuggestionDao;

	@Autowired(required = false)
	private UserDao userDao;

	@Autowired
	private SystemConfigurationDao iSystemConfigurationDao;

	@Override
	@Transactional
	public FriendlySiteSuggestion persistFriendlySiteSuggestion(String json, InputStream inputFile, String fileName) throws IOException{
		logger.info("Going to  persist FriendlySiteSuggestion for json : {} ", json);

		FriendlySiteSuggestion friendlySiteSuggestion = null;
		friendlySiteSuggestion = getSiteSuggestionWrapper(json);

		if (friendlySiteSuggestion.getDeviceId() != null 
				&& (friendlySiteSuggestion.getLatitude() != null 
				&& friendlySiteSuggestion.getLongitude() != null)) {

			friendlySiteSuggestion = siteSuggestionDao.create(friendlySiteSuggestion);
			logger.info("Going to Persist friendlySiteSuggestion");

			if (inputFile != null && inputFile.read() != -1) {
				String hdfsFileName = persistFileToHDFS(inputFile, fileName, friendlySiteSuggestion);
				friendlySiteSuggestion.setFilepath(hdfsFileName);
				siteSuggestionDao.update(friendlySiteSuggestion);
			}
			logger.info("Done persistNVDeviceData");

		} else {
			logger.error("FriendlySiteSuggestion data not persist for deviceId:  {} ,latituded : {} longitude: {}",
					friendlySiteSuggestion.getDeviceId(), friendlySiteSuggestion.getLatitude(),friendlySiteSuggestion.getLongitude());
		}
		return friendlySiteSuggestion;
	}


	private FriendlySiteSuggestion getSiteSuggestionWrapper(String json) {
		if(json != null) {
			FriendlySiteSuggestion wrapper = new FriendlySiteSuggestion();
			JSONObject map = new JSONObject(json);

			logger.info("map: {}", map);
			if(map.has("userId") && map.get("userId") != null){
				User user = userDao.findByPk((Integer)map.get("userId"));
				logger.info("user object: {}", user.toString());
				wrapper.setUserId(user);
			}
			wrapper.setDeviceId(map.has("deviceId") && map.get("deviceId") != null ? map.get("deviceId").toString(): null);
			wrapper.setLatitude(map.has("latitude") && map.get("latitude") != null ? Double.valueOf(map.get("latitude").toString()): null);
			wrapper.setLongitude(map.has("longitude") && map.get("longitude") != null ? Double.valueOf(map.get("longitude").toString()): null);
			wrapper.setBuildingName(map.has("buildingName") && map.get("buildingName") != null ? map.get("buildingName").toString(): null);
			wrapper.setSiteType(map.has("siteType") && map.get("siteType") != null ? map.get("siteType").toString(): null);
			wrapper.setBuildingType(map.has("buildingType") && map.get("buildingType") != null ? map.get("buildingType").toString(): null);
			wrapper.setAddress(map.has("address") && map.get("address") != null ? map.get("address").toString(): null);
			wrapper.setContactPersonName(map.has("contactPersonName") && map.get("contactPersonName") != null ? map.get("contactPersonName").toString(): null);
			wrapper.setContactPersonNumber(map.has("contactPersonNumber") && map.get("contactPersonNumber") != null ? map.get("contactPersonNumber").toString(): null);
			wrapper.setRefferalName(map.has("refferalName") && map.get("refferalName") != null ? map.get("refferalName").toString(): null);
			wrapper.setRefferalContactNumber(map.has("refferalContactNumber") && map.get("refferalContactNumber") != null ? map.get("refferalContactNumber").toString(): null);
			wrapper.setRefferalEmailId(map.has("refferalEmailId") && map.get("refferalEmailId") != null ? map.get("refferalEmailId").toString(): null);
			wrapper.setAppVersion(map.has("appVersion") && map.get("appVersion") != null ? map.get("appVersion").toString(): null);
			wrapper.setTimestamp(map.has("timestamp") && map.get("timestamp") != null ? new Date((Long)map.get("timestamp")): null);
			wrapper.setMcc(map.has("mcc") && map.get("mcc") != null ? (Integer)map.get("mcc"): null);
			wrapper.setMnc(map.has("mnc") && map.get("mnc") != null ? (Integer)map.get("mnc"): null);
			wrapper.setTac(map.has("tac") && map.get("tac") != null ? (Integer)map.get("tac"): null);
			wrapper.setLac(map.has("lac") && map.get("lac") != null ? (Integer)map.get("lac"): null);
			wrapper.setPci(map.has("pci") && map.get("pci") != null ? (Integer)map.get("pci"): null);
			wrapper.setPsc(map.has("psc") && map.get("psc") != null ? (Integer)map.get("psc"): null);
			wrapper.setNetworkType(map.has("networkType") && map.get("networkType") != null ? map.get("networkType").toString(): null);
			wrapper.setOperator(map.has("operator") && map.get("operator") != null ? map.get("operator").toString(): null);
			wrapper.setAutoTimeEnable(map.has("autoTimeEnable") && map.get("autoTimeEnable") != null ? (Boolean)map.get("autoTimeEnable"): null);
			wrapper.setRemark(map.has("remark") && map.get("remark") != null ? map.get("remark").toString(): null);


			return wrapper;
		}
		return null;

	}


	@Override
	public String persistFileToHDFS(InputStream fileStream, String fileName, FriendlySiteSuggestion friendlySiteSuggestion) throws IOException {

		logger.info("Inside persistFileToHDFS");
		String hdfsFilePath = ConfigUtils.getString(NVConstant.NV_SITE_SUGGESTION_HDFS_BASE_FILE_PATH) 
				+ friendlySiteSuggestion.getDeviceId() + Symbol.SLASH_FORWARD_STRING 
				+ friendlySiteSuggestion.getId() + NVLayer3Constants.ZIP_FILE_EXTENTION;

		NVLayer3Utils.validateFilePath(hdfsFilePath);

		if (!nvLayerHDFSDao.isFileExist(hdfsFilePath)) {
			logger.info("File Does'nt Exist in HDFS. Going to insert");

			try {
				nvLayerHDFSDao.persistWORecipeQMDLFileToHDFS(fileStream, hdfsFilePath);
			} catch (Exception e) {
				nvLayerHDFSDao.deleteFileFromHDFS(hdfsFilePath);
				throw new RestException();
			}
			nvLayerHDFSDao.persistWORecipeQMDLFileToHDFS(fileStream, hdfsFilePath);
		}
		logger.info("Done persistFileToHDFS");
		return hdfsFilePath;
	}

	@Override
	@Transactional
	public String getSiteSuggestionData(int decryZoom, double decodeNELat, double decodeNELng, double decodeSWLat,
			double decodeSWLng) {
		Double countryLat = getCountryLat();
		Double countryLng = getCountryLng();
		Integer gridSize = getGridSize(decryZoom);
		List<FriendlySiteSuggestion> friendlySiteSuggestionDatalist = siteSuggestionDao.findAll();
		List<FriendlySiteSuggestion> filterRecordsByViewPort = filterRecordsByViewPort(decodeNELat, decodeNELng, decodeSWLat, decodeSWLng, friendlySiteSuggestionDatalist);
		Map<String, List<FriendlySiteSuggestionWrapper>> findGridWiseResponse = findGridWiseResponse(filterRecordsByViewPort, countryLat, countryLng, gridSize);
		if(! findGridWiseResponse.isEmpty()) {
			return new Gson().toJson(findGridWiseResponse);
		}
		return null;
	}

	private List<FriendlySiteSuggestion> filterRecordsByViewPort(Double nELat, Double nELng, Double sWLat, Double sWLng,
			List<FriendlySiteSuggestion> friendlySiteSuggestionDatalist) {
		return friendlySiteSuggestionDatalist.stream()
				.filter(x -> x.getLatitude() != null && x.getLongitude() != null
				&& isValidRecord(nELat, nELng, sWLat, sWLng, x.getLatitude(), x.getLongitude()))
				.collect(Collectors.toList());
	}


	private Map<String, List<FriendlySiteSuggestionWrapper>> findGridWiseResponse(List<FriendlySiteSuggestion> friendlySiteSuggestionDatalist,
			Double countryLat, Double countryLng, Integer gridSize) {
		List<FriendlySiteSuggestionWrapper> wrapperList = new ArrayList<>();
		DegreeGrid dg = new DegreeGrid(gridSize, new LatLng(countryLat, countryLng));
		for (FriendlySiteSuggestion friendlySiteSuggestion : friendlySiteSuggestionDatalist) {
			FriendlySiteSuggestionWrapper friendlySiteSuggestionWrapper = new FriendlySiteSuggestionWrapper();
			setModelDataIntoWrapper(dg, friendlySiteSuggestion, friendlySiteSuggestionWrapper);
			wrapperList.add(friendlySiteSuggestionWrapper);
		}
		Map<String, List<FriendlySiteSuggestionWrapper>> collect = wrapperList.stream().filter(f->f.getGridId() != null).collect(Collectors.groupingBy(FriendlySiteSuggestionWrapper :: getGridId));
		return collect;
	}


	public void setModelDataIntoWrapper(DegreeGrid dg, FriendlySiteSuggestion friendlySiteSuggestion,
			FriendlySiteSuggestionWrapper friendlySiteSuggestionWrapper) {
		Double latitude = friendlySiteSuggestion.getLatitude();
		Double longitude = friendlySiteSuggestion.getLongitude();
		LatLng grid  = dg.getCornerForTopLeftGrid(dg.getGrid(new LatLng(latitude, longitude))).getCentroid();
		String gridId = grid.getLatitude() + ForesightConstants.COMMA + grid.getLongitude();
		friendlySiteSuggestionWrapper.setGridId(gridId);
		if(friendlySiteSuggestion.getDeviceId() != null) {
			friendlySiteSuggestionWrapper.setDeviceId(friendlySiteSuggestion.getDeviceId());
		}
		if(friendlySiteSuggestion.getBuildingName() != null) {
			friendlySiteSuggestionWrapper.setBuildingName(friendlySiteSuggestion.getBuildingName());
		}
		if(friendlySiteSuggestion.getBuildingType() != null) {
			friendlySiteSuggestionWrapper.setBuildingType(friendlySiteSuggestion.getBuildingType());
		}
		if(friendlySiteSuggestion.getSiteType() != null) {
			friendlySiteSuggestionWrapper.setSiteType(friendlySiteSuggestion.getSiteType() );
		}
		if(friendlySiteSuggestion.getAddress() != null) {
			friendlySiteSuggestionWrapper.setAddress(friendlySiteSuggestion.getAddress());
		}
		if(friendlySiteSuggestion.getContactPersonName() != null) {
			friendlySiteSuggestionWrapper.setContactPersonName(friendlySiteSuggestion.getContactPersonName());
		}
		if(friendlySiteSuggestion.getContactPersonNumber() != null) {
			friendlySiteSuggestionWrapper.setContactPersonNumber(friendlySiteSuggestion.getContactPersonNumber());
		}
		if(friendlySiteSuggestion.getModificationTime()!=null) {
			friendlySiteSuggestionWrapper.setModificationTime(friendlySiteSuggestion.getModificationTime());
		}
		
		if(friendlySiteSuggestion.getRefferalName()!=null) {
			friendlySiteSuggestionWrapper.setRefferalName(friendlySiteSuggestion.getRefferalName());
		}		

		if(friendlySiteSuggestion.getRefferalContactNumber()!=null) {
			friendlySiteSuggestionWrapper.setRefferalContactNumber(friendlySiteSuggestion.getRefferalContactNumber());
		}

		if(friendlySiteSuggestion.getRefferalEmailId()!=null) {
			friendlySiteSuggestionWrapper.setRefferalEmailId(friendlySiteSuggestion.getRefferalEmailId());
		}		
		
		friendlySiteSuggestionWrapper.setLatitude(latitude);
		friendlySiteSuggestionWrapper.setLongitude(longitude);
	}

	private Double getCountryLat() {
		try {
			return Double.parseDouble(SystemConfigurationUtils.systemConfMap.get("CountryLatitude"));
		} catch (Exception e) {
			return Double.parseDouble(iSystemConfigurationDao.getValueByName("CountryLatitude"));
		}
	}

	private Double getCountryLng() {
		try {
			return Double.parseDouble(SystemConfigurationUtils.systemConfMap.get("CountryLongitude"));
		} catch (Exception e) {
			return Double.parseDouble(iSystemConfigurationDao.getValueByName("CountryLongitude"));
		}
	}

	private Integer getGridSize(Integer zoom) {
		try {
			return Integer.parseInt(SystemConfigurationUtils.systemConfMap.get("ZOOM"+zoom));
		} catch (Exception e) {
			return Integer.parseInt(iSystemConfigurationDao.getValueByName("ZOOM"+zoom));
		}
	}

	private boolean isValidRecord(Double nELat, Double nELng, Double sWLat, Double sWLng, Double latitude,
			Double longitude) {
		Boolean isDataAddIntoList = true;
		if (latitude != null) {

			if (latitude > nELat || latitude < sWLat) {
				isDataAddIntoList = false;
			}
		}
		if (longitude != null) {
			if (longitude > nELng || longitude < sWLng) {
				isDataAddIntoList = false;
			}
		}
		return isDataAddIntoList;
	}


	@Override
	public Object getSiteAcquisitionLayerData(Integer zoom, Double nELat, Double nELng, Double sWLat, Double sWLng,
			String fromDate, String toDate, String buildingType, String siteType,Integer displayZoomLevel) {
		List<Map<String, Object>> response = new ArrayList<>();
		List<FriendlySiteSuggestion> siteAcquisitionRawList = siteSuggestionDao.getSiteAcquisitionLayerData(fromDate,
				toDate, buildingType, siteType);

		List<FriendlySiteSuggestion> filterFriendlySiteSuggestion = filterRecordByViewPort(nELat, nELng, sWLat, sWLng,
				siteAcquisitionRawList);
		logger.info("Size after view port filter {}", filterFriendlySiteSuggestion.size());
		Double countryLat = getCountryLat();
		Double countryLng = getCountryLng();
		Integer gridSize = getGridSize(zoom);
		logger.info("Found the country Lat {} , Country Lng {} and gridSize {}", countryLat, countryLng, gridSize);
		Map<String, List<FriendlySiteSuggestionWrapper>> map = findGridWiseResponse(filterFriendlySiteSuggestion,
				countryLat, countryLng, gridSize);
		populateResult(zoom, response, map,displayZoomLevel);
		logger.info("Final Response Size {}", response.size());
		return response;
	}

	private List<FriendlySiteSuggestion> filterRecordByViewPort(Double nELat, Double nELng, Double sWLat, Double sWLng,
			List<FriendlySiteSuggestion> filterFriendlySitekRawList) {
		return filterFriendlySitekRawList.stream()
				.filter(x -> x.getLatitude() != null && x.getLongitude() != null
				&& isValidRecord(nELat, nELng, sWLat, sWLng, x.getLatitude(), x.getLongitude()))
				.collect(Collectors.toList());
	}


	private void populateResult(Integer zoom, List<Map<String, Object>> response,
			Map<String, List<FriendlySiteSuggestionWrapper>> map, Integer displayZoomLevel) {
		for(Entry<String, List<FriendlySiteSuggestionWrapper>> entry:map.entrySet())
		{
			Map<String,Object> gridResponse = new HashMap<>();
			String[] centroid = entry.getKey().split(",");
			int size = entry.getValue().stream().filter(x-> Utils.hasValue(x.getDeviceId())).
					map(FriendlySiteSuggestionWrapper::getDeviceId).collect(Collectors.toList()).size();
			gridResponse.put(ConsumerFeedbackConstant.COUNT, size);
			gridResponse.put(ConsumerFeedbackConstant.LATITUDE_KEY, centroid[0]);
			gridResponse.put(ConsumerFeedbackConstant.LONGITUDE_KEY, centroid[1]);
			if(zoom >= displayZoomLevel)
			{
				Map<String, List<FriendlySiteSuggestionWrapper>> deviceIdWiseMap = entry.getValue().stream().filter(x-> Utils.hasValue(x.getDeviceId())).collect(Collectors.groupingBy(FriendlySiteSuggestionWrapper :: getDeviceId,
						Collectors.toList()));
				List<FriendlySiteSuggestionWrapper> data = new ArrayList<>();
				for(Entry<String, List<FriendlySiteSuggestionWrapper>> deviceData:deviceIdWiseMap.entrySet())
				{
					data.add(findLatestRecord(deviceData.getValue()));
				}
				gridResponse.put(ConsumerFeedbackConstant.DATA, data);
			}
			response.add(gridResponse);
		}
	}



	private FriendlySiteSuggestionWrapper findLatestRecord(List<FriendlySiteSuggestionWrapper> value) {
		Map<String, FriendlySiteSuggestionWrapper> response = new HashMap<>();
		for (FriendlySiteSuggestionWrapper siteSuggestion : value) {
			if (!response.isEmpty()) {
				Long oldTime = response.get("key").getModificationTime().getTime();
				Long newTime = siteSuggestion.getModificationTime().getTime();
				if (newTime > oldTime) {
					response.put("key", siteSuggestion);
				}
			} else {
				response.put("key", siteSuggestion);
			}
		}
		return response.get("key");
	}

}

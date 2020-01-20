package com.inn.foresight.module.nv.device.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.NoResultException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.commons.http.HttpGetRequest;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.device.constant.DeviceConstant;
import com.inn.foresight.module.nv.device.dao.IDeviceDao;
import com.inn.foresight.module.nv.device.model.Device;
import com.inn.foresight.module.nv.device.service.IDeviceService;

/** The Class DeviceServiceImpl. */
@Service("DeviceServiceImpl")
public class DeviceServiceImpl extends AbstractService<Integer, Device> implements IDeviceService {
	/** The logger. */
	private static Logger logger = LogManager.getLogger(DeviceServiceImpl.class);
	
	/** The i device dao. */
	@Autowired
	IDeviceDao iDeviceDao;
	
	 /**
     * Gets the device specification by model.
     *
     * @param model the model
     * @return the device specification by model
     */
	@Override
	public Device getDeviceSpecificationByModel(String model) {
		Device device=null;
		try {
			 device=iDeviceDao.getDeviceSpecificationByModel(model);
		} catch (NoResultException | EmptyResultDataAccessException e) {
			logger.error("NoResultException in getDeviceSpecificationByModel : {}", ExceptionUtils.getStackTrace(e));
		}catch (DaoException e) {
			logger.error("DaoException in getDeviceSpecificationByModel : {}", ExceptionUtils.getStackTrace(e));
		}
		return device;
	}

	  /**
     * Gets the map of model and device.
     *
     * @return the map of model and device
     */
	@Override
	public Map<String, Device> getMapOfModelAndDevice() {
		return iDeviceDao.getMapOfModelAndDevice();
	}

	
	/**
	 * Creating device data 
	 */
	private static String getCacheDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		return "" + cal.get(Calendar.MONTH) + ""
				+ Math.round((cal.get(Calendar.DATE) * ForesightConstants.TWENTY_FOUR + cal.get(Calendar.HOUR)) / (float) ForesightConstants.SIX);
	}

	private String getFormattedString(String inputString) {
		String[] tokens = StringUtils.split(inputString, ForesightConstants.HIPHEN);
		return StringUtils.join(tokens, ForesightConstants.UNDERSCORE).replace(ForesightConstants.SPACE,
				ForesightConstants.UNDERSCORE);
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, String> getBrandNameMap(JSONObject jsonObject) {
		logger.info("Inside getBrandNameMap method");
		Map<String, String> brandNames = new HashMap<>();
		Iterator<String> itr = jsonObject.keys();
		while (itr.hasNext()) {
			try {
				String brandId = itr.next();
				brandNames.put(brandId, jsonObject.getString(brandId));
			} catch (Exception e) {
				logger.error("Error in getting brand list : {}",Utils.getStackTrace(e));
			}
		}
		logger.info("Total no of brands found : {}", brandNames.size());
		return brandNames;
	}
	
	public JSONObject getAndParseDeviceData(String brand, String model, String deviceId) {
		JSONObject deviceJson = new JSONObject();
		try {
			String url = DeviceConstant.GSM_ARENA_URL + brand + model + ForesightConstants.HIPHEN + deviceId
					+ DeviceConstant.PHP_EXTENSION;
			// php url for getting device all information in the form of php pages
			url = url.trim().replace(" ", "");
			String deviceDetails = new HttpGetRequest(url).getString();
			if (!(deviceDetails.equalsIgnoreCase(ForesightConstants.EXCEPTION_NO_RECORD_FOUND)
					|| deviceDetails.equalsIgnoreCase(ForesightConstants.EXCEPTION_ON_CONNECTION))) {

				Document document = Jsoup.parse(deviceDetails);
				Elements rows = document.select(DeviceConstant.TABLE_ROW);
				for (int j = ForesightConstants.ZERO; j < rows.size(); j++) {
					Element row = rows.get(j);
					Elements col = row.select(DeviceConstant.TABLE_TD_TTL);
					// key --------- key like bluetooth, WLAN, Camera
					String key = col.text();
					Elements col2 = row.select(DeviceConstant.TABLE_TD_NFO);
					// value -- value like 2.1 - bluetooth, WLAN - Wi-Fi 802.11 b/g, hotspot
					String value = col2.text();
					if (key != null && !key.equals(ForesightConstants.BLANK_STRING)) {
						deviceJson.put(key, value);
					}
				}
				fetchImageUrl(deviceJson, document);
			}

		} catch (Exception e) {
			logger.error("Exception in parsing device with brand : {} model name : {}", brand, model);
		}
		return deviceJson;
	}

	private void fetchImageUrl(JSONObject obj, Document doc) {
		Elements imageDiv = doc.select("div[class=specs-photo-main]");
		for (int j = ForesightConstants.ZERO; j < imageDiv.size(); j++) {
			Element row = imageDiv.get(j);
			Elements imageTag = row.select("img");
			obj.put("imageUrl", imageTag.attr("src"));
		}
	}
	
	private void cleanBands(JSONObject device) {
		StringBuilder identifier = new StringBuilder(ForesightConstants.BLANK_STRING);
		Pattern pattern = Pattern.compile(DeviceConstant.BANDS_PATTERN);
		
		updateDeviceDetails(device, identifier, pattern, DeviceConstant.BANDS_2G);
		updateDeviceDetails(device, identifier, pattern, DeviceConstant.BANDS_3G);
		updateDeviceDetails(device, identifier, pattern, DeviceConstant.BANDS_4G);

	}

	private void updateDeviceDetails(JSONObject device, StringBuilder identifier, Pattern pattern, String bands) {
		Matcher matcher;
		if (device.has(bands)) {
			String inputband = device.getString(bands);
			matcher = pattern.matcher(inputband);
			while (matcher.find()) {
				if (!identifier.toString().equals(ForesightConstants.BLANK_STRING))
					identifier.append(ForesightConstants.COMMA);
				identifier.append(matcher.group(ForesightConstants.ZERO));

			}
			if (!identifier.toString().isEmpty()) {
				device.put(bands, identifier);
			} else {
				identifier = null;
				device.put(bands, identifier);
			}
		}
	}

	private void updateModelnameInDeviceList(List<JSONObject> synchronizedList) {
		try {
			logger.info("Updating model name in device list");
			String masterDeviceNames = new HttpGetRequest(
					"https://raw.githubusercontent.com/jaredrummler/AndroidDeviceNames/master/json/devices.json").getString();
			JSONArray masterDeviceArray = new JSONArray(masterDeviceNames);
			for (JSONObject oldJsonObject : synchronizedList) {
				String deviceModel = oldJsonObject.getString(DeviceConstant.MODEL_CAMELCASE);
				StringBuilder  modelCode = new StringBuilder(deviceModel);
				
				for (int i = ForesightConstants.ZERO; i < masterDeviceArray.length(); i++) {
					JSONObject newJsonsObject = masterDeviceArray.getJSONObject(i);
					if (deviceModel.equalsIgnoreCase(newJsonsObject.getString(DeviceConstant.MARKET_NAME))) {
						modelCode.append(ForesightConstants.COMMA + newJsonsObject.getString(DeviceConstant.MODEL));
					}
				}
				oldJsonObject.put(DeviceConstant.MODEL_CODE,modelCode);
			}
			logger.info("Device list updated with modelcode");
		} catch (Exception e) {
			logger.error("error in updating model name from raw.githubusercontent.com : {}", Utils.getStackTrace(e));
		}
		logger.info("Device list not updated with modelcode");
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public void createAndUpdateDeviceData() {
		logger.info("Going to create and update device data ");
		String url = DeviceConstant.GSM_ARENA_URL+ DeviceConstant.DEVICE_QUICK_SEARCH + ForesightConstants.HIPHEN + getCacheDate() + ForesightConstants.JPG_EXTENSION;
		logger.info("Device URL : {}", url);
		try {
			String result = new HttpGetRequest(url).getString();
			if (result != null && !result.isEmpty()
					&& !(result.equalsIgnoreCase(ForesightConstants.EXCEPTION_NO_RECORD_FOUND)
							|| result.equalsIgnoreCase(ForesightConstants.EXCEPTION_ON_CONNECTION))) {

				JSONArray deviceArray = new JSONArray(result);
				JSONObject brandJson = (JSONObject) deviceArray.get(ForesightConstants.ZERO); // brand name json list
				final Map<String, String> brandNames = getBrandNameMap(brandJson);
				final JSONArray modelnamesJson = (JSONArray) deviceArray.get(ForesightConstants.ONE); // model name json list
				final List<JSONObject> synchronizedDeviceList = Collections.synchronizedList(new ArrayList<>());
				List<Future> futures = new ArrayList<>();
				ExecutorService executors = Executors.newFixedThreadPool(ForesightConstants.HUNDRED); // fast execution
				updateDeviceUsingJsonArray(brandNames, modelnamesJson, synchronizedDeviceList, futures, executors);
				for (Future future : futures) {
					future.get();
				}
				executors.shutdown();
				logger.info("Device list parsed today size : {}", synchronizedDeviceList.size());
				updateModelnameInDeviceList(synchronizedDeviceList);
				createDeviceJsonMap(synchronizedDeviceList);
			} else {
				logger.info("response is null");
			}
		} catch (Exception e) {
			logger.error("Error in creating and updating device data  : {}", Utils.getStackTrace(e));
		}
	}

	private void updateDeviceUsingJsonArray(final Map<String, String> brandNames, final JSONArray modelnamesJson,
			final List<JSONObject> synchronizedDeviceList, List<Future> futures, ExecutorService executors) {
		for (int i = ForesightConstants.ZERO; i < modelnamesJson.length(); i++) {
			final int j = i;
			futures.add(executors.submit(new Runnable() {
				@Override
				public void run() {
					try {
						JSONArray item = modelnamesJson.getJSONArray(j);
						String brand = brandNames.get(item.getString(ForesightConstants.ZERO));
						String modelname = item.getString(2);
						if (brand != null && !brand.isEmpty() && modelname != null && !modelname.isEmpty()) {
							JSONObject device = getAndParseDeviceData(getFormattedString(brand),
									getFormattedString(modelname), item.getString(1));
							if (device.length() != ForesightConstants.ZERO) {
								device.put(DeviceConstant.BRAND, brand);
								device.put(DeviceConstant.MODEL_CAMELCASE, modelname);
								cleanBands(device);
								synchronizedDeviceList.add(device);
							}
						}
					} catch (Exception e) {
						logger.error("Error in parsing brand model json : {}",Utils.getStackTrace(e));
					}
				}
			}));
		}
	}
	
	private Map<String, Device> createModelNameDeviceMap() {
		logger.info("Inside createModelNameDeviceMap method");
		Map<String, Device> deviceMap = new HashMap<>();
		try {
			List<Device> deviceList = iDeviceDao.findAll();
			for (Device device : deviceList) {
				deviceMap.put(device.getBrand() + ForesightConstants.AT_THE_RATE
								+ device.getModelName().replace(ForesightConstants.SPACE, ForesightConstants.UNDERSCORE),	device);
			}
			logger.info("Existing devices size : {}", deviceMap.size());
		} catch (Exception e) {
			logger.error("Error in creating model name in device map : {}", Utils.getStackTrace(e));
		}
		return deviceMap;
	}

	private void setDeviceDataFromJSONObject(Device device, JSONObject jsonObj) {
		try {
			setDeviceDataSectionFirst(device, jsonObj);
			setDeviceDataSectionSecond(device, jsonObj);
			setDeviceDataSectionThird(device, jsonObj);
			setDeviceDataSectionFourth(device, jsonObj);
			setDeviceDataSectionFifth(device, jsonObj);
			setDeviceDataSectionSixth(device, jsonObj);
			setDeviceDataSectionSeventh(device, jsonObj);
			setDeviceDataSectionEight(device, jsonObj);

		} catch (Exception e) {
			logger.error("Error in setting device data from JSON object  : {}", Utils.getStackTrace(e));
		}
	}

	private void setDeviceDataSectionEight(Device device, JSONObject jsonObj) {
		device.setBluetooth(jsonObj.has(DeviceConstant.BLUETOOTH) && !jsonObj.getString(DeviceConstant.BLUETOOTH).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.BLUETOOTH)) : null);
		device.setModelName(jsonObj.getString(DeviceConstant.MODEL_CAMELCASE));
		device.setBand2g(jsonObj.has(DeviceConstant.BANDS_2G) && !jsonObj.getString(DeviceConstant.BANDS_2G).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.BANDS_2G)) : null);
		device.setSpeed(jsonObj.has(DeviceConstant.SPEED) && !jsonObj.getString(DeviceConstant.SPEED).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.SPEED)) : null);
		device.setImageUrl(	jsonObj.has(DeviceConstant.IMAGE_URL) && !jsonObj.getString(DeviceConstant.IMAGE_URL).isEmpty() ? jsonObj.getString(DeviceConstant.IMAGE_URL) : null);

	}

	private void setDeviceDataSectionSeventh(Device device, JSONObject jsonObj) {
		device.setBrowser(jsonObj.has(DeviceConstant.BROWSER) && !jsonObj.getString(DeviceConstant.BROWSER).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.BROWSER)) : null);
		device.setJava(jsonObj.has(DeviceConstant.JAVA) && !jsonObj.getString(DeviceConstant.JAVA).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.JAVA)) : null);
		device.setColor(jsonObj.has(DeviceConstant.COLORS) && !jsonObj.getString(DeviceConstant.COLORS).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.COLORS)) : null);
		device.setJack35mm(jsonObj.has(DeviceConstant.JACK_3_5MM) && !jsonObj.getString(DeviceConstant.JACK_3_5MM).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.JACK_3_5MM)) : null);
		device.setWeight(jsonObj.has(DeviceConstant.WEIGHT_CAMEL) && !jsonObj.getString(DeviceConstant.WEIGHT_CAMEL).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.WEIGHT_CAMEL)): null);
		device.setStandby(jsonObj.has(DeviceConstant.STAND_BY) && !jsonObj.getString(DeviceConstant.STAND_BY).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.STAND_BY)) : null);
		
	}

	private void setDeviceDataSectionSixth(Device device, JSONObject jsonObj) {
		device.setFeature(jsonObj.has(DeviceConstant.FEATURES) && !jsonObj.getString(DeviceConstant.FEATURES).isEmpty() ? getValidValue( jsonObj.getString(DeviceConstant.FEATURES)) : null);
		device.setVideo(jsonObj.has(DeviceConstant.VIDEO) && !jsonObj.getString(DeviceConstant.VIDEO).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.VIDEO)) : null);
		device.setDevicesize(jsonObj.has(DeviceConstant.SIZE_CAMEL) && !jsonObj.getString(DeviceConstant.SIZE_CAMEL).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.SIZE_CAMEL)) : null);
		device.setAlertType(jsonObj.has(DeviceConstant.ALERT_TYPES) && !jsonObj.getString(DeviceConstant.ALERT_TYPES).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.ALERT_TYPES)): null);
		device.setLoudspeaker(jsonObj.has(DeviceConstant.LOUDSPEAKER) && !jsonObj.getString(DeviceConstant.LOUDSPEAKER).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.LOUDSPEAKER))	: null);
		device.setChipset(jsonObj.has(DeviceConstant.CHIPSET) && !jsonObj.getString(DeviceConstant.CHIPSET).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.CHIPSET)) : null);
		
	}

	private void setDeviceDataSectionFifth(Device device, JSONObject jsonObj) {
		device.setTalktime(	jsonObj.has(DeviceConstant.TALK_TIME) && !jsonObj.getString(DeviceConstant.TALK_TIME).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.TALK_TIME)) : null);
		device.setMusicplay(jsonObj.has(DeviceConstant.MUSIC_PLAY) && !jsonObj.getString(DeviceConstant.MUSIC_PLAY).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.MUSIC_PLAY)) : null);
		device.setPriceGroup(jsonObj.has(DeviceConstant.PRICE_GROUP) && !jsonObj.getString(DeviceConstant.PRICE_GROUP).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.PRICE_GROUP))	: null);
		device.setSarUs(jsonObj.has(DeviceConstant.SAR_US) && !jsonObj.getString(DeviceConstant.SAR_US).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.SAR_US)) : null);
		device.setSarEu(jsonObj.has(DeviceConstant.SAR_EU) && !jsonObj.getString(DeviceConstant.SAR_EU).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.SAR_EU)) : null);
		device.setModificationTime(new Date());
		device.setModelCode(jsonObj.has(DeviceConstant.MODEL_CODE) && !jsonObj.getString(DeviceConstant.MODEL_CODE).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.MODEL_CODE))	: null);
	}

	private void setDeviceDataSectionFourth(Device device, JSONObject jsonObj) {
		device.setGps(jsonObj.has(DeviceConstant.GPS) && !jsonObj.getString(DeviceConstant.GPS).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.GPS)) : null);
		device.setRadio(jsonObj.has(DeviceConstant.RADIO) && !jsonObj.getString(DeviceConstant.RADIO).isEmpty()  ? getValidValue(jsonObj.getString(DeviceConstant.RADIO)) : null);
		device.setUsb(jsonObj.has(DeviceConstant.USB) && !jsonObj.getString(DeviceConstant.USB).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.USB)) : null);
		device.setSensor(jsonObj.has(DeviceConstant.SENSORS) && !jsonObj.getString(DeviceConstant.SENSORS).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.SENSORS)) : null);
		device.setMessaging(jsonObj.has(DeviceConstant.MESSASING) && !jsonObj.getString(DeviceConstant.MESSASING).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.MESSASING)) : null);
		device.setStatus(jsonObj.has(DeviceConstant.STATUS_CAMEL) && !jsonObj.getString(DeviceConstant.STATUS_CAMEL).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.STATUS_CAMEL)): null);
		device.setEdge(jsonObj.has(DeviceConstant.EDGE) && !jsonObj.getString(DeviceConstant.EDGE).isEmpty() ? getValidValue( jsonObj.getString(DeviceConstant.EDGE)) : null);

	}

	private void setDeviceDataSectionThird(Device device, JSONObject jsonObj) {
		device.setCpu(jsonObj.has(DeviceConstant.TELEMETRY_CPU) && !jsonObj.getString(DeviceConstant.TELEMETRY_CPU).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.TELEMETRY_CPU)) : null);
		device.setGpu(jsonObj.has(DeviceConstant.GPU) && !jsonObj.getString(DeviceConstant.GPU).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.GPU)) : null);
		device.setCardslot(jsonObj.has(DeviceConstant.CARD_SLOT) && !jsonObj.getString(DeviceConstant.CARD_SLOT).isEmpty()  ? getValidValue(jsonObj.getString(DeviceConstant.CARD_SLOT)) : null);
		device.setInternalMemory(jsonObj.has(DeviceConstant.INTERNAL) && !jsonObj.getString(DeviceConstant.INTERNAL).isEmpty() ? getValidValue( jsonObj.getString(DeviceConstant.INTERNAL)) : null);
		device.setPrimaryCamera(jsonObj.has(DeviceConstant.PRIMARY_CAMEL_CASE) && !jsonObj.getString(DeviceConstant.PRIMARY_CAMEL_CASE).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.PRIMARY_CAMEL_CASE)): null);
		device.setSecondaryCamera(jsonObj.has(DeviceConstant.SECONDARY_CAMEL_CASE) && !jsonObj.getString(DeviceConstant.SECONDARY_CAMEL_CASE).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.SECONDARY_CAMEL_CASE)): null);
		device.setVideoCallSupport(jsonObj.has(DeviceConstant.SECONDARY_CAMEL_CASE) && !jsonObj.getString(DeviceConstant.SECONDARY_CAMEL_CASE).isEmpty()&& jsonObj.getString(DeviceConstant.SECONDARY_CAMEL_CASE).contains(DeviceConstant.DUAL_VIDEO_CALL)  
				? DeviceConstant.YES : DeviceConstant.NO);

	}

	private void setDeviceDataSectionSecond(Device device, JSONObject jsonObj) {
		device.setGprs(jsonObj.has(DeviceConstant.GPRS) && !jsonObj.getString(DeviceConstant.GPRS).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.GPRS)) : null);
		device.setAnnounced(jsonObj.has(DeviceConstant.ANNOUNCED) && !jsonObj.getString(DeviceConstant.ANNOUNCED).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.ANNOUNCED)) : null);
		device.setDimension(jsonObj.has(DeviceConstant.DIMENSIONS) && !jsonObj.getString(DeviceConstant.DIMENSIONS).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.DIMENSIONS)) : null);
		device.setDisplayType(jsonObj.has(DeviceConstant.TYPE_CAPITALISE) && !jsonObj.getString(DeviceConstant.TYPE_CAPITALISE).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.TYPE_CAPITALISE)): null);
		device.setResolution(jsonObj.has(DeviceConstant.RESOLUTION) && !jsonObj.getString(DeviceConstant.RESOLUTION).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.RESOLUTION)) : null);
		device.setMultitouch(jsonObj.has(DeviceConstant.MULTITOUCH) && !jsonObj.getString(DeviceConstant.MULTITOUCH).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.MULTITOUCH)) : null);
		device.setOs(jsonObj.has(DeviceConstant.OS_CAPITAL) && !jsonObj.getString(DeviceConstant.OS_CAPITAL).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.OS_CAPITAL)) : null);
	}

	private void setDeviceDataSectionFirst(Device device, JSONObject jsonObj) {
		device.setBrand(jsonObj.getString(DeviceConstant.BRAND));
		device.setBand3g(jsonObj.has(DeviceConstant.BANDS_3G)  && !jsonObj.getString(DeviceConstant.BANDS_3G).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.BANDS_3G)) : null);
		device.setBand4g(jsonObj.has(DeviceConstant.BANDS_4G)  && !jsonObj.getString(DeviceConstant.BANDS_4G).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.BANDS_4G)) : null);
		device.setBattery(jsonObj.has(DeviceConstant.BATTERY_LIFE)  && !jsonObj.getString(DeviceConstant.BATTERY_LIFE).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.BATTERY_LIFE)): null);
		device.setWlan(jsonObj.has(DeviceConstant.WLAN) && !jsonObj.getString(DeviceConstant.WLAN).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.WLAN)): null);
		device.setSim(jsonObj.has(DeviceConstant.SIM) && !jsonObj.getString(DeviceConstant.SIM).isEmpty() ? getValidValue(jsonObj.getString(DeviceConstant.SIM)) : null);
		device.setTechnology(jsonObj.has(DeviceConstant.TECHNOLOGY_RAN) && !jsonObj.getString(DeviceConstant.TECHNOLOGY_RAN).isEmpty() ? getValidValue( jsonObj.getString(DeviceConstant.TECHNOLOGY_RAN)): null);
	}


	private String getValidValue(String value) {
		if (Utils.checkForValueInString(value).booleanValue()) {
			StringBuilder correctValue = new StringBuilder(ForesightConstants.BLANK_STRING);
			Pattern p = Pattern.compile("[\\w+\\s+\\d+\\(\\).\\/:~%\\-\\+~@%^&';/>.,]");
			Matcher m = p.matcher(value);
			while (m.find()) {
				correctValue.append(m.group());
			}
			return correctValue.toString();
		} else {
			return ForesightConstants.BLANK_STRING;
		}
	}
	
	private void createDeviceJsonMap(List<JSONObject> list) {
		Map<String,JSONObject> deviceJsonMap= new HashMap<>();
		try {
			for (JSONObject deviceJsonObj : list) {
				String key = deviceJsonObj.getString(DeviceConstant.BRAND) + ForesightConstants.AT_THE_RATE
						+ deviceJsonObj.getString(DeviceConstant.MODEL_CAMELCASE).replace(ForesightConstants.SPACE,
								ForesightConstants.UNDERSCORE);
				deviceJsonMap.put(key,deviceJsonObj);
			}
			setDeviceDataToDB(deviceJsonMap);
		} catch (Exception e) {
			logger.error("Error in creating device json map : {}", Utils.getStackTrace(e));
		}
	}
	
	private void setDeviceDataToDB(Map<String, JSONObject> deviceJsonMap) {
		Map<String, Device> deviceMap = createModelNameDeviceMap();
		Integer updateDeviceCount=ForesightConstants.ZERO;
		Integer createDeviceCount=ForesightConstants.ZERO;
		for (Map.Entry<String, JSONObject> entry : deviceJsonMap.entrySet()) {
			try {
				String key = entry.getKey();
				JSONObject deviceJsonObj = entry.getValue();
				Device device = null;
				if (deviceMap.containsKey(key)) {
					device = deviceMap.get(key);
					setDeviceDataFromJSONObject(device, deviceJsonObj);
					iDeviceDao.update(device);
					updateDeviceCount++;
				} else {
					device = new Device();
					setDeviceDataFromJSONObject(device, deviceJsonObj);
					device.setCreationTime(new Date());
					device.setGuid(UUID.randomUUID().toString());
					iDeviceDao.create(device);
					createDeviceCount++;
				}
			} catch (Exception e) {
				logger.error("Error in creating device : {}",Utils.getStackTrace(e));
			}
		}
		logger.info("Total no. created device : {} and updated device : {}",createDeviceCount,updateDeviceCount);
	}
	
}

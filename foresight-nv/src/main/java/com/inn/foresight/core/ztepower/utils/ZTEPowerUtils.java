package com.inn.foresight.core.ztepower.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.ztepower.constants.ZTEPowerConstants;
import com.inn.foresight.core.ztepower.model.ZTEPowerNEMeteMapping;
import com.inn.foresight.core.ztepower.model.ZTEPowerStationNEMapping;
import com.inn.foresight.core.ztepower.wrapper.ZTEPowerRequestWrapper;
import com.inn.foresight.core.ztepower.wrapper.ZTEPowerResultWrapper;

public class ZTEPowerUtils extends ZTEPowerConstants{

	public static ZTEPowerResultWrapper getZTEPowerAreaWiseCountResponse(List<ZTEPowerResultWrapper> list) {
		return null;
	}

	public static ZTEPowerResultWrapper getZTEPowerStationWiseCountResponse(List<ZTEPowerResultWrapper> list) {
		return null;
	}

	public static ZTEPowerResultWrapper getZTEPowerDeviceWiseCountResponse(List<ZTEPowerResultWrapper> list) {
		return null;
	}
	public static void addPredicateForZTEPowerData(ZTEPowerRequestWrapper wrapper,
			Root<ZTEPowerStationNEMapping> stationDevice,Root<ZTEPowerNEMeteMapping>deviceMete, List<Predicate> predicates) {
		if (Utils.isValidList(wrapper.getAreaName())) {
			Expression<String> parentExpression = stationDevice.get(KEY_ZTE_POWER_STATION_TABLE).get(KEY_ZTE_POWER_REGION_TABLE).get(KEY_AREA_NAME);
			Predicate eNamePredicate = parentExpression.in(wrapper.getAreaName());
			predicates.add(eNamePredicate);
		}
		if (Utils.isValidList(wrapper.getAreaId())) {
			Expression<String> parentExpression = stationDevice.get(KEY_ZTE_POWER_STATION_TABLE).get(KEY_ZTE_POWER_REGION_TABLE).get(KEY_AREA_ID);
			Predicate eNamePredicate = parentExpression.in(wrapper.getAreaId());
			predicates.add(eNamePredicate);
		}
		if (Utils.isValidList(wrapper.getLscName())) {
			Expression<String> parentExpression = stationDevice.get(KEY_ZTE_POWER_STATION_TABLE).get(KEY_ZTE_POWER_REGION_TABLE).get(KEY_LSC_NAME);
			Predicate eNamePredicate = parentExpression.in(wrapper.getLscName());
			predicates.add(eNamePredicate);
		}
		if (Utils.isValidList(wrapper.getLscId())) {
			Expression<String> parentExpression = stationDevice.get(KEY_ZTE_POWER_STATION_TABLE).get(KEY_ZTE_POWER_REGION_TABLE).get(KEY_LSC_ID);
			Predicate eNamePredicate = parentExpression.in(wrapper.getLscId());
			predicates.add(eNamePredicate);
		}
		if (Utils.isValidList(wrapper.getStationName())) {
			Expression<String> parentExpression = stationDevice.get(KEY_ZTE_POWER_STATION_TABLE).get(KEY_STATION_NAME);
			Predicate eNamePredicate = parentExpression.in(wrapper.getStationName());
			predicates.add(eNamePredicate);
		}
		if (Utils.isValidList(wrapper.getStationId())) {
			Expression<String> parentExpression = stationDevice.get(KEY_ZTE_POWER_STATION_TABLE).get(KEY_STATION_ID);
			Predicate eNamePredicate = parentExpression.in(wrapper.getStationName());
			predicates.add(eNamePredicate);
		}
		if (Utils.isValidList(wrapper.getDeviceName())) {
			Expression<String> parentExpression = stationDevice.get(KEY_NETWORK_ELEMENT_TABLE).get(KEY_NE_NAME);
			Predicate eNamePredicate = parentExpression.in(wrapper.getDeviceName());
			predicates.add(eNamePredicate);
		}
		if (Utils.isValidList(wrapper.getDeviceId())) {
			Expression<String> parentExpression = stationDevice.get(KEY_NETWORK_ELEMENT_TABLE).get(KEY_NE_ID);
			Predicate eNamePredicate = parentExpression.in(wrapper.getDeviceId());
			predicates.add(eNamePredicate);
		}
		if (Utils.isValidList(wrapper.getGeographyL4())) {
			Expression<String> parentExpression = stationDevice.get(KEY_ZTE_POWER_STATION_TABLE).get(KEY_GEOGRAPHYL4_TABLE).get(KEY_NAME);
			Predicate eNamePredicate = parentExpression.in(wrapper.getGeographyL4());
			predicates.add(eNamePredicate);
		}
		if (Utils.isValidList(wrapper.getGeographyL3())) {
			Expression<String> parentExpression = stationDevice.get(KEY_ZTE_POWER_STATION_TABLE).get(KEY_GEOGRAPHYL4_TABLE).get(KEY_GEOGRAPHYL3_TABLE).get(KEY_NAME);
			Predicate eNamePredicate = parentExpression.in(wrapper.getGeographyL3());
			predicates.add(eNamePredicate);
		}
		if (Utils.isValidList(wrapper.getGeographyL2())) {
			Expression<String> parentExpression = stationDevice.get(KEY_ZTE_POWER_STATION_TABLE).get(KEY_GEOGRAPHYL4_TABLE).get(KEY_GEOGRAPHYL3_TABLE).get(KEY_GEOGRAPHYL2_TABLE).get(KEY_NAME);
			Predicate eNamePredicate = parentExpression.in(wrapper.getGeographyL2());
			predicates.add(eNamePredicate);
		}
		if (Utils.isValidList(wrapper.getGeographyL1())) {
			Expression<String> parentExpression = stationDevice.get(KEY_ZTE_POWER_STATION_TABLE).get(KEY_GEOGRAPHYL4_TABLE).get(KEY_GEOGRAPHYL3_TABLE).get(KEY_GEOGRAPHYL2_TABLE).get(KEY_GEOGRAPHYL1_TABLE).get(KEY_NAME);
			Predicate eNamePredicate = parentExpression.in(wrapper.getGeographyL1());
			predicates.add(eNamePredicate);
		}
		if (Utils.isValidList(wrapper.getMeteId())) {
			Expression<String> parentExpression = deviceMete.get(KEY_ZTE_POWER_METE_INFO_TABLE).get(KEY_METE_ID);
			Predicate eNamePredicate = parentExpression.in(wrapper.getMeteId());
			predicates.add(eNamePredicate);
		}
		if (Utils.isValidList(wrapper.getMeteName())) {
			Expression<String> parentExpression = deviceMete.get(KEY_ZTE_POWER_METE_INFO_TABLE).get(KEY_METE_NAME);
			Predicate eNamePredicate = parentExpression.in(wrapper.getMeteName());
			predicates.add(eNamePredicate);
		}
		if (Utils.isValidList(wrapper.getMeteKind())) {
			Expression<String> parentExpression = deviceMete.get(KEY_ZTE_POWER_METE_INFO_TABLE).get(KEY_METE_KIND);
			Predicate eNamePredicate = parentExpression.in(wrapper.getMeteId());
			predicates.add(eNamePredicate);
		}
	}

	public static Map<String, Object> getResponseToReturn(List<ZTEPowerResultWrapper> ztePowerResultWrapperList,
			ZTEPowerRequestWrapper wrapper) {
		Map<String, Object> ztePowerDataMap = new HashMap<>();
		if (wrapper.getIsCount() != null && wrapper.getIsCount()) {
			ztePowerDataMap.put(COUNT, ztePowerResultWrapperList.size());
		} else {
			ztePowerDataMap.put(DATA_MAP, ztePowerResultWrapperList);
		}
		return ztePowerDataMap;
	}

	
	}
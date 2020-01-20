package com.inn.foresight.module.nv.device.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.module.nv.app.model.DeviceInfo;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.device.constant.DeviceConstant;
import com.inn.product.um.geography.model.GeographyL1;
import com.inn.product.um.geography.model.GeographyL2;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;

/**
 * The Class NVDeviceData.
 *
 * @author innoeye date - 27-Feb-2018 1:15:55 PM
 */
@NamedQueries({

		// Device Manager
		//@NamedQuery(name = DeviceConstant.GET_ALL_NV_DEVICE_LIST, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper(d.deviceId,n.make,n.model,n.imei,n.os,n.appVersion,n.modificationTime,n.geographyL4.name,n.operator,n.module,n.imsi,n.source,case when d.user.userid is null then false else true end,case when d.user.userid is not null then u.firstName else null end ,case when d.user.userid is not null then d.user.lastName else null end) from NVDeviceData n  join n.deviceInfo d left outer join d.user u  where n.geographyL4 is not null and n.geographyL3 is not null and n.geographyL2 is not null and n.geographyL1 is not null order by n.modificationTime desc"),
		
	@NamedQuery(name = DeviceConstant.GET_ALL_NV_DEVICE_LIST, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper(deviceinfo.deviceId,n.make,n.model,n.imei,n.os,n.appVersion,n.modificationTime,gl4.name,n.operator,n.module,n.imsi,n.source,case when deviceinfo.user.userid is null then false else true end,case when deviceinfo.user.userid is not null then u.firstName else null end ,case when deviceinfo.user.userid is not null then u.lastName else null end) from NVDeviceData n inner join DeviceInfo deviceinfo on n.deviceInfo.id=deviceinfo.id left join User u on u.id=deviceinfo.user.id left join GeographyL4 gl4 on gl4.id=n.geographyL4.id  and n.latitude is not null and n.longitude is not null and n.geographyL4 is not null and n.geographyL3 is not null and n.geographyL2 is not null and n.geographyL1 is not null order by n.modificationTime desc "),

	@NamedQuery(name = DeviceConstant.GET_DEVICE_LIST_COUNT, query = "select count (*) from NVDeviceData n where n.latitude is not null and n.longitude is not null and n.geographyL4 is not null and n.geographyL3 is not null and n.geographyL2 is not null and n.geographyL1 is not null"),
		// Device Manager

		// Device Layer
		//@NamedQuery(name = DeviceConstant.GET_DEVICE_COUNT_GROUP_BY_L1, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper(count(n.id),n.geographyL1.name,n.geographyL1.latitude,n.geographyL1.longitude,n.geographyL1.id) from NVDeviceData n  where n.geographyL1.id in (select g.id from GeographyL1 g where g.latitude>=:sWLat and  g.latitude<=:nELat and g.longitude>=:sWLng and g.longitude<=:nELng and n.geographyL4.id is not null and n.geographyL3.id is not null and n.geographyL2.id is not null and n.geographyL1.id is not null)group by n.geographyL1.id,n.geographyL1.name,n.geographyL1.latitude,n.geographyL1.longitude"),
     	@NamedQuery(name = DeviceConstant.GET_DEVICE_COUNT_GROUP_BY_L1, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper(count(n.id),gl1.name,gl1.latitude,gl1.longitude,gl1.id) from NVDeviceData n join fetch GeographyL1 gl1 on n.geographyL1.id=gl1.id where n.geographyL1.id is not null and n.operator is not null and n.latitude is not null and n.longitude is not null group by gl1.name order by n.modificationTime desc"),

	//	@NamedQuery(name = DeviceConstant.GET_DEVICE_COUNT_GROUP_BY_L2, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper(count(n.id),n.geographyL2.name,n.geographyL2.latitude,n.geographyL2.longitude,n.geographyL2.id) from NVDeviceData n  where n.geographyL2.id in (select g.id from GeographyL2 g where g.latitude>=:sWLat and  g.latitude<=:nELat and g.longitude>=:sWLng and g.longitude<=:nELng and n.geographyL4.id is not null and n.geographyL3.id is not null and n.geographyL2.id is not null and n.geographyL1.id is not null)group by n.geographyL2.id,n.geographyL2.name,n.geographyL2.latitude,n.geographyL2.longitude"),
     	@NamedQuery(name = DeviceConstant.GET_DEVICE_COUNT_GROUP_BY_L2, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper(count(n.id),gl2.name,gl2.latitude,gl2.longitude,gl2.id) from NVDeviceData n join fetch GeographyL2 gl2 on n.geographyL2.id =gl2.id where n.geographyL2.id is not null and n.operator is not null and n.latitude is not null and n.longitude is not null group by gl2.name order by n.modificationTime desc"),
     //	@NamedQuery(name = DeviceConstant.GET_DEVICE_COUNT_GROUP_BY_L3, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper(count(n.id),n.geographyL3.name,n.geographyL3.latitude,n.geographyL3.longitude,n.geographyL3.id) from NVDeviceData n  where n.geographyL3.id in (select g.id from GeographyL3 g where g.latitude>=:sWLat and  g.latitude<=:nELat and g.longitude>=:sWLng and g.longitude<=:nELng and n.geographyL4.id is not null and n.geographyL3.id is not null and n.geographyL2.id is not null and n.geographyL1.id is not null)group by n.geographyL3.id,n.geographyL3.name,n.geographyL3.latitude,n.geographyL3.longitude"),
     	@NamedQuery(name = DeviceConstant.GET_DEVICE_COUNT_GROUP_BY_L3, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper(count(n.id),gl3.name,gl3.latitude,gl3.longitude,gl3.id) from NVDeviceData n join fetch GeographyL3 gl3 on n.geographyL3.id =gl3.id where n.geographyL3.id is not null and n.operator is not null and n.latitude is not null and n.longitude is not null group by gl3.name  order by n.modificationTime desc"),

     	
     //	@NamedQuery(name = DeviceConstant.GET_DEVICE_COUNT_GROUP_BY_L4, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper(count(n.id),n.geographyL4.name,n.geographyL4.latitude,n.geographyL4.longitude,n.geographyL4.id) from NVDeviceData n  where n.geographyL4.id in (select g.id from GeographyL4 g where g.latitude>=:sWLat and  g.latitude<=:nELat and g.longitude>=:sWLng and g.longitude<=:nELng and n.geographyL4.id is not null and n.geographyL3.id is not null and n.geographyL2.id is not null and n.geographyL1.id is not null)group by n.geographyL4.id,n.geographyL4.name,n.geographyL4.latitude,n.geographyL4.longitude"),
     	@NamedQuery(name = DeviceConstant.GET_DEVICE_COUNT_GROUP_BY_L4, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper(count(n.id),gl4.name,gl4.latitude,gl4.longitude,gl4.id) from NVDeviceData n join fetch GeographyL4 gl4 on n.geographyL4.id =gl4.id where n.geographyL4.id is not null and n.operator is not null and n.latitude is not null and n.longitude is not null group by gl4.name  order by n.modificationTime desc"),
     	@NamedQuery(name = DeviceConstant.GET_DEVICE_FOR_VIEW_PORT, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper(n.deviceInfo.deviceId,n.make,n.model,n.imei,n.os,n.appVersion,n.modificationTime,n.geographyL4.name,n.operator,n.module,n.imsi,n.source,case when n.deviceInfo.user is null then false else true end,n.latitude,n.longitude) from NVDeviceData n where n.latitude>=:sWLat and  n.latitude<=:nELat and n.longitude>=:sWLng and n.longitude<=:nELng and n.geographyL4 is not null and n.geographyL3 is not null and n.geographyL2 is not null and n.geographyL1 is not null order by n.modificationTime desc"),
		@NamedQuery(name = DeviceConstant.GET_NV_DEVICE_DATA_WRAPPER_BY_DEVICE_ID, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper(n.deviceInfo.deviceId,n.make,n.model,n.imei,n.imsi,n.os,n.appVersion,n.modificationTime,n.geographyL4.name,n.operator,n.module,n.source,case when n.deviceInfo.user is null then false else true end,n.latitude,n.longitude) from NVDeviceData n where n.deviceInfo.deviceId =:deviceId"),
		@NamedQuery(name = DeviceConstant.GET_DEVICE_LIST_INSIDE_CLUSTER, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper(n.deviceInfo.deviceId,n.make,n.model,n.imei,n.os,n.appVersion,n.modificationTime,n.geographyL4.name,n.operator,n.module,n.imsi,n.source,case when n.deviceInfo.user is null then false else true end) from NVDeviceData n where n.geographyL4.name=:geographyName and n.geographyL4.id is not null and n.geographyL3.id is not null and n.geographyL2.id is not null and n.geographyL1.id is not null"),
		// Device Layer

		@NamedQuery(name = DeviceConstant.GET_DEVICE_RECHARGE_NOTIFICATION, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper(n.make,n.model,n.imei,n.imsi,n.deviceGroup.groupId,n.deviceGroup.address,n.geographyL4.geographyL3.name,n.msisdn,n.appVersion,n.lastRechargeDate,n.rechargePlan,n.validity,n.nextRechargeDate) from NVDeviceData n where DATEDIFF(n.nextRechargeDate,current_date()) <=:daysOfDifference"),
		@NamedQuery(name = DeviceConstant.GET_DEVICE_RECHARGE_NOTIFICATION_COUNT, query = "select count(n) from NVDeviceData n where DATEDIFF(n.nextRechargeDate,current_date()) <=:daysOfDifference and n.deviceGroup is not null and n.geographyL4.geographyL3.name is not null"),

		@NamedQuery(name = DeviceConstant.GET_DEVICE_COUNT_WRAPPER_BY_L2, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceCountWrapper(count(n.id),n.geographyL2.name,n.geographyL2.latitude,n.geographyL2.longitude,n.geographyL2.id) from NVDeviceData n  where n.geographyL2.id in (select g.id from GeographyL2 g where g.latitude>=:sWLat and  g.latitude<=:nELat and g.longitude>=:sWLng and g.longitude<=:nELng and n.geographyL4.id is not null and n.geographyL3.id is not null and n.geographyL2.id is not null and n.geographyL1.id is not null)group by n.geographyL2.id,n.geographyL2.name,n.geographyL2.latitude,n.geographyL2.longitude"),
		@NamedQuery(name = DeviceConstant.GET_DEVICE_COUNT_WRAPPER_BY_L3, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceCountWrapper(count(n.id),n.geographyL3.name,n.geographyL3.latitude,n.geographyL3.longitude,n.geographyL3.id) from NVDeviceData n  where n.geographyL3.id in (select g.id from GeographyL3 g where g.latitude>=:sWLat and  g.latitude<=:nELat and g.longitude>=:sWLng and g.longitude<=:nELng and n.geographyL4.id is not null and n.geographyL3.id is not null and n.geographyL2.id is not null and n.geographyL1.id is not null)group by n.geographyL3.id,n.geographyL3.name,n.geographyL3.latitude,n.geographyL3.longitude"),
		@NamedQuery(name = DeviceConstant.GET_DEVICE_COUNT_WRAPPER_BY_L4, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceCountWrapper(count(n.id),n.geographyL4.name,n.geographyL4.latitude,n.geographyL4.longitude,n.geographyL4.id) from NVDeviceData n  where n.geographyL4.id in (select g.id from GeographyL4 g where g.latitude>=:sWLat and  g.latitude<=:nELat and g.longitude>=:sWLng and g.longitude<=:nELng and n.geographyL4.id is not null and n.geographyL3.id is not null and n.geographyL2.id is not null and n.geographyL1.id is not null)group by n.geographyL4.id,n.geographyL4.name,n.geographyL4.latitude,n.geographyL4.longitude"),
		@NamedQuery(name = DeviceConstant.GET_DEVICE_LIST_INSIDE_VIEW_PORT, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper(n.deviceInfo.deviceId,n.make,n.model,n.imei,n.os,n.appVersion,n.modificationTime,n.geographyL4.name,n.operator,n.module,n.imsi,n.source,n.isEnterprise) from NVDeviceData n where n.geographyL4.id in (select g.id from GeographyL4 g where g.latitude>=:sWLat and  g.latitude<=:nELat and g.longitude>=:sWLng and g.longitude<=:nELng and n.geographyL4.id is not null and n.geographyL3.id is not null and n.geographyL2.id is not null and n.geographyL1.id is not null and g.geographyL3.id=:geographyId)order by n.modificationTime desc"),

		@NamedQuery(name = DeviceConstant.GET_NV_DEVICE_DETAIL_BY_DEVICE_ID, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper(n.deviceInfo.deviceId,n.make,n.model,n.imei,n.imsi,n.os,n.appVersion,n.modificationTime,n.geographyL4.name,n.operator,n.module,n.batteryLevel,n.latitude,n.longitude,n.locationType,n.locationInfo,n.lastRechargeDate,n.rechargePlan,n.validity,n.nextRechargeDate,n.deviceGroup.groupId,n.msisdn,n.type) from NVDeviceData n where n.deviceInfo.deviceId =:deviceId"),

		@NamedQuery(name = DeviceConstant.FIND_DEVICE_INFO, query = "select n.deviceInfo from NVDeviceData n"),

		@NamedQuery(name = DeviceConstant.GET_DEVICE_DATA_BY_DEVICE_ID, query = "select n from NVDeviceData n where n.deviceInfo.deviceId =:deviceId"),

		@NamedQuery(name = DeviceConstant.GET_DEVICE_LIST_FOR_WO, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper(n.id,n.deviceInfo.deviceId,n.make,n.model,n.imei,n.os,n.appVersion,n.modificationTime,n.geographyL4.name,n.operator,n.module,n.imsi) from NVDeviceData n "),

		@NamedQuery(name = DeviceConstant.GET_DEVICE_LIST_BY_IDS, query = "select n from NVDeviceData n where n.id in (:idList)"),

		@NamedQuery(name = DeviceConstant.GET_DEVICE_LIST_COUNT_FOR_WO, query = "select count (n) from NVDeviceData n "),

		@NamedQuery(name = DeviceConstant.GET_DEVICES_BY_DEVICE_IDS, query = "select n from NVDeviceData n where n.deviceInfo.deviceId in(:deviceId)"),

		@NamedQuery(name = DeviceConstant.GET_DEVICES_BY_DEVICE_IDS_AND_GROUP_ID, query = "select n from NVDeviceData n where n.deviceInfo.deviceId in(:deviceId) and n.deviceGroup.id = :groupId"),

		@NamedQuery(name = DeviceConstant.GET_DEVICES_BY_GROUP_ID, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper(n.deviceInfo.deviceId,n.make,n.model,n.imei,n.imsi,n.os,n.appVersion,n.modificationTime,n.geographyL4.name,n.operator,n.module,n.batteryLevel,n.latitude,n.longitude,n.locationType,n.locationInfo,n.lastRechargeDate,n.rechargePlan,n.validity,n.nextRechargeDate,n.deviceGroup.groupId,n.msisdn,n.type) from NVDeviceData n where n.deviceGroup.id = :groupId"),

		@NamedQuery(name = DeviceConstant.GET_DEVICE_DATA_BY_GROUP_ID, query = "select n from NVDeviceData n where n.deviceGroup.id = :groupId"),

		@NamedQuery(name = DeviceConstant.GET_DEVICE_COUNT_BY_GROUP_ID, query = "select count(n) from NVDeviceData n where n.deviceGroup.id = :groupId"),

		@NamedQuery(name = DeviceConstant.GET_DEVICE_LIST_FOR_WORK_ORDER, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper(n.id,n.make,n.model,n.imei,n.appVersion,n.modificationTime,n.geographyL4.name) from NVDeviceData n where n.deviceGroup.id is not null and n.deviceInfo.deviceId is not null and n.geographyL4.name is not null and n.id not in (select wo.deviceData.id from NVDeviceWOMapping wo where (((Date(wo.startTime) <= Date(:woStartTime) and Date(:woEndTime) <=  Date(wo.endTime)) or (Date(wo.startTime) >= Date(:woStartTime) and Date(wo.startTime) <= Date(:woEndTime) and Date(:woEndTime) <= Date(wo.endTime)) or	(Date(wo.startTime) <= Date(:woStartTime) and Date(:woStartTime) <= Date(wo.endTime) and Date(:woEndTime) >= Date(wo.endTime))) and ((DATE_FORMAT(wo.startTime, '%H:%i:%s') <= DATE_FORMAT(:woStartTime, '%H:%i:%s') and DATE_FORMAT(:woEndTime, '%H:%i:%s') <=  DATE_FORMAT(wo.endTime, '%H:%i:%s')) or (DATE_FORMAT(wo.startTime, '%H:%i:%s') >= DATE_FORMAT(:woStartTime, '%H:%i:%s') and DATE_FORMAT(wo.startTime, '%H:%i:%s') <= DATE_FORMAT(:woEndTime, '%H:%i:%s') and DATE_FORMAT(:woEndTime, '%H:%i:%s') <= DATE_FORMAT(wo.endTime, '%H:%i:%s')) or	(DATE_FORMAT(wo.startTime, '%H:%i:%s') <= DATE_FORMAT(:woStartTime, '%H:%i:%s') and DATE_FORMAT(:woStartTime, '%H:%i:%s') <= DATE_FORMAT(wo.endTime, '%H:%i:%s') and DATE_FORMAT(:woEndTime, '%H:%i:%s') >= DATE_FORMAT(wo.endTime, '%H:%i:%s')))))"),

		@NamedQuery(name = DeviceConstant.GET_DEVICE_LIST_COUNT_FOR_WORK_ORDER, query = "select count(n.id) from NVDeviceData n where n.deviceGroup.id is not null and n.deviceInfo.deviceId is not null and n.geographyL4.name is not null and n.id not in (select wo.deviceData.id from NVDeviceWOMapping wo where (((Date(wo.startTime) <= Date(:woStartTime) and Date(:woEndTime) <=  Date(wo.endTime)) or (Date(wo.startTime) >= Date(:woStartTime) and Date(wo.startTime) <= Date(:woEndTime) and Date(:woEndTime) <= Date(wo.endTime)) or	(Date(wo.startTime) <= Date(:woStartTime) and Date(:woStartTime) <= Date(wo.endTime) and Date(:woEndTime) >= Date(wo.endTime))) and ((DATE_FORMAT(wo.startTime, '%H:%i:%s') <= DATE_FORMAT(:woStartTime, '%H:%i:%s') and DATE_FORMAT(:woEndTime, '%H:%i:%s') <=  DATE_FORMAT(wo.endTime, '%H:%i:%s')) or (DATE_FORMAT(wo.startTime, '%H:%i:%s') >= DATE_FORMAT(:woStartTime, '%H:%i:%s') and DATE_FORMAT(wo.startTime, '%H:%i:%s') <= DATE_FORMAT(:woEndTime, '%H:%i:%s') and DATE_FORMAT(:woEndTime, '%H:%i:%s') <= DATE_FORMAT(wo.endTime, '%H:%i:%s')) or	(DATE_FORMAT(wo.startTime, '%H:%i:%s') <= DATE_FORMAT(:woStartTime, '%H:%i:%s') and DATE_FORMAT(:woStartTime, '%H:%i:%s') <= DATE_FORMAT(wo.endTime, '%H:%i:%s') and DATE_FORMAT(:woEndTime, '%H:%i:%s') >= DATE_FORMAT(wo.endTime, '%H:%i:%s')))))"),

		@NamedQuery(name = DeviceConstant.GET_DEVICE_LIST_BY_DEVICE_INFO_IDS, query = "select n from NVDeviceData n where n.deviceInfo.id in (:idList)"),
		@NamedQuery(name = DeviceConstant.GET_NV_INSTALLATION_DETAIL, query = "select (case when (date(nv.modificationTime)>=date(:previousDate) and date(nv.modificationTime)<=date(:currentDate)) then 'Installed' else 'Not Installed' end) from NVDeviceData nv where upper(nv.deviceInfo.deviceId)=:deviceId")

})

@FilterDefs({
		@FilterDef(name = DeviceConstant.NVDEVICE_OPERATOR_FILTER, parameters = {
				@ParamDef(name = "operator", type = "java.lang.String") }),

		@FilterDef(name = DeviceConstant.GEOGRAPHYL1_FILTER, parameters = {
				@ParamDef(name = "geographyId", type = "java.lang.Integer") }),

		@FilterDef(name = DeviceConstant.DEVICE_ID_FILTER, parameters = {
				@ParamDef(name = "deviceId", type = "java.lang.String") }),

		@FilterDef(name = DeviceConstant.GEOGRAPHYL2_FILTER, parameters = {
				@ParamDef(name = "geographyId", type = "java.lang.Integer") }),

		@FilterDef(name = DeviceConstant.GEOGRAPHYL3_FILTER, parameters = {
				@ParamDef(name = "geographyId", type = "java.lang.Integer") }),

		@FilterDef(name = DeviceConstant.GEOGRAPHYL4_FILTER, parameters = {
				@ParamDef(name = "geographyId", type = "java.lang.Integer") }),

		@FilterDef(name = DeviceConstant.MAKE_FILTER, parameters = {
				@ParamDef(name = "make", type = "java.lang.String") }),

		@FilterDef(name = DeviceConstant.MODEL_FILTER, parameters = {
				@ParamDef(name = "model", type = "java.lang.String") }),

		@FilterDef(name = DeviceConstant.OS_FILTER, parameters = { @ParamDef(name = "os", type = "java.lang.String") }),

		@FilterDef(name = DeviceConstant.USER_TYPE_FILTER, parameters = {
				@ParamDef(name = "isEnterprise", type = "java.lang.Integer") }),
		
		@FilterDef(name = DeviceConstant.ENTERPRISE_FILTER, parameters = {
				}),
		@FilterDef(name = DeviceConstant.CONSUMER_FILTER, parameters = {
		 }),
		@FilterDef(name = DeviceConstant.DEFAULT_OPERATOR_FILTER, parameters = {
		 }),

})

@Filters(value = {
		@Filter(name = DeviceConstant.NVDEVICE_OPERATOR_FILTER, condition = "operator in (:operator)"),

		@Filter(name = DeviceConstant.DEVICE_ID_FILTER, condition = "nvdeviceid_pk in (select n.nvdeviceid_pk from NVDevice n ,DeviceInfo d "
				+ "where n.deviceinfoid_fk = d.deviceinfoid_pk and d.deviceid in (:deviceId))"),

		@Filter(name = DeviceConstant.GEOGRAPHYL1_FILTER, condition = "nvdeviceid_pk in (select f.nvdeviceid_pk from NVDevice f "
				+ "where f.geographyl1id_fk in (:geographyId))"),

		@Filter(name = DeviceConstant.GEOGRAPHYL2_FILTER, condition = "nvdeviceid_pk in (select f.nvdeviceid_pk from NVDevice f "
				+ "where f.geographyl2id_fk in (:geographyId))"),

		@Filter(name = DeviceConstant.GEOGRAPHYL3_FILTER, condition = "nvdeviceid_pk in (select f.nvdeviceid_pk from NVDevice f "
				+ "where f.geographyl3id_fk in (:geographyId))"),

		@Filter(name = DeviceConstant.GEOGRAPHYL4_FILTER, condition = "nvdeviceid_pk in (select f.nvdeviceid_pk from NVDevice f "
				+ "where f.geographyl4id_fk in (:geographyId))"),

		@Filter(name = DeviceConstant.MAKE_FILTER, condition = "nvdeviceid_pk in (select f.nvdeviceid_pk from NVDevice f "
				+ "where f.make in (:make))"),

		@Filter(name = DeviceConstant.MODEL_FILTER, condition = "nvdeviceid_pk in (select f.nvdeviceid_pk from NVDevice f "
				+ "where f.model in (:model))"),

		@Filter(name = DeviceConstant.OS_FILTER, condition = "nvdeviceid_pk in (select f.nvdeviceid_pk from NVDevice f "
				+ "where f.os in (:os))"),
		@Filter(name = DeviceConstant.USER_TYPE_FILTER, condition = "nvdeviceid_pk in (select f.nvdeviceid_pk from NVDevice f "
				+ "where f.enterprise in (:isEnterprise))"),
		
        @Filter(name = DeviceConstant.ENTERPRISE_FILTER, condition = "nvdeviceid_pk in (select f.nvdeviceid_pk from NVDevice f "
				+ "join DeviceInfo d on f.deviceinfoid_fk=d.deviceinfoid_pk where d.userid_fk is not null)"),
		@Filter(name = DeviceConstant.CONSUMER_FILTER, condition = "nvdeviceid_pk in (select f.nvdeviceid_pk from NVDevice f "
				+ "join DeviceInfo d on f.deviceinfoid_fk=d.deviceinfoid_pk where d.userid_fk is null)"),
		@Filter(name =DeviceConstant.DEFAULT_OPERATOR_FILTER,condition ="nvdeviceid_pk in (select f.nvdeviceid_pk from NVDevice f ,SystemConfiguration s where s.type='NVProfileData' and s.name='DEFAULT_OPERATOR' and f.operator=s.value)"),

})

@Entity
@Table(name = "NVDevice")
@XmlRootElement(name = "NVDevice")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class NVDeviceData implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "nvdeviceid_pk")
	private Integer id;

	/** The make. */
	@Column(name = "make")
	private String make;

	/** The model. */
	@Column(name = "model")
	private String model;

	/** The model no. */
	@Column(name = "modelno")
	private String modelNo;

	/** The os. */
	@Column(name = "os")
	private String os;

	/** The geography L 1. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl1id_fk", nullable = true)
	private GeographyL1 geographyL1;

	/** The geography L 2. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl2id_fk", nullable = true)
	private GeographyL2 geographyL2;

	/** The geography L 3. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl3id_fk", nullable = true)
	private GeographyL3 geographyL3;

	/** The geography L 4. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl4id_fk", nullable = true)
	private GeographyL4 geographyL4;

	/** The creation time. */
	@Column(name = "creationtime")
	private Date creationTime;

	/** The modification time. */
	@Column(name = "modificationtime")
	private Date modificationTime;

	/** The device info. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deviceinfoid_fk", nullable = false)
	private DeviceInfo deviceInfo;

	/** The nvfcmkey. */
	@Column(name = "nvfcmkey")
	private String nvfcmkey;

	/** The sdkfcmkey. */
	@Column(name = "sdkfcmkey")
	private String sdkfcmkey;

	/** The sourcedate. */
	@Column(name = "sourcedate")
	private Date sourcedate;

	/** The module. */
	@Column(name = "module")
	private String module;

	/** The latitude. */
	@Column(name = "latitude")
	private Double latitude;

	/** The longitude. */
	@Column(name = "longitude")
	private Double longitude;

	/** The operator. */
	@Column(name = "operator")
	private String operator;

	/** The imsi. */
	@Column(name = "imsi")
	private String imsi;

	/** The imei. */
	@Column(name = "imei")
	private String imei;

	/** The app version. */
	@Column(name = "appversion")
	private String appVersion;

	/** The app version. */
	@Column(name = "woendtime")
	private Date woEndTime;

	/** The validity. */
	@Column(name = "source")
	private String source;

	/** The validity. */
	@Column(name = "enterprise")
	private Boolean isEnterprise = false;

	/** The battery level. */
	@Column(name = "batterylevel")
	private Double batteryLevel;

	/** The msisdn. */
	@Column(name = "msisdn")
	private String msisdn;

	/** The type. */
	@Column(name = "devicetype")
	private String type;

	/** The location type. */
	@Column(name = "locationtype")
	private String locationType;

	/** The location info. */
	@Column(name = "locationinfo")
	private String locationInfo;

	/** The exec time. */
	@Column(name = "exectime")
	private Date execTime;

	/** The lastrechargedate. */
	@Column(name = "lastrechargedate")
	private Date lastRechargeDate;

	/** The rechargeplan. */
	@Column(name = "rechargeplan")
	private String rechargePlan;

	/** The validity. */
	@Column(name = "validity")
	private String validity;

	/** The nextrechargedate. */
	@Column(name = "nextrechargedate")
	private Date nextRechargeDate;

	/** The device group. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nvdevicegroupid_fk", nullable = true)
	private NVDeviceGroup deviceGroup;

	/** The generic workorder. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "genericworkorderid_fk", nullable = true)
	private GenericWorkorder genericWorkorder;
	

	@Column(name = "rsrp")
	private Double rsrp;
	@Column(name = "rsrq")
	private Double rsrq;
	@Column(name = "sinr")
	private Double sinr;
	@Column(name = "rssi")
	private Double rssi;
	@Column(name = "cgi")
	private Integer cgi;
	

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the make.
	 *
	 * @return the make
	 */
	public String getMake() {
		return make;
	}

	/**
	 * Sets the make.
	 *
	 * @param make
	 *            the new make
	 */
	public void setMake(String make) {
		this.make = make;
	}

	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Sets the model.
	 *
	 * @param model
	 *            the new model
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * Gets the model no.
	 *
	 * @return the model no
	 */
	public String getModelNo() {
		return modelNo;
	}

	/**
	 * Sets the model no.
	 *
	 * @param modelNo
	 *            the new model no
	 */
	public void setModelNo(String modelNo) {
		this.modelNo = modelNo;
	}

	/**
	 * Gets the os.
	 *
	 * @return the os
	 */
	public String getOs() {
		return os;
	}

	/**
	 * Sets the os.
	 *
	 * @param os
	 *            the new os
	 */
	public void setOs(String os) {
		this.os = os;
	}

	/**
	 * Gets the geography L 1.
	 *
	 * @return the geography L 1
	 */
	public GeographyL1 getGeographyL1() {
		return geographyL1;
	}

	/**
	 * Sets the geography L 1.
	 *
	 * @param geographyL1
	 *            the new geography L 1
	 */
	public void setGeographyL1(GeographyL1 geographyL1) {
		this.geographyL1 = geographyL1;
	}

	/**
	 * Gets the geography L 2.
	 *
	 * @return the geography L 2
	 */
	public GeographyL2 getGeographyL2() {
		return geographyL2;
	}

	/**
	 * Sets the geography L 2.
	 *
	 * @param geographyL2
	 *            the new geography L 2
	 */
	public void setGeographyL2(GeographyL2 geographyL2) {
		this.geographyL2 = geographyL2;
	}

	/**
	 * Gets the geography L 3.
	 *
	 * @return the geography L 3
	 */
	public GeographyL3 getGeographyL3() {
		return geographyL3;
	}

	/**
	 * Sets the geography L 3.
	 *
	 * @param geographyL3
	 *            the new geography L 3
	 */
	public void setGeographyL3(GeographyL3 geographyL3) {
		this.geographyL3 = geographyL3;
	}

	/**
	 * Gets the geography L 4.
	 *
	 * @return the geography L 4
	 */
	public GeographyL4 getGeographyL4() {
		return geographyL4;
	}

	/**
	 * Sets the geography L 4.
	 *
	 * @param geographyL4
	 *            the new geography L 4
	 */
	public void setGeographyL4(GeographyL4 geographyL4) {
		this.geographyL4 = geographyL4;
	}

	/**
	 * Gets the creation time.
	 *
	 * @return the creation time
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	/**
	 * Sets the creation time.
	 *
	 * @param creationTime
	 *            the new creation time
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * Gets the modification time.
	 *
	 * @return the modification time
	 */
	public Date getModificationTime() {
		return modificationTime;
	}

	/**
	 * Sets the modification time.
	 *
	 * @param modificationTime
	 *            the new modification time
	 */
	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	/**
	 * Gets the device info.
	 *
	 * @return the device info
	 */
	public DeviceInfo getDeviceInfo() {
		return deviceInfo;
	}

	/**
	 * Sets the device info.
	 *
	 * @param deviceInfo
	 *            the new device info
	 */
	public void setDeviceInfo(DeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	/**
	 * Gets the nvfcmkey.
	 *
	 * @return the nvfcmkey
	 */
	public String getNvfcmkey() {
		return nvfcmkey;
	}

	/**
	 * Sets the nvfcmkey.
	 *
	 * @param nvfcmkey
	 *            the new nvfcmkey
	 */
	public void setNvfcmkey(String nvfcmkey) {
		this.nvfcmkey = nvfcmkey;
	}

	/**
	 * Gets the sdkfcmkey.
	 *
	 * @return the sdkfcmkey
	 */
	public String getSdkfcmkey() {
		return sdkfcmkey;
	}

	/**
	 * Sets the sdkfcmkey.
	 *
	 * @param sdkfcmkey
	 *            the new sdkfcmkey
	 */
	public void setSdkfcmkey(String sdkfcmkey) {
		this.sdkfcmkey = sdkfcmkey;
	}

	/**
	 * Gets the sourcedate.
	 *
	 * @return the sourcedate
	 */
	public Date getSourcedate() {
		return sourcedate;
	}

	/**
	 * Sets the sourcedate.
	 *
	 * @param sourcedate
	 *            the new sourcedate
	 */
	public void setSourcedate(Date sourcedate) {
		this.sourcedate = sourcedate;
	}

	/**
	 * Gets the module.
	 *
	 * @return the module
	 */
	public String getModule() {
		return module;
	}

	/**
	 * Sets the module.
	 *
	 * @param module
	 *            the module to set
	 */
	public void setModule(String module) {
		this.module = module;
	}

	/**
	 * Gets the latitude.
	 *
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * Sets the latitude.
	 *
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * Gets the longitude.
	 *
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * Sets the longitude.
	 *
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * Gets the operator.
	 *
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * Sets the operator.
	 *
	 * @param operator
	 *            the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * Gets the imsi.
	 *
	 * @return the imsi
	 */
	public String getImsi() {
		return imsi;
	}

	/**
	 * Sets the imsi.
	 *
	 * @param imsi
	 *            the imsi to set
	 */
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	/**
	 * Gets the imei.
	 *
	 * @return the imei
	 */
	public String getImei() {
		return imei;
	}

	/**
	 * Sets the imei.
	 *
	 * @param imei
	 *            the imei to set
	 */
	public void setImei(String imei) {
		this.imei = imei;
	}

	/**
	 * Gets the app version.
	 *
	 * @return the appVersion
	 */
	public String getAppVersion() {
		return appVersion;
	}

	/**
	 * Sets the app version.
	 *
	 * @param appVersion
	 *            the appVersion to set
	 */
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	/**
	 * Gets the serialversionuid.
	 *
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * Gets the wo end time.
	 *
	 * @return the wo end time
	 */
	public Date getWoEndTime() {
		return woEndTime;
	}

	/**
	 * Sets the wo end time.
	 *
	 * @param woEndTime
	 *            the new wo end time
	 */
	public void setWoEndTime(Date woEndTime) {
		this.woEndTime = woEndTime;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the isEnterprise
	 */
	public Boolean getIsEnterprise() {
		return isEnterprise;
	}

	/**
	 * @param isEnterprise
	 *            the isEnterprise to set
	 */
	public void setIsEnterprise(Boolean isEnterprise) {
		this.isEnterprise = isEnterprise;
	}

	/**
	 * Gets the battery level.
	 *
	 * @return the battery level
	 */
	public Double getBatteryLevel() {
		return batteryLevel;
	}

	/**
	 * Sets the battery level.
	 *
	 * @param batteryLevel
	 *            the new battery level
	 */
	public void setBatteryLevel(Double batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

	/**
	 * Gets the msisdn.
	 *
	 * @return the msisdn
	 */
	public String getMsisdn() {
		return msisdn;
	}

	/**
	 * Sets the msisdn.
	 *
	 * @param msisdn
	 *            the new msisdn
	 */
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type
	 *            the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the location type.
	 *
	 * @return the location type
	 */
	public String getLocationType() {
		return locationType;
	}

	/**
	 * Sets the location type.
	 *
	 * @param locationType
	 *            the new location type
	 */
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	/**
	 * Gets the location info.
	 *
	 * @return the location info
	 */
	public String getLocationInfo() {
		return locationInfo;
	}

	/**
	 * Sets the location info.
	 *
	 * @param locationInfo
	 *            the new location info
	 */
	public void setLocationInfo(String locationInfo) {
		this.locationInfo = locationInfo;
	}

	/**
	 * Gets the exec time.
	 *
	 * @return the exec time
	 */
	public Date getExecTime() {
		return execTime;
	}

	/**
	 * Sets the exec time.
	 *
	 * @param execTime
	 *            the new exec time
	 */
	public void setExecTime(Date execTime) {
		this.execTime = execTime;
	}

	/**
	 * Gets the device group.
	 *
	 * @return the device group
	 */
	public NVDeviceGroup getDeviceGroup() {
		return deviceGroup;
	}

	/**
	 * Sets the device group.
	 *
	 * @param deviceGroup
	 *            the new device group
	 */
	public void setDeviceGroup(NVDeviceGroup deviceGroup) {
		this.deviceGroup = deviceGroup;
	}

	/**
	 * Gets the lastrechargedate.
	 *
	 * @return the lastrechargedate
	 */
	public Date getLastRechargeDate() {
		return lastRechargeDate;
	}

	/**
	 * Sets the lastrechargedate.
	 *
	 * @param lastrechargedate
	 *            the new lastrechargedate
	 */
	public void setLastRechargeDate(Date lastrechargedate) {
		this.lastRechargeDate = lastrechargedate;
	}

	/**
	 * Gets the rechargeplan.
	 *
	 * @return the rechargeplan
	 */
	public String getRechargePlan() {
		return rechargePlan;
	}

	/**
	 * Sets the rechargeplan.
	 *
	 * @param rechargeplan
	 *            the new rechargeplan
	 */
	public void setRechargePlan(String rechargeplan) {
		this.rechargePlan = rechargeplan;
	}

	/**
	 * Gets the validity.
	 *
	 * @return the validity
	 */
	public String getValidity() {
		return validity;
	}

	/**
	 * Sets the validity.
	 *
	 * @param validity
	 *            the new validity
	 */
	public void setValidity(String validity) {
		this.validity = validity;
	}

	/**
	 * Gets the nextrechargedate.
	 *
	 * @return the nextrechargedate
	 */
	public Date getNextRechargeDate() {
		return nextRechargeDate;
	}

	/**
	 * Sets the nextrechargedate.
	 *
	 * @param nextrechargedate
	 *            the new nextrechargedate
	 */
	public void setNextRechargeDate(Date nextrechargedate) {
		this.nextRechargeDate = nextrechargedate;
	}

	public GenericWorkorder getGenericWorkorder() {
		return genericWorkorder;
	}

	public void setGenericWorkorder(GenericWorkorder genericWorkorder) {
		this.genericWorkorder = genericWorkorder;
	}

	/**
	 * @return the rsrp
	 */
	public Double getRsrp() {
		return rsrp;
	}

	/**
	 * @param rsrp
	 *            the rsrp to set
	 */
	public void setRsrp(Double rsrp) {
		this.rsrp = rsrp;
	}

	/**
	 * @return the rsrq
	 */
	public Double getRsrq() {
		return rsrq;
	}

	/**
	 * @param rsrq
	 *            the rsrq to set
	 */
	public void setRsrq(Double rsrq) {
		this.rsrq = rsrq;
	}

	/**
	 * @return the sinr
	 */
	public Double getSinr() {
		return sinr;
	}

	/**
	 * @param sinr
	 *            the sinr to set
	 */
	public void setSinr(Double sinr) {
		this.sinr = sinr;
	}

	/**
	 * @return the rssi
	 */
	public Double getRssi() {
		return rssi;
	}

	/**
	 * @param rssi
	 *            the rssi to set
	 */
	public void setRssi(Double rssi) {
		this.rssi = rssi;
	}

	/**
	 * @return the cgi
	 */
	public Integer getCgi() {
		return cgi;
	}

	/**
	 * @param cgi the cgi to set
	 */
	public void setCgi(Integer cgi) {
		this.cgi = cgi;
	}

	@Override
	public String toString() {
		return "NVDeviceData [id=" + id + ", make=" + make + ", model=" + model + ", modelNo=" + modelNo + ", os=" + os
				+ ", geographyL1=" + geographyL1 + ", geographyL2=" + geographyL2 + ", geographyL3=" + geographyL3
				+ ", geographyL4=" + geographyL4 + ", creationTime=" + creationTime + ", modificationTime="
				+ modificationTime + ", deviceInfo=" + deviceInfo + ", nvfcmkey=" + nvfcmkey + ", sdkfcmkey="
				+ sdkfcmkey + ", sourcedate=" + sourcedate + ", module=" + module + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", operator=" + operator + ", imsi=" + imsi + ", imei=" + imei
				+ ", appVersion=" + appVersion + ", woEndTime=" + woEndTime + ", source=" + source + ", isEnterprise="
				+ isEnterprise + ", batteryLevel=" + batteryLevel + ", msisdn=" + msisdn + ", type=" + type
				+ ", locationType=" + locationType + ", locationInfo=" + locationInfo + ", execTime=" + execTime
				+ ", lastRechargeDate=" + lastRechargeDate + ", rechargePlan=" + rechargePlan + ", validity=" + validity
				+ ", nextRechargeDate=" + nextRechargeDate + ", deviceGroup=" + deviceGroup + ", genericWorkorder="
				+ genericWorkorder + ", rsrp=" + rsrp + ", rsrq=" + rsrq + ", sinr=" + sinr + ", rssi=" + rssi
				+ ", cgi=" + cgi + "]";
	}

	
}

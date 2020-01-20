package com.inn.foresight.module.nv.workorder.stealth.kpi.model;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.module.nv.device.model.NVDeviceData;
import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants;
import com.inn.foresight.module.nv.workorder.stealth.model.StealthTaskResult;

/**
 * The Class DriveTestKPI.
 *
 * @author innoeye date - 24-Apr-2018 1:19:48 PM
 */
@NamedQueries({
		@NamedQuery(name = StealthConstants.GET_DEVICE_GROUP_COUNT_GROUP_BY_GEOL1, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceGroupWrapper(count(distinct n.nvDevice.deviceGroup),n.nvDevice.deviceGroup.geographyL4.geographyL3.geographyL2.geographyL1.name,n.nvDevice.deviceGroup.geographyL4.geographyL3.geographyL2.geographyL1.latitude,n.nvDevice.deviceGroup.geographyL4.geographyL3.geographyL2.geographyL1.longitude,n.nvDevice.deviceGroup.geographyL4.geographyL3.geographyL2.geographyL1.id) from DriveTestKPI n  where n.wptProcessStatus = 'PROCESSED' AND Date(n.modificationTime) >= Date(:startDate) AND Date(n.modificationTime) <= Date(:endDate) AND n.nvDevice.deviceGroup.geographyL4.geographyL3.geographyL2.geographyL1.id in (select g.id from GeographyL1 g where g.latitude>=:SWLat and g.latitude<=:NELat and g.longitude>=:SWLng and g.longitude<=:NELng) group by n.nvDevice.deviceGroup.geographyL4.geographyL3.geographyL2.geographyL1.name,n.nvDevice.deviceGroup.geographyL4.geographyL3.geographyL2.geographyL1.latitude,n.nvDevice.deviceGroup.geographyL4.geographyL3.geographyL2.geographyL1.longitude,n.nvDevice.deviceGroup.geographyL4.geographyL3.geographyL2.geographyL1.id"),
		@NamedQuery(name = StealthConstants.GET_DEVICE_GROUP_COUNT_GROUP_BY_GEOL2, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceGroupWrapper(count(distinct n.nvDevice.deviceGroup),n.nvDevice.deviceGroup.geographyL4.geographyL3.geographyL2.name,n.nvDevice.deviceGroup.geographyL4.geographyL3.geographyL2.latitude,n.nvDevice.deviceGroup.geographyL4.geographyL3.geographyL2.longitude,n.nvDevice.deviceGroup.geographyL4.geographyL3.geographyL2.id) from DriveTestKPI n where n.wptProcessStatus = 'PROCESSED' AND Date(n.modificationTime) >= Date(:startDate) AND Date(n.modificationTime) <= Date(:endDate) AND n.nvDevice.deviceGroup.geographyL4.geographyL3.geographyL2.id in (select g.id from GeographyL2 g where g.latitude>=:SWLat and g.latitude<=:NELat and g.longitude>=:SWLng and g.longitude<=:NELng) group by n.nvDevice.deviceGroup.geographyL4.geographyL3.geographyL2.name,n.nvDevice.deviceGroup.geographyL4.geographyL3.geographyL2.latitude,n.nvDevice.deviceGroup.geographyL4.geographyL3.geographyL2.longitude,n.nvDevice.deviceGroup.geographyL4.geographyL3.geographyL2.id"),
		@NamedQuery(name = StealthConstants.GET_DEVICE_GROUP_COUNT_GROUP_BY_GEOL3, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceGroupWrapper(count(distinct n.nvDevice.deviceGroup),n.nvDevice.deviceGroup.geographyL4.geographyL3.name,n.nvDevice.deviceGroup.geographyL4.geographyL3.latitude,n.nvDevice.deviceGroup.geographyL4.geographyL3.longitude,n.nvDevice.deviceGroup.geographyL4.geographyL3.id) from DriveTestKPI n where n.wptProcessStatus = 'PROCESSED' AND Date(n.modificationTime) >= Date(:startDate) AND Date(n.modificationTime) <= Date(:endDate) AND n.nvDevice.deviceGroup.geographyL4.geographyL3.id in (select g.id from GeographyL3 g where g.latitude>=:SWLat and g.latitude<=:NELat and g.longitude>=:SWLng and g.longitude<=:NELng) group by n.nvDevice.deviceGroup.geographyL4.geographyL3.name,n.nvDevice.deviceGroup.geographyL4.geographyL3.latitude,n.nvDevice.deviceGroup.geographyL4.geographyL3.longitude,n.nvDevice.deviceGroup.geographyL4.geographyL3.id"),
		@NamedQuery(name = StealthConstants.GET_DEVICE_GROUP_COUNT_GROUP_BY_GEOL4, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceGroupWrapper(count(distinct n.nvDevice.deviceGroup),n.nvDevice.deviceGroup.geographyL4.name,n.nvDevice.deviceGroup.geographyL4.latitude,n.nvDevice.deviceGroup.geographyL4.longitude,n.nvDevice.deviceGroup.geographyL4.id) from DriveTestKPI n where n.wptProcessStatus = 'PROCESSED' AND Date(n.modificationTime) >= Date(:startDate) AND Date(n.modificationTime) <= Date(:endDate) AND n.nvDevice.deviceGroup.geographyL4.id in (select g.id from GeographyL4 g where g.latitude>=:SWLat and g.latitude<=:NELat and g.longitude>=:SWLng and g.longitude<=:NELng) group by n.nvDevice.deviceGroup.geographyL4.name,n.nvDevice.deviceGroup.geographyL4.latitude,n.nvDevice.deviceGroup.geographyL4.longitude,n.nvDevice.deviceGroup.geographyL4.id"),
		@NamedQuery(name = StealthConstants.GET_DEVICE_GROUP_BY_VIEWPORT, query = "select distinct new com.inn.foresight.module.nv.device.wrapper.NVDeviceGroupWrapper(n.nvDevice.deviceGroup.id, n.nvDevice.deviceGroup.groupId, n.nvDevice.deviceGroup.name, n.nvDevice.deviceGroup.locationType,n.nvDevice.deviceGroup.address,n.nvDevice.deviceGroup.latitude,n.nvDevice.deviceGroup.longitude) from DriveTestKPI n where n.wptProcessStatus = 'PROCESSED' AND Date(n.modificationTime) >= Date(:startDate) AND Date(n.modificationTime) <= Date(:endDate) AND n.nvDevice.deviceGroup.latitude>=:SWLat and n.nvDevice.deviceGroup.latitude<=:NELat and n.nvDevice.deviceGroup.longitude>=:SWLng and n.nvDevice.deviceGroup.longitude<=:NELng"),

		@NamedQuery(name = StealthConstants.GET_DEVICE_DATA_BETWEEN_STARTDATE_AND_ENDDATE, query = "select distinct new com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper(sum(n.totalTTL)/sum(n.countTTL), sum(n.totalTTFB)/sum(n.countTTFB), sum(n.totalTDNS)/sum(n.countTDNS), sum(n.totalFDNS)/sum(n.countFDNS), n.nvDevice.make, n.nvDevice.model, n.nvDevice.imei, n.nvDevice.geographyL4.geographyL3.name, n.nvDevice.geographyL4.name, n.nvDevice.latitude, n.nvDevice.longitude, n.nvDevice.operator, n.nvDevice.deviceGroup.address, n.nvDevice.deviceGroup.groupId,n.nvDevice.deviceInfo.deviceId) from DriveTestKPI n where n.wptProcessStatus = 'PROCESSED' AND Date(n.modificationTime) >= Date(:startDate) AND Date(n.modificationTime) <= Date(:endDate) group by n.nvDevice.make, n.nvDevice.model, n.nvDevice.imei, n.nvDevice.geographyL4.geographyL3.name, n.nvDevice.geographyL4.name, n.nvDevice.latitude, n.nvDevice.longitude, n.nvDevice.operator, n.nvDevice.deviceGroup.address, n.nvDevice.deviceGroup.groupId,n.nvDevice.deviceInfo.deviceId"),
		@NamedQuery(name = StealthConstants.GET_FAILURE_DEVICE_DATA_BETWEEN_STARTDATE_AND_ENDDATE, query = "select distinct new com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper(n.nvDevice.make,n.nvDevice.model,n.nvDevice.imei,n.nvDevice.geographyL4.geographyL3.name,n.nvDevice.geographyL4.name,n.nvDevice.latitude,n.nvDevice.longitude,n.nvDevice.deviceGroup.address,n.nvDevice.deviceGroup.groupId,n.nvDevice.deviceInfo.deviceId) from DriveTestKPI n where n.wptProcessStatus = 'PROCESSED' AND n.taskResult.status = 'FAILURE' AND Date(n.modificationTime) >= Date(:startDate) AND Date(n.modificationTime) <= Date(:endDate) group by n.taskResult.remark,n.nvDevice.make,n.nvDevice.model,n.nvDevice.imei,n.nvDevice.geographyL4.geographyL3.name,n.nvDevice.geographyL4.name,n.nvDevice.latitude,n.nvDevice.longitude,n.nvDevice.deviceGroup.address,n.nvDevice.deviceGroup.groupId,n.nvDevice.deviceInfo.deviceId"),
		@NamedQuery(name = StealthConstants.GET_FAILURE_DEVICE_REMARK, query = "select distinct new com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper(n.taskResult.remark,n.startTime) from DriveTestKPI n where n.wptProcessStatus = 'PROCESSED' AND n.taskResult.status = 'FAILURE' AND Date(n.modificationTime) >= Date(:startDate) AND Date(n.modificationTime) <= Date(:endDate) AND n.nvDevice.deviceInfo.deviceId = :id order by n.startTime desc"),

		@NamedQuery(name = StealthConstants.GET_DEVICE_DATA_FOR_DAYWISE, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper(n.id, n.startTime,n.endTime,n.taskResult.operator,n.taskResult.technology,(n.totalTTL)/(countTTL),(n.totalTTFB)/(n.countTTFB),(n.totalFDNS)/(n.countFDNS),(n.totalTDNS)/(n.countTDNS)) from DriveTestKPI n where n.wptProcessStatus = 'PROCESSED' AND n.nvDevice.deviceInfo.deviceId = :id and Date(n.modificationTime) >= Date(:startDate)  AND Date(n.modificationTime) <= Date(:endDate)"),
		@NamedQuery(name = StealthConstants.GET_FAILURE_DEVICE_DATA_FOR_DAYWISE, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper(n.id, n.startTime,n.endTime,n.taskResult.operator,n.taskResult.technology,n.taskResult.totalIteration,n.taskResult.completedIteration,n.taskResult.remark) from DriveTestKPI n where n.wptProcessStatus = 'PROCESSED' AND n.taskResult.status = 'FAILURE' AND n.nvDevice.deviceInfo.deviceId = :id and Date(n.modificationTime) >= Date(:startDate)  AND Date(n.modificationTime) <= Date(:endDate)"),

		@NamedQuery(name = StealthConstants.GET_NON_RESPONDING_DEVICES, query = "SELECT n from NVDeviceData n,GenericWorkorder g where n.genericWorkorder=g.id AND g.status='NOT_STARTED' AND g.templateType = 'NV_STEALTH' AND NOW() BETWEEN g.startDate AND g.dueDate and n.id NOT IN (SELECT DISTINCT dt.nvDevice.id from DriveTestKPI dt where CURDATE() BETWEEN DATE(dt.startTime) AND DATE(dt.endTime))"),

		@NamedQuery(name = StealthConstants.GET_DEVICE_DATA_COUNT_BETWEEN_STARTDATE_AND_ENDDATE, query = "select count(distinct n.nvDevice.deviceInfo.deviceId) from DriveTestKPI n where n.wptProcessStatus = 'PROCESSED' AND Date(n.modificationTime) >= Date(:startDate) AND Date(n.modificationTime) <= Date(:endDate) AND n.nvDevice.deviceGroup is not null AND n.nvDevice.geographyL4 is not null"),
		@NamedQuery(name = StealthConstants.GET_FAILURE_DEVICE_DATA_COUNT_BETWEEN_STARTDATE_AND_ENDDATE, query = "select count(distinct n.nvDevice.deviceInfo.deviceId) from DriveTestKPI n where n.wptProcessStatus = 'PROCESSED' AND n.taskResult.status = 'FAILURE' AND Date(n.modificationTime) >= Date(:startDate) AND Date(n.modificationTime) <= Date(:endDate) AND n.nvDevice.deviceGroup is not null AND n.nvDevice.geographyL4 is not null"),

		@NamedQuery(name = StealthConstants.GET_DEVICE_DATA_COUNT_DAYWISE, query = "select count(distinct n.id) from DriveTestKPI n where n.wptProcessStatus = 'PROCESSED' AND n.nvDevice.deviceInfo.deviceId = :id and Date(n.modificationTime) >= Date(:startDate)  AND Date(n.modificationTime) <= Date(:endDate)"),
		@NamedQuery(name = StealthConstants.GET_FAILURE_DEVICE_DATA_COUNT_DAYWISE, query = "select count(distinct n.id) from DriveTestKPI n where n.wptProcessStatus = 'PROCESSED' AND n.taskResult.status = 'FAILURE' AND n.nvDevice.deviceInfo.deviceId = :id and Date(n.modificationTime) >= Date(:startDate)  AND Date(n.modificationTime) <= Date(:endDate)"),
		
		@NamedQuery(name = StealthConstants.GET_CEMS_DATA_BY_STEALTH_TASK_RESULT, query = "select cemsData from DriveTestKPI n where n.taskResult.stealthTaskDetail.id = :taskId AND Date(n.taskResult.startTime) = Date(:startDate)"),
})

@FilterDefs({
		@FilterDef(name = StealthConstants.TTFB_FILTER, parameters = {
				@ParamDef(name = "startDate", type = "java.lang.String"),
				@ParamDef(name = "endDate", type = "java.lang.String"),
				@ParamDef(name = "threshold", type = "java.lang.Double") }),
		@FilterDef(name = StealthConstants.TTL_FILTER, parameters = {
				@ParamDef(name = "startDate", type = "java.lang.String"),
				@ParamDef(name = "endDate", type = "java.lang.String"),
				@ParamDef(name = "threshold", type = "java.lang.Double") }),
		@FilterDef(name = StealthConstants.TDNS_FILTER, parameters = {
				@ParamDef(name = "startDate", type = "java.lang.String"),
				@ParamDef(name = "endDate", type = "java.lang.String"),
				@ParamDef(name = "threshold", type = "java.lang.Double") }),
		@FilterDef(name = StealthConstants.FDNS_FILTER, parameters = {
				@ParamDef(name = "startDate", type = "java.lang.String"),
				@ParamDef(name = "endDate", type = "java.lang.String"),
				@ParamDef(name = "threshold", type = "java.lang.Double") }),
		@FilterDef(name = StealthConstants.FAILURE_FILTER, parameters = {
				@ParamDef(name = "startDate", type = "java.lang.String"),
				@ParamDef(name = "endDate", type = "java.lang.String"),
				@ParamDef(name = "threshold", type = "java.lang.Double") }),
		@FilterDef(name = StealthConstants.TTFB_DEVICE_WISE_BAD_FILTER, parameters = {
				@ParamDef(name = "startDate", type = "java.lang.String"),
				@ParamDef(name = "endDate", type = "java.lang.String"),
				@ParamDef(name = "threshold", type = "java.lang.Double") }),
		@FilterDef(name = StealthConstants.TTL_DEVICE_WISE_BAD_FILTER, parameters = {
				@ParamDef(name = "startDate", type = "java.lang.String"),
				@ParamDef(name = "endDate", type = "java.lang.String"),
				@ParamDef(name = "threshold", type = "java.lang.Double") }),
		@FilterDef(name = StealthConstants.TDNS_DEVICE_WISE_BAD_FILTER, parameters = {
				@ParamDef(name = "startDate", type = "java.lang.String"),
				@ParamDef(name = "endDate", type = "java.lang.String"),
				@ParamDef(name = "threshold", type = "java.lang.Double") }),
		@FilterDef(name = StealthConstants.FDNS_DEVICE_WISE_BAD_FILTER, parameters = {
				@ParamDef(name = "startDate", type = "java.lang.String"),
				@ParamDef(name = "endDate", type = "java.lang.String"),
				@ParamDef(name = "threshold", type = "java.lang.Double") }),
		@FilterDef(name = StealthConstants.TTFB_DEVICE_WISE_GOOD_FILTER, parameters = {
				@ParamDef(name = "startDate", type = "java.lang.String"),
				@ParamDef(name = "endDate", type = "java.lang.String"),
				@ParamDef(name = "threshold", type = "java.lang.Double") }),
		@FilterDef(name = StealthConstants.TTL_DEVICE_WISE_GOOD_FILTER, parameters = {
				@ParamDef(name = "startDate", type = "java.lang.String"),
				@ParamDef(name = "endDate", type = "java.lang.String"),
				@ParamDef(name = "threshold", type = "java.lang.Double") }),
		@FilterDef(name = StealthConstants.TDNS_DEVICE_WISE_GOOD_FILTER, parameters = {
				@ParamDef(name = "startDate", type = "java.lang.String"),
				@ParamDef(name = "endDate", type = "java.lang.String"),
				@ParamDef(name = "threshold", type = "java.lang.Double") }),
		@FilterDef(name = StealthConstants.FDNS_DEVICE_WISE_GOOD_FILTER, parameters = {
				@ParamDef(name = "startDate", type = "java.lang.String"),
				@ParamDef(name = "endDate", type = "java.lang.String"),
				@ParamDef(name = "threshold", type = "java.lang.Double") }),
		@FilterDef(name = StealthConstants.TTFB_DAY_WISE_BAD_FILTER, parameters = {
				@ParamDef(name = "startDate", type = "java.lang.String"),
				@ParamDef(name = "endDate", type = "java.lang.String"),
				@ParamDef(name = "threshold", type = "java.lang.Double") }),
		@FilterDef(name = StealthConstants.TTL_DAY_WISE_BAD_FILTER, parameters = {
				@ParamDef(name = "startDate", type = "java.lang.String"),
				@ParamDef(name = "endDate", type = "java.lang.String"),
				@ParamDef(name = "threshold", type = "java.lang.Double") }),
		@FilterDef(name = StealthConstants.TDNS_DAY_WISE_BAD_FILTER, parameters = {
				@ParamDef(name = "startDate", type = "java.lang.String"),
				@ParamDef(name = "endDate", type = "java.lang.String"),
				@ParamDef(name = "threshold", type = "java.lang.Double") }),
		@FilterDef(name = StealthConstants.FDNS_DAY_WISE_BAD_FILTER, parameters = {
				@ParamDef(name = "startDate", type = "java.lang.String"),
				@ParamDef(name = "endDate", type = "java.lang.String"),
				@ParamDef(name = "threshold", type = "java.lang.Double") }),
		@FilterDef(name = StealthConstants.TTFB_DAY_WISE_GOOD_FILTER, parameters = {
				@ParamDef(name = "startDate", type = "java.lang.String"),
				@ParamDef(name = "endDate", type = "java.lang.String"),
				@ParamDef(name = "threshold", type = "java.lang.Double") }),
		@FilterDef(name = StealthConstants.TTL_DAY_WISE_GOOD_FILTER, parameters = {
				@ParamDef(name = "startDate", type = "java.lang.String"),
				@ParamDef(name = "endDate", type = "java.lang.String"),
				@ParamDef(name = "threshold", type = "java.lang.Double") }),
		@FilterDef(name = StealthConstants.TDNS_DAY_WISE_GOOD_FILTER, parameters = {
				@ParamDef(name = "startDate", type = "java.lang.String"),
				@ParamDef(name = "endDate", type = "java.lang.String"),
				@ParamDef(name = "threshold", type = "java.lang.Double") }),
		@FilterDef(name = StealthConstants.FDNS_DAY_WISE_GOOD_FILTER, parameters = {
				@ParamDef(name = "startDate", type = "java.lang.String"),
				@ParamDef(name = "endDate", type = "java.lang.String"),
				@ParamDef(name = "threshold", type = "java.lang.Double") }), })

@Filters(value = {

		@Filter(name = StealthConstants.TTFB_FILTER, condition = "nvdeviceid_fk in (select distinct nd.nvdeviceid_pk from NVDevice nd where nd.nvdevicegroupid_fk in "
				+ "(select d.nvdevicegroupid_fk from DriveTestKPI n ,NVDevice d where n.nvdeviceid_fk = d.nvdeviceid_pk and Date(n.modificationtime) >= date :startDate AND Date(n.modificationtime) <= date :endDate group by d.nvdevicegroupid_fk having sum(n.totalttfb)/sum(n.countttfb)  > :threshold))"),
		@Filter(name = StealthConstants.TTL_FILTER, condition = "nvdeviceid_fk in (select distinct nd.nvdeviceid_pk from NVDevice nd where nd.nvdevicegroupid_fk in "
				+ "(select d.nvdevicegroupid_fk from DriveTestKPI n ,NVDevice d where n.nvdeviceid_fk = d.nvdeviceid_pk and Date(n.modificationtime) >= date :startDate AND Date(n.modificationtime) <= date :endDate group by d.nvdevicegroupid_fk having sum(n.totalttl)/sum(n.countttl)  > :threshold))"),
		@Filter(name = StealthConstants.TDNS_FILTER, condition = "nvdeviceid_fk in (select distinct nd.nvdeviceid_pk from NVDevice nd where nd.nvdevicegroupid_fk in "
				+ "(select d.nvdevicegroupid_fk from DriveTestKPI n ,NVDevice d where n.nvdeviceid_fk = d.nvdeviceid_pk and Date(n.modificationtime) >= date :startDate AND Date(n.modificationtime) <= date :endDate group by d.nvdevicegroupid_fk having sum(n.totaltdns)/sum(n.counttdns)  > :threshold))"),
		@Filter(name = StealthConstants.FDNS_FILTER, condition = "nvdeviceid_fk in (select distinct nd.nvdeviceid_pk from NVDevice nd where nd.nvdevicegroupid_fk in "
				+ "(select d.nvdevicegroupid_fk from DriveTestKPI n ,NVDevice d where n.nvdeviceid_fk = d.nvdeviceid_pk and Date(n.modificationtime) >= date :startDate AND Date(n.modificationtime) <= date :endDate group by d.nvdevicegroupid_fk having sum(n.totalfdns)/sum(n.countfdns)  > :threshold))"),
		@Filter(name = StealthConstants.FAILURE_FILTER, condition = "nvdeviceid_fk in (select distinct nd.nvdeviceid_pk from NVDevice nd where nd.nvdevicegroupid_fk in "
				+ "(select d.nvdevicegroupid_fk from DriveTestKPI n ,NVDevice d, StealthTaskResult tr where tr.status= 'FAILURE' and n.nvdeviceid_fk = d.nvdeviceid_pk and Date(n.modificationtime) >= date :startDate AND Date(n.modificationtime) <= date :endDate group by d.nvdevicegroupid_fk having (sum(tr.completediteration)/sum(tr.totaliteration))  > (:threshold) / 100))"),
		@Filter(name = StealthConstants.TTFB_DEVICE_WISE_BAD_FILTER, condition = "nvdeviceid_fk in (select d.nvdeviceid_pk from DriveTestKPI n ,NVDevice d where n.nvdeviceid_fk = d.nvdeviceid_pk and n.wptProcessStatus = 'PROCESSED' AND Date(n.modificationtime) >= date :startDate AND Date(n.modificationtime) <= date :endDate group by d.nvdeviceid_pk having sum(n.totalttfb)/sum(n.countttfb)  > :threshold)"),
		@Filter(name = StealthConstants.TTL_DEVICE_WISE_BAD_FILTER, condition = "nvdeviceid_fk in (select d.nvdeviceid_pk from DriveTestKPI n ,NVDevice d where n.nvdeviceid_fk = d.nvdeviceid_pk and n.wptProcessStatus = 'PROCESSED' AND Date(n.modificationtime) >= date :startDate AND Date(n.modificationtime) <= date :endDate group by d.nvdeviceid_pk having sum(n.totalttl)/sum(n.countttl)  > :threshold)"),
		@Filter(name = StealthConstants.TDNS_DEVICE_WISE_BAD_FILTER, condition = "nvdeviceid_fk in (select d.nvdeviceid_pk from DriveTestKPI n ,NVDevice d where n.nvdeviceid_fk = d.nvdeviceid_pk and n.wptProcessStatus = 'PROCESSED' AND Date(n.modificationtime) >= date :startDate AND Date(n.modificationtime) <= date :endDate group by d.nvdeviceid_pk having sum(n.totaltdns)/sum(n.counttdns)  > :threshold)"),
		@Filter(name = StealthConstants.FDNS_DEVICE_WISE_BAD_FILTER, condition = "nvdeviceid_fk in (select d.nvdeviceid_pk from DriveTestKPI n ,NVDevice d where n.nvdeviceid_fk = d.nvdeviceid_pk and n.wptProcessStatus = 'PROCESSED' AND Date(n.modificationtime) >= date :startDate AND Date(n.modificationtime) <= date :endDate group by d.nvdeviceid_pk having sum(n.totalfdns)/sum(n.countfdns)  > :threshold)"),
		@Filter(name = StealthConstants.TTFB_DEVICE_WISE_GOOD_FILTER, condition = "nvdeviceid_fk in (select d.nvdeviceid_pk from DriveTestKPI n ,NVDevice d where  and n.nvdeviceid_fk = d.nvdeviceid_pk and n.wptProcessStatus = 'PROCESSED' AND Date(n.modificationtime) >= date :startDate AND Date(n.modificationtime) <= date :endDate AND n.totalttfb IS NOT NULL AND n.countttfb > 0 group by d.nvdeviceid_pk having sum(n.totalttfb)/sum(n.countttfb)  <= :threshold)"),
		@Filter(name = StealthConstants.TTL_DEVICE_WISE_GOOD_FILTER, condition = "nvdeviceid_fk in (select d.nvdeviceid_pk from DriveTestKPI n ,NVDevice d where  and n.nvdeviceid_fk = d.nvdeviceid_pk and n.wptProcessStatus = 'PROCESSED' AND Date(n.modificationtime) >= date :startDate AND Date(n.modificationtime) <= date :endDate AND n.totalttl IS NOT NULL AND n.countttl > 0 group by d.nvdeviceid_pk having sum(n.totalttl)/sum(n.countttl)  <= :threshold)"),
		@Filter(name = StealthConstants.TDNS_DEVICE_WISE_GOOD_FILTER, condition = "nvdeviceid_fk in (select d.nvdeviceid_pk from DriveTestKPI n ,NVDevice d where  and n.nvdeviceid_fk = d.nvdeviceid_pk and n.wptProcessStatus = 'PROCESSED' AND Date(n.modificationtime) >= date :startDate AND Date(n.modificationtime) <= date :endDate AND n.totaltdns IS NOT NULL AND n.counttdns > 0 group by d.nvdeviceid_pk having sum(n.totaltdns)/sum(n.counttdns)  <= :threshold)"),
		@Filter(name = StealthConstants.FDNS_DEVICE_WISE_GOOD_FILTER, condition = "nvdeviceid_fk in (select d.nvdeviceid_pk from DriveTestKPI n ,NVDevice d where  and n.nvdeviceid_fk = d.nvdeviceid_pk and n.wptProcessStatus = 'PROCESSED' AND Date(n.modificationtime) >= date :startDate AND Date(n.modificationtime) <= date :endDate AND n.totalfdns IS NOT NULL AND n.countfdns > 0 group by d.nvdeviceid_pk having sum(n.totalfdns)/sum(n.countfdns)  <= :threshold)"),
		@Filter(name = StealthConstants.TTFB_DAY_WISE_BAD_FILTER, condition = "modificationtime in (select n.modificationtime from DriveTestKPI n ,NVDevice d, where  and n.nvdeviceid_fk = d.nvdeviceid_pk and n.wptProcessStatus = 'PROCESSED' AND Date(n.modificationtime) >= date :startDate AND Date(n.modificationtime) <= date :endDate group by n.modificationtime having sum(n.totalttfb)/sum(n.countttfb)  > :threshold)"),
		@Filter(name = StealthConstants.TTL_DAY_WISE_BAD_FILTER, condition = "modificationtime in (select n.modificationtime from DriveTestKPI n ,NVDevice d, where  and n.nvdeviceid_fk = d.nvdeviceid_pk and n.wptProcessStatus = 'PROCESSED' AND Date(n.modificationtime) >= date :startDate AND Date(n.modificationtime) <= date :endDate group by n.modificationtime having sum(n.totalttl)/sum(n.countttl)  > :threshold)"),
		@Filter(name = StealthConstants.TDNS_DAY_WISE_BAD_FILTER, condition = "modificationtime in (select n.modificationtime from DriveTestKPI n ,NVDevice d, where  and n.nvdeviceid_fk = d.nvdeviceid_pk and n.wptProcessStatus = 'PROCESSED' AND Date(n.modificationtime) >= date :startDate AND Date(n.modificationtime) <= date :endDate group by n.modificationtime having sum(n.totaltdns)/sum(n.counttdns)  > :threshold)"),
		@Filter(name = StealthConstants.FDNS_DAY_WISE_BAD_FILTER, condition = "modificationtime in (select n.modificationtime from DriveTestKPI n ,NVDevice d, where  and n.nvdeviceid_fk = d.nvdeviceid_pk and n.wptProcessStatus = 'PROCESSED' AND Date(n.modificationtime) >= date :startDate AND Date(n.modificationtime) <= date :endDate group by n.modificationtime having sum(n.totalfdns)/sum(n.countfdns) > :threshold)"),
		@Filter(name = StealthConstants.TTFB_DAY_WISE_GOOD_FILTER, condition = "modificationtime in (select n.modificationtime from DriveTestKPI n ,NVDevice d, where and n.nvdeviceid_fk = d.nvdeviceid_pk and n.wptProcessStatus = 'PROCESSED' AND Date(n.modificationtime) >= date :startDate AND Date(n.modificationtime) <= date :endDate AND n.totalttfb IS NOT NULL AND n.countttfb > 0 group by n.modificationtime having sum(n.totalttfb)/sum(n.countttfb) <= :threshold)"),
		@Filter(name = StealthConstants.TTL_DAY_WISE_GOOD_FILTER, condition = "modificationtime in (select n.modificationtime from DriveTestKPI n ,NVDevice d, where and n.nvdeviceid_fk = d.nvdeviceid_pk and n.wptProcessStatus = 'PROCESSED' AND Date(n.modificationtime) >= date :startDate AND Date(n.modificationtime) <= date :endDate AND n.totalttl IS NOT NULL AND n.countttl > 0 group by n.modificationtime having sum(n.totalttl)/sum(n.countttl) <= :threshold)"),
		@Filter(name = StealthConstants.TDNS_DAY_WISE_GOOD_FILTER, condition = "modificationtime in (select n.modificationtime from DriveTestKPI n ,NVDevice d, where and n.nvdeviceid_fk = d.nvdeviceid_pk and n.wptProcessStatus = 'PROCESSED' AND Date(n.modificationtime) >= date :startDate AND Date(n.modificationtime) <= date :endDate AND n.totaltdns IS NOT NULL AND n.counttdns > 0 group by n.modificationtime having sum(n.totaltdns)/sum(n.counttdns) <= :threshold)"),
		@Filter(name = StealthConstants.FDNS_DAY_WISE_GOOD_FILTER, condition = "modificationtime in (select n.modificationtime from DriveTestKPI n ,NVDevice d, where and n.nvdeviceid_fk = d.nvdeviceid_pk and n.wptProcessStatus = 'PROCESSED' AND Date(n.modificationtime) >= date :startDate AND Date(n.modificationtime) <= date :endDate AND n.totalfdns IS NOT NULL AND n.countfdns > 0 group by n.modificationtime having sum(n.totalfdns)/sum(n.countfdns) <= :threshold)"),

})
@Entity
@Table(name = "DriveTestKPI")
@XmlRootElement(name = "DriveTestKPI")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class DriveTestKPI implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "drivetestkpiid_pk")
	private Integer id;

	/** The nv device. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nvdeviceid_fk")
	private NVDeviceData nvDevice;

	/** The task result. */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stealthtaskresultid_fk")
	private StealthTaskResult taskResult;

	/** The cems data. */
	@Column(name = "cemsdata")
	private String cemsData;

	/** The cems process status. */
	@Column(name = "cemsprocessstatus")
	private String cemsProcessStatus;

	/** The status. */
	@Column(name = "status")
	private String status;

	/** The startTime. */
	@Column(name = "starttime")
	private Date startTime;

	/** The endTime. */
	@Column(name = "endtime")
	private Date endTime;

	/** The creation time. */
	@Column(name = "creationtime")
	private Date creationTime;

	/** The modification time. */
	@Column(name = "modificationtime")
	private Date modificationTime;

	/** The wpt process status. */
	@Column(name = "wptprocessstatus")
	private String wptProcessStatus;

	/** The countrsrp. */
	@Column(name = "countrsrp")
	private Integer countRSRP;

	/** The totalrsrp. */
	@Column(name = "totalrsrp")
	private Double totalRSRP;

	/** The countrsrq. */
	@Column(name = "countrsrq")
	private Integer countRSRQ;

	/** The totalrsrq. */
	@Column(name = "totalrsrq")
	private Double totalRSRQ;

	/** The countrssi. */
	@Column(name = "countrssi")
	private Integer countRSSI;

	/** The totalrssi. */
	@Column(name = "totalrssi")
	private Double totalRSSI;

	/** The countsinr. */
	@Column(name = "countsinr")
	private Integer countSINR;

	/** The totalsinr. */
	@Column(name = "totalsinr")
	private Double totalSINR;

	/** The countrxlevel. */
	@Column(name = "countrxlevel")
	private Integer countRxLevel;

	/** The totalrxlevel. */
	@Column(name = "totalrxlevel")
	private Double totalRxLevel;

	/** The countrxquality. */
	@Column(name = "countrxquality")
	private Integer countRxQuality;

	/** The totalrxquality. */
	@Column(name = "totalrxquality")
	private Double totalRxQuality;

	/** The countrscp. */
	@Column(name = "countrscp")
	private Integer countRSCP;

	/** The totalrscp. */
	@Column(name = "totalrscp")
	private Double totalRSCP;

	/** The countecno. */
	@Column(name = "countecno")
	private Integer countECNO;

	/** The totalecno. */
	@Column(name = "totalecno")
	private Double totalECNO;

	/** The countttl. */
	@Column(name = "countttl")
	private Integer countTTL;

	/** The totalttl. */
	@Column(name = "totalttl")
	private Double totalTTL;

	/** The countttfb. */
	@Column(name = "countttfb")
	private Integer countTTFB;

	/** The totalttfb. */
	@Column(name = "totalttfb")
	private Double totalTTFB;

	/** The counttdns. */
	@Column(name = "counttdns")
	private Integer countTDNS;

	/** The totaltdns. */
	@Column(name = "totaltdns")
	private Double totalTDNS;

	/** The countfdns. */
	@Column(name = "countfdns")
	private Integer countFDNS;

	/** The totalfdns. */
	@Column(name = "totalfdns")
	private Double totalFDNS;

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
	 * Gets the nv device.
	 *
	 * @return the nv device
	 */
	public NVDeviceData getNvDevice() {
		return nvDevice;
	}

	/**
	 * Sets the nv device.
	 *
	 * @param nvDevice
	 *            the new nv device
	 */
	public void setNvDevice(NVDeviceData nvDevice) {
		this.nvDevice = nvDevice;
	}

	/**
	 * Gets the task result.
	 *
	 * @return the task result
	 */
	public StealthTaskResult getTaskResult() {
		return taskResult;
	}

	/**
	 * Sets the task result.
	 *
	 * @param taskResult
	 *            the new task result
	 */
	public void setTaskResult(StealthTaskResult taskResult) {
		this.taskResult = taskResult;
	}

	/**
	 * Gets the cems data.
	 *
	 * @return the cems data
	 */
	public String getCemsData() {
		return cemsData;
	}

	/**
	 * Sets the cems data.
	 *
	 * @param cemsData
	 *            the new cems data
	 */
	public void setCemsData(String cemsData) {
		this.cemsData = cemsData;
	}

	/**
	 * Gets the cems process status.
	 *
	 * @return the cems process status
	 */
	public String getCemsProcessStatus() {
		return cemsProcessStatus;
	}

	/**
	 * Sets the cems process status.
	 *
	 * @param cemsProcessStatus
	 *            the new cems process status
	 */
	public void setCemsProcessStatus(String cemsProcessStatus) {
		this.cemsProcessStatus = cemsProcessStatus;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status
	 *            the new status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gets the start time.
	 *
	 * @return the start time
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * Sets the start time.
	 *
	 * @param startTime
	 *            the new start time
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * Gets the end time.
	 *
	 * @return the end time
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * Sets the end time.
	 *
	 * @param endTime
	 *            the new end time
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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
	 * Gets the wpt process status.
	 *
	 * @return the wpt process status
	 */
	public String getWptProcessStatus() {
		return wptProcessStatus;
	}

	/**
	 * Sets the wpt process status.
	 *
	 * @param wptProcessStatus
	 *            the new wpt process status
	 */
	public void setWptProcessStatus(String wptProcessStatus) {
		this.wptProcessStatus = wptProcessStatus;
	}

	/**
	 * Gets the count RSRP.
	 *
	 * @return the count RSRP
	 */
	public Integer getCountRSRP() {
		return countRSRP;
	}

	/**
	 * Sets the count RSRP.
	 *
	 * @param countRSRP
	 *            the new count RSRP
	 */
	public void setCountRSRP(Integer countRSRP) {
		this.countRSRP = countRSRP;
	}

	/**
	 * Gets the total RSRP.
	 *
	 * @return the total RSRP
	 */
	public Double getTotalRSRP() {
		return totalRSRP;
	}

	/**
	 * Sets the total RSRP.
	 *
	 * @param totalRSRP
	 *            the new total RSRP
	 */
	public void setTotalRSRP(Double totalRSRP) {
		this.totalRSRP = totalRSRP;
	}

	/**
	 * Gets the count RSRQ.
	 *
	 * @return the count RSRQ
	 */
	public Integer getCountRSRQ() {
		return countRSRQ;
	}

	/**
	 * Sets the count RSRQ.
	 *
	 * @param countRSRQ
	 *            the new count RSRQ
	 */
	public void setCountRSRQ(Integer countRSRQ) {
		this.countRSRQ = countRSRQ;
	}

	/**
	 * Gets the total RSRQ.
	 *
	 * @return the total RSRQ
	 */
	public Double getTotalRSRQ() {
		return totalRSRQ;
	}

	/**
	 * Sets the total RSRQ.
	 *
	 * @param totalRSRQ
	 *            the new total RSRQ
	 */
	public void setTotalRSRQ(Double totalRSRQ) {
		this.totalRSRQ = totalRSRQ;
	}

	/**
	 * Gets the count RSSI.
	 *
	 * @return the count RSSI
	 */
	public Integer getCountRSSI() {
		return countRSSI;
	}

	/**
	 * Sets the count RSSI.
	 *
	 * @param countRSSI
	 *            the new count RSSI
	 */
	public void setCountRSSI(Integer countRSSI) {
		this.countRSSI = countRSSI;
	}

	/**
	 * Gets the total RSSI.
	 *
	 * @return the total RSSI
	 */
	public Double getTotalRSSI() {
		return totalRSSI;
	}

	/**
	 * Sets the total RSSI.
	 *
	 * @param totalRSSI
	 *            the new total RSSI
	 */
	public void setTotalRSSI(Double totalRSSI) {
		this.totalRSSI = totalRSSI;
	}

	/**
	 * Gets the count SINR.
	 *
	 * @return the count SINR
	 */
	public Integer getCountSINR() {
		return countSINR;
	}

	/**
	 * Sets the count SINR.
	 *
	 * @param countSINR
	 *            the new count SINR
	 */
	public void setCountSINR(Integer countSINR) {
		this.countSINR = countSINR;
	}

	/**
	 * Gets the total SINR.
	 *
	 * @return the total SINR
	 */
	public Double getTotalSINR() {
		return totalSINR;
	}

	/**
	 * Sets the total SINR.
	 *
	 * @param totalSINR
	 *            the new total SINR
	 */
	public void setTotalSINR(Double totalSINR) {
		this.totalSINR = totalSINR;
	}

	/**
	 * Gets the count rx level.
	 *
	 * @return the count rx level
	 */
	public Integer getCountRxLevel() {
		return countRxLevel;
	}

	/**
	 * Sets the count rx level.
	 *
	 * @param countRxLevel
	 *            the new count rx level
	 */
	public void setCountRxLevel(Integer countRxLevel) {
		this.countRxLevel = countRxLevel;
	}

	/**
	 * Gets the total rx level.
	 *
	 * @return the total rx level
	 */
	public Double getTotalRxLevel() {
		return totalRxLevel;
	}

	/**
	 * Sets the total rx level.
	 *
	 * @param totalRxLevel
	 *            the new total rx level
	 */
	public void setTotalRxLevel(Double totalRxLevel) {
		this.totalRxLevel = totalRxLevel;
	}

	/**
	 * Gets the count rx quality.
	 *
	 * @return the count rx quality
	 */
	public Integer getCountRxQuality() {
		return countRxQuality;
	}

	/**
	 * Sets the count rx quality.
	 *
	 * @param countRxQuality
	 *            the new count rx quality
	 */
	public void setCountRxQuality(Integer countRxQuality) {
		this.countRxQuality = countRxQuality;
	}

	/**
	 * Gets the total rx quality.
	 *
	 * @return the total rx quality
	 */
	public Double getTotalRxQuality() {
		return totalRxQuality;
	}

	/**
	 * Sets the total rx quality.
	 *
	 * @param totalRxQuality
	 *            the new total rx quality
	 */
	public void setTotalRxQuality(Double totalRxQuality) {
		this.totalRxQuality = totalRxQuality;
	}

	/**
	 * Gets the count RSCP.
	 *
	 * @return the count RSCP
	 */
	public Integer getCountRSCP() {
		return countRSCP;
	}

	/**
	 * Sets the count RSCP.
	 *
	 * @param countRSCP
	 *            the new count RSCP
	 */
	public void setCountRSCP(Integer countRSCP) {
		this.countRSCP = countRSCP;
	}

	/**
	 * Gets the total RSCP.
	 *
	 * @return the total RSCP
	 */
	public Double getTotalRSCP() {
		return totalRSCP;
	}

	/**
	 * Sets the total RSCP.
	 *
	 * @param totalRSCP
	 *            the new total RSCP
	 */
	public void setTotalRSCP(Double totalRSCP) {
		this.totalRSCP = totalRSCP;
	}

	/**
	 * Gets the count ECNO.
	 *
	 * @return the count ECNO
	 */
	public Integer getCountECNO() {
		return countECNO;
	}

	/**
	 * Sets the count ECNO.
	 *
	 * @param countECNO
	 *            the new count ECNO
	 */
	public void setCountECNO(Integer countECNO) {
		this.countECNO = countECNO;
	}

	/**
	 * Gets the total ECNO.
	 *
	 * @return the total ECNO
	 */
	public Double getTotalECNO() {
		return totalECNO;
	}

	/**
	 * Sets the total ECNO.
	 *
	 * @param totalECNO
	 *            the new total ECNO
	 */
	public void setTotalECNO(Double totalECNO) {
		this.totalECNO = totalECNO;
	}

	/**
	 * Gets the count TTL.
	 *
	 * @return the count TTL
	 */
	public Integer getCountTTL() {
		return countTTL;
	}

	/**
	 * Sets the count TTL.
	 *
	 * @param countTTL
	 *            the new count TTL
	 */
	public void setCountTTL(Integer countTTL) {
		this.countTTL = countTTL;
	}

	/**
	 * Gets the total TTL.
	 *
	 * @return the total TTL
	 */
	public Double getTotalTTL() {
		return totalTTL;
	}

	/**
	 * Sets the total TTL.
	 *
	 * @param totalTTL
	 *            the new total TTL
	 */
	public void setTotalTTL(Double totalTTL) {
		this.totalTTL = totalTTL;
	}

	/**
	 * Gets the count TTFB.
	 *
	 * @return the count TTFB
	 */
	public Integer getCountTTFB() {
		return countTTFB;
	}

	/**
	 * Sets the count TTFB.
	 *
	 * @param countTTFB
	 *            the new count TTFB
	 */
	public void setCountTTFB(Integer countTTFB) {
		this.countTTFB = countTTFB;
	}

	/**
	 * Gets the total TTFB.
	 *
	 * @return the total TTFB
	 */
	public Double getTotalTTFB() {
		return totalTTFB;
	}

	/**
	 * Sets the total TTFB.
	 *
	 * @param totalTTFB
	 *            the new total TTFB
	 */
	public void setTotalTTFB(Double totalTTFB) {
		this.totalTTFB = totalTTFB;
	}

	/**
	 * Gets the count TDNS.
	 *
	 * @return the count TDNS
	 */
	public Integer getCountTDNS() {
		return countTDNS;
	}

	/**
	 * Sets the count TDNS.
	 *
	 * @param countTDNS
	 *            the new count TDNS
	 */
	public void setCountTDNS(Integer countTDNS) {
		this.countTDNS = countTDNS;
	}

	/**
	 * Gets the total TDNS.
	 *
	 * @return the total TDNS
	 */
	public Double getTotalTDNS() {
		return totalTDNS;
	}

	/**
	 * Sets the total TDNS.
	 *
	 * @param totalTDNS
	 *            the new total TDNS
	 */
	public void setTotalTDNS(Double totalTDNS) {
		this.totalTDNS = totalTDNS;
	}

	/**
	 * Gets the count FDNS.
	 *
	 * @return the count FDNS
	 */
	public Integer getCountFDNS() {
		return countFDNS;
	}

	/**
	 * Sets the count FDNS.
	 *
	 * @param countFDNS
	 *            the new count FDNS
	 */
	public void setCountFDNS(Integer countFDNS) {
		this.countFDNS = countFDNS;
	}

	/**
	 * Gets the total FDNS.
	 *
	 * @return the total FDNS
	 */
	public Double getTotalFDNS() {
		return totalFDNS;
	}

	/**
	 * Sets the total FDNS.
	 *
	 * @param totalFDNS
	 *            the new total FDNS
	 */
	public void setTotalFDNS(Double totalFDNS) {
		this.totalFDNS = totalFDNS;
	}

}
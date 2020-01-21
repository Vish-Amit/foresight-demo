package com.inn.foresight.core.infra.service;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.infra.model.NETaskDetail;
import com.inn.foresight.core.infra.wrapper.NETaskDetailWrapper;
import com.inn.foresight.core.infra.wrapper.RRHDetailWrapper;

/** The Interface INETaskDetailService. */
public interface INETaskDetailService extends IGenericService<Integer, NETaskDetail> {
	/**
	 * Gets the distinct stages for planned sites.
	 *
	 * @return the distinct stages for planned sites
	 */
	List<String> getDistinctStagesForPlannedSites();

	/**
	 * Gets the site task status by site id and band.
	 *
	 * @param siteId     the site id
	 * @param taskName   the task name
	 * @param band       the band
	 * @param taskStatus the task status
	 * @return the site task status by site id and band
	 */
	boolean getSiteTaskStatusBySiteIdAndBand(String siteId, String taskName, String band, String taskStatus);

	/**
	 * Gets the site task by site id and band.
	 *
	 * @param siteId     the site id
	 * @param taskName   the task name
	 * @param band       the band
	 * @param taskStatus the task status
	 * @return the site task by site id and band
	 */
	NETaskDetail getSiteTaskBySiteIdAndBand(String siteId, String taskName, String band, String taskStatus);

	/**
	 * Update NE task detail.
	 *
	 * @param siteId      the site id
	 * @param riuSerialNo the riu serial no
	 * @param band        the band
	 * @param taskStatus  the task status
	 * @param taskName    the task name
	 * @param rrhList     the rrh list
	 * @param startDate   the start date
	 * @param endDate     the end date
	 * @param triggerTime the trigger time
	 * @return the map
	 */
	Map<String, String> updateNETaskDetail(String siteId, String riuSerialNo, String band, String taskStatus, String taskName, List<RRHDetailWrapper> rrhList, Long startDate, Long endDate, Long triggerTime);

	/**
	 * Update ne task detail by site and frequency.
	 *
	 * @param neDetailWrapper the ne detail wrapper
	 * @return the map
	 */
	Map<String, String> updateNeTaskDetailBySiteAndFrequency(NETaskDetailWrapper neDetailWrapper);

	/**
	 * Update network element status by pk.
	 *
	 * @param nePk   the ne pk
	 * @param status the status
	 * @return the string
	 */
	String updateNetworkElementStatusByPk(Integer nePk, String status);
}
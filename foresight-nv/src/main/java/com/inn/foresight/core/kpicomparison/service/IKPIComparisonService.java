package com.inn.foresight.core.kpicomparison.service;

import java.awt.image.BufferedImage;
import java.util.List;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.kpicomparison.wrapper.KPIComparisonRequest;

/**
 * The Interface IKpiComparisonService.
 */
public interface IKPIComparisonService {

	/**
	 * Gets the KPI comparison.
	 *
	 * @param tileId
	 *            the tile id
	 * @param siteStatus TODO
	 * @param frequency TODO
	 * @param request
	 *            the request
	 * @return the KPI comparison
	 * @throws RestException
	 *             the rest exception
	 */
	public BufferedImage getKPIComparison(String tileId, String siteStatus, String frequency, KPIComparisonRequest request);

	/**
	 * Gets the KPI data comparison.
	 *
	 * @param request
	 *            the request
	 * @return the KPI data comparison
	 * @throws RestException
	 *             the rest exception
	 */
	List<List<String>> getKPIDataComparison(KPIComparisonRequest request);
	
	public BufferedImage getKPIComparisonForMS(String tileId, String siteStatus, String frequency, KPIComparisonRequest request);
}

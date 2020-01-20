package com.inn.foresight.module.nv.inbuilding.rest;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import com.inn.core.generic.exceptions.application.RestException;

/**
 * The Interface IInBuildingResult.
 *
 * @author ist
 */
public interface IInBuildingResultRest {


	/**
	 * Sync in building result file.
	 *
	 * @param inputFile
	 *            the input file
	 * @param fileName
	 *            the file name
	 * @return the response
	 */
	Response syncInBuildingResultFile(InputStream inputFile, String fileName);

	/**
	 * Download floor plan.
	 *
	 * @param unitId
	 *            the unit id
	 * @param Technology
	 *            the technology
	 * @return the response
	 */
	Response downloadFloorPlan(Integer unitId);

	/**
	 * Upload floor plan.
	 *
	 * @param unitId
	 *            the unit id
	 * @param technology
	 *            the technology
	 * @param inputStream
	 *            the input stream
	 * @param fileName
	 *            the file name
	 * @return the response
	 */
	Response uploadFloorPlan(Integer unitId, String technology, InputStream inputStream, String fileName);

	/**
	 * Upload floor plan.
	 *
	 * @param request
	 *            the request
	 * @return the response
	 */
	Response uploadFloorPlans(HttpServletRequest request);

	/**
	 * Upload floor plan data from mobile.
	 *
	 * @param unitId
	 *            the unit id
	 * @param technology
	 *            the technology
	 * @return the response
	 * @throws RestException
	 *             the rest exception
	 */
	Response uploadFloorPlanData(Integer unitId, InputStream inputStream, String fileName);

	/**
	 * Download floor plan data.
	 *
	 * @param unitId
	 *            the unit id
	 * @return the response
	 */
	Response downloadFloorPlanData(Integer unitId);

}
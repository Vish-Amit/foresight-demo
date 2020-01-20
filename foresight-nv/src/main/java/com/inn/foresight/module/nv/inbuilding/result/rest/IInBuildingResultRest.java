package com.inn.foresight.module.nv.inbuilding.result.rest;

import java.io.InputStream;

import javax.ws.rs.core.Response;

import com.inn.core.generic.exceptions.application.RestException;


/**
 * The Interface IInBuildingResult.
 *
 * @author ist
 */
public interface IInBuildingResultRest {

//	/**
//	 * Sync in building result file.
//	 *
//	 * @param inputFile
//	 *            the input file
//	 * @param fileName
//	 *            the file name
//	 * @return the response
//	 * @throws RestException
//	 *             the rest exception
//	 */
//	public Response syncInBuildingResultFile(InputStream inputFile, String fileName) throws RestException;

	/**
	 * Download floor plan.
	 *
	 * @param unitId
	 *            the unit id
	 * @param technology
	 *            the technology
	 * @return the response
	 * @throws RestException
	 *             the rest exception
	 */
	Response downloadFloorPlan(Integer unitId);

//	/**
//	 * Upload floor plan.
//	 *
//	 * @param unitId
//	 *            the unit id
//	 * @param technology
//	 *            the technology
//	 * @return the response
//	 * @throws RestException
//	 *             the rest exception
//	 */
//	public Response uploadFloorPlan(Integer unitId, String technology, InputStream inputStream, String fileName)
//			throws RestException;

	/**
	 * Upload floor plan data.
	 *
	 * @param unitId
	 *            the unit id
	 * @param inputStream
	 *            the input stream
	 * @param fileName
	 *            the fileName
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
	 * @throws RestException
	 *             the rest exception
	 */
	Response downloadFloorPlanData(Integer unitId);

}

package com.inn.foresight.module.nv.inbuilding.result.service;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.infra.wrapper.BuildingWrapper;

/** The Interface IInBuildingResultService. */
public interface IInBuildingResultService {
//	/**
//	 * Upload floor plan.
//	 *
//	 * @param unitId
//	 *            the unit id
//	 * @param technology
//	 *            the technology
//	 * @param inputStream
//	 *            the input stream
//	 * @param fileName
//	 *            the file name
//	 * @return the string
//	 * @throws RestException
//	 *             the rest exception
//	 */
//	public String uploadFloorPlan(Integer unitId, String technology, InputStream inputStream, String fileName)
//			throws RestException;
//	/**
//	 * Sync in building result file.
//	 *
//	 * @param inputFile
//	 *            the input file
//	 * @param fileName
//	 *            the file name
//	 * @return the string
//	 * @throws RestException
//	 *             the rest exception
//	 */
//	public String syncInBuildingResultFile(InputStream inputFile, String fileName) throws RestException;

	/**
	 * Download floor plan.
	 *
	 * @param unitId
	 *            the unit id
	 * @param technology
	 *            the technology
	 * @return the string
	 * @throws RestException
	 *             the rest exception
	 */
	Response downloadFloorPlan(Integer unitId);
	
	/**
	 * Upload floor plan data.
	 *
	 * @param unitId
	 *            the unit id
	 * @param inputStream
	 *            the input stream
	 * @param fileName
	 *            the file name
	 * @return the string
	 * @throws RestException
	 *             the rest exception
	 */
	String uploadFloorPlanData(Integer unitId, InputStream inputStream, String fileName);
	
	/**S
	 * Download floor plan data.
	 *
	 * @param unitId
	 *            the unit id
	 * @return the string
	 * @throws RestException
	 *             the rest exception
	 */
	byte[] downloadFloorPlanData(Integer unitId);

	String processIBFloorPlanAndTestResultData(InputStream dataStream);

	String uploadFloorPlan(String templateType, Integer unitId, String technology, InputStream inputStream, String fileName);

	Response downloadFloorPlanImage(Integer unitId);

	String putFloorPlanData(Integer unitId, InputStream inputStream, String fileName);

	byte[] getFloorPlanData(Integer unitId);

	BuildingWrapper uploadMultipleFloorPlan(HttpServletRequest request);
	
}

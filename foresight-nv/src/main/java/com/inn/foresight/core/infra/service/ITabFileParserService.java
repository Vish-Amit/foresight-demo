package com.inn.foresight.core.infra.service;

import java.util.Map;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.infra.wrapper.GeographyBoundaryWrapper;

/**
 * The Interface ITabFileParserService.
 */
public interface ITabFileParserService extends IGenericService<Integer, Object> {

	/**
	 * Parses the tab file for boundaries.
	 *
	 * @param filePath the file path
	 * @return the map
	 * @throws RestException the rest exception
	 */
	Map<String, GeographyBoundaryWrapper> parseTabFileForBoundaries(String filePath);

}

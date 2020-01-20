package com.inn.foresight.module.nv.workorder.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.inn.commons.http.HttpException;
import com.inn.foresight.module.nv.workorder.model.CustomGeography;
import com.inn.foresight.module.nv.workorder.model.CustomGeography.GeographyType;
import com.inn.foresight.module.nv.workorder.wrapper.CustomGeographyWrapper;

/** The Interface ICustomGeographyService. */
public interface ICustomGeographyService {
	
	CustomGeography createCustomGeography(CustomGeographyWrapper wrapper, HttpServletRequest request);

	List<CustomGeographyWrapper> findAllCustomGeography(Integer geographyId, String geographyType, List<GeographyType> typeList);

	CustomGeographyWrapper getCustomGeography(Integer geographyId, String url) throws HttpException;

	CustomGeographyWrapper getCustomGeography(Integer gegraphyId, GeographyType type);

	String createCustomGeography(CustomGeographyWrapper wrapper);
	
}

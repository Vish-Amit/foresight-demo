package com.inn.foresight.module.nv.workorder.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.module.nv.workorder.model.CustomGeography;
import com.inn.foresight.module.nv.workorder.model.CustomGeography.GeographyType;
import com.inn.foresight.module.nv.workorder.wrapper.CustomGeographyWrapper;

/** The Interface ICustomGeographyDao. */
public interface ICustomGeographyDao extends IGenericDao<Integer, CustomGeography>{

	List<CustomGeography> findAllCustomGeographyForUser(Integer userId);

	List<CustomGeographyWrapper> findAllCustomGeography(Integer geographyId, String geographyType, List<GeographyType> typeList);

	
}

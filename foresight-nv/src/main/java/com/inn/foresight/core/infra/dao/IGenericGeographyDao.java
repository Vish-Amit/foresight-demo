package com.inn.foresight.core.infra.dao;

import java.util.List;

import com.inn.commons.maps.Corner;
import com.inn.commons.maps.LatLng;
import com.inn.core.generic.dao.IGenericDao;
import com.inn.product.um.geography.utils.wrapper.GeographyWrapper;

public interface IGenericGeographyDao<T> extends IGenericDao<Integer, T> {
	
	public T getGeography(String name);
	
	public List<T> getGeography(List<String> names);

	public List<String> getDistinctGeographyNames();

	public List<T> getAllGeography();

	public List<GeographyWrapper> getAllGeographyWrapper();
	
	public List<GeographyWrapper> getGeographyWrapper(Corner corner);

	public T getGeography(Integer id);
	
	public List<T> getGeographyByIdList(List<Integer> ids);
	
	public List<T> getChildByL1Name(String name);
	
	public List<T> getChildByL2Name(String name);
	
	public List<T> getChildByL3Name(String name);
	
    public List<T> getChildByL1Id(Integer id);
	
	public List<T> getChildByL2Id(Integer id);
	
	public List<T> getChildByL3Id(Integer id);
	
	public List<GeographyWrapper> getChildByParentId(Integer id);
	
	public List<GeographyWrapper> getChildByParentIds(List<Integer> ids);
	
	public List<T> searchByName(String name, Integer uLimit, Integer lLimit);
	
	public T getGeographyByNameAndParentId(String name, Integer parentId);
	
	public T getGeographyByLatLng(LatLng latLng);
	
	
	
	
	
	
	
	

}

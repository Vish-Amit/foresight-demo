package com.inn.foresight.core.gallery.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.gallery.model.GalleryDetail;

public interface IGalleryDetailDao extends IGenericDao<Integer, GalleryDetail>{

	List<GalleryDetail> getGallerySmileForVisualization(Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat);

	List<GalleryDetail> getGallerySmileDataList(Integer llimit, Integer ulimit);

	Long getGallerySmileCount();

	List<GalleryDetail> searchByName(String name,Integer llimit, Integer ulimit);

	Long getGallerySmileCountBySearchName(String name);

	int updateGalleryStatus(List<Integer> idList, Boolean enabled);

}

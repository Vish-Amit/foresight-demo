package com.inn.foresight.core.gallery.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.gallery.model.GalleryDetail;

public interface IGalleryDetailService extends IGenericService<Integer, GalleryDetail> {

	List<GalleryDetail> getGallerySmileForVisualization(Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat);

	@PreAuthorize("hasRole('ROLE_ADMIN_GALLERY_viewGallery')")
	List<GalleryDetail> getGallerySmileDataList(Integer llimit, Integer ulimit);

	@PreAuthorize("hasRole('ROLE_ADMIN_GALLERY_createGallery')")
	Map<String, String> createGallerySmile(GalleryDetail g);

	@PreAuthorize("hasRole('ROLE_ADMIN_GALLERY_editGallery')")
	Map<String, String> updateGallerySmile(GalleryDetail g);

	@PreAuthorize("hasRole('ROLE_ADMIN_GALLERY_viewGallery')")
	Map<String, Long> getGallerySmileCount();

	@PreAuthorize("hasRole('ROLE_ADMIN_GALLERY_viewGallery')")
	GalleryDetail getGallerySmileById(Integer id);

	Map<String, String> deleteGallerySmileById(Integer id);

	@PreAuthorize("hasRole('ROLE_ADMIN_GALLERY_viewGallery')")
	List<GalleryDetail> searchByName(String name, Integer llimit, Integer ulimit);

	Map<String, String> updateGalleryStatus(List<Integer> id, Boolean enabled);

	Map<String, Long> getGallerySmileCountBySearchName(String name);

	Map<String, String> searchGallerySmileForAdvanceSearch(Integer galleryId);

	String getGallerySmileManagerByManagerType(String managerType);

}

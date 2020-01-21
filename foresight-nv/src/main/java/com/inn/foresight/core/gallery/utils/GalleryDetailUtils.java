package com.inn.foresight.core.gallery.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.inn.foresight.core.gallery.model.GalleryDetail;
import com.inn.foresight.core.infra.utils.InfraUtils;

public class GalleryDetailUtils extends InfraUtils {

	public static void getSelectionForVisulisation(CriteriaQuery<GalleryDetail> criteriaQuery,
			Root<GalleryDetail> root) {
		criteriaQuery.select(root);
	}

	public static List<Predicate> getPredicatesForGalleryDetailViewPorts(CriteriaBuilder criteriaBuilder,
			Root<GalleryDetail> root, Map<String, Double> viewportMap) {
		List<Predicate> predicates = new ArrayList<>();

		if (viewportMap != null && !viewportMap.isEmpty()) {
			if (viewportMap.get(GalleryDetailConstants.GALLERY_SOUTHWEST_LATITUDE_KEY) != null) {
				predicates.add(criteriaBuilder.greaterThan(root.get(GalleryDetailConstants.GALLERY_LATITUDE),
						viewportMap.get(GalleryDetailConstants.GALLERY_SOUTHWEST_LATITUDE_KEY)));
			}
			if (viewportMap.get(GalleryDetailConstants.GALLERY_NORTHEAST_LATITUDE_KEY) != null) {
				predicates.add(criteriaBuilder.lessThan(root.get(GalleryDetailConstants.GALLERY_LATITUDE),
						viewportMap.get(GalleryDetailConstants.GALLERY_NORTHEAST_LATITUDE_KEY)));
			}
			if (viewportMap.get(GalleryDetailConstants.GALLERY_SOUTHWEST_LONGITUDE_KEY) != null) {
				predicates.add(criteriaBuilder.greaterThan(root.get(GalleryDetailConstants.GALLERY_LONGITUDE),
						viewportMap.get(GalleryDetailConstants.GALLERY_SOUTHWEST_LONGITUDE_KEY)));
			}
			if (viewportMap.get(GalleryDetailConstants.GALLERY_NORTHEAST_LONGITUDE_KEY) != null) {
				predicates.add(criteriaBuilder.lessThan(root.get(GalleryDetailConstants.GALLERY_LONGITUDE),
						viewportMap.get(GalleryDetailConstants.GALLERY_NORTHEAST_LONGITUDE_KEY)));
			}
			predicates.add(criteriaBuilder.lessThan(root.get(GalleryDetailConstants.GALLERY_LONGITUDE),
					viewportMap.get(GalleryDetailConstants.GALLERY_NORTHEAST_LONGITUDE_KEY)));
		}
		return predicates;
	}

	public static void getWhereClauseInCriteriaQuery(CriteriaQuery<GalleryDetail> criteriaQuery,
			List<Predicate> predicates) {
		criteriaQuery.where(predicates.toArray(new Predicate[] {}));
	}

}

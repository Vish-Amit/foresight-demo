package com.inn.foresight.module.nv.webrtc.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.webrtc.constant.WebRTCConstant;
import com.inn.foresight.module.nv.webrtc.model.IViberLayerDao;
import com.inn.foresight.module.nv.webrtc.model.ViberLayer;

@Repository("ViberLayerDaoImpl")
public class ViberLayerDaoImpl extends HibernateGenericDao<Integer, ViberLayer> implements IViberLayerDao {

	private Logger logger = LogManager.getLogger(ViberLayerDaoImpl.class);

	public ViberLayerDaoImpl() {
		super(ViberLayer.class);
	}

	@Override
	public List<ViberLayer> getViberLayerData(List<String> tileIdList, Integer zoom, String mediaType,
			String callDirection, String releaseType, String os, Long startTime, Long endTime, String minValue,
			String maxValue, String layerType) {

		List<ViberLayer> resultList = null;
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<ViberLayer> criteriaQuery = criteriaBuilder.createQuery(ViberLayer.class);
			Root<ViberLayer> root = criteriaQuery.from(ViberLayer.class);

			List<Predicate> predicateList = getPredicateList(tileIdList, zoom, mediaType, callDirection, releaseType,
					os, startTime, endTime, minValue, maxValue, layerType, criteriaBuilder, root);

			criteriaQuery = addPredicateInCreateriaQuery(criteriaQuery, predicateList);

			resultList = getEntityManager().createQuery(criteriaQuery).getResultList();

			logger.info("Result Size ::: {}", resultList.size());
		} catch (Exception e) {
			logger.info("Exception in fetching viber layer data {}", Utils.getStackTrace(e));
		}

		return resultList;
	}

	private List<Predicate> getPredicateList(List<String> tileIdList, Integer zoom, String mediaType,
			String callDirection, String releaseType, String os, Long startTime, Long endTime, String minValue,
			String maxValue, String layerType, CriteriaBuilder criteriaBuilder, Root<ViberLayer> root) {

		String startTimeString = "startTime";
		String endTimeString = "endTime";
		List<Predicate> predicates = new ArrayList<>();
		if (startTime != null && endTime != null && layerType.equalsIgnoreCase(WebRTCConstant.CALL_LAYER)) {

			predicates.add(criteriaBuilder.greaterThan(root.get(startTimeString), startTime));
			predicates.add(criteriaBuilder.lessThan(root.get(endTimeString), endTime));

			if (mediaType != null && !mediaType.equalsIgnoreCase(WebRTCConstant.ALL)) {
				predicates.add(criteriaBuilder.equal(root.get("mediaType"), mediaType));
			}
			if (callDirection != null && !callDirection.equalsIgnoreCase(WebRTCConstant.ALL)) {
				predicates.add(criteriaBuilder.equal(root.get("callDirection"), callDirection));
			}
			if (releaseType != null && !releaseType.equalsIgnoreCase(WebRTCConstant.ALL)) {
				predicates.add(criteriaBuilder.equal(root.get("releaseType"), releaseType));
			}
			if (os != null && !os.equalsIgnoreCase(WebRTCConstant.ALL)) {
				predicates.add(criteriaBuilder.equal(root.get("osName"), os));
			}

			addPredicateOfTileId(tileIdList, zoom, root, predicates);

		} else if (startTime != null && endTime != null && layerType.equalsIgnoreCase(WebRTCConstant.MOS_LAYER)) {
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(startTimeString), startTime));
			predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(endTimeString), endTime));
			predicates.add(
					criteriaBuilder.between(root.get("mosValue"), Double.valueOf(minValue), Double.valueOf(maxValue)));
			addPredicateOfTileId(tileIdList, zoom, root, predicates);
		} else if (startTime != null && endTime != null && layerType.equalsIgnoreCase(WebRTCConstant.PKTLOSS_LAYER)) {
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(startTimeString), startTime));
			predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(endTimeString), endTime));

			predicates.add(criteriaBuilder.between(root.get("packetLoss"), Double.valueOf(minValue),
					Double.valueOf(maxValue)));
			addPredicateOfTileId(tileIdList, zoom, root, predicates);
		}

		return predicates;
	}

	private void addPredicateOfTileId(List<String> tileIdList, Integer zoom, Root<ViberLayer> root,
			List<Predicate> predicates) {
		if (WebRTCConstant.FIVE.equals(zoom) || WebRTCConstant.SIX.equals(zoom) || WebRTCConstant.SEVEN.equals(zoom)
				|| WebRTCConstant.EIGHT.equals(zoom)) {
			Expression<String> parentExpression = root.get("tileIdGeographyL1");
			predicates.add(parentExpression.in(tileIdList));
		} else if (WebRTCConstant.NINE.equals(zoom) || WebRTCConstant.TEN.equals(zoom) || WebRTCConstant.ELEVEN.equals(zoom)
				|| WebRTCConstant.TWELVE.equals(zoom)) {
			Expression<String> parentExpression = root.get("tileIdGeographyL2");
			predicates.add(parentExpression.in(tileIdList));
		} else if (WebRTCConstant.THIRTEEN.equals(zoom) || WebRTCConstant.FOURTEEN.equals(zoom)
				|| WebRTCConstant.FIFTEEN.equals(zoom)) {
			Expression<String> parentExpression = root.get("tileIdGeographyL3");
			predicates.add(parentExpression.in(tileIdList));
		} else if (WebRTCConstant.SIXTEEN.equals(zoom) || WebRTCConstant.SEVENTEEN.equals(zoom)
				|| WebRTCConstant.EIGHTEEN.equals(zoom)) {
			Expression<String> parentExpression = root.get("tileIdGeographyL4");
			predicates.add(parentExpression.in(tileIdList));
		}
	}

	public static CriteriaQuery<ViberLayer> addPredicateInCreateriaQuery(CriteriaQuery<ViberLayer> criteriaQuery,
			List<Predicate> predicates) {
		criteriaQuery.where(predicates.toArray(new Predicate[] {}));
		return criteriaQuery;
	}

}

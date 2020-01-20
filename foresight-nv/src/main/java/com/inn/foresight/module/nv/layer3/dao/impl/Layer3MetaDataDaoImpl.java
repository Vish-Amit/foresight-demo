package com.inn.foresight.module.nv.layer3.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.cxf.jaxrs.utils.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.module.nv.layer3.constants.Layer3PPEConstant;
import com.inn.foresight.module.nv.layer3.dao.Layer3MetaDataDao;
import com.inn.foresight.module.nv.layer3.model.Layer3MetaData;
import com.inn.foresight.module.nv.layer3.wrapper.Layer3PPEWrapper;

@Repository("Layer3MetaDataDaoImpl")
public class Layer3MetaDataDaoImpl extends HibernateGenericDao<Integer, Layer3MetaData>
implements Layer3MetaDataDao {

	private Logger logger = LogManager.getLogger(Layer3MetaDataDaoImpl.class);


	public Layer3MetaDataDaoImpl() {
		super(Layer3MetaData.class);
	}

	@Override
	public List<Layer3MetaData> getLayer3MetaData(Layer3PPEWrapper layer3ppeWrapper) {
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Layer3MetaData> criteriaQuery = criteriaBuilder.createQuery(Layer3MetaData.class);

			List<Predicate> predicateList = getPredicateList(layer3ppeWrapper, criteriaBuilder,
					criteriaQuery.from(Layer3MetaData.class));

			addPredicateInCreateriaQuery(criteriaQuery, predicateList);

			return getEntityManager().createQuery(criteriaQuery).getResultList();
		} catch (Exception e) {
			logger.error("Exception while getLayer3MetaData {}",ExceptionUtils.getStackTrace(e));
		}

		return Collections.EMPTY_LIST;

	}




	private List<Predicate> getPredicateList(Layer3PPEWrapper layer3ppeWrapper,
			CriteriaBuilder criteriaBuilder, Root<Layer3MetaData> root) {
		List<Predicate> predicates = new ArrayList<>();

		if(layer3ppeWrapper == null) {
			predicates.add(criteriaBuilder.isNotNull(root.get(Layer3PPEConstant.LAYER3_KPI_DISPLAY_NAME)));

		}
		if (layer3ppeWrapper != null) {
			if (layer3ppeWrapper.getKpiName() != null) {
				predicates.add(criteriaBuilder.equal(root.get(Layer3PPEConstant.LAYER3_KPI_DISPLAY_NAME), layer3ppeWrapper.getKpiName()));
			}
			if (layer3ppeWrapper.getHbaseColumnName() != null) {
				predicates.add(criteriaBuilder.equal(root.get(Layer3PPEConstant.LAYER3_HBASE_COLUMN_NAME), layer3ppeWrapper.getHbaseColumnName()));
			}
			
			if (layer3ppeWrapper.getSourceType() != null) {
				predicates.add(criteriaBuilder.equal(root.get(Layer3PPEConstant.LAYER3_SOURCE_TYPE), layer3ppeWrapper.getSourceType()));
			} 

			
			
			/*	if (layer3ppeWrapper.getSummary != null) {
				predicates.add(criteriaBuilder.equal(root.get(Layer3PPEConstant.LAYER3_SUMMARY_AGG_TYPE), layer3ppeWrapper.getSummaryAggType()));
			} 

			if (layer3ppeWrapper.getDriveAggType() != null) {
				predicates.add(criteriaBuilder.equal(root.get(Layer3PPEConstant.LAYER3_DRIVE_AGG_TYPE), layer3ppeWrapper.getDriveAggType()));
			} 

			if (layer3ppeWrapper.getTechHierarchy() != null) {
				predicates.add(criteriaBuilder.equal(root.get(Layer3PPEConstant.LAYER3_TECH_HIERARCHY), layer3ppeWrapper.getTechHierarchy()));
			} 

			if (layer3ppeWrapper.getCategory() != null) {
				predicates.add(criteriaBuilder.equal(root.get(Layer3PPEConstant.LAYER3_CATEGORY), layer3ppeWrapper.getCategory()));
			} 

			if (layer3ppeWrapper.getChartType() != null) {
				predicates.add(criteriaBuilder.equal(root.get(Layer3PPEConstant.LAYER3_CHART_TYPE), layer3ppeWrapper.getChartType()));
			} 

			if (layer3ppeWrapper.getLegendid_fk() != null) {
				predicates.add(criteriaBuilder.equal(root.get(Layer3PPEConstant.LEGENDID_FK), layer3ppeWrapper.getLegendid_fk()));
			} 

			if (layer3ppeWrapper.getIsRequiredOnUi() != null) {
				predicates.add(criteriaBuilder.equal(root.get(Layer3PPEConstant.LAYER3_IS_REQUIRED_ON_UI), layer3ppeWrapper.getIsRequiredOnUi()));
			} 

			if (layer3ppeWrapper.getEventColor() != null) {
				predicates.add(criteriaBuilder.equal(root.get(Layer3PPEConstant.LAYER3_EVENT_COLOUR), layer3ppeWrapper.getEventColor()));
			} 

			if (layer3ppeWrapper.getSourceType() != null) {
				predicates.add(criteriaBuilder.equal(root.get(Layer3PPEConstant.LAYER3_SOURCE_TYPE), layer3ppeWrapper.getSourceType()));
			} 

			if (layer3ppeWrapper.getValueType() != null) {
				predicates.add(criteriaBuilder.equal(root.get(Layer3PPEConstant.LAYER3_VALUE_TYPE), layer3ppeWrapper.getValueType()));
			} 

			if (layer3ppeWrapper.getSummary() != null) {
				predicates.add(criteriaBuilder.equal(root.get(Layer3PPEConstant.LAYER3_SUMMARY), layer3ppeWrapper.getSummary()));
			} 

			if (layer3ppeWrapper.getLayerName() != null) {
				predicates.add(criteriaBuilder.equal(root.get(Layer3PPEConstant.LAYER3_LAYER_NAME), layer3ppeWrapper.getLayerName()));
			} 

			if (layer3ppeWrapper.getKpiHierarchy()!= null) {
				predicates.add(criteriaBuilder.equal(root.get(Layer3PPEConstant.LAYER3_KPI_HIERARCHY), layer3ppeWrapper.getKpiHierarchy()));
			} 

			if (layer3ppeWrapper.getUnit()!= null) {
				predicates.add(criteriaBuilder.equal(root.get(Layer3PPEConstant.UNIT), layer3ppeWrapper.getUnit()));
			}

			 */
		}

		return predicates;
	}

	private CriteriaQuery<Layer3MetaData> addPredicateInCreateriaQuery(
			CriteriaQuery<Layer3MetaData> criteriaQuery, List<Predicate> predicates) {
		if (!predicates.isEmpty()) {
			criteriaQuery.where(predicates.toArray(new Predicate[] {}));
		}
		return criteriaQuery;
	}




	@Override
	public void updateLayer3MetaData(Layer3MetaData layer3MetaData) {
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaUpdate<Layer3MetaData> update = criteriaBuilder.createCriteriaUpdate(Layer3MetaData.class);
			Root<Layer3MetaData> root = update.from(Layer3MetaData.class);

			if (layer3MetaData.getId() == null) {
				this.create(layer3MetaData);
			} else {
				update.where(criteriaBuilder.equal(root.get(Layer3PPEConstant.ID), layer3MetaData.getId()));
				update.set(Layer3PPEConstant.LAYER3_KPI_DISPLAY_NAME, layer3MetaData.getKpiName());
				update.set(Layer3PPEConstant.LAYER3_HBASE_COLUMN_NAME, layer3MetaData.getHbaseColumnName());
				update.set(Layer3PPEConstant.LAYER3_SUMMARY_AGG_TYPE, layer3MetaData.getSummaryAggType());
				update.set(Layer3PPEConstant.LAYER3_DRIVE_AGG_TYPE, layer3MetaData.getDriveAggType());
				update.set(Layer3PPEConstant.LAYER3_TECH_HIERARCHY, layer3MetaData.getTechHierarchy());
				update.set(Layer3PPEConstant.LAYER3_CATEGORY, layer3MetaData.getCategory());
				update.set(Layer3PPEConstant.LAYER3_CHART_TYPE, layer3MetaData.getChartType());
				update.set(Layer3PPEConstant.LEGENDID_FK, layer3MetaData.getLegendid_fk());
				update.set(Layer3PPEConstant.LAYER3_IS_REQUIRED_ON_UI, layer3MetaData.getIsRequiredOnUi());
				update.set(Layer3PPEConstant.LAYER3_EVENT_COLOUR, layer3MetaData.getEventColor());
				update.set(Layer3PPEConstant.LAYER3_SOURCE_TYPE, layer3MetaData.getSourceType());
				update.set(Layer3PPEConstant.LAYER3_VALUE_TYPE, layer3MetaData.getValueType());
				update.set(Layer3PPEConstant.LAYER3_SUMMARY, layer3MetaData.getSummary());
				update.set(Layer3PPEConstant.LAYER3_LAYER_NAME, layer3MetaData.getLayerName());
				update.set(Layer3PPEConstant.LAYER3_KPI_HIERARCHY, layer3MetaData.getKpiHierarchy());
				update.set(Layer3PPEConstant.UNIT, layer3MetaData.getUnit());
				
				getEntityManager().createQuery(update).executeUpdate();
			}
		} catch (Exception e) {
			logger.error("Exception while Updating KPIBuilderMeta {}", ExceptionUtils.getStackTrace(e));
		}

	}

	@Override
	public void deleteLayer3MetaData(Layer3MetaData layer3MetaData) {
		if (layer3MetaData.getId() != null) {
			this.deleteByPk(layer3MetaData.getId());
		}
	}

}

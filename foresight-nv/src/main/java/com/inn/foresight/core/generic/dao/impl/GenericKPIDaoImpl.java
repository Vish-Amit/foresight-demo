package com.inn.foresight.core.generic.dao.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.generic.dao.IGenericKPIDao;
import com.inn.foresight.core.generic.model.KPISummaryDetail;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.generic.wrapper.GenericCvFtWrapper;

@Repository("GenericKPIDaoImpl")
public class GenericKPIDaoImpl extends HibernateGenericDao<Integer, KPISummaryDetail> implements IGenericKPIDao {

	public GenericKPIDaoImpl() {
		super(KPISummaryDetail.class);
	}

	private Logger logger = LogManager.getLogger(GenericKPIDaoImpl.class);

	@Override
	public List<GenericCvFtWrapper> getDataForClusterviewAndFloatingTable(String band, String environment, String datasource, List<String> date, String module, boolean isLatest, String kpiName,
			String geographyType, Set<String> geographyList,String technology,String operatorName) {
		List<GenericCvFtWrapper> listOfData = new ArrayList<>();
		try {
			CriteriaQuery<GenericCvFtWrapper> criteriaQuery = getCriteriabuilderQuery(band, environment,
					datasource, date, module, isLatest, kpiName, geographyType, geographyList,technology,operatorName);
			Query query = getEntityManager().createQuery(criteriaQuery);
			return query.getResultList();
		} catch (Exception e) {
			logger.info("Exception in getDataForClusterviewAndFloatingTable--> {} ", ExceptionUtils.getStackTrace(e));
			return listOfData;
		}
	}

	public CriteriaQuery<GenericCvFtWrapper> getCriteriabuilderQuery(String band, String environment, String datasource,
			List<String> date, String module, boolean isLatest, String kpiName,
			String geographyType, Set<String> geographyList,String technology, String operatorName) {

		List<Predicate> whereClausePredicates = new ArrayList<>();
		List<Selection<?>> listOfSelections = new ArrayList<>();
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<GenericCvFtWrapper> criteriaQuery = criteriaBuilder.createQuery(GenericCvFtWrapper.class);

		Root<KPISummaryDetail> rootElement = criteriaQuery.from(KPISummaryDetail.class);

		getPredicatesAndSelectionsForGeography(criteriaBuilder, whereClausePredicates, listOfSelections, rootElement, geographyType, geographyList);

		getPredicatesForClusterviewAndFloatingTable(criteriaBuilder, whereClausePredicates,
				rootElement, date, band, datasource, environment, kpiName, module, isLatest, geographyType,technology,operatorName);

		getListOfSelections(geographyType, listOfSelections, criteriaBuilder, rootElement);

		getMultiSelectQuery(geographyType, whereClausePredicates, listOfSelections, criteriaBuilder, criteriaQuery, rootElement);
		return criteriaQuery;
	}

	private void getMultiSelectQuery(String geographyType, List<Predicate> whereClausePredicates, List<Selection<?>> listOfSelections, CriteriaBuilder criteriaBuilder,
			CriteriaQuery<GenericCvFtWrapper> criteriaQuery, Root<KPISummaryDetail> rootElement) {
		if (geographyType.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L4fk)) {
			criteriaQuery.multiselect(listOfSelections).where(whereClausePredicates.toArray(new Predicate[] {}))
					.groupBy(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.NAME));
		} else if (geographyType.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L3fk)) {
			criteriaQuery.multiselect(listOfSelections).where(whereClausePredicates.toArray(new Predicate[] {}))
					.groupBy(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.NAME));

		} else if (geographyType.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L2fk)) {
			criteriaQuery.multiselect(listOfSelections).where(whereClausePredicates.toArray(new Predicate[] {}))
					.groupBy(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.GEOGRAPHY_L2fk).get(ForesightConstants.NAME));
		} else if (geographyType.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L1fk)) {
			criteriaQuery.multiselect(listOfSelections).where(whereClausePredicates.toArray(new Predicate[] {})).groupBy(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk)
					.get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.GEOGRAPHY_L2fk).get(ForesightConstants.GEOGRAPHY_L1fk).get(ForesightConstants.NAME));
		}
	}

	private void getPredicatesAndSelectionsForGeography(CriteriaBuilder criteriaBuilder, List<Predicate> whereClausePredicates, List<Selection<?>> listOfSelections, Root<KPISummaryDetail> rootElement,
			String geographyType, Set<String> geographyNameList) {
		getViewportTagged(criteriaBuilder, whereClausePredicates, listOfSelections, rootElement, geographyType, geographyNameList);
	}

	private void getListOfSelections(String geographyType, List<Selection<?>> listOfSelections, CriteriaBuilder criteriaBuilder,
			Root<KPISummaryDetail> rootElement) {

		Expression<Double> totalSamples = criteriaBuilder.sum(rootElement.get(ForesightConstants.totalsamples));
		Expression<Double> sum = criteriaBuilder.sum(rootElement.get(ForesightConstants.SUM_VALUE));
		listOfSelections.add(totalSamples);
		listOfSelections.add(criteriaBuilder.sum(rootElement.get(ForesightConstants.validsamples)));
		listOfSelections.add(criteriaBuilder.quot(sum, totalSamples));
		listOfSelections.add(criteriaBuilder.sum(rootElement.get(ForesightConstants.RANGE0)));
		listOfSelections.add(criteriaBuilder.sum(rootElement.get(ForesightConstants.RANGE1)));
		listOfSelections.add(criteriaBuilder.sum(rootElement.get(ForesightConstants.RANGE2)));
		if(rootElement.get(ForesightConstants.TECHNOLOGY) != null) {
			listOfSelections.add(rootElement.get(ForesightConstants.TECHNOLOGY));
		}
		if(rootElement.get(ForesightConstants.OPERATOR) != null) {
			listOfSelections.add(rootElement.get(ForesightConstants.OPERATOR));
		}
	}

	/**
	 * Gets the predicates for criteria builder for CA.
	 *
	 * @param criteriaBuilder
	 *            the criteria builder
	 * @param listOfPredicates
	 *            the ca viewport predicates
	 * @param rootElement
	 *            the ca data
	 * @param date
	 *            the data source date
	 * @param band
	 *            the band
	 * @param dataSource
	 *            the data source
	 * @param environment

	 * @param operatorName 
	 * @return the predicates for criteria builder for CA
	 * @throws ParseException
	 */
	private void getPredicatesForClusterviewAndFloatingTable(CriteriaBuilder criteriaBuilder, List<Predicate> listOfPredicates, Root<KPISummaryDetail> rootElement, List<String> date, String band,
			String dataSource, String environment, String kpiname, String module, boolean isLatest, String geographyType
			,String technology, String operatorName) {

		logger.info("band {} , dataSource {} , date {} , environment {} , kpiname {} , module {} , isLatest {} , geographyType {}", band, dataSource, date, environment, kpiname, module, isLatest,
				geographyType);
		listOfPredicates.add(criteriaBuilder.equal(rootElement.get(ForesightConstants.BAND), band));
		listOfPredicates.add(criteriaBuilder.equal(rootElement.get(ForesightConstants.DATASOURCE.toLowerCase()), dataSource));

		listOfPredicates.add(criteriaBuilder.equal(rootElement.get(ForesightConstants.KPI_NAME), kpiname.toUpperCase()));

		listOfPredicates.add(criteriaBuilder.equal(rootElement.get(ForesightConstants.MODULE), module));
		if (environment != null) {
			if(environment.equalsIgnoreCase(ForesightConstants.EMPTYSPACE))
			{
				environment = ForesightConstants.COM_ENVIRONMENT;
			}
			listOfPredicates.add(criteriaBuilder.equal(rootElement.get(ForesightConstants.ENVIRONMENT), environment));
		}
		if (isLatest) {
			listOfPredicates.add(criteriaBuilder.equal(rootElement.get(ForesightConstants.ISLATEST), ForesightConstants.ONE));
		} else {
			listOfPredicates.add(criteriaBuilder.between(rootElement.get(ForesightConstants.DATE), date.get(0), date.get(1)));
		}
		if( technology != null) {
			listOfPredicates.add(criteriaBuilder.equal(rootElement.get(ForesightConstants.TECHNOLOGY), technology));
		}
		logger.info("Operator Name {} ,is String {}",operatorName,operatorName instanceof String);
		if(Utils.hasValidValue(operatorName)) {
			listOfPredicates.add(criteriaBuilder.like(rootElement.get(ForesightConstants.OPERATOR), operatorName));
		}	
		logger.info("Paramater values are for cluster view Technology {},environment {},module {} , KPI_NAME {} ,DATASOURCE {}",technology,environment,module,kpiname,dataSource);
	}

	private void getViewportTagged(CriteriaBuilder criteriaBuilder, List<Predicate> listOfPredicates, List<Selection<?>> listOfSelections, Root<KPISummaryDetail> rootElement, String geographyType,
			Set<String> geographyNameSet) {
		if (geographyType.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L4fk)) {
			if (geographyNameSet != null && !geographyNameSet.isEmpty()) {
				listOfPredicates.add(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.NAME).in(geographyNameSet));
			}
			getLatLngSelectionForL4(criteriaBuilder, listOfSelections, rootElement);
		} else if (geographyType.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L3fk)) {
			if (geographyNameSet != null && !geographyNameSet.isEmpty()) {
				listOfPredicates.add(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.NAME).in(geographyNameSet));
			}
			getLatLngSelectionForL3(criteriaBuilder, listOfSelections, rootElement);
		} else if (geographyType.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L2fk)) {
			if (geographyNameSet != null && !geographyNameSet.isEmpty()) {
				listOfPredicates.add(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.GEOGRAPHY_L2fk).get(ForesightConstants.NAME).in(geographyNameSet));
			}
			getLatLngSelectionForL2(criteriaBuilder, listOfSelections, rootElement);
		} else if (geographyType.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L1fk)) {
			if (geographyNameSet != null && !geographyNameSet.isEmpty()) {
				listOfPredicates.add(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.GEOGRAPHY_L2fk).get(ForesightConstants.GEOGRAPHY_L1fk).get(ForesightConstants.NAME).in(geographyNameSet));
			}
			getLatLngSelectionForL1(criteriaBuilder, listOfSelections, rootElement);
		}
	}

	private void getLatLngSelectionForL1(CriteriaBuilder criteriaBuilder, List<Selection<?>> listOfSelections, Root<KPISummaryDetail> rootElement) {

		listOfSelections.add(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.GEOGRAPHY_L2fk).get(ForesightConstants.GEOGRAPHY_L1fk)
				.get(ForesightConstants.NAME));

		listOfSelections.add(criteriaBuilder.avg(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.GEOGRAPHY_L2fk)
				.get(ForesightConstants.GEOGRAPHY_L1fk).get(ForesightConstants.LATITUDE)));
		listOfSelections.add(criteriaBuilder.avg(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.GEOGRAPHY_L2fk)
				.get(ForesightConstants.GEOGRAPHY_L1fk).get(ForesightConstants.LONGITUDE)));
		listOfSelections.add(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.GEOGRAPHY_L2fk).get(ForesightConstants.GEOGRAPHY_L1fk)
				.get(ForesightConstants.DISPLAY_NAME));
	}

	private void getLatLngSelectionForL2(CriteriaBuilder criteriaBuilder, List<Selection<?>> listOfSelections, Root<KPISummaryDetail> rootElement) {

		listOfSelections.add(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.GEOGRAPHY_L2fk).get(ForesightConstants.NAME));
		listOfSelections.add(
				criteriaBuilder.avg(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.GEOGRAPHY_L2fk).get(ForesightConstants.LATITUDE)));
		listOfSelections.add(criteriaBuilder
				.avg(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.GEOGRAPHY_L2fk).get(ForesightConstants.LONGITUDE)));

		listOfSelections.add(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.GEOGRAPHY_L2fk).get(ForesightConstants.DISPLAY_NAME));

	}

	private void getLatLngSelectionForL3(CriteriaBuilder criteriaBuilder, List<Selection<?>> listOfSelections, Root<KPISummaryDetail> rootElement) {
		listOfSelections.add(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.NAME));
		listOfSelections.add(criteriaBuilder.avg(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.LATITUDE)));
		listOfSelections.add(criteriaBuilder.avg(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.LONGITUDE)));
		listOfSelections.add(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.DISPLAY_NAME));

	}

	private void getLatLngSelectionForL4(CriteriaBuilder criteriaBuilder, List<Selection<?>> listOfSelections, Root<KPISummaryDetail> rootElement) {
		listOfSelections.add(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.NAME));
		listOfSelections.add(criteriaBuilder.avg(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.LATITUDE)));
		listOfSelections.add(criteriaBuilder.avg(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.LONGITUDE)));
		listOfSelections.add(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.DISPLAY_NAME));
	}
	
	//@Transactional
	@Override
	public List<Object[]> getKPIWeeklyData(String geographyLevel,
	      Integer geographyId, Date latestMondayDate, Date startDate, String band, String kpi, String module, String competitor){
	  try {
	      Query query = getEntityManager().createNamedQuery("getWeeklyKPIData");
	      query.setParameter("startMonday", startDate);
	      query.setParameter("latestMonday", latestMondayDate);
	      query.setParameter("band", band);
	      query.setParameter("kpi", kpi);
	      query.setParameter("module", module);
	      if (!ForesightConstants.GEOGRAPHY_L0.equalsIgnoreCase(geographyLevel)) {
	        enableGeographyFilter(geographyLevel, geographyId);
	      }
	      if(competitor != null) {
	        enableOperatorFilter(competitor);
	      }
	      return query.getResultList();
	    } catch (Exception e) {
	      logger.error("Exception in getting coverage area percent : {}",
	          ExceptionUtils.getStackTrace(e));
	    }
	    return new ArrayList<>();
	}
	
	private void enableOperatorFilter(String operatorName) {
	  Session session = (Session) getEntityManager().getDelegate();
	  session.enableFilter("filterKPISummaryOperator").setParameter("operator", operatorName);
	}
	
	
	private void enableGeographyFilter(String geographyLevel, Integer geographyId) {
	    Session session = (Session) getEntityManager().getDelegate();
	    switch (geographyLevel) {
	      case ForesightConstants.GeographyL1:
	        session.enableFilter("filterKPISummaryL1").setParameter("id", geographyId);
	        break;
	      case  ForesightConstants.GeographyL2:
	        session.enableFilter("filterKPISummaryL2").setParameter("id", geographyId);
	        break;
	      case  ForesightConstants.GeographyL3:
	        session.enableFilter("filterKPISummaryL3").setParameter("id", geographyId);
	        break;
	      case  ForesightConstants.GeographyL4:
	        session.enableFilter("filterKPISummaryL4").setParameter("id", geographyId);
	        break;
	      default:
	        break;
	    }
	  }

}

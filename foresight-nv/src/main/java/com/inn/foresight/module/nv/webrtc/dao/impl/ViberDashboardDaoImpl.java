package com.inn.foresight.module.nv.webrtc.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.module.nv.webrtc.constant.WebRTCConstant;
import com.inn.foresight.module.nv.webrtc.dao.IViberDashboardDao;
import com.inn.foresight.module.nv.webrtc.model.ViberDashboard;

@Repository("ViberDashboardDaoImpl")
public class ViberDashboardDaoImpl extends HibernateGenericDao<Integer, ViberDashboard> implements IViberDashboardDao {

	private Logger logger = LogManager.getLogger(ViberDashboardDaoImpl.class);

	public ViberDashboardDaoImpl() {
		super(ViberDashboard.class);
	}

	@Override
	public List<ViberDashboard> getViberDashboardData(String startDate, String endDate, String technology, String operator, String country,
							String nvModule, Boolean isLastSevenDayDataRequired) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ViberDashboard> criteriaQuery = criteriaBuilder.createQuery(ViberDashboard.class);
		Root<ViberDashboard> root = criteriaQuery.from(ViberDashboard.class);
		List<Predicate> predicateList = getPredicateList(sdf.parse(startDate),sdf.parse(endDate), technology, operator, country, nvModule,
				criteriaBuilder, root, isLastSevenDayDataRequired);
		criteriaQuery = addPredicateInCreateriaQuery(criteriaQuery, predicateList);
		List<ViberDashboard> resultList = getEntityManager().createQuery(criteriaQuery).getResultList();
		logger.info("Result Size ::: {}", resultList.size());
		return resultList;
	}

	private static List<Predicate> getPredicateList(Date startDate, Date endDate, String technology, String operator, String country, String nvModule,
					CriteriaBuilder criteriaBuilder, Root root, Boolean isLastSevenDayDataRequired) {
		List<Predicate> predicates = new ArrayList<>();
		if (startDate != null && endDate != null && !isLastSevenDayDataRequired) {
			predicates.add(criteriaBuilder.between(root.<Date>get("creationDate"), startDate, endDate));
		}
		
	
		if (isLastSevenDayDataRequired) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DATE, -6);
			Date previousSevenDate = cal.getTime();
			predicates.add(criteriaBuilder.between(root.<Date>get("creationDate"), previousSevenDate, endDate));
		}

		if (technology != null && !technology.equalsIgnoreCase(WebRTCConstant.ALL)) {
			predicates.add(criteriaBuilder.equal(root.get("technology"), technology));
		}

		if (operator != null && !operator.equalsIgnoreCase(WebRTCConstant.ALL)) {
			predicates.add(criteriaBuilder.equal(root.get("operatorName"), operator));
		}

		if (country != null && !country.equalsIgnoreCase(WebRTCConstant.ALL)) {
			predicates.add(criteriaBuilder.equal(root.get("countryName"), country));
		}

		if (nvModule != null) {
			predicates.add(criteriaBuilder.equal(root.get("nvModule"), nvModule));
		}
		return predicates;
	}

	public static CriteriaQuery<ViberDashboard> addPredicateInCreateriaQuery(
			CriteriaQuery<ViberDashboard> criteriaQuery, List<Predicate> predicates) {
		criteriaQuery.where(predicates.toArray(new Predicate[] {}));
		return criteriaQuery;
	}
}

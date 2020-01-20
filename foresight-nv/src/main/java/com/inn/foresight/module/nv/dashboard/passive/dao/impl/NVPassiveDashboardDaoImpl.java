package com.inn.foresight.module.nv.dashboard.passive.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.dashboard.passive.dao.INVPassiveDashboardDao;
import com.inn.foresight.module.nv.dashboard.passive.model.NVPassiveDashboard;
import com.inn.foresight.module.nv.dashboard.passive.utils.NVPassiveConstants;

@Repository("NVPassiveDashboardDaoImpl")
public class NVPassiveDashboardDaoImpl extends HibernateGenericDao<Integer, NVPassiveDashboard>
		implements INVPassiveDashboardDao {

	static Map<String, Integer> globalCountMap = new HashMap<>();
	private Logger logger = LoggerFactory.getLogger(NVPassiveDashboardDaoImpl.class);

	@Autowired
	public NVPassiveDashboardDaoImpl() {
		super(NVPassiveDashboard.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<NVPassiveDashboard> getCombinePassiveRecords(String date, String level, Integer geographyId,String duplexType,String tagType,String appName) {
		logger.info("inside the method GetCombinePassiveRecords Dao  date level geography Id {} {} {}", date, level,
				geographyId);

		List<NVPassiveDashboard> list = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(ForesightConstants.DATE_FORMAT_DDMMYY);
			Date sampleDate = formatter.parse(date);
			String queryString = getQueryByGeographyNameandLevel(level);
			logger.info(
					"Going to fire query inside getCombinePassiveRecords methods for "
							+ "geography id {} , creation time {} , level {} , query {}",
					geographyId, sampleDate, level, queryString);
			if (queryString != null) {
				Query query = getEntityManager().createNamedQuery(queryString);
				if (!level.equalsIgnoreCase(NVPassiveConstants.GEOGRAPHY_ALL)) {
					query.setParameter(NVPassiveConstants.GEOGRAPHY_ID, geographyId);
					query.setParameter(NVPassiveConstants.SAMPLE_DATE, sampleDate);
				} else {
					query.setParameter(NVPassiveConstants.SAMPLE_DATE, sampleDate);
				}
				String enableFilter = enableFilter(duplexType, tagType);
				String appFilterName = enableApplicationFilter(appName);
				list = query.getResultList();
				disableFilter(enableFilter);
				disableFilter(appFilterName);
				logger.info("Got Response from db size ::::::=====> {}", list.size());
			}
		} catch (Exception e) {
			logger.error("Error inside the method getCombinePassiveRecords {}", Utils.getStackTrace(e));
		}
		return list;
	}

	private void disableFilter(String filterName) {
		if (Utils.hasValidValue(filterName)) {
			Session s = (Session) getEntityManager().getDelegate();
			s.disableFilter(filterName);
		}
	}
	
	private String enableFilter(String duplexType, String tagType) {
		logger.info("Going to enable filters for duplex: {} , Tag Type: {}",duplexType,tagType);
		Session s = (Session) getEntityManager().getDelegate();
		if (tagType.equalsIgnoreCase(NVPassiveConstants.ALL) && !duplexType.equalsIgnoreCase(NVPassiveConstants.ALL)) {
			s.enableFilter(NVPassiveConstants.FILTER_DUPLEX_TYPE).setParameter(NVPassiveConstants.DUPLEX_TYPE,
					duplexType);
			return NVPassiveConstants.FILTER_DUPLEX_TYPE;
		}
		else if (!tagType.equalsIgnoreCase(NVPassiveConstants.ALL) && duplexType.equalsIgnoreCase(NVPassiveConstants.ALL)) {
			s.enableFilter(NVPassiveConstants.FILTER_TAG_TYPE).setParameter(NVPassiveConstants.TAG_TYPE,
					tagType);
			return NVPassiveConstants.FILTER_TAG_TYPE;
		}
		else if (!tagType.equalsIgnoreCase(NVPassiveConstants.ALL) && !duplexType.equalsIgnoreCase(NVPassiveConstants.ALL)) {
			s.enableFilter(NVPassiveConstants.FILTER_DUPLEX_TAG_TYPE).setParameter(NVPassiveConstants.TAG_TYPE,tagType).setParameter(NVPassiveConstants.DUPLEX_TYPE,duplexType);
			return NVPassiveConstants.FILTER_DUPLEX_TAG_TYPE;
		}
		else if (tagType.equalsIgnoreCase(NVPassiveConstants.ALL) && duplexType.equalsIgnoreCase(NVPassiveConstants.ALL)) {
			s.enableFilter(NVPassiveConstants.FILTER_PAN_LEVEL_DATA);
			return NVPassiveConstants.FILTER_PAN_LEVEL_DATA;
		}
		return null;
	}

	private String getQueryByGeographyNameandLevel(String geographyLevel) {
		if (geographyLevel != null) {
			if (geographyLevel.equals(NVPassiveConstants.GEOGRAPHY_L1)) {
				return "getL1WisePassiveData";
			} else if (geographyLevel.equals(NVPassiveConstants.GEOGRAPHY_L2)) {
				return "getL2WisePassiveData";
			} else if (geographyLevel.equals(NVPassiveConstants.GEOGRAPHY_L3)) {
				return "getL3WisePassiveData";
			} else if (geographyLevel.equals(NVPassiveConstants.GEOGRAPHY_L4)) {
				return "getL4WisePassiveData";
			} else if (geographyLevel.equals(NVPassiveConstants.GEOGRAPHY_ALL)) {
				return "getAllWisePassiveData";
			}
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<NVPassiveDashboard> getLastSevenDaysData(String date, String level, Integer geographyId,String duplexType,String tagType,String appName) {
		List<NVPassiveDashboard> list = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(ForesightConstants.DATE_FORMAT_DDMMYY);
			Date endTime = formatter.parse(date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(endTime);
			cal.add(Calendar.DATE, -6);
			Date startTime = cal.getTime();
			String queryString = getQueryForSevenDayData(level);

			logger.info(
					"Going to get 7 Days Records Based on geography id {} ,"
							+ " start Date {} ,  end Date {} ,level {} , query Name {}",
					geographyId, startTime, endTime, level, queryString);

			if (queryString != null) {
				Query query = getEntityManager().createNamedQuery(queryString);
				if (!level.equalsIgnoreCase(NVPassiveConstants.GEOGRAPHY_ALL)) {
					query.setParameter(NVPassiveConstants.GEOGRAPHY_ID, geographyId);
					query.setParameter(NVPassiveConstants.START_DATE, startTime);
					query.setParameter(NVPassiveConstants.END_DATE, endTime);
				} else {
					query.setParameter(NVPassiveConstants.START_DATE, startTime);
					query.setParameter(NVPassiveConstants.END_DATE, endTime);
				}
				String enableFilter = enableFilter(duplexType, tagType);
				String appFilterName = enableApplicationFilter(appName);
				list = query.getResultList();
				disableFilter(enableFilter);
				disableFilter(appFilterName);
				logger.info("Got Response Result Size::::::=====> {}", list.size());
			}
		} catch (Exception e) {
			logger.error("Error inside the method getLastSevenDaysData {}", Utils.getStackTrace(e));
		}
		return list;
	}

	private String enableApplicationFilter(String appName) {
		if (Utils.hasValidValue(appName)) {
			logger.info("Going to apply appName filter {}",appName);
			Session s = (Session) getEntityManager().getDelegate();
			s.enableFilter(NVPassiveConstants.FILTER_APPLICATION_SOURCE).setParameter(NVPassiveConstants.APP_NAME, appName);
			return NVPassiveConstants.FILTER_APPLICATION_SOURCE;
		}
		return null;
	}

	private String getQueryForSevenDayData(String level) {
		if (level != null) {
			if (level.equals(NVPassiveConstants.GEOGRAPHY_L1)) {
				return NVPassiveConstants.GEOGRAPHY_L1_UC;
			} else if (level.equals(NVPassiveConstants.GEOGRAPHY_L2)) {
				return NVPassiveConstants.GEOGRAPHY_L2_UC;
			} else if (level.equals(NVPassiveConstants.GEOGRAPHY_L3)) {
				return NVPassiveConstants.GEOGRAPHY_L3_UC;
			} else if (level.equals(NVPassiveConstants.GEOGRAPHY_L4)) {
				return NVPassiveConstants.GEOGRAPHY_L4_UC;
			} else if (level.equals(NVPassiveConstants.GEOGRAPHY_ALL)) {
				return NVPassiveConstants.GEOGRAPHY_ALL_UC;
			}
		}
		return null;

	}
}

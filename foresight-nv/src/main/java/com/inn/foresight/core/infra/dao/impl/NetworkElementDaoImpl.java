package com.inn.foresight.core.infra.dao.impl;

import static com.inn.foresight.core.infra.utils.InfraConstants.NE_NEID_KEY;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.Subquery;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
//import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.plexus.util.ExceptionUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.encoder.AESUtils;
import com.inn.commons.lang.MapUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.utils.ExceptionUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.core.infra.dao.INetworkElementDao;
import com.inn.foresight.core.infra.model.NEBandDetail;
import com.inn.foresight.core.infra.model.NELocation;
import com.inn.foresight.core.infra.model.NETaskDetail;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.foresight.core.infra.model.RANDetail;
import com.inn.foresight.core.infra.model.WifiSiteDetail;
import com.inn.foresight.core.infra.utils.CriteriaUtils;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.InfraUtils;
import com.inn.foresight.core.infra.utils.enums.DayOneStatus;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.NELType;
import com.inn.foresight.core.infra.utils.enums.NEStatus;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.utils.enums.Technology;
import com.inn.foresight.core.infra.utils.enums.Vendor;
import com.inn.foresight.core.infra.wrapper.MicroSiteDataWrapper;
import com.inn.foresight.core.infra.wrapper.NEDetailWrapper;
import com.inn.foresight.core.infra.wrapper.NETaskDetailWrapper;
import com.inn.foresight.core.infra.wrapper.NeDataWrapper;
import com.inn.foresight.core.infra.wrapper.NetworkElementCellDetailWrapper;
import com.inn.foresight.core.infra.wrapper.NetworkElementOnAirJsiWrapper;
import com.inn.foresight.core.infra.wrapper.NetworkElementWrapper;
import com.inn.foresight.core.infra.wrapper.NetworkHierarchyDetail;
import com.inn.foresight.core.infra.wrapper.SiteCountWrapper;
import com.inn.foresight.core.infra.wrapper.SiteEmsDetailsWrapper;
import com.inn.foresight.core.infra.wrapper.SiteGeographicalDetail;
import com.inn.foresight.core.infra.wrapper.TotalSiteLayerWiseWrapper;
import com.inn.product.security.spring.userdetails.CustomerInfo;
import com.inn.product.um.geography.model.GeographyL1;

/**
 * The Class NetworkElementDaoImpl.
 */
@Repository("NetworkElementDaoImpl")
public class NetworkElementDaoImpl extends HibernateGenericDao<Integer, NetworkElement> implements INetworkElementDao {

	/** The logger. */
	private static Logger logger = LogManager.getLogger(NetworkElementDaoImpl.class);

	/**
	 * The Map neId with Site Name.
	 */
	private Map<String, String> neIdWithSiteName;

	@Autowired
	private CustomerInfo customerInfo;

	/**
	 * Instantiates a new network element dao impl.
	 */
	public NetworkElementDaoImpl() {
		super(NetworkElement.class);
	}

	/**
	 * Creates the.
	 *
	 * @param networkElement the network element
	 * @return the network element
	 * @throws DaoException the dao exception
	 */
	@Override
	public NetworkElement create(NetworkElement networkElement) {
		return super.create(networkElement);
	}

	/**
	 * Update in new transaction.
	 *
	 * @param networkElement the network element
	 * @return the network element
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NetworkElement updateInNewTransaction(NetworkElement networkElement) {
		logger.info("Going to update NetworkElement In New Transaction");
		try {
			return super.update(networkElement);
		} catch (Exception e) {
			logger.error("Error occured in update NetworkElement In New Transaction @cause : {}",
					Utils.getStackTrace(e));
			return null;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public NetworkElement findById(Integer nePk) {
		logger.info("Going to get NetworkElement By nePk : {}", nePk);
		try {
			return super.findByPk(nePk);
		} catch (Exception e) {
			logger.error("Error occured in get NetworkElement @cause : {}", Utils.getStackTrace(e));
			return null;
		}
	}

	@Override
	public List<NEDetailWrapper> getNEDetail() {
		try {
			return getEntityManager().createNamedQuery("getNetworkElementByNeId").getResultList();
		} catch (NonUniqueResultException e) {
			logger.error("Non Unique Result Exception in getting overview, err msg= {}", e.getMessage());
			throw new DaoException(ExceptionUtil.generateExceptionCode("Dao", InfraConstants.NETWORKELEMENT_TABLE, e));
		} catch (Exception e) {
			logger.error("Exception in getting overview, err msg= {}", Utils.getStackTrace(e));
			throw new DaoException(ExceptionUtil.generateExceptionCode("Dao", InfraConstants.NETWORKELEMENT_TABLE, e));
		}
	}

	@Override
	public SiteGeographicalDetail getSiteOverviewDetailData(String neName) {
		SiteGeographicalDetail siteDetailWrapper = new SiteGeographicalDetail();
		try {
			Query query = getEntityManager().createNamedQuery("getSiteOverviewDetailData");
			query.setParameter(ForesightConstants.NE_NAME, neName);
			logger.info("going to get result for query");
			List<SiteGeographicalDetail> list = query.getResultList();
			if (list != null && list.size() > 0) {
				siteDetailWrapper = list.get(0);
			}
		} catch (NonUniqueResultException e) {
			logger.error("Non Unique Result Exception in getting overview, err msg= {}", e.getMessage());
			throw new DaoException(ExceptionUtil.generateExceptionCode("Dao", InfraConstants.NETWORKELEMENT_TABLE, e));
		} catch (Exception e) {
			// logger.error("error in getSiteOverviewDetailData, err = {}",
			// ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtil.generateExceptionCode("Dao", InfraConstants.NETWORKELEMENT_TABLE, e));
		}
		return siteDetailWrapper;
	}

	@Override
	public NetworkElement getNetworkElementByDomainVendorTechnologyAndNEname(String nename, Domain domain,
			Vendor vendor, Technology technology) {
		logger.info("Going to get getNetworkElemnetByNename by sapId {} ", nename);
		NetworkElement networkElement = null;
		try {
			Query query = getEntityManager().createNamedQuery("getNEBySapId")
					.setParameter(ForesightConstants.SAPID_CAMEL, nename)
					.setParameter(ForesightConstants.DOMAIN_SMALL, domain)
					.setParameter(ForesightConstants.VENDOR_SMALL, vendor)
					.setParameter(ForesightConstants.TECHNOLOGY, technology);
			networkElement = (NetworkElement) query.getSingleResult();
			if (networkElement != null) {
				logger.info("Returning NetworkElement with id {}", networkElement.getId());
			}
		} catch (NoResultException e) {
			logger.warn("Error while getNetworkElement ByNename,err msg {}", e.getMessage());
		} catch (IllegalStateException | PersistenceException e) {
			logger.warn("Error while getNetworkElementByNename,err msg{}", e.getMessage());
		}
		return networkElement;
	}

	/**
	 * Gets the NE for small site.
	 *
	 * @return the NE for small site
	 */
	@Override
	public List<NetworkElement> getNEForSmallSite() {
		logger.info("Going to getNEForSmallSite");
		List<NetworkElement> neElements = Collections.emptyList();
		try {
			Query query = getEntityManager().createNamedQuery("getNEForSmallSite");
			neElements = query.getResultList();
		} catch (IllegalStateException e) {
			logger.warn("IllegalStateException while getNEForSmallSite,err msg{}", e.getMessage());
		} catch (TransactionRequiredException e) {
			logger.warn("TransactionRequiredException while getNEForSmallSite,err msg{}", e.getMessage());
		} catch (QueryTimeoutException e) {
			logger.warn("QueryTimeoutException while getNEForSmallSite,err msg{}", e.getMessage());
		} catch (PersistenceException e) {
			logger.warn("PersistenceException while getNEForSmallSite,err msg{}", e.getMessage());
		}
		logger.info("Returning getNEForSmallSite list size {}", neElements.size());
		return neElements;
	}

	/**
	 * Gets the network element by sap id.
	 *
	 * @param sapId the sap id
	 * @return the network element by sap id
	 */
	@Override
	@Deprecated
	public List<NetworkElement> getNetworkElementBySapId(List<String> sapId) {
		logger.info("Going to getNEDetail By SapId {}", sapId);
		try {
			Query query = getEntityManager().createNamedQuery("getNetworkElementBySapIdList");
			query.setParameter(ForesightConstants.SAPID_CAMEL, sapId);
			return query.getResultList();
		} catch (Exception e) {
			logger.info("Error in getNetworkElementBySapId sapid : {} {} ", sapId, Utils.getStackTrace(e));
		}
		return Collections.emptyList();
	}

	/**
	 * Gets the sap ids by geography L 4.
	 *
	 * @param geographyL4 the geography L 4
	 * @return the sap ids by geography L 4
	 */
	@Override
	public List<String> getSapIdsByGeographyL4(String geographyL4) {
		List<String> sapIds = null;
		try {
			Query query = getEntityManager().createNamedQuery("getDistinctSapIdsBygeographyL4Name");
			query.setParameter("geographyL4", geographyL4);
			sapIds = query.getResultList();
		} catch (IllegalArgumentException e) {
			logger.warn("IllegalArgumentException while getEMSWiseSitesCounts,err msg{}", e.getMessage());
		} catch (IllegalStateException e) {
			logger.warn("IllegalStateException while getEMSWiseSitesCounts,err msg{}", e.getMessage());
		} catch (QueryTimeoutException e) {
			logger.warn("QueryTimeoutException while getEMSWiseSitesCounts,err msg{}", e.getMessage());
		} catch (TransactionRequiredException e) {
			logger.warn("TransactionRequiredException while getEMSWiseSitesCounts,err msg{}", e.getMessage());
		} catch (PersistenceException e) {
			logger.warn("PersistenceException while getEMSWiseSitesCounts,err msg{}", e.getMessage());
		}
		return sapIds;
	}

	@Override
	public List<String> getCellIdsByGeographyL4(String geographyL4) {
		List<String> cellIds = null;
		try {
			Query query = getEntityManager().createNamedQuery("getCellIdsBygeographyL4Name");
			query.setParameter("geographyL4", geographyL4);
			cellIds = query.getResultList();
		} catch (IllegalArgumentException e) {
			logger.warn("IllegalArgumentException while getcellIdsBygeographyL4Name,err msg{}", e.getMessage());
		} catch (IllegalStateException e) {
			logger.warn("IllegalStateException while getcellIdsBygeographyL4Name,err msg{}", e.getMessage());
		} catch (QueryTimeoutException e) {
			logger.warn("QueryTimeoutException while getcellIdsBygeographyL4Name,err msg{}", e.getMessage());
		} catch (TransactionRequiredException e) {
			logger.warn("TransactionRequiredException while getcellIdsBygeographyL4Name,err msg{}", e.getMessage());
		} catch (PersistenceException e) {
			logger.warn("PersistenceException while getcellIdsBygeographyL4Name,err msg{}", e.getMessage());
		}
		return cellIds;
	}

	/**
	 * Search site by name.
	 *
	 * @param neName the ne name
	 * @return the network element
	 */
	@Override
	public NetworkElement searchSiteByName(String neName) {
		logger.info("Going to searchSiteByName by neName {}", neName);
		NetworkElement networkElement = null;
		try {
			Query query = getEntityManager().createNamedQuery("searchSiteByName")
					.setParameter(ForesightConstants.NE_NAME, neName);
			networkElement = (NetworkElement) query.getSingleResult();
		} catch (IllegalStateException | PersistenceException e) {
			logger.warn("Error while getExistingNESite,err msg{}", e.getMessage());
		}
		return networkElement;
	}

	@Override
	public NetworkElement searchSiteByFriendlyName(String friendlyname, NEType neType) {
		NetworkElement networkElement = null;
		Query query = getEntityManager().createNamedQuery("searchSiteByFriendlyName")
				.setParameter("friendlyname", friendlyname).setParameter("netype", neType);
		query.setMaxResults(1);
		networkElement = (NetworkElement) query.getSingleResult();
		return networkElement;

	}

	/**
	 * Gets the location for cell id mnc mcc.
	 *
	 * @return the location for cell id mnc mcc
	 */
	@Override
	public List<Object[]> getLocationForCellIdMncMcc() {
		logger.info("going to generate location detail map");
		List<Object[]> result = null;
		try {
		} catch (NoResultException e) {
			logger.warn(
					"NoResultException while Get Sites By Reigonal Cluster, Reigonal Center, City, CircleName,err msg{}",
					e.getMessage());
		} catch (NonUniqueResultException e) {
			logger.warn(
					"NonUniqueResultException  While Get Sites By Reigonal Cluster, Reigonal Center, City, CircleName,err msg{}",
					e.getMessage());
		} catch (QueryTimeoutException e) {
			logger.warn(
					"QueryTimeoutException while Get Sites By Reigonal Cluster, Reigonal Center, City, CircleName, err msg{}",
					e.getMessage());
		} catch (TransactionRequiredException e) {
			logger.warn(
					"TransactionRequiredException while Get Sites By Reigonal Cluster, Reigonal Center, City, CircleName,,err msg{}",
					e.getMessage());
		} catch (PersistenceException e) {
			logger.warn(
					"PersistenceException while Get Sites By Reigonal Cluster, Reigonal Center, City, CircleName,err msg{}",
					e.getMessage());
		} catch (Exception e) {
			logger.error("Error in getting location detail from Database : {} ", e.getMessage());
		}

		return result;
	}

	/**
	 * Gets the all sites count.
	 *
	 * @param siteName the site name
	 * @return the all sites count
	 */
	@Override
	public Long getAllSitesCount(String siteName, List<Integer> cellIds, List<Integer> sectors, List<String> bands,
			Vendor vendor, NEType neType) {
		Query query = getEntityManager().createNamedQuery("getAllSitesCount");
		query.setParameter(InfraConstants.SITE_NAME, "%" + siteName.trim().toUpperCase() + "%");

		return (Long) query.getResultList().get(0);

	}

	/**
	 * Gets the site table data for geography L 2.
	 *
	 * @param southWestLong the south west long
	 * @param southWestLat  the south west lat
	 * @param northEastLong the north east long
	 * @param northEastLat  the north east lat
	 * @param progressState the progress state
	 * @param neType        the ne type
	 * @param neFrequencies the ne frequencies
	 * @return the site table data for geography L 2
	 * @throws DaoException the dao exception
	 */
	@Override
	public List<NetworkElementWrapper> getSiteTableDataForGeographyL2(Double southWestLong, Double southWestLat,
			Double northEastLong, Double northEastLat, String progressState, NEType neType,
			List<String> neFrequencies) {
		List<NetworkElementWrapper> neDataList = new ArrayList<>();
		logger.info(
				"going to get Combined Sites Data For GeographyL2 for progressState={},neTypes={} , southWestLong={}, southWestLat={},northEastLong={} ,northEastLat={} ",
				progressState, neType, southWestLong, southWestLat, northEastLong, northEastLat);
		try {
			Query query = getEntityManager().createNamedQuery("getSiteTableDataForGeographyL2");
			query.setParameter(ForesightConstants.SOUTH_WEST_LONGITUDE, southWestLong);
			query.setParameter(ForesightConstants.SOUTH_WEST_LATITUDE, southWestLat);
			query.setParameter(ForesightConstants.NORTH_EAST_LONGITUDE, northEastLong);
			query.setParameter(ForesightConstants.NORTH_EAST_LATITUDE, northEastLat);
			query.setParameter(ForesightConstants.SITE_PROGRESS_STATE, progressState);
			query.setParameter(ForesightConstants.NE_TYPE, neType);
			neDataList = query.getResultList();
			if (neDataList.size() == 0) {
				logger.info("no data found for the query ");
			}
		} catch (Exception e) {
			logger.error("error in getSiteTableDataForGeographyL2, err = {}", Utils.getStackTrace(e));
			throw new DaoException(ExceptionUtil.generateExceptionCode("Dao", InfraConstants.NETWORKELEMENT_TABLE, e));
		}
		return neDataList;
	}

	/**
	 * Gets the site table data for geography L 3.
	 *
	 * @param southWestLong the south west long
	 * @param southWestLat  the south west lat
	 * @param northEastLong the north east long
	 * @param northEastLat  the north east lat
	 * @param progressState the progress state
	 * @param neType        the ne type
	 * @param neFrequencies the ne frequencies
	 * @return the site table data for geography L 3
	 * @throws DaoException the dao exception
	 */
	@Override
	public List<NetworkElementWrapper> getSiteTableDataForGeographyL3(Double southWestLong, Double southWestLat,
			Double northEastLong, Double northEastLat, String progressState, NEType neType,
			List<String> neFrequencies) {
		List<NetworkElementWrapper> neDataList = new ArrayList<>();
		logger.info(
				"going to get Combined Sites Data For GeographyL3 for progressState={},neTypes={} , southWestLong={}, southWestLat={},northEastLong={} ,northEastLat={} ",
				progressState, neType, southWestLong, southWestLat, northEastLong, northEastLat);
		try {
			Query query = getEntityManager().createNamedQuery("getSiteTableDataForGeographyL3");
			query.setParameter(ForesightConstants.SOUTH_WEST_LONGITUDE, southWestLong);
			query.setParameter(ForesightConstants.SOUTH_WEST_LATITUDE, southWestLat);
			query.setParameter(ForesightConstants.NORTH_EAST_LONGITUDE, northEastLong);
			query.setParameter(ForesightConstants.NORTH_EAST_LATITUDE, northEastLat);
			query.setParameter(ForesightConstants.SITE_PROGRESS_STATE, progressState);
			query.setParameter(ForesightConstants.NE_TYPE, neType);
			neDataList = query.getResultList();
			if (neDataList.size() == 0) {
				logger.info("no data found for the query");
			}
		} catch (Exception e) {
			logger.error("error in getSiteTableDataForGeographyL3, err = {}", Utils.getStackTrace(e));
			throw new DaoException(ExceptionUtil.generateExceptionCode("Dao", "NetworkElement", e));
		}
		return neDataList;
	}

	/**
	 * Gets the site table data for geography L 4.
	 *
	 * @param southWestLong the south west long
	 * @param southWestLat  the south west lat
	 * @param northEastLong the north east long
	 * @param northEastLat  the north east lat
	 * @param progressState the progress state
	 * @param neType        the ne type
	 * @param neFrequencies the ne frequencies
	 * @return the site table data for geography L 4
	 * @throws DaoException the dao exception
	 */
	@Override
	public List<NetworkElementWrapper> getSiteTableDataForGeographyL4(Double southWestLong, Double southWestLat,
			Double northEastLong, Double northEastLat, String progressState, NEType neType,
			List<String> neFrequencies) {
		List<NetworkElementWrapper> neDataList = new ArrayList<>();
		logger.info(
				"going to get Combined Sites Data For GeographyL4 for progressState={},neTypes={} , southWestLong={}, southWestLat={},northEastLong={} ,northEastLat={} ",
				progressState, neType, southWestLong, southWestLat, northEastLong, northEastLat);
		try {
			Query query = getEntityManager().createNamedQuery("getSiteTableDataForGeographyL4");
			query.setParameter(ForesightConstants.SOUTH_WEST_LONGITUDE, southWestLong);
			query.setParameter(ForesightConstants.SOUTH_WEST_LATITUDE, southWestLat);
			query.setParameter(ForesightConstants.NORTH_EAST_LONGITUDE, northEastLong);
			query.setParameter(ForesightConstants.NORTH_EAST_LATITUDE, northEastLat);
			query.setParameter(ForesightConstants.SITE_PROGRESS_STATE, progressState);
			query.setParameter(ForesightConstants.NE_TYPE, neType);
			neDataList = query.getResultList();
			if (neDataList.size() == 0) {
				logger.info("no data found for the query");
			}
		} catch (Exception e) {
			logger.error("error in getSiteTableDataForGeographyL4, err = {}", Utils.getStackTrace(e));
			throw new DaoException(ExceptionUtil.generateExceptionCode("Dao", "NetworkElement", e));
		}
		return neDataList;
	}

	/*  *//**
			 * Gets the site overview detail data.
			 *
			 * @param neName the ne name
			 * @return the site overview detail data
			 * @throws DaoException the dao exception
			 *//*
				 * @Override public SiteGeographicalDetail getSiteOverviewDetailData(String
				 * neName) throws DaoException { SiteGeographicalDetail siteDetailWrapper = new
				 * SiteGeographicalDetail(); try { Query query =
				 * getEntityManager().createNamedQuery("getSiteOverviewDetailData");
				 * query.setParameter(ForesightConstants.NE_NAME, neName);
				 * logger.info("going to get result for query"); List<SiteGeographicalDetail>
				 * list = query.getResultList(); if (list != null && list.size() > 0) {
				 * siteDetailWrapper = list.get(0); } } catch (NonUniqueResultException e) {
				 * logger. error("Non Unique Result Exception in getting overview, err msg= {}"
				 * , e.getMessage()); throw new
				 * DaoException(ExceptionUtil.generateExceptionCode("Dao", "NetworkElement",
				 * e)); } catch (Exception e) {
				 * logger.error("error in getSiteOverviewDetailData, err = {}",
				 * Utils.getStackTrace(e)); throw new
				 * DaoException(ExceptionUtil.generateExceptionCode("Dao", "NetworkElement",
				 * e)); } return siteDetailWrapper; }
				 */

	/**
	 * Gets the distinct band by domain vendor tech.
	 *
	 * @param domain     the domain
	 * @param vendor     the vendor
	 * @param technology the technology
	 * @return the distinct band by domain vendor tech
	 * @throws DaoException the dao exception
	 */
	@Override
	public List<Object[]> getDistinctBandByDomainVendorTech(String domain, String vendor, String technology) {
		logger.info("Going to get Distinct Band By Domain {}, Vendor {}, Technology {}", domain, vendor, technology);
		List<Object[]> result = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getDistinctBandByDomainVendorTech");
			query.setParameter(ForesightConstants.DOMAIN_SMALL, Domain.valueOf(domain.toUpperCase()));
			query.setParameter(ForesightConstants.VENDOR_SMALL, Vendor.valueOf(vendor.toUpperCase()));
			query.setParameter(ForesightConstants.TECH, Technology.valueOf(technology.toUpperCase()));
			result = query.getResultList();
			logger.info("Retruning list size :{}", result.size());
		} catch (PersistenceException pe) {
			logger.warn("PersistenceException while get Distinct Band,err msg {}", pe);
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return result;
	}

	/**
	 * Gets the distinct version.
	 *
	 * @param domain the domain
	 * @param vendor the vendor
	 * @return the distinct version
	 * @throws DaoException the dao exception
	 */
	@Override
	public List<String> getDistinctVersion(String domain, String vendor) {
		logger.info("Going to getDistinctVersion");
		List<String> result = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getDistinctVersion");
			query.setParameter(InfraConstants.NE_DOMAIN_KEY, Domain.valueOf(domain.toUpperCase()));
			query.setParameter(InfraConstants.VENDOR_KEY, Vendor.valueOf(vendor.toUpperCase()));
			result = query.getResultList();
			logger.info("Retruning succesfuly from getDistinctVersion ,list size :{}", result.size());
		} catch (IllegalStateException e) {
			logger.warn("IllegalStateException while getDistinctVersion,err msg{}", e.getMessage());
		} catch (QueryTimeoutException e) {
			logger.warn("QueryTimeoutException while getDistinctVersion, err msg{}", e.getMessage());
		} catch (TransactionRequiredException e) {
			logger.warn("TransactionRequiredException while getDistinctVersion,err msg{}", e.getMessage());
		} catch (PersistenceException e) {
			logger.warn("PersistenceException while getDistinctVersion,err msg{}", e.getMessage());
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return result;
	}

	/**
	 * Gets the distinct vendor.
	 *
	 * @param domain          the domain
	 * @param technology      the technology
	 * @param softwareVersion the software version
	 * @return the distinct vendor
	 */
	@Override
	@Deprecated
	public List<Vendor> getDistinctVendor(String domain, String technology, String softwareVersion) {
		logger.info("Going get Distinct Vendor by domain {}, technology {}, softwareVersion {}", domain, technology,
				softwareVersion);
		List<Vendor> vendors = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getDistinctVendor");
			enableFilters(domain, null, technology, softwareVersion);
			vendors = query.getResultList();
		} catch (PersistenceException pe) {
			logger.warn("Error while getDistinctVendor,err msg {}", pe);
		}
		logger.info("Returning vendors list size {}", vendors.size());
		return vendors;
	}

	/**
	 * Gets the distinct technology.
	 *
	 * @param domain          the domain
	 * @param vendor          the vendor
	 * @param softwareVersion the software version
	 * @return the distinct technology
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Technology> getDistinctTechnology(String domain, String vendor, String softwareVersion) {
		logger.info("Going to get Distinct Technology by domain {}, vendor {}, softwareVersion {}", domain, vendor,
				softwareVersion);
		List<Technology> technologies = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getDistinctTechnology");
			enableFilters(domain, vendor, null, softwareVersion);
			technologies = query.getResultList();
		} catch (IllegalStateException | PersistenceException pe) {
			logger.warn("Exception  while getting Distinct Technology,err msg {}", pe);
		}
		logger.info("Returning technology list size {}", technologies.size());
		return technologies;
	}

	/**
	 * Gets the distinct domain.
	 *
	 * @return the distinct domain
	 */
	@Override
	@Deprecated
	public List<Domain> getDistinctDomain() {
		logger.info("Going to get Distinct Domains from NetworkElement");
		List<Domain> domain = new ArrayList<>();
		try {
			String vendor = customerInfo.getVendor();
			logger.info("going to get distinct vendor : {}", vendor);
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<NetworkElement> criteriaQuery = criteriaBuilder.createQuery(NetworkElement.class);
			Root<NetworkElement> networkElement = criteriaQuery.from(NetworkElement.class);
			List<Predicate> nePredicates = getPredicatesToGetDistinctDomain(criteriaBuilder, networkElement, vendor,
					null);
			nePredicates.add(networkElement.get("domain").isNotNull());
			criteriaQuery.select(networkElement.get("domain")).distinct(true)
					.where(nePredicates.toArray(new Predicate[] {}));
			Query query = getEntityManager().createQuery(criteriaQuery);
			domain = query.getResultList();
			logger.info("domain names for vendor {} are: {}", vendor, domain);

		} catch (Exception exception) {
			logger.warn("Error while getting Distinct Domains ,err msg {} ", exception.getMessage());
		}
		logger.info("Returning Domain list size {}", domain.size());
		return domain;
	}

	private List<Predicate> getPredicatesToGetDistinctDomain(CriteriaBuilder criteriaBuilder,
			Root<NetworkElement> networkElement, String vendor, List<Domain> domain) {
		List<Predicate> predicates = new ArrayList<>();
		if (vendor != null && !(vendor.equalsIgnoreCase(ForesightConstants.ALL))) {
			predicates
					.add(criteriaBuilder.equal(networkElement.get(InfraConstants.VENDOR_KEY), Vendor.valueOf(vendor)));
		}
		if (domain != null && !domain.isEmpty()) {
			predicates.add(networkElement.get(InfraConstants.NE_DOMAIN_KEY).in(domain));
		}
		return predicates;
	}

	/**
	 * Gets the distinct info from network element.
	 *
	 * @return the distinct info from network element
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	@Deprecated
	public List<NetworkElementWrapper> getDistinctInfoFromNetworkElement() {
		logger.info("Going to get Distinct Info From NetworkElement");
		List<NetworkElementWrapper> list = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getDistinctInfoFromNetworkElement");
			// networkElementNeTypeFilterByLike(ForesightConstants.MACRO);
			list = query.getResultList();
		} catch (PersistenceException e) {
			logger.warn("Error while get Distinct Info From NetworkElement, err msg ={}", Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("Exception went wrong in Dao Layer,Err msg ={}", Utils.getStackTrace(e));
			throw new DaoException(e);
		}
		logger.info("Returning from getDistinctInfoFromNetworkElement,list size = {}", list.size());
		return list;
	}

	/**
	 * Gets the distinct soft ware version.
	 *
	 * @param domain     the domain
	 * @param vendor     the vendor
	 * @param technology the technology
	 * @return the distinct soft ware version
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getDistinctSoftWareVersion(String domain, String vendor, String technology) {
		logger.info("Going to getDistinctSoftWareVersion by domain {}, vendor {}, technology {}", domain, vendor,
				technology);
		List<String> softWareVersion = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getDistinctSoftWareVersion");
			softWareVersion = query.getResultList();
		} catch (PersistenceException e) {
			logger.warn("Error while getDistinctSoftWareVersion,err msg{}", Utils.getStackTrace(e));
		}
		logger.info("Returning softWareVersion list size {}", softWareVersion.size());
		return softWareVersion;
	}

	/**
	 * Gets the distinct vendor by domain.
	 *
	 * @param domain the domain
	 * @return the distinct vendor by domain
	 */
	@Override
	@Deprecated
	public List<Vendor> getDistinctVendorByDomain(Domain domain) {
		logger.info("Going to getDistinctVendorByDomain from Network Element by {}", domain);
		List<Vendor> vendors = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getDistinctVendorByDomain");
			query.setParameter(InfraConstants.NE_DOMAIN_KEY, domain);
			vendors = query.getResultList();
		} catch (PersistenceException pe) {
			logger.warn("Error while getDistinctVendorByDomain,err msg {}", pe);
		}
		logger.info("Returning vendor list size {}", vendors.size());
		return vendors;
	}

	/**
	 * Gets the distinct technology by domain vendor.
	 *
	 * @param domain the domain
	 * @param vendor the vendor
	 * @return the distinct technology by domain vendor
	 */
	@Override
	@Deprecated
	public List<Technology> getDistinctTechnologyByDomainVendor(Domain domain, Vendor vendor) {
		logger.info("Going to getDistinctTechnologyByDomainVendor from Network Element {},{}", domain, vendor);
		List<Technology> technologies = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getDistinctTechnologyByDomainVendor");
			query.setParameter(InfraConstants.NE_DOMAIN_KEY, domain);
			query.setParameter(InfraConstants.VENDOR_KEY, vendor);
			technologies = query.getResultList();
		} catch (IllegalStateException | PersistenceException e) {
			logger.warn("Error while getDistinctTechnologyByDomainVendor,err msg{}", Utils.getStackTrace(e));
		}
		logger.info("Returning technology list size {}", technologies.size());
		return technologies;
	}

	/**
	 * Gets the NE by name domain vendor tech.
	 *
	 * @param nename     the nename
	 * @param domain     the domain
	 * @param vendor     the vendor
	 * @param technology the technology
	 * @return the NE by name domain vendor tech
	 */
	@Override
	public NetworkElement getNEByNameDomainVendorTech(String nename, String domain, String vendor, String technology) {
		logger.info("Going to get getNEByNameDomainVendorTech by sapId {},domain {},vendor {},tech {}", nename, domain,
				vendor, technology);
		NetworkElement networkElement = new NetworkElement();
		try {
			Query query = getEntityManager().createNamedQuery("getNEByNameDomainVendorTech")
					.setParameter(InfraConstants.NE_NENAME_KEY, nename);
			query.setParameter(InfraConstants.NE_DOMAIN_KEY, Domain.valueOf(domain.toUpperCase()));
			query.setParameter(InfraConstants.VENDOR_KEY, Vendor.valueOf(vendor.toUpperCase()));
			query.setParameter(InfraConstants.NE_TECHNOLOGY_KEY, Technology.valueOf(technology.toUpperCase()));

			networkElement = (NetworkElement) query.getSingleResult();
		} catch (IllegalStateException | PersistenceException e) {
			logger.warn("Error while getNEByNameDomainVendorTech ByNename,err msg {}", e.getMessage());
		}
		logger.info("Returning NetworkElement with id {}", networkElement.getId());
		return networkElement;
	}

	@Override
	@Deprecated
	public List<Object> getDistinctSWVersionFromNetworkElement(String domain, String vendor, String technology) {
		logger.info("Going to get distinct swversion by vendor {},domain {} and technology {}", vendor, domain,
				technology);
		List<Object> resultList = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getDistinctSWVersionFromNetworkElement");
			enableFilters(domain, vendor, technology, null);
			resultList = query.getResultList();
		} catch (IllegalStateException | QueryTimeoutException | TransactionRequiredException e) {
			logger.warn("Exception while get Distinct SWVersion From NetworkElement,err msg {}", e.getMessage());
		}
		return resultList;
	}

	@Override
	@Deprecated
	public List<Object> getDistinctNETypeFromNetworkElement(String domain, String vendor, String technology,
			String softwareVersion) {
		logger.info("Going to get distinct neType by vendor {},domain {}, technology {} and swversion", vendor, domain,
				technology, softwareVersion);
		List<Object> resultList = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getDistinctNETypeFromNetworkElement");
			enableFilters(domain, vendor, technology, softwareVersion);
			resultList = query.getResultList();
		} catch (IllegalStateException | QueryTimeoutException | TransactionRequiredException e) {
			logger.warn("Exception while get Distinct neType From NetworkElement,err msg {}", e.getMessage());
		}
		return resultList;
	}

	@Override
	@Deprecated
	public List<Object> getDistinctVendorFromNetworkElement(String domain) {
		logger.info("Going to get distinct neType by domain {} ", domain);
		List<Object> resultList = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getDistinctVendorFromNetworkElement");
			enableFilters(domain, null, null, null);
			resultList = query.getResultList();
		} catch (IllegalStateException | QueryTimeoutException | TransactionRequiredException e) {
			logger.warn("Exception while get Distinct vendor From NetworkElement,err msg {}", e.getMessage());
		}
		return resultList;
	}

	@Override
	@Deprecated
	public List<Object> getDistinctDomainFromNetworkElement() {
		logger.info("Going to get distinct domain");
		List<Object> resultList = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getDistinctDomainFromNetworkElement");
			resultList = query.getResultList();
		} catch (PersistenceException pe) {
			logger.warn("Exception while get Distinct domain From NetworkElement,err msg {}", pe);
		}
		logger.info("Returing a getDistinctDomainFromNetworkElement list size {}", resultList.size());
		return resultList;
	}

	/**
	 * Enable filters.
	 *
	 * @param domain          the domain
	 * @param vendor          the vendor
	 * @param technology      the technology
	 * @param softwareVersion the software version
	 */
	private void enableFilters(String domain, String vendor, String technology, String softwareVersion) {
		if (domain != null) {
			networkElementDomainFilter(domain.toUpperCase());
		}
		if (vendor != null) {
			networkElementVendorFilter(vendor.toUpperCase());
		}
		if (technology != null) {
			networkElementTechnologyFilter(technology.toUpperCase());
		}
		if (softwareVersion != null) {
			networkElementSoftwareVersionFilter(softwareVersion.toUpperCase());
		}

	}

	/**
	 * Network element technology filter.
	 *
	 * @param technology the technology
	 */
	private void networkElementTechnologyFilter(String technology) {
		logger.info("network element Technology Filter {}", technology);
		Session s = (Session) getEntityManager().getDelegate();
		s.enableFilter("networkElementTechnologyFilter").setParameter("technology", technology);
	}

	/**
	 * Network element software version filter.
	 *
	 * @param softwareVersion the software version
	 */
	private void networkElementSoftwareVersionFilter(String softwareVersion) {
		logger.info("network element softwareVersion Filter {}", softwareVersion);
		Session s = (Session) getEntityManager().getDelegate();
		s.enableFilter("networkElementSoftwareVersionFilter").setParameter("swversion", softwareVersion);
	}

	/**
	 * Network element domain filter.
	 *
	 * @param domain the domain
	 */
	private void networkElementDomainFilter(String domain) {
		logger.info("network element domain Filter {}", domain);
		Session s = (Session) getEntityManager().getDelegate();
		s.enableFilter("networkElementDomainFilter").setParameter(InfraConstants.NE_DOMAIN_KEY, domain);
	}

	/**
	 * Network element vendor filter.
	 *
	 * @param vendor the vendor
	 */
	private void networkElementVendorFilter(String vendor) {
		logger.info("network element Vendor Filter {}", vendor);
		Session s = (Session) getEntityManager().getDelegate();
		s.enableFilter("networkElementVendorFilter").setParameter(InfraConstants.VENDOR_KEY, vendor);
	}

	private void geographyL4IdFilter(String geographyL4) {
		logger.info("GeographyL4IdFilter for  {}", geographyL4);
		Session s = (Session) getEntityManager().getDelegate();
		s.enableFilter("geographyL4IdFilter").setParameter(InfraConstants.NE_GEOGRAPHYL4_KEY, geographyL4);
	}

	@Override
	public List<NetworkElement> getNEByDomainVendorAndL4Name(String domain, String vendor, String l4Name, String neId) {
		logger.info("Going NE list for domain:{}, vendor:{}, l4Name:{}, neId:{}", domain, vendor, l4Name, neId);
		String dynamicQuery = null;
		List<NetworkElement> networkElements = null;
		try {
			dynamicQuery = "Select n from NetworkElement n where Upper(n.neStatus) ='ONAIR' and Upper(n.neId) like (:neId)  ";
			Query query = getEntityManager().createQuery(dynamicQuery).setParameter("neId", "%" + neId + "%");
			networkElementDomainFilter(domain);
			networkElementVendorFilter(vendor);
			networkElementNeTypeFilter("MACRO_CELL");
			geographyL4IdFilter(l4Name);
			networkElements = query.getResultList();
			logger.info("networkElements list size: {}", networkElements.size());
		} catch (Exception e) {
			logger.error("Error inside @Method: getNEByDomainVendorAndGeographyL4: {}", Utils.getStackTrace(e));
		}
		return networkElements;
	}

	@Override
	public List<NetworkElementWrapper> getCountByGeoraphyAndVendor(Vendor vendor, List<String> geographyName,
			String geographyLevel) {
		logger.info("inside the method getCountByGeoraphyAndVendor Vendor {} geographyName{} geographyLevel{}", vendor,
				geographyName, geographyLevel);
		String dynamicQuery = null;
		List<NetworkElementWrapper> list = null;
		try {
			dynamicQuery = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(neType,count(*))from NetworkElement ne where ";
			if (!ForesightConstants.ALL.equalsIgnoreCase(vendor.toString())) {
				dynamicQuery += "ne.vendor=:vendor and ";
			}
			if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL1)
					|| geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL0)) {
				dynamicQuery += "ne.neStatus='ONAIR' and ne.isDeleted='false' and ne.neType is not null and ne.geographyL4.geographyL3.geographyL2.geographyL1.name in(:geographyName) group by ne.neType";
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL2)) {
				dynamicQuery += "ne.neStatus='ONAIR' and ne.isDeleted='false' and ne.neType is not null and ne.geographyL4.geographyL3.geographyL2.name in(:geographyName) group by ne.neType";
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL3)) {
				dynamicQuery += "ne.neStatus='ONAIR' and ne.isDeleted='false' and ne.neType is not null and ne.geographyL4.geographyL3.name in(:geographyName) group by ne.neType";
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL4)) {
				dynamicQuery += "ne.neStatus='ONAIR' and ne.isDeleted='false' and ne.neType is not null and ne.geographyL4.name in(:geographyName) group by ne.neType";
			}
			Query query = getEntityManager().createQuery(dynamicQuery).setParameter("geographyName", geographyName);
			if (!ForesightConstants.ALL.equalsIgnoreCase(vendor.toString())) {
				query.setParameter("vendor", vendor);
			}
			list = query.getResultList();
		} catch (NullPointerException nullPointerException) {
			logger.error("NullPointerException caught while getting Sites count from NetworkElement Exception : {}",
					Utils.getStackTrace(nullPointerException));
			throw new DaoException("Unable to get Sites count  from NetworkElement for specific parameters.");
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error("IllegalArgumentException caught while getting Sites count from NetworkElement Exception : {}",
					Utils.getStackTrace(illegalArgumentException));
			throw new DaoException("Unable to get Sites count from NetworkElement  for specific parameters.");
		} catch (NoResultException noResultException) {
			logger.error("NoResultException caught while getting Sites count from NetworkElement Exception : {}",
					Utils.getStackTrace(noResultException));
			throw new DaoException("Unable to get Sites count from NetworkElement for specific  parameters.");
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while getting Sites count from NetworkElement Exception : {}",
					Utils.getStackTrace(persistenceException));
			throw new DaoException("Unable to get Sites count from NetworkElement for specific parameters.");
		} catch (Exception exception) {
			logger.error("Exception caught while getting Sites count from NetworkElement Exception : {}",
					Utils.getStackTrace(exception));
			throw new DaoException("Unable to get Sites count from NetworkElement for specific parameters.");
		}
		return list;
	}

	@Override
	@Deprecated
	public List<Object[]> getNetworkElement(String netype, Integer geographyId, String geoType, String domain,
			String vendor, String technology, String softwareVersion) {
		logger.info(
				"Going to get NetworkElement List by vendor {},domain {}, technology {}, softwareVersion {}, netype {}, geoType {} and geographyId {}",
				vendor, domain, technology, softwareVersion, netype, geoType, geographyId);
		List<Object[]> resultList = Collections.emptyList();
		NEType eleType = NEType.valueOf(netype);
		Query query = null;
		try {
			if (netype.contains(ForesightConstants.MACRO)) {
				query = getEntityManager().createNamedQuery("getNetworkelementForMacro")
						.setParameter(InfraConstants.NE_NESTATUS_KEY, NEStatus.ONAIR);
			} else if (eleType.equals(NEType.SMALL_CELL_INDOOR) || eleType.equals(NEType.SMALL_CELL_OUTDOOR)) {
				query = getEntityManager().createNamedQuery("getNetworkelementForSmallCell").setParameter("neStatus",
						NEStatus.ONAIR);
			}
			enableFilters(domain, vendor, technology, softwareVersion);
			enableFiltersForType(geographyId, geoType);
			resultList = query != null ? query.getResultList() : null;
		} catch (IllegalStateException | QueryTimeoutException | TransactionRequiredException e) {
			logger.warn("Exception while getNetworkElementList From NetworkElement,err msg {}", e.getMessage());
		}
		logger.info("Returning NetworkElement list size {}", resultList.size());
		return resultList;

	}

	/**
	 * Enable filters for type.
	 *
	 * @param geographyId the geography id
	 * @param geoType     the geo type
	 */
	private void enableFiltersForType(Integer geographyId, String geoType) {
		if (geoType != null) {
			if (ForesightConstants.GEOGRAPHY_L1.equalsIgnoreCase(geoType)) {
				networkElementGeoL1Filter(geographyId);
			} else if (ForesightConstants.GEOGRAPHY_L2.equalsIgnoreCase(geoType)) {
				networkElementGeoL2Filter(geographyId);
			} else if (ForesightConstants.GEOGRAPHY_L3.equalsIgnoreCase(geoType)) {
				networkElementGeoL3Filter(geographyId);
			} else if (ForesightConstants.GEOGRAPHY_L4.equalsIgnoreCase(geoType)) {
				networkElementGeoL4Filter(geographyId);
			}
		}
	}

	/**
	 * Network element ne id filter.
	 *
	 * @param neName the ne id
	 */
	private void networkElementNeNameFilter(List<String> neName) {
		if (neName != null && !neName.isEmpty()) {
			logger.info("network element neName Filter {}", neName);
			Session s = (Session) getEntityManager().getDelegate();
			s.enableFilter("networkElementNeNameFilter").setParameterList(InfraConstants.NE_NENAME_KEY, neName);
		}
	}

	/**
	 * Network element ne type filter.
	 *
	 * @param netype the netype
	 */
	@SuppressWarnings("unused")
	private void networkElementNeTypeFilter(String netype) {
		if (netype != null) {
			logger.info("network element netype Filter {}", netype);
			Session s = (Session) getEntityManager().getDelegate();
			s.enableFilter("networkElementNeTypeFilter").setParameter(ForesightConstants.NE_TYPE, netype);
		}
	}

	/**
	 * Network element ne type filter by like.
	 *
	 * @param netype the netype
	 */
	private void networkElementNeTypeFilterByLike(String netype) {
		if (netype != null) {
			logger.info("network element netype Filter by like {}", netype);
			Session s = (Session) getEntityManager().getDelegate();
			s.enableFilter("networkElementNeTypeFilterByLike").setParameter(ForesightConstants.NE_TYPE,
					ForesightConstants.PERCENT + netype + ForesightConstants.PERCENT);
		}
	}

	/**
	 * Network element geo L 1 filter.
	 *
	 * @param geographyId the geography id
	 */
	private void networkElementGeoL1Filter(Integer geographyId) {
		if (geographyId != null) {
			logger.info("network element geoL1 Filter {}", geographyId);
			Session s = (Session) getEntityManager().getDelegate();
			s.enableFilter("networkElementGeoL1Filter").setParameter("geographyL1Id", geographyId);
		}
	}

	/**
	 * Network element geo L 2 filter.
	 *
	 * @param geographyId the geography id
	 */
	private void networkElementGeoL2Filter(Integer geographyId) {
		try {
			if (geographyId != null) {
				logger.info("network element geoL2 Filter {}", geographyId);
				Session s = (Session) getEntityManager().getDelegate();
				s.enableFilter("networkElementGeoL2Filter").setParameter("geographyL2Id", geographyId);
			}
		} catch (Exception e) {
			logger.error("Error while networkElementGeoL2Filter ,err msg {}", Utils.getStackTrace(e));
		}
	}

	/**
	 * Geography L 2 id filter.
	 *
	 * @param geographyL2 the geography L 2
	 */
	private void geographyL2IdFilter(Integer geographyL2) {
		try {
			if (geographyL2 != null) {
				logger.info("network element geoL2 Filter {}", geographyL2);
				Session s = (Session) getEntityManager().getDelegate();
				s.enableFilter("geographyL2IdFilter").setParameter("geographyL2", geographyL2);
			}
		} catch (Exception e) {
			logger.error("Error while geographyL2IdFilter ,err msg {}", Utils.getStackTrace(e));
		}
	}

	/**
	 * Network element geo L 3 filter.
	 *
	 * @param geographyId the geography id
	 */
	private void networkElementGeoL3Filter(Integer geographyId) {
		if (geographyId != null) {
			logger.info("network element geoL3 Filter {}", geographyId);
			Session s = (Session) getEntityManager().getDelegate();
			s.enableFilter("networkElementGeoL3Filter").setParameter("geographyL3Id", geographyId);
		}
	}

	/**
	 * Network element geo L 4 filter.
	 *
	 * @param geographyId the geography id
	 */
	private void networkElementGeoL4Filter(Integer geographyId) {
		if (geographyId != null) {
			logger.info("network element geoL4 Filter {}", geographyId);
			Session s = (Session) getEntityManager().getDelegate();
			s.enableFilter("networkElementGeoL4Filter").setParameter("geographyL4Id", geographyId);
		}
	}

	@Override
	public List<NetworkElementWrapper> getNEInfoByNeId(String domain, String vendor, String softwareVersion,
			String technology, String neId) {
		logger.info("Going to get NE Info by neId {},vendor {},domain {},softwareVersion {}", neId, vendor, domain,
				softwareVersion);
		List<NetworkElementWrapper> elementWrappers = Collections.emptyList();
		Query query = null;
		try {
			query = getEntityManager().createNamedQuery("getNEInfoByNeId").setParameter("neId", neId);
			elementWrappers = query.getResultList();
			enableFilters(domain, vendor, technology, softwareVersion);
		} catch (PersistenceException e) {
			logger.error("Error while get NE Info By NeId {},err msg {}", neId, e.toString());
		}
		logger.info("Return NetworkElement size {}", elementWrappers.size());
		return elementWrappers;
	}

	/**
	 * Gets the network elements for cell level detail.
	 *
	 * @param neTypeList      the ne type list
	 * @param neNameList      the ne name list
	 * @param neFrequencyList the ne frequency list
	 * @param neStatusList    the ne status list
	 * @param vendorList      the vendor list
	 * @param technologyList  the technology list
	 * @param domainList      the domain list
	 * @param viewportMap     the viewport map
	 * @param geographyNames  the geography names
	 * @param neIdList        the ne id list
	 * @return the network elements for cell level detail
	 * @throws DaoException the dao exception
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<NetworkElement> getNetworkElementsForCellLevelDetail(List<NEType> neTypeList, List<String> neNameList,
			List<String> neFrequencyList, List<NEStatus> neStatusList, List<Vendor> vendorList,
			List<Technology> technologyList, List<Domain> domainList, Map<String, Double> viewportMap,
			Map<String, List<String>> geographyNames, List<String> neIdList) {
		logger.info(
				"Going to get NetworkElement Cell Level data for neType : {} , neFrequency : {} , neStatus: {} , vendor : {}",
				neTypeList, neFrequencyList, neStatusList, vendorList);
		try {
			neNameList = InfraUtils.convertListElements(neNameList, InfraConstants.LIST_TOUPPERCASE);
			neFrequencyList = InfraUtils.convertListElements(neFrequencyList, InfraConstants.LIST_TOUPPERCASE);
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<NetworkElement> criteriaQuery = criteriaBuilder.createQuery(NetworkElement.class);
			Root<NetworkElement> networkElement = criteriaQuery.from(NetworkElement.class);
			List<Predicate> nePredicates = InfraUtils.getPredicatesForCriteriaBuilderForNE(criteriaBuilder,
					networkElement, neTypeList, neNameList, neFrequencyList, neStatusList, vendorList, technologyList,
					domainList, geographyNames, neIdList);
			if (neTypeList.contains(NEType.MACRO_CELL)) {
				nePredicates.add(networkElement.get(InfraConstants.NE_NETWORKELEMENT_KEY).isNotNull());
			}
			// nePredicates.add(networkElement.get(InfraConstants.NE_GEOGRAPHYL4_KEY).isNotNull());
			// nePredicates.add(networkElement.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
			// .get(InfraConstants.NE_GEOGRAPHYL3_KEY).isNotNull());
			// nePredicates.add(networkElement.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
			// .get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY).isNotNull());
			nePredicates.add(criteriaBuilder.equal(networkElement.get(InfraConstants.ISDELETED_STATUS),
					ForesightConstants.ZERO));

			List<Predicate> viewportPredicates = InfraUtils.getPredicatesForViewPortsForNE(criteriaBuilder,
					networkElement, viewportMap);
			List<Predicate> finalPredicates = new ArrayList<>();
			finalPredicates.addAll(nePredicates);
			finalPredicates.addAll(viewportPredicates);
			criteriaQuery.select(networkElement).where(finalPredicates.toArray(new Predicate[] {}));
			criteriaQuery.orderBy(criteriaBuilder.asc(networkElement.get(InfraConstants.NE_NENAME_KEY)));
			Query query = getEntityManager().createQuery(criteriaQuery);
			return query.getResultList();
		} catch (NullPointerException | IllegalArgumentException | PersistenceException exception) {
			logger.error("Exception caught while getting cell level data from NetworkElement Exception : {}",
					Utils.getStackTrace(exception));
			throw new DaoException("Unable to get NetworkElement Cell Level data for specific parameters.");
		}
	}

	@Override
	public NetworkElement getNetworkElementDataByNEId(String neId) {
		logger.info("Going to get NetworkElement by  neId");
		NetworkElement networkElementData = new NetworkElement();
		try {
			Query query = getEntityManager().createNamedQuery("getNetworkElementByNEId")
					.setParameter(InfraConstants.NE_NEID_KEY, neId.toUpperCase());
			networkElementData = (NetworkElement) query.getSingleResult();
		} catch (PersistenceException | IllegalArgumentException exception) {
			logger.warn("Exception caught  while getting NetworkElement data  by neId Exception");
			throw new DaoException(" Unable to  get NetworkElement data for neId .");
		}
		return networkElementData;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<NetworkElement> getNetworkElementsForSiteLevelDetail(List<NEType> neTypeList, List<String> neNameList,
			List<String> neFrequencyList, List<NEStatus> neStatusList, List<Vendor> vendorList,
			List<Technology> technologyList, List<Domain> domainList, Map<String, Double> viewportMap,
			Map<String, List<String>> geographyNames) {
		try {
			logger.info(
					"Going to get NetworkElement Site Level data for neType : {} , neFrequency : {} , neStatus: {} , vendor : {}",
					neTypeList, neFrequencyList, neStatusList, vendorList);
			neNameList = InfraUtils.convertListElements(neNameList, InfraConstants.LIST_TOUPPERCASE);
			neFrequencyList = InfraUtils.convertListElements(neFrequencyList, InfraConstants.LIST_TOUPPERCASE);
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<NetworkElement> criteriaQuery = criteriaBuilder.createQuery(NetworkElement.class);
			Root<NetworkElement> outerNetworkElement = criteriaQuery.from(NetworkElement.class);
			Subquery<Integer> neSubQuery = criteriaQuery.subquery(Integer.class);
			Root<NetworkElement> innerNetworkElement = neSubQuery.from(NetworkElement.class);
			List<Predicate> innerNEPredicates = InfraUtils.getPredicatesForCriteriaBuilderForNE(criteriaBuilder,
					innerNetworkElement, neTypeList, neNameList, neFrequencyList, neStatusList, vendorList,
					technologyList, domainList, geographyNames, null);
			innerNEPredicates.add(innerNetworkElement.get(InfraConstants.NE_NETWORKELEMENT_KEY).isNotNull());
			List<Predicate> outerNEPredicates = InfraUtils.getPredicatesForViewPortsForNE(criteriaBuilder,
					outerNetworkElement, viewportMap);
			outerNEPredicates.add(outerNetworkElement.get(InfraConstants.NE_NETWORKELEMENT_KEY).isNull());
			neSubQuery.select(innerNetworkElement.get(InfraConstants.NE_NETWORKELEMENT_KEY)).distinct(true)
					.where(innerNEPredicates.toArray(new Predicate[] {}));
			criteriaQuery.select(outerNetworkElement).where(criteriaBuilder
					.in(outerNetworkElement.get(InfraConstants.NE_NETWORKELEMENTID_KEY)).value(neSubQuery),
					criteriaBuilder.and(outerNEPredicates.toArray(new Predicate[] {})));
			criteriaQuery.orderBy(criteriaBuilder.asc(outerNetworkElement.get(InfraConstants.NE_NENAME_KEY)));
			Query query = getEntityManager().createQuery(criteriaQuery);
			return query.getResultList();
		} catch (NullPointerException nullPointerException) {
			logger.error("NullPointerException caught while getting data from NetworkElement Exception : {}",
					Utils.getStackTrace(nullPointerException));
			throw new DaoException("Unable to get NetworkElement  Site Level data for specific parameters.");
		} catch (IllegalArgumentException illegalArgumentException) {

			logger.error("IllegalArgumentException caught while getting data from NetworkElement Exception : {}",
					Utils.getStackTrace(illegalArgumentException));
			throw new DaoException("Unable to get NetworkElement Site Level data for specific parameters .");
		} catch (NoResultException noResultException) {
			logger.error("NoResultException caught while getting data from NetworkElement Exception : {}",
					Utils.getStackTrace(noResultException));
			throw new DaoException("Unable to get NetworkElement Site Level data  for specific parameters.");
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while getting data from NetworkElement Exception : {}",
					Utils.getStackTrace(persistenceException));
			throw new DaoException("Unable to get NetworkElement Site Level data for specific parameters.");
		} catch (Exception exception) {
			logger.error("Exception caught while getting data from  NetworkElement Exception : {}",
					Utils.getStackTrace(exception));
			throw new DaoException("Unable to get NetworkElement Site Level data for specific parameters.");
		}
	}

	/**
	 * Gets the constructor statement.
	 *
	 * @param geographyLevel the geography level
	 * @return the constructor statement
	 */
	private String getConstructorStatement(String geographyLevel) {
		String statement = InfraConstants.BLANK_STRING;
		if (geographyLevel.equalsIgnoreCase("GEOGRAPHYL1")) {
			statement = "ne.geographyL4.geographyL3.geographyL2.geographyL1.name,count(distinct ne.networkElement),avg(ne.latitude), avg(ne.longitude),ne.neType,ne.neStatus,ne.neFrequency";
		} else if (geographyLevel.equalsIgnoreCase("GEOGRAPHYL2")) {
			statement = "ne.geographyL4.geographyL3.geographyL2.name,count(distinct ne.networkElement),avg(ne.latitude), avg(ne.longitude),ne.neType,ne.neStatus,ne.neFrequency";
		} else if (geographyLevel.equalsIgnoreCase("GEOGRAPHYL3")) {
			statement = "ne.geographyL4.geographyL3.name,count(distinct ne.networkElement),avg(ne.latitude), avg(ne.longitude),ne.neType,ne.neStatus,ne.neFrequency";
		} else if (geographyLevel.equalsIgnoreCase("GEOGRAPHYL4")) {
			statement = "ne.geographyL4.name,count(distinct ne.networkElement),avg(ne.latitude), avg(ne.longitude),ne.neType,ne.neStatus,ne.neFrequency";
		}
		return statement;
	}

	/**
	 * Sets the value for ne type.
	 *
	 * @param neType    the ne type
	 * @param query     the query
	 * @param subNEType the sub NE type
	 * @return the query
	 */
	private Query setValueForNeType(String neType, Query query, List<NEType> subNEType) {
		if (neType.equalsIgnoreCase(NEType.MACRO_CELL.name())) {
			query.setParameter(InfraConstants.NE_NETYPE_KEY, NEType.MACRO_CELL);
		}
		if (neType.equalsIgnoreCase(NEType.MACRO.name())) {
			query.setParameter(InfraConstants.NE_NETYPE_KEY, NEType.MACRO);
		} else if (neType.equalsIgnoreCase(NEType.SMALL_CELL_OUTDOOR.name())) {
			query.setParameter(InfraConstants.NE_NETYPE_KEY, NEType.SMALL_CELL_OUTDOOR);
		} else if (neType.equalsIgnoreCase(NEType.SMALL_CELL_INDOOR.name())) {
			query.setParameter(InfraConstants.NE_NETYPE_KEY, NEType.SMALL_CELL_INDOOR);
		} else if (neType.equalsIgnoreCase(NEType.WIFI.name())) {
			query.setParameter(InfraConstants.NE_NETYPE_KEY, subNEType);
		}
		return query;
	}

	/**
	 * Sets the value for ne status.
	 *
	 * @param neStatus the ne status
	 * @param query    the query
	 * @return the query
	 */
	private Query setValueForNeStatus(String neStatus, Query query) {
		if (neStatus.equalsIgnoreCase(NEStatus.ONAIR.name())) {
			query.setParameter("neStatus", NEStatus.ONAIR);
		}
		if (neStatus.equalsIgnoreCase(NEStatus.PLANNED.name())) {
			query.setParameter("neStatus", NEStatus.PLANNED);
		}
		if (neStatus.equalsIgnoreCase(NEStatus.NONRADIATING.name())) {
			query.setParameter("neStatus", NEStatus.NONRADIATING);
		}
		if (neStatus.equalsIgnoreCase(NEStatus.DECOMMISSIONED.name())) {
			query.setParameter("neStatus", NEStatus.DECOMMISSIONED);
		}
		if (neStatus.equalsIgnoreCase(NEStatus.CONNECT.name())) {
			query.setParameter("neStatus", NEStatus.CONNECT);
		}
		return query;
	}

	/**
	 * Gets the n etask detail table.
	 *
	 * @param taskStatus the task status
	 * @return the n etask detail table
	 */
	private String getNEtaskDetailTable(List<String> taskStatus) {
		if ((taskStatus != null) && (!taskStatus.isEmpty())) {
			return ",NETaskDetail td ";
		}
		return InfraConstants.BLANK_STRING;
	}

	/**
	 * Gets the site category check.
	 *
	 * @param siteCategory the site category
	 * @return the site category check
	 */
	private String getSiteCategoryCheck(List<String> siteCategory) {
		if ((siteCategory != null) && (!siteCategory.isEmpty())) {
			return ", MacroSiteDetail ms ";
		}
		return InfraConstants.BLANK_STRING;
	}

	/**
	 * Gets the table name by ne type.
	 *
	 * @param neType       the ne type
	 * @param status       the status
	 * @param taskStatus   the task status
	 * @param siteCategory the site category
	 * @param isVisualize  the is visualize
	 * @return the table name by ne type
	 */
	private String getTableNameByNeType(String neType, String status, List<String> taskStatus,
			List<String> siteCategory, Boolean isVisualize) {
		if (neType.equalsIgnoreCase(NEType.MACRO_CELL.name()) && !isVisualize) {
			return getSiteCategoryCheck(siteCategory) + "" + getNEtaskDetailTable(taskStatus);
		}

		if (neType.equalsIgnoreCase(NEType.MACRO_CELL.name()) && isVisualize) {
			return "NetworkElement ne ,MacroSiteDetail ms ";
		}
		if (neType.equalsIgnoreCase(NEType.WIFI.name()) && isVisualize) {
			return " ";
		}
		if (neType.equalsIgnoreCase(NEType.SMALL_CELL_OUTDOOR.name()) && isVisualize) {
			return "NetworkElement ne ,SmallCellSiteDetail ms ";
		}
		if (neType.equalsIgnoreCase(NEType.SMALL_CELL_OUTDOOR.name()) && !isVisualize) {
			return ", SmallCellSiteDetail ms ";
		}
		return InfraConstants.BLANK_STRING;
	}

	/**
	 * Gets the WIFI query.
	 *
	 * @param dynamicQuery   the dynamic query
	 * @param geographyLevel the geography level
	 * @return the WIFI query
	 */
	private String getWIFIQuery(String dynamicQuery, String geographyLevel) {
		try {
			if (geographyLevel.equalsIgnoreCase("GEOGRAPHYL1")) {
				dynamicQuery = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(y.name, max(w.neStatus) , max(w.neType) ,count(*), avg(p.latitude),avg(p.longitude)) from NetworkElement w, "
						+ "GeographyL4 x, GeographyL3 y,GeographyL2 z, GeographyL1 p "
						+ " where w.geographyL4=x.id and x.geographyL3=y.id "
						+ " and w.latitude >= :southWestLat and w.latitude <= :northEastLat and w.longitude >=:southWestLong and w.longitude <=:northEastLong and w.latitude is not null and w.longitude is not null "
						+ " and y.geographyL2=z.id and z.geographyL1=p.id ";
				dynamicQuery = dynamicQuery + " and w.neStatus = :neStatus  ";
				dynamicQuery = dynamicQuery + "and w.neType in (:neType)";
				dynamicQuery = dynamicQuery + " group by p.name";
			} else if (geographyLevel.equalsIgnoreCase("GEOGRAPHYL2")) {
				dynamicQuery = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(z.name, max(w.neStatus) , max(w.neType) ,count(*), avg(w.latitude),avg(w.longitude)) from NetworkElement w, GeographyL4 x,"
						+ " GeographyL3 y,GeographyL2 z " + " where w.geographyL4=x.id and x.geographyL3=y.id "
						+ "  and w.latitude >= :southWestLat and w.latitude <= :northEastLat and w.longitude >=:southWestLong and w.longitude <=:northEastLong and w.latitude is not null and w.longitude is not null  "
						+ " and y.geographyL2=z.id ";
				dynamicQuery = dynamicQuery + " and w.neStatus = :neStatus  ";
				dynamicQuery = dynamicQuery + "and w.neType in (:neType)";
				dynamicQuery = dynamicQuery + " group by z.name";
			} else if (geographyLevel.equalsIgnoreCase("GEOGRAPHYL3")) {
				dynamicQuery = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(y.name, max(w.neStatus) , max(w.neType) ,count(*), avg(w.latitude),avg(w.longitude)) from NetworkElement w, GeographyL4 x,"
						+ " GeographyL3 y where w.geographyL4=x.id and x.geographyL3=y.id "
						+ "  and w.latitude >= :southWestLat and w.latitude <= :northEastLat and w.longitude >=:southWestLong and w.longitude <=:northEastLong and w.latitude is not null and w.longitude is not null ";
				dynamicQuery = dynamicQuery + " and w.neStatus = :neStatus ";
				dynamicQuery = dynamicQuery + " and w.neType in (:neType)";
				dynamicQuery = dynamicQuery + " group by y.name";
			} else if (geographyLevel.equalsIgnoreCase("GEOGRAPHYL4")) {
				dynamicQuery = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(x.name, max(w.neStatus), max(w.neType),count(*), avg(w.latitude),avg(w.longitude)) from NetworkElement w, GeographyL4 x"
						+ " where w.geographyL4=x.id"
						+ "  and w.latitude >= :southWestLat and w.latitude <= :northEastLat and w.longitude >=:southWestLong and w.longitude <=:northEastLong and w.latitude is not null and w.longitude is not null ";
				dynamicQuery = dynamicQuery + " and w.neStatus = :neStatus ";
				dynamicQuery = dynamicQuery + " and w.neType in (:neType)";
				dynamicQuery = dynamicQuery + " group by x.name ";
			}

		} catch (Exception e) {
			logger.error("Exception in getWIFIQuery : {}", Utils.getStackTrace(e));
		}
		return dynamicQuery;
	}

	@Override
	@Deprecated
	public List<Object> getDistinctTechnologyFromNetworkElement(String domain, String vendor) {
		logger.info("Going to get distinct technology by vendor {} and domain {}", vendor, domain);
		List<Object> resultList = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getDistinctTechnologyFromNetworkElement");
			enableFilters(domain, vendor, null, null);
			resultList = query.getResultList();
		} catch (IllegalStateException | QueryTimeoutException | TransactionRequiredException e) {
			logger.warn("Exception while get Distinct neType From NetworkElement,err msg {}", e.getMessage());
		}
		return resultList;
	}

	@Override
	public List<SiteEmsDetailsWrapper> getEmsDetailDataForSites(String neName) {
		logger.info("Going to get EmsDetail data by neName");
		List<SiteEmsDetailsWrapper> siteEMSDetailWrapper = new ArrayList<>();
		String dynamicQuery = ForesightConstants.BLANK_STRING;
		try {
			dynamicQuery += "select new com.inn.foresight.core.infra.wrapper.SiteEmsDetailsWrapper("
					+ "m.neBandDetail.emsHostname,m.neBandDetail.emsIPV6Address"
					+ ",m.neBandDetail.enodeBId,m.neBandDetail.enodeStatus,m.neBandDetail.adminStatus, "
					+ "m.neBandDetail.emsLiveDate,m.neBandDetail.oamVlan,"
					+ "m.neBandDetail.oamLsmrId,m.neBandDetail.oamIpv6Add," + "m.neBandDetail.oamIpv6Subnetm,"
					+ "m.neBandDetail.oamGIpv6Add,m.neBandDetail.oamGIpv6Subnetm,"
					+ "m.neBandDetail.signalVlan,m.neBandDetail.signalMmeGroupId,"
					+ "m.neBandDetail.signalIpv6Add,m.neBandDetail.signalIpv6Subnetm,"
					+ "m.neBandDetail.signalGIpv6Add," + "m.neBandDetail.signalGIpv6Subnetm,m.neBandDetail.bearerVlan,"
					+ "m.neBandDetail.bearerSAEGWPriGId,m.neBandDetail.bearerIpv6Add,"
					+ "m.neBandDetail.bearerIpv6Subnetm,m.neBandDetail.bearerGIpv6Add,"
					+ "m.neBandDetail.bearerGIPV6SubnetMask)  from MacroSiteDetail m where upper(m.neBandDetail.networkElement.neName) =:neName ";
			Query query = getEntityManager().createQuery(dynamicQuery).setParameter(InfraConstants.NE_NENAME_KEY,
					neName.toUpperCase());
			siteEMSDetailWrapper = query.getResultList();
		} catch (NullPointerException nullPointerException) {
			logger.error("NullPointerException caught while getting EmsDetail Data  by neName Exception : {}",
					Utils.getStackTrace(nullPointerException));
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error("IllegalArgumentException caught while getting EmsDetail data  by neName Exception : {}",
					Utils.getStackTrace(illegalArgumentException));
		} catch (NoResultException noResultException) {
			logger.error("NoResultException caught while getting EmsDetail data  by neName Exception : {}",
					Utils.getStackTrace(noResultException));
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while getting EmsDetail data  by neName Exception : {}",
					Utils.getStackTrace(persistenceException));
		} catch (Exception exception) {
			logger.error("Exception caught while getting EmsDetail data  by neName Exception : {}",
					Utils.getStackTrace(exception));
		}
		return siteEMSDetailWrapper;
	}

	@Override
	@Deprecated
	public List<Object[]> searchAllNetworkElement(String siteName, Integer llimit, Integer ulimit) {
		logger.info("Going to searchAllNetworkElement data by neName");
		List<Object[]> list = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("searchAllNetworkElement")
					.setParameter(InfraConstants.NENAME, "%" + siteName.trim().toUpperCase() + "%")
					.setParameter("vendor", Vendor.RUCKUS);
			query.setFirstResult(llimit);
			query.setMaxResults(ulimit - llimit + 1);
			list = query.getResultList();
		} catch (NullPointerException nullPointerException) {
			logger.error("NullPointerException caught while searching NetworkElement Data  by neName Exception : {}",
					Utils.getStackTrace(nullPointerException));
			throw new DaoException("Unable to get NetworkElement data  for neName .");
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error(
					"IllegalArgumentException caught while searching NetworkElement data  by neName Exception : {}",
					Utils.getStackTrace(illegalArgumentException));
			throw new DaoException("Unable to get NetworkElement data  for neName .");
		} catch (NoResultException noResultException) {
			logger.error("NoResultException caught while searching NetworkElement data  by neName Exception : {}",
					Utils.getStackTrace(noResultException));
			throw new DaoException("Unable to get NetworkElement  data for neName .");
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while searching NetworkElement data  by neName Exception : {}",
					Utils.getStackTrace(persistenceException));
			throw new DaoException("Unable to get NetworkElement data for neName .");
		} catch (Exception exception) {
			logger.error("Exception caught while searching NetworkElement data  by neName Exception : {}",
					Utils.getStackTrace(exception));
			throw new DaoException("Unable to get NetworkElement data for neName .");
		}
		return list;
	}

	@Override
	public NetworkElementWrapper getSiteInfoBySapId(String siteID) {
		NetworkElementWrapper networkElementWrapper = new NetworkElementWrapper();
		try {
			Query query = getEntityManager().createNamedQuery("getNetworkSiteInfo").setParameter("neName",
					"%" + siteID.toUpperCase() + "%");
			networkElementWrapper = (NetworkElementWrapper) query.getSingleResult();
			logger.info("Returning site Info");
		} catch (Exception e) {
			logger.warn("Error while getSiteInfo,err msg{}", e.getMessage());
		}

		return networkElementWrapper;
	}

	@Override
	public NetworkElementWrapper getGeographyL4BySapId(String siteID) {
		try {
			Query query = getEntityManager().createNamedQuery("getGeographyL4BySapId").setParameter("neName",
					"%" + siteID.toUpperCase() + "%");
			List<NetworkElementWrapper> list = query.getResultList();
			if (list != null && !list.isEmpty())
				return list.get(0);
			else
				throw new DaoException("NO Record found");
		} catch (Exception e) {
			logger.warn("Error while getGeographyL4BySapId,err msg{}", e.getMessage());
			throw new DaoException("Unable to get Data " + e.getMessage());
		}

	}

	@Override
	public NetworkElementWrapper getGeographyL1BySapId(String siteID) {
		try {
			Query query = getEntityManager().createNamedQuery("getGeographyL1BySapId").setParameter("neName",
					"%" + siteID.toUpperCase() + "%");
			List<NetworkElementWrapper> list = query.getResultList();
			if (list != null && !list.isEmpty())
				return list.get(0);
			else
				throw new DaoException("NO Record found");
		} catch (Exception e) {
			logger.warn("Error while getGeographyL4BySapId,err msg{}", e.getMessage());
			throw new DaoException("Unable to get Data " + e.getMessage());
		}

	}

	/**
	 * Gets the NetworkElement DATA for neName.
	 *
	 * @param neName the ne name
	 * @return the NetworkElement DATA for neName.
	 * @throws DaoException the dao exception
	 */
	@Override
	public List<NEBandDetail> getNetworkElementDataForNE(String neName) {
		List<NEBandDetail> neBandDetailDataList = new ArrayList<>();
		logger.info("Going to get NetworkElement data for neName {} ", neName);
		try {
			Query query = getEntityManager().createNamedQuery("getNetworkElementDataForNE");
			query.setParameter(InfraConstants.NENAME, neName.toUpperCase());
			query.setParameter(InfraConstants.NE_TYPE, NEType.MACRO);
			return query.getResultList();
		} catch (IllegalArgumentException | PersistenceException exception) {
			logger.error("NoResultException caught while getting NetworkElement data for neName {} Exception {}",
					neName, Utils.getStackTrace(exception));
		} catch (Exception exception) {
			logger.error("Error in getting NetworkElement for neName {} Exception {}", neName,
					Utils.getStackTrace(exception));
		}
		return neBandDetailDataList;
	}

	@Override
	public List<NetworkElementWrapper> getAggregateCountForWifi(List<NEType> neType, String neStatus,
			Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat, String geographyLevel,
			List<String> neFrequencyList, List<String> siteCategory, List<String> taskStatus) {
		List<NetworkElementWrapper> networkElementList = new ArrayList<>();
		Map<String, Double> viewportMap = new HashMap<>();
		try {
			logger.info("Going to get aggregate layer count of Sites for  neType {},neStatus {},neFrequencyList {}",
					neType, neStatus, neFrequencyList);
			CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<NetworkElementWrapper> criteriaQuery = builder.createQuery(NetworkElementWrapper.class);
			Root<WifiSiteDetail> rootElements = criteriaQuery.from(WifiSiteDetail.class);
			viewportMap = InfraUtils.getViewPortMap(southWestLong, southWestLat, northEastLong, northEastLat);
			criteriaQuery = getConstructorStatementForWifiCount(geographyLevel, builder, rootElements, criteriaQuery);
			List<Predicate> finalPredicates = getPredicateForWifi(builder, rootElements, neType, neStatus,
					neFrequencyList, siteCategory, taskStatus);
			finalPredicates.addAll(InfraUtils.getPredicatesForViewPorts(builder, rootElements, viewportMap));
			criteriaQuery = getGroupByStatementForWifiCount(geographyLevel, rootElements, criteriaQuery);
			Query query = getEntityManager()
					.createQuery(criteriaQuery.where(finalPredicates.toArray(new Predicate[] {})));
			networkElementList = query.getResultList();
		} catch (IllegalArgumentException | PersistenceException exception) {
			logger.error("Exception caught while getting data from  NetworkElement Exception : {}",
					Utils.getStackTrace(exception));
			throw new DaoException("Unable to get data From Network Element For Wifi ");
		} catch (Exception exception) {
			logger.error("Exception  caught while getting data from NetworkElement  Exception : {}",
					Utils.getStackTrace(exception));
			throw new DaoException("Unable to get data From Network Element For Wifi");
		}
		logger.info("Size of NetworkElement List {} ", networkElementList.size());
		return networkElementList;
	}

	@Override
	public List<NetworkElementWrapper> getActualCountForWifi(List<NEType> neType, String neStatus, Double southWestLong,
			Double southWestLat, Double northEastLong, Double northEastLat, List<String> neFrequencyList,
			List<String> siteCategory, List<String> taskStatus) {
		List<NetworkElementWrapper> networkElementList = new ArrayList<>();
		Map<String, Double> viewportMap = new HashMap<>();
		try {
			logger.info("Going to get Actual Sites for Wifi neType {},neStatus {},neFrequencyList {}", neType, neStatus,
					neFrequencyList);
			CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<NetworkElementWrapper> criteriaQuery = builder.createQuery(NetworkElementWrapper.class);
			Root<WifiSiteDetail> rootElements = criteriaQuery.from(WifiSiteDetail.class);
			viewportMap = InfraUtils.getViewPortMap(southWestLong, southWestLat, northEastLong, northEastLat);
			criteriaQuery = getConstructorForActualSiteForWifi(builder, rootElements, criteriaQuery);
			List<Predicate> finalPredicates = getPredicateForWifi(builder, rootElements, neType, neStatus,
					neFrequencyList, siteCategory, taskStatus);
			finalPredicates.addAll(InfraUtils.getPredicatesForViewPorts(builder, rootElements, viewportMap));
			Query query = getEntityManager()
					.createQuery(criteriaQuery.where(finalPredicates.toArray(new Predicate[] {})));
			networkElementList = query.getResultList();
		} catch (Exception exception) {
			logger.error("Exception  caught while  getting data from NetworkElement Exception : {}",
					Utils.getStackTrace(exception));
			throw new DaoException("Unable to Recieve Data from Networkelement for Wifi");
		}
		logger.info("Data Recieved for Wifi {} ", networkElementList.size());
		return networkElementList;
	}

	@Override
	public List<NetworkElementWrapper> getActualPlannedSites(String neType, String neStatus, Double southWestLong,
			Double southWestLat, Double northEastLong, Double northEastLat, List<String> neFrequencyList,
			List<String> siteCategory, List<String> taskStatus, List<Vendor> vendor, List<Technology> technologies,
			List<String> morphology) {
		Map<String, Double> viewportMap = new HashMap<>();
		try {
			CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<NetworkElementWrapper> criteriaQuery = builder.createQuery(NetworkElementWrapper.class);
			Root<NEBandDetail> rootElements = criteriaQuery.from(NEBandDetail.class);
			viewportMap = InfraUtils.getViewPortMap(southWestLong, southWestLat, northEastLong, northEastLat);
			criteriaQuery = getConstructorStatementForPlannedSite(builder, rootElements, criteriaQuery);
			List<Predicate> finalPredicates = new ArrayList<>();
			finalPredicates = getPredicateForActualSiteVisualisation(builder, rootElements, neType, neStatus,
					neFrequencyList, siteCategory, taskStatus, null, null, vendor, technologies, morphology, null);
			finalPredicates.addAll(InfraUtils.getPredicatesForViewPorts(builder, rootElements, viewportMap));
			Query query = getEntityManager()
					.createQuery(criteriaQuery.where(finalPredicates.toArray(new Predicate[] {})));
			return query.getResultList();
		} catch (Exception exception) {
			logger.error("Exception caught while getting data from NetworkElement Exception : {}",
					Utils.getStackTrace(exception));
			throw new DaoException("Unable to Recieve Data from Networkelement for Planned Site");
		}
	}

	/**
	 * Gets the constructor statement for planned site.
	 *
	 * @param builder       the builder
	 * @param networkRoot   the network root
	 * @param criteriaQuery the criteria query
	 * @return the constructor statement for planned site
	 */
	private CriteriaQuery<NetworkElementWrapper> getConstructorStatementForPlannedSite(CriteriaBuilder builder,
			Root networkRoot, CriteriaQuery<NetworkElementWrapper> criteriaQuery) {
		try {
			criteriaQuery.select(builder.construct(NetworkElementWrapper.class,
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NENAME),
					networkRoot.get(InfraConstants.NE_NEFREQUENCY_KEY), networkRoot.get(InfraConstants.NE_NESTATUS_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_LATITUDE_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_LONGITUDE_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY)
							.get(InfraConstants.BOUNDARY_NAME),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
					networkRoot.get("currentStage"), networkRoot.get("taskPercentage"),
					networkRoot.get(InfraConstants.SITE_NAME), networkRoot.get(InfraConstants.MODIFICATION_TIME_KEY)));

		} catch (Exception exception) {
			logger.error("Error in creating constructor for Planned Sites {}", Utils.getStackTrace(exception));
		}
		return criteriaQuery;
	}

	/**
	 * Gets the group by statement for wifi count.
	 *
	 * @param geographyLevel the geography level
	 * @param networkRoot    the network root
	 * @param criteriaQuery  the criteria query
	 * @return the group by statement for wifi count
	 */
	private CriteriaQuery<NetworkElementWrapper> getGroupByStatementForWifiCount(String geographyLevel,
			Root<WifiSiteDetail> networkRoot, CriteriaQuery<NetworkElementWrapper> criteriaQuery) {
		try {
			if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL1)) {
				criteriaQuery.groupBy(
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.BOUNDARY_NAME),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NESTATUS_KEY));
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL2)) {
				criteriaQuery.groupBy(
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY)
								.get(InfraConstants.BOUNDARY_NAME),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NESTATUS_KEY));
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL3)) {
				criteriaQuery.groupBy(
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.BOUNDARY_NAME),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NESTATUS_KEY));
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL4)) {
				criteriaQuery.groupBy(
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY)
								.get(InfraConstants.BOUNDARY_NAME),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NESTATUS_KEY));
			}
		} catch (Exception exception) {
			logger.error("Error in  creating Constructor for Wifi Count", Utils.getStackTrace(exception));

		}
		return criteriaQuery;
	}

	/**
	 * Gets the constructor statement for wifi count.
	 *
	 * @param geographyLevel the geography level
	 * @param builder        the builder
	 * @param networkRoot    the network root
	 * @param criteriaQuery  the criteria query
	 * @return the constructor statement for wifi count
	 */
	private CriteriaQuery<NetworkElementWrapper> getConstructorStatementForWifiCount(String geographyLevel,
			CriteriaBuilder builder, Root<WifiSiteDetail> networkRoot,
			CriteriaQuery<NetworkElementWrapper> criteriaQuery) {
		try {
			if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL1)) {
				criteriaQuery.select(builder.construct(NetworkElementWrapper.class,

						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.BOUNDARY_NAME),

						builder.countDistinct(networkRoot.get(InfraConstants.NE_NETWORKELEMENTID_KEY)),
						builder.avg(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
								.get(InfraConstants.NE_LATITUDE_KEY)),
						builder.avg(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
								.get(InfraConstants.NE_LONGITUDE_KEY)),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NESTATUS_KEY)));
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL2)) {
				criteriaQuery.select(builder.construct(NetworkElementWrapper.class,
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY)
								.get(InfraConstants.BOUNDARY_NAME),
						builder.countDistinct(networkRoot.get(InfraConstants.NE_NETWORKELEMENTID_KEY)),
						builder.avg(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
								.get(InfraConstants.NE_LATITUDE_KEY)),
						builder.avg(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
								.get(InfraConstants.NE_LONGITUDE_KEY)),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NESTATUS_KEY)));
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL3)) {
				criteriaQuery.select(builder.construct(NetworkElementWrapper.class,
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.BOUNDARY_NAME),
						builder.countDistinct(networkRoot.get(InfraConstants.NE_NETWORKELEMENTID_KEY)),
						builder.avg(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
								.get(InfraConstants.NE_LATITUDE_KEY)),
						builder.avg(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
								.get(InfraConstants.NE_LONGITUDE_KEY)),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NESTATUS_KEY)));
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL4)) {
				criteriaQuery.select(builder.construct(NetworkElementWrapper.class,
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY)
								.get(InfraConstants.BOUNDARY_NAME),
						builder.countDistinct(networkRoot.get(InfraConstants.NE_NETWORKELEMENTID_KEY)),
						builder.avg(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
								.get(InfraConstants.NE_LATITUDE_KEY)),
						builder.avg(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
								.get(InfraConstants.NE_LONGITUDE_KEY)),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NESTATUS_KEY)));
			}
		} catch (Exception exception) {

			logger.error("Error in getting geography for Wifi Count {}", Utils.getStackTrace(exception));
		}
		return criteriaQuery;
	}

	/**
	 * Gets the predicate for wifi.
	 *
	 * @param builder         the builder
	 * @param networkRoot     the network root
	 * @param neType          the ne type
	 * @param neStatus        the ne status
	 * @param neFrequencyList the ne frequency list
	 * @param siteCategory    the site category
	 * @param taskStatus      the task status
	 * @return the predicate for wifi
	 */
	private List<Predicate> getPredicateForWifi(CriteriaBuilder builder, Root networkRoot, List<NEType> neType,
			String neStatus, List<String> neFrequencyList, List<String> siteCategory, List<String> taskStatus) {
		List<Predicate> visualisationPredicate = new ArrayList<>();
		try {
			if (taskStatus != null && !taskStatus.isEmpty()) {
				Join<NEBandDetail, NETaskDetail> neTaskjoin = networkRoot.join(InfraConstants.NE_BAND_DETAIL_KEY,
						JoinType.INNER);
				visualisationPredicate
						.add((builder.upper(neTaskjoin.get(InfraConstants.TASK_STATUS_KEY)).in(taskStatus)));
			}
			if (neType != null) {
				visualisationPredicate.add((builder
						.upper(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY))
						.in(neType)));
			}
			if (neStatus != null) {
				visualisationPredicate.add((builder.equal(
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NESTATUS_KEY),
						NEStatus.valueOf(neStatus))));
			}

			visualisationPredicate.add((builder.equal(
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_ISDELETED_KEY), 0)));

		} catch (Exception exception) {

			logger.error("Error in getting Predicate for Layer Count", Utils.getStackTrace(exception));
		}

		return visualisationPredicate;
	}

	@Override
	public List<NetworkElementWrapper> getNetworkElementForAggregateLayerCount(String neType, String neStatus,
			Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat, String geographyLevel,
			List<String> neFrequencyList, List<String> siteCategory, List<String> taskStatus, List<Vendor> vendor,
			List<Technology> technologies, List<String> geographyList, List<String> morphology) {
		List<NetworkElementWrapper> networkElementList = new ArrayList<>();
		try {
			CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<NetworkElementWrapper> criteriaQuery = builder.createQuery(NetworkElementWrapper.class);
			Root<NEBandDetail> networkRoot = criteriaQuery.from(NEBandDetail.class);
			criteriaQuery = getConstructorStatementForLayerCount(geographyLevel, builder, networkRoot, criteriaQuery,
					southWestLat, northEastLat, southWestLong, northEastLong);
			List<Predicate> predicateList = getPredicateForLayerCount(criteriaQuery, builder, networkRoot, neType,
					neStatus, neFrequencyList, siteCategory, taskStatus, vendor, technologies, morphology);
			if (geographyList != null && !geographyList.isEmpty()) {
				predicateList = getPredicateForGeography(geographyLevel, predicateList, networkRoot, geographyList,
						builder);
			}
			criteriaQuery = getGroupByStatementForLayerCount(geographyLevel, networkRoot, criteriaQuery);
			Query query = getEntityManager()
					.createQuery(criteriaQuery.where(predicateList.toArray(new Predicate[] {})));
			networkElementList = query.getResultList();
		} catch (Exception exception) {
			logger.error("Error while getting Aggregate Layer Count {}", Utils.getStackTrace(exception));
			throw new DaoException("Unable to get Data from NetworkElement");
		}
		logger.info("NetworkElement list size : " + networkElementList.size());
		return networkElementList;
	}

	@Override
	public List<String> getDistinctGeography(String geograhyLevel, Double southWestLong, Double southWestLat,
			Double northEastLong, Double northEastLat) {
		Map<String, Double> viewportMap = new HashMap<>();
		List<String> geographyList = new ArrayList();
		try {
			CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<NetworkElement> criteriaQuery = builder.createQuery(NetworkElement.class);
			Root<NetworkElement> networkRoot = criteriaQuery.from(NetworkElement.class);
			viewportMap = InfraUtils.getViewPortMap(southWestLong, southWestLat, northEastLong, northEastLat);
			List<Predicate> predicateList = InfraUtils.getPredicatesForViewPortsForNE(builder, networkRoot,
					viewportMap);
			criteriaQuery = getGeographyCriteria(geograhyLevel, criteriaQuery, networkRoot);
			if (criteriaQuery != null) {
				Query query = getEntityManager()
						.createQuery(criteriaQuery.where(predicateList.toArray(new Predicate[] {})));
				geographyList = query.getResultList();
			}
		} catch (IllegalArgumentException | PersistenceException exception) {
			logger.error("Error while getting distinct geogroahy  {}", Utils.getStackTrace(exception));
			throw new DaoException("Unable to get distinct Geography");
		} catch (Exception exception) {
			logger.error("Error while getting  distinct geogroahy  {}", Utils.getStackTrace(exception));
			throw new DaoException("Unable to get distinct Geography");
		}
		return geographyList;
	}

	@SuppressWarnings("rawtypes")
	private CriteriaQuery getGeographyCriteria(String geograhyLevel, CriteriaQuery criteriaQuery, Root networkRoot) {
		if (geograhyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL1_TABLE)) {
			return criteriaQuery.select(networkRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
					.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY)
					.get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.BOUNDARY_NAME)).distinct(true);
		} else if (geograhyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL2_TABLE)) {
			return criteriaQuery
					.select(networkRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
							.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.BOUNDARY_NAME))
					.distinct(true);
		} else if (geograhyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL3_TABLE)) {
			return criteriaQuery.select(networkRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
					.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.BOUNDARY_NAME)).distinct(true);
		} else if (geograhyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL4_TABLE)) {
			return criteriaQuery
					.select(networkRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.BOUNDARY_NAME))
					.distinct(true);
		}
		return null;
	}

	@Override
	public List<NetworkElementWrapper> getNetworkElementForActualSites(String neType, String neStatus,
			Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat,
			List<String> neFrequencyList, List<String> siteCategory, List<String> taskStatus, String criteria,
			String failureRate, List<Vendor> vendor, List<Technology> technologies, List<String> morphology,
			String geography) {
		List<NetworkElementWrapper> networkElementList = new ArrayList<>();
		Map<String, Double> viewportMap = new HashMap<>();
		logger.info(
				"Going to get Network element  for Actual sites neStatus {},neType{},neFrequencyList{}, criteria {},failureRate {}",
				neStatus, neType, neFrequencyList, criteria, failureRate);
		try {
			Root networkRoot = null;
			CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<NetworkElementWrapper> criteriaQuery = builder.createQuery(NetworkElementWrapper.class);
			if (neType.equalsIgnoreCase(NEType.ODSC_CELL.name())) {
				networkRoot = criteriaQuery.from(RANDetail.class);
				criteriaQuery = getConstructorStatementForSmallCell(builder, networkRoot, criteriaQuery);
			} else {
				networkRoot = criteriaQuery.from(RANDetail.class);
				criteriaQuery = getConstructorStatementForActualSite(builder, networkRoot, criteriaQuery);
			}
			List<Predicate> predicateList = getPredicateForActualSiteVisualisation(builder, networkRoot, neType,
					neStatus, neFrequencyList, siteCategory, taskStatus, criteria, failureRate, vendor, technologies,
					morphology, geography);
			viewportMap = InfraUtils.getViewPortMap(southWestLong, southWestLat, northEastLong, northEastLat);
			List<Predicate> finalPredicates = new ArrayList<>();
			List<Predicate> viewPortPredicates = InfraUtils.getPredicatesForViewPorts(builder, networkRoot,
					viewportMap);
			finalPredicates.addAll(predicateList);
			finalPredicates.addAll(viewPortPredicates);
			Query query = getEntityManager()
					.createQuery(criteriaQuery.where(finalPredicates.toArray(new Predicate[] {})));
			networkElementList = query.getResultList();
			logger.info("NetworkElement size for Actual site {}", networkElementList.size());
		} catch (Exception exception) {
			logger.error("Exception caught while getting data from NetworkElement Exception : {}",
					Utils.getStackTrace(exception));
			throw new DaoException("Data Not Recieved For Site Visualisation");
		}
		return networkElementList;
	}

	/**
	 * Gets the constructor statement for actual site.
	 *
	 * @param builder       the builder
	 * @param networkRoot   the network root
	 * @param criteriaQuery the criteria query
	 * @return the constructor statement for actual site
	 */
	private CriteriaQuery<NetworkElementWrapper> getConstructorStatementForActualSite(CriteriaBuilder builder,
			Root networkRoot, CriteriaQuery<NetworkElementWrapper> criteriaQuery) {
		try {
			criteriaQuery.select(builder.construct(NetworkElementWrapper.class,
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETWORKELEMENT_KEY)
							.get(InfraConstants.NENAME),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_VENDOR_KEY),

					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NEFREQUENCY_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NENAME_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_CELLNUM_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY)
							.get(InfraConstants.BOUNDARY_NAME),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_LATITUDE_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_LONGITUDE_KEY),
					networkRoot.get(InfraConstants.PCI_KEY), networkRoot.get(InfraConstants.AZIMUTH_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NESTATUS_KEY),
					networkRoot.get(InfraConstants.SECTOR_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NESTATUS_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.ECGI),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NEID_KEY),
					networkRoot.get(InfraConstants.NE_BAND_DETAIL_KEY).get(InfraConstants.NE_NETWORKELEMENT_KEY)
							.get("friendlyname"),
					networkRoot.get(InfraConstants.MODIFICATION_TIME_KEY), networkRoot.get(InfraConstants.EARFCN_KEY),
					networkRoot.get(InfraConstants.NE_BAND_DETAIL_KEY).get(InfraConstants.NE_CARRIER_KEY),
					// networkRoot.get(InfraConstants.NE_CGI_KEY),
					// networkRoot.get(InfraConstants.NE_BAND_DETAIL_KEY).get(InfraConstants.CLUTTER_CATEGORY_KEY),
					networkRoot.get(InfraConstants.NE_BAND_DETAIL_KEY).get("neDetail")
							.get(InfraConstants.NE_MORPHOLOGY_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get("enbid"),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NENAME),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETWORKELEMENT_KEY)
							.get(InfraConstants.NE_NEID_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_DOMAIN_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_TECHNOLOGY_KEY),
					networkRoot.get(InfraConstants.NE_ANTENNA_HEIGHT), networkRoot.get(InfraConstants.NE_MECH_TILT),
					networkRoot.get(InfraConstants.NE_ELEC_TILT)));
		} catch (Exception exception) {
			logger.error("Error in creating constructor for Actual Sites {}", Utils.getStackTrace(exception));
		}
		return criteriaQuery;
	}

	private CriteriaQuery<NetworkElementWrapper> getConstructorStatementForSmallCell(CriteriaBuilder builder,
			Root networkRoot, CriteriaQuery<NetworkElementWrapper> criteriaQuery) {
		try {
			criteriaQuery.select(builder.construct(NetworkElementWrapper.class,
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETWORKELEMENT_KEY)
							.get(InfraConstants.NENAME),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_VENDOR_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NEFREQUENCY_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NENAME_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_CELLNUM_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY)
							.get(InfraConstants.BOUNDARY_NAME),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_LATITUDE_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_LONGITUDE_KEY),
					networkRoot.get(InfraConstants.PCI_KEY), networkRoot.get(InfraConstants.AZIMUTH_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NESTATUS_KEY),
					networkRoot.get(InfraConstants.SECTOR_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NESTATUS_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.ECGI),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NEID_KEY),
					networkRoot.get(InfraConstants.NE_BAND_DETAIL_KEY).get(InfraConstants.NE_NETWORKELEMENT_KEY)
							.get("friendlyname"),
					networkRoot.get(InfraConstants.MODIFICATION_TIME_KEY), networkRoot.get(InfraConstants.EARFCN_KEY),
					// networkRoot.get("neBandDetail").get(InfraConstants.CLUTTER_CATEGORY_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get("enbid"),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NENAME),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETWORKELEMENT_KEY)
							.get(InfraConstants.NE_NEID_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_DOMAIN_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_TECHNOLOGY_KEY),
					networkRoot.get("antennaHeight"), networkRoot.get(InfraConstants.NE_MECH_TILT),
					networkRoot.get(InfraConstants.NE_ELEC_TILT)));
		} catch (Exception exception) {
			logger.error("Error in creating constructor for Actual Sites of SmallCell {}",
					Utils.getStackTrace(exception));
		}
		return criteriaQuery;
	}

	/**
	 * Gets the predicate for actual site visualisation.
	 *
	 * @param builder         the builder
	 * @param networkRoot     the network root
	 * @param neType          the ne type
	 * @param neStatus        the ne status
	 * @param neFrequencyList the ne frequency list
	 * @param siteCategory    the site category
	 * @param taskStatus      the task status
	 * @param criteria        the criteria
	 * @param failureRate     the failure rate
	 * @return the predicate for actual site visualisation
	 */
	private List<Predicate> getPredicateForActualSiteVisualisation(CriteriaBuilder builder, Root networkRoot,
			String neType, String neStatus, List<String> neFrequencyList, List<String> siteCategory,
			List<String> taskStatus, String criteria, String failureRate, List<Vendor> vendor,
			List<Technology> technologies, List<String> morphology, String geography) {
		List<Predicate> sitePredicateList = new ArrayList<>();
		try {
			if (taskStatus != null && !taskStatus.isEmpty()) {
				Join<RANDetail, NETaskDetail> neTaskjoin = networkRoot.join(InfraConstants.NE_BAND_DETAIL_KEY,
						JoinType.INNER);
				sitePredicateList.add((builder.upper(neTaskjoin.get(InfraConstants.TASK_STATUS_KEY)).in(taskStatus)));
			}
			if (neType != null) {
				sitePredicateList.add((builder.equal(
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
						NEType.valueOf(neType))));
			}

			sitePredicateList
					.add((builder.equal(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get("isDeleted"), 0)));

			if (neStatus != null) {
				sitePredicateList.add((builder.equal(
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NESTATUS_KEY),
						NEStatus.valueOf(neStatus))));
			}
			sitePredicateList.add(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
					.get(InfraConstants.NE_LATITUDE_KEY).isNotNull());
			sitePredicateList.add(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
					.get(InfraConstants.NE_LONGITUDE_KEY).isNotNull());
			if (neFrequencyList != null && !neFrequencyList.isEmpty()) {
				if (neStatus.equalsIgnoreCase(InfraConstants.PLANNED)) {
					sitePredicateList.add((builder.upper(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
							.get(InfraConstants.NE_NEFREQUENCY_KEY)).in(neFrequencyList)));

				} else {
					sitePredicateList.add((builder.upper(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
							.get(InfraConstants.NE_NEFREQUENCY_KEY)).in(neFrequencyList)));
				}
			}
			sitePredicateList
					.add((builder.equal(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get("isDeleted"), 0)));
			if (vendor != null && !vendor.isEmpty()) {
				sitePredicateList.add((builder
						.upper(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.VENDOR_KEY))
						.in(vendor)));
			}
			if (technologies != null && !technologies.isEmpty()) {
				sitePredicateList.add((builder.upper(
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_TECHNOLOGY_KEY))
						.in(technologies)));
			}
			if (siteCategory != null && !siteCategory.isEmpty()) {
				sitePredicateList.add((builder
						.upper(networkRoot.get(InfraConstants.NE_BANDDETAIL_KEY).get("neDetail").get("category"))
						.in(siteCategory)));
			}
			if (morphology != null && !morphology.isEmpty()) {
				sitePredicateList.add((builder
						.upper(networkRoot.get(InfraConstants.NE_BANDDETAIL_KEY).get("neDetail").get("morphology"))
						.in(morphology)));
			}
			if (geography != null && !geography.isEmpty()) {
				sitePredicateList.add((builder.equal(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
						.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NAME), geography)));
			}

			if (criteria != null && failureRate != null) {
				if (criteria.equalsIgnoreCase(InfraConstants.PCI_LT_CRITERIA_KEY)) {
					sitePredicateList.add(builder.lessThan(networkRoot.get(InfraConstants.NETWORKELEMENT_PCI_KEY),
							Integer.valueOf(failureRate)));
				} else if (criteria.equalsIgnoreCase(InfraConstants.PCI_GT_CRITERIA_KEY)) {
					sitePredicateList.add(builder.greaterThan(networkRoot.get(InfraConstants.NETWORKELEMENT_PCI_KEY),
							Integer.valueOf(failureRate)));
				} else if (criteria.equalsIgnoreCase(InfraConstants.PCI_LTE_CRITERIA_KEY)) {
					sitePredicateList.add(builder.lessThanOrEqualTo(
							networkRoot.get(InfraConstants.NETWORKELEMENT_PCI_KEY), Integer.valueOf(failureRate)));
				} else if (criteria.equalsIgnoreCase(InfraConstants.PCI_GTE_CRITERIA_KEY)) {
					sitePredicateList.add(builder.greaterThanOrEqualTo(
							networkRoot.get(InfraConstants.NETWORKELEMENT_PCI_KEY), Integer.valueOf(failureRate)));
				} else if (criteria.equalsIgnoreCase(InfraConstants.PCI_EQ_CRITERIA_KEY)) {
					sitePredicateList.add(builder.equal(networkRoot.get(InfraConstants.NETWORKELEMENT_PCI_KEY),
							Integer.valueOf(failureRate)));
				} else if (criteria.equalsIgnoreCase(InfraConstants.PCI_BETWEEN_CRITERIA_KEY)) {
					String pciValues[] = failureRate.split(ForesightConstants.HIPHEN);
					if (pciValues != null && pciValues.length > 0) {
						sitePredicateList.add(builder.between(networkRoot.get(InfraConstants.NETWORKELEMENT_PCI_KEY),
								Integer.valueOf(pciValues[0]), Integer.valueOf(pciValues[1])));
					}
				}
			}
		} catch (Exception exception) {
			logger.error("Unable to create predicate for Actual Site visualisation {}", Utils.getStackTrace(exception));
		}
		return sitePredicateList;
	}

	/**
	 * Gets the constructor for actual site for wifi.
	 *
	 * @param builder       the builder
	 * @param networkRoot   the network root
	 * @param criteriaQuery the criteria query
	 * @return the constructor for actual site for wifi
	 */
	private CriteriaQuery<NetworkElementWrapper> getConstructorForActualSiteForWifi(CriteriaBuilder builder,
			Root networkRoot, CriteriaQuery<NetworkElementWrapper> criteriaQuery) {
		try {
			criteriaQuery.select(builder.construct(NetworkElementWrapper.class,
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NENAME),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_VENDOR_KEY),
					networkRoot.get(InfraConstants.ID),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY)
							.get(InfraConstants.BOUNDARY_NAME),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_LATITUDE_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_LONGITUDE_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_STATUS),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_DOMAIN_KEY),
					networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_TECHNOLOGY_KEY)));

		} catch (Exception exception) {
			logger.error("Error in creating constructor for Actual Sites {}", Utils.getStackTrace(exception));
		}
		return criteriaQuery;
	}

	/**
	 * Gets the constructor statement for layer count.
	 *
	 * @param geographyLevel the geography level
	 * @param builder        the builder
	 * @param networkRoot    the network root
	 * @param criteriaQuery  the criteria query
	 * @return the constructor statement for layer count
	 */
	private CriteriaQuery<NetworkElementWrapper> getConstructorStatementForLayerCount(String geographyLevel,
			CriteriaBuilder builder, Root<NEBandDetail> networkRoot, CriteriaQuery<NetworkElementWrapper> criteriaQuery,
			Double smalllat, Double largeLat, Double smallLong, Double largelong) {
		try {
			if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL1)) {
				criteriaQuery.select(builder.construct(NetworkElementWrapper.class,
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.BOUNDARY_NAME),
						builder.countDistinct(networkRoot.get(InfraConstants.NE_NETWORKELEMENTID_KEY)),
						builder.max(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.NE_GEOGRAPHYL1_KEY)
								.get(InfraConstants.NE_LATITUDE_KEY)),
						builder.max(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.NE_GEOGRAPHYL1_KEY)
								.get(InfraConstants.NE_LONGITUDE_KEY)),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
						networkRoot.get(InfraConstants.NE_NESTATUS_KEY),
						networkRoot.get(InfraConstants.NE_NEFREQUENCY_KEY),
						builder.max(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.NE_GEOGRAPHYL1_KEY)
								.get(InfraConstants.NE_LATITUDE_KEY)),
						builder.max(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.NE_GEOGRAPHYL1_KEY)
								.get(InfraConstants.NE_LONGITUDE_KEY))));
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL2)) {
				criteriaQuery
						.select(builder.construct(NetworkElementWrapper.class,
								networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
										.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(
												InfraConstants.NE_GEOGRAPHYL3_KEY)
										.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.BOUNDARY_NAME),
								builder.countDistinct(networkRoot
										.get(InfraConstants.NE_NETWORKELEMENTID_KEY)),
								builder.avg(builder.<Number>selectCase()
										.when(builder.between(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
												.get(InfraConstants.NE_LATITUDE_KEY), smalllat, largeLat),
												networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
														.get(InfraConstants.NE_LATITUDE_KEY))
										.otherwise(builder.nullLiteral(Number.class))),
								builder.avg(builder.<Number>selectCase()
										.when(builder.between(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
												.get(InfraConstants.NE_LONGITUDE_KEY), smallLong, largelong),
												networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
														.get(InfraConstants.NE_LONGITUDE_KEY))
										.otherwise(builder.nullLiteral(Number.class))),
								networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
								networkRoot.get(InfraConstants.NE_NESTATUS_KEY),
								networkRoot.get(InfraConstants.NE_NEFREQUENCY_KEY),
								builder.max(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
										.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
										.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.NE_LATITUDE_KEY)),
								builder.max(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
										.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
										.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.NE_LONGITUDE_KEY))));
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL3)) {
				criteriaQuery.select(
						builder.construct(NetworkElementWrapper.class,
								networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
										.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
										.get(InfraConstants.BOUNDARY_NAME),
								builder.countDistinct(networkRoot.get(InfraConstants.NE_NETWORKELEMENTID_KEY)),
								builder.avg(builder.<Number>selectCase()
										.when(builder.between(
												networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
														.get(InfraConstants.NE_LATITUDE_KEY),
												smalllat, largeLat),
												networkRoot.get(
														InfraConstants.NE_NETWORKELEMENT_KEY)
														.get(InfraConstants.NE_LATITUDE_KEY))
										.otherwise(builder.nullLiteral(Number.class))),
								builder.avg(builder.<Number>selectCase()
										.when(builder.between(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
												.get(InfraConstants.NE_LONGITUDE_KEY), smallLong, largelong),
												networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
														.get(InfraConstants.NE_LONGITUDE_KEY))
										.otherwise(builder.nullLiteral(Number.class))),
								networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
								networkRoot.get(InfraConstants.NE_NESTATUS_KEY),
								networkRoot.get(InfraConstants.NE_NEFREQUENCY_KEY),
								builder.max(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
										.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
										.get(InfraConstants.NE_LATITUDE_KEY)),
								builder.max(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
										.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
										.get(InfraConstants.NE_LONGITUDE_KEY))));
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL4)) {
				criteriaQuery
						.select(builder.construct(NetworkElementWrapper.class,
								networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
										.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.BOUNDARY_NAME),
								builder.countDistinct(networkRoot
										.get(InfraConstants.NE_NETWORKELEMENTID_KEY)),
								builder.avg(
										builder.<Number>selectCase()
												.when(builder
														.between(
																networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
																		.get(InfraConstants.NE_LATITUDE_KEY),
																smalllat, largeLat),
														networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
																.get(InfraConstants.NE_LATITUDE_KEY))
												.otherwise(builder.nullLiteral(Number.class))),
								builder.avg(builder.<Number>selectCase()
										.when(builder.between(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
												.get(InfraConstants.NE_LONGITUDE_KEY), smallLong, largelong),
												networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
														.get(InfraConstants.NE_LONGITUDE_KEY))
										.otherwise(builder.nullLiteral(Number.class))),
								networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
								networkRoot.get(InfraConstants.NE_NESTATUS_KEY),
								networkRoot.get(InfraConstants.NE_NEFREQUENCY_KEY),
								builder.max(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
										.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_LATITUDE_KEY)),
								builder.max(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
										.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_LONGITUDE_KEY))));
			}
		} catch (Exception exception) {
			logger.error("Error in getting geography for Layer Count {}", Utils.getStackTrace(exception));
		}
		return criteriaQuery;
	}

	/**
	 * Gets the predicate for layer count and visualisation.
	 *
	 * @param criteriaQuery   the criteria query
	 * @param builder         the builder
	 * @param networkRoot     the network root
	 * @param neType          the ne type
	 * @param neStatus        the ne status
	 * @param neFrequencyList the ne frequency list
	 * @param siteCategory    the site category
	 * @param taskStatus      the task status
	 * @return the predicate for layer count and visualisation
	 */
	private List<Predicate> getPredicateForLayerCount(CriteriaQuery criteriaQuery, CriteriaBuilder builder,
			Root networkRoot, String neType, String neStatus, List<String> neFrequencyList, List<String> siteCategory,
			List<String> taskStatus, List<Vendor> vendor, List<Technology> technologies, List<String> morphology) {
		List<Predicate> visualisationPredicate = new ArrayList<>();
		try {
			if (taskStatus != null && !taskStatus.isEmpty()) {
				Root<NETaskDetail> taskNetworkRoot = criteriaQuery.from(NETaskDetail.class);
				visualisationPredicate
						.add((builder.upper(taskNetworkRoot.get(InfraConstants.TASK_NAME_KEY)).in(taskStatus)));
				visualisationPredicate
						.add((builder.equal(builder.upper(taskNetworkRoot.get(InfraConstants.COMPLETION_STATUS_KEY)),
								InfraConstants.YES_UPERCASE)));
				visualisationPredicate.add((builder.equal(taskNetworkRoot.get(InfraConstants.NE_BAND_DETAIL_KEY),
						networkRoot.get(InfraConstants.ID))));
			}
			if (neType != null) {
				visualisationPredicate.add((builder.equal(
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
						NEType.valueOf(neType))));
			}
			if (neStatus != null) {
				visualisationPredicate.add(
						(builder.equal(networkRoot.get(InfraConstants.NE_NESTATUS_KEY), NEStatus.valueOf(neStatus))));
			}
			if (neFrequencyList != null && !neFrequencyList.isEmpty()) {
				visualisationPredicate
						.add((builder.upper(networkRoot.get(InfraConstants.NE_NEFREQUENCY_KEY)).in(neFrequencyList)));
			}
			if (siteCategory != null && !siteCategory.isEmpty()) {
				visualisationPredicate
						.add((builder.upper(networkRoot.get(InfraConstants.SITE_CATEGORY)).in(siteCategory)));
			}
			if (vendor != null && !vendor.isEmpty()) {
				visualisationPredicate.add((builder
						.upper(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.VENDOR_KEY))
						.in(vendor)));
			}
			if (technologies != null && !technologies.isEmpty()) {
				visualisationPredicate.add((builder.upper(
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_TECHNOLOGY_KEY))
						.in(technologies)));
			}
			if (morphology != null && !morphology.isEmpty()) {
				visualisationPredicate.add((builder.upper(networkRoot.get("morphology")).in(morphology)));
			}
			visualisationPredicate.add(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
					.get(InfraConstants.NE_LATITUDE_KEY).isNotNull());
			visualisationPredicate.add(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
					.get(InfraConstants.NE_LONGITUDE_KEY).isNotNull());

		} catch (Exception exception) {
			logger.error("Error in getting Predicate for Layer Count", Utils.getStackTrace(exception));
		}
		return visualisationPredicate;
	}

	/**
	 * Gets the group by statement for layer count.
	 *
	 * @param geographyLevel the geography level
	 * @param networkRoot    the network root
	 * @param criteriaQuery  the criteria query
	 * @return the group by statement for layer count
	 */
	private CriteriaQuery<NetworkElementWrapper> getGroupByStatementForLayerCount(String geographyLevel,
			Root<NEBandDetail> networkRoot, CriteriaQuery<NetworkElementWrapper> criteriaQuery) {
		try {
			if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL1)) {
				criteriaQuery.groupBy(
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.BOUNDARY_NAME),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
						networkRoot.get(InfraConstants.NE_NESTATUS_KEY),
						networkRoot.get(InfraConstants.NE_NEFREQUENCY_KEY));
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL2)) {
				criteriaQuery.groupBy(
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY)
								.get(InfraConstants.BOUNDARY_NAME),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
						networkRoot.get(InfraConstants.NE_NESTATUS_KEY),
						networkRoot.get(InfraConstants.NE_NEFREQUENCY_KEY));
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL3)) {
				criteriaQuery.groupBy(
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.BOUNDARY_NAME),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
						networkRoot.get(InfraConstants.NE_NESTATUS_KEY),
						networkRoot.get(InfraConstants.NE_NEFREQUENCY_KEY));
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL4)) {
				criteriaQuery.groupBy(
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY)
								.get(InfraConstants.BOUNDARY_NAME),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
						networkRoot.get(InfraConstants.NE_NESTATUS_KEY),
						networkRoot.get(InfraConstants.NE_NEFREQUENCY_KEY));
			}
		} catch (Exception exception) {
			logger.error("Error in  creating Constructor for Layer Count", Utils.getStackTrace(exception));

		}
		return criteriaQuery;
	}

	@Override
	@Deprecated
	public List<String> getNeFrequencyByDomainAndVendor(String domain, String vendor) {
		List<String> neFrequency = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getNeFrequencyByDomainAndVendor");
			query.setParameter(InfraConstants.NE_DOMAIN_KEY, Domain.valueOf(domain.toUpperCase()));
			query.setParameter(InfraConstants.VENDOR_KEY, Vendor.valueOf(vendor.toUpperCase()));
			neFrequency = query.getResultList();
		} catch (IllegalArgumentException e) {
			logger.warn("IllegalArgumentException while getNeFrequencyByDomainAndVendor,err msg{}", e.getMessage());
		} catch (IllegalStateException e) {
			logger.warn("IllegalStateException while getNeFrequencyByDomainAndVendor,err msg{}", e.getMessage());
		} catch (QueryTimeoutException e) {
			logger.warn("QueryTimeoutException while getNeFrequencyByDomainAndVendor,err msg{}", e.getMessage());
		} catch (TransactionRequiredException e) {
			logger.warn("TransactionRequiredException while getNeFrequencyByDomainAndVendor,err msg{}", e.getMessage());
		} catch (PersistenceException e) {
			logger.warn("PersistenceException while getNeFrequencyByDomainAndVendor,err msg{}", e.getMessage());
		}
		return neFrequency;
	}

	@Override
	public List<NetworkElementWrapper> getNetworkElementForTableView(String neType, String neStatus,
			Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat, String geographyLevel,
			List<String> neFrequencyList, List<String> siteCategory, List<String> taskStatus, List<Vendor> vendor,
			List<Technology> technologies, List<String> geographyList, List<String> morphology) {
		List<NetworkElementWrapper> networkElementList = new ArrayList<>();
		try {
			CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<NetworkElementWrapper> criteriaQuery = builder.createQuery(NetworkElementWrapper.class);
			Root<NEBandDetail> networkRoot = criteriaQuery.from(NEBandDetail.class);
			criteriaQuery = getConstructorStatementForTableView(geographyLevel, builder, networkRoot, criteriaQuery,
					southWestLat, northEastLat, southWestLong, northEastLong);
			List<Predicate> predicateList = getPredicateForLayerCount(criteriaQuery, builder, networkRoot, neType,
					neStatus, neFrequencyList, siteCategory, taskStatus, vendor, technologies, morphology);
			if (geographyList != null && !geographyList.isEmpty()) {
				predicateList = getPredicateForGeography(geographyLevel, predicateList, networkRoot, geographyList,
						builder);
			}
			criteriaQuery = getGroupByStatementForTableView(geographyLevel, networkRoot, criteriaQuery);
			Query query = getEntityManager()
					.createQuery(criteriaQuery.where(predicateList.toArray(new Predicate[] {})));
			networkElementList = query.getResultList();
		} catch (Exception exception) {
			logger.error("Error while getting Aggregate Layer Count {}", Utils.getStackTrace(exception));
			throw new DaoException("Unable to get data for table View");
		}
		logger.info("Data Recieved From NetworkElement {}", networkElementList.size());
		return networkElementList;
	}

	/**
	 * Gets the constructor statement for table view.
	 *
	 * @param geographyLevel the geography level
	 * @param builder        the builder
	 * @param networkRoot    the network root
	 * @param criteriaQuery  the criteria query
	 * @return the constructor statement for table view
	 */
	private CriteriaQuery<NetworkElementWrapper> getConstructorStatementForTableView(String geographyLevel,
			CriteriaBuilder builder, Root<NEBandDetail> networkRoot, CriteriaQuery<NetworkElementWrapper> criteriaQuery,
			Double smalllat, Double largeLat, Double smallLong, Double largelong) {
		try {
			if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL1)) {
				criteriaQuery.select(builder.construct(NetworkElementWrapper.class,
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.BOUNDARY_NAME),
						builder.countDistinct(networkRoot.get(InfraConstants.NE_NETWORKELEMENTID_KEY)),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_LATITUDE_KEY),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_LONGITUDE_KEY),
						networkRoot.get(InfraConstants.NE_NESTATUS_KEY),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
						builder.max(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
								.get(InfraConstants.NE_MODIFIED_KEY))));
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL2)) {
				criteriaQuery.select(builder.construct(NetworkElementWrapper.class,
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY)
								.get(InfraConstants.BOUNDARY_NAME),
						builder.countDistinct(networkRoot.get(InfraConstants.NE_NETWORKELEMENTID_KEY)),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_LATITUDE_KEY),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_LONGITUDE_KEY),
						networkRoot.get(InfraConstants.NE_NESTATUS_KEY),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
						builder.max(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
								.get(InfraConstants.NE_MODIFIED_KEY))));
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL3)) {
				criteriaQuery.select(builder.construct(NetworkElementWrapper.class,
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.BOUNDARY_NAME),
						builder.countDistinct(networkRoot.get(InfraConstants.NE_NETWORKELEMENTID_KEY)),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_LATITUDE_KEY),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_LONGITUDE_KEY),
						networkRoot.get(InfraConstants.NE_NESTATUS_KEY),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
						builder.max(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
								.get(InfraConstants.NE_MODIFIED_KEY))));
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL4)) {
				criteriaQuery.select(builder.construct(NetworkElementWrapper.class,
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY)
								.get(InfraConstants.BOUNDARY_NAME),
						builder.countDistinct(networkRoot.get(InfraConstants.NE_NETWORKELEMENTID_KEY)),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_LATITUDE_KEY),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_LONGITUDE_KEY),
						networkRoot.get(InfraConstants.NE_NESTATUS_KEY),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
						builder.max(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
								.get(InfraConstants.NE_MODIFIED_KEY))));
			}
		} catch (Exception exception) {
			logger.error("Error in getting geography for Layer Count {}", Utils.getStackTrace(exception));
		}
		return criteriaQuery;
	}

	/**
	 * Gets the group by statement for table view.
	 *
	 * @param geographyLevel the geography level
	 * @param networkRoot    the network root
	 * @param criteriaQuery  the criteria query
	 * @return the group by statement for table view
	 */
	private CriteriaQuery<NetworkElementWrapper> getGroupByStatementForTableView(String geographyLevel,
			Root<NEBandDetail> networkRoot, CriteriaQuery<NetworkElementWrapper> criteriaQuery) {
		try {
			if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL1)) {
				criteriaQuery.groupBy(
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.BOUNDARY_NAME),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
						networkRoot.get(InfraConstants.NE_NESTATUS_KEY));
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL2)) {
				criteriaQuery.groupBy(
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY)
								.get(InfraConstants.BOUNDARY_NAME),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
						networkRoot.get(InfraConstants.NE_NESTATUS_KEY));
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL3)) {
				criteriaQuery.groupBy(
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.BOUNDARY_NAME),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
						networkRoot.get(InfraConstants.NE_NESTATUS_KEY));
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL4)) {
				criteriaQuery.groupBy(
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY)
								.get(InfraConstants.BOUNDARY_NAME),
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NETYPE_KEY),
						networkRoot.get(InfraConstants.NE_NESTATUS_KEY));
			}
		} catch (Exception exception) {
			logger.error("Error in  creating Constructor for Layer Count", Utils.getStackTrace(exception));

		}
		return criteriaQuery;
	}

	/**
	 * Gets the NE detail by NE id.
	 *
	 * @param neId the ne id
	 * @return the NE detail by NE id
	 * @throws DaoException the dao exception
	 */
	@Override
	@Deprecated
	public NetworkElement getNEDetailByNEId(String neId) {
		logger.info("Going to get NetworkElement by neId");
		NetworkElement networkElementData = new NetworkElement();
		try {
			Query query = getEntityManager().createNamedQuery("getNEDetailByNEId")
					.setParameter(InfraConstants.NE_NEID_KEY, neId.toUpperCase());
			networkElementData = (NetworkElement) query.getSingleResult();
		} catch (NullPointerException nullPointerException) {
			logger.error("NullPointerException caught while getting  NetworkElement data  by neId Exception : {}",
					Utils.getStackTrace(nullPointerException));
			throw new DaoException("Unable  to get NetworkElement  data for neId .");
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error(" IllegalArgumentException caught while getting NetworkElement data  by neId Exception : {}",
					Utils.getStackTrace(illegalArgumentException));
			throw new DaoException(" Unable to  get NetworkElement data for neId .");
		} catch (NoResultException noResultException) {
			logger.error("NoResultException caught while getting NetworkElement data  by  neId Exception : {}",
					Utils.getStackTrace(noResultException));
			throw new DaoException("Unable to get NetworkElement  data for neId .");
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while getting NetworkElement  data  by neId Exception : {}",
					Utils.getStackTrace(persistenceException));
			throw new DaoException("Unable to get NetworkElement data for neId . ");
		} catch (Exception exception) {
			logger.error("Exception caught while getting  NetworkElement data  by neId Exception : {}",
					Utils.getStackTrace(exception));
			throw new DaoException("Unable to get  NetworkElement data  for neId .");
		}
		return networkElementData;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NETaskDetail> getNETaskDetailForNE(String neName) {
		List<NETaskDetail> list = new ArrayList<>();
		try {
			logger.info("Going to get NETaskDetail data for neName  {}", neName);
			Query query = getEntityManager().createNamedQuery("getNETaskDetailForNE")
					.setParameter(InfraConstants.NENAME, neName.toUpperCase())
					.setParameter(InfraConstants.NE_TYPE, NEType.MACRO);
			return query.getResultList();
		} catch (NoResultException noResultException) {
			logger.error("NoResultException caught while getting NETaskDetail data for neName {} Exception {}", neName,
					Utils.getStackTrace(noResultException));
		} catch (Exception exception) {
			logger.error("Exception caught while getting NETaskDetail data for neName {} Exception {}", neName,
					Utils.getStackTrace(exception));
		}
		return list;
	}

	/**
	 * Gets the alarm count circle wise.
	 *
	 * @return the alarm count circle wise
	 * @throws DaoException the dao exception
	 */
	@Override
	public List<TotalSiteLayerWiseWrapper> getAllSiteCountZoneWise() {
		logger.info("Going to get count of Distinct site From NetworkElement");
		List<TotalSiteLayerWiseWrapper> list = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getAllSiteCountZoneWise");
			list = query.getResultList();
		} catch (PersistenceException e) {
			logger.warn("Error while get Distinct site count From NetworkElement, err msg ={}", Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("Something went wrong in Dao Layer ,Err msg ={}", Utils.getStackTrace(e));
			throw new DaoException(e);
		}
		logger.info("Returning from getAllSiteCountZoneWise ,list size = {}", list.size());
		return list;
	}

	/**
	 * Gets the alarm count circle wise.
	 *
	 * @param swLat the sw lat
	 * @param swLng the sw lng
	 * @param neLat the ne lat
	 * @param neLng the ne lng
	 * @return the alarm count circle wise
	 * @throws DaoException the dao exception
	 */
	@Override
	public List<TotalSiteLayerWiseWrapper> getAlarmCountCircleWise(Double swLat, Double swLng, Double neLat,
			Double neLng) {
		logger.info("Going to get count of Distinct site circle wise From NetworkElement");
		List<TotalSiteLayerWiseWrapper> list = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getAllSiteCountCircleWise")
					.setParameter(InfraConstants.SOUTHWEST_LATITUDE_KEY, swLat)
					.setParameter(InfraConstants.SOUTHWEST_LONGITUDE_KEY, swLng)
					.setParameter(InfraConstants.NORTHEAST_LATITUDE_KEY, neLat)
					.setParameter(InfraConstants.NORTHEAST_LONGITUDE_KEY, neLng);
			list = query.getResultList();
		} catch (PersistenceException e) {
			logger.warn("Error while get Distinct site count circle wise From NetworkElement, err msg ={}",
					Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("Something went wrong in Dao Layer,Err msg ={} ", Utils.getStackTrace(e));
			throw new DaoException(e);
		}
		logger.info("Returning from getAlarmCountCircleWise ,list size = {}", list.size());
		return list;
	}

	/**
	 * Gets the alarm count circle wise.
	 *
	 * @param swLat the sw lat
	 * @param swLng the sw lng
	 * @param neLat the ne lat
	 * @param neLng the ne lng
	 * @return the alarm count city wise
	 * @throws DaoException the dao exception
	 */
	@Override
	public List<TotalSiteLayerWiseWrapper> getAlarmCountCityWise(Double swLat, Double swLng, Double neLat,
			Double neLng) {
		logger.info("Going to get count of Distinct site city wise From NetworkElement");
		List<TotalSiteLayerWiseWrapper> list = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getAlarmCountCityWise")
					.setParameter("southWestLat", swLat).setParameter("southWestLong", swLng)
					.setParameter("northEastLat", neLat).setParameter("northEastLong", neLng);
			list = query.getResultList();
		} catch (PersistenceException e) {
			logger.warn("Error while get Distinct site count city wise From NetworkElement, err msg ={}",
					Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("Something went wrong in Dao Layer,Err msg ={}", Utils.getStackTrace(e));
			throw new DaoException(e);
		}
		logger.info("Returning from getAlarmCountCityWise ,list size = {}", list.size());
		return list;
	}

	@Override
	@Deprecated
	public List<NetworkElement> getNEDetailByNEName(List<String> neNameList) {
		logger.info("Going to get NetworkElement by neNameList");
		try {
			Query query = getEntityManager().createNamedQuery("getNEDetailByNEName")
					.setParameter(InfraConstants.NE_NENAME_KEY, neNameList);
			return query.getResultList();
		} catch (NullPointerException nullPointerException) {
			logger.error("NullPointerException caught while getting NetworkElement data  by neNameList Exception : {}",
					Utils.getStackTrace(nullPointerException));
			throw new DaoException("Unable to get NetworkElement data for  neNameList .");
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error(
					"IllegalArgumentException caught while getting NetworkElement data  by neNameList Exception : {}",
					Utils.getStackTrace(illegalArgumentException));
			throw new DaoException(" Unable to get  NetworkElement data for neNameList .");
		} catch (NoResultException noResultException) {
			logger.error("NoResultException caught while getting NetworkElement data  by neNameList Exception : {}",
					Utils.getStackTrace(noResultException));
			throw new DaoException(" Unable to get NetworkElement data for neNameList .");
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while getting NetworkElement data  by neNameList Exception : {}",
					Utils.getStackTrace(persistenceException));
			throw new DaoException("Unable to get NetworkElement  data for neNameList .");
		} catch (Exception exception) {
			logger.error("Exception caught while getting NetworkElement data  by neNameList Exception : {}",
					Utils.getStackTrace(exception));
			throw new DaoException("Unable to get NetworkElement data for  neNameList .");
		}
	}

	/**
	 * Gets the total site count.
	 *
	 * @param swLat the sw lat
	 * @param swLng the sw lng
	 * @param neLat the ne lat
	 * @param neLng the ne lng
	 * @return the total site count
	 * @throws DaoException the dao exception
	 */
	@Override
	public List<TotalSiteLayerWiseWrapper> getTotalSiteCount(Double swLat, Double swLng, Double neLat, Double neLng) {
		logger.info("Going to get count of Distinct site Geograpghy wise From NetworkElement");
		List<TotalSiteLayerWiseWrapper> list = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getTotalSiteCount").setParameter("southWestLat", swLat)
					.setParameter("southWestLong", swLng).setParameter("northEastLat", neLat)
					.setParameter("northEastLong", neLng);
			list = query.getResultList();
		} catch (PersistenceException e) {
			logger.warn("Error while executing getAllSiteCount From NetworkElement, err msg ={}",
					Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("Something went wrong in Dao Layer,Err msg ={}", Utils.getStackTrace(e));
			throw new DaoException(e);
		}
		logger.info("Returning from getTotalSiteCount ,list size = {}", list.size());
		return list;
	}

	@Override
	public List<NetworkElement> getNEDetailByNEIdList(List<String> neIdList) {
		logger.info("Going to get NetworkElement by neId");
		try {
			Query query = getEntityManager().createNamedQuery("getNEDetailByNEIdList")
					.setParameter(InfraConstants.NE_NEID_KEY, neIdList);
			return query.getResultList();
		} catch (NullPointerException nullPointerException) {
			logger.error("NullPointerException caught while getting NetworkElement data  by neId Exception : {}",
					Utils.getStackTrace(nullPointerException));
			throw new DaoException(" Unable  to get  NetworkElement  data for neId.");
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error("IllegalArgumentException caught while getting NetworkElement data  by neId Exception : {}",
					Utils.getStackTrace(illegalArgumentException));
			throw new DaoException("Unable to get NetworkElement data set for neId .");
		} catch (NoResultException noResultException) {
			logger.error("NoResultException caught while getting NetworkElement data  by neId Exception : {}",
					Utils.getStackTrace(noResultException));
			throw new DaoException("Unable to get NetworkElement data for neId .");
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while getting NetworkElement data  by neId Exception : {}",
					Utils.getStackTrace(persistenceException));
			throw new DaoException("Unable to get NetworkElement data for neId .");
		} catch (Exception exception) {
			logger.error("Exception caught while getting NetworkElement data  by neId Exception : {}",
					Utils.getStackTrace(exception));
			throw new DaoException("Unable to get NetworkElement data for neId .");
		}
	}

	@Override
	public List<NetworkElement> getAllNEOfGeographyL3() {
		logger.info("Going to get getAllNEOfGeographyL3");
		try {
			Query query = getEntityManager().createNamedQuery("getAllNEOfGeographyL3");
			return query.getResultList();
		} catch (NullPointerException nullPointerException) {
			logger.error("NullPointerException caught while getting NetworkElement data  by neId Exception : {}",
					Utils.getStackTrace(nullPointerException));
			throw new DaoException("Unable to get NetworkElement data for neId .");
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error("IllegalArgumentException caught while getting NetworkElement data  by neId Exception : {}",
					Utils.getStackTrace(illegalArgumentException));
			throw new DaoException("Unable to get NetworkElement data for neId .");
		} catch (NoResultException noResultException) {
			logger.error("NoResultException caught while getting NetworkElement data  by neId Exception : {}",
					Utils.getStackTrace(noResultException));
			throw new DaoException("Unable to get NetworkElement data for neId .");
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while getting NetworkElement data  by neId Exception : {}",
					Utils.getStackTrace(persistenceException));
			throw new DaoException("Unable to get NetworkElement data for neId .");
		} catch (Exception exception) {
			logger.error("Exception caught while getting NetworkElement data  by neId Exception : {}",
					Utils.getStackTrace(exception));
			throw new DaoException("Unable to get NetworkElement data for neId .");
		}
	}

	@Override
	public Integer getNeIdCountByGeography(List<String> geoNameList, String geoType) {
		logger.info(
				"Inside @Class : " + this.getClass().getName()
						+ " @Method : getNeIdCountByGeography geoNameList_size {} geoType {} ",
				geoNameList.size(), geoType);
		Integer neIdCount = 0;
		try {
			if (geoType.equalsIgnoreCase(InfraConstants.GEOGRAPHYL4) && geoNameList != null) {
				Query query = getEntityManager().createNamedQuery("getNeIdCountByGeography").setParameter("geoNameList",
						geoNameList);
				return ((Long) query.getSingleResult()).intValue();
			}
		} catch (Exception e) {
			logger.error("Exception inside method getNeIdCountByGeography StackTrace {}", Utils.getStackTrace(e));
		}
		return neIdCount;
	}

	@Override
	@Transactional
	@Deprecated
	public List<Vendor> getDistinctVendor() {
		try {
			Query query = getEntityManager().createNamedQuery("getDistinctVendorForSite");
			return query.getResultList();
		} catch (NullPointerException nullPointerException) {
			logger.error("NullPointerException caught while getting distinct Vendor Exception : {}",
					Utils.getStackTrace(nullPointerException));
			throw new DaoException("Unable to get Distinct Vendor ");
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error(
					"IllegalArgumentException caught while getting NetworkElement data  by neIdList Exception : {}",
					Utils.getStackTrace(illegalArgumentException));
			throw new DaoException("Unable to get Distinct Vendor ");
		} catch (NoResultException noResultException) {
			logger.error("NoResultException caught while getting  getting distinct Vendor  Exception : {}",
					Utils.getStackTrace(noResultException));
			throw new DaoException("Unable  to get NetworkElement data for neIdList .");
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while   getting distinct Vendor Exception : {}",
					Utils.getStackTrace(persistenceException));
			throw new DaoException("Unable to get Distinct  Vendor");
		} catch (Exception exception) {
			logger.error("Exception caught while   getting distinct Vendor Exception : {}",
					Utils.getStackTrace(exception));
			throw new DaoException("Unable to get Distinct Vendor ");
		}
	}

	@Override
	@Deprecated
	public List<String> searchNeNameByNeType(String neName, NEType neType) {
		List<String> neNameList = null;
		try {
			logger.info("Inside searchNeIdByNeType neName string {}  neType{}", neName, neType);
			neNameList = getEntityManager().createNamedQuery("getNeNameListByNeNameAndNeType", String.class)
					.setParameter("neName", Symbol.PERCENT + neName.toUpperCase() + Symbol.PERCENT)
					.setParameter(ForesightConstants.NE_TYPE, neType)
					.setParameter(ForesightConstants.NE_STATUS, NEStatus.PLANNED).setMaxResults(15).getResultList();

			logger.info("Inside method@ searchNeNameByNeType neName list size {}", neNameList.size());
		} catch (Exception e) {
			logger.error("Exception Inside searchNeIdByNeType due to {}", Utils.getStackTrace(e));
		}

		return neNameList;
	}

	@Override
	@Deprecated
	public List<NetworkElement> getNEDataByBoundaryMinMax(Double minLat, Double maxLat, Double minLon, Double maxLon,
			List<NEType> neType, List<NEStatus> neStatus) {
		List<NetworkElement> neIdList = null;
		try {
			logger.info(
					"inside getNEDataByBoundaryMinMax minLat {} maxLat {} minLon {} maxLon {} neType {} neStatus {}",
					minLat, maxLat, minLon, maxLon, neType, neStatus);
			neIdList = getEntityManager().createNamedQuery("getNEDataByBoundaryMinMax", NetworkElement.class)
					.setParameter("minLat", minLat).setParameter("maxLat", maxLat).setParameter("minLon", minLon)
					.setParameter("maxLon", maxLon).setParameter("neType", neType).setParameter("neStatus", neStatus)
					.getResultList();
			logger.info("Inside method@ getNEDataByBoundaryMinMax  neIdList {}", neIdList.size());
		} catch (Exception e) {
			logger.error("Exception Inside getNEDataByBoundaryMinMax due to {}", Utils.getStackTrace(e));
		}
		return neIdList;
	}

	private List<Predicate> getPredicateForGeography(String geographyLevel, List<Predicate> predicateList,
			Root networkRoot, List<String> geographyList, CriteriaBuilder builder) {
		try {
			System.out.println("geographyLevel" + geographyLevel);
			logger.info("Going to get predicate for geography {}", geographyList);
			if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL1_TABLE)) {
				predicateList.add(builder.upper(
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.BOUNDARY_NAME))
						.in(geographyList));
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL2_TABLE)) {
				predicateList.add(builder
						.upper(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.BOUNDARY_NAME))
						.in(geographyList));
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL3_TABLE)) {
				predicateList.add(builder.upper(
						networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_GEOGRAPHYL4_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.BOUNDARY_NAME))
						.in(geographyList));
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL4_TABLE)) {
				predicateList.add(builder
						.upper(networkRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.BOUNDARY_NAME))
						.in(geographyList));
			}

		} catch (Exception exception) {
			logger.error("Unable to get predicate for geographyLevel {}  due to exception {} ", geographyLevel,
					Utils.getStackTrace(exception));
		}
		return predicateList;
	}

	@Override
	@Deprecated
	public List<NetworkElement> getNeMacroCellData(String neName, NEType neType) {
		logger.debug("inside getNeMacroCellData nename {} neType {} ", neName, neType);
		List<NetworkElement> neList = new ArrayList<>();
		try {
			neList = getEntityManager().createNamedQuery("getNeMacroCellData", NetworkElement.class)
					.setParameter("neName", neName).setParameter("neType", neType).getResultList();
		} catch (NoResultException noResultException) {
			logger.info("Macro cell is not present for {}", neName);
		}
		return neList;
	}

	@Override
	@Transactional
	@Deprecated
	public List<Vendor> getDistinctVendorByDomain(List<Domain> domain) {
		logger.info("going to get distinct vendor by domain . ");
		List<Vendor> vendorList = new ArrayList<>();
		try {
			logger.info("Going to get Distinct vendor from NetworkElement for domain: {}", domain);
			String vendor = customerInfo.getVendor();
			logger.info("vendor of looged in user: {}", vendor);
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<NetworkElement> criteriaQuery = criteriaBuilder.createQuery(NetworkElement.class);
			Root<NetworkElement> networkElement = criteriaQuery.from(NetworkElement.class);
			List<Predicate> nePredicates = getPredicatesToGetDistinctDomain(criteriaBuilder, networkElement, vendor,
					domain);
			criteriaQuery.select(networkElement.get("vendor")).distinct(true)
					.where(nePredicates.toArray(new Predicate[] {}));
			Query query = getEntityManager().createQuery(criteriaQuery);
			vendorList = query.getResultList();
			logger.info("vendor names for domains {} are: {}", vendor, domain);
		} catch (NullPointerException nullPointerException) {
			logger.error("NullPointerException caught while getting distinct Vendor Exception : {}",
					Utils.getStackTrace(nullPointerException));
			throw new DaoException(" Unable to get Distinct  Vendor");
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error("IllegalArgumentException caught while getting Vendor Exception : {}",
					Utils.getStackTrace(illegalArgumentException));
			throw new DaoException("  Unable to get Distinct Vendor ");
		} catch (NoResultException noResultException) {
			logger.error("NoResultException caught while getting Vendor  Exception : {}",
					Utils.getStackTrace(noResultException));
			throw new DaoException("Unable to get Vendor.");
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while getting distinct Vendor Exception : {}",
					Utils.getStackTrace(persistenceException));
			throw new DaoException("Unable to get Distinct Vendor");
		} catch (Exception exception) {
			logger.error("Exception caught while getting distinct Vendor Exception : {}",
					Utils.getStackTrace(exception));
			throw new DaoException("Unable to get Distinct Vendor");
		}
		return vendorList;
	}

	@Override
	@Transactional
	@Deprecated
	public List<Technology> getTechnologyByVendor(List<Vendor> vendors) {
		logger.info("going to get distinct technology by vendor .");
		try {
			Query query = getEntityManager().createNamedQuery("getTechnologyByVendor");
			if (vendors != null && !vendors.isEmpty()) {
				query.setParameter("vendors", vendors);
			}
			return query.getResultList();
		} catch (NullPointerException nullPointerException) {
			logger.error("NullPointerException caught while getting Technology Exception : {}",
					Utils.getStackTrace(nullPointerException));
			throw new DaoException("Unable to get Distinct Technology. ");
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error("IllegalArgumentException caught while getting Technology Exception : {}",
					Utils.getStackTrace(illegalArgumentException));
			throw new DaoException("Unable to get Distinct  Technology.");
		} catch (NoResultException noResultException) {
			logger.error("NoResultException caught while getting Technology  Exception : {}",
					Utils.getStackTrace(noResultException));
			throw new DaoException("Unable to get Technology.");
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while getting Technology Exception : {}",
					Utils.getStackTrace(persistenceException));
			throw new DaoException("Unable to get Distinct Technology.");
		} catch (Exception exception) {
			logger.error("Exception caught while getting Technology Exception : {}", Utils.getStackTrace(exception));
			throw new DaoException("Unable to get Distinct Technology.");
		}
	}

	@Override
	@Transactional
	@Deprecated
	public List<Vendor> getVendorsByType(List<NEType> neType) {
		logger.info("going to get distinct vendor By Type.");
		try {
			Query query = getEntityManager().createNamedQuery("getVendorsByType");
			if (neType != null && !neType.isEmpty()) {
				query.setParameter("neType", neType);
			}
			return query.getResultList();
		} catch (NullPointerException nullPointerException) {
			logger.error("NullPointerException caught while getting vendor Exception : {}",
					Utils.getStackTrace(nullPointerException));
			throw new DaoException(" Unable to get Distinct vendor.");
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error("IllegalArgumentException caught while getting vendor Exception : {}",
					Utils.getStackTrace(illegalArgumentException));
			throw new DaoException("Unable to get Distinct vendor. ");
		} catch (NoResultException noResultException) {
			logger.error("NoResultException caught while getting vendor  Exception : {}",
					Utils.getStackTrace(noResultException));
			throw new DaoException("Unable to get vendor.");
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while getting vendor Exception : {}",
					Utils.getStackTrace(persistenceException));
			throw new DaoException("Unable to get Distinct vendor.");
		} catch (Exception exception) {
			logger.error("Exception caught while getting vendor Exception : {}", Utils.getStackTrace(exception));
			throw new DaoException("Unable to get Distinct vendor.");
		}
	}

	@Override
	public List<MicroSiteDataWrapper> getNEDetailBySapIdSec(String neName, Integer sectorId, String domain,
			String vendor) {
		logger.info("going to getNEDetail By  neName {}, sector {}", neName, sectorId);
		List<MicroSiteDataWrapper> siteDetails = new ArrayList<>();
		Query query = null;
		try {
			query = getEntityManager().createNamedQuery("getNEDetailBySapIdSec");
			query.setParameter("sectorId", sectorId);
			query.setParameter("neName", neName + "%");
			query.setParameter("domain", Domain.valueOf(domain));
			query.setParameter("vendor", Vendor.valueOf(vendor));
			siteDetails = query.getResultList();
		} catch (Exception e) {
			logger.warn("Error while getNEDetailBySapId,err msg{}", Utils.getStackTrace(e));
		}
		logger.info("Returning siteDetails list size {}", siteDetails.size());
		return siteDetails;
	}

	@Override
	@Deprecated
	public Integer getNeIdByNeName(String neId) {
		logger.info("Going to getNeIdByNeName {}", neId);
		Integer result = null;
		try {
			Query query = getEntityManager().createNamedQuery("getNeIdByNeName");
			query.setParameter("neId", neId);
			result = (Integer) query.getSingleResult();
		} catch (Exception e) {
			logger.warn("Error while get NeId By NeName,err msg {}", e.toString());
		}
		logger.info("Returning neIdPk for neId{}", result);
		return result;
	}

	@Override
	@Deprecated
	public List<NetworkElement> getOnAirNetworkElement() {
		try {
			EntityManager entityManger = getEntityManager();
			Query query = entityManger.createNamedQuery("getOnAirNetworkElement");
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Error in getting on air network element : {}", Utils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	@Deprecated
	public List<NetworkElement> getNEDetailByNEIds(List<String> neIdList) {
		logger.info("Going to get NetworkElement by neNameList");
		try {
			EntityManager entityManger = getEntityManager();
			Query query = entityManger.createNamedQuery("getNEDetailByNEIds").setParameter(InfraConstants.NE_NEID_KEY,
					neIdList);
			return query.getResultList();
		} catch (NullPointerException nullPointerException) {
			logger.error("NullPointerException caught while getting NetworkElement data  by neNameList Exception : {}",
					Utils.getStackTrace(nullPointerException));
			throw new DaoException("Unable to get NetworkElement  data for neNameList .");
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error(
					"IllegalArgumentException caught while getting NetworkElement data  by neNameList Exception : {}",
					Utils.getStackTrace(illegalArgumentException));
			throw new DaoException("Unable to get NetworkElement data  for neNameList .");
		} catch (NoResultException noResultException) {
			logger.error("NoResultException caught while getting NetworkElement data  by neNameList Exception : {}",
					Utils.getStackTrace(noResultException));
			throw new DaoException("Unable to  get NetworkElement data for  neNameList .");
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while getting NetworkElement data  by neNameList Exception : {}",
					Utils.getStackTrace(persistenceException));
			throw new DaoException("Unable to get NetworkElement data for neNameList .");
		} catch (Exception exception) {
			logger.error("Exception caught while getting NetworkElement data  by neNameList Exception : {}",
					Utils.getStackTrace(exception));
			throw new DaoException("Unable to get NetworkElement data for neNameList .");
		}
	}

	@Override
	@Deprecated
	public List<NetworkElementCellDetailWrapper> getAllChildNEDetailByNEId(String neId) throws Exception {
		try {
			Query query = getEntityManager().createNamedQuery("getAllChildNEDetailByNEId");
			query.setParameter("neId", neId + "%");
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception on finding NE Frequency for NE ID: {}, Error: {}", neId, e.getMessage());
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public List<MicroSiteDataWrapper> getNEDetailByNEIdAndDomainVendor(List<String> neIdList, String domain,
			String vendor) {
		logger.info("Going to get NetworkElement by neIdList");
		try {
			Query query = getEntityManager().createNamedQuery("getNEDetailByNEIdAndDomainVendor")
					.setParameter(InfraConstants.NE_NEID_KEY, neIdList)
					.setParameter(InfraConstants.NE_DOMAIN_KEY, Domain.valueOf(domain))
					.setParameter(InfraConstants.NE_VENDOR_KEY, Vendor.valueOf(vendor));
			return query.getResultList();
		} catch (NullPointerException nullPointerException) {
			logger.error("NullPointerException caught while getting NetworkElement data  by neIdList Exception : {}",
					Utils.getStackTrace(nullPointerException));
			throw new DaoException("Unable to get NetworkElement data  for neIdList .");
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error(
					"IllegalArgumentException caught while getting NetworkElement data  by neIdList Exception : {}",
					Utils.getStackTrace(illegalArgumentException));
			throw new DaoException("Unable to  get NetworkElement data for neIdList .");
		} catch (NoResultException noResultException) {
			logger.error("NoResultException caught while getting NetworkElement data  by neIdList Exception : {}",
					Utils.getStackTrace(noResultException));
			throw new DaoException(" Unable to get NetworkElement data for neIdList .");
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while getting NetworkElement data  by neIdList Exception : {}",
					Utils.getStackTrace(persistenceException));
			throw new DaoException("Unable to get NetworkElement data for neIdList .");
		} catch (Exception exception) {
			logger.error("Exception caught while getting NetworkElement data  by neIdList Exception : {}",
					Utils.getStackTrace(exception));
			throw new DaoException("Unable to get NetworkElement data for neIdList .");
		}
	}

	@Override
	@Deprecated
	public List<NetworkElementWrapper> getLatLngByNE() {
		return getEntityManager().createNamedQuery("getLatLngByNE").getResultList();
	}

	@Override
	public List<NetworkElementWrapper> getNEByNodeAndGeoName(List<NEType> neType, String geoName) {
		try {
			return getEntityManager().createNamedQuery("getNEByNodeAndGeoName").setParameter("neType", neType)
					.setParameter("geoName", geoName).getResultList();
		} catch (Exception e) {
			logger.warn("Error while get Network element By NodeAndGeoName,err msg {}", e.toString());
		}
		return new ArrayList<>();
	}

	@Override
	@Deprecated
	public NetworkElement getNEDataByNeNameAndNeType(String nename, NEType netype) {
		NetworkElement networkElement = null;
		try {
			logger.info("inside getNEDataByNEIdAndNeType nename {} neType {} ", nename, netype);
			networkElement = getEntityManager().createNamedQuery("getNEDataByNeNameAndNeType", NetworkElement.class)
					.setParameter("nename", nename).setParameter("netype", netype).getSingleResult();
			logger.info("Inside method@ getNEDataByNEIdAndNeType  networkelement id {}", networkElement.getId());
		} catch (Exception e) {
			logger.error("Exception Inside getNEDataByNEIdAndNeType due to {}", e);
		}
		return networkElement;
	}

	@Override
	public List<NEBandDetail> getNetworkElementDataForNE(String neName, String neType) {
		List<NEBandDetail> neBandDetailDataList = new ArrayList<>();
		logger.info("Going to get NetworkElement data for neName {} ", neName);
		try {
			Query query = getEntityManager().createNamedQuery("getNetworkElementDataForNE");
			query.setParameter(InfraConstants.NENAME, neName.toUpperCase());
			query.setParameter(InfraConstants.NE_TYPE, NEType.valueOf(neType));
			return query.getResultList();
		} catch (IllegalArgumentException | PersistenceException exception) {
			logger.error("NoResultException caught while getting NetworkElement data for neName {} Exception {}",
					neName, Utils.getStackTrace(exception));
		} catch (Exception exception) {
			logger.error("Error in getting NetworkElement for neName {} Exception {}", neName,
					Utils.getStackTrace(exception));
		}
		return neBandDetailDataList;
	}

	@Override
	public SiteCountWrapper getNetworkElementByNeName(String neName) {
		SiteCountWrapper wrapper = null;
		try {
			wrapper = (SiteCountWrapper) getEntityManager().createNamedQuery("getL3AndNeidByNeName")
					.setParameter("neName", neName).getSingleResult();
		} catch (NoResultException e) {
			logger.error("Exception Inside getNetworkElementByNeName due to {}", e);
		}
		return wrapper;
	}

	@Override
	public String getGeographyL3NameByNEName(String neName) {
		String l3Name = null;
		try {
			l3Name = (String) getEntityManager().createNamedQuery("getGeographyL3ByNeName")
					.setParameter("neName", neName).getSingleResult();
		} catch (NoResultException e) {
			logger.error("Exception Inside getGeographyL3NameByNEName due to {}", e);
		}
		return l3Name;
	}

	@Override
	public List<NetworkElementWrapper> getDomainVendorAndL3ByNeId(String neId) {
		try {
			logger.debug("going to get domain, vendor and GeographyL3 for neid: {}", neId);
			return getEntityManager().createNamedQuery("getDomainVendorAndL3ByNeId").setParameter(NE_NEID_KEY, neId)
					.getResultList();
		} catch (NoResultException e) {
			logger.warn("no such domain, vendor and Geography found for neid: {}", neId);
			return null;
		}
	}

	@Override
	public String getTACByNeName(String nename) {
		logger.debug("inside method getTACByNeName nename {}", nename);
		String tac = null;
		try {
			tac = (String) getEntityManager().createNamedQuery("getTACByNeName")
					.setParameter("neName", nename.toUpperCase() + Symbol.PERCENT).setMaxResults(1).getSingleResult();
		} catch (NoResultException noResultException) {
			logger.warn("TAC is not present for {}", nename);
		}
		return tac;
	}

	@Override
	public List<NetworkElementWrapper> getDomainVendorAndL3ByPk(Integer pkId) {
		try {
			logger.debug("going to get domain, vendor and GeographyL3 for neid: {}", pkId);
			return getEntityManager().createNamedQuery("getDomainVendorAndL3ByNEPk").setParameter("pkId", pkId)
					.getResultList();
		} catch (NoResultException e) {
			logger.warn("no such domain, vendor and Geography found for neid: {}", pkId);
			return null;
		}
	}

	@Override
	@Deprecated
	public List<NetworkElementCellDetailWrapper> getAllChildNEDetailByNetworkFK(Integer networkElementIdFk) {
		try {
			Query query = getEntityManager().createNamedQuery("getAllChildNEDetailByNetworkFK");
			query.setParameter("neFkId", networkElementIdFk);
			return query.getResultList();
		} catch (RuntimeException e) {
			logger.error("Exception on getAllChildNEDetailByNetworkFK  NE ID FK: {}, Error: {}", networkElementIdFk,
					e.getMessage());
			throw e;
		}
	}

	@Override
	public List<NetworkElementOnAirJsiWrapper> getOnAirNetworkElementData() {
		try {
			return getEntityManager().createNamedQuery("getOnAirNetworkElementData").getResultList();
		} catch (Exception e) {
			logger.error("Error in getOnAirNetworkElementData for networkElement data : {}", Utils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Transactional(readOnly = true)
	@Override
	public Map<String, Long> getSiteCountL1Wise(String vendor) {
		Map<String, Long> zoneWiseCountMap = new HashMap<>();
		try {
			List<TotalSiteLayerWiseWrapper> resultList = null;
			if (StringUtils.isEmpty(vendor)) {
				resultList = getEntityManager().createNamedQuery("getL4SiteCountDomainWise")
						.setParameter("domain", Domain.RAN).getResultList();
			} else {
				resultList = getEntityManager().createNamedQuery("getL4SiteCountDomainAndVendorWise")
						.setParameter("domain", Domain.RAN).setParameter("vendor", Vendor.valueOf(vendor))
						.getResultList();
			}
			for (TotalSiteLayerWiseWrapper totalSiteLayerWiseWrapper : resultList) {
				zoneWiseCountMap.put(totalSiteLayerWiseWrapper.getGeography(),
						totalSiteLayerWiseWrapper.getTotalSiteCount());
			}
		} catch (Exception e) {
			logger.error("Error in getting SiteCount L1 Wise  and exception is : {}", Utils.getStackTrace(e));
		}
		return zoneWiseCountMap;
	}

	@Override
	public List<NetworkElementWrapper> getNetworkElementData() {
		logger.info("going to get NetworkElement Data ");
		try {
			Query query = getEntityManager().createNamedQuery("getNetworkElementData");
			return query.getResultList();
		} catch (RuntimeException e) {
			logger.error("Exception while get NetworkElement Data");
			throw e;
		}

	}

	@Override
	@Transactional
	public List<NetworkElementCellDetailWrapper> getParentWiseCells() {
		logger.trace("going to get parent wise cell data");
		return getEntityManager().createNamedQuery("getParentWiseCells").getResultList();
	}

	@Override
	public List<NetworkElement> getCellsBySite(String site) {
		logger.info("getting cells for site{}", site);
		return getEntityManager().createNamedQuery("getCellsBySite").setParameter("neName", site).getResultList();
	}

	@Override
	public List<NetworkElementWrapper> getNEByNodeAndOtherGeography(List<NEType> neType, String geoName) {
		try {
			return getEntityManager().createNamedQuery("getNEByNodeAndOtherGeography").setParameter("neType", neType)
					.setParameter("geoName", geoName).getResultList();
		} catch (Exception e) {
			logger.warn("Error while get NEByNodeAndOtherGeography,err msg {}", e.toString());
		}
		return new ArrayList<>();
	}

	@Override
	public NetworkElementWrapper getOtherGeographyNeidByNeNameDomainVendor(String neName, String domain,
			String vendor) {
		NetworkElementWrapper wrapper = new NetworkElementWrapper();
		try {
			wrapper = (NetworkElementWrapper) getEntityManager()
					.createNamedQuery("getOtherGeographyNeidByNeNameDomainVendor").setParameter("neName", neName)
					.setParameter("domain", domain).setParameter("vendor", vendor).getSingleResult();
		} catch (Exception e) {
			logger.warn("Error while get NEByNodeAndOtherGeography,err msg {}", Utils.getStackTrace(e));
		}
		logger.info("could not get other geography for neName:{}, domain:{}, vendor:{}", neName, domain, vendor);
		return wrapper;
	}

	@Override
	public NetworkElementWrapper getGeographyL3NameNeIdByNEName(String neName) {
		NetworkElementWrapper wrapper = new NetworkElementWrapper();
		try {
			wrapper = (NetworkElementWrapper) getEntityManager().createNamedQuery("getGeographyL3NameNeIdByNEName")
					.setParameter("neName", neName).getSingleResult();
		} catch (NoResultException e) {
			logger.error("Exception Inside getGeographyL3NameNeIdByNEName: {}", Utils.getStackTrace(e));
		}
		return wrapper;
	}

	@Override
	public List<Object> getSitesBySalesL4(String othergeographyname) {
		List<Object> list = new ArrayList<>();
		try {
			String nativeQuery = "select nename from NetworkElement ne inner join SalesL4 l4  on ne.salesl4id_fk = l4.salesl4id_pk where ne.networkelementid_fk is null and ne.domain = 'RAN' and UPPER(l4.name)=UPPER(:orgName)";
			Query query = getEntityManager().createNativeQuery(nativeQuery).setParameter("orgName", othergeographyname);
			return query.getResultList();
		} catch (NullPointerException nullPointerException) {
			logger.error("NullPointerException caught while getting data  Exception: {}",
					Utils.getStackTrace(nullPointerException));
			throw new DaoException("Unable to get data for specific parameters.");
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error("IllegalArgumentException caught while getting data  Exception : {}",
					Utils.getStackTrace(illegalArgumentException));
			throw new DaoException("Unable to get NEBandDetail data for specific parameters.");
		} catch (NoResultException noResultException) {
			logger.error("NoResultException caught while getting data Exception : {}",
					Utils.getStackTrace(noResultException));
			throw new DaoException("Unable to get data for specific parameters.");
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while getting data Exception : {}",
					Utils.getStackTrace(persistenceException));
			throw new DaoException("Unable to get  data for specific parameters.");
		} catch (Exception exception) {
			logger.error("Exception caught while getting data  Exception : {}", Utils.getStackTrace(exception));
			throw new DaoException("Unable to get for specific parameters.");
		}

	}

	@Override
	public List<GeographyL1> getDistinctL1ByNeId(List<Integer> neIdList) {
		try {
			return getEntityManager().createNamedQuery("getDistinctL1ByNeId").setParameter("neIdList", neIdList)
					.getResultList();
		} catch (Exception e) {
			logger.warn("Error while get getDistinctL1ByNeId,err msg {}", e.toString());
		}
		return new ArrayList<>();
	}

	@Override
	@Deprecated
	public List<NEType> getNetypeFromNetworkElement(String domain, String vendor) {
		logger.info("Going to get neType by vendor {},domain {}", vendor, domain);
		List<NEType> resultList = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getNetypeFromNetworkElement");
			enableFilters(domain, vendor, null, null);
			resultList = query.getResultList();
		} catch (IllegalStateException | PersistenceException e) {
			logger.warn("Exception while get neType From NetworkElement,err msg {}", e.getMessage());
		}
		return resultList;
	}

	@Override
	public List<NetworkElementWrapper> getDomainVendorAndL3ByNeIdList(List<String> neIdList) {
		try {
			logger.debug("going to get domain, vendor and GeographyL3 for neIdList: {}", neIdList);
			return getEntityManager().createNamedQuery("getDomainVendorAndL3ByNeIdList")
					.setParameter(NE_NEID_KEY, neIdList).getResultList();
		} catch (NoResultException e) {
			logger.warn("error while getting domain vendor and geographyL3 for neidlist {}", neIdList);
			return null;
		}
	}

	private void networkElementGeoIdInFilter(Set<String> nameList, String type) {
		if (nameList != null) {
			logger.info("inside networkElementGeoIdInFilter type:{}", type);
			Session s = (Session) getEntityManager().getDelegate();
			if ("L0".equalsIgnoreCase(type)) {
				s.enableFilter("networkElementGeoL1NameInFilter").setParameterList("geographyL1Name", nameList);
			} else if (ForesightConstants.L1.equalsIgnoreCase(type)) {
				s.enableFilter("networkElementGeoL2NameInFilter").setParameterList("geographyL2Name", nameList);
			} else if (ForesightConstants.L2.equalsIgnoreCase(type)) {
				s.enableFilter("networkElementGeoL3NameInFilter").setParameterList("geographyL3Name", nameList);
			} else if (ForesightConstants.L3.equalsIgnoreCase(type)) {
				s.enableFilter("networkElementGeoL4NameInFilter").setParameterList("geographyL4Name", nameList);
			} else if (ForesightConstants.L4.equalsIgnoreCase(type)) {
				s.enableFilter("networkElementNameInFilter").setParameterList("neName", nameList);
			} else if (ForesightConstants.MACRO.equalsIgnoreCase(type)) {
				s.enableFilter("networkElementNeNameFilter").setParameterList("neName", nameList);
			}
		}
	}

	@Override
	@Deprecated
	public List<NetworkElement> getNEListByTypeAndGeoIds(String type, Set<String> nameList, String entityLevel,
			String domain, String vendor) {
		try {
			Query query = getEntityManager().createNamedQuery("getNEForIsDeletedFalse");
			networkElementGeoIdInFilter(nameList, type);
			networkElementDomainFilter(domain);
			networkElementVendorFilter(vendor);
			networkElementNeTypeFilter(entityLevel);
			return query.getResultList();
		} catch (Exception e) {
			logger.error(
					"Exception @Method getNEListByTypeAndGeoIds for type:{}, domain:{}, vendor:{}, entityLevel:{}, nameList:{} "
							+ "due to {}",
					type, domain, vendor, entityLevel, nameList, ExceptionUtils.getStackTrace(e));
			return Collections.emptyList();
		}
	}

	@Override
	public List<Map<String, String>> getVendorAndL4WiseNename(Integer L4Id, String vendor) {
		List<Map<String, String>> neNames = new ArrayList<>();
		try {
			logger.debug("going to get NeName list by vendor and GeographyL4:");
			Query query = getEntityManager().createNamedQuery("getVendorAndL4WiseNename");
			query.setParameter("L4Id", L4Id);
			query.setParameter("vendor", Vendor.valueOf(vendor));
			return query.getResultList();
		} catch (NoResultException e) {
			logger.warn("error while getting NeName List for vendor and L4Id {}", Utils.getStackTrace(e));
			return neNames;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<NetworkElement> getNetworkElementsForSiteAndCellLevelDetail(List<NEType> neTypeList,
			List<String> neNameList, List<String> neFrequencyList, List<NEStatus> neStatusList, List<Vendor> vendorList,
			List<Technology> technologyList, List<Domain> domainList, Map<String, Double> viewportMap,
			Map<String, List<Integer>> geographyNames, List<String> neIdList) {
		logger.info(
				"Going to get NetworkElement Site and Cell Level data for neType : {} , neFrequency : {} , neStatus: {} , vendor : {}",
				neTypeList, neFrequencyList, neStatusList, vendorList);
		try {
			neNameList = InfraUtils.convertListElements(neNameList, InfraConstants.LIST_TOUPPERCASE);
			neFrequencyList = InfraUtils.convertListElements(neFrequencyList, InfraConstants.LIST_TOUPPERCASE);
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<NetworkElement> criteriaQuery = criteriaBuilder.createQuery(NetworkElement.class);
			Root<NetworkElement> networkElement = criteriaQuery.from(NetworkElement.class);
			List<Predicate> nePredicates = InfraUtils.getPredicatesForCriteriaBuilderForSiteAndCell(criteriaBuilder,
					networkElement, neTypeList, neNameList, neFrequencyList, neStatusList, vendorList, technologyList,
					domainList, geographyNames, neIdList);
			List<Predicate> viewportPredicates = InfraUtils.getPredicatesForViewPortsForNE(criteriaBuilder,
					networkElement, viewportMap);
			List<Predicate> finalPredicates = new ArrayList<>();
			finalPredicates.addAll(nePredicates);
			finalPredicates.addAll(viewportPredicates);
			criteriaQuery.select(networkElement).where(finalPredicates.toArray(new Predicate[] {}));
			Query query = getEntityManager().createQuery(criteriaQuery);
			return query.getResultList();
		} catch (NullPointerException | IllegalArgumentException | PersistenceException exception) {
			logger.error("Exception caught while getting Site and cell level data from NetworkElement Exception : {}",
					Utils.getStackTrace(exception));
			throw new DaoException("Unable to get NetworkElement Site and Cell Level data for specific parameters.");
		}
	}

	/** Method for Customer Care Module */
	@Override
	public List<Object[]> getSiteDetailForCustomerCare(List<Domain> domainList, NEStatus neStatus,
			List<NEType> neTypeList) {
		logger.info("Going to get Network Element data by domain : {} , nestatus : {} , netype : {}", domainList,
				neStatus, neTypeList);
		List<Object[]> neList = new ArrayList<>();
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
			Root<NetworkElement> root = criteriaQuery.from(NetworkElement.class);
			criteriaQuery.multiselect(root.get(ForesightConstants.ID), root.get(InfraConstants.NE_NENAME_KEY));
			List<Predicate> predicateList = new ArrayList<>();
			predicateList.add(criteriaBuilder.isNotNull(root.get(InfraConstants.NE_NENAME_KEY)));
			predicateList.add(criteriaBuilder.equal(root.get(InfraConstants.NE_ISDELETED_KEY), false));

			if (domainList != null && !domainList.isEmpty()) {
				addINQueryForNE(InfraConstants.NE_DOMAIN_KEY, domainList, predicateList, root);
			}
			if (neStatus != null) {
				predicateList.add(criteriaBuilder.equal(root.get(InfraConstants.NE_NESTATUS_KEY), neStatus));
			}
			if (neTypeList != null && !neTypeList.isEmpty()) {
				addINQueryForNE(InfraConstants.NE_NETYPE_KEY, neTypeList, predicateList, root);
			}
			criteriaQuery.where(predicateList.toArray(new Predicate[] {}));
			neList = getEntityManager().createQuery(criteriaQuery).getResultList();
			logger.info("NE parent site List size : {}", neList != null ? neList.size() : 0);
		} catch (NoResultException noResultException) {
			logger.error("Error in getting NE list by geography l4 : {}", noResultException.getMessage());
		} catch (Exception e) {
			logger.error("Error in getting NE list by geo L4 Exception : {}", Utils.getStackTrace(e));
		}
		return neList;
	}

	private <T> void addINQueryForNE(String columnName, List<T> valueList, List<Predicate> predicateList,
			Root<NetworkElement> root) {
		Expression<String> neTypeExpression = root.get(columnName);
		predicateList.add(neTypeExpression.in(valueList));
	}

	@Override
	public List<NetworkElement> getWifiAPDetailByFloorId(List<Integer> floorIds) {
		Query query = null;
		try {
			query = getEntityManager().createNamedQuery("getAPNDetailByFloorId").setParameter("floorId", floorIds);
			return query.getResultList();
		} catch (Exception e) {
			logger.error(InBuildingConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
		}

		return null;
	}

	@Override
	@Deprecated
	public List<NetworkElement> getNetworkElementByMacAddress(String macAddress) {
		Query query = null;
		try {
			query = getEntityManager().createNamedQuery("getNEByMacaddress").setParameter("macaddress", macAddress);
			return query.getResultList();
		} catch (Exception e) {
			logger.error(InBuildingConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public List<String> getDistinctNEFrequencyByNEName(String neName) {
		Query query = null;
		try {
			query = getEntityManager().createNamedQuery("getDistinctNEFrequencyByNEName").setParameter("neName",
					neName);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception inside getDistinctNEFrequencyByNEName {}", ExceptionUtils.getStackTrace(e));
		}
		return Collections.emptyList();
	}

	@Override
	public List<Tuple> searchNEDetail(Map<String, List<Map>> filters, Map<String, List<String>> projection,
			List groupByList, List orderByList, boolean isDistinct, Map viewportMap) {
		try {
			logger.info("Going to search NE Details for input parameters.");
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Tuple> criteriaTupleQuery = criteriaBuilder.createTupleQuery();
			List<Selection<?>> selection = new ArrayList<Selection<?>>();
			List<Predicate> predicateList = new ArrayList<Predicate>();
			List<String> distinctEntity = new ArrayList<String>();
			Map criteriaMap = new HashMap<>();
			distinctEntity = InfraUtils.getDistinctEntity(distinctEntity, filters, projection);
			Map<String, Root> rootMap = CriteriaUtils.getRootMap(distinctEntity, criteriaTupleQuery);
			criteriaMap = CriteriaUtils.getNEForCellDetail(criteriaMap, criteriaBuilder, predicateList, filters,
					selection, distinctEntity, criteriaTupleQuery, projection, viewportMap, rootMap, null);
			predicateList = (List<Predicate>) criteriaMap.get(InfraConstants.PREDICATE);
			selection = (List<Selection<?>>) criteriaMap.get(InfraConstants.SELECTION);
			Expression<?>[] expressions = InfraUtils
					.getExpressionObject(rootMap.get(InfraConstants.NETWORKELEMENT_TABLE), groupByList);
			InfraUtils.getCriteriaQueryForGroupBy(criteriaTupleQuery, expressions);
			Order[] orders = InfraUtils.getOrderObject(rootMap.get(InfraConstants.NETWORKELEMENT_TABLE), orderByList,
					criteriaBuilder);
			InfraUtils.getCriteriaQueryForOrderBy(criteriaTupleQuery, orders);
			predicateList = CriteriaUtils.getParametersForNewtorkElements(null, criteriaBuilder, predicateList,
					distinctEntity, filters, criteriaTupleQuery, rootMap, null);
			filters.remove(InfraConstants.PARENT_NETWORK_ELEMENT);
			filters.remove(InfraConstants.NETWORKELEMENT_TABLE);
			projection.remove(InfraConstants.NETWORKELEMENT_TABLE);
			predicateList = CriteriaUtils.getCriteriaPredicates(criteriaBuilder, rootMap, predicateList, filters);
			selection = CriteriaUtils.getSelectionForCriteriaBuilder(rootMap, projection, selection);
			if (isDistinct) {
				criteriaTupleQuery.select(criteriaBuilder.tuple(selection.toArray(new Selection<?>[] {})))
						.distinct(isDistinct);
			} else {
				criteriaTupleQuery.select(criteriaBuilder.tuple(selection.toArray(new Selection<?>[] {})));
			}
			Query query = getEntityManager()
					.createQuery(criteriaTupleQuery.where(predicateList.toArray(new Predicate[] {})));
			return query.getResultList();
		} catch (NullPointerException nullPointerException) {
			logger.error("NullPointerException caught while searching NEDetails Exception : {}",
					Utils.getStackTrace(nullPointerException));
			throw new DaoException("Unable to  search NEDetails.");
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error("IllegalArgumentException caught while searching NEDetails Exception : {}",
					Utils.getStackTrace(illegalArgumentException));
			throw new DaoException(" Unable to search NEDetails.");
		} catch (NoResultException noResultException) {
			logger.error("NoResultException caught while searching NEDetails Exception : {}",
					Utils.getStackTrace(noResultException));
			throw new DaoException("Unable  to search NEDetails.");
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while searching NEDetails Exception : {}",
					Utils.getStackTrace(persistenceException));
			throw new DaoException("Unable to  search NEDetails. ");
		} catch (Exception exception) {
			logger.error("Exception caught while searching NEDetails Exception : {}", Utils.getStackTrace(exception));
			throw new DaoException("Unable to search NEDetails. ");
		}
	}

	@Override
	public List<Tuple> searchNEDetailForSiteAndCellLevel(String tableName, List columnsFilterDetails,
			List projectionColumns, List groupByColumns, List orderByColumns, boolean isDistinctClause,
			Map geographyDetails, Map viewportDetails) {
		try {
			logger.info("Going to search NEDetails");
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Tuple> criteriaTupleQuery = criteriaBuilder.createTupleQuery();
			Class targetClass = Class.forName("com.inn.foresight.core.infra.model." + tableName);
			Root<NetworkElement> parentRoot = criteriaTupleQuery.from(targetClass);
			Root<NetworkElement> childRoot = criteriaTupleQuery.from(targetClass);
			List<Predicate> predicates = InfraUtils.getPredicatesForNEDetails(criteriaBuilder, childRoot,
					columnsFilterDetails, geographyDetails, viewportDetails);
			predicates.add(criteriaBuilder.equal(parentRoot.get(InfraConstants.NE_NETWORKELEMENTID_KEY),
					childRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)));
			Selection<?>[] selection = InfraUtils.getSelectionObject(parentRoot, childRoot, projectionColumns,
					orderByColumns, isDistinctClause, groupByColumns);
			InfraUtils.getCriteriaQueryForSelection(criteriaBuilder, criteriaTupleQuery, parentRoot, selection,
					predicates, isDistinctClause);
			Expression<?>[] expression = InfraUtils.getExpressionObject(parentRoot, groupByColumns);
			InfraUtils.getCriteriaQueryForGroupBy(criteriaTupleQuery, expression);
			Order order[] = InfraUtils.getOrderObject(parentRoot, orderByColumns, criteriaBuilder);
			InfraUtils.getCriteriaQueryForOrderBy(criteriaTupleQuery, order);
			return getEntityManager().createQuery(criteriaTupleQuery).getResultList();
		} catch (NullPointerException nullPointerException) {
			logger.error("NullPointerException caught while searching NEDetails Exception : {}",
					Utils.getStackTrace(nullPointerException));
			throw new DaoException(" Unable to search NEDetails.");
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error("IllegalArgumentException caught while searching NEDetails Exception : {}",
					Utils.getStackTrace(illegalArgumentException));
			throw new DaoException("Unable  to search  NEDetails.");
		} catch (NoResultException noResultException) {
			logger.error("NoResultException caught while searching NEDetails Exception : {}",
					Utils.getStackTrace(noResultException));
			throw new DaoException("Unable to  search NEDetails.");
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while searching NEDetails Exception : {}",
					Utils.getStackTrace(persistenceException));
			throw new DaoException("Unable to search NEDetails.");
		} catch (Exception exception) {
			logger.error("Exception caught while searching NEDetails Exception : {}", Utils.getStackTrace(exception));
			throw new DaoException("Unable to search NEDetails.");
		}
	}

	private void intializeMap() {
		neIdWithSiteName = new HashMap<>();
		logger.debug("going to get Site Friendly Name along with NEId");
		List<NeDataWrapper> getNEIdAndFriendlyName = getEntityManager().createNamedQuery("getNEIdAndFriendlyName")
				.getResultList();
		for (NeDataWrapper netElement : getNEIdAndFriendlyName) {
			neIdWithSiteName.put(netElement.getpNeId(), netElement.getSiteName());
		}
		logger.debug("NE data map size is : {}", neIdWithSiteName.size());
	}

	@Override
	public Map<String, String> getSiteWithFriendlyName() {
		if (MapUtils.isEmpty(neIdWithSiteName)) {
			logger.debug("going to intialize NEId with site Name map");
			intializeMap();
		}
		return neIdWithSiteName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getSiteDetailReportData(List<String> neFrequencyList,
			Map<String, List<String>> geographyNames, List<NEStatus> neStatusList, List<NEType> neTypeList,
			Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat) {
		List<Object[]> siteDetailDataList = new ArrayList<>();
		List<String> neStausListForReport = new ArrayList<>();
		List<String> neTypeListForReport = new ArrayList<>();
		for (NEStatus neStatus : neStatusList) {
			neStausListForReport.add(neStatus.toString());
		}
		for (NEType neType : neTypeList) {
			neTypeListForReport.add(neType.toString());
		}
		logger.info("Going to get data for site detail report.");
		try {
			String dynamicQuery = "select "
					+ "ne.nename as SiteId,case when nes.oldvendorid='' then '-' else nes.oldvendorid end as oldvendorid,case when nes.sfid='' then '-' else nes.sfid end as sfid,'-' as propertyId,"
					+ "case ne.netype  when 'ODSC_SITE' then 'SMALL CELL' when 'PICO_SITE' then 'PICO' when 'GALLERY_SITE' then 'GALLERY' when 'Shooter_Site' then 'SHOOTER' when 'IBS_Site' then 'IBS'  else ne.netype end as netype,"
					+ "ne.nestatus as nestatus1,ne.vendor,ne.latitude,ne.longitude,'-' as sitelocation,'-' as siteonairdate,";

			if (neFrequencyList != null && !neFrequencyList.isEmpty()) {
				for (String neFrequency : neFrequencyList) {
					dynamicQuery += " max(case when nbd.nefrequency='" + neFrequency + "' then 'Yes' else 'No' end) '"
							+ neFrequency + "BandAvailability'," + "max(case when nbd.nefrequency='" + neFrequency
							+ "' then nbd.bandstatus else null end)  '" + neFrequency + "Site_Status',"
							+ "max(case when nbd.nefrequency='" + neFrequency
							+ "' then date_format(nbd.bandonairdate,'%d-%m-%Y') else null end) '" + neFrequency
							+ "on_air_date'," + "max(case when nbd.nefrequency='" + neFrequency
							+ "' then nbd.currentstage else null end) '" + neFrequency + "LifecycleStatus',"
							+ "max(case when nbd.nefrequency='" + neFrequency
							+ "' then nbd.backhaulinfo else null end ) '" + neFrequency + "backhaulmedia',"
							+ "max(case when nbd.nefrequency='" + neFrequency
							+ "' then nbd.numberofsector else null end) '" + neFrequency + "no.of sector',";
				}
			}
			dynamicQuery += "gl1.displayName gl1,gl2.displayName gl2,gl3.displayName gl3,gl4.displayName gl4,case ned.morphology when 'DU' then 'DENSE URBAN'  when 'UR' then 'URBAN' when 'IN' then 'INDUSTRIAL' when 'RU' then 'RURAL' when 'WB' then 'WATER BODY' when 'RO' then 'Road' when '' then '-' else ned.morphology END as morphology,"
					+ "(case when ne.friendlyname='' then '-' else ne.friendlyname end) as siteName,nel.address,'-' as nominalLocation,ned.category as siteclass,ned.contactname,ned.contactnumber,ems.emsname as hostname , ems.ip,"
					+ "case when group_concat(distinct rs.emsstatus) like '%LIVE%' then 'LIVE' else rs.emsstatus end as emsstatus,date_format(min(rs.emslivedate),'%d-%m-%Y') as emslivedate,case when ne.nestatus='NONRADIATING' then 'Yes'  else 'No' end  'Non_Radiating_Status',"
					+ "case when ne.nestatus ='NONRADIATING' then date_format(min(rs.nonradiatingdate),'%d-%m-%Y') else '-' end  as nonradiatingdate,case when ne.nestatus='DECOMMISSIONED' then 'Yes'  else 'No' end as decomissioningstatus,case when ne.nestatus ='DECOMMISSIONED' then date_format(min(rs.decommissioningdate),'%d-%m-%Y') else '-' end  as decommissioningdate ,"
					+ " date_format(min(rs.cmavailabilitydate),'%d-%m-%Y') as cmavailabilitydate,date_format(min(rs.pmavailabilitydate),'%d-%m-%Y') as pmavailabilitydate,'-' as fmavailabilitydate,ne.nesource,date_format(ne.creationtime,'%d-%m-%Y') as creationtime, "
					+ "nesite.towersitetype as towerSiteType,nesite.provincecode as provinceCode,nesite.city as lteCity,"
					+ " nesite.siterolloutscenario as siteRolloutScenario,nesite.txclass as txSiteClass,nesite.island as island ,nesite.province as province,nesite.txvendor as txVendor,"
					+ " nesite.phase as phase,nesite.projectmilestone as projectMilestone  "
					+ "from NEBandDetail nbd right join NetworkElement ne on nbd.networkelementid_fk=ne.networkelementid_pk left join NEDetail ned on ned.networkelementid_fk=ne.networkelementid_pk left join NESiteDetail nes on nes.networkelementid_fk=ne.networkelementid_pk"
					+ " left join RANDetail msd on msd.nebanddetailid_fk=nbd.nebanddetailid_pk left join RANCellStats rs on rs.randetailid_fk=msd.randetailid_pk"
					+ " left join EMSServer ems on ne.emsserverid_fk=ems.emsserverid_pk left join NELocation nel on ne.nelocationid_fk=nel.nelocationid_pk left join GeographyL4 gl4 on  ne.geographyl4id_fk=gl4.geographyl4id_pk left join GeographyL3 gl3 on gl4.geographyl3id_fk=gl3.geographyl3id_pk "
					+ " left join GeographyL2 gl2 on gl3.geographyl2id_fk=gl2.geographyl2id_pk left join GeographyL1 gl1 on gl2.geographyl1id_fk=gl1.geographyl1id_pk "
					+ "  left join NESiteDetail nesite on nesite.networkelementid_fk=ne.networkelementid_pk"
					+ " left join NECellDetail necell on necell.randetailid_fk=msd.randetailid_pk" + "  where  ";
			if (geographyNames != null && !geographyNames.isEmpty()) {
				if (geographyNames.get(InfraConstants.GEOGRAPHYL4_TABLE) != null) {
					dynamicQuery += " gl4.name IN (:geographyL4) and ";
				}
				if (geographyNames.get(InfraConstants.GEOGRAPHYL3_TABLE) != null) {
					dynamicQuery += " gl3.name IN (:geographyL3) and ";
				}
				if (geographyNames.get(InfraConstants.GEOGRAPHYL2_TABLE) != null) {
					dynamicQuery += " gl2.name IN (:geographyL2) and ";
				}
				if (geographyNames.get(InfraConstants.GEOGRAPHYL1_TABLE) != null) {
					dynamicQuery += " gl1.name IN (:geographyL1) and ";
				}
			}

			if (neStausListForReport != null && !neStausListForReport.isEmpty()) {
				dynamicQuery += " ne.nestatus in (:neStausListForReport) and ";
			}
			if (neFrequencyList != null && !neFrequencyList.isEmpty()) {
				dynamicQuery += " nbd.nefrequency IN (:neFrequencyList) and ";
			}
			if (neTypeListForReport != null && !neTypeListForReport.isEmpty()) {
				dynamicQuery += " ne.netype IN (:neTypeListForReport) and ";
			}
			if (southWestLat != null) {
				dynamicQuery += " ne.latitude > " + southWestLat + " and ";
			}
			if (northEastLat != null) {
				dynamicQuery += " ne.latitude < " + northEastLat + " and ";
			}
			if (southWestLong != null) {
				dynamicQuery += " ne.longitude > " + southWestLong + " and ";
			}
			if (northEastLong != null) {
				dynamicQuery += " ne.longitude < " + northEastLong + " and ";
			}
			dynamicQuery += "   ne.deleted=0 and ne.latitude is not null and ne.geographyl4id_fk is not null and  ne.longitude is not null group by ne.nename,ne.nestatus,ne.netype";
			System.out.println("dynamicQuery:  " + dynamicQuery);
			Query query = getEntityManager().createNativeQuery(dynamicQuery);
			if (neStausListForReport != null && !neStausListForReport.isEmpty())
				query.setParameter("neStausListForReport", neStausListForReport);
			if (neFrequencyList != null && !neFrequencyList.isEmpty())
				query.setParameter("neFrequencyList", neFrequencyList);
			if (neTypeListForReport != null && !neTypeListForReport.isEmpty())
				query.setParameter("neTypeListForReport", neTypeListForReport);
			if (geographyNames != null && !geographyNames.isEmpty()) {
				if (geographyNames.get(InfraConstants.GEOGRAPHYL4_TABLE) != null)
					query.setParameter("geographyL4", geographyNames.get(InfraConstants.GEOGRAPHYL4_TABLE));
				if (geographyNames.get(InfraConstants.GEOGRAPHYL3_TABLE) != null)
					query.setParameter("geographyL3", geographyNames.get(InfraConstants.GEOGRAPHYL3_TABLE));
				if (geographyNames.get(InfraConstants.GEOGRAPHYL2_TABLE) != null)
					query.setParameter("geographyL2", geographyNames.get(InfraConstants.GEOGRAPHYL2_TABLE));
				if (geographyNames.get(InfraConstants.GEOGRAPHYL1_TABLE) != null)
					query.setParameter("geographyL1", geographyNames.get(InfraConstants.GEOGRAPHYL1_TABLE));
			}
			siteDetailDataList = query.getResultList();
			logger.info("list: {}", siteDetailDataList.size());
			return siteDetailDataList;
		} catch (IllegalArgumentException | PersistenceException exception) {
			logger.error("NoResultException caught while getting NetworkElement data Exception {}",
					Utils.getStackTrace(exception));
		} catch (Exception exception) {
			logger.error("Error in getting NetworkElement Exception {}", Utils.getStackTrace(exception));
		}
		return siteDetailDataList;
	}

	@Override
	public List<String> getDistinctMorphology() {
		List<String> morphologyList = new ArrayList<>();
		try {
			logger.info("Going to get distinct morphology");
			String dynamicQuery = "select distinct n.morphology from NEDetail n where n.morphology is not null  and n.morphology != '' ";
			Query query = getEntityManager().createNamedQuery(dynamicQuery);
			morphologyList = query.getResultList();
			logger.info(" Distinct Morphology For Sites: {}", morphologyList);
			if (morphologyList != null)
				return morphologyList;
		} catch (PersistenceException | NullPointerException exception) {
			logger.error("Exception caught while getting distinct Morphology count {}", Utils.getStackTrace(exception));
			throw new DaoException("Unable to get Distinct Morphology ");
		} catch (Exception exception) {
			logger.error("Exception caught while getting distinct Morphology count  {}",
					Utils.getStackTrace(exception));
			throw new DaoException(" Unable to get Distinct Morphology");
		}
		return morphologyList;

	}

	@Override
	public List<Integer> getEnodeBIdFromFrequencyAndParentNeName(List<String> neNameList, String frequency) {
		List<Integer> enbIds = new ArrayList<>();
		try {
			logger.info("Going to getEnodeBIdFromFrequencyAndParentNeName");
			Query query = getEntityManager().createNamedQuery("getEnodeBIdFromFrequencyAndParentNeName")
					.setParameter("frequency", frequency).setParameter("neList", neNameList);
			enbIds = query.getResultList();
		} catch (PersistenceException | NullPointerException exception) {
			logger.error("Exception caught while gettingEnodeBIdFromFrequencyAndParentNeName {}",
					Utils.getStackTrace(exception));
		} catch (Exception exception) {
			logger.error("Exception caught while getting EnodeBIdFromFrequencyAndParentNeName  {}",
					Utils.getStackTrace(exception));
		}
		return enbIds;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.inn.foresight.core.infra.dao.INetworkElementDao#checkForEnodeBAndRRH(
	 * java .lang.String)
	 */
	@Override
	public NetworkElement getNetworkElementByNEId(String neId) {
		NetworkElement neElement = null;
		try {
			logger.info("id :{}", neId);
			TypedQuery<NetworkElement> query = getEntityManager().createNamedQuery("searchByNEId",
					NetworkElement.class);
			query.setParameter("neId", neId);
			neElement = query.getSingleResult();
		} catch (NoResultException ne) {
			logger.error("no Entity found :{}", ne.getMessage());

		} catch (Exception e) {
			logger.error("error while getting count from DB :{}", ExceptionUtils.getStackTrace(e));
			throw new DaoException("Exception Occured while updating Record");

		}
		return neElement;

	}

	@Override
	public NetworkElement getNetworkElementByHostName(String hostname) {
		NetworkElement neElement = null;
		try {
			logger.info("id :{}", hostname);
			TypedQuery<NetworkElement> query = getEntityManager().createNamedQuery("searchByHostName",
					NetworkElement.class);
			query.setParameter("hostname", hostname);
			neElement = query.getSingleResult();
		} catch (NoResultException ne) {
			logger.error("no Entity found :{}", ne.getMessage());

		} catch (Exception e) {
			logger.error("error while getting count from DB :{}", ExceptionUtils.getStackTrace(e));
			throw new DaoException("Exception Occured while updating Record");

		}
		return neElement;

	}

	@Override
	public NetworkElement getNetworkElementForRrhByEnodeBPk(Integer enodeBPk) {
		NetworkElement neElement = null;
		try {
			logger.info("enodeBPk :{}", enodeBPk);
			TypedQuery<NetworkElement> query = getEntityManager().createNamedQuery("searchByEnodeBPk",
					NetworkElement.class);
			query.setParameter("enodeBPk", enodeBPk);
			List<NetworkElement> list = query.getResultList();
			if (list != null && !list.isEmpty())
				return list.get(0);
		} catch (NoResultException ne) {
			logger.error("no Entity found :{}", ne.getMessage());

		} catch (Exception e) {
			logger.error("error while getting count from DB :{}", ExceptionUtils.getStackTrace(e));
			throw new DaoException("Exception Occured while updating Record");

		}
		return neElement;

	}

	@Override
	public List<Object[]> getNetworkElementByNeType(String domain, String vendor, String neType) {

		List<Object[]> neElement = null;
		try {
			logger.info("neType :{}", neType);
			Query query = getEntityManager().createNamedQuery("getNetworkElementByNEType");
			query.setParameter("domain", Domain.valueOf(domain));
			query.setParameter("vendor", Vendor.valueOf(vendor));
			query.setParameter("neType", NEType.valueOf(neType));
			neElement = query.getResultList();
		} catch (NoResultException ne) {
			logger.error("no Entity found :{}", ne.getMessage());

		} catch (Exception e) {
			logger.error("error while getting count from DB :{}", ExceptionUtils.getStackTrace(e));
			throw new DaoException("Exception Occured while updating Record");

		}
		return neElement;

	}

	@Override
	public List<NetworkElementWrapper> getNetworkElementByGeography(String geoLevel, String domain,
			Integer geographyPK) {
		List<NetworkElementWrapper> list = new ArrayList<>();
		Query query = null;
		try {
			if (StringUtils.equalsIgnoreCase(geoLevel, ForesightConstants.GEOGRAPHY_L1)) {
				query = getEntityManager().createNamedQuery("getNetworkElementByGeographyL1");
			} else if (StringUtils.equalsIgnoreCase(geoLevel, ForesightConstants.GEOGRAPHY_L2)) {
				query = getEntityManager().createNamedQuery("getNetworkElementByGeographyL2").setParameter("id",
						geographyPK);
			} else if (StringUtils.equalsIgnoreCase(geoLevel, ForesightConstants.GEOGRAPHY_L3)) {
				query = getEntityManager().createNamedQuery("getNetworkElementByGeographyL3").setParameter("id",
						geographyPK);
			} else if (StringUtils.equalsIgnoreCase(geoLevel, ForesightConstants.GEOGRAPHY_L4)) {
				query = getEntityManager().createNamedQuery("getNetworkElementByGeographyL4").setParameter("id",
						geographyPK);
			} else if (StringUtils.equalsIgnoreCase(geoLevel, ForesightConstants.MACRO)) {
				query = getEntityManager().createNamedQuery("getNetworkElementByMACRO").setParameter("id", geographyPK);
			} else if (StringUtils.equalsIgnoreCase(geoLevel, ForesightConstants.MACRO_CELL)) {
				query = getEntityManager().createNamedQuery("getNetworkElementByMacroCell").setParameter("neidpk",
						geographyPK);
			}
			if (query != null)
				list = query.setParameter("domain", Domain.valueOf(domain.toUpperCase())).getResultList();
		} catch (Exception e) {
			logger.error("going to get NetworkElementByGeography :{}", e);
		}
		return list;

	}

	@Override
	public NetworkElement getNetworkElementByNELocation(Integer nelocationid_fk) {
		NetworkElement neElement = null;
		try {
			logger.info("nelocationid_fk :{}", nelocationid_fk);
			TypedQuery<NetworkElement> query = getEntityManager().createNamedQuery("getNetworkElementByNELocation",
					NetworkElement.class);
			query.setParameter("nelocationid_fk", nelocationid_fk);
			neElement = query.getResultList().get(ForesightConstants.ZERO);
		} catch (NoResultException nre) {
			logger.error("NO entity Found :{}", ExceptionUtils.getStackTrace(nre));
		} catch (Exception e) {
			logger.error("error while getting getNetworkElementByNELocation :{}",
					org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
			throw new DaoException("Exception Occured while updating Record");

		}
		return neElement;

	}

	@Override
	public List<Object> executeQuery(String userQuery) {
		logger.info("Going to execute user query, {}", userQuery);
		try {
			return getEntityManager().createQuery(userQuery, Object.class).getResultList();
		} catch (Exception exception) {
			logger.error("Inside executeQuery, {}", ExceptionUtils.getStackTrace(exception));
		}
		return new ArrayList<>();
	}

	@Override
	public NetworkElementWrapper getNetworkElementCountByNetype() {
		logger.info("going to get Network Element Count By Netype");
		try {
			return (NetworkElementWrapper) getEntityManager().createNamedQuery("getNetworkElementCountByNetype")
					.setParameter("domain", Domain.RAN).getSingleResult();
		} catch (Exception exception) {
			logger.error("Inside executeQuery, {}", ExceptionUtils.getStackTrace(exception));
		}
		return null;
	}

	@Override
	public List<NetworkElement> getChildNEListByParentNE(Integer parentNEid) {
		logger.info("inside Dao get child Network Element List for Parent Network Element");
		List<NetworkElement> neElementList = new ArrayList<>();
		try {
			logger.info("parentNEid :{}", parentNEid);
			TypedQuery<NetworkElement> query = getEntityManager().createNamedQuery("getChildNEListByParentNE",
					NetworkElement.class);
			query.setParameter("parentNEid", parentNEid);
			neElementList = query.getResultList();
		} catch (NoResultException ne) {
			logger.warn("no child Network Element found for Parent Id: {}", parentNEid);
		} catch (Exception ex) {
			logger.error(
					"Exception occured while getting Child Network Element for parent Network Element: {}, Cause: {}",
					parentNEid, ex.getMessage());
			throw new DaoException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
		return neElementList;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Deprecated
	public List<NetworkElement> getSitesByGeography(List<NEType> neTypeList, List<String> neNameList,
			List<String> neFrequencyList, List<NEStatus> neStatusList, List<Vendor> vendorList,
			List<Technology> technologyList, List<Domain> domainList, Map<String, Double> viewportMap,
			Map<String, List<String>> geographyNames) {
		try {
			logger.info(
					"Going to get NetworkElement Site Level data for neType : {} , neFrequency : {} , neStatus: {} , vendor : {}",
					neTypeList, neFrequencyList, neStatusList, vendorList);
			neNameList = InfraUtils.convertListElements(neNameList, InfraConstants.LIST_TOUPPERCASE);
			neFrequencyList = InfraUtils.convertListElements(neFrequencyList, InfraConstants.LIST_TOUPPERCASE);
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<NetworkElement> criteriaQuery = criteriaBuilder.createQuery(NetworkElement.class);
			Root<NetworkElement> outerNetworkElement = criteriaQuery.from(NetworkElement.class);
			Subquery<Integer> neSubQuery = criteriaQuery.subquery(Integer.class);
			Root<NetworkElement> innerNetworkElement = neSubQuery.from(NetworkElement.class);
			List<Predicate> innerNEPredicates = InfraUtils.getPredicatesForCriteriaBuilderForNE(criteriaBuilder,
					innerNetworkElement, neTypeList, neNameList, neFrequencyList, neStatusList, vendorList,
					technologyList, domainList, geographyNames, null);
			List<Predicate> outerNEPredicates = InfraUtils.getPredicatesForViewPortsForNE(criteriaBuilder,
					outerNetworkElement, viewportMap);
			neSubQuery.select(innerNetworkElement.get(InfraConstants.NE_NETWORKELEMENT_KEY)).distinct(true)
					.where(innerNEPredicates.toArray(new Predicate[] {}));
			criteriaQuery.select(outerNetworkElement).where(criteriaBuilder
					.in(outerNetworkElement.get(InfraConstants.NE_NETWORKELEMENTID_KEY)).value(neSubQuery),
					criteriaBuilder.and(outerNEPredicates.toArray(new Predicate[] {})));
			criteriaQuery.orderBy(criteriaBuilder.asc(outerNetworkElement.get(InfraConstants.NE_NENAME_KEY)));
			Query query = getEntityManager().createQuery(criteriaQuery);
			return query.getResultList();
		} catch (NullPointerException nullPointerException) {
			logger.error("NullPointerException caught while getting data from NetworkElement Exception : {}",
					Utils.getStackTrace(nullPointerException));
			throw new DaoException("Unable to get NetworkElement  Site Level data for specific parameters.");
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error("IllegalArgumentException caught while getting data from NetworkElement Exception : {}",
					Utils.getStackTrace(illegalArgumentException));
			throw new DaoException("Unable to get NetworkElement Site Level data for specific parameters .");
		} catch (NoResultException noResultException) {
			logger.error("NoResultException caught while getting data from NetworkElement Exception : {}",
					Utils.getStackTrace(noResultException));
			throw new DaoException("Unable to get NetworkElement Site Level data  for specific parameters.");
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while getting data from NetworkElement Exception : {}",
					Utils.getStackTrace(persistenceException));
			throw new DaoException("Unable to get NetworkElement Site Level data for specific parameters.");
		} catch (Exception exception) {
			logger.error("Exception caught while getting data from  NetworkElement Exception : {}",
					Utils.getStackTrace(exception));
			throw new DaoException("Unable to get NetworkElement Site Level data for specific parameters.");
		}
	}

	@Override
	public List<NetworkElement> getNeInfoByneFkAndNeTypeWithStatus(Integer networkFK, NEType neType,
			List<DayOneStatus> dayOneStatus) {
		logger.info("inside Dao get child Network Element List for networkFK:{} and neType:{} and dayOneStatus List:{}",
				networkFK, neType, dayOneStatus);
		List<NetworkElement> neElementList = new ArrayList<>();
		try {
			logger.info("networkFK :{}", networkFK);
			TypedQuery<NetworkElement> query = getEntityManager().createNamedQuery("getNeInfoByneFkAndNeTypeWithStatus",
					NetworkElement.class);
			query.setParameter("networkFK", networkFK).setParameter("neType", neType).setParameter("dayOneStatus",
					dayOneStatus);
			neElementList = query.getResultList();
			logger.info("NetworkElement List size:{}", neElementList.size());
		} catch (NoResultException ne) {
			logger.warn("no child Network Element found for networkFK: {}", networkFK);
		} catch (Exception ex) {
			logger.error("Exception occured while getting Child Network Element for networkFK: {}, Cause: {}",
					networkFK, ex.getMessage());
			throw new DaoException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
		return neElementList;
	}

	@Override
	public NetworkElement searchNetworkElementBySiteID(String siteID) {
		NetworkElement neElement = null;
		try {
			logger.info("siteID :{}", siteID);
			TypedQuery<NetworkElement> query = getEntityManager().createNamedQuery("searchByNeName",
					NetworkElement.class);
			query.setParameter("siteID", siteID);
			List<NetworkElement> neElementList = query.getResultList();
			if (neElementList != null && !neElementList.isEmpty()) {
				neElement = neElementList.get(ForesightConstants.ZERO);
			}
		} catch (NoResultException ne) {
			logger.error("no Entity found :{}", ne.getMessage());
		} catch (Exception e) {
			logger.error("error while getting count from DB :{}", ExceptionUtils.getStackTrace(e));
			throw new DaoException("Exception Occured while updating Record");
		}
		return neElement;
	}

	/**
	 * Gets the vdu and vcu ids by neids
	 *
	 * @param neids the neids
	 * @return the vdu and vcu by siteId
	 */
	@Override
	public List<String> getVduAndVcuByNeid(List<String> neids) {
		List<String> neDetails = null;
		try {
			Query query = getEntityManager().createNamedQuery("getVduAndVcuByNeid");
			query.setParameter("neids", neids);
			neDetails = query.getResultList();
		} catch (IllegalArgumentException e) {
			logger.warn("IllegalArgumentException while getVduAndVcuByNeid,err msg{}", e.getMessage());
		} catch (IllegalStateException e) {
			logger.warn("IllegalStateException while getVduAndVcuByNeid,err msg{}", e.getMessage());
		} catch (QueryTimeoutException e) {
			logger.warn("QueryTimeoutException while getVduAndVcuByNeid,err msg{}", e.getMessage());
		} catch (TransactionRequiredException e) {
			logger.warn("TransactionRequiredException while getVduAndVcuByNeid,err msg{}", e.getMessage());
		} catch (PersistenceException e) {
			logger.warn("PersistenceException while getVduAndVcuByNeid,err msg{}", e.getMessage());
		}
		return neDetails;
	}

	@Override
	public void updateCellAndSiteStatus(Integer id, NEStatus neStatus) {
		logger.info("inside updateCellAndSiteStatus method having id:{}", id);
		try {
			List<NEType> neTypeList = new ArrayList<>();
			neTypeList.add(NEType.MACRO);
			neTypeList.add(NEType.MACRO_CELL);
			neTypeList.add(NEType.MACRO_VDU);
			neTypeList.add(NEType.MACRO_ENB);
			logger.info("going to updateSiteAndCellNeStatus having neTypeList:{}", neTypeList);
			Query query = getEntityManager().createNamedQuery("updateCellAndSiteStatus").setParameter("id", id)
					.setParameter("neStatus", neStatus).setParameter("neTypeList", neTypeList);
			int updatedRecordCount = query.executeUpdate();
			logger.info("record update :{}", updatedRecordCount);
		} catch (NoResultException noResultException) {
			logger.error("Error Occured while updating record :{}", noResultException.getMessage());
		} catch (Exception e) {
			logger.error("Error Occured while updating record :{}", e.getMessage());
			throw new DaoException("Exception Occured while updating Record");
		}
	}

	@Override
	public void updateCoreGeography(String domain, String vendor, String neType, String neId, String pmEmsId) {
		logger.info("inside updateCellAndSiteStatus method having id:{}", neType);
		try {
			logger.info("going to pmEmsId :{}", pmEmsId);
			Query query = getEntityManager().createNamedQuery("updatePmEmsId")
					.setParameter("domain", Domain.valueOf(domain)).setParameter("vendor", Vendor.valueOf(vendor))
					.setParameter("neType", NEType.valueOf(neType)).setParameter("neId", neId)
					.setParameter("pmEmsId", pmEmsId);
			int updatedRecordCount = query.executeUpdate();
			logger.info("record update :{}", updatedRecordCount);
			if (updatedRecordCount == 0) {
				throw new NoResultException("Exception Occured while updating Record");
			}
		} catch (NoResultException noResultException) {
			logger.error("Error Occured while updating record :{}", noResultException.getMessage());
			throw new NoResultException("NoResultException Occured while updating Record");
		} catch (Exception e) {
			logger.error("Error Occured while updating record :{}", e.getMessage());
			throw new DaoException("Exception Occured while updating Record");
		}
	}

	@Override
	public List<MicroSiteDataWrapper> getNEDetailBySapId(String neName, String domain, String vendor) {
		logger.info("going to getNEDetail By  neName {}", neName);
		List<MicroSiteDataWrapper> siteDetails = new ArrayList<>();
		Query query = null;
		try {
			query = getEntityManager().createNamedQuery("getNEDetailBySapId");
			query.setParameter("neName", neName + "%");
			query.setParameter("domain", Domain.valueOf(domain));
			query.setParameter("vendor", Vendor.valueOf(vendor));
			siteDetails = query.getResultList();
		} catch (Exception e) {
			logger.warn("Error while getNEDetailBySapId,err msg{}", Utils.getStackTrace(e));
		}
		logger.info("Returning siteDetails list size {}", siteDetails.size());
		return siteDetails;
	}

	@Override
	public NetworkElement getNetworkElementByNeIdAndNetype(Integer parentId, NEType neType, String riuSerialNo) {
		try {
			TypedQuery<NetworkElement> query = getEntityManager().createNamedQuery("getNetworkElementByNeIdAndNetype",
					NetworkElement.class);
			query.setParameter("parentNEid", parentId);
			query.setParameter("netype", neType);
			query.setParameter("riuSerialNo", riuSerialNo);
			NetworkElement networkElement = query.getSingleResult();
			if (networkElement != null) {
				return networkElement;
			} else {
				throw new DaoException("No result found for parentId  " + parentId);
			}
		} catch (Exception e) {
			logger.error("error while getting for parentId  {}", parentId, ExceptionUtils.getStackTrace(e));
			return null;
		}

	}

	@Override
	public Integer getOnAirSitesCount(List<NEType> neType, String domain, String vendor) {
		Integer siteCount = 0;
		try {
			Query query = getEntityManager().createNamedQuery("getOnAirSitesCount").setParameter("neType", neType)
					.setParameter("domain", Domain.valueOf(domain)).setParameter("vendor", Vendor.valueOf(vendor));
			return ((Long) query.getSingleResult()).intValue();
		} catch (Exception e) {
			logger.error("Exception inside method getOnAirSitesCount StackTrace {}", Utils.getStackTrace(e));
		}
		return siteCount;
	}

	@Override
	public Map<String, Long> getSitesCountNestatusWise(List<NEType> neType) {
		Map<String, Long> sitesCountNestatusWise = new HashMap<>();
		try {
			Query query = getEntityManager().createNamedQuery("getSitesCountGroupByNestatus").setParameter("neType",
					neType);
			List<Object[]> siteCountList = query.getResultList();
			for (Object obj[] : siteCountList) {
				sitesCountNestatusWise.put(String.valueOf(obj[0]), (Long) obj[1]);
			}

		} catch (Exception e) {
			logger.error("Exception in getSitesCountNestatusWise - {}", Utils.getStackTrace(e));
		}
		return sitesCountNestatusWise;
	}

	@Override
	public Map<String, Set<String>> getAllChildbyNeId(String neid) {
		Map<String, Set<String>> allNodes = new HashMap<>();
		if (neid != null) {
			if (MapUtils.isEmpty(parentneChildId) || MapUtils.isEmpty(childParentneId)) {
				initializeRelationship();
			}
			Set<String[]> nearestChild = parentneChildId.get(neid);
			appendAllChilds(allNodes, nearestChild);

			String[] nearestParent = childParentneId.get(neid);
			appendAllParents(allNodes, nearestParent);
		}
		return allNodes;
	}

	private void appendAllChilds(Map<String, Set<String>> allChild, Set<String[]> nearestChild) {
		if (CollectionUtils.isEmpty(nearestChild)) {
			return;
		}

		for (String[] strings : nearestChild) {
			String newNeid = strings[0];
			String newNename = strings[1];
			String neType = strings[2];
			Set<String> list = allChild.get(neType);
			if (CollectionUtils.isEmpty(list)) {
				list = new HashSet<>();
			}
			list.add(newNename);
			allChild.put(neType, list);
			Set<String[]> set3 = parentneChildId.get(newNeid);
			if (CollectionUtils.isEmpty(set3)) {
				continue;
			}
			appendAllChilds(allChild, set3);
		}
	}

	private void appendAllParents(Map<String, Set<String>> allParent, String[] nearestParent) {
		if (nearestParent == null) {
			return;
		}
		String newNeid = nearestParent[0];
		String newNename = nearestParent[1];
		String neType = nearestParent[2];
		Set<String> list = allParent.get(neType);
		if (CollectionUtils.isEmpty(list)) {
			list = new HashSet<>();
		}
		list.add(newNename);
		allParent.put(neType, list);
		String[] set3 = childParentneId.get(newNeid);

		logger.info("In AppendAllParents : Parent not found for the neid {}", newNeid);
		appendAllParents(allParent, set3);
	}

	private static Map<String, Set<String[]>> parentneChildId = new HashMap<>();

	private static Map<String, String[]> childParentneId = new HashMap<>();

	@Override
	public void initializeRelationship() {
		try {
			logger.info("Going to populate data on parentNEId Map");
			parentneChildId = new HashMap<>();
			childParentneId = new HashMap<>();
			List<NEDetailWrapper> wrapperList = getNEDetail();
			for (NEDetailWrapper neDetailWrapper : wrapperList) {
				String pneId = neDetailWrapper.getPneId();
				if (StringUtils.isEmpty(pneId)) {
					continue;
				}
				Set<String[]> childSet = parentneChildId.get(pneId);
				if (CollectionUtils.isEmpty(childSet)) {
					childSet = new HashSet<>();
				}
				String[] neIdWithNename = new String[4];
				String[] pneIdWithPnename = new String[4];
				neIdWithNename[0] = neDetailWrapper.getNeId();
				neIdWithNename[1] = neDetailWrapper.getNename();
				neIdWithNename[2] = neDetailWrapper.getNetype();
				neIdWithNename[3] = neDetailWrapper.getNeStatus();

				pneIdWithPnename[0] = neDetailWrapper.getPneId();
				pneIdWithPnename[1] = neDetailWrapper.getPneName();
				pneIdWithPnename[2] = neDetailWrapper.getPneType();
				pneIdWithPnename[3] = neDetailWrapper.getPneStatus();

				childSet.add(neIdWithNename);
				parentneChildId.put(pneId, childSet);
				childParentneId.put(neDetailWrapper.getNeId(), pneIdWithPnename);
			}
			logger.info("total count of cne map {} ",  childParentneId.size());
			logger.info("total count of pne map " + parentneChildId.size());

		} catch (Exception e) {
			logger.error("Unable to populate data on parentNeId " + Utils.getStackTrace(e));
		}
	}

	@Override
	public Set<String> getAllRelatedNodes(String neid, NEType macro, List<String> neStatus, Boolean isNename) {
		Set<String> allRelations = new HashSet<>();
		int dataIndex = 0;
		if (isNename) {
			dataIndex = 1;
		}
		getAllChildNodeDetail(neid, macro, allRelations, neStatus, dataIndex);
		getAllParentNodeDetail(neid, macro, allRelations, neStatus, dataIndex);
		return allRelations;
	}

	@Override
	public Set<String> getAllParentNodes(String neid, NEType macro, List<String> neStatus, Boolean isNename) {
		Set<String> allRelations = new HashSet<>();
		int dataIndex = 0;
		if (isNename) {
			dataIndex = 1;
		}
		getAllParentNodeDetail(neid, macro, allRelations, neStatus, dataIndex);
		return allRelations;
	}

	@Override
	public Set<String> getAllChildNodes(String neid, NEType macro, List<String> neStatus, Boolean isNename) {
		Set<String> allRelations = new HashSet<>();
		int dataIndex = 0;
		if (isNename) {
			dataIndex = 1;
		}
		getAllChildNodeDetail(neid, macro, allRelations, neStatus, dataIndex);
		return allRelations;
	}

	private Set<String> getAllParentNodeDetail(String neid, NEType macro, Set<String> allRelations,
			List<String> neStatus, int dataIndex) {
		if (childParentneId.size() == 0) {
			initializeRelationship();
		}
		String[] value = childParentneId.get(neid);
		if (value == null) {
			return allRelations;
		}
		if (value != null && value.length > 3) {
			addNeDetailFromHierarchyMap(macro, value, allRelations, neStatus, dataIndex);
		}
		String[] recursiveValue = childParentneId.get(value[0]);
		if (recursiveValue != null && recursiveValue.length > 3) {
			addNeDetailFromHierarchyMap(macro, recursiveValue, allRelations, neStatus, dataIndex);
			getAllParentNodeDetail(recursiveValue[0], macro, allRelations, neStatus, dataIndex);
		}
		return allRelations;
	}

	private Set<String> getAllChildNodeDetail(String neid, NEType macro, Set<String> allRelations,
			List<String> neStatus, int dataIndex) {
		if (parentneChildId.size() == 0) {
			initializeRelationship();
		}
		Set<String[]> values = parentneChildId.get(neid);
		if (values == null) {
			return allRelations;
		}
		for (String[] value : values) {
			if (value != null && value.length > 3) {
				addNeDetailFromHierarchyMap(macro, value, allRelations, neStatus, dataIndex);
			}
			Set<String[]> recursiveValues = parentneChildId.get(value[0]);
			if (CollectionUtils.isNotEmpty(recursiveValues)) {
				for (String[] recursiveValue : recursiveValues) {
					if (recursiveValue != null && recursiveValue.length > 3) {
						addNeDetailFromHierarchyMap(macro, recursiveValue, allRelations, neStatus, dataIndex);
						getAllChildNodeDetail(recursiveValue[0], macro, allRelations, neStatus, dataIndex);
					}
				}
			}
		}
		return allRelations;
	}

	private void addNeDetailFromHierarchyMap(NEType macro, String[] value, Set<String> allRelations,
			List<String> neStatus, int dataIndex) {
		if (neStatus != null && macro != null) {
			if (StringUtils.equalsIgnoreCase(value[2], macro.name()) && neStatus.contains(value[3])) {
				allRelations.add(value[dataIndex]);
			}
		} else if (macro != null) {
			if (StringUtils.equalsIgnoreCase(value[2], macro.name())) {
				allRelations.add(value[dataIndex]);
			}
		} else if (neStatus != null) {
			if (neStatus.contains(value[3])) {
				allRelations.add(value[dataIndex]);
			}
		} else {
			allRelations.add(value[dataIndex]);
		}
	}

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public Long getTotalNECountByGeography(String geoLevel, Integer id, NEType neType, Vendor vendor, Domain domain,
			List<NEStatus> statusList) {
		Query query = null;
		try {
			if (StringUtils.equalsIgnoreCase(geoLevel, ForesightConstants.GEOGRAPHY_L1)) {
				query = getEntityManager().createNamedQuery("getTotalNECountByGeographyL1");
			} else if (StringUtils.equalsIgnoreCase(geoLevel, ForesightConstants.GEOGRAPHY_L2)) {
				query = getEntityManager().createNamedQuery("getTotalNECountByGeographyL2");
			} else if (StringUtils.equalsIgnoreCase(geoLevel, ForesightConstants.GEOGRAPHY_L3)) {
				query = getEntityManager().createNamedQuery("getTotalNECountByGeographyL3");
			} else if (StringUtils.equalsIgnoreCase(geoLevel, ForesightConstants.GEOGRAPHY_L4)) {
				query = getEntityManager().createNamedQuery("getTotalNECountByGeographyL4");
			} else {
				query = getEntityManager().createNamedQuery("getTotalNECountByPanGeography");
			}
			if (StringUtils.isNotEmpty(geoLevel)) {
				query.setParameter("id", id);
			}
			query.setParameter("neType", neType).setParameter("domain", domain).setParameter("vendor", vendor)
					.setParameter("statusList", statusList);
			if (query != null)
				return ((Long) query.getSingleResult());
		} catch (Exception e) {
			logger.error("going to get NetworkElementByGeography :{}", e);
		}
		return 0L;
	}

	private static Map<String, String[]> neNameWiseMap = new HashMap<>();

	@Override
	public void initializeNeNameNeIdMap() {
		try {
			logger.info("Going to populate data on NeNameNeId Map");
			neNameWiseMap = new HashMap<>();
			List<NEDetailWrapper> wrapperList = getNEDetail();
			for (NEDetailWrapper neDetailWrapper : wrapperList) {

				String[] neNameDetail = new String[4];

				neNameDetail[0] = neDetailWrapper.getNeId();
				neNameDetail[1] = neDetailWrapper.getNename();
				neNameDetail[2] = neDetailWrapper.getNetype();
				neNameDetail[3] = neDetailWrapper.getNeStatus();

				neNameWiseMap.put(neDetailWrapper.getNename(), neNameDetail);
			}
			logger.info("total count of neNameWise Map " + neNameWiseMap.size());

		} catch (Exception e) {
			logger.error("Unable to populate data on parentNeId " + ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public Map<String, String[]> getNenameWiseNEDetail() {
		if (neNameWiseMap.size() == 0) {
			logger.debug("going to intialize NEId with site Name map" + neNameWiseMap.size());
			initializeNeNameNeIdMap();
		}
		return neNameWiseMap;
	}

	@Override
	public Integer getCountForOnAirAndPlannedSites(String neType, String domain, String vendor, String status) {
		logger.info("inside getCountForOnAirAndPlannedSites method neType:{} and status:{}", neType, status);
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<NetworkElement> criteriaQuery = cb.createQuery(NetworkElement.class);
		TypedQuery<NetworkElement> query;
		try {
			Root<NetworkElement> networkElementRoot = criteriaQuery.from(NetworkElement.class);
			List<Predicate> predicateList = new ArrayList<>();
			if (status != null && neType != null) {
				predicateList.add(cb.equal(networkElementRoot.get(InfraConstants.NE_STATUS).as(NEStatus.class),
						NEStatus.valueOf(status)));
				predicateList.add(cb.equal(networkElementRoot.get(InfraConstants.NE_TYPE).as(NEType.class),
						NEType.valueOf(neType)));
				predicateList.add(cb.equal(networkElementRoot.get(InfraConstants.NE_ISDELETED_KEY).as(Integer.class),
						ForesightConstants.ZERO));
				criteriaQuery.select(networkElementRoot).where(predicateList.toArray(new Predicate[] {}));
				query = getEntityManager().createQuery(criteriaQuery);
				List<NetworkElement> networkElementList = query.getResultList();
				if (networkElementList != null && CollectionUtils.isNotEmpty(networkElementList)) {
					logger.info("getting SitesCount :{} for status {}", networkElementList.size(), status);
					return networkElementList.size();
				}
			}
		} catch (Exception e) {
			logger.error("getting exception while getting Count For Sites {}", e.getMessage());
			throw new DaoException("Exception Occured while Getting SitesCount");
		}
		return null;
	}

	@Override
	public List<NetworkElement> getChildByNetworkElementByParent(Integer id) {
		List<NetworkElement> neElement = new ArrayList<NetworkElement>();
		try {
			logger.info("id :{}", id);
			TypedQuery<NetworkElement> query = getEntityManager().createNamedQuery("getChildByNetworkElementByParent",
					NetworkElement.class);
			query.setParameter("id", id);
			neElement = query.getResultList();

		} catch (NoResultException ne) {
			logger.error("no Entity found :{}", ne.getMessage());
		} catch (Exception e) {
			logger.error("error while getChildByNetworkElementFk :{}", ExceptionUtils.getStackTrace(e));
		}
		return neElement;
	}

	@Override
	public List<NetworkHierarchyDetail> getSiteDetail(String type, String networkelementId) {
		logger.trace("Inside @method getDetailOfSite of @class {}", this.getClass().getSimpleName());
		logger.info("Inside @method getDetailOfSite for netype: {}, networkelementId: {} ", type, networkelementId);
		try {
			String dynamicQuery = "select new com.inn.foresight.core.infra.wrapper.NetworkHierarchyDetail(site.id, site.neName, site.networkElement.neName, site.networkElement.networkElement.neName, site.geographyL4.displayName, site.geographyL4.geographyL3.displayName, site.geographyL4.geographyL3.geographyL2.displayName, site.geographyL4.geographyL3.geographyL2.geographyL1.displayName) from NetworkElement site "
					+ "where site.neType = 'MACRO' and site.isDeleted = false and $filter";
			// and site.networkElement.neName is not null and
			// site.networkElement.networkElement.neName is not null
			String filterColumn = null;
			if (type.startsWith("Geography")) {
				filterColumn = getGeographyIdFromType(type);
				filterColumn += " in (:id)";
			} else {
				filterColumn = getSiteTypeFilter(type);
			}
			logger.info("filterColumn: {}", filterColumn);
			dynamicQuery = dynamicQuery.replaceAll("\\$filter", filterColumn);
			logger.info("getting final dynamicQuery: {}", dynamicQuery);
			TypedQuery<NetworkHierarchyDetail> query = getEntityManager().createQuery(dynamicQuery,
					NetworkHierarchyDetail.class);
			query.setParameter("id", Integer.parseInt(networkelementId));
			return query.getResultList();

		} catch (NoResultException nre) {
			logger.warn("NoResult found for selection netype: {} nename: {}", type, networkelementId);
		} catch (Exception error) {
			logger.error("Error while getting detail of Site: {}", ExceptionUtils.getStackTrace(error));
		}
		throw new DaoException(ForesightConstants.UNABLE_TO_PROCESS_REQUEST);
	}

	private String getSiteTypeFilter(String type) {
		String filterColumn;
		if (type.equalsIgnoreCase("MACRO_VDU")) {
			filterColumn = " site.networkElement.neName in (select n.neName from NetworkElement n where n.id =:id)";
		} else if (type.equalsIgnoreCase("MACRO_ENB")) {
			filterColumn = " site.networkElement.networkElement.neName in (select n.neName from NetworkElement n where n.id =:id)";
		} else {
			filterColumn = " site.id =:id";
		}
		return filterColumn;
	}

	@Override
	public List getSiteListingInfo(String type, String nename) {
		logger.trace("Inside @method getSiteListingInfo of @class: {}", this.getClass().getSimpleName());
		logger.info("Inside @method getSiteListingInfo with type: {} and nename: {}", type, nename);
		try {
			if (type.equalsIgnoreCase("MACRO")) {
				return getMacroSiteDetail(nename);
			}
			String dynamicQuery = "select new com.inn.foresight.core.infra.wrapper.NetworkHierarchyDetail(count(site.neName),$groupByCol,$id) from NetworkElement site where site.neType = 'MACRO' $filterParam and $groupByCol is not null and site.isDeleted = false group by $groupByCol,$id";
			String groupByCol = null;
			String id = null;
			if (type != null && type.startsWith("Geography")) {
				groupByCol = getGroupByColumnFromType(type);
				id = getGeographyIdFromType(type);
			} else {
				groupByCol = getGroupByColFromNeType(type);
				id = getNeIdFromType(type);
			}
			String filterParam = nename == null ? "" : "and " + groupByCol + " like (:nename)";
			logger.info("getting groupByCol: {} and filterParam: {}", groupByCol, filterParam);
			System.out.println("getting groupByCol: {} and filterParam: {}" + groupByCol + " " + filterParam);
			dynamicQuery = dynamicQuery.replaceAll("\\$groupByCol", groupByCol)
					.replaceAll("\\$filterParam", filterParam).replaceAll("\\$id", id);
			logger.info("final dynamicQuery : {}", dynamicQuery);

			Query query = getEntityManager().createQuery(dynamicQuery);
			if (nename != null) {
				query.setParameter("nename", nename + "%");
			}
			query.setMaxResults(10);
			return query.getResultList();
		} catch (Exception error) {
			logger.error("Error while getting listing data cause: {}", ExceptionUtils.getStackTrace(error));
		}
		throw new RestException(ForesightConstants.UNABLE_TO_PROCESS_REQUEST);
	}

	private String getNeIdFromType(String type) {
		String id = "site.networkElement.id";
		if (type.equalsIgnoreCase("MACRO_ENB")) {
			id = "site.networkElement.networkElement.id";
		}
		return id;
	}

	private List getMacroSiteDetail(String nename) {
		try {
			String dynamicQuery = "select new com.inn.foresight.core.infra.wrapper.NetworkHierarchyDetail(site.neName, site.id) from NetworkElement site where site.neType = 'MACRO' and site.isDeleted = false $filter";
			String filter = nename == null ? "" : "and site.neName like (:nename)";
			dynamicQuery = dynamicQuery.replaceAll("\\$filter", filter);
			logger.info("final dynamicQuery : {}", dynamicQuery);
			Query query = getEntityManager().createQuery(dynamicQuery);
			if (nename != null) {
				query.setParameter("nename", nename + "%");
			}
			query.setMaxResults(10);
			return query.getResultList();
		} catch (Exception error) {
			logger.error("Error while getting macrosite detail: {}", ExceptionUtils.getStackTrace(error));
		}
		throw new RestException(ForesightConstants.UNABLE_TO_PROCESS_REQUEST);
	}

	/*
	 * public static void main(String[] args) { NetworkElementDaoImpl obj = new
	 * NetworkElementDaoImpl(); String type = "GeographyL1"; String nename = "UH";
	 * obj.getSiteListingInfo(type, nename); }
	 */
	private String getGroupByColFromNeType(String type) {
		String groupByCol = "site.networkElement.neName";
		if (type.equalsIgnoreCase("MACRO_ENB")) {
			groupByCol = "site.networkElement.networkElement.neName";
		}
		return groupByCol;
	}

	private String getGroupByColumnFromType(String type) {
		String groupByCol = "site.geographyL4.name";
		if (type.equalsIgnoreCase("GeographyL3")) {
			groupByCol = "site.geographyL4.geographyL3.name";
		} else if (type.equalsIgnoreCase("GeographyL2")) {
			groupByCol = "site.geographyL4.geographyL3.geographyL2.name";
		} else if (type.equalsIgnoreCase("GeographyL1")) {
			groupByCol = "site.geographyL4.geographyL3.geographyL2.geographyL1.name";
		}
		return groupByCol;
	}

	private String getGeographyIdFromType(String type) {
		String groupByCol = "site.geographyL4.id";
		if (type.equalsIgnoreCase("GeographyL3")) {
			groupByCol = "site.geographyL4.geographyL3.id";
		} else if (type.equalsIgnoreCase("GeographyL2")) {
			groupByCol = "site.geographyL4.geographyL3.geographyL2.id";
		} else if (type.equalsIgnoreCase("GeographyL1")) {
			groupByCol = "site.geographyL4.geographyL3.geographyL2.geographyL1.id";
		}
		return groupByCol;
	}

	@Override
	public List<NetworkElement> getNetworkElementbyParentId(Integer parentId) {
		try {
			TypedQuery<NetworkElement> query = getEntityManager().createNamedQuery("getNEByParentId",
					NetworkElement.class);
			query.setParameter("pid", parentId);

			List<NetworkElement> networkElementList = query.getResultList();
			if (networkElementList != null) {
				return networkElementList;
			} else {
				throw new DaoException("No result found for parentId  " + parentId);
			}
		} catch (Exception e) {
			logger.error("error while getting for NetworkElement for parentId: {}", parentId,
					ExceptionUtils.getStackTrace(e));
			return null;
		}

	}

	@Override
	public List<NETaskDetailWrapper> getNETaskDetailData(String neName, String neType) {
		logger.info("going to NETaskDetail data for neName : {},neType : {}", neName, neType);
		List<NETaskDetailWrapper> neTaskDetailDataList = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getNETaskDetailDataByNameAndType");
			query.setParameter("neName", neName);
			query.setParameter("neType", NEType.valueOf(neType));
			neTaskDetailDataList = query.getResultList();
			logger.info("neTaskDetailDataList: {}", neTaskDetailDataList.size());
		} catch (Exception e) {
			logger.warn("Error while getNEDetailBySapId,err msg{}", Utils.getStackTrace(e));
		}
		return neTaskDetailDataList;
	}

	@Override
	public List<RANDetail> getNetworkElementIdByNameAndCellNum(String sourceSiteId, Integer sourceCellId,
			String sourceFrequencyBand, String vendor, String domain) {
		logger.info("going to networkelement data");
		List<RANDetail> ranDetailData = null;
		try {
			String dynamicQuery = "select r from  RANDetail r where r.networkElement.vendor =:vendor  and r.networkElement.domain =:domain  ";
			if (!sourceFrequencyBand.equalsIgnoreCase("All"))
				dynamicQuery += " and r.networkElement.neFrequency=:sourceFrequencyBand  ";
			if (sourceCellId != null) {
				dynamicQuery += " and r.networkElement.cellNum=:sourceCellId ";
			}
			if (sourceSiteId != null) {
				dynamicQuery += " and r.networkElement.networkElement.neName=:sourceSiteId ";
			}

			Query query = getEntityManager().createQuery(dynamicQuery);
			query.setParameter("vendor", Vendor.valueOf(vendor));
			query.setParameter("domain", Domain.valueOf(domain));
			if (!sourceFrequencyBand.equalsIgnoreCase("All"))
				query.setParameter("sourceFrequencyBand", sourceFrequencyBand);
			if (sourceCellId != null)
				query.setParameter("sourceCellId", sourceCellId);
			if (sourceSiteId != null)
				query.setParameter("sourceSiteId", sourceSiteId);
			ranDetailData = query.getResultList();
		} catch (Exception exception) {
			logger.warn("Error while getting networkelementid ,err msg{}", Utils.getStackTrace(exception));
		}
		return ranDetailData;
	}

	@Override
	public int updateNetworkElementStatusByVCUId(Integer vcuId, DayOneStatus status) {
		try {
			Query query = getEntityManager().createNamedQuery("updateNetworkElementStatusByVCUId")
					.setParameter("vcuId", vcuId).setParameter("status", status);
			int updatedRecordCount = query.executeUpdate();
			logger.info("record update :{}", updatedRecordCount);
			return updatedRecordCount;
		} catch (Exception e) {
			logger.error("error while updating NetworkElement for vcuId: {}", vcuId, ExceptionUtils.getStackTrace(e));
			return 0;
		}
	}

	@Override
	public List<NetworkElementWrapper> getNEStatusCount(List<NEType> neTypeList, List<Domain> domainList,
			Map<String, List<String>> geographyNames) {
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<NetworkElementWrapper> criteriaQuery = criteriaBuilder
					.createQuery(NetworkElementWrapper.class);
			Root<NetworkElement> networkElement = criteriaQuery.from(NetworkElement.class);
			criteriaQuery = getConstructorStatement(criteriaBuilder, networkElement, criteriaQuery);
			List<Predicate> predicates = getPredicates(criteriaBuilder, networkElement, neTypeList, domainList,
					geographyNames);
			criteriaQuery = getGroupByStatement(networkElement, criteriaQuery);
			Query query = getEntityManager().createQuery(criteriaQuery.where(predicates.toArray(new Predicate[] {})));
			List<NetworkElementWrapper> networkElementWrapperList = query.getResultList();
			return networkElementWrapperList;
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error("IllegalArgumentException caught while getting nestatus count. Message : {}",
					illegalArgumentException.getMessage());
			throw new DaoException("Unable to get nestatus count.");
		} catch (NoResultException noResultException) {
			logger.error("NoResultException caught while getting nestatus count. Message : {}",
					noResultException.getMessage());
			throw new DaoException("Unable to get nestatus count.");
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while getting nestatus count. Message : {}",
					persistenceException.getMessage());
			throw new DaoException("Unable to get nestatus count.");
		} catch (Exception exception) {
			logger.error("Exception caught while getting nestatus count. Message : {}", exception.getMessage());
			throw new DaoException("Unable to get nestatus count.");
		}
	}

	private List<Predicate> getPredicates(CriteriaBuilder criteriaBuilder, Root<NetworkElement> networkElement,
			List<NEType> neTypeList, List<Domain> domainList, Map<String, List<String>> geographyNames) {
		List<Predicate> predicates = new ArrayList<>();
		List<String> geographies = new ArrayList<>();
		try {
			predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_NETYPE_KEY)).in(neTypeList));
			predicates.add(criteriaBuilder.equal(networkElement.get(InfraConstants.NE_ISDELETED_KEY), false));
			predicates.add(criteriaBuilder.upper(networkElement.get(InfraConstants.NE_DOMAIN_KEY)).in(domainList));
			if (geographyNames != null && !geographyNames.isEmpty()) {
				if (geographyNames.get(InfraConstants.GEOGRAPHYL4_TABLE) != null) {
					geographies = geographyNames.get(InfraConstants.GEOGRAPHYL4_TABLE);
					predicates.add(criteriaBuilder.upper(
							networkElement.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.BOUNDARY_NAME))
							.in(geographies));
				}
				if (geographyNames.get(InfraConstants.GEOGRAPHYL3_TABLE) != null) {
					geographies = geographyNames.get(InfraConstants.GEOGRAPHYL3_TABLE);
					predicates.add(criteriaBuilder
							.upper(networkElement.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
									.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.BOUNDARY_NAME))
							.in(geographies));
				}
				if (geographyNames.get(InfraConstants.GEOGRAPHYL2_TABLE) != null) {
					geographies = geographyNames.get(InfraConstants.GEOGRAPHYL2_TABLE);
					predicates
							.add(criteriaBuilder
									.upper(networkElement.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
											.get(InfraConstants.NE_GEOGRAPHYL3_KEY)
											.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.BOUNDARY_NAME))
									.in(geographies));
				}
				if (geographyNames.get(InfraConstants.GEOGRAPHYL1_TABLE) != null) {
					geographies = geographyNames.get(InfraConstants.GEOGRAPHYL1_TABLE);
					predicates.add(criteriaBuilder
							.upper(networkElement.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
									.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY)
									.get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.BOUNDARY_NAME))
							.in(geographies));
				}
			}
		} catch (Exception exception) {
			logger.error("Error in adding predicates. Message : {}", exception.getMessage());
		}
		return predicates;
	}

	private CriteriaQuery<NetworkElementWrapper> getConstructorStatement(CriteriaBuilder builder,
			Root<NetworkElement> networkElement, CriteriaQuery<NetworkElementWrapper> criteriaQuery) {
		try {
			criteriaQuery.select(
					builder.construct(NetworkElementWrapper.class, networkElement.get(InfraConstants.NE_NESTATUS_KEY),
							builder.countDistinct(networkElement.get(InfraConstants.NE_NETWORKELEMENTID_KEY))));
		} catch (Exception exception) {
			logger.error("Error in getting constructor statement. Message : {}", exception.getMessage());
		}
		return criteriaQuery;
	}

	private CriteriaQuery<NetworkElementWrapper> getGroupByStatement(Root<NetworkElement> networkElement,
			CriteriaQuery<NetworkElementWrapper> criteriaQuery) {
		try {
			criteriaQuery.groupBy(networkElement.get(InfraConstants.NE_NESTATUS_KEY));
		} catch (Exception exception) {
			logger.error("Error in  getting group by statement.Message : {}", exception.getMessage());
		}
		return criteriaQuery;
	}

	@Override
	public List<NetworkElement> getChildFromParent(Integer netwokElementFk) {
		logger.info("going to get Child ListFrom Parent data by netwokElementFk : {}", netwokElementFk);
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<NetworkElement> criteriaQuery = criteriaBuilder.createQuery(NetworkElement.class);
			Root<NetworkElement> networkElement = criteriaQuery.from(NetworkElement.class);
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(criteriaBuilder.equal(networkElement.get("networkElement"), netwokElementFk));
			TypedQuery<NetworkElement> query = getEntityManager()
					.createQuery(criteriaQuery.where(predicates.toArray(new Predicate[] {})));
			List<NetworkElement> networkElementList = query.getResultList();
			return networkElementList;
		} catch (NoResultException noResultException) {
			logger.error("NO Network Element  Found  : {}", noResultException.getMessage());
		} catch (Exception exception) {
			logger.error("Exception caught while getting child list : {}", ExceptionUtils.getStackTrace(exception));
			throw new DaoException("Exception caught while getting child list");
		}
		return null;
	}

	@Transactional(readOnly = true)
	@Override
	public NetworkElement getNetworkElementByIPV4(String ipV4) {
		logger.info("Going to get NetworkElementByIPV4 for ipV4 :{}", ipV4);
		List<NetworkElement> networkElementDetail = new ArrayList<NetworkElement>();
		try {
			Query query = getEntityManager().createNamedQuery("getNetworkElementByIPV4")
					.setParameter(ForesightConstants.IPV4, ipV4);
			networkElementDetail = query.getResultList();
			if (networkElementDetail != null && !networkElementDetail.isEmpty()) {
				return networkElementDetail.get(0);
			}
		} catch (NoResultException | IllegalStateException | QueryTimeoutException | TransactionRequiredException e) {
			logger.warn("Error while getNetworkElementByIPV4,err msg{}", Utils.getStackTrace(e));
		}
		return new NetworkElement();
	}

	@Override
	public List<Object[]> getNEFriendlyNameListByNENameList(List<String> networkElementsList) {
		List<Object[]> result = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getNEFriendlyNameListByNENameList")
					.setParameter("neNamesList", networkElementsList);
			result = query.getResultList();
		} catch (Exception exception) {
			logger.info("Exception Occured While calling @getNEFriendlyNameListByNENameList StackTrace:{}",
					Utils.getStackTrace(exception));
		}
		return result;
	}

	@Override
	public List<NetworkElement> getNetworkElementByNeType(List<NEType> neTypeList) {
		try {
			TypedQuery<NetworkElement> query = getEntityManager()
					.createNamedQuery("getNEbyNETypeList", NetworkElement.class).setParameter("neTypeList", neTypeList);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Getting Exception while getting data for NeTypeList {}, Trace {}", neTypeList,
					org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
		}
		return Collections.emptyList();
	}

	@Override
	public List<Tuple> searchNetworkElementDetails(Map<String, List<Map>> filters, Map<String, List<String>> projection,
			List groupByList, List orderByList, boolean isDistinct, Map viewportMap) {
		try {
			logger.info("filters {}", filters);
			logger.info("Going to search NetworkElement Details for input parameters.");
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Tuple> criteriaTupleQuery = criteriaBuilder.createTupleQuery();
			List<Selection<?>> selection = new ArrayList<Selection<?>>();
			List<Predicate> predicateList = new ArrayList<Predicate>();
			List<String> distinctEntity = new ArrayList<String>();
			Map criteriaMap = new HashMap<>();
			distinctEntity = InfraUtils.getDistinctEntity(distinctEntity, filters, projection);
			Map<String, Root> rootMap = CriteriaUtils.getRootMap(distinctEntity, criteriaTupleQuery);
			criteriaMap = (Map) CriteriaUtils.getParametersForCells(viewportMap, criteriaBuilder, predicateList,
					distinctEntity, filters, criteriaTupleQuery, rootMap, null);
			predicateList = (List<Predicate>) criteriaMap.get(InfraConstants.PREDICATE);
			selection = (List<Selection<?>>) criteriaMap.get(InfraConstants.SELECTION);
			Expression<?>[] expressions = InfraUtils
					.getExpressionObject(rootMap.get(InfraConstants.NETWORKELEMENT_TABLE), groupByList);
			InfraUtils.getCriteriaQueryForGroupBy(criteriaTupleQuery, expressions);
			Order[] orders = InfraUtils.getOrderObject(rootMap.get(InfraConstants.NETWORKELEMENT_TABLE), orderByList,
					criteriaBuilder);
			InfraUtils.getCriteriaQueryForOrderBy(criteriaTupleQuery, orders);
			predicateList = CriteriaUtils.getParametersForNewtorkElements(null, criteriaBuilder, predicateList,
					distinctEntity, filters, criteriaTupleQuery, rootMap, null);
			filters.remove(InfraConstants.NETWORKELEMENT_TABLE);
			projection.remove(InfraConstants.NETWORKELEMENT_TABLE);
			predicateList = CriteriaUtils.getCriteriaPredicates(criteriaBuilder, rootMap, predicateList, filters);
			selection = CriteriaUtils.getSelectionForCriteriaBuilder(rootMap, projection, selection);
			if (isDistinct) {
				criteriaTupleQuery.select(criteriaBuilder.tuple(selection.toArray(new Selection<?>[] {})))
						.distinct(isDistinct);
			} else {
				criteriaTupleQuery.select(criteriaBuilder.tuple(selection.toArray(new Selection<?>[] {})));
			}
			Query query = getEntityManager()
					.createQuery(criteriaTupleQuery.where(predicateList.toArray(new Predicate[] {})));
			return query.getResultList();
		} catch (NullPointerException nullPointerException) {
			logger.error("NullPointerException caught while searching NetworkElement details Exception : {}",
					Utils.getStackTrace(nullPointerException));
			throw new DaoException("Unable to  search NetworkElement details.");
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error("IllegalArgumentException caught while searching NetworkElement details Exception : {}",
					Utils.getStackTrace(illegalArgumentException));
			throw new DaoException(" Unable to search NetworkElement details.");
		} catch (NoResultException noResultException) {
			logger.error("NoResultException caught while searching NetworkElement details Exception : {}",
					Utils.getStackTrace(noResultException));
			throw new DaoException("Unable  to search NetworkElement details.");
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while searching NetworkElement details Exception : {}",
					Utils.getStackTrace(persistenceException));
			throw new DaoException("Unable to  search NetworkElement details. ");
		} catch (Exception exception) {
			logger.error("Exception caught while searching NetworkElement details Exception : {}",
					Utils.getStackTrace(exception));
			throw new DaoException("Unable to search NetworkElement details. ");
		}
	}

	@Override
	public List<Tuple> sectorPropertiesDetails(Map<String, List<Map>> filters, Map<String, List<String>> projection,
			List groupByList, List orderByList, boolean isDistinct, Map viewportMap) {
		try {
			logger.info("filters {}", filters);
			logger.info("Going to search NetworkElement Details for input parameters.");
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Tuple> criteriaTupleQuery = criteriaBuilder.createTupleQuery();
			List<Selection<?>> selection = new ArrayList<Selection<?>>();
			List<Predicate> predicateList = new ArrayList<Predicate>();
			List<String> distinctEntity = new ArrayList<String>();
			Map criteriaMap = new HashMap<>();
			distinctEntity = InfraUtils.getDistinctEntity(distinctEntity, filters, projection);
			Map<String, Root> rootMap = CriteriaUtils.getRootMap(distinctEntity, criteriaTupleQuery);
			criteriaMap = CriteriaUtils.getNetworkElementDetails(criteriaMap, criteriaBuilder, predicateList, filters,
					selection, distinctEntity, criteriaTupleQuery, projection, viewportMap, rootMap, null);
			predicateList = (List<Predicate>) criteriaMap.get(InfraConstants.PREDICATE);
			selection = (List<Selection<?>>) criteriaMap.get(InfraConstants.SELECTION);
			Expression<?>[] expressions = InfraUtils
					.getExpressionObject(rootMap.get(InfraConstants.NETWORKELEMENT_TABLE), groupByList);
			InfraUtils.getCriteriaQueryForGroupBy(criteriaTupleQuery, expressions);
			Order[] orders = InfraUtils.getOrderObject(rootMap.get(InfraConstants.NETWORKELEMENT_TABLE), orderByList,
					criteriaBuilder);
			InfraUtils.getCriteriaQueryForOrderBy(criteriaTupleQuery, orders);
			predicateList = CriteriaUtils.getParametersForNewtorkElements(null, criteriaBuilder, predicateList,
					distinctEntity, filters, criteriaTupleQuery, rootMap, null);
			filters.remove(InfraConstants.NETWORKELEMENT_TABLE);
			projection.remove(InfraConstants.NETWORKELEMENT_TABLE);
			predicateList = CriteriaUtils.getCriteriaPredicates(criteriaBuilder, rootMap, predicateList, filters);
			selection = CriteriaUtils.getSelectionForCriteriaBuilder(rootMap, projection, selection);
			if (isDistinct) {
				criteriaTupleQuery.select(criteriaBuilder.tuple(selection.toArray(new Selection<?>[] {})))
						.distinct(isDistinct);
			} else {
				criteriaTupleQuery.select(criteriaBuilder.tuple(selection.toArray(new Selection<?>[] {})));
			}
			Query query = getEntityManager()
					.createQuery(criteriaTupleQuery.where(predicateList.toArray(new Predicate[] {})));
			return query.getResultList();
		} catch (NullPointerException nullPointerException) {
			logger.error("NullPointerException caught while searching NetworkElement details Exception : {}",
					Utils.getStackTrace(nullPointerException));
			throw new DaoException("Unable to  search NetworkElement details.");
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error("IllegalArgumentException caught while searching NetworkElement details Exception : {}",
					Utils.getStackTrace(illegalArgumentException));
			throw new DaoException(" Unable to search NetworkElement details.");
		} catch (NoResultException noResultException) {
			logger.error("NoResultException caught while searching NetworkElement details Exception : {}",
					Utils.getStackTrace(noResultException));
			throw new DaoException("Unable  to search NetworkElement details.");
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while searching NetworkElement details Exception : {}",
					Utils.getStackTrace(persistenceException));
			throw new DaoException("Unable to  search NetworkElement details. ");
		} catch (Exception exception) {
			logger.error("Exception caught while searching NetworkElement details Exception : {}",
					Utils.getStackTrace(exception));
			throw new DaoException("Unable to search NetworkElement details. ");
		}
	}

	@Override
	@Transactional
	public NetworkElement getNetworkElementDataBySiteId(String siteId) {
		logger.info("Going to get NetworkElement data for site id :{}", siteId);
		List<NetworkElement> networkElementDetail = new ArrayList<NetworkElement>();
		try {
			Query query = getEntityManager().createNamedQuery("getNetworkElementDataBySiteId").setParameter("siteId",
					siteId);
			networkElementDetail = (List<NetworkElement>) query.getResultList();

			if (networkElementDetail != null && !networkElementDetail.isEmpty()) {
				return networkElementDetail.get(0);
			}
		} catch (NoResultException | IllegalStateException | QueryTimeoutException | TransactionRequiredException e) {
			e.printStackTrace();
			logger.warn("Error while getNetworkElementDataBySiteId,err msg{}", Utils.getStackTrace(e));
		}
		return null;
	}

	@Override
	@Transactional
	public List<NetworkElement> getNetworkElementListById(List<Integer> networkElementIdList) {
		logger.info("Going to get NetworkElement data for id .");
		List<NetworkElement> networkElementDetail = new ArrayList<NetworkElement>();
		try {
			Query query = getEntityManager().createNamedQuery("getNetworkElementListById")
					.setParameter("networkElementIdList", networkElementIdList);
			return (List<NetworkElement>) query.getResultList();
		} catch (NoResultException | IllegalStateException | QueryTimeoutException | TransactionRequiredException e) {
			e.printStackTrace();
			logger.warn("Error while getNetworkElementListById,err msg{}", Utils.getStackTrace(e));
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NEType> getDistinctNeType(String domain) {
		Query query = null;
		List<NEType> neTypeList = new ArrayList<>();
		logger.info("Going to get NeType Details by domain{} ", domain);
		try {
			query = getEntityManager().createNamedQuery("getDistinctNeType");
			logger.info("query :{}", query);
			query.setParameter(ForesightConstants.DOMAIN_SMALL, Domain.valueOf(domain));
			neTypeList = query.getResultList();
			logger.info("netype list:{}, neTypeList size :{}", neTypeList, neTypeList.size());
		} catch (Exception exp) {
			logger.error("Exception while get Site Count for distinct neType,ErrMsg :{}", Utils.getStackTrace(exp));
			throw new DaoException(exp);
		}
		return neTypeList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<NetworkElement> getNetworkElementsDetailByGeoMapping(List<NEType> neTypeList, List<String> neNameList,
			List<String> neFrequencyList, List<NEStatus> neStatusList, List<Vendor> vendorList,
			List<Technology> technologyList, List<Domain> domainList, Map<String, Double> viewportMap,
			Map<String, List<String>> geographyNames, List<String> neIdList, String geoLevelKey) {
		logger.info(
				"Going to get NetworkElement details for geoLevelKey : {} , neType : {} , neFrequency : {} , neStatus: {} , vendor : {}",
				geoLevelKey, neTypeList, neFrequencyList, neStatusList, vendorList);
		try {
			neNameList = InfraUtils.convertListElements(neNameList, InfraConstants.LIST_TOUPPERCASE);
			neFrequencyList = InfraUtils.convertListElements(neFrequencyList, InfraConstants.LIST_TOUPPERCASE);
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<NetworkElement> criteriaQuery = criteriaBuilder.createQuery(NetworkElement.class);
			Root<NetworkElement> networkElement = criteriaQuery.from(NetworkElement.class);
			List<Predicate> nePredicates = InfraUtils.getPredicatesForCriteriaBuilderForNE(criteriaBuilder,
					networkElement, neTypeList, neNameList, neFrequencyList, neStatusList, vendorList, technologyList,
					domainList, geographyNames, neIdList, geoLevelKey);
			nePredicates.add(criteriaBuilder.equal(networkElement.get(InfraConstants.ISDELETED_STATUS),
					ForesightConstants.ZERO));

			List<Predicate> viewportPredicates = InfraUtils.getPredicatesForViewPortsForNE(criteriaBuilder,
					networkElement, viewportMap);
			List<Predicate> finalPredicates = new ArrayList<>();
			finalPredicates.addAll(nePredicates);
			finalPredicates.addAll(viewportPredicates);
			criteriaQuery.select(networkElement).where(finalPredicates.toArray(new Predicate[] {}));
			criteriaQuery.orderBy(criteriaBuilder.asc(networkElement.get(InfraConstants.NE_NENAME_KEY)));
			Query query = getEntityManager().createQuery(criteriaQuery);
			return query.getResultList();
		} catch (NullPointerException | IllegalArgumentException | PersistenceException exception) {
			logger.error(
					"Exception caught while getting NetworkElement details by geo mapping from NetworkElement Exception : {}",
					Utils.getStackTrace(exception));
			throw new DaoException("Unable to get NetworkElement details by geo mapping data for specific parameters.");
		}
	}

	@Override
	public Map<String, Map<String, Object>> getNECountByNETypeAndGeography(List<NEType> neTypeList, Integer geographyId,
			String geographyType) {
		logger.info("going to get getNECountByNETypeAndGeography by neTypeList : {},and geographyId:{}", neTypeList,
				geographyId);
		Map<String, Map<String, Object>> resultMap = new HashMap<>();

		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
			Root<NetworkElement> networkElement = criteriaQuery.from(NetworkElement.class);
			if (geographyType.equalsIgnoreCase("geographyL1")) {
				criteriaQuery.multiselect(criteriaBuilder.count(networkElement.get("id")), networkElement.get("neName"),
						networkElement.get("neType"), networkElement.get("geographyL2").get("name"),
						networkElement.get("geographyL2").get("id"));
			} else if (geographyType.equalsIgnoreCase("geographyL2")) {
				criteriaQuery.multiselect(criteriaBuilder.count(networkElement.get("id")), networkElement.get("neName"),
						networkElement.get("neType"), networkElement.get("geographyL3").get("name"),
						networkElement.get("geographyL3").get("id"));
			} else if (geographyType.equalsIgnoreCase("geographyL3")) {
				criteriaQuery.multiselect(criteriaBuilder.count(networkElement.get("id")), networkElement.get("neName"),
						networkElement.get("neType"), networkElement.get("geographyL4").get("name"),
						networkElement.get("geographyL4").get("id"));
			}

			List<Predicate> predicates = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(neTypeList))
				predicates.add(networkElement.get(InfraConstants.NE_NETYPE_KEY).in(neTypeList));
			if (ObjectUtils.allNotNull(geographyType, geographyId)
					&& geographyType.matches("geographyL1|geographyL2|geographyL3|geographyL4")) {

				if (geographyType.equalsIgnoreCase("geographyL1")) {
					predicates.add(criteriaBuilder.equal(networkElement.get("geographyL1").get("id"), geographyId));
					criteriaQuery.groupBy(networkElement.get("geographyL2").get("name"), networkElement.get("neType"));
				} else if (geographyType.equalsIgnoreCase("geographyL2")) {
					predicates.add(criteriaBuilder.equal(networkElement.get("geographyL2").get("id"), geographyId));
					criteriaQuery.groupBy(networkElement.get("geographyL3").get("name"), networkElement.get("neType"));
				} else if (geographyType.equalsIgnoreCase("geographyL3")) {
					predicates.add(criteriaBuilder.equal(networkElement.get("geographyL3").get("id"), geographyId));
					criteriaQuery.groupBy(networkElement.get("geographyL4").get("name"), networkElement.get("neType"));
				}
			}

			TypedQuery<Object[]> query = getEntityManager()
					.createQuery(criteriaQuery.where(predicates.toArray(new Predicate[] {})));
			List<Object[]> networkElementList = query.getResultList();
			for (Object[] networkElementObject : networkElementList) {
				Map<String, Object> networkElementMap = new HashMap<>();
				networkElementMap.put("networkElementId", networkElementObject[0]);
				networkElementMap.put("neName", networkElementObject[1]);
				networkElementMap.put("neType", networkElementObject[2]);
				networkElementMap.put("geoGraphyName", networkElementObject[3]);
				networkElementMap.put("geoGraphyId", networkElementObject[4]);
				resultMap.put(String.valueOf(networkElementObject[3]), networkElementMap);
			}
			System.out.println((new Gson()).toJson(resultMap));
			return resultMap;
		} catch (NoResultException noResultException) {
			logger.error("NO Network Element  Found  : {}", noResultException.getMessage());
		} catch (Exception exception) {
			logger.error("Exception caught while getting child list : {}", ExceptionUtils.getStackTrace(exception));
			throw new DaoException("Exception caught while getting child list");
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> getTotalSiteCountByGeoGraphyWise(String geoType, NEType neType, Vendor vendor,
			Domain domain, List<NEStatus> statusList, Set<String> geographyName) {
		logger.info(
				"Inside Method  getTotalSiteCountByGeoGraphyWise  geoType: {},and neType : {}, vendor : {},domain : {} ,statusList : {}",
				geoType, neType, vendor, domain, statusList);
		Map<String, Object> geoGraphyWiseCountMap = new HashMap<>();
		try {
			geoType = geographychecks(geoType);
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
			Root<NetworkElement> networkElement = criteriaQuery.from(NetworkElement.class);
			criteriaQuery.multiselect(criteriaBuilder.count(networkElement.get("id")),
					networkElement.get(geoType).get(ForesightConstants.DISPLAY_NAME));
			List<Predicate> predicates = new ArrayList<>();
			if (StringUtils.isNotBlank(neType.name()))
				predicates.add(criteriaBuilder.equal(networkElement.get(InfraConstants.NE_NETYPE_KEY), neType));

			if (StringUtils.isNotBlank(vendor.name()))
				predicates.add(criteriaBuilder.equal(networkElement.get(InfraConstants.VENDOR_KEY), vendor));

			if (StringUtils.isNotBlank(domain.name()))
				predicates.add(criteriaBuilder.equal(networkElement.get(InfraConstants.NE_DOMAIN_KEY), domain));

			if (geographyName != null && !geographyName.isEmpty() && geoType != null && !geoType.isEmpty()) {
				predicates.add(networkElement.get(geoType).get(ForesightConstants.DISPLAY_NAME).in(geographyName));
				criteriaQuery.groupBy(networkElement.get(geoType).get(ForesightConstants.DISPLAY_NAME));
			}

			if (CollectionUtils.isNotEmpty(statusList))
				predicates.add(networkElement.get(InfraConstants.NE_NESTATUS_KEY).in(statusList));

			predicates.add(criteriaBuilder.equal(networkElement.get("isDeleted"), false));
			TypedQuery<Object[]> query = getEntityManager()
					.createQuery(criteriaQuery.where(predicates.toArray(new Predicate[] {})));
			List<Object[]> resultList = query.getResultList();
			for (Object[] countdata : resultList) {

				geoGraphyWiseCountMap.put(String.valueOf(countdata[1]), countdata[0]);

			}
			logger.info("countList : {}", geoGraphyWiseCountMap);
		} catch (NoResultException noResultException) {
			logger.error("NO Network Element  Found  : {}", noResultException.getMessage());
		} catch (Exception exception) {
			logger.error("Exception caught while getting child list : {}", ExceptionUtils.getStackTrace(exception));
		}

		return geoGraphyWiseCountMap;
	}

	private String geographychecks(String geoType) {
		if (geoType.equalsIgnoreCase("l1")) {
			geoType = "geographyL1";
		} else if (geoType.equalsIgnoreCase("l2")) {
			geoType = "geographyL2";
		} else if (geoType.equalsIgnoreCase("l3")) {
			geoType = "geographyL3";
		} else if (geoType.equalsIgnoreCase("l4")) {
			geoType = "geographyL4";
		}
		return geoType;
	}

	@Override
	public List<String> getMostParentNEIDByChildNEID(List<String> neIdList) {
		logger.info("Going to get Most ParentNEID By ChildNEID");
		List<String> mostParentNeidList = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getMostParentNEIDByChildNEID").setParameter("neidList",
					neIdList);
			mostParentNeidList = query.getResultList();
		} catch (NoResultException noResultException) {
			logger.error("NO Most  Parent  Found  : {}", noResultException.getMessage());
		} catch (Exception exception) {
			logger.error("Exception caught while getting most parent list : {}",
					ExceptionUtils.getStackTrace(exception));
		}
		return mostParentNeidList;
	}

	@Override
	public Long getSiteCountByGC(Integer locationId) {
		logger.info("Inside getSiteCountByGC: locationId={} ", locationId);
		Long count = 0l;
		try {
			TypedQuery<Long> query = getEntityManager().createNamedQuery("getSiteCountByLocationId", Long.class)
					.setParameter("locationId", locationId);

			count = query.getSingleResult();

		} catch (Exception e) {
			logger.error("Exception in getSiteCountByGC: locationId={} error={}", locationId, e.getMessage());
		}
		return count;
	}

	@Override
	public List<NetworkElementWrapper> getSitesInfoByGC(Integer locationid) {
		List<NetworkElementWrapper> resultList = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getSitesInfoByLocationId").setParameter("locationId",
					locationid);
			resultList = query.getResultList();
		} catch (Exception e) {
			logger.error("Exception caught while getting  list : {}", ExceptionUtils.getStackTrace(e));
		}
		return resultList;
	}

	@Override
	public List<NetworkElementWrapper> getDomainVendorAndL3ByMultiNEPk(List<Integer> pkIds) {
		try {
			logger.debug("going to get domain, vendor and GeographyL3 for neid: {}", pkIds);
			return getEntityManager().createNamedQuery("getDomainVendorAndL3ByMultiNEPk").setParameter("pkId", pkIds)
					.getResultList();
		} catch (NoResultException e) {
			logger.warn("no such domain, vendor and Geography found for neid: {}", pkIds);
			return null;
		}
	}

	@Override
	public Integer getSiteCountForGCByNeStatus(String neType, String nelType, String nelId, String parentNeType,
			String neStatus) {
		Integer siteCount = 0;
		try {
			Query query = getEntityManager().createNamedQuery("getSiteCountForGCByNeStatus")
					.setParameter("neType", NEType.valueOf(neType)).setParameter("nelType", NELType.valueOf(nelType))
					.setParameter("nelId", nelId).setParameter("parentNeType", NEType.valueOf(parentNeType))
					.setParameter("neStatus", NEStatus.valueOf(neStatus));
			return ((Long) query.getSingleResult()).intValue();
		} catch (Exception e) {
			logger.error("Exception inside method getOnAirSitesCount StackTrace {}", Utils.getStackTrace(e));
		}
		return siteCount;
	}

	@Override
	public List<NetworkElement> getAllCellsData(Integer llimit, Integer ulimit) {
		List<NetworkElement> cellList = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getAllCellsData");
			if (llimit != null && ulimit != null) {
				query.setFirstResult(llimit);
				query.setMaxResults((ulimit - llimit) + 1);
			}
			cellList = query.getResultList();
			logger.info("cellList is : {}", cellList.size());
		} catch (Exception e) {
			logger.error("Error in getAllCells : {}", Utils.getStackTrace(e));
		}
		return cellList;
	}

	@Override
	public Long getSiteCountByLatLong(Double southWestLong, Double southWestLat, Double northEastLong,
			Double northEastLat, NEType neType) {
		logger.info(
				"Inside getSiteCountByLatLong: southWestLong: {} ,southWestLat: {} , northEastLong: {}, northEastLat: {}, neType: {}",
				southWestLong, southWestLat, northEastLong, northEastLat, neType);
		Long count = 0l;
		try {
			TypedQuery<Long> query = getEntityManager().createNamedQuery("getSiteCountByLatLong", Long.class);
			query.setParameter("southWestLong", southWestLong).setParameter("southWestLat", southWestLat)
					.setParameter("northEastLong", northEastLong).setParameter("northEastLat", northEastLat)
					.setParameter("neType", neType);

			count = query.getSingleResult();

		} catch (Exception e) {
			logger.error(
					"Exception in getSiteCountByLatLong: southWestLong: {} ,southWestLat: {} , northEastLong: {}, northEastLat: {}, neType: {} error={}",
					southWestLong, southWestLat, northEastLong, northEastLat, neType, e.getMessage());
		}
		return count;
	}

	@Transactional
	@Override
	public List<NetworkElement> getNetworkElementByNELocationId(Integer nelocationid_fk, String neType, Integer llimit,
			Integer ulimit) {
		logger.info("Going to get NetworkElement Data by nelocationid_fk :{},neType :{}", nelocationid_fk, neType);
		List<NetworkElement> networkElementData = new ArrayList<>();
		try {
			TypedQuery<NetworkElement> query = getEntityManager().createNamedQuery("getNetworkElementByNELocationId",
					NetworkElement.class);
			query.setParameter("neLocationId", nelocationid_fk);
			if (llimit != null && ulimit != null) {
				query.setFirstResult(llimit);
				query.setMaxResults((ulimit - llimit) + 1);
			}
			networkElementData = query.getResultList();
			logger.info("networkElementData size is : {}", networkElementData.size());
		} catch (Exception e) {
			logger.error("Error in getNetworkElementByNELocationId : {}", Utils.getStackTrace(e));
		}
		return networkElementData;
	}

	@Override
	public List<Tuple> getNetworkElementDetails(Map<String, List<Map>> filters, Map<String, List<String>> projection,
			Integer lLimit, Integer uLimit, boolean isGroupBy, boolean isDistinct) throws DaoException {
		List<Predicate> predicateList = new ArrayList<>();
		List<String> distinctEntity = new ArrayList<>();
		List<Selection<?>> selection = new ArrayList<>();
		List<Expression<?>> expression = new ArrayList<>();
		Map<String, Root> rootMap = new HashMap<>();
		try {
			logger.info(
					"Going to get data for Inbuilding Sites  for filter:{} ,projection : {} ,lLimit : {} ,uLimit : {} "
							+ ",isGroupBy {},isDistinct {} ",
					filters, projection, lLimit, uLimit, isGroupBy, isDistinct);
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
			distinctEntity = InfraUtils.getDistinctEntityForGeography(distinctEntity, filters, projection);
			rootMap = CriteriaUtils.getRootMap(distinctEntity, criteriaQuery);
			predicateList = CriteriaUtils.getRANPlanJoin(criteriaBuilder, filters, predicateList, criteriaQuery,
					rootMap);
			predicateList = CriteriaUtils.getCriteriaPredicates(criteriaBuilder, rootMap, predicateList, filters);
			selection = CriteriaUtils.getSelectionForCriteriaBuilder(rootMap, projection, selection);
			expression = CriteriaUtils.getExpressionForSPMS(rootMap, projection, expression, isGroupBy,
					criteriaBuilder);

			if (isGroupBy) {
				selection.add(criteriaBuilder
						.count(rootMap.get(InfraConstants.NETWORKELEMENT_TABLE).get(InfraConstants.NE_NEID_KEY))
						.alias(InfraConstants.TOTAL_COUNT_KEY));
				criteriaQuery.groupBy(expression.toArray(new Expression<?>[] {}));
			}
			if (selection != null && !selection.isEmpty()) {
				criteriaQuery.select(criteriaBuilder.tuple(selection.toArray(new Selection<?>[] {}))).isDistinct();
			}
			if (predicateList != null && !predicateList.isEmpty()) {
				criteriaQuery.where(predicateList.toArray(new Predicate[] {}));
			}

			/*
			 * criteriaQuery.orderBy(criteriaBuilder.desc(rootMap.get(InfraConstants.
			 * NETWORKELEMENT_TABLE) .get(InfraConstants.CREATION_TIME)));
			 */
			Query query = getEntityManager().createQuery(criteriaQuery);
			if (lLimit != null && uLimit != null) {
				CriteriaUtils.setPagination(lLimit, uLimit, query);
			}
			List<Tuple> tuples = query.getResultList();

			logger.info("Size of Data Recieved :{}", tuples.size());
			return tuples;

		} catch (IllegalArgumentException | QueryTimeoutException ee) {
			logger.error("Unable to get data for getIBSDetail exception:{}", Utils.getStackTrace(ee));
			throw new DaoException("Data not recieved");
		} catch (Exception exception) {
			logger.error("Exception in getting IBSDetail {}", Utils.getStackTrace(exception));
			throw new DaoException("Data not found");
		}
	}

	@Override
	public NetworkElement getNEDataByNeIdAndNeType(String neId, NEType netype) {
		NetworkElement networkElement = null;
		try {
			logger.info("inside getNEDataByNEIdAndNeType nename {} neType {} ", neId, netype);
			networkElement = getEntityManager().createNamedQuery("getNEDataByNeIdAndNeType", NetworkElement.class)
					.setParameter("neId", neId).setParameter("netype", netype).getSingleResult();
			logger.info("Inside method@ getNEDataByNeIdAndNeType  networkelement id {}", networkElement.getId());
		} catch (Exception e) {
			logger.error("Exception Inside getNEDataByNeIdAndNeType due to {}", e);
		}
		return networkElement;
	}

	/**
	 * Note :- This method is temporary method and will be removed shortly. Suggest
	 * you to not use it.
	 * 
	 * @param slNo
	 * @param neType
	 * @return
	 */
	@Deprecated
	@Override
	public NetworkElement getNetworkElementBySlNoAndType(String slNo, String neType) {
		logger.info("Logging Dao");
		try {
			return (NetworkElement) getEntityManager().createNamedQuery("getNetworkElementBySlNoAndType")
					.setParameter("slNo", slNo).setParameter("neType", NEType.valueOf(neType)).getSingleResult();
		} catch (NonUniqueResultException e) {
			logger.error("Multiple records found for {}, {}", slNo, neType);
		} catch (Exception e) {
			logger.error("Error in getting records {}, {}, {}", slNo, neType, Utils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public NetworkElement getNEByVCUNameAndCellNum(String neType, String vcuName, Integer cellNum) {
		logger.info("inside getNEByVCUNameAndCellNum");
		try {
			return (NetworkElement) getEntityManager().createNamedQuery("getNEByVCUNameAndCellNum")
					.setParameter("neType", NEType.valueOf(neType)).setParameter("vcuName", vcuName)
					.setParameter("cellNum", cellNum).getSingleResult();
		} catch (Exception e) {
			logger.error("Error in getNEByVCUNameAndCellNum {} ,  {}, {}, {}", neType, vcuName, cellNum,
					Utils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public Integer getMaxCellNumByParentNEandNEType(Integer parentneid, String neType) {
		logger.info("Inside @Class : " + this.getClass().getName()
				+ " @Method : getMaxCellNumByParentNEandNEType parentneid {} neType {} ", parentneid, neType);
		Integer maxCellNum = 0;
		try {
			Query query = getEntityManager().createNamedQuery("getMaxCellNumByParentNEandNEType")
					.setParameter("parentneid", parentneid).setParameter("neType", NEType.valueOf(neType));
			return ((Integer) query.getSingleResult());
			
		} catch (NullPointerException ne) {
			logger.error("NullPointerException inside method getMaxCellNumByParentNEandNEType StackTrace {}",
					Utils.getStackTrace(ne));
			return maxCellNum;
		} catch (Exception e) {
			logger.error("Exception inside method getMaxCellNumByParentNEandNEType StackTrace {}",
					Utils.getStackTrace(e));
			throw new DaoException(ExceptionUtil.generateExceptionCode("Dao", InfraConstants.NETWORKELEMENT_TABLE, e));
		}

	}

	@Override
	public List<Object[]> getTopologyGridViewFilterData(Map<String, Object> filterMap, Integer llimit, Integer ulimit) {
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
			Root<NetworkElement> cellNEData = criteriaQuery.from(NetworkElement.class);
			Join<NetworkElement, NetworkElement> siteNEData = cellNEData.join("networkElement");
			Join<NetworkElement, NetworkElement> vduNEData = siteNEData.join("networkElement");
			Join<NetworkElement, NetworkElement> vcuNEData = vduNEData.join("networkElement");
			Join<NetworkElement, NELocation> nelJoin = vcuNEData.join("neLocation", JoinType.LEFT);
			selectTopologyGridViewData(criteriaQuery, cellNEData, siteNEData, vduNEData, vcuNEData, nelJoin);

			List<Predicate> predicates = new ArrayList<>();
			predicates.add(criteriaBuilder.equal(cellNEData.get("isDeleted"), false));
			predicates.add(criteriaBuilder.equal(cellNEData.get("neType"), NEType.MACRO_CELL));
			predicates.add(criteriaBuilder.equal(cellNEData.get("neStatus"), NEStatus.ONAIR));
			setTopologyGridViewFilterData(filterMap, criteriaBuilder, cellNEData, predicates, nelJoin, vduNEData,
					vcuNEData, siteNEData);

			TypedQuery<Object[]> createQuery = getEntityManager()
					.createQuery(criteriaQuery.where(predicates.toArray(new Predicate[] {})));
			if (llimit != null && ulimit != null && ulimit > llimit) {
				createQuery.setFirstResult(llimit);
				createQuery.setMaxResults((ulimit - llimit) + 1);
			}
			List<Object[]> resultList = createQuery.getResultList();
			logger.info("resultList : {}", new Gson().toJson(resultList));
			return resultList;
		} catch (Exception e) {
			logger.error("Error in getTopologyGridViewFilterData : {}", Utils.getStackTrace(e));
		}
		return null;
	}

	private void setTopologyGridViewFilterData(Map<String, Object> filterMap, CriteriaBuilder criteriaBuilder,
			Root<NetworkElement> cellNEData, List<Predicate> predicates, Join<NetworkElement, NELocation> nelJoin,
			Join<NetworkElement, NetworkElement> vduNEData, Join<NetworkElement, NetworkElement> vcuNEData,
			Join<NetworkElement, NetworkElement> siteNEData) {
		if (filterMap != null && !filterMap.isEmpty()) {
			if (filterMap.get("siteId") != null) {
				predicates.add(criteriaBuilder.equal(siteNEData.get("neName"), filterMap.get("siteId").toString()));
			}

			if (filterMap.get("gcId") != null) {
				predicates.add(criteriaBuilder.equal(nelJoin.get("locationCode"), filterMap.get("gcId").toString()));
			}

			if (filterMap.get("rfCluster") != null) {
				predicates.add(criteriaBuilder.equal(siteNEData.get("geographyL4").get("displayName"),
						filterMap.get("rfCluster").toString()));
			}

			if (filterMap.get("gcName") != null) {
				predicates.add(criteriaBuilder.equal(nelJoin.get("nelId"), filterMap.get("gcName").toString()));
			}

			if (filterMap.get("gcCity") != null) {
				predicates.add(criteriaBuilder.equal(siteNEData.get("geographyL3").get("displayName"),
						filterMap.get("gcCity").toString()));
			}

			if (filterMap.get("vduHost") != null) {
				predicates.add(criteriaBuilder.equal(vduNEData.get("neName"), filterMap.get("vduHost").toString()));
			}

			if (filterMap.get("vcuHost") != null) {
				predicates.add(criteriaBuilder.equal(vcuNEData.get("neName"), filterMap.get("vcuHost").toString()));
			}
		}
	}

	private void selectTopologyGridViewData(CriteriaQuery<Object[]> criteriaQuery, Root<NetworkElement> cellNEData,
			Join<NetworkElement, NetworkElement> siteNEData, Join<NetworkElement, NetworkElement> vduNEData,
			Join<NetworkElement, NetworkElement> vcuNEData, Join<NetworkElement, NELocation> nelJoin) {
		try {
			criteriaQuery.multiselect(siteNEData.get("neName"), siteNEData.get("enbid"), siteNEData.get("latitude"),
					siteNEData.get("longitude"), siteNEData.get("geographyL4").get("displayName"),
					cellNEData.get("neName"), cellNEData.get("neId"), cellNEData.get("ecgi"), siteNEData.get("neName"),
					siteNEData.get("neId"), nelJoin.get("locationCode"), nelJoin.get("nelId"), nelJoin.get("latitude"),
					nelJoin.get("longitude"), siteNEData.get("geographyL3").get("displayName"), vduNEData.get("neName"),
					vcuNEData.get("neName"));
		} catch (Exception e) {
			logger.error("Error in selectTopologyGridViewData : {}", Utils.getStackTrace(e));
		}
	}

	@Override
	public NetworkElement searchNetworkElementByPhysicalSerialNo(String physicalSerialNo) {
		logger.info("Inside Method  searchNetworkElementByPhysicalSerialNo  method having physicalSerialNo {}",
				physicalSerialNo);
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<NetworkElement> criteriaQuery = criteriaBuilder.createQuery(NetworkElement.class);
			Root<NetworkElement> networkElement = criteriaQuery.from(NetworkElement.class);

			List<Predicate> predicates = new ArrayList<>();
			if (StringUtils.isNotBlank(physicalSerialNo))
				predicates.add(criteriaBuilder.equal(networkElement.get(InfraConstants.PHYSICAL_SERIAL_NUMBER),
						physicalSerialNo));

			TypedQuery<NetworkElement> query = getEntityManager()
					.createQuery(criteriaQuery.where(predicates.toArray(new Predicate[] {})));
			return query.getSingleResult();
		} catch (NoResultException noResultException) {
			logger.error("NO Network Element  Found  for pysicalserialNo : {},:{} ", physicalSerialNo,
					noResultException.getMessage());
			return null;
		} catch (Exception exception) {
			logger.error("Exception Occured while Searching NetworkElement by PhysicalSeerialNo : {}",
					ExceptionUtils.getStackTrace(exception));
			throw new DaoException("NO Network Element  Found  for pysicalserialNo");
		}
	}
	@Override
	public List<String> getfilterByNename(String neName, NEType neType) {
		List<String> finalFilter = new ArrayList<>();
		try {
			if (neName != null && neType != null) {
				Query query = getEntityManager().createNamedQuery("getfilterByNeName").setParameter("neName", neName)
						.setParameter("neType", neType);
				finalFilter = (List<String>) query.setMaxResults(10).getResultList();
			}
		} catch (Exception e) {
			logger.error("Exception caught while getting  list : {}", Utils.getStackTrace(e));
		}
		return finalFilter;
	}
	
	
	public static void main(String[] args) {
		System.out.println("user :  " +AESUtils.decrypt("4Vgg4wzkUoroENaepsRWNjkSM08CIYZavDrcx0RdMUs="));
		System.out.println("pwd :  " +AESUtils.decrypt("4Vgg4wzkUoroENaepsRWNopdd/qrbBTNG2K2ytO7W6I="));
		
		
	}

}
package com.inn.foresight.core.infra.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.ISmallCellSiteDetailDao;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.foresight.core.infra.model.RANDetail;
import com.inn.foresight.core.infra.model.SmallCellSiteDetail;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.NEStatus;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.utils.enums.Technology;
import com.inn.foresight.core.infra.utils.enums.Vendor;
import com.inn.foresight.core.infra.wrapper.NetworkElementWrapper;

/**
 * The Class SmallCellSiteDetailDaoImpl.
 */
@Repository("SmallCellSiteDetailDaoImpl")
public class SmallCellSiteDetailDaoImpl extends HibernateGenericDao<Integer, SmallCellSiteDetail>
		implements ISmallCellSiteDetailDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(SmallCellSiteDetailDaoImpl.class);

	/**
	 * Instantiates a new small cell site detail dao impl.
	 */
	public SmallCellSiteDetailDaoImpl() {
		super(SmallCellSiteDetail.class);
	}

	/**
	 * Creates the.
	 *
	 * @param smallCellSiteDetail
	 *            the small cell site detail
	 * @return the small cell site detail
	 * @throws DaoException
	 *             the dao exception
	 */
	@Override
	public SmallCellSiteDetail create(SmallCellSiteDetail smallCellSiteDetail) {
		return super.create(smallCellSiteDetail);
	}

	/**
	 * Gets the small site by site id sec and vendor.
	 *
	 * @param neType
	 *            the ne type
	 * @param sapid
	 *            the sapid
	 * @param band
	 *            the band
	 * @param vendor
	 *            the vendor
	 * @param technology
	 *            the technology
	 * @param domain
	 *            the domain
	 * @return the small site by site id sec and vendor
	 */
	@Override
	public NetworkElementWrapper getSmallCellSiteDetail(String neType, String sapid, String band, String vendor,
			String technology, String domain) {
		logger.info("Going to getSmallSiteBySapidSecAndBand by sapid,vendor,band [{},{},{}]", sapid, vendor, band);
		NetworkElementWrapper smallSite = null;
		try {
			Query query = getEntityManager().createNamedQuery("getSmallSiteBySiteIdSecAndVendor");
			query.setParameter(ForesightConstants.NE_NAME, sapid);
			query.setParameter(ForesightConstants.BAND, band);
			query.setParameter(ForesightConstants.VENDOR_SMALL, Vendor.valueOf(vendor.toUpperCase()));
			query.setParameter(ForesightConstants.TECHNOLOGY, Technology.valueOf(technology.toUpperCase()));
			query.setParameter(ForesightConstants.NE_TYPE, NEType.valueOf(neType));
			query.setParameter(ForesightConstants.DOMAIN_SMALL, Domain.valueOf(domain.toUpperCase()));
			query.setParameter("neStatus", NEStatus.ONAIR);
			smallSite = (NetworkElementWrapper) query.getSingleResult();
		} catch (NoResultException ne) {
			logger.error("NoResultException  err msg {}", ne.getMessage());
		} catch (NonUniqueResultException nu) {
			logger.error("NonUniqueResultException  err msg {}", nu.getMessage());
		} catch (IllegalStateException e) {
			logger.warn("IllegalStateException, err msg{}", e.getMessage());
		} catch (QueryTimeoutException e) {
			logger.warn("QueryTimeoutException  ,err msg{}", e.getMessage());
		} catch (TransactionRequiredException e) {
			logger.warn("TransactionRequiredException  ,err msg{}", e.getMessage());
		} catch (PersistenceException e) {
			logger.warn("PersistenceException  ,err msg{}", e.getMessage());
		}
		return smallSite;
	}

	/**
	 * Gets the small site by sap id and band.
	 *
	 * @param sapId
	 *            the sap id
	 * @param band
	 *            the band
	 * @param sector
	 *            the sector
	 * @return the small site by sap id and band
	 */
	@Override
	public NetworkElementWrapper getSmallSiteBySapIdAndBand(String sapId, String band, Integer sector) {
		logger.info("Going to getSmallSiteBySapIdAndBand by sapId,band,sector [{},{},{}]", sapId, band, sector);
		NetworkElementWrapper smallSite = null;
		try {
			Query query = getEntityManager().createNamedQuery("getSmallSiteBySapIdAndBand");
			query.setParameter(ForesightConstants.NE_NAME, sapId);
			query.setParameter(ForesightConstants.BAND, band);
			query.setParameter("sector", sector);
			smallSite = (NetworkElementWrapper) query.getSingleResult();
		} catch (NoResultException ne) {
			logger.error("NoResultException err msg {} ", ne.getMessage());
		} catch (NonUniqueResultException nu) {
			logger.error("NonUniqueResultException err msg {} ", nu.getMessage());
		} catch (IllegalStateException e) {
			logger.warn(" IllegalStateException,err msg{}", e.getMessage());
		} catch (QueryTimeoutException e) {
			logger.warn("QueryTimeoutException ,err  msg{}", e.getMessage());
		} catch (TransactionRequiredException e) {
			logger.warn("TransactionRequiredException  ,err msg{}", e.getMessage());
		} catch (PersistenceException e) {
			logger.warn("PersistenceException ,err msg {}", e.getMessage());
		}
		return smallSite;
	}

	/**
	 * Gets the small site by sap id.
	 *
	 * @param sapId
	 *            the sap id
	 * @return the small site by sap id
	 */
	@Override
	public List<NetworkElementWrapper> getSmallSiteBySapId(String sapId) {
		logger.info("Going to getSmallSiteBySapId by sapId [{}]", sapId);
		List<NetworkElementWrapper> smallSite = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getSmallSiteBySapId");
			query.setParameter(ForesightConstants.NE_NAME, sapId);
			smallSite = query.getResultList();
		} catch (IllegalStateException e) {
			logger.warn("IllegalStateException , err, msg{}", e.getMessage());
		} catch (QueryTimeoutException e) {
			logger.warn("QueryTimeoutException , err, msg{}", e.getMessage());
		} catch (TransactionRequiredException e) {
			logger.warn(" TransactionRequiredException , err, msg{}", e.getMessage());
		} catch (PersistenceException e) {
			logger.warn("PersistenceException ,err, msg{}", e.getMessage());
		}
		return smallSite;
	}

	@Override
	public List<NetworkElementWrapper> getNEIdForTracePort(Integer geographyL4Id) {
		logger.info("Going to get getNEIdsForTraceport for netype smallcell by geographyL4Id {} ", geographyL4Id);
		List<NetworkElementWrapper> resultList = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getSmallCellNEIdsForTraceport");
			query.setParameter(InfraConstants.KEY_GEOGRAPHYL4ID, geographyL4Id);
			query.setParameter(InfraConstants.NE_STATUS, NEStatus.ONAIR);
			resultList = query.getResultList();
		} catch (IllegalStateException | QueryTimeoutException | TransactionRequiredException e) {
			logger.warn("Exception while get getNEIdsForTraceport,err msg {}", e.getMessage());
		}
		logger.info("Returning a  getNEIdsForTraceport resultList size {} ", resultList.size());
		return resultList;
	}

	@Override
	public Integer getSmallCellEnbIdByNEId(Integer networkElementId) {
		logger.info("Going to get SmallCell EnbId By NEId by networkElementId {} ", networkElementId);
		Integer enodeBId = null;
		try {
			Query query = getEntityManager().createNamedQuery("getSmallCellEnbIdByNEId");
			query.setParameter("neId", networkElementId);
			List<Integer> enbId = query.getResultList();
			if (enbId.size() > 0) {
				enodeBId = enbId.get(0);
			}
		} catch (IllegalStateException | QueryTimeoutException | TransactionRequiredException e) {
			logger.warn("Exception while get SmallCell EnbId By NEId by networkElementId ,err msg {}", e.toString());
		}
		logger.info("Returing enodeBId {}", enodeBId);
		return enodeBId;
	}
	
	@Override
	public List<NetworkElementWrapper> getSmallCellDetailByGeographyL4(List<String> bandList, String domain, String vendor, String geographyName) {
		List<NetworkElementWrapper> results = new ArrayList<>();

		logger.info("going to get small cell detail for  {} {} {}",bandList, domain, vendor);
		try {
			Query query = getEntityManager().createNamedQuery("getSmallCellDetailByGeographyL4")
					.setParameter("bandList", bandList).setParameter(ForesightConstants.NE_TYPE, NEType.ODSC_CELL)
					.setParameter("domain", Domain.valueOf(domain)).setParameter("vendor", Vendor.valueOf(vendor)).setParameter("geographyName", geographyName);
			results = query.getResultList();
			logger.info("Getting results size for small cell details : {}", results.size());
		} catch (Exception e) {
			logger.error("Error in getting small cell detail by latitude and longitude :{} ", Utils.getStackTrace(e));
			throw new PersistenceException(e.getMessage());
		}
		if(results.isEmpty()) {
			throw new DaoException(ForesightConstants.ERROR_NO_SMALL_CELLS_AVAILABLE);
		}
		return results;
	}

	@Override
	@Transactional
	public List<RANDetail> getSmallCellSiteDetails(String neName, String neType) {
		logger.info("Going to get SmallCell Site Details for neName {}", neName);
		List<RANDetail> smallCellSiteDetail = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getSmallCellSiteDetails");
			query.setParameter(ForesightConstants.NE_NAME, neName);
			query.setParameter(ForesightConstants.NE_TYPE, NEType.valueOf(neType));
			smallCellSiteDetail = query.getResultList();
			logger.info("smallCellSiteDetail : {}",smallCellSiteDetail);
		} catch (NoResultException noResultException) {
			
			logger.error("NoResultException err msg : {}", noResultException.getMessage());
		} catch (NonUniqueResultException nonUniqueResultException) {
			
			logger.error("NonUniqueResultException err msg : {}", nonUniqueResultException.getMessage());
		} catch (IllegalStateException illegalStateException) {
			
			logger.warn("IllegalStateException,err msg : {}", illegalStateException.getMessage());
		} catch (QueryTimeoutException queryTimeoutException) {
			
			logger.warn("QueryTimeoutException ,err msg : {}", queryTimeoutException.getMessage());
		} catch (TransactionRequiredException transactionRequiredException) {
			
			logger.warn("TransactionRequiredException ,err msg: {}", transactionRequiredException.getMessage());
		} catch (PersistenceException persistenceException) {
			
			logger.warn("PersistenceException ,err msg : {}", persistenceException.getMessage());
		}
		return smallCellSiteDetail;
	}
	
	@Override
	@Transactional
	public List<Integer> getDistinctSmallCellSiteCellIds(String neType) {
		logger.info("Going to get SmallCell Site Details for neName");
		List<Integer> cellIds = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getDistinctSmallCellSiteCellIds");
			query.setParameter(ForesightConstants.NE_TYPE, NEType.valueOf(neType));
			cellIds = query.getResultList();
			logger.info("smallCellSiteDetail : {}",cellIds);
		} catch (NoResultException noResultException) {
			
			logger.error("NoResultException err msg {}", noResultException.getMessage());
		} catch (NonUniqueResultException nonUniqueResultException) {
			
			logger.error("NonUniqueResultException err msg {}", nonUniqueResultException.getMessage());
		} catch (IllegalStateException illegalStateException) {
			
			logger.warn("IllegalStateException,err msg{}", illegalStateException.getMessage());
		} catch (QueryTimeoutException queryTimeoutException) {
			
			logger.warn("QueryTimeoutException ,err msg{}", queryTimeoutException.getMessage());
		} catch (TransactionRequiredException transactionRequiredException) {
			
			logger.warn("TransactionRequiredException ,err msg{}", transactionRequiredException.getMessage());
		} catch (PersistenceException persistenceException) {
			
			logger.warn("PersistenceException ,err msg{}", persistenceException.getMessage());
		}
		return cellIds;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<NetworkElement> getNetworkElementBySmallCells(String domain, String vendor, String neType, List<String> cellsList) {
		logger.info("Going to get NetworkElement By SmallCells size {}", cellsList.size());
		List<NetworkElement> networkElementList = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getNetworkElementBySmallCells");
			query.setParameter("domain", Domain.valueOf(domain));
			query.setParameter("vendor", Vendor.valueOf(vendor));
			query.setParameter(ForesightConstants.NE_TYPE, NEType.valueOf(neType));
			query.setParameter("cellsList", cellsList);
			networkElementList = query.getResultList();
			logger.info("cellsList : {}",networkElementList.size());
		} catch (NoResultException noResultException) {
			
			logger.error("NoResultException err msg {}", noResultException.getMessage());
		} catch (NonUniqueResultException nonUniqueResultException) {
			
			logger.error("NonUniqueResultException err msg {}", nonUniqueResultException.getMessage());
		} catch (IllegalStateException illegalStateException) {
			
			logger.warn("IllegalStateException,err msg{}", illegalStateException.getMessage());
		} catch (QueryTimeoutException queryTimeoutException) {
			
			logger.warn("QueryTimeoutException ,err msg{}", queryTimeoutException.getMessage());
		} catch (TransactionRequiredException transactionRequiredException) {
			
			logger.warn("TransactionRequiredException ,err msg{}", transactionRequiredException.getMessage());
		} catch (PersistenceException persistenceException) {
			
			logger.warn("PersistenceException ,err msg{}", persistenceException.getMessage());
		}
		return networkElementList;
	}
	

}
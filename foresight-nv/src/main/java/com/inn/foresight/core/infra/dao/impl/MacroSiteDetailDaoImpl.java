package com.inn.foresight.core.infra.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.inn.commons.maps.LatLng;
import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.IMacroSiteDetailDao;
import com.inn.foresight.core.infra.model.MacroSiteDetail;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.InfraUtils;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.NEStatus;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.utils.enums.Technology;
import com.inn.foresight.core.infra.utils.enums.Vendor;
import com.inn.foresight.core.infra.wrapper.MacroSitesDetailWrapper;
import com.inn.foresight.core.infra.wrapper.NetworkElementWrapper;
import com.inn.foresight.core.infra.wrapper.SiteDataWrapper;
import com.inn.foresight.core.infra.wrapper.SiteSummaryOverviewWrapper;

/**
 * The Class MacroSiteDetailDaoImpl.
 */
@Repository("MacroSiteDetailDaoImpl")
public class MacroSiteDetailDaoImpl extends HibernateGenericDao<Integer, MacroSiteDetail> implements IMacroSiteDetailDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(MacroSiteDetailDaoImpl.class);

	/**
	 * Instantiates a new macro site detail dao impl.
	 */
	public MacroSiteDetailDaoImpl() {
		super(MacroSiteDetail.class);
	}

	/**
	 * Creates the.
	 *
	 * @param macroSiteDetail
	 *            the macro site detail
	 * @return the macro site detail
	 * @throws DaoException
	 *             the dao exception
	 */
	@Override
	public MacroSiteDetail create(MacroSiteDetail macroSiteDetail) {
		return super.create(macroSiteDetail);
	}

	/**
	 * Find by pk.
	 *
	 * @param entityPk
	 *            the entity pk
	 * @return the macro site detail
	 * @throws DaoException
	 *             the dao exception
	 */
	@Override
	public MacroSiteDetail findByPk(Integer entityPk) {
		return super.findByPk(entityPk);
	}

	/**
	 * Gets the macro site detail bysap id sec and band.
	 *
	 * @param sapid
	 *            the sapid
	 * @param sectorId
	 *            the sector id
	 * @param band
	 *            the band
	 * @return the macro site detail bysap id sec and band
	 */
	@Override
	public NetworkElementWrapper getMacroSiteDetailBySapIdSecAndBand(String sapid, Integer sectorId, String band) {
		logger.info("Going to getMacroSiteDetailBySapIdSecAndBand by sapid,sec,band [{},{},{}]", sapid, sectorId, band);
		NetworkElementWrapper macroSiteDetail = null;
		try {
			Query query = getEntityManager().createNamedQuery("getMacroSiteDetailBySapIdSecAndBand");
			query.setParameter(ForesightConstants.NE_NAME, sapid);
			query.setParameter(ForesightConstants.SECTOR_SMALL, sectorId);
			query.setParameter(ForesightConstants.BAND, band);
			macroSiteDetail = (NetworkElementWrapper) query.getSingleResult();
		} catch (PersistenceException ex) {
			logger.error("Error while getting  MacroSiteDetail By SapId Sec And Band,err msg {}", ex);
		}
		return macroSiteDetail;
	}

	/**
	 * Gets the macro site detail bysap id.
	 *
	 * @param sapId
	 *            the sap id
	 * @return the macro site detail bysap id
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NetworkElementWrapper> getMacroSiteDetailBySapId(String sapId) {
		logger.info("Inside Method get MacroSiteDetail By SapId {}", sapId);
		List<NetworkElementWrapper> list = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getMacroSiteDetailBySapId");
			query.setParameter(ForesightConstants.SAPID_CAMEL, sapId);
			list = query.getResultList();
		} catch (IllegalStateException ise) {
			logger.error("IllegalStateException getting MacroSiteDetail By SapId ,err msg{}", ise);
		} catch (PersistenceException pe) {
			logger.error("PersistenceException getting MacroSiteDetail By SapId,err msg{}", pe);
		}
		logger.info("Returning MacroSiteDetail list size {}", list.size());
		return list;
	}

	/**
	 * Gets the site detail by site id sec and vendor.
	 *
	 * @param sapid
	 *            the sapid
	 * @param sectorId
	 *            the sector id
	 * @param band
	 *            the band
	 * @param vendor
	 *            the vendor
	 * @param technology
	 *            the technology
	 * @param domain
	 *            the domain
	 * @return the site detail by site id sec and vendor
	 */
	@Override
	public NetworkElementWrapper getMacroCellDetail(String sapid, Integer sectorId, String band, String vendor, String technology, String domain) {
		logger.info("Going to getMacroSiteDetailBysapIdSecAndBand by sapid,sec,band,vendor [{},{},{},{}]", sapid, sectorId, band, vendor);
		NetworkElementWrapper macroDiteDetail = null;
		try {
			Query query = getEntityManager().createNamedQuery("getSiteDetailBySiteIdSecAndVendor");
			query.setParameter(InfraConstants.NE_NENAME_KEY, sapid);
			query.setParameter(InfraConstants.SECTOR_KEY, sectorId);
			query.setParameter(ForesightConstants.BAND, band);
			query.setParameter(InfraConstants.VENDOR_KEY, Vendor.valueOf(vendor.toUpperCase()));
			query.setParameter(InfraConstants.NE_TECHNOLOGY_KEY, Technology.valueOf(technology.toUpperCase()));
			query.setParameter(InfraConstants.NE_DOMAIN_KEY, Domain.valueOf(domain.toUpperCase()));
			macroDiteDetail = (NetworkElementWrapper) query.getSingleResult();
		} catch (NoResultException ne) {
			logger.warn("NoResultException err msg {}", ne.getMessage());
		} catch (NonUniqueResultException nu) {
			logger.error("NonUniqueResultException err msg {}", nu.getMessage());
		} catch (IllegalArgumentException e) {
			logger.warn("IllegalArgumentException,err msg{}", e.getMessage());
		} catch (QueryTimeoutException e) {
			logger.warn("QueryTimeoutException ,err msg{}", e.getMessage());
		} catch (TransactionRequiredException e) {
			logger.warn("TransactionRequiredException ,err msg{}", e.getMessage());
		} catch (PersistenceException e) {
			logger.warn("PersistenceException ,err msg{}", e.getMessage());
		}
		return macroDiteDetail;
	}

	/**
	 * Gets site summary overview by band.
	 *
	 * @param neName
	 *            the ne name
	 * @param neFrequency
	 *            the ne frequency
	 * @param neStatus
	 *            the ne status
	 * @return the site summary overview by band
	 */
	/*@Override
	public SiteSummaryOverviewWrapper getSiteSummaryOverviewByBand(String neName, String neFrequency, String neStatus) {
		logger.info(" Going to get  Site Summary Overview data for neName {} for {} neFrequency ", neName, neFrequency);
		SiteSummaryOverviewWrapper siteSummaryOverviewWrapper = new SiteSummaryOverviewWrapper();
		try {
			Query query = getEntityManager().createQuery(getQuerySiteSummaryOverviewByBand());
			query.setParameter(InfraConstants.NE_NAME, neName.toUpperCase());
			query.setParameter(InfraConstants.NE_FREQUENCY, neFrequency);
			query.setParameter(InfraConstants.NE_STATUS, NEStatus.valueOf(neStatus.toUpperCase()));
			siteSummaryOverviewWrapper = (SiteSummaryOverviewWrapper) query.getSingleResult();
		} catch (NoResultException noResultException) {
			logger.error("No records  Found : {}", Utils.getStackTrace(noResultException));
		} catch (NonUniqueResultException nonUniqueResultException) {
			logger.error("No Unique  records Found  : {} ", Utils.getStackTrace(nonUniqueResultException));
		} catch (Exception exception) {
			logger.error(" Exception : {} " + Utils.getStackTrace(exception));
		}
		return siteSummaryOverviewWrapper;
	}*/

	/*@Override
	public SiteSummaryOverviewWrapper getSiteSummaryOverviewByBandForSecondCarrier(String neName, String neFrequency, String neStatus) {
		logger.info("Going to get Site Summary Overview data for neName {} for {} neFrequency ", neName, neFrequency);
		SiteSummaryOverviewWrapper siteSummaryOverviewWrapper = new SiteSummaryOverviewWrapper();
		try {
			Query query = getEntityManager().createQuery(getQuerySiteSummaryOverviewByBandForSecondCarrier());
			query.setParameter(InfraConstants.NE_NAME, neName.toUpperCase());
			query.setParameter(InfraConstants.NE_FREQUENCY, neFrequency);
			query.setParameter(InfraConstants.NE_STATUS, NEStatus.valueOf(neStatus.toUpperCase()));
			siteSummaryOverviewWrapper = (SiteSummaryOverviewWrapper) query.getSingleResult();
		} catch (NoResultException noResultException) {
			logger.error("No  records Found : {}", Utils.getStackTrace(noResultException));
		} catch (NonUniqueResultException nonUniqueResultException) {
			logger.error("No Unique  records Found : {} ", Utils.getStackTrace(nonUniqueResultException));
		} catch (Exception exception) {
			logger.error(" Exception :  {} " + Utils.getStackTrace(exception));
		}
		return siteSummaryOverviewWrapper;
	}*/
	
	/**
	 * Gets site summary overview by band.
	 *
	 * @param neName
	 *            the ne name
	 * @param neFrequency
	 *            the ne frequency
	 * @param neStatus
	 *            the ne status
	 * @return the site summary overview by band
	 *//*
	@Override
	public SiteSummaryOverviewWrapper getAntennaParametersByBand(String neName, String neFrequency, String neStatus) {
		logger.info("Going to get Antenna Parameters for neName {} for {} neFrequency", neName, neFrequency);
		SiteSummaryOverviewWrapper siteSummaryOverviewWrapper = new SiteSummaryOverviewWrapper();
		try {
			Query query = getEntityManager().createQuery(getQueryAntennaParametersByBand());
			query.setParameter(InfraConstants.NE_NAME, neName.toUpperCase());
			query.setParameter(InfraConstants.NE_FREQUENCY, neFrequency);
			query.setParameter(InfraConstants.NE_STATUS, NEStatus.valueOf(neStatus.toUpperCase()));
			siteSummaryOverviewWrapper = (SiteSummaryOverviewWrapper) query.getSingleResult();
		} catch (NoResultException noResultException) {
			logger.error("No records Found : {}", Utils.getStackTrace(noResultException));
		} catch (NonUniqueResultException nonUniqueResultException) {
			logger.error("No Unique  records Found : {}", Utils.getStackTrace(nonUniqueResultException));
		} catch (Exception exception) {
			logger.error("Exception : {} " + Utils.getStackTrace(exception));
		}
		return siteSummaryOverviewWrapper;
	}*/

	/**
	 * Gets site summary overview data by band.
	 *
	 * @return the query site summary overview by band
	 */
	private String getQuerySiteSummaryOverviewByBand() {

		return " select new com.inn.foresight.core.infra.wrapper.SiteSummaryOverviewWrapper( " 
				+ "max(case when m.sector=1   then m.networkElement.ecgi else null end) as alphaecgi,   "
				+ "max(case when m.sector=2   then m.networkElement.ecgi else null end) as betaecgi,"
				+ "max(case when m.sector=3   then m.networkElement.ecgi else null end)  as gammaecgi, "
				+ "max(case when m.sector=4   then m.networkElement.ecgi else null end) as alphaecgiadd,   "
				+ "max(case when m.sector=5  then m.networkElement.ecgi else null end) as betaecgiadd,"
				+ "max(case when m.sector=6  then m.networkElement.ecgi else null end)  as gammaecgiadd, "
		        + "max(case when m.sector=1   then m.pci else null end) as alphapci,"
				+ "max(case when m.sector=2   then m.pci else null end) as betapci," 
		        + "max(case when m.sector=3   then m.pci else null end) as gammapci,"
		        + "max(case when m.sector=4   then m.pci else null end) as alphapciadd,"
				+ "max(case when m.sector=5   then m.pci else null end) as betapciadd," 
		        + "max(case when m.sector=6   then m.pci else null end) as gammapciadd,"
				+ " max(case when m.sector=1 then m.sector else null end) as alphaSectorId, " 
		        + " max(case when m.sector=2 then m.sector else null end) as betaSectorId, "
				+ " max(case when m.sector=3 then m.sector else null end) as gammaSectorId,"
				+ " max(case when m.sector=4 then m.sector else null end) as alphaSectorIdadd, " 
		        + " max(case when m.sector=5 then m.sector else null end) as betaSectorIdadd, "
				+ " max(case when m.sector=6 then m.sector else null end) as gammaSectorIdadd,"
				+ " max(case when m.sector=1   then m.networkElement.cellNum else null end) as alphacellid,"
				+ " max(case when m.sector=2   then m.networkElement.cellNum else null end) as betacellid,"
				+ " max(case when m.sector=3   then m.networkElement.cellNum else null end) as gammacellid,"
				+ " max(case when m.sector=4   then m.networkElement.cellNum else null end) as alphacellidadd,"
				+ " max(case when m.sector=5   then m.networkElement.cellNum else null end) as betacellidadd,"
				+ " max(case when m.sector=6   then m.networkElement.cellNum else null end) as gammacellidadd,"
				+ " max(case when m.sector=1   then m.bandwidth else null end) as alphabandwidth,"
				+ " max(case when m.sector=2   then m.bandwidth else null end) as betabandwidth,"
				+ " max(case when m.sector=3   then m.bandwidth else null end) as gammabandwidth,"
				+ " max(case when m.sector=4   then m.bandwidth else null end) as alphabandwidthadd,"
				+ " max(case when m.sector=5   then m.bandwidth else null end) as betabandwidthadd,"
				+ " max(case when m.sector=6   then m.bandwidth else null end) as gammabandwidthadd )"
				+ " from RANDetail m where upper(m.networkElement.networkElement.neName)=:neName and m.networkElement.neFrequency=:neFrequency and  "
				+ "  upper(m.networkElement.neStatus)=:neStatus  and m.neBandDetail.carrier='FIRST' ";

	}

	/*private String getQuerySiteSummaryOverviewByBandForSecondCarrier() {

		return "select  new com.inn.foresight.core.infra.wrapper.SiteSummaryOverviewWrapper( " 
				+ "max(case when m.sector=1   then m.networkElement.ecgi else null end) as alphaecgiSecond,   "
				+ "max(case when m.sector=2   then m.networkElement.ecgi else null end) as betaecgiSecond,"
				+ "max(case when m.sector=3   then m.networkElement.ecgi else null end)  as gammaecgiSecond, "
				
		        + "max(case when m.sector=1   then m.pci else null end) as alphapciSecond,"
				+ "max(case when m.sector=2   then m.pci else null end) as betapciSecond," 
		        + "max(case when m.sector=3   then m.pci else null end) as gammapciSecond,"
		        + "max(case when m.sector=4   then m.pci else null end) as alphapciaddSecond,"
				+ "max(case when m.sector=5   then m.pci else null end) as betapciaddSecond," 
		        + "max(case when m.sector=6   then m.pci else null end) as gammapciaddSecond,"
				
		        + " max(case when m.sector=1 then m.sector else null end) as alphaSectorIdSecond, " 
		        + " max(case when m.sector=2 then m.sector else null end) as betaSectorIdSecond, "
				+ " max(case when m.sector=3 then m.sector else null end) as gammaSectorIdSecond,"
				+ " max(case when m.sector=4 then m.sector else null end) as alphaSectorIdaddSecond, " 
		        + " max(case when m.sector=5 then m.sector else null end) as betaSectorIdaddSecond, "
				+ " max(case when m.sector=6 then m.sector else null end) as gammaSectorIdaddSecond,"
				
				+ " max(case when m.sector=1   then m.networkElement.cellNum else null end) as alphacellidSecond,"
				+ " max(case when m.sector=2   then m.networkElement.cellNum else null end) as betacellidSecond,"
				+ " max(case when m.sector=3   then m.networkElement.cellNum else null end) as gammacellidSecond,"
				+ " max(case when m.sector=4   then m.networkElement.cellNum else null end) as alphacellidaddSecond,"
				+ " max(case when m.sector=5   then m.networkElement.cellNum else null end) as betacellidaddSecond,"
				+ " max(case when m.sector=6   then m.networkElement.cellNum else null end) as gammacellidaddSecond,"

				+ " max(case when m.sector=1   then m.bandwidth else null end) as alphabandwidthSecond,"
				+ " max(case when m.sector=2   then m.bandwidth else null end) as betabandwidthSecond,"
				+ " max(case when m.sector=3   then m.bandwidth else null end) as gammabandwidthSecond,"
				+ " max(case when m.sector=4   then m.bandwidth else null end) as alphabandwidthaddSecond,"
				+ " max(case when m.sector=5   then m.bandwidth else null end) as betabandwidthaddSecond,"
				+ " max(case when m.sector=6   then m.bandwidth else null end) as gammabandwidthaddSecond,"
				+ " max(case when m.sector=4   then m.networkElement.ecgi else null end) as alphaecgiaddSecond,"
				+ " max(case when m.sector=5  then m.networkElement.ecgi else null end) as betaecgiaddSecond,"
				+ " max(case when m.sector=6  then m.networkElement.ecgi else null end)  as gammaecgiaddSecond )"
				
				+ " from RANDetail m where upper(m.networkElement.networkElement.neName)=:neName and m.networkElement.neFrequency=:neFrequency and  "
				+ "  upper(m.networkElement.neStatus)=:neStatus and m.networkElement.networkElement is not null and m.neBandDetail.carrier='SECOND' ";

	}*/

	
	/**
	 * Gets site summary overview data by band.
	 *
	 * @return the query site summary overview by band
	 */
	/*private String getQueryAntennaParametersByBand() {

		return "select new com.inn.foresight.core.infra.wrapper.SiteSummaryOverviewWrapper( " 
		        + " max(case when m.sector=1 then m.antennaType else null end) as alphaantennaType, "
				+ " max(case when m.sector=2 then m.antennaType else null end) as betaantennaType,  " 
		        + " max(case when m.sector=3 then m.antennaType else null end) as gammaantennaType,"
		        + " max(case when m.sector=4 then m.antennaType else null end) as alphaantennaTypeadd, "
				+ " max(case when m.sector=5 then m.antennaType else null end) as betaantennaTypeadd,  " 
		        + " max(case when m.sector=6 then m.antennaType else null end) as gammaantennaTypeadd,"
				+ " max(case when m.sector=1 then m.elecTilt else null end) as alphaElecTilt, " 
		        + " max(case when m.sector=2 then m.elecTilt else null end) as betaElecTilt, "
				+ " max(case when m.sector=3 then m.elecTilt else null end) as gammaElecTilt, " 
				+ " max(case when m.sector=4 then m.elecTilt else null end) as alphaElecTiltadd, " 
		        + " max(case when m.sector=5 then m.elecTilt else null end) as betaElecTiltadd, "
				+ " max(case when m.sector=6 then m.elecTilt else null end) as gammaElecTiltadd, " 
		        + " max(case when m.sector=1 then m.mechTilt else null end) as alphaMechTilt, "
				+ " max(case when m.sector=2 then m.mechTilt else null end) as betaMechTilt, "
		        + " max(case when m.sector=3 then m.mechTilt else null end) as gammaMechTilt, "
		        + " max(case when m.sector=4 then m.mechTilt else null end) as alphaMechTiltadd, "
				+ " max(case when m.sector=5 then m.mechTilt else null end) as betaMechTiltadd, "
		        + " max(case when m.sector=6 then m.mechTilt else null end) as gammaMechTiltadd, "
		        + " max(case when m.sector=1 then ((case when m.elecTilt is  null then 0  else m.elecTilt end) + (case when m.mechTilt is null then 0 else m.mechTilt end)) else null end) as alphaTotalTilt, "
				+ " max(case when m.sector=2 then ((case when m.elecTilt is  null then 0  else m.elecTilt end) + (case when m.mechTilt is null then 0 else m.mechTilt end)) else null end) as betaTotalTilt, "
		        + " max(case when m.sector=3 then ((case when m.elecTilt is  null then 0  else m.elecTilt end) + (case when m.mechTilt is null then 0 else m.mechTilt end)) else null end) as gammaTotalTilt, "
		        + " max(case when m.sector=4 then ((case when m.elecTilt is  null then 0  else m.elecTilt end) + (case when m.mechTilt is null then 0 else m.mechTilt end)) else null end) as alphaTotalTiltadd, "
				+ " max(case when m.sector=5 then ((case when m.elecTilt is  null then 0  else m.elecTilt end) + (case when m.mechTilt is null then 0 else m.mechTilt end)) else null end) as betaTotalTiltadd, "
		        + " max(case when m.sector=6 then ((case when m.elecTilt is  null then 0  else m.elecTilt end) + (case when m.mechTilt is null then 0 else m.mechTilt end)) else null end) as gammaTotalTiltadd, "
		        
		        + " max(case when m.sector=1 then m.antennaGain else null end) as alphaAntennaGain, "
				+ " max(case when m.sector=2 then m.antennaGain else null end) as betaAntennaGain, "
		        + " max(case when m.sector=3 then m.antennaGain else null end) as gammaAntennaGain, "
		        + " max(case when m.sector=4 then m.antennaGain else null end) as alphaAntennaGainadd, "
				+ " max(case when m.sector=5 then m.antennaGain else null end) as betaAntennaGainadd, "
		        + " max(case when m.sector=6 then m.antennaGain else null end) as gammaAntennaGainadd, "
		      
		        + " max(case when m.sector=1 then m.antennaVendor else null end) as alphaAntennaVendorName, "
				+ " max(case when m.sector=2 then m.antennaVendor else null end) as betaAntennaVendorName, "
		        + " max(case when m.sector=3 then m.antennaVendor else null end) as gammaAntennaVendorName, "
		        + " max(case when m.sector=4 then m.antennaVendor else null end) as alphaAntennaVendorNameadd, "
				+ " max(case when m.sector=5 then m.antennaVendor else null end) as betaAntennaVendorNameadd, "
		        + " max(case when m.sector=6 then m.antennaVendor else null end) as gammaAntennaVendorNameadd, "
				
				+ " max(case when m.sector=1 then m.azimuth else null end) as alphaAzimuth, " 
		        + " max(case when m.sector=2 then m.azimuth else null end) as betaAzimuth, "
				+ " max(case when m.sector=3 then m.azimuth else null end) as gammaAzimuth, "
				+ " max(case when m.sector=4 then m.azimuth else null end) as alphaAzimuth, " 
		        + " max(case when m.sector=5 then m.azimuth else null end) as betaAzimuth, "
				+ " max(case when m.sector=6 then m.azimuth else null end) as gammaAzimuth, "
		        
		        + " max(case when m.sector=1 then m.antennaHeight else null end) as alphaAntennaHeight, "
				+ " max(case when m.sector=2 then m.antennaHeight else null end) as betaAntennaHeight, " 
		        + " max(case when m.sector=3 then m.antennaHeight else null end) as gammaAntennaHeight, "
		        + " max(case when m.sector=4 then m.antennaHeight else null end) as alphaAntennaHeightadd, "
				+ " max(case when m.sector=5 then m.antennaHeight else null end) as betaAntennaHeightadd, " 
		        + " max(case when m.sector=6 then m.antennaHeight else null end) as gammaAntennaHeightadd, "
		        
		        + " max(case when m.sector=1 then m.horizontalBeamWidth else null end) as alphaHorizontalBeamWidth, "
				+ " max(case when m.sector=2 then m.horizontalBeamWidth else null end) as betaHorizontalBeamWidth, " 
		        + " max(case when m.sector=3 then m.horizontalBeamWidth else null end) as gammaHorizontalBeamWidth, "
		        + " max(case when m.sector=4 then m.horizontalBeamWidth else null end) as alphaHorizontalBeamWidthadd, "
				+ " max(case when m.sector=5 then m.horizontalBeamWidth else null end) as betaHorizontalBeamWidthadd, " 
		        + " max(case when m.sector=6 then m.horizontalBeamWidth else null end) as gammaHorizontalBeamWidthadd, "
		       
		        + " max(case when m.sector=1 then m.VerticalBeamWidth else null end) as alphaVerticalBeamWidth, "
				+ " max(case when m.sector=2 then m.VerticalBeamWidth else null end) as betaVerticalBeamWidth, " 
		        + " max(case when m.sector=3 then m.VerticalBeamWidth else null end) as gammaVerticalBeamWidth, "
		        + " max(case when m.sector=4 then m.VerticalBeamWidth else null end) as alphaVerticalBeamWidthadd, "
				+ " max(case when m.sector=5 then m.VerticalBeamWidth else null end) as betaVerticalBeamWidthadd, " 
		        + " max(case when m.sector=6 then m.VerticalBeamWidth else null end) as gammaVerticalBeamWidthadd, "
				
		        
		        + " max(case when m.sector=1 then m.feederCableLength else null end) as alphaFeederCableLength, "
				+ " max(case when m.sector=2 then m.feederCableLength else null end) as betaFeederCableLength, " 
		        + " max(case when m.sector=3 then m.feederCableLength else null end) as gammaFeederCableLength, "
		        + " max(case when m.sector=4 then m.feederCableLength else null end) as alphaFeederCableLengthadd, "
				+ " max(case when m.sector=5 then m.feederCableLength else null end) as betaFeederCableLengthadd, " 
		        + " max(case when m.sector=6 then m.feederCableLength else null end) as gammaFeederCableLengthadd, "
		       
		        + " max(case when m.sector=1 then m.opticCableLength else null end) as alphaOpticCableLength, "
				+ " max(case when m.sector=2 then m.opticCableLength else null end) as betaOpticCableLength, " 
		        + " max(case when m.sector=3 then m.opticCableLength else null end) as gammaOpticCableLength, "
		        + " max(case when m.sector=4 then m.opticCableLength else null end) as alphaOpticCableLengthadd, "
				+ " max(case when m.sector=5 then m.opticCableLength else null end) as betaOpticCableLengthadd, " 
		        + " max(case when m.sector=6 then m.opticCableLength else null end) as gammaOpticCableLengthadd, "
		        
		        
				+ " max(case when m.sector=1 then m.sector else null end) as alphaSectorId, " 
		        + " max(case when m.sector=2 then m.sector else null end) as betaSectorId, "
				+ " max(case when m.sector=3 then m.sector else null end) as gammaSectorId,"
				+ " max(case when m.sector=4 then m.sector else null end) as alphaSectorIdadd,"
				+ " max(case when m.sector=5 then m.sector else null end) as betaSectorIdadd,"
				+ " max(case when m.sector=6 then m.sector else null end) as gammaSectorIdadd ) "
				+ " from RANDetail m where upper(m.networkElement.networkElement.neName)=:neName and m.networkElement.neFrequency=:neFrequency and "
				+ "  upper(m.networkElement.neStatus)=:neStatus and m.networkElement.networkElement is not null and m.neBandDetail.carrier='FIRST'";

	}*/

	/**
	 * Gets the NE by sap ids.
	 *
	 * @param sapIds
	 *            the sap ids
	 * @return the NE by sap ids
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MacroSiteDetail> getNEBySapIds(List<String> sapIds) {
		logger.info("Going to get NE By SapIds {}", sapIds);
		List<MacroSiteDetail> neElements = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getNEBySapIds").setParameter(ForesightConstants.SAPIDS, sapIds);
			neElements = query.getResultList();
		} catch (IllegalStateException | QueryTimeoutException | TransactionRequiredException e) {
			logger.warn("Error while get NE By sapIds,err msg{}",e.toString());
		} 
		logger.info("Returning get NECells By sapIds list size {}", neElements.size());
		return neElements;
	}

	/**
	 * Gets the NE cells by sap ids.
	 *
	 * @param sapIds
	 *            the sap ids
	 * @return the NE cells by sap ids
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SiteDataWrapper> getNECellsBySapIds(List<String> sapIds) {
		logger.info("Going to get NECells By SapIds {}", sapIds);
		List<SiteDataWrapper> neElements = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getNECellsBySapids").setParameter(ForesightConstants.SAPIDS, sapIds);
			neElements = query.getResultList();
		} catch (IllegalStateException | PersistenceException e) {
			logger.warn("Error while get NECells By sapIds,err msg {}", e);
		} 
		logger.info("Returning get NECells By sapIds list size {}", neElements.size());
		return neElements;
	}
	
	@Override
	public Long countAllSites(String siteName,List<Integer> cellIds, List<Integer> sectors,List<String> band, String vendor, String neType) {
		Long resultCount = 0L;
		try {
//			Query query = getEntityManager().createNamedQuery("searchAllSites");
			Query query = getQueryForSiteCountByFilter(siteName, cellIds, sectors, band, vendor, neType);
			if(siteName != null && !siteName.isEmpty()) {
				query.setParameter(InfraConstants.SITE_NAME, "%" + siteName.trim().toUpperCase() + "%");
			} else {
				query.setParameter(InfraConstants.SITE_NAME,"%" + "" + "%");
			}
			if(cellIds != null && !cellIds.isEmpty()) {
				query.setParameter(InfraConstants.NE_CELLID_KEY, cellIds);
			}
			if(sectors != null && !sectors.isEmpty()) {
				query.setParameter(InfraConstants.SECTOR_KEY, sectors);
			}
			if(band != null && !band.isEmpty()) {
				query.setParameter(InfraConstants.NE_NEFREQUENCY_KEY, band);
			}
			if(vendor != null && !vendor.isEmpty()) {
				query.setParameter(InfraConstants.VENDOR_KEY, Vendor.valueOf(vendor));
			}
			if(neType != null && !neType.isEmpty()) {
				query.setParameter(InfraConstants.NE_NETYPE_KEY, NEType.valueOf(neType));
			}
			if(query.getResultList().size() > 0) {
				resultCount = (Long)query.getSingleResult();
			}
		} catch (Exception e) {
			logger.error("Error while getSiteNameBySearch,err msg{}",  Utils.getStackTrace(e));
		}
		return resultCount;
	}

	/**
	 * Search all sites.
	 *
	 * @param siteName
	 *            the site name
	 * @param llimit
	 *            the llimit
	 * @param ulimit
	 *            the ulimit
	 * @return the list
	 */
	@Override
	public List<Object[]> searchAllSites(String siteName, Integer llimit, Integer ulimit, List<Integer> cellIds, List<Integer> sectors,List<String> band, String vendor, String neType, String orderByKey, String order) {
		List<Object[]> result = new ArrayList<>();
		try {
//			Query query = getEntityManager().createNamedQuery("searchAllSites");
			Query query = getQueryForSiteSearchByFilter(siteName, cellIds, sectors, band, vendor, neType, orderByKey, order);
			if(siteName != null && !siteName.isEmpty()) {
				query.setParameter(InfraConstants.SITE_NAME, "%" + siteName.trim().toUpperCase() + "%");
			} else {
				query.setParameter(InfraConstants.SITE_NAME,"%" + "" + "%");
			}
			if(cellIds != null && !cellIds.isEmpty()) {
				query.setParameter(InfraConstants.CELLID_KEY, cellIds);
			}
			if(sectors != null && !sectors.isEmpty()) {
				query.setParameter(InfraConstants.SECTOR_KEY, sectors);
			}
			if(band != null && !band.isEmpty()) {
				query.setParameter(InfraConstants.NE_NEFREQUENCY_KEY, band);
			}
			if(vendor != null && !vendor.isEmpty()) {
				query.setParameter(InfraConstants.VENDOR_KEY, Vendor.valueOf(vendor));
			}
			if(neType != null && !neType.isEmpty()) {
				query.setParameter(InfraConstants.NE_NETYPE_KEY, NEType.valueOf(neType));
			}
			query.setFirstResult(llimit);
			query.setMaxResults(ulimit - llimit + 1);
			result = query.getResultList();
		} catch (Exception e) {
			logger.error("Error while getSiteNameBySearch,err msg{}",  Utils.getStackTrace(e));
		}
		return result;
	}
	
	private Query getQueryForSiteSearchByFilter(String siteName,
			List<Integer> cellIds, List<Integer> sectors,
			List<String> band, String vendor, String neType, String orderByKey, String order) {
		Query query;
		String queryString = "";
			logger.info("In IF .....");
			queryString = "select m.networkElement.networkElement.neName,m.cellId,m.networkElement.neFrequency,m.networkElement.vendor,m.networkElement.neType,m.sector,m.networkElement.networkElement.neName,m.networkElement.domain,m.networkElement.technology from MacroSiteDetail m where m.networkElement.networkElement.neName like :siteName" + 
					changeQueryForParamForCellIdAndSector(InfraConstants.CELLID_KEY, cellIds) + changeQueryForParamForCellIdAndSector(InfraConstants.SECTOR_KEY, sectors) + changeQueryForParamForNeFrequency(InfraConstants.NE_NEFREQUENCY_KEY, band) + changeQueryForParam(InfraConstants.VENDOR_KEY, vendor) + changeQueryForParam(InfraConstants.NE_NETYPE_KEY, neType) + getOrderKey(orderByKey, order);
			query = getEntityManager().createQuery(queryString);
			logger.info("getQueryForSiteSearchByFilter:{}", queryString);
		return query;
	}
	
	private Query getQueryForSiteCountByFilter(String siteName,
			List<Integer> cellIds, List<Integer> sectors,
			List<String> band, String vendor, String neType) {
		Query query;
		String queryString = "";
			logger.info("In IF .....");
			queryString = "select count(m.networkElement.networkElement.neName) from MacroSiteDetail m where m.networkElement.networkElement.neName like :siteName" + 
					changeQueryForParamForCellIdAndSector(InfraConstants.CELLID_KEY, cellIds) + changeQueryForParamForCellIdAndSector(InfraConstants.SECTOR_KEY, sectors) + changeQueryForParamForNeFrequency(InfraConstants.NE_NEFREQUENCY_KEY, band) + changeQueryForParam(InfraConstants.VENDOR_KEY, vendor) + changeQueryForParam(InfraConstants.NE_NETYPE_KEY, neType);
			query = getEntityManager().createQuery(queryString);
			logger.info("getQueryForSiteSearchByFilter:{}", queryString);
		return query;
	}
	
	public String changeQueryForParamForCellIdAndSector(String queryParam, List<Integer> paramValue){
		String key = "";
	  	logger.info("queryParam  {} ",queryParam);
	  	if (paramValue != null && !paramValue.isEmpty()) {
	  		key = " and m." + queryParam + " IN(:"+queryParam + ")";
		}
	  	return key;
		
	}
	
	public String changeQueryForParamForNeFrequency(String queryParam, List<String> paramValue){
		String key = "";
	  	logger.info("queryParam {} ",queryParam);
	  	if (paramValue != null && !paramValue.isEmpty()) {
	  		key = " and m.networkElement." + queryParam + " IN(:"+queryParam + ")";
			}
	  	return key;
		
	}
	
	public String changeQueryForParam(String queryParam, String paramValue){
		String key = "";
	  	logger.info("queryParam {} ",queryParam);
	  	if (paramValue != null && !paramValue.equalsIgnoreCase("")) {
	  		key = " and m.networkElement." + queryParam + "=:"+queryParam;
			}/*else{
				key = " and m.networkElement." + queryParam + " like :"+queryParam ;
			}*/
	  	return key;
		
	}
	
	public String getOrderKey(String orderByKey, String order){
	  	String key = "";
	  	logger.info("orderByKey {} and order {}",orderByKey,order);
	  	if (orderByKey != null && !orderByKey.equalsIgnoreCase("")) {
		  		if(orderByKey.equalsIgnoreCase(InfraConstants.NE_NENAME_KEY)) {
					key = "" +" order by m.networkElement.networkElement."+orderByKey ;
		  		} else if(orderByKey.equalsIgnoreCase(InfraConstants.CELLID_KEY)) {
		  			key = "" +" order by m."+orderByKey ;
		  		} else if(orderByKey.equalsIgnoreCase(InfraConstants.SECTOR_KEY)) {
		  			key = "" +" order by m."+orderByKey ;
		  		} else if(orderByKey.equalsIgnoreCase(InfraConstants.NE_NEFREQUENCY_KEY)) {
		  			key = "" +" order by m.networkElement. "+orderByKey ;
		  		} else if(orderByKey.equalsIgnoreCase(InfraConstants.VENDOR_KEY)) {
		  			key = "" +" order by m.networkElement."+orderByKey ;
		  		} else if(orderByKey.equalsIgnoreCase(InfraConstants.NE_NETYPE_KEY)) {
		  			key = "" +" order by m.networkElement."+orderByKey ;
		  		} else {
		  			key = "" +" order by m.networkElement.networkElement."+orderByKey ;
		  		}
				if(order != null && !order.equalsIgnoreCase("")){
					key +=" "+order;
				}else{
					key +=" "+"desc";
				}
			}else{
				key = " order by m.networkElement.networkElement.neName desc";
			}
	  	return key;
	  }

	/*@Override
	public List<MacroSiteDetail> getSectorPropertyData(String neName, String neFrequency, String neStatus) {
		logger.info("Going to Get sector info Data for neName {}", neName);
		List<MacroSiteDetail> macroSiteDetailDataList = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getSectorPropertyData");
			query.setParameter(InfraConstants.NE_NAME, neName.toUpperCase());
			query.setParameter(InfraConstants.NE_FREQUENCY, neFrequency);
			query.setParameter(InfraConstants.NE_STATUS, NEStatus.valueOf(neStatus.toUpperCase()));
			macroSiteDetailDataList = query.getResultList();
		} catch (NoResultException noResultException) {
			logger.error("No records Found from NetworkElement. {}", Utils.getStackTrace(noResultException));
		} catch (NonUniqueResultException nonUniqueResultException) {
			logger.error("No Unique records Found for NetworkElement. {}", Utils.getStackTrace(nonUniqueResultException));
		} catch (Exception exception) {
			logger.error("Exception in getting data for NetworkElement. : {}" + Utils.getStackTrace(exception));
		}
		return macroSiteDetailDataList;
	}*/

	@Override
	public List<String> getCellList(String geographyLevel,String geographyL4,Vendor vendor) {
		logger.info("Going to get dget CellList by vendor {} and geographyL4 {}",vendor,geographyL4);
		List<String> resultList=new ArrayList<>();
		try {
			Query query=null;
			if(geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL4))
			{
				query=getEntityManager().createNamedQuery("getCellListByGeogrphyL4").setParameter(InfraConstants.GEOGRAPHYNAME, geographyL4).setParameter(InfraConstants.VENDOR_KEY, vendor);
			}
			else if(geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL3))
			{
				query=getEntityManager().createNamedQuery("getCellListByGeogrphyL3").setParameter(InfraConstants.GEOGRAPHYNAME, geographyL4).setParameter(InfraConstants.VENDOR_KEY, vendor);
				
			}
			else if(geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL2))
			{
				query=getEntityManager().createNamedQuery("getCellListByGeogrphyL2").setParameter(InfraConstants.GEOGRAPHYNAME, geographyL4).setParameter(InfraConstants.VENDOR_KEY, vendor);
				
			}
			else if(geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL1))
			{
				query=getEntityManager().createNamedQuery("getCellListByGeogrphyL1").setParameter(InfraConstants.GEOGRAPHYNAME, geographyL4).setParameter(InfraConstants.VENDOR_KEY, vendor);
				
			}
			else if(geographyLevel.equalsIgnoreCase(ForesightConstants.PAN_INDIA_NO_SPACE))
			{
				query=getEntityManager().createNamedQuery("getCellListByGeogrphyPan").setParameter(InfraConstants.VENDOR_KEY, vendor);
				
			}
			resultList=query.getResultList();
		} catch(IllegalStateException | QueryTimeoutException | TransactionRequiredException e){
			logger.warn("Exception while getCellList  From NetworkElement, err  msg {}",e.getMessage());
		} 
		return resultList;
	}
	
	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	
	public List<MacroSiteDetail> getMacroSiteDetailsForCellLevel(List<NEType> neTypeList, List<String> neNameList, List<String> neFrequencyList, List<NEStatus> neStatusList, List<Vendor> vendorList,List<Technology> technologyList,List<Domain> domainList,Map<String, List<String>> geographyNames,List<String> neIdList) {
		logger.info("Going to get MacroSiteDetails SiteLevel data for neType : {} , neFrequency : {} , neStatus: {}, vendor : {}, technology : {},domain :{}", neTypeList, neFrequencyList, neStatusList, vendorList,technologyList,domainList);
		try {
			neNameList = InfraUtils.convertListElements(neNameList, InfraConstants.LIST_TOUPPERCASE);
			neFrequencyList = InfraUtils.convertListElements(neFrequencyList, InfraConstants.LIST_TOUPPERCASE);
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<MacroSiteDetail> criteriaQuery = criteriaBuilder.createQuery(MacroSiteDetail.class);
			Root<MacroSiteDetail> macroSiteDetail = criteriaQuery.from(MacroSiteDetail.class);
			Join<NetworkElement, MacroSiteDetail> networkElementJoin = macroSiteDetail.join(InfraConstants.NE_NETWORKELEMENT_KEY, JoinType.INNER);
			List<Predicate> predicates = InfraUtils.getPredicatesForCBForMacroSiteDetail(criteriaBuilder, networkElementJoin, neTypeList, neNameList, neFrequencyList, neStatusList, vendorList,technologyList,domainList,geographyNames,neIdList);
			predicates.add(networkElementJoin.get(InfraConstants.NE_NETWORKELEMENT_KEY).isNotNull());
			criteriaQuery.select(macroSiteDetail).where(predicates.toArray(new Predicate[] {}));
			//criteriaQuery.orderBy(criteriaBuilder.asc(networkElementJoin.get(InfraConstants.NE_NENAME_KEY)));
			if(neNameList!=null && neNameList.size()>0){
				criteriaQuery.orderBy(criteriaBuilder.asc(networkElementJoin.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NENAME_KEY)));
			}
			Query query = getEntityManager().createQuery(criteriaQuery);
			return query.getResultList();
		} catch (NullPointerException nullPointerException) {
			logger.error(" NullPointerException caught while getting Sites data from MacroSiteDetail Exception : {}", Utils.getStackTrace(nullPointerException));
			throw new DaoException("Unable  to  get MacroSiteDetail data for specific parameters.");
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error("IllegalArgumentException  caught while getting Sites data from MacroSiteDetail Exception : {}", Utils.getStackTrace(illegalArgumentException));
			throw new DaoException("Unable to get MacroSiteDetail  data for specific parameters.");
		} catch (NoResultException noResultException) {
			logger.error("NoResultException caught while  getting Sites data from MacroSiteDetail Exception : {}", Utils.getStackTrace(noResultException));
			throw new DaoException("Unable to  get MacroSiteDetail data for specific parameters.");
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while getting  Sites data from MacroSiteDetail Exception : {}", Utils.getStackTrace(persistenceException));
			throw new DaoException("Unable to get MacroSiteDetail  data for specific  parameters.");
		} catch (Exception exception) {
			logger.error(" Exception caught while getting Sites data from MacroSiteDetail Exception : {}", Utils.getStackTrace(exception));
			throw new DaoException("Unable to get MacroSiteDetail  data  for  specific parameters.");
		}
	}
	
	@Override
	public SiteSummaryOverviewWrapper getRadioParametersByBand(String neName, String neFrequency, String neStatus) {
		logger.info("Going to get Site Summary Overview data for neName {} for {} neFrequency ", neName, neFrequency);
		SiteSummaryOverviewWrapper siteSummaryOverviewWrapper = new SiteSummaryOverviewWrapper();
		try {
			Query query = getEntityManager().createQuery(getQueryRadioParametersByBand());
			query.setParameter(InfraConstants.NE_NAME, neName.toUpperCase());
			query.setParameter(InfraConstants.NE_FREQUENCY, neFrequency);
			query.setParameter(InfraConstants.NE_STATUS, NEStatus.valueOf(neStatus.toUpperCase()));
			siteSummaryOverviewWrapper = (SiteSummaryOverviewWrapper) query.getSingleResult();
		} catch (NoResultException noResultException) {
			logger.error("No records Found : {}", Utils.getStackTrace(noResultException));
		} catch (NonUniqueResultException nonUniqueResultException) {
			logger.error("No Unique  records Found : {} ", Utils.getStackTrace(nonUniqueResultException));
		} catch (Exception exception) {
			logger.error("Exception : {} " + Utils.getStackTrace(exception));
		}
		return siteSummaryOverviewWrapper;
	}
	
	private String getQueryRadioParametersByBand() {

		return "select new com.inn.foresight.core.infra.wrapper.SiteSummaryOverviewWrapper( " 
		        + "max(case when m.sector=1   then m.clutterCategory else null end) as alphaclutterCategory,"
				+ "max(case when m.sector=2   then m.clutterCategory else null end) as betaclutterCategory," 
		        + "max(case when m.sector=3   then m.clutterCategory else null end) as gammaclutterCategory,"
		        + "max(case when m.sector=4   then m.clutterCategory else null end) as alphaclutterCategoryadd,"
				+ "max(case when m.sector=5   then m.clutterCategory else null end) as betaclutterCategoryadd," 
		        + "max(case when m.sector=6   then m.clutterCategory else null end) as gammaclutterCategoryadd,"
				
				+ " max(case when m.sector=1 then m.propagationModel else null end) as alphapropagationModel, " 
		        + " max(case when m.sector=2 then m.propagationModel else null end) as betapropagationModel, "
				+ " max(case when m.sector=3 then m.propagationModel else null end) as gammapropagationModel,"
				+ " max(case when m.sector=4 then m.propagationModel else null end) as alphapropagationModeladd, " 
		        + " max(case when m.sector=5 then m.propagationModel else null end) as betapropagationModeladd, "
				+ " max(case when m.sector=6 then m.propagationModel else null end) as gammapropagationModeladd,"
		        
				+ " max(case when m.sector=1   then m.txPower else null end) as alphatxPower,"
				+ " max(case when m.sector=2   then m.txPower else null end) as betatxPower,"
				+ " max(case when m.sector=3   then m.txPower else null end) as gammatxPower,"
				+ " max(case when m.sector=4   then m.txPower else null end) as alphatxPoweradd,"
				+ " max(case when m.sector=5   then m.txPower else null end) as betatxPoweradd,"
				+ " max(case when m.sector=6   then m.txPower else null end) as gammatxPoweradd,"
				
				+ " max(case when m.sector=1   then m.eirp else null end) as alphaeirp,"
				+ " max(case when m.sector=2   then m.eirp else null end) as betaeirp,"
				+ " max(case when m.sector=3   then m.eirp else null end) as gammaeirp,"
				+ " max(case when m.sector=4  then m.eirp else null end) as alphaeirpadd,"
				+ " max(case when m.sector=5   then m.eirp else null end) as betaeirpadd,"
				+ " max(case when m.sector=6   then m.eirp else null end) as gammaeirpadd,"
				
				+ " max(case when m.sector=1   then m.pilotChannel else null end) as alphapilotChannelTxPower,"
				+ " max(case when m.sector=2   then m.pilotChannel else null end) as betapilotChannelTxPower,"
				+ " max(case when m.sector=3   then m.pilotChannel else null end) as gammapilotChannelTxPower,"
				+ " max(case when m.sector=4   then m.pilotChannel else null end) as alphapilotChannelTxPoweradd,"
				+ " max(case when m.sector=5   then m.pilotChannel else null end) as betapilotChannelTxPoweradd,"
				+ " max(case when m.sector=6   then m.pilotChannel else null end) as gammapilotChannelTxPoweradd,"
				
				+ " max(case when m.sector=1   then m.radiusThreshold else null end) as alpharadiusThreshold,"
				+ " max(case when m.sector=2   then m.radiusThreshold else null end) as betaradiusThreshold,"
				+ " max(case when m.sector=3   then m.radiusThreshold else null end) as gammaradiusThreshold,"
				+ " max(case when m.sector=4   then m.radiusThreshold else null end) as alpharadiusThresholdadd,"
				+ " max(case when m.sector=5   then m.radiusThreshold else null end) as betaradiusThresholdadd,"
				+ " max(case when m.sector=6   then m.radiusThreshold else null end) as gammaradiusThresholdadd,"
				
				+ " max(case when m.sector=1   then m.rsrpThreshold else null end) as alpharsrpThreshold,"
				+ " max(case when m.sector=2   then m.rsrpThreshold else null end) as betarsrpThreshold,"
				+ " max(case when m.sector=3   then m.rsrpThreshold else null end) as gammarsrpThreshold,"
				+ " max(case when m.sector=4   then m.rsrpThreshold else null end) as alpharsrpThresholdadd,"
				+ " max(case when m.sector=5   then m.rsrpThreshold else null end) as betarsrpThresholdadd,"
				+ " max(case when m.sector=6   then m.rsrpThreshold else null end) as gammarsrpThresholdadd,"
				
				+ " max(case when m.sector=1   then m.baseChannelFreq else null end) as alphabaseChannelFreq,"
				+ " max(case when m.sector=2   then m.baseChannelFreq else null end) as betabaseChannelFreq,"
				+ " max(case when m.sector=3   then m.baseChannelFreq else null end) as gammabaseChannelFreq,"
				+ " max(case when m.sector=4   then m.baseChannelFreq else null end) as alphabaseChannelFreqadd,"
				+ " max(case when m.sector=5   then m.baseChannelFreq else null end) as betabaseChannelFreqadd,"
				+ " max(case when m.sector=6   then m.baseChannelFreq else null end) as gammabaseChannelFreqadd,"
				
				+ " max(case when m.sector=1   then m.carrier else null end) as alphaSFCarrier,"
				+ " max(case when m.sector=2   then m.carrier else null end) as betaSFCarrier,"
				+ " max(case when m.sector=3   then m.carrier else null end) as gammaSFCarrier,"
				+ " max(case when m.sector=4   then m.carrier else null end) as alphaSFCarrieradd,"
				+ " max(case when m.sector=5   then m.carrier else null end) as betaSFCarrieradd,"
				+ " max(case when m.sector=6   then m.carrier else null end) as gammaSFCarrieradd)"
				+ " from MacroSiteDetail m where upper(m.networkElement.networkElement.neName)=:neName and m.networkElement.neFrequency=:neFrequency and "
				+ "  upper(m.networkElement.neStatus)=:neStatus and m.carrier='FIRST'";
	}
	
	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public List<MacroSiteDetail> getMacroSiteDetailByMncMccCellId(Integer mcc, Integer mnc, String cellId) {
		logger.info("Into the function getDataByMncMccCellId for mcc {} mnc {} cellid {}", mcc, mnc, cellId);
		NEType neType = NEType.MACRO_CELL;
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<MacroSiteDetail> criteriaQuery = criteriaBuilder.createQuery(MacroSiteDetail.class);
			Root<MacroSiteDetail> macroSiteDetail = criteriaQuery.from(MacroSiteDetail.class);
			Join<NetworkElement, MacroSiteDetail> networkElementJoin = macroSiteDetail.join(InfraConstants.NE_NETWORKELEMENT_KEY, JoinType.INNER);
			List<Predicate> criteriaList = new ArrayList<>();
			if (mcc != null) {
				Predicate firstCondition = criteriaBuilder.equal(networkElementJoin.get(InfraConstants.NE_MCC_KEY), mcc);
				criteriaList.add(firstCondition);
			}
			if (mnc != null) {
				Predicate secondCondition = criteriaBuilder.equal(networkElementJoin.get(InfraConstants.NE_MNC_KEY), mnc);
				criteriaList.add(secondCondition);
			}
			if (cellId != null) {
				Predicate thirdCondition = criteriaBuilder.equal(macroSiteDetail.get(InfraConstants.NE_CELLID_KEY), cellId);
				criteriaList.add(thirdCondition);
			}
			Predicate fourthCondition = criteriaBuilder.equal(networkElementJoin.get(InfraConstants.NE_NETYPE_KEY), neType);
			criteriaList.add(fourthCondition);

			if (criteriaList != null && criteriaList.size() > 0)
				criteriaQuery.where(criteriaBuilder.and(criteriaList.toArray(new Predicate[0])));

			Query query = getEntityManager().createQuery(criteriaQuery);
			return query.getResultList();
		} catch (NullPointerException nullPointerException) {
			logger.error("NullPointerException caught while getting Sites data from MacroSiteDetail Exception : {}", Utils.getStackTrace(nullPointerException));
			throw new DaoException("Unable to get MacroSiteDetail data for specific parameters. ");
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error("IllegalArgumentException caught while getting Sites data from  MacroSiteDetail Exception : {}", Utils.getStackTrace(illegalArgumentException));
			throw new DaoException("Unable to get MacroSiteDetail  data for specific parameters.");
		} catch (NoResultException noResultException) {
			logger.error("NoResultException  caught  while getting Sites data from MacroSiteDetail Exception : {}", Utils.getStackTrace(noResultException));
			throw new DaoException("Unable to get MacroSiteDetail data for  specific parameters.");
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while getting Sites data from  MacroSiteDetail Exception : {}", Utils.getStackTrace(persistenceException));
			throw new DaoException("Unable to get MacroSiteDetail data for  specific parameters.");
		} catch (Exception exception) {
			logger.error("Exception  caught while getting  Sites data from MacroSiteDetail  Exception : {}", Utils.getStackTrace(exception));
			throw new DaoException("Unable to get  MacroSiteDetail data for specific parameters.");
		}
	}
	
	
	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public List<MacroSiteDetail> getMacroSiteDetailByEcgi(String ecgi) {
		logger.info("Into the function getMacroSiteDetailByEcgi for ecgi {}", ecgi);
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<MacroSiteDetail> criteriaQuery = criteriaBuilder.createQuery(MacroSiteDetail.class);
			Root<MacroSiteDetail> macroSiteDetail = criteriaQuery.from(MacroSiteDetail.class);
			List<Predicate> criteriaList = new ArrayList<>();
			if (ecgi != null) {
				Predicate firstCondition = criteriaBuilder.equal(macroSiteDetail.get(InfraConstants.NE_ECGI_KEY), ecgi);
				criteriaList.add(firstCondition);
			}
			if (criteriaList != null && criteriaList.size() > 0)
				criteriaQuery.where(criteriaBuilder.and(criteriaList.toArray(new Predicate[0])));
			Query query = getEntityManager().createQuery(criteriaQuery);
			return query.getResultList();
		} catch (NullPointerException nullPointerException) {
			logger.error("NullPointerException caught while getting Sites data from MacroSiteDetail Exception : {}", Utils.getStackTrace(nullPointerException));
			throw new DaoException("Unable  to get MacroSiteDetail  data for   specific parameters.");
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error("IllegalArgumentException caught while getting Sites data from MacroSiteDetail Exception : {}", Utils.getStackTrace(illegalArgumentException));
			throw new DaoException("Unable to  get MacroSiteDetail data for specific parameters.");
		} catch (NoResultException noResultException) {
			logger.error("NoResultException caught while getting Sites data from MacroSiteDetail Exception : {}", Utils.getStackTrace(noResultException));
			throw new DaoException("Unable to get  MacroSiteDetail data for specific parameters.");
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while getting Sites data from MacroSiteDetail Exception : {}", Utils.getStackTrace(persistenceException));
			throw new DaoException("Unable to get MacroSiteDetail data for specific  parameters. ");
		} catch (Exception exception) {
			logger.error("Exception caught while getting Sites data from MacroSiteDetail  Exception : {}", Utils.getStackTrace(exception));
			throw new DaoException(" Unable to get MacroSiteDetail  data for specific parameters.");
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getSectorIdForSiteOverViewDetails(String neName, String neFrequency,String neStatus,String carrier) {
		logger.info("Going to get Fourth Sector data For SiteOverView Detail for neName {} , neFrequency {} , neStatus {} ",neName, neFrequency, neStatus);
		List<Integer> sectorIdList = new ArrayList<>();
		try {
			if (neName != null && neFrequency != null && neStatus != null) {
				Query query = getEntityManager().createQuery(
						"select distinct m.sector from RANDetail m where m.networkElement.networkElement.neName=:neName and m.networkElement.neFrequency=:neFrequency and m.networkElement.neStatus=:neStatus  and m.neBandDetail.carrier=:carrier and m.networkElement.networkElement is not null ");
				query.setParameter(InfraConstants.NE_NAME, neName.toUpperCase());
				query.setParameter(InfraConstants.NE_FREQUENCY, neFrequency);
				query.setParameter(InfraConstants.NE_STATUS, NEStatus.valueOf(neStatus.toUpperCase()));
				query.setParameter(ForesightConstants.CARRIER, carrier);
				sectorIdList = query.getResultList();
			}
		} catch (QueryTimeoutException queryTimeoutException) {
			logger.error("Unable to get Fourth Sector For neName {} and neFrequency {} neStatus {} SiteOverViewDetails Data Exception {} ",neName, neFrequency, neStatus, Utils.getStackTrace(queryTimeoutException));
		} catch (Exception exception) {
			logger.error("Unable to get Fourth Sector For sapid {} and band {} progressState {} SiteOverViewDetails Data Exception {} ",neName, neFrequency, neStatus, Utils.getStackTrace(exception));
		}
		return sectorIdList;
	}
	
	@Override
	public List<NetworkElementWrapper> getAllSapidCnum(String vendor, List<String> band) {
		List<NetworkElementWrapper> sapidCnumList=new ArrayList<>();
		try {
			logger.info("Going to get SapId and CNum for vendor : {}",vendor);
			Query query = getEntityManager().createNamedQuery("getAllSapIDCnum");
			query.setParameter(InfraConstants.VENDOR_KEY, Vendor.valueOf(vendor));
			query.setParameter(InfraConstants.NE_NETYPE_KEY, NEType.MACRO_CELL);
			query.setParameter(ForesightConstants.BAND, band);
			
			sapidCnumList = query.getResultList();
			logger.info(" Getting list size  {}",sapidCnumList==null?0:sapidCnumList.size());
		} catch (Exception ex) {
			logger.error("Error while getting SapId and CNum :" + Utils.getStackTrace(ex));
		}
		return sapidCnumList;
	}
	
	@Override
	public List<NetworkElementWrapper> getSapidCnumByGeographyLevelData(List<String> geographyName, String geographyType,String vendor, List<String> band) {
		List<NetworkElementWrapper> sapidCnumList=new ArrayList<>();
		try {
			logger.info(" Going to get row keys for geography: " + geographyName +"geography Type :"+geographyType +"vendor :"+vendor);
			String namedQuery="";
			if(geographyType.equals(InfraConstants.GEOGRAPHYL1)){
				namedQuery="getSapIDCnumBYGL1Wise";
			}
			else if(geographyType.equals(InfraConstants.GEOGRAPHYL2)){
				namedQuery="getSapIDCnumBYGL2Wise";
			}
			else if(geographyType.equals(InfraConstants.GEOGRAPHYL3)){
				namedQuery="getSapIDCnumBYGL3Wise";
			}
			else if(geographyType.equals(InfraConstants.GEOGRAPHYL4)){
				namedQuery="getSapIDCnumBYGL4Wise";
			}

			Query query = getEntityManager().createNamedQuery(namedQuery);
			query.setParameter(InfraConstants.GEOGRAPHY_LEVEL_NAME_KEY, geographyName);
			query.setParameter(InfraConstants.VENDOR_KEY, Vendor.valueOf(vendor));
			query.setParameter(InfraConstants.NE_NETYPE_KEY, NEType.MACRO_CELL);
			query.setParameter(ForesightConstants.BAND, band);
			
			sapidCnumList = query.getResultList();
			logger.info("Getting  list size  {}",sapidCnumList==null?0:sapidCnumList.size());
		} catch (Exception ex) {
			logger.error("Error while getting row keys: " + Utils.getStackTrace(ex));
		}
		return sapidCnumList;
	}
	
	@Override
	public List<NetworkElementWrapper> getUploadedCustomCell(String vendor, List<String> listOfCells,String node) {
		List<NetworkElementWrapper> sapidCnumList=new ArrayList<>();
		try {
			logger.info("Going to get SapId and CNum for vendor : {}",vendor);
			Query query = null;
			if(node.contains("ENODEB")) {
				query = getEntityManager().createNamedQuery("getUploadedCustomENB");
			} else {
				query = getEntityManager().createNamedQuery("getUploadedCustomCell");
			}
			query.setParameter(InfraConstants.NE_NETYPE_KEY, NEType.MACRO_CELL);
			query.setParameter(InfraConstants.VENDOR_KEY, Vendor.valueOf(vendor));
			query.setParameter("neIdList", listOfCells);
			sapidCnumList = query.getResultList();
			logger.info("Getting list size  {}",sapidCnumList==null?0:sapidCnumList.size());
		} catch (Exception ex) {
			logger.error("Error while getting SapId and CNum :" + Utils.getStackTrace(ex));
		}
		return sapidCnumList;
	}
	
	@Override
	public List<NetworkElementWrapper> getSiteDetailByLatLongAndBandWise(Double southWestLong, Double southWestLat,
			Double northEastLong, Double northEastLat, List<String> bandList, String domain, String vendor) {
		List<NetworkElementWrapper> results = new ArrayList<>();

		logger.info("going to get site detail for  {} {} {} {} {}", southWestLong, southWestLat, northEastLong,
				northEastLat, bandList);
		try {
			Query query = getEntityManager().createNamedQuery("getSiteDetailByLatLongAndBand")
					.setParameter(InfraConstants.BAND_LIST_KEY, bandList).setParameter("southWestLong", southWestLong)
					.setParameter(InfraConstants.SOUTHWEST_LATITUDE_KEY, southWestLat).setParameter("northEastLong", northEastLong)
					.setParameter(InfraConstants.NORTHEAST_LATITUDE_KEY, northEastLat).setParameter(ForesightConstants.NE_TYPE, NEType.MACRO_CELL)
					.setParameter(InfraConstants.NE_DOMAIN_KEY, Domain.valueOf(domain)).setParameter(InfraConstants.VENDOR_KEY, Vendor.valueOf(vendor));
			results = query.getResultList();
			logger.info("Getting results size for site details : {}", results.size());
		} catch (Exception e) {
			logger.error("Error in getting site detail by latitude and longitude :{} ", Utils.getStackTrace(e));
			throw new PersistenceException(e.getMessage());
		}
		if(results.isEmpty()) {
			throw new DaoException(ForesightConstants.ERROR_NO_CELLS_AVAILABLE);
		}
		return results;
	}
	
	@Override
	public List<NetworkElementWrapper> getSiteDetailByGeographyL4(List<String> bandList, String domain, String vendor, String geographyName) {
		List<NetworkElementWrapper> results = new ArrayList<>();

		logger.info("going to get site detail for  {} {} {}",bandList, domain, vendor);
		try {
			String namedQuery = null;
			if(ForesightConstants.ALL.equalsIgnoreCase(vendor)) {
				namedQuery = "getSiteDetailByGeogrphyL4ForAllVendor";
			} else {
				namedQuery = "getSiteDetailByGeogrphyL4";
			}
			Query query = getEntityManager().createNamedQuery(namedQuery)
					.setParameter(InfraConstants.BAND_LIST_KEY, bandList).setParameter(ForesightConstants.NE_TYPE, NEType.MACRO_CELL)
					.setParameter(InfraConstants.NE_DOMAIN_KEY, Domain.valueOf(domain)).setParameter(InfraConstants.GEOGRAPHYNAME, geographyName);
			
			if(!ForesightConstants.ALL.equalsIgnoreCase(vendor)) {
				query.setParameter(InfraConstants.VENDOR_KEY, Vendor.valueOf(vendor));
			}
			results = query.getResultList();
			logger.info("Getting results size for site details : {}", results.size());
		} catch (Exception e) {
			logger.error("Error in getting site detail by latitude and longitude :{} ", Utils.getStackTrace(e));
			throw new PersistenceException(e.getMessage());
		}
		if(results.isEmpty()) {
			throw new DaoException(ForesightConstants.ERROR_NO_CELLS_AVAILABLE);
		}
		return results;
	}
	
	@Override
	public List<NetworkElementWrapper> getCellDetailByVendorAndStatus(Vendor vendor, NEStatus status) {
		List<NetworkElementWrapper> list = new ArrayList<>();
		logger.info("inside method getCellDetailByVendorAndStatus vendor:{}", vendor);
		try {
			Query query = getEntityManager().createNamedQuery("getCellDetailByVendorAndStatus");
			query.setParameter(InfraConstants.VENDOR_KEY, vendor);
			query.setParameter(InfraConstants.NE_NESTATUS_KEY, status);
			list = query.getResultList();
			logger.info("size {}", list.size());
		} catch (Exception e) {
			logger.info("Exception inside method getCellDetailByVendorAndStatus vendor:{}, {}", vendor, Utils.getStackTrace(e));
		}
		return list;
	}
	
	@Override
	public List<String> getCarrierForNenameAndFrequency(String neName,String neFrequency,String neStatus) {
		logger.info("Going to get carrier for neName {} , neFrequency {}", neName,neFrequency);
		List<String> listOfCarrier = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getCarrierForNenameAndFrequency");
			query.setParameter(InfraConstants.NE_NAME, neName.toUpperCase());
			query.setParameter(InfraConstants.NE_FREQUENCY, neFrequency);
			query.setParameter(InfraConstants.NE_STATUS, NEStatus.valueOf(neStatus.toUpperCase()));
			listOfCarrier = query.getResultList();
			logger.info("List Of Carrier for neName : {},neFrequency : {}",neName,neFrequency);
		} catch (IllegalStateException | QueryTimeoutException | TransactionRequiredException e) {
			logger.warn("unable to get list of carrier for nename {},err msg{}",neName,e.toString());
		} 
		return listOfCarrier;
	}
	
	@Override
	public List<NetworkElement> getCellNameSpecificData(List<String> neIdList) {
		logger.info("Inside getCellNameSpecificData neIdList {} ", neIdList);
		List<NetworkElement> list = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getCellNameSpecificData")
					.setParameter("neIdList", neIdList);
			list =  query.getResultList();
		} catch (NoResultException e){
			logger.error("NoResultFound @getCellNameSpecificData");
		} catch (Exception e) {
			logger.error("Error while getting data of getCellNameSpecificData {}",ExceptionUtils.getStackTrace(e));
		}
		return list;
	}
	
	@Override
	public List<String> getSmallCellByGeographyTypeAndCellType(String geographyType, String vendor, List<String> geographyValues) {
		List<String> sapidCnumList = new ArrayList<>();
		String namedQuery="";
			try{
			List<String> geographyValue=new ArrayList<>();
			for(String geographyValue1:geographyValues)
			{
				geographyValue.add(geographyValue1.toUpperCase());
			}

		if(geographyType.equals(InfraConstants.GEOGRAPHYL2))
		{
			namedQuery="getSiteByGL2ForSmallCell";
		}
		else if(geographyType.equalsIgnoreCase(InfraConstants.GEOGRAPHYL4))
		{
			namedQuery="getSiteByGL4ForSmallCell";
		}
		Query query =getEntityManager().createNamedQuery(namedQuery);
		if(geographyValue!=null) {
            query.setParameter(InfraConstants.GEOGRAPHY_LEVEL_NAME_KEY, geographyValue);
        }
		   query.setParameter("vendor", Vendor.valueOf(vendor));
		sapidCnumList = query.getResultList();
		logger.info("Returning pm site small cell size :"+sapidCnumList.size());
		}catch(Exception e)
		{
			logger.info("Error in getting  small celll from Pm site Data {}",Utils.getStackTrace(e));
		}
		return sapidCnumList;
	}

	@Override
	public List<String> getSmallCellByGeographyTypeAndBandType(String geographyType, String smc, List<String> geographyValues,String band,String vendor) {
		List<String> sapidCnumList=new ArrayList<>();
		String namedQuery="";
			try{
			List<String> geographyValue=new ArrayList<>();
			for(String geographyValue1:geographyValues)
			{
				geographyValue.add(geographyValue1.toUpperCase());
			}

		if(geographyType.equals(InfraConstants.GEOGRAPHYL2))
		{
			namedQuery="getpmSiteByGL2forSmallCell";
		}
		else if(geographyType.equalsIgnoreCase(InfraConstants.GEOGRAPHYL4))
		{
			namedQuery="getPmSiteByGL4forSmallCell";
		}

		Query query = getEntityManager().createNamedQuery(namedQuery);

		if(geographyValue!=null) {
            query.setParameter(InfraConstants.GEOGRAPHY_LEVEL_NAME_KEY, geographyValue);
        }
		   query.setParameter("neFrequency", band);
		   query.setParameter("vendor", Vendor.valueOf(vendor));
		sapidCnumList = query.getResultList();
		logger.info("Returning pm site small cell size :"+sapidCnumList.size());
		}catch(Exception e)
		{
			logger.info("Error in getting  small celll from Pm site Data {}",Utils.getStackTrace(e));
		}
		return sapidCnumList;
	}


	@Override
	public List<String> getSapidCnumByGeographyLevel(List<String> geographyName, String geographyType,String vendor) {

		try {
			logger.info("Going to get row keys for geography: " + geographyName +"geography Type :"+geographyType +"vendor :"+vendor);
			List<String> sapidCnumList=new ArrayList<>();
			String namedQuery="";
			if(geographyType.equals("GEOGRAPHYL1")){
				namedQuery="getSapIDCnumBYGL1";
			}
			else if(geographyType.equals(InfraConstants.GEOGRAPHYL2)){
				namedQuery="getSapIDCnumBYGL2";
			}
			else if(geographyType.equals(InfraConstants.GEOGRAPHYL3)){
				namedQuery="getSapIDCnumBYGL3";
			}
			else if(geographyType.equals(InfraConstants.GEOGRAPHYL4)){
				namedQuery="getSapIDCnumBYGL4";
			}


			Query query =getEntityManager().createNamedQuery(namedQuery);

			if(geographyName!=null) {
                query.setParameter("geographyLevelName", geographyName);
            }
			query.setParameter("vendor", Vendor.valueOf(vendor));

			sapidCnumList = query.getResultList();
			logger.info("Getting list size  {}",sapidCnumList==null?0:sapidCnumList.size());

			return sapidCnumList;
		} catch (Exception ex) {

			logger.error("Error while getting row keys:" + Utils.getStackTrace(ex));
			return null;
		}
	}


	@Override
	public List<String> getSapidCnumByGeographyLevelAndBand(List<String> geographyName, String geographyType, String band, String vendor) {

		try {
			logger.info("Going to get row keys for geography: " + geographyName + "  band: " + band +" and vendor :"+vendor);
			List<String> sapidCnumList=new ArrayList<>();
			String namedQuery="";
			if(geographyType.equals("GEOGRAPHYL1")){
				namedQuery="getSapIDCNumBYGL1AndBand";
			}
			else if(geographyType.equals("GEOGRAPHYL2")){
				namedQuery="getSapIDCNumBYGL2AndBand";
			}
			else if(geographyType.equals("GEOGRAPHYL3")){
				namedQuery="getSapIDCNumBYGL3AndBand";
			}
			else if(geographyType.equals("GEOGRAPHYL4")){
				namedQuery="getSapIDCNumBYGL4AndBand";
			}


			Query query =getEntityManager().createNamedQuery(namedQuery);

			if(geographyName!=null) {
                query.setParameter("geographyLevelName", geographyName);
            }
			   query.setParameter("neFrequency", band);
			   query.setParameter("vendor", Vendor.valueOf(vendor));
			return query.getResultList();
		} catch (Exception ex) {

			logger.error("Error while getting row keys:" + Utils.getStackTrace(ex));
			return null;
		}
	}

	@Override
	public List<String> getEnodeByGeographyTypeAndCellType(String geographyType, List<String> geographyValues,String vendor) {
		List<String> sapIdList = new ArrayList<>();
		logger.info("Goiing to get sapids Data by geographyType {}  geographyValue {} " ,geographyType,geographyValues.get(0));
		try{
			Query query=null;
			List<String> geographyValue=new ArrayList<>();
			for(String geographyValue1:geographyValues)
			{
				geographyValue.add(geographyValue1.toUpperCase());
			}


		if(geographyType.equals("GEOGRAPHYL2"))
		{
			 query = getEntityManager().createNamedQuery("getSapIdByGeographyL2forSite")
					 .setParameter("geographyL2", geographyValue).setParameter("vendor", Vendor.valueOf(vendor));
		}
		else if(geographyType.equalsIgnoreCase("GEOGRAPHYL4"))
		{
			 query = getEntityManager().createNamedQuery("getSapIdByGeographyL4forSite")
					 .setParameter("geographyL4", geographyValue).setParameter("vendor", Vendor.valueOf(vendor));
		}
		sapIdList=query.getResultList();
		logger.info("Returning  EnodeB:"+sapIdList.size());
		}catch(Exception e)
		{
			logger.info("Error in getting site from pm site date error {}",Utils.getStackTrace(e));
		}
		return sapIdList;
	}
	
	@Override
	public List<MacroSitesDetailWrapper> getEcgiLocation() {
		try{
			Query query = getEntityManager().createNamedQuery("getEcgiLocation");
			return query.getResultList();
		} catch(Exception e) {
			 logger.error(ForesightConstants.LOG_EXCEPTION,e);
			 throw new DaoException(e);
		}
	}

	@Override             
	public MacroSiteDetail getMacroSiteDetailByNeNameAndType(String neName, NEType neType) {
		MacroSiteDetail macroSite=null;
		try{
            logger.debug("Inside method getMacroSiteDetailByNeNameAndType neName {} neType {}",neName,neType);
			macroSite = (MacroSiteDetail) getEntityManager().createNamedQuery("getMacroSiteByNeNameandType")
					.setParameter(InfraConstants.NE_NENAME_KEY, neName).setParameter(InfraConstants.NE_NETYPE_KEY, neType).getSingleResult();
			logger.info("macroSite  id {}", macroSite.getId());

		} catch (NoResultException noResultException) {
            logger.warn("Macro site detail is not present for neName: {}", neName);
        } 
	  return macroSite;
	}
	@Override
	public LatLng getLocationByCGIAndPci(Integer cgi, Integer pci) {
		logger.info("Going to get Macro Site Detail by CGI {} and PCI {} ", cgi, pci);
		try {
			Query query = getEntityManager().createNamedQuery("getMacroSiteDetailByCGIandPci");
			query.setParameter(InfraConstants.CGI_KEY, cgi);
			query.setParameter(InfraConstants.PCI_KEY, pci);
			return (LatLng) query.getSingleResult();
		} catch (Exception exception) {
			logger.error("Unable to get Macro Site Detail by cgi {} and pci {} Exception {}  ", cgi, pci,Utils.getStackTrace(exception));
			throw new DaoException("Unable to get latLong ");
		}
	}
	
	@Override             
	public List<NetworkElementWrapper> getSiteDetailByNeName(String neName) {
		List<NetworkElementWrapper> macroSite=null;
		try{
            logger.debug("Inside method getSiteDetailByNeName neName {} ",neName);
			macroSite = getEntityManager().createNamedQuery("getSiteDetailByNeName")
					.setParameter("neName", neName).getResultList();
			logger.info("macroSite details is {}",new Gson().toJson(macroSite));
		} catch (NoResultException noResultException) {
            logger.warn("Macro site detail is not present for neName: {}", neName);
        } 
	  return macroSite;
	}

	@Override
	public List<MacroSiteDetail> getSiteLocationByCGI(Integer cgi) {
		List<MacroSiteDetail> macroSiteList= null;
		try{
            logger.debug("Inside DAO  method getSiteLocationByCGI for CGI {} ",cgi);
            macroSiteList = getEntityManager().createNamedQuery("getMacroSiteDetailByCGI")
					.setParameter("cgi", cgi).getResultList();
            logger.info("Got the Response size from the DB :: {} ",macroSiteList.size());
		} catch (NoResultException noResultException) {
            logger.warn("Macro site detail is not present for CGI: {}", cgi);
        } 
	  return macroSiteList;
	}
	
	@Override
	public List<MacroSiteDetail> getMacroSiteDetailByListNename(List<String> nename) {
		logger.info("Into the function getMacroSiteDetailByListNename  nename {}", nename);
		try {
			TypedQuery<MacroSiteDetail> query = getEntityManager().createNamedQuery("getMacroSiteDetailByListNename",MacroSiteDetail.class).setParameter("nename", nename);
			return query.getResultList();
		}catch (IllegalArgumentException illegalArgumentException) {
			logger.error("IllegalArgumentException caught while getting Sites data from MacroSiteDetail Exception : {}", Utils.getStackTrace(illegalArgumentException));
			throw new DaoException("Unable to get MacroSiteDetail data for   specific parameters.");
		} catch (NoResultException noResultException) {
			logger.error("NoResultException caught while getting Sites data from MacroSiteDetail Exception : {}", Utils.getStackTrace(noResultException));
			throw new DaoException(" Unable to get MacroSiteDetail data for specific parameters. ");
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while getting Sites data from MacroSiteDetail Exception : {}", Utils.getStackTrace(persistenceException));
			throw new DaoException("Unable to get MacroSiteDetail data for specific parameters.");
		} catch (Exception exception) {
			logger.error("Exception caught while getting Sites data from MacroSiteDetail Exception : {}", Utils.getStackTrace(exception));
			throw new DaoException("Unable to get MacroSiteDetail data for specific parameters.");
		}
	}
	
	@Override
	public List<Integer> getDistinctCellIdsByNeType(String neType) {
		logger.info("Going to getDistinctCellIdsByNeType");
		List<Integer> resultList=new ArrayList<>();
		try {
			Query query=null;
			query=getEntityManager().createNamedQuery("getDistinctCellIdsByNeType").setParameter("neType", NEType.valueOf(neType));
			resultList=query.getResultList();
		} catch(IllegalStateException | QueryTimeoutException | TransactionRequiredException e){
			logger.warn("Exception while getCellList   From NetworkElement,err msg {}",e.getMessage());
		} 
		return resultList;
	}
	
	@Override
	public List<Integer> getDistinctCellIds() {
		logger.info("Going to getDistinctCellIdsByNeType");
		List<Integer> resultList=new ArrayList<>();
		try {
			Query query=null;
			query=getEntityManager().createNamedQuery("getDistinctCellIds");
			resultList=query.getResultList();
		} catch(IllegalStateException | QueryTimeoutException | TransactionRequiredException e){
			logger.warn(" Exception  while getCellList  From NetworkElement,err msg {}",e.getMessage());
		} 
		return resultList;
	}
	
	@Override
	public List<Integer> getDistinctSectorsByCellId(List<Integer> listCellIds) {
		logger.info("Going to getDistinctSectorsByCellId");
		List<Integer> resultList=new ArrayList<>();
		try {
			Query query=null;
			query=getEntityManager().createNamedQuery("getDistinctSectorsByCellId").setParameter("cellIds", listCellIds);
			resultList=query.getResultList();
		} catch(IllegalStateException | QueryTimeoutException | TransactionRequiredException e){
			logger.warn("Exception while getCellList  From NetworkElement,err msg {}",e.getMessage());
		} 
		return resultList;
	}
	
	@Override
	public List<Integer> getAllDistinctSectors() {
		logger.info("Going to getAllDistinctSectors");
		List<Integer> resultList=new ArrayList<>();
		try {
			Query query=null;
			query=getEntityManager().createNamedQuery("getAllDistinctSectors");
			resultList=query.getResultList();
		} catch(IllegalStateException | QueryTimeoutException | TransactionRequiredException e){
			logger.warn("Exception while getCellList  From NetworkElement,err msg {}",e.getMessage());
		} 
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> validateEnbid(Integer enbId) {
		List<Integer> result = new ArrayList<>();
		try {

			Query query = getEntityManager().createNamedQuery("validateEnbid").setParameter("enbId", enbId);
			result = query.getResultList();

		} catch (IllegalStateException ie) {
			logger.error("IllegalArgumentException caught while getting enbid from MacroSiteDetail Exception : {}",
					Utils.getStackTrace(ie));
			throw new DaoException("Unable to get data from MacroSiteDetail ");
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while getting enbid data from MacroSiteDetail Exception : {}",
					Utils.getStackTrace(persistenceException));
			throw new DaoException("Unable to get data form MacroSiteDetail ");
		} catch (Exception exception) {
			logger.error("Exception caught while getting enbid data from MacroSiteDetail Exception : {}",
					Utils.getStackTrace(exception));
			throw new DaoException("Unable to get data form MacroSiteDetail ");
		}
		return result;
	}
	/*@SuppressWarnings("unchecked")
	@Override
	public List<NetworkElementWrapper> getMacroSiteDetailByNameAndType(String neName,String neType) {
		List<Object[]> macroSiteDetailList = new ArrayList<>();
		List<NetworkElementWrapper> networkElementWrappperList=new ArrayList<>();
		logger.info("Going to get MacroSiteDetail data Bandwise for nename  {} ", neName);
		try {
			String dynamicQuery="Select nd.neFrequency,max(case when m.sector in(1,2,3) then  nd.bandOnairDate end) as onAirDate,"
					+ "MAX(case when m.sector in(1,2,3) then  nd.bandStatus end) as nestatus,"
					+ "m.sector,GROUP_CONCAT(case when m.sector in(1,2,3) then  m.bandwidth end ORDER BY nd.carrier SEPARATOR ' + ' ) as bandwidth,"
					+ "GROUP_CONCAT(case when m.sector in(4,5,6) then  m.bandwidth end ORDER BY nd.carrier SEPARATOR ' + ' ) as bandwidthFourthSector ,"
					+ "max(case when m.sector in(4,5,6) then  nd.bandOnairDate end) as onAirDateFourthSector,"
					+ "MAX(case when m.sector in(4,5,6) then  nd.bandStatus end) as neStatusFourthSector "
					+ " from RANDetail m "
					+ " inner join NEBandDetail nd on m.neBandDetailid_fk=nd.neBandDetailid_pk inner join NetworkElement ne on nd.networkElementid_fk=ne.networkElementid_pk"
					+ " where ne.neName='"+neName+"' and ne.neType='"+neType+"'  group by nd.neFrequency,m.sector";
			Query query = getEntityManager().createNativeQuery(dynamicQuery);
			macroSiteDetailList =  query.getResultList();
			logger.info("macroSiteDetailList: {}",macroSiteDetailList);
			if (macroSiteDetailList != null && macroSiteDetailList.size() > 0) {
			for(Object[] row : macroSiteDetailList){
				NetworkElementWrapper networkElementWrapper=new NetworkElementWrapper();
				networkElementWrapper.setNeFrequency(row[0] != null ? row[0].toString() : null);
				networkElementWrapper.setOnAirDate(row[1] != null ? new SimpleDateFormat("YYYY-MM-DD HH:MM:SS").parse(row[1].toString()) : null);
				//networkElementWrapper.setEmsLive(row[2] != null ? row[2].toString() : null);
				networkElementWrapper.setNeStatus(row[2] != null ? row[2].toString() : null);
				networkElementWrapper.setSectorId(row[3] != null ? Integer.valueOf(row[3].toString()) : null);
				networkElementWrapper.setBandwidth(row[4] != null ? row[4].toString() : null);
				networkElementWrapper.setBandwidthFourthSector(row[5] != null ? row[5].toString() : null);
				networkElementWrapper.setOnAirDateFourthSector(row[6] != null ? new SimpleDateFormat("YYYY-MM-DD HH:MM:SS").parse(row[6].toString()) : null);
				//networkElementWrapper.setEmsLiveFourthSector(row[8] != null ? row[8].toString() : null);
				networkElementWrapper.setNeStatusFourthSector(row[7] != null ? row[7].toString() : null);
				networkElementWrappperList.add(networkElementWrapper);
			}
		}
			return networkElementWrappperList;
		} catch (IllegalArgumentException | PersistenceException exception) {
			logger.error("NoResultException caught while getting NetworkElement data for neName {} Exception {}",
					neName, Utils.getStackTrace(exception));
		} catch (Exception exception) {
			logger.error("Error in getting NetworkElement for neName {} Exception {}", neName,
					Utils.getStackTrace(exception));
		}
		return networkElementWrappperList;
	}*/

	@Override
	@SuppressWarnings("unchecked")  
	public List<Object[]> getBtsDetailByCgi(String cgi) {
		logger.info("Going to get bts Code by ecgi {}  ", cgi);
		try {
			if (cgi != null) {
				Query query = getEntityManager()
						.createQuery("select m.networkElement.neName,m.neBandDetail.siteAddress,m.networkElement.neStatus,m.pci,m.networkElement.neFrequency from MacroSiteDetail m where m.cgi=:cgi AND m.networkElement.isDeleted=0");
				query.setParameter("cgi", Integer.parseInt(cgi));
				return query.getResultList();
			}
		} catch (Exception exception) {
			logger.error("Error in fetching bts Code by ecgi : {} ", Utils.getStackTrace(exception));
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findAllOnairSites(String status, String neType) {
		logger.info("Going to fetch All Onair Sites for sitestatus {} neType {} ", status, neType);
		List<Object[]> macroSiteDetails = new ArrayList<>();
		try {
			Query query = getEntityManager().createQuery(
					"select n1.id,m.cellId,m.azimuth,m.pci,n.neId,n.neFrequency,m.carrier,m.sector,n.latitude,n.longitude,n.neStatus,n.domain,n.vendor from MacroSiteDetail m join NetworkElement n on n.id=m.networkElement.id join NetworkElement n1 on n1.id=n.networkElement.id where n.longitude is not null and n.neName is not null and n.latitude is not null \n"
							+ "and upper(n.neType)=upper(:neType) and upper(n.neStatus)=upper(:status) and m.networkElement is not null and n1.id is not null AND n.isDeleted=0");
			query.setParameter("neType", neType);
			query.setParameter("status", status);
			macroSiteDetails = query.getResultList();
			logger.info("MacroSiteDetail list size : {}", macroSiteDetails.size());
		} catch (Exception exception) {
			logger.error("Unable to fetch All Onair Sites Exception {} ", Utils.getStackTrace(exception));
		}
		return macroSiteDetails;
	}
	
	@Override
	@SuppressWarnings("unchecked")  
	public List<Object[]> getBtsNameByEcgi(String ecgi) {
		logger.info("Going to get bts name by ecgi : {}", ecgi);
		List<Object[]> list = null;
		try {
			String sqlQuery = "select m.networkElement.neName, m.pci, m.networkElement.neFrequency from MacroSiteDetail m where m.ecgi=:ecgi AND m.networkElement.isDeleted=0";
			Query query = getEntityManager().createQuery(sqlQuery).setParameter("ecgi", ecgi);
			list = query.getResultList();
			logger.info("Bts list : {}", list);
		} catch (Exception ex) {
			logger.error("Error in getting bts name by ecgi :{} ,  Exception : {} ", ecgi, Utils.getStackTrace(ex));
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getDistinctPCIBySiteIdAndBand(String neName, List<String> bandList) {
		logger.info("Going to getDistinctPCIBySiteIdAndBand for neName {} , bandList {}",neName,bandList);
		try {
			Query query = getEntityManager().createNamedQuery("getDistinctPCIBySiteIdAndBand")
											.setParameter("neName", neName)
											.setParameter("bandList", bandList);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception in getDistinctPCIBySiteIdAndBand {}", Utils.getStackTrace(e));
		}
		return Collections.emptyList();
	}
	@Override
	public List<Object[]> getSectorDetailReportData(String neFrequencyList,Map<String ,List<String>> geographyNames,List<NEStatus> neStatusList,List<NEType> neTypeList,Double southWestLong, Double southWestLat, Double northEastLong,
			Double northEastLat ) {
		List<Object[]> sectorDetailDataList = new ArrayList<>();
		List<String> neStausListForReport=new ArrayList<>();
		List<String> neTypeListForReport=new ArrayList<>();
		for (NEStatus neStatus : neStatusList) {
			neStausListForReport.add(neStatus.toString());
		}
		for (NEType neType : neTypeList) {
			neTypeListForReport.add(neType.toString());
		}
		logger.info("Going to get data for sector detail report.");
		try {
			String dynamicQuery= "select ne.latitude,ne.longitude,n.nename as sapid,"
					+ "case ne.netype  when 'MACRO_CELL' then 'MACRO' when 'PICO_CELL' then 'PICO' when 'GALLERY_CELL' then 'GALLERY' when 'SHOOTER_CELL' then 'SHOOTER' when 'IBS_CELL' then 'IBS'  else ne.netype end as netype,"
					+ "ne.vendor,case when ne.netype in('MACRO_CELL','ODSC_CELL','SHOOTER_CELL') then 'OUTDOOR' else 'INDOOR' end as cell_location,ne.nestatus as nestatus,ne.enbid  as enbid,"
					+ "ne.mcc,ne.mnc,msd.tac,date_format(msd.cellonairdate,'%d-%m-%Y') as onairdate,gl1.displayName gl1,gl2.displayName gl2,gl3.displayName gl3,gl4.displayName gl4,"
					+ "max(case when nbd.carrier='FIRST' then msd.sector else null end) as sector,"
					+ " max(case when nbd.carrier='FIRST' then ne.cellNum else null end) as cellid,"
					+ " max(case when nbd.carrier='FIRST' then msd.bandwidth else null end) as bandwidth,"
					+ " max(case when nbd.carrier='FIRST' then ne.ecgi else null end) as ecgi,"
					+ " max(case when nbd.carrier='FIRST' then msd.pci else null end) as pci,"
					+ " max(case when nbd.carrier='FIRST' then msd.azimuth else null end) azimuth,"
					+ " max(case when nbd.carrier='FIRST' then msd.txpower else null end) txpower,"
					+ " max(case when nbd.carrier='FIRST' then msd.earfcn else null end) as dlearfcn,"
					+ " max(case when nbd.carrier='FIRST' then msd.ulearfcn else null end) as ulearfcn,"
					+ " max(case when nbd.carrier='FIRST' then rs.emsstatus else null end) emslive,"
					+ " max(case when nbd.carrier='FIRST' then date_format(rs.emslivedate,'%d-%m-%Y') else null end) as emslivedate,"
					+ " max(case when nbd.carrier='FIRST' then rs.emslivecounter else null end) as emslivecounter,"
					+ " max(case when nbd.carrier='SECOND' then ne.cellNum else null end) as 'second cellid',"
					+ " max(case when nbd.carrier='SECOND' then msd.bandwidth else null end) as 'second bandwidth',"
					+ " max(case when nbd.carrier='SECOND' then ne.ecgi else null end) as 'second ecgi',"
					+ " max(case when nbd.carrier='SECOND'  then msd.pci else null end) as 'second pci',"
					+ " max(case when nbd.carrier='SECOND' then msd.earfcn else null end) as 'second dlearfcn',"
					+ " max(case when nbd.carrier='SECOND' then msd.ulearfcn else null end) as 'second ulearfcn',"
					+ " max(case when nbd.carrier='SECOND' then rs.emsstatus else null end) as 'second emslive',"
					+ " max(case when nbd.carrier='SECOND' then date_format(rs.emslivedate,'%d-%m-%Y') else null end) as 'second emslivedate',"
					+ " max(case when nbd.carrier='SECOND' then rs.emslivecounter else null end) as 'second emslivecounter',"
					+ " msd.antennatype,msd.antennaheight,msd.antennagain,msd.antennavendor,msd.electilt,msd.mechtilt,"
					+ " msd.horizontalbeamwidth,msd.verticalbeamwidth, necell.rruconfiguration,necell.antennaportconfiguration,case msd.operationalstatus when 'enabled' then 'Enabled' when 'disabled' then 'Disabled' else msd.operationalstatus end  as enodeBstatus,"
					+ " case msd.adminState when 'locked' then 'Locked' when 'unlocked' then 'Unlocked' else msd.adminState end as adminState,"
					+ " case when ne.nestatus='NONRADIATING' then 'Yes'  else 'No' end  as Non_Radiating_Status,rs.nonradiatingdate,"
					+ " case when ne.nestatus='NONRADIATING' then rs.nonradiatingcounter else 0 end  Non_Radiating_EMS,"
					+ " case when ne.nestatus='DECOMMISSIONED' then 'Yes'  else 'No' end  'De_Commissioned_Sites' ,rs.decommissioningdate,"
					+ " date_format(rs.cmavailabilitydate,'%d-%m-%Y') as cmavailabilitydate,date_format(rs.pmavailabilitydate,'%d-%m-%Y') as pmavailabilitydate,'-' as fmavailabilitydate,ne.nesource,ne.creationtime "
					+ "  from  NetworkElement ne left join RANDetail msd on ne.networkelementid_pk=msd.networkelementid_fk left join  NEBandDetail nbd on msd.nebanddetailid_fk=nbd.nebanddetailid_pk "
					+ " left join NetworkElement n on nbd.networkelementid_fk=n.networkelementid_pk left join GeographyL4 gl4 on n.geographyl4id_fk=gl4.geographyl4id_pk  left join GeographyL3 gl3 on gl4.geographyl3id_fk=gl3.geographyl3id_pk "
					+ " left join GeographyL2 gl2 on gl3.geographyl2id_fk=gl2.geographyl2id_pk  left join GeographyL1 gl1 on gl2.geographyl1id_fk=gl1.geographyl1id_pk  left join RANCellStats rs on msd.randetailid_pk=rs.randetailid_fk "
					+ "  left join NECellDetail necell on msd.randetailid_pk=necell.randetailid_fk"
					+ "  left join NESiteDetail nesite on n.networkelementid_pk=nesite.networkelementid_fk  "
					+ " where    ";
					if (geographyNames != null && !geographyNames.isEmpty()) {
											if (geographyNames.get(InfraConstants.GEOGRAPHYL4_TABLE) != null) {
												dynamicQuery+= " gl4.name IN (:geographyL4) and ";
											}
											if (geographyNames.get(InfraConstants.GEOGRAPHYL3_TABLE) != null) {
												dynamicQuery+= " gl3.name IN (:geographyL3) and ";
											}
											if (geographyNames.get(InfraConstants.GEOGRAPHYL2_TABLE) != null) {
												dynamicQuery+= " gl2.name IN (:geographyL2) and ";
											}
											if (geographyNames.get(InfraConstants.GEOGRAPHYL1_TABLE) != null) {
												dynamicQuery+= " gl1.name IN (:geographyL1) and ";
											}
										}
										if(neStausListForReport != null && !neStausListForReport.isEmpty()) {
					                    	dynamicQuery+= " ne.nestatus in (:neStausListForReport) and ";
										}
										if(neFrequencyList != null && !neFrequencyList.isEmpty()) {
					                    	dynamicQuery+= " ne.nefrequency =:neFrequencyList and ";
										}
										if(neTypeListForReport != null && !neTypeListForReport.isEmpty()) {
					                    	dynamicQuery+= " ne.netype IN (:neTypeListForReport) and ";
										}
										if (southWestLat != null) {
											dynamicQuery+= " ne.latitude > "+southWestLat + " and ";
										}
										if (northEastLat != null) {
											dynamicQuery+= " ne.latitude < "+northEastLat + " and " ;
										}
										if (southWestLong != null) {
											dynamicQuery+= " ne.longitude > "+southWestLong + " and " ;
										}
										if (northEastLong != null) {
											dynamicQuery+= " ne.longitude < "+northEastLong + " and " ;
										}

					  dynamicQuery+= "    ne.deleted=0 group by n.nename,msd.sector ";
					   

Query query = getEntityManager().createNativeQuery(dynamicQuery);
			if(neStausListForReport != null && !neStausListForReport.isEmpty())
			query.setParameter("neStausListForReport", neStausListForReport);
			if(neFrequencyList != null && !neFrequencyList.isEmpty())
				query.setParameter("neFrequencyList", neFrequencyList);
			if(neTypeListForReport != null && !neTypeListForReport.isEmpty())
				query.setParameter("neTypeListForReport", neTypeListForReport);
			if(geographyNames != null && !geographyNames.isEmpty()) {
			if(geographyNames.get(InfraConstants.GEOGRAPHYL4_TABLE) != null)
				query.setParameter("geographyL4", geographyNames.get(InfraConstants.GEOGRAPHYL4_TABLE));
			if(geographyNames.get(InfraConstants.GEOGRAPHYL3_TABLE) != null)
				query.setParameter("geographyL3", geographyNames.get(InfraConstants.GEOGRAPHYL3_TABLE));
			if(geographyNames.get(InfraConstants.GEOGRAPHYL2_TABLE) != null)
				query.setParameter("geographyL2", geographyNames.get(InfraConstants.GEOGRAPHYL2_TABLE));
			if(geographyNames.get(InfraConstants.GEOGRAPHYL1_TABLE) != null)
				query.setParameter("geographyL1", geographyNames.get(InfraConstants.GEOGRAPHYL1_TABLE));
		}
			sectorDetailDataList =  query.getResultList();
			logger.info("list: {}",sectorDetailDataList.size());
			return sectorDetailDataList;
		} catch (IllegalArgumentException | PersistenceException exception) {
			logger.error("NoResultException caught while getting NetworkElement data Exception {}", Utils.getStackTrace(exception));
		} catch (Exception exception) {
			logger.error("Error in getting NetworkElement Exception {}", Utils.getStackTrace(exception));
		}
		return sectorDetailDataList;
	}

}

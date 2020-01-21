package com.inn.foresight.core.infra.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.IRANDetailDao;
import com.inn.foresight.core.infra.model.RANDetail;
import com.inn.foresight.core.infra.utils.CriteriaUtils;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.InfraUtils;
import com.inn.foresight.core.infra.utils.enums.NEStatus;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.wrapper.NetworkElementWrapper;
import com.inn.foresight.core.infra.wrapper.SiteSummaryOverviewWrapper;
import com.inn.foresight.core.infra.wrapper.WifiWrapper;

@Repository("RANDetailDaoImpl")
public class RANDetailDaoImpl extends HibernateGenericDao<Integer, RANDetail> implements IRANDetailDao {

    public RANDetailDaoImpl() {
        super(RANDetail.class);
    }

    private Logger logger = LogManager.getLogger(RANDetailDaoImpl.class);

    @SuppressWarnings({"unchecked", "rawtypes", "unused"})
    @Override
    public List<Tuple> getCellsDataForVisualisation(Map<String, Double> viewPortMap, Map<String, List<Map>> filters,
            Map<String, List<String>> projection, List distinctEntity) throws DaoException {
        Map criteriaMap = new HashMap<>();
        Map<String, Root> rootMap = new HashMap<>();
        try {
            logger.info("Going to get site cells data from entity : {}", distinctEntity);
            List<Predicate> predicateList = new ArrayList<>();
            List<Selection<?>> selection = new ArrayList<>();
            List<Expression<?>> expression = new ArrayList<>();
            distinctEntity = InfraUtils.getDistinctEntity(distinctEntity, filters, projection);
            CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
            Root<RANDetail> ranDetailRoot = criteriaQuery.from(RANDetail.class);
            logger.info("distinctEntity___________________-------->{}",distinctEntity);
            distinctEntity.remove(InfraConstants.RANDETAIL_TABLE);
            predicateList = CriteriaUtils.getPredicates(criteriaBuilder, ranDetailRoot, predicateList,
                    filters.get(InfraConstants.RANDETAIL_TABLE));
            selection = CriteriaUtils.getSelectionForSites(ranDetailRoot,
                    projection.get(InfraConstants.RANDETAIL_TABLE), selection);
            rootMap = CriteriaUtils.getRootMap(distinctEntity, criteriaQuery);
            
            logger.info("rootMap___________________-------->{}",rootMap);
            
            criteriaMap = CriteriaUtils.getNEForCellDetail(criteriaMap, criteriaBuilder, predicateList, filters,
                    selection, distinctEntity, criteriaQuery, projection, viewPortMap, rootMap, ranDetailRoot);

            filters.remove(InfraConstants.RANDETAIL_TABLE);
            filters.remove(InfraConstants.NETWORKELEMENT_TABLE);
            filters.remove(InfraConstants.PARENT_NETWORK_ELEMENT);
            projection.remove(InfraConstants.RANDETAIL_TABLE);
            projection.remove(InfraConstants.NETWORKELEMENT_TABLE);
            projection.remove(InfraConstants.PARENT_NETWORK_ELEMENT);

            predicateList = (List<Predicate>) criteriaMap.get(InfraConstants.PREDICATE);
            selection = (List<Selection<?>>) criteriaMap.get(InfraConstants.SELECTION);
            predicateList = CriteriaUtils.getParametersForCells(viewPortMap, criteriaBuilder, predicateList,
                    distinctEntity, filters, criteriaQuery, rootMap, ranDetailRoot);

            predicateList = CriteriaUtils.getCriteriaPredicates(criteriaBuilder, rootMap, predicateList, filters);
            selection = CriteriaUtils.getSelectionForCriteriaBuilder(rootMap, projection, selection);
            criteriaQuery.select(criteriaBuilder.tuple(selection.toArray(new Selection<?>[]{})));
            Query query = getEntityManager().createQuery(criteriaQuery.where(predicateList.toArray(new Predicate[]{})));
            List<Tuple> list= query.getResultList();
            logger.info("list  {}",list.size());
            return list;
        } catch (IllegalArgumentException | PersistenceException exce) {
            logger.error("Exception caught while getting data from entity {}. Exception : {}", distinctEntity,
                    Utils.getStackTrace(exce));
            throw new DaoException("Unable  to get site cells  data.");
        } catch (Exception exception) {
            logger.error("Exception caught while getting data from entity {}. Exception : {}", distinctEntity,
                    Utils.getStackTrace(exception));
            throw new DaoException("Unable to get  site cells data.");
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public List<Tuple> getCellsDataForRAN(Map<String, Double> viewPortMap, Map<String, List<Map>> filters,
            Map<String, List<String>> projection, Boolean distinct) throws DaoException {
        Map criteriaMap = new HashMap<>();
        List distinctEntity = new ArrayList<>();
        Map<String, Root> rootMap = new HashMap<>();
        try {
            logger.info("Going to get site cells data from entity : {}", distinctEntity);
            List<Predicate> predicateList = new ArrayList<>();
            List<Selection<?>> selection = new ArrayList<>();
            distinctEntity = InfraUtils.getDistinctEntity(distinctEntity, filters, projection);
            CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
            Root<RANDetail> ranDetailRoot = criteriaQuery.from(RANDetail.class);
            distinctEntity.remove(InfraConstants.RANDETAIL_TABLE);
            predicateList = CriteriaUtils.getPredicates(criteriaBuilder, ranDetailRoot, predicateList,
                    filters.get(InfraConstants.RANDETAIL_TABLE));
            selection = CriteriaUtils.getSelectionForSites(ranDetailRoot,
                    projection.get(InfraConstants.RANDETAIL_TABLE), selection);
            rootMap = CriteriaUtils.getRootMap(distinctEntity, criteriaQuery);
            criteriaMap = CriteriaUtils.getNEForCellDetail(criteriaMap, criteriaBuilder, predicateList, filters,
                    selection, distinctEntity, criteriaQuery, projection, viewPortMap, rootMap, ranDetailRoot);

            filters.remove(InfraConstants.RANDETAIL_TABLE);
            filters.remove(InfraConstants.PARENT_NETWORK_ELEMENT);
            filters.remove(InfraConstants.NETWORKELEMENT_TABLE);
            projection.remove(InfraConstants.RANDETAIL_TABLE);
            projection.remove(InfraConstants.NETWORKELEMENT_TABLE);

            predicateList = (List<Predicate>) criteriaMap.get(InfraConstants.PREDICATE);
            selection = (List<Selection<?>>) criteriaMap.get(InfraConstants.SELECTION);

            predicateList = CriteriaUtils.getParametersForCells(viewPortMap, criteriaBuilder, predicateList,
                    distinctEntity, filters, criteriaQuery, rootMap, ranDetailRoot);

            predicateList = CriteriaUtils.getCriteriaPredicates(criteriaBuilder, rootMap, predicateList, filters);
            selection = CriteriaUtils.getSelectionForCriteriaBuilder(rootMap, projection, selection);

            criteriaQuery.select(criteriaBuilder.tuple(selection.toArray(new Selection<?>[]{}))).distinct(distinct);
            Query query = getEntityManager().createQuery(criteriaQuery.where(predicateList.toArray(new Predicate[]{})));
            return query.getResultList();
        } catch (IllegalArgumentException | PersistenceException exce) {
            logger.error("Exception caught while getting data from entity {} Exception : {}", distinctEntity,
                    Utils.getStackTrace(exce));
            throw new DaoException("Unable to get site cells data.");
        } catch (Exception exception) {
            logger.error("Exception caught while getting data from entity {} Exception : {}", distinctEntity,
                    Utils.getStackTrace(exception));
            throw new DaoException("Unable to get site cells data.");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<NetworkElementWrapper> getMacroSiteDetailByNameAndType(String neName, String neType) {
        List<Object[]> macroSiteDetailList = new ArrayList<>();
        List<NetworkElementWrapper> networkElementWrappperList = new ArrayList<>();
        logger.info("Going to get MacroSiteDetail data Bandwise for nename  {} ", neName);
        try {
            String dynamicQuery = "Select nd.neFrequency,max(case when m.sector in(1,2,3) then  nd.bandOnairDate end) as onAirDate,"
                    + "MAX(case when m.sector in(1,2,3) then  nd.bandStatus end) as nestatus,"
                    + "m.sector,GROUP_CONCAT(case when m.sector in(1,2,3) then  m.bandwidth end ORDER BY nd.carrier SEPARATOR ' + ' ) as bandwidth,"
                    + "GROUP_CONCAT(case when m.sector in(4,5,6) then  m.bandwidth end ORDER BY nd.carrier SEPARATOR ' + ' ) as bandwidthFourthSector ,"
                    + "max(case when m.sector in(4,5,6) then  nd.bandOnairDate end) as onAirDateFourthSector,"
                    + "MAX(case when m.sector in(4,5,6) then  nd.bandStatus end) as neStatusFourthSector "
                    + " from RANDetail m "
                    + " inner join NEBandDetail nd on m.neBandDetailid_fk=nd.neBandDetailid_pk inner join NetworkElement ne on nd.networkElementid_fk=ne.networkElementid_pk"
                    + " where ne.neName='" + neName + "' and ne.neType='" + neType
                    + "' and ne.deleted=0 group by nd.neFrequency,m.sector";
            Query query = getEntityManager().createNativeQuery(dynamicQuery);
            macroSiteDetailList = query.getResultList();
             if (macroSiteDetailList != null && macroSiteDetailList.size() > 0) {
                for (Object[] row : macroSiteDetailList) {
                    NetworkElementWrapper networkElementWrapper = new NetworkElementWrapper();
                    networkElementWrapper.setNeFrequency(row[0] != null ? row[0].toString() : null);
                    networkElementWrapper.setOnAirDate(row[1] != null
                            ? new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(row[1].toString())
                            : null);
                    // networkElementWrapper.setEmsLive(row[2] != null ? row[2].toString() : null);
                    networkElementWrapper.setNeStatus(row[2] != null ? row[2].toString() : null);
                    networkElementWrapper.setSectorId(row[3] != null ? Integer.valueOf(row[3].toString()) : null);
                    networkElementWrapper.setBandwidth(row[4] != null ? row[4].toString() : null);
                    networkElementWrapper.setBandwidthFourthSector(row[5] != null ? row[5].toString() : null);
                    networkElementWrapper.setOnAirDateFourthSector(row[6] != null
                            ? new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(row[6].toString())
                            : null);
                    // networkElementWrapper.setEmsLiveFourthSector(row[8] != null ?
                    // row[8].toString() : null);
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
    }

    @Override
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
    }

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
                + " max(case when m.sector=6   then m.bandwidth else null end) as gammabandwidthadd,"
                
				+ " max(case when m.sector=1   then m.operationalStatus else null end) as alphaoperationalstatus,"
				+ " max(case when m.sector=2   then m.operationalStatus else null end) as betaoperationalstatus,"
				+ " max(case when m.sector=3   then m.operationalStatus else null end) as gammaoperationalstatus,"
				+ " max(case when m.sector=4   then m.operationalStatus else null end) as alphaoperationalstatusadd,"
				+ " max(case when m.sector=5   then m.operationalStatus else null end) as betaoperationalstatusadd,"
				+ " max(case when m.sector=6   then m.operationalStatus else null end) as gammaoperationalstatusadd,"
				
				+ " max(case when m.sector=1   then m.adminState else null end) as alphaadminstate,"
				+ " max(case when m.sector=2   then m.adminState else null end) as betaadminstate,"
				+ " max(case when m.sector=3   then m.adminState else null end) as gammaadminstate,"
				+ " max(case when m.sector=4   then m.adminState else null end) as alphaadminstateadd,"
				+ " max(case when m.sector=5   then m.adminState else null end) as betaadminstateadd,"
				+ " max(case when m.sector=6   then m.adminState else null end) as gammaadminstateadd )"
				
                + " from RANDetail m where m.networkElement.isDeleted=0 and upper(m.networkElement.networkElement.neName)=:neName and m.networkElement.neFrequency=:neFrequency and  "
                + "  upper(m.networkElement.neStatus)=:neStatus and m.networkElement.networkElement is not null and m.neBandDetail.carrier='FIRST' ";

    }

    @Override
    public SiteSummaryOverviewWrapper getSiteSummaryOverviewByBandForSecondCarrier(String neName, String neFrequency,
            String neStatus) {
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
    }

    private String getQuerySiteSummaryOverviewByBandForSecondCarrier() {

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
                + " max(case when m.sector=6  then m.networkElement.ecgi else null end)  as gammaecgiaddSecond,"
                
				+ " max(case when m.sector=1   then m.operationalStatus else null end) as alphaoperationalStatusSecond,"
				+ " max(case when m.sector=2   then m.operationalStatus else null end) as betaoperationalStatusSecond,"
				+ " max(case when m.sector=3   then m.operationalStatus else null end) as gammaoperationalStatusSecond,"
				+ " max(case when m.sector=4   then m.operationalStatus else null end) as alphoperationalStatusaddSecond,"
				+ " max(case when m.sector=5   then m.operationalStatus else null end) as betaoperationalStatusaddSecond,"
				+ " max(case when m.sector=6   then m.operationalStatus else null end) as gammaoperationalStatusaddSecond,"
				
				+ " max(case when m.sector=1   then m.adminState else null end) as alphaadminStateSecond,"
				+ " max(case when m.sector=2   then m.adminState else null end) as betaadminStateSecond,"
				+ " max(case when m.sector=3   then m.adminState else null end) as gammaadminStateSecond,"
				+ " max(case when m.sector=4   then m.adminState else null end) as alphaadminStateaddSecond,"
				+ " max(case when m.sector=5   then m.adminState else null end) as betaadminStateaddSecond,"
				+ " max(case when m.sector=6   then m.adminState else null end) as gammaadminStateaddSecond)"

                + " from RANDetail m where m.networkElement.isDeleted=0 and  upper(m.networkElement.networkElement.neName)=:neName and m.networkElement.neFrequency=:neFrequency and  "
                + "  upper(m.networkElement.neStatus)=:neStatus and m.networkElement.networkElement is not null and m.neBandDetail.carrier='SECOND' ";

    }

    /**
     * Gets site summary overview by band.
     *
     * @param neName the ne name
     * @param neFrequency the ne frequency
     * @param neStatus the ne status
     * @return the site summary overview by band
     */
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
    }

    /**
     * Gets site summary overview data by band.
     *
     * @return the query site summary overview by band
     */
    private String getQueryAntennaParametersByBand() {

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

                /*
                 * + " max(case when m.sector=1 then m.feederCableLength else null end) as alphaFeederCableLength, " +
                 * " max(case when m.sector=2 then m.feederCableLength else null end) as betaFeederCableLength, " +
                 * " max(case when m.sector=3 then m.feederCableLength else null end) as gammaFeederCableLength, " +
                 * " max(case when m.sector=4 then m.feederCableLength else null end) as alphaFeederCableLengthadd, " +
                 * " max(case when m.sector=5 then m.feederCableLength else null end) as betaFeederCableLengthadd, " +
                 * " max(case when m.sector=6 then m.feederCableLength else null end) as gammaFeederCableLengthadd, "
                 * 
                 * + " max(case when m.sector=1 then m.opticCableLength else null end) as alphaOpticCableLength, " +
                 * " max(case when m.sector=2 then m.opticCableLength else null end) as betaOpticCableLength, " +
                 * " max(case when m.sector=3 then m.opticCableLength else null end) as gammaOpticCableLength, " +
                 * " max(case when m.sector=4 then m.opticCableLength else null end) as alphaOpticCableLengthadd, " +
                 * " max(case when m.sector=5 then m.opticCableLength else null end) as betaOpticCableLengthadd, " +
                 * " max(case when m.sector=6 then m.opticCableLength else null end) as gammaOpticCableLengthadd, "
                 */

                + " max(case when m.sector=1 then m.sector else null end) as alphaSectorId, "
                + " max(case when m.sector=2 then m.sector else null end) as betaSectorId, "
                + " max(case when m.sector=3 then m.sector else null end) as gammaSectorId,"
                + " max(case when m.sector=4 then m.sector else null end) as alphaSectorIdadd,"
                + " max(case when m.sector=5 then m.sector else null end) as betaSectorIdadd,"
                + " max(case when m.sector=6 then m.sector else null end) as gammaSectorIdadd ) "
                + " from RANDetail m where m.networkElement.isDeleted=0 and  upper(m.networkElement.networkElement.neName)=:neName and m.networkElement.neFrequency=:neFrequency and "
                + "  upper(m.networkElement.neStatus)=:neStatus and m.networkElement.networkElement is not null and m.neBandDetail.carrier='FIRST'";

    }

    @Override
    public List<RANDetail> getSectorPropertyData(String neName, String neFrequency, String neStatus,String neType) {
        logger.info("Going to Get sector info Data for neName {}", neName);
        List<RANDetail> ranDetailDataList = new ArrayList<>();
        try {
            Query query = getEntityManager().createNamedQuery("getSectorPropertyData");
            query.setParameter(InfraConstants.NE_NAME, neName.toUpperCase());
            query.setParameter(InfraConstants.NE_FREQUENCY, neFrequency);
            query.setParameter(InfraConstants.NE_STATUS, NEStatus.valueOf(neStatus.toUpperCase()));
            query.setParameter(InfraConstants.NE_TYPE, NEType.valueOf(neType.toUpperCase()));
            ranDetailDataList = query.getResultList();
        } catch (NoResultException noResultException) {
            logger.error("No records Found from NetworkElement. {}", Utils.getStackTrace(noResultException));
        } catch (NonUniqueResultException nonUniqueResultException) {
            logger.error("No Unique records Found for NetworkElement. {}",
                    Utils.getStackTrace(nonUniqueResultException));
        } catch (Exception exception) {
            logger.error("Exception in getting data for NetworkElement. : {}" + Utils.getStackTrace(exception));
        }
        return ranDetailDataList;
    }

    /**Method for Customer Care Module*/
    @SuppressWarnings("rawtypes")
    @Override
	public List<Object[]> getBtsDetailByCgi(String cgi) {
		logger.info("Going to get bts Code by ecgi {}  ", cgi);
		List<Object[]> btsList = new ArrayList<>();
		try {
			if (cgi != null) {
				Map<String, List<String>> projection = new HashMap<>();
				InfraUtils.setProjectionList(projection, InfraConstants.NETWORKELEMENT_TABLE, InfraConstants.NE_NENAME_KEY,
						InfraConstants.NE_NESTATUS_KEY, InfraConstants.NE_NEFREQUENCY_KEY);
				InfraUtils.setProjectionList(projection, InfraConstants.RANDETAIL_TABLE, InfraConstants.NETWORKELEMENT_PCI_KEY);
				/**
				 * setProjectionsList(projection, "NELocation", "address"); -- For site address
				 * if have
				 */
				Map<String, List<Map>> filterMap = new HashMap<>();
				List<Map> neFilterList = new ArrayList<>();
				InfraUtils.setFilterMap(filterMap, neFilterList, InfraConstants.RANDETAIL_TABLE, InfraConstants.CGI_KEY, cgi,
						InfraConstants.INTEGER_DATATYPE_KEY, InfraConstants.EQUALS_OPERATOR);
				List<Tuple> tuples = getCellsDataForRAN(null, filterMap, projection, false);
				if (tuples != null && !tuples.isEmpty()) {
					Tuple t1 = tuples.get(0);
					Object[] object = new Object[5];
					object[0] = t1.get(InfraConstants.NE_NENAME_KEY);
					/** object[1] = t1.get("address") */
					object[2] = t1.get(InfraConstants.NE_NESTATUS_KEY);
					object[3] = t1.get(InfraConstants.NETWORKELEMENT_PCI_KEY);
					object[4] = t1.get(InfraConstants.NE_NEFREQUENCY_KEY);
					btsList.add(object);
					logger.info("BTS list by cgi : {}", btsList.size());
				}
			}
		} catch (Exception exception) {
			logger.error("Error in fetching bts Code by ecgi : {} ", Utils.getStackTrace(exception));
		}
		return btsList;
	}


    /**Method for Customer Care Module*/
    @SuppressWarnings({"rawtypes"})
    @Override    
	public List<Object[]> findAllOnairSites(String neStatus, List<String> neTypeList, List<String> domainList) {
		logger.info("Going to fetch All Onair Sites for neStatus : {} neTypeList : {} and domain : {} ", neStatus, neTypeList, domainList);
		List<Object[]> macroSiteDetails = new ArrayList<>();
		try {
			Map<String, List<String>> projection = new HashMap<>();
			InfraUtils.setProjectionList(projection, InfraConstants.NETWORKELEMENT_TABLE, InfraConstants.ID, InfraConstants.NE_CELLNUM_KEY,
					InfraConstants.NE_NEID_KEY, InfraConstants.NE_NEFREQUENCY_KEY, InfraConstants.NE_LATITUDE_KEY, InfraConstants.NE_LONGITUDE_KEY,
					InfraConstants.NE_NESTATUS_KEY, InfraConstants.NE_DOMAIN_KEY, InfraConstants.NE_VENDOR_KEY);
			InfraUtils.setProjectionList(projection, InfraConstants.RANDETAIL_TABLE, InfraConstants.AZIMUTH_KEY,
					InfraConstants.NETWORKELEMENT_PCI_KEY, InfraConstants.SECTOR_KEY);
			InfraUtils.setProjectionList(projection, InfraConstants.NEBANDDETAIL_TABLE, InfraConstants.NE_CARRIER_KEY);

			Map<String, List<Map>> filterMap = new HashMap<>();
			List<Map> neFilterList = new ArrayList<>();
			InfraUtils.setFilterMap(filterMap, neFilterList, InfraConstants.NETWORKELEMENT_TABLE, InfraConstants.NE_NETYPE_KEY, neTypeList,
					InfraConstants.NETYPE_ENUM_KEY, InfraConstants.IN_OPERATOR);
			InfraUtils.setFilterMap(filterMap, neFilterList, InfraConstants.NETWORKELEMENT_TABLE, InfraConstants.NE_NESTATUS_KEY, neStatus,
					InfraConstants.NESTATUS_ENUM_KEY, InfraConstants.EQUALS_OPERATOR);
			InfraUtils.setFilterMap(filterMap, neFilterList, InfraConstants.NETWORKELEMENT_TABLE, InfraConstants.NE_DOMAIN_KEY, domainList,
					InfraConstants.DOMAIN_ENUM_KEY, InfraConstants.IN_OPERATOR);

			List<Tuple> tuples = getCellsDataForRAN(null, filterMap, projection, false);

			setDatafromTuple(macroSiteDetails, tuples);
			logger.info("All NE Cell list size : {}", macroSiteDetails.size());
		} catch (Exception exception) {
			logger.error("Unable to fetch All Onair Sites Exception {} ", Utils.getStackTrace(exception));
		}
		return macroSiteDetails;
	}

	private void setDatafromTuple(List<Object[]> macroSiteDetails, List<Tuple> tuples) {
		if (tuples != null && !tuples.isEmpty()) {
			for (Tuple tuple : tuples) {
				Object[] object = new Object[15];
				object[0] = tuple.get(InfraConstants.NE_PARENT_ID_KEY);
				object[1] = tuple.get(InfraConstants.NE_CELLNUM_KEY);
				object[2] = tuple.get(InfraConstants.AZIMUTH_KEY);
				object[3] = tuple.get(InfraConstants.NETWORKELEMENT_PCI_KEY);
				object[4] = tuple.get(InfraConstants.NE_NEID_KEY);
				object[5] = tuple.get(InfraConstants.NE_NEFREQUENCY_KEY);
				object[6] = tuple.get(InfraConstants.NE_CARRIER_KEY);
				object[7] = tuple.get(InfraConstants.SECTOR_KEY);
				object[8] = tuple.get(InfraConstants.NE_LATITUDE_KEY);
				object[9] = tuple.get(InfraConstants.NE_LONGITUDE_KEY);
				object[10] = tuple.get(InfraConstants.NE_NESTATUS_KEY);
				object[11] = tuple.get(InfraConstants.NE_DOMAIN_KEY);
				object[12] = tuple.get(InfraConstants.VENDOR_KEY);
				object[13] = tuple.get(InfraConstants.ID);
				object[14] = tuple.get(InfraConstants.NE_PARENT_NEID_KEY);
				
				macroSiteDetails.add(object);
			}
		}
	}

    /**Method for Customer Care Module*/
    @Override
    @SuppressWarnings({"rawtypes"})
	public List<Object[]> getBtsNameByEcgi(String ecgi) {
		logger.info("Going to get bts name by ecgi : {}", ecgi);
		List<Object[]> list = new ArrayList<>();
		try {
			Map<String, List<String>> projection = new HashMap<>();
			InfraUtils.setProjectionList(projection, InfraConstants.NETWORKELEMENT_TABLE, InfraConstants.NE_NENAME_KEY,
					InfraConstants.NE_NEFREQUENCY_KEY);
			InfraUtils.setProjectionList(projection, InfraConstants.RANDETAIL_TABLE, InfraConstants.NETWORKELEMENT_PCI_KEY);
			Map<String, List<Map>> filterMap = new HashMap<>();
			List<Map> neFilterList = new ArrayList<>();
			InfraUtils.setFilterMap(filterMap, neFilterList, InfraConstants.NETWORKELEMENT_TABLE, InfraConstants.ECGI, ecgi,
					InfraConstants.STRING_DATATYPE_KEY, InfraConstants.EQUALS_OPERATOR);
			List<Tuple> tuples = getCellsDataForRAN(null, filterMap, projection, false);
			if (tuples != null && !tuples.isEmpty()) {
				Tuple t1 = tuples.get(0);
				Object[] object = new Object[3];
				object[0] = t1.get(InfraConstants.NE_NENAME_KEY);
				object[1] = t1.get(InfraConstants.NETWORKELEMENT_PCI_KEY);
				object[2] = t1.get(InfraConstants.NE_NEFREQUENCY_KEY);
				list.add(object);
			}
			logger.info("Bts list by ecgi : {}", list.size());
		} catch (Exception ex) {
			logger.error("Error in getting bts name by ecgi :{} ,  Exception : {} ", ecgi, Utils.getStackTrace(ex));
		}
		return list;
	}

    @Override
    public RANDetail searchByRrhSerialNo(String rrhSerialNo) {
        logger.info("inside searchByRrhSerialNo method and rrhSerialNo :{}", rrhSerialNo);
        RANDetail rnDetail = null;
        try {
            TypedQuery<RANDetail> query = getEntityManager().createNamedQuery("searchByRrhSerialNo", RANDetail.class);
            query.setParameter("rrhSerialNo", rrhSerialNo);
            rnDetail = query.getSingleResult();
        }catch (NoResultException e) {
            logger.error("error while getting count from DB :{}", ExceptionUtils.getMessage(e));
        }
        catch (Exception e) {
            logger.error("error while getting count from DB :{}", ExceptionUtils.getStackTrace(e));
        }
        return rnDetail;

    }
    @Override
    public RANDetail getRanDetailByNename(String nename) {
        logger.info("inside getRanDetailByNename method and nename :{}", nename);
        RANDetail rnDetail = null;
        try {
            TypedQuery<RANDetail> query = getEntityManager().createNamedQuery("getRANDetailBynename", RANDetail.class);
            query.setParameter("nename", nename);
            rnDetail = query.getSingleResult();
        } catch (Exception e) {
            logger.error("error while getting Randetail from DB :{}", ExceptionUtils.getStackTrace(e));
        }
        return rnDetail;

    }

	@Override
	public WifiWrapper getWIFIAPDetail(String macAddress) {
		logger.info("inside getWIFIAPDetail method and macaddress :{}", macAddress);
		Query query=null;
		WifiWrapper wrapper=null;
		try {
		query = getEntityManager().createNamedQuery("getRANDetailByMacaddress");
		query.setParameter("macaddress", macAddress);

		wrapper=(WifiWrapper) query.getSingleResult();
		
		return wrapper;
		}
		catch(Exception e) {
			logger.error("error while getting WIFIAPDetail from DB :{}", ExceptionUtils.getStackTrace(e));
		}
		return wrapper;
	}

}

package com.inn.foresight.core.infra.rest.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.inn.commons.lang.CommonUtils;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.rest.AbstractCXFRestService;
import com.inn.core.generic.service.IGenericService;
import com.inn.core.generic.utils.ExceptionUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.model.MacroSiteDetail;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.foresight.core.infra.service.IMacroSiteDetailService;
import com.inn.foresight.core.infra.service.INEReportService;
import com.inn.foresight.core.infra.service.INETaskDetailService;
import com.inn.foresight.core.infra.service.INEVisualizationService;
import com.inn.foresight.core.infra.service.INetworkElementService;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.wrapper.KPISummaryDataWrapper;
import com.inn.foresight.core.infra.wrapper.MacroSiteDetailWrapper;
import com.inn.foresight.core.infra.wrapper.NETaskDetailWrapper;
import com.inn.foresight.core.infra.wrapper.SectorSummaryWrapper;
import com.inn.foresight.core.infra.wrapper.SiteConnectionPointWrapper;
import com.inn.foresight.core.infra.wrapper.SiteEmsDetailsWrapper;
import com.inn.foresight.core.infra.wrapper.SiteGeographicalDetail;
import com.inn.foresight.core.infra.wrapper.SiteLayerSelection;
import com.inn.foresight.core.infra.wrapper.SiteMileStoneDetailWrapper;
import com.inn.foresight.core.infra.wrapper.SiteSelectionWrapper;
import com.inn.foresight.core.infra.wrapper.WifiWrapper;


/**
 * The Class NEVisualizationRestImpl.
 */
@Path("NEVisualization")
@Produces("application/json")
@Consumes("application/json")
public class NEVisualizationRestImpl extends AbstractCXFRestService<Integer, Object> {

	/**
	 * Instantiates a new NE visualization rest impl.
	 */
	public NEVisualizationRestImpl() {
	super(Object.class);
    }
	/** The logger. */
	private Logger logger = LogManager.getLogger(NEVisualizationRestImpl.class);

	private static final String NE_VISUALIZATION_REST_IMPL="NEVisualizationRestImpl";
	/** The service. */
	@Autowired
	private INEVisualizationService neVisualizationService;
	
	@Autowired
	private INetworkElementService iNetworkElementService;
	
	
	@Autowired(required=false)
	private INEReportService ineReportService;

	@Autowired
	private INETaskDetailService ineTaskDetailService;
	
	@Autowired 
	private IMacroSiteDetailService imacrositedetail;
	
	/**
	 * Search combined sites.
	 *
	 * @param siteSelectionWrapper the site selection wrapper
	 * @param southWestLong the south west long
	 * @param southWestLat the south west lat
	 * @param northEastLong the north east long
	 * @param northEastLat the north east lat
	 * @param zoomLevel the zoom level
	 * @return the map
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("searchCombinedSites")
	public Map searchCombinedSites(SiteSelectionWrapper siteSelectionWrapper, @QueryParam("SWLng") Double southWestLong, @QueryParam("SWLat") Double southWestLat,
			@QueryParam("NELng") Double northEastLong, @QueryParam("NELat") Double northEastLat, @QueryParam("zoomLevel") Integer zoomLevel) {
		Map neDataMap = new HashMap();
		try {
			if (southWestLong != null && southWestLat != null && northEastLong != null && northEastLat != null) {
				logger.info("Going to get combined Sites zoomLevel={} , siteSelectionWrapper={} ", zoomLevel, siteSelectionWrapper);
				return neVisualizationService.getSiteCountForLayerVisualisation(siteSelectionWrapper, southWestLong, southWestLat, northEastLong, northEastLat, zoomLevel);
			} else {
				throw new RestException(ForesightConstants.INVALID_PARAMETERS);
			}
		} catch (DaoException | RestException e) {
			throw new RestException(ExceptionUtil.generateExceptionCode("Rest", NE_VISUALIZATION_REST_IMPL, e));
		} catch (Exception e) {
			logger.error("error in searchCombinedSites, err = {}", Utils.getStackTrace(e));
		}
		return neDataMap;
	}

	/**
	 * Gets the ems detail data for sites.
	 *
	 * @param sapid the sapid
	 * @return the ems detail data for sites
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("getEmsDetailDataForSites")
	public SiteEmsDetailsWrapper getEmsDetailDataForSites(@QueryParam("neName") String neName){
		logger.info("Going to get EmsDetails For Sites {} " , neName);
		try{
			if(neName !=null){
				return neVisualizationService.getEmsDetailDataForSites(neName);
			}else {
				throw new RestException("Invalid Parameteres");
			}
		}catch(Exception exception){
			logger.error("Unable to get EmsDetails For Sites {} Exception {} " , neName , Utils.getStackTrace(exception));
			throw new RestException(exception.getMessage());
		}
	}

	/**
	 * Gets the site overview detail data.
	 *
	 * @param sapid the sapid
	 * @param band the band
	 * @return the site overview detail data
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("getSiteOverviewDetailData")
	public SiteGeographicalDetail getSiteOverviewDetailData(@QueryParam("sapid") String sapid, @QueryParam("band") String band) {
		logger.info("get Site Overview Detail Data For sapid {} band {} ",sapid ,band);
		try{
			if(sapid != null && band.equalsIgnoreCase(InfraConstants.STRING_ALL))
				return neVisualizationService.getSiteOverviewDetailData(sapid);
			else{
				throw new RestException(ForesightConstants.EXCEPTION_INVALID_PARAMS);
			}
		}catch (RestException e) {
			throw new RestException(ExceptionUtil.generateExceptionCode("Rest", NE_VISUALIZATION_REST_IMPL, e));
		}catch(Exception exception){
			logger.error(" Unable to get SiteOverviewDetails Exception {} " , Utils.getStackTrace(exception));
			throw new RestException(exception.getMessage());
		}
	}

	/**
	 * Gets the site overview detail by band.
	 *
	 * @param neName the ne name
	 * @param neFrequency the ne frequency
	 * @return the site overview detail by band
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("getSiteOverviewDetailByBand")
	public  Map<String, List<SectorSummaryWrapper>> getSiteOverviewDetailByBand(@QueryParam("neName") String neName, @QueryParam("neFrequency") String neFrequency) {
		logger.info("get Site Overview Detail Data For neName {} neFrequency {} ",neName ,neFrequency);
		Map<String, List<SectorSummaryWrapper>>  map = new HashMap<>();
		try{
			if(neName != null && neFrequency!=null && !neFrequency.equalsIgnoreCase("ALL"))
				map = neVisualizationService.getSiteSummaryOverviewByBand(neName, neFrequency,ForesightConstants.ONAIR_UPPERCASE);
			else{
				throw new RestException(ForesightConstants.EXCEPTION_INVALID_PARAMS);
			}
		}catch(RestException exception){
			logger.error("Unable to get SiteOverviewDetails Exception  {} " , Utils.getStackTrace(exception));
			throw new RestException(exception.getGuiMessage());
		}catch(Exception exception){
			logger.error("Unable to get SiteOverviewDetails Exception {} " , Utils.getStackTrace(exception));
			throw new RestException(exception.getMessage());
		}
		return map;
	}

	/**
	 * Gets the site summary antenna parameters by band.
	 *
	 * @param neName the ne name
	 * @param neFrequency the ne frequency
	 * @return the site summary antenna parameters by band
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("getSiteSummaryAntennaParametersByBand")
	public  Map<String, List<SectorSummaryWrapper>> getSiteSummaryAntennaParametersByBand(@QueryParam("neName") String neName, @QueryParam("neFrequency") String neFrequency) {
		logger.info("get Site Summary Antenna Parameters For neName {} neFrequency {} ",neName ,neFrequency);
		Map<String, List<SectorSummaryWrapper>>  map = new HashMap<>();
		try{
			if(neName != null && neFrequency != null && !neFrequency.equalsIgnoreCase("ALL"))
				map = neVisualizationService.getSiteSummaryAntennaParametersByBand(neName, neFrequency,"ONAIR");
			else{
				throw new RestException(ForesightConstants.EXCEPTION_INVALID_PARAMS);
			}
		}catch(RestException exception){
			logger.error("Unable to get Site Summary Antenna Parameters Exception {} " , Utils.getStackTrace(exception));
			throw new RestException(exception.getGuiMessage());
		}catch(Exception exception){
			logger.error("Unable to get Site Summary Antenna Parameters Exception {} " , Utils.getStackTrace(exception));
			throw new RestException(exception.getMessage());
		}
		return map;
	}

	@POST
	@Path("getSitesByGeography")
	public List<NetworkElement> getNetworkelementByGeographyLevel(SiteSelectionWrapper siteSelectionWrapper) {
		try {
			if (siteSelectionWrapper != null) {
				return neVisualizationService.getNetworkelementByGeographyLevel(siteSelectionWrapper);
			} else {
				throw new RestException(InfraConstants.INVALID_PARAMETERS);
			}
		} catch (Exception exception) {
			logger.error("Error in getting Networkelement by geography level Exception : {} ", Utils.getStackTrace(exception));
			throw new RestException(InfraConstants.INVALID_PARAMETERS);
		}
	}
	@POST
	@Path("getSitesByGeographyList")
	public List<NetworkElement> getSitesByGeography(SiteSelectionWrapper siteSelectionWrapper) {
		try {
			if (siteSelectionWrapper != null) {
				return neVisualizationService.getNetworkelementByGeographyLevel(siteSelectionWrapper);
			} else {
				throw new RestException(InfraConstants.INVALID_PARAMETERS);
			}
		} catch (Exception exception) {
			logger.error("Error in getting Networkelement by geography level Exception : {} ", Utils.getStackTrace(exception));
			throw new RestException(InfraConstants.INVALID_PARAMETERS);
		}
	}
	/**
	 * Gets the site geographic detail data.
	 *
	 * @param neName the ne name
	 * @return the site geographic detail data
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("getSiteGeographicDetailData")
	public SiteGeographicalDetail getSiteGeographicDetailData(@QueryParam("neName") String neName,@QueryParam("neType") String neType) {
		logger.info("Going to  SiteGeographicDetail data For neName {} ",neName );
		try{
			if(neName != null)
				return neVisualizationService.getSiteGeographicDetailData(neName,neType);
			else{
				throw new RestException(ForesightConstants.INVALID_PARAMETERS);
			}
		}catch (RestException restException) {
			throw new RestException(ExceptionUtil.generateExceptionCode("Rest", NE_VISUALIZATION_REST_IMPL, restException));
		}catch(Exception exception){
			logger.error("Unable to get SiteGeographicDetail data Exception {} " , Utils.getStackTrace(exception));
			throw new RestException(exception.getMessage());
		}
	}

	/**
	 * Search.
	 *
	 * @param entity the entity
	 * @return the list
	 * @throws RestException the rest exception
	 */
	@Override
	public List<Object> search(Object entity) {
	    return new ArrayList<Object>();
	}

	/**
	 * Find by id.
	 *
	 * @param primaryKey the primary key
	 * @return the object
	 * @throws RestException the rest exception
	 */
	@Override
	public Object findById(@NotNull Integer primaryKey) {
	    return new Object();
	}

	/**
	 * Find all.
	 *
	 * @return the list
	 * @throws RestException the rest exception
	 */
	@Override
	public List<Object> findAll() {
		 return new ArrayList<Object>();
	}

	/**
	 * Creates the.
	 *
	 * @param anEntity the an entity
	 * @return the object
	 * @throws RestException the rest exception
	 * @throws RestException the rest exception
	 */
	@Override
	public Object create(@Valid Object anEntity) {
	    return new Object();
	}

	/**
	 * Update.
	 *
	 * @param anEntity the an entity
	 * @return the object
	 * @throws RestException the rest exception
	 */
	@Override
	public Object update(@Valid Object anEntity) {
	    return new Object();
	}

	/**
	 * Removes the.
	 *
	 * @param anEntity the an entity
	 * @return true, if successful
	 * @throws RestException the rest exception
	 */
	@Override
	public boolean remove(@Valid Object anEntity) {
	    return false;
	}

	/**
	 * Removes the by id.
	 *
	 * @param primaryKey the primary key
	 * @throws RestException the rest exception
	 */
	@Override
	public void removeById(@NotNull Integer primaryKey) {
		//For removing by id
	}

	/**
	 * Gets the service.
	 *
	 * @return the service
	 */
	@Override
	public IGenericService<Integer, Object> getService() {
	    return null;
	}

	/**
	 * Gets the search context.
	 *
	 * @return the search context
	 */
	@Override
	public SearchContext getSearchContext() {
	    return null;
	}


	@POST
	@Path("kpiSummaryDetail")
	public Map<String, Long> kpiSummaryDetail(KPISummaryDataWrapper filterConfiguration, @QueryParam("SWLng") Double southWestLong, @QueryParam("SWLat") Double southWestLat,
			@QueryParam("NELng") Double northEastLong, @QueryParam("NELat") Double northEastLat, @QueryParam("zoomLevel") Integer zoomLevel) {
		logger.info("Going to get dashboard Search data filterConfiguration {} , zoomLevel {} , southWestLong {} , southWestLat {} , northEastLong {} , northEastLat {},  ", filterConfiguration,
				zoomLevel, southWestLong, southWestLat, northEastLong, northEastLat);
		if (filterConfiguration != null && filterConfiguration.getGeographyList() != null && !filterConfiguration.getGeographyList().isEmpty()) {
			return neVisualizationService.getCustonmCountForKPI(filterConfiguration);
		}
		if (filterConfiguration != null && filterConfiguration.getPolyList() != null && !filterConfiguration.getPolyList().isEmpty()) {
			logger.info("Going to get count of polygon for KPI for filterConfiguration {}", filterConfiguration);
			return neVisualizationService.getKPICountForPolygon(filterConfiguration, zoomLevel);
		} else {
			if (filterConfiguration != null && southWestLong != null && southWestLat != null && northEastLong != null && northEastLat != null) {
				return neVisualizationService.getsiteCountsForKPI(southWestLong, southWestLat, northEastLong, northEastLat, zoomLevel, filterConfiguration);
			} else {
				return new HashMap<>();
			}
		}
	}

	@GET
	@Path("getSiteSummaryRadioParametersByBand")
	public  Map<String, List<SectorSummaryWrapper>> getSiteSummaryRadioParametersByBand(@QueryParam("neName") String neName, @QueryParam("neFrequency") String neFrequency) {
		logger.info("get Site Summary Radio Parameters For neName {} neFrequency {} ",neName ,neFrequency);
		Map<String, List<SectorSummaryWrapper>>  map = new HashMap<>();
		try{
			if(neName != null && neFrequency != null && !neFrequency.equalsIgnoreCase("ALL"))
				map = neVisualizationService.getSiteSummaryRadioParametersByBand(neName, neFrequency,"ONAIR");
			else{
				throw new RestException("Invalid parameters");
			}
		}catch(RestException exception){
			logger.error("Unable to get Site Summary Radio Parameters Exception {} " , Utils.getStackTrace(exception));
			throw new RestException(exception.getGuiMessage());
		}catch(Exception exception){
			logger.error("Unable to get Site Summary Radio Parameters Exception {} " , Utils.getStackTrace(exception));
			throw new RestException(exception.getMessage());
		}
		return map;
	}
	@GET
	@Path("getSectorPropertyDataByBand")
	public SiteGeographicalDetail getSectorPropertyDataByBand(@QueryParam("neName") String neName, @QueryParam("neFrequency") String neFrequency,@QueryParam("neStatus") String neStatus,@QueryParam("neType") String neType) {
		logger.info("get Sector Property Data For neName {} neFrequency {} ",neName ,neFrequency);
		SiteGeographicalDetail siteGeographicalDetail = new SiteGeographicalDetail();
		try{
			if(neName != null && neFrequency != null)
				siteGeographicalDetail = neVisualizationService.getSectorPropertyDataByBand(neName, neFrequency, neStatus,neType);
			else{
				throw new RestException("Invalid parameters");
			}
		}catch(RestException exception){
			logger.error("Unable to get Sector Property Data Exception {} " , Utils.getStackTrace(exception));
			throw new RestException(exception.getGuiMessage());
		}catch(Exception exception){
			logger.error("Unable to get Sector Property Data Exception {} " , Utils.getStackTrace(exception));
			throw new RestException(exception.getMessage());
		}
		return siteGeographicalDetail;
	}

	@POST
	@Path("getMacroSiteDetails")
	public List<MacroSiteDetail> getMacroSiteDetails(MacroSiteDetailWrapper wrapper){
		try{
			if(wrapper != null){
			return neVisualizationService.getMacroSiteDetails(wrapper);
			} else {
				throw new RestException(ForesightConstants.INVALID_PARAMETER);
			}
		} catch (Exception e){
			logger.error("Exception Occoured In Processing Request {} ",Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
	
	@GET
	@Path("getWifiDetailByNEId/{neId}")
	public WifiWrapper getWifiDetailByNEId(@PathParam("neId") String neId) {
		if (CommonUtils.isAnyNull(neId)) {
			throw new RestException(ForesightConstants.INVALID_PARAMETER);
		}
		return neVisualizationService.getWifiDetailByNEId(neId);
	}
	@POST
	@Path("getSitesCountByGeographyLevel")
	public List getSitesCountByGeographyLevel(SiteSelectionWrapper siteSelectionWrapper, @QueryParam("SWLng") Double southWestLong, @QueryParam("SWLat") Double southWestLat,
			@QueryParam("NELng") Double northEastLong, @QueryParam("NELat") Double northEastLat, @QueryParam("zoomLevel") Integer zoomLevel) {
		List geographyList= new ArrayList();
		try {
				if(southWestLong != null && southWestLat != null && northEastLong != null && northEastLat != null) {
					logger.info("Going to get combined Sites zoomLevel={} , siteSelectionWrapper={} ",zoomLevel,siteSelectionWrapper);
					return neVisualizationService.getAggregatedCountByGeographyLevel(siteSelectionWrapper, southWestLong, southWestLat, northEastLong, northEastLat, zoomLevel);
				}else {
					throw new RestException(ForesightConstants.INVALID_PARAMETERS);
				}
			} catch (RestException e) {
			throw new RestException(ExceptionUtil.generateExceptionCode("Rest", NE_VISUALIZATION_REST_IMPL, e));
		} catch (Exception e) {
			logger.error("error in searchCombinedSites, err = {}", Utils.getStackTrace(e));
		}
		return geographyList;
	}
	
	@GET
	@Path("getSiteMilestoneDetails")
	public List<SiteMileStoneDetailWrapper> getSiteMilestoneDetails(@QueryParam("neName") String neName){
		try {
			if(neName!=null) {
				return neVisualizationService.getSiteMilestoneDetails(neName);
			} else {
				throw new RestException(ForesightConstants.INVALID_PARAMETERS);
			}
		}catch(Exception exception) {
			throw new RestException(exception.getMessage());
		}
	}
	@GET
	@Path("getSectorPropertyReport")
	public Map<String, String> getSectorPropertyReport(@QueryParam("neName") String neName,
			@QueryParam("bandFirst") Boolean bandFirst, @QueryParam("bandSecond") Boolean bandSecond,
			@QueryParam("bandThird") Boolean bandThird,@QueryParam("name") String name,@QueryParam("type") String type,@QueryParam("neType") String neType) {
		logger.info("Into the method for creating the report for sector property");
		Map<String, String> fileMap = new HashMap<>();
		try {
			fileMap = null;//ineReportService.getSitePropertyReport(neName, bandFirst, bandSecond, bandThird,name,type,neType);
		} catch (RestException e) {
			throw new RestException(ExceptionUtil.generateExceptionCode("Rest", NE_VISUALIZATION_REST_IMPL, e));
		} catch (Exception e) {
			logger.error("Error in creating the report {}  ", e.getStackTrace());
		}
		return fileMap;
	}
	
	/*@GET
	@Path("getSectorPropertyReportByBand")
	public Map<String, String> getSectorPropertyReportByBand(List<Map<String,SiteLayerSelection>> list,@QueryParam("name") String name,@QueryParam("type") String type) {
		logger.info("Into the method for creating the report for sector property");
		Map<String, String> fileMap = new HashMap<>();
		try {
			fileMap = ineReportService.getSectorPropertyReport(list,name,type);
		} catch (RestException e) {
			throw new RestException(ExceptionUtil.generateExceptionCode("Rest", NE_VISUALIZATION_REST_IMPL, e));
		} catch (Exception e) {
			logger.error("Error in creating the report {} ", e.getStackTrace());
		}
		return fileMap;
	}*/
	
	@GET
	@Path("getDistinctStagesForPlannedSites")
	public List<String> getDistinctStagesForPlannedSites(){
		try {
		//return ineTaskDetailService.getDistinctStagesForPlannedSites();
		return new ArrayList<String>();
		}catch(Exception exception) {
			throw new RestException(exception.getMessage());
		}
	}
	
	@GET
	@Path("getDistinctVendorForSites")
	public List<String> getDistinctVendor() {
		logger.info("Going to get distinct Vendor For Sites");
		try {
			return neVisualizationService.getDistinctVendor();
		} catch (Exception exception) {
			throw new RestException(exception.getMessage());
		}
	}

	@GET
	@Path("getDistinctMorphologyForSites")
	public List<String> getDistinctMorphology() {
		logger.info("Going to get distinct Morphology For Sites");
		try {
			return iNetworkElementService.getDistinctMorphology();
		} catch (Exception exception) {
			throw new RestException(exception.getMessage());
		}
	}
	@GET
	@Path("getFileByName")
	@Produces("application/excel")
	public Response getFileByPath(@QueryParam("path") String fileName) {
		logger.info("Going to get file by name " + fileName);
		ResponseBuilder response = null;
		try {
			response = Response.ok(ineReportService.getFileByPath(fileName));
			response.header("fileName",fileName);
		} catch (Exception e) {
			logger.error("Error in getting file for Site Exception {} ",e.getStackTrace());
			throw new RestException("Error In Getting File For SitePlan.");
		}
		return response.build();
	}
	@GET
	@Path("getDistinctDomainForSites")
	public List<String> getDistinctDomain() {
		logger.info("Going to get distinct Domains For Sites");
		try {
			return neVisualizationService.getDistinctDomain();
		} catch (Exception exception) {
			throw new RestException(exception.getMessage());
		}
	}
	@POST
	@Path("getDistinctVendorByDomain")
	public List<String> getDistinctVendorByDomain(List<String> domainList) {
		logger.info("Going to get distinct Vendor By Domain.");
		try {
			if(domainList != null && !domainList.isEmpty()) {
			return neVisualizationService.getDistinctVendorByDomain(domainList);
			}else {
				logger.info("domain cann't be null");
			}
		} catch (Exception exception) {
			throw new RestException(exception.getMessage());
		}
		return new ArrayList<String>();
	}
	
	
	@POST
	@Path("getTechnologyByVendor")
	public List<String> getTechnologyByVendor(List<String> vendorList) {
		logger.info("Going to get distinct Technology For Sites");
		try {
			if(vendorList != null && !vendorList.isEmpty()) {
			return neVisualizationService.getTechnologyByVendor(vendorList);
			}else {
				logger.info("vendor cann't be null");
			}
		} catch (Exception exception) {
			throw new RestException(exception.getMessage());
		}
		return new ArrayList<String>();
	}
	
	@POST
	@Path("getVendorsByType")
	public List<String> getVendorsByType(List<String> neType) {
		logger.info("Going to get Vendors for {} Sites.",neType);
		try {
			if(neType != null && !neType.isEmpty()) {
			return neVisualizationService.getVendorsByType(neType);
			}else {
				logger.info("neType cann't be null");
			}
		} catch (Exception exception) {
			throw new RestException(exception.getMessage());
		}
		return new ArrayList<String>();
	}
	@GET
	@Path("getSiteOverviewData")
	public SiteGeographicalDetail getSiteOverviewData(@QueryParam("neName") String neName,@QueryParam("neType") String neType) {
		logger.info("get Site Overview Detail Data For neName {} ",neName );
		try{
			if(neName != null)
				return neVisualizationService.getSiteOverviewData(neName,neType);
			else{
				throw new RestException(ForesightConstants.INVALID_PARAMETERS);
			}
		}catch (RestException restException) {
			throw new RestException(ExceptionUtil.generateExceptionCode("Rest", NE_VISUALIZATION_REST_IMPL, restException));
		}catch(Exception exception){
			logger.error("Unable to get SiteOverviewDetails Exception {} " , Utils.getStackTrace(exception));
			throw new RestException(exception.getMessage());
		}
	}
	@POST
	@Path("getSiteDetailReport")
	public Map<String, String> getSiteDetailReport(List<SiteLayerSelection> sitelayerselection, @QueryParam("SWLng") Double southWestLong, @QueryParam("SWLat") Double southWestLat,
			@QueryParam("NELng") Double northEastLong, @QueryParam("NELat") Double northEastLat, @QueryParam("zoomLevel") Integer zoomLevel) {
		logger.info("Into the method for creating the site detail report for Polygon.");
		Map<String, String> fileMap = new HashMap<>();
		try {
			if(sitelayerselection != null && !sitelayerselection.isEmpty()) {
				
			if (sitelayerselection.get(0) != null) {
				SiteLayerSelection filterConfiguration=sitelayerselection.get(0);
				fileMap=ineReportService.generateSiteDetailReport(southWestLong, southWestLat, northEastLong, northEastLat, filterConfiguration);
			}
		}
			
		} catch (RestException e) {
			throw new RestException(ExceptionUtil.generateExceptionCode("Rest", NE_VISUALIZATION_REST_IMPL, e));
		} catch (Exception e) {
			logger.error("Error in creating the report {} ", e.getStackTrace());
		}
		return fileMap;
	}
	@POST
	@Path("generateSectorDetailReport")
	public Map<String, String> generateSectorDetailReport(List<SiteLayerSelection> sitelayerselection, @QueryParam("SWLng") Double southWestLong, @QueryParam("SWLat") Double southWestLat,
			@QueryParam("NELng") Double northEastLong, @QueryParam("NELat") Double northEastLat, @QueryParam("zoomLevel") Integer zoomLevel) {
		logger.info("Into the method for creating the Sector detail report for Polygon.");
		Map<String, String> fileMap = new HashMap<>();
		try {
			if(sitelayerselection != null && !sitelayerselection.isEmpty()) {
				
			if (sitelayerselection.get(0) != null) {
				SiteLayerSelection filterConfiguration=sitelayerselection.get(0);
				fileMap=ineReportService.generateSectorDetailReport(southWestLong, southWestLat, northEastLong, northEastLat, filterConfiguration);
			}
		}
			
		} catch (RestException e) {
			throw new RestException(ExceptionUtil.generateExceptionCode("Rest", NE_VISUALIZATION_REST_IMPL, e));
		} catch (Exception e) {
			logger.error("Error in creating the report {} ", e.getStackTrace());
		}
		return fileMap;
	}
	@GET
	@Path("validateEnbid")
	public String validateEnbid(@QueryParam("neid") Integer enbId) {
		logger.info("Into the method for validate enbid");
		try {
		return imacrositedetail.validateEnbid(enbId);
		}
		catch(RestException re) {
			throw new RestException(re.getMessage()); }
	}
	
	@POST
	@Path("getNETaskDetailData")
	public List<NETaskDetailWrapper> getNETaskDetailData(SiteLayerSelection sitelayerselection) {
		logger.info("Into the method to get NE Task Detail Data.");
		try {
		return neVisualizationService.getNETaskDetailData(sitelayerselection.getFilters(), sitelayerselection.getProjection(), sitelayerselection.getIsGroupBy());
		}
		catch(RestException re) {
			throw new RestException(re.getMessage()); }
	}
	
	@GET
	@Path("getSiteConnectionPointDetails")
	public Response getSiteConnectionPointDetails(@QueryParam("neName") String neName,@QueryParam("neType") String neType) {
		logger.info("Going to get SiteConnectionPoint Details data for neName {} :, neType : {}",neName,neType);
		try{
			if(neName != null && neType!=null) {
				return Response.ok(new Gson().toJson(neVisualizationService.getSiteConnectionPointDetails(neName, neType))).build();
			}else{
				throw new RestException(ForesightConstants.INVALID_PARAMETERS);
			}
		}catch(Exception exception){
			logger.error("Unable to get SiteConnectionPoint Details data for neName {} :, neType : {}. Exception : {} " ,neName,neType,Utils.getStackTrace(exception));
			throw new RestException(exception.getMessage());
		}
	}
}

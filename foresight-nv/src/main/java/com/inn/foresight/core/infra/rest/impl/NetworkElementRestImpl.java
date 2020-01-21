package com.inn.foresight.core.infra.rest.impl;

import java.util.ArrayList;
import java.util.Collections;
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

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.rest.AbstractCXFRestService;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.foresight.core.infra.service.INETaskDetailService;
import com.inn.foresight.core.infra.service.INetworkElementService;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.wrapper.NetworkElementWrapper;

@Path("NetworkElement")
@Produces("application/json")
@Consumes("application/json")
public class NetworkElementRestImpl extends AbstractCXFRestService<Integer, NetworkElement>{

	/** The logger. */
	private Logger logger = LogManager.getLogger(NetworkElementRestImpl.class);
	
	public NetworkElementRestImpl() {
		super(NetworkElement.class);
	}
	
	@Autowired
	private INetworkElementService networkElementService;
	
	@Autowired
	private INETaskDetailService ineTaskDetailService;
	

	@Override
	public List<NetworkElement> search(NetworkElement entity) {
		return Collections.emptyList();
	}

	@Override
	public NetworkElement findById(@NotNull Integer primaryKey) {
		return null;
	}

	@Override
	public List<NetworkElement> findAll() {
		return Collections.emptyList();
	}

	@Override
	public NetworkElement create(@Valid NetworkElement anEntity) {
		return null;
	}

	@Override
	public NetworkElement update(@Valid NetworkElement anEntity) {
		return null;
	}

	@Override
	public boolean remove(@Valid NetworkElement anEntity) {
		return false;
	}

	@Override
	public void removeById(@NotNull Integer primaryKey) {
		//
	}

	@Override
	public IGenericService<Integer, NetworkElement> getService() {
		return null;
	}

	@Override
	public SearchContext getSearchContext() {
		return null;
	}
	
	@GET
	@Path("getNetworkElement/{geoType}/{geographyId}/{neType}")
	public List<Map<String,Object>> getNetworkElement(@PathParam("geoType") String geoType,@PathParam("geographyId") Integer geographyId,@PathParam("neType") String neType,@QueryParam("domain") String domain,
			@QueryParam("vendor") String vendor,@QueryParam("technology") String technology,@QueryParam("softwareVersion") String softwareVersion) {
		logger.info("Going to get NetworkElement by geoType {},geographyId {} and neType {}",geoType,geographyId,neType);
		return networkElementService.getNetworkElement(neType, geographyId, geoType, domain, vendor, technology, softwareVersion);
	}
	
	@GET
	@Path("getDistinctDataFromNetworkElement/{searchType}")
	public List<Object> getDistinctDataFromNetworkElement(@PathParam("searchType") String searchType,@QueryParam("domain") String domain,@QueryParam("vendor") String vendor,@QueryParam("technology") String technology,@QueryParam("softwareVersion") String softwareVersion) {
		logger.info("Going to get NetworkElement by searchType {}",searchType);
		return networkElementService.getDistinctDataFromNetworkElement(searchType, domain, vendor, technology, softwareVersion);
	}
	@GET
	@Path("getCountByGeoraphyAndVendor/{geographyType}/{geographyName}/{vendor}")
	public Map<NEType, Long> getCountByGeoraphyAndVendor(@PathParam("geographyType") String geographyType,@PathParam("geographyName") String geographyName,
			@PathParam("vendor")String vendor) {
		logger.info("Going to get NetworkElement by geographyType {},geographyName {} and vendor {}",geographyType,geographyName,vendor);
		return networkElementService.getCountByGeoraphyAndVendor(vendor,geographyName, geographyType );
	}
	
	@GET
	@Path("getSiteInfoBySapId")
	public NetworkElementWrapper getSiteInfoBySapId(@QueryParam("siteID") String siteID) {
		logger.info("getSiteInfoBySapId site id is {}",siteID);
		return networkElementService.getSiteInfoBySapId(siteID);
	}
	
	@GET
	@Path("getGeographyL4ByneName")
	public NetworkElementWrapper getGeographyL4BySapId(@QueryParam("siteID") String siteID) {
		logger.info("getGeographyL4BySapId site id is {}",siteID);
		try {
			if (siteID != null) {
				return networkElementService.getGeographyL4BySapId(siteID);
			} else {
				throw new RestException(ForesightConstants.INVALID_PARAMETERS);
			}
		} catch (Exception exception) {
			throw new RestException(exception.getMessage());
		}
	}
	

	@GET
	@Path("getNeFrequencyByDomainAndVendor/{domain}/{vendor}")
	public List<String> getNeFrequencyByDomainAndVendor(@PathParam("domain") String domain, @PathParam("vendor") String vendor ) {
		logger.info("getfrequency by domain {} and vendor {}",domain, vendor );
		return networkElementService.getNeFrequencyByDomainAndVendor(domain, vendor);
	}
	
	@GET
	@Path("getNEDetailsByNEId")
	public NetworkElementWrapper getNEDetailsByNEId(@QueryParam("neId") String neId, @QueryParam("key") String isEffectedNodes) {
		try {
			if (neId != null) {
				return networkElementService.getNEDetailsByNEId(neId, isEffectedNodes);
			} else {
				throw new RestException(ForesightConstants.INVALID_PARAMETERS);
			}
		} catch (Exception exception) {
			throw new RestException(exception.getMessage());
		}
	}
	
	@GET
	@Path("getSapIdsByGeographyL4/{name}")
	public List<String> getSapIdsByGeographyL4(@PathParam("name") String name) {
		logger.info("Going to get distinct sapids By GeographyL4 name {}",name);
		return networkElementService.getSapidsByGeographyL4(name);
	}
	
	@GET
	@Path("getCellIdsByGeographyL4/{name}")
	public List<String> getCellIdsByGeographyL4(@PathParam("name") String name) {
		logger.info("Going to get distinct sapids By GeographyL4 name {}",name);
		return networkElementService.getCellIdsByGeographyL4(name);
	}
	@GET
	@Path("getSitesBySalesL4/{othergeographyname}")
	public List<String> getSitesBySalesL4(@PathParam("othergeographyname")String othergeographyname){
		return networkElementService.getSitesBySalesL4(othergeographyname);	
	
	}

	/**
	 * Update ne status of network element.
	 *
	 * @param nePk     the ne pk
	 * @param neStatus the ne status
	 * @return the string
	 */
	@GET
	@Path("updateNeStatusOfNetworkElement/{nePk}/{neStatus}")
	public String updateNeStatusOfNetworkElement(@PathParam("nePk") Integer nePk, @PathParam("neStatus") String neStatus) {
		return ineTaskDetailService.updateNetworkElementStatusByPk(nePk, neStatus);
	}
	
	@GET
	@Path("getSitesByGC")
	public List<NetworkElementWrapper> getSitesByGC(@QueryParam("locationId") Integer locationid) {
		List<NetworkElementWrapper> SiteByGCList = new ArrayList<>();
		try {
			logger.info("Going to getSites By locationid : {}" , locationid);
			if ((locationid != null)) {
				SiteByGCList = networkElementService.getSitesInfoByGC(locationid);
			}
		} catch (Exception e) {
			logger.error("Error in getting site By GC :{}", Utils.getStackTrace(e));
		}
		return SiteByGCList;

	}
	
	@GET
	@Path("getProjectIdBySarfId")
	public String getProjectIdBySarfId() {
		logger.info("Going to get ProjectId By SarfId");
		return "SUCCESS";
	}
	
	@GET
	@Path("getfilterByNename")
	public List<String> getfilterByNename(@QueryParam("neName") String neName ,@QueryParam("neType") NEType neType) {
		List<String> filterByHostName = new ArrayList<>();
		try {
			logger.info("Going to get neName and neType  : {},{}", neName,neType);
			if ((neName != null)&&(neType !=null)) {
				filterByHostName = networkElementService.getfilterByNename(neName,neType);
			}
		} catch (Exception e) {
			logger.error("Error in getting Location :{}", Utils.getStackTrace(e));
		}
		return filterByHostName;
	}
}
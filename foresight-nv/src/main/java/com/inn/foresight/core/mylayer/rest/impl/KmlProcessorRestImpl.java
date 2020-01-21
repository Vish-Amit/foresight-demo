package com.inn.foresight.core.mylayer.rest.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.rest.AbstractCXFRestService;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.mylayer.model.KmlProcessor;
import com.inn.foresight.core.mylayer.service.IKmlProcessorService;
import com.inn.foresight.core.mylayer.utils.KmlProcessorWrapper;
import com.inn.foresight.core.mylayer.utils.MyLayerConstants;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;

@Path("/KmlProcessor")
@Produces("application/json")
@Consumes("application/json")
@Service("KmlProcessorRestImpl")
public class KmlProcessorRestImpl  extends AbstractCXFRestService<Long, KmlProcessor> {

	private Logger logger = LogManager.getLogger(KmlProcessorRestImpl.class);

	@Autowired
	private IKmlProcessorService iKmlProcessorService;
	
	public KmlProcessorRestImpl() {
		super(KmlProcessor.class);
	}
	
	@Override
	public List<KmlProcessor> search(KmlProcessor entity) {
		return null;
	}

	@Override
	public KmlProcessor findById(@NotNull Long primaryKey) {
		return null;
	}

	@Override
	public List<KmlProcessor> findAll() {
		return null;
	}

	@Override
	public KmlProcessor create(@Valid KmlProcessor anEntity) {
		return null;
	}

	@Override
	public KmlProcessor update(@Valid KmlProcessor anEntity) {
		return null;
	}

	@Override
	public boolean remove(@Valid KmlProcessor anEntity) {
		return false;
	}

	@Override
	public void removeById(@NotNull Long primaryKey) {
		
	}

	@Override
	public IGenericService<Long, KmlProcessor> getService() {
		return null;
	}

	@Override
	public SearchContext getSearchContext() {
		return null;
	}
	
	/**
	 * Update KML details.
	 *
	 * @param id the id
	 * @param colorCode the color code
	 * @param kmlName the kml name
	 * @return the string
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("updateKMLDetails")
	public KmlProcessor updateKMLDetails(KmlProcessor kmlProcessor)
	{
		if(kmlProcessor.getId() != null)
		{
			User user=UserContextServiceImpl.getUserInContext();
			if(user!=null)
			{
				Integer userid=user.getUserid();
				if(userid != null) {
				logger.info("Going to update kml {} and userid {}",kmlProcessor.getId() ,userid);
				return iKmlProcessorService.updateKMLDetails(userid, kmlProcessor);
				}
				else
					throw new RestException(MyLayerConstants.INVALID_USER);
			}
			else 
				throw new RestException(MyLayerConstants.SESSION_EXPIRED);
		}
		else
			throw new RestException(MyLayerConstants.INVALID_PARAMETERS);
	}
	
	/**
	 * Upload kml file.
	 *
	 * @param inputStream the input stream
	 * @param kmlName the kml name
	 * @param colorCode the color code
	 * @return the kml processor
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("kmlUpload")
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	@Produces({"application/json"})
	public KmlProcessor uploadKmlFile(@Multipart(value = "file") InputStream inputStream, 
			@QueryParam("kmlName") String kmlName,@QueryParam("colorCode") String colorCode) {
		logger.info("Going to upload KML file with fileName {} and colorCode {}" ,kmlName,colorCode);
		User user = UserContextServiceImpl.getUserInContext();
		if (user != null) {
			Integer userid=user.getUserid();
			if(userid!=null)
			{
				if (inputStream != null && kmlName != null) {
					if(kmlName.length()!=0 && kmlName.length()>50)
					{
						throw new RestException("Uploaded file name more than 50 characters is not allowed.");
					}
					else
					{
						return iKmlProcessorService.uploadFileAtDropwizard(inputStream, kmlName, colorCode, userid);
					}
				} else {
					throw new RestException(ForesightConstants.EXCEPTION_INVALID_PARAMS);
				} 
			}else {
				throw new RestException(MyLayerConstants.INVALID_USER);
			}
		} else {
			throw new RestException(ForesightConstants.SESSION_EXPIRED);
		}
	}

	@GET
	@Path("getKmlDataById")
	public KmlProcessor getKmlDataById(@QueryParam("id") Integer id) {
		logger.info("Going to get kml data by id {}", id);
		if(id!=null)
		{
		User user = UserContextServiceImpl.getUserInContext();
		if(user!=null)
		{
				return iKmlProcessorService.getKmlDataById(id);
		}else {
			throw new RestException(MyLayerConstants.SESSION_EXPIRED);
		}
		}else {
			throw new RestException(MyLayerConstants.INVALID_PARAMETERS);
		}
         }
	
	@GET
	@Path("getKmlList")
	public List<KmlProcessorWrapper> getKmlList(@QueryParam("upperLimit") Integer upperLimit, @QueryParam("lowerLimit") Integer lowerLimit) {
		logger.info("Going to get list of kml uploaded by user");
		User user = UserContextServiceImpl.getUserInContext();
		if (user != null) {
			Integer userid=user.getUserid();
			if(userid!=null)
			{
			return iKmlProcessorService.getKmlData(userid, upperLimit, lowerLimit);
			}
			else 
				throw new RestException(MyLayerConstants.INVALID_USER);
		} else {
			throw new RestException(ForesightConstants.SESSION_EXPIRED);
		}
	}
	@GET
	@Path("getListOfKMLBySearchTerm")
	public List<KmlProcessor> getListOfKMLBySearchTerm(@QueryParam("lowerLimit") Integer lowerLimit,@QueryParam("upperLimit") Integer upperLimit,@QueryParam("searchTerm") String searchTerm)
	{
		User user=UserContextServiceImpl.getUserInContext();
		if(user!=null)
		{
			Integer userid=user.getUserid();
			if(userid!=null)
			{
				logger.info("Going to get list of polygons for userid {}",userid);
				return iKmlProcessorService.getListOfKMLBySearchTerm(userid,lowerLimit,upperLimit,searchTerm);
			}
			else 
				throw new RestException(MyLayerConstants.INVALID_USER);
		}
		else 
			throw new RestException(MyLayerConstants.SESSION_EXPIRED);
	}
	@GET
	@Path("getCountsOfKML")
	public Long getCountsOfKML()
	{
		User user=UserContextServiceImpl.getUserInContext();
		if(user!=null)
		{
			Integer userid=user.getUserid();
			if(userid!=null)
			{
				logger.info("Going to get counts of KML for userid {}",userid);
				return iKmlProcessorService.getCountsOfKML(userid);
			}
			else 
				throw new RestException(MyLayerConstants.INVALID_USER);
		}
		else 
			throw new RestException(MyLayerConstants.SESSION_EXPIRED);
	}
	
	@GET
	@Path("getCountsOfKMLBySearchTerm")
	public Long getCountsOfKMLBySearchTerm(@QueryParam("searchTerm") String searchTerm)
	{
		User user=UserContextServiceImpl.getUserInContext();
		if(user!=null)
		{
			Integer userid=user.getUserid();
			if(userid!=null)
			{
				logger.info("Going to get counts of KML for userid {}",userid);
				return iKmlProcessorService.getCountsOfKMLBySearchTerm(userid,searchTerm);
			}
			else 
				throw new RestException(MyLayerConstants.INVALID_USER);
		}
		else 
			throw new RestException(MyLayerConstants.SESSION_EXPIRED);
	}
	@GET
	@Path("deleteKMLDetail")
	public String deleteKMLDetail(@QueryParam("id") Integer id)
	{
			if(id!=null)
			{
				User user=UserContextServiceImpl.getUserInContext();
				if(user!=null)
				{
					Integer userid=user.getUserid();
					if(userid!=null)
					{
						logger.info("Going to delete kml for id {} and for userid {}",id,userid);
						boolean result=iKmlProcessorService.deleteKMLDetailsByID(userid,id);
						if(result)
						{
							return ForesightConstants.SUCCESS_JSON;
						}
						else
							throw new RestException("Unable to delete kml");
					}
					else
						throw new RestException(MyLayerConstants.INVALID_USER);
				}
				else 
					throw new RestException(MyLayerConstants.SESSION_EXPIRED);
			}
			else
				throw new RestException(MyLayerConstants.INVALID_PARAMETERS);
	}

	@GET
	@Path("exportFileForKML")
	@Produces("application/kml")
	public Response exportFileForKML(@QueryParam("id") Integer id)
	{
		if(id!=null)
		{
			User user=UserContextServiceImpl.getUserInContext();
			if(user!=null)
			{
				Integer userid=user.getUserid();
				if(userid!=null)
				{
					logger.info("Going to export File for KML {} and for userid {}",id,userid);
					String fileName=iKmlProcessorService.getFileNameForKML(id);
					String result=iKmlProcessorService.exportFileForKML(id);
					ResponseBuilder response = Response.ok((Object)result);
					response.header("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
					return response.build();
				}
				else
					throw new RestException(MyLayerConstants.INVALID_USER);
			}
			else 
				throw new RestException(MyLayerConstants.SESSION_EXPIRED);
		}
		else
			throw new RestException(MyLayerConstants.INVALID_PARAMETERS);
	}
	
	@GET
	@Path("isKMLFileExistForUser")
	public Map<String,Boolean> isKMLFileExistForUser(@QueryParam("fileName") String fileName)
	{
		User user=UserContextServiceImpl.getUserInContext();
		Map<String,Boolean> result= new HashMap<>();
		if(user!=null)
		{
			Integer userid=user.getUserid();
			if(userid!=null)
			{
				logger.info("Going to check if kml file {} exist for user {}",fileName,userid);
				result.put("Result", iKmlProcessorService.isKMLFileExistForUser(fileName,userid));
				return result ;
			}
			else 
				throw new RestException(MyLayerConstants.INVALID_USER);
		}
		else 
			throw new RestException(MyLayerConstants.SESSION_EXPIRED);
	}

	@GET
	@Path("processKMLFile")
	public String processKMLFile(@QueryParam("kmlName") String kmlName,
			@QueryParam("colorCode") String colorCode, @QueryParam("userid") Integer userid) {
		logger.info("Going to upload KML file with fileName {} and colorCode {} for userid {}", kmlName, colorCode, userid);
		if ( kmlName != null && colorCode != null) {
			String fileContent = null;
			try {
				fileContent = iKmlProcessorService.uploadKmlFile( kmlName, colorCode, userid);
			}catch(Exception e) {
				logger.error("unable to upload: {}",ExceptionUtils.getStackTrace(e));
			}
			return fileContent;
		} else {
			throw new RestException(ForesightConstants.EXCEPTION_INVALID_PARAMS);
		}
	}
}

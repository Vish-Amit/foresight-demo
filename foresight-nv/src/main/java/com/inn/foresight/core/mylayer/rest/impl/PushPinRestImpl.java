package com.inn.foresight.core.mylayer.rest.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.rest.AbstractCXFRestService;
import com.inn.core.generic.service.IGenericService;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.mylayer.model.PushPin;
import com.inn.foresight.core.mylayer.service.IPushPinService;
import com.inn.foresight.core.mylayer.utils.MyLayerConstants;
import com.inn.foresight.core.mylayer.wrapper.DataWrapper;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;


/**
 *     This class is used for MyLayer for SitePlanning Activity functionality.
 */
@Path("/PushPin")
@Produces("application/json")
@Consumes("application/json")
@Service("PushPinRestImpl")
public class PushPinRestImpl extends AbstractCXFRestService<Long, PushPin>
{

	/** The logger. */
	private Logger logger = LogManager.getLogger(PushPinRestImpl.class);
	
	/** The i push pin service. */
	@Autowired
	IPushPinService iPushPinService;
	
	/**
	 * Instantiates a new push pin rest impl.
	 */
	public PushPinRestImpl() {
		super(PushPin.class);
	}

	/**
	 * Search.
	 *
	 * @param entity the entity
	 * @return the list
	 * @throws RestException the rest exception
	 */
	/* 
	 * To search an entity
	 */
	@Override
	public List<PushPin> search(PushPin entity)
	{
		return iPushPinService.search(entity);
	}

	/**
	 * Find all.
	 *
	 * @return the list
	 * @throws RestException the rest exception
	 */
	/* 
	 * To find all data
	 */
	@Override
	public List<PushPin> findAll()
	{
		return iPushPinService.findAll();
	}

	/**
	 * Creates the.
	 *
	 * @param anEntity the an entity
	 * @return the push pin
	 * @throws RestException the rest exception
	 */
	/* 
	 * To create an entity
	 */
	@Override
	public PushPin create(PushPin anEntity)
	{
		return iPushPinService.create(anEntity);
	}

	/**
	 * Update.
	 *
	 * @param anEntity the an entity
	 * @return the push pin
	 * @throws RestException the rest exception
	 */
	/* 
	 * To update an entity
	 */
	@Override
	public PushPin update(PushPin anEntity)
	{
		return iPushPinService.update(anEntity);
	}

	/**
	 * Removes the.
	 *
	 * @param anEntity the an entity
	 * @return true, if successful
	 * @throws RestException the rest exception
	 */
	/* 
	 * to remove an entity
	 */
	@Override
	public boolean remove(PushPin anEntity)
	{
		return false;
	}

	
	/**
	 * Gets the service.
	 *
	 * @return the service
	 */
	@Override
	public IGenericService<Long, PushPin> getService()
	{
		return null;
	}
	
	/**
	 * Find by id.
	 *
	 * @param primaryKey the primary key
	 * @return the push pin
	 * @throws RestException the rest exception
	 */
	/* 
	 * auto generated 
	 */
	@Override
	public PushPin findById(Long primaryKey)
	{
		return null;
	}

	/**
	 * Removes the by id.
	 *
	 * @param primaryKey the primary key
	 * @throws RestException the rest exception
	 */
	@Override
	public void removeById(Long primaryKey)
	{
		//empty
	}

	/**
	 * Gets the search context.
	 *
	 * @return the search context
	 */
	@Override
	public SearchContext getSearchContext()
	{
		return null;
	}
	
	/**
	 * Update group name.
	 *
	 * @param opacity the opacity
	 * @param colorCode the color code
	 * @param oldPinStatus the old pin status
	 * @param newPinStatus the new pin status
	 * @param imageName the image name
	 * @param oldGroupName the old group name
	 * @param newGroupName the new group name
	 * @return the string
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("updateGroupName")
	public String updateGroupName(List<DataWrapper> dataWrapper)
	{
		if(dataWrapper != null)
		{
			User user=UserContextServiceImpl.getUserInContext();
			if(user!=null)
			{
				Integer userid=user.getUserid();
				if(userid!=null)
				{
					logger.info("Going to update Group for userid {}",userid);
					boolean result=iPushPinService.updateGroupName(userid,dataWrapper);
					if(result)
					{
						return ForesightConstants.SUCCESS_JSON;
					}
					else
						throw new RestException("Unable to update group name.");
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
	 * Upload file.
	 *
	 * @param inputStream the input stream
	 * @param fileName the file name
	 * @return the map
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("uploadFile")
	@Consumes("multipart/form-data")
	@Produces("application/json")
	public Map uploadFile(@Multipart(value = "file") InputStream inputStream, @QueryParam("fileName") String fileName) {
		logger.info("Going to upload file with fileName {}", fileName);
		if (inputStream != null && fileName != null) {
			User user = UserContextServiceImpl.getUserInContext();
			if (user != null) {
				return iPushPinService.uploadFile(inputStream, fileName, user);
			} else {
				throw new RestException(MyLayerConstants.SESSION_EXPIRED);
			}
		} else {
			throw new RestException(MyLayerConstants.INVALID_PARAMETERS);
		}
	}
	
	/**
	 * Gets the pin names for group names.
	 *
	 * @param groupNames the group names
	 * @return the pin names for group names
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("getPinsNames")
	public List<String> getPinNamesForGroupNames(List<String> groupNames)
	{
		if(groupNames!=null && !groupNames.isEmpty())
		{
			logger.info("Going to get pin names for total groups {}",groupNames.size());
			User user=UserContextServiceImpl.getUserInContext();
			if(user!=null)
			{
				Integer userid=user.getUserid();
				if(userid!=null)
				{
					logger.info("Going to pin names for {} groupnames for userid {}",groupNames,userid);
					return iPushPinService.getPinNamesForGroupNames(groupNames, userid);
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
	 * Shared push pin group.
	 *
	 * @param groupName the group name
	 * @param emailId the email id
	 * @return the string
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("sharePushPinGroup")
	public String sharedPushPinGroup(@QueryParam("groupName") String groupName,@QueryParam("emailId") String emailId)
	{
		if(groupName!=null && emailId!=null)
		{
			User user=UserContextServiceImpl.getUserInContext();
			if(user!=null)
			{
			   logger.info("Going to share pushpin group {} to emailid {}",groupName,emailId);
			   try
			   {
			   iPushPinService.sharedPushPinGroup(user, groupName, emailId);
			   return ForesightConstants.SUCCESS_JSON;
			   }catch(RestException restException)
			   {
				   throw new RestException(restException.getMessage());
			   }
			}
			else 
				throw new RestException(MyLayerConstants.SESSION_EXPIRED);
		}
		else
			throw new RestException(MyLayerConstants.INVALID_PARAMETERS);
	}
	
	/**
	 * Gets the data in excel file.
	 * @param wrappers the wrappers
	 * @return the data in excel file
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("getDataInExcel")
	public String getDataInExcelFile(List<DataWrapper> wrappers) {
		logger.info("Going to download excel sheet for Site Plan using wrapper "+ wrappers);
		if (wrappers != null && !wrappers.isEmpty())
			return "{\"excelFilePath\":\"" + iPushPinService.getDataInExcel(wrappers)
					+ "\"}";
		else
			throw new RestException(MyLayerConstants.INVALID_PARAMETERS);
	}

	@GET
	@Path("getPinGroupDetails")
	public List<DataWrapper> getPinGroupDetails(@QueryParam("lowerLimit") Integer lowerLimit,@QueryParam("upperLimit") Integer upperLimit)
	{
		User user=UserContextServiceImpl.getUserInContext();
		if(user!=null)
		{
			Integer userid=user.getUserid();
			if(userid!=null)
			{
				logger.info("Going to get pin details for userid {}",userid);
				return iPushPinService.getPinGroupDetails(userid, lowerLimit,upperLimit);
			}
			else
				throw new RestException(MyLayerConstants.INVALID_USER);
		}
		else 
			throw new RestException(MyLayerConstants.SESSION_EXPIRED);
	}
	@GET
	@Path("getPinGroupDetailsBySearchTerm")
	public List<DataWrapper> getPinGroupDetailsBySearchTerm(@QueryParam("lowerLimit") Integer lowerLimit,@QueryParam("upperLimit") Integer upperLimit,@QueryParam("groupName") String groupName)
	{
		User user=UserContextServiceImpl.getUserInContext();
		if(user!=null)
		{
			Integer userid=user.getUserid();
			if(userid!=null)
			{
				logger.info("Going to get pin details for userid {}",userid);
				return iPushPinService.getPinGroupDetailsBySearchTerm(userid,lowerLimit,upperLimit,groupName);
			}
			else
				throw new RestException(MyLayerConstants.INVALID_USER);
		}
		else 
			throw new RestException(MyLayerConstants.SESSION_EXPIRED);
	}
	
	@POST
	@Path("insertPinsData")
	public Map<String, String> insertPinsData(List<DataWrapper> pins)
	{
		if(pins!=null && !pins.isEmpty())
		{
			User user=UserContextServiceImpl.getUserInContext();
			if(user!=null)
			{
				logger.info("Going to insert {} export pins",pins.size());
				return iPushPinService.insertPinsData(user, pins);
			}
			else 
				throw new RestException(MyLayerConstants.SESSION_EXPIRED);
		}
		else
			throw new RestException(MyLayerConstants.INVALID_PARAMETERS);
	}
	@GET
	@Path("getPinGroupListCount")
	public Long getPinGroupListCount()
	{
		User user=UserContextServiceImpl.getUserInContext();
		if(user!=null)
		{
			Integer userid=user.getUserid();
			if(userid!=null)
			{
				logger.info("Going to get counts of polygons for userid {}",userid);
				return iPushPinService.getPinGroupListCount(userid);
			}
			else 
				throw new RestException(MyLayerConstants.INVALID_USER);
		}
		else 
			throw new RestException(MyLayerConstants.SESSION_EXPIRED);
	}
	
	@GET
	@Path("getPinGroupListCountBySearchterm")
	public Long getPinGroupListCountBySearchterm(@QueryParam("searchTerm") String searchTerm)
	{
		User user=UserContextServiceImpl.getUserInContext();
		if(user!=null)
		{
			Integer userid=user.getUserid();
			if(userid!=null)
			{
				logger.info("Going to get counts of polygons for userid {}",userid);
				return iPushPinService.getPinGroupListCountBySearchterm(userid,searchTerm);
			}
			else 
				throw new RestException(MyLayerConstants.INVALID_USER);
		}
		else 
			throw new RestException(MyLayerConstants.SESSION_EXPIRED);
	}
	
	@GET
	@Path("deleteGroupName")
	public String deleteGroupName(@QueryParam("groupName") String groupName)
	{
		if(groupName!=null)
		{
			User user=UserContextServiceImpl.getUserInContext();
			if(user!=null)
			{
				Integer userid=user.getUserid();
				if(userid!=null)
				{
					logger.info("Going to delete groupName {} for userid {} ",groupName,userid);
					boolean result=iPushPinService.deleteGroupName(userid,groupName);
					if(result)
					{
						return ForesightConstants.SUCCESS_JSON;
					}
					else
						throw new RestException("Unable to delete group name.");
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
	@Path("deletePinFromGroup")
	public String deletePinFromGroup(@QueryParam("id") Integer id)
	{
		if(id!=null)
		{
			User user=UserContextServiceImpl.getUserInContext();
			if(user!=null)
			{
				Integer userid=user.getUserid();
				if(userid!=null)
				{
					logger.info("Going to delete pin id {} for userid {}",id,userid);
					boolean result=iPushPinService.deletePinFromGroup(userid,id);
					if(result)
					{
						return ForesightConstants.SUCCESS_JSON;
					}
					else
						throw new RestException("Unable to delete pin from group.");
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
	@Path("deletePinsByStatus")
	public String deletePinsByPinStatus(@QueryParam("groupName") String groupName,@QueryParam("pinStatus") String pinStatus)
	{
		if(groupName!=null && pinStatus!=null)
		{
			User user=UserContextServiceImpl.getUserInContext();
			if(user!=null)
			{
				Integer userid=user.getUserid();
				if(userid!=null)
				{
					logger.info("Going to delete pins from group {} having pinstatus {} for userid {}",groupName,pinStatus,userid);
					boolean result=iPushPinService.deletePinsByPinStatus(userid,groupName,pinStatus);
					if(result)
					{
						return ForesightConstants.SUCCESS_JSON;
					}
					else
						throw new RestException("Unable to delete pins from group.");
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
	@POST
	@Path("getPinsByGroupName")
	public List<String> getAllPinsByGroupName(List<String> groupName)
	{
		if(groupName!=null)
		{
			User user=UserContextServiceImpl.getUserInContext();
			if(user!=null)
			{
				Integer userid=user.getUserid();
				if(userid!=null)
				{
					logger.info("Going to get all pins for group name {}",groupName);
					return iPushPinService.getPinNameByGroupName(userid,groupName);
				}
				else
					throw new RestException(MyLayerConstants.INVALID_USER);
			}
			else 
				throw new RestException(MyLayerConstants.SESSION_EXPIRED);
		}
		else
			throw new RestException("Invalid parameter");
	}	
	@GET
	@Path("getPinGroupData")
	public List<DataWrapper> getPinGroupData(@QueryParam("groupName") String groupName)
	{
		User user=UserContextServiceImpl.getUserInContext();
		if(user!=null)
		{
			Integer userid=user.getUserid();
			if(userid!=null)
			{
				logger.info("Going to get pin group data for groupName {},userid {}",groupName,userid);
				return iPushPinService.getPinGroupData(userid, groupName);
			}
			else
				throw new RestException(MyLayerConstants.INVALID_USER);
		}
		else 
			throw new RestException(MyLayerConstants.SESSION_EXPIRED);
	}
	@POST
	@Path("searchPinDetails")
	public List<DataWrapper> searchPinDetails(List<String> groupName,@QueryParam("pinName") String pinName)
	{
		User user=UserContextServiceImpl.getUserInContext();
		if(user!=null)
		{
			Integer userid=user.getUserid();
			if(userid!=null)
			{
				logger.info("Going to get pin details for pinName :{},groupName: {} ,userid {}",pinName,groupName,userid);
				return iPushPinService.searchPinDetails(userid, groupName,pinName);
			}
			else
				throw new RestException(MyLayerConstants.INVALID_USER);
		}
		else 
			throw new RestException(MyLayerConstants.SESSION_EXPIRED);
	}
	
	@GET
	@Path("getPinStatusList")
	public List<String> getPinStatusList()
	{
		User user=UserContextServiceImpl.getUserInContext();
		if(user!=null)
		{
			Integer userid=user.getUserid();
			if(userid!=null )
			{
				return iPushPinService.getPinStatusList(userid);
			}
			else
				throw new RestException(MyLayerConstants.INVALID_USER);
	}
		return null;
	}
	
	@GET
	@Path("getPinStatusByGroupName")
	public List<DataWrapper> getPinStatusByGroupName(@QueryParam("groupName") String groupName)
	{
		long startTime = System.nanoTime();
		User user=UserContextServiceImpl.getUserInContext();
		if(user!=null)
		{
			Integer userid=user.getUserid();
			if(userid!=null)
			{
				if(groupName != null ) {
					List<DataWrapper> dataWrapperList=iPushPinService.getPinStatusByGroupName(userid,groupName);
					logger.info("getPinStatusByGroupName: {}",dataWrapperList.size());
					long endTime = System.nanoTime();
					logger.info("total time: {}",(endTime - startTime)/1000000);
				return dataWrapperList;
				}
				else
					throw new RestException("Invalid Group Name.");
			}
			else
				throw new RestException(MyLayerConstants.INVALID_USER);
	}
		return null;
	}
	@GET
	@Path("getPinGroupNameList")
	public List<String> getPinGroupNameList()
	{
		User user=UserContextServiceImpl.getUserInContext();
		if(user!=null)
		{
			Integer userid=user.getUserid();
			if(userid!=null)
			{
				logger.info("Going to get group name for userid {}",userid);
				return iPushPinService.getPinGroupNameList(userid);
			}
			else
				throw new RestException(MyLayerConstants.INVALID_USER);
		}
		else 
			throw new RestException(MyLayerConstants.SESSION_EXPIRED);
	}
	@GET
	@Path("getFileByName")
	@Produces("application/excel")
	public Response getFileByPath(@QueryParam("path") String fileName) {
		logger.info("Going to get file by name " + fileName);
		ResponseBuilder response = null;
		try {
			response = Response.ok(iPushPinService.getFileByPath(fileName));
			response.header("Content-Disposition", "attachment; fileName=\""
					+ fileName + "\"");
		} catch (Exception e) {
			logger.error("Error in getting file Exception {} ",Utils.getStackTrace(e));
			throw new RestException("Error In Getting File.");
		}
		return response.build();
	}
	
	@GET
	@Path("exportKMLFile")
	@Produces("application/kml")
	public Response exportPinsInKML(@QueryParam("groupName") String groupName)
	{
		if(groupName!=null)
		{
			User user=UserContextServiceImpl.getUserInContext();
			if(user!=null)
			{
				Integer userid=user.getUserid();
				if(userid!=null)
				{
					logger.info("Going to export pins in KML file for groupName {} and for userid {}",groupName,userid);
					String fileName=iPushPinService.getPushPinFileName(groupName);
					byte[] kmlContent=iPushPinService.exportPinsInKML(userid,groupName);
					ResponseBuilder response = Response.ok(kmlContent);
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
	@Path("getPinGroupDataBySearchTerm")
	public List<DataWrapper> getPinGroupDataBySearchTerm(@QueryParam("groupName") String groupName,@QueryParam("lowerLimit") Integer lowerLimit,@QueryParam("upperLimit") Integer upperLimit)
	{
		User user=UserContextServiceImpl.getUserInContext();
		if(user!=null)
		{
			Integer userid=user.getUserid();
			if(userid!=null)
			{
				logger.info("Going to get list of pins for userid {}",userid);
				return iPushPinService.getPinGroupData(userid,groupName,lowerLimit,upperLimit);
			}
			else 
				throw new RestException(MyLayerConstants.INVALID_USER);
		}
		else 
			throw new RestException(MyLayerConstants.SESSION_EXPIRED);
	}
	@GET
	@Path("getListOfPinsBySearchTerm")
	public List<DataWrapper> getListOfPinsBySearchTerm(@QueryParam("lowerLimit") Integer lowerLimit,@QueryParam("upperLimit") Integer upperLimit,@QueryParam("searchTerm") String searchTerm,@QueryParam("groupName") String groupName)
	{
		User user=UserContextServiceImpl.getUserInContext();
		if(user!=null)
		{
			Integer userid=user.getUserid();
			if(userid!=null)
			{
				logger.info("Going to get list of polygons for userid {}",userid);
				return iPushPinService.getPinsDetailsBySearchTerm(userid,lowerLimit,upperLimit,searchTerm,groupName);
			}
			else 
				throw new RestException(MyLayerConstants.INVALID_USER);
		}
		else 
			throw new RestException(MyLayerConstants.SESSION_EXPIRED);
	}
	
	@GET
	@Path("getCountsOfPins")
	public Map<String,Long> getCountsOfPins(@QueryParam("groupName") String groupName)
	{
		User user=UserContextServiceImpl.getUserInContext();
		Map<String,Long> countsOfPins= new HashMap<>();
		if(user!=null)
		{
			Integer userid=user.getUserid();
			if(userid!=null)
			{
				logger.info("Going to get counts of pins for userid {}",userid);
				countsOfPins.put("Counts", iPushPinService.getCountsOfPinsByGroupName(userid,groupName));
				return countsOfPins;
			}
			else 
				throw new RestException(MyLayerConstants.INVALID_USER);
		}
		else 
			throw new RestException(MyLayerConstants.SESSION_EXPIRED);
	}
	
	@GET
	@Path("getCountsOfPinsBySearchTerm")
	public Map<String,Long> getCountsOfPinsBySearchTerm(@QueryParam("groupName") String groupName,@QueryParam("searchTerm") String searchTerm)
	{
		User user=UserContextServiceImpl.getUserInContext();
		Map<String,Long> countsOfPins= new HashMap<>();
		if(user!=null)
		{
			Integer userid=user.getUserid();
			if(userid!=null)
			{
				logger.info("Going to get counts of pins for userid {}",userid);
				countsOfPins.put("Counts", iPushPinService.getCountsOfPinsByGroupName(userid,groupName,searchTerm));
				return countsOfPins ;
			}
			else 
				throw new RestException(MyLayerConstants.INVALID_USER);
		}
		else 
			throw new RestException(MyLayerConstants.SESSION_EXPIRED);
	}
	
	@GET
	@Path("getPinDetailsForGroupName")
	public PushPin getPinDetailsForGroupName(@QueryParam("groupName") String groupName,@QueryParam("id") Integer id)
	{
		User user=UserContextServiceImpl.getUserInContext();
		if(user!=null)
		{
			Integer userid=user.getUserid();
			if(userid!=null)
			{
				logger.info("Going to get pin details for Groupname: {} , userid: {}",groupName,userid);
				return iPushPinService.getPinDetailsForGroupName(userid,groupName,id) ;
			}
			else 
				throw new RestException(MyLayerConstants.INVALID_USER);
		}
		else 
			throw new RestException(MyLayerConstants.SESSION_EXPIRED);
	}
	
	@GET
	@Path("isFileNameExistForUser")
	public Map<String,Boolean> isFileNameExistForUser(@QueryParam("fileName") String fileName)
	{
		User user=UserContextServiceImpl.getUserInContext();
		Map<String,Boolean> result= new HashMap<>();
		if(user!=null)
		{
			Integer userid=user.getUserid();
			if(userid!=null)
			{
				logger.info("Going to check if filename {} exist for user {}",fileName,userid);
				result.put("Result", iPushPinService.isGroupExistForUser(fileName,userid));
				return result ;
			}
			else 
				throw new RestException(MyLayerConstants.INVALID_USER);
		}
		else 
			throw new RestException(MyLayerConstants.SESSION_EXPIRED);
	}
	
}

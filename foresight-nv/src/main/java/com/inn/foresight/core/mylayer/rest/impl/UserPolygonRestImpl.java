package com.inn.foresight.core.mylayer.rest.impl;

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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.rest.AbstractCXFRestService;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.mylayer.model.UserPolygon;
import com.inn.foresight.core.mylayer.service.IUserPolygonService;
import com.inn.foresight.core.mylayer.utils.MyLayerConstants;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;

@Path("/UserPolygon")
@Produces("application/json")
@Consumes("application/json")
@Service("UserPolygonRestImpl")
public class UserPolygonRestImpl extends AbstractCXFRestService<Long, UserPolygon>{

	private Logger logger = LogManager.getLogger(UserPolygonRestImpl.class);

	public UserPolygonRestImpl() {
		super(UserPolygon.class);
	}
	
	@Autowired 
	private IUserPolygonService iUserPolygonService;

	@Override
	public List<UserPolygon> search(UserPolygon entity) {
		return null;
	}

	@Override
	public UserPolygon findById(@NotNull Long primaryKey) {
		return null;
	}

	@Override
	public List<UserPolygon> findAll() {
		return null;
	}

	@Override
	public UserPolygon create(@Valid UserPolygon anEntity) {
		return null;
	}

	@Override
	public UserPolygon update(@Valid UserPolygon anEntity) {
		return null;
	}

	@Override
	public boolean remove(@Valid UserPolygon anEntity) {
		return false;
	}

	@Override
	public void removeById(@NotNull Long primaryKey) {
		
	}

	@Override
	public IGenericService<Long, UserPolygon> getService() {
		return null;
	}

	@Override
	public SearchContext getSearchContext() {
		return null;
	}
	
	@POST
	@Path("insertPolygonDetail")
	public UserPolygon insertPolygonDetail(UserPolygon userPolygon)
	{
		if(userPolygon!=null)
		{
			User user=UserContextServiceImpl.getUserInContext();
			if(user!=null)
			{
				logger.info("Going to insert polygon detail");
				return iUserPolygonService.insertPolygonDetail(user, userPolygon);
			}
			else 
				throw new RestException(MyLayerConstants.SESSION_EXPIRED);
		}
		else 
			throw new RestException(MyLayerConstants.INVALID_PARAMETERS);
	}
	
	@POST
	@Path("updatePolygonDetail")
	public UserPolygon updatePolygonDetail(UserPolygon userPolygon)
	{
		if(userPolygon!=null)
		{
			User user=UserContextServiceImpl.getUserInContext();
			if(user!=null)
			{
				Integer userid=user.getUserid();
				if(userid!=null)
				{
					try
					{
						logger.info("Going to update polygon detail for userid {}",userid);
						return iUserPolygonService.updatePolygonPoint(user, userPolygon);
					}catch(RestException restException)
					{
						throw new RestException(restException.getMessage());
					}
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
	@Path("deletePolygonById")
	public String deletePolygonById(@QueryParam("id") Integer id)
	{
			if(id!=null)
			{
				User user=UserContextServiceImpl.getUserInContext();
				if(user!=null)
				{
					Integer userid=user.getUserid();
					if(userid!=null)
					{
						logger.info("Going to delete polygon for id {} and for userid {}",id,userid);
						boolean result=iUserPolygonService.deletePolygonById(userid,id);
						if(result)
						{
							return ForesightConstants.SUCCESS_JSON;
						}
						else
							throw new RestException("Unable to delete polygon");
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
	

	@SuppressWarnings("rawtypes")
	@GET
	@Path("getListOfPolygon")
	public List<Map> getListOfPolygon(@QueryParam("lowerLimit") Integer lowerLimit,@QueryParam("upperLimit") Integer upperLimit)
	{
		User user=UserContextServiceImpl.getUserInContext();
		if(user!=null)
		{
			Integer userid=user.getUserid();
			if(userid!=null)
			{
				logger.info("Going to get list of polygon for userid {}",userid);
				return iUserPolygonService.getListOfPolygon(userid,lowerLimit,upperLimit,null);
			}
			else 
				throw new RestException(MyLayerConstants.INVALID_USER);
		}
		else 
			throw new RestException(MyLayerConstants.SESSION_EXPIRED);
	}

	@SuppressWarnings("rawtypes")
	@GET
	@Path("getListOfPolygonBySearchTerm")
	public List<Map> getListOfPolygonBySearchTerm(@QueryParam("lowerLimit") Integer lowerLimit,@QueryParam("upperLimit") Integer upperLimit,@QueryParam("searchTerm") String searchTerm)
	{
		User user=UserContextServiceImpl.getUserInContext();
		if(user!=null)
		{
			Integer userid=user.getUserid();
			if(userid!=null)
			{
				logger.info("Going to get list of polygons for userid {}",userid);
				return iUserPolygonService.getListOfPolygon(userid,lowerLimit,upperLimit,searchTerm);
			}
			else 
				throw new RestException(MyLayerConstants.INVALID_USER);
		}
		else 
			throw new RestException(MyLayerConstants.SESSION_EXPIRED);
	}

	@GET
	@Path("getCountsOfPolygon")
	public Long getCountsOfPolygon()
	{
		User user=UserContextServiceImpl.getUserInContext();
		if(user!=null)
		{
			Integer userid=user.getUserid();
			if(userid!=null)
			{
				logger.info("Going to get counts of polygons for userid {}",userid);
				return iUserPolygonService.getCountsOfPolygon(userid,null);
			}
			else 
				throw new RestException(MyLayerConstants.INVALID_USER);
		}
		else 
			throw new RestException(MyLayerConstants.SESSION_EXPIRED);
	}
	
	@GET
	@Path("getCountsOfPolygonBySearchTerm")
	public Long getCountsOfPolygonBySearchTerm(@QueryParam("searchTerm") String searchTerm)
	{
		User user=UserContextServiceImpl.getUserInContext();
		if(user!=null)
		{
			Integer userid=user.getUserid();
			if(userid!=null)
			{
				logger.info("Going to get counts of polygons for userid {}",userid);
				return iUserPolygonService.getCountsOfPolygon(userid,searchTerm);
			}
			else 
				throw new RestException(MyLayerConstants.INVALID_USER);
		}
		else 
			throw new RestException(MyLayerConstants.SESSION_EXPIRED);
	}
	
	@GET
	@Path("getPolygonById")
	public Map getPolygonById(@QueryParam("id") Integer id)
	{
		if(id!=null)
		{
			User user=UserContextServiceImpl.getUserInContext();
			if(user!=null)
			{
				Integer userid=user.getUserid();
				if(userid!=null)
				{
					logger.info("Going to get list of polygons for userid {}",userid);
					return iUserPolygonService.getPolygonById(userid,id);
				}
				else 
					throw new RestException(MyLayerConstants.INVALID_USER);
			}
			else 
				throw new RestException(MyLayerConstants.SESSION_EXPIRED);
		}
		else 
			throw new RestException("Invalid parameters");
	}
	
	@GET
	@Path("exportKMLFile")
	@Produces("application/kml")
	public Response exportPolygonInKML(@QueryParam("id") Integer id)
	{
		if(id!=null)
		{
			User user=UserContextServiceImpl.getUserInContext();
			if(user!=null)
			{
				Integer userid=user.getUserid();
				if(userid!=null)
				{
					logger.info("Going to export polygon points in KML file for id {} and for userid {}",id,userid);
					String fileName=iUserPolygonService.getKmlFileName(id);
					String result=iUserPolygonService.exportPolygonInKML(id);
					ResponseBuilder response = Response.ok((Object)result);
					response.header("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
					return response.build();
				}
				else
					throw new RestException("Invalid user");
			}
			else 
				throw new RestException(MyLayerConstants.SESSION_EXPIRED);
		}
		else
			throw new RestException(MyLayerConstants.INVALID_PARAMETERS);
	}
	
@POST
@Path("updatePolygonProperties")
public UserPolygon updatePolygonProperties(UserPolygon userPolygon)
{
	if(userPolygon.getId() != null)
	{
		User user=UserContextServiceImpl.getUserInContext();
		if(user!=null)
		{
			Integer userid=user.getUserid();
			if(userid!=null)
			{
				try
				{
					logger.info("Going to update polygon detail for userid {}",userid);
					return iUserPolygonService.updatePolygonProperties(user.getUserid(), userPolygon);
				}catch(RestException restException)
				{
					throw new RestException(restException.getMessage());
				}
			}
			else
				throw new RestException("Invalid user");
		}
		else 
			throw new RestException(MyLayerConstants.SESSION_EXPIRED);
	}
	else 
		throw new RestException(MyLayerConstants.INVALID_PARAMETERS);
}
}
package com.inn.foresight.core.um.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
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

import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.rest.AbstractCXFRestService;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.um.service.CustomUserService;
import com.inn.product.um.user.model.User;


@Path("/CustomUser")
@Produces("application/json")
@Consumes("application/json")
public class CustomUserRestImpl  extends AbstractCXFRestService<Integer, User> {

	/** The logger. */
	private Logger logger = LogManager.getLogger(CustomUserRestImpl.class);

	/**
	 * Instantiates a new custom user rest impl.
	 */
	
	@Autowired
    CustomUserService customUserService;
	
	
	public CustomUserRestImpl() {
		super(User.class);
	}

	@Override
	public User create(@Valid User arg0)  {
		return null;
	}

	@Override
	public List<User> findAll() {
		return new ArrayList<>();
	}

	@Override
	public User findById(@NotNull Integer arg0)  {
		return null;
	}

	@Override
	public boolean remove(@Valid User arg0)  {
		return false;
	}

	

	@Override
	public List<User> search(User arg0)  {
		return new ArrayList<>();
	}

	@Override
	public User update(@Valid User arg0) {
		return null;
	}

	@Override
	public SearchContext getSearchContext() {
		return null;
	}

	@Override
	public IGenericService<Integer, User> getService() {
		return null;
	}

	@POST
	@Path("createBulkUser")
	@Consumes("multipart/form-data")
	public Map<String,String> createBulkUser(@Multipart(value = "file") InputStream inputStream,@QueryParam("type") String type) {
		return customUserService.createBulkUser(inputStream,null,type);
	}
	
	@POST
	@Path("enableDisableBulkUser")
	@Consumes("multipart/form-data")
	public Map<String,String> enableDisableBulkUser(@Multipart(value = "file") InputStream inputStream,@QueryParam("actionType")String actionType) {
		logger.info("Inside enableDisableBulkUser");
		return customUserService.enableDisableBulkUser(inputStream,actionType,null);
	}
    
	@Override
	public void removeById(@NotNull Integer arg0) {
		/**/
	}
	
	@GET
	@Path("createUsers")
	public Map<String, String> createUsers(@QueryParam("filePath") String filePath) {
		try {
			logger.info("Inside createUsers");
			logger.info("filePath : {} ", filePath);
			return customUserService.createBulkUser(new FileInputStream(new File(filePath)),filePath,null);
		} catch (Exception e) {
			throw new RestException(e.getMessage());
		}
	}
	
	
	@GET
	@Path("enableDisableUsers")
	public Map<String, String> enableDisableUsers(@QueryParam("filePath") String filePath,@QueryParam("actionType") String actionType) {
		try {
			logger.info("Inside enableDisableUsers");
			logger.info("filePath : {} ", filePath);
			return customUserService.enableDisableBulkUser(new FileInputStream(new File(filePath)), actionType,filePath);
		} catch (Exception e) {
			throw new RestException(e.getMessage());
		}
	}
	
}
